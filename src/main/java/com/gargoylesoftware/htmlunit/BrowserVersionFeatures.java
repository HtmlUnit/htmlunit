/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
 * @author Ronald Brill
 */
public enum BrowserVersionFeatures {

    /**
     * If the "href" attribute of HtmlAnchor is defined but empty then IE interprets this as an empty filename.
     * Example: The page http://htmlunit.sourceforge.net/test/myFile.html contains an anchor with an empty
     * href attribute. Clicking the link in IE force the load of page http://htmlunit.sourceforge.net/test/.
     * In Firefox the url is unchanged.
     */
    ANCHOR_EMPTY_HREF_NO_FILENAME,

    /** Indicates that a blur event should be triggered before an onchange event. */
    BLUR_BEFORE_ONCHANGE,

    /** If the "type" attribute of HtmlButton should be evaluated to 'button' if not specified. */
    BUTTON_EMPTY_TYPE_BUTTON,

    /** Is canvas supported? */
    CANVAS,

    /** Indicates that the browser can inherit CSS property values. */
    CAN_INHERIT_CSS_PROPERTY_VALUES,

    /** Indicates that document.createEvent() initializes the target property. This is what FF2 does but not FF3. */
    CREATEEVENT_INITALIZES_TARGET,

    /** Was originally .isFirefox(). */
    CSS_DISPLAY_DEFAULT,

    /** */
    DIALOGWINDOW_REFERER,

    /** Indicates that "\n" are replaced by "\r\n" in textarea values. */
    DISPLAYED_COLLAPSE,

    /** */
    DOCTYPE_4_0_TRANSITIONAL_STANDARDS,

    /** IE removes all child text nodes, but FF preserves the first. */
    DOM_NORMALIZE_REMOVE_CHILDREN,

    /** Triggers "DOMContentLoaded" event. */
    EVENT_DOM_CONTENT_LOADED,

    /** Triggers "input" event. */
    EVENT_INPUT,

    /** Triggers "onchange" event handler on losing focus. */
    EVENT_ONCHANGE_LOSING_FOCUS,

    /** Triggers "onerror" if external loading of an external javascript failed. */
    EVENT_ONERROR_EXTERNAL_JAVASCRIPT,

    /** Triggers "onload" event if external javascript successfully loaded. */
    EVENT_ONLOAD_EXTERNAL_JAVASCRIPT,

    /** Triggers "onreadystatechange" event. */
    EVENT_ONREADY_STATE_CHANGE,

    /** Triggers "propertychange" event. */
    EVENT_PROPERTY_CHANGE,

    /** Indicates that document.execCommand() should throw an exception when called with an illegal command. */
    EXECCOMMAND_THROWS_ON_WRONG_COMMAND,

    /** */
    FILEINPUT_EMPTY_DEFAULT_VALUE,

    /** Indicates if a form field is directly reachable by its new name once this has been changed. */
    FORMFIELD_REACHABLE_BY_NEW_NAMES,

    /**
     * Special behavior of IE6; the URL submitted by a form with method type 'GET' has
     * a question mark at the end if there are no parameters.
     */
    FORM_SUBMISSION_URL_END_WITH_QUESTIONMARK,

    /** Form submit is done without the hash part of the form action url. */
    FORM_SUBMISSION_URL_WITHOUT_HASH,

    /** Was originally .isIE(). */
    GENERATED_100,

    /** Was originally .isIE(). */
    GENERATED_101,

    /** Was originally .isIE(). */
    GENERATED_102,

    /** Was originally .isIE(). */
    GENERATED_103,

    /** Was originally .isIE(). */
    GENERATED_104,

    /** Was originally .isIE(). */
    GENERATED_105,

    /** Was originally .isIE(). */
    GENERATED_106,

    /** Was originally .isIE(). */
    GENERATED_107,

    /** Was originally .isIE(). */
    GENERATED_108,

    /** Was originally .isIE(). */
    GENERATED_109,

    /** Was originally .isIE(). */
    GENERATED_110,

    /** Was originally .isIE(). */
    GENERATED_111,

