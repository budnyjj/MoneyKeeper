#include "cv/recognizer.hpp"

#include <vector>


Recognizer::Recognizer(
        AAssetManager* manager, const std::string& model_filename) {

}

void Recognizer::recognize(
        const cv::Mat& src_mat, cv::Mat& dst_mat) {
    dst_mat = src_mat;
}
