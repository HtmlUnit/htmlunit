/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.general.huge;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import com.gargoylesoftware.htmlunit.junit.BrowserParameterizedRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests two Host classes, if one prototype is parent of another.
 *
 * This class handles all host names which starts by character 'S'.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserParameterizedRunner.class)
public class HostParentOfSTest extends HostParentOf {

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    @Parameters
    public static Collection<Object[]> data() throws Exception {
        return HostParentOf.data(input -> {
            final char ch = Character.toUpperCase(input.charAt(0));
            return ch == 'S' && StringUtils.compareIgnoreCase(input, "SVGG") < 0;
        });
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
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _ScreenOrientation_ScreenOrientation() throws Exception {
        test("ScreenOrientation", "ScreenOrientation");
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
    public void _SecurityPolicyViolationEvent_SecurityPolicyViolationEvent() throws Exception {
        test("SecurityPolicyViolationEvent", "SecurityPolicyViolationEvent");
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
            CHROME = "true",
            EDGE = "true",
            FF = "true",
            FF_ESR = "true")
    public void _ServiceWorker_ServiceWorker() throws Exception {
        test("ServiceWorker", "ServiceWorker");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            EDGE = "true",
            FF = "true",
            FF_ESR = "true")
    public void _ServiceWorkerContainer_ServiceWorkerContainer() throws Exception {
        test("ServiceWorkerContainer", "ServiceWorkerContainer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            EDGE = "true",
            FF = "true",
            FF_ESR = "true")
    public void _ServiceWorkerRegistration_ServiceWorkerRegistration() throws Exception {
        test("ServiceWorkerRegistration", "ServiceWorkerRegistration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Set_Set() throws Exception {
        test("Set", "Set");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SharedArrayBuffer_SharedArrayBuffer() throws Exception {
        test("SharedArrayBuffer", "SharedArrayBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _ShadowRoot_ShadowRoot() throws Exception {
        test("ShadowRoot", "ShadowRoot");
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
    @Alerts("true")
    public void _SourceBuffer_SourceBuffer() throws Exception {
        test("SourceBuffer", "SourceBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SourceBufferList_SourceBufferList() throws Exception {
        test("SourceBufferList", "SourceBufferList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    public void _SpeechSynthesis_SpeechSynthesis() throws Exception {
        test("SpeechSynthesis", "SpeechSynthesis");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SpeechSynthesisErrorEvent_SpeechSynthesisErrorEvent() throws Exception {
        test("SpeechSynthesisErrorEvent", "SpeechSynthesisErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SpeechSynthesisEvent_SpeechSynthesisErrorEvent() throws Exception {
        test("SpeechSynthesisEvent", "SpeechSynthesisErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SpeechSynthesisEvent_SpeechSynthesisEvent() throws Exception {
        test("SpeechSynthesisEvent", "SpeechSynthesisEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SpeechSynthesisUtterance_SpeechSynthesisUtterance() throws Exception {
        test("SpeechSynthesisUtterance", "SpeechSynthesisUtterance");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    public void _SpeechSynthesisVoice_SpeechSynthesisVoice() throws Exception {
        test("SpeechSynthesisVoice", "SpeechSynthesisVoice");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _StereoPannerNode_StereoPannerNode() throws Exception {
        test("StereoPannerNode", "StereoPannerNode");
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
    public void _StorageEvent_StorageEvent() throws Exception {
        test("StorageEvent", "StorageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _StorageManager_StorageManager() throws Exception {
        test("StorageManager", "StorageManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _StyleMedia_StyleMedia() throws Exception {
        test("StyleMedia", "StyleMedia");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _StyleSheet_CSSStyleSheet() throws Exception {
        test("StyleSheet", "CSSStyleSheet");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _StyleSheet_StyleSheet() throws Exception {
        test("StyleSheet", "StyleSheet");
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
    public void _SubtleCrypto_SubtleCrypto() throws Exception {
        test("SubtleCrypto", "SubtleCrypto");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGAElement_SVGAElement() throws Exception {
        test("SVGAElement", "SVGAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGAngle_SVGAngle() throws Exception {
        test("SVGAngle", "SVGAngle");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGAnimatedAngle_SVGAnimatedAngle() throws Exception {
        test("SVGAnimatedAngle", "SVGAnimatedAngle");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGAnimatedBoolean_SVGAnimatedBoolean() throws Exception {
        test("SVGAnimatedBoolean", "SVGAnimatedBoolean");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGAnimatedEnumeration_SVGAnimatedEnumeration() throws Exception {
        test("SVGAnimatedEnumeration", "SVGAnimatedEnumeration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGAnimatedInteger_SVGAnimatedInteger() throws Exception {
        test("SVGAnimatedInteger", "SVGAnimatedInteger");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGAnimatedLength_SVGAnimatedLength() throws Exception {
        test("SVGAnimatedLength", "SVGAnimatedLength");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGAnimatedLengthList_SVGAnimatedLengthList() throws Exception {
        test("SVGAnimatedLengthList", "SVGAnimatedLengthList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGAnimatedNumber_SVGAnimatedNumber() throws Exception {
        test("SVGAnimatedNumber", "SVGAnimatedNumber");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGAnimatedNumberList_SVGAnimatedNumberList() throws Exception {
        test("SVGAnimatedNumberList", "SVGAnimatedNumberList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGAnimatedPreserveAspectRatio_SVGAnimatedPreserveAspectRatio() throws Exception {
        test("SVGAnimatedPreserveAspectRatio", "SVGAnimatedPreserveAspectRatio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGAnimatedRect_SVGAnimatedRect() throws Exception {
        test("SVGAnimatedRect", "SVGAnimatedRect");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGAnimatedString_SVGAnimatedString() throws Exception {
        test("SVGAnimatedString", "SVGAnimatedString");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGAnimatedTransformList_SVGAnimatedTransformList() throws Exception {
        test("SVGAnimatedTransformList", "SVGAnimatedTransformList");
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
            IE = "false")
    public void _SVGAnimationElement_SVGAnimateElement() throws Exception {
        test("SVGAnimationElement", "SVGAnimateElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGAnimationElement_SVGAnimateMotionElement() throws Exception {
        test("SVGAnimationElement", "SVGAnimateMotionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGAnimationElement_SVGAnimateTransformElement() throws Exception {
        test("SVGAnimationElement", "SVGAnimateTransformElement");
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
            IE = "false")
    public void _SVGAnimationElement_SVGSetElement() throws Exception {
        test("SVGAnimationElement", "SVGSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGCircleElement_SVGCircleElement() throws Exception {
        test("SVGCircleElement", "SVGCircleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGClipPathElement_SVGClipPathElement() throws Exception {
        test("SVGClipPathElement", "SVGClipPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGComponentTransferFunctionElement_SVGComponentTransferFunctionElement() throws Exception {
        test("SVGComponentTransferFunctionElement", "SVGComponentTransferFunctionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGComponentTransferFunctionElement_SVGFEFuncAElement() throws Exception {
        test("SVGComponentTransferFunctionElement", "SVGFEFuncAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGComponentTransferFunctionElement_SVGFEFuncBElement() throws Exception {
        test("SVGComponentTransferFunctionElement", "SVGFEFuncBElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGComponentTransferFunctionElement_SVGFEFuncGElement() throws Exception {
        test("SVGComponentTransferFunctionElement", "SVGFEFuncGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGComponentTransferFunctionElement_SVGFEFuncRElement() throws Exception {
        test("SVGComponentTransferFunctionElement", "SVGFEFuncRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGDefsElement_SVGDefsElement() throws Exception {
        test("SVGDefsElement", "SVGDefsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGDescElement_SVGDescElement() throws Exception {
        test("SVGDescElement", "SVGDescElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGAElement() throws Exception {
        test("SVGElement", "SVGAElement");
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
            IE = "false")
    public void _SVGElement_SVGAnimationElement() throws Exception {
        test("SVGElement", "SVGAnimationElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGCircleElement() throws Exception {
        test("SVGElement", "SVGCircleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGClipPathElement() throws Exception {
        test("SVGElement", "SVGClipPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGComponentTransferFunctionElement() throws Exception {
        test("SVGElement", "SVGComponentTransferFunctionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGDefsElement() throws Exception {
        test("SVGElement", "SVGDefsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGDescElement() throws Exception {
        test("SVGElement", "SVGDescElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGElement() throws Exception {
        test("SVGElement", "SVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGEllipseElement() throws Exception {
        test("SVGElement", "SVGEllipseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGFEBlendElement() throws Exception {
        test("SVGElement", "SVGFEBlendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGFEColorMatrixElement() throws Exception {
        test("SVGElement", "SVGFEColorMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGFEComponentTransferElement() throws Exception {
        test("SVGElement", "SVGFEComponentTransferElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGFECompositeElement() throws Exception {
        test("SVGElement", "SVGFECompositeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGFEConvolveMatrixElement() throws Exception {
        test("SVGElement", "SVGFEConvolveMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGFEDiffuseLightingElement() throws Exception {
        test("SVGElement", "SVGFEDiffuseLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGFEDisplacementMapElement() throws Exception {
        test("SVGElement", "SVGFEDisplacementMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGFEDistantLightElement() throws Exception {
        test("SVGElement", "SVGFEDistantLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGElement_SVGFEDropShadowElement() throws Exception {
        test("SVGElement", "SVGFEDropShadowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGFEFloodElement() throws Exception {
        test("SVGElement", "SVGFEFloodElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGFEFuncAElement() throws Exception {
        test("SVGElement", "SVGFEFuncAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGFEFuncBElement() throws Exception {
        test("SVGElement", "SVGFEFuncBElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGFEFuncGElement() throws Exception {
        test("SVGElement", "SVGFEFuncGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGFEFuncRElement() throws Exception {
        test("SVGElement", "SVGFEFuncRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGFEGaussianBlurElement() throws Exception {
        test("SVGElement", "SVGFEGaussianBlurElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGFEImageElement() throws Exception {
        test("SVGElement", "SVGFEImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGFEMergeElement() throws Exception {
        test("SVGElement", "SVGFEMergeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGFEMergeNodeElement() throws Exception {
        test("SVGElement", "SVGFEMergeNodeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGFEMorphologyElement() throws Exception {
        test("SVGElement", "SVGFEMorphologyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGFEOffsetElement() throws Exception {
        test("SVGElement", "SVGFEOffsetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGFEPointLightElement() throws Exception {
        test("SVGElement", "SVGFEPointLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGFESpecularLightingElement() throws Exception {
        test("SVGElement", "SVGFESpecularLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGFESpotLightElement() throws Exception {
        test("SVGElement", "SVGFESpotLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGFETileElement() throws Exception {
        test("SVGElement", "SVGFETileElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGFETurbulenceElement() throws Exception {
        test("SVGElement", "SVGFETurbulenceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
    @Alerts("true")
    public void _SVGElement_SVGGElement() throws Exception {
        test("SVGElement", "SVGGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGElement_SVGGeometryElement() throws Exception {
        test("SVGElement", "SVGGeometryElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGGradientElement() throws Exception {
        test("SVGElement", "SVGGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGElement_SVGGraphicsElement() throws Exception {
        test("SVGElement", "SVGGraphicsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGImageElement() throws Exception {
        test("SVGElement", "SVGImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGLinearGradientElement() throws Exception {
        test("SVGElement", "SVGLinearGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGLineElement() throws Exception {
        test("SVGElement", "SVGLineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGMarkerElement() throws Exception {
        test("SVGElement", "SVGMarkerElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGMaskElement() throws Exception {
        test("SVGElement", "SVGMaskElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
    @Alerts("true")
    public void _SVGElement_SVGPathElement() throws Exception {
        test("SVGElement", "SVGPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGPatternElement() throws Exception {
        test("SVGElement", "SVGPatternElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGPolygonElement() throws Exception {
        test("SVGElement", "SVGPolygonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGPolylineElement() throws Exception {
        test("SVGElement", "SVGPolylineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGRadialGradientElement() throws Exception {
        test("SVGElement", "SVGRadialGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGRectElement() throws Exception {
        test("SVGElement", "SVGRectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
    @Alerts("true")
    public void _SVGElement_SVGStopElement() throws Exception {
        test("SVGElement", "SVGStopElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGStyleElement() throws Exception {
        test("SVGElement", "SVGStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGSVGElement() throws Exception {
        test("SVGElement", "SVGSVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGSwitchElement() throws Exception {
        test("SVGElement", "SVGSwitchElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGSymbolElement() throws Exception {
        test("SVGElement", "SVGSymbolElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGTextContentElement() throws Exception {
        test("SVGElement", "SVGTextContentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGTextElement() throws Exception {
        test("SVGElement", "SVGTextElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGTextPathElement() throws Exception {
        test("SVGElement", "SVGTextPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGTextPositioningElement() throws Exception {
        test("SVGElement", "SVGTextPositioningElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGTitleElement() throws Exception {
        test("SVGElement", "SVGTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGTSpanElement() throws Exception {
        test("SVGElement", "SVGTSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGUseElement() throws Exception {
        test("SVGElement", "SVGUseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGElement_SVGViewElement() throws Exception {
        test("SVGElement", "SVGViewElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGEllipseElement_SVGEllipseElement() throws Exception {
        test("SVGEllipseElement", "SVGEllipseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGFEBlendElement_SVGFEBlendElement() throws Exception {
        test("SVGFEBlendElement", "SVGFEBlendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGFEColorMatrixElement_SVGFEColorMatrixElement() throws Exception {
        test("SVGFEColorMatrixElement", "SVGFEColorMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGFEComponentTransferElement_SVGFEComponentTransferElement() throws Exception {
        test("SVGFEComponentTransferElement", "SVGFEComponentTransferElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGFECompositeElement_SVGFECompositeElement() throws Exception {
        test("SVGFECompositeElement", "SVGFECompositeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGFEConvolveMatrixElement_SVGFEConvolveMatrixElement() throws Exception {
        test("SVGFEConvolveMatrixElement", "SVGFEConvolveMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGFEDiffuseLightingElement_SVGFEDiffuseLightingElement() throws Exception {
        test("SVGFEDiffuseLightingElement", "SVGFEDiffuseLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGFEDisplacementMapElement_SVGFEDisplacementMapElement() throws Exception {
        test("SVGFEDisplacementMapElement", "SVGFEDisplacementMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGFEDistantLightElement_SVGFEDistantLightElement() throws Exception {
        test("SVGFEDistantLightElement", "SVGFEDistantLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _SVGFEDropShadowElement_SVGFEDropShadowElement() throws Exception {
        test("SVGFEDropShadowElement", "SVGFEDropShadowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGFEFloodElement_SVGFEFloodElement() throws Exception {
        test("SVGFEFloodElement", "SVGFEFloodElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGFEFuncAElement_SVGFEFuncAElement() throws Exception {
        test("SVGFEFuncAElement", "SVGFEFuncAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGFEFuncBElement_SVGFEFuncBElement() throws Exception {
        test("SVGFEFuncBElement", "SVGFEFuncBElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGFEFuncGElement_SVGFEFuncGElement() throws Exception {
        test("SVGFEFuncGElement", "SVGFEFuncGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGFEFuncRElement_SVGFEFuncRElement() throws Exception {
        test("SVGFEFuncRElement", "SVGFEFuncRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGFEGaussianBlurElement_SVGFEGaussianBlurElement() throws Exception {
        test("SVGFEGaussianBlurElement", "SVGFEGaussianBlurElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGFEImageElement_SVGFEImageElement() throws Exception {
        test("SVGFEImageElement", "SVGFEImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGFEMergeElement_SVGFEMergeElement() throws Exception {
        test("SVGFEMergeElement", "SVGFEMergeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGFEMergeNodeElement_SVGFEMergeNodeElement() throws Exception {
        test("SVGFEMergeNodeElement", "SVGFEMergeNodeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGFEMorphologyElement_SVGFEMorphologyElement() throws Exception {
        test("SVGFEMorphologyElement", "SVGFEMorphologyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGFEOffsetElement_SVGFEOffsetElement() throws Exception {
        test("SVGFEOffsetElement", "SVGFEOffsetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGFEPointLightElement_SVGFEPointLightElement() throws Exception {
        test("SVGFEPointLightElement", "SVGFEPointLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGFESpecularLightingElement_SVGFESpecularLightingElement() throws Exception {
        test("SVGFESpecularLightingElement", "SVGFESpecularLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGFESpotLightElement_SVGFESpotLightElement() throws Exception {
        test("SVGFESpotLightElement", "SVGFESpotLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGFETileElement_SVGFETileElement() throws Exception {
        test("SVGFETileElement", "SVGFETileElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _SVGFETurbulenceElement_SVGFETurbulenceElement() throws Exception {
        test("SVGFETurbulenceElement", "SVGFETurbulenceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
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
}
