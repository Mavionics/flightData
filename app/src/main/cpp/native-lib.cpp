#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_com_github_mavionics_fligt_1data_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
