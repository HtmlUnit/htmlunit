/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * An error handler that throws an exception for all problems no matter how minor.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class StrictErrorHandler implements ErrorHandler {
    /**
     * A warning occurred.
     *
     * @param rethrow An exception which will be rethrown.
     * @throws SAXParseException Always.
     */
    public void warning( final SAXParseException rethrow ) throws SAXParseException {
        throw rethrow;
    }


    /**
     * An error occurred.
     *
     * @param rethrow An exception which will be rethrown.
     * @throws SAXParseException Always.
     */
    public void error( final SAXParseException rethrow ) throws SAXParseException {
        throw rethrow;
    }


    /**
     * A fatal error occurred.
     *
     * @param rethrow An exception which will be rethrown.
     * @throws SAXParseException Always.
     */
    public void fatalError( final SAXParseException rethrow ) throws SAXParseException {
        throw rethrow;
    }
}

