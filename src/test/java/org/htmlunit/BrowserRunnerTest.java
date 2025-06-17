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
package org.htmlunit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BrowserRunner}.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class BrowserRunnerTest extends SimpleWebTestCase {
    private static int Counter_ = 0;

    /**
     * Setup.
     */
    @BeforeAll
    public static void setupClass() {
        Counter_++;
    }

    /**
     * Tear down.
     */
    @AfterAll
    public static void tearDownClass() {
        Counter_--;
    }

    /**
     * Methods marked @BeforeClass (and @AfterClass) were wrongly called twice when using {@link BrowserRunner}.
     */
    @Test
    public void thatBeforeClassMethodHasBeenCalledOnlyOnce() {
        assertEquals(1, Counter_);
    }
}
