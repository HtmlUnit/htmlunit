/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit;

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import org.htmlunit.javascript.configuration.BrowserFeature;

/**
 * Constants of various features of each {@link BrowserVersion}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Frank Danek
 * @author Carsten Steul
 * @author Anton Demydenko
 */
public enum BrowserVersionFeatures {

    /** Ignore target when {@code href} is a javascript snippet. */
    @BrowserFeature({CHROME, EDGE})
    ANCHOR_SEND_PING_REQUEST,

    /** Background image is 'initial'. */
    @BrowserFeature({CHROME, EDGE})
    CSS_BACKGROUND_INITIAL,

    /** Background image is 'rgba(0, 0, 0, 0)'. */
    @BrowserFeature({FF, FF_ESR})
    CSS_BACKGROUND_RGBA,

    /** {@code CSSFontFaceRule.cssText} uses one more blank. */
    @BrowserFeature(FF)
    CSS_CSSTEXT_FF_STYLE,

    /** Is display style 'block'. */
    @BrowserFeature({FF, FF_ESR})
    CSS_DISPLAY_BLOCK,

    /** The default value of the display property for the 'noscript' tag is 'inline' instead of the default one. */
    @BrowserFeature({CHROME, EDGE})
    CSS_NOSCRIPT_DISPLAY_INLINE,

    /** The default value of the display property for the 'rp' tag is 'none'. */
    @BrowserFeature({FF, FF_ESR})
    CSS_RP_DISPLAY_NONE,

    /** The default value of the display property for the 'rt' tag is always 'ruby-text'. */
    @BrowserFeature(FF_ESR)
    CSS_RT_DISPLAY_RUBY_TEXT_ALWAYS,

    /** The context menu MouseEvent has a detail of 1. */
    @BrowserFeature(FF)
    EVENT_CONTEXT_MENU_HAS_DETAIL_1,

    /** Triggers the onfocus event when focusing the body on load. */
    @BrowserFeature({FF, FF_ESR})
    EVENT_FOCUS_ON_LOAD,

    /** <code>AnimationEvent</code> can not be created by calling document.createEvent('AnimationEvent'). */
    @BrowserFeature({FF, FF_ESR})
    EVENT_ONANIMATION_DOCUMENT_CREATE_NOT_SUPPORTED,

    /** Triggers 'onclick' event handler using <code>PointerEvent</code>. */
    @BrowserFeature({CHROME, EDGE})
    EVENT_ONCLICK_USES_POINTEREVENT,

    /** <code>CloseEvent</code> can not be created by calling document.createEvent('CloseEvent'). */
    @BrowserFeature({FF, FF_ESR})
    EVENT_ONCLOSE_DOCUMENT_CREATE_NOT_SUPPORTED,

    /** FF triggers a mouseover event even if the option is disabled. */
    @BrowserFeature({FF, FF_ESR})
    EVENT_ONMOUSEOVER_FOR_DISABLED_OPTION,

    /** <code>PopStateEvent</code> can not be created by calling document.createEvent('PopStateEvent'). */
    @BrowserFeature({FF, FF_ESR})
    EVENT_ONPOPSTATE_DOCUMENT_CREATE_NOT_SUPPORTED,

    /** Supports event type 'TextEvent'. */
    @BrowserFeature({CHROME, EDGE})
    EVENT_TYPE_TEXTEVENT,

    /** Supports event type 'WheelEvent'. */
    @BrowserFeature({CHROME, EDGE})
    EVENT_TYPE_WHEELEVENT,

    /** Form elements are able to refer to the for by using the from attribute. */
    @BrowserFeature({CHROME, EDGE})
    FORM_IGNORE_REL_NOREFERRER,

    /** Form submit includes the Cache-Control: max-age=0 header. */
    @BrowserFeature({CHROME, EDGE})
    FORM_SUBMISSION_HEADER_CACHE_CONTROL_MAX_AGE,

    /** If the frame src has 'about:' scheme always use 'about:blank' as source. */
    @BrowserFeature({FF, FF_ESR})
    FRAME_LOCATION_ABOUT_BLANK_FOR_ABOUT_SCHEME,

