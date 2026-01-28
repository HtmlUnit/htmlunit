/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link WebResponseData}.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class WebResponseData2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void bigContent() throws Exception {
        final StringBuilder builder = new StringBuilder();
        builder.append(" ".repeat(300));
        builder.append("Hello World!");
        loadPage(builder.toString());
    }

}
