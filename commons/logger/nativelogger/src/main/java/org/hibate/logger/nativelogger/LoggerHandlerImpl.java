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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.hibate.logger.nativelogger.LoggerContextImpl.getPriority;

/**
 * Created by Hibate on 2018/4/24.
 */

class LoggerHandlerImpl implements LoggerHandler, LoggerRegistration {

    private Set<LoggerListener> listeners;
    private ReadWriteLock lock;

    public LoggerHandlerImpl() {
        this.listeners = new HashSet<>();
        this.lock = new ReentrantReadWriteLock();
    }

    @Override
    public void onLogger(String tag, int priority, String location, String msg) {
        this.lock.readLock().lock();
        Iterator<LoggerListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            LoggerListener listener = iterator.next();
            if (listener == null) {
                continue;
            }
            listener.onLogger(tag, getPriority(priority), location, msg);
        }
        this.lock.readLock().unlock();
    }

    @Override
    public void addLoggerListener(LoggerListener listener) {
        this.lock.writeLock().lock();
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
        this.lock.writeLock().unlock();
    }
}
