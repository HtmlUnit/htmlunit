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
 * This class handles all host names which starts by character 'M'.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HostParentOfMTest extends HostParentOf {

    /**
     * Returns the parameterized data.
     *
     * @return the parameterized data
     * @throws Exception
     *             if an error occurs
     */
    public static Collection<Arguments> data() throws Exception {
        return HostParentOf.data(input -> {
            final char ch = Character.toUpperCase(input.charAt(0));
            return ch == 'M';
        });
    }

    @Alerts("true/false")
    void _MIDIAccess_MIDIAccess() throws Exception {
        test("MIDIAccess", "MIDIAccess");
    }

    @Alerts("true/false")
    void _MIDIConnectionEvent_MIDIConnectionEvent() throws Exception {
        test("MIDIConnectionEvent", "MIDIConnectionEvent");
    }

    @Alerts("true/false")
    void _MIDIInputMap_MIDIInputMap() throws Exception {
        test("MIDIInputMap", "MIDIInputMap");
    }

    @Alerts("true/false")
    void _MIDIInput_MIDIInput() throws Exception {
        test("MIDIInput", "MIDIInput");
    }

    @Alerts("true/false")
    void _MIDIMessageEvent_MIDIMessageEvent() throws Exception {
        test("MIDIMessageEvent", "MIDIMessageEvent");
    }

    @Alerts("true/false")
    void _MIDIOutputMap_MIDIOutputMap() throws Exception {
        test("MIDIOutputMap", "MIDIOutputMap");
    }

    @Alerts("true/false")
    void _MIDIOutput_MIDIOutput() throws Exception {
        test("MIDIOutput", "MIDIOutput");
    }

    @Alerts("true/true")
    void _MIDIPort_MIDIInput() throws Exception {
        test("MIDIPort", "MIDIInput");
    }

    @Alerts("true/true")
    void _MIDIPort_MIDIOutput() throws Exception {
        test("MIDIPort", "MIDIOutput");
    }

    @Alerts("true/false")
    void _MIDIPort_MIDIPort() throws Exception {
        test("MIDIPort", "MIDIPort");
    }

    @Alerts("true/false")
    void _MediaDeviceInfo_MediaDeviceInfo() throws Exception {
        test("MediaDeviceInfo", "MediaDeviceInfo");
    }

    @Alerts("true/false")
    void _MediaDevices_MediaDevices() throws Exception {
        test("MediaDevices", "MediaDevices");
    }

    @Alerts("true/false")
    void _MediaElementAudioSourceNode_MediaElementAudioSourceNode() throws Exception {
        test("MediaElementAudioSourceNode", "MediaElementAudioSourceNode");
    }

    @Alerts("true/false")
    void _MediaEncryptedEvent_MediaEncryptedEvent() throws Exception {
        test("MediaEncryptedEvent", "MediaEncryptedEvent");
    }

    @Alerts("true/false")
    void _MediaError_MediaError() throws Exception {
        test("MediaError", "MediaError");
    }

    @Alerts(DEFAULT = "false/false",
            FF = "true/false",
            FF_ESR = "true/false")
    void _MediaKeyError_MediaKeyError() throws Exception {
        test("MediaKeyError", "MediaKeyError");
    }

    @Alerts("true/false")
    void _MediaKeyMessageEvent_MediaKeyMessageEvent() throws Exception {
        test("MediaKeyMessageEvent", "MediaKeyMessageEvent");
    }

    @Alerts("true/false")
    void _MediaKeySession_MediaKeySession() throws Exception {
        test("MediaKeySession", "MediaKeySession");
    }

    @Alerts("true/false")
    void _MediaKeyStatusMap_MediaKeyStatusMap() throws Exception {
        test("MediaKeyStatusMap", "MediaKeyStatusMap");
    }

    @Alerts("true/false")
    void _MediaKeySystemAccess_MediaKeySystemAccess() throws Exception {
        test("MediaKeySystemAccess", "MediaKeySystemAccess");
    }

    @Alerts("true/false")
    void _MediaKeys_MediaKeys() throws Exception {
        test("MediaKeys", "MediaKeys");
    }

    @Alerts("true/false")
    void _MediaList_MediaList() throws Exception {
        test("MediaList", "MediaList");
    }

    @Alerts("true/false")
    void _MediaQueryListEvent_MediaQueryListEvent() throws Exception {
        test("MediaQueryListEvent", "MediaQueryListEvent");
    }

    @Alerts("true/false")
    void _MediaQueryList_MediaQueryList() throws Exception {
        test("MediaQueryList", "MediaQueryList");
    }

    @Alerts("true/false")
    void _MediaRecorder_MediaRecorder() throws Exception {
        test("MediaRecorder", "MediaRecorder");
    }

    @Alerts("true/false")
    void _MediaSource_MediaSource() throws Exception {
        test("MediaSource", "MediaSource");
    }

    @Alerts("true/false")
    void _MediaStreamAudioDestinationNode_MediaStreamAudioDestinationNode() throws Exception {
        test("MediaStreamAudioDestinationNode", "MediaStreamAudioDestinationNode");
    }

    @Alerts("true/false")
    void _MediaStreamAudioSourceNode_MediaStreamAudioSourceNode() throws Exception {
        test("MediaStreamAudioSourceNode", "MediaStreamAudioSourceNode");
    }

    @Alerts("true/false")
    void _MediaStreamEvent_MediaStreamEvent() throws Exception {
        test("MediaStreamEvent", "MediaStreamEvent");
    }

    @Alerts("true/false")
    void _MediaStreamTrackEvent_MediaStreamTrackEvent() throws Exception {
        test("MediaStreamTrackEvent", "MediaStreamTrackEvent");
    }

    @Alerts(DEFAULT = "true/true",
            FF = "false/false",
            FF_ESR = "false/false")
    void _MediaStreamTrack_CanvasCaptureMediaStreamTrack() throws Exception {
        test("MediaStreamTrack", "CanvasCaptureMediaStreamTrack");
    }

    @Alerts("true/false")
    void _MediaStreamTrack_MediaStreamTrack() throws Exception {
        test("MediaStreamTrack", "MediaStreamTrack");
    }

    @Alerts("true/false")
    void _MediaStream_MediaStream() throws Exception {
        test("MediaStream", "MediaStream");
    }

    @Alerts(DEFAULT = "false/false",
            FF = "true/true",
            FF_ESR = "true/true")
    void _MediaStream_CanvasCaptureMediaStream() throws Exception {
        test("MediaStream", "CanvasCaptureMediaStream");
    }

    @Alerts(DEFAULT = "true/false",
            FF = "false/false",
            FF_ESR = "false/false")
    void _MediaStream_webkitMediaStream() throws Exception {
        test("MediaStream", "webkitMediaStream");
    }

    @Alerts("true/false")
    void _MessageChannel_MessageChannel() throws Exception {
        test("MessageChannel", "MessageChannel");
    }

    @Alerts("true/false")
    void _MessageEvent_MessageEvent() throws Exception {
        test("MessageEvent", "MessageEvent");
    }

    @Alerts("true/false")
    void _MessagePort_MessagePort() throws Exception {
        test("MessagePort", "MessagePort");
    }

    @Alerts("true/false")
    void _MimeTypeArray_MimeTypeArray() throws Exception {
        test("MimeTypeArray", "MimeTypeArray");
    }

    @Alerts("true/false")
    void _MimeType_MimeType() throws Exception {
        test("MimeType", "MimeType");
    }

    @Alerts("true/true")
    void _MouseEvent_DragEvent() throws Exception {
        test("MouseEvent", "DragEvent");
    }

    @Alerts("true/false")
    void _MouseEvent_MouseEvent() throws Exception {
        test("MouseEvent", "MouseEvent");
    }

    @Alerts(DEFAULT = "false/false",
            FF = "true/true",
            FF_ESR = "true/true")
    void _MouseEvent_MouseScrollEvent() throws Exception {
        test("MouseEvent", "MouseScrollEvent");
    }

    @Alerts("true/true")
    void _MouseEvent_PointerEvent() throws Exception {
        test("MouseEvent", "PointerEvent");
    }

    @Alerts("true/true")
    void _MouseEvent_WheelEvent() throws Exception {
        test("MouseEvent", "WheelEvent");
    }

    @Alerts(DEFAULT = "false/false",
            FF = "true/false",
            FF_ESR = "true/false")
    void _MouseScrollEvent_MouseScrollEvent() throws Exception {
        test("MouseScrollEvent", "MouseScrollEvent");
    }

    @Alerts(DEFAULT = "false/false",
            FF_ESR = "true/false")
    void _MutationEvent_MutationEvent() throws Exception {
        test("MutationEvent", "MutationEvent");
    }

    @Alerts("true/false")
    void _MutationObserver_MutationObserver() throws Exception {
        test("MutationObserver", "MutationObserver");
    }

    @Alerts(DEFAULT = "true/false",
            FF = "false/false",
            FF_ESR = "false/false")
    void _MutationObserver_WebKitMutationObserver() throws Exception {
        test("MutationObserver", "WebKitMutationObserver");
    }

    @Alerts("true/false")
    void _MutationRecord_MutationRecord() throws Exception {
        test("MutationRecord", "MutationRecord");
    }
}
