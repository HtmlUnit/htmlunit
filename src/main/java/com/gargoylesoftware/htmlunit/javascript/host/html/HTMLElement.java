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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_167;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_65;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_66;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_69;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_70;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_71;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_72;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_73;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_74;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_75;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTML_COLOR_RESTRICT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ALIGN_ACCEPTS_ARBITRARY_VALUES;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_BOUNDING_CLIENT_RECT_OFFSET_TWO;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_CHAR_EMULATED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_CHAR_OFF_EMULATED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_CHAR_OFF_INTEGER;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_CHAR_UNDEFINED_DOT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_CLIENT_LEFT_TOP_ZERO;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ELEMENT_EXTENT_WITHOUT_PADDING;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INNER_HTML_REDUCE_WHITESPACES;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_OFFSET_PARENT_THROWS_NOT_ATTACHED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_SET_ATTRIBUTE_CONSIDERS_ATTR_FOR_CLASS_AS_REAL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.QUERYSELECTORALL_NOT_IN_QUIRKS;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.css.sac.CSSException;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomCharacterData;
import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlFrameSet;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.javascript.NamedNodeMap;
import com.gargoylesoftware.htmlunit.javascript.ScriptableWithFallbackGetter;
import com.gargoylesoftware.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJob;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.Attr;
import com.gargoylesoftware.htmlunit.javascript.host.BoxObject;
import com.gargoylesoftware.htmlunit.javascript.host.ClientRect;
import com.gargoylesoftware.htmlunit.javascript.host.DOMTokenList;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Event;
import com.gargoylesoftware.htmlunit.javascript.host.EventHandler;
import com.gargoylesoftware.htmlunit.javascript.host.MouseEvent;
import com.gargoylesoftware.htmlunit.javascript.host.Node;
import com.gargoylesoftware.htmlunit.javascript.host.NodeList;
import com.gargoylesoftware.htmlunit.javascript.host.StaticNodeList;
import com.gargoylesoftware.htmlunit.javascript.host.TextRange;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;

/**
 * The JavaScript object "HTMLElement" which is the base class for all HTML
 * objects. This will typically wrap an instance of {@link HtmlElement}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Barnaby Court
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Chris Erskine
 * @author David D. Kilzer
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Hans Donner
 * @author Bruce Faulkner
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Ronald Brill
 */
@JsxClass(domClasses = HtmlElement.class)
public class HTMLElement extends Element implements ScriptableWithFallbackGetter {

    private static final Pattern PERCENT_VALUE = Pattern.compile("\\d+%");
    /* http://msdn.microsoft.com/en-us/library/ie/aa358802.aspx */
    private static final Map<String, String> COLORS_MAP_IE = new HashMap<String, String>();

    private static final Log LOG = LogFactory.getLog(HTMLElement.class);

    private static final int BEHAVIOR_ID_UNKNOWN = -1;
    /** BEHAVIOR_ID_CLIENT_CAPS. */
    public static final int BEHAVIOR_ID_CLIENT_CAPS = 0;
    /** BEHAVIOR_ID_HOMEPAGE. */
    public static final int BEHAVIOR_ID_HOMEPAGE = 1;
    /** BEHAVIOR_ID_DOWNLOAD. */
    public static final int BEHAVIOR_ID_DOWNLOAD = 2;

    private static final String BEHAVIOR_CLIENT_CAPS = "#default#clientCaps";
    private static final String BEHAVIOR_HOMEPAGE = "#default#homePage";
    private static final String BEHAVIOR_DOWNLOAD = "#default#download";

    private static final Pattern CLASS_NAMES_SPLIT_PATTERN = Pattern.compile("\\s");
    private static final Pattern PRINT_NODE_PATTERN = Pattern.compile("  ");
    private static final Pattern PRINT_NODE_QUOTE_PATTERN = Pattern.compile("\"");

    static final String POSITION_BEFORE_BEGIN = "beforeBegin";
    static final String POSITION_AFTER_BEGIN = "afterBegin";
    static final String POSITION_BEFORE_END = "beforeEnd";
    static final String POSITION_AFTER_END = "afterEnd";

    /**
     * Static counter for {@link #uniqueID_}.
     */
    private static int UniqueID_Counter_ = 1;

    private final Set<String> behaviors_ = new HashSet<String>();
    private BoxObject boxObject_; // lazy init
    private HTMLCollection all_; // has to be a member to have equality (==) working
    private int scrollLeft_;
    private int scrollTop_;
    private String uniqueID_;
    private CSSStyleDeclaration style_;

    static {
        COLORS_MAP_IE.put("AliceBlue", "#F0F8FF");
        COLORS_MAP_IE.put("AntiqueWhite", "#FAEBD7");
        COLORS_MAP_IE.put("Aqua", "#00FFFF");
        COLORS_MAP_IE.put("Aquamarine", "#7FFFD4");
        COLORS_MAP_IE.put("Azure", "#F0FFFF");
        COLORS_MAP_IE.put("Beige", "#F5F5DC");
        COLORS_MAP_IE.put("Bisque", "#FFE4C4");
        COLORS_MAP_IE.put("Black", "#000000");
        COLORS_MAP_IE.put("BlanchedAlmond", "#FFEBCD");
        COLORS_MAP_IE.put("Blue", "#0000FF");
        COLORS_MAP_IE.put("BlueViolet", "#8A2BE2");
        COLORS_MAP_IE.put("Brown", "#A52A2A");
        COLORS_MAP_IE.put("BurlyWood", "#DEB887");
        COLORS_MAP_IE.put("CadetBlue", "#5F9EA0");
        COLORS_MAP_IE.put("Chartreuse", "#7FFF00");
        COLORS_MAP_IE.put("Chocolate", "#D2691E");
        COLORS_MAP_IE.put("Coral", "#FF7F50");
        COLORS_MAP_IE.put("CornflowerBlue", "#6495ED");
        COLORS_MAP_IE.put("Cornsilk", "#FFF8DC");
        COLORS_MAP_IE.put("Crimson", "#DC143C");
        COLORS_MAP_IE.put("Cyan", "#00FFFF");
        COLORS_MAP_IE.put("DarkBlue", "#00008B");
        COLORS_MAP_IE.put("DarkCyan", "#008B8B");
        COLORS_MAP_IE.put("DarkGoldenrod", "#B8860B");
        COLORS_MAP_IE.put("DarkGray", "#A9A9A9");
        COLORS_MAP_IE.put("DarkGrey", "#A9A9A9");
        COLORS_MAP_IE.put("DarkGreen", "#006400");
        COLORS_MAP_IE.put("DarkKhaki", "#BDB76B");
        COLORS_MAP_IE.put("DarkMagenta", "#8B008B");
        COLORS_MAP_IE.put("DarkOliveGreen", "#556B2F");
        COLORS_MAP_IE.put("DarkOrange", "#FF8C00");
        COLORS_MAP_IE.put("DarkOrchid", "#9932CC");
        COLORS_MAP_IE.put("DarkRed", "#8B0000");
        COLORS_MAP_IE.put("DarkSalmon", "#E9967A");
        COLORS_MAP_IE.put("DarkSeaGreen", "#8FBC8F");
        COLORS_MAP_IE.put("DarkSlateBlue", "#483D8B");
        COLORS_MAP_IE.put("DarkSlateGray", "#2F4F4F");
        COLORS_MAP_IE.put("DarkSlateGrey", "#2F4F4F");
        COLORS_MAP_IE.put("DarkTurquoise", "#00CED1");
        COLORS_MAP_IE.put("DarkViolet", "#9400D3");
        COLORS_MAP_IE.put("DeepPink", "#FF1493");
        COLORS_MAP_IE.put("DeepSkyBlue", "#00BFFF");
        COLORS_MAP_IE.put("DimGray", "#696969");
        COLORS_MAP_IE.put("DimGrey", "#696969");
        COLORS_MAP_IE.put("DodgerBlue", "#1E90FF");
        COLORS_MAP_IE.put("FireBrick", "#B22222");
        COLORS_MAP_IE.put("FloralWhite", "#FFFAF0");
        COLORS_MAP_IE.put("ForestGreen", "#228B22");
        COLORS_MAP_IE.put("Fuchsia", "#FF00FF");
        COLORS_MAP_IE.put("Gainsboro", "#DCDCDC");
        COLORS_MAP_IE.put("GhostWhite", "#F8F8FF");
        COLORS_MAP_IE.put("Gold", "#FFD700");
        COLORS_MAP_IE.put("Goldenrod", "#DAA520");
        COLORS_MAP_IE.put("Gray", "#808080");
        COLORS_MAP_IE.put("Grey", "#808080");
        COLORS_MAP_IE.put("Green", "#008000");
        COLORS_MAP_IE.put("GreenYellow", "#ADFF2F");
        COLORS_MAP_IE.put("Honeydew", "#F0FFF0");
        COLORS_MAP_IE.put("HotPink", "#FF69B4");
        COLORS_MAP_IE.put("IndianRed", "#CD5C5C");
        COLORS_MAP_IE.put("Indigo", "#4B0082");
        COLORS_MAP_IE.put("Ivory", "#FFFFF0");
        COLORS_MAP_IE.put("Khaki", "#F0E68C");
        COLORS_MAP_IE.put("Lavender", "#E6E6FA");
        COLORS_MAP_IE.put("LavenderBlush", "#FFF0F5");
        COLORS_MAP_IE.put("LawnGreen", "#7CFC00");
        COLORS_MAP_IE.put("LemonChiffon", "#FFFACD");
        COLORS_MAP_IE.put("LightBlue", "#ADD8E6");
        COLORS_MAP_IE.put("LightCoral", "#F08080");
        COLORS_MAP_IE.put("LightCyan", "#E0FFFF");
        COLORS_MAP_IE.put("LightGoldenrodYellow", "#FAFAD2");
        COLORS_MAP_IE.put("LightGreen", "#90EE90");
        COLORS_MAP_IE.put("LightGray", "#D3D3D3");
        COLORS_MAP_IE.put("LightGrey", "#D3D3D3");
        COLORS_MAP_IE.put("LightPink", "#FFB6C1");
        COLORS_MAP_IE.put("LightSalmon", "#FFA07A");
        COLORS_MAP_IE.put("LightSeaGreen", "#20B2AA");
        COLORS_MAP_IE.put("LightSkyBlue", "#87CEFA");
        COLORS_MAP_IE.put("LightSlateGray", "#778899");
        COLORS_MAP_IE.put("LightSlateGrey", "#778899");
        COLORS_MAP_IE.put("LightSteelBlue", "#B0C4DE");
        COLORS_MAP_IE.put("LightYellow", "#FFFFE0");
        COLORS_MAP_IE.put("Lime", "#00FF00");
        COLORS_MAP_IE.put("LimeGreen", "#32CD32");
        COLORS_MAP_IE.put("Linen", "#FAF0E6");
        COLORS_MAP_IE.put("Magenta", "#FF00FF");
        COLORS_MAP_IE.put("Maroon", "#800000");
        COLORS_MAP_IE.put("MediumAquamarine", "#66CDAA");
        COLORS_MAP_IE.put("MediumBlue", "#0000CD");
        COLORS_MAP_IE.put("MediumOrchid", "#BA55D3");
        COLORS_MAP_IE.put("MediumPurple", "#9370DB");
        COLORS_MAP_IE.put("MediumSeaGreen", "#3CB371");
        COLORS_MAP_IE.put("MediumSlateBlue", "#7B68EE");
        COLORS_MAP_IE.put("MediumSpringGreen", "#00FA9A");
        COLORS_MAP_IE.put("MediumTurquoise", "#48D1CC");
        COLORS_MAP_IE.put("MediumVioletRed", "#C71585");
        COLORS_MAP_IE.put("MidnightBlue", "#191970");
        COLORS_MAP_IE.put("MintCream", "#F5FFFA");
        COLORS_MAP_IE.put("MistyRose", "#FFE4E1");
        COLORS_MAP_IE.put("Moccasin", "#FFE4B5");
        COLORS_MAP_IE.put("NavajoWhite", "#FFDEAD");
        COLORS_MAP_IE.put("Navy", "#000080");
        COLORS_MAP_IE.put("OldLace", "#FDF5E6");
        COLORS_MAP_IE.put("Olive", "#808000");
        COLORS_MAP_IE.put("OliveDrab", "#6B8E23");
        COLORS_MAP_IE.put("Orange", "#FFA500");
        COLORS_MAP_IE.put("OrangeRed", "#FF4500");
        COLORS_MAP_IE.put("Orchid", "#DA70D6");
        COLORS_MAP_IE.put("PaleGoldenrod", "#EEE8AA");
        COLORS_MAP_IE.put("PaleGreen", "#98FB98");
        COLORS_MAP_IE.put("PaleTurquoise", "#AFEEEE");
        COLORS_MAP_IE.put("PaleVioletRed", "#DB7093");
        COLORS_MAP_IE.put("PapayaWhip", "#FFEFD5");
        COLORS_MAP_IE.put("PeachPuff", "#FFDAB9");
        COLORS_MAP_IE.put("Peru", "#CD853F");
        COLORS_MAP_IE.put("Pink", "#FFC0CB");
        COLORS_MAP_IE.put("Plum", "#DDA0DD");
        COLORS_MAP_IE.put("PowderBlue", "#B0E0E6");
        COLORS_MAP_IE.put("Purple", "#800080");
        COLORS_MAP_IE.put("Red", "#FF0000");
        COLORS_MAP_IE.put("RosyBrown", "#BC8F8F");
        COLORS_MAP_IE.put("RoyalBlue", "#4169E1");
        COLORS_MAP_IE.put("SaddleBrown", "#8B4513");
        COLORS_MAP_IE.put("Salmon", "#FA8072");
        COLORS_MAP_IE.put("SandyBrown", "#F4A460");
        COLORS_MAP_IE.put("SeaGreen", "#2E8B57");
        COLORS_MAP_IE.put("Seashell", "#FFF5EE");
        COLORS_MAP_IE.put("Sienna", "#A0522D");
        COLORS_MAP_IE.put("Silver", "#C0C0C0");
        COLORS_MAP_IE.put("SkyBlue", "#87CEEB");
        COLORS_MAP_IE.put("SlateBlue", "#6A5ACD");
        COLORS_MAP_IE.put("SlateGray", "#708090");
        COLORS_MAP_IE.put("SlateGrey", "#708090");
        COLORS_MAP_IE.put("Snow", "#FFFAFA");
        COLORS_MAP_IE.put("SpringGreen", "#00FF7F");
        COLORS_MAP_IE.put("SteelBlue", "#4682B4");
        COLORS_MAP_IE.put("Tan", "#D2B48C");
        COLORS_MAP_IE.put("Teal", "#008080");
        COLORS_MAP_IE.put("Thistle", "#D8BFD8");
        COLORS_MAP_IE.put("Tomato", "#FF6347");
        COLORS_MAP_IE.put("Turquoise", "#40E0D0");
        COLORS_MAP_IE.put("Violet", "#EE82EE");
        COLORS_MAP_IE.put("Wheat", "#F5DEB3");
        COLORS_MAP_IE.put("White", "#FFFFFF");
        COLORS_MAP_IE.put("WhiteSmoke", "#F5F5F5");
        COLORS_MAP_IE.put("Yellow", "#FFFF00");
        COLORS_MAP_IE.put("YellowGreen", "#9ACD32");
    }

