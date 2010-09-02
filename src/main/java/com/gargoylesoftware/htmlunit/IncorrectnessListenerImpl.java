/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Default implementation of {@link IncorrectnessListener} configured on {@link WebClient}.
 * Logs the notifications at WARN level to the originator's log.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class IncorrectnessListenerImpl implements IncorrectnessListener, Serializable  {

    private static final Log LOG = LogFactory.getLog(IncorrectnessListenerImpl.class);

    /**
     * {@inheritDoc}
     */
    public void notify(final String message, final Object origin) {
        LOG.warn(message);
    }

}
