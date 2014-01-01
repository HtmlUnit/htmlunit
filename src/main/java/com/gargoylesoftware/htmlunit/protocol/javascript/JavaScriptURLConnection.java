/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.protocol.javascript;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.gargoylesoftware.htmlunit.TextUtil;

/**
 * A URLConnection for supporting JavaScript URLs.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class JavaScriptURLConnection extends URLConnection {

    /** The JavaScript "URL" prefix. */
    public static final String JAVASCRIPT_PREFIX = "javascript:";

    /** The JavaScript code. */
    private final String content_;

    /**
     * Creates an instance.
     * @param newUrl the JavaScript URL
     */
    public JavaScriptURLConnection(final URL newUrl) {
        super(newUrl);
        content_ = newUrl.toExternalForm().substring(JAVASCRIPT_PREFIX.length());
    }

    /**
     * This method does nothing in this implementation but is required to be implemented.
     */
    @Override
    public void connect() {
        // Empty.
    }

    /**
     * Returns the input stream - in this case the content of the URL.
     * @return the input stream
     */
    @Override
    public InputStream getInputStream() {
        return TextUtil.toInputStream(content_);
    }

}
