/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;



/**
 * A JavaScript object for a Comment.
 *
 * JavaScript: in IE, Comment is Element, but in FF: Comment is CharacterDataImpl.
 *
 * However, in DOM, Comment is CharacterDataImpl.
 *
 * @version $Revision$
 * @author Mirko Friedenhagen
 * @author Ahmed Ashour
 */
public final class Comment extends CharacterDataImpl {

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public Comment() {
    }

    /**
     * Returns the element ID.
     * @return the ID of this element
     */
    public String jsxGet_id() {
        return "";
    }

    /**
     * Returns the class defined for this element.
     * @return the class name
     */
    public Object jsxGet_className() {
        return "";
    }

    /**
     * Returns the tag name of this element.
     * @return the tag name
     */
    public Object jsxGet_tagName() {
        return "!";
    }

    /**
     * Returns the text of this element.
     * @return the text
     */
    public String jsxGet_text() {
        return "<!--" + jsxGet_data() + "-->";
    }

    /**
     * Returns the document of this element.
     * @return the document
     */
    public Object jsxGet_document() {
        return getWindow().jsxGet_document();
    }
}
