/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.activex.javascript.msxml.MSXMLActiveXObjectFactory;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * This is the host object that allows JavaScript to instantiate Java objects via the ActiveXObject
 * constructor. This host object enables a person to emulate ActiveXObjects in JavaScript with Java
 * objects. See the <code>WebClient</code> class to see how ActiveXObject string parameter specifies
 * which Java class is instantiated.
 *
 * @see com.gargoylesoftware.htmlunit.WebClient
 * @version $Revision$
 * @author <a href="mailto:bcurren@esomnie.com">Ben Curren</a>
 * @author Ahmed Ashour
 * @author Chuck Dumont
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass(browsers = @WebBrowser(IE))
public class ActiveXObject extends SimpleScriptable {

    private static final Log LOG = LogFactory.getLog(ActiveXObject.class);

    /**
     * The default constructor.
     */
    public ActiveXObject() {
    }

    /**
     * This method
     * <ol>
     *   <li>instantiates the MSXML (ActiveX) object if requested (<code>XMLDOMDocument</code>,
     *      <code>XMLHTTPRequest</code>, <code>XSLTemplate</code>)
     *   <li>searches the map specified in the <code>WebClient</code> class for the Java object to instantiate based
     *      on the ActiveXObject constructor String
     *   <li>uses <code>ActiveXObjectImpl</code> to initiate Jacob.
     * </ol>
     *
     * @param cx the current context
     * @param args the arguments to the ActiveXObject constructor
     * @param ctorObj the function object
     * @param inNewExpr Is new or not
     * @return the java object to allow JavaScript to access
     */
    @JsxConstructor
    public static Scriptable jsConstructor(
            final Context cx, final Object[] args, final Function ctorObj,
            final boolean inNewExpr) {
        if (args.length < 1 || args.length > 2) {
            throw Context.reportRuntimeError(
                    "ActiveXObject Error: constructor must have one or two String parameters.");
        }
        if (args[0] == Context.getUndefinedValue()) {
            throw Context.reportRuntimeError("ActiveXObject Error: constructor parameter is undefined.");
        }
        if (!(args[0] instanceof String)) {
            throw Context.reportRuntimeError("ActiveXObject Error: constructor parameter must be a String.");
        }
        final String activeXName = (String) args[0];

        final WebWindow window = getWindow(ctorObj).getWebWindow();
        final MSXMLActiveXObjectFactory factory = window.getWebClient().getMSXMLActiveXObjectFactory();
        if (factory.supports(activeXName)) {
            final Scriptable scriptable = factory.create(activeXName, window);
            if (scriptable != null) {
                return scriptable;
            }
        }

        final WebClient webClient = getWindow(ctorObj).getWebWindow().getWebClient();
        final Map<String, String> map = webClient.getActiveXObjectMap();
        if (map != null) {
            final Object mapValue = map.get(activeXName);
            if (mapValue != null) {
                final String xClassString = (String) mapValue;
                Object object = null;
                try {
                    final Class<?> xClass = Class.forName(xClassString);
                    object = xClass.newInstance();
                }
                catch (final Exception e) {
                    throw Context.reportRuntimeError("ActiveXObject Error: failed instantiating class " + xClassString
                            + " because " + e.getMessage() + ".");
                }
                return Context.toObject(object, ctorObj);
            }
        }
        if (webClient.getOptions().isActiveXNative() && System.getProperty("os.name").contains("Windows")) {
            try {
                return new ActiveXObjectImpl(activeXName);
            }
            catch (final Exception e) {
                LOG.warn("Error initiating Jacob", e);
            }
        }

        LOG.warn("Automation server can't create object for '" + activeXName + "'.");
        throw Context.reportRuntimeError("Automation server can't create object for '" + activeXName + "'.");
    }

    /**
     * Adds a specific property to this object.
     * @param scriptable the scriptable
     * @param propertyName the property name
     * @param isGetter is getter
     * @param isSetter is setter
     */
    public static void addProperty(final SimpleScriptable scriptable, final String propertyName,
            final boolean isGetter, final boolean isSetter) {
        final String initialUpper = Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1);
        String getterName = null;
        if (isGetter) {
            getterName = "get" + initialUpper;
        }
        String setterName = null;
        if (isSetter) {
            setterName = "set" + initialUpper;
        }
        addProperty(scriptable, propertyName, getterName, setterName);
    }

    static void addProperty(final SimpleScriptable scriptable, final String propertyName,
            final String getterMethodName, final String setterMethodName) {
        scriptable.defineProperty(propertyName, null,
                getMethod(scriptable.getClass(), getterMethodName, JsxGetter.class),
                getMethod(scriptable.getClass(), setterMethodName, JsxSetter.class), PERMANENT);
    }

    /**
     * Gets the first method found of the class with the given name
     * and the correct annotation
     * @param clazz the class to search on
     * @param name the name of the searched method
     * @param annotationClass the class of the annotation required
     * @return <code>null</code> if not found
     */
    static Method getMethod(final Class<? extends SimpleScriptable> clazz,
            final String name, final Class<? extends Annotation> annotationClass) {
        if (name == null) {
            return null;
        }

        Method foundMethod = null;
        int foundByNameOnlyCount = 0;
        for (final Method method : clazz.getMethods()) {
            if (method.getName().equals(name)) {
                if (null != method.getAnnotation(annotationClass)) {
                    return method;
                }
                foundByNameOnlyCount++;
                foundMethod = method;
            }
        }
        if (foundByNameOnlyCount > 1) {
            throw new IllegalArgumentException("Found " + foundByNameOnlyCount + " methods for name '"
                    + name + "' in class '" + clazz + "'.");
        }
        return foundMethod;
    }

    /**
     * Gets the name of the host object class.
     * @return the JavaScript class name
     */
    @Override
    public String getClassName() {
        return "ActiveXObject";
    }
}
