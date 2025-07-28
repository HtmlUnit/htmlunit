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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.htmlunit.TestCaseTest;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;

/**
 * Tests the ctor prototype.
 *
 * @author Ronald Brill
 */
public class CtorPrototypeTest extends WebDriverTestCase {

    private static int ServerRestartCount_ = 0;

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    public static Collection<Arguments> data() throws Exception {
        final List<String> jsClassNames = new ArrayList<>();
        jsClassNames.addAll(TestCaseTest.getAllConfiguredJsConstructorNames());
        Collections.sort(jsClassNames);

        final List<Arguments> list = new ArrayList<>(jsClassNames.size() * jsClassNames.size() / 10);
        for (final String jsClassName : jsClassNames) {
            list.add(Arguments.of(jsClassName));
        }
        return list;
    }

    /**
     * The default test.
     * @throws Exception if an error occurs
     */
    @ParameterizedTest(name = "_{0}")
    @MethodSource("data")
    @Alerts("true")
    void ctor(final String jsClassName) throws Exception {
        test(jsClassName);
    }

    /**
     * Runs the test.
     *
     * @param jsClassName the host name
     * @throws Exception if an error occurs
     */
    protected void test(final String jsClassName) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<title>-</title>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  function check(c) {\n"
            + "    return (Object.getPrototypeOf(c) === Function.prototype);\n"
            + "  }\n"
            + "  try {\n"
            + "    document.title = check(" + jsClassName + ");\n"
            + "  } catch(e) { document.title = 'exception'; }\n"
            + "</script>\n"
            + "</body></html>";

        ServerRestartCount_++;
        if (ServerRestartCount_ == 200) {
            stopWebServers();
            ServerRestartCount_ = 0;
        }

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isWebClientCached() {
        return true;
    }

    @Alerts("false")
    void _AbortSignal() throws Exception {
        test("AbortSignal");
    }

    @Alerts("exception")
    void _AbstractList() throws Exception {
        test("AbstractList");
    }

    @Alerts("false")
    void _AnalyserNode() throws Exception {
        test("AnalyserNode");
    }

    @Alerts("false")
    void _Animation() throws Exception {
        test("Animation");
    }

    @Alerts("false")
    void _AnimationEvent() throws Exception {
        test("AnimationEvent");
    }

    @Alerts("false")
    void _Atomics() throws Exception {
        test("Atomics");
    }

    @Alerts("false")
    void _Attr() throws Exception {
        test("Attr");
    }

    @Alerts("true")
    @HtmlUnitNYI(CHROME = "false",
            EDGE = "false",
            FF = "false",
            FF_ESR = "false")
    void _Audio() throws Exception {
        test("Audio");
    }

    @Alerts("false")
    void _AudioBufferSourceNode() throws Exception {
        test("AudioBufferSourceNode");
    }

    @Alerts("false")
    void _AudioContext() throws Exception {
        test("AudioContext");
    }

    @Alerts("false")
    void _AudioDestinationNode() throws Exception {
        test("AudioDestinationNode");
    }

    @Alerts("false")
    void _AudioNode() throws Exception {
        test("AudioNode");
    }

    @Alerts("false")
    void _AudioProcessingEvent() throws Exception {
        test("AudioProcessingEvent");
    }

    @Alerts("false")
    void _AudioScheduledSourceNode() throws Exception {
        test("AudioScheduledSourceNode");
    }

    @Alerts("false")
    void _BaseAudioContext() throws Exception {
        test("BaseAudioContext");
    }

    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    void _BatteryManager() throws Exception {
        test("BatteryManager");
    }

    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    void _BeforeInstallPromptEvent() throws Exception {
        test("BeforeInstallPromptEvent");
    }

    @Alerts("false")
    void _BeforeUnloadEvent() throws Exception {
        test("BeforeUnloadEvent");
    }

    @Alerts("false")
    void _BiquadFilterNode() throws Exception {
        test("BiquadFilterNode");
    }

    @Alerts("false")
    void _BlobEvent() throws Exception {
        test("BlobEvent");
    }

    @Alerts("false")
    void _BroadcastChannel() throws Exception {
        test("BroadcastChannel");
    }

    @Alerts("false")
    void _CDATASection() throws Exception {
        test("CDATASection");
    }

    @Alerts("false")
    void _CSS() throws Exception {
        test("CSS");
    }

    @Alerts(DEFAULT = "exception",
            FF = "false",
            FF_ESR = "false")
    void _CSS2Properties() throws Exception {
        test("CSS2Properties");
    }

    @Alerts("false")
    void _CSSConditionRule() throws Exception {
        test("CSSConditionRule");
    }

    @Alerts("false")
    void _CSSCounterStyleRule() throws Exception {
        test("CSSCounterStyleRule");
    }

    @Alerts("false")
    void _CSSFontFaceRule() throws Exception {
        test("CSSFontFaceRule");
    }

    @Alerts("false")
    void _CSSGroupingRule() throws Exception {
        test("CSSGroupingRule");
    }

    @Alerts("false")
    void _CSSImportRule() throws Exception {
        test("CSSImportRule");
    }

    @Alerts("false")
    void _CSSKeyframeRule() throws Exception {
        test("CSSKeyframeRule");
    }

    @Alerts("false")
    void _CSSKeyframesRule() throws Exception {
        test("CSSKeyframesRule");
    }

    @Alerts("false")
    void _CSSMediaRule() throws Exception {
        test("CSSMediaRule");
    }

    @Alerts("false")
    void _CSSNamespaceRule() throws Exception {
        test("CSSNamespaceRule");
    }

    @Alerts("false")
    void _CSSPageRule() throws Exception {
        test("CSSPageRule");
    }

    @Alerts("false")
    void _CSSStyleRule() throws Exception {
        test("CSSStyleRule");
    }

    @Alerts("false")
    void _CSSStyleSheet() throws Exception {
        test("CSSStyleSheet");
    }

    @Alerts("false")
    void _CSSSupportsRule() throws Exception {
        test("CSSSupportsRule");
    }

    @Alerts(DEFAULT = "exception",
            FF = "false",
            FF_ESR = "false")
    void _CanvasCaptureMediaStream() throws Exception {
        test("CanvasCaptureMediaStream");
    }

    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    void _CanvasCaptureMediaStreamTrack() throws Exception {
        test("CanvasCaptureMediaStreamTrack");
    }

    @Alerts("false")
    void _ChannelMergerNode() throws Exception {
        test("ChannelMergerNode");
    }

    @Alerts("false")
    void _ChannelSplitterNode() throws Exception {
        test("ChannelSplitterNode");
    }

    @Alerts("false")
    void _CharacterData() throws Exception {
        test("CharacterData");
    }

    @Alerts("false")
    void _ClipboardEvent() throws Exception {
        test("ClipboardEvent");
    }

    @Alerts("false")
    void _CloseEvent() throws Exception {
        test("CloseEvent");
    }

    @Alerts("false")
    void _Comment() throws Exception {
        test("Comment");
    }

    @Alerts("false")
    void _CompositionEvent() throws Exception {
        test("CompositionEvent");
    }

    @Alerts("false")
    void _ConstantSourceNode() throws Exception {
        test("ConstantSourceNode");
    }

    @Alerts("false")
    void _ConvolverNode() throws Exception {
        test("ConvolverNode");
    }

    @Alerts("false")
    void _CustomEvent() throws Exception {
        test("CustomEvent");
    }

    @Alerts(DEFAULT = "true",
            FF = "exception",
            FF_ESR = "exception")
    void _DOMError() throws Exception {
        test("DOMError");
    }

    @Alerts("false")
    void _DOMMatrix() throws Exception {
        test("DOMMatrix");
    }

    @Alerts("false")
    void _DOMPoint() throws Exception {
        test("DOMPoint");
    }

    @Alerts("false")
    void _DOMRect() throws Exception {
        test("DOMRect");
    }

    @Alerts("false")
    void _DelayNode() throws Exception {
        test("DelayNode");
    }

    @Alerts("false")
    void _DeviceMotionEvent() throws Exception {
        test("DeviceMotionEvent");
    }

    @Alerts("false")
    void _DeviceOrientationEvent() throws Exception {
        test("DeviceOrientationEvent");
    }

    @Alerts("false")
    void _Document() throws Exception {
        test("Document");
    }

    @Alerts("false")
    void _DocumentFragment() throws Exception {
        test("DocumentFragment");
    }

    @Alerts("false")
    void _DocumentType() throws Exception {
        test("DocumentType");
    }

    @Alerts("false")
    void _DragEvent() throws Exception {
        test("DragEvent");
    }

    @Alerts("false")
    void _DynamicsCompressorNode() throws Exception {
        test("DynamicsCompressorNode");
    }

    @Alerts("false")
    void _Element() throws Exception {
        test("Element");
    }

    @Alerts("false")
    void _ErrorEvent() throws Exception {
        test("ErrorEvent");
    }

    @Alerts(DEFAULT = "true",
            FF = "exception",
            FF_ESR = "exception")
    void _External() throws Exception {
        test("External");
    }

    @Alerts("false")
    void _EventSource() throws Exception {
        test("EventSource");
    }

    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    void _FederatedCredential() throws Exception {
        test("FederatedCredential");
    }

    @Alerts("false")
    void _File() throws Exception {
        test("File");
    }

    @Alerts("false")
    void _FileReader() throws Exception {
        test("FileReader");
    }

    @Alerts(DEFAULT = "exception",
            FF = "true",
            FF_ESR = "true")
    void _FileSystem() throws Exception {
        test("FileSystem");
    }

    @Alerts(DEFAULT = "exception",
            FF = "false",
            FF_ESR = "false")
    void _FileSystemDirectoryEntry() throws Exception {
        test("FileSystemDirectoryEntry");
    }

    @Alerts(DEFAULT = "exception",
            FF = "true",
            FF_ESR = "true")
    void _FileSystemDirectoryReader() throws Exception {
        test("FileSystemDirectoryReader");
    }

    @Alerts(DEFAULT = "exception",
            FF = "true",
            FF_ESR = "true")
    void _FileSystemEntry() throws Exception {
        test("FileSystemEntry");
    }

    @Alerts(DEFAULT = "exception",
            FF = "false",
            FF_ESR = "false")
    void _FileSystemFileEntry() throws Exception {
        test("FileSystemFileEntry");
    }

    @Alerts("false")
    void _FocusEvent() throws Exception {
        test("FocusEvent");
    }

    @Alerts(DEFAULT = "exception",
            FF = "false",
            FF_ESR = "false")
    void _FontFaceSet() throws Exception {
        test("FontFaceSet");
    }

    @Alerts("false")
    void _GainNode() throws Exception {
        test("GainNode");
    }

    @Alerts("false")
    void _GamepadEvent() throws Exception {
        test("GamepadEvent");
    }

    @Alerts("false")
    void _HTMLAnchorElement() throws Exception {
        test("HTMLAnchorElement");
    }

    @Alerts("false")
    void _HTMLAreaElement() throws Exception {
        test("HTMLAreaElement");
    }

    @Alerts("false")
    void _HTMLAudioElement() throws Exception {
        test("HTMLAudioElement");
    }

    @Alerts("false")
    void _HTMLBRElement() throws Exception {
        test("HTMLBRElement");
    }

    @Alerts("false")
    void _HTMLBaseElement() throws Exception {
        test("HTMLBaseElement");
    }

    @Alerts("false")
    void _HTMLBodyElement() throws Exception {
        test("HTMLBodyElement");
    }

    @Alerts("false")
    void _HTMLButtonElement() throws Exception {
        test("HTMLButtonElement");
    }

    @Alerts("false")
    void _HTMLCanvasElement() throws Exception {
        test("HTMLCanvasElement");
    }

    @Alerts("false")
    void _HTMLDListElement() throws Exception {
        test("HTMLDListElement");
    }

    @Alerts("false")
    void _HTMLDataElement() throws Exception {
        test("HTMLDataElement");
    }

    @Alerts("false")
    void _HTMLDataListElement() throws Exception {
        test("HTMLDataListElement");
    }

    @Alerts("false")
    void _HTMLDetailsElement() throws Exception {
        test("HTMLDetailsElement");
    }

    @Alerts("false")
    void _HTMLDialogElement() throws Exception {
        test("HTMLDialogElement");
    }

    @Alerts("false")
    void _HTMLDirectoryElement() throws Exception {
        test("HTMLDirectoryElement");
    }

    @Alerts("false")
    void _HTMLDivElement() throws Exception {
        test("HTMLDivElement");
    }

    @Alerts("false")
    void _HTMLDocument() throws Exception {
        test("HTMLDocument");
    }

    @Alerts("false")
    void _HTMLElement() throws Exception {
        test("HTMLElement");
    }

    @Alerts("false")
    void _HTMLEmbedElement() throws Exception {
        test("HTMLEmbedElement");
    }

    @Alerts("false")
    void _HTMLFieldSetElement() throws Exception {
        test("HTMLFieldSetElement");
    }

    @Alerts("false")
    void _HTMLFontElement() throws Exception {
        test("HTMLFontElement");
    }

    @Alerts("false")
    void _HTMLFormControlsCollection() throws Exception {
        test("HTMLFormControlsCollection");
    }

    @Alerts("false")
    void _HTMLFormElement() throws Exception {
        test("HTMLFormElement");
    }

    @Alerts("false")
    void _HTMLFrameElement() throws Exception {
        test("HTMLFrameElement");
    }

    @Alerts("false")
    void _HTMLFrameSetElement() throws Exception {
        test("HTMLFrameSetElement");
    }

    @Alerts("false")
    void _HTMLHRElement() throws Exception {
        test("HTMLHRElement");
    }

    @Alerts("false")
    void _HTMLHeadElement() throws Exception {
        test("HTMLHeadElement");
    }

    @Alerts("false")
    void _HTMLHeadingElement() throws Exception {
        test("HTMLHeadingElement");
    }

    @Alerts("false")
    void _HTMLHtmlElement() throws Exception {
        test("HTMLHtmlElement");
    }

    @Alerts("false")
    void _HTMLIFrameElement() throws Exception {
        test("HTMLIFrameElement");
    }

    @Alerts("false")
    void _HTMLImageElement() throws Exception {
        test("HTMLImageElement");
    }

    @Alerts("false")
    void _HTMLInputElement() throws Exception {
        test("HTMLInputElement");
    }

    @Alerts("false")
    void _HTMLLIElement() throws Exception {
        test("HTMLLIElement");
    }

    @Alerts("false")
    void _HTMLLabelElement() throws Exception {
        test("HTMLLabelElement");
    }

    @Alerts("false")
    void _HTMLLegendElement() throws Exception {
        test("HTMLLegendElement");
    }

    @Alerts("false")
    void _HTMLLinkElement() throws Exception {
        test("HTMLLinkElement");
    }

    @Alerts("exception")
    void _HTMLListElement() throws Exception {
        test("HTMLListElement");
    }

    @Alerts("false")
    void _HTMLMapElement() throws Exception {
        test("HTMLMapElement");
    }

    @Alerts("false")
    void _HTMLMarqueeElement() throws Exception {
        test("HTMLMarqueeElement");
    }

    @Alerts("false")
    void _HTMLMediaElement() throws Exception {
        test("HTMLMediaElement");
    }

    @Alerts("false")
    void _HTMLMenuElement() throws Exception {
        test("HTMLMenuElement");
    }

    @Alerts("false")
    void _HTMLMetaElement() throws Exception {
        test("HTMLMetaElement");
    }

    @Alerts("false")
    void _HTMLMeterElement() throws Exception {
        test("HTMLMeterElement");
    }

    @Alerts("false")
    void _HTMLModElement() throws Exception {
        test("HTMLModElement");
    }

    @Alerts("false")
    void _HTMLOListElement() throws Exception {
        test("HTMLOListElement");
    }

    @Alerts("false")
    void _HTMLObjectElement() throws Exception {
        test("HTMLObjectElement");
    }

    @Alerts("false")
    void _HTMLOptGroupElement() throws Exception {
        test("HTMLOptGroupElement");
    }

    @Alerts("false")
    void _HTMLOptionElement() throws Exception {
        test("HTMLOptionElement");
    }

    @Alerts("false")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true",
            FF = "true",
            FF_ESR = "true")
    void _HTMLOptionsCollection() throws Exception {
        test("HTMLOptionsCollection");
    }

