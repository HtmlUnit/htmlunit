/*
 *  Copyright (C) 2002 Gargoyle Software. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class StrictErrorHandler implements ErrorHandler {
    public void warning( final SAXParseException rethrow ) throws SAXParseException {
        throw rethrow;
    }


    public void error( final SAXParseException rethrow ) throws SAXParseException {
        throw rethrow;
    }


    public void fatalError( final SAXParseException rethrow ) throws SAXParseException {
        throw rethrow;
    }
}

