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

import java.applet.AppletContext;
import java.applet.AppletStub;
import java.net.URL;
import java.util.HashMap;

import com.gargoylesoftware.htmlunit.html.HtmlApplet;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
 * {@link AppletStub} implementation for HtmlUnit. This is what is used by an applet to communicate
 * with the browser.
 * @author Marc Guillemot
 * @author Ronald Brill
 * @version $Revision$
 */
public class AppletStubImpl implements AppletStub {
    private final AppletContextImpl appletContextImpl_;
    private final HashMap<String, String> parameters_;
    private final URL codebase_;
    private final URL documentbase_;

    /**
     * Constructs a stub for an applet node.
     * @param htmlApplet the applet node
     * @param parameters app parameters of the applet
     * @param codebase codebase of the applet
     * @param documentbase documentbase of the applet
     */
    public AppletStubImpl(final HtmlApplet htmlApplet,
            final HashMap<String, String> parameters, final URL codebase, final URL documentbase) {
        appletContextImpl_ = new AppletContextImpl((HtmlPage) htmlApplet.getPage());
        parameters_ = parameters;
        codebase_ = codebase;
        documentbase_ = documentbase;
    }

    /**
     * {@inheritDoc}
     */
    public void appletResize(final int width, final int height) {
        // does nothing
    }

    /**
     * {@inheritDoc}
     */
    public AppletContext getAppletContext() {
        return appletContextImpl_;
    }

    /**
     * {@inheritDoc}
     */
    public URL getCodeBase() {
        return codebase_;
    }

    /**
     * {@inheritDoc}
     */
    public URL getDocumentBase() {
        return documentbase_;
    }

    /**
     * {@inheritDoc}
     */
    public String getParameter(final String name) {
        return parameters_.get(name);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isActive() {
        throw new RuntimeException(
                "Not yet implemented! (com.gargoylesoftware.htmlunit.html.applets.AppletStubImpl.isActive())");
    }
}
