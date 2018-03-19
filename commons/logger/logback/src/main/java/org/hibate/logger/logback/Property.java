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
