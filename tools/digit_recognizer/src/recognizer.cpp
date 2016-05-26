#include <algorithm>
#include <iostream>
#include <sstream>
#include <vector>

#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/ml/ml.hpp>
#include "opencv2/objdetect/objdetect.hpp"

#include "filters.hpp"
#include "operations.hpp"


using cv::Algorithm;
using cv::HOGDescriptor;
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
using std::string;
using std::vector;


const static char * const WINDOW_NAME_SRC = "Source image";
const static char * const WINDOW_NAME_PRC = "Processed image";
const static char * const WINDOW_NAME_SMP = "Samples";
const static char * const WINDOW_NAME_RSP = "Responses";
const static int SAMPLE_ROWS = 28;
const static int SAMPLE_COLS = 28;


void usage(const std::string& exec_name) {
    cout << "Usage: " << exec_name << " <image> <params>\n"
         << "  <image> path to the sample image\n"
         << "  <params> path to the parameters of trained classifier in XML format"
         << endl;
}

// extracts regions of interest
vector<Mat> findSamples(const Mat& src_mat) {
    Mat tmp_mat;
    src_mat.copyTo(tmp_mat);
    vector<vector<Point> > contours;
    vector<Vec4i> hierarchy;
    cv::findContours(tmp_mat, contours, hierarchy,
                     CV_RETR_CCOMP, CV_CHAIN_APPROX_SIMPLE);
    vector<Mat> samples;
    for (int i = 0; i < contours.size(); i = hierarchy[i][0]) {
        Rect sample_rect = cv::boundingRect(contours[i]);
        samples.push_back(src_mat(sample_rect));
    }

    return samples;
}

vector<Mat> normalizeSamples(const vector<Mat>& samples) {
    vector<Mat> samples_norm;
    for (int i = 0; i < samples.size(); i++) {
        const Mat& sample = samples[i];
        Mat sample_scaled(SAMPLE_ROWS - 10, SAMPLE_COLS - 10, CV_8U);
        cv::resize(sample, sample_scaled, sample_scaled.size(), 0, 0, cv::INTER_NEAREST);
        // double scale_factor =
        //     std::min(double(SAMPLE_ROWS) / sample.rows,
        //              double(SAMPLE_COLS) / sample.cols);
        // cv::resize(sample, sample_scaled, Size(), scale_factor, scale_factor, cv::INTER_NEAREST);
        Mat sample_norm = Mat::zeros(SAMPLE_ROWS, SAMPLE_ROWS, CV_8U);
        Operations::mergeCentered(sample_norm, sample_scaled, sample_norm);
        samples_norm.push_back(sample_norm);
    }
    return samples_norm;
}

Mat makeSamplesPreview(const vector<Mat>& src_vec) {
    Mat preview;

    int n_images = src_vec.size();
    if (n_images == 0) {
        return preview;
    }
    int n_rows = src_vec[0].rows;
    int n_cols = src_vec[0].cols;

    preview = Mat(n_rows, n_images * n_cols, CV_8U);
    int n_cols_offset = 0;
    for (int i = 0; i < n_images; i++) {
        src_vec[i].copyTo(preview
                          .rowRange(0, n_rows)
                          .colRange(n_cols_offset, n_cols_offset + n_cols));
        n_cols_offset += n_cols;
    }
    return preview;
}

Mat makeResponsesPreview(const Mat& responses, const Size& sample_size) {
    int n_responses = responses.rows;
    if (n_responses == 0) {
        return Mat();
    }

    int n_rows = sample_size.height;
    int n_cols = sample_size.width;

    vector<Mat> samples;
    for (int i = 0; i < n_responses; i++) {
        Mat sample = Mat::zeros(n_rows, n_cols, CV_8U);
        ostringstream oss;
        oss << sample.at<float>(i, 0);
        cv::putText(sample, oss.str(), Point(0, n_rows),
                    cv::FONT_HERSHEY_COMPLEX_SMALL, 1.0, Scalar(255));
        samples.push_back(sample);
    }
    return makeSamplesPreview(samples);
}

