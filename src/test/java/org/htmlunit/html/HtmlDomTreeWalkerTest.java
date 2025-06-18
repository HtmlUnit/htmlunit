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
package org.htmlunit.html;

import org.apache.commons.lang3.SerializationUtils;
import org.htmlunit.SimpleWebTestCase;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HtmlDomTreeWalker}.
 *
 * @author Ronald Brill
 */
public class HtmlDomTreeWalkerTest extends SimpleWebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head></head><body>\n"
                + "<div style='display:inline'>1</div><div style='display:inline'>2</div>\n"
                + "</body></html>";

        final HtmlPage page = loadPage(html);

        final byte[] bytes = SerializationUtils.serialize(new HtmlDomTreeWalker(page.getBody(), 0, null, false));

        final HtmlDomTreeWalker deserialized = (HtmlDomTreeWalker) SerializationUtils.deserialize(bytes);
        Assert.assertEquals(page.getBody().getNodeName(), deserialized.getRoot().getNodeName());
    }
}
