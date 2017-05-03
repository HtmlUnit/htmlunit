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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INPUT_SET_TYPE_LOWERCASE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INPUT_SET_VALUE_EMAIL_TRIMMED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INPUT_SET_VALUE_URL_TRIMMED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_SELECT_FILE_THROWS;
import static com.gargoylesoftware.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.FF;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.InputElementFactory;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ClassConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;

/**
 * The JavaScript object for {@code HTMLInputElement}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Frank Danek
 */
@ScriptClass
public class HTMLInputElement2 extends FormField2 {

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
    public static HTMLInputElement2 constructor(final boolean newObj, final Object self) {
        final HTMLInputElement2 host = new HTMLInputElement2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlInput getDomNodeOrDie() {
        return (HtmlInput) super.getDomNodeOrDie();
    }

    /**
     * Returns the {@code type} property.
     * @return the {@code type} property
     */
    @Getter
    public String getType() {
        return getDomNodeOrDie().getTypeAttribute().toLowerCase(Locale.ROOT);
    }

    /**
     * Sets the value of the attribute {@code type}.
     * Note: this replace the DOM node with a new one.
     * @param newType the new type to set
     */
    @Setter
    public void setType(String newType) {
        HtmlInput input = getDomNodeOrDie();

        final String currentType = input.getAttribute("type");

        if (!currentType.equalsIgnoreCase(newType)) {
            if (newType != null && getBrowserVersion().hasFeature(JS_INPUT_SET_TYPE_LOWERCASE)) {
                newType = newType.toLowerCase(Locale.ROOT);
            }
            final AttributesImpl attributes = readAttributes(input);
            final int index = attributes.getIndex("type");
            if (index > -1) {
                attributes.setValue(index, newType);
            }
            else {
                attributes.addAttribute(null, "type", "type", null, newType);
            }

            // create a new one only if we have a new type
            if (ATTRIBUTE_NOT_DEFINED != currentType || !"text".equalsIgnoreCase(newType)) {
                final HtmlInput newInput = (HtmlInput) InputElementFactory.instance
                    .createElement(input.getPage(), "input", attributes);

                if (input.wasCreatedByJavascript()) {
                    newInput.markAsCreatedByJavascript();
                }

                if (input.getParentNode() == null) {
                    // the input hasn't yet been inserted into the DOM tree (likely has been
                    // created via document.createElement()), so simply replace it with the
                    // new Input instance created in the code above
                    input = newInput;
                }
                else {
                    input.getParentNode().replaceChild(newInput, input);
                }

                input.setScriptableObject(null);
                setDomNode(newInput, true);
            }
            else {
                super.setAttribute("type", newType);
            }
        }
    }

    /**
     * Sets the value of the JavaScript attribute {@code value}.
     *
     * @param newValue the new value
     */
    @Override
    public void setValue(final Object newValue) {
        if (null == newValue) {
            getDomNodeOrDie().setAttribute("value", "");
            return;
        }

        String val = newValue.toString();
        final BrowserVersion browserVersion = getBrowserVersion();
        if (StringUtils.isNotEmpty(val) && "file".equalsIgnoreCase(getType())) {
            if (browserVersion.hasFeature(JS_SELECT_FILE_THROWS)) {
                throw new RuntimeException("InvalidStateError: "
                        + "Failed to set the 'value' property on 'HTMLInputElement'.");
            }
            return;
        }

        if (StringUtils.isBlank(val)) {
            if ("email".equalsIgnoreCase(getType()) && browserVersion.hasFeature(JS_INPUT_SET_VALUE_EMAIL_TRIMMED)) {
                val = "";
            }
            else if ("url".equalsIgnoreCase(getType()) && browserVersion.hasFeature(JS_INPUT_SET_VALUE_URL_TRIMMED)) {
                val = "";
            }
        }

        getDomNodeOrDie().setAttribute("value", val);
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(HTMLInputElement2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Function constructor.
     */
    @ClassConstructor({CHROME, FF})
    public static final class FunctionConstructor extends ScriptFunction {
        /**
         * Constructor.
         */
        public FunctionConstructor() {
            super("HTMLInputElement",
                    staticHandle("constructor", HTMLInputElement2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    /** Prototype. */
    public static final class Prototype extends PrototypeObject {
        Prototype() {
            ScriptUtils.initialize(this);
        }

        @Override
        public String getClassName() {
            return "HTMLInputElement";
        }
    }
}
