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
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 *  JavaScript object representing an Event that is passed into Event Handlers
 * when they are invoked.  For general information on which properties and functions
 * should be supported, see <a href="www.mozilla.org/docs/dom/domref/dom_event_ref.html">
 * the mozilla docs</a>.
 * 
 * @version $Revision$
 * @author <a hrer="mailto:chriseldredge@comcast.net">Chris Eldredge</a>
 * @author Mike Bowler
 */
public class Event extends SimpleScriptable {

    private static final long serialVersionUID = 4050485607908455730L;
    private Object currentTarget_;

    /**
     * Creates a new instance.
     * @param domNode The DomNode that triggered the Event.
     * @param currentTarget The current target Event is being propagated on behalf of.
     */
    public Event(final DomNode domNode, final Object currentTarget) {
        super();

        currentTarget_ = currentTarget;

        super.setDomNode(domNode, false);
    }

    /**
     * javascript getter for currentTarget property
     * @return the current target
     */
    public Object jsGet_currentTarget() {
        return currentTarget_;
    }

    /**
     * @return string description of Event and related fields
     */
    public String toString() {
        final StringBuffer buffer = new StringBuffer("Event: (");
        buffer.append("Current Target: ");
        buffer.append(currentTarget_.toString());
        buffer.append(");");
        return buffer.toString();
    }
}
