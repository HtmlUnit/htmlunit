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
package org.htmlunit.html;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.htmlunit.CollectingAlertHandler;
import org.htmlunit.MockWebConnection;
import org.htmlunit.Page;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.WebClient;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link org.htmlunit.WebClientOptions#setLocalFileAccessFromRemotePagesAllowed(boolean)}.
 * The pages are loaded from the (mock) server and refer to local files; this is blocked by default
 * (like in the browsers) and allowed if the option is enabled.
 *
 * @see LocalResourceAccessTest
 * @see LocalResourceAccess2Test
 * @author Ronald Brill
 */
public class LocalResourceAccess3Test extends SimpleWebTestCase {

    private File dir_;
    private String dirUrl_;

    private void createFiles() throws Exception {
        dir_ = Files.createTempDirectory("htmlunit-local").toFile().getCanonicalFile();
        dirUrl_ = dir_.toPath().toUri().toString();

        FileUtils.writeStringToFile(new File(dir_, "local.js"), "var localScript = 'loaded';", UTF_8);
        FileUtils.writeStringToFile(new File(dir_, "local.html"),
                DOCTYPE_HTML + "<html><head><title>local</title></head><body></body></html>", UTF_8);
    }

    /**
     * Deletes the temporary directory.
     */
    @AfterEach
    public void deleteFiles() {
        if (dir_ != null) {
            FileUtils.deleteQuietly(dir_);
            dir_ = null;
        }
    }

    private WebClient webClient(final boolean allowed) {
        final WebClient client = getWebClientWithMockWebConnection();
        client.getOptions().setLocalFileAccessFromRemotePagesAllowed(allowed);
        return client;
    }

    private List<String> loadScriptPage(final boolean allowed) throws Exception {
        createFiles();
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script src='" + dirUrl_ + "local.js'></script>\n"
                + "<script>alert(typeof localScript);</script>\n"
                + "</head><body></body></html>";

        final WebClient client = webClient(allowed);
        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));
        getMockWebConnection().setResponse(URL_FIRST, html);
        client.getPage(URL_FIRST);
        return collectedAlerts;
    }

    /**
     * The option is disabled by default.
     */
    @Test
    public void defaultValue() {
        Assertions.assertFalse(getWebClient().getOptions().isLocalFileAccessFromRemotePagesAllowed());
    }

    /**
     * A script from a local file is blocked by default.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void scriptBlocked() throws Exception {
        assertEquals(List.of("undefined"), loadScriptPage(false));
    }

    /**
     * A script from a local file is loaded if the option is enabled.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void scriptAllowed() throws Exception {
        assertEquals(List.of("string"), loadScriptPage(true));
    }

    /**
     * A local file is loaded into a frame if the option is enabled.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void iframeAllowed() throws Exception {
        createFiles();
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<iframe src='" + dirUrl_ + "local.html'></iframe>\n"
                + "</body></html>";

        final WebClient client = webClient(true);
        getMockWebConnection().setResponse(URL_FIRST, html);
        final HtmlPage page = client.getPage(URL_FIRST);

        final Page framePage = page.getFrames().get(0).getEnclosedPage();
        assertEquals("file", framePage.getUrl().getProtocol());
        assertEquals("local", ((HtmlPage) framePage).getTitleText());
    }

    /**
     * Setting the location to a local file navigates if the option is enabled.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void locationAllowed() throws Exception {
        createFiles();
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>location.href = '" + dirUrl_ + "local.html';</script>\n"
                + "</head><body></body></html>";

        final WebClient client = webClient(true);
        getMockWebConnection().setResponse(URL_FIRST, html);
        client.getPage(URL_FIRST);
        client.waitForBackgroundJavaScript(DEFAULT_WAIT_TIME.toMillis());

        final Page page = client.getCurrentWindow().getEnclosedPage();
        assertEquals("file", page.getUrl().getProtocol());
    }

    /**
     * window.open() of a local file opens the window if the option is enabled.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void windowOpenAllowed() throws Exception {
        createFiles();
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>window.open('" + dirUrl_ + "local.html', 'localWindow');</script>\n"
                + "</head><body></body></html>";

        final WebClient client = webClient(true);
        getMockWebConnection().setResponse(URL_FIRST, html);
        client.getPage(URL_FIRST);

        final Page page = client.getWebWindowByName("localWindow").getEnclosedPage();
        assertEquals("file", page.getUrl().getProtocol());
    }

    /**
     * A redirect to a local file is a network error (Fetch Standard).
     *
     * @throws Exception if the test fails
     */
    @Test
    public void redirectBlocked() throws Exception {
        createFiles();
        final WebClient client = webClient(false);
        redirect(dirUrl_ + "local.html");

        Assertions.assertThrows(IOException.class, () -> client.getPage(URL_FIRST));
    }

    /**
     * The option does not allow redirects to local files; the redirect is still a network error
     * (Fetch Standard).
     *
     * @throws Exception if the test fails
     */
    @Test
    public void redirectBlockedWithOption() throws Exception {
        createFiles();
        final WebClient client = webClient(true);
        redirect(dirUrl_ + "local.html");

        Assertions.assertThrows(IOException.class, () -> client.getPage(URL_FIRST));
    }

    private void redirect(final String target) {
        final MockWebConnection conn = getMockWebConnection();
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Location", target));
        conn.setResponse(URL_FIRST, "", 302, "Found", MimeType.TEXT_HTML, headers);
    }
}
