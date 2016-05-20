#ifndef CV_FILTERS_HPP_
#define CV_FILTERS_HPP_

#include <opencv2/core/core.hpp>

class Filters {
public:
    static bool basic(const cv::Mat& src_mat, cv::Mat& dst_mat);
    static bool highlight(const cv::Mat& src_mat, cv::Mat& dst_mat,
                          std::size_t width, std::size_t height);
private:
    static cv::RNG rng;
};

#endif  // define CV_FILTERS_HPP_
