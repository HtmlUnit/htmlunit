/*
 *  Copyright (C) 2002 Gargoyle Software. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

/**
 *  An object that can provide the appropriate username and password to access a
 *  given realm on a web server
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public interface CredentialProvider {
    /**
     *  Return a KeyValuePair containing the userid (key) and password (value)
     *  for the specified server and realm
     *
     * @param  realm The realm that we are trying to access
     * @param  server The server that we are trying to access
     * @param  port The specific port on that server
     * @return  The userid/password or null if there are no credentials for this
     *      realm
     */
    KeyValuePair getCredentialsFor( final String server, final int port, final String realm );
}

