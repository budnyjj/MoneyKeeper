#include "budny_moneykeeper_cv_Filters.h"

#include <opencv2/core/core.hpp>

#include "cv/filters.hpp"


extern "C" JNIEXPORT jboolean JNICALL
Java_budny_moneykeeper_cv_Filters_nativeBasic(
    JNIEnv*, jclass,
    jlong j_src_mat, jlong j_dst_mat) {
    cv::Mat& src_mat = *(reinterpret_cast<cv::Mat*>(j_src_mat));
    cv::Mat& dst_mat = *(reinterpret_cast<cv::Mat*>(j_dst_mat));
    return Filters::basic(src_mat, dst_mat);
}
