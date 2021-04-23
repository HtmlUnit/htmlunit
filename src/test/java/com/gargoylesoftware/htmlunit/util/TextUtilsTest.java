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
package com.gargoylesoftware.htmlunit.util;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for {@link TextUtils}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class TextUtilsTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void toInputStream_null() throws Exception {
        try {
            TextUtils.toInputStream(null);
            fail("Expected NullPointerException");
        }
        catch (final NullPointerException e) {
            // Expected path
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void toInputStream() throws Exception {
        final String[][] data = {
            {"", null},
            {"a", "a"},
            {"abcdefABCDEF", "abcdefABCDEF"},
        };
        final Charset encoding = ISO_8859_1;

        for (final String[] entry : data) {
            final String input = entry[0];
            final String expectedResult = entry[1];

            try (InputStream inputStream = TextUtils.toInputStream(input, encoding)) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, encoding))) {
                    assertEquals(expectedResult, reader.readLine());
                }
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void stringToByteArray() throws Exception {
        byte[] result = TextUtils.stringToByteArray(null, UTF_8);
        assertEquals(0, result.length);

        result = TextUtils.stringToByteArray("", UTF_8);
        assertEquals(0, result.length);

        result = TextUtils.stringToByteArray("htmlunit", UTF_8);
        assertEquals(8, result.length);
        assertEquals(104, result[0]);

        result = TextUtils.stringToByteArray("htmlunit", null);
        assertEquals(0, result.length);
    }

}
