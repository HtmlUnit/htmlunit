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
package com.gargoylesoftware.htmlunit.javascript.configuration;

import java.util.Map;
import java.util.WeakHashMap;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.NamedNodeMap;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.ActiveXObject;
import com.gargoylesoftware.htmlunit.javascript.host.Attr;
import com.gargoylesoftware.htmlunit.javascript.host.BoxObject;
import com.gargoylesoftware.htmlunit.javascript.host.CDATASection;
import com.gargoylesoftware.htmlunit.javascript.host.CharacterDataImpl;
import com.gargoylesoftware.htmlunit.javascript.host.ClientRect;
import com.gargoylesoftware.htmlunit.javascript.host.ClipboardData;
import com.gargoylesoftware.htmlunit.javascript.host.Comment;
import com.gargoylesoftware.htmlunit.javascript.host.Console;
import com.gargoylesoftware.htmlunit.javascript.host.Document;
import com.gargoylesoftware.htmlunit.javascript.host.DocumentFragment;
import com.gargoylesoftware.htmlunit.javascript.host.DocumentType;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Enumerator;
import com.gargoylesoftware.htmlunit.javascript.host.Event;
import com.gargoylesoftware.htmlunit.javascript.host.EventNode;
import com.gargoylesoftware.htmlunit.javascript.host.External;
import com.gargoylesoftware.htmlunit.javascript.host.FormChild;
import com.gargoylesoftware.htmlunit.javascript.host.FormField;
import com.gargoylesoftware.htmlunit.javascript.host.HashChangeEvent;
import com.gargoylesoftware.htmlunit.javascript.host.History;
import com.gargoylesoftware.htmlunit.javascript.host.KeyboardEvent;
import com.gargoylesoftware.htmlunit.javascript.host.Location;
import com.gargoylesoftware.htmlunit.javascript.host.MediaList;
import com.gargoylesoftware.htmlunit.javascript.host.MessageEvent;
import com.gargoylesoftware.htmlunit.javascript.host.MimeType;
import com.gargoylesoftware.htmlunit.javascript.host.MimeTypeArray;
import com.gargoylesoftware.htmlunit.javascript.host.MouseEvent;
import com.gargoylesoftware.htmlunit.javascript.host.MutationEvent;
import com.gargoylesoftware.htmlunit.javascript.host.Namespace;
import com.gargoylesoftware.htmlunit.javascript.host.NamespaceCollection;
import com.gargoylesoftware.htmlunit.javascript.host.Navigator;
import com.gargoylesoftware.htmlunit.javascript.host.Node;
import com.gargoylesoftware.htmlunit.javascript.host.NodeFilter;
import com.gargoylesoftware.htmlunit.javascript.host.NodeList;
import com.gargoylesoftware.htmlunit.javascript.host.ApplicationCache;
import com.gargoylesoftware.htmlunit.javascript.host.Plugin;
import com.gargoylesoftware.htmlunit.javascript.host.PluginArray;
import com.gargoylesoftware.htmlunit.javascript.host.Popup;
import com.gargoylesoftware.htmlunit.javascript.host.ProcessingInstruction;
import com.gargoylesoftware.htmlunit.javascript.host.Range;
import com.gargoylesoftware.htmlunit.javascript.host.RowContainer;
import com.gargoylesoftware.htmlunit.javascript.host.Screen;
import com.gargoylesoftware.htmlunit.javascript.host.Selection;
import com.gargoylesoftware.htmlunit.javascript.host.SimpleArray;
import com.gargoylesoftware.htmlunit.javascript.host.StaticNodeList;
import com.gargoylesoftware.htmlunit.javascript.host.Storage;
import com.gargoylesoftware.htmlunit.javascript.host.Text;
import com.gargoylesoftware.htmlunit.javascript.host.TextRange;
import com.gargoylesoftware.htmlunit.javascript.host.TreeWalker;
import com.gargoylesoftware.htmlunit.javascript.host.UIEvent;
import com.gargoylesoftware.htmlunit.javascript.host.WebSocket;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.XPathEvaluator;
import com.gargoylesoftware.htmlunit.javascript.host.XPathNSResolver;
import com.gargoylesoftware.htmlunit.javascript.host.XPathResult;
import com.gargoylesoftware.htmlunit.javascript.host.XSLTProcessor;
import com.gargoylesoftware.htmlunit.javascript.host.XSLTemplate;
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
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSCharsetRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSFontFaceRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSImportRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSMediaRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSPrimitiveValue;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSRuleList;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleRule;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSValue;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleSheetList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMException;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMImplementation;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMParser;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMStringMap;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMTokenList;
import com.gargoylesoftware.htmlunit.javascript.host.geo.Coordinates;
import com.gargoylesoftware.htmlunit.javascript.host.geo.Geolocation;
import com.gargoylesoftware.htmlunit.javascript.host.geo.Position;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAnchorElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAppletElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAreaElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAudioElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBGSoundElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBRElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBaseElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBaseFontElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBlockElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBlockQuoteElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLButtonElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCanvasElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollectionTags;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDListElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDataListElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDefinitionDescriptionElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDefinitionTermElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDirectoryElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDivElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLEmbedElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFieldSetElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLFontElement;
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
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLKeygenElement;
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
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLNoShowElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOListElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLObjectElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOptGroupElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOptionElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOptionsCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOutputElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLParagraphElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLParamElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLPhraseElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLPreElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLProgressElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLScriptElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSelectElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSourceElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSpanElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLStyleElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableCaptionElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableCellElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableColElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableComponent;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableRowElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableSectionElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTextAreaElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTextElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTimeElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTitleElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLUListElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLUnknownElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLVideoElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLWBRElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAltGlyphElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAngle;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimateElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimateMotionElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAnimateTransformElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGCircleElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGClipPathElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGDefsElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGDescElement;
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
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGImageElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGLineElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGLinearGradientElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMarkerElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMaskElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMatrix;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMetadataElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMpathElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPatternElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPolygonElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPolylineElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGRadialGradientElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGRect;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGRectElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGSVGElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGScriptElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGSetElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGStopElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGStyleElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGSwitchElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGSymbolElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTSpanElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTextElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTextPathElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGTitleElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGUseElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGViewElement;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLHttpRequest;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLSerializer;

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
        ArrayBuffer.class, ArrayBufferView.class, ArrayBufferViewBase.class,
        Attr.class, ActiveXObject.class, ApplicationCache.class,
        BoxObject.class, CDATASection.class, ClipboardData.class,
        CSSCharsetRule.class, CSSFontFaceRule.class, CSSImportRule.class, CSSMediaRule.class, CSSPrimitiveValue.class,
        CSSRule.class,
        CSSRuleList.class, CSSStyleDeclaration.class, CSSStyleRule.class, CSSStyleSheet.class, CSSValue.class,
        CanvasRenderingContext2D.class, CharacterDataImpl.class, ClientRect.class, Comment.class,
        ComputedCSSStyleDeclaration.class, Console.class, Coordinates.class, DataView.class, DOMException.class,
        DOMImplementation.class, DOMParser.class, DOMStringMap.class,
        DOMTokenList.class, Document.class, DocumentFragment.class,
        DocumentType.class, Element.class, Enumerator.class, Event.class, EventNode.class, External.class,
        Float32Array.class, Float64Array.class,
        FormChild.class, FormField.class, Geolocation.class,
        HashChangeEvent.class, History.class,
        HTMLAnchorElement.class, HTMLAppletElement.class, HTMLAreaElement.class, HTMLAudioElement.class,
        HTMLBGSoundElement.class,
        HTMLBRElement.class, HTMLBaseElement.class, HTMLBaseFontElement.class,
        HTMLBlockElement.class,
        HTMLBlockQuoteElement.class, HTMLBodyElement.class, HTMLButtonElement.class, HTMLCanvasElement.class,
        HTMLCollection.class, HTMLCollectionTags.class,
        HTMLDataListElement.class,
        HTMLDefinitionDescriptionElement.class, HTMLDefinitionTermElement.class,
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
        HTMLMediaElement.class, HTMLMenuElement.class, HTMLMetaElement.class, HTMLMeterElement.class,
        HTMLModElement.class,
        HTMLNoShowElement.class,
        HTMLNextIdElement.class,
        HTMLOListElement.class, HTMLObjectElement.class, HTMLOptGroupElement.class,
        HTMLOptionElement.class, HTMLOptionsCollection.class, HTMLOutputElement.class,
        HTMLParagraphElement.class, HTMLParamElement.class,
        HTMLPhraseElement.class,
        HTMLPreElement.class, HTMLProgressElement.class, HTMLScriptElement.class,
        HTMLSelectElement.class, HTMLSourceElement.class, HTMLSpanElement.class,
        HTMLStyleElement.class, HTMLTableCaptionElement.class, HTMLTableCellElement.class, HTMLTableColElement.class,
        HTMLTableComponent.class, HTMLTableElement.class, HTMLTableRowElement.class, HTMLTableSectionElement.class,
        HTMLTextElement.class, HTMLTextAreaElement.class, HTMLTimeElement.class, HTMLTitleElement.class,
        HTMLUListElement.class, HTMLUnknownElement.class,
        HTMLWBRElement.class,
        HTMLVideoElement.class,
        Int16Array.class, Int32Array.class, Int8Array.class,
        KeyboardEvent.class,
        Location.class, MediaList.class, MessageEvent.class, MimeType.class, MimeTypeArray.class, MouseEvent.class,
        MutationEvent.class, NamedNodeMap.class, Namespace.class, NamespaceCollection.class, Navigator.class,
        Node.class, NodeFilter.class, NodeList.class,
        Plugin.class, PluginArray.class, Popup.class, Position.class, ProcessingInstruction.class,
        Range.class, RowContainer.class,
        SVGAElement.class, SVGAltGlyphElement.class, SVGAngle.class, SVGAnimateElement.class,
        SVGAnimateMotionElement.class, SVGAnimateTransformElement.class, SVGCircleElement.class,
        SVGClipPathElement.class, SVGDefsElement.class, SVGDescElement.class, SVGElement.class,
        SVGEllipseElement.class, SVGFEBlendElement.class, SVGFEColorMatrixElement.class,
        SVGFEComponentTransferElement.class, SVGFECompositeElement.class, SVGFEConvolveMatrixElement.class,
        SVGFEDiffuseLightingElement.class, SVGFEDisplacementMapElement.class, SVGFEDistantLightElement.class,
        SVGFEFloodElement.class, SVGFEFuncAElement.class, SVGFEFuncBElement.class, SVGFEFuncGElement.class,
        SVGFEFuncRElement.class, SVGFEGaussianBlurElement.class, SVGFEImageElement.class, SVGFEMergeElement.class,
        SVGFEMergeNodeElement.class, SVGFEMorphologyElement.class, SVGFEOffsetElement.class,
        SVGFEPointLightElement.class, SVGFESpecularLightingElement.class, SVGFESpotLightElement.class,
        SVGFETileElement.class, SVGFETurbulenceElement.class, SVGFilterElement.class, SVGForeignObjectElement.class,
        SVGGElement.class, SVGImageElement.class, SVGLineElement.class, SVGLinearGradientElement.class,
        SVGMarkerElement.class, SVGMaskElement.class, SVGMatrix.class, SVGMetadataElement.class, SVGMpathElement.class,
        SVGPathElement.class, SVGPatternElement.class, SVGPolygonElement.class, SVGPolylineElement.class,
        SVGRadialGradientElement.class,
        SVGRect.class, SVGRectElement.class,
        SVGSVGElement.class, SVGScriptElement.class,
        SVGSetElement.class, SVGStopElement.class, SVGStyleElement.class, SVGSwitchElement.class,
        SVGSymbolElement.class, SVGTSpanElement.class, SVGTextElement.class, SVGTextPathElement.class,
        SVGTitleElement.class, SVGUseElement.class, SVGViewElement.class,
        Screen.class, Selection.class, SimpleArray.class,
        StaticNodeList.class, Storage.class, StyleSheetList.class, Text.class, TextRange.class, TreeWalker.class,
        UIEvent.class, Uint16Array.class, Uint32Array.class, Uint8Array.class, Uint8ClampedArray.class,
        WebSocket.class, Window.class, XMLDocument.class,
        XMLHttpRequest.class, XMLSerializer.class, XPathEvaluator.class, XPathNSResolver.class, XPathResult.class,
        XSLTProcessor.class, XSLTemplate.class};

    /** Cache of browser versions and their corresponding JavaScript configurations. */
    private static final Map<BrowserVersion, JavaScriptConfiguration> CONFIGURATION_MAP_ =
        new WeakHashMap<BrowserVersion, JavaScriptConfiguration>();

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
