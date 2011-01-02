/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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
import java.util.ArrayList;
import java.util.List;

/**
 * A simple alert handler that keeps track of alerts in a list.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class CollectingAlertHandler implements AlertHandler, Serializable {

    private final List<String> collectedAlerts_;

    /**
     * Creates a new instance, initializing it with an empty list.
     */
    public CollectingAlertHandler() {
        this(new ArrayList<String>());
    }

    /**
     * Creates an instance with the specified list.
     *
     * @param list the list to store alerts in
     */
    public CollectingAlertHandler(final List<String> list) {
        WebAssert.notNull("list", list);
        collectedAlerts_ = list;
    }

    /**
     * Handles the alert. This implementation will store the message in a list
     * for retrieval later.
     *
     * @param page the page that triggered the alert
     * @param message the message in the alert
     */
    public void handleAlert(final Page page, final String message) {
        collectedAlerts_.add(message);
    }

    /**
     * Returns a list containing the message portion of any collected alerts.
     * @return a list of alert messages
     */
    public List<String> getCollectedAlerts() {
        return collectedAlerts_;
    }
}
