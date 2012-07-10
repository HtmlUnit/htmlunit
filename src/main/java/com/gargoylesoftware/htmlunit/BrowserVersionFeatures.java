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

    /** Indicates that the default value for height of elements is 15 instead of 20. */
    CSS_DEFAULT_ELMENT_HEIGHT_15,

    /** Indicates that the default value for height of elements is used instance
     * of the calculated value, if the calculated value is smaller. */
    CSS_DEFAULT_ELMENT_HEIGHT_MARKS_MIN,

    /** Indicates that the default value for width is 'auto'. */
    CSS_DEFAULT_WIDTH_AUTO,

    /** Was originally .isFirefox(). */
    CSS_DISPLAY_DEFAULT,

    /** Default is 'normal'. */
    CSS_FONT_STRECH_DEFAULT_NORMAL,

    /** Indicates that the browser can surrounds image url's with quotes. */
    CSS_IMAGE_URL_QUOTED,

    /** Indicates that only integers are allowed for pixel value. */
    CSS_PIXEL_VALUES_INT_ONLY,

    /** Indicates that the :lang(..) selector is supported. */
    CSS_SELECTOR_LANG,

    /**
     * Indicates that the pseudo classes 'root', 'enabled', 'disabled'
     * and 'checked' are supported.
     */
    CSS_SPECIAL_PSEUDO_CLASSES,

    /** Internet Explorer versions 5 and later support the behavior property. The behavior property lets
     * you use CSS to attach a script to a specific element in order to implement
     * DHTML (Dynamic HTML) components.
     */
    CSS_SUPPORTS_BEHAVIOR_PROPERTY,

    /** Default is 'none'. */
    CSS_TEXT_SHADOW_DEFAULT_NONE,

    /** Default is 'normal'. */
    CSS_UNICODE_BIDI_DEFAULT_NORMAL,

    /** Default is 'normal'. */
    CSS_WORD_SPACING_DEFAULT_NORMAL,

    /** Values for the zIndex are rounded to integer. */
    CSS_ZINDEX_ROUNDED,

    /** IE uses the type Number for the zIndex Values (instead of String). */
    CSS_ZINDEX_TYPE_NUMBER,

    /** If values for the zIndex is undefined than set the zindex to the default value. */
    CSS_ZINDEX_UNDEFINED_FORCES_RESET,

    /** If values for the zIndex is undefined or null than set the zindex throws an error. */
    CSS_ZINDEX_UNDEFINED_OR_NULL_THROWS_ERROR,

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

    /** Triggers "onclick" event handler also for the select option. */
    EVENT_ONCLICK_FOR_SELECT_OPTION_ALSO,

    /** Triggers "onerror" if external loading of an external javascript failed. */
    EVENT_ONERROR_EXTERNAL_JAVASCRIPT,

    /** Triggers "onload" event if external javascript successfully loaded. */
    EVENT_ONLOAD_EXTERNAL_JAVASCRIPT,

    /** Triggers "onload" event of the frameset before the one from the frames. */
    EVENT_ONLOAD_FRAMESET_FIRST,

    /** Triggers "onload" event if an iframe was created by javascript and added to the page. */
    EVENT_ONLOAD_IFRAME_CREATED_BY_JAVASCRIPT,

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
    GENERATED_111,

    /** Was originally .isIE(). */
    GENERATED_112,

    /** Was originally .isIE(). */
    GENERATED_113,

    /** Was originally .isIE(). */
    GENERATED_116,

    /** Was originally .isIE(). */
    GENERATED_121,

    /** Was originally .isIE(). */
    GENERATED_124,

    /** Was originally .isIE(). */
    GENERATED_125,

    /** Was originally .isIE(). */
    GENERATED_129,

    /** Was originally .isIE(). */
    GENERATED_13,

    /** Was originally .isIE(). */
    GENERATED_133,

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
    GENERATED_150,

    /** Was originally .isFirefox(). */
    GENERATED_155,

    /** Was originally .isFirefox(). */
    GENERATED_156,

    /** Was originally .isFirefox(). */
    GENERATED_157,

    /** Was originally .isFirefox(). */
    GENERATED_158,

    /** Was originally .isFirefox(). */
    GENERATED_160,

    /** Was originally .isFirefox(). */
    GENERATED_161,

    /** Was originally .isFirefox(). */
    GENERATED_162,

    /** Was originally .isFirefox(). */
    GENERATED_164,

    /** Was originally .isFirefox(). */
    GENERATED_165,

    /** Was originally .isFirefox(). */
    GENERATED_167,

    /** Was originally .isFirefox(). */
    GENERATED_169,

    /** Was originally .isIE(). */
    GENERATED_17,

    /** Was originally .isFirefox(). */
    GENERATED_170,

    /** Was originally .isFirefox(). */
    GENERATED_172,

    /** Was originally .isFirefox(). */
    GENERATED_174,

    /** Was originally .isFirefox(). */
    GENERATED_176,

    /** Was originally .isIE(). */
    GENERATED_2,

    /** Was originally .isIE(). */
    GENERATED_21,

    /** Was originally .isIE(). */
    GENERATED_3,

    /** Was originally .isIE(). */
    GENERATED_30,

    /** Was originally .isIE(). */
    GENERATED_31,

    /** Was originally .isIE(). */
    GENERATED_32,

    /** Was originally .isIE(). */
    GENERATED_35,

    /** Was originally .isIE(). */
    GENERATED_37,

    /** Was originally .isIE(). */
    GENERATED_39,

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
    GENERATED_53,

    /** Was originally .isIE(). */
    GENERATED_55,

    /** Was originally .isIE(). */
    GENERATED_57,

    /** Was originally .isIE(). */
    GENERATED_59,

    /** Was originally .isIE(). */
    GENERATED_6,

    /** Was originally .isIE(). */
    GENERATED_60,

    /** Was originally .isIE(). */
    GENERATED_61,

    /** Was originally .isIE(). */
    GENERATED_63,

    /** Was originally .isIE(). */
    GENERATED_65,

    /** Was originally .isIE(). */
    GENERATED_66,

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
    GENERATED_80,

    /** Was originally .isIE(). */
    GENERATED_81,

    /** Was originally .isIE(). */
    GENERATED_85,

    /** Was originally .isIE(). */
    GENERATED_86,

    /** Was originally .isIE(). */
    GENERATED_88,

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

    /** Allow detection of object type for collection elements. */
    HTMLCOLLECTION_OBJECT_DETECTION,

    /**
     * Supports Conditional Comments.
     * @see <a href="http://en.wikipedia.org/wiki/Conditional_comment">Conditional comment</a>
     */
    HTMLCONDITIONAL_COMMENTS,

    /** Allows invalid 'align' values. */
    HTMLELEMENT_ALIGN_INVALID,

    /** */
    HTMLELEMENT_TRIM_CLASS_ATTRIBUTE,

    /**
     * Indicates if a selfclosing &lt;iframe/&gt; tag should be considered as an opening tag.
     **/
    HTMLIFRAME_IGNORE_SELFCLOSING,

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

    /** Set this checked state back to default when added to page (IE). */
    HTMLRADIOINPUT_SET_CHECKED_TO_DEFAULT_WHEN_ADDED_TO_PAGE,

    /**
     * Set this property if the script tag supports the
     * types 'application/javascript' and 'application/x-javascript'.
     */
    HTMLSCRIPT_APPLICATION_JAVASCRIPT,

    /** Runs <script src="javascript:'[code]'">. */
    HTMLSCRIPT_SRC_JAVASCRIPT,

    /** Trims the value of the type attribute before to verify it. */
    HTMLSCRIPT_TRIM_TYPE,

    /** Have a default value of body.link, body.vLink, etc. */
    HTML_BODY_COLOR,

    /** Indicates that "host" HTTP header should be the first. */
    HTTP_HEADER_HOST_FIRST,

    /** Indicates that the browser should ignore contents of inner head elements. */
    IGNORE_CONTENTS_OF_INNER_HEAD,

    /**
     * The function addEventListener or attachEvent(IE) accepts null as listener
     * instead of throwing an exception.
     */
    JS_ADD_EVENT_LISTENER_ACCEPTS_NULL_LISTENER,

    /** Setting the property align to arbitrary values is allowed. */
    JS_ALIGN_ACCEPTS_ARBITRARY_VALUES,

    /** Setting the property align of an input element ignores the value
     * if the value is one of center, justify, left or right.
     * For all other values an exception is still thrown.
     */
    JS_ALIGN_FOR_INPUT_IGNORES_VALUES,

    /**
     * Javascript property anchors includes all anchors with a name or an id property.
     * If not set name property is required
     */
    JS_ANCHORS_REQUIRES_NAME_OR_ID,

    /** Indicates that the appendChild call create a DocumentFragment to be
     * the parentNode's parentNode if this was null. */
    JS_APPEND_CHILD_CREATE_DOCUMENT_FRAGMENT_PARENT_IF_PARENT_IS_NULL,

    /** Indicates that the appendChild call throws no exception
     * if the provided note cannot be inserted. */
    JS_APPEND_CHILD_THROWS_NO_EXCEPTION_FOR_WRONG_NOTE,

    /** Indicates that the attributes map contains empty attr
     * objects for all properties of the object (like IE does). */
    JS_ATTRIBUTES_CONTAINS_EMPTY_ATTR_FOR_PROPERTIES,

    /** firstChild and lastChild returns null for Attr (like IE does). */
    JS_ATTR_FIRST_LAST_CHILD_RETURNS_NULL,

    /** Indicates that the getBoundingClientRect adds an offset of 2. */
    JS_BOUNDING_CLIENT_RECT_OFFSET_TWO,

    /** Indicates that the browser emulates the char attribute. */
    JS_CHAR_EMULATED,

    /** Indicates that the browser emulates the charOff attribute. */
    JS_CHAR_OFF_EMULATED,

    /** Indicates that the browser tries to convert the char attribute values
     * to integer. */
    JS_CHAR_OFF_INTEGER,

    /** Indicates that the browser returns a dot if the char attribute is not defined. */
    JS_CHAR_UNDEFINED_DOT,

    /** Indicates that the clientLeft and clientTop returning zero in all cases. */
    JS_CLIENT_LEFT_TOP_ZERO,

    /** Indicates that the cloneNode call copies all event listeners. */
    JS_CLONE_NODE_COPIES_EVENT_LISTENERS,

    /** */
    JS_DEFERRED,

    /** Javascript doctyp.entities returns an empty string (IE). */
    JS_DOCTYPE_ENTITIES_EMPTY_STRING,

    /** Javascript doctyp.entities returns null (FF10). */
    JS_DOCTYPE_ENTITIES_NULL,

    /** Javascript doctyp.internalSubset returns an empty string (IE). */
    JS_DOCTYPE_INTERNALSUBSET_EMPTY_STRING,

    /** Javascript doctyp.notations returns an empty string (IE). */
    JS_DOCTYPE_NOTATIONS_EMPTY_STRING,

    /** Javascript doctyp.notations returns null (FF10). */
    JS_DOCTYPE_NOTATIONS_NULL,

    /** Javascript document.appendChild is allowed (IE). */
    JS_DOCUMENT_APPEND_CHILD_SUPPORTED,

    /** Javascript function document.createElement can process html code.
     * e.g. document.createElement("<INPUT TYPE='RADIO' NAME='RADIOTEST' VALUE='First Choice'>")
     * @see "http://msdn.microsoft.com/en-us/library/ms536389%28v=VS.85%29.aspx"
     */
    JS_DOCUMENT_CREATE_ELEMENT_EXTENDED_SYNTAX,

    /** Javascript document.doctype returns null (IE). */
    JS_DOCUMENT_DOCTYPE_NULL,

    /** Javascript property document.domain is lowercase. */
    JS_DOCUMENT_DOMAIN_IS_LOWERCASE,

    /** Javascript document.forms(...) supported (IE). */
    JS_DOCUMENT_FORMS_FUNCTION_SUPPORTED,

    /** Javascript property document.domain
     * doesn't allow to set domain of about:blank.
     */
    JS_DOCUMENT_SETTING_DOMAIN_THROWS_FOR_ABOUT_BLANK,

    /** Enables Javascript ECMA5 functions (like Date.toISOString or Date.toJSON).
     */
    JS_ECMA5_FUNCTIONS,

    /** Javascript calculation of element clientHeight/Width does not
     * include the padding.
     */
    JS_ELEMENT_EXTENT_WITHOUT_PADDING,

    /** Javascript event aborted check is based on the event handler return value (IE);
     * (standards-compliant browsers doing this via preventDefault).
     */
    JS_EVENT_ABORTED_BY_RETURN_VALUE_FALSE,

    /** Javascript event handlers declared as property on a node
     * don't receive the event as argument.
     */
    JS_EVENT_HANDLER_DECLARED_AS_PROPERTY_DONT_RECEIVE_EVENT,

    /** Indicates that the URL of parent window is used to resolve URLs in frames with javascript src. */
    JS_FRAME_RESOLVE_URL_WITH_PARENT_WINDOW,

    /** Indicates if Function.bind is available. */
    JS_FUNCTION_BIND,

    /** Indicates if the method toSource exists on the native objects. */
    JS_FUNCTION_TOSOURCE,

    /** Indicates that the getAttribute method supports ie style flags. */
    JS_GET_ATTRIBUTE_SUPPORTS_FLAGS,

    /** Javascript function getBackgroundColor of computed styles returns the color as rgb. */
    JS_GET_BACKGROUND_COLOR_FOR_COMPUTED_STYLE_RETURNS_RGB,

    /** Javascript function getElementById calls getElementByName if nothing found by id. */
    JS_GET_ELEMENT_BY_ID_ALSO_BY_NAME,

    /** Javascript function getElementById compares the id's case sensitive. */
    JS_GET_ELEMENT_BY_ID_CASE_SENSITIVE,

    /** Indicates that objects with prototype property available in window scope; Firefox does this. */
    JS_HAS_OBJECT_WITH_PROTOTYPE_PROPERTY_IN_WINDOW_SCOPE,

    /** Javascript method getHeight of IFrames may return negative values. */
    JS_IFRAME_GET_HEIGHT_NEGATIVE_VALUES,

    /** Javascript method getWidth of IFrames may return negative values. */
    JS_IFRAME_GET_WIDTH_NEGATIVE_VALUES,

    /** IE ignores the last line containing uncommented. */
    JS_IGNORES_LAST_LINE_CONTAINING_UNCOMMENTED,

    /** Indicates if multiple spaces are replaced by a single one when accessing innerHTML. */
    JS_INNER_HTML_REDUCE_WHITESPACES,

    /** Javascript function returning a length (e.g. getWidth) without 'px' at the end. */
    JS_LENGTH_WITHOUT_PX,

    /**
     * Set this property if the browser evaluates<br>
     * window.location.hash to #&uuml; (like Firefox)<br>
     * for url 'http://localhost/something/#%C3%BC'.<br>
     * IE evaluates to #%C3%BC.
     */
    JS_LOCATION_HASH_IS_DECODED,

    /**
     * Indicates if the String representation of a native function begins and ends with a \n.
     */
    JS_NATIVE_FUNCTION_TOSTRING_NEW_LINE,

    /** Always "[object]". */
    JS_OBJECT_ONLY,

    /** Evaluates to "[object Element]". */
    JS_OBJECT_PREFIX,

    /** Indicates that someObj.offsetParent throws an exception when called on an object that is not yet attached
     *  to the page's DOM.
     */
    JS_OFFSET_PARENT_THROWS_NOT_ATTACHED,

    /** Setting the property opacity of an css style declaration to arbitrary values is allowed.
     * FF accepts only valid floats.
     */
    JS_OPACITY_ACCEPTS_ARBITRARY_VALUES,

    /** Javascript script.appendChild throws an error (IE6-IE8). */
    JS_SCRIPT_APPEND_CHILD_THROWS_EXCEPTION,

    /** Javascript script.insertBefore throws an error (IE6-IE8). */
    JS_SCRIPT_INSERT_BEFORE_THROWS_EXCEPTION,

    /** Javascript selectorText property returns selectors in uppercase. */
    JS_SELECTOR_TEXT_UPPERCASE,

    /** Indicates if calling HTMLSelectElement.add the second parameter
     * is treated as index like IE does.
     */
    JS_SELECT_ADD_SECOND_PARAM_IS_INDEX,

    /** Indicates if calling HTMLSelectElement.add without second parameter
     * throws an exception.
     */
    JS_SELECT_ADD_SECOND_PARAM_IS_REQUIRED,

    /** Indicates if calling HTMLSelectElement.item with a negative value should throw. */
    JS_SELECT_ITEM_THROWS_IF_NEGATIVE,

    /** Indicates that select.options.childNodes is a valid property (IE). */
    JS_SELECT_OPTIONS_HAS_CHILDNODES_PROPERTY,

    /** Indicates if setting an out of bound value for HTMLSelectElement.selectedIndex should throw. */
    JS_SELECT_SELECTED_INDEX_THROWS_IF_BAD,

    /** Indicates that the set attribute method treads the synthetic
     * empty attr for 'class' (IE6/7) as a normal one. */
    JS_SET_ATTRIBUTE_CONSIDERS_ATTR_FOR_CLASS_AS_REAL,

    /** IE supports accessing unsupported style elements via getter
     * like val = elem.style.htmlunit;.
     */
    JS_STYLE_UNSUPPORTED_PROPERTY_GETTER,

    /** Indicates that table elements supports the values "top", "bottom", "middle", "baseline" (IE). */
    JS_TABLE_VALIGN_SUPPORTS_IE_VALUES,

    /** Getting the property cols 20, if the defined value is not convertable into an integer (IE).
     * FF returns -1 in this case.
     */
    JS_TEXT_AREA_COLS_RETURNS_20,

    /** Setting the property cols throws an exception, if the provided value is not
     * convertable into an integer (IE).
     * FF ignores the provided value in this case and sets cols to 0.
     */
    JS_TEXT_AREA_SET_COLS_THROWS_EXCEPTION,

    /** Changing the opener of an window to something not null
     * is not valid (in FF).
     */
    JS_WINDOW_CHANGE_OPENER_NOT_ALLOWED,

    /** Window property not usable as function. */
    JS_WINDOW_IS_NOT_A_FUNCTION,

    /** Indicates that a xml attribute supports the text property. */
    JS_XML_ATTRIBUTE_HAS_TEXT_PROPERTY,

    /** Indicates that new XMLSerializer().serializeToString(..) adds the xhtml namespace to the root element. */
    JS_XML_SERIALIZER_ADD_XHTML_NAMESPACE,

    /** Indicates that new XMLSerializer().serializeToString(..) always appends a CRLF at the end
     * of the produced string. */
    JS_XML_SERIALIZER_APPENDS_CRLF,

    /** Indicates that new XMLSerializer().serializeToString(..) transforms the node name to upper case. */
    JS_XML_SERIALIZER_NODE_AS_UPPERCASE,

    /** Indicates that new XMLSerializer().serializeToString(..) respects the XHTML definition for non empty tags. */
    JS_XML_SERIALIZER_NON_EMPTY_TAGS,

    /** Indicates that the browser uses the ActiveXObject for implementing XML support (IE). */
    JS_XML_SUPPORT_VIA_ACTIVEXOBJECT,

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

    /** Indicates that a read only JS property can be... set as done by IE but not FF3. */
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

    /** Indicates if SVG is supported. */
    SVG_SUPPORT,

    /** Indicates that "\n" are replaced by "\r\n" in textarea values. */
    TEXTAREA_CRNL,

    /** Indicates that the browser treats "position: fixed" as if it were "position: static". */
    TREATS_POSITION_FIXED_LIKE_POSITION_STATIC,

    /**
     * Indicates, that the pathname for the url 'about:blank' is empty;
     * instead of '/blank'.
     */
    URL_ABOUT_BLANK_HAS_EMPTY_PATH,

    /**
     * Indicates, that the browser supports username and password as
     * part of the url (e.g. http://john.smith:secret@localhost).
     */
    URL_AUTH_CREDENTIALS,

    /** Replace only ' ' with %20 when encode the query part of an url. */
    URL_MINIMAL_QUERY_ENCODING,

    /** */
    URL_MISSING_SLASHES,

    /** */
    WINDOW_ACTIVE_ELEMENT_FOCUSED,

    /** XMLHttpRequest does not trigger the error handler (IE). */
    XMLHTTPREQUEST_ERRORHANDLER_NOT_SUPPORTED,

    /** Indicates that 'this' corresponds to the handler function when a XMLHttpRequest handler is executed. */
    XMLHTTPREQUEST_HANDLER_THIS_IS_FUNCTION,

    /** Indicates if a same origin check should be skipped. */
    XMLHTTPREQUEST_IGNORE_SAME_ORIGIN,

    /** Indicates if a request to a about URL is allowed. */
    XMLHTTPREQUEST_IGNORE_SAME_ORIGIN_TO_ABOUT,

    /** Indicates that the onreadystatechange handler is not triggered for sync requests. */
    XMLHTTPREQUEST_ONREADYSTATECANGE_SYNC_REQUESTS_NOT_TRIGGERED,

    /** Indicates that the onreadystatechange handler is triggered for sync requests for COMPLETED (4). */
    XMLHTTPREQUEST_ONREADYSTATECANGE_SYNC_REQUESTS_TRIGGER_COMPLETED,

    /** Indicates that the onreadystatechange handler is triggered with an event parameter (FF). */
    XMLHTTPREQUEST_ONREADYSTATECHANGE_WITH_EVENT_PARAM,

    /** Indicates that the onload handler is not triggered if completed (FF). */
    XMLHTTPREQUEST_TRIGGER_ONLOAD_ON_COMPLETED,

    /** Indicates if XUL is supported (FF only). */
    XUL_SUPPORT;
}
