/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests the <code>isDisabled()</code> method on all of the elements that must implement the <code>disabled</code>
 * attribute:  <code>button</code>, <code>input</code>, <code>optgroup</code>, <code>option</code>, <code>select</code>
 * and <code>textarea</code>.
 *
 * @version $Revision$
 * @author David D. Kilzer
 * @author Ahmed Ashour
 */
@RunWith(Parameterized.class)
public class DisabledElementTest extends WebTestCase {

    /**
     * Tests data.
     * @return tests data
     */
    @Parameters
    public static Collection<String[]> data() {
        return Arrays.asList(new String[][] {
            {"<button id='element1' {0}>foo</button>"},
            {"<input type='button' id='element1' {0}>"},
            {"<input type='checkbox' id='element1' {0}>"},
            {"<input type='file' id='element1' {0}>"},
            {"<input type='hidden' id='element1' {0}>"},
            {"<input type='image' id='element1' {0}>"},
            {"<input type='password' id='element1' {0}>"},
            {"<input type='radio' id='element1' {0}>"},
            {"<input type='reset' id='element1' {0}>"},
            {"<input type='submit' id='element1' {0}>"},
            {"<input type='text' id='element1' {0}>"},
            {"<select><optgroup id='element1' {0}><option value='1'></option></optgroup></select>"},
            {"<select><option id='element1' value='1' {0}></option></select>"},
            {"<select id='element1' {0}><option value='1'></option></select>"},
            {"<textarea id='element1' {0}></textarea>"}
        });
    }

    private final String htmlContent_;

    /**
     * Creates an instance of the test class for testing <em>one</em> of the test methods.
     *
     * @param elementHtml the HTML representing the element to test with attribute <code>id='element1'</code>
     */
    public DisabledElementTest(final String elementHtml) {
        final String htmlContent = "<html><body><form id='form1'>{0}</form></body></html>";
        htmlContent_ = MessageFormat.format(htmlContent, new Object[]{elementHtml});
    }

    /**
     * Tests that the <code>isDisabled()</code> method returns <code>false</code> when the <code>disabled</code>
     * attribute does not exist.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void noDisabledAttribute() throws Exception {
        executeDisabledTest("", false);
    }

    /**
     * Tests that the <code>isDisabled()</code> method returns <code>true</code> when the <code>disabled</code>
     * attribute exists and is blank.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void blankDisabledAttribute() throws Exception {
        executeDisabledTest("disabled=''", true);
    }

    /**
     * Tests that the <code>isDisabled()</code> method returns <code>false</code> when the <code>disabled</code>
     * attribute exists and is <em>not</em> blank.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void populatedDisabledAttribute() throws Exception {
        executeDisabledTest("disabled='disabled'", true);
    }

    /**
     * Tests the <code>isDisabled()</code> method with the given parameters.
     *
     * @param disabledAttribute the definition of the <code>disabled</code> attribute
     * @param expectedIsDisabled the expected return value of the <code>isDisabled()</code> method
     * @throws Exception if test fails
     */
    private void executeDisabledTest(final String disabledAttribute, final boolean expectedIsDisabled)
        throws Exception {

        final String htmlContent = MessageFormat.format(htmlContent_, new Object[]{disabledAttribute});
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        
        final HtmlForm form = page.getHtmlElementById("form1");
        final DisabledElement element = (DisabledElement) form.getHtmlElementById("element1");
        assertEquals(expectedIsDisabled, element.isDisabled());
    }
}
