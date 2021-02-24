/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.event;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import java.util.LinkedList;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * JavaScript object representing an event that is passed into event handlers when they are
 * invoked. For general information on which properties and functions should be supported,
 * see <a href="https://developer.mozilla.org/en-US/docs/DOM/event">the mozilla docs</a>,
 * <a href="http://www.w3.org/TR/DOM-Level-2-Events/events.html#Events-Event">the W3C DOM
 * Level 2 Event Documentation</a> or <a href="http://msdn2.microsoft.com/en-us/library/aa703876.aspx">IE's
 * IHTMLEventObj interface</a>.
 *
 * @author <a href="mailto:chriseldredge@comcast.net">Chris Eldredge</a>
 * @author Mike Bowler
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Brad Murray
 * @author Ahmed Ashour
 * @author Rob Di Marco
 * @author Ronald Brill
 * @author Frank Danek
 * @author Atsushi Nakagawa
 * @author Thorsten Wendelmuth
 */
@JsxClass
public class Event extends SimpleScriptable {

    /**
     * Key to place the event's target in the Context's scope during event processing
     * to compute node coordinates compatible with those of the event.
     */
    protected static final String KEY_CURRENT_EVENT = "Event#current";

    /** The submit event type, triggered by {@code onsubmit} event handlers. */
    public static final String TYPE_SUBMIT = "submit";

    /** The change event type, triggered by {@code onchange} event handlers. */
    public static final String TYPE_CHANGE = "change";

    /** The load event type, triggered by {@code onload} event handlers. */
    public static final String TYPE_LOAD = "load";

    /** The unload event type, triggered by {@code onunload} event handlers. */
    public static final String TYPE_UNLOAD = "unload";

    /** The popstate event type, triggered by {@code onpopstate} event handlers. */
    public static final String TYPE_POPSTATE = "popstate";

    /** The focus event type, triggered by {@code onfocus} event handlers. */
    public static final String TYPE_FOCUS = "focus";

    /** The focus in event type, triggered by {@code onfocusin} event handlers. */
    public static final String TYPE_FOCUS_IN = "focusin";

    /** The focus out event type, triggered by {@code onfocusout} event handlers. */
    public static final String TYPE_FOCUS_OUT = "focusout";

    /** The blur event type, triggered by {@code onblur} event handlers. */
    public static final String TYPE_BLUR = "blur";

    /** The key down event type, triggered by {@code onkeydown} event handlers. */
    public static final String TYPE_KEY_DOWN = "keydown";

    /** The key down event type, triggered by {@code onkeypress} event handlers. */
    public static final String TYPE_KEY_PRESS = "keypress";

    /** The input event type, triggered by {@code oninput} event handlers. */
    public static final String TYPE_INPUT = "input";

    /** The key down event type, triggered by {@code onkeyup} event handlers. */
    public static final String TYPE_KEY_UP = "keyup";

    /** The submit event type, triggered by {@code onreset} event handlers. */
    public static final String TYPE_RESET = "reset";

    /** The beforeunload event type, triggered by {@code onbeforeunload} event handlers. */
    public static final String TYPE_BEFORE_UNLOAD = "beforeunload";

    /** Triggered after the DOM has loaded but before images etc. */
    public static final String TYPE_DOM_DOCUMENT_LOADED = "DOMContentLoaded";

    /** The event type triggered by {@code onpropertychange} event handlers. */
    public static final String TYPE_PROPERTY_CHANGE = "propertychange";

    /** The event type triggered by {@code onhashchange} event handlers. */
    public static final String TYPE_HASH_CHANGE = "hashchange";

    /** The event type triggered by {@code onreadystatechange} event handlers. */
    public static final String TYPE_READY_STATE_CHANGE = "readystatechange";

    /** The event type triggered by {@code onerror} event handlers. */
    public static final String TYPE_ERROR = "error";

    /** The message event type, triggered by postMessage. */
    public static final String TYPE_MESSAGE = "message";

    /** The close event type, triggered by {@code onclose} event handlers. */
    public static final String TYPE_CLOSE = "close";

    /** The open event type, triggered by {@code onopen} event handlers. */
    public static final String TYPE_OPEN = "open";

    /** The load start event type, triggered by {@code onloadstart} event handlers. */
    public static final String TYPE_LOAD_START = "loadstart";

    /** The load end event type, triggered by {@code onloadend} event handlers. */
    public static final String TYPE_LOAD_END = "loadend";

    /** The progress event type, triggered by {@code onprogress} event handlers. */
    public static final String TYPE_PROGRESS = "progress";

    /** The abort event type, triggered by {@code onabort} event handlers. */
    public static final String TYPE_ABORT = "abort";

    /** The timeout event type, triggered by {@code ontimeout} event handlers. */
    public static final String TYPE_TIMEOUT = "timeout";

