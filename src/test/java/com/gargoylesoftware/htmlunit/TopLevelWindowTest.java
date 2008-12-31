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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.base.testing.EventCatcher;

/**
 * Tests for {@link WebClient}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Ahmed Ashour
 */
public class TopLevelWindowTest extends WebTestCase {

    /**
     * Tests closing the only open window.
     * @throws Exception if the test fails
     */
    @Test
    public void testCloseOnlyWindow() throws Exception {
        final WebClient webClient = new WebClient();
        final EventCatcher eventCatcher = new EventCatcher();
        eventCatcher.listenTo(webClient);

        final WebWindow windowToClose = webClient.getCurrentWindow();
        ((TopLevelWindow) windowToClose).close();

        final List<WebWindowEvent> expectedEvents = Arrays.asList(new WebWindowEvent[] {
            new WebWindowEvent(windowToClose, WebWindowEvent.CLOSE, null, null)
        });
        assertEquals(expectedEvents, eventCatcher.getEvents());

        // Since this was the only open window, a new window should have
        // been created when this one was closed. Verify this.
        assertNotNull(webClient.getCurrentWindow());
        assertNotSame(webClient.getCurrentWindow(), windowToClose);

        assertEquals(1, webClient.getWebWindows().size());
    }
}
