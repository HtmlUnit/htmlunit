/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.file;

import java.util.ArrayList;

import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSymbol;

/**
 * A JavaScript object for {@code FileList}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class FileList extends HtmlUnitScriptable {

    private ArrayList<File> files_;

    /**
     * Creates an instance.
     */
    public FileList() {
        super();
    }

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
        // nothing to do
    }

    /**
     * Creates a new instance.
     * @param array the array of files
     */
    public FileList(final java.io.File[] array) {
        super();
        files_ = new ArrayList<>();

        for (int i = 0; i < array.length; i++) {
            files_.add(new File(array[i].getAbsolutePath()));
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span>
     * Update the backing file array.
     * @param files the new files list
     */
    public void updateFiles(final ArrayList<File> files) {
        files_ = files;
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
        return files_.size();
    }

    /**
     * Returns a {@code File} object representing the file at the specified index in the file list.
     * @param index The zero-based index of the file to retrieve from the list
     * @return The {@code File} representing the requested file
     */
    @JsxFunction
    public File item(final int index) {
        if (index >= 0 && index < files_.size()) {
            return files_.get(index);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final int index, final Scriptable start) {
        if (this == start) {
            if (index >= 0 && index < files_.size()) {
                return files_.get(index);
            }
        }
        return super.get(index, start);
    }

    /**
     * Returns an Iterator allowing to go through all keys contained in this object.
     * @return a NativeArrayIterator
     */
    @JsxSymbol(symbolName = "iterator")
    public Scriptable values() {
        return JavaScriptEngine.newArrayIteratorTypeValues(getParentScope(), this);
    }
}
