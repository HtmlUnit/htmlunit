/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTML_COLOR_RESTRICT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTML_COLOR_TO_LOWER;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ALIGN_ACCEPTS_ARBITRARY_VALUES;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_BOUNDINGCLIENTRECT_THROWS_IF_DISCONNECTED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INNER_HTML_ADD_CHILD_FOR_NULL_VALUE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INNER_TEXT_CR_NL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INNER_TEXT_VALUE_NULL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_OFFSET_PARENT_NULL_IF_FIXED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_OUTER_HTML_NULL_AS_STRING;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_OUTER_HTML_REMOVES_CHILDS_FOR_DETACHED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_OUTER_HTML_THROWS_FOR_DETACHED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WIDTH_HEIGHT_ACCEPTS_ARBITRARY_VALUES;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomCharacterData;
import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlAbbreviated;
import com.gargoylesoftware.htmlunit.html.HtmlAcronym;
import com.gargoylesoftware.htmlunit.html.HtmlAddress;
import com.gargoylesoftware.htmlunit.html.HtmlArticle;
import com.gargoylesoftware.htmlunit.html.HtmlAside;
import com.gargoylesoftware.htmlunit.html.HtmlBaseFont;
import com.gargoylesoftware.htmlunit.html.HtmlBidirectionalIsolation;
import com.gargoylesoftware.htmlunit.html.HtmlBidirectionalOverride;
import com.gargoylesoftware.htmlunit.html.HtmlBig;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlBold;
import com.gargoylesoftware.htmlunit.html.HtmlCenter;
import com.gargoylesoftware.htmlunit.html.HtmlCitation;
import com.gargoylesoftware.htmlunit.html.HtmlCode;
import com.gargoylesoftware.htmlunit.html.HtmlCommand;
import com.gargoylesoftware.htmlunit.html.HtmlDefinition;
import com.gargoylesoftware.htmlunit.html.HtmlDefinitionDescription;
import com.gargoylesoftware.htmlunit.html.HtmlDefinitionTerm;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement.DisplayStyle;
import com.gargoylesoftware.htmlunit.html.HtmlEmphasis;
import com.gargoylesoftware.htmlunit.html.HtmlExample;
import com.gargoylesoftware.htmlunit.html.HtmlFigure;
import com.gargoylesoftware.htmlunit.html.HtmlFigureCaption;
import com.gargoylesoftware.htmlunit.html.HtmlFooter;
import com.gargoylesoftware.htmlunit.html.HtmlHeader;
import com.gargoylesoftware.htmlunit.html.HtmlItalic;
import com.gargoylesoftware.htmlunit.html.HtmlKeyboard;
import com.gargoylesoftware.htmlunit.html.HtmlLayer;
import com.gargoylesoftware.htmlunit.html.HtmlListing;
import com.gargoylesoftware.htmlunit.html.HtmlMain;
import com.gargoylesoftware.htmlunit.html.HtmlMark;
import com.gargoylesoftware.htmlunit.html.HtmlNav;
import com.gargoylesoftware.htmlunit.html.HtmlNoBreak;
import com.gargoylesoftware.htmlunit.html.HtmlNoEmbed;
import com.gargoylesoftware.htmlunit.html.HtmlNoFrames;
import com.gargoylesoftware.htmlunit.html.HtmlNoLayer;
import com.gargoylesoftware.htmlunit.html.HtmlNoScript;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPlainText;
import com.gargoylesoftware.htmlunit.html.HtmlRp;
import com.gargoylesoftware.htmlunit.html.HtmlRt;
import com.gargoylesoftware.htmlunit.html.HtmlRuby;
import com.gargoylesoftware.htmlunit.html.HtmlS;
import com.gargoylesoftware.htmlunit.html.HtmlSample;
import com.gargoylesoftware.htmlunit.html.HtmlSection;
import com.gargoylesoftware.htmlunit.html.HtmlSmall;
import com.gargoylesoftware.htmlunit.html.HtmlStrike;
import com.gargoylesoftware.htmlunit.html.HtmlStrong;
import com.gargoylesoftware.htmlunit.html.HtmlSubscript;
import com.gargoylesoftware.htmlunit.html.HtmlSummary;
import com.gargoylesoftware.htmlunit.html.HtmlSuperscript;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableDataCell;
import com.gargoylesoftware.htmlunit.html.HtmlTeletype;
import com.gargoylesoftware.htmlunit.html.HtmlUnderlined;
import com.gargoylesoftware.htmlunit.html.HtmlVariable;
import com.gargoylesoftware.htmlunit.html.HtmlWordBreak;
import com.gargoylesoftware.htmlunit.html.SubmittableElement;
import com.gargoylesoftware.htmlunit.javascript.NamedNodeMap;
import com.gargoylesoftware.htmlunit.javascript.ScriptableWithFallbackGetter;
import com.gargoylesoftware.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJob;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.ClientRect;
import com.gargoylesoftware.htmlunit.javascript.host.ClientRectList;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Attr;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMStringMap;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMTokenList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node;
import com.gargoylesoftware.htmlunit.javascript.host.dom.NodeList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.TextRange;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventHandler;
import com.gargoylesoftware.htmlunit.javascript.host.event.MouseEvent;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;

/**
 * The JavaScript object {@code HTMLElement} which is the base class for all HTML
 * objects. This will typically wrap an instance of {@link HtmlElement}.
 *
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
 * @author Frank Danek
 */
