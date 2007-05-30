/*
 * Copyright (c) 2002-2006 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.commons.collections.CollectionUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A javascript object for a Style
 * 
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Daniel Gredler
 * @author Chris Erskine
 */
public class Style extends SimpleScriptable {
    private static final long serialVersionUID = -1976370264911039311L;

    private static final MessageFormat URL_FORMAT = new MessageFormat(
            "url({0})");

    private HTMLElement jsElement_;

    /**
     * These are IE properties, this should be configured per browser
     */
    private static final String[] STYLE_PROPERTIES = { "backgroundColor",
        "bottom", "clear", "clip", "color", "direction",
        "display", "font", "fontFamily", "fontSize", "fontStyle",
        "fontWeight", "fontWeight", "hasLayout", "height", "layoutFlow",
        "layoutGrid", "layoutGridMode", "left", "letterSpacing",
        "lineHeight", "maxHeight", "maxWidth", "minHeight", "minWidth",
        "padding", "paddingBottom", "paddingLeft", "paddingRight",
        "paddingTop", "pixelBottom", "pixelHeight", "pixelLeft",
        "pixelRight", "pixelTop", "pixelWidth", "posBottom", "posHeight",
        "position", "posLeft", "posRight", "posTop", "posWidth", "right",
        "styleFloat", "textAutospace", "textDecoration",
        "textDecorationBlink", "textDecorationLineThrough",
        "textDecorationNone", "textDecorationOverline",
        "textDecorationUnderline", "textTransform",
        "textUnderlinePosition", "top", "unicodeBidi", "visibility",
        "width", "wordSpacing", "wordWrap", "zoom" };

    private static final Set STYLE_ALLOWED_PROPERTIES;
    
    static {
        final Set set = new HashSet();
        CollectionUtils.addAll(set, STYLE_PROPERTIES);
        STYLE_ALLOWED_PROPERTIES = Collections.unmodifiableSet(set);
    }

    /**
     * Create an instance. Javascript objects must have a default constructor.
     */
    public Style() {
    }

    /**
     * Create an instance and set its parent scope to the one of the provided
     * element
     * @param htmlElement the element to which this style is bound
     */
    Style(final HTMLElement htmlElement) {
        setParentScope(htmlElement.getParentScope());
        setPrototype(getPrototype(Style.class));
        initialize(htmlElement);
    }

    /**
     * Initialize the object
     * 
     * @param htmlElement
     *            The element that this style describes
     */
    void initialize(final HTMLElement htmlElement) {
        // Initialize.
        Assert.notNull("htmlElement", htmlElement);
        jsElement_ = htmlElement;
        setDomNode(htmlElement.getDomNodeOrNull(), false);

        if (htmlElement.getHtmlElementOrDie().getPage().getWebClient()
                .getBrowserVersion().isIE()) {
            // If a behavior was specified in the style, apply the behavior.
            for (final Iterator i = getStyleMap().entrySet().iterator(); i
                    .hasNext();) {
                final Map.Entry entry = (Map.Entry) i.next();
                final String key = (String) entry.getKey();
                if ("behavior".equals(key)) {
                    final String value = (String) entry.getValue();
                    try {
                        final Object[] url = URL_FORMAT.parse(value);
                        if (url.length > 0) {
                            jsElement_.jsxFunction_addBehavior((String) url[0]);
                            break;
                        }
                    }
                    catch (final ParseException e) {
                        getLog().warn("Invalid behavior: '" + value + "'.");
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    protected Object getWithPreemption(final String name) {
        if (STYLE_ALLOWED_PROPERTIES.contains(name)) {
            return getStyleAttribute(name);
        }
        return super.getWithPreemption(name);
    }
    
    /**
     * Gets the style attribute value
     * @param name the style attribute name
     * @return empty string if noting found
     */
    protected String getStyleAttribute(final String name) {
        final String value = (String) getStyleMap().get(name);
        if (value == null) {
            return "";
        }
        else {
            return value;
        }
    }

    /**
     * Set the specified property
     * 
     * @param name The name of the property
     * @param start The scriptable object that was originally invoked for this property
     * @param newValue The new value
     */
    public void put(final String name, final Scriptable start,
            final Object newValue) {
        // Some calls to put will happen during the initialization of the
        // superclass.
        // At this point, we don't have enough information to do our own
        // initialization
        // so we have to just pass this call through to the superclass.
        if (jsElement_ == null || !STYLE_ALLOWED_PROPERTIES.contains(name)) {
            super.put(name, start, newValue);
            return;
        }
        
        final String styleValue = (String) Context.jsToJava(newValue, String.class);

        setStyleAttribute(name, styleValue);
    }

    /**
     * Sets the specified style attribute
     * @param name the attribute name
     * @param newValue the attribute value
     */
    protected void setStyleAttribute(final String name, final String newValue) {
        final Map styleMap = getStyleMap();
        styleMap.put(name, newValue);

        final StringBuffer buffer = new StringBuffer();

        final Iterator iterator = styleMap.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry entry = (Map.Entry) iterator.next();
            buffer.append(" ");
            buffer.append(entry.getKey());
            buffer.append(": ");
            buffer.append(entry.getValue());
            buffer.append(";");
        }
        buffer.deleteCharAt(0);
        jsElement_.getHtmlElementOrDie().setAttributeValue("style", buffer.toString());
    }

    private Map getStyleMap() {
        // This must be a SortedMap so that the tests get results back in a
        // defined order.
        final SortedMap styleMap = new TreeMap();

        final String styleAttribute = jsElement_.getHtmlElementOrDie()
                .getAttributeValue("style");
        final StringTokenizer tokenizer = new StringTokenizer(styleAttribute, ";");
        while (tokenizer.hasMoreTokens()) {
            final String token = tokenizer.nextToken();
            final int index = token.indexOf(":");
            if (index != -1) {
                final String key = token.substring(0, index).trim();
                final String value = token.substring(index + 1).trim();

                styleMap.put(key, value);
            }
        }

        return styleMap;
    }

    /**
     * Gets the object's behavior
     * @return the behavior.
     */
    public String jsxGet_behavior() {
        return getStyleAttribute("behavior");
    }

    /**
     * Sets the object's behavior
     * @param newValue the new behavior
     */
    public void jsxSet_behavior(final String newValue) {
        setStyleAttribute("behavior", newValue);
    }
}
