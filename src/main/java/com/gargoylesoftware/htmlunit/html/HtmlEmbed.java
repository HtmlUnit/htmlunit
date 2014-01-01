/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * Wrapper for the HTML element "embed".
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlEmbed extends HtmlElement {

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "embed";

    /**
     * Creates a new instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlEmbed(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * Saves this content as the specified file.
     * @param file the file to save to
     * @throws IOException if an IO error occurs
     */
    public void saveAs(final File file) throws IOException {
        final HtmlPage page = (HtmlPage) getPage();
        final WebClient webclient = page.getWebClient();

        final URL url = page.getFullyQualifiedUrl(getAttribute("src"));
        final WebRequest request = new WebRequest(url);
        request.setAdditionalHeader("Referer", page.getUrl().toExternalForm());
        final WebResponse webResponse = webclient.loadWebResponse(request);
        final FileOutputStream fos = new FileOutputStream(file);
        IOUtils.copy(webResponse.getContentAsStream(), fos);
        fos.close();
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Returns the default display style.
     *
     * @return the default display style.
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        return DisplayStyle.INLINE;
    }
}
