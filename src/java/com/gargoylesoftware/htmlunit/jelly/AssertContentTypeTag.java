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
 * Jelly tag "assertContentType"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class AssertContentTypeTag extends HtmlUnitTagSupport {
    private String contentType_;


    /**
     * Process the tag
     * @param xmlOutput The xml output
     * @throws JellyTagException If a problem occurs
     */
    public void doTag(XMLOutput xmlOutput) throws JellyTagException {
        invokeBody(xmlOutput);

        if( contentType_ == null ) {
            throw new JellyTagException("contentType is a mandatory attribute");
        }

        final String actualContentType = getHtmlPage().getWebResponse().getContentType();

        if( actualContentType.equals(contentType_) == false ) {
            throw new JellyTagException("Expected content-type "+contentType_+" but got "+actualContentType+" instead");
        }
    }


    /**
     * Callback from Jelly to set the value of the contentType attribute.
     * @param contentType The new value.
     */
    public void setContentType( final String contentType ) {
        contentType_ = contentType;
    }
}

