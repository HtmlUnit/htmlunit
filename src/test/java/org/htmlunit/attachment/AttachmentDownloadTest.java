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
package org.htmlunit.attachment;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;

/**
 * Tests if a response is handled as download or shown in the window, depending on the
 * {@code Content-Disposition} header and the content type, with real browsers.
 * The expectation is the name of the downloaded file or {@code inline} if the response is shown
 * in the window.
 *
 * @author Ronald Brill
 */
public class AttachmentDownloadTest extends WebDriverTestCase {

    @Override
    protected boolean needDownloads() {
        return true;
    }

    private void request(final String contentDisposition, final String contentType) throws Exception {
        final URL fileUrl = new URL(URL_FIRST, "/file");

        final List<NameValuePair> headers = new ArrayList<>();
        if (contentDisposition != null) {
            headers.add(new NameValuePair("Content-Disposition", contentDisposition));
        }
        getMockWebConnection().setResponse(fileUrl,
                "<html><head><title>inline</title></head><body>content</body></html>",
                200, "OK", contentType, headers);

        loadPage2("<html><body><a id='dl' href='/file'>download</a></body></html>");
        ((JavascriptExecutor) getWebDriver()).executeScript("document.getElementById('dl').click();");

        final List<String> result = waitForDownloads(1, DEFAULT_WAIT_TIME);
        if (result.isEmpty() && "inline".equals(getWebDriver().getTitle())) {
            result.add("inline");
        }
        assertEquals(getExpectedAlerts(), result);
    }

    /**
     * The type attachment is a download.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo.txt")
    public void typeAttachment() throws Exception {
        request("attachment; filename=\"foo.txt\"", "text/html");
    }

    /**
     * The disposition type is case-insensitive.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo.txt")
    public void typeAttachmentCaseInsensitive() throws Exception {
        request("AttachMent; filename=\"foo.txt\"", "text/html");
    }

    /**
     * White space in front of the type is ignored.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo.txt")
    public void typeAttachmentLeadingWhitespace() throws Exception {
        request("  attachment; filename=\"foo.txt\"", "text/html");
    }

    /**
     * White space between the type and the first parameter is ignored.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo.txt")
    public void typeAttachmentWhitespaceBeforeSemicolon() throws Exception {
        request("attachment ; filename=\"foo.txt\"", "text/html");
    }

    /**
     * The type inline is shown in the window, even if there is a file name.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void typeInline() throws Exception {
        request("inline; filename=\"foo.txt\"", "text/html");
    }

    /**
     * The type inline without parameters is shown in the window.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void typeInlineWithoutFilename() throws Exception {
        request("inline", "text/html");
    }

    /**
     * The word attachment as value does not make a download.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void attachmentOnlyAsParameterValue() throws Exception {
        request("inline; filename=attachment", "text/html");
    }

    /**
     * A type that only starts with attachment is an unknown type.
     *
     * @throws Exception if the test fails
     */
    @Test
    // the behavior of the browsers is not verified yet; the expectation is the one from RFC 6266
    @Alerts("foo.txt")
    public void typeStartingWithAttachment() throws Exception {
        request("attachmentfoo; filename=\"foo.txt\"", "text/html");
    }

    /**
     * An unknown type; RFC 6266 says it should be handled like attachment.
     *
     * @throws Exception if the test fails
     */
    @Test
    // the behavior of the browsers is not verified yet; the expectation is the one from RFC 6266
    @Alerts("foo.txt")
    public void unknownType() throws Exception {
        request("foo; filename=\"foo.txt\"", "text/html");
    }

    /**
     * An empty header.
     *
     * @throws Exception if the test fails
     */
    @Test
    // the behavior of the browsers is not verified yet
    @Alerts("inline")
    public void emptyHeader() throws Exception {
        request("", "text/html");
    }

    /**
     * A header with a file name but without a type is shown in the window.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void filenameWithoutType() throws Exception {
        request("; filename=\"foo.txt\"", "text/html");
    }

    /**
     * Without header the content type octet-stream is a download, the name comes from the url.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("file")
    public void octetStreamWithoutHeader() throws Exception {
        request(null, "application/octet-stream");
    }

    /**
     * The content type octet-stream is a download, even if the type is inline.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("file")
    public void octetStreamInline() throws Exception {
        request("inline", "application/octet-stream");
    }

    /**
     * The type attachment with octet-stream is a download.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo.txt")
    public void octetStreamAttachment() throws Exception {
        request("attachment; filename=\"foo.txt\"", "application/octet-stream");
    }
}
