/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;
import org.mozilla.javascript.Scriptable;

/**
 * A javascript object for a Style
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class Style extends SimpleScriptable {
    private HTMLElement jsElement_;

    /**
     * Create an instance.  Javascript objects must have a default constructor.
     */
    public Style() {
    }


    /**
     * Initialize the object
     * @param htmlElement The element that this style describes
     */
    public void initialize( final HTMLElement htmlElement ) {
        Assert.assertNotNull("htmlElement", htmlElement);
        jsElement_ = htmlElement;
    }


     /**
      * Return the specified property or NOT_FOUND if it could not be found.
      * @param name The name of the property
      * @param start The scriptable object that was originally queried for this property
      * @return The property.
      */
     public Object get( final String name, final Scriptable start ) {
         final Object result = super.get(name, start);

         // We only handle the logic here if 1) we have been fully initialized and 2) the
         // superclass wasn't able to find anything with the matching name.
         if( jsElement_ == null || result != NOT_FOUND ) {
             return result;
         }

         final Object value = getStyleMap().get(name);
         if( value == null ) {
             return NOT_FOUND;
         }
         else {
             return value;
         }
     }


     /**
      * Set the specified property
      * @param name The name of the property
      * @param start The scriptable object that was originally invoked for this property
      * @param newValue The new value
      */
     public void put( final String name, final Scriptable start, Object newValue ) {
         // Some calls to put will happen during the initialization of the superclass.
         // At this point, we don't have enough information to do our own initialization
         // so we have to just pass this call through to the superclass.
         if( jsElement_ == null ) {
             super.put(name, start, newValue);
             return;
         }

         final Map styleMap = getStyleMap();
         styleMap.put( name, newValue );

         final StringBuffer buffer = new StringBuffer();

         final Iterator iterator = styleMap.entrySet().iterator();
         while( iterator.hasNext() ) {
             final Map.Entry entry = (Map.Entry)iterator.next();
             buffer.append( entry.getKey() );
             buffer.append( ": " );
             buffer.append( entry.getValue() );
             buffer.append( "; " );
         }
         jsElement_.getHtmlElementOrDie().getElement().setAttribute("style", buffer.toString());
     }


     private Map getStyleMap() {
         // This must be a SortedMap so that the tests get results back in a defined order.
         final SortedMap styleMap = new TreeMap();

         final String styleAttribute = jsElement_.getHtmlElementOrDie().getAttributeValue("style");
         final StringTokenizer tokenizer = new StringTokenizer(styleAttribute, ";");
         while( tokenizer.hasMoreTokens() ) {
             final String token = tokenizer.nextToken();
             final int index = token.indexOf(":");
             if( index != -1 ) {
                 final String key = token.substring(0,index).trim();
                 final String value = token.substring(index+1).trim();

                 styleMap.put( key, value );
             }
         }

         return styleMap;
     }
}