    /** Was originally .isIE(). */
    GENERATED_112,

    /** Was originally .isIE(). */
    GENERATED_113,

    /** Was originally .isIE(). */
    GENERATED_116,

    /** Was originally .isIE(). */
    GENERATED_117,

    /** Was originally .isIE(). */
    GENERATED_118,

    /** Was originally .isIE(). */
    GENERATED_119,

    /** Was originally .isIE(). */
    GENERATED_120,

    /** Was originally .isIE(). */
    GENERATED_121,

    /** Was originally .isIE(). */
    GENERATED_122,

    /** Was originally .isIE(). */
    GENERATED_123,

    /** Was originally .isIE(). */
    GENERATED_124,

    /** Was originally .isIE(). */
    GENERATED_125,

    /** Was originally .isIE(). */
    GENERATED_126,

    /** Was originally .isIE(). */
    GENERATED_127,

    /** Was originally .isIE(). */
    GENERATED_128,

    /** Was originally .isIE(). */
    GENERATED_129,

    /** Was originally .isIE(). */
    GENERATED_13,

    /** Was originally .isIE(). */
    GENERATED_130,

    /** Was originally .isIE(). */
    GENERATED_131,

    /** Was originally .isIE(). */
    GENERATED_132,

    /** Was originally .isIE(). */
    GENERATED_133,

    /** Was originally .isIE(). */
    GENERATED_134,

    /** Was originally .isIE(). */
    GENERATED_135,

    /** Was originally .isIE(). */
    GENERATED_136,

    /** Was originally .isIE(). */
    GENERATED_137,

    /** Was originally .isIE(). */
    GENERATED_138,

    /** Was originally .isIE(). */
    GENERATED_140,

    /** Was originally .isIE(). */
    GENERATED_141,

    /** Was originally .isIE(). */
    GENERATED_142,

    /** Was originally .isIE(). */
    GENERATED_143,

    /** Was originally .isIE(). */
    GENERATED_144,

    /** Was originally .isIE(). */
    GENERATED_146,

    /** Was originally .isIE(). */
    GENERATED_147,

    /** Was originally .isIE(). */
    GENERATED_148,

    /** Was originally .isIE(). */
    GENERATED_15,

    /** Was originally .isIE(). */
    GENERATED_150,

    /** Was originally .isFirefox(). */
    GENERATED_151,

    /** Was originally .isFirefox(). */
    GENERATED_153,

    /** Was originally .isFirefox(). */
    GENERATED_154,

    /** Was originally .isFirefox(). */
    GENERATED_155,

    /** Was originally .isFirefox(). */
    GENERATED_156,

    /** Was originally .isFirefox(). */
    GENERATED_157,

    /** Was originally .isFirefox(). */
    GENERATED_158,

    /** Was originally .isIE(). */
    GENERATED_16,

    /** Was originally .isFirefox(). */
    GENERATED_160,

    /** Was originally .isFirefox(). */
    GENERATED_161,

    /** Was originally .isFirefox(). */
    GENERATED_162,

    /** Was originally .isFirefox(). */
    GENERATED_163,

    /** Was originally .isFirefox(). */
    GENERATED_164,

    /** Was originally .isFirefox(). */
    GENERATED_165,

    /** Was originally .isFirefox(). */
    GENERATED_166,

    /** Was originally .isFirefox(). */
    GENERATED_167,

    /** Was originally .isFirefox(). */
    GENERATED_168,

    /** Was originally .isFirefox(). */
    GENERATED_169,

    /** Was originally .isIE(). */
    GENERATED_17,

    /** Was originally .isFirefox(). */
    GENERATED_170,

    /** Was originally .isFirefox(). */
    GENERATED_172,

    /** Was originally .isFirefox(). */
    GENERATED_173,

    /** Was originally .isFirefox(). */
    GENERATED_174,

    /** Was originally .isFirefox(). */
    GENERATED_176,

    /** Was originally .isFirefox(). */
    GENERATED_177,

    /** Was originally .isIE(). */
    GENERATED_18,

    /** Was originally .isIE(). */
    GENERATED_2,

