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
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;

/**
 * Tests for the file name a browser suggests for a download ({@code Content-Disposition} header).
 *
 * @author Ronald Brill
 */
public class AttachmentSuggestedFilename2Test extends WebDriverTestCase {

    @Override
    protected boolean needDownloads() {
        return true;
    }

    private void download(final String contentDisposition) throws Exception {
        final URL fileUrl = new URL(URL_FIRST, "/file");

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Content-Disposition", contentDisposition));
        getMockWebConnection().setResponse(fileUrl, "download content", 200, "OK",
                "application/octet-stream", headers);

        loadPage2("<html><body><a id='dl' href='/file'>download</a></body></html>");
        // a script click returns at once; WebElement.click() waits for the (never finishing)
        // navigation of a download in some drivers
        ((JavascriptExecutor) getWebDriver()).executeScript("document.getElementById('dl').click();");

        assertEquals(getExpectedAlerts(), waitForDownloads(1, DEFAULT_WAIT_TIME));
    }

    // ---------------------------------------------------------------------------
    // the plain filename parameter
    // ---------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo.txt")
    public void quoted() throws Exception {
        download("attachment; filename=\"foo.txt\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo.txt")
    public void unquoted() throws Exception {
        download("attachment; filename=foo.txt");
    }

    /**
     * Parameter names are case-insensitive.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo.txt")
    public void upperCaseParameterName() throws Exception {
        download("attachment; FILENAME=\"foo.txt\"");
    }

    /**
     * The disposition type is case-insensitive.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo.txt")
    public void upperCaseDispositionType() throws Exception {
        download("ATTACHMENT; filename=\"foo.txt\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo.txt")
    public void whitespaceAroundEquals() throws Exception {
        download("attachment; filename = \"foo.txt\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo.txt")
    public void whitespaceBeforeSemicolon() throws Exception {
        download("attachment; filename=foo.txt ; size=4");
    }

    /**
     * Other parameters before and after the file name.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo.txt")
    public void otherParameters() throws Exception {
        download("attachment; size=4; filename=\"foo.txt\"; creation-date=\"Wed, 12 Feb 1997 16:29:51 -0500\"");
    }

    /**
     * A semicolon inside of a quoted value does not end the value.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a;b.txt")
    public void semicolonInQuotedValue() throws Exception {
        download("attachment; filename=\"a;b.txt\"; size=4");
    }

    /**
     * A parameter that only ends with 'filename' is another parameter.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("y.txt")
    public void parameterNameWithPrefix() throws Exception {
        download("attachment; xfilename=\"x.txt\"; filename=y.txt");
    }

    /**
     * A directory part is never used.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("_.._evil.txt")
    public void pathTraversal() throws Exception {
        download("attachment; filename=\"../../evil.txt\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("_etc_evil.txt")
    public void absolutePath() throws Exception {
        download("attachment; filename=\"/etc/evil.txt\"");
    }

    // ---------------------------------------------------------------------------
    // no usable file name; the browser takes the name from the url (/file)
    // ---------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("file")
    @HtmlUnitNYI(CHROME = "", EDGE = "", FF = "", FF_ESR = "")
    public void noFilename() throws Exception {
        download("attachment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("file")
    @HtmlUnitNYI(CHROME = "", EDGE = "", FF = "", FF_ESR = "")
    public void emptyFilename() throws Exception {
        download("attachment; filename=\"\"");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("file")
    @HtmlUnitNYI(CHROME = "", EDGE = "", FF = "", FF_ESR = "")
    public void filenameWithoutValue() throws Exception {
        download("attachment; filename=");
    }

    // ---------------------------------------------------------------------------
    // the extended parameter filename* (RFC 8187)
    // ---------------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("€ rates.txt")
    public void extendedUtf8() throws Exception {
        download("attachment; filename*=UTF-8''%e2%82%ac%20rates.txt");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("£ rates.txt")
    public void extendedIso88591() throws Exception {
        download("attachment; filename*=iso-8859-1'en'%A3%20rates.txt");
    }

    /**
     * The extended parameter wins, whatever the order is.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("größe.txt")
    public void extendedParameterWins() throws Exception {
        download("attachment; filename=\"fallback.txt\"; filename*=UTF-8''gr%C3%B6%C3%9Fe.txt");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("größe.txt")
    public void extendedParameterWinsFirst() throws Exception {
        download("attachment; filename*=UTF-8''gr%C3%B6%C3%9Fe.txt; filename=\"fallback.txt\"");
    }

    /**
     * An unsupported charset is ignored; the plain parameter is the fallback.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "fallback.txt",
            FF = "bar.txt",
            FF_ESR = "bar.txt")
    @HtmlUnitNYI(
            FF = "fallback.txt",
            FF_ESR = "fallback.txt")
    public void extendedUnknownCharsetFallsBack() throws Exception {
        download("attachment; filename*=foo''bar.txt; filename=\"fallback.txt\"");
    }

    /**
     * An invalid percent encoding is ignored; the plain parameter is the fallback.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "abc%zz.txt",
            FF = "fallback.txt",
            FF_ESR = "fallback.txt")
    @HtmlUnitNYI(
            CHROME = "fallback.txt",
            EDGE = "fallback.txt")
    public void extendedInvalidEncodingFallsBack() throws Exception {
        download("attachment; filename*=UTF-8''abc%zz.txt; filename=\"fallback.txt\"");
    }

    /**
     * The directory part is also removed from the extended value.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("_.._evil.txt")
    public void extendedPathTraversal() throws Exception {
        download("attachment; filename*=UTF-8''..%2F..%2Fevil.txt");
    }

    /**
     * Character that is illegal in file names on some platforms.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a_b.txt")
    public void illegalCharColon() throws Exception {
        download("attachment; filename=\"a:b.txt\"");
    }

    /**
     * Character that is illegal in file names on some platforms.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a_b.txt")
    public void illegalCharAsterisk() throws Exception {
        download("attachment; filename=\"a*b.txt\"");
    }

    /**
     * Character that is illegal in file names on some platforms.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a_b.txt")
    public void illegalCharQuestionMark() throws Exception {
        download("attachment; filename=\"a?b.txt\"");
    }

    /**
     * Character that is illegal in file names on some platforms.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a_b.txt")
    public void illegalCharLessThan() throws Exception {
        download("attachment; filename=\"a<b.txt\"");
    }

    /**
     * Character that is illegal in file names on some platforms.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a_b.txt")
    public void illegalCharGreaterThan() throws Exception {
        download("attachment; filename=\"a>b.txt\"");
    }

    /**
     * Character that is illegal in file names on some platforms.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a_b.txt")
    public void illegalCharPipe() throws Exception {
        download("attachment; filename=\"a|b.txt\"");
    }

    /**
     * An escaped quote inside of a quoted value.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a_b.txt")
    public void illegalCharEscapedQuote() throws Exception {
        download("attachment; filename=\"a\\\"b.txt\"");
    }

    /**
     * Illegal characters in the extended parameter.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a_b.txt")
    public void extendedIllegalChar() throws Exception {
        download("attachment; filename*=UTF-8''a%3Ab.txt");
    }
}