/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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
import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests using the Tomcat.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HttpWebConnection4Test extends WebDriverTestCase {

    private Tomcat tomcat_;

    /**
     * Test case for Bug #1882.
     *
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    public void queryString() throws Exception {
        tomcat_ = new Tomcat();

        tomcat_.setPort(PORT);
        tomcat_.setBaseDir("target/tomcat");

        final Context context = tomcat_.addContext("", new File(".").getAbsolutePath());
        Tomcat.addServlet(context, "something", new HttpServlet() {
            @Override
            protected void service(final HttpServletRequest request, final HttpServletResponse response)
                    throws ServletException, IOException {
                String string = request.getQueryString();
                if (string == null) {
                    string = "";
                }
                response.getWriter().write(string);
            }
        });
        context.addServletMappingDecoded("/*", "something");
        tomcat_.start();

        final WebDriver driver = getWebDriver();

        driver.get("http://localhost:" + PORT + "?para=%u65E5");
        assertTrue(driver.getPageSource().contains("para=%u65E5"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @After
    public void stopServer() throws Exception {
        if (tomcat_ != null) {
            tomcat_.stop();
            tomcat_.destroy();
        }
    }
}