    /** Was originally .isIE(). */
    GENERATED_21,

    /** Was originally .isIE(). */
    GENERATED_22,

    /** Was originally .isIE(). */
    GENERATED_23,

    /** Was originally .isIE(). */
    GENERATED_24,

    /** Was originally .isIE(). */
    GENERATED_25,

    /** Was originally .isIE(). */
    GENERATED_26,

    /** Was originally .isIE(). */
    GENERATED_27,

    /** Was originally .isIE(). */
    GENERATED_28,

    /** Was originally .isIE(). */
    GENERATED_29,

    /** Was originally .isIE(). */
    GENERATED_3,

    /** Was originally .isIE(). */
    GENERATED_30,

    /** Was originally .isIE(). */
    GENERATED_31,

    /** Was originally .isIE(). */
    GENERATED_32,

    /** Was originally .isIE(). */
    GENERATED_33,

    /** Was originally .isIE(). */
    GENERATED_34,

    /** Was originally .isIE(). */
    GENERATED_35,

    /** Was originally .isIE(). */
    GENERATED_36,

    /** Was originally .isIE(). */
    GENERATED_37,

    /** Was originally .isIE(). */
    GENERATED_38,

    /** Was originally .isIE(). */
    GENERATED_39,

    /** Was originally .isIE(). */
    GENERATED_4,

    /** Was originally .isIE(). */
    GENERATED_40,

    /** Was originally .isIE(). */
    GENERATED_41,

    /** Was originally .isIE(). */
    GENERATED_42,

    /** Was originally .isIE(). */
    GENERATED_43,

    /** Was originally .isIE(). */
    GENERATED_44,

    /** Was originally .isIE(). */
    GENERATED_45,

    /** Was originally .isIE(). */
    GENERATED_46,

    /** Was originally .isIE(). */
    GENERATED_47,

    /** Was originally .isIE(). */
    GENERATED_48,

    /** Was originally .isIE(). */
    GENERATED_49,

    /** Was originally .isIE(). */
    GENERATED_5,

    /** Was originally .isIE(). */
    GENERATED_50,

    /** Was originally .isIE(). */
    GENERATED_51,

    /** Was originally .isIE(). */
    GENERATED_52,

    /** Was originally .isIE(). */
    GENERATED_53,

    /** Was originally .isIE(). */
    GENERATED_54,

    /** Was originally .isIE(). */
    GENERATED_55,

    /** Was originally .isIE(). */
    GENERATED_56,

    /** Was originally .isIE(). */
    GENERATED_57,

    /** Was originally .isIE(). */
    GENERATED_58,

    /** Was originally .isIE(). */
    GENERATED_59,

    /** Was originally .isIE(). */
    GENERATED_6,

    /** Was originally .isIE(). */
    GENERATED_60,

    /** Was originally .isIE(). */
    GENERATED_61,

    /** Was originally .isIE(). */
    GENERATED_62,

    /** Was originally .isIE(). */
    GENERATED_63,

    /** Was originally .isIE(). */
    GENERATED_64,

    /** Was originally .isIE(). */
    GENERATED_65,

    /** Was originally .isIE(). */
    GENERATED_66,

    /** Was originally .isIE(). */
    GENERATED_67,

    /** Was originally .isIE(). */
    GENERATED_68,

    /** Was originally .isIE(). */
    GENERATED_69,

    /** Was originally .isIE(). */
    GENERATED_7,

    /** Was originally .isIE(). */
    GENERATED_70,

    /** Was originally .isIE(). */
    GENERATED_71,

    /** Was originally .isIE(). */
    GENERATED_72,

    /** Was originally .isIE(). */
    GENERATED_73,

    /** Was originally .isIE(). */
    GENERATED_74,

    /** Was originally .isIE(). */
    GENERATED_75,

    /** Was originally .isIE(). */
    GENERATED_76,

    /** Was originally .isIE(). */
    GENERATED_77,

    /** Was originally .isIE(). */
    GENERATED_78,

    /** Was originally .isIE(). */
    GENERATED_79,

