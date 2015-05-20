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
 * This class handles all host names which starts by character 'T' to 'Z'.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserParameterizedRunner.class)
public class HostParentOfTTest extends HostParentOf {

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
                return ch >= 'T' && ch <= 'Z';
            }
        });
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
            IE = "false")
    public void _WaveShaperNode_WaveShaperNode() throws Exception {
        test("WaveShaperNode", "WaveShaperNode");
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
    public void _UIEvent_MouseWheelEvent() throws Exception {
        test("UIEvent", "MouseWheelEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _TextDecoder_TextDecoder() throws Exception {
        test("TextDecoder", "TextDecoder");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _TextEncoder_TextEncoder() throws Exception {
        test("TextEncoder", "TextEncoder");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _TextMetrics_TextMetrics() throws Exception {
        test("TextMetrics", "TextMetrics");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _URL_URL() throws Exception {
        test("URL", "URL");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _WeakMap_WeakMap() throws Exception {
        test("WeakMap", "WeakMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _WeakSet_WeakSet() throws Exception {
        test("WeakSet", "WeakSet");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitRTCPeerConnection_webkitRTCPeerConnection() throws Exception {
        test("webkitRTCPeerConnection", "webkitRTCPeerConnection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _TimeRanges_TimeRanges() throws Exception {
        test("TimeRanges", "TimeRanges");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _URIError_URIError() throws Exception {
        test("URIError", "URIError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _ValidityState_ValidityState() throws Exception {
        test("ValidityState", "ValidityState");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            IE11 = "true")
    public void _XMLHttpRequestEventTarget_XMLHttpRequestEventTarget() throws Exception {
        test("XMLHttpRequestEventTarget", "XMLHttpRequestEventTarget");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _webkitSpeechRecognition_webkitSpeechRecognition() throws Exception {
        test("webkitSpeechRecognition", "webkitSpeechRecognition");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            IE11 = "true")
    public void _TextEvent_TextEvent() throws Exception {
        test("TextEvent", "TextEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _TextTrackCueList_TextTrackCueList() throws Exception {
        test("TextTrackCueList", "TextTrackCueList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            IE11 = "true")
    public void _TextTrackCue_TextTrackCue() throws Exception {
        test("TextTrackCue", "TextTrackCue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _TextTrackCue_VTTCue() throws Exception {
        test("TextTrackCue", "VTTCue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _TextTrackList_TextTrackList() throws Exception {
        test("TextTrackList", "TextTrackList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _TextTrack_TextTrack() throws Exception {
        test("TextTrack", "TextTrack");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _TrackEvent_TrackEvent() throws Exception {
        test("TrackEvent", "TrackEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    @NotYetImplemented(CHROME)
    public void _TransitionEvent_WebKitTransitionEvent() throws Exception {
        test("TransitionEvent", "WebKitTransitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _UIEvent_SVGZoomEvent() throws Exception {
        test("UIEvent", "SVGZoomEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            IE11 = "true")
    public void _UIEvent_TextEvent() throws Exception {
        test("UIEvent", "TextEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _VTTCue_VTTCue() throws Exception {
        test("VTTCue", "VTTCue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _WebGLActiveInfo_WebGLActiveInfo() throws Exception {
        test("WebGLActiveInfo", "WebGLActiveInfo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _WebGLBuffer_WebGLBuffer() throws Exception {
        test("WebGLBuffer", "WebGLBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            IE11 = "true")
    public void _WebGLContextEvent_WebGLContextEvent() throws Exception {
        test("WebGLContextEvent", "WebGLContextEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _WebGLFramebuffer_WebGLFramebuffer() throws Exception {
        test("WebGLFramebuffer", "WebGLFramebuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _WebGLProgram_WebGLProgram() throws Exception {
        test("WebGLProgram", "WebGLProgram");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _WebGLRenderbuffer_WebGLRenderbuffer() throws Exception {
        test("WebGLRenderbuffer", "WebGLRenderbuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _WebGLShaderPrecisionFormat_WebGLShaderPrecisionFormat() throws Exception {
        test("WebGLShaderPrecisionFormat", "WebGLShaderPrecisionFormat");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _WebGLShader_WebGLShader() throws Exception {
        test("WebGLShader", "WebGLShader");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _WebGLTexture_WebGLTexture() throws Exception {
        test("WebGLTexture", "WebGLTexture");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE8 = "false")
    public void _WebGLUniformLocation_WebGLUniformLocation() throws Exception {
        test("WebGLUniformLocation", "WebGLUniformLocation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _WebKitAnimationEvent_WebKitAnimationEvent() throws Exception {
        test("WebKitAnimationEvent", "WebKitAnimationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _WebKitCSSMatrix_WebKitCSSMatrix() throws Exception {
        test("WebKitCSSMatrix", "WebKitCSSMatrix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    @NotYetImplemented(CHROME)
    public void _WebKitTransitionEvent_TransitionEvent() throws Exception {
        test("WebKitTransitionEvent", "TransitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _WebKitTransitionEvent_WebKitTransitionEvent() throws Exception {
        test("WebKitTransitionEvent", "WebKitTransitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false",
            FF31 = "false")
    public void _XMLHttpRequestEventTarget_XMLHttpRequestUpload() throws Exception {
        test("XMLHttpRequestEventTarget", "XMLHttpRequestUpload");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _XMLHttpRequestProgressEvent_XMLHttpRequestProgressEvent() throws Exception {
        test("XMLHttpRequestProgressEvent", "XMLHttpRequestProgressEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _XMLHttpRequestUpload_XMLHttpRequestUpload() throws Exception {
        test("XMLHttpRequestUpload", "XMLHttpRequestUpload");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _XPathExpression_XPathExpression() throws Exception {
        test("XPathExpression", "XPathExpression");
    }

}
