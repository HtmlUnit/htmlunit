/*
 * Copyright (c) 2002-2006 Gargoyle Software Inc. All rights reserved.
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

import org.mozilla.javascript.NativeArray;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A javascript object for a Navigator.
 * 
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Chris Erskine
 * 
 * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/objects/obj_navigator.asp">
 * MSDN documentation</a>
 */
public final class Navigator extends SimpleScriptable {

    private static final long serialVersionUID = 6741787912716453833L;


    /**
     * Create an instance. Javascript objects must have a default constructor.
     */
    public Navigator() {}


    /**
     * Return the property "appCodeName".
     * @return the property "appCodeName".
     */
    public String jsxGet_appCodeName() {
        return getBrowserVersion().getApplicationCodeName();
    }


    /**
     * Return the property "appMinorVersion".
     * @return the property "appMinorVersion".
     */
    public String jsxGet_appMinorVersion() {
        return getBrowserVersion().getApplicationMinorVersion();
    }


    /**
     * Return the property "appName".
     * @return the property "appName".
     */
    public String jsxGet_appName() {
        return getBrowserVersion().getApplicationName();
    }


    /**
     * Return the property "appVersion".
     * @return the property "appVersion".
     */
    public String jsxGet_appVersion() {
        return getBrowserVersion().getApplicationVersion();
    }


    /**
     * Return the language of the browser (for IE).
     * @return the language.
     */
    public String jsxGet_browserLanguage() {
        return getBrowserVersion().getBrowserLanguage();
    }

    /**
     * Return the language of the browser (for Mozilla).
     * @return the language.
     */
    public String jsxGet_language() {
        return getBrowserVersion().getBrowserLanguage();
    }

    /**
     * Return the property "cookieEnabled".
     * @return the property "cookieEnabled".
     */
    public boolean jsxGet_cookieEnabled() {
        return true;
    }


    /**
     * Return the property "cpuClass".
     * @return the property "cpuClass".
     */
    public String jsxGet_cpuClass() {
        return getBrowserVersion().getCpuClass();
    }

    /**
     * Return the property "onLine".
     * @return the property "onLine".
     */
    public boolean jsxGet_onLine() {
        return getBrowserVersion().isOnLine();
    }


    /**
     * Return the property "platform".
     * @return the property "platform".
     */
    public String jsxGet_platform() {
        return getBrowserVersion().getPlatform();
    }


    /**
     * Return the property "systemLanguage".
     * @return the property "systemLanguage".
     */
    public String jsxGet_systemLanguage() {
        return getBrowserVersion().getSystemLanguage();
    }


    /**
     * Return the property "userAgent".
     * @return The property "userAgent".
     */
    public String jsxGet_userAgent() {
        return getBrowserVersion().getUserAgent();
    }


    /**
     * Return the property "userLanguage".
     * @return the property "userLanguage".
     */
    public String jsxGet_userLanguage() {
        return getBrowserVersion().getUserLanguage();
    }


    /**
     * Return an empty array because HtmlUnit does not support embedded objects.
     * @return an empty array.
     */
    public Object jsxFunction_plugins() {
        return new NativeArray(0);
    }


    /**
     * Return <tt>false</tt> always as Java support is not enabled in HtmlUnit.
     * @return false.
     */
    public boolean jsxFunction_javaEnabled() {
        return false;
    }


    /**
     * Return <tt>false</tt> always as data tainting support is not enabled in HtmlUnit.
     * @return false.
     */
    public boolean jsxFunction_taintEnabled() {
        return false;
    }


    /**
     * Returns the default browser version.
     * @return the default browser version.
     */
    private BrowserVersion getBrowserVersion() {
        return getWindow().getWebWindow().getWebClient().getBrowserVersion();
    }

}
