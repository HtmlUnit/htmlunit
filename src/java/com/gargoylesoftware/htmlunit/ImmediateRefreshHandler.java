/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit;

import java.net.URL;

/**
 * The default handler for page refreshes. This refresh handler immediately
 * refreshes the specified page, using the specified URL and ignoring the
 * wait time.
 * 
 * If you want a refresh handler that does not ignore the wait time,
 * see {@link ThreadedRefreshHandler}.
 * 
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 */
public class ImmediateRefreshHandler implements RefreshHandler {

    /**
     * Immediately refreshes the specified page using the specified URL.
     * @param page The page that is going to be refreshed.
     * @param url The URL where the new page will be loaded.
     * @param seconds The number of seconds to wait before reloading the page (ignored!).
     */
    public void handleRefresh(final Page page, final URL url, final int seconds) {
        final WebWindow window = page.getEnclosingWindow();
        if( window == null ) {
            return;
        }
        final WebClient client = window.getWebClient();
        if( page.getWebResponse().getUrl().equals( url ) ) {
            String msg = "Attempted to refresh a page using an ImmediateRefreshHandler, " +
                "which would have caused an OutOfMemoryException; refresh aborted. " +
                "Please use a WaitRefreshHandler instead.";
            client.getLog().warn( msg );
            return;
        }
        try {
            client.getPage( window, new WebRequestSettings( url ) );
        }
        catch( final Exception e ) {
            client.getLog().error( "Unable to refresh page!", e );
        }
    }

}
