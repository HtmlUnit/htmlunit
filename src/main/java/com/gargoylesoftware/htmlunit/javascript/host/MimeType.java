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
 * A JavaScript object for a document.navigator.mimeTypes elements.
 *
 * @version $Revision$
 * @author Marc Guillemot
 *
 * @see <a href="http://www.xulplanet.com/references/objref/MimeType.html">XUL Planet</a>
 */
public final class MimeType extends SimpleScriptable {
    private static final long serialVersionUID = -4673239005661544554L;
    private String description_;
    private String suffixes_;
    private String type_;
    private Plugin enabledPlugin_;

    /**
     * Create an instance. JavaScript objects must have a default constructor.
     */
    public MimeType() {
        // nothing
    }

    /**
     * Constructor initializing fields.
     * @param type the mime type
     * @param description the type description
     * @param suffixes the file suffixes
     * @param plugin the associated plugin
     */
    public MimeType(final String type, final String description, final String suffixes, final Plugin plugin) {
        type_ = type;
        description_ = description;
        suffixes_ = suffixes;
        enabledPlugin_ = plugin;
    }

    /**
     * Returns the mime type's description.
     * @return the description.
     */
    public String jsxGet_description() {
        return description_;
    }

    /**
     * Returns the mime type's suffixes.
     * @return the suffixes.
     */
    public String jsxGet_suffixes() {
        return suffixes_;
    }

    /**
     * Returns the mime type's suffixes.
     * @return the suffixes.
     */
    public String jsxGet_type() {
        return type_;
    }

    /**
     * Returns the mime type's associated plugin.
     * @return the plugin.
     */
    public Object jsxGet_enabledPlugin() {
        return enabledPlugin_;
    }
}
