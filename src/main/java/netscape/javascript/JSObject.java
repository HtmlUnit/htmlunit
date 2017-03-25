/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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
package netscape.javascript;

import java.applet.Applet;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * Stub for the JSException. This is part of the Applet
 * LiveConnect simulation.
 *
 * TODO: we have to evaluate if it is possible to use plugin.jar from jdk
 *
 * @author Ronald Brill
 */
public class JSObject {

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
     * Empty stub.
     *
     * @param paramString the paramString
     * @param paramArrayOfObject the paramArrayOfObject
     * @return result Object
     * @throws JSException in case or error
     */
    public Object call(final String paramString, final Object[] paramArrayOfObject) throws JSException {
        throw new RuntimeException("Not yet implemented (netscape.javascript.JSObject.call(String, Object[])).");
    }

    /**
     * Empty stub.
     *
     * @param paramString the paramString
     * @return result Object
     * @throws JSException in case or error
     */
    public Object eval(final String paramString) throws JSException {
        final Page page = Window_.getWebWindow().getEnclosedPage();
        if (page instanceof HtmlPage) {
            final HtmlPage htmlPage = (HtmlPage) page;
            final ScriptResult result = htmlPage.executeJavaScript(paramString);
            if (result.getJavaScriptResult() instanceof ScriptableObject) {
                return new JSObject((ScriptableObject) result.getJavaScriptResult());
            }
            return result.getJavaScriptResult();
        }
        return null;
    }

    /**
     * Empty stub.
     *
     * @param paramString the paramString
     * @return result Object
     * @throws JSException in case or error
     */
    public Object getMember(final String paramString) throws JSException {
        throw new RuntimeException("Not yet implemented (netscape.javascript.JSObject.getMember(String)).");
    }

    /**
     * Empty stub.
     *
     * @param paramString the paramString
     * @param paramObject the paramObject
     * @throws JSException in case or error
     */
    public void setMember(final String paramString, final Object paramObject) throws JSException {
        if (scriptableObject_ instanceof Element) {
            ((Element) scriptableObject_).setAttribute(paramString, paramObject.toString());
            return;
        }
        scriptableObject_.put(paramString, scriptableObject_, paramObject);
    }

    /**
     * Empty stub.
     *
     * @param paramString the paramString
     * @throws JSException in case or error
     */
    public void removeMember(final String paramString) throws JSException {
        throw new RuntimeException("Not yet implemented (netscape.javascript.JSObject.removeMember(String)).");
    }

    /**
     * Empty stub.
     *
     * @param paramInt the paramInt
     * @return result Object
     * @throws JSException in case or error
     */
    public Object getSlot(final int paramInt) throws JSException {
        throw new RuntimeException("Not yet implemented (netscape.javascript.JSObject.getSlot(int)).");
    }

    /**
     * Empty stub.
     *
     * @param paramInt the paramInt
     * @param paramObject the paramObject
     * @throws JSException in case or error
     */
    public void setSlot(final int paramInt, final Object paramObject) throws JSException {
        throw new RuntimeException("Not yet implemented (netscape.javascript.JSObject.setSlot(int, Object)).");
    }

    /**
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
