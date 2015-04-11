/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.configuration;

import java.util.Map;
import java.util.WeakHashMap;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.NamedNodeMap;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.ActiveXObject;
import com.gargoylesoftware.htmlunit.javascript.host.ApplicationCache;
import com.gargoylesoftware.htmlunit.javascript.host.BoxObject;
import com.gargoylesoftware.htmlunit.javascript.host.ClientRect;
import com.gargoylesoftware.htmlunit.javascript.host.ClientRectList;
import com.gargoylesoftware.htmlunit.javascript.host.ClipboardData;
import com.gargoylesoftware.htmlunit.javascript.host.Console;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Event;
import com.gargoylesoftware.htmlunit.javascript.host.External;
import com.gargoylesoftware.htmlunit.javascript.host.History;
import com.gargoylesoftware.htmlunit.javascript.host.KeyboardEvent;
import com.gargoylesoftware.htmlunit.javascript.host.Location;
import com.gargoylesoftware.htmlunit.javascript.host.MessageChannel;
import com.gargoylesoftware.htmlunit.javascript.host.MessageEvent;
import com.gargoylesoftware.htmlunit.javascript.host.MessagePort;
import com.gargoylesoftware.htmlunit.javascript.host.MimeType;
import com.gargoylesoftware.htmlunit.javascript.host.MimeTypeArray;
import com.gargoylesoftware.htmlunit.javascript.host.MouseEvent;
import com.gargoylesoftware.htmlunit.javascript.host.Namespace;
import com.gargoylesoftware.htmlunit.javascript.host.NamespaceCollection;
import com.gargoylesoftware.htmlunit.javascript.host.Navigator;
import com.gargoylesoftware.htmlunit.javascript.host.Notification;
import com.gargoylesoftware.htmlunit.javascript.host.Plugin;
import com.gargoylesoftware.htmlunit.javascript.host.PluginArray;
import com.gargoylesoftware.htmlunit.javascript.host.Popup;
import com.gargoylesoftware.htmlunit.javascript.host.Promise;
import com.gargoylesoftware.htmlunit.javascript.host.Screen;
import com.gargoylesoftware.htmlunit.javascript.host.SharedWorker;
import com.gargoylesoftware.htmlunit.javascript.host.SimpleArray;
import com.gargoylesoftware.htmlunit.javascript.host.Storage;
import com.gargoylesoftware.htmlunit.javascript.host.URLSearchParams;
import com.gargoylesoftware.htmlunit.javascript.host.WebSocket;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.Worker;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.ArrayBuffer;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.ArrayBufferView;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.ArrayBufferViewBase;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.DataView;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.Float32Array;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.Float64Array;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.Int16Array;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.Int32Array;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.Int8Array;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.Uint16Array;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.Uint32Array;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.Uint8Array;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.Uint8ClampedArray;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.CanvasRenderingContext2D;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.Path2D;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.WebGLRenderingContext;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSS2Properties;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSCharsetRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSFontFaceRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSImportRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSMediaRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSPageRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSPrimitiveValue;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSRuleList;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSValue;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleSheetList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Attr;
import com.gargoylesoftware.htmlunit.javascript.host.dom.CDATASection;
import com.gargoylesoftware.htmlunit.javascript.host.dom.CharacterDataImpl;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Comment;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMCursor;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMException;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMImplementation;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMParser;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMStringMap;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMTokenList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DocumentFragment;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DocumentType;
import com.gargoylesoftware.htmlunit.javascript.host.dom.EventNode;
import com.gargoylesoftware.htmlunit.javascript.host.dom.MediaList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node;
import com.gargoylesoftware.htmlunit.javascript.host.dom.NodeFilter;
import com.gargoylesoftware.htmlunit.javascript.host.dom.NodeList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.ProcessingInstruction;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Range;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Selection;
import com.gargoylesoftware.htmlunit.javascript.host.dom.ShadowRoot;
import com.gargoylesoftware.htmlunit.javascript.host.dom.StaticNodeList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Text;
import com.gargoylesoftware.htmlunit.javascript.host.dom.TextRange;
import com.gargoylesoftware.htmlunit.javascript.host.dom.TreeWalker;
import com.gargoylesoftware.htmlunit.javascript.host.dom.XPathEvaluator;
import com.gargoylesoftware.htmlunit.javascript.host.dom.XPathNSResolver;
import com.gargoylesoftware.htmlunit.javascript.host.dom.XPathResult;
import com.gargoylesoftware.htmlunit.javascript.host.event.BeforeUnloadEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.HashChangeEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.MutationEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.PointerEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.UIEvent;
import com.gargoylesoftware.htmlunit.javascript.host.geo.Coordinates;
import com.gargoylesoftware.htmlunit.javascript.host.geo.Geolocation;
import com.gargoylesoftware.htmlunit.javascript.host.geo.Position;
import com.gargoylesoftware.htmlunit.javascript.host.html.*;
import com.gargoylesoftware.htmlunit.javascript.host.media.AnalyserNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.AudioBuffer;
import com.gargoylesoftware.htmlunit.javascript.host.media.AudioBufferSourceNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.AudioContext;
import com.gargoylesoftware.htmlunit.javascript.host.media.AudioDestinationNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.AudioListener;
import com.gargoylesoftware.htmlunit.javascript.host.media.AudioNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.AudioParam;
import com.gargoylesoftware.htmlunit.javascript.host.media.AudioProcessingEvent;
import com.gargoylesoftware.htmlunit.javascript.host.media.BiquadFilterNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.ChannelMergerNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.ChannelSplitterNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.ConvolverNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.DelayNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.DynamicsCompressorNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.GainNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.LocalMediaStream;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaStreamAudioDestinationNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaStreamAudioSourceNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.MediaStreamEvent;
import com.gargoylesoftware.htmlunit.javascript.host.media.OfflineAudioCompletionEvent;
import com.gargoylesoftware.htmlunit.javascript.host.media.OfflineAudioContext;
import com.gargoylesoftware.htmlunit.javascript.host.media.OscillatorNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.PannerNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.PeriodicWave;
import com.gargoylesoftware.htmlunit.javascript.host.media.ScriptProcessorNode;
import com.gargoylesoftware.htmlunit.javascript.host.media.WaveShaperNode;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAltGlyphElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAngle;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimateElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimateMotionElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimateTransformElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimatedAngle;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimatedBoolean;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimatedEnumeration;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimatedInteger;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimatedLength;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimatedLengthList;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimatedNumber;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimatedNumberList;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimatedPreserveAspectRatio;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimatedRect;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimatedString;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimatedTransformList;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimationElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGCircleElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGClipPathElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGCursorElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGDefsElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGDescElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGDocument;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGEllipseElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEBlendElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEColorMatrixElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEComponentTransferElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFECompositeElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEConvolveMatrixElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEDiffuseLightingElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEDisplacementMapElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEDistantLightElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFloodElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFuncAElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFuncBElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFuncGElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEFuncRElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEGaussianBlurElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEImageElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEMergeElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEMergeNodeElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEMorphologyElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEOffsetElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFEPointLightElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFESpecularLightingElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFESpotLightElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFETileElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFETurbulenceElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGFilterElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGForeignObjectElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGGElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGGradientElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGImageElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGLength;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGLengthList;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGLineElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGLinearGradientElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMPathElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMarkerElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMaskElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMatrix;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMetadataElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGNumber;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGNumberList;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPatternElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPoint;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPolygonElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPolylineElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPreserveAspectRatio;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGRadialGradientElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGRect;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGRectElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGSVGElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGScriptElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGSetElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGStopElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGStringList;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGStyleElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGSwitchElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGSymbolElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTSpanElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTextElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTextPathElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTextPositioningElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTitleElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTransform;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTransformList;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGUseElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGViewElement;
import com.gargoylesoftware.htmlunit.javascript.host.xml.FormData;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLHttpRequest;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLSerializer;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XSLTProcessor;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XSLTemplate;

