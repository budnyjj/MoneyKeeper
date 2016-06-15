#ifndef CV_RECOGNIZER_HPP_
#define CV_RECOGNIZER_HPP_

#include <string>
#include <vector>

#include <android/log.h>
#include <android/asset_manager.h>

#include <opencv2/core/core.hpp>
#include <opencv2/ml/ml.hpp>
#include <opencv2/objdetect/objdetect.hpp>


class Recognizer {
public:
    explicit Recognizer(
            AAssetManager* manager, const std::string& model_filename);

    void recognize(
            const cv::Mat& src_mat, cv::Mat& dst_mat);

private:
    const static int SAMPLE_ROWS;
    const static int SAMPLE_COLS;

    cv::Ptr<cv::ml::SVM> m_classifier;
    cv::HOGDescriptor m_descriptor;

    std::string readModel(
            AAssetManager* manager, const std::string& model_filename);
    std::vector<cv::Mat> findSamples(const cv::Mat& src_mat);
    std::vector<cv::Mat> normalizeSamples(const std::vector<cv::Mat>& samples);
};

#endif  // define CV_RECOGNIZER_HPP_
