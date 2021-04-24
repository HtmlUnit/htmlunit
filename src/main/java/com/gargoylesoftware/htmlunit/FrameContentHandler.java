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
package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.html.BaseFrameElement;

/**
 * Interface to customize the handling of frame content.
 * At the moment this only enables the user to make a decision to load the
 * frame content or not.
 *
 * @author Ronald Brill
 */
public interface FrameContentHandler {

    /**
     * Called to decide if the content (referred from the source attribute)
     * should be loaded or not.
     *
     * @param baseFrameElement the element that likes to load the content
     * @return true if the content should be loaded
     */
    boolean loadFrameDocument(BaseFrameElement baseFrameElement);
}
