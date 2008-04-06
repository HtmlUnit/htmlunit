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

import com.gargoylesoftware.htmlunit.PluginConfiguration;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * A JavaScript object for a Navigator.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Marc Guillemot
 *
 * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/objects/obj_navigator.asp">
 * MSDN documentation</a>
 */
public final class Navigator extends SimpleScriptable {

    private static final long serialVersionUID = 6741787912716453833L;

    private PluginArray plugins_;
    private MimeTypeArray mimeTypes_;

    /**
     * Create an instance. JavaScript objects must have a default constructor.
     */
    public Navigator() { }

    /**
     * Returns the property "appCodeName".
     * @return the property "appCodeName"
     */
    public String jsxGet_appCodeName() {
        return getBrowserVersion().getApplicationCodeName();
    }

    /**
     * Returns the property "appMinorVersion".
     * @return the property "appMinorVersion"
     */
    public String jsxGet_appMinorVersion() {
        return getBrowserVersion().getApplicationMinorVersion();
    }

    /**
     * Returns the property "appName".
     * @return the property "appName"
     */
    public String jsxGet_appName() {
        return getBrowserVersion().getApplicationName();
    }

    /**
     * Returns the property "appVersion".
     * @return the property "appVersion"
     */
    public String jsxGet_appVersion() {
        return getBrowserVersion().getApplicationVersion();
    }

    /**
     * Returns the language of the browser (for IE).
     * @return the language
     */
    public String jsxGet_browserLanguage() {
        return getBrowserVersion().getBrowserLanguage();
    }

    /**
     * Returns the language of the browser (for Mozilla).
     * @return the language
     */
    public String jsxGet_language() {
        return getBrowserVersion().getBrowserLanguage();
    }

    /**
     * Returns the property "cookieEnabled".
     * @return the property "cookieEnabled"
     */
    public boolean jsxGet_cookieEnabled() {
        return getWindow().getWebWindow().getWebClient().isCookiesEnabled();
    }

    /**
     * Returns the property "cpuClass".
     * @return the property "cpuClass"
     */
    public String jsxGet_cpuClass() {
        return getBrowserVersion().getCpuClass();
    }

    /**
     * Returns the property "onLine".
     * @return the property "onLine"
     */
    public boolean jsxGet_onLine() {
        return getBrowserVersion().isOnLine();
    }

    /**
     * Returns the property "platform".
     * @return the property "platform"
     */
    public String jsxGet_platform() {
        return getBrowserVersion().getPlatform();
    }

    /**
     * Returns the property "systemLanguage".
     * @return the property "systemLanguage"
     */
    public String jsxGet_systemLanguage() {
        return getBrowserVersion().getSystemLanguage();
    }

    /**
     * Returns the property "userAgent".
     * @return the property "userAgent"
     */
    public String jsxGet_userAgent() {
        return getBrowserVersion().getUserAgent();
    }

    /**
     * Returns the property "userLanguage".
     * @return the property "userLanguage"
     */
    public String jsxGet_userLanguage() {
        return getBrowserVersion().getUserLanguage();
    }

    /**
     * Returns an empty array because HtmlUnit does not support embedded objects.
     * @return an empty array
     */
    public Object jsxGet_plugins() {
        initPlugins();
        return plugins_;
    }

    private void initPlugins() {
        if (plugins_ != null) {
            return;
        }
        plugins_ = new PluginArray();
        plugins_.setParentScope(this);
        plugins_.setPrototype(getPrototype(PluginArray.class));

        mimeTypes_ = new MimeTypeArray();
        mimeTypes_.setParentScope(this);
        mimeTypes_.setPrototype(getPrototype(MimeTypeArray.class));

        for (final PluginConfiguration pluginConfig : getBrowserVersion().getPlugins()) {
            final Plugin plugin = new Plugin(pluginConfig.getName(), pluginConfig.getDescription(),
                pluginConfig.getFilename());
            plugin.setParentScope(this);
            plugin.setPrototype(getPrototype(Plugin.class));
            plugins_.add(plugin);

            for (final PluginConfiguration.MimeType mimeTypeConfig : pluginConfig.getMimeTypes()) {
                final MimeType mimeType = new MimeType(mimeTypeConfig.getType(), mimeTypeConfig.getDescription(),
                    mimeTypeConfig.getSuffixes(), plugin);
                mimeType.setParentScope(this);
                mimeType.setPrototype(getPrototype(MimeType.class));
                mimeTypes_.add(mimeType);
                plugin.add(mimeType);
            }
        }
    }

    /**
     * Returns an empty array because HtmlUnit does not support embedded objects.
     * @return an empty array
     */
    public Object jsxGet_mimeTypes() {
        initPlugins();
        return mimeTypes_;
    }

    /**
     * Returns <tt>false</tt> always as Java support is not enabled in HtmlUnit.
     * @return false
     */
    public boolean jsxFunction_javaEnabled() {
        return false;
    }

    /**
     * Returns <tt>false</tt> always as data tainting support is not enabled in HtmlUnit.
     * @return false
     */
    public boolean jsxFunction_taintEnabled() {
        return false;
    }
}
