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
package org.htmlunit.javascript.host.html;

import org.htmlunit.corejs.javascript.NativePromise;
import org.htmlunit.html.HtmlMedia;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstant;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.host.dom.DOMException;
import org.htmlunit.javascript.host.dom.Node;

/**
 * The JavaScript object {@code HTMLMediaElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class HTMLMediaElement extends HTMLElement {

    /**
     * No information is available about the media resource.
     */
    @JsxConstant
    public static final int HAVE_NOTHING = 0;

    /**
     * Enough of the media resource has been retrieved that the metadata attributes are initialized.
     * Seeking will no longer raise an exception.
     */
    @JsxConstant
    public static final int HAVE_METADATA = 1;

    /**
     * Data is available for the current playback position, but not enough to actually play more than one frame.
     */
    @JsxConstant
    public static final int HAVE_CURRENT_DATA = 2;

    /**
     * Data for the current playback position as well as for at least a little bit of time
     * into the future is available (in other words, at least two frames of video, for example).
     */
    @JsxConstant
    public static final int HAVE_FUTURE_DATA = 3;

    /**
     * Enough data is available—and the download rate is high enough—that the media
     * can be played through to the end without interruption.
     */
    @JsxConstant
    public static final int HAVE_ENOUGH_DATA = 4;

    /** There is no data yet. */
    @JsxConstant
    public static final int NETWORK_EMPTY = 0;

    /** Network is idle. */
    @JsxConstant
    public static final int NETWORK_IDLE = 1;

    /** The media is loading. */
    @JsxConstant
    public static final int NETWORK_LOADING = 2;

    /** There is no source. */
    @JsxConstant
    public static final int NETWORK_NO_SOURCE = 3;

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Determines whether the specified media type can be played back.
     * @param type the type
     * @return "probably", "maybe", or ""
     */
    @JsxFunction
    public String canPlayType(final String type) {
        final HtmlMedia element = (HtmlMedia) getDomNodeOrNull();
        if (element == null) {
            return "maybe";
        }
        return element.canPlayType(type);
    }

    /**
     * Begins playback of the media.
     *
     * @return a Promise which is fulfilled when playback has been started,
     *         or is rejected if for any reason playback cannot be started
     */
    @JsxFunction
    public NativePromise play() {
        return setupRejectedPromise(() ->
                    new DOMException("HtmlUnit does not support media play().", DOMException.NOT_FOUND_ERR));
    }

    /**
     * Pauses playback of the media.
     */
    @JsxFunction
    public void pause() {
        // nothing to do
    }

    /**
     * Resets the media element to its initial state and begins the process
     * of selecting a media source and loading the media in preparation
     * for playback to begin at the beginning.
     */
    @JsxFunction
    public void load() {
        // nothing to do
    }

    /**
     * Gets the JavaScript property {@code nodeType} for the current node.
     * @return the node type
     */
    @JsxGetter
    @Override
    public int getNodeType() {
        final HtmlMedia element = (HtmlMedia) getDomNodeOrNull();
        if (element == null) {
            return Node.ELEMENT_NODE;
        }
        return element.getNodeType();
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter
    @Override
    public String getNodeName() {
        return getNodeNameCustomize();
    }

    /**
     * Separate method to be able to override this in subclasses.
     *
     * @return the node name
     */
    protected String getNodeNameCustomize() {
        final HtmlMedia element = (HtmlMedia) getDomNodeOrNull();
        if (element == null) {
            return "MEDIA";
        }
        return element.getNodeName();
    }

    /**
     * Returns the URL of the audio to embed.
     * @return the value of the {@code src} attribute
     */
    @JsxGetter
    public String getSrc() {
        final HtmlMedia media = (HtmlMedia) getDomNodeOrDie();
        return media.getSrc();
    }

    /**
     * Sets the value of the {@code src} attribute.
     * @param src the value of the {@code src} attribute
     */
    @JsxSetter
    public void setSrc(final String src) {
        final HtmlMedia media = (HtmlMedia) getDomNodeOrDie();
        media.setSrc(src);
    }

    /**
     * Returns the absolute URL of the chosen media resource.
     * This could happen, for example, if the web server selects a
     * media file based on the resolution of the user's display.
     * The value is an empty string if the networkState property is EMPTY.
     * @return the absolute URL of the chosen media resource
     */
    @JsxGetter
    public String getCurrentSrc() {
        final HtmlMedia media = (HtmlMedia) getDomNodeOrDie();
        return media.getCurrentSrc();
    }
}
