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
package com.gargoylesoftware.htmlunit;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

/**
 * Tests for text content.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class TextPageTest extends WebServerTestCase {

    /**
     * Utility for temporary folders.
     * Has to be public due to JUnit's constraints for @Rule.
     */
    @Rule
    public final TemporaryFolder tmpFolderProvider_ = new TemporaryFolder();

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void save() throws Exception {
        final String response = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/plain\r\n"
                + "\r\n"
                + "HtmlUnit Text Response";

        try (PrimitiveWebServer primitiveWebServer = new PrimitiveWebServer(null, response, null)) {
            final WebClient client = getWebClient();

            final TextPage page = client.getPage("http://localhost:" + primitiveWebServer.getPort() + "/" + "text");

            final File tmpFolder = tmpFolderProvider_.newFolder("hu");
            final File file = new File(tmpFolder, "hu_txt.plain");
            page.save(file);
            assertTrue(file.exists());
            assertTrue(file.isFile());

            assertEquals("HtmlUnit Text Response", FileUtils.readFileToString(file, StandardCharsets.UTF_8));
        }
    }
}
