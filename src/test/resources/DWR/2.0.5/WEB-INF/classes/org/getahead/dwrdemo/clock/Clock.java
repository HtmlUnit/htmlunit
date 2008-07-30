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
package org.getahead.dwrdemo.clock;

import java.util.Collection;
import java.util.Date;

import javax.servlet.ServletContext;

import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwr.Util;
import org.directwebremoting.util.Logger;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Clock implements Runnable
{
    /**
     *
     */
    public Clock()
    {
        ServletContext servletContext = WebContextFactory.get().getServletContext();
        sctx = ServerContextFactory.get(servletContext);
    }

    /**
     *
     */
    public synchronized void toggle()
    {
        active = !active;

        if (active)
        {
            new Thread(this).start();
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        try
        {
            log.debug("CLOCK: Starting server-side thread");

            while (active)
            {
                Collection sessions = sctx.getScriptSessionsByPage("/dwr/clock/index.html");
                Util pages = new Util(sessions);
                pages.setValue("clockDisplay", new Date().toString());

                log.debug("Sent message");
                Thread.sleep(1000);
            }

            Collection sessions = sctx.getScriptSessionsByPage("/dwr/clock/index.html");
            Util pages = new Util(sessions);
            pages.setValue("clockDisplay", "");

            log.debug("CLOCK: Stopping server-side thread");
        }
        catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Our key to get hold of ServerContexts
     */
    private ServerContext sctx;

    /**
     * Are we updating the clocks on all the pages?
     */
    private transient boolean active = false;

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(Clock.class);
}
