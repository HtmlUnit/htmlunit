/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;

import org.junit.Test;

/**
 * Tests decoding difference between local machine and build server.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class DecodingTest {

//    /**
//     * Helper method Bug #1623.
//     *
//     * @throws Exception if the test fails
//     */
//    @Test
//    public void test() throws Exception {
//        System.out.println("test()");
//        final String string = "'\u9ec4'";
//        final Charset charset = new GBK();
//        System.out.println(charset.getClass().getName());
//        CharsetDecoder decoder = charset.newDecoder().onMalformedInput(CodingErrorAction.REPLACE)
//                .onUnmappableCharacter(CodingErrorAction.REPLACE);
//        byte[] bytes = string.getBytes("UTF-8");
//        final InputStream in = new ByteArrayInputStream(bytes);
//        ByteBuffer bb = ByteBuffer.allocate(8192);
//        bb.flip();
//
//        CharBuffer cb = CharBuffer.wrap(new char[2]);
//        System.out.println("01 - " + bb.position() + " " + bb.limit());
//        System.out.println("02 - " + cb.position() + " " + cb.limit());
//        decoder.decode(bb, cb, false);
//        System.out.println("03 - " + bb.position() + " " + bb.limit());
//        System.out.println("04 - " + cb.position() + " " + cb.limit());
//
//        bb.compact();
//        
//        in.read(bb.array(), 0, 8192);
//        bb.position(5);
//        bb.flip();
//
//        System.out.println("05 - " + bb.position() + " " + bb.limit());
//        System.out.println("06 - " + cb.position() + " " + cb.limit());
//        decoder.decode(bb, cb, false);
//        System.out.println("07 - " + bb.position() + " " + bb.limit());
//        System.out.println("08 - " + cb.position() + " " + cb.limit());
//
//        cb = CharBuffer.wrap(new char[2]);
//        System.out.println("09 - " + bb.position() + " " + bb.limit());
//        System.out.println("10 - " + cb.position() + " " + cb.limit());
//        decoder.decode(bb, cb, false);
//        System.out.println("11 - " + bb.position() + " " + bb.limit());
//        System.out.println("12 - " + cb.position() + " " + cb.limit());
//    }
//
//    /**
//     * Helper method Bug #1623.
//     *
//     * @throws Exception if the test fails
//     */
//    @Test
//    public void test2() throws Exception {
//        System.out.println("test2()");
//        final String string = "'\u9ec4'";
//        final Charset charset = Charset.forName("GBK");
//        System.out.println(charset.getClass().getName());
//        CharsetDecoder decoder = charset.newDecoder().onMalformedInput(CodingErrorAction.REPLACE)
//                .onUnmappableCharacter(CodingErrorAction.REPLACE);
//        byte[] bytes = string.getBytes("UTF-8");
//        final InputStream in = new ByteArrayInputStream(bytes);
//        ByteBuffer bb = ByteBuffer.allocate(8192);
//        bb.flip();
//
//        CharBuffer cb = CharBuffer.wrap(new char[2]);
//        System.out.println("01 - " + bb.position() + " " + bb.limit());
//        System.out.println("02 - " + cb.position() + " " + cb.limit());
//        decoder.decode(bb, cb, false);
//        System.out.println("03 - " + bb.position() + " " + bb.limit());
//        System.out.println("04 - " + cb.position() + " " + cb.limit());
//
//        bb.compact();
//        
//        in.read(bb.array(), 0, 8192);
//        bb.position(5);
//        bb.flip();
//
//        System.out.println("05 - " + bb.position() + " " + bb.limit());
//        System.out.println("06 - " + cb.position() + " " + cb.limit());
//        decoder.decode(bb, cb, false);
//        System.out.println("07 - " + bb.position() + " " + bb.limit());
//        System.out.println("08 - " + cb.position() + " " + cb.limit());
//
//        cb = CharBuffer.wrap(new char[2]);
//        System.out.println("09 - " + bb.position() + " " + bb.limit());
//        System.out.println("10 - " + cb.position() + " " + cb.limit());
//        decoder.decode(bb, cb, false);
//        System.out.println("11 - " + bb.position() + " " + bb.limit());
//        System.out.println("12 - " + cb.position() + " " + cb.limit());
//    }

//    public void test3() throws Exception {
//        log(new GBK());
//        System.out.println("JDK GBK");
//        log(Charset.forName("GBK"));
//    }

    private void log(Charset charset) throws Exception {
        charset.newDecoder();
        Field field = charset.getClass().getDeclaredField("b2c");
        field.setAccessible(true);
        char[][] b2c = (char[][]) field.get(null);
        for (int i = 0; i < b2c.length; i++) {
            char[] list = b2c[i];
            for (int x = 0; x < list.length; x++) {
                System.out.println("x-" + i + "-" + x + " " + (int) list[x]);
            }
        }

        field = charset.getClass().getDeclaredField("b2cSB");
        field.setAccessible(true);
        char[] b2cSB = (char[]) field.get(null);
        for (int i = 0; i < b2cSB.length; i++) {
            System.out.println("y-" + i + " " + (int) b2cSB[i]);
        }
    }

    protected Object getField(final Object obj, final String name) {
        for (Class<?> c = obj.getClass(); c != null; c = c.getSuperclass()) {
            try {
                final Field field = c.getDeclaredField(name);
                field.setAccessible(true);
                return field.get(obj);
            } catch (final Exception e) {
                ;
            }
        }
        return null;
    }

    @Test
    public void test4() {
        Charset charset1 = new GBK();
        CharsetDecoder decoder1 = charset1.newDecoder();
        
        Charset charset2 = Charset.forName("GBK");
        CharsetDecoder decoder2 = charset2.newDecoder();

        System.out.println(getField(decoder2, "b2Min"));
        assertEquals(getField(decoder1, "b2Min"), getField(decoder2, "b2Min"));
        System.out.println(getField(decoder2, "b2Max"));
        assertEquals(getField(decoder1, "b2Max"), getField(decoder2, "b2Max"));
        assertArrayEquals((char[]) getField(decoder1, "b2cSB"), (char[]) getField(decoder2, "b2cSB"));
        assertArrayEquals((char[][]) getField(decoder1, "b2c"), (char[][]) getField(decoder2, "b2c"));
        System.out.println(getField(decoder2, "replacement").toString().length());
        System.out.println(getField(decoder2, "replacement").toString().codePointAt(0));
        assertEquals(getField(decoder1, "replacement"), getField(decoder2, "replacement"));
        System.out.println(getField(decoder2, "averageCharsPerByte"));
        assertEquals(getField(decoder1, "averageCharsPerByte"), getField(decoder2, "averageCharsPerByte"));
        System.out.println(getField(decoder2, "maxCharsPerByte"));
        assertEquals(getField(decoder1, "maxCharsPerByte"), getField(decoder2, "maxCharsPerByte"));
        
    }
}
