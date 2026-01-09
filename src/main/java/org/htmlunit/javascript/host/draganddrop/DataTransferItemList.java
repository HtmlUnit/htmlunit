/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.draganddrop;

import java.util.ArrayList;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSymbol;
import org.htmlunit.javascript.host.file.File;
import org.htmlunit.javascript.host.file.FileList;

/**
 * A JavaScript object for {@code DataTransferItemList}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class DataTransferItemList extends HtmlUnitScriptable {

    private ArrayList<DataTransferItem> items_;
    private FileList fileList_;

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
        // nothing to do
    }

    /**
     * @return the {@code length} property
     */
    @JsxGetter
    public int getLength() {
        if (items_ == null) {
            return 0;
        }
        return items_.size();
    }

    /**
     * Creates a new {@link DataTransferItem} using the specified data and adds it to the drag data list.
     * The item may be a {@link File} or a string of a given type. If the item is successfully added to the list,
     * the newly-created {@link DataTransferItem} object is returned.
     *
     * @param context the JavaScript context
     * @param scope the scope
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return the newly-created {@link DataTransferItem} object
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms536782.aspx">MSDN documentation</a>
     */
    @JsxFunction
    public static DataTransferItem add(final Context context, final Scriptable scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        final DataTransferItemList itemList = (DataTransferItemList) thisObj;
        if (args.length == 1) {
            if (args[0] instanceof File file) {
                final DataTransferItem item = DataTransferItem.buildFileItem(file);
                item.setParentScope(itemList.getParentScope());
                item.setPrototype(itemList.getPrototype(item.getClass()));

                if (itemList.items_ == null) {
                    itemList.items_ = new ArrayList<>();
                }
                itemList.items_.add(item);
                itemList.updateFileList();

                return item;
            }
            throw JavaScriptEngine.typeError(
                    "Failed to execute 'add' on 'DataTransferItemList': parameter 1 is not of type 'File'.");
        }

        if (args.length > 1) {
            final String data = JavaScriptEngine.toString(args[0]);
            final String type = JavaScriptEngine.toString(args[1]);
            final DataTransferItem item = DataTransferItem.buildStringItem(data, type);
            item.setParentScope(itemList.getParentScope());
            item.setPrototype(itemList.getPrototype(item.getClass()));

            if (itemList.items_ == null) {
                itemList.items_ = new ArrayList<>();
            }
            itemList.items_.add(item);

            return item;
        }

        throw JavaScriptEngine.typeError(
                "Failed to execute 'add' on 'DataTransferItemList' - no args provided.");
    }

    /**
     * Removes all DataTransferItem objects from the drag data items list, leaving the list empty.
     */
    @JsxFunction
    public void clear() {
        if (items_ != null) {
            items_.clear();
            if (fileList_ != null) {
                fileList_.updateFiles(new ArrayList<>());
            }
        }
    }

    /**
     * Removes the DataTransferItem at the specified index from the list. If the index is less
     * than zero or greater than one less than the length of the list, the list will not be changed.
     * @param index the zero-based index number of the item in the drag data list to remove.
     *        If the index doesn't correspond to an existing item in the list, the list is left unchanged.
     */
    @JsxFunction
    public void remove(final int index) {
        if (items_ != null) {
            if (index >= 0 && index < items_.size()) {
                items_.remove(index);
                updateFileList();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final int index, final Scriptable start) {
        if (this == start) {
            if (index >= 0 && index < items_.size()) {
                return items_.get(index);
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

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span>
     * Maintains the file list for the parent DataTrasnfer object.
     * @return the {@code files} property
     */
    public FileList getFiles() {
        if (fileList_ == null) {
            final FileList list = new FileList(new java.io.File[0]);
            list.setParentScope(getParentScope());
            list.setPrototype(getPrototype(list.getClass()));
            fileList_ = list;

            if (items_ != null) {
                updateFileList();
            }
        }
        return fileList_;
    }

    private void updateFileList() {
        if (fileList_ != null) {
            final ArrayList<File> files = new ArrayList<>();
            for (final DataTransferItem item : items_) {
                if (item.isFile()) {
                    files.add(item.getAsFile());
                }
            }
            fileList_.updateFiles(files);
        }
    }
}
