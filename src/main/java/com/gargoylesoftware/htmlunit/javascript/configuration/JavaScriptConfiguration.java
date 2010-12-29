/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import com.gargoylesoftware.htmlunit.BrowserVersion;
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
import com.gargoylesoftware.htmlunit.html.HtmlElement;
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
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.StrictErrorHandler;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDivElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLHeadingElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLQuoteElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLSpanElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableColElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableSectionElement;

/**
 * A container for all the JavaScript configuration information.
 * TODO - Need to add the logic to support the browser and JavaScript conditionals in the Class elements.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public final class JavaScriptConfiguration {

    private static final Log LOG = LogFactory.getLog(JavaScriptConfiguration.class);

    /** The JavaScript configuration XML document. */
    private static Document XmlDocument_;

    /** Constant indicating that this function/property is used by the specified browser version. */
    public static final int ENABLED   = 1;

    /** Constant indicating that this function/property is not used by the specified browser version. */
    public static final int DISABLED  = 2;

    /** Constant indicating that this function/property is not defined in the configuration file. */
    public static final int NOT_FOUND = 3;

    /** Cache of browser versions and their corresponding JavaScript configurations. */
    private static Map<BrowserVersion, JavaScriptConfiguration> ConfigurationMap_ =
        new WeakHashMap<BrowserVersion, JavaScriptConfiguration>(11);

    private static Map<String, String> ClassnameMap_ = new HashMap<String, String>();

    private Map<Class < ? extends HtmlElement>, Class < ? extends SimpleScriptable>> htmlJavaScriptMap_;

    private final Map<String, ClassConfiguration> configuration_;

    /**
     * Constructor is only called from {@link #getInstance(BrowserVersion)} which is synchronized.
     * @param browser the browser version to use
     */
    private JavaScriptConfiguration(final BrowserVersion browser) {
        if (XmlDocument_ == null) {
            loadConfiguration();
        }
        if (XmlDocument_ == null) {
            throw new IllegalStateException("Configuration was not initialized - see log for details");
        }
        configuration_ = buildUsageMap(browser);
    }

    /**
     * Test for a configuration having been loaded for testing.
     *
     * @return boolean - true if the XmlDocument has been loaded;
     */
    protected static boolean isDocumentLoaded() {
        return XmlDocument_ != null;
    }

    /**
     * Resets this class to its initial state. This method is used for testing only.
     */
    protected static void resetClassForTesting() {
        XmlDocument_ = null;
        ConfigurationMap_ = new WeakHashMap<BrowserVersion, JavaScriptConfiguration>(11);
    }

    /**
     * Sets the document configuration for testing.
     * @param document - The configuration document
     */
    protected static void setXmlDocument(final Document document) {
        XmlDocument_ = document;
    }

    /**
     * Gets the configuration file and make it an input reader and then pass to the method to read the file.
     */
    protected static void loadConfiguration() {
        try {
            final Reader reader = getConfigurationFileAsReader();
            if (reader == null) {
                LOG.error("Unable to load JavaScriptConfiguration.xml");
            }
            else {
                loadConfiguration(reader);
                reader.close();
            }
        }
        catch (final Exception e) {
            LOG.error("Error when loading JavascriptConfiguration.xml", e);
            e.printStackTrace();
        }
    }

    /**
     * Loads the configuration from a supplied Reader.
     *
     * @param configurationReader - A reader pointing to the configuration
     */
    protected static void loadConfiguration(final Reader configurationReader) {
        final InputSource inputSource = new InputSource(configurationReader);

        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setValidating(false);

            final DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            documentBuilder.setErrorHandler(new StrictErrorHandler());

            XmlDocument_ = documentBuilder.parse(inputSource);
        }
        catch (final SAXParseException parseException) {
            LOG.error("line=[" + parseException.getLineNumber()
                + "] columnNumber=[" + parseException.getColumnNumber()
                + "] systemId=[" + parseException.getSystemId()
                + "] publicId=[" + parseException.getPublicId() + "]", parseException);
        }
        catch (final Exception e) {
            LOG.error("Error when loading JavascriptConfiguration.xml", e);
        }
    }

    /**
     * Returns the instance that represents the configuration for the specified {@link BrowserVersion}.
     * This method is synchronized to allow multithreaded access to the JavaScript configuration.
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

    private static Reader getConfigurationFileAsReader() {
        final Class< ? > clazz = JavaScriptConfiguration.class;
        final String name = clazz.getPackage().getName().replace('.', '/') + '/' + "JavaScriptConfiguration.xml";
        InputStream inputStream = clazz.getClassLoader().getResourceAsStream(name);
        if (inputStream == null) {
            try {
                final String localizedName = name.replace('/', File.separatorChar);
                inputStream = new FileInputStream(localizedName);
            }
            catch (final IOException e) {
                // Fall through
            }
        }

        // If we are running maven tests then the path will be slightly different
        if (inputStream == null) {
            try {
                final String localizedName = ("./src/java" + name).replace('/', File.separatorChar);
                inputStream = new FileInputStream(localizedName);
            }
            catch (final IOException e) {
                // Fall through
            }
        }
        return new InputStreamReader(inputStream);
    }

    /**
     * Gets all the configurations.
     * @return the class configurations
     */
    public Iterable<ClassConfiguration> getAll() {
        return configuration_.values();
    }

    private Map<String, ClassConfiguration> buildUsageMap(final BrowserVersion browser) {
        final Map<String, ClassConfiguration> classMap = new HashMap<String, ClassConfiguration>(30);
        Node node = XmlDocument_.getDocumentElement().getFirstChild();

        final Map<String, ClassConfiguration> virtualClasses = new HashMap<String, ClassConfiguration>();

        while (node != null) {
            if (node instanceof Element) {
                final Element element = (Element) node;
                if ("class".equals(element.getTagName())) {
                    if (!testToExcludeElement(element, browser)) {
                        final String hostClassName = element.getAttribute("classname");
                        if (hostClassName.startsWith("#")) {
                            // this is not a real class but a facility for configuration
                            final String extendsClassName = element.getAttribute("extends");
                            final ClassConfiguration parentConfig = classMap.get(extendsClassName);
                            if (parentConfig == null) {
                                throw new RuntimeException(extendsClassName
                                    + " should be specified before " + hostClassName);
                            }
                            element.setAttribute("classname", parentConfig.getHostClass().getName());
                        }

                        try {
                            final ClassConfiguration config = parseClassElement(element, browser);
                            if (config != null) {
                                if (hostClassName.startsWith("#")) {
                                    virtualClasses.put(hostClassName, config);
                                    element.setAttribute("classname", hostClassName); // restore it
                                }
                                else {
                                    classMap.put(config.getHostClass().getSimpleName(), config);
                                }
                            }
                        }
                        catch (final ClassNotFoundException e) {
                            throw new IllegalStateException("The class was not found for '"
                                    + element.getAttribute("classname") + "'");
                        }
                    }
                }
            }
            node = node.getNextSibling();
        }

        // add properties and methods from the virtual classes directly to the classes that "inherit" from them
        for (final ClassConfiguration config : classMap.values()) {
            final String extendsClassName = config.getExtendedClassName();
            if (extendsClassName.startsWith("#")) {
                final ClassConfiguration virtualClassConfig = virtualClasses.get(extendsClassName);
                if (virtualClassConfig == null) {
                    throw new RuntimeException("Virtual config >" + extendsClassName + "< doesn't exist!");
                }

                config.addAllDefinitions(virtualClassConfig);
                // only one level of virtual classes allowed
                config.setExtendedClassName(virtualClassConfig.getExtendedClassName());
            }
        }

        return Collections.unmodifiableMap(classMap);
    }

    /**
     * Parses the class element to build the class configuration.
     * @param className the name of the class element
     * @param element the element to parse
     * @param browser the browser version under consideration
     * @return the class element to build the class configuration
     * @throws ClassNotFoundException if the specified class could not be found
     */
    private ClassConfiguration parseClassElement(final Element element,
        final BrowserVersion browser) throws ClassNotFoundException {
        final String notImplemented = element.getAttribute("notImplemented");
        if ("true".equalsIgnoreCase(notImplemented)) {
            return null;
        }
        final String hostClassName = element.getAttribute("classname");
        final String jsConstructor = element.getAttribute("jsConstructor");
        final String extendsClassName = element.getAttribute("extends");
        final String htmlClassName = element.getAttribute("htmlClass");
        boolean jsObjectFlag = false;
        final String jsObjectStr = element.getAttribute("JSObject");
        if ("true".equalsIgnoreCase(jsObjectStr)) {
            jsObjectFlag = true;
        }
        final ClassConfiguration classConfiguration = new ClassConfiguration(hostClassName, jsConstructor,
                    extendsClassName, htmlClassName, jsObjectFlag);
        final String simpleClassName = hostClassName.substring(hostClassName.lastIndexOf('.') + 1);
        ClassnameMap_.put(hostClassName, simpleClassName);
        Node node = element.getFirstChild();
        while (node != null) {
            if (node instanceof Element) {
                final Element childElement = (Element) node;
                final String tagName = childElement.getTagName();
                if ("property".equals(tagName)) {
                    parsePropertyElement(classConfiguration, childElement, browser);
                }
                else if ("function".equals(tagName)) {
                    parseFunctionElement(classConfiguration, childElement, browser);
                }
                else if ("constant".equals(tagName)) {
                    parseConstantElement(classConfiguration, childElement, browser);
                }
                else if ("browser".equals(tagName)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("browser tag not yet handled for class " + hostClassName);
                    }
                }
                else if ("doclink".equals(tagName)) {
                    // ignore this link
                }
                else {
                    throw new IllegalStateException("Do not understand element type '"
                        + tagName + "' in '" + hostClassName + "'");
                }
            }
            node = node.getNextSibling();
        }
        return classConfiguration;
    }

    /**
     * Parse out the values for the property.
     *
     * @param classConfiguration the configuration that is being built
     * @param element the property element
     * @param browser the browser version under consideration
     */
    private void parsePropertyElement(final ClassConfiguration classConfiguration, final Element element,
        final BrowserVersion browser) {
        final String notImplemented = element.getAttribute("notImplemented");
        if ("true".equalsIgnoreCase(notImplemented)) {
            return;
        }
        if (testToExcludeElement(element, browser)) {
            return;
        }
        final String propertyName = element.getAttribute("name");
        boolean readable = false;
        boolean writable = false;
        final String readFlag = element.getAttribute("readable");
        if ("true".equalsIgnoreCase(readFlag)) {
            readable = true;
        }
        final String writeFlag = element.getAttribute("writable");
        if ("true".equalsIgnoreCase(writeFlag)) {
            writable = true;
        }
        classConfiguration.addProperty(propertyName, readable, writable);
    }

    /**
     * Parses out the values from the function element.
     *
     * @param classConfiguration the configuration that is being built
     * @param element the function element
     * @param browser the browser version under consideration
     */
    private void parseFunctionElement(final ClassConfiguration classConfiguration, final Element element,
        final BrowserVersion browser) {
        final String notImplemented = element.getAttribute("notImplemented");
        if ("true".equalsIgnoreCase(notImplemented)) {
            return;
        }
        final String propertyName = element.getAttribute("name");
        if (testToExcludeElement(element, browser)) {
            return;
        }
        classConfiguration.addFunction(propertyName);
    }

    /**
     * Parses out the values for the constant.
     *
     * @param classConfiguration the configuration that is being built
     * @param element the property element
     * @param browser the browser version under consideration
     */
    private void parseConstantElement(final ClassConfiguration classConfiguration, final Element element,
        final BrowserVersion browser) {
        if (testToExcludeElement(element, browser)) {
            return;
        }
        final String constantName = element.getAttribute("name");
        classConfiguration.addConstant(constantName);
    }

    /**
     * Test for the browser and JavaScript constraints. Returns true if any constraints are present
     * and the browser does not meet the constraints.
     * @param element the element to scan the children of
     * @param browser the browser version under consideration
     * @return true to exclude this element
     */
    private boolean testToExcludeElement(final Element element, final BrowserVersion browser) {
        if (browser == null) {
            return false;
        }
        Node node = element.getFirstChild();
        boolean browserConstraint = false;
        boolean allowBrowser = false;
        while (node != null) {
            if (node instanceof Element) {
                final Element childElement = (Element) node;
                if ("browser".equals(childElement.getTagName())) {
                    browserConstraint = true;
                    if (testToIncludeForBrowserConstraint(childElement, browser)) {
                        allowBrowser = true;
                    }
                }
            }
            node = node.getNextSibling();
        }
        if (browserConstraint && !allowBrowser) {
            return true;
        }
        return false;
    }

    /**
     * Gets the class configuration for the supplied JavaScript class name.
     * @param classname the js class name
     * @return the class configuration for the supplied JavaScript class name
     */
    public ClassConfiguration getClassConfiguration(final String classname) {
        return configuration_.get(classname);
    }

    private boolean testToIncludeForBrowserConstraint(final Element element, final BrowserVersion browser) {
        if ((!browser.isIE() || !"Internet Explorer".equals(element.getAttribute("name")))
            && (!browser.isFirefox() || !"Firefox".equals(element.getAttribute("name")))) {
            return false;
        }
        final String max = element.getAttribute("max-version");
        float maxVersion;
        if (max.length() == 0) {
            maxVersion = 0;
        }
        else {
            maxVersion = Float.parseFloat(max);
        }
        if (maxVersion > 0 && browser.getBrowserVersionNumeric() > maxVersion) {
            return false;
        }

        float minVersion;
        final String min = element.getAttribute("min-version");
        if (min.length() == 0) {
            minVersion = 0;
        }
        else {
            minVersion = Float.parseFloat(min);
        }
        if (minVersion > 0 && browser.getBrowserVersionNumeric() < minVersion) {
            return false;
        }
        return true;
    }

    /**
     * Returns the classname that the given class implements. If the class is
     * the input class, then the name is extracted from the type that the Input class
     * is masquerading as.
     * FIXME - Implement the Input class processing
     * @param clazz
     * @return the classname
     */
    String getClassnameForClass(final Class< ? > clazz) {
        final String name = ClassnameMap_.get(clazz.getName());
        if (name == null) {
            throw new IllegalStateException("Did not find the mapping of the class to the classname for "
                + clazz.getName());
        }
        return name;
    }

    /**
     * Returns an immutable map containing the HTML to JavaScript mappings. Keys are
     * java classes for the various HTML classes (ie HtmlInput.class) and the values
     * are the JavaScript class names (ie "HTMLAnchorElement").
     * @return the mappings
     */
    @SuppressWarnings("unchecked")
    public Map<Class < ? extends HtmlElement>, Class < ? extends SimpleScriptable>>
    getHtmlJavaScriptMapping() {
        if (htmlJavaScriptMap_ != null) {
            return htmlJavaScriptMap_;
        }

        final Map<Class < ? extends HtmlElement>, Class < ? extends SimpleScriptable>> map =
            new HashMap<Class < ? extends HtmlElement>, Class < ? extends SimpleScriptable>>();

        for (String jsClassname : configuration_.keySet()) {
            ClassConfiguration classConfig = getClassConfiguration(jsClassname);
            final String htmlClassname = classConfig.getHtmlClassname();
            if (htmlClassname != null) {
                try {
                    final Class< ? extends HtmlElement> htmlClass =
                        (Class< ? extends HtmlElement>) Class.forName(htmlClassname);
                    // preload and validate that the class exists
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Mapping " + htmlClass.getName() + " to " + jsClassname);
                    }
                    while (!classConfig.isJsObject()) {
                        jsClassname = classConfig.getExtendedClassName();
                        classConfig = getClassConfiguration(jsClassname);
                    }
                    map.put(htmlClass, classConfig.getHostClass());
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

        htmlJavaScriptMap_ = Collections.unmodifiableMap(map);

        return htmlJavaScriptMap_;
    }
}
