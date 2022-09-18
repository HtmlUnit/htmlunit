/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * A handler for JavaScript Window.print()
 * (<a href="https://html.spec.whatwg.org/multipage/timers-and-user-prompts.html#printing">Printing Spec</a>).
 * All js execution on page containing the document is blocked during the execution of the print method.
 * <p>If the {@link PrintHandler} for the {@link WebClient} is null Window.print() will be a nopp including
 * not triggering any print events.</p>
 *
 * @author Ronald Brill
 */
public interface PrintHandler extends Serializable {

    /**
     * Handle a call to Window.print().
     * @param page the {@link HtmlPage} to print
     */
    void handlePrint(HtmlPage page);
}
