#include <cmath>
#include <memory>

#include <string>
#include <vector>

#include "opencv2/core/core.hpp"
#include "opencv2/features2d/features2d.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/ml/ml.hpp"
#include "opencv2/objdetect/objdetect.hpp"

#include "mnist_loader.hpp"
#include "operations.hpp"


using cv::FileStorage;
using cv::HOGDescriptor;
using cv::KeyPoint;
using cv::Mat;
using cv::Point;
using cv::Ptr;
using cv::Rect;
using cv::Size;
using cv::TermCriteria;
using cv::ml::KNearest;
using cv::ml::SVM;
using cv::ml::TrainData;
using std::cout;
using std::endl;
using std::runtime_error;
using std::string;
using std::vector;


const static int SAMPLE_ROWS = 28;
const static int SAMPLE_COLS = 28;


void usage(const std::string& exec_name) {
    cout << "Usage: " << exec_name << " <images> <labels> <params>\n"
         << "  <images> path to the MNIST images\n"
         << "  <labels> path to the MNIST labels\n"
         << "  <params> path to store parameters of trained classifier in XML format"
         << endl;
}

int main(int argc, char** argv) {
    if (argc != 4) {
        usage(argv[0]);
        return -1;
    }

    string path_samples = argv[1];
    string path_responses = argv[2];
    string path_parameters = argv[3];
    cout << "Path to MNIST images:          " << path_samples << "\n"
         << "Path to MNIST labels:          " << path_responses << "\n"
         << "Path to classifier parameters: " << path_parameters
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
    // for (int i = 0; i < n_samples; i++) {
    //     Mat descriptor;
    //     samples[i].convertTo(descriptor, CV_32F);
    //     descriptors.push_back(descriptor);
    // }
    HOGDescriptor detector(Size(SAMPLE_ROWS, SAMPLE_COLS),
                           Size(SAMPLE_ROWS / 2, SAMPLE_COLS / 2),
                           Size(SAMPLE_COLS / 4, SAMPLE_COLS / 4),
                           Size(SAMPLE_COLS / 4, SAMPLE_COLS / 4),
                           9);
    vector<Point> locations;
    for (int i = 0; i < n_samples; i++) {
        vector<float> descriptor;
        detector.compute(samples[i], descriptor, Size(0, 0), Size(0, 0), locations);
        descriptors.push_back(Operations::toRow(descriptor));
    }
    cout << "Number of descriptors: " << descriptors.size() << endl;
    Mat descriptors_mrg = Operations::flatten<float>(descriptors);
    cout << "Train data size:\n"
         << "  samples:  " << descriptors_mrg.rows << "\n"
         << "  features: " << descriptors_mrg.cols
         << endl;

    Ptr<TrainData> data =
        TrainData::create(descriptors_mrg, cv::ml::ROW_SAMPLE, responses);

    // setup and train classifier
    // Ptr<KNearest> classifier = KNearest::create();
    // classifier->setIsClassifier(true);
    // classifier->setAlgorithmType(KNearest::BRUTE_FORCE);
    // classifier->setDefaultK(10);
    // classifier->train(data);

    Ptr<SVM> classifier = SVM::create();
    classifier->setType(SVM::C_SVC);
    classifier->setKernel(SVM::POLY);
    classifier->setTermCriteria(cv::TermCriteria(TermCriteria::MAX_ITER, 100, 1e-6));
    classifier->setGamma(3);
    classifier->setDegree(3);
    classifier->train(data);

    // store trained model in file
    classifier->save(path_parameters);
    return 0;
}
