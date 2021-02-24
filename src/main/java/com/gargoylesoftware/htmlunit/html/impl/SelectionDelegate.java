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
package com.gargoylesoftware.htmlunit.html.impl;

import java.io.Serializable;

/**
 * Contains selection-related functionality used by elements.
 *
 * @author Ahmed Ashour
 */
public interface SelectionDelegate extends Serializable {

    /**
     * Returns the start position of the selected text in the owner element.
     * @return the start position of the selected text in the owner element
     */
    int getSelectionStart();

    /**
     * Sets the start position of the selected text in the owner element.
     * @param selectionStart the start position of the selected text in the owner element
     */
    void setSelectionStart(int selectionStart);

    /**
     * Returns the end position of the selected text in the owner element.
     * @return the end position of the selected text in the owner element
     */
    int getSelectionEnd();

    /**
     * Sets the end position of the selected text in the owner element.
     * @param selectionEnd the end position of the selected text in the owner element
     */
    void setSelectionEnd(int selectionEnd);
}