    @Alerts("false")
    void _HTMLOutputElement() throws Exception {
        test("HTMLOutputElement");
    }

    @Alerts("false")
    void _HTMLParagraphElement() throws Exception {
        test("HTMLParagraphElement");
    }

    @Alerts("false")
    void _HTMLParamElement() throws Exception {
        test("HTMLParamElement");
    }

    @Alerts("false")
    void _HTMLPictureElement() throws Exception {
        test("HTMLPictureElement");
    }

    @Alerts("false")
    void _HTMLPreElement() throws Exception {
        test("HTMLPreElement");
    }

    @Alerts("false")
    void _HTMLProgressElement() throws Exception {
        test("HTMLProgressElement");
    }

    @Alerts("false")
    void _HTMLQuoteElement() throws Exception {
        test("HTMLQuoteElement");
    }

    @Alerts("false")
    void _HTMLScriptElement() throws Exception {
        test("HTMLScriptElement");
    }

    @Alerts("false")
    void _HTMLSelectElement() throws Exception {
        test("HTMLSelectElement");
    }

    @Alerts("false")
    void _HTMLSlotElement() throws Exception {
        test("HTMLSlotElement");
    }

    @Alerts("false")
    void _HTMLSourceElement() throws Exception {
        test("HTMLSourceElement");
    }

