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
package org.getahead.dwrdemo.intro;

import java.io.IOException;

import javax.servlet.ServletException;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * Used by the default webapp landing page to check basic functionallity
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Intro
{
    /**
     * A simple test that the DWr is working. Used by the front page.
     * @return The text of the insert.html page
     * @throws IOException From {@link WebContext#forwardToString(String)}
     * @throws ServletException From {@link WebContext#forwardToString(String)}
     */
    public String getInsert() throws ServletException, IOException
    {
        return WebContextFactory.get().forwardToString("/insert.html");
    }
}
