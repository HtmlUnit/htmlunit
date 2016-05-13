/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.FORMFIELD_REACHABLE_BY_NEW_NAMES;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.FORMFIELD_REACHABLE_BY_ORIGINAL_NAME;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.FormFieldWithNameHistory;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.javascript.host.Element2;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.FindProperty;
import com.gargoylesoftware.js.nashorn.internal.runtime.Property;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;

public class HTMLFormElement2 extends Element2 {

    public static HTMLFormElement2 constructor(final boolean newObj, final Object self) {
        final HTMLFormElement2 host = new HTMLFormElement2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(HTMLFormElement2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    private HtmlForm getHtmlForm() {
        return (HtmlForm) getDomNodeOrDie();
    }

    private void addElements(final String name, final Iterable<HtmlElement> nodes,
            final List<HtmlElement> addTo) {
        for (final HtmlElement node : nodes) {
            if (isAccessibleByIdOrName(node, name)) {
                addTo.add(node);
            }
        }
    }

    /**
     * Indicates if the element can be reached by id or name in expressions like "myForm.myField".
     * @param element the element to test
     * @param name the name used to address the element
     * @return {@code true} if this element matches the conditions
     */
    private boolean isAccessibleByIdOrName(final HtmlElement element, final String name) {
        if (element instanceof FormFieldWithNameHistory && !(element instanceof HtmlImageInput)) {
            if (element.getEnclosingForm() != getHtmlForm()) {
                return false; // nested forms
            }
            if (name.equals(element.getId())) {
                return true;
            }
            final FormFieldWithNameHistory elementWithNames = (FormFieldWithNameHistory) element;
            if (getBrowserVersion().hasFeature(FORMFIELD_REACHABLE_BY_ORIGINAL_NAME)) {
                if (name.equals(elementWithNames.getOriginalName())) {
                    return true;
                }
            }
            else if (name.equals(element.getAttribute("name"))) {
                return true;
            }

            if (getBrowserVersion().hasFeature(FORMFIELD_REACHABLE_BY_NEW_NAMES)) {
                if (elementWithNames.getNewNames().contains(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<HtmlElement> findElements(final String name) {
        final List<HtmlElement> elements = new ArrayList<>();
        addElements(name, getHtmlForm().getHtmlElementDescendants(), elements);
        addElements(name, getHtmlForm().getLostChildren(), elements);

        // If no form fields are found, IE and Firefox are able to find img elements by ID or name.
        if (elements.isEmpty()) {
            for (final DomNode node : getHtmlForm().getChildren()) {
                if (node instanceof HtmlImage) {
                    final HtmlImage img = (HtmlImage) node;
                    if (name.equals(img.getId()) || name.equals(img.getNameAttribute())) {
                        elements.add(img);
                    }
                }
            }
        }

        return elements;
    }

    @Override
    protected FindProperty findProperty(final String key, final boolean deep, final ScriptObject start) {
        final List<HtmlElement> elements = findElements(key);

        if (elements.isEmpty()) {
            return null;
        }
        
        if (elements.size() == 1) {
            addOwnProperty(key, Property.WRITABLE_ENUMERABLE_CONFIGURABLE, getScriptableFor(elements.get(0)));
        }

//        final HTMLCollection collection = new HTMLCollection(getHtmlForm(), elements) {
//            @Override
//            protected List<Object> computeElements() {
//                return new ArrayList<Object>(findElements(name));
//            }
//        };
//        return collection;
        return super.findProperty(key, deep, start);
    }

    public static final class FunctionConstructor extends ScriptFunction {
        public FunctionConstructor() {
            super("HTMLFormElement", 
                    staticHandle("constructor", HTMLFormElement2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    public static final class Prototype extends PrototypeObject {
        Prototype() {
            ScriptUtils.initialize(this);
        }

        public String getClassName() {
            return "HTMLFormElement";
        }
    }
}
