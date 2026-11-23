/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

    /**
     * The securitypolicyviolation event for a connection (XMLHttpRequest, fetch) blocked because of
     * a redirect target is fired after the request has failed (after the error event).
     */
    @BrowserFeature({CHROME, EDGE})
    CSP_CONNECT_REDIRECT_VIOLATION_DELAYED,

    /**
     * The form-action directive of the Content-Security-Policy is not checked for the
     * redirects of a form submission.
     */
    @BrowserFeature({FF, FF_ESR})
    CSP_FORM_ACTION_REDIRECTS_NOT_CHECKED,

    /**
     * The content of a Content-Security-Policy meta element is parsed as one policy
     * (commas are not splitting the content into several policies).
     */
    @BrowserFeature({FF, FF_ESR})
    CSP_META_SINGLE_POLICY,

    /**
     * The nonce attribute is only hidden if the page has an enforced policy delivered by a header
     * (not for report only policies).
     */
    @BrowserFeature({FF, FF_ESR})
    CSP_NONCE_HIDING_ENFORCED_ONLY,

    /**
     * Leading and trailing whitespace of the nonce of an element is ignored when matching
     * the nonce source expressions of the Content-Security-Policy.
     */
    @BrowserFeature({CHROME, EDGE})
    CSP_NONCE_TRIMMED,

    /** The error event is fired at an object element if the data url is blocked by the object-src directive. */
    @BrowserFeature({FF, FF_ESR})
    CSP_OBJECT_BLOCKED_FIRES_ERROR,

    /**
     * The Content-Security-Policy violation report (report-uri) uses the Firefox format:
     * the keys are sorted, the report uris of the original policy are absolute, the script-sample
     * is only included if not empty and the column-number is always included.
     */
    @BrowserFeature({FF, FF_ESR})
    CSP_REPORT_FIREFOX_FORMAT,

    /**
     * If the page upgrades insecure requests (upgrade-insecure-requests), http urls are matching
     * https source expressions also if the url is not upgraded (loopback hosts).
     */
    @BrowserFeature({FF, FF_ESR})
    CSP_UPGRADE_INSECURE_REQUESTS_MATCHES_HTTPS,

    /** Background image is 'initial'. */
    @BrowserFeature({CHROME, EDGE})
    CSS_BACKGROUND_INITIAL,

    /** Background image is 'rgba(0, 0, 0, 0)'. */
    @BrowserFeature({FF, FF_ESR})
    CSS_BACKGROUND_RGBA,

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
    @BrowserFeature({FF, FF_ESR})
    CSS_RT_DISPLAY_RUBY_TEXT_ALWAYS,

    /**
     * The MIME type of a data url is serialized like the MIME Sniffing Standard does it
     * (duplicate parameters are ignored, the charset parameter stays in place).
     */
    @BrowserFeature({FF, FF_ESR})
    DATA_URL_MIME_TYPE_STANDARD_SERIALIZATION,

    /** The context menu MouseEvent has a detail of 1. */
    @BrowserFeature({FF, FF_ESR})
    EVENT_CONTEXT_MENU_HAS_DETAIL_1,

    /** Triggers the onfocus event when focusing the body on load. */
    @BrowserFeature({FF, FF_ESR})
    EVENT_FOCUS_ON_LOAD,

    /** Scroll events are of type 'UIEvent'. */
    @BrowserFeature({FF, FF_ESR})
    EVENT_SCROLL_UIEVENT,

    /** Form elements are able to refer to the for by using the form attribute. */
    @BrowserFeature({CHROME, EDGE})
    FORM_IGNORE_REL_NOREFERRER,

    /** Form submit includes the Cache-Control: max-age=0 header. */
    @BrowserFeature({CHROME, EDGE})
    FORM_SUBMISSION_HEADER_CACHE_CONTROL_MAX_AGE,

    /** Should org.htmlunit.javascript.host.html.HTMLBaseFontElement#isEndTagForbidden(). */
    @BrowserFeature({FF, FF_ESR})
    HTMLBASEFONT_END_TAG_FORBIDDEN,

    /** HtmlCollection.namedItem searches by id first. */
    @BrowserFeature({CHROME, EDGE})
    HTMLCOLLECTION_NAMED_ITEM_ID_FIRST,

    /**
    /** {@code document.getElementsByName} returns an empty list if called with the empty string.
     */
    @BrowserFeature({FF, FF_ESR})
    HTMLDOCUMENT_ELEMENTS_BY_NAME_EMPTY,

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

    /** HTMLInputElement image type is not supported. */
    @BrowserFeature({CHROME, EDGE})
    HTMLINPUT_TYPE_IMAGE_IGNORES_CUSTOM_VALIDITY,

    /** HTMLInputElement month type is supported. */
    @BrowserFeature({CHROME, EDGE})
    HTMLINPUT_TYPE_MONTH_SUPPORTED,

    /** HTMLInputElement week type is supported. */
    @BrowserFeature({CHROME, EDGE})
    HTMLINPUT_TYPE_WEEK_SUPPORTED,

    /** For a stylesheet link report an error if the response type is not text/css. */
    @BrowserFeature({FF, FF_ESR})
    HTMLLINK_CHECK_RESPONSE_TYPE_FOR_STYLESHEET,

    /** If the type is present for a link only use if type is text/css. */
    @BrowserFeature({CHROME, EDGE})
    HTMLLINK_CHECK_TYPE_FOR_STYLESHEET,

    /** Should org.htmlunit.javascript.host.html.HTMLTrackElement#isEndTagForbidden(). */
    @BrowserFeature({FF, FF_ESR})
    HTMLTRACK_END_TAG_FORBIDDEN,

    /** HTML parser supports the 'layer' tag. */
    @BrowserFeature({CHROME, EDGE})
    HTML_LAYER_TAG,

    /** Additionally support dates in format "d/M/yyyy". */
    @BrowserFeature({FF, FF_ESR})
    HTTP_COOKIE_EXTENDED_DATE_PATTERNS_1,

    /** Dates format pattern 2. */
    @BrowserFeature({CHROME, EDGE})
    HTTP_COOKIE_EXTENDED_DATE_PATTERNS_2,

    /** Cookie prefix '__Host-' allows a domain attribute if the domain is the host itself. */
    @BrowserFeature({FF, FF_ESR})
    HTTP_COOKIE_HOST_PREFIX_ALLOWS_DOMAIN_OF_HOST,

    /** Cookie prefix '__Host-' requires only the path '/'; the path attribute is optional. */
    @BrowserFeature(FF_ESR)
    HTTP_COOKIE_HOST_PREFIX_PATH_ATTRIBUTE_OPTIONAL,

    /** Browser sends Sec-ch headers. */
    @BrowserFeature({CHROME, EDGE})
    HTTP_HEADER_CH_UA,

    /** Browser sends Priority headers. */
    @BrowserFeature({FF, FF_ESR})
    HTTP_HEADER_PRIORITY,

    /**
     * The anchor pathname detects url's starting with one letter as file url's
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

    /** Sorting an array using a user defined comperator accepts inconsistent iterators. */
    @BrowserFeature({FF, FF_ESR})
    JS_ARRAY_SORT_ACCEPTS_INCONSISTENT_COMPERATOR,

    /** AudioProcessingEvent ctor is callable. */
    @BrowserFeature({CHROME, EDGE})
    JS_AUDIO_PROCESSING_EVENT_CTOR,

    /** toDataURL for canvas returns the CHROME version of the PNG. */
    @BrowserFeature({CHROME, EDGE})
    JS_CANVAS_DATA_URL_CHROME_PNG,

    /** Setting the size of a canvas (clearing the bitmap) resets the origin-clean flag. */
    @BrowserFeature({CHROME, EDGE})
    JS_CANVAS_RESIZE_RESETS_ORIGIN_CLEAN,

    /** ClientHeight for input is 17. */
    @BrowserFeature({CHROME, EDGE})
    JS_CLIENTHEIGHT_INPUT_17,

    /** ClientHeight for input is 18. */
    @BrowserFeature({FF, FF_ESR})
    JS_CLIENTHEIGHT_INPUT_18,

    /** ClientHeight for radio button and checkbox is 14. */
    @BrowserFeature({FF, FF_ESR})
    JS_CLIENTHEIGHT_RADIO_CHECKBOX_14,

    /** ClientHeight for rb is 17. */
    @BrowserFeature({FF, FF_ESR})
    JS_CLIENTHEIGHT_RB_17,

    /** ClientHeight for rt is 9. */
    @BrowserFeature({FF, FF_ESR})
    JS_CLIENTHEIGHT_RT_9,

    /** ClientHeight for ruby is 17. */
    @BrowserFeature({FF, FF_ESR})
    JS_CLIENTHEIGHT_RUBY_17,

    /** ClientWidth for text/password input is 173. */
    @BrowserFeature({CHROME, EDGE})
    JS_CLIENTWIDTH_INPUT_TEXT_169,

    /** ClientWidth for radio button and checkbox is 14. */
    @BrowserFeature({FF, FF_ESR})
    JS_CLIENTWIDTH_RADIO_CHECKBOX_14,

    /** item is enumerated before length property of CSSRuleList. */
    @BrowserFeature({FF, FF_ESR})
    JS_CSSRULELIST_ENUM_ITEM_LENGTH,

    /** Javascript document.evaluate() creates a new result object even if provided as param. */
    @BrowserFeature({CHROME, EDGE})
    JS_DOCUMENT_EVALUATE_RECREATES_RESULT,

    /** The browser has selection {@code rangeCount}. */
    @BrowserFeature({FF, FF_ESR})
    JS_DOCUMENT_SELECTION_RANGE_COUNT,

    /** Javascript {@code Error.stackTraceLimit}. */
    @BrowserFeature({CHROME, EDGE})
    JS_ERROR_STACK_TRACE_LIMIT,

    /** Javascript {@code Error.stackTraceLimit}. */
    @BrowserFeature({FF, FF_ESR})
    JS_ERROR_STACK_TRACE_LIMIT_128,

    /** Javascript InputEvent reads the inputType property from data. */
    @BrowserFeature({FF, FF_ESR})
    JS_EVENT_INPUT_CTOR_INPUTTYPE,

    /** Javascript KeyboardEvent reads the which property from data. */
    @BrowserFeature({FF, FF_ESR})
    JS_EVENT_KEYBOARD_CTOR_WHICH,

    /**
     * Javascript SecurityPolicyViolationEvent created by the constructor always bubbles,
     * is composed and is not cancelable (the init values are ignored).
     */
    @BrowserFeature({CHROME, EDGE})
    JS_EVENT_SECURITY_POLICY_VIOLATION_CTOR_BUBBLES_COMPOSED,

    /**
     * Javascript SecurityPolicyViolationEvent lineNumber and columnNumber are unsigned
     * (-1 is converted to 4294967295).
     */
    @BrowserFeature({FF, FF_ESR})
    JS_EVENT_SECURITY_POLICY_VIOLATION_POSITION_UNSIGNED,

    /** form.dispatchEvent(e) submits the form if the event is of type 'submit'. */
    @BrowserFeature({FF, FF_ESR})
    JS_FORM_DISPATCHEVENT_SUBMITS,

    /** The named properties (ids and names of the options) of HTMLOptionsCollection are enumerable. */
    @BrowserFeature({CHROME, EDGE})
    JS_HTMLOPTIONSCOLLECTION_NAMED_PROPERTIES_ENUMERABLE,

    /** Executes the {@code onload} handler, regardless of the element was already attached to the page. */
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

    /** Indicates that the HTML serialization of a processing instruction ends with '&gt;' instead of '?&gt;'. */
    @BrowserFeature({FF, FF_ESR})
    JS_INNER_HTML_PROCESSING_INSTRUCTION_NO_QUESTION_MARK,

    /** Indicates that innerHTML returns an empty string for empty elements. */
    @BrowserFeature({CHROME, EDGE})
    JS_INNER_HTML_VOID_ELEMENT_EMPTY,

    /**
     * Indicates that the XML serialization writes boolean attributes of XHTML elements
     * with an empty value as name="name" (e.g. disabled="disabled").
     */
    @BrowserFeature({FF, FF_ESR})
    JS_INNER_HTML_XHTML_BOOLEAN_ATTRIBUTE_VALUE,

    /** Indicates that the XML serialization escapes line breaks in attribute values as hex references (&amp;#xA;). */
    @BrowserFeature({FF, FF_ESR})
    JS_INNER_HTML_XHTML_HEX_LINE_BREAK,

    /** Indicates that the XML serialization writes the namespace declaration of an attribute after the attribute. */
    @BrowserFeature({FF, FF_ESR})
    JS_INNER_HTML_XHTML_NS_DECLARATION_AFTER_ATTRIBUTE,

    /** Indicates that innerHTML of a script or style element in an XHTML document returns the text as is. */
    @BrowserFeature({FF, FF_ESR})
    JS_INNER_HTML_XHTML_RAW_SCRIPT_STYLE,

    /** Indicates that innerText add a nl when reaching svg element. */
    @BrowserFeature({FF, FF_ESR})
    JS_INNER_TEXT_SELECT_EMPTY,

    /** Indicates that innerText add a nl when reaching svg element. */
    @BrowserFeature({CHROME, EDGE})
    JS_INNER_TEXT_SVG_NL,

    /** The value is ignored when the type of the week/month input is changed. */
    @BrowserFeature({CHROME, EDGE})
    JS_INPUT_CHANGE_TYPE_DROPS_VALUE_WEEK_MONTH,

    /** FF accepts all chars. */
    @BrowserFeature({FF, FF_ESR})
    JS_INPUT_NUMBER_ACCEPT_ALL,

    /** FF comma at end is not an integer. */
    @BrowserFeature({FF, FF_ESR})
    JS_INPUT_NUMBER_DOT_AT_END_IS_DOUBLE,

    /** Indicates that the collation (from the options or for usage 'search') is part of the Intl.Collator locale. */
    @BrowserFeature({CHROME, EDGE})
    JS_INTL_COLLATOR_COLLATION_IN_LOCALE,

    /** Indicates that Intl.v8BreakIterator is supported. */
    @BrowserFeature({CHROME, EDGE})
    JS_INTL_V8_BREAK_ITERATOR,

    /** Setting location.protocol does not navigate if the url is not changed (e.g. 'file:' for an url with port). */
    @BrowserFeature({FF, FF_ESR})
    JS_LOCATION_PROTOCOL_UNCHANGED_NO_NAVIGATION,

    /** Reload sends a referrer header. */
    @BrowserFeature({CHROME, EDGE})
    JS_LOCATION_RELOAD_REFERRER,

    /** Type property of menu returns the current (maybe invalid) value. */
    @BrowserFeature({FF, FF_ESR})
    JS_MENU_TYPE_PASS,

    /**
     * Object.getOwnPropertyNames() of objects with named properties (e.g. NamedNodeMap, HTMLOptionsCollection)
     * also lists the named properties hidden by prototype properties.
     */
    @BrowserFeature({CHROME, EDGE})
    JS_NAMED_PROPERTIES_OWN_PROPERTY_NAMES_INCLUDE_HIDDEN,

    /** Indicates if the String representation of a native function is without newline. */
    @BrowserFeature({CHROME, EDGE})
    JS_NATIVE_FUNCTION_TOSTRING_COMPACT,

    /** Indicates if the String representation of a native function has a newline for empty parameter list. */
    @BrowserFeature({FF, FF_ESR})
    JS_NATIVE_FUNCTION_TOSTRING_NL,

    /** Navigator.doNotTrack returns unspecified if not set. */
    @BrowserFeature({FF, FF_ESR})
    JS_NAVIGATOR_DO_NOT_TRACK_UNSPECIFIED,

    /** PerformanceObserver.supportedEntryTypes returns only the entry types supported by Firefox. */
    @BrowserFeature({FF, FF_ESR})
    JS_PERFORMANCE_OBSERVER_ENTRY_TYPES_FF,

    /** PerformanceObserver.supportedEntryTypes returns a frozen array. */
    @BrowserFeature({CHROME, EDGE})
    JS_PERFORMANCE_OBSERVER_ENTRY_TYPES_FROZEN,

    /** Indicates that the {@code Object.getOwnPropertyDescriptor.get} contains name. */
    @BrowserFeature({FF, FF_ESR})
    JS_PROPERTY_DESCRIPTOR_NAME,

    /**
     * The User-Agent header is a forbidden request header (fetch() and XMLHttpRequest);
     * it is no longer part of the forbidden request headers of the Fetch standard.
     */
    @BrowserFeature({CHROME, EDGE})
    JS_REQUEST_HEADER_USER_AGENT_FORBIDDEN,

    /** script tags created from js as child of templates is processed if added to the dom. */
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

    /**
     * File urls of URL objects have no host: the host 'localhost' is removed, setting the protocol to
     * 'file' removes the host.
     */
    @BrowserFeature({FF, FF_ESR})
    JS_URL_FILE_WITHOUT_HOST,

    /** A WebSocket blocked by the Content-Security-Policy fires only the error event, no close event. */
    @BrowserFeature({CHROME, EDGE})
    JS_WEBSOCKET_CSP_BLOCKED_NO_CLOSE_EVENT,

    /** window.getComputedStyle works with pseudo selectors without colon in front. */
    @BrowserFeature({CHROME, EDGE})
    JS_WINDOW_COMPUTED_STYLE_PSEUDO_ACCEPT_WITHOUT_COLON,

    /** Javascript InstallTrigger property set to null. */
    @BrowserFeature({FF, FF_ESR})
    JS_WINDOW_INSTALL_TRIGGER_NULL,

    /**
     * Method window.open() of a local file (file url) from a page that was not loaded from a file
     * throws an Error.
     */
    @BrowserFeature({FF, FF_ESR})
    JS_WINDOW_OPEN_LOCAL_FILE_THROWS,

    /** window.open() of a sandboxed document without 'allow-popups' throws an InvalidAccessError. */
    @BrowserFeature({FF, FF_ESR})
    JS_WINDOW_OPEN_SANDBOXED_THROWS,

    /**
     * Difference of window.outer/inner height is 138.
     */
    @BrowserFeature(EDGE)
    JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_138,

    /**
     * Difference of window.outer/inner height is 147.
     */
    @BrowserFeature(CHROME)
    JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_147,

    /**
     * Difference of window.outer/inner height is 94.
     */
    @BrowserFeature({FF, FF_ESR})
    JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_94,

    /** Window.getSelection returns null, if the window is not visible. */
    @BrowserFeature({FF, FF_ESR})
    JS_WINDOW_SELECTION_NULL_IF_INVISIBLE,

    /**
     * Calling importScripts() of a local file (file url) from a worker of a page that was not loaded from a file
     * throws a SecurityError (instead of a NetworkError).
     */
    @BrowserFeature({FF, FF_ESR})
    JS_WORKER_IMPORT_SCRIPTS_LOCAL_FILE_SECURITY_ERROR,

    /**
     * Calling new Worker() of a local file (file url) from a page that was not loaded from a file does not throw;
     * the worker is not started and the error event is fired.
     */
    @BrowserFeature({FF, FF_ESR})
    JS_WORKER_LOCAL_FILE_FIRES_ERROR,

    /** A page loaded from a file is not allowed to start a worker from a local file (SecurityError). */
    @BrowserFeature({CHROME, EDGE})
    JS_WORKER_LOCAL_FILE_FORBIDDEN,

    /** Take care of the response charset when reading a worker script. */
    @BrowserFeature({CHROME, EDGE})
    JS_WORKER_SCRIPT_HONORS_CHARSET_HEADER,

    /** {@code XSLTProcessor.transformToDocument} supports output indent attribute. */
    @BrowserFeature({CHROME, EDGE})
    JS_XSLT_TRANSFORM_INDENT,

    /** Typing a char while the meta key is pressed fires no keypress event and does not insert the char. */
    @BrowserFeature({FF, FF_ESR})
    KEYBOARD_EVENT_META_NO_KEYPRESS,

    /** The key codes of the ';', '=' and '-' keys are 186, 187 and 189 (instead of 59, 61 and 173). */
    @BrowserFeature({CHROME, EDGE})
    KEYBOARD_EVENT_OEM_KEY_CODES,

    /**
     * The less restrictive referrer policies ('unsafe-url' and 'no-referrer-when-downgrade')
     * are ignored for cross-site requests (the default policy is used).
     */
    @BrowserFeature({FF, FF_ESR})
    REFERRER_POLICY_IGNORE_CROSS_SITE_RELAXING,

    /**
     * Indicates that the Browser handles async and sync network errors the same way.
     */
    @BrowserFeature({FF, FF_ESR})
    XHR_HANDLE_SYNC_NETWORK_ERRORS,

    /** XMLHttpRequest triggers the load events also if the abort was signaled. */
    @BrowserFeature({FF, FF_ESR})
    XHR_LOAD_ALWAYS_AFTER_DONE,

    /** Indicates if the XMLHttpRequest preflight request includes the 'Sec-Fetch-Mode=cors' header. */
    @BrowserFeature({CHROME, EDGE})
    XHR_PREFLIGHT_CORS,

    /** If state unsent the response text is empty even if the response type is wrong. */
    @BrowserFeature({FF, FF_ESR})
    XHR_RESPONSE_TEXT_EMPTY_UNSENT,

    /** Indicates if the XMLHttpRequest.send() method will throw if aborted. */
    @BrowserFeature({CHROME, EDGE})
    XHR_SEND_NETWORK_ERROR_IF_ABORTED,
}
