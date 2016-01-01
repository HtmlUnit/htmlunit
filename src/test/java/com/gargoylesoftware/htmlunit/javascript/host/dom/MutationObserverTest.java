/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link MutationObserver}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class MutationObserverTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("old, new")
    public void characterData() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test(){\n"
            + "    var div = document.getElementById('myDiv');\n"
            + "    if (!window.MutationObserver) return;\n"
            + "    var observer = new MutationObserver(function(mutations) {\n"
            + "      mutations.forEach(function(mutation) {\n"
            + "        alert(mutation.oldValue);\n"
            + "        alert(mutation.target.textContent);\n"
            + "      });\n"
            + "    });\n"
            + "\n"
            + "    observer.observe(div, {\n"
            + "      characterData: true,\n"
            + "      characterDataOldValue: true,\n"
            + "      subtree: true\n"
            + "    });\n"
            + "\n"
            + "    div.firstChild.textContent = 'new';\n"
            + "}\n"
            + "</script></head>"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'>old</div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null, new")
    public void characterDataNoOldValue() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test(){\n"
            + "    var div = document.getElementById('myDiv');\n"
            + "    if (!window.MutationObserver) return;\n"
            + "    var observer = new MutationObserver(function(mutations) {\n"
            + "      mutations.forEach(function(mutation) {\n"
            + "        alert(mutation.oldValue);\n"
            + "        alert(mutation.target.textContent);\n"
            + "      });\n"
            + "    });\n"
            + "\n"
            + "    observer.observe(div, {\n"
            + "      characterData: true,\n"
            + "      subtree: true\n"
            + "    });\n"
            + "\n"
            + "    div.firstChild.textContent = 'new';\n"
            + "}\n"
            + "</script></head>"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'>old</div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void characterDataNoSubtree() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test(){\n"
            + "    var div = document.getElementById('myDiv');\n"
            + "    if (!window.MutationObserver) return;\n"
            + "    var observer = new MutationObserver(function(mutations) {\n"
            + "      mutations.forEach(function(mutation) {\n"
            + "        alert(mutation.oldValue);\n"
            + "        alert(mutation.target.textContent);\n"
            + "      });\n"
            + "    });\n"
            + "\n"
            + "    observer.observe(div, {\n"
            + "      characterData: true,\n"
            + "      characterDataOldValue: true\n"
            + "    });\n"
            + "\n"
            + "    div.firstChild.textContent = 'new';\n"
            + "}\n"
            + "</script></head>"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'>old</div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("attributes, ltr")
    public void attributes() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test(){\n"
            + "    var div = document.getElementById('myDiv');\n"
            + "    if (!window.MutationObserver) return;\n"
            + "    var observer = new MutationObserver(function(mutations) {\n"
            + "      mutations.forEach(function(mutation) {\n"
            + "        alert(mutation.type);\n"
            + "        alert(mutation.oldValue);\n"
            + "      });\n"
            + "    });\n"
            + "\n"
            + "    observer.observe(div, {\n"
            + "      attributes: true,\n"
            + "      attributeFilter: ['dir'],\n"
            + "      attributeOldValue: true\n"
            + "    });\n"
            + "\n"
            + "    div.dir = 'rtl';\n"
            + "}\n"
            + "</script></head>"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv' dir='ltr'>old</div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

}
