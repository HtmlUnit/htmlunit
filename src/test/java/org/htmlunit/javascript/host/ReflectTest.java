/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link Reflect}.
 *
 * @author Ronald Brill
 * @author Lai Quang Duong
 */
@RunWith(BrowserRunner.class)
public class ReflectTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "false", "true", "true"},
            IE = "no Reflect")
    public void has() throws Exception {
        final String html = "<html><head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  if (typeof Reflect != 'undefined') {\n"
            + "    log(Reflect.has({x: 0}, 'x'));\n"
            + "    log(Reflect.has({x: 0}, 'y'));\n"
            + "    log(Reflect.has({x: 0}, 'toString'));\n"

            + "    log((Reflect ? Reflect.has : log)({x: 0}, 'x'));\n"
            + "  } else { log('no Reflect'); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"-1,0,6,8,55,773,str,str2,Symbol(foo),Symbol(bar)"},
            IE = "no Reflect")
    public void ownKeys() throws Exception {
        final String html = "<html><head>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  if (typeof Reflect != 'undefined') {\n"
                + "    var obj = {};\n"
                + "    obj[Symbol.for('foo')] = 0;\n"
                + "    obj['str'] = 0;\n"
                + "    obj[773] = 0;\n"
                + "    obj['55'] = 0;\n"
                + "    obj[0] = 0;\n"
                + "    obj['-1'] = 0;\n"
                + "    obj[8] = 0;\n"
                + "    obj['6'] = 0;\n"
                + "    obj[Symbol.for('bar')] = 0;\n"
                + "    obj['str2'] = 0;\n"
                + "    log(Reflect.ownKeys(obj));\n"
                + "  } else { log('no Reflect'); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
