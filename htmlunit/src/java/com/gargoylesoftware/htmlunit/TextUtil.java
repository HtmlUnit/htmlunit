/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

import java.io.InputStream;

/**
 * Utility methods relating to text.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public final class TextUtil {
    /** Private constructor to prevent instantiation */
    private TextUtil() {}


    /**
     * Return true if the string starts with the specified prefix, irrespective of case.
     * @param stringToCheck The string to check
     * @param prefix The prefix
     * @return true if the string starts with the prefix.
     */
    public static boolean startsWithIgnoreCase( final String stringToCheck, final String prefix ) {
        assertNotNull("stringToCheck", stringToCheck);
        assertNotNull("prefix", prefix);

        if( prefix.length() == 0 ) {
            throw new IllegalArgumentException("Prefix may not be empty");
        }

        final int prefixLength = prefix.length();
        if( stringToCheck.length() < prefixLength ) {
            return false;
        }
        else {
            return stringToCheck.substring(0,prefixLength).toLowerCase().equals(prefix.toLowerCase());
        }
    }


    private static void assertNotNull( final String description, final Object object ) {
        if( object == null ) {
            throw new NullPointerException(description);
        }
    }


    /**
     * Convert a string into an input stream.
     * @param content The string
     * @return The resulting input stream.
     */
    public static InputStream toInputStream( final String content ) {
        return new java.io.StringBufferInputStream(content);
    }
}