    /** Was originally .isIE(). */
    GENERATED_8,

    /** Was originally .isIE(). */
    GENERATED_80,

    /** Was originally .isIE(). */
    GENERATED_81,

    /** Was originally .isIE(). */
    GENERATED_82,

    /** Was originally .isIE(). */
    GENERATED_83,

    /** Was originally .isIE(). */
    GENERATED_84,

    /** Was originally .isIE(). */
    GENERATED_85,

    /** Was originally .isIE(). */
    GENERATED_86,

    /** Was originally .isIE(). */
    GENERATED_87,

    /** Was originally .isIE(). */
    GENERATED_88,

    /** Was originally .isIE(). */
    GENERATED_89,

    /** Was originally .isIE(). */
    GENERATED_90,

    /** Was originally .isIE(). */
    GENERATED_91,

    /** Was originally .isIE(). */
    GENERATED_92,

    /** Was originally .isIE(). */
    GENERATED_93,

    /** Was originally .isIE(). */
    GENERATED_94,

    /** Was originally .isIE(). */
    GENERATED_95,

    /** Was originally .isIE(). */
    GENERATED_96,

    /** Was originally .isIE(). */
    GENERATED_97,

    /** Was originally .isIE(). */
    GENERATED_98,

    /** Was originally .isIE(). */
    GENERATED_99,

    /** */
    HEADER_CONTENT_DISPOSITION_ABSOLUTE_PATH,

    /** Indicates if HTML5 tags source, video and audio are recognized. */
    HTML5_TAGS,

    /** */
    HTMLABBREVIATED,

    /** Allows multiple elements with the same 'id'. */
    HTMLCOLLECTION_IDENTICAL_IDS,

    /**
     * Supports Conditional Comments.
     * @see <a href="http://en.wikipedia.org/wiki/Conditional_comment">Conditional comment</a>
     */
    HTMLCONDITIONAL_COMMENTS,

    /** Allows invalid 'align' values. */
    HTMLELEMENT_ALIGN_INVALID,

    /** */
    HTMLELEMENT_TRIM_CLASS_ATTRIBUTE,

    /** */
    HTMLIMAGE_NAME_VALUE_PARAMS,

    /** */
    HTMLINPUT_DEFAULT_IS_CHECKED,

    /**
     * Set this property if the browser does NOT
     * support the disabling of an individual option group.
     */
    HTMLOPTIONGROUP_NO_DISABLED,

    /** */
    HTMLOPTION_EMPTY_TEXT_IS_NO_CHILDREN,

    /** */
    HTMLOPTION_PREVENT_DISABLED,

    /** Un-selecting an option in a (single-value) select causes the first option to become selected. */
    HTMLOPTION_UNSELECT_SELECTS_FIRST,

    /**
     * Set this property if the script tag supports the
     * types 'application/javascript' and 'application/x-javascript'.
     */
    HTMLSCRIPT_APPLICATION_JAVASCRIPT,

    /** Runs <script src="javascript:'[code]'">. */
    HTMLSCRIPT_SRC_JAVASCRIPT,

    /** Have a default value of body.link, body.vLink, etc. */
    HTML_BODY_COLOR,

    /** Indicates that "host" HTTP header should be the first. */
    HTTP_HEADER_HOST_FIRST,

    /** Indicates that the browser should ignore contents of inner head elements. */
    IGNORE_CONTENTS_OF_INNER_HEAD,

    /** */
    JAVASCRIPT_DEFERRED,

    /** Javascript function getBackgroundColor of computed styles returns the color as rgb. */
    JAVASCRIPT_GET_BACKGROUND_COLOR_FOR_COMPUTED_STYLE_RETURNS_RGB,

    /** Javascript function getElementById compares the id's case sensitive. */
    JAVASCRIPT_GET_ELEMENT_BY_ID_CASE_SENSITIVE,

    /** Javascript function returning a length (e.g. getWidth) without 'px' at the end. */
    JAVASCRIPT_LENGTH_WITHOUT_PX,

