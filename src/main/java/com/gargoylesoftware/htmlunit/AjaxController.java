/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit;

import java.io.Serializable;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * This class is notified when AJAX calls are made, and has the ability to influence these calls.
 * For instance, it can turn asynchronous AJAX calls into synchronous AJAX calls, making test code
 * deterministic and avoiding calls to <tt>Thread.sleep()</tt>.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class AjaxController implements Serializable {

    /**
     * Gets notified of an AJAX call to determine how it should be processed.
     * @param page the page the request comes from
     * @param request the request that should be performed
     * @param async indicates if the request should originally be asynchron
     * @return if the call should be synchron or not; here just like the original call
     */
    public boolean processSynchron(final HtmlPage page, final WebRequest request, final boolean async) {
        return !async;
    }

}
