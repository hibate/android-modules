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
// Created by Hibate on 2018/4/24.
//

#include "jniutil.h"
#include "errcode.h"
#include "logger.h"
#include "jni_logger_handler.h"

#define className "org/hibate/logger/nativelogger/LoggerContextImpl"

void logger_context_initialize(JNIEnv* env, jobject thiz, jobject jHandler) {
    if (NULL == jHandler) {
        return;
    }

    LoggerConfigurator* configurator = LoggerConfigurator::getInstance();
    configurator->addLoggerListener(new LoggerHandler(env, jHandler));
}

jboolean logger_context_isLoggable(JNIEnv* env, jobject thiz, jint jPriority) {
    LOG_PRIORITY priority;
    if (int2Priority((int) jPriority, &priority) < 0) {
        return JNI_FALSE;
    }

    int loggable = 0;
    LoggerConfigurator* configurator = LoggerConfigurator::getInstance();
    configurator->isLoggable(priority, &loggable);
    return (0 != loggable) ? JNI_TRUE : JNI_FALSE;
}

jboolean logger_context_setPriority(JNIEnv* env, jobject thiz, jint jPriority) {
    LOG_PRIORITY priority;
    if (int2Priority((int) jPriority, &priority) < 0) {
        return JNI_FALSE;
    }

    LoggerConfigurator* configurator = LoggerConfigurator::getInstance();
    return (RET_NO_ERROR == configurator->setPriority(priority)) ? JNI_TRUE : JNI_FALSE;
}

jboolean logger_context_isLineLoggable(JNIEnv* env, jobject thiz) {
    int loggable = 0;
    LoggerConfigurator* configurator = LoggerConfigurator::getInstance();
    configurator->isLineEnabled(&loggable);
    return (0 != loggable) ? JNI_TRUE : JNI_FALSE;
}

jboolean logger_context_setLineLoggable(JNIEnv* env, jobject thiz, jboolean jLoggable) {
    LoggerConfigurator* configurator = LoggerConfigurator::getInstance();
    return (RET_NO_ERROR == configurator->setLineEnabled((JNI_TRUE == jLoggable) ? 1 : 0)) ?
           JNI_TRUE : JNI_FALSE;
}

static JNINativeMethod methods[] = {
        {"_initialize", "(Lorg/hibate/logger/nativelogger/LoggerHandler;)V", (void*) logger_context_initialize},
        {"_isLoggable", "(I)Z", (void*) logger_context_isLoggable},
        {"_setPriority", "(I)Z", (void*) logger_context_setPriority},
        {"_isLineLoggable", "()Z", (void*) logger_context_isLineLoggable},
        {"_setLineLoggable", "(Z)Z", (void*) logger_context_setLineLoggable},
};

int register_logger_context(JNIEnv* env) {
    return registerNativeMethods(env, className, methods, NELEM(methods));
}

void unregister_logger_context(JNIEnv* env) {
}

MethodLoader logger_context_loader = {register_logger_context, unregister_logger_context};