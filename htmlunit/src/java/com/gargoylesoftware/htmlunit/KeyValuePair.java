/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

/**
 *  A container that holds a key and a value
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class KeyValuePair extends org.apache.commons.httpclient.NameValuePair {

    /**
     *  Create an instance
     *
     * @param  key A key
     * @param  value a Value
     */
    public KeyValuePair( final String key, final String value ) {
        super( key, value );
    }


    /**
     *  Return the key
     *
     * @return  Return the key
     */
    public String getKey() {
        return getName();
    }


    /**
     *  Return the value
     *
     * @return  Return the value
     */
    public String getValue() {
        return super.getValue();
    }


    /**
     * Return a string representation of this object
     * @return a string representation of this object
     */
    public String toString() {
        return "KeyValuePair(\""+getKey()+"\", \""+getValue()+"\")";
    }
}

