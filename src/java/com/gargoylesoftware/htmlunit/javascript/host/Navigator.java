/*
 * Copyright (c) 2002, 2004 Gargoyle Software Inc. All rights reserved.
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
    public String jsGet_appCodeName() {
        return getBrowserVersion().getApplicationCodeName();
    }


    /**
     * Return the property "appMinorVersion".
     * @return the property "appMinorVersion".
     */
    public String jsGet_appMinorVersion() {
        return getBrowserVersion().getApplicationMinorVersion();
    }


    /**
     * Return the property "appName".
     * @return the property "appName".
     */
    public String jsGet_appName() {
        return getBrowserVersion().getApplicationName();
    }


    /**
     * Return the property "appVersion".
     * @return the property "appVersion".
     */
    public String jsGet_appVersion() {
        return getBrowserVersion().getApplicationVersion();
    }


    /**
     * Return the property "browserLanguage".
     * @return the property "browserLanguage".
     */
    public String jsGet_browserLanguage() {
        return getBrowserVersion().getBrowserLanguage();
    }


    /**
     * Return the property "cookieEnabled".
     * @return the property "cookieEnabled".
     */
    public boolean jsGet_cookieEnabled() {
        return true;
    }


    /**
     * Return the property "cpuClass".
     * @return the property "cpuClass".
     */
    public String jsGet_cpuClass() {
        return getBrowserVersion().getCpuClass();
    }

    /**
     * Return the property "onLine".
     * @return the property "onLine".
     */
    public boolean jsGet_onLine() {
        return getBrowserVersion().isOnLine();
    }


    /**
     * Return the property "platform".
     * @return the property "platform".
     */
    public String jsGet_platform() {
        return getBrowserVersion().getPlatform();
    }


    /**
     * Return the property "systemLanguage".
     * @return the property "systemLanguage".
     */
    public String jsGet_systemLanguage() {
        return getBrowserVersion().getSystemLanguage();
    }


    /**
     * Return the property "userAgent".
     * @return The property "userAgent".
     */
    public String jsGet_userAgent() {
        return getBrowserVersion().getUserAgent();
    }


    /**
     * Return the property "userLanguage".
     * @return the property "userLanguage".
     */
    public String jsGet_userLanguage() {
        return getBrowserVersion().getUserLanguage();
    }


    /**
     * Return an empty array because HtmlUnit does not support embedded objects.
     * @return an empty array.
     */
    public Object jsFunction_plugins() {
        return new NativeArray(0);
    }


    /**
     * Return <tt>false</tt> always as Java support is not enabled in HtmlUnit.
     * @return false.
     */
    public boolean jsFunction_javaEnabled() {
        return false;
    }


    /**
     * Return <tt>false</tt> always as data tainting support is not enabled in HtmlUnit.
     * @return false.
     */
    public boolean jsFunction_taintEnabled() {
        return false;
    }


    /**
     * Returns the default browser version.
     * @return the default browser version.
     */
    private BrowserVersion getBrowserVersion() {
        return BrowserVersion.getDefault();
    }

}
