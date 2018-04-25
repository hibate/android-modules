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

package org.hibate.logger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.hibate.logger.nativelogger.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainActivity extends AppCompatActivity {

    private static final Logger logger =
            LoggerFactory.getLogger("MainActivity");

    static {
        System.loadLibrary("native_logger_demo");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");

        log_message(Priority.DEBUG,"debug");
        log_message(Priority.INFO, "info");
        log_message(Priority.WARN, "warn");
        log_message(Priority.ERROR, "error");
    }

    private static int getPriority(Priority priority) {
        return (priority == null) ? 1 : (priority.ordinal() + 1);
    }

    private void log_message(Priority priority, String format, Object ... args) {
        _log_message(getPriority(priority), String.format(format, args));
    }

    private native void _log_message(int priority, String message);
}
