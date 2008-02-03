/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit;

/**
 * An interface that represents one window in a browser.  It could be a top level window or a frame.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author David D. Kilzer
 */
public interface WebWindow {

    /**
     * Return the name of this window.
     *
     * @return The name of this window.
     */
    String getName();

    /**
     * Set the name of this window.
     *
     * @param name The new window name.
     */
    void setName(final String name);

    /**
     * Return the currently loaded page or null if no page has been loaded.
     *
     * @return The currently loaded page or null if no page has been loaded.
     */
    Page getEnclosedPage();

    /**
     * Set the currently loaded page.
     *
     * @param page The new page or null if there is no page (ie empty window)
     */
    void setEnclosedPage(final Page page);

    /**
     * Return the window that contains this window.  If this is a top
     * level window, then return this window.
     *
     * @return The parent window or this window if there is no parent.
     */
    WebWindow getParentWindow();

    /**
     * Return the top level window that contains this window.  If this
     * is a top level window, then return this window.
     *
     * @return The top level window that contains this window or this
     * window if there is no parent.
     */
    WebWindow getTopWindow();

    /**
     * Return the web client that "owns" this window.
     *
     * @return The web client or null if this window has been closed.
     */
    WebClient getWebClient();

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Set the javascript object that corresponds to this element.  This is not guaranteed
     * to be set even if there is a javascript object for this html element.
     * @param scriptObject The javascript object.
     */
    void setScriptObject(final Object scriptObject);

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Return the javascript object that corresponds to this element.
     * @return The javascript object that corresponds to this element.
     */
    Object getScriptObject();

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     *
     * Currently exposed here for testing purposes, a better API will be added in the
     * future and this will become a completely internal class.
     *
     * @return The ThreadManager for this WebWindow
     */
    ThreadManager getThreadManager();
}

