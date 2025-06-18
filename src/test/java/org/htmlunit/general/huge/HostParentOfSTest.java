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

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.params.provider.Arguments;

/**
 * Tests two Host classes, if one prototype is parent of another.
 *
 * This class handles all host names which starts by character 'S'.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HostParentOfSTest extends HostParentOf {

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    public static Collection<Arguments> data() throws Exception {
        return HostParentOf.data(input -> {
            final char ch = Character.toUpperCase(input.charAt(0));
            return ch == 'S' && StringUtils.compareIgnoreCase(input, "SVGG") < 0;
        });
    }

    @Alerts("true/false")
    void _Screen_Screen() throws Exception {
        test("Screen", "Screen");
    }

    @Alerts("true/false")
    void _ScreenOrientation_ScreenOrientation() throws Exception {
        test("ScreenOrientation", "ScreenOrientation");
    }

    @Alerts("true/false")
    void _ScriptProcessorNode_ScriptProcessorNode() throws Exception {
        test("ScriptProcessorNode", "ScriptProcessorNode");
    }

    @Alerts("true/false")
    void _SecurityPolicyViolationEvent_SecurityPolicyViolationEvent() throws Exception {
        test("SecurityPolicyViolationEvent", "SecurityPolicyViolationEvent");
    }

    @Alerts("true/false")
    void _Selection_Selection() throws Exception {
        test("Selection", "Selection");
    }

    @Alerts("true/false")
    void _ServiceWorker_ServiceWorker() throws Exception {
        test("ServiceWorker", "ServiceWorker");
    }

    @Alerts("true/false")
    void _ServiceWorkerContainer_ServiceWorkerContainer() throws Exception {
        test("ServiceWorkerContainer", "ServiceWorkerContainer");
    }

    @Alerts("true/false")
    void _ServiceWorkerRegistration_ServiceWorkerRegistration() throws Exception {
        test("ServiceWorkerRegistration", "ServiceWorkerRegistration");
    }

    @Alerts("true/false")
    void _Set_Set() throws Exception {
        test("Set", "Set");
    }

    @Alerts("false/false")
    void _SharedArrayBuffer_SharedArrayBuffer() throws Exception {
        test("SharedArrayBuffer", "SharedArrayBuffer");
    }

    @Alerts("true/false")
    void _ShadowRoot_ShadowRoot() throws Exception {
        test("ShadowRoot", "ShadowRoot");
    }

    @Alerts("true/false")
    void _SharedWorker_SharedWorker() throws Exception {
        test("SharedWorker", "SharedWorker");
    }

    @Alerts("true/false")
    void _SourceBuffer_SourceBuffer() throws Exception {
        test("SourceBuffer", "SourceBuffer");
    }

    @Alerts("true/false")
    void _SourceBufferList_SourceBufferList() throws Exception {
        test("SourceBufferList", "SourceBufferList");
    }

    @Alerts("true/false")
    void _SpeechSynthesis_SpeechSynthesis() throws Exception {
        test("SpeechSynthesis", "SpeechSynthesis");
    }

    @Alerts("true/false")
    void _SpeechSynthesisErrorEvent_SpeechSynthesisErrorEvent() throws Exception {
        test("SpeechSynthesisErrorEvent", "SpeechSynthesisErrorEvent");
    }

    @Alerts("true/true")
    void _SpeechSynthesisEvent_SpeechSynthesisErrorEvent() throws Exception {
        test("SpeechSynthesisEvent", "SpeechSynthesisErrorEvent");
    }

    @Alerts("true/false")
    void _SpeechSynthesisEvent_SpeechSynthesisEvent() throws Exception {
        test("SpeechSynthesisEvent", "SpeechSynthesisEvent");
    }

    @Alerts("true/false")
    void _SpeechSynthesisUtterance_SpeechSynthesisUtterance() throws Exception {
        test("SpeechSynthesisUtterance", "SpeechSynthesisUtterance");
    }

    @Alerts("true/false")
    void _SpeechSynthesisVoice_SpeechSynthesisVoice() throws Exception {
        test("SpeechSynthesisVoice", "SpeechSynthesisVoice");
    }

    @Alerts("true/false")
    void _StereoPannerNode_StereoPannerNode() throws Exception {
        test("StereoPannerNode", "StereoPannerNode");
    }

    @Alerts("true/false")
    void _Storage_Storage() throws Exception {
        test("Storage", "Storage");
    }

    @Alerts("true/false")
    void _StorageEvent_StorageEvent() throws Exception {
        test("StorageEvent", "StorageEvent");
    }

    @Alerts("true/false")
    void _StorageManager_StorageManager() throws Exception {
        test("StorageManager", "StorageManager");
    }

    @Alerts("false/false")
    void _StyleMedia_StyleMedia() throws Exception {
        test("StyleMedia", "StyleMedia");
    }

    @Alerts("true/true")
    void _StyleSheet_CSSStyleSheet() throws Exception {
        test("StyleSheet", "CSSStyleSheet");
    }

    @Alerts("true/false")
    void _StyleSheet_StyleSheet() throws Exception {
        test("StyleSheet", "StyleSheet");
    }

    @Alerts("true/false")
    void _StyleSheetList_StyleSheetList() throws Exception {
        test("StyleSheetList", "StyleSheetList");
    }

    @Alerts("true/false")
    void _SubmitEvent_SubmitEvent() throws Exception {
        test("SubmitEvent", "SubmitEvent");
    }

    @Alerts("true/false")
    void _SubtleCrypto_SubtleCrypto() throws Exception {
        test("SubtleCrypto", "SubtleCrypto");
    }

    @Alerts("true/false")
    void _SVGAElement_SVGAElement() throws Exception {
        test("SVGAElement", "SVGAElement");
    }

    @Alerts("true/false")
    void _SVGAngle_SVGAngle() throws Exception {
        test("SVGAngle", "SVGAngle");
    }

    @Alerts("true/false")
    void _SVGAnimatedAngle_SVGAnimatedAngle() throws Exception {
        test("SVGAnimatedAngle", "SVGAnimatedAngle");
    }

    @Alerts("true/false")
    void _SVGAnimatedBoolean_SVGAnimatedBoolean() throws Exception {
        test("SVGAnimatedBoolean", "SVGAnimatedBoolean");
    }

    @Alerts("true/false")
    void _SVGAnimatedEnumeration_SVGAnimatedEnumeration() throws Exception {
        test("SVGAnimatedEnumeration", "SVGAnimatedEnumeration");
    }

    @Alerts("true/false")
    void _SVGAnimatedInteger_SVGAnimatedInteger() throws Exception {
        test("SVGAnimatedInteger", "SVGAnimatedInteger");
    }

    @Alerts("true/false")
    void _SVGAnimatedLength_SVGAnimatedLength() throws Exception {
        test("SVGAnimatedLength", "SVGAnimatedLength");
    }

    @Alerts("true/false")
    void _SVGAnimatedLengthList_SVGAnimatedLengthList() throws Exception {
        test("SVGAnimatedLengthList", "SVGAnimatedLengthList");
    }

    @Alerts("true/false")
    void _SVGAnimatedNumber_SVGAnimatedNumber() throws Exception {
        test("SVGAnimatedNumber", "SVGAnimatedNumber");
    }

    @Alerts("true/false")
    void _SVGAnimatedNumberList_SVGAnimatedNumberList() throws Exception {
        test("SVGAnimatedNumberList", "SVGAnimatedNumberList");
    }

    @Alerts("true/false")
    void _SVGAnimatedPreserveAspectRatio_SVGAnimatedPreserveAspectRatio() throws Exception {
        test("SVGAnimatedPreserveAspectRatio", "SVGAnimatedPreserveAspectRatio");
    }

    @Alerts("true/false")
    void _SVGAnimatedRect_SVGAnimatedRect() throws Exception {
        test("SVGAnimatedRect", "SVGAnimatedRect");
    }

    @Alerts("true/false")
    void _SVGAnimatedString_SVGAnimatedString() throws Exception {
        test("SVGAnimatedString", "SVGAnimatedString");
    }

    @Alerts("true/false")
    void _SVGAnimatedTransformList_SVGAnimatedTransformList() throws Exception {
        test("SVGAnimatedTransformList", "SVGAnimatedTransformList");
    }

    @Alerts("true/false")
    void _SVGAnimateElement_SVGAnimateElement() throws Exception {
        test("SVGAnimateElement", "SVGAnimateElement");
    }

    @Alerts("true/false")
    void _SVGAnimateMotionElement_SVGAnimateMotionElement() throws Exception {
        test("SVGAnimateMotionElement", "SVGAnimateMotionElement");
    }

    @Alerts("true/false")
    void _SVGAnimateTransformElement_SVGAnimateTransformElement() throws Exception {
        test("SVGAnimateTransformElement", "SVGAnimateTransformElement");
    }

    @Alerts("true/true")
    void _SVGAnimationElement_SVGAnimateElement() throws Exception {
        test("SVGAnimationElement", "SVGAnimateElement");
    }

    @Alerts("true/true")
    void _SVGAnimationElement_SVGAnimateMotionElement() throws Exception {
        test("SVGAnimationElement", "SVGAnimateMotionElement");
    }

    @Alerts("true/true")
    void _SVGAnimationElement_SVGAnimateTransformElement() throws Exception {
        test("SVGAnimationElement", "SVGAnimateTransformElement");
    }

    @Alerts("true/false")
    void _SVGAnimationElement_SVGAnimationElement() throws Exception {
        test("SVGAnimationElement", "SVGAnimationElement");
    }

    @Alerts("false/false")
    void _SVGAnimationElement_SVGDiscardElement() throws Exception {
        test("SVGAnimationElement", "SVGDiscardElement");
    }

    @Alerts("true/true")
    void _SVGAnimationElement_SVGSetElement() throws Exception {
        test("SVGAnimationElement", "SVGSetElement");
    }

    @Alerts("true/false")
    void _SVGCircleElement_SVGCircleElement() throws Exception {
        test("SVGCircleElement", "SVGCircleElement");
    }

    @Alerts("true/false")
    void _SVGClipPathElement_SVGClipPathElement() throws Exception {
        test("SVGClipPathElement", "SVGClipPathElement");
    }

    @Alerts("true/false")
    void _SVGComponentTransferFunctionElement_SVGComponentTransferFunctionElement() throws Exception {
        test("SVGComponentTransferFunctionElement", "SVGComponentTransferFunctionElement");
    }

    @Alerts("true/true")
    void _SVGComponentTransferFunctionElement_SVGFEFuncAElement() throws Exception {
        test("SVGComponentTransferFunctionElement", "SVGFEFuncAElement");
    }

    @Alerts("true/true")
    void _SVGComponentTransferFunctionElement_SVGFEFuncBElement() throws Exception {
        test("SVGComponentTransferFunctionElement", "SVGFEFuncBElement");
    }

    @Alerts("true/true")
    void _SVGComponentTransferFunctionElement_SVGFEFuncGElement() throws Exception {
        test("SVGComponentTransferFunctionElement", "SVGFEFuncGElement");
    }

    @Alerts("true/true")
    void _SVGComponentTransferFunctionElement_SVGFEFuncRElement() throws Exception {
        test("SVGComponentTransferFunctionElement", "SVGFEFuncRElement");
    }

    @Alerts("true/false")
    void _SVGDefsElement_SVGDefsElement() throws Exception {
        test("SVGDefsElement", "SVGDefsElement");
    }

    @Alerts("true/false")
    void _SVGDescElement_SVGDescElement() throws Exception {
        test("SVGDescElement", "SVGDescElement");
    }

    @Alerts("false/false")
    void _SVGDiscardElement_SVGDiscardElement() throws Exception {
        test("SVGDiscardElement", "SVGDiscardElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGAElement() throws Exception {
        test("SVGElement", "SVGAElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGAnimateElement() throws Exception {
        test("SVGElement", "SVGAnimateElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGAnimateMotionElement() throws Exception {
        test("SVGElement", "SVGAnimateMotionElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGAnimateTransformElement() throws Exception {
        test("SVGElement", "SVGAnimateTransformElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGAnimationElement() throws Exception {
        test("SVGElement", "SVGAnimationElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGCircleElement() throws Exception {
        test("SVGElement", "SVGCircleElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGClipPathElement() throws Exception {
        test("SVGElement", "SVGClipPathElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGComponentTransferFunctionElement() throws Exception {
        test("SVGElement", "SVGComponentTransferFunctionElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGDefsElement() throws Exception {
        test("SVGElement", "SVGDefsElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGDescElement() throws Exception {
        test("SVGElement", "SVGDescElement");
    }

    @Alerts("false/false")
    void _SVGElement_SVGDiscardElement() throws Exception {
        test("SVGElement", "SVGDiscardElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGElement() throws Exception {
        test("SVGElement", "SVGElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGEllipseElement() throws Exception {
        test("SVGElement", "SVGEllipseElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGFEBlendElement() throws Exception {
        test("SVGElement", "SVGFEBlendElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGFEColorMatrixElement() throws Exception {
        test("SVGElement", "SVGFEColorMatrixElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGFEComponentTransferElement() throws Exception {
        test("SVGElement", "SVGFEComponentTransferElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGFECompositeElement() throws Exception {
        test("SVGElement", "SVGFECompositeElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGFEConvolveMatrixElement() throws Exception {
        test("SVGElement", "SVGFEConvolveMatrixElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGFEDiffuseLightingElement() throws Exception {
        test("SVGElement", "SVGFEDiffuseLightingElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGFEDisplacementMapElement() throws Exception {
        test("SVGElement", "SVGFEDisplacementMapElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGFEDistantLightElement() throws Exception {
        test("SVGElement", "SVGFEDistantLightElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGFEDropShadowElement() throws Exception {
        test("SVGElement", "SVGFEDropShadowElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGFEFloodElement() throws Exception {
        test("SVGElement", "SVGFEFloodElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGFEFuncAElement() throws Exception {
        test("SVGElement", "SVGFEFuncAElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGFEFuncBElement() throws Exception {
        test("SVGElement", "SVGFEFuncBElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGFEFuncGElement() throws Exception {
        test("SVGElement", "SVGFEFuncGElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGFEFuncRElement() throws Exception {
        test("SVGElement", "SVGFEFuncRElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGFEGaussianBlurElement() throws Exception {
        test("SVGElement", "SVGFEGaussianBlurElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGFEImageElement() throws Exception {
        test("SVGElement", "SVGFEImageElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGFEMergeElement() throws Exception {
        test("SVGElement", "SVGFEMergeElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGFEMergeNodeElement() throws Exception {
        test("SVGElement", "SVGFEMergeNodeElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGFEMorphologyElement() throws Exception {
        test("SVGElement", "SVGFEMorphologyElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGFEOffsetElement() throws Exception {
        test("SVGElement", "SVGFEOffsetElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGFEPointLightElement() throws Exception {
        test("SVGElement", "SVGFEPointLightElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGFESpecularLightingElement() throws Exception {
        test("SVGElement", "SVGFESpecularLightingElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGFESpotLightElement() throws Exception {
        test("SVGElement", "SVGFESpotLightElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGFETileElement() throws Exception {
        test("SVGElement", "SVGFETileElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGFETurbulenceElement() throws Exception {
        test("SVGElement", "SVGFETurbulenceElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGFilterElement() throws Exception {
        test("SVGElement", "SVGFilterElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGForeignObjectElement() throws Exception {
        test("SVGElement", "SVGForeignObjectElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGGElement() throws Exception {
        test("SVGElement", "SVGGElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGGeometryElement() throws Exception {
        test("SVGElement", "SVGGeometryElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGGradientElement() throws Exception {
        test("SVGElement", "SVGGradientElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGGraphicsElement() throws Exception {
        test("SVGElement", "SVGGraphicsElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGImageElement() throws Exception {
        test("SVGElement", "SVGImageElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGLinearGradientElement() throws Exception {
        test("SVGElement", "SVGLinearGradientElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGLineElement() throws Exception {
        test("SVGElement", "SVGLineElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGMarkerElement() throws Exception {
        test("SVGElement", "SVGMarkerElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGMaskElement() throws Exception {
        test("SVGElement", "SVGMaskElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGMetadataElement() throws Exception {
        test("SVGElement", "SVGMetadataElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGMPathElement() throws Exception {
        test("SVGElement", "SVGMPathElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGPathElement() throws Exception {
        test("SVGElement", "SVGPathElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGPatternElement() throws Exception {
        test("SVGElement", "SVGPatternElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGPolygonElement() throws Exception {
        test("SVGElement", "SVGPolygonElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGPolylineElement() throws Exception {
        test("SVGElement", "SVGPolylineElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGRadialGradientElement() throws Exception {
        test("SVGElement", "SVGRadialGradientElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGRectElement() throws Exception {
        test("SVGElement", "SVGRectElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGScriptElement() throws Exception {
        test("SVGElement", "SVGScriptElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGSetElement() throws Exception {
        test("SVGElement", "SVGSetElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGStopElement() throws Exception {
        test("SVGElement", "SVGStopElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGStyleElement() throws Exception {
        test("SVGElement", "SVGStyleElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGSVGElement() throws Exception {
        test("SVGElement", "SVGSVGElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGSwitchElement() throws Exception {
        test("SVGElement", "SVGSwitchElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGSymbolElement() throws Exception {
        test("SVGElement", "SVGSymbolElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGTextContentElement() throws Exception {
        test("SVGElement", "SVGTextContentElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGTextElement() throws Exception {
        test("SVGElement", "SVGTextElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGTextPathElement() throws Exception {
        test("SVGElement", "SVGTextPathElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGTextPositioningElement() throws Exception {
        test("SVGElement", "SVGTextPositioningElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGTitleElement() throws Exception {
        test("SVGElement", "SVGTitleElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGTSpanElement() throws Exception {
        test("SVGElement", "SVGTSpanElement");
    }

    @Alerts("true/false")
    void _SVGElement_SVGUseElement() throws Exception {
        test("SVGElement", "SVGUseElement");
    }

    @Alerts("true/true")
    void _SVGElement_SVGViewElement() throws Exception {
        test("SVGElement", "SVGViewElement");
    }

    @Alerts("true/false")
    void _SVGEllipseElement_SVGEllipseElement() throws Exception {
        test("SVGEllipseElement", "SVGEllipseElement");
    }

    @Alerts("true/false")
    void _SVGFEBlendElement_SVGFEBlendElement() throws Exception {
        test("SVGFEBlendElement", "SVGFEBlendElement");
    }

    @Alerts("true/false")
    void _SVGFEColorMatrixElement_SVGFEColorMatrixElement() throws Exception {
        test("SVGFEColorMatrixElement", "SVGFEColorMatrixElement");
    }

    @Alerts("true/false")
    void _SVGFEComponentTransferElement_SVGFEComponentTransferElement() throws Exception {
        test("SVGFEComponentTransferElement", "SVGFEComponentTransferElement");
    }

    @Alerts("true/false")
    void _SVGFECompositeElement_SVGFECompositeElement() throws Exception {
        test("SVGFECompositeElement", "SVGFECompositeElement");
    }

    @Alerts("true/false")
    void _SVGFEConvolveMatrixElement_SVGFEConvolveMatrixElement() throws Exception {
        test("SVGFEConvolveMatrixElement", "SVGFEConvolveMatrixElement");
    }

    @Alerts("true/false")
    void _SVGFEDiffuseLightingElement_SVGFEDiffuseLightingElement() throws Exception {
        test("SVGFEDiffuseLightingElement", "SVGFEDiffuseLightingElement");
    }

    @Alerts("true/false")
    void _SVGFEDisplacementMapElement_SVGFEDisplacementMapElement() throws Exception {
        test("SVGFEDisplacementMapElement", "SVGFEDisplacementMapElement");
    }

    @Alerts("true/false")
    void _SVGFEDistantLightElement_SVGFEDistantLightElement() throws Exception {
        test("SVGFEDistantLightElement", "SVGFEDistantLightElement");
    }

    @Alerts("true/false")
    void _SVGFEDropShadowElement_SVGFEDropShadowElement() throws Exception {
        test("SVGFEDropShadowElement", "SVGFEDropShadowElement");
    }

    @Alerts("true/false")
    void _SVGFEFloodElement_SVGFEFloodElement() throws Exception {
        test("SVGFEFloodElement", "SVGFEFloodElement");
    }

    @Alerts("true/false")
    void _SVGFEFuncAElement_SVGFEFuncAElement() throws Exception {
        test("SVGFEFuncAElement", "SVGFEFuncAElement");
    }

    @Alerts("true/false")
    void _SVGFEFuncBElement_SVGFEFuncBElement() throws Exception {
        test("SVGFEFuncBElement", "SVGFEFuncBElement");
    }

    @Alerts("true/false")
    void _SVGFEFuncGElement_SVGFEFuncGElement() throws Exception {
        test("SVGFEFuncGElement", "SVGFEFuncGElement");
    }

    @Alerts("true/false")
    void _SVGFEFuncRElement_SVGFEFuncRElement() throws Exception {
        test("SVGFEFuncRElement", "SVGFEFuncRElement");
    }

    @Alerts("true/false")
    void _SVGFEGaussianBlurElement_SVGFEGaussianBlurElement() throws Exception {
        test("SVGFEGaussianBlurElement", "SVGFEGaussianBlurElement");
    }

    @Alerts("true/false")
    void _SVGFEImageElement_SVGFEImageElement() throws Exception {
        test("SVGFEImageElement", "SVGFEImageElement");
    }

    @Alerts("true/false")
    void _SVGFEMergeElement_SVGFEMergeElement() throws Exception {
        test("SVGFEMergeElement", "SVGFEMergeElement");
    }

    @Alerts("true/false")
    void _SVGFEMergeNodeElement_SVGFEMergeNodeElement() throws Exception {
        test("SVGFEMergeNodeElement", "SVGFEMergeNodeElement");
    }

    @Alerts("true/false")
    void _SVGFEMorphologyElement_SVGFEMorphologyElement() throws Exception {
        test("SVGFEMorphologyElement", "SVGFEMorphologyElement");
    }

    @Alerts("true/false")
    void _SVGFEOffsetElement_SVGFEOffsetElement() throws Exception {
        test("SVGFEOffsetElement", "SVGFEOffsetElement");
    }

    @Alerts("true/false")
    void _SVGFEPointLightElement_SVGFEPointLightElement() throws Exception {
        test("SVGFEPointLightElement", "SVGFEPointLightElement");
    }

    @Alerts("true/false")
    void _SVGFESpecularLightingElement_SVGFESpecularLightingElement() throws Exception {
        test("SVGFESpecularLightingElement", "SVGFESpecularLightingElement");
    }

    @Alerts("true/false")
    void _SVGFESpotLightElement_SVGFESpotLightElement() throws Exception {
        test("SVGFESpotLightElement", "SVGFESpotLightElement");
    }

    @Alerts("true/false")
    void _SVGFETileElement_SVGFETileElement() throws Exception {
        test("SVGFETileElement", "SVGFETileElement");
    }

    @Alerts("true/false")
    void _SVGFETurbulenceElement_SVGFETurbulenceElement() throws Exception {
        test("SVGFETurbulenceElement", "SVGFETurbulenceElement");
    }

    @Alerts("true/false")
    void _SVGFilterElement_SVGFilterElement() throws Exception {
        test("SVGFilterElement", "SVGFilterElement");
    }

    @Alerts("true/false")
    void _SVGForeignObjectElement_SVGForeignObjectElement() throws Exception {
        test("SVGForeignObjectElement", "SVGForeignObjectElement");
    }
}