    @Alerts("false")
    void _HTMLSpanElement() throws Exception {
        test("HTMLSpanElement");
    }

    @Alerts("false")
    void _HTMLStyleElement() throws Exception {
        test("HTMLStyleElement");
    }

    @Alerts("false")
    void _HTMLTableCaptionElement() throws Exception {
        test("HTMLTableCaptionElement");
    }

    @Alerts("false")
    void _HTMLTableCellElement() throws Exception {
        test("HTMLTableCellElement");
    }

    @Alerts("false")
    void _HTMLTableColElement() throws Exception {
        test("HTMLTableColElement");
    }

    @Alerts("exception")
    void _HTMLTableComponent() throws Exception {
        test("HTMLTableComponent");
    }

    @Alerts("false")
    void _HTMLTableElement() throws Exception {
        test("HTMLTableElement");
    }

    @Alerts("false")
    void _HTMLTableRowElement() throws Exception {
        test("HTMLTableRowElement");
    }

    @Alerts("false")
    void _HTMLTableSectionElement() throws Exception {
        test("HTMLTableSectionElement");
    }

    @Alerts("false")
    void _HTMLTemplateElement() throws Exception {
        test("HTMLTemplateElement");
    }

    @Alerts("false")
    void _HTMLTextAreaElement() throws Exception {
        test("HTMLTextAreaElement");
    }

