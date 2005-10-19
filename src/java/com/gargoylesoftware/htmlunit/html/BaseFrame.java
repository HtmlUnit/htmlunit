/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.html;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebWindow;

/**
 * Base class for frame and iframe.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author  David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author David D. Kilzer 
 * @author Stefan Anzinger
 */
public abstract class BaseFrame extends StyledElement {

    /**
     * The web window for a frame or iframe.
     */
    public final class FrameWindow implements WebWindow {
        private Page enclosedPage_;
        private Object scriptObject_;

        /**
         * Private constructor as instanciation is not allowed directly.
         */
        private FrameWindow() {
            // nothing to do
        }

        /**
         * Return the name of this window.
         *
         * @return The name of this window.
         */
        public String getName() {
            return getNameAttribute();
        }

        /**
         * Set the name of this window.
         *
         * @param name The new name of this window.
         */
        public void setName(final String name) {
            setNameAttribute(name);
        }

        /**
         * Return the currently loaded page or null if no page has been loaded.
         *
         * @return The currently loaded page or null if no page has been loaded.
         */
        public Page getEnclosedPage() {
            return enclosedPage_;
        }

        /**
         * Set the currently loaded page.
         *
         * @param page The new page or null if there is no page (ie empty window)
         */
        public void setEnclosedPage( final Page page ) {
            enclosedPage_ = page;
        }

        /**
         * Return the window that contains this window.
         *
         * @return The parent window.
         */
        public WebWindow getParentWindow() {
            return getPage().getEnclosingWindow();
        }

        /**
         * Return the top level window that contains this window.
         *
         * @return The top level window that contains this window.
         */
        public WebWindow getTopWindow() {
            return getParentWindow().getTopWindow();
        }

        /**
         * Return the web client that "owns" this window.
         *
         * @return The web client or null if this window has been closed.
         */
        public WebClient getWebClient() {
            return getPage().getWebClient();
        }

        /**
         * Internal use only - subject to change without notice.<p>
         * Return the javascript object that corresponds to this window.
         * @return The javascript object that corresponds to this window.
         */
        public Object getScriptObject() {
            return scriptObject_;
        }

        /**
         * Internal use only - subject to change without notice.<p>
         * Set the javascript object that corresponds to this node.  This is not
         * guarenteed to be set.
         * @param scriptObject The javascript object.
         */
        public void setScriptObject( final Object scriptObject ) {
            scriptObject_ = scriptObject;
        }

        /**
         * Return the html page in wich the &lt;frame&gt; or &lt;iframe&gt; tag is contained
         * for this frame window.
         * This is a facility method for <code>(HtmlPage) (getParentWindow().getEnclosedPage())</code>.
         * @return the page in the parent window.
         */
        public HtmlPage getEnclosingPage() {
            return getPage();
        }

        /**
         * Gives a basic representation for debugging purposes
         * @return a basic representation
         */
        public String toString() {
            return "FrameWindow[name=\""+getName()+"\"]";
        }
    }

    private final WebWindow enclosedWindow_ = new FrameWindow();

    /**
     * Create an instance of BaseFrame
     *
     * @param page The HtmlPage that contains this element.
     * @param attributes the initial attributes
     */
    protected BaseFrame( final HtmlPage page, final Map attributes) {

        super(page, attributes);

        final WebClient webClient = page.getWebClient();
        webClient.registerWebWindow(enclosedWindow_);

        try {
            // put about:blank in the window to allow JS to run on this frame before the
            // real content is loaded
            getPage().getWebClient().pushClearFirstWindow();
            getPage().getWebClient().getPage(enclosedWindow_, new WebRequestSettings(WebClient.URL_ABOUT_BLANK));
            getPage().getWebClient().popFirstWindow();
        }
        catch (final FailingHttpStatusCodeException e) {
            // should never occur
        }
        catch (final IOException e) {
            // should never occur
        }
    }


