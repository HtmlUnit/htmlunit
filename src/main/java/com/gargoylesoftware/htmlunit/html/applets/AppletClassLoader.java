/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html.applets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.commons.io.IOUtils;

import com.gargoylesoftware.htmlunit.WebResponse;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
 * Class loader for loading applets.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class AppletClassLoader extends URLClassLoader {
    /**
     * The constructor.
     */
    public AppletClassLoader() {
        super(new URL[0]);
    }

    /**
     * Adds the content of specified WebResponse to the classpath for the applet.
     * @param webResponse the web response
     * @throws IOException in case of problem working with the response content
     */
    public void addArchiveToClassPath(final WebResponse webResponse) throws IOException {
        // normally content type should be "application/java-archive"
        // but it works when it is not the case
        final File tmpFile = File.createTempFile("HtmlUnit", ".jar");
        tmpFile.deleteOnExit();
        final OutputStream output = new FileOutputStream(tmpFile);
        IOUtils.copy(webResponse.getContentAsStream(), output);
        output.close();
        final URL jarUrl = new URL("jar", "", "file:" + tmpFile.getAbsolutePath() + "!/");
        addURL(jarUrl);
    }

    /**
     * Adds the content of specified WebResponse to the classpath for the applet.
     * @param className the name of the class to load
     * @param webResponse the web response
     * @throws IOException in case of problem working with the response content
     */
    public void addClassToClassPath(final String className, final WebResponse webResponse) throws IOException {
        final File tmpFile = File.createTempFile("HtmlUnit", ".class");
        tmpFile.deleteOnExit();
        final OutputStream output = new FileOutputStream(tmpFile);
        IOUtils.copy(webResponse.getContentAsStream(), output);
        output.close();
        final FileInputStream is = new FileInputStream(tmpFile);
        try {
            final byte[] bytes = IOUtils.toByteArray(is);
            defineClass(className, bytes, 0, bytes.length);
        }
        finally {
            is.close();
        }
    }
}