    /**
     * The value of the "ch" JavaScript attribute for browsers that say that they support it, but do not really
     * provide access to the value of the "char" DOM attribute. Not applicable to all types of HTML elements.
     */
    private String ch_ = "";

    /**
     * The value of the "chOff" JavaScript attribute for browsers that say that they support it, but do not really
     * provide access to the value of the "charOff" DOM attribute. Not applicable to all types of HTML elements.
     */
    private String chOff_ = "";

    /**
     * The tag names of the objects for which innerHTML is read only in IE
     */
    private static final List<String> INNER_HTML_READONLY_IN_IE =
        Arrays.asList(new String[] {
            "col", "colgroup", "frameset", "head", "html", "style", "table",
            "tbody", "tfoot", "thead", "title", "tr"});

    /**
     * The tag names of the objects for which innerText is read only
     */
    private static final List<String> INNER_TEXT_READONLY =
        Arrays.asList(new String[] {"html", "table", "tbody", "tfoot", "thead", "tr"});

    /**
     * The tag names of the objects for which outerHTML is read only
     */
    private static final List<String> OUTER_HTML_READONLY =
        Arrays.asList(new String[] {
            "caption", "col", "colgroup", "frameset", "html",
            "tbody", "td", "tfoot", "th", "thead", "tr"});

    /**
     * Returns the value of the "all" property.
     * @return the value of the "all" property
     */
    @JsxGetter(@WebBrowser(IE))
    public HTMLCollection getAll() {
        if (all_ == null) {
            all_ = new HTMLCollection(getDomNodeOrDie(), false, "HTMLElement.all") {
                @Override
                protected boolean isMatching(final DomNode node) {
                    return true;
                }
            };
        }
        return all_;
    }

    /**
     * Sets the DOM node that corresponds to this JavaScript object.
     * @param domNode the DOM node
     */
    @Override
    public void setDomNode(final DomNode domNode) {
        super.setDomNode(domNode);

        style_ = new CSSStyleDeclaration(this);

        /**
         * Convert JavaScript snippets defined in the attribute map to executable event handlers.
         * Should be called only on construction.
         */
        final HtmlElement htmlElt = (HtmlElement) domNode;
        for (final DomAttr attr : htmlElt.getAttributesMap().values()) {
            final String eventName = attr.getName();
            if (eventName.startsWith("on")) {
                createEventHandler(eventName, attr.getValue());
            }
        }
    }

    /**
     * Create the event handler function from the attribute value.
     * @param eventName the event name (ex: "onclick")
     * @param attrValue the attribute value
     */
    protected void createEventHandler(final String eventName, final String attrValue) {
        final HtmlElement htmlElt = getDomNodeOrDie();
        // TODO: check that it is an "allowed" event for the browser, and take care to the case
        final BaseFunction eventHandler = new EventHandler(htmlElt, eventName, attrValue);
        setEventHandler(eventName, eventHandler);
        // forward onload, onclick, ondblclick, ... to window
        if ((htmlElt instanceof HtmlBody || htmlElt instanceof HtmlFrameSet)) {
            getWindow().getEventListenersContainer()
                .setEventHandlerProp(eventName.substring(2), eventHandler);
        }
    }

    /**
     * Returns the element ID.
     * @return the ID of this element
     */
    @JsxGetter
    public String getId() {
        return getDomNodeOrDie().getId();
    }

    /**
     * Sets the identifier this element.
     * @param newId the new identifier of this element
     */
    @JsxSetter
    public void setId(final String newId) {
        getDomNodeOrDie().setId(newId);
    }

    /**
     * Returns the element title.
     * @return the ID of this element
     */
    @JsxGetter
    public String getTitle() {
        return getDomNodeOrDie().getAttribute("title");
    }

    /**
     * Sets the title of this element.
     * @param newTitle the new identifier of this element
     */
    @JsxSetter
    public void setTitle(final String newTitle) {
        getDomNodeOrDie().setAttribute("title", newTitle);
    }

    /**
     * Returns true if this element is disabled.
     * @return true if this element is disabled
     */
    @JsxGetter(@WebBrowser(IE))
    public boolean getDisabled() {
        return getDomNodeOrDie().hasAttribute("disabled");
    }

    /**
     * Returns the document.
     * @return the document
     */
    @JsxGetter(@WebBrowser(IE))
    public DocumentProxy getDocument() {
        return getWindow().getDocument_js();
    }

    /**
     * Sets whether or not to disable this element.
     * @param disabled True if this is to be disabled
     */
    @JsxSetter(@WebBrowser(IE))
    public void setDisabled(final boolean disabled) {
        final HtmlElement element = getDomNodeOrDie();
        if (disabled) {
            element.setAttribute("disabled", "disabled");
        }
        else {
            element.removeAttribute("disabled");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNamespaceURI() {
        final HtmlElement domNode = getDomNodeOrDie();
        if (getBrowserVersion().hasFeature(GENERATED_65)) {
            return domNode.getNamespaceURI();
        }
        if (domNode.getPage() instanceof HtmlPage) {
            return null;
        }
        return domNode.getNamespaceURI();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLocalName() {
        final DomNode domNode = getDomNodeOrDie();
        if (domNode.getPage() instanceof HtmlPage) {
            final StringBuilder localName = new StringBuilder();
            final String prefix = domNode.getPrefix();
            if (prefix != null) {
                localName.append(prefix);
                localName.append(':');
            }
            localName.append(domNode.getLocalName());
            return localName.toString().toUpperCase();
        }
        return domNode.getLocalName();
    }

    /**
     * Looks at attributes with the given name.
     * {@inheritDoc}
     */
    public Object getWithFallback(final String name) {
        if (!"class".equals(name)) {
            final HtmlElement htmlElement = getDomNodeOrNull();
            if (htmlElement != null && isAttributeName(name)) {
                final String value = htmlElement.getAttribute(name);
                if (DomElement.ATTRIBUTE_NOT_DEFINED != value) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Found attribute for evaluation of property \"" + name
                            + "\" for of " + this);
                    }
                    return value;
                }
            }
        }

        return NOT_FOUND;
    }

    /**
     * Indicates if this is the name of a well defined attribute that can be access as property.
     * Ex: for HtmlInputElement maxlength => false but maxLength => true
     * @param name the name (case sensitive!)
     * @return <code>false</code> if no standard attribute exists with this name
     */
    protected boolean isAttributeName(final String name) {
        // can name be an attribute of current element?
        // first approximation: attribute are all lowercase
        // this should be improved because it's wrong. For instance: tabIndex, hideFocus, acceptCharset
        return name.toLowerCase().equals(name);
    }

    /**
     * For IE, foo.getAttribute(x) uses same names as foo.x.
     * @param attributeName the name
     * @return the real name
     */
    @Override
    protected String fixAttributeName(final String attributeName) {
        if (getBrowserVersion().hasFeature(GENERATED_66)) {
            if ("className".equals(attributeName)) {
                return "class";
            }
            if ("class".equals(attributeName)) {
                return "_class"; // attribute should not be retrieved with "class"
            }
        }

        return attributeName;
    }

    /**
     * An IE-only method which clears all custom attributes.
     */
    @JsxFunction(@WebBrowser(IE))
    public void clearAttributes() {
        final HtmlElement node = getDomNodeOrDie();

        // Remove custom attributes defined directly in HTML.
        final List<String> removals = new ArrayList<String>();
        for (final String attributeName : node.getAttributesMap().keySet()) {
            // Quick hack to figure out what's a "custom" attribute, and what isn't.
            // May not be 100% correct.
            if (!ScriptableObject.hasProperty(getPrototype(), attributeName)) {
                removals.add(attributeName);
            }
        }
        for (final String attributeName : removals) {
            node.removeAttribute(attributeName);
        }

        // Remove custom attributes defined at runtime via JavaScript.
        for (final Object id : getAllIds()) {
            if (id instanceof Integer) {
                final int i = ((Integer) id).intValue();
                delete(i);
            }
            else if (id instanceof String) {
                delete((String) id);
            }
        }
    }