    /** Should org.htmlunit.javascript.host.html.HTMLBaseFontElement#isEndTagForbidden(). */
    @BrowserFeature({FF, FF_ESR})
    HTMLBASEFONT_END_TAG_FORBIDDEN,

    /** If type submit/reset the form update is triggered even if disabled. */
    @BrowserFeature(FF_ESR)
    HTMLBUTTON_SUBMIT_IGNORES_DISABLED_STATE,

    /** willValidate does not check the readonly property. */
    @BrowserFeature({FF, FF_ESR})
    HTMLBUTTON_WILL_VALIDATE_IGNORES_READONLY,

    /** HtmlCollection.namedItem searches by id first. */
    @BrowserFeature({CHROME, EDGE})
    HTMLCOLLECTION_NAMED_ITEM_ID_FIRST,

    /** Calling cookies setter with blank string does not reset the cookies. */
    @BrowserFeature({CHROME, EDGE})
    HTMLDOCUMENT_COOKIES_IGNORE_BLANK,

    /**
    /** {@code document.getElementsByName} returns an empty list if called with the empty string.
     */
    @BrowserFeature({FF, FF_ESR})
    HTMLDOCUMENT_ELEMENTS_BY_NAME_EMPTY,

    /** Calls to <code>document.XYZ</code> also looks at frames. */
    @BrowserFeature({CHROME, EDGE})
    HTMLDOCUMENT_GET_ALSO_FRAMES,

    /** Removing the active element from the dom tree triggers the onblur event. */
    @BrowserFeature({CHROME, EDGE})
    HTMLELEMENT_REMOVE_ACTIVE_TRIGGERS_BLUR_EVENT,

    /** Handle blank source like empty. */
    @BrowserFeature({CHROME, EDGE})
    HTMLIMAGE_BLANK_SRC_AS_EMPTY,

    /** Empty src attribute sets display to false. */
    @BrowserFeature({FF, FF_ESR})
    HTMLIMAGE_EMPTY_SRC_DISPLAY_FALSE,

    /** Is document.cretaeElement('image') an HTMLElement. */
    @BrowserFeature({FF, FF_ESR})
    HTMLIMAGE_HTMLELEMENT,

    /** Is document.cretaeElement('image') an HTMLUnknownElement. */
    @BrowserFeature({CHROME, EDGE})
    HTMLIMAGE_HTMLUNKNOWNELEMENT,

    /** Clicking an image input submits the value as param if defined. */
    @BrowserFeature({CHROME, EDGE})
    HTMLIMAGE_NAME_VALUE_PARAMS,

    /** When clicking a {@code checkbox} or {@code radio} input the surrounding anchor is not clicked. */
    @BrowserFeature({CHROME, EDGE, FF})
    HTMLINPUT_CHECKBOX_DOES_NOT_CLICK_SURROUNDING_ANCHOR,

    /** HTMLInputElement image type is not supported. */
    @BrowserFeature({CHROME, EDGE})
    HTMLINPUT_TYPE_IMAGE_IGNORES_CUSTOM_VALIDITY,

    /** HTMLInputElement month type is supported. */
    @BrowserFeature({CHROME, EDGE})
    HTMLINPUT_TYPE_MONTH_SUPPORTED,

    /** HTMLInputElement week type is supported. */
    @BrowserFeature({CHROME, EDGE})
    HTMLINPUT_TYPE_WEEK_SUPPORTED,

    /** If the type is present for a link only use if type is text/css. */
    @BrowserFeature({CHROME, EDGE})
    HTMLLINK_CHECK_TYPE_FOR_STYLESHEET,

    /** willValidate does not check the readonly property. */
    @BrowserFeature({FF, FF_ESR})
    HTMLSELECT_WILL_VALIDATE_IGNORES_READONLY,

    /** Should org.htmlunit.javascript.host.html.HTMLTrackElement#isEndTagForbidden(). */
    @BrowserFeature({FF, FF_ESR})
    HTMLTRACK_END_TAG_FORBIDDEN,

    /** HTML parser supports the 'command' tag. */
    @BrowserFeature({CHROME, EDGE})
    HTML_COMMAND_TAG,

    /** Additionally support dates in format "d/M/yyyy". */
    @BrowserFeature({FF, FF_ESR})
    HTTP_COOKIE_EXTENDED_DATE_PATTERNS_1,

