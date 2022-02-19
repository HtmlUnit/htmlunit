/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.platform;

import org.apache.commons.lang3.reflect.ConstructorUtils;

/**
 * Abstraction to be able to support android and maybe others.
 *
 * @author Ronald Brill
 */
public final class ClipboardAccessImpl {

    private static ClipboardAccess ClipboardAccess_;

    public static String getClipboardContent() {
        return getClipboardAccess().getClipboardContent();
    }

    public static void setClipboardContent(final String string) {
        getClipboardAccess().setClipboardContent(string);
    }

    private static ClipboardAccess getClipboardAccess() {
        if (ClipboardAccess_ == null) {
            try {
                final Class<?> clazz = Class.forName("com.gargoylesoftware.htmlunit.platform.AwtClipboard");
                ClipboardAccess_ = (ClipboardAccess) ConstructorUtils.invokeConstructor(clazz);

                // the class might be available bot in headless environments the clipboard
                // is not usable - let as make a check
                ClipboardAccess_.getClipboardContent();

                // clipboard is ready
                return ClipboardAccess_;
            }
            catch (final Throwable e) {
                // fallback
            }

            ClipboardAccess_ = new NoOpClipboard();
        }

        return ClipboardAccess_;
    }

    private ClipboardAccessImpl() {
    }
}
