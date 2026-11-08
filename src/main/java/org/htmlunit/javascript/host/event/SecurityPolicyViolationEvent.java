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
package org.htmlunit.javascript.host.event;

import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.html.DomNode;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for {@code SecurityPolicyViolationEvent}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class SecurityPolicyViolationEvent extends Event {

    /** The event type. */
    public static final String TYPE_SECURITY_POLICY_VIOLATION = "securitypolicyviolation";

    /** Disposition of enforced policies. */
    public static final String DISPOSITION_ENFORCE = "enforce";

    /** Disposition of report only policies. */
    public static final String DISPOSITION_REPORT = "report";

    private String documentURI_ = "";
    private String referrer_ = "";
    private String blockedURI_ = "";
    private String effectiveDirective_ = "";
    private String violatedDirective_ = "";
    private String originalPolicy_ = "";
    private String sourceFile_ = "";
    private String sample_ = "";
    private String disposition_ = DISPOSITION_ENFORCE;
    private boolean composed_;
    private int statusCode_;
    private int lineNumber_;
    private int columnNumber_;

    /**
     * Default constructor used to build the prototype.
     */
    public SecurityPolicyViolationEvent() {
        super();
    }

    /**
     * Creates a new securitypolicyviolation event (bubbling, not cancelable) for the given target.
     *
     * @param target the DOM node the event is fired at
     */
    public SecurityPolicyViolationEvent(final DomNode target) {
        super(target, TYPE_SECURITY_POLICY_VIOLATION);
        setBubbles(true);
        setCancelable(false);
        composed_ = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxConstructor
    public void jsConstructor(final String type, final ScriptableObject details) {
        super.jsConstructor(type, details);

        if (details != null && !JavaScriptEngine.isUndefined(details)) {
            composed_ = JavaScriptEngine.toBoolean(details.get("composed"));

            final Object documentURI = ScriptableObject.getProperty(details, "documentURI");
            if (isSet(documentURI)) {
                documentURI_ = JavaScriptEngine.toString(documentURI);
            }
            final Object referrer = ScriptableObject.getProperty(details, "referrer");
            if (isSet(referrer)) {
                referrer_ = JavaScriptEngine.toString(referrer);
            }
            final Object blockedURI = ScriptableObject.getProperty(details, "blockedURI");
            if (isSet(blockedURI)) {
                blockedURI_ = JavaScriptEngine.toString(blockedURI);
            }
            final Object effectiveDirective = ScriptableObject.getProperty(details, "effectiveDirective");
            if (isSet(effectiveDirective)) {
                effectiveDirective_ = JavaScriptEngine.toString(effectiveDirective);
            }
            final Object violatedDirective = ScriptableObject.getProperty(details, "violatedDirective");
            if (isSet(violatedDirective)) {
                violatedDirective_ = JavaScriptEngine.toString(violatedDirective);
            }
            final Object originalPolicy = ScriptableObject.getProperty(details, "originalPolicy");
            if (isSet(originalPolicy)) {
                originalPolicy_ = JavaScriptEngine.toString(originalPolicy);
            }
            final Object sourceFile = ScriptableObject.getProperty(details, "sourceFile");
            if (isSet(sourceFile)) {
                sourceFile_ = JavaScriptEngine.toString(sourceFile);
            }
            final Object sample = ScriptableObject.getProperty(details, "sample");
            if (isSet(sample)) {
                sample_ = JavaScriptEngine.toString(sample);
            }
            final Object disposition = ScriptableObject.getProperty(details, "disposition");
            if (isSet(disposition)) {
                final String value = JavaScriptEngine.toString(disposition);
                if (!DISPOSITION_ENFORCE.equals(value) && !DISPOSITION_REPORT.equals(value)) {
                    throw JavaScriptEngine.typeError("Failed to construct 'SecurityPolicyViolationEvent': "
                            + "The provided value '" + value
                            + "' is not a valid enum value of type SecurityPolicyViolationEventDisposition.");
                }
                disposition_ = value;
            }
            final Object statusCode = ScriptableObject.getProperty(details, "statusCode");
            if (isSet(statusCode)) {
                statusCode_ = JavaScriptEngine.toInt32(statusCode);
            }
            final Object lineNumber = ScriptableObject.getProperty(details, "lineNumber");
            if (isSet(lineNumber)) {
                lineNumber_ = JavaScriptEngine.toInt32(lineNumber);
            }
            final Object columnNumber = ScriptableObject.getProperty(details, "columnNumber");
            if (isSet(columnNumber)) {
                columnNumber_ = JavaScriptEngine.toInt32(columnNumber);
            }
        }
    }

    /**
     * In contrast to the other event init values, {@code null} is converted to "null";
     * only {@code undefined} (or a missing value) is ignored.
     */
    private static boolean isSet(final Object value) {
        return value != Scriptable.NOT_FOUND && !JavaScriptEngine.isUndefined(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isComposed() {
        return composed_;
    }

    /**
     * Returns the documentURI property.
     *
     * @return the documentURI
     */
    @JsxGetter
    public String getDocumentURI() {
        return documentURI_;
    }

    /**
     * Sets the documentURI property.
     *
     * @param documentURI the new value
     */
    public void setDocumentURI(final String documentURI) {
        documentURI_ = documentURI;
    }

    /**
     * Returns the referrer property.
     *
     * @return the referrer
     */
    @JsxGetter
    public String getReferrer() {
        return referrer_;
    }

    /**
     * Sets the referrer property.
     *
     * @param referrer the new value
     */
    public void setReferrer(final String referrer) {
        referrer_ = referrer;
    }

    /**
     * Returns the blockedURI property.
     *
     * @return the blockedURI
     */
    @JsxGetter
    public String getBlockedURI() {
        return blockedURI_;
    }

    /**
     * Sets the blockedURI property.
     *
     * @param blockedURI the new value
     */
    public void setBlockedURI(final String blockedURI) {
        blockedURI_ = blockedURI;
    }

    /**
     * Returns the effectiveDirective property.
     *
     * @return the effectiveDirective
     */
    @JsxGetter
    public String getEffectiveDirective() {
        return effectiveDirective_;
    }

    /**
     * Sets the effectiveDirective property.
     *
     * @param effectiveDirective the new value
     */
    public void setEffectiveDirective(final String effectiveDirective) {
        effectiveDirective_ = effectiveDirective;
    }

    /**
     * Returns the violatedDirective property.
     *
     * @return the violatedDirective
     */
    @JsxGetter
    public String getViolatedDirective() {
        return violatedDirective_;
    }

    /**
     * Sets the violatedDirective property.
     *
     * @param violatedDirective the new value
     */
    public void setViolatedDirective(final String violatedDirective) {
        violatedDirective_ = violatedDirective;
    }

    /**
     * Returns the originalPolicy property.
     *
     * @return the originalPolicy
     */
    @JsxGetter
    public String getOriginalPolicy() {
        return originalPolicy_;
    }

    /**
     * Sets the originalPolicy property.
     *
     * @param originalPolicy the new value
     */
    public void setOriginalPolicy(final String originalPolicy) {
        originalPolicy_ = originalPolicy;
    }

    /**
     * Returns the sourceFile property.
     *
     * @return the sourceFile
     */
    @JsxGetter
    public String getSourceFile() {
        return sourceFile_;
    }

    /**
     * Sets the sourceFile property.
     *
     * @param sourceFile the new value
     */
    public void setSourceFile(final String sourceFile) {
        sourceFile_ = sourceFile;
    }

    /**
     * Returns the sample property.
     *
     * @return the sample
     */
    @JsxGetter
    public String getSample() {
        return sample_;
    }

    /**
     * Sets the sample property.
     *
     * @param sample the new value
     */
    public void setSample(final String sample) {
        sample_ = sample;
    }

    /**
     * Returns the disposition property.
     *
     * @return the disposition
     */
    @JsxGetter
    public String getDisposition() {
        return disposition_;
    }

    /**
     * Sets the disposition property.
     *
     * @param disposition the new value
     */
    public void setDisposition(final String disposition) {
        disposition_ = disposition;
    }

    /**
     * Returns the statusCode property.
     *
     * @return the statusCode
     */
    @JsxGetter
    public int getStatusCode() {
        return statusCode_;
    }

    /**
     * Sets the statusCode property.
     *
     * @param statusCode the new value
     */
    public void setStatusCode(final int statusCode) {
        statusCode_ = statusCode;
    }

    /**
     * Returns the lineNumber property.
     *
     * @return the lineNumber
     */
    @JsxGetter
    public int getLineNumber() {
        return lineNumber_;
    }

    /**
     * Sets the lineNumber property.
     *
     * @param lineNumber the new value
     */
    public void setLineNumber(final int lineNumber) {
        lineNumber_ = lineNumber;
    }

    /**
     * Returns the columnNumber property.
     *
     * @return the columnNumber
     */
    @JsxGetter
    public int getColumnNumber() {
        return columnNumber_;
    }

    /**
     * Sets the columnNumber property.
     *
     * @param columnNumber the new value
     */
    public void setColumnNumber(final int columnNumber) {
        columnNumber_ = columnNumber;
    }
}
