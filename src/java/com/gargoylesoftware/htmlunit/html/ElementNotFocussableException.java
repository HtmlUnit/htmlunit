/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

/**
 * Thrown when an attempt is made to set the focus to an element that cannot recieve the focus.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class ElementNotFocussableException extends RuntimeException {
    /**
     * Create an instance.
     *
     * @param element The element on which the focus change was attempted.
     */
    public ElementNotFocussableException( final HtmlElement element ) {
    }
}
