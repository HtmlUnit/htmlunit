/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.test;

import com.gargoylesoftware.htmlunit.TextUtil;

public final class TextUtilTest extends WebTestCase {
    public TextUtilTest( final String args ) {
        super(args);
    }


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


    public void testStartsWithIgnoreCase_emptyPrefix() {
        try {
            TextUtil.startsWithIgnoreCase("foo", "");
            fail("Expected IllegalArgumentException");
        }
        catch( final IllegalArgumentException e ) {
            // Expected path
        }
    }


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
}
