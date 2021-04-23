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
package com.gargoylesoftware.htmlunit.html.parser;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
 *
 * <p>Interface for the parser.</p>
 *
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author David K. Taylor
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ethan Glasser-Camp
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Frank Danek
 * @author Carsten Steul
 */
public interface HTMLParserDOMBuilder {

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Parses and then inserts the specified HTML content into the HTML content currently being parsed.
     * @param html the HTML content to push
     */
    void pushInputString(String html);
}
