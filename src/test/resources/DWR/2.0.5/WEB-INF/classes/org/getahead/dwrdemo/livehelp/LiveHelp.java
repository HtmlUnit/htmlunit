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
package org.getahead.dwrdemo.livehelp;

import java.util.Collection;

import org.directwebremoting.Security;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwr.Util;
import org.directwebremoting.util.Logger;

/**
 * A simple shared input form that several users can share
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class LiveHelp
{
    /**
     * Something has changed
     * @param id The id of the field that changed
     * @param value The new value
     */
    public void notifyTyping(String id, String value)
    {
        Util utilAll = new Util(getUsersToAffect());

        utilAll.setValue(id, Security.replaceXmlCharacters(value));
    }

    /**
     * The user has tabbed in
     * @param id The id of the field that changed
     */
    public void notifyFocus(String id)
    {
        Util utilAll = new Util(getUsersToAffect());

        utilAll.addClassName(id, "disabled");
        String addr = WebContextFactory.get().getHttpServletRequest().getRemoteAddr();
        utilAll.setValue(id + "Tip", addr);

        //utilAll.addScript("$('" + id + "').disabled = true;");
    }

    /**
     * The user has tabbed out
     * @param id The id of the field that changed
     */
    public void notifyBlur(String id)
    {
        Util utilAll = new Util(getUsersToAffect());

        utilAll.removeClassName(id, "disabled");
        utilAll.setValue(id + "Tip", "");

        //utilAll.addScript("$('" + id + "').disabled = false;");
    }

    /**
     * @return The collection of people to affect
     */
    private Collection getUsersToAffect()
    {
        WebContext wctx = WebContextFactory.get();
        String currentPage = wctx.getCurrentPage();

        // For all the browsers on the current page:
        Collection sessions = wctx.getScriptSessionsByPage(currentPage);

        // But not the current user!
        sessions.remove(wctx.getScriptSession());

        log.debug("Affecting " + sessions.size() + " users");

        return sessions;
    }

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(LiveHelp.class);
}
