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
package com.gargoylesoftware.htmlunit.general;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
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
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests the host class names, in Standards Mode.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API">Web API Interfaces</a>
 * @see HostClassNameTest
 */
@RunWith(BrowserRunner.class)
public class HostClassNameStandardsTest extends WebDriverTestCase {

    private void test(final String className) throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><script>\n"
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
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.Attr}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object Attr]",
            CHROME = "function Attr() { [native code] }",
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
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.BeforeUnloadEvent}.
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
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.CDATASection}.
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
            FF38 = "function CSSRuleList() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "[object CSSStyleRule]",
            CHROME = "function CSSStyleRule() { [native code] }")
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
    @Alerts(DEFAULT = "[object CanvasRenderingContext2D]",
            CHROME = "function CanvasRenderingContext2D() { [native code] }",
            FF = "function CanvasRenderingContext2D() {\n    [native code]\n}",
            IE8 = "exception")
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
    @Alerts(DEFAULT = "[object ClientRect]",
            CHROME = "function ClientRect() { [native code] }",
            FF24 = "function ClientRect() {\n    [native code]\n}",
            FF31 = "exception",
            FF38 = "exception",
            IE8 = "exception")
    public void clientRect() throws Exception {
        test("ClientRect");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.Comment}.
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
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.Comment}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
        IE8 = "[object HTMLCommentElement]")
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
    @Alerts(DEFAULT = "exception",
            IE11 = "[object Coordinates]")
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
            FF = "function DOMException() {\n    [native code]\n}",
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
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.Document}.
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
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.DocumentFragment}.
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
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.DocumentType}.
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
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Event}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object Event]",
            CHROME = "function Event() { [native code] }",
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
            IE11 = "[object Geolocation]")
    public void geolocation() throws Exception {
        test("Geolocation");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.HashChangeEvent}.
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
    @Alerts(DEFAULT = "[object HTMLAnchorElement]",
            CHROME = "function HTMLAnchorElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLBRElement]",
            CHROME = "function HTMLBRElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLBaseElement]",
            CHROME = "function HTMLBaseElement() { [native code] }",
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
            IE = "[object HTMLBaseFontElement]")
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
    @Alerts(CHROME = "function HTMLQuoteElement() { [native code] }",
            FF = "function HTMLQuoteElement() {\n    [native code]\n}",
            IE = "[object HTMLQuoteElement]",
            IE8 = "exception")
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
    @Alerts(DEFAULT = "[object HTMLButtonElement]",
            CHROME = "function HTMLButtonElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLAllCollection]",
            CHROME = "function HTMLAllCollection() { [native code] }",
            FF24 = "exception",
            FF31 = "exception",
            FF38 = "function HTMLAllCollection() {\n    [native code]\n}",
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
    @Alerts(DEFAULT = "[object HTMLDListElement]",
            CHROME = "function HTMLDListElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLDocument]",
            CHROME = "function HTMLDocument() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLFieldSetElement]",
            CHROME = "function HTMLFieldSetElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLFontElement]",
            CHROME = "function HTMLFontElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLFormElement]",
            CHROME = "function HTMLFormElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLFrameElement]",
            CHROME = "function HTMLFrameElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLFrameSetElement]",
            CHROME = "function HTMLFrameSetElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLHRElement]",
            CHROME = "function HTMLHRElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLHeadElement]",
            CHROME = "function HTMLHeadElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLHeadingElement]",
            CHROME = "function HTMLHeadingElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLHtmlElement]",
            CHROME = "function HTMLHtmlElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLIFrameElement]",
            CHROME = "function HTMLIFrameElement() { [native code] }",
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
    @Alerts(CHROME = "function HTMLImageElement() { [native code] }",
            FF = "function HTMLImageElement() {\n    [native code]\n}",
            IE = "[object HTMLImageElement]")
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
    @Alerts(DEFAULT = "[object HTMLLIElement]",
            CHROME = "function HTMLLIElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLLabelElement]",
            CHROME = "function HTMLLabelElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLLegendElement]",
            CHROME = "function HTMLLegendElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLLinkElement]",
            CHROME = "function HTMLLinkElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLMapElement]",
            CHROME = "function HTMLMapElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLMarqueeElement]",
            CHROME = "function HTMLMarqueeElement() { [native code] }",
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
            IE8 = "[object HTMLNoShowElement]")
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
    @Alerts(DEFAULT = "[object HTMLOListElement]",
            CHROME = "function HTMLOListElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLObjectElement]",
            CHROME = "function HTMLObjectElement() { [native code] }",
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
            IE = "[object HTMLOptionElement]")
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
    @Alerts(DEFAULT = "[object HTMLParamElement]",
            CHROME = "function HTMLParamElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLSelectElement]",
            CHROME = "function HTMLSelectElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLStyleElement]",
            CHROME = "function HTMLStyleElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLTableCaptionElement]",
            CHROME = "function HTMLTableCaptionElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLTableCellElement]",
            CHROME = "function HTMLTableCellElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLTableColElement]",
            CHROME = "function HTMLTableColElement() { [native code] }",
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
            IE11 = "[object HTMLTableDataCellElement]")
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
            IE11 = "[object HTMLTableHeaderCellElement]")
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
    @Alerts(DEFAULT = "[object HTMLTableSectionElement]",
            CHROME = "function HTMLTableSectionElement() { [native code] }",
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
    @Alerts(DEFAULT = "exception",
            IE8 = "[object HTMLTextElement]")
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
    @Alerts(DEFAULT = "[object HTMLTitleElement]",
            CHROME = "function HTMLTitleElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLUListElement]",
            CHROME = "function HTMLUListElement() { [native code] }",
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
    @Alerts(DEFAULT = "[object HTMLUnknownElement]",
            CHROME = "function HTMLUnknownElement() { [native code] }",
            FF = "function HTMLUnknownElement() {\n    [native code]\n}")
    @NotYetImplemented(IE8)
    public void htmlUnknownElement() throws Exception {
        test("HTMLUnknownElement");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLUnknownElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE8 = "[object HTMLGenericElement]")
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
            FF38 = "function Location() {\n    [native code]\n}")
    public void location() throws Exception {
        test("Location");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.MediaList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object MediaList]",
            CHROME = "function MediaList() { [native code] }",
            FF = "function MediaList() {\n    [native code]\n}",
            IE8 = "exception")
    public void mediaList() throws Exception {
        test("MediaList");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.MessageEvent}.
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
            FF = "function MimeType() {\n    [native code]\n}",
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
            FF = "function MimeTypeArray() {\n    [native code]\n}",
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
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.MutationEvent}.
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
            FF24 = "exception",
            FF31 = "exception",
            FF38 = "function NamedNodeMap() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "[object Node]",
            CHROME = "function Node() { [native code] }",
            FF = "function Node() {\n    [native code]\n}",
            IE8 = "exception")
    public void node() throws Exception {
        test("Node");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.NodeFilter}.
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
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.NodeList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object NodeList]",
            CHROME = "function NodeList() { [native code] }",
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
    @Alerts(DEFAULT = "[object Plugin]",
            CHROME = "function Plugin() { [native code] }",
            FF = "function Plugin() {\n    [native code]\n}",
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
            FF = "function PluginArray() {\n    [native code]\n}",
            IE8 = "exception")
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
            IE11 = "[object PointerEvent]")
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
            IE11 = "[object Position]")
    public void position() throws Exception {
        test("Position");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.ProcessingInstruction}.
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
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.Range}.
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
    @Alerts(DEFAULT = "[object Selection]",
            CHROME = "function Selection() { [native code] }",
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
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.StaticNodeList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE8 = "[object StaticNodeList]")
    @NotYetImplemented({ FF, IE11, CHROME })
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
            FF38 = "function Storage() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "[object Text]",
            CHROME = "function Text() { [native code] }",
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
    @Alerts(DEFAULT = "[object TreeWalker]",
            CHROME = "function TreeWalker() { [native code] }",
            FF = "function TreeWalker() {\n    [native code]\n}",
            IE8 = "exception")
    public void treeWalker() throws Exception {
        test("TreeWalker");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.event.UIEvent}.
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
            FF38 = "function Window() {\n    [native code]\n}")
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
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.XPathEvaluator}.
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
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.XPathNSResolver}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF24 = "[object XPathNSResolver]",
            FF31 = "[object XPathNSResolver]")
    public void xPathNSResolver() throws Exception {
        test("XPathNSResolver");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.dom.XPathResult}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object XPathResult]",
            CHROME = "function XPathResult() { [native code] }",
            FF38 = "function XPathResult() {\n    [native code]\n}",
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
    @Alerts(DEFAULT = "[object XSLTProcessor]",
            CHROME = "function XSLTProcessor() { [native code] }",
            FF38 = "function XSLTProcessor() {\n    [native code]\n}",
            IE = "exception")
    public void xsltProcessor() throws Exception {
        test("XSLTProcessor");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.xml.XSLTemplate}.
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
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.ClientRectList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE11 = "[object ClientRectList]",
            FF24 = "function ClientRectList() {\n    [native code]\n}",
            CHROME = "function ClientRectList() { [native code] }")
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

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.URLSearchParams}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function URLSearchParams() {\n    [native code]\n}")
    public void urlSearchParams() throws Exception {
        test("URLSearchParams");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.intl.Intl}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object Object]",
            IE8 = "exception")
    public void intl() throws Exception {
        test("Intl");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.xml.FormData}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "function FormData() { [native code] }",
            FF = "function FormData() {\n    [native code]\n}",
            IE11 = "\nfunction FormData() {\n    [native code]\n}\n",
            IE8 = "exception")
    public void formData() throws Exception {
        test("FormData");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.MessageChannel}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MessageChannel() { [native code] }",
            IE11 = "\nfunction MessageChannel() {\n    [native code]\n}\n")
    public void messageChannel() throws Exception {
        test("MessageChannel");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.MessagePort}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "function MessagePort() { [native code] }",
            FF = "function MessagePort() {\n    [native code]\n}",
            IE8 = "exception",
            IE11 = "[object MessagePort]")
    public void messagePort() throws Exception {
        test("MessagePort");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.Promise}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "function Promise() { [native code] }",
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
    @Alerts(CHROME = "function Worker() { [native code] }",
            FF = "function Worker() {\n    [native code]\n}",
            IE11 = "\nfunction Worker() {\n    [native code]\n}\n",
            IE8 = "exception")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGAnimatedLengthList() { [native code] }",
            FF = "function SVGAnimatedLengthList() {\n    [native code]\n}",
            IE11 = "[object SVGAnimatedLengthList]")
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
    @Alerts(CHROME = "function RegExp() { [native code] }",
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function AnalyserNode() { [native code] }",
            FF = "function AnalyserNode() {\n    [native code]\n}")
    public void analyserNode() throws Exception {
        test("AnalyserNode");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.css.CSSPageRule}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function CSSPageRule() { [native code] }",
            FF = "[object CSSPageRule]",
            IE11 = "[object CSSPageRule]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function AudioParam() { [native code] }",
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
    @Alerts(CHROME = "function decodeURIComponent() { [native code] }",
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
            CHROME = "function Headers() { [native code] }")
    public void headers() throws Exception {
        test("Headers");
    }

    /**
     * Test {@link com.gargoylesoftware.htmlunit.javascript.host.media.ChannelMergerNode}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function ChannelMergerNode() { [native code] }",
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function CloseEvent() { [native code] }",
            FF = "function CloseEvent() {\n    [native code]\n}",
            IE11 = "[object CloseEvent]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function OfflineAudioContext() { [native code] }",
            FF = "function OfflineAudioContext() {\n    [native code]\n}")
    public void offlineAudioContext() throws Exception {
        test("OfflineAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "[object MozMobileMessageThread]")
    public void mozMobileMessageThread() throws Exception {
        test("MozMobileMessageThread");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function RTCSessionDescription() { [native code] }")
    public void rtcSessionDescription() throws Exception {
        test("RTCSessionDescription");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF31 = "function MozSmsEvent() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGAnimatedRect() { [native code] }",
            FF = "function SVGAnimatedRect() {\n    [native code]\n}",
            IE11 = "[object SVGAnimatedRect]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function ProgressEvent() { [native code] }",
            FF = "function ProgressEvent() {\n    [native code]\n}",
            IE11 = "[object ProgressEvent]")
    public void progressEvent() throws Exception {
        test("ProgressEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MediaKeySession() { [native code] }")
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
    @Alerts(DEFAULT = "exception",
            FF = "function MediaRecorder() {\n    [native code]\n}")
    public void mediaRecorder() throws Exception {
        test("MediaRecorder");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGAnimatedString() { [native code] }",
            FF = "function SVGAnimatedString() {\n    [native code]\n}",
            IE11 = "[object SVGAnimatedString]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBDatabase() { [native code] }",
            FF = "function IDBDatabase() {\n    [native code]\n}",
            IE11 = "[object IDBDatabase]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGAnimatedEnumeration() { [native code] }",
            FF = "function SVGAnimatedEnumeration() {\n    [native code]\n}",
            IE11 = "[object SVGAnimatedEnumeration]")
    public void svgAnimatedEnumeration() throws Exception {
        test("SVGAnimatedEnumeration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function Response() { [native code] }")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBIndex() { [native code] }",
            FF = "function IDBIndex() {\n    [native code]\n}",
            IE11 = "[object IDBIndex]")
    public void idbIndex() throws Exception {
        test("IDBIndex");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function WheelEvent() { [native code] }",
            FF = "function WheelEvent() {\n    [native code]\n}",
            IE11 = "[object WheelEvent]")
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
    @Alerts("exception")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGAnimatedNumberList() { [native code] }",
            FF = "function SVGAnimatedNumberList() {\n    [native code]\n}",
            IE11 = "[object SVGAnimatedNumberList]")
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
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBFactory() { [native code] }",
            FF = "function IDBFactory() {\n    [native code]\n}",
            IE11 = "[object IDBFactory]")
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
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function AnimationEvent() { [native code] }",
            FF = "function AnimationEvent() {\n    [native code]\n}",
            IE11 = "[object AnimationEvent]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function () { [native code] }",
            FF = "function DateTimeFormat() {\n    [native code]\n}",
            IE11 = "\nfunction DateTimeFormat() {\n    [native code]\n}\n")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function () { [native code] }",
            FF = "function Collator() {\n    [native code]\n}",
            IE11 = "\nfunction Collator() {\n    [native code]\n}\n")
    public void intl_Collator() throws Exception {
        test("Intl.Collator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF31 = "[object MozSmsFilter]")
    public void mozSmsFilter() throws Exception {
        test("MozSmsFilter");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MutationObserver() { [native code] }",
            FF = "function MutationObserver() {\n    [native code]\n}",
            IE11 = "\nfunction MutationObserver() {\n    [native code]\n}\n")
    public void mutationObserver() throws Exception {
        test("MutationObserver");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MutationRecord() { [native code] }",
            FF = "function MutationRecord() {\n    [native code]\n}",
            IE11 = "[object MutationRecord]")
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
            FF38 = "function DOMMatrixReadOnly() {\n    [native code]\n}")
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
    @Alerts(CHROME = "function isFinite() { [native code] }",
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
    @Alerts(CHROME = "function String() { [native code] }",
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function StyleSheet() { [native code] }",
            FF = "function StyleSheet() {\n    [native code]\n}",
            IE11 = "[object StyleSheet]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGPreserveAspectRatio() { [native code] }",
            FF = "function SVGPreserveAspectRatio() {\n    [native code]\n}",
            IE11 = "[object SVGPreserveAspectRatio]")
    public void svgPreserveAspectRatio() throws Exception {
        test("SVGPreserveAspectRatio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "function parseFloat() { [native code] }",
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
    @Alerts(CHROME = "function parseInt() { [native code] }",
            FF = "function parseInt() {\n    [native code]\n}",
            IE = "\nfunction parseInt() {\n    [native code]\n}\n")
    public void parseInt() throws Exception {
        test("parseInt");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF31 = "function LockedFile() {\n    [native code]\n}")
    public void lockedFile() throws Exception {
        test("LockedFile");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function AudioProcessingEvent() { [native code] }",
            FF = "function AudioProcessingEvent() {\n    [native code]\n}")
    public void audioProcessingEvent() throws Exception {
        test("AudioProcessingEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function CanvasGradient() { [native code] }",
            FF = "function CanvasGradient() {\n    [native code]\n}",
            IE11 = "[object CanvasGradient]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function FileReader() { [native code] }",
            FF = "function FileReader() {\n    [native code]\n}",
            IE11 = "\nfunction FileReader() {\n    [native code]\n}\n")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function AudioNode() { [native code] }",
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function BiquadFilterNode() { [native code] }",
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function TimeRanges() { [native code] }",
            FF = "function TimeRanges() {\n    [native code]\n}",
            IE11 = "[object TimeRanges]")
    public void timeRanges() throws Exception {
        test("TimeRanges");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function AudioContext() { [native code] }",
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
    @Alerts(CHROME = "function RangeError() { [native code] }",
            FF = "function RangeError() {\n    [native code]\n}",
            IE11 = "\nfunction RangeError() {\n    [native code]\n}\n",
            IE8 = "RangeError")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGAnimatedLength() { [native code] }",
            FF = "function SVGAnimatedLength() {\n    [native code]\n}",
            IE11 = "[object SVGAnimatedLength]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function CSSSupportsRule() { [native code] }",
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function DynamicsCompressorNode() { [native code] }",
            FF = "function DynamicsCompressorNode() {\n    [native code]\n}")
    public void dynamicsCompressorNode() throws Exception {
        test("DynamicsCompressorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGStringList() { [native code] }",
            FF = "function SVGStringList() {\n    [native code]\n}",
            IE11 = "[object SVGStringList]")
    public void svgStringList() throws Exception {
        test("SVGStringList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function DOMError() { [native code] }",
            FF = "function DOMError() {\n    [native code]\n}",
            IE11 = "[object DOMError]")
    public void domError() throws Exception {
        test("DOMError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function EventTarget() { [native code] }",
            FF = "function EventTarget() {\n    [native code]\n}")
    public void eventTarget() throws Exception {
        test("EventTarget");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MediaElementAudioSourceNode() { [native code] }",
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGAnimatedInteger() { [native code] }",
            FF = "function SVGAnimatedInteger() {\n    [native code]\n}",
            IE11 = "[object SVGAnimatedInteger]")
    public void svgAnimatedInteger() throws Exception {
        test("SVGAnimatedInteger");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MediaKeyStatusMap() { [native code] }")
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
    @Alerts(CHROME = "function URIError() { [native code] }",
            FF = "function URIError() {\n    [native code]\n}",
            IE11 = "\nfunction URIError() {\n    [native code]\n}\n",
            IE8 = "URIError")
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
            FF38 = "function HTMLPictureElement() {\n    [native code]\n}")
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
            CHROME = "function MediaKeyMessageEvent() { [native code] }")
    public void mediaKeyMessageEvent() throws Exception {
        test("MediaKeyMessageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MediaStreamAudioSourceNode() { [native code] }",
            FF = "function MediaStreamAudioSourceNode() {\n    [native code]\n}")
    public void mediaStreamAudioSourceNode() throws Exception {
        test("MediaStreamAudioSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function AudioListener() { [native code] }",
            FF = "function AudioListener() {\n    [native code]\n}")
    public void audioListener() throws Exception {
        test("AudioListener");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function PannerNode() {\n    [native code]\n}")
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
    @Alerts(CHROME = "function decodeURI() { [native code] }",
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
            CHROME = "function MediaKeys() { [native code] }")
    public void mediaKeys() throws Exception {
        test("MediaKeys");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PerformanceTiming() { [native code] }",
            FF = "function PerformanceTiming() {\n    [native code]\n}",
            IE11 = "[object PerformanceTiming]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBObjectStore() { [native code] }",
            FF = "function IDBObjectStore() {\n    [native code]\n}",
            IE11 = "[object IDBObjectStore]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function DeviceMotionEvent() { [native code] }",
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBRequest() { [native code] }",
            FF = "function IDBRequest() {\n    [native code]\n}",
            IE11 = "[object IDBRequest]")
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
    @Alerts(CHROME = "function SyntaxError() { [native code] }",
            FF = "function SyntaxError() {\n    [native code]\n}",
            IE11 = "\nfunction SyntaxError() {\n    [native code]\n}\n",
            IE8 = "SyntaxError")
    public void syntaxError() throws Exception {
        test("SyntaxError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function CSSKeyframeRule() { [native code] }",
            IE11 = "[object CSSKeyframeRule]")
    public void cssKeyframeRule() throws Exception {
        test("CSSKeyframeRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF38 = "function BroadcastChannel() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function FocusEvent() { [native code] }",
            FF = "function FocusEvent() {\n    [native code]\n}",
            IE11 = "[object FocusEvent]")
    public void focusEvent() throws Exception {
        test("FocusEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function DOMStringList() { [native code] }",
            FF = "function DOMStringList() {\n    [native code]\n}",
            IE11 = "[object DOMStringList]")
    public void domStringList() throws Exception {
        test("DOMStringList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF31 = "function MozMmsEvent() {\n    [native code]\n}")
    public void mozMmsEvent() throws Exception {
        test("MozMmsEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function OfflineAudioCompletionEvent() { [native code] }",
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function DeviceOrientationEvent() { [native code] }",
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function StorageEvent() { [native code] }",
            FF = "function StorageEvent() {\n    [native code]\n}",
            IE11 = "[object StorageEvent]")
    public void storageEvent() throws Exception {
        test("StorageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBOpenDBRequest() { [native code] }",
            FF = "function IDBOpenDBRequest() {\n    [native code]\n}",
            IE11 = "[object IDBOpenDBRequest]")
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
    @Alerts(DEFAULT = "exception",
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function GainNode() { [native code] }",
            FF = "function GainNode() {\n    [native code]\n}")
    public void gainNode() throws Exception {
        test("GainNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "function Number() { [native code] }",
            FF = "function Number() {\n    [native code]\n}",
            IE = "\nfunction Number() {\n    [native code]\n}\n")
    public void number() throws Exception {
        test("Number");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function Map() { [native code] }",
            FF = "function Map() {\n    [native code]\n}",
            IE11 = "\nfunction Map() {\n    [native code]\n}\n")
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
            CHROME = "function Cache() { [native code] }")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGTransformList() { [native code] }",
            FF = "function SVGTransformList() {\n    [native code]\n}",
            IE11 = "[object SVGTransformList]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function () { [native code] }",
            FF = "function NumberFormat() {\n    [native code]\n}",
            IE11 = "\nfunction NumberFormat() {\n    [native code]\n}\n")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBVersionChangeEvent() { [native code] }",
            FF = "function IDBVersionChangeEvent() {\n    [native code]\n}",
            IE11 = "[object IDBVersionChangeEvent]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGAnimatedAngle() { [native code] }",
            FF = "function SVGAnimatedAngle() {\n    [native code]\n}",
            IE11 = "[object SVGAnimatedAngle]")
    public void svgAnimatedAngle() throws Exception {
        test("SVGAnimatedAngle");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF31 = "[object MozMobileMessageManager]")
    public void mozMobileMessageManager() throws Exception {
        test("MozMobileMessageManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function ConvolverNode() { [native code] }",
            FF = "function ConvolverNode() {\n    [native code]\n}")
    public void convolverNode() throws Exception {
        test("ConvolverNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function CSSKeyframesRule() { [native code] }",
            IE11 = "[object CSSKeyframesRule]")
    public void cssKeyframesRule() throws Exception {
        test("CSSKeyframesRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function WaveShaperNode() { [native code] }",
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function SubtleCrypto() { [native code] }",
            FF38 = "function SubtleCrypto() {\n    [native code]\n}",
            IE11 = "[object SubtleCrypto]")
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
            FF38 = "function DOMMatrix() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGTransform() { [native code] }",
            FF = "function SVGTransform() {\n    [native code]\n}",
            IE11 = "[object SVGTransform]")
    public void svgTransform() throws Exception {
        test("SVGTransform");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MediaKeySystemAccess() { [native code] }")
    public void mediaKeySystemAccess() throws Exception {
        test("MediaKeySystemAccess");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "function encodeURI() { [native code] }",
            FF = "function encodeURI() {\n    [native code]\n}",
            IE = "\nfunction encodeURI() {\n    [native code]\n}\n")
    public void encodeURI() throws Exception {
        test("encodeURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "function EvalError() { [native code] }",
            FF = "function EvalError() {\n    [native code]\n}",
            IE11 = "\nfunction EvalError() {\n    [native code]\n}\n",
            IE8 = "EvalError")
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
    @Alerts(CHROME = "function Array() { [native code] }",
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function PageTransitionEvent() { [native code] }",
            FF = "function PageTransitionEvent() {\n    [native code]\n}",
            IE11 = "[object PageTransitionEvent]")
    public void pageTransitionEvent() throws Exception {
        test("PageTransitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGAnimatedNumber() { [native code] }",
            FF = "function SVGAnimatedNumber() {\n    [native code]\n}",
            IE11 = "[object SVGAnimatedNumber]")
    public void svgAnimatedNumber() throws Exception {
        test("SVGAnimatedNumber");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PeriodicWave() { [native code] }",
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
    @Alerts("exception")
    public void rtcPeerConnection() throws Exception {
        test("RTCPeerConnection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function CryptoKey() { [native code] }",
            FF38 = "function CryptoKey() {\n    [native code]\n}")
    public void cryptoKey() throws Exception {
        test("CryptoKey");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGTextPositioningElement() { [native code] }",
            FF = "function SVGTextPositioningElement() {\n    [native code]\n}",
            IE11 = "[object SVGTextPositioningElement]")
    public void svgTextPositioningElement() throws Exception {
        test("SVGTextPositioningElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "function ReferenceError() { [native code] }",
            FF = "function ReferenceError() {\n    [native code]\n}",
            IE11 = "\nfunction ReferenceError() {\n    [native code]\n}\n",
            IE8 = "ReferenceError")
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
    @Alerts(CHROME = "function eval() { [native code] }",
            FF = "function eval() {\n    [native code]\n}",
            IE = "\nfunction eval() {\n    [native code]\n}\n")
    public void eval() throws Exception {
        test("eval");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function ValidityState() { [native code] }",
            FF = "function ValidityState() {\n    [native code]\n}",
            IE11 = "[object ValidityState]")
    public void validityState() throws Exception {
        test("ValidityState");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF31 = "[object MozSmsSegmentInfo]")
    public void mozSmsSegmentInfo() throws Exception {
        test("MozSmsSegmentInfo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF31 = "function FileRequest() {\n    [native code]\n}")
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
    @Alerts(CHROME = "function isNaN() { [native code] }",
            FF = "function isNaN() {\n    [native code]\n}",
            IE = "\nfunction isNaN() {\n    [native code]\n}\n")
    public void isNaN() throws Exception {
        test("isNaN");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function WeakSet() { [native code] }",
            FF38 = "function WeakSet() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "exception",
            FF31 = "function DeviceStorage() {\n    [native code]\n}")
    public void deviceStorage() throws Exception {
        test("DeviceStorage");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function TextMetrics() { [native code] }",
            FF = "function TextMetrics() {\n    [native code]\n}",
            IE11 = "[object TextMetrics]")
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
    @Alerts(CHROME = "function Function() { [native code] }",
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
            IE11 = "[object MouseWheelEvent]")
    public void mouseWheelEvent() throws Exception {
        test("MouseWheelEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE11 = "[object PositionError]")
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
    @Alerts(DEFAULT = "exception",
            IE11 = "[object CSSNamespaceRule]")
    public void cssNamespaceRule() throws Exception {
        test("CSSNamespaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void deviceAcceleration() throws Exception {
        test("DeviceAcceleration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF31 = "function DeviceStorageChangeEvent() {\n    [native code]\n}")
    public void deviceStorageChangeEvent() throws Exception {
        test("DeviceStorageChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF31 = "function FileHandle() {\n    [native code]\n}")
    public void fileHandle() throws Exception {
        test("FileHandle");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGNumberList() { [native code] }",
            FF = "function SVGNumberList() {\n    [native code]\n}",
            IE11 = "[object SVGNumberList]")
    public void svgNumberList() throws Exception {
        test("SVGNumberList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function GamepadEvent() { [native code] }",
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function CustomEvent() { [native code] }",
            FF = "function CustomEvent() {\n    [native code]\n}",
            IE11 = "[object CustomEvent]")
    public void customEvent() throws Exception {
        test("CustomEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void deviceRotationRate() throws Exception {
        test("DeviceRotationRate");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function ErrorEvent() { [native code] }",
            FF = "function ErrorEvent() {\n    [native code]\n}",
            IE11 = "[object ErrorEvent]")
    public void errorEvent() throws Exception {
        test("ErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGAnimatedBoolean() { [native code] }",
            FF = "function SVGAnimatedBoolean() {\n    [native code]\n}",
            IE11 = "[object SVGAnimatedBoolean]")
    public void svgAnimatedBoolean() throws Exception {
        test("SVGAnimatedBoolean");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function Set() { [native code] }",
            FF = "function Set() {\n    [native code]\n}",
            IE11 = "\nfunction Set() {\n    [native code]\n}\n")
    public void set() throws Exception {
        test("Set");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGAnimatedPreserveAspectRatio() { [native code] }",
            FF = "function SVGAnimatedPreserveAspectRatio() {\n    [native code]\n}",
            IE11 = "[object SVGAnimatedPreserveAspectRatio]")
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
            FF = "[object MozMmsMessage]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function MediaQueryList() { [native code] }",
            FF = "function MediaQueryList() {\n    [native code]\n}",
            IE11 = "[object MediaQueryList]")
    public void mediaQueryList() throws Exception {
        test("MediaQueryList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function TransitionEvent() { [native code] }",
            FF = "function TransitionEvent() {\n    [native code]\n}",
            IE11 = "[object TransitionEvent]")
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
    @Alerts(DEFAULT = "exception",
            IE8 = "[object XDomainRequest]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function CharacterData() { [native code] }",
            FF = "function CharacterData() {\n    [native code]\n}",
            IE11 = "[object CharacterData]")
    public void characterData() throws Exception {
        test("CharacterData");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object SVGNumber]",
            IE8 = "exception",
            CHROME = "function SVGNumber() { [native code] }",
            FF38 = "function SVGNumber() {\n    [native code]\n}")
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
            FF38 = "function StereoPannerNode() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function NodeIterator() { [native code] }",
            FF = "function NodeIterator() {\n    [native code]\n}",
            IE11 = "[object NodeIterator]")
    public void nodeIterator() throws Exception {
        test("NodeIterator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function XMLHttpRequestEventTarget() { [native code] }",
            FF38 = "function XMLHttpRequestEventTarget() {\n    [native code]\n}",
            IE11 = "[object XMLHttpRequestEventTarget]")
    public void xmlHttpRequestEventTarget() throws Exception {
        test("XMLHttpRequestEventTarget");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function Performance() { [native code] }",
            FF = "function Performance() {\n    [native code]\n}",
            IE11 = "[object Performance]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function AudioDestinationNode() { [native code] }",
            FF = "function AudioDestinationNode() {\n    [native code]\n}")
    public void audioDestinationNode() throws Exception {
        test("AudioDestinationNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function CompositionEvent() { [native code] }",
            FF = "function CompositionEvent() {\n    [native code]\n}",
            IE11 = "[object CompositionEvent]")
    public void compositionEvent() throws Exception {
        test("CompositionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function RadioNodeList() { [native code] }",
            FF38 = "function RadioNodeList() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function WeakMap() { [native code] }",
            FF = "function WeakMap() {\n    [native code]\n}",
            IE11 = "\nfunction WeakMap() {\n    [native code]\n}\n")
    public void weakMap() throws Exception {
        test("WeakMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MediaSource() { [native code] }")
    public void mediaSource() throws Exception {
        test("MediaSource");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBKeyRange() { [native code] }",
            FF = "function IDBKeyRange() {\n    [native code]\n}",
            IE11 = "[object IDBKeyRange]")
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
            FF38 = "[object CSSCounterStyleRule]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function AudioBufferSourceNode() { [native code] }",
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
    @NotYetImplemented({ CHROME, IE })
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
    @Alerts(CHROME = "function Date() { [native code] }",
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function WebGLRenderingContext() { [native code] }",
            FF = "function WebGLRenderingContext() {\n    [native code]\n}",
            IE11 = "[object WebGLRenderingContext]")
    public void webGLRenderingContext() throws Exception {
        test("WebGLRenderingContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF38 = "function MediaDevices() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function Blob() { [native code] }",
            FF31 = "[object Blob]",
            FF38 = "function Blob() {\n    [native code]\n}",
            IE11 = "\nfunction Blob() {\n    [native code]\n}\n")
    public void blob() throws Exception {
        test("Blob");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGAnimatedTransformList() { [native code] }",
            FF = "function SVGAnimatedTransformList() {\n    [native code]\n}",
            IE11 = "[object SVGAnimatedTransformList]")
    public void svgAnimatedTransformList() throws Exception {
        test("SVGAnimatedTransformList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function OscillatorNode() { [native code] }",
            FF = "function OscillatorNode() {\n    [native code]\n}")
    public void oscillatorNode() throws Exception {
        test("OscillatorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void reflect() throws Exception {
        test("Reflect");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MediaStreamTrack() { [native code] }",
            FF = "function MediaStreamTrack() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function PerformanceNavigation() { [native code] }",
            FF = "function PerformanceNavigation() {\n    [native code]\n}",
            IE11 = "[object PerformanceNavigation]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function DelayNode() { [native code] }",
            FF = "function DelayNode() {\n    [native code]\n}")
    public void delayNode() throws Exception {
        test("DelayNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function Request() { [native code] }")
    public void request() throws Exception {
        test("Request");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function CacheStorage() { [native code] }")
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
    @Alerts(CHROME = "function escape() { [native code] }",
            FF = "function escape() {\n    [native code]\n}",
            IE = "\nfunction escape() {\n    [native code]\n}\n")
    public void escape() throws Exception {
        test("escape");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function ClipboardEvent() { [native code] }",
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
            FF = "function MediaStream() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function AudioBuffer() { [native code] }",
            FF = "function AudioBuffer() {\n    [native code]\n}")
    public void audioBuffer() throws Exception {
        test("AudioBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function URL() { [native code] }",
            FF = "function URL() {\n    [native code]\n}",
            IE11 = "[object URL]")
    public void url() throws Exception {
        test("URL");
    }

    /**
     * Tests {@link com.gargoylesoftware.htmlunit.javascript.host.GamepadButton}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function GamepadButton() { [native code] }",
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBTransaction() { [native code] }",
            FF = "function IDBTransaction() {\n    [native code]\n}",
            IE11 = "[object IDBTransaction]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBCursorWithValue() { [native code] }",
            FF = "function IDBCursorWithValue() {\n    [native code]\n}",
            IE11 = "[object IDBCursorWithValue]")
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
            FF = "[object CSSConditionRule]")
    public void cssConditionRule() throws Exception {
        test("CSSConditionRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGLengthList() { [native code] }",
            FF = "function SVGLengthList() {\n    [native code]\n}",
            IE11 = "[object SVGLengthList]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGGradientElement() { [native code] }",
            FF = "function SVGGradientElement() {\n    [native code]\n}",
            IE11 = "[object SVGGradientElement]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGLength() { [native code] }",
            FF = "function SVGLength() {\n    [native code]\n}",
            IE11 = "[object SVGLength]")
    public void svgLength() throws Exception {
        test("SVGLength");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBCursor() { [native code] }",
            FF = "function IDBCursor() {\n    [native code]\n}",
            IE11 = "[object IDBCursor]")
    public void idbCursor() throws Exception {
        test("IDBCursor");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function Crypto() { [native code] }",
            FF = "function Crypto() {\n    [native code]\n}",
            IE11 = "[object Crypto]")
    public void crypto() throws Exception {
        test("Crypto");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function BlobEvent() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGPoint() { [native code] }",
            FF = "function SVGPoint() {\n    [native code]\n}",
            IE11 = "[object SVGPoint]")
    public void svgPoint() throws Exception {
        test("SVGPoint");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "[object CSS]",
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
    @Alerts(CHROME = "function DataTransfer() { [native code] }",
            FF = "function DataTransfer() {\n    [native code]\n}",
            IE = "[object DataTransfer]")
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
    @Alerts(CHROME = "function TypeError() { [native code] }",
            FF = "function TypeError() {\n    [native code]\n}",
            IE11 = "\nfunction TypeError() {\n    [native code]\n}\n",
            IE8 = "TypeError")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function Symbol() { [native code] }",
            FF38 = "function Symbol() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function ImageData() { [native code] }",
            FF = "function ImageData() {\n    [native code]\n}",
            IE11 = "[object ImageData]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function Gamepad() { [native code] }",
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
    @Alerts(CHROME = "function encodeURIComponent() { [native code] }",
            FF = "function encodeURIComponent() {\n    [native code]\n}",
            IE = "\nfunction encodeURIComponent() {\n    [native code]\n}\n")
    public void encodeURIComponent() throws Exception {
        test("encodeURIComponent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function Proxy() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function ChannelSplitterNode() { [native code] }",
            FF = "function ChannelSplitterNode() {\n    [native code]\n}")
    public void channelSplitterNode() throws Exception {
        test("ChannelSplitterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PopStateEvent() { [native code] }",
            FF = "function PopStateEvent() {\n    [native code]\n}",
            IE11 = "[object PopStateEvent]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function FileList() { [native code] }",
            FF = "function FileList() {\n    [native code]\n}",
            IE11 = "[object FileList]")
    public void fileList() throws Exception {
        test("FileList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "function unescape() { [native code] }",
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function File() { [native code] }",
            FF31 = "[object File]",
            FF38 = "function File() {\n    [native code]\n}",
            IE11 = "[object File]")
    public void file() throws Exception {
        test("File");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "[object MozSmsMessage]")
    public void mozSmsMessage() throws Exception {
        test("MozSmsMessage");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function CanvasPattern() { [native code] }",
            FF = "function CanvasPattern() {\n    [native code]\n}",
            IE11 = "[object CanvasPattern]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function ScriptProcessorNode() { [native code] }",
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
            FF = "function DragEvent() {\n    [native code]\n}",
            IE11 = "[object DragEvent]")
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
            CHROME = "function RTCIceCandidate() { [native code] }")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function BarProp() { [native code] }",
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
            IE11 = "[object StyleMedia]")
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
    @Alerts("exception")
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
            FF31 = "function DOMPointReadOnly() {\n    [native code]\n}",
            FF38 = "function DOMPointReadOnly() {\n    [native code]\n}")
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
            FF31 = "function DOMPoint() {\n    [native code]\n}",
            FF38 = "function DOMPoint() {\n    [native code]\n}")
    public void domPoint() throws Exception {
        test("DOMPoint");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
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
            FF31 = "function DOMRectReadOnly() {\n    [native code]\n}",
            FF38 = "function DOMRectReadOnly() {\n    [native code]\n}")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function CSSUnknownRule() { [native code] }")
    public void cssUnknownRule() throws Exception {
        test("CSSUnknownRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function DOMSettableTokenList() { [native code] }",
            FF = "function DOMSettableTokenList() {\n    [native code]\n}",
            IE11 = "[object DOMSettableTokenList]")
    public void domSettableTokenList() throws Exception {
        test("DOMSettableTokenList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function DataTransferItem() { [native code] }")
    public void dataTransferItem() throws Exception {
        test("DataTransferItem");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function DataTransferItemList() { [native code] }")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function MediaError() { [native code] }",
            FF = "function MediaError() {\n    [native code]\n}",
            IE11 = "[object MediaError]")
    public void mediaError() throws Exception {
        test("MediaError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MediaKeyError() { [native code] }")
    public void mediaKeyError() throws Exception {
        test("MediaKeyError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MediaKeyEvent() { [native code] }")
    public void mediaKeyEvent() throws Exception {
        test("MediaKeyEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void overflowEvent() throws Exception {
        test("OverflowEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PerformanceEntry() { [native code] }",
            FF = "function PerformanceEntry() {\n    [native code]\n}",
            IE11 = "[object PerformanceEntry]")
    public void performanceEntry() throws Exception {
        test("PerformanceEntry");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PerformanceMark() { [native code] }",
            FF38 = "function PerformanceMark() {\n    [native code]\n}",
            IE11 = "[object PerformanceMark]")
    public void performanceMark() throws Exception {
        test("PerformanceMark");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PerformanceMeasure() { [native code] }",
            FF38 = "function PerformanceMeasure() {\n    [native code]\n}",
            IE11 = "[object PerformanceMeasure]")
    public void performanceMeasure() throws Exception {
        test("PerformanceMeasure");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PerformanceResourceTiming() { [native code] }",
            FF = "function PerformanceResourceTiming() {\n    [native code]\n}",
            IE11 = "[object PerformanceResourceTiming]")
    public void performanceResourceTiming() throws Exception {
        test("PerformanceResourceTiming");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGComponentTransferFunctionElement() { [native code] }",
            FF = "function SVGComponentTransferFunctionElement() {\n    [native code]\n}",
            IE11 = "[object SVGComponentTransferFunctionElement]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGPathSeg() { [native code] }",
            FF = "function SVGPathSeg() {\n    [native code]\n}",
            IE11 = "[object SVGPathSeg]")
    public void svgPathSeg() throws Exception {
        test("SVGPathSeg");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGPathSegArcAbs() { [native code] }",
            FF = "function SVGPathSegArcAbs() {\n    [native code]\n}",
            IE11 = "[object SVGPathSegArcAbs]")
    public void svgPathSegArcAbs() throws Exception {
        test("SVGPathSegArcAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGPathSegArcRel() { [native code] }",
            FF = "function SVGPathSegArcRel() {\n    [native code]\n}",
            IE11 = "[object SVGPathSegArcRel]")
    public void svgPathSegArcRel() throws Exception {
        test("SVGPathSegArcRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGPathSegClosePath() { [native code] }",
            FF = "function SVGPathSegClosePath() {\n    [native code]\n}",
            IE11 = "[object SVGPathSegClosePath]")
    public void svgPathSegClosePath() throws Exception {
        test("SVGPathSegClosePath");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGPathSegCurvetoCubicAbs() { [native code] }",
            FF = "function SVGPathSegCurvetoCubicAbs() {\n    [native code]\n}",
            IE11 = "[object SVGPathSegCurvetoCubicAbs]")
    public void svgPathSegCurvetoCubicAbs() throws Exception {
        test("SVGPathSegCurvetoCubicAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGPathSegCurvetoCubicRel() { [native code] }",
            FF = "function SVGPathSegCurvetoCubicRel() {\n    [native code]\n}",
            IE11 = "[object SVGPathSegCurvetoCubicRel]")
    public void svgPathSegCurvetoCubicRel() throws Exception {
        test("SVGPathSegCurvetoCubicRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGPathSegCurvetoCubicSmoothAbs() { [native code] }",
            FF = "function SVGPathSegCurvetoCubicSmoothAbs() {\n    [native code]\n}",
            IE11 = "[object SVGPathSegCurvetoCubicSmoothAbs]")
    public void svgPathSegCurvetoCubicSmoothAbs() throws Exception {
        test("SVGPathSegCurvetoCubicSmoothAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGPathSegCurvetoCubicSmoothRel() { [native code] }",
            FF = "function SVGPathSegCurvetoCubicSmoothRel() {\n    [native code]\n}",
            IE11 = "[object SVGPathSegCurvetoCubicSmoothRel]")
    public void svgPathSegCurvetoCubicSmoothRel() throws Exception {
        test("SVGPathSegCurvetoCubicSmoothRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGPathSegCurvetoQuadraticAbs() { [native code] }",
            FF = "function SVGPathSegCurvetoQuadraticAbs() {\n    [native code]\n}",
            IE11 = "[object SVGPathSegCurvetoQuadraticAbs]")
    public void svgPathSegCurvetoQuadraticAbs() throws Exception {
        test("SVGPathSegCurvetoQuadraticAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGPathSegCurvetoQuadraticRel() { [native code] }",
            FF = "function SVGPathSegCurvetoQuadraticRel() {\n    [native code]\n}",
            IE11 = "[object SVGPathSegCurvetoQuadraticRel]")
    public void svgPathSegCurvetoQuadraticRel() throws Exception {
        test("SVGPathSegCurvetoQuadraticRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGPathSegCurvetoQuadraticSmoothAbs() { [native code] }",
            FF = "function SVGPathSegCurvetoQuadraticSmoothAbs() {\n    [native code]\n}",
            IE11 = "[object SVGPathSegCurvetoQuadraticSmoothAbs]")
    public void svgPathSegCurvetoQuadraticSmoothAbs() throws Exception {
        test("SVGPathSegCurvetoQuadraticSmoothAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGPathSegCurvetoQuadraticSmoothRel() { [native code] }",
            FF = "function SVGPathSegCurvetoQuadraticSmoothRel() {\n    [native code]\n}",
            IE11 = "[object SVGPathSegCurvetoQuadraticSmoothRel]")
    public void svgPathSegCurvetoQuadraticSmoothRel() throws Exception {
        test("SVGPathSegCurvetoQuadraticSmoothRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGPathSegLinetoAbs() { [native code] }",
            FF = "function SVGPathSegLinetoAbs() {\n    [native code]\n}",
            IE11 = "[object SVGPathSegLinetoAbs]")
    public void svgPathSegLinetoAbs() throws Exception {
        test("SVGPathSegLinetoAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGPathSegLinetoHorizontalAbs() { [native code] }",
            FF = "function SVGPathSegLinetoHorizontalAbs() {\n    [native code]\n}",
            IE11 = "[object SVGPathSegLinetoHorizontalAbs]")
    public void svgPathSegLinetoHorizontalAbs() throws Exception {
        test("SVGPathSegLinetoHorizontalAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGPathSegLinetoHorizontalRel() { [native code] }",
            FF = "function SVGPathSegLinetoHorizontalRel() {\n    [native code]\n}",
            IE11 = "[object SVGPathSegLinetoHorizontalRel]")
    public void svgPathSegLinetoHorizontalRel() throws Exception {
        test("SVGPathSegLinetoHorizontalRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGPathSegLinetoRel() { [native code] }",
            FF = "function SVGPathSegLinetoRel() {\n    [native code]\n}",
            IE11 = "[object SVGPathSegLinetoRel]")
    public void svgPathSegLinetoRel() throws Exception {
        test("SVGPathSegLinetoRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGPathSegLinetoVerticalAbs() { [native code] }",
            FF = "function SVGPathSegLinetoVerticalAbs() {\n    [native code]\n}",
            IE11 = "[object SVGPathSegLinetoVerticalAbs]")
    public void svgPathSegLinetoVerticalAbs() throws Exception {
        test("SVGPathSegLinetoVerticalAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGPathSegLinetoVerticalRel() { [native code] }",
            FF = "function SVGPathSegLinetoVerticalRel() {\n    [native code]\n}",
            IE11 = "[object SVGPathSegLinetoVerticalRel]")
    public void svgPathSegLinetoVerticalRel() throws Exception {
        test("SVGPathSegLinetoVerticalRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGPathSegList() { [native code] }",
            FF = "function SVGPathSegList() {\n    [native code]\n}",
            IE11 = "[object SVGPathSegList]")
    public void svgPathSegList() throws Exception {
        test("SVGPathSegList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGPathSegMovetoAbs() { [native code] }",
            FF = "function SVGPathSegMovetoAbs() {\n    [native code]\n}",
            IE11 = "[object SVGPathSegMovetoAbs]")
    public void svgPathSegMovetoAbs() throws Exception {
        test("SVGPathSegMovetoAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGPathSegMovetoRel() { [native code] }",
            FF = "function SVGPathSegMovetoRel() {\n    [native code]\n}",
            IE11 = "[object SVGPathSegMovetoRel]")
    public void svgPathSegMovetoRel() throws Exception {
        test("SVGPathSegMovetoRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGPointList() { [native code] }",
            FF = "function SVGPointList() {\n    [native code]\n}",
            IE11 = "[object SVGPointList]")
    public void svgPointList() throws Exception {
        test("SVGPointList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGRenderingIntent() { [native code] }")
    public void svgRenderingIntent() throws Exception {
        test("SVGRenderingIntent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGTextContentElement() { [native code] }",
            FF = "function SVGTextContentElement() {\n    [native code]\n}",
            IE11 = "[object SVGTextContentElement]")
    public void svgTextContentElement() throws Exception {
        test("SVGTextContentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGUnitTypes() { [native code] }",
            FF = "function SVGUnitTypes() {\n    [native code]\n}",
            IE11 = "[object SVGUnitTypes]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function SVGZoomEvent() { [native code] }",
            FF = "function SVGZoomEvent() {\n    [native code]\n}",
            IE11 = "[object SVGZoomEvent]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function TextEvent() { [native code] }",
            IE11 = "[object TextEvent]")
    public void textEvent() throws Exception {
        test("TextEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function TextTrack() { [native code] }",
            FF = "function TextTrack() {\n    [native code]\n}",
            IE11 = "[object TextTrack]")
    public void textTrack() throws Exception {
        test("TextTrack");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function TextTrackCue() { [native code] }",
            IE11 = "\nfunction TextTrackCue() {\n    [native code]\n}\n")
    public void textTrackCue() throws Exception {
        test("TextTrackCue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function TextTrackCueList() { [native code] }",
            FF = "function TextTrackCueList() {\n    [native code]\n}",
            IE11 = "[object TextTrackCueList]")
    public void textTrackCueList() throws Exception {
        test("TextTrackCueList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function TextTrackList() { [native code] }",
            FF = "function TextTrackList() {\n    [native code]\n}",
            IE11 = "[object TextTrackList]")
    public void textTrackList() throws Exception {
        test("TextTrackList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function TrackEvent() { [native code] }",
            FF = "function TrackEvent() {\n    [native code]\n}",
            IE11 = "[object TrackEvent]")
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function WebGLActiveInfo() { [native code] }",
            FF = "function WebGLActiveInfo() {\n    [native code]\n}",
            IE11 = "[object WebGLActiveInfo]")
    public void webGLActiveInfo() throws Exception {
        test("WebGLActiveInfo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function WebGLBuffer() { [native code] }",
            FF = "function WebGLBuffer() {\n    [native code]\n}",
            IE11 = "[object WebGLBuffer]")
    public void webGLBuffer() throws Exception {
        test("WebGLBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function WebGLContextEvent() { [native code] }",
            IE11 = "\nfunction WebGLContextEvent() {\n    [native code]\n}\n")
    public void webGLContextEvent() throws Exception {
        test("WebGLContextEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function WebGLFramebuffer() { [native code] }",
            FF = "function WebGLFramebuffer() {\n    [native code]\n}",
            IE11 = "[object WebGLFramebuffer]")
    public void webGLFramebuffer() throws Exception {
        test("WebGLFramebuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function WebGLProgram() { [native code] }",
            FF = "function WebGLProgram() {\n    [native code]\n}",
            IE11 = "[object WebGLProgram]")
    public void webGLProgram() throws Exception {
        test("WebGLProgram");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function WebGLRenderbuffer() { [native code] }",
            FF = "function WebGLRenderbuffer() {\n    [native code]\n}",
            IE11 = "[object WebGLRenderbuffer]")
    public void webGLRenderbuffer() throws Exception {
        test("WebGLRenderbuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function WebGLShader() { [native code] }",
            FF = "function WebGLShader() {\n    [native code]\n}",
            IE11 = "[object WebGLShader]")
    public void webGLShader() throws Exception {
        test("WebGLShader");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function WebGLShaderPrecisionFormat() { [native code] }",
            FF = "function WebGLShaderPrecisionFormat() {\n    [native code]\n}",
            IE11 = "[object WebGLShaderPrecisionFormat]")
    public void webGLShaderPrecisionFormat() throws Exception {
        test("WebGLShaderPrecisionFormat");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function WebGLTexture() { [native code] }",
            FF = "function WebGLTexture() {\n    [native code]\n}",
            IE11 = "[object WebGLTexture]")
    public void webGLTexture() throws Exception {
        test("WebGLTexture");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function WebGLUniformLocation() { [native code] }",
            FF = "function WebGLUniformLocation() {\n    [native code]\n}",
            IE11 = "[object WebGLUniformLocation]")
    public void webGLUniformLocation() throws Exception {
        test("WebGLUniformLocation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function AnimationEvent() { [native code] }")
    @NotYetImplemented(CHROME)
    public void webKitAnimationEvent() throws Exception {
        test("WebKitAnimationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function WebKitCSSMatrix() { [native code] }")
    public void webKitCSSMatrix() throws Exception {
        test("WebKitCSSMatrix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function TransitionEvent() { [native code] }")
    @NotYetImplemented(CHROME)
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
    @Alerts(DEFAULT = "exception",
            CHROME = "function XMLHttpRequestUpload() { [native code] }",
            FF = "function XMLHttpRequestUpload() {\n    [native code]\n}")
    public void xMLHttpRequestUpload() throws Exception {
        test("XMLHttpRequestUpload");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function XPathExpression() { [native code] }",
            FF31 = "[object XPathExpression]",
            FF38 = "function XPathExpression() {\n    [native code]\n}")
    @NotYetImplemented(FF31)
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
            CHROME = "function FontFace() { [native code] }")
    public void fontFace() throws Exception {
        test("FontFace");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function ImageBitmap() { [native code] }")
    public void imageBitmap() throws Exception {
        test("ImageBitmap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function InputMethodContext() { [native code] }")
    public void inputMethodContext() throws Exception {
        test("InputMethodContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MediaEncryptedEvent() { [native code] }")
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
            CHROME = "function ScreenOrientation() { [native code] }")
    public void screenOrientation() throws Exception {
        test("ScreenOrientation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MutationObserver() { [native code] }")
    @NotYetImplemented(CHROME)
    public void webKitMutationObserver() throws Exception {
        test("WebKitMutationObserver");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function AudioContext() { [native code] }")
    @NotYetImplemented(CHROME)
    public void webkitAudioContext() throws Exception {
        test("webkitAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBCursor() { [native code] }")
    @NotYetImplemented(CHROME)
    public void webkitIDBCursor() throws Exception {
        test("webkitIDBCursor");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBDatabase() { [native code] }")
    @NotYetImplemented(CHROME)
    public void webkitIDBDatabase() throws Exception {
        test("webkitIDBDatabase");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBFactory() { [native code] }")
    @NotYetImplemented(CHROME)
    public void webkitIDBFactory() throws Exception {
        test("webkitIDBFactory");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBIndex() { [native code] }")
    @NotYetImplemented(CHROME)
    public void webkitIDBIndex() throws Exception {
        test("webkitIDBIndex");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBKeyRange() { [native code] }")
    @NotYetImplemented(CHROME)
    public void webkitIDBKeyRange() throws Exception {
        test("webkitIDBKeyRange");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBObjectStore() { [native code] }")
    @NotYetImplemented(CHROME)
    public void webkitIDBObjectStore() throws Exception {
        test("webkitIDBObjectStore");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBRequest() { [native code] }")
    @NotYetImplemented(CHROME)
    public void webkitIDBRequest() throws Exception {
        test("webkitIDBRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function IDBTransaction() { [native code] }")
    @NotYetImplemented(CHROME)
    public void webkitIDBTransaction() throws Exception {
        test("webkitIDBTransaction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MediaStream() { [native code] }")
    @NotYetImplemented(CHROME)
    public void webkitMediaStream() throws Exception {
        test("webkitMediaStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function OfflineAudioContext() { [native code] }")
    @NotYetImplemented(CHROME)
    public void webkitOfflineAudioContext() throws Exception {
        test("webkitOfflineAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SpeechGrammar() { [native code] }")
    @NotYetImplemented(CHROME)
    public void webkitSpeechGrammar() throws Exception {
        test("webkitSpeechGrammar");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SpeechGrammarList() { [native code] }")
    @NotYetImplemented(CHROME)
    public void webkitSpeechGrammarList() throws Exception {
        test("webkitSpeechGrammarList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SpeechRecognitionError() { [native code] }")
    @NotYetImplemented(CHROME)
    public void webkitSpeechRecognitionError() throws Exception {
        test("webkitSpeechRecognitionError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SpeechRecognitionEvent() { [native code] }")
    @NotYetImplemented(CHROME)
    public void webkitSpeechRecognitionEvent() throws Exception {
        test("webkitSpeechRecognitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function URL() { [native code] }")
    @NotYetImplemented(CHROME)
    public void webkitURL() throws Exception {
        test("webkitURL");
    }

}
