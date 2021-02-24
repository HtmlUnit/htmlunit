/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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

import java.lang.reflect.Executable;
import java.lang.reflect.Member;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.configuration.AbstractJavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration.ConstantInfo;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

import net.sourceforge.htmlunit.corejs.javascript.Context;
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

    /**
     * The constructor.
     * @param name the name of the function
     * @param methodOrConstructor a {@link Member} that defines the object
     * @param scope the enclosing scope of function
     */
    public RecursiveFunctionObject(final String name, final Executable methodOrConstructor,
            final Scriptable scope) {
        super(name, methodOrConstructor, scope);
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
        final Set<Object> objects = new LinkedHashSet<>();
        for (final Object o : super.getIds()) {
            objects.add(o);
        }
        for (Class<?> c = getMethodOrConstructor().getDeclaringClass().getSuperclass();
                c != null; c = c.getSuperclass()) {
            final Object scripatble = getParentScope().get(c.getSimpleName(), this);
            if (scripatble instanceof Scriptable) {
                for (final Object id : ((Scriptable) scripatble).getIds()) {
                    objects.add(id);
                }
            }
        }
        return objects.toArray(new Object[objects.size()]);
    }

    /**
     * Gets the browser version currently used.
     * @return the browser version
     */
    public BrowserVersion getBrowserVersion() {
        Scriptable parent = getParentScope();
        while (!(parent instanceof Window)) {
            parent = parent.getParentScope();
        }
        return ((Window) parent).getBrowserVersion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFunctionName() {
        final String functionName = super.getFunctionName();
        switch (functionName) {
            case "V8BreakIterator":
                return "v8BreakIterator";

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
        final String superFunctionName = super.getFunctionName();
        if ("prototype".equals(name)) {
            switch (superFunctionName) {
                case "Proxy":
                    return NOT_FOUND;

                default:
            }
        }
        Object value = super.get(name, start);

        if (value == NOT_FOUND && !"Image".equals(superFunctionName) && !"Option".equals(superFunctionName)
                && (!"WebGLContextEvent".equals(superFunctionName)
                        || getBrowserVersion().hasFeature(JS_WEBGL_CONTEXT_EVENT_CONSTANTS))) {
            Class<?> klass = getPrototypeProperty().getClass();

            final BrowserVersion browserVersion = getBrowserVersion();
            while (value == NOT_FOUND && HtmlUnitScriptable.class.isAssignableFrom(klass)) {
                final ClassConfiguration config = AbstractJavaScriptConfiguration.getClassConfiguration(
                        klass.asSubclass(HtmlUnitScriptable.class), browserVersion);
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
     * {@inheritDoc}
     */
    @Override
    public Object call(final Context cx, final Scriptable scope, final Scriptable thisObj, final Object[] args) {
        final Object object = super.call(cx, scope, thisObj, args);
        if (object instanceof Scriptable) {
            final Scriptable result = (Scriptable) object;
            if (result.getPrototype() == null) {
                final Scriptable proto = getClassPrototype();
                if (result != proto) {
                    result.setPrototype(proto);
                }
            }
            if (result.getParentScope() == null) {
                final Scriptable parent = getParentScope();
                if (result != parent) {
                    result.setParentScope(parent);
                }
            }
        }
        return object;
    }
}
