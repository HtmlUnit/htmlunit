/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

/**
 * Exception to indicate that no {@link WebWindow} could be found that matched
 * a given name.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class WebWindowNotFoundException extends RuntimeException {
    private final String name_;


    /**
     * Create an instance
     * @param name The name that was searched by.
     */
    public WebWindowNotFoundException( final String name ) {
        super("Searching for ["+name+"]");
        name_ = name;
    }


    /**
     * Return the name of the {@link WebWindow} that wasn't found
     * @return The name
     */
    public String getName() {
        return name_;
    }
}
