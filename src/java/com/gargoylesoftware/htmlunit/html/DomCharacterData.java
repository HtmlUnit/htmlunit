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
package com.gargoylesoftware.htmlunit.html;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Node;

/**
 * Wrapper for the DOM node CharacterData.
 *
 * @version  $Revision$
 * @author  David K. Taylor
 */
public class DomCharacterData extends HtmlElement {

    /**
     * Create an instance of DomCharacterData
     *
     * @param page The HtmlPage that contains this element.
     * @param xmlNode The actual XML character data node that we are wrapping.
     */
    DomCharacterData( final HtmlPage page, final Node xmlNode ) {
        super(page, xmlNode);
    }


    /**
     * Get the data character string for this character data.
     * @return The String of data.
     */
    public Object getData() {
        final CharacterData xmlNode = (CharacterData) getNode();
        return xmlNode.getData();
    }


    /**
     * Set the data character string to the new string.
     * @param newValue The new String of data.
     */
    public void setData( final String newValue ) {
        final CharacterData xmlNode = (CharacterData) getNode();
        xmlNode.setData( newValue );
    }


    /**
     * Get the number of characters in the character data.
     * @return The number of characters.
     */
    public int getLength() {
        final CharacterData xmlNode = (CharacterData) getNode();
        return xmlNode.getLength();
    }


    /**
     * Append a string to character data.
     * @param arg The string to be appended to the character data.
     */
    public void appendData(final String arg) {
        final CharacterData xmlNode = (CharacterData) getNode();
        xmlNode.appendData( arg );
    }


    /**
     * Delete characters from character data.
     * @param offset The position of the first character to be deleted.
     * @param count The number of characters to be deleted.
     */
    public void deleteData(final int offset, final int count) {
        final CharacterData xmlNode = (CharacterData) getNode();
        xmlNode.deleteData( offset, count);
    }


    /**
     * Insert a string into character data.
     * @param offset The position within the first character at which
     * the string is to be inserted.
     * @param arg The string to insert.
     */
    public void insertData(final int offset, final String arg) {
        final CharacterData xmlNode = (CharacterData) getNode();
        xmlNode.insertData( offset, arg);
    }


    /**
     * Replace characters of character data with a string.
     * @param offset The position within the first character at which
     * the string is to be replaced.
     * @param count The number of characters to be replaced.
     * @param arg The string that replaces the count characters beginning at
     * the character at offset.
     */
    public void replaceData(final int offset, final int count,
        final String arg) {
        final CharacterData xmlNode = (CharacterData) getNode();
        xmlNode.replaceData( offset, count, arg );
    }


    /**
     * Extract a substring from character data.
     * @param offset The position of the first character to be extracted.
     * @param count The number of characters to be extracted.
     * @return A string that consists of the count characters of the
     * character data starting from the character at position offset.
     */
    public String substringData(final int offset, final int count) {
        final CharacterData xmlNode = (CharacterData) getNode();
        return xmlNode.substringData( offset, count );
    }
}
