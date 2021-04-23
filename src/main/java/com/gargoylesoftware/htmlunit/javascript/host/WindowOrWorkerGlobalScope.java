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
package com.gargoylesoftware.htmlunit.javascript.host;

/**
 * The WindowOrWorkerGlobalScope mixin describes several features
 * common to the  {@code Window} and WorkerGlobalScope.
 *
 * @author Ronald Brill
 */
public interface WindowOrWorkerGlobalScope {

    /**
     * Decodes a string of data which has been encoded using base-64 encoding.
     * @param encodedData the encoded string
     * @return the decoded value
     */
    String atob(String encodedData);

    /**
     * Creates a base-64 encoded ASCII string from a string of binary data.
     * @param stringToEncode string to encode
     * @return the encoded string
     */
    String btoa(String stringToEncode);
}
