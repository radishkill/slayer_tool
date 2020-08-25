#include <jni.h>
#include <string>
#include <iostream>

extern "C" JNIEXPORT jstring JNICALL
Java_com_slayer_slayertool_activity_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";

    return env->NewStringUTF(hello.c_str());
}
