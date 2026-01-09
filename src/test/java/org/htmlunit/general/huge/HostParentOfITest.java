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
package org.htmlunit.general.huge;

import java.util.Collection;

import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.params.provider.Arguments;

/**
 * Tests two Host classes, if one prototype is parent of another.
 *
 * This class handles all host names which starts by character 'I' to 'L'.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HostParentOfITest extends HostParentOf {

    /**
     * Returns the parameterized data.
     *
     * @return the parameterized data
     * @throws Exception
     *             if an error occurs
     */
    public static Collection<Arguments> data() throws Exception {
        return HostParentOf.data(input -> {
            final char ch = Character.toUpperCase(input.charAt(0));
            return ch >= 'I' && ch <= 'L';
        });
    }

    @Alerts("true/false")
    void _IDBCursor_IDBCursor() throws Exception {
        test("IDBCursor", "IDBCursor");
    }

    @Alerts("true/true")
    void _IDBCursor_IDBCursorWithValue() throws Exception {
        test("IDBCursor", "IDBCursorWithValue");
    }

    @Alerts("true/false")
    void _IDBCursorWithValue_IDBCursorWithValue() throws Exception {
        test("IDBCursorWithValue", "IDBCursorWithValue");
    }

    @Alerts("true/false")
    void _IDBDatabase_IDBDatabase() throws Exception {
        test("IDBDatabase", "IDBDatabase");
    }

    @Alerts("true/false")
    void _IDBFactory_IDBFactory() throws Exception {
        test("IDBFactory", "IDBFactory");
    }

    @Alerts("true/false")
    void _IDBIndex_IDBIndex() throws Exception {
        test("IDBIndex", "IDBIndex");
    }

    @Alerts("true/false")
    void _IDBKeyRange_IDBKeyRange() throws Exception {
        test("IDBKeyRange", "IDBKeyRange");
    }

    @Alerts("false/false")
    void _IDBMutableFile_IDBMutableFile() throws Exception {
        test("IDBMutableFile", "IDBMutableFile");
    }

    @Alerts("true/false")
    void _IDBObjectStore_IDBObjectStore() throws Exception {
        test("IDBObjectStore", "IDBObjectStore");
    }

    @Alerts("true/false")
    void _IDBOpenDBRequest_IDBOpenDBRequest() throws Exception {
        test("IDBOpenDBRequest", "IDBOpenDBRequest");
    }

    @Alerts("true/true")
    void _IDBRequest_IDBOpenDBRequest() throws Exception {
        test("IDBRequest", "IDBOpenDBRequest");
    }

    @Alerts("true/false")
    void _IDBRequest_IDBRequest() throws Exception {
        test("IDBRequest", "IDBRequest");
    }

    @Alerts("true/false")
    void _IDBTransaction_IDBTransaction() throws Exception {
        test("IDBTransaction", "IDBTransaction");
    }

    @Alerts("true/false")
    void _IDBVersionChangeEvent_IDBVersionChangeEvent() throws Exception {
        test("IDBVersionChangeEvent", "IDBVersionChangeEvent");
    }

    @Alerts("true/false")
    void _IdleDeadline_IdleDeadline() throws Exception {
        test("IdleDeadline", "IdleDeadline");
    }

    @Alerts("true/false")
    void _IIRFilterNode_IIRFilterNode() throws Exception {
        test("IIRFilterNode", "IIRFilterNode");
    }

    @Alerts("true/false")
    void _Image_HTMLImageElement() throws Exception {
        // although Image != HTMLImageElement, they seem to be synonyms!!!
        test("Image", "HTMLImageElement");
    }

    @Alerts("true/false")
    void _Image_Image() throws Exception {
        test("Image", "Image");
    }

    @Alerts("true/false")
    void _ImageBitmap_ImageBitmap() throws Exception {
        test("ImageBitmap", "ImageBitmap");
    }

    @Alerts("true/false")
    void _ImageBitmapRenderingContext_ImageBitmapRenderingContext() throws Exception {
        test("ImageBitmapRenderingContext", "ImageBitmapRenderingContext");
    }

    @Alerts("true/false")
    void _ImageData_ImageData() throws Exception {
        test("ImageData", "ImageData");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _InputDeviceCapabilities_InputDeviceCapabilities() throws Exception {
        test("InputDeviceCapabilities", "InputDeviceCapabilities");
    }

    @Alerts("true/false")
    void _InputEvent_InputEvent() throws Exception {
        test("InputEvent", "InputEvent");
    }

    @Alerts("false/false")
    void _InstallTrigger_InstallTrigger() throws Exception {
        test("InstallTrigger", "InstallTrigger");
    }

    @Alerts("true/false")
    void _Int16Array_Int16Array() throws Exception {
        test("Int16Array", "Int16Array");
    }

    @Alerts("true/false")
    void _Int32Array_Int32Array() throws Exception {
        test("Int32Array", "Int32Array");
    }

    @Alerts("true/false")
    void _Int8Array_Int8Array() throws Exception {
        test("Int8Array", "Int8Array");
    }

    @Alerts("true/false")
    void _IntersectionObserver_IntersectionObserver() throws Exception {
        test("IntersectionObserver", "IntersectionObserver");
    }

    @Alerts("true/false")
    void _IntersectionObserverEntry_IntersectionObserverEntry() throws Exception {
        test("IntersectionObserverEntry", "IntersectionObserverEntry");
    }

    @Alerts("true/false")
    void _KeyboardEvent_KeyboardEvent() throws Exception {
        test("KeyboardEvent", "KeyboardEvent");
    }

    @Alerts("true/false")
    void _KeyframeEffect_KeyframeEffect() throws Exception {
        test("KeyframeEffect", "KeyframeEffect");
    }

    @Alerts("false/false")
    void _LocalMediaStream_LocalMediaStream() throws Exception {
        test("LocalMediaStream", "LocalMediaStream");
    }

    @Alerts("true/false")
    void _Location_Location() throws Exception {
        test("Location", "Location");
    }
}
