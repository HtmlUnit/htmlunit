/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.file;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import java.util.List;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * A JavaScript object for {@code FileList}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class FileList extends SimpleScriptable {

    private File[] files_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public FileList() {
    }

    /**
     * Creates a new instance.
     * @param pathnames the path names
     */
    public FileList(final List<java.io.File> pathnames) {
        files_ = new File[pathnames.size()];

        int i = 0;
        for (java.io.File file : pathnames) {
            files_[i] = new File(file.getAbsolutePath());
            i++;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setParentScope(final Scriptable m) {
        super.setParentScope(m);
        if (files_ != null) {
            for (final File file : files_) {
                file.setParentScope(m);
                file.setPrototype(getPrototype(file.getClass()));
            }
        }
    }

    /**
     * Returns the {@code length} property.
     * @return the {@code length} property
     */
    @JsxGetter
    public int getLength() {
        return files_.length;
    }

    /**
     * Returns a {@code File} object representing the file at the specified index in the file list.
     * @param index The zero-based index of the file to retrieve from the list
     * @return The {@code File} representing the requested file
     */
    @JsxFunction
    public File item(final int index) {
        return files_[index];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final int index, final Scriptable start) {
        return item(index);
    }
}
