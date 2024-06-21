/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.html;

import java.util.Map;

import org.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "title".
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 */
public class HtmlTitle extends HtmlElement {

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "title";

    /**
     * Creates an instance of HtmlTitle.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    HtmlTitle(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * Allows the text value for the title element be replaced.
     * {@inheritDoc}
     */
    @Override
    public void setNodeValue(final String message) {
        final DomNode child = getFirstChild();
        if (child == null) {
            final DomNode textNode = new DomText(getPage(), message);
            appendChild(textNode);
        }
        else if (child instanceof DomText) {
            ((DomText) child).setData(message);
        }
        else {
            throw new IllegalStateException("For title tag, this should be a text node");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isEmptyXmlTagExpanded() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        return DisplayStyle.NONE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInnerHtml(final String source) {
        // title is different
        setText(source);
    }

    /**
     * Returns the {@code text} attribute.
     * @return the {@code text} attribute
     */
    public String getText() {
        final DomNode firstChild = getFirstChild();
        if (firstChild != null) {
            return firstChild.getNodeValue();
        }
        return "";
    }

    /**
     * Sets the {@code text} attribute.
     * @param text the {@code text} attribute
     */
    public void setText(final String text) {
        DomNode firstChild = getFirstChild();
        if (firstChild == null) {
            firstChild = new DomText(getPage(), text);
            appendChild(firstChild);
        }
        else {
            firstChild.setNodeValue(text);
        }
    }
}
