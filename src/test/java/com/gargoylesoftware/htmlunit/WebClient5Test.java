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

import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;

/**
 * Tests for {@link WebClient}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author <a href="mailto:bcurren@esomnie.com">Ben Curren</a>
 * @author Marc Guillemot
 * @author David D. Kilzer
 * @author Chris Erskine
 * @author Hans Donner
 * @author Paul King
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Sudhan Moghe
 * @author Ronald Brill
 */
public class WebClient5Test extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void addRequestHeader_Cookie() throws Exception {
        try (WebClient wc = new WebClient()) {
            wc.addRequestHeader(HttpHeader.COOKIE, "some_value");
            fail("Should have thrown an exception ");
        }
        catch (final IllegalArgumentException e) {
            //success
        }
    }

    /**
     * Test that WebClient.getPage(String) calls WebClient.getPage(URL) with the right URL.
     * @throws Exception if the test fails
     */
    @Test
    public void getPageWithStringArg() throws Exception {
        final URL[] calledUrls = {null};
        try (WebClient wc = new WebClient() {
            @Override
            @SuppressWarnings("unchecked")
            public Page getPage(final URL url) throws IOException, FailingHttpStatusCodeException {
                calledUrls[0] = url;
                return null;
            }
        }) {
            wc.getPage(URL_FIRST.toExternalForm());
            assertEquals(URL_FIRST, calledUrls[0]);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void useAfterCloseShouldNotCreateThreads() throws Exception {
        final MockWebConnection connection = getMockWebConnection();
        connection.setDefaultResponse("hello");

        @SuppressWarnings("resource")
        final WebClient webClient = new WebClient();
        webClient.setWebConnection(connection);
        webClient.close();

        webClient.getPage(URL_FIRST);
        assertTrue(getJavaScriptThreads().isEmpty());
    }
}
