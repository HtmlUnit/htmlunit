/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.gargoylesoftware.base.testing.AcceptAllTestFilter;
import com.gargoylesoftware.base.testing.RecursiveTestSuite;

/**
 * This class is used by the junitui and junit targets in the build file.  It's
 * only purpose is to initialize the RecursiveTestSuite properly and to
 * ensure that all logging is turned up to its highest level.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class MainTestSuite extends RecursiveTestSuite {

    /**
     * Create an instance that will start from the current directory and
     * will include all tests.
     *
     * @param name Ignored field.  The swing test runner expects to find
     * a test with constructor that takes a string so we provide one here.
     * We don't actually use this field.
     * @exception IOException If an io error occurs.
     */
    public MainTestSuite( final String name ) throws IOException {
        super( new File("."), new AcceptAllTestFilter() );

        //enableAllLogging();
    }


    /**
     * Set the appropriate logging levels for running the tests.
     */
    public void enableAllLogging() {
        final Properties properties = System.getProperties();
        properties.put("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        properties.put("org.apache.commons.logging.simplelog.defaultlog", "trace");

        final String prefix = "org.apache.commons.logging.simplelog.log.";
        properties.put(prefix+"org.apache.commons.httpclient.Authenticator", "info");
        properties.put(prefix+"org.apache.commons.httpclient.Cookie", "info");
        properties.put(prefix+"org.apache.commons.httpclient.HeaderElement", "info");
        properties.put(prefix+"org.apache.commons.httpclient.HttpClient", "info");
        properties.put(prefix+"org.apache.commons.httpclient.HttpConnection", "info");
        properties.put(prefix+"org.apache.commons.httpclient.HttpMethod", "info");
        properties.put(prefix+"org.apache.commons.httpclient.HttpParser", "info");
        properties.put(prefix+"org.apache.commons.httpclient.HttpState", "info");
        properties.put(prefix+"org.apache.commons.httpclient.ResponseInputStream", "info");
        properties.put(prefix+"org.apache.commons.httpclient.cookie.CookieSpec", "info");
        properties.put(prefix+"org.apache.commons.httpclient.methods.GetMethod", "info");
        properties.put(prefix+"org.apache.commons.httpclient.HttpMethodBase", "info");
        properties.put(prefix+"org.apache.commons.logging.simplelog.showdatetime", "true");
        properties.put(prefix+"httpclient.wire", "info");
        properties.put(prefix+"org.apache.commons.httpclient", "info");


    }
}
