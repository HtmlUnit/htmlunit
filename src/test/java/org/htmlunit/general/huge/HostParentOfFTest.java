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
 * This class handles all host names which starts by character 'F' to 'G'.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HostParentOfFTest extends HostParentOf {

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    public static Collection<Arguments> data() throws Exception {
        return HostParentOf.data(input -> {
            final char ch = Character.toUpperCase(input.charAt(0));
            return ch >= 'F' && ch <= 'G';
        });
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _FederatedCredential_FederatedCredential() throws Exception {
        test("FederatedCredential", "FederatedCredential");
    }

    @Alerts("true/false")
    void _File_File() throws Exception {
        test("File", "File");
    }

    @Alerts("true/false")
    void _FileList_FileList() throws Exception {
        test("FileList", "FileList");
    }

    @Alerts("true/false")
    void _FileReader_FileReader() throws Exception {
        test("FileReader", "FileReader");
    }

    @Alerts(DEFAULT = "false/false",
            FF = "true/false",
            FF_ESR = "true/false")
    void _FileSystem_FileSystem() throws Exception {
        test("FileSystem", "FileSystem");
    }

    @Alerts(DEFAULT = "false/false",
            FF = "true/false",
            FF_ESR = "true/false")
    void _FileSystemDirectoryEntry_FileSystemDirectoryEntry() throws Exception {
        test("FileSystemDirectoryEntry", "FileSystemDirectoryEntry");
    }

    @Alerts(DEFAULT = "false/false",
            FF = "true/false",
            FF_ESR = "true/false")
    void _FileSystemDirectoryReader_FileSystemDirectoryReader() throws Exception {
        test("FileSystemDirectoryReader", "FileSystemDirectoryReader");
    }

    @Alerts(DEFAULT = "false/false",
            FF = "true/true",
            FF_ESR = "true/true")
    void _FileSystemEntry_FileSystemDirectoryEntry() throws Exception {
        test("FileSystemEntry", "FileSystemDirectoryEntry");
    }

    @Alerts(DEFAULT = "false/false",
            FF = "true/false",
            FF_ESR = "true/false")
    void _FileSystemEntry_FileSystemEntry() throws Exception {
        test("FileSystemEntry", "FileSystemEntry");
    }

    @Alerts(DEFAULT = "false/false",
            FF = "true/true",
            FF_ESR = "true/true")
    void _FileSystemEntry_FileSystemFileEntry() throws Exception {
        test("FileSystemEntry", "FileSystemFileEntry");
    }

    @Alerts(DEFAULT = "false/false",
            FF = "true/false",
            FF_ESR = "true/false")
    void _FileSystemFileEntry_FileSystemFileEntry() throws Exception {
        test("FileSystemFileEntry", "FileSystemFileEntry");
    }

    @Alerts("true/false")
    void _Float32Array_Float32Array() throws Exception {
        test("Float32Array", "Float32Array");
    }

    @Alerts("true/false")
    void _Float64Array_Float64Array() throws Exception {
        test("Float64Array", "Float64Array");
    }

    @Alerts("true/false")
    void _FocusEvent_FocusEvent() throws Exception {
        test("FocusEvent", "FocusEvent");
    }

    @Alerts("true/false")
    void _FontFace_FontFace() throws Exception {
        test("FontFace", "FontFace");
    }

    @Alerts(DEFAULT = "false/false",
            FF = "true/false",
            FF_ESR = "true/false")
    void _FontFaceSet_FontFaceSet() throws Exception {
        test("FontFaceSet", "FontFaceSet");
    }

    @Alerts("true/false")
    void _FormData_FormData() throws Exception {
        test("FormData", "FormData");
    }

    @Alerts("true/false")
    void _GainNode_GainNode() throws Exception {
        test("GainNode", "GainNode");
    }

    @Alerts("true/false")
    void _Gamepad_Gamepad() throws Exception {
        test("Gamepad", "Gamepad");
    }

    @Alerts("true/false")
    void _GamepadButton_GamepadButton() throws Exception {
        test("GamepadButton", "GamepadButton");
    }

    @Alerts("true/false")
    void _GamepadEvent_GamepadEvent() throws Exception {
        test("GamepadEvent", "GamepadEvent");
    }

    @Alerts("true/false")
    void _Geolocation_Geolocation() throws Exception {
        test("Geolocation", "Geolocation");
    }

    @Alerts("true/false")
    void _GeolocationCoordinates_GeolocationCoordinates() throws Exception {
        test("GeolocationCoordinates", "GeolocationCoordinates");
    }

    @Alerts("true/false")
    void _GeolocationPosition_GeolocationPosition() throws Exception {
        test("GeolocationPosition", "GeolocationPosition");
    }

    @Alerts("true/false")
    void _GeolocationPositionError_GeolocationPositionError() throws Exception {
        test("GeolocationPositionError", "GeolocationPositionError");
    }
}
