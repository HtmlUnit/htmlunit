/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A generic page that will be returned for any text related content.
 * Specifically any content types that start with {@code text/}
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Ronald Brill
 * @author Ahmed Ashour
 */
public class TextPage extends AbstractPage {

    /**
     * Creates an instance.
     *
     * @param webResponse the response from the server
     * @param enclosingWindow the window that holds the page
     */
    public TextPage(final WebResponse webResponse, final WebWindow enclosingWindow) {
        super(webResponse, enclosingWindow);
    }

    /**
     * Returns the content of this page.
     *
     * @return the content of this page
     */
    public String getContent() {
        return getWebResponse().getContentAsString();
    }

    /**
     * Saves the content of this page to a text file.
     *
     * @param file file to write this page into
     * @throws IOException If an error occurs
     */
    public void save(final File file) throws IOException {
        final Path savePath = file.toPath();
        final String text = getContent();
        final Charset charset = getWebResponse().getContentCharset();
        Files.write(savePath, text.getBytes(charset));
    }
}
