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
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for XMLDOMParseError.
 * @see <a href="http://msdn2.microsoft.com/en-us/library/ms757019.aspx">MSDN documentation</a>
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class XMLDOMParseError extends SimpleScriptable {

    private static final long serialVersionUID = 8435555857574741660L;

    private int errorCode_;
    private int filepos_;
    private int line_;
    private int linepos_;
    private String reason_ = "";
    private String srcText_ = "";
    private String url_ = "";
    
    /**
     * JavaScript constructor.
     */
    public void jsConstructor() {
        // Empty.
    }
    
    /**
     * Returns the error code of the last parse error.
     * @return the error code of the last parse error
     */
    public int jsxGet_errorCode() {
        return errorCode_;
    }

    /**
     * Returns the absolute file position where the error occurred.
     * @return the absolute file position where the error occurred
     */
    public int jsxGet_filepos() {
        return filepos_;
    }
    /**
     * Returns the line number that contains the error.
     * @return the line number that contains the error
     */
    public int jsxGet_line() {
        return line_;
    }
    /**
     * Returns the character position within the line where the error occurred.
     * @return the character position within the line where the error occurred
     */
    public int jsxGet_linepos() {
        return linepos_;
    }

    /**
     * Returns the reason for the error.
     * @return the reason for the error
     */
    public String jsxGet_reason() {
        return reason_;
    }

    /**
     * Returns the full text of the line containing the error.
     * @return the full text of the line containing the error
     */
    public String jsxGet_srcText() {
        return srcText_;
    }

    /**
     * Returns the URL of the XML document containing the last error.
     * @return the URL of the XML document containing the last error
     */
    public String jsxGet_url() {
        return url_;
    }

    void setErrorCode(final int errorCode) {
        this.errorCode_ = errorCode;
    }

    void setFilepos(final int filepos) {
        this.filepos_ = filepos;
    }

    void setLine(final int line) {
        this.line_ = line;
    }

    void setLinepos(final int linepos) {
        this.linepos_ = linepos;
    }

    void setReason(final String reason) {
        this.reason_ = reason;
    }

    void setSrcText(final String srcText) {
        this.srcText_ = srcText;
    }

    void setUrl(final String url) {
        this.url_ = url;
    }
}
