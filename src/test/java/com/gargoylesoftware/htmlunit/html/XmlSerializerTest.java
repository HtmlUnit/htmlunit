/*
 * Copyright (c) 2002-2019 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Tests for {@link XmlSerializer}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class XmlSerializerTest extends SimpleWebTestCase {

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
    public void unsupportedProtocol() throws Exception {
        final String html = "<html><head>"
                + "<link rel='alternate' href='android-app://somehost'>\n"
                + "</head></html>";

        final WebClient client = getWebClient();
        final MockWebConnection connection = getMockWebConnection();
        connection.setResponse(URL_FIRST, html);
        client.setWebConnection(connection);

        final HtmlPage page = client.getPage(URL_FIRST);

        final File tmpFolder = tmpFolderProvider_.newFolder("hu");
        final File file = new File(tmpFolder, "hu_XmlSerializerTest_unsupportedProtocol.html");
        page.save(file);
        assertTrue(file.exists());
        assertTrue(file.isFile());
    }

}
