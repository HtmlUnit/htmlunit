/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.protocol.javascript;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.io.StringBufferInputStream;
import java.io.InputStream;

public class JavaScriptURLConnection extends URLConnection {
    private final String content_;


    public JavaScriptURLConnection( final URL url ) {
        super(url);

        content_ = url.toExternalForm().substring("javascript:".length());
    }


    public void connect() throws IOException {
    }


    public InputStream getInputStream() throws IOException {
        return new StringBufferInputStream(content_);
    }
}
