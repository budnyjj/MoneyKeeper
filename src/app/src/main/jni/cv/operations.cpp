#include "cv/operations.hpp"

#include <vector>

#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>


using cv::Mat;
using cv::Point;
using cv::Rect;
using cv::RNG;
using cv::Scalar;
using cv::Size;
using cv::Vec4i;
using std::vector;


bool Operations::sliceCentered(
        const Mat& src_mat, cv::Mat& dst_mat,
        std::size_t width, std::size_t height) {
    std::size_t col_min = 0;
    std::size_t col_max = src_mat.cols - 1;
    if (width < src_mat.cols) {
        col_min = (src_mat.cols - width) / 2;
        col_max = (src_mat.cols + width) / 2;
    }
    std::size_t row_min = 0;
    std::size_t row_max = src_mat.rows - 1;
    if (height < src_mat.rows) {
        row_min = (src_mat.rows - height) / 2;
        row_max = (src_mat.rows + height) / 2;
    }

    dst_mat = src_mat(Rect(Point(col_min, row_min), Point(col_max, row_max)));
    return true;
}

bool Operations::mergeCentered(
        const cv::Mat& src_bottom_mat, const cv::Mat& src_top_mat,
        cv::Mat& dst_mat) {
    std::size_t bottom_cols = src_bottom_mat.cols;
    std::size_t bottom_rows = src_bottom_mat.rows;
    std::size_t top_cols = src_top_mat.cols;
    std::size_t top_rows = src_top_mat.rows;

    src_bottom_mat.copyTo(dst_mat);
    src_top_mat.copyTo(dst_mat
                       .rowRange((bottom_rows - top_rows) / 2, (bottom_rows + top_rows) / 2)
                       .colRange((bottom_cols - top_cols) / 2, (bottom_cols + top_cols) / 2));
    return true;
}
