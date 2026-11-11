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

/**
 * Content Security Policy (CSP) support: the {@link org.htmlunit.html.csp.ContentSecurityPolicy}
 * of an {@link org.htmlunit.html.HtmlPage} (from the Content-Security-Policy headers and meta elements),
 * checks for scripts, styles, images, frames, connections and more against the policy,
 * and the reporting of violations to a {@link org.htmlunit.html.csp.ContentSecurityPolicyViolationListener}.
 */
package org.htmlunit.html.csp;
