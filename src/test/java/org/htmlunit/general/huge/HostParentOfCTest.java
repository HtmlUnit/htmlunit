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
package org.htmlunit.general.huge;

import java.util.Collection;

import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.params.provider.Arguments;

/**
 * Tests two Host classes, if one prototype is parent of another.
 *
 * This class handles all host names which starts by character 'C'.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HostParentOfCTest extends HostParentOf {

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    public static Collection<Arguments> data() throws Exception {
        return HostParentOf.data(input -> {
            final char ch = Character.toUpperCase(input.charAt(0));
            return ch == 'C';
        });
    }

    @Alerts("true/false")
    void _Cache_Cache() throws Exception {
        test("Cache", "Cache");
    }

    @Alerts("true/false")
    void _CacheStorage_CacheStorage() throws Exception {
        test("CacheStorage", "CacheStorage");
    }

    @Alerts(DEFAULT = "false/false",
            FF = "true/false",
            FF_ESR = "true/false")
    void _CanvasCaptureMediaStream_CanvasCaptureMediaStream() throws Exception {
        test("CanvasCaptureMediaStream", "CanvasCaptureMediaStream");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    void _CanvasCaptureMediaStreamTrack_CanvasCaptureMediaStreamTrack() throws Exception {
        test("CanvasCaptureMediaStreamTrack", "CanvasCaptureMediaStreamTrack");
    }

    @Alerts("true/false")
    void _CanvasGradient_CanvasGradient() throws Exception {
        test("CanvasGradient", "CanvasGradient");
    }

    @Alerts("true/false")
    void _CanvasPattern_CanvasPattern() throws Exception {
        test("CanvasPattern", "CanvasPattern");
    }

    @Alerts("true/false")
    void _CanvasRenderingContext2D_CanvasRenderingContext2D() throws Exception {
        test("CanvasRenderingContext2D", "CanvasRenderingContext2D");
    }

    @Alerts("true/false")
    void _CaretPosition_CaretPosition() throws Exception {
        test("CaretPosition", "CaretPosition");
    }

    @Alerts("true/false")
    void _CDATASection_CDATASection() throws Exception {
        test("CDATASection", "CDATASection");
    }

    @Alerts("true/false")
    void _ChannelMergerNode_ChannelMergerNode() throws Exception {
        test("ChannelMergerNode", "ChannelMergerNode");
    }

    @Alerts("true/false")
    void _ChannelSplitterNode_ChannelSplitterNode() throws Exception {
        test("ChannelSplitterNode", "ChannelSplitterNode");
    }

    @Alerts("true/false")
    void _CharacterData_CDATASection() throws Exception {
        test("CharacterData", "CDATASection");
    }

    @Alerts("true/false")
    void _CharacterData_CharacterData() throws Exception {
        test("CharacterData", "CharacterData");
    }

    @Alerts("true/true")
    void _CharacterData_Comment() throws Exception {
        test("CharacterData", "Comment");
    }

    @Alerts("true/true")
    void _CharacterData_ProcessingInstruction() throws Exception {
        test("CharacterData", "ProcessingInstruction");
    }

    @Alerts("true/true")
    void _CharacterData_Text() throws Exception {
        test("CharacterData", "Text");
    }

    @Alerts("true/false")
    void _ClipboardEvent_ClipboardEvent() throws Exception {
        test("ClipboardEvent", "ClipboardEvent");
    }

    @Alerts("true/false")
    void _CloseEvent_CloseEvent() throws Exception {
        test("CloseEvent", "CloseEvent");
    }

    @Alerts("true/false")
    void _Comment_Comment() throws Exception {
        test("Comment", "Comment");
    }

    @Alerts("true/false")
    void _CompositionEvent_CompositionEvent() throws Exception {
        test("CompositionEvent", "CompositionEvent");
    }

    @Alerts("false/false")
    void _Console_Console() throws Exception {
        test("Console", "Console");
    }

    @Alerts("true/false")
    void _ConstantSourceNode_ConstantSourceNode() throws Exception {
        test("ConstantSourceNode", "ConstantSourceNode");
    }

    @Alerts("true/false")
    void _ConvolverNode_ConvolverNode() throws Exception {
        test("ConvolverNode", "ConvolverNode");
    }

    @Alerts("false/false")
    void _Coordinates_Coordinates() throws Exception {
        test("Coordinates", "Coordinates");
    }

    @Alerts("true/false")
    void _Credential_Credential() throws Exception {
        test("Credential", "Credential");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/true",
            EDGE = "true/true")
    void _Credential_FederatedCredential() throws Exception {
        test("Credential", "FederatedCredential");
    }

    @Alerts(DEFAULT = "false/false",
            CHROME = "true/true",
            EDGE = "true/true")
    void _Credential_PasswordCredential() throws Exception {
        test("Credential", "PasswordCredential");
    }

    @Alerts("true/false")
    void _CredentialsContainer_CredentialsContainer() throws Exception {
        test("CredentialsContainer", "CredentialsContainer");
    }

    @Alerts("true/false")
    void _Crypto_Crypto() throws Exception {
        test("Crypto", "Crypto");
    }

    @Alerts("true/false")
    void _CryptoKey_CryptoKey() throws Exception {
        test("CryptoKey", "CryptoKey");
    }

    @Alerts("false/false")
    void _CSS_CSS() throws Exception {
        test("CSS", "CSS");
    }

    @Alerts(DEFAULT = "false/false",
            FF_ESR = "true/false")
    void _CSS2Properties_CSS2Properties() throws Exception {
        test("CSS2Properties", "CSS2Properties");
    }

    @Alerts(DEFAULT = "false/false",
            FF = "true/false")
    void _CSSStyleProperties_CSSStyleProperties() throws Exception {
        test("CSSStyleProperties", "CSSStyleProperties");
    }

    @Alerts("true/false")
    void _CSSConditionRule_CSSConditionRule() throws Exception {
        test("CSSConditionRule", "CSSConditionRule");
    }

    @Alerts("true/true")
    void _CSSConditionRule_CSSMediaRule() throws Exception {
        test("CSSConditionRule", "CSSMediaRule");
    }

    @Alerts("true/true")
    void _CSSConditionRule_CSSSupportsRule() throws Exception {
        test("CSSConditionRule", "CSSSupportsRule");
    }

    @Alerts("true/false")
    void _CSSCounterStyleRule_CSSCounterStyleRule() throws Exception {
        test("CSSCounterStyleRule", "CSSCounterStyleRule");
    }

    @Alerts("true/false")
    void _CSSFontFaceRule_CSSFontFaceRule() throws Exception {
        test("CSSFontFaceRule", "CSSFontFaceRule");
    }

    @Alerts("true/true")
    void _CSSGroupingRule_CSSConditionRule() throws Exception {
        test("CSSGroupingRule", "CSSConditionRule");
    }

    @Alerts("true/false")
    void _CSSGroupingRule_CSSGroupingRule() throws Exception {
        test("CSSGroupingRule", "CSSGroupingRule");
    }

    @Alerts("true/false")
    void _CSSGroupingRule_CSSMediaRule() throws Exception {
        test("CSSGroupingRule", "CSSMediaRule");
    }

    @Alerts("true/true")
    @HtmlUnitNYI(CHROME = "false/false",
            EDGE = "false/false",
            FF = "false/false",
            FF_ESR = "false/false")
    void _CSSGroupingRule_CSSPageRule() throws Exception {
        test("CSSGroupingRule", "CSSPageRule");
    }

    @Alerts(DEFAULT = "false/false",
            FF = "true/true",
            FF_ESR = "true/true")
    @HtmlUnitNYI(FF = "false/false",
            FF_ESR = "false/false")
    void _CSSGroupingRule_CSSStyleRule() throws Exception {
        test("CSSGroupingRule", "CSSStyleRule");
    }

    @Alerts("true/false")
    void _CSSGroupingRule_CSSSupportsRule() throws Exception {
        test("CSSGroupingRule", "CSSSupportsRule");
    }

    @Alerts("true/false")
    void _CSSImportRule_CSSImportRule() throws Exception {
        test("CSSImportRule", "CSSImportRule");
    }

    @Alerts("true/false")
    void _CSSKeyframeRule_CSSKeyframeRule() throws Exception {
        test("CSSKeyframeRule", "CSSKeyframeRule");
    }

    @Alerts("true/false")
    void _CSSKeyframesRule_CSSKeyframesRule() throws Exception {
        test("CSSKeyframesRule", "CSSKeyframesRule");
    }

    @Alerts("true/false")
    void _CSSMediaRule_CSSMediaRule() throws Exception {
        test("CSSMediaRule", "CSSMediaRule");
    }

    @Alerts("true/false")
    void _CSSNamespaceRule_CSSNamespaceRule() throws Exception {
        test("CSSNamespaceRule", "CSSNamespaceRule");
    }

    @Alerts("true/false")
    void _CSSPageRule_CSSPageRule() throws Exception {
        test("CSSPageRule", "CSSPageRule");
    }

    @Alerts("false/false")
    void _CSSPrimitiveValue_CSSPrimitiveValue() throws Exception {
        test("CSSPrimitiveValue", "CSSPrimitiveValue");
    }

    @Alerts("true/false")
    void _CSSRule_CSSConditionRule() throws Exception {
        test("CSSRule", "CSSConditionRule");
    }

    @Alerts("true/true")
    void _CSSRule_CSSCounterStyleRule() throws Exception {
        test("CSSRule", "CSSCounterStyleRule");
    }

    @Alerts("true/true")
    void _CSSRule_CSSFontFaceRule() throws Exception {
        test("CSSRule", "CSSFontFaceRule");
    }

    @Alerts("true/true")
    void _CSSRule_CSSGroupingRule() throws Exception {
        test("CSSRule", "CSSGroupingRule");
    }

    @Alerts("true/true")
    void _CSSRule_CSSImportRule() throws Exception {
        test("CSSRule", "CSSImportRule");
    }

    @Alerts("true/true")
    void _CSSRule_CSSKeyframeRule() throws Exception {
        test("CSSRule", "CSSKeyframeRule");
    }

    @Alerts("true/true")
    void _CSSRule_CSSKeyframesRule() throws Exception {
        test("CSSRule", "CSSKeyframesRule");
    }

    @Alerts("true/false")
    void _CSSRule_CSSMediaRule() throws Exception {
        test("CSSRule", "CSSMediaRule");
    }

    @Alerts("true/true")
    void _CSSRule_CSSNamespaceRule() throws Exception {
        test("CSSRule", "CSSNamespaceRule");
    }

    @Alerts("true/false")
    @HtmlUnitNYI(CHROME = "true/true",
            EDGE = "true/true",
            FF = "true/true",
            FF_ESR = "true/true")
    void _CSSRule_CSSPageRule() throws Exception {
        test("CSSRule", "CSSPageRule");
    }

    @Alerts("true/false")
    void _CSSRule_CSSRule() throws Exception {
        test("CSSRule", "CSSRule");
    }

    @Alerts(DEFAULT = "true/true",
            FF = "true/false",
            FF_ESR = "true/false")
    @HtmlUnitNYI(FF = "true/true",
            FF_ESR = "true/true")
    void _CSSRule_CSSStyleRule() throws Exception {
        test("CSSRule", "CSSStyleRule");
    }

    @Alerts("true/false")
    void _CSSRule_CSSSupportsRule() throws Exception {
        test("CSSRule", "CSSSupportsRule");
    }

    @Alerts("true/false")
    void _CSSRuleList_CSSRuleList() throws Exception {
        test("CSSRuleList", "CSSRuleList");
    }

    @Alerts(DEFAULT = "false/false",
            FF_ESR = "true/true")
    void _CSSStyleDeclaration_CSS2Properties() throws Exception {
        test("CSSStyleDeclaration", "CSS2Properties");
    }

    @Alerts(DEFAULT = "false/false",
            FF = "true/true")
    void _CSSStyleDeclaration_CSSStyleProperties() throws Exception {
        test("CSSStyleDeclaration", "CSSStyleProperties");
    }

    @Alerts("true/false")
    void _CSSStyleDeclaration_CSSStyleDeclaration() throws Exception {
        test("CSSStyleDeclaration", "CSSStyleDeclaration");
    }

    @Alerts("true/false")
    void _CSSStyleRule_CSSStyleRule() throws Exception {
        test("CSSStyleRule", "CSSStyleRule");
    }

    @Alerts("true/false")
    void _CSSStyleSheet_CSSStyleSheet() throws Exception {
        test("CSSStyleSheet", "CSSStyleSheet");
    }

    @Alerts("true/false")
    void _CSSSupportsRule_CSSSupportsRule() throws Exception {
        test("CSSSupportsRule", "CSSSupportsRule");
    }

    @Alerts("false/false")
    void _CSSValue_CSSPrimitiveValue() throws Exception {
        test("CSSValue", "CSSPrimitiveValue");
    }

    @Alerts("false/false")
    void _CSSValue_CSSValue() throws Exception {
        test("CSSValue", "CSSValue");
    }

    @Alerts("false/false")
    void _CSSValue_CSSValueList() throws Exception {
        test("CSSValue", "CSSValueList");
    }

    @Alerts("false/false")
    void _CSSValueList_CSSValueList() throws Exception {
        test("CSSValueList", "CSSValueList");
    }

    @Alerts("true/false")
    void _CustomElementRegistry_CustomElementRegistry() throws Exception {
        test("CustomElementRegistry", "CustomElementRegistry");
    }

    @Alerts("true/false")
    void _CustomEvent_CustomEvent() throws Exception {
        test("CustomEvent", "CustomEvent");
    }
}
