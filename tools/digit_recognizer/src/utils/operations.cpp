#include "operations.hpp"

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
using std::size_t;


bool Operations::sliceCentered(
        const Mat& src_mat, cv::Mat& dst_mat,
        size_t width, size_t height) {
    size_t col_min = 0;
    size_t col_max = src_mat.cols - 1;
    if (width < src_mat.cols) {
        col_min = (src_mat.cols - width) / 2;
        col_max = (src_mat.cols + width) / 2;
    }
    size_t row_min = 0;
    size_t row_max = src_mat.rows - 1;
    if (height < src_mat.rows) {
        row_min = (src_mat.rows - height) / 2;
        row_max = (src_mat.rows + height) / 2;
    }

    dst_mat = src_mat(Rect(Point(col_min, row_min), Point(col_max, row_max)));
    return true;
}

bool Operations::mergeCentered(
        const Mat& src_bottom_mat, const Mat& src_top_mat,
        Mat& dst_mat) {
    size_t bottom_cols = src_bottom_mat.cols;
    size_t bottom_rows = src_bottom_mat.rows;
    size_t top_cols = src_top_mat.cols;
    size_t top_rows = src_top_mat.rows;

    src_bottom_mat.copyTo(dst_mat);
    src_top_mat.copyTo(dst_mat
                       .rowRange((bottom_rows - top_rows) / 2, (bottom_rows + top_rows) / 2)
                       .colRange((bottom_cols - top_cols) / 2, (bottom_cols + top_cols) / 2));
    return true;
}

Mat Operations::toRow(const std::vector<float>& src_vec) {
    Mat result;
    size_t n_values = src_vec.size();
    if (n_values == 0) {
        return result;
    }

    result = Mat(1, n_values, CV_32F);
    for (size_t i = 0; i < n_values; i++) {
        result.at<float>(0, i) = src_vec[i];
    }
    return result;
}
