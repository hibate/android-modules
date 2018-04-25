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

package org.hibate.logger.nativelogger;

/**
 * Created by Hibate on 2018/4/24.
 */

class LoggerContextImpl implements LoggerContext {

    private static final LibraryLoader LOADER =
            new LibraryLoader("nativelogger");

    private LoggerRegistration registration;

    public LoggerContextImpl() {
        LoggerHandlerImpl handler = new LoggerHandlerImpl();
        this.registration = handler;
        if (isAvailable()) {
            this._initialize(handler);
        }
    }

    @Override
    public boolean isAvailable() {
        return LOADER.isAvailable();
    }

    @Override
    public boolean isLoggable(Priority priority) {
        return ((priority == null) || !isAvailable()) ? false : this._isLoggable(getPriority(priority));
    }

    @Override
    public void setPriority(Priority priority) {
        if ((priority == null) || !isAvailable()) {
            return;
        }
        this._setPriority(getPriority(priority));
    }

    @Override
    public boolean isLineLoggable() {
        return !isAvailable() ? false : this._isLineLoggable();
    }

    @Override
    public void setLineLoggable(boolean loggable) {
        if (isAvailable()) {
            this._setLineLoggable(loggable);
        }
    }

    @Override
    public void addLoggerListener(LoggerListener listener) {
        isAvailable();
        this.registration.addLoggerListener(listener);
    }

    public static int getPriority(Priority priority) {
        return (priority == null) ? 1 : (priority.ordinal() + 1);
    }

    public static Priority getPriority(int priority) {
        priority = priority - 1;
        Priority[] values = Priority.values();
        if ((priority < 0) || (priority >= values.length)) {
            return Priority.DEBUG;
        }
        return values[priority];
    }

    private native void _initialize(LoggerHandler handler);
    private native boolean _isLoggable(int priority);
    private native boolean _setPriority(int priority);
    private native boolean _isLineLoggable();
    private native boolean _setLineLoggable(boolean loggable);
}