    /** The scroll event type, triggered by {@code onscroll} event handlers. */
    public static final String TYPE_SCROLL = "scroll";

    /** The search event type, triggered by {@code onsearch} event handlers. */
    public static final String TYPE_SEARCH = "search";

    /** The dragend event type, triggered by {@code dragend} event handlers. */
    public static final String TYPE_DRAGEND = "dragend";

    /** The invalid event type, triggered by {@code invalid} event handlers. */
    public static final String TYPE_INVALID = "invalid";

    /** The pointerout event type, triggered by {@code pointerout} event handlers. */
    public static final String TYPE_POINTEROUT = "pointerout";

    /** The help event type, triggered by {@code help} event handlers. */
    public static final String TYPE_HELP = "help";

    /** The ratechange event type, triggered by {@code ratechange} event handlers. */
    public static final String TYPE_RATECHANGE = "ratechange";

    /** The animationiteration event type, triggered by {@code animationiteration} event handlers. */
    public static final String TYPE_ANIMATIONITERATION = "animationiteration";

    /** The canplaythrough event type, triggered by {@code canplaythrough} event handlers. */
    public static final String TYPE_CANPLAYTHROUGH = "canplaythrough";

    /** The cancel event type, triggered by {@code cancel} event handlers. */
    public static final String TYPE_CANCEL = "cancel";

    /** The pointerenter event type, triggered by {@code pointerenter} event handlers. */
    public static final String TYPE_POINTERENTER = "pointerenter";

    /** The select event type, triggered by {@code select} event handlers. */
    public static final String TYPE_SELECT = "select";

    /** The auxclick event type, triggered by {@code auxclick} event handlers. */
    public static final String TYPE_AUXCLICK = "auxclick";

    /** The mspointerleave event type, triggered by {@code mspointerleave} event handlers. */
    public static final String TYPE_MSPOINTERLEAVE = "mspointerleave";

    /** The webkitanimationstart event type, triggered by {@code webkitanimationstart} event handlers. */
    public static final String TYPE_WEBANIMATIONSTART = "webkitanimationstart";

    /** The msgesturestart event type, triggered by {@code msgesturestart} event handlers. */
    public static final String TYPE_MSGESTURESTART = "msgesturestart";

    /** The deviceproximity event type, triggered by {@code deviceproximity} event handlers. */
    public static final String TYPE_DEVICEPROXIMITY = "deviceproximity";

    /** The drag event type, triggered by {@code drag} event handlers. */
    public static final String TYPE_DRAG = "drag";

    /** The seeked event type, triggered by {@code seeked} event handlers. */
    public static final String TYPE_SEEKED = "seeked";

    /** The offline event type, triggered by {@code offline} event handlers. */
    public static final String TYPE_OFFLINE = "offline";

    /** The deviceorientation event type, triggered by {@code deviceorientation} event handlers. */
    public static final String TYPE_DEVICEORIENTATION = "deviceorientation";

    /** The toggle event type, triggered by {@code toggle} event handlers. */
    public static final String TYPE_TOGGLE = "toggle";

    /** The play event type, triggered by {@code play} event handlers. */
    public static final String TYPE_PLAY = "play";

    /** The mspointerover event type, triggered by {@code mspointerover} event handlers. */
    public static final String TYPE_MSPOINTEROVER = "mspointerover";

    /** The mspointerup event type, triggered by {@code mspointerup} event handlers. */
    public static final String TYPE_MSPOINTERUP = "mspointerup";

    /** The loadedmetadata event type, triggered by {@code loadedmetadata} event handlers. */
    public static final String TYPE_LOADEDMETADATA = "loadedmetadata";

    /** The msinertiastart event type, triggered by {@code msinertiastart} event handlers. */
    public static final String TYPE_MSINERTIASTART = "msinertiastart";

    /** The pointermove event type, triggered by {@code pointermove} event handlers. */
    public static final String TYPE_POINTERMOVE = "pointermove";

    /** The mspointermove event type, triggered by {@code mspointermove} event handlers. */
    public static final String TYPE_MSPOINTERMOVE = "mspointermove";

    /** The userproximity event type, triggered by {@code userproximity} event handlers. */
    public static final String TYPE_USERPROXIMITY = "userproximity";

    /** The lostpointercapture event type, triggered by {@code lostpointercapture} event handlers. */
    public static final String TYPE_LOSTPOINTERCAPTURE = "lostpointercapture";

    /** The pointerover event type, triggered by {@code pointerover} event handlers. */
    public static final String TYPE_POINTEROVER = "pointerover";

    /** The animationcancel event type, triggered by {@code animationcancel} event handlers. */
    public static final String TYPE_ANIMATIONCANCEL = "animationcancel";

    /** The animationend event type, triggered by {@code animationend} event handlers. */
    public static final String TYPE_ANIMATIONEND = "animationend";

