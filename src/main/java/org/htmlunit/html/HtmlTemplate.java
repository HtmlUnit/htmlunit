/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import java.io.PrintWriter;
import java.util.Map;

import org.htmlunit.SgmlPage;

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

    @Override
    protected boolean isEmptyXmlTagExpanded() {
        // Always print expanded tag to force printing of
        // children if available
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean printXml(final String indent, final boolean tagBefore, final PrintWriter printWriter) {
        final boolean hasChildren = domDocumentFragment_.getFirstChild() != null;

        if (tagBefore) {
            printWriter.print("\r\n");
            printWriter.print(indent);
        }

        printWriter.print('<');
        printOpeningTagContentAsXml(printWriter);

        if (hasChildren) {
            printWriter.print(">");
            final boolean tag = domDocumentFragment_.printChildrenAsXml(indent, tagBefore, printWriter);
            if (tag) {
                printWriter.print("\r\n");
                printWriter.print(indent);
            }
            printWriter.print("</");
            printWriter.print(getTagName());
            printWriter.print(">");
        }
        else if (isEmptyXmlTagExpanded()) {
            printWriter.print("></");
            printWriter.print(getTagName());
            printWriter.print(">");
        }
        else {
            printWriter.print("/>");
        }

        return true;
    }
}
