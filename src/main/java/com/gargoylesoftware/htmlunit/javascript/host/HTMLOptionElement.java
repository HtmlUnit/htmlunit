/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import java.util.HashSet;
import java.util.Set;

import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * The JavaScript object that represents an option.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HTMLOptionElement extends HTMLElement {
    private static final long serialVersionUID = 947015932373556314L;
    private static final Set<String> namesIEAttributeAlwaysAvailable_ = new HashSet<String>();

    static {
        final String[] names = {"id", "value", "selected"};
        for (final String name : names) {
            namesIEAttributeAlwaysAvailable_.add(name);
        }
    }
    /**
     * Create an instance.
     */
    public HTMLOptionElement() {
    }

    /**
     * JavaScript constructor. This must be declared in every JavaScript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     * @param newText the text
     * @param newValue the value
     * @param defaultSelected Whether the option is initially selected
     * @param selected the current selection state of the option
     */
    public void jsConstructor(final String newText, final String newValue,
            final boolean defaultSelected, final boolean selected) {
        final HtmlPage page = (HtmlPage) getWindow().getWebWindow().getEnclosedPage();
        AttributesImpl attributes = null;
        if (defaultSelected) {
            attributes = new AttributesImpl();
            attributes.addAttribute(null, "selected", "selected", null, "selected");
        }

        final HtmlOption htmlOption = (HtmlOption) HTMLParser.getFactory(HtmlOption.TAG_NAME).createElement(
                page, HtmlOption.TAG_NAME, attributes);
        htmlOption.setSelected(selected);
        setDomNode(htmlOption);

        if (newText != null && !newText.equals("undefined")) {
            htmlOption.appendChild(new DomText(page, newText));
        }
        if (newValue != null && !newValue.equals("undefined")) {
            htmlOption.setValueAttribute(newValue);
        }
    }

    /**
     * Returns the value of the "value" property.
     * @return the value property
     */
    public String jsxGet_value() {
        return getHtmlElementOrNull().getValueAttribute();
    }

    /**
     * Sets the value of the "value" property.
     * @param newValue the value property
     */
    public void jsxSet_value(final String newValue) {
        getHtmlElementOrNull().setValueAttribute(newValue);
    }

    /**
     * Returns the value of the "text" property.
     * @return the text property
     */
    @Override
    public String jsxGet_text() {
        final HtmlOption htmlOption = getHtmlElementOrNull();
        if (htmlOption.hasAttribute("label")) {
            return htmlOption.getLabelAttribute();
        }
        return htmlOption.asText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlOption getHtmlElementOrNull() {
        return (HtmlOption) super.getHtmlElementOrNull();
    }

    /**
     * Sets the value of the "text" property.
     * @param newText the text property
     */
    public void jsxSet_text(final String newText) {
        getHtmlElementOrNull().setLabelAttribute(newText);
    }

    /**
     * Returns the value of the "selected" property.
     * @return the text property
     */
    public boolean jsxGet_selected() {
        return getHtmlElementOrNull().isSelected();
    }

    /**
     * Sets the value of the "selected" property.
     * @param selected the new selected property
     */
    public void jsxSet_selected(final boolean selected) {
        getHtmlElementOrNull().setSelected(selected);
    }

    /**
     * Returns the value of the "defaultSelected" property.
     * @return the text property
     */
    public boolean jsxGet_defaultSelected() {
        return getHtmlElementOrNull().isDefaultSelected();
    }

    /**
     * Returns the value of the "label" property.
     * @return the label property
     */
    public String jsxGet_label() {
        return getHtmlElementOrNull().getLabelAttribute();
    }

    /**
     * Sets the value of the "label" property.
     * @param label the new label property
     */
    public void jsxSet_label(final String label) {
        getHtmlElementOrNull().setLabelAttribute(label);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object jsxFunction_getAttributeNode(final String attributeName) {
        final Object response = super.jsxFunction_getAttributeNode(attributeName);
        if (response == null && getBrowserVersion().isIE()
            && namesIEAttributeAlwaysAvailable_.contains(attributeName)) {
            final Attr att = new Attr();
            att.setPrototype(getPrototype(Attr.class));
            att.setParentScope(getWindow());
            att.init(attributeName, getHtmlElementOrDie());
            return att;
        }

        return response;
    }
}
