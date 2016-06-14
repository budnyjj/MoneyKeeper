#include "util/logging.hpp"

#include <android/log.h>


namespace logging {

void d(const std::string& tag, const std::string value) {
    __android_log_print(ANDROID_LOG_DEBUG, tag.c_str(), "%s", value.c_str());
}

}
