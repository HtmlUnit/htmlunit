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
package com.gargoylesoftware.htmlunit.libraries;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebServerTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Base class for tests for compatibility with <a href="http://prototype.conio.net/">Prototype</a>.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public abstract class PrototypeTestBase extends WebServerTestCase {

    private static final Log LOG = LogFactory.getLog(PrototypeTestBase.class);

    private WebClient webClient_;

    /**
     * Gets the prototype tested version.
     * @return the version
     */
    protected abstract String getVersion();

    /**
     * Runs the specified test.
     * @param filename the test file to run
     * @throws Exception if the test fails
     */
    protected void test(final String filename) throws Exception {
        webClient_ = getWebClient();
        final HtmlPage page =
            webClient_.getPage("http://localhost:" + PORT + "/test/unit/" + filename);

        webClient_.waitForBackgroundJavaScript(25000);

        String expected = getExpectations(getBrowserVersion(), filename);
        final HtmlElement testlog = page.getHtmlElementById("testlog");
        String actual = testlog.asText();

        // ignore Info lines
        expected = expected.replaceAll("Info:.*", "Info: -- skipped for comparison --");
        actual = actual.replaceAll("Info:.*", "Info: -- skipped for comparison --");

        // normalize line break
        expected = expected.replaceAll("\r\n", "\n");
        actual = actual.replaceAll("\r\n", "\n");

        // dump the result page if not ok
        if (System.getProperty(PROPERTY_GENERATE_TESTPAGES) != null && !expected.equals(actual)) {
            final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
            final File f = new File(tmpDir, "prototype" + getVersion() + "_result_" + filename);
            FileUtils.writeStringToFile(f, page.asXml(), "UTF-8");
            LOG.info("Test result for " + filename + " written to: " + f.getAbsolutePath());
        }

        assertEquals(expected, actual);
    }

    private String getExpectations(final BrowserVersion browserVersion, final String filename)
        throws IOException {
        final String fileNameBase = StringUtils.substringBeforeLast(filename, ".");
        final String baseName = "src/test/resources/libraries/prototype/" + getVersion() + "/expected." + fileNameBase;

        File expectationsFile = null;
        // version specific to this browser (or browser group)?
        String browserSuffix = "." + browserVersion.getNickname();
        while (browserSuffix.length() > 0) {
            final File file = new File(baseName + browserSuffix + ".txt");
            if (file.exists()) {
                expectationsFile = file;
                break;
            }
            browserSuffix = browserSuffix.substring(0, browserSuffix.length() - 1);
        }

        // generic version
        if (expectationsFile == null) {
            expectationsFile = new File(baseName + ".txt");
            if (!expectationsFile.exists()) {
                throw new FileNotFoundException("Can't find expectations file for test " + filename
                        + "(" + browserVersion.getNickname() + ")");
            }
        }

        return FileUtils.readFileToString(expectationsFile, "UTF-8");
    }

    /**
     * Performs pre-test initialization.
     * @throws Exception if an error occurs
     */
    @Before
    public void setUp() throws Exception {
        startWebServer("src/test/resources/libraries/prototype/" + getVersion());
    }

    /**
     * Performs post-test deconstruction.
     * Ensures everything stops in the WebClient.
     * @throws Exception if an error occurs
     */
    @After
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        webClient_.closeAllWindows();
    }
}
