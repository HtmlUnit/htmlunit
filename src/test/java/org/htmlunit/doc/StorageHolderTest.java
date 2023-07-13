/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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
package org.htmlunit.doc;

import java.net.URL;
import java.util.Map;

import org.htmlunit.MockWebConnection;
import org.htmlunit.WebClient;
import org.htmlunit.WebServerTestCase;
import org.junit.Test;

/**
 * Tests for the sample code from the documentation to make sure
 * we adapt the docu or do not break the samples.
 *
 * @author Ronald Brill
 */
public class StorageHolderTest extends WebServerTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void sessionStorage() throws Exception {
        final MockWebConnection conn = getMockWebConnection();
        conn.setDefaultResponse("<script>sessionStorage.setItem('myNewKey', 'myNewData');</script>");

        startWebServer(conn);
        final URL url = URL_FIRST;

        try (WebClient webClient = new WebClient()) {

            // get the session storage for the current window
            final Map<String, String> sessionStorage =
                        webClient.getStorageHolder().getSessionStorage(webClient.getCurrentWindow());

            // place some data in the session storage
            sessionStorage.put("myKey", "myData");

            // load the page that consumes the session storage data
            webClient.getPage(url);

            // make sure the new data are in
            assertEquals("myNewData", sessionStorage.get("myNewKey"));
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void localStorage() throws Exception {
        final MockWebConnection conn = getMockWebConnection();
        conn.setDefaultResponse("<script>localStorage.setItem('myNewKey', 'myNewData');</script>");

        startWebServer(conn);
        final URL url = URL_FIRST;

        try (WebClient webClient = new WebClient()) {

            // get the local storage for the url
            // the url has to match the page url you will load later
            final Map<String, String> localStorage = webClient.getStorageHolder().getLocalStorage(url);

            // place some data in the session storage
            localStorage.put("myKey", "myData");

            // load the page that consumes the session storage data
            webClient.getPage(url);

            // make sure the new data are in
            assertEquals("myNewData", localStorage.get("myNewKey"));
        }
    }
}
