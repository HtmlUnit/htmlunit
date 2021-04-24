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
package netscape.javascript;

import java.applet.Applet;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

import net.sourceforge.htmlunit.corejs.javascript.ConsString;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * Stub for the JSException. This is part of the Applet
 * LiveConnect simulation.
 *
 * @author Ronald Brill
 */
public class JSObject {

    private static final Log LOG = LogFactory.getLog(JSObject.class);

    private static Window Window_;
    private ScriptableObject scriptableObject_;

    /**
     * Constructor.
     *
     * @param scriptableObject the wrapped scriptableObject
     */
    public JSObject(final ScriptableObject scriptableObject) {
        scriptableObject_ = scriptableObject;
    }

    /**
     * Calls a JavaScript method.
     * Equivalent to "this.methodName(args[0], args[1], ...)" in JavaScript.
     *
     * @param methodName the name of the JavaScript method to be invoked
     * @param args an array of Java object to be passed as arguments to the method
     * @return result result of the method
     * @throws JSException in case or error
     */
    public Object call(final String methodName, final Object[] args) throws JSException {
        if (LOG.isInfoEnabled()) {
            LOG.info("JSObject call '" + methodName + "(" + Arrays.toString(args) + ")'");
        }

        final Object jsResult = ScriptableObject.callMethod(scriptableObject_, methodName, args);
        if (jsResult instanceof ScriptableObject) {
            return new JSObject((ScriptableObject) jsResult);
        }
        if (jsResult instanceof ConsString) {
            return ((ConsString) jsResult).toString();
        }
        return jsResult;
    }

    /**
     * Evaluates a JavaScript expression.
     * The expression is a string of JavaScript source code which will be evaluated in the context given by "this".
     *
     * @param expression the JavaScript expression
     * @return result Object
     * @throws JSException in case or error
     */
    public Object eval(final String expression) throws JSException {
        if (LOG.isInfoEnabled()) {
            LOG.info("JSObject eval '" + expression + "'");
        }

        final Page page = Window_.getWebWindow().getEnclosedPage();
        if (page instanceof HtmlPage) {
            final HtmlPage htmlPage = (HtmlPage) page;
            final ScriptResult result = htmlPage.executeJavaScript(expression);
            final Object jsResult = result.getJavaScriptResult();
            if (jsResult instanceof ScriptableObject) {
                return new JSObject((ScriptableObject) jsResult);
            }
            if (jsResult instanceof ConsString) {
                return ((ConsString) jsResult).toString();
            }
            return jsResult;
        }
        return null;
    }

    /**
     * Retrieves a named member of a JavaScript object.
     * Equivalent to "this.name" in JavaScript.
     *
     * @param name the name of the JavaScript property to be accessed
     * @return result Object
     * @throws JSException in case or error
     */
    public Object getMember(final String name) throws JSException {
        if (LOG.isInfoEnabled()) {
            LOG.info("JSObject getMember '" + name + "'");
        }

        if (scriptableObject_ instanceof Element) {
            return  ((Element) scriptableObject_).getAttribute(name, null);
        }
        return scriptableObject_.get(name, scriptableObject_);
    }

    /**
     * Sets a named member of a JavaScript object.
     * Equivalent to "this.name = value" in JavaScript.
     *
     * @param name the name of the JavaScript property to be accessed
     * @param value the value of the property
     * @throws JSException in case or error
     */
    public void setMember(final String name, final Object value) throws JSException {
        String stringValue = "";
        if (value != null) {
            stringValue = Context.toString(value);
        }

        if (LOG.isInfoEnabled()) {
            LOG.info("JSObject setMember '" + name + "' to '" + stringValue + "'");
        }

        if (scriptableObject_ instanceof Element) {
            ((Element) scriptableObject_).setAttribute(name, stringValue);
            return;
        }
        scriptableObject_.put(name, scriptableObject_, value);
    }

    /**
     * Removes a named member of a JavaScript object.
     * Equivalent to "delete this.name" in JavaScript.
     *
     * @param name the name of the JavaScript property to be accessed
     * @throws JSException in case or error
     */
    public void removeMember(final String name) throws JSException {
        LOG.error("Not yet implemented (netscape.javascript.JSObject.removeMember(String)).");
        throw new RuntimeException("Not yet implemented (netscape.javascript.JSObject.removeMember(String)).");
    }

    /**
     * Retrieves an indexed member of a JavaScript object.
     * Equivalent to "this[index]" in JavaScript.
     *
     * @param index the index of the array to be accessed
     * @return result the value of the indexed member
     * @throws JSException in case or error
     */
    public Object getSlot(final int index) throws JSException {
        LOG.error("Not yet implemented (netscape.javascript.JSObject.getSlot(int)).");
        throw new RuntimeException("Not yet implemented (netscape.javascript.JSObject.getSlot(int)).");
    }

    /**
     * Sets an indexed member of a JavaScript object.
     * Equivalent to "this[index] = value" in JavaScript.
     *
     * @param index the index of the array to be accessed
     * @param value the value of the property
     * @throws JSException in case or error
     */
    public void setSlot(final int index, final Object value) throws JSException {
        LOG.error("Not yet implemented (netscape.javascript.JSObject.setSlot(int, Object)).");
        throw new RuntimeException("Not yet implemented (netscape.javascript.JSObject.setSlot(int, Object)).");
    }

    /**
     * Returns a JSObject for the window containing the given applet.
     *
     * @param paramApplet the paramApplet
     * @return result Object
     * @throws JSException in case or error
     */
    public static JSObject getWindow(final Applet paramApplet) throws JSException {
        return new JSObject(Window_);
    }

    /**
     * Sets the window this class works for.
     * Every applet works inside his onw class loaders, and this class
     * will be loaded separatly into every one.
     *
     * @param window the window
     */
    public static void setWindow(final Window window) {
        Window_ = window;
    }
}
