/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *  Tests for TextUtil.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public final class TextUtilTest extends WebTestCase {
    /**
     * Create an instance.
     * @param name The name of the test.
     */
    public TextUtilTest( final String name ) {
        super(name);
    }


    /**
     * Test startsWithIgnoreCase() with null values.
     */
    public void testStartsWithIgnoreCase_nulls() {
        try {
            TextUtil.startsWithIgnoreCase(null, "foo");
            fail("Expected null pointer exception");
        }
        catch( final NullPointerException e ) {
            // Expected path
        }

        try {
            TextUtil.startsWithIgnoreCase("foo", null);
            fail("Expected null pointer exception");
        }
        catch( final NullPointerException e ) {
            // Expected path
        }
    }


    /**
     * Test startsWithIgnoreCase() with an empty prefix
     */
    public void testStartsWithIgnoreCase_emptyPrefix() {
        try {
            TextUtil.startsWithIgnoreCase("foo", "");
            fail("Expected IllegalArgumentException");
        }
        catch( final IllegalArgumentException e ) {
            // Expected path
        }
    }


    /**
     * Test a variety of cases that should return true.
     */
    public void testStartsWithIgnoreCase_ShouldReturnTrue() {
        final String[][] data = {
            {"foo","foo"},
            {"foo:bar","foo"},
            {"FOO:BAR","foo"},
            {"foo:bar","FOO"},
        };

        for( int i=0; i<data.length; i++ ) {
            final String stringToCheck = data[i][0];
            final String prefix = data[i][1];

            assertTrue(
                "stringToCheck=["+stringToCheck+"] prefix=["+prefix+"]",
                TextUtil.startsWithIgnoreCase(stringToCheck, prefix));
        }
    }


    /**
     * Test a variety of cases that should return false.
     */
    public void testStartsWithIgnoreCase_ShouldReturnFalse() {
        final String[][] data = {
            {"","foo"},
            {"fobar","foo"},
            {"fo","foo"},
        };

        for( int i=0; i<data.length; i++ ) {
            final String stringToCheck = data[i][0];
            final String prefix = data[i][1];

            assertFalse(
                "stringToCheck=["+stringToCheck+"] prefix=["+prefix+"]",
                TextUtil.startsWithIgnoreCase(stringToCheck, prefix));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    public void testToInputStream_null() throws Exception {
        try {
            TextUtil.toInputStream(null);
            fail("Expected NullPointerException");
        }
        catch( final NullPointerException e ) {
            // Expected path
        }
    }


    /**
     * @throws Exception if the test fails
     */
    public void testToInputStream() throws Exception {
        final String[][] data = {
            {"", null},
            {"a", "a"},
            {"abcdefABCDEF", "abcdefABCDEF"},
        };
        final String encoding = "ISO-8859-1";

        for( int i=0; i<data.length; i++ ) {
            final String input = data[i][0];
            final String expectedResult = data[i][1];

            final InputStream inputStream = TextUtil.toInputStream(input, encoding);
            final String actualResult
                = new BufferedReader( new InputStreamReader(inputStream, encoding) ).readLine();
            assertEquals( expectedResult, actualResult);
        }
    }
}