int main(int argc, char** argv) {
    if (argc != 3) {
        usage(argv[0]);
        return -1;
    }

    string path_image = argv[1];
    string path_parameters = argv[2];

    cout << "Path to source image:          " << path_image << "\n"
         << "Path to classifier parameters: " << path_parameters
         << endl;

    Mat src = cv::imread(path_image, CV_LOAD_IMAGE_UNCHANGED);
    if (src.empty()) {
        cout << "Unable to load image" << endl;
        usage(argv[0]);
        return -1;
    }

    // perform basic filtering
    Mat filtered;
    cv::cvtColor(src, filtered, cv::COLOR_BGR2GRAY);
    Filters::basic(filtered, filtered);

    // extract regions of interest
    vector<Mat> samples = findSamples(filtered);
    vector<Mat> samples_norm = normalizeSamples(samples);
    int n_samples = samples_norm.size();

    // make preview from extracted samples
    Mat samples_preview = makeSamplesPreview(samples_norm);

    // detect features on samples and extract their descriptors
    vector<Mat> descriptors;
    for (int i = 0; i < n_samples; i++) {
        Mat descriptor;
        samples_norm[i].convertTo(descriptor, CV_32F);
        descriptors.push_back(descriptor);
    }
    // HOGDescriptor detector(Size(SAMPLE_ROWS, SAMPLE_COLS),
    //                        Size(SAMPLE_ROWS / 2, SAMPLE_COLS / 2),
    //                        Size(SAMPLE_COLS / 4, SAMPLE_COLS / 4),
    //                        Size(SAMPLE_COLS / 4, SAMPLE_COLS / 4),
    //                        9);
    // vector<Point> locations;
    // for (int i = 0; i < n_samples; i++) {
    //     vector<float> descriptor;
    //     detector.compute(samples_norm[i], descriptor, Size(0, 0), Size(0, 0), locations);
    //     descriptors.push_back(Operations::toRow(descriptor));
    // }
    cout << "Number of descriptors: " << descriptors.size() << endl;
    Mat descriptors_mrg = Operations::flatten<float>(descriptors);
    size_t n_descriptors = descriptors_mrg.rows;
    size_t n_features = descriptors_mrg.cols;
    cout << "Classification data size:\n"
         << "  samples:  " << n_descriptors << "\n"
         << "  features: " << n_features
         << endl;

    // classify descriptors
    Ptr<KNearest> classifier = Algorithm::load<KNearest>(path_parameters);
    Mat responses(0, 0, CV_32F);
    classifier->findNearest(descriptors_mrg, classifier->getDefaultK(), responses);

    // make preview for results
    Mat responses_preview =
        makeResponsesPreview(responses, Size(SAMPLE_ROWS, SAMPLE_COLS));

    // show results
    cv::namedWindow(WINDOW_NAME_SRC, CV_WINDOW_AUTOSIZE);
    cv::imshow(WINDOW_NAME_SRC, src);
    cv::namedWindow(WINDOW_NAME_PRC, CV_WINDOW_AUTOSIZE);
    cv::imshow(WINDOW_NAME_PRC, filtered);
    cv::namedWindow(WINDOW_NAME_SMP, CV_WINDOW_AUTOSIZE);
    cv::imshow(WINDOW_NAME_SMP, samples_preview);
    cv::namedWindow(WINDOW_NAME_RSP, CV_WINDOW_AUTOSIZE);
    cv::imshow(WINDOW_NAME_RSP, responses_preview);

    while (true) {
        if (cv::waitKey(0) == 'q') {
            break;
        }
    }

    cv::destroyWindow(WINDOW_NAME_SRC);
    cv::destroyWindow(WINDOW_NAME_PRC);
    cv::destroyWindow(WINDOW_NAME_SMP);
    cv::destroyWindow(WINDOW_NAME_RSP);

    return 0;
}
