#include <cmath>
#include <memory>

#include <string>
#include <vector>

#include "opencv2/core/core.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/ml/ml.hpp"

#include "mnist_loader.hpp"


using cv::FileStorage;
using cv::Mat;
using cv::Point;
using cv::Ptr;
using cv::Rect;
using cv::ml::KNearest;
using cv::ml::TrainData;
using std::cout;
using std::endl;
using std::runtime_error;
using std::string;
using std::vector;


void usage(const std::string& exec_name) {
    cout << "Usage: " << exec_name << " <images> <labels> <params>\n"
         << "  <images> path to the MNIST images\n"
         << "  <labels> path to the MNIST labels\n"
         << "  <params> path to store parameters of trained classifier in XML format\n"
         << endl;
}

int main(int argc, char** argv) {
    if (argc != 4) {
        usage(argv[0]);
        return -1;
    }

    cout << "Path to MNIST images:          " << argv[1] << "\n"
         << "Path to MNIST labels:          " << argv[2] << "\n"
         << "Path to classifier parameters: " << argv[3] << "\n"
         << endl;

    // read MNIST train images and labels into OpenCV Mats
    MnistLoader loader;
    Mat train_samples, train_responses;
    try {
        train_samples = loader.loadSamples(argv[1]);
        train_responses = loader.loadResponses(argv[2]);
    } catch (const runtime_error& e) {
        cout << "Error: unable to load train data" << endl;
        usage(argv[0]);
        return -1;
    }
    Ptr<TrainData> train_data =
        TrainData::create(train_samples, cv::ml::ROW_SAMPLE, train_responses);
    cout << "Train data size:\n"
         << "  samples:  " << train_samples.rows << "\n"
         << "  features: " << train_samples.cols
         << endl;

    // setup and train classifier
    Ptr<KNearest> classifier = KNearest::create();
    classifier->setIsClassifier(true);
    classifier->setAlgorithmType(KNearest::BRUTE_FORCE);
    classifier->setDefaultK(10);
    classifier->train(train_data);

    // store trained model in file
    classifier->save(argv[3]);

    // // read MNIST test images and labels into OpenCV Mats
    // Mat test_samples = loader.loadSamples("../data/t10k-images-idx3-ubyte");
    // Mat test_responses = loader.loadResponses("../data/t10k-labels-idx1-ubyte");
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
