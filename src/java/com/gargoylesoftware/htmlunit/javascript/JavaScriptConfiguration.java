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
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * A container for all the javascript configuration information.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public final class JavaScriptConfiguration {
    private org.w3c.dom.Document xmlDocument_;
    private static Map configurationMap_ = new HashMap(11);

    private JavaScriptConfiguration( final BrowserVersion browserVersion ) {

        try {
            final InputStream inputStream = getConfigurationFileAsStream();
            if( inputStream == null ) {
                getLog().error("Unable to load JavaScriptConfiguration.xml");
                return;
            }
            final InputSource inputSource = new InputSource(inputStream);

            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware( true );
            factory.setValidating( false );

            final DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            documentBuilder.setErrorHandler( new StrictErrorHandler() );

            xmlDocument_ = documentBuilder.parse( inputSource );
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
    }


    /**
     * Return the instance that represents the configuration for the specified {@link BrowserVersion}.
     * @param browserVersion The {@link BrowserVersion}
     * @return The instance for the specified {@link BrowserVersion}
     */
    public static JavaScriptConfiguration getInstance( final BrowserVersion browserVersion ) {
        JavaScriptConfiguration configuration
            = (JavaScriptConfiguration)configurationMap_.get(browserVersion);

        if( configuration == null ) {
            configuration = new JavaScriptConfiguration(browserVersion);
            configurationMap_.put( browserVersion, configuration );
        }
        return configuration;
    }


    private Log getLog() {
        return LogFactory.getLog(getClass());
    }


    /**
     * Return a list containing all the names of the writable properties for the specified class.
     * @param hostClass The class for which we are getting names.
     * @return A list of names.
     */
    public List getWritablePropertyNames( final Class hostClass ) {
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
     * Return a list containing all the names of the readable properties for the specified class.
     * @param hostClass The class for which we are getting names.
     * @return A list of names.
     */
    public List getReadablePropertyNames( final Class hostClass ) {
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
     * Return a list containing all the names of the functions for the specified class.
     * @param hostClass The class for which we are getting names.
     * @return A list of names.
     */
    public List getFunctionNames( final Class hostClass ) {
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
        InputStream inputStream = getClass().getResourceAsStream(fileName);
        if( inputStream == null ) {
            try {
                final String localizedFileName = fileName.replace( '/', File.separatorChar );
                inputStream = new FileInputStream( localizedFileName );
            }
            catch( final IOException e ) {
                // Fall through
            }
        }
        return inputStream;
    }
}
