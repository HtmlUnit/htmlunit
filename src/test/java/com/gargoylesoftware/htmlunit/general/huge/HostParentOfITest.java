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
package com.gargoylesoftware.htmlunit.general.huge;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF78;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import com.gargoylesoftware.htmlunit.BrowserParameterizedRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests two Host classes, if one prototype is parent of another.
 *
 * This class handles all host names which starts by character 'I' to 'L'.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserParameterizedRunner.class)
public class HostParentOfITest extends HostParentOf {

    /**
     * Returns the parameterized data.
     *
     * @return the parameterized data
     * @throws Exception
     *             if an error occurs
     */
    @Parameters
    public static Collection<Object[]> data() throws Exception {
        return HostParentOf.data(input -> {
            final char ch = Character.toUpperCase(input.charAt(0));
            return ch >= 'I' && ch <= 'L';
        });
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _IDBCursor_IDBCursor() throws Exception {
        test("IDBCursor", "IDBCursor");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _IDBCursor_IDBCursorWithValue() throws Exception {
        test("IDBCursor", "IDBCursorWithValue");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _IDBCursorWithValue_IDBCursorWithValue() throws Exception {
        test("IDBCursorWithValue", "IDBCursorWithValue");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _IDBDatabase_IDBDatabase() throws Exception {
        test("IDBDatabase", "IDBDatabase");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _IDBFactory_IDBFactory() throws Exception {
        test("IDBFactory", "IDBFactory");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _IDBIndex_IDBIndex() throws Exception {
        test("IDBIndex", "IDBIndex");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _IDBKeyRange_IDBKeyRange() throws Exception {
        test("IDBKeyRange", "IDBKeyRange");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF = "true",
            FF78 = "true")
    public void _IDBMutableFile_IDBMutableFile() throws Exception {
        test("IDBMutableFile", "IDBMutableFile");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _IDBObjectStore_IDBObjectStore() throws Exception {
        test("IDBObjectStore", "IDBObjectStore");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _IDBOpenDBRequest_IDBOpenDBRequest() throws Exception {
        test("IDBOpenDBRequest", "IDBOpenDBRequest");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _IDBRequest_IDBOpenDBRequest() throws Exception {
        test("IDBRequest", "IDBOpenDBRequest");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _IDBRequest_IDBRequest() throws Exception {
        test("IDBRequest", "IDBRequest");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _IDBTransaction_IDBTransaction() throws Exception {
        test("IDBTransaction", "IDBTransaction");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _IDBVersionChangeEvent_IDBVersionChangeEvent() throws Exception {
        test("IDBVersionChangeEvent", "IDBVersionChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _IdleDeadline_IdleDeadline() throws Exception {
        test("IdleDeadline", "IdleDeadline");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _IIRFilterNode_IIRFilterNode() throws Exception {
        test("IIRFilterNode", "IIRFilterNode");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    @NotYetImplemented({CHROME, EDGE})
    public void _Image_HTMLImageElement() throws Exception {
        // although Image != HTMLImageElement, they seem to be synonyms!!!
        test("Image", "HTMLImageElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Image_Image() throws Exception {
        test("Image", "Image");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _ImageBitmap_ImageBitmap() throws Exception {
        test("ImageBitmap", "ImageBitmap");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _ImageBitmapRenderingContext_ImageBitmapRenderingContext() throws Exception {
        test("ImageBitmapRenderingContext", "ImageBitmapRenderingContext");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _ImageData_ImageData() throws Exception {
        test("ImageData", "ImageData");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            EDGE = "true")
    public void _InputDeviceCapabilities_InputDeviceCapabilities() throws Exception {
        test("InputDeviceCapabilities", "InputDeviceCapabilities");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _InputEvent_InputEvent() throws Exception {
        test("InputEvent", "InputEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented({FF, FF78})
    public void _InstallTrigger_InstallTrigger() throws Exception {
        test("InstallTrigger", "InstallTrigger");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Int16Array_Int16Array() throws Exception {
        test("Int16Array", "Int16Array");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Int32Array_Int32Array() throws Exception {
        test("Int32Array", "Int32Array");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Int8Array_Int8Array() throws Exception {
        test("Int8Array", "Int8Array");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _IntersectionObserver_IntersectionObserver() throws Exception {
        test("IntersectionObserver", "IntersectionObserver");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _IntersectionObserverEntry_IntersectionObserverEntry() throws Exception {
        test("IntersectionObserverEntry", "IntersectionObserverEntry");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _KeyboardEvent_KeyboardEvent() throws Exception {
        test("KeyboardEvent", "KeyboardEvent");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _KeyframeEffect_KeyframeEffect() throws Exception {
        test("KeyframeEffect", "KeyframeEffect");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("false")
    public void _LocalMediaStream_LocalMediaStream() throws Exception {
        test("LocalMediaStream", "LocalMediaStream");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Location_Location() throws Exception {
        test("Location", "Location");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _RTCStatsReport_RTCStatsReport() throws Exception {
        test("RTCStatsReport", "RTCStatsReport");
    }

}
