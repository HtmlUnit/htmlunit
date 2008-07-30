/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.getahead.dwrdemo.filter;

import java.lang.reflect.Method;

import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterChain;


/**
 * An example filter that uses a fairly random event to define it's security
 * policy: If the current system time (in milliseconds) is even then the call
 * is allowed, otherwise it is denied.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class RandomSecurityAjaxFilter implements AjaxFilter
{
    /* (non-Javadoc)
     * @see uk.ltd.getahead.dwr.AjaxFilter#doFilter(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], uk.ltd.getahead.dwr.AjaxFilterChain)
     */
    public Object doFilter(Object obj, Method method, Object[] params, AjaxFilterChain chain) throws Exception
    {
        if (System.currentTimeMillis() % 2 == 1)
        {
            return chain.doFilter(obj, method, params);
        }
        else
        {
            throw new SecurityException("Wrong time. Try again later");
        }
    }
}
