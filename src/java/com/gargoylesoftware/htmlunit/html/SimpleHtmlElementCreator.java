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
package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.ObjectInstantiationException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * An object that knows how to create HtmlElements
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author  David K. Taylor
 */
class SimpleHtmlElementCreator extends HtmlElementCreator {
    private final Constructor constructor_;

    /**
     * Create an instance
     *
     * @param clazz The class that will be used to create the HtmlElement.
     */
    public SimpleHtmlElementCreator( final Class clazz ) {
        /*
         * The Java compiler won't allow a direct assignment to the
         * member variable constructor_ because it doesn't realize
         * that if there is an exception thrown then the assignment
         * must have failed.  So, we assign to a temporary variable
         * and then assign to the final member variable at the end.
         */
        Constructor tempConstructor = null;
        try {
            tempConstructor = clazz.getDeclaredConstructor(
                new Class[]{HtmlPage.class, Element.class});
        }
        catch( final NoSuchMethodException e ) {
            try {
                tempConstructor = clazz.getDeclaredConstructor(
                    new Class[]{HtmlPage.class, Node.class});
            }
            catch( final NoSuchMethodException e2 ) {
                throw new ObjectInstantiationException(
                    "Unable to get constuctor for class ["+clazz.getName()+"]", e2);
            }
        }
        constructor_ = tempConstructor;
    }


    /**
     * Create an HtmlElement for the specified xmlElement, contained in the specified page.
     *
     * @param page The page that this element will belong to.
     * @param xmlElement The xml element that this HtmlElement corresponds to.
     * @return The new HtmlElement.
     */
    HtmlElement create( final HtmlPage page, final Node xmlElement ) {
        try {
            return (HtmlElement)constructor_.newInstance( new Object[]{page, xmlElement});
        }
        catch( final IllegalAccessException e) {
            throw new ObjectInstantiationException(
                "Exception when calling constructor ["+constructor_+"]", e);
        }
        catch( final InstantiationException e ) {
            throw new ObjectInstantiationException(
                "Exception when calling constructor ["+constructor_+"]", e);
        }
        catch( final InvocationTargetException e ) {
            throw new ObjectInstantiationException(
                "Exception when calling constructor ["+constructor_+"]", e.getTargetException());
        }
    }
}

