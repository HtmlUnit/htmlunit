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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTML_COLOR_RESTRICT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTML_COLOR_TO_LOWER;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_ALIGN_ACCEPTS_ARBITRARY_VALUES;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INNER_TEXT_VALUE_NULL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_OFFSET_PARENT_NULL_IF_FIXED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_VALIGN_CONVERTS_TO_LOWERCASE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WIDTH_HEIGHT_ACCEPTS_ARBITRARY_VALUES;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
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
import com.gargoylesoftware.htmlunit.html.HtmlDefinition;
import com.gargoylesoftware.htmlunit.html.HtmlDefinitionDescription;
import com.gargoylesoftware.htmlunit.html.HtmlDefinitionTerm;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlEmphasis;
import com.gargoylesoftware.htmlunit.html.HtmlFigure;
import com.gargoylesoftware.htmlunit.html.HtmlFigureCaption;
import com.gargoylesoftware.htmlunit.html.HtmlFooter;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHeader;
import com.gargoylesoftware.htmlunit.html.HtmlItalic;
import com.gargoylesoftware.htmlunit.html.HtmlKeyboard;
import com.gargoylesoftware.htmlunit.html.HtmlLayer;
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
import com.gargoylesoftware.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJob;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.ClientRect;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleAttributes;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMStringMap;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMTokenList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node;
import com.gargoylesoftware.htmlunit.javascript.host.dom.NodeList;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
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
@JsxClass(domClass = HtmlAbbreviated.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlAcronym.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlAddress.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlArticle.class)
@JsxClass(domClass = HtmlAside.class)
@JsxClass(domClass = HtmlBaseFont.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlBidirectionalIsolation.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlBidirectionalOverride.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlBig.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlBold.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlCenter.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlCitation.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlCode.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlDefinition.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlDefinitionDescription.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlDefinitionTerm.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlElement.class, value = {FF, FF78, IE})
@JsxClass(domClass = HtmlEmphasis.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlFigure.class)
@JsxClass(domClass = HtmlFigureCaption.class)
@JsxClass(domClass = HtmlFooter.class)
@JsxClass(domClass = HtmlHeader.class)
@JsxClass(domClass = HtmlItalic.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlKeyboard.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlLayer.class, value = {CHROME, EDGE})
@JsxClass(domClass = HtmlMark.class)
@JsxClass(domClass = HtmlNav.class)
@JsxClass(domClass = HtmlNoBreak.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlNoEmbed.class)
@JsxClass(domClass = HtmlNoFrames.class)
@JsxClass(domClass = HtmlNoLayer.class, value = {CHROME, EDGE})
@JsxClass(domClass = HtmlNoScript.class)
@JsxClass(domClass = HtmlPlainText.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlRuby.class, value = {CHROME, EDGE})
@JsxClass(domClass = HtmlRp.class, value = {CHROME, EDGE})
@JsxClass(domClass = HtmlRt.class, value = {CHROME, EDGE})
@JsxClass(domClass = HtmlS.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlSample.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlSection.class)
@JsxClass(domClass = HtmlSmall.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlStrike.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlStrong.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlSubscript.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlSummary.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlSuperscript.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlTeletype.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlUnderlined.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlWordBreak.class)
@JsxClass(domClass = HtmlMain.class, value = {CHROME, EDGE, FF, FF78})
@JsxClass(domClass = HtmlVariable.class, value = {CHROME, EDGE, FF, FF78})
public class HTMLElement extends Element {

    private static final Class<?>[] METHOD_PARAMS_OBJECT = {Object.class};
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

    /**
     * Static counter for {@link #uniqueID_}.
     */
    private static AtomicInteger UniqueID_Counter_ = new AtomicInteger(1);

    private final Set<String> behaviors_ = new HashSet<>();
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
        COLORS_MAP_IE.put("OrangeRed", "#FF6000");
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
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public HTMLElement() {
    }

    /**
     * Sets the DOM node that corresponds to this JavaScript object.
     * @param domNode the DOM node
     */
    @Override
    public void setDomNode(final DomNode domNode) {
        super.setDomNode(domNode);

        final String name = domNode.getLocalName();
        if ("wbr".equalsIgnoreCase(name)
                || "basefont".equalsIgnoreCase(name)
                || "keygen".equalsIgnoreCase(name)
                || "track".equalsIgnoreCase(name)) {
            endTagForbidden_ = true;
        }

        if ("input".equalsIgnoreCase(name)
                || "button".equalsIgnoreCase(name)
                || "textarea".equalsIgnoreCase(name)
                || "select".equalsIgnoreCase(name)) {
            final HtmlForm form = ((HtmlElement) domNode).getEnclosingForm();
            if (form != null) {
                setParentScope(getScriptableFor(form));
            }
        }
    }

    /**
     * Returns the value of the JavaScript {@code form} attribute.
     *
     * @return the value of the JavaScript {@code form} attribute
     */
    public HTMLFormElement getForm() {
        final HtmlForm form = getDomNodeOrDie().getEnclosingForm();
        if (form == null) {
            return null;
        }
        return (HTMLFormElement) getScriptableFor(form);
    }

