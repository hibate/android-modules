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

package org.hibate.logger.logback.joran;

import android.support.annotation.Nullable;

import org.hibate.logger.logback.joran.action.PropertyAction;
import org.hibate.logger.logback.joran.action.PropertyContext;

import java.util.HashMap;
import java.util.Map;

import ch.qos.logback.core.joran.GenericConfigurator;
import ch.qos.logback.core.joran.action.AppenderAction;
import ch.qos.logback.core.joran.action.AppenderRefAction;
import ch.qos.logback.core.joran.action.ContextPropertyAction;
import ch.qos.logback.core.joran.action.ConversionRuleAction;
import ch.qos.logback.core.joran.action.DefinePropertyAction;
import ch.qos.logback.core.joran.action.NestedBasicPropertyIA;
import ch.qos.logback.core.joran.action.NestedComplexPropertyIA;
import ch.qos.logback.core.joran.action.NewRuleAction;
import ch.qos.logback.core.joran.action.ParamAction;
import ch.qos.logback.core.joran.action.ShutdownHookAction;
import ch.qos.logback.core.joran.action.StatusListenerAction;
import ch.qos.logback.core.joran.action.TimestampAction;
import ch.qos.logback.core.joran.spi.ElementSelector;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.joran.spi.Interpreter;
import ch.qos.logback.core.joran.spi.RuleStore;

/**
 * Created by lenovo on 2018/3/18.
 */

public abstract class JoranConfiguratorBase<E> extends GenericConfigurator {

    private PropertyContext propertyContext;

    public JoranConfiguratorBase(@Nullable PropertyContext propertyContext) {
        this.propertyContext = propertyContext;
    }

    protected void addInstanceRules(RuleStore rs) {
        rs.addRule(new ElementSelector("configuration/variable"), new PropertyAction(this.propertyContext));
        rs.addRule(new ElementSelector("configuration/property"), new PropertyAction(this.propertyContext));
        rs.addRule(new ElementSelector("configuration/substitutionProperty"), new PropertyAction(this.propertyContext));
        rs.addRule(new ElementSelector("configuration/timestamp"), new TimestampAction());
        rs.addRule(new ElementSelector("configuration/shutdownHook"), new ShutdownHookAction());
        rs.addRule(new ElementSelector("configuration/define"), new DefinePropertyAction());
        rs.addRule(new ElementSelector("configuration/contextProperty"), new ContextPropertyAction());
        rs.addRule(new ElementSelector("configuration/conversionRule"), new ConversionRuleAction());
        rs.addRule(new ElementSelector("configuration/statusListener"), new StatusListenerAction());
        rs.addRule(new ElementSelector("configuration/appender"), new AppenderAction());
        rs.addRule(new ElementSelector("configuration/appender/appender-ref"), new AppenderRefAction());
        rs.addRule(new ElementSelector("configuration/newRule"), new NewRuleAction());
        rs.addRule(new ElementSelector("*/param"), new ParamAction(this.getBeanDescriptionCache()));
    }

    protected void addImplicitRules(Interpreter interpreter) {
        NestedComplexPropertyIA nestedComplexPropertyIA = new NestedComplexPropertyIA(this.getBeanDescriptionCache());
        nestedComplexPropertyIA.setContext(this.context);
        interpreter.addImplicitAction(nestedComplexPropertyIA);
        NestedBasicPropertyIA nestedBasicIA = new NestedBasicPropertyIA(this.getBeanDescriptionCache());
        nestedBasicIA.setContext(this.context);
        interpreter.addImplicitAction(nestedBasicIA);
    }

    protected void buildInterpreter() {
        super.buildInterpreter();
        Map omap = this.interpreter.getInterpretationContext().getObjectMap();
        omap.put("APPENDER_BAG", new HashMap());
    }

    public InterpretationContext getInterpretationContext() {
        return this.interpreter.getInterpretationContext();
    }
}
