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
package com.gargoylesoftware.htmlunit;

import java.util.HashSet;
import java.util.Set;


/**
 * Contains information about a plugin as available in javascript through document.navigator.plugins
 * as well as the associated mime types (for Firefox browser simulation).
 * @version $Revision$
 * @author Marc Guillemot
 *
 * @see <a href="http://www.xulplanet.com/references/objref/Plugin.html">XUL Planet</a>
 */
public class PluginConfiguration {
    private String description_;
    private String filename_;
    private String name_;
    private Set mimeTypes_ = new HashSet();
    
    /**
     * Holds information about a single mime type associated with a plugin
     */
    public static class MimeType {
        private String description_;
        private String suffixes_;
        private String type_;

        /**
         * C'tor initializing fields
         * @param type the mime type
         * @param description the type description
         * @param suffixes the file suffixes
         */
        public MimeType(final String type, final String description, final String suffixes) {
            type_ = type;
            description_ = description;
            suffixes_ = suffixes;
        }

        /**
         * Return the mime type's description
         * @return the description.
         */
        public String getDescription() {
            return description_;
        }

        /**
         * Return the mime type's suffixes
         * @return the suffixes.
         */
        public String getSuffixes() {
            return suffixes_;
        }

        /**
         * Return the mime type
         * @return the type.
         */
        public String getType() {
            return type_;
        }

        /**
         * @return the hashCode of the type
         */
        public int hashCode() {
            return type_.hashCode();
        }
    }

    /**
     * C'tor initializing fields
     * @param name the plugin name
     * @param description the plugin description
     * @param filename the plugin filename
     */
    public PluginConfiguration(final String name, final String description, final String filename) {
        name_ = name;
        description_ = description;
        filename_ = filename;
    }

    /**
     * Gets the plugin's description
     * @return the description.
     */
    public String getDescription() {
        return description_;
    }

    /**
     * Gets the plugin's file name
     * @return the file name.
     */
    public String getFilename() {
        return filename_;
    }

    /**
     * Gets the plugin's name
     * @return the name.
     */
    public String getName() {
        return name_;
    }
    
    /**
     * Gets the associated mime types
     * @return a set of {@link MimeType}
     */
    public Set getMimeTypes() {
        return mimeTypes_;
    }
    
    /**
     * @return the hashCode of the plugin name
     */
    public int hashCode() {
        return name_.hashCode();
    }
}