    /**
     * Returns the element title.
     * @return the ID of this element
     */
    @JsxGetter
    public String getTitle() {
        return getDomNodeOrDie().getAttributeDirect("title");
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
    @JsxGetter(IE)
    public boolean isDisabled() {
        return getDomNodeOrDie().hasAttribute("disabled");
    }

    /**
     * Sets whether or not to disable this element.
     * @param disabled True if this is to be disabled
     */
    @JsxSetter(IE)
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
                final StringBuilder localName = new StringBuilder(prefix.toLowerCase(Locale.ROOT))
                    .append(':')
                    .append(domNode.getLocalName().toLowerCase(Locale.ROOT));
                return localName.toString();
            }
            return domNode.getLocalName().toLowerCase(Locale.ROOT);
        }
        return domNode.getLocalName();
    }

    /**
     * An IE-only method which clears all custom attributes.
     */
    @JsxFunction(IE)
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
    @JsxFunction(IE)
    public void mergeAttributes(final HTMLElement source, final Object preserveIdentity) {
        final HtmlElement src = source.getDomNodeOrDie();
        final HtmlElement target = getDomNodeOrDie();

        // Merge ID and name if we aren't preserving identity.
        if (preserveIdentity instanceof Boolean && !((Boolean) preserveIdentity).booleanValue()) {
            target.setId(src.getId());
            target.setAttribute("name", src.getAttributeDirect("name"));
        }
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
                    method.invoke(this, new EventHandler(getDomNodeOrDie(), name.substring(2), value));
                }
                catch (final NoSuchMethodException | IllegalAccessException e) {
                    //silently ignore
                }
                catch (final InvocationTargetException e) {
                    throw new RuntimeException(e.getCause());
                }
            }
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
     * Removes this object from the document hierarchy.
     * @param removeChildren whether to remove children or no
     * @return a reference to the object that is removed
     */
    @JsxFunction(IE)
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
    public Object getAttributeNode(final String attributeName) {
        return getAttributes().getNamedItem(attributeName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxFunction(IE)
    public HTMLCollection getElementsByClassName(final String className) {
        return super.getElementsByClassName(className);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter(propertyName = "className", value = IE)
    public Object getClassName_js() {
        return super.getClassName_js();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter(IE)
    public String getOuterHTML() {
        return super.getOuterHTML();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter(IE)
    public void setOuterHTML(final Object value) {
        super.setOuterHTML(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter(IE)
    public String getInnerHTML() {
        return super.getInnerHTML();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter(IE)
    public void setInnerHTML(final Object value) {
        super.setInnerHTML(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter(propertyName = "className", value = IE)
    public void setClassName_js(final String className) {
        super.setClassName_js(className);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxFunction(IE)
    public void insertAdjacentHTML(final String position, final String text) {
        super.insertAdjacentHTML(position, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxFunction(IE)
    public void insertAdjacentText(final String where, final String text) {
        super.insertAdjacentText(where, text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxFunction(IE)
    public Object insertAdjacentElement(final String where, final Object insertedElement) {
        return super.insertAdjacentElement(where, insertedElement);
    }

    /**
     * Gets the innerText attribute.
     * @return the contents of this node as text
     */
    @JsxGetter
    public String getInnerText() {
        final StringBuilder buf = new StringBuilder();
        // we can't rely on DomNode.asXml because it adds indentation and new lines
        printChildren(buf, getDomNodeOrDie(), false, false);
        return buf.toString();
    }

    /**
     * Replaces all child elements of this element with the supplied text value.
     * @param value the new value for the contents of this element
     */
    @JsxSetter
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
            domNode.appendChild(new DomText(domNode.getPage(), value));
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
            if (LOG.isWarnEnabled()) {
                LOG.warn("Unimplemented behavior: " + behavior);
            }
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
                if (LOG.isWarnEnabled()) {
                    LOG.warn("Unexpected behavior id: " + id + ". Ignoring.");
                }
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
     * Returns {@code true} if cookies are enabled. Part of the <tt>#default#clientCaps</tt>
     * default IE behavior implementation.
     * @return whether or not cookies are enabled
     */
    public boolean isCookieEnabled() {
        return getWindow().getNavigator().isCookieEnabled();
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
    public boolean isJavaEnabled() {
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
     * @return -1 if v1 is less than v2, 0 if v1 equals v2, and 1 if v1 is more than v2
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
        final WebWindow ww = getWindow().getWebWindow();
        final HtmlPage page = (HtmlPage) ww.getEnclosedPage();
        final URL url = page.getFullyQualifiedUrl(uri);
        if (!page.getUrl().getHost().equals(url.getHost())) {
            throw Context.reportRuntimeError("Not authorized url: " + url);
        }
        final JavaScriptJob job = BackgroundJavaScriptFactory.theFactory().
                createDownloadBehaviorJob(url, callback, ww.getWebClient());
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
        if (isDisplayNone() || !getDomNodeOrDie().isAttachedToPage()) {
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
        if (isDisplayNone() || !getDomNodeOrDie().isAttachedToPage()) {
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
        if (!(event.getSrcElement() instanceof HTMLElement)) {
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
     * Sets the Uniform Resource Name (URN) specified in the namespace declaration.
     * @param tagUrn the Uniform Resource Name (URN) specified in the namespace declaration
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534658.aspx">MSDN documentation</a>
     */
    @JsxSetter(IE)
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
     * {@inheritDoc}
     */
    @Override
    @JsxFunction(IE)
    public void scrollIntoView() {
        /* do nothing at the moment */
    }

    /**
     * Retrieves an auto-generated, unique identifier for the object.
     * <b>Note</b> The unique ID generated is not guaranteed to be the same every time the page is loaded.
     * @return an auto-generated, unique identifier for the object
     */
    @JsxGetter(IE)
    public String getUniqueID() {
        if (uniqueID_ == null) {
            uniqueID_ = "ms__id" + HTMLElement.UniqueID_Counter_.incrementAndGet();
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
    @JsxFunction
    public void blur() {
        getDomNodeOrDie().blur();
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
    @JsxFunction(IE)
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
        // when triggered from js the visibility is ignored
        getDomNodeOrDie().click(false, false, false, true, true, false);
    }

    /**
     * Returns the {@code spellcheck} property.
     * @return the {@code spellcheck} property
     */
    @JsxGetter({FF, FF78})
    public boolean isSpellcheck() {
        return Context.toBoolean(getDomNodeOrDie().getAttributeDirect("spellcheck"));
    }

    /**
     * Sets the {@code spellcheck} property.
     * @param spellcheck the {@code spellcheck} property
     */
    @JsxSetter({FF, FF78})
    public void setSpellcheck(final boolean spellcheck) {
        getDomNodeOrDie().setAttribute("spellcheck", Boolean.toString(spellcheck));
    }

    /**
     * Returns the {@code lang} property.
     * @return the {@code lang} property
     */
    @JsxGetter
    public String getLang() {
        return getDomNodeOrDie().getAttributeDirect("lang");
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
    @JsxGetter(IE)
    public String getLanguage() {
        return getDomNodeOrDie().getAttributeDirect("language");
    }

    /**
     * Sets the {@code language} property.
     * @param language the {@code language} property
     */
    @JsxSetter(IE)
    public void setLanguage(final String language) {
        getDomNodeOrDie().setAttribute("language", language);
    }

    /**
     * Returns the {@code dir} property.
     * @return the {@code dir} property
     */
    @JsxGetter
    public String getDir() {
        return getDomNodeOrDie().getAttributeDirect("dir");
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
        return (int) Context.toNumber(getDomNodeOrDie().getAttributeDirect("tabindex"));
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
        return getDomNodeOrDie().getAttributeDirect("accesskey");
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
                    if (i < 0 && !allowNegativeValues) {
                        error = true;
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
            if (!isName && restrict) {
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

        final String align = getDomNodeOrDie().getAttributeDirect("align");
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
                || "right".equals(alignLC)
                || "bottom".equals(alignLC)
                || "middle".equals(alignLC)
                || "top".equals(alignLC)) {
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
        final String valign = getDomNodeOrDie().getAttributeDirect("valign");
        final String valignLC = valign.toLowerCase(Locale.ROOT);
        if (valid == null || ArrayUtils.contains(valid, valignLC)) {
            if (getBrowserVersion().hasFeature(JS_VALIGN_CONVERTS_TO_LOWERCASE)) {
                return valignLC;
            }
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
        final String valign = Context.toString(vAlign);
        final String valignLC = valign.toLowerCase(Locale.ROOT);
        if (valid == null || ArrayUtils.contains(valid, valignLC)) {
            if (getBrowserVersion().hasFeature(JS_VALIGN_CONVERTS_TO_LOWERCASE)) {
                getDomNodeOrDie().setAttribute("valign", valignLC);
            }
            else {
                getDomNodeOrDie().setAttribute("valign", valign);
            }
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
        return getDomNodeOrDie().getAttributeDirect("char");
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
        HTMLElement element = node.getScriptableObject();
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
                element = node.getScriptableObject();
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
        HTMLElement element = node.getScriptableObject();
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
                element = node.getScriptableObject();
                style = element.getWindow().getComputedStyle(element, null);
                top += style.getTop(false, true, true);
            }
            node = node.getParentNode();
        }

        if (offsetParent != null) {
            final HTMLElement thiz = getDomNodeOrDie().getScriptableObject();
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

        final HTMLElement htmlElement = currentElement.getScriptableObject();
        if (returnNullIfFixed && "fixed".equals(htmlElement.getStyle().getStyleAttribute(
                StyleAttributes.Definition.POSITION, true))) {
            return null;
        }

        final ComputedCSSStyleDeclaration style = htmlElement.getWindow().getComputedStyle(htmlElement, null);
        final String position = style.getPositionWithInheritance();
        final boolean staticPos = "static".equals(position);

        while (currentElement != null) {

            final DomNode parentNode = currentElement.getParentNode();
            if (parentNode instanceof HtmlBody
                || (staticPos && parentNode instanceof HtmlTableDataCell)
                || (staticPos && parentNode instanceof HtmlTable)) {
                return parentNode.getScriptableObject();
            }

            if (parentNode != null && parentNode.getScriptableObject() instanceof HTMLElement) {
                final HTMLElement parentElement = parentNode.getScriptableObject();
                final ComputedCSSStyleDeclaration parentStyle =
                            parentElement.getWindow().getComputedStyle(parentElement, null);
                final String parentPosition = parentStyle.getPositionWithInheritance();
                if (!"static".equals(parentPosition)) {
                    return parentNode.getScriptableObject();
                }
            }

            currentElement = currentElement.getParentNode();
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClientRect getBoundingClientRect() {
        final ClientRect textRectangle = super.getBoundingClientRect();

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

        textRectangle.setBottom(top + getOffsetHeight());
        textRectangle.setLeft(left);
        textRectangle.setRight(left + getOffsetWidth());
        textRectangle.setTop(top);

        return textRectangle;
    }

    /**
     * Gets the token list of class attribute.
     * @return the token list of class attribute
     */
    @Override
    @JsxGetter(IE)
    public DOMTokenList getClassList() {
        return super.getClassList();
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxGetter(IE)
    public HTMLCollection getChildren() {
        return super.getChildren();
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxGetter(IE)
    public Element getParentElement() {
        return super.getParentElement();
    }

    /**
     * Returns the {@code dataset} attribute.
     * @return the {@code dataset} attribute
     */
    @JsxGetter({CHROME, EDGE, FF, FF78, IE})
    public DOMStringMap getDataset() {
        return new DOMStringMap(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
        setEventHandler(Event.TYPE_CHANGE, onchange);
    }

    /**
     * Returns the {@code onchange} event handler for this element.
     * @return the {@code onchange} event handler for this element
     */
    @JsxGetter
    public Function getOnchange() {
        return getEventHandler(Event.TYPE_CHANGE);
    }

    /**
     * Returns the {@code onsubmit} event handler for this element.
     * @return the {@code onsubmit} event handler for this element
     */
    @JsxGetter
    public Object getOnsubmit() {
        return getEventHandler(Event.TYPE_SUBMIT);
    }

    /**
     * Sets the {@code onsubmit} event handler for this element.
     * @param onsubmit the {@code onsubmit} event handler for this element
     */
    @JsxSetter
    public void setOnsubmit(final Object onsubmit) {
        setEventHandler(Event.TYPE_SUBMIT, onsubmit);
    }

    /**
     * Returns the {@code onwheel} event handler for this element.
     * @return the {@code onwheel} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    @Override
    public Function getOnwheel() {
        return super.getOnwheel();
    }

    /**
     * Sets the {@code onwheel} event handler for this element.
     * @param onwheel the {@code onwheel} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    @Override
    public void setOnwheel(final Object onwheel) {
        super.setOnwheel(onwheel);
    }

    /**
     * Mock for the moment.
     * @param retargetToElement if true, all events are targeted directly to this element;
     * if false, events can also fire at descendants of this element
     */
    @JsxFunction(IE)
    @Override
    public void setCapture(final boolean retargetToElement) {
        super.setCapture(retargetToElement);
    }

    /**
     * Mock for the moment.
     * @return true for success
     */
    @JsxFunction(IE)
    @Override
    public boolean releaseCapture() {
        return super.releaseCapture();
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
    public boolean isIsContentEditable() {
        final String attribute = getContentEditable();
        if ("true".equals(attribute)) {
            return true;
        }

        if ("inherit".equals(attribute)) {
            final DomNode parent = getDomNodeOrDie().getParentNode();
            if (parent != null) {
                final Object parentScriptable = parent.getScriptableObject();
                if (parentScriptable instanceof HTMLElement) {
                    return ((HTMLElement) parentScriptable).isIsContentEditable();
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter
    public CSSStyleDeclaration getStyle() {
        return super.getStyle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter
    public void setStyle(final String style) {
        super.setStyle(style);
    }

    /**
     * Returns the runtime style object for this element.
     * @return the runtime style object for this element
     */
    @JsxGetter(IE)
    public CSSStyleDeclaration getRuntimeStyle() {
        return super.getStyle();
    }

    /**
     * Returns the current (calculated) style object for this element.
     * @return the current (calculated) style object for this element
     */
    @JsxGetter(IE)
    public ComputedCSSStyleDeclaration getCurrentStyle() {
        if (!getDomNodeOrDie().isAttachedToPage()) {
            return null;
        }
        return getWindow().getComputedStyle(this, null);
    }

    /**
     * Sets the {@code onclick} event handler for this element.
     * @param handler the {@code onclick} event handler for this element
     */
    @JsxSetter
    public void setOnclick(final Object handler) {
        setEventHandler(MouseEvent.TYPE_CLICK, handler);
    }

    /**
     * Returns the {@code onclick} event handler for this element.
     * @return the {@code onclick} event handler for this element
     */
    @JsxGetter
    public Object getOnclick() {
        return getEventHandler(MouseEvent.TYPE_CLICK);
    }

    /**
     * Sets the {@code ondblclick} event handler for this element.
     * @param handler the {@code ondblclick} event handler for this element
     */
    @JsxSetter
    public void setOndblclick(final Object handler) {
        setEventHandler(MouseEvent.TYPE_DBL_CLICK, handler);
    }

    /**
     * Returns the {@code ondblclick} event handler for this element.
     * @return the {@code ondblclick} event handler for this element
     */
    @JsxGetter
    public Object getOndblclick() {
        return getEventHandler(MouseEvent.TYPE_DBL_CLICK);
    }

    /**
     * Sets the {@code onblur} event handler for this element.
     * @param handler the {@code onblur} event handler for this element
     */
    @JsxSetter
    public void setOnblur(final Object handler) {
        setEventHandler(Event.TYPE_BLUR, handler);
    }

    /**
     * Returns the {@code onblur} event handler for this element.
     * @return the {@code onblur} event handler for this element
     */
    @JsxGetter
    public Object getOnblur() {
        return getEventHandler(Event.TYPE_BLUR);
    }

    /**
     * Sets the {@code onfocus} event handler for this element.
     * @param handler the {@code onfocus} event handler for this element
     */
    @JsxSetter
    public void setOnfocus(final Object handler) {
        setEventHandler(Event.TYPE_FOCUS, handler);
    }

    /**
     * Returns the {@code onfocus} event handler for this element.
     * @return the {@code onfocus} event handler for this element
     */
    @JsxGetter
    public Object getOnfocus() {
        return getEventHandler(Event.TYPE_FOCUS);
    }

    /**
     * Sets the {@code onfocusin} event handler for this element.
     * @param handler the {@code onfocusin} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnfocusin(final Object handler) {
        setEventHandler(Event.TYPE_FOCUS_IN, handler);
    }

    /**
     * Returns the {@code onfocusin} event handler for this element.
     * @return the {@code onfocusin} event handler for this element
     */
    @JsxGetter(IE)
    public Object getOnfocusin() {
        return getEventHandler(Event.TYPE_FOCUS_IN);
    }

    /**
     * Sets the {@code onfocusout} event handler for this element.
     * @param handler the {@code onfocusout} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnfocusout(final Object handler) {
        setEventHandler(Event.TYPE_FOCUS_OUT, handler);
    }

    /**
     * Returns the {@code onfocusout} event handler for this element.
     * @return the {@code onfocusout} event handler for this element
     */
    @JsxGetter(IE)
    public Object getOnfocusout() {
        return getEventHandler(Event.TYPE_FOCUS_OUT);
    }

    /**
     * Sets the {@code onkeydown} event handler for this element.
     * @param handler the {@code onkeydown} event handler for this element
     */
    @JsxSetter
    public void setOnkeydown(final Object handler) {
        setEventHandler(Event.TYPE_KEY_DOWN, handler);
    }

    /**
     * Returns the {@code onkeydown} event handler for this element.
     * @return the {@code onkeydown} event handler for this element
     */
    @JsxGetter
    public Object getOnkeydown() {
        return getEventHandler(Event.TYPE_KEY_DOWN);
    }

    /**
     * Sets the {@code onkeypress} event handler for this element.
     * @param handler the {@code onkeypress} event handler for this element
     */
    @JsxSetter
    public void setOnkeypress(final Object handler) {
        setEventHandler(Event.TYPE_KEY_PRESS, handler);
    }

    /**
     * Returns the {@code onkeypress} event handler for this element.
     * @return the {@code onkeypress} event handler for this element
     */
    @JsxGetter
    public Object getOnkeypress() {
        return getEventHandler(Event.TYPE_KEY_PRESS);
    }

    /**
     * Sets the {@code onkeyup} event handler for this element.
     * @param handler the {@code onkeyup} event handler for this element
     */
    @JsxSetter
    public void setOnkeyup(final Object handler) {
        setEventHandler(Event.TYPE_KEY_UP, handler);
    }

    /**
     * Returns the {@code onkeyup} event handler for this element.
     * @return the {@code onkeyup} event handler for this element
     */
    @JsxGetter
    public Object getOnkeyup() {
        return getEventHandler(Event.TYPE_KEY_UP);
    }

    /**
     * Sets the {@code onmousedown} event handler for this element.
     * @param handler the {@code onmousedown} event handler for this element
     */
    @JsxSetter
    public void setOnmousedown(final Object handler) {
        setEventHandler(MouseEvent.TYPE_MOUSE_DOWN, handler);
    }

    /**
     * Returns the {@code onmousedown} event handler for this element.
     * @return the {@code onmousedown} event handler for this element
     */
    @JsxGetter
    public Object getOnmousedown() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_DOWN);
    }

    /**
     * Sets the {@code onmousemove} event handler for this element.
     * @param handler the {@code onmousemove} event handler for this element
     */
    @JsxSetter
    public void setOnmousemove(final Object handler) {
        setEventHandler(MouseEvent.TYPE_MOUSE_MOVE, handler);
    }

    /**
     * Returns the {@code onmousemove} event handler for this element.
     * @return the {@code onmousemove} event handler for this element
     */
    @JsxGetter
    public Object getOnmousemove() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_MOVE);
    }

    /**
     * Sets the {@code onmouseout} event handler for this element.
     * @param handler the {@code onmouseout} event handler for this element
     */
    @JsxSetter
    public void setOnmouseout(final Object handler) {
        setEventHandler(MouseEvent.TYPE_MOUSE_OUT, handler);
    }

    /**
     * Returns the {@code onmouseout} event handler for this element.
     * @return the {@code onmouseout} event handler for this element
     */
    @JsxGetter
    public Object getOnmouseout() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_OUT);
    }

    /**
     * Sets the {@code onmouseover} event handler for this element.
     * @param handler the {@code onmouseover} event handler for this element
     */
    @JsxSetter
    public void setOnmouseover(final Object handler) {
        setEventHandler(MouseEvent.TYPE_MOUSE_OVER, handler);
    }

    /**
     * Returns the {@code onmouseover} event handler for this element.
     * @return the {@code onmouseover} event handler for this element
     */
    @JsxGetter
    public Object getOnmouseover() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_OVER);
    }

    /**
     * Sets the {@code onmouseup} event handler for this element.
     * @param handler the {@code onmouseup} event handler for this element
     */
    @JsxSetter
    public void setOnmouseup(final Object handler) {
        setEventHandler(MouseEvent.TYPE_MOUSE_UP, handler);
    }

    /**
     * Returns the {@code onmouseup} event handler for this element.
     * @return the {@code onmouseup} event handler for this element
     */
    @JsxGetter
    public Object getOnmouseup() {
        return getEventHandler(MouseEvent.TYPE_MOUSE_UP);
    }

    /**
     * Sets the {@code oncontextmenu} event handler for this element.
     * @param handler the {@code oncontextmenu} event handler for this element
     */
    @JsxSetter
    public void setOncontextmenu(final Object handler) {
        setEventHandler(MouseEvent.TYPE_CONTEXT_MENU, handler);
    }

    /**
     * Returns the {@code oncontextmenu} event handler for this element.
     * @return the {@code oncontextmenu} event handler for this element
     */
    @JsxGetter
    public Object getOncontextmenu() {
        return getEventHandler(MouseEvent.TYPE_CONTEXT_MENU);
    }

    /**
     * Sets the {@code onresize} event handler for this element.
     * @param handler the {@code onresize} event handler for this element
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnresize(final Object handler) {
        setEventHandler(Event.TYPE_RESIZE, handler);
    }

    /**
     * Returns the {@code onresize} event handler for this element.
     * @return the {@code onresize} event handler for this element
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnresize() {
        return getEventHandler(Event.TYPE_RESIZE);
    }

    /**
     * Sets the {@code onerror} event handler for this element.
     * @param handler the {@code onerror} event handler for this element
     */
    @JsxSetter
    public void setOnerror(final Object handler) {
        setEventHandler(Event.TYPE_ERROR, handler);
    }

    /**
     * Returns the {@code onerror} event handler for this element.
     * @return the {@code onerror} event handler for this element
     */
    @JsxGetter
    public Object getOnerror() {
        return getEventHandler(Event.TYPE_ERROR);
    }

    /**
     * Returns the {@code oninput} event handler for this element.
     * @return the {@code oninput} event handler for this element
     */
    @JsxGetter
    public Function getOninput() {
        return getEventHandler(Event.TYPE_INPUT);
    }

    /**
     * Sets the {@code oninput} event handler for this element.
     * @param oninput the {@code oninput} event handler for this element
     */
    @JsxSetter
    public void setOninput(final Object oninput) {
        setEventHandler(Event.TYPE_INPUT, oninput);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxFunction(IE)
    public boolean contains(final Object element) {
        return super.contains(element);
    }

    /**
     * Returns the {@code hidden} property.
     * @return the {@code hidden} property
     */
    @JsxGetter
    public boolean isHidden() {
        return getDomNodeOrDie().isHidden();
    }

    /**
     * Sets the {@code hidden} property.
     * @param hidden the {@code hidden} value
     */
    @JsxSetter
    public void setHidden(final boolean hidden) {
        if (hidden) {
            getDomNodeOrDie().setAttribute("hidden", "hidden");
        }
        else {
            getDomNodeOrDie().removeAttribute("hidden");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxGetter(IE)
    public String getId() {
        return super.getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxSetter(IE)
    public void setId(final String newId) {
        super.setId(newId);
    }

    /**
     * Returns the {@code onabort} event handler for this element.
     * @return the {@code onabort} event handler for this element
     */
    @JsxGetter
    public Function getOnabort() {
        return getEventHandler(Event.TYPE_ABORT);
    }

    /**
     * Sets the {@code onabort} event handler for this element.
     * @param onabort the {@code onabort} event handler for this element
     */
    @JsxSetter
    public void setOnabort(final Object onabort) {
        setEventHandler(Event.TYPE_ABORT, onabort);
    }

    /**
     * Returns the {@code onauxclick} event handler for this element.
     * @return the {@code onauxclick} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnauxclick() {
        return getEventHandler(Event.TYPE_AUXCLICK);
    }

    /**
     * Sets the {@code onauxclick} event handler for this element.
     * @param onauxclick the {@code onauxclick} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnauxclick(final Object onauxclick) {
        setEventHandler(Event.TYPE_AUXCLICK, onauxclick);
    }

    /**
     * Returns the {@code oncancel} event handler for this element.
     * @return the {@code oncancel} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOncancel() {
        return getEventHandler(Event.TYPE_CANCEL);
    }

    /**
     * Sets the {@code oncancel} event handler for this element.
     * @param oncancel the {@code oncancel} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOncancel(final Object oncancel) {
        setEventHandler(Event.TYPE_CANCEL, oncancel);
    }

    /**
     * Returns the {@code oncanplay} event handler for this element.
     * @return the {@code oncanplay} event handler for this element
     */
    @JsxGetter
    public Function getOncanplay() {
        return getEventHandler(Event.TYPE_CANPLAY);
    }

    /**
     * Sets the {@code oncanplay} event handler for this element.
     * @param oncanplay the {@code oncanplay} event handler for this element
     */
    @JsxSetter
    public void setOncanplay(final Object oncanplay) {
        setEventHandler(Event.TYPE_CANPLAY, oncanplay);
    }

    /**
     * Returns the {@code oncanplaythrough} event handler for this element.
     * @return the {@code oncanplaythrough} event handler for this element
     */
    @JsxGetter
    public Function getOncanplaythrough() {
        return getEventHandler(Event.TYPE_CANPLAYTHROUGH);
    }

    /**
     * Sets the {@code oncanplaythrough} event handler for this element.
     * @param oncanplaythrough the {@code oncanplaythrough} event handler for this element
     */
    @JsxSetter
    public void setOncanplaythrough(final Object oncanplaythrough) {
        setEventHandler(Event.TYPE_CANPLAYTHROUGH, oncanplaythrough);
    }

    /**
     * Returns the {@code onclose} event handler for this element.
     * @return the {@code onclose} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnclose() {
        return getEventHandler(Event.TYPE_CLOSE);
    }

    /**
     * Sets the {@code onclose} event handler for this element.
     * @param onclose the {@code onclose} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnclose(final Object onclose) {
        setEventHandler(Event.TYPE_CLOSE, onclose);
    }

    /**
     * Returns the {@code oncuechange} event handler for this element.
     * @return the {@code oncuechange} event handler for this element
     */
    @JsxGetter
    public Function getOncuechange() {
        return getEventHandler(Event.TYPE_CUECHANGE);
    }

    /**
     * Sets the {@code oncuechange} event handler for this element.
     * @param oncuechange the {@code oncuechange} event handler for this element
     */
    @JsxSetter
    public void setOncuechange(final Object oncuechange) {
        setEventHandler(Event.TYPE_CUECHANGE, oncuechange);
    }

    /**
     * Returns the {@code ondrag} event handler for this element.
     * @return the {@code ondrag} event handler for this element
     */
    @JsxGetter
    public Function getOndrag() {
        return getEventHandler(Event.TYPE_DRAG);
    }

    /**
     * Sets the {@code ondrag} event handler for this element.
     * @param ondrag the {@code ondrag} event handler for this element
     */
    @JsxSetter
    public void setOndrag(final Object ondrag) {
        setEventHandler(Event.TYPE_DRAG, ondrag);
    }

    /**
     * Returns the {@code ondragend} event handler for this element.
     * @return the {@code ondragend} event handler for this element
     */
    @JsxGetter
    public Function getOndragend() {
        return getEventHandler(Event.TYPE_DRAGEND);
    }

    /**
     * Sets the {@code ondragend} event handler for this element.
     * @param ondragend the {@code ondragend} event handler for this element
     */
    @JsxSetter
    public void setOndragend(final Object ondragend) {
        setEventHandler(Event.TYPE_DRAGEND, ondragend);
    }

    /**
     * Returns the {@code ondragenter} event handler for this element.
     * @return the {@code ondragenter} event handler for this element
     */
    @JsxGetter
    public Function getOndragenter() {
        return getEventHandler(Event.TYPE_DRAGENTER);
    }

    /**
     * Sets the {@code ondragenter} event handler for this element.
     * @param ondragenter the {@code ondragenter} event handler for this element
     */
    @JsxSetter
    public void setOndragenter(final Object ondragenter) {
        setEventHandler(Event.TYPE_DRAGENTER, ondragenter);
    }

    /**
     * Returns the {@code ondragleave} event handler for this element.
     * @return the {@code ondragleave} event handler for this element
     */
    @JsxGetter
    public Function getOndragleave() {
        return getEventHandler(Event.TYPE_DRAGLEAVE);
    }

    /**
     * Sets the {@code ondragleave} event handler for this element.
     * @param ondragleave the {@code ondragleave} event handler for this element
     */
    @JsxSetter
    public void setOndragleave(final Object ondragleave) {
        setEventHandler(Event.TYPE_DRAGLEAVE, ondragleave);
    }

    /**
     * Returns the {@code ondragover} event handler for this element.
     * @return the {@code ondragover} event handler for this element
     */
    @JsxGetter
    public Function getOndragover() {
        return getEventHandler(Event.TYPE_DRAGOVER);
    }

    /**
     * Sets the {@code ondragover} event handler for this element.
     * @param ondragover the {@code ondragover} event handler for this element
     */
    @JsxSetter
    public void setOndragover(final Object ondragover) {
        setEventHandler(Event.TYPE_DRAGOVER, ondragover);
    }

    /**
     * Returns the {@code ondragstart} event handler for this element.
     * @return the {@code ondragstart} event handler for this element
     */
    @JsxGetter
    public Function getOndragstart() {
        return getEventHandler(Event.TYPE_DRAGSTART);
    }

    /**
     * Sets the {@code ondragstart} event handler for this element.
     * @param ondragstart the {@code ondragstart} event handler for this element
     */
    @JsxSetter
    public void setOndragstart(final Object ondragstart) {
        setEventHandler(Event.TYPE_DRAGSTART, ondragstart);
    }

    /**
     * Returns the {@code ondrop} event handler for this element.
     * @return the {@code ondrop} event handler for this element
     */
    @JsxGetter
    public Function getOndrop() {
        return getEventHandler(Event.TYPE_DROP);
    }

    /**
     * Sets the {@code ondrop} event handler for this element.
     * @param ondrop the {@code ondrop} event handler for this element
     */
    @JsxSetter
    public void setOndrop(final Object ondrop) {
        setEventHandler(Event.TYPE_DROP, ondrop);
    }

    /**
     * Returns the {@code ondurationchange} event handler for this element.
     * @return the {@code ondurationchange} event handler for this element
     */
    @JsxGetter
    public Function getOndurationchange() {
        return getEventHandler(Event.TYPE_DURATIONCHANGE);
    }

    /**
     * Sets the {@code ondurationchange} event handler for this element.
     * @param ondurationchange the {@code ondurationchange} event handler for this element
     */
    @JsxSetter
    public void setOndurationchange(final Object ondurationchange) {
        setEventHandler(Event.TYPE_DURATIONCHANGE, ondurationchange);
    }

    /**
     * Returns the {@code onemptied} event handler for this element.
     * @return the {@code onemptied} event handler for this element
     */
    @JsxGetter
    public Function getOnemptied() {
        return getEventHandler(Event.TYPE_EMPTIED);
    }

    /**
     * Sets the {@code onemptied} event handler for this element.
     * @param onemptied the {@code onemptied} event handler for this element
     */
    @JsxSetter
    public void setOnemptied(final Object onemptied) {
        setEventHandler(Event.TYPE_EMPTIED, onemptied);
    }

    /**
     * Returns the {@code onended} event handler for this element.
     * @return the {@code onended} event handler for this element
     */
    @JsxGetter
    public Function getOnended() {
        return getEventHandler(Event.TYPE_ENDED);
    }

    /**
     * Sets the {@code onended} event handler for this element.
     * @param onended the {@code onended} event handler for this element
     */
    @JsxSetter
    public void setOnended(final Object onended) {
        setEventHandler(Event.TYPE_ENDED, onended);
    }

    /**
     * Returns the {@code ongotpointercapture} event handler for this element.
     * @return the {@code ongotpointercapture} event handler for this element
     */
    @Override
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOngotpointercapture() {
        return getEventHandler(Event.TYPE_GOTPOINTERCAPTURE);
    }

    /**
     * Sets the {@code ongotpointercapture} event handler for this element.
     * @param ongotpointercapture the {@code ongotpointercapture} event handler for this element
     */
    @Override
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOngotpointercapture(final Object ongotpointercapture) {
        setEventHandler(Event.TYPE_GOTPOINTERCAPTURE, ongotpointercapture);
    }

    /**
     * Returns the {@code oninvalid} event handler for this element.
     * @return the {@code oninvalid} event handler for this element
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOninvalid() {
        return getEventHandler(Event.TYPE_INVALID);
    }

    /**
     * Sets the {@code oninvalid} event handler for this element.
     * @param oninvalid the {@code oninvalid} event handler for this element
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOninvalid(final Object oninvalid) {
        setEventHandler(Event.TYPE_INVALID, oninvalid);
    }

    /**
     * Returns the {@code onload} event handler for this element.
     * @return the {@code onload} event handler for this element
     */
    @JsxGetter
    public Object getOnload() {
        if (this instanceof HTMLBodyElement) {
            return getWindow().getEventHandler(Event.TYPE_LOAD);
        }

        return getEventHandler(Event.TYPE_LOAD);
    }

    /**
     * Sets the {@code onload} event handler for this element.
     * @param onload the {@code onload} event handler for this element
     */
    @JsxSetter
    public void setOnload(final Object onload) {
        if (this instanceof HTMLBodyElement) {
            getWindow().setEventHandler(Event.TYPE_LOAD, onload);
            return;
        }

        setEventHandler(Event.TYPE_LOAD, onload);
    }

    /**
     * Returns the {@code onloadeddata} event handler for this element.
     * @return the {@code onloadeddata} event handler for this element
     */
    @JsxGetter
    public Function getOnloadeddata() {
        return getEventHandler(Event.TYPE_LOADEDDATA);
    }

    /**
     * Sets the {@code onloadeddata} event handler for this element.
     * @param onloadeddata the {@code onloadeddata} event handler for this element
     */
    @JsxSetter
    public void setOnloadeddata(final Object onloadeddata) {
        setEventHandler(Event.TYPE_LOADEDDATA, onloadeddata);
    }

    /**
     * Returns the {@code onloadedmetadata} event handler for this element.
     * @return the {@code onloadedmetadata} event handler for this element
     */
    @JsxGetter
    public Function getOnloadedmetadata() {
        return getEventHandler(Event.TYPE_LOADEDMETADATA);
    }

    /**
     * Sets the {@code onloadedmetadata} event handler for this element.
     * @param onloadedmetadata the {@code onloadedmetadata} event handler for this element
     */
    @JsxSetter
    public void setOnloadedmetadata(final Object onloadedmetadata) {
        setEventHandler(Event.TYPE_LOADEDMETADATA, onloadedmetadata);
    }

    /**
     * Returns the {@code onloadstart} event handler for this element.
     * @return the {@code onloadstart} event handler for this element
     */
    @JsxGetter
    public Function getOnloadstart() {
        return getEventHandler(Event.TYPE_LOAD_START);
    }

    /**
     * Sets the {@code onloadstart} event handler for this element.
     * @param onloadstart the {@code onloadstart} event handler for this element
     */
    @JsxSetter
    public void setOnloadstart(final Object onloadstart) {
        setEventHandler(Event.TYPE_LOAD_START, onloadstart);
    }

    /**
     * Returns the {@code onlostpointercapture} event handler for this element.
     * @return the {@code onlostpointercapture} event handler for this element
     */
    @Override
    @JsxGetter({CHROME, EDGE})
    public Function getOnlostpointercapture() {
        return getEventHandler(Event.TYPE_LOSTPOINTERCAPTURE);
    }

    /**
     * Sets the {@code onlostpointercapture} event handler for this element.
     * @param onlostpointercapture the {@code onlostpointercapture} event handler for this element
     */
    @Override
    @JsxSetter({CHROME, EDGE})
    public void setOnlostpointercapture(final Object onlostpointercapture) {
        setEventHandler(Event.TYPE_LOSTPOINTERCAPTURE, onlostpointercapture);
    }

    /**
     * Returns the {@code onmouseenter} event handler for this element.
     * @return the {@code onmouseenter} event handler for this element
     */
    @JsxGetter
    public Function getOnmouseenter() {
        return getEventHandler(Event.TYPE_MOUDEENTER);
    }

    /**
     * Sets the {@code onmouseenter} event handler for this element.
     * @param onmouseenter the {@code onmouseenter} event handler for this element
     */
    @JsxSetter
    public void setOnmouseenter(final Object onmouseenter) {
        setEventHandler(Event.TYPE_MOUDEENTER, onmouseenter);
    }

    /**
     * Returns the {@code onmouseleave} event handler for this element.
     * @return the {@code onmouseleave} event handler for this element
     */
    @JsxGetter
    public Function getOnmouseleave() {
        return getEventHandler(Event.TYPE_MOUSELEAVE);
    }

    /**
     * Sets the {@code onmouseleave} event handler for this element.
     * @param onmouseleave the {@code onmouseleave} event handler for this element
     */
    @JsxSetter
    public void setOnmouseleave(final Object onmouseleave) {
        setEventHandler(Event.TYPE_MOUSELEAVE, onmouseleave);
    }

    /**
     * Returns the {@code onmousewheel} event handler for this element.
     * @return the {@code onmousewheel} event handler for this element
     */
    @JsxGetter({CHROME, EDGE, IE})
    public Function getOnmousewheel() {
        return getEventHandler(Event.TYPE_MOUSEWHEEL);
    }

    /**
     * Sets the {@code onmousewheel} event handler for this element.
     * @param onmousewheel the {@code onmousewheel} event handler for this element
     */
    @JsxSetter({CHROME, EDGE, IE})
    public void setOnmousewheel(final Object onmousewheel) {
        setEventHandler(Event.TYPE_MOUSEWHEEL, onmousewheel);
    }

    /**
     * Returns the {@code onpause} event handler for this element.
     * @return the {@code onpause} event handler for this element
     */
    @JsxGetter
    public Function getOnpause() {
        return getEventHandler(Event.TYPE_PAUSE);
    }

    /**
     * Sets the {@code onpause} event handler for this element.
     * @param onpause the {@code onpause} event handler for this element
     */
    @JsxSetter
    public void setOnpause(final Object onpause) {
        setEventHandler(Event.TYPE_PAUSE, onpause);
    }

    /**
     * Returns the {@code onplay} event handler for this element.
     * @return the {@code onplay} event handler for this element
     */
    @JsxGetter
    public Function getOnplay() {
        return getEventHandler(Event.TYPE_PLAY);
    }

    /**
     * Sets the {@code onplay} event handler for this element.
     * @param onplay the {@code onplay} event handler for this element
     */
    @JsxSetter
    public void setOnplay(final Object onplay) {
        setEventHandler(Event.TYPE_PLAY, onplay);
    }

    /**
     * Returns the {@code onplaying} event handler for this element.
     * @return the {@code onplaying} event handler for this element
     */
    @JsxGetter
    public Function getOnplaying() {
        return getEventHandler(Event.TYPE_PLAYNG);
    }

    /**
     * Sets the {@code onplaying} event handler for this element.
     * @param onplaying the {@code onplaying} event handler for this element
     */
    @JsxSetter
    public void setOnplaying(final Object onplaying) {
        setEventHandler(Event.TYPE_PLAYNG, onplaying);
    }

    /**
     * Returns the {@code onpointercancel} event handler for this element.
     * @return the {@code onpointercancel} event handler for this element
     */
    @Override
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnpointercancel() {
        return getEventHandler(Event.TYPE_POINTERCANCEL);
    }

    /**
     * Sets the {@code onpointercancel} event handler for this element.
     * @param onpointercancel the {@code onpointercancel} event handler for this element
     */
    @Override
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnpointercancel(final Object onpointercancel) {
        setEventHandler(Event.TYPE_POINTERCANCEL, onpointercancel);
    }

    /**
     * Returns the {@code onpointerdown} event handler for this element.
     * @return the {@code onpointerdown} event handler for this element
     */
    @Override
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnpointerdown() {
        return getEventHandler(Event.TYPE_POINTERDOWN);
    }

    /**
     * Sets the {@code onpointerdown} event handler for this element.
     * @param onpointerdown the {@code onpointerdown} event handler for this element
     */
    @Override
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnpointerdown(final Object onpointerdown) {
        setEventHandler(Event.TYPE_POINTERDOWN, onpointerdown);
    }

    /**
     * Returns the {@code onpointerenter} event handler for this element.
     * @return the {@code onpointerenter} event handler for this element
     */
    @Override
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnpointerenter() {
        return getEventHandler(Event.TYPE_POINTERENTER);
    }

    /**
     * Sets the {@code onpointerenter} event handler for this element.
     * @param onpointerenter the {@code onpointerenter} event handler for this element
     */
    @Override
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnpointerenter(final Object onpointerenter) {
        setEventHandler(Event.TYPE_POINTERENTER, onpointerenter);
    }

    /**
     * Returns the {@code onpointerleave} event handler for this element.
     * @return the {@code onpointerleave} event handler for this element
     */
    @Override
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnpointerleave() {
        return getEventHandler(Event.TYPE_POINTERLEAVE);
    }

    /**
     * Sets the {@code onpointerleave} event handler for this element.
     * @param onpointerleave the {@code onpointerleave} event handler for this element
     */
    @Override
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnpointerleave(final Object onpointerleave) {
        setEventHandler(Event.TYPE_POINTERLEAVE, onpointerleave);
    }

    /**
     * Returns the {@code onpointermove} event handler for this element.
     * @return the {@code onpointermove} event handler for this element
     */
    @Override
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnpointermove() {
        return getEventHandler(Event.TYPE_POINTERMOVE);
    }

    /**
     * Sets the {@code onpointermove} event handler for this element.
     * @param onpointermove the {@code onpointermove} event handler for this element
     */
    @Override
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnpointermove(final Object onpointermove) {
        setEventHandler(Event.TYPE_POINTERMOVE, onpointermove);
    }

    /**
     * Returns the {@code onpointerout} event handler for this element.
     * @return the {@code onpointerout} event handler for this element
     */
    @Override
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnpointerout() {
        return getEventHandler(Event.TYPE_POINTEROUT);
    }

    /**
     * Sets the {@code onpointerout} event handler for this element.
     * @param onpointerout the {@code onpointerout} event handler for this element
     */
    @Override
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnpointerout(final Object onpointerout) {
        setEventHandler(Event.TYPE_POINTEROUT, onpointerout);
    }

    /**
     * Returns the {@code onpointerover} event handler for this element.
     * @return the {@code onpointerover} event handler for this element
     */
    @Override
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnpointerover() {
        return getEventHandler(Event.TYPE_POINTEROVER);
    }

    /**
     * Sets the {@code onpointerover} event handler for this element.
     * @param onpointerover the {@code onpointerover} event handler for this element
     */
    @Override
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnpointerover(final Object onpointerover) {
        setEventHandler(Event.TYPE_POINTEROVER, onpointerover);
    }

    /**
     * Returns the {@code onpointerup} event handler for this element.
     * @return the {@code onpointerup} event handler for this element
     */
    @Override
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnpointerup() {
        return getEventHandler(Event.TYPE_POINTERUP);
    }

    /**
     * Sets the {@code onpointerup} event handler for this element.
     * @param onpointerup the {@code onpointerup} event handler for this element
     */
    @Override
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnpointerup(final Object onpointerup) {
        setEventHandler(Event.TYPE_POINTERUP, onpointerup);
    }

    /**
     * Returns the {@code onprogress} event handler for this element.
     * @return the {@code onprogress} event handler for this element
     */
    @JsxGetter
    public Function getOnprogress() {
        return getEventHandler(Event.TYPE_PROGRESS);
    }

    /**
     * Sets the {@code onprogress} event handler for this element.
     * @param onprogress the {@code onprogress} event handler for this element
     */
    @JsxSetter
    public void setOnprogress(final Object onprogress) {
        setEventHandler(Event.TYPE_PROGRESS, onprogress);
    }

    /**
     * Returns the {@code onratechange} event handler for this element.
     * @return the {@code onratechange} event handler for this element
     */
    @JsxGetter
    public Function getOnratechange() {
        return getEventHandler(Event.TYPE_RATECHANGE);
    }

    /**
     * Sets the {@code onratechange} event handler for this element.
     * @param onratechange the {@code onratechange} event handler for this element
     */
    @JsxSetter
    public void setOnratechange(final Object onratechange) {
        setEventHandler(Event.TYPE_RATECHANGE, onratechange);
    }

    /**
     * Returns the {@code onreset} event handler for this element.
     * @return the {@code onreset} event handler for this element
     */
    @JsxGetter
    public Function getOnreset() {
        return getEventHandler(Event.TYPE_RESET);
    }

    /**
     * Sets the {@code onreset} event handler for this element.
     * @param onreset the {@code onreset} event handler for this element
     */
    @JsxSetter
    public void setOnreset(final Object onreset) {
        setEventHandler(Event.TYPE_RESET, onreset);
    }

    /**
     * Returns the {@code onscroll} event handler for this element.
     * @return the {@code onscroll} event handler for this element
     */
    @JsxGetter
    public Function getOnscroll() {
        return getEventHandler(Event.TYPE_SCROLL);
    }

    /**
     * Sets the {@code onscroll} event handler for this element.
     * @param onscroll the {@code onscroll} event handler for this element
     */
    @JsxSetter
    public void setOnscroll(final Object onscroll) {
        setEventHandler(Event.TYPE_SCROLL, onscroll);
    }

    /**
     * Returns the {@code onseeked} event handler for this element.
     * @return the {@code onseeked} event handler for this element
     */
    @JsxGetter
    public Function getOnseeked() {
        return getEventHandler(Event.TYPE_SEEKED);
    }

    /**
     * Sets the {@code onseeked} event handler for this element.
     * @param onseeked the {@code onseeked} event handler for this element
     */
    @JsxSetter
    public void setOnseeked(final Object onseeked) {
        setEventHandler(Event.TYPE_SEEKED, onseeked);
    }

    /**
     * Returns the {@code onseeking} event handler for this element.
     * @return the {@code onseeking} event handler for this element
     */
    @JsxGetter
    public Function getOnseeking() {
        return getEventHandler(Event.TYPE_SEEKING);
    }

    /**
     * Sets the {@code onseeking} event handler for this element.
     * @param onseeking the {@code onseeking} event handler for this element
     */
    @JsxSetter
    public void setOnseeking(final Object onseeking) {
        setEventHandler(Event.TYPE_SEEKING, onseeking);
    }

    /**
     * Returns the {@code onselect} event handler for this element.
     * @return the {@code onselect} event handler for this element
     */
    @JsxGetter
    public Function getOnselect() {
        return getEventHandler(Event.TYPE_SELECT);
    }

    /**
     * Sets the {@code onselect} event handler for this element.
     * @param onselect the {@code onselect} event handler for this element
     */
    @JsxSetter
    public void setOnselect(final Object onselect) {
        setEventHandler(Event.TYPE_SELECT, onselect);
    }

    /**
     * Returns the {@code onshow} event handler for this element.
     * @return the {@code onshow} event handler for this element
     */
    @JsxGetter(FF78)
    public Function getOnshow() {
        return getEventHandler(Event.TYPE_SHOW);
    }

    /**
     * Sets the {@code onshow} event handler for this element.
     * @param onshow the {@code onshow} event handler for this element
     */
    @JsxSetter(FF78)
    public void setOnshow(final Object onshow) {
        setEventHandler(Event.TYPE_SHOW, onshow);
    }

    /**
     * Returns the {@code onstalled} event handler for this element.
     * @return the {@code onstalled} event handler for this element
     */
    @JsxGetter
    public Function getOnstalled() {
        return getEventHandler(Event.TYPE_STALLED);
    }

    /**
     * Sets the {@code onstalled} event handler for this element.
     * @param onstalled the {@code onstalled} event handler for this element
     */
    @JsxSetter
    public void setOnstalled(final Object onstalled) {
        setEventHandler(Event.TYPE_STALLED, onstalled);
    }

    /**
     * Returns the {@code onsuspend} event handler for this element.
     * @return the {@code onsuspend} event handler for this element
     */
    @JsxGetter
    public Function getOnsuspend() {
        return getEventHandler(Event.TYPE_SUSPEND);
    }

    /**
     * Sets the {@code onsuspend} event handler for this element.
     * @param onsuspend the {@code onsuspend} event handler for this element
     */
    @JsxSetter
    public void setOnsuspend(final Object onsuspend) {
        setEventHandler(Event.TYPE_SUSPEND, onsuspend);
    }

    /**
     * Returns the {@code ontimeupdate} event handler for this element.
     * @return the {@code ontimeupdate} event handler for this element
     */
    @JsxGetter
    public Function getOntimeupdate() {
        return getEventHandler(Event.TYPE_TIMEUPDATE);
    }

    /**
     * Sets the {@code ontimeupdate} event handler for this element.
     * @param ontimeupdate the {@code ontimeupdate} event handler for this element
     */
    @JsxSetter
    public void setOntimeupdate(final Object ontimeupdate) {
        setEventHandler(Event.TYPE_TIMEUPDATE, ontimeupdate);
    }

    /**
     * Returns the {@code ontoggle} event handler for this element.
     * @return the {@code ontoggle} event handler for this element
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOntoggle() {
        return getEventHandler(Event.TYPE_TOGGLE);
    }

    /**
     * Sets the {@code ontoggle} event handler for this element.
     * @param ontoggle the {@code ontoggle} event handler for this element
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOntoggle(final Object ontoggle) {
        setEventHandler(Event.TYPE_TOGGLE, ontoggle);
    }

    /**
     * Returns the {@code onvolumechange} event handler for this element.
     * @return the {@code onvolumechange} event handler for this element
     */
    @JsxGetter
    public Function getOnvolumechange() {
        return getEventHandler(Event.TYPE_VOLUMECHANGE);
    }

    /**
     * Sets the {@code onvolumechange} event handler for this element.
     * @param onvolumechange the {@code onvolumechange} event handler for this element
     */
    @JsxSetter
    public void setOnvolumechange(final Object onvolumechange) {
        setEventHandler(Event.TYPE_VOLUMECHANGE, onvolumechange);
    }

    /**
     * Returns the {@code onwaiting} event handler for this element.
     * @return the {@code onwaiting} event handler for this element
     */
    @JsxGetter
    public Function getOnwaiting() {
        return getEventHandler(Event.TYPE_WAITING);
    }

    /**
     * Sets the {@code onwaiting} event handler for this element.
     * @param onwaiting the {@code onwaiting} event handler for this element
     */
    @JsxSetter
    public void setOnwaiting(final Object onwaiting) {
        setEventHandler(Event.TYPE_WAITING, onwaiting);
    }

    /**
     * Returns the {@code oncopy} event handler for this element.
     * @return the {@code oncopy} event handler for this element
     */
    @JsxGetter
    public Function getOncopy() {
        return getEventHandler(Event.TYPE_COPY);
    }

    /**
     * Sets the {@code oncopy} event handler for this element.
     * @param oncopy the {@code oncopy} event handler for this element
     */
    @JsxSetter
    public void setOncopy(final Object oncopy) {
        setEventHandler(Event.TYPE_COPY, oncopy);
    }

    /**
     * Returns the {@code oncut} event handler for this element.
     * @return the {@code oncut} event handler for this element
     */
    @JsxGetter
    public Function getOncut() {
        return getEventHandler(Event.TYPE_CUT);
    }

    /**
     * Sets the {@code oncut} event handler for this element.
     * @param oncut the {@code oncut} event handler for this element
     */
    @JsxSetter
    public void setOncut(final Object oncut) {
        setEventHandler(Event.TYPE_CUT, oncut);
    }

    /**
     * Returns the {@code onpaste} event handler for this element.
     * @return the {@code onpaste} event handler for this element
     */
    @JsxGetter
    public Function getOnpaste() {
        return getEventHandler(Event.TYPE_PASTE);
    }

    /**
     * Sets the {@code onpaste} event handler for this element.
     * @param onpaste the {@code onpaste} event handler for this element
     */
    @JsxSetter
    public void setOnpaste(final Object onpaste) {
        setEventHandler(Event.TYPE_PASTE, onpaste);
    }

    /**
     * Returns the {@code onmozfullscreenchange} event handler for this element.
     * @return the {@code onmozfullscreenchange} event handler for this element
     */
    @JsxGetter({FF, FF78})
    public Function getOnmozfullscreenchange() {
        return getEventHandler(Event.TYPE_MOZFULLSCREENCHANGE);
    }

    /**
     * Sets the {@code onmozfullscreenchange} event handler for this element.
     * @param onmozfullscreenchange the {@code onmozfullscreenchange} event handler for this element
     */
    @JsxSetter({FF, FF78})
    public void setOnmozfullscreenchange(final Object onmozfullscreenchange) {
        setEventHandler(Event.TYPE_MOZFULLSCREENCHANGE, onmozfullscreenchange);
    }

    /**
     * Returns the {@code onmozfullscreenerror} event handler for this element.
     * @return the {@code onmozfullscreenerror} event handler for this element
     */
    @JsxGetter({FF, FF78})
    public Function getOnmozfullscreenerror() {
        return getEventHandler(Event.TYPE_MOZFULLSCREENERROR);
    }

    /**
     * Sets the {@code onmozfullscreenerror} event handler for this element.
     * @param onmozfullscreenerror the {@code onmozfullscreenerror} event handler for this element
     */
    @JsxSetter({FF, FF78})
    public void setOnmozfullscreenerror(final Object onmozfullscreenerror) {
        setEventHandler(Event.TYPE_MOZFULLSCREENERROR, onmozfullscreenerror);
    }

    /**
     * Returns the {@code onactivate} event handler for this element.
     * @return the {@code onactivate} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnactivate() {
        return getEventHandler(Event.TYPE_ACTIVATE);
    }

    /**
     * Sets the {@code onactivate} event handler for this element.
     * @param onactivate the {@code onactivate} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnactivate(final Object onactivate) {
        setEventHandler(Event.TYPE_ACTIVATE, onactivate);
    }

    /**
     * Returns the {@code onbeforeactivate} event handler for this element.
     * @return the {@code onbeforeactivate} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnbeforeactivate() {
        return getEventHandler(Event.TYPE_BEFOREACTIVATE);
    }

    /**
     * Sets the {@code onbeforeactivate} event handler for this element.
     * @param onbeforeactivate the {@code onbeforeactivate} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnbeforeactivate(final Object onbeforeactivate) {
        setEventHandler(Event.TYPE_BEFOREACTIVATE, onbeforeactivate);
    }

    /**
     * Returns the {@code onbeforecopy} event handler for this element.
     * @return the {@code onbeforecopy} event handler for this element
     */
    @Override
    @JsxGetter(IE)
    public Function getOnbeforecopy() {
        return getEventHandler(Event.TYPE_BEFORECOPY);
    }

    /**
     * Sets the {@code onbeforecopy} event handler for this element.
     * @param onbeforecopy the {@code onbeforecopy} event handler for this element
     */
    @Override
    @JsxSetter(IE)
    public void setOnbeforecopy(final Object onbeforecopy) {
        setEventHandler(Event.TYPE_BEFORECOPY, onbeforecopy);
    }

    /**
     * Returns the {@code onbeforecut} event handler for this element.
     * @return the {@code onbeforecut} event handler for this element
     */
    @Override
    @JsxGetter(IE)
    public Function getOnbeforecut() {
        return getEventHandler(Event.TYPE_BEFORECUT);
    }

    /**
     * Sets the {@code onbeforecut} event handler for this element.
     * @param onbeforecut the {@code onbeforecut} event handler for this element
     */
    @Override
    @JsxSetter(IE)
    public void setOnbeforecut(final Object onbeforecut) {
        setEventHandler(Event.TYPE_BEFORECUT, onbeforecut);
    }

    /**
     * Returns the {@code onbeforedeactivate} event handler for this element.
     * @return the {@code onbeforedeactivate} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnbeforedeactivate() {
        return getEventHandler(Event.TYPE_BEFOREDEACTIVATE);
    }

    /**
     * Sets the {@code onbeforedeactivate} event handler for this element.
     * @param onbeforedeactivate the {@code onbeforedeactivate} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnbeforedeactivate(final Object onbeforedeactivate) {
        setEventHandler(Event.TYPE_BEFOREDEACTIVATE, onbeforedeactivate);
    }

    /**
     * Returns the {@code onbeforepaste} event handler for this element.
     * @return the {@code onbeforepaste} event handler for this element
     */
    @Override
    @JsxGetter(IE)
    public Function getOnbeforepaste() {
        return getEventHandler(Event.TYPE_BEFOREPASTE);
    }

    /**
     * Sets the {@code onbeforepaste} event handler for this element.
     * @param onbeforepaste the {@code onbeforepaste} event handler for this element
     */
    @Override
    @JsxSetter(IE)
    public void setOnbeforepaste(final Object onbeforepaste) {
        setEventHandler(Event.TYPE_BEFOREPASTE, onbeforepaste);
    }

    /**
     * Returns the {@code ondeactivate} event handler for this element.
     * @return the {@code ondeactivate} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOndeactivate() {
        return getEventHandler(Event.TYPE_DEACTIVATE);
    }

    /**
     * Sets the {@code ondeactivate} event handler for this element.
     * @param ondeactivate the {@code ondeactivate} event handler for this element
     */
    @JsxSetter(IE)
    public void setOndeactivate(final Object ondeactivate) {
        setEventHandler(Event.TYPE_DEACTIVATE, ondeactivate);
    }

    /**
     * Returns the {@code onhelp} event handler for this element.
     * @return the {@code onhelp} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnhelp() {
        return getEventHandler(Event.TYPE_HELP);
    }

    /**
     * Sets the {@code onhelp} event handler for this element.
     * @param onhelp the {@code onhelp} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnhelp(final Object onhelp) {
        setEventHandler(Event.TYPE_HELP, onhelp);
    }

    /**
     * Returns the {@code onmscontentzoom} event handler for this element.
     * @return the {@code onmscontentzoom} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnmscontentzoom() {
        return getEventHandler(Event.TYPE_MSCONTENTZOOM);
    }

    /**
     * Sets the {@code onmscontentzoom} event handler for this element.
     * @param onmscontentzoom the {@code onmscontentzoom} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnmscontentzoom(final Object onmscontentzoom) {
        setEventHandler(Event.TYPE_MSCONTENTZOOM, onmscontentzoom);
    }

    /**
     * Returns the {@code onmsmanipulationstatechanged} event handler for this element.
     * @return the {@code onmsmanipulationstatechanged} event handler for this element
     */
    @JsxGetter(IE)
    public Function getOnmsmanipulationstatechanged() {
        return getEventHandler(Event.TYPE_MSMANIPULATIONSTATECHANGED);
    }

    /**
     * Sets the {@code onmsmanipulationstatechanged} event handler for this element.
     * @param onmsmanipulationstatechanged the {@code onmsmanipulationstatechanged} event handler for this element
     */
    @JsxSetter(IE)
    public void setOnmsmanipulationstatechanged(final Object onmsmanipulationstatechanged) {
        setEventHandler(Event.TYPE_MSMANIPULATIONSTATECHANGED, onmsmanipulationstatechanged);
    }

    /**
     * Returns the {@code onselectstart} event handler for this element.
     * @return the {@code onselectstart} event handler for this element
     */
    @JsxGetter({CHROME, EDGE, IE})
    public Function getOnselectstart() {
        return getEventHandler(Event.TYPE_SELECTSTART);
    }

    /**
     * Sets the {@code onselectstart} event handler for this element.
     * @param onselectstart the {@code onselectstart} event handler for this element
     */
    @JsxSetter({CHROME, EDGE, IE})
    public void setOnselectstart(final Object onselectstart) {
        setEventHandler(Event.TYPE_SELECTSTART, onselectstart);
    }

    /**
     * Returns the value of the JavaScript attribute {@code name}.
     *
     * @return the value of this attribute
     */
    public String getName() {
        return getDomNodeOrDie().getAttributeDirect("name");
    }

    /**
     * Sets the value of the JavaScript attribute {@code name}.
     *
     * @param newName the new name
     */
    public void setName(final String newName) {
        getDomNodeOrDie().setAttribute("name", newName);
    }

    /**
     * Returns the value of the JavaScript attribute {@code value}.
     *
     * @return the value of this attribute
     */
    public Object getValue() {
        return getDomNodeOrDie().getAttributeDirect("value");
    }

    /**
     * Sets the value of the JavaScript attribute {@code value}.
     *
     * @param newValue the new value
     */
    public void setValue(final Object newValue) {
        getDomNodeOrDie().setAttribute("value", Context.toString(newValue));
    }

    /**
     * Returns the {@code onanimationcancel} event handler.
     * @return the {@code onanimationcancel} event handler
     */
    @JsxGetter({FF, FF78})
    public Function getOnanimationcancel() {
        return getEventHandler(Event.TYPE_ANIMATIONCANCEL);
    }

    /**
     * Sets the {@code onanimationcancel} event handler.
     * @param onanimationcancel the {@code onanimationcancel} event handler
     */
    @JsxSetter({FF, FF78})
    public void setOnanimationcancel(final Object onanimationcancel) {
        setEventHandler(Event.TYPE_ANIMATIONCANCEL, onanimationcancel);
    }

    /**
     * Returns the {@code onanimationend} event handler.
     * @return the {@code onanimationend} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnanimationend() {
        return getEventHandler(Event.TYPE_ANIMATIONEND);
    }

    /**
     * Sets the {@code onanimationend} event handler.
     * @param onanimationend the {@code onanimationend} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnanimationend(final Object onanimationend) {
        setEventHandler(Event.TYPE_ANIMATIONEND, onanimationend);
    }

    /**
     * Returns the {@code onanimationiteration} event handler.
     * @return the {@code onanimationiteration} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnanimationiteration() {
        return getEventHandler(Event.TYPE_ANIMATIONITERATION);
    }

    /**
     * Sets the {@code onanimationiteration} event handler.
     * @param onanimationiteration the {@code onanimationiteration} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnanimationiteration(final Object onanimationiteration) {
        setEventHandler(Event.TYPE_ANIMATIONITERATION, onanimationiteration);
    }

    /**
     * Returns the {@code onanimationstart} event handler.
     * @return the {@code onanimationstart} event handler
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOnanimationstart() {
        return getEventHandler(Event.TYPE_ANIMATIONSTART);
    }

    /**
     * Sets the {@code onanimationstart} event handler.
     * @param onanimationstart the {@code onanimationstart} event handler
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOnanimationstart(final Object onanimationstart) {
        setEventHandler(Event.TYPE_ANIMATIONSTART, onanimationstart);
    }

    /**
     * Returns the {@code onselectionchange} event handler for this element.
     * @return the {@code onselectionchange} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnselectionchange() {
        return getEventHandler(Event.TYPE_SELECTIONCHANGE);
    }

    /**
     * Sets the {@code onselectionchange} event handler for this element.
     * @param onselectionchange the {@code onselectionchange} event handler for this element
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnselectionchange(final Object onselectionchange) {
        setEventHandler(Event.TYPE_SELECTIONCHANGE, onselectionchange);
    }

    /**
     * Returns the {@code ontransitioncancel} event handler for this element.
     * @return the {@code ontransitioncancel} event handler for this element
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOntransitioncancel() {
        return getEventHandler(Event.TYPE_ONTRANSITIONCANCEL);
    }

    /**
     * Sets the {@code ontransitioncancel} event handler for this element.
     * @param ontransitioncancel the {@code ontransitioncancel} event handler for this element
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOntransitioncancel(final Object ontransitioncancel) {
        setEventHandler(Event.TYPE_ONTRANSITIONCANCEL, ontransitioncancel);
    }

    /**
     * Returns the {@code ontransitionend} event handler for this element.
     * @return the {@code ontransitionend} event handler for this element
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOntransitionend() {
        return getEventHandler(Event.TYPE_ONTRANSITIONEND);
    }

    /**
     * Sets the {@code ontransitionend} event handler for this element.
     * @param ontransitionend the {@code ontransitionend} event handler for this element
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOntransitionend(final Object ontransitionend) {
        setEventHandler(Event.TYPE_ONTRANSITIONEND, ontransitionend);
    }

    /**
     * Returns the {@code ontransitionrun} event handler for this element.
     * @return the {@code ontransitionrun} event handler for this element
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOntransitionrun() {
        return getEventHandler(Event.TYPE_ONTRANSITIONRUN);
    }

    /**
     * Sets the {@code ontransitionrun} event handler for this element.
     * @param ontransitionrun the {@code ontransitionrun} event handler for this element
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOntransitionrun(final Object ontransitionrun) {
        setEventHandler(Event.TYPE_ONTRANSITIONRUN, ontransitionrun);
    }

    /**
     * Returns the {@code ontransitionstart} event handler for this element.
     * @return the {@code ontransitionstart} event handler for this element
     */
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public Function getOntransitionstart() {
        return getEventHandler(Event.TYPE_ONTRANSITIONSTART);
    }

    /**
     * Sets the {@code ontransitionstart} event handler for this element.
     * @param ontransitionstart the {@code ontransitionstart} event handler for this element
     */
    @JsxSetter({CHROME, EDGE, FF, FF78})
    public void setOntransitionstart(final Object ontransitionstart) {
        setEventHandler(Event.TYPE_ONTRANSITIONSTART, ontransitionstart);
    }
}
