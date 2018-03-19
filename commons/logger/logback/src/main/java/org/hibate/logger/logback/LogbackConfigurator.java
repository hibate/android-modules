package org.hibate.logger.logback;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.hibate.logger.logback.joran.JoranConfigurator;
import org.hibate.logger.logback.joran.action.PropertyContext;
import org.slf4j.impl.StaticLoggerBinder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.selector.ContextSelector;
import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * Created by lenovo on 2018/3/18.
 */

public class LogbackConfigurator {

    private static final String LOGGER_FILE = "logback.xml";
    private static final LogbackProperty LOGBACK_PROPERTY = new LogbackProperty();

    private LogbackConfigurator() {
    }

    private static String getExternalFilesDirPath(@NonNull Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File filesDir = context.getExternalCacheDir();
            if(filesDir == null) {
                filesDir = new File(Environment.getExternalStorageDirectory(), "Android/data/" + context.getPackageName() + "/files/");
            } else if(filesDir.getAbsolutePath().endsWith("cache")) {
                filesDir = new File(filesDir.getParent(), "files");
            }

            return filesDir.getAbsolutePath();
        } else {
            return null;
        }
    }

    private static String getInternalFilesDirPath(@NonNull Context context) {
        return context.getFilesDir().getAbsolutePath();
    }

    private static String getFilesDirPath(@NonNull Context context) {
        String cacheDirPath = getExternalFilesDirPath(context);
        if ((cacheDirPath == null) ||
                (cacheDirPath.trim().length() == 0)) {
            cacheDirPath = getInternalFilesDirPath(context);
        }

        return cacheDirPath;
    }

    public static String getContextFilesHome(@NonNull Context context) {
        return getFilesDirPath(context);
    }

    public static String getContextLogHome(@NonNull Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append(getFilesDirPath(context))
                .append(File.separator)
                .append("log");
        return sb.toString();
    }

    public static void configure(@NonNull Context context) {
        AssetManager assetManager = context.getAssets();

        try {
            InputStream inputStream = assetManager.open(LOGGER_FILE);
            if(inputStream == null) {
                throw new RuntimeException("Cannot open logback.xml file at assets folder");
            } else {
                configure(context, inputStream);
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Cannot open logback.xml file at assets folder");
        }
    }

    public static void configure(@NonNull Context context, InputStream inputStream) {
        LOGBACK_PROPERTY.addProperty("context.files.home",
                LogbackConfigurator.getContextFilesHome(context), Property.LOCAL);
        LOGBACK_PROPERTY.addProperty("context.log.home",
                LogbackConfigurator.getContextLogHome(context), Property.LOCAL);

        if (inputStream != null) {
            LoggerContext loggerContext = (LoggerContext) StaticLoggerBinder.getSingleton().getLoggerFactory();
            loggerContext.reset();
            JoranConfigurator configurator = new JoranConfigurator(LOGBACK_PROPERTY);
            configurator.setContext(loggerContext);

            try {
                configurator.doConfigure(inputStream);
            } catch (JoranException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void reset() {
        ContextSelector selector = ContextSelectorStaticBinder.getSingleton().getContextSelector();
        LoggerContext loggerContext = selector.getLoggerContext();
        String loggerContextName = loggerContext.getName();
        LoggerContext context = selector.detachLoggerContext(loggerContextName);
        context.reset();
        LOGBACK_PROPERTY.reset();
    }

    public static LogbackContext getLogbackContext() {
        return LOGBACK_PROPERTY;
    }

    private static class LogbackProperty implements PropertyContext, LogbackContext {

        private List<Property> propertyList;

        public LogbackProperty() {
            this.propertyList = new Vector<>();
        }

        @Override
        public List<Property> getPropertyList() {
            return this.propertyList;
        }

        public void reset() {
            this.propertyList.clear();
        }

        public LogbackContext addProperty(@NonNull final String name, final String value, final String scope) {
            return this.addProperty(new Property() {
                @NonNull
                @Override
                public String getName() {
                    return name;
                }

                @Override
                public String getValue() {
                    return value;
                }

                @Override
                public String getScope() {
                    return scope;
                }
            });
        }

        @Override
        public LogbackContext addProperty(@Nullable Property property) {
            this.propertyList.add(property);
            return this;
        }
    }
}
