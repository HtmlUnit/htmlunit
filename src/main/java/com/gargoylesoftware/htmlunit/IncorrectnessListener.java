/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
 * Interface to receive notification of incorrect information in HTML code
 * (but not the parser messages), headers, ...
 * that HtmlUnit can handle but that denote a badly written application.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public interface IncorrectnessListener {
    /**
     * Called to notify an incorrectness.
     * @param message the explanation of the incorrectness
     * @param origin the object that encountered this incorrectness
     */
    void notify(final String message, final Object origin);
}
