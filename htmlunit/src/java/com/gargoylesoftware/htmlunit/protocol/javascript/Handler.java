/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.protocol.javascript;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 *  Stream handler for javascript urls
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class Handler extends URLStreamHandler {
    /**
     * Return a new URLConnection for this url.
     * @param url The javascript url.
     * @return The connection.
     */
    protected URLConnection openConnection( final URL url ) {
        return new JavaScriptURLConnection(url);
    }
}