    /** The dragenter event type, triggered by {@code dragenter} event handlers. */
    public static final String TYPE_DRAGENTER = "dragenter";

    /** The afterprint event type, triggered by {@code afterprint} event handlers. */
    public static final String TYPE_AFTERPRINT = "afterprint";

    /** The mozfullscreenerror event type, triggered by {@code mozfullscreenerror} event handlers. */
    public static final String TYPE_MOZFULLSCREENERROR = "mozfullscreenerror";

    /** The mouseleave event type, triggered by {@code mouseleave} event handlers. */
    public static final String TYPE_MOUSELEAVE = "mouseleave";

    /** The mousewheel event type, triggered by {@code mousewheel} event handlers. */
    public static final String TYPE_MOUSEWHEEL = "mousewheel";

    /** The seeking event type, triggered by {@code seeking} event handlers. */
    public static final String TYPE_SEEKING = "seeking";

    /** The cuechange event type, triggered by {@code cuechange} event handlers. */
    public static final String TYPE_CUECHANGE = "cuechange";

    /** The pageshow event type, triggered by {@code pageshow} event handlers. */
    public static final String TYPE_PAGESHOW = "pageshow";

    /** The mspointerenter event type, triggered by {@code mspointerenter} event handlers. */
    public static final String TYPE_MSPOINTENTER = "mspointerenter";

    /** The mozfullscreenchange event type, triggered by {@code mozfullscreenchange} event handlers. */
    public static final String TYPE_MOZFULLSCREENCHANGE = "mozfullscreenchange";

    /** The durationchange event type, triggered by {@code durationchange} event handlers. */
    public static final String TYPE_DURATIONCHANGE = "durationchange";

    /** The playing event type, triggered by {@code playing} event handlers. */
    public static final String TYPE_PLAYNG = "playing";

    /** The ended event type, triggered by {@code ended} event handlers. */
    public static final String TYPE_ENDED = "ended";

    /** The loadeddata event type, triggered by {@code loadeddata} event handlers. */
    public static final String TYPE_LOADEDDATA = "loadeddata";

    /** The unhandledrejection event type, triggered by {@code unhandledrejection} event handlers. */
    public static final String TYPE_UNHANDLEDREJECTION = "unhandledrejection";

    /** The suspend event type, triggered by {@code suspend} event handlers. */
    public static final String TYPE_SUSPEND = "suspend";

    /** The waiting event type, triggered by {@code waiting} event handlers. */
    public static final String TYPE_WAITING = "waiting";

    /** The canplay event type, triggered by {@code canplay} event handlers. */
    public static final String TYPE_CANPLAY = "canplay";

    /** The languagechange event type, triggered by {@code languagechange} event handlers. */
    public static final String TYPE_LANGUAGECHANGE = "languagechange";

    /** The emptied event type, triggered by {@code emptied} event handlers. */
    public static final String TYPE_EMPTIED = "emptied";

    /** The rejectionhandled event type, triggered by {@code rejectionhandled} event handlers. */
    public static final String TYPE_REJECTIONHANDLED = "rejectionhandled";

    /** The pointercancel event type, triggered by {@code pointercancel} event handlers. */
    public static final String TYPE_POINTERCANCEL = "pointercancel";

    /** The msgestureend event type, triggered by {@code msgestureend} event handlers. */
    public static final String TYPE_MSGESTUREEND = "msgestureend";

    /** The resize event type, triggered by {@code resize} event handlers. */
    public static final String TYPE_RESIZE = "resize";

    /** The pause event type, triggered by {@code pause} event handlers. */
    public static final String TYPE_PAUSE = "pause";

    /** The pointerup event type, triggered by {@code pointerup} event handlers. */
    public static final String TYPE_POINTERUP = "pointerup";

    /** The wheel event type, triggered by {@code wheel} event handlers. */
    public static final String TYPE_WHEEL = "wheel";

    /** The mspointerdown event type, triggered by {@code mspointerdown} event handlers. */
    public static final String TYPE_MSPOINTERDOWN = "mspointerdown";

    /** The pointerleave event type, triggered by {@code pointerleave} event handlers. */
    public static final String TYPE_POINTERLEAVE = "pointerleave";

    /** The beforeprint event type, triggered by {@code beforeprint} event handlers. */
    public static final String TYPE_BEFOREPRINT = "beforeprint";

    /** The storage event type, triggered by {@code storage} event handlers. */
    public static final String TYPE_STORAGE = "storage";

    /** The devicelight event type, triggered by {@code devicelight} event handlers. */
    public static final String TYPE_DEVICELIGHT = "devicelight";

    /** The animationstart event type, triggered by {@code animationstart} event handlers. */
    public static final String TYPE_ANIMATIONSTART = "animationstart";

