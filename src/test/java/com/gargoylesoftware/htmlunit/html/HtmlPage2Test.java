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
package com.gargoylesoftware.htmlunit.html;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebServerTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlPage}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlPage2Test extends WebServerTestCase {

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void constructor() throws Exception {
        final String html = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "<p>hello world</p>\n"
            + "<form id='form1' action='/formSubmit' method='post'>\n"
            + "<input type='text' NAME='textInput1' value='textInput1'/>\n"
            + "<input type='text' name='textInput2' value='textInput2'/>\n"
            + "<input type='hidden' name='hidden1' value='hidden1'/>\n"
            + "<input type='submit' name='submitInput1' value='push me'/>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertEquals("foo", page.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testGetInputByName() throws Exception {
        final String html = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "<p>hello world</p>\n"
            + "<form id='form1' action='/formSubmit' method='post'>\n"
            + "<input type='text' NAME='textInput1' value='textInput1'/>\n"
            + "<input type='text' name='textInput2' value='textInput2'/>\n"
            + "<input type='hidden' name='hidden1' value='hidden1'/>\n"
            + "<input type='submit' name='submitInput1' value='push me'/>\n"
            + "</form>\n"
            + "</body></html>";
        final HtmlPage page = loadPageWithAlerts(html);

        final HtmlForm form = page.getHtmlElementById("form1");
        final HtmlInput input = form.getInputByName("textInput1");
        Assert.assertEquals("name", "textInput1", input.getNameAttribute());

        Assert.assertEquals("value", "textInput1", input.getValueAttribute());
        Assert.assertEquals("type", "text", input.getTypeAttribute());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "25", FF = "error")
    public void loadExternalJavaScript() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "function makeIframe() {\n"
            + "  var iframesrc = '<html><head>';\n"
            + "  iframesrc += '<script src=\"" + "js.js" + "\"></' + 'script>';\n"
            + "  iframesrc += '<script>';\n"
            + "  iframesrc += 'function doSquared(){';\n"
            + "  iframesrc += '    try {';\n"
            + "  iframesrc += '      var y = squared(5);';\n"
            + "  iframesrc += '      alert(y);';\n"
            + "  iframesrc += '    } catch (e) {';\n"
            + "  iframesrc += '      alert(\"error\");';\n"
            + "  iframesrc += '    }'\n"
            + "  iframesrc += '};';\n"
            + "  iframesrc += '</' + 'script>';\n"
            + "  iframesrc += '</head>';\n"
            + "  iframesrc += '<body onLoad=\"doSquared()\" >';\n"
            + "  iframesrc += '</body>';\n"
            + "  iframesrc += '</html>';\n"
            + "  var iframe = document.createElement('IFRAME');\n"
            + "  iframe.id = 'iMessage';\n"
            + "  iframe.name = 'iMessage';\n"
            + "  iframe.src = \"javascript:'\" + iframesrc + \"'\";\n"
            + "  document.body.appendChild(iframe);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='makeIframe()'>\n"
            + "</body></html>";

        final String js = "function squared(n) {return n * n}";

        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(new URL(URL_FIRST, "js.js"), js);
        webClient.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        webClient.getPage(URL_FIRST);
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Differs from {@link #loadExternalJavaScript()} by the absolute reference of the javascript source.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("25")
    public void loadExternalJavaScript_absolute() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "function makeIframe() {\n"
            + "  var iframesrc = '<html><head>';\n"
            + "  iframesrc += '<script src=\"" + URL_SECOND + "\"></' + 'script>';\n"
            + "  iframesrc += '<script>';\n"
            + "  iframesrc += 'function doSquared(){';\n"
            + "  iframesrc += '    try {';\n"
            + "  iframesrc += '      var y = squared(5);';\n"
            + "  iframesrc += '      alert(y);';\n"
            + "  iframesrc += '    } catch (e) {';\n"
            + "  iframesrc += '      alert(\"error\");';\n"
            + "  iframesrc += '    }'\n"
            + "  iframesrc += '};';\n"
            + "  iframesrc += '</' + 'script>';\n"
            + "  iframesrc += '</head>';\n"
            + "  iframesrc += '<body onLoad=\"doSquared()\" >';\n"
            + "  iframesrc += '</body>';\n"
            + "  iframesrc += '</html>';\n"
            + "  var iframe = document.createElement('IFRAME');\n"
            + "  iframe.id = 'iMessage';\n"
            + "  iframe.name = 'iMessage';\n"
            + "  iframe.src = \"javascript:'\" + iframesrc + \"'\";\n"
            + "  document.body.appendChild(iframe);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='makeIframe()'>\n"
            + "</body></html>";

        final String js = "function squared(n) {return n * n}";

        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(URL_SECOND, js);
        webClient.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        webClient.getPage(URL_FIRST);
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getFullQualifiedUrl_topWindow() throws Exception {
        final String firstHtml = "<html><head><title>first</title>\n"
            + "<script>\n"
            + "  function init() {\n"
            + "    var iframe = window.frames['f'];\n"
            + "    iframe.document.write(\"<form name='form' action='" + URL_SECOND + "'>"
            + "<input name='submit' type='submit'></form>\");\n"
            + "    iframe.document.close();\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='init()'>\n"
            + "  <iframe name='f'></iframe>\n"
            + "</body></html>";
        final String secondHtml = "<html><head><title>second</title></head>"
            + "<body><p>Form submitted successfully.</p></body></html>";

        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstHtml);
        webConnection.setDefaultResponse(secondHtml);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);

        HtmlPage framePage = (HtmlPage) page.getFrameByName("f").getEnclosedPage();
        final HtmlForm form = framePage.getFormByName("form");
        final HtmlInput submit = form.getInputByName("submit");
        framePage = submit.click();
        assertEquals("Form submitted successfully.", framePage.getBody().asText());
    }
}
