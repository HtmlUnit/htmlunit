/*
 * Copyright (c) 2002, 2004 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;


/**
 * A JavaScript object for frames.
 * @version $Revision$
 * @author Marc Guillemot
 */
public final class FrameWindow extends Window {
    private static final long serialVersionUID = 3761121622400448304L;
    private SimpleScriptable htmlElementView_; 

    /**
     * Create an instance.  The rhino engine requires all host objects
     * to have a default constructor.
     */
    public FrameWindow() {
    }


    /**
     * Javascript constructor.  This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }

    /**
     * Initializes the "window view" as well as the "html element view"
     * @see com.gargoylesoftware.htmlunit.javascript.host.Window#initialize(com.gargoylesoftware.htmlunit.html.HtmlPage)
     */
    public void initialize(final HtmlPage htmlPage) throws Exception {
        super.initialize(htmlPage);

        final DomNode frameDomNode = getDomNodeOrDie();

        // create additional js object to access "normal" properties of the htmlelement
        htmlElementView_ = makeJavaScriptObject("HTMLElement");
        htmlElementView_.setDomNode(frameDomNode);
        
        // set script object again as it has been changed by previous line
        frameDomNode.setScriptObject(this);
    }

    /**
     * Return the specified property or {@link #NOT_FOUND} if it could not be found.
     * @param name The name of the property
     * @param start The scriptable object that was originally queried for this property
     * @return The property.
     */
    public Object get( final String name, final Scriptable start ) {
        // try to get it as property of the window
        Object result = NOT_FOUND;
        
        
        if (result == NOT_FOUND) {
            result = super.get(name, start); 
            }

        // if not found
        // try to get it as property of the html element 
        if (result == NOT_FOUND) {
            result = ((FrameWindow) start).htmlElementView_.get(name, start);
        }
            
        return result;
    }
}
