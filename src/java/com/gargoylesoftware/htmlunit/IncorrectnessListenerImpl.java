package com.gargoylesoftware.htmlunit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Default implementation of {@link IncorrectnessListener} configured on {@link WebClient}.
 * Logs the notifications at WARN level to the originator's log.
 * @author Marc Guillemot
 */
public class IncorrectnessListenerImpl implements IncorrectnessListener {

	/**
	 * {@inheritDoc}
	 */
	public void notify(final String message, final Object origin) {
        final Log log = LogFactory.getLog(origin.getClass());
        log.warn(message);
	}
}
