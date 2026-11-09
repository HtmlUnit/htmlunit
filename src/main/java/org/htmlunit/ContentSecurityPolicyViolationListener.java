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
package org.htmlunit;

/**
 * A listener for violations of the Content-Security-Policy of a page.
 * <p>
 * The listener is called (synchronously) for every violation detected by HtmlUnit,
 * for enforced policies (the action was blocked) as well as for report only policies
 * (the action was allowed); see {@link ContentSecurityPolicyViolation#isReportOnly()}.
 * Unlike the securitypolicyviolation event, the listener is also called if JavaScript is disabled.
 * </p>
 * <p>
 * The listener is called while HtmlUnit processes the page (e.g. while parsing or while a
 * script is executed); therefore it should not throw exceptions or modify the page.
 * To make tests fail on violations, collect them (e.g. with
 * {@link CollectingContentSecurityPolicyViolationListener}) and check them afterwards.
 * </p>
 *
 * @author Ronald Brill
 * @see WebClient#setContentSecurityPolicyViolationListener(ContentSecurityPolicyViolationListener)
 */
@FunctionalInterface
public interface ContentSecurityPolicyViolationListener {

    /**
     * Informs about a violation of the Content-Security-Policy.
     *
     * @param page the page whose policy was violated; for frame-ancestors violations
     *        this is the page containing the frame
     * @param violation the details of the violation
     */
    void violation(Page page, ContentSecurityPolicyViolation violation);
}
