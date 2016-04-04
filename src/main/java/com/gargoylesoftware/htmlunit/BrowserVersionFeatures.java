/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.javascript.configuration.BrowserFeature;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.event.PopStateEvent;
import com.gargoylesoftware.htmlunit.javascript.host.intl.DateTimeFormat;

/**
 * Constants of various features of each {@link BrowserVersion}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Frank Danek
 * @author Carsten Steul
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

    /** Ignore target when {@code href} is a javascript snippet. */
    @BrowserFeature(@WebBrowser(CHROME))
    ANCHOR_IGNORE_TARGET_FOR_JS_HREF,

    /**
     * Is the default display style of Applet is 'inline-block'.
     */
    @BrowserFeature(@WebBrowser(FF))
    APPLET_INLINE_BLOCK,

    /** Background image is 'initial'. */
    @BrowserFeature(@WebBrowser(CHROME))
    CSS_BACKGROUND_INITIAL,

    /** Computed {@code display} is {@code block} for non-attached elements. */
    @BrowserFeature(@WebBrowser(FF))
    CSS_COMPUTED_BLOCK_IF_NOT_ATTACHED,

    /** Computed {@code zIndex} is not considered. */
    @BrowserFeature(@WebBrowser(CHROME))
    CSS_COMPUTED_NO_Z_INDEX,

    /** Indicates that the default value for height of elements is 18 instead of 20. */
    @BrowserFeature({ @WebBrowser(IE), @WebBrowser(CHROME) })
    CSS_DEFAULT_ELEMENT_HEIGHT_18,

    /** Is display style of HtmlDialog is 'none'. */
    @BrowserFeature(@WebBrowser(CHROME))
    CSS_DIALOG_NONE,

    /** Is display style 'block'. */
    @BrowserFeature(@WebBrowser(FF))
    CSS_DISPLAY_BLOCK,

    /** Is display style 'block'. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    CSS_DISPLAY_BLOCK2,

    /** {@code CSSFontFaceRule.cssText} uses {@code \r\n} to break lines. */
    @BrowserFeature(@WebBrowser(IE))
    CSS_FONTFACERULE_CSSTEXT_CRLF,

    /** {@code CSSFontFaceRule.cssText} has no {@code \n}. */
    @BrowserFeature(@WebBrowser(CHROME))
    CSS_FONTFACERULE_CSSTEXT_NO_CRLF,

    /** The default value of the display property for the 'input' tags is 'inline-block'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(IE) })
    CSS_INPUT_DISPLAY_INLINE_BLOCK,

    /** The default value of the display property for the 'keygen' tag is always 'inline'. */
    @BrowserFeature(@WebBrowser(IE))
    CSS_KEYGEN_DISPLAY_INLINE_ALWAYS,

    /** The default value of the display property for the 'keygen' tag is 'inline' when created by JavaScript. */
    @BrowserFeature(@WebBrowser(FF))
    CSS_KEYGEN_DISPLAY_INLINE_JS,

    /** Is display style of HtmlNoEmbed is 'inline'. */
    @BrowserFeature(@WebBrowser(CHROME))
    CSS_NOEMBED_INLINE,

    /** The default value of the display property for the 'noscript' tag is 'inline' instead of the default one. */
    @BrowserFeature(@WebBrowser(CHROME))
    CSS_NOSCRIPT_DISPLAY_INLINE,

    /** The default value of the display property for the 'progress' tag is 'inline' instead of the default one. */
    @BrowserFeature(@WebBrowser(IE))
    CSS_PROGRESS_DISPLAY_INLINE,

    /** The default value of the display property for the 'rp' tag is 'none'. */
    @BrowserFeature(@WebBrowser(FF))
    CSS_RP_DISPLAY_NONE,

    /** The default value of the display property for the 'rt' tag is always 'ruby-text'. */
    @BrowserFeature({ @WebBrowser(IE), @WebBrowser(FF) })
    CSS_RT_DISPLAY_RUBY_TEXT_ALWAYS,

    /** The default value of the display property for the 'ruby' tag is 'inline'. */
    @BrowserFeature(@WebBrowser(CHROME))
    CSS_RUBY_DISPLAY_INLINE,

    /** Throws exception on setting a CSS style value to null. */
    @BrowserFeature(@WebBrowser(IE))
    CSS_SET_NULL_THROWS,

    /** Internet Explorer versions 5 and later support the behavior property. The behavior property lets
     * you use CSS to attach a script to a specific element in order to implement
     * DHTML (Dynamic HTML) components.
     */
    @BrowserFeature(@WebBrowser(IE))
    CSS_SUPPORTS_BEHAVIOR_PROPERTY,

    /** zIndex is of type Integer. Other values are ignored (''). */
    @BrowserFeature(@WebBrowser(IE))
    CSS_ZINDEX_TYPE_INTEGER,

    /** */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    DIALOGWINDOW_REFERER,

    /** DOCTYPE has undefined value for 'prefix'. */
    @BrowserFeature(@WebBrowser(CHROME))
    DOCTYPE_PREFIX_UNDEFINED,

    /** IE removes all child text nodes, but FF preserves the first. */
    @BrowserFeature(@WebBrowser(IE))
    DOM_NORMALIZE_REMOVE_CHILDREN,

    /** <code>BeforeUnloadEvent</code> automatically gets the <code>type</code> 'beforeunload'. */
    @BrowserFeature(@WebBrowser(CHROME))
    EVENT_BEFOREUNLOAD_AUTO_TYPE,

    /** Event false result. */
    @BrowserFeature(@WebBrowser(IE))
    EVENT_FALSE_RESULT,

    /** Triggers the onfocus onfocusin blur onfocusout events in this order. */
    @BrowserFeature(@WebBrowser(CHROME))
    EVENT_FOCUS_FOCUS_IN_BLUR_OUT,

    /** Triggers the onfocusin onfocus onfocusout blur events in this order. */
    @BrowserFeature(@WebBrowser(IE))
    EVENT_FOCUS_IN_FOCUS_OUT_BLUR,
    /** Triggers "onchange" event handler after "onclick" event handler. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    EVENT_ONCHANGE_AFTER_ONCLICK,

    /** Triggers "onclick" event handler for the select only, not for the clicked option. */
    @BrowserFeature(@WebBrowser(IE))
    EVENT_ONCLICK_FOR_SELECT_ONLY,

    /** Triggers 'onclick' and 'ondblclick' event handler using <code>PointerEvent</code>. */
    @BrowserFeature(@WebBrowser(IE))
    EVENT_ONCLICK_USES_POINTEREVENT,

    /** <code>CloseEvent</code> has type '' when created from document.createEvent('CloseEvent'). */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(IE) })
    EVENT_ONCLOSE_DEFAULT_TYPE_EMPTY,

    /** <code>CloseEvent</code> can not be created by calling document.createEvent('CloseEvent'). */
    @BrowserFeature(@WebBrowser(value = FF, minVersion = 45))
    EVENT_ONCLOSE_DOCUMENT_CREATE_NOT_SUPPORTED,

    /** <code>CloseEvent</code> initCloseEvent is available but throws an exception when called. */
    @BrowserFeature(@WebBrowser(value = FF, minVersion = 45))
    EVENT_ONCLOSE_INIT_CLOSE_EVENT_THROWS,

    /** <code>Event.bubbles</code> and <code>Event.cancelable</code> are false in 'onhashchange' event handler. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(IE) })
    EVENT_ONHASHCHANGE_BUBBLES_FALSE,

    /** <code>Event.cancelable</code> is false in 'onload' event handler. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF),
        @WebBrowser(IE) })
    EVENT_ONLOAD_CANCELABLE_FALSE,

    /** Triggers "onload" event if internal javascript loaded. */
    @BrowserFeature(@WebBrowser(IE))
    EVENT_ONLOAD_INTERNAL_JAVASCRIPT,

    /** MessageEvent default data value is null. */
    @BrowserFeature(@WebBrowser(CHROME))
    EVENT_ONMESSAGE_DEFAULT_DATA_NULL,

    /** Does not trigger "onmousedown" event handler for the select options. */
    @BrowserFeature({ @WebBrowser(IE) })
    EVENT_ONMOUSEDOWN_FOR_SELECT_OPTION_TRIGGERS_ADDITIONAL_DOWN_FOR_SELECT,

    /** Does not trigger "onmousedown" event handler for the select options. */
    @BrowserFeature(@WebBrowser(IE))
    EVENT_ONMOUSEDOWN_NOT_FOR_SELECT_OPTION,

    /** FF triggers an mouseover event even if the option is disabled. */
    @BrowserFeature(@WebBrowser(FF))
    EVENT_ONMOUSEOVER_FOR_DISABLED_OPTION,

    /** IE never triggers an mouseover event for select options. */
    @BrowserFeature(@WebBrowser(IE))
    EVENT_ONMOUSEOVER_NEVER_FOR_SELECT_OPTION,

    /** Does not trigger "onmousedown" event handler for the select options. */
    @BrowserFeature(@WebBrowser(IE))
    EVENT_ONMOUSEUP_FOR_SELECT_OPTION_TRIGGERS_ADDITIONAL_UP_FOR_SELECT,

    /** Does not trigger "onmouseup" event handler for the select options. */
    @BrowserFeature(@WebBrowser(IE))
    EVENT_ONMOUSEUP_NOT_FOR_SELECT_OPTION,

    /** Supports event type 'BeforeUnloadEvent'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    EVENT_TYPE_BEFOREUNLOADEVENT,

    /** Supports event type 'HashChangeEvent'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    EVENT_TYPE_HASHCHANGEEVENT,

    /** Supports vendor specific event type 'KeyEvents'. */
    @BrowserFeature(@WebBrowser(FF))
    EVENT_TYPE_KEY_EVENTS,

    /** Supports event type 'PointerEvent'. */
    @BrowserFeature(@WebBrowser(IE))
    EVENT_TYPE_POINTEREVENT,

    /** Supports event type 'ProgressEvent'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(IE) })
    EVENT_TYPE_PROGRESSEVENT,

    /** Supports event type 'XMLHttpRequestProgressEvent'. */
    @BrowserFeature(@WebBrowser(CHROME))
    EVENT_TYPE_XMLHTTPREQUESTPROGRESSEVENT,

    /** For new pages the focus points to the body node. */
    @BrowserFeature(@WebBrowser(IE))
    FOCUS_BODY_ELEMENT_AT_START,

    /** Indicates if a form field is directly reachable by its new name once this has been changed. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    FORMFIELD_REACHABLE_BY_NEW_NAMES,

    /** Indicates if a form field is directly reachable by its original name once this has been changed. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    FORMFIELD_REACHABLE_BY_ORIGINAL_NAME,

    /** Form elements are able to refer to the for by using the from attribute. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    FORM_FORM_ATTRIBUTE_SUPPORTED,

    /** Form submit forces an real request also if only the hash was changed. */
    @BrowserFeature(@WebBrowser(CHROME))
    FORM_SUBMISSION_DOWNLOWDS_ALSO_IF_ONLY_HASH_CHANGED,

    /** Form submit is done without the hash part of the action url. */
    @BrowserFeature(@WebBrowser(IE))
    FORM_SUBMISSION_URL_WITHOUT_HASH,

    /** */
    @BrowserFeature(@WebBrowser(IE))
    HEADER_CONTENT_DISPOSITION_ABSOLUTE_PATH,

    /** */
    @BrowserFeature(@WebBrowser(IE))
    HTMLABBREVIATED,

    /** HtmlAllCollection.item does not check the name, only the id. */
    @BrowserFeature(@WebBrowser(CHROME))
    HTMLALLCOLLECTION_DO_NOT_CHECK_NAME,

    /** HtmlAllCollection.item returns null instead of undefined if an element was not found. */
    @BrowserFeature({ @WebBrowser(IE), @WebBrowser(FF) })
    HTMLALLCOLLECTION_DO_NOT_CONVERT_STRINGS_TO_NUMBER,

    /** HtmlAllCollection.item(int) is not supported. */
    @BrowserFeature(@WebBrowser(FF))
    HTMLALLCOLLECTION_DO_NOT_SUPPORT_PARANTHESES,

    /** HtmlAllCollection.item(int) requires int parameter. */
    @BrowserFeature(@WebBrowser(CHROME))
    HTMLALLCOLLECTION_INTEGER_INDEX,

    /** HtmlCollection returns the first hit instead of a collection if many elements found. */
    @BrowserFeature(@WebBrowser(IE))
    HTMLALLCOLLECTION_NO_COLLECTION_FOR_MANY_HITS,

    /** HtmlAllCollection.item returns null instead of undefined if an element was not found. */
    @BrowserFeature({ @WebBrowser(IE), @WebBrowser(FF) })
    HTMLALLCOLLECTION_NULL_IF_ITEM_NOT_FOUND,

    /** HtmlAllCollection.namedItem returns null instead of undefined if an element was not found. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTMLALLCOLLECTION_NULL_IF_NAMED_ITEM_NOT_FOUND,

    /** Should {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBaseFontElement#isEndTagForbidden}. */
    @BrowserFeature(@WebBrowser(FF))
    HTMLBASEFONT_END_TAG_FORBIDDEN,

    /** Base tag href attribute is empty if not defined. */
    @BrowserFeature(@WebBrowser(IE))
    HTMLBASE_HREF_DEFAULT_EMPTY,
    /** HtmlCollection.item() supports also doubles as index. */
    @BrowserFeature({ @WebBrowser(IE), @WebBrowser(FF) })
    HTMLCOLLECTION_ITEM_FUNCT_SUPPORTS_DOUBLE_INDEX_ALSO,

    /** HtmlCollection.item[] supports also doubles as index. */
    @BrowserFeature(@WebBrowser(IE))
    HTMLCOLLECTION_ITEM_SUPPORTS_DOUBLE_INDEX_ALSO,

    /** HtmlCollection.item searches by id also. */
    @BrowserFeature(@WebBrowser(IE))
    HTMLCOLLECTION_ITEM_SUPPORTS_ID_SEARCH_ALSO,

    /** HtmlCollection.namedItem searches by id first. */
    @BrowserFeature(@WebBrowser(CHROME))
    HTMLCOLLECTION_NAMED_ITEM_ID_FIRST,

    /** HtmlCollection.item returns null instead of undefined if an element was not found. */
    @BrowserFeature(@WebBrowser(IE))
    HTMLCOLLECTION_NULL_IF_ITEM_NOT_FOUND,

    /** HtmlCollection returns null instead of undefined if an element was not found. */
    @BrowserFeature(@WebBrowser(FF))
    HTMLCOLLECTION_NULL_IF_NOT_FOUND,

    /** HtmlAllCollection(int) is not supported. */
    @BrowserFeature(@WebBrowser(IE))
    HTMLCOLLECTION_SUPPORTS_PARANTHESES,

    /** Is the default display style {@code inline} for quirks mode. */
    @BrowserFeature(@WebBrowser(FF))
    HTMLDEFINITION_INLINE_IN_QUIRKS,

    /** Is {@code document.charset} lower-case. */
    @BrowserFeature(@WebBrowser(IE))
    HTMLDOCUMENT_CHARSET_LOWERCASE,

    /** Do a normalization of the charset names. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTMLDOCUMENT_CHARSET_NORMALIZED,

    /** Do document.bgColor/.alinkColor/.vlinkColor/.linkColor have value by default. */
    @BrowserFeature(@WebBrowser(IE))
    HTMLDOCUMENT_COLOR,

    /** We can used function in detached documents. */
    @BrowserFeature(@WebBrowser(IE))
    HTMLDOCUMENT_FUNCTION_DETACHED,

    /** Calls to <code>document.XYZ</code> also looks at frames. */
    @BrowserFeature({ @WebBrowser(IE), @WebBrowser(CHROME) })
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
    @BrowserFeature(@WebBrowser(IE))
    HTMLDOCUMENT_GET_PREFERS_STANDARD_FUNCTIONS,

    /** Allows invalid 'align' values. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTMLELEMENT_ALIGN_INVALID,

    /** Removing the active element from the dom tree triggers the onblur event. */
    @BrowserFeature(@WebBrowser(CHROME))
    HTMLELEMENT_REMOVE_ACTIVE_TRIGGERS_BLUR_EVENT,

    /** Handle blank source like empty. */
    @BrowserFeature({ @WebBrowser(IE), @WebBrowser(CHROME) })
    HTMLIMAGE_BLANK_SRC_AS_EMPTY,

    /** Is document.cretaeElement('image') an HTMLElement. */
    @BrowserFeature(@WebBrowser(FF))
    HTMLIMAGE_HTMLELEMENT,

    /** Is document.cretaeElement('image') an HTMLUnknownElement. */
    @BrowserFeature(@WebBrowser(CHROME))
    HTMLIMAGE_HTMLUNKNOWNELEMENT,

    /** Mark the image as invisible if no src attribute defined. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTMLIMAGE_INVISIBLE_NO_SRC,

    /** Clicking an image input submits the value as param if defined. */
    @BrowserFeature(@WebBrowser(CHROME))
    HTMLIMAGE_NAME_VALUE_PARAMS,

    /** When clicking a {@code checkbox} or {@code radio} input the surrounding anchor is not clicked. */
    @BrowserFeature(@WebBrowser(CHROME))
    HTMLINPUT_CHECKBOX_DOES_NOT_CLICK_SURROUNDING_ANCHOR,

    /** When clicking a input the surrounding anchor is not clicked. */
    @BrowserFeature(@WebBrowser(IE))
    HTMLINPUT_DOES_NOT_CLICK_SURROUNDING_ANCHOR,

    /** HTMLInputElement: {@code files} to be {@code undefined}. */
    @BrowserFeature(@WebBrowser(IE))
    HTMLINPUT_FILES_UNDEFINED,

    /** Should the HTMLElement of {@link com.gargoylesoftware.htmlunit.html.HtmlKeygen} have no end tag. */
    @BrowserFeature(@WebBrowser(IE))
    HTMLKEYGEN_END_TAG_FORBIDDEN,

    /** */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(IE) })
    HTMLOPTION_EMPTY_TEXT_IS_NO_CHILDREN,

    /** */
    @BrowserFeature(@WebBrowser(IE))
    HTMLOPTION_PREVENT_DISABLED,

    /** Removing the selected attribute, de selects the option. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTMLOPTION_REMOVE_SELECTED_ATTRIB_DESELECTS,

    /** Trims the value of the type attribute before to verify it. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTMLSCRIPT_TRIM_TYPE,

    /** Setting defaultValue updates the value also. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTMLTEXTAREA_SET_DEFAULT_VALUE_UPDATES_VALUE,

    /** When calculation the value of an text area ie uses a recursive approach. */
    @BrowserFeature(@WebBrowser(IE))
    HTMLTEXTAREA_USE_ALL_TEXT_CHILDREN,

    /** Should {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTrackElement#isEndTagForbidden}. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(IE) })
    HTMLTRACK_END_TAG_FORBIDDEN,

    /** HTML attributes are always lower case. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTML_ATTRIBUTE_LOWER_CASE,

    /** Expand #0 to #000000. */
    @BrowserFeature(@WebBrowser(IE))
    HTML_COLOR_EXPAND_ZERO,

    /** Do not allow anything invalid in color, but restrict to valid values (names and hex digits) only. */
    @BrowserFeature({ @WebBrowser(IE) })
    HTML_COLOR_RESTRICT,

    /** Convert the color (name and hex code) to lower case. */
    @BrowserFeature(@WebBrowser(IE))
    HTML_COLOR_TO_LOWER,

    /** Supports &lt;object&gt; classid attribute. */
    @BrowserFeature(@WebBrowser(IE))
    HTML_OBJECT_CLASSID,

    /** Additionally support dates in format "d/M/yyyy". */
    @BrowserFeature(@WebBrowser(FF))
    HTTP_COOKIE_EXTENDED_DATE_PATTERNS_1,

    /** Dates format pattern 2. */
    @BrowserFeature(@WebBrowser(CHROME))
    HTTP_COOKIE_EXTENDED_DATE_PATTERNS_2,

    /** Indicates that the pas is extracted from the location.
     * Sample: from the location /foo/boo only /foo is used.
     */
    @BrowserFeature(@WebBrowser(IE))
    HTTP_COOKIE_EXTRACT_PATH_FROM_LOCATION,

    /** Ignore empty cookie. */
    @BrowserFeature(@WebBrowser(CHROME))
    HTTP_COOKIE_IGNORE_EMPTY,

    /** Indicates that the start date for two digits cookies is 1970
     * instead of 2000 (Two digits years are interpreted as 20xx
     * if before 1970 and as 19xx otherwise).
     */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTTP_COOKIE_START_DATE_1970,

    /** Supports redirect via 308 code. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    HTTP_REDIRECT_308,

    /** Setting the property align to arbitrary values is allowed. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_ALIGN_ACCEPTS_ARBITRARY_VALUES,

    /** Setting the property align of an input element ignores the value
     * if the value is one of center, justify, left or right.
     * For all other values an exception is still thrown.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_ALIGN_FOR_INPUT_IGNORES_VALUES,

    /**
     * Javascript property anchors includes all anchors with a name or an id property.
     * If not set name property is required.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_ANCHORS_REQUIRES_NAME_OR_ID,

    /** The anchor pathname detects url's starting with one letter as file url's. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(IE) })
    JS_ANCHOR_PATHNAME_DETECT_WIN_DRIVES_URL,

    /** The anchor pathname property returns nothing for broken http(s) url's. */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_ANCHOR_PATHNAME_NONE_FOR_BROKEN_URL,

    /** The anchor pathname property returns nothing for none http(s) url's. */
    @BrowserFeature(@WebBrowser(FF))
    JS_ANCHOR_PATHNAME_NONE_FOR_NONE_HTTP_URL,

    /** The anchor pathname prefixes file url's with '/'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(IE) })
    JS_ANCHOR_PATHNAME_PREFIX_WIN_DRIVES_URL,

    /** The anchor protocol property returns ':' for broken http(s) url's. */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_ANCHOR_PROTOCOL_COLON_FOR_BROKEN_URL,

    /** The anchor protocol property converts drive letters to uppercase. */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_ANCHOR_PROTOCOL_COLON_UPPER_CASE_DRIVE_LETTERS,

    /** Indicates that "someFunction.arguments" is a read-only view of the function's argument. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_ARGUMENTS_READ_ONLY_ACCESSED_FROM_FUNCTION,

    /** firstChild and lastChild returns null for Attr (like IE does). */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_ATTR_FIRST_LAST_CHILD_RETURNS_NULL,

    /** HTMLBGSoundElement reported as HTMLUnknownElement. */
    @BrowserFeature(@WebBrowser(FF))
    JS_BGSOUND_AS_UNKNOWN,

    /** Body {@code margin} is 8px. */
    @BrowserFeature(@WebBrowser(IE))
    JS_BODY_MARGINS_8,

    /** HtmlElement.getBoundingClientRect throws an error if the element is not attached to the page. */
    @BrowserFeature(@WebBrowser(IE))
    JS_BOUNDINGCLIENTRECT_THROWS_IF_DISCONNECTED,

    /** If we're emulating IE, the overall JavaScript return value is the last return value. */
    @BrowserFeature(@WebBrowser(IE))
    JS_CALL_RESULT_IS_LAST_RETURN_VALUE,

    /** toDataURL for canvas returns the CHROME version of the png. */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_CANVAS_DATA_URL_CHROME_PNG,

    /** toDataURL for canvas returns the IE11 version of the png. */
    @BrowserFeature(@WebBrowser(IE))
    JS_CANVAS_DATA_URL_IE_PNG,

    /** draw for canvas throws an error if no image available. */
    @BrowserFeature(@WebBrowser(FF))
    JS_CANVAS_DRAW_THROWS_FOR_MISSING_IMG,

    /** Do not allow invalid clear values. */
    @BrowserFeature(@WebBrowser(IE))
    JS_CLEAR_RESTRICT,

    /** ClientRectList toString reports the first item. */
    @BrowserFeature(@WebBrowser(FF))
    JS_CLIENTRECTLIST_DEFAUL_VALUE_FROM_FIRST,

    /** ClientRectList.item throws instead of returning null if an element was not found. */
    @BrowserFeature(@WebBrowser(IE))
    JS_CLIENTRECTLIST_THROWS_IF_ITEM_NOT_FOUND,

    /** Supports {@code CSSCharsetRule}. */
    @BrowserFeature(@WebBrowser(value = FF, maxVersion = 38))
    JS_CSSRULELIST_CHARSET_RULE,

    /** item is enumerated before length property of CSSRuleList. */
    @BrowserFeature(@WebBrowser(FF))
    JS_CSSRULELIST_ENUM_ITEM_LENGTH,

    /** Uses {@code MozCSSKeyframesRule}. */
    @BrowserFeature(@WebBrowser(FF))
    JS_CSS_MOZ_CSS_KEYFRAMES_RULE,

    /** {@link DateTimeFormat} uses the Ascii digits for {@code ar-DZ} locale. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(IE) })
    JS_DATE_AR_DZ_ASCII_DIGITS,

    /** <code>Date.toLocaleDateString()</code> returns a short form (d.M.yyyy). */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DATE_LOCALE_DATE_SHORT,

    /** {@link DateTimeFormat} uses the Unicode Character {@code 'LEFT-TO-RIGHT MARK'}. */
    @BrowserFeature(@WebBrowser(IE))
    JS_DATE_WITH_LEFT_TO_RIGHT_MARK,

    /** */
    @BrowserFeature(@WebBrowser(IE))
    JS_DEFERRED,

    /** Javascript doctyp.entities returns null (FF10). */
    @BrowserFeature(@WebBrowser(IE))
    JS_DOCTYPE_ENTITIES_NULL,

    /** Javascript doctyp.notations returns null (FF10). */
    @BrowserFeature(@WebBrowser(IE))
    JS_DOCTYPE_NOTATIONS_NULL,

    /** Indicates that document.createAttribute converts the local name to lowercase. */
    @BrowserFeature(@WebBrowser(value = FF, minVersion = 45))
    JS_DOCUMENT_CREATE_ATTRUBUTE_LOWER_CASE,

    /** Javascript function document.createElement accepts only tag names. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME),
        @WebBrowser(IE) })
    JS_DOCUMENT_CREATE_ELEMENT_STRICT,

    /** The browser supports the design mode 'Inherit'. */
    @BrowserFeature(@WebBrowser(IE))
    JS_DOCUMENT_DESIGN_MODE_INHERIT,

    /** Javascript document.forms(...) supported. */
    @BrowserFeature(@WebBrowser(IE))
    JS_DOCUMENT_FORMS_FUNCTION_SUPPORTED,

    /** The browser has selection {@code rangeCount}. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(IE) })
    JS_DOCUMENT_SELECTION_RANGE_COUNT,

    /** Javascript property document.domain doesn't allow to set domain of {@code about:blank}. */
    @BrowserFeature(@WebBrowser(IE))
    JS_DOCUMENT_SETTING_DOMAIN_THROWS_FOR_ABOUT_BLANK,

    /** If setting the document.location inside onclick() of anchor element should be triggered. */
    @BrowserFeature({ @WebBrowser(IE), @WebBrowser(FF) })
    JS_DOCUMENT_SET_LOCATION_EXECUTED_IN_ANCHOR,

    /** createHTMLDucument requires a title. */
    @BrowserFeature(@WebBrowser(IE))
    JS_DOMIMPLEMENTATION_CREATE_HTMLDOCOMENT_REQUIRES_TITLE,

    /** If document.implementation.hasFeature() supports 'Core 1.0'. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_DOMIMPLEMENTATION_FEATURE_CORE_3,

    /** If document.implementation.hasFeature() supports 'CSS2 1.0'. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_DOMIMPLEMENTATION_FEATURE_CSS2_1,

    /** If document.implementation.hasFeature() supports 'CSS2 2.0'. */
    @BrowserFeature
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
    @BrowserFeature
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
    @BrowserFeature
    JS_DOMIMPLEMENTATION_FEATURE_MOUSEEVENTS_2,

    /** If document.implementation.hasFeature() supports 'MutationEvents 1.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_MUTATIONEVENTS_1,

    /** If document.implementation.hasFeature() supports 'MutationEvents 2.0'. */
    @BrowserFeature
    JS_DOMIMPLEMENTATION_FEATURE_MUTATIONEVENTS_2,

    /** If document.implementation.hasFeature() supports 'MutationNameEvents'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_MUTATIONNAMEEVENTS,

    /** If document.implementation.hasFeature() supports 'Range 1.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_RANGE_1,

    /** If document.implementation.hasFeature() supports 'Range 2.0'. */
    @BrowserFeature
    JS_DOMIMPLEMENTATION_FEATURE_RANGE_2,

    /** If document.implementation.hasFeature() supports 'Range 3.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_RANGE_3,

    /** If document.implementation.hasFeature() supports 'StyleSheets 2.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_STYLESHEETS,

    /** If document.implementation.hasFeature() supports 'http://www.w3.org/TR/SVG11/feature#BasicStructure 1.0'. */
    @BrowserFeature
    JS_DOMIMPLEMENTATION_FEATURE_SVG_BASICSTRUCTURE_1_0,

    /** If document.implementation.hasFeature() supports 'http://www.w3.org/TR/SVG11/feature#BasicStructure 1.2'. */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_DOMIMPLEMENTATION_FEATURE_SVG_BASICSTRUCTURE_1_2,

    /** If document.implementation.hasFeature() supports 'http://www.w3.org/TR/SVG11/feature#Shape 1.0'. */
    @BrowserFeature
    JS_DOMIMPLEMENTATION_FEATURE_SVG_SHAPE_1_0,

    /** If document.implementation.hasFeature() supports 'http://www.w3.org/TR/SVG11/feature#Shape 1.2'. */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_DOMIMPLEMENTATION_FEATURE_SVG_SHAPE_1_2,

    /** If document.implementation.hasFeature() supports 'MutationNameEvents'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_TEXTEVENTS,

    /** If document.implementation.hasFeature() supports 'Traversal 1.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_TRAVERSAL_1,

    /** If document.implementation.hasFeature() supports 'Traversal 2.0'. */
    @BrowserFeature
    JS_DOMIMPLEMENTATION_FEATURE_TRAVERSAL_2,

    /** If document.implementation.hasFeature() supports 'Traversal 3.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_TRAVERSAL_3,

    /** If document.implementation.hasFeature() supports 'UIEvents 2.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_UIEVENTS_2,

    /** If document.implementation.hasFeature() supports 'UIEvents 3.0'. */
    @BrowserFeature
    JS_DOMIMPLEMENTATION_FEATURE_UIEVENTS_3,

    /** If document.implementation.hasFeature() supports 'Validation'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_VALIDATION,

    /** If document.implementation.hasFeature() supports 'Views 1.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_VIEWS_1,

    /** If document.implementation.hasFeature() supports 'Views 2.0'. */
    @BrowserFeature
    JS_DOMIMPLEMENTATION_FEATURE_VIEWS_2,

    /** If document.implementation.hasFeature() supports 'Views 3.0'. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMIMPLEMENTATION_FEATURE_VIEWS_3,

    /** If document.implementation.hasFeature() supports 'XHTML 1.0'. */
    @BrowserFeature
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
    @BrowserFeature(@WebBrowser(IE))
    JS_DOMPARSER_EXCEPTION_ON_ERROR,

    /** {@code DOMParser.parseFromString(..)} creates a document containing a {@code parsererror} element. */
    @BrowserFeature({@WebBrowser(CHROME), @WebBrowser(FF) })
    JS_DOMPARSER_PARSERERROR_ON_ERROR,

    /** DOMTokenList uses a enhanced set of whitespace chars. */
    @BrowserFeature(@WebBrowser(IE))
    JS_DOMTOKENLIST_ENHANCED_WHITESPACE_CHARS,

    /** DOMTokenList removed all whitespace chars during edit. */
    @BrowserFeature(@WebBrowser(IE))
    JS_DOMTOKENLIST_REMOVE_WHITESPACE_CHARS_ON_EDIT,

    /** DOMTokenList removed all whitespace chars during remove. */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_DOMTOKENLIST_REMOVE_WHITESPACE_CHARS_ON_REMOVE,

    /** Javascript property function {@code delete} throws an exception if the given count is negative. */
    @BrowserFeature(@WebBrowser(IE))
    JS_DOM_CDATA_DELETE_THROWS_NEGATIVE_COUNT,

    /** Element.baseURI is null for XML element. */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_ELEMENT_BASE_URL_NULL,

    /** Indicates that attributeNS returns an empty string instead of null if not found. */
    @BrowserFeature(@WebBrowser(IE))
    JS_ELEMENT_GET_ATTRIBUTE_RETURNS_EMPTY,

    /** The Enumerator constructor throws an exception if called with HtmlCollections as parameter. */
    @BrowserFeature(@WebBrowser(IE))
    JS_ENUMERATOR_CONSTRUCTOR_THROWS,

    /** Indicates that for(x in y) should enumerate the numbers first. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME),
        @WebBrowser(IE) })
    JS_ENUM_NUMBERS_FIRST,

    /** Javascript {@code Error.stackTraceLimit}. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(IE) })
    JS_ERROR_STACK_TRACE_LIMIT,

    /** Javascript event.keyCode and event.charCode distinguish between printable and not printable keys. */
    @BrowserFeature(@WebBrowser(FF))
    JS_EVENT_DISTINGUISH_PRINTABLE_KEY,

    /** Do not send parameter in event handlers. */
    @BrowserFeature(@WebBrowser(IE))
    JS_EVENT_NO_PARAMETER,

    /** Executes the window event listeners if the node is detached from the document. */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_EVENT_WINDOW_EXECUTE_IF_DITACHED,

    /** FF uses a different date format for file.lastModifiedDate. */
    @BrowserFeature(@WebBrowser(FF))
    JS_FILE_SHORT_DATE_FORMAT,

    /** Indicates that the action property will not be expanded if defined as empty string. */
    @BrowserFeature(@WebBrowser(FF))
    JS_FORM_ACTION_EXPANDURL_IGNORE_EMPTY,

    /** form.dispatchEvent(e) submits the form if the event is of type 'submit'. */
    @BrowserFeature(@WebBrowser(FF))
    JS_FORM_DISPATCHEVENT_SUBMITS,

    /** Setting form.encoding only allowed for valid encodings. */
    @BrowserFeature(@WebBrowser(IE))
    JS_FORM_REJECT_INVALID_ENCODING,

    /** Calling form.submit() twice forces double download. */
    @BrowserFeature(@WebBrowser(IE))
    JS_FORM_SUBMIT_FORCES_DOWNLOAD,

    /** Support for document.formName('inputName'). */
    @BrowserFeature(@WebBrowser(IE))
    JS_FORM_USABLE_AS_FUNCTION,

    /** Indicates that function is defined even before its declaration, inside a block. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(IE) })
    JS_FUNCTION_DECLARED_FORWARD_IN_BLOCK,

    /** Indicates if the method toSource exists on the native objects. */
    @BrowserFeature(@WebBrowser(FF))
    JS_FUNCTION_TOSOURCE,

    /** HTMLElement instead of HTMLUnknownElement for elements with hyphen ('-'). */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_HTML_HYPHEN_ELEMENT_CLASS_NAME,

    /** HTMLElement instead of HTMLUnknownElement for ruby elements. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_HTML_RUBY_ELEMENT_CLASS_NAME,

    /** Executes the {@code onload} handler, regardless of the whether the element was already attached to the page. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(IE) })
    JS_IFRAME_ALWAYS_EXECUTE_ONLOAD,

    /** Ignore the last line containing uncommented. */
    @BrowserFeature(@WebBrowser(IE))
    JS_IGNORES_LAST_LINE_CONTAINING_UNCOMMENTED,

    /**
     * The complete property returns also true, if the image download was failing
     * or if there was no src at all.
     */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_IMAGE_COMPLETE_RETURNS_TRUE_FOR_NO_REQUEST,

    /**
     * Is class name of {@link com.gargoylesoftware.htmlunit.javascript.host.html.Image} the same as
     * {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLImageElement}.
     */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_IMAGE_HTML_IMAGE_ELEMENT,

    /**
     * Is the prototype of {@link com.gargoylesoftware.htmlunit.javascript.host.html.Image} the same as
     * {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLImageElement}.
     */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(IE) })
    JS_IMAGE_PROTOTYPE_SAME_AS_HTML_IMAGE,

    /**
     * Getting the width and height of an image tag without a source returns 18x20;
     * for invalid values returns 1.
     */
    @BrowserFeature(@WebBrowser(FF))
    JS_IMAGE_WIDTH_HEIGHT_RETURNS_0x0_0x0,

    /**
     * Getting the width and height of an image tag without a source returns 18x20;
     * for invalid values returns 1.
     */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_IMAGE_WIDTH_HEIGHT_RETURNS_18x20_0x0,

    /**
     * Getting the width and height of an image tag without a source returns 28x30;
     * for invalid values returns same.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_IMAGE_WIDTH_HEIGHT_RETURNS_28x30_28x30,

    /** Indicates that innerHTML adds the child also for null values. */
    @BrowserFeature(@WebBrowser(IE))
    JS_INNER_HTML_ADD_CHILD_FOR_NULL_VALUE,

    /** Indicates that innerHTML uses {@code crnl} instead of {@code nl}. */
    @BrowserFeature(@WebBrowser(IE))
    JS_INNER_TEXT_CR_NL,

    /** Indicates that innerText is readonly for tables. */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_INNER_TEXT_READONLY_FOR_TABLE,

    /** Indicates that innerText setter supports null values. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(value = FF, maxVersion = 38) })
    JS_INNER_TEXT_VALUE_NULL,

    /** Setting the type property of an input converts the type to lowercase. */
    @BrowserFeature(@WebBrowser(IE))
    JS_INPUT_SET_TYPE_LOWERCASE,

    /** Setting the value of an Input URL to blank will result in an empty value. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_INPUT_SET_VALUE_EMAIL_TRIMMED,

    /** Setting the value of an Input Text/Password/TextArea resets the selection. */
    @BrowserFeature(@WebBrowser(IE))
    JS_INPUT_SET_VALUE_MOVE_SELECTION_TO_START,

    /** Setting the value of an Input URL to blank will result in an empty value. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_INPUT_SET_VALUE_URL_TRIMMED,

    /** Indicates that Intl.v8BreakIterator is supported. */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_INTL_V8_BREAK_ITERATOR,

    /** Supports Iterator and StopIteration. */
    @BrowserFeature(@WebBrowser(FF))
    JS_Iterator,

    /** location.hash returns an encoded hash. */
    @BrowserFeature(@WebBrowser(value = FF, minVersion = 45))
    JS_LOCATION_HASH_HASH_IS_ENCODED,

    /**
     * Set this property if the browser evaluates<br>
     * window.location.hash to #&uuml; (like Firefox)<br>
     * for url 'http://localhost/something/#%C3%BC'.<br>
     * IE evaluates to #%C3%BC.
     */
    @BrowserFeature(@WebBrowser(FF))
    JS_LOCATION_HASH_IS_DECODED,

    /**
     * Property location.hash returns '#' for urls ending with a hash
     * sign (e.g. http://localhost/something/#).
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_LOCATION_HASH_RETURNS_HASH_FOR_EMPTY_DEFINED,

    /**
     * Set this property if the browser evaluates<br>
     * window.location.hash to #%C3%BC; (like Firefox)<br>
     * for url 'http://localhost/something/#&uuml;'.<br>
     * IE evaluates to #&uuml;.
     */
    @BrowserFeature(@WebBrowser(FF))
    JS_LOCATION_HREF_HASH_IS_ENCODED,

    /** Map supports the argument constructor. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_MAP_CONSTRUCTOR_ARGUMENT,

    /** Indicates that an empty media list is represented by the string 'all'. */
    @BrowserFeature(@WebBrowser(IE))
    JS_MEDIA_LIST_ALL,

    /** Indicates that an empty media list is represented by the string 'all'. */
    @BrowserFeature(@WebBrowser(FF))
    JS_MEDIA_LIST_EMPTY_STRING,

    /** Type property of menu has always '' as value. */
    @BrowserFeature(@WebBrowser(IE))
    JS_MENU_TYPE_EMPTY,

    /** Indicates if the String representation of a native function is without newline. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(EDGE) })
    JS_NATIVE_FUNCTION_TOSTRING_COMPACT,

    /** Indicates if the String representation of a native function begins and ends with a {@code \n}.*/
    @BrowserFeature(@WebBrowser(IE))
    JS_NATIVE_FUNCTION_TOSTRING_NEW_LINE,

    /** <code>Node.contains</code> returns false instead of throwing an exception. */
    @BrowserFeature(@WebBrowser(IE))
    JS_NODE_CONTAINS_RETURNS_FALSE_FOR_INVALID_ARG,

    /** The reference argument of <code>Node.insertBefore(..)</code> is optional. */
    @BrowserFeature(@WebBrowser(IE))
    JS_NODE_INSERT_BEFORE_REF_OPTIONAL,

    /** Children are enumerated. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(IE) })
    JS_NODE_LIST_ENUMERATE_CHILDREN,

    /** Functions are enumerated. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_NODE_LIST_ENUMERATE_FUNCTIONS,

    /** Indicates that someObj.offsetParent returns null, it someObj has fixed style. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(IE) })
    JS_OFFSET_PARENT_NULL_IF_FIXED,

    /**
     * Is class name of {@link com.gargoylesoftware.htmlunit.javascript.host.html.Option} the same as
     * {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOptionElement}.
     */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_OPTION_HTML_OPTION_ELEMENT,

    /**
     * Is the prototype of {@link com.gargoylesoftware.htmlunit.javascript.host.html.Option} the same as
     * {@link com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOptionElement}.
     */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(IE) })
    JS_OPTION_PROTOTYPE_SAME_AS_HTML_OPTION,

    /** element.outerHTML handles null value as string "null". */
    @BrowserFeature(@WebBrowser(IE))
    JS_OUTER_HTML_NULL_AS_STRING,

    /** element.outerHTML removes all children from detached node. */
    @BrowserFeature(@WebBrowser(IE))
    JS_OUTER_HTML_REMOVES_CHILDS_FOR_DETACHED,

    /** element.outerHTML removes all children from detached node. */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_OUTER_HTML_THROWS_FOR_DETACHED,

    /** If {@code true}, then treat <tt>__parent__</tt> and <tt>__proto__</tt> as special properties. */
    @BrowserFeature(@WebBrowser(IE))
    JS_PARENT_PROTO_PROPERTIES,

    /** Indicates that parseInt() should have radix 10 by default. */
    @BrowserFeature
    JS_PARSE_INT_RADIX_10,

    /** Indicates that HTMLPhraseElements returning 'HTMLElement'
     * as class name. */
    @BrowserFeature(@WebBrowser(FF))
    JS_PHRASE_COMMON_CLASS_NAME,

    /** Indicates that the {@link PopStateEvent}.{@code state} is cloned. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(IE) })
    JS_POP_STATE_EVENT_CLONE_STATE,

    /** Indicates that the {@link PopStateEvent}.{@code type} has value. */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_POP_STATE_EVENT_TYPE,

    /** Indicates that the {@code pre.width} is string. */
    @BrowserFeature(@WebBrowser(IE))
    JS_PRE_WIDTH_STRING,

    /** Support {@code Reflect}. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(value = FF, minVersion = 45), @WebBrowser(EDGE) })
    JS_REFLECT,

    /** <code>RegExp.lastParen</code> returns an empty string if the RegExp has too many groups. */
    @BrowserFeature(@WebBrowser(IE))
    JS_REGEXP_EMPTY_LASTPAREN_IF_TOO_MANY_GROUPS,

    /** RegExp group <code>$0</code> returns the whole previous match (see {@link java.util.regex.Matcher#group()}. */
    @BrowserFeature(@WebBrowser(IE))
    JS_REGEXP_GROUP0_RETURNS_WHOLE_MATCH,

    /** Javascript script tags supports the 'for' and the 'event' attribute.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_SCRIPT_SUPPORTS_FOR_AND_EVENT_WINDOW,

    /** Javascript selectorText property returns selectors in lower case. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(IE) })
    JS_SELECTOR_TEXT_LOWERCASE,

    /** Indicates that setting the value to null has no effect. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_SELECT_FILE_THROWS,

    /** When expanding the collection by setting the length don't add
     * a empty text node. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(IE) })
    JS_SELECT_OPTIONS_DONT_ADD_EMPTY_TEXT_CHILD_WHEN_EXPANDING,

    /** Indicates that select.options.childNodes is a valid property. */
    @BrowserFeature(@WebBrowser(IE))
    JS_SELECT_OPTIONS_HAS_CHILDNODES_PROPERTY,

    /** Indicates that select.options has a wong class name. */
    @BrowserFeature(@WebBrowser(IE))
    JS_SELECT_OPTIONS_HAS_SELECT_CLASS_NAME,

    /** Ignore negative value when setting the length. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_SELECT_OPTIONS_IGNORE_NEGATIVE_LENGTH,

    /** Indicates that select.options returns null if requested index is outside. */
    @BrowserFeature(@WebBrowser(IE))
    JS_SELECT_OPTIONS_NULL_FOR_OUTSIDE,

    /** Indicates that select.options.remove ignores the call if index is too large. */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_SELECT_OPTIONS_REMOVE_IGNORE_IF_INDEX_NEGATIVE,

    /** Indicates that select.options.remove ignores the call if index is too large. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(IE) })
    JS_SELECT_OPTIONS_REMOVE_IGNORE_IF_INDEX_TOO_LARGE,

    /** Indicates that select.options[i] throws an exception if the requested index is neagtive. */
    @BrowserFeature(@WebBrowser(IE))
    JS_SELECT_OPTIONS_REMOVE_THROWS_IF_NEGATIV,

    /** Indicates that select.options.remove ignores the call if index is too large. */
    @BrowserFeature(@WebBrowser(FF))
    JS_SELECT_REMOVE_IGNORE_IF_INDEX_OUTSIDE,

    /** Indicates that select.value = 'val' only checks the value attribute and
     * not the option text. */
    @BrowserFeature(@WebBrowser(IE))
    JS_SELECT_SET_VALUES_CHECKS_ONLY_VALUE_ATTRIBUTE,

    /** Whether to get any property from the items first. */
    @BrowserFeature(@WebBrowser(IE))
    JS_STORAGE_GET_FROM_ITEMS,

    /** Whether to add to the storage even preserved words. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(IE) })
    JS_STORAGE_PRESERVED_INCLUDED,

    /** Stylesheet list contains only active style sheets. */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_STYLESHEETLIST_ACTIVE_ONLY,

    /** Indicates if style.setProperty ignores case when determining the priority. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(IE) })
    JS_STYLE_SET_PROPERTY_IMPORTANT_IGNORES_CASE,

    /** IE supports accessing unsupported style elements via getter
     * like val = elem.style.htmlunit;.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_STYLE_UNSUPPORTED_PROPERTY_GETTER,

    /** Indicates wordSpacing support percent values. */
    @BrowserFeature(@WebBrowser(value = FF, minVersion = 45))
    JS_STYLE_WORD_SPACING_ACCEPTS_PERCENT,

    /** Indicates that trying to access the style property with a wrong index returns undefined
     * instead of "". */
    @BrowserFeature(@WebBrowser(FF))
    JS_STYLE_WRONG_INDEX_RETURNS_UNDEFINED,

    /** The width cell height does not return negative values. */
    @BrowserFeature(@WebBrowser(IE))
    JS_TABLE_CELL_HEIGHT_DOES_NOT_RETURN_NEGATIVE_VALUES,

    /** The width cell offset calculation takes border into account. */
    @BrowserFeature(@WebBrowser(IE))
    JS_TABLE_CELL_OFFSET_INCLUDES_BORDER,

    /** The width cell property does not return negative values. */
    @BrowserFeature(@WebBrowser(IE))
    JS_TABLE_CELL_WIDTH_DOES_NOT_RETURN_NEGATIVE_VALUES,

    /** The width column property does not return negative values. */
    @BrowserFeature(@WebBrowser(IE))
    JS_TABLE_COLUMN_WIDTH_NO_NEGATIVE_VALUES,

    /** The width column property has a value of 'null' for null. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_TABLE_COLUMN_WIDTH_NULL_STRING,

    /** Calling deleteCell without an index throws an exception. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_TABLE_ROW_DELETE_CELL_REQUIRES_INDEX,

    /** Throws an exception if the value for column span is less than one. */
    @BrowserFeature(@WebBrowser(IE))
    JS_TABLE_SPAN_THROWS_EXCEPTION_IF_INVALID,

    /** Indicates that table elements supports the values "top", "bottom", "middle", "baseline". */
    @BrowserFeature(@WebBrowser(IE))
    JS_TABLE_VALIGN_SUPPORTS_IE_VALUES,

    /** Getting the property maxLength if it is not defined in the DOM returns MAX_INT.
     * FF and Chrome return -1.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_TEXT_AREA_GET_MAXLENGTH_MAX_INT,

    /** Setting the property cols throws an exception, if the provided value is less than 0.
     * FF ignores the provided value in this case.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_TEXT_AREA_SET_COLS_NEGATIVE_THROWS_EXCEPTION,

    /** Setting the property cols throws an exception, if the provided value is not convertible into an integer.
     * FF ignores the provided value in this case and sets cols to 0.
     */
    @BrowserFeature({ @WebBrowser(IE), @WebBrowser(FF) })
    JS_TEXT_AREA_SET_COLS_THROWS_EXCEPTION,

    /** Setting the property {@code maxLength} throws an exception, if the provided value is less than 0. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_TEXT_AREA_SET_MAXLENGTH_NEGATIVE_THROWS_EXCEPTION,

    /** Setting the property {@code rows} throws an exception, if the provided value is less than 0. */
    @BrowserFeature(@WebBrowser(IE))
    JS_TEXT_AREA_SET_ROWS_NEGATIVE_THROWS_EXCEPTION,

    /** Setting the property rows throws an exception, if the provided value is not convertible into an integer.
     * FF ignores the provided value in this case and sets rows to 0.
     */
    @BrowserFeature({ @WebBrowser(IE), @WebBrowser(FF) })
    JS_TEXT_AREA_SET_ROWS_THROWS_EXCEPTION,

    /** Setting the value processes null as null value. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_TEXT_AREA_SET_VALUE_NULL,

    /** Indicates that <code>TreeWalker.expandEntityReferences</code> is always {@code false}. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_TREEWALKER_EXPAND_ENTITY_REFERENCES_FALSE,

    /**
     * Indicates that the filter to be used by the TreeWalker has to be a function (so no object with a method
     * <code>acceptNode(..)</code> is supported).
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_TREEWALKER_FILTER_FUNCTION_ONLY,

    /** Setting the property align to arbitrary values is allowed. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_TYPE_ACCEPTS_ARBITRARY_VALUES,

    /** WeakMap supports the argument constructor. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_WEAKMAP_CONSTRUCTOR_ARGUMENT,

    /** Allow inheriting parent constants
     * in {@link com.gargoylesoftware.htmlunit.javascript.host.event.WebGLContextEvent}. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(FF) })
    JS_WEBGL_CONTEXT_EVENT_CONSTANTS,

    /** Setting the property width/height to arbitrary values is allowed. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_WIDTH_HEIGHT_ACCEPTS_ARBITRARY_VALUES,

    /**
     * The window.ActiveXObject is special in IE11
     * http://msdn.microsoft.com/en-us/library/ie/dn423948%28v=vs.85%29.aspx.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_WINDOW_ACTIVEXOBJECT_HIDDEN,

    /** Changing the opener of a window to something not null and not a window is not valid. */
    @BrowserFeature(@WebBrowser(IE))
    JS_WINDOW_CHANGE_OPENER_ONLY_WINDOW_OBJECT,

    /** <code>window.name</code> returns also form fields (e.g. input, textarea). */
    @BrowserFeature(@WebBrowser(IE))
    JS_WINDOW_FORMFIELDS_ACCESSIBLE_BY_NAME,

    /** Support for accessing the frame of a window by id additionally to using the name (FF). */
    @BrowserFeature(@WebBrowser(IE))
    JS_WINDOW_FRAMES_ACCESSIBLE_BY_ID,

    /** <code>window..frames['id']</code> returns the frame window instead of the frame element. */
    @BrowserFeature(@WebBrowser(IE))
    JS_WINDOW_FRAME_BY_ID_RETURNS_WINDOW,

    /**
     * Difference of window.outer/inner height is 63.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_63,

    /**
     * Difference of window.outer/inner height is 89.
     */
    @BrowserFeature(@WebBrowser(CHROME))
    JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_89,

    /**
     * Difference of window.outer/inner height is 94.
     */
    @BrowserFeature(@WebBrowser(FF))
    JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_94,

    /** Window.getSelection returns null, if the window is not visible. */
    @BrowserFeature(@WebBrowser(FF))
    JS_WINDOW_SELECTION_NULL_IF_INVISIBLE,

    /** Window.top property is writable. */
    @BrowserFeature(@WebBrowser(IE))
    JS_WINDOW_TOP_WRITABLE,

    /** Supports XML. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_XML,

    /** XMLDocument: .getElementsByTagName() to search the nodes by their local name. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(IE) })
    JS_XML_GET_ELEMENTS_BY_TAG_NAME_LOCAL,

    /** XMLDocument: .getElementById() to return any element, not HTML specifically. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_XML_GET_ELEMENT_BY_ID__ANY_ELEMENT,

    /** Indicates that new XMLSerializer().serializeToString(..) inserts a blank before self-closing a tag. */
    @BrowserFeature(@WebBrowser(IE))
    JS_XML_SERIALIZER_BLANK_BEFORE_SELF_CLOSING,

    /**
     * Indicates that new XMLSerializer().serializeToString(..) called with a document fragment created by an
     * HTMLPage always returns ''.
     */
    @BrowserFeature(@WebBrowser(IE))
    JS_XML_SERIALIZER_HTML_DOCUMENT_FRAGMENT_ALWAYS_EMPTY,

    /** Indicates that new XMLSerializer().serializeToString(..) respects the XHTML definition for non empty tags. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    JS_XML_SERIALIZER_NON_EMPTY_TAGS,

    /** Indicates that <code>XMLSerializer.serializeToString(..)</code> serializes a single CDataSection as escaped
     * text instead of <code>&lt;![CDATA[xxx]]&gt;</code>. */
    @BrowserFeature(@WebBrowser(IE))
    JS_XML_SERIALIZER_ROOT_CDATA_AS_ESCAPED_TEXT,

    /** Indicates that the browser uses the ActiveXObject for implementing XML support. */
    @BrowserFeature(@WebBrowser(IE))
    JS_XML_SUPPORT_VIA_ACTIVEXOBJECT,

    /** With special keys [in .type(int)], should we trigger onkeypress event or not. */
    @BrowserFeature(@WebBrowser(FF))
    KEYBOARD_EVENT_SPECIAL_KEYPRESS,

    /** Handle {@code <keygen>} as {@code <select>}. */
    @BrowserFeature(@WebBrowser(FF))
    KEYGEN_AS_SELECT,

    /**
     * Indicates that the browser considers the meta X-UA-Compatible when determining
     * compatibility/quirks mode.
     */
    @BrowserFeature(@WebBrowser(IE))
    META_X_UA_COMPATIBLE,

    /**
     * The default display style of multicol is 'block'.
     */
    @BrowserFeature(@WebBrowser(FF))
    MULTICOL_BLOCK,

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
    @BrowserFeature(@WebBrowser(IE))
    QUERYSELECTORALL_NOT_IN_QUIRKS,

    /** Indicates {@code .querySelectorAll()} supports {@code :target} condition. */
    @BrowserFeature(@WebBrowser(CHROME))
    QUERYSELECTORALL_NO_TARGET,

    /** IE11 throws a syntax error if a css3 pseudo selector is used on an detached node. */
    @BrowserFeature(@WebBrowser(IE))
    QUERYSELECTOR_CSS3_PSEUDO_REQUIRE_ATTACHED_NODE,

    /** Set the value attribute of a reset input to 'Reset' if no value attribute specified. */
    @BrowserFeature(@WebBrowser(IE))
    RESETINPUT_DEFAULT_VALUE_IF_VALUE_NOT_DEFINED,

    /**
     * Indicates that all options of a select are deselected,
     * if the select state is changed for an unknown option.
     */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(IE) })
    SELECT_DESELECT_ALL_IF_SWITCHING_UNKNOWN,

    /** Indicates that string.contains() is supported. */
    @BrowserFeature(@WebBrowser(FF))
    STRING_CONTAINS,

    /** Indicates that string.trimLeft() and .trimRight() are supported. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    STRING_TRIM_LEFT_RIGHT,

    /**
     * Indicates that the href property for a &lt;link rel="stylesheet" type="text/css" href="" /&gt;
     * (href empty) is null.
     */
    @BrowserFeature(@WebBrowser(IE))
    STYLESHEET_HREF_EMPTY_IS_NULL,

    /** Set the value attribute of a submit input to 'Submit Query' if no value attribute specified. */
    @BrowserFeature(@WebBrowser(IE))
    SUBMITINPUT_DEFAULT_VALUE_IF_VALUE_NOT_DEFINED,

    /** Indicates that unknown tags inside an SVG element are handled as DOM elements, not SVG elements. */
    @BrowserFeature(@WebBrowser(IE))
    SVG_UNKNOWN_ARE_DOM,

    /**
     * Indicates, that the pathname for the url 'blank' is empty;
     * instead of 'blank'.
     */
    @BrowserFeature(@WebBrowser(CHROME))
    URL_ABOUT_BLANK_HAS_BLANK_PATH,

    /**
     * Indicates, that the pathname for the url {@code about:blank} is empty;
     * instead of '/blank'.
     */
    @BrowserFeature(@WebBrowser(FF))
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

    /** Execute window events. */
    @BrowserFeature(@WebBrowser(IE))
    WINDOW_EXECUTE_EVENTS,

    /** XMLHttpRequest triggers the opened event at the beginning of the send method again. */
    @BrowserFeature(@WebBrowser(IE))
    XHR_FIRE_STATE_OPENED_AGAIN_IN_ASYNC_MODE,

    /**
     * Indicates if the port should be ignored during origin check.
     * For IE11: this is currently a bug, see
     * http://connect.microsoft.com/IE/feedback/details/781303/
     * origin-header-is-not-added-to-cors-requests-to-same-domain-but-different-port
     */
    @BrowserFeature(@WebBrowser(IE))
    XHR_IGNORE_PORT_FOR_SAME_ORIGIN,

    /** A cross origin request to {@code about:blank} is not allowed. */
    @BrowserFeature(@WebBrowser(IE))
    XHR_NO_CROSS_ORIGIN_TO_ABOUT,

    /** Indicates if an empty url is allowed as url param for the open method. */
    @BrowserFeature({ @WebBrowser(FF), @WebBrowser(CHROME) })
    XHR_OPEN_ALLOW_EMTPY_URL,

    /** Indicates that open() throws an exception in sync mode if 'withCredentials' is set to true. */
    @BrowserFeature(@WebBrowser(FF))
    XHR_OPEN_WITHCREDENTIALS_TRUE_IN_SYNC_EXCEPTION,

    /** Indicates that method overrideMimeType throws if msg was already sent. */
    @BrowserFeature({ @WebBrowser(CHROME), @WebBrowser(IE) })
    XHR_OVERRIDE_MIME_TYPE_BEFORE_SEND,

    /** Indicates that the "*" pattern is allowed when withCredential is enabled. */
    @BrowserFeature(@WebBrowser(IE))
    XHR_WITHCREDENTIALS_ALLOW_ORIGIN_ALL,

    /**
     * Indicates that the property <code>withCredentials</code> is not writable for sync requests.
     * Setting the property throws an exception.
     */
    @BrowserFeature(@WebBrowser(FF))
    XHR_WITHCREDENTIALS_NOT_WRITEABLE_IN_SYNC_EXCEPTION,

    /** Indicates that the XPath attribute is case sensitive. */
    @BrowserFeature(@WebBrowser(CHROME))
    XPATH_ATTRIBUTE_CASE_SENSITIVE,

    /** Indicates that the 'SelectionNamespaces' property is supported by XPath expressions. */
    @BrowserFeature({ @WebBrowser(IE), @WebBrowser(CHROME) })
    XPATH_SELECTION_NAMESPACES,

}
