/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.javascript.host.Document;

/**
 * Tests for JavaScriptConfiguration.
 *
 * @version $Revision$
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class JavaScriptConfigurationTest extends WebTestCase {
    /**
     * Create an instance
     * @param name The name of the test
     */
    public JavaScriptConfigurationTest( final String name ) {
        super(name);
    }
    
    /**
     * Reset the JavaScriptConfiguration file for each test to it's initial clean state.
     * 
     * @throws Exception if the test fails
     */
    protected void setUp() throws Exception {
        super.setUp();
        JavaScriptConfiguration.resetClassForTesting();
    }

    /**
     * Reset the JavaScriptConfiguration file for each test to it's initial clean state.
     * 
     * @throws Exception if the test fails
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        JavaScriptConfiguration.resetClassForTesting();
    }

    /**
     * Test loading a configuration from the supplied stream
     *
     */
    public void testLoadLocalConfiguration() {
        final String configurationString
            = "<?xml version=\"1.0\"?>"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <property name=\"readyState\" readable=\"true\" writable=\"false\">\n"
            + "        </property>\n"
            + "    </class>\n"
            + "</configuration>\n";
        final Reader reader = new StringReader(configurationString);
        assertFalse("Document should not be loaded", JavaScriptConfiguration.isDocumentLoaded());
        JavaScriptConfiguration.loadConfiguration(reader);
        assertTrue("Documnet should now be loaded", JavaScriptConfiguration.isDocumentLoaded());
    }
    
    /**
     * Test loading a configuration from the supplied stream
     *
     */
    public void testLoadSystemConfigurationFile() {
        assertFalse("Document should not be loaded", JavaScriptConfiguration.isDocumentLoaded());
        JavaScriptConfiguration.loadConfiguration();
        assertTrue("Documnet should now be loaded", JavaScriptConfiguration.isDocumentLoaded());
    }
        
    /**
     * Test getting the configuration for the full browser
     *
     * @throws Exception - Exception on error
     */
    public void testGetInstance() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" extends=\"NodeImpl\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <property name=\"readyState\" readable=\"true\" writable=\"false\">\n"
            + "            <browser name=\"Microsoft Internet Explorer\" min-version=\"4\"/>\n"
            + "        </property>\n"
            + "    </class>\n"
            + "    <class name=\"NodeImpl\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.NodeImpl\">\n"
            + "        <property name=\"firstChild\" readable=\"true\" writable=\"false\">\n"
            + "        </property>\n"
            + "    </class>\n"
            + "</configuration>\n";
        final Reader reader = new StringReader(configurationString);
        JavaScriptConfiguration.loadConfiguration(reader);
        final BrowserVersion browser = BrowserVersion.FULL_FEATURED_BROWSER;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        final ClassConfiguration expectedConfig = new ClassConfiguration("Document", 
            Document.class.getName(), null, null, null, true);
        expectedConfig.addProperty("readyState", true, false);
        assertTrue("Document property did not match", configuration.classConfigEquals("Document", expectedConfig));
    }
    
    /**
     * Test getting the configuration for the Netscape browser.  The readyState property should not be
     * available in this case.
     *
     * @throws Exception - Exception on error
     */
    public void testGetConditionalPropertyBrowser() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" extends=\"NodeImpl\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <property name=\"readyState\" readable=\"true\" writable=\"false\">\n"
            + "            <browser name=\"Microsoft Internet Explorer\" min-version=\"4\"/>\n"
            + "        </property>\n"
            + "    </class>\n"
            + "    <class name=\"NodeImpl\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.NodeImpl\">\n"
            + "        <property name=\"firstChild\" readable=\"true\" writable=\"false\">\n"
            + "        </property>\n"
            + "    </class>\n"
            + "</configuration>\n";
        final Reader reader = new StringReader(configurationString);
        JavaScriptConfiguration.loadConfiguration(reader);
        final BrowserVersion browser = BrowserVersion.NETSCAPE_6_2_3;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        final ClassConfiguration expectedConfig = new ClassConfiguration("Document", 
            Document.class.getName(), null, null, null, true);
        assertTrue("Document property did not match", configuration.classConfigEquals("Document", expectedConfig));
    }
    
    /**
     * Test that the JSObject is being set correctly
     * @throws Exception on error
     */
    public void testForSettingJSObject() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" extends=\"NodeImpl\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\" JSObject=\"true\">\n"
            + "    </class>\n"
            + "</configuration>\n";
        final Reader reader = new StringReader(configurationString);
        JavaScriptConfiguration.loadConfiguration(reader);
        final BrowserVersion browser = BrowserVersion.FULL_FEATURED_BROWSER;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        final ClassConfiguration config = configuration.getClassConfiguration("Document");
        assertTrue("JSObject is not set", config.isJsObject());
    }

    /**
     * Test getting the configuration for the Netscape browser.  The readyState property should not be
     * available in this case.
     *
     * @throws Exception - Exception on error
     */
    public void testInstanceForTestVersion() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" extends=\"NodeImpl\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <property name=\"readyState\" readable=\"true\" writable=\"false\">\n"
            + "            <browser name=\"Microsoft Internet Explorer\" min-version=\"4\"/>\n"
            + "        </property>\n"
            + "    </class>\n"
            + "    <class name=\"NodeImpl\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.NodeImpl\">\n"
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
     * Test getting the configuration for the Netscape browser.  The readyState property should not be
     * available in this case.
     *
     * @throws Exception - Exception on error
     */
    public void testPropertyForNullBrowser() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" extends=\"NodeImpl\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <property name=\"readyState\" readable=\"true\" writable=\"false\">\n"
            + "            <browser name=\"Microsoft Internet Explorer\" min-version=\"4\"/>\n"
            + "        </property>\n"
            + "    </class>\n"
            + "    <class name=\"NodeImpl\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.NodeImpl\">\n"
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
     * Test getting the configuration for the Netscape browser.  The readyState property should not be
     * available in this case.
     *
     * @throws Exception - Exception on error
     */
    public void testGetConditionalPropertyMinBrowserVersion() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" extends=\"NodeImpl\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <property name=\"readyState\" readable=\"true\" writable=\"false\">\n"
            + "            <browser name=\"Microsoft Internet Explorer\" min-version=\"8\"/>\n"
            + "        </property>\n"
            + "    </class>\n"
            + "    <class name=\"NodeImpl\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.NodeImpl\">\n"
            + "        <property name=\"firstChild\" readable=\"true\" writable=\"false\">\n"
            + "        </property>\n"
            + "    </class>\n"
            + "</configuration>\n";
        final Reader reader = new StringReader(configurationString);
        JavaScriptConfiguration.loadConfiguration(reader);
        final BrowserVersion browser = BrowserVersion.FULL_FEATURED_BROWSER;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        final ClassConfiguration expectedConfig = new ClassConfiguration("Document", 
            Document.class.getName(), null, null, null, true);
        assertTrue("Document should not property did not match", 
            configuration.classConfigEquals("Document", expectedConfig));
    }
    
    /**
     * Test getting the configuration for the browser max version.  The readyState property should not be
     * available in this case.
     *
     * @throws Exception - Exception on error
     */
    public void testGetConditionalPropertyMaxBrowserVersion() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" extends=\"NodeImpl\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <property name=\"readyState\" readable=\"true\" writable=\"false\">\n"
            + "            <browser name=\"Microsoft Internet Explorer\" max-version=\"3\"/>\n"
            + "        </property>\n"
            + "    </class>\n"
            + "    <class name=\"NodeImpl\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.NodeImpl\">\n"
            + "        <property name=\"firstChild\" readable=\"true\" writable=\"false\">\n"
            + "        </property>\n"
            + "    </class>\n"
            + "</configuration>\n";
        final Reader reader = new StringReader(configurationString);
        JavaScriptConfiguration.loadConfiguration(reader);
        final BrowserVersion browser = BrowserVersion.FULL_FEATURED_BROWSER;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        final ClassConfiguration expectedConfig = new ClassConfiguration("Document", 
            Document.class.getName(), null, null, null, true);
        assertTrue("Document should not property did not match", 
            configuration.classConfigEquals("Document", expectedConfig));
    }

    /**
     * Test getting the configuration for the JavaScript max version.  The readyState property should not be
     * available in this case.
     *
     * @throws Exception - Exception on error
     */
    public void testGetConditionalPropertyMaxJSVersion() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" extends=\"NodeImpl\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <property name=\"readyState\" readable=\"true\" writable=\"false\">\n"
            + "            <javascript max-version=\"1\"/>\n"
            + "        </property>\n"
            + "    </class>\n"
            + "    <class name=\"NodeImpl\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.NodeImpl\">\n"
            + "        <property name=\"firstChild\" readable=\"true\" writable=\"false\">\n"
            + "        </property>\n"
            + "    </class>\n"
            + "</configuration>\n";
        final Reader reader = new StringReader(configurationString);
        JavaScriptConfiguration.loadConfiguration(reader);
        final BrowserVersion browser = BrowserVersion.FULL_FEATURED_BROWSER;
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
    public void testParseFunction() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>"
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
        final BrowserVersion browser = BrowserVersion.FULL_FEATURED_BROWSER;
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
    public void testParseFunctionForLimitedBrowser() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>"
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
        final BrowserVersion browser = BrowserVersion.FULL_FEATURED_BROWSER;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        final ClassConfiguration expectedConfig = new ClassConfiguration("Document", 
            Document.class.getName(), null, null, null, true);
        assertTrue("Document function did not match", configuration.classConfigEquals("Document", expectedConfig));
    }

    /**
     * Test that the file JavaScriptConfiguration.xml is valid.
     * @throws Exception If the test fails
     */
    public void testConfigurationFileAgainstSchema() throws Exception {
        final XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        final String directory = "src/java/com/gargoylesoftware/htmlunit/javascript/configuration/";
        parser.setFeature("http://xml.org/sax/features/validation", true);
        parser.setFeature("http://apache.org/xml/features/validation/schema", true);
        parser.setEntityResolver( new EntityResolver() {
            public InputSource resolveEntity(final String publicId, final String systemId) throws IOException {
                return createInputSourceForFile(directory+"JavaScriptConfiguration.xsd");
            }
        });
        parser.setErrorHandler( new ErrorHandler() {
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

        parser.parse( createInputSourceForFile(directory+"JavaScriptConfiguration.xml") );

    }
    
    private InputSource createInputSourceForFile( final String fileName ) throws FileNotFoundException {
        return new InputSource( getFileAsStream(fileName) );
    }

    /**
     * Test that the data in the JavaScriptConfiguration file matches the classes in listed
     * properties and functions.  The first step is to get the configuration using a special method which
     * ignores the browser and javascript constraints.  This checks that there is a method for each item in 
     * the configuration file and fails by throwing an exception.
     * 
     * The second step is to go through each class and determine if for each jsGet, jsSet, and jsFunction class,
     * there is an entry in the configuration.
     *  
     * @throws Exception Exception on error
     */
    public void testConfigurationFile() throws Exception {
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getAllEntries();
        
        final Iterator it = configuration.keyIterator();
        while (it.hasNext()) {
            final String classname = (String) it.next();
            getLog().debug("Now testing for class " + classname);
            final Class clazz = configuration.getClassObject(classname);
            final Method[] methods = clazz.getMethods();
            String elementName;
            Method theMethod;
            for (int i=0; i < methods.length; i++) {
                final String name = methods[i].getName();
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
        final String[] ignoreList = { "Button|jsxGet_form",
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
        for (int i = 0; i < ignoreList.length; i++) {
            if (ignoreList[i].equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Test for if the property exists for the configuration
     * 
     * @throws Exception Exception on error
     */
    public void testForPropertyExist() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" extends=\"NodeImpl\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <property name=\"readyState\" readable=\"true\" writable=\"false\">\n"
            + "            <browser name=\"Microsoft Internet Explorer\" min-version=\"4\"/>\n"
            + "        </property>\n"
            + "    </class>\n"
            + "    <class name=\"NodeImpl\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.NodeImpl\">\n"
            + "        <property name=\"firstChild\" readable=\"true\" writable=\"false\">\n"
            + "        </property>\n"
            + "    </class>\n"
            + "</configuration>\n";
        final Reader reader = new StringReader(configurationString);
        JavaScriptConfiguration.loadConfiguration(reader);
        final BrowserVersion browser = BrowserVersion.FULL_FEATURED_BROWSER;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        assertTrue("Requested property should have existed", 
            configuration.propertyExists(Document.class, "readyState"));
    }
    
    /**
     * Test for if the property exists for the configuration
     * 
     * @throws Exception Exception on error
     */
    public void testForPropertyNotExist() throws Exception {
        final String configurationString
            = "<?xml version=\"1.0\"?>"
            + "<configuration\n"
            + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "    xsi:noNamespaceSchemaLocation=\"JavaScriptConfiguration.xsd\">\n"
            + "    <class name=\"Document\" extends=\"NodeImpl\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.Document\">\n"
            + "        <property name=\"readyState\" readable=\"true\" writable=\"false\">\n"
            + "            <browser name=\"Microsoft Internet Explorer\" min-version=\"4\"/>\n"
            + "        </property>\n"
            + "    </class>\n"
            + "    <class name=\"NodeImpl\" "
            + "classname=\"com.gargoylesoftware.htmlunit.javascript.host.NodeImpl\">\n"
            + "        <property name=\"firstChild\" readable=\"true\" writable=\"false\">\n"
            + "        </property>\n"
            + "    </class>\n"
            + "</configuration>\n";
        final Reader reader = new StringReader(configurationString);
        JavaScriptConfiguration.loadConfiguration(reader);
        final BrowserVersion browser = BrowserVersion.FULL_FEATURED_BROWSER;
        final JavaScriptConfiguration configuration = JavaScriptConfiguration.getInstance(browser);
        assertFalse("Requested property should not exist", 
            configuration.propertyExists(Document.class, "noreadyState"));
    }

}