    @Alerts("false")
    void _HTMLTimeElement() throws Exception {
        test("HTMLTimeElement");
    }

    @Alerts("false")
    void _HTMLTitleElement() throws Exception {
        test("HTMLTitleElement");
    }

    @Alerts("false")
    void _HTMLTrackElement() throws Exception {
        test("HTMLTrackElement");
    }

    @Alerts("false")
    void _HTMLUListElement() throws Exception {
        test("HTMLUListElement");
    }

    @Alerts("false")
    void _HTMLUnknownElement() throws Exception {
        test("HTMLUnknownElement");
    }

    @Alerts("false")
    void _HTMLVideoElement() throws Exception {
        test("HTMLVideoElement");
    }

    @Alerts("false")
    void _HashChangeEvent() throws Exception {
        test("HashChangeEvent");
    }

    @Alerts("false")
    void _IDBCursorWithValue() throws Exception {
        test("IDBCursorWithValue");
    }

    @Alerts("false")
    void _IDBDatabase() throws Exception {
        test("IDBDatabase");
    }

    @Alerts("false")
    void _IDBOpenDBRequest() throws Exception {
        test("IDBOpenDBRequest");
    }

    @Alerts("false")
    void _IDBRequest() throws Exception {
        test("IDBRequest");
    }

    @Alerts("false")
    void _IDBTransaction() throws Exception {
        test("IDBTransaction");
    }

    @Alerts("false")
    void _IDBVersionChangeEvent() throws Exception {
        test("IDBVersionChangeEvent");
    }

    @Alerts("false")
    void _IIRFilterNode() throws Exception {
        test("IIRFilterNode");
    }

    @Alerts(DEFAULT = "true",
            FF = "exception",
            FF_ESR = "exception")
    void _InputDeviceCapabilities() throws Exception {
        test("InputDeviceCapabilities");
    }

    @Alerts("false")
    void _InputEvent() throws Exception {
        test("InputEvent");
    }

    @Alerts("false")
    void _KeyboardEvent() throws Exception {
        test("KeyboardEvent");
    }

