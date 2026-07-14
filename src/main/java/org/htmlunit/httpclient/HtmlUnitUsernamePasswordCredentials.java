/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.httpclient;

import java.io.Serializable;
import java.security.Principal;

import org.apache.hc.client5.http.auth.Credentials;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;

/**
 * Wrapper for {@link UsernamePasswordCredentials} to avoid direct references spread around.
 *
 * @author Ronald Brill
 * @author Lai Quang Duong
 */
public class HtmlUnitUsernamePasswordCredentials implements Credentials, Serializable {

    private final UsernamePasswordCredentials httpClientCreds_;

    /**
     * The constructor with the username and password arguments.
     *
     * @param userName the user name
     * @param password the password
     */
    public HtmlUnitUsernamePasswordCredentials(final String userName, final char[] password) {
        httpClientCreds_ = new UsernamePasswordCredentials(userName, password);
    }

    @Override
    public char[] getPassword() {
        return httpClientCreds_.getPassword();
    }

    @Override
    public Principal getUserPrincipal() {
        return httpClientCreds_.getUserPrincipal();
    }

    @Override
    public int hashCode() {
        return getUserPrincipal().hashCode();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof HtmlUnitUsernamePasswordCredentials that) {
            final Principal thisPrincipal = this.getUserPrincipal();
            final Principal thatPrincipal = that.getUserPrincipal();
            if (thisPrincipal != null && thisPrincipal.equals(thatPrincipal)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return getUserPrincipal().toString();
    }
}
