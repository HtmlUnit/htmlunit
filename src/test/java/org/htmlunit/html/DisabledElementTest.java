/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.htmlunit.BrowserVersion;
import org.htmlunit.SimpleWebTestCase;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the <code>isDisabled()</code> method on all of the elements that must implement the <code>disabled</code>
 * attribute:  <code>button</code>, <code>input</code>, <code>optgroup</code>, <code>option</code>, <code>select</code>
 * and <code>textarea</code>.
 *
 * @author David D. Kilzer
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class DisabledElementTest extends SimpleWebTestCase {

    /**
     * Tests data.
     * @return tests data
     */
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

    /**
     * Tests that the <code>isDisabled()</code> method returns {@code false} when the <code>disabled</code>
     * attribute does not exist.
     *
     * @throws Exception if the test fails
     */
    @ParameterizedTest
    @MethodSource("data")
    void noDisabledAttribute(final String elementHtml) throws Exception {
        executeDisabledTest(elementHtml, "", false);
    }

    /**
     * Tests that the <code>isDisabled()</code> method returns {@code true} when the <code>disabled</code>
     * attribute exists and is blank.
     *
     * @throws Exception if the test fails
     */
    @ParameterizedTest
    @MethodSource("data")
    void blankDisabledAttribute(final String elementHtml) throws Exception {
        executeDisabledTest(elementHtml, "disabled=''", true);
    }

    /**
     * Tests that the <code>isDisabled()</code> method returns {@code false} when the <code>disabled</code>
     * attribute exists and is <em>not</em> blank.
     *
     * @throws Exception if the test fails
     */
    @ParameterizedTest
    @MethodSource("data")
    void populatedDisabledAttribute(final String elementHtml) throws Exception {
        executeDisabledTest(elementHtml, "disabled='disabled'", true);
    }

    /**
     * Tests the <code>isDisabled()</code> method with the given parameters.
     *
     * @param disabledAttribute the definition of the <code>disabled</code> attribute
     * @param expectedIsDisabled the expected return value of the <code>isDisabled()</code> method
     * @throws Exception if test fails
     */
    private void executeDisabledTest(final String elementHtml,
            final String disabledAttribute, final boolean expectedIsDisabled)
                    throws Exception {

        String htmlContent = DOCTYPE_HTML + "<html><body><form id='form1'>{0}</form></body></html>";
        htmlContent = MessageFormat.format(htmlContent, new Object[]{elementHtml});


        htmlContent = MessageFormat.format(htmlContent, new Object[]{disabledAttribute});
        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(BrowserVersion.CHROME, htmlContent, collectedAlerts, URL_FIRST);

        final DisabledElement element = (DisabledElement) page.getHtmlElementById("element1");
        assertEquals(expectedIsDisabled, element.isDisabled());
    }

}
