/*
 *  Copyright (C) 2002, 2003 Gargoyle Software Inc. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit;

/**
 *  A credential provider that provides the same userid/password combination for
 *  any realm on any server.
 *
 * @version  $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public final class SimpleCredentialProvider implements CredentialProvider {

    private final KeyValuePair keyValuePair_;


    /**
     *  Create an instance
     *
     * @param  userId The user id
     * @param  password The password
     */
    public SimpleCredentialProvider( final String userId, final String password ) {
        Assert.notNull( "userId", userId );
        Assert.notNull( "password", password );

        keyValuePair_ = new KeyValuePair( userId, password );
    }


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
    public KeyValuePair getCredentialsFor(
        final String server, final int port, final String realm ) {

        return keyValuePair_;
    }


    /**
     * Return a string representation of this object.
     * @return a string representation of this object.
     */
    public String toString() {
        return "SimpleCredentialProvider[userId=\""+keyValuePair_.getKey()+"\"]";
    }
}

