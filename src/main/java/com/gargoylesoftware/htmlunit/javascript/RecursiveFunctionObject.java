/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WEBGL_CONTEXT_EVENT_CONSTANTS;

import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.configuration.AbstractJavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration.ConstantInfo;

import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * A FunctionObject that returns IDs of this object and all its parent classes.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class RecursiveFunctionObject extends FunctionObject {

    private final BrowserVersion browserVersion_;

    /**
     * The constructor.
     * @param name the name of the function
     * @param methodOrConstructor a {@link Member} that defines the object
     * @param scope the enclosing scope of function
     * @param browserVersion the browserVersion
     */
    public RecursiveFunctionObject(final String name, final Member methodOrConstructor,
            final Scriptable scope, final BrowserVersion browserVersion) {
        super(name, methodOrConstructor, scope);
        browserVersion_ = browserVersion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean has(final String name, final Scriptable start) {
        if (super.has(name, start)) {
            return true;
        }
        for (Class<?> c = getMethodOrConstructor().getDeclaringClass().getSuperclass();
                c != null; c = c.getSuperclass()) {
            final Object scripatble = getParentScope().get(c.getSimpleName(), this);
            if (scripatble instanceof Scriptable && ((Scriptable) scripatble).has(name, start)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] getIds() {
        final Set<Object> objects = new LinkedHashSet<>(Arrays.asList(super.getIds()));
        for (Class<?> c = getMethodOrConstructor().getDeclaringClass().getSuperclass();
                c != null; c = c.getSuperclass()) {
            final Object scripatble = getParentScope().get(c.getSimpleName(), this);
            if (scripatble instanceof Scriptable) {
                objects.addAll(Arrays.asList(((Scriptable) scripatble).getIds()));
            }
        }
        return objects.toArray(new Object[0]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFunctionName() {
        final String functionName = super.getFunctionName();
        switch (functionName) {
            case "webkitRTCPeerConnection":
                return "RTCPeerConnection";

            case "webkitSpeechRecognition":
                return "SpeechRecognition";

            case "WebKitMutationObserver":
                return "MutationObserver";

            case "webkitMediaStream":
                return "MediaStream";

            case "webkitSpeechGrammar":
                return "SpeechGrammar";

            case "webkitSpeechGrammarList":
                return "SpeechGrammarList";

            case "webkitSpeechRecognitionError":
                return "SpeechRecognitionErrorEvent";

            case "webkitSpeechRecognitionEvent":
                return "SpeechRecognitionEvent";

            case "webkitURL":
                return "URL";

            default:
        }
        return functionName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        Object value = super.get(name, start);
        if (value != NOT_FOUND) {
            return value;
        }

        final String superFunctionName = super.getFunctionName();
        if (!"Image".equals(superFunctionName) && !"Option".equals(superFunctionName)
                && (!"WebGLContextEvent".equals(superFunctionName)
                        || browserVersion_.hasFeature(JS_WEBGL_CONTEXT_EVENT_CONSTANTS))) {
            Class<?> klass = getPrototypeProperty().getClass();

            while (value == NOT_FOUND && HtmlUnitScriptable.class.isAssignableFrom(klass)) {
                final ClassConfiguration config = AbstractJavaScriptConfiguration.getClassConfiguration(
                        klass.asSubclass(HtmlUnitScriptable.class), browserVersion_);
                if (config != null) {
                    final List<ConstantInfo> constants = config.getConstants();
                    if (constants != null) {
                        for (final ConstantInfo constantInfo : constants) {
                            if (constantInfo.getName().equals(name)) {
                                value = ScriptableObject.getProperty((Scriptable) getPrototypeProperty(), name);
                                break;
                            }
                        }
                    }
                }
                klass = klass.getSuperclass();
            }
        }
        return value;
    }

    /**
     * Make this public.
     */
    @Override
    public Scriptable getClassPrototype() {
        return super.getClassPrototype();
    }
}
