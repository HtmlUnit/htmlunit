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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.*;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE11;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import java.lang.reflect.Field;
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
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;

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
        final List<String> strings = getAllClassNames();
        for (final String parent : strings) {
            for (final String child : strings) {
                list.add(new Object[] {parent, child});
            }
        }
        return list;
    }

    private static List<String> getAllClassNames() throws Exception {
        final Field field = JavaScriptConfiguration.class.getDeclaredField("CLASSES_");
        field.setAccessible(true);

        final List<String> list = new ArrayList<>();
        for (final Class<?> c : (Class<?>[]) field.get(null)) {
            final String name = c.getSimpleName();
            list.add(name);
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
    @Alerts("false")
    public void _ArrayBufferView_ArrayBufferView() throws Exception {
        test("ArrayBufferView", "ArrayBufferView");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferViewBase_ArrayBufferViewBase() throws Exception {
        test("ArrayBufferViewBase", "ArrayBufferViewBase");
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
    @Alerts("false")
    public void _BoxObject_BoxObject() throws Exception {
        test("BoxObject", "BoxObject");
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
    @Alerts("false")
    public void _ClipboardData_ClipboardData() throws Exception {
        test("ClipboardData", "ClipboardData");
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
    @Alerts("false")
    public void _CharacterDataImpl_CharacterDataImpl() throws Exception {
        test("CharacterDataImpl", "CharacterDataImpl");
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
    @Alerts("false")
    public void _ComputedCSSStyleDeclaration_ComputedCSSStyleDeclaration() throws Exception {
        test("ComputedCSSStyleDeclaration", "ComputedCSSStyleDeclaration");
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
    @Alerts("false")
    public void _EventNode_EventNode() throws Exception {
        test("EventNode", "EventNode");
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
    @Alerts("false")
    public void _FormChild_FormChild() throws Exception {
        test("FormChild", "FormChild");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _FormField_FormField() throws Exception {
        test("FormField", "FormField");
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
    @Alerts("false")
    public void _HTMLInlineQuotationElement_HTMLInlineQuotationElement() throws Exception {
        test("HTMLInlineQuotationElement", "HTMLInlineQuotationElement");
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
    @Alerts("false")
    public void _HTMLListElement_HTMLListElement() throws Exception {
        test("HTMLListElement", "HTMLListElement");
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
    @Alerts("false")
    public void _HTMLTableComponent_HTMLTableComponent() throws Exception {
        test("HTMLTableComponent", "HTMLTableComponent");
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
    @Alerts("false")
    public void _Namespace_Namespace() throws Exception {
        test("Namespace", "Namespace");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _NamespaceCollection_NamespaceCollection() throws Exception {
        test("NamespaceCollection", "NamespaceCollection");
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
    @NotYetImplemented(FF)
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
    @Alerts("false")
    public void _Popup_Popup() throws Exception {
        test("Popup", "Popup");
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
    @Alerts("false")
    public void _RowContainer_RowContainer() throws Exception {
        test("RowContainer", "RowContainer");
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
    @Alerts("false")
    public void _SimpleArray_SimpleArray() throws Exception {
        test("SimpleArray", "SimpleArray");
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
    @Alerts("false")
    public void _XSLTemplate_XSLTemplate() throws Exception {
        test("XSLTemplate", "XSLTemplate");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferView_ArrayBufferViewBase() throws Exception {
        test("ArrayBufferView", "ArrayBufferViewBase");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferView_DataView() throws Exception {
        test("ArrayBufferView", "DataView");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferView_Float32Array() throws Exception {
        test("ArrayBufferView", "Float32Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferView_Float64Array() throws Exception {
        test("ArrayBufferView", "Float64Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferView_Int16Array() throws Exception {
        test("ArrayBufferView", "Int16Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferView_Int32Array() throws Exception {
        test("ArrayBufferView", "Int32Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferView_Int8Array() throws Exception {
        test("ArrayBufferView", "Int8Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferView_Uint16Array() throws Exception {
        test("ArrayBufferView", "Uint16Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferView_Uint32Array() throws Exception {
        test("ArrayBufferView", "Uint32Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferView_Uint8Array() throws Exception {
        test("ArrayBufferView", "Uint8Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferView_Uint8ClampedArray() throws Exception {
        test("ArrayBufferView", "Uint8ClampedArray");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferViewBase_Float32Array() throws Exception {
        test("ArrayBufferViewBase", "Float32Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferViewBase_Float64Array() throws Exception {
        test("ArrayBufferViewBase", "Float64Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferViewBase_Int16Array() throws Exception {
        test("ArrayBufferViewBase", "Int16Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferViewBase_Int32Array() throws Exception {
        test("ArrayBufferViewBase", "Int32Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferViewBase_Int8Array() throws Exception {
        test("ArrayBufferViewBase", "Int8Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferViewBase_Uint16Array() throws Exception {
        test("ArrayBufferViewBase", "Uint16Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferViewBase_Uint32Array() throws Exception {
        test("ArrayBufferViewBase", "Uint32Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferViewBase_Uint8Array() throws Exception {
        test("ArrayBufferViewBase", "Uint8Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferViewBase_Uint8ClampedArray() throws Exception {
        test("ArrayBufferViewBase", "Uint8ClampedArray");
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
    @Alerts("false")
    public void _CSSStyleDeclaration_ComputedCSSStyleDeclaration() throws Exception {
        test("CSSStyleDeclaration", "ComputedCSSStyleDeclaration");
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
    @Alerts("false")
    public void _CharacterDataImpl_CDATASection() throws Exception {
        test("CharacterDataImpl", "CDATASection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CharacterDataImpl_Comment() throws Exception {
        test("CharacterDataImpl", "Comment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CharacterDataImpl_HTMLCommentElement() throws Exception {
        test("CharacterDataImpl", "HTMLCommentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CharacterDataImpl_Text() throws Exception {
        test("CharacterDataImpl", "Text");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Comment_HTMLCommentElement() throws Exception {
        test("Comment", "HTMLCommentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ComputedCSSStyleDeclaration_CSS2Properties() throws Exception {
        test("ComputedCSSStyleDeclaration", "CSS2Properties");
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
    @NotYetImplemented
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
    @Alerts("false")
    public void _Element_HTMLInlineQuotationElement() throws Exception {
        test("Element", "HTMLInlineQuotationElement");
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
    @Alerts("false")
    public void _Element_HTMLListElement() throws Exception {
        test("Element", "HTMLListElement");
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
    @Alerts("false")
    public void _Element_HTMLTableComponent() throws Exception {
        test("Element", "HTMLTableComponent");
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
    @Alerts("false")
    public void _Element_RowContainer() throws Exception {
        test("Element", "RowContainer");
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
    @NotYetImplemented({ CHROME, IE11 })
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
    @NotYetImplemented({ CHROME, FF, IE11 })
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
    @Alerts("false")
    public void _HTMLElement_HTMLInlineQuotationElement() throws Exception {
        test("HTMLElement", "HTMLInlineQuotationElement");
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
    @Alerts("false")
    public void _HTMLElement_HTMLListElement() throws Exception {
        test("HTMLElement", "HTMLListElement");
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
    @Alerts("false")
    public void _HTMLElement_HTMLNoShowElement() throws Exception {
        test("HTMLElement", "HTMLNoShowElement");
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
    @Alerts("false")
    public void _HTMLElement_HTMLTableComponent() throws Exception {
        test("HTMLElement", "HTMLTableComponent");
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
    @Alerts("false")
    public void _HTMLElement_HTMLTextElement() throws Exception {
        test("HTMLElement", "HTMLTextElement");
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
    @Alerts("false")
    public void _HTMLElement_RowContainer() throws Exception {
        test("HTMLElement", "RowContainer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLListElement_HTMLDListElement() throws Exception {
        test("HTMLListElement", "HTMLDListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLListElement_HTMLDirectoryElement() throws Exception {
        test("HTMLListElement", "HTMLDirectoryElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLListElement_HTMLMenuElement() throws Exception {
        test("HTMLListElement", "HTMLMenuElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLListElement_HTMLOListElement() throws Exception {
        test("HTMLListElement", "HTMLOListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLListElement_HTMLUListElement() throws Exception {
        test("HTMLListElement", "HTMLUListElement");
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
    @Alerts("false")
    public void _HTMLTableComponent_HTMLTableCellElement() throws Exception {
        test("HTMLTableComponent", "HTMLTableCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLTableComponent_HTMLTableColElement() throws Exception {
        test("HTMLTableComponent", "HTMLTableColElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLTableComponent_HTMLTableDataCellElement() throws Exception {
        test("HTMLTableComponent", "HTMLTableDataCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLTableComponent_HTMLTableHeaderCellElement() throws Exception {
        test("HTMLTableComponent", "HTMLTableHeaderCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLTableComponent_HTMLTableRowElement() throws Exception {
        test("HTMLTableComponent", "HTMLTableRowElement");
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
    @Alerts("false")
    public void _Node_CharacterDataImpl() throws Exception {
        test("Node", "CharacterDataImpl");
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
    @Alerts("false")
    public void _Node_FormField() throws Exception {
        test("Node", "FormField");
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
    @Alerts("false")
    public void _Node_HTMLCommentElement() throws Exception {
        test("Node", "HTMLCommentElement");
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
    @NotYetImplemented({ CHROME, FF, IE11 })
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
    @Alerts("false")
    public void _Node_HTMLInlineQuotationElement() throws Exception {
        test("Node", "HTMLInlineQuotationElement");
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
    @Alerts("false")
    public void _Node_HTMLListElement() throws Exception {
        test("Node", "HTMLListElement");
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
    @Alerts("false")
    public void _Node_HTMLNoShowElement() throws Exception {
        test("Node", "HTMLNoShowElement");
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
    @Alerts("false")
    public void _Node_HTMLTableComponent() throws Exception {
        test("Node", "HTMLTableComponent");
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
    @Alerts("false")
    public void _Node_HTMLTextElement() throws Exception {
        test("Node", "HTMLTextElement");
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
    @Alerts("false")
    public void _Node_RowContainer() throws Exception {
        test("Node", "RowContainer");
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
    @Alerts("false")
    public void _RowContainer_HTMLTableElement() throws Exception {
        test("RowContainer", "HTMLTableElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _RowContainer_HTMLTableSectionElement() throws Exception {
        test("RowContainer", "HTMLTableSectionElement");
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
    @Alerts("false")
    public void _SimpleArray_MimeTypeArray() throws Exception {
        test("SimpleArray", "MimeTypeArray");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SimpleArray_Plugin() throws Exception {
        test("SimpleArray", "Plugin");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SimpleArray_PluginArray() throws Exception {
        test("SimpleArray", "PluginArray");
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
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _ArrayBuffer_HTMLDocument() throws Exception {
        test("ArrayBuffer", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _ArrayBuffer_HTMLFormElement() throws Exception {
        test("ArrayBuffer", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _ArrayBuffer_HTMLOptionsCollection() throws Exception {
        test("ArrayBuffer", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Attr_HTMLDocument() throws Exception {
        test("Attr", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _Attr_HTMLFormElement() throws Exception {
        test("Attr", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Attr_HTMLOptionsCollection() throws Exception {
        test("Attr", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _BeforeUnloadEvent_HTMLDocument() throws Exception {
        test("BeforeUnloadEvent", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _BeforeUnloadEvent_HTMLFormElement() throws Exception {
        test("BeforeUnloadEvent", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _BeforeUnloadEvent_HTMLOptionsCollection() throws Exception {
        test("BeforeUnloadEvent", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CDATASection_HTMLDocument() throws Exception {
        test("CDATASection", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CDATASection_HTMLFormElement() throws Exception {
        test("CDATASection", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CDATASection_HTMLOptionsCollection() throws Exception {
        test("CDATASection", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _CSS2Properties_HTMLDocument() throws Exception {
        test("CSS2Properties", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _CSS2Properties_HTMLFormElement() throws Exception {
        test("CSS2Properties", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _CSS2Properties_HTMLOptionsCollection() throws Exception {
        test("CSS2Properties", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _CSSCharsetRule_HTMLDocument() throws Exception {
        test("CSSCharsetRule", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _CSSCharsetRule_HTMLFormElement() throws Exception {
        test("CSSCharsetRule", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _CSSCharsetRule_HTMLOptionsCollection() throws Exception {
        test("CSSCharsetRule", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CSSFontFaceRule_HTMLDocument() throws Exception {
        test("CSSFontFaceRule", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CSSFontFaceRule_HTMLFormElement() throws Exception {
        test("CSSFontFaceRule", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CSSFontFaceRule_HTMLOptionsCollection() throws Exception {
        test("CSSFontFaceRule", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CSSImportRule_HTMLDocument() throws Exception {
        test("CSSImportRule", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CSSImportRule_HTMLFormElement() throws Exception {
        test("CSSImportRule", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CSSImportRule_HTMLOptionsCollection() throws Exception {
        test("CSSImportRule", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CSSMediaRule_HTMLDocument() throws Exception {
        test("CSSMediaRule", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CSSMediaRule_HTMLFormElement() throws Exception {
        test("CSSMediaRule", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CSSMediaRule_HTMLOptionsCollection() throws Exception {
        test("CSSMediaRule", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _CSSPrimitiveValue_HTMLDocument() throws Exception {
        test("CSSPrimitiveValue", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _CSSPrimitiveValue_HTMLFormElement() throws Exception {
        test("CSSPrimitiveValue", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _CSSPrimitiveValue_HTMLOptionsCollection() throws Exception {
        test("CSSPrimitiveValue", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CSSRuleList_HTMLDocument() throws Exception {
        test("CSSRuleList", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _CSSRuleList_HTMLFormElement() throws Exception {
        test("CSSRuleList", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CSSRuleList_HTMLOptionsCollection() throws Exception {
        test("CSSRuleList", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CSSRule_HTMLDocument() throws Exception {
        test("CSSRule", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CSSRule_HTMLFormElement() throws Exception {
        test("CSSRule", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CSSRule_HTMLOptionsCollection() throws Exception {
        test("CSSRule", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CSSStyleDeclaration_HTMLDocument() throws Exception {
        test("CSSStyleDeclaration", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _CSSStyleDeclaration_HTMLFormElement() throws Exception {
        test("CSSStyleDeclaration", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CSSStyleDeclaration_HTMLOptionsCollection() throws Exception {
        test("CSSStyleDeclaration", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CSSStyleRule_HTMLDocument() throws Exception {
        test("CSSStyleRule", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _CSSStyleRule_HTMLFormElement() throws Exception {
        test("CSSStyleRule", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CSSStyleRule_HTMLOptionsCollection() throws Exception {
        test("CSSStyleRule", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CSSStyleSheet_HTMLDocument() throws Exception {
        test("CSSStyleSheet", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _CSSStyleSheet_HTMLFormElement() throws Exception {
        test("CSSStyleSheet", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CSSStyleSheet_HTMLOptionsCollection() throws Exception {
        test("CSSStyleSheet", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _CSSValue_HTMLDocument() throws Exception {
        test("CSSValue", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _CSSValue_HTMLFormElement() throws Exception {
        test("CSSValue", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _CSSValue_HTMLOptionsCollection() throws Exception {
        test("CSSValue", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CanvasRenderingContext2D_HTMLDocument() throws Exception {
        test("CanvasRenderingContext2D", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CanvasRenderingContext2D_HTMLFormElement() throws Exception {
        test("CanvasRenderingContext2D", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _CanvasRenderingContext2D_HTMLOptionsCollection() throws Exception {
        test("CanvasRenderingContext2D", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Comment_HTMLDocument() throws Exception {
        test("Comment", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Comment_HTMLFormElement() throws Exception {
        test("Comment", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Comment_HTMLOptionsCollection() throws Exception {
        test("Comment", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMException_HTMLDocument() throws Exception {
        test("DOMException", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMException_HTMLFormElement() throws Exception {
        test("DOMException", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMException_HTMLOptionsCollection() throws Exception {
        test("DOMException", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMImplementation_HTMLDocument() throws Exception {
        test("DOMImplementation", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _DOMImplementation_HTMLFormElement() throws Exception {
        test("DOMImplementation", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMImplementation_HTMLOptionsCollection() throws Exception {
        test("DOMImplementation", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMParser_HTMLDocument() throws Exception {
        test("DOMParser", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMParser_HTMLFormElement() throws Exception {
        test("DOMParser", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMParser_HTMLOptionsCollection() throws Exception {
        test("DOMParser", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_ArrayBuffer() throws Exception {
        test("DOMStringMap", "ArrayBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Attr() throws Exception {
        test("DOMStringMap", "Attr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_BeforeUnloadEvent() throws Exception {
        test("DOMStringMap", "BeforeUnloadEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_CDATASection() throws Exception {
        test("DOMStringMap", "CDATASection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _DOMStringMap_CSS2Properties() throws Exception {
        test("DOMStringMap", "CSS2Properties");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _DOMStringMap_CSSCharsetRule() throws Exception {
        test("DOMStringMap", "CSSCharsetRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_CSSFontFaceRule() throws Exception {
        test("DOMStringMap", "CSSFontFaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_CSSImportRule() throws Exception {
        test("DOMStringMap", "CSSImportRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_CSSMediaRule() throws Exception {
        test("DOMStringMap", "CSSMediaRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _DOMStringMap_CSSPrimitiveValue() throws Exception {
        test("DOMStringMap", "CSSPrimitiveValue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_CSSRule() throws Exception {
        test("DOMStringMap", "CSSRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_CSSRuleList() throws Exception {
        test("DOMStringMap", "CSSRuleList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_CSSStyleDeclaration() throws Exception {
        test("DOMStringMap", "CSSStyleDeclaration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_CSSStyleRule() throws Exception {
        test("DOMStringMap", "CSSStyleRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_CSSStyleSheet() throws Exception {
        test("DOMStringMap", "CSSStyleSheet");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _DOMStringMap_CSSValue() throws Exception {
        test("DOMStringMap", "CSSValue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_CanvasRenderingContext2D() throws Exception {
        test("DOMStringMap", "CanvasRenderingContext2D");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Comment() throws Exception {
        test("DOMStringMap", "Comment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_DOMException() throws Exception {
        test("DOMStringMap", "DOMException");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_DOMImplementation() throws Exception {
        test("DOMStringMap", "DOMImplementation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_DOMParser() throws Exception {
        test("DOMStringMap", "DOMParser");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_DOMTokenList() throws Exception {
        test("DOMStringMap", "DOMTokenList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_DataView() throws Exception {
        test("DOMStringMap", "DataView");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Document() throws Exception {
        test("DOMStringMap", "Document");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_DocumentFragment() throws Exception {
        test("DOMStringMap", "DocumentFragment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_DocumentType() throws Exception {
        test("DOMStringMap", "DocumentType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Element() throws Exception {
        test("DOMStringMap", "Element");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Event() throws Exception {
        test("DOMStringMap", "Event");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _DOMStringMap_External() throws Exception {
        test("DOMStringMap", "External");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Float32Array() throws Exception {
        test("DOMStringMap", "Float32Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Float64Array() throws Exception {
        test("DOMStringMap", "Float64Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLAnchorElement() throws Exception {
        test("DOMStringMap", "HTMLAnchorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLAppletElement() throws Exception {
        test("DOMStringMap", "HTMLAppletElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLAreaElement() throws Exception {
        test("DOMStringMap", "HTMLAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLAudioElement() throws Exception {
        test("DOMStringMap", "HTMLAudioElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLBRElement() throws Exception {
        test("DOMStringMap", "HTMLBRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLBaseElement() throws Exception {
        test("DOMStringMap", "HTMLBaseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLBodyElement() throws Exception {
        test("DOMStringMap", "HTMLBodyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLButtonElement() throws Exception {
        test("DOMStringMap", "HTMLButtonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLCanvasElement() throws Exception {
        test("DOMStringMap", "HTMLCanvasElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLCollection() throws Exception {
        test("DOMStringMap", "HTMLCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLDListElement() throws Exception {
        test("DOMStringMap", "HTMLDListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLDataListElement() throws Exception {
        test("DOMStringMap", "HTMLDataListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLDirectoryElement() throws Exception {
        test("DOMStringMap", "HTMLDirectoryElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLDivElement() throws Exception {
        test("DOMStringMap", "HTMLDivElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLDocument() throws Exception {
        test("DOMStringMap", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLElement() throws Exception {
        test("DOMStringMap", "HTMLElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLEmbedElement() throws Exception {
        test("DOMStringMap", "HTMLEmbedElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLFieldSetElement() throws Exception {
        test("DOMStringMap", "HTMLFieldSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLFontElement() throws Exception {
        test("DOMStringMap", "HTMLFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLFormElement() throws Exception {
        test("DOMStringMap", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLFrameElement() throws Exception {
        test("DOMStringMap", "HTMLFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLFrameSetElement() throws Exception {
        test("DOMStringMap", "HTMLFrameSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLHRElement() throws Exception {
        test("DOMStringMap", "HTMLHRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLHeadElement() throws Exception {
        test("DOMStringMap", "HTMLHeadElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLHeadingElement() throws Exception {
        test("DOMStringMap", "HTMLHeadingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLHtmlElement() throws Exception {
        test("DOMStringMap", "HTMLHtmlElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLIFrameElement() throws Exception {
        test("DOMStringMap", "HTMLIFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLImageElement() throws Exception {
        test("DOMStringMap", "HTMLImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLInputElement() throws Exception {
        test("DOMStringMap", "HTMLInputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLLIElement() throws Exception {
        test("DOMStringMap", "HTMLLIElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLLabelElement() throws Exception {
        test("DOMStringMap", "HTMLLabelElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLLegendElement() throws Exception {
        test("DOMStringMap", "HTMLLegendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLLinkElement() throws Exception {
        test("DOMStringMap", "HTMLLinkElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLMapElement() throws Exception {
        test("DOMStringMap", "HTMLMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLMediaElement() throws Exception {
        test("DOMStringMap", "HTMLMediaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLMenuElement() throws Exception {
        test("DOMStringMap", "HTMLMenuElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _DOMStringMap_HTMLMenuItemElement() throws Exception {
        test("DOMStringMap", "HTMLMenuItemElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLMetaElement() throws Exception {
        test("DOMStringMap", "HTMLMetaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLMeterElement() throws Exception {
        test("DOMStringMap", "HTMLMeterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLModElement() throws Exception {
        test("DOMStringMap", "HTMLModElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLOListElement() throws Exception {
        test("DOMStringMap", "HTMLOListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLObjectElement() throws Exception {
        test("DOMStringMap", "HTMLObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLOptGroupElement() throws Exception {
        test("DOMStringMap", "HTMLOptGroupElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLOptionElement() throws Exception {
        test("DOMStringMap", "HTMLOptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLOptionsCollection() throws Exception {
        test("DOMStringMap", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLOutputElement() throws Exception {
        test("DOMStringMap", "HTMLOutputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLParagraphElement() throws Exception {
        test("DOMStringMap", "HTMLParagraphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLParamElement() throws Exception {
        test("DOMStringMap", "HTMLParamElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLPreElement() throws Exception {
        test("DOMStringMap", "HTMLPreElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLProgressElement() throws Exception {
        test("DOMStringMap", "HTMLProgressElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLQuoteElement() throws Exception {
        test("DOMStringMap", "HTMLQuoteElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLScriptElement() throws Exception {
        test("DOMStringMap", "HTMLScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLSelectElement() throws Exception {
        test("DOMStringMap", "HTMLSelectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLSourceElement() throws Exception {
        test("DOMStringMap", "HTMLSourceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLSpanElement() throws Exception {
        test("DOMStringMap", "HTMLSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLStyleElement() throws Exception {
        test("DOMStringMap", "HTMLStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLTableCaptionElement() throws Exception {
        test("DOMStringMap", "HTMLTableCaptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLTableCellElement() throws Exception {
        test("DOMStringMap", "HTMLTableCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLTableColElement() throws Exception {
        test("DOMStringMap", "HTMLTableColElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLTableElement() throws Exception {
        test("DOMStringMap", "HTMLTableElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLTableRowElement() throws Exception {
        test("DOMStringMap", "HTMLTableRowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLTableSectionElement() throws Exception {
        test("DOMStringMap", "HTMLTableSectionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLTextAreaElement() throws Exception {
        test("DOMStringMap", "HTMLTextAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _DOMStringMap_HTMLTimeElement() throws Exception {
        test("DOMStringMap", "HTMLTimeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLTitleElement() throws Exception {
        test("DOMStringMap", "HTMLTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLTrackElement() throws Exception {
        test("DOMStringMap", "HTMLTrackElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLUListElement() throws Exception {
        test("DOMStringMap", "HTMLUListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLUnknownElement() throws Exception {
        test("DOMStringMap", "HTMLUnknownElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HTMLVideoElement() throws Exception {
        test("DOMStringMap", "HTMLVideoElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_HashChangeEvent() throws Exception {
        test("DOMStringMap", "HashChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_History() throws Exception {
        test("DOMStringMap", "History");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Image() throws Exception {
        test("DOMStringMap", "Image");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Int16Array() throws Exception {
        test("DOMStringMap", "Int16Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Int32Array() throws Exception {
        test("DOMStringMap", "Int32Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Int8Array() throws Exception {
        test("DOMStringMap", "Int8Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_KeyboardEvent() throws Exception {
        test("DOMStringMap", "KeyboardEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Location() throws Exception {
        test("DOMStringMap", "Location");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_MediaList() throws Exception {
        test("DOMStringMap", "MediaList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_MessageEvent() throws Exception {
        test("DOMStringMap", "MessageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_MimeType() throws Exception {
        test("DOMStringMap", "MimeType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_MimeTypeArray() throws Exception {
        test("DOMStringMap", "MimeTypeArray");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_MouseEvent() throws Exception {
        test("DOMStringMap", "MouseEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_MutationEvent() throws Exception {
        test("DOMStringMap", "MutationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Navigator() throws Exception {
        test("DOMStringMap", "Navigator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Node() throws Exception {
        test("DOMStringMap", "Node");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_NodeFilter() throws Exception {
        test("DOMStringMap", "NodeFilter");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_NodeList() throws Exception {
        test("DOMStringMap", "NodeList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Notification() throws Exception {
        test("DOMStringMap", "Notification");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Option() throws Exception {
        test("DOMStringMap", "Option");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Plugin() throws Exception {
        test("DOMStringMap", "Plugin");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_PluginArray() throws Exception {
        test("DOMStringMap", "PluginArray");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_ProcessingInstruction() throws Exception {
        test("DOMStringMap", "ProcessingInstruction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Range() throws Exception {
        test("DOMStringMap", "Range");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGAElement() throws Exception {
        test("DOMStringMap", "SVGAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _DOMStringMap_SVGAltGlyphElement() throws Exception {
        test("DOMStringMap", "SVGAltGlyphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGAngle() throws Exception {
        test("DOMStringMap", "SVGAngle");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGAnimateElement() throws Exception {
        test("DOMStringMap", "SVGAnimateElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGAnimateMotionElement() throws Exception {
        test("DOMStringMap", "SVGAnimateMotionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGAnimateTransformElement() throws Exception {
        test("DOMStringMap", "SVGAnimateTransformElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGCircleElement() throws Exception {
        test("DOMStringMap", "SVGCircleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGClipPathElement() throws Exception {
        test("DOMStringMap", "SVGClipPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGDefsElement() throws Exception {
        test("DOMStringMap", "SVGDefsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGDescElement() throws Exception {
        test("DOMStringMap", "SVGDescElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGElement() throws Exception {
        test("DOMStringMap", "SVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGEllipseElement() throws Exception {
        test("DOMStringMap", "SVGEllipseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFEBlendElement() throws Exception {
        test("DOMStringMap", "SVGFEBlendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFEColorMatrixElement() throws Exception {
        test("DOMStringMap", "SVGFEColorMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFEComponentTransferElement() throws Exception {
        test("DOMStringMap", "SVGFEComponentTransferElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFECompositeElement() throws Exception {
        test("DOMStringMap", "SVGFECompositeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFEConvolveMatrixElement() throws Exception {
        test("DOMStringMap", "SVGFEConvolveMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFEDiffuseLightingElement() throws Exception {
        test("DOMStringMap", "SVGFEDiffuseLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFEDisplacementMapElement() throws Exception {
        test("DOMStringMap", "SVGFEDisplacementMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFEDistantLightElement() throws Exception {
        test("DOMStringMap", "SVGFEDistantLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFEFloodElement() throws Exception {
        test("DOMStringMap", "SVGFEFloodElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFEFuncAElement() throws Exception {
        test("DOMStringMap", "SVGFEFuncAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFEFuncBElement() throws Exception {
        test("DOMStringMap", "SVGFEFuncBElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFEFuncGElement() throws Exception {
        test("DOMStringMap", "SVGFEFuncGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFEFuncRElement() throws Exception {
        test("DOMStringMap", "SVGFEFuncRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFEGaussianBlurElement() throws Exception {
        test("DOMStringMap", "SVGFEGaussianBlurElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFEImageElement() throws Exception {
        test("DOMStringMap", "SVGFEImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFEMergeElement() throws Exception {
        test("DOMStringMap", "SVGFEMergeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFEMergeNodeElement() throws Exception {
        test("DOMStringMap", "SVGFEMergeNodeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFEMorphologyElement() throws Exception {
        test("DOMStringMap", "SVGFEMorphologyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFEOffsetElement() throws Exception {
        test("DOMStringMap", "SVGFEOffsetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFEPointLightElement() throws Exception {
        test("DOMStringMap", "SVGFEPointLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFESpecularLightingElement() throws Exception {
        test("DOMStringMap", "SVGFESpecularLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFESpotLightElement() throws Exception {
        test("DOMStringMap", "SVGFESpotLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFETileElement() throws Exception {
        test("DOMStringMap", "SVGFETileElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFETurbulenceElement() throws Exception {
        test("DOMStringMap", "SVGFETurbulenceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGFilterElement() throws Exception {
        test("DOMStringMap", "SVGFilterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGForeignObjectElement() throws Exception {
        test("DOMStringMap", "SVGForeignObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGGElement() throws Exception {
        test("DOMStringMap", "SVGGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGImageElement() throws Exception {
        test("DOMStringMap", "SVGImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGLineElement() throws Exception {
        test("DOMStringMap", "SVGLineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGLinearGradientElement() throws Exception {
        test("DOMStringMap", "SVGLinearGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGMPathElement() throws Exception {
        test("DOMStringMap", "SVGMPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGMarkerElement() throws Exception {
        test("DOMStringMap", "SVGMarkerElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGMaskElement() throws Exception {
        test("DOMStringMap", "SVGMaskElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGMatrix() throws Exception {
        test("DOMStringMap", "SVGMatrix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGMetadataElement() throws Exception {
        test("DOMStringMap", "SVGMetadataElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGPathElement() throws Exception {
        test("DOMStringMap", "SVGPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGPatternElement() throws Exception {
        test("DOMStringMap", "SVGPatternElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGPolygonElement() throws Exception {
        test("DOMStringMap", "SVGPolygonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGPolylineElement() throws Exception {
        test("DOMStringMap", "SVGPolylineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGRadialGradientElement() throws Exception {
        test("DOMStringMap", "SVGRadialGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGRect() throws Exception {
        test("DOMStringMap", "SVGRect");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGRectElement() throws Exception {
        test("DOMStringMap", "SVGRectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGSVGElement() throws Exception {
        test("DOMStringMap", "SVGSVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGScriptElement() throws Exception {
        test("DOMStringMap", "SVGScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGSetElement() throws Exception {
        test("DOMStringMap", "SVGSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGStopElement() throws Exception {
        test("DOMStringMap", "SVGStopElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGStyleElement() throws Exception {
        test("DOMStringMap", "SVGStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGSwitchElement() throws Exception {
        test("DOMStringMap", "SVGSwitchElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGSymbolElement() throws Exception {
        test("DOMStringMap", "SVGSymbolElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGTSpanElement() throws Exception {
        test("DOMStringMap", "SVGTSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGTextElement() throws Exception {
        test("DOMStringMap", "SVGTextElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGTextPathElement() throws Exception {
        test("DOMStringMap", "SVGTextPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGTitleElement() throws Exception {
        test("DOMStringMap", "SVGTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGUseElement() throws Exception {
        test("DOMStringMap", "SVGUseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_SVGViewElement() throws Exception {
        test("DOMStringMap", "SVGViewElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Screen() throws Exception {
        test("DOMStringMap", "Screen");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Selection() throws Exception {
        test("DOMStringMap", "Selection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_StaticNodeList() throws Exception {
        test("DOMStringMap", "StaticNodeList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Storage() throws Exception {
        test("DOMStringMap", "Storage");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_StyleSheetList() throws Exception {
        test("DOMStringMap", "StyleSheetList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Text() throws Exception {
        test("DOMStringMap", "Text");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_TreeWalker() throws Exception {
        test("DOMStringMap", "TreeWalker");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_UIEvent() throws Exception {
        test("DOMStringMap", "UIEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Uint16Array() throws Exception {
        test("DOMStringMap", "Uint16Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Uint32Array() throws Exception {
        test("DOMStringMap", "Uint32Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Uint8Array() throws Exception {
        test("DOMStringMap", "Uint8Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Uint8ClampedArray() throws Exception {
        test("DOMStringMap", "Uint8ClampedArray");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_WebSocket() throws Exception {
        test("DOMStringMap", "WebSocket");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_Window() throws Exception {
        test("DOMStringMap", "Window");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_XMLDocument() throws Exception {
        test("DOMStringMap", "XMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_XMLHttpRequest() throws Exception {
        test("DOMStringMap", "XMLHttpRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_XMLSerializer() throws Exception {
        test("DOMStringMap", "XMLSerializer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_XPathEvaluator() throws Exception {
        test("DOMStringMap", "XPathEvaluator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _DOMStringMap_XPathNSResolver() throws Exception {
        test("DOMStringMap", "XPathNSResolver");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_XPathResult() throws Exception {
        test("DOMStringMap", "XPathResult");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMStringMap_XSLTProcessor() throws Exception {
        test("DOMStringMap", "XSLTProcessor");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMTokenList_HTMLDocument() throws Exception {
        test("DOMTokenList", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMTokenList_HTMLFormElement() throws Exception {
        test("DOMTokenList", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DOMTokenList_HTMLOptionsCollection() throws Exception {
        test("DOMTokenList", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DataView_HTMLDocument() throws Exception {
        test("DataView", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DataView_HTMLFormElement() throws Exception {
        test("DataView", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DataView_HTMLOptionsCollection() throws Exception {
        test("DataView", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DocumentFragment_HTMLDocument() throws Exception {
        test("DocumentFragment", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DocumentFragment_HTMLFormElement() throws Exception {
        test("DocumentFragment", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DocumentFragment_HTMLOptionsCollection() throws Exception {
        test("DocumentFragment", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DocumentType_HTMLDocument() throws Exception {
        test("DocumentType", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DocumentType_HTMLFormElement() throws Exception {
        test("DocumentType", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _DocumentType_HTMLOptionsCollection() throws Exception {
        test("DocumentType", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Document_HTMLFormElement() throws Exception {
        test("Document", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Document_HTMLOptionsCollection() throws Exception {
        test("Document", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Element_HTMLDocument() throws Exception {
        test("Element", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Element_HTMLOptionsCollection() throws Exception {
        test("Element", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Event_HTMLDocument() throws Exception {
        test("Event", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _Event_HTMLFormElement() throws Exception {
        test("Event", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Event_HTMLOptionsCollection() throws Exception {
        test("Event", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _External_HTMLDocument() throws Exception {
        test("External", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _External_HTMLFormElement() throws Exception {
        test("External", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _External_HTMLOptionsCollection() throws Exception {
        test("External", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Float32Array_HTMLDocument() throws Exception {
        test("Float32Array", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Float32Array_HTMLFormElement() throws Exception {
        test("Float32Array", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Float32Array_HTMLOptionsCollection() throws Exception {
        test("Float32Array", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Float64Array_HTMLDocument() throws Exception {
        test("Float64Array", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Float64Array_HTMLFormElement() throws Exception {
        test("Float64Array", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Float64Array_HTMLOptionsCollection() throws Exception {
        test("Float64Array", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLAnchorElement_HTMLDocument() throws Exception {
        test("HTMLAnchorElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLAnchorElement_HTMLFormElement() throws Exception {
        test("HTMLAnchorElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLAnchorElement_HTMLOptionsCollection() throws Exception {
        test("HTMLAnchorElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLAppletElement_HTMLDocument() throws Exception {
        test("HTMLAppletElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLAppletElement_HTMLFormElement() throws Exception {
        test("HTMLAppletElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLAppletElement_HTMLOptionsCollection() throws Exception {
        test("HTMLAppletElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLAreaElement_HTMLDocument() throws Exception {
        test("HTMLAreaElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLAreaElement_HTMLFormElement() throws Exception {
        test("HTMLAreaElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLAreaElement_HTMLOptionsCollection() throws Exception {
        test("HTMLAreaElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLAudioElement_HTMLDocument() throws Exception {
        test("HTMLAudioElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLAudioElement_HTMLFormElement() throws Exception {
        test("HTMLAudioElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLAudioElement_HTMLOptionsCollection() throws Exception {
        test("HTMLAudioElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLBRElement_HTMLDocument() throws Exception {
        test("HTMLBRElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLBRElement_HTMLFormElement() throws Exception {
        test("HTMLBRElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLBRElement_HTMLOptionsCollection() throws Exception {
        test("HTMLBRElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLBaseElement_HTMLDocument() throws Exception {
        test("HTMLBaseElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLBaseElement_HTMLFormElement() throws Exception {
        test("HTMLBaseElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLBaseElement_HTMLOptionsCollection() throws Exception {
        test("HTMLBaseElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLBodyElement_HTMLDocument() throws Exception {
        test("HTMLBodyElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLBodyElement_HTMLFormElement() throws Exception {
        test("HTMLBodyElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLBodyElement_HTMLOptionsCollection() throws Exception {
        test("HTMLBodyElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLButtonElement_HTMLDocument() throws Exception {
        test("HTMLButtonElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLButtonElement_HTMLFormElement() throws Exception {
        test("HTMLButtonElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLButtonElement_HTMLOptionsCollection() throws Exception {
        test("HTMLButtonElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLCanvasElement_HTMLDocument() throws Exception {
        test("HTMLCanvasElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLCanvasElement_HTMLFormElement() throws Exception {
        test("HTMLCanvasElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLCanvasElement_HTMLOptionsCollection() throws Exception {
        test("HTMLCanvasElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLCollection_HTMLDocument() throws Exception {
        test("HTMLCollection", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLCollection_HTMLFormElement() throws Exception {
        test("HTMLCollection", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLDListElement_HTMLDocument() throws Exception {
        test("HTMLDListElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLDListElement_HTMLFormElement() throws Exception {
        test("HTMLDListElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLDListElement_HTMLOptionsCollection() throws Exception {
        test("HTMLDListElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLDataListElement_HTMLDocument() throws Exception {
        test("HTMLDataListElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLDataListElement_HTMLFormElement() throws Exception {
        test("HTMLDataListElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLDataListElement_HTMLOptionsCollection() throws Exception {
        test("HTMLDataListElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLDirectoryElement_HTMLDocument() throws Exception {
        test("HTMLDirectoryElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLDirectoryElement_HTMLFormElement() throws Exception {
        test("HTMLDirectoryElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLDirectoryElement_HTMLOptionsCollection() throws Exception {
        test("HTMLDirectoryElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLDivElement_HTMLDocument() throws Exception {
        test("HTMLDivElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLDivElement_HTMLFormElement() throws Exception {
        test("HTMLDivElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLDivElement_HTMLOptionsCollection() throws Exception {
        test("HTMLDivElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLDocument_HTMLFormElement() throws Exception {
        test("HTMLDocument", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLDocument_HTMLOptionsCollection() throws Exception {
        test("HTMLDocument", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLElement_HTMLDocument() throws Exception {
        test("HTMLElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLElement_HTMLOptionsCollection() throws Exception {
        test("HTMLElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLEmbedElement_HTMLDocument() throws Exception {
        test("HTMLEmbedElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLEmbedElement_HTMLFormElement() throws Exception {
        test("HTMLEmbedElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLEmbedElement_HTMLOptionsCollection() throws Exception {
        test("HTMLEmbedElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLFieldSetElement_HTMLDocument() throws Exception {
        test("HTMLFieldSetElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLFieldSetElement_HTMLFormElement() throws Exception {
        test("HTMLFieldSetElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLFieldSetElement_HTMLOptionsCollection() throws Exception {
        test("HTMLFieldSetElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLFontElement_HTMLDocument() throws Exception {
        test("HTMLFontElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLFontElement_HTMLFormElement() throws Exception {
        test("HTMLFontElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLFontElement_HTMLOptionsCollection() throws Exception {
        test("HTMLFontElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLFormElement_HTMLDocument() throws Exception {
        test("HTMLFormElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLFormElement_HTMLOptionsCollection() throws Exception {
        test("HTMLFormElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLFrameElement_HTMLDocument() throws Exception {
        test("HTMLFrameElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLFrameElement_HTMLFormElement() throws Exception {
        test("HTMLFrameElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLFrameElement_HTMLOptionsCollection() throws Exception {
        test("HTMLFrameElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLFrameSetElement_HTMLDocument() throws Exception {
        test("HTMLFrameSetElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLFrameSetElement_HTMLFormElement() throws Exception {
        test("HTMLFrameSetElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLFrameSetElement_HTMLOptionsCollection() throws Exception {
        test("HTMLFrameSetElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLHRElement_HTMLDocument() throws Exception {
        test("HTMLHRElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLHRElement_HTMLFormElement() throws Exception {
        test("HTMLHRElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLHRElement_HTMLOptionsCollection() throws Exception {
        test("HTMLHRElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLHeadElement_HTMLDocument() throws Exception {
        test("HTMLHeadElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLHeadElement_HTMLFormElement() throws Exception {
        test("HTMLHeadElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLHeadElement_HTMLOptionsCollection() throws Exception {
        test("HTMLHeadElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLHeadingElement_HTMLDocument() throws Exception {
        test("HTMLHeadingElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLHeadingElement_HTMLFormElement() throws Exception {
        test("HTMLHeadingElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLHeadingElement_HTMLOptionsCollection() throws Exception {
        test("HTMLHeadingElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLHtmlElement_HTMLDocument() throws Exception {
        test("HTMLHtmlElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLHtmlElement_HTMLFormElement() throws Exception {
        test("HTMLHtmlElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLHtmlElement_HTMLOptionsCollection() throws Exception {
        test("HTMLHtmlElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLIFrameElement_HTMLDocument() throws Exception {
        test("HTMLIFrameElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLIFrameElement_HTMLFormElement() throws Exception {
        test("HTMLIFrameElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLIFrameElement_HTMLOptionsCollection() throws Exception {
        test("HTMLIFrameElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLImageElement_HTMLDocument() throws Exception {
        test("HTMLImageElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLImageElement_HTMLFormElement() throws Exception {
        test("HTMLImageElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLImageElement_HTMLOptionsCollection() throws Exception {
        test("HTMLImageElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLInputElement_HTMLDocument() throws Exception {
        test("HTMLInputElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLInputElement_HTMLFormElement() throws Exception {
        test("HTMLInputElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLInputElement_HTMLOptionsCollection() throws Exception {
        test("HTMLInputElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLLIElement_HTMLDocument() throws Exception {
        test("HTMLLIElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLLIElement_HTMLFormElement() throws Exception {
        test("HTMLLIElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLLIElement_HTMLOptionsCollection() throws Exception {
        test("HTMLLIElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLLabelElement_HTMLDocument() throws Exception {
        test("HTMLLabelElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLLabelElement_HTMLFormElement() throws Exception {
        test("HTMLLabelElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLLabelElement_HTMLOptionsCollection() throws Exception {
        test("HTMLLabelElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLLegendElement_HTMLDocument() throws Exception {
        test("HTMLLegendElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLLegendElement_HTMLFormElement() throws Exception {
        test("HTMLLegendElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLLegendElement_HTMLOptionsCollection() throws Exception {
        test("HTMLLegendElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLLinkElement_HTMLDocument() throws Exception {
        test("HTMLLinkElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLLinkElement_HTMLFormElement() throws Exception {
        test("HTMLLinkElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLLinkElement_HTMLOptionsCollection() throws Exception {
        test("HTMLLinkElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLMapElement_HTMLDocument() throws Exception {
        test("HTMLMapElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLMapElement_HTMLFormElement() throws Exception {
        test("HTMLMapElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLMapElement_HTMLOptionsCollection() throws Exception {
        test("HTMLMapElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLMediaElement_HTMLDocument() throws Exception {
        test("HTMLMediaElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLMediaElement_HTMLFormElement() throws Exception {
        test("HTMLMediaElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLMediaElement_HTMLOptionsCollection() throws Exception {
        test("HTMLMediaElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLMenuElement_HTMLDocument() throws Exception {
        test("HTMLMenuElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLMenuElement_HTMLFormElement() throws Exception {
        test("HTMLMenuElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLMenuElement_HTMLOptionsCollection() throws Exception {
        test("HTMLMenuElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _HTMLMenuItemElement_HTMLDocument() throws Exception {
        test("HTMLMenuItemElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _HTMLMenuItemElement_HTMLFormElement() throws Exception {
        test("HTMLMenuItemElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _HTMLMenuItemElement_HTMLOptionsCollection() throws Exception {
        test("HTMLMenuItemElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLMetaElement_HTMLDocument() throws Exception {
        test("HTMLMetaElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLMetaElement_HTMLFormElement() throws Exception {
        test("HTMLMetaElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLMetaElement_HTMLOptionsCollection() throws Exception {
        test("HTMLMetaElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLMeterElement_HTMLDocument() throws Exception {
        test("HTMLMeterElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLMeterElement_HTMLFormElement() throws Exception {
        test("HTMLMeterElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLMeterElement_HTMLOptionsCollection() throws Exception {
        test("HTMLMeterElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLModElement_HTMLDocument() throws Exception {
        test("HTMLModElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLModElement_HTMLFormElement() throws Exception {
        test("HTMLModElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLModElement_HTMLOptionsCollection() throws Exception {
        test("HTMLModElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLOListElement_HTMLDocument() throws Exception {
        test("HTMLOListElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLOListElement_HTMLFormElement() throws Exception {
        test("HTMLOListElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLOListElement_HTMLOptionsCollection() throws Exception {
        test("HTMLOListElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLObjectElement_HTMLDocument() throws Exception {
        test("HTMLObjectElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLObjectElement_HTMLFormElement() throws Exception {
        test("HTMLObjectElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLObjectElement_HTMLOptionsCollection() throws Exception {
        test("HTMLObjectElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLOptGroupElement_HTMLDocument() throws Exception {
        test("HTMLOptGroupElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLOptGroupElement_HTMLFormElement() throws Exception {
        test("HTMLOptGroupElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLOptGroupElement_HTMLOptionsCollection() throws Exception {
        test("HTMLOptGroupElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLOptionElement_HTMLDocument() throws Exception {
        test("HTMLOptionElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLOptionElement_HTMLFormElement() throws Exception {
        test("HTMLOptionElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLOptionElement_HTMLOptionsCollection() throws Exception {
        test("HTMLOptionElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLOptionsCollection_HTMLDocument() throws Exception {
        test("HTMLOptionsCollection", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLOptionsCollection_HTMLFormElement() throws Exception {
        test("HTMLOptionsCollection", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLOutputElement_HTMLDocument() throws Exception {
        test("HTMLOutputElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLOutputElement_HTMLFormElement() throws Exception {
        test("HTMLOutputElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLOutputElement_HTMLOptionsCollection() throws Exception {
        test("HTMLOutputElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLParagraphElement_HTMLDocument() throws Exception {
        test("HTMLParagraphElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLParagraphElement_HTMLFormElement() throws Exception {
        test("HTMLParagraphElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLParagraphElement_HTMLOptionsCollection() throws Exception {
        test("HTMLParagraphElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLParamElement_HTMLDocument() throws Exception {
        test("HTMLParamElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLParamElement_HTMLFormElement() throws Exception {
        test("HTMLParamElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLParamElement_HTMLOptionsCollection() throws Exception {
        test("HTMLParamElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLPreElement_HTMLDocument() throws Exception {
        test("HTMLPreElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLPreElement_HTMLFormElement() throws Exception {
        test("HTMLPreElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLPreElement_HTMLOptionsCollection() throws Exception {
        test("HTMLPreElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLProgressElement_HTMLDocument() throws Exception {
        test("HTMLProgressElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLProgressElement_HTMLFormElement() throws Exception {
        test("HTMLProgressElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLProgressElement_HTMLOptionsCollection() throws Exception {
        test("HTMLProgressElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLQuoteElement_HTMLDocument() throws Exception {
        test("HTMLQuoteElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLQuoteElement_HTMLFormElement() throws Exception {
        test("HTMLQuoteElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLQuoteElement_HTMLOptionsCollection() throws Exception {
        test("HTMLQuoteElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLScriptElement_HTMLDocument() throws Exception {
        test("HTMLScriptElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLScriptElement_HTMLFormElement() throws Exception {
        test("HTMLScriptElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLScriptElement_HTMLOptionsCollection() throws Exception {
        test("HTMLScriptElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLSelectElement_HTMLDocument() throws Exception {
        test("HTMLSelectElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLSelectElement_HTMLFormElement() throws Exception {
        test("HTMLSelectElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLSelectElement_HTMLOptionsCollection() throws Exception {
        test("HTMLSelectElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLSourceElement_HTMLDocument() throws Exception {
        test("HTMLSourceElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLSourceElement_HTMLFormElement() throws Exception {
        test("HTMLSourceElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLSourceElement_HTMLOptionsCollection() throws Exception {
        test("HTMLSourceElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLSpanElement_HTMLDocument() throws Exception {
        test("HTMLSpanElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLSpanElement_HTMLFormElement() throws Exception {
        test("HTMLSpanElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLSpanElement_HTMLOptionsCollection() throws Exception {
        test("HTMLSpanElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLStyleElement_HTMLDocument() throws Exception {
        test("HTMLStyleElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLStyleElement_HTMLFormElement() throws Exception {
        test("HTMLStyleElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLStyleElement_HTMLOptionsCollection() throws Exception {
        test("HTMLStyleElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLTableCaptionElement_HTMLDocument() throws Exception {
        test("HTMLTableCaptionElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLTableCaptionElement_HTMLFormElement() throws Exception {
        test("HTMLTableCaptionElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLTableCaptionElement_HTMLOptionsCollection() throws Exception {
        test("HTMLTableCaptionElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLTableCellElement_HTMLDocument() throws Exception {
        test("HTMLTableCellElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLTableCellElement_HTMLFormElement() throws Exception {
        test("HTMLTableCellElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLTableCellElement_HTMLOptionsCollection() throws Exception {
        test("HTMLTableCellElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLTableColElement_HTMLDocument() throws Exception {
        test("HTMLTableColElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLTableColElement_HTMLFormElement() throws Exception {
        test("HTMLTableColElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLTableColElement_HTMLOptionsCollection() throws Exception {
        test("HTMLTableColElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLTableElement_HTMLDocument() throws Exception {
        test("HTMLTableElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLTableElement_HTMLFormElement() throws Exception {
        test("HTMLTableElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLTableElement_HTMLOptionsCollection() throws Exception {
        test("HTMLTableElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLTableRowElement_HTMLDocument() throws Exception {
        test("HTMLTableRowElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLTableRowElement_HTMLFormElement() throws Exception {
        test("HTMLTableRowElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLTableRowElement_HTMLOptionsCollection() throws Exception {
        test("HTMLTableRowElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLTableSectionElement_HTMLDocument() throws Exception {
        test("HTMLTableSectionElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLTableSectionElement_HTMLFormElement() throws Exception {
        test("HTMLTableSectionElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLTableSectionElement_HTMLOptionsCollection() throws Exception {
        test("HTMLTableSectionElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLTextAreaElement_HTMLDocument() throws Exception {
        test("HTMLTextAreaElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLTextAreaElement_HTMLFormElement() throws Exception {
        test("HTMLTextAreaElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLTextAreaElement_HTMLOptionsCollection() throws Exception {
        test("HTMLTextAreaElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _HTMLTimeElement_HTMLDocument() throws Exception {
        test("HTMLTimeElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _HTMLTimeElement_HTMLFormElement() throws Exception {
        test("HTMLTimeElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _HTMLTimeElement_HTMLOptionsCollection() throws Exception {
        test("HTMLTimeElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLTitleElement_HTMLDocument() throws Exception {
        test("HTMLTitleElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLTitleElement_HTMLFormElement() throws Exception {
        test("HTMLTitleElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLTitleElement_HTMLOptionsCollection() throws Exception {
        test("HTMLTitleElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLTrackElement_HTMLDocument() throws Exception {
        test("HTMLTrackElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLTrackElement_HTMLFormElement() throws Exception {
        test("HTMLTrackElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLTrackElement_HTMLOptionsCollection() throws Exception {
        test("HTMLTrackElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLUListElement_HTMLDocument() throws Exception {
        test("HTMLUListElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _HTMLUListElement_HTMLFormElement() throws Exception {
        test("HTMLUListElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLUListElement_HTMLOptionsCollection() throws Exception {
        test("HTMLUListElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLUnknownElement_HTMLDocument() throws Exception {
        test("HTMLUnknownElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLUnknownElement_HTMLFormElement() throws Exception {
        test("HTMLUnknownElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLUnknownElement_HTMLOptionsCollection() throws Exception {
        test("HTMLUnknownElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLVideoElement_HTMLDocument() throws Exception {
        test("HTMLVideoElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLVideoElement_HTMLFormElement() throws Exception {
        test("HTMLVideoElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HTMLVideoElement_HTMLOptionsCollection() throws Exception {
        test("HTMLVideoElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HashChangeEvent_HTMLDocument() throws Exception {
        test("HashChangeEvent", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HashChangeEvent_HTMLFormElement() throws Exception {
        test("HashChangeEvent", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _HashChangeEvent_HTMLOptionsCollection() throws Exception {
        test("HashChangeEvent", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _History_HTMLDocument() throws Exception {
        test("History", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _History_HTMLFormElement() throws Exception {
        test("History", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _History_HTMLOptionsCollection() throws Exception {
        test("History", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Image_HTMLDocument() throws Exception {
        test("Image", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _Image_HTMLFormElement() throws Exception {
        test("Image", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Image_HTMLOptionsCollection() throws Exception {
        test("Image", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Int16Array_HTMLDocument() throws Exception {
        test("Int16Array", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Int16Array_HTMLFormElement() throws Exception {
        test("Int16Array", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Int16Array_HTMLOptionsCollection() throws Exception {
        test("Int16Array", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Int32Array_HTMLDocument() throws Exception {
        test("Int32Array", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Int32Array_HTMLFormElement() throws Exception {
        test("Int32Array", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Int32Array_HTMLOptionsCollection() throws Exception {
        test("Int32Array", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Int8Array_HTMLDocument() throws Exception {
        test("Int8Array", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Int8Array_HTMLFormElement() throws Exception {
        test("Int8Array", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Int8Array_HTMLOptionsCollection() throws Exception {
        test("Int8Array", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _KeyboardEvent_HTMLDocument() throws Exception {
        test("KeyboardEvent", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _KeyboardEvent_HTMLFormElement() throws Exception {
        test("KeyboardEvent", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _KeyboardEvent_HTMLOptionsCollection() throws Exception {
        test("KeyboardEvent", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Location_HTMLDocument() throws Exception {
        test("Location", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _Location_HTMLFormElement() throws Exception {
        test("Location", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Location_HTMLOptionsCollection() throws Exception {
        test("Location", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _MediaList_HTMLDocument() throws Exception {
        test("MediaList", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _MediaList_HTMLFormElement() throws Exception {
        test("MediaList", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _MediaList_HTMLOptionsCollection() throws Exception {
        test("MediaList", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _MessageEvent_HTMLDocument() throws Exception {
        test("MessageEvent", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _MessageEvent_HTMLFormElement() throws Exception {
        test("MessageEvent", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _MessageEvent_HTMLOptionsCollection() throws Exception {
        test("MessageEvent", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _MimeTypeArray_HTMLDocument() throws Exception {
        test("MimeTypeArray", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _MimeTypeArray_HTMLFormElement() throws Exception {
        test("MimeTypeArray", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _MimeTypeArray_HTMLOptionsCollection() throws Exception {
        test("MimeTypeArray", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _MimeType_HTMLDocument() throws Exception {
        test("MimeType", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _MimeType_HTMLFormElement() throws Exception {
        test("MimeType", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _MimeType_HTMLOptionsCollection() throws Exception {
        test("MimeType", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _MouseEvent_HTMLDocument() throws Exception {
        test("MouseEvent", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _MouseEvent_HTMLFormElement() throws Exception {
        test("MouseEvent", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _MouseEvent_HTMLOptionsCollection() throws Exception {
        test("MouseEvent", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _MutationEvent_HTMLDocument() throws Exception {
        test("MutationEvent", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _MutationEvent_HTMLFormElement() throws Exception {
        test("MutationEvent", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _MutationEvent_HTMLOptionsCollection() throws Exception {
        test("MutationEvent", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Navigator_HTMLDocument() throws Exception {
        test("Navigator", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _Navigator_HTMLFormElement() throws Exception {
        test("Navigator", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Navigator_HTMLOptionsCollection() throws Exception {
        test("Navigator", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _NodeFilter_HTMLDocument() throws Exception {
        test("NodeFilter", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _NodeFilter_HTMLFormElement() throws Exception {
        test("NodeFilter", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _NodeFilter_HTMLOptionsCollection() throws Exception {
        test("NodeFilter", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _NodeList_HTMLDocument() throws Exception {
        test("NodeList", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _NodeList_HTMLFormElement() throws Exception {
        test("NodeList", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _NodeList_HTMLOptionsCollection() throws Exception {
        test("NodeList", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Node_HTMLOptionsCollection() throws Exception {
        test("Node", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Notification_HTMLDocument() throws Exception {
        test("Notification", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Notification_HTMLFormElement() throws Exception {
        test("Notification", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Notification_HTMLOptionsCollection() throws Exception {
        test("Notification", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Option_HTMLDocument() throws Exception {
        test("Option", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _Option_HTMLFormElement() throws Exception {
        test("Option", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Option_HTMLOptionsCollection() throws Exception {
        test("Option", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _PluginArray_HTMLDocument() throws Exception {
        test("PluginArray", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _PluginArray_HTMLFormElement() throws Exception {
        test("PluginArray", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _PluginArray_HTMLOptionsCollection() throws Exception {
        test("PluginArray", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Plugin_HTMLDocument() throws Exception {
        test("Plugin", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Plugin_HTMLFormElement() throws Exception {
        test("Plugin", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Plugin_HTMLOptionsCollection() throws Exception {
        test("Plugin", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _ProcessingInstruction_HTMLDocument() throws Exception {
        test("ProcessingInstruction", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _ProcessingInstruction_HTMLFormElement() throws Exception {
        test("ProcessingInstruction", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _ProcessingInstruction_HTMLOptionsCollection() throws Exception {
        test("ProcessingInstruction", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Range_HTMLDocument() throws Exception {
        test("Range", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Range_HTMLFormElement() throws Exception {
        test("Range", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Range_HTMLOptionsCollection() throws Exception {
        test("Range", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGAElement_HTMLDocument() throws Exception {
        test("SVGAElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGAElement_HTMLFormElement() throws Exception {
        test("SVGAElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGAElement_HTMLOptionsCollection() throws Exception {
        test("SVGAElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _SVGAltGlyphElement_HTMLDocument() throws Exception {
        test("SVGAltGlyphElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _SVGAltGlyphElement_HTMLFormElement() throws Exception {
        test("SVGAltGlyphElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _SVGAltGlyphElement_HTMLOptionsCollection() throws Exception {
        test("SVGAltGlyphElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGAngle_HTMLDocument() throws Exception {
        test("SVGAngle", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGAngle_HTMLFormElement() throws Exception {
        test("SVGAngle", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGAngle_HTMLOptionsCollection() throws Exception {
        test("SVGAngle", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGAnimateElement_HTMLDocument() throws Exception {
        test("SVGAnimateElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGAnimateElement_HTMLFormElement() throws Exception {
        test("SVGAnimateElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGAnimateElement_HTMLOptionsCollection() throws Exception {
        test("SVGAnimateElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGAnimateMotionElement_HTMLDocument() throws Exception {
        test("SVGAnimateMotionElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGAnimateMotionElement_HTMLFormElement() throws Exception {
        test("SVGAnimateMotionElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGAnimateMotionElement_HTMLOptionsCollection() throws Exception {
        test("SVGAnimateMotionElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGAnimateTransformElement_HTMLDocument() throws Exception {
        test("SVGAnimateTransformElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGAnimateTransformElement_HTMLFormElement() throws Exception {
        test("SVGAnimateTransformElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGAnimateTransformElement_HTMLOptionsCollection() throws Exception {
        test("SVGAnimateTransformElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGCircleElement_HTMLDocument() throws Exception {
        test("SVGCircleElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGCircleElement_HTMLFormElement() throws Exception {
        test("SVGCircleElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGCircleElement_HTMLOptionsCollection() throws Exception {
        test("SVGCircleElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGClipPathElement_HTMLDocument() throws Exception {
        test("SVGClipPathElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGClipPathElement_HTMLFormElement() throws Exception {
        test("SVGClipPathElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGClipPathElement_HTMLOptionsCollection() throws Exception {
        test("SVGClipPathElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGDefsElement_HTMLDocument() throws Exception {
        test("SVGDefsElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGDefsElement_HTMLFormElement() throws Exception {
        test("SVGDefsElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGDefsElement_HTMLOptionsCollection() throws Exception {
        test("SVGDefsElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGDescElement_HTMLDocument() throws Exception {
        test("SVGDescElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGDescElement_HTMLFormElement() throws Exception {
        test("SVGDescElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGDescElement_HTMLOptionsCollection() throws Exception {
        test("SVGDescElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGElement_HTMLDocument() throws Exception {
        test("SVGElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGElement_HTMLFormElement() throws Exception {
        test("SVGElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGElement_HTMLOptionsCollection() throws Exception {
        test("SVGElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGEllipseElement_HTMLDocument() throws Exception {
        test("SVGEllipseElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGEllipseElement_HTMLFormElement() throws Exception {
        test("SVGEllipseElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGEllipseElement_HTMLOptionsCollection() throws Exception {
        test("SVGEllipseElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEBlendElement_HTMLDocument() throws Exception {
        test("SVGFEBlendElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEBlendElement_HTMLFormElement() throws Exception {
        test("SVGFEBlendElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEBlendElement_HTMLOptionsCollection() throws Exception {
        test("SVGFEBlendElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEColorMatrixElement_HTMLDocument() throws Exception {
        test("SVGFEColorMatrixElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEColorMatrixElement_HTMLFormElement() throws Exception {
        test("SVGFEColorMatrixElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEColorMatrixElement_HTMLOptionsCollection() throws Exception {
        test("SVGFEColorMatrixElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEComponentTransferElement_HTMLDocument() throws Exception {
        test("SVGFEComponentTransferElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEComponentTransferElement_HTMLFormElement() throws Exception {
        test("SVGFEComponentTransferElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEComponentTransferElement_HTMLOptionsCollection() throws Exception {
        test("SVGFEComponentTransferElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFECompositeElement_HTMLDocument() throws Exception {
        test("SVGFECompositeElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFECompositeElement_HTMLFormElement() throws Exception {
        test("SVGFECompositeElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFECompositeElement_HTMLOptionsCollection() throws Exception {
        test("SVGFECompositeElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEConvolveMatrixElement_HTMLDocument() throws Exception {
        test("SVGFEConvolveMatrixElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEConvolveMatrixElement_HTMLFormElement() throws Exception {
        test("SVGFEConvolveMatrixElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEConvolveMatrixElement_HTMLOptionsCollection() throws Exception {
        test("SVGFEConvolveMatrixElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEDiffuseLightingElement_HTMLDocument() throws Exception {
        test("SVGFEDiffuseLightingElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEDiffuseLightingElement_HTMLFormElement() throws Exception {
        test("SVGFEDiffuseLightingElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEDiffuseLightingElement_HTMLOptionsCollection() throws Exception {
        test("SVGFEDiffuseLightingElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEDisplacementMapElement_HTMLDocument() throws Exception {
        test("SVGFEDisplacementMapElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEDisplacementMapElement_HTMLFormElement() throws Exception {
        test("SVGFEDisplacementMapElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEDisplacementMapElement_HTMLOptionsCollection() throws Exception {
        test("SVGFEDisplacementMapElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEDistantLightElement_HTMLDocument() throws Exception {
        test("SVGFEDistantLightElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEDistantLightElement_HTMLFormElement() throws Exception {
        test("SVGFEDistantLightElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEDistantLightElement_HTMLOptionsCollection() throws Exception {
        test("SVGFEDistantLightElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEFloodElement_HTMLDocument() throws Exception {
        test("SVGFEFloodElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEFloodElement_HTMLFormElement() throws Exception {
        test("SVGFEFloodElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEFloodElement_HTMLOptionsCollection() throws Exception {
        test("SVGFEFloodElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEFuncAElement_HTMLDocument() throws Exception {
        test("SVGFEFuncAElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEFuncAElement_HTMLFormElement() throws Exception {
        test("SVGFEFuncAElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEFuncAElement_HTMLOptionsCollection() throws Exception {
        test("SVGFEFuncAElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEFuncBElement_HTMLDocument() throws Exception {
        test("SVGFEFuncBElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEFuncBElement_HTMLFormElement() throws Exception {
        test("SVGFEFuncBElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEFuncBElement_HTMLOptionsCollection() throws Exception {
        test("SVGFEFuncBElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEFuncGElement_HTMLDocument() throws Exception {
        test("SVGFEFuncGElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEFuncGElement_HTMLFormElement() throws Exception {
        test("SVGFEFuncGElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEFuncGElement_HTMLOptionsCollection() throws Exception {
        test("SVGFEFuncGElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEFuncRElement_HTMLDocument() throws Exception {
        test("SVGFEFuncRElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEFuncRElement_HTMLFormElement() throws Exception {
        test("SVGFEFuncRElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEFuncRElement_HTMLOptionsCollection() throws Exception {
        test("SVGFEFuncRElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEGaussianBlurElement_HTMLDocument() throws Exception {
        test("SVGFEGaussianBlurElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEGaussianBlurElement_HTMLFormElement() throws Exception {
        test("SVGFEGaussianBlurElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEGaussianBlurElement_HTMLOptionsCollection() throws Exception {
        test("SVGFEGaussianBlurElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEImageElement_HTMLDocument() throws Exception {
        test("SVGFEImageElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEImageElement_HTMLFormElement() throws Exception {
        test("SVGFEImageElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEImageElement_HTMLOptionsCollection() throws Exception {
        test("SVGFEImageElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEMergeElement_HTMLDocument() throws Exception {
        test("SVGFEMergeElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEMergeElement_HTMLFormElement() throws Exception {
        test("SVGFEMergeElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEMergeElement_HTMLOptionsCollection() throws Exception {
        test("SVGFEMergeElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEMergeNodeElement_HTMLDocument() throws Exception {
        test("SVGFEMergeNodeElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEMergeNodeElement_HTMLFormElement() throws Exception {
        test("SVGFEMergeNodeElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEMergeNodeElement_HTMLOptionsCollection() throws Exception {
        test("SVGFEMergeNodeElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEMorphologyElement_HTMLDocument() throws Exception {
        test("SVGFEMorphologyElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEMorphologyElement_HTMLFormElement() throws Exception {
        test("SVGFEMorphologyElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEMorphologyElement_HTMLOptionsCollection() throws Exception {
        test("SVGFEMorphologyElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEOffsetElement_HTMLDocument() throws Exception {
        test("SVGFEOffsetElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEOffsetElement_HTMLFormElement() throws Exception {
        test("SVGFEOffsetElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEOffsetElement_HTMLOptionsCollection() throws Exception {
        test("SVGFEOffsetElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEPointLightElement_HTMLDocument() throws Exception {
        test("SVGFEPointLightElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEPointLightElement_HTMLFormElement() throws Exception {
        test("SVGFEPointLightElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFEPointLightElement_HTMLOptionsCollection() throws Exception {
        test("SVGFEPointLightElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFESpecularLightingElement_HTMLDocument() throws Exception {
        test("SVGFESpecularLightingElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFESpecularLightingElement_HTMLFormElement() throws Exception {
        test("SVGFESpecularLightingElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFESpecularLightingElement_HTMLOptionsCollection() throws Exception {
        test("SVGFESpecularLightingElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFESpotLightElement_HTMLDocument() throws Exception {
        test("SVGFESpotLightElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFESpotLightElement_HTMLFormElement() throws Exception {
        test("SVGFESpotLightElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFESpotLightElement_HTMLOptionsCollection() throws Exception {
        test("SVGFESpotLightElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFETileElement_HTMLDocument() throws Exception {
        test("SVGFETileElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFETileElement_HTMLFormElement() throws Exception {
        test("SVGFETileElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFETileElement_HTMLOptionsCollection() throws Exception {
        test("SVGFETileElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFETurbulenceElement_HTMLDocument() throws Exception {
        test("SVGFETurbulenceElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFETurbulenceElement_HTMLFormElement() throws Exception {
        test("SVGFETurbulenceElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFETurbulenceElement_HTMLOptionsCollection() throws Exception {
        test("SVGFETurbulenceElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFilterElement_HTMLDocument() throws Exception {
        test("SVGFilterElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFilterElement_HTMLFormElement() throws Exception {
        test("SVGFilterElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGFilterElement_HTMLOptionsCollection() throws Exception {
        test("SVGFilterElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGForeignObjectElement_HTMLDocument() throws Exception {
        test("SVGForeignObjectElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGForeignObjectElement_HTMLFormElement() throws Exception {
        test("SVGForeignObjectElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGForeignObjectElement_HTMLOptionsCollection() throws Exception {
        test("SVGForeignObjectElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGGElement_HTMLDocument() throws Exception {
        test("SVGGElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGGElement_HTMLFormElement() throws Exception {
        test("SVGGElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGGElement_HTMLOptionsCollection() throws Exception {
        test("SVGGElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGImageElement_HTMLDocument() throws Exception {
        test("SVGImageElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGImageElement_HTMLFormElement() throws Exception {
        test("SVGImageElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGImageElement_HTMLOptionsCollection() throws Exception {
        test("SVGImageElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGLineElement_HTMLDocument() throws Exception {
        test("SVGLineElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGLineElement_HTMLFormElement() throws Exception {
        test("SVGLineElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGLineElement_HTMLOptionsCollection() throws Exception {
        test("SVGLineElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGLinearGradientElement_HTMLDocument() throws Exception {
        test("SVGLinearGradientElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGLinearGradientElement_HTMLFormElement() throws Exception {
        test("SVGLinearGradientElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGLinearGradientElement_HTMLOptionsCollection() throws Exception {
        test("SVGLinearGradientElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGMPathElement_HTMLDocument() throws Exception {
        test("SVGMPathElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGMPathElement_HTMLFormElement() throws Exception {
        test("SVGMPathElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGMPathElement_HTMLOptionsCollection() throws Exception {
        test("SVGMPathElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGMarkerElement_HTMLDocument() throws Exception {
        test("SVGMarkerElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGMarkerElement_HTMLFormElement() throws Exception {
        test("SVGMarkerElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGMarkerElement_HTMLOptionsCollection() throws Exception {
        test("SVGMarkerElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGMaskElement_HTMLDocument() throws Exception {
        test("SVGMaskElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGMaskElement_HTMLFormElement() throws Exception {
        test("SVGMaskElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGMaskElement_HTMLOptionsCollection() throws Exception {
        test("SVGMaskElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGMatrix_HTMLDocument() throws Exception {
        test("SVGMatrix", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGMatrix_HTMLFormElement() throws Exception {
        test("SVGMatrix", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGMatrix_HTMLOptionsCollection() throws Exception {
        test("SVGMatrix", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGMetadataElement_HTMLDocument() throws Exception {
        test("SVGMetadataElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGMetadataElement_HTMLFormElement() throws Exception {
        test("SVGMetadataElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGMetadataElement_HTMLOptionsCollection() throws Exception {
        test("SVGMetadataElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGPathElement_HTMLDocument() throws Exception {
        test("SVGPathElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGPathElement_HTMLFormElement() throws Exception {
        test("SVGPathElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGPathElement_HTMLOptionsCollection() throws Exception {
        test("SVGPathElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGPatternElement_HTMLDocument() throws Exception {
        test("SVGPatternElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGPatternElement_HTMLFormElement() throws Exception {
        test("SVGPatternElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGPatternElement_HTMLOptionsCollection() throws Exception {
        test("SVGPatternElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGPolygonElement_HTMLDocument() throws Exception {
        test("SVGPolygonElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGPolygonElement_HTMLFormElement() throws Exception {
        test("SVGPolygonElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGPolygonElement_HTMLOptionsCollection() throws Exception {
        test("SVGPolygonElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGPolylineElement_HTMLDocument() throws Exception {
        test("SVGPolylineElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGPolylineElement_HTMLFormElement() throws Exception {
        test("SVGPolylineElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGPolylineElement_HTMLOptionsCollection() throws Exception {
        test("SVGPolylineElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGRadialGradientElement_HTMLDocument() throws Exception {
        test("SVGRadialGradientElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGRadialGradientElement_HTMLFormElement() throws Exception {
        test("SVGRadialGradientElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGRadialGradientElement_HTMLOptionsCollection() throws Exception {
        test("SVGRadialGradientElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGRectElement_HTMLDocument() throws Exception {
        test("SVGRectElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGRectElement_HTMLFormElement() throws Exception {
        test("SVGRectElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGRectElement_HTMLOptionsCollection() throws Exception {
        test("SVGRectElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGRect_HTMLDocument() throws Exception {
        test("SVGRect", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGRect_HTMLFormElement() throws Exception {
        test("SVGRect", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGRect_HTMLOptionsCollection() throws Exception {
        test("SVGRect", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGSVGElement_HTMLDocument() throws Exception {
        test("SVGSVGElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGSVGElement_HTMLFormElement() throws Exception {
        test("SVGSVGElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGSVGElement_HTMLOptionsCollection() throws Exception {
        test("SVGSVGElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGScriptElement_HTMLDocument() throws Exception {
        test("SVGScriptElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGScriptElement_HTMLFormElement() throws Exception {
        test("SVGScriptElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGScriptElement_HTMLOptionsCollection() throws Exception {
        test("SVGScriptElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGSetElement_HTMLDocument() throws Exception {
        test("SVGSetElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGSetElement_HTMLFormElement() throws Exception {
        test("SVGSetElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGSetElement_HTMLOptionsCollection() throws Exception {
        test("SVGSetElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGStopElement_HTMLDocument() throws Exception {
        test("SVGStopElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGStopElement_HTMLFormElement() throws Exception {
        test("SVGStopElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGStopElement_HTMLOptionsCollection() throws Exception {
        test("SVGStopElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGStyleElement_HTMLDocument() throws Exception {
        test("SVGStyleElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGStyleElement_HTMLFormElement() throws Exception {
        test("SVGStyleElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGStyleElement_HTMLOptionsCollection() throws Exception {
        test("SVGStyleElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGSwitchElement_HTMLDocument() throws Exception {
        test("SVGSwitchElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGSwitchElement_HTMLFormElement() throws Exception {
        test("SVGSwitchElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGSwitchElement_HTMLOptionsCollection() throws Exception {
        test("SVGSwitchElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGSymbolElement_HTMLDocument() throws Exception {
        test("SVGSymbolElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGSymbolElement_HTMLFormElement() throws Exception {
        test("SVGSymbolElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGSymbolElement_HTMLOptionsCollection() throws Exception {
        test("SVGSymbolElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGTSpanElement_HTMLDocument() throws Exception {
        test("SVGTSpanElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGTSpanElement_HTMLFormElement() throws Exception {
        test("SVGTSpanElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGTSpanElement_HTMLOptionsCollection() throws Exception {
        test("SVGTSpanElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGTextElement_HTMLDocument() throws Exception {
        test("SVGTextElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGTextElement_HTMLFormElement() throws Exception {
        test("SVGTextElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGTextElement_HTMLOptionsCollection() throws Exception {
        test("SVGTextElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGTextPathElement_HTMLDocument() throws Exception {
        test("SVGTextPathElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGTextPathElement_HTMLFormElement() throws Exception {
        test("SVGTextPathElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGTextPathElement_HTMLOptionsCollection() throws Exception {
        test("SVGTextPathElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGTitleElement_HTMLDocument() throws Exception {
        test("SVGTitleElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGTitleElement_HTMLFormElement() throws Exception {
        test("SVGTitleElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGTitleElement_HTMLOptionsCollection() throws Exception {
        test("SVGTitleElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGUseElement_HTMLDocument() throws Exception {
        test("SVGUseElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGUseElement_HTMLFormElement() throws Exception {
        test("SVGUseElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGUseElement_HTMLOptionsCollection() throws Exception {
        test("SVGUseElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGViewElement_HTMLDocument() throws Exception {
        test("SVGViewElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGViewElement_HTMLFormElement() throws Exception {
        test("SVGViewElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _SVGViewElement_HTMLOptionsCollection() throws Exception {
        test("SVGViewElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Screen_HTMLDocument() throws Exception {
        test("Screen", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _Screen_HTMLFormElement() throws Exception {
        test("Screen", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Screen_HTMLOptionsCollection() throws Exception {
        test("Screen", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Selection_HTMLDocument() throws Exception {
        test("Selection", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _Selection_HTMLFormElement() throws Exception {
        test("Selection", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Selection_HTMLOptionsCollection() throws Exception {
        test("Selection", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _StaticNodeList_HTMLDocument() throws Exception {
        test("StaticNodeList", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _StaticNodeList_HTMLFormElement() throws Exception {
        test("StaticNodeList", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _StaticNodeList_HTMLOptionsCollection() throws Exception {
        test("StaticNodeList", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Storage_HTMLDocument() throws Exception {
        test("Storage", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _Storage_HTMLFormElement() throws Exception {
        test("Storage", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Storage_HTMLOptionsCollection() throws Exception {
        test("Storage", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _StyleSheetList_HTMLDocument() throws Exception {
        test("StyleSheetList", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _StyleSheetList_HTMLFormElement() throws Exception {
        test("StyleSheetList", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _StyleSheetList_HTMLOptionsCollection() throws Exception {
        test("StyleSheetList", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Text_HTMLDocument() throws Exception {
        test("Text", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _Text_HTMLFormElement() throws Exception {
        test("Text", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Text_HTMLOptionsCollection() throws Exception {
        test("Text", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _TreeWalker_HTMLDocument() throws Exception {
        test("TreeWalker", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _TreeWalker_HTMLFormElement() throws Exception {
        test("TreeWalker", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _TreeWalker_HTMLOptionsCollection() throws Exception {
        test("TreeWalker", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _UIEvent_HTMLDocument() throws Exception {
        test("UIEvent", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _UIEvent_HTMLFormElement() throws Exception {
        test("UIEvent", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _UIEvent_HTMLOptionsCollection() throws Exception {
        test("UIEvent", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Uint16Array_HTMLDocument() throws Exception {
        test("Uint16Array", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Uint16Array_HTMLFormElement() throws Exception {
        test("Uint16Array", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Uint16Array_HTMLOptionsCollection() throws Exception {
        test("Uint16Array", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Uint32Array_HTMLDocument() throws Exception {
        test("Uint32Array", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Uint32Array_HTMLFormElement() throws Exception {
        test("Uint32Array", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Uint32Array_HTMLOptionsCollection() throws Exception {
        test("Uint32Array", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Uint8Array_HTMLDocument() throws Exception {
        test("Uint8Array", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Uint8Array_HTMLFormElement() throws Exception {
        test("Uint8Array", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Uint8Array_HTMLOptionsCollection() throws Exception {
        test("Uint8Array", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Uint8ClampedArray_HTMLDocument() throws Exception {
        test("Uint8ClampedArray", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Uint8ClampedArray_HTMLFormElement() throws Exception {
        test("Uint8ClampedArray", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Uint8ClampedArray_HTMLOptionsCollection() throws Exception {
        test("Uint8ClampedArray", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _WebSocket_HTMLDocument() throws Exception {
        test("WebSocket", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _WebSocket_HTMLFormElement() throws Exception {
        test("WebSocket", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _WebSocket_HTMLOptionsCollection() throws Exception {
        test("WebSocket", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Window_HTMLDocument() throws Exception {
        test("Window", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _Window_HTMLFormElement() throws Exception {
        test("Window", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _Window_HTMLOptionsCollection() throws Exception {
        test("Window", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _XMLDocument_HTMLDocument() throws Exception {
        test("XMLDocument", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _XMLDocument_HTMLFormElement() throws Exception {
        test("XMLDocument", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _XMLDocument_HTMLOptionsCollection() throws Exception {
        test("XMLDocument", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _XMLHttpRequest_HTMLDocument() throws Exception {
        test("XMLHttpRequest", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, IE8, CHROME })
    public void _XMLHttpRequest_HTMLFormElement() throws Exception {
        test("XMLHttpRequest", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _XMLHttpRequest_HTMLOptionsCollection() throws Exception {
        test("XMLHttpRequest", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _XMLSerializer_HTMLDocument() throws Exception {
        test("XMLSerializer", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _XMLSerializer_HTMLFormElement() throws Exception {
        test("XMLSerializer", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _XMLSerializer_HTMLOptionsCollection() throws Exception {
        test("XMLSerializer", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _XPathEvaluator_HTMLDocument() throws Exception {
        test("XPathEvaluator", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _XPathEvaluator_HTMLFormElement() throws Exception {
        test("XPathEvaluator", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _XPathEvaluator_HTMLOptionsCollection() throws Exception {
        test("XPathEvaluator", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _XPathNSResolver_HTMLDocument() throws Exception {
        test("XPathNSResolver", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _XPathNSResolver_HTMLFormElement() throws Exception {
        test("XPathNSResolver", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(FF31)
    public void _XPathNSResolver_HTMLOptionsCollection() throws Exception {
        test("XPathNSResolver", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _XPathResult_HTMLDocument() throws Exception {
        test("XPathResult", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _XPathResult_HTMLFormElement() throws Exception {
        test("XPathResult", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _XPathResult_HTMLOptionsCollection() throws Exception {
        test("XPathResult", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _XSLTProcessor_HTMLDocument() throws Exception {
        test("XSLTProcessor", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _XSLTProcessor_HTMLFormElement() throws Exception {
        test("XSLTProcessor", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ FF31, CHROME })
    public void _XSLTProcessor_HTMLOptionsCollection() throws Exception {
        test("XSLTProcessor", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _ActiveXObject_HTMLFormElement() throws Exception {
        test("ActiveXObject", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _ActiveXObject_NamedNodeMap() throws Exception {
        test("ActiveXObject", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _Attr_NamedNodeMap() throws Exception {
        test("Attr", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _CSSRuleList_NamedNodeMap() throws Exception {
        test("CSSRuleList", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _CSSStyleDeclaration_NamedNodeMap() throws Exception {
        test("CSSStyleDeclaration", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _CSSStyleRule_NamedNodeMap() throws Exception {
        test("CSSStyleRule", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _CSSStyleSheet_NamedNodeMap() throws Exception {
        test("CSSStyleSheet", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _DOMImplementation_NamedNodeMap() throws Exception {
        test("DOMImplementation", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _Element_NamedNodeMap() throws Exception {
        test("Element", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _Enumerator_HTMLFormElement() throws Exception {
        test("Enumerator", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _Enumerator_NamedNodeMap() throws Exception {
        test("Enumerator", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _Event_NamedNodeMap() throws Exception {
        test("Event", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLAnchorElement_NamedNodeMap() throws Exception {
        test("HTMLAnchorElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLAreaElement_NamedNodeMap() throws Exception {
        test("HTMLAreaElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _HTMLBGSoundElement_HTMLFormElement() throws Exception {
        test("HTMLBGSoundElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _HTMLBGSoundElement_NamedNodeMap() throws Exception {
        test("HTMLBGSoundElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLBRElement_NamedNodeMap() throws Exception {
        test("HTMLBRElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLBaseElement_NamedNodeMap() throws Exception {
        test("HTMLBaseElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _HTMLBaseFontElement_HTMLFormElement() throws Exception {
        test("HTMLBaseFontElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _HTMLBaseFontElement_NamedNodeMap() throws Exception {
        test("HTMLBaseFontElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _HTMLBlockElement_HTMLFormElement() throws Exception {
        test("HTMLBlockElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _HTMLBlockElement_NamedNodeMap() throws Exception {
        test("HTMLBlockElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLBodyElement_NamedNodeMap() throws Exception {
        test("HTMLBodyElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLButtonElement_NamedNodeMap() throws Exception {
        test("HTMLButtonElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLCollection_NamedNodeMap() throws Exception {
        test("HTMLCollection", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _HTMLCommentElement_HTMLFormElement() throws Exception {
        test("HTMLCommentElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _HTMLCommentElement_NamedNodeMap() throws Exception {
        test("HTMLCommentElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _HTMLDDElement_HTMLFormElement() throws Exception {
        test("HTMLDDElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _HTMLDDElement_NamedNodeMap() throws Exception {
        test("HTMLDDElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLDListElement_NamedNodeMap() throws Exception {
        test("HTMLDListElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _HTMLDTElement_HTMLFormElement() throws Exception {
        test("HTMLDTElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _HTMLDTElement_NamedNodeMap() throws Exception {
        test("HTMLDTElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLDivElement_NamedNodeMap() throws Exception {
        test("HTMLDivElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLDocument_NamedNodeMap() throws Exception {
        test("HTMLDocument", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLEmbedElement_NamedNodeMap() throws Exception {
        test("HTMLEmbedElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLFieldSetElement_NamedNodeMap() throws Exception {
        test("HTMLFieldSetElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLFontElement_NamedNodeMap() throws Exception {
        test("HTMLFontElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLFormElement_NamedNodeMap() throws Exception {
        test("HTMLFormElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLFrameElement_NamedNodeMap() throws Exception {
        test("HTMLFrameElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLFrameSetElement_NamedNodeMap() throws Exception {
        test("HTMLFrameSetElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLHRElement_NamedNodeMap() throws Exception {
        test("HTMLHRElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLHeadElement_NamedNodeMap() throws Exception {
        test("HTMLHeadElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLHeadingElement_NamedNodeMap() throws Exception {
        test("HTMLHeadingElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLHtmlElement_NamedNodeMap() throws Exception {
        test("HTMLHtmlElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLIFrameElement_NamedNodeMap() throws Exception {
        test("HTMLIFrameElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLImageElement_NamedNodeMap() throws Exception {
        test("HTMLImageElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLInputElement_NamedNodeMap() throws Exception {
        test("HTMLInputElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _HTMLIsIndexElement_HTMLFormElement() throws Exception {
        test("HTMLIsIndexElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _HTMLIsIndexElement_NamedNodeMap() throws Exception {
        test("HTMLIsIndexElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLLIElement_NamedNodeMap() throws Exception {
        test("HTMLLIElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLLabelElement_NamedNodeMap() throws Exception {
        test("HTMLLabelElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLLegendElement_NamedNodeMap() throws Exception {
        test("HTMLLegendElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLLinkElement_NamedNodeMap() throws Exception {
        test("HTMLLinkElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLMapElement_NamedNodeMap() throws Exception {
        test("HTMLMapElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLMarqueeElement_HTMLFormElement() throws Exception {
        test("HTMLMarqueeElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLMarqueeElement_NamedNodeMap() throws Exception {
        test("HTMLMarqueeElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLMetaElement_NamedNodeMap() throws Exception {
        test("HTMLMetaElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _HTMLNextIdElement_HTMLFormElement() throws Exception {
        test("HTMLNextIdElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _HTMLNextIdElement_NamedNodeMap() throws Exception {
        test("HTMLNextIdElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _HTMLNoShowElement_HTMLFormElement() throws Exception {
        test("HTMLNoShowElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _HTMLNoShowElement_NamedNodeMap() throws Exception {
        test("HTMLNoShowElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLOListElement_NamedNodeMap() throws Exception {
        test("HTMLOListElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLObjectElement_NamedNodeMap() throws Exception {
        test("HTMLObjectElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLOptionElement_NamedNodeMap() throws Exception {
        test("HTMLOptionElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLParagraphElement_NamedNodeMap() throws Exception {
        test("HTMLParagraphElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLParamElement_NamedNodeMap() throws Exception {
        test("HTMLParamElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _HTMLPhraseElement_HTMLFormElement() throws Exception {
        test("HTMLPhraseElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _HTMLPhraseElement_NamedNodeMap() throws Exception {
        test("HTMLPhraseElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLScriptElement_NamedNodeMap() throws Exception {
        test("HTMLScriptElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLSelectElement_NamedNodeMap() throws Exception {
        test("HTMLSelectElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLSpanElement_NamedNodeMap() throws Exception {
        test("HTMLSpanElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLStyleElement_NamedNodeMap() throws Exception {
        test("HTMLStyleElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLTableCaptionElement_NamedNodeMap() throws Exception {
        test("HTMLTableCaptionElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLTableCellElement_NamedNodeMap() throws Exception {
        test("HTMLTableCellElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLTableColElement_NamedNodeMap() throws Exception {
        test("HTMLTableColElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLTableElement_NamedNodeMap() throws Exception {
        test("HTMLTableElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLTableRowElement_NamedNodeMap() throws Exception {
        test("HTMLTableRowElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLTableSectionElement_NamedNodeMap() throws Exception {
        test("HTMLTableSectionElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLTextAreaElement_NamedNodeMap() throws Exception {
        test("HTMLTextAreaElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _HTMLTextElement_HTMLFormElement() throws Exception {
        test("HTMLTextElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _HTMLTextElement_NamedNodeMap() throws Exception {
        test("HTMLTextElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLTitleElement_NamedNodeMap() throws Exception {
        test("HTMLTitleElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _HTMLUListElement_NamedNodeMap() throws Exception {
        test("HTMLUListElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _History_NamedNodeMap() throws Exception {
        test("History", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _Image_NamedNodeMap() throws Exception {
        test("Image", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _Location_NamedNodeMap() throws Exception {
        test("Location", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _NamedNodeMap_HTMLFormElement() throws Exception {
        test("NamedNodeMap", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _Navigator_NamedNodeMap() throws Exception {
        test("Navigator", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _NodeList_NamedNodeMap() throws Exception {
        test("NodeList", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _Option_NamedNodeMap() throws Exception {
        test("Option", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _Screen_NamedNodeMap() throws Exception {
        test("Screen", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _Selection_NamedNodeMap() throws Exception {
        test("Selection", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _StaticNodeList_NamedNodeMap() throws Exception {
        test("StaticNodeList", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _Storage_NamedNodeMap() throws Exception {
        test("Storage", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _StyleSheetList_NamedNodeMap() throws Exception {
        test("StyleSheetList", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _TextRange_HTMLFormElement() throws Exception {
        test("TextRange", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(IE8)
    public void _TextRange_NamedNodeMap() throws Exception {
        test("TextRange", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _Text_NamedNodeMap() throws Exception {
        test("Text", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _Window_NamedNodeMap() throws Exception {
        test("Window", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ IE8, CHROME })
    public void _XMLHttpRequest_NamedNodeMap() throws Exception {
        test("XMLHttpRequest", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _ApplicationCache_HTMLDocument() throws Exception {
        test("ApplicationCache", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _ApplicationCache_HTMLFormElement() throws Exception {
        test("ApplicationCache", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _ApplicationCache_HTMLOptionsCollection() throws Exception {
        test("ApplicationCache", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _ApplicationCache_NamedNodeMap() throws Exception {
        test("ApplicationCache", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _ArrayBuffer_NamedNodeMap() throws Exception {
        test("ArrayBuffer", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _BeforeUnloadEvent_NamedNodeMap() throws Exception {
        test("BeforeUnloadEvent", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _CDATASection_NamedNodeMap() throws Exception {
        test("CDATASection", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _CSSFontFaceRule_NamedNodeMap() throws Exception {
        test("CSSFontFaceRule", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _CSSImportRule_NamedNodeMap() throws Exception {
        test("CSSImportRule", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _CSSMediaRule_NamedNodeMap() throws Exception {
        test("CSSMediaRule", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _CSSRule_NamedNodeMap() throws Exception {
        test("CSSRule", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _CanvasRenderingContext2D_NamedNodeMap() throws Exception {
        test("CanvasRenderingContext2D", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _ClientRect_HTMLDocument() throws Exception {
        test("ClientRect", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _ClientRect_HTMLFormElement() throws Exception {
        test("ClientRect", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _ClientRect_HTMLOptionsCollection() throws Exception {
        test("ClientRect", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _ClientRect_NamedNodeMap() throws Exception {
        test("ClientRect", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _Comment_NamedNodeMap() throws Exception {
        test("Comment", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _DOMException_NamedNodeMap() throws Exception {
        test("DOMException", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _DOMParser_NamedNodeMap() throws Exception {
        test("DOMParser", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _DOMStringMap_ApplicationCache() throws Exception {
        test("DOMStringMap", "ApplicationCache");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _DOMStringMap_ClientRect() throws Exception {
        test("DOMStringMap", "ClientRect");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _DOMStringMap_HTMLAllCollection() throws Exception {
        test("DOMStringMap", "HTMLAllCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _DOMStringMap_HTMLDetailsElement() throws Exception {
        test("DOMStringMap", "HTMLDetailsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _DOMStringMap_HTMLDialogElement() throws Exception {
        test("DOMStringMap", "HTMLDialogElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _DOMStringMap_HTMLKeygenElement() throws Exception {
        test("DOMStringMap", "HTMLKeygenElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _DOMStringMap_HTMLMarqueeElement() throws Exception {
        test("DOMStringMap", "HTMLMarqueeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _DOMStringMap_NamedNodeMap() throws Exception {
        test("DOMStringMap", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _DOMStringMap_SVGCursorElement() throws Exception {
        test("DOMStringMap", "SVGCursorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _DOMTokenList_NamedNodeMap() throws Exception {
        test("DOMTokenList", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _DataView_NamedNodeMap() throws Exception {
        test("DataView", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _DocumentFragment_NamedNodeMap() throws Exception {
        test("DocumentFragment", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _DocumentType_NamedNodeMap() throws Exception {
        test("DocumentType", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _Document_NamedNodeMap() throws Exception {
        test("Document", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _Float32Array_NamedNodeMap() throws Exception {
        test("Float32Array", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _Float64Array_NamedNodeMap() throws Exception {
        test("Float64Array", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLAllCollection_HTMLDocument() throws Exception {
        test("HTMLAllCollection", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLAllCollection_HTMLFormElement() throws Exception {
        test("HTMLAllCollection", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLAllCollection_HTMLOptionsCollection() throws Exception {
        test("HTMLAllCollection", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLAllCollection_NamedNodeMap() throws Exception {
        test("HTMLAllCollection", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLAppletElement_NamedNodeMap() throws Exception {
        test("HTMLAppletElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLAudioElement_NamedNodeMap() throws Exception {
        test("HTMLAudioElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLCanvasElement_NamedNodeMap() throws Exception {
        test("HTMLCanvasElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLDataListElement_NamedNodeMap() throws Exception {
        test("HTMLDataListElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLDetailsElement_HTMLDocument() throws Exception {
        test("HTMLDetailsElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLDetailsElement_HTMLFormElement() throws Exception {
        test("HTMLDetailsElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLDetailsElement_HTMLOptionsCollection() throws Exception {
        test("HTMLDetailsElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLDetailsElement_NamedNodeMap() throws Exception {
        test("HTMLDetailsElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLDialogElement_HTMLDocument() throws Exception {
        test("HTMLDialogElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLDialogElement_HTMLFormElement() throws Exception {
        test("HTMLDialogElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLDialogElement_HTMLOptionsCollection() throws Exception {
        test("HTMLDialogElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLDialogElement_NamedNodeMap() throws Exception {
        test("HTMLDialogElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLDirectoryElement_NamedNodeMap() throws Exception {
        test("HTMLDirectoryElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLElement_NamedNodeMap() throws Exception {
        test("HTMLElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLKeygenElement_HTMLDocument() throws Exception {
        test("HTMLKeygenElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLKeygenElement_HTMLFormElement() throws Exception {
        test("HTMLKeygenElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLKeygenElement_HTMLOptionsCollection() throws Exception {
        test("HTMLKeygenElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLKeygenElement_NamedNodeMap() throws Exception {
        test("HTMLKeygenElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLMarqueeElement_HTMLDocument() throws Exception {
        test("HTMLMarqueeElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLMarqueeElement_HTMLOptionsCollection() throws Exception {
        test("HTMLMarqueeElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLMediaElement_NamedNodeMap() throws Exception {
        test("HTMLMediaElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLMenuElement_NamedNodeMap() throws Exception {
        test("HTMLMenuElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLMeterElement_NamedNodeMap() throws Exception {
        test("HTMLMeterElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLModElement_NamedNodeMap() throws Exception {
        test("HTMLModElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLOptGroupElement_NamedNodeMap() throws Exception {
        test("HTMLOptGroupElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLOptionsCollection_NamedNodeMap() throws Exception {
        test("HTMLOptionsCollection", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLOutputElement_NamedNodeMap() throws Exception {
        test("HTMLOutputElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLPreElement_NamedNodeMap() throws Exception {
        test("HTMLPreElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLProgressElement_NamedNodeMap() throws Exception {
        test("HTMLProgressElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLQuoteElement_NamedNodeMap() throws Exception {
        test("HTMLQuoteElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLSourceElement_NamedNodeMap() throws Exception {
        test("HTMLSourceElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLTrackElement_NamedNodeMap() throws Exception {
        test("HTMLTrackElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLUnknownElement_NamedNodeMap() throws Exception {
        test("HTMLUnknownElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HTMLVideoElement_NamedNodeMap() throws Exception {
        test("HTMLVideoElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _HashChangeEvent_NamedNodeMap() throws Exception {
        test("HashChangeEvent", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _Int16Array_NamedNodeMap() throws Exception {
        test("Int16Array", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _Int32Array_NamedNodeMap() throws Exception {
        test("Int32Array", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _Int8Array_NamedNodeMap() throws Exception {
        test("Int8Array", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _KeyboardEvent_NamedNodeMap() throws Exception {
        test("KeyboardEvent", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _MediaList_NamedNodeMap() throws Exception {
        test("MediaList", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _MessageEvent_NamedNodeMap() throws Exception {
        test("MessageEvent", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _MimeTypeArray_NamedNodeMap() throws Exception {
        test("MimeTypeArray", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _MimeType_NamedNodeMap() throws Exception {
        test("MimeType", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _MouseEvent_NamedNodeMap() throws Exception {
        test("MouseEvent", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _MutationEvent_NamedNodeMap() throws Exception {
        test("MutationEvent", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _NamedNodeMap_HTMLDocument() throws Exception {
        test("NamedNodeMap", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _NamedNodeMap_HTMLOptionsCollection() throws Exception {
        test("NamedNodeMap", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _NodeFilter_NamedNodeMap() throws Exception {
        test("NodeFilter", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _Node_NamedNodeMap() throws Exception {
        test("Node", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _Notification_NamedNodeMap() throws Exception {
        test("Notification", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _PluginArray_NamedNodeMap() throws Exception {
        test("PluginArray", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _Plugin_NamedNodeMap() throws Exception {
        test("Plugin", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _ProcessingInstruction_NamedNodeMap() throws Exception {
        test("ProcessingInstruction", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _Range_NamedNodeMap() throws Exception {
        test("Range", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGAElement_NamedNodeMap() throws Exception {
        test("SVGAElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGAngle_NamedNodeMap() throws Exception {
        test("SVGAngle", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGAnimateElement_NamedNodeMap() throws Exception {
        test("SVGAnimateElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGAnimateMotionElement_NamedNodeMap() throws Exception {
        test("SVGAnimateMotionElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGAnimateTransformElement_NamedNodeMap() throws Exception {
        test("SVGAnimateTransformElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGCircleElement_NamedNodeMap() throws Exception {
        test("SVGCircleElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGClipPathElement_NamedNodeMap() throws Exception {
        test("SVGClipPathElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGCursorElement_HTMLDocument() throws Exception {
        test("SVGCursorElement", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGCursorElement_HTMLFormElement() throws Exception {
        test("SVGCursorElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGCursorElement_HTMLOptionsCollection() throws Exception {
        test("SVGCursorElement", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGCursorElement_NamedNodeMap() throws Exception {
        test("SVGCursorElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGDefsElement_NamedNodeMap() throws Exception {
        test("SVGDefsElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGDescElement_NamedNodeMap() throws Exception {
        test("SVGDescElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGElement_NamedNodeMap() throws Exception {
        test("SVGElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGEllipseElement_NamedNodeMap() throws Exception {
        test("SVGEllipseElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFEBlendElement_NamedNodeMap() throws Exception {
        test("SVGFEBlendElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFEColorMatrixElement_NamedNodeMap() throws Exception {
        test("SVGFEColorMatrixElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFEComponentTransferElement_NamedNodeMap() throws Exception {
        test("SVGFEComponentTransferElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFECompositeElement_NamedNodeMap() throws Exception {
        test("SVGFECompositeElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFEConvolveMatrixElement_NamedNodeMap() throws Exception {
        test("SVGFEConvolveMatrixElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFEDiffuseLightingElement_NamedNodeMap() throws Exception {
        test("SVGFEDiffuseLightingElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFEDisplacementMapElement_NamedNodeMap() throws Exception {
        test("SVGFEDisplacementMapElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFEDistantLightElement_NamedNodeMap() throws Exception {
        test("SVGFEDistantLightElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFEFloodElement_NamedNodeMap() throws Exception {
        test("SVGFEFloodElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFEFuncAElement_NamedNodeMap() throws Exception {
        test("SVGFEFuncAElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFEFuncBElement_NamedNodeMap() throws Exception {
        test("SVGFEFuncBElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFEFuncGElement_NamedNodeMap() throws Exception {
        test("SVGFEFuncGElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFEFuncRElement_NamedNodeMap() throws Exception {
        test("SVGFEFuncRElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFEGaussianBlurElement_NamedNodeMap() throws Exception {
        test("SVGFEGaussianBlurElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFEImageElement_NamedNodeMap() throws Exception {
        test("SVGFEImageElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFEMergeElement_NamedNodeMap() throws Exception {
        test("SVGFEMergeElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFEMergeNodeElement_NamedNodeMap() throws Exception {
        test("SVGFEMergeNodeElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFEMorphologyElement_NamedNodeMap() throws Exception {
        test("SVGFEMorphologyElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFEOffsetElement_NamedNodeMap() throws Exception {
        test("SVGFEOffsetElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFEPointLightElement_NamedNodeMap() throws Exception {
        test("SVGFEPointLightElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFESpecularLightingElement_NamedNodeMap() throws Exception {
        test("SVGFESpecularLightingElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFESpotLightElement_NamedNodeMap() throws Exception {
        test("SVGFESpotLightElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFETileElement_NamedNodeMap() throws Exception {
        test("SVGFETileElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFETurbulenceElement_NamedNodeMap() throws Exception {
        test("SVGFETurbulenceElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGFilterElement_NamedNodeMap() throws Exception {
        test("SVGFilterElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGForeignObjectElement_NamedNodeMap() throws Exception {
        test("SVGForeignObjectElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGGElement_NamedNodeMap() throws Exception {
        test("SVGGElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGImageElement_NamedNodeMap() throws Exception {
        test("SVGImageElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGLineElement_NamedNodeMap() throws Exception {
        test("SVGLineElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGLinearGradientElement_NamedNodeMap() throws Exception {
        test("SVGLinearGradientElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGMPathElement_NamedNodeMap() throws Exception {
        test("SVGMPathElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGMarkerElement_NamedNodeMap() throws Exception {
        test("SVGMarkerElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGMaskElement_NamedNodeMap() throws Exception {
        test("SVGMaskElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGMatrix_NamedNodeMap() throws Exception {
        test("SVGMatrix", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGMetadataElement_NamedNodeMap() throws Exception {
        test("SVGMetadataElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGPathElement_NamedNodeMap() throws Exception {
        test("SVGPathElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGPatternElement_NamedNodeMap() throws Exception {
        test("SVGPatternElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGPolygonElement_NamedNodeMap() throws Exception {
        test("SVGPolygonElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGPolylineElement_NamedNodeMap() throws Exception {
        test("SVGPolylineElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGRadialGradientElement_NamedNodeMap() throws Exception {
        test("SVGRadialGradientElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGRectElement_NamedNodeMap() throws Exception {
        test("SVGRectElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGRect_NamedNodeMap() throws Exception {
        test("SVGRect", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGSVGElement_NamedNodeMap() throws Exception {
        test("SVGSVGElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGScriptElement_NamedNodeMap() throws Exception {
        test("SVGScriptElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGSetElement_NamedNodeMap() throws Exception {
        test("SVGSetElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGStopElement_NamedNodeMap() throws Exception {
        test("SVGStopElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGStyleElement_NamedNodeMap() throws Exception {
        test("SVGStyleElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGSwitchElement_NamedNodeMap() throws Exception {
        test("SVGSwitchElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGSymbolElement_NamedNodeMap() throws Exception {
        test("SVGSymbolElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGTSpanElement_NamedNodeMap() throws Exception {
        test("SVGTSpanElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGTextElement_NamedNodeMap() throws Exception {
        test("SVGTextElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGTextPathElement_NamedNodeMap() throws Exception {
        test("SVGTextPathElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGTitleElement_NamedNodeMap() throws Exception {
        test("SVGTitleElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGUseElement_NamedNodeMap() throws Exception {
        test("SVGUseElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _SVGViewElement_NamedNodeMap() throws Exception {
        test("SVGViewElement", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _TreeWalker_NamedNodeMap() throws Exception {
        test("TreeWalker", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _UIEvent_NamedNodeMap() throws Exception {
        test("UIEvent", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _Uint16Array_NamedNodeMap() throws Exception {
        test("Uint16Array", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _Uint32Array_NamedNodeMap() throws Exception {
        test("Uint32Array", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _Uint8Array_NamedNodeMap() throws Exception {
        test("Uint8Array", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _Uint8ClampedArray_NamedNodeMap() throws Exception {
        test("Uint8ClampedArray", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _WebSocket_NamedNodeMap() throws Exception {
        test("WebSocket", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _XMLDocument_NamedNodeMap() throws Exception {
        test("XMLDocument", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _XMLSerializer_NamedNodeMap() throws Exception {
        test("XMLSerializer", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _XPathEvaluator_NamedNodeMap() throws Exception {
        test("XPathEvaluator", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _XPathResult_NamedNodeMap() throws Exception {
        test("XPathResult", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented(CHROME)
    public void _XSLTProcessor_NamedNodeMap() throws Exception {
        test("XSLTProcessor", "NamedNodeMap");
    }
}
