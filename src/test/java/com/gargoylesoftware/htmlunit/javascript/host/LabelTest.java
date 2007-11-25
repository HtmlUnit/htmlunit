/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlLabel;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link Label}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class LabelTest extends WebTestCase {

    /**
     * Create an instance
     * @param name The name of the test
     */
    public LabelTest(final String name) {
        super(name);
    }

    /**
     * @throws Exception if the test fails
     */
    public void testHtmlFor() throws Exception {
        testHtmlFor(BrowserVersion.INTERNET_EXPLORER_7_0);
        testHtmlFor(BrowserVersion.FIREFOX_2);
    }

    private void testHtmlFor(final BrowserVersion browserVersion) throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    document.getElementById('label1').htmlFor = 'checkbox1';\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<label id='label1'>My Label</label>\n"
            + "<input type='checkbox' id='checkbox1'><br>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlLabel label = (HtmlLabel) page.getHtmlElementById("label1");
        final HtmlCheckBoxInput checkbox = (HtmlCheckBoxInput) page.getHtmlElementById("checkbox1");
        assertFalse(checkbox.isChecked());
        label.click();
        assertTrue(checkbox.isChecked());
    }

    /**
     * Tests that clicking the label by javascript does not change 'htmlFor' attribute in FF!!
     *
     * @throws Exception if the test fails
     */
    public void testHtmlFor_click() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        testHtmlFor_click(BrowserVersion.INTERNET_EXPLORER_7_0, true);
        testHtmlFor_click(BrowserVersion.FIREFOX_2, false);
    }

    /**
     * @param changedByClick if 'label.click()' javascript causes the associated 'htmlFor' element to be changed.
     */
    private void testHtmlFor_click(final BrowserVersion browserVersion, final boolean changedByClick) throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    document.getElementById('label1').htmlFor = 'checkbox1';\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<label id='label1'>My Label</label>\n"
            + "<input type='checkbox' id='checkbox1'><br>\n"
            + "<input type=button id='button1' value='Test' onclick='document.getElementById(\"label1\").click()'>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlCheckBoxInput checkbox = (HtmlCheckBoxInput) page.getHtmlElementById("checkbox1");
        final HtmlButtonInput button = (HtmlButtonInput) page.getHtmlElementById("button1");
        assertFalse(checkbox.isChecked());
        button.click();
        assertTrue(checkbox.isChecked() == changedByClick);
    }
}
