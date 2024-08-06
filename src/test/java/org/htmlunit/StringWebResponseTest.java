/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.junit.Test;

/**
 * Unit tests for {@link StringWebResponse}.
 *
 * @author Marc Guillemot
 * @author Carsten Steul
 * @author Ronald Brill
 */
public class StringWebResponseTest extends SimpleWebTestCase {

    /**
     * Regression test for bug 2998004.
     */
    @Test
    public void charset() {
        final StringWebResponse webResponse = new StringWebResponse("hello", UTF_8, URL_FIRST);
        assertSame(UTF_8, webResponse.getContentCharset());
    }

    @Test
    public void charsetImplicit() {
        final StringWebResponse webResponse = new StringWebResponse("hello", URL_FIRST);
        assertSame(UTF_8, webResponse.getContentCharset());
    }

    /**
     * Regression test for bug #1660.
     */
    @Test
    public void charsetInContent() {
        final String content = "<html><head>\n"
                + "<meta http-equiv='Content-Type' content='text/html; charset=windows-1250' />\n"
                + "</head><body>\u010C\u00CDSLO</body></html>";
        final StringWebResponse webResponse = new StringWebResponse(content, UTF_8, URL_FIRST);

        assertSame(UTF_8, webResponse.getContentCharset());
        assertEquals(content, webResponse.getContentAsString());
    }

    @Test
    public void charsetInContentImplicit() {
        final String content = "<html><head>\n"
                + "<meta http-equiv='Content-Type' content='text/html; charset=windows-1250' />\n"
                + "</head><body>\u010C\u00CDSLO</body></html>";
        final StringWebResponse webResponse = new StringWebResponse(content, URL_FIRST);

        assertSame(UTF_8, webResponse.getContentCharset());
        assertEquals(content, webResponse.getContentAsString());
    }

    /**
     * @throws IOException
     */
    @Test
    public void inputStream() throws IOException {
        final String content = "<html><head>\n"
                + "<meta http-equiv='Content-Type' content='text/html; charset=windows-1250' />\n"
                + "</head><body>\u010C\u00CDSLO</body></html>";
        final StringWebResponse webResponse = new StringWebResponse(content, URL_FIRST);

        Charset charset = webResponse.getContentCharset();
        try (InputStream is = webResponse.getContentAsStreamWithBomIfApplicable()) {
            if (is instanceof BOMInputStream) {
                final String bomCharsetName = ((BOMInputStream) is).getBOMCharsetName();
                if (bomCharsetName != null) {
                    charset = Charset.forName(bomCharsetName);
                }
            }

            try (InputStreamReader reader = new InputStreamReader(is, charset)) {
                final String read = IOUtils.toString(reader);

                assertSame(UTF_8, charset);
                assertEquals(content, read);
            }
        }
    }
}
