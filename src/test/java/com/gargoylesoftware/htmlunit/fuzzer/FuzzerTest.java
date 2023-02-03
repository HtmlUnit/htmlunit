/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.fuzzer;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;

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
        // https://bugs.chromium.org/p/oss-fuzz/issues/detail?id=54965
        test("test-55628.html");
    }

    private void test(final String inputFileName) throws Exception {
        final InputStream file = getClass().getClassLoader()
                .getResourceAsStream("com/gargoylesoftware/htmlunit/fuzzer/" + inputFileName);
        final String input = IOUtils.toString(file, StandardCharsets.UTF_8);

        try (WebClient webClient = new WebClient(getBrowserVersion())) {
            webClient.loadHtmlCodeIntoCurrentWindow(input);
        }
    }
}