@JsxClasses({
        @JsxClass(domClass = HtmlAbbreviated.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlAcronym.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlAddress.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlArticle.class,
            browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(IE) }),
        @JsxClass(domClass = HtmlAside.class,
            browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(IE) }),
        @JsxClass(domClass = HtmlBaseFont.class,
            browsers = { @WebBrowser(CHROME), @WebBrowser(value = FF, minVersion = 38) }),
        @JsxClass(domClass = HtmlBidirectionalIsolation.class, browsers = @WebBrowser(CHROME)),
        @JsxClass(domClass = HtmlBidirectionalOverride.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlBig.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlBold.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlCenter.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) }),
        @JsxClass(domClass = HtmlCitation.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlCode.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlCommand.class, browsers = @WebBrowser(CHROME)),
        @JsxClass(domClass = HtmlDefinition.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlDefinitionDescription.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlDefinitionTerm.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlElement.class,
            browsers = { @WebBrowser(FF), @WebBrowser(IE) }),
        @JsxClass(domClass = HtmlEmphasis.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlExample.class, browsers = @WebBrowser(FF)),
        @JsxClass(domClass = HtmlFigure.class,
            browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(IE) }),
        @JsxClass(domClass = HtmlFigureCaption.class,
            browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(IE) }),
        @JsxClass(domClass = HtmlFooter.class,
            browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(IE) }),
        @JsxClass(domClass = HtmlHeader.class,
            browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(IE) }),
        @JsxClass(domClass = HtmlItalic.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlKeyboard.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlLayer.class, browsers = @WebBrowser(CHROME)),
        @JsxClass(domClass = HtmlListing.class, browsers = @WebBrowser(FF)),
        @JsxClass(domClass = HtmlMark.class,
            browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(IE) }),
        @JsxClass(domClass = HtmlNav.class,
            browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(IE) }),
        @JsxClass(domClass = HtmlNoBreak.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlNoEmbed.class,
            browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(IE) }),
        @JsxClass(domClass = HtmlNoFrames.class,
            browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(IE) }),
        @JsxClass(domClass = HtmlNoLayer.class, browsers = @WebBrowser(CHROME)),
        @JsxClass(domClass = HtmlNoScript.class,
            browsers = { @WebBrowser(FF), @WebBrowser(IE), @WebBrowser(CHROME) }),
        @JsxClass(domClass = HtmlPlainText.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlRuby.class, browsers = @WebBrowser(CHROME)),
        @JsxClass(domClass = HtmlRp.class, browsers = @WebBrowser(CHROME)),
        @JsxClass(domClass = HtmlRt.class, browsers = @WebBrowser(CHROME)),
        @JsxClass(domClass = HtmlS.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlSample.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlSection.class,
            browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(IE) }),
        @JsxClass(domClass = HtmlSmall.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlStrike.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlStrong.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlSubscript.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlSummary.class, browsers = @WebBrowser(CHROME)),
        @JsxClass(domClass = HtmlSuperscript.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlTeletype.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlUnderlined.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlWordBreak.class,
            browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(IE) }),
        @JsxClass(domClass = HtmlMain.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) }),
        @JsxClass(domClass = HtmlVariable.class, browsers = { @WebBrowser(CHROME), @WebBrowser(FF) })
    })
public class HTMLElement extends Element implements ScriptableWithFallbackGetter {

    private static final Class<?>[] METHOD_PARAMS_OBJECT = new Class[] {Object.class};
    private static final Pattern PERCENT_VALUE = Pattern.compile("\\d+%");
    /* http://msdn.microsoft.com/en-us/library/ie/aa358802.aspx */
    private static final Map<String, String> COLORS_MAP_IE = new HashMap<>();

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

    static final String POSITION_BEFORE_BEGIN = "beforebegin";
    static final String POSITION_AFTER_BEGIN = "afterbegin";
    static final String POSITION_BEFORE_END = "beforeend";
    static final String POSITION_AFTER_END = "afterend";

    /**
     * Static counter for {@link #uniqueID_}.
     */
    private static int UniqueID_Counter_ = 1;

    private final Set<String> behaviors_ = new HashSet<>();
    private int scrollLeft_;
    private int scrollTop_;
    private String uniqueID_;

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

    private boolean endTagForbidden_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public HTMLElement() {
    }

