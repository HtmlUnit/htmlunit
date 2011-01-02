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
package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.WebWindowImpl;

/**
 * The web window for a frame or iframe.
 *
 * @version $Revision$
 * @author Brad Clarke
 * @author Ahmed Ashour
 */
public class FrameWindow extends WebWindowImpl {

    private final BaseFrame frame_;

    /**
     * Creates an instance for a given frame.
     */
    FrameWindow(final BaseFrame frame) {
        super(frame.getPage().getWebClient());
        frame_ = frame;
        final WebWindowImpl parent = (WebWindowImpl) getParentWindow();
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
     * Gets the DOM node of the (i)frame containing this window.
     * @return the DOM node
     */
    public BaseFrame getFrameElement() {
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
}
