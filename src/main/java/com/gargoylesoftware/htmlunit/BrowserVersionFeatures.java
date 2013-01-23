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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.javascript.configuration.BrowserFeature;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

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
     * In Firefox the URL is unchanged.
     */
    @BrowserFeature(@WebBrowser(IE))
    ANCHOR_EMPTY_HREF_NO_FILENAME,

    /** Indicates that a blur event should be triggered before an onchange event. */
    BLUR_BEFORE_ONCHANGE,

    /** If the "type" attribute of HtmlButton should be evaluated to 'button' if not specified. */
    @BrowserFeature(@WebBrowser(IE))
    BUTTON_EMPTY_TYPE_BUTTON,

    /** Is canvas supported? */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    CANVAS,

    /** Indicates that the browser can inherit CSS property values. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    CAN_INHERIT_CSS_PROPERTY_VALUES,

    /** The status/value update of a control is processed before the click event. */
    @BrowserFeature(@WebBrowser(IE))
    CONTROL_UPDATE_DONE_BEFORE_CLICK_EVENT_FIRED,

    /** Indicates that the default value for height of elements is 15 instead of 20. */
    @BrowserFeature(@WebBrowser(IE))
    CSS_DEFAULT_ELEMENT_HEIGHT_15,

    /** Indicates that the default value for height of elements is used instead
     * of the calculated value, if the calculated value is smaller. */
    @BrowserFeature(@WebBrowser(IE))
    CSS_DEFAULT_ELEMENT_HEIGHT_MARKS_MIN,

    /** Indicates that the default value for width is 'auto'. */
    @BrowserFeature(@WebBrowser(IE))
    CSS_DEFAULT_WIDTH_AUTO,

    /** Was originally .isFirefox(). */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    CSS_DISPLAY_DEFAULT,

    /** Default is 'normal'. */
    @BrowserFeature(@WebBrowser(FF))
    CSS_FONT_STRECH_DEFAULT_NORMAL,

    /** Indicates that the browser can surrounds image url's with quotes. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    CSS_IMAGE_URL_QUOTED,

    /** Indicates that only integers are allowed for pixel value. */
    @BrowserFeature(@WebBrowser(IE))
    CSS_PIXEL_VALUES_INT_ONLY,

    /** Indicates that the :lang(..) selector is supported. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    CSS_SELECTOR_LANG,

    /** Internet Explorer versions 5 and later support the behavior property. The behavior property lets
     * you use CSS to attach a script to a specific element in order to implement
     * DHTML (Dynamic HTML) components.
     */
    @BrowserFeature(@WebBrowser(IE))
    CSS_SUPPORTS_BEHAVIOR_PROPERTY,

    /** Default is 'none'. */
    @BrowserFeature(@WebBrowser(FF))
    CSS_TEXT_SHADOW_DEFAULT_NONE,

    /** Default is 'normal'. */
    CSS_WORD_SPACING_DEFAULT_NORMAL,

    /** Values for the zIndex are rounded to integer. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 7))
    CSS_ZINDEX_ROUNDED,

    /** IE uses the type Number for the zIndex Values (instead of String). */
    @BrowserFeature(@WebBrowser(IE))
    CSS_ZINDEX_TYPE_NUMBER,

    /** If values for the zIndex is undefined than set the zindex to the default value. */
    @BrowserFeature(@WebBrowser(IE))
    CSS_ZINDEX_UNDEFINED_FORCES_RESET,

    /** If values for the zIndex is undefined or null than set the zindex throws an error. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 8))
    CSS_ZINDEX_UNDEFINED_OR_NULL_THROWS_ERROR,

    /** */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    DIALOGWINDOW_REFERER,

    /** Indicates that "\n" are replaced by "\r\n" in textarea values. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    DISPLAYED_COLLAPSE,

    /** */
    @BrowserFeature(@WebBrowser(IE))
    DOCTYPE_4_0_TRANSITIONAL_STANDARDS,

    /** DOCTYPE is a Comment from JavaScript perspective. */
    @BrowserFeature(@WebBrowser(IE))
    DOCTYPE_IS_COMMENT,

    /** If document.implementation.hasFeature() supports only "HTML". */
    @BrowserFeature(@WebBrowser(IE))
    DOMIMPLEMENTATION_ONLY_HTML,

    /** IE removes all child text nodes, but FF preserves the first. */
    @BrowserFeature(@WebBrowser(IE))
    DOM_NORMALIZE_REMOVE_CHILDREN,

    /** Triggers "DOMContentLoaded" event. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    EVENT_DOM_CONTENT_LOADED,

    /** Is setting 'focus' and 'blur' events of 'document', triggers the event for the descendants elements. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    EVENT_FOCUS_DOCUMENT_DESCENDANTS,

    /** Triggers "input" event. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    EVENT_INPUT,

    /** Triggers "onchange" event handler on losing focus. */
    @BrowserFeature(@WebBrowser(IE))
    EVENT_ONCHANGE_LOSING_FOCUS,

    /** Triggers "onclick" event handler also for the select option. */
    @BrowserFeature({ @WebBrowser(value = IE, minVersion = 8), @WebBrowser(FF), @WebBrowser(CHROME) })
    EVENT_ONCLICK_FOR_SELECT_OPTION_ALSO,

    /** Triggers "onerror" if external loading of an external javascript failed. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    EVENT_ONERROR_EXTERNAL_JAVASCRIPT,

    /** Triggers "onload" event if external javascript successfully loaded. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    EVENT_ONLOAD_EXTERNAL_JAVASCRIPT,

    /** Triggers "onload" event of the frameset before the one from the frames. */
    @BrowserFeature(@WebBrowser(IE))
    EVENT_ONLOAD_FRAMESET_FIRST,

    /** Triggers "onload" event if an iframe was created by javascript and added to the page. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    EVENT_ONLOAD_IFRAME_CREATED_BY_JAVASCRIPT,

    /** Triggers "onreadystatechange" event. */
    @BrowserFeature(@WebBrowser(IE))
    EVENT_ONREADY_STATE_CHANGE,

    /** Triggers "propertychange" event. */
    @BrowserFeature(@WebBrowser(IE))
    EVENT_PROPERTY_CHANGE,

    /** Indicates that document.execCommand() should throw an exception when called with an illegal command. */
    @BrowserFeature(@WebBrowser(IE))
    EXECCOMMAND_THROWS_ON_WRONG_COMMAND,

    /** */
    @BrowserFeature(@WebBrowser(IE))
    FILEINPUT_EMPTY_DEFAULT_VALUE,

    /** Indicates if a form field is directly reachable by its new name once this has been changed. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    FORMFIELD_REACHABLE_BY_NEW_NAMES,

    /**
     * Special behavior of IE6; the URL submitted by a form with method type 'GET' has
     * a question mark at the end if there are no parameters.
     */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 6))
    FORM_SUBMISSION_URL_END_WITH_QUESTIONMARK,

    /** Form submit is done without the hash part of the form action url. */
    @BrowserFeature(@WebBrowser(IE))
    FORM_SUBMISSION_URL_WITHOUT_HASH,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_100,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_101,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_104,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_105,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_106,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_107,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_108,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_111,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_112,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_113,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_116,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_121,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_124,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_125,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_13,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_133,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_150,

    /** Was originally .isFirefox(). */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    GENERATED_155,

    /** Was originally .isFirefox(). */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    GENERATED_156,

    /** Was originally .isFirefox(). */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    GENERATED_157,

    /** Was originally .isFirefox(). */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    GENERATED_158,

    /** Was originally .isFirefox(). */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    GENERATED_160,

    /** Was originally .isFirefox(). */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    GENERATED_161,

    /** Was originally .isFirefox(). */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    GENERATED_164,

    /** Was originally .isFirefox(). */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    GENERATED_165,

    /** Was originally .isFirefox(). */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    GENERATED_167,

    /** Was originally .isFirefox(). */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    GENERATED_169,

    /** Was originally .isFirefox(). */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    GENERATED_170,

    /** Was originally .isFirefox(). */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    GENERATED_172,

    /** Was originally .isFirefox(). */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    GENERATED_174,

    /** Was originally .isFirefox(). */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    GENERATED_176,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_21,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_3,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_30,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_31,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_32,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_37,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_40,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_41,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_42,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_43,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_44,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_45,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_48,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_49,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_50,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_51,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_53,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_55,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_57,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_59,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_60,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_61,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_63,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_65,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_70,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_71,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_72,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_73,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_74,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_75,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_80,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_81,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_85,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_86,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_88,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_90,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_91,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_92,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_93,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_94,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_95,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_96,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_97,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_98,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_99,

    /** If the class name is [object GeoGeolocation]. */
    @BrowserFeature(@WebBrowser(FF))
    GEO_GEOLOCATION,

    /** */
    @BrowserFeature(@WebBrowser(IE))
    HEADER_CONTENT_DISPOSITION_ABSOLUTE_PATH,

    /** Indicates if HTML5 tags source, video and audio are recognized. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTML5_TAGS,

    /** */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 7))
    HTMLABBREVIATED,

    /** Should {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBaseFontElement#isEndTagForbidden}
     * return <code>true</code>. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTMLBASEFONT_END_TAG_FORBIDDEN,

    /** [object HTMLBGSoundElement]. */
    @BrowserFeature(@WebBrowser(IE))
    HTMLBGSOUND,

    /** Indicates that comment nodes should be treated similar to elements, e.g. getElementsByTagName(). */
    @BrowserFeature(@WebBrowser(IE))
    HTMLCOLLECTION_COMMENT_IS_ELEMENT,

    /** Allows multiple elements with the same 'id'. */
    @BrowserFeature({ @WebBrowser(IE), @WebBrowser(FF), @WebBrowser(CHROME) })
    HTMLCOLLECTION_IDENTICAL_IDS,

    /** Allow detection of object type for collection elements. */
    @BrowserFeature(@WebBrowser(IE))
    HTMLCOLLECTION_OBJECT_DETECTION,

    /**
     * Supports Conditional Comments.
     * @see <a href="http://en.wikipedia.org/wiki/Conditional_comment">Conditional comment</a>
     */
    @BrowserFeature(@WebBrowser(IE))
    HTMLCONDITIONAL_COMMENTS,

    /** Is document.charset lower-case (and defaultCharset is 'windows-1252'). */
    @BrowserFeature(@WebBrowser(IE))
    HTMLDOCUMENT_CHARSET_LOWERCASE,

    /** Do document.bgColor/.alinkColor/.vlinkColor/.linkColor have value by default. */
    @BrowserFeature(@WebBrowser(IE))
    HTMLDOCUMENT_COLOR,

    /** Allows invalid 'align' values. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTMLELEMENT_ALIGN_INVALID,

    /**
     * Indicates that attribute name should be fixed for get/setAttribute(), specifically "className" and "class",
     * only in quirks mode.
     */
    @BrowserFeature(@WebBrowser(IE))
    HTMLELEMENT_ATTRIBUTE_FIX_IN_QUIRKS_MODE,

    /** Indicates that element.innerHTML/outerHTML the tag name is in upper case or not. */
    @BrowserFeature(@WebBrowser(IE))
    HTMLELEMENT_OUTER_HTML_UPPER_CASE,

    /** */
    HTMLELEMENT_TRIM_CLASS_ATTRIBUTE,

    /**
     * Indicates if a self-closing &lt;iframe/&gt; tag should be considered as an opening tag.
     **/
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTMLIFRAME_IGNORE_SELFCLOSING,

    /** */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTMLIMAGE_NAME_VALUE_PARAMS,

    /** */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTMLINPUT_DEFAULT_IS_CHECKED,

    /**
     * Set this property if the browser does NOT
     * support the disabling of an individual option group.
     */
    @BrowserFeature(@WebBrowser(IE))
    HTMLOPTIONGROUP_NO_DISABLED,

    /** */
    @BrowserFeature(@WebBrowser(IE))
    HTMLOPTION_EMPTY_TEXT_IS_NO_CHILDREN,

    /** */
    @BrowserFeature(@WebBrowser(IE))
    HTMLOPTION_PREVENT_DISABLED,

    /** Un-selecting an option in a (single-value) select causes the first option to become selected. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTMLOPTION_UNSELECT_SELECTS_FIRST,

    /** Indicates that for some elements, the empty text after it should be removed. */
    @BrowserFeature(@WebBrowser(IE))
    HTMLPARSER_REMOVE_EMPTY_CONTENT,

    /** Set this checked state back to default when added to page (IE). */
    @BrowserFeature(@WebBrowser(IE))
    HTMLRADIOINPUT_SET_CHECKED_TO_DEFAULT_WHEN_ADDED,

    /**
     * Set this property if the script tag supports the
     * types 'application/javascript' and 'application/x-javascript'.
     */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTMLSCRIPT_APPLICATION_JAVASCRIPT,

    /** Runs <script src="javascript:'[code]'">. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 6))
    HTMLSCRIPT_SRC_JAVASCRIPT,

    /** Trims the value of the type attribute before to verify it. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTMLSCRIPT_TRIM_TYPE,

    /** Do not allow anything in color, but restrict to valid values only. */
    @BrowserFeature({ @WebBrowser(IE), @WebBrowser(value = FF, maxVersion = 3.6f) })
    HTML_COLOR_RESTRICT,

    /** HTMLCommentElement instead of Comment. */
    @BrowserFeature(@WebBrowser(IE))
    HTML_COMMENT_ELEMENT,

    /** HTMLGenericElement instead of HTMLUnknownElement. */
    @BrowserFeature(@WebBrowser(IE))
    HTML_GENERIC_ELEMENT,

    /** Indicates that "host" HTTP header should be the first. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTTP_HEADER_HOST_FIRST,

    /** Indicates that the browser should ignore contents of inner head elements. */
    @BrowserFeature(@WebBrowser(IE))
    IGNORE_CONTENTS_OF_INNER_HEAD,

    /**
     * The function addEventListener or attachEvent(IE) accepts null as listener
     * instead of throwing an exception.
     */
    @BrowserFeature({ @WebBrowser(IE), @WebBrowser(value = FF, minVersion = 17) })
    JS_ADD_EVENT_LISTENER_ACCEPTS_NULL_LISTENER,

    /** Setting the property align to arbitrary values is allowed. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_ALIGN_ACCEPTS_ARBITRARY_VALUES,

    /** Setting the property align of an input element ignores the value
     * if the value is one of center, justify, left or right.
     * For all other values an exception is still thrown.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_ALIGN_FOR_INPUT_IGNORES_VALUES,

    /** Top scope constants can be assign (and are not... constants).
     */
    @BrowserFeature({ @WebBrowser(value = FF, maxVersion = 3.6f), @WebBrowser(IE) })
    JS_ALLOW_CONST_ASSIGNMENT,

    /**
     * Javascript property anchors includes all anchors with a name or an id property.
     * If not set name property is required
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_ANCHORS_REQUIRES_NAME_OR_ID,

    /** Indicates that the appendChild call create a DocumentFragment to be
     * the parentNode's parentNode if this was null. */
    @BrowserFeature(@WebBrowser(IE))
    JS_APPEND_CHILD_CREATE_DOCUMENT_FRAGMENT_PARENT,

    /** Indicates that the appendChild call throws no exception
     * if the provided note cannot be inserted. */
    @BrowserFeature(@WebBrowser(IE))
    JS_APPEND_CHILD_THROWS_NO_EXCEPTION_FOR_WRONG_NOTE,

    /** Indicates that the class name of "arguments" object is "Object". */
    @BrowserFeature(@WebBrowser(IE))
    JS_ARGUMENTS_IS_OBJECT,

    /** Indicates that the "arguments" object is read-only. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(value = FF, minVersion = 17) })
    JS_ARGUMENTS_IS_READ_ONLY,

    /** Indicates that the attributes map contains empty attr
     * objects for all properties of the object (like IE does). */
    @BrowserFeature(@WebBrowser(IE))
    JS_ATTRIBUTES_CONTAINS_EMPTY_ATTR_FOR_PROPERTIES,

    /** firstChild and lastChild returns null for Attr (like IE does). */
    @BrowserFeature(@WebBrowser(IE))
    JS_ATTR_FIRST_LAST_CHILD_RETURNS_NULL,

    /** Indicates that the getBoundingClientRect adds an offset of 2. */
    @BrowserFeature(@WebBrowser(IE))
    JS_BOUNDING_CLIENT_RECT_OFFSET_TWO,

    /** Indicates that the browser emulates the char attribute. */
    @BrowserFeature(@WebBrowser(IE))
    JS_CHAR_EMULATED,

    /** Indicates that the browser emulates the charOff attribute. */
    @BrowserFeature(@WebBrowser(IE))
    JS_CHAR_OFF_EMULATED,

    /** Indicates that the browser tries to convert the char attribute values
     * to integer. */
    @BrowserFeature(@WebBrowser(value = FF, maxVersion = 3.6f))
    JS_CHAR_OFF_INTEGER,

    /** Indicates that the browser returns a dot if the char attribute is not defined. */
    @BrowserFeature(@WebBrowser(value = FF, maxVersion = 3.6f))
    JS_CHAR_UNDEFINED_DOT,

    /** Indicates that the clientLeft and clientTop returning zero in all cases. */
    @BrowserFeature(@WebBrowser(IE))
    JS_CLIENT_LEFT_TOP_ZERO,

    /** Indicates that the cloneNode call copies all event listeners. */
    @BrowserFeature(@WebBrowser(IE))
    JS_CLONE_NODE_COPIES_EVENT_LISTENERS,

    /** Indicates that "constructor" propery is defined, e.g. <tt>document.constructor</tt>. */
    @BrowserFeature(@WebBrowser(FF))
    JS_CONSTRUCTOR,

    /** Is Date.toLocaleTimeString() in 24-hour format. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(value = FF, minVersion = 17) })
    JS_DATE_LOCATE_TIME_24,

    /** */
    @BrowserFeature(@WebBrowser(IE))
    JS_DEFERRED,

    /** Object prototype supports <tt>__defineGetter__</tt> and similar properties. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_DEFINE_GETTER,

    /** Javascript doctyp.entities returns an empty string (IE). */
    @BrowserFeature(@WebBrowser(IE))
    JS_DOCTYPE_ENTITIES_EMPTY_STRING,

    /** Javascript doctyp.entities returns null (FF10). */
    @BrowserFeature(@WebBrowser(value = FF, maxVersion = 3.6f))
    JS_DOCTYPE_ENTITIES_NULL,

    /** Javascript doctyp.internalSubset returns an empty string (IE). */
    JS_DOCTYPE_INTERNALSUBSET_EMPTY_STRING,

    /** Javascript doctyp.notations returns an empty string (IE). */
    @BrowserFeature(@WebBrowser(IE))
    JS_DOCTYPE_NOTATIONS_EMPTY_STRING,

    /** Javascript doctyp.notations returns null (FF10). */
    @BrowserFeature(@WebBrowser(value = FF, maxVersion = 3.6f))
    JS_DOCTYPE_NOTATIONS_NULL,

    /** Javascript document.appendChild is allowed (IE). */
    @BrowserFeature(@WebBrowser(IE))
    JS_DOCUMENT_APPEND_CHILD_SUPPORTED,

    /** Javascript function document.createElement can process html code.
     * e.g. document.createElement("<INPUT TYPE='RADIO' NAME='RADIOTEST' VALUE='First Choice'>")
     * @see "http://msdn.microsoft.com/en-us/library/ms536389%28v=VS.85%29.aspx"
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_DOCUMENT_CREATE_ELEMENT_EXTENDED_SYNTAX,

    /** Javascript function document.createElement accepts only tag names. */
    @BrowserFeature({ @WebBrowser(value = FF, minVersion = 4), @WebBrowser(CHROME) })
    JS_DOCUMENT_CREATE_ELEMENT_STRICT,

    /** Javascript document.doctype returns null (IE). */
    @BrowserFeature(@WebBrowser(IE))
    JS_DOCUMENT_DOCTYPE_NULL,

    /** Javascript property document.domain is lowercase. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_DOCUMENT_DOMAIN_IS_LOWERCASE,

    /** Javascript document.forms(...) supported (IE). */
    @BrowserFeature(@WebBrowser(IE))
    JS_DOCUMENT_FORMS_FUNCTION_SUPPORTED,

    /** Javascript property document.domain doesn't allow to set domain of about:blank. */
    @BrowserFeature(@WebBrowser(IE))
    JS_DOCUMENT_SETTING_DOMAIN_THROWS_FOR_ABOUT_BLANK,

    /** Don't enumerate functions, see {@link net.sourceforge.htmlunit.corejs.javascript.ScriptableObject#DONTENUM}. */
    @BrowserFeature(@WebBrowser(IE))
    JS_DONT_ENUM_FUNCTIONS,

    /** Enables Javascript ECMA5 functions (like Date.toISOString or Date.toJSON). */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_ECMA5_FUNCTIONS,

    /** Javascript calculation of element clientHeight/Width does not
     * include the padding.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_ELEMENT_EXTENT_WITHOUT_PADDING,

    /** Indicates that 'exception' (technically NativeError) exposes "stack" property. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_ERROR_STACK,

    /** Indicates that "eval" function should have access to the local function scope. */
    @BrowserFeature(@WebBrowser(IE))
    JS_EVAL_LOCAL_SCOPE,

    /** Javascript event aborted check is based on the event handler return value (IE);
     * (standards-compliant browsers doing this via preventDefault).
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_EVENT_ABORTED_BY_RETURN_VALUE_FALSE,

    /** Javascript event handlers declared as property on a node
     * don't receive the event as argument.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_EVENT_HANDLER_AS_PROPERTY_DONT_RECEIVE_EVENT,

    /** Do not send parameter in event handlers. */
    @BrowserFeature(@WebBrowser(IE))
    JS_EVENT_NO_PARAMETER,

    /** Indicates that the URL of parent window is used to resolve URLs in frames with javascript src. */
    @BrowserFeature({ @WebBrowser(value = IE, maxVersion = 6),
        @WebBrowser(value = IE, minVersion = 8), @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_FRAME_RESOLVE_URL_WITH_PARENT_WINDOW,

    /** Indicates if Function.bind is available. */
    @BrowserFeature({ @WebBrowser(value = FF, minVersion = 10), @WebBrowser(CHROME) })
    JS_FUNCTION_BIND,

    /**
     * Indicates that function is defined even before its declaration, inside a block.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_FUNCTION_DECLARED_FORWARD_IN_BLOCK,

    /** Indicates if the method isXmlName exists in the window scope. */
    @BrowserFeature(@WebBrowser(value = FF, maxVersion = 10))
    JS_FUNCTION_ISXMLNAME,

    /**
     * Indicates that function can be defined as
     * <code>function object.property() {}</code> instead of <code>object.property = function() {}</code>.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_FUNCTION_OBJECT_METHOD,

    /** Indicates if the method toSource exists on the native objects. */
    @BrowserFeature(@WebBrowser(FF))
    JS_FUNCTION_TOSOURCE,

    /** Indicates that the getAttribute method supports IE style flags, only in quirks mode . */
    @BrowserFeature(@WebBrowser(IE))
    JS_GET_ATTRIBUTE_SUPPORTS_FLAGS_IN_QUIRKS_MODE,

    /** Javascript function getBackgroundColor of computed styles returns the color as rgb. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_GET_BACKGROUND_COLOR_FOR_COMPUTED_STYLE_AS_RGB,

    /** Javascript function getElementById calls getElementByName if nothing found by id, only in quirks mode. */
    @BrowserFeature(@WebBrowser(IE))
    JS_GET_ELEMENT_BY_ID_ALSO_BY_NAME_IN_QUICKS_MODE,

    /** Javascript function getElementById compares the id's case sensitive. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_GET_ELEMENT_BY_ID_CASE_SENSITIVE,

    /** Indicates that not defined function handler should be 'undefined', or 'null'. */
    @BrowserFeature(@WebBrowser(value = FF, maxVersion = 3.6f))
    JS_HANDLER_UNDEFINED,

    /** Indicates that objects with prototype property available in window scope; Firefox does this. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_HAS_OBJECT_WITH_PROTOTYPE_PROPERTY_IN_WINDOW_SCOPE,

    /** Javascript method getHeight of IFrames may return negative values. */
    @BrowserFeature(@WebBrowser(IE))
    JS_IFRAME_GET_HEIGHT_NEGATIVE_VALUES,

    /** Javascript method getWidth of IFrames may return negative values. */
    @BrowserFeature(@WebBrowser(IE))
    JS_IFRAME_GET_WIDTH_NEGATIVE_VALUES,

    /** IE ignores the last line containing uncommented. */
    @BrowserFeature(@WebBrowser(IE))
    JS_IGNORES_LAST_LINE_CONTAINING_UNCOMMENTED,

    /** Indicates if multiple spaces are replaced by a single one when accessing innerHTML. */
    @BrowserFeature(@WebBrowser(IE))
    JS_INNER_HTML_REDUCE_WHITESPACES,

    /** Javascript function returning a length (e.g. getWidth) without 'px' at the end. */
    @BrowserFeature(@WebBrowser(IE))
    JS_LENGTH_WITHOUT_PX,

    /**
     * Set this property if the browser evaluates<br>
     * window.location.hash to #&uuml; (like Firefox)<br>
     * for url 'http://localhost/something/#%C3%BC'.<br>
     * IE evaluates to #%C3%BC.
     */
    @BrowserFeature({ @WebBrowser(value = IE, minVersion = 8), @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_LOCATION_HASH_IS_DECODED,

    /**
     * Indicates if the String representation of a native function begins and ends with a \n.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_NATIVE_FUNCTION_TOSTRING_NEW_LINE,

    /** If <tt>true</tt>, Date.prototype.getYear subtracts 1900 only if 1900 <= date < 2000. */
    @BrowserFeature(@WebBrowser(IE))
    JS_NON_ECMA_GET_YEAR,

    /** "[object]" in quirks mode. */
    @BrowserFeature(@WebBrowser(IE))
    JS_OBJECT_IN_QUIRKS_MODE,

    /** Indicates that someObj.offsetParent throws an exception when called on an object that is not yet attached
     *  to the page's DOM.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_OFFSET_PARENT_THROWS_NOT_ATTACHED,

    /** Setting the property opacity of an css style declaration to arbitrary values is allowed.
     * FF accepts only valid floats.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_OPACITY_ACCEPTS_ARBITRARY_VALUES,

    /** If <tt>true</tt>, then treat <tt>__parent__</tt> and <tt>__proto__</tt> as special properties. */
    @BrowserFeature(@WebBrowser(IE))
    JS_PARENT_PROTO_PROPERTIES,

    /** Javascript script.text(...) reexecutes the script (IE). */
    @BrowserFeature(@WebBrowser(IE))
    JS_SCRIPT_ALWAYS_REEXECUTE_ON_SET_TEXT,

    /** Always execute the script if IE;
     *  in FF, only execute if the old "src" attribute was undefined
     *  and there was no inline code.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_SCRIPT_ALWAYS_REEXECUTE_ON_SRC_CHANGE,

    /** Javascript script.appendChild throws an error (IE6-IE8). */
    @BrowserFeature(@WebBrowser(IE))
    JS_SCRIPT_APPEND_CHILD_THROWS_EXCEPTION,

    /** Javascript script.insertBefore throws an error (IE6-IE8). */
    @BrowserFeature(@WebBrowser(IE))
    JS_SCRIPT_INSERT_BEFORE_THROWS_EXCEPTION,

    /** Javascript script tags supports the 'for' and the 'event'
     * attribute (IE).
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_SCRIPT_SUPPORTS_FOR_AND_EVENT,

    /** Javascript script object supports the onreadystatechange event (IE). */
    @BrowserFeature(@WebBrowser(IE))
    JS_SCRIPT_SUPPORTS_ONREADYSTATECHANGE,

    /** Javascript selectorText property returns selectors in uppercase. */
    @BrowserFeature(@WebBrowser(IE))
    JS_SELECTOR_TEXT_UPPERCASE,

    /** Indicates if calling HTMLSelectElement.add the second parameter
     * is treated as index like IE does.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_SELECT_ADD_SECOND_PARAM_IS_INDEX,

    /** Indicates if calling HTMLSelectElement.add without second parameter
     * throws an exception.
     */
    @BrowserFeature(@WebBrowser(value = FF, maxVersion = 3.6f))
    JS_SELECT_ADD_SECOND_PARAM_IS_REQUIRED,

    /** Indicates if calling HTMLSelectElement.item with a negative value should throw. */
    @BrowserFeature(@WebBrowser(IE))
    JS_SELECT_ITEM_THROWS_IF_NEGATIVE,

    /** Indicates that select.options.childNodes is a valid property (IE). */
    @BrowserFeature(@WebBrowser(IE))
    JS_SELECT_OPTIONS_HAS_CHILDNODES_PROPERTY,

    /** Indicates if setting an out of bound value for HTMLSelectElement.selectedIndex should throw. */
    JS_SELECT_SELECTED_INDEX_THROWS_IF_BAD,

    /** Indicates that the set attribute method treads the synthetic
     * empty attr for 'class' (IE6/7) as a normal one. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 7))
    JS_SET_ATTRIBUTE_CONSIDERS_ATTR_FOR_CLASS_AS_REAL,

    /** IE supports accessing unsupported style elements via getter
     * like val = elem.style.htmlunit;.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_STYLE_UNSUPPORTED_PROPERTY_GETTER,

    /** Indicates that table elements supports the values "top", "bottom", "middle", "baseline" (IE). */
    @BrowserFeature(@WebBrowser(IE))
    JS_TABLE_VALIGN_SUPPORTS_IE_VALUES,

    /** Getting the property cols 20, if the defined value is not convertable into an integer (IE).
     * FF returns -1 in this case.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_TEXT_AREA_COLS_RETURNS_20,

    /** Setting the property cols throws an exception, if the provided value is not
     * convertible into an integer (IE).
     * FF ignores the provided value in this case and sets cols to 0.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_TEXT_AREA_SET_COLS_THROWS_EXCEPTION,

    /** It looks likes TreeWalker.expandEntityReferences is always <code>false</code> for FF17.
     */
    @BrowserFeature(@WebBrowser(value = FF, minVersion = 17))
    JS_TREEWALKER_EXPAND_ENTITY_REFERENCES_FALSE,

    /** Changing the opener of an window to something not null
     * is not valid (in FF).
     */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_WINDOW_CHANGE_OPENER_NOT_ALLOWED,

    /** Support for accessing the frame of a window by id additionally
     * to using the name (FF).
     */
    @BrowserFeature({ @WebBrowser(IE) })
    JS_WINDOW_FRAMES_ACCESSIBLE_BY_ID,

    /** Window property not usable as function. */
    @BrowserFeature(@WebBrowser(IE))
    JS_WINDOW_IS_NOT_A_FUNCTION,

    /** Supports XML. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_XML,

    /** Indicates that a xml attribute supports the text property. */
    @BrowserFeature(@WebBrowser(IE))
    JS_XML_ATTRIBUTE_HAS_TEXT_PROPERTY,

    /** Indicates that new XMLSerializer().serializeToString(..) adds the xhtml namespace to the root element. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_XML_SERIALIZER_ADD_XHTML_NAMESPACE,

    /** Indicates that new XMLSerializer().serializeToString(..) always appends a CRLF at the end
     * of the produced string. */
    @BrowserFeature(@WebBrowser(IE))
    JS_XML_SERIALIZER_APPENDS_CRLF,

    /** Indicates that new XMLSerializer().serializeToString(..) transforms the node name to upper case. */
    JS_XML_SERIALIZER_NODE_AS_UPPERCASE,

    /** Indicates that new XMLSerializer().serializeToString(..) respects the XHTML definition for non empty tags. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_XML_SERIALIZER_NON_EMPTY_TAGS,

    /** Indicates that the browser uses the ActiveXObject for implementing XML support (IE). */
    @BrowserFeature(@WebBrowser(IE))
    JS_XML_SUPPORT_VIA_ACTIVEXOBJECT,

    /** With special keys [in .type(int)], should we trigger onkeypress event or not. */
    @BrowserFeature(@WebBrowser(FF))
    KEYBOARD_EVENT_SPECIAL_KEYPRESS,

    /**
     * Indicates that the browser considers the meta X-UA-Compatible when determining
     * compatibility/quirks mode.
     */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 8))
    META_X_UA_COMPATIBLE,

    /** If true, then silently ignore element.appendChild(element). */
    @BrowserFeature(@WebBrowser(IE))
    NODE_APPEND_CHILD_SELF_IGNORE,

    /** Body of a &lt;noscript&gt; tag is not totally ignored but considered as a (not displayed) text node. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    NOSCRIPT_BODY_AS_TEXT,

    /** */
    @BrowserFeature(@WebBrowser(IE))
    PAGE_SELECTION_RANGE_FROM_SELECTABLE_TEXT_INPUT,

    /** Wait for the whole page to load before initializing bodies for frames. */
    @BrowserFeature(@WebBrowser(IE))
    PAGE_WAIT_LOAD_BEFORE_BODY,

    /** Supports 'data' protocol. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    PROTOCOL_DATA,

    /** Indicates .querySelectorAll() is not supported in quirks mode. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 8))
    QUERYSELECTORALL_NOT_IN_QUIRKS,

    /**
     * Indicates that all options of a select are deselected,
     * if the select state is changed for an unknown option.
     */
    @BrowserFeature(@WebBrowser(IE))
    SELECT_DESELECT_ALL_IF_SWITCHING_UNKNOWN,

    /**
     * Indicates that a read only JS property can potentially be set.
     * If supported, {@link net.sourceforge.htmlunit.corejs.javascript.ScriptableObject}.isReadOnlySettable()
     * will be checked,
     * if not supported, an exception will be thrown.
     */
    @BrowserFeature(@WebBrowser(FF))
    SET_READONLY_PROPERTIES,

    /** Indicates [object StorageObsolete] instead of [object Storage]. */
    @BrowserFeature({ @WebBrowser(value = FF, maxVersion = 10), @WebBrowser(CHROME) })
    STORAGE_OBSOLETE,

    /** Indicates that string.trim(), .trimLeft() and .trimRight() are supported. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    STRING_TRIM,

    /**
     * Indicates that the href property for a &lt;link rel="stylesheet" type="text/css" href="..." /&gt;
     * is the fully qualified URL.
     */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    STYLESHEET_HREF_EXPANDURL,

    /** Indicates that the href property for a &lt;style type="text/css"&gt; ... &lt;/style&gt; is "". */
    @BrowserFeature(@WebBrowser(IE))
    STYLESHEET_HREF_STYLE_EMPTY,

    /** Indicates that the href property for a &lt;style type="text/css"&gt; ... &lt;/style&gt; is null. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    STYLESHEET_HREF_STYLE_NULL,

    /** Set the value attribute of a submit input to 'Submit Query' if no value attribute specified. */
    @BrowserFeature(@WebBrowser(IE))
    SUBMITINPUT_DEFAULT_VALUE_IF_VALUE_NOT_DEFINED,

    /** Indicates if SVG is supported. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    SVG,

    /** Throws an exception if the value for column span is less than one. */
    @BrowserFeature(@WebBrowser(IE))
    TABLE_COLUMN_SPAN_THROWS_EXCEPTION_IF_LESS_THAN_ONE,

    /** The width column property does not return negative values. */
    @BrowserFeature(@WebBrowser(IE))
    TABLE_COLUMN_WIDTH_DOES_NOT_RETURN_NEGATIVE_VALUES,

    /** Indicates that "\n" are replaced by "\r\n" in textarea values. */
    @BrowserFeature(@WebBrowser(IE))
    TEXTAREA_CRNL,

    /** Indicates that the browser treats "position: fixed" as if it were "position: static". */
    @BrowserFeature(@WebBrowser(IE))
    TREATS_POSITION_FIXED_LIKE_POSITION_STATIC,

    /**
     * Indicates, that the pathname for the url 'about:blank' is empty;
     * instead of '/blank'.
     */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    URL_ABOUT_BLANK_HAS_EMPTY_PATH,

    /**
     * Indicates, that the browser supports username and password as
     * part of the url (e.g. http://john.smith:secret@localhost).
     */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    URL_AUTH_CREDENTIALS,

    /** Replace only ' ' with %20 when encode the query part of an url. */
    @BrowserFeature(@WebBrowser(IE))
    URL_MINIMAL_QUERY_ENCODING,

    /** */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    URL_MISSING_SLASHES,

    /** */
    @BrowserFeature({ @WebBrowser(IE), @WebBrowser(FF), @WebBrowser(CHROME) })
    WINDOW_ACTIVE_ELEMENT_FOCUSED,

    /** XMLHttpRequest does not trigger the error handler (IE). */
    @BrowserFeature(@WebBrowser(IE))
    XHR_ERRORHANDLER_NOT_SUPPORTED,

    /** Indicates that 'this' corresponds to the handler function when a XMLHttpRequest handler is executed. */
    XHR_HANDLER_THIS_IS_FUNCTION,

    /** Indicates if a same origin check should be skipped. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 8) })
    XHR_IGNORE_SAME_ORIGIN,

    /** Indicates if a request to a about URL is allowed. */
    @BrowserFeature(@WebBrowser(IE))
    XHR_IGNORE_SAME_ORIGIN_TO_ABOUT,

    /** Indicates that the onreadystatechange handler is triggered for sync requests for COMPLETED (4). */
    @BrowserFeature({ @WebBrowser(value = FF, minVersion = 10), @WebBrowser(CHROME) })
    XHR_ONREADYSTATECANGE_SYNC_REQUESTS_COMPLETED,

    /** Indicates that the onreadystatechange handler is not triggered for sync requests. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    XHR_ONREADYSTATECANGE_SYNC_REQUESTS_NOT_TRIGGERED,

    /** Indicates that the onreadystatechange handler is triggered with an event parameter (FF). */
    @BrowserFeature(@WebBrowser(FF))
    XHR_ONREADYSTATECHANGE_WITH_EVENT_PARAM,

    /** Indicates if a "Origin" header should be sent. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    XHR_ORIGIN_HEADER,

    /** Indicates that the onload handler is not triggered if completed (FF). */
    @BrowserFeature(@WebBrowser(FF))
    XHR_TRIGGER_ONLOAD_ON_COMPLETED,

    /** Indicates if XUL is supported (FF only). */
    @BrowserFeature(@WebBrowser(value = FF, maxVersion = 3.6f))
    XUL_SUPPORT;
}
