/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

/**
 *  A collection of constants that represent the various ways a page can be
 *  submitted
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class SubmitMethod {
    /**
     *  POST
     */
    public static final SubmitMethod POST = new SubmitMethod( "post" );
    /**
     *  GET
     */
    public static final SubmitMethod GET = new SubmitMethod( "get" );

    private final String name_;


    private SubmitMethod( final String name ) {
        name_ = name;
    }


    /**
     *  Return the name of this SubmitMethod
     *
     * @return  See above
     */
    public String getName() {
        return name_;
    }


    /**
     *  Return the constant that matches the given name
     *
     * @param  name The name to search by
     * @return  See above
     */
    public static SubmitMethod getInstance( final String name ) {
        final String lowerCaseName = name.toLowerCase();
        final SubmitMethod allInstances[] = new SubmitMethod[]{POST, GET};

        int i;
        for( i = 0; i < allInstances.length; i++ ) {
            if( allInstances[i].getName().equals( lowerCaseName ) ) {
                return allInstances[i];
            }
        }

        // Special case: empty string defaults to get
        if( name.equals( "" ) ) {
            return GET;
        }

        throw new IllegalArgumentException( "No method found for [" + name + "]" );
    }


    /**
     *  Return a string representation of this object
     *
     * @return  See above
     */
    public String toString() {
        return "SubmitMethod[name=" + getName() + "]";
    }
}

