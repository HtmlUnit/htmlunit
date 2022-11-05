/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.configuration;

import java.util.Map;
import java.util.WeakHashMap;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.ActiveXObject;
import com.gargoylesoftware.htmlunit.javascript.host.ApplicationCache;
import com.gargoylesoftware.htmlunit.javascript.host.AudioScheduledSourceNode;
import com.gargoylesoftware.htmlunit.javascript.host.BarProp;
import com.gargoylesoftware.htmlunit.javascript.host.BatteryManager;
import com.gargoylesoftware.htmlunit.javascript.host.BroadcastChannel;
import com.gargoylesoftware.htmlunit.javascript.host.Cache;
import com.gargoylesoftware.htmlunit.javascript.host.CacheStorage;
import com.gargoylesoftware.htmlunit.javascript.host.ClientRect;
import com.gargoylesoftware.htmlunit.javascript.host.ClientRectList;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.External;
import com.gargoylesoftware.htmlunit.javascript.host.FontFace;
import com.gargoylesoftware.htmlunit.javascript.host.FontFaceSet;
import com.gargoylesoftware.htmlunit.javascript.host.Gamepad;
import com.gargoylesoftware.htmlunit.javascript.host.GamepadButton;
import com.gargoylesoftware.htmlunit.javascript.host.History;
import com.gargoylesoftware.htmlunit.javascript.host.ImageBitmap;
import com.gargoylesoftware.htmlunit.javascript.host.InstallTrigger;
import com.gargoylesoftware.htmlunit.javascript.host.Location;
import com.gargoylesoftware.htmlunit.javascript.host.MessageChannel;
import com.gargoylesoftware.htmlunit.javascript.host.MessagePort;
import com.gargoylesoftware.htmlunit.javascript.host.MimeType;
import com.gargoylesoftware.htmlunit.javascript.host.MimeTypeArray;
import com.gargoylesoftware.htmlunit.javascript.host.NamedNodeMap;
import com.gargoylesoftware.htmlunit.javascript.host.Namespace;
import com.gargoylesoftware.htmlunit.javascript.host.NamespaceCollection;
import com.gargoylesoftware.htmlunit.javascript.host.Navigator;
import com.gargoylesoftware.htmlunit.javascript.host.Notification;
import com.gargoylesoftware.htmlunit.javascript.host.PerformanceObserver;
import com.gargoylesoftware.htmlunit.javascript.host.PerformanceObserverEntryList;
import com.gargoylesoftware.htmlunit.javascript.host.PermissionStatus;
import com.gargoylesoftware.htmlunit.javascript.host.Permissions;
import com.gargoylesoftware.htmlunit.javascript.host.Plugin;
import com.gargoylesoftware.htmlunit.javascript.host.PluginArray;
import com.gargoylesoftware.htmlunit.javascript.host.PushManager;
import com.gargoylesoftware.htmlunit.javascript.host.PushSubscription;
import com.gargoylesoftware.htmlunit.javascript.host.PushSubscriptionOptions;
import com.gargoylesoftware.htmlunit.javascript.host.ReadableStream;
import com.gargoylesoftware.htmlunit.javascript.host.Screen;
import com.gargoylesoftware.htmlunit.javascript.host.ScreenOrientation;
import com.gargoylesoftware.htmlunit.javascript.host.SharedWorker;
import com.gargoylesoftware.htmlunit.javascript.host.SimpleArray;
import com.gargoylesoftware.htmlunit.javascript.host.Storage;
import com.gargoylesoftware.htmlunit.javascript.host.StorageManager;
import com.gargoylesoftware.htmlunit.javascript.host.TextDecoder;
import com.gargoylesoftware.htmlunit.javascript.host.TextEncoder;
import com.gargoylesoftware.htmlunit.javascript.host.Touch;
import com.gargoylesoftware.htmlunit.javascript.host.TouchList;
import com.gargoylesoftware.htmlunit.javascript.host.URL;
import com.gargoylesoftware.htmlunit.javascript.host.URLSearchParams;
import com.gargoylesoftware.htmlunit.javascript.host.WebSocket;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.XPathExpression;
import com.gargoylesoftware.htmlunit.javascript.host.webkitURL;
import com.gargoylesoftware.htmlunit.javascript.host.animations.Animation;
import com.gargoylesoftware.htmlunit.javascript.host.animations.AnimationEvent;
import com.gargoylesoftware.htmlunit.javascript.host.animations.KeyframeEffect;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.Atomics;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.CanvasCaptureMediaStream;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.CanvasCaptureMediaStreamTrack;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.CanvasGradient;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.CanvasPattern;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.CanvasRenderingContext2D;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.ImageBitmapRenderingContext;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.ImageData;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.IntersectionObserver;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.IntersectionObserverEntry;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.Path2D;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.TextMetrics;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGL2RenderingContext;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLActiveInfo;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLBuffer;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLFramebuffer;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLProgram;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLQuery;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLRenderbuffer;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLRenderingContext;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLSampler;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLShader;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLShaderPrecisionFormat;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLSync;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLTexture;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLTransformFeedback;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLUniformLocation;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLVertexArrayObject;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.ext.ANGLE_instanced_arrays;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.ext.EXT_texture_filter_anisotropic;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.ext.OES_element_index_uint;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.ext.OES_standard_derivatives;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.ext.OES_texture_float;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.ext.OES_texture_float_linear;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.ext.WEBGL_compressed_texture_s3tc;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.ext.WEBGL_debug_renderer_info;
import com.gargoylesoftware.htmlunit.javascript.host.crypto.Crypto;
import com.gargoylesoftware.htmlunit.javascript.host.crypto.CryptoKey;
import com.gargoylesoftware.htmlunit.javascript.host.crypto.SubtleCrypto;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSS;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSConditionRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSCounterStyleRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSFontFaceRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSGroupingRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSImportRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSKeyframeRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSKeyframesRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSMediaRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSNamespaceRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSPageRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSRuleList;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSSupportsRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CaretPosition;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.MediaList;
import com.gargoylesoftware.htmlunit.javascript.host.css.MediaQueryList;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleMedia;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleSheet;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleSheetList;
import com.gargoylesoftware.htmlunit.javascript.host.css.WebKitCSSMatrix;
import com.gargoylesoftware.htmlunit.javascript.host.dom.AbstractList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Attr;
import com.gargoylesoftware.htmlunit.javascript.host.dom.CDATASection;
import com.gargoylesoftware.htmlunit.javascript.host.dom.CharacterData;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Comment;
import com.gargoylesoftware.htmlunit.javascript.host.dom.CustomElementRegistry;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMError;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMException;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMImplementation;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMMatrix;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMMatrixReadOnly;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMParser;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMPoint;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMPointReadOnly;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMRectReadOnly;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMRequest;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMSettableTokenList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMStringList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMStringMap;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMTokenList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DocumentFragment;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DocumentType;
import com.gargoylesoftware.htmlunit.javascript.host.dom.IdleDeadline;
import com.gargoylesoftware.htmlunit.javascript.host.dom.MutationObserver;
import com.gargoylesoftware.htmlunit.javascript.host.dom.MutationRecord;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node;
import com.gargoylesoftware.htmlunit.javascript.host.dom.NodeFilter;
import com.gargoylesoftware.htmlunit.javascript.host.dom.NodeIterator;
import com.gargoylesoftware.htmlunit.javascript.host.dom.NodeList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.ProcessingInstruction;
import com.gargoylesoftware.htmlunit.javascript.host.dom.RadioNodeList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Range;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Selection;
import com.gargoylesoftware.htmlunit.javascript.host.dom.ShadowRoot;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Text;
import com.gargoylesoftware.htmlunit.javascript.host.dom.TextRange;
import com.gargoylesoftware.htmlunit.javascript.host.dom.TreeWalker;
import com.gargoylesoftware.htmlunit.javascript.host.dom.WebKitMutationObserver;
import com.gargoylesoftware.htmlunit.javascript.host.dom.XPathEvaluator;
import com.gargoylesoftware.htmlunit.javascript.host.dom.XPathNSResolver;
import com.gargoylesoftware.htmlunit.javascript.host.dom.XPathResult;
import com.gargoylesoftware.htmlunit.javascript.host.event.AudioProcessingEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.BeforeInstallPromptEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.BeforeUnloadEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.BlobEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.ClipboardEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.CloseEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.CompositionEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.CustomEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.DeviceMotionEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.DeviceOrientationEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.DragEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.ErrorEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventSource;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;
import com.gargoylesoftware.htmlunit.javascript.host.event.FocusEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.GamepadEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.HashChangeEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.IDBVersionChangeEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.InputEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.KeyboardEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MIDIConnectionEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MIDIMessageEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MSGestureEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MediaEncryptedEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MediaKeyMessageEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MediaQueryListEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MediaStreamEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MediaStreamTrackEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MessageEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseScrollEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseWheelEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MutationEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.OfflineAudioCompletionEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.PageTransitionEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.PointerEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.PopStateEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.PresentationConnectionAvailableEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.PresentationConnectionCloseEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.ProgressEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.PromiseRejectionEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.RTCDataChannelEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.RTCPeerConnectionIceEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.SVGZoomEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.SecurityPolicyViolationEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.SpeechSynthesisEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.StorageEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.TextEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.TimeEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.TouchEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.TrackEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.TransitionEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.UIEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.WebGLContextEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.WebkitSpeechRecognitionError;
import com.gargoylesoftware.htmlunit.javascript.host.event.WebkitSpeechRecognitionEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.WheelEvent;
import com.gargoylesoftware.htmlunit.javascript.host.fetch.Headers;
import com.gargoylesoftware.htmlunit.javascript.host.fetch.Request;
import com.gargoylesoftware.htmlunit.javascript.host.fetch.Response;
import com.gargoylesoftware.htmlunit.javascript.host.file.Blob;
import com.gargoylesoftware.htmlunit.javascript.host.file.DataTransferItem;
import com.gargoylesoftware.htmlunit.javascript.host.file.DataTransferItemList;
import com.gargoylesoftware.htmlunit.javascript.host.file.File;
import com.gargoylesoftware.htmlunit.javascript.host.file.FileList;
import com.gargoylesoftware.htmlunit.javascript.host.file.FileReader;
import com.gargoylesoftware.htmlunit.javascript.host.file.FileSystem;
import com.gargoylesoftware.htmlunit.javascript.host.file.FileSystemDirectoryEntry;
import com.gargoylesoftware.htmlunit.javascript.host.file.FileSystemDirectoryReader;
import com.gargoylesoftware.htmlunit.javascript.host.file.FileSystemEntry;
import com.gargoylesoftware.htmlunit.javascript.host.file.FileSystemFileEntry;
import com.gargoylesoftware.htmlunit.javascript.host.geo.Coordinates;
import com.gargoylesoftware.htmlunit.javascript.host.geo.Geolocation;
import com.gargoylesoftware.htmlunit.javascript.host.geo.Position;
import com.gargoylesoftware.htmlunit.javascript.host.geo.PositionError;
import com.gargoylesoftware.htmlunit.javascript.host.html.Audio;
import com.gargoylesoftware.htmlunit.javascript.host.html.DataTransfer;
import com.gargoylesoftware.htmlunit.javascript.host.html.Enumerator;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAllCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAnchorElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAppletElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAreaElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAudioElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBGSoundElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBRElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBaseElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBaseFontElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBlockElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLButtonElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCanvasElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDDElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDListElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDTElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDataElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDataListElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDetailsElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDialogElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDirectoryElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDivElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLEmbedElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFieldSetElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFontElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFormControlsCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFormElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFrameElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFrameSetElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHRElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHeadElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHeadingElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHtmlElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLIFrameElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLImageElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLInlineQuotationElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLInputElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLIsIndexElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLIElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLabelElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLegendElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLinkElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLListElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMapElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMarqueeElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMediaElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMenuElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMetaElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMeterElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLModElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLNextIdElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOListElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLObjectElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOptGroupElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOptionElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOptionsCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOutputElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLParagraphElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLParamElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLPhraseElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLPictureElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLPreElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLProgressElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLQuoteElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLScriptElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSelectElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSlotElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSourceElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSpanElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLStyleElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableCaptionElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableCellElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableColElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableComponent;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableDataCellElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableHeaderCellElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableRowElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableSectionElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTemplateElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTextAreaElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTimeElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTitleElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTrackElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLUListElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLUnknownElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLVideoElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.Image;
import com.gargoylesoftware.htmlunit.javascript.host.html.Option;
import com.gargoylesoftware.htmlunit.javascript.host.html.RowContainer;
import com.gargoylesoftware.htmlunit.javascript.host.html.ValidityState;
import com.gargoylesoftware.htmlunit.javascript.host.idb.IDBCursor;
import com.gargoylesoftware.htmlunit.javascript.host.idb.IDBCursorWithValue;
import com.gargoylesoftware.htmlunit.javascript.host.idb.IDBDatabase;
import com.gargoylesoftware.htmlunit.javascript.host.idb.IDBFactory;
import com.gargoylesoftware.htmlunit.javascript.host.idb.IDBIndex;
import com.gargoylesoftware.htmlunit.javascript.host.idb.IDBKeyRange;
import com.gargoylesoftware.htmlunit.javascript.host.idb.IDBMutableFile;
import com.gargoylesoftware.htmlunit.javascript.host.idb.IDBObjectStore;
import com.gargoylesoftware.htmlunit.javascript.host.idb.IDBOpenDBRequest;
import com.gargoylesoftware.htmlunit.javascript.host.idb.IDBRequest;
import com.gargoylesoftware.htmlunit.javascript.host.idb.IDBTransaction;
import com.gargoylesoftware.htmlunit.javascript.host.media.AnalyserNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.AudioBuffer;
import com.gargoylesoftware.htmlunit.javascript.host.media.AudioBufferSourceNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.AudioContext;
import com.gargoylesoftware.htmlunit.javascript.host.media.AudioDestinationNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.AudioListener;
import com.gargoylesoftware.htmlunit.javascript.host.media.AudioNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.AudioParam;
import com.gargoylesoftware.htmlunit.javascript.host.media.BaseAudioContext;
import com.gargoylesoftware.htmlunit.javascript.host.media.BiquadFilterNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.ChannelMergerNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.ChannelSplitterNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.ConstantSourceNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.ConvolverNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.DelayNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.DynamicsCompressorNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.GainNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.IIRFilterNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.InputDeviceCapabilities;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaDeviceInfo;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaDevices;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaElementAudioSourceNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaError;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaKeyError;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaKeySession;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaKeyStatusMap;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaKeySystemAccess;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaKeys;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaRecorder;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaSource;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaStream;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaStreamAudioDestinationNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaStreamAudioSourceNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaStreamTrack;
import com.gargoylesoftware.htmlunit.javascript.host.media.OfflineAudioContext;
import com.gargoylesoftware.htmlunit.javascript.host.media.OscillatorNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.PannerNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.PeriodicSyncManager;
import com.gargoylesoftware.htmlunit.javascript.host.media.PeriodicWave;
import com.gargoylesoftware.htmlunit.javascript.host.media.RemotePlayback;
import com.gargoylesoftware.htmlunit.javascript.host.media.ScriptProcessorNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.SourceBuffer;
import com.gargoylesoftware.htmlunit.javascript.host.media.SourceBufferList;
import com.gargoylesoftware.htmlunit.javascript.host.media.StereoPannerNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.TextTrack;
import com.gargoylesoftware.htmlunit.javascript.host.media.TextTrackCue;
import com.gargoylesoftware.htmlunit.javascript.host.media.TextTrackCueList;
import com.gargoylesoftware.htmlunit.javascript.host.media.TextTrackList;
import com.gargoylesoftware.htmlunit.javascript.host.media.TimeRanges;
import com.gargoylesoftware.htmlunit.javascript.host.media.VTTCue;
import com.gargoylesoftware.htmlunit.javascript.host.media.VideoPlaybackQuality;
import com.gargoylesoftware.htmlunit.javascript.host.media.WaveShaperNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.WebkitMediaStream;
import com.gargoylesoftware.htmlunit.javascript.host.media.midi.MIDIAccess;
import com.gargoylesoftware.htmlunit.javascript.host.media.midi.MIDIInput;
import com.gargoylesoftware.htmlunit.javascript.host.media.midi.MIDIInputMap;
import com.gargoylesoftware.htmlunit.javascript.host.media.midi.MIDIOutput;
import com.gargoylesoftware.htmlunit.javascript.host.media.midi.MIDIOutputMap;
import com.gargoylesoftware.htmlunit.javascript.host.media.midi.MIDIPort;
import com.gargoylesoftware.htmlunit.javascript.host.media.presentation.Presentation;
import com.gargoylesoftware.htmlunit.javascript.host.media.presentation.PresentationAvailability;
import com.gargoylesoftware.htmlunit.javascript.host.media.presentation.PresentationConnection;
import com.gargoylesoftware.htmlunit.javascript.host.media.presentation.PresentationRequest;
import com.gargoylesoftware.htmlunit.javascript.host.media.rtc.MozRTCIceCandidate;
import com.gargoylesoftware.htmlunit.javascript.host.media.rtc.MozRTCPeerConnection;
import com.gargoylesoftware.htmlunit.javascript.host.media.rtc.MozRTCSessionDescription;
import com.gargoylesoftware.htmlunit.javascript.host.media.rtc.RTCCertificate;
import com.gargoylesoftware.htmlunit.javascript.host.media.rtc.RTCIceCandidate;
import com.gargoylesoftware.htmlunit.javascript.host.media.rtc.RTCPeerConnection;
import com.gargoylesoftware.htmlunit.javascript.host.media.rtc.RTCSessionDescription;
import com.gargoylesoftware.htmlunit.javascript.host.media.rtc.RTCStatsReport;
import com.gargoylesoftware.htmlunit.javascript.host.media.rtc.WebkitRTCPeerConnection;
import com.gargoylesoftware.htmlunit.javascript.host.network.NetworkInformation;
import com.gargoylesoftware.htmlunit.javascript.host.payment.PaymentAddress;
import com.gargoylesoftware.htmlunit.javascript.host.payment.PaymentRequest;
import com.gargoylesoftware.htmlunit.javascript.host.payment.PaymentResponse;
import com.gargoylesoftware.htmlunit.javascript.host.performance.Performance;
import com.gargoylesoftware.htmlunit.javascript.host.performance.PerformanceEntry;
import com.gargoylesoftware.htmlunit.javascript.host.performance.PerformanceMark;
import com.gargoylesoftware.htmlunit.javascript.host.performance.PerformanceMeasure;
import com.gargoylesoftware.htmlunit.javascript.host.performance.PerformanceNavigation;
import com.gargoylesoftware.htmlunit.javascript.host.performance.PerformanceNavigationTiming;
import com.gargoylesoftware.htmlunit.javascript.host.performance.PerformanceResourceTiming;
import com.gargoylesoftware.htmlunit.javascript.host.performance.PerformanceTiming;
import com.gargoylesoftware.htmlunit.javascript.host.security.Credential;
import com.gargoylesoftware.htmlunit.javascript.host.security.CredentialsContainer;
import com.gargoylesoftware.htmlunit.javascript.host.security.FederatedCredential;
import com.gargoylesoftware.htmlunit.javascript.host.security.PasswordCredential;
import com.gargoylesoftware.htmlunit.javascript.host.speech.SpeechSynthesis;
import com.gargoylesoftware.htmlunit.javascript.host.speech.SpeechSynthesisErrorEvent;
import com.gargoylesoftware.htmlunit.javascript.host.speech.SpeechSynthesisUtterance;
import com.gargoylesoftware.htmlunit.javascript.host.speech.SpeechSynthesisVoice;
import com.gargoylesoftware.htmlunit.javascript.host.speech.WebkitSpeechGrammar;
import com.gargoylesoftware.htmlunit.javascript.host.speech.WebkitSpeechGrammarList;
import com.gargoylesoftware.htmlunit.javascript.host.speech.WebkitSpeechRecognition;
import com.gargoylesoftware.htmlunit.javascript.host.svg.*;
import com.gargoylesoftware.htmlunit.javascript.host.worker.ServiceWorker;
import com.gargoylesoftware.htmlunit.javascript.host.worker.ServiceWorkerContainer;
import com.gargoylesoftware.htmlunit.javascript.host.worker.ServiceWorkerRegistration;
import com.gargoylesoftware.htmlunit.javascript.host.worker.SyncManager;
import com.gargoylesoftware.htmlunit.javascript.host.worker.Worker;
import com.gargoylesoftware.htmlunit.javascript.host.xml.FormData;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLHttpRequest;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLHttpRequestEventTarget;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLHttpRequestUpload;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLSerializer;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XSLTProcessor;

