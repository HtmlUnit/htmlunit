/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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

import org.junit.Test;

/**
 * Tests for {@link WebRequestSettings}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class WebRequestSettingsTest extends WebServerTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void headers() throws Exception {
        final WebRequestSettings settings = new WebRequestSettings(URL_GARGOYLE);
        final int initialSize = settings.getAdditionalHeaders().size();
        settings.addAdditionalHeader("Accept", "nothing");
        assertEquals(initialSize, settings.getAdditionalHeaders().size());
        settings.addAdditionalHeader("ACCEPT", "compares");
        assertEquals(initialSize, settings.getAdditionalHeaders().size());
        settings.removeAdditionalHeader("ACcEpT");
        assertEquals(initialSize - 1, settings.getAdditionalHeaders().size());
    }
}
