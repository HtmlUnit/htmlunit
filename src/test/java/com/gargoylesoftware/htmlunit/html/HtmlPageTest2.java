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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebServerTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests for {@link HtmlPage}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlPageTest2 extends WebServerTestCase {

    /**
     * 
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = "25", FF = "error")
    @NotYetImplemented(Browser.FF)
    public void loadExternalJavaScript() throws Exception {
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
}
