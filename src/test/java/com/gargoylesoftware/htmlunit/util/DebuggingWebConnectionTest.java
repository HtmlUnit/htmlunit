/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.util;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * Tests for {@link DebuggingWebConnection}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class DebuggingWebConnectionTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void nameValueListToJsMap() throws Exception {
        assertEquals("{}", DebuggingWebConnection.nameValueListToJsMap(null));
        final List<NameValuePair> emptyList = Collections.emptyList();
        assertEquals("{}", DebuggingWebConnection.nameValueListToJsMap(emptyList));

        List<NameValuePair> list = Collections.singletonList(new NameValuePair("name", "value"));
        assertEquals("{'name': 'value'}", DebuggingWebConnection.nameValueListToJsMap(list));

        list = Collections.singletonList(new NameValuePair("na me", "value"));
        assertEquals("{'na me': 'value'}", DebuggingWebConnection.nameValueListToJsMap(list));

        list = new ArrayList<NameValuePair>();
        list.add(new NameValuePair("na me", "value1"));
        list.add(new NameValuePair("key", "value 2"));
        list.add(new NameValuePair("key 2", "value 3"));
        list.add(new NameValuePair("key 4", "with ' quote")); // can it really happen in header?
        final String expected = "{'na me': 'value1', 'key': 'value 2', 'key 2': 'value 3', 'key 4': 'with \\' quote'}";
        assertEquals(expected, DebuggingWebConnection.nameValueListToJsMap(list));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void escapeJSString() throws Exception {
        assertEquals("", DebuggingWebConnection.escapeJSString(""));
        assertEquals("hello", DebuggingWebConnection.escapeJSString("hello"));
        assertEquals("I\\'m here", DebuggingWebConnection.escapeJSString("I'm here"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void chooseExtension() throws Exception {
        assertEquals(".html", DebuggingWebConnection.chooseExtension("text/html"));

        assertEquals(".js", DebuggingWebConnection.chooseExtension("text/javascript"));

        assertEquals(".css", DebuggingWebConnection.chooseExtension("text/css"));

        assertEquals(".xml", DebuggingWebConnection.chooseExtension("text/xml"));

        assertEquals(".txt", DebuggingWebConnection.chooseExtension("text/plain"));
    }

    /**
     * Ensures that Content-Encoding headers are removed when JavaScript is uncompressed.
     * (was causing java.io.IOException: Not in GZIP format as of HtmlUnit-2.10).
     * @throws Exception if the test fails
     */
    @Test
    public void gzip() throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(baos);
        IOUtils.write("alert(1)", gzipOutputStream, "UTF-8");
        gzipOutputStream.close();

        final MockWebConnection mockConnection = new MockWebConnection();
        final List<NameValuePair> responseHeaders = Arrays.asList(
            new NameValuePair("Content-Encoding", "gzip"));
        mockConnection.setResponse(getDefaultUrl(), baos.toByteArray(), 200, "OK", "application/javascript",
            responseHeaders);

        final String dirName = "test-" + getClass().getSimpleName();
        final DebuggingWebConnection dwc = new DebuggingWebConnection(mockConnection, dirName);

        final WebRequest request = new WebRequest(getDefaultUrl());
        final WebResponse response = dwc.getResponse(request); // was throwing here
        assertNull(response.getResponseHeaderValue("Content-Encoding"));

        FileUtils.deleteDirectory(dwc.getReportFolder());
    }
}
