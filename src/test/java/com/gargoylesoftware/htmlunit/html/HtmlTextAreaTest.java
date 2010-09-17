/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlTextArea}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Amit Khanna
 */
@RunWith(BrowserRunner.class)
public class HtmlTextAreaTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void formSubmission() throws Exception {
        formSubmission("foo", "foo");
        formSubmission("\r\nfoo\r\n", "foo%0D%0A");
        formSubmission("\nfoo\n", "foo%0D%0A");
        formSubmission("\r\n\r\nfoo\r\n", "%0D%0Afoo%0D%0A");
    }

    private void formSubmission(final String textAreaText, final String expectedValue) throws Exception {
        final String content
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<textarea name='textArea1'>" + textAreaText + "</textarea>\n"
            + "<input type='submit' id='mysubmit'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(content);
        final MockWebConnection webConnection = getMockConnection(page);
        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlTextArea textArea = form.getTextAreaByName("textArea1");
        assertNotNull(textArea);

        final Page secondPage = page.getElementById("mysubmit").click();

        assertEquals("url", getDefaultUrl() + "?textArea1=" + expectedValue,
                secondPage.getWebResponse().getWebRequest().getUrl());
        assertSame("method", HttpMethod.GET, webConnection.getLastMethod());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testFormSubmission_NewValue() throws Exception {
        final String content
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<textarea name='textArea1'>foo</textarea>\n"
            + "<input type='submit' id='mysubmit'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(content);
        final MockWebConnection webConnection = getMockConnection(page);
        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlTextArea textArea = form.getTextAreaByName("textArea1");
        textArea.setText("Flintstone");
        final Page secondPage = page.getElementById("mysubmit").click();

        assertEquals("url", getDefaultUrl() + "?textArea1=Flintstone",
                secondPage.getWebResponse().getWebRequest().getUrl());
        assertSame("method", HttpMethod.GET, webConnection.getLastMethod());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetText() throws Exception {
        final String content
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<textarea name='textArea1'> foo \n bar </textarea>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(content);
        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlTextArea textArea = form.getTextAreaByName("textArea1");
        assertNotNull(textArea);
        Assert.assertEquals("White space must be preserved!", " foo \n bar ", textArea.getText());
        Assert.assertEquals(" foo \n bar ", textArea.getDefaultValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asXml() throws Exception {
        final String content
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<textarea name='textArea1'> foo \n bar </textarea>\n"
            + "<textarea name='textArea2'></textarea>\n"
            + "<textarea name='textArea3'>1 &lt; 2 &amp; 3 &gt; 2</textarea>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(content);
        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlTextArea textArea1 = form.getTextAreaByName("textArea1");
        final String expected1 = "<textarea name=\"textArea1\"> foo \n bar </textarea>";
        assertEquals(expected1, textArea1.asXml());
        assertTrue(form.asXml(), form.asXml().contains(expected1));

        final HtmlTextArea textArea2 = form.getTextAreaByName("textArea2");
        assertEquals("<textarea name=\"textArea2\"></textarea>", textArea2.asXml());

        final HtmlTextArea textArea3 = form.getTextAreaByName("textArea3");
        assertEquals("<textarea name=\"textArea3\">1 &lt; 2 &amp; 3 &gt; 2</textarea>", textArea3.asXml());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testPreventDefault() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function handler(e) {\n"
            + "    if (e && e.target.value.length > 2)\n"
            + "      e.preventDefault();\n"
            + "    else if (!e && window.event.srcElement.value.length > 2)\n"
            + "      return false;\n"
            + "  }\n"
            + "  function init() {\n"
            + "    document.getElementById('text1').onkeydown = handler;\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='init()'>\n"
            + "<textarea id='text1'></textarea>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlTextArea text1 = page.getHtmlElementById("text1");
        text1.type("abcd");
        assertEquals("abc", text1.getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void type() throws Exception {
        final String html = "<html><head></head><body><textarea id='t'></textarea></body></html>";
        final HtmlPage page = loadPage(html, null);
        final HtmlTextArea t = page.getHtmlElementById("t");
        t.type("abc");
        assertEquals("abc", t.getText());
        t.type('\b');
        assertEquals("ab", t.getText());
        t.type('\b');
        assertEquals("a", t.getText());
        t.type('\b');
        assertEquals("", t.getText());
        t.type('\b');
        assertEquals("", t.getText());
        t.type("ab\ncd");
        assertEquals("ab\ncd", t.getText());
        t.type("\r\nef");
        assertEquals("ab\ncd\r\nef", t.getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void typeWhileDisabled() throws Exception {
        final String html = "<html><body><textarea id='t' disabled='disabled'></textarea></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlTextArea t = page.getHtmlElementById("t");
        t.type("abc");
        assertEquals("", t.getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asText() throws Exception {
        final String content
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<textarea name='textArea1'> foo \n bar "
            + "<p>html snippet</p>\n"
            + "</textarea>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(content);
        final HtmlForm form = page.getHtmlElementById("form1");
        final HtmlTextArea textArea = form.getTextAreaByName("textArea1");
        assertNotNull(textArea);
        Assert.assertEquals("White space must be preserved!",
            " foo " + LINE_SEPARATOR + " bar <p>html snippet</p>" + LINE_SEPARATOR, textArea.asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testParentAsText() throws Exception {
        final String content
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<textarea name='textArea1'> foo \n bar "
            + "<p>html snippet</p>\n"
            + "</textarea>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(content);
        final HtmlForm form = page.getHtmlElementById("form1");
        Assert.assertEquals(" foo " + LINE_SEPARATOR + " bar <p>html snippet</p>" + LINE_SEPARATOR, form.asText());
    }

    /**
     * Style=visibility:hidden should not affect getText().
     * @throws Exception if the test fails
     */
    @Test
    public void getTextAndVisibility() throws Exception {
        final String content
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "<textarea name='textArea1' style='visibility:hidden'> foo \n bar "
            + "</textarea>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(content);
        final HtmlForm form = page.getHtmlElementById("form1");
        final HtmlTextArea textArea = form.getTextAreaByName("textArea1");
        assertNotNull(textArea);
        assertEquals(" foo \n bar ", textArea.getText());
        assertEquals("", textArea.asText());
    }
}