    /**
     * An IE-only method which copies all custom attributes from the specified source element
     * to this element.
     * @param source the source element from which to copy the custom attributes
     * @param preserveIdentity if <tt>false</tt>, the <tt>name</tt> and <tt>id</tt> attributes are not copied
     */
    @JsxFunction(@WebBrowser(IE))
    public void mergeAttributes(final HTMLElement source, final Object preserveIdentity) {
        final HtmlElement src = source.getDomNodeOrDie();
        final HtmlElement target = getDomNodeOrDie();

        // Merge custom attributes defined directly in HTML.
        for (final Map.Entry<String, DomAttr> attribute : src.getAttributesMap().entrySet()) {
            // Quick hack to figure out what's a "custom" attribute, and what isn't.
            // May not be 100% correct.
            final String attributeName = attribute.getKey();
            if (!ScriptableObject.hasProperty(getPrototype(), attributeName)) {
                final String attributeValue = attribute.getValue().getNodeValue();
                target.setAttribute(attributeName, attributeValue);
            }
        }

        // Merge custom attributes defined at runtime via JavaScript.
        for (final Object id : source.getAllIds()) {
            if (id instanceof Integer) {
                final int i = ((Integer) id).intValue();
                put(i, this, source.get(i, source));
            }
            else if (id instanceof String) {
                final String s = (String) id;
                put(s, this, source.get(s, source));
            }
        }

        // Merge ID and name if we aren't preserving identity.
        if (preserveIdentity instanceof Boolean && !((Boolean) preserveIdentity).booleanValue()) {
            target.setId(src.getId());
            target.setAttribute("name", src.getAttribute("name"));
        }
    }

    /**
     * Returns the specified attribute.
     * @param namespaceURI the namespace URI
     * @param localName the local name of the attribute to look for
     * @return the specified attribute, <code>null</code> if the attribute is not defined
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public Object getAttributeNodeNS(final String namespaceURI, final String localName) {
        return getDomNodeOrDie().getAttributeNodeNS(namespaceURI, localName).getScriptObject();
    }

    /**
     * Sets an attribute.
     * See also <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/core.html#ID-F68F082">
     * the DOM reference</a>
     *
     * @param name Name of the attribute to set
     * @param value Value to set the attribute to
     */
    @Override
    @JsxFunction
    public void setAttribute(String name, final String value) {
        name = fixAttributeName(name);
        getDomNodeOrDie().setAttribute(name, value);

        //FF: call corresponding event handler set_onxxx if found
        if (getBrowserVersion().hasFeature(GENERATED_167) && !name.isEmpty()) {
            try {
                name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                final Method method = getClass().getMethod("set" + name, new Class[] {Object.class});
                final String source = "function(){" + value + "}";
                method.invoke(this, new Object[] {
                        Context.getCurrentContext().compileFunction(getWindow(), source, "", 0, null)});
            }
            catch (final NoSuchMethodException e) {
                //silently ignore
            }
            catch (final IllegalAccessException e) {
                //silently ignore
            }
            catch (final InvocationTargetException e) {
                throw new RuntimeException(e.getCause());
            }
        }
    }

    /**
     * Removes the specified attribute.
     * @param attribute the attribute to remove
     */
    @JsxFunction
    public void removeAttributeNode(final Attr attribute) {
        final String name = attribute.getName();
        final String namespaceUri = attribute.getNamespaceURI();
        removeAttributeNS(namespaceUri, name);
    }

    /**
     * Removes this object from the document hierarchy.
     * @param removeChildren whether to remove children or no
     * @return a reference to the object that is removed
     */
    @JsxFunction(@WebBrowser(IE))
    public HTMLElement removeNode(final boolean removeChildren) {
        final HTMLElement parent = (HTMLElement) getParentElement();
        if (parent != null) {
            parent.removeChild(this);
            if (!removeChildren) {
                final NodeList collection = getChildNodes();
                final int length = collection.getLength();
                for (int i = 0; i < length; i++) {
                    final Node object = (Node) collection.item(Integer.valueOf(0));
                    parent.appendChild(object);
                }
            }
        }
        return this;
    }

    /**
     * Gets the attribute node for the specified attribute.
     * @param attributeName the name of the attribute to retrieve
     * @return the attribute node for the specified attribute
     */
    @Override
    @JsxFunction
    public Object getAttributeNode(final String attributeName) {
        return ((NamedNodeMap) getAttributes()).getNamedItem(attributeName);
    }

    /**
     * Sets the attribute node for the specified attribute.
     * @param newAtt the attribute to set
     * @return the replaced attribute node, if any
     */
    @JsxFunction
    public Attr setAttributeNode(final Attr newAtt) {
        final String name = newAtt.getName();

        final Attr replacedAtt;
        final boolean undefForClass = getBrowserVersion().
                hasFeature(JS_SET_ATTRIBUTE_CONSIDERS_ATTR_FOR_CLASS_AS_REAL);
        if (undefForClass) {
            replacedAtt = (Attr) getAttributeNode(name);
        }
        else {
            final NamedNodeMap nodes = (NamedNodeMap) getAttributes();
            replacedAtt = (Attr) nodes.getNamedItemWithoutSytheticClassAttr(name);
        }
        if (replacedAtt != null) {
            replacedAtt.detachFromParent();
        }

        final DomAttr newDomAttr = newAtt.getDomNodeOrDie();
        getDomNodeOrDie().setAttributeNode(newDomAttr);
        return replacedAtt;
    }

    /**
     * Returns all the descendant elements with the specified class.
     * @param className the name to search for
     * @return all the descendant elements with the specified class name
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public HTMLCollection getElementsByClassName(final String className) {
        final HtmlElement elt = getDomNodeOrDie();
        final String description = "HTMLElement.getElementsByClassName('" + className + "')";
        final String[] classNames = CLASS_NAMES_SPLIT_PATTERN.split(className, 0);

        final HTMLCollection collection = new HTMLCollection(elt, true, description) {
            @Override
            protected boolean isMatching(final DomNode node) {
                if (!(node instanceof HtmlElement)) {
                    return false;
                }
                final HtmlElement elt = (HtmlElement) node;
                String classAttribute = elt.getAttribute("class");
                if (classAttribute == DomElement.ATTRIBUTE_NOT_DEFINED) {
                    return false; // probably better performance as most of elements won't have a class attribute
                }

                classAttribute = " " + classAttribute + " ";
                for (final String aClassName : classNames) {
                    if (!classAttribute.contains(" " + aClassName + " ")) {
                        return false;
                    }
                }
                return true;
            }
        };

        return collection;
    }

    /**
     * Returns the class defined for this element.
     * @return the class name
     */
    @JsxGetter(propertyName = "className")
    public Object getClassName_js() {
        return getDomNodeOrDie().getAttribute("class");
    }

    /**
     * Returns "clientHeight" attribute.
     * @return the "clientHeight" attribute
     */
    @JsxGetter
    public int getClientHeight() {
        final boolean includePadding = !getBrowserVersion().hasFeature(JS_ELEMENT_EXTENT_WITHOUT_PADDING);
        final ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
        return style.getCalculatedHeight(false, includePadding);
    }

    /**
     * Returns "clientWidth" attribute.
     * @return the "clientWidth" attribute
     */
    @JsxGetter
    public int getClientWidth() {
        final boolean includePadding = !getBrowserVersion().hasFeature(JS_ELEMENT_EXTENT_WITHOUT_PADDING);
        final ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
        return style.getCalculatedWidth(false, includePadding);
    }

    /**
     * Sets the class attribute for this element.
     * @param className the new class name
     */
    @JsxSetter
    public void setClassName(final String className) {
        getDomNodeOrDie().setAttribute("class", className);
    }

    /**
     * Gets the innerHTML attribute.
     * @return the contents of this node as HTML
     */
    @JsxGetter
    public String getInnerHTML() {
        final StringBuilder buf = new StringBuilder();
        // we can't rely on DomNode.asXml because it adds indentation and new lines
        printChildren(buf, getDomNodeOrDie(), !"SCRIPT".equals(getTagName()));
        return buf.toString();
    }

    /**
     * Gets the innerText attribute.
     * @return the contents of this node as text
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(CHROME) })
    public String getInnerText() {
        final StringBuilder buf = new StringBuilder();
        // we can't rely on DomNode.asXml because it adds indentation and new lines
        printChildren(buf, getDomNodeOrDie(), false);
        return buf.toString();
    }

    /**
     * Gets the outerHTML of the node.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534310.aspx">MSDN documentation</a>
     * @return the contents of this node as HTML
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(CHROME) })
    public String getOuterHTML() {
        final StringBuilder buf = new StringBuilder();
        // we can't rely on DomNode.asXml because it adds indentation and new lines
        printNode(buf, getDomNodeOrDie(), true);
        return buf.toString();
    }

    private void printChildren(final StringBuilder buffer, final DomNode node, final boolean html) {
        for (final DomNode child : node.getChildren()) {
            printNode(buffer, child, html);
        }
    }

    private void printNode(final StringBuilder buffer, final DomNode node, final boolean html) {
        if (node instanceof DomComment) {
            if (html) {
                // Remove whitespace sequences.
                final String s = PRINT_NODE_PATTERN.matcher(node.getNodeValue()).replaceAll(" ");
                buffer.append("<!--").append(s).append("-->");
            }
        }
        else if (node instanceof DomCharacterData) {
            // Remove whitespace sequences, possibly escape XML characters.
            String s = node.getNodeValue();
            if (getBrowserVersion().hasFeature(JS_INNER_HTML_REDUCE_WHITESPACES)) {
                s = PRINT_NODE_PATTERN.matcher(s).replaceAll(" ");
            }
            if (html) {
                s = com.gargoylesoftware.htmlunit.util.StringUtils.escapeXmlChars(s);
            }
            buffer.append(s);
        }
        else if (html) {
            // Start the tag name. IE does it in uppercase, FF in lowercase.
            final HtmlElement element = (HtmlElement) node;
            final boolean ie = getBrowserVersion().hasFeature(GENERATED_69);
            String tag = element.getTagName();
            if (ie) {
                tag = tag.toUpperCase();
            }
            buffer.append("<").append(tag);
            // Add the attributes. IE does not use quotes, FF does.
            for (final DomAttr attr : element.getAttributesMap().values()) {
                final String name = attr.getName();
                final String value = PRINT_NODE_QUOTE_PATTERN.matcher(attr.getValue()).replaceAll("&quot;");
                final boolean quote = !ie
                    || com.gargoylesoftware.htmlunit.util.StringUtils.containsWhitespace(value)
                    || value.isEmpty()
                    || (element instanceof HtmlAnchor && "href".equals(name));
                buffer.append(' ').append(name).append("=");
                if (quote) {
                    buffer.append("\"");
                }
                buffer.append(value);
                if (quote) {
                    buffer.append("\"");
                }
            }
            buffer.append(">");
            // Add the children.
            printChildren(buffer, node, html);
            // Close the tag. IE does it in uppercase, FF in lowercase.
            buffer.append("</").append(tag).append(">");
        }
        else {
            final HtmlElement element = (HtmlElement) node;
            if ("p".equals(element.getTagName())) {
                buffer.append("\r\n"); // \r\n because it's to implement something IE specific
            }
            if (!"script".equals(element.getTagName())) {
                printChildren(buffer, node, html);
            }
        }
    }

    /**
     * Replace all children elements of this element with the supplied value.
     * @param value the new value for the contents of this node
     */
    @JsxSetter
    public void setInnerHTML(final Object value) {
        final DomNode domNode = getDomNodeOrDie();
        final boolean ie = getBrowserVersion().hasFeature(GENERATED_70);

        if (ie && INNER_HTML_READONLY_IN_IE.contains(domNode.getNodeName())) {
            throw Context.reportRuntimeError("innerHTML is read-only for tag " + domNode.getNodeName());
        }

        domNode.removeAllChildren();

        // null && IE     -> add child
        // null && non-IE -> Don't add
        // ''             -> Don't add
        if ((value == null && ie) || (value != null && !"".equals(value))) {

            final String valueAsString = Context.toString(value);
            parseHtmlSnippet(domNode, true, valueAsString);

            //if the parentNode has null parentNode in IE,
            //create a DocumentFragment to be the parentNode's parentNode.
            if (domNode.getParentNode() == null && ie) {
                final DomDocumentFragment fragment = ((HtmlPage) domNode.getPage()).createDomDocumentFragment();
                fragment.appendChild(domNode);
            }
        }
    }

