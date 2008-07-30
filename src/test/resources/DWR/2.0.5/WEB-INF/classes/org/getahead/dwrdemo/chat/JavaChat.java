package org.getahead.dwrdemo.chat;

import java.util.Collection;
import java.util.LinkedList;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwr.Util;
import org.directwebremoting.util.Logger;

/**
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class JavaChat
{
    /**
     * @param text The new message text to add
     */
    public void addMessage(String text)
    {
        // Make sure we have a list of the list 10 messages
        if (text != null && text.trim().length() > 0)
        {
            messages.addFirst(new Message(text));
            while (messages.size() > 10)
            {
                messages.removeLast();
            }
        }

        WebContext wctx = WebContextFactory.get();
        String currentPage = wctx.getCurrentPage();

        // Clear the input box in the browser that kicked off this page only
        Util utilThis = new Util(wctx.getScriptSession());
        utilThis.setValue("text", "");

        // For all the browsers on the current page:
        Collection sessions = wctx.getScriptSessionsByPage(currentPage);
        Util utilAll = new Util(sessions);

        // Clear the list and add in the new set of messages
        utilAll.removeAllOptions("chatlog");
        utilAll.addOptions("chatlog", messages, "text");
    }

    /**
     * The current set of messages
     */
    private LinkedList messages = new LinkedList();

    /**
     * The log stream
     */
    protected static final Logger log = Logger.getLogger(JavaChat.class);
}
