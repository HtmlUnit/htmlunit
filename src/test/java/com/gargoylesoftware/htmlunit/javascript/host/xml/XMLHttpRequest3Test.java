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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.util.ServletContentWrapper;

/**
 * Additional tests for {@link XMLHttpRequest} using already WebDriverTestCase.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class XMLHttpRequest3Test extends WebDriverTestCase {
    private static String XHRInstantiation_ = "(window.XMLHttpRequest ? "
        + "new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP'))";

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("a=b,0")
    public void post() throws Exception {
        final Map<String, Class<? extends Servlet>> servlets = new HashMap<String, Class<? extends Servlet>>();
        servlets.put("/test1", PostServlet1.class);
        servlets.put("/test2", PostServlet2.class);
        startWebServer("./", new String[0], servlets);
        loadPageWithAlerts2(new URL("http://localhost:" + PORT + "/test1"));
    }

    /**
     * Servlet for {@link #post()}.
     */
    public static class PostServlet1 extends ServletContentWrapper {
        private static final long serialVersionUID = -439785252750617301L;

        /** Constructor. */
        public PostServlet1() {
            super(getModifiedContent("<html><head><script>\n"
                    + "function test() {\n"
                    + "  var xhr = " + XHRInstantiation_ + ";\n"
                    + "  xhr.open('POST', '/test2?a=b', false);\n"
                    + "  xhr.send('');\n"
                    + "  alert(xhr.responseText);\n"
                    + "}\n"
                    + "</script></head><body onload='test()'></body></html>"));
        }
    }

    /**
     * Servlet for {@link #post()}.
     */
    public static class PostServlet2 extends HttpServlet {
        private static final long serialVersionUID = 5286696645520397377L;

        @Override
        protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
            final Writer writer = resp.getWriter();
            writer.write(req.getQueryString() + ',' + req.getContentLength());
            writer.close();
        }
    }
}
