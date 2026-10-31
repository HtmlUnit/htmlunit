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
package org.htmlunit.javascript.host;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PerformanceObserver}.
 *
 * @author Ronald Brill
 */
public class PerformanceObserverTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void inWindow() throws Exception {
        runTest("log('PerformanceObserver' in window);");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "function", "function", "true"})
    public void functions() throws Exception {
        runTest("var observer = new PerformanceObserver(function(list, obs) {});\n"
                + "log(typeof observer.observe);\n"
                + "log(typeof observer.disconnect);\n"
                + "log(typeof observer.takeRecords);\n"
                + "log('supportedEntryTypes' in PerformanceObserver);");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void ctorWithoutCallback() throws Exception {
        runTest("try {\n"
                + "  new PerformanceObserver();\n"
                + "  log('created');\n"
                + "} catch(e) { logEx(e); }");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void ctorCallbackNotAFunction() throws Exception {
        runTest("try {\n"
                + "  new PerformanceObserver('callback');\n"
                + "  log('created');\n"
                + "} catch(e) { logEx(e); }");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void ctorWithoutNew() throws Exception {
        runTest("try {\n"
                + "  PerformanceObserver(function() {});\n"
                + "  log('created');\n"
                + "} catch(e) { logEx(e); }");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "done"})
    public void observeEntryTypes() throws Exception {
        runTest("var observer = new PerformanceObserver(function() { log('callback'); });\n"
                + "log(observer.observe({ entryTypes: ['mark', 'measure'] }));\n"
                + "log('done');");
    }

    /**
     * This is the pattern used by the Cloudflare challenge script (see issue #1136).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "done"})
    public void observeType() throws Exception {
        runTest("var observer = new PerformanceObserver(function() { log('callback'); });\n"
                + "log(observer.observe({ type: 'paint', buffered: true }));\n"
                + "log('done');");
    }

    /**
     * Calling observe() again with the same mode is allowed.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void observeTypeTwice() throws Exception {
        runTest("var observer = new PerformanceObserver(function() {});\n"
                + "observer.observe({ type: 'mark' });\n"
                + "observer.observe({ type: 'measure' });\n"
                + "log('done');");
    }

    /**
     * Unsupported entry types are ignored, no exception.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"done", "done", "done"})
    public void observeUnsupported() throws Exception {
        runTest("var observer = new PerformanceObserver(function() {});\n"
                + "try { observer.observe({ type: 'htmlunit' }); log('done'); } catch(e) { logEx(e); }\n"
                + "observer = new PerformanceObserver(function() {});\n"
                + "try { observer.observe({ entryTypes: ['htmlunit'] }); log('done'); } catch(e) { logEx(e); }\n"
                + "observer = new PerformanceObserver(function() {});\n"
                + "try { observer.observe({ entryTypes: [] }); log('done'); } catch(e) { logEx(e); }");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TypeError", "TypeError", "TypeError", "TypeError"})
    public void observeMissingOptions() throws Exception {
        runTest("var observer = new PerformanceObserver(function() {});\n"
                + "try { observer.observe(); log('done'); } catch(e) { logEx(e); }\n"
                + "try { observer.observe({}); log('done'); } catch(e) { logEx(e); }\n"
                + "try { observer.observe({ buffered: true }); log('done'); } catch(e) { logEx(e); }\n"
                + "try { observer.observe({ type: undefined, entryTypes: undefined }); log('done'); }"
                        + " catch(e) { logEx(e); }");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void observeTypeAndEntryTypes() throws Exception {
        runTest("var observer = new PerformanceObserver(function() {});\n"
                + "try {\n"
                + "  observer.observe({ type: 'mark', entryTypes: ['mark'] });\n"
                + "  log('done');\n"
                + "} catch(e) { logEx(e); }");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"InvalidModificationError/DOMException", "InvalidModificationError/DOMException"})
    public void observeChangeMode() throws Exception {
        runTest("var observer = new PerformanceObserver(function() {});\n"
                + "observer.observe({ entryTypes: ['mark'] });\n"
                + "try { observer.observe({ type: 'measure' }); log('done'); } catch(e) { logEx(e); }\n"
                + "observer = new PerformanceObserver(function() {});\n"
                + "observer.observe({ type: 'mark' });\n"
                + "try { observer.observe({ entryTypes: ['measure'] }); log('done'); } catch(e) { logEx(e); }");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "0", "true", "0"})
    public void takeRecords() throws Exception {
        runTest("var observer = new PerformanceObserver(function() {});\n"
                + "var records = observer.takeRecords();\n"
                + "log(Array.isArray(records));\n"
                + "log(records.length);\n"
                + "observer.observe({ type: 'mark' });\n"
                + "records = observer.takeRecords();\n"
                + "log(Array.isArray(records));\n"
                + "log(records.length);");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "undefined", "done"})
    public void disconnect() throws Exception {
        runTest("var observer = new PerformanceObserver(function() {});\n"
                + "log(observer.disconnect());\n"
                + "observer.observe({ type: 'mark' });\n"
                + "log(observer.disconnect());\n"
                + "observer.observe({ type: 'measure' });\n"
                + "log('done');");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "true", "true", "true"},
            FF = {"true", "false", "true", "true"},
            FF_ESR = {"true", "false", "true", "true"})
    @HtmlUnitNYI(CHROME = {"true", "false", "true", "true"},
            EDGE = {"true", "false", "true", "true"},
            FF = {"true", "false", "true", "true"},
            FF_ESR = {"true", "false", "true", "true"})
    public void supportedEntryTypes() throws Exception {
        runTest("var types = PerformanceObserver.supportedEntryTypes;\n"
                + "log(Array.isArray(types));\n"
                + "log(Object.isFrozen(types));\n"
                + "log(types.indexOf('mark') > -1);\n"
                + "log(types.indexOf('measure') > -1);");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "element,event,first-input,interaction-contentful-paint,largest-contentful-paint,layout-shift,"
                + "long-animation-frame,longtask,mark,measure,navigation,paint,resource,soft-navigation,visibility-state",
            FF = "event,first-input,largest-contentful-paint,mark,measure,navigation,paint,resource",
            FF_ESR = "event,first-input,largest-contentful-paint,mark,measure,navigation,paint,resource")
    @HtmlUnitNYI(
            CHROME = "element,event,first-input,largest-contentful-paint,layout-shift,"
                + "long-animation-frame,longtask,mark,measure,navigation,paint,resource,visibility-state",
            EDGE = "element,event,first-input,largest-contentful-paint,layout-shift,"
                + "long-animation-frame,longtask,mark,measure,navigation,paint,resource,visibility-state",
            FF = "element,event,first-input,largest-contentful-paint,layout-shift,"
                + "long-animation-frame,longtask,mark,measure,navigation,paint,resource,visibility-state",
            FF_ESR = "element,event,first-input,largest-contentful-paint,layout-shift,"
                + "long-animation-frame,longtask,mark,measure,navigation,paint,resource,visibility-state")
    public void supportedEntryTypesList() throws Exception {
        runTest("log(PerformanceObserver.supportedEntryTypes.join());");
    }

    private void runTest(final String js) throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + js + "\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'></body>\n"
                + "</html>";

        loadPageVerifyTitle2(html);
    }
}
