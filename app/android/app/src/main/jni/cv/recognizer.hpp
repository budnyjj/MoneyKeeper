#ifndef CV_RECOGNIZER_HPP_
#define CV_RECOGNIZER_HPP_

#include <android/log.h>
#include <android/asset_manager.h>

#include <opencv2/core/core.hpp>


class Recognizer {
public:
    explicit Recognizer(
            AAssetManager* manager, const std::string& model_filename);

    void recognize(
            const cv::Mat& src_mat, cv::Mat& dst_mat);
};

#endif  // define CV_RECOGNIZER_HPP_
