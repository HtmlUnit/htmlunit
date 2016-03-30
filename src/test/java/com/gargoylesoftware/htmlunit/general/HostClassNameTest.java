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
package com.gargoylesoftware.htmlunit.general;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.EDGE;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.AlertsStandards;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.annotations.StandardsMode;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Test the host class names.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API">Web API Interfaces</a>
 */
@RunWith(BrowserRunner.class)
@StandardsMode
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
    @Alerts(DEFAULT = "function ArrayBuffer() { [native code] }",
            FF = "function ArrayBuffer() {\n    [native code]\n}",
            IE = "\nfunction ArrayBuffer() {\n    [native code]\n}\n")
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
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.Attr}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Attr() { [native code] }",
            IE = "[object Attr]",
            FF = "function Attr() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "function ApplicationCache() { [native code] }",
            IE = "[object ApplicationCache]",
            FF = "exception")
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
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.BeforeUnloadEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function BeforeUnloadEvent() { [native code] }",
            IE = "[object BeforeUnloadEvent]",
            FF = "function BeforeUnloadEvent() {\n    [native code]\n}")
    public void beforeUnloadEvent() throws Exception {
        test("BeforeUnloadEvent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.BoxObject}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void boxObject() throws Exception {
        test("BoxObject");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.CDATASection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function CDATASection() { [native code] }",
            IE = "[object CDATASection]",
            FF = "function CDATASection() {\n    [native code]\n}")
    public void cdataSection() throws Exception {
        test("CDATASection");
    }

    /**
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
            FF38 = "[object CSSCharsetRule]")
    public void cssCharsetRule() throws Exception {
        test("CSSCharsetRule");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSFontFaceRule}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function CSSFontFaceRule() { [native code] }",
            FF = "[object CSSFontFaceRule]",
            IE = "[object CSSFontFaceRule]")
    public void cssFontFaceRule() throws Exception {
        test("CSSFontFaceRule");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSImportRule}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function CSSImportRule() { [native code] }",
            FF = "[object CSSImportRule]",
            IE = "[object CSSImportRule]")
    public void cssImportRule() throws Exception {
        test("CSSImportRule");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSMediaRule}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function CSSMediaRule() { [native code] }",
            FF = "[object CSSMediaRule]",
            IE = "[object CSSMediaRule]")
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
    @Alerts(DEFAULT = "function CSSRule() { [native code] }",
            FF = "[object CSSRule]",
            IE = "[object CSSRule]")
    public void cssRule() throws Exception {
        test("CSSRule");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSRuleList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function CSSRuleList() { [native code] }",
            IE = "[object CSSRuleList]",
            FF = "function CSSRuleList() {\n    [native code]\n}")
    public void cssRuleList() throws Exception {
        test("CSSRuleList");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function CSSStyleDeclaration() { [native code] }",
            IE = "[object CSSStyleDeclaration]",
            FF = "function CSSStyleDeclaration() {\n    [native code]\n}")
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
    public void css2Properties() throws Exception {
        test("CSS2Properties");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleRule}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function CSSStyleRule() { [native code] }",
            FF = "[object CSSStyleRule]",
            IE = "[object CSSStyleRule]")
    @AlertsStandards(DEFAULT = "[object CSSStyleRule]",
            CHROME = "function CSSStyleRule() { [native code] }",
            EDGE = "function CSSStyleRule() { [native code] }")
    public void cssStyleRule() throws Exception {
        test("CSSStyleRule");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function CSSStyleSheet() { [native code] }",
            IE = "[object CSSStyleSheet]",
            FF = "function CSSStyleSheet() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "function CanvasRenderingContext2D() { [native code] }",
            IE = "[object CanvasRenderingContext2D]",
            FF = "function CanvasRenderingContext2D() {\n    [native code]\n}")
    public void canvasRenderingContext2D() throws Exception {
        test("CanvasRenderingContext2D");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.CharacterData}.
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
    @Alerts(DEFAULT = "function ClientRect() { [native code] }",
            IE = "[object ClientRect]",
            FF = "exception")
    public void clientRect() throws Exception {
        test("ClientRect");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.Comment}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Comment() { [native code] }",
            IE = "[object Comment]",
            FF = "function Comment() {\n    [native code]\n}")
    public void comment() throws Exception {
        test("Comment");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.Comment}.
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
            EDGE = "function Console() { [native code] }",
            IE = "[object Console]")
    public void console() throws Exception {
        test("Console");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.geo.Coordinates}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            EDGE = "function Coordinates() { [native code] }",
            IE = "[object Coordinates]")
    public void coordinates() throws Exception {
        test("Coordinates");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.DataView}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function DataView() { [native code] }",
            FF = "function DataView() {\n    [native code]\n}",
            IE = "\nfunction DataView() {\n    [native code]\n}\n")
    public void dataView() throws Exception {
        test("DataView");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.DOMException}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function DOMException() { [native code] }",
            IE = "[object DOMException]",
            FF = "function DOMException() {\n    [native code]\n}")
    public void domException() throws Exception {
        test("DOMException");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.DOMImplementation}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function DOMImplementation() { [native code] }",
            IE = "[object DOMImplementation]",
            FF = "function DOMImplementation() {\n    [native code]\n}")
    public void domImplementation() throws Exception {
        test("DOMImplementation");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.DOMParser}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function DOMParser() { [native code] }",
            FF = "function DOMParser() {\n    [native code]\n}",
            IE = "\nfunction DOMParser() {\n    [native code]\n}\n")
    public void domParser() throws Exception {
        test("DOMParser");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.DOMStringMap}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function DOMStringMap() { [native code] }",
            IE = "[object DOMStringMap]",
            FF = "function DOMStringMap() {\n    [native code]\n}")
    public void domStringMap() throws Exception {
        test("DOMStringMap");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.DOMTokenList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function DOMTokenList() { [native code] }",
            IE = "[object DOMTokenList]",
            FF = "function DOMTokenList() {\n    [native code]\n}")
    public void domTokenList() throws Exception {
        test("DOMTokenList");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.Document}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Document() { [native code] }",
            IE = "[object Document]",
            FF = "function Document() {\n    [native code]\n}")
    public void document() throws Exception {
        test("Document");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.DocumentFragment}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function DocumentFragment() { [native code] }",
            IE = "[object DocumentFragment]",
            FF = "function DocumentFragment() {\n    [native code]\n}")
    public void documentFragment() throws Exception {
        test("DocumentFragment");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.DocumentType}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function DocumentType() { [native code] }",
            IE = "[object DocumentType]",
            FF = "function DocumentType() {\n    [native code]\n}")
    public void documentType() throws Exception {
        test("DocumentType");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Element}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Element() { [native code] }",
            IE = "[object Element]",
            FF = "function Element() {\n    [native code]\n}")
    public void element() throws Exception {
        test("Element");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.Enumerator}.
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
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.Event}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Event() { [native code] }",
            IE = "[object Event]",
            FF = "function Event() {\n    [native code]\n}")
    public void event() throws Exception {
        test("Event");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.EventNode}.
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
            FF = "function External() {\n    [native code]\n}")
    public void external() throws Exception {
        test("External");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Float32Array}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Float32Array() { [native code] }",
            FF = "function Float32Array() {\n    [native code]\n}",
            IE = "\nfunction Float32Array() {\n    [native code]\n}\n")
    public void float32Array() throws Exception {
        test("Float32Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Float64Array}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Float64Array() { [native code] }",
            FF = "function Float64Array() {\n    [native code]\n}",
            IE = "\nfunction Float64Array() {\n    [native code]\n}\n")
    public void float64Array() throws Exception {
        test("Float64Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.FormChild}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void formChild() throws Exception {
        test("FormChild");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.FormField}.
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
    @Alerts(DEFAULT = "exception",
            IE = "[object Geolocation]",
            EDGE = "function Geolocation() { [native code] }")
    public void geolocation() throws Exception {
        test("Geolocation");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.HashChangeEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HashChangeEvent() { [native code] }",
            IE = "exception",
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
    @Alerts(DEFAULT = "function History() { [native code] }",
            IE = "[object History]",
            FF = "function History() {\n    [native code]\n}")
    public void history() throws Exception {
        test("History");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAnchorElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLAnchorElement() { [native code] }",
            IE = "[object HTMLAnchorElement]",
            FF = "function HTMLAnchorElement() {\n    [native code]\n}")
    public void htmlAnchorElement() throws Exception {
        test("HTMLAnchorElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAppletElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object HTMLAppletElement]",
            FF = "function HTMLAppletElement() {\n    [native code]\n}")
    public void htmlAppletElement() throws Exception {
        test("HTMLAppletElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAreaElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLAreaElement() { [native code] }",
            IE = "[object HTMLAreaElement]",
            FF = "function HTMLAreaElement() {\n    [native code]\n}")
    public void htmlAreaElement() throws Exception {
        test("HTMLAreaElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAudioElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLAudioElement() { [native code] }",
            IE = "[object HTMLAudioElement]",
            FF = "function HTMLAudioElement() {\n    [native code]\n}")
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
            IE = "[object HTMLBGSoundElement]")
    public void htmlBGSoundElement() throws Exception {
        test("HTMLBGSoundElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBRElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLBRElement() { [native code] }",
            IE = "[object HTMLBRElement]",
            FF = "function HTMLBRElement() {\n    [native code]\n}")
    public void htmlBRElement() throws Exception {
        test("HTMLBRElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBaseElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLBaseElement() { [native code] }",
            IE = "[object HTMLBaseElement]",
            FF = "function HTMLBaseElement() {\n    [native code]\n}")
    public void htmlBaseElement() throws Exception {
        test("HTMLBaseElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBaseFontElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object HTMLBaseFontElement]",
            EDGE = "function HTMLBaseFontElement() { [native code] }")
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
            IE = "[object HTMLBlockElement]")
    public void htmlBlockElement() throws Exception {
        test("HTMLBlockElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLQuoteElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlBlockQuoteElement() throws Exception {
        test("HTMLBlockQuoteElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLQuoteElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLQuoteElement() { [native code] }",
            FF = "function HTMLQuoteElement() {\n    [native code]\n}",
            IE = "[object HTMLQuoteElement]")
    public void htmlQuoteElement() throws Exception {
        test("HTMLQuoteElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLBodyElement() { [native code] }",
            IE = "[object HTMLBodyElement]",
            FF = "function HTMLBodyElement() {\n    [native code]\n}")
    public void htmlBodyElement() throws Exception {
        test("HTMLBodyElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLButtonElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLButtonElement() { [native code] }",
            IE = "[object HTMLButtonElement]",
            FF = "function HTMLButtonElement() {\n    [native code]\n}")
    public void htmlButtonElement() throws Exception {
        test("HTMLButtonElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCanvasElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLCanvasElement() { [native code] }",
            IE = "[object HTMLCanvasElement]",
            FF = "function HTMLCanvasElement() {\n    [native code]\n}")
    public void htmlCanvasElement() throws Exception {
        test("HTMLCanvasElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLCollection() { [native code] }",
            IE = "[object HTMLCollection]",
            FF = "function HTMLCollection() {\n    [native code]\n}")
    public void htmlCollection() throws Exception {
        test("HTMLCollection");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAllCollection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLAllCollection() { [native code] }",
            IE = "[object HTMLAllCollection]",
            FF = "function HTMLAllCollection() {\n    [native code]\n}")
    public void htmlAllCollection() throws Exception {
        test("HTMLAllCollection");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDataListElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLDataListElement() { [native code] }",
            IE = "[object HTMLDataListElement]",
            FF = "function HTMLDataListElement() {\n    [native code]\n}")
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
            IE = "[object HTMLDDElement]")
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
            IE = "[object HTMLDTElement]")
    public void htmlDTElement() throws Exception {
        test("HTMLDTElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDListElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLDListElement() { [native code] }",
            IE = "[object HTMLDListElement]",
            FF = "function HTMLDListElement() {\n    [native code]\n}")
    public void htmlDListElement() throws Exception {
        test("HTMLDListElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDirectoryElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLDirectoryElement() { [native code] }",
            IE = "[object HTMLDirectoryElement]",
            FF = "function HTMLDirectoryElement() {\n    [native code]\n}")
    public void htmlDirectoryElement() throws Exception {
        test("HTMLDirectoryElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDivElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLDivElement() { [native code] }",
            IE = "[object HTMLDivElement]",
            FF = "function HTMLDivElement() {\n    [native code]\n}")
    public void htmlDivElement() throws Exception {
        test("HTMLDivElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLDocument() { [native code] }",
            IE = "[object HTMLDocument]",
            FF = "function HTMLDocument() {\n    [native code]\n}")
    public void htmlDocument() throws Exception {
        test("HTMLDocument");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLElement() { [native code] }",
            IE = "[object HTMLElement]",
            FF = "function HTMLElement() {\n    [native code]\n}")
    public void htmlElement() throws Exception {
        test("HTMLElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLEmbedElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLEmbedElement() { [native code] }",
            IE = "[object HTMLEmbedElement]",
            FF = "function HTMLEmbedElement() {\n    [native code]\n}")
    public void htmlEmbedElement() throws Exception {
        test("HTMLEmbedElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFieldSetElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLFieldSetElement() { [native code] }",
            IE = "[object HTMLFieldSetElement]",
            FF = "function HTMLFieldSetElement() {\n    [native code]\n}")
    public void htmlFieldSetElement() throws Exception {
        test("HTMLFieldSetElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFontElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLFontElement() { [native code] }",
            IE = "[object HTMLFontElement]",
            FF = "function HTMLFontElement() {\n    [native code]\n}")
    public void htmlFontElement() throws Exception {
        test("HTMLFontElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFormElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLFormElement() { [native code] }",
            IE = "[object HTMLFormElement]",
            FF = "function HTMLFormElement() {\n    [native code]\n}")
    public void htmlFormElement() throws Exception {
        test("HTMLFormElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFrameElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLFrameElement() { [native code] }",
            IE = "[object HTMLFrameElement]",
            FF = "function HTMLFrameElement() {\n    [native code]\n}")
    public void htmlFrameElement() throws Exception {
        test("HTMLFrameElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFrameSetElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLFrameSetElement() { [native code] }",
            IE = "[object HTMLFrameSetElement]",
            FF = "function HTMLFrameSetElement() {\n    [native code]\n}")
    public void htmlFrameSetElement() throws Exception {
        test("HTMLFrameSetElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHRElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLHRElement() { [native code] }",
            IE = "[object HTMLHRElement]",
            FF = "function HTMLHRElement() {\n    [native code]\n}")
    public void htmlHRElement() throws Exception {
        test("HTMLHRElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHeadElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLHeadElement() { [native code] }",
            IE = "[object HTMLHeadElement]",
            FF = "function HTMLHeadElement() {\n    [native code]\n}")
    public void htmlHeadElement() throws Exception {
        test("HTMLHeadElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHeadingElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLHeadingElement() { [native code] }",
            IE = "[object HTMLHeadingElement]",
            FF = "function HTMLHeadingElement() {\n    [native code]\n}")
    public void htmlHeadingElement() throws Exception {
        test("HTMLHeadingElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHtmlElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLHtmlElement() { [native code] }",
            IE = "[object HTMLHtmlElement]",
            FF = "function HTMLHtmlElement() {\n    [native code]\n}")
    public void htmlHtmlElement() throws Exception {
        test("HTMLHtmlElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLIFrameElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLIFrameElement() { [native code] }",
            IE = "[object HTMLIFrameElement]",
            FF = "function HTMLIFrameElement() {\n    [native code]\n}")
    public void htmlIFrameElement() throws Exception {
        test("HTMLIFrameElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLImageElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLImageElement() { [native code] }",
            IE = "[object HTMLImageElement]",
            FF = "function HTMLImageElement() {\n    [native code]\n}")
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
            EDGE = "function Image() { [native code] }",
            FF = "function Image() {\n    [native code]\n}",
            IE = "\nfunction Image() {\n    [native code]\n}\n")
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
    @Alerts(DEFAULT = "function HTMLInputElement() { [native code] }",
            IE = "[object HTMLInputElement]",
            FF = "function HTMLInputElement() {\n    [native code]\n}")
    public void htmlInputElement() throws Exception {
        test("HTMLInputElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLIsIndexElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object HTMLIsIndexElement]")
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
    @Alerts(DEFAULT = "function HTMLLIElement() { [native code] }",
            IE = "[object HTMLLIElement]",
            FF = "function HTMLLIElement() {\n    [native code]\n}")
    public void htmlLIElement() throws Exception {
        test("HTMLLIElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLabelElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLLabelElement() { [native code] }",
            IE = "[object HTMLLabelElement]",
            FF = "function HTMLLabelElement() {\n    [native code]\n}")
    public void htmlLabelElement() throws Exception {
        test("HTMLLabelElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLegendElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLLegendElement() { [native code] }",
            IE = "[object HTMLLegendElement]",
            FF = "function HTMLLegendElement() {\n    [native code]\n}")
    public void htmlLegendElement() throws Exception {
        test("HTMLLegendElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLinkElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLLinkElement() { [native code] }",
            IE = "[object HTMLLinkElement]",
            FF = "function HTMLLinkElement() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "function HTMLMapElement() { [native code] }",
            IE = "[object HTMLMapElement]",
            FF = "function HTMLMapElement() {\n    [native code]\n}")
    public void htmlMapElement() throws Exception {
        test("HTMLMapElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMarqueeElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLMarqueeElement() { [native code] }",
            IE = "[object HTMLMarqueeElement]",
            FF = "exception")
    public void htmlMarqueeElement() throws Exception {
        test("HTMLMarqueeElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMediaElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLMediaElement() { [native code] }",
            IE = "[object HTMLMediaElement]",
            FF = "function HTMLMediaElement() {\n    [native code]\n}")
    public void htmlMediaElement() throws Exception {
        test("HTMLMediaElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMenuElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLMenuElement() { [native code] }",
            IE = "[object HTMLMenuElement]",
            FF = "function HTMLMenuElement() {\n    [native code]\n}")
    public void htmlMenuElement() throws Exception {
        test("HTMLMenuElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMetaElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLMetaElement() { [native code] }",
            IE = "[object HTMLMetaElement]",
            FF = "function HTMLMetaElement() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "function HTMLModElement() { [native code] }",
            IE = "[object HTMLModElement]",
            FF = "function HTMLModElement() {\n    [native code]\n}")
    public void htmlModElement() throws Exception {
        test("HTMLModElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLNoShowElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlNoShowElement() throws Exception {
        test("HTMLNoShowElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLNextIdElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object HTMLNextIdElement]")
    public void htmlNextIdElement() throws Exception {
        test("HTMLNextIdElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOListElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLOListElement() { [native code] }",
            IE = "[object HTMLOListElement]",
            FF = "function HTMLOListElement() {\n    [native code]\n}")
    public void htmlOListElement() throws Exception {
        test("HTMLOListElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLObjectElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLObjectElement() { [native code] }",
            IE = "[object HTMLObjectElement]",
            FF = "function HTMLObjectElement() {\n    [native code]\n}")
    public void htmlObjectElement() throws Exception {
        test("HTMLObjectElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOptGroupElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLOptGroupElement() { [native code] }",
            IE = "[object HTMLOptGroupElement]",
            FF = "function HTMLOptGroupElement() {\n    [native code]\n}")
    public void htmlOptGroupElement() throws Exception {
        test("HTMLOptGroupElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOptionElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLOptionElement() { [native code] }",
            IE = "[object HTMLOptionElement]",
            FF = "function HTMLOptionElement() {\n    [native code]\n}")
    public void htmlOptionElement() throws Exception {
        test("HTMLOptionElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOptionElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLOptionElement() { [native code] }",
            EDGE = "function Option() { [native code] }",
            FF = "function Option() {\n    [native code]\n}",
            IE = "\nfunction Option() {\n    [native code]\n}\n")
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
    @Alerts(DEFAULT = "function HTMLParagraphElement() { [native code] }",
            IE = "[object HTMLParagraphElement]",
            FF = "function HTMLParagraphElement() {\n    [native code]\n}")
    public void htmlParagraphElement() throws Exception {
        test("HTMLParagraphElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLParamElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLParamElement() { [native code] }",
            IE = "[object HTMLParamElement]",
            FF = "function HTMLParamElement() {\n    [native code]\n}")
    public void htmlParamElement() throws Exception {
        test("HTMLParamElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLPhraseElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object HTMLPhraseElement]")
    public void htmlPhraseElement() throws Exception {
        test("HTMLPhraseElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLPreElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLPreElement() { [native code] }",
            IE = "[object HTMLPreElement]",
            FF = "function HTMLPreElement() {\n    [native code]\n}")
    public void htmlPreElement() throws Exception {
        test("HTMLPreElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLProgressElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLProgressElement() { [native code] }",
            IE = "[object HTMLProgressElement]",
            FF = "function HTMLProgressElement() {\n    [native code]\n}")
    public void htmlProgressElement() throws Exception {
        test("HTMLProgressElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLScriptElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLScriptElement() { [native code] }",
            IE = "[object HTMLScriptElement]",
            FF = "function HTMLScriptElement() {\n    [native code]\n}")
    public void htmlScriptElement() throws Exception {
        test("HTMLScriptElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSelectElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLSelectElement() { [native code] }",
            IE = "[object HTMLSelectElement]",
            FF = "function HTMLSelectElement() {\n    [native code]\n}")
    public void htmlSelectElement() throws Exception {
        test("HTMLSelectElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSourceElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLSourceElement() { [native code] }",
            IE = "[object HTMLSourceElement]",
            FF = "function HTMLSourceElement() {\n    [native code]\n}")
    public void htmlSourceElement() throws Exception {
        test("HTMLSourceElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSpanElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLSpanElement() { [native code] }",
            IE = "[object HTMLSpanElement]",
            FF = "function HTMLSpanElement() {\n    [native code]\n}")
    public void htmlSpanElement() throws Exception {
        test("HTMLSpanElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLStyleElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLStyleElement() { [native code] }",
            IE = "[object HTMLStyleElement]",
            FF = "function HTMLStyleElement() {\n    [native code]\n}")
    public void htmlStyleElement() throws Exception {
        test("HTMLStyleElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableCaptionElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLTableCaptionElement() { [native code] }",
            IE = "[object HTMLTableCaptionElement]",
            FF = "function HTMLTableCaptionElement() {\n    [native code]\n}")
    public void htmlTableCaptionElement() throws Exception {
        test("HTMLTableCaptionElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableCellElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLTableCellElement() { [native code] }",
            IE = "[object HTMLTableCellElement]",
            FF = "function HTMLTableCellElement() {\n    [native code]\n}")
    public void htmlTableCellElement() throws Exception {
        test("HTMLTableCellElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableColElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLTableColElement() { [native code] }",
            IE = "[object HTMLTableColElement]",
            FF = "function HTMLTableColElement() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "exception",
            IE = "[object HTMLTableDataCellElement]",
            EDGE = "function HTMLTableDataCellElement() { [native code] }")
    public void htmlTableDataCellElement() throws Exception {
        test("HTMLTableDataCellElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLTableElement() { [native code] }",
            IE = "[object HTMLTableElement]",
            FF = "function HTMLTableElement() {\n    [native code]\n}")
    public void htmlTableElement() throws Exception {
        test("HTMLTableElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableHeaderCellElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object HTMLTableHeaderCellElement]",
            EDGE = "function HTMLTableHeaderCellElement() { [native code] }")
    public void htmlTableHeaderCellElement() throws Exception {
        test("HTMLTableHeaderCellElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableRowElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLTableRowElement() { [native code] }",
            IE = "[object HTMLTableRowElement]",
            FF = "function HTMLTableRowElement() {\n    [native code]\n}")
    public void htmlTableRowElement() throws Exception {
        test("HTMLTableRowElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableSectionElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLTableSectionElement() { [native code] }",
            IE = "[object HTMLTableSectionElement]",
            FF = "function HTMLTableSectionElement() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "function HTMLTextAreaElement() { [native code] }",
            IE = "[object HTMLTextAreaElement]",
            FF = "function HTMLTextAreaElement() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "function HTMLTitleElement() { [native code] }",
            IE = "[object HTMLTitleElement]",
            FF = "function HTMLTitleElement() {\n    [native code]\n}")
    public void htmlTitleElement() throws Exception {
        test("HTMLTitleElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLUListElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLUListElement() { [native code] }",
            IE = "[object HTMLUListElement]",
            FF = "function HTMLUListElement() {\n    [native code]\n}")
    public void htmlUListElement() throws Exception {
        test("HTMLUListElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLUnknownElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLUnknownElement() { [native code] }",
            IE = "[object HTMLUnknownElement]",
            FF = "function HTMLUnknownElement() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "function HTMLVideoElement() { [native code] }",
            IE = "[object HTMLVideoElement]",
            FF = "function HTMLVideoElement() {\n    [native code]\n}")
    public void htmlVideoElement() throws Exception {
        test("HTMLVideoElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Int16Array}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Int16Array() { [native code] }",
            FF = "function Int16Array() {\n    [native code]\n}",
            IE = "\nfunction Int16Array() {\n    [native code]\n}\n")
    public void int16Array() throws Exception {
        test("Int16Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Int32Array}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Int32Array() { [native code] }",
            FF = "function Int32Array() {\n    [native code]\n}",
            IE = "\nfunction Int32Array() {\n    [native code]\n}\n")
    public void int32Array() throws Exception {
        test("Int32Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Int8Array}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Int8Array() { [native code] }",
            FF = "function Int8Array() {\n    [native code]\n}",
            IE = "\nfunction Int8Array() {\n    [native code]\n}\n")
    public void int8Array() throws Exception {
        test("Int8Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.KeyboardEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function KeyboardEvent() { [native code] }",
            IE = "[object KeyboardEvent]",
            FF = "function KeyboardEvent() {\n    [native code]\n}")
    public void keyboardEvent() throws Exception {
        test("KeyboardEvent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Location}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Location() { [native code] }",
            IE = "[object Location]",
            FF = "function Location() {\n    [native code]\n}")
    @AlertsStandards(DEFAULT = "[object Location]",
            CHROME = "function Location() { [native code] }",
            EDGE = "function Location() { [native code] }",
            FF = "function Location() {\n    [native code]\n}")
    public void location() throws Exception {
        test("Location");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.MediaList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function MediaList() { [native code] }",
            IE = "[object MediaList]",
            FF = "function MediaList() {\n    [native code]\n}")
    public void mediaList() throws Exception {
        test("MediaList");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.MessageEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function MessageEvent() { [native code] }",
            IE = "[object MessageEvent]",
            FF = "function MessageEvent() {\n    [native code]\n}")
    public void messageEvent() throws Exception {
        test("MessageEvent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.MimeType}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function MimeType() { [native code] }",
            IE = "[object MimeType]",
            FF = "function MimeType() {\n    [native code]\n}")
    public void mimeType() throws Exception {
        test("MimeType");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.MimeTypeArray}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function MimeTypeArray() { [native code] }",
            IE = "[object MimeTypeArray]",
            FF = "function MimeTypeArray() {\n    [native code]\n}")
    public void mimeTypeArray() throws Exception {
        test("MimeTypeArray");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function MouseEvent() { [native code] }",
            IE = "[object MouseEvent]",
            FF = "function MouseEvent() {\n    [native code]\n}")
    public void mouseEvent() throws Exception {
        test("MouseEvent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.MutationEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function MutationEvent() { [native code] }",
            IE = "[object MutationEvent]",
            FF = "function MutationEvent() {\n    [native code]\n}")
    public void mutationEvent() throws Exception {
        test("MutationEvent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.NamedNodeMap}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function NamedNodeMap() { [native code] }",
            IE = "[object NamedNodeMap]",
            FF = "function NamedNodeMap() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "function Navigator() { [native code] }",
            IE = "[object Navigator]",
            FF = "function Navigator() {\n    [native code]\n}")
    public void navigator() throws Exception {
        test("Navigator");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.Node}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Node() { [native code] }",
            IE = "[object Node]",
            FF = "function Node() {\n    [native code]\n}")
    public void node() throws Exception {
        test("Node");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.NodeFilter}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function NodeFilter() { [native code] }",
            IE = "[object NodeFilter]",
            FF = "function NodeFilter() {\n    [native code]\n}")
    public void nodeFilter() throws Exception {
        test("NodeFilter");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.NodeList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function NodeList() { [native code] }",
            IE = "[object NodeList]",
            FF = "function NodeList() {\n    [native code]\n}")
    public void nodeList() throws Exception {
        test("NodeList");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Plugin}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Plugin() { [native code] }",
            IE = "[object Plugin]",
            FF = "function Plugin() {\n    [native code]\n}")
    public void plugin() throws Exception {
        test("Plugin");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.PluginArray}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function PluginArray() { [native code] }",
            IE = "[object PluginArray]",
            FF = "function PluginArray() {\n    [native code]\n}")
    public void pluginArray() throws Exception {
        test("PluginArray");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.PointerEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object PointerEvent]",
            EDGE = "function PointerEvent() { [native code] }")
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
    @Alerts(DEFAULT = "exception",
            IE = "[object Position]",
            EDGE = "function Position() { [native code] }")
    public void position() throws Exception {
        test("Position");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.ProcessingInstruction}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function ProcessingInstruction() { [native code] }",
            IE = "[object ProcessingInstruction]",
            FF = "function ProcessingInstruction() {\n    [native code]\n}")
    public void processingInstruction() throws Exception {
        test("ProcessingInstruction");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.Range}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Range() { [native code] }",
            IE = "[object Range]",
            FF = "function Range() {\n    [native code]\n}")
    public void range() throws Exception {
        test("Range");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.RowContainer}.
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
    @Alerts(DEFAULT = "function SVGAElement() { [native code] }",
            IE = "[object SVGAElement]",
            FF = "function SVGAElement() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "function SVGAngle() { [native code] }",
            IE = "[object SVGAngle]",
            FF = "function SVGAngle() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "function SVGCircleElement() { [native code] }",
            IE = "[object SVGCircleElement]",
            FF = "function SVGCircleElement() {\n    [native code]\n}")
    public void svgCircleElement() throws Exception {
        test("SVGCircleElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGClipPathElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGClipPathElement() { [native code] }",
            IE = "[object SVGClipPathElement]",
            FF = "function SVGClipPathElement() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "function SVGDefsElement() { [native code] }",
            IE = "[object SVGDefsElement]",
            FF = "function SVGDefsElement() {\n    [native code]\n}")
    public void svgDefsElement() throws Exception {
        test("SVGDefsElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGDescElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGDescElement() { [native code] }",
            IE = "[object SVGDescElement]",
            FF = "function SVGDescElement() {\n    [native code]\n}")
    public void svgDescElement() throws Exception {
        test("SVGDescElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGElement() { [native code] }",
            IE = "[object SVGElement]",
            FF = "function SVGElement() {\n    [native code]\n}")
    public void svgElement() throws Exception {
        test("SVGElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGEllipseElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGEllipseElement() { [native code] }",
            IE = "[object SVGEllipseElement]",
            FF = "function SVGEllipseElement() {\n    [native code]\n}")
    public void svgEllipseElement() throws Exception {
        test("SVGEllipseElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEBlendElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEBlendElement() { [native code] }",
            IE = "[object SVGFEBlendElement]",
            FF = "function SVGFEBlendElement() {\n    [native code]\n}")
    public void svgFEBlendElement() throws Exception {
        test("SVGFEBlendElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEColorMatrixElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEColorMatrixElement() { [native code] }",
            IE = "[object SVGFEColorMatrixElement]",
            FF = "function SVGFEColorMatrixElement() {\n    [native code]\n}")
    public void svgFEColorMatrixElement() throws Exception {
        test("SVGFEColorMatrixElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEComponentTransferElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEComponentTransferElement() { [native code] }",
            IE = "[object SVGFEComponentTransferElement]",
            FF = "function SVGFEComponentTransferElement() {\n    [native code]\n}")
    public void svgFEComponentTransferElement() throws Exception {
        test("SVGFEComponentTransferElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFECompositeElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFECompositeElement() { [native code] }",
            IE = "[object SVGFECompositeElement]",
            FF = "function SVGFECompositeElement() {\n    [native code]\n}")
    public void svgFECompositeElement() throws Exception {
        test("SVGFECompositeElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEConvolveMatrixElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEConvolveMatrixElement() { [native code] }",
            IE = "[object SVGFEConvolveMatrixElement]",
            FF = "function SVGFEConvolveMatrixElement() {\n    [native code]\n}")
    public void svgFEConvolveMatrixElement() throws Exception {
        test("SVGFEConvolveMatrixElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEDiffuseLightingElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEDiffuseLightingElement() { [native code] }",
            IE = "[object SVGFEDiffuseLightingElement]",
            FF = "function SVGFEDiffuseLightingElement() {\n    [native code]\n}")
    public void svgFEDiffuseLightingElement() throws Exception {
        test("SVGFEDiffuseLightingElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEDisplacementMapElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEDisplacementMapElement() { [native code] }",
            IE = "[object SVGFEDisplacementMapElement]",
            FF = "function SVGFEDisplacementMapElement() {\n    [native code]\n}")
    public void svgFEDisplacementMapElement() throws Exception {
        test("SVGFEDisplacementMapElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEDistantLightElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEDistantLightElement() { [native code] }",
            IE = "[object SVGFEDistantLightElement]",
            FF = "function SVGFEDistantLightElement() {\n    [native code]\n}")
    public void svgFEDistantLightElement() throws Exception {
        test("SVGFEDistantLightElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFloodElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEFloodElement() { [native code] }",
            IE = "[object SVGFEFloodElement]",
            FF = "function SVGFEFloodElement() {\n    [native code]\n}")
    public void svgFEFloodElement() throws Exception {
        test("SVGFEFloodElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFuncAElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEFuncAElement() { [native code] }",
            IE = "[object SVGFEFuncAElement]",
            FF = "function SVGFEFuncAElement() {\n    [native code]\n}")
    public void svgFEFuncAElement() throws Exception {
        test("SVGFEFuncAElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFuncBElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEFuncBElement() { [native code] }",
            IE = "[object SVGFEFuncBElement]",
            FF = "function SVGFEFuncBElement() {\n    [native code]\n}")
    public void svgFEFuncBElement() throws Exception {
        test("SVGFEFuncBElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFuncGElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEFuncGElement() { [native code] }",
            IE = "[object SVGFEFuncGElement]",
            FF = "function SVGFEFuncGElement() {\n    [native code]\n}")
    public void svgFEFuncGElement() throws Exception {
        test("SVGFEFuncGElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFuncRElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEFuncRElement() { [native code] }",
            IE = "[object SVGFEFuncRElement]",
            FF = "function SVGFEFuncRElement() {\n    [native code]\n}")
    public void svgFEFuncRElement() throws Exception {
        test("SVGFEFuncRElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEGaussianBlurElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEGaussianBlurElement() { [native code] }",
            IE = "[object SVGFEGaussianBlurElement]",
            FF = "function SVGFEGaussianBlurElement() {\n    [native code]\n}")
    public void svgFEGaussianBlurElement() throws Exception {
        test("SVGFEGaussianBlurElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEImageElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEImageElement() { [native code] }",
            IE = "[object SVGFEImageElement]",
            FF = "function SVGFEImageElement() {\n    [native code]\n}")
    public void svgFEImageElement() throws Exception {
        test("SVGFEImageElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEMergeElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEMergeElement() { [native code] }",
            IE = "[object SVGFEMergeElement]",
            FF = "function SVGFEMergeElement() {\n    [native code]\n}")
    public void svgFEMergeElement() throws Exception {
        test("SVGFEMergeElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEMergeNodeElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEMergeNodeElement() { [native code] }",
            IE = "[object SVGFEMergeNodeElement]",
            FF = "function SVGFEMergeNodeElement() {\n    [native code]\n}")
    public void svgFEMergeNodeElement() throws Exception {
        test("SVGFEMergeNodeElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEMorphologyElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEMorphologyElement() { [native code] }",
            IE = "[object SVGFEMorphologyElement]",
            FF = "function SVGFEMorphologyElement() {\n    [native code]\n}")
    public void svgFEMorphologyElement() throws Exception {
        test("SVGFEMorphologyElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEOffsetElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEOffsetElement() { [native code] }",
            IE = "[object SVGFEOffsetElement]",
            FF = "function SVGFEOffsetElement() {\n    [native code]\n}")
    public void svgFEOffsetElement() throws Exception {
        test("SVGFEOffsetElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEPointLightElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEPointLightElement() { [native code] }",
            IE = "[object SVGFEPointLightElement]",
            FF = "function SVGFEPointLightElement() {\n    [native code]\n}")
    public void svgFEPointLightElement() throws Exception {
        test("SVGFEPointLightElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFESpecularLightingElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFESpecularLightingElement() { [native code] }",
            IE = "[object SVGFESpecularLightingElement]",
            FF = "function SVGFESpecularLightingElement() {\n    [native code]\n}")
    public void svgFESpecularLightingElement() throws Exception {
        test("SVGFESpecularLightingElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFESpotLightElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFESpotLightElement() { [native code] }",
            IE = "[object SVGFESpotLightElement]",
            FF = "function SVGFESpotLightElement() {\n    [native code]\n}")
    public void svgFESpotLightElement() throws Exception {
        test("SVGFESpotLightElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFETileElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFETileElement() { [native code] }",
            IE = "[object SVGFETileElement]",
            FF = "function SVGFETileElement() {\n    [native code]\n}")
    public void svgFETileElement() throws Exception {
        test("SVGFETileElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFETurbulenceElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFETurbulenceElement() { [native code] }",
            IE = "[object SVGFETurbulenceElement]",
            FF = "function SVGFETurbulenceElement() {\n    [native code]\n}")
    public void svgFETurbulenceElement() throws Exception {
        test("SVGFETurbulenceElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFilterElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFilterElement() { [native code] }",
            IE = "[object SVGFilterElement]",
            FF = "function SVGFilterElement() {\n    [native code]\n}")
    public void svgFilterElement() throws Exception {
        test("SVGFilterElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGForeignObjectElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGForeignObjectElement() { [native code] }",
            IE = "exception",
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
    @Alerts(DEFAULT = "function SVGGElement() { [native code] }",
            IE = "[object SVGGElement]",
            FF = "function SVGGElement() {\n    [native code]\n}")
    public void svgGElement() throws Exception {
        test("SVGGElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGImageElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGImageElement() { [native code] }",
            IE = "[object SVGImageElement]",
            FF = "function SVGImageElement() {\n    [native code]\n}")
    public void svgImageElement() throws Exception {
        test("SVGImageElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGLineElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGLineElement() { [native code] }",
            IE = "[object SVGLineElement]",
            FF = "function SVGLineElement() {\n    [native code]\n}")
    public void svgLineElement() throws Exception {
        test("SVGLineElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGLinearGradientElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGLinearGradientElement() { [native code] }",
            IE = "[object SVGLinearGradientElement]",
            FF = "function SVGLinearGradientElement() {\n    [native code]\n}")
    public void svgLinearGradientElement() throws Exception {
        test("SVGLinearGradientElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMarkerElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGMarkerElement() { [native code] }",
            IE = "[object SVGMarkerElement]",
            FF = "function SVGMarkerElement() {\n    [native code]\n}")
    public void svgMarkerElement() throws Exception {
        test("SVGMarkerElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMaskElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGMaskElement() { [native code] }",
            IE = "[object SVGMaskElement]",
            FF = "function SVGMaskElement() {\n    [native code]\n}")
    public void svgMaskElement() throws Exception {
        test("SVGMaskElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMatrix}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGMatrix() { [native code] }",
            IE = "[object SVGMatrix]",
            FF = "function SVGMatrix() {\n    [native code]\n}")
    public void svgMatrix() throws Exception {
        test("SVGMatrix");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMetadataElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGMetadataElement() { [native code] }",
            IE = "[object SVGMetadataElement]",
            FF = "function SVGMetadataElement() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "function SVGPathElement() { [native code] }",
            IE = "[object SVGPathElement]",
            FF = "function SVGPathElement() {\n    [native code]\n}")
    public void svgPathElement() throws Exception {
        test("SVGPathElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPatternElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGPatternElement() { [native code] }",
            IE = "[object SVGPatternElement]",
            FF = "function SVGPatternElement() {\n    [native code]\n}")
    public void svgPatternElement() throws Exception {
        test("SVGPatternElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPolygonElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGPolygonElement() { [native code] }",
            IE = "[object SVGPolygonElement]",
            FF = "function SVGPolygonElement() {\n    [native code]\n}")
    public void svgPolygonElement() throws Exception {
        test("SVGPolygonElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPolylineElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGPolylineElement() { [native code] }",
            IE = "[object SVGPolylineElement]",
            FF = "function SVGPolylineElement() {\n    [native code]\n}")
    public void svgPolylineElement() throws Exception {
        test("SVGPolylineElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGRadialGradientElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGRadialGradientElement() { [native code] }",
            IE = "[object SVGRadialGradientElement]",
            FF = "function SVGRadialGradientElement() {\n    [native code]\n}")
    public void svgRadialGradientElement() throws Exception {
        test("SVGRadialGradientElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGRect}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGRect() { [native code] }",
            IE = "[object SVGRect]",
            FF = "function SVGRect() {\n    [native code]\n}")
    public void svgRect() throws Exception {
        test("SVGRect");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGRectElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGRectElement() { [native code] }",
            IE = "[object SVGRectElement]",
            FF = "function SVGRectElement() {\n    [native code]\n}")
    public void svgRectElement() throws Exception {
        test("SVGRectElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGSVGElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGSVGElement() { [native code] }",
            IE = "[object SVGSVGElement]",
            FF = "function SVGSVGElement() {\n    [native code]\n}")
    public void svgSVGElement() throws Exception {
        test("SVGSVGElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGScriptElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGScriptElement() { [native code] }",
            IE = "[object SVGScriptElement]",
            FF = "function SVGScriptElement() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "function SVGStopElement() { [native code] }",
            IE = "[object SVGStopElement]",
            FF = "function SVGStopElement() {\n    [native code]\n}")
    public void svgStopElement() throws Exception {
        test("SVGStopElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGStyleElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGStyleElement() { [native code] }",
            IE = "[object SVGStyleElement]",
            FF = "function SVGStyleElement() {\n    [native code]\n}")
    public void svgStyleElement() throws Exception {
        test("SVGStyleElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGSwitchElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGSwitchElement() { [native code] }",
            IE = "[object SVGSwitchElement]",
            FF = "function SVGSwitchElement() {\n    [native code]\n}")
    public void svgSwitchElement() throws Exception {
        test("SVGSwitchElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGSymbolElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGSymbolElement() { [native code] }",
            IE = "[object SVGSymbolElement]",
            FF = "function SVGSymbolElement() {\n    [native code]\n}")
    public void svgSymbolElement() throws Exception {
        test("SVGSymbolElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTSpanElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGTSpanElement() { [native code] }",
            IE = "[object SVGTSpanElement]",
            FF = "function SVGTSpanElement() {\n    [native code]\n}")
    public void svgTSpanElement() throws Exception {
        test("SVGTSpanElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTextElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGTextElement() { [native code] }",
            IE = "[object SVGTextElement]",
            FF = "function SVGTextElement() {\n    [native code]\n}")
    public void svgTextElement() throws Exception {
        test("SVGTextElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTextPathElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGTextPathElement() { [native code] }",
            IE = "[object SVGTextPathElement]",
            FF = "function SVGTextPathElement() {\n    [native code]\n}")
    public void svgTextPathElement() throws Exception {
        test("SVGTextPathElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTitleElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGTitleElement() { [native code] }",
            IE = "[object SVGTitleElement]",
            FF = "function SVGTitleElement() {\n    [native code]\n}")
    public void svgTitleElement() throws Exception {
        test("SVGTitleElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGUseElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGUseElement() { [native code] }",
            IE = "[object SVGUseElement]",
            FF = "function SVGUseElement() {\n    [native code]\n}")
    public void svgUseElement() throws Exception {
        test("SVGUseElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.svg.SVGViewElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGViewElement() { [native code] }",
            IE = "[object SVGViewElement]",
            FF = "function SVGViewElement() {\n    [native code]\n}")
    public void svgViewElement() throws Exception {
        test("SVGViewElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Screen}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Screen() { [native code] }",
            IE = "[object Screen]",
            FF = "function Screen() {\n    [native code]\n}")
    public void screen() throws Exception {
        test("Screen");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.Selection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Selection() { [native code] }",
            IE = "[object Selection]",
            FF = "function Selection() {\n    [native code]\n}")
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
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void staticNodeList() throws Exception {
        test("StaticNodeList");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Storage}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Storage() { [native code] }",
            FF = "function Storage() {\n    [native code]\n}",
            IE  = "[object Storage]")
    public void storage() throws Exception {
        test("Storage");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.StyleSheetList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function StyleSheetList() { [native code] }",
            IE = "[object StyleSheetList]",
            FF = "function StyleSheetList() {\n    [native code]\n}")
    public void styleSheetList() throws Exception {
        test("StyleSheetList");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.Text}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Text() { [native code] }",
            IE = "[object Text]",
            FF = "function Text() {\n    [native code]\n}")
    public void text() throws Exception {
        test("Text");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.TextRange}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
        IE = "[object TextRange]")
    public void textRange() throws Exception {
        test("TextRange");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.TreeWalker}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function TreeWalker() { [native code] }",
            IE = "[object TreeWalker]",
            FF = "function TreeWalker() {\n    [native code]\n}")
    public void treeWalker() throws Exception {
        test("TreeWalker");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.UIEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function UIEvent() { [native code] }",
            IE = "[object UIEvent]",
            FF = "function UIEvent() {\n    [native code]\n}")
    public void uiEvent() throws Exception {
        test("UIEvent");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Uint16Array}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Uint16Array() { [native code] }",
            FF = "function Uint16Array() {\n    [native code]\n}",
            IE = "\nfunction Uint16Array() {\n    [native code]\n}\n")
    public void uint16Array() throws Exception {
        test("Uint16Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Uint32Array}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Uint32Array() { [native code] }",
            FF = "function Uint32Array() {\n    [native code]\n}",
            IE = "\nfunction Uint32Array() {\n    [native code]\n}\n")
    public void uint32Array() throws Exception {
        test("Uint32Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Uint8Array}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Uint8Array() { [native code] }",
            FF = "function Uint8Array() {\n    [native code]\n}",
            IE = "\nfunction Uint8Array() {\n    [native code]\n}\n")
    public void uint8Array() throws Exception {
        test("Uint8Array");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.arrays.Uint8ClampedArray}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Uint8ClampedArray() { [native code] }",
            FF = "function Uint8ClampedArray() {\n    [native code]\n}",
            IE = "\nfunction Uint8ClampedArray() {\n    [native code]\n}\n")
    public void uint8ClampedArray() throws Exception {
        test("Uint8ClampedArray");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.WebSocket}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function WebSocket() { [native code] }",
            FF = "function WebSocket() {\n    [native code]\n}",
            IE = "\nfunction WebSocket() {\n    [native code]\n}\n")
    public void webSocket() throws Exception {
        test("WebSocket");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Window}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Window() { [native code] }",
            IE = "[object Window]",
            FF = "function Window() {\n    [native code]\n}")
    public void window() throws Exception {
        test("Window");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function XMLDocument() { [native code] }",
            IE = "[object XMLDocument]",
            FF = "function XMLDocument() {\n    [native code]\n}")
    public void xmlDocument() throws Exception {
        test("XMLDocument");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.xml.XMLHttpRequest}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function XMLHttpRequest() { [native code] }",
            FF = "function XMLHttpRequest() {\n    [native code]\n}",
            IE = "\nfunction XMLHttpRequest() {\n    [native code]\n}\n")
    public void xmlHttpRequest() throws Exception {
        test("XMLHttpRequest");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.xml.XMLSerializer}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function XMLSerializer() { [native code] }",
            FF = "function XMLSerializer() {\n    [native code]\n}",
            IE = "\nfunction XMLSerializer() {\n    [native code]\n}\n")
    public void xmlSerializer() throws Exception {
        test("XMLSerializer");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.XPathEvaluator}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function XPathEvaluator() { [native code] }",
            IE = "exception",
            FF = "function XPathEvaluator() {\n    [native code]\n}")
    public void xPathEvaluator() throws Exception {
        test("XPathEvaluator");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.XPathNSResolver}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            EDGE = "function XPathNSResolver() { [native code] }")
    public void xPathNSResolver() throws Exception {
        test("XPathNSResolver");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.XPathResult}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function XPathResult() { [native code] }",
            FF = "function XPathResult() {\n    [native code]\n}",
            IE = "exception")
    public void xPathResult() throws Exception {
        test("XPathResult");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.xml.XSLTProcessor}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function XSLTProcessor() { [native code] }",
            FF = "function XSLTProcessor() {\n    [native code]\n}",
            IE = "exception")
    public void xsltProcessor() throws Exception {
        test("XSLTProcessor");
    }

    /**
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
            FF = "function DOMRect() {\n    [native code]\n}")
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
            IE = "[object MSStyleCSSProperties]")
    @NotYetImplemented(IE)
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
            IE = "[object MSCurrentStyleCSSProperties]")
    @NotYetImplemented(IE)
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
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.ClientRectList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function ClientRectList() { [native code] }",
            FF = "exception",
            IE = "[object ClientRectList]")
    public void clientRectList() throws Exception {
        test("ClientRectList");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function SVGDocument() {\n    [native code]\n}")
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
    @Alerts("[object Arguments]")
    public void arguments() throws Exception {
        test("arguments");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Notification}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function Notification() { [native code] }",
            FF = "function Notification() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "function HTMLTrackElement() { [native code] }",
            FF = "function HTMLTrackElement() {\n    [native code]\n}",
            IE = "[object HTMLTrackElement]")
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

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.URLSearchParams}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function URLSearchParams() { [native code] }",
            FF = "function URLSearchParams() {\n    [native code]\n}",
            IE = "exception")
    public void urlSearchParams() throws Exception {
        test("URLSearchParams");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.intl.Intl}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Object]")
    public void intl() throws Exception {
        test("Intl");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.xml.FormData}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function FormData() { [native code] }",
            FF = "function FormData() {\n    [native code]\n}",
            IE = "\nfunction FormData() {\n    [native code]\n}\n")
    public void formData() throws Exception {
        test("FormData");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.MessageChannel}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function MessageChannel() { [native code] }",
            FF = "exception",
            IE = "\nfunction MessageChannel() {\n    [native code]\n}\n",
            FF45 = "function MessageChannel() {\n    [native code]\n}")
    public void messageChannel() throws Exception {
        test("MessageChannel");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.MessagePort}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function MessagePort() { [native code] }",
            FF = "function MessagePort() {\n    [native code]\n}",
            IE = "[object MessagePort]")
    public void messagePort() throws Exception {
        test("MessagePort");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Promise}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Promise() { [native code] }",
            FF = "function Promise() {\n    [native code]\n}",
            IE = "exception")
    public void promise() throws Exception {
        test("Promise");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.worker.Worker}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Worker() { [native code] }",
            FF = "function Worker() {\n    [native code]\n}",
            IE = "\nfunction Worker() {\n    [native code]\n}\n")
    public void worker() throws Exception {
        test("Worker");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.SharedWorker}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SharedWorker() { [native code] }",
            FF = "function SharedWorker() {\n    [native code]\n}")
    public void sharedWorker() throws Exception {
        test("SharedWorker");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimatedLengthList() { [native code] }",
            FF = "function SVGAnimatedLengthList() {\n    [native code]\n}",
            IE = "[object SVGAnimatedLengthList]")
    public void svgAnimatedLengthList() throws Exception {
        test("SVGAnimatedLengthList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function Iterator() {\n    [native code]\n}")
    public void iterator() throws Exception {
        test("Iterator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function RegExp() { [native code] }",
            FF = "function RegExp() {\n    [native code]\n}",
            IE = "\nfunction RegExp() {\n    [native code]\n}\n")
    public void regExp() throws Exception {
        test("RegExp");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgMissingGlyphElement() throws Exception {
        test("SVGMissingGlyphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void transferable() throws Exception {
        test("Transferable");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.media.AnalyserNode}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function AnalyserNode() { [native code] }",
            FF = "function AnalyserNode() {\n    [native code]\n}",
            IE = "exception")
    public void analyserNode() throws Exception {
        test("AnalyserNode");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSPageRule}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CSSPageRule() { [native code] }",
            FF = "[object CSSPageRule]",
            IE = "[object CSSPageRule]")
    public void cssPageRule() throws Exception {
        test("CSSPageRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void idbVersionChangeRequest() throws Exception {
        test("IDBVersionChangeRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgFontFaceSrcElement() throws Exception {
        test("SVGFontFaceSrcElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgTests() throws Exception {
        test("SVGTests");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void point() throws Exception {
        test("Point");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.canvas.Path2D}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function Path2D() { [native code] }",
            FF = "function Path2D() {\n    [native code]\n}")
    public void path2D() throws Exception {
        test("Path2D");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void generatorFunction() throws Exception {
        test("GeneratorFunction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozActivity() throws Exception {
        test("MozActivity");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLShadowElement}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function HTMLShadowElement() { [native code] }",
            FF = "function HTMLShadowElement() {\n    [native code]\n}")
    public void htmlShadowElement() throws Exception {
        test("HTMLShadowElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.ShadowRoot}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function ShadowRoot() { [native code] }")
    public void shadowRoot() throws Exception {
        test("ShadowRoot");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void fileReaderSync() throws Exception {
        test("FileReaderSync");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.DOMCursor}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function DOMCursor() {\n    [native code]\n}")
    public void domCursor() throws Exception {
        test("DOMCursor");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozNetworkStatsManager() throws Exception {
        test("MozNetworkStatsManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozMobileICCInfo() throws Exception {
        test("MozMobileICCInfo");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.media.LocalMediaStream}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function LocalMediaStream() {\n    [native code]\n}")
    public void localMediaStream() throws Exception {
        test("LocalMediaStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void typeInfo() throws Exception {
        test("TypeInfo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void nonDocumentTypeChildNode() throws Exception {
        test("NonDocumentTypeChildNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void index() throws Exception {
        test("Index");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domApplicationsManager() throws Exception {
        test("DOMApplicationsManager");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.media.AudioParam}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function AudioParam() { [native code] }",
            IE = "exception",
            FF = "function AudioParam() {\n    [native code]\n}")
    public void audioParam() throws Exception {
        test("AudioParam");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void sharedWorkerGlobalScope() throws Exception {
        test("SharedWorkerGlobalScope");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function decodeURIComponent() { [native code] }",
            FF = "function decodeURIComponent() {\n    [native code]\n}",
            IE = "\nfunction decodeURIComponent() {\n    [native code]\n}\n")
    public void decodeURIComponent() throws Exception {
        test("decodeURIComponent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function Headers() { [native code] }",
            FF45 = "function Headers() {\n    [native code]\n}")
    public void headers() throws Exception {
        test("Headers");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.media.ChannelMergerNode}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ChannelMergerNode() { [native code] }",
            IE = "exception",
            FF = "function ChannelMergerNode() {\n    [native code]\n}")
    public void channelMergerNode() throws Exception {
        test("ChannelMergerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void portCollection() throws Exception {
        test("PortCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void idbEnvironment() throws Exception {
        test("IDBEnvironment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CloseEvent() { [native code] }",
            FF = "function CloseEvent() {\n    [native code]\n}",
            IE = "[object CloseEvent]")
    public void closeEvent() throws Exception {
        test("CloseEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void cameraCapabilities() throws Exception {
        test("CameraCapabilities");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgHKernElement() throws Exception {
        test("SVGHKernElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function OfflineAudioContext() { [native code] }",
            IE = "exception",
            FF = "function OfflineAudioContext() {\n    [native code]\n}")
    public void offlineAudioContext() throws Exception {
        test("OfflineAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF38 = "[object MozMobileMessageThread]")
    public void mozMobileMessageThread() throws Exception {
        test("MozMobileMessageThread");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function RTCSessionDescription() { [native code] }",
            FF45 = "function RTCSessionDescription() {\n    [native code]\n}")
    public void rtcSessionDescription() throws Exception {
        test("RTCSessionDescription");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozSmsEvent() throws Exception {
        test("MozSmsEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void idbDatabaseSync() throws Exception {
        test("IDBDatabaseSync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void installEvent() throws Exception {
        test("InstallEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void bluetoothDeviceEvent() throws Exception {
        test("BluetoothDeviceEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimatedRect() { [native code] }",
            FF = "function SVGAnimatedRect() {\n    [native code]\n}",
            IE = "[object SVGAnimatedRect]")
    public void svgAnimatedRect() throws Exception {
        test("SVGAnimatedRect");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void userDataHandler() throws Exception {
        test("UserDataHandler");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void rtcIdentityEvent() throws Exception {
        test("RTCIdentityEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void extendableEvent() throws Exception {
        test("ExtendableEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ProgressEvent() { [native code] }",
            FF = "function ProgressEvent() {\n    [native code]\n}",
            IE = "[object ProgressEvent]")
    public void progressEvent() throws Exception {
        test("ProgressEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MediaKeySession() { [native code] }",
            FF45 = "function MediaKeySession() {\n    [native code]\n}")
    public void mediaKeySession() throws Exception {
        test("MediaKeySession");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void l10n_language_direction() throws Exception {
        test("L10n.language.direction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaRecorder() { [native code] }",
            FF = "function MediaRecorder() {\n    [native code]\n}",
            IE = "exception")
    public void mediaRecorder() throws Exception {
        test("MediaRecorder");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimatedString() { [native code] }",
            FF = "function SVGAnimatedString() {\n    [native code]\n}",
            IE = "[object SVGAnimatedString]")
    public void svgAnimatedString() throws Exception {
        test("SVGAnimatedString");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function TimeEvent() {\n    [native code]\n}")
    public void timeEvent() throws Exception {
        test("TimeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IDBDatabase() { [native code] }",
            FF = "function IDBDatabase() {\n    [native code]\n}",
            IE = "[object IDBDatabase]")
    public void idbDatabase() throws Exception {
        test("IDBDatabase");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domLocator() throws Exception {
        test("DOMLocator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimatedEnumeration() { [native code] }",
            FF = "function SVGAnimatedEnumeration() {\n    [native code]\n}",
            IE = "[object SVGAnimatedEnumeration]")
    public void svgAnimatedEnumeration() throws Exception {
        test("SVGAnimatedEnumeration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function Response() { [native code] }",
            FF45 = "function Response() {\n    [native code]\n}")
    public void response() throws Exception {
        test("Response");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozIccManager() throws Exception {
        test("MozIccManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IDBIndex() { [native code] }",
            FF = "function IDBIndex() {\n    [native code]\n}",
            IE = "[object IDBIndex]")
    public void idbIndex() throws Exception {
        test("IDBIndex");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WheelEvent() { [native code] }",
            FF = "function WheelEvent() {\n    [native code]\n}",
            IE = "[object WheelEvent]")
    public void wheelEvent() throws Exception {
        test("WheelEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void globalFetch() throws Exception {
        test("GlobalFetch");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF45 = "function VideoPlaybackQuality() {\n    [native code]\n}",
            EDGE = "function VideoPlaybackQuality() { [native code] }")
    public void videoPlaybackQuality() throws Exception {
        test("VideoPlaybackQuality");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void fileEntry() throws Exception {
        test("FileEntry");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimatedNumberList() { [native code] }",
            FF = "function SVGAnimatedNumberList() {\n    [native code]\n}",
            IE = "[object SVGAnimatedNumberList]")
    public void svgAnimatedNumberList() throws Exception {
        test("SVGAnimatedNumberList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void parallelArray() throws Exception {
        test("ParallelArray");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function TextEncoder() { [native code] }",
            FF = "function TextEncoder() {\n    [native code]\n}")
    public void textEncoder() throws Exception {
        test("TextEncoder");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void l10n_formatValue() throws Exception {
        test("L10n.formatValue");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.idb.IDBFactory}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IDBFactory() { [native code] }",
            FF = "function IDBFactory() {\n    [native code]\n}",
            IE = "[object IDBFactory]")
    public void idbFactory() throws Exception {
        test("IDBFactory");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgTransformable() throws Exception {
        test("SVGTransformable");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void fileEntrySync() throws Exception {
        test("FileEntrySync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PushManager() { [native code] }")
    public void pushManager() throws Exception {
        test("PushManager");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.AnimationEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function AnimationEvent() { [native code] }",
            FF = "function AnimationEvent() {\n    [native code]\n}",
            IE = "[object AnimationEvent]")
    public void animationEvent() throws Exception {
        test("AnimationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domErrorHandler() throws Exception {
        test("DOMErrorHandler");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function () { [native code] }",
            EDGE = "function DateTimeFormat() { [native code] }",
            FF = "function DateTimeFormat() {\n    [native code]\n}",
            IE = "\nfunction DateTimeFormat() {\n    [native code]\n}\n")
    public void intl_DateTimeFormat() throws Exception {
        test("Intl.DateTimeFormat");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void urlUtilsReadOnly() throws Exception {
        test("URLUtilsReadOnly");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PushSubscription() { [native code] }")
    public void pushSubscription() throws Exception {
        test("PushSubscription");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void entityReference() throws Exception {
        test("EntityReference");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function () { [native code] }",
            EDGE = "function Collator() { [native code] }",
            FF = "function Collator() {\n    [native code]\n}",
            IE = "\nfunction Collator() {\n    [native code]\n}\n")
    public void intl_Collator() throws Exception {
        test("Intl.Collator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozSmsFilter() throws Exception {
        test("MozSmsFilter");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MutationObserver() { [native code] }",
            FF = "function MutationObserver() {\n    [native code]\n}",
            IE = "\nfunction MutationObserver() {\n    [native code]\n}\n")
    public void mutationObserver() throws Exception {
        test("MutationObserver");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MutationRecord() { [native code] }",
            FF = "function MutationRecord() {\n    [native code]\n}",
            IE = "[object MutationRecord]")
    public void mutationRecord() throws Exception {
        test("MutationRecord");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozWifiP2pGroupOwner() throws Exception {
        test("MozWifiP2pGroupOwner");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void pushEvent() throws Exception {
        test("PushEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void navigatorID() throws Exception {
        test("NavigatorID");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function DOMMatrixReadOnly() {\n    [native code]\n}")
    public void domMatrixReadOnly() throws Exception {
        test("DOMMatrixReadOnly");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void idbEnvironmentSync() throws Exception {
        test("IDBEnvironmentSync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mediaQueryListListener() throws Exception {
        test("MediaQueryListListener");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function isFinite() { [native code] }",
            FF = "function isFinite() {\n    [native code]\n}",
            IE = "\nfunction isFinite() {\n    [native code]\n}\n")
    public void isFinite() throws Exception {
        test("isFinite");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void fetchEvent() throws Exception {
        test("FetchEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function String() { [native code] }",
            FF = "function String() {\n    [native code]\n}",
            IE = "\nfunction String() {\n    [native code]\n}\n")
    public void string() throws Exception {
        test("String");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void rtcIdentityErrorEvent() throws Exception {
        test("RTCIdentityErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function StyleSheet() { [native code] }",
            FF = "function StyleSheet() {\n    [native code]\n}",
            IE = "[object StyleSheet]")
    public void styleSheet() throws Exception {
        test("StyleSheet");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function TouchEvent() { [native code] }")
    public void touchEvent() throws Exception {
        test("TouchEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozActivityOptions() throws Exception {
        test("MozActivityOptions");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void fileException() throws Exception {
        test("FileException");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPreserveAspectRatio() { [native code] }",
            FF = "function SVGPreserveAspectRatio() {\n    [native code]\n}",
            IE = "[object SVGPreserveAspectRatio]")
    public void svgPreserveAspectRatio() throws Exception {
        test("SVGPreserveAspectRatio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function parseFloat() { [native code] }",
            FF = "function parseFloat() {\n    [native code]\n}",
            IE = "\nfunction parseFloat() {\n    [native code]\n}\n")
    public void parseFloat() throws Exception {
        test("parseFloat");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domImplementationSource() throws Exception {
        test("DOMImplementationSource");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void entity() throws Exception {
        test("Entity");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozNFCTag() throws Exception {
        test("MozNFCTag");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void canvasImageSource() throws Exception {
        test("CanvasImageSource");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void bluetoothDevice() throws Exception {
        test("BluetoothDevice");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function MozContactChangeEvent() {\n    [native code]\n}")
    public void mozContactChangeEvent() throws Exception {
        test("MozContactChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function parseInt() { [native code] }",
            FF = "function parseInt() {\n    [native code]\n}",
            IE = "\nfunction parseInt() {\n    [native code]\n}\n")
    public void parseInt() throws Exception {
        test("parseInt");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void lockedFile() throws Exception {
        test("LockedFile");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function AudioProcessingEvent() { [native code] }",
            IE = "exception",
            FF = "function AudioProcessingEvent() {\n    [native code]\n}")
    public void audioProcessingEvent() throws Exception {
        test("AudioProcessingEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CanvasGradient() { [native code] }",
            FF = "function CanvasGradient() {\n    [native code]\n}",
            IE = "[object CanvasGradient]")
    public void canvasGradient() throws Exception {
        test("CanvasGradient");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgAnimatedPoints() throws Exception {
        test("SVGAnimatedPoints");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function FileReader() { [native code] }",
            FF = "function FileReader() {\n    [native code]\n}",
            IE = "\nfunction FileReader() {\n    [native code]\n}\n")
    public void fileReader() throws Exception {
        test("FileReader");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void byteString() throws Exception {
        test("ByteString");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void networkInformation() throws Exception {
        test("NetworkInformation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void callEvent() throws Exception {
        test("CallEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function AudioNode() { [native code] }",
            IE = "exception",
            FF = "function AudioNode() {\n    [native code]\n}")
    public void audioNode() throws Exception {
        test("AudioNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozNFC() throws Exception {
        test("MozNFC");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function BiquadFilterNode() { [native code] }",
            IE = "exception",
            FF = "function BiquadFilterNode() {\n    [native code]\n}")
    public void biquadFilterNode() throws Exception {
        test("BiquadFilterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function RTCPeerConnectionIceEvent() {\n    [native code]\n}")
    public void rtcPeerConnectionIceEvent() throws Exception {
        test("RTCPeerConnectionIceEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void navigatorPlugins() throws Exception {
        test("NavigatorPlugins");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void powerManager() throws Exception {
        test("PowerManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void l10n_readyState() throws Exception {
        test("L10n.readyState");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function TimeRanges() { [native code] }",
            FF = "function TimeRanges() {\n    [native code]\n}",
            IE = "[object TimeRanges]")
    public void timeRanges() throws Exception {
        test("TimeRanges");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function AudioContext() { [native code] }",
            IE = "exception",
            FF = "function AudioContext() {\n    [native code]\n}")
    public void audioContext() throws Exception {
        test("AudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void rtcConfiguration() throws Exception {
        test("RTCConfiguration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozWifiStatusChangeEvent() throws Exception {
        test("MozWifiStatusChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function RangeError() { [native code] }",
            FF = "function RangeError() {\n    [native code]\n}",
            IE = "\nfunction RangeError() {\n    [native code]\n}\n")
    public void rangeError() throws Exception {
        test("RangeError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void notation() throws Exception {
        test("Notation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void rtcDataChannel() throws Exception {
        test("RTCDataChannel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimatedLength() { [native code] }",
            FF = "function SVGAnimatedLength() {\n    [native code]\n}",
            IE = "[object SVGAnimatedLength]")
    public void svgAnimatedLength() throws Exception {
        test("SVGAnimatedLength");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void workerLocation() throws Exception {
        test("WorkerLocation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CSSSupportsRule() { [native code] }",
            IE = "exception",
            FF = "[object CSSSupportsRule]")
    public void cssSupportsRule() throws Exception {
        test("CSSSupportsRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void parentNode() throws Exception {
        test("ParentNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DynamicsCompressorNode() { [native code] }",
            IE = "exception",
            FF = "function DynamicsCompressorNode() {\n    [native code]\n}")
    public void dynamicsCompressorNode() throws Exception {
        test("DynamicsCompressorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGStringList() { [native code] }",
            FF = "function SVGStringList() {\n    [native code]\n}",
            IE = "[object SVGStringList]")
    public void svgStringList() throws Exception {
        test("SVGStringList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DOMError() { [native code] }",
            FF = "function DOMError() {\n    [native code]\n}",
            IE = "[object DOMError]")
    public void domError() throws Exception {
        test("DOMError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function EventTarget() { [native code] }",
            IE = "exception",
            FF = "function EventTarget() {\n    [native code]\n}")
    public void eventTarget() throws Exception {
        test("EventTarget");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaElementAudioSourceNode() { [native code] }",
            IE = "exception",
            FF = "function MediaElementAudioSourceNode() {\n    [native code]\n}")
    public void mediaElementAudioSourceNode() throws Exception {
        test("MediaElementAudioSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgFontFaceUriElement() throws Exception {
        test("SVGFontFaceUriElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MediaStreamEvent() { [native code] }",
            FF = "function MediaStreamEvent() {\n    [native code]\n}")
    public void mediaStreamEvent() throws Exception {
        test("MediaStreamEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void workerNavigator() throws Exception {
        test("WorkerNavigator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function HTMLFormControlsCollection() { [native code] }",
            FF = "function HTMLFormControlsCollection() {\n    [native code]\n}")
    public void htmlFormControlsCollection() throws Exception {
        test("HTMLFormControlsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgVKernElement() throws Exception {
        test("SVGVKernElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozSocial() throws Exception {
        test("MozSocial");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimatedInteger() { [native code] }",
            FF = "function SVGAnimatedInteger() {\n    [native code]\n}",
            IE = "[object SVGAnimatedInteger]")
    public void svgAnimatedInteger() throws Exception {
        test("SVGAnimatedInteger");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MediaKeyStatusMap() { [native code] }",
            FF45 = "function MediaKeyStatusMap() {\n    [native code]\n}")
    public void mediaKeyStatusMap() throws Exception {
        test("MediaKeyStatusMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozContact() throws Exception {
        test("MozContact");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void typedArray() throws Exception {
        test("TypedArray");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void dataStoreChangeEvent() throws Exception {
        test("DataStoreChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void eventListener() throws Exception {
        test("EventListener");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgFontFaceFormatElement() throws Exception {
        test("SVGFontFaceFormatElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function URIError() { [native code] }",
            FF = "function URIError() {\n    [native code]\n}",
            IE = "\nfunction URIError() {\n    [native code]\n}\n")
    public void uriError() throws Exception {
        test("URIError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void urlUtils() throws Exception {
        test("URLUtils");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void entry() throws Exception {
        test("Entry");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozNetworkStats() throws Exception {
        test("MozNetworkStats");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function HTMLPictureElement() { [native code] }",
            FF = "function HTMLPictureElement() {\n    [native code]\n}")
    public void htmlPictureElement() throws Exception {
        test("HTMLPictureElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void generator() throws Exception {
        test("Generator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgEvent() throws Exception {
        test("SVGEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MediaKeyMessageEvent() { [native code] }",
            FF45 = "function MediaKeyMessageEvent() {\n    [native code]\n}")
    public void mediaKeyMessageEvent() throws Exception {
        test("MediaKeyMessageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaStreamAudioSourceNode() { [native code] }",
            IE = "exception",
            FF = "function MediaStreamAudioSourceNode() {\n    [native code]\n}")
    public void mediaStreamAudioSourceNode() throws Exception {
        test("MediaStreamAudioSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function AudioListener() { [native code] }",
            IE = "exception",
            FF = "function AudioListener() {\n    [native code]\n}")
    public void audioListener() throws Exception {
        test("AudioListener");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function PannerNode() {\n    [native code]\n}",
            EDGE = "function PannerNode() { [native code] }")
    public void pannerNode() throws Exception {
        test("PannerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domConfiguration() throws Exception {
        test("DOMConfiguration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function decodeURI() { [native code] }",
            FF = "function decodeURI() {\n    [native code]\n}",
            IE = "\nfunction decodeURI() {\n    [native code]\n}\n")
    public void decodeURI() throws Exception {
        test("decodeURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MediaKeys() { [native code] }",
            FF45 = "function MediaKeys() {\n    [native code]\n}")
    public void mediaKeys() throws Exception {
        test("MediaKeys");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PerformanceTiming() { [native code] }",
            FF = "function PerformanceTiming() {\n    [native code]\n}",
            IE = "[object PerformanceTiming]")
    public void performanceTiming() throws Exception {
        test("PerformanceTiming");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void bluetoothAdapter() throws Exception {
        test("BluetoothAdapter");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IDBObjectStore() { [native code] }",
            FF = "function IDBObjectStore() {\n    [native code]\n}",
            IE = "[object IDBObjectStore]")
    public void idbObjectStore() throws Exception {
        test("IDBObjectStore");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domImplementationRegistry() throws Exception {
        test("DOMImplementationRegistry");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void telephony() throws Exception {
        test("Telephony");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DeviceMotionEvent() { [native code] }",
            IE = "exception",
            FF = "function DeviceMotionEvent() {\n    [native code]\n}")
    public void deviceMotionEvent() throws Exception {
        test("DeviceMotionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function TouchList() { [native code] }")
    public void touchList() throws Exception {
        test("TouchList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object JSON]")
    public void json() throws Exception {
        test("JSON");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IDBRequest() { [native code] }",
            FF = "function IDBRequest() {\n    [native code]\n}",
            IE = "[object IDBRequest]")
    public void idbRequest() throws Exception {
        test("IDBRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function TextDecoder() { [native code] }",
            FF = "function TextDecoder() {\n    [native code]\n}")
    public void textDecoder() throws Exception {
        test("TextDecoder");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SyntaxError() { [native code] }",
            FF = "function SyntaxError() {\n    [native code]\n}",
            IE = "\nfunction SyntaxError() {\n    [native code]\n}\n")
    public void syntaxError() throws Exception {
        test("SyntaxError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CSSKeyframeRule() { [native code] }",
            FF = "exception",
            IE = "[object CSSKeyframeRule]")
    public void cssKeyframeRule() throws Exception {
        test("CSSKeyframeRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function BroadcastChannel() {\n    [native code]\n}")
    public void broadcastChannel() throws Exception {
        test("BroadcastChannel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void directoryReaderSync() throws Exception {
        test("DirectoryReaderSync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void documentTouch() throws Exception {
        test("DocumentTouch");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozVoicemailEvent() throws Exception {
        test("MozVoicemailEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void navigatorGeolocation() throws Exception {
        test("NavigatorGeolocation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function FocusEvent() { [native code] }",
            FF = "function FocusEvent() {\n    [native code]\n}",
            IE = "[object FocusEvent]")
    public void focusEvent() throws Exception {
        test("FocusEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DOMStringList() { [native code] }",
            FF = "function DOMStringList() {\n    [native code]\n}",
            IE = "[object DOMStringList]")
    public void domStringList() throws Exception {
        test("DOMStringList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozMmsEvent() throws Exception {
        test("MozMmsEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function OfflineAudioCompletionEvent() { [native code] }",
            IE = "exception",
            FF = "function OfflineAudioCompletionEvent() {\n    [native code]\n}")
    public void offlineAudioCompletionEvent() throws Exception {
        test("OfflineAudioCompletionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void elementTraversal() throws Exception {
        test("ElementTraversal");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DeviceOrientationEvent() { [native code] }",
            IE = "exception",
            FF = "function DeviceOrientationEvent() {\n    [native code]\n}")
    public void deviceOrientationEvent() throws Exception {
        test("DeviceOrientationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void uSVString() throws Exception {
        test("USVString");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domImplementationList() throws Exception {
        test("DOMImplementationList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void localFileSystem() throws Exception {
        test("LocalFileSystem");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void tcpServerSocket() throws Exception {
        test("TCPServerSocket");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function uneval() {\n    [native code]\n}")
    public void uneval() throws Exception {
        test("uneval");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function StorageEvent() { [native code] }",
            FF = "function StorageEvent() {\n    [native code]\n}",
            IE = "[object StorageEvent]")
    public void storageEvent() throws Exception {
        test("StorageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IDBOpenDBRequest() { [native code] }",
            FF = "function IDBOpenDBRequest() {\n    [native code]\n}",
            IE = "[object IDBOpenDBRequest]")
    public void idbOpenDBRequest() throws Exception {
        test("IDBOpenDBRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void dataStore() throws Exception {
        test("DataStore");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CSSGroupingRule() { [native code] }",
            IE = "exception",
            FF = "[object CSSGroupingRule]")
    public void cssGroupingRule() throws Exception {
        test("CSSGroupingRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void body() throws Exception {
        test("Body");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("NaN")
    public void naN() throws Exception {
        test("NaN");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function FileError() { [native code] }")
    public void fileError() throws Exception {
        test("FileError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function GainNode() { [native code] }",
            IE = "exception",
            FF = "function GainNode() {\n    [native code]\n}")
    public void gainNode() throws Exception {
        test("GainNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Number() { [native code] }",
            FF = "function Number() {\n    [native code]\n}",
            IE = "\nfunction Number() {\n    [native code]\n}\n")
    public void number() throws Exception {
        test("Number");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Map() { [native code] }",
            FF = "function Map() {\n    [native code]\n}",
            IE = "\nfunction Map() {\n    [native code]\n}\n")
    public void map() throws Exception {
        test("Map");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozMobileConnection() throws Exception {
        test("MozMobileConnection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function Cache() { [native code] }",
            FF45 = "function Cache() {\n    [native code]\n}")
    public void cache() throws Exception {
        test("Cache");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozVoicemail() throws Exception {
        test("MozVoicemail");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGTransformList() { [native code] }",
            FF = "function SVGTransformList() {\n    [native code]\n}",
            IE = "[object SVGTransformList]")
    public void svgTransformList() throws Exception {
        test("SVGTransformList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozNFCPeer() throws Exception {
        test("MozNFCPeer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void directoryEntrySync() throws Exception {
        test("DirectoryEntrySync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void linkStyle() throws Exception {
        test("LinkStyle");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function () { [native code] }",
            EDGE = "function NumberFormat() { [native code] }",
            FF = "function NumberFormat() {\n    [native code]\n}",
            IE = "\nfunction NumberFormat() {\n    [native code]\n}\n")
    public void intl_NumberFormat() throws Exception {
        test("Intl.NumberFormat");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function CaretPosition() {\n    [native code]\n}")
    public void caretPosition() throws Exception {
        test("CaretPosition");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void directoryReader() throws Exception {
        test("DirectoryReader");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function DeviceProximityEvent() {\n    [native code]\n}")
    public void deviceProximityEvent() throws Exception {
        test("DeviceProximityEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IDBVersionChangeEvent() { [native code] }",
            FF = "function IDBVersionChangeEvent() {\n    [native code]\n}",
            IE = "[object IDBVersionChangeEvent]")
    public void idbVersionChangeEvent() throws Exception {
        test("IDBVersionChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void l10n_setAttributes() throws Exception {
        test("L10n.setAttributes");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void settingsManager() throws Exception {
        test("SettingsManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void windowBase64() throws Exception {
        test("WindowBase64");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mediaKeySystemConfiguration() throws Exception {
        test("MediaKeySystemConfiguration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimatedAngle() { [native code] }",
            FF = "function SVGAnimatedAngle() {\n    [native code]\n}",
            IE = "[object SVGAnimatedAngle]")
    public void svgAnimatedAngle() throws Exception {
        test("SVGAnimatedAngle");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozMobileMessageManager() throws Exception {
        test("MozMobileMessageManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ConvolverNode() { [native code] }",
            IE = "exception",
            FF = "function ConvolverNode() {\n    [native code]\n}")
    public void convolverNode() throws Exception {
        test("ConvolverNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CSSKeyframesRule() { [native code] }",
            FF = "exception",
            IE = "[object CSSKeyframesRule]")
    public void cssKeyframesRule() throws Exception {
        test("CSSKeyframesRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WaveShaperNode() { [native code] }",
            IE = "exception",
            FF = "function WaveShaperNode() {\n    [native code]\n}")
    public void waveShaperNode() throws Exception {
        test("WaveShaperNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void blobBuilder() throws Exception {
        test("BlobBuilder");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void rtcSessionDescriptionCallback() throws Exception {
        test("RTCSessionDescriptionCallback");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SubtleCrypto() { [native code] }",
            FF = "function SubtleCrypto() {\n    [native code]\n}",
            IE = "[object SubtleCrypto]")
    public void subtleCrypto() throws Exception {
        test("SubtleCrypto");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozMobileNetworkInfo() throws Exception {
        test("MozMobileNetworkInfo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function DOMMatrix() {\n    [native code]\n}")
    public void domMatrix() throws Exception {
        test("DOMMatrix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void clients() throws Exception {
        test("Clients");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozWifiP2pManager() throws Exception {
        test("MozWifiP2pManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGTransform() { [native code] }",
            FF = "function SVGTransform() {\n    [native code]\n}",
            IE = "[object SVGTransform]")
    public void svgTransform() throws Exception {
        test("SVGTransform");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MediaKeySystemAccess() { [native code] }",
            FF45 = "function MediaKeySystemAccess() {\n    [native code]\n}")
    public void mediaKeySystemAccess() throws Exception {
        test("MediaKeySystemAccess");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function encodeURI() { [native code] }",
            FF = "function encodeURI() {\n    [native code]\n}",
            IE = "\nfunction encodeURI() {\n    [native code]\n}\n")
    public void encodeURI() throws Exception {
        test("encodeURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function EvalError() { [native code] }",
            FF = "function EvalError() {\n    [native code]\n}",
            IE = "\nfunction EvalError() {\n    [native code]\n}\n")
    public void evalError() throws Exception {
        test("EvalError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Math]")
    public void math() throws Exception {
        test("Math");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Array() { [native code] }",
            FF = "function Array() {\n    [native code]\n}",
            IE = "\nfunction Array() {\n    [native code]\n}\n")
    public void array() throws Exception {
        test("Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void renderingContext() throws Exception {
        test("RenderingContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PageTransitionEvent() { [native code] }",
            FF = "function PageTransitionEvent() {\n    [native code]\n}",
            IE = "[object PageTransitionEvent]")
    public void pageTransitionEvent() throws Exception {
        test("PageTransitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimatedNumber() { [native code] }",
            FF = "function SVGAnimatedNumber() {\n    [native code]\n}",
            IE = "[object SVGAnimatedNumber]")
    public void svgAnimatedNumber() throws Exception {
        test("SVGAnimatedNumber");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PeriodicWave() { [native code] }",
            IE = "exception",
            FF = "function PeriodicWave() {\n    [native code]\n}")
    public void periodicWave() throws Exception {
        test("PeriodicWave");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void idbDatabaseException() throws Exception {
        test("IDBDatabaseException");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function DeviceLightEvent() {\n    [native code]\n}")
    public void deviceLightEvent() throws Exception {
        test("DeviceLightEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domUserData() throws Exception {
        test("DOMUserData");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF45 = "function RTCPeerConnection() {\n    [native code]\n}")
    public void rtcPeerConnection() throws Exception {
        test("RTCPeerConnection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CryptoKey() { [native code] }",
            IE = "exception",
            FF = "function CryptoKey() {\n    [native code]\n}")
    public void cryptoKey() throws Exception {
        test("CryptoKey");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGTextPositioningElement() { [native code] }",
            FF = "function SVGTextPositioningElement() {\n    [native code]\n}",
            IE = "[object SVGTextPositioningElement]")
    public void svgTextPositioningElement() throws Exception {
        test("SVGTextPositioningElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ReferenceError() { [native code] }",
            FF = "function ReferenceError() {\n    [native code]\n}",
            IE = "\nfunction ReferenceError() {\n    [native code]\n}\n")
    public void referenceError() throws Exception {
        test("ReferenceError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void dataStoreTask() throws Exception {
        test("DataStoreTask");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function eval() { [native code] }",
            FF = "function eval() {\n    [native code]\n}",
            IE = "\nfunction eval() {\n    [native code]\n}\n")
    public void eval() throws Exception {
        test("eval");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ValidityState() { [native code] }",
            FF = "function ValidityState() {\n    [native code]\n}",
            IE = "[object ValidityState]")
    public void validityState() throws Exception {
        test("ValidityState");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozSmsSegmentInfo() throws Exception {
        test("MozSmsSegmentInfo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void fileRequest() throws Exception {
        test("FileRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void windowEventHandlers_onbeforeprint() throws Exception {
        test("WindowEventHandlers.onbeforeprint");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function isNaN() { [native code] }",
            FF = "function isNaN() {\n    [native code]\n}",
            IE = "\nfunction isNaN() {\n    [native code]\n}\n")
    public void isNaN() throws Exception {
        test("isNaN");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WeakSet() { [native code] }",
            IE = "exception",
            FF = "function WeakSet() {\n    [native code]\n}")
    public void weakSet() throws Exception {
        test("WeakSet");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozMobileCellInfo() throws Exception {
        test("MozMobileCellInfo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domTimeStamp() throws Exception {
        test("DOMTimeStamp");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozNetworkStatsData() throws Exception {
        test("MozNetworkStatsData");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void idbTransactionSync() throws Exception {
        test("IDBTransactionSync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void fileSystemSync() throws Exception {
        test("FileSystemSync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void idbFactorySync() throws Exception {
        test("IDBFactorySync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function HTMLContentElement() { [native code] }",
            FF = "function HTMLContentElement() {\n    [native code]\n}")
    public void htmlContentElement() throws Exception {
        test("HTMLContentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void cssMatrix() throws Exception {
        test("CSSMatrix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void nameList() throws Exception {
        test("NameList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void client() throws Exception {
        test("Client");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void deviceStorage() throws Exception {
        test("DeviceStorage");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function TextMetrics() { [native code] }",
            FF = "function TextMetrics() {\n    [native code]\n}",
            IE = "[object TextMetrics]")
    public void textMetrics() throws Exception {
        test("TextMetrics");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domHighResTimeStamp() throws Exception {
        test("DOMHighResTimeStamp");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Function() { [native code] }",
            FF = "function Function() {\n    [native code]\n}",
            IE = "\nfunction Function() {\n    [native code]\n}\n")
    public void function() throws Exception {
        test("Function");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgStylable() throws Exception {
        test("SVGStylable");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MediaStreamAudioDestinationNode() { [native code] }",
            FF = "function MediaStreamAudioDestinationNode() {\n    [native code]\n}")
    public void mediaStreamAudioDestinationNode() throws Exception {
        test("MediaStreamAudioDestinationNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object MouseWheelEvent]")
    public void mouseWheelEvent() throws Exception {
        test("MouseWheelEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object PositionError]",
            EDGE = "function PositionError() { [native code] }")
    public void positionError() throws Exception {
        test("PositionError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void bluetoothStatusChangedEvent() throws Exception {
        test("BluetoothStatusChangedEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozWifiConnectionInfoEvent() throws Exception {
        test("MozWifiConnectionInfoEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void idbIndexSync() throws Exception {
        test("IDBIndexSync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CSSNamespaceRule() { [native code] }",
            IE = "[object CSSNamespaceRule]",
            FF = "exception")
    public void cssNamespaceRule() throws Exception {
        test("CSSNamespaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            EDGE = "function DeviceAcceleration() { [native code] }")
    public void deviceAcceleration() throws Exception {
        test("DeviceAcceleration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void deviceStorageChangeEvent() throws Exception {
        test("DeviceStorageChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void fileHandle() throws Exception {
        test("FileHandle");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGNumberList() { [native code] }",
            FF = "function SVGNumberList() {\n    [native code]\n}",
            IE = "[object SVGNumberList]")
    public void svgNumberList() throws Exception {
        test("SVGNumberList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function GamepadEvent() { [native code] }",
            IE = "exception",
            FF = "function GamepadEvent() {\n    [native code]\n}")
    public void gamepadEvent() throws Exception {
        test("GamepadEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function MozSettingsEvent() {\n    [native code]\n}")
    public void mozSettingsEvent() throws Exception {
        test("MozSettingsEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void l10n_language_code() throws Exception {
        test("L10n.language.code");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CustomEvent() { [native code] }",
            FF = "function CustomEvent() {\n    [native code]\n}",
            IE = "[object CustomEvent]")
    public void customEvent() throws Exception {
        test("CustomEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            EDGE = "function DeviceRotationRate() { [native code] }")
    public void deviceRotationRate() throws Exception {
        test("DeviceRotationRate");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ErrorEvent() { [native code] }",
            FF = "function ErrorEvent() {\n    [native code]\n}",
            IE = "[object ErrorEvent]")
    public void errorEvent() throws Exception {
        test("ErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimatedBoolean() { [native code] }",
            FF = "function SVGAnimatedBoolean() {\n    [native code]\n}",
            IE = "[object SVGAnimatedBoolean]")
    public void svgAnimatedBoolean() throws Exception {
        test("SVGAnimatedBoolean");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Set() { [native code] }",
            FF = "function Set() {\n    [native code]\n}",
            IE = "\nfunction Set() {\n    [native code]\n}\n")
    public void set() throws Exception {
        test("Set");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimatedPreserveAspectRatio() { [native code] }",
            FF = "function SVGAnimatedPreserveAspectRatio() {\n    [native code]\n}",
            IE = "[object SVGAnimatedPreserveAspectRatio]")
    public void svgAnimatedPreserveAspectRatio() throws Exception {
        test("SVGAnimatedPreserveAspectRatio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void idbObjectStoreSync() throws Exception {
        test("IDBObjectStoreSync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF38 = "[object MozMmsMessage]")
    public void mozMmsMessage() throws Exception {
        test("MozMmsMessage");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function BatteryManager() { [native code] }",
            FF = "function BatteryManager() {\n    [native code]\n}")
    public void batteryManager() throws Exception {
        test("BatteryManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function RTCDataChannelEvent() {\n    [native code]\n}")
    public void rtcDataChannelEvent() throws Exception {
        test("RTCDataChannelEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function ServiceWorkerRegistration() { [native code] }")
    public void serviceWorkerRegistration() throws Exception {
        test("ServiceWorkerRegistration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void navigatorOnLine() throws Exception {
        test("NavigatorOnLine");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgTRefElement() throws Exception {
        test("SVGTRefElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaQueryList() { [native code] }",
            FF = "function MediaQueryList() {\n    [native code]\n}",
            IE = "[object MediaQueryList]")
    public void mediaQueryList() throws Exception {
        test("MediaQueryList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function TransitionEvent() { [native code] }",
            FF = "function TransitionEvent() {\n    [native code]\n}",
            IE = "[object TransitionEvent]")
    public void transitionEvent() throws Exception {
        test("TransitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void cameraControl() throws Exception {
        test("CameraControl");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void contactManager() throws Exception {
        test("ContactManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void xDomainRequest() throws Exception {
        test("XDomainRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgFontFaceElement() throws Exception {
        test("SVGFontFaceElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.CharacterData}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CharacterData() { [native code] }",
            FF = "function CharacterData() {\n    [native code]\n}",
            IE = "[object CharacterData]")
    public void characterData() throws Exception {
        test("CharacterData");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGNumber() { [native code] }",
            IE = "[object SVGNumber]",
            FF = "function SVGNumber() {\n    [native code]\n}")
    public void svgNumber() throws Exception {
        test("SVGNumber");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void apps_mgmt() throws Exception {
        test("Apps.mgmt");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void positionOptions() throws Exception {
        test("PositionOptions");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void cameraManager() throws Exception {
        test("CameraManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function StereoPannerNode() {\n    [native code]\n}",
            EDGE = "function StereoPannerNode() { [native code] }")
    public void stereoPannerNode() throws Exception {
        test("StereoPannerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void undefined() throws Exception {
        test("undefined");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function NodeIterator() { [native code] }",
            FF = "function NodeIterator() {\n    [native code]\n}",
            IE = "[object NodeIterator]")
    public void nodeIterator() throws Exception {
        test("NodeIterator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function XMLHttpRequestEventTarget() { [native code] }",
            FF = "function XMLHttpRequestEventTarget() {\n    [native code]\n}",
            IE = "[object XMLHttpRequestEventTarget]")
    public void xmlHttpRequestEventTarget() throws Exception {
        test("XMLHttpRequestEventTarget");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Performance() { [native code] }",
            FF = "function Performance() {\n    [native code]\n}",
            IE = "[object Performance]")
    public void performance() throws Exception {
        test("Performance");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void bluetoothManager() throws Exception {
        test("BluetoothManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function AudioDestinationNode() { [native code] }",
            IE = "exception",
            FF = "function AudioDestinationNode() {\n    [native code]\n}")
    public void audioDestinationNode() throws Exception {
        test("AudioDestinationNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CompositionEvent() { [native code] }",
            FF = "function CompositionEvent() {\n    [native code]\n}",
            IE = "[object CompositionEvent]")
    public void compositionEvent() throws Exception {
        test("CompositionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function RadioNodeList() { [native code] }",
            FF = "function RadioNodeList() {\n    [native code]\n}")
    public void radioNodeList() throws Exception {
        test("RadioNodeList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void workerGlobalScope() throws Exception {
        test("WorkerGlobalScope");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgFontFaceNameElement() throws Exception {
        test("SVGFontFaceNameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domApplicationsRegistry() throws Exception {
        test("DOMApplicationsRegistry");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void audioChannelManager() throws Exception {
        test("AudioChannelManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WeakMap() { [native code] }",
            FF = "function WeakMap() {\n    [native code]\n}",
            IE = "\nfunction WeakMap() {\n    [native code]\n}\n")
    public void weakMap() throws Exception {
        test("WeakMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaSource() { [native code] }",
            IE = "exception",
            FF = "exception",
            FF45 = "function MediaSource() {\n    [native code]\n}")
    public void mediaSource() throws Exception {
        test("MediaSource");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IDBKeyRange() { [native code] }",
            FF = "function IDBKeyRange() {\n    [native code]\n}",
            IE = "[object IDBKeyRange]")
    public void idbKeyRange() throws Exception {
        test("IDBKeyRange");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void serviceWorkerGlobalScope() throws Exception {
        test("ServiceWorkerGlobalScope");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "[object CSSCounterStyleRule]")
    public void cssCounterStyleRule() throws Exception {
        test("CSSCounterStyleRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Infinity")
    public void infinity() throws Exception {
        test("Infinity");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function AudioBufferSourceNode() { [native code] }",
            IE = "exception",
            FF = "function AudioBufferSourceNode() {\n    [native code]\n}")
    public void audioBufferSourceNode() throws Exception {
        test("AudioBufferSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function DOMRequest() {\n    [native code]\n}")
    public void domRequest() throws Exception {
        test("DOMRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void chromeWorker() throws Exception {
        test("ChromeWorker");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function InputEvent() {\n    [native code]\n}")
    public void inputEvent() throws Exception {
        test("InputEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function Touch() { [native code] }")
    public void touch() throws Exception {
        test("Touch");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function InternalError() {\n    [native code]\n}")
    @NotYetImplemented({ CHROME, IE, EDGE })
    public void internalError() throws Exception {
        test("InternalError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function EventSource() { [native code] }",
            FF = "function EventSource() {\n    [native code]\n}")
    public void eventSource() throws Exception {
        test("EventSource");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void animationPlayer() throws Exception {
        test("AnimationPlayer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Date() { [native code] }",
            FF = "function Date() {\n    [native code]\n}",
            IE = "\nfunction Date() {\n    [native code]\n}\n")
    public void date() throws Exception {
        test("Date");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLRenderingContext}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLRenderingContext() { [native code] }",
            FF = "function WebGLRenderingContext() {\n    [native code]\n}",
            IE = "[object WebGLRenderingContext]")
    public void webGLRenderingContext() throws Exception {
        test("WebGLRenderingContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaDevices() { [native code] }",
            IE = "exception",
            FF = "function MediaDevices() {\n    [native code]\n}")
    public void mediaDevices() throws Exception {
        test("MediaDevices");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgFontElement() throws Exception {
        test("SVGFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void randomSource() throws Exception {
        test("RandomSource");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void fMRadio() throws Exception {
        test("FMRadio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Blob() { [native code] }",
            FF = "function Blob() {\n    [native code]\n}",
            IE = "\nfunction Blob() {\n    [native code]\n}\n")
    public void blob() throws Exception {
        test("Blob");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimatedTransformList() { [native code] }",
            FF = "function SVGAnimatedTransformList() {\n    [native code]\n}",
            IE = "[object SVGAnimatedTransformList]")
    public void svgAnimatedTransformList() throws Exception {
        test("SVGAnimatedTransformList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function OscillatorNode() { [native code] }",
            IE = "exception",
            FF = "function OscillatorNode() {\n    [native code]\n}")
    public void oscillatorNode() throws Exception {
        test("OscillatorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "[object Object]",
            EDGE = "[object Object]",
            FF45 = "[object Object]")
    public void reflect() throws Exception {
        test("Reflect");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaStreamTrack() { [native code] }",
            FF = "function MediaStreamTrack() {\n    [native code]\n}",
            IE = "exception")
    public void mediaStreamTrack() throws Exception {
        test("MediaStreamTrack");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void directoryEntry() throws Exception {
        test("DirectoryEntry");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void settingsLock() throws Exception {
        test("SettingsLock");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void entrySync() throws Exception {
        test("EntrySync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PerformanceNavigation() { [native code] }",
            FF = "function PerformanceNavigation() {\n    [native code]\n}",
            IE = "[object PerformanceNavigation]")
    public void performanceNavigation() throws Exception {
        test("PerformanceNavigation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGAnimationElement() { [native code] }",
            FF = "function SVGAnimationElement() {\n    [native code]\n}")
    public void svgAnimationElement() throws Exception {
        test("SVGAnimationElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void notifyAudioAvailableEvent() throws Exception {
        test("NotifyAudioAvailableEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DelayNode() { [native code] }",
            IE = "exception",
            FF = "function DelayNode() {\n    [native code]\n}")
    public void delayNode() throws Exception {
        test("DelayNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function Request() { [native code] }",
            FF45 = "function Request() {\n    [native code]\n}")
    public void request() throws Exception {
        test("Request");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function CacheStorage() { [native code] }",
            FF45 = "function CacheStorage() {\n    [native code]\n}")
    public void cacheStorage() throws Exception {
        test("CacheStorage");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void telephonyCall() throws Exception {
        test("TelephonyCall");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function escape() { [native code] }",
            FF = "function escape() {\n    [native code]\n}",
            IE = "\nfunction escape() {\n    [native code]\n}\n")
    public void escape() throws Exception {
        test("escape");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ClipboardEvent() { [native code] }",
            IE = "exception",
            FF = "function ClipboardEvent() {\n    [native code]\n}")
    public void clipboardEvent() throws Exception {
        test("ClipboardEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void globalEventHandlers() throws Exception {
        test("GlobalEventHandlers");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void l10n_ready() throws Exception {
        test("L10n.ready");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function ServiceWorkerContainer() { [native code] }")
    public void serviceWorkerContainer() throws Exception {
        test("ServiceWorkerContainer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void abstractWorker() throws Exception {
        test("AbstractWorker");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void abstractList() throws Exception {
        test("AbstractList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function MediaStream() {\n    [native code]\n}",
            EDGE = "function MediaStream() { [native code] }")
    public void mediaStream() throws Exception {
        test("MediaStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void windowEventHandlers() throws Exception {
        test("WindowEventHandlers");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozSmsManager() throws Exception {
        test("MozSmsManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function AudioBuffer() { [native code] }",
            IE = "exception",
            FF = "function AudioBuffer() {\n    [native code]\n}")
    public void audioBuffer() throws Exception {
        test("AudioBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function URL() { [native code] }",
            FF = "function URL() {\n    [native code]\n}",
            IE = "[object URL]")
    public void url() throws Exception {
        test("URL");
    }

    /**
     * Tests {@link com.gargoylesoftware.htmlunit.javascript.host.GamepadButton}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function GamepadButton() { [native code] }",
            IE = "exception",
            FF = "function GamepadButton() {\n    [native code]\n}")
    public void gamepadButton() throws Exception {
        test("GamepadButton");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozMobileCFInfo() throws Exception {
        test("MozMobileCFInfo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IDBTransaction() { [native code] }",
            FF = "function IDBTransaction() {\n    [native code]\n}",
            IE = "[object IDBTransaction]")
    public void idbTransaction() throws Exception {
        test("IDBTransaction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function HTMLDataElement() {\n    [native code]\n}")
    public void htmlDataElement() throws Exception {
        test("HTMLDataElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IDBCursorWithValue() { [native code] }",
            FF = "function IDBCursorWithValue() {\n    [native code]\n}",
            IE = "[object IDBCursorWithValue]")
    public void idbCursorWithValue() throws Exception {
        test("IDBCursorWithValue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozAlarmsManager() throws Exception {
        test("MozAlarmsManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void windowClient() throws Exception {
        test("WindowClient");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgAnimateColorElement() throws Exception {
        test("SVGAnimateColorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void wifiManager() throws Exception {
        test("WifiManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            EDGE = "function CSSConditionRule() { [native code] }",
            FF = "[object CSSConditionRule]")
    public void cssConditionRule() throws Exception {
        test("CSSConditionRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGLengthList() { [native code] }",
            FF = "function SVGLengthList() {\n    [native code]\n}",
            IE = "[object SVGLengthList]")
    public void svgLengthList() throws Exception {
        test("SVGLengthList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function MouseScrollEvent() {\n    [native code]\n}")
    public void mouseScrollEvent() throws Exception {
        test("MouseScrollEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozVoicemailStatus() throws Exception {
        test("MozVoicemailStatus");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void dedicatedWorkerGlobalScope() throws Exception {
        test("DedicatedWorkerGlobalScope");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void promiseResolver() throws Exception {
        test("PromiseResolver");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGGradientElement() { [native code] }",
            FF = "function SVGGradientElement() {\n    [native code]\n}",
            IE = "[object SVGGradientElement]")
    public void svgGradientElement() throws Exception {
        test("SVGGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void identityManager() throws Exception {
        test("IdentityManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void l10n_get() throws Exception {
        test("L10n.get");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGLength() { [native code] }",
            FF = "function SVGLength() {\n    [native code]\n}",
            IE = "[object SVGLength]")
    public void svgLength() throws Exception {
        test("SVGLength");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IDBCursor() { [native code] }",
            FF = "function IDBCursor() {\n    [native code]\n}",
            IE = "[object IDBCursor]")
    public void idbCursor() throws Exception {
        test("IDBCursor");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Crypto() { [native code] }",
            FF = "function Crypto() {\n    [native code]\n}",
            IE = "[object Crypto]")
    public void crypto() throws Exception {
        test("Crypto");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function BlobEvent() { [native code] }",
            FF = "function BlobEvent() {\n    [native code]\n}",
            IE = "exception")
    public void blobEvent() throws Exception {
        test("BlobEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void tcpSocket() throws Exception {
        test("TCPSocket");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPoint() { [native code] }",
            FF = "function SVGPoint() {\n    [native code]\n}",
            IE = "[object SVGPoint]")
    public void svgPoint() throws Exception {
        test("SVGPoint");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CSS() { [native code] }",
            IE = "exception",
            FF = "function CSS() {\n    [native code]\n}")
    public void css() throws Exception {
        test("CSS");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function UserProximityEvent() {\n    [native code]\n}")
    public void userProximityEvent() throws Exception {
        test("UserProximityEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void fileSystem() throws Exception {
        test("FileSystem");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void idbCursorSync() throws Exception {
        test("IDBCursorSync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DataTransfer() { [native code] }",
            IE = "[object DataTransfer]",
            FF = "function DataTransfer() {\n    [native code]\n}")
    public void dataTransfer() throws Exception {
        test("DataTransfer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void childNode() throws Exception {
        test("ChildNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function TypeError() { [native code] }",
            FF = "function TypeError() {\n    [native code]\n}",
            IE = "\nfunction TypeError() {\n    [native code]\n}\n")
    public void typeError() throws Exception {
        test("TypeError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void dataStoreCursor() throws Exception {
        test("DataStoreCursor");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozTimeManager() throws Exception {
        test("MozTimeManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Symbol() { [native code] }",
            IE = "exception",
            FF = "function Symbol() {\n    [native code]\n}")
    public void symbol() throws Exception {
        test("Symbol");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgGlyphElement() throws Exception {
        test("SVGGlyphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domString() throws Exception {
        test("DOMString");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function ServiceWorker() { [native code] }")
    public void serviceWorker() throws Exception {
        test("ServiceWorker");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozActivityRequestHandler() throws Exception {
        test("MozActivityRequestHandler");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ImageData() { [native code] }",
            FF = "function ImageData() {\n    [native code]\n}",
            IE = "[object ImageData]")
    public void imageData() throws Exception {
        test("ImageData");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void l10n_once() throws Exception {
        test("L10n.once");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Gamepad() { [native code] }",
            IE = "exception",
            FF = "function Gamepad() {\n    [native code]\n}")
    public void gamepad() throws Exception {
        test("Gamepad");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void connection() throws Exception {
        test("Connection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozNDEFRecord() throws Exception {
        test("MozNDEFRecord");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function encodeURIComponent() { [native code] }",
            FF = "function encodeURIComponent() {\n    [native code]\n}",
            IE = "\nfunction encodeURIComponent() {\n    [native code]\n}\n")
    public void encodeURIComponent() throws Exception {
        test("encodeURIComponent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Proxy() { [native code] }",
            FF = "function Proxy() {\n    [native code]\n}",
            IE = "exception")
    public void proxy() throws Exception {
        test("Proxy");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void navigatorLanguage() throws Exception {
        test("NavigatorLanguage");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ChannelSplitterNode() { [native code] }",
            IE = "exception",
            FF = "function ChannelSplitterNode() {\n    [native code]\n}")
    public void channelSplitterNode() throws Exception {
        test("ChannelSplitterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PopStateEvent() { [native code] }",
            FF = "function PopStateEvent() {\n    [native code]\n}",
            IE = "[object PopStateEvent]")
    public void popStateEvent() throws Exception {
        test("PopStateEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void localFileSystemSync() throws Exception {
        test("LocalFileSystemSync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function FileList() { [native code] }",
            FF = "function FileList() {\n    [native code]\n}",
            IE = "[object FileList]")
    public void fileList() throws Exception {
        test("FileList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function unescape() { [native code] }",
            FF = "function unescape() {\n    [native code]\n}",
            IE = "\nfunction unescape() {\n    [native code]\n}\n")
    public void unescape() throws Exception {
        test("unescape");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void windowTimers() throws Exception {
        test("WindowTimers");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function File() { [native code] }",
            FF = "function File() {\n    [native code]\n}",
            IE = "[object File]")
    public void file() throws Exception {
        test("File");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF38 = "[object MozSmsMessage]")
    public void mozSmsMessage() throws Exception {
        test("MozSmsMessage");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CanvasPattern() { [native code] }",
            FF = "function CanvasPattern() {\n    [native code]\n}",
            IE = "[object CanvasPattern]")
    public void canvasPattern() throws Exception {
        test("CanvasPattern");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domObject() throws Exception {
        test("DOMObject");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ScriptProcessorNode() { [native code] }",
            IE = "exception",
            FF = "function ScriptProcessorNode() {\n    [native code]\n}")
    public void scriptProcessorNode() throws Exception {
        test("ScriptProcessorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozMobileConnectionInfo() throws Exception {
        test("MozMobileConnectionInfo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void permissionSettings() throws Exception {
        test("PermissionSettings");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function DragEvent() { [native code] }",
            FF = "function DragEvent() {\n    [native code]\n}",
            IE = "[object DragEvent]",
            EDGE = "function DragEvent() { [native code] }")
    public void dragEvent() throws Exception {
        test("DragEvent");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function RTCPeerConnection() { [native code] }")
    public void webkitRTCPeerConnection() throws Exception {
        test("webkitRTCPeerConnection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function mozRTCPeerConnection() {\n    [native code]\n}")
    public void mozRTCPeerConnection() throws Exception {
        test("mozRTCPeerConnection");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function RTCIceCandidate() { [native code] }",
            FF45 = "function RTCIceCandidate() {\n    [native code]\n}")
    public void rtcIceCandidate() throws Exception {
        test("RTCIceCandidate");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function mozRTCIceCandidate() {\n    [native code]\n}")
    public void mozRTCIceCandidate() throws Exception {
        test("mozRTCIceCandidate");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function mozRTCSessionDescription() {\n    [native code]\n}")
    public void mozRTCSessionDescription() throws Exception {
        test("mozRTCSessionDescription");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webkitRTCSessionDescription() throws Exception {
        test("webkitRTCSessionDescription");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function BarProp() { [native code] }",
            IE = "exception",
            FF = "function BarProp() {\n    [native code]\n}")
    public void barProp() throws Exception {
        test("BarProp");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void speechSynthesis() throws Exception {
        test("SpeechSynthesis");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SpeechSynthesisUtterance() { [native code] }")
    public void speechSynthesisUtterance() throws Exception {
        test("SpeechSynthesisUtterance");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void speechRecognition() throws Exception {
        test("SpeechRecognition");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object StyleMedia]",
            EDGE = "function StyleMedia() { [native code] }")
    public void styleMedia() throws Exception {
        test("StyleMedia");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SpeechRecognition() { [native code] }")
    public void webkitSpeechRecognition() throws Exception {
        test("webkitSpeechRecognition");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void syncRegistration() throws Exception {
        test("SyncRegistration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MIDIConnectionEvent() { [native code] }")
    public void midiConnectionEvent() throws Exception {
        test("MIDIConnectionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MIDIMessageEvent() { [native code] }")
    public void midiMessageEvent() throws Exception {
        test("MIDIMessageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MIDIOutput() { [native code] }")
    public void midiOutput() throws Exception {
        test("MIDIOutput");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MIDIOutputMap() { [native code] }")
    public void midiOutputMap() throws Exception {
        test("MIDIOutputMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MIDIPort() { [native code] }")
    public void midiPort() throws Exception {
        test("MIDIPort");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function ReadableByteStream() { [native code] }")
    public void readableByteStream() throws Exception {
        test("ReadableByteStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function ReadableStream() { [native code] }")
    public void readableStream() throws Exception {
        test("ReadableStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void vrDevice() throws Exception {
        test("VRDevice");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_int16x8() throws Exception {
        test("SIMD.int16x8");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MIDIInput() { [native code] }")
    public void midiInput() throws Exception {
        test("MIDIInput");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd() throws Exception {
        test("SIMD");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MIDIInputMap() { [native code] }")
    public void midiInputMap() throws Exception {
        test("MIDIInputMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_int8x16() throws Exception {
        test("SIMD.int8x16");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void syncEvent() throws Exception {
        test("SyncEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_float32x4() throws Exception {
        test("SIMD.float32x4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void periodicSyncManager() throws Exception {
        test("PeriodicSyncManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SyncManager() { [native code] }")
    public void syncManager() throws Exception {
        test("SyncManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void periodicSyncEvent() throws Exception {
        test("PeriodicSyncEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function DOMPointReadOnly() {\n    [native code]\n}")
    public void domPointReadOnly() throws Exception {
        test("DOMPointReadOnly");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void vrFieldOfView() throws Exception {
        test("VRFieldOfView");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PermissionStatus() { [native code] }")
    public void permissionStatus() throws Exception {
        test("PermissionStatus");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function DOMPoint() {\n    [native code]\n}")
    public void domPoint() throws Exception {
        test("DOMPoint");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function BeforeInstallPromptEvent() { [native code] }")
    public void beforeInstallPromptEvent() throws Exception {
        test("BeforeInstallPromptEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void positionSensorVRDevice() throws Exception {
        test("PositionSensorVRDevice");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void hMDVRDevice() throws Exception {
        test("HMDVRDevice");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function DOMRectReadOnly() {\n    [native code]\n}")
    public void domRectReadOnly() throws Exception {
        test("DOMRectReadOnly");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MIDIAccess() { [native code] }")
    public void midiAccess() throws Exception {
        test("MIDIAccess");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domApplication() throws Exception {
        test("DOMApplication");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_float64x2() throws Exception {
        test("SIMD.float64x2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void periodicSyncRegistration() throws Exception {
        test("PeriodicSyncRegistration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function Permissions() { [native code] }")
    public void permissions() throws Exception {
        test("Permissions");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void vrPositionState() throws Exception {
        test("VRPositionState");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void vrFieldOfViewReadOnly() throws Exception {
        test("VRFieldOfViewReadOnly");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void vrEyeParameters() throws Exception {
        test("VREyeParameters");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_int32x4() throws Exception {
        test("SIMD.int32x4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function AutocompleteErrorEvent() { [native code] }")
    public void autocompleteErrorEvent() throws Exception {
        test("AutocompleteErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void cssUnknownRule() throws Exception {
        test("CSSUnknownRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DOMSettableTokenList() { [native code] }",
            FF = "function DOMSettableTokenList() {\n    [native code]\n}",
            IE = "[object DOMSettableTokenList]")
    public void domSettableTokenList() throws Exception {
        test("DOMSettableTokenList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DataTransferItem() { [native code] }",
            IE = "exception",
            FF = "exception")
    public void dataTransferItem() throws Exception {
        test("DataTransferItem");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DataTransferItemList() { [native code] }",
            IE = "exception",
            FF = "exception")
    public void dataTransferItemList() throws Exception {
        test("DataTransferItemList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function HTMLTemplateElement() { [native code] }",
            FF = "function HTMLTemplateElement() {\n    [native code]\n}")
    public void htmlTemplateElement() throws Exception {
        test("HTMLTemplateElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaError() { [native code] }",
            FF = "function MediaError() {\n    [native code]\n}",
            IE = "[object MediaError]")
    public void mediaError() throws Exception {
        test("MediaError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF45 = "function MediaKeyError() {\n    [native code]\n}")
    public void mediaKeyError() throws Exception {
        test("MediaKeyError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mediaKeyEvent() throws Exception {
        test("MediaKeyEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            EDGE = "function OverflowEvent() { [native code] }")
    public void overflowEvent() throws Exception {
        test("OverflowEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PerformanceEntry() { [native code] }",
            FF = "function PerformanceEntry() {\n    [native code]\n}",
            IE = "[object PerformanceEntry]")
    public void performanceEntry() throws Exception {
        test("PerformanceEntry");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PerformanceMark() { [native code] }",
            FF = "function PerformanceMark() {\n    [native code]\n}",
            IE = "[object PerformanceMark]")
    public void performanceMark() throws Exception {
        test("PerformanceMark");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PerformanceMeasure() { [native code] }",
            FF = "function PerformanceMeasure() {\n    [native code]\n}",
            IE = "[object PerformanceMeasure]")
    public void performanceMeasure() throws Exception {
        test("PerformanceMeasure");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PerformanceResourceTiming() { [native code] }",
            FF = "function PerformanceResourceTiming() {\n    [native code]\n}",
            IE = "[object PerformanceResourceTiming]")
    public void performanceResourceTiming() throws Exception {
        test("PerformanceResourceTiming");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGComponentTransferFunctionElement() { [native code] }",
            FF = "function SVGComponentTransferFunctionElement() {\n    [native code]\n}",
            IE = "[object SVGComponentTransferFunctionElement]")
    public void svgComponentTransferFunctionElement() throws Exception {
        test("SVGComponentTransferFunctionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGFEDropShadowElement() { [native code] }",
            FF = "function SVGFEDropShadowElement() {\n    [native code]\n}")
    public void svgFEDropShadowElement() throws Exception {
        test("SVGFEDropShadowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGGraphicsElement() { [native code] }",
            FF = "function SVGGraphicsElement() {\n    [native code]\n}")
    public void svgGraphicsElement() throws Exception {
        test("SVGGraphicsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPathSeg() { [native code] }",
            FF = "function SVGPathSeg() {\n    [native code]\n}",
            IE = "[object SVGPathSeg]",
            CHROME = "exception")
    public void svgPathSeg() throws Exception {
        test("SVGPathSeg");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPathSegArcAbs() { [native code] }",
            FF = "function SVGPathSegArcAbs() {\n    [native code]\n}",
            IE = "[object SVGPathSegArcAbs]",
            CHROME = "exception")
    public void svgPathSegArcAbs() throws Exception {
        test("SVGPathSegArcAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPathSegArcRel() { [native code] }",
            FF = "function SVGPathSegArcRel() {\n    [native code]\n}",
            IE = "[object SVGPathSegArcRel]",
            CHROME = "exception")
    public void svgPathSegArcRel() throws Exception {
        test("SVGPathSegArcRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPathSegClosePath() { [native code] }",
            FF = "function SVGPathSegClosePath() {\n    [native code]\n}",
            IE = "[object SVGPathSegClosePath]",
            CHROME = "exception")
    public void svgPathSegClosePath() throws Exception {
        test("SVGPathSegClosePath");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPathSegCurvetoCubicAbs() { [native code] }",
            FF = "function SVGPathSegCurvetoCubicAbs() {\n    [native code]\n}",
            IE = "[object SVGPathSegCurvetoCubicAbs]",
            CHROME = "exception")
    public void svgPathSegCurvetoCubicAbs() throws Exception {
        test("SVGPathSegCurvetoCubicAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPathSegCurvetoCubicRel() { [native code] }",
            FF = "function SVGPathSegCurvetoCubicRel() {\n    [native code]\n}",
            IE = "[object SVGPathSegCurvetoCubicRel]",
            CHROME = "exception")
    public void svgPathSegCurvetoCubicRel() throws Exception {
        test("SVGPathSegCurvetoCubicRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPathSegCurvetoCubicSmoothAbs() { [native code] }",
            FF = "function SVGPathSegCurvetoCubicSmoothAbs() {\n    [native code]\n}",
            IE = "[object SVGPathSegCurvetoCubicSmoothAbs]",
            CHROME = "exception")
    public void svgPathSegCurvetoCubicSmoothAbs() throws Exception {
        test("SVGPathSegCurvetoCubicSmoothAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPathSegCurvetoCubicSmoothRel() { [native code] }",
            FF = "function SVGPathSegCurvetoCubicSmoothRel() {\n    [native code]\n}",
            IE = "[object SVGPathSegCurvetoCubicSmoothRel]",
            CHROME = "exception")
    public void svgPathSegCurvetoCubicSmoothRel() throws Exception {
        test("SVGPathSegCurvetoCubicSmoothRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPathSegCurvetoQuadraticAbs() { [native code] }",
            FF = "function SVGPathSegCurvetoQuadraticAbs() {\n    [native code]\n}",
            IE = "[object SVGPathSegCurvetoQuadraticAbs]",
            CHROME = "exception")
    public void svgPathSegCurvetoQuadraticAbs() throws Exception {
        test("SVGPathSegCurvetoQuadraticAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPathSegCurvetoQuadraticRel() { [native code] }",
            FF = "function SVGPathSegCurvetoQuadraticRel() {\n    [native code]\n}",
            IE = "[object SVGPathSegCurvetoQuadraticRel]",
            CHROME = "exception")
    public void svgPathSegCurvetoQuadraticRel() throws Exception {
        test("SVGPathSegCurvetoQuadraticRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPathSegCurvetoQuadraticSmoothAbs() { [native code] }",
            FF = "function SVGPathSegCurvetoQuadraticSmoothAbs() {\n    [native code]\n}",
            IE = "[object SVGPathSegCurvetoQuadraticSmoothAbs]",
            CHROME = "exception")
    public void svgPathSegCurvetoQuadraticSmoothAbs() throws Exception {
        test("SVGPathSegCurvetoQuadraticSmoothAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPathSegCurvetoQuadraticSmoothRel() { [native code] }",
            FF = "function SVGPathSegCurvetoQuadraticSmoothRel() {\n    [native code]\n}",
            IE = "[object SVGPathSegCurvetoQuadraticSmoothRel]",
            CHROME = "exception")
    public void svgPathSegCurvetoQuadraticSmoothRel() throws Exception {
        test("SVGPathSegCurvetoQuadraticSmoothRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPathSegLinetoAbs() { [native code] }",
            FF = "function SVGPathSegLinetoAbs() {\n    [native code]\n}",
            IE = "[object SVGPathSegLinetoAbs]",
            CHROME = "exception")
    public void svgPathSegLinetoAbs() throws Exception {
        test("SVGPathSegLinetoAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPathSegLinetoHorizontalAbs() { [native code] }",
            FF = "function SVGPathSegLinetoHorizontalAbs() {\n    [native code]\n}",
            IE = "[object SVGPathSegLinetoHorizontalAbs]",
            CHROME = "exception")
    public void svgPathSegLinetoHorizontalAbs() throws Exception {
        test("SVGPathSegLinetoHorizontalAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPathSegLinetoHorizontalRel() { [native code] }",
            FF = "function SVGPathSegLinetoHorizontalRel() {\n    [native code]\n}",
            IE = "[object SVGPathSegLinetoHorizontalRel]",
            CHROME = "exception")
    public void svgPathSegLinetoHorizontalRel() throws Exception {
        test("SVGPathSegLinetoHorizontalRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPathSegLinetoRel() { [native code] }",
            FF = "function SVGPathSegLinetoRel() {\n    [native code]\n}",
            IE = "[object SVGPathSegLinetoRel]",
            CHROME = "exception")
    public void svgPathSegLinetoRel() throws Exception {
        test("SVGPathSegLinetoRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPathSegLinetoVerticalAbs() { [native code] }",
            FF = "function SVGPathSegLinetoVerticalAbs() {\n    [native code]\n}",
            IE = "[object SVGPathSegLinetoVerticalAbs]",
            CHROME = "exception")
    public void svgPathSegLinetoVerticalAbs() throws Exception {
        test("SVGPathSegLinetoVerticalAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPathSegLinetoVerticalRel() { [native code] }",
            FF = "function SVGPathSegLinetoVerticalRel() {\n    [native code]\n}",
            IE = "[object SVGPathSegLinetoVerticalRel]",
            CHROME = "exception")
    public void svgPathSegLinetoVerticalRel() throws Exception {
        test("SVGPathSegLinetoVerticalRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPathSegList() { [native code] }",
            FF = "function SVGPathSegList() {\n    [native code]\n}",
            IE = "[object SVGPathSegList]",
            CHROME = "exception")
    public void svgPathSegList() throws Exception {
        test("SVGPathSegList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPathSegMovetoAbs() { [native code] }",
            FF = "function SVGPathSegMovetoAbs() {\n    [native code]\n}",
            IE = "[object SVGPathSegMovetoAbs]",
            CHROME = "exception")
    public void svgPathSegMovetoAbs() throws Exception {
        test("SVGPathSegMovetoAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPathSegMovetoRel() { [native code] }",
            FF = "function SVGPathSegMovetoRel() {\n    [native code]\n}",
            IE = "[object SVGPathSegMovetoRel]",
            CHROME = "exception")
    public void svgPathSegMovetoRel() throws Exception {
        test("SVGPathSegMovetoRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPointList() { [native code] }",
            FF = "function SVGPointList() {\n    [native code]\n}",
            IE = "[object SVGPointList]")
    public void svgPointList() throws Exception {
        test("SVGPointList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgRenderingIntent() throws Exception {
        test("SVGRenderingIntent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGTextContentElement() { [native code] }",
            FF = "function SVGTextContentElement() {\n    [native code]\n}",
            IE = "[object SVGTextContentElement]")
    public void svgTextContentElement() throws Exception {
        test("SVGTextContentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGUnitTypes() { [native code] }",
            FF = "function SVGUnitTypes() {\n    [native code]\n}",
            IE = "[object SVGUnitTypes]")
    public void svgUnitTypes() throws Exception {
        test("SVGUnitTypes");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGViewSpec() { [native code] }")
    public void svgViewSpec() throws Exception {
        test("SVGViewSpec");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGZoomEvent() { [native code] }",
            FF = "function SVGZoomEvent() {\n    [native code]\n}",
            IE = "[object SVGZoomEvent]")
    public void svgZoomEvent() throws Exception {
        test("SVGZoomEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SecurityPolicyViolationEvent() { [native code] }")
    public void securityPolicyViolationEvent() throws Exception {
        test("SecurityPolicyViolationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SpeechSynthesisEvent() { [native code] }")
    public void speechSynthesisEvent() throws Exception {
        test("SpeechSynthesisEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function TextEvent() { [native code] }",
            FF = "exception",
            IE = "[object TextEvent]")
    public void textEvent() throws Exception {
        test("TextEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function TextTrack() { [native code] }",
            FF = "function TextTrack() {\n    [native code]\n}",
            IE = "[object TextTrack]")
    public void textTrack() throws Exception {
        test("TextTrack");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function TextTrackCue() { [native code] }",
            FF = "exception",
            IE = "\nfunction TextTrackCue() {\n    [native code]\n}\n",
            FF45 = "function TextTrackCue() {\n    [native code]\n}")
    public void textTrackCue() throws Exception {
        test("TextTrackCue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function TextTrackCueList() { [native code] }",
            FF = "function TextTrackCueList() {\n    [native code]\n}",
            IE = "[object TextTrackCueList]")
    public void textTrackCueList() throws Exception {
        test("TextTrackCueList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function TextTrackList() { [native code] }",
            FF = "function TextTrackList() {\n    [native code]\n}",
            IE = "[object TextTrackList]")
    public void textTrackList() throws Exception {
        test("TextTrackList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function TrackEvent() { [native code] }",
            FF = "function TrackEvent() {\n    [native code]\n}",
            IE = "[object TrackEvent]")
    public void trackEvent() throws Exception {
        test("TrackEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function VTTCue() { [native code] }",
            FF = "function VTTCue() {\n    [native code]\n}")
    public void vTTCue() throws Exception {
        test("VTTCue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLActiveInfo() { [native code] }",
            FF = "function WebGLActiveInfo() {\n    [native code]\n}",
            IE = "[object WebGLActiveInfo]")
    public void webGLActiveInfo() throws Exception {
        test("WebGLActiveInfo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLBuffer() { [native code] }",
            FF = "function WebGLBuffer() {\n    [native code]\n}",
            IE = "[object WebGLBuffer]")
    public void webGLBuffer() throws Exception {
        test("WebGLBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLContextEvent() { [native code] }",
            FF = "exception",
            IE = "\nfunction WebGLContextEvent() {\n    [native code]\n}\n")
    public void webGLContextEvent() throws Exception {
        test("WebGLContextEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLFramebuffer() { [native code] }",
            FF = "function WebGLFramebuffer() {\n    [native code]\n}",
            IE = "[object WebGLFramebuffer]")
    public void webGLFramebuffer() throws Exception {
        test("WebGLFramebuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLProgram() { [native code] }",
            FF = "function WebGLProgram() {\n    [native code]\n}",
            IE = "[object WebGLProgram]")
    public void webGLProgram() throws Exception {
        test("WebGLProgram");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLRenderbuffer() { [native code] }",
            FF = "function WebGLRenderbuffer() {\n    [native code]\n}",
            IE = "[object WebGLRenderbuffer]")
    public void webGLRenderbuffer() throws Exception {
        test("WebGLRenderbuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLShader() { [native code] }",
            FF = "function WebGLShader() {\n    [native code]\n}",
            IE = "[object WebGLShader]")
    public void webGLShader() throws Exception {
        test("WebGLShader");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLShaderPrecisionFormat() { [native code] }",
            FF = "function WebGLShaderPrecisionFormat() {\n    [native code]\n}",
            IE = "[object WebGLShaderPrecisionFormat]")
    public void webGLShaderPrecisionFormat() throws Exception {
        test("WebGLShaderPrecisionFormat");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLTexture() { [native code] }",
            FF = "function WebGLTexture() {\n    [native code]\n}",
            IE = "[object WebGLTexture]")
    public void webGLTexture() throws Exception {
        test("WebGLTexture");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLUniformLocation() { [native code] }",
            FF = "function WebGLUniformLocation() {\n    [native code]\n}",
            IE = "[object WebGLUniformLocation]")
    public void webGLUniformLocation() throws Exception {
        test("WebGLUniformLocation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function AnimationEvent() { [native code] }")
    public void webKitAnimationEvent() throws Exception {
        test("WebKitAnimationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function WebKitCSSMatrix() { [native code] }",
            EDGE = "function WebKitCSSMatrix() { [native code] }")
    public void webKitCSSMatrix() throws Exception {
        test("WebKitCSSMatrix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function TransitionEvent() { [native code] }")
    public void webKitTransitionEvent() throws Exception {
        test("WebKitTransitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function XMLHttpRequestProgressEvent() { [native code] }")
    public void xMLHttpRequestProgressEvent() throws Exception {
        test("XMLHttpRequestProgressEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function XMLHttpRequestUpload() { [native code] }",
            IE = "exception",
            FF = "function XMLHttpRequestUpload() {\n    [native code]\n}")
    public void xmlHttpRequestUpload() throws Exception {
        test("XMLHttpRequestUpload");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function XPathExpression() { [native code] }",
            IE = "exception",
            FF = "function XPathExpression() {\n    [native code]\n}")
    public void xPathExpression() throws Exception {
        test("XPathExpression");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function ApplicationCacheErrorEvent() { [native code] }")
    public void applicationCacheErrorEvent() throws Exception {
        test("ApplicationCacheErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function CSSViewportRule() { [native code] }")
    public void cssViewportRule() throws Exception {
        test("CSSViewportRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function FontFace() { [native code] }",
            FF45 = "function FontFace() {\n    [native code]\n}")
    public void fontFace() throws Exception {
        test("FontFace");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function ImageBitmap() { [native code] }",
            FF45 = "function ImageBitmap() {\n    [native code]\n}")
    public void imageBitmap() throws Exception {
        test("ImageBitmap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void inputMethodContext() throws Exception {
        test("InputMethodContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MediaEncryptedEvent() { [native code] }",
            FF45 = "function MediaEncryptedEvent() {\n    [native code]\n}")
    public void mediaEncryptedEvent() throws Exception {
        test("MediaEncryptedEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MediaQueryListEvent() { [native code] }")
    public void mediaQueryListEvent() throws Exception {
        test("MediaQueryListEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGDiscardElement() { [native code] }")
    public void svgDiscardElement() throws Exception {
        test("SVGDiscardElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGGeometryElement() { [native code] }")
    public void svgGeometryElement() throws Exception {
        test("SVGGeometryElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function ScreenOrientation() { [native code] }",
            FF45 = "function ScreenOrientation() {\n    [native code]\n}")
    public void screenOrientation() throws Exception {
        test("ScreenOrientation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MutationObserver() { [native code] }")
    public void webKitMutationObserver() throws Exception {
        test("WebKitMutationObserver");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function AudioContext() { [native code] }")
    public void webkitAudioContext() throws Exception {
        test("webkitAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBCursor() { [native code] }")
    public void webkitIDBCursor() throws Exception {
        test("webkitIDBCursor");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBDatabase() { [native code] }")
    public void webkitIDBDatabase() throws Exception {
        test("webkitIDBDatabase");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBFactory() { [native code] }")
    public void webkitIDBFactory() throws Exception {
        test("webkitIDBFactory");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBIndex() { [native code] }")
    public void webkitIDBIndex() throws Exception {
        test("webkitIDBIndex");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBKeyRange() { [native code] }")
    public void webkitIDBKeyRange() throws Exception {
        test("webkitIDBKeyRange");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBObjectStore() { [native code] }")
    public void webkitIDBObjectStore() throws Exception {
        test("webkitIDBObjectStore");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBRequest() { [native code] }")
    public void webkitIDBRequest() throws Exception {
        test("webkitIDBRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBTransaction() { [native code] }")
    public void webkitIDBTransaction() throws Exception {
        test("webkitIDBTransaction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MediaStream() { [native code] }")
    public void webkitMediaStream() throws Exception {
        test("webkitMediaStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function OfflineAudioContext() { [native code] }")
    public void webkitOfflineAudioContext() throws Exception {
        test("webkitOfflineAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SpeechGrammar() { [native code] }")
    public void webkitSpeechGrammar() throws Exception {
        test("webkitSpeechGrammar");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SpeechGrammarList() { [native code] }")
    public void webkitSpeechGrammarList() throws Exception {
        test("webkitSpeechGrammarList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SpeechRecognitionError() { [native code] }")
    public void webkitSpeechRecognitionError() throws Exception {
        test("webkitSpeechRecognitionError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SpeechRecognitionEvent() { [native code] }")
    public void webkitSpeechRecognitionEvent() throws Exception {
        test("webkitSpeechRecognitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function URL() { [native code] }")
    public void webkitURL() throws Exception {
        test("webkitURL");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function AppBannerPromptResult() { [native code] }")
    public void appBannerPromptResult() throws Exception {
        test("AppBannerPromptResult");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function ServiceWorkerMessageEvent() { [native code] }")
    public void serviceWorkerMessageEvent() throws Exception {
        test("ServiceWorkerMessageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webSockets() throws Exception {
        test("WebSockets");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void telephonyCallGroup() throws Exception {
        test("TelephonyCallGroup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_Int8x16() throws Exception {
        test("SIMD.Int8x16");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webVTT() throws Exception {
        test("WebVTT");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void uDPSocket() throws Exception {
        test("UDPSocket");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webSMS() throws Exception {
        test("WebSMS");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_Int16x8() throws Exception {
        test("SIMD.Int16x8");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void animation() throws Exception {
        test("Animation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "[object InstallTriggerImpl]")
    public void installTrigger() throws Exception {
        test("InstallTrigger");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void installTriggerImpl() throws Exception {
        test("InstallTriggerImpl");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function MozPowerManager() {\n    [native code]\n}")
    public void mozPowerManager() throws Exception {
        test("MozPowerManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void pushRegistrationManager() throws Exception {
        test("PushRegistrationManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_Int32x4() throws Exception {
        test("SIMD.Int32x4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF45 = "function CanvasCaptureMediaStream() {\n    [native code]\n}")
    public void canvasCaptureMediaStream() throws Exception {
        test("CanvasCaptureMediaStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void promiseRejection() throws Exception {
        test("PromiseRejection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_Float64x2() throws Exception {
        test("SIMD.Float64x2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF45 = "function FontFaceSet() {\n    [native code]\n}")
    public void fontFaceSet() throws Exception {
        test("FontFaceSet");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void l10n() throws Exception {
        test("L10n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_Float32x4() throws Exception {
        test("SIMD.Float32x4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void imageBitmapFactories() throws Exception {
        test("ImageBitmapFactories");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void bluetoothGATTRemoteServer() throws Exception {
        test("BluetoothGATTRemoteServer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void beforeInstallPrompt() throws Exception {
        test("BeforeInstallPrompt");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void bluetoothGATTService() throws Exception {
        test("BluetoothGATTService");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void notificationEvent() throws Exception {
        test("NotificationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void indexedDB() throws Exception {
        test("IndexedDB");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void pushMessageData() throws Exception {
        test("PushMessageData");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void bluetoothAdvertisingData() throws Exception {
        test("BluetoothAdvertisingData");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webGL() throws Exception {
        test("WebGL");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void idbLocaleAwareKeyRange() throws Exception {
        test("IDBLocaleAwareKeyRange");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webGL_lose_context() throws Exception {
        test("WEBGL_lose_context");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF45 = "function MediaDeviceInfo() {\n    [native code]\n}")
    public void mediaDeviceInfo() throws Exception {
        test("MediaDeviceInfo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "[object MozCSSKeyframesRule]")
    @NotYetImplemented(FF)
    public void mozCSSKeyframesRule() throws Exception {
        test("MozCSSKeyframesRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function IdleDeadline() { [native code] }")
    public void idleDeadline() throws Exception {
        test("IdleDeadline");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function InputDeviceCapabilities() { [native code] }")
    public void inputDeviceCapabilities() throws Exception {
        test("InputDeviceCapabilities");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function Presentation() { [native code] }")
    public void presentation() throws Exception {
        test("Presentation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PresentationAvailability() { [native code] }")
    public void presentationAvailability() throws Exception {
        test("PresentationAvailability");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PresentationConnection() { [native code] }")
    public void presentationConnection() throws Exception {
        test("PresentationConnection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PresentationConnectionAvailableEvent() { [native code] }")
    public void presentationConnectionAvailableEvent() throws Exception {
        test("PresentationConnectionAvailableEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PresentationRequest() { [native code] }")
    public void presentationRequest() throws Exception {
        test("PresentationRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void speechRecognitionAlternative() throws Exception {
        test("SpeechRecognitionAlternative");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void performanceObserverEntryList() throws Exception {
        test("PerformanceObserverEntryList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_Bool32x4() throws Exception {
        test("SIMD.Bool32x4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_Uint16x8() throws Exception {
        test("SIMD.Uint16x8");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webGL_compressed_texture_pvrtc() throws Exception {
        test("WEBGL_compressed_texture_pvrtc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webGL_compressed_texture_atc() throws Exception {
        test("WEBGL_compressed_texture_atc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void imageBitmapRenderingContext() throws Exception {
        test("ImageBitmapRenderingContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_Uint32x4() throws Exception {
        test("SIMD.Uint32x4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF45 = "function SourceBufferList() {\n    [native code]\n}")
    public void sourceBufferList() throws Exception {
        test("SourceBufferList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void htmlHyperlinkElementUtils() throws Exception {
        test("HTMLHyperlinkElementUtils");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void bluetoothRemoteGATTServer() throws Exception {
        test("BluetoothRemoteGATTServer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void extendableMessageEvent() throws Exception {
        test("ExtendableMessageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void performanceObserver() throws Exception {
        test("PerformanceObserver");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void speechGrammar() throws Exception {
        test("SpeechGrammar");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void oes_texture_half_float_linear() throws Exception {
        test("OES_texture_half_float_linear");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void trackDefault() throws Exception {
        test("TrackDefault");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void speechRecognitionResult() throws Exception {
        test("SpeechRecognitionResult");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void speechGrammarList() throws Exception {
        test("SpeechGrammarList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void oes_texture_half_float() throws Exception {
        test("OES_texture_half_float");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webGL_compressed_texture_es3() throws Exception {
        test("WEBGL_compressed_texture_es3");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void offscreenCanvas() throws Exception {
        test("OffscreenCanvas");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webGL_compressed_texture_etc1() throws Exception {
        test("WEBGL_compressed_texture_etc1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webGL_color_buffer_float() throws Exception {
        test("WEBGL_color_buffer_float");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object OES_texture_float]")
    public void oes_texture_float() throws Exception {
        test("OES_texture_float");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void gestureEvent() throws Exception {
        test("GestureEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void speechRecognitionError() throws Exception {
        test("SpeechRecognitionError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void keyframeEffect() throws Exception {
        test("KeyframeEffect");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void speechRecognitionEvent() throws Exception {
        test("SpeechRecognitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object WEBGL_debug_renderer_info]")
    public void webGL_debug_renderer_info() throws Exception {
        test("WEBGL_debug_renderer_info");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object PerformanceNavigationTiming]")
    public void performanceNavigationTiming() throws Exception {
        test("PerformanceNavigationTiming");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void speechSynthesisVoice() throws Exception {
        test("SpeechSynthesisVoice");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object OES_texture_float_linear]")
    public void oes_texture_float_linear() throws Exception {
        test("OES_texture_float_linear");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_Bool8x16() throws Exception {
        test("SIMD.Bool8x16");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void bluetooth() throws Exception {
        test("Bluetooth");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void trackDefaultList() throws Exception {
        test("TrackDefaultList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void bluetoothRemoteGATTCharacteristic() throws Exception {
        test("BluetoothRemoteGATTCharacteristic");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object EXT_texture_filter_anisotropic]")
    public void ext_texture_filter_anisotropic() throws Exception {
        test("EXT_texture_filter_anisotropic");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void serviceWorkerState() throws Exception {
        test("ServiceWorkerState");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object WEBGL_compressed_texture_s3tc]")
    public void webGL_compressed_texture_s3tc() throws Exception {
        test("WEBGL_compressed_texture_s3tc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void performanceFrameTiming() throws Exception {
        test("PerformanceFrameTiming");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF45 = "function SourceBuffer() {\n    [native code]\n}")
    public void sourceBuffer() throws Exception {
        test("SourceBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void speechRecognitionResultList() throws Exception {
        test("SpeechRecognitionResultList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webGL_debug_shaders() throws Exception {
        test("WEBGL_debug_shaders");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_Bool64x2() throws Exception {
        test("SIMD.Bool64x2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_Uint8x16() throws Exception {
        test("SIMD.Uint8x16");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webGL2RenderingContext() throws Exception {
        test("WebGL2RenderingContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_Bool16x8() throws Exception {
        test("SIMD.Bool16x8");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void rtcIdentityAssertion() throws Exception {
        test("RTCIdentityAssertion");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object MSGestureEvent]")
    public void msGestureEvent() throws Exception {
        test("MSGestureEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function RTCCertificate() { [native code] }",
            FF45 = "function RTCCertificate() {\n    [native code]\n}")
    public void rtcCertificate() throws Exception {
        test("RTCCertificate");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void speechSynthesisErrorEvent() throws Exception {
        test("SpeechSynthesisErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void sharedArrayBuffer() throws Exception {
        test("SharedArrayBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function IDBMutableFile() {\n    [native code]\n}")
    public void idbMutableFile() throws Exception {
        test("IDBMutableFile");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webGL_depth_texture() throws Exception {
        test("WEBGL_depth_texture");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void atomics() throws Exception {
        test("Atomics");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void ext_color_buffer_half_float() throws Exception {
        test("EXT_color_buffer_half_float");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function IIRFilterNode() { [native code] }")
    public void iirFilterNode() throws Exception {
        test("IIRFilterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PromiseRejectionEvent() { [native code] }")
    public void promiseRejectionEvent() throws Exception {
        test("PromiseRejectionEvent");
    }

}
