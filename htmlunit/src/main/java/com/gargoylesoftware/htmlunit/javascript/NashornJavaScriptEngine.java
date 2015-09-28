/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript;

import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily.CHROME;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.SimpleScriptContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.InteractivePage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.host.Window2;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget2;
import com.gargoylesoftware.js.nashorn.api.scripting.NashornScriptEngineFactory;
import com.gargoylesoftware.js.nashorn.api.scripting.ScriptObjectMirror;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Browser;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.BrowserFamily;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;

/**
 * A wrapper for the <a href="http://openjdk.java.net/projects/nashorn/">Nashorn JavaScript engine</a>.
 *
 * @author Ahmed Ashour
 */
public class NashornJavaScriptEngine implements AbstractJavaScriptEngine {

    private static final Log LOG = LogFactory.getLog(NashornJavaScriptEngine.class);

    private final WebClient webClient_;
    final ScriptEngine engine = new NashornScriptEngineFactory().getScriptEngine();

    /**
     * Creates an instance for the specified {@link WebClient}.
     *
     * @param webClient the client that will own this engine
     */
    public NashornJavaScriptEngine(final WebClient webClient) {
        webClient_ = webClient;
        initGlobal(engine, new Browser(BrowserFamily.CHROME, 45));
    }

    private void initGlobal(final ScriptEngine engine, final Browser browser) {
        Browser.setCurrent(browser);
        final SimpleScriptContext context = (SimpleScriptContext) engine.getContext();
        final Global global = (Global) ((ScriptObjectMirror) context.getBindings(ScriptContext.ENGINE_SCOPE)).getScriptObject();
        final Global oldGlobal = Context.getGlobal();
        try {
            Context.setGlobal(global);

            final BrowserFamily browserFamily = browser.getFamily();
            if (browserFamily == CHROME) {
                global.put("EventTarget", new EventTarget2.FunctionConstructor(), true);
                global.put("Window", new Window2.FunctionConstructor(), true);
                setProto(global, "Window", "EventTarget");
            }
            else {
                global.put("Window", new Window2(), true);
                setProto(global, "Window", new EventTarget2.ObjectConstructor());
            }

            final Window2 window = new Window2();
            ScriptObject windowProto = Context.getGlobal().getPrototype(window.getClass());
            if (windowProto == null) {
                windowProto = (ScriptObject) global.get("Window");
            }
            window.setProto(windowProto);

            global.put("window", window, true);
        }
        finally {
            Context.setGlobal(oldGlobal);
        }
    }

    private void setProto(final Global global, final String childName, final String parentName) {
        final Object child = global.get(childName);
        if (child instanceof ScriptFunction) {
            final ScriptFunction childFunction = (ScriptFunction) global.get(childName);
            final PrototypeObject childPrototype = (PrototypeObject) childFunction.getPrototype();
            final ScriptFunction parentFunction = (ScriptFunction) global.get(parentName);
            final PrototypeObject parentPrototype = (PrototypeObject) parentFunction.getPrototype();
            childPrototype.setProto(parentPrototype);
            childFunction.setProto(parentFunction);
        }
        else {
            final ScriptObject childObject = (ScriptObject) global.get(childName);
            final ScriptObject parentObject = (ScriptObject) global.get(parentName);
            childObject.setProto(parentObject);
        }
    }

    private void setProto(final Global global, final String childName, final ScriptObject parentObject) {
        final ScriptObject childObject = (ScriptObject) global.get(childName);
        childObject.setProto(parentObject);
    }

    @Override
    public JavaScriptConfiguration getJavaScriptConfiguration() {
        return null;
    }

    @Override
    public void addPostponedAction(PostponedAction action) {
    }

    @Override
    public void processPostponedActions() {
    }

    @Override
    public Object execute(InteractivePage page, String sourceCode, String sourceName, int startLine) {
        return null;
    }

    @Override
    public void registerWindowAndMaybeStartEventLoop(WebWindow webWindow) {
    }

    @Override
    public void initialize(final WebWindow webWindow) {
        final SimpleScriptContext context = (SimpleScriptContext) engine.getContext();
        final Global global = (Global) ((ScriptObjectMirror) context.getBindings(ScriptContext.ENGINE_SCOPE)).getScriptObject();
        Window2 window2 = (Window2) global.get("window");
        window2.initialize(webWindow);
    }

    @Override
    public void setJavaScriptTimeout(long timeout) {
    }

    @Override
    public long getJavaScriptTimeout() {
        return 0;
    }

    @Override
    public void shutdown() {
    }

    @Override
    public boolean isScriptRunning() {
        return false;
    }

}
