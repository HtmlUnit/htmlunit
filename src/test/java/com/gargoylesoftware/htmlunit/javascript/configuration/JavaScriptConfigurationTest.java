/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.javascript.StrictErrorHandler;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;

/**
 * Tests for {@link JavaScriptConfiguration}.
 *
 * @version $Revision$
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class JavaScriptConfigurationTest extends WebTestCase {

    private static final Log LOG = LogFactory.getLog(JavaScriptConfigurationTest.class);

    /**
     * Constructor.
     */
    public JavaScriptConfigurationTest() {
        JavaScriptConfiguration.resetClassForTesting();
    }

    /**
     * Resets the JavaScriptConfiguration file for each test to it's initial clean state.
     * @throws Exception if the test fails
     */
    @After
    public void tearDown() throws Exception {
        JavaScriptConfiguration.resetClassForTesting();
    }

    /**
     * Tests loading a configuration from the supplied stream.
     */
    @Test
    public void loadLocalConfiguration() {
        final String configurationString
            = "<?xml version=\"1.0\"?>\n"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <property name=\"readyState\" readable=\"true\" writable=\"false\">\n"
            + "        </property>\n"
            + "    </class>\n"
            + "</configuration>";
        final Reader reader = new StringReader(configurationString);
        Assert.assertFalse("Document should not be loaded", JavaScriptConfiguration.isDocumentLoaded());
        JavaScriptConfiguration.loadConfiguration(reader);
        assertTrue("Documnet should now be loaded", JavaScriptConfiguration.isDocumentLoaded());
    }

    /**
     * Tests loading a configuration from the supplied stream.
     */
    @Test
    public void loadSystemConfigurationFile() {
        Assert.assertFalse("Document should not be loaded", JavaScriptConfiguration.isDocumentLoaded());
        JavaScriptConfiguration.loadConfiguration();
        assertTrue("Documnet should now be loaded", JavaScriptConfiguration.isDocumentLoaded());
    }

    /**
     * Tests getting the configuration for the full browser.
     * @throws Exception - Exception on error
     */
    @Test
    public void getInstance() throws Exception {
        final String configurationString
            = "<?xml version='1.0'?>\n"
            + "<configuration\n"
            + "    xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'\n"
            + "    xsi:noNamespaceSchemaLocation='JavaScriptConfiguration.xsd'>\n"
            + "    <class name='HTMLDocument' extends='Node' "
            + "classname='com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument'>\n"
            + "        <property name='readyState' readable='true' writable='false'>\n"
            + "            <browser name='Internet Explorer' min-version='4'/>\n"
            + "        </property>\n"
            + "    </class>\n"
            + "    <class name='Node' "
            + "classname='com.gargoylesoftware.htmlunit.javascript.host.Node'>\n"
            + "        <property name='firstChild' readable='true' writable='false'>\n"
            + "        </property>\n"
            + "    </class>\n"
            + "</configuration>\n";
        final Reader reader = new StringReader(configurationString);
        JavaScriptConfiguration.loadConfiguration(reader);
        final BrowserVersion browser = BrowserVersion.INTERNET_EXPLORER_6;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        final ClassConfiguration expectedConfig = new ClassConfiguration("HTMLDocument",
            HTMLDocument.class.getName(), null, null, null, true);
        expectedConfig.addProperty("readyState", true, false);
        assertTrue("Document property did not match", configuration.classConfigEquals("HTMLDocument", expectedConfig));
    }

    /**
     * Test getting the configuration for the Netscape browser. The readyState property should not be available
     * in this case.
     * @throws Exception if an error occurs
     */
    @Test
    public void getConditionalPropertyBrowser() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>\n"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" extends=\"Node\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <property name=\"readyState\" readable=\"true\" writable=\"false\">\n"
            + "            <browser name=\"Internet Explorer\" min-version=\"4\"/>\n"
            + "        </property>\n"
            + "    </class>\n"
            + "    <class name=\"Node\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Node\">\n"
            + "        <property name=\"firstChild\" readable=\"true\" writable=\"false\">\n"
            + "        </property>\n"
            + "    </class>\n"
            + "</configuration>\n";
        final Reader reader = new StringReader(configurationString);
        JavaScriptConfiguration.loadConfiguration(reader);
        final BrowserVersion browser = BrowserVersion.FIREFOX_2;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        final ClassConfiguration expectedConfig = new ClassConfiguration("Document",
            HTMLDocument.class.getName(), null, null, null, true);
        assertTrue("Document property did not match", configuration.classConfigEquals("Document", expectedConfig));
    }

    /**
     * Test that the JSObject is being set correctly.
     * @throws Exception on error
     */
    @Test
    public void forSettingJSObject() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>\n"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" extends=\"Node\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\" JSObject=\"true\">\n"
            + "    </class>\n"
            + "</configuration>\n";
        final Reader reader = new StringReader(configurationString);
        JavaScriptConfiguration.loadConfiguration(reader);
        final BrowserVersion browser = BrowserVersion.INTERNET_EXPLORER_6;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        final ClassConfiguration config = configuration.getClassConfiguration("Document");
        assertTrue("JSObject is not set", config.isJsObject());
    }

    /**
     * Test getting the configuration for the Netscape browser. The readyState property should not be
     * available in this case.
     * @throws Exception if an error occurs
     */
    @Test
    public void instanceForTestVersion() throws Exception {
        final String configurationString
            = "<?xml version='1.0'?>\n"
            + "<configuration\n"
            + "    xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'\n"
            + "    xsi:noNamespaceSchemaLocation='JavaScriptConfiguration.xsd'>\n"
            + "    <class name='HTMLDocument' extends='Node' "
            + "classname='com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument'>\n"
            + "        <property name='readyState' readable='true' writable='false'>\n"
            + "            <browser name='Internet Explorer' min-version='4'/>\n"
            + "        </property>\n"
            + "    </class>\n"
            + "    <class name='Node' "
            + "classname='com.gargoylesoftware.htmlunit.javascript.host.Node'>\n"
            + "        <property name='firstChild' readable='true' writable='false'>\n"
            + "        </property>\n"
            + "    </class>\n"
            + "</configuration>\n";
        final Reader reader = new StringReader(configurationString);
        JavaScriptConfiguration.loadConfiguration(reader);
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getAllEntries();
        final ClassConfiguration expectedConfig = new ClassConfiguration("HTMLDocument",
            HTMLDocument.class.getName(), null, null, null, true);
        expectedConfig.addProperty("readyState", true, false);
        assertTrue("Document property did not match", configuration.classConfigEquals("HTMLDocument", expectedConfig));
    }

    /**
     * Tests getting the configuration for the Netscape browser. The readyState property should not be
     * available in this case.
     * @throws Exception if an error occurs
     */
    @Test
    public void propertyForNullBrowser() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>\n"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" extends=\"Node\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <property name=\"readyState\" readable=\"true\" writable=\"false\">\n"
            + "            <browser name=\"Internet Explorer\" min-version=\"4\"/>\n"
            + "        </property>\n"
            + "    </class>\n"
            + "    <class name=\"Node\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Node\">\n"
            + "        <property name=\"firstChild\" readable=\"true\" writable=\"false\">\n"
            + "        </property>\n"
            + "    </class>\n"
            + "</configuration>\n";
        final Reader reader = new StringReader(configurationString);
        JavaScriptConfiguration.loadConfiguration(reader);
        try {
            JavaScriptConfiguration.getInstance(null);
            assertTrue("Should have thrown an exception for no browser supplied", false);
        }
        catch (final IllegalStateException e) {
            // everything is fine
        }
    }

    /**
     * Test getting the configuration for the Netscape browser. The readyState property should not be
     * available in this case.
     *
     * @throws Exception - Exception on error
     */
    @Test
    public void getConditionalPropertyMinBrowserVersion() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>\n"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" extends=\"Node\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <property name=\"readyState\" readable=\"true\" writable=\"false\">\n"
            + "            <browser name=\"Internet Explorer\" min-version=\"8\"/>\n"
            + "        </property>\n"
            + "    </class>\n"
            + "    <class name=\"Node\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Node\">\n"
            + "        <property name=\"firstChild\" readable=\"true\" writable=\"false\">\n"
            + "        </property>\n"
            + "    </class>\n"
            + "</configuration>\n";
        final Reader reader = new StringReader(configurationString);
        JavaScriptConfiguration.loadConfiguration(reader);
        final BrowserVersion browser = BrowserVersion.INTERNET_EXPLORER_6;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        final ClassConfiguration expectedConfig = new ClassConfiguration("Document",
            HTMLDocument.class.getName(), null, null, null, true);
        assertTrue("Document should not property did not match",
            configuration.classConfigEquals("Document", expectedConfig));
    }

    /**
     * Test getting the configuration for the browser max version. The readyState property should not be
     * available in this case.
     *
     * @throws Exception - Exception on error
     */
    @Test
    public void getConditionalPropertyMaxBrowserVersion() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>\n"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" extends=\"Node\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <property name=\"readyState\" readable=\"true\" writable=\"false\">\n"
            + "            <browser name=\"Internet Explorer\" max-version=\"3\"/>\n"
            + "        </property>\n"
            + "    </class>\n"
            + "    <class name=\"Node\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Node\">\n"
            + "        <property name=\"firstChild\" readable=\"true\" writable=\"false\">\n"
            + "        </property>\n"
            + "    </class>\n"
            + "</configuration>\n";
        final Reader reader = new StringReader(configurationString);
        JavaScriptConfiguration.loadConfiguration(reader);
        final BrowserVersion browser = BrowserVersion.INTERNET_EXPLORER_6;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        final ClassConfiguration expectedConfig = new ClassConfiguration("Document",
            HTMLDocument.class.getName(), null, null, null, true);
        assertTrue("Document should not property did not match",
            configuration.classConfigEquals("Document", expectedConfig));
    }

    /**
     * Test parsing the configuration with a function in it.
     *
     * @throws Exception - Exception on error
     */
    @Test
    public void parseFunction() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>\n"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <function name=\"createAttribute\"/>\n"
            + "    </class>\n"
            + "</configuration>\n";
        final Reader reader = new StringReader(configurationString);
        JavaScriptConfiguration.loadConfiguration(reader);
        final BrowserVersion browser = BrowserVersion.INTERNET_EXPLORER_6;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        final ClassConfiguration expectedConfig = new ClassConfiguration("Document",
            HTMLDocument.class.getName(), null, null, null, true);
        expectedConfig.addFunction("createAttribute");
        assertTrue("Document function did not match", configuration.classConfigEquals("Document", expectedConfig));
    }

    /**
     * Test parsing the configuration for a browser that does not support the function.
     *
     * @throws Exception - Exception on error
     */
    @Test
    public void parseFunctionForLimitedBrowser() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>\n"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <function name=\"createAttribute\">\n"
            + "            <browser name=\"Netscape\"/>\n"
            + "        </function>\n"
            + "    </class>\n"
            + "</configuration>\n";
        final Reader reader = new StringReader(configurationString);
        JavaScriptConfiguration.loadConfiguration(reader);
        final BrowserVersion browser = BrowserVersion.INTERNET_EXPLORER_6;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        final ClassConfiguration expectedConfig = new ClassConfiguration("Document",
            HTMLDocument.class.getName(), null, null, null, true);
        assertTrue("Document function did not match", configuration.classConfigEquals("Document", expectedConfig));
    }

    /**
     * Test that the file JavaScriptConfiguration.xml is valid.
     * @throws Exception if the test fails
     */
    @Test
    public void configurationFileAgainstSchema() throws Exception {
        final XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        final String directory = "src/main/resources/com/gargoylesoftware/htmlunit/javascript/configuration/";
        parser.setFeature("http://xml.org/sax/features/validation", true);
        parser.setFeature("http://apache.org/xml/features/validation/schema", true);
        parser.setEntityResolver(new EntityResolver() {
            public InputSource resolveEntity(final String publicId, final String systemId) throws IOException {
                return createInputSourceForFile(directory + "JavaScriptConfiguration.xsd");
            }
        });
        parser.setErrorHandler(new ErrorHandler() {
            public void warning(final SAXParseException exception) throws SAXException {
                throw exception;
            }
            public void error(final SAXParseException exception) throws SAXException {
                throw exception;
            }
            public void fatalError(final SAXParseException exception) throws SAXException {
                throw exception;
            }
        });

        parser.parse(createInputSourceForFile(directory + "JavaScriptConfiguration.xml"));
    }

    private InputSource createInputSourceForFile(final String fileName) throws FileNotFoundException {
        return new InputSource(getFileAsStream(fileName));
    }

    /**
     * Test that the data in the JavaScriptConfiguration file matches the classes in listed
     * properties and functions. The first step is to get the configuration using a special method which
     * ignores the browser and JavaScript constraints. This checks that there is a method for each item in
     * the configuration file and fails by throwing an exception.
     *
     * The second step is to go through each class and determine if for each jsGet, jsSet, and jsFunction class,
     * there is an entry in the configuration.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void configurationFile() throws Exception {
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getAllEntries();

        for (final String classname : configuration.keySet()) {
            LOG.debug("Now testing for class " + classname);
            final Class< ? > clazz = configuration.getClassObject(classname);
            final Method[] methods = clazz.getMethods();
            String elementName;
            Method theMethod;
            for (final Method method : methods) {
                final String name = method.getName();
                if (checkForIgnore(name, classname)) {
                    continue;
                }
                if (name.startsWith("jsxGet_")) {
                    elementName = name.substring(7);
                    theMethod = configuration.getPropertyReadMethod(classname, elementName);
                    assertNotNull("No definition found for " + name + " defined in " + clazz.getName()
                        + " for object " + classname, theMethod);
                }
                else if (name.startsWith("jsxSet_")) {
                    elementName = name.substring(7);
                    theMethod = configuration.getPropertyWriteMethod(classname, elementName);
                    assertNotNull("No definition found for " + name + " defined in " + clazz.getName()
                        + " for object " + classname, theMethod);
                }
                else if (name.startsWith("jsxFunction_")) {
                    elementName = name.substring(12);
                    theMethod = configuration.getFunctionMethod(classname, elementName);
                    assertNotNull("No definition found for " + name + " defined in " + clazz.getName()
                        + " for object " + classname, theMethod);
                }
            }
        }
//        Now test the config for each class and test for methods being defined in the config file
//        This is the place
    }

    private boolean checkForIgnore(final String methodName, final String classname) {
        final String[] ignoreList = {"Button|jsxGet_form",
            "FormField|jsxGet_form",
            "FileUpload|jsxGet_form",
            "Radio|jsxGet_form",
            "Reset|jsxGet_form",
            "Submit|jsxGet_form",
            "Checkbox|jsxGet_form",
            "Hidden|jsxGet_form",
            "Select|jsxGet_form",
            "Textarea|jsxGet_form",
            "Input|jsxGet_form",
            "Password|jsxGet_form",
            "HTMLSpanElement|jsxGet_cite",
            "HTMLSpanElement|jsxSet_cite",
            "HTMLSpanElement|jsxGet_dateTime",
            "HTMLSpanElement|jsxSet_dateTime",
            "CharacterDataImpl|jsxGet_tabindex"};
        final String key = classname + "|" + methodName;
        for (final String value : ignoreList) {
            if (value.equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests for if the property exists for the configuration.
     * @throws Exception if an error occurs
     */
    @Test
    public void forPropertyExist() throws Exception {
        final String configurationString
            = "<?xml version='1.0'?>\n"
            + "<configuration\n"
            + "    xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'\n"
            + "    xsi:noNamespaceSchemaLocation='JavaScriptConfiguration.xsd'>\n"
            + "    <class name='HTMLDocument' extends='Node' "
            + "classname='com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument'>\n"
            + "        <property name='readyState' readable='true' writable='false'>\n"
            + "            <browser name='Internet Explorer' min-version='4'/>\n"
            + "        </property>\n"
            + "    </class>\n"
            + "    <class name='Node' "
            + "classname='com.gargoylesoftware.htmlunit.javascript.host.Node'>\n"
            + "        <property name='firstChild' readable='true' writable='false'>\n"
            + "        </property>\n"
            + "    </class>\n"
            + "</configuration>\n";
        final Reader reader = new StringReader(configurationString);
        JavaScriptConfiguration.loadConfiguration(reader);
        final BrowserVersion browser = BrowserVersion.INTERNET_EXPLORER_6;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        assertTrue("Requested property should have existed",
            configuration.propertyExists(HTMLDocument.class, "readyState"));
    }

    /**
     * Tests for if the property exists for the configuration.
     * @throws Exception if an error occurs
     */
    @Test
    public void forPropertyNotExist() throws Exception {
        final String configurationString
            = "<?xml version='1.0'?>\n"
            + "<configuration\n"
            + "    xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'\n"
            + "    xsi:noNamespaceSchemaLocation='JavaScriptConfiguration.xsd'>\n"
            + "    <class name='HTMLDocument' extends='Node' "
            + "classname='com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument'>\n"
            + "        <property name='readyState' readable='true' writable='false'>\n"
            + "            <browser name='Internet Explorer' min-version='4'/>\n"
            + "        </property>\n"
            + "    </class>\n"
            + "    <class name='Node' "
            + "classname='com.gargoylesoftware.htmlunit.javascript.host.Node'>\n"
            + "        <property name='firstChild' readable='true' writable='false'>\n"
            + "        </property>\n"
            + "    </class>\n"
            + "</configuration>\n";
        final Reader reader = new StringReader(configurationString);
        JavaScriptConfiguration.loadConfiguration(reader);
        final BrowserVersion browser = BrowserVersion.INTERNET_EXPLORER_6;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        Assert.assertFalse("Requested property should not exist",
            configuration.propertyExists(HTMLDocument.class, "noreadyState"));
    }

    /**
     * Test if configuration map expands with each new instance of BrowserVersion used.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void configurationMapExpands() throws Exception {
        // get a reference to the leaky map
        final Field field = JavaScriptConfiguration.class.getDeclaredField("ConfigurationMap_");
        field.setAccessible(true);
        final Map< ? , ? > leakyMap = (Map< ? , ? >) field.get(null);
        for (int i = 0; i < 3; i++) {
            final BrowserVersion browserVersion = new BrowserVersion("App", "Version", "User agent", 1);
            JavaScriptConfiguration.getInstance(browserVersion);
        }
        assertEquals(1, leakyMap.size());
    }

    /**
     * Regression test for bug 2854240.
     * This test was throwing an OutOfMemoryError when the bug existed.
     * @throws Exception if an error occurs
     */
    @Test
    public void memoryLeak() throws Exception {
        long count = 0;
        while (count++ < 3000) {
            final BrowserVersion browserVersion = new BrowserVersion(
                "App" + RandomStringUtils.randomAlphanumeric(20),
                "Version" + RandomStringUtils.randomAlphanumeric(20),
                "User Agent" + RandomStringUtils.randomAlphanumeric(20),
                1);
            JavaScriptConfiguration.getInstance(browserVersion);
            LOG.info("count: " + count + "; memory stats: " + getMemoryStats());
        }
    }

    private String getMemoryStats() {
        final Runtime rt = Runtime.getRuntime();
        final long free = rt.freeMemory() / 1024;
        final long total = rt.totalMemory() / 1024;
        final long max = rt.maxMemory() / 1024;
        final long used = total - free;
        final String format = "used: {0,number,0}K, free: {1,number,0}K, total: {2,number,0}K, max: {3,number,0}K";
        return MessageFormat.format(format, used, free, total, max);
    }

    /**
     * Test if the <tt>class name</tt> entries are lexicographically sorted.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void lexicographicOrder() throws Exception {
        final String directory = "src/main/resources/com/gargoylesoftware/htmlunit/javascript/configuration/";

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);

        final DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        documentBuilder.setErrorHandler(new StrictErrorHandler());

        final Document doc =
            documentBuilder.parse(createInputSourceForFile(directory + "JavaScriptConfiguration.xml"));

        String lastClassName = null;
        Node node = doc.getDocumentElement().getFirstChild();
        while (node != null) {
            if (node instanceof Element) {
                final Element element = (Element) node;
                if (element.getTagName().equals("class")) {
                    final String className = element.getAttribute("name");
                    if (lastClassName != null && className.compareToIgnoreCase(lastClassName) < 1) {
                        fail("JavaScriptConfiguration.xml: \""
                                + className + "\" should be before \"" + lastClassName + '"');
                    }
                    lastClassName = className;

                    String lastChildName = null;
                    int lastChildType = 0;
                    Node child = element.getFirstChild();
                    while (child != null) {
                        if (child instanceof Element) {
                            final Element childE = (Element) child;
                            final String name = childE.getAttribute("name");
                            final String tagName = childE.getTagName();
                            final int childType = getType(tagName);
                            if (childType != -1 && childType != lastChildType) {
                                lastChildType = childType;
                                lastChildName = name;
                            }
                            else {
                                if (childType > 0) {
                                    if (lastChildName != null && name.compareToIgnoreCase(lastChildName) < 1) {
                                        fail("JavaScriptConfiguration.xml: \""
                                                + name + "\" should be before \"" + lastChildName + '"');
                                    }
                                    lastChildName = name;
                                }
                            }
                        }
                        child = child.getNextSibling();
                    }
                }
            }
            node = node.getNextSibling();
        }
    }

    private static int getType(final String tagName) {
        if (tagName.equals("constant")) {
            return 0;
        }
        if (tagName.equals("property")) {
            return 1;
        }
        if (tagName.equals("function")) {
            return 2;
        }
        return -1;
    }
}
