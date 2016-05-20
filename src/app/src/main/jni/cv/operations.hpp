#ifndef CV_OPERATIONS_HPP_
#define CV_OPERATIONS_HPP_

#include <opencv2/core/core.hpp>


class Operations {
public:
    static bool sliceCentered(
            const cv::Mat& src_mat, cv::Mat& dst_mat,
            std::size_t width, std::size_t height);
    static bool mergeCentered(
            const cv::Mat& src_bottom_mat, const cv::Mat& src_top_mat,
            cv::Mat& dst_mat);
};

#endif  // define CV_OPERATIONS_HPP_
