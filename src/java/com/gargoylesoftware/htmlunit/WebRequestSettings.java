/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit;

import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ClassUtils;

/**
 * Parameter object for making web requests
 * 
 * @version $Revision$
 * @author Brad Clarke
 * @author Hans Donner
 */
public class WebRequestSettings {
    private URL url_;
    private SubmitMethod submitMethod_ = SubmitMethod.GET;
    private FormEncodingType encodingType_ = FormEncodingType.URL_ENCODED;
    private List requestParameters_ = Collections.EMPTY_LIST;

    /**
     * @param target The URL for this request
     */
    public WebRequestSettings(final URL target) {
        this.url_ = target;
    }
    /**
     * @return the URL
     */
    public URL getURL() {
        return url_;
    }
    /**
     * @param url The new URL
     * @return This object
     */
    public WebRequestSettings setURL(final URL url) {
        this.url_ = url;
        return this;
    }

    /**
     * @return Returns the encodingType.
     */
    public FormEncodingType getEncodingType() {
        return encodingType_;
    }
    /**
     * @param encodingType The encodingType to set.
     * @return This object
     */
    public WebRequestSettings setEncodingType(final FormEncodingType encodingType) {
        this.encodingType_ = encodingType;
        return this;
    }
    /**
     * @return Returns the requestParameters.
     */
    public List getRequestParameters() {
        return requestParameters_;
    }
    /**
     * @param requestParameters The requestParameters to set.
     * @return This object
     */
    public WebRequestSettings setRequestParameters(final List requestParameters) {
        this.requestParameters_ = requestParameters;
        return this;
    }
    /**
     * @return Returns the submitMethod.
     */
    public SubmitMethod getSubmitMethod() {
        return submitMethod_;
    }
    /**
     * @param submitMethod The submitMethod to set.
     * @return This object
     */
    public WebRequestSettings setSubmitMethod(final SubmitMethod submitMethod) {
        this.submitMethod_ = submitMethod;
        return this;
    }

    /**
     * Return a string representation of this object
     * @return  See above
     */
    public String toString() {
        final StringBuffer buffer = new StringBuffer();

        buffer.append(ClassUtils.getShortClassName(getClass()));

        buffer.append("[<");
        buffer.append("url=\"" + url_.toExternalForm() + "\"");
        buffer.append(", " + submitMethod_);
        buffer.append(", " + encodingType_);
        buffer.append(", " + requestParameters_);
        buffer.append(">]");

        return buffer.toString();
    }

}
