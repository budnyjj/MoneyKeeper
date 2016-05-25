#include <cmath>
#include <memory>

#include <string>
#include <vector>

#include "opencv2/core/core.hpp"
#include "opencv2/features2d/features2d.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/ml/ml.hpp"

#include "mnist_loader.hpp"
#include "operations.hpp"


using cv::Algorithm;
using cv::KeyPoint;
using cv::Mat;
using cv::ORB;
using cv::Point;
using cv::Ptr;
using cv::Rect;
using cv::Size;
using cv::ml::KNearest;
using cv::ml::TrainData;
using std::cout;
using std::endl;
using std::runtime_error;
using std::string;
using std::vector;


void usage(const std::string& exec_name) {
    cout << "Usage: " << exec_name << " <images> <labels> <params>\n"
         << "  <images>  path to the MNIST images\n"
         << "  <labels>  path to the MNIST labels\n"
         << "  <params>  path to load parameters of trained classifier in XML format\n"
         << "  <n_tests> number of tests"
         << endl;
}

int main(int argc, char** argv) {
    if (argc != 5) {
        usage(argv[0]);
        return -1;
    }

    string path_samples = argv[1];
    string path_responses = argv[2];
    string path_parameters = argv[3];
    size_t n_tests = std::stoi(argv[4]);
    cout << "Path to MNIST images:          " << path_samples << "\n"
         << "Path to MNIST labels:          " << path_responses << "\n"
         << "Path to classifier parameters: " << path_parameters << "\n"
         << "Number of tests:               " << n_tests
         << endl;

    // read MNIST train images and labels into OpenCV Mats
    MnistLoader loader;
    vector<Mat> samples;
    try {
        samples = loader.loadSamples(path_samples);
    } catch (const runtime_error& e) {
        cout << "Error: unable to load train samples" << endl;
        usage(argv[0]);
        return -1;
    }
    Mat responses;
    try {
        responses = loader.loadResponses(path_responses);
    } catch (const runtime_error& e) {
        cout << "Error: unable to load train responses" << endl;
        usage(argv[0]);
        return -1;
    }
    int n_samples = samples.size();
    cout << "Number of samples: " << n_samples << endl;

    // detect features on samples and extract their descriptors
    vector<Mat> descriptors;
    for (int i = 0; i < n_samples; i++) {
        Mat descriptor;
        samples[i].convertTo(descriptor, CV_32F);
        descriptors.push_back(descriptor);
    }
    // Ptr<ORB> detector = ORB::create(10, 1.2f, 8, 2, 0, 2, ORB::HARRIS_SCORE, 2, 20);
    // vector<KeyPoint> keypoints;
    // for (int i = 0; i < n_samples; i++) {
    //     Mat sample, descriptor, descriptor_float;
    //     cv::cvtColor(samples[i], sample, cv::COLOR_GRAY2RGB);
    //     detector->detect(sample, keypoints);
    //     detector->compute(sample, keypoints, descriptor);
    //     descriptor.convertTo(descriptor_float, CV_32F);
    //     descriptors.push_back(descriptor_float);
    // }
    cout << "Number of descriptors: " << descriptors.size() << endl;
    Mat descriptors_mrg = Operations::flatten<float>(descriptors);
    size_t n_descriptors = descriptors_mrg.rows;
    size_t n_features = descriptors_mrg.cols;
    cout << "Train data size:\n"
         << "  samples:  " << n_descriptors << "\n"
         << "  features: " << n_features
         << endl;

    // load classifier
    Ptr<KNearest> classifier = Algorithm::load<KNearest>(path_parameters);
    // classify test samples
    size_t n_successes = 0;
    Mat test_results(0, 0, CV_32F);
    for (int i = 0; i < n_tests; i++) {
        Mat descriptor_mrg = descriptors_mrg(Rect(Point(0, i), Point(n_features, i+1)));
        float result =
            classifier->findNearest(descriptor_mrg, classifier->getDefaultK(), test_results);
        cout << "Expected: " << responses.at<float>(i, 0)
             << ", Got: " << result << endl;
        if (result == responses.at<float>(i, 0)) {
            n_successes++;
        }
    }

    cout << "Number of positive guesses (total number of tests): accuracy\n"
         << "  " << n_successes
         << " (" << n_tests << "): "
         << float(n_successes) / n_tests * 100
         << endl;
}
