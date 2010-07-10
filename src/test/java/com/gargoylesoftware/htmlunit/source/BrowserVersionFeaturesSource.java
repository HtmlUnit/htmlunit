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
package com.gargoylesoftware.htmlunit.source;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;

/**
 * {@link BrowserVersionFeatures} utilities.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public final class BrowserVersionFeaturesSource {

    private File root_;

    /**
     * Creates a new instance.
     *
     * @param root HtmlUnit root directory
     */
    public BrowserVersionFeaturesSource(final File root) {
        root_ = root;
    }

    /**
     * Rename the name specified {@link BrowserVersionFeatures} to the new one, this is applicable only
     * to source folders, not to the test ones.
     *
     * @param features the feature to rename
     * @param newName the new name
     * @throws IOException if an error occurs
     */
    public void rename(final BrowserVersionFeatures features, final String newName) throws IOException {
        rename(new File(root_, "src/main/java"), features, newName);
        final File propertiesFolder =  new File(root_,
                "src/main/resources/com/gargoylesoftware/htmlunit/javascript/configuration");
        for (final File f : propertiesFolder.listFiles(new FileFilter() {
            public boolean accept(final File pathname) {
                return pathname.getName().endsWith(".properties");
            }
        })) {
            rename(f, features.name(), newName);
        }
    }

    private void rename(final File dir, final BrowserVersionFeatures features, final String newName)
        throws IOException {
        for (final File f : dir.listFiles()) {
            if (f.isDirectory()) {
                if (!f.getName().equals(".svn")) {
                    rename(f, features, newName);
                }
            }
            else {
                rename(f, features.name(), newName);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void rename(final File file, final String oldName, final String newName) throws IOException {
        final List<String> lines = FileUtils.readLines(file);
        boolean modified = false;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains(oldName)) {
                line = line.replace(oldName, newName);
                lines.set(i, line);
                modified = true;
            }
        }
        if (modified) {
            FileUtils.writeLines(file, lines);
        }
    }
}
