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
     * Test {@link Base64#encode(String)}
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
     * Test {@link Base64#encode(String,String)} with a null string.
     * @throws Exception if the test fails
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
     * Test {@link Base64#encode(String,String)} with a null encoding.
     * @throws Exception if the test fails
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
     * Test {@link Base64#encode(String)} with a null string.
     * @throws Exception if the test fails
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
     * Test {@link Base64#encode(String,String)} with a null encoding.
     * @throws Exception if the test fails
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

