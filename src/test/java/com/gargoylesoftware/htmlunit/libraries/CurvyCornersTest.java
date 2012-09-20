/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.libraries;

import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Tests for compatibility with <a href="http://www.curvycorners.net">curvyCorners</a>.
 *
 * @version $Revision$
 * @author Gareth Davis
 */
@RunWith(BrowserRunner.class)
public class CurvyCornersTest extends SimpleWebTestCase {

    private static final String BASE_FILE_PATH = "libraries/curvyCorners/1.2.9-beta/";

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void demo() throws Exception {
        doTest("demo.html");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void demo2() throws Exception {
        doTest("demo2.html");
    }

    private void doTest(final String fileName) throws Exception {
        final URL url = getClass().getClassLoader().getResource(BASE_FILE_PATH + fileName);
        assertNotNull(url);

        final WebClient client = getWebClient();
        client.getPage(url);
    }

}
