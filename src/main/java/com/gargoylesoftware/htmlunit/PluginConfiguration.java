/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Contains information about a plugin as available in JavaScript via <tt>document.navigator.plugins</tt>,
 * as well as the associated mime types (for Firefox browser simulation).
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @see <a href="http://www.xulplanet.com/references/objref/Plugin.html">XUL Planet Documentation</a>
 */
public class PluginConfiguration implements Serializable {

    private static final long serialVersionUID = -3160049910272683027L;

    private final String description_;
    private final String filename_;
    private final String name_;
    private final Set<PluginConfiguration.MimeType> mimeTypes_ = new HashSet<PluginConfiguration.MimeType>();

    /**
     * Creates a new instance.
     * @param name the plugin name
     * @param description the plugin description
     * @param filename the plugin filename
     */
    public PluginConfiguration(final String name, final String description, final String filename) {
        WebAssert.notNull("name", name);
        name_ = name;
        description_ = description;
        filename_ = filename;
    }

    /**
     * Gets the plugin's description.
     * @return the description
     */
    public String getDescription() {
        return description_;
    }

    /**
     * Gets the plugin's file name.
     * @return the file name
     */
    public String getFilename() {
        return filename_;
    }

    /**
     * Gets the plugin's name.
     * @return the name
     */
    public String getName() {
        return name_;
    }

    /**
     * Gets the associated mime types.
     * @return a set of {@link MimeType}
     */
    public Set<PluginConfiguration.MimeType> getMimeTypes() {
        return mimeTypes_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return name_.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof PluginConfiguration)) {
            return false;
        }
        final PluginConfiguration other = (PluginConfiguration) o;
        return name_.equals(other.name_);
    }

    /**
     * Holds information about a single mime type associated with a plugin.
     */
    public static class MimeType implements Serializable {

        private static final long serialVersionUID = 3775313058008352464L;

        private final String description_;
        private final String suffixes_;
        private final String type_;

        /**
         * Creates a new instance.
         * @param type the mime type
         * @param description the type description
         * @param suffixes the file suffixes
         */
        public MimeType(final String type, final String description, final String suffixes) {
            WebAssert.notNull("type", type);
            type_ = type;
            description_ = description;
            suffixes_ = suffixes;
        }

        /**
         * Returns the mime type's description.
         * @return the description
         */
        public String getDescription() {
            return description_;
        }

        /**
         * Returns the mime type's suffixes.
         * @return the suffixes
         */
        public String getSuffixes() {
            return suffixes_;
        }

        /**
         * Returns the mime type.
         * @return the type
         */
        public String getType() {
            return type_;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return type_.hashCode();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof PluginConfiguration.MimeType)) {
                return false;
            }
            final PluginConfiguration.MimeType other = (PluginConfiguration.MimeType) o;
            return type_.equals(other.type_);
        }
    }

}
