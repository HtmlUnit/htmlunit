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
package org.htmlunit.general.huge;

import java.util.Collection;

import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.params.provider.Arguments;

/**
 * Tests two Host classes, if one prototype is parent of another.
 *
 * This class handles all host names which starts by character 'A'.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HostParentOfATest extends HostParentOf {

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    public static Collection<Arguments> data() throws Exception {
        return HostParentOf.data(input -> {
            final char ch = Character.toUpperCase(input.charAt(0));
            return ch == 'A';
        });
    }

    @Alerts("true/false")
    void _AbortController_AbortController() throws Exception {
        test("AbortController", "AbortController");
    }

    @Alerts("true/false")
    void _AbortSignal_AbortSignal() throws Exception {
        test("AbortSignal", "AbortSignal");
    }

    @Alerts("true/false")
    void _AbstractRange_AbstractRange() throws Exception {
        test("AbstractRange", "AbstractRange");
    }

    @Alerts("true/true")
    void _AbstractRange_Range() throws Exception {
        test("AbstractRange", "Range");
    }

    @Alerts("false/false")
    void _ActiveXObject_ActiveXObject() throws Exception {
        test("ActiveXObject", "ActiveXObject");
    }

    @Alerts("true/false")
    void _AnalyserNode_AnalyserNode() throws Exception {
        test("AnalyserNode", "AnalyserNode");
    }

    @Alerts("false/false")
    void _ANGLE_instanced_arrays_ANGLE_instanced_arrays() throws Exception {
        test("ANGLE_instanced_arrays", "ANGLE_instanced_arrays");
    }

    @Alerts("true/false")
    void _Animation_Animation() throws Exception {
        test("Animation", "Animation");
    }

    @Alerts("true/false")
    void _AnimationEvent_AnimationEvent() throws Exception {
        test("AnimationEvent", "AnimationEvent");
    }

    @Alerts("false/false")
    void _ApplicationCache_ApplicationCache() throws Exception {
        test("ApplicationCache", "ApplicationCache");
    }

    @Alerts("false/false")
    void _ApplicationCacheErrorEvent_ApplicationCacheErrorEvent() throws Exception {
        test("ApplicationCacheErrorEvent", "ApplicationCacheErrorEvent");
    }

    @Alerts("true/false")
    void _ArrayBuffer_ArrayBuffer() throws Exception {
        test("ArrayBuffer", "ArrayBuffer");
    }

    @Alerts("false/false")
    @HtmlUnitNYI(CHROME = "true/false",
            EDGE = "true/false",
            FF = "true/false",
            FF_ESR = "true/false")
    void _Atomics_Atomics() throws Exception {
        test("Atomics", "Atomics");
    }

    @Alerts("true/false")
    void _Attr_Attr() throws Exception {
        test("Attr", "Attr");
    }

    @Alerts("true/false")
    void _Audio_Audio() throws Exception {
        test("Audio", "Audio");
    }

    @Alerts("true/false")
    @HtmlUnitNYI(CHROME = "false/false",
            EDGE = "false/false",
            FF = "false/false",
            FF_ESR = "false/false")
    void _Audio_HTMLAudioElement() throws Exception {
        test("Audio", "HTMLAudioElement");
    }

    @Alerts("true/false")
    void _AudioBuffer_AudioBuffer() throws Exception {
        test("AudioBuffer", "AudioBuffer");
    }

    @Alerts("true/false")
    void _AudioBufferSourceNode_AudioBufferSourceNode() throws Exception {
        test("AudioBufferSourceNode", "AudioBufferSourceNode");
    }

    @Alerts("true/false")
    void _AudioContext_AudioContext() throws Exception {
        test("AudioContext", "AudioContext");
    }

    @Alerts("true/false")
    void _AudioDestinationNode_AudioDestinationNode() throws Exception {
        test("AudioDestinationNode", "AudioDestinationNode");
    }

    @Alerts("true/false")
    void _AudioListener_AudioListener() throws Exception {
        test("AudioListener", "AudioListener");
    }

    @Alerts("true/true")
    void _AudioNode_AnalyserNode() throws Exception {
        test("AudioNode", "AnalyserNode");
    }

    @Alerts("true/false")
    void _AudioNode_AudioBufferSourceNode() throws Exception {
        test("AudioNode", "AudioBufferSourceNode");
    }

    @Alerts("true/true")
    void _AudioNode_AudioDestinationNode() throws Exception {
        test("AudioNode", "AudioDestinationNode");
    }

    @Alerts("true/false")
    void _AudioNode_AudioNode() throws Exception {
        test("AudioNode", "AudioNode");
    }

    @Alerts("true/true")
    void _AudioNode_AudioScheduledSourceNode() throws Exception {
        test("AudioNode", "AudioScheduledSourceNode");
    }

    @Alerts("true/true")
    void _AudioNode_BiquadFilterNode() throws Exception {
        test("AudioNode", "BiquadFilterNode");
    }

    @Alerts("true/true")
    void _AudioNode_ChannelMergerNode() throws Exception {
        test("AudioNode", "ChannelMergerNode");
    }

    @Alerts("true/true")
    void _AudioNode_ChannelSplitterNode() throws Exception {
        test("AudioNode", "ChannelSplitterNode");
    }

    @Alerts("true/false")
    void _AudioNode_ConstantSourceNode() throws Exception {
        test("AudioNode", "ConstantSourceNode");
    }

    @Alerts("true/true")
    void _AudioNode_ConvolverNode() throws Exception {
        test("AudioNode", "ConvolverNode");
    }

    @Alerts("true/true")
    void _AudioNode_DelayNode() throws Exception {
        test("AudioNode", "DelayNode");
    }

    @Alerts("true/true")
    void _AudioNode_DynamicsCompressorNode() throws Exception {
        test("AudioNode", "DynamicsCompressorNode");
    }

    @Alerts("true/true")
    void _AudioNode_GainNode() throws Exception {
        test("AudioNode", "GainNode");
    }

    @Alerts("true/true")
    void _AudioNode_IIRFilterNode() throws Exception {
        test("AudioNode", "IIRFilterNode");
    }

    @Alerts("true/true")
    void _AudioNode_MediaElementAudioSourceNode() throws Exception {
        test("AudioNode", "MediaElementAudioSourceNode");
    }

    @Alerts("true/true")
    void _AudioNode_MediaStreamAudioDestinationNode() throws Exception {
        test("AudioNode", "MediaStreamAudioDestinationNode");
    }

    @Alerts("true/true")
    void _AudioNode_MediaStreamAudioSourceNode() throws Exception {
        test("AudioNode", "MediaStreamAudioSourceNode");
    }

    @Alerts("true/false")
    void _AudioNode_OscillatorNode() throws Exception {
        test("AudioNode", "OscillatorNode");
    }

    @Alerts("true/true")
    void _AudioNode_PannerNode() throws Exception {
        test("AudioNode", "PannerNode");
    }

    @Alerts("true/true")
    void _AudioNode_ScriptProcessorNode() throws Exception {
        test("AudioNode", "ScriptProcessorNode");
    }

    @Alerts("true/true")
    void _AudioNode_StereoPannerNode() throws Exception {
        test("AudioNode", "StereoPannerNode");
    }

    @Alerts("true/true")
    void _AudioNode_WaveShaperNode() throws Exception {
        test("AudioNode", "WaveShaperNode");
    }

    @Alerts("true/false")
    void _AudioParam_AudioParam() throws Exception {
        test("AudioParam", "AudioParam");
    }

    @Alerts("true/false")
    void _AudioProcessingEvent_AudioProcessingEvent() throws Exception {
        test("AudioProcessingEvent", "AudioProcessingEvent");
    }

    @Alerts("true/true")
    void _AudioScheduledSourceNode_AudioBufferSourceNode() throws Exception {
        test("AudioScheduledSourceNode", "AudioBufferSourceNode");
    }

    @Alerts("true/false")
    void _AudioScheduledSourceNode_AudioScheduledSourceNode() throws Exception {
        test("AudioScheduledSourceNode", "AudioScheduledSourceNode");
    }

    @Alerts("true/true")
    void _AudioScheduledSourceNode_ConstantSourceNode() throws Exception {
        test("AudioScheduledSourceNode", "ConstantSourceNode");
    }

    @Alerts("true/true")
    void _AudioScheduledSourceNode_OscillatorNode() throws Exception {
        test("AudioScheduledSourceNode", "OscillatorNode");
    }
}
