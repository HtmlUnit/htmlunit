/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.gae;

/**
 * Utilities for <a href="http://code.google.com/appengine/">Google App Engine</a>
 * support.
 *
 * @version $Revision$
 * @author Amit Manjhi
 */
public final class GAEUtils {

    private GAEUtils() {
    }

    /**
     * Indicates if current JVM is running on Google App Engine.
     * @see <a href="http://code.google.com/appengine/docs/java/runtime.html#The_Environment>GAE documentation</a>
     * @return true if running in GAE mode.
     */
    public static boolean isGaeMode() {
        return System.getProperty("com.google.appengine.runtime.environment") != null;
    }
}