    /**
     * Set this property if the browser evaluates<br>
     * window.location.hash to #&uuml; (like Firefox)<br>
     * for url 'http://localhost/something/#%C3%BC'.<br>
     * IE evaluates to #%C3%BC.
     */
    JAVASCRIPT_LOCATION_HASH_IS_DECODED,

    /** Always "[object]". */
    JAVASCRIPT_OBJECT_ONLY,

    /** Evaluates to "[object Element]". */
    JAVASCRIPT_OBJECT_PREFIX,

    /** Indicates that the URL of parent window is used to resolve URLs in frames with javascript src. */
    JS_FRAME_RESOLVE_URL_WITH_PARENT_WINDOW,

    /** Indicates that objects with prototype property available in window scope; Firefox does this. */
    JS_HAS_OBJECT_WITH_PROTOTYPE_PROPERTY_IN_WINDOW_SCOPE,

    /** Indicates if multiple spaces are replaced by a single one when accessing innerHTML. */
    JS_INNER_HTML_REDUCE_WHITESPACES,

    /** Indicates that someObj.offsetParent throws an exception when called on an object that is not yet attached
     *  to the page's DOM.
     */
    JS_OFFSET_PARENT_THROWS_NOT_ATTACHED,

    /** Indicates if calling HTMLSelectElement.item with a negative value should throw. */
    JS_SELECT_ITEM_THROWS_IF_NEGATIVE,

    /** Indicates if setting an out of bound value for HTMLSelectElement.selectedIndex should throw. */
    JS_SELECT_SELECTED_INDEX_THROWS_IF_BAD,

    /** Indicates that new XMLSerializer().serializeToString(..) always appends a CRLF at the end
     * of the produced string. */
    JS_XML_SERIALIZER_APPENDS_CRLF,

    /** Indicates that new XMLSerializer().serializeToString(..) transforms the node name to upper case. */
    JS_XML_SERIALIZER_NODE_AS_UPPERCASE,

    /** Body of a &lt;noscript&gt; tag is not totally ignored but considered as a (not displayed) text node. */
    NOSCRIPT_BODY_AS_TEXT,

    /** */
    PAGE_SELECTION_RANGE_FROM_SELECTABLE_TEXT_INPUT,

    /** Wait for the whole page to load before initializing bodies for frames. */
    PAGE_WAIT_LOAD_BEFORE_BODY,

    /** Supports 'data' protocol. */
    PROTOCOL_DATA,

    /** Indicates .querySelectorAll() is not supported in quirks mode. */
    QUERYSELECTORALL_NOT_IN_QUIRKS,

    /**
     * Indicates that all options of a select are deselected,
     * if the select state is changed for an unknown option.
     */
    SELECT_DESELECT_ALL_IF_SWITCHING_UNKNOWN,

    /** Indicates that a read only JS property can be... set as done by IE and FF2 but not FF3. */
    SET_READONLY_PROPERTIES,

    /** Indicates [object StorageObsolete] instead of [object Storage]. */
    STORAGE_OBSOLETE,

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

    /** Set the value attribute of a submit input to 'Submit Query' if no value attribute specified. */
    SUBMITINPUT_DEFAULT_VALUE_IF_VALUE_NOT_DEFINED,

    /** Indicates that "\n" are replaced by "\r\n" in textarea values. */
    TEXTAREA_CRNL,

    /** Indicates that the browser treats "position: fixed" as if it were "position: static". */
    TREATS_POSITION_FIXED_LIKE_POSITION_STATIC,

    /**
     * Indicates, that the pathname for the url 'about:blank' is empty;
     * instead of '/blank'.
     */
    URL_ABOUT_BLANK_HAS_EMPTY_PATH,

    /** Replace only ' ' with %20 when encode the query part of an url. */
    URL_MINIMAL_QUERY_ENCODING,

    /** */
    URL_MISSING_SLASHES,

    /** */
    WINDOW_ACTIVE_ELEMENT_FOCUSED,

    /** Indicates that 'this' corresponds to the handler function when a XMLHttpRequest handler is executed. */
    XMLHTTPREQUEST_HANDLER_THIS_IS_FUNCTION;

}
