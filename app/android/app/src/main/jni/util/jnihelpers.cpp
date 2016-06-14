#include "util/jnihelpers.hpp"


namespace jni {

std::string stdString(JNIEnv* j_env, jstring j_string) {
    const char* cs = j_env->GetStringUTFChars(j_string, nullptr);
    std::string s(cs);
    j_env->ReleaseStringUTFChars(j_string, cs);
    return s;
}

}
