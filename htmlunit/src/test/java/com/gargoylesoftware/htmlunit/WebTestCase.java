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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import junit.framework.AssertionFailedError;

import com.gargoylesoftware.base.testing.BaseTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Common superclass for HtmlUnit tests
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David D. Kilzer
 */
public abstract class WebTestCase extends BaseTestCase {
    /** Constant for the url http://first which is used in the tests. */
    public static final URL URL_FIRST;

    /** Constant for the url http://second which is used in the tests. */
    public static final URL URL_SECOND;

    /** Constant for the url http://third which is used in the tests. */
    public static final URL URL_THIRD;

    /** Constant for the url http://www.gargoylesoftware.com which is used in the tests. */
    public static final URL URL_GARGOYLE;
    static {
        try {
            URL_FIRST = new URL("http://first");
            URL_SECOND = new URL("http://second");
            URL_THIRD = new URL("http://third");
            URL_GARGOYLE = new URL("http://www.gargoylesoftware.com");
        }
        catch( final MalformedURLException e ) {
            // This is theoretically impossible.
            throw new IllegalStateException("Unable to create url constants");
        }
    }
    /**
     * Create an instance.
     * @param name The name of the test.
     */
    public WebTestCase( final String name ) {
        super( name );
    }


    /**
     * Load a page with the specified html.
     * @param html The html to use.
     * @return The new page.
     * @throws Exception if something goes wrong.
     */
    protected final HtmlPage loadPage( final String html ) throws Exception {
        return loadPage(html, null);
    }


    /**
     * Load a page with the specified html and collect alerts into the list.
     * @param html The HTML to use.
     * @param collectedAlerts The list to hold the alerts.
     * @return The new page.
     * @throws Exception If something goes wrong.
     */
    protected final HtmlPage loadPage( final String html, final List collectedAlerts )
        throws Exception {

        final WebClient client = new WebClient();
        if( collectedAlerts != null ) {
            client.setAlertHandler( new CollectingAlertHandler(collectedAlerts) );
        }

        final MockWebConnection webConnection = new MockWebConnection( client );
        webConnection.setDefaultResponse( html );
        client.setWebConnection( webConnection );

        final HtmlPage page = ( HtmlPage )client.getPage(
                new URL( "http://www.gargoylesoftware.com" ),
                SubmitMethod.POST, Collections.EMPTY_LIST );
        return page;
    }


    /**
     * Assert that the specified object is null.
     * @param object The object to check.
     */
    public static void assertNull( final Object object ) {
        if( object != null ) {
            throw new AssertionFailedError("Expected null but found ["+object+"]");
        }
    }

    /**
     * Return an input stream for the specified file name.  Refer to {@link #getFileObject(String)}
     * for details on how the file is located.
     * @param fileName The base file name.
     * @return The input stream.
     * @throws FileNotFoundException If the file cannot be found.
     */
    public static InputStream getFileAsStream( final String fileName ) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(getFileObject(fileName)));
    }

    /**
     * Return a File object for the specified file name.  This is different from just
     * <code>new File(fileName)</code> because it will adjust the location of the file
     * depending on how the code is being executed.
     *
     * @param fileName The base filename.
     * @return The new File object.
     * @throws FileNotFoundException if !file.exists()
     */
    public static File getFileObject( final String fileName ) throws FileNotFoundException {
        final String localizedName = fileName.replace( '/', File.separatorChar );

        File file = new File(localizedName);
        if( file.exists() == false ) {
            file = new File("../../"+localizedName);
        }

        if( file.exists() == false ) {
            try {
                System.out.println("currentDir="+new File(".").getCanonicalPath());
            }
            catch( final IOException e ) {
                e.printStackTrace();
            }
            throw new FileNotFoundException(localizedName);
        }
        return file;
    }
}

