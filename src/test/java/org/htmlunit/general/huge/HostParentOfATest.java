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

import java.util.Collection;

import org.htmlunit.junit.BrowserParameterizedRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests two Host classes, if one prototype is parent of another.
 *
 * This class handles all host names which starts by character 'A'.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
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
            return ch == 'A';
        });
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _AbortController_AbortController() throws Exception {
        test("AbortController", "AbortController");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _AbortSignal_AbortSignal() throws Exception {
        test("AbortSignal", "AbortSignal");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _AbstractRange_AbstractRange() throws Exception {
        test("AbstractRange", "AbstractRange");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _AbstractRange_Range() throws Exception {
        test("AbstractRange", "Range");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false/false")
    public void _ActiveXObject_ActiveXObject() throws Exception {
        test("ActiveXObject", "ActiveXObject");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _AnalyserNode_AnalyserNode() throws Exception {
        test("AnalyserNode", "AnalyserNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false/false")
    public void _ANGLE_instanced_arrays_ANGLE_instanced_arrays() throws Exception {
        test("ANGLE_instanced_arrays", "ANGLE_instanced_arrays");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _Animation_Animation() throws Exception {
        test("Animation", "Animation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _AnimationEvent_AnimationEvent() throws Exception {
        test("AnimationEvent", "AnimationEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false/false")
    public void _ApplicationCache_ApplicationCache() throws Exception {
        test("ApplicationCache", "ApplicationCache");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false/false")
    public void _ApplicationCacheErrorEvent_ApplicationCacheErrorEvent() throws Exception {
        test("ApplicationCacheErrorEvent", "ApplicationCacheErrorEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _ArrayBuffer_ArrayBuffer() throws Exception {
        test("ArrayBuffer", "ArrayBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false/false")
    @HtmlUnitNYI(CHROME = "true/false",
            EDGE = "true/false",
            FF = "true/false",
            FF_ESR = "true/false")
    public void _Atomics_Atomics() throws Exception {
        test("Atomics", "Atomics");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _Attr_Attr() throws Exception {
        test("Attr", "Attr");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _Audio_Audio() throws Exception {
        test("Audio", "Audio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    @HtmlUnitNYI(CHROME = "false/false",
            EDGE = "false/false",
            FF = "false/false",
            FF_ESR = "false/false")
    public void _Audio_HTMLAudioElement() throws Exception {
        test("Audio", "HTMLAudioElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _AudioBuffer_AudioBuffer() throws Exception {
        test("AudioBuffer", "AudioBuffer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _AudioBufferSourceNode_AudioBufferSourceNode() throws Exception {
        test("AudioBufferSourceNode", "AudioBufferSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _AudioContext_AudioContext() throws Exception {
        test("AudioContext", "AudioContext");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _AudioDestinationNode_AudioDestinationNode() throws Exception {
        test("AudioDestinationNode", "AudioDestinationNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _AudioListener_AudioListener() throws Exception {
        test("AudioListener", "AudioListener");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _AudioNode_AnalyserNode() throws Exception {
        test("AudioNode", "AnalyserNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _AudioNode_AudioBufferSourceNode() throws Exception {
        test("AudioNode", "AudioBufferSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _AudioNode_AudioDestinationNode() throws Exception {
        test("AudioNode", "AudioDestinationNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _AudioNode_AudioNode() throws Exception {
        test("AudioNode", "AudioNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _AudioNode_AudioScheduledSourceNode() throws Exception {
        test("AudioNode", "AudioScheduledSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _AudioNode_BiquadFilterNode() throws Exception {
        test("AudioNode", "BiquadFilterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _AudioNode_ChannelMergerNode() throws Exception {
        test("AudioNode", "ChannelMergerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _AudioNode_ChannelSplitterNode() throws Exception {
        test("AudioNode", "ChannelSplitterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _AudioNode_ConstantSourceNode() throws Exception {
        test("AudioNode", "ConstantSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _AudioNode_ConvolverNode() throws Exception {
        test("AudioNode", "ConvolverNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _AudioNode_DelayNode() throws Exception {
        test("AudioNode", "DelayNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _AudioNode_DynamicsCompressorNode() throws Exception {
        test("AudioNode", "DynamicsCompressorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _AudioNode_GainNode() throws Exception {
        test("AudioNode", "GainNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _AudioNode_IIRFilterNode() throws Exception {
        test("AudioNode", "IIRFilterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _AudioNode_MediaElementAudioSourceNode() throws Exception {
        test("AudioNode", "MediaElementAudioSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _AudioNode_MediaStreamAudioDestinationNode() throws Exception {
        test("AudioNode", "MediaStreamAudioDestinationNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _AudioNode_MediaStreamAudioSourceNode() throws Exception {
        test("AudioNode", "MediaStreamAudioSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _AudioNode_OscillatorNode() throws Exception {
        test("AudioNode", "OscillatorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _AudioNode_PannerNode() throws Exception {
        test("AudioNode", "PannerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _AudioNode_ScriptProcessorNode() throws Exception {
        test("AudioNode", "ScriptProcessorNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _AudioNode_StereoPannerNode() throws Exception {
        test("AudioNode", "StereoPannerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _AudioNode_WaveShaperNode() throws Exception {
        test("AudioNode", "WaveShaperNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _AudioParam_AudioParam() throws Exception {
        test("AudioParam", "AudioParam");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _AudioProcessingEvent_AudioProcessingEvent() throws Exception {
        test("AudioProcessingEvent", "AudioProcessingEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _AudioScheduledSourceNode_AudioBufferSourceNode() throws Exception {
        test("AudioScheduledSourceNode", "AudioBufferSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _AudioScheduledSourceNode_AudioScheduledSourceNode() throws Exception {
        test("AudioScheduledSourceNode", "AudioScheduledSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _AudioScheduledSourceNode_ConstantSourceNode() throws Exception {
        test("AudioScheduledSourceNode", "ConstantSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _AudioScheduledSourceNode_OscillatorNode() throws Exception {
        test("AudioScheduledSourceNode", "OscillatorNode");
    }
}
