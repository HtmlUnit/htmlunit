/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAbbreviated;
import com.gargoylesoftware.htmlunit.html.HtmlAcronym;
import com.gargoylesoftware.htmlunit.html.HtmlAddress;
import com.gargoylesoftware.htmlunit.html.HtmlBackgroundSound;
import com.gargoylesoftware.htmlunit.html.HtmlBidirectionalOverride;
import com.gargoylesoftware.htmlunit.html.HtmlBig;
import com.gargoylesoftware.htmlunit.html.HtmlBlink;
import com.gargoylesoftware.htmlunit.html.HtmlBlockQuote;
import com.gargoylesoftware.htmlunit.html.HtmlBold;
import com.gargoylesoftware.htmlunit.html.HtmlCenter;
import com.gargoylesoftware.htmlunit.html.HtmlCitation;
import com.gargoylesoftware.htmlunit.html.HtmlCode;
import com.gargoylesoftware.htmlunit.html.HtmlDefinition;
import com.gargoylesoftware.htmlunit.html.HtmlDefinitionDescription;
import com.gargoylesoftware.htmlunit.html.HtmlDefinitionTerm;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlEmphasis;
import com.gargoylesoftware.htmlunit.html.HtmlExample;
import com.gargoylesoftware.htmlunit.html.HtmlHeading1;
import com.gargoylesoftware.htmlunit.html.HtmlHeading2;
import com.gargoylesoftware.htmlunit.html.HtmlHeading3;
import com.gargoylesoftware.htmlunit.html.HtmlHeading4;
import com.gargoylesoftware.htmlunit.html.HtmlHeading5;
import com.gargoylesoftware.htmlunit.html.HtmlHeading6;
import com.gargoylesoftware.htmlunit.html.HtmlInlineQuotation;
import com.gargoylesoftware.htmlunit.html.HtmlItalic;
import com.gargoylesoftware.htmlunit.html.HtmlKeyboard;
import com.gargoylesoftware.htmlunit.html.HtmlListing;
import com.gargoylesoftware.htmlunit.html.HtmlMarquee;
import com.gargoylesoftware.htmlunit.html.HtmlMultiColumn;
import com.gargoylesoftware.htmlunit.html.HtmlNoBreak;
import com.gargoylesoftware.htmlunit.html.HtmlNoEmbed;
import com.gargoylesoftware.htmlunit.html.HtmlNoFrames;
import com.gargoylesoftware.htmlunit.html.HtmlNoScript;
import com.gargoylesoftware.htmlunit.html.HtmlPlainText;
import com.gargoylesoftware.htmlunit.html.HtmlS;
import com.gargoylesoftware.htmlunit.html.HtmlSample;
import com.gargoylesoftware.htmlunit.html.HtmlSmall;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlStrike;
import com.gargoylesoftware.htmlunit.html.HtmlStrong;
import com.gargoylesoftware.htmlunit.html.HtmlSubscript;
import com.gargoylesoftware.htmlunit.html.HtmlSuperscript;
import com.gargoylesoftware.htmlunit.html.HtmlTableBody;
import com.gargoylesoftware.htmlunit.html.HtmlTableColumn;
import com.gargoylesoftware.htmlunit.html.HtmlTableColumnGroup;
import com.gargoylesoftware.htmlunit.html.HtmlTableFooter;
import com.gargoylesoftware.htmlunit.html.HtmlTableHeader;
import com.gargoylesoftware.htmlunit.html.HtmlTeletype;
import com.gargoylesoftware.htmlunit.html.HtmlUnderlined;
import com.gargoylesoftware.htmlunit.html.HtmlVariable;
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
import com.gargoylesoftware.htmlunit.javascript.host.DOMException;
import com.gargoylesoftware.htmlunit.javascript.host.DOMImplementation;
import com.gargoylesoftware.htmlunit.javascript.host.DOMParser;
import com.gargoylesoftware.htmlunit.javascript.host.DOMTokenList;
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
import com.gargoylesoftware.htmlunit.javascript.host.OfflineResourceList;
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
import com.gargoylesoftware.htmlunit.javascript.host.XPathNSResolver;
import com.gargoylesoftware.htmlunit.javascript.host.XPathResult;
import com.gargoylesoftware.htmlunit.javascript.host.XSLTProcessor;
import com.gargoylesoftware.htmlunit.javascript.host.XSLTemplate;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.CanvasRenderingContext2D;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSCharsetRule;
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
import com.gargoylesoftware.htmlunit.javascript.host.geo.Coordinates;
import com.gargoylesoftware.htmlunit.javascript.host.geo.Geolocation;
import com.gargoylesoftware.htmlunit.javascript.host.geo.Position;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAnchorElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAppletElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAreaElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLAudioElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBRElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBaseElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBaseFontElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLButtonElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCanvasElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLCollectionTags;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDListElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDelElement;
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
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLInputElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLInsElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLIsIndexElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLIElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLabelElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLegendElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLLinkElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLListElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMapElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMenuElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLMetaElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOListElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLObjectElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOptGroupElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOptionElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLOptionsCollection;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLParagraphElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLParamElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLPreElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLProgressElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLQuoteElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLScriptElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSelectElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSourceElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSpacerElement;
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
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTitleElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLUListElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLUnknownElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLVideoElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLWBRElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGAltGlyphElement;
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
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMetadataElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGMpathElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPathElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPatternElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPolygonElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGPolylineElement;
import com.gargoylesoftware.htmlunit.javascript.host.svg.SVGRadialGradientElement;
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
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLAttr;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDOMParseError;
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
 */
