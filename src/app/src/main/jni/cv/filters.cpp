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
    cv::cvtColor(src_mat, dst_mat, cv::COLOR_RGBA2GRAY);
    cv::GaussianBlur(dst_mat, dst_mat, Size(3, 3), 0, 0);
    adaptiveThreshold(dst_mat, dst_mat, 255, cv::ADAPTIVE_THRESH_GAUSSIAN_C,
                      cv::THRESH_BINARY, 11, 5);
    // find and draw contours
    vector<vector<Point>> contours;
    vector<Vec4i> hierarchy;
    cv::findContours(dst_mat, contours, hierarchy,
        CV_RETR_CCOMP, CV_CHAIN_APPROX_SIMPLE);
    for (int i = 0; i < contours.size(); i = hierarchy[i][0]) {
        Scalar color = Scalar(
                rng.uniform(0, 255), rng.uniform(0, 255), rng.uniform(0, 255));
        Rect rect = cv::boundingRect(contours[i]);
        cv::rectangle(dst_mat, rect.tl(), rect.br(), color, 2, 8, 0);
    }

    return true;
}
