/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.Base64;

/**
 * Tests for Base64
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class Base64Test extends WebTestCase {
    /**
     * Create an instance.
     * @param name The name of the test.
     */
    public Base64Test( final String name ) {
        super( name );
    }


    /**
     * Test {@link Base64.encode(String)}
     */
    public void testEncode() {
        final String data[][] = {
                {"", ""},
                {"a", "YQ=="},
                {"bc", "YmM="},
                {"def", "ZGVm"},
                {"ghij", "Z2hpag=="},
                {"klmno", "a2xtbm8="},
                {"pqrztu", "cHFyenR1"},
                };

        for( int i = 0; i < data.length; i++ ) {
            final String input = data[i][0];
            final String expectedOutput = data[i][1];

            assertEquals( "encoding \"" + input + "\"", expectedOutput, Base64.encode( input ) );
        }
    }


    /**
     * Test {@link Base64.encode(String,String)} with a null string.
     */
    public void testEncodeStringEncoding_NullString()
        throws Exception {
        try {
            Base64.encode( null, "us-ascii" );
            fail( "Expected NullPointerException" );
        }
        catch( final NullPointerException e ) {
            // Expected path
        }
    }


    /**
     * Test {@link Base64.encode(String,String)} with a null encoding.
     */
    public void testEncodeStringEncoding_NullEncoding()
        throws Exception {
        try {
            Base64.encode( null, "us-ascii" );
            fail( "Expected NullPointerException" );
        }
        catch( final NullPointerException e ) {
            // Expected path
        }
    }


    /**
     * Test {@link Base64.encode(String)} with a null string.
     */
    public void testEncodeString_Null()
        throws Exception {
        try {
            Base64.encode( ( String )null );
            fail( "Expected NullPointerException" );
        }
        catch( final NullPointerException e ) {
            // Expected path
        }
    }


    /**
     * Test {@link Base64.encode(String,String)} with a null encoding.
     */
    public void testEncodeBytes_NullArray()
        throws Exception {
        try {
            Base64.encode( "foo", null );
            fail( "Expected NullPointerException" );
        }
        catch( final NullPointerException e ) {
            // Expected path
        }
    }
}

