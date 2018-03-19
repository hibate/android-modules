package org.hibate.logger.logback.joran.action;

import android.support.annotation.Nullable;

import org.hibate.logger.logback.Property;

import java.util.List;

/**
 * Created by Hibate on 2018/3/19.
 */

public interface PropertyContext {

    @Nullable
    List<Property> getPropertyList();
}
