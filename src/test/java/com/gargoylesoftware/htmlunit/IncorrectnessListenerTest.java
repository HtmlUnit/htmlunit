/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link IncorrectnessListener}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public final class IncorrectnessListenerTest extends WebTestCase {
    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testNotification() throws Exception {
        final String html = "<html><head>\n"
                + "<meta http-equiv='set-cookie' content='webm=none; path=/; a=b;'>\n"
                + "</head>\n"
                + "<body></body>\n"
                + "</html>";

        final WebClient webClient = getWebClient();
        final List<String> collectedIncorrectness = new ArrayList<String>();
        final IncorrectnessListener listener = new IncorrectnessListener() {
            public void notify(final String message, final Object origin) {
                collectedIncorrectness.add(message);
            }
        };
        webClient.setIncorrectnessListener(listener);

        final MockWebConnection webConnection = new MockWebConnection();
        webClient.setWebConnection(webConnection);
        webConnection.setDefaultResponse(html);
        webClient.getPage(URL_FIRST);

        final String[] expectedIncorrectness = {
            "set-cookie http-equiv meta tag: unknown attribute 'a'."
        };
        assertEquals(expectedIncorrectness, collectedIncorrectness);
    }
}
