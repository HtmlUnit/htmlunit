/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

import java.io.UnsupportedEncodingException;

/**
 * A utility class for base 64 encoding.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>

 */
public class Base64 {
    private static final byte[] ENCODING_TABLE;
    private static final byte PADDING_BYTE;

    static {
        final String table = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
        try {
            ENCODING_TABLE = table.getBytes( "ISO-8859-1" );
            PADDING_BYTE = "=".getBytes( "ISO-8859-1" )[0];
        }
        catch( final UnsupportedEncodingException e ) {
            throw new IllegalStateException(
                "Theoretically impossible: ISO-8859-1 (us-ascii) is not a supported encoding" );
        }

        if( ENCODING_TABLE.length != 64 ) {
            throw new IllegalStateException( "Encoding table doesn't contain 64 values" );
        }
    }


    private Base64() {
    }


    /**
     * Encode the specified string as base 64.
     *
     * @param input The input string
     * @return The encoded string
     */
    public static String encode( final String input ) {
        try {
            return encode( input, "ISO-8859-1" );
        }
        catch( final UnsupportedEncodingException e ) {
            throw new IllegalStateException(
                "Theoretically impossible: ISO-8859-1 (us-ascii) is not a supported encoding" );
        }
    }


    /**
     * Encode the string as base 64 using the specified encoding
     *
     * @param input The input string
     * @param characterEncoding The encoding to use for the input string
     * @return The encoded string
     * @exception UnsupportedEncodingException If the specified encoding is invalid.
     */
    public static String encode( final String input, final String characterEncoding )
        throws UnsupportedEncodingException {

        return new String( encode( input.getBytes( characterEncoding ) ), characterEncoding );
    }


    /**
     * Encode the specified byte as base 64.
     *
     * @param array The input array
     * @return The encoded byte array
     */
    public static byte[] encode( final byte[] array ) {
        final int paddingCharCount = ( 3 - ( array.length % 3 ) ) % 3;

        final byte[] input;

        if( paddingCharCount == 0 ) {
            input = array;
        }
        else {
            input = new byte[array.length + paddingCharCount];
            System.arraycopy( array, 0, input, 0, array.length );
        }

        final byte output[] = new byte[( input.length * 4 / 3 )];
        int outputIndex = 0;

        byte byte1;
        byte byte2;
        byte byte3;

        for( int i = 0; i < input.length; i += 3 ) {
            byte1 = input[i];
            byte2 = input[i + 1];
            byte3 = input[i + 2];

            output[outputIndex++] = ENCODING_TABLE[( byte1 & 0xFC ) >>> 2];
            output[outputIndex++] = ENCODING_TABLE[( ( byte1 & 0x03 ) << 4 )
                | ( ( byte2 & 0xF0 ) >>> 4 )];
            output[outputIndex++] = ENCODING_TABLE[( ( byte2 & 0x0F ) << 2 )
                | ( ( byte3 & 0xC0 ) >>> 6 )];
            output[outputIndex++] = ENCODING_TABLE[byte3 & 0x3F];
        }

        if( paddingCharCount > 1 ) {
            output[--outputIndex] = PADDING_BYTE;
        }
        if( paddingCharCount > 0 ) {
            output[--outputIndex] = PADDING_BYTE;
        }

        return output;
    }
}

