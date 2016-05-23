#include <iostream>
#include <vector>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/ml/ml.hpp>


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
using std::vector;


const static char * const WINDOW_NAME_ORIG = "Original image";
const static char * const WINDOW_NAME_PROC = "Processed image";
const static int SAMPLE_ROWS = 28;
const static int SAMPLE_COLS = 28;

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
        Mat normalized_roi(SAMPLE_ROWS, SAMPLE_COLS, CV_8U);
        cv::resize(rois[i], normalized_roi, normalized_roi.size(), 0, 0, cv::INTER_NEAREST);
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

    dataset = Mat(n_images, n_rows * n_cols, CV_32FC1);
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

int main(int argc, char** argv) {
  if (argc != 2) {
    cout << "Usage: recognizer <image_path>\n" << endl;
    return -1;
  }

  Mat src = cv::imread(argv[1], CV_LOAD_IMAGE_UNCHANGED);
  if (src.empty()) {
    cout << "No image data \n" << endl;
    return -1;
  }

  Mat dst;
  cv::cvtColor(src, dst, cv::COLOR_BGR2GRAY);
  vector<Mat> rois = preprocess(dst, dst);

  // prepare dataset
  vector<Mat> normalized_rois = normalize(rois);
  Mat merged_rois = mergeDataset(normalized_rois);
  // setup classifier
  Ptr<KNearest> classifier = Algorithm::load<KNearest>("data/params.xml");
  // classify test samples
  int n_test_samples = merged_rois.rows;
  int n_test_features = merged_rois.cols;

  for (int i = 0; i < n_test_samples; i++) {
      Mat test_sample = merged_rois(Rect(Point(0, i), Point(n_test_features, i+1)));
      Mat test_results(0, 0, CV_32F);
      float result =
          classifier->findNearest(test_sample, classifier->getDefaultK(), test_results);
      cout << result << ", ";
  }
  cout << endl;

  cv::namedWindow(WINDOW_NAME_ORIG, CV_WINDOW_AUTOSIZE);
  cv::imshow(WINDOW_NAME_ORIG, src);
  cv::namedWindow(WINDOW_NAME_PROC, CV_WINDOW_AUTOSIZE);
  cv::imshow(WINDOW_NAME_PROC, dst);

  while (true) {
      if (cv::waitKey(0) == 'q') {
          break;
      }
  }

  cv::destroyWindow(WINDOW_NAME_ORIG);
  cv::destroyWindow(WINDOW_NAME_PROC);

  return 0;
}
