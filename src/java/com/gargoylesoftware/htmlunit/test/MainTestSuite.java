/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.test;

import com.gargoylesoftware.base.testing.AcceptAllTestFilter;
import com.gargoylesoftware.base.testing.RecursiveTestSuite;
import java.io.File;
import java.io.IOException;

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

        enableAllLogging();
    }


    /**
     * Set the appropriate logging levels for running the tests.
     */
    public static void enableAllLogging() {
        System.getProperties().put("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
        System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog", "trace");

        final String prefix = "org.apache.commons.logging.simplelog.log.";
        System.getProperties().put(prefix+"org.apache.commons.httpclient.Authenticator", "info");
        System.getProperties().put(prefix+"org.apache.commons.httpclient.Cookie", "info");
        System.getProperties().put(prefix+"org.apache.commons.httpclient.HeaderElement", "info");
        System.getProperties().put(prefix+"org.apache.commons.httpclient.HttpClient", "info");
        System.getProperties().put(prefix+"org.apache.commons.httpclient.HttpConnection", "info");
        System.getProperties().put(prefix+"org.apache.commons.httpclient.HttpMethod", "info");
        System.getProperties().put(prefix+"org.apache.commons.httpclient.HttpParser", "info");
        System.getProperties().put(prefix+"org.apache.commons.httpclient.HttpState", "info");
        System.getProperties().put(prefix+"org.apache.commons.httpclient.ResponseInputStream", "info");
        System.getProperties().put(prefix+"org.apache.commons.httpclient.cookie.CookieSpec", "info");
        System.getProperties().put(prefix+"org.apache.commons.httpclient.methods.GetMethod", "info");
    }
}
