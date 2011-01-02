/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit;

/**
 * A handler for JavaScript window.prompt(). Confirms are triggered when the JavaScript
 * method Window.prompt() is called.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public interface PromptHandler {
    /**
     * Handle a call to Window.prompt() for the given page.
     * @param page the page on which the prompt occurred
     * @param message the message in the prompt
     * @return the value typed in or null if the user pressed cancel
     */
    String handlePrompt(final Page page, final String message);
}
