/*
 *  Copyright (C) 2002 Gargoyle Software. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.host.Document;
import com.gargoylesoftware.htmlunit.javascript.host.Form;
import com.gargoylesoftware.htmlunit.javascript.host.Input;
import com.gargoylesoftware.htmlunit.javascript.host.Option;
import com.gargoylesoftware.htmlunit.javascript.host.Select;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * A container for all the javascript configuration information.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public final class JavaScriptConfiguration {
    private static org.w3c.dom.Document XmlDocument_;
    private static final Object INITIALIZATION_LOCK = new Object();

    private static Map ConfigurationMap_ = new HashMap(11);


    private JavaScriptConfiguration( final BrowserVersion browserVersion ) {
        synchronized(INITIALIZATION_LOCK) {
            if( XmlDocument_ == null ) {
                XmlDocument_ = loadConfiguration();
            }
        }

        if( XmlDocument_ == null ) {
            throw new IllegalStateException("Configuration was not initialized - see log for details");
        }
    }


    private org.w3c.dom.Document loadConfiguration() {
        try {
            final InputStream inputStream = getConfigurationFileAsStream();
            if( inputStream == null ) {
                getLog().error("Unable to load JavaScriptConfiguration.xml");
                return null;
            }
            final InputSource inputSource = new InputSource(inputStream);

            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware( true );
            factory.setValidating( false );

            final DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            documentBuilder.setErrorHandler( new StrictErrorHandler() );

            return documentBuilder.parse( inputSource );
        }
        catch( final SAXParseException parseException ) {
            getLog().error( "line=[" + parseException.getLineNumber()
                    + "] columnNumber=[" + parseException.getColumnNumber()
                    + "] systemId=[" + parseException.getSystemId()
                    + "] publicId=[" + parseException.getPublicId() + "]", parseException );
        }
        catch( final Exception e ) {
            getLog().error("Error when loading JavascriptConfiguration.xml", e);
        }

        return null;
    }


    /**
     * Return the instance that represents the configuration for the specified {@link BrowserVersion}.
     * @param browserVersion The {@link BrowserVersion}
     * @return The instance for the specified {@link BrowserVersion}
     */
    public static JavaScriptConfiguration getInstance( final BrowserVersion browserVersion ) {
        JavaScriptConfiguration configuration
            = (JavaScriptConfiguration)ConfigurationMap_.get(browserVersion);

        if( configuration == null ) {
            configuration = new JavaScriptConfiguration(browserVersion);
            ConfigurationMap_.put( browserVersion, configuration );
        }
        return configuration;
    }


    private Log getLog() {
        return LogFactory.getLog(getClass());
    }


    /**
     * Return true if the specified name is a valid writable property for this configuration.
     *
     * @param hostClass The class that the property will be defined in.
     * @param name The name of the property.
     * @return true if this name is valid.
     */
    public boolean isValidWritablePropertyName( final Class hostClass, final String name ) {
        assertNotNull("hostClass", hostClass);
        assertNotNull("name", name);

        return getWritablePropertyNames(hostClass).contains(name);
    }


    /**
     * Return a list containing all the names of the writable properties for the specified class.
     * @param hostClass The class for which we are getting names.
     * @return A list of names.
     */
    private List getWritablePropertyNames( final Class hostClass ) {
        final List list = new ArrayList();

        if( Input.class.isAssignableFrom(hostClass) ) {
            list.add("value");
            list.add("checked");
            list.add("action");
            list.add("method");
            list.add("target");
            list.add("encoding");
        }

        if( Form.class.isAssignableFrom(hostClass) ) {
            list.add("action");
            list.add("method");
            list.add("target");
            list.add("encoding");
            list.add("style");
        }

        if( Select.class.isAssignableFrom(hostClass) ) {
            list.add("selectedIndex");
        }

        return list;
    }


    /**
     * Return true if the specified name is a valid readable property for this configuration.
     *
     * @param hostClass The class that the property will be defined in.
     * @param name The name of the property.
     * @return true if this name is valid.
     */
    public boolean isValidReadablePropertyName( final Class hostClass, final String name ) {
        assertNotNull("hostClass", hostClass);
        assertNotNull("name", name);

        return getReadablePropertyNames(hostClass).contains(name);
    }


    /**
     * Return a list containing all the names of the readable properties for the specified class.
     * @param hostClass The class for which we are getting names.
     * @return A list of names.
     */
    private List getReadablePropertyNames( final Class hostClass ) {
        final List list = new ArrayList();

        if( Input.class.isAssignableFrom(hostClass) ) {
            list.add("type");
            list.add("name");
            list.add("checked");
            list.add("value");
            list.add("form");
            list.add("style");
        }

        if( Form.class.isAssignableFrom(hostClass) ) {
            list.add("name");
            list.add("length");
            list.add("elements");
            list.add("action");
            list.add("method");
            list.add("target");
            list.add("encoding");
            list.add("style");
        }

        if( Document.class.isAssignableFrom(hostClass) ) {
            list.add("location");
            list.add("forms");
            list.add("cookie");
            list.add("style");
            list.add("images");
            list.add("referrer");
            list.add("URL");
            list.add("all");
        }

        if( Option.class.isAssignableFrom(hostClass) ) {
            list.add("value");
            list.add("text");
            list.add("style");
        }

        if( Select.class.isAssignableFrom(hostClass) ) {
            list.add("length");
            list.add("options");
            list.add("selectedIndex");
        }

        return list;
    }


    /**
     * Return true if the specified name is a valid function for this configuration.
     *
     * @param hostClass The class that the property will be defined in.
     * @param name The name of the function.
     * @return true if this name is valid.
     */
    public boolean isValidFunctionName( final Class hostClass, final String name ) {
        assertNotNull("hostClass", hostClass);
        assertNotNull("name", name);

        Element classElement = getClassElement(getClassName(hostClass));
        while( classElement != null ) {

            final Element functionElement = getElementByTypeAndName(classElement, "function", name);
            if( functionElement != null ) {
                return isEnabled(functionElement);
            }

            final String superClassName = classElement.getAttribute("extends");
            classElement = null;
            if( superClassName != null && superClassName.length() != 0 ) {
                classElement = getClassElement(superClassName);
            }
        }

        return false;
        //return getFunctionNames(hostClass).contains(name);
    }


    /**
     * Return a list containing all the names of the functions for the specified class.
     * @param hostClass The class for which we are getting names.
     * @return A list of names.
     */
    private List getFunctionNames( final Class hostClass ) {
        final List list = new ArrayList();

        if( Input.class.isAssignableFrom(hostClass) ) {
            list.add("focus");
            list.add("blur");
            list.add("click");
            list.add("select");
        }

        if( Window.class.isAssignableFrom(hostClass) ) {
            list.add("alert");
        }

        if( Form.class.isAssignableFrom(hostClass) ) {
            list.add("submit");
            list.add("reset");
        }

        if( Document.class.isAssignableFrom(hostClass) ) {
            list.add("write");
            list.add("writeln");
            list.add("close");
        }

        return list;
    }


    private InputStream getConfigurationFileAsStream() {
        final String fileName = "com/gargoylesoftware/htmlunit/javascript/JavaScriptConfiguration.xml";
        return getResourceAsStream(fileName);

    }


    private InputStream getResourceAsStream( final String name ) {
        assertNotNull("name", name);

        InputStream inputStream = getClass().getResourceAsStream(name);
        if( inputStream == null ) {
            try {
                final String localizedName = name.replace( '/', File.separatorChar );
                inputStream = new FileInputStream( localizedName );
            }
            catch( final IOException e ) {
                // Fall through
            }
        }
        return inputStream;
    }


    private void assertNotNull( final String description, final Object object ) {
        if( object == null ) {
            throw new NullPointerException(description);
        }
    }


    private String getClassName( final Class hostClass ) {
        assertNotNull("hostClass", hostClass);

        final String className = hostClass.getName();
        final int index = className.lastIndexOf(".");
        if( index == -1 ) {
            throw new IllegalArgumentException("hostClass does not contain a dot ["+className+"]");
        }

        return className.substring( index + 1 );
    }


    private Element getClassElement( final String className ) {
        Node node = XmlDocument_.getDocumentElement().getFirstChild();
        while( node != null ) {
            if( node instanceof Element ) {
                final Element element = (Element)node;
                if( element.getTagName().equals("class") && className.equals(element.getAttribute("name") )) {
                    return element;
                }
            }
            node = node.getNextSibling();
        }
        getLog().warn("Unexpected class ["+className+"]");
        return null;
    }


    private boolean isEnabled( final Element element ) {
        assertNotNull("element", element);
        return true;
    }


    private Element getElementByTypeAndName( final Element root, final String type, final String name ) {
        assertNotNull("type", type);
        assertNotNull("name", name);

        Node node = root.getFirstChild();
        while( node != null ) {
            if( node instanceof Element ) {
                final Element element = (Element)node;
                if( element.getTagName().equals(type) && name.equals(element.getAttribute("name") ) ) {
                    return element;
                }
            }
            node = node.getNextSibling();
        }
        return null;
    }
}
