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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link History}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
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
     * Tests going in history {@link History#back()} with {@code POST} request.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("49")
    // limit varies for IE
    public void historyCache() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<>();
        final int testDeep = 70;

        for (int i = 0; i < testDeep; i++) {
            servlets.put("/post" + i, Post1Servlet.class);
        }
        startWebServer("./", new String[0], servlets);

        final WebDriver driver = getWebDriver();

        int count = Post1Servlet.Count_;
        for (int i = 0; i < testDeep; i++) {
            driver.get("http://localhost:" + PORT + "/post" + i);
            assertTrue(driver.getPageSource(), driver.getPageSource().contains("Call: " + (i + count)));
        }

        count = Post1Servlet.Count_;
        for (int i = 0; i < testDeep - 1; i++) {
            driver.navigate().back();
            if (!driver.getPageSource().contains("Call: " + (count - i - 2))) {
                assertEquals(Integer.parseInt(getExpectedAlerts()[0]), i);
                return;
            }

            if (count != Post1Servlet.Count_) {
                Assert.fail("Server called for " + i);
                break;
            }
        }

        assertEquals(getExpectedAlerts()[0], "done");
    }

    /**
     * Servlet for '/post1'.
     */
    public static class Post1Servlet extends HttpServlet {
        private static int Count_ = 0;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {

            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html");
            response.getWriter().write(
                    "<html>\n"
                    + "<body>\n"
                    + "  <h1>Call: " + Count_ + "</h1>\n"
                    + "  <form action='post2' method='post'>\n"
                    + "    <input type='hidden' name='para1' value='value1'>\n"
                    + "    <input type='submit' value='Click Me' id='mySubmit'>\n"
                    + "  </form>\n"
                    + "</body>\n"
                    + "</html>\n");

            Count_++;
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
