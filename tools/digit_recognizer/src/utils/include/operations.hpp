#ifndef CV_OPERATIONS_HPP_
#define CV_OPERATIONS_HPP_

#include <vector>

#include <opencv2/core/core.hpp>


class Operations {
public:
    static bool sliceCentered(
            const cv::Mat& src_mat, cv::Mat& dst_mat,
            std::size_t width, std::size_t height);
    static bool mergeCentered(
            const cv::Mat& src_bottom_mat, const cv::Mat& src_top_mat,
            cv::Mat& dst_mat);
    template<typename T>
    static cv::Mat flatten(const std::vector<cv::Mat>& src_vec);
};

template<typename T>
cv::Mat Operations::flatten(const std::vector<cv::Mat>& src_vec) {
    cv::Mat result;
    std::size_t n_images = src_vec.size();
    if (n_images == 0) {
        return result;
    }

    std::size_t n_rows = src_vec[0].rows;
    std::size_t n_cols = src_vec[0].cols;
    int type = src_vec[0].type();
    result = cv::Mat::zeros(n_images, n_rows*n_cols, src_vec[0].type());
    for (std::size_t i = 0; i < n_images; i++) {
        std::size_t rc = 0;
        for (std::size_t r = 0; r < n_rows; r++) {
            for (std::size_t c = 0; c < n_cols; c++) {
                result.at<T>(i, rc) = src_vec[i].at<T>(r, c);
                rc++;
            }
        }
    }
    return result;
}

#endif  // define CV_OPERATIONS_HPP_
