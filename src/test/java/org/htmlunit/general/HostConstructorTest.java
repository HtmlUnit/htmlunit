/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
import org.htmlunit.junit.BrowserParameterizedRunner;
import org.htmlunit.junit.BrowserParameterizedRunner.Default;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test the host class constructors.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserParameterizedRunner.class)
@Ignore("Work in progress")
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
    @Parameters
    public static Collection<Object[]> data() throws Exception {
        final List<Object[]> list = new ArrayList<>();
        final Set<String> strings = TestCaseTest.getAllClassNames();
        for (final String className : strings) {
            list.add(new Object[] {className});
        }
        return list;
    }

    /**
     * The class name.
     */
    @Parameter
    public String className_;

    /**
     * The default test.
     * @throws Exception if an error occurs
     */
    @Test
    @Default
    public void test() throws Exception {
        setExpectedAlerts(getExpectedString(className_));

        test(className_);
    }

    private void test(final String className) throws Exception {
        final String html = "<html><head></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION

                + "  try {\n"
                + "    log(new " + className + "());\n"
                + "  } catch(e) {log('exception')}\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    private String getExpectedString(final String className) throws Exception {
        if (PASSING.contains(className)) {
            return "[object " + className_ + "]";
        }

        return "exception";
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
    @Alerts("[object Animation]")
    public void _Animation() throws Exception {
        test("Animation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLAudioElement]")
    public void _Audio() throws Exception {
        test("Audio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object AudioContext]")
    public void _AudioContext() throws Exception {
        test("AudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object CSSStyleSheet]")
    public void _CSSStyleSheet() throws Exception {
        test("CSSStyleSheet");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Comment]")
    public void _Comment() throws Exception {
        test("Comment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("matrix(1, 0, 0, 1, 0, 0)")
    public void _DOMMatrix() throws Exception {
        test("DOMMatrix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("matrix(1, 0, 0, 1, 0, 0)")
    public void _DOMMatrixReadOnly() throws Exception {
        test("DOMMatrixReadOnly");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Error")
    public void _DOMException() throws Exception {
        test("DOMException");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object DOMPoint]")
    public void _DOMPoint() throws Exception {
        test("DOMPoint");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object DOMPointReadOnly]")
    public void _DOMPointReadOnly() throws Exception {
        test("DOMPointReadOnly");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object DOMRect]")
    public void _DOMRect() throws Exception {
        test("DOMRect");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object DOMRectReadOnly]")
    public void _DOMRectReadOnly() throws Exception {
        test("DOMRectReadOnly");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object DataTransfer]")
    public void _DataTransfer() throws Exception {
        test("DataTransfer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Document]")
    public void _Document() throws Exception {
        test("Document");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object DocumentFragment]")
    public void _DocumentFragment() throws Exception {
        test("DocumentFragment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void _Enumerator() throws Exception {
        test("Enumerator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object EventTarget]")
    public void _EventTarget() throws Exception {
        test("EventTarget");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Headers]")
    public void _Headers() throws Exception {
        test("Headers");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLImageElement]")
    public void _Image() throws Exception {
        test("Image");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object InputDeviceCapabilities]",
            FF = "exception",
            FF_ESR = "exception")
    public void _InputDeviceCapabilities() throws Exception {
        test("InputDeviceCapabilities");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object MediaStream]")
    public void _MediaStream() throws Exception {
        test("MediaStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "[object mozRTCPeerConnection]",
            FF_ESR = "[object mozRTCPeerConnection]")
    public void _mozRTCPeerConnection() throws Exception {
        test("mozRTCPeerConnection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLOptionElement]")
    public void _Option() throws Exception {
        test("Option");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Path2D]")
    public void _Path2D() throws Exception {
        test("Path2D");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void _Range() throws Exception {
        test("Range");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object ReadableStream]")
    public void _ReadableStream() throws Exception {
        test("ReadableStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Response]")
    public void _Response() throws Exception {
        test("Response");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object RTCPeerConnection]")
    public void _RTCPeerConnection() throws Exception {
        test("RTCPeerConnection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object RTCSessionDescription]",
            FF = "exception",
            FF_ESR = "exception")
    public void _RTCSessionDescription() throws Exception {
        test("RTCSessionDescription");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object SpeechSynthesisUtterance]")
    public void _SpeechSynthesisUtterance() throws Exception {
        test("SpeechSynthesisUtterance");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Text]")
    public void _Text() throws Exception {
        test("Text");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object TextDecoder]")
    public void _TextDecoder() throws Exception {
        test("TextDecoder");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object TextEncoder]")
    public void _TextEncoder() throws Exception {
        test("TextEncoder");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void _URLSearchParams() throws Exception {
        test("URLSearchParams");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void _WebGLContextEvent() throws Exception {
        test("WebGLContextEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("matrix(1, 0, 0, 1, 0, 0)")
    public void _WebKitCSSMatrix() throws Exception {
        test("WebKitCSSMatrix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object MediaStream]",
            FF = "exception",
            FF_ESR = "exception")
    public void _webkitMediaStream() throws Exception {
        test("webkitMediaStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object RTCPeerConnection]",
            FF = "exception",
            FF_ESR = "exception")
    public void _webkitRTCPeerConnection() throws Exception {
        test("webkitRTCPeerConnection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object SpeechGrammar]",
            FF = "exception",
            FF_ESR = "exception")
    public void _webkitSpeechGrammar() throws Exception {
        test("webkitSpeechGrammar");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object SpeechGrammarList]",
            FF = "exception",
            FF_ESR = "exception")
    public void _webkitSpeechGrammarList() throws Exception {
        test("webkitSpeechGrammarList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object SpeechRecognition]",
            FF = "exception",
            FF_ESR = "exception")
    public void _webkitSpeechRecognition() throws Exception {
        test("webkitSpeechRecognition");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object XPathEvaluator]")
    public void _XPathEvaluator() throws Exception {
        test("XPathEvaluator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object XSLTProcessor]")
    public void _XSLTProcessor() throws Exception {
        test("XSLTProcessor");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.abort.AbortController}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object AbortController]")
    public void abortController() throws Exception {
        test("AbortController");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.abort.AbortSignal}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object AbortSignal]")
    public void abortSignal() throws Exception {
        test("AbortSignal");
    }
}
