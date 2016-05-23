#include "cv/filters.hpp"

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


RNG Filters::rng(12345);


bool Filters::basic(const Mat& src_mat, Mat& dst_mat) {
    // preprocessing

    // my filter
    cv::blur(src_mat, dst_mat, Size(3, 3));
    adaptiveThreshold(dst_mat, dst_mat, 255, cv::ADAPTIVE_THRESH_MEAN_C,
                      cv::THRESH_BINARY_INV, 11, 5);


    // find and draw contours
    vector<vector<Point>> contours;
    vector<Vec4i> hierarchy;
    cv::findContours(dst_mat, contours, hierarchy,
        CV_RETR_CCOMP, CV_CHAIN_APPROX_SIMPLE);
    for (int i = 0; i < contours.size(); i = hierarchy[i][0]) {
        Rect rect = cv::boundingRect(contours[i]);
        cv::rectangle(dst_mat, rect.tl(), rect.br(), Scalar(100, 100, 100), 2, 8, 0);
    }

    return true;
}

bool Filters::basic2(const Mat& src_mat, Mat& dst_mat) {
    // preprocessing

    // cv::blur(src_mat, dst_mat, Size(3, 3));
    // cv::Canny(dst_mat, dst_mat, 10, 100);
    // adaptiveThreshold(dst_mat, dst_mat, 255, cv::ADAPTIVE_THRESH_MEAN_C,
    //                   cv::THRESH_BINARY_INV, 11, 5);


    // book filter
    // cv::dilate(src_mat, dst_mat,
    //            cv::getStructuringElement(CV_SHAPE_CROSS, Size(3,3)));
    // adaptiveThreshold(dst_mat, dst_mat, 255, cv::ADAPTIVE_THRESH_MEAN_C,
    //                   cv::THRESH_BINARY_INV, 15, 2);

    // find and draw contours
    // vector<vector<Point>> contours;
    // vector<Vec4i> hierarchy;
    // cv::findContours(dst_mat, contours, hierarchy,
    //     CV_RETR_CCOMP, CV_CHAIN_APPROX_SIMPLE);
    // for (int i = 0; i < contours.size(); i = hierarchy[i][0]) {
    //     Rect rect = cv::boundingRect(contours[i]);
    //     cv::rectangle(dst_mat, rect.tl(), rect.br(), Scalar(150, 150, 150), 2, 8, 0);
    // }

    return true;
}


bool Filters::highlight(const Mat& src_mat, cv::Mat& dst_mat,
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

    return true;
}
