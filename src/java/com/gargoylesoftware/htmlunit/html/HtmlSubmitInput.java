/*
 *  Copyright (C) 2002 Gargoyle Software. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import java.io.IOException;
import org.w3c.dom.Element;

/**
 *  Wrapper for the html element "input"
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class HtmlSubmitInput extends HtmlInput {

    /**
     *  Create an instance
     *
     * @param  page The page that contains this element
     * @param  element the xml element that represents this tag
     */
    HtmlSubmitInput( final HtmlPage page, final Element element ) {
        super( page, element );
    }


    /**
     *  Submit the form that contains this input
     *
     * @deprecated Use {@link #click()} instead
     * @return  The Page that is the result of submitting this page to the
     *      server
     * @exception  IllegalStateException If the type of this input isn't
     *      "submit"
     * @exception  IOException If an io error occurs
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     */
    public Page submit()
        throws
            IllegalStateException,
            IOException,
            ElementNotFoundException {

        return click();
    }


    /**
     *  Submit the form that contains this input
     *
     * @return  The Page that is the result of submitting this page to the
     *      server
     * @exception  IllegalStateException If the type of this input isn't
     *      "submit"
     * @exception  IOException If an io error occurs
     * @exception  ElementNotFoundException If a particular xml element could
     *      not be found in the dom model
     */
    public Page click()
        throws
            IllegalStateException,
            IOException,
            ElementNotFoundException {

        final String onClick = getOnClickAttribute();
        final HtmlPage page = getPage();
        if( onClick.length() == 0 || page.getWebClient().isJavaScriptEnabled() == false ) {
            return getEnclosingFormOrDie().submit(this);
        }
        else {
            return page.executeJavascriptIfPossible(onClick, "HtmlSubmitInput onClick handler");
        }
    }
}