public final class JavaScriptConfiguration {

    private static final Log LOG = LogFactory.getLog(JavaScriptConfiguration.class);

    @SuppressWarnings("unchecked")
    static final Class<? extends SimpleScriptable>[] CLASSES_ = new Class[] {
        Attr.class, ActiveXObject.class, BoxObject.class, CDATASection.class, ClipboardData.class, //aaaaaaaaaaaaaaaaaa
        CSSCharsetRule.class, CSSImportRule.class, CSSMediaRule.class, CSSPrimitiveValue.class, CSSRule.class,
        CSSRuleList.class, CSSStyleDeclaration.class, CSSStyleRule.class, CSSStyleSheet.class, CSSValue.class,
        CanvasRenderingContext2D.class, CharacterDataImpl.class, ClientRect.class, Comment.class,
        ComputedCSSStyleDeclaration.class, Console.class, Coordinates.class, DOMException.class,
        DOMImplementation.class, DOMParser.class, DOMTokenList.class, Document.class, DocumentFragment.class,
        DocumentType.class, Element.class, Enumerator.class, Event.class, EventNode.class, External.class,
        FormChild.class, FormField.class, Geolocation.class, History.class,
        HTMLAnchorElement.class, HTMLAppletElement.class, HTMLAreaElement.class, HTMLAudioElement.class,
        HTMLBRElement.class, HTMLBaseElement.class, HTMLBaseFontElement.class,
        HTMLBodyElement.class, HTMLButtonElement.class, HTMLCanvasElement.class, HTMLCollection.class,
        HTMLCollectionTags.class, HTMLDListElement.class, HTMLDelElement.class, HTMLDirectoryElement.class,
        HTMLDivElement.class, HTMLDocument.class, HTMLElement.class, HTMLEmbedElement.class, HTMLFieldSetElement.class,
        HTMLFontElement.class, HTMLFormElement.class, HTMLFrameElement.class, HTMLFrameSetElement.class,
        HTMLHRElement.class, HTMLHeadElement.class, HTMLHeadingElement.class, HTMLHtmlElement.class,
        HTMLIFrameElement.class, HTMLImageElement.class, HTMLInputElement.class, HTMLInsElement.class,
        HTMLIsIndexElement.class, HTMLLIElement.class, HTMLLabelElement.class, HTMLLegendElement.class,
        HTMLLinkElement.class, HTMLListElement.class, HTMLMapElement.class, HTMLMenuElement.class,
        HTMLMetaElement.class, HTMLOListElement.class, HTMLObjectElement.class, HTMLOptGroupElement.class,
        HTMLOptionElement.class, HTMLOptionsCollection.class, HTMLParagraphElement.class, HTMLParamElement.class,
        HTMLPreElement.class, HTMLProgressElement.class, HTMLQuoteElement.class, HTMLScriptElement.class,
        HTMLSelectElement.class, HTMLSourceElement.class, HTMLSpacerElement.class, HTMLSpanElement.class,
        HTMLStyleElement.class, HTMLTableCaptionElement.class, HTMLTableCellElement.class, HTMLTableColElement.class,
        HTMLTableComponent.class, HTMLTableElement.class, HTMLTableRowElement.class, HTMLTableSectionElement.class,
        HTMLTextAreaElement.class, HTMLTitleElement.class, HTMLUListElement.class, HTMLUnknownElement.class,
        HTMLVideoElement.class, HTMLWBRElement.class, HashChangeEvent.class, History.class, KeyboardEvent.class,
        Location.class, MediaList.class, MessageEvent.class, MimeType.class, MimeTypeArray.class, MouseEvent.class,
        MutationEvent.class, NamedNodeMap.class, Namespace.class, NamespaceCollection.class, Navigator.class,
        Node.class, NodeFilter.class, NodeList.class, OfflineResourceList.class,
        Plugin.class, PluginArray.class, Popup.class, Position.class, ProcessingInstruction.class,
        Range.class, RowContainer.class, SVGAElement.class, SVGAltGlyphElement.class, SVGAnimateElement.class,
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
        SVGMarkerElement.class, SVGMaskElement.class, SVGMetadataElement.class, SVGMpathElement.class,
        SVGPathElement.class, SVGPatternElement.class, SVGPolygonElement.class, SVGPolylineElement.class,
        SVGRadialGradientElement.class, SVGRectElement.class, SVGSVGElement.class, SVGScriptElement.class,
        SVGSetElement.class, SVGStopElement.class, SVGStyleElement.class, SVGSwitchElement.class,
        SVGSymbolElement.class, SVGTSpanElement.class, SVGTextElement.class, SVGTextPathElement.class,
        SVGTitleElement.class, SVGUseElement.class, Screen.class, Selection.class, SimpleArray.class,
        StaticNodeList.class, Storage.class, StyleSheetList.class, Text.class, TextRange.class, TreeWalker.class,
        UIEvent.class, WebSocket.class, Window.class, XMLAttr.class, XMLDocument.class, XMLDOMParseError.class,
        XMLHttpRequest.class, XMLSerializer.class, XPathNSResolver.class, XPathResult.class, XSLTProcessor.class,
        XSLTemplate.class};

