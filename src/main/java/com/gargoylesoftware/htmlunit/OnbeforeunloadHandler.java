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
package com.gargoylesoftware.htmlunit;

import java.io.Serializable;

/**
 * A handler for <tt>onbeforeunload</tt> events.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public interface OnbeforeunloadHandler extends Serializable {

    /**
     * Handles an <tt>onbeforeunload</tt> event for the specified page.
     * @param page the page on which the event occurred
     * @param returnValue the event's <tt>returnValue</tt>
     * @return {@code true} to accept the event, {@code false} otherwise
     */
    boolean handleEvent(Page page, String returnValue);
}
