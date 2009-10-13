/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html.impl;

import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.Page;

/**
 * Internal interface which defines an input element which contains selectable text. This interface just keeps
 * the various implementations in sync as to selection functionality, and provides a definition of the functionality
 * required by the {@link SelectionDelegate} so that it can do its job. This interface is not public because it is
 * an internal contract.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public interface SelectableTextInput extends Node {

    /**
     * Returns the page which contains this element.
     * @return the page which contains this element
     */
    Page getPage();

    /**
     * Focuses this element.
     */
    void focus();

    /**
     * Focuses this element and selects all of its text.
     */
    void select();

    /**
     * Returns all of the text in this element.
     * @return all of the text in this element
     */
    String getText();

    /**
     * Sets the text in this element.
     * @param text the text to put in this element
     */
    void setText(String text);

    /**
     * Returns the selected text in this element, or <tt>null</tt> if there is no selected text in this element.
     * @return the selected text in this element, or <tt>null</tt> if there is no selected text in this element
     */
    String getSelectedText();

    /**
     * Returns the start position of the selected text in this element.
     * @return the start position of the selected text in this element
     */
    int getSelectionStart();

    /**
     * Sets the start position of the selected text in this element.
     * @param selectionStart the start position of the selected text in this element
     */
    void setSelectionStart(int selectionStart);

    /**
     * Returns the end position of the selected text in this element.
     * @return the end position of the selected text in this element
     */
    int getSelectionEnd();

    /**
     * Sets the end position of the selected text in this element.
     * @param selectionEnd the end position of the selected text in this element
     */
    void setSelectionEnd(int selectionEnd);

}
