#include <algorithm>
#include <iostream>
#include <sstream>
#include <vector>

#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/ml/ml.hpp>

#include "operations.hpp"


using cv::Algorithm;
using cv::Mat;
using cv::Point;
using cv::Ptr;
using cv::Rect;
using cv::Scalar;
using cv::Size;
using cv::Vec4i;
using cv::ml::KNearest;
using std::cout;
using std::endl;
using std::ostringstream;
using std::vector;


const static char * const WINDOW_NAME_ORIG = "Original image";
const static char * const WINDOW_NAME_PROC = "Processed image";
const static char * const WINDOW_NAME_ROIS = "Regions of interest";
const static char * const WINDOW_NAME_RESP = "Responses";
const static int SAMPLE_ROWS = 28;
const static int SAMPLE_COLS = 28;


void usage(const std::string& exec_name) {
    cout << "Usage: " << exec_name << " <image> <params>\n"
         << "  <image> path to the sample image\n"
         << "  <params> path to the parameters of trained classifier in XML format\n"
         << endl;
}

// extracts regions of interest
vector<Mat> preprocess(const Mat& src_mat, Mat& dst_mat) {
    // preprocessing
    cv::blur(src_mat, dst_mat, Size(3, 3));
    adaptiveThreshold(dst_mat, dst_mat, 255, cv::ADAPTIVE_THRESH_MEAN_C,
                      cv::THRESH_BINARY_INV, 11, 5);

    // find and draw contours
    Mat tmp_mat;
    dst_mat.copyTo(tmp_mat);
    vector<vector<Point> > contours;
    vector<Vec4i> hierarchy;
    cv::findContours(tmp_mat, contours, hierarchy,
        CV_RETR_CCOMP, CV_CHAIN_APPROX_SIMPLE);
    vector<Mat> rois;
    for (int i = 0; i < contours.size(); i = hierarchy[i][0]) {
        Rect roi_rect = cv::boundingRect(contours[i]);
        // cv::rectangle(dst_mat, roi.tl(), roi.br(), Scalar(100, 100, 100), 2, 8, 0);
        rois.push_back(dst_mat(roi_rect));
    }

    return rois;
}

vector<Mat> normalize(const vector<Mat>& rois) {
    vector<Mat> normalized_rois;
    for (int i = 0; i < rois.size(); i++) {
        const Mat& roi = rois[i];
        Mat scaled_roi(0, 0, CV_8U);
        // cv::resize(rois[i], normalized_roi, normalized_roi.size(), 0, 0, cv::INTER_NEAREST);
        double scale_factor =
            std::min(double(SAMPLE_ROWS) / roi.rows,
                     double(SAMPLE_COLS) / roi.cols);
        cv::resize(roi, scaled_roi, Size(), scale_factor, scale_factor, cv::INTER_NEAREST);
        Mat normalized_roi = Mat::zeros(SAMPLE_ROWS, SAMPLE_ROWS, CV_8U);
        Operations::mergeCentered(normalized_roi, scaled_roi, normalized_roi);
        normalized_rois.push_back(normalized_roi);
    }
    return normalized_rois;
}

Mat mergeDataset(const vector<Mat>& src_vec) {
    Mat dataset;

    int n_images = src_vec.size();
    if (n_images == 0) {
        return dataset;
    }
    int n_rows = src_vec[0].rows;
    int n_cols = src_vec[0].cols;

    dataset = Mat(n_images, n_rows * n_cols, CV_32F);
    for (int i = 0; i < n_images; i++) {
        int rc = 0;
        for (int r = 0; r < n_rows; r++) {
            for (int c = 0; c < n_cols; c++) {
                dataset.at<float>(i, rc) = src_vec[i].at<uchar>(r, c);
                rc++;
            }
        }
    }
    return dataset;
}

Mat makePreview(const vector<Mat>& src_vec) {
    Mat preview;

    int n_images = src_vec.size();
    if (n_images == 0) {
        return preview;
    }
    int n_rows = src_vec[0].rows;
    int n_cols = src_vec[0].cols;

    preview = Mat(n_rows, n_images * n_cols, CV_32F);
    int n_cols_offset = 0;
    for (int i = 0; i < n_images; i++) {
        src_vec[i].copyTo(preview
                          .rowRange(0, n_rows)
                          .colRange(n_cols_offset, n_cols_offset + n_cols));
        n_cols_offset += n_cols;
    }
    return preview;
}

Mat drawResponses(const Mat& responses, const vector<Mat>& rois) {
    int n_rois = rois.size();
    if (n_rois == 0) {
        return Mat();
    }

    int n_rows = rois[0].rows;
    int n_cols = rois[0].cols;

    vector<Mat> preview_responses;
    for (int i = 0; i < n_rois; i++) {
        Mat preview_response = Mat::zeros(n_rows, n_cols, CV_32F);
        ostringstream oss;
        oss << responses.at<float>(i, 0);
        cv::putText(preview_response, oss.str(), Point(0, n_rows),
                    cv::FONT_HERSHEY_COMPLEX_SMALL, 1.0, Scalar(255));
        preview_responses.push_back(preview_response);
    }
    return makePreview(preview_responses);
}

int main(int argc, char** argv) {
  if (argc != 3) {
      usage(argv[0]);
      return -1;
  }

  cout << "Path to sample image:          " << argv[1] << "\n"
       << "Path to classifier parameters: " << argv[2] << "\n"
       << endl;

  Mat preview_src = cv::imread(argv[1], CV_LOAD_IMAGE_UNCHANGED);
  if (preview_src.empty()) {
    cout << "Unable to load image" << endl;
    usage(argv[0]);
    return -1;
  }

  Mat preview_dst;
  cv::cvtColor(preview_src, preview_dst, cv::COLOR_BGR2GRAY);
  vector<Mat> rois = preprocess(preview_dst, preview_dst);

  // prepare dataset
  vector<Mat> norm_rois = normalize(rois);
  Mat merged_rois = mergeDataset(norm_rois);
  Mat preview_rois = makePreview(norm_rois);
  // setup classifier
  Ptr<KNearest> classifier = Algorithm::load<KNearest>(argv[2]);

  Mat test_results(0, 0, CV_32F);
  classifier->findNearest(merged_rois, classifier->getDefaultK(), test_results);
  Mat preview_responses = drawResponses(test_results, norm_rois);

  cv::namedWindow(WINDOW_NAME_ORIG, CV_WINDOW_AUTOSIZE);
  cv::imshow(WINDOW_NAME_ORIG, preview_src);
  cv::namedWindow(WINDOW_NAME_PROC, CV_WINDOW_AUTOSIZE);
  cv::imshow(WINDOW_NAME_PROC, preview_dst);
  cv::namedWindow(WINDOW_NAME_ROIS, CV_WINDOW_AUTOSIZE);
  cv::imshow(WINDOW_NAME_ROIS, preview_rois);
  cv::namedWindow(WINDOW_NAME_RESP, CV_WINDOW_AUTOSIZE);
  cv::imshow(WINDOW_NAME_RESP, preview_responses);

  while (true) {
      if (cv::waitKey(0) == 'q') {
          break;
      }
  }

  cv::destroyWindow(WINDOW_NAME_ORIG);
  cv::destroyWindow(WINDOW_NAME_PROC);
  cv::destroyWindow(WINDOW_NAME_ROIS);
  cv::destroyWindow(WINDOW_NAME_RESP);

  return 0;
}
