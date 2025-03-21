/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.html;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link HTMLFrameElement} when used for {@link org.htmlunit.html.HtmlFrame}.
 *
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Thomas Robbs
 * @author David K. Taylor
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLFrameElementTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Frame2")
    public void serialization() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>first</title></head>\n"
            + "<frameset cols='20%,80%'>\n"
            + "  <frame id='frame1'>\n"
            + "  <frame name='Frame2' onload='alert(this.name)' id='frame2'>\n"
            + "</frameset></html>";

        final HtmlPage page = loadPageWithAlerts(html);

        final ObjectOutputStream objectOS = new ObjectOutputStream(new ByteArrayOutputStream());
        objectOS.writeObject(page);
    }
}
