/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
 * {@link AppletContext} implementation for HtmlUnit.
 * @author Marc Guillemot
 * @version $Revision$
 */
public class AppletContextImpl implements AppletContext {
    private static final Enumeration<Applet> EMPTY_ENUMERATION
        = Collections.enumeration(Collections.<Applet>emptyList());
    private HtmlPage htmlPage_;

    AppletContextImpl(final HtmlPage page) {
        htmlPage_ = page;
    }

    /**
     * {@inheritDoc}
     */
    public Applet getApplet(final String name) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Enumeration<Applet> getApplets() {
        return EMPTY_ENUMERATION;
    }

    /**
     * {@inheritDoc}
     */
    public AudioClip getAudioClip(final URL url) {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public Image getImage(final URL url) {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public InputStream getStream(final String key) {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<String> getStreamKeys() {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public void setStream(final String key, final InputStream stream) throws IOException {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public void showDocument(final URL url) {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public void showDocument(final URL url, final String target) {
        throw new RuntimeException("Not yet implemented!");
    }

    /**
     * {@inheritDoc}
     */
    public void showStatus(final String status) {
        // perhaps should we move status handling to WebWindow
        // on the other side this allows "orphaned" pages to be usable
        final Window window = ((SimpleScriptable) htmlPage_.getScriptObject()).getWindow();
        window.jsxSet_status(status);
    }
}
