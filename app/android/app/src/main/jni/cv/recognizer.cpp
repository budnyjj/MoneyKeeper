#include "cv/recognizer.hpp"
#include "util/logging.hpp"

#include <cstddef>
#include <vector>


static std::string readModel(
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

Recognizer::Recognizer(
        AAssetManager* manager, const std::string& model_filename) {
    std::string model = readModel(manager, model_filename);

}

void Recognizer::recognize(
        const cv::Mat& src_mat, cv::Mat& dst_mat) {
    dst_mat = src_mat;
}
