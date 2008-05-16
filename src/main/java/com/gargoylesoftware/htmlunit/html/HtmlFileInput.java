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
package com.gargoylesoftware.htmlunit.html;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.StringUtils;

import com.gargoylesoftware.htmlunit.KeyDataPair;
import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "input".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public class HtmlFileInput extends HtmlInput {

    private static final long serialVersionUID = 7925479292349207154L;
    private String contentType_;

    /**
     * Creates an instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlFileInput(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
        setAttributeValue("value", "");
        if (page.getWebClient().getBrowserVersion().isIE()) {
            setDefaultValue("");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NameValuePair[] getSubmitKeyValuePairs() {
        String value = getValueAttribute();

        if (StringUtils.isEmpty(value)) {
            return new NameValuePair[] {new KeyDataPair(getNameAttribute(), new File(""), null, null)};
        }
        
        File file = null;
        // to tolerate file://
        if (value.startsWith("file:/")) {
            if (value.startsWith("file://") && !value.startsWith("file:///")) {
                value = "file:///" + value.substring(7);
            }
            try {
                file = new File(new URI(value));
            }
            catch (final URISyntaxException e) {
                // nothing here
            }
        }
        
        if (file == null) {
            file = new File(value);
        }

        // contentType and charset are determined from browser and page
        // perhaps it could be interesting to have setters for it in this class
        // to give finer control to user
        final String contentType;
        if (contentType_ == null) {
            contentType = getPage().getWebClient().guessContentType(file);
        }
        else {
            contentType = contentType_;
        }
        final String charset = getPage().getPageEncoding();
        return new NameValuePair[] {new KeyDataPair(getNameAttribute(), file, contentType, charset)};
    }

    /**
     * {@inheritDoc} This method <b>does nothing</b> for file input elements.
     * @see SubmittableElement#reset()
     */
    @Override
    public void reset() {
        // Empty.
    }

    /**
     * {@inheritDoc} Overridden so that this does not set the value attribute when emulating
     * Netscape browsers.
     * @see HtmlInput#setDefaultValue(String)
     */
    @Override
    public void setDefaultValue(final String defaultValue) {
        setDefaultValue(defaultValue, false);
    }

    /**
     * Sets the content type value that should be send together with the uploaded file.
     * If content type is not explicitly set, HtmlUnit will try to guess it from the file content.
     * @param contentType the content type, <code>null</code> resets it
     */
    public void setContentType(final String contentType) {
        contentType_ = contentType;
    }
    
    /**
     * Gets the content type that should be send together with the uploaded file.
     * @return the content type, <code>null</code> if this has not been explicitly set
     * and should be guessed from file content.
     */
    public String getContentType() {
        return contentType_;
    }
}
