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
 * This class handles all host names which starts by character 'T' to 'V'.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HostParentOfTTest extends HostParentOf {

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    public static Collection<Arguments> data() throws Exception {
        return HostParentOf.data(input -> {
            final char ch = Character.toUpperCase(input.charAt(0));
            return ch >= 'T' && ch <= 'V';
        });
    }

    @Alerts("true/true")
    void _Text_CDATASection() throws Exception {
        test("Text", "CDATASection");
    }

    @Alerts("true/false")
    void _Text_Text() throws Exception {
        test("Text", "Text");
    }

    @Alerts("true/false")
    void _TextDecoder_TextDecoder() throws Exception {
        test("TextDecoder", "TextDecoder");
    }

    @Alerts("true/false")
    void _TextEncoder_TextEncoder() throws Exception {
        test("TextEncoder", "TextEncoder");
    }

    @Alerts(DEFAULT = "true/false",
            FF_ESR = "false/false")
    void _TextEvent_TextEvent() throws Exception {
        test("TextEvent", "TextEvent");
    }

    @Alerts("true/false")
    void _TextMetrics_TextMetrics() throws Exception {
        test("TextMetrics", "TextMetrics");
    }

    @Alerts("false/false")
    void _TextRange_TextRange() throws Exception {
        test("TextRange", "TextRange");
    }

    @Alerts("true/false")
    void _TextTrack_TextTrack() throws Exception {
        test("TextTrack", "TextTrack");
    }

    @Alerts("true/false")
    void _TextTrackCue_TextTrackCue() throws Exception {
        test("TextTrackCue", "TextTrackCue");
    }

    @Alerts("true/true")
    void _TextTrackCue_VTTCue() throws Exception {
        test("TextTrackCue", "VTTCue");
    }

    @Alerts("true/false")
    void _TextTrackCueList_TextTrackCueList() throws Exception {
        test("TextTrackCueList", "TextTrackCueList");
    }

    @Alerts("true/false")
    void _TextTrackList_TextTrackList() throws Exception {
        test("TextTrackList", "TextTrackList");
    }

    @Alerts(DEFAULT = "false/false",
            FF = "true/false",
            FF_ESR = "true/false")
    void _TimeEvent_TimeEvent() throws Exception {
        test("TimeEvent", "TimeEvent");
    }

    @Alerts("true/false")
    void _TimeRanges_TimeRanges() throws Exception {
        test("TimeRanges", "TimeRanges");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _Touch_Touch() throws Exception {
        test("Touch", "Touch");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _TouchEvent_TouchEvent() throws Exception {
        test("TouchEvent", "TouchEvent");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _TouchList_TouchList() throws Exception {
        test("TouchList", "TouchList");
    }

    @Alerts("true/false")
    void _TrackEvent_TrackEvent() throws Exception {
        test("TrackEvent", "TrackEvent");
    }

    @Alerts("true/false")
    void _TransitionEvent_TransitionEvent() throws Exception {
        test("TransitionEvent", "TransitionEvent");
    }

    @Alerts("true/false")
    void _TreeWalker_TreeWalker() throws Exception {
        test("TreeWalker", "TreeWalker");
    }

    @Alerts("true/true")
    void _UIEvent_CompositionEvent() throws Exception {
        test("UIEvent", "CompositionEvent");
    }

    @Alerts("true/false")
    void _UIEvent_DragEvent() throws Exception {
        test("UIEvent", "DragEvent");
    }

    @Alerts("true/true")
    void _UIEvent_FocusEvent() throws Exception {
        test("UIEvent", "FocusEvent");
    }

    @Alerts("true/true")
    void _UIEvent_InputEvent() throws Exception {
        test("UIEvent", "InputEvent");
    }

    @Alerts("true/true")
    void _UIEvent_KeyboardEvent() throws Exception {
        test("UIEvent", "KeyboardEvent");
    }

    @Alerts("true/true")
    void _UIEvent_MouseEvent() throws Exception {
        test("UIEvent", "MouseEvent");
    }

    @Alerts(DEFAULT = "false/false",
            FF = "true/false",
            FF_ESR = "true/false")
    void _UIEvent_MouseScrollEvent() throws Exception {
        test("UIEvent", "MouseScrollEvent");
    }

    @Alerts("false/false")
    void _UIEvent_MouseWheelEvent() throws Exception {
        test("UIEvent", "MouseWheelEvent");
    }

    @Alerts("false/false")
    void _UIEvent_MSGestureEvent() throws Exception {
        test("UIEvent", "MSGestureEvent");
    }

    @Alerts("true/false")
    void _UIEvent_PointerEvent() throws Exception {
        test("UIEvent", "PointerEvent");
    }

    @Alerts("false/false")
    void _UIEvent_SVGZoomEvent() throws Exception {
        test("UIEvent", "SVGZoomEvent");
    }

    @Alerts(DEFAULT = "true/true",
            FF_ESR = "false/false")
    void _UIEvent_TextEvent() throws Exception {
        test("UIEvent", "TextEvent");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/true",
            EDGE = "true/true")
    void _UIEvent_TouchEvent() throws Exception {
        test("UIEvent", "TouchEvent");
    }

    @Alerts("true/false")
    void _UIEvent_UIEvent() throws Exception {
        test("UIEvent", "UIEvent");
    }

    @Alerts("true/false")
    void _UIEvent_WheelEvent() throws Exception {
        test("UIEvent", "WheelEvent");
    }

    @Alerts("true/false")
    void _Uint16Array_Uint16Array() throws Exception {
        test("Uint16Array", "Uint16Array");
    }

    @Alerts("true/false")
    void _Uint32Array_Uint32Array() throws Exception {
        test("Uint32Array", "Uint32Array");
    }

    @Alerts("true/false")
    void _Uint8Array_Uint8Array() throws Exception {
        test("Uint8Array", "Uint8Array");
    }

    @Alerts("true/false")
    void _Uint8ClampedArray_Uint8ClampedArray() throws Exception {
        test("Uint8ClampedArray", "Uint8ClampedArray");
    }

    @Alerts("true/false")
    void _URIError_URIError() throws Exception {
        test("URIError", "URIError");
    }

    @Alerts("true/false")
    void _URL_URL() throws Exception {
        test("URL", "URL");
    }

    @Alerts("true/false")
    void _URL_webkitURL() throws Exception {
        test("URL", "webkitURL");
    }

    @Alerts("true/false")
    void _URLSearchParams_URLSearchParams() throws Exception {
        test("URLSearchParams", "URLSearchParams");
    }

    @Alerts("false/false")
    void _UserProximityEvent_UserProximityEvent() throws Exception {
        test("UserProximityEvent", "UserProximityEvent");
    }

    @Alerts("true/false")
    void _ValidityState_ValidityState() throws Exception {
        test("ValidityState", "ValidityState");
    }

    @Alerts("true/false")
    void _VideoPlaybackQuality_VideoPlaybackQuality() throws Exception {
        test("VideoPlaybackQuality", "VideoPlaybackQuality");
    }

    @Alerts("true/false")
    void _VTTCue_VTTCue() throws Exception {
        test("VTTCue", "VTTCue");
    }
}
