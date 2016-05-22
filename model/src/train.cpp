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
using cv::Mat;


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
    dst_vec.resize(n_images, Mat::zeros(n_rows, n_cols, CV_8UC1));
    for (int i = 0; i < n_images; i++) {
        for (int r = 0; r < n_rows; r++) {
            for (int c = 0; c < n_cols; c++) {
                uchar value = 0;
                file.read(reinterpret_cast<char*>(&value), sizeof(value));
                dst_vec[i].at<uchar>(r, c) = value;

                // if (dst_vec[i].at<uchar>(r, c) > 0) {
                //     cout << "# ";
                // } else {
                //     cout << "  ";
                // }
            }
// e            cout << "\n";
        }
// e        cout << "\n\n" << endl; //
    }

    // for (int i = 0; i < dst_vec.size(); i++) {
    //     cout << dst_vec[i].rows << ":" << dst_vec[i].cols << endl;
        for (int r = 0; r < dst_vec[10].rows; r++) {
            for (int c = 0; c < dst_vec[10].cols; c++) {
                if (dst_vec[10].at<uchar>(r, c) > 0) {
                    cout << "# ";
                } else {
                    cout << "  ";
                }
            }
            cout << "\n";
        }
    //     cout << "\n\n" << endl;
    // }


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
        // cout << i << ":" << endl;
        int rc = 0;
        for (int r = 0; r < n_rows; r++) {
            for (int c = 0; c < n_cols; c++) {
                // if (src_vec[i].at<uchar>(r, c) > 0) {
                //     cout << "# ";
                // } else {
                //     cout << "  ";
                // }


                dataset.at<float>(i, rc) = src_vec[i].at<uchar>(r, c);
                rc++;
            }
            // cout << "\n";
        }
        // cout << "\n\n" << endl;
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

    // for (int i = 0; i < vec_train_data.size(); i++) {
    //     cout << vec_train_data[i].rows << ":" << vec_train_data[i].cols << endl;
    //     for (int r = 0; r < vec_train_data[i].rows; r++) {
    //         for (int c = 0; c < vec_train_data[i].cols; c++) {
    //             if (vec_train_data[i].at<uchar>(r, c) > 0) {
    //                 cout << "# ";
    //             } else {
    //                 cout << "  ";
    //             }
    //         }
    //         cout << "\n";
    //     }
    //     cout << "\n\n" << endl;
    // }

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
    Mat train_data = readData("data/train-images-idx3-ubyte");
    Mat train_labels = readLabels("data/train-labels-idx1-ubyte");
    // train classifier
    CvKNearest knn(train_data, train_labels);
    // read MNIST test images and labels into OpenCV Mats
    Mat test_data = readData("data/t10k-images-idx3-ubyte");
    Mat test_labels = readLabels("data/t10k-labels-idx1-ubyte");

    // classify test data
    int n_tests = 100;
    int n_successes = 0;

    for (int i = 0; i < n_tests; i++) {
        Mat test_sample =
            test_data(cv::Rect(cv::Point(0, i), cv::Point(test_data.cols, i+1)));
        float prediction = knn.find_nearest(test_sample, knn.get_max_k());
        cout << train_labels.at<float>(i, 0) << ": "
             << prediction << endl;
        if (train_labels.at<float>(i, 0) == prediction) {
            n_successes++;
        }
    }
    cout << "Number of positive guesses (total number of tests): accuracy\n"
         << "  " << n_successes
         << " (" << n_tests << "): "
         << float(n_successes) / n_tests * 100
         << endl;

    return 0;
}
