/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.background;

/**
 * A JavaScript-triggered background job managed by a {@link JavaScriptJobManager}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public abstract class JavaScriptJob implements Runnable {

    /** The job ID. */
    private Integer id_;

    /**
     * Sets the job ID.
     * @param id the job ID
     */
    public void setId(final Integer id) {
        id_ = id;
    }

    /**
     * Returns the job ID.
     * @return the job ID
     */
    public Integer getId() {
        return id_;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "JavaScript Job " + id_;
    }

}
