/*
 * Copyright (c) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.html.DomCharacterData;

/**
 * A javascript object for CharacterData.
 *
 * NOTE: This is derived from HTMLElement due to only partial support
 * for DOM in the html package.  HtmlElement is the closest class to
 * the base Node class.  You should not rely on this incorrect derivation
 * by using HTMLElement methods for CharacterData or its derived classes
 * like Text.
 *
 * @version  $Revision$
 * @author David K. Taylor
 */
public abstract class CharacterDataImpl extends HTMLElement {

    /**
     * Create an instance.  Javascript objects must have a default constructor.
     */
    public CharacterDataImpl() {
    }


    /**
     * Get the JavaScript property "data" for this character data.
     * @return The String of data.
     */
    public Object jsGet_data() {
        final DomCharacterData domCharacterData =
            (DomCharacterData) getHtmlElementOrDie();
        return domCharacterData.getData();
    }


    /**
     * Set the JavaScript property "data" for this character data.
     * @param The new String of data.
     */
    public void jsSet_data( final String newValue ) {
        final DomCharacterData domCharacterData =
            (DomCharacterData) getHtmlElementOrDie();
        domCharacterData.setData(newValue);
    }


    /**
     * Get the number of character in the character data.
     * @return The number of characters.
     */
    public int jsGet_length() {
        final DomCharacterData domCharacterData =
            (DomCharacterData) getHtmlElementOrDie();
        return domCharacterData.getLength();
    }


    /**
     * Append a string to character data.
     * @param arg The string to be appended to the character data.
     */
    public void jsFunction_appendData(final String arg) {
        final DomCharacterData domCharacterData =
            (DomCharacterData) getHtmlElementOrDie();
        domCharacterData.appendData(arg);
    }


    /**
     * Delete characters from character data.
     * @param offset The position of the first character to be deleted.
     * @param count The number of characters to be deleted.
     */
    public void jsFunction_deleteData(final int offset, final int count) {
        final DomCharacterData domCharacterData =
            (DomCharacterData) getHtmlElementOrDie();
        domCharacterData.deleteData(offset, count);
    }


    /**
     * Insert a string into character data.
     * @param offset The position within the first character at which
     * the string is to be inserted.
     * @param arg The string to insert.
     */
    public void jsFunction_insertData(final int offset, final String arg) {
        final DomCharacterData domCharacterData =
            (DomCharacterData) getHtmlElementOrDie();
        domCharacterData.insertData(offset, arg);
    }


    /**
     * Replace characters of character data with a string.
     * @param offset The position within the first character at which
     * the string is to be replaced.
     * @param count The number of characters to be replaced.
     * @param arg The string that replaces the count characters beginning at
     * the character at offset.
     */
    public void jsFunction_replaceData(final int offset, final int count,
        final String arg) {
        final DomCharacterData domCharacterData =
            (DomCharacterData) getHtmlElementOrDie();
        domCharacterData.replaceData(offset, count, arg);
    }


    /**
     * Extract a substring from character data.
     * @param offset The position of the first character to be extracted.
     * @param count The number of characters to be extracted.
     * @return A string that consists of the count characters of the
     * character data starting from the character at position offset.
     */
    public String jsFunction_substringData(final int offset,
        final int count) {
        final DomCharacterData domCharacterData =
            (DomCharacterData) getHtmlElementOrDie();
        return domCharacterData.substringData(offset, count);
    }
}
