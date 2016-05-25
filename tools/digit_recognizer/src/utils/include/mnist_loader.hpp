#ifndef CV_MNIST_LOADER_HPP_
#define CV_MNIST_LOADER_HPP_

#include <string>
#include <vector>

#include "dataset_loader.hpp"


class MnistLoader: public DatasetLoader {
public:
    virtual std::vector<cv::Mat> loadSamples(const std::string& filename);
    virtual cv::Mat loadResponses(const std::string& filename);

private:

};

#endif  // define CV_MNIST_LOADER_HPP_
