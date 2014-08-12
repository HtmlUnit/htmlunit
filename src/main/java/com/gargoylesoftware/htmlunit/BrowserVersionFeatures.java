/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
 * @author Frank Danek
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

    /** If the "type" attribute of HtmlButton should be evaluated to 'button' if not specified. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    BUTTON_EMPTY_TYPE_BUTTON,

    /** Is canvas supported? */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    CANVAS,

    /** Indicates that the browser can inherit CSS property values. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    CAN_INHERIT_CSS_PROPERTY_VALUES,

    /** Indicates that the default value for height of elements is 15 instead of 20. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    CSS_DEFAULT_ELEMENT_HEIGHT_15,

    /** Indicates that the default value for height of elements is 18 instead of 20. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    CSS_DEFAULT_ELEMENT_HEIGHT_18,

    /** Indicates that the default value for height of elements is used instead
     * of the calculated value, if the calculated value is smaller. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    CSS_DEFAULT_ELEMENT_HEIGHT_MARKS_MIN,

    /** Indicates that the default value for width is 'auto'. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    CSS_DEFAULT_WIDTH_AUTO,

    /** Was originally .isFirefox(). */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    CSS_DISPLAY_DEFAULT,

    /** <code>CSSFontFaceRule.cssText</code> uses \r\n to break lines. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    CSS_FONTFACERULE_CSSTEXT_CRLF,

    /** Default is 'Times New Roman'. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    CSS_FONT_FAMILY_DEFAULT_TIMES,

    /** Default is 'normal'. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    CSS_FONT_STRECH_DEFAULT_NORMAL,

    /** Indicates that the browser can surrounds image url's with quotes. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    CSS_IMAGE_URL_QUOTED,

    /** The default value of the display property for the 'keygen' tag is 'inline-block' instead of the default one. */
    @BrowserFeature(@WebBrowser(FF))
    CSS_KEYGEN_DISPLAY_INLINE_BLOCK,

    /** The default value of the display property for the 'noscript' tag is 'inline' instead of the default one. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    CSS_NOSCRIPT_DISPLAY_INLINE,

    /** Indicates that only integers are allowed for pixel value. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    CSS_PIXEL_VALUES_INT_ONLY,

    /** The default value of the display property for the 'progress' tag is 'inline' instead of the default one. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    CSS_PROGRESS_DISPLAY_INLINE,

    /** The default value of the display property for the 'script' tag is 'inline' instead of the default one. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    CSS_SCRIPT_DISPLAY_INLINE,

    /** Indicates that the :lang(..) selector is supported. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    CSS_SELECTOR_LANG,

    /** The default value of the display property for the 'select' tag is 'inline' instead of the default one. */
    @BrowserFeature(@WebBrowser(CHROME))
    CSS_SELECT_DISPLAY_INLINE,

    /** Throws exception on setting a CSS style value to null. */
    @BrowserFeature(@WebBrowser(IE))
    CSS_SET_NULL_THROWS,

    /** Internet Explorer versions 5 and later support the behavior property. The behavior property lets
     * you use CSS to attach a script to a specific element in order to implement
     * DHTML (Dynamic HTML) components.
     */
    @BrowserFeature(@WebBrowser(IE))
    CSS_SUPPORTS_BEHAVIOR_PROPERTY,

    /** Default is 'none'. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    CSS_TEXT_SHADOW_DEFAULT_NONE,

    /** zIndex is of type Integer. Other values are ignored (''). */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    CSS_ZINDEX_TYPE_INTEGER,

    /** IE uses the type Number for the zIndex Values (instead of String). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    CSS_ZINDEX_TYPE_NUMBER,

    /** If values for the zIndex is undefined than set the zindex to the default value. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    CSS_ZINDEX_UNDEFINED_FORCES_RESET,

    /** If values for the zIndex is undefined or null than set the zindex throws an error. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    CSS_ZINDEX_UNDEFINED_OR_NULL_THROWS_ERROR,

    /** */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    DIALOGWINDOW_REFERER,

    /** Indicates that "\n" are replaced by "\r\n" in textarea values. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    DISPLAYED_COLLAPSE,

    /** */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    DOCTYPE_4_0_TRANSITIONAL_STANDARDS,

    /** DOCTYPE is a Comment from JavaScript perspective. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    DOCTYPE_IS_COMMENT,

    /** IE removes all child text nodes, but FF preserves the first. */
    @BrowserFeature(@WebBrowser(IE))
    DOM_NORMALIZE_REMOVE_CHILDREN,

    /** <code>BeforeUnloadEvent</code> automatically gets the <code>type</code> 'beforeunload'. */
    @BrowserFeature(@WebBrowser(CHROME))
    EVENT_BEFOREUNLOAD_AUTO_TYPE,

    /** <code>Event.bubbles</code> and <code>Event.cancelable</code> are false as default. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11),
        @WebBrowser(value = FF, minVersion = 31) })
    EVENT_BUBBLES_AND_CANCELABLE_DEFAULT_FALSE,

    /** Triggers "DOMContentLoaded" event. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    EVENT_DOM_CONTENT_LOADED,

    /** Supports DOM level 2 events. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    EVENT_DOM_LEVEL_2,

    /** Supports DOM level 3 events. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    EVENT_DOM_LEVEL_3,

    /** Is setting 'focus' and 'blur' events of 'document', triggers the event for the descendants elements. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    EVENT_FOCUS_DOCUMENT_DESCENDANTS,

    /** Triggers "input" event. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    EVENT_INPUT,

    /** MouseEvent.button uses IE numbering scheme. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    EVENT_MOUSERVENT_BUTTON_CODE_IE,

    /** Triggers 'onbeforeunload' event handler using <code>Event</code>. */
    @BrowserFeature({ @WebBrowser(value = IE, maxVersion = 8) })
    EVENT_ONBEFOREUNLOAD_USES_EVENT,

    /** Triggers "onchange" event handler after "onclick" event handler. */
    @BrowserFeature(@WebBrowser(FF))
    EVENT_ONCHANGE_AFTER_ONCLICK,

    /** Triggers "onchange" event handler on losing focus. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    EVENT_ONCHANGE_LOSING_FOCUS,

    /** Triggers "onclick" event handler for the select only, not for the clicked option. */
    @BrowserFeature(@WebBrowser(IE))
    EVENT_ONCLICK_FOR_SELECT_ONLY,

    /** Triggers 'onclick' and 'ondblclick' event handler using <code>PointerEvent</code>. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    EVENT_ONCLICK_USES_POINTEREVENT,

    /** Triggers "onerror" if external loading of an external javascript failed. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    EVENT_ONERROR_EXTERNAL_JAVASCRIPT,

    /** <code>Event.bubbles</code> and <code>Event.cancelable</code> are false in 'onhashchange' event handler. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    EVENT_ONHASHCHANGE_BUBBLES_AND_CANCELABLE_FALSE,

    /** <code>Event.cancelable</code> is false in 'onload' event handler. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    EVENT_ONLOAD_CANCELABLE_FALSE,

    /** Triggers "onload" event if external javascript successfully loaded. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    EVENT_ONLOAD_EXTERNAL_JAVASCRIPT,

    /** Triggers "onload" event if an iframe was created by javascript and added to the page. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    EVENT_ONLOAD_IFRAME_CREATED_BY_JAVASCRIPT,

    /** Triggers "onload" event if internal javascript loaded. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    EVENT_ONLOAD_INTERNAL_JAVASCRIPT,

    /** Setting the 'onload' event handler to <code>undefined</code> throws an error. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    EVENT_ONLOAD_UNDEFINED_THROWS_ERROR,

    /** Does not trigger "onmousedown" event handler for the select options. */
    @BrowserFeature({ @WebBrowser(IE) })
    EVENT_ONMOUSEDOWN_FOR_SELECT_OPTION_TRIGGERS_ADDITIONAL_DOWN_FOR_SELECT,

    /** Does not trigger "onmousedown" event handler for the select options. */
    @BrowserFeature({ @WebBrowser(IE), @WebBrowser(CHROME) })
    EVENT_ONMOUSEDOWN_NOT_FOR_SELECT_OPTION,

    /** Does not trigger "onmousedown" event handler for the select options. */
    @BrowserFeature({ @WebBrowser(IE), @WebBrowser(CHROME) })
    EVENT_ONMOUSEUP_FOR_SELECT_OPTION_TRIGGERS_ADDITIONAL_UP_FOR_SELECT,

    /** Does not trigger "onmouseup" event handler for the select options. */
    @BrowserFeature({ @WebBrowser(IE), @WebBrowser(CHROME) })
    EVENT_ONMOUSEUP_NOT_FOR_SELECT_OPTION,

    /** Triggers "onreadystatechange" event. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    EVENT_ONREADY_STATE_CHANGE,

    /** Triggers "propertychange" event. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    EVENT_PROPERTY_CHANGE,

    /** Supports event type 'BeforeUnloadEvent'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    EVENT_TYPE_BEFOREUNLOADEVENT,

    /** Supports vendor specific event type 'Events'. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11), @WebBrowser(CHROME) })
    EVENT_TYPE_EVENTS,

    /** Supports event type 'HashChangeEvent'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    EVENT_TYPE_HASHCHANGEEVENT,

    /** Supports vendor specific event type 'KeyEvents'. */
    @BrowserFeature(@WebBrowser(FF))
    EVENT_TYPE_KEY_EVENTS,

    /** Supports event type 'PointerEvent'. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    EVENT_TYPE_POINTEREVENT,

    /** Indicates that document.execCommand() should throw an exception when called with an illegal command. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    EXECCOMMAND_THROWS_ON_WRONG_COMMAND,

    /** */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    FILEINPUT_EMPTY_DEFAULT_VALUE,

    /** For new pages the focus points to the body node. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    FOCUS_BODY_ELEMENT_AT_START,

    /** For new pages the focus points to the html root node. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    FOCUS_HTML_ELEMENT_AT_START,

    /** Indicates if a form field is directly reachable by its new name once this has been changed. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    FORMFIELD_REACHABLE_BY_NEW_NAMES,

    /** Indicates if a form field is directly reachable by its original name once this has been changed. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, maxVersion = 8) })
    FORMFIELD_REACHABLE_BY_ORIGINAL_NAME,

    /** Form submit is done without the hash part of the action url. */
    @BrowserFeature(@WebBrowser(IE))
    FORM_SUBMISSION_URL_WITHOUT_HASH,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    GENERATED_112,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_150,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_40,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    GENERATED_51,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_53,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(IE))
    GENERATED_63,

    /** */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    HEADER_CONTENT_DISPOSITION_ABSOLUTE_PATH,

    /** Indicates if HTML5 ruby tags ruby, tp and rt are recognized. */
    @BrowserFeature({ @WebBrowser(IE) })
    HTML5_RUBY_TAGS,

    /** Indicates if HTML5 tags source, video and audio are recognized. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11), @WebBrowser(CHROME) })
    HTML5_TAGS,

    /** */
    @BrowserFeature(@WebBrowser(IE))
    HTMLABBREVIATED,

    /** Should {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBaseFontElement#isEndTagForbidden}. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTMLBASEFONT_END_TAG_FORBIDDEN,

    /** Supports basefont. */
    @BrowserFeature(@WebBrowser(IE))
    HTMLBASEFONT_SUPPORTED,

    /** Set this checked state to false when added to page (IE). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    HTMLCHECKEDINPUT_SET_CHECKED_TO_FALSE_WHEN_CLONE,

    /** Set the default value based on the current value when clone (IE). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    HTMLCHECKEDINPUT_SET_DEFAULT_VALUE_WHEN_CLONE,

    /** Indicates that comment nodes should be treated similar to elements, e.g. getElementsByTagName(). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    HTMLCOLLECTION_COMMENT_IS_ELEMENT,

    /** HtmlCollection.item returns null instead of undefined if an element was not found. */
    @BrowserFeature(@WebBrowser(IE))
    HTMLCOLLECTION_NULL_IF_ITEM_NOT_FOUND,

    /** HtmlCollection returns null instead of undefined if an element was not found. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    HTMLCOLLECTION_NULL_IF_NOT_FOUND,

    /** Allow detection of object type for collection elements. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    HTMLCOLLECTION_OBJECT_DETECTION,

    /**
     * Supports Conditional Comments.
     * @see <a href="http://en.wikipedia.org/wiki/Conditional_comment">Conditional comment</a>
     */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    HTMLCONDITIONAL_COMMENTS,

    /** Is document.charset lower-case (and defaultCharset is 'windows-1252'). */
    @BrowserFeature(@WebBrowser(IE))
    HTMLDOCUMENT_CHARSET_LOWERCASE,

    /** Do a normalization of the charset names. */
    @BrowserFeature(@WebBrowser(FF))
    HTMLDOCUMENT_CHARSET_NORMALIZED,

    /** Do document.bgColor/.alinkColor/.vlinkColor/.linkColor have value by default. */
    @BrowserFeature(@WebBrowser(IE))
    HTMLDOCUMENT_COLOR,

    /** Calls to <code>document.XYZ</code> also looks at frames. */
    @BrowserFeature(@WebBrowser(IE))
    HTMLDOCUMENT_GET_ALSO_FRAMES,

    /** Calls to <code>document.XYZ</code> looks at children with the specified ID and/or name. */
    @BrowserFeature(@WebBrowser(IE))
    HTMLDOCUMENT_GET_FOR_ID_AND_OR_NAME,

    /** Calls to <code>document.XYZ</code> looks at children with the specified name. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTMLDOCUMENT_GET_FOR_NAME,

    /**
     * Calls to <code>document.XYZ</code> should first look at standard functions before looking at elements
     * named <code>XYZ</code>.
     */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    HTMLDOCUMENT_GET_PREFERS_STANDARD_FUNCTIONS,

    /** Allows invalid 'align' values. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTMLELEMENT_ALIGN_INVALID,

    /**
     * Indicates that attribute name should be fixed for get/setAttribute(), specifically "className" and "class",
     * only in quirks mode.
     */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    HTMLELEMENT_ATTRIBUTE_FIX_IN_QUIRKS_MODE,

    /** Indicates that element.innerHTML/outerHTML the tag name is in upper case or not. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    HTMLELEMENT_OUTER_HTML_UPPER_CASE,

    /**
     * Indicates outer/innerHtml quotes attributes.
     */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    HTMLELEMENT_OUTER_INNER_HTML_QUOTE_ATTRIBUTES,

    /**
     * Indicates if a self-closing &lt;iframe/&gt; tag should be considered as an opening tag.
     */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    HTMLIFRAME_IGNORE_SELFCLOSING,

    /** Handle blank source like empty. */
    @BrowserFeature(@WebBrowser(IE))
    HTMLIMAGE_BLANK_SRC_AS_EMPTY,

    /** Clicking an image input submits the value as param if defined. */
    @BrowserFeature(@WebBrowser(CHROME))
    HTMLIMAGE_NAME_VALUE_PARAMS,

    /** When clicking a input the the surrounding anchor is not clicked. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    HTMLINPUT_DOES_NOT_CLICK_SURROUNDING_ANCHOR,

    /** Setting defaultValue updates the value also. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11), @WebBrowser(CHROME) })
    HTMLINPUT_SET_DEFAULT_VALUE_UPDATES_VALUE,

    /** Setting value updates the defaultValue also. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11), @WebBrowser(CHROME) })
    HTMLINPUT_SET_VALUE_UPDATES_DEFAULT_VALUE,

    /** Attribute 'compact' may only be a boolean value. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    HTMLLIST_LIMIT_COMPACT_TO_BOOLEAN,

    /**
     * Set this property if the browser does NOT
     * support the disabling of an individual option group.
     */
    @BrowserFeature(@WebBrowser(IE))
    HTMLOPTIONGROUP_NO_DISABLED,

    /** */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(IE) })
    HTMLOPTION_EMPTY_TEXT_IS_NO_CHILDREN,

    /** */
    @BrowserFeature(@WebBrowser(IE))
    HTMLOPTION_PREVENT_DISABLED,

    /** Un-selecting an option in a (single-value) select causes the first option to become selected. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    HTMLOPTION_UNSELECT_SELECTS_FIRST,

    /** Indicates that for some elements, the empty text after it should be removed. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    HTMLPARSER_REMOVE_EMPTY_CONTENT,

    /**
     * Set this property if the script tag supports the
     * types 'application/javascript' and 'application/x-javascript'.
     */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTMLSCRIPT_APPLICATION_JAVASCRIPT,

    /** Trims the value of the type attribute before to verify it. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTMLSCRIPT_TRIM_TYPE,

    /** Setting defaultValue updates the value also. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTMLTEXTAREA_SET_DEFAULT_VALUE_UPDATES_VALUE,

    /** HTML attributes are always lower case. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, maxVersion = 8) })
    HTML_ATTRIBUTE_LOWER_CASE,

    /** Adds CData nodes as Comment elements to the DOM. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    HTML_CDATA_AS_COMMENT,

    /** Expand shorthand to 6-digit hex color codes. */
    @BrowserFeature({ @WebBrowser(value = IE, maxVersion = 8) })
    HTML_COLOR_EXPAND_SHORT_HEX,

    /** Expand #0 to #000000. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    HTML_COLOR_EXPAND_ZERO,

    /** Replace color names by their 6-digit hex color code. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    HTML_COLOR_REPLACE_NAME_BY_HEX,

    /** Do not allow anything invalid in color, but restrict to valid values (names and hex digits) only. */
    @BrowserFeature({ @WebBrowser(value = IE, minVersion = 11) })
    HTML_COLOR_RESTRICT,

    /** Do not allow anything invalid in color, but restrict to valid values (names and hex digits) only.
     * Fill up to 6 digits if shorter. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    HTML_COLOR_RESTRICT_AND_FILL_UP,

    /** Convert the color (name and hex code) to lower case. */
    @BrowserFeature(@WebBrowser(IE))
    HTML_COLOR_TO_LOWER,

    /** HTMLCommentElement instead of Comment. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    HTML_COMMENT_ELEMENT,

    /** Supports &lt;object&gt; classid attribute. */
    @BrowserFeature(@WebBrowser(IE))
    HTML_OBJECT_CLASSID,

    /** Additionally support dates in format "d/M/yyyy". */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(value = IE, maxVersion = 8) })
    HTTP_COOKIE_EXTENDED_DATE_PATTERNS,

    /** Indicates that the pas is extracted from the location (IE11).
     * Sample: from the location /foo/boo only /foo is used.
     */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    HTTP_COOKIE_EXTRACT_PATH_FROM_LOCATION,

    /** Indicates that the start date for two digits cookies is 1970
     * instead of 2000 (Two digits years are interpreted as 20xx
     * if before 1970 and as 19xx otherwise).
     */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(value = IE, maxVersion = 8) })
    HTTP_COOKIE_START_DATE_1970,

    /** Indicates that "host" HTTP header should be the first. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTTP_HEADER_HOST_FIRST,

    /** Indicates that the browser should ignore contents of inner head elements. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    IGNORE_CONTENTS_OF_INNER_HEAD,

    /** Setting the property align to arbitrary values is allowed. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_ALIGN_ACCEPTS_ARBITRARY_VALUES,

    /** Setting the property align of an input element ignores the value
     * if the value is one of center, justify, left or right.
     * For all other values an exception is still thrown.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_ALIGN_FOR_INPUT_IGNORES_VALUES,

    /** Top scope constants can be assign (and are not... constants). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_ALLOW_CONST_ASSIGNMENT,

    /**
     * Javascript property anchors includes all anchors with a name or an id property.
     * If not set name property is required
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_ANCHORS_REQUIRES_NAME_OR_ID,

    /** Indicates that the <code>ApplicationCache</code> is named <code>OfflineResourceList</code> instead. */
    @BrowserFeature(@WebBrowser(FF))
    JS_APPCACHE_NAME_OFFLINERESOURCELIST,

    /** Indicates that the appendChild call create a DocumentFragment to be
     * the parentNode's parentNode if this was null. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_APPEND_CHILD_CREATE_DOCUMENT_FRAGMENT_PARENT,

    /** Indicates that the appendChild call throws no exception
     * if the provided node cannot be inserted. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_APPEND_CHILD_THROWS_NO_EXCEPTION_FOR_WRONG_NODE,

    /** Indicates that the class name of "arguments" object is "Object". */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_ARGUMENTS_IS_OBJECT,

    /** Indicates that "someFunction.arguments" is a read-only view of the function's argument. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_ARGUMENTS_READ_ONLY_ACCESSED_FROM_FUNCTION,

    /** Indicates that getting an attribute by name (attributes.name) is case-sensitive. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_ATTRIBUTES_BY_NAME_CASE_SENSITIVE,

    /** Indicates that the attributes map contains empty attr
     * objects for all properties of the object (like IE does). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_ATTRIBUTES_CONTAINS_EMPTY_ATTR_FOR_PROPERTIES,

    /** firstChild and lastChild returns null for Attr (like IE does). */
    @BrowserFeature({ @WebBrowser(value = IE, maxVersion = 8), @WebBrowser(FF) })
    JS_ATTR_FIRST_LAST_CHILD_RETURNS_NULL,

    /** HTMLBGSoundElement reported as HTMLUnknownElement. */
    @BrowserFeature(@WebBrowser(FF))
    JS_BGSOUND_AS_UNKNOWN,

    /** Indicates that HTMLBlockElements returning 'HTMLElement'
     * as class name. */
    @BrowserFeature(@WebBrowser(FF))
    JS_BLOCK_COMMON_CLASS_NAME,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    JS_BODY_MARGINS_IE11,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_BODY_MARGINS_IE8,

    /** Indicates that the getBoundingClientRect adds an offset of 2. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_BOUNDING_CLIENT_RECT_OFFSET_TWO,

    /** Trying to change the type of a button element throws an exception (IE). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_BUTTON_SET_TYPE_THROWS_EXCEPTION,

    /** If we're emulating IE, the overall JavaScript return value is the last return value. */
    @BrowserFeature(@WebBrowser(IE))
    JS_CALL_RESULT_IS_LAST_RETURN_VALUE,

    /** toDataURL for canvas returns the IE11 version of the png. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    JS_CANVAS_DATA_URL_IE_PNG,

    /** Indicates that the browser emulates the char attribute. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_CHAR_EMULATED,

    /** Indicates that the browser emulates the charOff attribute. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_CHAR_OFF_EMULATED,

    /** Do not allow invalid clear values. */
    @BrowserFeature(@WebBrowser(IE))
    JS_CLEAR_RESTRICT,

    /** Indicates that the click method call does not trigger the onchange
     * event handlers for checkboxes. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_CLICK_CHECKBOX_TRIGGERS_NO_CHANGE_EVENT,

    /** Indicates that the clientLeft and clientTop returning zero in all cases. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_CLIENT_LEFT_TOP_ZERO,

    /** Indicates that the cloneNode call copies all event listeners. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_CLONE_NODE_COPIES_EVENT_LISTENERS,

    /** Indicates that "constructor" property is defined, e.g. <tt>document.constructor</tt>. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    JS_CONSTRUCTOR,

    /** item is not a enumerable property of CSSRuleList. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_CSSRULELIST_DONT_ENUM_ITEM,

    /** <code>Date.toLocaleDateString()</code> returns a short form (d.M.yyyy). */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_DATE_LOCALE_DATE_SHORT,

    /** <code>Date.toLocaleDateString()</code> returns a short form (dd.MM.yyyy) with some weird special chars. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    JS_DATE_LOCALE_DATE_SHORT_WITH_SPECIAL_CHARS,

    /** <code>Date.toLocaleTimeString()</code> returns a form with some weird special chars. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    JS_DATE_LOCALE_TIME_WITH_SPECIAL_CHARS,

    /** Is Date.toUTCString() and Date.toGMTString are returning UTC instead of GMT. */
    @BrowserFeature({ @WebBrowser(value = IE, maxVersion = 8) })
    JS_DATE_USE_UTC,

    /** */
    @BrowserFeature(@WebBrowser(IE))
    JS_DEFERRED,

    /** Object prototype supports <tt>__defineGetter__</tt> and similar properties. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    JS_DEFINE_GETTER,

    /** Indicates that HTMLDefinition...Elements returning 'HTMLElement'
     * as class name. */
    @BrowserFeature(@WebBrowser(FF))
    JS_DEFINITION_COMMON_CLASS_NAME,

    /** Javascript doctyp.entities returns an empty string (IE). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_DOCTYPE_ENTITIES_EMPTY_STRING,

    /** Javascript doctyp.entities returns null (FF10). */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    JS_DOCTYPE_ENTITIES_NULL,

    /** Javascript doctyp.notations returns an empty string (IE). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_DOCTYPE_NOTATIONS_EMPTY_STRING,

    /** Javascript doctyp.notations returns null (FF10). */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    JS_DOCTYPE_NOTATIONS_NULL,

    /** Javascript document.appendChild is allowed (IE). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_DOCUMENT_APPEND_CHILD_SUPPORTED,

    /** Javascript function document.createElement can process html code.
     * e.g. document.createElement("<INPUT TYPE='RADIO' NAME='RADIOTEST' VALUE='First Choice'>")
     * @see "http://msdn.microsoft.com/en-us/library/ms536389%28v=VS.85%29.aspx"
     */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_DOCUMENT_CREATE_ELEMENT_EXTENDED_SYNTAX,

    /** Javascript function document.createElement accepts only tag names. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME),
        @WebBrowser(value = IE, minVersion = 11) })
    JS_DOCUMENT_CREATE_ELEMENT_STRICT,

    /** Design mode constants start with a capital letter. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_DOCUMENT_DESIGN_MODE_CAPITAL_FIRST,

    /** The browser supports the design mode 'Inherit' (IE). */
    @BrowserFeature(@WebBrowser(IE))
    JS_DOCUMENT_DESIGN_MODE_INHERIT,

    /** The browser supports the design mode only for frames. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_DOCUMENT_DESIGN_MODE_ONLY_FOR_FRAMES,

    /** Javascript document.doctype returns null (IE). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_DOCUMENT_DOCTYPE_NULL,

    /** Javascript property document.domain is lowercase. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_DOCUMENT_DOMAIN_IS_LOWERCASE,

    /** dicument.elementFromPoint returns null if at least one point coordinat is zero or smaller. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    JS_DOCUMENT_ELEMENT_FROM_POINT_NULL_WHEN_OUTSIDE,

    /** Javascript document.forms(...) supported (IE). */
    @BrowserFeature(@WebBrowser(IE))
    JS_DOCUMENT_FORMS_FUNCTION_SUPPORTED,

    /** Javascript property document.domain doesn't allow to set domain of about:blank. */
    @BrowserFeature(@WebBrowser(IE))
    JS_DOCUMENT_SETTING_DOMAIN_THROWS_FOR_ABOUT_BLANK,

    /** If document.implementation.hasFeature() supports 'Core 1.0'. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    JS_DOMIMPLEMENTATION_FEATURE_CORE_1,

    /** If document.implementation.hasFeature() supports 'Core 1.0'. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_DOMIMPLEMENTATION_FEATURE_CORE_3,

    /** If document.implementation.hasFeature() supports 'CSS2 1.0'. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_DOMIMPLEMENTATION_FEATURE_CSS2_1,

    /** If document.implementation.hasFeature() supports 'CSS2 2.0'. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    JS_DOMIMPLEMENTATION_FEATURE_CSS2_2,

    /** If document.implementation.hasFeature() supports 'CSS2 3.0'. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_DOMIMPLEMENTATION_FEATURE_CSS2_3,

    /** If document.implementation.hasFeature() supports 'CSS3 1.0'. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_DOMIMPLEMENTATION_FEATURE_CSS3_1,

    /** If document.implementation.hasFeature() supports 'CSS3 2.0'. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_DOMIMPLEMENTATION_FEATURE_CSS3_2,

    /** If document.implementation.hasFeature() supports 'CSS3 3.0'. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_DOMIMPLEMENTATION_FEATURE_CSS3_3,

    /** If document.implementation.hasFeature() supports 'CSS 1.0'. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_DOMIMPLEMENTATION_FEATURE_CSS_1,

    /** If document.implementation.hasFeature() supports 'CSS 2.0'. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_DOMIMPLEMENTATION_FEATURE_CSS_2,

    /** If document.implementation.hasFeature() supports 'CSS 3.0'. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_DOMIMPLEMENTATION_FEATURE_CSS_3,

    /** If document.implementation.hasFeature() supports 'Events 1.0'. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_DOMIMPLEMENTATION_FEATURE_EVENTS_1,

    /** If document.implementation.hasFeature() supports 'Events 3.0'. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    JS_DOMIMPLEMENTATION_FEATURE_EVENTS_3,

    /** If document.implementation.hasFeature() supports 'HTML 3.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_HTML_3,

    /** If document.implementation.hasFeature() supports 'KeyboardEvents'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_KEYBOARDEVENTS,

    /** If document.implementation.hasFeature() supports 'LS'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_LS,

    /** If document.implementation.hasFeature() supports 'MouseEvents 1.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_MOUSEEVENTS_1,

    /** If document.implementation.hasFeature() supports 'MouseEvents 2.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    JS_DOMIMPLEMENTATION_FEATURE_MOUSEEVENTS_2,

    /** If document.implementation.hasFeature() supports 'MutationEvents 1.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_MUTATIONEVENTS_1,

    /** If document.implementation.hasFeature() supports 'MutationEvents 2.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    JS_DOMIMPLEMENTATION_FEATURE_MUTATIONEVENTS_2,

    /** If document.implementation.hasFeature() supports 'MutationNameEvents'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_MUTATIONNAMEEVENTS,

    /** If document.implementation.hasFeature() supports only 'HTML'. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_DOMIMPLEMENTATION_FEATURE_ONLY_HTML,

    /** If document.implementation.hasFeature() supports 'Range 1.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_RANGE_1,

    /** If document.implementation.hasFeature() supports 'Range 2.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    JS_DOMIMPLEMENTATION_FEATURE_RANGE_2,

    /** If document.implementation.hasFeature() supports 'Range 3.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_RANGE_3,

    /** If document.implementation.hasFeature() supports 'StyleSheets 2.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_STYLESHEETS,

    /** If document.implementation.hasFeature() supports 'http://www.w3.org/TR/SVG11/feature#BasicStructure 1.0'. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    JS_DOMIMPLEMENTATION_FEATURE_SVG_BASICSTRUCTURE_1_0,

    /** If document.implementation.hasFeature() supports 'http://www.w3.org/TR/SVG11/feature#Shape 1.0'. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    JS_DOMIMPLEMENTATION_FEATURE_SVG_SHAPE_1_0,

    /** If document.implementation.hasFeature() supports 'MutationNameEvents'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_TEXTEVENTS,

    /** If document.implementation.hasFeature() supports 'Traversal 1.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_TRAVERSAL_1,

    /** If document.implementation.hasFeature() supports 'Traversal 2.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    JS_DOMIMPLEMENTATION_FEATURE_TRAVERSAL_2,

    /** If document.implementation.hasFeature() supports 'Traversal 3.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_TRAVERSAL_3,

    /** If document.implementation.hasFeature() supports 'UIEvents 2.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_UIEVENTS_2,

    /** If document.implementation.hasFeature() supports 'UIEvents 3.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    JS_DOMIMPLEMENTATION_FEATURE_UIEVENTS_3,

    /** If document.implementation.hasFeature() supports 'Validation'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_VALIDATION,

    /** If document.implementation.hasFeature() supports 'Views 1.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_VIEWS_1,

    /** If document.implementation.hasFeature() supports 'Views 2.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    JS_DOMIMPLEMENTATION_FEATURE_VIEWS_2,

    /** If document.implementation.hasFeature() supports 'Views 3.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_VIEWS_3,

    /** If document.implementation.hasFeature() supports 'XHTML 1.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    JS_DOMIMPLEMENTATION_FEATURE_XHTML_1,

    /** If document.implementation.hasFeature() supports 'XHTML 3.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_XHTML_3,

    /** If document.implementation.hasFeature() supports 'XML 3.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_XML_3,

    /** If document.implementation.hasFeature() supports 'XPath 3.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_XPATH,

    /** <code>DOMParser.parseFromString(..)</code> handles an empty String as error. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMPARSER_EMPTY_STRING_IS_ERROR,

    /** <code>DOMParser.parseFromString(..)</code> throws an exception if an error occurs. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    JS_DOMPARSER_EXCEPTION_ON_ERROR,

    /** <code>DOMParser.parseFromString(..)</code> creates a document containing a <code>parsererror</code> element. */
    @BrowserFeature(@WebBrowser(FF))
    JS_DOMPARSER_PARSERERROR_ON_ERROR,

    /** DOMTokenList uses a enhanced set of whitespace chars. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    JS_DOMTOKENLIST_ENHANCED_WHITESPACE_CHARS,

    /** DOMTokenList removed all whitespace chars during edit. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    JS_DOMTOKENLIST_REMOVE_WHITESPACE_CHARS_ON_EDIT,

    /** Javascript property function delete thows an exception if the
     * given count is negative. */
    @BrowserFeature(@WebBrowser(IE))
    JS_DOM_CDATA_DELETE_THROWS_NEGATIVE_COUNT,

    /** Don't enumerate functions, see {@link net.sourceforge.htmlunit.corejs.javascript.ScriptableObject#DONTENUM}. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_DONT_ENUM_FUNCTIONS,

    /** Enables Javascript ECMA5 functions (like Date.toISOString or Date.toJSON). */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    JS_ECMA5_FUNCTIONS,

    /** Javascript calculation of element clientHeight/Width does not
     * include the padding.
     */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_ELEMENT_EXTENT_WITHOUT_PADDING,

    /** Element.removeAttirbute emoves the named property also. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_ELEMENT_REMOVE_ATTRIBUTE_REMOVES_PROPERTY,

    /** The Enumerator constructor throws an exception if called with HtmlCollections
     * as parameter. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    JS_ENUMERATOR_CONSTRUCTOR_THROWS,

    /** Indicates that for(x in y) should enumerate the numbers first. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME),
        @WebBrowser(value = IE, minVersion = 11) })
    JS_ENUM_NUMBERS_FIRST,

    /** Indicates that 'exception' (technically NativeError) exposes "stack" property. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    JS_ERROR_STACK,

    /** Indicates that "eval" function should have access to the local function scope. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_EVAL_LOCAL_SCOPE,

    /** Javascript event aborted check is based on the event handler return value (IE);
     * (standards-compliant browsers doing this via preventDefault).
     */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_EVENT_ABORTED_BY_RETURN_VALUE_FALSE,

    /** Javascript event.keyCode and event.charCode distinguish between printable and not printable keys. */
    @BrowserFeature(@WebBrowser(FF))
    JS_EVENT_DISTINGUISH_PRINTABLE_KEY,

    /** Javascript event handlers declared as property on a node don't receive the event as argument. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_EVENT_HANDLER_AS_PROPERTY_DONT_RECEIVE_EVENT,

    /** If an event handler has the value <code>undefined</code> <code>null</code> is returned instead. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    JS_EVENT_HANDLER_UNDEFINED_AS_NULL,

    /** Javascript event.keyCode returns undefined instead of zero if the keyCode is not set. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_EVENT_KEY_CODE_UNDEFINED,

    /** Do not send parameter in event handlers. */
    @BrowserFeature(@WebBrowser(IE))
    JS_EVENT_NO_PARAMETER,

    /** Indicates that the action property of a form is the fully qualified URL. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    JS_FORM_ACTION_EXPANDURL,

    /** Setting form.encoding only allowed for valid encodings. */
    @BrowserFeature(@WebBrowser(IE))
    JS_FORM_REJECT_INVALID_ENCODING,

    /** Support for document.formName('inputName'). */
    @BrowserFeature(@WebBrowser(IE))
    JS_FORM_USABLE_AS_FUNCTION,

    /** Indicated that the body of a not yet loaded frame/iframe is null. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_FRAME_BODY_NULL_IF_NOT_LOADED,

    /** Indicates if Function.bind is available. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME),
        @WebBrowser(value = IE, minVersion = 11) })
    JS_FUNCTION_BIND,

    /**
     * Indicates that function is defined even before its declaration, inside a block.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_FUNCTION_DECLARED_FORWARD_IN_BLOCK,

    /**
     * Indicates that function can be defined as
     * <code>function object.property() {}</code> instead of <code>object.property = function() {}</code>.
     */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_FUNCTION_OBJECT_METHOD,

    /** Indicates if the method toSource exists on the native objects. */
    @BrowserFeature(@WebBrowser(FF))
    JS_FUNCTION_TOSOURCE,

    /** Indicates that the getAttribute method supports IE style flags, only in quirks mode . */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_GET_ATTRIBUTE_SUPPORTS_FLAGS_IN_QUIRKS_MODE,

    /** Javascript function getBackgroundColor of computed styles returns the color as rgb. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    JS_GET_BACKGROUND_COLOR_FOR_COMPUTED_STYLE_AS_RGB,

    /** Javascript function getElementsByName returns an empty collection if called with empty string. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_GET_ELEMENTS_BY_NAME_EMPTY_RETURNS_NOTHING,

    /** Javascript function getElementsByName returns an empty collection if called with null. */
    @BrowserFeature({ @WebBrowser(IE), @WebBrowser(FF) })
    JS_GET_ELEMENTS_BY_NAME_NULL_RETURNS_NOTHING,

    /** Javascript function getElementsByTagName does not support namespaces. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_GET_ELEMENTS_BY_TAG_NAME_NOT_SUPPORTS_NAMESPACES,

    /** Javascript function getElementById calls getElementByName if nothing found by id, only in quirks mode. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_GET_ELEMENT_BY_ID_ALSO_BY_NAME_IN_QUICKS_MODE,

    /** Javascript function getElementById compares the id's case sensitive. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    JS_GET_ELEMENT_BY_ID_CASE_SENSITIVE,

    /** Indicates that objects with prototype property available in window scope; Firefox does this. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    JS_HAS_OBJECT_WITH_PROTOTYPE_PROPERTY_IN_WINDOW_SCOPE,

    /** History entries are enumerated as properties. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_HISTORY_ENUMS_ENTRIES,

    /** HTMLGenericElement instead of HTMLUnknownElement. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_HTML_GENERIC_ELEMENT_CLASS_NAME,

    /** HTMLElement instead of HTMLUnknownElement for elements with hyphen ('-'). */
    @BrowserFeature({ @WebBrowser(value = FF, minVersion = 31), @WebBrowser(CHROME) })
    JS_HTML_HYPHEN_ELEMENT_CLASS_NAME,

    /** IE ignores the last line containing uncommented. */
    @BrowserFeature(@WebBrowser(IE))
    JS_IGNORES_LAST_LINE_CONTAINING_UNCOMMENTED,

    /**
     * The complete property returns also true, if the image download was failing
     * or if there was no src at all.
     */
    @BrowserFeature(@WebBrowser(FF))
    JS_IMAGE_COMPLETE_RETURNS_TRUE_FOR_NO_REQUEST,
    /**
     * Getting the width and height of an image tag without a source returns 18x20;
     * for invalid values returns 1.
     */
    @BrowserFeature(@WebBrowser(value = FF, minVersion = 31))
    JS_IMAGE_WIDTH_HEIGHT_RETURNS_0x0_0x0,

    /**
     * Getting the width and height of an image tag without a source returns 18x20;
     * for invalid values returns 1.
     */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_IMAGE_WIDTH_HEIGHT_RETURNS_18x20_0x0,

    /**
     * Getting the width and height of an image tag without a source returns 28x30;
     * for invalid values returns 1.
     */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_IMAGE_WIDTH_HEIGHT_RETURNS_28x30_1x1,

    /**
     * Getting the width and height of an image tag without a source returns 28x30;
     * for invalid values returns same.
     */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    JS_IMAGE_WIDTH_HEIGHT_RETURNS_28x30_28x30,

    /** Indicates that innerHTML adds the child also for null values. */
    @BrowserFeature(@WebBrowser(IE))
    JS_INNER_HTML_ADD_CHILD_FOR_NULL_VALUE,

    /** Indicates that innerHTML creates a document fragment as parent node
     * if the receiver node has no parent at all. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_INNER_HTML_CREATES_DOC_FRAGMENT_AS_PARENT,

    /** Indicates that innerHTML is readonly for some tags. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_INNER_HTML_READONLY_FOR_SOME_TAGS,

    /** Indicates if multiple spaces are replaced by a single one when accessing innerHTML. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_INNER_HTML_REDUCE_WHITESPACES,

    /** the inner html of a script tag start always with a \r\n. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_INNER_HTML_SCRIPT_STARTSWITH_NEW_LINE,

    /** Indicates that innerText is readonly for tables. */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_INNER_TEXT_READONLY_FOR_TABLE,

    /** Setting the value of an Input Text/Password/TextArea resets the selection. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    JS_INPUT_SET_VALUE_MOVE_SELECTION_TO_START,

    /** Javascript function returning a length (e.g. getWidth) without 'px' at the end. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_LENGTH_WITHOUT_PX,

    /**
     * Set this property if the browser evaluates<br>
     * window.location.hash to #&uuml; (like Firefox)<br>
     * for url 'http://localhost/something/#%C3%BC'.<br>
     * IE evaluates to #%C3%BC.
     */
    @BrowserFeature({ @WebBrowser(value = IE, maxVersion = 8), @WebBrowser(FF) })
    JS_LOCATION_HASH_IS_DECODED,

    /**
     * Set this property if the browser evaluates<br>
     * window.location.hash to #%C3%BC; (like Firefox)<br>
     * for url 'http://localhost/something/#&uuml;'.<br>
     * IE evaluates to #&uuml;.
     */
    @BrowserFeature({ @WebBrowser(value = IE, maxVersion = 8), @WebBrowser(FF) })
    JS_LOCATION_HASH_IS_ENCODED,

    /**
     * Property location.hash returns '#' for urls ending with a hash
     * sign (e.g. http://localhost/something/#).
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_LOCATION_HASH_RETURNS_HASH_FOR_EMPTY_DEFINED,

    /**
     * Method mergeAttributes will merge all attribs instead only the
     * known/supported ones.
     */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_MERGE_ATTRIBUTES_ALL,

    /**
     * Indicates if the String representation of a native function begins and ends with a \n.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_NATIVE_FUNCTION_TOSTRING_NEW_LINE,

    /** <code>Node.childNodes</code> ignores empty text nodes for XML pages. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_NODE_CHILDNODES_IGNORE_EMPTY_TEXT_NODES,

    /** <code>Node.contains</code> returns false instead of throwing an exception. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    JS_NODE_CONTAINS_RETURNS_FALSE_FOR_INVALID_ARG,

    /** The reference argument of <code>Node.insertBefore(..)</code> is optional. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    JS_NODE_INSERT_BEFORE_REF_OPTIONAL,

    /** Should throw exception if extra argument is passed to node.insertBefore(). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_NODE_INSERT_BEFORE_THROW_EXCEPTION_FOR_EXTRA_ARGUMENT,

    /** Functions are enumerated. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_NODE_LIST_ENUMERATE_FUNCTIONS,

    /** If <tt>true</tt>, Date.prototype.getYear subtracts 1900 only if 1900 <= date < 2000. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_NON_ECMA_GET_YEAR,

    /** "[object]" in quirks mode. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_OBJECT_IN_QUIRKS_MODE,

    /** Indicates that someObj.offsetParent returns null, it someObj has fixed style.
     */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    JS_OFFSET_PARENT_NULL_IF_FIXED,

    /** Indicates that someObj.offsetParent throws an exception when called on an object that is not yet attached
     *  to the page's DOM.
     */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_OFFSET_PARENT_THROWS_NOT_ATTACHED,

    /** Was originally .isIE(). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_OFFSET_PARENT_USE_TABLES_IF_FIXED,

    /** Setting the property opacity of an css style declaration to arbitrary values is allowed.
     * FF accepts only valid floats.
     */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_OPACITY_ACCEPTS_ARBITRARY_VALUES,

    /** Indicates that new option(..) does not update the label property (IE8). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_OPTION_CONSTRUCTOR_IGNORES_LABEL,

    /** Was originally .isFirefox(). */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    JS_OPTION_USE_TEXT_AS_VALUE_IF_NOT_DEFINED,

    /** element.outerHTML handles the body and head tag as readonly (IE). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_OUTER_HTML_BODY_HEAD_READONLY,

    /** element.outerHTML handles null value as string "null" (IE). */
    @BrowserFeature(@WebBrowser(IE))
    JS_OUTER_HTML_NULL_AS_STRING,

    /** element.outerHTML removes all children from detached node (IE). */
    @BrowserFeature(@WebBrowser(IE))
    JS_OUTER_HTML_REMOVES_CHILDS_FOR_DETACHED,

    /** element.outerHTML removes all children from detached node (IE). */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_OUTER_HTML_THROWS_FOR_DETACHED,

    /** element.outerHTML throws an exception, if the new tag will close
     * the outer one when parsing the html source (IE).
     */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_OUTER_HTML_THROW_EXCEPTION_WHEN_CLOSES,

    /** If <tt>true</tt>, then treat <tt>__parent__</tt> and <tt>__proto__</tt> as special properties. */
    @BrowserFeature(@WebBrowser(IE))
    JS_PARENT_PROTO_PROPERTIES,

    /** Indicates that parseInt() should have radix 10 by default. */
    @BrowserFeature({ @WebBrowser(value = IE, minVersion = 11), @WebBrowser(FF),
        @WebBrowser(CHROME) })
    JS_PARSE_INT_RADIX_10,

    /** Indicates that HTMLPhraseElements returning 'HTMLElement'
     * as class name. */
    @BrowserFeature(@WebBrowser(FF))
    JS_PHRASE_COMMON_CLASS_NAME,

    /** Indicates that the prefix property returns an empty string if no prefix defined. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_PREFIX_RETURNS_EMPTY_WHEN_UNDEFINED,

    /** <code>RegExp.lastParen</code> returns an empty string if the RegExp has too many groups. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    JS_REGEXP_EMPTY_LASTPAREN_IF_TOO_MANY_GROUPS,

    /** RegExp group <code>$0</code> returns the whole previous match (see {@link java.util.regex.Matcher#group()}. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    JS_REGEXP_GROUP0_RETURNS_WHOLE_MATCH,

    /** Javascript script.text(...) reexecutes the script (IE). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_SCREEN_SETTER_THROWS_ERROR,

    /** Javascript script.text(...) reexecutes the script (IE). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_SCRIPT_ALWAYS_REEXECUTE_ON_SET_TEXT,

    /**
     * Always execute the script if IE;
     * in FF, only execute if the old "src" attribute was undefined
     * and there was no inline code.
     */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_SCRIPT_ALWAYS_REEXECUTE_ON_SRC_CHANGE,

    /** Javascript script.appendChild throws an error (IE6-IE8). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_SCRIPT_APPEND_CHILD_THROWS_EXCEPTION,

    /** Javascript script.insertBefore throws an error (IE6-IE8). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_SCRIPT_INSERT_BEFORE_THROWS_EXCEPTION,

    /** Javascript script tags supports the 'for' and the 'event'
     * attribute (IE).
     */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_SCRIPT_SUPPORTS_FOR_AND_EVENT_ELEMENT_BY_ID,

    /** Javascript script tags supports the 'for' and the 'event'
     * attribute (IE).
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_SCRIPT_SUPPORTS_FOR_AND_EVENT_WINDOW,

    /** Javascript script object supports the onreadystatechange event (IE). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_SCRIPT_SUPPORTS_ONREADYSTATECHANGE,

    /** If true the content of a selection is it's default value instead of toString. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    JS_SELECTION_CONTENT_IS_DEFAULT_VALUE,

    /** Only one selection per page. */
    @BrowserFeature(@WebBrowser(IE))
    JS_SELECTION_ONLY_ONE_PER_PAGE,

    /** Javascript selectorText property returns selectors in uppercase. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_SELECTOR_TEXT_UPPERCASE,

    /** Indicates if calling HTMLSelectElement.add the second parameter
     * is treated as index like IE does.
     */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_SELECT_ADD_SECOND_PARAM_IS_INDEX,

    /** Indicates if calling HTMLSelectElement.item with a negative value should throw. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_SELECT_ITEM_THROWS_IF_NEGATIVE,

    /** When expanding the collection by setting the length don't add
     * a empty text node. */
    @BrowserFeature(@WebBrowser(IE))
    JS_SELECT_OPTIONS_DONT_ADD_EMPTY_TEXT_CHILD_WHEN_EXPANDING,

    /** Indicates that select.options throws an exception if the requested index is neagtive (IE8). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_SELECT_OPTIONS_EXCEPTION_FOR_NEGATIVE,

    /** Indicates that select.options.childNodes is a valid property (IE). */
    @BrowserFeature(@WebBrowser(IE))
    JS_SELECT_OPTIONS_HAS_CHILDNODES_PROPERTY,

    /** Indicates that select.options has a wong class name (IE11). */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    JS_SELECT_OPTIONS_HAS_SELECT_CLASS_NAME,

    /** Ignore negative value when setting the length (FF24). */
    @BrowserFeature(@WebBrowser(FF))
    JS_SELECT_OPTIONS_IGNORE_NEGATIVE_LENGTH,

    /** Indicates that select.options returns null if requested index is outside (IE). */
    @BrowserFeature(@WebBrowser(IE))
    JS_SELECT_OPTIONS_NULL_FOR_OUTSIDE,

    /** Indicates that select.value = 'val' only checks the value attribute and
     * not the option text. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    JS_SELECT_SET_VALUES_CHECKS_ONLY_VALUE_ATTRIBUTE,

    /** Indicates that the set attribute method is able to update the event handlers also.
     * e.g. element.setAttribute("onclick", "test(1);"); */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    JS_SET_ATTRIBUTE_SUPPORTS_EVENT_HANDLERS,

    /**
     * When <tt>setInterval()</tt> is called with a 0 millisecond delay, Internet Explorer turns it
     * into a <tt>setTimeout()</tt> call.
     */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_SET_INTERVAL_ZERO_TIMEOUT_FORCES_SET_TIMEOUT,

    /** When addressing an item in a stylesheet list using a negative index an exception is thrown. */
    @BrowserFeature({ @WebBrowser(value = FF, maxVersion = 24), @WebBrowser(value = IE, maxVersion = 8) })
    JS_STYLESHEETLIST_EXCEPTION_FOR_NEGATIVE_INDEX,

    /**
     * When addressing an item in a stylesheet list using an index higher than the count of contained items an
     * exception is thrown.
     */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_STYLESHEETLIST_EXCEPTION_FOR_TOO_HIGH_INDEX,

    /** Indicates if style.getAttribute supports a (second) flags argument. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_STYLE_GET_ATTRIBUTE_SUPPORTS_FLAGS,

    /** Indicates if style.removeAttribute supports a (second) flags argument. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_STYLE_REMOVE_ATTRIBUTE_SUPPORTS_FLAGS,

    /** Indicates if style.setAttribute supports a (second) flags argument. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_STYLE_SET_ATTRIBUTE_SUPPORTS_FLAGS,

    /** IE supports accessing unsupported style elements via getter
     * like val = elem.style.htmlunit;.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_STYLE_UNSUPPORTED_PROPERTY_GETTER,

    /** The width cell height does not return negative values. */
    @BrowserFeature(@WebBrowser(IE))
    JS_TABLE_CELL_HEIGHT_DOES_NOT_RETURN_NEGATIVE_VALUES,

    /** Value of attribute 'nowrap' is always set to true if a not empty value is set. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_TABLE_CELL_NOT_EMPTY_ALWAYS_TRUE,

    /** Attribute 'nowrap' has value true instead of empty if set. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_TABLE_CELL_NOWRAP_VALUE_TRUE_IF_SET,

    /** The width cell offset calculation takes border into account. */
    @BrowserFeature(@WebBrowser(IE))
    JS_TABLE_CELL_OFFSET_INCLUDES_BORDER,

    /** The width cell property does not return negative values. */
    @BrowserFeature(@WebBrowser(IE))
    JS_TABLE_CELL_WIDTH_DOES_NOT_RETURN_NEGATIVE_VALUES,

    /** The width column property does not return negative values. */
    @BrowserFeature(@WebBrowser(IE))
    JS_TABLE_COLUMN_WIDTH_NO_NEGATIVE_VALUES,

    /** Calling deleteCell without an index throws an exeption. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_TABLE_ROW_DELETE_CELL_REQUIRES_INDEX,

    /** Value of attribute 'sectionRowIndex' is a big int if the row is not attached to a table. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_TABLE_ROW_SECTION_INDEX_BIG_INT_IF_UNATTACHED,

    /** When trying to set a table caption although there is already one an error is thrown. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_TABLE_SET_CAPTION_ALTHOUGH_ALREADY_SET_THROWS_ERROR,

    /** When trying to set a table footer (tfoot) although there is already one an error is thrown. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_TABLE_SET_TFOOT_ALTHOUGH_ALREADY_SET_THROWS_ERROR,

    /** When trying to set a table header (thead) although there is already one an error is thrown. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_TABLE_SET_THEAD_ALTHOUGH_ALREADY_SET_THROWS_ERROR,

    /** Throws an exception if the value for column span is less than one. */
    @BrowserFeature(@WebBrowser(IE))
    JS_TABLE_SPAN_THROWS_EXCEPTION_IF_INVALID,

    /** Indicates that table elements supports the values "top", "bottom", "middle", "baseline" (IE). */
    @BrowserFeature(@WebBrowser(IE))
    JS_TABLE_VALIGN_SUPPORTS_IE_VALUES,

    /** Setting the property cols throws an exception, if the provided value is less
     * than 0 (IE).
     * FF ignores the provided value in this case.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_TEXT_AREA_SET_COLS_NEGATIVE_THROWS_EXCEPTION,

    /** Setting the property cols throws an exception, if the provided value is not
     * convertible into an integer (IE).
     * FF ignores the provided value in this case and sets cols to 0.
     */
    @BrowserFeature({ @WebBrowser(IE), @WebBrowser(FF) })
    JS_TEXT_AREA_SET_COLS_THROWS_EXCEPTION,

    /** Setting the property rows throws an exception, if the provided value is less
     * than 0 (IE).
     * FF ignores the provided value in this case.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_TEXT_AREA_SET_ROWS_NEGATIVE_THROWS_EXCEPTION,

    /** Setting the property rows throws an exception, if the provided value is not
     * convertible into an integer (IE).
     * FF ignores the provided value in this case and sets rows to 0.
     */
    @BrowserFeature({ @WebBrowser(IE), @WebBrowser(FF) })
    JS_TEXT_AREA_SET_ROWS_THROWS_EXCEPTION,

    /** Indicates that <code>TreeWalker.expandEntityReferences</code> is always <code>false</code>. */
    @BrowserFeature(@WebBrowser(FF))
    JS_TREEWALKER_EXPAND_ENTITY_REFERENCES_FALSE,

    /**
     * Indicates that the filter to be used by the TreeWalker has to be a function (so no object with a method
     * <code>acceptNode(..)</code> is supported).
     */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    JS_TREEWALKER_FILTER_FUNCTION_ONLY,

    /** Setting the property align to arbitrary values is allowed. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_TYPE_ACCEPTS_ARBITRARY_VALUES,

    /** Setting the property width/heigth to arbitrary values is allowed. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_WIDTH_HEIGHT_ACCEPTS_ARBITRARY_VALUES,

    /** Changing the opener of a window to something not null is not valid. */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_WINDOW_CHANGE_OPENER_NOT_ALLOWED,

    /** Changing the opener of a window to something not null and not a window is not valid. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    JS_WINDOW_CHANGE_OPENER_ONLY_WINDOW_OBJECT,

    /** <code>window.name</code> returns also form fields (e.g. input, textarea). */
    @BrowserFeature(@WebBrowser(IE))
    JS_WINDOW_FORMFIELDS_ACCESSIBLE_BY_NAME,

    /** Support for accessing the frame of a window by id additionally to using the name (FF). */
    @BrowserFeature({ @WebBrowser(IE) })
    JS_WINDOW_FRAMES_ACCESSIBLE_BY_ID,

    /** <code>window..frames['id']</code> returns the frame window instead of the frame element. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    JS_WINDOW_FRAME_BY_ID_RETURNS_WINDOW,

    /** Window property usable as function. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_WINDOW_IS_A_FUNCTION,

    /** <code>Window.onerror</code> gets the column number as 4th argument. */
    @BrowserFeature({ @WebBrowser(value = IE, minVersion = 11), @WebBrowser(value = FF, minVersion = 31) })
    JS_WINDOW_ONERROR_COLUMN_ARGUMENT,

    /** <code>Window.onerror</code> gets the column number as 5th argument. */
    @BrowserFeature(@WebBrowser(value = FF, minVersion = 31))
    JS_WINDOW_ONERROR_ERROR_ARGUMENT,

    /**
     * Difference of window.outer/inner height is 57.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_57,

    /** Window.postMessage is sent when the targetOrigin port is different than the current port. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_WINDOW_POST_MESSAGE_ALLOW_INVALID_PORT,

    /** Window.postMessage created cancelable event. */
    @BrowserFeature(@WebBrowser(value = FF, maxVersion = 24))
    JS_WINDOW_POST_MESSAGE_CANCELABLE,

    /** Window.postMessage is synchronous. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_WINDOW_POST_MESSAGE_SYNCHRONOUS,

    /** Supports XML. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_XML,

    /** XMLDocument: .getElementById() to return any element, not HTML specifically. */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_XML_GET_ELEMENT_BY_ID__ANY_ELEMENT,

    /** Indicates that XML code embedded in an HTML page is handled by MSXML ActiveX. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_XML_IN_HTML_VIA_ACTIVEXOBJECT,

    /** Indicates that new XMLSerializer().serializeToString(..) adds the xhtml namespace to the root element. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    JS_XML_SERIALIZER_ADD_XHTML_NAMESPACE,

    /** Indicates that new XMLSerializer().serializeToString(..) always appends a CRLF at the end
     * of the produced string. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    JS_XML_SERIALIZER_APPENDS_CRLF,

    /** Indicates that new XMLSerializer().serializeToString(..) inserts a blank before self-closing a tag. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    JS_XML_SERIALIZER_BLANK_BEFORE_SELF_CLOSING,

    /**
     * Indicates that new XMLSerializer().serializeToString(..) called with a document fragment created by an
     * HTMLPage always returns ''.
     */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    JS_XML_SERIALIZER_HTML_DOCUMENT_FRAGMENT_ALWAYS_EMPTY,

    /** Indicates that new XMLSerializer().serializeToString(..) respects the XHTML definition for non empty tags. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_XML_SERIALIZER_NON_EMPTY_TAGS,

    /** Indicates that <code>XMLSerializer.serializeToString(..)</code> serializes a single CDataSection as escaped
     * text instead of <code>&lt;![CDATA[xxx]]&gt;</code>. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    JS_XML_SERIALIZER_ROOT_CDATA_AS_ESCAPED_TEXT,

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
    @BrowserFeature(@WebBrowser(IE))
    META_X_UA_COMPATIBLE,

    /** If true, then silently ignore element.appendChild(element). */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    NODE_APPEND_CHILD_SELF_IGNORE,

    /** Body of a &lt;noscript&gt; tag is not totally ignored but considered as a (not displayed) text node. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
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

    /** Indicates <code>.querySelectorAll()</code> and <code>.querySelector()</code> is not supported in quirks mode. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 8))
    QUERYSELECTORALL_NOT_IN_QUIRKS,

    /** Document mode is always 5 in quirks mode ignoring the browser version. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    QUIRKS_MODE_ALWAYS_DOC_MODE_5,

    /** Set the value attribute of a reset input to 'Reset' if no value attribute specified. */
    @BrowserFeature(@WebBrowser(IE))
    RESETINPUT_DEFAULT_VALUE_IF_VALUE_NOT_DEFINED,

    /** Set the default value attribute of a reset input to 'undefined' if declared as 'Reset'. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    RESETINPUT_DEFAULT_VALUE_UNDEFINED,

    /** Indicates that escaping in attribute selectors is supported. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    SELECTOR_ATTRIBUTE_ESCAPING,

    /**
     * Indicates that all options of a select are deselected,
     * if the select state is changed for an unknown option.
     */
    @BrowserFeature(@WebBrowser(IE))
    SELECT_DESELECT_ALL_IF_SWITCHING_UNKNOWN,

    /**
     * Indicates that a read only JS property can potentially be set.
     * If supported, {@link net.sourceforge.htmlunit.corejs.javascript.ScriptableObject}.isReadOnlySettable()
     * will be checked, if not supported, an exception will be thrown.
     */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    SET_READONLY_PROPERTIES,

    /** Indicates that string.contains() is supported. */
    @BrowserFeature(@WebBrowser(FF))
    STRING_CONTAINS,

    /** Indicates that string.trim() is supported. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    STRING_TRIM,

    /** Indicates that string.trimLeft() and .trimRight() are supported. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    STRING_TRIM_LEFT_RIGHT,

    /**
     * Indicates that the href property for a &lt;link rel="stylesheet" type="text/css" href="" /&gt;
     * (href empty) is null.
     */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    STYLESHEET_HREF_EMPTY_IS_NULL,

    /**
     * Indicates that the href property for a &lt;link rel="stylesheet" type="text/css" href="..." /&gt;
     * is the fully qualified URL.
     */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    STYLESHEET_HREF_EXPANDURL,

    /** Indicates that the href property for a &lt;style type="text/css"&gt; ... &lt;/style&gt; is "". */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    STYLESHEET_HREF_STYLE_EMPTY,

    /** Indicates that the href property for a &lt;style type="text/css"&gt; ... &lt;/style&gt; is null. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    STYLESHEET_HREF_STYLE_NULL,

    /** Set the value attribute of a submit input to 'Submit Query' if no value attribute specified. */
    @BrowserFeature(@WebBrowser(IE))
    SUBMITINPUT_DEFAULT_VALUE_IF_VALUE_NOT_DEFINED,

    /** Set the default value attribute of a submit input to 'undefined' if declared as 'Reset'. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    SUBMITINPUT_DEFAULT_VALUE_UNDEFINED,

    /** Indicates if SVG is supported. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    SVG,

    /** Indicates that unknown tags inside an SVG element are handled as DOM elements, not SVG elements. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    SVG_UNKNOWN_ARE_DOM,

    /** Indicates that "\n" are replaced by "\r\n" in textarea values. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    TEXTAREA_CRNL,

    /** Indicates that the browser treats "position: fixed" as if it were "position: static". */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
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

    /** XMLHttpRequest does not trigger the error handler. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    XHR_ERRORHANDLER_NOT_SUPPORTED,

    /** XMLHttpRequest triggers the opened event at the beginning of the send
     * method again.
     */
    @BrowserFeature({ @WebBrowser(IE), @WebBrowser(value = FF, minVersion = 31) })
    XHR_FIRE_STATE_OPENED_AGAIN_IN_ASYNC_MODE,

    /**
     * Indicates if the port should be ignored during origin check.
     * For IE11: this is currently a bug, see
     * http://connect.microsoft.com/IE/feedback/details/781303/
     * origin-header-is-not-added-to-cors-requests-to-same-domain-but-different-port
     */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    XHR_IGNORE_PORT_FOR_SAME_ORIGIN,

    /** A cross origin request to about:blank is not allowed. */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    XHR_NO_CROSS_ORIGIN_TO_ABOUT,

    /** Indicates that the onreadystatechange handler is triggered for sync requests for COMPLETED (4). */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME),
        @WebBrowser(value = IE, minVersion = 11) })
    XHR_ONREADYSTATECANGE_SYNC_REQUESTS_COMPLETED,

    /** Indicates that the onreadystatechange handler is not triggered for sync requests. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    XHR_ONREADYSTATECANGE_SYNC_REQUESTS_NOT_TRIGGERED,

    /** Indicates that the onreadystatechange handler is triggered with an event parameter (FF). */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    XHR_ONREADYSTATECHANGE_WITH_EVENT_PARAM,

    /** Indicates if an empty url is allowed as url param for the open method. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    XHR_OPEN_ALLOW_EMTPY_URL,

    /** Indicates if a "Origin" header should be sent. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    XHR_ORIGIN_HEADER,

    /** Indicates that <code>responseXML</code> returns an MXSML ActiveX object. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    XHR_RESPONSE_XML_IS_ACTIVEXOBJECT,

    /** Indicates that the impl throws an exception when accessing the status/statusText
     * property in unset state. */
    @BrowserFeature(@WebBrowser(value = IE, maxVersion = 8))
    XHR_STATUS_THROWS_EXCEPTION_WHEN_UNSET,

    /** Indicates that the onload handler is not triggered if completed (FF). */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    XHR_TRIGGER_ONLOAD_ON_COMPLETED,

    /** Indicates that the "*" pattern is allowed when withCredential is enabled. */
    @BrowserFeature(@WebBrowser(IE))
    XHR_WITHCREDENTIALS_ALLOW_ORIGIN_ALL,

    /**
     * Indicates that the property <code>withCredentials</code> is not writable before calling <code>open()</code>.
     * Setting the property throws an exception.
     */
    @BrowserFeature(@WebBrowser(value = IE, minVersion = 11))
    XHR_WITHCREDENTIALS_NOT_WRITEABLE_BEFORE_OPEN_EXCEPTION,

    /**
     * Indicates that the property <code>withCredentials</code> is not writable for sync requests.
     * Setting the property throws an exception.
     */
    @BrowserFeature(@WebBrowser(FF))
    XHR_WITHCREDENTIALS_NOT_WRITEABLE_IN_SYNC_EXCEPTION,

    /** Indicates that the 'SelectionNamespaces' property is supported by XPath expressions. */
    @BrowserFeature({ @WebBrowser(IE), @WebBrowser(CHROME) })
    XPATH_SELECTION_NAMESPACES,
}
