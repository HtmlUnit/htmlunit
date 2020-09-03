/*
 * Copyright (c) 2002-2020 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriverException;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.util.MimeType;

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
 * This is mainly done because we cannot reliably handle the amount & speed of the alerts if everything is
 * executed together (Chrome did work in tests, FF & IE did not).
 *
 * @author Thorsten Wendelmuth
 *
 */
@RunWith(BrowserRunner.class)
public class XmlHttpRequestLifeCycleTest extends WebDriverTestCase {
    private static final String SUCCESS_URL = "/xmlhttprequest/success.html";
    private static final String ERROR_URL = "/xmlhttprequest/error.html";
    private static final String TIMEOUT_URL = "/xmlhttprequest/timeout.html";

    private static final String RETURN_XML = "<xml>\n"
            + "<content>htmlunit</content>\n"
            + "<content>xmlhttpRequest</content>\n"
            + "</xml>";

    private final Map<String, Class<? extends Servlet>> servlets_ = new HashMap<>();

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
        ONLY_SEND, SEND_ABORT, NETWORK_ERROR, ERROR_500, TIMEOUT
    }

    @Before
    public void prepareTestingServlets() {
        servlets_.put(SUCCESS_URL, Xml200Servlet.class);
        servlets_.put(ERROR_URL, Xml500Servlet.class);
        servlets_.put(TIMEOUT_URL, XmlTimeoutServlet.class);
    }

    @Test
    @Alerts({"readystatechange_true", "readystatechange_true", "load_false", "loadend_false"})
    public void addEventListener_lifeCycle_sync() throws Exception {
        //we can register ourselves for every state here since it's in sync mode and most of them won't fire anyway.
        loadPageWithAlerts2(buildHtml(Mode.SYNC, State.values()), URL_FIRST, DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    @Alerts("readystatechange_true")
    public void addEventListener_lifeCycle_sync_networkError() throws Exception {
        //will throw an exception and user is supposed to handle this.
        //That's why we only have one readystatechange callback.
        loadPageWithAlerts2(buildHtml(Mode.SYNC, Execution.NETWORK_ERROR, State.values()), URL_FIRST, DEFAULT_WAIT_TIME,
                servlets_);
    }

    @Test
    @Alerts({"readystatechange_true", "readystatechange_true", "load_false", "loadend_false"})
    public void addEventListener_lifeCycle_sync_Error500() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.SYNC, Execution.ERROR_500, State.values()), URL_FIRST, DEFAULT_WAIT_TIME,
                servlets_);
    }

    @Test
    public void addEventListener_lifeCycle_sync_timeout() throws Exception {
        //that's invalid. You cannot set timeout for synced requests. Will throw an exception and not trigger any event.
        try {
            loadPageWithAlerts2(buildHtml(Mode.SYNC, Execution.TIMEOUT, State.values()), URL_FIRST, DEFAULT_WAIT_TIME,
                    servlets_);
        }
        catch (final WebDriverException e) {
            if (useRealBrowser()) {
                //we only expect the error to be thrown in htmlunit scenarios.
                throw e;
            }
        }
    }

    /*
     * Testing the whole lifecycle of an async request
     * we're doing this one by one since we're getting too many alerts to reliably determine the test outcome.
     */

    @Test
    @Alerts("loadstart_false")
    public void addEventListener_lifeCycle_async_loadStart() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC, State.LOAD_START), URL_FIRST, DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    @Alerts("load_false")
    public void addEventListener_lifeCycle_async_load() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC, State.LOAD), URL_FIRST, DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    @Alerts("loadend_false")
    public void addEventListener_lifeCycle_async_loadEnd() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC, State.LOAD_END), URL_FIRST, DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    @Alerts("progress_false")
    public void addEventListener_lifeCycle_async_progress() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC, State.PROGRESS), URL_FIRST, DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    @Alerts({"readystatechange_true", "readystatechange_true", "readystatechange_true", "readystatechange_true"})
    public void addEventListener_lifeCycle_async_readyStateChange() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC, State.READY_STATE_CHANGE), URL_FIRST, DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    public void addEventListener_lifeCycle_async_error() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC, State.ERROR), URL_FIRST, DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    public void addEventListener_lifeCycle_async_noAbort() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC, State.ABORT), URL_FIRST, DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    @Alerts("abort")
    public void addEventListener_lifeCycle_async_abortTriggered() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC, Execution.SEND_ABORT, State.ABORT), URL_FIRST, DEFAULT_WAIT_TIME,
                servlets_);
    }

    @Test
    @Alerts("error_false")
    public void addEventListener_lifeCycle_async_networkErrorTriggered() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC, Execution.NETWORK_ERROR, State.ERROR), URL_FIRST, DEFAULT_WAIT_TIME,
                servlets_);
    }

    @Test
    @Alerts("loadstart_false")
    public void addEventListener_lifeCycle_async_networkErrorTriggered_loadStart() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC, Execution.NETWORK_ERROR, State.LOAD_START), URL_FIRST,
                DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    @Alerts("loadend_false")
    public void addEventListener_lifeCycle_async_networkErrorTriggered_loadEnd() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC, Execution.NETWORK_ERROR, State.LOAD_END), URL_FIRST,
                DEFAULT_WAIT_TIME, servlets_);
    }

    /**
     * Error 500 on the server side still count as a valid requests for {@link XMLHttpRequest}.
     */
    @Test
    public void addEventListener_lifeCycle_async_Error500Triggered() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC, Execution.ERROR_500, State.ERROR), URL_FIRST, DEFAULT_WAIT_TIME,
                servlets_);
    }

    @Test
    @Alerts("loadstart_false")
    public void addEventListener_timeout_async_loadStart() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC, Execution.TIMEOUT, State.LOAD_START), URL_FIRST, DEFAULT_WAIT_TIME,
                servlets_);
    }

    @Test
    @Alerts("loadend_false")
    public void addEventListener_timeout_async_loadEnd() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC, Execution.TIMEOUT, State.LOAD_END), URL_FIRST, DEFAULT_WAIT_TIME,
                servlets_);
    }

    @Test
    @Alerts("timeout_false")
    @NotYetImplemented
    public void addEventListener_timeout_async_timeout() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC, Execution.TIMEOUT, State.TIMEOUT), URL_FIRST, DEFAULT_WAIT_TIME,
                servlets_);
    }

    //same tests as above, but this time we're triggering with the onkeyword.
    @Test
    @Alerts({"readystatechange_true", "readystatechange_true", "load_false", "loadend_false"})
    public void onKeyWord_lifeCycle_sync() throws Exception {
        //we can register ourselves for every state here since it's in sync mode and most of them won't fire anyway.
        loadPageWithAlerts2(buildHtml(Mode.SYNC_ON_KEYWORD, State.values()), URL_FIRST, DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    @Alerts("readystatechange_true")
    public void onKeyWord_lifeCycle_sync_networkError() throws Exception {
        //will throw an exception and user is supposed to handle this.
        //That's why we only have one readystatechange callback.
        loadPageWithAlerts2(buildHtml(Mode.SYNC_ON_KEYWORD, Execution.NETWORK_ERROR, State.values()), URL_FIRST,
                DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    @Alerts({"readystatechange_true", "readystatechange_true", "load_false", "loadend_false"})
    public void onKeyWord_lifeCycle_sync_Error500() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.SYNC_ON_KEYWORD, Execution.ERROR_500, State.values()), URL_FIRST,
                DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    public void onKeyWord_lifeCycle_sync_timeout() throws Exception {
        //that's invalid. You cannot set timeout for synced requests. Will throw an exception and not trigger any event.
        try {
            loadPageWithAlerts2(buildHtml(Mode.SYNC_ON_KEYWORD, Execution.TIMEOUT, State.values()), URL_FIRST,
                    DEFAULT_WAIT_TIME, servlets_);
        }
        catch (final WebDriverException e) {
            if (useRealBrowser()) {
                //we only expect the error to be thrown in htmlunit scenarios.
                throw e;
            }
        }
    }

    @Test
    @Alerts("loadstart_false")
    public void onKeyWord_lifeCycle_async_loadStart() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC_ON_KEYWORD, State.LOAD_START), URL_FIRST, DEFAULT_WAIT_TIME,
                servlets_);
    }

    @Test
    @Alerts("load_false")
    public void onKeyWord_lifeCycle_async_load() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC_ON_KEYWORD, State.LOAD), URL_FIRST, DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    @Alerts("loadend_false")
    public void onKeyWord_lifeCycle_async_loadEnd() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC_ON_KEYWORD, State.LOAD_END), URL_FIRST, DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    @Alerts("progress_false")
    public void onKeyWord_lifeCycle_async_progress() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC_ON_KEYWORD, State.PROGRESS), URL_FIRST, DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    @Alerts({"readystatechange_true", "readystatechange_true", "readystatechange_true", "readystatechange_true"})
    public void onKeyWord_lifeCycle_async_readyStateChange() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC_ON_KEYWORD, State.READY_STATE_CHANGE), URL_FIRST, DEFAULT_WAIT_TIME,
                servlets_);
    }

    @Test
    public void onKeyWord_lifeCycle_async_error() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC_ON_KEYWORD, State.ERROR), URL_FIRST, DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    public void onKeyWord_lifeCycle_async_noAbort() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC_ON_KEYWORD, State.ABORT), URL_FIRST, DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    @Alerts("abort")
    public void onKeyWord_lifeCycle_async_abortTriggered() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC_ON_KEYWORD, Execution.SEND_ABORT, State.ABORT), URL_FIRST,
                DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    @Alerts("error_false")
    public void onKeyWord_lifeCycle_async_networkErrorTriggered() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC_ON_KEYWORD, Execution.NETWORK_ERROR, State.ERROR), URL_FIRST,
                DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    @Alerts("loadstart_false")
    public void onKeyWord_lifeCycle_async_networkErrorTriggered_loadStart() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC_ON_KEYWORD, Execution.NETWORK_ERROR, State.LOAD_START), URL_FIRST,
                DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    @Alerts("loadend_false")
    public void onKeyWord_lifeCycle_async_networkErrorTriggered_loadEnd() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC_ON_KEYWORD, Execution.NETWORK_ERROR, State.LOAD_END), URL_FIRST,
                DEFAULT_WAIT_TIME, servlets_);
    }

    /**
     * Error 500 on the server side still count as a valid requests for {@link XMLHttpRequest}.
     */
    @Test
    public void onKeyWord_lifeCycle_async_Error500Triggered() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC_ON_KEYWORD, Execution.ERROR_500, State.ERROR), URL_FIRST,
                DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    @Alerts("loadstart_false")
    public void onKeyWord_timeout_async_loadStart() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC_ON_KEYWORD, Execution.TIMEOUT, State.LOAD_START), URL_FIRST,
                DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    @Alerts("loadend_false")
    public void onKeyWord_timeout_async_loadEnd() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC_ON_KEYWORD, Execution.TIMEOUT, State.LOAD_END), URL_FIRST,
                DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    @Alerts("timeout_false")
    @NotYetImplemented
    public void onKeyWord_timeout_async_timeout() throws Exception {
        loadPageWithAlerts2(buildHtml(Mode.ASYNC_ON_KEYWORD, Execution.TIMEOUT, State.TIMEOUT), URL_FIRST,
                DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    @Alerts({"readystatechange_true", "loadstart_false", "readystatechange_true", "readystatechange_true",
            "progress_false", "readystatechange_true", "load_false", "loadend_false"})
    @Ignore("The tests are highly faulty because of the collecting of the alerts. We're swallowing some the alerts."
            + "The test is still useful to check that the order of the events is correct.")
    public void addEventListener_async_all_success() throws Exception {
        //we can register ourselves for every state here since it's in sync mode and most of them won't fire anyway.
        loadPageWithAlerts2(buildHtml(Mode.ASYNC, State.values()), URL_FIRST, DEFAULT_WAIT_TIME, servlets_);
    }

    @Test
    @Alerts({"readystatechange_true", "loadstart_false", "readystatechange_true", "readystatechange_true",
            "progress_false", "readystatechange_true", "load_false", "loadend_false"})
    @Ignore("The tests are highly faulty because of the collecting of the alerts. We're swallowing some the alerts."
            + "The test is still useful to check that the order of the events is correct.")
    public void onKeyWord_async_all_success() throws Exception {
        //we can register ourselves for every state here since it's in sync mode and most of them won't fire anyway.
        loadPageWithAlerts2(buildHtml(Mode.ASYNC_ON_KEYWORD, State.values()), URL_FIRST, DEFAULT_WAIT_TIME, servlets_);
    }

    private String buildHtml(final Mode mode, final State... statesParam) {
        return buildHtml(mode, Execution.ONLY_SEND, statesParam);
    }

    /**
     * Alerts each State that has been triggered in the form of:
     * event.type_(isUndefined?)
     * @param mode
     * @param execution
     * @param statesParam
     * @return
     */
    private String buildHtml(final Mode mode, final Execution execution, final State... statesParam) {
        final List<State> states = Arrays.asList(statesParam);

        final StringBuffer htmlBuilder = new StringBuffer();
        htmlBuilder.append("<html>\n");
        htmlBuilder.append("  <head>\n");
        htmlBuilder.append("    <title>XMLHttpRequest Test</title>\n");
        htmlBuilder.append("    <script>\n");
        htmlBuilder.append("      function test() {\n");
        htmlBuilder.append("        var xhr = new XMLHttpRequest();\n");
        states.forEach(state -> registerEventListener(htmlBuilder, mode, state));

        htmlBuilder.append("        xhr.open('GET', '");
        if (Execution.NETWORK_ERROR.equals(execution)) {
            htmlBuilder.append((URL_FIRST + SUCCESS_URL).replace("http://", "https://"));
        }
        else if (Execution.ERROR_500.equals(execution)) {
            htmlBuilder.append(ERROR_URL);
        }
        else if (Execution.TIMEOUT.equals(execution)) {
            htmlBuilder.append(TIMEOUT_URL);
        }
        else {
            htmlBuilder.append(SUCCESS_URL);
        }
        htmlBuilder.append("', ").append(mode.isAsync()).append(");\n");

        if (Execution.TIMEOUT.equals(execution)) {
            htmlBuilder.append("        xhr.timeout = 10;\n");
        }

        htmlBuilder.append("        xhr.send();\n");
        if (Execution.SEND_ABORT.equals(execution)) {
            htmlBuilder.append("        xhr.abort();\n");
        }
        htmlBuilder.append("      }\n");
        htmlBuilder.append("      function alertEventState(event) {\n");
        htmlBuilder.append("        alert(event.type + '_' + (event.loaded === undefined));\n");
        htmlBuilder.append("      }\n");
        htmlBuilder.append("      function alertAbort(event) {\n");
        htmlBuilder.append("        alert(event.type);\n");
        htmlBuilder.append("      }\n");
        htmlBuilder.append("    </script>\n");
        htmlBuilder.append("  </head>\n");
        htmlBuilder.append("  <body onload='test()'>\n");
        htmlBuilder.append("  </body>\n");
        htmlBuilder.append("</html>");

        return htmlBuilder.toString();
    }

    private void registerEventListener(final StringBuffer buffer, final Mode mode, final State state) {
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

    public static class Xml200Servlet extends HttpServlet {

        @Override
        protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
                throws ServletException, IOException {
            resp.setContentType(MimeType.TEXT_XML);
            resp.setContentLength(RETURN_XML.length());
            resp.setStatus(HttpStatus.SC_OK);
            final ServletOutputStream outputStream = resp.getOutputStream();
            try (Writer writer = new OutputStreamWriter(outputStream)) {
                writer.write(RETURN_XML);
            }

        }
    }

    public static class Xml500Servlet extends HttpServlet {

        @Override
        protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
                throws ServletException, IOException {
            resp.setContentType(MimeType.TEXT_XML);
            resp.setContentLength(RETURN_XML.length());
            resp.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            final ServletOutputStream outputStream = resp.getOutputStream();
            try (Writer writer = new OutputStreamWriter(outputStream)) {
                writer.write(RETURN_XML);
            }
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
                //ignored.
            }
        }
    }

}
