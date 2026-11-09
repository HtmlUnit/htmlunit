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

import java.io.Serializable;

/**
 * The details of a violation of the Content-Security-Policy of a page.
 * The properties correspond to the ones of the securitypolicyviolation event
 * (see <a href="https://w3c.github.io/webappsec-csp/#violation-events">CSP - Violation Events</a>).
 * <ul>
 *   <li>blockedUri: the blocked url; 'inline' for inline scripts, event handler attributes and
 *       javascript: urls, 'eval' for the evaluation of strings; only the scheme for urls
 *       without host (e.g. 'data'); for frame-ancestors the ancestor not allowed to embed
 *       the document</li>
 *   <li>effectiveDirective: the directive whose enforcement caused the violation
 *       (e.g. 'script-src-elem', 'script-src-attr', 'script-src', 'connect-src', 'frame-ancestors')</li>
 *   <li>sourceFile, lineNumber, columnNumber: the location of the code causing the violation;
 *       an empty string or 0 if not known</li>
 *   <li>sample: the first 40 characters of the blocked code if the directive contains
 *       'report-sample', an empty string otherwise</li>
 * </ul>
 *
 * @author Ronald Brill
 * @see ContentSecurityPolicyViolationListener
 */
public final class ContentSecurityPolicyViolation implements Serializable {

    private final String documentUri_;
    private final String blockedUri_;
    private final String effectiveDirective_;
    private final String originalPolicy_;
    private final String sourceFile_;
    private final String sample_;
    private final int lineNumber_;
    private final int columnNumber_;
    private final int statusCode_;
    private final boolean reportOnly_;

    /**
     * Ctor.
     *
     * @param documentUri the url of the document
     * @param blockedUri the blocked url
     * @param effectiveDirective the directive whose enforcement caused the violation
     * @param originalPolicy the policy (serialized) that was violated
     * @param sourceFile the url of the source causing the violation
     * @param sample the sample of the blocked code
     * @param lineNumber the line number in the source file
     * @param columnNumber the column number in the source file
     * @param statusCode the http status code of the document
     * @param reportOnly whether the violated policy is a report only policy
     */
    public ContentSecurityPolicyViolation(final String documentUri,
            final String blockedUri,
            final String effectiveDirective,
            final String originalPolicy,
            final String sourceFile,
            final String sample,
            final int lineNumber,
            final int columnNumber,
            final int statusCode,
            final boolean reportOnly) {
        documentUri_ = documentUri;
        blockedUri_ = blockedUri;
        effectiveDirective_ = effectiveDirective;
        originalPolicy_ = originalPolicy;
        sourceFile_ = sourceFile;
        sample_ = sample;
        lineNumber_ = lineNumber;
        columnNumber_ = columnNumber;
        statusCode_ = statusCode;
        reportOnly_ = reportOnly;
    }

    /**
     * Returns the url of the document.
     *
     * @return the url of the document
     */
    public String getDocumentUri() {
        return documentUri_;
    }

    /**
     * Returns the blocked url.
     *
     * @return the blocked url
     */
    public String getBlockedUri() {
        return blockedUri_;
    }

    /**
     * Returns the directive whose enforcement caused the violation.
     *
     * @return the directive whose enforcement caused the violation
     */
    public String getEffectiveDirective() {
        return effectiveDirective_;
    }

    /**
     * Returns the policy (serialized) that was violated.
     *
     * @return the policy (serialized) that was violated
     */
    public String getOriginalPolicy() {
        return originalPolicy_;
    }

    /**
     * Returns the url of the source causing the violation.
     *
     * @return the url of the source causing the violation
     */
    public String getSourceFile() {
        return sourceFile_;
    }

    /**
     * Returns the sample of the blocked code.
     *
     * @return the sample of the blocked code
     */
    public String getSample() {
        return sample_;
    }

    /**
     * Returns the line number in the source file.
     *
     * @return the line number in the source file
     */
    public int getLineNumber() {
        return lineNumber_;
    }

    /**
     * Returns the column number in the source file.
     *
     * @return the column number in the source file
     */
    public int getColumnNumber() {
        return columnNumber_;
    }

    /**
     * Returns the http status code of the document.
     *
     * @return the http status code of the document
     */
    public int getStatusCode() {
        return statusCode_;
    }

    /**
     * Returns the violated directive; same as {@link #getEffectiveDirective()}.
     *
     * @return the violated directive
     */
    public String getViolatedDirective() {
        return effectiveDirective_;
    }

    /**
     * Returns whether the violated policy is a report only policy
     * (Content-Security-Policy-Report-Only header); in this case the action was not blocked.
     *
     * @return {@code true} if the violated policy is a report only policy
     */
    public boolean isReportOnly() {
        return reportOnly_;
    }

    /**
     * Returns the disposition of the violated policy ('enforce' or 'report').
     *
     * @return the disposition
     */
    public String getDisposition() {
        return reportOnly_ ? "report" : "enforce";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return (reportOnly_ ? "[Report Only] " : "")
                + "'" + effectiveDirective_ + "' blocked '" + blockedUri_ + "' in '" + documentUri_
                + "' (policy '" + originalPolicy_ + "')";
    }
}
