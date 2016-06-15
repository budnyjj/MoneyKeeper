#include "budny_moneykeeper_cv_Filters.h"
#include "budny_moneykeeper_cv_Operations.h"
#include "budny_moneykeeper_cv_Recognizer.h"

#include <string>

#include <android/asset_manager_jni.h>

#include <opencv2/core/core.hpp>

#include "cv/filters.hpp"
#include "cv/operations.hpp"
#include "cv/recognizer.hpp"
#include "util/jnihelpers.hpp"
#include "util/logging.hpp"


extern "C" JNIEXPORT jboolean JNICALL
Java_budny_moneykeeper_cv_Filters_nativeBasic(
        JNIEnv*, jclass,
        jlong j_src_mat, jlong j_dst_mat) {
    cv::Mat& src_mat = *(reinterpret_cast<cv::Mat*>(j_src_mat));
    cv::Mat& dst_mat = *(reinterpret_cast<cv::Mat*>(j_dst_mat));
    return Filters::basic(src_mat, dst_mat);
}

extern "C" JNIEXPORT jboolean JNICALL
Java_budny_moneykeeper_cv_Filters_nativeContours(
        JNIEnv*, jclass,
        jlong j_src_mat, jlong j_dst_mat) {
    cv::Mat& src_mat = *(reinterpret_cast<cv::Mat*>(j_src_mat));
    cv::Mat& dst_mat = *(reinterpret_cast<cv::Mat*>(j_dst_mat));
    return Filters::contours(src_mat, dst_mat);
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

extern "C" JNIEXPORT jlong JNICALL
Java_budny_moneykeeper_cv_Recognizer_nativeInitialize(
       JNIEnv* j_env, jobject,
       jobject j_manager, jstring j_model_filename) {
    AAssetManager* manager = AAssetManager_fromJava(j_env, j_manager);
    std::string model_filename = jni::stdString(j_env, j_model_filename);
    Recognizer* recognizer = new Recognizer(manager, model_filename);
    return reinterpret_cast<jlong>(recognizer);
}

extern "C" JNIEXPORT void JNICALL
Java_budny_moneykeeper_cv_Recognizer_nativeDispose(
        JNIEnv*, jobject,
        jlong j_recognizer) {
    Recognizer* recognizer = reinterpret_cast<Recognizer*>(j_recognizer);
    delete recognizer;
}

extern "C" JNIEXPORT void JNICALL
Java_budny_moneykeeper_cv_Recognizer_nativeRecognize(
       JNIEnv*, jobject,
       jlong j_recognizer, jlong j_src_mat, jlong j_dst_mat) {
    Recognizer* recognizer = reinterpret_cast<Recognizer*>(j_recognizer);
    cv::Mat& src_mat = *(reinterpret_cast<cv::Mat*>(j_src_mat));
    cv::Mat& dst_mat = *(reinterpret_cast<cv::Mat*>(j_dst_mat));
    recognizer->recognize(src_mat, dst_mat);
}
