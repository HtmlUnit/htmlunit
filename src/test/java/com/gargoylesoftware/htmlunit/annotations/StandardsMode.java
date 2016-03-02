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
package com.gargoylesoftware.htmlunit.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to denote a test which will automatically run twice, in {@code Quirks Mode} and
 * in {@code Standards Mode}.
 *
 * <p>For now, class must by annotated with {@literal @StandardsMode} to run twice, but later
 * this could be the default behavior and we then have something like {@literal @WithoutStandardsMode}
 * </p>
 *
 * <p>A typical example would be:
 * <pre>
 * {@literal @RunWith(BrowserRunner.class)}
 * {@literal @StandardsMode}
 * public class SomeTest extends WebDriverTestCase {
 *
 *     {@literal @Test}
 *     {@literal @Alerts("BackCompat")}
 *     {@literal @AlertsStandards("CSS1Compat")}
 *     public void test() throws Exception {
 *          final String html = "&lt;html&gt;\n"
 *              + "&lt;head&gt;\n"
 *              + "    &lt;script&gt;\n"
 *              + "    function test() {\n"
 *              + "        alert(document.compatMode);\n"
 *              + "    }\n"
 *              + "    &lt;/script&gt;\n"
 *              + "&lt;/head&gt;\n"
 *              + "&lt;body onload='test()'&gt;\n"
 *              + "&lt;/body&gt;\n"
 *              + "&lt;/html&gt;";
 *          loadPageWithAlerts2(html);
 *     }
 * }
 * </pre>
 * </p>
 * @author Ahmed Ashour
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface StandardsMode {

}
