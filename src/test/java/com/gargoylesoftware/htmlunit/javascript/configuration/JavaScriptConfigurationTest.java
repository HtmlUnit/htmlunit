/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
import com.gargoylesoftware.htmlunit.javascript.host.Document;

/**
 * Tests for {@link JavaScriptConfiguration}.
 *
 * @version $Revision$
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class JavaScriptConfigurationTest extends WebTestCase {

    /**
     * Resets the JavaScriptConfiguration file for each test to it's initial clean state.
     * @throws Exception if the test fails
     */
    @Before
    public void setUp() throws Exception {
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
    public void testLoadLocalConfiguration() {
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
    public void testLoadSystemConfigurationFile() {
        Assert.assertFalse("Document should not be loaded", JavaScriptConfiguration.isDocumentLoaded());
        JavaScriptConfiguration.loadConfiguration();
        assertTrue("Documnet should now be loaded", JavaScriptConfiguration.isDocumentLoaded());
    }
        
    /**
     * Tests getting the configuration for the full browser.
     * @throws Exception - Exception on error
     */
    @Test
    public void testGetInstance() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>\n"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" extends=\"Node\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <property name=\"readyState\" readable=\"true\" writable=\"false\">\n"
            + "            <browser name=\"Microsoft Internet Explorer\" min-version=\"4\"/>\n"
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
        final BrowserVersion browser = BrowserVersion.INTERNET_EXPLORER_6_0;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        final ClassConfiguration expectedConfig = new ClassConfiguration("Document",
            Document.class.getName(), null, null, null, true);
        expectedConfig.addProperty("readyState", true, false);
        assertTrue("Document property did not match", configuration.classConfigEquals("Document", expectedConfig));
    }
    
    /**
     * Test getting the configuration for the Netscape browser. The readyState property should not be available
     * in this case.
     * @throws Exception if an error occurs
     */
    @Test
    public void testGetConditionalPropertyBrowser() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>\n"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" extends=\"Node\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <property name=\"readyState\" readable=\"true\" writable=\"false\">\n"
            + "            <browser name=\"Microsoft Internet Explorer\" min-version=\"4\"/>\n"
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
            Document.class.getName(), null, null, null, true);
        assertTrue("Document property did not match", configuration.classConfigEquals("Document", expectedConfig));
    }
    
    /**
     * Test that the JSObject is being set correctly.
     * @throws Exception on error
     */
    @Test
    public void testForSettingJSObject() throws Exception {
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
        final BrowserVersion browser = BrowserVersion.INTERNET_EXPLORER_6_0;
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
    public void testInstanceForTestVersion() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>\n"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" extends=\"Node\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <property name=\"readyState\" readable=\"true\" writable=\"false\">\n"
            + "            <browser name=\"Microsoft Internet Explorer\" min-version=\"4\"/>\n"
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
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getAllEntries();
        final ClassConfiguration expectedConfig = new ClassConfiguration("Document",
            Document.class.getName(), null, null, null, true);
        expectedConfig.addProperty("readyState", true, false);
        assertTrue("Document property did not match", configuration.classConfigEquals("Document", expectedConfig));
    }

    /**
     * Tests getting the configuration for the Netscape browser. The readyState property should not be
     * available in this case.
     * @throws Exception if an error occurs
     */
    @Test
    public void testPropertyForNullBrowser() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>\n"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" extends=\"Node\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <property name=\"readyState\" readable=\"true\" writable=\"false\">\n"
            + "            <browser name=\"Microsoft Internet Explorer\" min-version=\"4\"/>\n"
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
    public void testGetConditionalPropertyMinBrowserVersion() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>\n"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" extends=\"Node\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <property name=\"readyState\" readable=\"true\" writable=\"false\">\n"
            + "            <browser name=\"Microsoft Internet Explorer\" min-version=\"8\"/>\n"
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
        final BrowserVersion browser = BrowserVersion.INTERNET_EXPLORER_6_0;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        final ClassConfiguration expectedConfig = new ClassConfiguration("Document",
            Document.class.getName(), null, null, null, true);
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
    public void testGetConditionalPropertyMaxBrowserVersion() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>\n"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" extends=\"Node\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <property name=\"readyState\" readable=\"true\" writable=\"false\">\n"
            + "            <browser name=\"Microsoft Internet Explorer\" max-version=\"3\"/>\n"
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
        final BrowserVersion browser = BrowserVersion.INTERNET_EXPLORER_6_0;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        final ClassConfiguration expectedConfig = new ClassConfiguration("Document",
            Document.class.getName(), null, null, null, true);
        assertTrue("Document should not property did not match",
            configuration.classConfigEquals("Document", expectedConfig));
    }

    /**
     * Test getting the configuration for the JavaScript max version. The readyState property should not be
     * available in this case.
     *
     * @throws Exception - Exception on error
     */
    @Test
    public void testGetConditionalPropertyMaxJSVersion() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>\n"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" extends=\"Node\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <property name=\"readyState\" readable=\"true\" writable=\"false\">\n"
            + "            <javascript max-version=\"1\"/>\n"
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
        final BrowserVersion browser = BrowserVersion.INTERNET_EXPLORER_6_0;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        final ClassConfiguration expectedConfig = new ClassConfiguration("Document",
            Document.class.getName(), null, null, null, true);
        assertTrue("Document should not property did not match",
            configuration.classConfigEquals("Document", expectedConfig));
    }

    /**
     * Test parsing the configuration with a function in it.
     *
     * @throws Exception - Exception on error
     */
    @Test
    public void testParseFunction() throws Exception {
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
        final BrowserVersion browser = BrowserVersion.INTERNET_EXPLORER_6_0;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        final ClassConfiguration expectedConfig = new ClassConfiguration("Document",
            Document.class.getName(), null, null, null, true);
        expectedConfig.addFunction("createAttribute");
        assertTrue("Document function did not match", configuration.classConfigEquals("Document", expectedConfig));
    }

    /**
     * Test parsing the configuration for a browser that does not support the function.
     *
     * @throws Exception - Exception on error
     */
    @Test
    public void testParseFunctionForLimitedBrowser() throws Exception {
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
        final BrowserVersion browser = BrowserVersion.INTERNET_EXPLORER_6_0;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        final ClassConfiguration expectedConfig = new ClassConfiguration("Document",
            Document.class.getName(), null, null, null, true);
        assertTrue("Document function did not match", configuration.classConfigEquals("Document", expectedConfig));
    }

    /**
     * Test that the file JavaScriptConfiguration.xml is valid.
     * @throws Exception if the test fails
     */
    @Test
    public void testConfigurationFileAgainstSchema() throws Exception {
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
            @Test
            public void warning(final SAXParseException exception) throws SAXException {
                throw exception;
            }
            @Test
            public void error(final SAXParseException exception) throws SAXException {
                throw exception;
            }
            @Test
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
    public void testConfigurationFile() throws Exception {
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getAllEntries();
        
        for (final String classname : configuration.keySet()) {
            getLog().debug("Now testing for class " + classname);
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
    public void testForPropertyExist() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>\n"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" extends=\"Node\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <property name=\"readyState\" readable=\"true\" writable=\"false\">\n"
            + "            <browser name=\"Microsoft Internet Explorer\" min-version=\"4\"/>\n"
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
        final BrowserVersion browser = BrowserVersion.INTERNET_EXPLORER_6_0;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        assertTrue("Requested property should have existed",
            configuration.propertyExists(Document.class, "readyState"));
    }
    
    /**
     * Tests for if the property exists for the configuration.
     * @throws Exception if an error occurs
     */
    @Test
    public void testForPropertyNotExist() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>\n"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" extends=\"Node\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <property name=\"readyState\" readable=\"true\" writable=\"false\">\n"
            + "            <browser name=\"Microsoft Internet Explorer\" min-version=\"4\"/>\n"
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
        final BrowserVersion browser = BrowserVersion.INTERNET_EXPLORER_6_0;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        Assert.assertFalse("Requested property should not exist",
            configuration.propertyExists(Document.class, "noreadyState"));
    }

    /**
     * Test if configuration map expands with each new instance of BrowserVersion used.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testConfigurationMapExpands() throws Exception {
        // get a reference to the leaky map
        final Field field = JavaScriptConfiguration.class.getDeclaredField("ConfigurationMap_");
        field.setAccessible(true);
        final HashMap< ? , ? > leakyMap = (HashMap< ? , ? >) field.get(null);

        for (int i = 0; i < 3; i++) {
            final BrowserVersion browserVersion = new BrowserVersion("App", "Version", "User agent", "1.2", 1);
            JavaScriptConfiguration.getInstance(browserVersion);
        }
        assertEquals(1, leakyMap.size());
    }

    /**
     * Test if the <tt>class name</tt> entries are lexicographically sorted.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testLexicographicOrder() throws Exception {
        final String directory = "src/main/resources/com/gargoylesoftware/htmlunit/javascript/configuration/";

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);

        final DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        documentBuilder.setErrorHandler(new StrictErrorHandler());

        final org.w3c.dom.Document doc =
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
                }
            }
            node = node.getNextSibling();
        }
    }
}
