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

//
// Created by Hibate on 2018/4/25.
//

#define TAG "MainActivity_Native"

#include "jniutil.h"
#include "logger.h"

#define className "org/hibate/logger/MainActivity"

void main_acitivity_log_message(JNIEnv* env, jobject thiz, jint jPriority, jstring jMessage) {
    const char* msg = (NULL == jMessage) ? NULL : env->GetStringUTFChars(jMessage, JNI_FALSE);

    switch ((int) jPriority) {
        case LOG_VERBOSE:
            LVERBOSE("%s", (NULL == msg) ? "null" : msg);
            break;
        case LOG_DEBUG:
            LDEBUG("%s", (NULL == msg) ? "null" : msg);
            break;
        case LOG_INFO:
            LINFO("%s", (NULL == msg) ? "null" : msg);
            break;
        case LOG_WARN:
            LWARN("%s", (NULL == msg) ? "null" : msg);
            break;
        case LOG_ERROR:
            LERROR("%s", (NULL == msg) ? "null" : msg);
            break;
        case LOG_FATAL:
            LFATAL("%s", (NULL == msg) ? "null" : msg);
            break;
        case LOG_SILENT:
            LSILENT("%s", (NULL == msg) ? "null" : msg);
            break;
    }

    if (NULL != msg) {
        env->ReleaseStringUTFChars(jMessage, msg);
    }
}

static JNINativeMethod methods[] = {
        {"_log_message", "(ILjava/lang/String;)V", (void*) main_acitivity_log_message},
};

int register_main_activity(JNIEnv* env) {
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_main_activity(JNIEnv* env) {
    //
}

MethodLoader main_activity_loader = {register_main_activity, unregister_main_activity};