/*
 * Copyright (C) 2018 Hibate <ycaia86@126.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * jniutil.cpp
 *
 *  Created on: 2017/6/19
 *      Author: Hibate
 */

#include <android/log.h>
#include "jniutil.h"
#include "onload.h"

#define HB_TAG "OnLoad"
#define HB_ERROR(format, ...) __android_log_print(ANDROID_LOG_ERROR, HB_TAG, format, ##__VA_ARGS__)
#define HB_INFO(format, ...) __android_log_print(ANDROID_LOG_INFO, HB_TAG, format, ##__VA_ARGS__)

#define _JNI_VERSION_ JNI_VERSION_1_6

static JavaVM *sVm;

int throwException(JNIEnv* env, const char* className, const char* msg) {
    jclass exceptionClass = env->FindClass(className);
    if (exceptionClass == NULL) {
        HB_ERROR("Unable to find exception class %s", className);
        return -1;
    }

    if (env->ThrowNew(exceptionClass, msg) != JNI_OK) {
        HB_ERROR("Failed throwing '%s' '%s'", className, msg);
    }

    env->DeleteLocalRef(exceptionClass);
    return 0;
}

int throwUserException(JNIEnv* env, jclass exceptionClass, const char* msg) {
    if (exceptionClass == NULL) {
        HB_ERROR("Exception class can't be null");
        return -1;
    }

    if (env->ThrowNew(exceptionClass, msg) != JNI_OK) {
        HB_ERROR("Failed throwing '%s'", msg);
    }

    env->DeleteLocalRef(exceptionClass);
    return 0;
}

JavaVM* getVM() {
    if (NULL != sVm) {
        return sVm;
    }
    return NULL;
}

JNIEnv* getJniEnv() {
    JNIEnv* env = NULL;
    if (JNI_OK != sVm->GetEnv((void**) &env, _JNI_VERSION_)) {
        return NULL;
    }

    return env;
}

int registerNativeMethods(JNIEnv* env, const char* className,
        const JNINativeMethod* methods, int numMethods) {
    jclass clazz;

    HB_INFO("Registering '%s' natives", className);
    clazz = env->FindClass(className);
    if (clazz == NULL) {
        HB_ERROR("Native registration unable to find class '%s'", className);
        return -1;
    }
    if (env->RegisterNatives(clazz, methods, numMethods) < 0) {
        HB_ERROR("RegisterNatives failed for '%s'", className);
        return -1;
    }

    return 0;
}

int registerMethod(JNIEnv* env, MethodLoader* methodLoader) {
    if (NULL == methodLoader) {
        return JNI_OK;
    }
    if (methodLoader->registerMethod) {
        if (JNI_OK != methodLoader->registerMethod(env)) {
            return JNI_ERR;
        }
    }
    return JNI_OK;
}

void unregisterMethod(JNIEnv* env, MethodLoader* methodLoader) {
    if (NULL == methodLoader) {
        return;
    }
    if (methodLoader->unregisterMethod) {
        methodLoader->unregisterMethod(env);
    }
}

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    JNIEnv* env = NULL;
    jint result = JNI_ERR;
    sVm = vm;

    if (JNI_OK != vm->GetEnv((void**) &env, _JNI_VERSION_)) {
        HB_ERROR("GetEnv failed!");
        return result;
    }

    HB_INFO("Loading ...");

    MethodLoader* loaders = NULL;
    int size = 0;
    if ((0 != getMethodLoaders(&loaders, &size)) || (NULL == loaders)) {
        HB_ERROR("Initialize native methods failed!");
        return result;
    }
    while (size > 0) {
        MethodLoader methodLoader = *loaders++;
        if (JNI_OK != registerMethod(env, &methodLoader)) {
            return result;
        }
        size--;
    }

    HB_INFO("Loaded ...");

    result = _JNI_VERSION_;
    env = NULL;

    return result;
}

void JNI_OnUnload(JavaVM* vm, void* reserved) {
    JNIEnv* env = NULL;

    if (vm->GetEnv((void**) &env, _JNI_VERSION_) != JNI_OK) {
        HB_ERROR("GetEnv failed!");
        return;
    }

    HB_INFO("UnLoading ...");

    MethodLoader* loaders = NULL;
    int size = 0;
    if ((0 != getMethodLoaders(&loaders, &size)) || (NULL == loaders)) {
        HB_ERROR("Initialize native methods failed!");
    }
    while (size > 0) {
        MethodLoader methodLoader = *loaders++;
        unregisterMethod(env, &methodLoader);
        size--;
    }

    HB_INFO("UnLoaded");

    env = NULL;
}
