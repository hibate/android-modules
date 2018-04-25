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

#ifndef NATIVE_LOGGER_JNI_LOGGER_HANDLER_H
#define NATIVE_LOGGER_JNI_LOGGER_HANDLER_H

#include <jni.h>
#include "logger_configurator.h"

class LoggerHandler : public LoggerListener {
private:
    struct fields_t {
        jmethodID onLogger;
    };

    struct fields_t fields;
    jobject handler;

protected:
    int onLogging(JNIEnv* env, const char* tag, LOG_PRIORITY priority,
                  const char* location, const char* message);

public:
    LoggerHandler(JNIEnv* env, jobject jReference);
    ~LoggerHandler();
    int onLogging(const char* tag, LOG_PRIORITY priority,
                  const char* location, const char* message);
};

#endif //NATIVE_LOGGER_JNI_LOGGER_HANDLER_H
