/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.net.MalformedURLException;

import org.mozilla.javascript.Context;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;

/**
 * The JavaScript object that represents an "Image".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HTMLImageElement extends HTMLElement {

    private static final long serialVersionUID = 5630843390548382869L;
    private String src_;

    /**
     * Creates an instance.
     */
    public HTMLImageElement() {
        // Empty.
    }

    /**
     * JavaScript constructor. This must be declared in every JavaScript file because
     * the Rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
        // Empty.
    }

    /**
     * Sets the src property, either on the DOM node which corresponds to this
     * JavaScript object, or if none exists (as when using JavaScript to preload
     * images), on the JavaScript object itself.
     * @param src the src attribute value
     */
    public void jsxSet_src(final String src) {
        src_ = src;
        final HtmlImage img = (HtmlImage) getHtmlElementOrNull();
        if (img != null) {
            img.setAttribute("src", src);
        }
        getWindow().getJavaScriptEngine().addPostponedAction(new ImageOnLoadAction());
    }

    /**
     * Returns the value of the src property, either from the DOM node which
     * corresponds to this JavaScript object, or if that doesn't exist (as
     * when using JavaScript to preload images), from the JavaScript object
     * itself.
     * @return the src attribute
     */
    public String jsxGet_src() {
        final HtmlImage img = (HtmlImage) getHtmlElementOrNull();
        if (img != null) {
            // This image has been added to the DOM tree.
            final String src = img.getSrcAttribute();
            try {
                final HtmlPage page = (HtmlPage) img.getPage();
                return page.getFullyQualifiedUrl(src).toExternalForm();
            }
            catch (final MalformedURLException e) {
                final String msg = "Unable to create fully qualified URL for src attribute of image " + e.getMessage();
                throw Context.reportRuntimeError(msg);
            }
        }
        // This is an image instantiated in JavaScript via "new Image()" and not yet added to the DOM tree.
        final WebClient webClient = getWindow().getWebWindow().getWebClient();
        final HtmlPage currentPage = (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();
        try {
            return currentPage.getFullyQualifiedUrl(src_).toExternalForm();
        }
        catch (final MalformedURLException e) {
            final String msg = "Unable to create fully qualified URL for src attribute of image " + e.getMessage();
            throw Context.reportRuntimeError(msg);
        }
    }

    /**
     * Sets the <tt>onload</tt> event handler for this element.
     * @param onloadHandler the <tt>onload</tt> event handler for this element
     */
    public void jsxSet_onload(final Object onloadHandler) {
        setEventHandlerProp("onload", onloadHandler);
        getWindow().getJavaScriptEngine().addPostponedAction(new ImageOnLoadAction());
    }

    /**
     * Returns the <tt>onload</tt> event handler for this element.
     * @return the <tt>onload</tt> event handler for this element
     */
    public Object jsxGet_onload() {
        return getEventHandlerProp("onload");
    }

    /**
     * Custom JavaScript postponed action which downloads the image and invokes the onload handler, if necessary.
     */
    private class ImageOnLoadAction implements PostponedAction {
        public void execute() throws Exception {
            final HtmlImage img = (HtmlImage) getHtmlElementOrNull();
            if (img != null) {
                img.doOnLoad();
            }
        }
    }

}
