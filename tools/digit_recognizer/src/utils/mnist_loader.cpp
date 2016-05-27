#include "mnist_loader.hpp"

#include <algorithm>
#include <fstream>
#include <stdexcept>
#include <string>
#include <vector>

#include "opencv2/core/core.hpp"


using cv::Mat;
using std::ifstream;
using std::string;
using std::runtime_error;
using std::vector;


template <class T>
static void endswap(T* obj) {
    uchar* memp = reinterpret_cast<uchar*>(obj);
    std::reverse(memp, memp + sizeof(T));
}

vector<Mat> MnistLoader::loadSamples(const string& filename) {
    ifstream file(filename.c_str(), std::ios::binary);
    if (!file.is_open()) {
        throw runtime_error("Unable to open file with MNIST data");
    }
    // read magic number
    int magic_number = 0;
    file.read(reinterpret_cast<char*>(&magic_number), sizeof(magic_number));
    endswap(&magic_number);
    // read number of images
    int n_images = 0;
    file.read(reinterpret_cast<char*>(&n_images), sizeof(n_images));
    endswap(&n_images);
    // read number of rows in image
    int n_rows = 0;
    file.read(reinterpret_cast<char*>(&n_rows), sizeof(n_rows));
    endswap(&n_rows);
    // read number of columns in image
    int n_cols = 0;
    file.read(reinterpret_cast<char*>(&n_cols), sizeof(n_cols));
    endswap(&n_cols);
    // read image data and store it in vector
    vector<Mat> samples;
    for (int i = 0; i < n_images; i++) {
        Mat sample(n_rows, n_cols, CV_8U);
        for (int r = 0; r < n_rows; r++) {
            for (int c = 0; c < n_cols; c++) {
                uchar value = 0;
                file.read(reinterpret_cast<char*>(&value), sizeof(value));
                sample.at<uchar>(r, c) = value;
            }
        }
        samples.push_back(sample);
    }
    return samples;
}

Mat MnistLoader::loadResponses(const string& filename) {
    ifstream file(filename.c_str(), std::ios::binary);
    if (!file.is_open()) {
        throw runtime_error("Unable to open file with MNIST labels");
    }

    // read magic number
    int magic_number = 0;
    file.read(reinterpret_cast<char*>(&magic_number), sizeof(magic_number));
    endswap(&magic_number);
    // read number of images
    int n_labels = 0;
    file.read(reinterpret_cast<char*>(&n_labels), sizeof(n_labels));
    endswap(&n_labels);
    // read and store labels
    Mat responses(n_labels, 1, CV_32S);
    for (int i = 0; i < n_labels; i++) {
        uchar value = 0;
        file.read(reinterpret_cast<char*>(&value), sizeof(value));
        responses.at<int>(i, 0) = value;
    }
    return responses;
}
