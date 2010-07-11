/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.ActiveXObjectImpl;
import com.gargoylesoftware.htmlunit.javascript.host.FormChild;

/**
 * The JavaScript object "HTMLObjectElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class HTMLObjectElement extends FormChild {

    private static final long serialVersionUID = -916091257587937486L;

    private SimpleScriptable wrappedActiveX_;

    /**
     * Creates an instance.
     */
    public HTMLObjectElement() {
        // Empty.
    }

    /**
     * Returns the value of the "alt" property.
     * @return the value of the "alt" property
     */
    public String jsxGet_alt() {
        String alt = getDomNodeOrDie().getAttribute("alt");
        if (alt == NOT_FOUND) {
            alt = "";
        }
        return alt;
    }

    /**
     * Returns the value of the "alt" property.
     * @param alt the value
     */
    public void jsxSet_alt(final String alt) {
        getDomNodeOrDie().setAttribute("alt", alt);
    }

    /**
     * Gets the "border" attribute.
     * @return the "border" attribute
     */
    public String jsxGet_border() {
        String border = getDomNodeOrDie().getAttribute("border");
        if (border == NOT_FOUND) {
            border = "";
        }
        return border;
    }

    /**
     * Sets the "border" attribute.
     * @param border the "border" attribute
     */
    public void jsxSet_border(final String border) {
        getDomNodeOrDie().setAttribute("border", border);
    }

    /**
     * Gets the "classid" attribute.
     * @return the "classid" attribute
     */
    public String jsxGet_classid() {
        String classid = getDomNodeOrDie().getAttribute("classid");
        if (classid == NOT_FOUND) {
            classid = "";
        }
        return classid;
    }

    /**
     * Sets the "classid" attribute.
     * @param classid the "classid" attribute
     */
    public void jsxSet_classid(final String classid) {
        getDomNodeOrDie().setAttribute("classid", classid);
        if (classid.indexOf(':') != -1 && getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_86)
                && getWindow().getWebWindow().getWebClient().isActiveXNative()
                && System.getProperty("os.name").contains("Windows")) {
            try {
                wrappedActiveX_ = new ActiveXObjectImpl(classid);
                wrappedActiveX_.setParentScope(getParentScope());
            }
            catch (final Exception e) {
                Context.throwAsScriptRuntimeEx(e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        if (wrappedActiveX_ != null) {
            return wrappedActiveX_.get(name, start);
        }
        return super.get(name, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final String name, final Scriptable start, final Object value) {
        if (wrappedActiveX_ != null) {
            wrappedActiveX_.put(name, start, value);
        }
        else {
            super.put(name, start, value);
        }
    }
}
