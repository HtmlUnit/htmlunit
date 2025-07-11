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
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.htmlunit.TestCaseTest;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test the host class constructors.
 *
 * @author Ronald Brill
 */
public class HostConstructorTest extends WebDriverTestCase {

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
    @Alerts("TypeError")
    void test(final String className) throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
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

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isWebClientCached() {
        return true;
    }

    @Alerts("[object AbortController]")
    void _AbortController() throws Exception {
        test("AbortController");
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

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _BatteryManager() throws Exception {
        test("BatteryManager");
    }

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _BeforeInstallPromptEvent() throws Exception {
        test("BeforeInstallPromptEvent");
    }

    @Alerts("[object Blob]")
    void _Blob() throws Exception {
        test("Blob");
    }

    @Alerts(DEFAULT = "ReferenceError",
            FF = "TypeError",
            FF_ESR = "TypeError")
    void _CanvasCaptureMediaStream() throws Exception {
        test("CanvasCaptureMediaStream");
    }

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _CanvasCaptureMediaStreamTrack() throws Exception {
        test("CanvasCaptureMediaStreamTrack");
    }

    @Alerts("[object Comment]")
    void _Comment() throws Exception {
        test("Comment");
    }

    @Alerts(DEFAULT = "ReferenceError",
            FF = "TypeError",
            FF_ESR = "TypeError")
    void _CSS2Properties() throws Exception {
        test("CSS2Properties");
    }

    @Alerts("[object CSSStyleSheet]")
    void _CSSStyleSheet() throws Exception {
        test("CSSStyleSheet");
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

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _DOMError() throws Exception {
        test("DOMError");
    }

    @Alerts("Error")
    void _DOMException() throws Exception {
        test("DOMException");
    }

    @Alerts("matrix(1, 0, 0, 1, 0, 0)")
    void _DOMMatrix() throws Exception {
        test("DOMMatrix");
    }

    @Alerts("matrix(1, 0, 0, 1, 0, 0)")
    void _DOMMatrixReadOnly() throws Exception {
        test("DOMMatrixReadOnly");
    }

    @Alerts("[object DOMParser]")
    void _DOMParser() throws Exception {
        test("DOMParser");
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

    @Alerts("[object EventTarget]")
    void _EventTarget() throws Exception {
        test("EventTarget");
    }

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _External() throws Exception {
        test("External");
    }

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _FederatedCredential() throws Exception {
        test("FederatedCredential");
    }

    @Alerts("[object FileReader]")
    void _FileReader() throws Exception {
        test("FileReader");
    }

    @Alerts(DEFAULT = "ReferenceError",
            FF = "TypeError",
            FF_ESR = "TypeError")
    void _FileSystem() throws Exception {
        test("FileSystem");
    }

    @Alerts(DEFAULT = "ReferenceError",
            FF = "TypeError",
            FF_ESR = "TypeError")
    void _FileSystemDirectoryEntry() throws Exception {
        test("FileSystemDirectoryEntry");
    }

    @Alerts(DEFAULT = "ReferenceError",
            FF = "TypeError",
            FF_ESR = "TypeError")
    void _FileSystemDirectoryReader() throws Exception {
        test("FileSystemDirectoryReader");
    }

    @Alerts(DEFAULT = "ReferenceError",
            FF = "TypeError",
            FF_ESR = "TypeError")
    void _FileSystemEntry() throws Exception {
        test("FileSystemEntry");
    }

    @Alerts(DEFAULT = "ReferenceError",
            FF = "TypeError",
            FF_ESR = "TypeError")
    void _FileSystemFileEntry() throws Exception {
        test("FileSystemFileEntry");
    }

    @Alerts(DEFAULT = "ReferenceError",
            FF = "TypeError",
            FF_ESR = "TypeError")
    void _FontFaceSet() throws Exception {
        test("FontFaceSet");
    }

    @Alerts("[object FormData]")
    void _FormData() throws Exception {
        test("FormData");
    }

    @Alerts("[object Headers]")
    void _Headers() throws Exception {
        test("Headers");
    }

    @Alerts(DEFAULT = "[object InputDeviceCapabilities]",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _InputDeviceCapabilities() throws Exception {
        test("InputDeviceCapabilities");
    }

    @Alerts(DEFAULT = "ReferenceError",
            FF = "TypeError",
            FF_ESR = "TypeError")
    void _MediaKeyError() throws Exception {
        test("MediaKeyError");
    }

    @Alerts("[object MediaSource]")
    void _MediaSource() throws Exception {
        test("MediaSource");
    }

    @Alerts("[object MediaStream]")
    void _MediaStream() throws Exception {
        test("MediaStream");
    }

    @Alerts("[object MessageChannel]")
    void _MessageChannel() throws Exception {
        test("MessageChannel");
    }

    @Alerts(DEFAULT = "ReferenceError",
            FF = "TypeError",
            FF_ESR = "TypeError")
    void _MouseScrollEvent() throws Exception {
        test("MouseScrollEvent");
    }

    @Alerts(DEFAULT = "ReferenceError",
            FF_ESR = "TypeError")
    void _MutationEvent() throws Exception {
        test("MutationEvent");
    }

    @Alerts("ReferenceError")
    void _NativeXPathNSResolver() throws Exception {
        test("NativeXPathNSResolver");
    }

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _NetworkInformation() throws Exception {
        test("NetworkInformation");
    }

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _PasswordCredential() throws Exception {
        test("PasswordCredential");
    }

    @Alerts("[object Path2D]")
    void _Path2D() throws Exception {
        test("Path2D");
    }

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _PaymentAddress() throws Exception {
        test("PaymentAddress");
    }

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _PaymentRequest() throws Exception {
        test("PaymentRequest");
    }

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _PaymentResponse() throws Exception {
        test("PaymentResponse");
    }

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _PeriodicSyncManager() throws Exception {
        test("PeriodicSyncManager");
    }

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _Presentation() throws Exception {
        test("Presentation");
    }

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _PresentationAvailability() throws Exception {
        test("PresentationAvailability");
    }

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _PresentationConnection() throws Exception {
        test("PresentationConnection");
    }

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _PresentationConnectionAvailableEvent() throws Exception {
        test("PresentationConnectionAvailableEvent");
    }

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _PresentationConnectionCloseEvent() throws Exception {
        test("PresentationConnectionCloseEvent");
    }

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _PresentationRequest() throws Exception {
        test("PresentationRequest");
    }

    @Alerts("")
    void _Range() throws Exception {
        test("Range");
    }

    @Alerts("[object ReadableStream]")
    void _ReadableStream() throws Exception {
        test("ReadableStream");
    }

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _RemotePlayback() throws Exception {
        test("RemotePlayback");
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
            FF = "TypeError",
            FF_ESR = "TypeError")
    void _RTCSessionDescription() throws Exception {
        test("RTCSessionDescription");
    }

    @Alerts("[object SpeechSynthesisUtterance]")
    void _SpeechSynthesisUtterance() throws Exception {
        test("SpeechSynthesisUtterance");
    }

    @Alerts("ReferenceError")
    void _StyleMedia() throws Exception {
        test("StyleMedia");
    }

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _SyncManager() throws Exception {
        test("SyncManager");
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

    @Alerts(DEFAULT = "TypeError",
            FF_ESR = "ReferenceError")
    void _TextEvent() throws Exception {
        test("TextEvent");
    }

    @Alerts(DEFAULT = "ReferenceError",
            FF = "TypeError",
            FF_ESR = "TypeError")
    void _TimeEvent() throws Exception {
        test("TimeEvent");
    }

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _Touch() throws Exception {
        test("Touch");
    }

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _TouchEvent() throws Exception {
        test("TouchEvent");
    }

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _TouchList() throws Exception {
        test("TouchList");
    }

    @Alerts("")
    void _URLSearchParams() throws Exception {
        test("URLSearchParams");
    }

    @Alerts(DEFAULT = "[object SpeechGrammar]",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _webkitSpeechGrammar() throws Exception {
        test("webkitSpeechGrammar");
    }

    @Alerts(DEFAULT = "[object SpeechGrammarList]",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _webkitSpeechGrammarList() throws Exception {
        test("webkitSpeechGrammarList");
    }

    @Alerts(DEFAULT = "[object SpeechRecognition]",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _webkitSpeechRecognition() throws Exception {
        test("webkitSpeechRecognition");
    }

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _webkitSpeechRecognitionError() throws Exception {
        test("webkitSpeechRecognitionError");
    }

    @Alerts(DEFAULT = "TypeError",
            FF = "ReferenceError",
            FF_ESR = "ReferenceError")
    void _webkitSpeechRecognitionEvent() throws Exception {
        test("WebkitSpeechRecognitionEvent");
    }

    @Alerts("[object XMLHttpRequest]")
    void _XMLHttpRequest() throws Exception {
        test("XMLHttpRequest");
    }

    @Alerts("[object XMLSerializer]")
    void _XMLSerializer() throws Exception {
        test("XMLSerializer");
    }

    @Alerts("[object XPathEvaluator]")
    void _XPathEvaluator() throws Exception {
        test("XPathEvaluator");
    }

    @Alerts("[object XSLTProcessor]")
    void _XSLTProcessor() throws Exception {
        test("XSLTProcessor");
    }
}
