package com.gargoylesoftware.htmlunit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import org.apache.xerces.util.XMLAttributesImpl;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.cyberneko.html.HTMLConfiguration;
import org.cyberneko.html.filters.DefaultFilter;
import org.cyberneko.html.filters.Identity;
import org.cyberneko.html.filters.Writer;

/**
 *  Originally based on a sample written by Andy Clark
 */
public final class ScriptFilter extends DefaultFilter {

    private HTMLConfiguration configuration_;
    private StringBuffer scriptBuffer_;
    private String systemId_;
    private int scriptCount_;
    private String scriptType_;

    /**
     *  Create an instance
     *
     * @param  config
     */
    public ScriptFilter( final HTMLConfiguration config ) {
        assertNotNull("config", config);
        configuration_ = config;
    }


    /**
     *  Start document.
     */
    public void startDocument(
            final XMLLocator locator,
            final String encoding,
            final Augmentations augmentations )
        throws
            XNIException {

        scriptBuffer_ = null;
        systemId_ = locator != null ? locator.getLiteralSystemId() : null;
        scriptCount_ = 0;
        super.startDocument( locator, encoding, augmentations );
    }


    /**
     *  Start element.
     */
    public void startElement(
            final QName element,
            final XMLAttributes attrs,
            final Augmentations augmentations )
        throws
            XNIException {

        if( element.rawname.equalsIgnoreCase( "script" ) ) {
            scriptType_ = attrs.getValue( "type" );
            if( scriptType_ == null ) {
                scriptType_ = "";
            }
            scriptBuffer_ = new StringBuffer();
        }
        else {
            super.startElement( element, attrs, augmentations );
        }
    }


    /**
     *  Empty element.
     */
    public void emptyElement(
            final QName element,
            final XMLAttributes attrs,
            final Augmentations augmentations )
        throws
            XNIException {

        if( element.rawname.equalsIgnoreCase( "script" ) ) {
            return;
        }
        super.emptyElement( element, attrs, augmentations );
    }


    /**
     *  Characters.
     */
    public void characters(
            final XMLString text,
            final Augmentations augmentations )
        throws
            XNIException {

        if( scriptBuffer_ == null ) {
            super.characters( text, augmentations );
        }
        else {
            scriptBuffer_.append( text.ch, text.offset, text.length );
        }
    }


    /**
     *  End element.
     */
    public void endElement(
            final QName element,
            final Augmentations augmentations )
        throws
            XNIException {

        if( scriptBuffer_ != null ) {
            if( element.rawname.equalsIgnoreCase( "script" ) == false ) {
                throw new IllegalStateException("Other elements were contained within the script tag");
            }

            try {
                final String script = scriptBuffer_.toString();
                System.out.println();
                System.out.println("=================== SCRIPT START ======================");
                System.out.println(script);
                System.out.println("=================== SCRIPT END   ======================");

                final String result = executeScript(script);
                if( result.length() != 0 ) {
                    final XMLInputSource xmlInputSource = new XMLInputSource(
                        null, systemId_, null, new StringReader( scriptBuffer_.toString() ), "UTF-8" );
                    configuration_.pushInputSource( xmlInputSource );
                }
            }
            finally {
                scriptBuffer_ = null;
            }
        }
        else {
            super.endElement( element, augmentations );
        }
    }


    private void assertNotNull( final String description, final Object object ) {
        if( object == null ) {
            throw new NullPointerException(description);
        }
    }


    private String executeScript( final String script ) {
        return "";
    }
}

