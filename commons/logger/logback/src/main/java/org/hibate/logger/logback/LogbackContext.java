package org.hibate.logger.logback;

import android.support.annotation.Nullable;

/**
 * Created by Hibate on 2018/3/19.
 */

public interface LogbackContext {

    LogbackContext addProperty(@Nullable Property property);
}
