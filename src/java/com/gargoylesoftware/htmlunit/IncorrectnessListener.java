package com.gargoylesoftware.htmlunit;

/**
 * Interface to receive notification of incorrect information in html code 
 * (but not the parser messages), headers, ...
 * that HtmlUnit can handle but that denote a badly written application.
 * @author Marc Guillemot
 */
public interface IncorrectnessListener {
	/**
	 * Called to notify an incorrectness.
	 * @param message the explaination of the incorrectness
	 * @param origin the object that encountered this incorrectness
	 */
	void notify(final String message, final Object origin);
}