    @Alerts("false")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true",
            FF = "true",
            FF_ESR = "true")
    void _KeyframeEffect() throws Exception {
        test("KeyframeEffect");
    }

    @Alerts("false")
    void _MIDIAccess() throws Exception {
        test("MIDIAccess");
    }

    @Alerts("false")
    void _MIDIConnectionEvent() throws Exception {
        test("MIDIConnectionEvent");
    }

    @Alerts("false")
    void _MIDIInput() throws Exception {
        test("MIDIInput");
    }

    @Alerts("false")
    void _MIDIMessageEvent() throws Exception {
        test("MIDIMessageEvent");
    }

    @Alerts("false")
    void _MIDIOutput() throws Exception {
        test("MIDIOutput");
    }

    @Alerts("false")
    void _MIDIPort() throws Exception {
        test("MIDIPort");
    }

    @Alerts("false")
    void _MediaDevices() throws Exception {
        test("MediaDevices");
    }

    @Alerts("false")
    void _MediaElementAudioSourceNode() throws Exception {
        test("MediaElementAudioSourceNode");
    }

    @Alerts("false")
    void _MediaEncryptedEvent() throws Exception {
        test("MediaEncryptedEvent");
    }

    @Alerts(DEFAULT = "exception",
            FF = "false",
            FF_ESR = "false")
    void _MediaKeyError() throws Exception {
        test("MediaKeyError");
    }

    @Alerts("false")
    void _MediaKeyMessageEvent() throws Exception {
        test("MediaKeyMessageEvent");
    }

    @Alerts("false")
    void _MediaKeySession() throws Exception {
        test("MediaKeySession");
    }

    @Alerts("false")
    void _MediaQueryList() throws Exception {
        test("MediaQueryList");
    }

    @Alerts("false")
    void _MediaQueryListEvent() throws Exception {
        test("MediaQueryListEvent");
    }

    @Alerts("false")
    void _MediaRecorder() throws Exception {
        test("MediaRecorder");
    }

    @Alerts("false")
    void _MediaSource() throws Exception {
        test("MediaSource");
    }

    @Alerts("false")
    void _MediaStream() throws Exception {
        test("MediaStream");
    }

    @Alerts("false")
    void _MediaStreamAudioDestinationNode() throws Exception {
        test("MediaStreamAudioDestinationNode");
    }

    @Alerts("false")
    void _MediaStreamAudioSourceNode() throws Exception {
        test("MediaStreamAudioSourceNode");
    }

    @Alerts("false")
    void _MediaStreamEvent() throws Exception {
        test("MediaStreamEvent");
    }

    @Alerts("false")
    void _MediaStreamTrack() throws Exception {
        test("MediaStreamTrack");
    }

    @Alerts("false")
    void _MediaStreamTrackEvent() throws Exception {
        test("MediaStreamTrackEvent");
    }

    @Alerts("false")
    void _MessageEvent() throws Exception {
        test("MessageEvent");
    }

    @Alerts("false")
    void _MessagePort() throws Exception {
        test("MessagePort");
    }

    @Alerts("false")
    void _MouseEvent() throws Exception {
        test("MouseEvent");
    }

    @Alerts(DEFAULT = "exception",
            FF = "false",
            FF_ESR = "false")
    void _MouseScrollEvent() throws Exception {
        test("MouseScrollEvent");
    }

    @Alerts(DEFAULT = "exception",
            FF_ESR = "false")
    void _MutationEvent() throws Exception {
        test("MutationEvent");
    }

    @Alerts("exception")
    void _NativeXPathNSResolver() throws Exception {
        test("NativeXPathNSResolver");
    }

    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    void _NetworkInformation() throws Exception {
        test("NetworkInformation");
    }

    @Alerts("false")
    void _Node() throws Exception {
        test("Node");
    }

    @Alerts("false")
    void _Notification() throws Exception {
        test("Notification");
    }

    @Alerts("false")
    void _OfflineAudioCompletionEvent() throws Exception {
        test("OfflineAudioCompletionEvent");
    }

    @Alerts("false")
    void _OfflineAudioContext() throws Exception {
        test("OfflineAudioContext");
    }

    @Alerts("false")
    void _OscillatorNode() throws Exception {
        test("OscillatorNode");
    }

    @Alerts("false")
    void _PageTransitionEvent() throws Exception {
        test("PageTransitionEvent");
    }

    @Alerts("false")
    void _PannerNode() throws Exception {
        test("PannerNode");
    }

    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    void _PasswordCredential() throws Exception {
        test("PasswordCredential");
    }

    @Alerts(DEFAULT = "true",
            FF = "exception",
            FF_ESR = "exception")
    void _PaymentAddress() throws Exception {
        test("PaymentAddress");
    }

    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    void _PaymentRequest() throws Exception {
        test("PaymentRequest");
    }

    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    void _PaymentResponse() throws Exception {
        test("PaymentResponse");
    }

    @Alerts("false")
    void _Performance() throws Exception {
        test("Performance");
    }

    @Alerts("false")
    void _PerformanceMark() throws Exception {
        test("PerformanceMark");
    }

    @Alerts("false")
    void _PerformanceMeasure() throws Exception {
        test("PerformanceMeasure");
    }

    @Alerts("false")
    void _PerformanceNavigationTiming() throws Exception {
        test("PerformanceNavigationTiming");
    }

    @Alerts("false")
    void _PerformanceResourceTiming() throws Exception {
        test("PerformanceResourceTiming");
    }

    @Alerts(DEFAULT = "true",
            FF = "exception",
            FF_ESR = "exception")
    void _PeriodicSyncManager() throws Exception {
        test("PeriodicSyncManager");
    }

    @Alerts("false")
    void _PermissionStatus() throws Exception {
        test("PermissionStatus");
    }

    @Alerts("false")
    void _PointerEvent() throws Exception {
        test("PointerEvent");
    }

    @Alerts("false")
    void _PopStateEvent() throws Exception {
        test("PopStateEvent");
    }

    @Alerts(DEFAULT = "true",
            FF = "exception",
            FF_ESR = "exception")
    void _Presentation() throws Exception {
        test("Presentation");
    }

    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    void _PresentationAvailability() throws Exception {
        test("PresentationAvailability");
    }

    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    void _PresentationConnection() throws Exception {
        test("PresentationConnection");
    }

    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    void _PresentationConnectionAvailableEvent() throws Exception {
        test("PresentationConnectionAvailableEvent");
    }

    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    void _PresentationConnectionCloseEvent() throws Exception {
        test("PresentationConnectionCloseEvent");
    }

    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    void _PresentationRequest() throws Exception {
        test("PresentationRequest");
    }

    @Alerts("false")
    void _ProcessingInstruction() throws Exception {
        test("ProcessingInstruction");
    }

    @Alerts("false")
    void _ProgressEvent() throws Exception {
        test("ProgressEvent");
    }

    @Alerts("false")
    void _PromiseRejectionEvent() throws Exception {
        test("PromiseRejectionEvent");
    }

    @Alerts("false")
    void _RTCDataChannelEvent() throws Exception {
        test("RTCDataChannelEvent");
    }

    @Alerts("false")
    void _RTCPeerConnection() throws Exception {
        test("RTCPeerConnection");
    }

    @Alerts("false")
    void _RTCPeerConnectionIceEvent() throws Exception {
        test("RTCPeerConnectionIceEvent");
    }

    @Alerts("false")
    void _RTCSctpTransport() throws Exception {
        test("RTCSctpTransport");
    }

    @Alerts("false")
    void _RadioNodeList() throws Exception {
        test("RadioNodeList");
    }

    @Alerts("false")
    void _Range() throws Exception {
        test("Range");
    }

    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    void _RemotePlayback() throws Exception {
        test("RemotePlayback");
    }

    @Alerts("exception")
    void _RowContainer() throws Exception {
        test("RowContainer");
    }

    @Alerts("false")
    void _SVGAElement() throws Exception {
        test("SVGAElement");
    }

    @Alerts("false")
    void _SVGAnimateElement() throws Exception {
        test("SVGAnimateElement");
    }

    @Alerts("false")
    void _SVGAnimateMotionElement() throws Exception {
        test("SVGAnimateMotionElement");
    }

    @Alerts("false")
    void _SVGAnimateTransformElement() throws Exception {
        test("SVGAnimateTransformElement");
    }

    @Alerts("false")
    void _SVGAnimationElement() throws Exception {
        test("SVGAnimationElement");
    }

    @Alerts("false")
    void _SVGCircleElement() throws Exception {
        test("SVGCircleElement");
    }

    @Alerts("false")
    void _SVGClipPathElement() throws Exception {
        test("SVGClipPathElement");
    }

    @Alerts("false")
    void _SVGComponentTransferFunctionElement() throws Exception {
        test("SVGComponentTransferFunctionElement");
    }

    @Alerts("false")
    void _SVGDefsElement() throws Exception {
        test("SVGDefsElement");
    }

    @Alerts("false")
    void _SVGDescElement() throws Exception {
        test("SVGDescElement");
    }

    @Alerts("exception")
    void _SVGDiscardElement() throws Exception {
        test("SVGDiscardElement");
    }

    @Alerts("false")
    void _SVGElement() throws Exception {
        test("SVGElement");
    }

    @Alerts("false")
    void _SVGEllipseElement() throws Exception {
        test("SVGEllipseElement");
    }

    @Alerts("false")
    void _SVGFEBlendElement() throws Exception {
        test("SVGFEBlendElement");
    }

    @Alerts("false")
    void _SVGFEColorMatrixElement() throws Exception {
        test("SVGFEColorMatrixElement");
    }

    @Alerts("false")
    void _SVGFEComponentTransferElement() throws Exception {
        test("SVGFEComponentTransferElement");
    }

    @Alerts("false")
    void _SVGFECompositeElement() throws Exception {
        test("SVGFECompositeElement");
    }

    @Alerts("false")
    void _SVGFEConvolveMatrixElement() throws Exception {
        test("SVGFEConvolveMatrixElement");
    }

    @Alerts("false")
    void _SVGFEDiffuseLightingElement() throws Exception {
        test("SVGFEDiffuseLightingElement");
    }

    @Alerts("false")
    void _SVGFEDisplacementMapElement() throws Exception {
        test("SVGFEDisplacementMapElement");
    }

    @Alerts("false")
    void _SVGFEDistantLightElement() throws Exception {
        test("SVGFEDistantLightElement");
    }

    @Alerts("false")
    void _SVGFEDropShadowElement() throws Exception {
        test("SVGFEDropShadowElement");
    }

    @Alerts("false")
    void _SVGFEFloodElement() throws Exception {
        test("SVGFEFloodElement");
    }

    @Alerts("false")
    void _SVGFEFuncAElement() throws Exception {
        test("SVGFEFuncAElement");
    }

    @Alerts("false")
    void _SVGFEFuncBElement() throws Exception {
        test("SVGFEFuncBElement");
    }

    @Alerts("false")
    void _SVGFEFuncGElement() throws Exception {
        test("SVGFEFuncGElement");
    }

    @Alerts("false")
    void _SVGFEFuncRElement() throws Exception {
        test("SVGFEFuncRElement");
    }

    @Alerts("false")
    void _SVGFEGaussianBlurElement() throws Exception {
        test("SVGFEGaussianBlurElement");
    }

    @Alerts("false")
    void _SVGFEImageElement() throws Exception {
        test("SVGFEImageElement");
    }

    @Alerts("false")
    void _SVGFEMergeElement() throws Exception {
        test("SVGFEMergeElement");
    }

    @Alerts("false")
    void _SVGFEMergeNodeElement() throws Exception {
        test("SVGFEMergeNodeElement");
    }

    @Alerts("false")
    void _SVGFEMorphologyElement() throws Exception {
        test("SVGFEMorphologyElement");
    }

    @Alerts("false")
    void _SVGFEOffsetElement() throws Exception {
        test("SVGFEOffsetElement");
    }

    @Alerts("false")
    void _SVGFEPointLightElement() throws Exception {
        test("SVGFEPointLightElement");
    }

    @Alerts("false")
    void _SVGFESpecularLightingElement() throws Exception {
        test("SVGFESpecularLightingElement");
    }

    @Alerts("false")
    void _SVGFESpotLightElement() throws Exception {
        test("SVGFESpotLightElement");
    }

    @Alerts("false")
    void _SVGFETileElement() throws Exception {
        test("SVGFETileElement");
    }

    @Alerts("false")
    void _SVGFETurbulenceElement() throws Exception {
        test("SVGFETurbulenceElement");
    }

    @Alerts("false")
    void _SVGFilterElement() throws Exception {
        test("SVGFilterElement");
    }

    @Alerts("false")
    void _SVGForeignObjectElement() throws Exception {
        test("SVGForeignObjectElement");
    }

    @Alerts("false")
    void _SVGGElement() throws Exception {
        test("SVGGElement");
    }

    @Alerts("false")
    void _SVGGeometryElement() throws Exception {
        test("SVGGeometryElement");
    }

    @Alerts("false")
    void _SVGGradientElement() throws Exception {
        test("SVGGradientElement");
    }

    @Alerts("false")
    void _SVGGraphicsElement() throws Exception {
        test("SVGGraphicsElement");
    }

    @Alerts("false")
    void _SVGImageElement() throws Exception {
        test("SVGImageElement");
    }

    @Alerts("false")
    void _SVGLineElement() throws Exception {
        test("SVGLineElement");
    }

    @Alerts("false")
    void _SVGLinearGradientElement() throws Exception {
        test("SVGLinearGradientElement");
    }

    @Alerts("false")
    void _SVGMPathElement() throws Exception {
        test("SVGMPathElement");
    }

    @Alerts("false")
    void _SVGMarkerElement() throws Exception {
        test("SVGMarkerElement");
    }

    @Alerts("false")
    void _SVGMaskElement() throws Exception {
        test("SVGMaskElement");
    }

    @Alerts("false")
    void _SVGMetadataElement() throws Exception {
        test("SVGMetadataElement");
    }

    @Alerts("false")
    void _SVGPathElement() throws Exception {
        test("SVGPathElement");
    }

    @Alerts("false")
    void _SVGPatternElement() throws Exception {
        test("SVGPatternElement");
    }

    @Alerts("false")
    void _SVGPolygonElement() throws Exception {
        test("SVGPolygonElement");
    }

    @Alerts("false")
    void _SVGPolylineElement() throws Exception {
        test("SVGPolylineElement");
    }

    @Alerts("false")
    void _SVGRadialGradientElement() throws Exception {
        test("SVGRadialGradientElement");
    }

    @Alerts("false")
    void _SVGRectElement() throws Exception {
        test("SVGRectElement");
    }

    @Alerts("false")
    void _SVGSVGElement() throws Exception {
        test("SVGSVGElement");
    }

    @Alerts("false")
    void _SVGScriptElement() throws Exception {
        test("SVGScriptElement");
    }

    @Alerts("false")
    void _SVGSetElement() throws Exception {
        test("SVGSetElement");
    }

    @Alerts("false")
    void _SVGStopElement() throws Exception {
        test("SVGStopElement");
    }

    @Alerts("false")
    void _SVGStyleElement() throws Exception {
        test("SVGStyleElement");
    }

    @Alerts("false")
    void _SVGSwitchElement() throws Exception {
        test("SVGSwitchElement");
    }

    @Alerts("false")
    void _SVGSymbolElement() throws Exception {
        test("SVGSymbolElement");
    }

    @Alerts("false")
    void _SVGTSpanElement() throws Exception {
        test("SVGTSpanElement");
    }

    @Alerts("false")
    void _SVGTextContentElement() throws Exception {
        test("SVGTextContentElement");
    }

    @Alerts("false")
    void _SVGTextElement() throws Exception {
        test("SVGTextElement");
    }

    @Alerts("false")
    void _SVGTextPathElement() throws Exception {
        test("SVGTextPathElement");
    }

    @Alerts("false")
    void _SVGTextPositioningElement() throws Exception {
        test("SVGTextPositioningElement");
    }

    @Alerts("false")
    void _SVGTitleElement() throws Exception {
        test("SVGTitleElement");
    }

    @Alerts("false")
    void _SVGUseElement() throws Exception {
        test("SVGUseElement");
    }

    @Alerts("false")
    void _SVGViewElement() throws Exception {
        test("SVGViewElement");
    }

    @Alerts("false")
    void _Screen() throws Exception {
        test("Screen");
    }

    @Alerts("false")
    void _ScreenOrientation() throws Exception {
        test("ScreenOrientation");
    }

    @Alerts("false")
    void _ScriptProcessorNode() throws Exception {
        test("ScriptProcessorNode");
    }

    @Alerts("false")
    void _SecurityPolicyViolationEvent() throws Exception {
        test("SecurityPolicyViolationEvent");
    }

    @Alerts("false")
    void _ServiceWorker() throws Exception {
        test("ServiceWorker");
    }

    @Alerts("false")
    void _ServiceWorkerContainer() throws Exception {
        test("ServiceWorkerContainer");
    }

    @Alerts("false")
    void _ServiceWorkerRegistration() throws Exception {
        test("ServiceWorkerRegistration");
    }

    @Alerts("false")
    void _ShadowRoot() throws Exception {
        test("ShadowRoot");
    }

    @Alerts("false")
    void _SharedWorker() throws Exception {
        test("SharedWorker");
    }

    @Alerts("false")
    void _SourceBuffer() throws Exception {
        test("SourceBuffer");
    }

    @Alerts("false")
    void _SourceBufferList() throws Exception {
        test("SourceBufferList");
    }

    @Alerts("exception")
    void _SpeechGrammar() throws Exception {
        test("SpeechGrammar");
    }

    @Alerts("exception")
    void _SpeechGrammarList() throws Exception {
        test("SpeechGrammarList");
    }

    @Alerts("exception")
    void _SpeechRecognition() throws Exception {
        test("SpeechRecognition");
    }

    @Alerts("exception")
    void _SpeechRecognitionErrorEvent() throws Exception {
        test("SpeechRecognitionErrorEvent");
    }

    @Alerts("exception")
    void _SpeechRecognitionEvent() throws Exception {
        test("SpeechRecognitionEvent");
    }

    @Alerts("false")
    void _SpeechSynthesis() throws Exception {
        test("SpeechSynthesis");
    }

    @Alerts("false")
    void _SpeechSynthesisErrorEvent() throws Exception {
        test("SpeechSynthesisErrorEvent");
    }

    @Alerts("false")
    void _SpeechSynthesisEvent() throws Exception {
        test("SpeechSynthesisEvent");
    }

    @Alerts("false")
    void _SpeechSynthesisUtterance() throws Exception {
        test("SpeechSynthesisUtterance");
    }

    @Alerts("false")
    void _StereoPannerNode() throws Exception {
        test("StereoPannerNode");
    }

    @Alerts("false")
    void _StorageEvent() throws Exception {
        test("StorageEvent");
    }

    @Alerts("exception")
    void _StyleMedia() throws Exception {
        test("StyleMedia");
    }

    @Alerts("false")
    void _SubmitEvent() throws Exception {
        test("SubmitEvent");
    }

    @Alerts(DEFAULT = "true",
            FF = "exception",
            FF_ESR = "exception")
    void _SyncManager() throws Exception {
        test("SyncManager");
    }

    @Alerts("false")
    void _Text() throws Exception {
        test("Text");
    }

    @Alerts(DEFAULT = "false",
            FF_ESR = "exception")
    void _TextEvent() throws Exception {
        test("TextEvent");
    }

    @Alerts("false")
    void _TextTrack() throws Exception {
        test("TextTrack");
    }

    @Alerts("false")
    void _TextTrackCue() throws Exception {
        test("TextTrackCue");
    }

    @Alerts("false")
    void _TextTrackList() throws Exception {
        test("TextTrackList");
    }

    @Alerts(DEFAULT = "exception",
            FF = "false",
            FF_ESR = "false")
    void _TimeEvent() throws Exception {
        test("TimeEvent");
    }

    @Alerts(DEFAULT = "true",
            FF = "exception",
            FF_ESR = "exception")
    void _Touch() throws Exception {
        test("Touch");
    }

    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    void _TouchEvent() throws Exception {
        test("TouchEvent");
    }

    @Alerts(DEFAULT = "true",
            FF = "exception",
            FF_ESR = "exception")
    void _TouchList() throws Exception {
        test("TouchList");
    }

    @Alerts("false")
    void _TrackEvent() throws Exception {
        test("TrackEvent");
    }

    @Alerts("false")
    void _TransitionEvent() throws Exception {
        test("TransitionEvent");
    }

    @Alerts("false")
    void _UIEvent() throws Exception {
        test("UIEvent");
    }

    @Alerts("false")
    void _VTTCue() throws Exception {
        test("VTTCue");
    }

    @Alerts("false")
    void _WaveShaperNode() throws Exception {
        test("WaveShaperNode");
    }

    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true")
    void _WebGLBuffer() throws Exception {
        test("WebGLBuffer");
    }

    @Alerts("false")
    void _WebGLContextEvent() throws Exception {
        test("WebGLContextEvent");
    }

    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true")
    void _WebGLFramebuffer() throws Exception {
        test("WebGLFramebuffer");
    }

    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true")
    void _WebGLProgram() throws Exception {
        test("WebGLProgram");
    }

    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true")
    void _WebGLQuery() throws Exception {
        test("WebGLQuery");
    }

    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true")
    void _WebGLRenderbuffer() throws Exception {
        test("WebGLRenderbuffer");
    }

    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true")
    void _WebGLSampler() throws Exception {
        test("WebGLSampler");
    }

    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true")
    void _WebGLShader() throws Exception {
        test("WebGLShader");
    }

    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true")
    void _WebGLSync() throws Exception {
        test("WebGLSync");
    }

    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true")
    void _WebGLTexture() throws Exception {
        test("WebGLTexture");
    }

    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true")
    void _WebGLTransformFeedback() throws Exception {
        test("WebGLTransformFeedback");
    }

    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true")
    void _WebGLVertexArrayObject() throws Exception {
        test("WebGLVertexArrayObject");
    }

    @Alerts("false")
    void _WebKitCSSMatrix() throws Exception {
        test("WebKitCSSMatrix");
    }

    @Alerts(DEFAULT = "true",
            FF = "exception",
            FF_ESR = "exception")
    void _WebKitMutationObserver() throws Exception {
        test("WebKitMutationObserver");
    }

    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    void _WebKitMediaStream() throws Exception {
        test("webkitMediaStream");
    }

    @Alerts("false")
    void _WebSocket() throws Exception {
        test("WebSocket");
    }

    @Alerts("false")
    void _WheelEvent() throws Exception {
        test("WheelEvent");
    }

    @Alerts("false")
    void _Window() throws Exception {
        test("Window");
    }

    @Alerts("false")
    void _Worker() throws Exception {
        test("Worker");
    }

    @Alerts("false")
    void _XMLDocument() throws Exception {
        test("XMLDocument");
    }

    @Alerts("false")
    void _XMLHttpRequest() throws Exception {
        test("XMLHttpRequest");
    }

    @Alerts("false")
    void _XMLHttpRequestEventTarget() throws Exception {
        test("XMLHttpRequestEventTarget");
    }

    @Alerts("false")
    void _XMLHttpRequestUpload() throws Exception {
        test("XMLHttpRequestUpload");
    }

    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    void _webkitMediaStream() throws Exception {
        test("webkitMediaStream");
    }

    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    void _webkitRTCPeerConnection() throws Exception {
        test("webkitRTCPeerConnection");
    }
}
