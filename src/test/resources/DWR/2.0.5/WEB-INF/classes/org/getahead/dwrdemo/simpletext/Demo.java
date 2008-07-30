/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.getahead.dwrdemo.simpletext;

import java.io.IOException;

import javax.servlet.ServletException;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * Some simple text demos
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Demo
{
    /**
     * Return a server side string to display on the client in real time
     * @param name The name of person to say hello to
     * @return A demo string
     */
    public String sayHello(String name)
    {
        return "Hello, " + name;
    }

    /**
     * Fetch a resource using forwardToString()
     * @return a demo HTML page
     * @throws ServletException If the servlet engine breaks
     * @throws IOException If the servlet engine breaks
     */
    public String getInclude() throws ServletException, IOException
    {
        WebContext wctx = WebContextFactory.get();
        return wctx.forwardToString("/simpletext/forward.html");
    }
}
