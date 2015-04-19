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
package com.gargoylesoftware.htmlunit.general.huge;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE11;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import com.gargoylesoftware.htmlunit.BrowserParameterizedRunner;
import com.gargoylesoftware.htmlunit.BrowserParameterizedRunner.Default;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.TestCaseTest;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Tests two Host classes, if one prototype is parent of another.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserParameterizedRunner.class)
public class HostParentOfTest extends WebDriverTestCase {

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    @Parameters
    public static Collection<Object[]> data() throws Exception {
        final List<Object[]> list = new ArrayList<>();
        final List<String> strings = TestCaseTest.getAllClassNames();
        for (final String parent : strings) {
            for (final String child : strings) {
                list.add(new Object[] {parent, child});
            }
        }
        return list;
    }

    /**
     * The parent element name.
     */
    @Parameter
    public String parent_;

    /**
     * The child element name.
     */
    @Parameter(1)
    public String child_;

    /**
     * The default test.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("false")
    @Default
    public void isParentOf() throws Exception {
        test(parent_, child_);
    }

    private void test(final String parent, final String child) throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head><title>" + (getBrowserVersion().isIE() ? "Blank Page" : "New Tab") + "</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(isParentOf(" + parent + ", " + child + "));\n"
            + "    } catch(e) { alert('false'); }\n"
            + "  }\n"

            + "  /*\n"
            + "   * Returns true if o1 prototype is parent/grandparent of o2 prototype\n"
            + "   */\n"
            + "  function isParentOf(o1, o2) {\n"
            + "    o1.prototype.myCustomFunction = function() {};\n"
            + "    return o2.prototype.myCustomFunction != undefined;\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isWebClientCached() {
        return true;
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            CHROME = "false")
    public void _Image_HTMLImageElement() throws Exception {
        //although Image != HTMLImageElement, they seem to be synonyms!!!
        test("Image", "HTMLImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLImageElement_Image() throws Exception {
        //although Image != HTMLImageElement, they seem to be synonyms!!!
        test("HTMLImageElement", "Image");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            CHROME = "false")
    public void _Option_HTMLOptionElement() throws Exception {
        //although Option != HTMLOptionElement, they seem to be synonyms!!!
        test("Option", "HTMLOptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLOptionElement_Option() throws Exception {
        //although Option != HTMLOptionElement, they seem to be synonyms!!!
        test("HTMLOptionElement", "Option");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _ArrayBuffer_ArrayBuffer() throws Exception {
        test("ArrayBuffer", "ArrayBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Attr_Attr() throws Exception {
        test("Attr", "Attr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _ActiveXObject_ActiveXObject() throws Exception {
        test("ActiveXObject", "ActiveXObject");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false",
            IE8 = "false")
    public void _ApplicationCache_ApplicationCache() throws Exception {
        test("ApplicationCache", "ApplicationCache");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _BeforeUnloadEvent_BeforeUnloadEvent() throws Exception {
        test("BeforeUnloadEvent", "BeforeUnloadEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CDATASection_CDATASection() throws Exception {
        test("CDATASection", "CDATASection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSS2Properties_CSS2Properties() throws Exception {
        test("CSS2Properties", "CSS2Properties");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSSCharsetRule_CSSCharsetRule() throws Exception {
        test("CSSCharsetRule", "CSSCharsetRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CSSFontFaceRule_CSSFontFaceRule() throws Exception {
        test("CSSFontFaceRule", "CSSFontFaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CSSImportRule_CSSImportRule() throws Exception {
        test("CSSImportRule", "CSSImportRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CSSMediaRule_CSSMediaRule() throws Exception {
        test("CSSMediaRule", "CSSMediaRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSSPrimitiveValue_CSSPrimitiveValue() throws Exception {
        test("CSSPrimitiveValue", "CSSPrimitiveValue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CSSRule_CSSRule() throws Exception {
        test("CSSRule", "CSSRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSRuleList_CSSRuleList() throws Exception {
        test("CSSRuleList", "CSSRuleList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSStyleDeclaration_CSSStyleDeclaration() throws Exception {
        test("CSSStyleDeclaration", "CSSStyleDeclaration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSStyleRule_CSSStyleRule() throws Exception {
        test("CSSStyleRule", "CSSStyleRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSStyleSheet_CSSStyleSheet() throws Exception {
        test("CSSStyleSheet", "CSSStyleSheet");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSSValue_CSSValue() throws Exception {
        test("CSSValue", "CSSValue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CanvasRenderingContext2D_CanvasRenderingContext2D() throws Exception {
        test("CanvasRenderingContext2D", "CanvasRenderingContext2D");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false",
            IE8 = "false")
    public void _ClientRect_ClientRect() throws Exception {
        test("ClientRect", "ClientRect");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Comment_Comment() throws Exception {
        test("Comment", "Comment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Console_Console() throws Exception {
        test("Console", "Console");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Coordinates_Coordinates() throws Exception {
        test("Coordinates", "Coordinates");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _DataView_DataView() throws Exception {
        test("DataView", "DataView");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _DOMException_DOMException() throws Exception {
        test("DOMException", "DOMException");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _DOMImplementation_DOMImplementation() throws Exception {
        test("DOMImplementation", "DOMImplementation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _DOMParser_DOMParser() throws Exception {
        test("DOMParser", "DOMParser");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void _DOMStringMap_DOMStringMap() throws Exception {
        test("DOMStringMap", "DOMStringMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _DOMTokenList_DOMTokenList() throws Exception {
        test("DOMTokenList", "DOMTokenList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Document_Document() throws Exception {
        test("Document", "Document");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _DocumentFragment_DocumentFragment() throws Exception {
        test("DocumentFragment", "DocumentFragment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _DocumentType_DocumentType() throws Exception {
        test("DocumentType", "DocumentType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_Element() throws Exception {
        test("Element", "Element");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Enumerator_Enumerator() throws Exception {
        test("Enumerator", "Enumerator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Event_Event() throws Exception {
        test("Event", "Event");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _External_External() throws Exception {
        test("External", "External");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Float32Array_Float32Array() throws Exception {
        test("Float32Array", "Float32Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Float64Array_Float64Array() throws Exception {
        test("Float64Array", "Float64Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Geolocation_Geolocation() throws Exception {
        test("Geolocation", "Geolocation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HashChangeEvent_HashChangeEvent() throws Exception {
        test("HashChangeEvent", "HashChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _History_History() throws Exception {
        test("History", "History");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false",
            IE8 = "false")
    public void _HTMLAllCollection_HTMLAllCollection() throws Exception {
        test("HTMLAllCollection", "HTMLAllCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLAnchorElement_HTMLAnchorElement() throws Exception {
        test("HTMLAnchorElement", "HTMLAnchorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLAppletElement_HTMLAppletElement() throws Exception {
        test("HTMLAppletElement", "HTMLAppletElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLAreaElement_HTMLAreaElement() throws Exception {
        test("HTMLAreaElement", "HTMLAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLAudioElement_HTMLAudioElement() throws Exception {
        test("HTMLAudioElement", "HTMLAudioElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLBGSoundElement_HTMLBGSoundElement() throws Exception {
        test("HTMLBGSoundElement", "HTMLBGSoundElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLBRElement_HTMLBRElement() throws Exception {
        test("HTMLBRElement", "HTMLBRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLBaseElement_HTMLBaseElement() throws Exception {
        test("HTMLBaseElement", "HTMLBaseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLBaseFontElement_HTMLBaseFontElement() throws Exception {
        test("HTMLBaseFontElement", "HTMLBaseFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLBlockElement_HTMLBlockElement() throws Exception {
        test("HTMLBlockElement", "HTMLBlockElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLQuoteElement_HTMLQuoteElement() throws Exception {
        test("HTMLQuoteElement", "HTMLQuoteElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLBodyElement_HTMLBodyElement() throws Exception {
        test("HTMLBodyElement", "HTMLBodyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLButtonElement_HTMLButtonElement() throws Exception {
        test("HTMLButtonElement", "HTMLButtonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLCanvasElement_HTMLCanvasElement() throws Exception {
        test("HTMLCanvasElement", "HTMLCanvasElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLCollection_HTMLCollection() throws Exception {
        test("HTMLCollection", "HTMLCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE11 = "false")
    @NotYetImplemented({ CHROME, FF, IE8 })
    public void _HTMLCollection_HTMLOptionsCollection() throws Exception {
        test("HTMLCollection", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE8 = "true")
    public void _HTMLCommentElement_HTMLCommentElement() throws Exception {
        test("HTMLCommentElement", "HTMLCommentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLDataListElement_HTMLDataListElement() throws Exception {
        test("HTMLDataListElement", "HTMLDataListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLDDElement_HTMLDDElement() throws Exception {
        test("HTMLDDElement", "HTMLDDElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _HTMLDetailsElement_HTMLDetailsElement() throws Exception {
        test("HTMLDetailsElement", "HTMLDetailsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _HTMLDialogElement_HTMLDialogElement() throws Exception {
        test("HTMLDialogElement", "HTMLDialogElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLDTElement_HTMLDTElement() throws Exception {
        test("HTMLDTElement", "HTMLDTElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLDListElement_HTMLDListElement() throws Exception {
        test("HTMLDListElement", "HTMLDListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLDirectoryElement_HTMLDirectoryElement() throws Exception {
        test("HTMLDirectoryElement", "HTMLDirectoryElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLDivElement_HTMLDivElement() throws Exception {
        test("HTMLDivElement", "HTMLDivElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLDocument_HTMLDocument() throws Exception {
        test("HTMLDocument", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLElement() throws Exception {
        test("HTMLElement", "HTMLElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLEmbedElement_HTMLEmbedElement() throws Exception {
        test("HTMLEmbedElement", "HTMLEmbedElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLFieldSetElement_HTMLFieldSetElement() throws Exception {
        test("HTMLFieldSetElement", "HTMLFieldSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLFontElement_HTMLFontElement() throws Exception {
        test("HTMLFontElement", "HTMLFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLFormElement_HTMLFormElement() throws Exception {
        test("HTMLFormElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLFrameElement_HTMLFrameElement() throws Exception {
        test("HTMLFrameElement", "HTMLFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLFrameSetElement_HTMLFrameSetElement() throws Exception {
        test("HTMLFrameSetElement", "HTMLFrameSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLHRElement_HTMLHRElement() throws Exception {
        test("HTMLHRElement", "HTMLHRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLHeadElement_HTMLHeadElement() throws Exception {
        test("HTMLHeadElement", "HTMLHeadElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLHeadingElement_HTMLHeadingElement() throws Exception {
        test("HTMLHeadingElement", "HTMLHeadingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLHtmlElement_HTMLHtmlElement() throws Exception {
        test("HTMLHtmlElement", "HTMLHtmlElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLIFrameElement_HTMLIFrameElement() throws Exception {
        test("HTMLIFrameElement", "HTMLIFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLImageElement_HTMLImageElement() throws Exception {
        test("HTMLImageElement", "HTMLImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLInputElement_HTMLInputElement() throws Exception {
        test("HTMLInputElement", "HTMLInputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLIsIndexElement_HTMLIsIndexElement() throws Exception {
        test("HTMLIsIndexElement", "HTMLIsIndexElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _HTMLKeygenElement_HTMLKeygenElement() throws Exception {
        test("HTMLKeygenElement", "HTMLKeygenElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLLIElement_HTMLLIElement() throws Exception {
        test("HTMLLIElement", "HTMLLIElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLLabelElement_HTMLLabelElement() throws Exception {
        test("HTMLLabelElement", "HTMLLabelElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLLegendElement_HTMLLegendElement() throws Exception {
        test("HTMLLegendElement", "HTMLLegendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLLinkElement_HTMLLinkElement() throws Exception {
        test("HTMLLinkElement", "HTMLLinkElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLMapElement_HTMLMapElement() throws Exception {
        test("HTMLMapElement", "HTMLMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false")
    public void _HTMLMarqueeElement_HTMLMarqueeElement() throws Exception {
        test("HTMLMarqueeElement", "HTMLMarqueeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLMediaElement_HTMLMediaElement() throws Exception {
        test("HTMLMediaElement", "HTMLMediaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLMenuElement_HTMLMenuElement() throws Exception {
        test("HTMLMenuElement", "HTMLMenuElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _HTMLMenuItemElement_HTMLMenuItemElement() throws Exception {
        test("HTMLMenuItemElement", "HTMLMenuItemElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLMetaElement_HTMLMetaElement() throws Exception {
        test("HTMLMetaElement", "HTMLMetaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLMeterElement_HTMLMeterElement() throws Exception {
        test("HTMLMeterElement", "HTMLMeterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLModElement_HTMLModElement() throws Exception {
        test("HTMLModElement", "HTMLModElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE8 = "true")
    public void _HTMLNoShowElement_HTMLNoShowElement() throws Exception {
        test("HTMLNoShowElement", "HTMLNoShowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLNextIdElement_HTMLNextIdElement() throws Exception {
        test("HTMLNextIdElement", "HTMLNextIdElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLOListElement_HTMLOListElement() throws Exception {
        test("HTMLOListElement", "HTMLOListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLObjectElement_HTMLObjectElement() throws Exception {
        test("HTMLObjectElement", "HTMLObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLOptGroupElement_HTMLOptGroupElement() throws Exception {
        test("HTMLOptGroupElement", "HTMLOptGroupElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLOptionElement_HTMLOptionElement() throws Exception {
        test("HTMLOptionElement", "HTMLOptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLOptionsCollection_HTMLOptionsCollection() throws Exception {
        test("HTMLOptionsCollection", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLOutputElement_HTMLOutputElement() throws Exception {
        test("HTMLOutputElement", "HTMLOutputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLParagraphElement_HTMLParagraphElement() throws Exception {
        test("HTMLParagraphElement", "HTMLParagraphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLParamElement_HTMLParamElement() throws Exception {
        test("HTMLParamElement", "HTMLParamElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLPhraseElement_HTMLPhraseElement() throws Exception {
        test("HTMLPhraseElement", "HTMLPhraseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLPreElement_HTMLPreElement() throws Exception {
        test("HTMLPreElement", "HTMLPreElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLProgressElement_HTMLProgressElement() throws Exception {
        test("HTMLProgressElement", "HTMLProgressElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLScriptElement_HTMLScriptElement() throws Exception {
        test("HTMLScriptElement", "HTMLScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLSelectElement_HTMLSelectElement() throws Exception {
        test("HTMLSelectElement", "HTMLSelectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLSourceElement_HTMLSourceElement() throws Exception {
        test("HTMLSourceElement", "HTMLSourceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLSpanElement_HTMLSpanElement() throws Exception {
        test("HTMLSpanElement", "HTMLSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLStyleElement_HTMLStyleElement() throws Exception {
        test("HTMLStyleElement", "HTMLStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTableCaptionElement_HTMLTableCaptionElement() throws Exception {
        test("HTMLTableCaptionElement", "HTMLTableCaptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTableCellElement_HTMLTableCellElement() throws Exception {
        test("HTMLTableCellElement", "HTMLTableCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTableColElement_HTMLTableColElement() throws Exception {
        test("HTMLTableColElement", "HTMLTableColElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLTableDataCellElement_HTMLTableDataCellElement() throws Exception {
        test("HTMLTableDataCellElement", "HTMLTableDataCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTableElement_HTMLTableElement() throws Exception {
        test("HTMLTableElement", "HTMLTableElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLTableHeaderCellElement_HTMLTableHeaderCellElement() throws Exception {
        test("HTMLTableHeaderCellElement", "HTMLTableHeaderCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTableRowElement_HTMLTableRowElement() throws Exception {
        test("HTMLTableRowElement", "HTMLTableRowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTableSectionElement_HTMLTableSectionElement() throws Exception {
        test("HTMLTableSectionElement", "HTMLTableSectionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE8 = "true")
    public void _HTMLTextElement_HTMLTextElement() throws Exception {
        test("HTMLTextElement", "HTMLTextElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTextAreaElement_HTMLTextAreaElement() throws Exception {
        test("HTMLTextAreaElement", "HTMLTextAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _HTMLTimeElement_HTMLTimeElement() throws Exception {
        test("HTMLTimeElement", "HTMLTimeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTitleElement_HTMLTitleElement() throws Exception {
        test("HTMLTitleElement", "HTMLTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLTrackElement_HTMLTrackElement() throws Exception {
        test("HTMLTrackElement", "HTMLTrackElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLUListElement_HTMLUListElement() throws Exception {
        test("HTMLUListElement", "HTMLUListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _HTMLUnknownElement_HTMLUnknownElement() throws Exception {
        test("HTMLUnknownElement", "HTMLUnknownElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLVideoElement_HTMLVideoElement() throws Exception {
        test("HTMLVideoElement", "HTMLVideoElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Image_Image() throws Exception {
        test("Image", "Image");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Int16Array_Int16Array() throws Exception {
        test("Int16Array", "Int16Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Int32Array_Int32Array() throws Exception {
        test("Int32Array", "Int32Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Int8Array_Int8Array() throws Exception {
        test("Int8Array", "Int8Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _KeyboardEvent_KeyboardEvent() throws Exception {
        test("KeyboardEvent", "KeyboardEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false")
    @NotYetImplemented(FF)
    public void _Location_Location() throws Exception {
        test("Location", "Location");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _MediaList_MediaList() throws Exception {
        test("MediaList", "MediaList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _MessageEvent_MessageEvent() throws Exception {
        test("MessageEvent", "MessageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _MimeType_MimeType() throws Exception {
        test("MimeType", "MimeType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _MimeTypeArray_MimeTypeArray() throws Exception {
        test("MimeTypeArray", "MimeTypeArray");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _MouseEvent_MouseEvent() throws Exception {
        test("MouseEvent", "MouseEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _MutationEvent_MutationEvent() throws Exception {
        test("MutationEvent", "MutationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false")
    public void _NamedNodeMap_NamedNodeMap() throws Exception {
        test("NamedNodeMap", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Navigator_Navigator() throws Exception {
        test("Navigator", "Navigator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_Node() throws Exception {
        test("Node", "Node");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    @NotYetImplemented({ FF, IE11 })
    public void _NodeFilter_NodeFilter() throws Exception {
        test("NodeFilter", "NodeFilter");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _NodeList_NodeList() throws Exception {
        test("NodeList", "NodeList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Notification_Notification() throws Exception {
        test("Notification", "Notification");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Option_Option() throws Exception {
        test("Option", "Option");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Plugin_Plugin() throws Exception {
        test("Plugin", "Plugin");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _PluginArray_PluginArray() throws Exception {
        test("PluginArray", "PluginArray");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _PointerEvent_PointerEvent() throws Exception {
        test("PointerEvent", "PointerEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Position_Position() throws Exception {
        test("Position", "Position");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _ProcessingInstruction_ProcessingInstruction() throws Exception {
        test("ProcessingInstruction", "ProcessingInstruction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Range_Range() throws Exception {
        test("Range", "Range");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGAElement_SVGAElement() throws Exception {
        test("SVGAElement", "SVGAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _SVGAltGlyphElement_SVGAltGlyphElement() throws Exception {
        test("SVGAltGlyphElement", "SVGAltGlyphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGAngle_SVGAngle() throws Exception {
        test("SVGAngle", "SVGAngle");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGAnimateElement_SVGAnimateElement() throws Exception {
        test("SVGAnimateElement", "SVGAnimateElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGAnimateMotionElement_SVGAnimateMotionElement() throws Exception {
        test("SVGAnimateMotionElement", "SVGAnimateMotionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGAnimateTransformElement_SVGAnimateTransformElement() throws Exception {
        test("SVGAnimateTransformElement", "SVGAnimateTransformElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGCircleElement_SVGCircleElement() throws Exception {
        test("SVGCircleElement", "SVGCircleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGClipPathElement_SVGClipPathElement() throws Exception {
        test("SVGClipPathElement", "SVGClipPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _SVGCursorElement_SVGCursorElement() throws Exception {
        test("SVGCursorElement", "SVGCursorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGDefsElement_SVGDefsElement() throws Exception {
        test("SVGDefsElement", "SVGDefsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGDescElement_SVGDescElement() throws Exception {
        test("SVGDescElement", "SVGDescElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGElement() throws Exception {
        test("SVGElement", "SVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGEllipseElement_SVGEllipseElement() throws Exception {
        test("SVGEllipseElement", "SVGEllipseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEBlendElement_SVGFEBlendElement() throws Exception {
        test("SVGFEBlendElement", "SVGFEBlendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEColorMatrixElement_SVGFEColorMatrixElement() throws Exception {
        test("SVGFEColorMatrixElement", "SVGFEColorMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEComponentTransferElement_SVGFEComponentTransferElement() throws Exception {
        test("SVGFEComponentTransferElement", "SVGFEComponentTransferElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFECompositeElement_SVGFECompositeElement() throws Exception {
        test("SVGFECompositeElement", "SVGFECompositeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEConvolveMatrixElement_SVGFEConvolveMatrixElement() throws Exception {
        test("SVGFEConvolveMatrixElement", "SVGFEConvolveMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEDiffuseLightingElement_SVGFEDiffuseLightingElement() throws Exception {
        test("SVGFEDiffuseLightingElement", "SVGFEDiffuseLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEDisplacementMapElement_SVGFEDisplacementMapElement() throws Exception {
        test("SVGFEDisplacementMapElement", "SVGFEDisplacementMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEDistantLightElement_SVGFEDistantLightElement() throws Exception {
        test("SVGFEDistantLightElement", "SVGFEDistantLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEFloodElement_SVGFEFloodElement() throws Exception {
        test("SVGFEFloodElement", "SVGFEFloodElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEFuncAElement_SVGFEFuncAElement() throws Exception {
        test("SVGFEFuncAElement", "SVGFEFuncAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEFuncBElement_SVGFEFuncBElement() throws Exception {
        test("SVGFEFuncBElement", "SVGFEFuncBElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEFuncGElement_SVGFEFuncGElement() throws Exception {
        test("SVGFEFuncGElement", "SVGFEFuncGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEFuncRElement_SVGFEFuncRElement() throws Exception {
        test("SVGFEFuncRElement", "SVGFEFuncRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEGaussianBlurElement_SVGFEGaussianBlurElement() throws Exception {
        test("SVGFEGaussianBlurElement", "SVGFEGaussianBlurElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEImageElement_SVGFEImageElement() throws Exception {
        test("SVGFEImageElement", "SVGFEImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEMergeElement_SVGFEMergeElement() throws Exception {
        test("SVGFEMergeElement", "SVGFEMergeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEMergeNodeElement_SVGFEMergeNodeElement() throws Exception {
        test("SVGFEMergeNodeElement", "SVGFEMergeNodeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEMorphologyElement_SVGFEMorphologyElement() throws Exception {
        test("SVGFEMorphologyElement", "SVGFEMorphologyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEOffsetElement_SVGFEOffsetElement() throws Exception {
        test("SVGFEOffsetElement", "SVGFEOffsetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEPointLightElement_SVGFEPointLightElement() throws Exception {
        test("SVGFEPointLightElement", "SVGFEPointLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFESpecularLightingElement_SVGFESpecularLightingElement() throws Exception {
        test("SVGFESpecularLightingElement", "SVGFESpecularLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFESpotLightElement_SVGFESpotLightElement() throws Exception {
        test("SVGFESpotLightElement", "SVGFESpotLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFETileElement_SVGFETileElement() throws Exception {
        test("SVGFETileElement", "SVGFETileElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFETurbulenceElement_SVGFETurbulenceElement() throws Exception {
        test("SVGFETurbulenceElement", "SVGFETurbulenceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFilterElement_SVGFilterElement() throws Exception {
        test("SVGFilterElement", "SVGFilterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGForeignObjectElement_SVGForeignObjectElement() throws Exception {
        test("SVGForeignObjectElement", "SVGForeignObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGGElement_SVGGElement() throws Exception {
        test("SVGGElement", "SVGGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGImageElement_SVGImageElement() throws Exception {
        test("SVGImageElement", "SVGImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGLineElement_SVGLineElement() throws Exception {
        test("SVGLineElement", "SVGLineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGLinearGradientElement_SVGLinearGradientElement() throws Exception {
        test("SVGLinearGradientElement", "SVGLinearGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGMarkerElement_SVGMarkerElement() throws Exception {
        test("SVGMarkerElement", "SVGMarkerElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGMaskElement_SVGMaskElement() throws Exception {
        test("SVGMaskElement", "SVGMaskElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGMatrix_SVGMatrix() throws Exception {
        test("SVGMatrix", "SVGMatrix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGMetadataElement_SVGMetadataElement() throws Exception {
        test("SVGMetadataElement", "SVGMetadataElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGMPathElement_SVGMPathElement() throws Exception {
        test("SVGMPathElement", "SVGMPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGPathElement_SVGPathElement() throws Exception {
        test("SVGPathElement", "SVGPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGPatternElement_SVGPatternElement() throws Exception {
        test("SVGPatternElement", "SVGPatternElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGPolygonElement_SVGPolygonElement() throws Exception {
        test("SVGPolygonElement", "SVGPolygonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGPolylineElement_SVGPolylineElement() throws Exception {
        test("SVGPolylineElement", "SVGPolylineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGRadialGradientElement_SVGRadialGradientElement() throws Exception {
        test("SVGRadialGradientElement", "SVGRadialGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGRect_SVGRect() throws Exception {
        test("SVGRect", "SVGRect");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGRectElement_SVGRectElement() throws Exception {
        test("SVGRectElement", "SVGRectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGSVGElement_SVGSVGElement() throws Exception {
        test("SVGSVGElement", "SVGSVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGScriptElement_SVGScriptElement() throws Exception {
        test("SVGScriptElement", "SVGScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGSetElement_SVGSetElement() throws Exception {
        test("SVGSetElement", "SVGSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGStopElement_SVGStopElement() throws Exception {
        test("SVGStopElement", "SVGStopElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGStyleElement_SVGStyleElement() throws Exception {
        test("SVGStyleElement", "SVGStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGSwitchElement_SVGSwitchElement() throws Exception {
        test("SVGSwitchElement", "SVGSwitchElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGSymbolElement_SVGSymbolElement() throws Exception {
        test("SVGSymbolElement", "SVGSymbolElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGTSpanElement_SVGTSpanElement() throws Exception {
        test("SVGTSpanElement", "SVGTSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGTextElement_SVGTextElement() throws Exception {
        test("SVGTextElement", "SVGTextElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGTextPathElement_SVGTextPathElement() throws Exception {
        test("SVGTextPathElement", "SVGTextPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGTitleElement_SVGTitleElement() throws Exception {
        test("SVGTitleElement", "SVGTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGUseElement_SVGUseElement() throws Exception {
        test("SVGUseElement", "SVGUseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGViewElement_SVGViewElement() throws Exception {
        test("SVGViewElement", "SVGViewElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Screen_Screen() throws Exception {
        test("Screen", "Screen");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Selection_Selection() throws Exception {
        test("Selection", "Selection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE8 = "true")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void _StaticNodeList_StaticNodeList() throws Exception {
        test("StaticNodeList", "StaticNodeList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Storage_Storage() throws Exception {
        test("Storage", "Storage");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _StyleSheetList_StyleSheetList() throws Exception {
        test("StyleSheetList", "StyleSheetList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Text_Text() throws Exception {
        test("Text", "Text");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _TextRange_TextRange() throws Exception {
        test("TextRange", "TextRange");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _TreeWalker_TreeWalker() throws Exception {
        test("TreeWalker", "TreeWalker");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _UIEvent_UIEvent() throws Exception {
        test("UIEvent", "UIEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Uint16Array_Uint16Array() throws Exception {
        test("Uint16Array", "Uint16Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Uint32Array_Uint32Array() throws Exception {
        test("Uint32Array", "Uint32Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Uint8Array_Uint8Array() throws Exception {
        test("Uint8Array", "Uint8Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Uint8ClampedArray_Uint8ClampedArray() throws Exception {
        test("Uint8ClampedArray", "Uint8ClampedArray");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _WebSocket_WebSocket() throws Exception {
        test("WebSocket", "WebSocket");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Window_Window() throws Exception {
        test("Window", "Window");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _XMLDocument_XMLDocument() throws Exception {
        test("XMLDocument", "XMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _XMLHttpRequest_XMLHttpRequest() throws Exception {
        test("XMLHttpRequest", "XMLHttpRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _XMLSerializer_XMLSerializer() throws Exception {
        test("XMLSerializer", "XMLSerializer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _XPathEvaluator_XPathEvaluator() throws Exception {
        test("XPathEvaluator", "XPathEvaluator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _XPathNSResolver_XPathNSResolver() throws Exception {
        test("XPathNSResolver", "XPathNSResolver");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _XPathResult_XPathResult() throws Exception {
        test("XPathResult", "XPathResult");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _XSLTProcessor_XSLTProcessor() throws Exception {
        test("XSLTProcessor", "XSLTProcessor");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSSRule_CSSCharsetRule() throws Exception {
        test("CSSRule", "CSSCharsetRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CSSRule_CSSFontFaceRule() throws Exception {
        test("CSSRule", "CSSFontFaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CSSRule_CSSImportRule() throws Exception {
        test("CSSRule", "CSSImportRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CSSRule_CSSMediaRule() throws Exception {
        test("CSSRule", "CSSMediaRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CSSRule_CSSStyleRule() throws Exception {
        test("CSSRule", "CSSStyleRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
        FF = "true")
    public void _CSSStyleDeclaration_CSS2Properties() throws Exception {
        test("CSSStyleDeclaration", "CSS2Properties");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSSValue_CSSPrimitiveValue() throws Exception {
        test("CSSValue", "CSSPrimitiveValue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Document_HTMLDocument() throws Exception {
        test("Document", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Document_XMLDocument() throws Exception {
        test("Document", "XMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLAnchorElement() throws Exception {
        test("Element", "HTMLAnchorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLAppletElement() throws Exception {
        test("Element", "HTMLAppletElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLAreaElement() throws Exception {
        test("Element", "HTMLAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLAudioElement() throws Exception {
        test("Element", "HTMLAudioElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Element_HTMLBGSoundElement() throws Exception {
        test("Element", "HTMLBGSoundElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLBRElement() throws Exception {
        test("Element", "HTMLBRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLBaseElement() throws Exception {
        test("Element", "HTMLBaseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Element_HTMLBaseFontElement() throws Exception {
        test("Element", "HTMLBaseFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Element_HTMLBlockElement() throws Exception {
        test("Element", "HTMLBlockElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLQuoteElement() throws Exception {
        test("Element", "HTMLQuoteElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLBodyElement() throws Exception {
        test("Element", "HTMLBodyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLButtonElement() throws Exception {
        test("Element", "HTMLButtonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLCanvasElement() throws Exception {
        test("Element", "HTMLCanvasElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLDataListElement() throws Exception {
        test("Element", "HTMLDataListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Element_HTMLDDElement() throws Exception {
        test("Element", "HTMLDDElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Element_HTMLDetailsElement() throws Exception {
        test("Element", "HTMLDetailsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Element_HTMLDialogElement() throws Exception {
        test("Element", "HTMLDialogElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Element_HTMLDTElement() throws Exception {
        test("Element", "HTMLDTElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLDListElement() throws Exception {
        test("Element", "HTMLDListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLDirectoryElement() throws Exception {
        test("Element", "HTMLDirectoryElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLDivElement() throws Exception {
        test("Element", "HTMLDivElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLElement() throws Exception {
        test("Element", "HTMLElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLEmbedElement() throws Exception {
        test("Element", "HTMLEmbedElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLFieldSetElement() throws Exception {
        test("Element", "HTMLFieldSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLFontElement() throws Exception {
        test("Element", "HTMLFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLFormElement() throws Exception {
        test("Element", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLFrameElement() throws Exception {
        test("Element", "HTMLFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLFrameSetElement() throws Exception {
        test("Element", "HTMLFrameSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLHRElement() throws Exception {
        test("Element", "HTMLHRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLHeadElement() throws Exception {
        test("Element", "HTMLHeadElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLHeadingElement() throws Exception {
        test("Element", "HTMLHeadingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLHtmlElement() throws Exception {
        test("Element", "HTMLHtmlElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLIFrameElement() throws Exception {
        test("Element", "HTMLIFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLImageElement() throws Exception {
        test("Element", "HTMLImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLInputElement() throws Exception {
        test("Element", "HTMLInputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Element_HTMLIsIndexElement() throws Exception {
        test("Element", "HTMLIsIndexElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Element_HTMLKeygenElement() throws Exception {
        test("Element", "HTMLKeygenElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLLIElement() throws Exception {
        test("Element", "HTMLLIElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLLabelElement() throws Exception {
        test("Element", "HTMLLabelElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLLegendElement() throws Exception {
        test("Element", "HTMLLegendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLLinkElement() throws Exception {
        test("Element", "HTMLLinkElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLMapElement() throws Exception {
        test("Element", "HTMLMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false")
    public void _Element_HTMLMarqueeElement() throws Exception {
        test("Element", "HTMLMarqueeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLMediaElement() throws Exception {
        test("Element", "HTMLMediaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLMenuElement() throws Exception {
        test("Element", "HTMLMenuElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _EventTarget_HTMLMenuItemElement() throws Exception {
        test("EventTarget", "HTMLMenuItemElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Element_HTMLMenuItemElement() throws Exception {
        test("Element", "HTMLMenuItemElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLMetaElement() throws Exception {
        test("Element", "HTMLMetaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Element_HTMLMeterElement() throws Exception {
        test("Element", "HTMLMeterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLModElement() throws Exception {
        test("Element", "HTMLModElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE8 = "true")
    public void _Element_HTMLNoShowElement() throws Exception {
        test("Element", "HTMLNoShowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Element_HTMLNextIdElement() throws Exception {
        test("Element", "HTMLNextIdElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLOListElement() throws Exception {
        test("Element", "HTMLOListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLObjectElement() throws Exception {
        test("Element", "HTMLObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLOptGroupElement() throws Exception {
        test("Element", "HTMLOptGroupElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLOptionElement() throws Exception {
        test("Element", "HTMLOptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Element_HTMLOutputElement() throws Exception {
        test("Element", "HTMLOutputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLParagraphElement() throws Exception {
        test("Element", "HTMLParagraphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLParamElement() throws Exception {
        test("Element", "HTMLParamElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Element_HTMLPhraseElement() throws Exception {
        test("Element", "HTMLPhraseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLPreElement() throws Exception {
        test("Element", "HTMLPreElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLProgressElement() throws Exception {
        test("Element", "HTMLProgressElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLScriptElement() throws Exception {
        test("Element", "HTMLScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLSelectElement() throws Exception {
        test("Element", "HTMLSelectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLSourceElement() throws Exception {
        test("Element", "HTMLSourceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLSpanElement() throws Exception {
        test("Element", "HTMLSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLStyleElement() throws Exception {
        test("Element", "HTMLStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLTableCaptionElement() throws Exception {
        test("Element", "HTMLTableCaptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLTableCellElement() throws Exception {
        test("Element", "HTMLTableCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLTableColElement() throws Exception {
        test("Element", "HTMLTableColElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Element_HTMLTableDataCellElement() throws Exception {
        test("Element", "HTMLTableDataCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLTableElement() throws Exception {
        test("Element", "HTMLTableElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Element_HTMLTableHeaderCellElement() throws Exception {
        test("Element", "HTMLTableHeaderCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLTableRowElement() throws Exception {
        test("Element", "HTMLTableRowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLTableSectionElement() throws Exception {
        test("Element", "HTMLTableSectionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE8 = "true")
    public void _Element_HTMLTextElement() throws Exception {
        test("Element", "HTMLTextElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLTextAreaElement() throws Exception {
        test("Element", "HTMLTextAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _EventTarget_HTMLTimeElement() throws Exception {
        test("EventTarget", "HTMLTimeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Element_HTMLTimeElement() throws Exception {
        test("Element", "HTMLTimeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLTitleElement() throws Exception {
        test("Element", "HTMLTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLTrackElement() throws Exception {
        test("Element", "HTMLTrackElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLUListElement() throws Exception {
        test("Element", "HTMLUListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _Element_HTMLUnknownElement() throws Exception {
        test("Element", "HTMLUnknownElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLVideoElement() throws Exception {
        test("Element", "HTMLVideoElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_Image() throws Exception {
        test("Element", "Image");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_Option() throws Exception {
        test("Element", "Option");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGAElement() throws Exception {
        test("Element", "SVGAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _EventTarget_SVGAltGlyphElement() throws Exception {
        test("EventTarget", "SVGAltGlyphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Element_SVGAltGlyphElement() throws Exception {
        test("Element", "SVGAltGlyphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Element_SVGAnimateElement() throws Exception {
        test("Element", "SVGAnimateElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Element_SVGAnimateMotionElement() throws Exception {
        test("Element", "SVGAnimateMotionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Element_SVGAnimateTransformElement() throws Exception {
        test("Element", "SVGAnimateTransformElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGCircleElement() throws Exception {
        test("Element", "SVGCircleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGClipPathElement() throws Exception {
        test("Element", "SVGClipPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Element_SVGCursorElement() throws Exception {
        test("Element", "SVGCursorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGDefsElement() throws Exception {
        test("Element", "SVGDefsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGDescElement() throws Exception {
        test("Element", "SVGDescElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGElement() throws Exception {
        test("Element", "SVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGEllipseElement() throws Exception {
        test("Element", "SVGEllipseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEBlendElement() throws Exception {
        test("Element", "SVGFEBlendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEColorMatrixElement() throws Exception {
        test("Element", "SVGFEColorMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEComponentTransferElement() throws Exception {
        test("Element", "SVGFEComponentTransferElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFECompositeElement() throws Exception {
        test("Element", "SVGFECompositeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEConvolveMatrixElement() throws Exception {
        test("Element", "SVGFEConvolveMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEDiffuseLightingElement() throws Exception {
        test("Element", "SVGFEDiffuseLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEDisplacementMapElement() throws Exception {
        test("Element", "SVGFEDisplacementMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEDistantLightElement() throws Exception {
        test("Element", "SVGFEDistantLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEFloodElement() throws Exception {
        test("Element", "SVGFEFloodElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEFuncAElement() throws Exception {
        test("Element", "SVGFEFuncAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEFuncBElement() throws Exception {
        test("Element", "SVGFEFuncBElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEFuncGElement() throws Exception {
        test("Element", "SVGFEFuncGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEFuncRElement() throws Exception {
        test("Element", "SVGFEFuncRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEGaussianBlurElement() throws Exception {
        test("Element", "SVGFEGaussianBlurElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEImageElement() throws Exception {
        test("Element", "SVGFEImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEMergeElement() throws Exception {
        test("Element", "SVGFEMergeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEMergeNodeElement() throws Exception {
        test("Element", "SVGFEMergeNodeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEMorphologyElement() throws Exception {
        test("Element", "SVGFEMorphologyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEOffsetElement() throws Exception {
        test("Element", "SVGFEOffsetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEPointLightElement() throws Exception {
        test("Element", "SVGFEPointLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFESpecularLightingElement() throws Exception {
        test("Element", "SVGFESpecularLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFESpotLightElement() throws Exception {
        test("Element", "SVGFESpotLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFETileElement() throws Exception {
        test("Element", "SVGFETileElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFETurbulenceElement() throws Exception {
        test("Element", "SVGFETurbulenceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFilterElement() throws Exception {
        test("Element", "SVGFilterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Element_SVGForeignObjectElement() throws Exception {
        test("Element", "SVGForeignObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGGElement() throws Exception {
        test("Element", "SVGGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGImageElement() throws Exception {
        test("Element", "SVGImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGLineElement() throws Exception {
        test("Element", "SVGLineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGLinearGradientElement() throws Exception {
        test("Element", "SVGLinearGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGMarkerElement() throws Exception {
        test("Element", "SVGMarkerElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGMaskElement() throws Exception {
        test("Element", "SVGMaskElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGMetadataElement() throws Exception {
        test("Element", "SVGMetadataElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Element_SVGMPathElement() throws Exception {
        test("Element", "SVGMPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGPathElement() throws Exception {
        test("Element", "SVGPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGPatternElement() throws Exception {
        test("Element", "SVGPatternElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGPolygonElement() throws Exception {
        test("Element", "SVGPolygonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGPolylineElement() throws Exception {
        test("Element", "SVGPolylineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGRadialGradientElement() throws Exception {
        test("Element", "SVGRadialGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGRectElement() throws Exception {
        test("Element", "SVGRectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGSVGElement() throws Exception {
        test("Element", "SVGSVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGScriptElement() throws Exception {
        test("Element", "SVGScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Element_SVGSetElement() throws Exception {
        test("Element", "SVGSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGStopElement() throws Exception {
        test("Element", "SVGStopElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGStyleElement() throws Exception {
        test("Element", "SVGStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGSwitchElement() throws Exception {
        test("Element", "SVGSwitchElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGSymbolElement() throws Exception {
        test("Element", "SVGSymbolElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGTSpanElement() throws Exception {
        test("Element", "SVGTSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGTextElement() throws Exception {
        test("Element", "SVGTextElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGTextPathElement() throws Exception {
        test("Element", "SVGTextPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGTitleElement() throws Exception {
        test("Element", "SVGTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGUseElement() throws Exception {
        test("Element", "SVGUseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGViewElement() throws Exception {
        test("Element", "SVGViewElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Event_BeforeUnloadEvent() throws Exception {
        test("Event", "BeforeUnloadEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Event_HashChangeEvent() throws Exception {
        test("Event", "HashChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Event_KeyboardEvent() throws Exception {
        test("Event", "KeyboardEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Event_MessageEvent() throws Exception {
        test("Event", "MessageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Event_MouseEvent() throws Exception {
        test("Event", "MouseEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Event_MutationEvent() throws Exception {
        test("Event", "MutationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Event_PointerEvent() throws Exception {
        test("Event", "PointerEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Event_UIEvent() throws Exception {
        test("Event", "UIEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    @NotYetImplemented(CHROME)
    public void _HTMLCollection_HTMLAllCollection() throws Exception {
        test("HTMLCollection", "HTMLAllCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLAnchorElement() throws Exception {
        test("HTMLElement", "HTMLAnchorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLAppletElement() throws Exception {
        test("HTMLElement", "HTMLAppletElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLAreaElement() throws Exception {
        test("HTMLElement", "HTMLAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLAudioElement() throws Exception {
        test("HTMLElement", "HTMLAudioElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLElement_HTMLBGSoundElement() throws Exception {
        test("HTMLElement", "HTMLBGSoundElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLBRElement() throws Exception {
        test("HTMLElement", "HTMLBRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLBaseElement() throws Exception {
        test("HTMLElement", "HTMLBaseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLElement_HTMLBaseFontElement() throws Exception {
        test("HTMLElement", "HTMLBaseFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLElement_HTMLBlockElement() throws Exception {
        test("HTMLElement", "HTMLBlockElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLQuoteElement() throws Exception {
        test("HTMLElement", "HTMLQuoteElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLBodyElement() throws Exception {
        test("HTMLElement", "HTMLBodyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLButtonElement() throws Exception {
        test("HTMLElement", "HTMLButtonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLCanvasElement() throws Exception {
        test("HTMLElement", "HTMLCanvasElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLDataListElement() throws Exception {
        test("HTMLElement", "HTMLDataListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLElement_HTMLDDElement() throws Exception {
        test("HTMLElement", "HTMLDDElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _HTMLElement_HTMLDetailsElement() throws Exception {
        test("HTMLElement", "HTMLDetailsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _HTMLElement_HTMLDialogElement() throws Exception {
        test("HTMLElement", "HTMLDialogElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLElement_HTMLDTElement() throws Exception {
        test("HTMLElement", "HTMLDTElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLDListElement() throws Exception {
        test("HTMLElement", "HTMLDListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLDirectoryElement() throws Exception {
        test("HTMLElement", "HTMLDirectoryElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLDivElement() throws Exception {
        test("HTMLElement", "HTMLDivElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLEmbedElement() throws Exception {
        test("HTMLElement", "HTMLEmbedElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLFieldSetElement() throws Exception {
        test("HTMLElement", "HTMLFieldSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLFontElement() throws Exception {
        test("HTMLElement", "HTMLFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLFormElement() throws Exception {
        test("HTMLElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLFrameElement() throws Exception {
        test("HTMLElement", "HTMLFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLFrameSetElement() throws Exception {
        test("HTMLElement", "HTMLFrameSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLHRElement() throws Exception {
        test("HTMLElement", "HTMLHRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLHeadElement() throws Exception {
        test("HTMLElement", "HTMLHeadElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLHeadingElement() throws Exception {
        test("HTMLElement", "HTMLHeadingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
        IE8 = "false")
    public void _HTMLElement_HTMLHtmlElement() throws Exception {
        test("HTMLElement", "HTMLHtmlElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLIFrameElement() throws Exception {
        test("HTMLElement", "HTMLIFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLImageElement() throws Exception {
        test("HTMLElement", "HTMLImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLInputElement() throws Exception {
        test("HTMLElement", "HTMLInputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLElement_HTMLIsIndexElement() throws Exception {
        test("HTMLElement", "HTMLIsIndexElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _HTMLElement_HTMLKeygenElement() throws Exception {
        test("HTMLElement", "HTMLKeygenElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLLIElement() throws Exception {
        test("HTMLElement", "HTMLLIElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLLabelElement() throws Exception {
        test("HTMLElement", "HTMLLabelElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLLegendElement() throws Exception {
        test("HTMLElement", "HTMLLegendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLLinkElement() throws Exception {
        test("HTMLElement", "HTMLLinkElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLMapElement() throws Exception {
        test("HTMLElement", "HTMLMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false",
            IE8 = "false")
    public void _HTMLElement_HTMLMarqueeElement() throws Exception {
        test("HTMLElement", "HTMLMarqueeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLMediaElement() throws Exception {
        test("HTMLElement", "HTMLMediaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLMenuElement() throws Exception {
        test("HTMLElement", "HTMLMenuElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _HTMLElement_HTMLMenuItemElement() throws Exception {
        test("HTMLElement", "HTMLMenuItemElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLMetaElement() throws Exception {
        test("HTMLElement", "HTMLMetaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLElement_HTMLMeterElement() throws Exception {
        test("HTMLElement", "HTMLMeterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLModElement() throws Exception {
        test("HTMLElement", "HTMLModElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLElement_HTMLNextIdElement() throws Exception {
        test("HTMLElement", "HTMLNextIdElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLOListElement() throws Exception {
        test("HTMLElement", "HTMLOListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLObjectElement() throws Exception {
        test("HTMLElement", "HTMLObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLOptGroupElement() throws Exception {
        test("HTMLElement", "HTMLOptGroupElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLOptionElement() throws Exception {
        test("HTMLElement", "HTMLOptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLElement_HTMLOutputElement() throws Exception {
        test("HTMLElement", "HTMLOutputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLParagraphElement() throws Exception {
        test("HTMLElement", "HTMLParagraphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLParamElement() throws Exception {
        test("HTMLElement", "HTMLParamElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLElement_HTMLPhraseElement() throws Exception {
        test("HTMLElement", "HTMLPhraseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLPreElement() throws Exception {
        test("HTMLElement", "HTMLPreElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLProgressElement() throws Exception {
        test("HTMLElement", "HTMLProgressElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLScriptElement() throws Exception {
        test("HTMLElement", "HTMLScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLSelectElement() throws Exception {
        test("HTMLElement", "HTMLSelectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLSourceElement() throws Exception {
        test("HTMLElement", "HTMLSourceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLSpanElement() throws Exception {
        test("HTMLElement", "HTMLSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLStyleElement() throws Exception {
        test("HTMLElement", "HTMLStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLTableCaptionElement() throws Exception {
        test("HTMLElement", "HTMLTableCaptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLTableCellElement() throws Exception {
        test("HTMLElement", "HTMLTableCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLTableColElement() throws Exception {
        test("HTMLElement", "HTMLTableColElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLElement_HTMLTableDataCellElement() throws Exception {
        test("HTMLElement", "HTMLTableDataCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLTableElement() throws Exception {
        test("HTMLElement", "HTMLTableElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLElement_HTMLTableHeaderCellElement() throws Exception {
        test("HTMLElement", "HTMLTableHeaderCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLTableRowElement() throws Exception {
        test("HTMLElement", "HTMLTableRowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLTableSectionElement() throws Exception {
        test("HTMLElement", "HTMLTableSectionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLTextAreaElement() throws Exception {
        test("HTMLElement", "HTMLTextAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _HTMLElement_HTMLTimeElement() throws Exception {
        test("HTMLElement", "HTMLTimeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLTitleElement() throws Exception {
        test("HTMLElement", "HTMLTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLTrackElement() throws Exception {
        test("HTMLElement", "HTMLTrackElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLUListElement() throws Exception {
        test("HTMLElement", "HTMLUListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLUnknownElement() throws Exception {
        test("HTMLElement", "HTMLUnknownElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLVideoElement() throws Exception {
        test("HTMLElement", "HTMLVideoElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_Image() throws Exception {
        test("HTMLElement", "Image");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_Option() throws Exception {
        test("HTMLElement", "Option");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLMediaElement_HTMLAudioElement() throws Exception {
        test("HTMLMediaElement", "HTMLAudioElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLMediaElement_HTMLVideoElement() throws Exception {
        test("HTMLMediaElement", "HTMLVideoElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLTableCellElement_HTMLTableDataCellElement() throws Exception {
        test("HTMLTableCellElement", "HTMLTableDataCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLTableCellElement_HTMLTableHeaderCellElement() throws Exception {
        test("HTMLTableCellElement", "HTMLTableHeaderCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _MouseEvent_PointerEvent() throws Exception {
        test("MouseEvent", "PointerEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_Attr() throws Exception {
        test("Node", "Attr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_CDATASection() throws Exception {
        test("Node", "CDATASection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_Comment() throws Exception {
        test("Node", "Comment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_Document() throws Exception {
        test("Node", "Document");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_DocumentFragment() throws Exception {
        test("Node", "DocumentFragment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_DocumentType() throws Exception {
        test("Node", "DocumentType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_Element() throws Exception {
        test("Node", "Element");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLAnchorElement() throws Exception {
        test("Node", "HTMLAnchorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLAppletElement() throws Exception {
        test("Node", "HTMLAppletElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLAreaElement() throws Exception {
        test("Node", "HTMLAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLAudioElement() throws Exception {
        test("Node", "HTMLAudioElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Node_HTMLBGSoundElement() throws Exception {
        test("Node", "HTMLBGSoundElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLBRElement() throws Exception {
        test("Node", "HTMLBRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLBaseElement() throws Exception {
        test("Node", "HTMLBaseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Node_HTMLBaseFontElement() throws Exception {
        test("Node", "HTMLBaseFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Node_HTMLBlockElement() throws Exception {
        test("Node", "HTMLBlockElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLQuoteElement() throws Exception {
        test("Node", "HTMLQuoteElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLBodyElement() throws Exception {
        test("Node", "HTMLBodyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLButtonElement() throws Exception {
        test("Node", "HTMLButtonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLCanvasElement() throws Exception {
        test("Node", "HTMLCanvasElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLDataListElement() throws Exception {
        test("Node", "HTMLDataListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Node_HTMLDDElement() throws Exception {
        test("Node", "HTMLDDElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Node_HTMLDetailsElement() throws Exception {
        test("Node", "HTMLDetailsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Node_HTMLDialogElement() throws Exception {
        test("Node", "HTMLDialogElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Node_HTMLDTElement() throws Exception {
        test("Node", "HTMLDTElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLDListElement() throws Exception {
        test("Node", "HTMLDListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLDirectoryElement() throws Exception {
        test("Node", "HTMLDirectoryElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLDivElement() throws Exception {
        test("Node", "HTMLDivElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLDocument() throws Exception {
        test("Node", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLElement() throws Exception {
        test("Node", "HTMLElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLEmbedElement() throws Exception {
        test("Node", "HTMLEmbedElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLFieldSetElement() throws Exception {
        test("Node", "HTMLFieldSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLFontElement() throws Exception {
        test("Node", "HTMLFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLFormElement() throws Exception {
        test("Node", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLFrameElement() throws Exception {
        test("Node", "HTMLFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLFrameSetElement() throws Exception {
        test("Node", "HTMLFrameSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLHRElement() throws Exception {
        test("Node", "HTMLHRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLHeadElement() throws Exception {
        test("Node", "HTMLHeadElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLHeadingElement() throws Exception {
        test("Node", "HTMLHeadingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLHtmlElement() throws Exception {
        test("Node", "HTMLHtmlElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLIFrameElement() throws Exception {
        test("Node", "HTMLIFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLImageElement() throws Exception {
        test("Node", "HTMLImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLInputElement() throws Exception {
        test("Node", "HTMLInputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Node_HTMLIsIndexElement() throws Exception {
        test("Node", "HTMLIsIndexElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Node_HTMLKeygenElement() throws Exception {
        test("Node", "HTMLKeygenElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLLIElement() throws Exception {
        test("Node", "HTMLLIElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLLabelElement() throws Exception {
        test("Node", "HTMLLabelElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLLegendElement() throws Exception {
        test("Node", "HTMLLegendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLLinkElement() throws Exception {
        test("Node", "HTMLLinkElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLMapElement() throws Exception {
        test("Node", "HTMLMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false",
            IE8 = "false")
    public void _Node_HTMLMarqueeElement() throws Exception {
        test("Node", "HTMLMarqueeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLMediaElement() throws Exception {
        test("Node", "HTMLMediaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLMenuElement() throws Exception {
        test("Node", "HTMLMenuElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Node_HTMLMenuItemElement() throws Exception {
        test("Node", "HTMLMenuItemElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLMetaElement() throws Exception {
        test("Node", "HTMLMetaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_HTMLMeterElement() throws Exception {
        test("Node", "HTMLMeterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLModElement() throws Exception {
        test("Node", "HTMLModElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Node_HTMLNextIdElement() throws Exception {
        test("Node", "HTMLNextIdElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLOListElement() throws Exception {
        test("Node", "HTMLOListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLObjectElement() throws Exception {
        test("Node", "HTMLObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLOptGroupElement() throws Exception {
        test("Node", "HTMLOptGroupElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLOptionElement() throws Exception {
        test("Node", "HTMLOptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_HTMLOutputElement() throws Exception {
        test("Node", "HTMLOutputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLParagraphElement() throws Exception {
        test("Node", "HTMLParagraphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLParamElement() throws Exception {
        test("Node", "HTMLParamElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Node_HTMLPhraseElement() throws Exception {
        test("Node", "HTMLPhraseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLPreElement() throws Exception {
        test("Node", "HTMLPreElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLProgressElement() throws Exception {
        test("Node", "HTMLProgressElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLScriptElement() throws Exception {
        test("Node", "HTMLScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLSelectElement() throws Exception {
        test("Node", "HTMLSelectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLSourceElement() throws Exception {
        test("Node", "HTMLSourceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLSpanElement() throws Exception {
        test("Node", "HTMLSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLStyleElement() throws Exception {
        test("Node", "HTMLStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLTableCaptionElement() throws Exception {
        test("Node", "HTMLTableCaptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLTableCellElement() throws Exception {
        test("Node", "HTMLTableCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLTableColElement() throws Exception {
        test("Node", "HTMLTableColElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Node_HTMLTableDataCellElement() throws Exception {
        test("Node", "HTMLTableDataCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLTableElement() throws Exception {
        test("Node", "HTMLTableElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Node_HTMLTableHeaderCellElement() throws Exception {
        test("Node", "HTMLTableHeaderCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLTableRowElement() throws Exception {
        test("Node", "HTMLTableRowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLTableSectionElement() throws Exception {
        test("Node", "HTMLTableSectionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLTextAreaElement() throws Exception {
        test("Node", "HTMLTextAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Node_HTMLTimeElement() throws Exception {
        test("Node", "HTMLTimeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLTitleElement() throws Exception {
        test("Node", "HTMLTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLTrackElement() throws Exception {
        test("Node", "HTMLTrackElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLUListElement() throws Exception {
        test("Node", "HTMLUListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLUnknownElement() throws Exception {
        test("Node", "HTMLUnknownElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLVideoElement() throws Exception {
        test("Node", "HTMLVideoElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_Image() throws Exception {
        test("Node", "Image");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_Option() throws Exception {
        test("Node", "Option");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_ProcessingInstruction() throws Exception {
        test("Node", "ProcessingInstruction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGAElement() throws Exception {
        test("Node", "SVGAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Node_SVGAltGlyphElement() throws Exception {
        test("Node", "SVGAltGlyphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_SVGAnimateElement() throws Exception {
        test("Node", "SVGAnimateElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_SVGAnimateMotionElement() throws Exception {
        test("Node", "SVGAnimateMotionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_SVGAnimateTransformElement() throws Exception {
        test("Node", "SVGAnimateTransformElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGCircleElement() throws Exception {
        test("Node", "SVGCircleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGClipPathElement() throws Exception {
        test("Node", "SVGClipPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Node_SVGCursorElement() throws Exception {
        test("Node", "SVGCursorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGDefsElement() throws Exception {
        test("Node", "SVGDefsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGDescElement() throws Exception {
        test("Node", "SVGDescElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGElement() throws Exception {
        test("Node", "SVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGEllipseElement() throws Exception {
        test("Node", "SVGEllipseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEBlendElement() throws Exception {
        test("Node", "SVGFEBlendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEColorMatrixElement() throws Exception {
        test("Node", "SVGFEColorMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEComponentTransferElement() throws Exception {
        test("Node", "SVGFEComponentTransferElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFECompositeElement() throws Exception {
        test("Node", "SVGFECompositeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEConvolveMatrixElement() throws Exception {
        test("Node", "SVGFEConvolveMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEDiffuseLightingElement() throws Exception {
        test("Node", "SVGFEDiffuseLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEDisplacementMapElement() throws Exception {
        test("Node", "SVGFEDisplacementMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEDistantLightElement() throws Exception {
        test("Node", "SVGFEDistantLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEFloodElement() throws Exception {
        test("Node", "SVGFEFloodElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEFuncAElement() throws Exception {
        test("Node", "SVGFEFuncAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEFuncBElement() throws Exception {
        test("Node", "SVGFEFuncBElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEFuncGElement() throws Exception {
        test("Node", "SVGFEFuncGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEFuncRElement() throws Exception {
        test("Node", "SVGFEFuncRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEGaussianBlurElement() throws Exception {
        test("Node", "SVGFEGaussianBlurElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEImageElement() throws Exception {
        test("Node", "SVGFEImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEMergeElement() throws Exception {
        test("Node", "SVGFEMergeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEMergeNodeElement() throws Exception {
        test("Node", "SVGFEMergeNodeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEMorphologyElement() throws Exception {
        test("Node", "SVGFEMorphologyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEOffsetElement() throws Exception {
        test("Node", "SVGFEOffsetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEPointLightElement() throws Exception {
        test("Node", "SVGFEPointLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFESpecularLightingElement() throws Exception {
        test("Node", "SVGFESpecularLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFESpotLightElement() throws Exception {
        test("Node", "SVGFESpotLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFETileElement() throws Exception {
        test("Node", "SVGFETileElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFETurbulenceElement() throws Exception {
        test("Node", "SVGFETurbulenceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFilterElement() throws Exception {
        test("Node", "SVGFilterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_SVGForeignObjectElement() throws Exception {
        test("Node", "SVGForeignObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGGElement() throws Exception {
        test("Node", "SVGGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGImageElement() throws Exception {
        test("Node", "SVGImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGLineElement() throws Exception {
        test("Node", "SVGLineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGLinearGradientElement() throws Exception {
        test("Node", "SVGLinearGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGMarkerElement() throws Exception {
        test("Node", "SVGMarkerElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGMaskElement() throws Exception {
        test("Node", "SVGMaskElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGMetadataElement() throws Exception {
        test("Node", "SVGMetadataElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_SVGMPathElement() throws Exception {
        test("Node", "SVGMPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGPathElement() throws Exception {
        test("Node", "SVGPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGPatternElement() throws Exception {
        test("Node", "SVGPatternElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGPolygonElement() throws Exception {
        test("Node", "SVGPolygonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGPolylineElement() throws Exception {
        test("Node", "SVGPolylineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGRadialGradientElement() throws Exception {
        test("Node", "SVGRadialGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGRectElement() throws Exception {
        test("Node", "SVGRectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGSVGElement() throws Exception {
        test("Node", "SVGSVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGScriptElement() throws Exception {
        test("Node", "SVGScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_SVGSetElement() throws Exception {
        test("Node", "SVGSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGStopElement() throws Exception {
        test("Node", "SVGStopElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGStyleElement() throws Exception {
        test("Node", "SVGStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGSwitchElement() throws Exception {
        test("Node", "SVGSwitchElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGSymbolElement() throws Exception {
        test("Node", "SVGSymbolElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGTSpanElement() throws Exception {
        test("Node", "SVGTSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGTextElement() throws Exception {
        test("Node", "SVGTextElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGTextPathElement() throws Exception {
        test("Node", "SVGTextPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGTitleElement() throws Exception {
        test("Node", "SVGTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGUseElement() throws Exception {
        test("Node", "SVGUseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGViewElement() throws Exception {
        test("Node", "SVGViewElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_Text() throws Exception {
        test("Node", "Text");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_XMLDocument() throws Exception {
        test("Node", "XMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ CHROME, IE11 })
    public void _NodeList_HTMLAllCollection() throws Exception {
        test("NodeList", "HTMLAllCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented
    public void _NodeList_HTMLCollection() throws Exception {
        test("NodeList", "HTMLCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGAElement() throws Exception {
        test("SVGElement", "SVGAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _SVGElement_SVGAltGlyphElement() throws Exception {
        test("SVGElement", "SVGAltGlyphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGElement_SVGAnimateElement() throws Exception {
        test("SVGElement", "SVGAnimateElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGElement_SVGAnimateMotionElement() throws Exception {
        test("SVGElement", "SVGAnimateMotionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGElement_SVGAnimateTransformElement() throws Exception {
        test("SVGElement", "SVGAnimateTransformElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGCircleElement() throws Exception {
        test("SVGElement", "SVGCircleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGClipPathElement() throws Exception {
        test("SVGElement", "SVGClipPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _SVGElement_SVGCursorElement() throws Exception {
        test("SVGElement", "SVGCursorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGDefsElement() throws Exception {
        test("SVGElement", "SVGDefsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGDescElement() throws Exception {
        test("SVGElement", "SVGDescElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGEllipseElement() throws Exception {
        test("SVGElement", "SVGEllipseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFEBlendElement() throws Exception {
        test("SVGElement", "SVGFEBlendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFEColorMatrixElement() throws Exception {
        test("SVGElement", "SVGFEColorMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFEComponentTransferElement() throws Exception {
        test("SVGElement", "SVGFEComponentTransferElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFECompositeElement() throws Exception {
        test("SVGElement", "SVGFECompositeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFEConvolveMatrixElement() throws Exception {
        test("SVGElement", "SVGFEConvolveMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFEDiffuseLightingElement() throws Exception {
        test("SVGElement", "SVGFEDiffuseLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFEDisplacementMapElement() throws Exception {
        test("SVGElement", "SVGFEDisplacementMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFEDistantLightElement() throws Exception {
        test("SVGElement", "SVGFEDistantLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFEFloodElement() throws Exception {
        test("SVGElement", "SVGFEFloodElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFEFuncAElement() throws Exception {
        test("SVGElement", "SVGFEFuncAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFEFuncBElement() throws Exception {
        test("SVGElement", "SVGFEFuncBElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFEFuncGElement() throws Exception {
        test("SVGElement", "SVGFEFuncGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFEFuncRElement() throws Exception {
        test("SVGElement", "SVGFEFuncRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFEGaussianBlurElement() throws Exception {
        test("SVGElement", "SVGFEGaussianBlurElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFEImageElement() throws Exception {
        test("SVGElement", "SVGFEImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFEMergeElement() throws Exception {
        test("SVGElement", "SVGFEMergeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFEMergeNodeElement() throws Exception {
        test("SVGElement", "SVGFEMergeNodeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFEMorphologyElement() throws Exception {
        test("SVGElement", "SVGFEMorphologyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFEOffsetElement() throws Exception {
        test("SVGElement", "SVGFEOffsetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFEPointLightElement() throws Exception {
        test("SVGElement", "SVGFEPointLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFESpecularLightingElement() throws Exception {
        test("SVGElement", "SVGFESpecularLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFESpotLightElement() throws Exception {
        test("SVGElement", "SVGFESpotLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFETileElement() throws Exception {
        test("SVGElement", "SVGFETileElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFETurbulenceElement() throws Exception {
        test("SVGElement", "SVGFETurbulenceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGFilterElement() throws Exception {
        test("SVGElement", "SVGFilterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGElement_SVGForeignObjectElement() throws Exception {
        test("SVGElement", "SVGForeignObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGGElement() throws Exception {
        test("SVGElement", "SVGGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGImageElement() throws Exception {
        test("SVGElement", "SVGImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGLineElement() throws Exception {
        test("SVGElement", "SVGLineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGLinearGradientElement() throws Exception {
        test("SVGElement", "SVGLinearGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGMarkerElement() throws Exception {
        test("SVGElement", "SVGMarkerElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGMaskElement() throws Exception {
        test("SVGElement", "SVGMaskElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGMetadataElement() throws Exception {
        test("SVGElement", "SVGMetadataElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGElement_SVGMPathElement() throws Exception {
        test("SVGElement", "SVGMPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGPathElement() throws Exception {
        test("SVGElement", "SVGPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGPatternElement() throws Exception {
        test("SVGElement", "SVGPatternElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGPolygonElement() throws Exception {
        test("SVGElement", "SVGPolygonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGPolylineElement() throws Exception {
        test("SVGElement", "SVGPolylineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGRadialGradientElement() throws Exception {
        test("SVGElement", "SVGRadialGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGRectElement() throws Exception {
        test("SVGElement", "SVGRectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGSVGElement() throws Exception {
        test("SVGElement", "SVGSVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGScriptElement() throws Exception {
        test("SVGElement", "SVGScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGElement_SVGSetElement() throws Exception {
        test("SVGElement", "SVGSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGStopElement() throws Exception {
        test("SVGElement", "SVGStopElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGStyleElement() throws Exception {
        test("SVGElement", "SVGStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGSwitchElement() throws Exception {
        test("SVGElement", "SVGSwitchElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGSymbolElement() throws Exception {
        test("SVGElement", "SVGSymbolElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGTSpanElement() throws Exception {
        test("SVGElement", "SVGTSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGTextElement() throws Exception {
        test("SVGElement", "SVGTextElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGTextPathElement() throws Exception {
        test("SVGElement", "SVGTextPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGTitleElement() throws Exception {
        test("SVGElement", "SVGTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGUseElement() throws Exception {
        test("SVGElement", "SVGUseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGViewElement() throws Exception {
        test("SVGElement", "SVGViewElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Text_CDATASection() throws Exception {
        test("Text", "CDATASection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _UIEvent_KeyboardEvent() throws Exception {
        test("UIEvent", "KeyboardEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _UIEvent_MouseEvent() throws Exception {
        test("UIEvent", "MouseEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _UIEvent_PointerEvent() throws Exception {
        test("UIEvent", "PointerEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _URLSearchParams_URLSearchParams() throws Exception {
        test("URLSearchParams", "URLSearchParams");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _FormData_FormData() throws Exception {
        test("FormData", "FormData");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false",
            IE8 = "false")
    public void _MessageChannel_MessageChannel() throws Exception {
        test("MessageChannel", "MessageChannel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _MessagePort_MessagePort() throws Exception {
        test("MessagePort", "MessagePort");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Promise_Promise() throws Exception {
        test("Promise", "Promise");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SharedWorker_SharedWorker() throws Exception {
        test("SharedWorker", "SharedWorker");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Worker_Worker() throws Exception {
        test("Worker", "Worker");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _WebGLRenderingContext_WebGLRenderingContext() throws Exception {
        test("WebGLRenderingContext", "WebGLRenderingContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGGradientElement() throws Exception {
        test("Element", "SVGGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGGradientElement() throws Exception {
        test("Node", "SVGGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGGradientElement() throws Exception {
        test("SVGElement", "SVGGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGGradientElement_SVGGradientElement() throws Exception {
        test("SVGGradientElement", "SVGGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGGradientElement_SVGLinearGradientElement() throws Exception {
        test("SVGGradientElement", "SVGLinearGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGGradientElement_SVGRadialGradientElement() throws Exception {
        test("SVGGradientElement", "SVGRadialGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGAnimatedAngle_SVGAnimatedAngle() throws Exception {
        test("SVGAnimatedAngle", "SVGAnimatedAngle");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGAnimatedBoolean_SVGAnimatedBoolean() throws Exception {
        test("SVGAnimatedBoolean", "SVGAnimatedBoolean");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGAnimatedEnumeration_SVGAnimatedEnumeration() throws Exception {
        test("SVGAnimatedEnumeration", "SVGAnimatedEnumeration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGAnimatedInteger_SVGAnimatedInteger() throws Exception {
        test("SVGAnimatedInteger", "SVGAnimatedInteger");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGAnimatedLengthList_SVGAnimatedLengthList() throws Exception {
        test("SVGAnimatedLengthList", "SVGAnimatedLengthList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGAnimatedLength_SVGAnimatedLength() throws Exception {
        test("SVGAnimatedLength", "SVGAnimatedLength");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGAnimatedNumberList_SVGAnimatedNumberList() throws Exception {
        test("SVGAnimatedNumberList", "SVGAnimatedNumberList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGAnimatedNumber_SVGAnimatedNumber() throws Exception {
        test("SVGAnimatedNumber", "SVGAnimatedNumber");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGAnimatedPreserveAspectRatio_SVGAnimatedPreserveAspectRatio() throws Exception {
        test("SVGAnimatedPreserveAspectRatio", "SVGAnimatedPreserveAspectRatio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGAnimatedRect_SVGAnimatedRect() throws Exception {
        test("SVGAnimatedRect", "SVGAnimatedRect");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGAnimatedString_SVGAnimatedString() throws Exception {
        test("SVGAnimatedString", "SVGAnimatedString");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGAnimatedTransformList_SVGAnimatedTransformList() throws Exception {
        test("SVGAnimatedTransformList", "SVGAnimatedTransformList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGAnimationElement_SVGAnimationElement() throws Exception {
        test("SVGAnimationElement", "SVGAnimationElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGLengthList_SVGLengthList() throws Exception {
        test("SVGLengthList", "SVGLengthList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGLength_SVGLength() throws Exception {
        test("SVGLength", "SVGLength");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGNumberList_SVGNumberList() throws Exception {
        test("SVGNumberList", "SVGNumberList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGNumber_SVGNumber() throws Exception {
        test("SVGNumber", "SVGNumber");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGPoint_SVGPoint() throws Exception {
        test("SVGPoint", "SVGPoint");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGPreserveAspectRatio_SVGPreserveAspectRatio() throws Exception {
        test("SVGPreserveAspectRatio", "SVGPreserveAspectRatio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGStringList_SVGStringList() throws Exception {
        test("SVGStringList", "SVGStringList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGTextPositioningElement_SVGTextPositioningElement() throws Exception {
        test("SVGTextPositioningElement", "SVGTextPositioningElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGTransformList_SVGTransformList() throws Exception {
        test("SVGTransformList", "SVGTransformList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGTransform_SVGTransform() throws Exception {
        test("SVGTransform", "SVGTransform");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _SVGDocument_SVGDocument() throws Exception {
        test("SVGDocument", "SVGDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AnalyserNode_AnalyserNode() throws Exception {
        test("AnalyserNode", "AnalyserNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioParam_AudioParam() throws Exception {
        test("AudioParam", "AudioParam");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _ChannelMergerNode_ChannelMergerNode() throws Exception {
        test("ChannelMergerNode", "ChannelMergerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _DocumentFragment_ShadowRoot() throws Exception {
        test("DocumentFragment", "ShadowRoot");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Element_HTMLShadowElement() throws Exception {
        test("Element", "HTMLShadowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLElement_HTMLShadowElement() throws Exception {
        test("HTMLElement", "HTMLShadowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLShadowElement_HTMLShadowElement() throws Exception {
        test("HTMLShadowElement", "HTMLShadowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_HTMLShadowElement() throws Exception {
        test("Node", "HTMLShadowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Node_ShadowRoot() throws Exception {
        test("Node", "ShadowRoot");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Path2D_Path2D() throws Exception {
        test("Path2D", "Path2D");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _ShadowRoot_ShadowRoot() throws Exception {
        test("ShadowRoot", "ShadowRoot");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _DOMCursor_DOMCursor() throws Exception {
        test("DOMCursor", "DOMCursor");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _LocalMediaStream_LocalMediaStream() throws Exception {
        test("LocalMediaStream", "LocalMediaStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
        IE = "false")
    public void _AudioBuffer_AudioBuffer() throws Exception {
        test("AudioBuffer", "AudioBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioBufferSourceNode_AudioBufferSourceNode() throws Exception {
        test("AudioBufferSourceNode", "AudioBufferSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioContext_AudioContext() throws Exception {
        test("AudioContext", "AudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioDestinationNode_AudioDestinationNode() throws Exception {
        test("AudioDestinationNode", "AudioDestinationNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioListener_AudioListener() throws Exception {
        test("AudioListener", "AudioListener");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_AnalyserNode() throws Exception {
        test("AudioNode", "AnalyserNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_AudioBufferSourceNode() throws Exception {
        test("AudioNode", "AudioBufferSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_AudioDestinationNode() throws Exception {
        test("AudioNode", "AudioDestinationNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_AudioNode() throws Exception {
        test("AudioNode", "AudioNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_BiquadFilterNode() throws Exception {
        test("AudioNode", "BiquadFilterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_ChannelMergerNode() throws Exception {
        test("AudioNode", "ChannelMergerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_ChannelSplitterNode() throws Exception {
        test("AudioNode", "ChannelSplitterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_ConvolverNode() throws Exception {
        test("AudioNode", "ConvolverNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_DelayNode() throws Exception {
        test("AudioNode", "DelayNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioProcessingEvent_AudioProcessingEvent() throws Exception {
        test("AudioProcessingEvent", "AudioProcessingEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _BiquadFilterNode_BiquadFilterNode() throws Exception {
        test("BiquadFilterNode", "BiquadFilterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _ChannelSplitterNode_ChannelSplitterNode() throws Exception {
        test("ChannelSplitterNode", "ChannelSplitterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _ConvolverNode_ConvolverNode() throws Exception {
        test("ConvolverNode", "ConvolverNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _DelayNode_DelayNode() throws Exception {
        test("DelayNode", "DelayNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Event_AudioProcessingEvent() throws Exception {
        test("Event", "AudioProcessingEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
        IE8 = "false")
    public void _CSSPageRule_CSSPageRule() throws Exception {
        test("CSSPageRule", "CSSPageRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
        IE8 = "false")
    public void _CSSRule_CSSPageRule() throws Exception {
        test("CSSRule", "CSSPageRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
        CHROME = "true",
        IE11 = "true")
    public void _ClientRectList_ClientRectList() throws Exception {
        test("ClientRectList", "ClientRectList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioContext_OfflineAudioContext() throws Exception {
        test("AudioContext", "OfflineAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_DynamicsCompressorNode() throws Exception {
        test("AudioNode", "DynamicsCompressorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_GainNode() throws Exception {
        test("AudioNode", "GainNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_MediaStreamAudioDestinationNode() throws Exception {
        test("AudioNode", "MediaStreamAudioDestinationNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_MediaStreamAudioSourceNode() throws Exception {
        test("AudioNode", "MediaStreamAudioSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_OscillatorNode() throws Exception {
        test("AudioNode", "OscillatorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _AudioNode_PannerNode() throws Exception {
        test("AudioNode", "PannerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _PannerNode_PannerNode() throws Exception {
        test("PannerNode", "PannerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_ScriptProcessorNode() throws Exception {
        test("AudioNode", "ScriptProcessorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_WaveShaperNode() throws Exception {
        test("AudioNode", "WaveShaperNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _DynamicsCompressorNode_DynamicsCompressorNode() throws Exception {
        test("DynamicsCompressorNode", "DynamicsCompressorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Event_MediaStreamEvent() throws Exception {
        test("Event", "MediaStreamEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Event_OfflineAudioCompletionEvent() throws Exception {
        test("Event", "OfflineAudioCompletionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _GainNode_GainNode() throws Exception {
        test("GainNode", "GainNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MediaStreamAudioDestinationNode_MediaStreamAudioDestinationNode() throws Exception {
        test("MediaStreamAudioDestinationNode", "MediaStreamAudioDestinationNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MediaStreamAudioSourceNode_MediaStreamAudioSourceNode() throws Exception {
        test("MediaStreamAudioSourceNode", "MediaStreamAudioSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MediaStreamEvent_MediaStreamEvent() throws Exception {
        test("MediaStreamEvent", "MediaStreamEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _OfflineAudioCompletionEvent_OfflineAudioCompletionEvent() throws Exception {
        test("OfflineAudioCompletionEvent", "OfflineAudioCompletionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _OfflineAudioContext_OfflineAudioContext() throws Exception {
        test("OfflineAudioContext", "OfflineAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _OscillatorNode_OscillatorNode() throws Exception {
        test("OscillatorNode", "OscillatorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _PeriodicWave_PeriodicWave() throws Exception {
        test("PeriodicWave", "PeriodicWave");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _ScriptProcessorNode_ScriptProcessorNode() throws Exception {
        test("ScriptProcessorNode", "ScriptProcessorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _WaveShaperNode_WaveShaperNode() throws Exception {
        test("WaveShaperNode", "WaveShaperNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Element_HTMLContentElement() throws Exception {
        test("Element", "HTMLContentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLContentElement_HTMLContentElement() throws Exception {
        test("HTMLContentElement", "HTMLContentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLElement_HTMLContentElement() throws Exception {
        test("HTMLElement", "HTMLContentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_HTMLContentElement() throws Exception {
        test("Node", "HTMLContentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _EventTarget_HTMLDataElement() throws Exception {
        test("EventTarget", "HTMLDataElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Element_HTMLDataElement() throws Exception {
        test("Element", "HTMLDataElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _HTMLDataElement_HTMLDataElement() throws Exception {
        test("HTMLDataElement", "HTMLDataElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _HTMLElement_HTMLDataElement() throws Exception {
        test("HTMLElement", "HTMLDataElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Node_HTMLDataElement() throws Exception {
        test("Node", "HTMLDataElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CharacterData_CDATASection() throws Exception {
        test("CharacterData", "CDATASection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CharacterData_CharacterData() throws Exception {
        test("CharacterData", "CharacterData");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CharacterData_Comment() throws Exception {
        test("CharacterData", "Comment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CharacterData_ProcessingInstruction() throws Exception {
        test("CharacterData", "ProcessingInstruction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CharacterData_Text() throws Exception {
        test("CharacterData", "Text");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_CharacterData() throws Exception {
        test("Node", "CharacterData");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_MediaElementAudioSourceNode() throws Exception {
        test("AudioNode", "MediaElementAudioSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            IE11 = "true")
    public void _CSSKeyframeRule_CSSKeyframeRule() throws Exception {
        test("CSSKeyframeRule", "CSSKeyframeRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            IE11 = "true")
    public void _CSSKeyframesRule_CSSKeyframesRule() throws Exception {
        test("CSSKeyframesRule", "CSSKeyframesRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            IE11 = "true")
    public void _CSSRule_CSSKeyframeRule() throws Exception {
        test("CSSRule", "CSSKeyframeRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            IE11 = "true")
    public void _CSSRule_CSSKeyframesRule() throws Exception {
        test("CSSRule", "CSSKeyframesRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _CSSRule_CSSSupportsRule() throws Exception {
        test("CSSRule", "CSSSupportsRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _CSSSupportsRule_CSSSupportsRule() throws Exception {
        test("CSSSupportsRule", "CSSSupportsRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CanvasGradient_CanvasGradient() throws Exception {
        test("CanvasGradient", "CanvasGradient");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CanvasPattern_CanvasPattern() throws Exception {
        test("CanvasPattern", "CanvasPattern");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _ClipboardEvent_ClipboardEvent() throws Exception {
        test("ClipboardEvent", "ClipboardEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _CloseEvent_CloseEvent() throws Exception {
        test("CloseEvent", "CloseEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CompositionEvent_CompositionEvent() throws Exception {
        test("CompositionEvent", "CompositionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CustomEvent_CustomEvent() throws Exception {
        test("CustomEvent", "CustomEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _DeviceMotionEvent_DeviceMotionEvent() throws Exception {
        test("DeviceMotionEvent", "DeviceMotionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _DeviceOrientationEvent_DeviceOrientationEvent() throws Exception {
        test("DeviceOrientationEvent", "DeviceOrientationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _ErrorEvent_ErrorEvent() throws Exception {
        test("ErrorEvent", "ErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventSource_EventSource() throws Exception {
        test("EventSource", "EventSource");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_AnalyserNode() throws Exception {
        test("EventTarget", "AnalyserNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_ApplicationCache() throws Exception {
        test("EventTarget", "ApplicationCache");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_Attr() throws Exception {
        test("EventTarget", "Attr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_AudioBufferSourceNode() throws Exception {
        test("EventTarget", "AudioBufferSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_AudioContext() throws Exception {
        test("EventTarget", "AudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_AudioDestinationNode() throws Exception {
        test("EventTarget", "AudioDestinationNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_AudioNode() throws Exception {
        test("EventTarget", "AudioNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_BiquadFilterNode() throws Exception {
        test("EventTarget", "BiquadFilterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_CDATASection() throws Exception {
        test("EventTarget", "CDATASection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_ChannelMergerNode() throws Exception {
        test("EventTarget", "ChannelMergerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_ChannelSplitterNode() throws Exception {
        test("EventTarget", "ChannelSplitterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_CharacterData() throws Exception {
        test("EventTarget", "CharacterData");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_Comment() throws Exception {
        test("EventTarget", "Comment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_ConvolverNode() throws Exception {
        test("EventTarget", "ConvolverNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_DelayNode() throws Exception {
        test("EventTarget", "DelayNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_Document() throws Exception {
        test("EventTarget", "Document");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_DocumentFragment() throws Exception {
        test("EventTarget", "DocumentFragment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_DocumentType() throws Exception {
        test("EventTarget", "DocumentType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_DynamicsCompressorNode() throws Exception {
        test("EventTarget", "DynamicsCompressorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_Element() throws Exception {
        test("EventTarget", "Element");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_EventSource() throws Exception {
        test("EventTarget", "EventSource");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_EventTarget() throws Exception {
        test("EventTarget", "EventTarget");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_GainNode() throws Exception {
        test("EventTarget", "GainNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLAnchorElement() throws Exception {
        test("EventTarget", "HTMLAnchorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLAppletElement() throws Exception {
        test("EventTarget", "HTMLAppletElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLAreaElement() throws Exception {
        test("EventTarget", "HTMLAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLAudioElement() throws Exception {
        test("EventTarget", "HTMLAudioElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLBRElement() throws Exception {
        test("EventTarget", "HTMLBRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLBaseElement() throws Exception {
        test("EventTarget", "HTMLBaseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLBodyElement() throws Exception {
        test("EventTarget", "HTMLBodyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLButtonElement() throws Exception {
        test("EventTarget", "HTMLButtonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLCanvasElement() throws Exception {
        test("EventTarget", "HTMLCanvasElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLContentElement() throws Exception {
        test("EventTarget", "HTMLContentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLDListElement() throws Exception {
        test("EventTarget", "HTMLDListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLDataListElement() throws Exception {
        test("EventTarget", "HTMLDataListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_HTMLDetailsElement() throws Exception {
        test("EventTarget", "HTMLDetailsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_HTMLDialogElement() throws Exception {
        test("EventTarget", "HTMLDialogElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLDirectoryElement() throws Exception {
        test("EventTarget", "HTMLDirectoryElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLDivElement() throws Exception {
        test("EventTarget", "HTMLDivElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLDocument() throws Exception {
        test("EventTarget", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLElement() throws Exception {
        test("EventTarget", "HTMLElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLEmbedElement() throws Exception {
        test("EventTarget", "HTMLEmbedElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLFieldSetElement() throws Exception {
        test("EventTarget", "HTMLFieldSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLFontElement() throws Exception {
        test("EventTarget", "HTMLFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLFormElement() throws Exception {
        test("EventTarget", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLFrameElement() throws Exception {
        test("EventTarget", "HTMLFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLFrameSetElement() throws Exception {
        test("EventTarget", "HTMLFrameSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLHRElement() throws Exception {
        test("EventTarget", "HTMLHRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLHeadElement() throws Exception {
        test("EventTarget", "HTMLHeadElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLHeadingElement() throws Exception {
        test("EventTarget", "HTMLHeadingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLHtmlElement() throws Exception {
        test("EventTarget", "HTMLHtmlElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLIFrameElement() throws Exception {
        test("EventTarget", "HTMLIFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLImageElement() throws Exception {
        test("EventTarget", "HTMLImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLInputElement() throws Exception {
        test("EventTarget", "HTMLInputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_HTMLKeygenElement() throws Exception {
        test("EventTarget", "HTMLKeygenElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLLIElement() throws Exception {
        test("EventTarget", "HTMLLIElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLLabelElement() throws Exception {
        test("EventTarget", "HTMLLabelElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLLegendElement() throws Exception {
        test("EventTarget", "HTMLLegendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLLinkElement() throws Exception {
        test("EventTarget", "HTMLLinkElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLMapElement() throws Exception {
        test("EventTarget", "HTMLMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_HTMLMarqueeElement() throws Exception {
        test("EventTarget", "HTMLMarqueeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLMediaElement() throws Exception {
        test("EventTarget", "HTMLMediaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLMenuElement() throws Exception {
        test("EventTarget", "HTMLMenuElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLMetaElement() throws Exception {
        test("EventTarget", "HTMLMetaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLMeterElement() throws Exception {
        test("EventTarget", "HTMLMeterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLModElement() throws Exception {
        test("EventTarget", "HTMLModElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLOListElement() throws Exception {
        test("EventTarget", "HTMLOListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLObjectElement() throws Exception {
        test("EventTarget", "HTMLObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLOptGroupElement() throws Exception {
        test("EventTarget", "HTMLOptGroupElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLOptionElement() throws Exception {
        test("EventTarget", "HTMLOptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLOutputElement() throws Exception {
        test("EventTarget", "HTMLOutputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLParagraphElement() throws Exception {
        test("EventTarget", "HTMLParagraphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLParamElement() throws Exception {
        test("EventTarget", "HTMLParamElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLPreElement() throws Exception {
        test("EventTarget", "HTMLPreElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLProgressElement() throws Exception {
        test("EventTarget", "HTMLProgressElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLQuoteElement() throws Exception {
        test("EventTarget", "HTMLQuoteElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLScriptElement() throws Exception {
        test("EventTarget", "HTMLScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLSelectElement() throws Exception {
        test("EventTarget", "HTMLSelectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLShadowElement() throws Exception {
        test("EventTarget", "HTMLShadowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLSourceElement() throws Exception {
        test("EventTarget", "HTMLSourceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLSpanElement() throws Exception {
        test("EventTarget", "HTMLSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLStyleElement() throws Exception {
        test("EventTarget", "HTMLStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLTableCaptionElement() throws Exception {
        test("EventTarget", "HTMLTableCaptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLTableCellElement() throws Exception {
        test("EventTarget", "HTMLTableCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLTableColElement() throws Exception {
        test("EventTarget", "HTMLTableColElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLTableElement() throws Exception {
        test("EventTarget", "HTMLTableElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLTableRowElement() throws Exception {
        test("EventTarget", "HTMLTableRowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLTableSectionElement() throws Exception {
        test("EventTarget", "HTMLTableSectionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLTextAreaElement() throws Exception {
        test("EventTarget", "HTMLTextAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLTitleElement() throws Exception {
        test("EventTarget", "HTMLTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLTrackElement() throws Exception {
        test("EventTarget", "HTMLTrackElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLUListElement() throws Exception {
        test("EventTarget", "HTMLUListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLUnknownElement() throws Exception {
        test("EventTarget", "HTMLUnknownElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLVideoElement() throws Exception {
        test("EventTarget", "HTMLVideoElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_Image() throws Exception {
        test("EventTarget", "Image");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_MediaElementAudioSourceNode() throws Exception {
        test("EventTarget", "MediaElementAudioSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_MediaStreamAudioDestinationNode() throws Exception {
        test("EventTarget", "MediaStreamAudioDestinationNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_MediaStreamAudioSourceNode() throws Exception {
        test("EventTarget", "MediaStreamAudioSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_MediaStreamTrack() throws Exception {
        test("EventTarget", "MediaStreamTrack");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _EventTarget_PannerNode() throws Exception {
        test("EventTarget", "PannerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_MessagePort() throws Exception {
        test("EventTarget", "MessagePort");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_Node() throws Exception {
        test("EventTarget", "Node");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_Notification() throws Exception {
        test("EventTarget", "Notification");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_OfflineAudioContext() throws Exception {
        test("EventTarget", "OfflineAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_Option() throws Exception {
        test("EventTarget", "Option");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_OscillatorNode() throws Exception {
        test("EventTarget", "OscillatorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_ProcessingInstruction() throws Exception {
        test("EventTarget", "ProcessingInstruction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGAElement() throws Exception {
        test("EventTarget", "SVGAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGAnimateElement() throws Exception {
        test("EventTarget", "SVGAnimateElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGAnimateMotionElement() throws Exception {
        test("EventTarget", "SVGAnimateMotionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGAnimateTransformElement() throws Exception {
        test("EventTarget", "SVGAnimateTransformElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGAnimationElement() throws Exception {
        test("EventTarget", "SVGAnimationElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGCircleElement() throws Exception {
        test("EventTarget", "SVGCircleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGClipPathElement() throws Exception {
        test("EventTarget", "SVGClipPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_SVGCursorElement() throws Exception {
        test("EventTarget", "SVGCursorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGDefsElement() throws Exception {
        test("EventTarget", "SVGDefsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGDescElement() throws Exception {
        test("EventTarget", "SVGDescElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGElement() throws Exception {
        test("EventTarget", "SVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGEllipseElement() throws Exception {
        test("EventTarget", "SVGEllipseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFEBlendElement() throws Exception {
        test("EventTarget", "SVGFEBlendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFEColorMatrixElement() throws Exception {
        test("EventTarget", "SVGFEColorMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFEComponentTransferElement() throws Exception {
        test("EventTarget", "SVGFEComponentTransferElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFECompositeElement() throws Exception {
        test("EventTarget", "SVGFECompositeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFEConvolveMatrixElement() throws Exception {
        test("EventTarget", "SVGFEConvolveMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFEDiffuseLightingElement() throws Exception {
        test("EventTarget", "SVGFEDiffuseLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFEDisplacementMapElement() throws Exception {
        test("EventTarget", "SVGFEDisplacementMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFEDistantLightElement() throws Exception {
        test("EventTarget", "SVGFEDistantLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFEFloodElement() throws Exception {
        test("EventTarget", "SVGFEFloodElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFEFuncAElement() throws Exception {
        test("EventTarget", "SVGFEFuncAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFEFuncBElement() throws Exception {
        test("EventTarget", "SVGFEFuncBElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFEFuncGElement() throws Exception {
        test("EventTarget", "SVGFEFuncGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFEFuncRElement() throws Exception {
        test("EventTarget", "SVGFEFuncRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFEGaussianBlurElement() throws Exception {
        test("EventTarget", "SVGFEGaussianBlurElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFEImageElement() throws Exception {
        test("EventTarget", "SVGFEImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFEMergeElement() throws Exception {
        test("EventTarget", "SVGFEMergeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFEMergeNodeElement() throws Exception {
        test("EventTarget", "SVGFEMergeNodeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFEMorphologyElement() throws Exception {
        test("EventTarget", "SVGFEMorphologyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFEOffsetElement() throws Exception {
        test("EventTarget", "SVGFEOffsetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFEPointLightElement() throws Exception {
        test("EventTarget", "SVGFEPointLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFESpecularLightingElement() throws Exception {
        test("EventTarget", "SVGFESpecularLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFESpotLightElement() throws Exception {
        test("EventTarget", "SVGFESpotLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFETileElement() throws Exception {
        test("EventTarget", "SVGFETileElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFETurbulenceElement() throws Exception {
        test("EventTarget", "SVGFETurbulenceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFilterElement() throws Exception {
        test("EventTarget", "SVGFilterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGForeignObjectElement() throws Exception {
        test("EventTarget", "SVGForeignObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGGElement() throws Exception {
        test("EventTarget", "SVGGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGGradientElement() throws Exception {
        test("EventTarget", "SVGGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGImageElement() throws Exception {
        test("EventTarget", "SVGImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGLineElement() throws Exception {
        test("EventTarget", "SVGLineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGLinearGradientElement() throws Exception {
        test("EventTarget", "SVGLinearGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGMPathElement() throws Exception {
        test("EventTarget", "SVGMPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGMarkerElement() throws Exception {
        test("EventTarget", "SVGMarkerElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGMaskElement() throws Exception {
        test("EventTarget", "SVGMaskElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGMetadataElement() throws Exception {
        test("EventTarget", "SVGMetadataElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGPathElement() throws Exception {
        test("EventTarget", "SVGPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGPatternElement() throws Exception {
        test("EventTarget", "SVGPatternElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGPolygonElement() throws Exception {
        test("EventTarget", "SVGPolygonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGPolylineElement() throws Exception {
        test("EventTarget", "SVGPolylineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGRadialGradientElement() throws Exception {
        test("EventTarget", "SVGRadialGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGRectElement() throws Exception {
        test("EventTarget", "SVGRectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGSVGElement() throws Exception {
        test("EventTarget", "SVGSVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGScriptElement() throws Exception {
        test("EventTarget", "SVGScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGSetElement() throws Exception {
        test("EventTarget", "SVGSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGStopElement() throws Exception {
        test("EventTarget", "SVGStopElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGStyleElement() throws Exception {
        test("EventTarget", "SVGStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGSwitchElement() throws Exception {
        test("EventTarget", "SVGSwitchElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGSymbolElement() throws Exception {
        test("EventTarget", "SVGSymbolElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGTSpanElement() throws Exception {
        test("EventTarget", "SVGTSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGTextElement() throws Exception {
        test("EventTarget", "SVGTextElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGTextPathElement() throws Exception {
        test("EventTarget", "SVGTextPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGTextPositioningElement() throws Exception {
        test("EventTarget", "SVGTextPositioningElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGTitleElement() throws Exception {
        test("EventTarget", "SVGTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGUseElement() throws Exception {
        test("EventTarget", "SVGUseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGViewElement() throws Exception {
        test("EventTarget", "SVGViewElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_ScriptProcessorNode() throws Exception {
        test("EventTarget", "ScriptProcessorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_ShadowRoot() throws Exception {
        test("EventTarget", "ShadowRoot");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SharedWorker() throws Exception {
        test("EventTarget", "SharedWorker");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_Text() throws Exception {
        test("EventTarget", "Text");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_WaveShaperNode() throws Exception {
        test("EventTarget", "WaveShaperNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_WebSocket() throws Exception {
        test("EventTarget", "WebSocket");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    @NotYetImplemented({ CHROME, FF })
    public void _EventTarget_Window() throws Exception {
        test("EventTarget", "Window");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_Worker() throws Exception {
        test("EventTarget", "Worker");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_XMLDocument() throws Exception {
        test("EventTarget", "XMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_XMLHttpRequest() throws Exception {
        test("EventTarget", "XMLHttpRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Event_ClipboardEvent() throws Exception {
        test("Event", "ClipboardEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Event_CloseEvent() throws Exception {
        test("Event", "CloseEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Event_CompositionEvent() throws Exception {
        test("Event", "CompositionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Event_CustomEvent() throws Exception {
        test("Event", "CustomEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Event_DeviceMotionEvent() throws Exception {
        test("Event", "DeviceMotionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Event_DeviceOrientationEvent() throws Exception {
        test("Event", "DeviceOrientationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Event_ErrorEvent() throws Exception {
        test("Event", "ErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Event_FocusEvent() throws Exception {
        test("Event", "FocusEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Event_GamepadEvent() throws Exception {
        test("Event", "GamepadEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Event_PageTransitionEvent() throws Exception {
        test("Event", "PageTransitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Event_PopStateEvent() throws Exception {
        test("Event", "PopStateEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Event_ProgressEvent() throws Exception {
        test("Event", "ProgressEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Event_StorageEvent() throws Exception {
        test("Event", "StorageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Event_TouchEvent() throws Exception {
        test("Event", "TouchEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Event_TransitionEvent() throws Exception {
        test("Event", "TransitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Event_WheelEvent() throws Exception {
        test("Event", "WheelEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _FocusEvent_FocusEvent() throws Exception {
        test("FocusEvent", "FocusEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _GamepadEvent_GamepadEvent() throws Exception {
        test("GamepadEvent", "GamepadEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MediaElementAudioSourceNode_MediaElementAudioSourceNode() throws Exception {
        test("MediaElementAudioSourceNode", "MediaElementAudioSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MediaStreamTrack_MediaStreamTrack() throws Exception {
        test("MediaStreamTrack", "MediaStreamTrack");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _MediaStream_MediaStream() throws Exception {
        test("MediaStream", "MediaStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MouseEvent_WheelEvent() throws Exception {
        test("MouseEvent", "WheelEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _PageTransitionEvent_PageTransitionEvent() throws Exception {
        test("PageTransitionEvent", "PageTransitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _PopStateEvent_PopStateEvent() throws Exception {
        test("PopStateEvent", "PopStateEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _ProgressEvent_ProgressEvent() throws Exception {
        test("ProgressEvent", "ProgressEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _StorageEvent_StorageEvent() throws Exception {
        test("StorageEvent", "StorageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _TouchEvent_TouchEvent() throws Exception {
        test("TouchEvent", "TouchEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _TouchList_TouchList() throws Exception {
        test("TouchList", "TouchList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Touch_Touch() throws Exception {
        test("Touch", "Touch");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _TransitionEvent_TransitionEvent() throws Exception {
        test("TransitionEvent", "TransitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _UIEvent_CompositionEvent() throws Exception {
        test("UIEvent", "CompositionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _UIEvent_FocusEvent() throws Exception {
        test("UIEvent", "FocusEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _UIEvent_TouchEvent() throws Exception {
        test("UIEvent", "TouchEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _UIEvent_WheelEvent() throws Exception {
        test("UIEvent", "WheelEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _WheelEvent_WheelEvent() throws Exception {
        test("WheelEvent", "WheelEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true",
            IE11 = "true")
    public void _AnimationEvent_AnimationEvent() throws Exception {
        test("AnimationEvent", "AnimationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _CSSConditionRule_CSSConditionRule() throws Exception {
        test("CSSConditionRule", "CSSConditionRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _CSSConditionRule_CSSMediaRule() throws Exception {
        test("CSSConditionRule", "CSSMediaRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _CSSConditionRule_CSSSupportsRule() throws Exception {
        test("CSSConditionRule", "CSSSupportsRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _CSSGroupingRule_CSSConditionRule() throws Exception {
        test("CSSGroupingRule", "CSSConditionRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _CSSGroupingRule_CSSGroupingRule() throws Exception {
        test("CSSGroupingRule", "CSSGroupingRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _CSSGroupingRule_CSSMediaRule() throws Exception {
        test("CSSGroupingRule", "CSSMediaRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _CSSGroupingRule_CSSSupportsRule() throws Exception {
        test("CSSGroupingRule", "CSSSupportsRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _CSSRule_CSSConditionRule() throws Exception {
        test("CSSRule", "CSSConditionRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _CSSRule_CSSGroupingRule() throws Exception {
        test("CSSRule", "CSSGroupingRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _DeviceLightEvent_DeviceLightEvent() throws Exception {
        test("DeviceLightEvent", "DeviceLightEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _DeviceStorageChangeEvent_DeviceStorageChangeEvent() throws Exception {
        test("DeviceStorageChangeEvent", "DeviceStorageChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true",
            IE11 = "true")
    public void _DragEvent_DragEvent() throws Exception {
        test("DragEvent", "DragEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true",
            IE11 = "true")
    public void _Event_AnimationEvent() throws Exception {
        test("Event", "AnimationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _Event_DeviceLightEvent() throws Exception {
        test("Event", "DeviceLightEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _Event_DeviceStorageChangeEvent() throws Exception {
        test("Event", "DeviceStorageChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true",
            IE11 = "true")
    public void _Event_DragEvent() throws Exception {
        test("Event", "DragEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _Event_MouseScrollEvent() throws Exception {
        test("Event", "MouseScrollEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _Event_TimeEvent() throws Exception {
        test("Event", "TimeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _Event_UserProximityEvent() throws Exception {
        test("Event", "UserProximityEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true",
            IE11 = "true")
    public void _MouseEvent_DragEvent() throws Exception {
        test("MouseEvent", "DragEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _MouseEvent_MouseScrollEvent() throws Exception {
        test("MouseEvent", "MouseScrollEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _MouseScrollEvent_MouseScrollEvent() throws Exception {
        test("MouseScrollEvent", "MouseScrollEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _TimeEvent_TimeEvent() throws Exception {
        test("TimeEvent", "TimeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true",
            IE11 = "true")
    public void _UIEvent_DragEvent() throws Exception {
        test("UIEvent", "DragEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _UIEvent_MouseScrollEvent() throws Exception {
        test("UIEvent", "MouseScrollEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _UserProximityEvent_UserProximityEvent() throws Exception {
        test("UserProximityEvent", "UserProximityEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _CSSNamespaceRule_CSSNamespaceRule() throws Exception {
        test("CSSNamespaceRule", "CSSNamespaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _CSSRule_CSSNamespaceRule() throws Exception {
        test("CSSRule", "CSSNamespaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Event_MouseWheelEvent() throws Exception {
        test("Event", "MouseWheelEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _MouseEvent_MouseWheelEvent() throws Exception {
        test("MouseEvent", "MouseWheelEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _MouseWheelEvent_MouseWheelEvent() throws Exception {
        test("MouseWheelEvent", "MouseWheelEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _UIEvent_MouseWheelEvent() throws Exception {
        test("UIEvent", "MouseWheelEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_SVGAnimationElement() throws Exception {
        test("Node", "SVGAnimationElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Element_SVGAnimationElement() throws Exception {
        test("Element", "SVGAnimationElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGElement_SVGAnimationElement() throws Exception {
        test("SVGElement", "SVGAnimationElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_SVGTextPositioningElement() throws Exception {
        test("Node", "SVGTextPositioningElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGTextPositioningElement() throws Exception {
        test("Element", "SVGTextPositioningElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGTextPositioningElement() throws Exception {
        test("SVGElement", "SVGTextPositioningElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _Event_DeviceProximityEvent() throws Exception {
        test("Event", "DeviceProximityEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _DeviceProximityEvent_DeviceProximityEvent() throws Exception {
        test("DeviceProximityEvent", "DeviceProximityEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _Event_InputEvent() throws Exception {
        test("Event", "InputEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF31 = "true")
    public void _InputEvent_InputEvent() throws Exception {
        test("InputEvent", "InputEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Element_HTMLPictureElement() throws Exception {
        test("Element", "HTMLPictureElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_HTMLPictureElement() throws Exception {
        test("EventTarget", "HTMLPictureElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _HTMLElement_HTMLPictureElement() throws Exception {
        test("HTMLElement", "HTMLPictureElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _HTMLPictureElement_HTMLPictureElement() throws Exception {
        test("HTMLPictureElement", "HTMLPictureElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Node_HTMLPictureElement() throws Exception {
        test("Node", "HTMLPictureElement");
    }
}
