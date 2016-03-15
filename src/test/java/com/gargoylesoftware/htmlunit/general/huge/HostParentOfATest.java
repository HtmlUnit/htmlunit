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
package com.gargoylesoftware.htmlunit.general.huge;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import com.gargoylesoftware.htmlunit.BrowserParameterizedRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.google.common.base.Predicate;

/**
 * Tests two Host classes, if one prototype is parent of another.
 *
 * This class handles all host names which starts by character 'A' to 'G'.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserParameterizedRunner.class)
public class HostParentOfATest extends HostParentOf {

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    @Parameters
    public static Collection<Object[]> data() throws Exception {
        return HostParentOf.data(new Predicate<String>() {

            @Override
            public boolean apply(final String input) {
                final char ch = Character.toUpperCase(input.charAt(0));
                return ch >= 'A' && ch <= 'G';
            }
        });
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
            FF = "false")
    public void _ApplicationCache_ApplicationCache() throws Exception {
        test("ApplicationCache", "ApplicationCache");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _BeforeUnloadEvent_BeforeUnloadEvent() throws Exception {
        test("BeforeUnloadEvent", "BeforeUnloadEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
    @Alerts("true")
    public void _CSSFontFaceRule_CSSFontFaceRule() throws Exception {
        test("CSSFontFaceRule", "CSSFontFaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSImportRule_CSSImportRule() throws Exception {
        test("CSSImportRule", "CSSImportRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
    @Alerts("true")
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
    @Alerts("true")
    public void _CanvasRenderingContext2D_CanvasRenderingContext2D() throws Exception {
        test("CanvasRenderingContext2D", "CanvasRenderingContext2D");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false")
    public void _ClientRect_ClientRect() throws Exception {
        test("ClientRect", "ClientRect");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Comment_Comment() throws Exception {
        test("Comment", "Comment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Console_Console() throws Exception {
        test("Console", "Console");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Coordinates_Coordinates() throws Exception {
        test("Coordinates", "Coordinates");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _DataView_DataView() throws Exception {
        test("DataView", "DataView");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
    @Alerts("true")
    public void _DOMParser_DOMParser() throws Exception {
        test("DOMParser", "DOMParser");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented
    public void _DOMStringMap_DOMStringMap() throws Exception {
        test("DOMStringMap", "DOMStringMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _DOMTokenList_DOMTokenList() throws Exception {
        test("DOMTokenList", "DOMTokenList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Document_Document() throws Exception {
        test("Document", "Document");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _DocumentFragment_DocumentFragment() throws Exception {
        test("DocumentFragment", "DocumentFragment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
    @Alerts("true")
    public void _Float32Array_Float32Array() throws Exception {
        test("Float32Array", "Float32Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Float64Array_Float64Array() throws Exception {
        test("Float64Array", "Float64Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Geolocation_Geolocation() throws Exception {
        test("Geolocation", "Geolocation");
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
    @Alerts("true")
    public void _CSSRule_CSSFontFaceRule() throws Exception {
        test("CSSRule", "CSSFontFaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSRule_CSSImportRule() throws Exception {
        test("CSSRule", "CSSImportRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSRule_CSSMediaRule() throws Exception {
        test("CSSRule", "CSSMediaRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
    @Alerts("true")
    public void _Document_HTMLDocument() throws Exception {
        test("Document", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Document_XMLDocument() throws Exception {
        test("Document", "XMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Document_SVGDocument() throws Exception {
        test("Document", "SVGDocument");
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
            CHROME = "false")
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
    @Alerts("true")
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
    @Alerts("true")
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
    @Alerts("true")
    public void _Element_HTMLCanvasElement() throws Exception {
        test("Element", "HTMLCanvasElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
    @Alerts("true")
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
    @Alerts("true")
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
    @Alerts("true")
    public void _Element_HTMLMediaElement() throws Exception {
        test("Element", "HTMLMediaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
    @Alerts("true")
    public void _Element_HTMLModElement() throws Exception {
        test("Element", "HTMLModElement");
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
    @Alerts("true")
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
    @Alerts("true")
    public void _Element_HTMLPreElement() throws Exception {
        test("Element", "HTMLPreElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
    @Alerts("true")
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
            IE = "true")
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
            IE = "true")
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
    @Alerts("true")
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
    public void _Element_HTMLUnknownElement() throws Exception {
        test("Element", "HTMLUnknownElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
    @Alerts("true")
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
    @Alerts("true")
    public void _Element_SVGCircleElement() throws Exception {
        test("Element", "SVGCircleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
    @Alerts("true")
    public void _Element_SVGDefsElement() throws Exception {
        test("Element", "SVGDefsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGDescElement() throws Exception {
        test("Element", "SVGDescElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGElement() throws Exception {
        test("Element", "SVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGEllipseElement() throws Exception {
        test("Element", "SVGEllipseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGFEBlendElement() throws Exception {
        test("Element", "SVGFEBlendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGFEColorMatrixElement() throws Exception {
        test("Element", "SVGFEColorMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGFEComponentTransferElement() throws Exception {
        test("Element", "SVGFEComponentTransferElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGFECompositeElement() throws Exception {
        test("Element", "SVGFECompositeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGFEConvolveMatrixElement() throws Exception {
        test("Element", "SVGFEConvolveMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGFEDiffuseLightingElement() throws Exception {
        test("Element", "SVGFEDiffuseLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGFEDisplacementMapElement() throws Exception {
        test("Element", "SVGFEDisplacementMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGFEDistantLightElement() throws Exception {
        test("Element", "SVGFEDistantLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGFEFloodElement() throws Exception {
        test("Element", "SVGFEFloodElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGFEFuncAElement() throws Exception {
        test("Element", "SVGFEFuncAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGFEFuncBElement() throws Exception {
        test("Element", "SVGFEFuncBElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGFEFuncGElement() throws Exception {
        test("Element", "SVGFEFuncGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGFEFuncRElement() throws Exception {
        test("Element", "SVGFEFuncRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGFEGaussianBlurElement() throws Exception {
        test("Element", "SVGFEGaussianBlurElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGFEImageElement() throws Exception {
        test("Element", "SVGFEImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGFEMergeElement() throws Exception {
        test("Element", "SVGFEMergeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGFEMergeNodeElement() throws Exception {
        test("Element", "SVGFEMergeNodeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGFEMorphologyElement() throws Exception {
        test("Element", "SVGFEMorphologyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGFEOffsetElement() throws Exception {
        test("Element", "SVGFEOffsetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGFEPointLightElement() throws Exception {
        test("Element", "SVGFEPointLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGFESpecularLightingElement() throws Exception {
        test("Element", "SVGFESpecularLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGFESpotLightElement() throws Exception {
        test("Element", "SVGFESpotLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGFETileElement() throws Exception {
        test("Element", "SVGFETileElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGFETurbulenceElement() throws Exception {
        test("Element", "SVGFETurbulenceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
    @Alerts("true")
    public void _Element_SVGGElement() throws Exception {
        test("Element", "SVGGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGImageElement() throws Exception {
        test("Element", "SVGImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGLineElement() throws Exception {
        test("Element", "SVGLineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGLinearGradientElement() throws Exception {
        test("Element", "SVGLinearGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGMarkerElement() throws Exception {
        test("Element", "SVGMarkerElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGMaskElement() throws Exception {
        test("Element", "SVGMaskElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
    @Alerts("true")
    public void _Element_SVGPathElement() throws Exception {
        test("Element", "SVGPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGPatternElement() throws Exception {
        test("Element", "SVGPatternElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGPolygonElement() throws Exception {
        test("Element", "SVGPolygonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGPolylineElement() throws Exception {
        test("Element", "SVGPolylineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGRadialGradientElement() throws Exception {
        test("Element", "SVGRadialGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGRectElement() throws Exception {
        test("Element", "SVGRectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGSVGElement() throws Exception {
        test("Element", "SVGSVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
    @Alerts("true")
    public void _Element_SVGStopElement() throws Exception {
        test("Element", "SVGStopElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGStyleElement() throws Exception {
        test("Element", "SVGStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGSwitchElement() throws Exception {
        test("Element", "SVGSwitchElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGSymbolElement() throws Exception {
        test("Element", "SVGSymbolElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGTSpanElement() throws Exception {
        test("Element", "SVGTSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGTextElement() throws Exception {
        test("Element", "SVGTextElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGTextPathElement() throws Exception {
        test("Element", "SVGTextPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGTitleElement() throws Exception {
        test("Element", "SVGTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGUseElement() throws Exception {
        test("Element", "SVGUseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGViewElement() throws Exception {
        test("Element", "SVGViewElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
    @Alerts("true")
    public void _Event_KeyboardEvent() throws Exception {
        test("Event", "KeyboardEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Event_MessageEvent() throws Exception {
        test("Event", "MessageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Event_MouseEvent() throws Exception {
        test("Event", "MouseEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Event_MutationEvent() throws Exception {
        test("Event", "MutationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Event_PointerEvent() throws Exception {
        test("Event", "PointerEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Event_UIEvent() throws Exception {
        test("Event", "UIEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _FormData_FormData() throws Exception {
        test("FormData", "FormData");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGGradientElement() throws Exception {
        test("Element", "SVGGradientElement");
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
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _DOMCursor_DOMCursor() throws Exception {
        test("DOMCursor", "DOMCursor");
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
    @Alerts("true")
    public void _CSSPageRule_CSSPageRule() throws Exception {
        test("CSSPageRule", "CSSPageRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSRule_CSSPageRule() throws Exception {
        test("CSSRule", "CSSPageRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
        CHROME = "true",
        IE = "true")
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
    public void _Element_HTMLContentElement() throws Exception {
        test("Element", "HTMLContentElement");
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
    @Alerts("true")
    public void _CharacterData_CDATASection() throws Exception {
        test("CharacterData", "CDATASection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CharacterData_CharacterData() throws Exception {
        test("CharacterData", "CharacterData");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CharacterData_Comment() throws Exception {
        test("CharacterData", "Comment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CharacterData_ProcessingInstruction() throws Exception {
        test("CharacterData", "ProcessingInstruction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CharacterData_Text() throws Exception {
        test("CharacterData", "Text");
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
            IE = "true")
    public void _CSSKeyframeRule_CSSKeyframeRule() throws Exception {
        test("CSSKeyframeRule", "CSSKeyframeRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            IE = "true")
    public void _CSSKeyframesRule_CSSKeyframesRule() throws Exception {
        test("CSSKeyframesRule", "CSSKeyframesRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            IE = "true")
    public void _CSSRule_CSSKeyframeRule() throws Exception {
        test("CSSRule", "CSSKeyframeRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            IE = "true")
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
    @Alerts("true")
    public void _CanvasGradient_CanvasGradient() throws Exception {
        test("CanvasGradient", "CanvasGradient");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
    @Alerts("true")
    public void _CloseEvent_CloseEvent() throws Exception {
        test("CloseEvent", "CloseEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CompositionEvent_CompositionEvent() throws Exception {
        test("CompositionEvent", "CompositionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
    @Alerts("true")
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
    @Alerts(DEFAULT = "false",
            FF = "true")
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
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _EventTarget_SVGDocument() throws Exception {
        test("EventTarget", "SVGDocument");
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
    @Alerts("true")
    public void _Event_CloseEvent() throws Exception {
        test("Event", "CloseEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Event_CompositionEvent() throws Exception {
        test("Event", "CompositionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
    @Alerts("true")
    public void _Event_ErrorEvent() throws Exception {
        test("Event", "ErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
    @Alerts("true")
    public void _Event_PageTransitionEvent() throws Exception {
        test("Event", "PageTransitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Event_PopStateEvent() throws Exception {
        test("Event", "PopStateEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Event_ProgressEvent() throws Exception {
        test("Event", "ProgressEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
    @Alerts("true")
    public void _Event_TransitionEvent() throws Exception {
        test("Event", "TransitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Event_WheelEvent() throws Exception {
        test("Event", "WheelEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
    @Alerts("true")
    public void _AnimationEvent_AnimationEvent() throws Exception {
        test("AnimationEvent", "AnimationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSSConditionRule_CSSConditionRule() throws Exception {
        test("CSSConditionRule", "CSSConditionRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSSConditionRule_CSSMediaRule() throws Exception {
        test("CSSConditionRule", "CSSMediaRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSSConditionRule_CSSSupportsRule() throws Exception {
        test("CSSConditionRule", "CSSSupportsRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSSGroupingRule_CSSConditionRule() throws Exception {
        test("CSSGroupingRule", "CSSConditionRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _CSSGroupingRule_CSSGroupingRule() throws Exception {
        test("CSSGroupingRule", "CSSGroupingRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _CSSGroupingRule_CSSMediaRule() throws Exception {
        test("CSSGroupingRule", "CSSMediaRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    @NotYetImplemented(CHROME)
    public void _CSSGroupingRule_CSSSupportsRule() throws Exception {
        test("CSSGroupingRule", "CSSSupportsRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSSRule_CSSConditionRule() throws Exception {
        test("CSSRule", "CSSConditionRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _CSSRule_CSSGroupingRule() throws Exception {
        test("CSSRule", "CSSGroupingRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _DeviceLightEvent_DeviceLightEvent() throws Exception {
        test("DeviceLightEvent", "DeviceLightEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _DragEvent_DragEvent() throws Exception {
        test("DragEvent", "DragEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Event_AnimationEvent() throws Exception {
        test("Event", "AnimationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Event_DeviceLightEvent() throws Exception {
        test("Event", "DeviceLightEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Event_DragEvent() throws Exception {
        test("Event", "DragEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Event_MouseScrollEvent() throws Exception {
        test("Event", "MouseScrollEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Event_TimeEvent() throws Exception {
        test("Event", "TimeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Event_UserProximityEvent() throws Exception {
        test("Event", "UserProximityEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            IE = "true")
    public void _CSSNamespaceRule_CSSNamespaceRule() throws Exception {
        test("CSSNamespaceRule", "CSSNamespaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true",
            CHROME = "true")
    public void _CSSRule_CSSNamespaceRule() throws Exception {
        test("CSSRule", "CSSNamespaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Event_MouseWheelEvent() throws Exception {
        test("Event", "MouseWheelEvent");
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
    @Alerts("true")
    public void _Element_SVGTextPositioningElement() throws Exception {
        test("Element", "SVGTextPositioningElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Event_DeviceProximityEvent() throws Exception {
        test("Event", "DeviceProximityEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _DeviceProximityEvent_DeviceProximityEvent() throws Exception {
        test("DeviceProximityEvent", "DeviceProximityEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Event_InputEvent() throws Exception {
        test("Event", "InputEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            FF38 = "true")
    public void _Element_HTMLPictureElement() throws Exception {
        test("Element", "HTMLPictureElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            FF38 = "true")
    public void _EventTarget_HTMLPictureElement() throws Exception {
        test("EventTarget", "HTMLPictureElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_IDBDatabase() throws Exception {
        test("EventTarget", "IDBDatabase");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_IDBOpenDBRequest() throws Exception {
        test("EventTarget", "IDBOpenDBRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_IDBRequest() throws Exception {
        test("EventTarget", "IDBRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_IDBTransaction() throws Exception {
        test("EventTarget", "IDBTransaction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Event_IDBVersionChangeEvent() throws Exception {
        test("Event", "IDBVersionChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_MediaQueryList() throws Exception {
        test("EventTarget", "MediaQueryList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF38 = "false",
            IE = "false")
    public void _EventTarget_MediaSource() throws Exception {
        test("EventTarget", "MediaSource");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_MediaRecorder() throws Exception {
        test("EventTarget", "MediaRecorder");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    @NotYetImplemented(CHROME)
    public void _CSS_CSS() throws Exception {
        test("CSS", "CSS");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_webkitRTCPeerConnection() throws Exception {
        test("EventTarget", "webkitRTCPeerConnection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _EventTarget_mozRTCPeerConnection() throws Exception {
        test("EventTarget", "mozRTCPeerConnection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Event_RTCDataChannelEvent() throws Exception {
        test("Event", "RTCDataChannelEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Event_RTCPeerConnectionIceEvent() throws Exception {
        test("Event", "RTCPeerConnectionIceEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Blob_Blob() throws Exception {
        test("Blob", "Blob");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Blob_File() throws Exception {
        test("Blob", "File");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            FF38 = "true")
    public void _CryptoKey_CryptoKey() throws Exception {
        test("CryptoKey", "CryptoKey");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Crypto_Crypto() throws Exception {
        test("Crypto", "Crypto");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _DOMError_DOMError() throws Exception {
        test("DOMError", "DOMError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _DOMError_FileError() throws Exception {
        test("DOMError", "FileError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _DOMStringList_DOMStringList() throws Exception {
        test("DOMStringList", "DOMStringList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _DataTransfer_DataTransfer() throws Exception {
        test("DataTransfer", "DataTransfer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_FileReader() throws Exception {
        test("EventTarget", "FileReader");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_Performance() throws Exception {
        test("EventTarget", "Performance");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_ServiceWorker() throws Exception {
        test("EventTarget", "ServiceWorker");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_ServiceWorkerContainer() throws Exception {
        test("EventTarget", "ServiceWorkerContainer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_ServiceWorkerRegistration() throws Exception {
        test("EventTarget", "ServiceWorkerRegistration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _FileError_FileError() throws Exception {
        test("FileError", "FileError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _FileList_FileList() throws Exception {
        test("FileList", "FileList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _FileReader_FileReader() throws Exception {
        test("FileReader", "FileReader");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _File_File() throws Exception {
        test("File", "File");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _BlobEvent_BlobEvent() throws Exception {
        test("BlobEvent", "BlobEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CaretPosition_CaretPosition() throws Exception {
        test("CaretPosition", "CaretPosition");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _DOMRequest_DOMCursor() throws Exception {
        test("DOMRequest", "DOMCursor");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _DOMRequest_DOMRequest() throws Exception {
        test("DOMRequest", "DOMRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _EventTarget_DOMCursor() throws Exception {
        test("EventTarget", "DOMCursor");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _EventTarget_DOMRequest() throws Exception {
        test("EventTarget", "DOMRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Event_BlobEvent() throws Exception {
        test("Event", "BlobEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Event_MozContactChangeEvent() throws Exception {
        test("Event", "MozContactChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _Event_MozSettingsEvent() throws Exception {
        test("Event", "MozSettingsEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _GamepadButton_GamepadButton() throws Exception {
        test("GamepadButton", "GamepadButton");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _BatteryManager_BatteryManager() throws Exception {
        test("BatteryManager", "BatteryManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_BatteryManager() throws Exception {
        test("EventTarget", "BatteryManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Gamepad_Gamepad() throws Exception {
        test("Gamepad", "Gamepad");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    @NotYetImplemented(CHROME)
    public void _Error_DOMException() throws Exception {
        test("Error", "DOMException");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Error_Error() throws Exception {
        test("Error", "Error");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF38 = "false",
            IE = "false")
    public void _EventTarget_MediaKeySession() throws Exception {
        test("EventTarget", "MediaKeySession");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF38 = "false",
            IE = "false")
    public void _Event_MediaKeyMessageEvent() throws Exception {
        test("Event", "MediaKeyMessageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF38 = "true")
    public void _AudioNode_StereoPannerNode() throws Exception {
        test("AudioNode", "StereoPannerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF38 = "true")
    public void _BroadcastChannel_BroadcastChannel() throws Exception {
        test("BroadcastChannel", "BroadcastChannel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF38 = "true")
    public void _DOMMatrixReadOnly_DOMMatrix() throws Exception {
        test("DOMMatrixReadOnly", "DOMMatrix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF38 = "true")
    public void _DOMMatrix_DOMMatrix() throws Exception {
        test("DOMMatrix", "DOMMatrix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF38 = "true")
    public void _DOMMatrixReadOnly_DOMMatrixReadOnly() throws Exception {
        test("DOMMatrixReadOnly", "DOMMatrixReadOnly");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF38 = "true")
    public void _EventTarget_BroadcastChannel() throws Exception {
        test("EventTarget", "BroadcastChannel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF38 = "true")
    public void _CSSCounterStyleRule_CSSCounterStyleRule() throws Exception {
        test("CSSCounterStyleRule", "CSSCounterStyleRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF38 = "true")
    public void _CSSRule_CSSCounterStyleRule() throws Exception {
        test("CSSRule", "CSSCounterStyleRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            FF38 = "true")
    public void _EventTarget_MediaDevices() throws Exception {
        test("EventTarget", "MediaDevices");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF38 = "true")
    public void _EventTarget_StereoPannerNode() throws Exception {
        test("EventTarget", "StereoPannerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _BarProp_BarProp() throws Exception {
        test("BarProp", "BarProp");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_SpeechSynthesisUtterance() throws Exception {
        test("EventTarget", "SpeechSynthesisUtterance");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_webkitSpeechRecognition() throws Exception {
        test("EventTarget", "webkitSpeechRecognition");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _AutocompleteErrorEvent_AutocompleteErrorEvent() throws Exception {
        test("AutocompleteErrorEvent", "AutocompleteErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _DOMSettableTokenList_DOMSettableTokenList() throws Exception {
        test("DOMSettableTokenList", "DOMSettableTokenList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _DOMTokenList_DOMSettableTokenList() throws Exception {
        test("DOMTokenList", "DOMSettableTokenList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _DataTransferItemList_DataTransferItemList() throws Exception {
        test("DataTransferItemList", "DataTransferItemList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _DataTransferItem_DataTransferItem() throws Exception {
        test("DataTransferItem", "DataTransferItem");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Element_HTMLTemplateElement() throws Exception {
        test("Element", "HTMLTemplateElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGComponentTransferFunctionElement() throws Exception {
        test("Element", "SVGComponentTransferFunctionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Element_SVGFEDropShadowElement() throws Exception {
        test("Element", "SVGFEDropShadowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Element_SVGGraphicsElement() throws Exception {
        test("Element", "SVGGraphicsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Element_SVGTextContentElement() throws Exception {
        test("Element", "SVGTextContentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_HTMLTemplateElement() throws Exception {
        test("EventTarget", "HTMLTemplateElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGComponentTransferFunctionElement() throws Exception {
        test("EventTarget", "SVGComponentTransferFunctionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGFEDropShadowElement() throws Exception {
        test("EventTarget", "SVGFEDropShadowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGGraphicsElement() throws Exception {
        test("EventTarget", "SVGGraphicsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_SVGTextContentElement() throws Exception {
        test("EventTarget", "SVGTextContentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_TextTrack() throws Exception {
        test("EventTarget", "TextTrack");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF38 = "false",
            IE = "false")
    public void _EventTarget_TextTrackCue() throws Exception {
        test("EventTarget", "TextTrackCue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_TextTrackList() throws Exception {
        test("EventTarget", "TextTrackList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_VTTCue() throws Exception {
        test("EventTarget", "VTTCue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            FF38 = "true")
    public void _EventTarget_XMLHttpRequestEventTarget() throws Exception {
        test("EventTarget", "XMLHttpRequestEventTarget");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _EventTarget_XMLHttpRequestUpload() throws Exception {
        test("EventTarget", "XMLHttpRequestUpload");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Event_AutocompleteErrorEvent() throws Exception {
        test("Event", "AutocompleteErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Event_SVGZoomEvent() throws Exception {
        test("Event", "SVGZoomEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Event_SecurityPolicyViolationEvent() throws Exception {
        test("Event", "SecurityPolicyViolationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Event_SpeechSynthesisEvent() throws Exception {
        test("Event", "SpeechSynthesisEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false")
    public void _Event_TextEvent() throws Exception {
        test("Event", "TextEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Event_TrackEvent() throws Exception {
        test("Event", "TrackEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            IE = "true")
    public void _Event_WebGLContextEvent() throws Exception {
        test("Event", "WebGLContextEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Event_XMLHttpRequestProgressEvent() throws Exception {
        test("Event", "XMLHttpRequestProgressEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _ApplicationCacheErrorEvent_ApplicationCacheErrorEvent() throws Exception {
        test("ApplicationCacheErrorEvent", "ApplicationCacheErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _CSSRule_CSSViewportRule() throws Exception {
        test("CSSRule", "CSSViewportRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _CSSViewportRule_CSSViewportRule() throws Exception {
        test("CSSViewportRule", "CSSViewportRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Element_SVGDiscardElement() throws Exception {
        test("Element", "SVGDiscardElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Element_SVGGeometryElement() throws Exception {
        test("Element", "SVGGeometryElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_SVGDiscardElement() throws Exception {
        test("EventTarget", "SVGDiscardElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_SVGGeometryElement() throws Exception {
        test("EventTarget", "SVGGeometryElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_ScreenOrientation() throws Exception {
        test("EventTarget", "ScreenOrientation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Event_ApplicationCacheErrorEvent() throws Exception {
        test("Event", "ApplicationCacheErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF38 = "false",
            IE = "false")
    public void _Event_MediaEncryptedEvent() throws Exception {
        test("Event", "MediaEncryptedEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Event_MediaQueryListEvent() throws Exception {
        test("Event", "MediaQueryListEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF38 = "false",
            IE = "false")
    public void _FontFace_FontFace() throws Exception {
        test("FontFace", "FontFace");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _DOMPointReadOnly_DOMPoint() throws Exception {
        test("DOMPointReadOnly", "DOMPoint");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _DOMPointReadOnly_DOMPointReadOnly() throws Exception {
        test("DOMPointReadOnly", "DOMPointReadOnly");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _DOMPoint_DOMPoint() throws Exception {
        test("DOMPoint", "DOMPoint");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _DOMRectReadOnly_DOMRectReadOnly() throws Exception {
        test("DOMRectReadOnly", "DOMRectReadOnly");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF38 = "false",
            IE = "false")
    public void _CacheStorage_CacheStorage() throws Exception {
        test("CacheStorage", "CacheStorage");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF38 = "false",
            IE = "false")
    public void _Cache_Cache() throws Exception {
        test("Cache", "Cache");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_MIDIAccess() throws Exception {
        test("EventTarget", "MIDIAccess");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_MIDIInput() throws Exception {
        test("EventTarget", "MIDIInput");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_MIDIOutput() throws Exception {
        test("EventTarget", "MIDIOutput");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_MIDIPort() throws Exception {
        test("EventTarget", "MIDIPort");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_PermissionStatus() throws Exception {
        test("EventTarget", "PermissionStatus");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Event_MIDIConnectionEvent() throws Exception {
        test("Event", "MIDIConnectionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Event_MIDIMessageEvent() throws Exception {
        test("Event", "MIDIMessageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _AppBannerPromptResult_AppBannerPromptResult() throws Exception {
        test("AppBannerPromptResult", "AppBannerPromptResult");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Event_ServiceWorkerMessageEvent() throws Exception {
        test("Event", "ServiceWorkerMessageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _BeforeInstallPromptEvent_BeforeInstallPromptEvent() throws Exception {
        test("BeforeInstallPromptEvent", "BeforeInstallPromptEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Event_BeforeInstallPromptEvent() throws Exception {
        test("Event", "BeforeInstallPromptEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _AnimationEvent_WebKitAnimationEvent() throws Exception {
        test("AnimationEvent", "WebKitAnimationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Event_WebKitAnimationEvent() throws Exception {
        test("Event", "WebKitAnimationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _AudioContext_webkitAudioContext() throws Exception {
        test("AudioContext", "webkitAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _AudioContext_webkitOfflineAudioContext() throws Exception {
        test("AudioContext", "webkitOfflineAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_webkitAudioContext() throws Exception {
        test("EventTarget", "webkitAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_webkitIDBDatabase() throws Exception {
        test("EventTarget", "webkitIDBDatabase");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_webkitIDBRequest() throws Exception {
        test("EventTarget", "webkitIDBRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_webkitIDBTransaction() throws Exception {
        test("EventTarget", "webkitIDBTransaction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_webkitMediaStream() throws Exception {
        test("EventTarget", "webkitMediaStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_webkitOfflineAudioContext() throws Exception {
        test("EventTarget", "webkitOfflineAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Event_WebKitTransitionEvent() throws Exception {
        test("Event", "WebKitTransitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Event_webkitSpeechRecognitionError() throws Exception {
        test("Event", "webkitSpeechRecognitionError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Event_webkitSpeechRecognitionEvent() throws Exception {
        test("Event", "webkitSpeechRecognitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _EXT_texture_filter_anisotropic_EXT_texture_filter_anisotropic() throws Exception {
        test("EXT_texture_filter_anisotropic", "EXT_texture_filter_anisotropic");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _EventTarget_IDBMutableFile() throws Exception {
        test("EventTarget", "IDBMutableFile");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Event_MSGestureEvent() throws Exception {
        test("Event", "MSGestureEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Event_PresentationConnectionAvailableEvent() throws Exception {
        test("Event", "PresentationConnectionAvailableEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _AudioNode_IIRFilterNode() throws Exception {
        test("AudioNode", "IIRFilterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _EventTarget_IIRFilterNode() throws Exception {
        test("EventTarget", "IIRFilterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Event_PromiseRejectionEvent() throws Exception {
        test("Event", "PromiseRejectionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF45 = "true")
    public void _CanvasCaptureMediaStream_CanvasCaptureMediaStream() throws Exception {
        test("CanvasCaptureMediaStream", "CanvasCaptureMediaStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF45 = "true")
    public void _EventTarget_CanvasCaptureMediaStream() throws Exception {
        test("EventTarget", "CanvasCaptureMediaStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF45 = "true")
    public void _EventTarget_FontFaceSet() throws Exception {
        test("EventTarget", "FontFaceSet");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF45 = "true")
    public void _EventTarget_RTCPeerConnection() throws Exception {
        test("EventTarget", "RTCPeerConnection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF45 = "true")
    public void _EventTarget_SourceBuffer() throws Exception {
        test("EventTarget", "SourceBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF45 = "true")
    public void _EventTarget_SourceBufferList() throws Exception {
        test("EventTarget", "SourceBufferList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF45 = "true")
    public void _Event_MediaKeyError() throws Exception {
        test("Event", "MediaKeyError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF45 = "true")
    public void _FontFaceSet_FontFaceSet() throws Exception {
        test("FontFaceSet", "FontFaceSet");
    }

}
