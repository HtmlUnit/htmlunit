/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

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
            getLog().error("Exception when getting constructor for class["+clazz.getName()+"]",e);
            throw new IllegalStateException(
                "Unable to get constuctor for class ["+clazz.getName()+"].  See log for details");
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
            getLog().error("IllegalAccessException when calling constructor["+constructor_+"]",e);
            throw new IllegalStateException(
                "IllegalAccessException when calling constructor ["+constructor_
                +"].  See log for details");
        }
        catch( final InstantiationException e ) {
            getLog().error("InstantiationException when calling constructor["+constructor_+"]",e);
            throw new IllegalStateException(
                "InstantiationException when calling constructor ["+constructor_
                +"].  See log for details");
        }
        catch( final InvocationTargetException e ) {
            getLog().error(
                "InvocationTargetException when calling constructor["+constructor_
                +"]",e.getTargetException());
            throw new IllegalStateException(
                "InvocationTargetException when calling constructor ["+constructor_
                +"].  See log for details");
        }
    }


    private Log getLog() {
        return LogFactory.getLog(getClass());
    }
}

