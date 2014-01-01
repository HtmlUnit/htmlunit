/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import javax.net.ssl.SSLHandshakeException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.util.WebConnectionWrapper;

/**
 * Tests for insecure SSL.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HttpWebConnectionInsecureSSLTest extends SimpleWebTestCase {

    private InsecureHttpsServer localServer_;

    /**
     * @throws Exception if an error occurs
     */
    @Before
    public void setUp() throws Exception {
        localServer_ = new InsecureHttpsServer();
        localServer_.start();
    }

    /**
     * @throws Exception if an error occurs
     */
    @After
    public void tearDown() throws Exception {
        if (localServer_ != null) {
            localServer_.stop();
        }
        localServer_ = null;
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test(expected = SSLHandshakeException.class)
    public void normal() throws Exception {
        final WebClient webClient = getWebClient();
        webClient.getPage("https://" + localServer_.getHostName()
                + ':' + localServer_.getPort()
                + "/random/100");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void insecureSSL() throws Exception {
        final WebClient webClient = getWebClient();
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getPage("https://" + localServer_.getHostName()
                + ':' + localServer_.getPort()
                + "/random/100");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void insecureSSL_withWrapper() throws Exception {
        final WebClient webClient = getWebClient();
        webClient.setWebConnection(new WebConnectionWrapper(webClient.getWebConnection()));
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getPage("https://" + localServer_.getHostName()
                + ':' + localServer_.getPort()
                + "/random/100");
    }
}
