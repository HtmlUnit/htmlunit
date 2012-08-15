/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for {@link HtmlHtml}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlHtmlTest extends SimpleWebTestCase {
    /**
     * @throws Exception if the test fails
     */
    @Test
    public void attributes() throws Exception {
        final String htmlContent = "<?xml version=\"1.0\"?>\n"
            + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" "
            + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
            + "<html xmlns='http://www.w3.org/1999/xhtml' lang='en' xml:lang='en'>\n"
            + "<head><title>test</title></head>\n"
            + "<body></body></html>";

        final HtmlPage page = loadPage(htmlContent);
        final HtmlHtml root = (HtmlHtml) page.getDocumentElement();
        assertEquals("en", root.getLangAttribute());
        assertEquals("en", root.getXmlLangAttribute());
    }

    /**
     * Regression test for
     * <a href="http://sf.net/support/tracker.php?aid=2865948">Bug 2865948</a>:
     * canonical XPath for html element was computed to "/html[2]" where a doctype
     * was present.
     * @throws Exception if the test fails
     */
    @Test
    public void canonicalXPath() throws Exception {
        final String htmlContent = "<?xml version=\"1.0\"?>\n"
            + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" "
            + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
            + "<html xmlns='http://www.w3.org/1999/xhtml' lang='en' xml:lang='en'>\n"
            + "<head><title>test</title></head>\n"
            + "<body></body></html>";

        final HtmlPage page = loadPage(htmlContent);
        final HtmlHtml root = (HtmlHtml) page.getDocumentElement();
        assertEquals("/html", root.getCanonicalXPath());
    }
}
