/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.protocol.javascript;

import com.gargoylesoftware.htmlunit.TextUtil;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;

/**
 * A URLConnection for supporting javascript urls
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class JavaScriptURLConnection extends URLConnection {
    private final String content_;


    /**
     * Create an instance
     * @param url The javascript url.
     */
    public JavaScriptURLConnection( final URL url ) {
        super(url);

        content_ = url.toExternalForm().substring("javascript:".length());
    }


    /**
     * This method does nothing in this implementation but is required to be implemented.
     * @throws IOException If an error occurs.
     */
    public void connect() throws IOException {
    }


    /**
     * Return the input stream - in this case the content of the url
     * @return The input stream
     * @throws IOException If an IO error occurs.
     */
    public InputStream getInputStream() throws IOException {
        return TextUtil.toInputStream(content_);
    }
}