    /**
     * Replace all children elements of this element with the supplied value.
     * @param value the new value for the contents of this node
     */
    @JsxSetter({ @WebBrowser(IE), @WebBrowser(CHROME) })
    public void setInnerText(final String value) {
        setInnerTextImpl(Context.toString(value));
    }

    private void setInnerTextImpl(final String value) {
        final DomNode domNode = getDomNodeOrDie();

        if (INNER_TEXT_READONLY.contains(domNode.getNodeName())) {
            throw Context.reportRuntimeError("innerText is read-only for tag " + domNode.getNodeName());
        }

        domNode.removeAllChildren();

        if (value != null && !value.isEmpty()) {
            domNode.appendChild(new DomText(domNode.getPage(), Context.toString(value)));
        }

        //if the parentNode has null parentNode in IE,
        //create a DocumentFragment to be the parentNode's parentNode.
        if (domNode.getParentNode() == null && getBrowserVersion().hasFeature(GENERATED_71)) {
            final DomDocumentFragment fragment = ((HtmlPage) domNode.getPage()).createDomDocumentFragment();
            fragment.appendChild(domNode);
        }
    }

    /**
     * Replace all children elements of this element with the supplied value.
     * @param value the new value for the contents of this node
     */
    @Override
    public void setTextContent(final Object value) {
        setInnerTextImpl(value == null ? null : Context.toString(value));
    }

    /**
     * Replace all children elements of this element with the supplied value.
     * Sets the outerHTML of the node.
     * @param value the new value for replacing this node
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534310.aspx">MSDN documentation</a>
     */
    @JsxSetter({ @WebBrowser(IE), @WebBrowser(CHROME) })
    public void setOuterHTML(final String value) {
        final DomNode domNode = getDomNodeOrDie();

        if (OUTER_HTML_READONLY.contains(domNode.getNodeName())) {
            throw Context.reportRuntimeError("outerHTML is read-only for tag " + domNode.getNodeName());
        }

        final DomNode proxyNode = new ProxyDomNode(domNode.getPage(), domNode, false);
        parseHtmlSnippet(proxyNode, false, value);
        domNode.remove();
    }

    /**
     * Parses the specified HTML source code, appending the resultant content at the specified target location.
     * @param target the node indicating the position at which the parsed content should be placed
     * @param append if <tt>true</tt>, append the parsed content as a child of the specified target;
     *               if <tt>false</tt>, append the parsed content as the previous sibling of the specified target
     * @param source the HTML code extract to parse
     */
    public static void parseHtmlSnippet(final DomNode target, final boolean append, final String source) {
        try {
            HTMLParser.parseFragment(target, source);
        }
        catch (final IOException e) {
            LogFactory.getLog(HtmlElement.class).error("Unexpected exception occurred while parsing HTML snippet", e);
            throw Context.reportRuntimeError("Unexpected exception occurred while parsing HTML snippet: "
                    + e.getMessage());
        }
        catch (final SAXException e) {
            LogFactory.getLog(HtmlElement.class).error("Unexpected exception occurred while parsing HTML snippet", e);
            throw Context.reportRuntimeError("Unexpected exception occurred while parsing HTML snippet: "
                    + e.getMessage());
        }
    }

    /**
     * ProxyDomNode.
     */
    public static class ProxyDomNode extends HtmlDivision {

        private final DomNode target_;
        private final boolean append_;

        /**
         * Constructor.
         * @param page the page
         * @param target the target
         * @param append append or no
         */
        public ProxyDomNode(final SgmlPage page, final DomNode target, final boolean append) {
            super(null, HtmlDivision.TAG_NAME, page, null);
            target_ = target;
            append_ = append;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public DomNode appendChild(final org.w3c.dom.Node node) {
            final DomNode domNode = (DomNode) node;
            if (append_) {
                return target_.appendChild(domNode);
            }
            target_.insertBefore(domNode);
            return domNode;
        }

        /**
         * Gets wrapped DomNode.
         * @return the node
         */
        public DomNode getDomNode() {
            return target_;
        }

        /**
         * Returns append or not.
         * @return append or not
         */
        public boolean isAppend() {
            return append_;
        }
    }

    /**
     * Gets the attributes of the element in the form of a {@link org.xml.sax.Attributes}.
     * @param element the element to read the attributes from
     * @return the attributes
     */
    protected AttributesImpl readAttributes(final HtmlElement element) {
        final AttributesImpl attributes = new AttributesImpl();
        for (final DomAttr entry : element.getAttributesMap().values()) {
            final String name = entry.getName();
            final String value = entry.getValue();
            attributes.addAttribute(null, name, name, null, value);
        }

        return attributes;
    }

    /**
     * Inserts the given HTML text into the element at the location.
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms536452.aspx">
     * MSDN documentation</a>
     * @param where specifies where to insert the HTML text, using one of the following value:
     *        beforeBegin, afterBegin, beforeEnd, afterEnd
     * @param text the HTML text to insert
     */
    @JsxFunction(@WebBrowser(IE))
    public void insertAdjacentHTML(final String where, final String text) {
        final Object[] values = getInsertAdjacentLocation(where);
        final DomNode node = (DomNode) values[0];
        final boolean append = ((Boolean) values[1]).booleanValue();

        // add the new nodes
        final DomNode proxyNode = new ProxyDomNode(node.getPage(), node, append);
        parseHtmlSnippet(proxyNode, append, text);
    }

    /**
     * Inserts the given element into the element at the location.
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms536451.aspx">
     * MSDN documentation</a>
     * @param where specifies where to insert the element, using one of the following value:
     *        beforeBegin, afterBegin, beforeEnd, afterEnd
     * @param object the element to insert
     * @return an element object
     */
    @JsxFunction(@WebBrowser(IE))
    public Object insertAdjacentElement(final String where, final Object object) {
        if (object instanceof Node) {
            final DomNode childNode = ((Node) object).getDomNodeOrDie();
            final Object[] values = getInsertAdjacentLocation(where);
            final DomNode node = (DomNode) values[0];
            final boolean append = ((Boolean) values[1]).booleanValue();

            if (append) {
                node.appendChild(childNode);
            }
            else {
                node.insertBefore(childNode);
            }
            return object;
        }
        throw Context.reportRuntimeError("Passed object is not an element: " + object);
    }

    /**
     * Returns where and how to add the new node.
     * Used by {@link #insertAdjacentHTML(String, String)} and
     * {@link #insertAdjacentElement(String, Object)}.
     *
     * @param where specifies where to insert the element, using one of the following value:
     *        beforeBegin, afterBegin, beforeEnd, afterEnd
     *
     * @return an array of 1-DomNode:parentNode and 2-Boolean:append
     */
    private Object[] getInsertAdjacentLocation(final String where) {
        final DomNode currentNode = getDomNodeOrDie();
        final DomNode node;
        final boolean append;

        // compute the where and how the new nodes should be added
        if (POSITION_AFTER_BEGIN.equalsIgnoreCase(where)) {
            if (currentNode.getFirstChild() == null) {
                // new nodes should appended to the children of current node
                node = currentNode;
                append = true;
            }
            else {
                // new nodes should be inserted before first child
                node = currentNode.getFirstChild();
                append = false;
            }
        }
        else if (POSITION_BEFORE_BEGIN.equalsIgnoreCase(where)) {
            // new nodes should be inserted before current node
            node = currentNode;
            append = false;
        }
        else if (POSITION_BEFORE_END.equalsIgnoreCase(where)) {
            // new nodes should appended to the children of current node
            node = currentNode;
            append = true;
        }
        else if (POSITION_AFTER_END.equalsIgnoreCase(where)) {
            if (currentNode.getNextSibling() == null) {
                // new nodes should appended to the children of parent node
                node = currentNode.getParentNode();
                append = true;
            }
            else {
                // new nodes should be inserted before current node's next sibling
                node = currentNode.getNextSibling();
                append = false;
            }
        }
        else {
            throw Context.reportRuntimeError("Illegal position value: \"" + where + "\"");
        }

        if (append) {
            return new Object[] {node, Boolean.TRUE};
        }
        return new Object[] {node, Boolean.FALSE};
    }

    /**
     * Adds the specified behavior to this HTML element. Currently only supports
     * the following default IE behaviors:
     * <ul>
     *   <li>#default#clientCaps</li>
     *   <li>#default#homePage</li>
     *   <li>#default#download</li>
     * </ul>
     * @param behavior the URL of the behavior to add, or a default behavior name
     * @return an identifier that can be user later to detach the behavior from the element
     */
    @JsxFunction(@WebBrowser(IE))
    public int addBehavior(final String behavior) {
        // if behavior already defined, then nothing to do
        if (behaviors_.contains(behavior)) {
            return 0;
        }

        final Class<? extends HTMLElement> c = getClass();
        if (BEHAVIOR_CLIENT_CAPS.equalsIgnoreCase(behavior)) {
            defineProperty("availHeight", c, 0);
            defineProperty("availWidth", c, 0);
            defineProperty("bufferDepth", c, 0);
            defineProperty("colorDepth", c, 0);
            defineProperty("connectionType", c, 0);
            defineProperty("cookieEnabled", c, 0);
            defineProperty("cpuClass", c, 0);
            defineProperty("height", c, 0);
            defineProperty("javaEnabled", c, 0);
            defineProperty("platform", c, 0);
            defineProperty("systemLanguage", c, 0);
            defineProperty("userLanguage", c, 0);
            defineProperty("width", c, 0);
            defineFunctionProperties(new String[] {"addComponentRequest"}, c, 0);
            defineFunctionProperties(new String[] {"clearComponentRequest"}, c, 0);
            defineFunctionProperties(new String[] {"compareVersions"}, c, 0);
            defineFunctionProperties(new String[] {"doComponentRequest"}, c, 0);
            defineFunctionProperties(new String[] {"getComponentVersion"}, c, 0);
            defineFunctionProperties(new String[] {"isComponentInstalled"}, c, 0);
            behaviors_.add(BEHAVIOR_CLIENT_CAPS);
            return BEHAVIOR_ID_CLIENT_CAPS;
        }
        else if (BEHAVIOR_HOMEPAGE.equalsIgnoreCase(behavior)) {
            defineFunctionProperties(new String[] {"isHomePage"}, c, 0);
            defineFunctionProperties(new String[] {"setHomePage"}, c, 0);
            defineFunctionProperties(new String[] {"navigateHomePage"}, c, 0);
            behaviors_.add(BEHAVIOR_CLIENT_CAPS);
            return BEHAVIOR_ID_HOMEPAGE;
        }
        else if (BEHAVIOR_DOWNLOAD.equalsIgnoreCase(behavior)) {
            defineFunctionProperties(new String[] {"startDownload"}, c, 0);
            behaviors_.add(BEHAVIOR_DOWNLOAD);
            return BEHAVIOR_ID_DOWNLOAD;
        }
        else {
            LOG.warn("Unimplemented behavior: " + behavior);
            return BEHAVIOR_ID_UNKNOWN;
        }
    }

