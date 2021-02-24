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
package com.gargoylesoftware.htmlunit.javascript.host.media;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;

/**
 * A JavaScript object for {@code GainNode}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass({CHROME, EDGE, FF, FF78})
public class GainNode extends AudioNode {

    private BaseAudioContext baseAudioContext_;
    private AudioParam gain_;

    /**
     * Creates an instance.
     */
    public GainNode() {
    }

    @JsxConstructor
    public void jsConstructor(final Object baCtx) {
        if (!(baCtx instanceof BaseAudioContext)) {
            throw ScriptRuntime.typeError(
                    "Failed to construct 'GainNode': first parameter is not of type 'BaseAudioContext'.");
        }

        baseAudioContext_ = (BaseAudioContext) baCtx;

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
