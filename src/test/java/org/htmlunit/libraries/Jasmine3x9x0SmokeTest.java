/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.libraries;

import org.eclipse.jetty.server.Server;
import org.htmlunit.WebClient;
import org.htmlunit.WebServerTestCase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * A simple smoke test for jasmine 3.9.0.
 * See issue #1001 for more.
 *
 * @author Ronald Brill
 */
public class Jasmine3x9x0SmokeTest extends WebServerTestCase {

    /** The server. */
    protected static Server SERVER_;

    /**
     * @throws Exception if an error occurs
     */
    @BeforeAll
    public static void startSesrver() throws Exception {
        SERVER_ = WebServerTestCase.createWebServer("src/test/resources/libraries/jasmine/jasmine_3.9.0", null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @AfterAll
    public static void stopServer() throws Exception {
        if (SERVER_ != null) {
            SERVER_.stop();
            SERVER_.destroy();
            SERVER_ = null;
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void shouldTestJavaScript() throws Exception {
        final WebClient webClient = getWebClient();

        webClient.getPage(URL_FIRST + "smoketest.html");
    }
}
