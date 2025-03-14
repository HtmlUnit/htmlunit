/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.configuration;

import java.util.Map;
import java.util.WeakHashMap;

import org.htmlunit.BrowserVersion;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.host.AudioScheduledSourceNode;
import org.htmlunit.javascript.host.BarProp;
import org.htmlunit.javascript.host.BatteryManager;
import org.htmlunit.javascript.host.BroadcastChannel;
import org.htmlunit.javascript.host.Cache;
import org.htmlunit.javascript.host.CacheStorage;
import org.htmlunit.javascript.host.ClientRect;
import org.htmlunit.javascript.host.ClientRectList;
import org.htmlunit.javascript.host.Element;
import org.htmlunit.javascript.host.External;
import org.htmlunit.javascript.host.FontFace;
import org.htmlunit.javascript.host.FontFaceSet;
import org.htmlunit.javascript.host.Gamepad;
import org.htmlunit.javascript.host.GamepadButton;
import org.htmlunit.javascript.host.History;
import org.htmlunit.javascript.host.ImageBitmap;
import org.htmlunit.javascript.host.Location;
import org.htmlunit.javascript.host.MessageChannel;
import org.htmlunit.javascript.host.MessagePort;
import org.htmlunit.javascript.host.MimeType;
import org.htmlunit.javascript.host.MimeTypeArray;
import org.htmlunit.javascript.host.NamedNodeMap;
import org.htmlunit.javascript.host.Navigator;
import org.htmlunit.javascript.host.Notification;
import org.htmlunit.javascript.host.PerformanceObserver;
import org.htmlunit.javascript.host.PerformanceObserverEntryList;
import org.htmlunit.javascript.host.PermissionStatus;
import org.htmlunit.javascript.host.Permissions;
import org.htmlunit.javascript.host.Plugin;
import org.htmlunit.javascript.host.PluginArray;
import org.htmlunit.javascript.host.PushManager;
import org.htmlunit.javascript.host.PushSubscription;
import org.htmlunit.javascript.host.PushSubscriptionOptions;
import org.htmlunit.javascript.host.ReadableStream;
import org.htmlunit.javascript.host.Screen;
import org.htmlunit.javascript.host.ScreenOrientation;
import org.htmlunit.javascript.host.SharedWorker;
import org.htmlunit.javascript.host.Storage;
import org.htmlunit.javascript.host.StorageManager;
import org.htmlunit.javascript.host.TextDecoder;
import org.htmlunit.javascript.host.TextEncoder;
import org.htmlunit.javascript.host.Touch;
import org.htmlunit.javascript.host.TouchList;
import org.htmlunit.javascript.host.URL;
import org.htmlunit.javascript.host.URLSearchParams;
import org.htmlunit.javascript.host.WebSocket;
import org.htmlunit.javascript.host.Window;
import org.htmlunit.javascript.host.abort.AbortController;
import org.htmlunit.javascript.host.abort.AbortSignal;
import org.htmlunit.javascript.host.animations.Animation;
import org.htmlunit.javascript.host.animations.AnimationEvent;
import org.htmlunit.javascript.host.animations.KeyframeEffect;
import org.htmlunit.javascript.host.arrays.Atomics;
import org.htmlunit.javascript.host.canvas.CanvasCaptureMediaStream;
import org.htmlunit.javascript.host.canvas.CanvasCaptureMediaStreamTrack;
import org.htmlunit.javascript.host.canvas.CanvasGradient;
import org.htmlunit.javascript.host.canvas.CanvasPattern;
import org.htmlunit.javascript.host.canvas.CanvasRenderingContext2D;
import org.htmlunit.javascript.host.canvas.ImageBitmapRenderingContext;
import org.htmlunit.javascript.host.canvas.ImageData;
import org.htmlunit.javascript.host.canvas.IntersectionObserver;
import org.htmlunit.javascript.host.canvas.IntersectionObserverEntry;
import org.htmlunit.javascript.host.canvas.Path2D;
import org.htmlunit.javascript.host.canvas.TextMetrics;
import org.htmlunit.javascript.host.canvas.WebGL2RenderingContext;
import org.htmlunit.javascript.host.canvas.WebGLActiveInfo;
import org.htmlunit.javascript.host.canvas.WebGLBuffer;
import org.htmlunit.javascript.host.canvas.WebGLFramebuffer;
import org.htmlunit.javascript.host.canvas.WebGLProgram;
import org.htmlunit.javascript.host.canvas.WebGLQuery;
import org.htmlunit.javascript.host.canvas.WebGLRenderbuffer;
import org.htmlunit.javascript.host.canvas.WebGLRenderingContext;
import org.htmlunit.javascript.host.canvas.WebGLSampler;
import org.htmlunit.javascript.host.canvas.WebGLShader;
import org.htmlunit.javascript.host.canvas.WebGLShaderPrecisionFormat;
import org.htmlunit.javascript.host.canvas.WebGLSync;
import org.htmlunit.javascript.host.canvas.WebGLTexture;
import org.htmlunit.javascript.host.canvas.WebGLTransformFeedback;
import org.htmlunit.javascript.host.canvas.WebGLUniformLocation;
import org.htmlunit.javascript.host.canvas.WebGLVertexArrayObject;
import org.htmlunit.javascript.host.crypto.Crypto;
import org.htmlunit.javascript.host.crypto.CryptoKey;
import org.htmlunit.javascript.host.crypto.SubtleCrypto;
import org.htmlunit.javascript.host.css.CSS;
import org.htmlunit.javascript.host.css.CSSConditionRule;
import org.htmlunit.javascript.host.css.CSSCounterStyleRule;
import org.htmlunit.javascript.host.css.CSSFontFaceRule;
import org.htmlunit.javascript.host.css.CSSGroupingRule;
import org.htmlunit.javascript.host.css.CSSImportRule;
import org.htmlunit.javascript.host.css.CSSKeyframeRule;
import org.htmlunit.javascript.host.css.CSSKeyframesRule;
import org.htmlunit.javascript.host.css.CSSMediaRule;
import org.htmlunit.javascript.host.css.CSSNamespaceRule;
import org.htmlunit.javascript.host.css.CSSPageRule;
import org.htmlunit.javascript.host.css.CSSRule;
import org.htmlunit.javascript.host.css.CSSRuleList;
import org.htmlunit.javascript.host.css.CSSStyleDeclaration;
import org.htmlunit.javascript.host.css.CSSStyleRule;
import org.htmlunit.javascript.host.css.CSSStyleSheet;
import org.htmlunit.javascript.host.css.CSSSupportsRule;
import org.htmlunit.javascript.host.css.CaretPosition;
import org.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import org.htmlunit.javascript.host.css.MediaList;
import org.htmlunit.javascript.host.css.MediaQueryList;
import org.htmlunit.javascript.host.css.StyleMedia;
import org.htmlunit.javascript.host.css.StyleSheet;
import org.htmlunit.javascript.host.css.StyleSheetList;
import org.htmlunit.javascript.host.dom.AbstractList;
import org.htmlunit.javascript.host.dom.AbstractRange;
import org.htmlunit.javascript.host.dom.Attr;
import org.htmlunit.javascript.host.dom.CDATASection;
import org.htmlunit.javascript.host.dom.CharacterData;
import org.htmlunit.javascript.host.dom.Comment;
import org.htmlunit.javascript.host.dom.CustomElementRegistry;
import org.htmlunit.javascript.host.dom.DOMError;
import org.htmlunit.javascript.host.dom.DOMException;
import org.htmlunit.javascript.host.dom.DOMImplementation;
import org.htmlunit.javascript.host.dom.DOMMatrix;
import org.htmlunit.javascript.host.dom.DOMMatrixReadOnly;
import org.htmlunit.javascript.host.dom.DOMParser;
import org.htmlunit.javascript.host.dom.DOMPoint;
import org.htmlunit.javascript.host.dom.DOMPointReadOnly;
import org.htmlunit.javascript.host.dom.DOMRectReadOnly;
import org.htmlunit.javascript.host.dom.DOMStringList;
import org.htmlunit.javascript.host.dom.DOMStringMap;
import org.htmlunit.javascript.host.dom.DOMTokenList;
import org.htmlunit.javascript.host.dom.Document;
import org.htmlunit.javascript.host.dom.DocumentFragment;
import org.htmlunit.javascript.host.dom.DocumentType;
import org.htmlunit.javascript.host.dom.IdleDeadline;
import org.htmlunit.javascript.host.dom.MutationObserver;
import org.htmlunit.javascript.host.dom.MutationRecord;
import org.htmlunit.javascript.host.dom.Node;
import org.htmlunit.javascript.host.dom.NodeFilter;
import org.htmlunit.javascript.host.dom.NodeIterator;
import org.htmlunit.javascript.host.dom.NodeList;
import org.htmlunit.javascript.host.dom.ProcessingInstruction;
import org.htmlunit.javascript.host.dom.RadioNodeList;
import org.htmlunit.javascript.host.dom.Range;
import org.htmlunit.javascript.host.dom.Selection;
import org.htmlunit.javascript.host.dom.ShadowRoot;
import org.htmlunit.javascript.host.dom.Text;
import org.htmlunit.javascript.host.dom.TreeWalker;
import org.htmlunit.javascript.host.dom.XPathEvaluator;
import org.htmlunit.javascript.host.dom.XPathExpression;
import org.htmlunit.javascript.host.dom.XPathNSResolver;
import org.htmlunit.javascript.host.dom.XPathResult;
import org.htmlunit.javascript.host.draganddrop.DataTransfer;
import org.htmlunit.javascript.host.draganddrop.DataTransferItem;
import org.htmlunit.javascript.host.draganddrop.DataTransferItemList;
import org.htmlunit.javascript.host.event.AudioProcessingEvent;
import org.htmlunit.javascript.host.event.BeforeInstallPromptEvent;
import org.htmlunit.javascript.host.event.BeforeUnloadEvent;
import org.htmlunit.javascript.host.event.BlobEvent;
import org.htmlunit.javascript.host.event.ClipboardEvent;
import org.htmlunit.javascript.host.event.CloseEvent;
import org.htmlunit.javascript.host.event.CompositionEvent;
import org.htmlunit.javascript.host.event.CustomEvent;
import org.htmlunit.javascript.host.event.DeviceMotionEvent;
import org.htmlunit.javascript.host.event.DeviceOrientationEvent;
import org.htmlunit.javascript.host.event.DragEvent;
import org.htmlunit.javascript.host.event.ErrorEvent;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.event.EventSource;
import org.htmlunit.javascript.host.event.EventTarget;
import org.htmlunit.javascript.host.event.FocusEvent;
import org.htmlunit.javascript.host.event.GamepadEvent;
import org.htmlunit.javascript.host.event.HashChangeEvent;
import org.htmlunit.javascript.host.event.IDBVersionChangeEvent;
import org.htmlunit.javascript.host.event.InputEvent;
import org.htmlunit.javascript.host.event.KeyboardEvent;
import org.htmlunit.javascript.host.event.MIDIConnectionEvent;
import org.htmlunit.javascript.host.event.MIDIMessageEvent;
import org.htmlunit.javascript.host.event.MediaEncryptedEvent;
import org.htmlunit.javascript.host.event.MediaKeyMessageEvent;
import org.htmlunit.javascript.host.event.MediaQueryListEvent;
import org.htmlunit.javascript.host.event.MediaStreamEvent;
import org.htmlunit.javascript.host.event.MediaStreamTrackEvent;
import org.htmlunit.javascript.host.event.MessageEvent;
import org.htmlunit.javascript.host.event.MouseEvent;
import org.htmlunit.javascript.host.event.MouseScrollEvent;
import org.htmlunit.javascript.host.event.MutationEvent;
import org.htmlunit.javascript.host.event.OfflineAudioCompletionEvent;
import org.htmlunit.javascript.host.event.PageTransitionEvent;
import org.htmlunit.javascript.host.event.PointerEvent;
import org.htmlunit.javascript.host.event.PopStateEvent;
import org.htmlunit.javascript.host.event.PresentationConnectionAvailableEvent;
import org.htmlunit.javascript.host.event.PresentationConnectionCloseEvent;
import org.htmlunit.javascript.host.event.ProgressEvent;
import org.htmlunit.javascript.host.event.PromiseRejectionEvent;
import org.htmlunit.javascript.host.event.RTCDataChannelEvent;
import org.htmlunit.javascript.host.event.RTCPeerConnectionIceEvent;
import org.htmlunit.javascript.host.event.SecurityPolicyViolationEvent;
import org.htmlunit.javascript.host.event.SpeechSynthesisEvent;
import org.htmlunit.javascript.host.event.StorageEvent;
import org.htmlunit.javascript.host.event.SubmitEvent;
import org.htmlunit.javascript.host.event.TextEvent;
import org.htmlunit.javascript.host.event.TimeEvent;
import org.htmlunit.javascript.host.event.TouchEvent;
import org.htmlunit.javascript.host.event.TrackEvent;
import org.htmlunit.javascript.host.event.TransitionEvent;
import org.htmlunit.javascript.host.event.UIEvent;
import org.htmlunit.javascript.host.event.WebGLContextEvent;
import org.htmlunit.javascript.host.event.WebkitSpeechRecognitionError;
import org.htmlunit.javascript.host.event.WebkitSpeechRecognitionEvent;
import org.htmlunit.javascript.host.event.WheelEvent;
import org.htmlunit.javascript.host.fetch.Headers;
import org.htmlunit.javascript.host.fetch.Request;
import org.htmlunit.javascript.host.fetch.Response;
import org.htmlunit.javascript.host.file.Blob;
import org.htmlunit.javascript.host.file.File;
import org.htmlunit.javascript.host.file.FileList;
import org.htmlunit.javascript.host.file.FileReader;
import org.htmlunit.javascript.host.file.FileSystem;
import org.htmlunit.javascript.host.file.FileSystemDirectoryEntry;
import org.htmlunit.javascript.host.file.FileSystemDirectoryReader;
import org.htmlunit.javascript.host.file.FileSystemEntry;
import org.htmlunit.javascript.host.file.FileSystemFileEntry;
import org.htmlunit.javascript.host.geo.Geolocation;
import org.htmlunit.javascript.host.geo.GeolocationCoordinates;
import org.htmlunit.javascript.host.geo.GeolocationPosition;
import org.htmlunit.javascript.host.geo.GeolocationPositionError;
import org.htmlunit.javascript.host.html.Audio;
import org.htmlunit.javascript.host.html.HTMLAllCollection;
import org.htmlunit.javascript.host.html.HTMLAnchorElement;
import org.htmlunit.javascript.host.html.HTMLAreaElement;
import org.htmlunit.javascript.host.html.HTMLAudioElement;
import org.htmlunit.javascript.host.html.HTMLBRElement;
import org.htmlunit.javascript.host.html.HTMLBaseElement;
import org.htmlunit.javascript.host.html.HTMLBodyElement;
import org.htmlunit.javascript.host.html.HTMLButtonElement;
import org.htmlunit.javascript.host.html.HTMLCanvasElement;
import org.htmlunit.javascript.host.html.HTMLCollection;
import org.htmlunit.javascript.host.html.HTMLDListElement;
import org.htmlunit.javascript.host.html.HTMLDataElement;
import org.htmlunit.javascript.host.html.HTMLDataListElement;
import org.htmlunit.javascript.host.html.HTMLDetailsElement;
import org.htmlunit.javascript.host.html.HTMLDialogElement;
import org.htmlunit.javascript.host.html.HTMLDirectoryElement;
import org.htmlunit.javascript.host.html.HTMLDivElement;
import org.htmlunit.javascript.host.html.HTMLDocument;
import org.htmlunit.javascript.host.html.HTMLElement;
import org.htmlunit.javascript.host.html.HTMLEmbedElement;
import org.htmlunit.javascript.host.html.HTMLFieldSetElement;
import org.htmlunit.javascript.host.html.HTMLFontElement;
import org.htmlunit.javascript.host.html.HTMLFormControlsCollection;
import org.htmlunit.javascript.host.html.HTMLFormElement;
import org.htmlunit.javascript.host.html.HTMLFrameElement;
import org.htmlunit.javascript.host.html.HTMLFrameSetElement;
import org.htmlunit.javascript.host.html.HTMLHRElement;
import org.htmlunit.javascript.host.html.HTMLHeadElement;
import org.htmlunit.javascript.host.html.HTMLHeadingElement;
import org.htmlunit.javascript.host.html.HTMLHtmlElement;
import org.htmlunit.javascript.host.html.HTMLIFrameElement;
import org.htmlunit.javascript.host.html.HTMLImageElement;
import org.htmlunit.javascript.host.html.HTMLInputElement;
import org.htmlunit.javascript.host.html.HTMLLIElement;
import org.htmlunit.javascript.host.html.HTMLLabelElement;
import org.htmlunit.javascript.host.html.HTMLLegendElement;
import org.htmlunit.javascript.host.html.HTMLLinkElement;
import org.htmlunit.javascript.host.html.HTMLListElement;
import org.htmlunit.javascript.host.html.HTMLMapElement;
import org.htmlunit.javascript.host.html.HTMLMarqueeElement;
import org.htmlunit.javascript.host.html.HTMLMediaElement;
import org.htmlunit.javascript.host.html.HTMLMenuElement;
import org.htmlunit.javascript.host.html.HTMLMetaElement;
import org.htmlunit.javascript.host.html.HTMLMeterElement;
import org.htmlunit.javascript.host.html.HTMLModElement;
import org.htmlunit.javascript.host.html.HTMLOListElement;
import org.htmlunit.javascript.host.html.HTMLObjectElement;
import org.htmlunit.javascript.host.html.HTMLOptGroupElement;
import org.htmlunit.javascript.host.html.HTMLOptionElement;
import org.htmlunit.javascript.host.html.HTMLOptionsCollection;
import org.htmlunit.javascript.host.html.HTMLOutputElement;
import org.htmlunit.javascript.host.html.HTMLParagraphElement;
import org.htmlunit.javascript.host.html.HTMLParamElement;
import org.htmlunit.javascript.host.html.HTMLPictureElement;
import org.htmlunit.javascript.host.html.HTMLPreElement;
import org.htmlunit.javascript.host.html.HTMLProgressElement;
import org.htmlunit.javascript.host.html.HTMLQuoteElement;
import org.htmlunit.javascript.host.html.HTMLScriptElement;
import org.htmlunit.javascript.host.html.HTMLSelectElement;
import org.htmlunit.javascript.host.html.HTMLSlotElement;
import org.htmlunit.javascript.host.html.HTMLSourceElement;
import org.htmlunit.javascript.host.html.HTMLSpanElement;
import org.htmlunit.javascript.host.html.HTMLStyleElement;
import org.htmlunit.javascript.host.html.HTMLTableCaptionElement;
import org.htmlunit.javascript.host.html.HTMLTableCellElement;
import org.htmlunit.javascript.host.html.HTMLTableColElement;
import org.htmlunit.javascript.host.html.HTMLTableComponent;
import org.htmlunit.javascript.host.html.HTMLTableElement;
import org.htmlunit.javascript.host.html.HTMLTableRowElement;
import org.htmlunit.javascript.host.html.HTMLTableSectionElement;
import org.htmlunit.javascript.host.html.HTMLTemplateElement;
import org.htmlunit.javascript.host.html.HTMLTextAreaElement;
import org.htmlunit.javascript.host.html.HTMLTimeElement;
import org.htmlunit.javascript.host.html.HTMLTitleElement;
import org.htmlunit.javascript.host.html.HTMLTrackElement;
import org.htmlunit.javascript.host.html.HTMLUListElement;
import org.htmlunit.javascript.host.html.HTMLUnknownElement;
import org.htmlunit.javascript.host.html.HTMLVideoElement;
import org.htmlunit.javascript.host.html.RowContainer;
import org.htmlunit.javascript.host.html.ValidityState;
import org.htmlunit.javascript.host.idb.IDBCursor;
import org.htmlunit.javascript.host.idb.IDBCursorWithValue;
import org.htmlunit.javascript.host.idb.IDBDatabase;
import org.htmlunit.javascript.host.idb.IDBFactory;
import org.htmlunit.javascript.host.idb.IDBIndex;
import org.htmlunit.javascript.host.idb.IDBKeyRange;
import org.htmlunit.javascript.host.idb.IDBObjectStore;
import org.htmlunit.javascript.host.idb.IDBOpenDBRequest;
import org.htmlunit.javascript.host.idb.IDBRequest;
import org.htmlunit.javascript.host.idb.IDBTransaction;
import org.htmlunit.javascript.host.media.AnalyserNode;
import org.htmlunit.javascript.host.media.AudioBuffer;
import org.htmlunit.javascript.host.media.AudioBufferSourceNode;
import org.htmlunit.javascript.host.media.AudioContext;
import org.htmlunit.javascript.host.media.AudioDestinationNode;
import org.htmlunit.javascript.host.media.AudioListener;
import org.htmlunit.javascript.host.media.AudioNode;
import org.htmlunit.javascript.host.media.AudioParam;
import org.htmlunit.javascript.host.media.BaseAudioContext;
import org.htmlunit.javascript.host.media.BiquadFilterNode;
import org.htmlunit.javascript.host.media.ChannelMergerNode;
import org.htmlunit.javascript.host.media.ChannelSplitterNode;
import org.htmlunit.javascript.host.media.ConstantSourceNode;
import org.htmlunit.javascript.host.media.ConvolverNode;
import org.htmlunit.javascript.host.media.DelayNode;
import org.htmlunit.javascript.host.media.DynamicsCompressorNode;
import org.htmlunit.javascript.host.media.GainNode;
import org.htmlunit.javascript.host.media.IIRFilterNode;
import org.htmlunit.javascript.host.media.InputDeviceCapabilities;
import org.htmlunit.javascript.host.media.MediaDeviceInfo;
import org.htmlunit.javascript.host.media.MediaDevices;
import org.htmlunit.javascript.host.media.MediaElementAudioSourceNode;
import org.htmlunit.javascript.host.media.MediaError;
import org.htmlunit.javascript.host.media.MediaKeyError;
import org.htmlunit.javascript.host.media.MediaKeySession;
import org.htmlunit.javascript.host.media.MediaKeyStatusMap;
import org.htmlunit.javascript.host.media.MediaKeySystemAccess;
import org.htmlunit.javascript.host.media.MediaKeys;
import org.htmlunit.javascript.host.media.MediaRecorder;
import org.htmlunit.javascript.host.media.MediaSource;
import org.htmlunit.javascript.host.media.MediaStream;
import org.htmlunit.javascript.host.media.MediaStreamAudioDestinationNode;
import org.htmlunit.javascript.host.media.MediaStreamAudioSourceNode;
import org.htmlunit.javascript.host.media.MediaStreamTrack;
import org.htmlunit.javascript.host.media.OfflineAudioContext;
import org.htmlunit.javascript.host.media.OscillatorNode;
import org.htmlunit.javascript.host.media.PannerNode;
import org.htmlunit.javascript.host.media.PeriodicSyncManager;
import org.htmlunit.javascript.host.media.PeriodicWave;
import org.htmlunit.javascript.host.media.RemotePlayback;
import org.htmlunit.javascript.host.media.ScriptProcessorNode;
import org.htmlunit.javascript.host.media.SourceBuffer;
import org.htmlunit.javascript.host.media.SourceBufferList;
import org.htmlunit.javascript.host.media.StereoPannerNode;
import org.htmlunit.javascript.host.media.TextTrack;
import org.htmlunit.javascript.host.media.TextTrackCue;
import org.htmlunit.javascript.host.media.TextTrackCueList;
import org.htmlunit.javascript.host.media.TextTrackList;
import org.htmlunit.javascript.host.media.TimeRanges;
import org.htmlunit.javascript.host.media.VTTCue;
import org.htmlunit.javascript.host.media.VideoPlaybackQuality;
import org.htmlunit.javascript.host.media.WaveShaperNode;
import org.htmlunit.javascript.host.media.midi.MIDIAccess;
import org.htmlunit.javascript.host.media.midi.MIDIInput;
import org.htmlunit.javascript.host.media.midi.MIDIInputMap;
import org.htmlunit.javascript.host.media.midi.MIDIOutput;
import org.htmlunit.javascript.host.media.midi.MIDIOutputMap;
import org.htmlunit.javascript.host.media.midi.MIDIPort;
import org.htmlunit.javascript.host.media.presentation.Presentation;
import org.htmlunit.javascript.host.media.presentation.PresentationAvailability;
import org.htmlunit.javascript.host.media.presentation.PresentationConnection;
import org.htmlunit.javascript.host.media.presentation.PresentationRequest;
import org.htmlunit.javascript.host.media.rtc.RTCCertificate;
import org.htmlunit.javascript.host.media.rtc.RTCIceCandidate;
import org.htmlunit.javascript.host.media.rtc.RTCPeerConnection;
import org.htmlunit.javascript.host.media.rtc.RTCSctpTransport;
import org.htmlunit.javascript.host.media.rtc.RTCSessionDescription;
import org.htmlunit.javascript.host.media.rtc.RTCStatsReport;
import org.htmlunit.javascript.host.network.NetworkInformation;
import org.htmlunit.javascript.host.payment.PaymentAddress;
import org.htmlunit.javascript.host.payment.PaymentRequest;
import org.htmlunit.javascript.host.payment.PaymentResponse;
import org.htmlunit.javascript.host.performance.Performance;
import org.htmlunit.javascript.host.performance.PerformanceEntry;
import org.htmlunit.javascript.host.performance.PerformanceMark;
import org.htmlunit.javascript.host.performance.PerformanceMeasure;
import org.htmlunit.javascript.host.performance.PerformanceNavigation;
import org.htmlunit.javascript.host.performance.PerformanceNavigationTiming;
import org.htmlunit.javascript.host.performance.PerformanceResourceTiming;
import org.htmlunit.javascript.host.performance.PerformanceTiming;
import org.htmlunit.javascript.host.security.Credential;
import org.htmlunit.javascript.host.security.CredentialsContainer;
import org.htmlunit.javascript.host.security.FederatedCredential;
import org.htmlunit.javascript.host.security.PasswordCredential;
import org.htmlunit.javascript.host.speech.SpeechSynthesis;
import org.htmlunit.javascript.host.speech.SpeechSynthesisErrorEvent;
import org.htmlunit.javascript.host.speech.SpeechSynthesisUtterance;
import org.htmlunit.javascript.host.speech.SpeechSynthesisVoice;
import org.htmlunit.javascript.host.speech.WebkitSpeechGrammar;
import org.htmlunit.javascript.host.speech.WebkitSpeechGrammarList;
import org.htmlunit.javascript.host.speech.WebkitSpeechRecognition;
import org.htmlunit.javascript.host.svg.SVGAElement;
import org.htmlunit.javascript.host.svg.SVGAngle;
import org.htmlunit.javascript.host.svg.SVGAnimateElement;
import org.htmlunit.javascript.host.svg.SVGAnimateMotionElement;
import org.htmlunit.javascript.host.svg.SVGAnimateTransformElement;
import org.htmlunit.javascript.host.svg.SVGAnimatedAngle;
import org.htmlunit.javascript.host.svg.SVGAnimatedBoolean;
import org.htmlunit.javascript.host.svg.SVGAnimatedEnumeration;
import org.htmlunit.javascript.host.svg.SVGAnimatedInteger;
import org.htmlunit.javascript.host.svg.SVGAnimatedLength;
import org.htmlunit.javascript.host.svg.SVGAnimatedLengthList;
import org.htmlunit.javascript.host.svg.SVGAnimatedNumber;
import org.htmlunit.javascript.host.svg.SVGAnimatedNumberList;
import org.htmlunit.javascript.host.svg.SVGAnimatedPreserveAspectRatio;
import org.htmlunit.javascript.host.svg.SVGAnimatedRect;
import org.htmlunit.javascript.host.svg.SVGAnimatedString;
import org.htmlunit.javascript.host.svg.SVGAnimatedTransformList;
import org.htmlunit.javascript.host.svg.SVGAnimationElement;
import org.htmlunit.javascript.host.svg.SVGCircleElement;
import org.htmlunit.javascript.host.svg.SVGClipPathElement;
import org.htmlunit.javascript.host.svg.SVGComponentTransferFunctionElement;
import org.htmlunit.javascript.host.svg.SVGDefsElement;
import org.htmlunit.javascript.host.svg.SVGDescElement;
import org.htmlunit.javascript.host.svg.SVGElement;
import org.htmlunit.javascript.host.svg.SVGEllipseElement;
import org.htmlunit.javascript.host.svg.SVGFEBlendElement;
import org.htmlunit.javascript.host.svg.SVGFEColorMatrixElement;
import org.htmlunit.javascript.host.svg.SVGFEComponentTransferElement;
import org.htmlunit.javascript.host.svg.SVGFECompositeElement;
import org.htmlunit.javascript.host.svg.SVGFEConvolveMatrixElement;
import org.htmlunit.javascript.host.svg.SVGFEDiffuseLightingElement;
import org.htmlunit.javascript.host.svg.SVGFEDisplacementMapElement;
import org.htmlunit.javascript.host.svg.SVGFEDistantLightElement;
import org.htmlunit.javascript.host.svg.SVGFEDropShadowElement;
import org.htmlunit.javascript.host.svg.SVGFEFloodElement;
import org.htmlunit.javascript.host.svg.SVGFEFuncAElement;
import org.htmlunit.javascript.host.svg.SVGFEFuncBElement;
import org.htmlunit.javascript.host.svg.SVGFEFuncGElement;
import org.htmlunit.javascript.host.svg.SVGFEFuncRElement;
import org.htmlunit.javascript.host.svg.SVGFEGaussianBlurElement;
import org.htmlunit.javascript.host.svg.SVGFEImageElement;
import org.htmlunit.javascript.host.svg.SVGFEMergeElement;
import org.htmlunit.javascript.host.svg.SVGFEMergeNodeElement;
import org.htmlunit.javascript.host.svg.SVGFEMorphologyElement;
import org.htmlunit.javascript.host.svg.SVGFEOffsetElement;
import org.htmlunit.javascript.host.svg.SVGFEPointLightElement;
import org.htmlunit.javascript.host.svg.SVGFESpecularLightingElement;
import org.htmlunit.javascript.host.svg.SVGFESpotLightElement;
import org.htmlunit.javascript.host.svg.SVGFETileElement;
import org.htmlunit.javascript.host.svg.SVGFETurbulenceElement;
import org.htmlunit.javascript.host.svg.SVGFilterElement;
import org.htmlunit.javascript.host.svg.SVGForeignObjectElement;
import org.htmlunit.javascript.host.svg.SVGGElement;
import org.htmlunit.javascript.host.svg.SVGGeometryElement;
import org.htmlunit.javascript.host.svg.SVGGradientElement;
import org.htmlunit.javascript.host.svg.SVGGraphicsElement;
import org.htmlunit.javascript.host.svg.SVGImageElement;
import org.htmlunit.javascript.host.svg.SVGLength;
import org.htmlunit.javascript.host.svg.SVGLengthList;
import org.htmlunit.javascript.host.svg.SVGLineElement;
import org.htmlunit.javascript.host.svg.SVGLinearGradientElement;
import org.htmlunit.javascript.host.svg.SVGMPathElement;
import org.htmlunit.javascript.host.svg.SVGMarkerElement;
import org.htmlunit.javascript.host.svg.SVGMaskElement;
import org.htmlunit.javascript.host.svg.SVGMatrix;
import org.htmlunit.javascript.host.svg.SVGMetadataElement;
import org.htmlunit.javascript.host.svg.SVGNumber;
import org.htmlunit.javascript.host.svg.SVGNumberList;
import org.htmlunit.javascript.host.svg.SVGPathElement;
import org.htmlunit.javascript.host.svg.SVGPatternElement;
import org.htmlunit.javascript.host.svg.SVGPoint;
import org.htmlunit.javascript.host.svg.SVGPointList;
import org.htmlunit.javascript.host.svg.SVGPolygonElement;
import org.htmlunit.javascript.host.svg.SVGPolylineElement;
import org.htmlunit.javascript.host.svg.SVGPreserveAspectRatio;
import org.htmlunit.javascript.host.svg.SVGRadialGradientElement;
import org.htmlunit.javascript.host.svg.SVGRect;
import org.htmlunit.javascript.host.svg.SVGRectElement;
import org.htmlunit.javascript.host.svg.SVGSVGElement;
import org.htmlunit.javascript.host.svg.SVGScriptElement;
import org.htmlunit.javascript.host.svg.SVGSetElement;
import org.htmlunit.javascript.host.svg.SVGStopElement;
import org.htmlunit.javascript.host.svg.SVGStringList;
import org.htmlunit.javascript.host.svg.SVGStyleElement;
import org.htmlunit.javascript.host.svg.SVGSwitchElement;
import org.htmlunit.javascript.host.svg.SVGSymbolElement;
import org.htmlunit.javascript.host.svg.SVGTSpanElement;
import org.htmlunit.javascript.host.svg.SVGTextContentElement;
import org.htmlunit.javascript.host.svg.SVGTextElement;
import org.htmlunit.javascript.host.svg.SVGTextPathElement;
import org.htmlunit.javascript.host.svg.SVGTextPositioningElement;
import org.htmlunit.javascript.host.svg.SVGTitleElement;
import org.htmlunit.javascript.host.svg.SVGTransform;
import org.htmlunit.javascript.host.svg.SVGTransformList;
import org.htmlunit.javascript.host.svg.SVGUnitTypes;
import org.htmlunit.javascript.host.svg.SVGUseElement;
import org.htmlunit.javascript.host.svg.SVGViewElement;
import org.htmlunit.javascript.host.worker.ServiceWorker;
import org.htmlunit.javascript.host.worker.ServiceWorkerContainer;
import org.htmlunit.javascript.host.worker.ServiceWorkerRegistration;
import org.htmlunit.javascript.host.worker.SyncManager;
import org.htmlunit.javascript.host.worker.Worker;
import org.htmlunit.javascript.host.xml.FormData;
import org.htmlunit.javascript.host.xml.XMLDocument;
import org.htmlunit.javascript.host.xml.XMLHttpRequest;
import org.htmlunit.javascript.host.xml.XMLHttpRequestEventTarget;
import org.htmlunit.javascript.host.xml.XMLHttpRequestUpload;
import org.htmlunit.javascript.host.xml.XMLSerializer;
import org.htmlunit.javascript.host.xml.XSLTProcessor;

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
        AbortController.class, AbortSignal.class,
        AbstractList.class, AbstractRange.class, AnalyserNode.class,
        Animation.class, AnimationEvent.class, Atomics.class,
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
        ConstantSourceNode.class, ConvolverNode.class,
        Credential.class, CredentialsContainer.class, Crypto.class,
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
        DOMPointReadOnly.class, DOMRectReadOnly.class,
        DOMStringList.class, DOMStringMap.class, DOMTokenList.class,
        DragEvent.class, DynamicsCompressorNode.class,
        Element.class, ErrorEvent.class, Event.class, EventSource.class,
        EventTarget.class, External.class, FederatedCredential.class,
        File.class, FileList.class, FileReader.class,
        FileSystem.class, FileSystemDirectoryEntry.class, FileSystemDirectoryReader.class,
        FileSystemEntry.class, FileSystemFileEntry.class,
        FocusEvent.class, FontFace.class,
        FontFaceSet.class, FormData.class, GainNode.class, Gamepad.class,
        GamepadButton.class, GamepadEvent.class,
        Geolocation.class, GeolocationCoordinates.class, GeolocationPosition.class, GeolocationPositionError.class,
        HashChangeEvent.class, Headers.class, History.class,
        HTMLAllCollection.class, HTMLAnchorElement.class, HTMLAreaElement.class, HTMLAudioElement.class,
        HTMLBaseElement.class,
        HTMLBodyElement.class, HTMLBRElement.class, HTMLButtonElement.class,
        HTMLCanvasElement.class, HTMLCollection.class,
        HTMLDataElement.class, HTMLDataListElement.class,
        HTMLDetailsElement.class, HTMLDialogElement.class, HTMLDirectoryElement.class,
        HTMLDivElement.class, HTMLDListElement.class, HTMLDocument.class, HTMLElement.class,
        HTMLEmbedElement.class, HTMLFieldSetElement.class,
        HTMLFontElement.class, HTMLFormControlsCollection.class, HTMLFormElement.class, HTMLFrameElement.class,
        HTMLFrameSetElement.class,
        HTMLHeadElement.class, HTMLHeadingElement.class, HTMLHRElement.class, HTMLHtmlElement.class,
        HTMLIFrameElement.class, HTMLImageElement.class, HTMLInputElement.class, HTMLLabelElement.class,
        HTMLLegendElement.class, HTMLLIElement.class, HTMLLinkElement.class, HTMLListElement.class,
        HTMLMapElement.class, HTMLMarqueeElement.class,
        HTMLMediaElement.class, HTMLMenuElement.class, HTMLMetaElement.class,
        HTMLMeterElement.class, HTMLModElement.class,
        HTMLObjectElement.class, HTMLOListElement.class, HTMLOptGroupElement.class,
        HTMLOptionElement.class, HTMLOptionsCollection.class, HTMLOutputElement.class,
        HTMLParagraphElement.class, HTMLParamElement.class, HTMLPictureElement.class,
        HTMLPreElement.class, HTMLProgressElement.class, HTMLQuoteElement.class, HTMLScriptElement.class,
        HTMLSelectElement.class, HTMLSlotElement.class, HTMLSourceElement.class,
        HTMLSpanElement.class,
        HTMLStyleElement.class, HTMLTableCaptionElement.class, HTMLTableCellElement.class, HTMLTableColElement.class,
        HTMLTableComponent.class, HTMLTableElement.class,
        HTMLTableRowElement.class, HTMLTableSectionElement.class,
        HTMLTemplateElement.class, HTMLTextAreaElement.class, HTMLTimeElement.class,
        HTMLTitleElement.class, HTMLTrackElement.class, HTMLUListElement.class, HTMLUnknownElement.class,
        HTMLVideoElement.class,
        IDBCursor.class, IDBCursorWithValue.class, IDBDatabase.class, IDBFactory.class, IDBIndex.class,
        IDBKeyRange.class, IDBObjectStore.class, IDBOpenDBRequest.class, IDBRequest.class,
        IDBTransaction.class, IDBVersionChangeEvent.class, IdleDeadline.class, IIRFilterNode.class,
        ImageBitmap.class, ImageBitmapRenderingContext.class, ImageData.class,
        InputDeviceCapabilities.class, InputEvent.class,
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
        MutationEvent.class, MutationObserver.class, MutationRecord.class, NamedNodeMap.class,
        Navigator.class, NetworkInformation.class, Node.class, NodeFilter.class, NodeIterator.class,
        NodeList.class, Notification.class,
        OfflineAudioCompletionEvent.class,
        OfflineAudioContext.class, OscillatorNode.class, PageTransitionEvent.class, PannerNode.class,
        PasswordCredential.class,
        Path2D.class,
        PaymentAddress.class, PaymentRequest.class, PaymentResponse.class,
        Performance.class, PerformanceEntry.class, PerformanceMark.class,
        PerformanceMeasure.class, PerformanceNavigation.class, PerformanceNavigationTiming.class,
        PerformanceObserver.class, PerformanceObserverEntryList.class,
        PerformanceResourceTiming.class, PerformanceTiming.class, PeriodicSyncManager.class, PeriodicWave.class,
        Permissions.class, PermissionStatus.class, Plugin.class, PluginArray.class,
        PointerEvent.class, PopStateEvent.class, Presentation.class,
        PresentationAvailability.class, PresentationConnection.class, PresentationConnectionAvailableEvent.class,
        PresentationConnectionCloseEvent.class, PresentationRequest.class,
        ProcessingInstruction.class, ProgressEvent.class, PromiseRejectionEvent.class,
        PushManager.class,
        PushSubscription.class, PushSubscriptionOptions.class, RadioNodeList.class, Range.class, ReadableStream.class,
        RemotePlayback.class, Request.class, Response.class, RowContainer.class, RTCCertificate.class,
        RTCDataChannelEvent.class, RTCIceCandidate.class, RTCPeerConnection.class, RTCPeerConnectionIceEvent.class,
        RTCSctpTransport.class, RTCSessionDescription.class, RTCStatsReport.class,
        Screen.class, ScreenOrientation.class, ScriptProcessorNode.class,
        SecurityPolicyViolationEvent.class, Selection.class, ServiceWorker.class, ServiceWorkerContainer.class,
        ServiceWorkerRegistration.class,
        ShadowRoot.class, SharedWorker.class, SourceBuffer.class, SourceBufferList.class,
        SpeechSynthesis.class, SpeechSynthesisErrorEvent.class, SpeechSynthesisEvent.class,
        SpeechSynthesisUtterance.class, SpeechSynthesisVoice.class,
        StereoPannerNode.class, Storage.class, StorageEvent.class, StorageManager.class,
        StyleMedia.class, StyleSheet.class, StyleSheetList.class, SubmitEvent.class, SubtleCrypto.class,
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
        SVGPathElement.class,
        SVGPatternElement.class,
        SVGPoint.class, SVGPointList.class, SVGPolygonElement.class, SVGPolylineElement.class,
        SVGPreserveAspectRatio.class, SVGRadialGradientElement.class, SVGRect.class, SVGRectElement.class,
        SVGScriptElement.class, SVGSetElement.class, SVGStopElement.class,
        SVGStringList.class, SVGStyleElement.class, SVGSVGElement.class, SVGSwitchElement.class,
        SVGSymbolElement.class, SVGTextContentElement.class, SVGTextElement.class,
        SVGTextPathElement.class, SVGTextPositioningElement.class, SVGTitleElement.class, SVGTransform.class,
        SVGTransformList.class, SVGTSpanElement.class, SVGUnitTypes.class, SVGUseElement.class, SVGViewElement.class,
        SyncManager.class, Text.class, TextDecoder.class,
        TextEncoder.class, TextEvent.class, TextMetrics.class, TextTrack.class, TextTrackCue.class,
        TextTrackCueList.class, TextTrackList.class, TimeEvent.class, TimeRanges.class,
        Touch.class, TouchEvent.class, TouchList.class, TrackEvent.class, TransitionEvent.class, TreeWalker.class,
        UIEvent.class,
        URL.class,
        URLSearchParams.class, ValidityState.class, VideoPlaybackQuality.class,
        VTTCue.class, WaveShaperNode.class, WebGL2RenderingContext.class,
        WebGLActiveInfo.class, WebGLBuffer.class,
        WebGLContextEvent.class, WebGLFramebuffer.class, WebGLProgram.class,
        WebGLQuery.class,
        WebGLRenderbuffer.class,
        WebGLRenderingContext.class,
        WebGLSampler.class, WebGLShader.class, WebGLShaderPrecisionFormat.class, WebGLSync.class,
        WebGLTexture.class, WebGLTransformFeedback.class,
        WebGLUniformLocation.class, WebGLVertexArrayObject.class,
        WebkitSpeechGrammar.class,
        WebkitSpeechGrammarList.class, WebkitSpeechRecognition.class, WebkitSpeechRecognitionError.class,
        WebkitSpeechRecognitionEvent.class,
        WebSocket.class, WheelEvent.class, Window.class, Worker.class, XMLDocument.class,
        XMLHttpRequest.class, XMLHttpRequestEventTarget.class, XMLHttpRequestUpload.class, XMLSerializer.class,
        XPathEvaluator.class, XPathEvaluator.class, XPathExpression.class,
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
    @SuppressWarnings("PMD.SingletonClassReturningNewInstance")
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
