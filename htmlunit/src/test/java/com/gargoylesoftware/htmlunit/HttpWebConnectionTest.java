/*
 * Copyright (c) 2002, 2004 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit;

import junit.framework.TestCase;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;



/**
 * Tests methods in {@link HttpWebConnection} class.
 *
 * @author David D. Kilzer
 * @version $Revision$
 */
public class HttpWebConnectionTest extends TestCase {

    /**
     * Tests {@link HttpWebConnection#getStateForUrl(java.net.URL)} using a url with hostname
     * <code>gargoylesoftware.com</code>.
     */
    public void testGetStateForUrl_gargoylesoftware_com() {
        assertGetStateForUrl("gargoylesoftware.com");
    }


    /**
     * Tests {@link HttpWebConnection#getStateForUrl(java.net.URL)} using a url with hostname
     * <code>www.gargoylesoftware.com</code>.
     */
    public void testGetStateForUrl_www_gargoylesoftware_com() {
        assertGetStateForUrl("www.gargoylesoftware.com");
    }


    /**
     * Tests {@link HttpWebConnection#getStateForUrl(java.net.URL)} using a url with hostname
     * <code>www.gargoylesoftware.com</code>.
     */
    public void testGetStateForUrl_www_sub_gargoylesoftware_com() {
        assertGetStateForUrl("www.sub.gargoylesoftware.com");
    }


    /**
     * Tests {@link HttpWebConnection#getStateForUrl(java.net.URL)} using a url with hostname
     * <code>localhost</code>.
     */
    public void testGetStateForUrl_localhost() {
        assertGetStateForUrl("localhost");
    }


    /**
     * Tests {@link HttpWebConnection#getStateForUrl(java.net.URL)} using a url with hostname
     * <code>GARGOYLESOFTWARE.COM</code>.
     */
    public void testGetStateForUrl_GARGOYLESOFTWARE_COM() {
        assertGetStateForUrl("GARGOYLESOFTWARE.COM");
    }


    /**
     * Tests {@link HttpWebConnection#getStateForUrl(java.net.URL)} using a url with hostname
     * <code>WWW.GARGOYLESOFTWARE.COM</code>.
     */
    public void testGetStateForUrl_WWW_GARGOYLESOFTWARE_COM() {
        assertGetStateForUrl("WWW.GARGOYLESOFTWARE.COM");
    }


    /**
     * Tests {@link HttpWebConnection#getStateForUrl(java.net.URL)} using a url with hostname
     * <code>WWW.SUB.GARGOYLESOFTWARE.COM</code>.
     */
    public void testGetStateForUrl_WWW_SUB_GARGOYLESOFTWARE_COM() {
        assertGetStateForUrl("WWW.SUB.GARGOYLESOFTWARE.COM");
    }


    /**
     * Tests {@link HttpWebConnection#getStateForUrl(java.net.URL)} using a url with hostname
     * <code>LOCALHOST</code>.
     */
    public void testGetStateForUrl_LOCALHOST() {
        assertGetStateForUrl("LOCALHOST");
    }


    /**
     * Tests the {@link HttpWebConnection#getStateForUrl(java.net.URL)} using reflection.
     *
     * @param hostname The hostname of the url to test
     */
    private void assertGetStateForUrl(String hostname) {

        HttpWebConnection connection = new HttpWebConnection(new WebClient());
        try {

            Field httpClients_ = connection.getClass().getDeclaredField("httpClients_");
            httpClients_.setAccessible(true);
            Map map = (Map) httpClients_.get(connection);

            HttpState expectedHttpState = new HttpState();

            HttpClient httpClient = new HttpClient();
            Field httpState = httpClient.getClass().getDeclaredField("state");
            httpState.setAccessible(true);
            httpState.set(httpClient, expectedHttpState);

            map.put("http://" + hostname.toLowerCase(), httpClient);

            URL url = new URL("http://" + hostname + "/context");
            HttpState actualHttpState = connection.getStateForUrl(url);
            assertSame(expectedHttpState, actualHttpState);
        }
        catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

}