/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests methods in {@link HttpWebConnection}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HttpWebConnection3Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "<body>host,user-agent,", IE = "<body>")
    public void headerOrder() throws Exception {
        final Map<String, Class< ? extends Servlet>> servlets = new HashMap<String, Class< ? extends Servlet>>();
        servlets.put("/test", HeaderOrderServlet.class);
        startWebServer("./", null, servlets);
        final WebDriver driver = getWebDriver();

        driver.get("http://localhost:" + PORT + "/test");
        final String body = driver.getPageSource().toLowerCase().replaceAll("\\s", "");
        assertTrue(body.contains(getExpectedAlerts()[0]));
    }

    /**
     * Servlet for {@link #headerOrder()}.
     */
    public static class HeaderOrderServlet extends HttpServlet {

        private static final long serialVersionUID = -6422333931337496616L;

        /**
         * {@inheritDoc}
         */
        @Override
        @SuppressWarnings("unchecked")
        protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
            response.setContentType("text/html");
            final Writer writer = response.getWriter();
            for (final Enumeration<String> en = request.getHeaderNames(); en.hasMoreElements();) {
                writer.write(en.nextElement() + ",");
            }
            writer.close();
        }
    }
}
