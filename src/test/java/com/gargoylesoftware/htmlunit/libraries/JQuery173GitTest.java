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

import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebServerTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for compatibility with web server loading of
 * version 1.7.3 of the <a href="http://jquery.com/">jQuery</a> JavaScript library.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class JQuery173GitTest extends WebServerTestCase {

    /**
     * Returns the jQuery version being tested.
     * @return the jQuery version being tested
     */
    protected static String getVersion() {
        return "1.7.3-git";
    }

    /**
     * {@inheritDoc}
     */
    protected String getExpectedPath() throws Exception {
        final String resource = "libraries/jquery/" + getVersion() + "/" + getBrowserVersion().getNickname() + ".txt";
        final URL url = getClass().getClassLoader().getResource(resource);
        return url.toURI().getPath();
    }

    private static final int TESTS_TO_RUN_ = 50;

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented
    public void test() throws Exception {
        final List<String> lines = FileUtils.readLines(new File(getExpectedPath()), "UTF-8");

        startWebServer("src/test/resources/libraries/jquery/" + getVersion(), null);
        final WebClient webClient = getWebClient();
        final HtmlPage page = webClient.getPage("http://localhost:" + PORT + "/test/index.html");
        HtmlElement element = null;
        final String xpath = "//li[@id='qunit-test-output" + (TESTS_TO_RUN_ - 1) + "']";
        boolean allFound;
        do {
            if (element == null) {
                final List<?> list = page.getByXPath(xpath);
                if (!list.isEmpty()) {
                    element = (HtmlElement) list.get(0);
                }
            }
            Thread.sleep(10 * 1000);
            allFound = true;
            for (int i = 0; i < TESTS_TO_RUN_; i++) {
                try {
                    if (!((HtmlElement) page.getByXPath("//li[@id='qunit-test-output" + i + "']").get(0)).asText()
                            .contains(",")) {
                        allFound = false;
                        break;
                    }
                }
                catch (final Exception e) {
                    allFound = false;
                }
            }
        } while (!allFound);

        final List<String> failures = new ArrayList<String>();

        for (int i = 0; i < TESTS_TO_RUN_; i++) {
            String expected = lines.get(i);
            expected = expected.substring(expected.indexOf('.') + 1);
            if (expected.charAt(0) == ' ') {
                expected = expected.substring(1);
            }

            String result = ((HtmlElement) page.getByXPath("//li[@id='qunit-test-output" + i + "']").get(0)).asText();
            result = result.substring(0, result.indexOf("Rerun")).trim();
            if (!result.equals(expected)) {
                failures.add(new ComparisonFailure("", expected, result).getMessage());
            }
        }

        final StringBuilder sb = new StringBuilder();
        for (final String error : failures) {
            sb.append('\n').append(error);
        }

        final int errorsNumber = failures.size();
        if (errorsNumber == 1) {
            fail("Failure: " + sb);
        }
        else if (errorsNumber > 1) {
            fail(errorsNumber + " failures: " + sb);
        }
    }

}
