/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF24;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF31;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE11;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests that <code>typeof</code> host class is correct.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HostTypeOfTest extends WebDriverTestCase {

    private void test(final String className) throws Exception {
        final String html =
            "<html><head><script>\n"
            + "  function test() {\n"
            + "    alert(typeof " + className + ");\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.ArrayBuffer}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function",
            IE8 = "undefined")
    public void arrayBuffer() throws Exception {
        test("ArrayBuffer");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.ArrayBufferView}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void arrayBufferView() throws Exception {
        test("ArrayBufferView");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.ArrayBufferViewBase}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void arrayBufferViewBase() throws Exception {
        test("ArrayBufferViewBase");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Attr}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void attr() throws Exception {
        test("Attr");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.ActiveXObject}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE8 = "function")
    public void activeXObject() throws Exception {
        test("ActiveXObject");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.ApplicationCache}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "undefined",
            IE8 = "undefined")
    public void applicationCache() throws Exception {
        test("ApplicationCache");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.ApplicationCache}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            FF = "function")
    public void offlineResourceList() throws Exception {
        test("OfflineResourceList");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.BeforeUnloadEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void beforeUnloadEvent() throws Exception {
        test("BeforeUnloadEvent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.BoxObject}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            FF24 = "object")
    public void boxObject() throws Exception {
        test("BoxObject");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.CDATASection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void cdataSection() throws Exception {
        test("CDATASection");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.ClipboardData}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void clipboardData() throws Exception {
        test("ClipboardData");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSCharsetRule}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            IE = "undefined")
    public void cssCharsetRule() throws Exception {
        test("CSSCharsetRule");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSFontFaceRule}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            IE8 = "undefined")
    public void cssFontFaceRule() throws Exception {
        test("CSSFontFaceRule");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSImportRule}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            IE8 = "undefined")
    public void cssImportRule() throws Exception {
        test("CSSImportRule");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSMediaRule}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            IE8 = "undefined")
    public void cssMediaRule() throws Exception {
        test("CSSMediaRule");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSPrimitiveValue}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function",
            CHROME = "undefined",
            IE = "undefined")
    @NotYetImplemented(CHROME)
    public void cssPrimitiveValue() throws Exception {
        test("CSSPrimitiveValue");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSRule}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            IE8 = "undefined")
    @NotYetImplemented(CHROME)
    public void cssRule() throws Exception {
        test("CSSRule");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSRuleList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            IE8 = "undefined")
    public void cssRuleList() throws Exception {
        test("CSSRuleList");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void cssStyleDeclaration() throws Exception {
        test("CSSStyleDeclaration");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            FF = "function")
    @NotYetImplemented(FF)
    public void css2Properties() throws Exception {
        test("CSS2Properties");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleRule}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            IE8 = "undefined")
    public void cssStyleRule() throws Exception {
        test("CSSStyleRule");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void cssStyleSheet() throws Exception {
        test("CSSStyleSheet");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSValue}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            FF = "function")
    @NotYetImplemented(CHROME)
    public void cssValue() throws Exception {
        test("CSSValue");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.canvas.CanvasRenderingContext2D}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void canvasRenderingContext2D() throws Exception {
        test("CanvasRenderingContext2D");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.CharacterDataImpl}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void characterDataImpl() throws Exception {
        test("CharacterDataImpl");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.ClientRect}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF24 = "function",
            FF31 = "undefined",
            IE8 = "undefined")
    @NotYetImplemented(FF24)
    public void clientRect() throws Exception {
        test("ClientRect");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Comment}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void comment() throws Exception {
        test("Comment");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Comment}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void htmlCommentElement() throws Exception {
        test("HTMLCommentElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void computedCSSStyleDeclaration() throws Exception {
        test("ComputedCSSStyleDeclaration");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Console}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "undefined",
            FF = "undefined",
            IE8 = "undefined")
    @NotYetImplemented({ FF, CHROME })
    public void console() throws Exception {
        test("Console");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.geo.Coordinates}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "undefined",
            FF = "undefined",
            IE8 = "undefined")
    public void coordinates() throws Exception {
        test("Coordinates");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.DataView}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function",
            IE8 = "undefined")
    public void dataView() throws Exception {
        test("DataView");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.DOMException}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF31 = "function",
            IE8 = "undefined")
    @NotYetImplemented(FF24)
    public void domException() throws Exception {
        test("DOMException");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.DOMImplementation}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void domImplementation() throws Exception {
        test("DOMImplementation");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.DOMParser}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function",
            IE8 = "undefined")
    public void domParser() throws Exception {
        test("DOMParser");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.DOMStringMap}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void domStringMap() throws Exception {
        test("DOMStringMap");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.DOMTokenList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void domTokenList() throws Exception {
        test("DOMTokenList");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Document}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void document() throws Exception {
        test("Document");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.DocumentFragment}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void documentFragment() throws Exception {
        test("DocumentFragment");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.DocumentType}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void documentType() throws Exception {
        test("DocumentType");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Element}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void element() throws Exception {
        test("Element");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Enumerator}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE = "function")
    public void enumerator() throws Exception {
        test("Enumerator");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Event}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void event() throws Exception {
        test("Event");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.EventNode}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void eventNode() throws Exception {
        test("EventNode");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.External}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            FF31 = "function")
    @NotYetImplemented(FF31)
    public void external() throws Exception {
        test("External");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Float32Array}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function",
            IE8 = "undefined")
    public void float32Array() throws Exception {
        test("Float32Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Float64Array}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function",
            IE8 = "undefined")
    public void float64Array() throws Exception {
        test("Float64Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.FormChild}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void formChild() throws Exception {
        test("FormChild");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.FormField}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void formField() throws Exception {
        test("FormField");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.geo.Geolocation}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "undefined",
            FF = "undefined",
            IE8 = "undefined")
    public void geolocation() throws Exception {
        test("Geolocation");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.HashChangeEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            CHROME = "function",
            FF = "function")
    public void hashChangeEvent() throws Exception {
        test("HashChangeEvent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.History}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF31 = "function",
            IE8 = "undefined")
    @NotYetImplemented(FF24)
    public void history() throws Exception {
        test("History");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAnchorElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlAnchorElement() throws Exception {
        test("HTMLAnchorElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAppletElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlAppletElement() throws Exception {
        test("HTMLAppletElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAreaElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlAreaElement() throws Exception {
        test("HTMLAreaElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAudioElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlAudioElement() throws Exception {
        test("HTMLAudioElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBGSoundElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE11 = "object")
    public void htmlBGSoundElement() throws Exception {
        test("HTMLBGSoundElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBRElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlBRElement() throws Exception {
        test("HTMLBRElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBaseElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlBaseElement() throws Exception {
        test("HTMLBaseElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBaseFontElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "undefined",
            FF = "undefined",
            IE8 = "undefined")
    public void htmlBaseFontElement() throws Exception {
        test("HTMLBaseFontElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBlockElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "undefined",
            FF = "undefined",
            IE8 = "undefined")
    @NotYetImplemented({ IE8, FF, CHROME })
    public void htmlBlockElement() throws Exception {
        test("HTMLBlockElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBlockQuoteElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void htmlBlockQuoteElement() throws Exception {
        test("HTMLBlockQuoteElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBlockQuoteElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function",
        IE8 = "undefined",
        IE11 = "object")
    @NotYetImplemented({ CHROME, FF })
    public void htmlQuoteElement() throws Exception {
        test("HTMLQuoteElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBlockQuoteElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlBodyElement() throws Exception {
        test("HTMLBodyElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLButtonElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlButtonElement() throws Exception {
        test("HTMLButtonElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCanvasElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlCanvasElement() throws Exception {
        test("HTMLCanvasElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlCollection() throws Exception {
        test("HTMLCollection");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAllCollection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "undefined",
            IE8 = "undefined")
    public void htmlAllCollection() throws Exception {
        test("HTMLAllCollection");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDataListElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlDataListElement() throws Exception {
        test("HTMLDataListElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDDElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void htmlDefinitionDescriptionElement() throws Exception {
        test("HTMLDefinitionDescriptionElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDDElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE11 = "object")
    public void htmlDDElement() throws Exception {
        test("HTMLDDElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDTElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void htmlDefinitionTermElement() throws Exception {
        test("HTMLDefinitionTermElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDTElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE11 = "object")
    public void htmlDTElement() throws Exception {
        test("HTMLDTElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDListElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlDListElement() throws Exception {
        test("HTMLDListElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDirectoryElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlDirectoryElement() throws Exception {
        test("HTMLDirectoryElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDivElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlDivElement() throws Exception {
        test("HTMLDivElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlDocument() throws Exception {
        test("HTMLDocument");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlElement() throws Exception {
        test("HTMLElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLEmbedElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlEmbedElement() throws Exception {
        test("HTMLEmbedElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFieldSetElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlFieldSetElement() throws Exception {
        test("HTMLFieldSetElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFontElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlFontElement() throws Exception {
        test("HTMLFontElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFormElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlFormElement() throws Exception {
        test("HTMLFormElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFrameElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlFrameElement() throws Exception {
        test("HTMLFrameElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFrameSetElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlFrameSetElement() throws Exception {
        test("HTMLFrameSetElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHRElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlHRElement() throws Exception {
        test("HTMLHRElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHeadElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlHeadElement() throws Exception {
        test("HTMLHeadElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHeadingElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    @NotYetImplemented(IE8)
    public void htmlHeadingElement() throws Exception {
        test("HTMLHeadingElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHtmlElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlHtmlElement() throws Exception {
        test("HTMLHtmlElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLIFrameElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlIFrameElement() throws Exception {
        test("HTMLIFrameElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLImageElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    @NotYetImplemented(IE)
    public void htmlImageElement() throws Exception {
        test("HTMLImageElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLInlineQuotationElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void htmlInlineQuotationElement() throws Exception {
        test("HTMLInlineQuotationElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLInputElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlInputElement() throws Exception {
        test("HTMLInputElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLIsIndexElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "undefined",
            FF = "undefined",
            IE8 = "undefined")
    public void htmlIsIndexElement() throws Exception {
        test("HTMLIsIndexElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLKeygenElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            CHROME = "function")
    @NotYetImplemented(CHROME)
    public void htmlKeygenElement() throws Exception {
        test("HTMLKeygenElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLIElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlLIElement() throws Exception {
        test("HTMLLIElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLabelElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlLabelElement() throws Exception {
        test("HTMLLabelElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLegendElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlLegendElement() throws Exception {
        test("HTMLLegendElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLinkElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlLinkElement() throws Exception {
        test("HTMLLinkElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLListElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void htmlListElement() throws Exception {
        test("HTMLListElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMapElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlMapElement() throws Exception {
        test("HTMLMapElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMarqueeElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "undefined",
            IE8 = "undefined")
    @NotYetImplemented(CHROME)
    public void htmlMarqueeElement() throws Exception {
        test("HTMLMarqueeElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMediaElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlMediaElement() throws Exception {
        test("HTMLMediaElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMenuElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    @NotYetImplemented(CHROME)
    public void htmlMenuElement() throws Exception {
        test("HTMLMenuElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMetaElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlMetaElement() throws Exception {
        test("HTMLMetaElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMeterElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            CHROME = "function",
            FF = "function")
    public void htmlMeterElement() throws Exception {
        test("HTMLMeterElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLModElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlModElement() throws Exception {
        test("HTMLModElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLNoShowElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE8 = "undefined")
    public void htmlNoShowElement() throws Exception {
        test("HTMLNoShowElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLNextIdElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "undefined",
            FF = "undefined",
            IE8 = "undefined")
    public void htmlNextIdElement() throws Exception {
        test("HTMLNextIdElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOListElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlOListElement() throws Exception {
        test("HTMLOListElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLObjectElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlObjectElement() throws Exception {
        test("HTMLObjectElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOptGroupElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlOptGroupElement() throws Exception {
        test("HTMLOptGroupElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOptionElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    @NotYetImplemented(IE)
    public void htmlOptionElement() throws Exception {
        test("HTMLOptionElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOptionsCollection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            CHROME = "function",
            FF = "function")
    public void htmlOptionsCollection() throws Exception {
        test("HTMLOptionsCollection");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOutputElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            CHROME = "function",
            FF = "function")
    public void htmlOutputElement() throws Exception {
        test("HTMLOutputElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLParagraphElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlParagraphElement() throws Exception {
        test("HTMLParagraphElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLParamElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlParamElement() throws Exception {
        test("HTMLParamElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLPhraseElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "undefined",
            FF = "undefined",
            IE8 = "undefined")
    @NotYetImplemented({ FF, CHROME, IE8 })
    public void htmlPhraseElement() throws Exception {
        test("HTMLPhraseElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLPreElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlPreElement() throws Exception {
        test("HTMLPreElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLProgressElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlProgressElement() throws Exception {
        test("HTMLProgressElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLScriptElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlScriptElement() throws Exception {
        test("HTMLScriptElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSelectElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlSelectElement() throws Exception {
        test("HTMLSelectElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSourceElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlSourceElement() throws Exception {
        test("HTMLSourceElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSpanElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    @NotYetImplemented({ FF, IE8 })
    public void htmlSpanElement() throws Exception {
        test("HTMLSpanElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLStyleElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlStyleElement() throws Exception {
        test("HTMLStyleElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableCaptionElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlTableCaptionElement() throws Exception {
        test("HTMLTableCaptionElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableCellElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlTableCellElement() throws Exception {
        test("HTMLTableCellElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableColElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlTableColElement() throws Exception {
        test("HTMLTableColElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableComponent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void htmlTableComponent() throws Exception {
        test("HTMLTableComponent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableDataCellElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "undefined",
            FF = "undefined",
            IE8 = "undefined")
    public void htmlTableDataCellElement() throws Exception {
        test("HTMLTableDataCellElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlTableElement() throws Exception {
        test("HTMLTableElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableHeaderCellElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "undefined",
            FF = "undefined",
            IE8 = "undefined")
    public void htmlTableHeaderCellElement() throws Exception {
        test("HTMLTableHeaderCellElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableRowElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlTableRowElement() throws Exception {
        test("HTMLTableRowElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableSectionElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlTableSectionElement() throws Exception {
        test("HTMLTableSectionElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTextElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined")
    public void htmlTextElement() throws Exception {
        test("HTMLTextElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTextAreaElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlTextAreaElement() throws Exception {
        test("HTMLTextAreaElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTimeElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            FF = "function")
    @NotYetImplemented(FF)
    public void htmlTimeElement() throws Exception {
        test("HTMLTimeElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTitleElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlTitleElement() throws Exception {
        test("HTMLTitleElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLUListElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    @NotYetImplemented(IE8)
    public void htmlUListElement() throws Exception {
        test("HTMLUListElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLUnknownElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlUnknownElement() throws Exception {
        test("HTMLUnknownElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLUnknownElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void htmlGenericElement() throws Exception {
        test("HTMLGenericElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLWBRElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    @NotYetImplemented(CHROME)
    public void htmlWBRElement() throws Exception {
        test("HTMLWBRElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLVideoElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlVideoElement() throws Exception {
        test("HTMLVideoElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Int16Array}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function",
            IE8 = "undefined")
    public void int16Array() throws Exception {
        test("Int16Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Int32Array}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function",
            IE8 = "undefined")
    public void int32Array() throws Exception {
        test("Int32Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Int8Array}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function",
            IE8 = "undefined")
    public void int8Array() throws Exception {
        test("Int8Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.KeyboardEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void keyboardEvent() throws Exception {
        test("KeyboardEvent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Location}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            IE8 = "undefined")
    @NotYetImplemented(IE8)
    public void location() throws Exception {
        test("Location");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.MediaList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF31 = "function",
            IE8 = "undefined")
    @NotYetImplemented(FF24)
    public void mediaList() throws Exception {
        test("MediaList");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.MessageEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void messageEvent() throws Exception {
        test("MessageEvent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.MimeType}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF31 = "function",
            IE8 = "undefined")
    @NotYetImplemented({ FF24, IE11 })
    public void mimeType() throws Exception {
        test("MimeType");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.MimeTypeArray}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF31 = "function",
            IE8 = "undefined")
    @NotYetImplemented(FF24)
    public void mimeTypeArray() throws Exception {
        test("MimeTypeArray");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.MouseEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void mouseEvent() throws Exception {
        test("MouseEvent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.MutationEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void mutationEvent() throws Exception {
        test("MutationEvent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.NamedNodeMap}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "undefined",
            IE8 = "undefined")
    @NotYetImplemented(FF)
    public void namedNodeMap() throws Exception {
        test("NamedNodeMap");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Namespace}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void namespace() throws Exception {
        test("Namespace");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.NamespaceCollection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void namespaceCollection() throws Exception {
        test("NamespaceCollection");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Navigator}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF31 = "function",
            IE8 = "undefined")
    @NotYetImplemented(FF24)
    public void navigator() throws Exception {
        test("Navigator");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Node}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void node() throws Exception {
        test("Node");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.NodeFilter}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void nodeFilter() throws Exception {
        test("NodeFilter");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.NodeList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void nodeList() throws Exception {
        test("NodeList");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Plugin}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF31 = "function",
            IE8 = "undefined")
    @NotYetImplemented({ FF24, IE11 })
    public void plugin() throws Exception {
        test("Plugin");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.PluginArray}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF31 = "function",
            IE8 = "undefined")
    @NotYetImplemented(FF24)
    public void pluginArray() throws Exception {
        test("PluginArray");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.PointerEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "undefined",
            FF = "undefined",
            IE8 = "undefined")
    public void pointerEvent() throws Exception {
        test("PointerEvent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Popup}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void popup() throws Exception {
        test("Popup");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.geo.Position}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "undefined",
            FF = "undefined",
            IE8 = "undefined")
    public void position() throws Exception {
        test("Position");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.ProcessingInstruction}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void processingInstruction() throws Exception {
        test("ProcessingInstruction");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Range}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void range() throws Exception {
        test("Range");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.RowContainer}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void rowContainer() throws Exception {
        test("RowContainer");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgAElement() throws Exception {
        test("SVGAElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAltGlyphElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            FF = "function")
    public void svgAltGlyphElement() throws Exception {
        test("SVGAltGlyphElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAngle}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgAngle() throws Exception {
        test("SVGAngle");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimateElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            CHROME = "function",
            FF = "function")
    public void svgAnimateElement() throws Exception {
        test("SVGAnimateElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimateMotionElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            CHROME = "function",
            FF = "function")
    public void svgAnimateMotionElement() throws Exception {
        test("SVGAnimateMotionElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimateTransformElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            CHROME = "function",
            FF = "function")
    public void svgAnimateTransformElement() throws Exception {
        test("SVGAnimateTransformElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGCircleElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgCircleElement() throws Exception {
        test("SVGCircleElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGClipPathElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgClipPathElement() throws Exception {
        test("SVGClipPathElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGCursorElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            CHROME = "function")
    public void svgCursorElement() throws Exception {
        test("SVGCursorElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGDefsElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgDefsElement() throws Exception {
        test("SVGDefsElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGDescElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgDescElement() throws Exception {
        test("SVGDescElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgElement() throws Exception {
        test("SVGElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGEllipseElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgEllipseElement() throws Exception {
        test("SVGEllipseElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEBlendElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFEBlendElement() throws Exception {
        test("SVGFEBlendElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEColorMatrixElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFEColorMatrixElement() throws Exception {
        test("SVGFEColorMatrixElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEComponentTransferElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFEComponentTransferElement() throws Exception {
        test("SVGFEComponentTransferElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFECompositeElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFECompositeElement() throws Exception {
        test("SVGFECompositeElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEConvolveMatrixElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFEConvolveMatrixElement() throws Exception {
        test("SVGFEConvolveMatrixElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEDiffuseLightingElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFEDiffuseLightingElement() throws Exception {
        test("SVGFEDiffuseLightingElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEDisplacementMapElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFEDisplacementMapElement() throws Exception {
        test("SVGFEDisplacementMapElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEDistantLightElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFEDistantLightElement() throws Exception {
        test("SVGFEDistantLightElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFloodElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFEFloodElement() throws Exception {
        test("SVGFEFloodElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFuncAElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFEFuncAElement() throws Exception {
        test("SVGFEFuncAElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFuncBElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFEFuncBElement() throws Exception {
        test("SVGFEFuncBElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFuncGElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFEFuncGElement() throws Exception {
        test("SVGFEFuncGElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFuncRElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFEFuncRElement() throws Exception {
        test("SVGFEFuncRElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEGaussianBlurElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFEGaussianBlurElement() throws Exception {
        test("SVGFEGaussianBlurElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEImageElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFEImageElement() throws Exception {
        test("SVGFEImageElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEMergeElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFEMergeElement() throws Exception {
        test("SVGFEMergeElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEMergeNodeElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFEMergeNodeElement() throws Exception {
        test("SVGFEMergeNodeElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEMorphologyElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFEMorphologyElement() throws Exception {
        test("SVGFEMorphologyElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEOffsetElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFEOffsetElement() throws Exception {
        test("SVGFEOffsetElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEPointLightElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFEPointLightElement() throws Exception {
        test("SVGFEPointLightElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFESpecularLightingElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFESpecularLightingElement() throws Exception {
        test("SVGFESpecularLightingElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFESpotLightElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFESpotLightElement() throws Exception {
        test("SVGFESpotLightElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFETileElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFETileElement() throws Exception {
        test("SVGFETileElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFETurbulenceElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFETurbulenceElement() throws Exception {
        test("SVGFETurbulenceElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFilterElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgFilterElement() throws Exception {
        test("SVGFilterElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGForeignObjectElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            CHROME = "function",
            FF = "function")
    public void svgForeignObjectElement() throws Exception {
        test("SVGForeignObjectElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGGElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgGElement() throws Exception {
        test("SVGGElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGImageElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgImageElement() throws Exception {
        test("SVGImageElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGLineElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgLineElement() throws Exception {
        test("SVGLineElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGLinearGradientElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgLinearGradientElement() throws Exception {
        test("SVGLinearGradientElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMarkerElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgMarkerElement() throws Exception {
        test("SVGMarkerElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMaskElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgMaskElement() throws Exception {
        test("SVGMaskElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMatrix}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgMatrix() throws Exception {
        test("SVGMatrix");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMetadataElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgMetadataElement() throws Exception {
        test("SVGMetadataElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMPathElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            CHROME = "function",
            FF = "function")
    public void svgMPathElement() throws Exception {
        test("SVGMPathElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgPathElement() throws Exception {
        test("SVGPathElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPatternElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgPatternElement() throws Exception {
        test("SVGPatternElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPolygonElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgPolygonElement() throws Exception {
        test("SVGPolygonElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPolylineElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgPolylineElement() throws Exception {
        test("SVGPolylineElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGRadialGradientElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgRadialGradientElement() throws Exception {
        test("SVGRadialGradientElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGRect}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgRect() throws Exception {
        test("SVGRect");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGRectElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgRectElement() throws Exception {
        test("SVGRectElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGSVGElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgSVGElement() throws Exception {
        test("SVGSVGElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGScriptElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgScriptElement() throws Exception {
        test("SVGScriptElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGSetElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            CHROME = "function",
            FF = "function")
    public void svgSetElement() throws Exception {
        test("SVGSetElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGStopElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgStopElement() throws Exception {
        test("SVGStopElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGStyleElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgStyleElement() throws Exception {
        test("SVGStyleElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGSwitchElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgSwitchElement() throws Exception {
        test("SVGSwitchElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGSymbolElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgSymbolElement() throws Exception {
        test("SVGSymbolElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTSpanElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgTSpanElement() throws Exception {
        test("SVGTSpanElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTextElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgTextElement() throws Exception {
        test("SVGTextElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTextPathElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgTextPathElement() throws Exception {
        test("SVGTextPathElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTitleElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgTitleElement() throws Exception {
        test("SVGTitleElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGUseElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgUseElement() throws Exception {
        test("SVGUseElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGViewElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void svgViewElement() throws Exception {
        test("SVGViewElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Screen}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void screen() throws Exception {
        test("Screen");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Selection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF31 = "function",
            IE8 = "undefined")
    @NotYetImplemented(FF24)
    public void selection() throws Exception {
        test("Selection");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.SimpleArray}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void simpleArray() throws Exception {
        test("SimpleArray");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.StaticNodeList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined")
    @NotYetImplemented({ FF, CHROME, IE11 })
    public void staticNodeList() throws Exception {
        test("StaticNodeList");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Storage}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            IE8 = "undefined")
    public void storage() throws Exception {
        test("Storage");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.StyleSheetList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF31 = "function",
            IE8 = "undefined")
    @NotYetImplemented(FF24)
    public void styleSheetList() throws Exception {
        test("StyleSheetList");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Text}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void text() throws Exception {
        test("Text");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.TextRange}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "undefined",
            FF = "undefined",
            IE8 = "undefined")
    public void textRange() throws Exception {
        test("TextRange");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.TreeWalker}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void treeWalker() throws Exception {
        test("TreeWalker");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.UIEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void uIEvent() throws Exception {
        test("UIEvent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Uint16Array}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function",
            IE8 = "undefined")
    public void uint16Array() throws Exception {
        test("Uint16Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Uint32Array}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function",
            IE8 = "undefined")
    public void uint32Array() throws Exception {
        test("Uint32Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Uint8Array}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function",
            IE8 = "undefined")
    public void uint8Array() throws Exception {
        test("Uint8Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Uint8ClampedArray}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function",
            IE8 = "undefined")
    public void uint8ClampedArray() throws Exception {
        test("Uint8ClampedArray");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.WebSocket}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function",
            IE8 = "undefined")
    @NotYetImplemented(IE11)
    public void webSocket() throws Exception {
        test("WebSocket");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Window}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            IE8 = "undefined")
    public void window() throws Exception {
        test("Window");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void xmlDocument() throws Exception {
        test("XMLDocument");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.xml.XMLHttpRequest}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function",
            IE8 = "object")
    public void xmlHttpRequest() throws Exception {
        test("XMLHttpRequest");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.xml.XMLSerializer}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function",
            IE8 = "undefined")
    public void xmlSerializer() throws Exception {
        test("XMLSerializer");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.XPathEvaluator}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            CHROME = "function",
            FF = "function")
    public void xPathEvaluator() throws Exception {
        test("XPathEvaluator");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.XPathNSResolver}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            FF = "object")
    @NotYetImplemented(CHROME)
    public void xPathNSResolver() throws Exception {
        test("XPathNSResolver");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.XPathResult}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            IE = "undefined")
    public void xPathResult() throws Exception {
        test("XPathResult");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.XSLTProcessor}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function",
            IE = "undefined")
    @NotYetImplemented(IE11)
    public void xsltProcessor() throws Exception {
        test("XSLTProcessor");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.XSLTemplate}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void xsltemplate() throws Exception {
        test("XSLTemplate");
    }

    /**
     * Test {@link net.sourceforge.htmlunit.corejs.javascript.NativeIterator.StopIteration}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            FF = "object")
    public void stopIteration() throws Exception {
        test("StopIteration");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.ClientRect}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            FF = "function")
    @NotYetImplemented(FF)
    public void domRect() throws Exception {
        test("DOMRect");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE11 = "object")
    @NotYetImplemented(IE11)
    public void msStyleCSSProperties() throws Exception {
        test("MSStyleCSSProperties");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE11 = "object")
    @NotYetImplemented(IE11)
    public void msCurrentStyleCSSProperties() throws Exception {
        test("MSCurrentStyleCSSProperties");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function")
    public void error() throws Exception {
        test("Error");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            IE11 = "object",
            CHROME = "function")
    @NotYetImplemented({ CHROME, IE11 })
    public void clientRectList() throws Exception {
        test("ClientRectList");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined",
            FF = "function")
    @NotYetImplemented(FF)
    public void svgDocument() throws Exception {
        test("SVGDocument");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function")
    public void object() throws Exception {
        test("Object");
    }

    /**
     * Test {@link net.sourceforge.htmlunit.corejs.javascript.Arguments}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("object")
    public void arguments() throws Exception {
        test("arguments");
    }
}
