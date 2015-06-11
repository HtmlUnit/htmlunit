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
import java.nio.charset.Charset;
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
        System.out.println(charset.getClass().getName());
        System.out.println(charset.getClass().getProtectionDomain().getCodeSource());
        final InputStream is = new ByteArrayInputStream(string.getBytes("UTF-8"));
        final sun.nio.cs.StreamDecoder decoder = sun.nio.cs.StreamDecoder.forInputStreamReader(is, this, charset);
        int i;
        while((i = decoder.read()) != -1) {
            System.out.println(i);
        }
        System.out.println("--------------");
        final Properties systemProperties = System.getProperties();
        for (final Object key : systemProperties.keySet()) {
            System.out.println("" + key + '=' + systemProperties.getProperty((String) key));
        }
    }
}
