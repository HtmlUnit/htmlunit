/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.ObjectInstantiationException;
import org.w3c.dom.Element;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An object that knows how to create HtmlElements
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
class SimpleHtmlElementCreator extends HtmlElementCreator {
    private final Constructor constructor_;

    /**
     * Create an instance
     *
     * @param clazz The class that will be used to create the HtmlElement.
     */
    public SimpleHtmlElementCreator( final Class clazz ) {
        try {
            constructor_ = clazz.getDeclaredConstructor(
                new Class[]{HtmlPage.class, Element.class});
        }
        catch( final NoSuchMethodException e ) {
            throw new ObjectInstantiationException(
                "Unable to get constuctor for class ["+clazz.getName()+"]", e);
        }
    }


    /**
     * Create an HtmlElement for the specified xmlElement, contained in the specified page.
     *
     * @param page The page that this element will belong to.
     * @param xmlElement The xml element that this HtmlElement corresponds to.
     * @return The new HtmlElement.
     */
    HtmlElement create( final HtmlPage page, final Element xmlElement ) {
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


    private Log getLog() {
        return LogFactory.getLog(getClass());
    }
}

