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
package com.gargoylesoftware.htmlunit.javascript;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for {@link HtmlUnitContextFactory}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlUnitContextFactoryTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void customBrowserVersion() throws Exception {
        final String html = "<html></html>";
        final String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0";
        final BrowserVersion browserVersion = new BrowserVersion("Firefox",
                "5.0 (Windows NT 10.0; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0", userAgent, 39.0f);

        loadPage(browserVersion, html, null, URL_FIRST);
    }
}