    /** The mspointercancel event type, triggered by {@code mspointercancel} event handlers. */
    public static final String TYPE_MSPOINTERCANCEL = "mspointercancel";

    /** The timeupdate event type, triggered by {@code timeupdate} event handlers. */
    public static final String TYPE_TIMEUPDATE = "timeupdate";

    /** The pagehide event type, triggered by {@code pagehide} event handlers. */
    public static final String TYPE_PAGEHIDE = "pagehide";

    /** The webkitanimationiteration event type, triggered by {@code webkitanimationiteration} event handlers. */
    public static final String TYPE_WEBKITANIMATIONITERATION = "webkitanimationiteration";

    /** The msgesturetap event type, triggered by {@code msgesturetap} event handlers. */
    public static final String TYPE_MSGESTURETAP = "msgesturetap";

    /** The dragover event type, triggered by {@code dragover} event handlers. */
    public static final String TYPE_DRAGOVER = "dragover";

    /** The online event type, triggered by {@code online} event handlers. */
    public static final String TYPE_ONLINE = "online";

    /** The msgesturedoubletap event type, triggered by {@code msgesturedoubletap} event handlers. */
    public static final String TYPE_MSGESTUREDOUBLETAP = "msgesturedoubletap";

    /** The show event type, triggered by {@code show} event handlers. */
    public static final String TYPE_SHOW = "show";

    /** The volumechange event type, triggered by {@code volumechange} event handlers. */
    public static final String TYPE_VOLUMECHANGE = "volumechange";

    /** The msgesturechange event type, triggered by {@code msgesturechange} event handlers. */
    public static final String TYPE_MSGESTURECHANGE = "msgesturechange";

    /** The gotpointercapture event type, triggered by {@code gotpointercapture} event handlers. */
    public static final String TYPE_GOTPOINTERCAPTURE = "gotpointercapture";

    /** The webkittransitionend event type, triggered by {@code webkittransitionend} event handlers. */
    public static final String TYPE_WEBKITTRANSITIONEND = "webkittransitionend";

    /** The webkitanimationend event type, triggered by {@code webkitanimationend} event handlers. */
    public static final String TYPE_WEBKITANIMATIONEND = "webkitanimationend";

    /** The mspointerout event type, triggered by {@code mspointerout} event handlers. */
    public static final String TYPE_MSPOINTEROUT = "mspointerout";

    /** The devicemotion event type, triggered by {@code devicemotion} event handlers. */
    public static final String TYPE_DEVICEMOTION = "devicemotion";

    /** The stalled event type, triggered by {@code stalled} event handlers. */
    public static final String TYPE_STALLED = "stalled";

    /** The mouseenter event type, triggered by {@code mouseenter} event handlers. */
    public static final String TYPE_MOUDEENTER = "mouseenter";

    /** The dragleave event type, triggered by {@code dragleave} event handlers. */
    public static final String TYPE_DRAGLEAVE = "dragleave";

    /** The pointerdown event type, triggered by {@code pointerdown} event handlers. */
    public static final String TYPE_POINTERDOWN = "pointerdown";

    /** The drop event type, triggered by {@code drop} event handlers. */
    public static final String TYPE_DROP = "drop";

    /** The dragstart event type, triggered by {@code dragstart} event handlers. */
    public static final String TYPE_DRAGSTART = "dragstart";

    /** The transitionend event type, triggered by {@code transitionend} event handlers. */
    public static final String TYPE_TRANSITIONEND = "transitionend";

    /** The msgesturehold event type, triggered by {@code msgesturehold} event handlers. */
    public static final String TYPE_MSGESTUREHOLD = "msgesturehold";

    /** The deviceorientationabsolute event type, triggered by {@code deviceorientationabsolute} event handlers. */
    public static final String TYPE_DEVICEORIENTATIONABSOLUTE = "deviceorientationabsolute";

    /** The beforecopy event type, triggered by {@code beforecopy} event handlers. */
    public static final String TYPE_BEFORECOPY = "beforecopy";

    /** The beforecut event type, triggered by {@code beforecut} event handlers. */
    public static final String TYPE_BEFORECUT = "beforecut";

    /** The beforepaste event type, triggered by {@code beforepaste} event handlers. */
    public static final String TYPE_BEFOREPASTE = "beforepaste";

    /** The selectstart event type, triggered by {@code selectstart} event handlers. */
    public static final String TYPE_SELECTSTART = "selectstart";

    /** The webkitfullscreenchange event type, triggered by {@code webkitfullscreenchange} event handlers. */
    public static final String TYPE_WEBKITFULLSCREENCHANGE = "webkitfullscreenchange";

    /** The webkitfullscreenerror event type, triggered by {@code webkitfullscreenerror} event handlers. */
    public static final String TYPE_WEBKITFULLSCREENERROR = "webkitfullscreenerror";
    /** The copy event type, triggered by {@code copy} event handlers. */
    public static final String TYPE_COPY = "copy";

