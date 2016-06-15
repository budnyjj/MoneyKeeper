#include "cv/recognizer.hpp"

#include <cstddef>
#include <sstream>

#include <opencv2/imgproc/imgproc.hpp>

#include "cv/filters.hpp"
#include "cv/operations.hpp"
#include "util/logging.hpp"


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
using cv::ml::SVM;
using std::ostringstream;
using std::string;
using std::vector;


// static fields
const int Recognizer::SAMPLE_ROWS = 28;
const int Recognizer::SAMPLE_COLS = 28;


static std::string toString(const Mat responses) {
    int n_responses = responses.rows;
    ostringstream oss;
    for (int i = 0; i < n_responses; i++) {
        oss << responses.at<float>(i, 0) << " ";
    }
    oss << "\n";
    return oss.str();
}

Recognizer::Recognizer(
        AAssetManager* manager, const std::string& model_filename)
    : m_descriptor(Size(SAMPLE_ROWS, SAMPLE_COLS),
                   Size(SAMPLE_ROWS / 2, SAMPLE_COLS / 2),
                   Size(SAMPLE_COLS / 4, SAMPLE_COLS / 4),
                   Size(SAMPLE_COLS / 4, SAMPLE_COLS / 4), 9) {
    std::string model = readModel(manager, model_filename);
    m_classifier = Algorithm::loadFromString<SVM>(model);
}

void Recognizer::recognize(
        const cv::Mat& src_mat, cv::Mat& dst_mat) {
    // perform basic filtering
    Filters::basic(src_mat, dst_mat);

    // extract regions of interest
    vector<Mat> samples = findSamples(dst_mat);
    vector<Mat> samples_norm = normalizeSamples(samples);
    int n_samples = samples_norm.size();
    if (n_samples == 0) {
        return;
    }

    // detect features on samples and extract their descriptors
    vector<Mat> descriptors;
    vector<Point> locations;
    for (int i = 0; i < n_samples; i++) {
        vector<float> descriptor;
        m_descriptor.compute(samples_norm[i], descriptor, Size(0, 0), Size(0, 0), locations);
        descriptors.push_back(Operations::toRow(descriptor));
    }

    // flatten descriptors
    Mat descriptors_mrg = Operations::flatten<float>(descriptors);
    size_t n_descriptors = descriptors_mrg.rows;
    size_t n_features = descriptors_mrg.cols;

    // classify descriptors
    Mat responses(0, 0, CV_32F);
    m_classifier->predict(descriptors_mrg, responses);
    logging::d("REC", toString(responses));
}

string Recognizer::readModel(
        AAssetManager* manager, const std::string& model_filename) {
    AAsset* file = AAssetManager_open(manager, model_filename.c_str(), AASSET_MODE_BUFFER);
    std::size_t file_size = AAsset_getLength(file);
    char* buffer = new char[file_size+1];

    AAsset_read(file, buffer, file_size);
    buffer[file_size] = '\0';
    std::string model(buffer);

    delete[] buffer;
    AAsset_close(file);

    return model;
}

vector<Mat> Recognizer::findSamples(const Mat& src_mat) {
    Mat tmp_mat;
    src_mat.copyTo(tmp_mat);
    vector<vector<Point> > contours;
    vector<Vec4i> hierarchy;
    cv::findContours(tmp_mat, contours, hierarchy,
                     CV_RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE);
    vector<Mat> samples;
    for (int i = 0; i < contours.size(); i = hierarchy[i][0]) {
        Rect sample_rect = cv::boundingRect(contours[i]);
        samples.push_back(src_mat(sample_rect));
    }

    return samples;
}

vector<Mat> Recognizer::normalizeSamples(const vector<Mat>& samples) {
    vector<Mat> samples_norm;
    for (int i = 0; i < samples.size(); i++) {
        const Mat& sample = samples[i];
        Mat sample_scaled(20, 20, CV_8U);
        cv::resize(sample, sample_scaled, sample_scaled.size(), 0, 0, cv::INTER_AREA);
        Mat sample_norm = Mat::zeros(SAMPLE_ROWS, SAMPLE_ROWS, CV_8U);
        Operations::mergeCentered(sample_norm, sample_scaled, sample_norm);
        samples_norm.push_back(sample_norm);
    }
    return samples_norm;
}
