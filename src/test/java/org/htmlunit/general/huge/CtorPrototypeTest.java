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
import java.util.List;
import java.util.Set;

import org.htmlunit.TestCaseTest;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserParameterizedRunner;
import org.htmlunit.junit.BrowserParameterizedRunner.Default;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.WebDriver;

/**
 * Tests the prototype.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserParameterizedRunner.class)
public class CtorPrototypeTest extends WebDriverTestCase {

    private static int ServerRestartCount_ = 0;

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    @Parameters
    public static Collection<Object[]> data() throws Exception {
        final Set<String> jsClassNames = TestCaseTest.getAllConfiguredJsConstructorNames();
        final List<Object[]> list = new ArrayList<>(jsClassNames.size() * jsClassNames.size() / 10);
        for (final String jsClassName : jsClassNames) {
            list.add(new Object[] {jsClassName});
        }
        return list;
    }

    /**
     * The parent element name.
     */
    @Parameter
    public String jsClassName_;

    /**
     * The default test.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    @Default
    public void ctor() throws Exception {
        test(jsClassName_);
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
            + "    detector = function() {};\n"
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

    /**
     * Cleanup.
     */
    @After
    public void after() {
        jsClassName_ = null;
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _AbortSignal() throws Exception {
        test("AbortSignal");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void _AbstractList() throws Exception {
        test("AbstractList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _AnalyserNode() throws Exception {
        test("AnalyserNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Animation() throws Exception {
        test("Animation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _AnimationEvent() throws Exception {
        test("AnimationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Atomics() throws Exception {
        test("Atomics");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Attr() throws Exception {
        test("Attr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @HtmlUnitNYI(CHROME = "false",
            EDGE = "false",
            FF = "false",
            FF_ESR = "false")
    public void _Audio() throws Exception {
        test("Audio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _AudioBufferSourceNode() throws Exception {
        test("AudioBufferSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _AudioContext() throws Exception {
        test("AudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _AudioDestinationNode() throws Exception {
        test("AudioDestinationNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _AudioNode() throws Exception {
        test("AudioNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _AudioProcessingEvent() throws Exception {
        test("AudioProcessingEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _AudioScheduledSourceNode() throws Exception {
        test("AudioScheduledSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _BaseAudioContext() throws Exception {
        test("BaseAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    public void _BatteryManager() throws Exception {
        test("BatteryManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    public void _BeforeInstallPromptEvent() throws Exception {
        test("BeforeInstallPromptEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _BeforeUnloadEvent() throws Exception {
        test("BeforeUnloadEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _BiquadFilterNode() throws Exception {
        test("BiquadFilterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _BlobEvent() throws Exception {
        test("BlobEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _BroadcastChannel() throws Exception {
        test("BroadcastChannel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CDATASection() throws Exception {
        test("CDATASection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CSS() throws Exception {
        test("CSS");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "false",
            FF_ESR = "false")
    public void _CSS2Properties() throws Exception {
        test("CSS2Properties");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CSSConditionRule() throws Exception {
        test("CSSConditionRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CSSCounterStyleRule() throws Exception {
        test("CSSCounterStyleRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CSSFontFaceRule() throws Exception {
        test("CSSFontFaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CSSGroupingRule() throws Exception {
        test("CSSGroupingRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CSSImportRule() throws Exception {
        test("CSSImportRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CSSKeyframeRule() throws Exception {
        test("CSSKeyframeRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CSSKeyframesRule() throws Exception {
        test("CSSKeyframesRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CSSMediaRule() throws Exception {
        test("CSSMediaRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CSSNamespaceRule() throws Exception {
        test("CSSNamespaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CSSPageRule() throws Exception {
        test("CSSPageRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CSSStyleRule() throws Exception {
        test("CSSStyleRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CSSStyleSheet() throws Exception {
        test("CSSStyleSheet");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CSSSupportsRule() throws Exception {
        test("CSSSupportsRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "false",
            FF_ESR = "false")
    public void _CanvasCaptureMediaStream() throws Exception {
        test("CanvasCaptureMediaStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    public void _CanvasCaptureMediaStreamTrack() throws Exception {
        test("CanvasCaptureMediaStreamTrack");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ChannelMergerNode() throws Exception {
        test("ChannelMergerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ChannelSplitterNode() throws Exception {
        test("ChannelSplitterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CharacterData() throws Exception {
        test("CharacterData");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ClipboardEvent() throws Exception {
        test("ClipboardEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CloseEvent() throws Exception {
        test("CloseEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Comment() throws Exception {
        test("Comment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CompositionEvent() throws Exception {
        test("CompositionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ConstantSourceNode() throws Exception {
        test("ConstantSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ConvolverNode() throws Exception {
        test("ConvolverNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _CustomEvent() throws Exception {
        test("CustomEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "exception",
            FF_ESR = "exception")
    public void _DOMError() throws Exception {
        test("DOMError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _DOMMatrix() throws Exception {
        test("DOMMatrix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _DOMPoint() throws Exception {
        test("DOMPoint");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _DOMRect() throws Exception {
        test("DOMRect");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _DelayNode() throws Exception {
        test("DelayNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _DeviceMotionEvent() throws Exception {
        test("DeviceMotionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _DeviceOrientationEvent() throws Exception {
        test("DeviceOrientationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Document() throws Exception {
        test("Document");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _DocumentFragment() throws Exception {
        test("DocumentFragment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _DocumentType() throws Exception {
        test("DocumentType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _DragEvent() throws Exception {
        test("DragEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _DynamicsCompressorNode() throws Exception {
        test("DynamicsCompressorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Element() throws Exception {
        test("Element");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ErrorEvent() throws Exception {
        test("ErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "exception",
            FF_ESR = "exception")
    public void _External() throws Exception {
        test("External");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _EventSource() throws Exception {
        test("EventSource");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    public void _FederatedCredential() throws Exception {
        test("FederatedCredential");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _File() throws Exception {
        test("File");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _FileReader() throws Exception {
        test("FileReader");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "true",
            FF_ESR = "true")
    public void _FileSystem() throws Exception {
        test("FileSystem");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "false",
            FF_ESR = "false")
    public void _FileSystemDirectoryEntry() throws Exception {
        test("FileSystemDirectoryEntry");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "true",
            FF_ESR = "true")
    public void _FileSystemDirectoryReader() throws Exception {
        test("FileSystemDirectoryReader");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "true",
            FF_ESR = "true")
    public void _FileSystemEntry() throws Exception {
        test("FileSystemEntry");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "false",
            FF_ESR = "false")
    public void _FileSystemFileEntry() throws Exception {
        test("FileSystemFileEntry");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _FocusEvent() throws Exception {
        test("FocusEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "false",
            FF_ESR = "false")
    public void _FontFaceSet() throws Exception {
        test("FontFaceSet");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _GainNode() throws Exception {
        test("GainNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _GamepadEvent() throws Exception {
        test("GamepadEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLAnchorElement() throws Exception {
        test("HTMLAnchorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLAreaElement() throws Exception {
        test("HTMLAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLAudioElement() throws Exception {
        test("HTMLAudioElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLBRElement() throws Exception {
        test("HTMLBRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLBaseElement() throws Exception {
        test("HTMLBaseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLBodyElement() throws Exception {
        test("HTMLBodyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLButtonElement() throws Exception {
        test("HTMLButtonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLCanvasElement() throws Exception {
        test("HTMLCanvasElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLDListElement() throws Exception {
        test("HTMLDListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLDataElement() throws Exception {
        test("HTMLDataElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLDataListElement() throws Exception {
        test("HTMLDataListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLDetailsElement() throws Exception {
        test("HTMLDetailsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLDialogElement() throws Exception {
        test("HTMLDialogElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLDirectoryElement() throws Exception {
        test("HTMLDirectoryElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLDivElement() throws Exception {
        test("HTMLDivElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLDocument() throws Exception {
        test("HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLElement() throws Exception {
        test("HTMLElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLEmbedElement() throws Exception {
        test("HTMLEmbedElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLFieldSetElement() throws Exception {
        test("HTMLFieldSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLFontElement() throws Exception {
        test("HTMLFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLFormControlsCollection() throws Exception {
        test("HTMLFormControlsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLFormElement() throws Exception {
        test("HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLFrameElement() throws Exception {
        test("HTMLFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLFrameSetElement() throws Exception {
        test("HTMLFrameSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLHRElement() throws Exception {
        test("HTMLHRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLHeadElement() throws Exception {
        test("HTMLHeadElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLHeadingElement() throws Exception {
        test("HTMLHeadingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLHtmlElement() throws Exception {
        test("HTMLHtmlElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLIFrameElement() throws Exception {
        test("HTMLIFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLImageElement() throws Exception {
        test("HTMLImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLInputElement() throws Exception {
        test("HTMLInputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLLIElement() throws Exception {
        test("HTMLLIElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLLabelElement() throws Exception {
        test("HTMLLabelElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLLegendElement() throws Exception {
        test("HTMLLegendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLLinkElement() throws Exception {
        test("HTMLLinkElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void _HTMLListElement() throws Exception {
        test("HTMLListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLMapElement() throws Exception {
        test("HTMLMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLMarqueeElement() throws Exception {
        test("HTMLMarqueeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLMediaElement() throws Exception {
        test("HTMLMediaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLMenuElement() throws Exception {
        test("HTMLMenuElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLMetaElement() throws Exception {
        test("HTMLMetaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLMeterElement() throws Exception {
        test("HTMLMeterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLModElement() throws Exception {
        test("HTMLModElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLOListElement() throws Exception {
        test("HTMLOListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLObjectElement() throws Exception {
        test("HTMLObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLOptGroupElement() throws Exception {
        test("HTMLOptGroupElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLOptionElement() throws Exception {
        test("HTMLOptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true",
            FF = "true",
            FF_ESR = "true")
    public void _HTMLOptionsCollection() throws Exception {
        test("HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLOutputElement() throws Exception {
        test("HTMLOutputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLParagraphElement() throws Exception {
        test("HTMLParagraphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLParamElement() throws Exception {
        test("HTMLParamElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLPictureElement() throws Exception {
        test("HTMLPictureElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLPreElement() throws Exception {
        test("HTMLPreElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLProgressElement() throws Exception {
        test("HTMLProgressElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLQuoteElement() throws Exception {
        test("HTMLQuoteElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLScriptElement() throws Exception {
        test("HTMLScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLSelectElement() throws Exception {
        test("HTMLSelectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLSlotElement() throws Exception {
        test("HTMLSlotElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLSourceElement() throws Exception {
        test("HTMLSourceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLSpanElement() throws Exception {
        test("HTMLSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLStyleElement() throws Exception {
        test("HTMLStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLTableCaptionElement() throws Exception {
        test("HTMLTableCaptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLTableCellElement() throws Exception {
        test("HTMLTableCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLTableColElement() throws Exception {
        test("HTMLTableColElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void _HTMLTableComponent() throws Exception {
        test("HTMLTableComponent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLTableElement() throws Exception {
        test("HTMLTableElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLTableRowElement() throws Exception {
        test("HTMLTableRowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLTableSectionElement() throws Exception {
        test("HTMLTableSectionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLTemplateElement() throws Exception {
        test("HTMLTemplateElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLTextAreaElement() throws Exception {
        test("HTMLTextAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLTimeElement() throws Exception {
        test("HTMLTimeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLTitleElement() throws Exception {
        test("HTMLTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLTrackElement() throws Exception {
        test("HTMLTrackElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLUListElement() throws Exception {
        test("HTMLUListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLUnknownElement() throws Exception {
        test("HTMLUnknownElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HTMLVideoElement() throws Exception {
        test("HTMLVideoElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _HashChangeEvent() throws Exception {
        test("HashChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _IDBCursorWithValue() throws Exception {
        test("IDBCursorWithValue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _IDBDatabase() throws Exception {
        test("IDBDatabase");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _IDBOpenDBRequest() throws Exception {
        test("IDBOpenDBRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _IDBRequest() throws Exception {
        test("IDBRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _IDBTransaction() throws Exception {
        test("IDBTransaction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _IDBVersionChangeEvent() throws Exception {
        test("IDBVersionChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _IIRFilterNode() throws Exception {
        test("IIRFilterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "exception",
            FF_ESR = "exception")
    public void _InputDeviceCapabilities() throws Exception {
        test("InputDeviceCapabilities");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _InputEvent() throws Exception {
        test("InputEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _KeyboardEvent() throws Exception {
        test("KeyboardEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true",
            FF = "true",
            FF_ESR = "true")
    public void _KeyframeEffect() throws Exception {
        test("KeyframeEffect");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _MIDIAccess() throws Exception {
        test("MIDIAccess");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _MIDIConnectionEvent() throws Exception {
        test("MIDIConnectionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _MIDIInput() throws Exception {
        test("MIDIInput");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _MIDIMessageEvent() throws Exception {
        test("MIDIMessageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _MIDIOutput() throws Exception {
        test("MIDIOutput");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _MIDIPort() throws Exception {
        test("MIDIPort");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _MediaDevices() throws Exception {
        test("MediaDevices");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _MediaElementAudioSourceNode() throws Exception {
        test("MediaElementAudioSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _MediaEncryptedEvent() throws Exception {
        test("MediaEncryptedEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "false",
            FF_ESR = "false")
    public void _MediaKeyError() throws Exception {
        test("MediaKeyError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _MediaKeyMessageEvent() throws Exception {
        test("MediaKeyMessageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _MediaKeySession() throws Exception {
        test("MediaKeySession");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _MediaQueryList() throws Exception {
        test("MediaQueryList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _MediaQueryListEvent() throws Exception {
        test("MediaQueryListEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _MediaRecorder() throws Exception {
        test("MediaRecorder");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _MediaSource() throws Exception {
        test("MediaSource");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _MediaStream() throws Exception {
        test("MediaStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _MediaStreamAudioDestinationNode() throws Exception {
        test("MediaStreamAudioDestinationNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _MediaStreamAudioSourceNode() throws Exception {
        test("MediaStreamAudioSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _MediaStreamEvent() throws Exception {
        test("MediaStreamEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _MediaStreamTrack() throws Exception {
        test("MediaStreamTrack");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _MediaStreamTrackEvent() throws Exception {
        test("MediaStreamTrackEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _MessageEvent() throws Exception {
        test("MessageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _MessagePort() throws Exception {
        test("MessagePort");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _MouseEvent() throws Exception {
        test("MouseEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "false",
            FF_ESR = "false")
    public void _MouseScrollEvent() throws Exception {
        test("MouseScrollEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "false",
            FF_ESR = "false")
    public void _MutationEvent() throws Exception {
        test("MutationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void _NativeXPathNSResolver() throws Exception {
        test("NativeXPathNSResolver");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    public void _NetworkInformation() throws Exception {
        test("NetworkInformation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Node() throws Exception {
        test("Node");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Notification() throws Exception {
        test("Notification");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _OfflineAudioCompletionEvent() throws Exception {
        test("OfflineAudioCompletionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _OfflineAudioContext() throws Exception {
        test("OfflineAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _OscillatorNode() throws Exception {
        test("OscillatorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _PageTransitionEvent() throws Exception {
        test("PageTransitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _PannerNode() throws Exception {
        test("PannerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    public void _PasswordCredential() throws Exception {
        test("PasswordCredential");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "exception",
            FF_ESR = "exception")
    public void _PaymentAddress() throws Exception {
        test("PaymentAddress");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    public void _PaymentRequest() throws Exception {
        test("PaymentRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    public void _PaymentResponse() throws Exception {
        test("PaymentResponse");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Performance() throws Exception {
        test("Performance");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _PerformanceMark() throws Exception {
        test("PerformanceMark");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _PerformanceMeasure() throws Exception {
        test("PerformanceMeasure");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _PerformanceNavigationTiming() throws Exception {
        test("PerformanceNavigationTiming");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _PerformanceResourceTiming() throws Exception {
        test("PerformanceResourceTiming");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "exception",
            FF_ESR = "exception")
    public void _PeriodicSyncManager() throws Exception {
        test("PeriodicSyncManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _PermissionStatus() throws Exception {
        test("PermissionStatus");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _PointerEvent() throws Exception {
        test("PointerEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _PopStateEvent() throws Exception {
        test("PopStateEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "exception",
            FF_ESR = "exception")
    public void _Presentation() throws Exception {
        test("Presentation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    public void _PresentationAvailability() throws Exception {
        test("PresentationAvailability");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    public void _PresentationConnection() throws Exception {
        test("PresentationConnection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    public void _PresentationConnectionAvailableEvent() throws Exception {
        test("PresentationConnectionAvailableEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    public void _PresentationConnectionCloseEvent() throws Exception {
        test("PresentationConnectionCloseEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    public void _PresentationRequest() throws Exception {
        test("PresentationRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ProcessingInstruction() throws Exception {
        test("ProcessingInstruction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ProgressEvent() throws Exception {
        test("ProgressEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _PromiseRejectionEvent() throws Exception {
        test("PromiseRejectionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _RTCDataChannelEvent() throws Exception {
        test("RTCDataChannelEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _RTCPeerConnection() throws Exception {
        test("RTCPeerConnection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _RTCPeerConnectionIceEvent() throws Exception {
        test("RTCPeerConnectionIceEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _RTCSctpTransport() throws Exception {
        test("RTCSctpTransport");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _RadioNodeList() throws Exception {
        test("RadioNodeList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Range() throws Exception {
        test("Range");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    public void _RemotePlayback() throws Exception {
        test("RemotePlayback");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void _RowContainer() throws Exception {
        test("RowContainer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGAElement() throws Exception {
        test("SVGAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGAnimateElement() throws Exception {
        test("SVGAnimateElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGAnimateMotionElement() throws Exception {
        test("SVGAnimateMotionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGAnimateTransformElement() throws Exception {
        test("SVGAnimateTransformElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGAnimationElement() throws Exception {
        test("SVGAnimationElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGCircleElement() throws Exception {
        test("SVGCircleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGClipPathElement() throws Exception {
        test("SVGClipPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGComponentTransferFunctionElement() throws Exception {
        test("SVGComponentTransferFunctionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGDefsElement() throws Exception {
        test("SVGDefsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGDescElement() throws Exception {
        test("SVGDescElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    @HtmlUnitNYI(FF = "false")
    public void _SVGDiscardElement() throws Exception {
        test("SVGDiscardElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGElement() throws Exception {
        test("SVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGEllipseElement() throws Exception {
        test("SVGEllipseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFEBlendElement() throws Exception {
        test("SVGFEBlendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFEColorMatrixElement() throws Exception {
        test("SVGFEColorMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFEComponentTransferElement() throws Exception {
        test("SVGFEComponentTransferElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFECompositeElement() throws Exception {
        test("SVGFECompositeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFEConvolveMatrixElement() throws Exception {
        test("SVGFEConvolveMatrixElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFEDiffuseLightingElement() throws Exception {
        test("SVGFEDiffuseLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFEDisplacementMapElement() throws Exception {
        test("SVGFEDisplacementMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFEDistantLightElement() throws Exception {
        test("SVGFEDistantLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFEDropShadowElement() throws Exception {
        test("SVGFEDropShadowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFEFloodElement() throws Exception {
        test("SVGFEFloodElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFEFuncAElement() throws Exception {
        test("SVGFEFuncAElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFEFuncBElement() throws Exception {
        test("SVGFEFuncBElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFEFuncGElement() throws Exception {
        test("SVGFEFuncGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFEFuncRElement() throws Exception {
        test("SVGFEFuncRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFEGaussianBlurElement() throws Exception {
        test("SVGFEGaussianBlurElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFEImageElement() throws Exception {
        test("SVGFEImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFEMergeElement() throws Exception {
        test("SVGFEMergeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFEMergeNodeElement() throws Exception {
        test("SVGFEMergeNodeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFEMorphologyElement() throws Exception {
        test("SVGFEMorphologyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFEOffsetElement() throws Exception {
        test("SVGFEOffsetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFEPointLightElement() throws Exception {
        test("SVGFEPointLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFESpecularLightingElement() throws Exception {
        test("SVGFESpecularLightingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFESpotLightElement() throws Exception {
        test("SVGFESpotLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFETileElement() throws Exception {
        test("SVGFETileElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFETurbulenceElement() throws Exception {
        test("SVGFETurbulenceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGFilterElement() throws Exception {
        test("SVGFilterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGForeignObjectElement() throws Exception {
        test("SVGForeignObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGGElement() throws Exception {
        test("SVGGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGGeometryElement() throws Exception {
        test("SVGGeometryElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGGradientElement() throws Exception {
        test("SVGGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGGraphicsElement() throws Exception {
        test("SVGGraphicsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGImageElement() throws Exception {
        test("SVGImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGLineElement() throws Exception {
        test("SVGLineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGLinearGradientElement() throws Exception {
        test("SVGLinearGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGMPathElement() throws Exception {
        test("SVGMPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGMarkerElement() throws Exception {
        test("SVGMarkerElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGMaskElement() throws Exception {
        test("SVGMaskElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGMetadataElement() throws Exception {
        test("SVGMetadataElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGPathElement() throws Exception {
        test("SVGPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGPatternElement() throws Exception {
        test("SVGPatternElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGPolygonElement() throws Exception {
        test("SVGPolygonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGPolylineElement() throws Exception {
        test("SVGPolylineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGRadialGradientElement() throws Exception {
        test("SVGRadialGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGRectElement() throws Exception {
        test("SVGRectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGSVGElement() throws Exception {
        test("SVGSVGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGScriptElement() throws Exception {
        test("SVGScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGSetElement() throws Exception {
        test("SVGSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGStopElement() throws Exception {
        test("SVGStopElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGStyleElement() throws Exception {
        test("SVGStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGSwitchElement() throws Exception {
        test("SVGSwitchElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGSymbolElement() throws Exception {
        test("SVGSymbolElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGTSpanElement() throws Exception {
        test("SVGTSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGTextContentElement() throws Exception {
        test("SVGTextContentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGTextElement() throws Exception {
        test("SVGTextElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGTextPathElement() throws Exception {
        test("SVGTextPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGTextPositioningElement() throws Exception {
        test("SVGTextPositioningElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGTitleElement() throws Exception {
        test("SVGTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGUseElement() throws Exception {
        test("SVGUseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SVGViewElement() throws Exception {
        test("SVGViewElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Screen() throws Exception {
        test("Screen");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ScreenOrientation() throws Exception {
        test("ScreenOrientation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ScriptProcessorNode() throws Exception {
        test("ScriptProcessorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SecurityPolicyViolationEvent() throws Exception {
        test("SecurityPolicyViolationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ServiceWorker() throws Exception {
        test("ServiceWorker");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ServiceWorkerContainer() throws Exception {
        test("ServiceWorkerContainer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ServiceWorkerRegistration() throws Exception {
        test("ServiceWorkerRegistration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _ShadowRoot() throws Exception {
        test("ShadowRoot");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SharedWorker() throws Exception {
        test("SharedWorker");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SourceBuffer() throws Exception {
        test("SourceBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SourceBufferList() throws Exception {
        test("SourceBufferList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void _SpeechGrammar() throws Exception {
        test("SpeechGrammar");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void _SpeechGrammarList() throws Exception {
        test("SpeechGrammarList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void _SpeechRecognition() throws Exception {
        test("SpeechRecognition");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void _SpeechRecognitionErrorEvent() throws Exception {
        test("SpeechRecognitionErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void _SpeechRecognitionEvent() throws Exception {
        test("SpeechRecognitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SpeechSynthesis() throws Exception {
        test("SpeechSynthesis");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SpeechSynthesisErrorEvent() throws Exception {
        test("SpeechSynthesisErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SpeechSynthesisEvent() throws Exception {
        test("SpeechSynthesisEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SpeechSynthesisUtterance() throws Exception {
        test("SpeechSynthesisUtterance");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _StereoPannerNode() throws Exception {
        test("StereoPannerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _StorageEvent() throws Exception {
        test("StorageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void _StyleMedia() throws Exception {
        test("StyleMedia");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _SubmitEvent() throws Exception {
        test("SubmitEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "exception",
            FF_ESR = "exception")
    public void _SyncManager() throws Exception {
        test("SyncManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Text() throws Exception {
        test("Text");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF_ESR = "exception")
    public void _TextEvent() throws Exception {
        test("TextEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _TextTrack() throws Exception {
        test("TextTrack");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _TextTrackCue() throws Exception {
        test("TextTrackCue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _TextTrackList() throws Exception {
        test("TextTrackList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "false",
            FF_ESR = "false")
    public void _TimeEvent() throws Exception {
        test("TimeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "exception",
            FF_ESR = "exception")
    public void _Touch() throws Exception {
        test("Touch");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    public void _TouchEvent() throws Exception {
        test("TouchEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "exception",
            FF_ESR = "exception")
    public void _TouchList() throws Exception {
        test("TouchList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _TrackEvent() throws Exception {
        test("TrackEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _TransitionEvent() throws Exception {
        test("TransitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _UIEvent() throws Exception {
        test("UIEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _VTTCue() throws Exception {
        test("VTTCue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _WaveShaperNode() throws Exception {
        test("WaveShaperNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true")
    public void _WebGLBuffer() throws Exception {
        test("WebGLBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _WebGLContextEvent() throws Exception {
        test("WebGLContextEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true")
    public void _WebGLFramebuffer() throws Exception {
        test("WebGLFramebuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true")
    public void _WebGLProgram() throws Exception {
        test("WebGLProgram");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true")
    public void _WebGLQuery() throws Exception {
        test("WebGLQuery");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true")
    public void _WebGLRenderbuffer() throws Exception {
        test("WebGLRenderbuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true")
    public void _WebGLSampler() throws Exception {
        test("WebGLSampler");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true")
    public void _WebGLShader() throws Exception {
        test("WebGLShader");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true")
    public void _WebGLSync() throws Exception {
        test("WebGLSync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true")
    public void _WebGLTexture() throws Exception {
        test("WebGLTexture");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true")
    public void _WebGLTransformFeedback() throws Exception {
        test("WebGLTransformFeedback");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true",
            FF_ESR = "true")
    @HtmlUnitNYI(CHROME = "true",
            EDGE = "true")
    public void _WebGLVertexArrayObject() throws Exception {
        test("WebGLVertexArrayObject");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _WebKitCSSMatrix() throws Exception {
        test("WebKitCSSMatrix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "exception",
            FF_ESR = "exception")
    public void _WebKitMutationObserver() throws Exception {
        test("WebKitMutationObserver");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    public void _WebKitMediaStream() throws Exception {
        test("webkitMediaStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _WebSocket() throws Exception {
        test("WebSocket");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _WheelEvent() throws Exception {
        test("WheelEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Window() throws Exception {
        test("Window");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _Worker() throws Exception {
        test("Worker");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _XMLDocument() throws Exception {
        test("XMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _XMLHttpRequest() throws Exception {
        test("XMLHttpRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _XMLHttpRequestEventTarget() throws Exception {
        test("XMLHttpRequestEventTarget");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void _XMLHttpRequestUpload() throws Exception {
        test("XMLHttpRequestUpload");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    public void _webkitMediaStream() throws Exception {
        test("webkitMediaStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "exception",
            FF_ESR = "exception")
    public void _webkitRTCPeerConnection() throws Exception {
        test("webkitRTCPeerConnection");
    }
}
