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

import java.util.List;

import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HtmlOption}.
 *
 * @author Mike Bowler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 */
public class HtmlOptionTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void select() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "  <form id='form1'>\n"
            + "    <select name='select1' id='select1'>\n"
            + "      <option value='option1' id='option1'>Option1</option>\n"
            + "      <option value='option2' id='option2' selected='selected'>Option2</option>\n"
            + "      <option value='option3' id='option3'>Option3</option>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlOption option1 = page.getHtmlElementById("option1");
        final HtmlOption option2 = page.getHtmlElementById("option2");
        final HtmlOption option3 = page.getHtmlElementById("option3");

        assertFalse(option1.isSelected());
        assertTrue(option2.isSelected());
        assertFalse(option3.isSelected());

        option3.setSelected(true);

        assertFalse(option1.isSelected());
        assertFalse(option2.isSelected());
        assertTrue(option3.isSelected());

        option3.setSelected(false);

        assertFalse(option1.isSelected());
        assertFalse(option2.isSelected());
        assertFalse(option3.isSelected());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getValue() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "  <form id='form1'>\n"
            + "    <select name='select1' id='select1'>\n"
            + "      <option value='option1' id='option1'>Option1</option>\n"
            + "      <option id='option2' selected>Number Two</option>\n"
            + "  </select>\n"
            + "  </form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlOption option1 = page.getHtmlElementById("option1");
        final HtmlOption option2 = page.getHtmlElementById("option2");

        assertEquals("option1", option1.getValueAttribute());
        assertEquals("Number Two", option2.getValueAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getValue_ContentsIsValue() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "  <form id='form1'>\n"
            + "    <select name='select1' id='select1'>\n"
            + "      <option id='option1'>Option1</option>\n"
            + "      <option id='option2' selected>Number Two</option>\n"
            + "      <option id='option3'>\n  Number 3 with blanks </option>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlOption option1 = page.getHtmlElementById("option1");
        assertEquals("Option1", option1.getValueAttribute());

        final HtmlOption option2 = page.getHtmlElementById("option2");
        assertEquals("Number Two", option2.getValueAttribute());

        final HtmlOption option3 = page.getHtmlElementById("option3");
        assertEquals("Number 3 with blanks", option3.getValueAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void click() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <form id='form1'>\n"
            + "    <select name='select1' id='select1'>\n"
            + "      <option id='option1'>Option1</option>\n"
            + "      <option id='option2' selected>Number Two</option>\n"
            + "    </select>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);

        final HtmlOption option1 = page.getHtmlElementById("option1");
        assertFalse(option1.isSelected());
        option1.click();
        assertTrue(option1.isSelected());
        option1.click();
        assertTrue(option1.isSelected());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asNormalizedText() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "  <form>\n"
            + "    <select>\n"
            + "      <option id='option1'>option1</option>\n"
            + "      <option id='option2' label='Number Two'/>\n"
            + "      <option id='option3' label='overridden'>Number Three</option>\n"
            + "      <option id='option4'>Number&nbsp;4</option>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);

        final HtmlOption option1 = page.getHtmlElementById("option1");
        final HtmlOption option2 = page.getHtmlElementById("option2");
        final HtmlOption option3 = page.getHtmlElementById("option3");
        final HtmlOption option4 = page.getHtmlElementById("option4");

        assertEquals("option1", option1.asNormalizedText());
        assertEquals("", option2.asNormalizedText());
        assertEquals("Number Three", option3.asNormalizedText());
        assertEquals("Number 4", option4.asNormalizedText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "false", "true", "true", "false"})
    public void disabled() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test()'><form name='f'>\n"
            + "  <select name='s' id='s'>\n"
            + "    <option value='o1' id='o1'>One</option>\n"
            + "    <option value='o2' id='o2' disabled='disabled'>Two</option>\n"
            + "  </select>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var s = document.getElementById('s');\n"
            + "      var o1 = document.getElementById('o1');\n"
            + "      var o2 = document.getElementById('o2');\n"
            + "      alert(s.disabled);\n"
            + "      alert(o1.disabled);\n"
            + "      alert(o2.disabled);\n"
            + "      o1.disabled = true;\n"
            + "      o2.disabled = false;\n"
            + "      alert(o1.disabled);\n"
            + "      alert(o2.disabled);\n"
            + "    }\n"
            + "  </script>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertTrue(((HtmlOption) page.getElementById("o1")).isDisabled());
        assertFalse(((HtmlOption) page.getElementById("o2")).isDisabled());
    }

    /**
     * Test case for #1864.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void isSelected() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>"
                + "  <select multiple><option value='a'>a</option><option value='b'>b</option></select>\n"
                + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlSelect select = (HtmlSelect) page.getElementsByTagName("select").get(0);
        assertTrue(select.isMultipleSelectEnabled());

        final List<HtmlOption> options = select.getOptions();
        for (final HtmlOption option : options) {
            option.setSelected(true);
        }

        for (final HtmlOption option : options) {
            assertTrue(option.isSelected());
        }
    }
}