    /** The cut event type, triggered by {@code cut} event handlers. */
    public static final String TYPE_CUT = "cut";

    /** The paste event type, triggered by {@code paste} event handlers. */
    public static final String TYPE_PASTE = "paste";

    /** The onmessageerror event type, triggered by {@code onmessageerror} event handlers. */
    public static final String TYPE_ONMESSAGEERROR = "onmessageerror";

    /** The stop event type, triggered by {@code stop} event handlers. */
    public static final String TYPE_STOP = "stop";

    /** The msgotpointercapture event type, triggered by {@code msgotpointercapture} event handlers. */
    public static final String TYPE_MSGOTPOINTERCAPTURE = "msgotpointercapture";

    /** The mslostpointercapture event type, triggered by {@code mslostpointercapture} event handlers. */
    public static final String TYPE_MSLOSTPOINTERCAPTURE = "mslostpointercapture";

    /** The activate event type, triggered by {@code activate} event handlers. */
    public static final String TYPE_ACTIVATE = "activate";

    /** The deactivate event type, triggered by {@code deactivate} event handlers. */
    public static final String TYPE_DEACTIVATE = "deactivate";

    /** The mscontentzoom event type, triggered by {@code mscontentzoom} event handlers. */
    public static final String TYPE_MSCONTENTZOOM = "mscontentzoom";

    /** The msmanipulationstatechanged event type, triggered by {@code msmanipulationstatechanged} event handlers. */
    public static final String TYPE_MSMANIPULATIONSTATECHANGED = "msmanipulationstatechanged";

    /** The beforeactivate event type, triggered by {@code beforeactivate} event handlers. */
    public static final String TYPE_BEFOREACTIVATE = "beforeactivate";

    /** The pointerlockchange event type, triggered by {@code pointerlockchange} event handlers. */
    public static final String TYPE_POINTERLOCKCHANGE = "pointerlockchange";

    /** The pointerlockerror event type, triggered by {@code pointerlockerror} event handlers. */
    public static final String TYPE_POINTERLOCKERROR = "pointerlockerror";

    /** The selectionchange event type, triggered by {@code selectionchange} event handlers. */
    public static final String TYPE_SELECTIONCHANGE = "selectionchange";

    /** The afterscriptexecute event type, triggered by {@code afterscriptexecute} event handlers. */
    public static final String TYPE_AFTERSCRIPTEXECUTE = "afterscriptexecute";

    /** The beforescriptexecute event type, triggered by {@code beforescriptexecute} event handlers. */
    public static final String TYPE_BEFORESCRIPTEXECUTE = "beforescriptexecute";

    /** The msfullscreenchange event type, triggered by {@code msfullscreenchange} event handlers. */
    public static final String TYPE_MSFULLSCREENCHANGE = "msfullscreenchange";

    /** The msfullscreenerror event type, triggered by {@code msfullscreenerror} event handlers. */
    public static final String TYPE_MSFULLSCREENERROR = "msfullscreenerror";

    /** The beforedeactivate event type, triggered by {@code beforedeactivate} event handlers. */
    public static final String TYPE_BEFOREDEACTIVATE = "beforedeactivate";

    /** The msthumbnailclick event type, triggered by {@code msthumbnailclick} event handlers. */
    public static final String TYPE_MSTHUMBNAILCLICK = "msthumbnailclick";

    /** The storagecommit event type, triggered by {@code storagecommit} event handlers. */
    public static final String TYPE_STORAGECOMMIT = "storagecommit";

    /** The ontransitioncancel event type, triggered by {@code ontransitioncancel} event handlers. */
    public static final String TYPE_ONTRANSITIONCANCEL = "ontransitioncancel";

    /** The ontransitionend event type, triggered by {@code ontransitionend} event handlers. */
    public static final String TYPE_ONTRANSITIONEND = "ontransitionend";

    /** The ontransitionrun event type, triggered by {@code ontransitionrun} event handlers. */
    public static final String TYPE_ONTRANSITIONRUN = "ontransitionrun";

    /** The ontransitionstart event type, triggered by {@code ontransitionstart} event handlers. */
    public static final String TYPE_ONTRANSITIONSTART = "ontransitionstart";

    /**
     * The mssitemodejumplistitemremoved event type, triggered
     * by {@code mssitemodejumplistitemremoved} event handlers.
     */
    public static final String TYPE_MSSITEMODEJUMPLISTITEMREMOVED = "mssitemodejumplistitemremoved";

    /** No event phase. */
    @JsxConstant({CHROME, EDGE, FF, FF78})
    public static final short NONE = 0;

    /** The first event phase: the capturing phase. */
    @JsxConstant
    public static final short CAPTURING_PHASE = 1;

