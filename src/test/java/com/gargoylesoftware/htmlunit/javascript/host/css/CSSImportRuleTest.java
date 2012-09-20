/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link CSSImportRule}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class CSSImportRuleTest extends WebDriverTestCase {

    /**
     * Regression test for bug 2658249.
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(FF)
    public void testGetImportFromCssRulesCollection() throws Exception {
        // with absolute URL
        testGetImportFromCssRulesCollection(getDefaultUrl(), URL_SECOND.toExternalForm(), URL_SECOND);

        // with relative URL
        final URL urlPage = new URL(URL_FIRST, "/dir1/dir2/foo.html");
        final URL urlCss = new URL(URL_FIRST, "/dir1/dir2/foo.css");
        testGetImportFromCssRulesCollection(urlPage, "foo.css", urlCss);
    }

    private void testGetImportFromCssRulesCollection(final URL pageUrl, final String cssRef, final URL cssUrl)
        throws Exception {
        final String html
            = "<html><body>\n"
            + "<style>@import url('" + cssRef + "');</style><div id='d'>foo</div>\n"
            + "<script>\n"
            + "var r = document.styleSheets.item(0).cssRules[0];\n"
            + "alert(r);\n"
            + "alert(r.href);\n"
            + "alert(r.media);\n"
            + "alert(r.media.length);\n"
            + "alert(r.styleSheet);\n"
            + "</script>\n"
            + "</body></html>";
        final String css = "#d { color: green }";

        getMockWebConnection().setResponse(cssUrl, css, "text/css");

        setExpectedAlerts("[object CSSImportRule]", cssRef,
            "[object MediaList]", "0", "[object CSSStyleSheet]");
        loadPageWithAlerts2(html, pageUrl);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void testImportedStylesheetsLoaded() throws Exception {
        final String html
            = "<html><body>\n"
            + "<style>@import url('" + URL_SECOND + "');</style>\n"
            + "<div id='d'>foo</div>\n"
            + "<script>\n"
            + "var d = document.getElementById('d');\n"
            + "var s = window.getComputedStyle ? window.getComputedStyle(d,null) : d.currentStyle;\n"
            + "alert(s.color.indexOf('128') > 0);\n"
            + "</script>\n"
            + "</body></html>";
        final String css = "#d { color: rgb(0, 128, 0); }";

        getMockWebConnection().setResponse(URL_SECOND, css, "text/css");

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void testImportedStylesheetsURLResolution() throws Exception {
        final String html = "<html><head>\n"
            + "<link rel='stylesheet' type='text/css' href='dir1/dir2/file1.css'></link>\n"
            + "<body>\n"
            + "<div id='d'>foo</div>\n"
            + "<script>\n"
            + "var d = document.getElementById('d');\n"
            + "var s = window.getComputedStyle ? window.getComputedStyle(d, null) : d.currentStyle;\n"
            + "alert(s.color.indexOf('128') > 0);\n"
            + "</script>\n"
            + "</body></html>";
        final String css1 = "@import url('file2.css');";
        final String css2 = "#d { color: rgb(0, 128, 0); }";

        final URL urlPage = URL_FIRST;
        final URL urlCss1 = new URL(urlPage, "dir1/dir2/file1.css");
        final URL urlCss2 = new URL(urlPage, "dir1/dir2/file2.css");
        getMockWebConnection().setResponse(urlCss1, css1, "text/css");
        getMockWebConnection().setResponse(urlCss2, css2, "text/css");

        loadPageWithAlerts2(html, urlPage);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void testCircularImportedStylesheets() throws Exception {
        final String html = "<html><head>\n"
            + "<link rel='stylesheet' type='text/css' href='dir1/dir2/file1.css'></link>\n"
            + "<body>\n"
            + "<div id='d'>foo</div>\n"
            + "<script>\n"
            + "var d = document.getElementById('d');\n"
            + "var s = window.getComputedStyle ? window.getComputedStyle(d, null) : d.currentStyle;\n"
            + "alert(s.color.indexOf('128') > 0);\n"
            + "</script>\n"
            + "</body></html>";
        final String css1 = "@import url('file2.css');";
        final String css2 = "@import url('file1.css');\n"
            + "#d { color: rgb(0, 128, 0); }";

        final URL urlPage = URL_FIRST;
        final URL urlCss1 = new URL(urlPage, "dir1/dir2/file1.css");
        final URL urlCss2 = new URL(urlPage, "dir1/dir2/file2.css");
        getMockWebConnection().setResponse(urlCss1, css1, "text/css");
        getMockWebConnection().setResponse(urlCss2, css2, "text/css");

        loadPageWithAlerts2(html, urlPage);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "true", "true", "true" })
    public void testCircularImportedStylesheetsComplexCase() throws Exception {
        final String html = "<html><head>\n"
            + "<link rel='stylesheet' type='text/css' href='dir1/dir2/file1.css'></link>\n"
            + "<body>\n"
            + "<div id='d'>foo</div>\n"
            + "<div id='e'>foo</div>\n"
            + "<div id='f'>foo</div>\n"
            + "<script>\n"
            + "var d = document.getElementById('d');\n"
            + "var s = window.getComputedStyle ? window.getComputedStyle(d, null) : d.currentStyle;\n"
            + "alert(s.color.indexOf('128') > 0);\n"
            + "var e = document.getElementById('e');\n"
            + "s = window.getComputedStyle ? window.getComputedStyle(e, null) : e.currentStyle;\n"
            + "alert(s.color.indexOf('127') > 0);\n"
            + "var f = document.getElementById('f');\n"
            + "s = window.getComputedStyle ? window.getComputedStyle(f, null) : f.currentStyle;\n"
            + "alert(s.color.indexOf('126') > 0);\n"
            + "</script>\n"
            + "</body></html>";
        final String css1 = "@import url('file2.css');";
        final String css2 = "@import url('file3.css');\n"
            + "@import url('file4.css');";
        final String css3 = "#d { color: rgb(0, 128, 0); }";
        final String css4 = "@import url('file5.css');\n"
            + "#e { color: rgb(0, 127, 0); }";
        final String css5 = "@import url('file2.css');\n"
            + "#f { color: rgb(0, 126, 0); }";

        final URL urlPage = URL_FIRST;
        final URL urlCss1 = new URL(urlPage, "dir1/dir2/file1.css");
        final URL urlCss2 = new URL(urlPage, "dir1/dir2/file2.css");
        final URL urlCss3 = new URL(urlPage, "dir1/dir2/file3.css");
        final URL urlCss4 = new URL(urlPage, "dir1/dir2/file4.css");
        final URL urlCss5 = new URL(urlPage, "dir1/dir2/file5.css");
        getMockWebConnection().setResponse(urlCss1, css1, "text/css");
        getMockWebConnection().setResponse(urlCss2, css2, "text/css");
        getMockWebConnection().setResponse(urlCss3, css3, "text/css");
        getMockWebConnection().setResponse(urlCss4, css4, "text/css");
        getMockWebConnection().setResponse(urlCss5, css5, "text/css");

        loadPageWithAlerts2(html, urlPage);
    }
}
