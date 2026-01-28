/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit;

import java.net.URL;

import javax.net.ssl.SSLHandshakeException;

import org.htmlunit.util.WebConnectionWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for insecure SSL.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HttpWebConnectionInsecureSSLTest extends WebServerTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void normal() throws Exception {
        final URL https = new URL("https://localhost:" + PORT2 + "/");
        Assertions.assertThrows(SSLHandshakeException.class,
                () -> loadPage("<div>test</div>", https));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void insecureSSL() throws Exception {
        final WebClient webClient = getWebClient();
        webClient.getOptions().setUseInsecureSSL(true);

        final URL https = new URL("https://localhost:" + PORT2 + "/");
        loadPage("<div>test</div>", https);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void insecureSSL_withWrapper() throws Exception {
        final WebClient webClient = getWebClient();
        webClient.setWebConnection(new WebConnectionWrapper(webClient.getWebConnection()));
        webClient.getOptions().setUseInsecureSSL(true);

        final URL https = new URL("https://localhost:" + PORT2 + "/");
        loadPage("<div>test</div>", https);
    }

    @Override
    public SSLVariant getSSLVariant() {
        return SSLVariant.INSECURE;
    }
}
