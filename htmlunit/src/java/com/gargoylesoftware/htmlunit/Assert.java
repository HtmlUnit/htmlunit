/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

/**
 *  Utility methods for performing runtime assertions.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public final class Assert {
    /**
     * Assert that the specified parameter is not null.  Throw a NullPointerException
     * if a null is found.
     * @param description The description to pass into the NullPointerException
     * @param object The object to check for null.
     */
    public static final void notNull( final String description, final Object object ) {
        if( object == null ) {
            throw new NullPointerException(description);
        }
    }
}
