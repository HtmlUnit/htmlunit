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
package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowImpl;

/**
 * The web window for a frame or iframe.
 *
 * @version $Revision$
 * @author Brad Clarke
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class FrameWindow extends WebWindowImpl {

    private final BaseFrameElement frame_;

    /**
     * Creates an instance for a given frame.
     */
    FrameWindow(final BaseFrameElement frame) {
        super(frame.getPage().getWebClient());
        frame_ = frame;
        final WebWindowImpl parent = (WebWindowImpl) getParentWindow();
        performRegistration();
        parent.addChildWindow(this);
    }

    /**
     * {@inheritDoc}
     * A FrameWindow shares it's name with it's containing frame.
     */
    @Override
    public String getName() {
        return frame_.getNameAttribute();
    }

    /**
     * {@inheritDoc}
     * A FrameWindow shares it's name with it's containing frame.
     */
    @Override
    public void setName(final String name) {
        frame_.setNameAttribute(name);
    }

    /**
     * {@inheritDoc}
     */
    public WebWindow getParentWindow() {
        return frame_.getPage().getEnclosingWindow();
    }

    /**
     * {@inheritDoc}
     */
    public WebWindow getTopWindow() {
        return getParentWindow().getTopWindow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isJavaScriptInitializationNeeded() {
        return getScriptObject() == null
            || !(getEnclosedPage().getWebResponse() instanceof StringWebResponse);
        // TODO: find a better way to distinguish content written by document.open(),...
    }

    /**
     * Returns the HTML page in which the &lt;frame&gt; or &lt;iframe&gt; tag is contained
     * for this frame window.
     * This is a facility method for <code>(HtmlPage) (getParentWindow().getEnclosedPage())</code>.
     * @return the page in the parent window
     */
    public HtmlPage getEnclosingPage() {
        return (HtmlPage) frame_.getPage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEnclosedPage(final Page page) {
        super.setEnclosedPage(page);

        // we have updated a frame window by javascript write();
        // so we have to disable future updates during initialization
        // see com.gargoylesoftware.htmlunit.html.HtmlPage.loadFrames()
        final WebResponse webResponse = page.getWebResponse();
        if (webResponse instanceof StringWebResponse) {
            final StringWebResponse response = (StringWebResponse) webResponse;
            if (response.isFromJavascript()) {
                final BaseFrameElement frame = getFrameElement();
                frame.setContentLoaded();
            }
        }
    }

    /**
     * Gets the DOM node of the (i)frame containing this window.
     * @return the DOM node
     */
    public BaseFrameElement getFrameElement() {
        return frame_;
    }

    /**
     * Gives a basic representation for debugging purposes.
     * @return a basic representation
     */
    @Override
    public String toString() {
        return "FrameWindow[name=\"" + getName() + "\"]";
    }

    /**
     * Closes this frame window.
     */
    public void close() {
        setClosed();
        final Page page = getEnclosedPage();
        if (page != null) {
            page.cleanUp();
        }
        getJobManager().shutdown();
        destroyChildren();
        getWebClient().deregisterWebWindow(this);
    }
}
