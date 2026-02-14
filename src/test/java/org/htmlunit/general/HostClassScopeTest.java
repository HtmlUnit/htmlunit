/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.htmlunit.TestCaseTest;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests the scope setup for JavaScript host objects.
 *
 * For each host class this checks whether the constructor/object
 * is properly visible in the window scope by testing:
 * <ul>
 *   <li>{@code 'ClassName' in window} - whether the name is a property of the window object</li>
 *   <li>{@code typeof window.ClassName} - the type when accessed via the window object</li>
 *   <li>{@code typeof ClassName} - the type when accessed as a bare name (scope chain lookup)</li>
 * </ul>
 *
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API">Web API Interfaces</a>
 */
public class HostClassScopeTest extends WebDriverTestCase {

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    public static Collection<Arguments> data() throws Exception {
        final List<Arguments> list = new ArrayList<>();
        final Set<String> classNames = TestCaseTest.getAllConfiguredJsClassNames();
        final ArrayList<String> classNamesSorted = new ArrayList<>(classNames);
        Collections.sort(classNamesSorted);

        for (final String className : classNamesSorted) {
            list.add(Arguments.of(className));
        }
        return list;
    }

    /**
     * The default test.
     * @throws Exception if an error occurs
     */
    @ParameterizedTest(name = "_{0}", quoteTextArguments = false)
    @MethodSource("data")
    @Alerts({"true", "function", "function"})
    void test(final String className) throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TEXTAREA_FUNCTION
                + "  function test() {\n"
                + "    try {\n"
                + "      log('" + className + "' in window);\n"
                + "    } catch(e) { logEx(e) }\n"
                + "    try {\n"
                + "      log(typeof window." + className + ");\n"
                + "    } catch(e) { logEx(e) }\n"
                + "    try {\n"
                + "      log(typeof " + className + ");\n"
                + "    } catch(e) { logEx(e) }\n"
                + "  }\n"
                + "</script></head>\n"
                + "<body onload='test()'>\n"
                + LOG_TEXTAREA
                + "</body></html>";

