/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
 * Constants of various features of each {@link BrowserVersion}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Sudhan Moghe
 */
public enum BrowserVersionFeatures {

    /** Indicates that a blur event should be triggered before an onchange event. */
    BLUR_BEFORE_ONCHANGE,

    /** If the "type" attribute of HtmlButton should be evaluated to 'button' if not specified. */
    BUTTON_EMPTY_TYPE_BUTTON,

    /** Indicates that the browser can inherit CSS property values. */
    CAN_INHERIT_CSS_PROPERTY_VALUES,

    /** Indicates that document.createEvent() initializes the target property. This is what FF2 does but not FF3. */
    CREATEEVENT_INITALIZES_TARGET,

    /** */
    DIALOGWINDOW_REFERER,

    /** Indicates that "\n" are replaced by "\r\n" in textarea values. */
    DISPLAYED_COLLAPSE,

    /** IE removes all child text nodes, but FF preserves the first. */
    DOM_NORMALIZE_REMOVE_CHILDREN,

    /** Triggers "DOMContentLoaded" event. */
    EVENT_DOM_CONTENT_LOADED,

    /** Triggers "input" event. */
    EVENT_INPUT,

    /** Triggers "propertychange" event. */
    EVENT_PROPERTY_CHANGE,

    /** Indicates that document.execCommand() should throw an exception when called with an illegal command. */
    EXECCOMMAND_THROWS_ON_WRONG_COMMAND,

    /** */
    FILEINPUT_EMPTY_DEFAULT_VALUE,

    /** */
    FORM_SUBMISSION_URL_END_WITH_QUESTIONMARK,

    /** */
    HEADER_CONTENT_DISPOSITION_ABSOLUTE_PATH,

    /** */
    HTMLOPTIONGROUP_NO_DISABLED,

    /** */
    HTMLOPTION_EMPTY_TEXT_IS_NO_CHILDREN,

    /** */
    HTMLOPTION_PREVENT_DISABLED,

    /** Indicates that the browser should ignore contents of inner head elements. */
    IGNORE_CONTENTS_OF_INNER_HEAD,

    /** */
    JS_DEFERRED,

    /** Indicates that the URL of parent window is used to resolve URLs in frames with javascript src. */
    JS_FRAME_RESOLVE_URL_WITH_PARENT_WINDOW,

    /** Indicates .querySelectorAll() is supported in only quirks mode. */
    QUERYSELECTORALL_QUIRKS,

    /** Indicates that a read only JS property can be... set as done by IE and FF2 but not FF3. */
    SET_READONLY_PROPERTIES,

    /** Indicates that string.trim(), .trimLeft() and .trimRight() are supported. */
    STRING_TRIM,

    /**
     * Indicates that the href property for a &lt;link rel="stylesheet" type="text/css" href="..." /&gt;
     * is the fully qualified URL.
     */
    STYLESHEET_HREF_EXPANDURL,

    /** Indicates that the href property for a &lt;style type="text/css"&gt; ... &lt;/style&gt; is "". */
    STYLESHEET_HREF_STYLE_EMPTY,

    /** Indicates that the href property for a &lt;style type="text/css"&gt; ... &lt;/style&gt; is null. */
    STYLESHEET_HREF_STYLE_NULL,

    /** Indicates that "\n" are replaced by "\r\n" in textarea values. */
    TEXTAREA_CRNL,

    /** Indicates that the browser treats "position: fixed" as if it were "position: static". */
    TREATS_POSITION_FIXED_LIKE_POSITION_STATIC,

    /** */
    URL_MINIMAL_QUERY_ENCODING,

    /** */
    WINDOW_ACTIVE_ELEMENT_FOCUSED,

    /** Indicates that 'this' corresponds to the handler function when a XMLHttpRequest handler is executed. */
    XMLHTTPREQUEST_HANDLER_THIS_IS_FUNCTION;

}
