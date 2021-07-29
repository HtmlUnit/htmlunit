/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.httpclient;

import org.apache.hc.client5.http.impl.DefaultRedirectStrategy;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.ProtocolException;
import org.apache.hc.core5.http.protocol.HttpContext;

/**
 * Customized DefaultRedirectStrategy for HtmlUnit.
 *
 * @author Ronald Brill
 */
public final class HtmlUnitRedirectStrategie extends DefaultRedirectStrategy {

    @Override
    public boolean isRedirected(final HttpRequest request, final HttpResponse response,
            final HttpContext context) throws ProtocolException {
        return super.isRedirected(request, response, context) && response.getFirstHeader("location") != null;
    }
}
