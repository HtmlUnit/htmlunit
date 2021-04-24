/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.svg;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for running JavaScripts in SVG elements..
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class SvgJavaScriptTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void onclick() throws Exception {
        final String html = ""
            + "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
            + "<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" baseProfile=\"full\" width=\"100px\""
            + " height=\"100px\" viewBox=\"0 0 100 100\">\n"
            + "  <rect id=\"rect\" width=\"50\" height=\"50\" fill=\"blue\" "
            + "onclick=\"document.getElementById('rect').setAttribute('fill', 'green')\" />\n"
            + "</svg>";

        final WebDriver driver = loadPage2(html, URL_FIRST, "image/svg+xml", ISO_8859_1);
        final WebElement rect = driver.findElement(By.id("rect"));

        assertEquals("blue", rect.getAttribute("fill"));
        rect.click();
        assertEquals("green", rect.getAttribute("fill"));
    }
}

