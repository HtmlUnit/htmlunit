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
package org.htmlunit.html.csp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.htmlunit.Page;

/**
 * A {@link ContentSecurityPolicyViolationListener} that collects all violations.
 * <p>
 * Use it to make tests fail on violations of the Content-Security-Policy:
 * </p>
 * <pre>
 * final CollectingContentSecurityPolicyViolationListener listener =
 *         new CollectingContentSecurityPolicyViolationListener();
 * webClient.setContentSecurityPolicyViolationListener(listener);
 *
 * webClient.getPage(url);
 * assertTrue(listener.getViolations().isEmpty());
 * </pre>
 *
 * @author Ronald Brill
 */
public class CollectingContentSecurityPolicyViolationListener
        implements ContentSecurityPolicyViolationListener, Serializable {

    private final List<ContentSecurityPolicyViolation> violations_ =
            Collections.synchronizedList(new ArrayList<>());

    /**
     * {@inheritDoc}
     */
    @Override
    public void violation(final Page page, final ContentSecurityPolicyViolation violation) {
        violations_.add(violation);
    }

    /**
     * Returns the collected violations.
     *
     * @return a copy of the collected violations (in the order they were detected)
     */
    public List<ContentSecurityPolicyViolation> getViolations() {
        synchronized (violations_) {
            return new ArrayList<>(violations_);
        }
    }

    /**
     * Removes all collected violations.
     */
    public void clear() {
        violations_.clear();
    }
}
