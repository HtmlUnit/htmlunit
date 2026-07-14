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
package org.htmlunit.javascript.host;

/**
 * The {@code WindowOrWorkerGlobalScope} mixin describes several features
 * common to {@link Window} and {@code WorkerGlobalScope}.
 *
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/WindowOrWorkerGlobalScope">MDN Documentation</a>
 */
public interface WindowOrWorkerGlobalScope {

    /**
     * Decodes a string of data that has been encoded using Base64 encoding.
     *
     * @param encodedData the Base64-encoded string to decode
     * @return the decoded value
     */
    String atob(String encodedData);

    /**
     * Creates a Base64-encoded ASCII string from a string of binary data.
     *
     * @param stringToEncode the string to encode
     * @return the Base64-encoded string
     */
    String btoa(String stringToEncode);
}
