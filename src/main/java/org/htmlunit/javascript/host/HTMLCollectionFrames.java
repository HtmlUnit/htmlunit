/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host;

import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.WebWindow;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.html.BaseFrameElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.FrameWindow;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.javascript.host.html.HTMLCollection;

/**
 * The frames of a window ({@code window.frames}).
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
class HTMLCollectionFrames extends HTMLCollection {
    private static final Log LOG = LogFactory.getLog(HTMLCollectionFrames.class);

    HTMLCollectionFrames(final HtmlPage page) {
        super(page, false);
        this.setIsMatchingPredicate((Predicate<DomNode> & Serializable) node -> node instanceof BaseFrameElement);
    }

    @Override
    protected Scriptable getScriptableForElement(final Object obj) {
        final WebWindow window;
        if (obj instanceof BaseFrameElement element) {
            window = element.getEnclosedWindow();
        }
        else {
            window = ((FrameWindow) obj).getFrameElement().getEnclosedWindow();
        }

        return window.getScriptableObject();
    }

    @Override
    protected Object getWithPreemption(final String name) {
        final List<DomNode> elements = getElements();

        for (final Object next : elements) {
            final BaseFrameElement frameElt = (BaseFrameElement) next;
            final WebWindow window = frameElt.getEnclosedWindow();
            if (name.equals(window.getName())) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Property \"" + name + "\" evaluated (by name) to " + window);
                }
                return getScriptableForElement(window);
            }
        }

        return NOT_FOUND;
    }
}
