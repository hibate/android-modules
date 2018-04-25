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

package org.hibate.logger.logback.joran.action;

import android.support.annotation.Nullable;

import org.hibate.logger.logback.Property;
import org.xml.sax.Attributes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.joran.action.ActionUtil;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import ch.qos.logback.core.util.Loader;
import ch.qos.logback.core.util.OptionHelper;

/**
 * Created by lenovo on 2018/3/18.
 */

public class PropertyAction extends Action {
    
    static final String RESOURCE_ATTRIBUTE = "resource";
    static String INVALID_ATTRIBUTES = "In <property> element, either the \"file\" attribute alone, " +
            "or the \"resource\" element alone, or both the \"name\" and \"value\" attributes must be set.";

    private PropertyContext propertyContext;

    public PropertyAction(@Nullable PropertyContext propertyContext) {
        this.propertyContext = propertyContext;
    }

    public void begin(InterpretationContext ec, String localName, Attributes attributes) {
        if ("substitutionProperty".equals(localName)) {
            this.addWarn("[substitutionProperty] element has been deprecated. Please use the [property] element instead.");
        }

        this.loadSupportProperties(ec);
        String name = attributes.getValue(NAME_ATTRIBUTE);
        String value = attributes.getValue(VALUE_ATTRIBUTE);
        String scopeStr = attributes.getValue(SCOPE_ATTRIBUTE);
        ActionUtil.Scope scope = ActionUtil.stringToScope(scopeStr);
        String resource;
        if (this.checkFileAttributeSanity(attributes)) {
            resource = attributes.getValue(FILE_ATTRIBUTE);
            resource = ec.subst(resource);

            try {
                FileInputStream resourceURL = new FileInputStream(resource);
                this.loadAndSetProperties(ec, resourceURL, scope);
            } catch (FileNotFoundException var12) {
                this.addError("Could not find properties file [" + resource + "].");
            } catch (IOException var13) {
                this.addError("Could not read properties file [" + resource + "].", var13);
            }
        } else if (this.checkResourceAttributeSanity(attributes)) {
            resource = attributes.getValue(RESOURCE_ATTRIBUTE);
            resource = ec.subst(resource);
            URL resourceURL1 = Loader.getResourceBySelfClassLoader(resource);
            if (resourceURL1 == null) {
                this.addError("Could not find resource [" + resource + "].");
            } else {
                try {
                    InputStream e = resourceURL1.openStream();
                    this.loadAndSetProperties(ec, e, scope);
                } catch (IOException var11) {
                    this.addError("Could not read resource file [" + resource + "].", var11);
                }
            }
        } else if (this.checkValueNameAttributesSanity(attributes)) {
            value = RegularEscapeUtil.basicEscape(value);
            value = value.trim();
            value = ec.subst(value);
            ActionUtil.setProperty(ec, name, value, scope);
        } else {
            this.addError(INVALID_ATTRIBUTES);
        }
    }

    void loadAndSetProperties(InterpretationContext ec, InputStream istream, ActionUtil.Scope scope) throws IOException {
        Properties props = new Properties();
        props.load(istream);
        istream.close();
        ActionUtil.setProperties(ec, props, scope);
    }

    /**
     * 加载自定义属性
     */
    void loadSupportProperties(InterpretationContext ec) {
        List<Property> properties = (this.propertyContext == null) ? null : this.propertyContext.getPropertyList();
        if (properties != null) {
            Iterator<Property> iterator = properties.iterator();

            while (iterator.hasNext()) {
                Property property = iterator.next();
                if (property != null) {
                    String name = property.getName();
                    String value = property.getValue();
                    String scopeStr = property.getScope();
                    ActionUtil.Scope scope = ActionUtil.stringToScope(scopeStr);
                    value = RegularEscapeUtil.basicEscape(value);
                    value = value.trim();
                    value = ec.subst(value);
                    ActionUtil.setProperty(ec, name, value, scope);
                }
            }
        }
    }

    boolean checkFileAttributeSanity(Attributes attributes) {
        String file = attributes.getValue(FILE_ATTRIBUTE);
        String name = attributes.getValue(NAME_ATTRIBUTE);
        String value = attributes.getValue(VALUE_ATTRIBUTE);
        String resource = attributes.getValue(RESOURCE_ATTRIBUTE);
        return !OptionHelper.isEmpty(file) && OptionHelper.isEmpty(name) &&
                OptionHelper.isEmpty(value) && OptionHelper.isEmpty(resource);
    }

    boolean checkResourceAttributeSanity(Attributes attributes) {
        String file = attributes.getValue(FILE_ATTRIBUTE);
        String name = attributes.getValue(NAME_ATTRIBUTE);
        String value = attributes.getValue(VALUE_ATTRIBUTE);
        String resource = attributes.getValue(RESOURCE_ATTRIBUTE);
        return !OptionHelper.isEmpty(resource) && OptionHelper.isEmpty(name) &&
                OptionHelper.isEmpty(value) && OptionHelper.isEmpty(file);
    }

    boolean checkValueNameAttributesSanity(Attributes attributes) {
        String file = attributes.getValue(FILE_ATTRIBUTE);
        String name = attributes.getValue(NAME_ATTRIBUTE);
        String value = attributes.getValue(VALUE_ATTRIBUTE);
        String resource = attributes.getValue(RESOURCE_ATTRIBUTE);
        return !OptionHelper.isEmpty(name) && !OptionHelper.isEmpty(value) &&
                OptionHelper.isEmpty(file) && OptionHelper.isEmpty(resource);
    }

    public void end(InterpretationContext ec, String name) {
    }

    public void finish(InterpretationContext ec) {
    }
}
