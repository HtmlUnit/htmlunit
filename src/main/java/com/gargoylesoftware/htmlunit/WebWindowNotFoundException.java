/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

/**
 * Exception to indicate that no {@link WebWindow} could be found that matched
 * a given name.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class WebWindowNotFoundException extends RuntimeException {
    private final String name_;

    /**
     * Creates an instance.
     * @param name the name that was searched by
     */
    public WebWindowNotFoundException(final String name) {
        super("Searching for [" + name + "]");
        name_ = name;
    }

    /**
     * Returns the name of the {@link WebWindow} that wasn't found.
     * @return the name
     */
    public String getName() {
        return name_;
    }
}