    /** Cache of browser versions and their corresponding JavaScript configurations. */
    private static Map<BrowserVersion, JavaScriptConfiguration> ConfigurationMap_ =
        new WeakHashMap<BrowserVersion, JavaScriptConfiguration>();

    private static Map<String, String> ClassnameMap_ = new HashMap<String, String>();

    private Map<Class<? extends DomElement>, Class<? extends SimpleScriptable>> domJavaScriptMap_;

    private final Map<String, ClassConfiguration> configuration_;

    /**
     * Constructor is only called from {@link #getInstance(BrowserVersion)} which is synchronized.
     * @param browser the browser version to use
     */
    private JavaScriptConfiguration(final BrowserVersion browser) {
        configuration_ = buildUsageMap(browser);
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
        JavaScriptConfiguration configuration = ConfigurationMap_.get(browserVersion);

        if (configuration == null) {
            configuration = new JavaScriptConfiguration(browserVersion);
            ConfigurationMap_.put(browserVersion, configuration);
        }
        return configuration;
    }

    /**
     * Returns the configuration that has all entries. No constraints are put on the returned entries.
     *
     * @return the instance containing all entries from the configuration file
     */
    static JavaScriptConfiguration getAllEntries() {
        final JavaScriptConfiguration configuration = new JavaScriptConfiguration(null);
        return configuration;
    }

    /**
     * Gets all the configurations.
     * @return the class configurations
     */
    public Iterable<ClassConfiguration> getAll() {
        return configuration_.values();
    }

    private Map<String, ClassConfiguration> buildUsageMap(final BrowserVersion browser) {
        final Map<String, ClassConfiguration> classMap = new HashMap<String, ClassConfiguration>(CLASSES_.length);

        for (final Class<? extends SimpleScriptable> klass : CLASSES_) {
            final ClassConfiguration config = processClass(klass, browser);
            if (config != null) {
                classMap.put(config.getHostClass().getSimpleName(), config);
            }
        }
        return Collections.unmodifiableMap(classMap);
    }