/**
 * A container for all the JavaScript configuration information.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public final class JavaScriptConfiguration extends AbstractJavaScriptConfiguration {

    @SuppressWarnings("unchecked")
    static final Class<? extends SimpleScriptable>[] CLASSES_ = new Class[] {
        AnalyserNode.class,
        ArrayBuffer.class, ArrayBufferView.class, ArrayBufferViewBase.class,
        Attr.class, ActiveXObject.class, ApplicationCache.class, AudioBuffer.class,
        AudioBufferSourceNode.class, AudioContext.class, AudioDestinationNode.class, AudioListener.class,
        AudioNode.class, AudioParam.class, AudioProcessingEvent.class, BiquadFilterNode.class,
        BeforeUnloadEvent.class, BoxObject.class, CDATASection.class, ChannelMergerNode.class,
        ChannelSplitterNode.class, ClipboardData.class, ConvolverNode.class,
        CSS2Properties.class,
        CSSCharsetRule.class, CSSFontFaceRule.class, CSSImportRule.class, CSSMediaRule.class, CSSPageRule.class,
        CSSPrimitiveValue.class, CSSRule.class,
        CSSRuleList.class, CSSStyleDeclaration.class, CSSStyleRule.class, CSSStyleSheet.class, CSSValue.class,
        CanvasRenderingContext2D.class, CharacterDataImpl.class, ClientRect.class, ClientRectList.class, Comment.class,
        ComputedCSSStyleDeclaration.class, Console.class, Coordinates.class, DataView.class,
        DelayNode.class, DOMCursor.class, DOMException.class,
        DOMImplementation.class, DOMParser.class, DOMStringMap.class,
        DOMTokenList.class, Document.class, DocumentFragment.class,
        DocumentType.class, DynamicsCompressorNode.class,
        Element.class, Enumerator.class, Event.class, EventNode.class, External.class,
        Float32Array.class, Float64Array.class,
        FormChild.class, FormData.class, FormField.class, GainNode.class, Geolocation.class,
        HashChangeEvent.class, History.class,
        HTMLAllCollection.class,
        HTMLAnchorElement.class, HTMLAppletElement.class, HTMLAreaElement.class, HTMLAudioElement.class,
        HTMLBGSoundElement.class,
        HTMLBRElement.class, HTMLBaseElement.class, HTMLBaseFontElement.class,
        HTMLBlockElement.class,
        HTMLQuoteElement.class, HTMLBodyElement.class, HTMLButtonElement.class, HTMLCanvasElement.class,
        HTMLCollection.class, HTMLCommentElement.class, HTMLContentElement.class,
        HTMLDataElement.class, HTMLDataListElement.class,
        HTMLDDElement.class, HTMLDetailsElement.class, HTMLDialogElement.class, HTMLDTElement.class,
        HTMLDListElement.class,
        HTMLDirectoryElement.class,
        HTMLDivElement.class, HTMLDocument.class, HTMLElement.class, HTMLEmbedElement.class, HTMLFieldSetElement.class,
        HTMLFontElement.class, HTMLFormElement.class, HTMLFrameElement.class, HTMLFrameSetElement.class,
        HTMLHRElement.class, HTMLHeadElement.class, HTMLHeadingElement.class, HTMLHtmlElement.class,
        HTMLIFrameElement.class, HTMLImageElement.class, HTMLInlineQuotationElement.class, HTMLInputElement.class,
        HTMLIsIndexElement.class,
        HTMLKeygenElement.class,
        HTMLLIElement.class, HTMLLabelElement.class,
        HTMLLegendElement.class, HTMLLinkElement.class, HTMLListElement.class, HTMLMapElement.class,
        HTMLMarqueeElement.class,
        HTMLMediaElement.class, HTMLMenuElement.class, HTMLMenuItemElement.class, HTMLMetaElement.class,
        HTMLMeterElement.class, HTMLModElement.class, HTMLNoShowElement.class,
        HTMLNextIdElement.class,
        HTMLOListElement.class, HTMLObjectElement.class, HTMLOptGroupElement.class,
        HTMLOptionElement.class, HTMLOptionsCollection.class, HTMLOutputElement.class,
        HTMLParagraphElement.class, HTMLParamElement.class,
        HTMLPhraseElement.class,
        HTMLPreElement.class, HTMLProgressElement.class, HTMLScriptElement.class,
        HTMLSelectElement.class, HTMLShadowElement.class, HTMLSourceElement.class, HTMLSpanElement.class,
        HTMLStyleElement.class, HTMLTableCaptionElement.class, HTMLTableCellElement.class, HTMLTableColElement.class,
        HTMLTableComponent.class, HTMLTableDataCellElement.class, HTMLTableElement.class,
        HTMLTableHeaderCellElement.class, HTMLTableRowElement.class, HTMLTableSectionElement.class,
        HTMLTextElement.class, HTMLTextAreaElement.class, HTMLTimeElement.class, HTMLTitleElement.class,
        HTMLTrackElement.class, HTMLUListElement.class, HTMLUnknownElement.class,
        HTMLVideoElement.class,
        Image.class,
        Int16Array.class, Int32Array.class, Int8Array.class,
        KeyboardEvent.class, LocalMediaStream.class,
        Location.class, MediaList.class, MediaStreamAudioDestinationNode.class, MediaStreamAudioSourceNode.class,
        MediaStreamEvent.class, MessageChannel.class,
        MessageEvent.class, MessagePort.class, MimeType.class, MimeTypeArray.class, MouseEvent.class,
        MutationEvent.class, NamedNodeMap.class, Namespace.class, NamespaceCollection.class, Navigator.class,
        Node.class, NodeFilter.class, NodeList.class, Notification.class, OfflineAudioCompletionEvent.class,
        OfflineAudioContext.class, Option.class, OscillatorNode.class, PannerNode.class, Path2D.class,
        PeriodicWave.class, Plugin.class, PluginArray.class, PointerEvent.class, Popup.class, Position.class,
        ProcessingInstruction.class,
        Promise.class, Range.class, RowContainer.class, ScriptProcessorNode.class, ShadowRoot.class,
        SharedWorker.class,
        SVGAElement.class, SVGAltGlyphElement.class, SVGAngle.class, SVGAnimatedAngle.class,
        SVGAnimatedBoolean.class, SVGAnimateElement.class, SVGAnimatedEnumeration.class, SVGAnimatedInteger.class,
        SVGAnimatedLength.class, SVGAnimatedLengthList.class, SVGAnimatedNumber.class, SVGAnimatedNumberList.class,
        SVGAnimatedPreserveAspectRatio.class, SVGAnimatedRect.class, SVGAnimatedString.class,
        SVGAnimatedTransformList.class,
        SVGAnimateMotionElement.class, SVGAnimateTransformElement.class, SVGAnimationElement.class,
        SVGCircleElement.class,
        SVGClipPathElement.class, SVGCursorElement.class, SVGDefsElement.class, SVGDescElement.class,
        SVGDocument.class, SVGElement.class,
        SVGEllipseElement.class, SVGFEBlendElement.class, SVGFEColorMatrixElement.class,
        SVGFEComponentTransferElement.class, SVGFECompositeElement.class, SVGFEConvolveMatrixElement.class,
        SVGFEDiffuseLightingElement.class, SVGFEDisplacementMapElement.class, SVGFEDistantLightElement.class,
        SVGFEFloodElement.class, SVGFEFuncAElement.class, SVGFEFuncBElement.class, SVGFEFuncGElement.class,
        SVGFEFuncRElement.class, SVGFEGaussianBlurElement.class, SVGFEImageElement.class, SVGFEMergeElement.class,
        SVGFEMergeNodeElement.class, SVGFEMorphologyElement.class, SVGFEOffsetElement.class,
        SVGFEPointLightElement.class, SVGFESpecularLightingElement.class, SVGFESpotLightElement.class,
        SVGFETileElement.class, SVGFETurbulenceElement.class, SVGFilterElement.class, SVGForeignObjectElement.class,
        SVGGElement.class, SVGGradientElement.class,
        SVGImageElement.class, SVGLength.class, SVGLengthList.class, SVGLineElement.class,
        SVGLinearGradientElement.class, SVGMarkerElement.class, SVGMaskElement.class, SVGMatrix.class,
        SVGMetadataElement.class, SVGNumber.class, SVGNumberList.class,
        SVGMPathElement.class, SVGPathElement.class, SVGPatternElement.class,
        SVGPoint.class, SVGPolygonElement.class, SVGPolylineElement.class, SVGPreserveAspectRatio.class,
        SVGRadialGradientElement.class, SVGRect.class, SVGRectElement.class,
        SVGSVGElement.class, SVGScriptElement.class,
        SVGSetElement.class, SVGStopElement.class, SVGStringList.class,
        SVGStyleElement.class, SVGSwitchElement.class,
        SVGSymbolElement.class, SVGTSpanElement.class, SVGTextElement.class, SVGTextPathElement.class,
        SVGTextPositioningElement.class, SVGTitleElement.class, SVGTransform.class, SVGTransformList.class,
        SVGUseElement.class, SVGViewElement.class,
        Screen.class, Selection.class, SimpleArray.class,
        StaticNodeList.class, Storage.class, StyleSheetList.class, Text.class, TextRange.class, TreeWalker.class,
        UIEvent.class, Uint16Array.class, Uint32Array.class, Uint8Array.class, Uint8ClampedArray.class,
        URLSearchParams.class, WaveShaperNode.class, WebGLRenderingContext.class,
        WebSocket.class, Window.class, Worker.class, XMLDocument.class,
        XMLHttpRequest.class, XMLSerializer.class, XPathEvaluator.class, XPathNSResolver.class, XPathResult.class,
        XSLTProcessor.class, XSLTemplate.class};

    /** Cache of browser versions and their corresponding JavaScript configurations. */
    private static final Map<BrowserVersion, JavaScriptConfiguration> CONFIGURATION_MAP_ =
        new WeakHashMap<>();

    /**
     * Constructor is only called from {@link #getInstance(BrowserVersion)} which is synchronized.
     * @param browser the browser version to use
     */
    protected JavaScriptConfiguration(final BrowserVersion browser) {
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
            throw new IllegalStateException("BrowserVersion must be defined");
        }
        JavaScriptConfiguration configuration = CONFIGURATION_MAP_.get(browserVersion);

        if (configuration == null) {
            configuration = new JavaScriptConfiguration(browserVersion);
            CONFIGURATION_MAP_.put(browserVersion, configuration);
        }
        return configuration;
    }

    @Override
    protected Class<? extends SimpleScriptable>[] getClasses() {
        return CLASSES_;
    }
}
