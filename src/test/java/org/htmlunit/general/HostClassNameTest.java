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
            // normalize FF output
            + "      clsName = clsName.replace('{\\n    [native code]\\n}', '{ [native code] }');\n"
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
    @Alerts("function AbstractRange() { [native code] }")
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
    @Alerts("exception")
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
    @Alerts("function AnalyserNode() { [native code] }")
    public void analyserNode() throws Exception {
        test("AnalyserNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void angle_instanced_arrays() throws Exception {
        test("ANGLE_instanced_arrays");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function Animation() { [native code] }")
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
    @Alerts("function AnimationEvent() { [native code] }")
    public void animationEvent() throws Exception {
        test("AnimationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function AnimationPlaybackEvent() { [native code] }")
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
    @Alerts("function AnimationTimeline() { [native code] }")
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
    @Alerts("exception")
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
    @Alerts("function Array() { [native code] }")
    public void array() throws Exception {
        test("Array");
    }

    /**
     * Test ArrayBuffer.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function ArrayBuffer() { [native code] }")
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
    @Alerts("[object Atomics]")
    public void atomics() throws Exception {
        test("Atomics");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.Attr}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function Attr() { [native code] }")
    public void attr() throws Exception {
        test("Attr");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.Audio}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function Audio() { [native code] }")
    public void audio() throws Exception {
        test("Audio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function AudioBuffer() { [native code] }")
    public void audioBuffer() throws Exception {
        test("AudioBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function AudioBufferSourceNode() { [native code] }")
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
    @Alerts("function AudioContext() { [native code] }")
    public void audioContext() throws Exception {
        test("AudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function AudioDestinationNode() { [native code] }")
    public void audioDestinationNode() throws Exception {
        test("AudioDestinationNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function AudioListener() { [native code] }")
    public void audioListener() throws Exception {
        test("AudioListener");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function AudioNode() { [native code] }")
    public void audioNode() throws Exception {
        test("AudioNode");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.media.AudioParam}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function AudioParam() { [native code] }")
    public void audioParam() throws Exception {
        test("AudioParam");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function AudioProcessingEvent() { [native code] }")
    public void audioProcessingEvent() throws Exception {
        test("AudioProcessingEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function AudioScheduledSourceNode() { [native code] }")
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
    @Alerts("function BarProp() { [native code] }")
    public void barProp() throws Exception {
        test("BarProp");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function BaseAudioContext() { [native code] }")
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
    @Alerts("function BeforeUnloadEvent() { [native code] }")
    public void beforeUnloadEvent() throws Exception {
        test("BeforeUnloadEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function BiquadFilterNode() { [native code] }")
    public void biquadFilterNode() throws Exception {
        test("BiquadFilterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function Blob() { [native code] }")
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
    @Alerts("function BlobEvent() { [native code] }")
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
    @Alerts("function BroadcastChannel() { [native code] }")
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
    @Alerts("function Cache() { [native code] }")
    public void cache() throws Exception {
        test("Cache");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function CacheStorage() { [native code] }")
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
    @Alerts("function CanvasGradient() { [native code] }")
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
    @Alerts("function CanvasPattern() { [native code] }")
    public void canvasPattern() throws Exception {
        test("CanvasPattern");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.canvas.CanvasRenderingContext2D}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function CanvasRenderingContext2D() { [native code] }")
    public void canvasRenderingContext2D() throws Exception {
        test("CanvasRenderingContext2D");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function CaretPosition() { [native code] }")
    public void caretPosition() throws Exception {
        test("CaretPosition");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.CDATASection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function CDATASection() { [native code] }")
    public void cdataSection() throws Exception {
        test("CDATASection");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.media.ChannelMergerNode}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function ChannelMergerNode() { [native code] }")
    public void channelMergerNode() throws Exception {
        test("ChannelMergerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function ChannelSplitterNode() { [native code] }")
    public void channelSplitterNode() throws Exception {
        test("ChannelSplitterNode");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.CharacterData}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function CharacterData() { [native code] }")
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
    @Alerts("exception")
    public void clientRect() throws Exception {
        test("ClientRect");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.ClientRectList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
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
    @Alerts("function ClipboardEvent() { [native code] }")
    public void clipboardEvent() throws Exception {
        test("ClipboardEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function CloseEvent() { [native code] }")
    public void closeEvent() throws Exception {
        test("CloseEvent");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.Comment}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function Comment() { [native code] }")
    public void comment() throws Exception {
        test("Comment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function CompositionEvent() { [native code] }")
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
    @Alerts("exception")
    public void console() throws Exception {
        test("Console");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function ConstantSourceNode() { [native code] }")
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
    @Alerts("function ConvolverNode() { [native code] }")
    public void convolverNode() throws Exception {
        test("ConvolverNode");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.geo.GeolocationCoordinates}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void coordinates() throws Exception {
        test("Coordinates");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function Credential() { [native code] }")
    public void credential() throws Exception {
        test("Credential");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function CredentialsContainer() { [native code] }")
    public void credentialsContainer() throws Exception {
        test("CredentialsContainer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function Crypto() { [native code] }")
    public void crypto() throws Exception {
        test("Crypto");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function CryptoKey() { [native code] }")
    public void cryptoKey() throws Exception {
        test("CryptoKey");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object CSS]")
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
     * Test {@link CSSPageDescriptors}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "exception",
            FF = "function CSSPageDescriptors() { [native code] }")
    @HtmlUnitNYI(FF = "exception")
    public void cssPageDescriptors() throws Exception {
        test("CSSPageDescriptors");
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
    @Alerts("function CSSConditionRule() { [native code] }")
    public void cssConditionRule() throws Exception {
        test("CSSConditionRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function CSSCounterStyleRule() { [native code] }")
    public void cssCounterStyleRule() throws Exception {
        test("CSSCounterStyleRule");
    }

    /**
     * Test {@link CSSFontFaceRule}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function CSSFontFaceRule() { [native code] }")
    public void cssFontFaceRule() throws Exception {
        test("CSSFontFaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function CSSGroupingRule() { [native code] }")
    public void cssGroupingRule() throws Exception {
        test("CSSGroupingRule");
    }

    /**
     * Test {@link CSSImportRule}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function CSSImportRule() { [native code] }")
    public void cssImportRule() throws Exception {
        test("CSSImportRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function CSSKeyframeRule() { [native code] }")
    public void cssKeyframeRule() throws Exception {
        test("CSSKeyframeRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function CSSKeyframesRule() { [native code] }")
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
    @Alerts("function CSSMediaRule() { [native code] }")
    public void cssMediaRule() throws Exception {
        test("CSSMediaRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function CSSNamespaceRule() { [native code] }")
    public void cssNamespaceRule() throws Exception {
        test("CSSNamespaceRule");
    }

    /**
     * Test {@link CSSPageRule}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function CSSPageRule() { [native code] }")
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
    @Alerts("function CSSRule() { [native code] }")
    public void cssRule() throws Exception {
        test("CSSRule");
    }

    /**
     * Test {@link CSSRuleList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function CSSRuleList() { [native code] }")
    public void cssRuleList() throws Exception {
        test("CSSRuleList");
    }

    /**
     * Test {@link CSSStyleDeclaration}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function CSSStyleDeclaration() { [native code] }")
    public void cssStyleDeclaration() throws Exception {
        test("CSSStyleDeclaration");
    }

    /**
     * Test {@link CSSStyleRule}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function CSSStyleRule() { [native code] }")
    @AlertsStandards("function CSSStyleRule() { [native code] }")
    public void cssStyleRule() throws Exception {
        test("CSSStyleRule");
    }

    /**
     * Test {@link CSSStyleSheet}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function CSSStyleSheet() { [native code] }")
    public void cssStyleSheet() throws Exception {
        test("CSSStyleSheet");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function CSSSupportsRule() { [native code] }")
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
    @Alerts("function CustomElementRegistry() { [native code] }")
    public void customElementRegistry() throws Exception {
        test("CustomElementRegistry");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function CustomEvent() { [native code] }")
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
    @Alerts("function DataTransfer() { [native code] }")
    public void dataTransfer() throws Exception {
        test("DataTransfer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function DataTransferItem() { [native code] }")
    public void dataTransferItem() throws Exception {
        test("DataTransferItem");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function DataTransferItemList() { [native code] }")
    public void dataTransferItemList() throws Exception {
        test("DataTransferItemList");
    }

    /**
     * Test DataView.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function DataView() { [native code] }")
    public void dataView() throws Exception {
        test("DataView");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function Date() { [native code] }")
    public void date() throws Exception {
        test("Date");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function decodeURI() { [native code] }")
    public void decodeURI() throws Exception {
        test("decodeURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function decodeURIComponent() { [native code] }")
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
    @Alerts("function DelayNode() { [native code] }")
    public void delayNode() throws Exception {
        test("DelayNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
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
    @Alerts("function DeviceMotionEvent() { [native code] }")
    public void deviceMotionEvent() throws Exception {
        test("DeviceMotionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function DeviceOrientationEvent() { [native code] }")
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
    @Alerts("exception")
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
    @Alerts("function Document() { [native code] }")
    public void document() throws Exception {
        test("Document");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.DocumentFragment}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function DocumentFragment() { [native code] }")
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
    @Alerts("function DocumentTimeline() { [native code] }")
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
    @Alerts("function DocumentType() { [native code] }")
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
            FF_ESR = "exception")
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
    @Alerts("function DOMException() { [native code] }")
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
    @Alerts("function DOMImplementation() { [native code] }")
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
    @Alerts("function DOMMatrix() { [native code] }")
    public void domMatrix() throws Exception {
        test("DOMMatrix");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function DOMMatrixReadOnly() { [native code] }")
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
    @Alerts("function DOMParser() { [native code] }")
    public void domParser() throws Exception {
        test("DOMParser");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function DOMPoint() { [native code] }")
    public void domPoint() throws Exception {
        test("DOMPoint");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function DOMPointReadOnly() { [native code] }")
    public void domPointReadOnly() throws Exception {
        test("DOMPointReadOnly");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.ClientRect}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function DOMRect() { [native code] }")
    public void domRect() throws Exception {
        test("DOMRect");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function DOMRectList() { [native code] }")
    public void domRectList() throws Exception {
        test("DOMRectList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function DOMRectReadOnly() { [native code] }")
    public void domRectReadOnly() throws Exception {
        test("DOMRectReadOnly");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void domRequest() throws Exception {
        test("DOMRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
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
    @Alerts("function DOMStringList() { [native code] }")
    public void domStringList() throws Exception {
        test("DOMStringList");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.DOMStringMap}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function DOMStringMap() { [native code] }")
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
    @Alerts("function DOMTokenList() { [native code] }")
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
    @Alerts("function DragEvent() { [native code] }")
    public void dragEvent() throws Exception {
        test("DragEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function DynamicsCompressorNode() { [native code] }")
    public void dynamicsCompressorNode() throws Exception {
        test("DynamicsCompressorNode");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.Element}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function Element() { [native code] }")
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
    @Alerts("function encodeURI() { [native code] }")
    public void encodeURI() throws Exception {
        test("encodeURI");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function encodeURIComponent() { [native code] }")
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
    @Alerts("exception")
    public void enumerator() throws Exception {
        test("Enumerator");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function Error() { [native code] }")
    public void error() throws Exception {
        test("Error");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function ErrorEvent() { [native code] }")
    public void errorEvent() throws Exception {
        test("ErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function escape() { [native code] }")
    public void escape() throws Exception {
        test("escape");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function eval() { [native code] }")
    public void eval() throws Exception {
        test("eval");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function EvalError() { [native code] }")
    public void evalError() throws Exception {
        test("EvalError");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.Event}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function Event() { [native code] }")
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
    @Alerts("function EventSource() { [native code] }")
    public void eventSource() throws Exception {
        test("EventSource");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function EventTarget() { [native code] }")
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
    @Alerts("exception")
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
    @Alerts("function File() { [native code] }")
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
    @Alerts("function FileList() { [native code] }")
    public void fileList() throws Exception {
        test("FileList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function FileReader() { [native code] }")
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
    @Alerts("function Float32Array() { [native code] }")
    public void float32Array() throws Exception {
        test("Float32Array");
    }

    /**
     * Test Float64Array.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function Float64Array() { [native code] }")
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
    @Alerts("function FocusEvent() { [native code] }")
    public void focusEvent() throws Exception {
        test("FocusEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function FontFace() { [native code] }")
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
    @Alerts("function FormData() { [native code] }")
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
    @Alerts("function Function() { [native code] }")
    public void function() throws Exception {
        test("Function");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function GainNode() { [native code] }")
    public void gainNode() throws Exception {
        test("GainNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function Gamepad() { [native code] }")
    public void gamepad() throws Exception {
        test("Gamepad");
    }

    /**
     * Tests {@link org.htmlunit.javascript.host.GamepadButton}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function GamepadButton() { [native code] }")
    public void gamepadButton() throws Exception {
        test("GamepadButton");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function GamepadEvent() { [native code] }")
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
    @Alerts("function Geolocation() { [native code] }")
    public void geolocation() throws Exception {
        test("Geolocation");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.geo.GeolocationCoordinates}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function GeolocationCoordinates() { [native code] }")
    public void geolocationCoordinates() throws Exception {
        test("GeolocationCoordinates");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.geo.GeolocationPosition}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function GeolocationPosition() { [native code] }")
    public void geolocationPosition() throws Exception {
        test("GeolocationPosition");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.geo.GeolocationPositionError}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function GeolocationPositionError() { [native code] }")
    public void geolocationPositionError() throws Exception {
        test("GeolocationPositionError");
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
    @Alerts("function HashChangeEvent() { [native code] }")
    public void hashChangeEvent() throws Exception {
        test("HashChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function Headers() { [native code] }")
    public void headers() throws Exception {
        test("Headers");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.History}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function History() { [native code] }")
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
    @Alerts("function HTMLAllCollection() { [native code] }")
    public void htmlAllCollection() throws Exception {
        test("HTMLAllCollection");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLAnchorElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLAnchorElement() { [native code] }")
    public void htmlAnchorElement() throws Exception {
        test("HTMLAnchorElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLAppletElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlAppletElement() throws Exception {
        test("HTMLAppletElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLAreaElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLAreaElement() { [native code] }")
    public void htmlAreaElement() throws Exception {
        test("HTMLAreaElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLAudioElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLAudioElement() { [native code] }")
    public void htmlAudioElement() throws Exception {
        test("HTMLAudioElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLBaseElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLBaseElement() { [native code] }")
    public void htmlBaseElement() throws Exception {
        test("HTMLBaseElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLBaseFontElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlBaseFontElement() throws Exception {
        test("HTMLBaseFontElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLBGSoundElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlBGSoundElement() throws Exception {
        test("HTMLBGSoundElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLBlockElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
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
    @Alerts("function HTMLBodyElement() { [native code] }")
    public void htmlBodyElement() throws Exception {
        test("HTMLBodyElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLBRElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLBRElement() { [native code] }")
    public void htmlBRElement() throws Exception {
        test("HTMLBRElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLButtonElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLButtonElement() { [native code] }")
    public void htmlButtonElement() throws Exception {
        test("HTMLButtonElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLCanvasElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLCanvasElement() { [native code] }")
    public void htmlCanvasElement() throws Exception {
        test("HTMLCanvasElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLCollection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLCollection() { [native code] }")
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
    @Alerts("function HTMLDataElement() { [native code] }")
    public void htmlDataElement() throws Exception {
        test("HTMLDataElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLDataListElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLDataListElement() { [native code] }")
    public void htmlDataListElement() throws Exception {
        test("HTMLDataListElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLDDElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
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
    @Alerts("function HTMLDetailsElement() { [native code] }")
    public void htmlDetailsElement() throws Exception {
        test("HTMLDetailsElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLDialogElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLDialogElement() { [native code] }")
    public void htmlDialogElement() throws Exception {
        test("HTMLDialogElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLDirectoryElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLDirectoryElement() { [native code] }")
    public void htmlDirectoryElement() throws Exception {
        test("HTMLDirectoryElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLDivElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLDivElement() { [native code] }")
    public void htmlDivElement() throws Exception {
        test("HTMLDivElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLDListElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLDListElement() { [native code] }")
    public void htmlDListElement() throws Exception {
        test("HTMLDListElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLDocument}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLDocument() { [native code] }")
    public void htmlDocument() throws Exception {
        test("HTMLDocument");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLDTElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlDTElement() throws Exception {
        test("HTMLDTElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLElement() { [native code] }")
    public void htmlElement() throws Exception {
        test("HTMLElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLEmbedElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLEmbedElement() { [native code] }")
    public void htmlEmbedElement() throws Exception {
        test("HTMLEmbedElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLFieldSetElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLFieldSetElement() { [native code] }")
    public void htmlFieldSetElement() throws Exception {
        test("HTMLFieldSetElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLFontElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLFontElement() { [native code] }")
    public void htmlFontElement() throws Exception {
        test("HTMLFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function HTMLFormControlsCollection() { [native code] }")
    public void htmlFormControlsCollection() throws Exception {
        test("HTMLFormControlsCollection");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLFormElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLFormElement() { [native code] }")
    public void htmlFormElement() throws Exception {
        test("HTMLFormElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLFrameElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLFrameElement() { [native code] }")
    public void htmlFrameElement() throws Exception {
        test("HTMLFrameElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLFrameSetElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLFrameSetElement() { [native code] }")
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
    @Alerts("function HTMLHeadElement() { [native code] }")
    public void htmlHeadElement() throws Exception {
        test("HTMLHeadElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLHeadingElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLHeadingElement() { [native code] }")
    public void htmlHeadingElement() throws Exception {
        test("HTMLHeadingElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLHRElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLHRElement() { [native code] }")
    public void htmlHRElement() throws Exception {
        test("HTMLHRElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLHtmlElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLHtmlElement() { [native code] }")
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
    @Alerts("function HTMLIFrameElement() { [native code] }")
    public void htmlIFrameElement() throws Exception {
        test("HTMLIFrameElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLImageElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLImageElement() { [native code] }")
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
    @Alerts("function HTMLInputElement() { [native code] }")
    public void htmlInputElement() throws Exception {
        test("HTMLInputElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLIsIndexElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
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
    @Alerts("function HTMLLabelElement() { [native code] }")
    public void htmlLabelElement() throws Exception {
        test("HTMLLabelElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLLegendElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLLegendElement() { [native code] }")
    public void htmlLegendElement() throws Exception {
        test("HTMLLegendElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLLIElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLLIElement() { [native code] }")
    public void htmlLIElement() throws Exception {
        test("HTMLLIElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLLinkElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLLinkElement() { [native code] }")
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
    @Alerts("function HTMLMapElement() { [native code] }")
    public void htmlMapElement() throws Exception {
        test("HTMLMapElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLMarqueeElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLMarqueeElement() { [native code] }")
    public void htmlMarqueeElement() throws Exception {
        test("HTMLMarqueeElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLMediaElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLMediaElement() { [native code] }")
    public void htmlMediaElement() throws Exception {
        test("HTMLMediaElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLMenuElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLMenuElement() { [native code] }")
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
    @Alerts("function HTMLMetaElement() { [native code] }")
    public void htmlMetaElement() throws Exception {
        test("HTMLMetaElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLMeterElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLMeterElement() { [native code] }")
    public void htmlMeterElement() throws Exception {
        test("HTMLMeterElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLModElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLModElement() { [native code] }")
    public void htmlModElement() throws Exception {
        test("HTMLModElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLNextIdElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
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
    @Alerts("function HTMLObjectElement() { [native code] }")
    public void htmlObjectElement() throws Exception {
        test("HTMLObjectElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLOListElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLOListElement() { [native code] }")
    public void htmlOListElement() throws Exception {
        test("HTMLOListElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLOptGroupElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLOptGroupElement() { [native code] }")
    public void htmlOptGroupElement() throws Exception {
        test("HTMLOptGroupElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLOptionElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLOptionElement() { [native code] }")
    public void htmlOptionElement() throws Exception {
        test("HTMLOptionElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLOptionsCollection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLOptionsCollection() { [native code] }")
    public void htmlOptionsCollection() throws Exception {
        test("HTMLOptionsCollection");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLOutputElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLOutputElement() { [native code] }")
    public void htmlOutputElement() throws Exception {
        test("HTMLOutputElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLParagraphElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLParagraphElement() { [native code] }")
    public void htmlParagraphElement() throws Exception {
        test("HTMLParagraphElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLParamElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLParamElement() { [native code] }")
    public void htmlParamElement() throws Exception {
        test("HTMLParamElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLPhraseElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlPhraseElement() throws Exception {
        test("HTMLPhraseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function HTMLPictureElement() { [native code] }")
    public void htmlPictureElement() throws Exception {
        test("HTMLPictureElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLPreElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLPreElement() { [native code] }")
    public void htmlPreElement() throws Exception {
        test("HTMLPreElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLProgressElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLProgressElement() { [native code] }")
    public void htmlProgressElement() throws Exception {
        test("HTMLProgressElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLQuoteElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLQuoteElement() { [native code] }")
    public void htmlQuoteElement() throws Exception {
        test("HTMLQuoteElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLScriptElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLScriptElement() { [native code] }")
    public void htmlScriptElement() throws Exception {
        test("HTMLScriptElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLSelectElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLSelectElement() { [native code] }")
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
    @Alerts("function HTMLSlotElement() { [native code] }")
    public void htmlSlotElement() throws Exception {
        test("HTMLSlotElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLSourceElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLSourceElement() { [native code] }")
    public void htmlSourceElement() throws Exception {
        test("HTMLSourceElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLSpanElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLSpanElement() { [native code] }")
    public void htmlSpanElement() throws Exception {
        test("HTMLSpanElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLStyleElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLStyleElement() { [native code] }")
    public void htmlStyleElement() throws Exception {
        test("HTMLStyleElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLTableCaptionElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLTableCaptionElement() { [native code] }")
    public void htmlTableCaptionElement() throws Exception {
        test("HTMLTableCaptionElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLTableCellElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLTableCellElement() { [native code] }")
    public void htmlTableCellElement() throws Exception {
        test("HTMLTableCellElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLTableColElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLTableColElement() { [native code] }")
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
    @Alerts("exception")
    public void htmlTableDataCellElement() throws Exception {
        test("HTMLTableDataCellElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLTableElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLTableElement() { [native code] }")
    public void htmlTableElement() throws Exception {
        test("HTMLTableElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLTableHeaderCellElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void htmlTableHeaderCellElement() throws Exception {
        test("HTMLTableHeaderCellElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLTableRowElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLTableRowElement() { [native code] }")
    public void htmlTableRowElement() throws Exception {
        test("HTMLTableRowElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLTableSectionElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLTableSectionElement() { [native code] }")
    public void htmlTableSectionElement() throws Exception {
        test("HTMLTableSectionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function HTMLTemplateElement() { [native code] }")
    public void htmlTemplateElement() throws Exception {
        test("HTMLTemplateElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLTextAreaElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLTextAreaElement() { [native code] }")
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
    @Alerts("function HTMLTimeElement() { [native code] }")
    public void htmlTimeElement() throws Exception {
        test("HTMLTimeElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLTitleElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLTitleElement() { [native code] }")
    public void htmlTitleElement() throws Exception {
        test("HTMLTitleElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLTrackElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLTrackElement() { [native code] }")
    public void htmlTrackElement() throws Exception {
        test("HTMLTrackElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLUListElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLUListElement() { [native code] }")
    public void htmlUListElement() throws Exception {
        test("HTMLUListElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLUnknownElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLUnknownElement() { [native code] }")
    public void htmlUnknownElement() throws Exception {
        test("HTMLUnknownElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLVideoElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function HTMLVideoElement() { [native code] }")
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
    @Alerts("function IDBCursor() { [native code] }")
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
    @Alerts("function IDBCursorWithValue() { [native code] }")
    public void idbCursorWithValue() throws Exception {
        test("IDBCursorWithValue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function IDBDatabase() { [native code] }")
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
    @Alerts("function IDBFactory() { [native code] }")
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
    @Alerts("function IDBIndex() { [native code] }")
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
    @Alerts("function IDBKeyRange() { [native code] }")
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
    @Alerts("function IDBObjectStore() { [native code] }")
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
    @Alerts("function IDBOpenDBRequest() { [native code] }")
    public void idbOpenDBRequest() throws Exception {
        test("IDBOpenDBRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function IDBRequest() { [native code] }")
    public void idbRequest() throws Exception {
        test("IDBRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function IDBTransaction() { [native code] }")
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
    @Alerts("function IDBVersionChangeEvent() { [native code] }")
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
    @Alerts("function IdleDeadline() { [native code] }")
    public void idleDeadline() throws Exception {
        test("IdleDeadline");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function IIRFilterNode() { [native code] }")
    public void iirFilterNode() throws Exception {
        test("IIRFilterNode");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLImageElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function Image() { [native code] }")
    public void image() throws Exception {
        test("Image");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function ImageBitmap() { [native code] }")
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
    @Alerts("function ImageBitmapRenderingContext() { [native code] }")
    public void imageBitmapRenderingContext() throws Exception {
        test("ImageBitmapRenderingContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function ImageData() { [native code] }")
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
    @Alerts("function InputEvent() { [native code] }")
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
    @Alerts("function Int16Array() { [native code] }")
    public void int16Array() throws Exception {
        test("Int16Array");
    }

    /**
     * Test Int32Array.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function Int32Array() { [native code] }")
    public void int32Array() throws Exception {
        test("Int32Array");
    }

    /**
     * Test Int8Array.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function Int8Array() { [native code] }")
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
            EDGE = "function InternalError() { [native code] }")
    public void internalError() throws Exception {
        test("InternalError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function IntersectionObserver() { [native code] }")
    public void intersectionObserver() throws Exception {
        test("IntersectionObserver");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function IntersectionObserverEntry() { [native code] }")
    public void intersectionObserverEntry() throws Exception {
        test("IntersectionObserverEntry");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.intl.Intl}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Intl]")
    public void intl() throws Exception {
        test("Intl");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function Collator() { [native code] }")
    public void intl_Collator() throws Exception {
        test("Intl.Collator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function DateTimeFormat() { [native code] }")
    public void intl_DateTimeFormat() throws Exception {
        test("Intl.DateTimeFormat");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function NumberFormat() { [native code] }")
    public void intl_NumberFormat() throws Exception {
        test("Intl.NumberFormat");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function isFinite() { [native code] }")
    public void isFinite() throws Exception {
        test("isFinite");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function isNaN() { [native code] }")
    public void isNaN() throws Exception {
        test("isNaN");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Iterator() { [native code] }",
            FF_ESR = "exception")
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
    @Alerts("function KeyboardEvent() { [native code] }")
    public void keyboardEvent() throws Exception {
        test("KeyboardEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function KeyframeEffect() { [native code] }")
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
    @Alerts("function Location() { [native code] }")
    @AlertsStandards("function Location() { [native code] }")
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
    @Alerts("function Map() { [native code] }")
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
    @Alerts("function MediaDeviceInfo() { [native code] }")
    public void mediaDeviceInfo() throws Exception {
        test("MediaDeviceInfo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MediaDevices() { [native code] }")
    public void mediaDevices() throws Exception {
        test("MediaDevices");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MediaElementAudioSourceNode() { [native code] }")
    public void mediaElementAudioSourceNode() throws Exception {
        test("MediaElementAudioSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MediaEncryptedEvent() { [native code] }")
    public void mediaEncryptedEvent() throws Exception {
        test("MediaEncryptedEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MediaError() { [native code] }")
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
    @Alerts("function MediaKeyMessageEvent() { [native code] }")
    public void mediaKeyMessageEvent() throws Exception {
        test("MediaKeyMessageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MediaKeys() { [native code] }")
    public void mediaKeys() throws Exception {
        test("MediaKeys");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MediaKeySession() { [native code] }")
    public void mediaKeySession() throws Exception {
        test("MediaKeySession");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MediaKeyStatusMap() { [native code] }")
    public void mediaKeyStatusMap() throws Exception {
        test("MediaKeyStatusMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MediaKeySystemAccess() { [native code] }")
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
    @Alerts("function MediaList() { [native code] }")
    public void mediaList() throws Exception {
        test("MediaList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MediaQueryList() { [native code] }")
    public void mediaQueryList() throws Exception {
        test("MediaQueryList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MediaQueryListEvent() { [native code] }")
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
    @Alerts("function MediaRecorder() { [native code] }")
    public void mediaRecorder() throws Exception {
        test("MediaRecorder");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MediaSource() { [native code] }")
    public void mediaSource() throws Exception {
        test("MediaSource");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MediaStream() { [native code] }")
    public void mediaStream() throws Exception {
        test("MediaStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MediaStreamAudioDestinationNode() { [native code] }")
    public void mediaStreamAudioDestinationNode() throws Exception {
        test("MediaStreamAudioDestinationNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MediaStreamAudioSourceNode() { [native code] }")
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
    @Alerts("function MediaStreamEvent() { [native code] }")
    public void mediaStreamEvent() throws Exception {
        test("MediaStreamEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MediaStreamTrack() { [native code] }")
    public void mediaStreamTrack() throws Exception {
        test("MediaStreamTrack");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MediaStreamTrackEvent() { [native code] }")
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
    @Alerts("function MessageChannel() { [native code] }")
    public void messageChannel() throws Exception {
        test("MessageChannel");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.MessageEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function MessageEvent() { [native code] }")
    public void messageEvent() throws Exception {
        test("MessageEvent");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.MessagePort}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function MessagePort() { [native code] }")
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
    @Alerts("function MIDIAccess() { [native code] }")
    public void midiAccess() throws Exception {
        test("MIDIAccess");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MIDIConnectionEvent() { [native code] }")
    public void midiConnectionEvent() throws Exception {
        test("MIDIConnectionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MIDIInput() { [native code] }")
    public void midiInput() throws Exception {
        test("MIDIInput");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MIDIInputMap() { [native code] }")
    public void midiInputMap() throws Exception {
        test("MIDIInputMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MIDIMessageEvent() { [native code] }")
    public void midiMessageEvent() throws Exception {
        test("MIDIMessageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MIDIOutput() { [native code] }")
    public void midiOutput() throws Exception {
        test("MIDIOutput");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MIDIOutputMap() { [native code] }")
    public void midiOutputMap() throws Exception {
        test("MIDIOutputMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MIDIPort() { [native code] }")
    public void midiPort() throws Exception {
        test("MIDIPort");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.MimeType}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function MimeType() { [native code] }")
    public void mimeType() throws Exception {
        test("MimeType");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.MimeTypeArray}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function MimeTypeArray() { [native code] }")
    public void mimeTypeArray() throws Exception {
        test("MimeTypeArray");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.MouseEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function MouseEvent() { [native code] }")
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
    @Alerts("exception")
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
    @Alerts("exception")
    public void msCurrentStyleCSSProperties() throws Exception {
        test("MSCurrentStyleCSSProperties");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void msGestureEvent() throws Exception {
        test("MSGestureEvent");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void msStyleCSSProperties() throws Exception {
        test("MSStyleCSSProperties");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.event.MutationEvent}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function MutationEvent() { [native code] }")
    public void mutationEvent() throws Exception {
        test("MutationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MutationObserver() { [native code] }")
    public void mutationObserver() throws Exception {
        test("MutationObserver");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function MutationRecord() { [native code] }")
    public void mutationRecord() throws Exception {
        test("MutationRecord");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.NamedNodeMap}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function NamedNodeMap() { [native code] }")
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
    @Alerts("function Navigator() { [native code] }")
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
    @Alerts("function Node() { [native code] }")
    public void node() throws Exception {
        test("Node");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.NodeFilter}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function NodeFilter() { [native code] }")
    public void nodeFilter() throws Exception {
        test("NodeFilter");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function NodeIterator() { [native code] }")
    public void nodeIterator() throws Exception {
        test("NodeIterator");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.NodeList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function NodeList() { [native code] }")
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
    @Alerts("function Notification() { [native code] }")
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
    @Alerts("function Number() { [native code] }")
    public void number() throws Exception {
        test("Number");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function Object() { [native code] }")
    public void object() throws Exception {
        test("Object");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void oes_element_index_uint() throws Exception {
        test("OES_element_index_uint");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void oes_standard_derivatives() throws Exception {
        test("OES_standard_derivatives");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void oes_texture_float() throws Exception {
        test("OES_texture_float");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
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
    @Alerts("function OfflineAudioCompletionEvent() { [native code] }")
    public void offlineAudioCompletionEvent() throws Exception {
        test("OfflineAudioCompletionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function OfflineAudioContext() { [native code] }")
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
    @Alerts("function OffscreenCanvas() { [native code] }")
    @HtmlUnitNYI(CHROME = "exception",
            EDGE = "exception",
            FF = "exception",
            FF_ESR = "exception")
    public void offscreenCanvas() throws Exception {
        test("OffscreenCanvas");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.html.HTMLOptionElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function Option() { [native code] }")
    public void option() throws Exception {
        test("Option");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function OscillatorNode() { [native code] }")
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
    @Alerts("function PageTransitionEvent() { [native code] }")
    public void pageTransitionEvent() throws Exception {
        test("PageTransitionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function PannerNode() { [native code] }")
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
    @Alerts("function parseFloat() { [native code] }")
    public void parseFloat() throws Exception {
        test("parseFloat");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function parseInt() { [native code] }")
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
    @Alerts("function Path2D() { [native code] }")
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
    @Alerts("function Performance() { [native code] }")
    public void performance() throws Exception {
        test("Performance");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function PerformanceEntry() { [native code] }")
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
    @Alerts("function PerformanceMark() { [native code] }")
    public void performanceMark() throws Exception {
        test("PerformanceMark");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function PerformanceMeasure() { [native code] }")
    public void performanceMeasure() throws Exception {
        test("PerformanceMeasure");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function PerformanceNavigation() { [native code] }")
    public void performanceNavigation() throws Exception {
        test("PerformanceNavigation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function PerformanceNavigationTiming() { [native code] }")
    public void performanceNavigationTiming() throws Exception {
        test("PerformanceNavigationTiming");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function PerformanceObserver() { [native code] }")
    public void performanceObserver() throws Exception {
        test("PerformanceObserver");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function PerformanceObserverEntryList() { [native code] }")
    public void performanceObserverEntryList() throws Exception {
        test("PerformanceObserverEntryList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function PerformanceResourceTiming() { [native code] }")
    public void performanceResourceTiming() throws Exception {
        test("PerformanceResourceTiming");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function PerformanceTiming() { [native code] }")
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
    @Alerts("function PeriodicWave() { [native code] }")
    public void periodicWave() throws Exception {
        test("PeriodicWave");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function Permissions() { [native code] }")
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
    @Alerts("function PermissionStatus() { [native code] }")
    public void permissionStatus() throws Exception {
        test("PermissionStatus");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.Plugin}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function Plugin() { [native code] }")
    public void plugin() throws Exception {
        test("Plugin");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.PluginArray}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function PluginArray() { [native code] }")
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
    @Alerts("function PointerEvent() { [native code] }")
    public void pointerEvent() throws Exception {
        test("PointerEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function PopStateEvent() { [native code] }")
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
     * Test {@link org.htmlunit.javascript.host.geo.GeolocationPosition}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void position() throws Exception {
        test("Position");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
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
    @Alerts("function ProcessingInstruction() { [native code] }")
    public void processingInstruction() throws Exception {
        test("ProcessingInstruction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function ProgressEvent() { [native code] }")
    public void progressEvent() throws Exception {
        test("ProgressEvent");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function Promise() { [native code] }")
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
    @Alerts("function PromiseRejectionEvent() { [native code] }")
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
    @Alerts("function Proxy() { [native code] }")
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
    @Alerts("function PushManager() { [native code] }")
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
    @Alerts("function PushSubscription() { [native code] }")
    public void pushSubscription() throws Exception {
        test("PushSubscription");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function PushSubscriptionOptions() { [native code] }")
    public void pushSubscriptionOptions() throws Exception {
        test("PushSubscriptionOptions");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function RadioNodeList() { [native code] }")
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
    @Alerts("function Range() { [native code] }")
    public void range() throws Exception {
        test("Range");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function RangeError() { [native code] }")
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
    @Alerts("function ReadableStream() { [native code] }")
    public void readableStream() throws Exception {
        test("ReadableStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function ReferenceError() { [native code] }")
    public void referenceError() throws Exception {
        test("ReferenceError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Reflect]")
    public void reflect() throws Exception {
        test("Reflect");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function RegExp() { [native code] }")
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
    @Alerts("function Request() { [native code] }")
    public void request() throws Exception {
        test("Request");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function Response() { [native code] }")
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
    @Alerts("function RTCCertificate() { [native code] }")
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
    @Alerts("function RTCDataChannel() { [native code] }")
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
    @Alerts("function RTCDataChannelEvent() { [native code] }")
    public void rtcDataChannelEvent() throws Exception {
        test("RTCDataChannelEvent");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function RTCIceCandidate() { [native code] }")
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
    @Alerts("function RTCPeerConnection() { [native code] }")
    public void rtcPeerConnection() throws Exception {
        test("RTCPeerConnection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function RTCPeerConnectionIceEvent() { [native code] }")
    public void rtcPeerConnectionIceEvent() throws Exception {
        test("RTCPeerConnectionIceEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function RTCSctpTransport() { [native code] }")
    public void rtcSctpTransport() throws Exception {
        test("RTCSctpTransport");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function RTCSessionDescription() { [native code] }")
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
    @Alerts("function RTCStatsReport() { [native code] }")
    public void rtcStatsReport() throws Exception {
        test("RTCStatsReport");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.Screen}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function Screen() { [native code] }")
    public void screen() throws Exception {
        test("Screen");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function ScreenOrientation() { [native code] }")
    public void screenOrientation() throws Exception {
        test("ScreenOrientation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function ScriptProcessorNode() { [native code] }")
    public void scriptProcessorNode() throws Exception {
        test("ScriptProcessorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SecurityPolicyViolationEvent() { [native code] }")
    public void securityPolicyViolationEvent() throws Exception {
        test("SecurityPolicyViolationEvent");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.Selection}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function Selection() { [native code] }")
    public void selection() throws Exception {
        test("Selection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function ServiceWorker() { [native code] }")
    public void serviceWorker() throws Exception {
        test("ServiceWorker");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function ServiceWorkerContainer() { [native code] }")
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
    @Alerts("function ServiceWorkerRegistration() { [native code] }")
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
    @Alerts("function Set() { [native code] }")
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
    @Alerts("function ShadowRoot() { [native code] }")
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
    @Alerts("function SharedWorker() { [native code] }")
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
    @Alerts("function SourceBuffer() { [native code] }")
    public void sourceBuffer() throws Exception {
        test("SourceBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SourceBufferList() { [native code] }")
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
    @Alerts("function SpeechSynthesis() { [native code] }")
    public void speechSynthesis() throws Exception {
        test("SpeechSynthesis");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SpeechSynthesisErrorEvent() { [native code] }")
    public void speechSynthesisErrorEvent() throws Exception {
        test("SpeechSynthesisErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SpeechSynthesisEvent() { [native code] }")
    public void speechSynthesisEvent() throws Exception {
        test("SpeechSynthesisEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SpeechSynthesisUtterance() { [native code] }")
    public void speechSynthesisUtterance() throws Exception {
        test("SpeechSynthesisUtterance");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SpeechSynthesisVoice() { [native code] }")
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
    @Alerts("function StereoPannerNode() { [native code] }")
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
    @Alerts("function Storage() { [native code] }")
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
    @Alerts("function StorageEvent() { [native code] }")
    public void storageEvent() throws Exception {
        test("StorageEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function StorageManager() { [native code] }")
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
    @Alerts("function String() { [native code] }")
    public void string() throws Exception {
        test("String");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void styleMedia() throws Exception {
        test("StyleMedia");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function StyleSheet() { [native code] }")
    public void styleSheet() throws Exception {
        test("StyleSheet");
    }

    /**
     * Test {@link StyleSheetList}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function StyleSheetList() { [native code] }")
    public void styleSheetList() throws Exception {
        test("StyleSheetList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SubmitEvent() { [native code] }")
    public void submitEvent() throws Exception {
        test("SubmitEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SubtleCrypto() { [native code] }")
    public void subtleCrypto() throws Exception {
        test("SubtleCrypto");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGAElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGAElement() { [native code] }")
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
    @Alerts("function SVGAngle() { [native code] }")
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
    @Alerts("function SVGAnimatedAngle() { [native code] }")
    public void svgAnimatedAngle() throws Exception {
        test("SVGAnimatedAngle");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGAnimatedBoolean() { [native code] }")
    public void svgAnimatedBoolean() throws Exception {
        test("SVGAnimatedBoolean");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGAnimatedEnumeration() { [native code] }")
    public void svgAnimatedEnumeration() throws Exception {
        test("SVGAnimatedEnumeration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGAnimatedInteger() { [native code] }")
    public void svgAnimatedInteger() throws Exception {
        test("SVGAnimatedInteger");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGAnimatedLength() { [native code] }")
    public void svgAnimatedLength() throws Exception {
        test("SVGAnimatedLength");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGAnimatedLengthList() { [native code] }")
    public void svgAnimatedLengthList() throws Exception {
        test("SVGAnimatedLengthList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGAnimatedNumber() { [native code] }")
    public void svgAnimatedNumber() throws Exception {
        test("SVGAnimatedNumber");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGAnimatedNumberList() { [native code] }")
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
    @Alerts("function SVGAnimatedPreserveAspectRatio() { [native code] }")
    public void svgAnimatedPreserveAspectRatio() throws Exception {
        test("SVGAnimatedPreserveAspectRatio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGAnimatedRect() { [native code] }")
    public void svgAnimatedRect() throws Exception {
        test("SVGAnimatedRect");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGAnimatedString() { [native code] }")
    public void svgAnimatedString() throws Exception {
        test("SVGAnimatedString");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGAnimatedTransformList() { [native code] }")
    public void svgAnimatedTransformList() throws Exception {
        test("SVGAnimatedTransformList");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGAnimateElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGAnimateElement() { [native code] }")
    public void svgAnimateElement() throws Exception {
        test("SVGAnimateElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGAnimateMotionElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGAnimateMotionElement() { [native code] }")
    public void svgAnimateMotionElement() throws Exception {
        test("SVGAnimateMotionElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGAnimateTransformElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGAnimateTransformElement() { [native code] }")
    public void svgAnimateTransformElement() throws Exception {
        test("SVGAnimateTransformElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGAnimationElement() { [native code] }")
    public void svgAnimationElement() throws Exception {
        test("SVGAnimationElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGCircleElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGCircleElement() { [native code] }")
    public void svgCircleElement() throws Exception {
        test("SVGCircleElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGClipPathElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGClipPathElement() { [native code] }")
    public void svgClipPathElement() throws Exception {
        test("SVGClipPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGComponentTransferFunctionElement() { [native code] }")
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
    @Alerts("function SVGDefsElement() { [native code] }")
    public void svgDefsElement() throws Exception {
        test("SVGDefsElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGDescElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGDescElement() { [native code] }")
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
    @Alerts("function SVGElement() { [native code] }")
    public void svgElement() throws Exception {
        test("SVGElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGEllipseElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGEllipseElement() { [native code] }")
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
    @Alerts("function SVGFEBlendElement() { [native code] }")
    public void svgFEBlendElement() throws Exception {
        test("SVGFEBlendElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEColorMatrixElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGFEColorMatrixElement() { [native code] }")
    public void svgFEColorMatrixElement() throws Exception {
        test("SVGFEColorMatrixElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEComponentTransferElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGFEComponentTransferElement() { [native code] }")
    public void svgFEComponentTransferElement() throws Exception {
        test("SVGFEComponentTransferElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFECompositeElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGFECompositeElement() { [native code] }")
    public void svgFECompositeElement() throws Exception {
        test("SVGFECompositeElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEConvolveMatrixElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGFEConvolveMatrixElement() { [native code] }")
    public void svgFEConvolveMatrixElement() throws Exception {
        test("SVGFEConvolveMatrixElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEDiffuseLightingElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGFEDiffuseLightingElement() { [native code] }")
    public void svgFEDiffuseLightingElement() throws Exception {
        test("SVGFEDiffuseLightingElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEDisplacementMapElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGFEDisplacementMapElement() { [native code] }")
    public void svgFEDisplacementMapElement() throws Exception {
        test("SVGFEDisplacementMapElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEDistantLightElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGFEDistantLightElement() { [native code] }")
    public void svgFEDistantLightElement() throws Exception {
        test("SVGFEDistantLightElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGFEDropShadowElement() { [native code] }")
    public void svgFEDropShadowElement() throws Exception {
        test("SVGFEDropShadowElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEFloodElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGFEFloodElement() { [native code] }")
    public void svgFEFloodElement() throws Exception {
        test("SVGFEFloodElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEFuncAElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGFEFuncAElement() { [native code] }")
    public void svgFEFuncAElement() throws Exception {
        test("SVGFEFuncAElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEFuncBElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGFEFuncBElement() { [native code] }")
    public void svgFEFuncBElement() throws Exception {
        test("SVGFEFuncBElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEFuncGElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGFEFuncGElement() { [native code] }")
    public void svgFEFuncGElement() throws Exception {
        test("SVGFEFuncGElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEFuncRElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGFEFuncRElement() { [native code] }")
    public void svgFEFuncRElement() throws Exception {
        test("SVGFEFuncRElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEGaussianBlurElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGFEGaussianBlurElement() { [native code] }")
    public void svgFEGaussianBlurElement() throws Exception {
        test("SVGFEGaussianBlurElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEImageElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGFEImageElement() { [native code] }")
    public void svgFEImageElement() throws Exception {
        test("SVGFEImageElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEMergeElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGFEMergeElement() { [native code] }")
    public void svgFEMergeElement() throws Exception {
        test("SVGFEMergeElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEMergeNodeElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGFEMergeNodeElement() { [native code] }")
    public void svgFEMergeNodeElement() throws Exception {
        test("SVGFEMergeNodeElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEMorphologyElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGFEMorphologyElement() { [native code] }")
    public void svgFEMorphologyElement() throws Exception {
        test("SVGFEMorphologyElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEOffsetElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGFEOffsetElement() { [native code] }")
    public void svgFEOffsetElement() throws Exception {
        test("SVGFEOffsetElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFEPointLightElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGFEPointLightElement() { [native code] }")
    public void svgFEPointLightElement() throws Exception {
        test("SVGFEPointLightElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFESpecularLightingElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGFESpecularLightingElement() { [native code] }")
    public void svgFESpecularLightingElement() throws Exception {
        test("SVGFESpecularLightingElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFESpotLightElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGFESpotLightElement() { [native code] }")
    public void svgFESpotLightElement() throws Exception {
        test("SVGFESpotLightElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFETileElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGFETileElement() { [native code] }")
    public void svgFETileElement() throws Exception {
        test("SVGFETileElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFETurbulenceElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGFETurbulenceElement() { [native code] }")
    public void svgFETurbulenceElement() throws Exception {
        test("SVGFETurbulenceElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGFilterElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGFilterElement() { [native code] }")
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
    @Alerts("function SVGForeignObjectElement() { [native code] }")
    public void svgForeignObjectElement() throws Exception {
        test("SVGForeignObjectElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGGElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGGElement() { [native code] }")
    public void svgGElement() throws Exception {
        test("SVGGElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGGeometryElement() { [native code] }")
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
    @Alerts("function SVGGradientElement() { [native code] }")
    public void svgGradientElement() throws Exception {
        test("SVGGradientElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGGraphicsElement() { [native code] }")
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
    @Alerts("function SVGImageElement() { [native code] }")
    public void svgImageElement() throws Exception {
        test("SVGImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGLength() { [native code] }")
    public void svgLength() throws Exception {
        test("SVGLength");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGLengthList() { [native code] }")
    public void svgLengthList() throws Exception {
        test("SVGLengthList");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGLinearGradientElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGLinearGradientElement() { [native code] }")
    public void svgLinearGradientElement() throws Exception {
        test("SVGLinearGradientElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGLineElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGLineElement() { [native code] }")
    public void svgLineElement() throws Exception {
        test("SVGLineElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGMarkerElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGMarkerElement() { [native code] }")
    public void svgMarkerElement() throws Exception {
        test("SVGMarkerElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGMaskElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGMaskElement() { [native code] }")
    public void svgMaskElement() throws Exception {
        test("SVGMaskElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGMatrix}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGMatrix() { [native code] }")
    public void svgMatrix() throws Exception {
        test("SVGMatrix");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGMetadataElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGMetadataElement() { [native code] }")
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
    @Alerts("function SVGMPathElement() { [native code] }")
    public void svgMPathElement() throws Exception {
        test("SVGMPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGNumber() { [native code] }")
    public void svgNumber() throws Exception {
        test("SVGNumber");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGNumberList() { [native code] }")
    public void svgNumberList() throws Exception {
        test("SVGNumberList");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGPathElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGPathElement() { [native code] }")
    public void svgPathElement() throws Exception {
        test("SVGPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgPathSeg() throws Exception {
        test("SVGPathSeg");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgPathSegArcAbs() throws Exception {
        test("SVGPathSegArcAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgPathSegArcRel() throws Exception {
        test("SVGPathSegArcRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgPathSegClosePath() throws Exception {
        test("SVGPathSegClosePath");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgPathSegCurvetoCubicAbs() throws Exception {
        test("SVGPathSegCurvetoCubicAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgPathSegCurvetoCubicRel() throws Exception {
        test("SVGPathSegCurvetoCubicRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgPathSegCurvetoCubicSmoothAbs() throws Exception {
        test("SVGPathSegCurvetoCubicSmoothAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgPathSegCurvetoCubicSmoothRel() throws Exception {
        test("SVGPathSegCurvetoCubicSmoothRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgPathSegCurvetoQuadraticAbs() throws Exception {
        test("SVGPathSegCurvetoQuadraticAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgPathSegCurvetoQuadraticRel() throws Exception {
        test("SVGPathSegCurvetoQuadraticRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgPathSegCurvetoQuadraticSmoothAbs() throws Exception {
        test("SVGPathSegCurvetoQuadraticSmoothAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgPathSegCurvetoQuadraticSmoothRel() throws Exception {
        test("SVGPathSegCurvetoQuadraticSmoothRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgPathSegLinetoAbs() throws Exception {
        test("SVGPathSegLinetoAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgPathSegLinetoHorizontalAbs() throws Exception {
        test("SVGPathSegLinetoHorizontalAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgPathSegLinetoHorizontalRel() throws Exception {
        test("SVGPathSegLinetoHorizontalRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgPathSegLinetoRel() throws Exception {
        test("SVGPathSegLinetoRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgPathSegLinetoVerticalAbs() throws Exception {
        test("SVGPathSegLinetoVerticalAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgPathSegLinetoVerticalRel() throws Exception {
        test("SVGPathSegLinetoVerticalRel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgPathSegList() throws Exception {
        test("SVGPathSegList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgPathSegMovetoAbs() throws Exception {
        test("SVGPathSegMovetoAbs");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void svgPathSegMovetoRel() throws Exception {
        test("SVGPathSegMovetoRel");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGPatternElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGPatternElement() { [native code] }")
    public void svgPatternElement() throws Exception {
        test("SVGPatternElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGPoint() { [native code] }")
    public void svgPoint() throws Exception {
        test("SVGPoint");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGPointList() { [native code] }")
    public void svgPointList() throws Exception {
        test("SVGPointList");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGPolygonElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGPolygonElement() { [native code] }")
    public void svgPolygonElement() throws Exception {
        test("SVGPolygonElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGPolylineElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGPolylineElement() { [native code] }")
    public void svgPolylineElement() throws Exception {
        test("SVGPolylineElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGPreserveAspectRatio() { [native code] }")
    public void svgPreserveAspectRatio() throws Exception {
        test("SVGPreserveAspectRatio");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGRadialGradientElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGRadialGradientElement() { [native code] }")
    public void svgRadialGradientElement() throws Exception {
        test("SVGRadialGradientElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGRect}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGRect() { [native code] }")
    public void svgRect() throws Exception {
        test("SVGRect");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGRectElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGRectElement() { [native code] }")
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
    @Alerts("function SVGScriptElement() { [native code] }")
    public void svgScriptElement() throws Exception {
        test("SVGScriptElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGSetElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGSetElement() { [native code] }")
    public void svgSetElement() throws Exception {
        test("SVGSetElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGStopElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGStopElement() { [native code] }")
    public void svgStopElement() throws Exception {
        test("SVGStopElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGStringList() { [native code] }")
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
    @Alerts("function SVGStyleElement() { [native code] }")
    public void svgStyleElement() throws Exception {
        test("SVGStyleElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGSVGElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGSVGElement() { [native code] }")
    public void svgSVGElement() throws Exception {
        test("SVGSVGElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGSwitchElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGSwitchElement() { [native code] }")
    public void svgSwitchElement() throws Exception {
        test("SVGSwitchElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGSymbolElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGSymbolElement() { [native code] }")
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
    @Alerts("function SVGTextContentElement() { [native code] }")
    public void svgTextContentElement() throws Exception {
        test("SVGTextContentElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGTextElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGTextElement() { [native code] }")
    public void svgTextElement() throws Exception {
        test("SVGTextElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGTextPathElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGTextPathElement() { [native code] }")
    public void svgTextPathElement() throws Exception {
        test("SVGTextPathElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGTextPositioningElement() { [native code] }")
    public void svgTextPositioningElement() throws Exception {
        test("SVGTextPositioningElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGTitleElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGTitleElement() { [native code] }")
    public void svgTitleElement() throws Exception {
        test("SVGTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGTransform() { [native code] }")
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
    @Alerts("function SVGTransformList() { [native code] }")
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
    @Alerts("function SVGTSpanElement() { [native code] }")
    public void svgTSpanElement() throws Exception {
        test("SVGTSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function SVGUnitTypes() { [native code] }")
    public void svgUnitTypes() throws Exception {
        test("SVGUnitTypes");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGUseElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGUseElement() { [native code] }")
    public void svgUseElement() throws Exception {
        test("SVGUseElement");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.svg.SVGViewElement}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function SVGViewElement() { [native code] }")
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
    @Alerts("exception")
    public void svgZoomEvent() throws Exception {
        test("SVGZoomEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function Symbol() { [native code] }")
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
    @Alerts("function SyntaxError() { [native code] }")
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
    @Alerts("function Text() { [native code] }")
    public void text() throws Exception {
        test("Text");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function TextDecoder() { [native code] }")
    public void textDecoder() throws Exception {
        test("TextDecoder");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function TextEncoder() { [native code] }")
    public void textEncoder() throws Exception {
        test("TextEncoder");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function TextEvent() { [native code] }",
            FF_ESR = "exception")
    public void textEvent() throws Exception {
        test("TextEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function TextMetrics() { [native code] }")
    public void textMetrics() throws Exception {
        test("TextMetrics");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.TextRange}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("exception")
    public void textRange() throws Exception {
        test("TextRange");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function TextTrack() { [native code] }")
    public void textTrack() throws Exception {
        test("TextTrack");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function TextTrackCue() { [native code] }")
    public void textTrackCue() throws Exception {
        test("TextTrackCue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function TextTrackCueList() { [native code] }")
    public void textTrackCueList() throws Exception {
        test("TextTrackCueList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function TextTrackList() { [native code] }")
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
    @Alerts("function TimeRanges() { [native code] }")
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
    @Alerts("function TrackEvent() { [native code] }")
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
    @Alerts("function TransitionEvent() { [native code] }")
    public void transitionEvent() throws Exception {
        test("TransitionEvent");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.TreeWalker}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function TreeWalker() { [native code] }")
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
    @Alerts("function TypeError() { [native code] }")
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
    @Alerts("function UIEvent() { [native code] }")
    public void uiEvent() throws Exception {
        test("UIEvent");
    }

    /**
     * Test Uint16Array.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function Uint16Array() { [native code] }")
    public void uint16Array() throws Exception {
        test("Uint16Array");
    }

    /**
     * Test Uint32Array.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function Uint32Array() { [native code] }")
    public void uint32Array() throws Exception {
        test("Uint32Array");
    }

    /**
     * Test Uint8Array.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function Uint8Array() { [native code] }")
    public void uint8Array() throws Exception {
        test("Uint8Array");
    }

    /**
     * Test Uint8ClampedArray.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function Uint8ClampedArray() { [native code] }")
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
    @Alerts("function unescape() { [native code] }")
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
    @Alerts("function URIError() { [native code] }")
    public void uriError() throws Exception {
        test("URIError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function URL() { [native code] }")
    public void url() throws Exception {
        test("URL");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.URLSearchParams}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function URLSearchParams() { [native code] }")
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
    @Alerts("function ValidityState() { [native code] }")
    public void validityState() throws Exception {
        test("ValidityState");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function VideoPlaybackQuality() { [native code] }")
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
    @Alerts("function VTTCue() { [native code] }")
    public void vTTCue() throws Exception {
        test("VTTCue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function WaveShaperNode() { [native code] }")
    public void waveShaperNode() throws Exception {
        test("WaveShaperNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function WeakMap() { [native code] }")
    public void weakMap() throws Exception {
        test("WeakMap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function WeakSet() { [native code] }")
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
    @Alerts("exception")
    public void webGL_compressed_texture_s3tc() throws Exception {
        test("WEBGL_compressed_texture_s3tc");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
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
    @Alerts("function WebGL2RenderingContext() { [native code] }")
    public void webGL2RenderingContext() throws Exception {
        test("WebGL2RenderingContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function WebGLActiveInfo() { [native code] }")
    public void webGLActiveInfo() throws Exception {
        test("WebGLActiveInfo");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function WebGLBuffer() { [native code] }")
    public void webGLBuffer() throws Exception {
        test("WebGLBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function WebGLContextEvent() { [native code] }")
    public void webGLContextEvent() throws Exception {
        test("WebGLContextEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function WebGLFramebuffer() { [native code] }")
    public void webGLFramebuffer() throws Exception {
        test("WebGLFramebuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function WebGLProgram() { [native code] }")
    public void webGLProgram() throws Exception {
        test("WebGLProgram");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function WebGLQuery() { [native code] }")
    public void webGLQuery() throws Exception {
        test("WebGLQuery");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function WebGLRenderbuffer() { [native code] }")
    public void webGLRenderbuffer() throws Exception {
        test("WebGLRenderbuffer");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.canvas.WebGLRenderingContext}.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function WebGLRenderingContext() { [native code] }")
    public void webGLRenderingContext() throws Exception {
        test("WebGLRenderingContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function WebGLSampler() { [native code] }")
    public void webGLSampler() throws Exception {
        test("WebGLSampler");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function WebGLShader() { [native code] }")
    public void webGLShader() throws Exception {
        test("WebGLShader");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function WebGLShaderPrecisionFormat() { [native code] }")
    public void webGLShaderPrecisionFormat() throws Exception {
        test("WebGLShaderPrecisionFormat");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function WebGLSync() { [native code] }")
    public void webGLSync() throws Exception {
        test("WebGLSync");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function WebGLTexture() { [native code] }")
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
    @Alerts("function WebGLTransformFeedback() { [native code] }")
    public void webGLTransformFeedback() throws Exception {
        test("WebGLTransformFeedback");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function WebGLUniformLocation() { [native code] }")
    public void webGLUniformLocation() throws Exception {
        test("WebGLUniformLocation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function WebGLVertexArrayObject() { [native code] }")
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
    @Alerts("function DOMMatrix() { [native code] }")
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
    @Alerts("function URL() { [native code] }")
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
    @Alerts("function WebSocket() { [native code] }")
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
    @Alerts("function WheelEvent() { [native code] }")
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
    @Alerts("function Window() { [native code] }")
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
    @Alerts("function Worker() { [native code] }")
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
    @Alerts("function XMLDocument() { [native code] }")
    public void xmlDocument() throws Exception {
        test("XMLDocument");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.xml.XMLHttpRequest}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function XMLHttpRequest() { [native code] }")
    public void xmlHttpRequest() throws Exception {
        test("XMLHttpRequest");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function XMLHttpRequestEventTarget() { [native code] }")
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
    @Alerts("function XMLHttpRequestUpload() { [native code] }")
    public void xmlHttpRequestUpload() throws Exception {
        test("XMLHttpRequestUpload");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.xml.XMLSerializer}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function XMLSerializer() { [native code] }")
    public void xmlSerializer() throws Exception {
        test("XMLSerializer");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.dom.XPathEvaluator}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function XPathEvaluator() { [native code] }")
    public void xPathEvaluator() throws Exception {
        test("XPathEvaluator");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function XPathExpression() { [native code] }")
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
    @Alerts("function XPathResult() { [native code] }")
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
    @Alerts("function XSLTProcessor() { [native code] }")
    public void xsltProcessor() throws Exception {
        test("XSLTProcessor");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.abort.AbortController}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function AbortController() { [native code] }")
    public void abortController() throws Exception {
        test("AbortController");
    }

    /**
     * Test {@link org.htmlunit.javascript.host.abort.AbortSignal}.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("function AbortSignal() { [native code] }")
    public void abortSignal() throws Exception {
        test("AbortSignal");
    }
}
