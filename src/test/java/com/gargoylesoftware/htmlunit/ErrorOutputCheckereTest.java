/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Unit tests for the unit test utility {@link ErrorOutputChecker}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class ErrorOutputCheckereTest extends WebTestCase {

    // no need to specify Rule here as it is specified in {@link WebTestCase}

    /**
     * Test expected to fail because it produces output on {@link System.err}.
     */
    @Test
    @NotYetImplemented // in fact it IS implemented, but this allows to test that the test fails due to the output
    public void systemErrOutputShouldBreakTest() {
        System.err.println("Dummy output to test ErrorOutputChecker");
    }
}
