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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Selector;
import org.w3c.dom.NodeList;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlStyle;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLStyleElement;

/**
 * Unit tests for {@link CSSStyleSheet}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class CSSStyleSheet2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void selects_miscSelectors() throws Exception {
        final String html = "<html><head><title>test</title>\n"
            + "</head><body><style></style>\n"
            + "<form name='f1' action='foo' class='yui-log'>\n"
            + "<div><div><input name='i1' id='m1'></div></div>\n"
            + "<input name='i2' class='yui-log'>\n"
            + "<button name='b1' class='yui-log'>\n"
            + "<button name='b2'>\n"
            + "</form>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlElement body = page.getBody();
        final HtmlForm form = page.getFormByName("f1");
        final HtmlInput input1 = (HtmlInput) page.getElementsByName("i1").get(0);
        final HtmlInput input2 = (HtmlInput) page.getElementsByName("i2").get(0);
        final DomElement button1 = page.getElementsByName("b1").get(0);
        final DomElement button2 = page.getElementsByName("b2").get(0);

        final HtmlStyle node = (HtmlStyle) page.getElementsByTagName("style").item(0);
        final HTMLStyleElement host = (HTMLStyleElement) node.getScriptObject();
        final CSSStyleSheet sheet = host.jsxGet_sheet();

        Selector selector = parseSelector(sheet, "*.yui-log input");
        assertFalse(sheet.selects(selector, body));
        assertFalse(sheet.selects(selector, form));
        assertTrue(sheet.selects(selector, input1));
        assertTrue(sheet.selects(selector, input2));
        assertFalse(sheet.selects(selector, button1));
        assertFalse(sheet.selects(selector, button2));

        selector = parseSelector(sheet, "#m1");
        assertTrue(sheet.selects(selector, input1));
        assertFalse(sheet.selects(selector, input2));
    }

    private Selector parseSelector(final CSSStyleSheet sheet, final String rule) {
        return sheet.parseSelectors(new InputSource(new StringReader(rule))).item(0);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void selects_anyNodeSelector() throws Exception {
        testSelects("*", true, true, true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void selects_childSelector() throws Exception {
        testSelects("body > div", false, true, false);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void selects_descendantSelector() throws Exception {
        testSelects("body span", false, false, true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void selects_elementSelector() throws Exception {
        testSelects("div", false, true, false);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void selects_directAdjacentSelector() throws Exception {
        testSelects("span + span", false, false, true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void selects_conditionalSelector_idCondition() throws Exception {
        testSelects("span#s", false, false, true);
        testSelects("#s", false, false, true);
        testSelects("span[id=s]", false, false, true);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void selectsIdConditionWithSpecialChars() throws Exception {
        final String html =
                "<html><body><style></style>\n"
              + "<div id='d:e'></div>"
              + "<div id='d-e'></div>"
              + "</body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlStyle node = (HtmlStyle) page.getElementsByTagName("style").item(0);
        final HTMLStyleElement host = (HTMLStyleElement) node.getScriptObject();
        final CSSStyleSheet sheet = host.jsxGet_sheet();

        Selector selector = sheet.parseSelectors(new InputSource(new StringReader("#d\\:e"))).item(0);
        assertTrue(sheet.selects(selector, page.getHtmlElementById("d:e")));

        selector = sheet.parseSelectors(new InputSource(new StringReader("#d-e"))).item(0);
        assertTrue(sheet.selects(selector, page.getHtmlElementById("d-e")));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void selects_conditionalSelector_classCondition() throws Exception {
        testSelects("div.bar", false, true, false);
        testSelects(".bar", false, true, false);
        testSelects("div[class~=bar]", false, true, false);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void selects_pseudoClass_root() throws Exception {
        testSelects(":root", false, false, false);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void selects_pseudoClass_lang() throws Exception {
        if (getBrowserVersion().isFirefox()) {
            testSelects(":lang(en)", false, true, true);
            testSelects(":lang(de)", false, false, false);
        }
        else {
            testSelects(":lang(en)", false, false, false);
            testSelects(":lang(de)", false, false, false);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented
    public void selects_pseudoClass_negation() throws Exception {
        testSelects(":not(div)", true, false, true);
    }

    private void testSelects(final String css, final boolean selectBody, final boolean selectDivD,
        final boolean selectSpanS) throws Exception {
        final String html =
              "<html><body id='b'><style></style>\n"
            + "<div id='d' class='foo bar' lang='en-GB'>"
            + "<span>x</span>"
            + "<span id='s'>a</span>b</div>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlStyle node = (HtmlStyle) page.getElementsByTagName("style").item(0);
        final HTMLStyleElement host = (HTMLStyleElement) node.getScriptObject();
        final CSSStyleSheet sheet = host.jsxGet_sheet();
        final Selector selector = sheet.parseSelectors(new InputSource(new StringReader(css))).item(0);
        assertEquals(selectBody, sheet.selects(selector, page.getHtmlElementById("b")));
        assertEquals(selectDivD, sheet.selects(selector, page.getHtmlElementById("d")));
        assertEquals(selectSpanS, sheet.selects(selector, page.getHtmlElementById("s")));
    }

    /**
     * Test for 3325124.
     * @throws Exception if the test fails
     */
    @Test
    public void brokenExternalCSS() throws Exception {
        final String html = "<html><head>"
            + "<link rel='stylesheet' type='text/css' href='" + URL_SECOND + "'/></head></html>";

        getMockWebConnection().setResponse(URL_SECOND, "body { font-weight: 900\\9; }");
        final HtmlPage htmlPage = loadPage(html);

        final NodeList list = htmlPage.getElementsByTagName("body");
        final HtmlElement element = (HtmlElement) list.item(0);
        final ComputedCSSStyleDeclaration style = ((HTMLElement) element.getScriptObject()).jsxGet_currentStyle();
        assertEquals("CSSStyleDeclaration for ''", style.toString());
    }

}
