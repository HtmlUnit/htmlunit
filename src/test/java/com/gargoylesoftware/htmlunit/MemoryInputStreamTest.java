/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * Tests for {@link MemoryInputStream}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class MemoryInputStreamTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void test() throws Exception {
        test(1024, 5);
        test(1023, 1023);
        test(1023, 1024);
        test(1023, 1025);
        test(1024, 1023);
        test(1024, 1024);
        test(1024, 1025);
        test(1025, 1023);
        test(1025, 1024);
        test(1025, 1025);
    }

    private void test(final int bufferSize, final int maximumSize) throws Exception {
        final byte[] buff = new byte[bufferSize];
        final Random random = new Random();
        for (int i = 0; i < bufferSize; i++) {
            buff[i] = (byte) random.nextInt(0xFF);
        }
        final MemoryInputStream in = new MemoryInputStream(new ByteArrayInputStream(buff), maximumSize);
        assertTrue(Arrays.equals(buff, IOUtils.toByteArray(in)));
        assertEquals(bufferSize < maximumSize, in.isInMemory());
    }
}