    /**
     * Called after the node for <frame...> or <iframe> has been added to the containing page.
     * The node needs to be added first to allow js in the frame to see the frame in the parent
     */
    void loadInnerPage() {
        String source = getSrcAttribute();
        if( source.length() == 0 ) {
            // Nothing to load
            source = "about:blank";
        }
        getPage().getWebClient().pushClearFirstWindow();
        loadInnerPageIfPossible(source);
        getPage().getWebClient().popFirstWindow();
    }

    private void loadInnerPageIfPossible(final String srcAttribute) {

        if( srcAttribute.length() != 0 ) {
            URL url = null;
            try {
                url = getPage().getFullyQualifiedUrl(srcAttribute);
            }
            catch( final MalformedURLException e ) {
                getLog().error("Bad url in src attribute of " + getTagName() + ": url=["+srcAttribute+"]", e);
            }
            try {
                getPage().getWebClient().getPage(enclosedWindow_, new WebRequestSettings(url));
            }
            catch (final FailingHttpStatusCodeException e){
                // do nothing
            }
            catch( final IOException e ) {
                getLog().error("IOException when getting content for " + getTagName()
                        + ": url=["+url.toExternalForm()+"]", e);
            }
        }
    }

    /**
     * @return the HTML tag name
     */
    public abstract String getTagName();

    /**
     * Return the value of the attribute "longdesc".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "longdesc"
     * or an empty string if that attribute isn't defined.
     */
    public final String getLongDescAttribute() {
        return getAttributeValue("longdesc");
    }


    /**
     * Return the value of the attribute "name".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "name"
     * or an empty string if that attribute isn't defined.
     */
    public final String getNameAttribute() {
        return getAttributeValue("name");
    }


    /**
     * Set the value of the "name" attribute.
     *
     * @param name The new window name.
     */
    public final void setNameAttribute(final String name) {
        setAttributeValue("name", name);
    }


    /**
     * Return the value of the attribute "src".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "src"
     * or an empty string if that attribute isn't defined.
     */
    public final String getSrcAttribute() {
        return getAttributeValue("src");
    }


    /**
     * Return the value of the attribute "frameborder".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "frameborder"
     * or an empty string if that attribute isn't defined.
     */
    public final String getFrameBorderAttribute() {
        return getAttributeValue("frameborder");
    }


    /**
     * Return the value of the attribute "marginwidth".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "marginwidth"
     * or an empty string if that attribute isn't defined.
     */
    public final String getMarginWidthAttribute() {
        return getAttributeValue("marginwidth");
    }


    /**
     * Return the value of the attribute "marginheight".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "marginheight"
     * or an empty string if that attribute isn't defined.
     */
    public final String getMarginHeightAttribute() {
        return getAttributeValue("marginheight");
    }


    /**
     * Return the value of the attribute "noresize".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "noresize"
     * or an empty string if that attribute isn't defined.
     */
    public final String getNoResizeAttribute() {
        return getAttributeValue("noresize");
    }


    /**
     * Return the value of the attribute "scrolling".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "scrolling"
     * or an empty string if that attribute isn't defined.
     */
    public final String getScrollingAttribute() {
        return getAttributeValue("scrolling");
    }


    /**
     * Return the value of the attribute "onload".  This attribute is not
     * actually supported by the HTML specification however it is supported
     * by the popular browsers.
     *
     * @return The value of the attribute "onload"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnLoadAttribute() {
        return getAttributeValue("onload");
    }


    /**
     * Return the currently loaded page in the enclosed window.
     * This is a facility method for <code>getEnclosedWindow().getEnclosedPage()</code>.
     * @see WebWindow#getEnclosedPage()
     * @return The currently loaded page in the enclosed window or null if no page has been loaded.
     */
    public Page getEnclosedPage() {
        return getEnclosedWindow().getEnclosedPage();
    }


    /**
     * Gets the window enclosed in this frame.
     * @return the window
     */
    public WebWindow getEnclosedWindow() {
        return enclosedWindow_;
    }


    /**
     * Set the value of the "src" attribute.  Also load the frame with the specified url if possible.
     * @param attribute The new value
     */
    public final void setSrcAttribute( final String attribute ) {
        setAttributeValue("src", attribute);
        loadInnerPageIfPossible(attribute);
    }
}
