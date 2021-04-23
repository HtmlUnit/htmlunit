/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit;

import java.io.Serializable;

/**
 * A handler for JavaScript window.prompt(). Prompts are triggered when the JavaScript
 * method Window.prompt() is called.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public interface PromptHandler extends Serializable {

    /**
     * Handle a call to Window.prompt() for the given page.
     * @param page the page on which the prompt occurred
     * @param message the message in the prompt
     * @param defaultValue the default value in the prompt
     * @return the value typed in or {@code null} if the user pressed {@code cancel}
     */
    String handlePrompt(Page page, String message, String defaultValue);
}
