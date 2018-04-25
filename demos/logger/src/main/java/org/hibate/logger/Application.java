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

import org.hibate.logger.logback.LogbackConfigurator;
import org.hibate.logger.nativelogger.LoggerContext;
import org.hibate.logger.nativelogger.LoggerContextFactory;
import org.hibate.logger.nativelogger.LoggerOverSlf4j;
import org.hibate.logger.nativelogger.Priority;

/**
 * Created by Hibate on 2018/4/25.
 */

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LogbackConfigurator.configure(this);
        LoggerContext loggerContext = LoggerContextFactory.getLoggerContext();
        loggerContext.setPriority(Priority.DEBUG);
        loggerContext.setLineLoggable(true);
        loggerContext.addLoggerListener(new LoggerOverSlf4j());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        LogbackConfigurator.reset();
    }
}
