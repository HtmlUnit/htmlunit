/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.general.huge;

import java.util.Collection;

import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.params.provider.Arguments;

/**
 * Tests two Host classes, if one prototype is parent of another.
 *
 * This class handles all host names which starts by character 'M' to 'O'.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HostParentOfNTest extends HostParentOf {

    /**
     * Returns the parameterized data.
     *
     * @return the parameterized data
     * @throws Exception
     *             if an error occurs
     */
    public static Collection<Arguments> data() throws Exception {
        return HostParentOf.data(input -> {
            final char ch = Character.toUpperCase(input.charAt(0));
            return ch >= 'N' && ch <= 'O';
        });
    }

    @Alerts("true/false")
    void _NamedNodeMap_NamedNodeMap() throws Exception {
        test("NamedNodeMap", "NamedNodeMap");
    }

    @Alerts("true/false")
    void _Navigator_Navigator() throws Exception {
        test("Navigator", "Navigator");
    }

    @Alerts(DEFAULT = "true/false",
            FF = "false/false",
            FF_ESR = "false/false")
    void _NetworkInformation_NetworkInformation() throws Exception {
        test("NetworkInformation", "NetworkInformation");
    }

    @Alerts("true/true")
    void _Node_Attr() throws Exception {
        test("Node", "Attr");
    }

    @Alerts("true/false")
    void _Node_Audio() throws Exception {
        test("Node", "Audio");
    }

    @Alerts("true/false")
    void _Node_CDATASection() throws Exception {
        test("Node", "CDATASection");
    }

    @Alerts("true/true")
    void _Node_CharacterData() throws Exception {
        test("Node", "CharacterData");
    }

    @Alerts("true/false")
    void _Node_Comment() throws Exception {
        test("Node", "Comment");
    }

    @Alerts("true/true")
    void _Node_Document() throws Exception {
        test("Node", "Document");
    }

    @Alerts("true/true")
    void _Node_DocumentFragment() throws Exception {
        test("Node", "DocumentFragment");
    }

    @Alerts("true/true")
    void _Node_DocumentType() throws Exception {
        test("Node", "DocumentType");
    }

    @Alerts("true/true")
    void _Node_Element() throws Exception {
        test("Node", "Element");
    }

    @Alerts("true/false")
    void _Node_HTMLAnchorElement() throws Exception {
        test("Node", "HTMLAnchorElement");
    }

    @Alerts("false/false")
    void _Node_HTMLAppletElement() throws Exception {
        test("Node", "HTMLAppletElement");
    }

    @Alerts("true/false")
    void _Node_HTMLAreaElement() throws Exception {
        test("Node", "HTMLAreaElement");
    }

    @Alerts("true/false")
    void _Node_HTMLAudioElement() throws Exception {
        test("Node", "HTMLAudioElement");
    }

    @Alerts("true/false")
    void _Node_HTMLBaseElement() throws Exception {
        test("Node", "HTMLBaseElement");
    }

    @Alerts("false/false")
    void _Node_HTMLBaseFontElement() throws Exception {
        test("Node", "HTMLBaseFontElement");
    }

    @Alerts("false/false")
    void _Node_HTMLBGSoundElement() throws Exception {
        test("Node", "HTMLBGSoundElement");
    }

    @Alerts("false/false")
    void _Node_HTMLBlockElement() throws Exception {
        test("Node", "HTMLBlockElement");
    }

    @Alerts("true/false")
    void _Node_HTMLBodyElement() throws Exception {
        test("Node", "HTMLBodyElement");
    }

    @Alerts("true/false")
    void _Node_HTMLBRElement() throws Exception {
        test("Node", "HTMLBRElement");
    }

    @Alerts("true/false")
    void _Node_HTMLButtonElement() throws Exception {
        test("Node", "HTMLButtonElement");
    }

    @Alerts("true/false")
    void _Node_HTMLCanvasElement() throws Exception {
        test("Node", "HTMLCanvasElement");
    }

    @Alerts("true/false")
    void _Node_HTMLDataElement() throws Exception {
        test("Node", "HTMLDataElement");
    }

    @Alerts("true/false")
    void _Node_HTMLDataListElement() throws Exception {
        test("Node", "HTMLDataListElement");
    }

    @Alerts("false/false")
    void _Node_HTMLDDElement() throws Exception {
        test("Node", "HTMLDDElement");
    }

    @Alerts("true/false")
    void _Node_HTMLDetailsElement() throws Exception {
        test("Node", "HTMLDetailsElement");
    }

    @Alerts("true/false")
    void _Node_HTMLDialogElement() throws Exception {
        test("Node", "HTMLDialogElement");
    }

    @Alerts("true/false")
    void _Node_HTMLDirectoryElement() throws Exception {
        test("Node", "HTMLDirectoryElement");
    }

    @Alerts("true/false")
    void _Node_HTMLDivElement() throws Exception {
        test("Node", "HTMLDivElement");
    }

    @Alerts("true/false")
    void _Node_HTMLDListElement() throws Exception {
        test("Node", "HTMLDListElement");
    }

    @Alerts("true/false")
    void _Node_HTMLDocument() throws Exception {
        test("Node", "HTMLDocument");
    }

    @Alerts("false/false")
    void _Node_HTMLDTElement() throws Exception {
        test("Node", "HTMLDTElement");
    }

    @Alerts("true/false")
    void _Node_HTMLElement() throws Exception {
        test("Node", "HTMLElement");
    }

    @Alerts("true/false")
    void _Node_HTMLEmbedElement() throws Exception {
        test("Node", "HTMLEmbedElement");
    }

    @Alerts("true/false")
    void _Node_HTMLFieldSetElement() throws Exception {
        test("Node", "HTMLFieldSetElement");
    }

    @Alerts("true/false")
    void _Node_HTMLFontElement() throws Exception {
        test("Node", "HTMLFontElement");
    }

    @Alerts("true/false")
    void _Node_HTMLFormElement() throws Exception {
        test("Node", "HTMLFormElement");
    }

    @Alerts("true/false")
    void _Node_HTMLFrameElement() throws Exception {
        test("Node", "HTMLFrameElement");
    }

    @Alerts("true/false")
    void _Node_HTMLFrameSetElement() throws Exception {
        test("Node", "HTMLFrameSetElement");
    }

    @Alerts("true/false")
    void _Node_HTMLHeadElement() throws Exception {
        test("Node", "HTMLHeadElement");
    }

    @Alerts("true/false")
    void _Node_HTMLHeadingElement() throws Exception {
        test("Node", "HTMLHeadingElement");
    }

    @Alerts("true/false")
    void _Node_HTMLHRElement() throws Exception {
        test("Node", "HTMLHRElement");
    }

    @Alerts("true/false")
    void _Node_HTMLHtmlElement() throws Exception {
        test("Node", "HTMLHtmlElement");
    }

    @Alerts("true/false")
    void _Node_HTMLIFrameElement() throws Exception {
        test("Node", "HTMLIFrameElement");
    }

    @Alerts("true/false")
    void _Node_HTMLImageElement() throws Exception {
        test("Node", "HTMLImageElement");
    }

    @Alerts("true/false")
    void _Node_HTMLInputElement() throws Exception {
        test("Node", "HTMLInputElement");
    }

    @Alerts("false/false")
    void _Node_HTMLIsIndexElement() throws Exception {
        test("Node", "HTMLIsIndexElement");
    }

    @Alerts("true/false")
    void _Node_HTMLLabelElement() throws Exception {
        test("Node", "HTMLLabelElement");
    }

    @Alerts("true/false")
    void _Node_HTMLLegendElement() throws Exception {
        test("Node", "HTMLLegendElement");
    }

    @Alerts("true/false")
    void _Node_HTMLLIElement() throws Exception {
        test("Node", "HTMLLIElement");
    }

    @Alerts("true/false")
    void _Node_HTMLLinkElement() throws Exception {
        test("Node", "HTMLLinkElement");
    }

    @Alerts("true/false")
    void _Node_HTMLMapElement() throws Exception {
        test("Node", "HTMLMapElement");
    }

    @Alerts("true/false")
    void _Node_HTMLMarqueeElement() throws Exception {
        test("Node", "HTMLMarqueeElement");
    }

    @Alerts("true/false")
    void _Node_HTMLMediaElement() throws Exception {
        test("Node", "HTMLMediaElement");
    }

    @Alerts("true/false")
    void _Node_HTMLMenuElement() throws Exception {
        test("Node", "HTMLMenuElement");
    }

    @Alerts("false/false")
    void _Node_HTMLMenuItemElement() throws Exception {
        test("Node", "HTMLMenuItemElement");
    }

    @Alerts("true/false")
    void _Node_HTMLMetaElement() throws Exception {
        test("Node", "HTMLMetaElement");
    }

    @Alerts("true/false")
    void _Node_HTMLMeterElement() throws Exception {
        test("Node", "HTMLMeterElement");
    }

    @Alerts("true/false")
    void _Node_HTMLModElement() throws Exception {
        test("Node", "HTMLModElement");
    }

    @Alerts("false/false")
    void _Node_HTMLNextIdElement() throws Exception {
        test("Node", "HTMLNextIdElement");
    }

    @Alerts("true/false")
    void _Node_HTMLObjectElement() throws Exception {
        test("Node", "HTMLObjectElement");
    }

    @Alerts("true/false")
    void _Node_HTMLOListElement() throws Exception {
        test("Node", "HTMLOListElement");
    }

    @Alerts("true/false")
    void _Node_HTMLOptGroupElement() throws Exception {
        test("Node", "HTMLOptGroupElement");
    }

    @Alerts("true/false")
    void _Node_HTMLOptionElement() throws Exception {
        test("Node", "HTMLOptionElement");
    }

    @Alerts("true/false")
    void _Node_HTMLOutputElement() throws Exception {
        test("Node", "HTMLOutputElement");
    }

    @Alerts("true/false")
    void _Node_HTMLParagraphElement() throws Exception {
        test("Node", "HTMLParagraphElement");
    }

    @Alerts("true/false")
    void _Node_HTMLParamElement() throws Exception {
        test("Node", "HTMLParamElement");
    }

    @Alerts("false/false")
    void _Node_HTMLPhraseElement() throws Exception {
        test("Node", "HTMLPhraseElement");
    }

    @Alerts("true/false")
    void _Node_HTMLPictureElement() throws Exception {
        test("Node", "HTMLPictureElement");
    }

    @Alerts("true/false")
    void _Node_HTMLPreElement() throws Exception {
        test("Node", "HTMLPreElement");
    }

    @Alerts("true/false")
    void _Node_HTMLProgressElement() throws Exception {
        test("Node", "HTMLProgressElement");
    }

    @Alerts("true/false")
    void _Node_HTMLQuoteElement() throws Exception {
        test("Node", "HTMLQuoteElement");
    }

    @Alerts("true/false")
    void _Node_HTMLScriptElement() throws Exception {
        test("Node", "HTMLScriptElement");
    }

    @Alerts("true/false")
    void _Node_HTMLSelectElement() throws Exception {
        test("Node", "HTMLSelectElement");
    }

    @Alerts("true/false")
    void _Node_HTMLSlotElement() throws Exception {
        test("Node", "HTMLSlotElement");
    }

    @Alerts("true/false")
    void _Node_HTMLSourceElement() throws Exception {
        test("Node", "HTMLSourceElement");
    }

    @Alerts("true/false")
    void _Node_HTMLSpanElement() throws Exception {
        test("Node", "HTMLSpanElement");
    }

    @Alerts("true/false")
    void _Node_HTMLStyleElement() throws Exception {
        test("Node", "HTMLStyleElement");
    }

    @Alerts("true/false")
    void _Node_HTMLTableCaptionElement() throws Exception {
        test("Node", "HTMLTableCaptionElement");
    }

    @Alerts("true/false")
    void _Node_HTMLTableCellElement() throws Exception {
        test("Node", "HTMLTableCellElement");
    }

    @Alerts("true/false")
    void _Node_HTMLTableColElement() throws Exception {
        test("Node", "HTMLTableColElement");
    }

    @Alerts("false/false")
    void _Node_HTMLTableDataCellElement() throws Exception {
        test("Node", "HTMLTableDataCellElement");
    }

    @Alerts("true/false")
    void _Node_HTMLTableElement() throws Exception {
        test("Node", "HTMLTableElement");
    }

    @Alerts("false/false")
    void _Node_HTMLTableHeaderCellElement() throws Exception {
        test("Node", "HTMLTableHeaderCellElement");
    }

    @Alerts("true/false")
    void _Node_HTMLTableRowElement() throws Exception {
        test("Node", "HTMLTableRowElement");
    }

    @Alerts("true/false")
    void _Node_HTMLTableSectionElement() throws Exception {
        test("Node", "HTMLTableSectionElement");
    }

    @Alerts("true/false")
    void _Node_HTMLTemplateElement() throws Exception {
        test("Node", "HTMLTemplateElement");
    }

    @Alerts("true/false")
    void _Node_HTMLTextAreaElement() throws Exception {
        test("Node", "HTMLTextAreaElement");
    }

    @Alerts("true/false")
    void _Node_HTMLTimeElement() throws Exception {
        test("Node", "HTMLTimeElement");
    }

    @Alerts("true/false")
    void _Node_HTMLTitleElement() throws Exception {
        test("Node", "HTMLTitleElement");
    }

    @Alerts("true/false")
    void _Node_HTMLTrackElement() throws Exception {
        test("Node", "HTMLTrackElement");
    }

    @Alerts("true/false")
    void _Node_HTMLUListElement() throws Exception {
        test("Node", "HTMLUListElement");
    }

    @Alerts("true/false")
    void _Node_HTMLUnknownElement() throws Exception {
        test("Node", "HTMLUnknownElement");
    }

    @Alerts("true/false")
    void _Node_HTMLVideoElement() throws Exception {
        test("Node", "HTMLVideoElement");
    }

    @Alerts("true/false")
    void _Node_Image() throws Exception {
        test("Node", "Image");
    }

    @Alerts("true/false")
    void _Node_Node() throws Exception {
        test("Node", "Node");
    }

    @Alerts("true/false")
    void _Node_Option() throws Exception {
        test("Node", "Option");
    }

    @Alerts("true/false")
    void _Node_ProcessingInstruction() throws Exception {
        test("Node", "ProcessingInstruction");
    }

    @Alerts("true/false")
    void _Node_ShadowRoot() throws Exception {
        test("Node", "ShadowRoot");
    }

    @Alerts("true/false")
    void _Node_SVGAElement() throws Exception {
        test("Node", "SVGAElement");
    }

    @Alerts("true/false")
    void _Node_SVGAnimateElement() throws Exception {
        test("Node", "SVGAnimateElement");
    }

    @Alerts("true/false")
    void _Node_SVGAnimateMotionElement() throws Exception {
        test("Node", "SVGAnimateMotionElement");
    }

    @Alerts("true/false")
    void _Node_SVGAnimateTransformElement() throws Exception {
        test("Node", "SVGAnimateTransformElement");
    }

    @Alerts("true/false")
    void _Node_SVGAnimationElement() throws Exception {
        test("Node", "SVGAnimationElement");
    }

    @Alerts("true/false")
    void _Node_SVGCircleElement() throws Exception {
        test("Node", "SVGCircleElement");
    }

    @Alerts("true/false")
    void _Node_SVGClipPathElement() throws Exception {
        test("Node", "SVGClipPathElement");
    }

    @Alerts("true/false")
    void _Node_SVGComponentTransferFunctionElement() throws Exception {
        test("Node", "SVGComponentTransferFunctionElement");
    }

    @Alerts("true/false")
    void _Node_SVGDefsElement() throws Exception {
        test("Node", "SVGDefsElement");
    }

    @Alerts("true/false")
    void _Node_SVGDescElement() throws Exception {
        test("Node", "SVGDescElement");
    }

    @Alerts("false/false")
    void _Node_SVGDiscardElement() throws Exception {
        test("Node", "SVGDiscardElement");
    }

    @Alerts("true/false")
    void _Node_SVGElement() throws Exception {
        test("Node", "SVGElement");
    }

    @Alerts("true/false")
    void _Node_SVGEllipseElement() throws Exception {
        test("Node", "SVGEllipseElement");
    }

    @Alerts("true/false")
    void _Node_SVGFEBlendElement() throws Exception {
        test("Node", "SVGFEBlendElement");
    }

    @Alerts("true/false")
    void _Node_SVGFEColorMatrixElement() throws Exception {
        test("Node", "SVGFEColorMatrixElement");
    }

    @Alerts("true/false")
    void _Node_SVGFEComponentTransferElement() throws Exception {
        test("Node", "SVGFEComponentTransferElement");
    }

    @Alerts("true/false")
    void _Node_SVGFECompositeElement() throws Exception {
        test("Node", "SVGFECompositeElement");
    }

    @Alerts("true/false")
    void _Node_SVGFEConvolveMatrixElement() throws Exception {
        test("Node", "SVGFEConvolveMatrixElement");
    }

    @Alerts("true/false")
    void _Node_SVGFEDiffuseLightingElement() throws Exception {
        test("Node", "SVGFEDiffuseLightingElement");
    }

    @Alerts("true/false")
    void _Node_SVGFEDisplacementMapElement() throws Exception {
        test("Node", "SVGFEDisplacementMapElement");
    }

    @Alerts("true/false")
    void _Node_SVGFEDistantLightElement() throws Exception {
        test("Node", "SVGFEDistantLightElement");
    }

    @Alerts("true/false")
    void _Node_SVGFEDropShadowElement() throws Exception {
        test("Node", "SVGFEDropShadowElement");
    }

    @Alerts("true/false")
    void _Node_SVGFEFloodElement() throws Exception {
        test("Node", "SVGFEFloodElement");
    }

    @Alerts("true/false")
    void _Node_SVGFEFuncAElement() throws Exception {
        test("Node", "SVGFEFuncAElement");
    }

    @Alerts("true/false")
    void _Node_SVGFEFuncBElement() throws Exception {
        test("Node", "SVGFEFuncBElement");
    }

    @Alerts("true/false")
    void _Node_SVGFEFuncGElement() throws Exception {
        test("Node", "SVGFEFuncGElement");
    }

    @Alerts("true/false")
    void _Node_SVGFEFuncRElement() throws Exception {
        test("Node", "SVGFEFuncRElement");
    }

    @Alerts("true/false")
    void _Node_SVGFEGaussianBlurElement() throws Exception {
        test("Node", "SVGFEGaussianBlurElement");
    }

    @Alerts("true/false")
    void _Node_SVGFEImageElement() throws Exception {
        test("Node", "SVGFEImageElement");
    }

    @Alerts("true/false")
    void _Node_SVGFEMergeElement() throws Exception {
        test("Node", "SVGFEMergeElement");
    }

    @Alerts("true/false")
    void _Node_SVGFEMergeNodeElement() throws Exception {
        test("Node", "SVGFEMergeNodeElement");
    }

    @Alerts("true/false")
    void _Node_SVGFEMorphologyElement() throws Exception {
        test("Node", "SVGFEMorphologyElement");
    }

    @Alerts("true/false")
    void _Node_SVGFEOffsetElement() throws Exception {
        test("Node", "SVGFEOffsetElement");
    }

    @Alerts("true/false")
    void _Node_SVGFEPointLightElement() throws Exception {
        test("Node", "SVGFEPointLightElement");
    }

    @Alerts("true/false")
    void _Node_SVGFESpecularLightingElement() throws Exception {
        test("Node", "SVGFESpecularLightingElement");
    }

    @Alerts("true/false")
    void _Node_SVGFESpotLightElement() throws Exception {
        test("Node", "SVGFESpotLightElement");
    }

    @Alerts("true/false")
    void _Node_SVGFETileElement() throws Exception {
        test("Node", "SVGFETileElement");
    }

    @Alerts("true/false")
    void _Node_SVGFETurbulenceElement() throws Exception {
        test("Node", "SVGFETurbulenceElement");
    }

    @Alerts("true/false")
    void _Node_SVGFilterElement() throws Exception {
        test("Node", "SVGFilterElement");
    }

    @Alerts("true/false")
    void _Node_SVGForeignObjectElement() throws Exception {
        test("Node", "SVGForeignObjectElement");
    }

    @Alerts("true/false")
    void _Node_SVGGElement() throws Exception {
        test("Node", "SVGGElement");
    }

    @Alerts("true/false")
    void _Node_SVGGeometryElement() throws Exception {
        test("Node", "SVGGeometryElement");
    }

    @Alerts("true/false")
    void _Node_SVGGradientElement() throws Exception {
        test("Node", "SVGGradientElement");
    }

    @Alerts("true/false")
    void _Node_SVGGraphicsElement() throws Exception {
        test("Node", "SVGGraphicsElement");
    }

    @Alerts("true/false")
    void _Node_SVGImageElement() throws Exception {
        test("Node", "SVGImageElement");
    }

    @Alerts("true/false")
    void _Node_SVGLinearGradientElement() throws Exception {
        test("Node", "SVGLinearGradientElement");
    }

    @Alerts("true/false")
    void _Node_SVGLineElement() throws Exception {
        test("Node", "SVGLineElement");
    }

    @Alerts("true/false")
    void _Node_SVGMarkerElement() throws Exception {
        test("Node", "SVGMarkerElement");
    }

    @Alerts("true/false")
    void _Node_SVGMaskElement() throws Exception {
        test("Node", "SVGMaskElement");
    }

    @Alerts("true/false")
    void _Node_SVGMetadataElement() throws Exception {
        test("Node", "SVGMetadataElement");
    }

    @Alerts("true/false")
    void _Node_SVGMPathElement() throws Exception {
        test("Node", "SVGMPathElement");
    }

    @Alerts("true/false")
    void _Node_SVGPathElement() throws Exception {
        test("Node", "SVGPathElement");
    }

    @Alerts("true/false")
    void _Node_SVGPatternElement() throws Exception {
        test("Node", "SVGPatternElement");
    }

    @Alerts("true/false")
    void _Node_SVGPolygonElement() throws Exception {
        test("Node", "SVGPolygonElement");
    }

    @Alerts("true/false")
    void _Node_SVGPolylineElement() throws Exception {
        test("Node", "SVGPolylineElement");
    }

    @Alerts("true/false")
    void _Node_SVGRadialGradientElement() throws Exception {
        test("Node", "SVGRadialGradientElement");
    }

    @Alerts("true/false")
    void _Node_SVGRectElement() throws Exception {
        test("Node", "SVGRectElement");
    }

    @Alerts("true/false")
    void _Node_SVGScriptElement() throws Exception {
        test("Node", "SVGScriptElement");
    }

    @Alerts("true/false")
    void _Node_SVGSetElement() throws Exception {
        test("Node", "SVGSetElement");
    }

    @Alerts("true/false")
    void _Node_SVGStopElement() throws Exception {
        test("Node", "SVGStopElement");
    }

    @Alerts("true/false")
    void _Node_SVGStyleElement() throws Exception {
        test("Node", "SVGStyleElement");
    }

    @Alerts("true/false")
    void _Node_SVGSVGElement() throws Exception {
        test("Node", "SVGSVGElement");
    }

    @Alerts("true/false")
    void _Node_SVGSwitchElement() throws Exception {
        test("Node", "SVGSwitchElement");
    }

    @Alerts("true/false")
    void _Node_SVGSymbolElement() throws Exception {
        test("Node", "SVGSymbolElement");
    }

    @Alerts("true/false")
    void _Node_SVGTextContentElement() throws Exception {
        test("Node", "SVGTextContentElement");
    }

    @Alerts("true/false")
    void _Node_SVGTextElement() throws Exception {
        test("Node", "SVGTextElement");
    }

    @Alerts("true/false")
    void _Node_SVGTextPathElement() throws Exception {
        test("Node", "SVGTextPathElement");
    }

    @Alerts("true/false")
    void _Node_SVGTextPositioningElement() throws Exception {
        test("Node", "SVGTextPositioningElement");
    }

    @Alerts("true/false")
    void _Node_SVGTitleElement() throws Exception {
        test("Node", "SVGTitleElement");
    }

    @Alerts("true/false")
    void _Node_SVGTSpanElement() throws Exception {
        test("Node", "SVGTSpanElement");
    }

    @Alerts("true/false")
    void _Node_SVGUseElement() throws Exception {
        test("Node", "SVGUseElement");
    }

    @Alerts("true/false")
    void _Node_SVGViewElement() throws Exception {
        test("Node", "SVGViewElement");
    }

    @Alerts("true/false")
    void _Node_Text() throws Exception {
        test("Node", "Text");
    }

    @Alerts("true/false")
    void _Node_XMLDocument() throws Exception {
        test("Node", "XMLDocument");
    }

    @Alerts("false/false")
    @HtmlUnitNYI(CHROME = "true/false",
            EDGE = "true/false",
            FF = "true/false",
            FF_ESR = "true/false")
    void _NodeFilter_NodeFilter() throws Exception {
        test("NodeFilter", "NodeFilter");
    }

    @Alerts("true/false")
    void _NodeIterator_NodeIterator() throws Exception {
        test("NodeIterator", "NodeIterator");
    }

    @Alerts("true/false")
    void _NodeList_NodeList() throws Exception {
        test("NodeList", "NodeList");
    }

    @Alerts("true/true")
    void _NodeList_RadioNodeList() throws Exception {
        test("NodeList", "RadioNodeList");
    }

    @Alerts("true/false")
    void _Notification_Notification() throws Exception {
        test("Notification", "Notification");
    }

    @Alerts("false/false")
    void _OES_element_index_uint_OES_element_index_uint() throws Exception {
        test("OES_element_index_uint", "OES_element_index_uint");
    }

    @Alerts("false/false")
    void _OES_standard_derivatives_OES_standard_derivatives() throws Exception {
        test("OES_standard_derivatives", "OES_standard_derivatives");
    }

    @Alerts("false/false")
    void _OES_texture_float_linear_OES_texture_float_linear() throws Exception {
        test("OES_texture_float_linear", "OES_texture_float_linear");
    }

    @Alerts("false/false")
    void _OES_texture_float_OES_texture_float() throws Exception {
        test("OES_texture_float", "OES_texture_float");
    }

    @Alerts("true/false")
    void _OfflineAudioCompletionEvent_OfflineAudioCompletionEvent() throws Exception {
        test("OfflineAudioCompletionEvent", "OfflineAudioCompletionEvent");
    }

    @Alerts("true/false")
    void _OfflineAudioContext_OfflineAudioContext() throws Exception {
        test("OfflineAudioContext", "OfflineAudioContext");
    }

    @Alerts("false/false")
    void _OfflineResourceList_OfflineResourceList() throws Exception {
        test("OfflineResourceList", "OfflineResourceList");
    }

    @Alerts("true/false")
    void _Option_HTMLOptionElement() throws Exception {
        // although Option != HTMLOptionElement, they seem to be synonyms!!!
        test("Option", "HTMLOptionElement");
    }

    @Alerts("true/false")
    void _Option_Option() throws Exception {
        test("Option", "Option");
    }

    @Alerts("true/false")
    void _OscillatorNode_OscillatorNode() throws Exception {
        test("OscillatorNode", "OscillatorNode");
    }

}
