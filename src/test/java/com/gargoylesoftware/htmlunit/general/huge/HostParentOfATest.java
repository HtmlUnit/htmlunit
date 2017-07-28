/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF52;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import com.gargoylesoftware.htmlunit.BrowserParameterizedRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests two Host classes, if one prototype is parent of another.
 *
 * This class handles all host names which starts by character 'A' to 'C'.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserParameterizedRunner.class)
public class HostParentOfATest extends HostParentOf {

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    @Parameters
    public static Collection<Object[]> data() throws Exception {
        return HostParentOf.data(input -> {
            final char ch = Character.toUpperCase(input.charAt(0));
            return ch >= 'A' && ch <= 'C';
        });
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _ActiveXObject_ActiveXObject() throws Exception {
        test("ActiveXObject", "ActiveXObject");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AnalyserNode_AnalyserNode() throws Exception {
        test("AnalyserNode", "AnalyserNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _ANGLE_instanced_arrays_ANGLE_instanced_arrays() throws Exception {
        test("ANGLE_instanced_arrays", "ANGLE_instanced_arrays");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF52 = "true")
    public void _Animation_Animation() throws Exception {
        test("Animation", "Animation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _AnimationEvent_AnimationEvent() throws Exception {
        test("AnimationEvent", "AnimationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _AnimationEvent_WebKitAnimationEvent() throws Exception {
        test("AnimationEvent", "WebKitAnimationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _AppBannerPromptResult_AppBannerPromptResult() throws Exception {
        test("AppBannerPromptResult", "AppBannerPromptResult");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false")
    public void _ApplicationCache_ApplicationCache() throws Exception {
        test("ApplicationCache", "ApplicationCache");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _ApplicationCacheErrorEvent_ApplicationCacheErrorEvent() throws Exception {
        test("ApplicationCacheErrorEvent", "ApplicationCacheErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _ArrayBuffer_ArrayBuffer() throws Exception {
        test("ArrayBuffer", "ArrayBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Attr_Attr() throws Exception {
        test("Attr", "Attr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Audio_Audio() throws Exception {
        test("Audio", "Audio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            FF52 = "true")
    @NotYetImplemented(FF52)
    public void _Audio_HTMLAudioElement() throws Exception {
        test("Audio", "HTMLAudioElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioBuffer_AudioBuffer() throws Exception {
        test("AudioBuffer", "AudioBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioBufferSourceNode_AudioBufferSourceNode() throws Exception {
        test("AudioBufferSourceNode", "AudioBufferSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioContext_AudioContext() throws Exception {
        test("AudioContext", "AudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    @NotYetImplemented(CHROME)
    public void _AudioContext_OfflineAudioContext() throws Exception {
        test("AudioContext", "OfflineAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioDestinationNode_AudioDestinationNode() throws Exception {
        test("AudioDestinationNode", "AudioDestinationNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioListener_AudioListener() throws Exception {
        test("AudioListener", "AudioListener");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_AnalyserNode() throws Exception {
        test("AudioNode", "AnalyserNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_AudioBufferSourceNode() throws Exception {
        test("AudioNode", "AudioBufferSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_AudioDestinationNode() throws Exception {
        test("AudioNode", "AudioDestinationNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_AudioNode() throws Exception {
        test("AudioNode", "AudioNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _AudioNode_AudioScheduledSourceNode() throws Exception {
        test("AudioNode", "AudioScheduledSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_BiquadFilterNode() throws Exception {
        test("AudioNode", "BiquadFilterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_ChannelMergerNode() throws Exception {
        test("AudioNode", "ChannelMergerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_ChannelSplitterNode() throws Exception {
        test("AudioNode", "ChannelSplitterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF52 = "true",
            CHROME = "true")
    @NotYetImplemented(FF52)
    public void _AudioNode_ConstantSourceNode() throws Exception {
        test("AudioNode", "ConstantSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_ConvolverNode() throws Exception {
        test("AudioNode", "ConvolverNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_DelayNode() throws Exception {
        test("AudioNode", "DelayNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_DynamicsCompressorNode() throws Exception {
        test("AudioNode", "DynamicsCompressorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_GainNode() throws Exception {
        test("AudioNode", "GainNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF52 = "true",
            CHROME = "true")
    public void _AudioNode_IIRFilterNode() throws Exception {
        test("AudioNode", "IIRFilterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_MediaElementAudioSourceNode() throws Exception {
        test("AudioNode", "MediaElementAudioSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_MediaStreamAudioDestinationNode() throws Exception {
        test("AudioNode", "MediaStreamAudioDestinationNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_MediaStreamAudioSourceNode() throws Exception {
        test("AudioNode", "MediaStreamAudioSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_OscillatorNode() throws Exception {
        test("AudioNode", "OscillatorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_PannerNode() throws Exception {
        test("AudioNode", "PannerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_ScriptProcessorNode() throws Exception {
        test("AudioNode", "ScriptProcessorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_StereoPannerNode() throws Exception {
        test("AudioNode", "StereoPannerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioNode_WaveShaperNode() throws Exception {
        test("AudioNode", "WaveShaperNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioParam_AudioParam() throws Exception {
        test("AudioParam", "AudioParam");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _AudioProcessingEvent_AudioProcessingEvent() throws Exception {
        test("AudioProcessingEvent", "AudioProcessingEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    @NotYetImplemented(CHROME)
    public void _AudioScheduledSourceNode_AudioBufferSourceNode() throws Exception {
        test("AudioScheduledSourceNode", "AudioBufferSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _AudioScheduledSourceNode_AudioScheduledSourceNode() throws Exception {
        test("AudioScheduledSourceNode", "AudioScheduledSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _AudioScheduledSourceNode_ConstantSourceNode() throws Exception {
        test("AudioScheduledSourceNode", "ConstantSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    @NotYetImplemented(CHROME)
    public void _AudioScheduledSourceNode_OscillatorNode() throws Exception {
        test("AudioScheduledSourceNode", "OscillatorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _BarProp_BarProp() throws Exception {
        test("BarProp", "BarProp");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _BaseAudioContext_AudioContext() throws Exception {
        test("BaseAudioContext", "AudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _BaseAudioContext_BaseAudioContext() throws Exception {
        test("BaseAudioContext", "BaseAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _BaseAudioContext_OfflineAudioContext() throws Exception {
        test("BaseAudioContext", "OfflineAudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _BatteryManager_BatteryManager() throws Exception {
        test("BatteryManager", "BatteryManager");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _BeforeInstallPromptEvent_BeforeInstallPromptEvent() throws Exception {
        test("BeforeInstallPromptEvent", "BeforeInstallPromptEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _BeforeUnloadEvent_BeforeUnloadEvent() throws Exception {
        test("BeforeUnloadEvent", "BeforeUnloadEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _BiquadFilterNode_BiquadFilterNode() throws Exception {
        test("BiquadFilterNode", "BiquadFilterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Blob_Blob() throws Exception {
        test("Blob", "Blob");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Blob_File() throws Exception {
        test("Blob", "File");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _BlobEvent_BlobEvent() throws Exception {
        test("BlobEvent", "BlobEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _BroadcastChannel_BroadcastChannel() throws Exception {
        test("BroadcastChannel", "BroadcastChannel");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Cache_Cache() throws Exception {
        test("Cache", "Cache");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _CacheStorage_CacheStorage() throws Exception {
        test("CacheStorage", "CacheStorage");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CanvasCaptureMediaStream_CanvasCaptureMediaStream() throws Exception {
        test("CanvasCaptureMediaStream", "CanvasCaptureMediaStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _CanvasCaptureMediaStreamTrack_CanvasCaptureMediaStreamTrack() throws Exception {
        test("CanvasCaptureMediaStreamTrack", "CanvasCaptureMediaStreamTrack");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CanvasGradient_CanvasGradient() throws Exception {
        test("CanvasGradient", "CanvasGradient");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CanvasPattern_CanvasPattern() throws Exception {
        test("CanvasPattern", "CanvasPattern");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CanvasRenderingContext2D_CanvasRenderingContext2D() throws Exception {
        test("CanvasRenderingContext2D", "CanvasRenderingContext2D");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CaretPosition_CaretPosition() throws Exception {
        test("CaretPosition", "CaretPosition");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CDATASection_CDATASection() throws Exception {
        test("CDATASection", "CDATASection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _ChannelMergerNode_ChannelMergerNode() throws Exception {
        test("ChannelMergerNode", "ChannelMergerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _ChannelSplitterNode_ChannelSplitterNode() throws Exception {
        test("ChannelSplitterNode", "ChannelSplitterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CharacterData_CDATASection() throws Exception {
        test("CharacterData", "CDATASection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CharacterData_CharacterData() throws Exception {
        test("CharacterData", "CharacterData");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CharacterData_Comment() throws Exception {
        test("CharacterData", "Comment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CharacterData_ProcessingInstruction() throws Exception {
        test("CharacterData", "ProcessingInstruction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CharacterData_Text() throws Exception {
        test("CharacterData", "Text");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false")
    public void _ClientRect_ClientRect() throws Exception {
        test("ClientRect", "ClientRect");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false")
    public void _ClientRectList_ClientRectList() throws Exception {
        test("ClientRectList", "ClientRectList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _ClipboardEvent_ClipboardEvent() throws Exception {
        test("ClipboardEvent", "ClipboardEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CloseEvent_CloseEvent() throws Exception {
        test("CloseEvent", "CloseEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Comment_Comment() throws Exception {
        test("Comment", "Comment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CompositionEvent_CompositionEvent() throws Exception {
        test("CompositionEvent", "CompositionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Console_Console() throws Exception {
        test("Console", "Console");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF52 = "true",
            CHROME = "true")
    public void _ConstantSourceNode_ConstantSourceNode() throws Exception {
        test("ConstantSourceNode", "ConstantSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _ConvolverNode_ConvolverNode() throws Exception {
        test("ConvolverNode", "ConvolverNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Coordinates_Coordinates() throws Exception {
        test("Coordinates", "Coordinates");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Credential_Credential() throws Exception {
        test("Credential", "Credential");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Credential_FederatedCredential() throws Exception {
        test("Credential", "FederatedCredential");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _Credential_PasswordCredential() throws Exception {
        test("Credential", "PasswordCredential");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _CredentialsContainer_CredentialsContainer() throws Exception {
        test("CredentialsContainer", "CredentialsContainer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _Crypto_Crypto() throws Exception {
        test("Crypto", "Crypto");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _CryptoKey_CryptoKey() throws Exception {
        test("CryptoKey", "CryptoKey");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    @NotYetImplemented(CHROME)
    public void _CSS_CSS() throws Exception {
        test("CSS", "CSS");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSS2Properties_CSS2Properties() throws Exception {
        test("CSS2Properties", "CSS2Properties");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _CSSConditionRule_CSSConditionRule() throws Exception {
        test("CSSConditionRule", "CSSConditionRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _CSSConditionRule_CSSMediaRule() throws Exception {
        test("CSSConditionRule", "CSSMediaRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _CSSConditionRule_CSSSupportsRule() throws Exception {
        test("CSSConditionRule", "CSSSupportsRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSSCounterStyleRule_CSSCounterStyleRule() throws Exception {
        test("CSSCounterStyleRule", "CSSCounterStyleRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSFontFaceRule_CSSFontFaceRule() throws Exception {
        test("CSSFontFaceRule", "CSSFontFaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _CSSGroupingRule_CSSConditionRule() throws Exception {
        test("CSSGroupingRule", "CSSConditionRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _CSSGroupingRule_CSSGroupingRule() throws Exception {
        test("CSSGroupingRule", "CSSGroupingRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _CSSGroupingRule_CSSMediaRule() throws Exception {
        test("CSSGroupingRule", "CSSMediaRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _CSSGroupingRule_CSSSupportsRule() throws Exception {
        test("CSSGroupingRule", "CSSSupportsRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSImportRule_CSSImportRule() throws Exception {
        test("CSSImportRule", "CSSImportRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF45 = "false")
    public void _CSSKeyframeRule_CSSKeyframeRule() throws Exception {
        test("CSSKeyframeRule", "CSSKeyframeRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF45 = "false")
    public void _CSSKeyframesRule_CSSKeyframesRule() throws Exception {
        test("CSSKeyframesRule", "CSSKeyframesRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSMediaRule_CSSMediaRule() throws Exception {
        test("CSSMediaRule", "CSSMediaRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false")
    public void _CSSNamespaceRule_CSSNamespaceRule() throws Exception {
        test("CSSNamespaceRule", "CSSNamespaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSPageRule_CSSPageRule() throws Exception {
        test("CSSPageRule", "CSSPageRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSSPrimitiveValue_CSSPrimitiveValue() throws Exception {
        test("CSSPrimitiveValue", "CSSPrimitiveValue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _CSSRule_CSSConditionRule() throws Exception {
        test("CSSRule", "CSSConditionRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSSRule_CSSCounterStyleRule() throws Exception {
        test("CSSRule", "CSSCounterStyleRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSRule_CSSFontFaceRule() throws Exception {
        test("CSSRule", "CSSFontFaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _CSSRule_CSSGroupingRule() throws Exception {
        test("CSSRule", "CSSGroupingRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSRule_CSSImportRule() throws Exception {
        test("CSSRule", "CSSImportRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF45 = "false")
    public void _CSSRule_CSSKeyframeRule() throws Exception {
        test("CSSRule", "CSSKeyframeRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF45 = "false")
    public void _CSSRule_CSSKeyframesRule() throws Exception {
        test("CSSRule", "CSSKeyframesRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSRule_CSSMediaRule() throws Exception {
        test("CSSRule", "CSSMediaRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF = "false")
    public void _CSSRule_CSSNamespaceRule() throws Exception {
        test("CSSRule", "CSSNamespaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSRule_CSSPageRule() throws Exception {
        test("CSSRule", "CSSPageRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSRule_CSSRule() throws Exception {
        test("CSSRule", "CSSRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSRule_CSSStyleRule() throws Exception {
        test("CSSRule", "CSSStyleRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _CSSRule_CSSSupportsRule() throws Exception {
        test("CSSRule", "CSSSupportsRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _CSSRule_CSSViewportRule() throws Exception {
        test("CSSRule", "CSSViewportRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF45 = "true")
    public void _CSSRule_MozCSSKeyframesRule() throws Exception {
        test("CSSRule", "MozCSSKeyframesRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSRuleList_CSSRuleList() throws Exception {
        test("CSSRuleList", "CSSRuleList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSSStyleDeclaration_CSS2Properties() throws Exception {
        test("CSSStyleDeclaration", "CSS2Properties");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSStyleDeclaration_CSSStyleDeclaration() throws Exception {
        test("CSSStyleDeclaration", "CSSStyleDeclaration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSStyleRule_CSSStyleRule() throws Exception {
        test("CSSStyleRule", "CSSStyleRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CSSStyleSheet_CSSStyleSheet() throws Exception {
        test("CSSStyleSheet", "CSSStyleSheet");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _CSSSupportsRule_CSSSupportsRule() throws Exception {
        test("CSSSupportsRule", "CSSSupportsRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSSValue_CSSPrimitiveValue() throws Exception {
        test("CSSValue", "CSSPrimitiveValue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSSValue_CSSValue() throws Exception {
        test("CSSValue", "CSSValue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSSValue_CSSValueList() throws Exception {
        test("CSSValue", "CSSValueList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _CSSValueList_CSSValueList() throws Exception {
        test("CSSValueList", "CSSValueList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _CSSViewportRule_CSSViewportRule() throws Exception {
        test("CSSViewportRule", "CSSViewportRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _CustomElementRegistry_CustomElementRegistry() throws Exception {
        test("CustomElementRegistry", "CustomElementRegistry");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _CustomEvent_CustomEvent() throws Exception {
        test("CustomEvent", "CustomEvent");
    }
}
