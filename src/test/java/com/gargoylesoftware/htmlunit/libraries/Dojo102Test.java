/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.libraries;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests for compatibility with version 1.0.2 of the <a href="http://dojotoolkit.org/">Dojo
 * JavaScript library</a>.
 *
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class Dojo102Test extends DojoTestBase {

    @Override
    String getVersion() {
        return "1.0.2";
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = {"../../dojo/tests/_base/fx.html::t::animateHeight",
                    "test: format", "test: parse", "test: format_patterns",
                    "test: format_rounding", "PASSED test: format_perMill",
                    "test: number_regression_1", "test: number_regression_3",
                    "test: number_regression_8", "test: number_regression_10",
                    "../../dojo/tests/io/script.html::t::ioScriptJsonpTimeout"},
            FF38 = {"test: ../../dojo/tests/_base/fx.html::t::animateHeight",
                    "test: ../../dojo/tests/behavior.html::t::topics",
                    "../../dojo/tests/io/script.html::t::ioScriptJsonpTimeout"},
            FF45 = {"test: ../../dojo/tests/_base/fx.html::t::animateHeight",
                    "test: ../../dojo/tests/behavior.html::t::topics",
                    "../../dojo/tests/io/script.html::t::ioScriptJsonpTimeout"},
            IE = {"test: ../../dojo/tests/_base/html.html::t::sq100nopos",
                    "../../dojo/tests/_base/html_quirks.html::t::sq100nopos",
                    "test: ../../dojo/tests/_base/fx.html::t::animateHeight",
                    "test: format", "test: parse", "test: format_patterns",
                    "test: format_rounding", "PASSED test: format_perMill",
                    "test: number_regression_1", "test: number_regression_3",
                    "test: number_regression_8", "test: number_regression_10",
                    "test: ../../dojo/tests/behavior.html::t::topics",
                    "../../dojo/tests/io/script.html::t::ioScriptJsonpTimeout"})
    @NotYetImplemented(CHROME)
    public void dojo() throws Exception {
        test();
    }
}
