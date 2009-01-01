/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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

import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.html.HtmlArea;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * The JavaScript object "HTMLAreaElement".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class HTMLAreaElement extends HTMLElement {

    private static final long serialVersionUID = -6024985411914294862L;

    /**
     * Create an instance.
     */
    public HTMLAreaElement() {
        // Empty.
    }

    /**
     * Calls for instance for implicit conversion to string.
     * @see com.gargoylesoftware.htmlunit.javascript.SimpleScriptable#getDefaultValue(java.lang.Class)
     * @param hint the type hint
     * @return the default value
     */
    @Override
    public Object getDefaultValue(final Class< ? > hint) {
        final HtmlArea link = (HtmlArea) getHtmlElementOrDie();
        final String href = link.getHrefAttribute();

        final String response;
        if (href == HtmlElement.ATTRIBUTE_NOT_DEFINED) {
            response = "";
        }
        else {
            final int indexAnchor = href.indexOf('#');
            final String beforeAnchor;
            final String anchorPart;
            if (indexAnchor == -1) {
                beforeAnchor = href;
                anchorPart = "";
            }
            else {
                beforeAnchor = href.substring(0, indexAnchor);
                anchorPart = href.substring(indexAnchor);
            }

            try {
                response =
                    ((HtmlPage) link.getPage()).getFullyQualifiedUrl(beforeAnchor).toExternalForm() + anchorPart;
            }
            catch (final MalformedURLException e) {
                return href;
            }
        }

        return response;
    }
}
