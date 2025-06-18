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
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests two Host classes, if one prototype is parent of another.
 *
 * This class handles all host names which starts by character 'P' to 'R'.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserParameterizedRunner.class)
public class HostParentOfPTest extends HostParentOf {

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
            return ch >= 'P' && ch <= 'R';
        });
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _PageTransitionEvent_PageTransitionEvent() throws Exception {
        test("PageTransitionEvent", "PageTransitionEvent");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _PannerNode_PannerNode() throws Exception {
        test("PannerNode", "PannerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    public void _PasswordCredential_PasswordCredential() throws Exception {
        test("PasswordCredential", "PasswordCredential");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _Path2D_Path2D() throws Exception {
        test("Path2D", "Path2D");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    public void _PaymentAddress_PaymentAddress() throws Exception {
        test("PaymentAddress", "PaymentAddress");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    public void _PaymentRequest_PaymentRequest() throws Exception {
        test("PaymentRequest", "PaymentRequest");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    public void _PaymentResponse_PaymentResponse() throws Exception {
        test("PaymentResponse", "PaymentResponse");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _Performance_Performance() throws Exception {
        test("Performance", "Performance");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _PerformanceEntry_PerformanceEntry() throws Exception {
        test("PerformanceEntry", "PerformanceEntry");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _PerformanceEntry_PerformanceMark() throws Exception {
        test("PerformanceEntry", "PerformanceMark");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _PerformanceEntry_PerformanceMeasure() throws Exception {
        test("PerformanceEntry", "PerformanceMeasure");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _PerformanceEntry_PerformanceNavigationTiming() throws Exception {
        test("PerformanceEntry", "PerformanceNavigationTiming");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _PerformanceEntry_PerformanceResourceTiming() throws Exception {
        test("PerformanceEntry", "PerformanceResourceTiming");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _PerformanceMark_PerformanceMark() throws Exception {
        test("PerformanceMark", "PerformanceMark");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _PerformanceMeasure_PerformanceMeasure() throws Exception {
        test("PerformanceMeasure", "PerformanceMeasure");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _PerformanceNavigation_PerformanceNavigation() throws Exception {
        test("PerformanceNavigation", "PerformanceNavigation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _PerformanceNavigationTiming_PerformanceNavigationTiming() throws Exception {
        test("PerformanceNavigationTiming", "PerformanceNavigationTiming");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _PerformanceObserver_PerformanceObserver() throws Exception {
        test("PerformanceObserver", "PerformanceObserver");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _PerformanceObserverEntryList_PerformanceObserverEntryList() throws Exception {
        test("PerformanceObserverEntryList", "PerformanceObserverEntryList");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _PerformanceResourceTiming_PerformanceNavigationTiming() throws Exception {
        test("PerformanceResourceTiming", "PerformanceNavigationTiming");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _PerformanceResourceTiming_PerformanceResourceTiming() throws Exception {
        test("PerformanceResourceTiming", "PerformanceResourceTiming");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _PerformanceTiming_PerformanceTiming() throws Exception {
        test("PerformanceTiming", "PerformanceTiming");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    public void _PeriodicSyncManager_PeriodicSyncManager() throws Exception {
        test("PeriodicSyncManager", "PeriodicSyncManager");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _PeriodicWave_PeriodicWave() throws Exception {
        test("PeriodicWave", "PeriodicWave");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _Permissions_Permissions() throws Exception {
        test("Permissions", "Permissions");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _PermissionStatus_PermissionStatus() throws Exception {
        test("PermissionStatus", "PermissionStatus");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _Plugin_Plugin() throws Exception {
        test("Plugin", "Plugin");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _PluginArray_PluginArray() throws Exception {
        test("PluginArray", "PluginArray");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _PointerEvent_PointerEvent() throws Exception {
        test("PointerEvent", "PointerEvent");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _PopStateEvent_PopStateEvent() throws Exception {
        test("PopStateEvent", "PopStateEvent");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("false/false")
    public void _Position_Position() throws Exception {
        test("Position", "Position");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("false/false")
    public void _PositionError_PositionError() throws Exception {
        test("PositionError", "PositionError");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    public void _Presentation_Presentation() throws Exception {
        test("Presentation", "Presentation");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    public void _PresentationAvailability_PresentationAvailability() throws Exception {
        test("PresentationAvailability", "PresentationAvailability");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    public void _PresentationConnection_PresentationConnection() throws Exception {
        test("PresentationConnection", "PresentationConnection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    public void _PresentationConnectionAvailableEvent_PresentationConnectionAvailableEvent() throws Exception {
        test("PresentationConnectionAvailableEvent", "PresentationConnectionAvailableEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    public void _PresentationConnectionCloseEvent_PresentationConnectionCloseEvent() throws Exception {
        test("PresentationConnectionCloseEvent", "PresentationConnectionCloseEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    public void _PresentationRequest_PresentationRequest() throws Exception {
        test("PresentationRequest", "PresentationRequest");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _ProcessingInstruction_ProcessingInstruction() throws Exception {
        test("ProcessingInstruction", "ProcessingInstruction");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _ProgressEvent_ProgressEvent() throws Exception {
        test("ProgressEvent", "ProgressEvent");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _Promise_Promise() throws Exception {
        test("Promise", "Promise");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _PromiseRejectionEvent_PromiseRejectionEvent() throws Exception {
        test("PromiseRejectionEvent", "PromiseRejectionEvent");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _PushManager_PushManager() throws Exception {
        test("PushManager", "PushManager");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _PushSubscription_PushSubscription() throws Exception {
        test("PushSubscription", "PushSubscription");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _PushSubscriptionOptions_PushSubscriptionOptions() throws Exception {
        test("PushSubscriptionOptions", "PushSubscriptionOptions");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _RadioNodeList_RadioNodeList() throws Exception {
        test("RadioNodeList", "RadioNodeList");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _Range_Range() throws Exception {
        test("Range", "Range");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _ReadableStream_ReadableStream() throws Exception {
        test("ReadableStream", "ReadableStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    public void _RemotePlayback_RemotePlayback() throws Exception {
        test("RemotePlayback", "RemotePlayback");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _Request_Request() throws Exception {
        test("Request", "Request");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _Response_Response() throws Exception {
        test("Response", "Response");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _RTCCertificate_RTCCertificate() throws Exception {
        test("RTCCertificate", "RTCCertificate");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _RTCDataChannelEvent_RTCDataChannelEvent() throws Exception {
        test("RTCDataChannelEvent", "RTCDataChannelEvent");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("false/false")
    public void _RTCIceCandidate_mozRTCIceCandidate() throws Exception {
        test("RTCIceCandidate", "mozRTCIceCandidate");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _RTCIceCandidate_RTCIceCandidate() throws Exception {
        test("RTCIceCandidate", "RTCIceCandidate");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false/false")
    public void _RTCPeerConnection_mozRTCPeerConnection() throws Exception {
        test("RTCPeerConnection", "mozRTCPeerConnection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _RTCPeerConnection_RTCPeerConnection() throws Exception {
        test("RTCPeerConnection", "RTCPeerConnection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    public void _RTCPeerConnection_webkitRTCPeerConnection() throws Exception {
        test("RTCPeerConnection", "webkitRTCPeerConnection");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _RTCPeerConnectionIceEvent_RTCPeerConnectionIceEvent() throws Exception {
        test("RTCPeerConnectionIceEvent", "RTCPeerConnectionIceEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _RTCSctpTransport_RTCSctpTransport() throws Exception {
        test("RTCSctpTransport", "RTCSctpTransport");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false/false")
    public void _RTCSessionDescription_mozRTCSessionDescription() throws Exception {
        test("RTCSessionDescription", "mozRTCSessionDescription");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _RTCSessionDescription_RTCSessionDescription() throws Exception {
        test("RTCSessionDescription", "RTCSessionDescription");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _RTCStatsReport_RTCStatsReport() throws Exception {
        test("RTCStatsReport", "RTCStatsReport");
    }

}