    private ClassConfiguration processClass(final Class<? extends SimpleScriptable> klass,
            final BrowserVersion browser) {
        if (browser != null) {
            final JsxClass jsxClass = klass.getAnnotation(JsxClass.class);
            if (jsxClass != null && isSupported(jsxClass.browsers(), browser)) {
                final String hostClassName = klass.getName();

                final String domClassName = jsxClass.domClass() != Object.class ? jsxClass.domClass().getName() : "";

                final boolean isJsObject = jsxClass.isJSObject();
                final ClassConfiguration classConfiguration = new ClassConfiguration(klass, domClassName, isJsObject);

                final String simpleClassName = hostClassName.substring(hostClassName.lastIndexOf('.') + 1);
                ClassnameMap_.put(hostClassName, simpleClassName);
                final Map<String, Method> allGetters = new HashMap<String, Method>();
                final Map<String, Method> allSetters = new HashMap<String, Method>();
                for (final Method method : classConfiguration.getHostClass().getDeclaredMethods()) {
                    for (final Annotation annotation : method.getAnnotations()) {
                        if (annotation instanceof JsxGetter) {
                            if (isSupported(((JsxGetter) annotation).value(), browser)) {
                                final String property = method.getName().substring("get_".length());
                                allGetters.put(property, method);
                            }
                        }
                        else if (annotation instanceof JsxSetter) {
                            if (isSupported(((JsxSetter) annotation).value(), browser)) {
                                final String property = method.getName().substring("set_".length());
                                allSetters.put(property, method);
                            }
                        }
                        else if (annotation instanceof JsxFunction) {
                            if (isSupported(((JsxFunction) annotation).value(), browser)) {
                                classConfiguration.addFunction(method);
                            }
                        }
                        else if (annotation instanceof JsxConstructor) {
                            classConfiguration.setJSConstructor(method);
                        }
                    }
                }
                for (final Field field : classConfiguration.getHostClass().getDeclaredFields()) {
                    final JsxConstant jsxConstant = field.getAnnotation(JsxConstant.class);
                    if (jsxConstant != null && isSupported(jsxConstant.value(), browser)) {
                        classConfiguration.addConstant(field.getName());
                    }
                }
                for (final String property : allGetters.keySet()) {
                    classConfiguration.addProperty(property,
                            allGetters.get(property), allSetters.get(property));
                }
                return classConfiguration;
            }
        }
        return null;
    }

