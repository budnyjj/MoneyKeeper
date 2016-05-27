#include "filters.hpp"

#include <vector>

#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>


using cv::Mat;
using cv::Point;
using cv::Rect;
using cv::Scalar;
using cv::Size;
using cv::Vec4i;
using std::vector;


void Filters::basic(const Mat& src_mat, Mat& dst_mat) {
    cv::blur(src_mat, dst_mat, Size(3, 3));
    adaptiveThreshold(dst_mat, dst_mat, 255, cv::ADAPTIVE_THRESH_MEAN_C,
                      cv::THRESH_BINARY_INV, 11, 5);
}

void Filters::highlight(const Mat& src_mat, cv::Mat& dst_mat,
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

    Mat highlighted_mat =
        src_mat(Rect(Point(col_min, row_min), Point(col_max, row_max)));

    src_mat.convertTo(dst_mat, -1, 1, -50);
    highlighted_mat
        .copyTo(dst_mat.rowRange(row_min, row_max).colRange(col_min, col_max));
}
