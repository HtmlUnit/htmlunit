/*
 *  Copyright (C) 2002 Gargoyle Software. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

/**
 *  An exception that is thrown when the server returns a failing status code
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class FailingHttpStatusCodeException extends RuntimeException {
    private final int statusCode_;
    private final String statusMessage_;


    /**
     *  Create an instance
     *
     * @param  statusCode The failing status code
     * @param  statusMessage The message associated with the failing code
     */
    public FailingHttpStatusCodeException( final int statusCode, final String statusMessage ) {
        super( "" + statusCode + " " + statusMessage );

        statusCode_ = statusCode;
        statusMessage_ = statusMessage;
    }


    /**
     *  Return the failing status code
     *
     * @return  the code
     */
    public int getStatusCode() {
        return statusCode_;
    }


    /**
     *  Return the message associated with the failing status code
     *
     * @return  The message
     */
    public String getStatusMessage() {
        return statusMessage_;
    }
}

