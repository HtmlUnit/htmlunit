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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import java.io.StringReader;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Selector;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLStyleElement;

/**
 * Tests for {@link SelectorSpecificity}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class SelectorSpecificityTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void selectorSpecifity() throws Exception {
        final SelectorSpecificity specificy0 = selectorSpecifity("*", "0,0,0,0");
        final SelectorSpecificity specificy1 = selectorSpecifity("li", "0,0,0,1");
        final SelectorSpecificity specificy2a = selectorSpecifity("li:first-line", "0,0,0,2");
        final SelectorSpecificity specificy2b = selectorSpecifity("ul li", "0,0,0,2");
        final SelectorSpecificity specificy2c = selectorSpecifity("body > p", "0,0,0,2");
        final SelectorSpecificity specificy3 = selectorSpecifity("ul ol+li", "0,0,0,3");
        final SelectorSpecificity specificy11 = selectorSpecifity("h1 + *[rel=up]", "0,0,1,1");
        final SelectorSpecificity specificy13 = selectorSpecifity("ul ol li.red", "0,0,1,3");
        final SelectorSpecificity specificy21 = selectorSpecifity("li.red.level", "0,0,2,1");
        final SelectorSpecificity specificy100 = selectorSpecifity("#x34y", "0,1,0,0");

        Assert.assertEquals(0, specificy0.compareTo(specificy0));
        Assert.assertTrue(specificy0.compareTo(specificy1) < 0);
        Assert.assertTrue(specificy0.compareTo(specificy2a) < 0);
        Assert.assertTrue(specificy0.compareTo(specificy13) < 0);

        Assert.assertEquals(0, specificy1.compareTo(specificy1));
        Assert.assertTrue(specificy1.compareTo(specificy0) > 0);
        Assert.assertTrue(specificy1.compareTo(specificy2a) < 0);
        Assert.assertTrue(specificy1.compareTo(specificy13) < 0);

        Assert.assertEquals(0, specificy2a.compareTo(specificy2b));
        Assert.assertEquals(0, specificy2a.compareTo(specificy2c));
        Assert.assertTrue(specificy2a.compareTo(specificy0) > 0);
        Assert.assertTrue(specificy2a.compareTo(specificy3) < 0);
        Assert.assertTrue(specificy2a.compareTo(specificy11) < 0);
        Assert.assertTrue(specificy2a.compareTo(specificy13) < 0);
        Assert.assertTrue(specificy2a.compareTo(specificy100) < 0);

        Assert.assertEquals(0, specificy11.compareTo(specificy11));
        Assert.assertTrue(specificy11.compareTo(specificy0) > 0);
        Assert.assertTrue(specificy11.compareTo(specificy13) < 0);
        Assert.assertTrue(specificy11.compareTo(specificy21) < 0);
        Assert.assertTrue(specificy11.compareTo(specificy100) < 0);
    }

    private SelectorSpecificity selectorSpecifity(final String selector, final String expectedSpecificity)
        throws Exception {
        final String html =
            "<html><body id='b'><style></style>\n"
            + "<div id='d' class='foo bar'><span>x</span><span id='s'>a</span>b</div>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlStyle node = (HtmlStyle) page.getElementsByTagName("style").item(0);
        final HTMLStyleElement host = (HTMLStyleElement) node.getScriptObject();
        final CSSStyleSheet sheet = host.getSheet();

        final Selector selectorObject = parseSelector(sheet, selector);
        final SelectorSpecificity specificity = new SelectorSpecificity(selectorObject);
        assertEquals(expectedSpecificity, specificity.toString());
        return specificity;
    }

    private Selector parseSelector(final CSSStyleSheet sheet, final String rule) {
        return sheet.parseSelectors(new InputSource(new StringReader(rule))).item(0);
    }
}
