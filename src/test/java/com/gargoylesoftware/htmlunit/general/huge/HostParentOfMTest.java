/*
 * Copyright (c) 2002-2019 Gargoyle Software Inc.
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
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

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
 * This class handles all host names which starts by character 'M' to 'O'.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserParameterizedRunner.class)
public class HostParentOfMTest extends HostParentOf {

    /**
     * Returns the parameterized data.
     *
     * @return the parameterized data
     * @throws Exception
     *             if an error occurs
     */
    @Parameters
    public static Collection<Object[]> data() throws Exception {
        return HostParentOf.data(input -> {
            final char ch = Character.toUpperCase(input.charAt(0));
            return ch == 'M';
        });
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Map_Map() throws Exception {
        test("Map", "Map");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MediaDeviceInfo_MediaDeviceInfo() throws Exception {
        test("MediaDeviceInfo", "MediaDeviceInfo");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MediaDevices_MediaDevices() throws Exception {
        test("MediaDevices", "MediaDevices");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MediaElementAudioSourceNode_MediaElementAudioSourceNode() throws Exception {
        test("MediaElementAudioSourceNode", "MediaElementAudioSourceNode");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MediaEncryptedEvent_MediaEncryptedEvent() throws Exception {
        test("MediaEncryptedEvent", "MediaEncryptedEvent");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _MediaError_MediaError() throws Exception {
        test("MediaError", "MediaError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _MediaKeyError_MediaKeyError() throws Exception {
        test("MediaKeyError", "MediaKeyError");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MediaKeyMessageEvent_MediaKeyMessageEvent() throws Exception {
        test("MediaKeyMessageEvent", "MediaKeyMessageEvent");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MediaKeys_MediaKeys() throws Exception {
        test("MediaKeys", "MediaKeys");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MediaKeySession_MediaKeySession() throws Exception {
        test("MediaKeySession", "MediaKeySession");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MediaKeyStatusMap_MediaKeyStatusMap() throws Exception {
        test("MediaKeyStatusMap", "MediaKeyStatusMap");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MediaKeySystemAccess_MediaKeySystemAccess() throws Exception {
        test("MediaKeySystemAccess", "MediaKeySystemAccess");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _MediaList_MediaList() throws Exception {
        test("MediaList", "MediaList");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _MediaQueryList_MediaQueryList() throws Exception {
        test("MediaQueryList", "MediaQueryList");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            FF60 = "true")
    public void _MediaQueryListEvent_MediaQueryListEvent() throws Exception {
        test("MediaQueryListEvent", "MediaQueryListEvent");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MediaRecorder_MediaRecorder() throws Exception {
        test("MediaRecorder", "MediaRecorder");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MediaSource_MediaSource() throws Exception {
        test("MediaSource", "MediaSource");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _MediaStream_CanvasCaptureMediaStream() throws Exception {
        test("MediaStream", "CanvasCaptureMediaStream");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _MediaStream_LocalMediaStream() throws Exception {
        test("MediaStream", "LocalMediaStream");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MediaStream_MediaStream() throws Exception {
        test("MediaStream", "MediaStream");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    @NotYetImplemented(CHROME)
    public void _MediaStream_webkitMediaStream() throws Exception {
        test("MediaStream", "webkitMediaStream");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MediaStreamAudioDestinationNode_MediaStreamAudioDestinationNode() throws Exception {
        test("MediaStreamAudioDestinationNode", "MediaStreamAudioDestinationNode");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MediaStreamAudioSourceNode_MediaStreamAudioSourceNode() throws Exception {
        test("MediaStreamAudioSourceNode", "MediaStreamAudioSourceNode");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MediaStreamEvent_MediaStreamEvent() throws Exception {
        test("MediaStreamEvent", "MediaStreamEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _MediaStreamTrack_CanvasCaptureMediaStreamTrack() throws Exception {
        test("MediaStreamTrack", "CanvasCaptureMediaStreamTrack");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MediaStreamTrack_MediaStreamTrack() throws Exception {
        test("MediaStreamTrack", "MediaStreamTrack");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _MediaStreamTrackEvent_MediaStreamTrackEvent() throws Exception {
        test("MediaStreamTrackEvent", "MediaStreamTrackEvent");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _MessageChannel_MessageChannel() throws Exception {
        test("MessageChannel", "MessageChannel");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _MessageEvent_MessageEvent() throws Exception {
        test("MessageEvent", "MessageEvent");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _MessagePort_MessagePort() throws Exception {
        test("MessagePort", "MessagePort");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _MIDIAccess_MIDIAccess() throws Exception {
        test("MIDIAccess", "MIDIAccess");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _MIDIConnectionEvent_MIDIConnectionEvent() throws Exception {
        test("MIDIConnectionEvent", "MIDIConnectionEvent");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _MIDIInput_MIDIInput() throws Exception {
        test("MIDIInput", "MIDIInput");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _MIDIInputMap_MIDIInputMap() throws Exception {
        test("MIDIInputMap", "MIDIInputMap");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _MIDIMessageEvent_MIDIMessageEvent() throws Exception {
        test("MIDIMessageEvent", "MIDIMessageEvent");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _MIDIOutput_MIDIOutput() throws Exception {
        test("MIDIOutput", "MIDIOutput");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _MIDIOutputMap_MIDIOutputMap() throws Exception {
        test("MIDIOutputMap", "MIDIOutputMap");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _MIDIPort_MIDIInput() throws Exception {
        test("MIDIPort", "MIDIInput");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _MIDIPort_MIDIOutput() throws Exception {
        test("MIDIPort", "MIDIOutput");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _MIDIPort_MIDIPort() throws Exception {
        test("MIDIPort", "MIDIPort");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _MimeType_MimeType() throws Exception {
        test("MimeType", "MimeType");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _MimeTypeArray_MimeTypeArray() throws Exception {
        test("MimeTypeArray", "MimeTypeArray");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _MouseEvent_DragEvent() throws Exception {
        test("MouseEvent", "DragEvent");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _MouseEvent_MouseEvent() throws Exception {
        test("MouseEvent", "MouseEvent");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _MouseEvent_MouseScrollEvent() throws Exception {
        test("MouseEvent", "MouseScrollEvent");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    @NotYetImplemented(IE)
    public void _MouseEvent_MouseWheelEvent() throws Exception {
        test("MouseEvent", "MouseWheelEvent");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            FF52 = "false")
    public void _MouseEvent_PointerEvent() throws Exception {
        test("MouseEvent", "PointerEvent");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _MouseEvent_WheelEvent() throws Exception {
        test("MouseEvent", "WheelEvent");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _MouseScrollEvent_MouseScrollEvent() throws Exception {
        test("MouseScrollEvent", "MouseScrollEvent");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _MouseWheelEvent_MouseWheelEvent() throws Exception {
        test("MouseWheelEvent", "MouseWheelEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _mozRTCIceCandidate_mozRTCIceCandidate() throws Exception {
        test("mozRTCIceCandidate", "mozRTCIceCandidate");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _mozRTCPeerConnection_mozRTCPeerConnection() throws Exception {
        test("mozRTCPeerConnection", "mozRTCPeerConnection");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true")
    public void _mozRTCSessionDescription_mozRTCSessionDescription() throws Exception {
        test("mozRTCSessionDescription", "mozRTCSessionDescription");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _MSGestureEvent_MSGestureEvent() throws Exception {
        test("MSGestureEvent", "MSGestureEvent");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _MutationEvent_MutationEvent() throws Exception {
        test("MutationEvent", "MutationEvent");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _MutationObserver_MutationObserver() throws Exception {
        test("MutationObserver", "MutationObserver");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true")
    public void _MutationObserver_WebKitMutationObserver() throws Exception {
        test("MutationObserver", "WebKitMutationObserver");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _MutationRecord_MutationRecord() throws Exception {
        test("MutationRecord", "MutationRecord");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _NamedNodeMap_NamedNodeMap() throws Exception {
        test("NamedNodeMap", "NamedNodeMap");
    }
}