/**
 * A container for all the JavaScript configuration information.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public final class JavaScriptConfiguration extends AbstractJavaScriptConfiguration {

    @SuppressWarnings("unchecked")
    static final Class<? extends HtmlUnitScriptable>[] CLASSES_ = new Class[] {
        AbstractList.class, ActiveXObject.class, AnalyserNode.class, ANGLE_instanced_arrays.class,
        Animation.class, AnimationEvent.class,
        ApplicationCache.class,
        Atomics.class,
        Attr.class, Audio.class, AudioBuffer.class,
        AudioBufferSourceNode.class, AudioContext.class, AudioDestinationNode.class, AudioListener.class,
        AudioNode.class, AudioParam.class, AudioProcessingEvent.class, AudioScheduledSourceNode.class,
        BarProp.class, BaseAudioContext.class,
        BatteryManager.class, BeforeInstallPromptEvent.class, BeforeUnloadEvent.class, BiquadFilterNode.class,
        Blob.class, BlobEvent.class, BroadcastChannel.class,
        Cache.class, CacheStorage.class,
        CanvasCaptureMediaStream.class, CanvasCaptureMediaStreamTrack.class,
        CanvasGradient.class, CanvasPattern.class, CanvasRenderingContext2D.class, CaretPosition.class,
        CDATASection.class, ChannelMergerNode.class, ChannelSplitterNode.class, CharacterData.class, ClientRect.class,
        ClientRectList.class, ClipboardEvent.class,
        CloseEvent.class, Comment.class, CompositionEvent.class, ComputedCSSStyleDeclaration.class,
        ConstantSourceNode.class,
        ConvolverNode.class, Coordinates.class, Credential.class, CredentialsContainer.class, Crypto.class,
        CryptoKey.class, CSS.class, CSSConditionRule.class,
        CSSCounterStyleRule.class, CSSFontFaceRule.class, CSSGroupingRule.class, CSSImportRule.class,
        CSSKeyframeRule.class, CSSKeyframesRule.class, CSSMediaRule.class, CSSNamespaceRule.class, CSSPageRule.class,
        CSSRule.class, CSSRuleList.class, CSSStyleDeclaration.class, CSSStyleRule.class,
        CSSStyleSheet.class,
        CSSSupportsRule.class, CustomElementRegistry.class,
        CustomEvent.class, DataTransfer.class,
        DataTransferItem.class, DataTransferItemList.class, DelayNode.class,
        DeviceMotionEvent.class, DeviceOrientationEvent.class,
        Document.class, DocumentFragment.class, DocumentType.class, DOMError.class, DOMException.class,
        DOMImplementation.class, DOMMatrix.class, DOMMatrixReadOnly.class, DOMParser.class, DOMPoint.class,
        DOMPointReadOnly.class, DOMRectReadOnly.class, DOMRequest.class,
        DOMSettableTokenList.class, DOMStringList.class, DOMStringMap.class, DOMTokenList.class,
        DragEvent.class, DynamicsCompressorNode.class,
        Element.class, Enumerator.class, ErrorEvent.class, Event.class, EventSource.class,
        EventTarget.class, EXT_texture_filter_anisotropic.class, External.class, FederatedCredential.class,
        File.class, FileList.class, FileReader.class,
        FileSystem.class, FileSystemDirectoryEntry.class, FileSystemDirectoryReader.class,
        FileSystemEntry.class, FileSystemFileEntry.class,
        FocusEvent.class, FontFace.class,
        FontFaceSet.class, FormData.class, GainNode.class, Gamepad.class,
        GamepadButton.class, GamepadEvent.class, Geolocation.class, HashChangeEvent.class, Headers.class, History.class,
        HTMLAllCollection.class,
        HTMLAnchorElement.class, HTMLAppletElement.class, HTMLAreaElement.class, HTMLAudioElement.class,
        HTMLBaseElement.class, HTMLBaseFontElement.class, HTMLBGSoundElement.class, HTMLBlockElement.class,
        HTMLBodyElement.class, HTMLBRElement.class, HTMLButtonElement.class,
        HTMLCanvasElement.class, HTMLCollection.class,
        HTMLDataElement.class, HTMLDataListElement.class,
        HTMLDDElement.class, HTMLDetailsElement.class, HTMLDialogElement.class, HTMLDirectoryElement.class,
        HTMLDivElement.class, HTMLDListElement.class, HTMLDocument.class, HTMLDTElement.class, HTMLElement.class,
        HTMLEmbedElement.class, HTMLFieldSetElement.class,
        HTMLFontElement.class, HTMLFormControlsCollection.class, HTMLFormElement.class, HTMLFrameElement.class,
        HTMLFrameSetElement.class,
        HTMLHeadElement.class, HTMLHeadingElement.class, HTMLHRElement.class, HTMLHtmlElement.class,
        HTMLIFrameElement.class, HTMLImageElement.class, HTMLInlineQuotationElement.class, HTMLInputElement.class,
        HTMLIsIndexElement.class, HTMLLabelElement.class,
        HTMLLegendElement.class, HTMLLIElement.class, HTMLLinkElement.class, HTMLListElement.class,
        HTMLMapElement.class, HTMLMarqueeElement.class,
        HTMLMediaElement.class, HTMLMenuElement.class, HTMLMetaElement.class,
        HTMLMeterElement.class, HTMLModElement.class, HTMLNextIdElement.class,
        HTMLObjectElement.class, HTMLOListElement.class, HTMLOptGroupElement.class,
        HTMLOptionElement.class, HTMLOptionsCollection.class, HTMLOutputElement.class,
        HTMLParagraphElement.class, HTMLParamElement.class, HTMLPhraseElement.class, HTMLPictureElement.class,
        HTMLPreElement.class, HTMLProgressElement.class, HTMLQuoteElement.class, HTMLScriptElement.class,
        HTMLSelectElement.class, HTMLSlotElement.class, HTMLSourceElement.class,
        HTMLSpanElement.class,
        HTMLStyleElement.class, HTMLTableCaptionElement.class, HTMLTableCellElement.class, HTMLTableColElement.class,
        HTMLTableComponent.class, HTMLTableDataCellElement.class, HTMLTableElement.class,
        HTMLTableHeaderCellElement.class, HTMLTableRowElement.class, HTMLTableSectionElement.class,
        HTMLTemplateElement.class, HTMLTextAreaElement.class, HTMLTimeElement.class,
        HTMLTitleElement.class, HTMLTrackElement.class, HTMLUListElement.class, HTMLUnknownElement.class,
        HTMLVideoElement.class,
        IDBCursor.class, IDBCursorWithValue.class, IDBDatabase.class, IDBFactory.class, IDBIndex.class,
        IDBKeyRange.class, IDBMutableFile.class, IDBObjectStore.class, IDBOpenDBRequest.class, IDBRequest.class,
        IDBTransaction.class, IDBVersionChangeEvent.class, IdleDeadline.class, IIRFilterNode.class,
        Image.class, ImageBitmap.class, ImageBitmapRenderingContext.class, ImageData.class,
        InputDeviceCapabilities.class, InputEvent.class,
        InstallTrigger.class,
        IntersectionObserver.class, IntersectionObserverEntry.class,
        KeyboardEvent.class, KeyframeEffect.class,
        Location.class,
        MediaDeviceInfo.class,
        MediaDevices.class, MediaElementAudioSourceNode.class, MediaEncryptedEvent.class, MediaError.class,
        MediaKeyError.class, MediaKeyMessageEvent.class, MediaKeys.class, MediaKeySession.class,
        MediaKeyStatusMap.class, MediaKeySystemAccess.class, MediaList.class, MediaQueryList.class,
        MediaQueryListEvent.class, MediaRecorder.class,
        MediaSource.class, MediaStream.class, MediaStreamAudioDestinationNode.class, MediaStreamAudioSourceNode.class,
        MediaStreamEvent.class, MediaStreamTrack.class, MediaStreamTrackEvent.class, MessageChannel.class,
        MessageEvent.class, MessagePort.class, MIDIAccess.class, MIDIConnectionEvent.class, MIDIInput.class,
        MIDIInputMap.class, MIDIMessageEvent.class, MIDIOutput.class, MIDIOutputMap.class, MIDIPort.class,
        MimeType.class, MimeTypeArray.class, MouseEvent.class, MouseScrollEvent.class,
        MouseWheelEvent.class, MozRTCIceCandidate.class, MozRTCPeerConnection.class, MozRTCSessionDescription.class,
        MSGestureEvent.class,
        MutationEvent.class, MutationObserver.class, MutationRecord.class, NamedNodeMap.class,
        Namespace.class, NamespaceCollection.class,
        Navigator.class, NetworkInformation.class, Node.class, NodeFilter.class, NodeIterator.class,
        NodeList.class, Notification.class, OES_element_index_uint.class, OES_standard_derivatives.class,
        OES_texture_float.class, OES_texture_float_linear.class, OfflineAudioCompletionEvent.class,
        OfflineAudioContext.class, Option.class, OscillatorNode.class, PageTransitionEvent.class, PannerNode.class,
        PasswordCredential.class,
        Path2D.class,
        PaymentAddress.class, PaymentRequest.class, PaymentResponse.class,
        Performance.class, PerformanceEntry.class, PerformanceMark.class,
        PerformanceMeasure.class, PerformanceNavigation.class, PerformanceNavigationTiming.class,
        PerformanceObserver.class, PerformanceObserverEntryList.class,
        PerformanceResourceTiming.class, PerformanceTiming.class, PeriodicSyncManager.class, PeriodicWave.class,
        Permissions.class, PermissionStatus.class, Plugin.class, PluginArray.class,
        PointerEvent.class, PopStateEvent.class, Position.class, PositionError.class, Presentation.class,
        PresentationAvailability.class, PresentationConnection.class, PresentationConnectionAvailableEvent.class,
        PresentationConnectionCloseEvent.class, PresentationRequest.class,
        ProcessingInstruction.class, ProgressEvent.class, PromiseRejectionEvent.class,
        PushManager.class,
        PushSubscription.class, PushSubscriptionOptions.class, RadioNodeList.class, Range.class, ReadableStream.class,
        RemotePlayback.class, Request.class, Response.class, RowContainer.class, RTCCertificate.class,
        RTCDataChannelEvent.class, RTCIceCandidate.class, RTCPeerConnection.class, RTCPeerConnectionIceEvent.class,
        RTCSessionDescription.class, RTCStatsReport.class, Screen.class, ScreenOrientation.class,
        ScriptProcessorNode.class,
        SecurityPolicyViolationEvent.class, Selection.class, ServiceWorker.class, ServiceWorkerContainer.class,
        ServiceWorkerRegistration.class,
        ShadowRoot.class, SharedWorker.class, SimpleArray.class, SourceBuffer.class, SourceBufferList.class,
        SpeechSynthesis.class, SpeechSynthesisErrorEvent.class, SpeechSynthesisEvent.class,
        SpeechSynthesisUtterance.class, SpeechSynthesisVoice.class,
        StereoPannerNode.class, Storage.class, StorageEvent.class, StorageManager.class,
        StyleMedia.class, StyleSheet.class, StyleSheetList.class, SubtleCrypto.class,
        SVGAElement.class, SVGAngle.class, SVGAnimatedAngle.class,
        SVGAnimatedBoolean.class, SVGAnimatedEnumeration.class, SVGAnimatedInteger.class,
        SVGAnimatedLength.class, SVGAnimatedLengthList.class, SVGAnimatedNumber.class, SVGAnimatedNumberList.class,
        SVGAnimatedPreserveAspectRatio.class, SVGAnimatedRect.class, SVGAnimatedString.class,
        SVGAnimatedTransformList.class, SVGAnimateElement.class,
        SVGAnimateMotionElement.class, SVGAnimateTransformElement.class, SVGAnimationElement.class,
        SVGCircleElement.class,
        SVGClipPathElement.class, SVGComponentTransferFunctionElement.class,
        SVGDefsElement.class, SVGDescElement.class, SVGElement.class,
        SVGEllipseElement.class, SVGFEBlendElement.class, SVGFEColorMatrixElement.class,
        SVGFEComponentTransferElement.class, SVGFECompositeElement.class, SVGFEConvolveMatrixElement.class,
        SVGFEDiffuseLightingElement.class, SVGFEDisplacementMapElement.class, SVGFEDistantLightElement.class,
        SVGFEDropShadowElement.class,
        SVGFEFloodElement.class, SVGFEFuncAElement.class, SVGFEFuncBElement.class, SVGFEFuncGElement.class,
        SVGFEFuncRElement.class, SVGFEGaussianBlurElement.class, SVGFEImageElement.class, SVGFEMergeElement.class,
        SVGFEMergeNodeElement.class, SVGFEMorphologyElement.class, SVGFEOffsetElement.class,
        SVGFEPointLightElement.class, SVGFESpecularLightingElement.class, SVGFESpotLightElement.class,
        SVGFETileElement.class, SVGFETurbulenceElement.class, SVGFilterElement.class, SVGForeignObjectElement.class,
        SVGGElement.class, SVGGeometryElement.class, SVGGradientElement.class, SVGGraphicsElement.class,
        SVGImageElement.class, SVGLength.class, SVGLengthList.class, SVGLinearGradientElement.class,
        SVGLineElement.class, SVGMarkerElement.class, SVGMaskElement.class, SVGMatrix.class,
        SVGMetadataElement.class, SVGMPathElement.class, SVGNumber.class, SVGNumberList.class,
        SVGPathElement.class, SVGPathSeg.class, SVGPathSegArcAbs.class,
        SVGPathSegArcRel.class, SVGPathSegClosePath.class, SVGPathSegCurvetoCubicAbs.class,
        SVGPathSegCurvetoCubicRel.class, SVGPathSegCurvetoCubicSmoothAbs.class, SVGPathSegCurvetoCubicSmoothRel.class,
        SVGPathSegCurvetoQuadraticAbs.class, SVGPathSegCurvetoQuadraticRel.class,
        SVGPathSegCurvetoQuadraticSmoothAbs.class, SVGPathSegCurvetoQuadraticSmoothRel.class,
        SVGPathSegLinetoAbs.class, SVGPathSegLinetoHorizontalAbs.class, SVGPathSegLinetoHorizontalRel.class,
        SVGPathSegLinetoRel.class, SVGPathSegLinetoVerticalAbs.class, SVGPathSegLinetoVerticalRel.class,
        SVGPathSegList.class, SVGPathSegMovetoAbs.class, SVGPathSegMovetoRel.class, SVGPatternElement.class,
        SVGPoint.class, SVGPointList.class, SVGPolygonElement.class, SVGPolylineElement.class,
        SVGPreserveAspectRatio.class, SVGRadialGradientElement.class, SVGRect.class, SVGRectElement.class,
        SVGScriptElement.class, SVGSetElement.class, SVGStopElement.class,
        SVGStringList.class, SVGStyleElement.class, SVGSVGElement.class, SVGSwitchElement.class,
        SVGSymbolElement.class, SVGTextContentElement.class, SVGTextElement.class,
        SVGTextPathElement.class, SVGTextPositioningElement.class, SVGTitleElement.class, SVGTransform.class,
        SVGTransformList.class, SVGTSpanElement.class, SVGUnitTypes.class, SVGUseElement.class, SVGViewElement.class,
        SVGZoomEvent.class, SyncManager.class, Text.class, TextDecoder.class,
        TextEncoder.class, TextEvent.class, TextMetrics.class, TextRange.class, TextTrack.class, TextTrackCue.class,
        TextTrackCueList.class, TextTrackList.class, TimeEvent.class, TimeRanges.class,
        Touch.class, TouchEvent.class, TouchList.class, TrackEvent.class, TransitionEvent.class, TreeWalker.class,
        UIEvent.class,
        URL.class,
        URLSearchParams.class, ValidityState.class, VideoPlaybackQuality.class,
        VTTCue.class, WaveShaperNode.class, WebGL2RenderingContext.class,
        WEBGL_compressed_texture_s3tc.class, WEBGL_debug_renderer_info.class, WebGLActiveInfo.class, WebGLBuffer.class,
        WebGLContextEvent.class, WebGLFramebuffer.class, WebGLProgram.class,
        WebGLQuery.class,
        WebGLRenderbuffer.class,
        WebGLRenderingContext.class,
        WebGLSampler.class, WebGLShader.class, WebGLShaderPrecisionFormat.class, WebGLSync.class,
        WebGLTexture.class, WebGLTransformFeedback.class,
        WebGLUniformLocation.class, WebGLVertexArrayObject.class,
        WebKitCSSMatrix.class, WebkitMediaStream.class, WebKitMutationObserver.class,
        WebkitRTCPeerConnection.class, WebkitSpeechGrammar.class,
        WebkitSpeechGrammarList.class, WebkitSpeechRecognition.class, WebkitSpeechRecognitionError.class,
        WebkitSpeechRecognitionEvent.class, webkitURL.class,
        WebSocket.class, WheelEvent.class, Window.class, Worker.class, XMLDocument.class,
        XMLHttpRequest.class, XMLHttpRequestEventTarget.class, XMLHttpRequestUpload.class, XMLSerializer.class,
        XPathEvaluator.class, XPathExpression.class,
        XPathNSResolver.class, XPathResult.class, XSLTProcessor.class
    };

    /** Cache of browser versions and their corresponding JavaScript configurations. */
    private static final Map<String, JavaScriptConfiguration> CONFIGURATION_MAP_ = new WeakHashMap<>();

    /**
     * Constructor is only called from {@link #getInstance(BrowserVersion)} which is synchronized.
     * @param browser the browser version to use
     */
    private JavaScriptConfiguration(final BrowserVersion browser) {
        super(browser);
    }

    /**
     * Returns the instance that represents the configuration for the specified {@link BrowserVersion}.
     * This method is synchronized to allow multi-threaded access to the JavaScript configuration.
     * @param browserVersion the {@link BrowserVersion}
     * @return the instance for the specified {@link BrowserVersion}
     */
    public static synchronized JavaScriptConfiguration getInstance(final BrowserVersion browserVersion) {
        if (browserVersion == null) {
            throw new IllegalArgumentException("BrowserVersion must be provided");
        }
        JavaScriptConfiguration configuration = CONFIGURATION_MAP_.get(browserVersion.getNickname());

        if (configuration == null) {
            configuration = new JavaScriptConfiguration(browserVersion);
            CONFIGURATION_MAP_.put(browserVersion.getNickname(), configuration);
        }
        return configuration;
    }

    @Override
    protected Class<? extends HtmlUnitScriptable>[] getClasses() {
        return CLASSES_;
    }
}
