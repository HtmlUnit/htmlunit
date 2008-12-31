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
package com.gargoylesoftware.htmlunit.javascript;

/**
 * An action triggered by a script execution but that should be executed first when the script is finished.
 * Example: when a script sets the source of an (i)frame, the request to the specified page will be first
 * triggered after the script execution.
 * @version $Revision$
 * @author Marc Guillemot
 */
public interface PostponedAction {
    /**
     * Execute the action.
     * @throws Exception if it fails
     */
    void execute() throws Exception;
}
