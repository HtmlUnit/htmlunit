/*
 *  Copyright (C) 2002 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;


/**
 * A handler for javascript window.prompt().  Confirms are triggered when the javascript
 * method Window.prompt() is called.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public interface PromptHandler {
    /**
     * Handle a call to Window.prompt() for the given page.
     * @param page The page on which the prompt occurred.
     * @param message The message in the prompt.
     * @return The value typed in or null if the user pressed cancel.
     */
    String handlePrompt( final Page page, final String message );
}
