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
 * This class handles all host names which starts by character 'D' to 'E'.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HostParentOfDTest extends HostParentOf {

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    public static Collection<Arguments> data() throws Exception {
        return HostParentOf.data(input -> {
            final char ch = Character.toUpperCase(input.charAt(0));
            return ch >= 'D' && ch <= 'E';
        });
    }

    @Alerts("true/false")
    void _DataTransfer_DataTransfer() throws Exception {
        test("DataTransfer", "DataTransfer");
    }

    @Alerts("true/false")
    void _DataTransferItem_DataTransferItem() throws Exception {
        test("DataTransferItem", "DataTransferItem");
    }

    @Alerts("true/false")
    void _DataTransferItemList_DataTransferItemList() throws Exception {
        test("DataTransferItemList", "DataTransferItemList");
    }

    @Alerts("true/false")
    void _DataView_DataView() throws Exception {
        test("DataView", "DataView");
    }

    @Alerts("true/false")
    void _DelayNode_DelayNode() throws Exception {
        test("DelayNode", "DelayNode");
    }

    @Alerts("false/false")
    void _DeviceLightEvent_DeviceLightEvent() throws Exception {
        test("DeviceLightEvent", "DeviceLightEvent");
    }

    @Alerts("true/false")
    void _DeviceMotionEvent_DeviceMotionEvent() throws Exception {
        test("DeviceMotionEvent", "DeviceMotionEvent");
    }

    @Alerts("true/false")
    void _DeviceOrientationEvent_DeviceOrientationEvent() throws Exception {
        test("DeviceOrientationEvent", "DeviceOrientationEvent");
    }

    @Alerts("false/false")
    void _DeviceProximityEvent_DeviceProximityEvent() throws Exception {
        test("DeviceProximityEvent", "DeviceProximityEvent");
    }

    @Alerts("true/false")
    void _Document_Document() throws Exception {
        test("Document", "Document");
    }

    @Alerts("true/true")
    void _Document_HTMLDocument() throws Exception {
        test("Document", "HTMLDocument");
    }

    @Alerts("true/true")
    void _Document_XMLDocument() throws Exception {
        test("Document", "XMLDocument");
    }

    @Alerts("true/false")
    void _DocumentFragment_DocumentFragment() throws Exception {
        test("DocumentFragment", "DocumentFragment");
    }

    @Alerts("true/true")
    void _DocumentFragment_ShadowRoot() throws Exception {
        test("DocumentFragment", "ShadowRoot");
    }

    @Alerts("true/false")
    void _DocumentType_DocumentType() throws Exception {
        test("DocumentType", "DocumentType");
    }

    @Alerts("false/false")
    void _DOMCursor_DOMCursor() throws Exception {
        test("DOMCursor", "DOMCursor");
    }

    @Alerts(DEFAULT = "true/false",
            FF = "false/false",
            FF_ESR = "false/false")
    void _DOMError_DOMError() throws Exception {
        test("DOMError", "DOMError");
    }

    @Alerts("true/false")
    void _DOMException_DOMException() throws Exception {
        test("DOMException", "DOMException");
    }

    @Alerts("true/false")
    void _DOMImplementation_DOMImplementation() throws Exception {
        test("DOMImplementation", "DOMImplementation");
    }

    @Alerts("true/false")
    void _DOMMatrix_DOMMatrix() throws Exception {
        test("DOMMatrix", "DOMMatrix");
    }

    @Alerts("true/false")
    void _DOMMatrix_WebKitCSSMatrix() throws Exception {
        test("DOMMatrix", "WebKitCSSMatrix");
    }

    @Alerts("true/true")
    void _DOMMatrixReadOnly_DOMMatrix() throws Exception {
        test("DOMMatrixReadOnly", "DOMMatrix");
    }

    @Alerts("true/false")
    void _DOMMatrixReadOnly_DOMMatrixReadOnly() throws Exception {
        test("DOMMatrixReadOnly", "DOMMatrixReadOnly");
    }

    @Alerts("true/true")
    void _DOMMatrixReadOnly_WebKitCSSMatrix() throws Exception {
        test("DOMMatrixReadOnly", "WebKitCSSMatrix");
    }

    @Alerts("true/false")
    void _DOMParser_DOMParser() throws Exception {
        test("DOMParser", "DOMParser");
    }

    @Alerts("true/false")
    void _DOMPoint_DOMPoint() throws Exception {
        test("DOMPoint", "DOMPoint");
    }

    @Alerts("true/true")
    void _DOMPointReadOnly_DOMPoint() throws Exception {
        test("DOMPointReadOnly", "DOMPoint");
    }

    @Alerts("true/false")
    void _DOMPointReadOnly_DOMPointReadOnly() throws Exception {
        test("DOMPointReadOnly", "DOMPointReadOnly");
    }

    @Alerts("true/false")
    void _DOMRect_DOMRect() throws Exception {
        test("DOMRect", "DOMRect");
    }

    @Alerts("true/false")
    void _DOMRectList_DOMRectList() throws Exception {
        test("DOMRectList", "DOMRectList");
    }

    @Alerts("true/true")
    void _DOMRectReadOnly_DOMRect() throws Exception {
        test("DOMRectReadOnly", "DOMRect");
    }

    @Alerts("true/false")
    void _DOMRectReadOnly_DOMRectReadOnly() throws Exception {
        test("DOMRectReadOnly", "DOMRectReadOnly");
    }

    @Alerts("false/false")
    void _DOMRequest_DOMRequest() throws Exception {
        test("DOMRequest", "DOMRequest");
    }

    @Alerts("false/false")
    void _DOMSettableTokenList_DOMSettableTokenList() throws Exception {
        test("DOMSettableTokenList", "DOMSettableTokenList");
    }

    @Alerts("true/false")
    void _DOMStringList_DOMStringList() throws Exception {
        test("DOMStringList", "DOMStringList");
    }

    @Alerts("true/false")
    @HtmlUnitNYI(CHROME = "false/false",
            EDGE = "false/false",
            FF = "false/false",
            FF_ESR = "false/false")
    void _DOMStringMap_DOMStringMap() throws Exception {
        test("DOMStringMap", "DOMStringMap");
    }

    @Alerts("false/false")
    void _DOMTokenList_DOMSettableTokenList() throws Exception {
        test("DOMTokenList", "DOMSettableTokenList");
    }

    @Alerts("true/false")
    void _DOMTokenList_DOMTokenList() throws Exception {
        test("DOMTokenList", "DOMTokenList");
    }

    @Alerts("true/false")
    void _DragEvent_DragEvent() throws Exception {
        test("DragEvent", "DragEvent");
    }

    @Alerts("true/false")
    void _DynamicsCompressorNode_DynamicsCompressorNode() throws Exception {
        test("DynamicsCompressorNode", "DynamicsCompressorNode");
    }

    @Alerts("true/false")
    void _Element_Audio() throws Exception {
        test("Element", "Audio");
    }

    @Alerts("true/false")
    void _Element_Element() throws Exception {
        test("Element", "Element");
    }

    @Alerts("true/false")
    void _Element_HTMLAnchorElement() throws Exception {
        test("Element", "HTMLAnchorElement");
    }

    @Alerts("false/false")
    void _Element_HTMLAppletElement() throws Exception {
        test("Element", "HTMLAppletElement");
    }

    @Alerts("true/false")
    void _Element_HTMLAreaElement() throws Exception {
        test("Element", "HTMLAreaElement");
    }

    @Alerts("true/false")
    void _Element_HTMLAudioElement() throws Exception {
        test("Element", "HTMLAudioElement");
    }

    @Alerts("true/false")
    void _Element_HTMLBaseElement() throws Exception {
        test("Element", "HTMLBaseElement");
    }

    @Alerts("false/false")
    void _Element_HTMLBaseFontElement() throws Exception {
        test("Element", "HTMLBaseFontElement");
    }

    @Alerts("false/false")
    void _Element_HTMLBGSoundElement() throws Exception {
        test("Element", "HTMLBGSoundElement");
    }

    @Alerts("false/false")
    void _Element_HTMLBlockElement() throws Exception {
        test("Element", "HTMLBlockElement");
    }

    @Alerts("true/false")
    void _Element_HTMLBodyElement() throws Exception {
        test("Element", "HTMLBodyElement");
    }

    @Alerts("true/false")
    void _Element_HTMLBRElement() throws Exception {
        test("Element", "HTMLBRElement");
    }

    @Alerts("true/false")
    void _Element_HTMLButtonElement() throws Exception {
        test("Element", "HTMLButtonElement");
    }

    @Alerts("true/false")
    void _Element_HTMLCanvasElement() throws Exception {
        test("Element", "HTMLCanvasElement");
    }

    @Alerts("true/false")
    void _Element_HTMLDataElement() throws Exception {
        test("Element", "HTMLDataElement");
    }

    @Alerts("true/false")
    void _Element_HTMLDataListElement() throws Exception {
        test("Element", "HTMLDataListElement");
    }

    @Alerts("false/false")
    void _Element_HTMLDDElement() throws Exception {
        test("Element", "HTMLDDElement");
    }

    @Alerts("true/false")
    void _Element_HTMLDetailsElement() throws Exception {
        test("Element", "HTMLDetailsElement");
    }

    @Alerts("true/false")
    void _Element_HTMLDialogElement() throws Exception {
        test("Element", "HTMLDialogElement");
    }

    @Alerts("true/false")
    void _Element_HTMLDirectoryElement() throws Exception {
        test("Element", "HTMLDirectoryElement");
    }

    @Alerts("true/false")
    void _Element_HTMLDivElement() throws Exception {
        test("Element", "HTMLDivElement");
    }

    @Alerts("true/false")
    void _Element_HTMLDListElement() throws Exception {
        test("Element", "HTMLDListElement");
    }

    @Alerts("false/false")
    void _Element_HTMLDTElement() throws Exception {
        test("Element", "HTMLDTElement");
    }

    @Alerts("true/true")
    void _Element_HTMLElement() throws Exception {
        test("Element", "HTMLElement");
    }

    @Alerts("true/false")
    void _Element_HTMLEmbedElement() throws Exception {
        test("Element", "HTMLEmbedElement");
    }

    @Alerts("true/false")
    void _Element_HTMLFieldSetElement() throws Exception {
        test("Element", "HTMLFieldSetElement");
    }

    @Alerts("true/false")
    void _Element_HTMLFontElement() throws Exception {
        test("Element", "HTMLFontElement");
    }

    @Alerts("true/false")
    void _Element_HTMLFormElement() throws Exception {
        test("Element", "HTMLFormElement");
    }

    @Alerts("true/false")
    void _Element_HTMLFrameElement() throws Exception {
        test("Element", "HTMLFrameElement");
    }

    @Alerts("true/false")
    void _Element_HTMLFrameSetElement() throws Exception {
        test("Element", "HTMLFrameSetElement");
    }

    @Alerts("true/false")
    void _Element_HTMLHeadElement() throws Exception {
        test("Element", "HTMLHeadElement");
    }

    @Alerts("true/false")
    void _Element_HTMLHeadingElement() throws Exception {
        test("Element", "HTMLHeadingElement");
    }

    @Alerts("true/false")
    void _Element_HTMLHRElement() throws Exception {
        test("Element", "HTMLHRElement");
    }

    @Alerts("true/false")
    void _Element_HTMLHtmlElement() throws Exception {
        test("Element", "HTMLHtmlElement");
    }

    @Alerts("true/false")
    void _Element_HTMLIFrameElement() throws Exception {
        test("Element", "HTMLIFrameElement");
    }

    @Alerts("true/false")
    void _Element_HTMLImageElement() throws Exception {
        test("Element", "HTMLImageElement");
    }

    @Alerts("true/false")
    void _Element_HTMLInputElement() throws Exception {
        test("Element", "HTMLInputElement");
    }

    @Alerts("false/false")
    void _Element_HTMLIsIndexElement() throws Exception {
        test("Element", "HTMLIsIndexElement");
    }

    @Alerts("true/false")
    void _Element_HTMLLabelElement() throws Exception {
        test("Element", "HTMLLabelElement");
    }

    @Alerts("true/false")
    void _Element_HTMLLegendElement() throws Exception {
        test("Element", "HTMLLegendElement");
    }

    @Alerts("true/false")
    void _Element_HTMLLIElement() throws Exception {
        test("Element", "HTMLLIElement");
    }

    @Alerts("true/false")
    void _Element_HTMLLinkElement() throws Exception {
        test("Element", "HTMLLinkElement");
    }

    @Alerts("true/false")
    void _Element_HTMLMapElement() throws Exception {
        test("Element", "HTMLMapElement");
    }

    @Alerts("true/false")
    void _Element_HTMLMarqueeElement() throws Exception {
        test("Element", "HTMLMarqueeElement");
    }

    @Alerts("true/false")
    void _Element_HTMLMediaElement() throws Exception {
        test("Element", "HTMLMediaElement");
    }

    @Alerts("true/false")
    void _Element_HTMLMenuElement() throws Exception {
        test("Element", "HTMLMenuElement");
    }

    @Alerts("false/false")
    void _Element_HTMLMenuItemElement() throws Exception {
        test("Element", "HTMLMenuItemElement");
    }

    @Alerts("true/false")
    void _Element_HTMLMetaElement() throws Exception {
        test("Element", "HTMLMetaElement");
    }

    @Alerts("true/false")
    void _Element_HTMLMeterElement() throws Exception {
        test("Element", "HTMLMeterElement");
    }

    @Alerts("true/false")
    void _Element_HTMLModElement() throws Exception {
        test("Element", "HTMLModElement");
    }

    @Alerts("false/false")
    void _Element_HTMLNextIdElement() throws Exception {
        test("Element", "HTMLNextIdElement");
    }

    @Alerts("true/false")
    void _Element_HTMLObjectElement() throws Exception {
        test("Element", "HTMLObjectElement");
    }

    @Alerts("true/false")
    void _Element_HTMLOListElement() throws Exception {
        test("Element", "HTMLOListElement");
    }

    @Alerts("true/false")
    void _Element_HTMLOptGroupElement() throws Exception {
        test("Element", "HTMLOptGroupElement");
    }

    @Alerts("true/false")
    void _Element_HTMLOptionElement() throws Exception {
        test("Element", "HTMLOptionElement");
    }

    @Alerts("true/false")
    void _Element_HTMLOutputElement() throws Exception {
        test("Element", "HTMLOutputElement");
    }

    @Alerts("true/false")
    void _Element_HTMLParagraphElement() throws Exception {
        test("Element", "HTMLParagraphElement");
    }

    @Alerts("true/false")
    void _Element_HTMLParamElement() throws Exception {
        test("Element", "HTMLParamElement");
    }

    @Alerts("false/false")
    void _Element_HTMLPhraseElement() throws Exception {
        test("Element", "HTMLPhraseElement");
    }

    @Alerts("true/false")
    void _Element_HTMLPictureElement() throws Exception {
        test("Element", "HTMLPictureElement");
    }

    @Alerts("true/false")
    void _Element_HTMLPreElement() throws Exception {
        test("Element", "HTMLPreElement");
    }

    @Alerts("true/false")
    void _Element_HTMLProgressElement() throws Exception {
        test("Element", "HTMLProgressElement");
    }

    @Alerts("true/false")
    void _Element_HTMLQuoteElement() throws Exception {
        test("Element", "HTMLQuoteElement");
    }

    @Alerts("true/false")
    void _Element_HTMLScriptElement() throws Exception {
        test("Element", "HTMLScriptElement");
    }

    @Alerts("true/false")
    void _Element_HTMLSelectElement() throws Exception {
        test("Element", "HTMLSelectElement");
    }

    @Alerts("true/false")
    void _Element_HTMLSlotElement() throws Exception {
        test("Element", "HTMLSlotElement");
    }

    @Alerts("true/false")
    void _Element_HTMLSourceElement() throws Exception {
        test("Element", "HTMLSourceElement");
    }

    @Alerts("true/false")
    void _Element_HTMLSpanElement() throws Exception {
        test("Element", "HTMLSpanElement");
    }

    @Alerts("true/false")
    void _Element_HTMLStyleElement() throws Exception {
        test("Element", "HTMLStyleElement");
    }

    @Alerts("true/false")
    void _Element_HTMLTableCaptionElement() throws Exception {
        test("Element", "HTMLTableCaptionElement");
    }

    @Alerts("true/false")
    void _Element_HTMLTableCellElement() throws Exception {
        test("Element", "HTMLTableCellElement");
    }

    @Alerts("true/false")
    void _Element_HTMLTableColElement() throws Exception {
        test("Element", "HTMLTableColElement");
    }

    @Alerts("false/false")
    void _Element_HTMLTableDataCellElement() throws Exception {
        test("Element", "HTMLTableDataCellElement");
    }

    @Alerts("true/false")
    void _Element_HTMLTableElement() throws Exception {
        test("Element", "HTMLTableElement");
    }

    @Alerts("false/false")
    void _Element_HTMLTableHeaderCellElement() throws Exception {
        test("Element", "HTMLTableHeaderCellElement");
    }

    @Alerts("true/false")
    void _Element_HTMLTableRowElement() throws Exception {
        test("Element", "HTMLTableRowElement");
    }

    @Alerts("true/false")
    void _Element_HTMLTableSectionElement() throws Exception {
        test("Element", "HTMLTableSectionElement");
    }

    @Alerts("true/false")
    void _Element_HTMLTemplateElement() throws Exception {
        test("Element", "HTMLTemplateElement");
    }

    @Alerts("true/false")
    void _Element_HTMLTextAreaElement() throws Exception {
        test("Element", "HTMLTextAreaElement");
    }

    @Alerts("true/false")
    void _Element_HTMLTimeElement() throws Exception {
        test("Element", "HTMLTimeElement");
    }

    @Alerts("true/false")
    void _Element_HTMLTitleElement() throws Exception {
        test("Element", "HTMLTitleElement");
    }

    @Alerts("true/false")
    void _Element_HTMLTrackElement() throws Exception {
        test("Element", "HTMLTrackElement");
    }

    @Alerts("true/false")
    void _Element_HTMLUListElement() throws Exception {
        test("Element", "HTMLUListElement");
    }

    @Alerts("true/false")
    void _Element_HTMLUnknownElement() throws Exception {
        test("Element", "HTMLUnknownElement");
    }

    @Alerts("true/false")
    void _Element_HTMLVideoElement() throws Exception {
        test("Element", "HTMLVideoElement");
    }

    @Alerts("true/false")
    void _Element_Image() throws Exception {
        test("Element", "Image");
    }

    @Alerts("true/false")
    void _Element_Option() throws Exception {
        test("Element", "Option");
    }

    @Alerts("true/false")
    void _Element_SVGAElement() throws Exception {
        test("Element", "SVGAElement");
    }

    @Alerts("true/false")
    void _Element_SVGAnimateElement() throws Exception {
        test("Element", "SVGAnimateElement");
    }

    @Alerts("true/false")
    void _Element_SVGAnimateMotionElement() throws Exception {
        test("Element", "SVGAnimateMotionElement");
    }

    @Alerts("true/false")
    void _Element_SVGAnimateTransformElement() throws Exception {
        test("Element", "SVGAnimateTransformElement");
    }

    @Alerts("true/false")
    void _Element_SVGAnimationElement() throws Exception {
        test("Element", "SVGAnimationElement");
    }

    @Alerts("true/false")
    void _Element_SVGCircleElement() throws Exception {
        test("Element", "SVGCircleElement");
    }

    @Alerts("true/false")
    void _Element_SVGClipPathElement() throws Exception {
        test("Element", "SVGClipPathElement");
    }

    @Alerts("true/false")
    void _Element_SVGComponentTransferFunctionElement() throws Exception {
        test("Element", "SVGComponentTransferFunctionElement");
    }

    @Alerts("true/false")
    void _Element_SVGDefsElement() throws Exception {
        test("Element", "SVGDefsElement");
    }

    @Alerts("true/false")
    void _Element_SVGDescElement() throws Exception {
        test("Element", "SVGDescElement");
    }

    @Alerts("false/false")
    void _Element_SVGDiscardElement() throws Exception {
        test("Element", "SVGDiscardElement");
    }

    @Alerts("true/true")
    void _Element_SVGElement() throws Exception {
        test("Element", "SVGElement");
    }

    @Alerts("true/false")
    void _Element_SVGEllipseElement() throws Exception {
        test("Element", "SVGEllipseElement");
    }

    @Alerts("true/false")
    void _Element_SVGFEBlendElement() throws Exception {
        test("Element", "SVGFEBlendElement");
    }

    @Alerts("true/false")
    void _Element_SVGFEColorMatrixElement() throws Exception {
        test("Element", "SVGFEColorMatrixElement");
    }

    @Alerts("true/false")
    void _Element_SVGFEComponentTransferElement() throws Exception {
        test("Element", "SVGFEComponentTransferElement");
    }

    @Alerts("true/false")
    void _Element_SVGFECompositeElement() throws Exception {
        test("Element", "SVGFECompositeElement");
    }

    @Alerts("true/false")
    void _Element_SVGFEConvolveMatrixElement() throws Exception {
        test("Element", "SVGFEConvolveMatrixElement");
    }

    @Alerts("true/false")
    void _Element_SVGFEDiffuseLightingElement() throws Exception {
        test("Element", "SVGFEDiffuseLightingElement");
    }

    @Alerts("true/false")
    void _Element_SVGFEDisplacementMapElement() throws Exception {
        test("Element", "SVGFEDisplacementMapElement");
    }

    @Alerts("true/false")
    void _Element_SVGFEDistantLightElement() throws Exception {
        test("Element", "SVGFEDistantLightElement");
    }

    @Alerts("true/false")
    void _Element_SVGFEDropShadowElement() throws Exception {
        test("Element", "SVGFEDropShadowElement");
    }

    @Alerts("true/false")
    void _Element_SVGFEFloodElement() throws Exception {
        test("Element", "SVGFEFloodElement");
    }

    @Alerts("true/false")
    void _Element_SVGFEFuncAElement() throws Exception {
        test("Element", "SVGFEFuncAElement");
    }

    @Alerts("true/false")
    void _Element_SVGFEFuncBElement() throws Exception {
        test("Element", "SVGFEFuncBElement");
    }

    @Alerts("true/false")
    void _Element_SVGFEFuncGElement() throws Exception {
        test("Element", "SVGFEFuncGElement");
    }

    @Alerts("true/false")
    void _Element_SVGFEFuncRElement() throws Exception {
        test("Element", "SVGFEFuncRElement");
    }

    @Alerts("true/false")
    void _Element_SVGFEGaussianBlurElement() throws Exception {
        test("Element", "SVGFEGaussianBlurElement");
    }

    @Alerts("true/false")
    void _Element_SVGFEImageElement() throws Exception {
        test("Element", "SVGFEImageElement");
    }

    @Alerts("true/false")
    void _Element_SVGFEMergeElement() throws Exception {
        test("Element", "SVGFEMergeElement");
    }

    @Alerts("true/false")
    void _Element_SVGFEMergeNodeElement() throws Exception {
        test("Element", "SVGFEMergeNodeElement");
    }

    @Alerts("true/false")
    void _Element_SVGFEMorphologyElement() throws Exception {
        test("Element", "SVGFEMorphologyElement");
    }

    @Alerts("true/false")
    void _Element_SVGFEOffsetElement() throws Exception {
        test("Element", "SVGFEOffsetElement");
    }

    @Alerts("true/false")
    void _Element_SVGFEPointLightElement() throws Exception {
        test("Element", "SVGFEPointLightElement");
    }

    @Alerts("true/false")
    void _Element_SVGFESpecularLightingElement() throws Exception {
        test("Element", "SVGFESpecularLightingElement");
    }

    @Alerts("true/false")
    void _Element_SVGFESpotLightElement() throws Exception {
        test("Element", "SVGFESpotLightElement");
    }

    @Alerts("true/false")
    void _Element_SVGFETileElement() throws Exception {
        test("Element", "SVGFETileElement");
    }

    @Alerts("true/false")
    void _Element_SVGFETurbulenceElement() throws Exception {
        test("Element", "SVGFETurbulenceElement");
    }

    @Alerts("true/false")
    void _Element_SVGFilterElement() throws Exception {
        test("Element", "SVGFilterElement");
    }

    @Alerts("true/false")
    void _Element_SVGForeignObjectElement() throws Exception {
        test("Element", "SVGForeignObjectElement");
    }

    @Alerts("true/false")
    void _Element_SVGGElement() throws Exception {
        test("Element", "SVGGElement");
    }

    @Alerts("true/false")
    void _Element_SVGGeometryElement() throws Exception {
        test("Element", "SVGGeometryElement");
    }

    @Alerts("true/false")
    void _Element_SVGGradientElement() throws Exception {
        test("Element", "SVGGradientElement");
    }

    @Alerts("true/false")
    void _Element_SVGGraphicsElement() throws Exception {
        test("Element", "SVGGraphicsElement");
    }

    @Alerts("true/false")
    void _Element_SVGImageElement() throws Exception {
        test("Element", "SVGImageElement");
    }

    @Alerts("true/false")
    void _Element_SVGLinearGradientElement() throws Exception {
        test("Element", "SVGLinearGradientElement");
    }

    @Alerts("true/false")
    void _Element_SVGLineElement() throws Exception {
        test("Element", "SVGLineElement");
    }

    @Alerts("true/false")
    void _Element_SVGMarkerElement() throws Exception {
        test("Element", "SVGMarkerElement");
    }

    @Alerts("true/false")
    void _Element_SVGMaskElement() throws Exception {
        test("Element", "SVGMaskElement");
    }

    @Alerts("true/false")
    void _Element_SVGMetadataElement() throws Exception {
        test("Element", "SVGMetadataElement");
    }

    @Alerts("true/false")
    void _Element_SVGMPathElement() throws Exception {
        test("Element", "SVGMPathElement");
    }

    @Alerts("true/false")
    void _Element_SVGPathElement() throws Exception {
        test("Element", "SVGPathElement");
    }

    @Alerts("true/false")
    void _Element_SVGPatternElement() throws Exception {
        test("Element", "SVGPatternElement");
    }

    @Alerts("true/false")
    void _Element_SVGPolygonElement() throws Exception {
        test("Element", "SVGPolygonElement");
    }

    @Alerts("true/false")
    void _Element_SVGPolylineElement() throws Exception {
        test("Element", "SVGPolylineElement");
    }

    @Alerts("true/false")
    void _Element_SVGRadialGradientElement() throws Exception {
        test("Element", "SVGRadialGradientElement");
    }

    @Alerts("true/false")
    void _Element_SVGRectElement() throws Exception {
        test("Element", "SVGRectElement");
    }

    @Alerts("true/false")
    void _Element_SVGScriptElement() throws Exception {
        test("Element", "SVGScriptElement");
    }

    @Alerts("true/false")
    void _Element_SVGSetElement() throws Exception {
        test("Element", "SVGSetElement");
    }

    @Alerts("true/false")
    void _Element_SVGStopElement() throws Exception {
        test("Element", "SVGStopElement");
    }

    @Alerts("true/false")
    void _Element_SVGStyleElement() throws Exception {
        test("Element", "SVGStyleElement");
    }

    @Alerts("true/false")
    void _Element_SVGSVGElement() throws Exception {
        test("Element", "SVGSVGElement");
    }

    @Alerts("true/false")
    void _Element_SVGSwitchElement() throws Exception {
        test("Element", "SVGSwitchElement");
    }

    @Alerts("true/false")
    void _Element_SVGSymbolElement() throws Exception {
        test("Element", "SVGSymbolElement");
    }

    @Alerts("true/false")
    void _Element_SVGTextContentElement() throws Exception {
        test("Element", "SVGTextContentElement");
    }

    @Alerts("true/false")
    void _Element_SVGTextElement() throws Exception {
        test("Element", "SVGTextElement");
    }

    @Alerts("true/false")
    void _Element_SVGTextPathElement() throws Exception {
        test("Element", "SVGTextPathElement");
    }

    @Alerts("true/false")
    void _Element_SVGTextPositioningElement() throws Exception {
        test("Element", "SVGTextPositioningElement");
    }

    @Alerts("true/false")
    void _Element_SVGTitleElement() throws Exception {
        test("Element", "SVGTitleElement");
    }

    @Alerts("true/false")
    void _Element_SVGTSpanElement() throws Exception {
        test("Element", "SVGTSpanElement");
    }

    @Alerts("true/false")
    void _Element_SVGUseElement() throws Exception {
        test("Element", "SVGUseElement");
    }

    @Alerts("true/false")
    void _Element_SVGViewElement() throws Exception {
        test("Element", "SVGViewElement");
    }

    @Alerts("false/false")
    void _Enumerator_Enumerator() throws Exception {
        test("Enumerator", "Enumerator");
    }

    @Alerts("true/false")
    @HtmlUnitNYI(CHROME = "false/false",
            EDGE = "false/false",
            FF = "false/false",
            FF_ESR = "false/false")
    void _Error_DOMException() throws Exception {
        test("Error", "DOMException");
    }

    @Alerts("true/false")
    void _Error_Error() throws Exception {
        test("Error", "Error");
    }

    @Alerts("true/false")
    void _ErrorEvent_ErrorEvent() throws Exception {
        test("ErrorEvent", "ErrorEvent");
    }

    @Alerts("true/true")
    void _Event_AnimationEvent() throws Exception {
        test("Event", "AnimationEvent");
    }

    @Alerts("false/false")
    void _Event_ApplicationCacheErrorEvent() throws Exception {
        test("Event", "ApplicationCacheErrorEvent");
    }

    @Alerts("true/true")
    void _Event_AudioProcessingEvent() throws Exception {
        test("Event", "AudioProcessingEvent");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/true",
            EDGE = "true/true")
    void _Event_BeforeInstallPromptEvent() throws Exception {
        test("Event", "BeforeInstallPromptEvent");
    }

    @Alerts("true/true")
    void _Event_BeforeUnloadEvent() throws Exception {
        test("Event", "BeforeUnloadEvent");
    }

    @Alerts("true/true")
    void _Event_BlobEvent() throws Exception {
        test("Event", "BlobEvent");
    }

    @Alerts("true/true")
    void _Event_ClipboardEvent() throws Exception {
        test("Event", "ClipboardEvent");
    }

    @Alerts("true/true")
    void _Event_CloseEvent() throws Exception {
        test("Event", "CloseEvent");
    }

    @Alerts("true/false")
    void _Event_CompositionEvent() throws Exception {
        test("Event", "CompositionEvent");
    }

    @Alerts("true/true")
    void _Event_CustomEvent() throws Exception {
        test("Event", "CustomEvent");
    }

    @Alerts("false/false")
    void _Event_DeviceLightEvent() throws Exception {
        test("Event", "DeviceLightEvent");
    }

    @Alerts("true/true")
    void _Event_DeviceMotionEvent() throws Exception {
        test("Event", "DeviceMotionEvent");
    }

    @Alerts("true/true")
    void _Event_DeviceOrientationEvent() throws Exception {
        test("Event", "DeviceOrientationEvent");
    }

    @Alerts("false/false")
    void _Event_DeviceProximityEvent() throws Exception {
        test("Event", "DeviceProximityEvent");
    }

    @Alerts("true/false")
    void _Event_DragEvent() throws Exception {
        test("Event", "DragEvent");
    }

    @Alerts("true/true")
    void _Event_ErrorEvent() throws Exception {
        test("Event", "ErrorEvent");
    }

    @Alerts("true/false")
    void _Event_Event() throws Exception {
        test("Event", "Event");
    }

    @Alerts("true/false")
    void _Event_FocusEvent() throws Exception {
        test("Event", "FocusEvent");
    }

    @Alerts("true/true")
    void _Event_GamepadEvent() throws Exception {
        test("Event", "GamepadEvent");
    }

    @Alerts("true/true")
    void _Event_HashChangeEvent() throws Exception {
        test("Event", "HashChangeEvent");
    }

    @Alerts("true/true")
    void _Event_IDBVersionChangeEvent() throws Exception {
        test("Event", "IDBVersionChangeEvent");
    }

    @Alerts("true/false")
    void _Event_InputEvent() throws Exception {
        test("Event", "InputEvent");
    }

    @Alerts("true/false")
    void _Event_KeyboardEvent() throws Exception {
        test("Event", "KeyboardEvent");
    }

    @Alerts("true/true")
    void _Event_MediaEncryptedEvent() throws Exception {
        test("Event", "MediaEncryptedEvent");
    }

    @Alerts(DEFAULT = "false/false",
            FF = "true/true",
            FF_ESR = "true/true")
    void _Event_MediaKeyError() throws Exception {
        test("Event", "MediaKeyError");
    }

    @Alerts("true/true")
    void _Event_MediaKeyMessageEvent() throws Exception {
        test("Event", "MediaKeyMessageEvent");
    }

    @Alerts("true/true")
    void _Event_MediaQueryListEvent() throws Exception {
        test("Event", "MediaQueryListEvent");
    }

    @Alerts("true/true")
    void _Event_MediaStreamEvent() throws Exception {
        test("Event", "MediaStreamEvent");
    }

    @Alerts("true/true")
    void _Event_MediaStreamTrackEvent() throws Exception {
        test("Event", "MediaStreamTrackEvent");
    }

    @Alerts("true/true")
    void _Event_MessageEvent() throws Exception {
        test("Event", "MessageEvent");
    }

    @Alerts("true/true")
    void _Event_MIDIConnectionEvent() throws Exception {
        test("Event", "MIDIConnectionEvent");
    }

    @Alerts("true/true")
    void _Event_MIDIMessageEvent() throws Exception {
        test("Event", "MIDIMessageEvent");
    }

    @Alerts("true/false")
    void _Event_MouseEvent() throws Exception {
        test("Event", "MouseEvent");
    }

    @Alerts(DEFAULT = "false/false",
            FF = "true/false",
            FF_ESR = "true/false")
    void _Event_MouseScrollEvent() throws Exception {
        test("Event", "MouseScrollEvent");
    }

    @Alerts("false/false")
    void _Event_MouseWheelEvent() throws Exception {
        test("Event", "MouseWheelEvent");
    }

    @Alerts("false/false")
    void _Event_MSGestureEvent() throws Exception {
        test("Event", "MSGestureEvent");
    }

    @Alerts("false/false")
    void _Event_MutationEvent() throws Exception {
        test("Event", "MutationEvent");
    }

    @Alerts("true/true")
    void _Event_OfflineAudioCompletionEvent() throws Exception {
        test("Event", "OfflineAudioCompletionEvent");
    }

    @Alerts("true/true")
    void _Event_PageTransitionEvent() throws Exception {
        test("Event", "PageTransitionEvent");
    }

    @Alerts("true/false")
    void _Event_PointerEvent() throws Exception {
        test("Event", "PointerEvent");
    }

    @Alerts("true/true")
    void _Event_PopStateEvent() throws Exception {
        test("Event", "PopStateEvent");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/true",
            EDGE = "true/true")
    void _Event_PresentationConnectionAvailableEvent() throws Exception {
        test("Event", "PresentationConnectionAvailableEvent");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/true",
            EDGE = "true/true")
    void _Event_PresentationConnectionCloseEvent() throws Exception {
        test("Event", "PresentationConnectionCloseEvent");
    }

    @Alerts("true/true")
    void _Event_ProgressEvent() throws Exception {
        test("Event", "ProgressEvent");
    }

    @Alerts("true/true")
    void _Event_PromiseRejectionEvent() throws Exception {
        test("Event", "PromiseRejectionEvent");
    }

    @Alerts("true/true")
    void _Event_RTCDataChannelEvent() throws Exception {
        test("Event", "RTCDataChannelEvent");
    }

    @Alerts("true/true")
    void _Event_RTCPeerConnectionIceEvent() throws Exception {
        test("Event", "RTCPeerConnectionIceEvent");
    }

    @Alerts("true/true")
    void _Event_SecurityPolicyViolationEvent() throws Exception {
        test("Event", "SecurityPolicyViolationEvent");
    }

    @Alerts("true/true")
    void _Event_SpeechSynthesisEvent() throws Exception {
        test("Event", "SpeechSynthesisEvent");
    }

    @Alerts("true/true")
    void _Event_StorageEvent() throws Exception {
        test("Event", "StorageEvent");
    }

    @Alerts("true/true")
    void _Event_SubmitEvent() throws Exception {
        test("Event", "SubmitEvent");
    }

    @Alerts("false/false")
    void _Event_SVGZoomEvent() throws Exception {
        test("Event", "SVGZoomEvent");
    }

    @Alerts("true/false")
    void _Event_TextEvent() throws Exception {
        test("Event", "TextEvent");
    }

    @Alerts(DEFAULT = "false/false",
            FF = "true/true",
            FF_ESR = "true/true")
    void _Event_TimeEvent() throws Exception {
        test("Event", "TimeEvent");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _Event_TouchEvent() throws Exception {
        test("Event", "TouchEvent");
    }

    @Alerts("true/true")
    void _Event_TrackEvent() throws Exception {
        test("Event", "TrackEvent");
    }

    @Alerts("true/true")
    void _Event_TransitionEvent() throws Exception {
        test("Event", "TransitionEvent");
    }

    @Alerts("true/true")
    void _Event_UIEvent() throws Exception {
        test("Event", "UIEvent");
    }

    @Alerts("false/false")
    void _Event_UserProximityEvent() throws Exception {
        test("Event", "UserProximityEvent");
    }

    @Alerts("true/true")
    void _Event_WebGLContextEvent() throws Exception {
        test("Event", "WebGLContextEvent");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/true",
            EDGE = "true/true")
    void _Event_webkitSpeechRecognitionError() throws Exception {
        test("Event", "webkitSpeechRecognitionError");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/true",
            EDGE = "true/true")
    void _Event_webkitSpeechRecognitionEvent() throws Exception {
        test("Event", "webkitSpeechRecognitionEvent");
    }

    @Alerts("true/false")
    void _Event_WheelEvent() throws Exception {
        test("Event", "WheelEvent");
    }

    @Alerts("true/false")
    void _EventSource_EventSource() throws Exception {
        test("EventSource", "EventSource");
    }

    @Alerts("true/true")
    void _EventTarget_AbortSignal() throws Exception {
        test("EventTarget", "AbortSignal");
    }

    @Alerts("true/false")
    void _EventTarget_AnalyserNode() throws Exception {
        test("EventTarget", "AnalyserNode");
    }

    @Alerts("true/true")
    void _EventTarget_Animation() throws Exception {
        test("EventTarget", "Animation");
    }

    @Alerts("false/false")
    void _EventTarget_ApplicationCache() throws Exception {
        test("EventTarget", "ApplicationCache");
    }

    @Alerts("true/false")
    void _EventTarget_Attr() throws Exception {
        test("EventTarget", "Attr");
    }

    @Alerts("true/false")
    void _EventTarget_Audio() throws Exception {
        test("EventTarget", "Audio");
    }

    @Alerts("true/false")
    void _EventTarget_AudioBufferSourceNode() throws Exception {
        test("EventTarget", "AudioBufferSourceNode");
    }

    @Alerts("true/false")
    void _EventTarget_AudioContext() throws Exception {
        test("EventTarget", "AudioContext");
    }

    @Alerts("true/false")
    void _EventTarget_AudioDestinationNode() throws Exception {
        test("EventTarget", "AudioDestinationNode");
    }

    @Alerts("true/true")
    void _EventTarget_AudioNode() throws Exception {
        test("EventTarget", "AudioNode");
    }

    @Alerts("true/false")
    void _EventTarget_AudioScheduledSourceNode() throws Exception {
        test("EventTarget", "AudioScheduledSourceNode");
    }

    @Alerts("true/true")
    void _EventTarget_BaseAudioContext() throws Exception {
        test("EventTarget", "BaseAudioContext");
    }

    @Alerts(DEFAULT = "true/true",
            FF = "false/false",
            FF_ESR = "false/false")
    void _EventTarget_BatteryManager() throws Exception {
        test("EventTarget", "BatteryManager");
    }

    @Alerts("true/false")
    void _EventTarget_BiquadFilterNode() throws Exception {
        test("EventTarget", "BiquadFilterNode");
    }

    @Alerts("true/true")
    void _EventTarget_BroadcastChannel() throws Exception {
        test("EventTarget", "BroadcastChannel");
    }

    @Alerts(DEFAULT = "false/false",
            FF = "true/false",
            FF_ESR = "true/false")
    void _EventTarget_CanvasCaptureMediaStream() throws Exception {
        test("EventTarget", "CanvasCaptureMediaStream");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _EventTarget_CanvasCaptureMediaStreamTrack() throws Exception {
        test("EventTarget", "CanvasCaptureMediaStreamTrack");
    }

    @Alerts("true/false")
    void _EventTarget_CDATASection() throws Exception {
        test("EventTarget", "CDATASection");
    }

    @Alerts("true/false")
    void _EventTarget_ChannelMergerNode() throws Exception {
        test("EventTarget", "ChannelMergerNode");
    }

    @Alerts("true/false")
    void _EventTarget_ChannelSplitterNode() throws Exception {
        test("EventTarget", "ChannelSplitterNode");
    }

    @Alerts("true/false")
    void _EventTarget_CharacterData() throws Exception {
        test("EventTarget", "CharacterData");
    }

    @Alerts("true/false")
    void _EventTarget_Comment() throws Exception {
        test("EventTarget", "Comment");
    }

    @Alerts("true/false")
    void _EventTarget_ConstantSourceNode() throws Exception {
        test("EventTarget", "ConstantSourceNode");
    }

    @Alerts("true/false")
    void _EventTarget_ConvolverNode() throws Exception {
        test("EventTarget", "ConvolverNode");
    }

    @Alerts("true/false")
    void _EventTarget_DelayNode() throws Exception {
        test("EventTarget", "DelayNode");
    }

    @Alerts("true/false")
    void _EventTarget_Document() throws Exception {
        test("EventTarget", "Document");
    }

    @Alerts("true/false")
    void _EventTarget_DocumentFragment() throws Exception {
        test("EventTarget", "DocumentFragment");
    }

    @Alerts("true/false")
    void _EventTarget_DocumentType() throws Exception {
        test("EventTarget", "DocumentType");
    }

    @Alerts("false/false")
    void _EventTarget_DOMCursor() throws Exception {
        test("EventTarget", "DOMCursor");
    }

    @Alerts("false/false")
    void _EventTarget_DOMRequest() throws Exception {
        test("EventTarget", "DOMRequest");
    }

    @Alerts("true/false")
    void _EventTarget_DynamicsCompressorNode() throws Exception {
        test("EventTarget", "DynamicsCompressorNode");
    }

    @Alerts("true/false")
    void _EventTarget_Element() throws Exception {
        test("EventTarget", "Element");
    }

    @Alerts("true/true")
    void _EventTarget_EventSource() throws Exception {
        test("EventTarget", "EventSource");
    }

    @Alerts("true/false")
    void _EventTarget_EventTarget() throws Exception {
        test("EventTarget", "EventTarget");
    }

    @Alerts("true/true")
    void _EventTarget_FileReader() throws Exception {
        test("EventTarget", "FileReader");
    }

    @Alerts(DEFAULT = "false/false",
            FF = "true/true",
            FF_ESR = "true/true")
    void _EventTarget_FontFaceSet() throws Exception {
        test("EventTarget", "FontFaceSet");
    }

    @Alerts("true/false")
    void _EventTarget_GainNode() throws Exception {
        test("EventTarget", "GainNode");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLAnchorElement() throws Exception {
        test("EventTarget", "HTMLAnchorElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLAreaElement() throws Exception {
        test("EventTarget", "HTMLAreaElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLAudioElement() throws Exception {
        test("EventTarget", "HTMLAudioElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLBaseElement() throws Exception {
        test("EventTarget", "HTMLBaseElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLBodyElement() throws Exception {
        test("EventTarget", "HTMLBodyElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLBRElement() throws Exception {
        test("EventTarget", "HTMLBRElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLButtonElement() throws Exception {
        test("EventTarget", "HTMLButtonElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLCanvasElement() throws Exception {
        test("EventTarget", "HTMLCanvasElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLDataElement() throws Exception {
        test("EventTarget", "HTMLDataElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLDataListElement() throws Exception {
        test("EventTarget", "HTMLDataListElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLDetailsElement() throws Exception {
        test("EventTarget", "HTMLDetailsElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLDialogElement() throws Exception {
        test("EventTarget", "HTMLDialogElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLDirectoryElement() throws Exception {
        test("EventTarget", "HTMLDirectoryElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLDivElement() throws Exception {
        test("EventTarget", "HTMLDivElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLDListElement() throws Exception {
        test("EventTarget", "HTMLDListElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLDocument() throws Exception {
        test("EventTarget", "HTMLDocument");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLElement() throws Exception {
        test("EventTarget", "HTMLElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLEmbedElement() throws Exception {
        test("EventTarget", "HTMLEmbedElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLFieldSetElement() throws Exception {
        test("EventTarget", "HTMLFieldSetElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLFontElement() throws Exception {
        test("EventTarget", "HTMLFontElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLFormElement() throws Exception {
        test("EventTarget", "HTMLFormElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLFrameElement() throws Exception {
        test("EventTarget", "HTMLFrameElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLFrameSetElement() throws Exception {
        test("EventTarget", "HTMLFrameSetElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLHeadElement() throws Exception {
        test("EventTarget", "HTMLHeadElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLHeadingElement() throws Exception {
        test("EventTarget", "HTMLHeadingElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLHRElement() throws Exception {
        test("EventTarget", "HTMLHRElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLHtmlElement() throws Exception {
        test("EventTarget", "HTMLHtmlElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLIFrameElement() throws Exception {
        test("EventTarget", "HTMLIFrameElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLImageElement() throws Exception {
        test("EventTarget", "HTMLImageElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLInputElement() throws Exception {
        test("EventTarget", "HTMLInputElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLLabelElement() throws Exception {
        test("EventTarget", "HTMLLabelElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLLegendElement() throws Exception {
        test("EventTarget", "HTMLLegendElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLLIElement() throws Exception {
        test("EventTarget", "HTMLLIElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLLinkElement() throws Exception {
        test("EventTarget", "HTMLLinkElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLMapElement() throws Exception {
        test("EventTarget", "HTMLMapElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLMarqueeElement() throws Exception {
        test("EventTarget", "HTMLMarqueeElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLMediaElement() throws Exception {
        test("EventTarget", "HTMLMediaElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLMenuElement() throws Exception {
        test("EventTarget", "HTMLMenuElement");
    }

    @Alerts("false/false")
    void _EventTarget_HTMLMenuItemElement() throws Exception {
        test("EventTarget", "HTMLMenuItemElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLMetaElement() throws Exception {
        test("EventTarget", "HTMLMetaElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLMeterElement() throws Exception {
        test("EventTarget", "HTMLMeterElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLModElement() throws Exception {
        test("EventTarget", "HTMLModElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLObjectElement() throws Exception {
        test("EventTarget", "HTMLObjectElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLOListElement() throws Exception {
        test("EventTarget", "HTMLOListElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLOptGroupElement() throws Exception {
        test("EventTarget", "HTMLOptGroupElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLOptionElement() throws Exception {
        test("EventTarget", "HTMLOptionElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLOutputElement() throws Exception {
        test("EventTarget", "HTMLOutputElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLParagraphElement() throws Exception {
        test("EventTarget", "HTMLParagraphElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLParamElement() throws Exception {
        test("EventTarget", "HTMLParamElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLPictureElement() throws Exception {
        test("EventTarget", "HTMLPictureElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLPreElement() throws Exception {
        test("EventTarget", "HTMLPreElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLProgressElement() throws Exception {
        test("EventTarget", "HTMLProgressElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLQuoteElement() throws Exception {
        test("EventTarget", "HTMLQuoteElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLScriptElement() throws Exception {
        test("EventTarget", "HTMLScriptElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLSelectElement() throws Exception {
        test("EventTarget", "HTMLSelectElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLSlotElement() throws Exception {
        test("EventTarget", "HTMLSlotElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLSourceElement() throws Exception {
        test("EventTarget", "HTMLSourceElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLSpanElement() throws Exception {
        test("EventTarget", "HTMLSpanElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLStyleElement() throws Exception {
        test("EventTarget", "HTMLStyleElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLTableCaptionElement() throws Exception {
        test("EventTarget", "HTMLTableCaptionElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLTableCellElement() throws Exception {
        test("EventTarget", "HTMLTableCellElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLTableColElement() throws Exception {
        test("EventTarget", "HTMLTableColElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLTableElement() throws Exception {
        test("EventTarget", "HTMLTableElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLTableRowElement() throws Exception {
        test("EventTarget", "HTMLTableRowElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLTableSectionElement() throws Exception {
        test("EventTarget", "HTMLTableSectionElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLTemplateElement() throws Exception {
        test("EventTarget", "HTMLTemplateElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLTextAreaElement() throws Exception {
        test("EventTarget", "HTMLTextAreaElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLTimeElement() throws Exception {
        test("EventTarget", "HTMLTimeElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLTitleElement() throws Exception {
        test("EventTarget", "HTMLTitleElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLTrackElement() throws Exception {
        test("EventTarget", "HTMLTrackElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLUListElement() throws Exception {
        test("EventTarget", "HTMLUListElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLUnknownElement() throws Exception {
        test("EventTarget", "HTMLUnknownElement");
    }

    @Alerts("true/false")
    void _EventTarget_HTMLVideoElement() throws Exception {
        test("EventTarget", "HTMLVideoElement");
    }

    @Alerts("true/true")
    void _EventTarget_IDBDatabase() throws Exception {
        test("EventTarget", "IDBDatabase");
    }

    @Alerts("false/false")
    void _EventTarget_IDBMutableFile() throws Exception {
        test("EventTarget", "IDBMutableFile");
    }

    @Alerts("true/false")
    void _EventTarget_IDBOpenDBRequest() throws Exception {
        test("EventTarget", "IDBOpenDBRequest");
    }

    @Alerts("true/true")
    void _EventTarget_IDBRequest() throws Exception {
        test("EventTarget", "IDBRequest");
    }

    @Alerts("true/true")
    void _EventTarget_IDBTransaction() throws Exception {
        test("EventTarget", "IDBTransaction");
    }

    @Alerts("true/false")
    void _EventTarget_IIRFilterNode() throws Exception {
        test("EventTarget", "IIRFilterNode");
    }

    @Alerts("true/false")
    void _EventTarget_Image() throws Exception {
        test("EventTarget", "Image");
    }

    @Alerts("false/false")
    void _EventTarget_LocalMediaStream() throws Exception {
        test("EventTarget", "LocalMediaStream");
    }

    @Alerts("true/true")
    void _EventTarget_MediaDevices() throws Exception {
        test("EventTarget", "MediaDevices");
    }

    @Alerts("true/false")
    void _EventTarget_MediaElementAudioSourceNode() throws Exception {
        test("EventTarget", "MediaElementAudioSourceNode");
    }

    @Alerts("true/true")
    void _EventTarget_MediaKeySession() throws Exception {
        test("EventTarget", "MediaKeySession");
    }

    @Alerts("true/true")
    void _EventTarget_MediaQueryList() throws Exception {
        test("EventTarget", "MediaQueryList");
    }

    @Alerts("true/true")
    void _EventTarget_MediaRecorder() throws Exception {
        test("EventTarget", "MediaRecorder");
    }

    @Alerts("true/true")
    void _EventTarget_MediaSource() throws Exception {
        test("EventTarget", "MediaSource");
    }

    @Alerts("true/true")
    void _EventTarget_MediaStream() throws Exception {
        test("EventTarget", "MediaStream");
    }

    @Alerts("true/false")
    void _EventTarget_MediaStreamAudioDestinationNode() throws Exception {
        test("EventTarget", "MediaStreamAudioDestinationNode");
    }

    @Alerts("true/false")
    void _EventTarget_MediaStreamAudioSourceNode() throws Exception {
        test("EventTarget", "MediaStreamAudioSourceNode");
    }

    @Alerts("true/true")
    void _EventTarget_MediaStreamTrack() throws Exception {
        test("EventTarget", "MediaStreamTrack");
    }

    @Alerts("true/true")
    void _EventTarget_MessagePort() throws Exception {
        test("EventTarget", "MessagePort");
    }

    @Alerts("true/true")
    void _EventTarget_MIDIAccess() throws Exception {
        test("EventTarget", "MIDIAccess");
    }

    @Alerts("true/false")
    void _EventTarget_MIDIInput() throws Exception {
        test("EventTarget", "MIDIInput");
    }

    @Alerts("true/false")
    void _EventTarget_MIDIOutput() throws Exception {
        test("EventTarget", "MIDIOutput");
    }

    @Alerts("true/true")
    void _EventTarget_MIDIPort() throws Exception {
        test("EventTarget", "MIDIPort");
    }

    @Alerts("false/false")
    void _EventTarget_mozRTCPeerConnection() throws Exception {
        test("EventTarget", "mozRTCPeerConnection");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/true",
            EDGE = "true/true")
    void _EventTarget_NetworkInformation() throws Exception {
        test("EventTarget", "NetworkInformation");
    }

    @Alerts("true/true")
    void _EventTarget_Node() throws Exception {
        test("EventTarget", "Node");
    }

    @Alerts("true/true")
    void _EventTarget_Notification() throws Exception {
        test("EventTarget", "Notification");
    }

    @Alerts("true/false")
    void _EventTarget_OfflineAudioContext() throws Exception {
        test("EventTarget", "OfflineAudioContext");
    }

    @Alerts("false/false")
    void _EventTarget_OfflineResourceList() throws Exception {
        test("EventTarget", "OfflineResourceList");
    }

    @Alerts("true/false")
    void _EventTarget_Option() throws Exception {
        test("EventTarget", "Option");
    }

    @Alerts("true/false")
    void _EventTarget_OscillatorNode() throws Exception {
        test("EventTarget", "OscillatorNode");
    }

    @Alerts("true/false")
    void _EventTarget_PannerNode() throws Exception {
        test("EventTarget", "PannerNode");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/true",
            EDGE = "true/true")
    void _EventTarget_PaymentRequest() throws Exception {
        test("EventTarget", "PaymentRequest");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/true",
            EDGE = "true/true")
    void _EventTarget_PaymentResponse() throws Exception {
        test("EventTarget", "PaymentResponse");
    }

    @Alerts("true/true")
    void _EventTarget_Performance() throws Exception {
        test("EventTarget", "Performance");
    }

    @Alerts("true/true")
    void _EventTarget_PermissionStatus() throws Exception {
        test("EventTarget", "PermissionStatus");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/true",
            EDGE = "true/true")
    void _EventTarget_PresentationAvailability() throws Exception {
        test("EventTarget", "PresentationAvailability");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/true",
            EDGE = "true/true")
    void _EventTarget_PresentationConnection() throws Exception {
        test("EventTarget", "PresentationConnection");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/true",
            EDGE = "true/true")
    void _EventTarget_PresentationRequest() throws Exception {
        test("EventTarget", "PresentationRequest");
    }

    @Alerts("true/false")
    void _EventTarget_ProcessingInstruction() throws Exception {
        test("EventTarget", "ProcessingInstruction");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/true",
            EDGE = "true/true")
    void _EventTarget_RemotePlayback() throws Exception {
        test("EventTarget", "RemotePlayback");
    }

    @Alerts("true/true")
    void _EventTarget_RTCPeerConnection() throws Exception {
        test("EventTarget", "RTCPeerConnection");
    }

    @Alerts("true/true")
    void _EventTarget_RTCSctpTransport() throws Exception {
        test("EventTarget", "RTCSctpTransport");
    }

    @Alerts("true/true")
    void _EventTarget_Screen() throws Exception {
        test("EventTarget", "Screen");
    }

    @Alerts("true/true")
    void _EventTarget_ScreenOrientation() throws Exception {
        test("EventTarget", "ScreenOrientation");
    }

    @Alerts("true/false")
    void _EventTarget_ScriptProcessorNode() throws Exception {
        test("EventTarget", "ScriptProcessorNode");
    }

    @Alerts("true/true")
    void _EventTarget_ServiceWorker() throws Exception {
        test("EventTarget", "ServiceWorker");
    }

    @Alerts("true/true")
    void _EventTarget_ServiceWorkerContainer() throws Exception {
        test("EventTarget", "ServiceWorkerContainer");
    }

    @Alerts("true/true")
    void _EventTarget_ServiceWorkerRegistration() throws Exception {
        test("EventTarget", "ServiceWorkerRegistration");
    }

    @Alerts("true/false")
    void _EventTarget_ShadowRoot() throws Exception {
        test("EventTarget", "ShadowRoot");
    }

    @Alerts("true/true")
    void _EventTarget_SharedWorker() throws Exception {
        test("EventTarget", "SharedWorker");
    }

    @Alerts("true/true")
    void _EventTarget_SourceBuffer() throws Exception {
        test("EventTarget", "SourceBuffer");
    }

    @Alerts("true/true")
    void _EventTarget_SourceBufferList() throws Exception {
        test("EventTarget", "SourceBufferList");
    }

    @Alerts("true/true")
    void _EventTarget_SpeechSynthesis() throws Exception {
        test("EventTarget", "SpeechSynthesis");
    }

    @Alerts("true/true")
    void _EventTarget_SpeechSynthesisUtterance() throws Exception {
        test("EventTarget", "SpeechSynthesisUtterance");
    }

    @Alerts("true/false")
    void _EventTarget_StereoPannerNode() throws Exception {
        test("EventTarget", "StereoPannerNode");
    }

    @Alerts("false/false")
    void _EventTarget_StorageManager() throws Exception {
        test("EventTarget", "StorageManager");
    }

    @Alerts("true/false")
    void _EventTarget_SVGAElement() throws Exception {
        test("EventTarget", "SVGAElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGAnimateElement() throws Exception {
        test("EventTarget", "SVGAnimateElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGAnimateMotionElement() throws Exception {
        test("EventTarget", "SVGAnimateMotionElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGAnimateTransformElement() throws Exception {
        test("EventTarget", "SVGAnimateTransformElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGAnimationElement() throws Exception {
        test("EventTarget", "SVGAnimationElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGCircleElement() throws Exception {
        test("EventTarget", "SVGCircleElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGClipPathElement() throws Exception {
        test("EventTarget", "SVGClipPathElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGComponentTransferFunctionElement() throws Exception {
        test("EventTarget", "SVGComponentTransferFunctionElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGDefsElement() throws Exception {
        test("EventTarget", "SVGDefsElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGDescElement() throws Exception {
        test("EventTarget", "SVGDescElement");
    }

    @Alerts("false/false")
    void _EventTarget_SVGDiscardElement() throws Exception {
        test("EventTarget", "SVGDiscardElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGElement() throws Exception {
        test("EventTarget", "SVGElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGEllipseElement() throws Exception {
        test("EventTarget", "SVGEllipseElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFEBlendElement() throws Exception {
        test("EventTarget", "SVGFEBlendElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFEColorMatrixElement() throws Exception {
        test("EventTarget", "SVGFEColorMatrixElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFEComponentTransferElement() throws Exception {
        test("EventTarget", "SVGFEComponentTransferElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFECompositeElement() throws Exception {
        test("EventTarget", "SVGFECompositeElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFEConvolveMatrixElement() throws Exception {
        test("EventTarget", "SVGFEConvolveMatrixElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFEDiffuseLightingElement() throws Exception {
        test("EventTarget", "SVGFEDiffuseLightingElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFEDisplacementMapElement() throws Exception {
        test("EventTarget", "SVGFEDisplacementMapElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFEDistantLightElement() throws Exception {
        test("EventTarget", "SVGFEDistantLightElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFEDropShadowElement() throws Exception {
        test("EventTarget", "SVGFEDropShadowElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFEFloodElement() throws Exception {
        test("EventTarget", "SVGFEFloodElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFEFuncAElement() throws Exception {
        test("EventTarget", "SVGFEFuncAElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFEFuncBElement() throws Exception {
        test("EventTarget", "SVGFEFuncBElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFEFuncGElement() throws Exception {
        test("EventTarget", "SVGFEFuncGElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFEFuncRElement() throws Exception {
        test("EventTarget", "SVGFEFuncRElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFEGaussianBlurElement() throws Exception {
        test("EventTarget", "SVGFEGaussianBlurElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFEImageElement() throws Exception {
        test("EventTarget", "SVGFEImageElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFEMergeElement() throws Exception {
        test("EventTarget", "SVGFEMergeElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFEMergeNodeElement() throws Exception {
        test("EventTarget", "SVGFEMergeNodeElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFEMorphologyElement() throws Exception {
        test("EventTarget", "SVGFEMorphologyElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFEOffsetElement() throws Exception {
        test("EventTarget", "SVGFEOffsetElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFEPointLightElement() throws Exception {
        test("EventTarget", "SVGFEPointLightElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFESpecularLightingElement() throws Exception {
        test("EventTarget", "SVGFESpecularLightingElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFESpotLightElement() throws Exception {
        test("EventTarget", "SVGFESpotLightElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFETileElement() throws Exception {
        test("EventTarget", "SVGFETileElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFETurbulenceElement() throws Exception {
        test("EventTarget", "SVGFETurbulenceElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGFilterElement() throws Exception {
        test("EventTarget", "SVGFilterElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGForeignObjectElement() throws Exception {
        test("EventTarget", "SVGForeignObjectElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGGElement() throws Exception {
        test("EventTarget", "SVGGElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGGeometryElement() throws Exception {
        test("EventTarget", "SVGGeometryElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGGradientElement() throws Exception {
        test("EventTarget", "SVGGradientElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGGraphicsElement() throws Exception {
        test("EventTarget", "SVGGraphicsElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGImageElement() throws Exception {
        test("EventTarget", "SVGImageElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGLinearGradientElement() throws Exception {
        test("EventTarget", "SVGLinearGradientElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGLineElement() throws Exception {
        test("EventTarget", "SVGLineElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGMarkerElement() throws Exception {
        test("EventTarget", "SVGMarkerElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGMaskElement() throws Exception {
        test("EventTarget", "SVGMaskElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGMetadataElement() throws Exception {
        test("EventTarget", "SVGMetadataElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGMPathElement() throws Exception {
        test("EventTarget", "SVGMPathElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGPathElement() throws Exception {
        test("EventTarget", "SVGPathElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGPatternElement() throws Exception {
        test("EventTarget", "SVGPatternElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGPolygonElement() throws Exception {
        test("EventTarget", "SVGPolygonElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGPolylineElement() throws Exception {
        test("EventTarget", "SVGPolylineElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGRadialGradientElement() throws Exception {
        test("EventTarget", "SVGRadialGradientElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGRectElement() throws Exception {
        test("EventTarget", "SVGRectElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGScriptElement() throws Exception {
        test("EventTarget", "SVGScriptElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGSetElement() throws Exception {
        test("EventTarget", "SVGSetElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGStopElement() throws Exception {
        test("EventTarget", "SVGStopElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGStyleElement() throws Exception {
        test("EventTarget", "SVGStyleElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGSVGElement() throws Exception {
        test("EventTarget", "SVGSVGElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGSwitchElement() throws Exception {
        test("EventTarget", "SVGSwitchElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGSymbolElement() throws Exception {
        test("EventTarget", "SVGSymbolElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGTextContentElement() throws Exception {
        test("EventTarget", "SVGTextContentElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGTextElement() throws Exception {
        test("EventTarget", "SVGTextElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGTextPathElement() throws Exception {
        test("EventTarget", "SVGTextPathElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGTextPositioningElement() throws Exception {
        test("EventTarget", "SVGTextPositioningElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGTitleElement() throws Exception {
        test("EventTarget", "SVGTitleElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGTSpanElement() throws Exception {
        test("EventTarget", "SVGTSpanElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGUseElement() throws Exception {
        test("EventTarget", "SVGUseElement");
    }

    @Alerts("true/false")
    void _EventTarget_SVGViewElement() throws Exception {
        test("EventTarget", "SVGViewElement");
    }

    @Alerts("true/false")
    void _EventTarget_Text() throws Exception {
        test("EventTarget", "Text");
    }

    @Alerts("true/true")
    void _EventTarget_TextTrack() throws Exception {
        test("EventTarget", "TextTrack");
    }

    @Alerts("true/true")
    void _EventTarget_TextTrackCue() throws Exception {
        test("EventTarget", "TextTrackCue");
    }

    @Alerts("true/true")
    void _EventTarget_TextTrackList() throws Exception {
        test("EventTarget", "TextTrackList");
    }

    @Alerts("true/false")
    void _EventTarget_VTTCue() throws Exception {
        test("EventTarget", "VTTCue");
    }

    @Alerts("true/false")
    void _EventTarget_WaveShaperNode() throws Exception {
        test("EventTarget", "WaveShaperNode");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/true",
            EDGE = "true/true")
    void _EventTarget_webkitMediaStream() throws Exception {
        test("EventTarget", "webkitMediaStream");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/true",
            EDGE = "true/true")
    void _EventTarget_webkitRTCPeerConnection() throws Exception {
        test("EventTarget", "webkitRTCPeerConnection");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/true",
            EDGE = "true/true")
    void _EventTarget_webkitSpeechRecognition() throws Exception {
        test("EventTarget", "webkitSpeechRecognition");
    }

    @Alerts("true/true")
    void _EventTarget_WebSocket() throws Exception {
        test("EventTarget", "WebSocket");
    }

    @Alerts("true/true")
    void _EventTarget_Window() throws Exception {
        test("EventTarget", "Window");
    }

    @Alerts("true/true")
    void _EventTarget_Worker() throws Exception {
        test("EventTarget", "Worker");
    }

    @Alerts("true/false")
    void _EventTarget_XMLDocument() throws Exception {
        test("EventTarget", "XMLDocument");
    }

    @Alerts("true/false")
    void _EventTarget_XMLHttpRequest() throws Exception {
        test("EventTarget", "XMLHttpRequest");
    }

    @Alerts("true/true")
    void _EventTarget_XMLHttpRequestEventTarget() throws Exception {
        test("EventTarget", "XMLHttpRequestEventTarget");
    }

    @Alerts("true/false")
    void _EventTarget_XMLHttpRequestUpload() throws Exception {
        test("EventTarget", "XMLHttpRequestUpload");
    }

    @Alerts("true/false")
    void _Event_SpeechSynthesisErrorEvent() throws Exception {
        test("Event", "SpeechSynthesisErrorEvent");
    }

    @Alerts("false/false")
    void _EXT_texture_filter_anisotropic_EXT_texture_filter_anisotropic() throws Exception {
        test("EXT_texture_filter_anisotropic", "EXT_texture_filter_anisotropic");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _External_External() throws Exception {
        test("External", "External");
    }
}