    /**
     * Sets the DOM node that corresponds to this JavaScript object.
     * @param domNode the DOM node
     */
    @Override
    public void setDomNode(final DomNode domNode) {
        super.setDomNode(domNode);

        if ("wbr".equalsIgnoreCase(domNode.getLocalName())) {
            endTagForbidden_ = true;
        }
        else if ("basefont".equalsIgnoreCase(domNode.getLocalName())) {
            endTagForbidden_ = true;
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
    public String getLocalName() {
        final DomNode domNode = getDomNodeOrDie();
        if (domNode.getHtmlPageOrNull() != null) {
            final String prefix = domNode.getPrefix();
            if (prefix != null) {
                // create string builder only if needed (performance)
                final StringBuilder localName = new StringBuilder(prefix.toLowerCase(Locale.ROOT));
                localName.append(':');
                localName.append(domNode.getLocalName().toLowerCase(Locale.ROOT));
                return localName.toString();
            }
            return domNode.getLocalName().toLowerCase(Locale.ROOT);
        }
        return domNode.getLocalName();
    }

    /**
    * Looks at attributes with the given name.
    * {@inheritDoc}
    */
    @Override
    public Object getWithFallback(final String name) {
        return NOT_FOUND;
    }

    /**
     * An IE-only method which clears all custom attributes.
     */
    @JsxFunction(@WebBrowser(IE))
    public void clearAttributes() {
        final HtmlElement node = getDomNodeOrDie();

        // Remove custom attributes defined directly in HTML.
        final List<String> removals = new ArrayList<>();
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
     * @param preserveIdentity if {@code false}, the <tt>name</tt> and <tt>id</tt> attributes are not copied
     */
    @JsxFunction(@WebBrowser(IE))
    public void mergeAttributes(final HTMLElement source, final Object preserveIdentity) {
        final HtmlElement src = source.getDomNodeOrDie();
        final HtmlElement target = getDomNodeOrDie();

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
     * @return the specified attribute, {@code null} if the attribute is not defined
     */
    @JsxFunction
    public Object getAttributeNodeNS(final String namespaceURI, final String localName) {
        return getDomNodeOrDie().getAttributeNodeNS(namespaceURI, localName).getScriptableObject();
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
    public void setAttribute(String name, final String value) {
        getDomNodeOrDie().setAttribute(name, value);

        // call corresponding event handler setOnxxx if found
        if (!name.isEmpty()) {
            name = name.toLowerCase(Locale.ROOT);
            if (name.startsWith("on")) {
                try {
                    name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                    final Method method = getClass().getMethod("set" + name, METHOD_PARAMS_OBJECT);
                    method.invoke(this, new Object[] {new EventHandler(getDomNodeOrDie(), name.substring(2), value)});
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
    }

    /**
     * Removes the specified attribute.
     * @param attribute the attribute to remove
     */
    @JsxFunction
    public void removeAttributeNode(final Attr attribute) {
        final String name = attribute.getName();
        final Object namespaceUri = attribute.getNamespaceURI();
        if (namespaceUri instanceof String) {
            removeAttributeNS((String) namespaceUri, name);
            return;
        }
        removeAttributeNS(null, name);
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
     * Returns all the descendant elements with the specified class.
     * @param className the name to search for
     * @return all the descendant elements with the specified class name
     */
    @JsxFunction
    public HTMLCollection getElementsByClassName(final String className) {
        final HtmlElement elt = getDomNodeOrDie();
        final String[] classNames = CLASS_NAMES_SPLIT_PATTERN.split(className, 0);

        final HTMLCollection collection = new HTMLCollection(elt, true) {
            @Override
            protected boolean isMatching(final DomNode node) {
                if (!(node instanceof HtmlElement)) {
                    return false;
                }
                String classAttribute = ((HtmlElement) node).getAttribute("class");
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
     * Returns the {@code clientHeight} attribute.
     * @return the {@code clientHeight} attribute
     */
    @JsxGetter
    public int getClientHeight() {
        final ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
        return style.getCalculatedHeight(false, true);
    }

    /**
     * Returns the {@code clientWidth} attribute.
     * @return the {@code clientWidth} attribute
     */
    @JsxGetter
    public int getClientWidth() {
        final ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
        return style.getCalculatedWidth(false, true);
    }

    /**
     * Sets the class attribute for this element.
     * @param className the new class name
     */
    @JsxSetter(propertyName = "className")
    public void setClassName_js(final String className) {
        getDomNodeOrDie().setAttribute("class", className);
    }

    /**
     * Gets the innerHTML attribute.
     * @return the contents of this node as HTML
     */
    @JsxGetter
    public String getInnerHTML() {
        final DomNode domNode;
        try {
            domNode = getDomNodeOrDie();
        }
        catch (final IllegalStateException e) {
            Context.throwAsScriptRuntimeEx(e);
            return "";
        }

        final StringBuilder buf = new StringBuilder();

        final String tagName = getTagName();
        boolean isPlain = "SCRIPT".equals(tagName);

        isPlain = isPlain || "STYLE".equals(tagName);

        // we can't rely on DomNode.asXml because it adds indentation and new lines
        printChildren(buf, domNode, !isPlain);
        return buf.toString();
    }

    /**
     * Gets the innerText attribute.
     * @return the contents of this node as text
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(CHROME), @WebBrowser(value = FF, minVersion = 45)})
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
    @JsxGetter
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
            if (html) {
                s = com.gargoylesoftware.htmlunit.util.StringUtils.escapeXmlChars(s);
            }
            buffer.append(s);
        }
        else if (html) {
            final DomElement element = (DomElement) node;
            final Element scriptObject = (Element) node.getScriptableObject();
            final String tag = element.getTagName();

            HTMLElement htmlElement = null;
            if (scriptObject instanceof HTMLElement) {
                htmlElement = (HTMLElement) scriptObject;
            }
            buffer.append("<").append(tag);
            // Add the attributes. IE does not use quotes, FF does.
            for (final DomAttr attr : element.getAttributesMap().values()) {
                if (!attr.getSpecified()) {
                    continue;
                }

                final String name = attr.getName();
                final String value = PRINT_NODE_QUOTE_PATTERN.matcher(attr.getValue()).replaceAll("&quot;");
                buffer.append(' ').append(name).append("=");
                buffer.append("\"");
                buffer.append(value);
                buffer.append("\"");
            }
            buffer.append(">");
            // Add the children.
            final boolean isHtml = html
                    && !(scriptObject instanceof HTMLScriptElement)
                    && !(scriptObject instanceof HTMLStyleElement);
            printChildren(buffer, node, isHtml);
            if (null == htmlElement || !htmlElement.isEndTagForbidden()) {
                buffer.append("</").append(tag).append(">");
            }
        }
        else {
            final HtmlElement element = (HtmlElement) node;
            if ("p".equals(element.getTagName())) {
                if (getBrowserVersion().hasFeature(JS_INNER_TEXT_CR_NL)) {
                    buffer.append("\r\n"); // \r\n because it's to implement something IE specific
                }
                else {
                    int i = buffer.length() - 1;
                    while (i >= 0 && Character.isWhitespace(buffer.charAt(i))) {
                        i--;
                    }
                    buffer.setLength(i + 1);
                    buffer.append("\n");
                }
            }
            if (!"script".equals(element.getTagName())) {
                printChildren(buffer, node, html);
            }
        }
    }

    /**
     * Replaces all child elements of this element with the supplied value.
     * @param value the new value for the contents of this element
     */
    @JsxSetter
    public void setInnerHTML(final Object value) {
        final DomNode domNode;
        try {
            domNode = getDomNodeOrDie();
        }
        catch (final IllegalStateException e) {
            Context.throwAsScriptRuntimeEx(e);
            return;
        }

        domNode.removeAllChildren();

        final boolean addChildForNull = getBrowserVersion().hasFeature(JS_INNER_HTML_ADD_CHILD_FOR_NULL_VALUE);
        if ((value == null && addChildForNull) || (value != null && !"".equals(value))) {

            final String valueAsString = Context.toString(value);
            parseHtmlSnippet(domNode, valueAsString);
        }
    }

    /**
     * Replaces all child elements of this element with the supplied text value.
     * @param value the new value for the contents of this element
     */
    @JsxSetter({ @WebBrowser(IE), @WebBrowser(CHROME), @WebBrowser(value = FF, minVersion = 45) })
    public void setInnerText(final Object value) {
        final String valueString;
        if (value == null && getBrowserVersion().hasFeature(JS_INNER_TEXT_VALUE_NULL)) {
            valueString = null;
        }
        else {
            valueString = Context.toString(value);
        }
        setInnerTextImpl(valueString);
    }

    /**
     * The worker for setInnerText.
     * @param value the new value for the contents of this node
     */
    protected void setInnerTextImpl(final String value) {
        final DomNode domNode = getDomNodeOrDie();

        domNode.removeAllChildren();

        if (value != null && !value.isEmpty()) {
            domNode.appendChild(new DomText(domNode.getPage(), Context.toString(value)));
        }
    }

    /**
     * Replaces all child elements of this element with the supplied text value.
     * @param value the new value for the contents of this element
     */
    @Override
    public void setTextContent(final Object value) {
        setInnerTextImpl(value == null ? null : Context.toString(value));
    }

    /**
     * Replaces this element (including all child elements) with the supplied value.
     * @param value the new value for replacing this element
     */
    @JsxSetter
    public void setOuterHTML(final Object value) {
        final DomNode domNode = getDomNodeOrDie();
        final DomNode parent = domNode.getParentNode();
        if (null == parent) {
            if (getBrowserVersion().hasFeature(JS_OUTER_HTML_REMOVES_CHILDS_FOR_DETACHED)) {
                domNode.removeAllChildren();
            }
            if (getBrowserVersion().hasFeature(JS_OUTER_HTML_THROWS_FOR_DETACHED)) {
                throw Context.reportRuntimeError("outerHTML is readonly for detached nodes");
            }
            return;
        }

        if (value == null && !getBrowserVersion().hasFeature(JS_OUTER_HTML_NULL_AS_STRING)) {
            domNode.remove();
            return;
        }
        final String valueStr = Context.toString(value);
        if (valueStr.isEmpty()) {
            domNode.remove();
            return;
        }

        final DomNode nextSibling = domNode.getNextSibling();
        domNode.remove();

        final DomNode target;
        final boolean append;
        if (nextSibling != null) {
            target = nextSibling;
            append = false;
        }
        else {
            target = parent;
            append = true;
        }

        final DomNode proxyDomNode = new ProxyDomNode(target.getPage(), target, append);
        parseHtmlSnippet(proxyDomNode, valueStr);
    }

    /**
     * Parses the specified HTML source code, appending the resulting content at the specified target location.
     * @param target the node indicating the position at which the parsed content should be placed
     * @param source the HTML code extract to parse
     */
    public static void parseHtmlSnippet(final DomNode target, final String source) {
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
            super(HtmlDivision.TAG_NAME, page, null);
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
     * Parses the given text as HTML or XML and inserts the resulting nodes into the tree in the position given by the
     * position argument.
     * @param position specifies where to insert the nodes, using one of the following values (case-insensitive):
     *        <code>beforebegin</code>, <code>afterbegin</code>, <code>beforeend</code>, <code>afterend</code>
     * @param text the text to parse
     *
     * @see <a href="http://www.w3.org/TR/DOM-Parsing/#methods-2">W3C Spec</a>
     * @see <a href="http://domparsing.spec.whatwg.org/#dom-element-insertadjacenthtml">WhatWG Spec</a>
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Element.insertAdjacentHTML"
     *      >Mozilla Developer Network</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/ie/ms536452.aspx">MSDN</a>
     */
    @JsxFunction
    public void insertAdjacentHTML(final String position, final String text) {
        final Object[] values = getInsertAdjacentLocation(position);
        final DomNode domNode = (DomNode) values[0];
        final boolean append = ((Boolean) values[1]).booleanValue();

        // add the new nodes
        final DomNode proxyDomNode = new ProxyDomNode(domNode.getPage(), domNode, append);
        parseHtmlSnippet(proxyDomNode, text);
    }

    /**
     * Inserts the given element into the element at the location.
     * @param where specifies where to insert the element, using one of the following values (case-insensitive):
     *        beforebegin, afterbegin, beforeend, afterend
     * @param insertedElement the element to be inserted
     * @return an element object
     *
     * @see <a href="http://msdn.microsoft.com/en-us/library/ie/ms536451.aspx">MSDN</a>
     */
    @JsxFunction({ @WebBrowser(CHROME), @WebBrowser(IE) })
    public Object insertAdjacentElement(final String where, final Object insertedElement) {
        if (insertedElement instanceof Node) {
            final DomNode childNode = ((Node) insertedElement).getDomNodeOrDie();
            final Object[] values = getInsertAdjacentLocation(where);
            final DomNode node = (DomNode) values[0];
            final boolean append = ((Boolean) values[1]).booleanValue();

            if (append) {
                node.appendChild(childNode);
            }
            else {
                node.insertBefore(childNode);
            }
            return insertedElement;
        }
        throw Context.reportRuntimeError("Passed object is not an element: " + insertedElement);
    }

    /**
     * Inserts the given text into the element at the specified location.
     * @param where specifies where to insert the text, using one of the following values (case-insensitive):
     *      beforebegin, afterbegin, beforeend, afterend
     * @param text the text to insert
     *
     * @see <a href="http://msdn.microsoft.com/en-us/library/ie/ms536453.aspx">MSDN</a>
     */
    @JsxFunction({ @WebBrowser(CHROME), @WebBrowser(IE) })
    public void insertAdjacentText(final String where, final String text) {
        final Object[] values = getInsertAdjacentLocation(where);
        final DomNode node = (DomNode) values[0];
        final boolean append = ((Boolean) values[1]).booleanValue();

        final DomText domText = new DomText(node.getPage(), text);
        // add the new nodes
        if (append) {
            node.appendChild(domText);
        }
        else {
            node.insertBefore(domText);
        }
    }

    /**
     * Returns where and how to add the new node.
     * Used by {@link #insertAdjacentHTML(String, String)},
     * {@link #insertAdjacentElement(String, Object)} and
     * {@link #insertAdjacentText(String, String)}.
     * @param where specifies where to insert the element, using one of the following values (case-insensitive):
     *        beforebegin, afterbegin, beforeend, afterend
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
     * Returns {@code true} if cookies are enabled. Part of the <tt>#default#clientCaps</tt>
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
     * Returns {@code true} if Java is enabled. Part of the <tt>#default#clientCaps</tt>
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
        if (i == 0) {
            return 0;
        }
        else if (i < 0) {
            return -1;
        }
        else {
            return 1;
        }
    }

    /**
     * Downloads all the components queued via {@link #addComponentRequest(String, String, String)}.
     * @return {@code true} if the components are downloaded successfully
     * Current implementation always return {@code false}
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
     * Returns {@code true} if the specified component is installed.
     * @param id the identifier for the component to check for
     * @param idType the type of id specified
     * @param minVersion the minimum version to check for
     * @return {@code true} if the specified component is installed
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
     * Returns {@code true} if the specified URL is the web client's current
     * homepage and the document calling the method is on the same domain as the
     * user's homepage. Part of the <tt>#default#homePage</tt> default IE behavior
     * implementation.
     * @param url the URL to check
     * @return {@code true} if the specified URL is the current homepage
     */
    public boolean isHomePage(final String url) {
        try {
            final URL newUrl = new URL(url);
            final URL currentUrl = getDomNodeOrDie().getPage().getUrl();
            final String home = getDomNodeOrDie().getPage().getEnclosingWindow()
                    .getWebClient().getOptions().getHomePage();
            final boolean sameDomains = newUrl.getHost().equalsIgnoreCase(currentUrl.getHost());
            final boolean isHomePage = home != null && home.equals(url);
            return sameDomains && isHomePage;
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
        if (isDislayNone() || !getDomNodeOrDie().isAttachedToPage()) {
            return 0;
        }
        final MouseEvent event = MouseEvent.getCurrentMouseEvent();
        if (isAncestorOfEventTarget(event)) {
            // compute appropriate offset height to pretend mouse event was produced within this element
            return event.getClientY() - getPosY() + 50;
        }
        final ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
        return style.getCalculatedHeight(true, true);
    }

    private boolean isDislayNone() {
        // if a parent is display:none there's nothing that a child can do to override it
        HTMLElement element = this;
        while (element != null) {
            final CSSStyleDeclaration style = element.getWindow().getComputedStyle(element, null);
            final String display = style.getDisplay();
            if (DisplayStyle.NONE.value().equals(display)) {
                return true;
            }
            element = element.getParentHTMLElement();
        }
        return false;
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
        if (isDislayNone() || !getDomNodeOrDie().isAttachedToPage()) {
            return 0;
        }

        final MouseEvent event = MouseEvent.getCurrentMouseEvent();
        if (isAncestorOfEventTarget(event)) {
            // compute appropriate offset width to pretend mouse event was produced within this element
            return event.getClientX() - getPosX() + 50;
        }
        final ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
        return style.getCalculatedWidth(true, true);
    }

    /**
     * Returns {@code true} if this element's node is an ancestor of the specified event's target node.
     * @param event the event whose target node is to be checked
     * @return {@code true} if this element's node is an ancestor of the specified event's target node
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
            final ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
            if (!style.isScrollable(false)) {
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
            final ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
            if (!style.isScrollable(true)) {
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
    public void scrollIntoView() { /* do nothing at the moment */ }

    /**
     * Retrieves a collection of rectangles that describes the layout of the contents of an object
     * or range within the client. Each rectangle describes a single line.
     * @return a collection of rectangles that describes the layout of the contents
     */
    @JsxFunction
    public Object getClientRects() {
        final ClientRectList rectList = new ClientRectList();
        rectList.setParentScope(getWindow());
        rectList.setPrototype(getPrototype(rectList.getClass()));

        if (getDomNodeOrDie().isAttachedToPage()) {
            final ClientRect rect = new ClientRect(0, 0, 1, 1);
            rect.setParentScope(getWindow());
            rect.setPrototype(getPrototype(rect.getClass()));
            rectList.add(rect);
        }

        return rectList;
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
     * {@inheritDoc}
     */
    @Override
    public HtmlElement getDomNodeOrDie() {
        return (HtmlElement) super.getDomNodeOrDie();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlElement getDomNodeOrNull() {
        return (HtmlElement) super.getDomNodeOrNull();
    }

    /**
     * Remove focus from this element.
     */
    @Override
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(IE) })
    public void blur() {
        super.blur();
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
     * Sets the focus to this element.
     */
    @JsxFunction
    public void focus() {
        final HtmlElement domNode = getDomNodeOrDie();
        if (domNode instanceof SubmittableElement) {
            domNode.focus();
        }

        // no action otherwise!
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
     * {@inheritDoc}
     */
    @Override
    public String getNodeName() {
        final DomNode domNode = getDomNodeOrDie();
        String nodeName = domNode.getNodeName();
        if (domNode.getHtmlPageOrNull() != null) {
            nodeName = nodeName.toUpperCase(Locale.ROOT);
        }
        return nodeName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrefix() {
        return null;
    }

    /**
     * Click this element. This simulates the action of the user clicking with the mouse.
     * @throws IOException if this click triggers a page load that encounters problems
     */
    @JsxFunction
    public void click() throws IOException {
        getDomNodeOrDie().click();
    }

    /**
     * Returns the {@code spellcheck} property.
     * @return the {@code spellcheck} property
     */
    @JsxGetter(@WebBrowser(FF))
    public boolean getSpellcheck() {
        return Context.toBoolean(getDomNodeOrDie().getAttribute("spellcheck"));
    }

    /**
     * Sets the {@code spellcheck} property.
     * @param spellcheck the {@code spellcheck} property
     */
    @JsxSetter(@WebBrowser(FF))
    public void setSpellcheck(final boolean spellcheck) {
        getDomNodeOrDie().setAttribute("spellcheck", Boolean.toString(spellcheck));
    }

    /**
     * Returns the {@code lang} property.
     * @return the {@code lang} property
     */
    @JsxGetter
    public String getLang() {
        return getDomNodeOrDie().getAttribute("lang");
    }

    /**
     * Sets the {@code lang} property.
     * @param lang the {@code lang} property
     */
    @JsxSetter
    public void setLang(final String lang) {
        getDomNodeOrDie().setAttribute("lang", lang);
    }

    /**
     * Returns the {@code language} property.
     * @return the {@code language} property
     */
    @JsxGetter(@WebBrowser(IE))
    public String getLanguage() {
        return getDomNodeOrDie().getAttribute("language");
    }

    /**
     * Sets the {@code language} property.
     * @param language the {@code language} property
     */
    @JsxSetter(@WebBrowser(IE))
    public void setLanguage(final String language) {
        getDomNodeOrDie().setAttribute("language", language);
    }

    /**
     * Returns the {@code dir} property.
     * @return the {@code dir} property
     */
    @JsxGetter
    public String getDir() {
        return getDomNodeOrDie().getAttribute("dir");
    }

    /**
     * Sets the {@code dir} property.
     * @param dir the {@code dir} property
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
     * Sets the {@code tabIndex} property.
     * @param tabIndex the {@code tabIndex} property
     */
    @JsxSetter
    public void setTabIndex(final int tabIndex) {
        getDomNodeOrDie().setAttribute("tabindex", Integer.toString(tabIndex));
    }

    /**
     * Returns the {@code accessKey} property.
     * @return the {@code accessKey} property
     */
    @JsxGetter
    public String getAccessKey() {
        return getDomNodeOrDie().getAttribute("accesskey");
    }

    /**
     * Sets the {@code accessKey} property.
     * @param accessKey the {@code accessKey} property
     */
    @JsxSetter
    public void setAccessKey(final String accessKey) {
        getDomNodeOrDie().setAttribute("accesskey", accessKey);
    }

    /**
     * Returns the value of the specified attribute (width or height).
     * @return the value of the specified attribute (width or height)
     * @param attributeName the name of the attribute to return (<tt>"width"</tt> or <tt>"height"</tt>)
     * @param returnNegativeValues if {@code true}, negative values are returned;
     *        if {@code false}, this method returns an empty string in lieu of negative values;
     *        if {@code null}, this method returns <tt>0</tt> in lieu of negative values
     */
    protected String getWidthOrHeight(final String attributeName, final Boolean returnNegativeValues) {
        String value = getDomNodeOrDie().getAttribute(attributeName);
        if (getBrowserVersion().hasFeature(JS_WIDTH_HEIGHT_ACCEPTS_ARBITRARY_VALUES)) {
            return value;
        }
        if (!PERCENT_VALUE.matcher(value).matches()) {
            try {
                final Float f = Float.valueOf(value);
                final int i = f.intValue();
                if (i < 0) {
                    if (returnNegativeValues == null) {
                        value = "0";
                    }
                    else if (!returnNegativeValues.booleanValue()) {
                        value = "";
                    }
                    else {
                        value = Integer.toString(i);
                    }
                }
                else {
                    value = Integer.toString(i);
                }
            }
            catch (final NumberFormatException e) {
                if (!getBrowserVersion().hasFeature(JS_WIDTH_HEIGHT_ACCEPTS_ARBITRARY_VALUES)) {
                    value = "";
                }
            }
        }
        return value;
    }

    /**
     * Sets the value of the specified attribute (width or height).
     * @param attributeName the name of the attribute to set (<tt>"width"</tt> or <tt>"height"</tt>)
     * @param value the value of the specified attribute (width or height)
     * @param allowNegativeValues if {@code true}, negative values will be stored;
     *        if {@code false}, negative values cause an exception to be thrown;<br>
     *        this check/conversion is only done if the feature JS_WIDTH_HEIGHT_ACCEPTS_ARBITRARY_VALUES
     *        is set for the simulated browser
     */
    protected void setWidthOrHeight(final String attributeName, String value, final boolean allowNegativeValues) {
        if (!getBrowserVersion().hasFeature(JS_WIDTH_HEIGHT_ACCEPTS_ARBITRARY_VALUES) && !value.isEmpty()) {
            if (value.endsWith("px")) {
                value = value.substring(0, value.length() - 2);
            }
            boolean error = false;
            if (!PERCENT_VALUE.matcher(value).matches()) {
                try {
                    final Float f = Float.valueOf(value);
                    final int i = f.intValue();
                    if (i < 0) {
                        if (!allowNegativeValues) {
                            error = true;
                        }
                    }
                }
                catch (final NumberFormatException e) {
                    error = true;
                }
            }
            if (error) {
                final Exception e = new Exception("Cannot set the '" + attributeName
                        + "' property to invalid value: '" + value + "'");
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
        if (!s.isEmpty()) {
            final boolean restrict = getBrowserVersion().hasFeature(HTML_COLOR_RESTRICT);

            boolean isName = false;
            if (restrict) {
                for (final String key : COLORS_MAP_IE.keySet()) {
                    if (key.equalsIgnoreCase(value)) {
                        isName = true;
                        break;
                    }
                }
            }
            if (!isName) {
                if (restrict) {
                    if (s.charAt(0) == '#') {
                        s = s.substring(1);
                    }
                    final StringBuilder builder = new StringBuilder(7);
                    for (int x = 0; x < 6 && x < s.length(); x++) {
                        final char ch = s.charAt(x);
                        if ((ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F')) {
                            builder.append(ch);
                        }
                        else {
                            builder.append('0');
                        }
                    }
                    builder.insert(0, '#');
                    s = builder.toString();
                }
            }
            if (getBrowserVersion().hasFeature(HTML_COLOR_TO_LOWER)) {
                s = s.toLowerCase(Locale.ROOT);
            }
        }
        getDomNodeOrDie().setAttribute(name, s);
    }

    /**
     * Returns the value of the {@code align} property.
     * @param returnInvalidValues if {@code true}, this method will return any value, including technically
     *        invalid values; if {@code false}, this method will return an empty string instead of invalid values
     * @return the value of the {@code align} property
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
     * Sets the value of the {@code align} property.
     * @param align the value of the {@code align} property
     * @param ignoreIfNoError if {@code true}, the invocation will be a no-op if it does not trigger an error
     *        (i.e., it will not actually set the align attribute)
     */
    protected void setAlign(final String align, final boolean ignoreIfNoError) {
        final String alignLC = align.toLowerCase(Locale.ROOT);
        final boolean acceptArbitraryValues = getBrowserVersion().hasFeature(JS_ALIGN_ACCEPTS_ARBITRARY_VALUES);
        if (acceptArbitraryValues
                || "center".equals(alignLC)
                || "justify".equals(alignLC)
                || "left".equals(alignLC)
                || "right".equals(alignLC)) {
            if (!ignoreIfNoError) {
                final String newValue = acceptArbitraryValues ? align : alignLC;
                getDomNodeOrDie().setAttribute("align", newValue);
            }
            return;
        }

        throw Context.reportRuntimeError("Cannot set the align property to invalid value: '" + align + "'");
    }

    /**
     * Returns the value of the {@code vAlign} property.
     * @param valid the valid values; if {@code null}, any value is valid
     * @param defaultValue the default value to use, if necessary
     * @return the value of the {@code vAlign} property
     */
    protected String getVAlign(final String[] valid, final String defaultValue) {
        final String valign = getDomNodeOrDie().getAttribute("valign");
        if (valid == null || ArrayUtils.contains(valid, valign)) {
            return valign;
        }
        return defaultValue;
    }

    /**
     * Sets the value of the {@code vAlign} property.
     * @param vAlign the value of the {@code vAlign} property
     * @param valid the valid values; if {@code null}, any value is valid
     */
    protected void setVAlign(final Object vAlign, final String[] valid) {
        final String s = Context.toString(vAlign).toLowerCase(Locale.ROOT);
        if (valid == null || ArrayUtils.contains(valid, s)) {
            getDomNodeOrDie().setAttribute("valign", s);
        }
        else {
            throw Context.reportRuntimeError("Cannot set the vAlign property to invalid value: " + vAlign);
        }
    }

    /**
     * Returns the value of the {@code ch} property.
     * @return the value of the {@code ch} property
     */
    protected String getCh() {
        return getDomNodeOrDie().getAttribute("char");
    }

    /**
     * Sets the value of the {@code ch} property.
     * @param ch the value of the {@code ch} property
     */
    protected void setCh(final String ch) {
        getDomNodeOrDie().setAttribute("char", ch);
    }

    /**
     * Returns the value of the {@code chOff} property.
     * @return the value of the {@code chOff} property
     */
    protected String getChOff() {
        return getDomNodeOrDie().getAttribute("charOff");
    }

    /**
     * Sets the value of the {@code chOff} property.
     * @param chOff the value of the {@code chOff} property
     */
    protected void setChOff(String chOff) {
        try {
            final float f = Float.parseFloat(chOff);
            final int i = (int) f;
            if (i == f) {
                chOff = Integer.toString(i);
            }
            else {
                chOff = Float.toString(f);
            }
        }
        catch (final NumberFormatException e) {
            // Ignore.
        }
        getDomNodeOrDie().setAttribute("charOff", chOff);
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
        HTMLElement element = (HTMLElement) node.getScriptableObject();
        ComputedCSSStyleDeclaration style = element.getWindow().getComputedStyle(element, null);
        left += style.getLeft(true, false, false);

        // If this node is absolutely positioned, we're done.
        final String position = style.getPositionWithInheritance();
        if ("absolute".equals(position)) {
            return left;
        }

        // Add the offset for the ancestor nodes.
        node = node.getParentNode();
        while (node != null && node.getScriptableObject() != offsetParent) {
            if (node.getScriptableObject() instanceof HTMLElement) {
                element = (HTMLElement) node.getScriptableObject();
                style = element.getWindow().getComputedStyle(element, null);
                left += style.getLeft(true, true, true);
            }
            node = node.getParentNode();
        }

        if (offsetParent != null) {
            style = offsetParent.getWindow().getComputedStyle(offsetParent, null);
            left += style.getMarginLeftValue();
            left += style.getPaddingLeftValue();
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
                final ComputedCSSStyleDeclaration style = element.getWindow().getComputedStyle(element, null);
                cumulativeOffset += style.getBorderLeftValue();
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
                final ComputedCSSStyleDeclaration style = element.getWindow().getComputedStyle(element, null);
                cumulativeOffset += style.getBorderTopValue();
            }
            element = element.getOffsetParent();
        }
        return cumulativeOffset;
    }

    /**
     * Gets the offset parent or {@code null} if this is not an {@link HTMLElement}.
     * @return the offset parent or {@code null}
     */
    private HTMLElement getOffsetParent() {
        final Object offsetParent = getOffsetParentInternal(false);
        if (offsetParent instanceof HTMLElement) {
            return (HTMLElement) offsetParent;
        }
        return null;
    }

    /**
     * Returns the {@code clientLeft} attribute.
     * @return the {@code clientLeft} attribute
     */
    @JsxGetter
    public int getClientLeft() {
        final ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
        return style.getBorderLeftValue();
    }

    /**
     * Returns {@code clientTop} attribute.
     * @return the {@code clientTop} attribute
     */
    @JsxGetter
    public int getClientTop() {
        final ComputedCSSStyleDeclaration style = getWindow().getComputedStyle(this, null);
        return style.getBorderTopValue();
    }

    /**
     * Returns this element's {@code offsetTop}, which is the calculated top position of this
     * element relative to the {@code offsetParent}.
     *
     * @return this element's {@code offsetTop}
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
        HTMLElement element = (HTMLElement) node.getScriptableObject();
        ComputedCSSStyleDeclaration style = element.getWindow().getComputedStyle(element, null);
        top += style.getTop(true, false, false);

        // If this node is absolutely positioned, we're done.
        final String position = style.getPositionWithInheritance();
        if ("absolute".equals(position)) {
            return top;
        }

        // Add the offset for the ancestor nodes.
        node = node.getParentNode();
        while (node != null && node.getScriptableObject() != offsetParent) {
            if (node.getScriptableObject() instanceof HTMLElement) {
                element = (HTMLElement) node.getScriptableObject();
                style = element.getWindow().getComputedStyle(element, null);
                top += style.getTop(false, true, true);
            }
            node = node.getParentNode();
        }

        if (offsetParent != null) {
            final HTMLElement thiz = (HTMLElement) getDomNodeOrDie().getScriptableObject();
            style = thiz.getWindow().getComputedStyle(thiz, null);
            final boolean thisElementHasTopMargin = style.getMarginTopValue() != 0;

            style = offsetParent.getWindow().getComputedStyle(offsetParent, null);
            if (!thisElementHasTopMargin) {
                top += style.getMarginTopValue();
            }
            top += style.getPaddingTopValue();
        }

        return top;
    }

    /**
     * Returns this element's <tt>offsetParent</tt>. The <tt>offsetLeft</tt> and
     * <tt>offsetTop</tt> attributes are relative to the <tt>offsetParent</tt>.
     *
     * @return this element's <tt>offsetParent</tt>. This may be <code>undefined</code> when this node is
     * not attached or {@code null} for <code>body</code>.
     * @see <a href="http://msdn2.microsoft.com/en-us/library/ms534302.aspx">MSDN Documentation</a>
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_el_ref20.html">Gecko DOM Reference</a>
     * @see <a href="http://www.quirksmode.org/js/elementdimensions.html">Element Dimensions</a>
     * @see <a href="http://www.w3.org/TR/REC-CSS2/box.html">Box Model</a>
     * @see <a href="http://dump.testsuite.org/2006/dom/style/offset/spec">Reverse Engineering by Anne van Kesteren</a>
     */
    @JsxGetter(propertyName = "offsetParent")
    public Object getOffsetParent_js() {
        return getOffsetParentInternal(getBrowserVersion().hasFeature(JS_OFFSET_PARENT_NULL_IF_FIXED));
    }

    private Object getOffsetParentInternal(final boolean returnNullIfFixed) {
        DomNode currentElement = getDomNodeOrDie();

        if (currentElement.getParentNode() == null) {
            return null;
        }

        Object offsetParent = null;
        final HTMLElement htmlElement = (HTMLElement) currentElement.getScriptableObject();
        if (returnNullIfFixed && "fixed".equals(htmlElement.getStyle().getStyleAttribute(
                StyleAttributes.Definition.POSITION))) {
            return null;
        }

        final ComputedCSSStyleDeclaration style = htmlElement.getWindow().getComputedStyle(htmlElement, null);
        final String position = style.getPositionWithInheritance();
        final boolean staticPos = "static".equals(position);

        final boolean useTables = staticPos;

        while (currentElement != null) {

            final DomNode parentNode = currentElement.getParentNode();
            if (parentNode instanceof HtmlBody
                || (useTables && parentNode instanceof HtmlTableDataCell)
                || (useTables && parentNode instanceof HtmlTable)) {
                offsetParent = parentNode.getScriptableObject();
                break;
            }

            if (parentNode != null && parentNode.getScriptableObject() instanceof HTMLElement) {
                final HTMLElement parentElement = (HTMLElement) parentNode.getScriptableObject();
                final ComputedCSSStyleDeclaration parentStyle =
                            parentElement.getWindow().getComputedStyle(parentElement, null);
                final String parentPosition = parentStyle.getPositionWithInheritance();
                final boolean parentIsStatic = "static".equals(parentPosition);
                if (!parentIsStatic) {
                    offsetParent = parentNode.getScriptableObject();
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
        if (!getDomNodeOrDie().isAttachedToPage()
                && getBrowserVersion().hasFeature(JS_BOUNDINGCLIENTRECT_THROWS_IF_DISCONNECTED)) {
            throw Context.reportRuntimeError("Element is not attache to a page");
        }

        int left = getPosX();
        int top = getPosY();

        // account for any scrolled ancestors
        Object parentNode = getOffsetParentInternal(false);
        while (parentNode != null
                && (parentNode instanceof HTMLElement)
                && !(parentNode instanceof HTMLBodyElement)) {
            final HTMLElement elem = (HTMLElement) parentNode;
            left -= elem.getScrollLeft();
            top -= elem.getScrollTop();

            parentNode = elem.getParentNode();
        }

        final ClientRect textRectangle = new ClientRect(top + getOffsetHeight(), left, left + getOffsetWidth(), top);
        textRectangle.setParentScope(getWindow());
        textRectangle.setPrototype(getPrototype(textRectangle.getClass()));
        return textRectangle;
    }

    /**
     * Gets the token list of class attribute.
     * @return the token list of class attribute
     */
    @Override
    @JsxGetter
    public DOMTokenList getClassList() {
        return new DOMTokenList(this, "class");
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxFunction
    public boolean hasAttribute(final String name) {
        return super.hasAttribute(name);
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxGetter({ @WebBrowser(CHROME), @WebBrowser(IE) })
    public HTMLCollection getChildren() {
        return super.getChildren();
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxGetter
    public Element getParentElement() {
        return super.getParentElement();
    }

    /**
     * Returns the {@code dataset} attribute.
     * @return the {@code dataset} attribute
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(CHROME),
        @WebBrowser(IE) })
    public DOMStringMap getDataset() {
        return new DOMStringMap(this);
    }

    /**
     * Returns whether the end tag is forbidden or not.
     * @see <a href="http://www.w3.org/TR/html4/index/elements.html">HTML 4 specs</a>
     * @return whether the end tag is forbidden or not
     */
    protected boolean isEndTagForbidden() {
        return endTagForbidden_;
    }

    /**
     * Returns whether the tag is lower case in .outerHTML/.innerHTML.
     * It seems to be a feature for HTML5 elements for IE.
     * @return whether the tag is lower case in .outerHTML/.innerHTML
     */
    protected boolean isLowerCaseInOuterHtml() {
        return false;
    }

    /**
     * Sets the {@code onchange} event handler for this element.
     * @param onchange the {@code onchange} event handler for this element
     */
    @JsxSetter
    public void setOnchange(final Object onchange) {
        setEventHandlerProp("onchange", onchange);
    }

    /**
     * Returns the {@code onchange} event handler for this element.
     * @return the {@code onchange} event handler for this element
     */
    @JsxGetter
    public Function getOnchange() {
        return getEventHandler("onchange");
    }

    /**
     * Returns the {@code onsubmit} event handler for this element.
     * @return the {@code onsubmit} event handler for this element
     */
    @JsxGetter
    public Object getOnsubmit() {
        return getEventHandlerProp("onsubmit");
    }

    /**
     * Sets the {@code onsubmit} event handler for this element.
     * @param onsubmit the {@code onsubmit} event handler for this element
     */
    @JsxSetter
    public void setOnsubmit(final Object onsubmit) {
        setEventHandlerProp("onsubmit", onsubmit);
    }

    /**
     * Returns the default display style.
     *
     * @return the default display style
     */
    public String getDefaultStyleDisplay() {
        final HtmlElement htmlElt = getDomNodeOrDie();
        return htmlElt.getDefaultStyleDisplay().value();
    }

    /**
     * Mock for the moment.
     * @param retargetToElement if true, all events are targeted directly to this element;
     * if false, events can also fire at descendants of this element
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(IE) })
    public void setCapture(final boolean retargetToElement) {
        // empty
    }

    /**
     * Mock for the moment.
     * @return true for success
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(IE) })
    public boolean releaseCapture() {
        return true;
    }

    /**
     * Returns the {@code contentEditable} property.
     * @return the {@code contentEditable} property
     */
    @JsxGetter
    public String getContentEditable() {
        final String attribute = getDomNodeOrDie().getAttribute("contentEditable");
        if (attribute == DomElement.ATTRIBUTE_NOT_DEFINED) {
            return "inherit";
        }
        if (attribute == DomElement.ATTRIBUTE_VALUE_EMPTY) {
            return "true";
        }
        return attribute;
    }

    /**
     * Sets the {@code contentEditable} property.
     * @param contentEditable the {@code contentEditable} property to set
     */
    @JsxSetter
    public void setContentEditable(final String contentEditable) {
        getDomNodeOrDie().setAttribute("contentEditable", contentEditable);
    }

    /**
     * Returns the {@code isContentEditable} property.
     * @return the {@code isContentEditable} property
     */
    @JsxGetter
    public boolean getIsContentEditable() {
        final String attribute = getContentEditable();
        if ("true".equals(attribute)) {
            return true;
        }
        else if ("inherit".equals(attribute)) {
            final DomNode parent = getDomNodeOrDie().getParentNode();
            if (parent != null) {
                final Object parentScriptable = parent.getScriptableObject();
                if (parentScriptable instanceof HTMLElement) {
                    return ((HTMLElement) parentScriptable).getIsContentEditable();
                }
            }
        }
        return false;
    }
}
