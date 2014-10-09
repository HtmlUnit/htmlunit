/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.html.HtmlApplet;

/**
 * A handler for Applets. Like in real browsers, you have to confirm
 * before the browser starts an applet.
 *
 * @version $Revision$
 * @author Ronald Brill
 */
public interface AppletConfirmHandler {
    /**
     * Handles a confirm for the specified page.
     * @param applet the applet the browser likes to start
     * @return true if starting is allowed
     */
    boolean confirm(final HtmlApplet applet);
}
