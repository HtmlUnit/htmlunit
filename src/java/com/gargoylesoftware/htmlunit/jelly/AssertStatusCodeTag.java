/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.jelly;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.XMLOutput;

/**
 * Jelly tag "assertStatusCode"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class AssertStatusCodeTag extends HtmlUnitTagSupport {
    private String code_;

    /**
     * Process the tag
     * @param xmlOutput The xml output
     * @throws JellyTagException If a problem occurs
     */
    public void doTag(XMLOutput xmlOutput) throws JellyTagException {
        invokeBody(xmlOutput);
        final int expectedStatusCode;
        try {
            expectedStatusCode = Integer.parseInt(code_);
        }
        catch( final NumberFormatException e ) {
            throw new JellyTagException("Invalid value for code: "+code_);
        }
        final int actualStatusCode = getHtmlPage().getWebResponse().getStatusCode();

        if( actualStatusCode != expectedStatusCode ) {
            throw new JellyTagException("Expected status code "+expectedStatusCode
                +" but got "+actualStatusCode+" instead");
        }
    }


    /**
     * Callback from Jelly to set the value of the code attribute.
     * @param code The new value.
     */
    public void setCode( final String code ) {
        code_ = code;
    }
}

