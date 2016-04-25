/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.arrays;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

/**
 * Tests for {@link ArrayBuffer}.
 *
 * @author Ahmed Ashour
 */
public class ArrayBuffer2Test {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void outOfRange() throws Exception {
        final ArrayBuffer arrayBuffer = new ArrayBuffer();
        arrayBuffer.constructor(5);

        arrayBuffer.setBytes(0, new byte[] {10, 11, 12, 13, 14});
        assertArrayEquals(new byte[] {10, 11, 12, 13, 14}, arrayBuffer.getBytes());

        arrayBuffer.setBytes(3, new byte[] {100, 101, 102, 103});
        assertArrayEquals(new byte[] {10, 11, 12, 100, 101}, arrayBuffer.getBytes());

        arrayBuffer.setBytes(4, new byte[] {40, 41, 42, 43});
        assertArrayEquals(new byte[] {10, 11, 12, 100, 40}, arrayBuffer.getBytes());

        arrayBuffer.setBytes(5, new byte[] {50, 51, 52, 53});
        assertArrayEquals(new byte[] {10, 11, 12, 100, 40}, arrayBuffer.getBytes());
    }
}
