//
// Created by maciu on 07/01/19.
//
#include <stdio.h>
#include <malloc.h>
#include <string.h>

#include "hping2/hping2.h"
#include "Hping2.h"

static JavaVM* vm_reference;


int ktprint(char const* format, ...)
{
    static char huge_buf[8192] = {0};

    int printed;
    va_list ap;
    va_start(ap, format);
    printed = vsnprintf(huge_buf, sizeof(huge_buf), format, ap);
    va_end(ap);

    JNIEnv* env;
    if ((*vm_reference)->GetEnv(vm_reference, (void**)(&env), JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }

    jstring test = (*env)->NewStringUTF(env, huge_buf);
    jclass system_class = (*env)->FindClass(env, "java/lang/System");
    jfieldID out_id = (*env)->GetStaticFieldID(env, system_class, "out", "Ljava/io/PrintStream;");

    jclass print_stream_class = (*env)->FindClass(env, "java/io/PrintStream");
    jobject print_stream = (*env)->GetStaticObjectField(env, system_class, out_id);
    jmethodID print_method_id = (*env)->GetMethodID(env, print_stream_class, "print", "(Ljava/lang/String;)V");
    jmethodID flush_method_id = (*env)->GetMethodID(env, print_stream_class, "flush", "()V");

    (*env)->CallVoidMethod(env, print_stream, print_method_id, test);
    (*env)->CallVoidMethod(env, print_stream, flush_method_id);

    return printed;
}

JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
    JNIEnv* env;
    if ((*vm)->GetEnv(vm, (void**)(&env), JNI_VERSION_1_6) != JNI_OK) {
        return -1;
    }

    vm_reference = vm;

    // Get jclass with env->FindClass.
    // Register methods with env->RegisterNatives.

    return JNI_VERSION_1_6;
}

JNIEXPORT jint JNICALL Java_com_root_hping2_Hping2_00024Companion_invokeHping2 (JNIEnv * env, jobject thiz, jobjectArray jargv)
{
    jsize jargc = (*env)->GetArrayLength(env, jargv);
    char** argv = malloc(sizeof(char*) * jargc);
    for (jsize i = 0; i < jargc; ++i)
    {
        jstring str = (*env)->GetObjectArrayElement(env, jargv, i);
        const char* utfstr_const = (*env)->GetStringUTFChars(env, str, NULL);
        size_t len = (size_t)(*env)->GetStringUTFLength(env, str);
        char* utfstr = malloc(len + 1);
        memcpy(utfstr, utfstr_const, len);
        utfstr[len] = 0;
        argv[i] = utfstr;
    }

    int hping2Result = hping2(jargc, argv);

    for (jsize i = 0; i < jargc; ++i)
    {
        free(argv[i]);
    }
    free(argv);

    return hping2Result;
}
