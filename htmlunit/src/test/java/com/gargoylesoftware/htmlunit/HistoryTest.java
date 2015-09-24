/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link History}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HistoryTest extends WebDriverTestCase {

    /**
     * Tests going in history {@link History#back()} with {@code POST} request.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void post() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        servlets.put("/post1", Post1Servlet.class);
        servlets.put("/post2", Post2Servlet.class);
        servlets.put("/post3", Post3Servlet.class);
        startWebServer("./", new String[0], servlets);

        final WebDriver driver = getWebDriver();
        driver.get("http://localhost:" + PORT + "/post1");

        driver.findElement(By.id("mySubmit")).click();
        assertEquals(URL_FIRST + "post2", driver.getCurrentUrl());
        assertTrue(driver.getPageSource().contains("POST"));
        assertTrue(driver.getPageSource().contains("para1=value1"));

        driver.findElement(By.linkText("Go to GET")).click();
        assertEquals(URL_FIRST + "post3", driver.getCurrentUrl());
        assertTrue(driver.getPageSource().contains("GET"));

        driver.navigate().back();
        assertEquals(URL_FIRST + "post2", driver.getCurrentUrl());
        assertTrue(driver.getPageSource().contains("POST"));
        assertTrue(driver.getPageSource().contains("para1=value1"));
    }

    /**
     * Servlet for '/pos1'.
     */
    public static class Post1Servlet extends HttpServlet {

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            response.getWriter().write("<html>"
                + "<body><form action='post2' method='post'>\n"
                + "Name: <input type='hidden' name='para1' value='value1'><br>\n"
                + "<input type='submit' value='Click Me' id='mySubmit'>\n"
                + "</form></body></html>\n");
        }
    }

    /**
     * Servlet for '/post2'.
     */
    public static class Post2Servlet extends HttpServlet {

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            final Writer writer = response.getWriter();
            writer.write("POST<br>\n");
            for (final Enumeration<String> en = request.getParameterNames(); en.hasMoreElements();) {
                final String key = en.nextElement();
                writer.write(key + "=" + request.getParameter(key) + "<br>\n");
            }
            writer.write("<a href='post3'>Go to GET</a>\n");
            writer.close();
        }
    }

    /**
     * Servlet for '/post3'.
     */
    public static class Post3Servlet extends HttpServlet {

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
                throws ServletException, IOException {
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            final Writer writer = response.getWriter();
            writer.write("GET<br>\n");
        }
    }

}