        loadPageVerifyTextArea2(html);
    }

    @Alerts({"true", "object", "object"})
    void _Atomics() throws Exception {
        test("Atomics");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _BatteryManager() throws Exception {
        test("BatteryManager");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _BeforeInstallPromptEvent() throws Exception {
        test("BeforeInstallPromptEvent");
    }

    @Alerts({"true", "object", "object"})
    void _CSS() throws Exception {
        test("CSS");
    }

    @Alerts(DEFAULT = {"false", "undefined", "undefined"},
            FF_ESR = {"true", "function", "function"})
    void _CSS2Properties() throws Exception {
        test("CSS2Properties");
    }

    @Alerts(DEFAULT = {"false", "undefined", "undefined"},
            FF = {"true", "function", "function"})
    void _CSSStyleProperties() throws Exception {
        test("CSSStyleProperties");
    }

    @Alerts(DEFAULT = {"false", "undefined", "undefined"},
            FF= {"true", "function", "function"},
            FF_ESR = {"true", "function", "function"})
    void _CanvasCaptureMediaStream() throws Exception {
        test("CanvasCaptureMediaStream");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _CanvasCaptureMediaStreamTrack() throws Exception {
        test("CanvasCaptureMediaStream");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _DOMError() throws Exception {
        test("DOMError");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _External() throws Exception {
        test("External");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _FederatedCredential() throws Exception {
        test("FederatedCredential");
    }

    @Alerts(DEFAULT = {"false", "undefined", "undefined"},
            FF = {"true", "function", "function"},
            FF_ESR = {"true", "function", "function"})
    void _FileSystem() throws Exception {
        test("FileSystem");
    }

    @Alerts(DEFAULT = {"false", "undefined", "undefined"},
            FF = {"true", "function", "function"},
            FF_ESR = {"true", "function", "function"})
    void _FileSystemDirectoryEntry() throws Exception {
        test("FileSystemDirectoryEntry");
    }

    @Alerts(DEFAULT = {"false", "undefined", "undefined"},
            FF = {"true", "function", "function"},
            FF_ESR = {"true", "function", "function"})
    void _FileSystemDirectoryReader() throws Exception {
        test("FileSystemDirectoryReader");
    }

    @Alerts(DEFAULT = {"false", "undefined", "undefined"},
            FF = {"true", "function", "function"},
            FF_ESR = {"true", "function", "function"})
    void _FileSystemEntry() throws Exception {
        test("FileSystemEntry");
    }

    @Alerts(DEFAULT = {"false", "undefined", "undefined"},
            FF = {"true", "function", "function"},
            FF_ESR = {"true", "function", "function"})
    void _FileSystemFileEntry() throws Exception {
        test("FileSystemFileEntry");
    }

    @Alerts(DEFAULT = {"false", "undefined", "undefined"},
            FF = {"true", "function", "function"},
            FF_ESR = {"true", "function", "function"})
    void _FontFaceSet() throws Exception {
        test("FontFaceSet");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _InputDeviceCapabilities() throws Exception {
        test("InputDeviceCapabilities");
    }

    @Alerts(DEFAULT = {"false", "undefined", "undefined"},
            FF = {"true", "function", "function"},
            FF_ESR = {"true", "function", "function"})
    void _MediaKeyError() throws Exception {
        test("MediaKeyError");
    }

    @Alerts(DEFAULT = {"false", "undefined", "undefined"},
            FF = {"true", "function", "function"},
            FF_ESR = {"true", "function", "function"})
    void _MouseScrollEvent() throws Exception {
        test("MouseScrollEvent");
    }

    @Alerts({"false", "undefined", "undefined"})
    void _MutationEvent() throws Exception {
        test("MutationEvent");
    }

    @Alerts({"false", "undefined", "undefined"})
    void _NativeXPathNSResolver() throws Exception {
        test("NativeXPathNSResolver");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _NetworkInformation() throws Exception {
        test("NetworkInformation");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _PasswordCredential() throws Exception {
        test("PasswordCredential");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _PaymentAddress() throws Exception {
        test("PaymentAddress");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _PaymentRequest() throws Exception {
        test("PaymentRequest");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _PaymentResponse() throws Exception {
        test("PaymentResponse");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _PeriodicSyncManager() throws Exception {
        test("PeriodicSyncManager");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _Presentation() throws Exception {
        test("Presentation");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _PresentationAvailability() throws Exception {
        test("PresentationAvailability");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _PresentationConnection() throws Exception {
        test("PresentationConnection");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _PresentationConnectionAvailableEvent() throws Exception {
        test("PresentationConnectionAvailableEvent");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _PresentationConnectionCloseEvent() throws Exception {
        test("PresentationConnectionCloseEvent");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _PresentationRequest() throws Exception {
        test("PresentationRequest");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _RemotePlayback() throws Exception {
        test("RemotePlayback");
    }

    @Alerts({"false", "undefined", "undefined"})
    void _StyleMedia() throws Exception {
        test("StyleMedia");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _SyncManager() throws Exception {
        test("SyncManager");
    }

    @Alerts(DEFAULT = {"false", "undefined", "undefined"},
            FF = {"true", "function", "function"},
            FF_ESR = {"true", "function", "function"})
    void _TimeEvent() throws Exception {
        test("TimeEvent");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _Touch() throws Exception {
        test("Touch");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _TouchEvent() throws Exception {
        test("TouchEvent");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _TouchList() throws Exception {
        test("TouchList");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _webkitSpeechGrammar() throws Exception {
        test("webkitSpeechGrammar");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _webkitSpeechGrammarList() throws Exception {
        test("WebkitSpeechGrammarList");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _webkitSpeechRecognition() throws Exception {
        test("webkitSpeechRecognition");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _webkitSpeechRecognitionError() throws Exception {
        test("webkitSpeechRecognitionError");
    }

    @Alerts(DEFAULT = {"true", "function", "function"},
            FF = {"false", "undefined", "undefined"},
            FF_ESR = {"false", "undefined", "undefined"})
    void _webkitSpeechRecognitionEvent() throws Exception {
        test("webkitSpeechRecognitionEvent");
    }
}
