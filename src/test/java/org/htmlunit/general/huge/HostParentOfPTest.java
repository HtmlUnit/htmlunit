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
 * This class handles all host names which starts by character 'P' to 'R'.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HostParentOfPTest extends HostParentOf {

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
            return ch >= 'P' && ch <= 'R';
        });
    }

    @Alerts("true/false")
    void _PageTransitionEvent_PageTransitionEvent() throws Exception {
        test("PageTransitionEvent", "PageTransitionEvent");
    }

    @Alerts("true/false")
    void _PannerNode_PannerNode() throws Exception {
        test("PannerNode", "PannerNode");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _PasswordCredential_PasswordCredential() throws Exception {
        test("PasswordCredential", "PasswordCredential");
    }

    @Alerts("true/false")
    void _Path2D_Path2D() throws Exception {
        test("Path2D", "Path2D");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _PaymentAddress_PaymentAddress() throws Exception {
        test("PaymentAddress", "PaymentAddress");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _PaymentRequest_PaymentRequest() throws Exception {
        test("PaymentRequest", "PaymentRequest");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _PaymentResponse_PaymentResponse() throws Exception {
        test("PaymentResponse", "PaymentResponse");
    }

    @Alerts("true/false")
    void _Performance_Performance() throws Exception {
        test("Performance", "Performance");
    }

    @Alerts("true/false")
    void _PerformanceEntry_PerformanceEntry() throws Exception {
        test("PerformanceEntry", "PerformanceEntry");
    }

    @Alerts("true/true")
    void _PerformanceEntry_PerformanceMark() throws Exception {
        test("PerformanceEntry", "PerformanceMark");
    }

    @Alerts("true/true")
    void _PerformanceEntry_PerformanceMeasure() throws Exception {
        test("PerformanceEntry", "PerformanceMeasure");
    }

    @Alerts("true/false")
    void _PerformanceEntry_PerformanceNavigationTiming() throws Exception {
        test("PerformanceEntry", "PerformanceNavigationTiming");
    }

    @Alerts("true/true")
    void _PerformanceEntry_PerformanceResourceTiming() throws Exception {
        test("PerformanceEntry", "PerformanceResourceTiming");
    }

    @Alerts("true/false")
    void _PerformanceMark_PerformanceMark() throws Exception {
        test("PerformanceMark", "PerformanceMark");
    }

    @Alerts("true/false")
    void _PerformanceMeasure_PerformanceMeasure() throws Exception {
        test("PerformanceMeasure", "PerformanceMeasure");
    }

    @Alerts("true/false")
    void _PerformanceNavigation_PerformanceNavigation() throws Exception {
        test("PerformanceNavigation", "PerformanceNavigation");
    }

    @Alerts("true/false")
    void _PerformanceNavigationTiming_PerformanceNavigationTiming() throws Exception {
        test("PerformanceNavigationTiming", "PerformanceNavigationTiming");
    }

    @Alerts("true/false")
    void _PerformanceObserver_PerformanceObserver() throws Exception {
        test("PerformanceObserver", "PerformanceObserver");
    }

    @Alerts("true/false")
    void _PerformanceObserverEntryList_PerformanceObserverEntryList() throws Exception {
        test("PerformanceObserverEntryList", "PerformanceObserverEntryList");
    }

    @Alerts("true/true")
    void _PerformanceResourceTiming_PerformanceNavigationTiming() throws Exception {
        test("PerformanceResourceTiming", "PerformanceNavigationTiming");
    }

    @Alerts("true/false")
    void _PerformanceResourceTiming_PerformanceResourceTiming() throws Exception {
        test("PerformanceResourceTiming", "PerformanceResourceTiming");
    }

    @Alerts("true/false")
    void _PerformanceTiming_PerformanceTiming() throws Exception {
        test("PerformanceTiming", "PerformanceTiming");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _PeriodicSyncManager_PeriodicSyncManager() throws Exception {
        test("PeriodicSyncManager", "PeriodicSyncManager");
    }

    @Alerts("true/false")
    void _PeriodicWave_PeriodicWave() throws Exception {
        test("PeriodicWave", "PeriodicWave");
    }

    @Alerts("true/false")
    void _Permissions_Permissions() throws Exception {
        test("Permissions", "Permissions");
    }

    @Alerts("true/false")
    void _PermissionStatus_PermissionStatus() throws Exception {
        test("PermissionStatus", "PermissionStatus");
    }

    @Alerts("true/false")
    void _Plugin_Plugin() throws Exception {
        test("Plugin", "Plugin");
    }

    @Alerts("true/false")
    void _PluginArray_PluginArray() throws Exception {
        test("PluginArray", "PluginArray");
    }

    @Alerts("true/false")
    void _PointerEvent_PointerEvent() throws Exception {
        test("PointerEvent", "PointerEvent");
    }

    @Alerts("true/false")
    void _PopStateEvent_PopStateEvent() throws Exception {
        test("PopStateEvent", "PopStateEvent");
    }

    @Alerts("false/false")
    void _Position_Position() throws Exception {
        test("Position", "Position");
    }

    @Alerts("false/false")
    void _PositionError_PositionError() throws Exception {
        test("PositionError", "PositionError");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _Presentation_Presentation() throws Exception {
        test("Presentation", "Presentation");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _PresentationAvailability_PresentationAvailability() throws Exception {
        test("PresentationAvailability", "PresentationAvailability");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _PresentationConnection_PresentationConnection() throws Exception {
        test("PresentationConnection", "PresentationConnection");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _PresentationConnectionAvailableEvent_PresentationConnectionAvailableEvent() throws Exception {
        test("PresentationConnectionAvailableEvent", "PresentationConnectionAvailableEvent");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _PresentationConnectionCloseEvent_PresentationConnectionCloseEvent() throws Exception {
        test("PresentationConnectionCloseEvent", "PresentationConnectionCloseEvent");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _PresentationRequest_PresentationRequest() throws Exception {
        test("PresentationRequest", "PresentationRequest");
    }

    @Alerts("true/false")
    void _ProcessingInstruction_ProcessingInstruction() throws Exception {
        test("ProcessingInstruction", "ProcessingInstruction");
    }

    @Alerts("true/false")
    void _ProgressEvent_ProgressEvent() throws Exception {
        test("ProgressEvent", "ProgressEvent");
    }

    @Alerts("true/false")
    void _Promise_Promise() throws Exception {
        test("Promise", "Promise");
    }

    @Alerts("true/false")
    void _PromiseRejectionEvent_PromiseRejectionEvent() throws Exception {
        test("PromiseRejectionEvent", "PromiseRejectionEvent");
    }

    @Alerts("true/false")
    void _PushManager_PushManager() throws Exception {
        test("PushManager", "PushManager");
    }

    @Alerts("true/false")
    void _PushSubscription_PushSubscription() throws Exception {
        test("PushSubscription", "PushSubscription");
    }

    @Alerts("true/false")
    void _PushSubscriptionOptions_PushSubscriptionOptions() throws Exception {
        test("PushSubscriptionOptions", "PushSubscriptionOptions");
    }

    @Alerts("true/false")
    void _RadioNodeList_RadioNodeList() throws Exception {
        test("RadioNodeList", "RadioNodeList");
    }

    @Alerts("true/false")
    void _Range_Range() throws Exception {
        test("Range", "Range");
    }

    @Alerts("true/false")
    void _ReadableStream_ReadableStream() throws Exception {
        test("ReadableStream", "ReadableStream");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _RemotePlayback_RemotePlayback() throws Exception {
        test("RemotePlayback", "RemotePlayback");
    }

    @Alerts("true/false")
    void _Request_Request() throws Exception {
        test("Request", "Request");
    }

    @Alerts("true/false")
    void _Response_Response() throws Exception {
        test("Response", "Response");
    }

    @Alerts("true/false")
    void _RTCCertificate_RTCCertificate() throws Exception {
        test("RTCCertificate", "RTCCertificate");
    }

    @Alerts("true/false")
    void _RTCDataChannelEvent_RTCDataChannelEvent() throws Exception {
        test("RTCDataChannelEvent", "RTCDataChannelEvent");
    }

    @Alerts("false/false")
    void _RTCIceCandidate_mozRTCIceCandidate() throws Exception {
        test("RTCIceCandidate", "mozRTCIceCandidate");
    }

    @Alerts("true/false")
    void _RTCIceCandidate_RTCIceCandidate() throws Exception {
        test("RTCIceCandidate", "RTCIceCandidate");
    }

    @Alerts("false/false")
    void _RTCPeerConnection_mozRTCPeerConnection() throws Exception {
        test("RTCPeerConnection", "mozRTCPeerConnection");
    }

    @Alerts("true/false")
    void _RTCPeerConnection_RTCPeerConnection() throws Exception {
        test("RTCPeerConnection", "RTCPeerConnection");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _RTCPeerConnection_webkitRTCPeerConnection() throws Exception {
        test("RTCPeerConnection", "webkitRTCPeerConnection");
    }

    @Alerts("true/false")
    void _RTCPeerConnectionIceEvent_RTCPeerConnectionIceEvent() throws Exception {
        test("RTCPeerConnectionIceEvent", "RTCPeerConnectionIceEvent");
    }

    @Alerts("true/false")
    void _RTCSctpTransport_RTCSctpTransport() throws Exception {
        test("RTCSctpTransport", "RTCSctpTransport");
    }

    @Alerts("false/false")
    void _RTCSessionDescription_mozRTCSessionDescription() throws Exception {
        test("RTCSessionDescription", "mozRTCSessionDescription");
    }

    @Alerts("true/false")
    void _RTCSessionDescription_RTCSessionDescription() throws Exception {
        test("RTCSessionDescription", "RTCSessionDescription");
    }

    @Alerts("true/false")
    void _RTCStatsReport_RTCStatsReport() throws Exception {
        test("RTCStatsReport", "RTCStatsReport");
    }

}
