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

package org.hibate.logger.logback;

import android.support.annotation.NonNull;

/**
 * Created by lenovo on 2018/3/18.
 */

public interface Property {

    String LOCAL = "local";
    String CONTEXT = "context";
    String SYSTEM = "system";

    @NonNull
    String getName();

    String getValue();

    String getScope();
}
