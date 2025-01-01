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
package org.htmlunit.fuzzer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.htmlunit.WebClient;
import org.htmlunit.WebTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for issues reported by Google OSS-Fuzz
 * (https://github.com/google/oss-fuzz).
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class FuzzerTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void case54522() throws Exception {
        // https://bugs.chromium.org/p/oss-fuzz/issues/detail?id=54522
        test("test-54522.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void case54523() throws Exception {
        // https://bugs.chromium.org/p/oss-fuzz/issues/detail?id=54523
        test("test-54523.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void case54524() throws Exception {
        // https://bugs.chromium.org/p/oss-fuzz/issues/detail?id=54524
        test("test-54524.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void case54526() throws Exception {
        // https://bugs.chromium.org/p/oss-fuzz/issues/detail?id=54526
        test("test-54526.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void case54527() throws Exception {
        // https://bugs.chromium.org/p/oss-fuzz/issues/detail?id=54527
        test("test-54527.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void case54528() throws Exception {
        // https://bugs.chromium.org/p/oss-fuzz/issues/detail?id=54528
        test("test-54528.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void case54535() throws Exception {
        // https://bugs.chromium.org/p/oss-fuzz/issues/detail?id=54535
        test("test-54535.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void case54613() throws Exception {
        // https://bugs.chromium.org/p/oss-fuzz/issues/detail?id=54613
        test("test-54613.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void case54965() throws Exception {
        // https://bugs.chromium.org/p/oss-fuzz/issues/detail?id=54965
        test("test-54965.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void case55628() throws Exception {
        // https://bugs.chromium.org/p/oss-fuzz/issues/detail?id=55628
        test("test-55628.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void case55747() throws Exception {
        // https://bugs.chromium.org/p/oss-fuzz/issues/detail?id=55747
        test("test-55747.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void case56702() throws Exception {
        // https://bugs.chromium.org/p/oss-fuzz/issues/detail?id=56702
        test("test-56702.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void case58943() throws Exception {
        // https://bugs.chromium.org/p/oss-fuzz/issues/detail?id=58943
        test("test-58943.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void case67067() throws Exception {
        // https://bugs.chromium.org/p/oss-fuzz/issues/detail?id=67067
        test("test-67067.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void case67493() throws Exception {
        // https://bugs.chromium.org/p/oss-fuzz/issues/detail?id=67493
        test("test-67493.html");
    }

    private void test(final String inputFileName) throws Exception {
        final InputStream file = getClass().getClassLoader()
                .getResourceAsStream("org/htmlunit/fuzzer/" + inputFileName);
        final String input = IOUtils.toString(file, StandardCharsets.UTF_8);

        try (WebClient webClient = new WebClient(getBrowserVersion())) {
            // org.htmlunit.corejs.javascript.EvaluatorException seems to be fatal
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            // no exception if linked resources are not available
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);

            webClient.loadHtmlCodeIntoCurrentWindow(input);
        }
        catch (final IllegalArgumentException e) {
        }
        catch (final IOException e) {
        }
    }
}
