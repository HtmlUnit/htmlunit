/*
 * Copyright (c) 2002, 2003 Gargoyle Software Inc. All rights reserved.
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

import java.util.EventObject;

/**
 * An event that will be fired when a WebWindow changes.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public final class WebWindowEvent extends EventObject {
    private final Page oldPage_;
    private final Page newPage_;


    /**
     * Create an instance
     *
     * @param source The WebWindow that caused the event
     * @param oldPage The old contents of the web window
     * @param newPage The new contents of the web window
     */
    public WebWindowEvent( final WebWindow source, final Page oldPage, final Page newPage ) {
        super(source);
        oldPage_ = oldPage;
        newPage_ = newPage;
    }


    /**
     * Return true if the two objects are equal
     *
     * @param object The object to compare against.
     * @return true if the two objects are equal.
     */
    public boolean equals( final Object object ) {
        if( getClass() == object.getClass() ) {
            final WebWindowEvent event = (WebWindowEvent)object;
            return isEqual(getSource(), event.getSource())
                && isEqual(getOldPage(), event.getOldPage())
                && isEqual(getNewPage(), event.getNewPage());
        }
        return false;
    }


    /**
     * Return the hash code for this object.
     * @return the hash code for this object.
     */
    public int hashCode() {
        return source.hashCode();
    }

    /**
     * Return the oldPage
     * @return the page or null if the window has no page
     */
    public Page getOldPage() {
        return oldPage_;
    }


    /**
     * Return the oldPage
     * @return the page or null if the window has no page
     */
    public Page getNewPage() {
        return newPage_;
    }


    /**
     * Return the web window that fired the event.
     * @return The web window that fired the event.
     */
    public WebWindow getWebWindow() {
        return (WebWindow)getSource();
    }


    private boolean isEqual( final Object object1, final Object object2 ) {
        final boolean result;

        if( object1 == null && object2 == null ) {
            result = true;
        }
        else if( object1 == null || object2 == null ) {
            result = false;
        }
        else {
            result = object1.equals(object2);
        }

        return result;
    }


    /**
     * Return a string representation of this event
     * @return A string representation of this event.
     */
    public String toString() {
        return "WebWindowEvent(source=["+getSource()+"] oldPage=["
            +getOldPage()+"] newPage=["+getNewPage()+"])";
    }
}