    private static boolean isSupported(final WebBrowser[] browsers, final BrowserVersion browserVersion) {
        for (final WebBrowser browser : browsers) {
            final String expectedBrowserName;
            if (browserVersion.isIE()) {
                expectedBrowserName = "IE";
            }
            else if (browserVersion.isFirefox()) {
                expectedBrowserName = "FF";
            }
            else {
                expectedBrowserName = "CHROME";
            }
            if (browser.value().name().equals(expectedBrowserName)
                    && browser.minVersion() <= browserVersion.getBrowserVersionNumeric()
                    && browser.maxVersion() >= browserVersion.getBrowserVersionNumeric()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the class configuration for the supplied JavaScript class name.
     * @param hostClassName the js class name
     * @return the class configuration for the supplied JavaScript class name
     */
    public ClassConfiguration getClassConfiguration(final String hostClassName) {
        return configuration_.get(hostClassName);
    }

    /**
     * Returns the classname that the given class implements. If the class is
     * the input class, then the name is extracted from the type that the Input class
     * is masquerading as.
     * FIXME - Implement the Input class processing
     * @param clazz
     * @return the classname
     */
    String getClassnameForClass(final Class<?> clazz) {
        final String name = ClassnameMap_.get(clazz.getName());
        if (name == null) {
            throw new IllegalStateException("Did not find the mapping of the class to the classname for "
                + clazz.getName());
        }
        return name;
    }

    /**
     * Returns an immutable map containing the DOM to JavaScript mappings. Keys are
     * java classes for the various DOM classes (e.g. HtmlInput.class) and the values
     * are the JavaScript class names (e.g. "HTMLAnchorElement").
     * @return the mappings
     */
    @SuppressWarnings("unchecked")
    public Map<Class<? extends DomElement>, Class<? extends SimpleScriptable>>
    getDomJavaScriptMapping() {
        if (domJavaScriptMap_ != null) {
            return domJavaScriptMap_;
        }

        final Map<Class<? extends DomElement>, Class<? extends SimpleScriptable>> map =
            new HashMap<Class<? extends DomElement>, Class<? extends SimpleScriptable>>();

        for (String hostClassName : configuration_.keySet()) {
            ClassConfiguration classConfig = getClassConfiguration(hostClassName);
            final String domClassName = classConfig.getDomClassName();
            if (domClassName != null) {
                try {
                    final Class<? extends DomElement> domClass =
                        (Class<? extends DomElement>) Class.forName(domClassName);
                    // preload and validate that the class exists
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Mapping " + domClass.getName() + " to " + hostClassName);
                    }
                    while (!classConfig.isJsObject()) {
                        hostClassName = classConfig.getExtendedClassName();
                        classConfig = getClassConfiguration(hostClassName);
                    }
                    map.put(domClass, classConfig.getHostClass());
                }
                catch (final ClassNotFoundException e) {
                    throw new NoClassDefFoundError(e.getMessage());
                }
            }
        }
        map.put(HtmlHeading1.class, HTMLHeadingElement.class);
        map.put(HtmlHeading2.class, HTMLHeadingElement.class);
        map.put(HtmlHeading3.class, HTMLHeadingElement.class);
        map.put(HtmlHeading4.class, HTMLHeadingElement.class);
        map.put(HtmlHeading5.class, HTMLHeadingElement.class);
        map.put(HtmlHeading6.class, HTMLHeadingElement.class);

        map.put(HtmlInlineQuotation.class, HTMLQuoteElement.class);
        map.put(HtmlBlockQuote.class, HTMLQuoteElement.class);

        map.put(HtmlAbbreviated.class, HTMLSpanElement.class);
        map.put(HtmlAcronym.class, HTMLSpanElement.class);
        map.put(HtmlAddress.class, HTMLSpanElement.class);
        map.put(HtmlBackgroundSound.class, HTMLSpanElement.class);
        map.put(HtmlBidirectionalOverride.class, HTMLSpanElement.class);
        map.put(HtmlBig.class, HTMLSpanElement.class);
        map.put(HtmlBold.class, HTMLSpanElement.class);
        map.put(HtmlBlink.class, HTMLSpanElement.class);
        map.put(HtmlCenter.class, HTMLSpanElement.class);
        map.put(HtmlCitation.class, HTMLSpanElement.class);
        map.put(HtmlCode.class, HTMLSpanElement.class);
        map.put(HtmlDefinition.class, HTMLSpanElement.class);
        map.put(HtmlDefinitionDescription.class, HTMLSpanElement.class);
        map.put(HtmlDefinitionTerm.class, HTMLSpanElement.class);
        map.put(HtmlEmphasis.class, HTMLSpanElement.class);
        map.put(HtmlItalic.class, HTMLSpanElement.class);
        map.put(HtmlKeyboard.class, HTMLSpanElement.class);
        map.put(HtmlListing.class, HTMLSpanElement.class);
        map.put(HtmlMultiColumn.class, HTMLSpanElement.class);
        map.put(HtmlNoBreak.class, HTMLSpanElement.class);
        map.put(HtmlPlainText.class, HTMLSpanElement.class);
        map.put(HtmlS.class, HTMLSpanElement.class);
        map.put(HtmlSample.class, HTMLSpanElement.class);
        map.put(HtmlSmall.class, HTMLSpanElement.class);
        map.put(HtmlSpan.class, HTMLSpanElement.class);
        map.put(HtmlStrike.class, HTMLSpanElement.class);
        map.put(HtmlStrong.class, HTMLSpanElement.class);
        map.put(HtmlSubscript.class, HTMLSpanElement.class);
        map.put(HtmlSuperscript.class, HTMLSpanElement.class);
        map.put(HtmlTeletype.class, HTMLSpanElement.class);
        map.put(HtmlUnderlined.class, HTMLSpanElement.class);
        map.put(HtmlVariable.class, HTMLSpanElement.class);
        map.put(HtmlExample.class, HTMLSpanElement.class);

        map.put(HtmlDivision.class, HTMLDivElement.class);
        map.put(HtmlMarquee.class, HTMLDivElement.class);
        map.put(HtmlNoEmbed.class, HTMLDivElement.class);
        map.put(HtmlNoFrames.class, HTMLDivElement.class);
        map.put(HtmlNoScript.class, HTMLDivElement.class);

        map.put(HtmlTableBody.class, HTMLTableSectionElement.class);
        map.put(HtmlTableHeader.class, HTMLTableSectionElement.class);
        map.put(HtmlTableFooter.class, HTMLTableSectionElement.class);

        map.put(HtmlTableColumn.class, HTMLTableColElement.class);
        map.put(HtmlTableColumnGroup.class, HTMLTableColElement.class);

        domJavaScriptMap_ = Collections.unmodifiableMap(map);

        return domJavaScriptMap_;
    }
}