    /** The second event phase: at the event target. */
    @JsxConstant
    public static final short AT_TARGET = 2;

    /** The third (and final) event phase: the bubbling phase. */
    @JsxConstant
    public static final short BUBBLING_PHASE = 3;

    /** Constant. */
    @JsxConstant({FF, FF78})
    public static final int ALT_MASK = 0x1;

    /** Constant. */
    @JsxConstant({FF, FF78})
    public static final int CONTROL_MASK = 0x2;

    /** Constant. */
    @JsxConstant({FF, FF78})
    public static final int SHIFT_MASK = 0x4;

    /** Constant. */
    @JsxConstant({FF, FF78})
    public static final int META_MASK = 0x8;

    private Object srcElement_;        // IE-only writable equivalent of target.
    private EventTarget target_;       // W3C standard read-only equivalent of srcElement.
    private Scriptable currentTarget_; // Changes during event capturing and bubbling.
    private String type_ = "";         // The event type.
    private int keyCode_;              // Key code for a keypress
    private boolean shiftKey_;         // Exposed here in IE, only in mouse events in FF.
    private boolean ctrlKey_;          // Exposed here in IE, only in mouse events in FF.
    private boolean altKey_;           // Exposed here in IE, only in mouse events in FF.
    private String propertyName_;
    private boolean stopPropagation_;
    private boolean stopImmediatePropagation_;
    private boolean preventDefault_;

    /**
     * The current event phase. This is a W3C standard attribute. One of {@link #NONE},
     * {@link #CAPTURING_PHASE}, {@link #AT_TARGET} or {@link #BUBBLING_PHASE}.
     */
    private short eventPhase_;

    /**
     * Whether or not the event bubbles. The value of this attribute depends on the event type. To
     * determine if a certain event type bubbles, see http://www.w3.org/TR/DOM-Level-2-Events/events.html
     * Most event types do bubble, so this is true by default; event types which do not bubble should
     * overwrite this value in their constructors.
     */
    private boolean bubbles_ = true;

    /**
     * Whether or not the event can be canceled. The value of this attribute depends on the event type. To
     * determine if a certain event type can be canceled, see http://www.w3.org/TR/DOM-Level-2-Events/events.html
     * The more common event types are cancelable, so this is true by default; event types which cannot be
     * canceled should overwrite this value in their constructors.
     */
    private boolean cancelable_ = true;

    /**
     * The time at which the event was created.
     */
    private final long timeStamp_ = System.currentTimeMillis();

    /**
     * Creates a new event instance.
     * @param domNode the DOM node that triggered the event
     * @param type the event type
     */
    public Event(final DomNode domNode, final String type) {
        this((EventTarget) domNode.getScriptableObject(), type);
        setDomNode(domNode, false);
    }

    /**
     * Creates a new event instance.
     * @param target the target
     * @param type the event type
     */
    public Event(final EventTarget target, final String type) {
        srcElement_ = target;
        target_ = target;
        currentTarget_ = target;
        type_ = type;

        setParentScope(target);
        setPrototype(getPrototype(getClass()));

        if (TYPE_CHANGE.equals(type)) {
            cancelable_ = false;
        }
        else if (TYPE_LOAD.equals(type)) {
            bubbles_ = false;
            cancelable_ = false;
        }
        else if (TYPE_ERROR.equals(type)) {
            // https://www.w3.org/TR/DOM-Level-3-Events/#event-type-error
            bubbles_ = false;
        }
        else if (TYPE_FOCUS.equals(type) || TYPE_BLUR.equals(type)) {
            bubbles_ = false;
            cancelable_ = false;
        }
    }

    /**
     * Creates a new Event with {@link #TYPE_PROPERTY_CHANGE} type.
     * @param domNode the DOM node that triggered the event
     * @param propertyName the property name that was changed
     * @return the new Event object
     */
    public static Event createPropertyChangeEvent(final DomNode domNode, final String propertyName) {
        final Event event = new Event(domNode, TYPE_PROPERTY_CHANGE);
        event.propertyName_ = propertyName;
        return event;
    }

    /**
     * Used to build the prototype.
     */
    public Event() {
    }

    /**
     * Called whenever an event is created using <code>Document.createEvent(..)</code>.
     * This method is called after the parent scope was set so you are able to access the browser version.
     */
    public void eventCreated() {
        setBubbles(false);
        setCancelable(false);
    }

