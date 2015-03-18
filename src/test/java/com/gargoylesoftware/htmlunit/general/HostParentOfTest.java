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
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE11;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Ignore;
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
    @Ignore
    public void isParentOf() throws Exception {
        isParentOf(parent_, child_);
    }

    private void isParentOf(final String parent, final String child) throws Exception {
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
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            CHROME = "false")
    public void _Image_HTMLImageElement() throws Exception {
        //although Image != HTMLImageElement, they seem to be synonyms!!!
        isParentOf("Image", "HTMLImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLImageElement_Image() throws Exception {
        //although Image != HTMLImageElement, they seem to be synonyms!!!
        isParentOf("HTMLImageElement", "Image");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            CHROME = "false")
    public void _Option_HTMLOptionElement() throws Exception {
        //although Option != HTMLOptionElement, they seem to be synonyms!!!
        isParentOf("Option", "HTMLOptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLOptionElement_Option() throws Exception {
        //although Option != HTMLOptionElement, they seem to be synonyms!!!
        isParentOf("HTMLOptionElement", "Option");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _ArrayBuffer_ArrayBuffer() throws Exception {
        isParentOf("ArrayBuffer", "ArrayBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferView_ArrayBufferView() throws Exception {
        isParentOf("ArrayBufferView", "ArrayBufferView");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferViewBase_ArrayBufferViewBase() throws Exception {
        isParentOf("ArrayBufferViewBase", "ArrayBufferViewBase");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Attr_Attr() throws Exception {
        isParentOf("Attr", "Attr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _ActiveXObject_ActiveXObject() throws Exception {
        isParentOf("ActiveXObject", "ActiveXObject");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false",
            IE8 = "false")
    public void _ApplicationCache_ApplicationCache() throws Exception {
        isParentOf("ApplicationCache", "ApplicationCache");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _BeforeUnloadEvent_BeforeUnloadEvent() throws Exception {
        isParentOf("BeforeUnloadEvent", "BeforeUnloadEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _BoxObject_BoxObject() throws Exception {
        isParentOf("BoxObject", "BoxObject");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CDATASection_CDATASection() throws Exception {
        isParentOf("CDATASection", "CDATASection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ClipboardData_ClipboardData() throws Exception {
        isParentOf("ClipboardData", "ClipboardData");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSS2Properties_CSS2Properties() throws Exception {
        isParentOf("CSS2Properties", "CSS2Properties");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSSCharsetRule_CSSCharsetRule() throws Exception {
        isParentOf("CSSCharsetRule", "CSSCharsetRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CSSFontFaceRule_CSSFontFaceRule() throws Exception {
        isParentOf("CSSFontFaceRule", "CSSFontFaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CSSImportRule_CSSImportRule() throws Exception {
        isParentOf("CSSImportRule", "CSSImportRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CSSMediaRule_CSSMediaRule() throws Exception {
        isParentOf("CSSMediaRule", "CSSMediaRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSSPrimitiveValue_CSSPrimitiveValue() throws Exception {
        isParentOf("CSSPrimitiveValue", "CSSPrimitiveValue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CSSRule_CSSRule() throws Exception {
        isParentOf("CSSRule", "CSSRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSRuleList_CSSRuleList() throws Exception {
        isParentOf("CSSRuleList", "CSSRuleList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSStyleDeclaration_CSSStyleDeclaration() throws Exception {
        isParentOf("CSSStyleDeclaration", "CSSStyleDeclaration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSStyleRule_CSSStyleRule() throws Exception {
        isParentOf("CSSStyleRule", "CSSStyleRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSStyleSheet_CSSStyleSheet() throws Exception {
        isParentOf("CSSStyleSheet", "CSSStyleSheet");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSSValue_CSSValue() throws Exception {
        isParentOf("CSSValue", "CSSValue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CanvasRenderingContext2D_CanvasRenderingContext2D() throws Exception {
        isParentOf("CanvasRenderingContext2D", "CanvasRenderingContext2D");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CharacterDataImpl_CharacterDataImpl() throws Exception {
        isParentOf("CharacterDataImpl", "CharacterDataImpl");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false",
            IE8 = "false")
    public void _ClientRect_ClientRect() throws Exception {
        isParentOf("ClientRect", "ClientRect");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Comment_Comment() throws Exception {
        isParentOf("Comment", "Comment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ComputedCSSStyleDeclaration_ComputedCSSStyleDeclaration() throws Exception {
        isParentOf("ComputedCSSStyleDeclaration", "ComputedCSSStyleDeclaration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Console_Console() throws Exception {
        isParentOf("Console", "Console");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Coordinates_Coordinates() throws Exception {
        isParentOf("Coordinates", "Coordinates");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _DataView_DataView() throws Exception {
        isParentOf("DataView", "DataView");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _DOMException_DOMException() throws Exception {
        isParentOf("DOMException", "DOMException");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _DOMImplementation_DOMImplementation() throws Exception {
        isParentOf("DOMImplementation", "DOMImplementation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _DOMParser_DOMParser() throws Exception {
        isParentOf("DOMParser", "DOMParser");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void _DOMStringMap_DOMStringMap() throws Exception {
        isParentOf("DOMStringMap", "DOMStringMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _DOMTokenList_DOMTokenList() throws Exception {
        isParentOf("DOMTokenList", "DOMTokenList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Document_Document() throws Exception {
        isParentOf("Document", "Document");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _DocumentFragment_DocumentFragment() throws Exception {
        isParentOf("DocumentFragment", "DocumentFragment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _DocumentType_DocumentType() throws Exception {
        isParentOf("DocumentType", "DocumentType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_Element() throws Exception {
        isParentOf("Element", "Element");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Enumerator_Enumerator() throws Exception {
        isParentOf("Enumerator", "Enumerator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Event_Event() throws Exception {
        isParentOf("Event", "Event");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _EventNode_EventNode() throws Exception {
        isParentOf("EventNode", "EventNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _External_External() throws Exception {
        isParentOf("External", "External");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Float32Array_Float32Array() throws Exception {
        isParentOf("Float32Array", "Float32Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Float64Array_Float64Array() throws Exception {
        isParentOf("Float64Array", "Float64Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _FormChild_FormChild() throws Exception {
        isParentOf("FormChild", "FormChild");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _FormField_FormField() throws Exception {
        isParentOf("FormField", "FormField");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Geolocation_Geolocation() throws Exception {
        isParentOf("Geolocation", "Geolocation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HashChangeEvent_HashChangeEvent() throws Exception {
        isParentOf("HashChangeEvent", "HashChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _History_History() throws Exception {
        isParentOf("History", "History");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false",
            IE8 = "false")
    public void _HTMLAllCollection_HTMLAllCollection() throws Exception {
        isParentOf("HTMLAllCollection", "HTMLAllCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLAnchorElement_HTMLAnchorElement() throws Exception {
        isParentOf("HTMLAnchorElement", "HTMLAnchorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLAppletElement_HTMLAppletElement() throws Exception {
        isParentOf("HTMLAppletElement", "HTMLAppletElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLAreaElement_HTMLAreaElement() throws Exception {
        isParentOf("HTMLAreaElement", "HTMLAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLAudioElement_HTMLAudioElement() throws Exception {
        isParentOf("HTMLAudioElement", "HTMLAudioElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLBGSoundElement_HTMLBGSoundElement() throws Exception {
        isParentOf("HTMLBGSoundElement", "HTMLBGSoundElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLBRElement_HTMLBRElement() throws Exception {
        isParentOf("HTMLBRElement", "HTMLBRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLBaseElement_HTMLBaseElement() throws Exception {
        isParentOf("HTMLBaseElement", "HTMLBaseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLBaseFontElement_HTMLBaseFontElement() throws Exception {
        isParentOf("HTMLBaseFontElement", "HTMLBaseFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLBlockElement_HTMLBlockElement() throws Exception {
        isParentOf("HTMLBlockElement", "HTMLBlockElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLQuoteElement_HTMLQuoteElement() throws Exception {
        isParentOf("HTMLQuoteElement", "HTMLQuoteElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLBodyElement_HTMLBodyElement() throws Exception {
        isParentOf("HTMLBodyElement", "HTMLBodyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLButtonElement_HTMLButtonElement() throws Exception {
        isParentOf("HTMLButtonElement", "HTMLButtonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLCanvasElement_HTMLCanvasElement() throws Exception {
        isParentOf("HTMLCanvasElement", "HTMLCanvasElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLCollection_HTMLCollection() throws Exception {
        isParentOf("HTMLCollection", "HTMLCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE11 = "false")
    @NotYetImplemented({ CHROME, FF, IE8 })
    public void _HTMLCollection_HTMLOptionsCollection() throws Exception {
        isParentOf("HTMLCollection", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE8 = "true")
    public void _HTMLCommentElement_HTMLCommentElement() throws Exception {
        isParentOf("HTMLCommentElement", "HTMLCommentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLDataListElement_HTMLDataListElement() throws Exception {
        isParentOf("HTMLDataListElement", "HTMLDataListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLDDElement_HTMLDDElement() throws Exception {
        isParentOf("HTMLDDElement", "HTMLDDElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _HTMLDetailsElement_HTMLDetailsElement() throws Exception {
        isParentOf("HTMLDetailsElement", "HTMLDetailsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _HTMLDialogElement_HTMLDialogElement() throws Exception {
        isParentOf("HTMLDialogElement", "HTMLDialogElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLDTElement_HTMLDTElement() throws Exception {
        isParentOf("HTMLDTElement", "HTMLDTElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLDListElement_HTMLDListElement() throws Exception {
        isParentOf("HTMLDListElement", "HTMLDListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLDirectoryElement_HTMLDirectoryElement() throws Exception {
        isParentOf("HTMLDirectoryElement", "HTMLDirectoryElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLDivElement_HTMLDivElement() throws Exception {
        isParentOf("HTMLDivElement", "HTMLDivElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLDocument_HTMLDocument() throws Exception {
        isParentOf("HTMLDocument", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLElement() throws Exception {
        isParentOf("HTMLElement", "HTMLElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLEmbedElement_HTMLEmbedElement() throws Exception {
        isParentOf("HTMLEmbedElement", "HTMLEmbedElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLFieldSetElement_HTMLFieldSetElement() throws Exception {
        isParentOf("HTMLFieldSetElement", "HTMLFieldSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLFontElement_HTMLFontElement() throws Exception {
        isParentOf("HTMLFontElement", "HTMLFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLFormElement_HTMLFormElement() throws Exception {
        isParentOf("HTMLFormElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLFrameElement_HTMLFrameElement() throws Exception {
        isParentOf("HTMLFrameElement", "HTMLFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLFrameSetElement_HTMLFrameSetElement() throws Exception {
        isParentOf("HTMLFrameSetElement", "HTMLFrameSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLHRElement_HTMLHRElement() throws Exception {
        isParentOf("HTMLHRElement", "HTMLHRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLHeadElement_HTMLHeadElement() throws Exception {
        isParentOf("HTMLHeadElement", "HTMLHeadElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLHeadingElement_HTMLHeadingElement() throws Exception {
        isParentOf("HTMLHeadingElement", "HTMLHeadingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLHtmlElement_HTMLHtmlElement() throws Exception {
        isParentOf("HTMLHtmlElement", "HTMLHtmlElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLIFrameElement_HTMLIFrameElement() throws Exception {
        isParentOf("HTMLIFrameElement", "HTMLIFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLImageElement_HTMLImageElement() throws Exception {
        isParentOf("HTMLImageElement", "HTMLImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLInlineQuotationElement_HTMLInlineQuotationElement() throws Exception {
        isParentOf("HTMLInlineQuotationElement", "HTMLInlineQuotationElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLInputElement_HTMLInputElement() throws Exception {
        isParentOf("HTMLInputElement", "HTMLInputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLIsIndexElement_HTMLIsIndexElement() throws Exception {
        isParentOf("HTMLIsIndexElement", "HTMLIsIndexElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _HTMLKeygenElement_HTMLKeygenElement() throws Exception {
        isParentOf("HTMLKeygenElement", "HTMLKeygenElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLLIElement_HTMLLIElement() throws Exception {
        isParentOf("HTMLLIElement", "HTMLLIElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLLabelElement_HTMLLabelElement() throws Exception {
        isParentOf("HTMLLabelElement", "HTMLLabelElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLLegendElement_HTMLLegendElement() throws Exception {
        isParentOf("HTMLLegendElement", "HTMLLegendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLLinkElement_HTMLLinkElement() throws Exception {
        isParentOf("HTMLLinkElement", "HTMLLinkElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLListElement_HTMLListElement() throws Exception {
        isParentOf("HTMLListElement", "HTMLListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLMapElement_HTMLMapElement() throws Exception {
        isParentOf("HTMLMapElement", "HTMLMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false")
    public void _HTMLMarqueeElement_HTMLMarqueeElement() throws Exception {
        isParentOf("HTMLMarqueeElement", "HTMLMarqueeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLMediaElement_HTMLMediaElement() throws Exception {
        isParentOf("HTMLMediaElement", "HTMLMediaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLMenuElement_HTMLMenuElement() throws Exception {
        isParentOf("HTMLMenuElement", "HTMLMenuElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _HTMLMenuItemElement_HTMLMenuItemElement() throws Exception {
        isParentOf("HTMLMenuItemElement", "HTMLMenuItemElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLMetaElement_HTMLMetaElement() throws Exception {
        isParentOf("HTMLMetaElement", "HTMLMetaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLMeterElement_HTMLMeterElement() throws Exception {
        isParentOf("HTMLMeterElement", "HTMLMeterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLModElement_HTMLModElement() throws Exception {
        isParentOf("HTMLModElement", "HTMLModElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE8 = "false")
    @NotYetImplemented(IE8)
    public void _HTMLNoShowElement_HTMLNoShowElement() throws Exception {
        isParentOf("HTMLNoShowElement", "HTMLNoShowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLNextIdElement_HTMLNextIdElement() throws Exception {
        isParentOf("HTMLNextIdElement", "HTMLNextIdElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLOListElement_HTMLOListElement() throws Exception {
        isParentOf("HTMLOListElement", "HTMLOListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLObjectElement_HTMLObjectElement() throws Exception {
        isParentOf("HTMLObjectElement", "HTMLObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLOptGroupElement_HTMLOptGroupElement() throws Exception {
        isParentOf("HTMLOptGroupElement", "HTMLOptGroupElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLOptionElement_HTMLOptionElement() throws Exception {
        isParentOf("HTMLOptionElement", "HTMLOptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLOptionsCollection_HTMLOptionsCollection() throws Exception {
        isParentOf("HTMLOptionsCollection", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLOutputElement_HTMLOutputElement() throws Exception {
        isParentOf("HTMLOutputElement", "HTMLOutputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLParagraphElement_HTMLParagraphElement() throws Exception {
        isParentOf("HTMLParagraphElement", "HTMLParagraphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLParamElement_HTMLParamElement() throws Exception {
        isParentOf("HTMLParamElement", "HTMLParamElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLPhraseElement_HTMLPhraseElement() throws Exception {
        isParentOf("HTMLPhraseElement", "HTMLPhraseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLPreElement_HTMLPreElement() throws Exception {
        isParentOf("HTMLPreElement", "HTMLPreElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLProgressElement_HTMLProgressElement() throws Exception {
        isParentOf("HTMLProgressElement", "HTMLProgressElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLScriptElement_HTMLScriptElement() throws Exception {
        isParentOf("HTMLScriptElement", "HTMLScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLSelectElement_HTMLSelectElement() throws Exception {
        isParentOf("HTMLSelectElement", "HTMLSelectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLSourceElement_HTMLSourceElement() throws Exception {
        isParentOf("HTMLSourceElement", "HTMLSourceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLSpanElement_HTMLSpanElement() throws Exception {
        isParentOf("HTMLSpanElement", "HTMLSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLStyleElement_HTMLStyleElement() throws Exception {
        isParentOf("HTMLStyleElement", "HTMLStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTableCaptionElement_HTMLTableCaptionElement() throws Exception {
        isParentOf("HTMLTableCaptionElement", "HTMLTableCaptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTableCellElement_HTMLTableCellElement() throws Exception {
        isParentOf("HTMLTableCellElement", "HTMLTableCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTableColElement_HTMLTableColElement() throws Exception {
        isParentOf("HTMLTableColElement", "HTMLTableColElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLTableComponent_HTMLTableComponent() throws Exception {
        isParentOf("HTMLTableComponent", "HTMLTableComponent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLTableDataCellElement_HTMLTableDataCellElement() throws Exception {
        isParentOf("HTMLTableDataCellElement", "HTMLTableDataCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTableElement_HTMLTableElement() throws Exception {
        isParentOf("HTMLTableElement", "HTMLTableElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLTableHeaderCellElement_HTMLTableHeaderCellElement() throws Exception {
        isParentOf("HTMLTableHeaderCellElement", "HTMLTableHeaderCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTableRowElement_HTMLTableRowElement() throws Exception {
        isParentOf("HTMLTableRowElement", "HTMLTableRowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTableSectionElement_HTMLTableSectionElement() throws Exception {
        isParentOf("HTMLTableSectionElement", "HTMLTableSectionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE8 = "false")
    @NotYetImplemented(IE8)
    public void _HTMLTextElement_HTMLTextElement() throws Exception {
        isParentOf("HTMLTextElement", "HTMLTextElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTextAreaElement_HTMLTextAreaElement() throws Exception {
        isParentOf("HTMLTextAreaElement", "HTMLTextAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _HTMLTimeElement_HTMLTimeElement() throws Exception {
        isParentOf("HTMLTimeElement", "HTMLTimeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTitleElement_HTMLTitleElement() throws Exception {
        isParentOf("HTMLTitleElement", "HTMLTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLTrackElement_HTMLTrackElement() throws Exception {
        isParentOf("HTMLTrackElement", "HTMLTrackElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLUListElement_HTMLUListElement() throws Exception {
        isParentOf("HTMLUListElement", "HTMLUListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _HTMLUnknownElement_HTMLUnknownElement() throws Exception {
        isParentOf("HTMLUnknownElement", "HTMLUnknownElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLVideoElement_HTMLVideoElement() throws Exception {
        isParentOf("HTMLVideoElement", "HTMLVideoElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Image_Image() throws Exception {
        isParentOf("Image", "Image");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Int16Array_Int16Array() throws Exception {
        isParentOf("Int16Array", "Int16Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Int32Array_Int32Array() throws Exception {
        isParentOf("Int32Array", "Int32Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Int8Array_Int8Array() throws Exception {
        isParentOf("Int8Array", "Int8Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _KeyboardEvent_KeyboardEvent() throws Exception {
        isParentOf("KeyboardEvent", "KeyboardEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false")
    @NotYetImplemented(FF)
    public void _Location_Location() throws Exception {
        isParentOf("Location", "Location");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _MediaList_MediaList() throws Exception {
        isParentOf("MediaList", "MediaList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _MessageEvent_MessageEvent() throws Exception {
        isParentOf("MessageEvent", "MessageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _MimeType_MimeType() throws Exception {
        isParentOf("MimeType", "MimeType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _MimeTypeArray_MimeTypeArray() throws Exception {
        isParentOf("MimeTypeArray", "MimeTypeArray");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _MouseEvent_MouseEvent() throws Exception {
        isParentOf("MouseEvent", "MouseEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _MutationEvent_MutationEvent() throws Exception {
        isParentOf("MutationEvent", "MutationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false")
    public void _NamedNodeMap_NamedNodeMap() throws Exception {
        isParentOf("NamedNodeMap", "NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Namespace_Namespace() throws Exception {
        isParentOf("Namespace", "Namespace");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _NamespaceCollection_NamespaceCollection() throws Exception {
        isParentOf("NamespaceCollection", "NamespaceCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Navigator_Navigator() throws Exception {
        isParentOf("Navigator", "Navigator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_Node() throws Exception {
        isParentOf("Node", "Node");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    @NotYetImplemented(FF)
    public void _NodeFilter_NodeFilter() throws Exception {
        isParentOf("NodeFilter", "NodeFilter");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _NodeList_NodeList() throws Exception {
        isParentOf("NodeList", "NodeList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Notification_Notification() throws Exception {
        isParentOf("Notification", "Notification");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Option_Option() throws Exception {
        isParentOf("Option", "Option");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Plugin_Plugin() throws Exception {
        isParentOf("Plugin", "Plugin");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _PluginArray_PluginArray() throws Exception {
        isParentOf("PluginArray", "PluginArray");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _PointerEvent_PointerEvent() throws Exception {
        isParentOf("PointerEvent", "PointerEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Popup_Popup() throws Exception {
        isParentOf("Popup", "Popup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Position_Position() throws Exception {
        isParentOf("Position", "Position");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _ProcessingInstruction_ProcessingInstruction() throws Exception {
        isParentOf("ProcessingInstruction", "ProcessingInstruction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Range_Range() throws Exception {
        isParentOf("Range", "Range");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _RowContainer_RowContainer() throws Exception {
        isParentOf("RowContainer", "RowContainer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGAElement_SVGAElement() throws Exception {
        isParentOf("SVGAElement", "SVGAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _SVGAltGlyphElement_SVGAltGlyphElement() throws Exception {
        isParentOf("SVGAltGlyphElement", "SVGAltGlyphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGAngle_SVGAngle() throws Exception {
        isParentOf("SVGAngle", "SVGAngle");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGAnimateElement_SVGAnimateElement() throws Exception {
        isParentOf("SVGAnimateElement", "SVGAnimateElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGAnimateMotionElement_SVGAnimateMotionElement() throws Exception {
        isParentOf("SVGAnimateMotionElement", "SVGAnimateMotionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGAnimateTransformElement_SVGAnimateTransformElement() throws Exception {
        isParentOf("SVGAnimateTransformElement", "SVGAnimateTransformElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGCircleElement_SVGCircleElement() throws Exception {
        isParentOf("SVGCircleElement", "SVGCircleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGClipPathElement_SVGClipPathElement() throws Exception {
        isParentOf("SVGClipPathElement", "SVGClipPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _SVGCursorElement_SVGCursorElement() throws Exception {
        isParentOf("SVGCursorElement", "SVGCursorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGDefsElement_SVGDefsElement() throws Exception {
        isParentOf("SVGDefsElement", "SVGDefsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGDescElement_SVGDescElement() throws Exception {
        isParentOf("SVGDescElement", "SVGDescElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGElement_SVGElement() throws Exception {
        isParentOf("SVGElement", "SVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGEllipseElement_SVGEllipseElement() throws Exception {
        isParentOf("SVGEllipseElement", "SVGEllipseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEBlendElement_SVGFEBlendElement() throws Exception {
        isParentOf("SVGFEBlendElement", "SVGFEBlendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEColorMatrixElement_SVGFEColorMatrixElement() throws Exception {
        isParentOf("SVGFEColorMatrixElement", "SVGFEColorMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEComponentTransferElement_SVGFEComponentTransferElement() throws Exception {
        isParentOf("SVGFEComponentTransferElement", "SVGFEComponentTransferElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFECompositeElement_SVGFECompositeElement() throws Exception {
        isParentOf("SVGFECompositeElement", "SVGFECompositeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEConvolveMatrixElement_SVGFEConvolveMatrixElement() throws Exception {
        isParentOf("SVGFEConvolveMatrixElement", "SVGFEConvolveMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEDiffuseLightingElement_SVGFEDiffuseLightingElement() throws Exception {
        isParentOf("SVGFEDiffuseLightingElement", "SVGFEDiffuseLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEDisplacementMapElement_SVGFEDisplacementMapElement() throws Exception {
        isParentOf("SVGFEDisplacementMapElement", "SVGFEDisplacementMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEDistantLightElement_SVGFEDistantLightElement() throws Exception {
        isParentOf("SVGFEDistantLightElement", "SVGFEDistantLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEFloodElement_SVGFEFloodElement() throws Exception {
        isParentOf("SVGFEFloodElement", "SVGFEFloodElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEFuncAElement_SVGFEFuncAElement() throws Exception {
        isParentOf("SVGFEFuncAElement", "SVGFEFuncAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEFuncBElement_SVGFEFuncBElement() throws Exception {
        isParentOf("SVGFEFuncBElement", "SVGFEFuncBElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEFuncGElement_SVGFEFuncGElement() throws Exception {
        isParentOf("SVGFEFuncGElement", "SVGFEFuncGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEFuncRElement_SVGFEFuncRElement() throws Exception {
        isParentOf("SVGFEFuncRElement", "SVGFEFuncRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEGaussianBlurElement_SVGFEGaussianBlurElement() throws Exception {
        isParentOf("SVGFEGaussianBlurElement", "SVGFEGaussianBlurElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEImageElement_SVGFEImageElement() throws Exception {
        isParentOf("SVGFEImageElement", "SVGFEImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEMergeElement_SVGFEMergeElement() throws Exception {
        isParentOf("SVGFEMergeElement", "SVGFEMergeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEMergeNodeElement_SVGFEMergeNodeElement() throws Exception {
        isParentOf("SVGFEMergeNodeElement", "SVGFEMergeNodeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEMorphologyElement_SVGFEMorphologyElement() throws Exception {
        isParentOf("SVGFEMorphologyElement", "SVGFEMorphologyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEOffsetElement_SVGFEOffsetElement() throws Exception {
        isParentOf("SVGFEOffsetElement", "SVGFEOffsetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFEPointLightElement_SVGFEPointLightElement() throws Exception {
        isParentOf("SVGFEPointLightElement", "SVGFEPointLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFESpecularLightingElement_SVGFESpecularLightingElement() throws Exception {
        isParentOf("SVGFESpecularLightingElement", "SVGFESpecularLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFESpotLightElement_SVGFESpotLightElement() throws Exception {
        isParentOf("SVGFESpotLightElement", "SVGFESpotLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFETileElement_SVGFETileElement() throws Exception {
        isParentOf("SVGFETileElement", "SVGFETileElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFETurbulenceElement_SVGFETurbulenceElement() throws Exception {
        isParentOf("SVGFETurbulenceElement", "SVGFETurbulenceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGFilterElement_SVGFilterElement() throws Exception {
        isParentOf("SVGFilterElement", "SVGFilterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGForeignObjectElement_SVGForeignObjectElement() throws Exception {
        isParentOf("SVGForeignObjectElement", "SVGForeignObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGGElement_SVGGElement() throws Exception {
        isParentOf("SVGGElement", "SVGGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGImageElement_SVGImageElement() throws Exception {
        isParentOf("SVGImageElement", "SVGImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGLineElement_SVGLineElement() throws Exception {
        isParentOf("SVGLineElement", "SVGLineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGLinearGradientElement_SVGLinearGradientElement() throws Exception {
        isParentOf("SVGLinearGradientElement", "SVGLinearGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGMarkerElement_SVGMarkerElement() throws Exception {
        isParentOf("SVGMarkerElement", "SVGMarkerElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGMaskElement_SVGMaskElement() throws Exception {
        isParentOf("SVGMaskElement", "SVGMaskElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGMatrix_SVGMatrix() throws Exception {
        isParentOf("SVGMatrix", "SVGMatrix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGMetadataElement_SVGMetadataElement() throws Exception {
        isParentOf("SVGMetadataElement", "SVGMetadataElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGMPathElement_SVGMPathElement() throws Exception {
        isParentOf("SVGMPathElement", "SVGMPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGPathElement_SVGPathElement() throws Exception {
        isParentOf("SVGPathElement", "SVGPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGPatternElement_SVGPatternElement() throws Exception {
        isParentOf("SVGPatternElement", "SVGPatternElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGPolygonElement_SVGPolygonElement() throws Exception {
        isParentOf("SVGPolygonElement", "SVGPolygonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGPolylineElement_SVGPolylineElement() throws Exception {
        isParentOf("SVGPolylineElement", "SVGPolylineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGRadialGradientElement_SVGRadialGradientElement() throws Exception {
        isParentOf("SVGRadialGradientElement", "SVGRadialGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGRect_SVGRect() throws Exception {
        isParentOf("SVGRect", "SVGRect");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGRectElement_SVGRectElement() throws Exception {
        isParentOf("SVGRectElement", "SVGRectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGSVGElement_SVGSVGElement() throws Exception {
        isParentOf("SVGSVGElement", "SVGSVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGScriptElement_SVGScriptElement() throws Exception {
        isParentOf("SVGScriptElement", "SVGScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGSetElement_SVGSetElement() throws Exception {
        isParentOf("SVGSetElement", "SVGSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGStopElement_SVGStopElement() throws Exception {
        isParentOf("SVGStopElement", "SVGStopElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGStyleElement_SVGStyleElement() throws Exception {
        isParentOf("SVGStyleElement", "SVGStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGSwitchElement_SVGSwitchElement() throws Exception {
        isParentOf("SVGSwitchElement", "SVGSwitchElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGSymbolElement_SVGSymbolElement() throws Exception {
        isParentOf("SVGSymbolElement", "SVGSymbolElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGTSpanElement_SVGTSpanElement() throws Exception {
        isParentOf("SVGTSpanElement", "SVGTSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGTextElement_SVGTextElement() throws Exception {
        isParentOf("SVGTextElement", "SVGTextElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGTextPathElement_SVGTextPathElement() throws Exception {
        isParentOf("SVGTextPathElement", "SVGTextPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGTitleElement_SVGTitleElement() throws Exception {
        isParentOf("SVGTitleElement", "SVGTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGUseElement_SVGUseElement() throws Exception {
        isParentOf("SVGUseElement", "SVGUseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _SVGViewElement_SVGViewElement() throws Exception {
        isParentOf("SVGViewElement", "SVGViewElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Screen_Screen() throws Exception {
        isParentOf("Screen", "Screen");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Selection_Selection() throws Exception {
        isParentOf("Selection", "Selection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SimpleArray_SimpleArray() throws Exception {
        isParentOf("SimpleArray", "SimpleArray");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE8 = "true")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void _StaticNodeList_StaticNodeList() throws Exception {
        isParentOf("StaticNodeList", "StaticNodeList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Storage_Storage() throws Exception {
        isParentOf("Storage", "Storage");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _StyleSheetList_StyleSheetList() throws Exception {
        isParentOf("StyleSheetList", "StyleSheetList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Text_Text() throws Exception {
        isParentOf("Text", "Text");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _TextRange_TextRange() throws Exception {
        isParentOf("TextRange", "TextRange");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _TreeWalker_TreeWalker() throws Exception {
        isParentOf("TreeWalker", "TreeWalker");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _UIEvent_UIEvent() throws Exception {
        isParentOf("UIEvent", "UIEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Uint16Array_Uint16Array() throws Exception {
        isParentOf("Uint16Array", "Uint16Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Uint32Array_Uint32Array() throws Exception {
        isParentOf("Uint32Array", "Uint32Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Uint8Array_Uint8Array() throws Exception {
        isParentOf("Uint8Array", "Uint8Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Uint8ClampedArray_Uint8ClampedArray() throws Exception {
        isParentOf("Uint8ClampedArray", "Uint8ClampedArray");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _WebSocket_WebSocket() throws Exception {
        isParentOf("WebSocket", "WebSocket");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Window_Window() throws Exception {
        isParentOf("Window", "Window");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _XMLDocument_XMLDocument() throws Exception {
        isParentOf("XMLDocument", "XMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _XMLHttpRequest_XMLHttpRequest() throws Exception {
        isParentOf("XMLHttpRequest", "XMLHttpRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _XMLSerializer_XMLSerializer() throws Exception {
        isParentOf("XMLSerializer", "XMLSerializer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _XPathEvaluator_XPathEvaluator() throws Exception {
        isParentOf("XPathEvaluator", "XPathEvaluator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _XPathNSResolver_XPathNSResolver() throws Exception {
        isParentOf("XPathNSResolver", "XPathNSResolver");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _XPathResult_XPathResult() throws Exception {
        isParentOf("XPathResult", "XPathResult");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _XSLTProcessor_XSLTProcessor() throws Exception {
        isParentOf("XSLTProcessor", "XSLTProcessor");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _XSLTemplate_XSLTemplate() throws Exception {
        isParentOf("XSLTemplate", "XSLTemplate");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferView_ArrayBufferViewBase() throws Exception {
        isParentOf("ArrayBufferView", "ArrayBufferViewBase");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferView_DataView() throws Exception {
        isParentOf("ArrayBufferView", "DataView");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferView_Float32Array() throws Exception {
        isParentOf("ArrayBufferView", "Float32Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferView_Float64Array() throws Exception {
        isParentOf("ArrayBufferView", "Float64Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferView_Int16Array() throws Exception {
        isParentOf("ArrayBufferView", "Int16Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferView_Int32Array() throws Exception {
        isParentOf("ArrayBufferView", "Int32Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferView_Int8Array() throws Exception {
        isParentOf("ArrayBufferView", "Int8Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferView_Uint16Array() throws Exception {
        isParentOf("ArrayBufferView", "Uint16Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferView_Uint32Array() throws Exception {
        isParentOf("ArrayBufferView", "Uint32Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferView_Uint8Array() throws Exception {
        isParentOf("ArrayBufferView", "Uint8Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferView_Uint8ClampedArray() throws Exception {
        isParentOf("ArrayBufferView", "Uint8ClampedArray");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferViewBase_Float32Array() throws Exception {
        isParentOf("ArrayBufferViewBase", "Float32Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferViewBase_Float64Array() throws Exception {
        isParentOf("ArrayBufferViewBase", "Float64Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferViewBase_Int16Array() throws Exception {
        isParentOf("ArrayBufferViewBase", "Int16Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferViewBase_Int32Array() throws Exception {
        isParentOf("ArrayBufferViewBase", "Int32Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferViewBase_Int8Array() throws Exception {
        isParentOf("ArrayBufferViewBase", "Int8Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferViewBase_Uint16Array() throws Exception {
        isParentOf("ArrayBufferViewBase", "Uint16Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferViewBase_Uint32Array() throws Exception {
        isParentOf("ArrayBufferViewBase", "Uint32Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferViewBase_Uint8Array() throws Exception {
        isParentOf("ArrayBufferViewBase", "Uint8Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ArrayBufferViewBase_Uint8ClampedArray() throws Exception {
        isParentOf("ArrayBufferViewBase", "Uint8ClampedArray");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSSRule_CSSCharsetRule() throws Exception {
        isParentOf("CSSRule", "CSSCharsetRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CSSRule_CSSFontFaceRule() throws Exception {
        isParentOf("CSSRule", "CSSFontFaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CSSRule_CSSImportRule() throws Exception {
        isParentOf("CSSRule", "CSSImportRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CSSRule_CSSMediaRule() throws Exception {
        isParentOf("CSSRule", "CSSMediaRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _CSSRule_CSSStyleRule() throws Exception {
        isParentOf("CSSRule", "CSSStyleRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
        FF = "true")
    public void _CSSStyleDeclaration_CSS2Properties() throws Exception {
        isParentOf("CSSStyleDeclaration", "CSS2Properties");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CSSStyleDeclaration_ComputedCSSStyleDeclaration() throws Exception {
        isParentOf("CSSStyleDeclaration", "ComputedCSSStyleDeclaration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSSValue_CSSPrimitiveValue() throws Exception {
        isParentOf("CSSValue", "CSSPrimitiveValue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CharacterDataImpl_CDATASection() throws Exception {
        isParentOf("CharacterDataImpl", "CDATASection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CharacterDataImpl_Comment() throws Exception {
        isParentOf("CharacterDataImpl", "Comment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CharacterDataImpl_HTMLCommentElement() throws Exception {
        isParentOf("CharacterDataImpl", "HTMLCommentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CharacterDataImpl_Text() throws Exception {
        isParentOf("CharacterDataImpl", "Text");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Comment_HTMLCommentElement() throws Exception {
        isParentOf("Comment", "HTMLCommentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ComputedCSSStyleDeclaration_CSS2Properties() throws Exception {
        isParentOf("ComputedCSSStyleDeclaration", "CSS2Properties");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Document_HTMLDocument() throws Exception {
        isParentOf("Document", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Document_XMLDocument() throws Exception {
        isParentOf("Document", "XMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLAnchorElement() throws Exception {
        isParentOf("Element", "HTMLAnchorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLAppletElement() throws Exception {
        isParentOf("Element", "HTMLAppletElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLAreaElement() throws Exception {
        isParentOf("Element", "HTMLAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLAudioElement() throws Exception {
        isParentOf("Element", "HTMLAudioElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Element_HTMLBGSoundElement() throws Exception {
        isParentOf("Element", "HTMLBGSoundElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLBRElement() throws Exception {
        isParentOf("Element", "HTMLBRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLBaseElement() throws Exception {
        isParentOf("Element", "HTMLBaseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Element_HTMLBaseFontElement() throws Exception {
        isParentOf("Element", "HTMLBaseFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Element_HTMLBlockElement() throws Exception {
        isParentOf("Element", "HTMLBlockElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLQuoteElement() throws Exception {
        isParentOf("Element", "HTMLQuoteElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLBodyElement() throws Exception {
        isParentOf("Element", "HTMLBodyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLButtonElement() throws Exception {
        isParentOf("Element", "HTMLButtonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLCanvasElement() throws Exception {
        isParentOf("Element", "HTMLCanvasElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLDataListElement() throws Exception {
        isParentOf("Element", "HTMLDataListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Element_HTMLDDElement() throws Exception {
        isParentOf("Element", "HTMLDDElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Element_HTMLDetailsElement() throws Exception {
        isParentOf("Element", "HTMLDetailsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Element_HTMLDialogElement() throws Exception {
        isParentOf("Element", "HTMLDialogElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Element_HTMLDTElement() throws Exception {
        isParentOf("Element", "HTMLDTElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLDListElement() throws Exception {
        isParentOf("Element", "HTMLDListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _Element_HTMLDirectoryElement() throws Exception {
        isParentOf("Element", "HTMLDirectoryElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLDivElement() throws Exception {
        isParentOf("Element", "HTMLDivElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLElement() throws Exception {
        isParentOf("Element", "HTMLElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLEmbedElement() throws Exception {
        isParentOf("Element", "HTMLEmbedElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLFieldSetElement() throws Exception {
        isParentOf("Element", "HTMLFieldSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLFontElement() throws Exception {
        isParentOf("Element", "HTMLFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented
    public void _Element_HTMLFormElement() throws Exception {
        isParentOf("Element", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLFrameElement() throws Exception {
        isParentOf("Element", "HTMLFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLFrameSetElement() throws Exception {
        isParentOf("Element", "HTMLFrameSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLHRElement() throws Exception {
        isParentOf("Element", "HTMLHRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLHeadElement() throws Exception {
        isParentOf("Element", "HTMLHeadElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLHeadingElement() throws Exception {
        isParentOf("Element", "HTMLHeadingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLHtmlElement() throws Exception {
        isParentOf("Element", "HTMLHtmlElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLIFrameElement() throws Exception {
        isParentOf("Element", "HTMLIFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLImageElement() throws Exception {
        isParentOf("Element", "HTMLImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Element_HTMLInlineQuotationElement() throws Exception {
        isParentOf("Element", "HTMLInlineQuotationElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLInputElement() throws Exception {
        isParentOf("Element", "HTMLInputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Element_HTMLIsIndexElement() throws Exception {
        isParentOf("Element", "HTMLIsIndexElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Element_HTMLKeygenElement() throws Exception {
        isParentOf("Element", "HTMLKeygenElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLLIElement() throws Exception {
        isParentOf("Element", "HTMLLIElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLLabelElement() throws Exception {
        isParentOf("Element", "HTMLLabelElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLLegendElement() throws Exception {
        isParentOf("Element", "HTMLLegendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLLinkElement() throws Exception {
        isParentOf("Element", "HTMLLinkElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Element_HTMLListElement() throws Exception {
        isParentOf("Element", "HTMLListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLMapElement() throws Exception {
        isParentOf("Element", "HTMLMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false")
    public void _Element_HTMLMarqueeElement() throws Exception {
        isParentOf("Element", "HTMLMarqueeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLMediaElement() throws Exception {
        isParentOf("Element", "HTMLMediaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLMenuElement() throws Exception {
        isParentOf("Element", "HTMLMenuElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Element_HTMLMenuItemElement() throws Exception {
        isParentOf("Element", "HTMLMenuItemElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLMetaElement() throws Exception {
        isParentOf("Element", "HTMLMetaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Element_HTMLMeterElement() throws Exception {
        isParentOf("Element", "HTMLMeterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLModElement() throws Exception {
        isParentOf("Element", "HTMLModElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE8 = "true")
    public void _Element_HTMLNoShowElement() throws Exception {
        isParentOf("Element", "HTMLNoShowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Element_HTMLNextIdElement() throws Exception {
        isParentOf("Element", "HTMLNextIdElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLOListElement() throws Exception {
        isParentOf("Element", "HTMLOListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLObjectElement() throws Exception {
        isParentOf("Element", "HTMLObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLOptGroupElement() throws Exception {
        isParentOf("Element", "HTMLOptGroupElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLOptionElement() throws Exception {
        isParentOf("Element", "HTMLOptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Element_HTMLOutputElement() throws Exception {
        isParentOf("Element", "HTMLOutputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLParagraphElement() throws Exception {
        isParentOf("Element", "HTMLParagraphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLParamElement() throws Exception {
        isParentOf("Element", "HTMLParamElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Element_HTMLPhraseElement() throws Exception {
        isParentOf("Element", "HTMLPhraseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _Element_HTMLPreElement() throws Exception {
        isParentOf("Element", "HTMLPreElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLProgressElement() throws Exception {
        isParentOf("Element", "HTMLProgressElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLScriptElement() throws Exception {
        isParentOf("Element", "HTMLScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLSelectElement() throws Exception {
        isParentOf("Element", "HTMLSelectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLSourceElement() throws Exception {
        isParentOf("Element", "HTMLSourceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLSpanElement() throws Exception {
        isParentOf("Element", "HTMLSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLStyleElement() throws Exception {
        isParentOf("Element", "HTMLStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLTableCaptionElement() throws Exception {
        isParentOf("Element", "HTMLTableCaptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLTableCellElement() throws Exception {
        isParentOf("Element", "HTMLTableCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLTableColElement() throws Exception {
        isParentOf("Element", "HTMLTableColElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Element_HTMLTableComponent() throws Exception {
        isParentOf("Element", "HTMLTableComponent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Element_HTMLTableDataCellElement() throws Exception {
        isParentOf("Element", "HTMLTableDataCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLTableElement() throws Exception {
        isParentOf("Element", "HTMLTableElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Element_HTMLTableHeaderCellElement() throws Exception {
        isParentOf("Element", "HTMLTableHeaderCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLTableRowElement() throws Exception {
        isParentOf("Element", "HTMLTableRowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLTableSectionElement() throws Exception {
        isParentOf("Element", "HTMLTableSectionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE8 = "true")
    public void _Element_HTMLTextElement() throws Exception {
        isParentOf("Element", "HTMLTextElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLTextAreaElement() throws Exception {
        isParentOf("Element", "HTMLTextAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Element_HTMLTimeElement() throws Exception {
        isParentOf("Element", "HTMLTimeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLTitleElement() throws Exception {
        isParentOf("Element", "HTMLTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLTrackElement() throws Exception {
        isParentOf("Element", "HTMLTrackElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_HTMLUListElement() throws Exception {
        isParentOf("Element", "HTMLUListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _Element_HTMLUnknownElement() throws Exception {
        isParentOf("Element", "HTMLUnknownElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_HTMLVideoElement() throws Exception {
        isParentOf("Element", "HTMLVideoElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_Image() throws Exception {
        isParentOf("Element", "Image");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_Option() throws Exception {
        isParentOf("Element", "Option");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Element_RowContainer() throws Exception {
        isParentOf("Element", "RowContainer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGAElement() throws Exception {
        isParentOf("Element", "SVGAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Element_SVGAltGlyphElement() throws Exception {
        isParentOf("Element", "SVGAltGlyphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Element_SVGAnimateElement() throws Exception {
        isParentOf("Element", "SVGAnimateElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Element_SVGAnimateMotionElement() throws Exception {
        isParentOf("Element", "SVGAnimateMotionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Element_SVGAnimateTransformElement() throws Exception {
        isParentOf("Element", "SVGAnimateTransformElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGCircleElement() throws Exception {
        isParentOf("Element", "SVGCircleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGClipPathElement() throws Exception {
        isParentOf("Element", "SVGClipPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Element_SVGCursorElement() throws Exception {
        isParentOf("Element", "SVGCursorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGDefsElement() throws Exception {
        isParentOf("Element", "SVGDefsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGDescElement() throws Exception {
        isParentOf("Element", "SVGDescElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGElement() throws Exception {
        isParentOf("Element", "SVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGEllipseElement() throws Exception {
        isParentOf("Element", "SVGEllipseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEBlendElement() throws Exception {
        isParentOf("Element", "SVGFEBlendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEColorMatrixElement() throws Exception {
        isParentOf("Element", "SVGFEColorMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEComponentTransferElement() throws Exception {
        isParentOf("Element", "SVGFEComponentTransferElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFECompositeElement() throws Exception {
        isParentOf("Element", "SVGFECompositeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEConvolveMatrixElement() throws Exception {
        isParentOf("Element", "SVGFEConvolveMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEDiffuseLightingElement() throws Exception {
        isParentOf("Element", "SVGFEDiffuseLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEDisplacementMapElement() throws Exception {
        isParentOf("Element", "SVGFEDisplacementMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEDistantLightElement() throws Exception {
        isParentOf("Element", "SVGFEDistantLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEFloodElement() throws Exception {
        isParentOf("Element", "SVGFEFloodElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEFuncAElement() throws Exception {
        isParentOf("Element", "SVGFEFuncAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEFuncBElement() throws Exception {
        isParentOf("Element", "SVGFEFuncBElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEFuncGElement() throws Exception {
        isParentOf("Element", "SVGFEFuncGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEFuncRElement() throws Exception {
        isParentOf("Element", "SVGFEFuncRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEGaussianBlurElement() throws Exception {
        isParentOf("Element", "SVGFEGaussianBlurElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEImageElement() throws Exception {
        isParentOf("Element", "SVGFEImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEMergeElement() throws Exception {
        isParentOf("Element", "SVGFEMergeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEMergeNodeElement() throws Exception {
        isParentOf("Element", "SVGFEMergeNodeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEMorphologyElement() throws Exception {
        isParentOf("Element", "SVGFEMorphologyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEOffsetElement() throws Exception {
        isParentOf("Element", "SVGFEOffsetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFEPointLightElement() throws Exception {
        isParentOf("Element", "SVGFEPointLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFESpecularLightingElement() throws Exception {
        isParentOf("Element", "SVGFESpecularLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFESpotLightElement() throws Exception {
        isParentOf("Element", "SVGFESpotLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFETileElement() throws Exception {
        isParentOf("Element", "SVGFETileElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFETurbulenceElement() throws Exception {
        isParentOf("Element", "SVGFETurbulenceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGFilterElement() throws Exception {
        isParentOf("Element", "SVGFilterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Element_SVGForeignObjectElement() throws Exception {
        isParentOf("Element", "SVGForeignObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGGElement() throws Exception {
        isParentOf("Element", "SVGGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGImageElement() throws Exception {
        isParentOf("Element", "SVGImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGLineElement() throws Exception {
        isParentOf("Element", "SVGLineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGLinearGradientElement() throws Exception {
        isParentOf("Element", "SVGLinearGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGMarkerElement() throws Exception {
        isParentOf("Element", "SVGMarkerElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGMaskElement() throws Exception {
        isParentOf("Element", "SVGMaskElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGMetadataElement() throws Exception {
        isParentOf("Element", "SVGMetadataElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Element_SVGMPathElement() throws Exception {
        isParentOf("Element", "SVGMPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGPathElement() throws Exception {
        isParentOf("Element", "SVGPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGPatternElement() throws Exception {
        isParentOf("Element", "SVGPatternElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGPolygonElement() throws Exception {
        isParentOf("Element", "SVGPolygonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGPolylineElement() throws Exception {
        isParentOf("Element", "SVGPolylineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGRadialGradientElement() throws Exception {
        isParentOf("Element", "SVGRadialGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGRectElement() throws Exception {
        isParentOf("Element", "SVGRectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGSVGElement() throws Exception {
        isParentOf("Element", "SVGSVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGScriptElement() throws Exception {
        isParentOf("Element", "SVGScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Element_SVGSetElement() throws Exception {
        isParentOf("Element", "SVGSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGStopElement() throws Exception {
        isParentOf("Element", "SVGStopElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGStyleElement() throws Exception {
        isParentOf("Element", "SVGStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGSwitchElement() throws Exception {
        isParentOf("Element", "SVGSwitchElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGSymbolElement() throws Exception {
        isParentOf("Element", "SVGSymbolElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGTSpanElement() throws Exception {
        isParentOf("Element", "SVGTSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGTextElement() throws Exception {
        isParentOf("Element", "SVGTextElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGTextPathElement() throws Exception {
        isParentOf("Element", "SVGTextPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGTitleElement() throws Exception {
        isParentOf("Element", "SVGTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGUseElement() throws Exception {
        isParentOf("Element", "SVGUseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Element_SVGViewElement() throws Exception {
        isParentOf("Element", "SVGViewElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Event_BeforeUnloadEvent() throws Exception {
        isParentOf("Event", "BeforeUnloadEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Event_HashChangeEvent() throws Exception {
        isParentOf("Event", "HashChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Event_KeyboardEvent() throws Exception {
        isParentOf("Event", "KeyboardEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Event_MessageEvent() throws Exception {
        isParentOf("Event", "MessageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Event_MouseEvent() throws Exception {
        isParentOf("Event", "MouseEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Event_MutationEvent() throws Exception {
        isParentOf("Event", "MutationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Event_PointerEvent() throws Exception {
        isParentOf("Event", "PointerEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Event_UIEvent() throws Exception {
        isParentOf("Event", "UIEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    @NotYetImplemented({ CHROME, IE11 })
    public void _HTMLCollection_HTMLAllCollection() throws Exception {
        isParentOf("HTMLCollection", "HTMLAllCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLAnchorElement() throws Exception {
        isParentOf("HTMLElement", "HTMLAnchorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLAppletElement() throws Exception {
        isParentOf("HTMLElement", "HTMLAppletElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLAreaElement() throws Exception {
        isParentOf("HTMLElement", "HTMLAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLAudioElement() throws Exception {
        isParentOf("HTMLElement", "HTMLAudioElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLElement_HTMLBGSoundElement() throws Exception {
        isParentOf("HTMLElement", "HTMLBGSoundElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLBRElement() throws Exception {
        isParentOf("HTMLElement", "HTMLBRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLBaseElement() throws Exception {
        isParentOf("HTMLElement", "HTMLBaseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLElement_HTMLBaseFontElement() throws Exception {
        isParentOf("HTMLElement", "HTMLBaseFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLElement_HTMLBlockElement() throws Exception {
        isParentOf("HTMLElement", "HTMLBlockElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLQuoteElement() throws Exception {
        isParentOf("HTMLElement", "HTMLQuoteElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLBodyElement() throws Exception {
        isParentOf("HTMLElement", "HTMLBodyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLButtonElement() throws Exception {
        isParentOf("HTMLElement", "HTMLButtonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLCanvasElement() throws Exception {
        isParentOf("HTMLElement", "HTMLCanvasElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLDataListElement() throws Exception {
        isParentOf("HTMLElement", "HTMLDataListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLElement_HTMLDDElement() throws Exception {
        isParentOf("HTMLElement", "HTMLDDElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _HTMLElement_HTMLDetailsElement() throws Exception {
        isParentOf("HTMLElement", "HTMLDetailsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _HTMLElement_HTMLDialogElement() throws Exception {
        isParentOf("HTMLElement", "HTMLDialogElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLElement_HTMLDTElement() throws Exception {
        isParentOf("HTMLElement", "HTMLDTElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLDListElement() throws Exception {
        isParentOf("HTMLElement", "HTMLDListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLDirectoryElement() throws Exception {
        isParentOf("HTMLElement", "HTMLDirectoryElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLDivElement() throws Exception {
        isParentOf("HTMLElement", "HTMLDivElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLEmbedElement() throws Exception {
        isParentOf("HTMLElement", "HTMLEmbedElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLFieldSetElement() throws Exception {
        isParentOf("HTMLElement", "HTMLFieldSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLFontElement() throws Exception {
        isParentOf("HTMLElement", "HTMLFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented
    public void _HTMLElement_HTMLFormElement() throws Exception {
        isParentOf("HTMLElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLFrameElement() throws Exception {
        isParentOf("HTMLElement", "HTMLFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLFrameSetElement() throws Exception {
        isParentOf("HTMLElement", "HTMLFrameSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLHRElement() throws Exception {
        isParentOf("HTMLElement", "HTMLHRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLHeadElement() throws Exception {
        isParentOf("HTMLElement", "HTMLHeadElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLHeadingElement() throws Exception {
        isParentOf("HTMLElement", "HTMLHeadingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
        IE8 = "false")
    public void _HTMLElement_HTMLHtmlElement() throws Exception {
        isParentOf("HTMLElement", "HTMLHtmlElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLIFrameElement() throws Exception {
        isParentOf("HTMLElement", "HTMLIFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLImageElement() throws Exception {
        isParentOf("HTMLElement", "HTMLImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLElement_HTMLInlineQuotationElement() throws Exception {
        isParentOf("HTMLElement", "HTMLInlineQuotationElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLInputElement() throws Exception {
        isParentOf("HTMLElement", "HTMLInputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLElement_HTMLIsIndexElement() throws Exception {
        isParentOf("HTMLElement", "HTMLIsIndexElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _HTMLElement_HTMLKeygenElement() throws Exception {
        isParentOf("HTMLElement", "HTMLKeygenElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLLIElement() throws Exception {
        isParentOf("HTMLElement", "HTMLLIElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLLabelElement() throws Exception {
        isParentOf("HTMLElement", "HTMLLabelElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLLegendElement() throws Exception {
        isParentOf("HTMLElement", "HTMLLegendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLLinkElement() throws Exception {
        isParentOf("HTMLElement", "HTMLLinkElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLElement_HTMLListElement() throws Exception {
        isParentOf("HTMLElement", "HTMLListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLMapElement() throws Exception {
        isParentOf("HTMLElement", "HTMLMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false",
            IE8 = "false")
    public void _HTMLElement_HTMLMarqueeElement() throws Exception {
        isParentOf("HTMLElement", "HTMLMarqueeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLMediaElement() throws Exception {
        isParentOf("HTMLElement", "HTMLMediaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLMenuElement() throws Exception {
        isParentOf("HTMLElement", "HTMLMenuElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _HTMLElement_HTMLMenuItemElement() throws Exception {
        isParentOf("HTMLElement", "HTMLMenuItemElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLMetaElement() throws Exception {
        isParentOf("HTMLElement", "HTMLMetaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLElement_HTMLMeterElement() throws Exception {
        isParentOf("HTMLElement", "HTMLMeterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLModElement() throws Exception {
        isParentOf("HTMLElement", "HTMLModElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLElement_HTMLNoShowElement() throws Exception {
        isParentOf("HTMLElement", "HTMLNoShowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLElement_HTMLNextIdElement() throws Exception {
        isParentOf("HTMLElement", "HTMLNextIdElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLOListElement() throws Exception {
        isParentOf("HTMLElement", "HTMLOListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLObjectElement() throws Exception {
        isParentOf("HTMLElement", "HTMLObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLOptGroupElement() throws Exception {
        isParentOf("HTMLElement", "HTMLOptGroupElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLOptionElement() throws Exception {
        isParentOf("HTMLElement", "HTMLOptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLElement_HTMLOutputElement() throws Exception {
        isParentOf("HTMLElement", "HTMLOutputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLParagraphElement() throws Exception {
        isParentOf("HTMLElement", "HTMLParagraphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLParamElement() throws Exception {
        isParentOf("HTMLElement", "HTMLParamElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLElement_HTMLPhraseElement() throws Exception {
        isParentOf("HTMLElement", "HTMLPhraseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLPreElement() throws Exception {
        isParentOf("HTMLElement", "HTMLPreElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLProgressElement() throws Exception {
        isParentOf("HTMLElement", "HTMLProgressElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLScriptElement() throws Exception {
        isParentOf("HTMLElement", "HTMLScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _HTMLElement_HTMLSelectElement() throws Exception {
        isParentOf("HTMLElement", "HTMLSelectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLSourceElement() throws Exception {
        isParentOf("HTMLElement", "HTMLSourceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLSpanElement() throws Exception {
        isParentOf("HTMLElement", "HTMLSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLStyleElement() throws Exception {
        isParentOf("HTMLElement", "HTMLStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLTableCaptionElement() throws Exception {
        isParentOf("HTMLElement", "HTMLTableCaptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLTableCellElement() throws Exception {
        isParentOf("HTMLElement", "HTMLTableCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLTableColElement() throws Exception {
        isParentOf("HTMLElement", "HTMLTableColElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLElement_HTMLTableComponent() throws Exception {
        isParentOf("HTMLElement", "HTMLTableComponent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLElement_HTMLTableDataCellElement() throws Exception {
        isParentOf("HTMLElement", "HTMLTableDataCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLTableElement() throws Exception {
        isParentOf("HTMLElement", "HTMLTableElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLElement_HTMLTableHeaderCellElement() throws Exception {
        isParentOf("HTMLElement", "HTMLTableHeaderCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLTableRowElement() throws Exception {
        isParentOf("HTMLElement", "HTMLTableRowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLTableSectionElement() throws Exception {
        isParentOf("HTMLElement", "HTMLTableSectionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLElement_HTMLTextElement() throws Exception {
        isParentOf("HTMLElement", "HTMLTextElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLTextAreaElement() throws Exception {
        isParentOf("HTMLElement", "HTMLTextAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _HTMLElement_HTMLTimeElement() throws Exception {
        isParentOf("HTMLElement", "HTMLTimeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLTitleElement() throws Exception {
        isParentOf("HTMLElement", "HTMLTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLTrackElement() throws Exception {
        isParentOf("HTMLElement", "HTMLTrackElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLUListElement() throws Exception {
        isParentOf("HTMLElement", "HTMLUListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLUnknownElement() throws Exception {
        isParentOf("HTMLElement", "HTMLUnknownElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_HTMLVideoElement() throws Exception {
        isParentOf("HTMLElement", "HTMLVideoElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_Image() throws Exception {
        isParentOf("HTMLElement", "Image");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLElement_Option() throws Exception {
        isParentOf("HTMLElement", "Option");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLElement_RowContainer() throws Exception {
        isParentOf("HTMLElement", "RowContainer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLListElement_HTMLDListElement() throws Exception {
        isParentOf("HTMLListElement", "HTMLDListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLListElement_HTMLDirectoryElement() throws Exception {
        isParentOf("HTMLListElement", "HTMLDirectoryElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLListElement_HTMLMenuElement() throws Exception {
        isParentOf("HTMLListElement", "HTMLMenuElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLListElement_HTMLOListElement() throws Exception {
        isParentOf("HTMLListElement", "HTMLOListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLListElement_HTMLUListElement() throws Exception {
        isParentOf("HTMLListElement", "HTMLUListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLMediaElement_HTMLAudioElement() throws Exception {
        isParentOf("HTMLMediaElement", "HTMLAudioElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _HTMLMediaElement_HTMLVideoElement() throws Exception {
        isParentOf("HTMLMediaElement", "HTMLVideoElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLTableCellElement_HTMLTableDataCellElement() throws Exception {
        isParentOf("HTMLTableCellElement", "HTMLTableDataCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _HTMLTableCellElement_HTMLTableHeaderCellElement() throws Exception {
        isParentOf("HTMLTableCellElement", "HTMLTableHeaderCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLTableComponent_HTMLTableCellElement() throws Exception {
        isParentOf("HTMLTableComponent", "HTMLTableCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLTableComponent_HTMLTableColElement() throws Exception {
        isParentOf("HTMLTableComponent", "HTMLTableColElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLTableComponent_HTMLTableDataCellElement() throws Exception {
        isParentOf("HTMLTableComponent", "HTMLTableDataCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLTableComponent_HTMLTableHeaderCellElement() throws Exception {
        isParentOf("HTMLTableComponent", "HTMLTableHeaderCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLTableComponent_HTMLTableRowElement() throws Exception {
        isParentOf("HTMLTableComponent", "HTMLTableRowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _MouseEvent_PointerEvent() throws Exception {
        isParentOf("MouseEvent", "PointerEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_Attr() throws Exception {
        isParentOf("Node", "Attr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_CDATASection() throws Exception {
        isParentOf("Node", "CDATASection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Node_CharacterDataImpl() throws Exception {
        isParentOf("Node", "CharacterDataImpl");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_Comment() throws Exception {
        isParentOf("Node", "Comment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_Document() throws Exception {
        isParentOf("Node", "Document");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_DocumentFragment() throws Exception {
        isParentOf("Node", "DocumentFragment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_DocumentType() throws Exception {
        isParentOf("Node", "DocumentType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_Element() throws Exception {
        isParentOf("Node", "Element");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Node_FormField() throws Exception {
        isParentOf("Node", "FormField");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLAnchorElement() throws Exception {
        isParentOf("Node", "HTMLAnchorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLAppletElement() throws Exception {
        isParentOf("Node", "HTMLAppletElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLAreaElement() throws Exception {
        isParentOf("Node", "HTMLAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLAudioElement() throws Exception {
        isParentOf("Node", "HTMLAudioElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Node_HTMLBGSoundElement() throws Exception {
        isParentOf("Node", "HTMLBGSoundElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLBRElement() throws Exception {
        isParentOf("Node", "HTMLBRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLBaseElement() throws Exception {
        isParentOf("Node", "HTMLBaseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Node_HTMLBaseFontElement() throws Exception {
        isParentOf("Node", "HTMLBaseFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Node_HTMLBlockElement() throws Exception {
        isParentOf("Node", "HTMLBlockElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLQuoteElement() throws Exception {
        isParentOf("Node", "HTMLQuoteElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLBodyElement() throws Exception {
        isParentOf("Node", "HTMLBodyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLButtonElement() throws Exception {
        isParentOf("Node", "HTMLButtonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLCanvasElement() throws Exception {
        isParentOf("Node", "HTMLCanvasElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Node_HTMLCommentElement() throws Exception {
        isParentOf("Node", "HTMLCommentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLDataListElement() throws Exception {
        isParentOf("Node", "HTMLDataListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Node_HTMLDDElement() throws Exception {
        isParentOf("Node", "HTMLDDElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Node_HTMLDetailsElement() throws Exception {
        isParentOf("Node", "HTMLDetailsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Node_HTMLDialogElement() throws Exception {
        isParentOf("Node", "HTMLDialogElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Node_HTMLDTElement() throws Exception {
        isParentOf("Node", "HTMLDTElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLDListElement() throws Exception {
        isParentOf("Node", "HTMLDListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLDirectoryElement() throws Exception {
        isParentOf("Node", "HTMLDirectoryElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLDivElement() throws Exception {
        isParentOf("Node", "HTMLDivElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLDocument() throws Exception {
        isParentOf("Node", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLElement() throws Exception {
        isParentOf("Node", "HTMLElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLEmbedElement() throws Exception {
        isParentOf("Node", "HTMLEmbedElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLFieldSetElement() throws Exception {
        isParentOf("Node", "HTMLFieldSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLFontElement() throws Exception {
        isParentOf("Node", "HTMLFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void _Node_HTMLFormElement() throws Exception {
        isParentOf("Node", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLFrameElement() throws Exception {
        isParentOf("Node", "HTMLFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLFrameSetElement() throws Exception {
        isParentOf("Node", "HTMLFrameSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLHRElement() throws Exception {
        isParentOf("Node", "HTMLHRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLHeadElement() throws Exception {
        isParentOf("Node", "HTMLHeadElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLHeadingElement() throws Exception {
        isParentOf("Node", "HTMLHeadingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLHtmlElement() throws Exception {
        isParentOf("Node", "HTMLHtmlElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLIFrameElement() throws Exception {
        isParentOf("Node", "HTMLIFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLImageElement() throws Exception {
        isParentOf("Node", "HTMLImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Node_HTMLInlineQuotationElement() throws Exception {
        isParentOf("Node", "HTMLInlineQuotationElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLInputElement() throws Exception {
        isParentOf("Node", "HTMLInputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Node_HTMLIsIndexElement() throws Exception {
        isParentOf("Node", "HTMLIsIndexElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Node_HTMLKeygenElement() throws Exception {
        isParentOf("Node", "HTMLKeygenElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLLIElement() throws Exception {
        isParentOf("Node", "HTMLLIElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLLabelElement() throws Exception {
        isParentOf("Node", "HTMLLabelElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLLegendElement() throws Exception {
        isParentOf("Node", "HTMLLegendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLLinkElement() throws Exception {
        isParentOf("Node", "HTMLLinkElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Node_HTMLListElement() throws Exception {
        isParentOf("Node", "HTMLListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLMapElement() throws Exception {
        isParentOf("Node", "HTMLMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false",
            IE8 = "false")
    public void _Node_HTMLMarqueeElement() throws Exception {
        isParentOf("Node", "HTMLMarqueeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLMediaElement() throws Exception {
        isParentOf("Node", "HTMLMediaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLMenuElement() throws Exception {
        isParentOf("Node", "HTMLMenuElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Node_HTMLMenuItemElement() throws Exception {
        isParentOf("Node", "HTMLMenuItemElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLMetaElement() throws Exception {
        isParentOf("Node", "HTMLMetaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_HTMLMeterElement() throws Exception {
        isParentOf("Node", "HTMLMeterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLModElement() throws Exception {
        isParentOf("Node", "HTMLModElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Node_HTMLNoShowElement() throws Exception {
        isParentOf("Node", "HTMLNoShowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Node_HTMLNextIdElement() throws Exception {
        isParentOf("Node", "HTMLNextIdElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLOListElement() throws Exception {
        isParentOf("Node", "HTMLOListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLObjectElement() throws Exception {
        isParentOf("Node", "HTMLObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLOptGroupElement() throws Exception {
        isParentOf("Node", "HTMLOptGroupElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLOptionElement() throws Exception {
        isParentOf("Node", "HTMLOptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_HTMLOutputElement() throws Exception {
        isParentOf("Node", "HTMLOutputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLParagraphElement() throws Exception {
        isParentOf("Node", "HTMLParagraphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLParamElement() throws Exception {
        isParentOf("Node", "HTMLParamElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Node_HTMLPhraseElement() throws Exception {
        isParentOf("Node", "HTMLPhraseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLPreElement() throws Exception {
        isParentOf("Node", "HTMLPreElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLProgressElement() throws Exception {
        isParentOf("Node", "HTMLProgressElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLScriptElement() throws Exception {
        isParentOf("Node", "HTMLScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLSelectElement() throws Exception {
        isParentOf("Node", "HTMLSelectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLSourceElement() throws Exception {
        isParentOf("Node", "HTMLSourceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLSpanElement() throws Exception {
        isParentOf("Node", "HTMLSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLStyleElement() throws Exception {
        isParentOf("Node", "HTMLStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLTableCaptionElement() throws Exception {
        isParentOf("Node", "HTMLTableCaptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLTableCellElement() throws Exception {
        isParentOf("Node", "HTMLTableCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLTableColElement() throws Exception {
        isParentOf("Node", "HTMLTableColElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Node_HTMLTableComponent() throws Exception {
        isParentOf("Node", "HTMLTableComponent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Node_HTMLTableDataCellElement() throws Exception {
        isParentOf("Node", "HTMLTableDataCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLTableElement() throws Exception {
        isParentOf("Node", "HTMLTableElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _Node_HTMLTableHeaderCellElement() throws Exception {
        isParentOf("Node", "HTMLTableHeaderCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLTableRowElement() throws Exception {
        isParentOf("Node", "HTMLTableRowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLTableSectionElement() throws Exception {
        isParentOf("Node", "HTMLTableSectionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Node_HTMLTextElement() throws Exception {
        isParentOf("Node", "HTMLTextElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLTextAreaElement() throws Exception {
        isParentOf("Node", "HTMLTextAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Node_HTMLTimeElement() throws Exception {
        isParentOf("Node", "HTMLTimeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLTitleElement() throws Exception {
        isParentOf("Node", "HTMLTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLTrackElement() throws Exception {
        isParentOf("Node", "HTMLTrackElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLUListElement() throws Exception {
        isParentOf("Node", "HTMLUListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_HTMLUnknownElement() throws Exception {
        isParentOf("Node", "HTMLUnknownElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _Node_HTMLVideoElement() throws Exception {
        isParentOf("Node", "HTMLVideoElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_Image() throws Exception {
        isParentOf("Node", "Image");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_Option() throws Exception {
        isParentOf("Node", "Option");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_ProcessingInstruction() throws Exception {
        isParentOf("Node", "ProcessingInstruction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Node_RowContainer() throws Exception {
        isParentOf("Node", "RowContainer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGAElement() throws Exception {
        isParentOf("Node", "SVGAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Node_SVGAltGlyphElement() throws Exception {
        isParentOf("Node", "SVGAltGlyphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_SVGAnimateElement() throws Exception {
        isParentOf("Node", "SVGAnimateElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_SVGAnimateMotionElement() throws Exception {
        isParentOf("Node", "SVGAnimateMotionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_SVGAnimateTransformElement() throws Exception {
        isParentOf("Node", "SVGAnimateTransformElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGCircleElement() throws Exception {
        isParentOf("Node", "SVGCircleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGClipPathElement() throws Exception {
        isParentOf("Node", "SVGClipPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Node_SVGCursorElement() throws Exception {
        isParentOf("Node", "SVGCursorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGDefsElement() throws Exception {
        isParentOf("Node", "SVGDefsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGDescElement() throws Exception {
        isParentOf("Node", "SVGDescElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGElement() throws Exception {
        isParentOf("Node", "SVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGEllipseElement() throws Exception {
        isParentOf("Node", "SVGEllipseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEBlendElement() throws Exception {
        isParentOf("Node", "SVGFEBlendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEColorMatrixElement() throws Exception {
        isParentOf("Node", "SVGFEColorMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEComponentTransferElement() throws Exception {
        isParentOf("Node", "SVGFEComponentTransferElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFECompositeElement() throws Exception {
        isParentOf("Node", "SVGFECompositeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEConvolveMatrixElement() throws Exception {
        isParentOf("Node", "SVGFEConvolveMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEDiffuseLightingElement() throws Exception {
        isParentOf("Node", "SVGFEDiffuseLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEDisplacementMapElement() throws Exception {
        isParentOf("Node", "SVGFEDisplacementMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEDistantLightElement() throws Exception {
        isParentOf("Node", "SVGFEDistantLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEFloodElement() throws Exception {
        isParentOf("Node", "SVGFEFloodElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEFuncAElement() throws Exception {
        isParentOf("Node", "SVGFEFuncAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEFuncBElement() throws Exception {
        isParentOf("Node", "SVGFEFuncBElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEFuncGElement() throws Exception {
        isParentOf("Node", "SVGFEFuncGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEFuncRElement() throws Exception {
        isParentOf("Node", "SVGFEFuncRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEGaussianBlurElement() throws Exception {
        isParentOf("Node", "SVGFEGaussianBlurElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEImageElement() throws Exception {
        isParentOf("Node", "SVGFEImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEMergeElement() throws Exception {
        isParentOf("Node", "SVGFEMergeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEMergeNodeElement() throws Exception {
        isParentOf("Node", "SVGFEMergeNodeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEMorphologyElement() throws Exception {
        isParentOf("Node", "SVGFEMorphologyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEOffsetElement() throws Exception {
        isParentOf("Node", "SVGFEOffsetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFEPointLightElement() throws Exception {
        isParentOf("Node", "SVGFEPointLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFESpecularLightingElement() throws Exception {
        isParentOf("Node", "SVGFESpecularLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFESpotLightElement() throws Exception {
        isParentOf("Node", "SVGFESpotLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFETileElement() throws Exception {
        isParentOf("Node", "SVGFETileElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFETurbulenceElement() throws Exception {
        isParentOf("Node", "SVGFETurbulenceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGFilterElement() throws Exception {
        isParentOf("Node", "SVGFilterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_SVGForeignObjectElement() throws Exception {
        isParentOf("Node", "SVGForeignObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGGElement() throws Exception {
        isParentOf("Node", "SVGGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGImageElement() throws Exception {
        isParentOf("Node", "SVGImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGLineElement() throws Exception {
        isParentOf("Node", "SVGLineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGLinearGradientElement() throws Exception {
        isParentOf("Node", "SVGLinearGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGMarkerElement() throws Exception {
        isParentOf("Node", "SVGMarkerElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGMaskElement() throws Exception {
        isParentOf("Node", "SVGMaskElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGMetadataElement() throws Exception {
        isParentOf("Node", "SVGMetadataElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_SVGMPathElement() throws Exception {
        isParentOf("Node", "SVGMPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGPathElement() throws Exception {
        isParentOf("Node", "SVGPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGPatternElement() throws Exception {
        isParentOf("Node", "SVGPatternElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGPolygonElement() throws Exception {
        isParentOf("Node", "SVGPolygonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGPolylineElement() throws Exception {
        isParentOf("Node", "SVGPolylineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGRadialGradientElement() throws Exception {
        isParentOf("Node", "SVGRadialGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGRectElement() throws Exception {
        isParentOf("Node", "SVGRectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGSVGElement() throws Exception {
        isParentOf("Node", "SVGSVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGScriptElement() throws Exception {
        isParentOf("Node", "SVGScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_SVGSetElement() throws Exception {
        isParentOf("Node", "SVGSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGStopElement() throws Exception {
        isParentOf("Node", "SVGStopElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGStyleElement() throws Exception {
        isParentOf("Node", "SVGStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGSwitchElement() throws Exception {
        isParentOf("Node", "SVGSwitchElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGSymbolElement() throws Exception {
        isParentOf("Node", "SVGSymbolElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGTSpanElement() throws Exception {
        isParentOf("Node", "SVGTSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGTextElement() throws Exception {
        isParentOf("Node", "SVGTextElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGTextPathElement() throws Exception {
        isParentOf("Node", "SVGTextPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGTitleElement() throws Exception {
        isParentOf("Node", "SVGTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGUseElement() throws Exception {
        isParentOf("Node", "SVGUseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_SVGViewElement() throws Exception {
        isParentOf("Node", "SVGViewElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_Text() throws Exception {
        isParentOf("Node", "Text");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Node_XMLDocument() throws Exception {
        isParentOf("Node", "XMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({ CHROME, IE11 })
    public void _NodeList_HTMLAllCollection() throws Exception {
        isParentOf("NodeList", "HTMLAllCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented
    public void _NodeList_HTMLCollection() throws Exception {
        isParentOf("NodeList", "HTMLCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _RowContainer_HTMLTableElement() throws Exception {
        isParentOf("RowContainer", "HTMLTableElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _RowContainer_HTMLTableSectionElement() throws Exception {
        isParentOf("RowContainer", "HTMLTableSectionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGAElement() throws Exception {
        isParentOf("SVGElement", "SVGAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _SVGElement_SVGAltGlyphElement() throws Exception {
        isParentOf("SVGElement", "SVGAltGlyphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE11 = "false")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGAnimateElement() throws Exception {
        isParentOf("SVGElement", "SVGAnimateElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE11 = "false")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGAnimateMotionElement() throws Exception {
        isParentOf("SVGElement", "SVGAnimateMotionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE11 = "false")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGAnimateTransformElement() throws Exception {
        isParentOf("SVGElement", "SVGAnimateTransformElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGCircleElement() throws Exception {
        isParentOf("SVGElement", "SVGCircleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGClipPathElement() throws Exception {
        isParentOf("SVGElement", "SVGClipPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _SVGElement_SVGCursorElement() throws Exception {
        isParentOf("SVGElement", "SVGCursorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGDefsElement() throws Exception {
        isParentOf("SVGElement", "SVGDefsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGDescElement() throws Exception {
        isParentOf("SVGElement", "SVGDescElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGEllipseElement() throws Exception {
        isParentOf("SVGElement", "SVGEllipseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFEBlendElement() throws Exception {
        isParentOf("SVGElement", "SVGFEBlendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFEColorMatrixElement() throws Exception {
        isParentOf("SVGElement", "SVGFEColorMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFEComponentTransferElement() throws Exception {
        isParentOf("SVGElement", "SVGFEComponentTransferElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFECompositeElement() throws Exception {
        isParentOf("SVGElement", "SVGFECompositeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFEConvolveMatrixElement() throws Exception {
        isParentOf("SVGElement", "SVGFEConvolveMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFEDiffuseLightingElement() throws Exception {
        isParentOf("SVGElement", "SVGFEDiffuseLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFEDisplacementMapElement() throws Exception {
        isParentOf("SVGElement", "SVGFEDisplacementMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFEDistantLightElement() throws Exception {
        isParentOf("SVGElement", "SVGFEDistantLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFEFloodElement() throws Exception {
        isParentOf("SVGElement", "SVGFEFloodElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFEFuncAElement() throws Exception {
        isParentOf("SVGElement", "SVGFEFuncAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFEFuncBElement() throws Exception {
        isParentOf("SVGElement", "SVGFEFuncBElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFEFuncGElement() throws Exception {
        isParentOf("SVGElement", "SVGFEFuncGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFEFuncRElement() throws Exception {
        isParentOf("SVGElement", "SVGFEFuncRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFEGaussianBlurElement() throws Exception {
        isParentOf("SVGElement", "SVGFEGaussianBlurElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFEImageElement() throws Exception {
        isParentOf("SVGElement", "SVGFEImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFEMergeElement() throws Exception {
        isParentOf("SVGElement", "SVGFEMergeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFEMergeNodeElement() throws Exception {
        isParentOf("SVGElement", "SVGFEMergeNodeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFEMorphologyElement() throws Exception {
        isParentOf("SVGElement", "SVGFEMorphologyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFEOffsetElement() throws Exception {
        isParentOf("SVGElement", "SVGFEOffsetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFEPointLightElement() throws Exception {
        isParentOf("SVGElement", "SVGFEPointLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFESpecularLightingElement() throws Exception {
        isParentOf("SVGElement", "SVGFESpecularLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFESpotLightElement() throws Exception {
        isParentOf("SVGElement", "SVGFESpotLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFETileElement() throws Exception {
        isParentOf("SVGElement", "SVGFETileElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFETurbulenceElement() throws Exception {
        isParentOf("SVGElement", "SVGFETurbulenceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGFilterElement() throws Exception {
        isParentOf("SVGElement", "SVGFilterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE11 = "false")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGForeignObjectElement() throws Exception {
        isParentOf("SVGElement", "SVGForeignObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGGElement() throws Exception {
        isParentOf("SVGElement", "SVGGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGImageElement() throws Exception {
        isParentOf("SVGElement", "SVGImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGLineElement() throws Exception {
        isParentOf("SVGElement", "SVGLineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGLinearGradientElement() throws Exception {
        isParentOf("SVGElement", "SVGLinearGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGMarkerElement() throws Exception {
        isParentOf("SVGElement", "SVGMarkerElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGMaskElement() throws Exception {
        isParentOf("SVGElement", "SVGMaskElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGMetadataElement() throws Exception {
        isParentOf("SVGElement", "SVGMetadataElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE11 = "false")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGMPathElement() throws Exception {
        isParentOf("SVGElement", "SVGMPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGPathElement() throws Exception {
        isParentOf("SVGElement", "SVGPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGPatternElement() throws Exception {
        isParentOf("SVGElement", "SVGPatternElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGPolygonElement() throws Exception {
        isParentOf("SVGElement", "SVGPolygonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGPolylineElement() throws Exception {
        isParentOf("SVGElement", "SVGPolylineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGRadialGradientElement() throws Exception {
        isParentOf("SVGElement", "SVGRadialGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGRectElement() throws Exception {
        isParentOf("SVGElement", "SVGRectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGSVGElement() throws Exception {
        isParentOf("SVGElement", "SVGSVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGScriptElement() throws Exception {
        isParentOf("SVGElement", "SVGScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE11 = "false")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGSetElement() throws Exception {
        isParentOf("SVGElement", "SVGSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGStopElement() throws Exception {
        isParentOf("SVGElement", "SVGStopElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGStyleElement() throws Exception {
        isParentOf("SVGElement", "SVGStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGSwitchElement() throws Exception {
        isParentOf("SVGElement", "SVGSwitchElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGSymbolElement() throws Exception {
        isParentOf("SVGElement", "SVGSymbolElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGTSpanElement() throws Exception {
        isParentOf("SVGElement", "SVGTSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGTextElement() throws Exception {
        isParentOf("SVGElement", "SVGTextElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGTextPathElement() throws Exception {
        isParentOf("SVGElement", "SVGTextPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGTitleElement() throws Exception {
        isParentOf("SVGElement", "SVGTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGUseElement() throws Exception {
        isParentOf("SVGElement", "SVGUseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented(IE8)
    public void _SVGElement_SVGViewElement() throws Exception {
        isParentOf("SVGElement", "SVGViewElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SimpleArray_MimeTypeArray() throws Exception {
        isParentOf("SimpleArray", "MimeTypeArray");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SimpleArray_Plugin() throws Exception {
        isParentOf("SimpleArray", "Plugin");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SimpleArray_PluginArray() throws Exception {
        isParentOf("SimpleArray", "PluginArray");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _Text_CDATASection() throws Exception {
        isParentOf("Text", "CDATASection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _UIEvent_KeyboardEvent() throws Exception {
        isParentOf("UIEvent", "KeyboardEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _UIEvent_MouseEvent() throws Exception {
        isParentOf("UIEvent", "MouseEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE11 = "true")
    public void _UIEvent_PointerEvent() throws Exception {
        isParentOf("UIEvent", "PointerEvent");
    }

}
