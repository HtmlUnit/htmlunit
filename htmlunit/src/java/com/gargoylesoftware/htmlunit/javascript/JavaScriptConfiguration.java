/*
 * Copyright (c) 2002, 2003 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
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

    /** Constant indicating that this function/property is used by the specified browser version */
    public static final int ENABLED   = 1;

    /** Constant indicating that this function/property is not used by the specified browser version */
    public static final int DISABLED  = 2;

    /** Constant indicating that this function/property is not defined in the configuration file */
    public static final int NOT_FOUND = 3;

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


    private int getState(
            final Class hostClass,
            final String type,
            final String typeName,
            final String booleanAttributeName ) {

        Assert.notNull("hostClass", hostClass);
        Assert.notNull("typeName", typeName);

        Element classElement = getClassElement(getClassName(hostClass));
        while( classElement != null ) {

            final Element functionElement = getElementByTypeAndName(classElement, type, typeName);
            if( functionElement != null ) {
                if( booleanAttributeName == null ) {
                    return getEnabledState(functionElement);
                }
                else if( getBooleanAttribute(functionElement,booleanAttributeName ) )  {
                    return getEnabledState(functionElement);
                }
                else {
                    return NOT_FOUND;
                }
            }

            final String superClassName = classElement.getAttribute("extends");
            classElement = null;
            if( superClassName != null && superClassName.length() != 0 ) {
                classElement = getClassElement(superClassName);
            }
        }

        return NOT_FOUND;
    }


    /**
     * Return the state of the specified readable property.
     *
     * @param hostClass The class for which this property is defined.
     * @param name The name of the property.
     * @return {@link #ENABLED}, {@link #DISABLED} or {@link #NOT_FOUND}
     */
    public int getReadablePropertyNameState( final Class hostClass, final String name ) {
        return getState(hostClass, "property", name, "readable");
    }


    /**
     * Return the state of the specified writable property.
     *
     * @param hostClass The class for which this property is defined.
     * @param name The name of the property.
     * @return {@link #ENABLED}, {@link #DISABLED} or {@link #NOT_FOUND}
     */
    public int getWritablePropertyNameState( final Class hostClass, final String name ) {
        return getState(hostClass, "property", name, "writable");
    }


    /**
     * Return the state of the specified function.
     *
     * @param hostClass The class for which this function is defined.
     * @param name The name of the property.
     * @return {@link #ENABLED}, {@link #DISABLED} or {@link #NOT_FOUND}
     */
    public int getFunctionNameState( final Class hostClass, final String name ) {
        return getState(hostClass, "function", name, null);
    }


    private boolean getBooleanAttribute( final Element classElement, final String attributeName ) {
        Assert.notNull("classElement", classElement);
        Assert.notNull("attributeName", attributeName);

        final String value = classElement.getAttribute(attributeName);
        if( "true".equals(value) ) {
            return true;
        }
        else if( "false".equals(value) ) {
            return false;
        }
        else {
            throw new IllegalStateException("Unexpected value for attribute ["
                +attributeName+"] on element ["+classElement+"] value=["+value+"]");
        }
    }


    private InputStream getConfigurationFileAsStream() {
        final String fileName = "/com/gargoylesoftware/htmlunit/javascript/JavaScriptConfiguration.xml";
        return getResourceAsStream(fileName);
    }


    private InputStream getResourceAsStream( final String name ) {
        Assert.notNull("name", name);

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

        // If we are running maven tests then the path will be slightly different
        if( inputStream == null ) {
            try {
                final String localizedName = ("./src/java"+name).replace( '/', File.separatorChar );
                inputStream = new FileInputStream( localizedName );
            }
            catch( final IOException e ) {
                // Fall through
            }
        }
        return inputStream;
    }


    private String getClassName( final Class hostClass ) {
        Assert.notNull("hostClass", hostClass);

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


    private int getEnabledState( final Element element ) {
        Assert.notNull("element", element);
        return ENABLED;
    }


    private Element getElementByTypeAndName( final Element root, final String type, final String name ) {
        Assert.notNull("type", type);
        Assert.notNull("name", name);

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
