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
import org.junit.jupiter.params.provider.Arguments;

/**
 * Tests two Host classes, if one prototype is parent of another.
 *
 * This class handles all host names which starts by character 'W' to 'Z'.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HostParentOfWTest extends HostParentOf {

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    public static Collection<Arguments> data() throws Exception {
        return HostParentOf.data(input -> {
            final char ch = Character.toUpperCase(input.charAt(0));
            return ch >= 'W' && ch <= 'Z';
        });
    }

    @Alerts("true/false")
    void _WaveShaperNode_WaveShaperNode() throws Exception {
        test("WaveShaperNode", "WaveShaperNode");
    }

    @Alerts("true/false")
    void _WeakMap_WeakMap() throws Exception {
        test("WeakMap", "WeakMap");
    }

    @Alerts("true/false")
    void _WeakSet_WeakSet() throws Exception {
        test("WeakSet", "WeakSet");
    }

    @Alerts("false/false")
    void _WEBGL_compressed_texture_s3tc_WEBGL_compressed_texture_s3tc() throws Exception {
        test("WEBGL_compressed_texture_s3tc", "WEBGL_compressed_texture_s3tc");
    }

    @Alerts("false/false")
    void _WEBGL_debug_renderer_info_WEBGL_debug_renderer_info() throws Exception {
        test("WEBGL_debug_renderer_info", "WEBGL_debug_renderer_info");
    }

    @Alerts("true/false")
    void _WebGL2RenderingContext_WebGL2RenderingContext() throws Exception {
        test("WebGL2RenderingContext", "WebGL2RenderingContext");
    }

    @Alerts("true/false")
    void _WebGLActiveInfo_WebGLActiveInfo() throws Exception {
        test("WebGLActiveInfo", "WebGLActiveInfo");
    }

    @Alerts("true/false")
    void _WebGLBuffer_WebGLBuffer() throws Exception {
        test("WebGLBuffer", "WebGLBuffer");
    }

    @Alerts("true/false")
    void _WebGLContextEvent_WebGLContextEvent() throws Exception {
        test("WebGLContextEvent", "WebGLContextEvent");
    }

    @Alerts("true/false")
    void _WebGLFramebuffer_WebGLFramebuffer() throws Exception {
        test("WebGLFramebuffer", "WebGLFramebuffer");
    }

    @Alerts("true/false")
    void _WebGLProgram_WebGLProgram() throws Exception {
        test("WebGLProgram", "WebGLProgram");
    }

    @Alerts("true/false")
    void _WebGLQuery_WebGLQuery() throws Exception {
        test("WebGLQuery", "WebGLQuery");
    }

    @Alerts("true/false")
    void _WebGLRenderbuffer_WebGLRenderbuffer() throws Exception {
        test("WebGLRenderbuffer", "WebGLRenderbuffer");
    }

    @Alerts("true/false")
    void _WebGLRenderingContext_WebGLRenderingContext() throws Exception {
        test("WebGLRenderingContext", "WebGLRenderingContext");
    }

    @Alerts("true/false")
    void _WebGLSampler_WebGLSampler() throws Exception {
        test("WebGLSampler", "WebGLSampler");
    }

    @Alerts("true/false")
    void _WebGLShader_WebGLShader() throws Exception {
        test("WebGLShader", "WebGLShader");
    }

    @Alerts("true/false")
    void _WebGLShaderPrecisionFormat_WebGLShaderPrecisionFormat() throws Exception {
        test("WebGLShaderPrecisionFormat", "WebGLShaderPrecisionFormat");
    }

    @Alerts("true/false")
    void _WebGLSync_WebGLSync() throws Exception {
        test("WebGLSync", "WebGLSync");
    }

    @Alerts("true/false")
    void _WebGLTexture_WebGLTexture() throws Exception {
        test("WebGLTexture", "WebGLTexture");
    }

    @Alerts("true/false")
    void _WebGLTransformFeedback_WebGLTransformFeedback() throws Exception {
        test("WebGLTransformFeedback", "WebGLTransformFeedback");
    }

    @Alerts("true/false")
    void _WebGLUniformLocation_WebGLUniformLocation() throws Exception {
        test("WebGLUniformLocation", "WebGLUniformLocation");
    }

    @Alerts("true/false")
    void _WebGLVertexArrayObject_WebGLVertexArrayObject() throws Exception {
        test("WebGLVertexArrayObject", "WebGLVertexArrayObject");
    }

    @Alerts("true/false")
    void _WebKitCSSMatrix_DOMMatrix() throws Exception {
        test("WebKitCSSMatrix", "DOMMatrix");
    }

    @Alerts("true/false")
    void _WebKitCSSMatrix_WebKitCSSMatrix() throws Exception {
        test("WebKitCSSMatrix", "WebKitCSSMatrix");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _webkitMediaStream_MediaStream() throws Exception {
        test("webkitMediaStream", "MediaStream");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _webkitMediaStream_webkitMediaStream() throws Exception {
        test("webkitMediaStream", "webkitMediaStream");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _WebKitMutationObserver_MutationObserver() throws Exception {
        test("WebKitMutationObserver", "MutationObserver");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _WebKitMutationObserver_WebKitMutationObserver() throws Exception {
        test("WebKitMutationObserver", "WebKitMutationObserver");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _webkitRTCPeerConnection_RTCPeerConnection() throws Exception {
        test("webkitRTCPeerConnection", "RTCPeerConnection");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _webkitRTCPeerConnection_webkitRTCPeerConnection() throws Exception {
        test("webkitRTCPeerConnection", "webkitRTCPeerConnection");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _webkitSpeechGrammar_webkitSpeechGrammar() throws Exception {
        test("webkitSpeechGrammar", "webkitSpeechGrammar");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _webkitSpeechGrammarList_webkitSpeechGrammarList() throws Exception {
        test("webkitSpeechGrammarList", "webkitSpeechGrammarList");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _webkitSpeechRecognition_webkitSpeechRecognition() throws Exception {
        test("webkitSpeechRecognition", "webkitSpeechRecognition");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _webkitSpeechRecognitionError_webkitSpeechRecognitionError() throws Exception {
        test("webkitSpeechRecognitionError", "webkitSpeechRecognitionError");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _webkitSpeechRecognitionEvent_webkitSpeechRecognitionEvent() throws Exception {
        test("webkitSpeechRecognitionEvent", "webkitSpeechRecognitionEvent");
    }

    @Alerts("true/false")
    void _webkitURL_URL() throws Exception {
        test("webkitURL", "URL");
    }

    @Alerts("true/false")
    void _webkitURL_webkitURL() throws Exception {
        test("webkitURL", "webkitURL");
    }

    @Alerts("true/false")
    void _WebSocket_WebSocket() throws Exception {
        test("WebSocket", "WebSocket");
    }

    @Alerts("true/false")
    void _WheelEvent_WheelEvent() throws Exception {
        test("WheelEvent", "WheelEvent");
    }

    @Alerts("true/false")
    void _Window_Window() throws Exception {
        test("Window", "Window");
    }

    @Alerts("true/false")
    void _Worker_Worker() throws Exception {
        test("Worker", "Worker");
    }

    @Alerts("true/false")
    void _XMLDocument_XMLDocument() throws Exception {
        test("XMLDocument", "XMLDocument");
    }

    @Alerts("true/false")
    void _XMLHttpRequest_XMLHttpRequest() throws Exception {
        test("XMLHttpRequest", "XMLHttpRequest");
    }

    @Alerts("true/true")
    void _XMLHttpRequestEventTarget_XMLHttpRequest() throws Exception {
        test("XMLHttpRequestEventTarget", "XMLHttpRequest");
    }

    @Alerts("true/false")
    void _XMLHttpRequestEventTarget_XMLHttpRequestEventTarget() throws Exception {
        test("XMLHttpRequestEventTarget", "XMLHttpRequestEventTarget");
    }

    @Alerts("true/true")
    void _XMLHttpRequestEventTarget_XMLHttpRequestUpload() throws Exception {
        test("XMLHttpRequestEventTarget", "XMLHttpRequestUpload");
    }

    @Alerts("true/false")
    void _XMLHttpRequestUpload_XMLHttpRequestUpload() throws Exception {
        test("XMLHttpRequestUpload", "XMLHttpRequestUpload");
    }

    @Alerts("true/false")
    void _XMLSerializer_XMLSerializer() throws Exception {
        test("XMLSerializer", "XMLSerializer");
    }

    @Alerts("true/false")
    void _XPathEvaluator_XPathEvaluator() throws Exception {
        test("XPathEvaluator", "XPathEvaluator");
    }

    @Alerts("true/false")
    void _XPathExpression_XPathExpression() throws Exception {
        test("XPathExpression", "XPathExpression");
    }

    @Alerts("true/false")
    void _XPathResult_XPathResult() throws Exception {
        test("XPathResult", "XPathResult");
    }

    @Alerts("true/false")
    void _XSLTProcessor_XSLTProcessor() throws Exception {
        test("XSLTProcessor", "XSLTProcessor");
    }
}
