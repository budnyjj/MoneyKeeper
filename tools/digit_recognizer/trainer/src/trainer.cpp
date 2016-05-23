#include <algorithm>
#include <cmath>
#include <fstream>
#include <iostream>
#include <memory>
#include <stdexcept>

#include "opencv2/core/core.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/ml/ml.hpp"


using std::cout;
using std::endl;
using std::ifstream;
using std::runtime_error;
using std::string;
using std::auto_ptr;
using std::vector;
using cv::ml::KNearest;
using cv::ml::TrainData;
using cv::FileStorage;
using cv::Mat;
using cv::Point;
using cv::Ptr;
using cv::Rect;


template <class T>
void endswap(T* obj) {
    uchar* memp = reinterpret_cast<uchar*>(obj);
    std::reverse(memp, memp + sizeof(T));
}

vector<Mat> readMnistData(const string& filename) {
    ifstream file(filename.c_str(), std::ios::binary);
    if (!file.is_open()) {
        throw runtime_error("Unable to open file with training data");
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
    vector<Mat> dst_vec;
    for (int i = 0; i < n_images; i++) {
        Mat image(n_rows, n_cols, CV_8U);
        for (int r = 0; r < n_rows; r++) {
            for (int c = 0; c < n_cols; c++) {
                uchar value = 0;
                file.read(reinterpret_cast<char*>(&value), sizeof(value));
                image.at<uchar>(r, c) = value;
            }
        }
        dst_vec.push_back(image);
    }
    return dst_vec;
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

vector<double> readMnistLabels(const string& filename) {
    ifstream file(filename.c_str(), std::ios::binary);
    if (!file.is_open()) {
        throw runtime_error("Unable to open file with training labels");
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
    vector<double> dst_vec;
    dst_vec.resize(n_labels);
    for (int i = 0; i < n_labels; i++) {
        uchar value = 0;
        file.read(reinterpret_cast<char*>(&value), sizeof(value));
        dst_vec[i] = value;
    }

    return dst_vec;
}

Mat mergeLabelset(const vector<double>& src_vec) {
    Mat labelset;

    int n_labels = src_vec.size();
    if (n_labels == 0) {
        return labelset;
    }

    labelset = Mat(n_labels, 1, CV_32FC1);
    for (int i = 0; i < n_labels; i++) {
        labelset.at<float>(i, 0) = src_vec[i];
    }

    return labelset;
}

Mat readData(const string& filename) {
    vector<Mat> vec_train_data = readMnistData(filename);
    cout << "Size of source data set:\n"
         << "  " << vec_train_data.size() << endl;
    // merge training dataset into single mat
    Mat mat_train_data = mergeDataset(vec_train_data);
    cout << "Size of merged data set:\n"
         << "  rows:    " << mat_train_data.rows << "\n"
         << "  columns: " << mat_train_data.cols << endl;
    return mat_train_data;
}

Mat readLabels(const string& filename) {
    vector<double> vec_train_labels = readMnistLabels(filename);
    cout << "Size of source label set:\n"
         << "  " << vec_train_labels.size() << endl;
    // merge training labels into single mat
    Mat mat_train_labels = mergeLabelset(vec_train_labels);
    cout << "Size of merged label set:\n"
         << "  rows:    " << mat_train_labels.rows << "\n"
         << "  columns: " << mat_train_labels.cols << endl;
    return mat_train_labels;
}

int main() {
    // read MNIST train images and labels into OpenCV Mats
    Mat train_samples = readData("data/train-images-idx3-ubyte");
    Mat train_responses = readLabels("data/train-labels-idx1-ubyte");
    Ptr<TrainData> train_data =
        TrainData::create(train_samples, cv::ml::ROW_SAMPLE, train_responses);
    // setup and train classifier
    Ptr<KNearest> classifier = KNearest::create();
    classifier->setIsClassifier(true);
    classifier->setAlgorithmType(KNearest::BRUTE_FORCE);
    classifier->setDefaultK(10);
    classifier->train(train_data);
    // store trained model in file
    classifier->save("data/params.xml");

    // // read MNIST test images and labels into OpenCV Mats
    // Mat test_samples = readData("data/t10k-images-idx3-ubyte");
    // Mat test_responses = readLabels("data/t10k-labels-idx1-ubyte");
    // // classify test samples
    // int n_test_samples = 100;
    // int n_test_features = test_samples.cols;
    // int n_test_successes = 0;
    // for (int i = 0; i < n_test_samples; i++) {
    //     Mat test_sample = test_samples(Rect(Point(0, i), Point(n_test_features, i+1)));
    //     Mat test_results(0, 0, CV_32F);
    //     float result =
    //         classifier->findNearest(test_sample, classifier->getDefaultK(), test_results);
    //     // cout << "Expected: " << test_responses.at<float>(i, 0)
    //     //      << ", Got: " << result << endl;
    //     if (result == test_responses.at<float>(i, 0)) {
    //         n_test_successes++;
    //     }
    // }

    // cout << "Number of positive guesses (total number of tests): accuracy\n"
    //      << "  " << n_test_successes
    //      << " (" << n_test_samples << "): "
    //      << float(n_test_successes) / n_test_samples * 100
    //      << endl;

    return 0;
}