    /**
     * JavaScript constructor.
     *
     * @param type the event type
     * @param details the event details (optional)
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public void jsConstructor(final String type, final ScriptableObject details) {
        boolean bubbles = false;
        boolean cancelable = false;

        if (details != null && !Undefined.isUndefined(details)) {
            bubbles = ScriptRuntime.toBoolean(details.get("bubbles"));
            cancelable  = ScriptRuntime.toBoolean(details.get("cancelable"));
        }
        initEvent(type, bubbles, cancelable);
    }

    /**
     * Called when the event starts being fired.
     */
    @SuppressWarnings("unchecked")
    public void startFire() {
        final Context context = Context.getCurrentContext();
        LinkedList<Event> events = (LinkedList<Event>) context.getThreadLocal(KEY_CURRENT_EVENT);
        if (events == null) {
            events = new LinkedList<>();
            context.putThreadLocal(KEY_CURRENT_EVENT, events);
        }
        events.add(this);
    }

    /**
     * Called when the event being fired ends.
     */
    @SuppressWarnings("unchecked")
    public void endFire() {
        ((LinkedList<Event>) Context.getCurrentContext().getThreadLocal(KEY_CURRENT_EVENT)).removeLast();
    }

    /**
     * Returns the object that fired the event.
     * @return the object that fired the event
     */
    @JsxGetter
    public Object getSrcElement() {
        return srcElement_;
    }

    /**
     * Sets the object that fired the event.
     * @param srcElement the object that fired the event
     */
    @JsxSetter(IE)
    public void setSrcElement(final Object srcElement) {
        srcElement_ = srcElement;
    }

    /**
     * Returns the event target to which the event was originally dispatched.
     * @return the event target to which the event was originally dispatched
     */
    @JsxGetter
    public Object getTarget() {
        return target_;
    }

    /**
     * Sets the event target.
     * @param target the event target
     */
    public void setTarget(final EventTarget target) {
        target_ = target;
    }

    /**
     * Returns the event target whose event listeners are currently being processed. This
     * is useful during event capturing and event bubbling.
     * @return the current event target
     */
    @JsxGetter
    public Scriptable getCurrentTarget() {
        return currentTarget_;
    }

    /**
     * Sets the current target.
     * @param target the new value
     */
    public void setCurrentTarget(final Scriptable target) {
        currentTarget_ = target;
    }

    /**
     * Returns the event type.
     * @return the event type
     */
    @JsxGetter
    public String getType() {
        return type_;
    }

    /**
     * Sets the event type.
     * @param type the event type
     */
    @JsxSetter
    public void setType(final String type) {
        type_ = type;
    }

    /**
     * Sets the event type.
     * @param eventType the event type
     */
    public void setEventType(final String eventType) {
        type_ = eventType;
    }

    /**
     * Returns the time at which this event was created.
     * @return the time at which this event was created
     */
    @JsxGetter
    public long getTimeStamp() {
        return timeStamp_;
    }

    /**
     * Sets the key code.
     * @param keyCode the virtual key code value of the key which was depressed, otherwise zero
     */
    protected void setKeyCode(final int keyCode) {
        keyCode_ = keyCode;
    }

    /**
     * Returns the key code associated with the event.
     * @return the key code associated with the event
     */
    public int getKeyCode() {
        return keyCode_;
    }

    /**
     * Returns whether {@code SHIFT} has been pressed during this event or not.
     * @return whether {@code SHIFT} has been pressed during this event or not
     */
    public boolean isShiftKey() {
        return shiftKey_;
    }

    /**
     * Sets whether {@code SHIFT} key is pressed on not.
     * @param shiftKey whether {@code SHIFT} has been pressed during this event or not
     */
    protected void setShiftKey(final boolean shiftKey) {
        shiftKey_ = shiftKey;
    }

    /**
     * Returns whether {@code CTRL} has been pressed during this event or not.
     * @return whether {@code CTRL} has been pressed during this event or not
     */
    public boolean isCtrlKey() {
        return ctrlKey_;
    }

    /**
     * Sets whether {@code CTRL} key is pressed on not.
     * @param ctrlKey whether {@code CTRL} has been pressed during this event or not
     */
    protected void setCtrlKey(final boolean ctrlKey) {
        ctrlKey_ = ctrlKey;
    }

    /**
     * Returns whether {@code ALT} has been pressed during this event or not.
     * @return whether {@code ALT} has been pressed during this event or not
     */
    public boolean isAltKey() {
        return altKey_;
    }

    /**
     * Sets whether {@code ALT} key is pressed on not.
     * @param altKey whether {@code ALT} has been pressed during this event or not
     */
    protected void setAltKey(final boolean altKey) {
        altKey_ = altKey;
    }

    /**
     * Returns the current event phase for the event.
     * @return the current event phase for the event
     */
    @JsxGetter
    public int getEventPhase() {
        return eventPhase_;
    }

    /**
     * Sets the current event phase. Must be one of {@link #CAPTURING_PHASE}, {@link #AT_TARGET} or
     * {@link #BUBBLING_PHASE}.
     *
     * @param phase the phase the event is in
     */
    public void setEventPhase(final short phase) {
        if (phase != CAPTURING_PHASE && phase != AT_TARGET && phase != BUBBLING_PHASE) {
            throw new IllegalArgumentException("Illegal phase specified: " + phase);
        }
        eventPhase_ = phase;
    }