    /**
     * Removes the behavior corresponding to the specified identifier from this element.
     * @param id the identifier for the behavior to remove
     */
    @JsxFunction(@WebBrowser(IE))
    public void removeBehavior(final int id) {
        switch (id) {
            case BEHAVIOR_ID_CLIENT_CAPS:
                delete("availHeight");
                delete("availWidth");
                delete("bufferDepth");
                delete("colorDepth");
                delete("connectionType");
                delete("cookieEnabled");
                delete("cpuClass");
                delete("height");
                delete("javaEnabled");
                delete("platform");
                delete("systemLanguage");
                delete("userLanguage");
                delete("width");
                delete("addComponentRequest");
                delete("clearComponentRequest");
                delete("compareVersions");
                delete("doComponentRequest");
                delete("getComponentVersion");
                delete("isComponentInstalled");
                behaviors_.remove(BEHAVIOR_CLIENT_CAPS);
                break;
            case BEHAVIOR_ID_HOMEPAGE:
                delete("isHomePage");
                delete("setHomePage");
                delete("navigateHomePage");
                behaviors_.remove(BEHAVIOR_HOMEPAGE);
                break;
            case BEHAVIOR_ID_DOWNLOAD:
                delete("startDownload");
                behaviors_.remove(BEHAVIOR_DOWNLOAD);
                break;
            default:
                LOG.warn("Unexpected behavior id: " + id + ". Ignoring.");
        }
    }

    //----------------------- START #default#clientCaps BEHAVIOR -----------------------

    /**
     * Returns the screen's available height. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the screen's available height
     */
    public int getAvailHeight() {
        return getWindow().getScreen().getAvailHeight();
    }

    /**
     * Returns the screen's available width. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the screen's available width
     */
    public int getAvailWidth() {
        return getWindow().getScreen().getAvailWidth();
    }

    /**
     * Returns the screen's buffer depth. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the screen's buffer depth
     */
    public int getBufferDepth() {
        return getWindow().getScreen().getBufferDepth();
    }

    /**
     * Returns the BoxObject for this element.
     * @return the BoxObject for this element
     */
    public BoxObject getBoxObject() {
        if (boxObject_ == null) {
            boxObject_ = new BoxObject(this);
            boxObject_.setParentScope(getWindow());
            boxObject_.setPrototype(getPrototype(boxObject_.getClass()));
        }
        return boxObject_;
    }

    /**
     * Returns the screen's color depth. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the screen's color depth
     */
    public int getColorDepth() {
        return getWindow().getScreen().getColorDepth();
    }

    /**
     * Returns the connection type being used. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the connection type being used
     * Current implementation always return "modem"
     */
    public String getConnectionType() {
        return "modem";
    }

    /**
     * Returns <tt>true</tt> if cookies are enabled. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return whether or not cookies are enabled
     */
    public boolean getCookieEnabled() {
        return getWindow().getNavigator().getCookieEnabled();
    }

    /**
     * Returns the type of CPU used. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the type of CPU used
     */
    public String getCpuClass() {
        return getWindow().getNavigator().getCpuClass();
    }

    /**
     * Returns the screen's height. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the screen's height
     */
    public int getHeight() {
        return getWindow().getScreen().getHeight();
    }

    /**
     * Returns <tt>true</tt> if Java is enabled. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return whether or not Java is enabled
     */
    public boolean getJavaEnabled() {
        return getWindow().getNavigator().javaEnabled();
    }

    /**
     * Returns the platform used. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the platform used
     */
    public String getPlatform() {
        return getWindow().getNavigator().getPlatform();
    }

    /**
     * Returns the system language. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the system language
     */
    public String getSystemLanguage() {
        return getWindow().getNavigator().getSystemLanguage();
    }

    /**
     * Returns the user language. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the user language
     */
    public String getUserLanguage() {
        return getWindow().getNavigator().getUserLanguage();
    }

    /**
     * Returns the screen's width. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return the screen's width
     */
    public int getWidth() {
        return getWindow().getScreen().getWidth();
    }

