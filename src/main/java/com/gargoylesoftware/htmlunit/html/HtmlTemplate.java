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
package com.gargoylesoftware.htmlunit.html;

import java.io.PrintWriter;
import java.util.Map;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "template".
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Ronny Shapiro
 */
public class HtmlTemplate extends HtmlElement {

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "template";

    private final DomDocumentFragment domDocumentFragment_;

    /**
     * Creates a new instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlTemplate(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);

        domDocumentFragment_ = new DomDocumentFragment(page);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        return DisplayStyle.NONE;
    }

    /**
     * @return the associated document fragment
     */
    public DomDocumentFragment getContent() {
        return domDocumentFragment_;
    }

    /**
     * Parsing of the children is done, we can move our children to the content.
     */
    @Override
    public void onAllChildrenAddedToPage(final boolean postponed) {
        while (getFirstChild() != null) {
            final DomNode child = getFirstChild();

            child.basicRemove();
            final HtmlPage htmlPage = getHtmlPageOrNull();
            if (htmlPage != null) {
                htmlPage.notifyNodeRemoved(child);
            }

            domDocumentFragment_.appendChild(child);
        }
    }

    @Override
    protected boolean isEmptyXmlTagExpanded() {
        // Always print expanded tag to force printing of
        // children if available
        return true;
    }

    @Override
    protected void printChildrenAsXml(final String indent, final PrintWriter printWriter) {
        domDocumentFragment_.printChildrenAsXml(indent, printWriter);
    }
}
