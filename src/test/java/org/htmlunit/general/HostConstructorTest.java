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
package org.htmlunit.general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.htmlunit.TestCaseTest;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test the host class constructors.
 *
 * @author Ronald Brill
 */
@Disabled("Work in progress")
public class HostConstructorTest extends WebDriverTestCase {

    private static final HashSet<String> PASSING = new HashSet<>(Arrays.asList(
            "Animation",
            "Blob",
            "DOMParser",
            "FileReader",
            "FormData",
            "MediaSource",
            "MessageChannel",
            "XMLHttpRequest",
            "XMLSerializer"
            ));

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    public static Collection<Arguments> data() throws Exception {
        final List<Arguments> list = new ArrayList<>();
        final Set<String> strings = TestCaseTest.getAllConfiguredJsClassNames();
        for (final String className : strings) {
            list.add(Arguments.of(className));
        }
        return list;
    }

    /**
     * The default test.
     * @throws Exception if an error occurs
     */
    @ParameterizedTest(name = "_{0}")
    @MethodSource("data")
    void test(final String className) throws Exception {
        setExpectedAlerts(getExpectedString(className));

        final String html = DOCTYPE_HTML
                + "<html><head></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION

                + "  try {\n"
                + "    log(new " + className + "());\n"
                + "  } catch(e) { logEx(e) }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    private String getExpectedString(final String className) throws Exception {
        if (PASSING.contains(className)) {
            return "[object " + className + "]";
        }

        return "ReferenceError";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isWebClientCached() {
        return true;
    }

    @Alerts("[object Animation]")
    void _Animation() throws Exception {
        test("Animation");
    }

    @Alerts("[object HTMLAudioElement]")
    void _Audio() throws Exception {
        test("Audio");
    }

    @Alerts("[object AudioContext]")
    void _AudioContext() throws Exception {
        test("AudioContext");
    }

    @Alerts("[object CSSStyleSheet]")
    void _CSSStyleSheet() throws Exception {
        test("CSSStyleSheet");
    }

    @Alerts("[object Comment]")
    void _Comment() throws Exception {
        test("Comment");
    }

    @Alerts("matrix(1, 0, 0, 1, 0, 0)")
    void _DOMMatrix() throws Exception {
        test("DOMMatrix");
    }

    @Alerts("matrix(1, 0, 0, 1, 0, 0)")
    void _DOMMatrixReadOnly() throws Exception {
        test("DOMMatrixReadOnly");
    }

    @Alerts("Error")
    void _DOMException() throws Exception {
        test("DOMException");
    }

    @Alerts("[object DOMPoint]")
    void _DOMPoint() throws Exception {
        test("DOMPoint");
    }

    @Alerts("[object DOMPointReadOnly]")
    void _DOMPointReadOnly() throws Exception {
        test("DOMPointReadOnly");
    }

    @Alerts("[object DOMRect]")
    void _DOMRect() throws Exception {
        test("DOMRect");
    }

    @Alerts("[object DOMRectReadOnly]")
    void _DOMRectReadOnly() throws Exception {
        test("DOMRectReadOnly");
    }

    @Alerts("[object DataTransfer]")
    void _DataTransfer() throws Exception {
        test("DataTransfer");
    }

    @Alerts("[object Document]")
    void _Document() throws Exception {
        test("Document");
    }

    @Alerts("[object DocumentFragment]")
    void _DocumentFragment() throws Exception {
        test("DocumentFragment");
    }

    @Alerts("exception")
    void _Enumerator() throws Exception {
        test("Enumerator");
    }

    @Alerts("[object EventTarget]")
    void _EventTarget() throws Exception {
        test("EventTarget");
    }

    @Alerts("[object Headers]")
    void _Headers() throws Exception {
        test("Headers");
    }

    @Alerts("[object HTMLImageElement]")
    void _Image() throws Exception {
        test("Image");
    }

    @Alerts(DEFAULT = "[object InputDeviceCapabilities]",
            FF = "exception",
            FF_ESR = "exception")
    void _InputDeviceCapabilities() throws Exception {
        test("InputDeviceCapabilities");
    }

    @Alerts("[object MediaStream]")
    void _MediaStream() throws Exception {
        test("MediaStream");
    }

    @Alerts(DEFAULT = "exception",
            FF = "[object mozRTCPeerConnection]",
            FF_ESR = "[object mozRTCPeerConnection]")
    void _mozRTCPeerConnection() throws Exception {
        test("mozRTCPeerConnection");
    }

    @Alerts("[object HTMLOptionElement]")
    void _Option() throws Exception {
        test("Option");
    }

    @Alerts("[object Path2D]")
    void _Path2D() throws Exception {
        test("Path2D");
    }

    @Alerts("")
    void _Range() throws Exception {
        test("Range");
    }

    @Alerts("[object ReadableStream]")
    void _ReadableStream() throws Exception {
        test("ReadableStream");
    }

    @Alerts("[object Response]")
    void _Response() throws Exception {
        test("Response");
    }

    @Alerts("[object RTCPeerConnection]")
    void _RTCPeerConnection() throws Exception {
        test("RTCPeerConnection");
    }

    @Alerts(DEFAULT = "[object RTCSessionDescription]",
            FF = "exception",
            FF_ESR = "exception")
    void _RTCSessionDescription() throws Exception {
        test("RTCSessionDescription");
    }

    @Alerts("[object SpeechSynthesisUtterance]")
    void _SpeechSynthesisUtterance() throws Exception {
        test("SpeechSynthesisUtterance");
    }

    @Alerts("[object Text]")
    void _Text() throws Exception {
        test("Text");
    }

    @Alerts("[object TextDecoder]")
    void _TextDecoder() throws Exception {
        test("TextDecoder");
    }

    @Alerts("[object TextEncoder]")
    void _TextEncoder() throws Exception {
        test("TextEncoder");
    }

    @Alerts("")
    void _URLSearchParams() throws Exception {
        test("URLSearchParams");
    }

    @Alerts("exception")
    void _WebGLContextEvent() throws Exception {
        test("WebGLContextEvent");
    }

    @Alerts("matrix(1, 0, 0, 1, 0, 0)")
    void _WebKitCSSMatrix() throws Exception {
        test("WebKitCSSMatrix");
    }

    @Alerts(DEFAULT = "[object MediaStream]",
            FF = "exception",
            FF_ESR = "exception")
    void _webkitMediaStream() throws Exception {
        test("webkitMediaStream");
    }

    @Alerts(DEFAULT = "[object RTCPeerConnection]",
            FF = "exception",
            FF_ESR = "exception")
    void _webkitRTCPeerConnection() throws Exception {
        test("webkitRTCPeerConnection");
    }

    @Alerts(DEFAULT = "[object SpeechGrammar]",
            FF = "exception",
            FF_ESR = "exception")
    void _webkitSpeechGrammar() throws Exception {
        test("webkitSpeechGrammar");
    }

    @Alerts(DEFAULT = "[object SpeechGrammarList]",
            FF = "exception",
            FF_ESR = "exception")
    void _webkitSpeechGrammarList() throws Exception {
        test("webkitSpeechGrammarList");
    }

    @Alerts(DEFAULT = "[object SpeechRecognition]",
            FF = "exception",
            FF_ESR = "exception")
    void _webkitSpeechRecognition() throws Exception {
        test("webkitSpeechRecognition");
    }

    @Alerts("[object XPathEvaluator]")
    void _XPathEvaluator() throws Exception {
        test("XPathEvaluator");
    }

    @Alerts("[object XSLTProcessor]")
    void _XSLTProcessor() throws Exception {
        test("XSLTProcessor");
    }

    @Alerts("[object AbortController]")
    void abortController() throws Exception {
        test("AbortController");
    }

    @Alerts("[object AbortSignal]")
    void abortSignal() throws Exception {
        test("AbortSignal");
    }
}
