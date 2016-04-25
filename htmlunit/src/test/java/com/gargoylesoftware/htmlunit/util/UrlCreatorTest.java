/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.util;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.util.URLCreator.URLCreatorGAE;

/**
 * Tests for {@link URLCreator}.
 *
 * @author Marc Guillemot
 */
public class UrlCreatorTest {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void gaeGetProtocol() throws Exception {
        final URLCreatorGAE creator = new URLCreator.URLCreatorGAE();

        final URL url = creator.toUrlUnsafeClassic("data:text/javascript,d1%20%3D%20'one'%3B");
        assertEquals("http://gaeHack_data/text/javascript,d1%20%3D%20'one'%3B", url.toString());
        assertEquals("data", creator.getProtocol(url));
    }
}