    /**
     * @return whether or not this event bubbles
     */
    @JsxGetter
    public boolean isBubbles() {
        return bubbles_;
    }

    /**
     * @param bubbles the bubbles to set
     */
    protected void setBubbles(final boolean bubbles) {
        bubbles_ = bubbles;
    }

    /**
     * @return whether or not this event can be canceled
     */
    @JsxGetter
    public boolean isCancelable() {
        return cancelable_;
    }

    /**
     * @param cancelable the cancelable to set
     */
    protected void setCancelable(final boolean cancelable) {
        cancelable_ = cancelable;
    }

    /**
     * Returns {@code true} if both <tt>cancelable</tt> is {@code true} and <tt>preventDefault()</tt> has been
     * called for this event. Otherwise this attribute must return {@code false}.
     * @return {@code true} if this event has been cancelled or not
     */
    @JsxGetter
    public boolean isDefaultPrevented() {
        return cancelable_ && preventDefault_;
    }

    /**
     * @return indicates if event propagation is stopped
     */
    @JsxGetter
    public boolean isCancelBubble() {
        return stopPropagation_;
    }

    /**
     * @param newValue indicates if event propagation is stopped
     */
    @JsxSetter
    public void setCancelBubble(final boolean newValue) {
        stopPropagation_ = newValue;
    }

    /**
     * Stops the event from propagating.
     */
    @JsxFunction
    public void stopPropagation() {
        stopPropagation_ = true;
    }

    /**
     * Indicates if event propagation is stopped.
     * @return the status
     */
    public boolean isPropagationStopped() {
        return stopPropagation_;
    }

    /**
     * Prevents other listeners of the same event from being called.
     */
    @JsxFunction
    public void stopImmediatePropagation() {
        stopImmediatePropagation_ = true;
        stopPropagation();
    }

    /**
     * Indicates if event immediate propagation is stopped.
     * @return the status
     */
    public boolean isImmediatePropagationStopped() {
        return stopImmediatePropagation_;
    }

    /**
     * Handles the return values of property handlers.
     * @param returnValue the return value returned by the property handler
     */
    void handlePropertyHandlerReturnValue(final Object returnValue) {
        if (Boolean.FALSE.equals(returnValue)) {
            preventDefault();
        }
    }

    /**
     * Returns the property name associated with the event.
     * @return the property name associated with the event
     */
    public String getPropertyName() {
        return propertyName_;
    }

    /**
     * Initializes this event.
     * @param type the event type
     * @param bubbles whether or not the event should bubble
     * @param cancelable whether or not the event the event should be cancelable
     */
    @JsxFunction
    public void initEvent(final String type, final boolean bubbles, final boolean cancelable) {
        type_ = type;
        bubbles_ = bubbles;
        cancelable_ = cancelable;
    }

    /**
     * If, during any stage of event flow, this method is called the event is canceled.
     * Any default action associated with the event will not occur.
     * Calling this method for a non-cancelable event has no effect.
     */
    @JsxFunction
    public void preventDefault() {
        if (isCancelable()) {
            preventDefault_ = true;
        }
    }

    /**
     * Returns {@code true} if this event has been aborted via <tt>preventDefault()</tt> in
     * standards-compliant browsers, or via the event's <tt>returnValue</tt> property in IE, or
     * by the event handler returning {@code false}.
     *
     * @param result the event handler result (if {@code false}, the event is considered aborted)
     * @return {@code true} if this event has been aborted
     */
    public boolean isAborted(final ScriptResult result) {
        return ScriptResult.isFalse(result) || preventDefault_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(40);
        builder.append("Event ")
            .append(getType())
            .append(" (")
            .append("Current Target: ")
            .append(currentTarget_)
            .append(");");
        return builder.toString();
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * If we click on a label, we have to simulate a click on the element referenced by the 'for' attribute also.
     * To support this for special events we have this method here.
     * @return false in this default impl
     */
    public boolean processLabelAfterBubbling() {
        return false;
    }

    /**
     * @return the return value property
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Object getReturnValue() {
        return !preventDefault_;
    }

    /**
     * @param newValue the new return value
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setReturnValue(final Object newValue) {
        if (isCancelable()) {
            final boolean bool = !ScriptRuntime.toBoolean(newValue);
            if (bool) {
                preventDefault_ = bool;
            }
        }
    }

    /**
     * @return the return composed property
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Object getComposed() {
        return false;
    }

    /**
     * Returns whether the given value indicates a missing or undefined property.
     * @param value the new value
     * @return whether the given value indicates a missing or undefined property
     */
    protected static boolean isMissingOrUndefined(final Object value) {
        return value == Scriptable.NOT_FOUND || Undefined.isUndefined(value);
    }
}
