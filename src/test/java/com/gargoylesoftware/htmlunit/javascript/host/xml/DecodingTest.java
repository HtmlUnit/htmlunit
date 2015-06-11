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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Properties;

import org.junit.Test;

/**
 * Tests decoding difference between local machine and build server.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class DecodingTest {

    /**
     * Helper method Bug #1623.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void test() throws Exception {
        System.out.println("--------------");
        final String string = "'\u9ec4'";
        final Charset charset = Charset.forName("GBK");
        final InputStream is = new ByteArrayInputStream(string.getBytes("UTF-8"));
        final StreamDecoder decoder = StreamDecoder.forInputStreamReader(is, this, charset);
        int i;
        while((i = decoder.read()) != -1) {
            System.out.println("test result: " + i);
        }
        System.out.println("--------------");
        final Properties systemProperties = System.getProperties();
        for (final Object key : systemProperties.keySet()) {
            //System.out.println("" + key + '=' + systemProperties.getProperty((String) key));
        }
    }

    /**
     * Helper method Bug #1623.
     *
     * @throws Exception if the test fails
     */
    //@Test
    public void test2() throws Exception {
        System.out.println("--------------");
        final String string = "'\u9ec4'";
        final Charset charset = Charset.forName("GBK");
        CharsetDecoder decoder = charset.newDecoder();
        System.out.println(decoder.getClass().getName());
        byte[] bytes = string.getBytes("UTF-8");
        System.out.println("-------Original-------");
        for (byte b: bytes) {
            System.out.println("Byte " + (int) b);
        }
        
        for (int i = 1; i <= bytes.length; i++) {
            for (int offset = 0; offset < bytes.length; offset++) {
                process(decoder, bytes, offset, i);
            }
        }
        bytes = increase(bytes, 6);

        System.out.println("-------Extended-------");
        for (byte b: bytes) {
            System.out.println("Byte " + (int) b);
        }
        
        for (int i = 1; i <= bytes.length; i++) {
            for (int offset = 0; offset < bytes.length; offset++) {
                process(decoder, bytes, offset, i);
            }
        }
    }

    private void process(CharsetDecoder decoder, byte[] bytes, int offset, int length) {
        try {
          CharBuffer buff = decoder.decode(ByteBuffer.wrap(bytes, offset, length));
          System.out.println("----------" + bytes.length + ", offset " + offset + ", length " + length);
          for (char ch : buff.array()) {
              System.out.println("Char " + (int) ch);
          }
        }
        catch(Exception e) {
            //System.out.println("Exception");
        }
        
    }
    private byte[] increase(byte[] bytes, int size) {
        byte[] newBytes = new byte[size];
        System.arraycopy(bytes, 0, newBytes, 0, Math.min(bytes.length, size));
        return newBytes;
    }
}
