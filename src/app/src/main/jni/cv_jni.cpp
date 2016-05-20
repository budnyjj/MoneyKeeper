#include "budny_moneykeeper_cv_Filters.h"
#include "budny_moneykeeper_cv_Operations.h"

#include <opencv2/core/core.hpp>

#include "cv/filters.hpp"
#include "cv/operations.hpp"


extern "C" JNIEXPORT jboolean JNICALL
Java_budny_moneykeeper_cv_Filters_nativeBasic(
        JNIEnv*, jclass,
        jlong j_src_mat, jlong j_dst_mat) {
    cv::Mat& src_mat = *(reinterpret_cast<cv::Mat*>(j_src_mat));
    cv::Mat& dst_mat = *(reinterpret_cast<cv::Mat*>(j_dst_mat));
    return Filters::basic(src_mat, dst_mat);
}

extern "C" JNIEXPORT jboolean JNICALL
Java_budny_moneykeeper_cv_Filters_nativeHighlight(
        JNIEnv*, jclass,
        jlong j_src_mat, jlong j_dst_mat, jint j_width, jint j_height) {
    cv::Mat& src_mat = *(reinterpret_cast<cv::Mat*>(j_src_mat));
    cv::Mat& dst_mat = *(reinterpret_cast<cv::Mat*>(j_dst_mat));
    return Filters::highlight(src_mat, dst_mat, j_width, j_height);
}

extern "C" JNIEXPORT jboolean JNICALL
Java_budny_moneykeeper_cv_Operations_nativeSliceCentered(
        JNIEnv*, jclass,
        jlong j_src_mat, jlong j_dst_mat, jint j_width, jint j_height) {
    cv::Mat& src_mat = *(reinterpret_cast<cv::Mat*>(j_src_mat));
    cv::Mat& dst_mat = *(reinterpret_cast<cv::Mat*>(j_dst_mat));
    return Operations::sliceCentered(src_mat, dst_mat, j_width, j_height);
}

extern "C" JNIEXPORT jboolean JNICALL
Java_budny_moneykeeper_cv_Operations_nativeMergeCentered(
        JNIEnv*, jclass,
        jlong j_src_bottom_mat, jlong j_src_top_mat, jlong j_dst_mat) {
    cv::Mat& src_bottom_mat = *(reinterpret_cast<cv::Mat*>(j_src_bottom_mat));
    cv::Mat& src_top_mat = *(reinterpret_cast<cv::Mat*>(j_src_top_mat));
    cv::Mat& dst_mat = *(reinterpret_cast<cv::Mat*>(j_dst_mat));
    return Operations::mergeCentered(src_bottom_mat, src_top_mat, dst_mat);
}
