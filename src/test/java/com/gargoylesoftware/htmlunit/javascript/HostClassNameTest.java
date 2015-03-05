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
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE11;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests the host class names match the Firefox (w3c names).
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API">Web API Interfaces</a>
 * @see HostClassNameStandardsTest
 */
@RunWith(BrowserRunner.class)
public class HostClassNameTest extends WebDriverTestCase {

    private void test(final String className) throws Exception {
        final String html =
            "<html><head><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(" + className + ");\n"
            + "    } catch(e) {alert('exception')}\n"
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
    @Alerts(DEFAULT = "function ArrayBuffer() {\n    [native code]\n}",
            CHROME = "function ArrayBuffer() { [native code] }",
            IE11 = "\nfunction ArrayBuffer() {\n    [native code]\n}\n",
            IE8 = "exception")
    public void arrayBuffer() throws Exception {
        test("ArrayBuffer");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.ArrayBufferView}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void arrayBufferView() throws Exception {
        test("ArrayBufferView");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.ArrayBufferViewBase}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void arrayBufferViewBase() throws Exception {
        test("ArrayBufferViewBase");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Attr}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object Attr]",
            CHROME = "function Attr() { [native code] }",
            FF = "function Attr() {\n    [native code]\n}",
            IE8 = "exception")
    public void attr() throws Exception {
        test("Attr");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.ActiveXObject}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "\nfunction ActiveXObject() {\n    [native code]\n}\n")
    public void activeXObject() throws Exception {
        test("ActiveXObject");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.ApplicationCache}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object ApplicationCache]",
            CHROME = "function ApplicationCache() { [native code] }",
            FF = "exception",
            IE8 = "exception")
    public void applicationCache() throws Exception {
        test("ApplicationCache");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.ApplicationCache}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
        FF = "function OfflineResourceList() {\n    [native code]\n}")
    public void offlineResourceList() throws Exception {
        test("OfflineResourceList");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.BeforeUnloadEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object BeforeUnloadEvent]",
            CHROME = "function BeforeUnloadEvent() { [native code] }",
            FF = "function BeforeUnloadEvent() {\n    [native code]\n}",
            IE8 = "exception")
    public void beforeUnloadEvent() throws Exception {
        test("BeforeUnloadEvent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.BoxObject}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF24 = "[object BoxObject]")
    public void boxObject() throws Exception {
        test("BoxObject");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.CDATASection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object CDATASection]",
            CHROME = "function CDATASection() { [native code] }",
            FF = "function CDATASection() {\n    [native code]\n}",
            IE8 = "exception")
    public void cdataSection() throws Exception {
        test("CDATASection");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.ClipboardData}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void clipboardData() throws Exception {
        test("ClipboardData");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSCharsetRule}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "[object CSSCharsetRule]")
    public void cssCharsetRule() throws Exception {
        test("CSSCharsetRule");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSFontFaceRule}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object CSSFontFaceRule]",
            CHROME = "function CSSFontFaceRule() { [native code] }",
            IE8 = "exception")
    public void cssFontFaceRule() throws Exception {
        test("CSSFontFaceRule");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSImportRule}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object CSSImportRule]",
            CHROME = "function CSSImportRule() { [native code] }",
            IE8 = "exception")
    public void cssImportRule() throws Exception {
        test("CSSImportRule");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSMediaRule}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object CSSMediaRule]",
            CHROME = "function CSSMediaRule() { [native code] }",
            IE8 = "exception")
    public void cssMediaRule() throws Exception {
        test("CSSMediaRule");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSPrimitiveValue}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function CSSPrimitiveValue() {\n    [native code]\n}")
    public void cssPrimitiveValue() throws Exception {
        test("CSSPrimitiveValue");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSRule}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object CSSRule]",
            CHROME = "function CSSRule() { [native code] }",
            IE8 = "exception")
    public void cssRule() throws Exception {
        test("CSSRule");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSRuleList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object CSSRuleList]",
            CHROME = "function CSSRuleList() { [native code] }",
            IE8 = "exception")
    public void cssRuleList() throws Exception {
        test("CSSRuleList");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object CSSStyleDeclaration]",
            CHROME = "function CSSStyleDeclaration() { [native code] }",
            FF = "function CSSStyleDeclaration() {\n    [native code]\n}",
            IE8 = "exception")
    public void cssStyleDeclaration() throws Exception {
        test("CSSStyleDeclaration");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function CSS2Properties() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "[object CSSStyleRule]",
            CHROME = "function CSSStyleRule() { [native code] }",
            IE8 = "exception")
    public void cssStyleRule() throws Exception {
        test("CSSStyleRule");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object CSSStyleSheet]",
            CHROME = "function CSSStyleSheet() { [native code] }",
            FF = "function CSSStyleSheet() {\n    [native code]\n}",
            IE8 = "exception")
    public void cssStyleSheet() throws Exception {
        test("CSSStyleSheet");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSValue}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function CSSValue() {\n    [native code]\n}")
    public void cssValue() throws Exception {
        test("CSSValue");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.canvas.CanvasRenderingContext2D}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object CanvasRenderingContext2D]",
            CHROME = "function CanvasRenderingContext2D() { [native code] }",
            FF = "function CanvasRenderingContext2D() {\n    [native code]\n}",
            IE8 = "exception")
    public void canvasRenderingContext2D() throws Exception {
        test("CanvasRenderingContext2D");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.CharacterDataImpl}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void characterDataImpl() throws Exception {
        test("CharacterDataImpl");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.ClientRect}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object ClientRect]",
            CHROME = "function ClientRect() { [native code] }",
            FF24 = "function ClientRect() {\n    [native code]\n}",
            FF31 = "exception",
            IE8 = "exception")
    public void clientRect() throws Exception {
        test("ClientRect");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Comment}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object Comment]",
            CHROME = "function Comment() { [native code] }",
            FF = "function Comment() {\n    [native code]\n}",
            IE8 = "exception")
    public void comment() throws Exception {
        test("Comment");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Comment}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlCommentElement() throws Exception {
        test("HTMLCommentElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void computedCSSStyleDeclaration() throws Exception {
        test("ComputedCSSStyleDeclaration");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Console}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE11 = "[object Console]")
    public void console() throws Exception {
        test("Console");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.geo.Coordinates}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object Coordinates]",
            CHROME = "exception",
            FF = "exception",
            IE8 = "exception")
    public void coordinates() throws Exception {
        test("Coordinates");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.DataView}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function DataView() {\n    [native code]\n}",
            CHROME = "function DataView() { [native code] }",
            IE11 = "\nfunction DataView() {\n    [native code]\n}\n",
            IE8 = "exception")
    public void dataView() throws Exception {
        test("DataView");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.DOMException}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object DOMException]",
            CHROME = "function DOMException() { [native code] }",
            FF31 = "function DOMException() {\n    [native code]\n}",
            IE8 = "exception")
    public void domException() throws Exception {
        test("DOMException");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.DOMImplementation}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object DOMImplementation]",
            CHROME = "function DOMImplementation() { [native code] }",
            FF = "function DOMImplementation() {\n    [native code]\n}",
            IE8 = "exception")
    public void domImplementation() throws Exception {
        test("DOMImplementation");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.DOMParser}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function DOMParser() {\n    [native code]\n}",
            CHROME = "function DOMParser() { [native code] }",
            IE11 = "\nfunction DOMParser() {\n    [native code]\n}\n",
            IE8 = "exception")
    public void domParser() throws Exception {
        test("DOMParser");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.DOMStringMap}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object DOMStringMap]",
            CHROME = "function DOMStringMap() { [native code] }",
            FF = "function DOMStringMap() {\n    [native code]\n}",
            IE8 = "exception")
    public void domStringMap() throws Exception {
        test("DOMStringMap");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.DOMTokenList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object DOMTokenList]",
            CHROME = "function DOMTokenList() { [native code] }",
            FF = "function DOMTokenList() {\n    [native code]\n}",
            IE8 = "exception")
    public void domTokenList() throws Exception {
        test("DOMTokenList");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Document}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object Document]",
            CHROME = "function Document() { [native code] }",
            FF = "function Document() {\n    [native code]\n}",
            IE8 = "exception")
    public void document() throws Exception {
        test("Document");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.DocumentFragment}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object DocumentFragment]",
            CHROME = "function DocumentFragment() { [native code] }",
            FF = "function DocumentFragment() {\n    [native code]\n}",
            IE8 = "exception")
    public void documentFragment() throws Exception {
        test("DocumentFragment");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.DocumentType}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object DocumentType]",
            CHROME = "function DocumentType() { [native code] }",
            FF = "function DocumentType() {\n    [native code]\n}",
            IE8 = "exception")
    public void documentType() throws Exception {
        test("DocumentType");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Element}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object Element]",
            CHROME = "function Element() { [native code] }",
            FF = "function Element() {\n    [native code]\n}",
            IE8 = "exception")
    public void element() throws Exception {
        test("Element");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Enumerator}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "\nfunction Enumerator() {\n    [native code]\n}\n")
    public void enumerator() throws Exception {
        test("Enumerator");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Event}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object Event]",
            CHROME = "function Event() { [native code] }",
            FF = "function Event() {\n    [native code]\n}",
            IE8 = "exception")
    public void event() throws Exception {
        test("Event");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.EventNode}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void eventNode() throws Exception {
        test("EventNode");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.External}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF31 = "function External() {\n    [native code]\n}")
    public void external() throws Exception {
        test("External");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Float32Array}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Float32Array() {\n    [native code]\n}",
            CHROME = "function Float32Array() { [native code] }",
            IE11 = "\nfunction Float32Array() {\n    [native code]\n}\n",
            IE8 = "exception")
    public void float32Array() throws Exception {
        test("Float32Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Float64Array}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Float64Array() {\n    [native code]\n}",
            CHROME = "function Float64Array() { [native code] }",
            IE11 = "\nfunction Float64Array() {\n    [native code]\n}\n",
            IE8 = "exception")
    public void float64Array() throws Exception {
        test("Float64Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.FormChild}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void formChild() throws Exception {
        test("FormChild");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.FormField}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void formField() throws Exception {
        test("FormField");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.geo.Geolocation}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object Geolocation]",
            CHROME = "exception",
            FF = "exception",
            IE8 = "exception")
    public void geolocation() throws Exception {
        test("Geolocation");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.HashChangeEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function HashChangeEvent() { [native code] }",
            FF = "function HashChangeEvent() {\n    [native code]\n}")
    public void hashChangeEvent() throws Exception {
        test("HashChangeEvent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.History}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object History]",
            CHROME = "function History() { [native code] }",
            FF31 = "function History() {\n    [native code]\n}",
            IE8 = "exception")
    public void history() throws Exception {
        test("History");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAnchorElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLAnchorElement]",
            CHROME = "function HTMLAnchorElement() { [native code] }",
            FF = "function HTMLAnchorElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlAnchorElement() throws Exception {
        test("HTMLAnchorElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAppletElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLAppletElement]",
            CHROME = "function HTMLAppletElement() { [native code] }",
            FF = "function HTMLAppletElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlAppletElement() throws Exception {
        test("HTMLAppletElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAreaElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLAreaElement]",
            CHROME = "function HTMLAreaElement() { [native code] }",
            FF = "function HTMLAreaElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlAreaElement() throws Exception {
        test("HTMLAreaElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAudioElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLAudioElement]",
            CHROME = "function HTMLAudioElement() { [native code] }",
            FF = "function HTMLAudioElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlAudioElement() throws Exception {
        test("HTMLAudioElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBGSoundElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE11 = "[object HTMLBGSoundElement]")
    public void htmlBGSoundElement() throws Exception {
        test("HTMLBGSoundElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBRElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLBRElement]",
            CHROME = "function HTMLBRElement() { [native code] }",
            FF = "function HTMLBRElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlBRElement() throws Exception {
        test("HTMLBRElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBaseElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLBaseElement]",
            CHROME = "function HTMLBaseElement() { [native code] }",
            FF = "function HTMLBaseElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlBaseElement() throws Exception {
        test("HTMLBaseElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBaseFontElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLBaseFontElement]",
            CHROME = "exception",
            FF = "exception",
            IE8 = "exception")
    public void htmlBaseFontElement() throws Exception {
        test("HTMLBaseFontElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBlockElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE11 = "[object HTMLBlockElement]")
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
    @Alerts("exception")
    public void htmlBlockQuoteElement() throws Exception {
        test("HTMLBlockQuoteElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBlockQuoteElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function HTMLQuoteElement() { [native code] }",
            FF = "function HTMLQuoteElement() {\n    [native code]\n}",
            IE11 = "[object HTMLQuoteElement]"
            )
    @NotYetImplemented({ FF, CHROME })
    public void htmlQuoteElement() throws Exception {
        test("HTMLQuoteElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLBodyElement]",
            CHROME = "function HTMLBodyElement() { [native code] }",
            FF = "function HTMLBodyElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlBodyElement() throws Exception {
        test("HTMLBodyElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLButtonElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLButtonElement]",
            CHROME = "function HTMLButtonElement() { [native code] }",
            FF = "function HTMLButtonElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlButtonElement() throws Exception {
        test("HTMLButtonElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCanvasElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLCanvasElement]",
            CHROME = "function HTMLCanvasElement() { [native code] }",
            FF = "function HTMLCanvasElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlCanvasElement() throws Exception {
        test("HTMLCanvasElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLCollection]",
            CHROME = "function HTMLCollection() { [native code] }",
            FF = "function HTMLCollection() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlCollection() throws Exception {
        test("HTMLCollection");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAllCollection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLAllCollection]",
            CHROME = "function HTMLAllCollection() { [native code] }",
            FF = "exception",
            IE8 = "exception")
    public void htmlAllCollection() throws Exception {
        test("HTMLAllCollection");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDataListElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLDataListElement]",
            CHROME = "function HTMLDataListElement() { [native code] }",
            FF = "function HTMLDataListElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlDataListElement() throws Exception {
        test("HTMLDataListElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDDElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlDefinitionDescriptionElement() throws Exception {
        test("HTMLDefinitionDescriptionElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDDElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
        IE11 = "[object HTMLDDElement]")
    public void htmlDDElement() throws Exception {
        test("HTMLDDElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDTElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlDefinitionTermElement() throws Exception {
        test("HTMLDefinitionTermElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDTElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE11 = "[object HTMLDTElement]")
    public void htmlDTElement() throws Exception {
        test("HTMLDTElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDListElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLDListElement]",
            CHROME = "function HTMLDListElement() { [native code] }",
            FF = "function HTMLDListElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlDListElement() throws Exception {
        test("HTMLDListElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDirectoryElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLDirectoryElement]",
            CHROME = "function HTMLDirectoryElement() { [native code] }",
            FF = "function HTMLDirectoryElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlDirectoryElement() throws Exception {
        test("HTMLDirectoryElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDivElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLDivElement]",
            CHROME = "function HTMLDivElement() { [native code] }",
            FF = "function HTMLDivElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlDivElement() throws Exception {
        test("HTMLDivElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLDocument]",
            CHROME = "function HTMLDocument() { [native code] }",
            FF = "function HTMLDocument() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlDocument() throws Exception {
        test("HTMLDocument");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLElement]",
            CHROME = "function HTMLElement() { [native code] }",
            FF = "function HTMLElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlElement() throws Exception {
        test("HTMLElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLEmbedElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLEmbedElement]",
            CHROME = "function HTMLEmbedElement() { [native code] }",
            FF = "function HTMLEmbedElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlEmbedElement() throws Exception {
        test("HTMLEmbedElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFieldSetElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLFieldSetElement]",
            CHROME = "function HTMLFieldSetElement() { [native code] }",
            FF = "function HTMLFieldSetElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlFieldSetElement() throws Exception {
        test("HTMLFieldSetElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFontElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLFontElement]",
            CHROME = "function HTMLFontElement() { [native code] }",
            FF = "function HTMLFontElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlFontElement() throws Exception {
        test("HTMLFontElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFormElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLFormElement]",
            CHROME = "function HTMLFormElement() { [native code] }",
            FF = "function HTMLFormElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlFormElement() throws Exception {
        test("HTMLFormElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFrameElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLFrameElement]",
            CHROME = "function HTMLFrameElement() { [native code] }",
            FF = "function HTMLFrameElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlFrameElement() throws Exception {
        test("HTMLFrameElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFrameSetElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLFrameSetElement]",
            CHROME = "function HTMLFrameSetElement() { [native code] }",
            FF = "function HTMLFrameSetElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlFrameSetElement() throws Exception {
        test("HTMLFrameSetElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHRElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLHRElement]",
            CHROME = "function HTMLHRElement() { [native code] }",
            FF = "function HTMLHRElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlHRElement() throws Exception {
        test("HTMLHRElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHeadElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLHeadElement]",
            CHROME = "function HTMLHeadElement() { [native code] }",
            FF = "function HTMLHeadElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlHeadElement() throws Exception {
        test("HTMLHeadElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHeadingElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLHeadingElement]",
            CHROME = "function HTMLHeadingElement() { [native code] }",
            FF = "function HTMLHeadingElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlHeadingElement() throws Exception {
        test("HTMLHeadingElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHtmlElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLHtmlElement]",
            CHROME = "function HTMLHtmlElement() { [native code] }",
            FF = "function HTMLHtmlElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlHtmlElement() throws Exception {
        test("HTMLHtmlElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLIFrameElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLIFrameElement]",
            CHROME = "function HTMLIFrameElement() { [native code] }",
            FF = "function HTMLIFrameElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlIFrameElement() throws Exception {
        test("HTMLIFrameElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLImageElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "function HTMLImageElement() { [native code] }",
            FF = "function HTMLImageElement() {\n    [native code]\n}",
            IE8 = "exception",
            IE11 = "[object HTMLImageElement]")
    public void htmlImageElement() throws Exception {
        test("HTMLImageElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLImageElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "function HTMLImageElement() { [native code] }",
            FF = "function Image() {\n    [native code]\n}",
            IE8 = "[object HTMLImageElement]",
            IE11 = "\nfunction Image() {\n    [native code]\n}\n")
    public void image() throws Exception {
        test("Image");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLInlineQuotationElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlInlineQuotationElement() throws Exception {
        test("HTMLInlineQuotationElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLInputElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLInputElement]",
            CHROME = "function HTMLInputElement() { [native code] }",
            FF = "function HTMLInputElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlInputElement() throws Exception {
        test("HTMLInputElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLIsIndexElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLIsIndexElement]",
            CHROME = "exception",
            FF = "exception",
            IE8 = "exception")
    public void htmlIsIndexElement() throws Exception {
        test("HTMLIsIndexElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLKeygenElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function HTMLKeygenElement() { [native code] }")
    public void htmlKeygenElement() throws Exception {
        test("HTMLKeygenElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLIElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLLIElement]",
            CHROME = "function HTMLLIElement() { [native code] }",
            FF = "function HTMLLIElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlLIElement() throws Exception {
        test("HTMLLIElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLabelElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLLabelElement]",
            CHROME = "function HTMLLabelElement() { [native code] }",
            FF = "function HTMLLabelElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlLabelElement() throws Exception {
        test("HTMLLabelElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLegendElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLLegendElement]",
            CHROME = "function HTMLLegendElement() { [native code] }",
            FF = "function HTMLLegendElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlLegendElement() throws Exception {
        test("HTMLLegendElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLinkElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLLinkElement]",
            CHROME = "function HTMLLinkElement() { [native code] }",
            FF = "function HTMLLinkElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlLinkElement() throws Exception {
        test("HTMLLinkElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLListElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlListElement() throws Exception {
        test("HTMLListElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMapElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLMapElement]",
            CHROME = "function HTMLMapElement() { [native code] }",
            FF = "function HTMLMapElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlMapElement() throws Exception {
        test("HTMLMapElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMarqueeElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLMarqueeElement]",
            CHROME = "function HTMLMarqueeElement() { [native code] }",
            FF = "exception",
            IE8 = "exception")
    public void htmlMarqueeElement() throws Exception {
        test("HTMLMarqueeElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMediaElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLMediaElement]",
            CHROME = "function HTMLMediaElement() { [native code] }",
            FF = "function HTMLMediaElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlMediaElement() throws Exception {
        test("HTMLMediaElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMenuElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLMenuElement]",
            CHROME = "function HTMLMenuElement() { [native code] }",
            FF = "function HTMLMenuElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlMenuElement() throws Exception {
        test("HTMLMenuElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMetaElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLMetaElement]",
            CHROME = "function HTMLMetaElement() { [native code] }",
            FF = "function HTMLMetaElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlMetaElement() throws Exception {
        test("HTMLMetaElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMeterElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function HTMLMeterElement() { [native code] }",
            FF = "function HTMLMeterElement() {\n    [native code]\n}")
    public void htmlMeterElement() throws Exception {
        test("HTMLMeterElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLModElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLModElement]",
            CHROME = "function HTMLModElement() { [native code] }",
            FF = "function HTMLModElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlModElement() throws Exception {
        test("HTMLModElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLNoShowElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE8 = "exception")
    public void htmlNoShowElement() throws Exception {
        test("HTMLNoShowElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLNextIdElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLNextIdElement]",
            CHROME = "exception",
            FF = "exception",
            IE8 = "exception")
    public void htmlNextIdElement() throws Exception {
        test("HTMLNextIdElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOListElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLOListElement]",
            CHROME = "function HTMLOListElement() { [native code] }",
            FF = "function HTMLOListElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlOListElement() throws Exception {
        test("HTMLOListElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLObjectElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLObjectElement]",
            CHROME = "function HTMLObjectElement() { [native code] }",
            FF = "function HTMLObjectElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlObjectElement() throws Exception {
        test("HTMLObjectElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOptGroupElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLOptGroupElement]",
            CHROME = "function HTMLOptGroupElement() { [native code] }",
            FF = "function HTMLOptGroupElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlOptGroupElement() throws Exception {
        test("HTMLOptGroupElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOptionElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "function HTMLOptionElement() { [native code] }",
            FF = "function HTMLOptionElement() {\n    [native code]\n}",
            IE8 = "exception",
            IE11 = "[object HTMLOptionElement]")
    public void htmlOptionElement() throws Exception {
        test("HTMLOptionElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOptionElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "function HTMLOptionElement() { [native code] }",
            FF = "function Option() {\n    [native code]\n}",
            IE8 = "[object HTMLOptionElement]",
            IE11 = "\nfunction Option() {\n    [native code]\n}\n")
    public void option() throws Exception {
        test("Option");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOptionsCollection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function HTMLOptionsCollection() { [native code] }",
            FF = "function HTMLOptionsCollection() {\n    [native code]\n}")
    public void htmlOptionsCollection() throws Exception {
        test("HTMLOptionsCollection");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOutputElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function HTMLOutputElement() { [native code] }",
            FF = "function HTMLOutputElement() {\n    [native code]\n}")
    public void htmlOutputElement() throws Exception {
        test("HTMLOutputElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLParagraphElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLParagraphElement]",
            CHROME = "function HTMLParagraphElement() { [native code] }",
            FF = "function HTMLParagraphElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlParagraphElement() throws Exception {
        test("HTMLParagraphElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLParamElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLParamElement]",
            CHROME = "function HTMLParamElement() { [native code] }",
            FF = "function HTMLParamElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlParamElement() throws Exception {
        test("HTMLParamElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLPhraseElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLPhraseElement]",
            CHROME = "exception",
            FF = "exception",
            IE8 = "exception")
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
    @Alerts(DEFAULT = "[object HTMLPreElement]",
            CHROME = "function HTMLPreElement() { [native code] }",
            FF = "function HTMLPreElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlPreElement() throws Exception {
        test("HTMLPreElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLProgressElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLProgressElement]",
            CHROME = "function HTMLProgressElement() { [native code] }",
            FF = "function HTMLProgressElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlProgressElement() throws Exception {
        test("HTMLProgressElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLScriptElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLScriptElement]",
            CHROME = "function HTMLScriptElement() { [native code] }",
            FF = "function HTMLScriptElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlScriptElement() throws Exception {
        test("HTMLScriptElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSelectElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLSelectElement]",
            CHROME = "function HTMLSelectElement() { [native code] }",
            FF = "function HTMLSelectElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlSelectElement() throws Exception {
        test("HTMLSelectElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSourceElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLSourceElement]",
            CHROME = "function HTMLSourceElement() { [native code] }",
            FF = "function HTMLSourceElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlSourceElement() throws Exception {
        test("HTMLSourceElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSpanElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLSpanElement]",
            CHROME = "function HTMLSpanElement() { [native code] }",
            FF = "function HTMLSpanElement() {\n    [native code]\n}",
            IE8 = "exception")
    @NotYetImplemented(IE8)
    public void htmlSpanElement() throws Exception {
        test("HTMLSpanElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLStyleElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLStyleElement]",
            CHROME = "function HTMLStyleElement() { [native code] }",
            FF = "function HTMLStyleElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlStyleElement() throws Exception {
        test("HTMLStyleElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableCaptionElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLTableCaptionElement]",
            CHROME = "function HTMLTableCaptionElement() { [native code] }",
            FF = "function HTMLTableCaptionElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlTableCaptionElement() throws Exception {
        test("HTMLTableCaptionElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableCellElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLTableCellElement]",
            CHROME = "function HTMLTableCellElement() { [native code] }",
            FF = "function HTMLTableCellElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlTableCellElement() throws Exception {
        test("HTMLTableCellElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableColElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLTableColElement]",
            CHROME = "function HTMLTableColElement() { [native code] }",
            FF = "function HTMLTableColElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlTableColElement() throws Exception {
        test("HTMLTableColElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableComponent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlTableComponent() throws Exception {
        test("HTMLTableComponent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableDataCellElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLTableDataCellElement]",
            CHROME = "exception",
            FF = "exception",
            IE8 = "exception")
    public void htmlTableDataCellElement() throws Exception {
        test("HTMLTableDataCellElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLTableElement]",
            CHROME = "function HTMLTableElement() { [native code] }",
            FF = "function HTMLTableElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlTableElement() throws Exception {
        test("HTMLTableElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableHeaderCellElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLTableHeaderCellElement]",
            CHROME = "exception",
            FF = "exception",
            IE8 = "exception")
    public void htmlTableHeaderCellElement() throws Exception {
        test("HTMLTableHeaderCellElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableRowElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLTableRowElement]",
            CHROME = "function HTMLTableRowElement() { [native code] }",
            FF = "function HTMLTableRowElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlTableRowElement() throws Exception {
        test("HTMLTableRowElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableSectionElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLTableSectionElement]",
            CHROME = "function HTMLTableSectionElement() { [native code] }",
            FF = "function HTMLTableSectionElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlTableSectionElement() throws Exception {
        test("HTMLTableSectionElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTextElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlTextElement() throws Exception {
        test("HTMLTextElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTextAreaElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLTextAreaElement]",
            CHROME = "function HTMLTextAreaElement() { [native code] }",
            FF = "function HTMLTextAreaElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlTextAreaElement() throws Exception {
        test("HTMLTextAreaElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTimeElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function HTMLTimeElement() {\n    [native code]\n}")
    public void htmlTimeElement() throws Exception {
        test("HTMLTimeElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTitleElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLTitleElement]",
            CHROME = "function HTMLTitleElement() { [native code] }",
            FF = "function HTMLTitleElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlTitleElement() throws Exception {
        test("HTMLTitleElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLUListElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLUListElement]",
            CHROME = "function HTMLUListElement() { [native code] }",
            FF = "function HTMLUListElement() {\n    [native code]\n}",
            IE8 = "exception")
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
    @Alerts(DEFAULT = "[object HTMLUnknownElement]",
            CHROME = "function HTMLUnknownElement() { [native code] }",
            FF = "function HTMLUnknownElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlUnknownElement() throws Exception {
        test("HTMLUnknownElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLUnknownElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlGenericElement() throws Exception {
        test("HTMLGenericElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLWBRElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlWBRElement() throws Exception {
        test("HTMLWBRElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLVideoElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLVideoElement]",
            CHROME = "function HTMLVideoElement() { [native code] }",
            FF = "function HTMLVideoElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void htmlVideoElement() throws Exception {
        test("HTMLVideoElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Int16Array}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Int16Array() {\n    [native code]\n}",
            CHROME = "function Int16Array() { [native code] }",
            IE11 = "\nfunction Int16Array() {\n    [native code]\n}\n",
            IE8 = "exception")
    public void int16Array() throws Exception {
        test("Int16Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Int32Array}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Int32Array() {\n    [native code]\n}",
            CHROME = "function Int32Array() { [native code] }",
            IE11 = "\nfunction Int32Array() {\n    [native code]\n}\n",
            IE8 = "exception")
    public void int32Array() throws Exception {
        test("Int32Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Int8Array}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Int8Array() {\n    [native code]\n}",
            CHROME = "function Int8Array() { [native code] }",
            IE11 = "\nfunction Int8Array() {\n    [native code]\n}\n",
            IE8 = "exception")
    public void int8Array() throws Exception {
        test("Int8Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.KeyboardEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object KeyboardEvent]",
            CHROME = "function KeyboardEvent() { [native code] }",
            FF = "function KeyboardEvent() {\n    [native code]\n}",
            IE8 = "exception")
    public void keyboardEvent() throws Exception {
        test("KeyboardEvent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Location}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object Location]",
            CHROME = "function Location() { [native code] }",
            IE8 = "exception")
    public void location() throws Exception {
        test("Location");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.MediaList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object MediaList]",
            CHROME = "function MediaList() { [native code] }",
            FF31 = "function MediaList() {\n    [native code]\n}",
            IE8 = "exception")
    public void mediaList() throws Exception {
        test("MediaList");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.MessageEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object MessageEvent]",
            CHROME = "function MessageEvent() { [native code] }",
            FF = "function MessageEvent() {\n    [native code]\n}",
            IE8 = "exception")
    public void messageEvent() throws Exception {
        test("MessageEvent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.MimeType}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object MimeType]",
            CHROME = "function MimeType() { [native code] }",
            FF31 = "function MimeType() {\n    [native code]\n}",
            IE8 = "exception")
    public void mimeType() throws Exception {
        test("MimeType");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.MimeTypeArray}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object MimeTypeArray]",
            CHROME = "function MimeTypeArray() { [native code] }",
            FF31 = "function MimeTypeArray() {\n    [native code]\n}",
            IE8 = "exception")
    public void mimeTypeArray() throws Exception {
        test("MimeTypeArray");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.MouseEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object MouseEvent]",
            CHROME = "function MouseEvent() { [native code] }",
            FF = "function MouseEvent() {\n    [native code]\n}",
            IE8 = "exception")
    public void mouseEvent() throws Exception {
        test("MouseEvent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.MutationEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object MutationEvent]",
            CHROME = "function MutationEvent() { [native code] }",
            FF = "function MutationEvent() {\n    [native code]\n}",
            IE8 = "exception")
    public void mutationEvent() throws Exception {
        test("MutationEvent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.NamedNodeMap}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object NamedNodeMap]",
            CHROME = "function NamedNodeMap() { [native code] }",
            FF = "exception",
            IE8 = "exception")
    public void namedNodeMap() throws Exception {
        test("NamedNodeMap");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Namespace}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void namespace() throws Exception {
        test("Namespace");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.NamespaceCollection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void namespaceCollection() throws Exception {
        test("NamespaceCollection");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Navigator}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object Navigator]",
            CHROME = "function Navigator() { [native code] }",
            FF31 = "function Navigator() {\n    [native code]\n}",
            IE8 = "exception")
    public void navigator() throws Exception {
        test("Navigator");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Node}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object Node]",
            CHROME = "function Node() { [native code] }",
            FF = "function Node() {\n    [native code]\n}",
            IE8 = "exception")
    public void node() throws Exception {
        test("Node");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.NodeFilter}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object NodeFilter]",
            CHROME = "function NodeFilter() { [native code] }",
            FF = "function NodeFilter() {\n    [native code]\n}",
            IE8 = "exception")
    public void nodeFilter() throws Exception {
        test("NodeFilter");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.NodeList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object NodeList]",
            CHROME = "function NodeList() { [native code] }",
            FF = "function NodeList() {\n    [native code]\n}",
            IE8 = "exception")
    public void nodeList() throws Exception {
        test("NodeList");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Plugin}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object Plugin]",
            CHROME = "function Plugin() { [native code] }",
            FF31 = "function Plugin() {\n    [native code]\n}",
            IE8 = "exception")
    public void plugin() throws Exception {
        test("Plugin");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.PluginArray}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object PluginArray]",
            CHROME = "function PluginArray() { [native code] }",
            FF31 = "function PluginArray() {\n    [native code]\n}",
            IE8 = "exception")
    public void pluginArray() throws Exception {
        test("PluginArray");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.PointerEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object PointerEvent]",
            CHROME = "exception",
            FF = "exception",
            IE8 = "exception")
    public void pointerEvent() throws Exception {
        test("PointerEvent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Popup}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void popup() throws Exception {
        test("Popup");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.geo.Position}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object Position]",
            CHROME = "exception",
            FF = "exception",
            IE8 = "exception")
    public void position() throws Exception {
        test("Position");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.ProcessingInstruction}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object ProcessingInstruction]",
            CHROME = "function ProcessingInstruction() { [native code] }",
            FF = "function ProcessingInstruction() {\n    [native code]\n}",
            IE8 = "exception")
    public void processingInstruction() throws Exception {
        test("ProcessingInstruction");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Range}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object Range]",
            CHROME = "function Range() { [native code] }",
            FF = "function Range() {\n    [native code]\n}",
            IE8 = "exception")
    public void range() throws Exception {
        test("Range");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.RowContainer}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void rowContainer() throws Exception {
        test("RowContainer");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGAElement]",
            CHROME = "function SVGAElement() { [native code] }",
            FF = "function SVGAElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgAElement() throws Exception {
        test("SVGAElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAltGlyphElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function SVGAltGlyphElement() {\n    [native code]\n}")
    public void svgAltGlyphElement() throws Exception {
        test("SVGAltGlyphElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAngle}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGAngle]",
            CHROME = "function SVGAngle() { [native code] }",
            FF = "function SVGAngle() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgAngle() throws Exception {
        test("SVGAngle");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimateElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGAnimateElement() { [native code] }",
            FF = "function SVGAnimateElement() {\n    [native code]\n}")
    public void svgAnimateElement() throws Exception {
        test("SVGAnimateElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimateMotionElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGAnimateMotionElement() { [native code] }",
            FF = "function SVGAnimateMotionElement() {\n    [native code]\n}")
    public void svgAnimateMotionElement() throws Exception {
        test("SVGAnimateMotionElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimateTransformElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGAnimateTransformElement() { [native code] }",
            FF = "function SVGAnimateTransformElement() {\n    [native code]\n}")
    public void svgAnimateTransformElement() throws Exception {
        test("SVGAnimateTransformElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGCircleElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGCircleElement]",
            CHROME = "function SVGCircleElement() { [native code] }",
            FF = "function SVGCircleElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgCircleElement() throws Exception {
        test("SVGCircleElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGClipPathElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGClipPathElement]",
            CHROME = "function SVGClipPathElement() { [native code] }",
            FF = "function SVGClipPathElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgClipPathElement() throws Exception {
        test("SVGClipPathElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGCursorElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGCursorElement() { [native code] }")
    public void svgCursorElement() throws Exception {
        test("SVGCursorElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGDefsElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGDefsElement]",
            CHROME = "function SVGDefsElement() { [native code] }",
            FF = "function SVGDefsElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgDefsElement() throws Exception {
        test("SVGDefsElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGDescElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGDescElement]",
            CHROME = "function SVGDescElement() { [native code] }",
            FF = "function SVGDescElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgDescElement() throws Exception {
        test("SVGDescElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGElement]",
            CHROME = "function SVGElement() { [native code] }",
            FF = "function SVGElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgElement() throws Exception {
        test("SVGElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGEllipseElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGEllipseElement]",
            CHROME = "function SVGEllipseElement() { [native code] }",
            FF = "function SVGEllipseElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgEllipseElement() throws Exception {
        test("SVGEllipseElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEBlendElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFEBlendElement]",
            CHROME = "function SVGFEBlendElement() { [native code] }",
            FF = "function SVGFEBlendElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFEBlendElement() throws Exception {
        test("SVGFEBlendElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEColorMatrixElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFEColorMatrixElement]",
            CHROME = "function SVGFEColorMatrixElement() { [native code] }",
            FF = "function SVGFEColorMatrixElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFEColorMatrixElement() throws Exception {
        test("SVGFEColorMatrixElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEComponentTransferElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFEComponentTransferElement]",
            CHROME = "function SVGFEComponentTransferElement() { [native code] }",
            FF = "function SVGFEComponentTransferElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFEComponentTransferElement() throws Exception {
        test("SVGFEComponentTransferElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFECompositeElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFECompositeElement]",
            CHROME = "function SVGFECompositeElement() { [native code] }",
            FF = "function SVGFECompositeElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFECompositeElement() throws Exception {
        test("SVGFECompositeElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEConvolveMatrixElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFEConvolveMatrixElement]",
            CHROME = "function SVGFEConvolveMatrixElement() { [native code] }",
            FF = "function SVGFEConvolveMatrixElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFEConvolveMatrixElement() throws Exception {
        test("SVGFEConvolveMatrixElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEDiffuseLightingElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFEDiffuseLightingElement]",
            CHROME = "function SVGFEDiffuseLightingElement() { [native code] }",
            FF = "function SVGFEDiffuseLightingElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFEDiffuseLightingElement() throws Exception {
        test("SVGFEDiffuseLightingElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEDisplacementMapElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFEDisplacementMapElement]",
            CHROME = "function SVGFEDisplacementMapElement() { [native code] }",
            FF = "function SVGFEDisplacementMapElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFEDisplacementMapElement() throws Exception {
        test("SVGFEDisplacementMapElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEDistantLightElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFEDistantLightElement]",
            CHROME = "function SVGFEDistantLightElement() { [native code] }",
            FF = "function SVGFEDistantLightElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFEDistantLightElement() throws Exception {
        test("SVGFEDistantLightElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFloodElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFEFloodElement]",
            CHROME = "function SVGFEFloodElement() { [native code] }",
            FF = "function SVGFEFloodElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFEFloodElement() throws Exception {
        test("SVGFEFloodElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFuncAElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFEFuncAElement]",
            CHROME = "function SVGFEFuncAElement() { [native code] }",
            FF = "function SVGFEFuncAElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFEFuncAElement() throws Exception {
        test("SVGFEFuncAElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFuncBElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFEFuncBElement]",
            CHROME = "function SVGFEFuncBElement() { [native code] }",
            FF = "function SVGFEFuncBElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFEFuncBElement() throws Exception {
        test("SVGFEFuncBElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFuncGElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFEFuncGElement]",
            CHROME = "function SVGFEFuncGElement() { [native code] }",
            FF = "function SVGFEFuncGElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFEFuncGElement() throws Exception {
        test("SVGFEFuncGElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFuncRElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFEFuncRElement]",
            CHROME = "function SVGFEFuncRElement() { [native code] }",
            FF = "function SVGFEFuncRElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFEFuncRElement() throws Exception {
        test("SVGFEFuncRElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEGaussianBlurElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFEGaussianBlurElement]",
            CHROME = "function SVGFEGaussianBlurElement() { [native code] }",
            FF = "function SVGFEGaussianBlurElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFEGaussianBlurElement() throws Exception {
        test("SVGFEGaussianBlurElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEImageElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFEImageElement]",
            CHROME = "function SVGFEImageElement() { [native code] }",
            FF = "function SVGFEImageElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFEImageElement() throws Exception {
        test("SVGFEImageElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEMergeElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFEMergeElement]",
            CHROME = "function SVGFEMergeElement() { [native code] }",
            FF = "function SVGFEMergeElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFEMergeElement() throws Exception {
        test("SVGFEMergeElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEMergeNodeElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFEMergeNodeElement]",
            CHROME = "function SVGFEMergeNodeElement() { [native code] }",
            FF = "function SVGFEMergeNodeElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFEMergeNodeElement() throws Exception {
        test("SVGFEMergeNodeElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEMorphologyElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFEMorphologyElement]",
            CHROME = "function SVGFEMorphologyElement() { [native code] }",
            FF = "function SVGFEMorphologyElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFEMorphologyElement() throws Exception {
        test("SVGFEMorphologyElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEOffsetElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFEOffsetElement]",
            CHROME = "function SVGFEOffsetElement() { [native code] }",
            FF = "function SVGFEOffsetElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFEOffsetElement() throws Exception {
        test("SVGFEOffsetElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEPointLightElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFEPointLightElement]",
            CHROME = "function SVGFEPointLightElement() { [native code] }",
            FF = "function SVGFEPointLightElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFEPointLightElement() throws Exception {
        test("SVGFEPointLightElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFESpecularLightingElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFESpecularLightingElement]",
            CHROME = "function SVGFESpecularLightingElement() { [native code] }",
            FF = "function SVGFESpecularLightingElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFESpecularLightingElement() throws Exception {
        test("SVGFESpecularLightingElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFESpotLightElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFESpotLightElement]",
            CHROME = "function SVGFESpotLightElement() { [native code] }",
            FF = "function SVGFESpotLightElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFESpotLightElement() throws Exception {
        test("SVGFESpotLightElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFETileElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFETileElement]",
            CHROME = "function SVGFETileElement() { [native code] }",
            FF = "function SVGFETileElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFETileElement() throws Exception {
        test("SVGFETileElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFETurbulenceElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFETurbulenceElement]",
            CHROME = "function SVGFETurbulenceElement() { [native code] }",
            FF = "function SVGFETurbulenceElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFETurbulenceElement() throws Exception {
        test("SVGFETurbulenceElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFilterElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGFilterElement]",
            CHROME = "function SVGFilterElement() { [native code] }",
            FF = "function SVGFilterElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgFilterElement() throws Exception {
        test("SVGFilterElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGForeignObjectElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGForeignObjectElement() { [native code] }",
            FF = "function SVGForeignObjectElement() {\n    [native code]\n}")
    public void svgForeignObjectElement() throws Exception {
        test("SVGForeignObjectElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGGElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGGElement]",
            CHROME = "function SVGGElement() { [native code] }",
            FF = "function SVGGElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgGElement() throws Exception {
        test("SVGGElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGImageElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGImageElement]",
            CHROME = "function SVGImageElement() { [native code] }",
            FF = "function SVGImageElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgImageElement() throws Exception {
        test("SVGImageElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGLineElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGLineElement]",
            CHROME = "function SVGLineElement() { [native code] }",
            FF = "function SVGLineElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgLineElement() throws Exception {
        test("SVGLineElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGLinearGradientElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGLinearGradientElement]",
            CHROME = "function SVGLinearGradientElement() { [native code] }",
            FF = "function SVGLinearGradientElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgLinearGradientElement() throws Exception {
        test("SVGLinearGradientElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMarkerElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGMarkerElement]",
            CHROME = "function SVGMarkerElement() { [native code] }",
            FF = "function SVGMarkerElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgMarkerElement() throws Exception {
        test("SVGMarkerElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMaskElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGMaskElement]",
            CHROME = "function SVGMaskElement() { [native code] }",
            FF = "function SVGMaskElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgMaskElement() throws Exception {
        test("SVGMaskElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMatrix}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGMatrix]",
            CHROME = "function SVGMatrix() { [native code] }",
            FF = "function SVGMatrix() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgMatrix() throws Exception {
        test("SVGMatrix");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMetadataElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGMetadataElement]",
            CHROME = "function SVGMetadataElement() { [native code] }",
            FF = "function SVGMetadataElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgMetadataElement() throws Exception {
        test("SVGMetadataElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMPathElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGMPathElement() { [native code] }",
            FF = "function SVGMPathElement() {\n    [native code]\n}")
    public void svgMPathElement() throws Exception {
        test("SVGMPathElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGPathElement]",
            CHROME = "function SVGPathElement() { [native code] }",
            FF = "function SVGPathElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgPathElement() throws Exception {
        test("SVGPathElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPatternElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGPatternElement]",
            CHROME = "function SVGPatternElement() { [native code] }",
            FF = "function SVGPatternElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgPatternElement() throws Exception {
        test("SVGPatternElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPolygonElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGPolygonElement]",
            CHROME = "function SVGPolygonElement() { [native code] }",
            FF = "function SVGPolygonElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgPolygonElement() throws Exception {
        test("SVGPolygonElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPolylineElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGPolylineElement]",
            CHROME = "function SVGPolylineElement() { [native code] }",
            FF = "function SVGPolylineElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgPolylineElement() throws Exception {
        test("SVGPolylineElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGRadialGradientElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGRadialGradientElement]",
            CHROME = "function SVGRadialGradientElement() { [native code] }",
            FF = "function SVGRadialGradientElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgRadialGradientElement() throws Exception {
        test("SVGRadialGradientElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGRect}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGRect]",
            CHROME = "function SVGRect() { [native code] }",
            FF = "function SVGRect() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgRect() throws Exception {
        test("SVGRect");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGRectElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGRectElement]",
            CHROME = "function SVGRectElement() { [native code] }",
            FF = "function SVGRectElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgRectElement() throws Exception {
        test("SVGRectElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGSVGElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGSVGElement]",
            CHROME = "function SVGSVGElement() { [native code] }",
            FF = "function SVGSVGElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgSVGElement() throws Exception {
        test("SVGSVGElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGScriptElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGScriptElement]",
            CHROME = "function SVGScriptElement() { [native code] }",
            FF = "function SVGScriptElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgScriptElement() throws Exception {
        test("SVGScriptElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGSetElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGSetElement() { [native code] }",
            FF = "function SVGSetElement() {\n    [native code]\n}")
    public void svgSetElement() throws Exception {
        test("SVGSetElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGStopElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGStopElement]",
            CHROME = "function SVGStopElement() { [native code] }",
            FF = "function SVGStopElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgStopElement() throws Exception {
        test("SVGStopElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGStyleElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGStyleElement]",
            CHROME = "function SVGStyleElement() { [native code] }",
            FF = "function SVGStyleElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgStyleElement() throws Exception {
        test("SVGStyleElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGSwitchElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGSwitchElement]",
            CHROME = "function SVGSwitchElement() { [native code] }",
            FF = "function SVGSwitchElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgSwitchElement() throws Exception {
        test("SVGSwitchElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGSymbolElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGSymbolElement]",
            CHROME = "function SVGSymbolElement() { [native code] }",
            FF = "function SVGSymbolElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgSymbolElement() throws Exception {
        test("SVGSymbolElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTSpanElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGTSpanElement]",
            CHROME = "function SVGTSpanElement() { [native code] }",
            FF = "function SVGTSpanElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgTSpanElement() throws Exception {
        test("SVGTSpanElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTextElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGTextElement]",
            CHROME = "function SVGTextElement() { [native code] }",
            FF = "function SVGTextElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgTextElement() throws Exception {
        test("SVGTextElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTextPathElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGTextPathElement]",
            CHROME = "function SVGTextPathElement() { [native code] }",
            FF = "function SVGTextPathElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgTextPathElement() throws Exception {
        test("SVGTextPathElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTitleElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGTitleElement]",
            CHROME = "function SVGTitleElement() { [native code] }",
            FF = "function SVGTitleElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgTitleElement() throws Exception {
        test("SVGTitleElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGUseElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGUseElement]",
            CHROME = "function SVGUseElement() { [native code] }",
            FF = "function SVGUseElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgUseElement() throws Exception {
        test("SVGUseElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGViewElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object SVGViewElement]",
            CHROME = "function SVGViewElement() { [native code] }",
            FF = "function SVGViewElement() {\n    [native code]\n}",
            IE8 = "exception")
    public void svgViewElement() throws Exception {
        test("SVGViewElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Screen}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object Screen]",
            CHROME = "function Screen() { [native code] }",
            FF = "function Screen() {\n    [native code]\n}",
            IE8 = "exception")
    public void screen() throws Exception {
        test("Screen");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Selection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object Selection]",
            CHROME = "function Selection() { [native code] }",
            FF31 = "function Selection() {\n    [native code]\n}",
            IE8 = "exception")
    public void selection() throws Exception {
        test("Selection");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.SimpleArray}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void simpleArray() throws Exception {
        test("SimpleArray");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.StaticNodeList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
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
    @Alerts(DEFAULT = "[object Storage]",
            CHROME = "function Storage() { [native code] }",
            IE8 = "exception")
    public void storage() throws Exception {
        test("Storage");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.StyleSheetList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object StyleSheetList]",
            CHROME = "function StyleSheetList() { [native code] }",
            FF31 = "function StyleSheetList() {\n    [native code]\n}",
            IE8 = "exception")
    public void styleSheetList() throws Exception {
        test("StyleSheetList");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Text}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object Text]",
            CHROME = "function Text() { [native code] }",
            FF = "function Text() {\n    [native code]\n}",
            IE8 = "exception")
    public void text() throws Exception {
        test("Text");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.TextRange}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object TextRange]",
            CHROME = "exception",
            FF = "exception",
            IE8 = "exception")
    public void textRange() throws Exception {
        test("TextRange");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.TreeWalker}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object TreeWalker]",
            CHROME = "function TreeWalker() { [native code] }",
            FF = "function TreeWalker() {\n    [native code]\n}",
            IE8 = "exception")
    public void treeWalker() throws Exception {
        test("TreeWalker");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.UIEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object UIEvent]",
            CHROME = "function UIEvent() { [native code] }",
            FF = "function UIEvent() {\n    [native code]\n}",
            IE8 = "exception")
    public void uIEvent() throws Exception {
        test("UIEvent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Uint16Array}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Uint16Array() {\n    [native code]\n}",
            CHROME = "function Uint16Array() { [native code] }",
            IE11 = "\nfunction Uint16Array() {\n    [native code]\n}\n",
            IE8 = "exception")
    public void uint16Array() throws Exception {
        test("Uint16Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Uint32Array}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Uint32Array() {\n    [native code]\n}",
            CHROME = "function Uint32Array() { [native code] }",
            IE11 = "\nfunction Uint32Array() {\n    [native code]\n}\n",
            IE8 = "exception")
    public void uint32Array() throws Exception {
        test("Uint32Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Uint8Array}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Uint8Array() {\n    [native code]\n}",
            CHROME = "function Uint8Array() { [native code] }",
            IE11 = "\nfunction Uint8Array() {\n    [native code]\n}\n",
            IE8 = "exception")
    public void uint8Array() throws Exception {
        test("Uint8Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Uint8ClampedArray}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Uint8ClampedArray() {\n    [native code]\n}",
            CHROME = "function Uint8ClampedArray() { [native code] }",
            IE11 = "\nfunction Uint8ClampedArray() {\n    [native code]\n}\n",
            IE8 = "exception")
    public void uint8ClampedArray() throws Exception {
        test("Uint8ClampedArray");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.WebSocket}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function WebSocket() {\n    [native code]\n}",
            CHROME = "function WebSocket() { [native code] }",
            IE11 = "\nfunction WebSocket() {\n    [native code]\n}\n",
            IE8 = "exception")
    public void webSocket() throws Exception {
        test("WebSocket");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Window}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object Window]",
            CHROME = "function Window() { [native code] }",
            IE8 = "exception")
    public void window() throws Exception {
        test("Window");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object XMLDocument]",
            CHROME = "function XMLDocument() { [native code] }",
            FF = "function XMLDocument() {\n    [native code]\n}",
            IE8 = "exception")
    public void xmlDocument() throws Exception {
        test("XMLDocument");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.xml.XMLHttpRequest}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function XMLHttpRequest() {\n    [native code]\n}",
            CHROME = "function XMLHttpRequest() { [native code] }",
            IE11 = "\nfunction XMLHttpRequest() {\n    [native code]\n}\n",
            IE8 = "[object XMLHttpRequest]")
    public void xmlHttpRequest() throws Exception {
        test("XMLHttpRequest");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.xml.XMLSerializer}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function XMLSerializer() {\n    [native code]\n}",
            CHROME = "function XMLSerializer() { [native code] }",
            IE11 = "\nfunction XMLSerializer() {\n    [native code]\n}\n",
            IE8 = "exception")
    public void xmlSerializer() throws Exception {
        test("XMLSerializer");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.XPathEvaluator}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function XPathEvaluator() { [native code] }",
            FF = "function XPathEvaluator() {\n    [native code]\n}")
    public void xPathEvaluator() throws Exception {
        test("XPathEvaluator");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.XPathNSResolver}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "[object XPathNSResolver]")
    public void xPathNSResolver() throws Exception {
        test("XPathNSResolver");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.XPathResult}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object XPathResult]",
            CHROME = "function XPathResult() { [native code] }",
            IE = "exception")
    public void xPathResult() throws Exception {
        test("XPathResult");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.XSLTProcessor}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object XSLTProcessor]",
            CHROME = "function XSLTProcessor() { [native code] }",
            IE = "exception")
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
    @Alerts("exception")
    public void xsltemplate() throws Exception {
        test("XSLTemplate");
    }

    /**
     * Test {@link net.sourceforge.htmlunit.corejs.javascript.NativeIterator.StopIteration}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "[object StopIteration]")
    public void stopIteration() throws Exception {
        test("StopIteration");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.ClientRect}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF31 = "function DOMRect() {\n    [native code]\n}")
    @NotYetImplemented(FF31)
    public void domRect() throws Exception {
        test("DOMRect");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE11 = "[object MSStyleCSSProperties]")
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
    @Alerts(DEFAULT = "exception",
            IE11 = "[object MSCurrentStyleCSSProperties]")
    @NotYetImplemented(IE11)
    public void msCurrentStyleCSSProperties() throws Exception {
        test("MSCurrentStyleCSSProperties");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Error() { [native code] }",
            FF = "function Error() {\n    [native code]\n}",
            IE = "\nfunction Error() {\n    [native code]\n}\n")
    public void error() throws Exception {
        test("Error");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE11 = "[object ClientRectList]",
            FF24 = "function ClientRectList() {\n    [native code]\n}",
            CHROME = "function ClientRectList() { [native code] }")
    @NotYetImplemented({ CHROME, IE11, FF24 })
    public void clientRectList() throws Exception {
        test("ClientRectList");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function SVGDocument() {\n    [native code]\n}")
    @NotYetImplemented(FF)
    public void svgDocument() throws Exception {
        test("SVGDocument");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Object() { [native code] }",
            FF = "function Object() {\n    [native code]\n}",
            IE = "\nfunction Object() {\n    [native code]\n}\n")
    public void object() throws Exception {
        test("Object");
    }

    /**
     * Test {@link net.sourceforge.htmlunit.corejs.javascript.Arguments}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object Arguments]",
            IE8 = "[object Object]")
    public void arguments() throws Exception {
        test("arguments");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Notification}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Notification() { [native code] }",
              FF = "function Notification() {\n    [native code]\n}",
            IE = "exception")
    public void notification() throws Exception {
        test("Notification");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDetailsElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function HTMLDetailsElement() { [native code] }")
    public void htmlDetailsElement() throws Exception {
        test("HTMLDetailsElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDialogElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function HTMLDialogElement() { [native code] }")
    public void htmlDialogElement() throws Exception {
        test("HTMLDialogElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTrackElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "function HTMLTrackElement() { [native code] }",
            FF = "function HTMLTrackElement() {\n    [native code]\n}",
            IE11 = "[object HTMLTrackElement]",
            IE8 = "exception")
    public void htmlTrackElement() throws Exception {
        test("HTMLTrackElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMenuItemElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function HTMLMenuItemElement() {\n    [native code]\n}")
    public void htmlMenuItemElement() throws Exception {
        test("HTMLMenuItemElement");
    }

}
