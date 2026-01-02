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
package org.htmlunit.attachment;

import java.net.URL;
import java.util.Collections;

import org.htmlunit.HttpMethod;
import org.htmlunit.Page;
import org.htmlunit.WebResponse;
import org.htmlunit.WebResponseData;
import org.htmlunit.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link AttachmentHandler}.
 *
 * @author Ronald Brill
 */
public class AttachmentHandlerTest {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void noHeaders() throws Exception {
        final WebResponseData data = new WebResponseData("HtmlUnit".getBytes(),
                HttpStatus.OK_200, HttpStatus.OK_200_MSG, Collections.emptyList());
        final WebResponse response = new WebResponse(data, new URL("http://test.com"), HttpMethod.GET, 1000);
        final AttachmentHandler attachmentHandler = (page, attachmentFilename) -> {
            // mock
        };

        Assertions.assertFalse(attachmentHandler.isAttachment(response));
    }
}
