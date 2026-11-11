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
package org.htmlunit.html;

import java.net.MalformedURLException;
import java.util.Map;

import org.htmlunit.SgmlPage;
import org.htmlunit.javascript.PostponedAction;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.util.StringUtils;

/**
 * HTML Media element, e.g. {@link HtmlAudio} or {@link HtmlVideo}.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
public abstract class HtmlMedia extends HtmlElement {

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    private boolean childrenChecked_;
    private String checkedSrc_;

    HtmlMedia(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * {@inheritDoc}
     * Checks the url against the Content-Security-Policy (media-src); the source and track
     * children are checked after they are added.
     */
    @Override
    protected void onAddedToPage() {
        super.onAddedToPage();
        childrenChecked_ = false;
        final String src = getAttributeDirect(SRC_ATTRIBUTE);
        // the src may be checked already (set by script before the element was added)
        if (ATTRIBUTE_NOT_DEFINED != src && !src.equals(checkedSrc_)) {
            checkSrc(src);
        }
        if (getFirstElementChild() != null) {
            checkChildrenContentSecurityPolicy();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAllChildrenAddedToPage(final boolean postponed) {
        super.onAllChildrenAddedToPage(postponed);
        if (!childrenChecked_) {
            checkChildrenContentSecurityPolicy();
        }
    }

    /**
     * {@inheritDoc}
     * Checks the new src url against the Content-Security-Policy (media-src); like the browsers,
     * also if the element is not attached to the page.
     */
    @Override
    protected void setAttributeNS(final String namespaceURI, final String qualifiedName, final String value,
            final boolean notifyAttributeChangeListeners, final boolean notifyMutationObservers) {
        final String qualifiedNameLC = StringUtils.toRootLowerCase(qualifiedName);
        final boolean changed = SRC_ATTRIBUTE.equals(qualifiedNameLC)
                && !getAttributeNS(namespaceURI, qualifiedNameLC).equals(value);

        super.setAttributeNS(namespaceURI, qualifiedName, value,
                notifyAttributeChangeListeners, notifyMutationObservers);

        if (changed) {
            checkSrc(value);
        }
    }

    private void checkSrc(final String src) {
        checkedSrc_ = src;
        if (!ContentSecurityPolicy.allowsMedia(this, src)) {
            fireErrorEvent(this);
        }
    }

    /**
     * Fires the error event (async) at the element blocked by the Content-Security-Policy.
     */
    private static void fireErrorEvent(final HtmlElement element) {
        final HtmlPage page = element.getHtmlPageOrNull();
        if (page == null || !page.getWebClient().isJavaScriptEngineEnabled()) {
            return;
        }

        final PostponedAction action = new PostponedAction(page, "HtmlMedia.blocked") {
            @Override
            public void execute() {
                element.fireEvent(Event.TYPE_ERROR);
            }
        };
        if (READY_STATE_LOADING.equals(page.getReadyState())) {
            page.addAfterLoadAction(action);
        }
        else {
            page.getWebClient().getJavaScriptEngine().addPostponedAction(action);
        }
    }

    /**
     * Checks the urls of the children against the Content-Security-Policy (media-src): without
     * a src attribute the source children (in order, until the first allowed one) and the default track.
     */
    private void checkChildrenContentSecurityPolicy() {
        childrenChecked_ = true;
        final HtmlPage page = getHtmlPageOrNull();
        if (page == null || page.getContentSecurityPolicy() == null) {
            return;
        }

        if (!hasAttribute(SRC_ATTRIBUTE)) {
            for (final DomNode child : getChildren()) {
                if (child instanceof HtmlSource source) {
                    final String sourceSrc = source.getAttributeDirect(SRC_ATTRIBUTE);
                    if (!StringUtils.isBlank(sourceSrc)) {
                        if (ContentSecurityPolicy.allowsMedia(source, sourceSrc)) {
                            break;
                        }
                        fireErrorEvent(source);
                    }
                }
            }
        }

        for (final DomNode child : getChildren()) {
            if (child instanceof HtmlTrack track && track.hasAttribute("default")) {
                if (!ContentSecurityPolicy.allowsMedia(track, track.getAttributeDirect(SRC_ATTRIBUTE))) {
                    fireErrorEvent(track);
                }
                break;
            }
        }
    }

    /**
     * Returns the value of the attribute {@code src}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code src} or an empty string if that attribute isn't defined
     */
    public final String getSrcAttribute() {
        return getSrcAttributeNormalized();
    }

    /**
     * Returns the fully qualified source URL.
     *
     * @return the fully qualified value of the {@code src} attribute
     */
    public String getSrc() {
        final String src = getSrcAttribute();
        if (StringUtils.isEmptyString(src)) {
            return src;
        }
        try {
            final HtmlPage page = (HtmlPage) getPage();
            return page.getFullyQualifiedUrl(src).toExternalForm();
        }
        catch (final MalformedURLException e) {
            final String msg = "Unable to create fully qualified URL for src attribute of media " + e.getMessage();
            throw new RuntimeException(msg, e);
        }
    }

    /**
     * Sets the value of the {@code src} attribute.
     * @param src the value of the {@code src} attribute
     */
    public void setSrc(final String src) {
        setAttribute(SRC_ATTRIBUTE, src);
    }

    /**
     * Returns the absolute URL of the chosen media resource.
     * This could happen, for example, if the web server selects a
     * media file based on the resolution of the user's display.
     * The value is an empty string if the networkState property is EMPTY.
     * @return the absolute URL of the chosen media resource
     */
    public String getCurrentSrc() {
        return "";
    }
}
