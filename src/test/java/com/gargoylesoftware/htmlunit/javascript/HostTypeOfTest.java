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

    private void testHostClassName(final String className) throws Exception {
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
        testHostClassName("ArrayBuffer");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.ArrayBufferView}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void arrayBufferView() throws Exception {
        testHostClassName("ArrayBufferView");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.ArrayBufferViewBase}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void arrayBufferViewBase() throws Exception {
        testHostClassName("ArrayBufferViewBase");
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
        testHostClassName("Attr");
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
        testHostClassName("ActiveXObject");
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
    @NotYetImplemented({ FF, CHROME })
    public void applicationCache() throws Exception {
        testHostClassName("ApplicationCache");
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
        testHostClassName("BeforeUnloadEvent");
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
        testHostClassName("BoxObject");
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
        testHostClassName("CDATASection");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.ClipboardData}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void clipboardData() throws Exception {
        testHostClassName("ClipboardData");
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
        testHostClassName("CSSCharsetRule");
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
        testHostClassName("CSSFontFaceRule");
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
        testHostClassName("CSSImportRule");
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
        testHostClassName("CSSMediaRule");
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
        testHostClassName("CSSPrimitiveValue");
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
        testHostClassName("CSSRule");
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
        testHostClassName("CSSRuleList");
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
        testHostClassName("CSSStyleDeclaration");
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
        testHostClassName("CSSStyleRule");
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
        testHostClassName("CSSStyleSheet");
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
        testHostClassName("CSSValue");
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
        testHostClassName("CanvasRenderingContext2D");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.CharacterDataImpl}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void characterDataImpl() throws Exception {
        testHostClassName("CharacterDataImpl");
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
        testHostClassName("ClientRect");
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
        testHostClassName("Comment");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void computedCSSStyleDeclaration() throws Exception {
        testHostClassName("ComputedCSSStyleDeclaration");
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
        testHostClassName("Console");
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
        testHostClassName("Coordinates");
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
        testHostClassName("DataView");
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
        testHostClassName("DOMException");
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
        testHostClassName("DOMImplementation");
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
        testHostClassName("DOMParser");
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
        testHostClassName("DOMStringMap");
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
        testHostClassName("DOMTokenList");
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
        testHostClassName("Document");
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
        testHostClassName("DocumentFragment");
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
        testHostClassName("DocumentType");
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
        testHostClassName("Element");
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
        testHostClassName("Enumerator");
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
        testHostClassName("Event");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.EventNode}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void eventNode() throws Exception {
        testHostClassName("EventNode");
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
        testHostClassName("External");
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
        testHostClassName("Float32Array");
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
        testHostClassName("Float64Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.FormChild}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void formChild() throws Exception {
        testHostClassName("FormChild");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.FormField}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void formField() throws Exception {
        testHostClassName("FormField");
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
        testHostClassName("Geolocation");
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
        testHostClassName("HashChangeEvent");
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
        testHostClassName("History");
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
        testHostClassName("HTMLAnchorElement");
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
        testHostClassName("HTMLAppletElement");
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
        testHostClassName("HTMLAreaElement");
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
        testHostClassName("HTMLAudioElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBGSoundElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "undefined",
            FF = "undefined",
            IE8 = "undefined")
    public void htmlBGSoundElement() throws Exception {
        testHostClassName("HTMLBGSoundElement");
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
        testHostClassName("HTMLBRElement");
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
        testHostClassName("HTMLBaseElement");
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
        testHostClassName("HTMLBaseFontElement");
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
        testHostClassName("HTMLBlockElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBlockQuoteElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void htmlBlockQuoteElement() throws Exception {
        testHostClassName("HTMLBlockQuoteElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "object",
            CHROME = "function",
            FF = "function",
            IE8 = "undefined")
    public void htmlBodyElement() throws Exception {
        testHostClassName("HTMLBodyElement");
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
        testHostClassName("HTMLButtonElement");
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
        testHostClassName("HTMLCanvasElement");
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
        testHostClassName("HTMLCollection");
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
        testHostClassName("HTMLAllCollection");
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
        testHostClassName("HTMLDataListElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDefinitionDescriptionElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void htmlDefinitionDescriptionElement() throws Exception {
        testHostClassName("HTMLDefinitionDescriptionElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDefinitionTermElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void htmlDefinitionTermElement() throws Exception {
        testHostClassName("HTMLDefinitionTermElement");
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
        testHostClassName("HTMLDListElement");
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
        testHostClassName("HTMLDirectoryElement");
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
    @NotYetImplemented(IE8)
    public void htmlDivElement() throws Exception {
        testHostClassName("HTMLDivElement");
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
        testHostClassName("HTMLDocument");
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
        testHostClassName("HTMLElement");
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
        testHostClassName("HTMLEmbedElement");
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
        testHostClassName("HTMLFieldSetElement");
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
        testHostClassName("HTMLFontElement");
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
        testHostClassName("HTMLFormElement");
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
        testHostClassName("HTMLFrameElement");
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
        testHostClassName("HTMLFrameSetElement");
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
        testHostClassName("HTMLHRElement");
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
        testHostClassName("HTMLHeadElement");
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
        testHostClassName("HTMLHeadingElement");
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
        testHostClassName("HTMLHtmlElement");
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
        testHostClassName("HTMLIFrameElement");
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
        testHostClassName("HTMLImageElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLInlineQuotationElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void htmlInlineQuotationElement() throws Exception {
        testHostClassName("HTMLInlineQuotationElement");
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
        testHostClassName("HTMLInputElement");
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
        testHostClassName("HTMLIsIndexElement");
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
        testHostClassName("HTMLKeygenElement");
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
        testHostClassName("HTMLLIElement");
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
        testHostClassName("HTMLLabelElement");
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
        testHostClassName("HTMLLegendElement");
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
        testHostClassName("HTMLLinkElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLListElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void htmlListElement() throws Exception {
        testHostClassName("HTMLListElement");
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
        testHostClassName("HTMLMapElement");
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
        testHostClassName("HTMLMarqueeElement");
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
        testHostClassName("HTMLMediaElement");
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
        testHostClassName("HTMLMenuElement");
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
        testHostClassName("HTMLMetaElement");
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
        testHostClassName("HTMLMeterElement");
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
        testHostClassName("HTMLModElement");
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
        testHostClassName("HTMLNoShowElement");
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
        testHostClassName("HTMLNextIdElement");
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
        testHostClassName("HTMLOListElement");
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
        testHostClassName("HTMLObjectElement");
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
        testHostClassName("HTMLOptGroupElement");
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
        testHostClassName("HTMLOptionElement");
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
        testHostClassName("HTMLOptionsCollection");
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
        testHostClassName("HTMLOutputElement");
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
        testHostClassName("HTMLParagraphElement");
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
        testHostClassName("HTMLParamElement");
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
        testHostClassName("HTMLPhraseElement");
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
        testHostClassName("HTMLPreElement");
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
        testHostClassName("HTMLProgressElement");
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
        testHostClassName("HTMLScriptElement");
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
        testHostClassName("HTMLSelectElement");
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
        testHostClassName("HTMLSourceElement");
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
        testHostClassName("HTMLSpanElement");
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
        testHostClassName("HTMLStyleElement");
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
        testHostClassName("HTMLTableCaptionElement");
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
        testHostClassName("HTMLTableCellElement");
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
        testHostClassName("HTMLTableColElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableComponent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void htmlTableComponent() throws Exception {
        testHostClassName("HTMLTableComponent");
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
        testHostClassName("HTMLTableDataCellElement");
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
        testHostClassName("HTMLTableElement");
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
        testHostClassName("HTMLTableHeaderCellElement");
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
        testHostClassName("HTMLTableRowElement");
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
        testHostClassName("HTMLTableSectionElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTextElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "undefined")
    public void htmlTextElement() throws Exception {
        testHostClassName("HTMLTextElement");
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
        testHostClassName("HTMLTextAreaElement");
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
        testHostClassName("HTMLTimeElement");
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
        testHostClassName("HTMLTitleElement");
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
        testHostClassName("HTMLUListElement");
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
        testHostClassName("HTMLUnknownElement");
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
        testHostClassName("HTMLWBRElement");
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
        testHostClassName("HTMLVideoElement");
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
        testHostClassName("Int16Array");
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
        testHostClassName("Int32Array");
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
        testHostClassName("Int8Array");
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
        testHostClassName("KeyboardEvent");
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
        testHostClassName("Location");
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
        testHostClassName("MediaList");
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
        testHostClassName("MessageEvent");
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
        testHostClassName("MimeType");
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
        testHostClassName("MimeTypeArray");
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
        testHostClassName("MouseEvent");
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
        testHostClassName("MutationEvent");
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
        testHostClassName("NamedNodeMap");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Namespace}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void namespace() throws Exception {
        testHostClassName("Namespace");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.NamespaceCollection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void namespaceCollection() throws Exception {
        testHostClassName("NamespaceCollection");
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
        testHostClassName("Navigator");
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
        testHostClassName("Node");
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
        testHostClassName("NodeFilter");
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
        testHostClassName("NodeList");
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
        testHostClassName("Plugin");
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
        testHostClassName("PluginArray");
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
        testHostClassName("PointerEvent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Popup}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void popup() throws Exception {
        testHostClassName("Popup");
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
        testHostClassName("Position");
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
        testHostClassName("ProcessingInstruction");
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
        testHostClassName("Range");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.RowContainer}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void rowContainer() throws Exception {
        testHostClassName("RowContainer");
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
        testHostClassName("SVGAElement");
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
        testHostClassName("SVGAltGlyphElement");
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
        testHostClassName("SVGAngle");
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
        testHostClassName("SVGAnimateElement");
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
        testHostClassName("SVGAnimateMotionElement");
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
        testHostClassName("SVGAnimateTransformElement");
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
        testHostClassName("SVGCircleElement");
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
        testHostClassName("SVGClipPathElement");
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
        testHostClassName("SVGCursorElement");
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
        testHostClassName("SVGDefsElement");
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
        testHostClassName("SVGDescElement");
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
        testHostClassName("SVGElement");
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
        testHostClassName("SVGEllipseElement");
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
        testHostClassName("SVGFEBlendElement");
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
        testHostClassName("SVGFEColorMatrixElement");
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
        testHostClassName("SVGFEComponentTransferElement");
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
        testHostClassName("SVGFECompositeElement");
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
        testHostClassName("SVGFEConvolveMatrixElement");
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
        testHostClassName("SVGFEDiffuseLightingElement");
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
        testHostClassName("SVGFEDisplacementMapElement");
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
        testHostClassName("SVGFEDistantLightElement");
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
        testHostClassName("SVGFEFloodElement");
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
        testHostClassName("SVGFEFuncAElement");
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
        testHostClassName("SVGFEFuncBElement");
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
        testHostClassName("SVGFEFuncGElement");
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
        testHostClassName("SVGFEFuncRElement");
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
        testHostClassName("SVGFEGaussianBlurElement");
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
        testHostClassName("SVGFEImageElement");
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
        testHostClassName("SVGFEMergeElement");
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
        testHostClassName("SVGFEMergeNodeElement");
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
        testHostClassName("SVGFEMorphologyElement");
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
        testHostClassName("SVGFEOffsetElement");
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
        testHostClassName("SVGFEPointLightElement");
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
        testHostClassName("SVGFESpecularLightingElement");
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
        testHostClassName("SVGFESpotLightElement");
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
        testHostClassName("SVGFETileElement");
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
        testHostClassName("SVGFETurbulenceElement");
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
        testHostClassName("SVGFilterElement");
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
        testHostClassName("SVGForeignObjectElement");
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
        testHostClassName("SVGGElement");
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
        testHostClassName("SVGImageElement");
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
        testHostClassName("SVGLineElement");
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
        testHostClassName("SVGLinearGradientElement");
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
        testHostClassName("SVGMarkerElement");
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
        testHostClassName("SVGMaskElement");
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
        testHostClassName("SVGMatrix");
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
        testHostClassName("SVGMetadataElement");
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
        testHostClassName("SVGMPathElement");
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
        testHostClassName("SVGPathElement");
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
        testHostClassName("SVGPatternElement");
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
        testHostClassName("SVGPolygonElement");
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
        testHostClassName("SVGPolylineElement");
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
        testHostClassName("SVGRadialGradientElement");
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
        testHostClassName("SVGRect");
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
        testHostClassName("SVGRectElement");
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
        testHostClassName("SVGSVGElement");
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
        testHostClassName("SVGScriptElement");
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
        testHostClassName("SVGSetElement");
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
        testHostClassName("SVGStopElement");
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
        testHostClassName("SVGStyleElement");
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
        testHostClassName("SVGSwitchElement");
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
        testHostClassName("SVGSymbolElement");
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
        testHostClassName("SVGTSpanElement");
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
        testHostClassName("SVGTextElement");
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
        testHostClassName("SVGTextPathElement");
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
        testHostClassName("SVGTitleElement");
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
        testHostClassName("SVGUseElement");
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
        testHostClassName("SVGViewElement");
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
        testHostClassName("Screen");
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
        testHostClassName("Selection");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.SimpleArray}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void simpleArray() throws Exception {
        testHostClassName("SimpleArray");
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
        testHostClassName("StaticNodeList");
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
        testHostClassName("Storage");
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
        testHostClassName("StyleSheetList");
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
        testHostClassName("Text");
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
        testHostClassName("TextRange");
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
        testHostClassName("TreeWalker");
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
        testHostClassName("UIEvent");
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
        testHostClassName("Uint16Array");
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
        testHostClassName("Uint32Array");
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
        testHostClassName("Uint8Array");
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
        testHostClassName("Uint8ClampedArray");
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
        testHostClassName("WebSocket");
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
        testHostClassName("Window");
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
        testHostClassName("XMLDocument");
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
        testHostClassName("XMLHttpRequest");
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
        testHostClassName("XMLSerializer");
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
        testHostClassName("XPathEvaluator");
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
        testHostClassName("XPathNSResolver");
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
        testHostClassName("XPathResult");
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
        testHostClassName("XSLTProcessor");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.XSLTemplate}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("undefined")
    public void xsltemplate() throws Exception {
        testHostClassName("XSLTemplate");
    }
}
