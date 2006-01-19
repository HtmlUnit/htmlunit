/*
 * Copyright (c) 2002-2006 Gargoyle Software Inc. All rights reserved.
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
 * An exception that is thrown when the server returns a failing status code.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 */
public class FailingHttpStatusCodeException extends RuntimeException {
    private static final long serialVersionUID = 4080165207084775250L;

    private final int statusCode_; // to remove together with the deprecated c'tor
    private final String statusMessage_; // to remove together with the deprecated c'tor
    private final WebResponse response_;

    /**
     *  Create an instance
     *
     * @param  statusCode The failing status code
     * @param  statusMessage The message associated with the failing code
     * @deprecated after 1.7 since it doesn't allow to acces the received response 
     */
    public FailingHttpStatusCodeException( final int statusCode, final String statusMessage ) {
        statusCode_ = statusCode;
        statusMessage_ = statusMessage;
        response_ = null;
    }


    /**
     * Create an instance
     *
     * @param failingResponse the failing response
     */
    public FailingHttpStatusCodeException(final WebResponse failingResponse) {

        statusCode_ = failingResponse.getStatusCode();
        statusMessage_ = failingResponse.getStatusMessage();
        response_ = failingResponse;
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

    /**
     * @see java.lang.Throwable#getMessage()
     */
    public String getMessage() {
        return "" + getStatusCode() + " " + getStatusMessage();
    }

    /**
     * Gets the failing response
     * @return the response
     */
    public WebResponse getResponse() {
        return response_;
    }
}