    /** Dates format pattern 2. */
    @BrowserFeature({CHROME, EDGE})
    HTTP_COOKIE_EXTENDED_DATE_PATTERNS_2,

    /** domain '.org' is handled as 'org'. */
    @BrowserFeature({FF, FF_ESR})
    HTTP_COOKIE_REMOVE_DOT_FROM_ROOT_DOMAINS,

    /** Browser sends Sec-ch headers. */
    @BrowserFeature({CHROME, EDGE})
    HTTP_HEADER_CH_UA,

    /** The anchor hostname setter ignores blank url's. */
    @BrowserFeature({FF, FF_ESR})
    JS_ANCHOR_HOSTNAME_IGNORE_BLANK,

    /** The anchor pathname detects url's starting with one letter as file url's. */
    @BrowserFeature(FF_ESR)
    JS_ANCHOR_PATHNAME_DETECT_WIN_DRIVES_URL,

    /** The anchor pathname detects url's starting with one letter as file url's
     * and replaces them with the file protocol. */
    @BrowserFeature({CHROME, EDGE})
    JS_ANCHOR_PATHNAME_DETECT_WIN_DRIVES_URL_REPLACE,

    /** The anchor pathname prefixes file url's with '/'. */
    @BrowserFeature({CHROME, EDGE})
    JS_ANCHOR_PATHNAME_PREFIX_WIN_DRIVES_URL,

    /** The anchor protocol property converts drive letters to uppercase. */
    @BrowserFeature({CHROME, EDGE})
    JS_ANCHOR_PROTOCOL_COLON_UPPER_CASE_DRIVE_LETTERS,

    /** An area element without a href attribute is focusable. */
    @BrowserFeature({FF, FF_ESR})
    JS_AREA_WITHOUT_HREF_FOCUSABLE,

    /** AudioProcessingEvent ctor is callable. */
    @BrowserFeature({CHROME, EDGE})
    JS_AUDIO_PROCESSING_EVENT_CTOR,

    /** HTMLBGSoundElement reported as HTMLUnknownElement. */
    @BrowserFeature({FF, FF_ESR})
    JS_BGSOUND_AS_UNKNOWN,

    /** toDataURL for canvas returns the CHROME version of the PNG. */
    @BrowserFeature({CHROME, EDGE})
    JS_CANVAS_DATA_URL_CHROME_PNG,

    /** ClientHeight for input is 17. */
    @BrowserFeature({CHROME, EDGE})
    JS_CLIENTHEIGHT_INPUT_17,

    /** ClientHeight for input is 18. */
    @BrowserFeature({FF, FF_ESR})
    JS_CLIENTHEIGHT_INPUT_18,

    /** ClientHeight for radio button and checkbox is 10. */
    @BrowserFeature({FF, FF_ESR})
    JS_CLIENTHEIGHT_RADIO_CHECKBOX_10,

    /** ClientWidth for text/password input is 173. */
    @BrowserFeature({CHROME, EDGE})
    JS_CLIENTWIDTH_INPUT_TEXT_173,

    /** ClientWidth for radio button and checkbox is 10. */
    @BrowserFeature({FF, FF_ESR})
    JS_CLIENTWIDTH_RADIO_CHECKBOX_10,

    /** item is enumerated before length property of CSSRuleList. */
    @BrowserFeature({FF, FF_ESR})
    JS_CSSRULELIST_ENUM_ITEM_LENGTH,

    /** Javascript document.evaluate creates a new result object even if provided as param. */
    @BrowserFeature({CHROME, EDGE})
    JS_DOCUMENT_EVALUATE_RECREATES_RESULT,

    /** The browser has selection {@code rangeCount}. */
    @BrowserFeature({FF, FF_ESR})
    JS_DOCUMENT_SELECTION_RANGE_COUNT,

    /** Javascript {@code Error.captureStackTrace}. */
    @BrowserFeature({CHROME, EDGE})
    JS_ERROR_CAPTURE_STACK_TRACE,

    /** Javascript {@code Error.stackTraceLimit}. */
    @BrowserFeature({CHROME, EDGE})
    JS_ERROR_STACK_TRACE_LIMIT,

