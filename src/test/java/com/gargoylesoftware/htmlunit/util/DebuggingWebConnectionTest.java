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
package com.gargoylesoftware.htmlunit.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link DebuggingWebConnection}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class DebuggingWebConnectionTest extends WebTestCase {

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
        final String expected = "{'na me': 'value1', 'key': 'value 2', 'key 2': 'value 3'}";
        assertEquals(expected, DebuggingWebConnection.nameValueListToJsMap(list));
    }

}
