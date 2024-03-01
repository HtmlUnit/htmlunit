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
import static org.htmlunit.javascript.configuration.SupportedBrowser.IE;

import org.htmlunit.javascript.configuration.BrowserFeature;
import org.htmlunit.javascript.host.css.CSSGroupingRule;

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

    /** Indicates if a form field is directly reachable by its new name once this has been changed. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    FORMFIELD_REACHABLE_BY_NEW_NAMES,

    /** Indicates if a form field is directly reachable by its original name once this has been changed. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    FORMFIELD_REACHABLE_BY_ORIGINAL_NAME,

    /** Form elements are able to refer to the for by using the from attribute. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    FORM_FORM_ATTRIBUTE_SUPPORTED,

    /** Form elements are able to refer to the for by using the from attribute. */
    @BrowserFeature({CHROME, EDGE})
    FORM_IGNORE_REL_NOREFERRER,

    /** Form submit forces a real request also if only the hash was changed. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    FORM_SUBMISSION_DOWNLOWDS_ALSO_IF_ONLY_HASH_CHANGED,

    /** Form submit includes the Cache-Control: max-age=0 header. */
    @BrowserFeature({CHROME, EDGE})
    FORM_SUBMISSION_HEADER_CACHE_CONTROL_MAX_AGE,

    /** Forms are ignoring the rel='noreferrer' attribute. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    FORM_SUBMISSION_HEADER_ORIGIN,

    /** If the frame src has 'about:' scheme always use 'about:blank' as source. */
    @BrowserFeature({FF, FF_ESR})
    FRAME_LOCATION_ABOUT_BLANK_FOR_ABOUT_SCHEME,

    /** HtmlAllCollection.item(int) requires int parameter. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    HTMLALLCOLLECTION_INTEGER_INDEX,

    /** HtmlAllCollection.namedItem returns null instead of undefined if an element was not found. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    HTMLALLCOLLECTION_NULL_IF_NAMED_ITEM_NOT_FOUND,

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

    /** HtmlCollection returns null instead of undefined if an element was not found. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    HTMLCOLLECTION_NULL_IF_NOT_FOUND,

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

    /** Allows invalid 'align' values. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    HTMLELEMENT_ALIGN_INVALID,

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

    /** Mark the image as invisible if no src attribute defined. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    HTMLIMAGE_INVISIBLE_NO_SRC,

    /** Clicking an image input submits the value as param if defined. */
    @BrowserFeature({CHROME, EDGE})
    HTMLIMAGE_NAME_VALUE_PARAMS,

    /** HTMLInputElement: minlength and maxlength attributes are supported. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    HTMLINPUT_ATTRIBUTE_MIN_MAX_LENGTH_SUPPORTED,

    /** When clicking a {@code checkbox} or {@code radio} input the surrounding anchor is not clicked. */
    @BrowserFeature({CHROME, EDGE})
    HTMLINPUT_CHECKBOX_DOES_NOT_CLICK_SURROUNDING_ANCHOR,

    /** HTMLInputElement: type {@code file} selectionSart/End are null. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    HTMLINPUT_FILE_SELECTION_START_END_NULL,

    /** HTMLInputElement datetime-local type is supported. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    HTMLINPUT_TYPE_DATETIME_LOCAL_SUPPORTED,

    /** HTMLInputElement date and time types are supported. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    HTMLINPUT_TYPE_DATETIME_SUPPORTED,

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

    /** Removing the selected attribute, de selects the option. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    HTMLOPTION_REMOVE_SELECTED_ATTRIB_DESELECTS,

    /** Trims the value of the type attribute before to verify it. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    HTMLSCRIPT_TRIM_TYPE,

    /** willValidate does not check the readonly property. */
    @BrowserFeature({FF, FF_ESR})
    HTMLSELECT_WILL_VALIDATE_IGNORES_READONLY,

    /** Setting defaultValue updates the value also. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    HTMLTEXTAREA_SET_DEFAULT_VALUE_UPDATES_VALUE,

    /** Should org.htmlunit.javascript.host.html.HTMLTrackElement#isEndTagForbidden(). */
    @BrowserFeature({FF, FF_ESR})
    HTMLTRACK_END_TAG_FORBIDDEN,

    /** HTML attributes are always lower case. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    HTML_ATTRIBUTE_LOWER_CASE,

    /** HTML parser supports the 'command' tag. */
    @BrowserFeature({CHROME, EDGE})
    HTML_COMMAND_TAG,

    /** HTML parser supports the 'isindex' tag. */
    @BrowserFeature(IE)
    HTML_ISINDEX_TAG,

    /** HTML parser supports the 'main' tag. */
    @BrowserFeature(IE)
    HTML_MAIN_TAG,

    /** Supports &lt;object&gt; {@code classid} attribute. */
    @BrowserFeature(IE)
    HTML_OBJECT_CLASSID,

    /** Additionally support dates in format "d/M/yyyy". */
    @BrowserFeature({FF, FF_ESR})
    HTTP_COOKIE_EXTENDED_DATE_PATTERNS_1,

    /** Dates format pattern 2. */
    @BrowserFeature({CHROME, EDGE})
    HTTP_COOKIE_EXTENDED_DATE_PATTERNS_2,

    /** domain '.org' is handled as 'org'. */
    @BrowserFeature({FF, FF_ESR})
    HTTP_COOKIE_REMOVE_DOT_FROM_ROOT_DOMAINS,

    /** Indicates that the start date for two digits cookies is 1970
     * instead of 2000 (Two digits years are interpreted as 20xx
     * if before 1970 and as 19xx otherwise).
     */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    HTTP_COOKIE_START_DATE_1970,

    /** Browser sends Sec-ch headers. */
    @BrowserFeature({CHROME, EDGE})
    HTTP_HEADER_CH_UA,

    /** Browser sends Sec-Fetch headers. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    HTTP_HEADER_SEC_FETCH,

    /** Browser sends Upgrade-Insecure-Requests header. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    HTTP_HEADER_UPGRADE_INSECURE_REQUEST,

    /** Setting the property align to arbitrary values is allowed. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_ALIGN_ACCEPTS_ARBITRARY_VALUES,

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

    /** The anchor pathname property returns nothing for broken http(s) url's. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_ANCHOR_PATHNAME_NONE_FOR_BROKEN_URL,

    /** The anchor pathname prefixes file url's with '/'. */
    @BrowserFeature({CHROME, EDGE})
    JS_ANCHOR_PATHNAME_PREFIX_WIN_DRIVES_URL,

    /** The anchor protocol property returns ':' for broken http(s) url's. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_ANCHOR_PROTOCOL_COLON_FOR_BROKEN_URL,

    /** The anchor protocol property converts drive letters to uppercase. */
    @BrowserFeature({CHROME, EDGE})
    JS_ANCHOR_PROTOCOL_COLON_UPPER_CASE_DRIVE_LETTERS,

    /** The anchor protocol property returns 'http' for broken http(s) url's. */
    @BrowserFeature({FF, FF_ESR})
    JS_ANCHOR_PROTOCOL_HTTP_FOR_BROKEN_URL,

    /**
     * Javascript fetch api is supported.
     */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_API_FETCH,

    /** An area element without a href attribute is focusable. */
    @BrowserFeature({FF, FF_ESR})
    JS_AREA_WITHOUT_HREF_FOCUSABLE,

    /** Indicates that "someFunction.arguments" is a read-only view of the function's argument. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_ARGUMENTS_READ_ONLY_ACCESSED_FROM_FUNCTION,

    /** Indicates that Array.from() is supported. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_ARRAY_FROM,

    /** firstChild and lastChild returns null for Attr (like IE does). */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_ATTR_FIRST_LAST_CHILD_RETURNS_NULL,

    /** AudioProcessingEvent ctor is callable. */
    @BrowserFeature({CHROME, EDGE})
    JS_AUDIO_PROCESSING_EVENT_CTOR,

    /** HTMLBGSoundElement reported as HTMLUnknownElement. */
    @BrowserFeature({FF, FF_ESR})
    JS_BGSOUND_AS_UNKNOWN,

    /** BlobEvent ctor requires a data value. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_BLOB_EVENT_REQUIRES_DATA,

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

    /** Console has timeStamp() method. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_CONSOLE_TIMESTAMP,

    /** item is enumerated before length property of CSSRuleList. */
    @BrowserFeature({FF, FF_ESR})
    JS_CSSRULELIST_ENUM_ITEM_LENGTH,

    /** <code>Date.toLocaleDateString()</code> returns a short form (d.M.yyyy). */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DATE_LOCALE_DATE_SHORT,

    /** Indicates that document.createAttribute converts the local name to lowercase. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOCUMENT_CREATE_ATTRUBUTE_LOWER_CASE,

    /** Javascript document.evaluate creates a new result object even if provided as param. */
    @BrowserFeature({CHROME, EDGE})
    JS_DOCUMENT_EVALUATE_RECREATES_RESULT,

    /** The browser has selection {@code rangeCount}. */
    @BrowserFeature({FF, FF_ESR})
    JS_DOCUMENT_SELECTION_RANGE_COUNT,

    /** If document.implementation.hasFeature() supports 'Core 1.0'. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMIMPLEMENTATION_FEATURE_CORE_3,

    /** If document.implementation.hasFeature() supports 'CSS2 1.0'. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMIMPLEMENTATION_FEATURE_CSS2_1,

    /** If document.implementation.hasFeature() supports 'CSS2 3.0'. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMIMPLEMENTATION_FEATURE_CSS2_3,

    /** If document.implementation.hasFeature() supports 'CSS3 1.0'. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMIMPLEMENTATION_FEATURE_CSS3_1,

    /** If document.implementation.hasFeature() supports 'CSS3 2.0'. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMIMPLEMENTATION_FEATURE_CSS3_2,

    /** If document.implementation.hasFeature() supports 'CSS3 3.0'. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMIMPLEMENTATION_FEATURE_CSS3_3,

    /** If document.implementation.hasFeature() supports 'CSS 1.0'. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMIMPLEMENTATION_FEATURE_CSS_1,

    /** If document.implementation.hasFeature() supports 'CSS 2.0'. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMIMPLEMENTATION_FEATURE_CSS_2,

    /** If document.implementation.hasFeature() supports 'CSS 3.0'. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMIMPLEMENTATION_FEATURE_CSS_3,

    /** If document.implementation.hasFeature() supports 'Events 1.0'. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMIMPLEMENTATION_FEATURE_EVENTS_1,

    /** If document.implementation.hasFeature() supports 'KeyboardEvents'. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMIMPLEMENTATION_FEATURE_KEYBOARDEVENTS,

    /** If document.implementation.hasFeature() supports 'LS'. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMIMPLEMENTATION_FEATURE_LS,

    /** If document.implementation.hasFeature() supports 'MutationNameEvents'. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMIMPLEMENTATION_FEATURE_MUTATIONNAMEEVENTS,

    /** If document.implementation.hasFeature() supports 'Range 1.0'. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMIMPLEMENTATION_FEATURE_RANGE_1,

    /** If document.implementation.hasFeature() supports 'Range 3.0'. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMIMPLEMENTATION_FEATURE_RANGE_3,

    /** If document.implementation.hasFeature() supports 'StyleSheets 2.0'. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMIMPLEMENTATION_FEATURE_STYLESHEETS,

    /** If document.implementation.hasFeature() supports 'http://www.w3.org/TR/SVG11/feature#BasicStructure 1.2'. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMIMPLEMENTATION_FEATURE_SVG_BASICSTRUCTURE_1_2,

    /** If document.implementation.hasFeature() supports 'TextEvents'. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMIMPLEMENTATION_FEATURE_TEXTEVENTS,

    /** If document.implementation.hasFeature() supports 'UIEvents 2.0'. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMIMPLEMENTATION_FEATURE_UIEVENTS_2,

    /** If document.implementation.hasFeature() supports 'Validation'. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMIMPLEMENTATION_FEATURE_VALIDATION,

    /** If document.implementation.hasFeature() supports 'Views 1.0'. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMIMPLEMENTATION_FEATURE_VIEWS_1,

    /** If document.implementation.hasFeature() supports 'Views 3.0'. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMIMPLEMENTATION_FEATURE_VIEWS_3,

    /** If document.implementation.hasFeature() supports 'XPath 3.0'. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMIMPLEMENTATION_FEATURE_XPATH,

    /** <code>DOMParser.parseFromString(..)</code> handles an empty String as error. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMPARSER_EMPTY_STRING_IS_ERROR,

    /** {@code DOMParser.parseFromString(..)} creates a document containing a {@code parsererror} element. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMPARSER_PARSERERROR_ON_ERROR,

    /** DOMTokenList returns false instead of throwing an exception when receiver is blank. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMTOKENLIST_CONTAINS_RETURNS_FALSE_FOR_BLANK,

    /** DOMTokenList ignores duplicates when determining the length. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMTOKENLIST_LENGTH_IGNORES_DUPLICATES,

    /** DOMTokenList removed all whitespace chars during add. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMTOKENLIST_REMOVE_WHITESPACE_CHARS_ON_ADD,

    /** DOMTokenList removed all whitespace chars during remove. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_DOMTOKENLIST_REMOVE_WHITESPACE_CHARS_ON_REMOVE,

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
    @BrowserFeature({FF, FF_ESR})
    JS_EVENT_LOAD_SUPPRESSED_BY_CONTENT_SECURIRY_POLICY,

    /** Whether {@code FileReader} includes content type or not. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_FILEREADER_CONTENT_TYPE,

    /** Indicates that the action property will not be expanded if defined as empty string. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_FORM_ACTION_EXPANDURL_NOT_DEFINED,

    /** form.dispatchEvent(e) submits the form if the event is of type 'submit'. */
    @BrowserFeature({FF, FF_ESR})
    JS_FORM_DISPATCHEVENT_SUBMITS,

    /** Support for document.formName('inputName'). */
    @BrowserFeature(IE)
    JS_FORM_USABLE_AS_FUNCTION,

    /** Supports globalThis. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_GLOBAL_THIS,

    /** The index parameter of {@link CSSGroupingRule#insertRule(String, Object)} is optional. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_GROUPINGRULE_INSERTRULE_INDEX_OPTIONAL,

    /** HTMLElement instead of HTMLUnknownElement for elements with hyphen ('-'). */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_HTML_HYPHEN_ELEMENT_CLASS_NAME,

    /** HTMLObject Validity isValid ignores custom error property. */
    @BrowserFeature({CHROME, EDGE})
    JS_HTML_OBJECT_VALIDITYSTATE_ISVALID_IGNORES_CUSTOM_ERROR,

    /** HTMLElement instead of HTMLUnknownElement for ruby elements. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_HTML_RUBY_ELEMENT_CLASS_NAME,

    /** Executes the {@code onload} handler, regardless of the whether the element was already attached to the page. */
    @BrowserFeature({FF, FF_ESR})
    JS_IFRAME_ALWAYS_EXECUTE_ONLOAD,

    /**
     * The complete property returns also true, if the image download was failing
     * or if there was no src at all.
     */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_IMAGE_COMPLETE_RETURNS_TRUE_FOR_NO_REQUEST,

    /**
     * Getting the width and height of an image tag with an empty source returns 0x0.
     */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_IMAGE_WIDTH_HEIGHT_EMPTY_SOURCE_RETURNS_0x0,

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

    /** Indicates that innerText setter supports null values. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_INNER_TEXT_VALUE_NULL,

    /** The value is ignored when the type of an week/month input is changed. */
    @BrowserFeature({CHROME, EDGE})
    JS_INPUT_CHANGE_TYPE_DROPS_VALUE_WEEK_MONTH,

    /** Ignore negative selection starts. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_INPUT_IGNORE_NEGATIVE_SELECTION_START,

    /** FF accepts all chars. */
    @BrowserFeature({FF, FF_ESR})
    JS_INPUT_NUMBER_ACCEPT_ALL,

    /** FF comma at end is not an integer. */
    @BrowserFeature({FF, FF_ESR})
    JS_INPUT_NUMBER_DOT_AT_END_IS_DOUBLE,

    /** Chrome/FF returns null for selectionStart/selectionEnd. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_INPUT_NUMBER_SELECTION_START_END_NULL,

    /** Setting the value of an Input Date will check for correct format. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_INPUT_SET_VALUE_DATE_SUPPORTED,

    /** Setting the value of an Input Email to blank will result in an empty value. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_INPUT_SET_VALUE_EMAIL_TRIMMED,

    /** Setting the value of an Input URL to blank will result in an empty value. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_INPUT_URL_VALUE_TRIMMED,

    /** Indicates that Intl.v8BreakIterator is supported. */
    @BrowserFeature({CHROME, EDGE})
    JS_INTL_V8_BREAK_ITERATOR,

    /** Indicates that isSearchProviderInstalled returns zero instead of undefined. */
    @BrowserFeature({CHROME, EDGE})
    JS_IS_SEARCH_PROVIDER_INSTALLED_ZERO,

    /** location.hash returns an encoded hash. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_LOCATION_HASH_HASH_IS_ENCODED,

    /**
     * Set this property if the browser evaluates<br>
     * window.location.hash to #&uuml; (like Firefox)<br>
     * for url 'http://localhost/something/#%C3%BC'.<br>
     * IE evaluates to #%C3%BC.
     */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_LOCATION_HASH_IS_DECODED,

    /**
     * Set this property if the browser evaluates<br>
     * window.location.hash to #%C3%BC; (like Firefox)<br>
     * for url 'http://localhost/something/#&uuml;'.<br>
     * IE evaluates to #&uuml;.
     */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_LOCATION_HREF_HASH_IS_ENCODED,

    /** Reload sends a referrer header. */
    @BrowserFeature({CHROME, EDGE})
    JS_LOCATION_RELOAD_REFERRER,

    /** Indicates that an empty media list is represented by the string ''. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_MEDIA_LIST_EMPTY_STRING,

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

    /** Indicates that Object.assign() is supported. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_OBJECT_ASSIGN,

    /** Indicates that Object.getOwnPropertySymbols() is supported. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_OBJECT_GET_OWN_PROPERTY_SYMBOLS,

    /** Indicates that someObj.offsetParent returns null, it someObj has fixed style. */
    @BrowserFeature({CHROME, EDGE})
    JS_OFFSET_PARENT_NULL_IF_FIXED,

    /** element.outerHTML removes all children from detached node. */
    @BrowserFeature({CHROME, EDGE})
    JS_OUTER_HTML_THROWS_FOR_DETACHED,

    /** Indicates that HTMLPhraseElements returning 'HTMLElement'
     * as class name. */
    @BrowserFeature({FF, FF_ESR})
    JS_PHRASE_COMMON_CLASS_NAME,

    /** Supports Promise. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_PROMISE,

    /** Indicates that the {@code Object.getOwnPropertyDescriptor.get} contains name. */
    @BrowserFeature({FF, FF_ESR})
    JS_PROPERTY_DESCRIPTOR_NAME,

    /** Support {@code Reflect}. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_REFLECT,

    /** Javascript selectorText property returns selectors in lower case. */
    @BrowserFeature({CHROME, EDGE})
    JS_SELECTOR_TEXT_LOWERCASE,

    /** Indicates that setting the value to null has no effect. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_SELECT_FILE_THROWS,

    /** Ignore negative value when setting the length. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_SELECT_OPTIONS_IGNORE_NEGATIVE_LENGTH,

    /** Indicates that select.options.remove ignores the call if index is too large. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_SELECT_OPTIONS_REMOVE_IGNORE_IF_INDEX_NEGATIVE,

    /** Indicates that select.options.remove ignores the call if index is too large. */
    @BrowserFeature({FF, FF_ESR})
    JS_SELECT_REMOVE_IGNORE_IF_INDEX_OUTSIDE,

    /** Whether to add to the storage even preserved words. */
    @BrowserFeature({FF, FF_ESR})
    JS_STORAGE_PRESERVED_INCLUDED,

    /** Indicates that string.includes() is supported. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_STRING_INCLUDES,

    /** Indicates that string.startsWith() and .endWith() are supported. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_STRING_REPEAT,

    /** Indicates that string.startsWith() and .endWith() are supported. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_STRING_STARTS_ENDS_WITH,

    /** Indicates that string.trimLeft() and .trimRight() are supported. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_STRING_TRIM_LEFT_RIGHT,

    /** Stylesheet list contains only active style sheets. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_STYLESHEETLIST_ACTIVE_ONLY,

    /** Indicates wordSpacing support percent values. */
    @BrowserFeature({FF, FF_ESR})
    JS_STYLE_WORD_SPACING_ACCEPTS_PERCENT,

    /** Indicates that trying to access the style property with a wrong index returns undefined
     * instead of "". */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_STYLE_WRONG_INDEX_RETURNS_UNDEFINED,

    /** Supports Symbol. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_SYMBOL,

    /** The width column property has a value of 'null' for null. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_TABLE_COLUMN_WIDTH_NULL_STRING,

    /** Calling deleteCell without an index throws an exception. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_TABLE_ROW_DELETE_CELL_REQUIRES_INDEX,

    /** Set span zo zero if provided value is invalid. */
    @BrowserFeature({CHROME, EDGE})
    JS_TABLE_SPAN_SET_ZERO_IF_INVALID,

    /** Setting the property {@code maxLength} throws an exception, if the provided value is less than 0. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_TEXT_AREA_SET_MAXLENGTH_NEGATIVE_THROWS_EXCEPTION,

    /** Setting the value processes null as null value. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_TEXT_AREA_SET_VALUE_NULL,

    /** Indicates that <code>TreeWalker.expandEntityReferences</code> is always {@code false}. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_TREEWALKER_EXPAND_ENTITY_REFERENCES_FALSE,

    /** Setting the property align to arbitrary values is allowed. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_TYPE_ACCEPTS_ARBITRARY_VALUES,

    /** Indicates that WeakSet is supported. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_WEAK_SET,

    /** Setting the property width/height to arbitrary values is allowed. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_WIDTH_HEIGHT_ACCEPTS_ARBITRARY_VALUES,

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
    JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_138,

    /**
     * Difference of window.outer/inner height is 91.
     */
    @BrowserFeature({FF, FF_ESR})
    JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_91,

    /** Window.getSelection returns null, if the window is not visible. */
    @BrowserFeature({FF, FF_ESR})
    JS_WINDOW_SELECTION_NULL_IF_INVISIBLE,

    /** Supports XML. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_XML,

    /** XMLDocument: .getElementById() to return any element, not HTML specifically. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    JS_XML_GET_ELEMENT_BY_ID__ANY_ELEMENT,

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

    /** The default display style of slot is 'content'. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    SLOT_CONTENTS,

    /**
     * Indicates, that the pathname for the url 'blank' is empty;
     * instead of 'blank'.
     */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    URL_ABOUT_BLANK_HAS_BLANK_PATH,

    /**
     * Indicates, that the browser supports username and password as
     * part of the url (e.g. http://john.smith:secret@localhost).
     */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    URL_AUTH_CREDENTIALS,

    /** The protocol setter does not check for special protocols. */
    @BrowserFeature(FF_ESR)
    URL_IGNORE_SPECIAL,

    /** Handles missing slashes. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    URL_MISSING_SLASHES,

    /** Set the origin property for web socket events. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    WEBSOCKET_ORIGIN_SET,

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

    /** Indicates if an empty url is allowed as url param for the open method. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    XHR_OPEN_ALLOW_EMTPY_URL,

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

    /** Indicates that the content charset is used for response parsing. */
    @BrowserFeature({CHROME, EDGE, FF, FF_ESR})
    XHR_USE_CONTENT_CHARSET,
}
