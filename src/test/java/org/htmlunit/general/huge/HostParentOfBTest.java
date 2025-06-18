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

import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.params.provider.Arguments;

/**
 * Tests two Host classes, if one prototype is parent of another.
 *
 * This class handles all host names which starts by character 'B'.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HostParentOfBTest extends HostParentOf {

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    public static Collection<Arguments> data() throws Exception {
        return HostParentOf.data(input -> {
            final char ch = Character.toUpperCase(input.charAt(0));
            return ch == 'B';
        });
    }

    @Alerts("true/false")
    void _BarProp_BarProp() throws Exception {
        test("BarProp", "BarProp");
    }

    @Alerts("true/true")
    void _BaseAudioContext_AudioContext() throws Exception {
        test("BaseAudioContext", "AudioContext");
    }

    @Alerts("true/false")
    void _BaseAudioContext_BaseAudioContext() throws Exception {
        test("BaseAudioContext", "BaseAudioContext");
    }

    @Alerts("true/true")
    void _BaseAudioContext_OfflineAudioContext() throws Exception {
        test("BaseAudioContext", "OfflineAudioContext");
    }

    @Alerts(DEFAULT = "true/false",
            FF = "false/false",
            FF_ESR = "false/false")
    void _BatteryManager_BatteryManager() throws Exception {
        test("BatteryManager", "BatteryManager");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _BeforeInstallPromptEvent_BeforeInstallPromptEvent() throws Exception {
        test("BeforeInstallPromptEvent", "BeforeInstallPromptEvent");
    }

    @Alerts("true/false")
    void _BeforeUnloadEvent_BeforeUnloadEvent() throws Exception {
        test("BeforeUnloadEvent", "BeforeUnloadEvent");
    }

    @Alerts("true/false")
    void _BiquadFilterNode_BiquadFilterNode() throws Exception {
        test("BiquadFilterNode", "BiquadFilterNode");
    }

    @Alerts("true/false")
    void _Blob_Blob() throws Exception {
        test("Blob", "Blob");
    }

    @Alerts("true/true")
    void _Blob_File() throws Exception {
        test("Blob", "File");
    }

    @Alerts("true/false")
    void _BlobEvent_BlobEvent() throws Exception {
        test("BlobEvent", "BlobEvent");
    }

    @Alerts("true/false")
    void _BroadcastChannel_BroadcastChannel() throws Exception {
        test("BroadcastChannel", "BroadcastChannel");
    }
}
