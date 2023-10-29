/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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

import org.htmlunit.HttpHeader;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.annotations.StandardsMode;
import org.htmlunit.javascript.host.css.CSSFontFaceRule;
import org.htmlunit.javascript.host.css.CSSImportRule;
import org.htmlunit.javascript.host.css.CSSMediaRule;
import org.htmlunit.javascript.host.css.CSSPageRule;
import org.htmlunit.javascript.host.css.CSSRule;
import org.htmlunit.javascript.host.css.CSSRuleList;
import org.htmlunit.javascript.host.css.CSSStyleDeclaration;
import org.htmlunit.javascript.host.css.CSSStyleRule;
import org.htmlunit.javascript.host.css.CSSStyleSheet;
import org.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import org.htmlunit.javascript.host.css.MediaList;
import org.htmlunit.javascript.host.css.StyleSheetList;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.htmlunit.junit.BrowserRunner.AlertsStandards;
import org.htmlunit.junit.BrowserRunner.HtmlUnitNYI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.css.CSS2Properties;

/**
 * Test the host class names.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API">Web API Interfaces</a>
 */
@RunWith(BrowserRunner.class)
@StandardsMode
public class HostClassNameTest extends WebDriverTestCase {

    private void test(final String className) throws Exception {
        final String html =
            "<html><head><script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var clsName = '' + " + className + ";\n"
            + "      if (clsName.startsWith('function ')) {\n"
            + "        clsName = clsName.replace('\\n    ', ' ');\n"
            + "        clsName = clsName.replace(']\\n}', '] }');\n"
            + "      }\n"
            + "      log(clsName);\n"
            + "    } catch(e) {log('exception')}\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void abstractList() throws Exception {
        test("AbstractList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function AbstractRange() { [native code] }",
            IE = "exception")
    @HtmlUnitNYI(IE = "function AbstractRange() {\n    [native code]\n}\n")
    public void abstractRange() throws Exception {
        test("AbstractRange");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void abstractWorker() throws Exception {
        test("AbstractWorker");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.ActiveXObject}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "function ActiveXObject() {\n    [native code]\n}\n")
    public void activeXObject() throws Exception {
        test("ActiveXObject");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void ambientLightSensor() throws Exception {
        test("AmbientLightSensor");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void ambientLightSensorReading() throws Exception {
        test("AmbientLightSensorReading");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.media.AnalyserNode}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function AnalyserNode() { [native code] }",
            IE = "exception")
    public void analyserNode() throws Exception {
        test("AnalyserNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object ANGLE_instanced_arrays]")
    public void angle_instanced_arrays() throws Exception {
        test("ANGLE_instanced_arrays");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Animation() { [native code] }",
            IE = "exception")
    public void animation() throws Exception {
        test("Animation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void animationEffectReadOnly() throws Exception {
        test("AnimationEffectReadOnly");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void animationEffectTiming() throws Exception {
        test("AnimationEffectTiming");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void animationEffectTimingProperties() throws Exception {
        test("AnimationEffectTimingProperties");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void animationEffectTimingReadOnly() throws Exception {
        test("AnimationEffectTimingReadOnly");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.animations.AnimationEvent}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function AnimationEvent() { [native code] }",
            IE = "[object AnimationEvent]")
    public void animationEvent() throws Exception {
        test("AnimationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function AnimationPlaybackEvent() { [native code] }",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "exception", EDGE = "exception", FF = "exception", FF_ESR = "exception")
    public void animationPlaybackEvent() throws Exception {
        test("AnimationPlaybackEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void animationPlayer() throws Exception {
        test("AnimationPlayer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function AnimationTimeline() { [native code] }",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "exception", EDGE = "exception", FF = "exception", FF_ESR = "exception")
    public void animationTimeline() throws Exception {
        test("AnimationTimeline");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void appBannerPromptResult() throws Exception {
        test("AppBannerPromptResult");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.ApplicationCache}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object ApplicationCache]")
    public void applicationCache() throws Exception {
        test("ApplicationCache");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void applicationCacheErrorEvent() throws Exception {
        test("ApplicationCacheErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void apps_mgmt() throws Exception {
        test("Apps.mgmt");
    }

    /**
     * Test {@code org.htmlunit.corejs.javascript.Arguments}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("[object Arguments]")
    public void arguments() throws Exception {
        test("arguments");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Array() { [native code] }",
            IE = "function Array() {\n    [native code]\n}\n")
    public void array() throws Exception {
        test("Array");
    }

    /**
     * Test ArrayBuffer.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function ArrayBuffer() { [native code] }",
            IE = "function ArrayBuffer() {\n    [native code]\n}\n")
    public void arrayBuffer() throws Exception {
        test("ArrayBuffer");
    }

    /**
     * Test ArrayBufferView.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void arrayBufferView() throws Exception {
        test("ArrayBufferView");
    }

    /**
     * Test ArrayBufferViewBase.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void arrayBufferViewBase() throws Exception {
        test("ArrayBufferViewBase");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void asyncFunction() throws Exception {
        test("AsyncFunction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object Atomics]",
            IE = "exception")
    public void atomics() throws Exception {
        test("Atomics");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.Attr}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Attr() { [native code] }",
            IE = "[object Attr]")
    public void attr() throws Exception {
        test("Attr");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.Audio}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Audio() { [native code] }",
            IE = "function Audio() {\n    [native code]\n}\n")
    public void audio() throws Exception {
        test("Audio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function AudioBuffer() { [native code] }",
            IE = "exception")
    public void audioBuffer() throws Exception {
        test("AudioBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function AudioBufferSourceNode() { [native code] }",
            IE = "exception")
    public void audioBufferSourceNode() throws Exception {
        test("AudioBufferSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void audioChannelManager() throws Exception {
        test("AudioChannelManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function AudioContext() { [native code] }",
            IE = "exception")
    public void audioContext() throws Exception {
        test("AudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function AudioDestinationNode() { [native code] }",
            IE = "exception")
    public void audioDestinationNode() throws Exception {
        test("AudioDestinationNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function AudioListener() { [native code] }",
            IE = "exception")
    public void audioListener() throws Exception {
        test("AudioListener");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function AudioNode() { [native code] }",
            IE = "exception")
    public void audioNode() throws Exception {
        test("AudioNode");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.media.AudioParam}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function AudioParam() { [native code] }",
            IE = "exception")
    public void audioParam() throws Exception {
        test("AudioParam");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function AudioProcessingEvent() { [native code] }",
            IE = "exception")
    public void audioProcessingEvent() throws Exception {
        test("AudioProcessingEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function AudioScheduledSourceNode() { [native code] }",
            IE = "exception")
    public void audioScheduledSourceNode() throws Exception {
        test("AudioScheduledSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void autocompleteErrorEvent() throws Exception {
        test("AutocompleteErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function BarProp() { [native code] }",
            IE = "exception")
    public void barProp() throws Exception {
        test("BarProp");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function BaseAudioContext() { [native code] }",
            IE = "exception")
    public void baseAudioContext() throws Exception {
        test("BaseAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function BatteryManager() { [native code] }",
            EDGE = "function BatteryManager() { [native code] }")
    public void batteryManager() throws Exception {
        test("BatteryManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void beforeInstallPrompt() throws Exception {
        test("BeforeInstallPrompt");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function BeforeInstallPromptEvent() { [native code] }",
            EDGE = "function BeforeInstallPromptEvent() { [native code] }")
    public void beforeInstallPromptEvent() throws Exception {
        test("BeforeInstallPromptEvent");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.BeforeUnloadEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function BeforeUnloadEvent() { [native code] }",
            IE = "[object BeforeUnloadEvent]")
    public void beforeUnloadEvent() throws Exception {
        test("BeforeUnloadEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function BiquadFilterNode() { [native code] }",
            IE = "exception")
    public void biquadFilterNode() throws Exception {
        test("BiquadFilterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Blob() { [native code] }",
            IE = "function Blob() {\n    [native code]\n}\n")
    public void blob() throws Exception {
        test("Blob");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void blobBuilder() throws Exception {
        test("BlobBuilder");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function BlobEvent() { [native code] }",
            IE = "exception")
    public void blobEvent() throws Exception {
        test("BlobEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function Bluetooth() { [native code] }",
            EDGE = "function Bluetooth() { [native code] }")
    @HtmlUnitNYI(CHROME = "exception", EDGE = "exception")
    public void bluetooth() throws Exception {
        test("Bluetooth");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void bluetoothAdapter() throws Exception {
        test("BluetoothAdapter");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void bluetoothAdvertisingData() throws Exception {
        test("BluetoothAdvertisingData");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function BluetoothCharacteristicProperties() { [native code] }",
            EDGE = "function BluetoothCharacteristicProperties() { [native code] }")
    @HtmlUnitNYI(CHROME = "exception", EDGE = "exception")
    public void bluetoothCharacteristicProperties() throws Exception {
        test("BluetoothCharacteristicProperties");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function BluetoothDevice() { [native code] }",
            EDGE = "function BluetoothDevice() { [native code] }")
    @HtmlUnitNYI(CHROME = "exception", EDGE = "exception")
    public void bluetoothDevice() throws Exception {
        test("BluetoothDevice");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void bluetoothDeviceEvent() throws Exception {
        test("BluetoothDeviceEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void bluetoothGATTRemoteServer() throws Exception {
        test("BluetoothGATTRemoteServer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void bluetoothGATTService() throws Exception {
        test("BluetoothGATTService");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void bluetoothManager() throws Exception {
        test("BluetoothManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function BluetoothRemoteGATTCharacteristic() { [native code] }",
            EDGE = "function BluetoothRemoteGATTCharacteristic() { [native code] }")
    @HtmlUnitNYI(CHROME = "exception", EDGE = "exception")
    public void bluetoothRemoteGATTCharacteristic() throws Exception {
        test("BluetoothRemoteGATTCharacteristic");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function BluetoothRemoteGATTServer() { [native code] }",
            EDGE = "function BluetoothRemoteGATTServer() { [native code] }")
    @HtmlUnitNYI(CHROME = "exception", EDGE = "exception")
    public void bluetoothRemoteGATTServer() throws Exception {
        test("BluetoothRemoteGATTServer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void bluetoothStatusChangedEvent() throws Exception {
        test("BluetoothStatusChangedEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void body() throws Exception {
        test("Body");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void boxObject() throws Exception {
        test("BoxObject");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function BroadcastChannel() { [native code] }",
            IE = "exception")
    public void broadcastChannel() throws Exception {
        test("BroadcastChannel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void budgetService() throws Exception {
        test("BudgetService");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void budgetState() throws Exception {
        test("BudgetState");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void bufferSource() throws Exception {
        test("BufferSource");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void byteString() throws Exception {
        test("ByteString");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Cache() { [native code] }",
            IE = "exception")
    public void cache() throws Exception {
        test("Cache");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CacheStorage() { [native code] }",
            IE = "exception")
    public void cacheStorage() throws Exception {
        test("CacheStorage");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void callEvent() throws Exception {
        test("CallEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void cameraCapabilities() throws Exception {
        test("CameraCapabilities");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void cameraControl() throws Exception {
        test("CameraControl");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void cameraManager() throws Exception {
        test("CameraManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function CanvasCaptureMediaStream() { [native code] }",
            FF_ESR = "function CanvasCaptureMediaStream() { [native code] }")
    public void canvasCaptureMediaStream() throws Exception {
        test("CanvasCaptureMediaStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function CanvasCaptureMediaStreamTrack() { [native code] }",
            EDGE = "function CanvasCaptureMediaStreamTrack() { [native code] }")
    public void canvasCaptureMediaStreamTrack() throws Exception {
        test("CanvasCaptureMediaStreamTrack");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CanvasGradient() { [native code] }",
            IE = "[object CanvasGradient]")
    public void canvasGradient() throws Exception {
        test("CanvasGradient");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void canvasImageSource() throws Exception {
        test("CanvasImageSource");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CanvasPattern() { [native code] }",
            IE = "[object CanvasPattern]")
    public void canvasPattern() throws Exception {
        test("CanvasPattern");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.canvas.CanvasRenderingContext2D}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function CanvasRenderingContext2D() { [native code] }",
            IE = "[object CanvasRenderingContext2D]")
    public void canvasRenderingContext2D() throws Exception {
        test("CanvasRenderingContext2D");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function CaretPosition() { [native code] }",
            FF_ESR = "function CaretPosition() { [native code] }")
    public void caretPosition() throws Exception {
        test("CaretPosition");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.CDATASection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function CDATASection() { [native code] }",
            IE = "[object CDATASection]")
    public void cdataSection() throws Exception {
        test("CDATASection");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.media.ChannelMergerNode}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ChannelMergerNode() { [native code] }",
            IE = "exception")
    public void channelMergerNode() throws Exception {
        test("ChannelMergerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ChannelSplitterNode() { [native code] }",
            IE = "exception")
    public void channelSplitterNode() throws Exception {
        test("ChannelSplitterNode");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.CharacterData}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CharacterData() { [native code] }",
            IE = "[object CharacterData]")
    public void characterData() throws Exception {
        test("CharacterData");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.CharacterData}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void characterDataImpl() throws Exception {
        test("CharacterDataImpl");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void childNode() throws Exception {
        test("ChildNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void chromeWorker() throws Exception {
        test("ChromeWorker");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void client() throws Exception {
        test("Client");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.ClientRect}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object ClientRect]")
    public void clientRect() throws Exception {
        test("ClientRect");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.ClientRectList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object ClientRectList]")
    public void clientRectList() throws Exception {
        test("ClientRectList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void clients() throws Exception {
        test("Clients");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void clipboardData() throws Exception {
        test("ClipboardData");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ClipboardEvent() { [native code] }",
            IE = "exception")
    public void clipboardEvent() throws Exception {
        test("ClipboardEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CloseEvent() { [native code] }",
            IE = "[object CloseEvent]")
    public void closeEvent() throws Exception {
        test("CloseEvent");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.Comment}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Comment() { [native code] }",
            IE = "[object Comment]")
    public void comment() throws Exception {
        test("Comment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CompositionEvent() { [native code] }",
            IE = "[object CompositionEvent]")
    public void compositionEvent() throws Exception {
        test("CompositionEvent");
    }

    /**
     * Test {@link ComputedCSSStyleDeclaration}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void computedCSSStyleDeclaration() throws Exception {
        test("ComputedCSSStyleDeclaration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void connection() throws Exception {
        test(HttpHeader.CONNECTION);
    }

    /**
     * Test Console.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object Console]")
    public void console() throws Exception {
        test("Console");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ConstantSourceNode() { [native code] }",
            IE = "exception")
    public void constantSourceNode() throws Exception {
        test("ConstantSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void constrainBoolean() throws Exception {
        test("ConstrainBoolean");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void constrainDOMString() throws Exception {
        test("ConstrainDOMString");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void constrainDouble() throws Exception {
        test("ConstrainDouble");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void constrainLong() throws Exception {
        test("ConstrainLong");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void contactManager() throws Exception {
        test("ContactManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ConvolverNode() { [native code] }",
            IE = "exception")
    public void convolverNode() throws Exception {
        test("ConvolverNode");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.geo.Coordinates}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object Coordinates]")
    public void coordinates() throws Exception {
        test("Coordinates");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Credential() { [native code] }",
            IE = "exception")
    public void credential() throws Exception {
        test("Credential");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CredentialsContainer() { [native code] }",
            IE = "exception")
    public void credentialsContainer() throws Exception {
        test("CredentialsContainer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Crypto() { [native code] }",
            IE = "[object Crypto]")
    public void crypto() throws Exception {
        test("Crypto");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CryptoKey() { [native code] }",
            IE = "exception")
    public void cryptoKey() throws Exception {
        test("CryptoKey");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object CSS]",
            IE = "exception")
    public void css() throws Exception {
        test("CSS");
    }

    /**
     * Test {@link CSS2Properties}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function CSS2Properties() { [native code] }",
            FF_ESR = "function CSS2Properties() { [native code] }")
    public void css2Properties() throws Exception {
        test("CSS2Properties");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void cssCharsetRule() throws Exception {
        test("CSSCharsetRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CSSConditionRule() { [native code] }",
            IE = "exception")
    public void cssConditionRule() throws Exception {
        test("CSSConditionRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CSSCounterStyleRule() { [native code] }",
            IE = "exception")
    public void cssCounterStyleRule() throws Exception {
        test("CSSCounterStyleRule");
    }

    /**
     * Test {@link CSSFontFaceRule}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function CSSFontFaceRule() { [native code] }",
            IE = "[object CSSFontFaceRule]")
    public void cssFontFaceRule() throws Exception {
        test("CSSFontFaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CSSGroupingRule() { [native code] }",
            IE = "exception")
    public void cssGroupingRule() throws Exception {
        test("CSSGroupingRule");
    }

    /**
     * Test {@link CSSImportRule}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function CSSImportRule() { [native code] }",
            IE = "[object CSSImportRule]")
    public void cssImportRule() throws Exception {
        test("CSSImportRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CSSKeyframeRule() { [native code] }",
            IE = "[object CSSKeyframeRule]")
    public void cssKeyframeRule() throws Exception {
        test("CSSKeyframeRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CSSKeyframesRule() { [native code] }",
            IE = "[object CSSKeyframesRule]")
    public void cssKeyframesRule() throws Exception {
        test("CSSKeyframesRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void cssMatrix() throws Exception {
        test("CSSMatrix");
    }

    /**
     * Test {@link CSSMediaRule}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function CSSMediaRule() { [native code] }",
            IE = "[object CSSMediaRule]")
    public void cssMediaRule() throws Exception {
        test("CSSMediaRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CSSNamespaceRule() { [native code] }",
            IE = "[object CSSNamespaceRule]")
    public void cssNamespaceRule() throws Exception {
        test("CSSNamespaceRule");
    }

    /**
     * Test {@link CSSPageRule}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CSSPageRule() { [native code] }",
            IE = "[object CSSPageRule]")
    public void cssPageRule() throws Exception {
        test("CSSPageRule");
    }

    /**
     * Test CSSPrimitiveValue.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void cssPrimitiveValue() throws Exception {
        test("CSSPrimitiveValue");
    }

    /**
     * Test {@link CSSRule}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function CSSRule() { [native code] }",
            IE = "[object CSSRule]")
    public void cssRule() throws Exception {
        test("CSSRule");
    }

    /**
     * Test {@link CSSRuleList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function CSSRuleList() { [native code] }",
            IE = "[object CSSRuleList]")
    public void cssRuleList() throws Exception {
        test("CSSRuleList");
    }

    /**
     * Test {@link CSSStyleDeclaration}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function CSSStyleDeclaration() { [native code] }",
            IE = "[object CSSStyleDeclaration]")
    public void cssStyleDeclaration() throws Exception {
        test("CSSStyleDeclaration");
    }

    /**
     * Test {@link CSSStyleRule}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function CSSStyleRule() { [native code] }",
            IE = "[object CSSStyleRule]")
    @AlertsStandards(DEFAULT = "function CSSStyleRule() { [native code] }",
            IE = "[object CSSStyleRule]")
    public void cssStyleRule() throws Exception {
        test("CSSStyleRule");
    }

    /**
     * Test {@link CSSStyleSheet}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function CSSStyleSheet() { [native code] }",
            IE = "[object CSSStyleSheet]")
    public void cssStyleSheet() throws Exception {
        test("CSSStyleSheet");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CSSSupportsRule() { [native code] }",
            IE = "exception")
    public void cssSupportsRule() throws Exception {
        test("CSSSupportsRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void cssUnknownRule() throws Exception {
        test("CSSUnknownRule");
    }

    /**
     * Test CSSValue.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void cssValue() throws Exception {
        test("CSSValue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void cssValueList() throws Exception {
        test("CSSValueList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void cssViewportRule() throws Exception {
        test("CSSViewportRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CustomElementRegistry() { [native code] }",
            IE = "exception")
    public void customElementRegistry() throws Exception {
        test("CustomElementRegistry");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function CustomEvent() { [native code] }",
            IE = "[object CustomEvent]")
    public void customEvent() throws Exception {
        test("CustomEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void dataStore() throws Exception {
        test("DataStore");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void dataStoreChangeEvent() throws Exception {
        test("DataStoreChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void dataStoreCursor() throws Exception {
        test("DataStoreCursor");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void dataStoreTask() throws Exception {
        test("DataStoreTask");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DataTransfer() { [native code] }",
            IE = "[object DataTransfer]")
    public void dataTransfer() throws Exception {
        test("DataTransfer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DataTransferItem() { [native code] }",
            IE = "exception")
    public void dataTransferItem() throws Exception {
        test("DataTransferItem");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DataTransferItemList() { [native code] }",
            IE = "exception")
    public void dataTransferItemList() throws Exception {
        test("DataTransferItemList");
    }

    /**
     * Test DataView.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function DataView() { [native code] }",
            IE = "function DataView() {\n    [native code]\n}\n")
    public void dataView() throws Exception {
        test("DataView");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Date() { [native code] }",
            IE = "function Date() {\n    [native code]\n}\n")
    public void date() throws Exception {
        test("Date");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function decodeURI() { [native code] }",
            IE = "function decodeURI() {\n    [native code]\n}\n")
    public void decodeURI() throws Exception {
        test("decodeURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function decodeURIComponent() { [native code] }",
            IE = "function decodeURIComponent() {\n    [native code]\n}\n")
    public void decodeURIComponent() throws Exception {
        test("decodeURIComponent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void dedicatedWorkerGlobalScope() throws Exception {
        test("DedicatedWorkerGlobalScope");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DelayNode() { [native code] }",
            IE = "exception")
    public void delayNode() throws Exception {
        test("DelayNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object DeviceAcceleration]")
    @HtmlUnitNYI(IE = "exception")
    public void deviceAcceleration() throws Exception {
        test("DeviceAcceleration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void deviceLightEvent() throws Exception {
        test("DeviceLightEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DeviceMotionEvent() { [native code] }",
            IE = "[object DeviceMotionEvent]")
    public void deviceMotionEvent() throws Exception {
        test("DeviceMotionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DeviceOrientationEvent() { [native code] }",
            IE = "[object DeviceOrientationEvent]")
    public void deviceOrientationEvent() throws Exception {
        test("DeviceOrientationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void deviceProximityEvent() throws Exception {
        test("DeviceProximityEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object DeviceRotationRate]")
    @HtmlUnitNYI(IE = "exception")
    public void deviceRotationRate() throws Exception {
        test("DeviceRotationRate");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void deviceStorage() throws Exception {
        test("DeviceStorage");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void deviceStorageChangeEvent() throws Exception {
        test("DeviceStorageChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void directoryEntry() throws Exception {
        test("DirectoryEntry");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void directoryEntrySync() throws Exception {
        test("DirectoryEntrySync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void directoryReader() throws Exception {
        test("DirectoryReader");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void directoryReaderSync() throws Exception {
        test("DirectoryReaderSync");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.Document}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Document() { [native code] }",
            IE = "[object Document]")
    public void document() throws Exception {
        test("Document");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.DocumentFragment}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function DocumentFragment() { [native code] }",
            IE = "[object DocumentFragment]")
    public void documentFragment() throws Exception {
        test("DocumentFragment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void documentOrShadowRoot() throws Exception {
        test("DocumentOrShadowRoot");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DocumentTimeline() { [native code] }",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "exception", EDGE = "exception", FF = "exception", FF_ESR = "exception")
    public void documentTimeline() throws Exception {
        test("DocumentTimeline");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void documentTouch() throws Exception {
        test("DocumentTouch");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.DocumentType}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function DocumentType() { [native code] }",
            IE = "[object DocumentType]")
    public void documentType() throws Exception {
        test("DocumentType");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domApplication() throws Exception {
        test("DOMApplication");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domApplicationsManager() throws Exception {
        test("DOMApplicationsManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domApplicationsRegistry() throws Exception {
        test("DOMApplicationsRegistry");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domConfiguration() throws Exception {
        test("DOMConfiguration");
    }

    /**
     * Test DOMCursor.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domCursor() throws Exception {
        test("DOMCursor");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DOMError() { [native code] }",
            FF = "exception",
            FF_ESR = "exception",
            IE = "[object DOMError]")
    public void domError() throws Exception {
        test("DOMError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domErrorHandler() throws Exception {
        test("DOMErrorHandler");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.DOMException}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function DOMException() { [native code] }",
            IE = "[object DOMException]")
    public void domException() throws Exception {
        test("DOMException");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domHighResTimeStamp() throws Exception {
        test("DOMHighResTimeStamp");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.DOMImplementation}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function DOMImplementation() { [native code] }",
            IE = "[object DOMImplementation]")
    public void domImplementation() throws Exception {
        test("DOMImplementation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domImplementationList() throws Exception {
        test("DOMImplementationList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domImplementationRegistry() throws Exception {
        test("DOMImplementationRegistry");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domImplementationSource() throws Exception {
        test("DOMImplementationSource");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domLocator() throws Exception {
        test("DOMLocator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DOMMatrix() { [native code] }",
            IE = "exception")
    public void domMatrix() throws Exception {
        test("DOMMatrix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DOMMatrixReadOnly() { [native code] }",
            IE = "exception")
    public void domMatrixReadOnly() throws Exception {
        test("DOMMatrixReadOnly");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domObject() throws Exception {
        test("DOMObject");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.DOMParser}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function DOMParser() { [native code] }",
            IE = "function DOMParser() {\n    [native code]\n}\n")
    public void domParser() throws Exception {
        test("DOMParser");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DOMPoint() { [native code] }",
            IE = "exception")
    public void domPoint() throws Exception {
        test("DOMPoint");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DOMPointReadOnly() { [native code] }",
            IE = "exception")
    public void domPointReadOnly() throws Exception {
        test("DOMPointReadOnly");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.ClientRect}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function DOMRect() { [native code] }",
            IE = "exception")
    public void domRect() throws Exception {
        test("DOMRect");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function DOMRectList() { [native code] }",
            IE = "exception")
    public void domRectList() throws Exception {
        test("DOMRectList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DOMRectReadOnly() { [native code] }",
            IE = "exception")
    public void domRectReadOnly() throws Exception {
        test("DOMRectReadOnly");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function DOMRequest() { [native code] }",
            FF_ESR = "function DOMRequest() { [native code] }")
    public void domRequest() throws Exception {
        test("DOMRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object DOMSettableTokenList]")
    public void domSettableTokenList() throws Exception {
        test("DOMSettableTokenList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domString() throws Exception {
        test("DOMString");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DOMStringList() { [native code] }",
            IE = "[object DOMStringList]")
    public void domStringList() throws Exception {
        test("DOMStringList");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.DOMStringMap}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function DOMStringMap() { [native code] }",
            IE = "[object DOMStringMap]")
    public void domStringMap() throws Exception {
        test("DOMStringMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domTimeStamp() throws Exception {
        test("DOMTimeStamp");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.DOMTokenList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function DOMTokenList() { [native code] }",
            IE = "[object DOMTokenList]")
    public void domTokenList() throws Exception {
        test("DOMTokenList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domUserData() throws Exception {
        test("DOMUserData");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void doubleRange() throws Exception {
        test("DoubleRange");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DragEvent() { [native code] }",
            IE = "[object DragEvent]")
    public void dragEvent() throws Exception {
        test("DragEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DynamicsCompressorNode() { [native code] }",
            IE = "exception")
    public void dynamicsCompressorNode() throws Exception {
        test("DynamicsCompressorNode");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.Element}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Element() { [native code] }",
            IE = "[object Element]")
    public void element() throws Exception {
        test("Element");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void elementTraversal() throws Exception {
        test("ElementTraversal");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function encodeURI() { [native code] }",
            IE = "function encodeURI() {\n    [native code]\n}\n")
    public void encodeURI() throws Exception {
        test("encodeURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function encodeURIComponent() { [native code] }",
            IE = "function encodeURIComponent() {\n    [native code]\n}\n")
    public void encodeURIComponent() throws Exception {
        test("encodeURIComponent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void entity() throws Exception {
        test("Entity");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void entityReference() throws Exception {
        test("EntityReference");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void entry() throws Exception {
        test("Entry");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void entrySync() throws Exception {
        test("EntrySync");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.Enumerator}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "function Enumerator() {\n    [native code]\n}\n")
    public void enumerator() throws Exception {
        test("Enumerator");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Error() { [native code] }",
            IE = "function Error() {\n    [native code]\n}\n")
    public void error() throws Exception {
        test("Error");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ErrorEvent() { [native code] }",
            IE = "[object ErrorEvent]")
    public void errorEvent() throws Exception {
        test("ErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function escape() { [native code] }",
            IE = "function escape() {\n    [native code]\n}\n")
    public void escape() throws Exception {
        test("escape");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function eval() { [native code] }",
            IE = "function eval() {\n    [native code]\n}\n")
    public void eval() throws Exception {
        test("eval");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function EvalError() { [native code] }",
            IE = "function EvalError() {\n    [native code]\n}\n")
    public void evalError() throws Exception {
        test("EvalError");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.Event}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Event() { [native code] }",
            IE = "[object Event]")
    public void event() throws Exception {
        test("Event");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void eventListener() throws Exception {
        test("EventListener");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void eventNode() throws Exception {
        test("EventNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function EventSource() { [native code] }",
            IE = "exception")
    public void eventSource() throws Exception {
        test("EventSource");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function EventTarget() { [native code] }",
            IE = "exception")
    public void eventTarget() throws Exception {
        test("EventTarget");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void ext_blend_minmax() throws Exception {
        test("EXT_blend_minmax");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void ext_color_buffer_float() throws Exception {
        test("EXT_color_buffer_float");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void ext_color_buffer_half_float() throws Exception {
        test("EXT_color_buffer_half_float");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void ext_disjoint_timer_query() throws Exception {
        test("EXT_disjoint_timer_query");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void ext_frag_depth() throws Exception {
        test("EXT_frag_depth");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void ext_shader_texture_lod() throws Exception {
        test("EXT_shader_texture_lod");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void ext_sRGB() throws Exception {
        test("EXT_sRGB");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object EXT_texture_filter_anisotropic]")
    public void ext_texture_filter_anisotropic() throws Exception {
        test("EXT_texture_filter_anisotropic");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void extendableEvent() throws Exception {
        test("ExtendableEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void extendableMessageEvent() throws Exception {
        test("ExtendableMessageEvent");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.External}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function External() { [native code] }",
            EDGE = "function External() { [native code] }")
    public void external() throws Exception {
        test("External");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function FederatedCredential() { [native code] }",
            EDGE = "function FederatedCredential() { [native code] }")
    public void federatedCredential() throws Exception {
        test("FederatedCredential");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void fetchEvent() throws Exception {
        test("FetchEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function File() { [native code] }",
            IE = "[object File]")
    public void file() throws Exception {
        test("File");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void fileEntry() throws Exception {
        test("FileEntry");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void fileEntrySync() throws Exception {
        test("FileEntrySync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void fileError() throws Exception {
        test("FileError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void fileException() throws Exception {
        test("FileException");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void fileHandle() throws Exception {
        test("FileHandle");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function FileList() { [native code] }",
            IE = "[object FileList]")
    public void fileList() throws Exception {
        test("FileList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function FileReader() { [native code] }",
            IE = "function FileReader() {\n    [native code]\n}\n")
    public void fileReader() throws Exception {
        test("FileReader");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void fileReaderSync() throws Exception {
        test("FileReaderSync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void fileRequest() throws Exception {
        test("FileRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function FileSystem() { [native code] }",
            FF_ESR = "function FileSystem() { [native code] }")
    public void fileSystem() throws Exception {
        test("FileSystem");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function FileSystemDirectoryEntry() { [native code] }",
            FF_ESR = "function FileSystemDirectoryEntry() { [native code] }")
    public void fileSystemDirectoryEntry() throws Exception {
        test("FileSystemDirectoryEntry");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function FileSystemDirectoryReader() { [native code] }",
            FF_ESR = "function FileSystemDirectoryReader() { [native code] }")
    public void fileSystemDirectoryReader() throws Exception {
        test("FileSystemDirectoryReader");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function FileSystemEntry() { [native code] }",
            FF_ESR = "function FileSystemEntry() { [native code] }")
    public void fileSystemEntry() throws Exception {
        test("FileSystemEntry");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function FileSystemFileEntry() { [native code] }",
            FF_ESR = "function FileSystemFileEntry() { [native code] }")
    public void fileSystemFileEntry() throws Exception {
        test("FileSystemFileEntry");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void fileSystemFlags() throws Exception {
        test("FileSystemFlags");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void fileSystemSync() throws Exception {
        test("FileSystemSync");
    }

    /**
     * Test Float32Array.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Float32Array() { [native code] }",
            IE = "function Float32Array() {\n    [native code]\n}\n")
    public void float32Array() throws Exception {
        test("Float32Array");
    }

    /**
     * Test Float64Array.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Float64Array() { [native code] }",
            IE = "function Float64Array() {\n    [native code]\n}\n")
    public void float64Array() throws Exception {
        test("Float64Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void fMRadio() throws Exception {
        test("FMRadio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function FocusEvent() { [native code] }",
            IE = "[object FocusEvent]")
    public void focusEvent() throws Exception {
        test("FocusEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function FontFace() { [native code] }",
            IE = "exception")
    public void fontFace() throws Exception {
        test("FontFace");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function FontFaceSet() { [native code] }",
            FF_ESR = "function FontFaceSet() { [native code] }")
    public void fontFaceSet() throws Exception {
        test("FontFaceSet");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void formChild() throws Exception {
        test("FormChild");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.xml.FormData}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function FormData() { [native code] }",
            IE = "function FormData() {\n    [native code]\n}\n")
    public void formData() throws Exception {
        test("FormData");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void formField() throws Exception {
        test("FormField");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Function() { [native code] }",
            IE = "function Function() {\n    [native code]\n}\n")
    public void function() throws Exception {
        test("Function");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function GainNode() { [native code] }",
            IE = "exception")
    public void gainNode() throws Exception {
        test("GainNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Gamepad() { [native code] }",
            IE = "exception")
    public void gamepad() throws Exception {
        test("Gamepad");
    }

    /**
     * Tests {@link org.htmlunit.javascript.host.GamepadButton}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function GamepadButton() { [native code] }",
            IE = "exception")
    public void gamepadButton() throws Exception {
        test("GamepadButton");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function GamepadEvent() { [native code] }",
            IE = "exception")
    public void gamepadEvent() throws Exception {
        test("GamepadEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void generator() throws Exception {
        test("Generator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void generatorFunction() throws Exception {
        test("GeneratorFunction");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.geo.Geolocation}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Geolocation() { [native code] }",
            IE = "[object Geolocation]")
    public void geolocation() throws Exception {
        test("Geolocation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void gestureEvent() throws Exception {
        test("GestureEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void globalEventHandlers() throws Exception {
        test("GlobalEventHandlers");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void globalFetch() throws Exception {
        test("GlobalFetch");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.HashChangeEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HashChangeEvent() { [native code] }",
            IE = "exception")
    public void hashChangeEvent() throws Exception {
        test("HashChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Headers() { [native code] }",
            IE = "exception")
    public void headers() throws Exception {
        test("Headers");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.History}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function History() { [native code] }",
            IE = "[object History]")
    public void history() throws Exception {
        test("History");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void hMDVRDevice() throws Exception {
        test("HMDVRDevice");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLAllCollection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLAllCollection() { [native code] }",
            IE = "[object HTMLAllCollection]")
    public void htmlAllCollection() throws Exception {
        test("HTMLAllCollection");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLAnchorElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLAnchorElement() { [native code] }",
            IE = "[object HTMLAnchorElement]")
    public void htmlAnchorElement() throws Exception {
        test("HTMLAnchorElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLAppletElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object HTMLAppletElement]")
    public void htmlAppletElement() throws Exception {
        test("HTMLAppletElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLAreaElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLAreaElement() { [native code] }",
            IE = "[object HTMLAreaElement]")
    public void htmlAreaElement() throws Exception {
        test("HTMLAreaElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLAudioElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLAudioElement() { [native code] }",
            IE = "[object HTMLAudioElement]")
    public void htmlAudioElement() throws Exception {
        test("HTMLAudioElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLBaseElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLBaseElement() { [native code] }",
            IE = "[object HTMLBaseElement]")
    public void htmlBaseElement() throws Exception {
        test("HTMLBaseElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLBaseFontElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object HTMLBaseFontElement]")
    public void htmlBaseFontElement() throws Exception {
        test("HTMLBaseFontElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLBGSoundElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object HTMLBGSoundElement]")
    public void htmlBGSoundElement() throws Exception {
        test("HTMLBGSoundElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLBlockElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object HTMLBlockElement]")
    public void htmlBlockElement() throws Exception {
        test("HTMLBlockElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLQuoteElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlBlockQuoteElement() throws Exception {
        test("HTMLBlockQuoteElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLBodyElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLBodyElement() { [native code] }",
            IE = "[object HTMLBodyElement]")
    public void htmlBodyElement() throws Exception {
        test("HTMLBodyElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLBRElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLBRElement() { [native code] }",
            IE = "[object HTMLBRElement]")
    public void htmlBRElement() throws Exception {
        test("HTMLBRElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLButtonElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLButtonElement() { [native code] }",
            IE = "[object HTMLButtonElement]")
    public void htmlButtonElement() throws Exception {
        test("HTMLButtonElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLCanvasElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLCanvasElement() { [native code] }",
            IE = "[object HTMLCanvasElement]")
    public void htmlCanvasElement() throws Exception {
        test("HTMLCanvasElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLCollection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLCollection() { [native code] }",
            IE = "[object HTMLCollection]")
    public void htmlCollection() throws Exception {
        test("HTMLCollection");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.Comment}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlCommentElement() throws Exception {
        test("HTMLCommentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void htmlContentElement() throws Exception {
        test("HTMLContentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function HTMLDataElement() { [native code] }",
            IE = "exception")
    public void htmlDataElement() throws Exception {
        test("HTMLDataElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLDataListElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLDataListElement() { [native code] }",
            IE = "[object HTMLDataListElement]")
    public void htmlDataListElement() throws Exception {
        test("HTMLDataListElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLDDElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object HTMLDDElement]")
    public void htmlDDElement() throws Exception {
        test("HTMLDDElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLDDElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlDefinitionDescriptionElement() throws Exception {
        test("HTMLDefinitionDescriptionElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLDTElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlDefinitionTermElement() throws Exception {
        test("HTMLDefinitionTermElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLDetailsElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLDetailsElement() { [native code] }",
            IE = "exception")
    public void htmlDetailsElement() throws Exception {
        test("HTMLDetailsElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLDialogElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLDialogElement() { [native code] }",
            IE = "exception")
    public void htmlDialogElement() throws Exception {
        test("HTMLDialogElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLDirectoryElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLDirectoryElement() { [native code] }",
            IE = "[object HTMLDirectoryElement]")
    public void htmlDirectoryElement() throws Exception {
        test("HTMLDirectoryElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLDivElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLDivElement() { [native code] }",
            IE = "[object HTMLDivElement]")
    public void htmlDivElement() throws Exception {
        test("HTMLDivElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLDListElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLDListElement() { [native code] }",
            IE = "[object HTMLDListElement]")
    public void htmlDListElement() throws Exception {
        test("HTMLDListElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLDocument}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLDocument() { [native code] }",
            IE = "[object HTMLDocument]")
    public void htmlDocument() throws Exception {
        test("HTMLDocument");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLDTElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object HTMLDTElement]")
    public void htmlDTElement() throws Exception {
        test("HTMLDTElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLElement() { [native code] }",
            IE = "[object HTMLElement]")
    public void htmlElement() throws Exception {
        test("HTMLElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLEmbedElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLEmbedElement() { [native code] }",
            IE = "[object HTMLEmbedElement]")
    public void htmlEmbedElement() throws Exception {
        test("HTMLEmbedElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLFieldSetElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLFieldSetElement() { [native code] }",
            IE = "[object HTMLFieldSetElement]")
    public void htmlFieldSetElement() throws Exception {
        test("HTMLFieldSetElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLFontElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLFontElement() { [native code] }",
            IE = "[object HTMLFontElement]")
    public void htmlFontElement() throws Exception {
        test("HTMLFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function HTMLFormControlsCollection() { [native code] }",
            IE = "exception")
    public void htmlFormControlsCollection() throws Exception {
        test("HTMLFormControlsCollection");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLFormElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLFormElement() { [native code] }",
            IE = "[object HTMLFormElement]")
    public void htmlFormElement() throws Exception {
        test("HTMLFormElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLFrameElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLFrameElement() { [native code] }",
            IE = "[object HTMLFrameElement]")
    public void htmlFrameElement() throws Exception {
        test("HTMLFrameElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLFrameSetElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLFrameSetElement() { [native code] }",
            IE = "[object HTMLFrameSetElement]")
    public void htmlFrameSetElement() throws Exception {
        test("HTMLFrameSetElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLUnknownElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlGenericElement() throws Exception {
        test("HTMLGenericElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLHeadElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLHeadElement() { [native code] }",
            IE = "[object HTMLHeadElement]")
    public void htmlHeadElement() throws Exception {
        test("HTMLHeadElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLHeadingElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLHeadingElement() { [native code] }",
            IE = "[object HTMLHeadingElement]")
    public void htmlHeadingElement() throws Exception {
        test("HTMLHeadingElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLHRElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLHRElement() { [native code] }",
            IE = "[object HTMLHRElement]")
    public void htmlHRElement() throws Exception {
        test("HTMLHRElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLHtmlElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLHtmlElement() { [native code] }",
            IE = "[object HTMLHtmlElement]")
    public void htmlHtmlElement() throws Exception {
        test("HTMLHtmlElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void htmlHyperlinkElementUtils() throws Exception {
        test("HTMLHyperlinkElementUtils");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLIFrameElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLIFrameElement() { [native code] }",
            IE = "[object HTMLIFrameElement]")
    public void htmlIFrameElement() throws Exception {
        test("HTMLIFrameElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLImageElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLImageElement() { [native code] }",
            IE = "[object HTMLImageElement]")
    public void htmlImageElement() throws Exception {
        test("HTMLImageElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLInlineQuotationElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlInlineQuotationElement() throws Exception {
        test("HTMLInlineQuotationElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLInputElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLInputElement() { [native code] }",
            IE = "[object HTMLInputElement]")
    public void htmlInputElement() throws Exception {
        test("HTMLInputElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLIsIndexElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object HTMLIsIndexElement]")
    public void htmlIsIndexElement() throws Exception {
        test("HTMLIsIndexElement");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlKeygenElement() throws Exception {
        test("HTMLKeygenElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLLabelElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLLabelElement() { [native code] }",
            IE = "[object HTMLLabelElement]")
    public void htmlLabelElement() throws Exception {
        test("HTMLLabelElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLLegendElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLLegendElement() { [native code] }",
            IE = "[object HTMLLegendElement]")
    public void htmlLegendElement() throws Exception {
        test("HTMLLegendElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLLIElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLLIElement() { [native code] }",
            IE = "[object HTMLLIElement]")
    public void htmlLIElement() throws Exception {
        test("HTMLLIElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLLinkElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLLinkElement() { [native code] }",
            IE = "[object HTMLLinkElement]")
    public void htmlLinkElement() throws Exception {
        test("HTMLLinkElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLListElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlListElement() throws Exception {
        test("HTMLListElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLMapElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLMapElement() { [native code] }",
            IE = "[object HTMLMapElement]")
    public void htmlMapElement() throws Exception {
        test("HTMLMapElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLMarqueeElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLMarqueeElement() { [native code] }",
            IE = "[object HTMLMarqueeElement]")
    public void htmlMarqueeElement() throws Exception {
        test("HTMLMarqueeElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLMediaElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLMediaElement() { [native code] }",
            IE = "[object HTMLMediaElement]")
    public void htmlMediaElement() throws Exception {
        test("HTMLMediaElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLMenuElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLMenuElement() { [native code] }",
            IE = "[object HTMLMenuElement]")
    public void htmlMenuElement() throws Exception {
        test("HTMLMenuElement");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlMenuItemElement() throws Exception {
        test("HTMLMenuItemElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLMetaElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLMetaElement() { [native code] }",
            IE = "[object HTMLMetaElement]")
    public void htmlMetaElement() throws Exception {
        test("HTMLMetaElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLMeterElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLMeterElement() { [native code] }",
            IE = "exception")
    public void htmlMeterElement() throws Exception {
        test("HTMLMeterElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLModElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLModElement() { [native code] }",
            IE = "[object HTMLModElement]")
    public void htmlModElement() throws Exception {
        test("HTMLModElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLNextIdElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object HTMLNextIdElement]")
    public void htmlNextIdElement() throws Exception {
        test("HTMLNextIdElement");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlNoShowElement() throws Exception {
        test("HTMLNoShowElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLObjectElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLObjectElement() { [native code] }",
            IE = "[object HTMLObjectElement]")
    public void htmlObjectElement() throws Exception {
        test("HTMLObjectElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLOListElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLOListElement() { [native code] }",
            IE = "[object HTMLOListElement]")
    public void htmlOListElement() throws Exception {
        test("HTMLOListElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLOptGroupElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLOptGroupElement() { [native code] }",
            IE = "[object HTMLOptGroupElement]")
    public void htmlOptGroupElement() throws Exception {
        test("HTMLOptGroupElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLOptionElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLOptionElement() { [native code] }",
            IE = "[object HTMLOptionElement]")
    public void htmlOptionElement() throws Exception {
        test("HTMLOptionElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLOptionsCollection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLOptionsCollection() { [native code] }",
            IE = "exception")
    public void htmlOptionsCollection() throws Exception {
        test("HTMLOptionsCollection");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLOutputElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLOutputElement() { [native code] }",
            IE = "exception")
    public void htmlOutputElement() throws Exception {
        test("HTMLOutputElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLParagraphElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLParagraphElement() { [native code] }",
            IE = "[object HTMLParagraphElement]")
    public void htmlParagraphElement() throws Exception {
        test("HTMLParagraphElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLParamElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLParamElement() { [native code] }",
            IE = "[object HTMLParamElement]")
    public void htmlParamElement() throws Exception {
        test("HTMLParamElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLPhraseElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object HTMLPhraseElement]")
    public void htmlPhraseElement() throws Exception {
        test("HTMLPhraseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function HTMLPictureElement() { [native code] }",
            IE = "exception")
    public void htmlPictureElement() throws Exception {
        test("HTMLPictureElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLPreElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLPreElement() { [native code] }",
            IE = "[object HTMLPreElement]")
    public void htmlPreElement() throws Exception {
        test("HTMLPreElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLProgressElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLProgressElement() { [native code] }",
            IE = "[object HTMLProgressElement]")
    public void htmlProgressElement() throws Exception {
        test("HTMLProgressElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLQuoteElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLQuoteElement() { [native code] }",
            IE = "[object HTMLQuoteElement]")
    public void htmlQuoteElement() throws Exception {
        test("HTMLQuoteElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLScriptElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLScriptElement() { [native code] }",
            IE = "[object HTMLScriptElement]")
    public void htmlScriptElement() throws Exception {
        test("HTMLScriptElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLSelectElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLSelectElement() { [native code] }",
            IE = "[object HTMLSelectElement]")
    public void htmlSelectElement() throws Exception {
        test("HTMLSelectElement");
    }

    /**
     * Test HTMLShadowElement.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void htmlShadowElement() throws Exception {
        test("HTMLShadowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function HTMLSlotElement() { [native code] }",
            IE = "exception")
    public void htmlSlotElement() throws Exception {
        test("HTMLSlotElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLSourceElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLSourceElement() { [native code] }",
            IE = "[object HTMLSourceElement]")
    public void htmlSourceElement() throws Exception {
        test("HTMLSourceElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLSpanElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLSpanElement() { [native code] }",
            IE = "[object HTMLSpanElement]")
    public void htmlSpanElement() throws Exception {
        test("HTMLSpanElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLStyleElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLStyleElement() { [native code] }",
            IE = "[object HTMLStyleElement]")
    public void htmlStyleElement() throws Exception {
        test("HTMLStyleElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLTableCaptionElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLTableCaptionElement() { [native code] }",
            IE = "[object HTMLTableCaptionElement]")
    public void htmlTableCaptionElement() throws Exception {
        test("HTMLTableCaptionElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLTableCellElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLTableCellElement() { [native code] }",
            IE = "[object HTMLTableCellElement]")
    public void htmlTableCellElement() throws Exception {
        test("HTMLTableCellElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLTableColElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLTableColElement() { [native code] }",
            IE = "[object HTMLTableColElement]")
    public void htmlTableColElement() throws Exception {
        test("HTMLTableColElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLTableComponent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlTableComponent() throws Exception {
        test("HTMLTableComponent");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLTableDataCellElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object HTMLTableDataCellElement]")
    public void htmlTableDataCellElement() throws Exception {
        test("HTMLTableDataCellElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLTableElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLTableElement() { [native code] }",
            IE = "[object HTMLTableElement]")
    public void htmlTableElement() throws Exception {
        test("HTMLTableElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLTableHeaderCellElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object HTMLTableHeaderCellElement]")
    public void htmlTableHeaderCellElement() throws Exception {
        test("HTMLTableHeaderCellElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLTableRowElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLTableRowElement() { [native code] }",
            IE = "[object HTMLTableRowElement]")
    public void htmlTableRowElement() throws Exception {
        test("HTMLTableRowElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLTableSectionElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLTableSectionElement() { [native code] }",
            IE = "[object HTMLTableSectionElement]")
    public void htmlTableSectionElement() throws Exception {
        test("HTMLTableSectionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function HTMLTemplateElement() { [native code] }",
            IE = "exception")
    public void htmlTemplateElement() throws Exception {
        test("HTMLTemplateElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLTextAreaElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLTextAreaElement() { [native code] }",
            IE = "[object HTMLTextAreaElement]")
    public void htmlTextAreaElement() throws Exception {
        test("HTMLTextAreaElement");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlTextElement() throws Exception {
        test("HTMLTextElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLTimeElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLTimeElement() { [native code] }",
            IE = "exception")
    public void htmlTimeElement() throws Exception {
        test("HTMLTimeElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLTitleElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLTitleElement() { [native code] }",
            IE = "[object HTMLTitleElement]")
    public void htmlTitleElement() throws Exception {
        test("HTMLTitleElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLTrackElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLTrackElement() { [native code] }",
            IE = "[object HTMLTrackElement]")
    public void htmlTrackElement() throws Exception {
        test("HTMLTrackElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLUListElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLUListElement() { [native code] }",
            IE = "[object HTMLUListElement]")
    public void htmlUListElement() throws Exception {
        test("HTMLUListElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLUnknownElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLUnknownElement() { [native code] }",
            IE = "[object HTMLUnknownElement]")
    public void htmlUnknownElement() throws Exception {
        test("HTMLUnknownElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLVideoElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function HTMLVideoElement() { [native code] }",
            IE = "[object HTMLVideoElement]")
    public void htmlVideoElement() throws Exception {
        test("HTMLVideoElement");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlWBRElement() throws Exception {
        test("HTMLWBRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IDBCursor() { [native code] }",
            IE = "[object IDBCursor]")
    public void idbCursor() throws Exception {
        test("IDBCursor");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void idbCursorSync() throws Exception {
        test("IDBCursorSync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IDBCursorWithValue() { [native code] }",
            IE = "[object IDBCursorWithValue]")
    public void idbCursorWithValue() throws Exception {
        test("IDBCursorWithValue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IDBDatabase() { [native code] }",
            IE = "[object IDBDatabase]")
    public void idbDatabase() throws Exception {
        test("IDBDatabase");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void idbDatabaseException() throws Exception {
        test("IDBDatabaseException");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void idbDatabaseSync() throws Exception {
        test("IDBDatabaseSync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void idbEnvironment() throws Exception {
        test("IDBEnvironment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void idbEnvironmentSync() throws Exception {
        test("IDBEnvironmentSync");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.idb.IDBFactory}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IDBFactory() { [native code] }",
            IE = "[object IDBFactory]")
    public void idbFactory() throws Exception {
        test("IDBFactory");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void idbFactorySync() throws Exception {
        test("IDBFactorySync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IDBIndex() { [native code] }",
            IE = "[object IDBIndex]")
    public void idbIndex() throws Exception {
        test("IDBIndex");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void idbIndexSync() throws Exception {
        test("IDBIndexSync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IDBKeyRange() { [native code] }",
            IE = "[object IDBKeyRange]")
    public void idbKeyRange() throws Exception {
        test("IDBKeyRange");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void idbLocaleAwareKeyRange() throws Exception {
        test("IDBLocaleAwareKeyRange");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void idbMutableFile() throws Exception {
        test("IDBMutableFile");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IDBObjectStore() { [native code] }",
            IE = "[object IDBObjectStore]")
    public void idbObjectStore() throws Exception {
        test("IDBObjectStore");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void idbObjectStoreSync() throws Exception {
        test("IDBObjectStoreSync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IDBOpenDBRequest() { [native code] }",
            IE = "[object IDBOpenDBRequest]")
    public void idbOpenDBRequest() throws Exception {
        test("IDBOpenDBRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IDBRequest() { [native code] }",
            IE = "[object IDBRequest]")
    public void idbRequest() throws Exception {
        test("IDBRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IDBTransaction() { [native code] }",
            IE = "[object IDBTransaction]")
    public void idbTransaction() throws Exception {
        test("IDBTransaction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void idbTransactionSync() throws Exception {
        test("IDBTransactionSync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IDBVersionChangeEvent() { [native code] }",
            IE = "[object IDBVersionChangeEvent]")
    public void idbVersionChangeEvent() throws Exception {
        test("IDBVersionChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void idbVersionChangeRequest() throws Exception {
        test("IDBVersionChangeRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void identityManager() throws Exception {
        test("IdentityManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IdleDeadline() { [native code] }",
            IE = "exception")
    public void idleDeadline() throws Exception {
        test("IdleDeadline");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IIRFilterNode() { [native code] }",
            IE = "exception")
    public void iirFilterNode() throws Exception {
        test("IIRFilterNode");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLImageElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Image() { [native code] }",
            IE = "function Image() {\n    [native code]\n}\n")
    public void image() throws Exception {
        test("Image");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ImageBitmap() { [native code] }",
            IE = "exception")
    public void imageBitmap() throws Exception {
        test("ImageBitmap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void imageBitmapFactories() throws Exception {
        test("ImageBitmapFactories");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ImageBitmapRenderingContext() { [native code] }",
            IE = "exception")
    public void imageBitmapRenderingContext() throws Exception {
        test("ImageBitmapRenderingContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ImageData() { [native code] }",
            IE = "[object ImageData]")
    public void imageData() throws Exception {
        test("ImageData");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void index() throws Exception {
        test("Index");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void indexedDB() throws Exception {
        test("IndexedDB");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Infinity")
    public void infinity() throws Exception {
        test("Infinity");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function InputDeviceCapabilities() { [native code] }",
            EDGE = "function InputDeviceCapabilities() { [native code] }")
    public void inputDeviceCapabilities() throws Exception {
        test("InputDeviceCapabilities");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function InputEvent() { [native code] }",
            IE = "exception")
    public void inputEvent() throws Exception {
        test("InputEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void inputMethodContext() throws Exception {
        test("InputMethodContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void installEvent() throws Exception {
        test("InstallEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "null",
            FF_ESR = "null")
    public void installTrigger() throws Exception {
        test("InstallTrigger");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void installTriggerImpl() throws Exception {
        test("InstallTriggerImpl");
    }

    /**
     * Test Int16Array.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Int16Array() { [native code] }",
            IE = "function Int16Array() {\n    [native code]\n}\n")
    public void int16Array() throws Exception {
        test("Int16Array");
    }

    /**
     * Test Int32Array.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Int32Array() { [native code] }",
            IE = "function Int32Array() {\n    [native code]\n}\n")
    public void int32Array() throws Exception {
        test("Int32Array");
    }

    /**
     * Test Int8Array.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Int8Array() { [native code] }",
            IE = "function Int8Array() {\n    [native code]\n}\n")
    public void int8Array() throws Exception {
        test("Int8Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function InternalError() { [native code] }",
            FF_ESR = "function InternalError() { [native code] }")
    @HtmlUnitNYI(CHROME = "function InternalError() { [native code] }",
            EDGE = "function InternalError() { [native code] }",
            IE = "function InternalError() {\n    [native code]\n}\n")
    public void internalError() throws Exception {
        test("InternalError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IntersectionObserver() { [native code] }",
            IE = "exception")
    public void intersectionObserver() throws Exception {
        test("IntersectionObserver");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function IntersectionObserverEntry() { [native code] }",
            IE = "exception")
    public void intersectionObserverEntry() throws Exception {
        test("IntersectionObserverEntry");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.intl.Intl}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object Intl]",
            IE = "[object Object]")
    public void intl() throws Exception {
        test("Intl");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Collator() { [native code] }",
            IE = "function Collator() {\n    [native code]\n}\n")
    public void intl_Collator() throws Exception {
        test("Intl.Collator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DateTimeFormat() { [native code] }",
            IE = "function DateTimeFormat() {\n    [native code]\n}\n")
    public void intl_DateTimeFormat() throws Exception {
        test("Intl.DateTimeFormat");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function NumberFormat() { [native code] }",
            IE = "function NumberFormat() {\n    [native code]\n}\n")
    public void intl_NumberFormat() throws Exception {
        test("Intl.NumberFormat");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function isFinite() { [native code] }",
            IE = "function isFinite() {\n    [native code]\n}\n")
    public void isFinite() throws Exception {
        test("isFinite");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function isNaN() { [native code] }",
            IE = "function isNaN() {\n    [native code]\n}\n")
    public void isNaN() throws Exception {
        test("isNaN");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function Iterator() { [native code] }",
            EDGE = "function Iterator() { [native code] }")
    @HtmlUnitNYI(CHROME = "exception",
            EDGE = "exception")
    public void iterator() throws Exception {
        test("Iterator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object JSON]")
    public void json() throws Exception {
        test("JSON");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.KeyboardEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function KeyboardEvent() { [native code] }",
            IE = "[object KeyboardEvent]")
    public void keyboardEvent() throws Exception {
        test("KeyboardEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function KeyframeEffect() { [native code] }",
            IE = "exception")
    public void keyframeEffect() throws Exception {
        test("KeyframeEffect");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void keyframeEffectReadOnly() throws Exception {
        test("KeyframeEffectReadOnly");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void l10n() throws Exception {
        test("L10n");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void l10n_formatValue() throws Exception {
        test("L10n.formatValue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void l10n_get() throws Exception {
        test("L10n.get");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void l10n_language_code() throws Exception {
        test("L10n.language.code");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void l10n_language_direction() throws Exception {
        test("L10n.language.direction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void l10n_once() throws Exception {
        test("L10n.once");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void l10n_ready() throws Exception {
        test("L10n.ready");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void l10n_readyState() throws Exception {
        test("L10n.readyState");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void l10n_setAttributes() throws Exception {
        test("L10n.setAttributes");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void linkStyle() throws Exception {
        test("LinkStyle");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void localFileSystem() throws Exception {
        test("LocalFileSystem");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void localFileSystemSync() throws Exception {
        test("LocalFileSystemSync");
    }

    /**
     * Test LocalMediaStream.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void localMediaStream() throws Exception {
        test("LocalMediaStream");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.Location}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Location() { [native code] }",
            IE = "[object Location]")
    @AlertsStandards(DEFAULT = "function Location() { [native code] }",
            IE = "[object Location]")
    public void location() throws Exception {
        test("Location");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void lockedFile() throws Exception {
        test("LockedFile");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void longRange() throws Exception {
        test("LongRange");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Map() { [native code] }",
            IE = "function Map() {\n    [native code]\n}\n")
    public void map() throws Exception {
        test("Map");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Math]")
    public void math() throws Exception {
        test("Math");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaDeviceInfo() { [native code] }",
            IE = "exception")
    public void mediaDeviceInfo() throws Exception {
        test("MediaDeviceInfo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaDevices() { [native code] }",
            IE = "exception")
    public void mediaDevices() throws Exception {
        test("MediaDevices");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaElementAudioSourceNode() { [native code] }",
            IE = "exception")
    public void mediaElementAudioSourceNode() throws Exception {
        test("MediaElementAudioSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaEncryptedEvent() { [native code] }",
            IE = "exception")
    public void mediaEncryptedEvent() throws Exception {
        test("MediaEncryptedEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaError() { [native code] }",
            IE = "[object MediaError]")
    public void mediaError() throws Exception {
        test("MediaError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function MediaKeyError() { [native code] }",
            FF_ESR = "function MediaKeyError() { [native code] }")
    public void mediaKeyError() throws Exception {
        test("MediaKeyError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mediaKeyEvent() throws Exception {
        test("MediaKeyEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaKeyMessageEvent() { [native code] }",
            IE = "exception")
    public void mediaKeyMessageEvent() throws Exception {
        test("MediaKeyMessageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaKeys() { [native code] }",
            IE = "exception")
    public void mediaKeys() throws Exception {
        test("MediaKeys");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaKeySession() { [native code] }",
            IE = "exception")
    public void mediaKeySession() throws Exception {
        test("MediaKeySession");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaKeyStatusMap() { [native code] }",
            IE = "exception")
    public void mediaKeyStatusMap() throws Exception {
        test("MediaKeyStatusMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaKeySystemAccess() { [native code] }",
            IE = "exception")
    public void mediaKeySystemAccess() throws Exception {
        test("MediaKeySystemAccess");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mediaKeySystemConfiguration() throws Exception {
        test("MediaKeySystemConfiguration");
    }

    /**
     * Test {@link MediaList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function MediaList() { [native code] }",
            IE = "[object MediaList]")
    public void mediaList() throws Exception {
        test("MediaList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaQueryList() { [native code] }",
            IE = "[object MediaQueryList]")
    public void mediaQueryList() throws Exception {
        test("MediaQueryList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaQueryListEvent() { [native code] }",
            IE = "exception")
    public void mediaQueryListEvent() throws Exception {
        test("MediaQueryListEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mediaQueryListListener() throws Exception {
        test("MediaQueryListListener");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaRecorder() { [native code] }",
            IE = "exception")
    public void mediaRecorder() throws Exception {
        test("MediaRecorder");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaSource() { [native code] }",
            IE = "function MediaSource() {\n    [native code]\n}\n")
    public void mediaSource() throws Exception {
        test("MediaSource");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaStream() { [native code] }",
            IE = "exception")
    public void mediaStream() throws Exception {
        test("MediaStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaStreamAudioDestinationNode() { [native code] }",
            IE = "exception")
    public void mediaStreamAudioDestinationNode() throws Exception {
        test("MediaStreamAudioDestinationNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaStreamAudioSourceNode() { [native code] }",
            IE = "exception")
    public void mediaStreamAudioSourceNode() throws Exception {
        test("MediaStreamAudioSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mediaStreamConstraints() throws Exception {
        test("MediaStreamConstraints");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaStreamEvent() { [native code] }",
            IE = "exception")
    public void mediaStreamEvent() throws Exception {
        test("MediaStreamEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaStreamTrack() { [native code] }",
            IE = "exception")
    public void mediaStreamTrack() throws Exception {
        test("MediaStreamTrack");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MediaStreamTrackEvent() { [native code] }",
            IE = "exception")
    public void mediaStreamTrackEvent() throws Exception {
        test("MediaStreamTrackEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mediaTrackConstraints() throws Exception {
        test("MediaTrackConstraints");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mediaTrackSettings() throws Exception {
        test("MediaTrackSettings");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mediaTrackSupportedConstraints() throws Exception {
        test("MediaTrackSupportedConstraints");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.MessageChannel}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function MessageChannel() { [native code] }",
            IE = "function MessageChannel() {\n    [native code]\n}\n")
    public void messageChannel() throws Exception {
        test("MessageChannel");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.MessageEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function MessageEvent() { [native code] }",
            IE = "[object MessageEvent]")
    public void messageEvent() throws Exception {
        test("MessageEvent");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.MessagePort}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function MessagePort() { [native code] }",
            IE = "[object MessagePort]")
    public void messagePort() throws Exception {
        test("MessagePort");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void metadata() throws Exception {
        test("Metadata");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MIDIAccess() { [native code] }",
            IE = "exception")
    public void midiAccess() throws Exception {
        test("MIDIAccess");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MIDIConnectionEvent() { [native code] }",
            IE = "exception")
    public void midiConnectionEvent() throws Exception {
        test("MIDIConnectionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MIDIInput() { [native code] }",
            IE = "exception")
    public void midiInput() throws Exception {
        test("MIDIInput");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MIDIInputMap() { [native code] }",
            IE = "exception")
    public void midiInputMap() throws Exception {
        test("MIDIInputMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MIDIMessageEvent() { [native code] }",
            IE = "exception")
    public void midiMessageEvent() throws Exception {
        test("MIDIMessageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MIDIOutput() { [native code] }",
            IE = "exception")
    public void midiOutput() throws Exception {
        test("MIDIOutput");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MIDIOutputMap() { [native code] }",
            IE = "exception")
    public void midiOutputMap() throws Exception {
        test("MIDIOutputMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MIDIPort() { [native code] }",
            IE = "exception")
    public void midiPort() throws Exception {
        test("MIDIPort");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.MimeType}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function MimeType() { [native code] }",
            IE = "[object MimeType]")
    public void mimeType() throws Exception {
        test("MimeType");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.MimeTypeArray}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function MimeTypeArray() { [native code] }",
            IE = "[object MimeTypeArray]")
    public void mimeTypeArray() throws Exception {
        test("MimeTypeArray");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.MouseEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function MouseEvent() { [native code] }",
            IE = "[object MouseEvent]")
    public void mouseEvent() throws Exception {
        test("MouseEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function MouseScrollEvent() { [native code] }",
            FF_ESR = "function MouseScrollEvent() { [native code] }")
    public void mouseScrollEvent() throws Exception {
        test("MouseScrollEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object MouseWheelEvent]")
    public void mouseWheelEvent() throws Exception {
        test("MouseWheelEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozActivity() throws Exception {
        test("MozActivity");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozActivityOptions() throws Exception {
        test("MozActivityOptions");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozActivityRequestHandler() throws Exception {
        test("MozActivityRequestHandler");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozAlarmsManager() throws Exception {
        test("MozAlarmsManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozContact() throws Exception {
        test("MozContact");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozContactChangeEvent() throws Exception {
        test("MozContactChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozCSSKeyframesRule() throws Exception {
        test("MozCSSKeyframesRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozIccManager() throws Exception {
        test("MozIccManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozMmsEvent() throws Exception {
        test("MozMmsEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozMmsMessage() throws Exception {
        test("MozMmsMessage");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozMobileCellInfo() throws Exception {
        test("MozMobileCellInfo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozMobileCFInfo() throws Exception {
        test("MozMobileCFInfo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozMobileConnection() throws Exception {
        test("MozMobileConnection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozMobileConnectionInfo() throws Exception {
        test("MozMobileConnectionInfo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozMobileICCInfo() throws Exception {
        test("MozMobileICCInfo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozMobileMessageManager() throws Exception {
        test("MozMobileMessageManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozMobileMessageThread() throws Exception {
        test("MozMobileMessageThread");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozMobileNetworkInfo() throws Exception {
        test("MozMobileNetworkInfo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozNDEFRecord() throws Exception {
        test("MozNDEFRecord");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozNetworkStats() throws Exception {
        test("MozNetworkStats");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozNetworkStatsData() throws Exception {
        test("MozNetworkStatsData");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozNetworkStatsManager() throws Exception {
        test("MozNetworkStatsManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozNFC() throws Exception {
        test("MozNFC");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozNFCPeer() throws Exception {
        test("MozNFCPeer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozNFCTag() throws Exception {
        test("MozNFCTag");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozPowerManager() throws Exception {
        test("MozPowerManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozRTCIceCandidate() throws Exception {
        test("mozRTCIceCandidate");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozRTCPeerConnection() throws Exception {
        test("mozRTCPeerConnection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozRTCSessionDescription() throws Exception {
        test("mozRTCSessionDescription");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozSettingsEvent() throws Exception {
        test("MozSettingsEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozSmsEvent() throws Exception {
        test("MozSmsEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozSmsFilter() throws Exception {
        test("MozSmsFilter");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozSmsManager() throws Exception {
        test("MozSmsManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozSmsMessage() throws Exception {
        test("MozSmsMessage");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozSmsSegmentInfo() throws Exception {
        test("MozSmsSegmentInfo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozSocial() throws Exception {
        test("MozSocial");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozTimeManager() throws Exception {
        test("MozTimeManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozVoicemail() throws Exception {
        test("MozVoicemail");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozVoicemailEvent() throws Exception {
        test("MozVoicemailEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozVoicemailStatus() throws Exception {
        test("MozVoicemailStatus");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozWifiConnectionInfoEvent() throws Exception {
        test("MozWifiConnectionInfoEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozWifiP2pGroupOwner() throws Exception {
        test("MozWifiP2pGroupOwner");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozWifiP2pManager() throws Exception {
        test("MozWifiP2pManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void mozWifiStatusChangeEvent() throws Exception {
        test("MozWifiStatusChangeEvent");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object MSCurrentStyleCSSProperties]")
    @HtmlUnitNYI(IE = "exception")
    public void msCurrentStyleCSSProperties() throws Exception {
        test("MSCurrentStyleCSSProperties");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object MSGestureEvent]")
    public void msGestureEvent() throws Exception {
        test("MSGestureEvent");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object MSStyleCSSProperties]")
    @HtmlUnitNYI(IE = "exception")
    public void msStyleCSSProperties() throws Exception {
        test("MSStyleCSSProperties");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.MutationEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function MutationEvent() { [native code] }",
            IE = "[object MutationEvent]")
    public void mutationEvent() throws Exception {
        test("MutationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MutationObserver() { [native code] }",
            IE = "function MutationObserver() {\n    [native code]\n}\n")
    public void mutationObserver() throws Exception {
        test("MutationObserver");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function MutationRecord() { [native code] }",
            IE = "[object MutationRecord]")
    public void mutationRecord() throws Exception {
        test("MutationRecord");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.NamedNodeMap}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function NamedNodeMap() { [native code] }",
            IE = "[object NamedNodeMap]")
    public void namedNodeMap() throws Exception {
        test("NamedNodeMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void nameList() throws Exception {
        test("NameList");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.Namespace}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void namespace() throws Exception {
        test("Namespace");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.NamespaceCollection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void namespaceCollection() throws Exception {
        test("NamespaceCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("NaN")
    public void naN() throws Exception {
        test("NaN");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.XPathNSResolver}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void nativeXPathNSResolver() throws Exception {
        test("NativeXPathNSResolver");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.Navigator}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Navigator() { [native code] }",
            IE = "[object Navigator]")
    public void navigator() throws Exception {
        test("Navigator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void navigatorConcurrentHardware() throws Exception {
        test("NavigatorConcurrentHardware");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void navigatorGeolocation() throws Exception {
        test("NavigatorGeolocation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void navigatorID() throws Exception {
        test("NavigatorID");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void navigatorLanguage() throws Exception {
        test("NavigatorLanguage");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void navigatorOnLine() throws Exception {
        test("NavigatorOnLine");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void navigatorPlugins() throws Exception {
        test("NavigatorPlugins");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void navigatorStorage() throws Exception {
        test("NavigatorStorage");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function NetworkInformation() { [native code] }",
            EDGE = "function NetworkInformation() { [native code] }")
    public void networkInformation() throws Exception {
        test("NetworkInformation");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.Node}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Node() { [native code] }",
            IE = "[object Node]")
    public void node() throws Exception {
        test("Node");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.NodeFilter}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function NodeFilter() { [native code] }",
            IE = "[object NodeFilter]")
    public void nodeFilter() throws Exception {
        test("NodeFilter");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function NodeIterator() { [native code] }",
            IE = "[object NodeIterator]")
    public void nodeIterator() throws Exception {
        test("NodeIterator");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.NodeList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function NodeList() { [native code] }",
            IE = "[object NodeList]")
    public void nodeList() throws Exception {
        test("NodeList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void nonDocumentTypeChildNode() throws Exception {
        test("NonDocumentTypeChildNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void notation() throws Exception {
        test("Notation");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.Notification}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Notification() { [native code] }",
            IE = "exception")
    public void notification() throws Exception {
        test("Notification");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void notificationEvent() throws Exception {
        test("NotificationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void notifyAudioAvailableEvent() throws Exception {
        test("NotifyAudioAvailableEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Number() { [native code] }",
            IE = "function Number() {\n    [native code]\n}\n")
    public void number() throws Exception {
        test("Number");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Object() { [native code] }",
            IE = "function Object() {\n    [native code]\n}\n")
    public void object() throws Exception {
        test("Object");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object OES_element_index_uint]")
    public void oes_element_index_uint() throws Exception {
        test("OES_element_index_uint");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object OES_standard_derivatives]")
    public void oes_standard_derivatives() throws Exception {
        test("OES_standard_derivatives");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object OES_texture_float]")
    public void oes_texture_float() throws Exception {
        test("OES_texture_float");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object OES_texture_float_linear]")
    public void oes_texture_float_linear() throws Exception {
        test("OES_texture_float_linear");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void oes_texture_half_float() throws Exception {
        test("OES_texture_half_float");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void oes_texture_half_float_linear() throws Exception {
        test("OES_texture_half_float_linear");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void oes_vertex_array_object() throws Exception {
        test("OES_vertex_array_object");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function OfflineAudioCompletionEvent() { [native code] }",
            IE = "exception")
    public void offlineAudioCompletionEvent() throws Exception {
        test("OfflineAudioCompletionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function OfflineAudioContext() { [native code] }",
            IE = "exception")
    public void offlineAudioContext() throws Exception {
        test("OfflineAudioContext");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void offlineResourceList() throws Exception {
        test("OfflineResourceList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function OffscreenCanvas() { [native code] }",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "exception",
            EDGE = "exception",
            FF = "exception")
    public void offscreenCanvas() throws Exception {
        test("OffscreenCanvas");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLOptionElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Option() { [native code] }",
            IE = "function Option() {\n    [native code]\n}\n")
    public void option() throws Exception {
        test("Option");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function OscillatorNode() { [native code] }",
            IE = "exception")
    public void oscillatorNode() throws Exception {
        test("OscillatorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void overflowEvent() throws Exception {
        test("OverflowEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PageTransitionEvent() { [native code] }",
            IE = "[object PageTransitionEvent]")
    public void pageTransitionEvent() throws Exception {
        test("PageTransitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PannerNode() { [native code] }",
            IE = "exception")
    public void pannerNode() throws Exception {
        test("PannerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void parallelArray() throws Exception {
        test("ParallelArray");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void parentNode() throws Exception {
        test("ParentNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function parseFloat() { [native code] }",
            IE = "function parseFloat() {\n    [native code]\n}\n")
    public void parseFloat() throws Exception {
        test("parseFloat");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function parseInt() { [native code] }",
            IE = "function parseInt() {\n    [native code]\n}\n")
    public void parseInt() throws Exception {
        test("parseInt");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PasswordCredential() { [native code] }",
            EDGE = "function PasswordCredential() { [native code] }")
    public void passwordCredential() throws Exception {
        test("PasswordCredential");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.canvas.Path2D}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Path2D() { [native code] }",
            IE = "exception")
    public void path2D() throws Exception {
        test("Path2D");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PaymentAddress() { [native code] }",
            EDGE = "function PaymentAddress() { [native code] }")
    public void paymentAddress() throws Exception {
        test("PaymentAddress");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PaymentRequest() { [native code] }",
            EDGE = "function PaymentRequest() { [native code] }")
    public void paymentRequest() throws Exception {
        test("PaymentRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PaymentResponse() { [native code] }",
            EDGE = "function PaymentResponse() { [native code] }")
    public void paymentResponse() throws Exception {
        test("PaymentResponse");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Performance() { [native code] }",
            IE = "[object Performance]")
    public void performance() throws Exception {
        test("Performance");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PerformanceEntry() { [native code] }",
            IE = "[object PerformanceEntry]")
    public void performanceEntry() throws Exception {
        test("PerformanceEntry");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void performanceFrameTiming() throws Exception {
        test("PerformanceFrameTiming");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PerformanceMark() { [native code] }",
            IE = "[object PerformanceMark]")
    public void performanceMark() throws Exception {
        test("PerformanceMark");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PerformanceMeasure() { [native code] }",
            IE = "[object PerformanceMeasure]")
    public void performanceMeasure() throws Exception {
        test("PerformanceMeasure");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PerformanceNavigation() { [native code] }",
            IE = "[object PerformanceNavigation]")
    public void performanceNavigation() throws Exception {
        test("PerformanceNavigation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PerformanceNavigationTiming() { [native code] }",
            IE = "[object PerformanceNavigationTiming]")
    public void performanceNavigationTiming() throws Exception {
        test("PerformanceNavigationTiming");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PerformanceObserver() { [native code] }",
            IE = "exception")
    public void performanceObserver() throws Exception {
        test("PerformanceObserver");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PerformanceObserverEntryList() { [native code] }",
            IE = "exception")
    public void performanceObserverEntryList() throws Exception {
        test("PerformanceObserverEntryList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PerformanceResourceTiming() { [native code] }",
            IE = "[object PerformanceResourceTiming]")
    public void performanceResourceTiming() throws Exception {
        test("PerformanceResourceTiming");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PerformanceTiming() { [native code] }",
            IE = "[object PerformanceTiming]")
    public void performanceTiming() throws Exception {
        test("PerformanceTiming");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void periodicSyncEvent() throws Exception {
        test("PeriodicSyncEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PeriodicSyncManager() { [native code] }",
            EDGE = "function PeriodicSyncManager() { [native code] }")
    public void periodicSyncManager() throws Exception {
        test("PeriodicSyncManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void periodicSyncRegistration() throws Exception {
        test("PeriodicSyncRegistration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PeriodicWave() { [native code] }",
            IE = "exception")
    public void periodicWave() throws Exception {
        test("PeriodicWave");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Permissions() { [native code] }",
            IE = "exception")
    public void permissions() throws Exception {
        test("Permissions");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void permissionSettings() throws Exception {
        test("PermissionSettings");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PermissionStatus() { [native code] }",
            IE = "exception")
    public void permissionStatus() throws Exception {
        test("PermissionStatus");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.Plugin}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Plugin() { [native code] }",
            IE = "[object Plugin]")
    public void plugin() throws Exception {
        test("Plugin");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.PluginArray}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function PluginArray() { [native code] }",
            IE = "[object PluginArray]")
    public void pluginArray() throws Exception {
        test("PluginArray");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void point() throws Exception {
        test("Point");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.PointerEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function PointerEvent() { [native code] }",
            IE = "[object PointerEvent]")
    public void pointerEvent() throws Exception {
        test("PointerEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PopStateEvent() { [native code] }",
            IE = "[object PopStateEvent]")
    public void popStateEvent() throws Exception {
        test("PopStateEvent");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void popup() throws Exception {
        test("Popup");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void portCollection() throws Exception {
        test("PortCollection");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.geo.Position}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object Position]")
    public void position() throws Exception {
        test("Position");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object PositionError]")
    public void positionError() throws Exception {
        test("PositionError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void positionOptions() throws Exception {
        test("PositionOptions");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void positionSensorVRDevice() throws Exception {
        test("PositionSensorVRDevice");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void powerManager() throws Exception {
        test("PowerManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function Presentation() { [native code] }",
            EDGE = "function Presentation() { [native code] }")
    public void presentation() throws Exception {
        test("Presentation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PresentationAvailability() { [native code] }",
            EDGE = "function PresentationAvailability() { [native code] }")
    public void presentationAvailability() throws Exception {
        test("PresentationAvailability");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PresentationConnection() { [native code] }",
            EDGE = "function PresentationConnection() { [native code] }")
    public void presentationConnection() throws Exception {
        test("PresentationConnection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PresentationConnectionAvailableEvent() { [native code] }",
            EDGE = "function PresentationConnectionAvailableEvent() { [native code] }")
    public void presentationConnectionAvailableEvent() throws Exception {
        test("PresentationConnectionAvailableEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void presentationConnectionClosedEvent() throws Exception {
        test("PresentationConnectionClosedEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PresentationConnectionCloseEvent() { [native code] }",
            EDGE = "function PresentationConnectionCloseEvent() { [native code] }")
    public void presentationConnectionCloseEvent() throws Exception {
        test("PresentationConnectionCloseEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PresentationConnectionList() { [native code] }",
            EDGE = "function PresentationConnectionList() { [native code] }")
    @HtmlUnitNYI(CHROME = "exception",
            EDGE = "exception")
    public void presentationConnectionList() throws Exception {
        test("PresentationConnectionList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PresentationReceiver() { [native code] }",
            EDGE = "function PresentationReceiver() { [native code] }")
    @HtmlUnitNYI(CHROME = "exception",
            EDGE = "exception")
    public void presentationReceiver() throws Exception {
        test("PresentationReceiver");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function PresentationRequest() { [native code] }",
            EDGE = "function PresentationRequest() { [native code] }")
    public void presentationRequest() throws Exception {
        test("PresentationRequest");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.ProcessingInstruction}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function ProcessingInstruction() { [native code] }",
            IE = "[object ProcessingInstruction]")
    public void processingInstruction() throws Exception {
        test("ProcessingInstruction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ProgressEvent() { [native code] }",
            IE = "[object ProgressEvent]")
    public void progressEvent() throws Exception {
        test("ProgressEvent");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Promise() { [native code] }",
            IE = "exception")
    public void promise() throws Exception {
        test("Promise");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void promiseRejection() throws Exception {
        test("PromiseRejection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PromiseRejectionEvent() { [native code] }",
            IE = "exception")
    public void promiseRejectionEvent() throws Exception {
        test("PromiseRejectionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void promiseResolver() throws Exception {
        test("PromiseResolver");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Proxy() { [native code] }",
            IE = "exception")
    public void proxy() throws Exception {
        test("Proxy");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void pushEvent() throws Exception {
        test("PushEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PushManager() { [native code] }",
            IE = "exception")
    public void pushManager() throws Exception {
        test("PushManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void pushMessageData() throws Exception {
        test("PushMessageData");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void pushRegistrationManager() throws Exception {
        test("PushRegistrationManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PushSubscription() { [native code] }",
            IE = "exception")
    public void pushSubscription() throws Exception {
        test("PushSubscription");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function PushSubscriptionOptions() { [native code] }",
            IE = "exception")
    public void pushSubscriptionOptions() throws Exception {
        test("PushSubscriptionOptions");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function RadioNodeList() { [native code] }",
            IE = "exception")
    public void radioNodeList() throws Exception {
        test("RadioNodeList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void randomSource() throws Exception {
        test("RandomSource");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.Range}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Range() { [native code] }",
            IE = "[object Range]")
    public void range() throws Exception {
        test("Range");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function RangeError() { [native code] }",
            IE = "function RangeError() {\n    [native code]\n}\n")
    public void rangeError() throws Exception {
        test("RangeError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void readableByteStream() throws Exception {
        test("ReadableByteStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ReadableStream() { [native code] }",
            IE = "exception")
    public void readableStream() throws Exception {
        test("ReadableStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ReferenceError() { [native code] }",
            IE = "function ReferenceError() {\n    [native code]\n}\n")
    public void referenceError() throws Exception {
        test("ReferenceError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object Reflect]",
            IE = "exception")
    public void reflect() throws Exception {
        test("Reflect");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function RegExp() { [native code] }",
            IE = "function RegExp() {\n    [native code]\n}\n")
    public void regExp() throws Exception {
        test("RegExp");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function RemotePlayback() { [native code] }",
            EDGE = "function RemotePlayback() { [native code] }")
    public void remotePlayback() throws Exception {
        test("RemotePlayback");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void renderingContext() throws Exception {
        test("RenderingContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Request() { [native code] }",
            IE = "exception")
    public void request() throws Exception {
        test("Request");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Response() { [native code] }",
            IE = "exception")
    public void response() throws Exception {
        test("Response");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.RowContainer}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void rowContainer() throws Exception {
        test("RowContainer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function RTCCertificate() { [native code] }",
            IE = "exception")
    public void rtcCertificate() throws Exception {
        test("RTCCertificate");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void rtcConfiguration() throws Exception {
        test("RTCConfiguration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function RTCDataChannel() { [native code] }",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "exception",
            EDGE = "exception",
            FF = "exception",
            FF_ESR = "exception")
    public void rtcDataChannel() throws Exception {
        test("RTCDataChannel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function RTCDataChannelEvent() { [native code] }",
            IE = "exception")
    public void rtcDataChannelEvent() throws Exception {
        test("RTCDataChannelEvent");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function RTCIceCandidate() { [native code] }",
            IE = "exception")
    public void rtcIceCandidate() throws Exception {
        test("RTCIceCandidate");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void rtcIceServer() throws Exception {
        test("RTCIceServer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void rtcIdentityAssertion() throws Exception {
        test("RTCIdentityAssertion");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void rtcIdentityErrorEvent() throws Exception {
        test("RTCIdentityErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void rtcIdentityEvent() throws Exception {
        test("RTCIdentityEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function RTCPeerConnection() { [native code] }",
            IE = "exception")
    public void rtcPeerConnection() throws Exception {
        test("RTCPeerConnection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function RTCPeerConnectionIceEvent() { [native code] }",
            IE = "exception")
    public void rtcPeerConnectionIceEvent() throws Exception {
        test("RTCPeerConnectionIceEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function RTCSctpTransport() { [native code] }",
            IE = "exception")
    public void rtcSctpTransport() throws Exception {
        test("RTCSctpTransport");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function RTCSessionDescription() { [native code] }",
            IE = "exception")
    public void rtcSessionDescription() throws Exception {
        test("RTCSessionDescription");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void rtcSessionDescriptionCallback() throws Exception {
        test("RTCSessionDescriptionCallback");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function RTCStatsReport() { [native code] }",
            IE = "exception")
    public void rtcStatsReport() throws Exception {
        test("RTCStatsReport");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.Screen}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Screen() { [native code] }",
            IE = "[object Screen]")
    public void screen() throws Exception {
        test("Screen");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ScreenOrientation() { [native code] }",
            IE = "exception")
    public void screenOrientation() throws Exception {
        test("ScreenOrientation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ScriptProcessorNode() { [native code] }",
            IE = "exception")
    public void scriptProcessorNode() throws Exception {
        test("ScriptProcessorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SecurityPolicyViolationEvent() { [native code] }",
            IE = "exception")
    public void securityPolicyViolationEvent() throws Exception {
        test("SecurityPolicyViolationEvent");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.Selection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Selection() { [native code] }",
            IE = "[object Selection]")
    public void selection() throws Exception {
        test("Selection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ServiceWorker() { [native code] }",
            IE = "exception")
    public void serviceWorker() throws Exception {
        test("ServiceWorker");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ServiceWorkerContainer() { [native code] }",
            IE = "exception")
    public void serviceWorkerContainer() throws Exception {
        test("ServiceWorkerContainer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void serviceWorkerGlobalScope() throws Exception {
        test("ServiceWorkerGlobalScope");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void serviceWorkerMessageEvent() throws Exception {
        test("ServiceWorkerMessageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ServiceWorkerRegistration() { [native code] }",
            IE = "exception")
    public void serviceWorkerRegistration() throws Exception {
        test("ServiceWorkerRegistration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void serviceWorkerState() throws Exception {
        test("ServiceWorkerState");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Set() { [native code] }",
            IE = "function Set() {\n    [native code]\n}\n")
    public void set() throws Exception {
        test("Set");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void settingsLock() throws Exception {
        test("SettingsLock");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void settingsManager() throws Exception {
        test("SettingsManager");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.ShadowRoot}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ShadowRoot() { [native code] }",
            IE = "exception")
    public void shadowRoot() throws Exception {
        test("ShadowRoot");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void sharedArrayBuffer() throws Exception {
        test("SharedArrayBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void sharedKeyframeList() throws Exception {
        test("SharedKeyframeList");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.SharedWorker}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SharedWorker() { [native code] }",
            IE = "exception")
    public void sharedWorker() throws Exception {
        test("SharedWorker");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void sharedWorkerGlobalScope() throws Exception {
        test("SharedWorkerGlobalScope");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd() throws Exception {
        test("SIMD");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_Bool16x8() throws Exception {
        test("SIMD.Bool16x8");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_Bool32x4() throws Exception {
        test("SIMD.Bool32x4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_Bool64x2() throws Exception {
        test("SIMD.Bool64x2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_Bool8x16() throws Exception {
        test("SIMD.Bool8x16");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_float32x4() throws Exception {
        test("SIMD.float32x4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_Float32x4() throws Exception {
        test("SIMD.Float32x4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_float64x2() throws Exception {
        test("SIMD.float64x2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_Float64x2() throws Exception {
        test("SIMD.Float64x2");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_int16x8() throws Exception {
        test("SIMD.int16x8");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_Int16x8() throws Exception {
        test("SIMD.Int16x8");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_int32x4() throws Exception {
        test("SIMD.int32x4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_Int32x4() throws Exception {
        test("SIMD.Int32x4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_int8x16() throws Exception {
        test("SIMD.int8x16");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_Int8x16() throws Exception {
        test("SIMD.Int8x16");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_Uint16x8() throws Exception {
        test("SIMD.Uint16x8");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_Uint32x4() throws Exception {
        test("SIMD.Uint32x4");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void simd_Uint8x16() throws Exception {
        test("SIMD.Uint8x16");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.SimpleArray}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void simpleArray() throws Exception {
        test("SimpleArray");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void siteBoundCredential() throws Exception {
        test("SiteBoundCredential");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SourceBuffer() { [native code] }",
            IE = "[object SourceBuffer]")
    @HtmlUnitNYI(IE = "function SourceBuffer() {\n    [native code]\n}\n")
    public void sourceBuffer() throws Exception {
        test("SourceBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SourceBufferList() { [native code] }",
            IE = "[object SourceBufferList]")
    @HtmlUnitNYI(IE = "function SourceBufferList() {\n    [native code]\n}\n")
    public void sourceBufferList() throws Exception {
        test("SourceBufferList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void speechGrammar() throws Exception {
        test("SpeechGrammar");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void speechGrammarList() throws Exception {
        test("SpeechGrammarList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void speechRecognition() throws Exception {
        test("SpeechRecognition");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void speechRecognitionAlternative() throws Exception {
        test("SpeechRecognitionAlternative");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void speechRecognitionError() throws Exception {
        test("SpeechRecognitionError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void speechRecognitionErrorEvent() throws Exception {
        test("SpeechRecognitionErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void speechRecognitionEvent() throws Exception {
        test("SpeechRecognitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void speechRecognitionResult() throws Exception {
        test("SpeechRecognitionResult");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void speechRecognitionResultList() throws Exception {
        test("SpeechRecognitionResultList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function SpeechSynthesis() { [native code] }",
            FF_ESR = "function SpeechSynthesis() { [native code] }")
    public void speechSynthesis() throws Exception {
        test("SpeechSynthesis");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SpeechSynthesisErrorEvent() { [native code] }",
            IE = "exception")
    public void speechSynthesisErrorEvent() throws Exception {
        test("SpeechSynthesisErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SpeechSynthesisEvent() { [native code] }",
            IE = "exception")
    public void speechSynthesisEvent() throws Exception {
        test("SpeechSynthesisEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SpeechSynthesisUtterance() { [native code] }",
            IE = "exception")
    public void speechSynthesisUtterance() throws Exception {
        test("SpeechSynthesisUtterance");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function SpeechSynthesisVoice() { [native code] }",
            FF_ESR = "function SpeechSynthesisVoice() { [native code] }")
    public void speechSynthesisVoice() throws Exception {
        test("SpeechSynthesisVoice");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void staticNodeList() throws Exception {
        test("StaticNodeList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function StereoPannerNode() { [native code] }",
            IE = "exception")
    public void stereoPannerNode() throws Exception {
        test("StereoPannerNode");
    }

    /**
     * Test {@code org.htmlunit.corejs.javascript.NativeIterator#StopIteration}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void stopIteration() throws Exception {
        test("StopIteration");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.Storage}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Storage() { [native code] }",
            IE  = "[object Storage]")
    public void storage() throws Exception {
        test("Storage");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void storageEstimate() throws Exception {
        test("StorageEstimate");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function StorageEvent() { [native code] }",
            IE = "[object StorageEvent]")
    public void storageEvent() throws Exception {
        test("StorageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function StorageManager() { [native code] }",
            IE = "exception")
    public void storageManager() throws Exception {
        test("StorageManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void storageQuota() throws Exception {
        test("StorageQuota");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function String() { [native code] }",
            IE = "function String() {\n    [native code]\n}\n")
    public void string() throws Exception {
        test("String");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object StyleMedia]")
    public void styleMedia() throws Exception {
        test("StyleMedia");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function StyleSheet() { [native code] }",
            IE = "[object StyleSheet]")
    public void styleSheet() throws Exception {
        test("StyleSheet");
    }

    /**
     * Test {@link StyleSheetList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function StyleSheetList() { [native code] }",
            IE = "[object StyleSheetList]")
    public void styleSheetList() throws Exception {
        test("StyleSheetList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SubmitEvent() { [native code] }",
            IE = "exception")
    public void submitEvent() throws Exception {
        test("SubmitEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SubtleCrypto() { [native code] }",
            IE = "[object SubtleCrypto]")
    public void subtleCrypto() throws Exception {
        test("SubtleCrypto");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGAElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGAElement() { [native code] }",
            IE = "[object SVGAElement]")
    public void svgAElement() throws Exception {
        test("SVGAElement");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void svgAltGlyphElement() throws Exception {
        test("SVGAltGlyphElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGAngle}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGAngle() { [native code] }",
            IE = "[object SVGAngle]")
    public void svgAngle() throws Exception {
        test("SVGAngle");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgAnimateColorElement() throws Exception {
        test("SVGAnimateColorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimatedAngle() { [native code] }",
            IE = "[object SVGAnimatedAngle]")
    public void svgAnimatedAngle() throws Exception {
        test("SVGAnimatedAngle");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimatedBoolean() { [native code] }",
            IE = "[object SVGAnimatedBoolean]")
    public void svgAnimatedBoolean() throws Exception {
        test("SVGAnimatedBoolean");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimatedEnumeration() { [native code] }",
            IE = "[object SVGAnimatedEnumeration]")
    public void svgAnimatedEnumeration() throws Exception {
        test("SVGAnimatedEnumeration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimatedInteger() { [native code] }",
            IE = "[object SVGAnimatedInteger]")
    public void svgAnimatedInteger() throws Exception {
        test("SVGAnimatedInteger");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimatedLength() { [native code] }",
            IE = "[object SVGAnimatedLength]")
    public void svgAnimatedLength() throws Exception {
        test("SVGAnimatedLength");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimatedLengthList() { [native code] }",
            IE = "[object SVGAnimatedLengthList]")
    public void svgAnimatedLengthList() throws Exception {
        test("SVGAnimatedLengthList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimatedNumber() { [native code] }",
            IE = "[object SVGAnimatedNumber]")
    public void svgAnimatedNumber() throws Exception {
        test("SVGAnimatedNumber");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimatedNumberList() { [native code] }",
            IE = "[object SVGAnimatedNumberList]")
    public void svgAnimatedNumberList() throws Exception {
        test("SVGAnimatedNumberList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgAnimatedPoints() throws Exception {
        test("SVGAnimatedPoints");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimatedPreserveAspectRatio() { [native code] }",
            IE = "[object SVGAnimatedPreserveAspectRatio]")
    public void svgAnimatedPreserveAspectRatio() throws Exception {
        test("SVGAnimatedPreserveAspectRatio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimatedRect() { [native code] }",
            IE = "[object SVGAnimatedRect]")
    public void svgAnimatedRect() throws Exception {
        test("SVGAnimatedRect");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimatedString() { [native code] }",
            IE = "[object SVGAnimatedString]")
    public void svgAnimatedString() throws Exception {
        test("SVGAnimatedString");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimatedTransformList() { [native code] }",
            IE = "[object SVGAnimatedTransformList]")
    public void svgAnimatedTransformList() throws Exception {
        test("SVGAnimatedTransformList");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGAnimateElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimateElement() { [native code] }",
            IE = "exception")
    public void svgAnimateElement() throws Exception {
        test("SVGAnimateElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGAnimateMotionElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimateMotionElement() { [native code] }",
            IE = "exception")
    public void svgAnimateMotionElement() throws Exception {
        test("SVGAnimateMotionElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGAnimateTransformElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimateTransformElement() { [native code] }",
            IE = "exception")
    public void svgAnimateTransformElement() throws Exception {
        test("SVGAnimateTransformElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGAnimationElement() { [native code] }",
            IE = "exception")
    public void svgAnimationElement() throws Exception {
        test("SVGAnimationElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGCircleElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGCircleElement() { [native code] }",
            IE = "[object SVGCircleElement]")
    public void svgCircleElement() throws Exception {
        test("SVGCircleElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGClipPathElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGClipPathElement() { [native code] }",
            IE = "[object SVGClipPathElement]")
    public void svgClipPathElement() throws Exception {
        test("SVGClipPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGComponentTransferFunctionElement() { [native code] }",
            IE = "[object SVGComponentTransferFunctionElement]")
    public void svgComponentTransferFunctionElement() throws Exception {
        test("SVGComponentTransferFunctionElement");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void svgCursorElement() throws Exception {
        test("SVGCursorElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGDefsElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGDefsElement() { [native code] }",
            IE = "[object SVGDefsElement]")
    public void svgDefsElement() throws Exception {
        test("SVGDefsElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGDescElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGDescElement() { [native code] }",
            IE = "[object SVGDescElement]")
    public void svgDescElement() throws Exception {
        test("SVGDescElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgDiscardElement() throws Exception {
        test("SVGDiscardElement");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void svgDocument() throws Exception {
        test("SVGDocument");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGElement() { [native code] }",
            IE = "[object SVGElement]")
    public void svgElement() throws Exception {
        test("SVGElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGEllipseElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGEllipseElement() { [native code] }",
            IE = "[object SVGEllipseElement]")
    public void svgEllipseElement() throws Exception {
        test("SVGEllipseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgEvent() throws Exception {
        test("SVGEvent");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEBlendElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEBlendElement() { [native code] }",
            IE = "[object SVGFEBlendElement]")
    public void svgFEBlendElement() throws Exception {
        test("SVGFEBlendElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEColorMatrixElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEColorMatrixElement() { [native code] }",
            IE = "[object SVGFEColorMatrixElement]")
    public void svgFEColorMatrixElement() throws Exception {
        test("SVGFEColorMatrixElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEComponentTransferElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEComponentTransferElement() { [native code] }",
            IE = "[object SVGFEComponentTransferElement]")
    public void svgFEComponentTransferElement() throws Exception {
        test("SVGFEComponentTransferElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFECompositeElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFECompositeElement() { [native code] }",
            IE = "[object SVGFECompositeElement]")
    public void svgFECompositeElement() throws Exception {
        test("SVGFECompositeElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEConvolveMatrixElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEConvolveMatrixElement() { [native code] }",
            IE = "[object SVGFEConvolveMatrixElement]")
    public void svgFEConvolveMatrixElement() throws Exception {
        test("SVGFEConvolveMatrixElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEDiffuseLightingElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEDiffuseLightingElement() { [native code] }",
            IE = "[object SVGFEDiffuseLightingElement]")
    public void svgFEDiffuseLightingElement() throws Exception {
        test("SVGFEDiffuseLightingElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEDisplacementMapElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEDisplacementMapElement() { [native code] }",
            IE = "[object SVGFEDisplacementMapElement]")
    public void svgFEDisplacementMapElement() throws Exception {
        test("SVGFEDisplacementMapElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEDistantLightElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEDistantLightElement() { [native code] }",
            IE = "[object SVGFEDistantLightElement]")
    public void svgFEDistantLightElement() throws Exception {
        test("SVGFEDistantLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEDropShadowElement() { [native code] }",
            IE = "exception")
    public void svgFEDropShadowElement() throws Exception {
        test("SVGFEDropShadowElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEFloodElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEFloodElement() { [native code] }",
            IE = "[object SVGFEFloodElement]")
    public void svgFEFloodElement() throws Exception {
        test("SVGFEFloodElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEFuncAElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEFuncAElement() { [native code] }",
            IE = "[object SVGFEFuncAElement]")
    public void svgFEFuncAElement() throws Exception {
        test("SVGFEFuncAElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEFuncBElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEFuncBElement() { [native code] }",
            IE = "[object SVGFEFuncBElement]")
    public void svgFEFuncBElement() throws Exception {
        test("SVGFEFuncBElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEFuncGElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEFuncGElement() { [native code] }",
            IE = "[object SVGFEFuncGElement]")
    public void svgFEFuncGElement() throws Exception {
        test("SVGFEFuncGElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEFuncRElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEFuncRElement() { [native code] }",
            IE = "[object SVGFEFuncRElement]")
    public void svgFEFuncRElement() throws Exception {
        test("SVGFEFuncRElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEGaussianBlurElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEGaussianBlurElement() { [native code] }",
            IE = "[object SVGFEGaussianBlurElement]")
    public void svgFEGaussianBlurElement() throws Exception {
        test("SVGFEGaussianBlurElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEImageElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEImageElement() { [native code] }",
            IE = "[object SVGFEImageElement]")
    public void svgFEImageElement() throws Exception {
        test("SVGFEImageElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEMergeElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEMergeElement() { [native code] }",
            IE = "[object SVGFEMergeElement]")
    public void svgFEMergeElement() throws Exception {
        test("SVGFEMergeElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEMergeNodeElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEMergeNodeElement() { [native code] }",
            IE = "[object SVGFEMergeNodeElement]")
    public void svgFEMergeNodeElement() throws Exception {
        test("SVGFEMergeNodeElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEMorphologyElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEMorphologyElement() { [native code] }",
            IE = "[object SVGFEMorphologyElement]")
    public void svgFEMorphologyElement() throws Exception {
        test("SVGFEMorphologyElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEOffsetElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEOffsetElement() { [native code] }",
            IE = "[object SVGFEOffsetElement]")
    public void svgFEOffsetElement() throws Exception {
        test("SVGFEOffsetElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEPointLightElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFEPointLightElement() { [native code] }",
            IE = "[object SVGFEPointLightElement]")
    public void svgFEPointLightElement() throws Exception {
        test("SVGFEPointLightElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFESpecularLightingElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFESpecularLightingElement() { [native code] }",
            IE = "[object SVGFESpecularLightingElement]")
    public void svgFESpecularLightingElement() throws Exception {
        test("SVGFESpecularLightingElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFESpotLightElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFESpotLightElement() { [native code] }",
            IE = "[object SVGFESpotLightElement]")
    public void svgFESpotLightElement() throws Exception {
        test("SVGFESpotLightElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFETileElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFETileElement() { [native code] }",
            IE = "[object SVGFETileElement]")
    public void svgFETileElement() throws Exception {
        test("SVGFETileElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFETurbulenceElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFETurbulenceElement() { [native code] }",
            IE = "[object SVGFETurbulenceElement]")
    public void svgFETurbulenceElement() throws Exception {
        test("SVGFETurbulenceElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFilterElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGFilterElement() { [native code] }",
            IE = "[object SVGFilterElement]")
    public void svgFilterElement() throws Exception {
        test("SVGFilterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgFontElement() throws Exception {
        test("SVGFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgFontFaceElement() throws Exception {
        test("SVGFontFaceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgFontFaceFormatElement() throws Exception {
        test("SVGFontFaceFormatElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgFontFaceNameElement() throws Exception {
        test("SVGFontFaceNameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgFontFaceSrcElement() throws Exception {
        test("SVGFontFaceSrcElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgFontFaceUriElement() throws Exception {
        test("SVGFontFaceUriElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGForeignObjectElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGForeignObjectElement() { [native code] }",
            IE = "exception")
    public void svgForeignObjectElement() throws Exception {
        test("SVGForeignObjectElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGGElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGGElement() { [native code] }",
            IE = "[object SVGGElement]")
    public void svgGElement() throws Exception {
        test("SVGGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGGeometryElement() { [native code] }",
            IE = "exception")
    public void svgGeometryElement() throws Exception {
        test("SVGGeometryElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgGlyphElement() throws Exception {
        test("SVGGlyphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGGradientElement() { [native code] }",
            IE = "[object SVGGradientElement]")
    public void svgGradientElement() throws Exception {
        test("SVGGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGGraphicsElement() { [native code] }",
            IE = "exception")
    public void svgGraphicsElement() throws Exception {
        test("SVGGraphicsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgHKernElement() throws Exception {
        test("SVGHKernElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGImageElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGImageElement() { [native code] }",
            IE = "[object SVGImageElement]")
    public void svgImageElement() throws Exception {
        test("SVGImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGLength() { [native code] }",
            IE = "[object SVGLength]")
    public void svgLength() throws Exception {
        test("SVGLength");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGLengthList() { [native code] }",
            IE = "[object SVGLengthList]")
    public void svgLengthList() throws Exception {
        test("SVGLengthList");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGLinearGradientElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGLinearGradientElement() { [native code] }",
            IE = "[object SVGLinearGradientElement]")
    public void svgLinearGradientElement() throws Exception {
        test("SVGLinearGradientElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGLineElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGLineElement() { [native code] }",
            IE = "[object SVGLineElement]")
    public void svgLineElement() throws Exception {
        test("SVGLineElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGMarkerElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGMarkerElement() { [native code] }",
            IE = "[object SVGMarkerElement]")
    public void svgMarkerElement() throws Exception {
        test("SVGMarkerElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGMaskElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGMaskElement() { [native code] }",
            IE = "[object SVGMaskElement]")
    public void svgMaskElement() throws Exception {
        test("SVGMaskElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGMatrix}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGMatrix() { [native code] }",
            IE = "[object SVGMatrix]")
    public void svgMatrix() throws Exception {
        test("SVGMatrix");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGMetadataElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGMetadataElement() { [native code] }",
            IE = "[object SVGMetadataElement]")
    public void svgMetadataElement() throws Exception {
        test("SVGMetadataElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgMissingGlyphElement() throws Exception {
        test("SVGMissingGlyphElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGMPathElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGMPathElement() { [native code] }",
            IE = "exception")
    public void svgMPathElement() throws Exception {
        test("SVGMPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGNumber() { [native code] }",
            IE = "[object SVGNumber]")
    public void svgNumber() throws Exception {
        test("SVGNumber");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGNumberList() { [native code] }",
            IE = "[object SVGNumberList]")
    public void svgNumberList() throws Exception {
        test("SVGNumberList");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGPathElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGPathElement() { [native code] }",
            IE = "[object SVGPathElement]")
    public void svgPathElement() throws Exception {
        test("SVGPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object SVGPathSeg]")
    public void svgPathSeg() throws Exception {
        test("SVGPathSeg");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object SVGPathSegArcAbs]")
    public void svgPathSegArcAbs() throws Exception {
        test("SVGPathSegArcAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object SVGPathSegArcRel]")
    public void svgPathSegArcRel() throws Exception {
        test("SVGPathSegArcRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object SVGPathSegClosePath]")
    public void svgPathSegClosePath() throws Exception {
        test("SVGPathSegClosePath");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object SVGPathSegCurvetoCubicAbs]")
    public void svgPathSegCurvetoCubicAbs() throws Exception {
        test("SVGPathSegCurvetoCubicAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object SVGPathSegCurvetoCubicRel]")
    public void svgPathSegCurvetoCubicRel() throws Exception {
        test("SVGPathSegCurvetoCubicRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object SVGPathSegCurvetoCubicSmoothAbs]")
    public void svgPathSegCurvetoCubicSmoothAbs() throws Exception {
        test("SVGPathSegCurvetoCubicSmoothAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object SVGPathSegCurvetoCubicSmoothRel]")
    public void svgPathSegCurvetoCubicSmoothRel() throws Exception {
        test("SVGPathSegCurvetoCubicSmoothRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object SVGPathSegCurvetoQuadraticAbs]")
    public void svgPathSegCurvetoQuadraticAbs() throws Exception {
        test("SVGPathSegCurvetoQuadraticAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object SVGPathSegCurvetoQuadraticRel]")
    public void svgPathSegCurvetoQuadraticRel() throws Exception {
        test("SVGPathSegCurvetoQuadraticRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object SVGPathSegCurvetoQuadraticSmoothAbs]")
    public void svgPathSegCurvetoQuadraticSmoothAbs() throws Exception {
        test("SVGPathSegCurvetoQuadraticSmoothAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object SVGPathSegCurvetoQuadraticSmoothRel]")
    public void svgPathSegCurvetoQuadraticSmoothRel() throws Exception {
        test("SVGPathSegCurvetoQuadraticSmoothRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object SVGPathSegLinetoAbs]")
    public void svgPathSegLinetoAbs() throws Exception {
        test("SVGPathSegLinetoAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object SVGPathSegLinetoHorizontalAbs]")
    public void svgPathSegLinetoHorizontalAbs() throws Exception {
        test("SVGPathSegLinetoHorizontalAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object SVGPathSegLinetoHorizontalRel]")
    public void svgPathSegLinetoHorizontalRel() throws Exception {
        test("SVGPathSegLinetoHorizontalRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object SVGPathSegLinetoRel]")
    public void svgPathSegLinetoRel() throws Exception {
        test("SVGPathSegLinetoRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object SVGPathSegLinetoVerticalAbs]")
    public void svgPathSegLinetoVerticalAbs() throws Exception {
        test("SVGPathSegLinetoVerticalAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object SVGPathSegLinetoVerticalRel]")
    public void svgPathSegLinetoVerticalRel() throws Exception {
        test("SVGPathSegLinetoVerticalRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object SVGPathSegList]")
    public void svgPathSegList() throws Exception {
        test("SVGPathSegList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object SVGPathSegMovetoAbs]")
    public void svgPathSegMovetoAbs() throws Exception {
        test("SVGPathSegMovetoAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object SVGPathSegMovetoRel]")
    public void svgPathSegMovetoRel() throws Exception {
        test("SVGPathSegMovetoRel");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGPatternElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGPatternElement() { [native code] }",
            IE = "[object SVGPatternElement]")
    public void svgPatternElement() throws Exception {
        test("SVGPatternElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPoint() { [native code] }",
            IE = "[object SVGPoint]")
    public void svgPoint() throws Exception {
        test("SVGPoint");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPointList() { [native code] }",
            IE = "[object SVGPointList]")
    public void svgPointList() throws Exception {
        test("SVGPointList");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGPolygonElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGPolygonElement() { [native code] }",
            IE = "[object SVGPolygonElement]")
    public void svgPolygonElement() throws Exception {
        test("SVGPolygonElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGPolylineElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGPolylineElement() { [native code] }",
            IE = "[object SVGPolylineElement]")
    public void svgPolylineElement() throws Exception {
        test("SVGPolylineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGPreserveAspectRatio() { [native code] }",
            IE = "[object SVGPreserveAspectRatio]")
    public void svgPreserveAspectRatio() throws Exception {
        test("SVGPreserveAspectRatio");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGRadialGradientElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGRadialGradientElement() { [native code] }",
            IE = "[object SVGRadialGradientElement]")
    public void svgRadialGradientElement() throws Exception {
        test("SVGRadialGradientElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGRect}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGRect() { [native code] }",
            IE = "[object SVGRect]")
    public void svgRect() throws Exception {
        test("SVGRect");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGRectElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGRectElement() { [native code] }",
            IE = "[object SVGRectElement]")
    public void svgRectElement() throws Exception {
        test("SVGRectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgRenderingIntent() throws Exception {
        test("SVGRenderingIntent");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGScriptElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGScriptElement() { [native code] }",
            IE = "[object SVGScriptElement]")
    public void svgScriptElement() throws Exception {
        test("SVGScriptElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGSetElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGSetElement() { [native code] }",
            IE = "exception")
    public void svgSetElement() throws Exception {
        test("SVGSetElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGStopElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGStopElement() { [native code] }",
            IE = "[object SVGStopElement]")
    public void svgStopElement() throws Exception {
        test("SVGStopElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGStringList() { [native code] }",
            IE = "[object SVGStringList]")
    public void svgStringList() throws Exception {
        test("SVGStringList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgStylable() throws Exception {
        test("SVGStylable");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGStyleElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGStyleElement() { [native code] }",
            IE = "[object SVGStyleElement]")
    public void svgStyleElement() throws Exception {
        test("SVGStyleElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGSVGElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGSVGElement() { [native code] }",
            IE = "[object SVGSVGElement]")
    public void svgSVGElement() throws Exception {
        test("SVGSVGElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGSwitchElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGSwitchElement() { [native code] }",
            IE = "[object SVGSwitchElement]")
    public void svgSwitchElement() throws Exception {
        test("SVGSwitchElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGSymbolElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGSymbolElement() { [native code] }",
            IE = "[object SVGSymbolElement]")
    public void svgSymbolElement() throws Exception {
        test("SVGSymbolElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgTests() throws Exception {
        test("SVGTests");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGTextContentElement() { [native code] }",
            IE = "[object SVGTextContentElement]")
    public void svgTextContentElement() throws Exception {
        test("SVGTextContentElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGTextElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGTextElement() { [native code] }",
            IE = "[object SVGTextElement]")
    public void svgTextElement() throws Exception {
        test("SVGTextElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGTextPathElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGTextPathElement() { [native code] }",
            IE = "[object SVGTextPathElement]")
    public void svgTextPathElement() throws Exception {
        test("SVGTextPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGTextPositioningElement() { [native code] }",
            IE = "[object SVGTextPositioningElement]")
    public void svgTextPositioningElement() throws Exception {
        test("SVGTextPositioningElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGTitleElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGTitleElement() { [native code] }",
            IE = "[object SVGTitleElement]")
    public void svgTitleElement() throws Exception {
        test("SVGTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGTransform() { [native code] }",
            IE = "[object SVGTransform]")
    public void svgTransform() throws Exception {
        test("SVGTransform");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgTransformable() throws Exception {
        test("SVGTransformable");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGTransformList() { [native code] }",
            IE = "[object SVGTransformList]")
    public void svgTransformList() throws Exception {
        test("SVGTransformList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgTRefElement() throws Exception {
        test("SVGTRefElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGTSpanElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGTSpanElement() { [native code] }",
            IE = "[object SVGTSpanElement]")
    public void svgTSpanElement() throws Exception {
        test("SVGTSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SVGUnitTypes() { [native code] }",
            IE = "[object SVGUnitTypes]")
    public void svgUnitTypes() throws Exception {
        test("SVGUnitTypes");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGUseElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGUseElement() { [native code] }",
            IE = "[object SVGUseElement]")
    public void svgUseElement() throws Exception {
        test("SVGUseElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGViewElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function SVGViewElement() { [native code] }",
            IE = "[object SVGViewElement]")
    public void svgViewElement() throws Exception {
        test("SVGViewElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgViewSpec() throws Exception {
        test("SVGViewSpec");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgVKernElement() throws Exception {
        test("SVGVKernElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object SVGZoomEvent]")
    public void svgZoomEvent() throws Exception {
        test("SVGZoomEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Symbol() { [native code] }",
            IE = "exception")
    public void symbol() throws Exception {
        test("Symbol");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void syncEvent() throws Exception {
        test("SyncEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SyncManager() { [native code] }",
            EDGE = "function SyncManager() { [native code] }")
    public void syncManager() throws Exception {
        test("SyncManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void syncRegistration() throws Exception {
        test("SyncRegistration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function SyntaxError() { [native code] }",
            IE = "function SyntaxError() {\n    [native code]\n}\n")
    public void syntaxError() throws Exception {
        test("SyntaxError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void tcpServerSocket() throws Exception {
        test("TCPServerSocket");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void tcpSocket() throws Exception {
        test("TCPSocket");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void telephony() throws Exception {
        test("Telephony");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void telephonyCall() throws Exception {
        test("TelephonyCall");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void telephonyCallGroup() throws Exception {
        test("TelephonyCallGroup");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.Text}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Text() { [native code] }",
            IE = "[object Text]")
    public void text() throws Exception {
        test("Text");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function TextDecoder() { [native code] }",
            IE = "exception")
    public void textDecoder() throws Exception {
        test("TextDecoder");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function TextEncoder() { [native code] }",
            IE = "exception")
    public void textEncoder() throws Exception {
        test("TextEncoder");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function TextEvent() { [native code] }",
            FF = "exception",
            IE = "[object TextEvent]",
            FF_ESR = "exception")
    public void textEvent() throws Exception {
        test("TextEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function TextMetrics() { [native code] }",
            IE = "[object TextMetrics]")
    public void textMetrics() throws Exception {
        test("TextMetrics");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.TextRange}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object TextRange]")
    public void textRange() throws Exception {
        test("TextRange");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function TextTrack() { [native code] }",
            IE = "[object TextTrack]")
    public void textTrack() throws Exception {
        test("TextTrack");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function TextTrackCue() { [native code] }",
            IE = "function TextTrackCue() {\n    [native code]\n}\n")
    public void textTrackCue() throws Exception {
        test("TextTrackCue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function TextTrackCueList() { [native code] }",
            IE = "[object TextTrackCueList]")
    public void textTrackCueList() throws Exception {
        test("TextTrackCueList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function TextTrackList() { [native code] }",
            IE = "[object TextTrackList]")
    public void textTrackList() throws Exception {
        test("TextTrackList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function TimeEvent() { [native code] }",
            FF_ESR = "function TimeEvent() { [native code] }")
    public void timeEvent() throws Exception {
        test("TimeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function TimeRanges() { [native code] }",
            IE = "[object TimeRanges]")
    public void timeRanges() throws Exception {
        test("TimeRanges");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function Touch() { [native code] }",
            EDGE = "function Touch() { [native code] }")
    public void touch() throws Exception {
        test("Touch");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function TouchEvent() { [native code] }",
            EDGE = "function TouchEvent() { [native code] }")
    public void touchEvent() throws Exception {
        test("TouchEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function TouchList() { [native code] }",
            EDGE = "function TouchList() { [native code] }")
    public void touchList() throws Exception {
        test("TouchList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void trackDefault() throws Exception {
        test("TrackDefault");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void trackDefaultList() throws Exception {
        test("TrackDefaultList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function TrackEvent() { [native code] }",
            IE = "[object TrackEvent]")
    public void trackEvent() throws Exception {
        test("TrackEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void transferable() throws Exception {
        test("Transferable");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function TransitionEvent() { [native code] }",
            IE = "[object TransitionEvent]")
    public void transitionEvent() throws Exception {
        test("TransitionEvent");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.TreeWalker}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function TreeWalker() { [native code] }",
            IE = "[object TreeWalker]")
    public void treeWalker() throws Exception {
        test("TreeWalker");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void typedArray() throws Exception {
        test("TypedArray");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function TypeError() { [native code] }",
            IE = "function TypeError() {\n    [native code]\n}\n")
    public void typeError() throws Exception {
        test("TypeError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void typeInfo() throws Exception {
        test("TypeInfo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void uDPSocket() throws Exception {
        test("UDPSocket");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.UIEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function UIEvent() { [native code] }",
            IE = "[object UIEvent]")
    public void uiEvent() throws Exception {
        test("UIEvent");
    }

    /**
     * Test Uint16Array.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Uint16Array() { [native code] }",
            IE = "function Uint16Array() {\n    [native code]\n}\n")
    public void uint16Array() throws Exception {
        test("Uint16Array");
    }

    /**
     * Test Uint32Array.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Uint32Array() { [native code] }",
            IE = "function Uint32Array() {\n    [native code]\n}\n")
    public void uint32Array() throws Exception {
        test("Uint32Array");
    }

    /**
     * Test Uint8Array.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Uint8Array() { [native code] }",
            IE = "function Uint8Array() {\n    [native code]\n}\n")
    public void uint8Array() throws Exception {
        test("Uint8Array");
    }

    /**
     * Test Uint8ClampedArray.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Uint8ClampedArray() { [native code] }",
            IE = "function Uint8ClampedArray() {\n    [native code]\n}\n")
    public void uint8ClampedArray() throws Exception {
        test("Uint8ClampedArray");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void undefined() throws Exception {
        test("undefined");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function unescape() { [native code] }",
            IE = "function unescape() {\n    [native code]\n}\n")
    public void unescape() throws Exception {
        test("unescape");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void uneval() throws Exception {
        test("uneval");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function URIError() { [native code] }",
            IE = "function URIError() {\n    [native code]\n}\n")
    public void uriError() throws Exception {
        test("URIError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function URL() { [native code] }",
            IE = "[object URL]")
    public void url() throws Exception {
        test("URL");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.URLSearchParams}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function URLSearchParams() { [native code] }",
            IE = "exception")
    public void urlSearchParams() throws Exception {
        test("URLSearchParams");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void urlUtils() throws Exception {
        test("URLUtils");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void urlUtilsReadOnly() throws Exception {
        test("URLUtilsReadOnly");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void userDataHandler() throws Exception {
        test("UserDataHandler");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void userProximityEvent() throws Exception {
        test("UserProximityEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void uSVString() throws Exception {
        test("USVString");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function ValidityState() { [native code] }",
            IE = "[object ValidityState]")
    public void validityState() throws Exception {
        test("ValidityState");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function VideoPlaybackQuality() { [native code] }",
            IE = "[object VideoPlaybackQuality]")
    public void videoPlaybackQuality() throws Exception {
        test("VideoPlaybackQuality");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void vrDevice() throws Exception {
        test("VRDevice");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void vrDisplay() throws Exception {
        test("VRDisplay");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void vrDisplayCapabilities() throws Exception {
        test("VRDisplayCapabilities");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void vrEyeParameters() throws Exception {
        test("VREyeParameters");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void vrFieldOfView() throws Exception {
        test("VRFieldOfView");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void vrFieldOfViewReadOnly() throws Exception {
        test("VRFieldOfViewReadOnly");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void vrLayer() throws Exception {
        test("VRLayer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void vrPose() throws Exception {
        test("VRPose");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void vrPositionState() throws Exception {
        test("VRPositionState");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void vrStageParameters() throws Exception {
        test("VRStageParameters");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function VTTCue() { [native code] }",
            IE = "exception")
    public void vTTCue() throws Exception {
        test("VTTCue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WaveShaperNode() { [native code] }",
            IE = "exception")
    public void waveShaperNode() throws Exception {
        test("WaveShaperNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WeakMap() { [native code] }",
            IE = "function WeakMap() {\n    [native code]\n}\n")
    public void weakMap() throws Exception {
        test("WeakMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WeakSet() { [native code] }",
            IE = "exception")
    public void weakSet() throws Exception {
        test("WeakSet");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webGL() throws Exception {
        test("WebGL");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webGL_color_buffer_float() throws Exception {
        test("WEBGL_color_buffer_float");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webGL_compressed_texture_atc() throws Exception {
        test("WEBGL_compressed_texture_atc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webGL_compressed_texture_es3() throws Exception {
        test("WEBGL_compressed_texture_es3");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void wEBGL_compressed_texture_etc() throws Exception {
        test("WEBGL_compressed_texture_etc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webGL_compressed_texture_etc1() throws Exception {
        test("WEBGL_compressed_texture_etc1");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webGL_compressed_texture_pvrtc() throws Exception {
        test("WEBGL_compressed_texture_pvrtc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object WEBGL_compressed_texture_s3tc]")
    public void webGL_compressed_texture_s3tc() throws Exception {
        test("WEBGL_compressed_texture_s3tc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            IE = "[object WEBGL_debug_renderer_info]")
    public void webGL_debug_renderer_info() throws Exception {
        test("WEBGL_debug_renderer_info");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webGL_debug_shaders() throws Exception {
        test("WEBGL_debug_shaders");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webGL_depth_texture() throws Exception {
        test("WEBGL_depth_texture");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webGL_draw_buffers() throws Exception {
        test("WEBGL_draw_buffers");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webGL_lose_context() throws Exception {
        test("WEBGL_lose_context");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGL2RenderingContext() { [native code] }",
            IE = "exception")
    public void webGL2RenderingContext() throws Exception {
        test("WebGL2RenderingContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLActiveInfo() { [native code] }",
            IE = "[object WebGLActiveInfo]")
    public void webGLActiveInfo() throws Exception {
        test("WebGLActiveInfo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLBuffer() { [native code] }",
            IE = "[object WebGLBuffer]")
    public void webGLBuffer() throws Exception {
        test("WebGLBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLContextEvent() { [native code] }",
            IE = "function WebGLContextEvent() {\n    [native code]\n}\n")
    public void webGLContextEvent() throws Exception {
        test("WebGLContextEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLFramebuffer() { [native code] }",
            IE = "[object WebGLFramebuffer]")
    public void webGLFramebuffer() throws Exception {
        test("WebGLFramebuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLProgram() { [native code] }",
            IE = "[object WebGLProgram]")
    public void webGLProgram() throws Exception {
        test("WebGLProgram");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLQuery() { [native code] }",
            IE = "exception")
    public void webGLQuery() throws Exception {
        test("WebGLQuery");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLRenderbuffer() { [native code] }",
            IE = "[object WebGLRenderbuffer]")
    public void webGLRenderbuffer() throws Exception {
        test("WebGLRenderbuffer");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.canvas.WebGLRenderingContext}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLRenderingContext() { [native code] }",
            IE = "[object WebGLRenderingContext]")
    public void webGLRenderingContext() throws Exception {
        test("WebGLRenderingContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLSampler() { [native code] }",
            IE = "exception")
    public void webGLSampler() throws Exception {
        test("WebGLSampler");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLShader() { [native code] }",
            IE = "[object WebGLShader]")
    public void webGLShader() throws Exception {
        test("WebGLShader");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLShaderPrecisionFormat() { [native code] }",
            IE = "[object WebGLShaderPrecisionFormat]")
    public void webGLShaderPrecisionFormat() throws Exception {
        test("WebGLShaderPrecisionFormat");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLSync() { [native code] }",
            IE = "exception")
    public void webGLSync() throws Exception {
        test("WebGLSync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLTexture() { [native code] }",
            IE = "[object WebGLTexture]")
    public void webGLTexture() throws Exception {
        test("WebGLTexture");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webGLTimerQueryEXT() throws Exception {
        test("WebGLTimerQueryEXT");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLTransformFeedback() { [native code] }",
            IE = "exception")
    public void webGLTransformFeedback() throws Exception {
        test("WebGLTransformFeedback");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLUniformLocation() { [native code] }",
            IE = "[object WebGLUniformLocation]")
    public void webGLUniformLocation() throws Exception {
        test("WebGLUniformLocation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WebGLVertexArrayObject() { [native code] }",
            IE = "exception")
    public void webGLVertexArrayObject() throws Exception {
        test("WebGLVertexArrayObject");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webGLVertexArrayObjectOES() throws Exception {
        test("WebGLVertexArrayObjectOES");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webKitAnimationEvent() throws Exception {
        test("WebKitAnimationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webkitAudioContext() throws Exception {
        test("webkitAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function DOMMatrix() { [native code] }",
            IE = "exception")
    @HtmlUnitNYI(CHROME = "function WebKitCSSMatrix() { [native code] }",
            EDGE = "function WebKitCSSMatrix() { [native code] }",
            FF = "function WebKitCSSMatrix() {\n    [native code]\n}",
            FF_ESR = "function WebKitCSSMatrix() {\n    [native code]\n}")
    public void webKitCSSMatrix() throws Exception {
        test("WebKitCSSMatrix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webkitIDBCursor() throws Exception {
        test("webkitIDBCursor");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webkitIDBDatabase() throws Exception {
        test("webkitIDBDatabase");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webkitIDBFactory() throws Exception {
        test("webkitIDBFactory");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webkitIDBIndex() throws Exception {
        test("webkitIDBIndex");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webkitIDBKeyRange() throws Exception {
        test("webkitIDBKeyRange");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webkitIDBObjectStore() throws Exception {
        test("webkitIDBObjectStore");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webkitIDBRequest() throws Exception {
        test("webkitIDBRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webkitIDBTransaction() throws Exception {
        test("webkitIDBTransaction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MediaStream() { [native code] }",
            EDGE = "function MediaStream() { [native code] }")
    public void webkitMediaStream() throws Exception {
        test("webkitMediaStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function MutationObserver() { [native code] }",
            EDGE = "function MutationObserver() { [native code] }")
    public void webKitMutationObserver() throws Exception {
        test("WebKitMutationObserver");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webkitOfflineAudioContext() throws Exception {
        test("webkitOfflineAudioContext");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function RTCPeerConnection() { [native code] }",
            EDGE = "function RTCPeerConnection() { [native code] }")
    public void webkitRTCPeerConnection() throws Exception {
        test("webkitRTCPeerConnection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webkitRTCSessionDescription() throws Exception {
        test("webkitRTCSessionDescription");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SpeechGrammar() { [native code] }",
            EDGE = "function SpeechGrammar() { [native code] }")
    public void webkitSpeechGrammar() throws Exception {
        test("webkitSpeechGrammar");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SpeechGrammarList() { [native code] }",
            EDGE = "function SpeechGrammarList() { [native code] }")
    public void webkitSpeechGrammarList() throws Exception {
        test("webkitSpeechGrammarList");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SpeechRecognition() { [native code] }",
            EDGE = "function SpeechRecognition() { [native code] }")
    public void webkitSpeechRecognition() throws Exception {
        test("webkitSpeechRecognition");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SpeechRecognitionErrorEvent() { [native code] }",
            EDGE = "function SpeechRecognitionErrorEvent() { [native code] }")
    public void webkitSpeechRecognitionError() throws Exception {
        test("webkitSpeechRecognitionError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception",
            CHROME = "function SpeechRecognitionEvent() { [native code] }",
            EDGE = "function SpeechRecognitionEvent() { [native code] }")
    public void webkitSpeechRecognitionEvent() throws Exception {
        test("webkitSpeechRecognitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webKitTransitionEvent() throws Exception {
        test("WebKitTransitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function URL() { [native code] }",
            IE = "exception")
    public void webkitURL() throws Exception {
        test("webkitURL");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webSMS() throws Exception {
        test("WebSMS");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.WebSocket}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function WebSocket() { [native code] }",
            IE = "function WebSocket() {\n    [native code]\n}\n")
    public void webSocket() throws Exception {
        test("WebSocket");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webSockets() throws Exception {
        test("WebSockets");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void webVTT() throws Exception {
        test("WebVTT");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function WheelEvent() { [native code] }",
            IE = "[object WheelEvent]")
    public void wheelEvent() throws Exception {
        test("WheelEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void wifiManager() throws Exception {
        test("WifiManager");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.Window}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Window() { [native code] }",
            IE = "[object Window]")
    public void window() throws Exception {
        test("Window");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void windowBase64() throws Exception {
        test("WindowBase64");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void windowClient() throws Exception {
        test("WindowClient");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void windowEventHandlers() throws Exception {
        test("WindowEventHandlers");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void windowEventHandlers_onbeforeprint() throws Exception {
        test("WindowEventHandlers.onbeforeprint");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void windowOrWorkerGlobalScope() throws Exception {
        test("WindowOrWorkerGlobalScope");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void windowProperties() throws Exception {
        test("WindowProperties");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void windowTimers() throws Exception {
        test("WindowTimers");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.worker.Worker}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function Worker() { [native code] }",
            IE = "function Worker() {\n    [native code]\n}\n")
    public void worker() throws Exception {
        test("Worker");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void workerGlobalScope() throws Exception {
        test("WorkerGlobalScope");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void workerLocation() throws Exception {
        test("WorkerLocation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void workerNavigator() throws Exception {
        test("WorkerNavigator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void xDomainRequest() throws Exception {
        test("XDomainRequest");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.xml.XMLDocument}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function XMLDocument() { [native code] }",
            IE = "[object XMLDocument]")
    public void xmlDocument() throws Exception {
        test("XMLDocument");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.xml.XMLHttpRequest}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function XMLHttpRequest() { [native code] }",
            IE = "function XMLHttpRequest() {\n    [native code]\n}\n")
    public void xmlHttpRequest() throws Exception {
        test("XMLHttpRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function XMLHttpRequestEventTarget() { [native code] }",
            IE = "[object XMLHttpRequestEventTarget]")
    public void xmlHttpRequestEventTarget() throws Exception {
        test("XMLHttpRequestEventTarget");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void xmlHttpRequestProgressEvent() throws Exception {
        test("XMLHttpRequestProgressEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function XMLHttpRequestUpload() { [native code] }",
            IE = "exception")
    public void xmlHttpRequestUpload() throws Exception {
        test("XMLHttpRequestUpload");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.xml.XMLSerializer}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function XMLSerializer() { [native code] }",
            IE = "function XMLSerializer() {\n    [native code]\n}\n")
    public void xmlSerializer() throws Exception {
        test("XMLSerializer");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.XPathEvaluator}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function XPathEvaluator() { [native code] }",
            IE = "exception")
    public void xPathEvaluator() throws Exception {
        test("XPathEvaluator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function XPathExpression() { [native code] }",
            IE = "exception")
    public void xPathExpression() throws Exception {
        test("XPathExpression");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.XPathNSResolver}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void xPathNSResolver() throws Exception {
        test("XPathNSResolver");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.XPathResult}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function XPathResult() { [native code] }",
            IE = "exception")
    public void xPathResult() throws Exception {
        test("XPathResult");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void xsltemplate() throws Exception {
        test("XSLTemplate");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.xml.XSLTProcessor}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "function XSLTProcessor() { [native code] }",
            IE = "exception")
    public void xsltProcessor() throws Exception {
        test("XSLTProcessor");
    }
}
