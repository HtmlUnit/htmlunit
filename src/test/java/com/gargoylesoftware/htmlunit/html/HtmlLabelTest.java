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
package com.gargoylesoftware.htmlunit.html;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 *  Tests for {@link HtmlLabel}
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class HtmlLabelTest extends WebTestCase {
    /**
     *  Create an instance
     *
     * @param  name The name of the test
     */
    public HtmlLabelTest( final String name ) {
        super( name );
    }

    /**
     * Verifies that a checkbox is togled when the related label is clicked.
     * @throws Exception if the test fails
     */
    public void test_click() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>"
            + "<form id='form1'>"
            + " <input type='checkbox' name='checkbox' id='testCheckbox' onclick='alert(\"checkbox\")'/>"
            + " <label for='testCheckbox' id='testLabel' onclick='alert(\"label\")'>Check me</label>"
            + "</form></body></html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        final HtmlCheckBoxInput checkBox = (HtmlCheckBoxInput) page.getHtmlElementById("testCheckbox");

        assertFalse(checkBox.isChecked());
        final HtmlLabel label = (HtmlLabel) page.getHtmlElementById("testLabel");
        label.click();
        assertTrue(checkBox.isChecked());
        final String[] expectedAlerts = new String[] {"label", "checkbox"};
        assertEquals(expectedAlerts, collectedAlerts);
        label.click();
        assertFalse(checkBox.isChecked());
    }

    /**
     * 
     * @throws Exception if the test fails
     */
    public void test_getReferencedElement() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>"
            + "<form id='form1'>"
            + " <input type='checkbox' name='checkbox' id='testCheckbox'/>"
            + " <label for='testCheckbox' id='testLabel1'>Check me</label>"
            + " <label for='notExisting' id='testLabel2'>Check me too</label>"
            + "</form></body></html>";
        final List collectedAlerts = new ArrayList();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        final HtmlCheckBoxInput checkBox = (HtmlCheckBoxInput) page.getHtmlElementById("testCheckbox");

        final HtmlLabel label = (HtmlLabel) page.getHtmlElementById("testLabel1");
        assertSame(checkBox, label.getReferencedElement());
        final HtmlLabel label2 = (HtmlLabel) page.getHtmlElementById("testLabel2");
        assertNull(label2.getReferencedElement());
    }
}
