/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.media;

import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for {@code GainNode}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class GainNode extends AudioNode {

    private AudioParam gain_;

    @Override
    @JsxConstructor
    public void jsConstructor(final Object baCtx) {
        super.jsConstructor(baCtx);

        final AudioParam node = new AudioParam();
        node.setParentScope(getParentScope());
        node.setPrototype(getPrototype(node.getClass()));
        node.jsConstructor();
        gain_ = node;
    }

    /**
     * @return an a-rate AudioParam representing the amount of gain to apply.
     */
    @JsxGetter
    public AudioParam getGain() {
        return gain_;
    }

}
