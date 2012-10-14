/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_EVENT_NO_PARAMETER;
import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * Allows to wrap event handler code as Function object.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class EventHandler extends BaseFunction {
    private final DomNode node_;
    private final String eventName_;
    private final String jsSnippet_;
    private Function realFunction_;

    /**
     * Builds a function that will execute the JavaScript code provided.
     * @param node the element for which the event is build
     * @param eventName the event for which this handler is created
     * @param jsSnippet the JavaScript code
     */
    public EventHandler(final DomNode node, final String eventName, final String jsSnippet) {
        node_ = node;
        eventName_ = eventName;

        final String functionSignature;
        if (node.getPage().getEnclosingWindow().getWebClient()
                .getBrowserVersion().hasFeature(JS_EVENT_NO_PARAMETER)) {
            functionSignature = "function " + eventName + "()";
        }
        else {
            functionSignature = "function " + eventName + "(event)";
        }
        jsSnippet_ = functionSignature + " {" + jsSnippet + "\n}";

        final Window w = (Window) node.getPage().getEnclosingWindow().getScriptObject();
        final Scriptable function = (Scriptable) w.get("Function", w);
        setPrototype(function.getPrototype());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object call(final Context cx, final Scriptable scope,
        final Scriptable thisObj, final Object[] args)
        throws JavaScriptException {

        // the js object to which this event is attached has to be the scope
        final SimpleScriptable jsObj = (SimpleScriptable) node_.getScriptObject();
        // compile "just in time"
        if (realFunction_ == null) {
            realFunction_ = cx.compileFunction(jsObj, jsSnippet_, eventName_ + " event for " + node_
                + " in " + node_.getPage().getWebResponse().getWebRequest().getUrl(), 0, null);
        }

        final Object result = realFunction_.call(cx, scope, thisObj, args);
        return result;
    }

    /**
     * @see net.sourceforge.htmlunit.corejs.javascript.ScriptableObject#getDefaultValue(java.lang.Class)
     * @param typeHint the type hint
     * @return the js code of the function declaration
     */
    @Override
    public Object getDefaultValue(final Class<?> typeHint) {
        return jsSnippet_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        // quick and dirty
        if ("toString".equals(name)) {
            return new BaseFunction() {
                @Override
                public Object call(final Context cx, final Scriptable scope,
                        final Scriptable thisObj, final Object[] args) {
                    return jsSnippet_;
                }
            };
        }

        return super.get(name, start);
    }

}
