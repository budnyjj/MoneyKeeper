#ifndef CV_FILTERS_HPP_
#define CV_FILTERS_HPP_

#include <opencv2/core/core.hpp>


class Filters {
public:
    static void basic(const cv::Mat& src_mat, cv::Mat& dst_mat);
    static void highlight(const cv::Mat& src_mat, cv::Mat& dst_mat,
                          std::size_t width, std::size_t height);
};

#endif  // define CV_FILTERS_HPP_