    /** Javascript InputEvent reads the inputType property from data. */
    @BrowserFeature({FF, FF_ESR})
    JS_EVENT_INPUT_CTOR_INPUTTYPE,

    /** Javascript KeyboardEvent reads the which property from data. */
    @BrowserFeature({FF, FF_ESR})
    JS_EVENT_KEYBOARD_CTOR_WHICH,

    /** do not trigger the onload event if the frame content
     * was not shown because of the csp. */
    @BrowserFeature(FF_ESR)
    JS_EVENT_LOAD_SUPPRESSED_BY_CONTENT_SECURIRY_POLICY,

    /** form.dispatchEvent(e) submits the form if the event is of type 'submit'. */
    @BrowserFeature({FF, FF_ESR})
    JS_FORM_DISPATCHEVENT_SUBMITS,

    /** HTMLObject Validity isValid ignores custom error property. */
    @BrowserFeature({CHROME, EDGE})
    JS_HTML_OBJECT_VALIDITYSTATE_ISVALID_IGNORES_CUSTOM_ERROR,

    /** Executes the {@code onload} handler, regardless of the whether the element was already attached to the page. */
    @BrowserFeature({FF, FF_ESR})
    JS_IFRAME_ALWAYS_EXECUTE_ONLOAD,

    /**
     * Getting the width and height of an image tag without a source returns 16x16;
     * for invalid values returns 0.
     */
    @BrowserFeature({CHROME, EDGE})
    JS_IMAGE_WIDTH_HEIGHT_RETURNS_16x16_0x0,

    /**
     * Getting the width and height of an image tag without a source returns 24x24;
     * for invalid values returns 0x0.
     */
    @BrowserFeature({FF, FF_ESR})
    JS_IMAGE_WIDTH_HEIGHT_RETURNS_24x24_0x0,

    /** Indicates that innerText add a nl when reaching svg element. */
    @BrowserFeature({CHROME, EDGE})
    JS_INNER_TEXT_SVG_NL,

    /** The value is ignored when the type of an week/month input is changed. */
    @BrowserFeature({CHROME, EDGE})
    JS_INPUT_CHANGE_TYPE_DROPS_VALUE_WEEK_MONTH,

    /** FF accepts all chars. */
    @BrowserFeature({FF, FF_ESR})
    JS_INPUT_NUMBER_ACCEPT_ALL,

    /** FF comma at end is not an integer. */
    @BrowserFeature({FF, FF_ESR})
    JS_INPUT_NUMBER_DOT_AT_END_IS_DOUBLE,

    /** Indicates that Intl.v8BreakIterator is supported. */
    @BrowserFeature({CHROME, EDGE})
    JS_INTL_V8_BREAK_ITERATOR,

    /** Indicates that window.Iterator is supported. */
    @BrowserFeature({CHROME, EDGE})
    JS_ITERATOR_VISIBLE_IN_WINDOW,

    /** Reload sends a referrer header. */
    @BrowserFeature({CHROME, EDGE})
    JS_LOCATION_RELOAD_REFERRER,

    /** Type property of menu returns the current (maybe invalid) value. */
    @BrowserFeature({FF, FF_ESR})
    JS_MENU_TYPE_PASS,

    /** Indicates if the String representation of a native function is without newline. */
    @BrowserFeature({CHROME, EDGE})
    JS_NATIVE_FUNCTION_TOSTRING_COMPACT,

    /** Indicates if the String representation of a native function has a newline for empty parameter list. */
    @BrowserFeature({FF, FF_ESR})
    JS_NATIVE_FUNCTION_TOSTRING_NL,

    /** Navigator.doNotTrack returns unspecified if not set. */
    @BrowserFeature({FF, FF_ESR})
    JS_NAVIGATOR_DO_NOT_TRACK_UNSPECIFIED,

    /** Indicates that someObj.offsetParent returns null, it someObj has fixed style. */
    @BrowserFeature({CHROME, EDGE})
    JS_OFFSET_PARENT_NULL_IF_FIXED,

    /** element.outerHTML removes all children from detached node. */
    @BrowserFeature({CHROME, EDGE})
    JS_OUTER_HTML_THROWS_FOR_DETACHED,

