/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Proxy.
 *
 * @author Ronald Brill
 */
public class NativeProxyTest extends WebDriverTestCase {

    @Test
    @Alerts("correct")
    public void getTrapReceivesCorrectReceiverWhenDifferentFromProxy() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var res = [];\n"
                + "  var target = {};\n"
                + "  var otherReceiver = { label: 'other' };\n"
                + "  var proxy = new Proxy(target, {\n"
                + "    get: function(t, prop, receiver) {\n"
                + "      res.push(receiver === otherReceiver ? 'correct' : 'wrong');\n"
                + "      return 42;\n"
                + "    }\n"
                + "  });\n"
                + "  Reflect.get(proxy, 'p', otherReceiver);\n"
                + "  log('' + res);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts("true")
    public void getTrapReceivesProxyAsReceiverWhenNoExplicitReceiverGiven() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var target = {};\n"
                + "  var proxy = new Proxy(target, {\n"
                + "    get: function(t, prop, receiver) {\n"
                + "      return receiver === proxy;\n"
                + "    }\n"
                + "  });\n"
                + "  log('' + Reflect.get(proxy, 'p'));\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts("proxy")
    public void getTrapReceiverIsProxyForDirectPropertyRead() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var res = [];\n"
                + "  var target = {};\n"
                + "  var proxy = new Proxy(target, {\n"
                + "    get: function(t, prop, receiver) {\n"
                + "      res.push(receiver === proxy ? 'proxy' : 'other');\n"
                + "      return 1;\n"
                + "    }\n"
                + "  });\n"
                + "  var _ = proxy.p;\n"
                + "  log('' + res);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts("correct")
    public void getTrapReceivesCorrectReceiverWithIndexKey() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var res = [];\n"
                + "  var target = [];\n"
                + "  var otherReceiver = { label: 'other' };\n"
                + "  var proxy = new Proxy(target, {\n"
                + "    get: function(t, prop, receiver) {\n"
                + "      if (prop === '0') {\n"
                + "        res.push(receiver === otherReceiver ? 'correct' : 'wrong');\n"
                + "      }\n"
                + "      return undefined;\n"
                + "    }\n"
                + "  });\n"
                + "  Reflect.get(proxy, 0, otherReceiver);\n"
                + "  log('' + res);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts("correct")
    public void getTrapReceivesCorrectReceiverWithSymbolKey() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var res = [];\n"
                + "  var sym = Symbol('test');\n"
                + "  var target = {};\n"
                + "  var otherReceiver = { label: 'other' };\n"
                + "  var proxy = new Proxy(target, {\n"
                + "    get: function(t, prop, receiver) {\n"
                + "      if (prop === sym) {\n"
                + "        res.push(receiver === otherReceiver ? 'correct' : 'wrong');\n"
                + "      }\n"
                + "      return undefined;\n"
                + "    }\n"
                + "  });\n"
                + "  Reflect.get(proxy, sym, otherReceiver);\n"
                + "  log('' + res);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
