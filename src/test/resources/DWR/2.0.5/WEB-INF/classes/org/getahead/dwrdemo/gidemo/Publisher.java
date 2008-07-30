package org.getahead.dwrdemo.gidemo;

import java.util.Collection;
import java.util.Random;

import javax.servlet.ServletContext;

import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.ScriptProxy;
import org.directwebremoting.util.Logger;

/**
 * A generator of random objects to push to GI
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class Publisher implements Runnable
{
    /**
     * Create a new publish thread and start it
     */
    public Publisher()
    {
        WebContext webContext = WebContextFactory.get();
        ServletContext servletContext = webContext.getServletContext();

        serverContext = ServerContextFactory.get(servletContext);

        // A bit nasty: the call to serverContext.getScriptSessionsByPage()
        // below could fail because the system might need to read web.xml which
        // means it needs a ServletContext, which is only available  using
        // WebContext, which in turn requires a DWR thread. We can cache the
        // results simply by calling this in a DWR thread, as we are now.
        webContext.getScriptSessionsByPage("");

        synchronized (Publisher.class)
        {
            if (worker == null)
            {
                worker = new Thread(this, "Publisher");
                worker.start();
            }
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        try
        {
            log.info("Starting Publisher thread");

            while (!Thread.currentThread().isInterrupted())
            {
                Collection sessions = serverContext.getScriptSessionsByPage("/dwr/gi/index.html");
                ScriptProxy proxy = new ScriptProxy(sessions);

                Corporation corp = corporations.getNextChangedCorporation();
                proxy.addFunctionCall("OpenAjax.publish", "gidemo", "corporation", corp);

                int timeToSleep = random.nextInt(2500);
                Thread.sleep(timeToSleep);
            }
        }
        catch (InterruptedException ex)
        {
            // Ignore, we expect this
        }
        finally
        {
            log.info("Stopping Publisher thread");
        }
    }

    /**
     * The thread that does the work
     */
    protected static Thread worker;

    /**
     * The set of corporations that we manage
     */
    private Corporations corporations = new Corporations();

    /**
     * We use DWRs ServerContext to find users of a given page
     */
    private ServerContext serverContext;

    /**
     * Used to generate random data
     */
    private Random random = new Random();

    /**
     * The log stream
     */
    private static final Logger log = Logger.getLogger(Publisher.class);
}
