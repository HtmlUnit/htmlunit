/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import java.net.URL;

import org.junit.Test;

/**
 * Tests for {@link WebRequestSettings}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Rodney Gitzel
 */
public class WebRequestSettingsTest extends WebServerTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void headers() throws Exception {
        final WebRequestSettings settings = new WebRequestSettings(getDefaultUrl());
        final int initialSize = settings.getAdditionalHeaders().size();
        settings.setAdditionalHeader("Accept", "nothing");
        assertEquals(initialSize, settings.getAdditionalHeaders().size());
        settings.setAdditionalHeader("ACCEPT", "compares");
        assertEquals(initialSize, settings.getAdditionalHeaders().size());
        settings.removeAdditionalHeader("ACcEpT");
        assertEquals(initialSize - 1, settings.getAdditionalHeaders().size());
    }

    /**
     * A number of these refer to '285434' which is this defect:
     *  https://sourceforge.net/tracker/?func=detail&aid=2854634&group_id=47038&atid=448266.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void setUrl_eliminateDirUp() throws Exception {
        final URL url1 = new URL("http://htmlunit.sf.net/foo.html");
        final URL url2 = new URL("http://htmlunit.sf.net/dir/foo.html");
        final URL url3 = new URL("http://htmlunit.sf.net/dir/foo.html?a=1&b=2");

        // with directory/..
        WebRequestSettings settings = new WebRequestSettings(new URL("http://htmlunit.sf.net/bla/../foo.html"));
        assertEquals(url1, settings.getUrl());

        // with /..
        settings = new WebRequestSettings(new URL("http://htmlunit.sf.net/../foo.html"));
        assertEquals(url1, settings.getUrl());

        // with /(\w\w)/.. (c.f. 2854634)
        settings = new WebRequestSettings(new URL("http://htmlunit.sf.net/dir/fu/../foo.html"));
        assertEquals(url2, settings.getUrl());

        // with /../..
        settings = new WebRequestSettings(new URL("http://htmlunit.sf.net/../../foo.html"));
        assertEquals(url1, settings.getUrl());

        // with ../.. (c.f. 2854634)
        settings = new WebRequestSettings(new URL("http://htmlunit.sf.net/dir/foo/bar/../../foo.html"));
        assertEquals(url2, settings.getUrl());

        settings = new WebRequestSettings(
                          new URL("http://htmlunit.sf.net/dir/foo/bar/boo/hoo/silly/../../../../../foo.html"));
        assertEquals(url2, settings.getUrl());

        // with /.
        settings = new WebRequestSettings(new URL("http://htmlunit.sf.net/./foo.html"));
        assertEquals(url1, settings.getUrl());

        // with /\w//. (c.f. 2854634)
        settings = new WebRequestSettings(new URL("http://htmlunit.sf.net/a/./foo.html"));
        assertEquals(new URL("http://htmlunit.sf.net/a/foo.html"), settings.getUrl());

        // with /.
        settings = new WebRequestSettings(new URL("http://htmlunit.sf.net/dir/./foo.html"));
        assertEquals(url2, settings.getUrl());

        // with /. and query
        settings = new WebRequestSettings(new URL("http://htmlunit.sf.net/dir/./foo.html?a=1&b=2"));
        assertEquals(url3, settings.getUrl());

        // pathological
        settings = new WebRequestSettings(
                new URL("http://htmlunit.sf.net/dir/foo/bar/./boo/hoo/silly/.././../../../.././foo.html?a=1&b=2"));
        assertEquals(url3, settings.getUrl());
    }
}
