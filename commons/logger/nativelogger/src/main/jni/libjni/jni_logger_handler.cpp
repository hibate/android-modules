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
#include "exceptions.h"
#include "jni_logger_handler.h"

LoggerHandler::LoggerHandler(JNIEnv *env, jobject jReference) {
    this->handler = (NULL == jReference) ? NULL : env->NewGlobalRef(jReference);

    if (NULL != this->handler) {
        jclass clazz = env->GetObjectClass(this->handler);

        fields.onLogger = env->GetMethodID(clazz, "onLogger", "(Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;)V");
        if (NULL == fields.onLogger) {
            throwException(env, RUNTIME_EXCEPTION,
                           "Cannot find onLogger method in LoggerHandler class");
            return;
        }
    }
}

LoggerHandler::~LoggerHandler() {
    JavaVM* vm = NULL;
    JNIEnv* env = NULL;

    if (NULL != this->handler) {
        env = getJniEnv();
        if (NULL == env) {
            vm = getVM();
            if (!vm) {
                goto END;
            }
            vm->AttachCurrentThread(&env, NULL);
            if (!env) {
                goto END;
            }
            env->DeleteGlobalRef(this->handler);
            vm->DetachCurrentThread();
        } else {
            env->DeleteGlobalRef(this->handler);
        }
    }

END:
    this->handler = NULL;
    this->fields.onLogger = NULL;
}

int LoggerHandler::onLogging(JNIEnv *env, const char *tag, LOG_PRIORITY priority,
                             const char *location, const char *message) {
    jstring jTag = (NULL == tag) ? NULL : env->NewStringUTF(tag);
    jstring jLocation = (NULL == location) ? NULL : env->NewStringUTF(location);
    jstring jMessage = (NULL == message) ? NULL : env->NewStringUTF(message);

    env->CallVoidMethod(this->handler, this->fields.onLogger,
                        jTag, (jint) priority, jLocation, jMessage);

    if (NULL != jTag) {
        env->DeleteLocalRef(jTag);
    }
    if (NULL != jLocation) {
        env->DeleteLocalRef(jLocation);
    }
    if (NULL != jMessage) {
        env->DeleteLocalRef(jMessage);
    }
    return 0;
}

int LoggerHandler::onLogging(const char *tag, LOG_PRIORITY priority, const char *location,
                             const char *message) {
    JavaVM* vm = NULL;
    JNIEnv* env = NULL;

    if ((NULL != this->handler) &&
            (NULL != this->fields.onLogger)) {
        env = getJniEnv();
        if (NULL == env) {
            vm = getVM();
            if (!vm) {
                return 0;
            }
            vm->AttachCurrentThread(&env, NULL);
            if (!env) {
                return 0;
            }
            this->onLogging(env, tag, priority, location, message);
            vm->DetachCurrentThread();
        } else {
            this->onLogging(env, tag, priority, location, message);
        }
    }

    return 0;
}