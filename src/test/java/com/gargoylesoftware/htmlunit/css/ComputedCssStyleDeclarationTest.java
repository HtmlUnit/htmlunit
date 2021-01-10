/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.css;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.css.StyleAttributes.Definition;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@RunWith(BrowserRunner.class)
public class ComputedCssStyleDeclarationTest extends SimpleWebTestCase {

    @Test
    public void testInheritStyle() throws Exception {
        String html = "<font id='parent' style=\"font-size:30px\"><b id='child'>txt</b></font>";
        HtmlPage page = loadPage(html);

        DomElement parent = page.getElementById("parent");
        assertEquals(parent.getComputedStyle().getStyleAttribute(Definition.FONT_SIZE.getAttributeName()), "30px");
        assertEquals(parent.getComputedStyle().getStyleAttribute(Definition.FONT_SIZE_.getAttributeName()), "30px");

        DomElement child = page.getElementById("child");
        assertEquals(child.getComputedStyle().getStyleAttribute(Definition.FONT_SIZE.getAttributeName()), "30px");
        assertEquals(child.getComputedStyle().getStyleAttribute(Definition.FONT_SIZE), "30px");
    }

}