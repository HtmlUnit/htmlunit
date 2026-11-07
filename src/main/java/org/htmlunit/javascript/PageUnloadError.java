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
package org.htmlunit.javascript;

/**
 * An error that is thrown to stop a script whose page has been replaced
 * by another page in the window (e.g. because of a navigation) while the
 * script was still running.
 * <p>
 * This is an {@link Error} to make sure the script never gets
 * control back through catch or finally.
 * </p>
 *
 * @author Ronald Brill
 */
final class PageUnloadedError extends Error {

    /**
     * Ctor.
     */
    PageUnloadedError() {
        super("The page of the running script has been unloaded");
    }
}
