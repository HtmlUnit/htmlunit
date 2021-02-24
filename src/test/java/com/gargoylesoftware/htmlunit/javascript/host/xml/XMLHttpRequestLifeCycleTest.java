/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.MiniServer;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for the LifeCycle events for XMLHttpRequests.
 * readystatechange
 * loadstart - whenever the loading is started. will always have a payload of 0.
 * loadend - whenever the loading is finished. Will be triggered after error as well.
 * progress - periodic updates that the transfer is still in progress. will only be triggered in async requests.
 * abort - aborts the scheduled request. will only be triggerd in async requests.
 * error - on network errors. server status will be ignored for this.
 * timeout - when the request is terminated because of the timeout
 * (only available in async requests, otherwise xhr.send will fail)
 *
 * The tests are split between sync (full-event cycle test) and async (each event is tested on it's own).
 * This is mainly done because we cannot reliably handle the amount &amp; speed of the alerts if everything is
 * executed together (Chrome did work in tests, FF &amp; IE did not).
 *
 * @author Thorsten Wendelmuth
 * @author Ronald Brill
 *
 */
@RunWith(Enclosed.class)
public final class XMLHttpRequestLifeCycleTest {
    private static final String SUCCESS_URL = "/xmlhttprequest/success.html";
    private static final String SUCCESS_WITHOUT_ORIGIN_URL = "/xmlhttprequest/success_without_origin.html";
    private static final String ERROR_403_URL = "/xmlhttprequest/error_403.html";
    private static final String ERROR_500_URL = "/xmlhttprequest/error_500.html";
    private static final String PREFLIGHT_ERROR_403_URL = "/xmlhttprequest/preflighterror_403.html";
    private static final String PREFLIGHT_ERROR_500_URL = "/xmlhttprequest/preflighterror_500.html";
    private static final String TIMEOUT_URL = "/xmlhttprequest/timeout.html";

    private static final String RETURN_XML = "<xml>\n"
            + "<content>htmlunit</content>\n"
            + "<content>xmlhttpRequest</content>\n"
            + "</xml>";

    private enum State {
        LOAD_START("loadstart"), LOAD("load"), LOAD_END("loadend"), PROGRESS("progress"), ERROR("error"),
        ABORT("abort"), READY_STATE_CHANGE("readystatechange"), TIMEOUT("timeout");

        private final String eventName_;

        State(final String eventName) {
            eventName_ = eventName;
        }

        public String getEventName_() {
            return eventName_;
        }
    }

    private enum Mode {
        ASYNC(true, false), SYNC(false, false), ASYNC_ON_KEYWORD(true, true), SYNC_ON_KEYWORD(false, true);

        private final boolean async_;
        private final boolean useOnKeyword_;

        Mode(final boolean async, final boolean useOnKeyword) {
            async_ = async;
            useOnKeyword_ = useOnKeyword;
        }

        public boolean isAsync() {
            return async_;
        }

        public boolean isUseOnKeyword() {
            return useOnKeyword_;
        }
    }

    private enum Execution {
        ONLY_SEND, SEND_ABORT, NETWORK_ERROR, ERROR_403, ERROR_500, TIMEOUT,
        ONLY_SEND_PREFLIGHT, ONLY_SEND_PREFLIGHT_FORBIDDEN,
        WITHOUT_ORIGIN, WITHOUT_ORIGIN_PREFLIGHT,
        NETWORK_ERROR_PREFLIGHT,
        ERROR_403_PREFLIGHT, ERROR_403_DURING_PREFLIGHT,
        ERROR_500_PREFLIGHT, ERROR_500_DURING_PREFLIGHT
    }

    /**
     * Test using our JettyServer.
     */
    @RunWith(BrowserRunner.class)
    public static class JettyServerTest extends WebDriverTestCase {

        public static class Xml200Servlet extends HttpServlet {

            @Override
            protected void doOptions(final HttpServletRequest request, final HttpServletResponse response) {
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Methods", "*");
                response.setHeader("Access-Control-Allow-Headers", "X-PINGOTHER");
            }

            @Override
            protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
                    throws ServletException, IOException {
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Methods", "*");
                response.setHeader("Access-Control-Allow-Headers", "X-PINGOTHER");

                response.setContentType(MimeType.TEXT_XML);
                response.setContentLength(RETURN_XML.length());
                response.setStatus(HttpStatus.SC_OK);
                final ServletOutputStream outputStream = response.getOutputStream();
                try (Writer writer = new OutputStreamWriter(outputStream)) {
                    writer.write(RETURN_XML);
                }
            }
        }

        public static class Xml200ServletWithoutOriginHeader extends HttpServlet {

            @Override
            protected void doOptions(final HttpServletRequest request, final HttpServletResponse response) {
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Methods", "*");
                response.setHeader("Access-Control-Allow-Headers", "X-PINGOTHER");
            }

            @Override
            protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
                    throws ServletException, IOException {
                response.setContentType(MimeType.TEXT_XML);
                response.setContentLength(RETURN_XML.length());
                response.setStatus(HttpStatus.SC_OK);
                final ServletOutputStream outputStream = response.getOutputStream();
                try (Writer writer = new OutputStreamWriter(outputStream)) {
                    writer.write(RETURN_XML);
                }
            }
        }

        public static class Xml403Servlet extends HttpServlet {

            @Override
            protected void doOptions(final HttpServletRequest request, final HttpServletResponse response) {
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Methods", "*");
                response.setHeader("Access-Control-Allow-Headers", "X-PINGOTHER");
            }

            @Override
            protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
                    throws ServletException, IOException {
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Methods", "*");
                response.setHeader("Access-Control-Allow-Headers", "X-PINGOTHER");

                response.setContentType(MimeType.TEXT_XML);
                response.setContentLength(RETURN_XML.length());
                response.setStatus(HttpStatus.SC_FORBIDDEN);
                final ServletOutputStream outputStream = response.getOutputStream();
                try (Writer writer = new OutputStreamWriter(outputStream)) {
                    writer.write(RETURN_XML);
                }
            }
        }

        public static class Xml500Servlet extends HttpServlet {

            @Override
            protected void doOptions(final HttpServletRequest request, final HttpServletResponse response) {
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Methods", "*");
                response.setHeader("Access-Control-Allow-Headers", "X-PINGOTHER");
            }

            @Override
            protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
                    throws ServletException, IOException {
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Methods", "*");
                response.setHeader("Access-Control-Allow-Headers", "X-PINGOTHER");

                response.setContentType(MimeType.TEXT_XML);
                response.setContentLength(RETURN_XML.length());
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
                final ServletOutputStream outputStream = response.getOutputStream();
                try (Writer writer = new OutputStreamWriter(outputStream)) {
                    writer.write(RETURN_XML);
                }
            }
        }

        public static class Preflight403Servlet extends HttpServlet {

            @Override
            protected void doOptions(final HttpServletRequest request, final HttpServletResponse response) {
                response.setStatus(HttpStatus.SC_FORBIDDEN);
            }
        }

        public static class Preflight500Servlet extends HttpServlet {

            @Override
            protected void doOptions(final HttpServletRequest request, final HttpServletResponse response) {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
        }

        public static class XmlTimeoutServlet extends HttpServlet {

            @Override
            protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
                    throws ServletException, IOException {
                resp.setContentType(MimeType.TEXT_XML);
                resp.setContentLength(RETURN_XML.length());
                resp.setStatus(HttpStatus.SC_OK);
                final ServletOutputStream outputStream = resp.getOutputStream();
                try (Writer writer = new OutputStreamWriter(outputStream)) {
                    writer.flush();
                    Thread.sleep(500);
                    writer.write(RETURN_XML);
                }
                catch (final Exception e) {
                    // ignored.
                }
            }
        }

        private final Map<String, Class<? extends Servlet>> servlets_ = new HashMap<>();

        @Before
        public void prepareTestingServlets() {
            servlets_.put(SUCCESS_URL, Xml200Servlet.class);
            servlets_.put(SUCCESS_WITHOUT_ORIGIN_URL, Xml200ServletWithoutOriginHeader.class);
            servlets_.put(ERROR_403_URL, Xml403Servlet.class);
            servlets_.put(ERROR_500_URL, Xml500Servlet.class);
            servlets_.put(PREFLIGHT_ERROR_403_URL, Preflight403Servlet.class);
            servlets_.put(PREFLIGHT_ERROR_500_URL, Preflight500Servlet.class);
            servlets_.put(TIMEOUT_URL, XmlTimeoutServlet.class);
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts({"readystatechange_1_0_true", "open-done",
                "readystatechange_4_200_true", "load_4_200_false",
                "loadend_4_200_false", "send-done"})
        public void addEventListener_sync() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.SYNC, Execution.ONLY_SEND),
                    URL_FIRST, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts({"readystatechange_1_0_true", "open-done",
                        "readystatechange_4_200_true", "load_4_200_false",
                        "loadend_4_200_false", "send-done"})
        public void addEventListener_sync_preflight() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.SYNC, Execution.ONLY_SEND_PREFLIGHT),
                    URL_FIRST, servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done",
                        "ExceptionThrown"},
                FF = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_4_0_true", "error_4_0_false",
                        "loadend_4_0_false", "ExceptionThrown"},
                FF78 = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_4_0_true", "error_4_0_false",
                        "loadend_4_0_false", "ExceptionThrown"})
        public void addEventListener_sync_preflight_forbidden() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.SYNC, Execution.ONLY_SEND_PREFLIGHT_FORBIDDEN),
                    URL_FIRST, servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "ExceptionThrown"},
                FF = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_4_0_true", "error_4_0_false", "loadend_4_0_false",
                        "ExceptionThrown"},
                FF78 = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_4_0_true", "error_4_0_false", "loadend_4_0_false",
                        "ExceptionThrown"})
        public void addEventListener_sync_without_origin() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.SYNC, Execution.WITHOUT_ORIGIN),
                    URL_FIRST, servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "ExceptionThrown"},
                FF = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_4_0_true", "error_4_0_false", "loadend_4_0_false",
                        "ExceptionThrown"},
                FF78 = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_4_0_true", "error_4_0_false", "loadend_4_0_false",
                        "ExceptionThrown"})
        public void addEventListener_sync_preflight_without_origin() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.SYNC, Execution.WITHOUT_ORIGIN_PREFLIGHT),
                    URL_FIRST, servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "ExceptionThrown"},
                FF = {"readystatechange_1_0_true", "open-done", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false", "ExceptionThrown"},
                FF78 = {"readystatechange_1_0_true", "open-done", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false", "ExceptionThrown"})
        public void addEventListener_sync_networkError() throws Exception {
            try {
                loadPage2(buildHtml(Mode.SYNC, Execution.NETWORK_ERROR), URL_FIRST, servlets_);
            }
            catch (final WebDriverException e) {
                if (useRealBrowser()) {
                    // we only expect the error to be thrown in htmlunit scenarios.
                    throw e;
                }
            }
            finally {
                verifyAlerts(() -> extractLog(getWebDriver()), String.join("\n", getExpectedAlerts()));
            }
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "ExceptionThrown"},
                FF = {"readystatechange_1_0_true", "open-done", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false", "ExceptionThrown"},
                FF78 = {"readystatechange_1_0_true", "open-done", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false", "ExceptionThrown"})
        public void addEventListener_sync_networkError_preflight() throws Exception {
            try {
                loadPage2(buildHtml(Mode.SYNC, Execution.NETWORK_ERROR_PREFLIGHT), URL_FIRST, servlets_, servlets_);
            }
            catch (final WebDriverException e) {
                if (useRealBrowser()) {
                    // we only expect the error to be thrown in htmlunit scenarios.
                    throw e;
                }
            }
            finally {
                verifyAlerts(() -> extractLog(getWebDriver()), String.join("\n", getExpectedAlerts()));
            }
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts({"readystatechange_1_0_true", "open-done", "readystatechange_4_403_true",
                    "load_4_403_false", "loadend_4_403_false", "send-done"})
        public void addEventListener_sync_Error403() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.SYNC, Execution.ERROR_403), URL_FIRST,
                    servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()), DEFAULT_WAIT_TIME * 10);
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts({"readystatechange_1_0_true", "open-done", "readystatechange_4_403_true",
                    "load_4_403_false", "loadend_4_403_false", "send-done"})
        public void addEventListener_sync_Error403_preflight() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.SYNC, Execution.ERROR_403_PREFLIGHT), URL_FIRST,
                    servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "ExceptionThrown"},
                FF = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_4_0_true", "error_4_0_false", "loadend_4_0_false",
                        "ExceptionThrown"},
                FF78 = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_4_0_true", "error_4_0_false", "loadend_4_0_false",
                        "ExceptionThrown"})
        public void addEventListener_sync_Error403_during_preflight() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.SYNC, Execution.ERROR_403_DURING_PREFLIGHT), URL_FIRST,
                    servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts({"readystatechange_1_0_true", "open-done", "readystatechange_4_500_true",
                    "load_4_500_false", "loadend_4_500_false", "send-done"})
        public void addEventListener_sync_Error500() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.SYNC, Execution.ERROR_500), URL_FIRST,
                    servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()), DEFAULT_WAIT_TIME * 10);
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts({"readystatechange_1_0_true", "open-done", "readystatechange_4_500_true",
                    "load_4_500_false", "loadend_4_500_false", "send-done"})
        public void addEventListener_sync_Error500_preflight() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.SYNC, Execution.ERROR_500_PREFLIGHT), URL_FIRST,
                    servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "ExceptionThrown"},
                FF = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_4_0_true", "error_4_0_false", "loadend_4_0_false",
                        "ExceptionThrown"},
                FF78 = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_4_0_true", "error_4_0_false", "loadend_4_0_false",
                        "ExceptionThrown"})
        public void addEventListener_sync_Error500_during_preflight() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.SYNC, Execution.ERROR_500_DURING_PREFLIGHT), URL_FIRST,
                    servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts({"readystatechange_1_0_true", "open-done", "ExceptionThrown"})
        public void addEventListener_sync_timeout() throws Exception {
            // that's invalid. You cannot set timeout for synced requests. Will throw an
            // exception only triggers readystatechange
            try {
                loadPage2(buildHtml(Mode.SYNC, Execution.TIMEOUT), URL_FIRST, servlets_);
            }
            catch (final WebDriverException e) {
                if (useRealBrowser()) {
                    // we only expect the error to be thrown in htmlunit scenarios.
                    throw e;
                }
            }
            finally {
                verifyAlerts(() -> extractLog(getWebDriver()), String.join("\n", getExpectedAlerts()));
            }
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_2_200_true", "readystatechange_3_200_true",
                        "progress_3_200_false", "readystatechange_4_200_true", "load_4_200_false",
                        "loadend_4_200_false"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "loadstart_1_0_false",
                        "readystatechange_2_200_true", "readystatechange_3_200_true",
                        "progress_3_200_false", "readystatechange_4_200_true", "load_4_200_false",
                        "loadend_4_200_false"})
        public void addEventListener_async() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC, Execution.ONLY_SEND),
                    URL_FIRST, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_2_200_true", "readystatechange_3_200_true",
                        "progress_3_200_false", "readystatechange_4_200_true",
                        "load_4_200_false", "loadend_4_200_false"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "loadstart_1_0_false", "readystatechange_2_200_true",
                        "readystatechange_3_200_true", "progress_3_200_false", "readystatechange_4_200_true",
                        "load_4_200_false", "loadend_4_200_false"})
        public void addEventListener_async_preflight() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC, Execution.ONLY_SEND_PREFLIGHT),
                    URL_FIRST, servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_4_0_true", "error_4_0_false",
                        "loadend_4_0_false"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "loadstart_1_0_false",
                        "readystatechange_4_0_true", "error_4_0_false", "loadend_4_0_false"})
        public void addEventListener_async_preflight_forbidden() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC, Execution.ONLY_SEND_PREFLIGHT_FORBIDDEN),
                    URL_FIRST, servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done",
                        "loadstart_1_0_false", "send-done", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "loadstart_1_0_false", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false"})
        public void addEventListener_async_without_origin() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC, Execution.WITHOUT_ORIGIN),
                    URL_FIRST, servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done",
                        "loadstart_1_0_false", "send-done", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "loadstart_1_0_false", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false"})
        public void addEventListener_async_preflight_without_origin() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC, Execution.WITHOUT_ORIGIN_PREFLIGHT),
                    URL_FIRST, servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_4_0_true", "abort_4_0", "loadend_4_0_false",
                        "abort-done"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "readystatechange_4_0_true", "abort_4_0",
                        "loadend_4_0_false", "abort-done"})
        public void addEventListener_async_abortTriggered() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC, Execution.SEND_ABORT), URL_FIRST,
                    servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                "send-done", "readystatechange_4_0_true", "error_4_0_false",
                "loadend_4_0_false"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "loadstart_1_0_false", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false"})
        public void addEventListener_async_networkErrorTriggered() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC, Execution.NETWORK_ERROR), URL_FIRST,
                    servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                "send-done", "readystatechange_4_0_true", "error_4_0_false",
                "loadend_4_0_false"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "loadstart_1_0_false", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false"})
        public void addEventListener_async_networkErrorTriggered_preflight() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC, Execution.NETWORK_ERROR_PREFLIGHT), URL_FIRST,
                    servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()), DEFAULT_WAIT_TIME * 10);
        }

        /**
         * Error 500 on the server side still count as a valid requests for {@link XMLHttpRequest}.
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_2_500_true", "readystatechange_3_500_true",
                        "progress_3_500_false", "readystatechange_4_500_true",
                        "load_4_500_false", "loadend_4_500_false"},
                IE = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_1_0_true", "send-done", "loadstart_1_0_false",
                        "readystatechange_2_500_true", "readystatechange_3_500_true",
                        "progress_3_500_false", "readystatechange_4_500_true",
                        "load_4_500_false", "loadend_4_500_false"})
        public void addEventListener_async_Error500Triggered() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC, Execution.ERROR_500), URL_FIRST,
                    servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * Error 500 on the server side still count as a valid requests for {@link XMLHttpRequest}.
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_2_500_true", "readystatechange_3_500_true",
                        "progress_3_500_false", "readystatechange_4_500_true",
                        "load_4_500_false", "loadend_4_500_false"},
                IE = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_1_0_true", "send-done", "loadstart_1_0_false",
                        "readystatechange_2_500_true", "readystatechange_3_500_true",
                        "progress_3_500_false", "readystatechange_4_500_true",
                        "load_4_500_false", "loadend_4_500_false"})
        public void addEventListener_async_Error500Triggered_preflight() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC, Execution.ERROR_500_PREFLIGHT), URL_FIRST,
                    servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * Error 500 on the server side still count as a valid requests for {@link XMLHttpRequest}.
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "loadstart_1_0_false", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false"})
        public void addEventListener_async_Error500Triggered_during_preflight() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC, Execution.ERROR_500_DURING_PREFLIGHT), URL_FIRST,
                    servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_4_0_true", "timeout_4_0_false",
                        "loadend_4_0_false"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "loadstart_1_0_false", "readystatechange_2_200_true",
                        "readystatechange_4_0_true", "timeout_4_0_false",
                        "loadend_4_0_false"})
        public void addEventListener_async_timeout() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC, Execution.TIMEOUT), URL_FIRST,
                    servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        // same tests as above, but this time we're triggering with the onkeyword.
        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts({"readystatechange_1_0_true", "open-done", "readystatechange_4_200_true",
                    "load_4_200_false", "loadend_4_200_false", "send-done"})
        public void onKeyWord_sync() throws Exception {
            // we can register ourselves for every state here since it's in sync mode and
            // most of them won't fire anyway.
            final WebDriver driver = loadPage2(buildHtml(Mode.SYNC_ON_KEYWORD, Execution.ONLY_SEND),
                    URL_FIRST, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts({"readystatechange_1_0_true", "open-done",
                        "readystatechange_4_200_true", "load_4_200_false",
                        "loadend_4_200_false", "send-done"})
        public void onKeyWord_sync_preflight() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.SYNC_ON_KEYWORD, Execution.ONLY_SEND_PREFLIGHT),
                    URL_FIRST, servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done",
                        "ExceptionThrown"},
                FF = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_4_0_true", "error_4_0_false",
                        "loadend_4_0_false", "ExceptionThrown"},
                FF78 = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_4_0_true", "error_4_0_false",
                        "loadend_4_0_false", "ExceptionThrown"})
        public void onKeyWord_sync_preflight_forbidden() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.SYNC_ON_KEYWORD, Execution.ONLY_SEND_PREFLIGHT_FORBIDDEN),
                    URL_FIRST, servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "ExceptionThrown"},
                FF = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_4_0_true", "error_4_0_false", "loadend_4_0_false",
                        "ExceptionThrown"},
                FF78 = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_4_0_true", "error_4_0_false", "loadend_4_0_false",
                        "ExceptionThrown"})
        public void onKeyWord_sync_without_origin() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.SYNC_ON_KEYWORD, Execution.WITHOUT_ORIGIN),
                    URL_FIRST, servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "ExceptionThrown"},
                FF = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_4_0_true", "error_4_0_false", "loadend_4_0_false",
                        "ExceptionThrown"},
                FF78 = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_4_0_true", "error_4_0_false", "loadend_4_0_false",
                        "ExceptionThrown"})
        public void onKeyWord_sync_preflight_without_origin() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.SYNC_ON_KEYWORD, Execution.WITHOUT_ORIGIN_PREFLIGHT),
                    URL_FIRST, servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "ExceptionThrown"},
                FF = {"readystatechange_1_0_true", "open-done", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false", "ExceptionThrown"},
                FF78 = {"readystatechange_1_0_true", "open-done", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false", "ExceptionThrown"})
        public void onKeyWord_sync_networkError() throws Exception {
            // will throw an exception and user is supposed to handle this.
            // That's why we only have one readystatechange callback.
            try {
                loadPage2(buildHtml(Mode.SYNC_ON_KEYWORD, Execution.NETWORK_ERROR), URL_FIRST, servlets_);

            }
            catch (final WebDriverException e) {
                if (useRealBrowser()) {
                    // we only expect the error to be thrown in htmlunit scenarios.
                    throw e;
                }
            }
            finally {
                verifyAlerts(() -> extractLog(getWebDriver()), String.join("\n", getExpectedAlerts()));
            }
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts({"readystatechange_1_0_true", "open-done", "readystatechange_4_403_true",
                    "load_4_403_false", "loadend_4_403_false", "send-done"})
        public void onKeyWord_sync_Error403() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.SYNC_ON_KEYWORD, Execution.ERROR_403), URL_FIRST,
                    servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()), DEFAULT_WAIT_TIME * 10);
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts({"readystatechange_1_0_true", "open-done", "readystatechange_4_403_true",
                    "load_4_403_false", "loadend_4_403_false", "send-done"})
        public void onKeyWord_sync_Error403_preflight() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.SYNC_ON_KEYWORD,
                    Execution.ERROR_403_PREFLIGHT), URL_FIRST,
                    servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "ExceptionThrown"},
                FF = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_4_0_true", "error_4_0_false", "loadend_4_0_false",
                        "ExceptionThrown"},
                FF78 = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_4_0_true", "error_4_0_false", "loadend_4_0_false",
                        "ExceptionThrown"})
        public void onKeyWord_sync_Error403_during_preflight() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.SYNC_ON_KEYWORD,
                    Execution.ERROR_403_DURING_PREFLIGHT), URL_FIRST,
                    servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts({"readystatechange_1_0_true", "open-done", "readystatechange_4_500_true",
                    "load_4_500_false", "loadend_4_500_false", "send-done"})
        public void onKeyWord_sync_Error500() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.SYNC_ON_KEYWORD, Execution.ERROR_500), URL_FIRST,
                    servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()), DEFAULT_WAIT_TIME * 10);
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts({"readystatechange_1_0_true", "open-done", "readystatechange_4_500_true",
                    "load_4_500_false", "loadend_4_500_false", "send-done"})
        public void onKeyWord_sync_Error500_preflight() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.SYNC_ON_KEYWORD,
                    Execution.ERROR_500_PREFLIGHT), URL_FIRST,
                    servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "ExceptionThrown"},
                FF = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_4_0_true", "error_4_0_false", "loadend_4_0_false",
                        "ExceptionThrown"},
                FF78 = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_4_0_true", "error_4_0_false", "loadend_4_0_false",
                        "ExceptionThrown"})
        public void onKeyWord_sync_Error500_during_preflight() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.SYNC_ON_KEYWORD,
                    Execution.ERROR_500_DURING_PREFLIGHT), URL_FIRST,
                    servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts({"readystatechange_1_0_true", "open-done", "ExceptionThrown"})
        public void onKeyWord_sync_timeout() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.SYNC_ON_KEYWORD, Execution.TIMEOUT),
                    URL_FIRST, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_2_200_true", "readystatechange_3_200_true",
                        "progress_3_200_false", "readystatechange_4_200_true", "load_4_200_false",
                        "loadend_4_200_false"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "loadstart_1_0_false",
                        "readystatechange_2_200_true", "readystatechange_3_200_true",
                        "progress_3_200_false", "readystatechange_4_200_true", "load_4_200_false",
                        "loadend_4_200_false"})
        public void onKeyWord_async() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC_ON_KEYWORD, Execution.ONLY_SEND),
                    URL_FIRST, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_2_200_true", "readystatechange_3_200_true",
                        "progress_3_200_false", "readystatechange_4_200_true",
                        "load_4_200_false", "loadend_4_200_false"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "loadstart_1_0_false", "readystatechange_2_200_true",
                        "readystatechange_3_200_true", "progress_3_200_false", "readystatechange_4_200_true",
                        "load_4_200_false", "loadend_4_200_false"})
        public void onKeyWord_async_preflight() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC_ON_KEYWORD, Execution.ONLY_SEND_PREFLIGHT),
                    URL_FIRST, servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_4_0_true", "abort_4_0",
                        "loadend_4_0_false", "abort-done"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "readystatechange_4_0_true", "abort_4_0",
                        "loadend_4_0_false", "abort-done"})
        public void onKeyWord_async_abortTriggered() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC_ON_KEYWORD, Execution.SEND_ABORT),
                    URL_FIRST, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                    "send-done", "readystatechange_4_0_true", "error_4_0_false",
                    "loadend_4_0_false"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "loadstart_1_0_false", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false"})
        public void onKeyWord_async_networkErrorTriggered() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC_ON_KEYWORD, Execution.NETWORK_ERROR),
                    URL_FIRST, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }


        /**
         * Error 403 on the server side still count as a valid requests for {@link XMLHttpRequest}.
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_2_403_true", "readystatechange_3_403_true",
                        "progress_3_403_false", "readystatechange_4_403_true",
                        "load_4_403_false", "loadend_4_403_false"},
                IE = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_1_0_true", "send-done", "loadstart_1_0_false",
                        "readystatechange_2_403_true", "readystatechange_3_403_true",
                        "progress_3_403_false", "readystatechange_4_403_true",
                        "load_4_403_false", "loadend_4_403_false"})
        public void onKeyWord_async_Error403Triggered() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC_ON_KEYWORD, Execution.ERROR_403), URL_FIRST,
                    servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * Error 403 on the server side still count as a valid requests for {@link XMLHttpRequest}.
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_2_403_true", "readystatechange_3_403_true",
                        "progress_3_403_false", "readystatechange_4_403_true",
                        "load_4_403_false", "loadend_4_403_false"},
                IE = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_1_0_true", "send-done", "loadstart_1_0_false",
                        "readystatechange_2_403_true", "readystatechange_3_403_true",
                        "progress_3_403_false", "readystatechange_4_403_true",
                        "load_4_403_false", "loadend_4_403_false"})
        public void onKeyWord_async_Error403Triggered_preflight() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC_ON_KEYWORD,
                    Execution.ERROR_403_PREFLIGHT), URL_FIRST,
                    servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * Error 403 on the server side still count as a valid requests for {@link XMLHttpRequest}.
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "loadstart_1_0_false", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false"})
        public void onKeyWord_async_Error403Triggered_during_preflight() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC_ON_KEYWORD,
                    Execution.ERROR_403_DURING_PREFLIGHT), URL_FIRST,
                    servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * Error 500 on the server side still count as a valid requests for {@link XMLHttpRequest}.
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_2_500_true", "readystatechange_3_500_true",
                        "progress_3_500_false", "readystatechange_4_500_true", "load_4_500_false",
                        "loadend_4_500_false"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "loadstart_1_0_false",
                        "readystatechange_2_500_true", "readystatechange_3_500_true",
                        "progress_3_500_false", "readystatechange_4_500_true", "load_4_500_false",
                        "loadend_4_500_false"})
        public void onKeyWord_async_Error500Triggered() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC_ON_KEYWORD, Execution.ERROR_500),
                    URL_FIRST, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * Error 403 on the server side still count as a valid requests for {@link XMLHttpRequest}.
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_2_403_true", "readystatechange_3_403_true",
                        "progress_3_403_false", "readystatechange_4_403_true",
                        "load_4_403_false", "loadend_4_403_false"},
                IE = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_1_0_true", "send-done", "loadstart_1_0_false",
                        "readystatechange_2_403_true", "readystatechange_3_403_true",
                        "progress_3_403_false", "readystatechange_4_403_true",
                        "load_4_403_false", "loadend_4_403_false"})
        public void addEventListener_async_Error403Triggered() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC, Execution.ERROR_403), URL_FIRST,
                    servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * Error 403 on the server side still count as a valid requests for {@link XMLHttpRequest}.
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_2_403_true", "readystatechange_3_403_true",
                        "progress_3_403_false", "readystatechange_4_403_true",
                        "load_4_403_false", "loadend_4_403_false"},
                IE = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_1_0_true", "send-done", "loadstart_1_0_false",
                        "readystatechange_2_403_true", "readystatechange_3_403_true",
                        "progress_3_403_false", "readystatechange_4_403_true",
                        "load_4_403_false", "loadend_4_403_false"})
        public void addEventListener_async_Error403Triggered_preflight() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC, Execution.ERROR_403_PREFLIGHT), URL_FIRST,
                    servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * Error 403 on the server side still count as a valid requests for {@link XMLHttpRequest}.
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "loadstart_1_0_false", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false"})
        public void addEventListener_async_Error403Triggered_during_preflight() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC, Execution.ERROR_403_DURING_PREFLIGHT), URL_FIRST,
                    servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * Error 500 on the server side still count as a valid requests for {@link XMLHttpRequest}.
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_2_500_true", "readystatechange_3_500_true",
                        "progress_3_500_false", "readystatechange_4_500_true",
                        "load_4_500_false", "loadend_4_500_false"},
                IE = {"readystatechange_1_0_true", "open-done",
                        "readystatechange_1_0_true", "send-done", "loadstart_1_0_false",
                        "readystatechange_2_500_true", "readystatechange_3_500_true",
                        "progress_3_500_false", "readystatechange_4_500_true",
                        "load_4_500_false", "loadend_4_500_false"})
        public void onKeyWord_async_Error500Triggered_preflight() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC_ON_KEYWORD,
                    Execution.ERROR_500_PREFLIGHT), URL_FIRST,
                    servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * Error 500 on the server side still count as a valid requests for {@link XMLHttpRequest}.
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "loadstart_1_0_false", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false"})
        public void onKeyWord_async_Error500Triggered_during_preflight() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC_ON_KEYWORD,
                    Execution.ERROR_500_DURING_PREFLIGHT), URL_FIRST,
                    servlets_, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }

        /**
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_4_0_true", "timeout_4_0_false",
                        "loadend_4_0_false"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "loadstart_1_0_false", "readystatechange_2_200_true",
                        "readystatechange_4_0_true", "timeout_4_0_false",
                        "loadend_4_0_false"})
        public void onKeyWord_async_timeout() throws Exception {
            final WebDriver driver = loadPage2(buildHtml(Mode.ASYNC_ON_KEYWORD, Execution.TIMEOUT),
                    URL_FIRST, servlets_);
            verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));
        }
    }

    /**
     * Test using our MiniServer to be able to simulate special error conditions.
     */
    @RunWith(BrowserRunner.class)
    public static class MiniServerTest extends WebDriverTestCase {

        @Before
        public void before() throws Exception {
            // Chrome seems to cache preflight results
            shutDownAll();
            MiniServer.resetDropRequests();
        }

        @After
        public void after() throws Exception {
            MiniServer.resetDropRequests();
        }

        /**
         * NoHttpResponseException.
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "ExceptionThrown"},
                FF = {"readystatechange_1_0_true", "open-done", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false", "ExceptionThrown"},
                FF78 = {"readystatechange_1_0_true", "open-done", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false", "ExceptionThrown"})
        public void addEventListener_sync_NoHttpResponseException() throws Exception {
            final MockWebConnection mockWebConnection = getMockWebConnection();
            mockWebConnection.setResponse(WebTestCase.URL_FIRST, buildHtml(Mode.SYNC, Execution.ONLY_SEND));
            MiniServer.configureDropRequest(new URL(WebTestCase.URL_FIRST + SUCCESS_URL));

            final MiniServer miniServer = new MiniServer(PORT, mockWebConnection);
            miniServer.start();
            try {
                final WebDriver driver = getWebDriver();
                driver.get(WebTestCase.URL_FIRST.toExternalForm());

                verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));

                // no chance to to check the request count because of retries
                assertEquals(new URL(WebTestCase.URL_FIRST, SUCCESS_URL),
                        mockWebConnection.getLastWebRequest().getUrl());
                assertEquals(HttpMethod.GET,
                        mockWebConnection.getLastWebRequest().getHttpMethod());
            }
            finally {
                miniServer.shutDown();
            }
        }

        /**
         * NoHttpResponseException after preflight.
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "ExceptionThrown"},
                FF = {"readystatechange_1_0_true", "open-done", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false", "ExceptionThrown"},
                FF78 = {"readystatechange_1_0_true", "open-done", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false", "ExceptionThrown"})
        public void addEventListener_sync_preflight_NoHttpResponseException() throws Exception {
            final MockWebConnection mockWebConnection = getMockWebConnection();
            mockWebConnection.setResponse(WebTestCase.URL_FIRST, buildHtml(Mode.SYNC, Execution.ONLY_SEND_PREFLIGHT));

            final List<NameValuePair> headers = new ArrayList<>();
            headers.add(new NameValuePair("Access-Control-Allow-Origin", "*"));
            headers.add(new NameValuePair("Access-Control-Allow-Methods", "*"));
            headers.add(new NameValuePair("Access-Control-Allow-Headers", "X-PINGOTHER"));
            getMockWebConnection().setResponse(new URL("http://localhost:" + PORT2 + SUCCESS_URL),
                    "",  200, "OK", MimeType.TEXT_HTML, headers);
            MiniServer.configureDropGetRequest(new URL("http://localhost:" + PORT2 + SUCCESS_URL));

            final MiniServer miniServer1 = new MiniServer(PORT, mockWebConnection);
            miniServer1.start();
            try {
                final MiniServer miniServer2 = new MiniServer(PORT2, mockWebConnection);
                miniServer2.start();

                try {
                    final WebDriver driver = getWebDriver();
                    driver.get(WebTestCase.URL_FIRST.toExternalForm());

                    verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));

                    // no chance to to check the request count because of retries
                    assertEquals(new URL("http://localhost:" + PORT2 + SUCCESS_URL),
                            mockWebConnection.getLastWebRequest().getUrl());
                    assertEquals(HttpMethod.GET,
                            mockWebConnection.getLastWebRequest().getHttpMethod());
                }
                finally {
                    miniServer2.shutDown();
                }
            }
            finally {
                miniServer1.shutDown();
            }
        }

        /**
         * NoHttpResponseException during preflight.
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "ExceptionThrown"},
                FF = {"readystatechange_1_0_true", "open-done", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false", "ExceptionThrown"},
                FF78 = {"readystatechange_1_0_true", "open-done", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false", "ExceptionThrown"})
        public void addEventListener_sync_preflight_NoHttpResponseException_during_preflight() throws Exception {
            final MockWebConnection mockWebConnection = getMockWebConnection();
            mockWebConnection.setResponse(WebTestCase.URL_FIRST, buildHtml(Mode.SYNC, Execution.ONLY_SEND_PREFLIGHT));
            MiniServer.configureDropRequest(new URL("http://localhost:" + PORT2 + SUCCESS_URL));

            final MiniServer miniServer1 = new MiniServer(PORT, mockWebConnection);
            miniServer1.start();
            try {
                final MiniServer miniServer2 = new MiniServer(PORT2, mockWebConnection);
                miniServer2.start();

                try {
                    final WebDriver driver = getWebDriver();
                    driver.get(WebTestCase.URL_FIRST.toExternalForm());

                    verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));

                    // no chance to to check the request count because of retries
                    assertEquals(new URL("http://localhost:" + PORT2 + SUCCESS_URL),
                            mockWebConnection.getLastWebRequest().getUrl());
                    assertEquals(HttpMethod.OPTIONS,
                            mockWebConnection.getLastWebRequest().getHttpMethod());
                }
                finally {
                    miniServer2.shutDown();
                }
            }
            finally {
                miniServer1.shutDown();
            }
        }

        /**
         * NoHttpResponseException.
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_4_0_true", "error_4_0_false",
                        "loadend_4_0_false"},
                FF = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "progress_1_0_false", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false"},
                FF78 = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "progress_1_0_false", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "loadstart_1_0_false",
                        "readystatechange_4_0_true", "error_4_0_false",
                        "loadend_4_0_false"})
        public void addEventListener_async_NoHttpResponseException() throws Exception {
            final MockWebConnection mockWebConnection = getMockWebConnection();
            mockWebConnection.setResponse(WebTestCase.URL_FIRST, buildHtml(Mode.ASYNC, Execution.ONLY_SEND));
            MiniServer.configureDropRequest(new URL(WebTestCase.URL_FIRST + SUCCESS_URL));

            final MiniServer miniServer = new MiniServer(PORT, mockWebConnection);
            miniServer.start();
            try {
                final WebDriver driver = getWebDriver();
                driver.get(WebTestCase.URL_FIRST.toExternalForm());

                verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));

                // no chance to to check the request count because of retries
                assertEquals(new URL(WebTestCase.URL_FIRST, SUCCESS_URL),
                        mockWebConnection.getLastWebRequest().getUrl());
                assertEquals(HttpMethod.GET,
                        mockWebConnection.getLastWebRequest().getHttpMethod());
            }
            finally {
                miniServer.shutDown();
            }
        }

        /**
         * NoHttpResponseException after preflight.
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_4_0_true", "error_4_0_false",
                        "loadend_4_0_false"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "loadstart_1_0_false",
                        "readystatechange_4_0_true", "error_4_0_false",
                        "loadend_4_0_false"})
        public void addEventListener_async_preflight_NoHttpResponseException() throws Exception {
            final MockWebConnection mockWebConnection = getMockWebConnection();
            mockWebConnection.setResponse(WebTestCase.URL_FIRST, buildHtml(Mode.ASYNC, Execution.ONLY_SEND_PREFLIGHT));

            final List<NameValuePair> headers = new ArrayList<>();
            headers.add(new NameValuePair("Access-Control-Allow-Origin", "*"));
            headers.add(new NameValuePair("Access-Control-Allow-Methods", "*"));
            headers.add(new NameValuePair("Access-Control-Allow-Headers", "X-PINGOTHER"));
            getMockWebConnection().setResponse(new URL("http://localhost:" + PORT2 + SUCCESS_URL),
                    "",  200, "OK", MimeType.TEXT_HTML, headers);
            MiniServer.configureDropGetRequest(new URL("http://localhost:" + PORT2 + SUCCESS_URL));

            final MiniServer miniServer1 = new MiniServer(PORT, mockWebConnection);
            miniServer1.start();
            try {
                final MiniServer miniServer2 = new MiniServer(PORT2, mockWebConnection);
                miniServer2.start();

                try {
                    final WebDriver driver = getWebDriver();
                    driver.get(WebTestCase.URL_FIRST.toExternalForm());

                    verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));

                    // no chance to to check the request count because of retries
                    assertEquals(new URL("http://localhost:" + PORT2 + SUCCESS_URL),
                            mockWebConnection.getLastWebRequest().getUrl());
                    assertEquals(HttpMethod.GET,
                            mockWebConnection.getLastWebRequest().getHttpMethod());
                }
                finally {
                    miniServer2.shutDown();
                }
            }
            finally {
                miniServer1.shutDown();
            }
        }

        /**
         * NoHttpResponseException during preflight.
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_4_0_true", "error_4_0_false",
                        "loadend_4_0_false"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "loadstart_1_0_false",
                        "readystatechange_4_0_true", "error_4_0_false",
                        "loadend_4_0_false"})
        public void addEventListener_async_preflight_NoHttpResponseException_during_preflight() throws Exception {
            final MockWebConnection mockWebConnection = getMockWebConnection();
            mockWebConnection.setResponse(WebTestCase.URL_FIRST, buildHtml(Mode.ASYNC, Execution.ONLY_SEND_PREFLIGHT));
            MiniServer.configureDropRequest(new URL("http://localhost:" + PORT2 + SUCCESS_URL));

            final MiniServer miniServer1 = new MiniServer(PORT, mockWebConnection);
            miniServer1.start();
            try {
                final MiniServer miniServer2 = new MiniServer(PORT2, mockWebConnection);
                miniServer2.start();

                try {
                    final WebDriver driver = getWebDriver();
                    driver.get(WebTestCase.URL_FIRST.toExternalForm());

                    verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));

                    // no chance to to check the request count because of retries
                    assertEquals(new URL("http://localhost:" + PORT2 + SUCCESS_URL),
                            mockWebConnection.getLastWebRequest().getUrl());
                    assertEquals(HttpMethod.OPTIONS,
                            mockWebConnection.getLastWebRequest().getHttpMethod());
                }
                finally {
                    miniServer2.shutDown();
                }
            }
            finally {
                miniServer1.shutDown();
            }
        }

        /**
         * NoHttpResponseException.
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "ExceptionThrown"},
                FF = {"readystatechange_1_0_true", "open-done", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false", "ExceptionThrown"},
                FF78 = {"readystatechange_1_0_true", "open-done", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false", "ExceptionThrown"})
        public void onKeyWord_sync_NoHttpResponseException() throws Exception {
            final MockWebConnection mockWebConnection = getMockWebConnection();
            mockWebConnection.setResponse(WebTestCase.URL_FIRST, buildHtml(Mode.SYNC, Execution.ONLY_SEND));
            MiniServer.configureDropRequest(new URL(WebTestCase.URL_FIRST + SUCCESS_URL));

            final MiniServer miniServer = new MiniServer(PORT, mockWebConnection);
            miniServer.start();
            try {
                final WebDriver driver = getWebDriver();
                driver.get(WebTestCase.URL_FIRST.toExternalForm());

                verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));

                // no chance to to check the request count because of retries
                assertEquals(new URL(WebTestCase.URL_FIRST, SUCCESS_URL),
                        mockWebConnection.getLastWebRequest().getUrl());
                assertEquals(HttpMethod.GET,
                        mockWebConnection.getLastWebRequest().getHttpMethod());
            }
            finally {
                miniServer.shutDown();
            }
        }

        /**
         * NoHttpResponseException after preflight.
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "ExceptionThrown"},
                FF = {"readystatechange_1_0_true", "open-done", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false", "ExceptionThrown"},
                FF78 = {"readystatechange_1_0_true", "open-done", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false", "ExceptionThrown"})
        public void onKeyWord_sync_preflight_NoHttpResponseException() throws Exception {
            final MockWebConnection mockWebConnection = getMockWebConnection();
            mockWebConnection.setResponse(WebTestCase.URL_FIRST, buildHtml(Mode.SYNC, Execution.ONLY_SEND_PREFLIGHT));

            final List<NameValuePair> headers = new ArrayList<>();
            headers.add(new NameValuePair("Access-Control-Allow-Origin", "*"));
            headers.add(new NameValuePair("Access-Control-Allow-Methods", "*"));
            headers.add(new NameValuePair("Access-Control-Allow-Headers", "X-PINGOTHER"));
            getMockWebConnection().setResponse(new URL("http://localhost:" + PORT2 + SUCCESS_URL),
                    "",  200, "OK", MimeType.TEXT_HTML, headers);
            MiniServer.configureDropGetRequest(new URL("http://localhost:" + PORT2 + SUCCESS_URL));

            final MiniServer miniServer1 = new MiniServer(PORT, mockWebConnection);
            miniServer1.start();
            try {
                final MiniServer miniServer2 = new MiniServer(PORT2, mockWebConnection);
                miniServer2.start();

                try {
                    final WebDriver driver = getWebDriver();
                    driver.get(WebTestCase.URL_FIRST.toExternalForm());

                    verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));

                    // no chance to to check the request count because of retries
                    assertEquals(new URL("http://localhost:" + PORT2 + SUCCESS_URL),
                            mockWebConnection.getLastWebRequest().getUrl());
                    assertEquals(HttpMethod.GET,
                            mockWebConnection.getLastWebRequest().getHttpMethod());
                }
                finally {
                    miniServer2.shutDown();
                }
            }
            finally {
                miniServer1.shutDown();
            }
        }

        /**
         * NoHttpResponseException during preflight.
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "ExceptionThrown"},
                FF = {"readystatechange_1_0_true", "open-done", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false", "ExceptionThrown"},
                FF78 = {"readystatechange_1_0_true", "open-done", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false", "ExceptionThrown"})
        public void onKeyWord_sync_preflight_NoHttpResponseException_during_preflight() throws Exception {
            final MockWebConnection mockWebConnection = getMockWebConnection();
            mockWebConnection.setResponse(WebTestCase.URL_FIRST, buildHtml(Mode.SYNC, Execution.ONLY_SEND_PREFLIGHT));
            MiniServer.configureDropRequest(new URL("http://localhost:" + PORT2 + SUCCESS_URL));

            final MiniServer miniServer1 = new MiniServer(PORT, mockWebConnection);
            miniServer1.start();
            try {
                final MiniServer miniServer2 = new MiniServer(PORT2, mockWebConnection);
                miniServer2.start();

                try {
                    final WebDriver driver = getWebDriver();
                    driver.get(WebTestCase.URL_FIRST.toExternalForm());

                    verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));

                    // no chance to to check the request count because of retries
                    assertEquals(new URL("http://localhost:" + PORT2 + SUCCESS_URL),
                            mockWebConnection.getLastWebRequest().getUrl());
                    assertEquals(HttpMethod.OPTIONS,
                            mockWebConnection.getLastWebRequest().getHttpMethod());
                }
                finally {
                    miniServer2.shutDown();
                }
            }
            finally {
                miniServer1.shutDown();
            }
        }

        /**
         * NoHttpResponseException.
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_4_0_true", "error_4_0_false",
                        "loadend_4_0_false"},
                FF = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "progress_1_0_false", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false"},
                FF78 = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "progress_1_0_false", "readystatechange_4_0_true",
                        "error_4_0_false", "loadend_4_0_false"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "loadstart_1_0_false",
                        "readystatechange_4_0_true", "error_4_0_false",
                        "loadend_4_0_false"})
        public void onKeyWord_async_NoHttpResponseException() throws Exception {
            final MockWebConnection mockWebConnection = getMockWebConnection();
            mockWebConnection.setResponse(WebTestCase.URL_FIRST, buildHtml(Mode.ASYNC, Execution.ONLY_SEND));
            MiniServer.configureDropRequest(new URL(WebTestCase.URL_FIRST + SUCCESS_URL));

            final MiniServer miniServer = new MiniServer(PORT, mockWebConnection);
            miniServer.start();
            try {
                final WebDriver driver = getWebDriver();
                driver.get(WebTestCase.URL_FIRST.toExternalForm());

                verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));

                // no chance to to check the request count because of retries
                assertEquals(new URL(WebTestCase.URL_FIRST, SUCCESS_URL),
                        mockWebConnection.getLastWebRequest().getUrl());
                assertEquals(HttpMethod.GET,
                        mockWebConnection.getLastWebRequest().getHttpMethod());
            }
            finally {
                miniServer.shutDown();
            }
        }

        /**
         * NoHttpResponseException after preflight.
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_4_0_true", "error_4_0_false",
                        "loadend_4_0_false"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "loadstart_1_0_false",
                        "readystatechange_4_0_true", "error_4_0_false",
                        "loadend_4_0_false"})
        public void onKeyWord_async_preflight_NoHttpResponseException() throws Exception {
            final MockWebConnection mockWebConnection = getMockWebConnection();
            mockWebConnection.setResponse(WebTestCase.URL_FIRST, buildHtml(Mode.ASYNC, Execution.ONLY_SEND_PREFLIGHT));

            final List<NameValuePair> headers = new ArrayList<>();
            headers.add(new NameValuePair("Access-Control-Allow-Origin", "*"));
            headers.add(new NameValuePair("Access-Control-Allow-Methods", "*"));
            headers.add(new NameValuePair("Access-Control-Allow-Headers", "X-PINGOTHER"));
            getMockWebConnection().setResponse(new URL("http://localhost:" + PORT2 + SUCCESS_URL),
                    "",  200, "OK", MimeType.TEXT_HTML, headers);
            MiniServer.configureDropGetRequest(new URL("http://localhost:" + PORT2 + SUCCESS_URL));

            final MiniServer miniServer1 = new MiniServer(PORT, mockWebConnection);
            miniServer1.start();
            try {
                final MiniServer miniServer2 = new MiniServer(PORT2, mockWebConnection);
                miniServer2.start();

                try {
                    final WebDriver driver = getWebDriver();
                    driver.get(WebTestCase.URL_FIRST.toExternalForm());

                    verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));

                    // no chance to to check the request count because of retries
                    assertEquals(new URL("http://localhost:" + PORT2 + SUCCESS_URL),
                            mockWebConnection.getLastWebRequest().getUrl());
                    assertEquals(HttpMethod.GET,
                            mockWebConnection.getLastWebRequest().getHttpMethod());
                }
                finally {
                    miniServer2.shutDown();
                }
            }
            finally {
                miniServer1.shutDown();
            }
        }

        /**
         * NoHttpResponseException during preflight.
         * @throws Exception if the test fails
         */
        @Test
        @Alerts(DEFAULT = {"readystatechange_1_0_true", "open-done", "loadstart_1_0_false",
                        "send-done", "readystatechange_4_0_true", "error_4_0_false",
                        "loadend_4_0_false"},
                IE = {"readystatechange_1_0_true", "open-done", "readystatechange_1_0_true",
                        "send-done", "loadstart_1_0_false",
                        "readystatechange_4_0_true", "error_4_0_false",
                        "loadend_4_0_false"})
        public void onKeyWord_async_preflight_NoHttpResponseException_during_preflight() throws Exception {
            final MockWebConnection mockWebConnection = getMockWebConnection();
            mockWebConnection.setResponse(WebTestCase.URL_FIRST, buildHtml(Mode.ASYNC, Execution.ONLY_SEND_PREFLIGHT));
            MiniServer.configureDropRequest(new URL("http://localhost:" + PORT2 + SUCCESS_URL));

            final MiniServer miniServer1 = new MiniServer(PORT, mockWebConnection);
            miniServer1.start();
            try {
                final MiniServer miniServer2 = new MiniServer(PORT2, mockWebConnection);
                miniServer2.start();

                try {
                    final WebDriver driver = getWebDriver();
                    driver.get(WebTestCase.URL_FIRST.toExternalForm());

                    verifyAlerts(() -> extractLog(driver), String.join("\n", getExpectedAlerts()));

                    // no chance to to check the request count because of retries
                    assertEquals(new URL("http://localhost:" + PORT2 + SUCCESS_URL),
                            mockWebConnection.getLastWebRequest().getUrl());
                    assertEquals(HttpMethod.OPTIONS,
                            mockWebConnection.getLastWebRequest().getHttpMethod());
                }
                finally {
                    miniServer2.shutDown();
                }
            }
            finally {
                miniServer1.shutDown();
            }
        }
    }

    static String extractLog(final WebDriver driver) {
        return driver.findElement(By.id("log")).getAttribute("value").trim().replaceAll("\r", "");
    }

    /**
     * Alerts each State that has been triggered in the form of:
     * event.type_(isUndefined?)
     * @param mode
     * @param execution
     * @param statesParam
     * @return
     */
    static String buildHtml(final Mode mode, final Execution execution) {
        final StringBuffer htmlBuilder = new StringBuffer();
        htmlBuilder.append("<html>\n");
        htmlBuilder.append("  <head>\n");
        htmlBuilder.append("    <title>XMLHttpRequest Test</title>\n");
        htmlBuilder.append("    <script>\n");
        htmlBuilder.append("      function test() {\n");
        htmlBuilder.append("        document.getElementById('log').value = '';\n");
        htmlBuilder.append("        xhr = new XMLHttpRequest();\n");
        Arrays.asList(State.values()).forEach(state -> registerEventListener(htmlBuilder, mode, state));

        if (Execution.WITHOUT_ORIGIN.equals(execution)
                || Execution.WITHOUT_ORIGIN_PREFLIGHT.equals(execution)) {
            htmlBuilder.append("        var url = 'http://' + window.location.hostname + ':"
                    + WebTestCase.PORT2 + SUCCESS_WITHOUT_ORIGIN_URL + "';\n");
        }
        else if (Execution.NETWORK_ERROR.equals(execution)
                || Execution.NETWORK_ERROR_PREFLIGHT.equals(execution)) {
            htmlBuilder.append("        var url = 'https://' + window.location.hostname + ':"
                                + WebTestCase.PORT + SUCCESS_URL + "';\n");
        }
        else if (Execution.ERROR_403.equals(execution)) {
            htmlBuilder.append("        var url = '" + ERROR_403_URL + "';\n");
        }
        else if (Execution.ERROR_403_PREFLIGHT.equals(execution)) {
            htmlBuilder.append("        var url = 'http://' + window.location.hostname + ':"
                    + WebTestCase.PORT2 + ERROR_403_URL + "';\n");
        }
        else if (Execution.ERROR_403_DURING_PREFLIGHT.equals(execution)) {
            htmlBuilder.append("        var url = 'http://' + window.location.hostname + ':"
                    + WebTestCase.PORT2 + PREFLIGHT_ERROR_403_URL + "';\n");
        }
        else if (Execution.ERROR_500.equals(execution)) {
            htmlBuilder.append("        var url = '" + ERROR_500_URL + "';\n");
        }
        else if (Execution.ERROR_500_PREFLIGHT.equals(execution)) {
            htmlBuilder.append("        var url = 'http://' + window.location.hostname + ':"
                    + WebTestCase.PORT2 + ERROR_500_URL + "';\n");
        }
        else if (Execution.ERROR_500_DURING_PREFLIGHT.equals(execution)) {
            htmlBuilder.append("        var url = 'http://' + window.location.hostname + ':"
                    + WebTestCase.PORT2 + PREFLIGHT_ERROR_500_URL + "';\n");
        }
        else if (Execution.TIMEOUT.equals(execution)) {
            htmlBuilder.append("        var url = '" + TIMEOUT_URL + "';\n");
        }
        else if (Execution.ONLY_SEND_PREFLIGHT.equals(execution)
                || Execution.ONLY_SEND_PREFLIGHT_FORBIDDEN.equals(execution)) {
            htmlBuilder.append("        var url = 'http://' + window.location.hostname + ':"
                    + WebTestCase.PORT2 + SUCCESS_URL + "';\n");
        }
        else {
            htmlBuilder.append("        var url = '" + SUCCESS_URL + "';\n");
        }

        htmlBuilder.append("        xhr.open('GET', url, ").append(mode.isAsync()).append(");\n");
        htmlBuilder.append("        logText('open-done');");

        htmlBuilder.append("        try {\n");

        if (Execution.ONLY_SEND_PREFLIGHT.equals(execution)
                || Execution.WITHOUT_ORIGIN_PREFLIGHT.equals(execution)
                || Execution.NETWORK_ERROR_PREFLIGHT.equals(execution)
                || Execution.ERROR_403_PREFLIGHT.equals(execution)
                || Execution.ERROR_403_DURING_PREFLIGHT.equals(execution)
                || Execution.ERROR_500_PREFLIGHT.equals(execution)
                || Execution.ERROR_500_DURING_PREFLIGHT.equals(execution)) {
            htmlBuilder.append("        xhr.setRequestHeader('X-PINGOTHER', 'pingpong');\n");
        }
        else if (Execution.ONLY_SEND_PREFLIGHT_FORBIDDEN.equals(execution)) {
            htmlBuilder.append("        xhr.setRequestHeader('X-FORBIDDEN', 'forbidden');\n");
        }

        if (Execution.TIMEOUT.equals(execution)) {
            htmlBuilder.append("        xhr.timeout = 10;\n");
        }

        htmlBuilder.append("           xhr.send();\n");
        htmlBuilder.append("           logText('send-done');");
        if (Execution.SEND_ABORT.equals(execution)) {
            htmlBuilder.append("           xhr.abort();\n");
            htmlBuilder.append("           logText('abort-done');");
        }
        htmlBuilder.append("        } catch (e) { logText('ExceptionThrown'); }\n");
        htmlBuilder.append("      }\n");

        htmlBuilder.append("      function alertEventState(event) {\n");
        htmlBuilder.append("        logText(event.type + '_' + xhr.readyState + '_'"
                                        + "+ xhr.status + '_' + (event.loaded === undefined));\n");
        htmlBuilder.append("      }\n");

        htmlBuilder.append("      function alertAbort(event) {\n");
        htmlBuilder.append("        logText(event.type + '_' + xhr.readyState + '_' + xhr.status);\n");
        htmlBuilder.append("      }\n");

        htmlBuilder.append("      function logText(txt) {\n");
        htmlBuilder.append("        document.getElementById('log').value += txt + '\\n';\n");
        htmlBuilder.append("      }\n");
        htmlBuilder.append("    </script>\n");
        htmlBuilder.append("  </head>\n");
        htmlBuilder.append("  <body onload='test()'>\n");
        htmlBuilder.append("    <textarea id='log' cols='80' rows='40'></textarea>\n");
        htmlBuilder.append("  </body>\n");
        htmlBuilder.append("</html>");

        return htmlBuilder.toString();
    }

    static void registerEventListener(final StringBuffer buffer, final Mode mode, final State state) {
        String function = "alertEventState";
        if (State.ABORT.equals(state)) {
            function = "alertAbort";
        }

        if (mode.isUseOnKeyword()) {
            buffer.append("        xhr.on").append(state.getEventName_()).append("=").append(function).append(";\n");
        }
        else {
            buffer.append("        xhr.addEventListener('").append(state.getEventName_()).append("', ").append(function)
                    .append(");\n");
        }
    }

    private XMLHttpRequestLifeCycleTest() {
    }
}
