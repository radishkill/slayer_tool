//
// Created by slayer on 7/24/20.
//

#include <jni.h>
#include <string>
#include <iostream>
#include <sstream>
#include <thread>

extern "C" JNIEXPORT jstring JNICALL
Java_com_slayer_slayertool_activity_MainActivity_GetThreadId(
        JNIEnv* env,
        jobject /* this */) {
    std::ostringstream ss;
    ss << std::this_thread::get_id();
    return env->NewStringUTF(ss.str().c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_slayer_slayertool_activity_AutoLocationActivity_GetThreadId(
        JNIEnv* env,
jobject /* this */) {
    std::ostringstream ss;
    ss << std::this_thread::get_id();
    return env->NewStringUTF(ss.str().c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_slayer_slayertool_service_MainService_GetThreadId(
        JNIEnv* env,
        jobject /* this */) {
    std::ostringstream ss;
    ss << std::this_thread::get_id();
    return env->NewStringUTF(ss.str().c_str());
}
