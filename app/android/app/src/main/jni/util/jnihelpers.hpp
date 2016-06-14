#ifndef UTIL_JNIHELPERS_HPP_
#define UTIL_JNIHELPERS_HPP_

#include <string>

#include <jni.h>


namespace jni {

std::string stdString(JNIEnv* j_env, jstring j_string);

}

#endif  // define UTIL_JNIHELPERS_HPP_