    /** Indicates that the {@code Object.getOwnPropertyDescriptor.get} contains name. */
    @BrowserFeature({FF, FF_ESR})
    JS_PROPERTY_DESCRIPTOR_NAME,

    /** script tags created from js as child of templates are processed if added to the dom. */
    @BrowserFeature({CHROME, EDGE})
    JS_SCRIPT_IN_TEMPLATE_EXECUTED_ON_ATTACH,

    /** Javascript selectorText property returns selectors in lower case. */
    @BrowserFeature({CHROME, EDGE})
    JS_SELECTOR_TEXT_LOWERCASE,

    /** Indicates that select.options.remove ignores the call if index is too large. */
    @BrowserFeature({FF, FF_ESR})
    JS_SELECT_REMOVE_IGNORE_IF_INDEX_OUTSIDE,

    /** Whether to add to the storage even preserved words. */
    @BrowserFeature({FF, FF_ESR})
    JS_STORAGE_PRESERVED_INCLUDED,

    /** Indicates wordSpacing support percent values. */
    @BrowserFeature({FF, FF_ESR})
    JS_STYLE_WORD_SPACING_ACCEPTS_PERCENT,

    /** Set span zo zero if provided value is invalid. */
    @BrowserFeature({CHROME, EDGE})
    JS_TABLE_SPAN_SET_ZERO_IF_INVALID,

    /** window.getComputedStyle works with pseudo selectors without colon in front. */
    @BrowserFeature({CHROME, EDGE})
    JS_WINDOW_COMPUTED_STYLE_PSEUDO_ACCEPT_WITHOUT_COLON,

    /** Javascript InstallTrigger property set to null. */
    @BrowserFeature({FF, FF_ESR})
    JS_WINDOW_INSTALL_TRIGGER_NULL,

    /**
     * Difference of window.outer/inner height is 131.
     */
    @BrowserFeature(EDGE)
    JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_131,

    /**
     * Difference of window.outer/inner height is 138.
     */
    @BrowserFeature(CHROME)
    JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_148,

    /**
     * Difference of window.outer/inner height is 91.
     */
    @BrowserFeature({FF, FF_ESR})
    JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_91,

    /** Window.getSelection returns null, if the window is not visible. */
    @BrowserFeature({FF, FF_ESR})
    JS_WINDOW_SELECTION_NULL_IF_INVISIBLE,

    /** {@code XSLTProcessor.transformToDocument} supports output indent attribute. */
    @BrowserFeature({CHROME, EDGE})
    JS_XSLT_TRANSFORM_INDENT,

    /** With special keys [in .type(int)], should we trigger onkeypress event or not. */
    @BrowserFeature({FF, FF_ESR})
    KEYBOARD_EVENT_SPECIAL_KEYPRESS,

    /**
     * The default display style of multicol is 'block'.
     */
    @BrowserFeature({FF, FF_ESR})
    MULTICOL_BLOCK,

    /** The protocol setter does not check for special protocols. */
    @BrowserFeature(FF_ESR)
    URL_IGNORE_SPECIAL,

    /** XMLHttpRequest.getAllResponseHeaders() uses only Lf as separator. */
    @BrowserFeature({FF, FF_ESR})
    XHR_ALL_RESPONSE_HEADERS_SEPARATE_BY_LF,

    /**
     * Indicates that the Browser handles async and sync network errors the same way.
     */
    @BrowserFeature({FF, FF_ESR})
    XHR_HANDLE_SYNC_NETWORK_ERRORS,

    /** XMLHttpRequest triggers the load events also if the abort was signaled. */
    @BrowserFeature({FF, FF_ESR})
    XHR_LOAD_ALWAYS_AFTER_DONE,

    /** XMLHttpRequest triggers an additional progress event if a network error
     * was thrown in async mode. */
    @BrowserFeature(FF_ESR)
    XHR_PROGRESS_ON_NETWORK_ERROR_ASYNC,

    /** If state unsent the response text is empty even if the response type is wrong. */
    @BrowserFeature({FF, FF_ESR})
    XHR_RESPONSE_TEXT_EMPTY_UNSENT,

    /** Indicates if the XMLHttpRequest.send() method will throw if aborted. */
    @BrowserFeature({CHROME, EDGE})
    XHR_SEND_NETWORK_ERROR_IF_ABORTED,
}
