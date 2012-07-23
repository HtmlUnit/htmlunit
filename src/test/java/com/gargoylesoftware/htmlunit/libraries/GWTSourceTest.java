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
package com.gargoylesoftware.htmlunit.libraries;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests of the source repository of <a href="http://code.google.com/webtoolkit">Google Web Toolkit</a>,
 * which are marked to fail with HtmlUnit.
 *
 * To generate the JavaScript, copy the test case to "Hello" GWT sample, compile it with "-style PRETTY"
 * by modifying "gwtc" target, and run "ant".  In generated ".nocache.js", search for "ie6" or "gecko1_8"
 * to know which JavaScript file corresponds to IE or FF respectively.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class GWTSourceTest extends WebDriverTestCase {

    /**
     * Test case for
     * <a href="http://code.google.com/p/google-web-toolkit/source/browse/trunk/user/test/com/google/gwt/core/client/impl/StackTraceCreatorTest.java">StackTraceCreatorTest</a>.
     *
     * Test case to be moved to {@link com.gargoylesoftware.htmlunit.javascript.SimpleScriptableTest}
     *
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(Browser.IE)
    @Alerts(IE = "undefined", FF = "true")
    public void stack() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    null.method();\n"
            + "  } catch (e) {\n"
            + "    if (e.stack)\n"
            + "      alert(e.stack.indexOf('test()@') != -1);\n"
            + "    else\n"
            + "      alert('undefined');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
