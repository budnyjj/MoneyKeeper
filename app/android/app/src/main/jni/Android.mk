LOCAL_PATH := $(call my-dir)
JNIAPP_PATH := $(LOCAL_PATH)/../jni
OPENCV_PATH := $(LOCAL_PATH)/../../../../thirdparty/opencv/native


include $(CLEAR_VARS)

OPENCV_CAMERA_MODULES := on
OPENCV_INSTALL_MODULES := on
OPENCV_LIB_TYPE := STATIC
include $(OPENCV_PATH)/jni/OpenCV.mk

LOCAL_MODULE := cv
LOCAL_SRC_FILES := \
	cv_jni.cpp \
	cv/filters.cpp \
	cv/operations.cpp \
    cv/recognizer.cpp \
	util/jnihelpers.cpp \
	util/logging.cpp
LOCAL_C_INCLUDES += $(JNIAPP_PATH)
LOCAL_C_INCLUDES += $(OPENCV_PATH)/jni/include

LOCAL_CXXFLAGS += -std=c++11 -fexceptions -frtti
LOCAL_LDLIBS += -landroid -llog -ldl

include $(BUILD_SHARED_LIBRARY)