    /**
     * Adds the specified component to the queue of components to be installed. Note
     * that no components ever get installed, and this call is always ignored. Part of
     * the <tt>#default#clientCaps</tt> default IE behavior implementation.
     * @param id the identifier for the component to install
     * @param idType the type of identifier specified
     * @param minVersion the minimum version of the component to install
     */
    public void addComponentRequest(final String id, final String idType, final String minVersion) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Call to addComponentRequest(" + id + ", " + idType + ", " + minVersion + ") ignored.");
        }
    }

    /**
     * Clears the component install queue of all component requests. Note that no components
     * ever get installed, and this call is always ignored. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     */
    public void clearComponentRequest() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Call to clearComponentRequest() ignored.");
        }
    }

    /**
     * Compares the two specified version numbers. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @param v1 the first of the two version numbers to compare
     * @param v2 the second of the two version numbers to compare
     * @return -1 if v1 < v2, 0 if v1 = v2, and 1 if v1 > v2
     */
    public int compareVersions(final String v1, final String v2) {
        final int i = v1.compareTo(v2);
        if (i == 0) { return 0; }
        else if (i < 0) { return -1; }
        else { return 1; }
    }

    /**
     * Downloads all the components queued via {@link #addComponentRequest(String, String, String)}.
     * @return <tt>true</tt> if the components are downloaded successfully
     * Current implementation always return <code>false</code>
     */
    public boolean doComponentRequest() {
        return false;
    }

    /**
     * Returns the version of the specified component.
     * @param id the identifier for the component whose version is to be returned
     * @param idType the type of identifier specified
     * @return the version of the specified component
     */
    public String getComponentVersion(final String id, final String idType) {
        if ("{E5D12C4E-7B4F-11D3-B5C9-0050045C3C96}".equals(id)) {
            // Yahoo Messenger.
            return "";
        }
        // Everything else.
        return "1.0";
    }

    /**
     * Returns <tt>true</tt> if the specified component is installed.
     * @param id the identifier for the component to check for
     * @param idType the type of id specified
     * @param minVersion the minimum version to check for
     * @return <tt>true</tt> if the specified component is installed
     */
    public boolean isComponentInstalled(final String id, final String idType, final String minVersion) {
        return false;
    }

    //----------------------- START #default#download BEHAVIOR -----------------------

    /**
     * Implementation of the IE behavior #default#download.
     * @param uri the URI of the download source
     * @param callback the method which should be called when the download is finished
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms531406.aspx">MSDN documentation</a>
     * @throws MalformedURLException if the URL cannot be created
     */
    public void startDownload(final String uri, final Function callback) throws MalformedURLException {
        final HtmlPage page = (HtmlPage) getWindow().getWebWindow().getEnclosedPage();
        final URL url = page.getFullyQualifiedUrl(uri);
        if (!page.getUrl().getHost().equals(url.getHost())) {
            throw Context.reportRuntimeError("Not authorized url: " + url);
        }
        final JavaScriptJob job = BackgroundJavaScriptFactory.theFactory().
                createDownloadBehaviorJob(url, callback, getWindow().getWebWindow().getWebClient());
        page.getEnclosingWindow().getJobManager().addJob(job, page);
    }

    //----------------------- END #default#download BEHAVIOR -----------------------

    //----------------------- START #default#homePage BEHAVIOR -----------------------

    /**
     * Returns <tt>true</tt> if the specified URL is the web client's current
     * homepage and the document calling the method is on the same domain as the
     * user's homepage. Part of the <tt>#default#homePage</tt> default IE behavior
     * implementation.
     * @param url the URL to check
     * @return <tt>true</tt> if the specified URL is the current homepage
     */
    public boolean isHomePage(final String url) {
        try {
            final URL newUrl = new URL(url);
            final URL currentUrl = getDomNodeOrDie().getPage().getUrl();
            final String home = getDomNodeOrDie().getPage().getEnclosingWindow()
                    .getWebClient().getOptions().getHomePage();
            final boolean sameDomains = newUrl.getHost().equalsIgnoreCase(currentUrl.getHost());
            final boolean isHomePage = (home != null && home.equals(url));
            return (sameDomains && isHomePage);
        }
        catch (final MalformedURLException e) {
            return false;
        }
    }

    /**
     * Sets the web client's current homepage. Part of the <tt>#default#homePage</tt>
     * default IE behavior implementation.
     * @param url the new homepage URL
     */
    public void setHomePage(final String url) {
        getDomNodeOrDie().getPage().getEnclosingWindow().getWebClient().getOptions().setHomePage(url);
    }

    /**
     * Causes the web client to navigate to the current home page. Part of the
     * <tt>#default#homePage</tt> default IE behavior implementation.
     * @throws IOException if loading home page fails
     */
    public void navigateHomePage() throws IOException {
        final WebClient webClient = getDomNodeOrDie().getPage().getEnclosingWindow().getWebClient();
        webClient.getPage(webClient.getOptions().getHomePage());
    }

    //----------------------- END #default#homePage BEHAVIOR -----------------------

    /**
     * Returns this element's <tt>offsetHeight</tt>, which is the element height plus the element's padding
     * plus the element's border. This method returns a dummy value compatible with mouse event coordinates
     * during mouse events.
     * @return this element's <tt>offsetHeight</tt>
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms534199.aspx">MSDN Documentation</a>
     * @see <a href="http://www.quirksmode.org/js/elementdimensions.html">Element Dimensions</a>
     */
    @JsxGetter
    public int getOffsetHeight() {
        final MouseEvent event = MouseEvent.getCurrentMouseEvent();
        if (isAncestorOfEventTarget(event)) {
            // compute appropriate offset height to pretend mouse event was produced within this element
            return event.getClientY() - getPosY() + 50;
        }
        return getCurrentStyle().getCalculatedHeight(true, true);
    }

    /**
     * Returns this element's <tt>offsetWidth</tt>, which is the element width plus the element's padding
     * plus the element's border. This method returns a dummy value compatible with mouse event coordinates
     * during mouse events.
     * @return this element's <tt>offsetWidth</tt>
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms534304.aspx">MSDN Documentation</a>
     * @see <a href="http://www.quirksmode.org/js/elementdimensions.html">Element Dimensions</a>
     */
    @JsxGetter
    public int getOffsetWidth() {
        final MouseEvent event = MouseEvent.getCurrentMouseEvent();
        if (isAncestorOfEventTarget(event)) {
            // compute appropriate offset width to pretend mouse event was produced within this element
            return event.getClientX() - getPosX() + 50;
        }
        return getCurrentStyle().getCalculatedWidth(true, true);
    }

    /**
     * Returns <tt>true</tt> if this element's node is an ancestor of the specified event's target node.
     * @param event the event whose target node is to be checked
     * @return <tt>true</tt> if this element's node is an ancestor of the specified event's target node
     */
    protected boolean isAncestorOfEventTarget(final MouseEvent event) {
        if (event == null) {
            return false;
        }
        else if (!(event.getSrcElement() instanceof HTMLElement)) {
            return false;
        }
        final HTMLElement srcElement = (HTMLElement) event.getSrcElement();
        return getDomNodeOrDie().isAncestorOf(srcElement.getDomNodeOrDie());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "HTMLElement for " + getDomNodeOrNull();
    }

    /**
     * Gets the scrollTop value for this element.
     * @return the scrollTop value for this element
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534618.aspx">MSDN documentation</a>
     */
    @JsxGetter
    public int getScrollTop() {
        // It's easier to perform these checks and adjustments in the getter, rather than in the setter,
        // because modifying the CSS style of the element is supposed to affect the attribute value.
        if (scrollTop_ < 0) {
            scrollTop_ = 0;
        }
        else if (scrollTop_ > 0) {
            if (!getCurrentStyle().isScrollable(false)) {
                scrollTop_ = 0;
            }
        }
        return scrollTop_;
    }

    /**
     * Sets the scrollTop value for this element.
     * @param scroll the scrollTop value for this element
     */
    @JsxSetter
    public void setScrollTop(final int scroll) {
        scrollTop_ = scroll;
    }

    /**
     * Gets the scrollLeft value for this element.
     * @return the scrollLeft value for this element
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534617.aspx">MSDN documentation</a>
     */
    @JsxGetter
    public int getScrollLeft() {
        // It's easier to perform these checks and adjustments in the getter, rather than in the setter,
        // because modifying the CSS style of the element is supposed to affect the attribute value.
        if (scrollLeft_ < 0) {
            scrollLeft_ = 0;
        }
        else if (scrollLeft_ > 0) {
            if (!getCurrentStyle().isScrollable(true)) {
                scrollLeft_ = 0;
            }
        }
        return scrollLeft_;
    }

    /**
     * Sets the scrollLeft value for this element.
     * @param scroll the scrollLeft value for this element
     */
    @JsxSetter
    public void setScrollLeft(final int scroll) {
        scrollLeft_ = scroll;
    }

    /**
     * Gets the scrollHeight for this element.
     * @return a dummy value of 10
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534615.aspx">MSDN documentation</a>
     */
    @JsxGetter
    public int getScrollHeight() {
        return 10;
    }

    /**
     * Gets the scrollWidth for this element.
     * @return a dummy value of 10
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534619.aspx">MSDN documentation</a>
     */
    @JsxGetter
    public int getScrollWidth() {
        return 10;
    }

    /**
     * Gets the namespace defined for the element.
     * @return the namespace defined for the element
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534388.aspx">MSDN documentation</a>
     */
    @JsxGetter(@WebBrowser(IE))
    public String getScopeName() {
        final String prefix = getDomNodeOrDie().getPrefix();
        return prefix != null ? prefix : "HTML";
    }

    /**
     * Gets the Uniform Resource Name (URN) specified in the namespace declaration.
     * @return the Uniform Resource Name (URN) specified in the namespace declaration
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534658.aspx">MSDN documentation</a>
     */
    @JsxGetter(@WebBrowser(IE))
    public String getTagUrn() {
        final String urn = getDomNodeOrDie().getNamespaceURI();
        return urn != null ? urn : "";
    }

    /**
     * Sets the Uniform Resource Name (URN) specified in the namespace declaration.
     * @param tagUrn the Uniform Resource Name (URN) specified in the namespace declaration
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534658.aspx">MSDN documentation</a>
     */
    @JsxSetter(@WebBrowser(IE))
    public void setTagUrn(final String tagUrn) {
        throw Context.reportRuntimeError("Error trying to set tagUrn to '" + tagUrn + "'.");
    }

    /**
     * Gets the first ancestor instance of {@link HTMLElement}. It is mostly identical
     * to {@link #getParent()} except that it skips XML nodes.
     * @return the parent HTML element
     * @see #getParent()
     */
    public HTMLElement getParentHTMLElement() {
        Node parent = getParent();
        while (parent != null && !(parent instanceof HTMLElement)) {
            parent = parent.getParent();
        }
        return (HTMLElement) parent;
    }

    /**
     * Implement the scrollIntoView() JavaScript function but don't actually do
     * anything. The requirement
     * is just to prevent scripts that call that method from failing
     */
    @JsxFunction
    public void scrollIntoView() { }

    /**
     * Retrieves a collection of rectangles that describes the layout of the contents of an object
     * or range within the client. Each rectangle describes a single line.
     * @return a collection of rectangles that describes the layout of the contents
     */
    @JsxFunction(@WebBrowser(IE))
    public Object getClientRects() {
        return new NativeArray(0);
    }

    /**
     * Sets an expression for the specified HTMLElement.
     *
     * @param propertyName Specifies the name of the property to which expression is added
     * @param expression specifies any valid script statement without quotations or semicolons
     *        This string can include references to other properties on the current page.
     *        Array references are not allowed on object properties included in this script.
     * @param language specified the language used
     */
    @JsxFunction(@WebBrowser(IE))
    public void setExpression(final String propertyName, final String expression, final String language) {
        // Empty.
    }

    /**
     * Removes the expression from the specified property.
     *
     * @param propertyName Specifies the name of the property from which to remove an expression
     * @return true if the expression was successfully removed
     */
    @JsxFunction(@WebBrowser(IE))
    public boolean removeExpression(final String propertyName) {
        return true;
    }

    /**
     * Retrieves an auto-generated, unique identifier for the object.
     * <b>Note</b> The unique ID generated is not guaranteed to be the same every time the page is loaded.
     * @return an auto-generated, unique identifier for the object
     */
    @JsxGetter(@WebBrowser(IE))
    public String getUniqueID() {
        if (uniqueID_ == null) {
            uniqueID_ = "ms__id" + UniqueID_Counter_++;
        }
        return uniqueID_;
    }

    /**
     * Dispatches an event into the event system (standards-conformant browsers only). See
     * <a href="https://developer.mozilla.org/en-US/docs/DOM/element.dispatchEvent">the Gecko
     * DOM reference</a> for more information.
     *
     * @param event the event to be dispatched
     * @return <tt>false</tt> if at least one of the event handlers which handled the event
     *         called <tt>preventDefault</tt>; <tt>true</tt> otherwise
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public boolean dispatchEvent(final Event event) {
        event.setTarget(this);
        final HtmlElement element = getDomNodeOrDie();
        ScriptResult result = null;
        if (event.getType().equals(MouseEvent.TYPE_CLICK)) {
            try {
                element.click(event);
            }
            catch (final IOException e) {
                throw Context.reportRuntimeError("Error calling click(): " + e.getMessage());
            }
        }
        else {
            result = fireEvent(event);
        }
        return !event.isAborted(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final HtmlElement getDomNodeOrDie() {
        return (HtmlElement) super.getDomNodeOrDie();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public HtmlElement getDomNodeOrNull() {
        return (HtmlElement) super.getDomNodeOrNull();
    }

    /**
     * Remove focus from this element.
     */
    @JsxFunction
    public void blur() {
        getDomNodeOrDie().blur();
    }

    /**
     * Creates a new TextRange object for this element.
     * @return a new TextRange object for this element
     */
    @JsxFunction(@WebBrowser(IE))
    public Object createTextRange() {
        final TextRange range = new TextRange(this);
        range.setParentScope(getParentScope());
        range.setPrototype(getPrototype(range.getClass()));
        return range;
    }

    /**
     * Checks whether the given element is contained within this object.
     * @param element element object that specifies the element to check
     * @return true if the element is contained within this object
     */
    @JsxFunction({ @WebBrowser(IE), @WebBrowser(value = FF, minVersion = 10), @WebBrowser(CHROME) })
    public boolean contains(final HTMLElement element) {
        for (HTMLElement parent = element; parent != null; parent = (HTMLElement) parent.getParentElement()) {
            if (this == parent) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the focus to this element.
     */
    @JsxFunction
    public void focus() {
        getDomNodeOrDie().focus();
    }

    /**
     * Sets the object as active without setting focus to the object.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536738.aspx">MSDN documentation</a>
     */
    @JsxFunction(@WebBrowser(IE))
    public void setActive() {
        final Window window = getWindow();
        final HTMLDocument document = (HTMLDocument) window.getDocument();
        document.setActiveElement(this);
        if (window.getWebWindow() == window.getWebWindow().getWebClient().getCurrentWindow()) {
            final HtmlElement element = getDomNodeOrDie();
            ((HtmlPage) element.getPage()).setFocusedElement(element);
        }
    }

    /**
     * Retrieves all element nodes from descendants of the starting element node that match any selector
     * within the supplied selector strings.
     * The NodeList object returned by the querySelectorAll() method must be static, not live.
     * @param selectors the selectors
     * @return the static node list
     */
    @JsxFunction({ @WebBrowser(value = IE, minVersion = 8), @WebBrowser(FF) })
    public StaticNodeList querySelectorAll(final String selectors) {
        try {
            final List<Node> nodes = new ArrayList<Node>();
            for (final DomNode domNode : getDomNodeOrDie().querySelectorAll(selectors)) {
                nodes.add((Node) domNode.getScriptObject());
            }
            return new StaticNodeList(nodes, this);
        }
        catch (final CSSException e) {
            throw Context.reportRuntimeError("An invalid or illegal selector was specified (selector: '"
                    + selectors + "' error: " + e.getMessage() + ").");
        }
    }

    /**
     * Returns the first element within the document that matches the specified group of selectors.
     * @param selectors the selectors
     * @return null if no matches are found; otherwise, it returns the first matching element
     */
    @JsxFunction({ @WebBrowser(value = IE, minVersion = 8), @WebBrowser(FF) })
    public Node querySelector(final String selectors) {
        try {
            final DomNode node = getDomNodeOrDie().querySelector(selectors);
            if (node != null) {
                return (Node) node.getScriptObject();
            }
            return null;
        }
        catch (final CSSException e) {
            throw Context.reportRuntimeError("An invalid or illegal selector was specified (selector: '"
                    + selectors + "' error: " + e.getMessage() + ").");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        final Object response = super.get(name, start);

        // IE8 support .querySelector(All) but not in quirks mode
        // => TODO: find a better way to handle this!
        if (response instanceof FunctionObject
            && ("querySelectorAll".equals(name) || "querySelector".equals(name))
            && getBrowserVersion().hasFeature(QUERYSELECTORALL_NOT_IN_QUIRKS)) {

            final DomNode domNode = getDomNodeOrNull();
            if (null == domNode) {
                return response;
            }
            final SgmlPage sgmlPage = domNode.getPage();
            if ((sgmlPage instanceof HtmlPage) && ((HtmlPage) sgmlPage).isQuirksMode()) {
                return NOT_FOUND;
            }
        }

        return response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNodeName() {
        final DomNode domNode = getDomNodeOrDie();
        String nodeName = domNode.getNodeName();
        if (domNode.getPage() instanceof HtmlPage) {
            nodeName = nodeName.toUpperCase();
        }
        return nodeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrefix() {
        if (getBrowserVersion().hasFeature(GENERATED_73)) {
            return "";
        }
        return null;
    }

    /**
     * Gets the filters.
     * @return the filters
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms537452.aspx">MSDN doc</a>
     */
    @JsxGetter(@WebBrowser(IE))
    public Object getFilters() {
        return this; // return anything, what matters is that it is not null
    }

    /**
     * Click this element. This simulates the action of the user clicking with the mouse.
     * @throws IOException if this click triggers a page load that encounters problems
     */
    @JsxFunction(@WebBrowser(IE))
    public void click() throws IOException {
        getDomNodeOrDie().click();
    }

    /**
     * Returns the "spellcheck" property.
     * @return the "spellcheck" property
     */
    @JsxGetter(@WebBrowser(FF))
    public boolean getSpellcheck() {
        return Context.toBoolean(getDomNodeOrDie().getAttribute("spellcheck"));
    }

    /**
     * Sets the "spellcheck" property.
     * @param spellcheck the "spellcheck" property
     */
    @JsxSetter(@WebBrowser(FF))
    public void setSpellcheck(final boolean spellcheck) {
        getDomNodeOrDie().setAttribute("spellcheck", Boolean.toString(spellcheck));
    }

    /**
     * Returns the "lang" property.
     * @return the "lang" property
     */
    @JsxGetter
    public String getLang() {
        return getDomNodeOrDie().getAttribute("lang");
    }

    /**
     * Sets the "lang" property.
     * @param lang the "lang" property
     */
    @JsxSetter
    public void setLang(final String lang) {
        getDomNodeOrDie().setAttribute("lang", lang);
    }

    /**
     * Returns the "language" property.
     * @return the "language" property
     */
    @JsxGetter(@WebBrowser(IE))
    public String getLanguage() {
        return getDomNodeOrDie().getAttribute("language");
    }

    /**
     * Sets the "language" property.
     * @param language the "language" property
     */
    @JsxSetter(@WebBrowser(IE))
    public void setLanguage(final String language) {
        getDomNodeOrDie().setAttribute("language", language);
    }

    /**
     * Returns the "dir" property.
     * @return the "dir" property
     */
    @JsxGetter
    public String getDir() {
        return getDomNodeOrDie().getAttribute("dir");
    }

    /**
     * Sets the "dir" property.
     * @param dir the "dir" property
     */
    @JsxSetter
    public void setDir(final String dir) {
        getDomNodeOrDie().setAttribute("dir", dir);
    }

    /**
     * Returns the value of the tabIndex attribute.
     * @return the value of the tabIndex attribute
     */
    @JsxGetter
    public int getTabIndex() {
        return (int) Context.toNumber(getDomNodeOrDie().getAttribute("tabindex"));
    }

    /**
     * Sets the "tabIndex" property.
     * @param tabIndex the "tabIndex" property
     */
    @JsxSetter
    public void setTabIndex(final int tabIndex) {
        getDomNodeOrDie().setAttribute("tabindex", Integer.toString(tabIndex));
    }

    /**
     * Simulates a click on a scrollbar component (IE only).
     * @param scrollAction the type of scroll action to simulate
     */
    @JsxFunction(@WebBrowser(IE))
    public void doScroll(final String scrollAction) {
        if (((HtmlPage) getDomNodeOrDie().getPage()).isBeingParsed()) {
            throw Context.reportRuntimeError("The data necessary to complete this operation is not yet available.");
        }
        // Ignore because we aren't displaying anything!
    }

    /**
     * Returns the "accessKey" property.
     * @return the "accessKey" property
     */
    @JsxGetter(@WebBrowser(IE))
    public String getAccessKey() {
        return getDomNodeOrDie().getAttribute("accesskey");
    }

    /**
     * Sets the "accessKey" property.
     * @param accessKey the "accessKey" property
     */
    @JsxSetter(@WebBrowser(IE))
    public void setAccessKey(final String accessKey) {
        getDomNodeOrDie().setAttribute("accesskey", accessKey);
    }

    /**
     * Returns the value of the specified attribute (width or height).
     * @return the value of the specified attribute (width or height)
     * @param attributeName the name of the attribute to return (<tt>"width"</tt> or <tt>"height"</tt>)
     * @param returnNegativeValues if <tt>true</tt>, negative values are returned;
     *        if <tt>false</tt>, this method returns an empty string in lieu of negative values;
     *        if <tt>null</tt>, this method returns <tt>0</tt> in lieu of negative values
     */
    protected String getWidthOrHeight(final String attributeName, final Boolean returnNegativeValues) {
        String s = getDomNodeOrDie().getAttribute(attributeName);
        if (!PERCENT_VALUE.matcher(s).matches()) {
            try {
                final Float f = Float.valueOf(s);
                final int i = f.intValue();
                if (i < 0) {
                    if (returnNegativeValues == null) {
                        s = "0";
                    }
                    else if (!returnNegativeValues.booleanValue()) {
                        s = "";
                    }
                    else {
                        s = Integer.toString(i);
                    }
                }
                else {
                    s = Integer.toString(i);
                }
            }
            catch (final NumberFormatException e) {
                if (getBrowserVersion().hasFeature(GENERATED_74)) {
                    s = "";
                }
            }
        }
        return s;
    }

    /**
     * Sets the value of the specified attribute (width or height).
     * @param attributeName the name of the attribute to set (<tt>"width"</tt> or <tt>"height"</tt>)
     * @param value the value of the specified attribute (width or height)
     * @param allowNegativeValues if <tt>true</tt>, negative values will be stored;
     *        if <tt>false</tt>, negative values cause an exception to be thrown;
     *        if <tt>null</tt>, negative values set the value to <tt>0</tt>
     */
    protected void setWidthOrHeight(final String attributeName, String value, final Boolean allowNegativeValues) {
        if (value.endsWith("px")) {
            value = value.substring(0, value.length() - 2);
        }
        if (getBrowserVersion().hasFeature(GENERATED_75) && value.length() > 0) {
            boolean error = false;
            if (!PERCENT_VALUE.matcher(value).matches()) {
                try {
                    final Float f = Float.valueOf(value);
                    final int i = f.intValue();
                    if (i < 0) {
                        if (allowNegativeValues == null) {
                            value = "0";
                        }
                        else if (!allowNegativeValues.booleanValue()) {
                            error = true;
                        }
                    }
                }
                catch (final NumberFormatException e) {
                    error = true;
                }
            }
            if (error) {
                final Exception e = new Exception("Cannot set the width property to invalid value: " + value);
                Context.throwAsScriptRuntimeEx(e);
            }
        }
        getDomNodeOrDie().setAttribute(attributeName, value);
    }

    /**
     * Sets the specified color attribute to the specified value.
     * @param name the color attribute's name
     * @param value the color attribute's value
     */
    protected void setColorAttribute(final String name, final String value) {
        String s = value;
        if (getBrowserVersion().hasFeature(HTML_COLOR_RESTRICT) && !s.isEmpty()) {
            //For IE9, check HTMLElementTest#setColorAttribute

            s = null;
            for (final String key : COLORS_MAP_IE.keySet()) {
                if (key.equalsIgnoreCase(value)) {
                    s = COLORS_MAP_IE.get(key).toLowerCase();
                    break;
                }
            }
            if (s == null) {
                s = value.toLowerCase();
                if (s.charAt(0) == '#') {
                    s = s.substring(1);
                }
                final StringBuilder builder = new StringBuilder(7);
                for (int x = 0; x < 6 && x < s.length(); x++) {
                    final char ch = s.charAt(x);
                    if ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f')) {
                        builder.append(ch);
                    }
                    else {
                        builder.append('0');
                    }
                }
                while (builder.length() < 6) {
                    builder.append('0');
                }
                builder.insert(0, '#');
                s = builder.toString();
            }
        }
        getDomNodeOrDie().setAttribute(name, s);
    }

    /**
     * Returns the value of the "align" property.
     * @param returnInvalidValues if <tt>true</tt>, this method will return any value, including technically
     *        invalid values; if <tt>false</tt>, this method will return an empty string instead of invalid values
     * @return the value of the "align" property
     */
    protected String getAlign(final boolean returnInvalidValues) {
        final boolean acceptArbitraryValues = getBrowserVersion().hasFeature(JS_ALIGN_ACCEPTS_ARBITRARY_VALUES);

        final String align = getDomNodeOrDie().getAttribute("align");
        if (returnInvalidValues || acceptArbitraryValues
            || "center".equals(align)
            || "justify".equals(align)
            || "left".equals(align)
            || "right".equals(align)) {
            return align;
        }
        return "";
    }

    /**
     * Sets the value of the "align" property.
     * @param align the value of the "align" property
     * @param ignoreIfNoError if <tt>true</tt>, the invocation will be a no-op if it does not trigger an error
     *        (i.e., it will not actually set the align attribute)
     */
    protected void setAlign(String align, final boolean ignoreIfNoError) {
        align = align.toLowerCase();
        final boolean acceptArbitraryValues = getBrowserVersion().hasFeature(JS_ALIGN_ACCEPTS_ARBITRARY_VALUES);
        if (acceptArbitraryValues
                || "center".equals(align)
                || "justify".equals(align)
                || "left".equals(align)
                || "right".equals(align)) {
            if (!ignoreIfNoError) {
                getDomNodeOrDie().setAttribute("align", align);
            }
            return;
        }

        throw Context.reportRuntimeError("Cannot set the align property to invalid value: '" + align + "'");
    }

    /**
     * Returns the value of the "vAlign" property.
     * @param valid the valid values; if <tt>null</tt>, any value is valid
     * @param defaultValue the default value to use, if necessary
     * @return the value of the "vAlign" property
     */
    protected String getVAlign(final String[] valid, final String defaultValue) {
        final String valign = getDomNodeOrDie().getAttribute("valign");
        if (valid == null || ArrayUtils.contains(valid, valign)) {
            return valign;
        }
        return defaultValue;
    }

    /**
     * Sets the value of the "vAlign" property.
     * @param vAlign the value of the "vAlign" property
     * @param valid the valid values; if <tt>null</tt>, any value is valid
     */
    protected void setVAlign(final Object vAlign, final String[] valid) {
        final String s = Context.toString(vAlign).toLowerCase();
        if (valid == null || ArrayUtils.contains(valid, s)) {
            getDomNodeOrDie().setAttribute("valign", s);
        }
        else {
            throw Context.reportRuntimeError("Cannot set the vAlign property to invalid value: " + vAlign);
        }
    }

    /**
     * Returns the value of the "ch" property.
     * @return the value of the "ch" property
     */
    protected String getCh() {
        final boolean emulated = getBrowserVersion().hasFeature(JS_CHAR_EMULATED);
        if (emulated) {
            return ch_;
        }

        final String ch = getDomNodeOrDie().getAttribute("char");
        if ((ch == DomElement.ATTRIBUTE_NOT_DEFINED) && (getBrowserVersion().hasFeature(JS_CHAR_UNDEFINED_DOT))) {
            return ".";
        }
        return ch;
    }

    /**
     * Sets the value of the "ch" property.
     * @param ch the value of the "ch" property
     */
    protected void setCh(final String ch) {
        final boolean emulated = getBrowserVersion().hasFeature(JS_CHAR_EMULATED);
        if (emulated) {
            ch_ = ch;
        }
        else {
            getDomNodeOrDie().setAttribute("char", ch);
        }
    }

    /**
     * Returns the value of the "chOff" property.
     * @return the value of the "chOff" property
     */
    protected String getChOff() {
        final boolean emulated = getBrowserVersion().hasFeature(JS_CHAR_OFF_EMULATED);
        if (emulated) {
            return chOff_;
        }
        return getDomNodeOrDie().getAttribute("charOff");
    }

    /**
     * Sets the value of the "chOff" property.
     * @param chOff the value of the "chOff" property
     */
    protected void setChOff(String chOff) {
        final boolean emulated = getBrowserVersion().hasFeature(JS_CHAR_OFF_EMULATED);
        if (emulated) {
            chOff_ = chOff;
            return;
        }

        try {
            final float f = Float.parseFloat(chOff);
            if (getBrowserVersion().hasFeature(JS_CHAR_OFF_INTEGER)) {
                if (f < 0) {
                    chOff = "0";
                }
                else {
                    chOff = Integer.toString((int) f);
                }
            }
            else {
                final int i = (int) f;
                if (i == f) {
                    chOff = Integer.toString(i);
                }
                else {
                    chOff = Float.toString(f);
                }
            }
        }
        catch (final NumberFormatException e) {
            // Ignore.
        }
        getDomNodeOrDie().setAttribute("charOff", chOff);
    }

    /**
     * Returns the current (calculated) style object for this element.
     * @return the current (calculated) style object for this element
     */
    @JsxGetter(@WebBrowser(IE))
    public ComputedCSSStyleDeclaration getCurrentStyle() {
        return getWindow().getComputedStyle(this, null);
    }

    /**
     * Returns this element's <tt>offsetLeft</tt>, which is the calculated left position of this
     * element relative to the <tt>offsetParent</tt>.
     *
     * @return this element's <tt>offsetLeft</tt>
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms534200.aspx">MSDN Documentation</a>
     * @see <a href="http://www.quirksmode.org/js/elementdimensions.html">Element Dimensions</a>
     * @see <a href="http://dump.testsuite.org/2006/dom/style/offset/spec">Reverse Engineering by Anne van Kesteren</a>
     */
    @JsxGetter
    public int getOffsetLeft() {
        if (this instanceof HTMLBodyElement) {
            return 0;
        }

        int left = 0;
        final HTMLElement offsetParent = getOffsetParent();

        // Add the offset for this node.
        DomNode node = getDomNodeOrDie();
        HTMLElement element = (HTMLElement) node.getScriptObject();
        left += element.getCurrentStyle().getLeft(true, false, false);

        // If this node is absolutely positioned, we're done.
        final String position = element.getCurrentStyle().getPositionWithInheritance();
        if ("absolute".equals(position)) {
            return left;
        }

        // Add the offset for the ancestor nodes.
        node = node.getParentNode();
        while (node != null && node.getScriptObject() != offsetParent) {
            if (node.getScriptObject() instanceof HTMLElement) {
                element = (HTMLElement) node.getScriptObject();
                left += element.getCurrentStyle().getLeft(true, true, true);
            }
            node = node.getParentNode();
        }

        if (offsetParent != null) {
            left += offsetParent.getCurrentStyle().getMarginLeftValue();
            left += offsetParent.getCurrentStyle().getPaddingLeftValue();
        }

        return left;
    }

    /**
     * Returns this element's X position.
     * @return this element's X position
     */
    public int getPosX() {
        int cumulativeOffset = 0;
        HTMLElement element = this;
        while (element != null) {
            cumulativeOffset += element.getOffsetLeft();
            if (element != this) {
                cumulativeOffset += element.getCurrentStyle().getBorderLeftValue();
            }
            element = element.getOffsetParent();
        }
        return cumulativeOffset;
    }

    /**
     * Returns this element's Y position.
     * @return this element's Y position
     */
    public int getPosY() {
        int cumulativeOffset = 0;
        HTMLElement element = this;
        while (element != null) {
            cumulativeOffset += element.getOffsetTop();
            if (element != this) {
                cumulativeOffset += element.getCurrentStyle().getBorderTopValue();
            }
            element = element.getOffsetParent();
        }
        return cumulativeOffset;
    }

    /**
     * Gets the offset parent or <code>null</code> if this is not an {@link HTMLElement}.
     * @return the offset parent or <code>null</code>
     */
    private HTMLElement getOffsetParent() {
        final Object offsetParent = getOffsetParent_js();
        if (offsetParent instanceof HTMLElement) {
            return (HTMLElement) offsetParent;
        }
        return null;
    }

    /**
     * Returns "clientLeft" attribute.
     * @return the "clientLeft" attribute
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public int getClientLeft() {
        if (getBrowserVersion().hasFeature(JS_CLIENT_LEFT_TOP_ZERO)) {
            return 0;
        }
        return getCurrentStyle().getBorderLeftValue();
    }

    /**
     * Returns "clientTop" attribute.
     * @return the "clientTop" attribute
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(FF) })
    public int getClientTop() {
        if (getBrowserVersion().hasFeature(JS_CLIENT_LEFT_TOP_ZERO)) {
            return 0;
        }
        return getCurrentStyle().getBorderTopValue();
    }

    /**
     * Returns this element's <tt>offsetTop</tt>, which is the calculated top position of this
     * element relative to the <tt>offsetParent</tt>.
     *
     * @return this element's <tt>offsetTop</tt>
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms534303.aspx">MSDN Documentation</a>
     * @see <a href="http://www.quirksmode.org/js/elementdimensions.html">Element Dimensions</a>
     * @see <a href="http://dump.testsuite.org/2006/dom/style/offset/spec">Reverse Engineering by Anne van Kesteren</a>
     */
    @JsxGetter
    public int getOffsetTop() {
        if (this instanceof HTMLBodyElement) {
            return 0;
        }

        int top = 0;
        final HTMLElement offsetParent = getOffsetParent();

        // Add the offset for this node.
        DomNode node = getDomNodeOrDie();
        HTMLElement element = (HTMLElement) node.getScriptObject();
        top += element.getCurrentStyle().getTop(true, false, false);

        // If this node is absolutely positioned, we're done.
        final String position = element.getCurrentStyle().getPositionWithInheritance();
        if ("absolute".equals(position)) {
            return top;
        }

        // Add the offset for the ancestor nodes.
        node = node.getParentNode();
        while (node != null && node.getScriptObject() != offsetParent) {
            if (node.getScriptObject() instanceof HTMLElement) {
                element = (HTMLElement) node.getScriptObject();
                top += element.getCurrentStyle().getTop(false, true, true);
            }
            node = node.getParentNode();
        }

        if (offsetParent != null) {
            final HTMLElement thiz = (HTMLElement) getDomNodeOrDie().getScriptObject();
            final boolean thisElementHasTopMargin = (thiz.getCurrentStyle().getMarginTopValue() != 0);
            if (!thisElementHasTopMargin) {
                top += offsetParent.getCurrentStyle().getMarginTopValue();
            }
            top += offsetParent.getCurrentStyle().getPaddingTopValue();
        }

        return top;
    }

    /**
     * Returns this element's <tt>offsetParent</tt>. The <tt>offsetLeft</tt> and
     * <tt>offsetTop</tt> attributes are relative to the <tt>offsetParent</tt>.
     *
     * @return this element's <tt>offsetParent</tt>. This may be <code>undefined</code> when this node is
     * not attached or <code>null</code> for <code>body</code>.
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms534302.aspx">MSDN Documentation</a>
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_el_ref20.html">Gecko DOM Reference</a>
     * @see <a href="http://www.quirksmode.org/js/elementdimensions.html">Element Dimensions</a>
     * @see <a href="http://www.w3.org/TR/REC-CSS2/box.html">Box Model</a>
     * @see <a href="http://dump.testsuite.org/2006/dom/style/offset/spec">Reverse Engineering by Anne van Kesteren</a>
     */
    @JsxGetter(propertyName = "offsetParent")
    public Object getOffsetParent_js() {
        DomNode currentElement = getDomNodeOrDie();

        if (currentElement.getParentNode() == null) {
            if (getBrowserVersion().hasFeature(JS_OFFSET_PARENT_THROWS_NOT_ATTACHED)) {
                throw Context.reportRuntimeError("Unspecified error");
            }
            return null;
        }

        Object offsetParent = null;
        final HTMLElement htmlElement = (HTMLElement) currentElement.getScriptObject();
        final ComputedCSSStyleDeclaration style = htmlElement.getCurrentStyle();
        final String position = style.getPositionWithInheritance();
        final boolean ie = getBrowserVersion().hasFeature(GENERATED_72);
        final boolean staticPos = "static".equals(position);
        final boolean fixedPos = "fixed".equals(position);
        final boolean useTables = ((ie && (staticPos || fixedPos)) || (!ie && staticPos));

        while (currentElement != null) {

            final DomNode parentNode = currentElement.getParentNode();
            if (parentNode instanceof HtmlBody
                || (useTables && parentNode instanceof HtmlTableDataCell)
                || (useTables && parentNode instanceof HtmlTable)) {
                offsetParent = parentNode.getScriptObject();
                break;
            }

            if (parentNode != null && parentNode.getScriptObject() instanceof HTMLElement) {
                final HTMLElement parentElement = (HTMLElement) parentNode.getScriptObject();
                final ComputedCSSStyleDeclaration parentStyle = parentElement.getCurrentStyle();
                final String parentPosition = parentStyle.getPositionWithInheritance();
                final boolean parentIsStatic = "static".equals(parentPosition);
                final boolean parentIsFixed = "fixed".equals(parentPosition);
                if ((ie && !parentIsStatic && !parentIsFixed) || (!ie && !parentIsStatic)) {
                    offsetParent = parentNode.getScriptObject();
                    break;
                }
            }

            currentElement = currentElement.getParentNode();
        }

        return offsetParent;
    }

    /**
     * Retrieves an object that specifies the bounds of a collection of TextRectangle objects.
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536433.aspx">MSDN doc</a>
     * @return an object that specifies the bounds of a collection of TextRectangle objects
     */
    @Override
    public ClientRect getBoundingClientRect() {
        int left = getPosX();
        int top = getPosY();

        // account for any scrolled ancestors
        Object parentNode = getOffsetParent_js();
        while (parentNode != null
                && (parentNode instanceof HTMLElement)
                && !(parentNode instanceof HTMLBodyElement)) {
            final HTMLElement elem = (HTMLElement) parentNode;
            left -= elem.getScrollLeft();
            top -= elem.getScrollTop();

            parentNode = elem.getParentNode();
        }

        if (getBrowserVersion().hasFeature(JS_BOUNDING_CLIENT_RECT_OFFSET_TWO)) {
            left += 2;
            top += 2;
        }

        final ClientRect textRectangle = new ClientRect(0, left, 0, top);
        textRectangle.setParentScope(getWindow());
        textRectangle.setPrototype(getPrototype(textRectangle.getClass()));
        return textRectangle;
    }

    /**
     * Returns the style object for this element.
     * @return the style object for this element
     */
    @JsxGetter
    public CSSStyleDeclaration getStyle() {
        return style_;
    }

    /**
     * Returns the runtime style object for this element.
     * @return the runtime style object for this element
     */
    @JsxGetter(@WebBrowser(IE))
    public CSSStyleDeclaration getRuntimeStyle() {
        return style_;
    }

    /**
     * Gets the token list of class attribute.
     * @return the token list of class attribute
     */
    @Override
    public DOMTokenList getClassList() {
        return new DOMTokenList(this, "class");
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public boolean hasAttribute(final String name) {
        return super.hasAttribute(name);
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxFunction
    public Object getAttribute(final String attributeName, final Integer flags) {
        return super.getAttribute(attributeName, flags);
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxGetter
    public final String getTagName() {
        return super.getTagName();
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxGetter(@WebBrowser(IE))
    public HTMLCollection getChildren() {
        return super.getChildren();
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(value = FF, minVersion = 10), @WebBrowser(CHROME) })
    public Element getParentElement() {
        return super.getParentElement();
    }
}
