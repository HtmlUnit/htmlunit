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
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;

/**
 * {@link BrowserVersionFeatures} utilities.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public final class BrowserVersionFeaturesSource {

    private static final String GENERATED_PREFIX = "GENERATED_";
    private File root_;
    private int generatedNext_;

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

    /**
     * Replace all browserVersion.".isIE()" and ".isFirefox()" with
     *         ".hasFeature(BrowserVersionFeatures.GENERATED_XYZ".
     * @throws IOException if an error occurs
     */
    public void generate() throws IOException {
        generatedNext_ = 1;
        for (final BrowserVersionFeatures feature : BrowserVersionFeatures.values()) {
            final String name = feature.name();
            if (name.startsWith(GENERATED_PREFIX)) {
                generatedNext_ = Integer.parseInt(name.substring(GENERATED_PREFIX.length())) + 1;
            }
        }
        final File folder = new File(root_, "src/main/java");
        generate(folder, ".isIE()", new BrowserVersion[] {
            BrowserVersion.INTERNET_EXPLORER_6, BrowserVersion.INTERNET_EXPLORER_7,
            BrowserVersion.INTERNET_EXPLORER_8 });
        generate(folder, ".isFirefox()", new BrowserVersion[] {
            BrowserVersion.FIREFOX_2, BrowserVersion.FIREFOX_3,
            BrowserVersion.FIREFOX_3_6 });
    }

    @SuppressWarnings("unchecked")
    private void generate(final File dir, final String toSearchFor, final BrowserVersion[] versions)
        throws IOException {
        final File propertiesFolder =  new File(root_,
            "src/main/resources/com/gargoylesoftware/htmlunit/javascript/configuration");
        final File featuresFile = new File(root_,
            "src/main/java/com/gargoylesoftware/htmlunit/BrowserVersionFeatures.java");
        for (final File f : dir.listFiles()) {
            if (f.isDirectory()) {
                if (!f.getName().equals(".svn")) {
                    generate(f, toSearchFor, versions);
                }
            }
            else if (!f.getName().equals("JavaScriptConfiguration.java")) {
                final List<String> lines = FileUtils.readLines(f);
                boolean modified = false;
                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    if (line.contains(toSearchFor)) {
                        line = line.replace(toSearchFor,
                                ".hasFeature(BrowserVersionFeatures." + GENERATED_PREFIX + generatedNext_ + ")");
                        lines.set(i, line);
                        modified = true;

                        final List<String> featuresLines = FileUtils.readLines(featuresFile);
                        featuresLines.add(featuresLines.size() - 4, "");
                        featuresLines.add(featuresLines.size() - 4, "    /** Was originally " + toSearchFor + ". */");
                        featuresLines.add(featuresLines.size() - 4, "    " + GENERATED_PREFIX + generatedNext_ + ",");
                        FileUtils.writeLines(featuresFile, featuresLines);

                        for (final File file : propertiesFolder.listFiles(new FileFilter() {
                            public boolean accept(final File pathname) {
                                for (final BrowserVersion version : versions) {
                                    if (pathname.getName().equals(version.getNickname() + ".properties")) {
                                        return true;
                                    }
                                }
                                return false;
                            }
                        })) {
                            final List<String> features = FileUtils.readLines(file);
                            features.add(GENERATED_PREFIX + generatedNext_);
                            Collections.sort(features);
                            FileUtils.writeLines(file, features);
                        }
                        generatedNext_++;
                    }
                }
                if (modified) {
                    FileUtils.writeLines(f, lines);
                }
            }
        }
    }
}
