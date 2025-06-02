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
package org.htmlunit.general.huge;

import java.util.Collection;

import org.htmlunit.junit.BrowserParameterizedRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests two Host classes, if one prototype is parent of another.
 *
 * This class handles all host names which starts by character 'C'.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserParameterizedRunner.class)
public class HostParentOfCTest extends HostParentOf {

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    @Parameters
    public static Collection<Object[]> data() throws Exception {
        return HostParentOf.data(input -> {
            final char ch = Character.toUpperCase(input.charAt(0));
            return ch == 'C';
        });
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _Cache_Cache() throws Exception {
        test("Cache", "Cache");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CacheStorage_CacheStorage() throws Exception {
        test("CacheStorage", "CacheStorage");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false/false",
            FF = "true/false",
            FF_ESR = "true/false")
    public void _CanvasCaptureMediaStream_CanvasCaptureMediaStream() throws Exception {
        test("CanvasCaptureMediaStream", "CanvasCaptureMediaStream");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false/false",
            CHROME = "true/false",
            EDGE = "true/false")
    public void _CanvasCaptureMediaStreamTrack_CanvasCaptureMediaStreamTrack() throws Exception {
        test("CanvasCaptureMediaStreamTrack", "CanvasCaptureMediaStreamTrack");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CanvasGradient_CanvasGradient() throws Exception {
        test("CanvasGradient", "CanvasGradient");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CanvasPattern_CanvasPattern() throws Exception {
        test("CanvasPattern", "CanvasPattern");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CanvasRenderingContext2D_CanvasRenderingContext2D() throws Exception {
        test("CanvasRenderingContext2D", "CanvasRenderingContext2D");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CaretPosition_CaretPosition() throws Exception {
        test("CaretPosition", "CaretPosition");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CDATASection_CDATASection() throws Exception {
        test("CDATASection", "CDATASection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _ChannelMergerNode_ChannelMergerNode() throws Exception {
        test("ChannelMergerNode", "ChannelMergerNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _ChannelSplitterNode_ChannelSplitterNode() throws Exception {
        test("ChannelSplitterNode", "ChannelSplitterNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CharacterData_CDATASection() throws Exception {
        test("CharacterData", "CDATASection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CharacterData_CharacterData() throws Exception {
        test("CharacterData", "CharacterData");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _CharacterData_Comment() throws Exception {
        test("CharacterData", "Comment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _CharacterData_ProcessingInstruction() throws Exception {
        test("CharacterData", "ProcessingInstruction");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _CharacterData_Text() throws Exception {
        test("CharacterData", "Text");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _ClipboardEvent_ClipboardEvent() throws Exception {
        test("ClipboardEvent", "ClipboardEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CloseEvent_CloseEvent() throws Exception {
        test("CloseEvent", "CloseEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _Comment_Comment() throws Exception {
        test("Comment", "Comment");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CompositionEvent_CompositionEvent() throws Exception {
        test("CompositionEvent", "CompositionEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false/false")
    public void _Console_Console() throws Exception {
        test("Console", "Console");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _ConstantSourceNode_ConstantSourceNode() throws Exception {
        test("ConstantSourceNode", "ConstantSourceNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _ConvolverNode_ConvolverNode() throws Exception {
        test("ConvolverNode", "ConvolverNode");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false/false")
    public void _Coordinates_Coordinates() throws Exception {
        test("Coordinates", "Coordinates");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _Credential_Credential() throws Exception {
        test("Credential", "Credential");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false/false",
            CHROME = "true/true",
            EDGE = "true/true")
    public void _Credential_FederatedCredential() throws Exception {
        test("Credential", "FederatedCredential");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false/false",
            CHROME = "true/true",
            EDGE = "true/true")
    public void _Credential_PasswordCredential() throws Exception {
        test("Credential", "PasswordCredential");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CredentialsContainer_CredentialsContainer() throws Exception {
        test("CredentialsContainer", "CredentialsContainer");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _Crypto_Crypto() throws Exception {
        test("Crypto", "Crypto");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CryptoKey_CryptoKey() throws Exception {
        test("CryptoKey", "CryptoKey");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false/false")
    public void _CSS_CSS() throws Exception {
        test("CSS", "CSS");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false/false",
            FF = "true/false",
            FF_ESR = "true/false")
    public void _CSS2Properties_CSS2Properties() throws Exception {
        test("CSS2Properties", "CSS2Properties");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CSSConditionRule_CSSConditionRule() throws Exception {
        test("CSSConditionRule", "CSSConditionRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _CSSConditionRule_CSSMediaRule() throws Exception {
        test("CSSConditionRule", "CSSMediaRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _CSSConditionRule_CSSSupportsRule() throws Exception {
        test("CSSConditionRule", "CSSSupportsRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CSSCounterStyleRule_CSSCounterStyleRule() throws Exception {
        test("CSSCounterStyleRule", "CSSCounterStyleRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CSSFontFaceRule_CSSFontFaceRule() throws Exception {
        test("CSSFontFaceRule", "CSSFontFaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _CSSGroupingRule_CSSConditionRule() throws Exception {
        test("CSSGroupingRule", "CSSConditionRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CSSGroupingRule_CSSGroupingRule() throws Exception {
        test("CSSGroupingRule", "CSSGroupingRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CSSGroupingRule_CSSMediaRule() throws Exception {
        test("CSSGroupingRule", "CSSMediaRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    @HtmlUnitNYI(CHROME = "false/false",
            EDGE = "false/false",
            FF = "false/false",
            FF_ESR = "false/false")
    public void _CSSGroupingRule_CSSPageRule() throws Exception {
        test("CSSGroupingRule", "CSSPageRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false/false",
            FF = "true/true",
            FF_ESR = "true/true")
    @HtmlUnitNYI(FF = "false/false",
            FF_ESR = "false/false")
    public void _CSSGroupingRule_CSSStyleRule() throws Exception {
        test("CSSGroupingRule", "CSSStyleRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CSSGroupingRule_CSSSupportsRule() throws Exception {
        test("CSSGroupingRule", "CSSSupportsRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CSSImportRule_CSSImportRule() throws Exception {
        test("CSSImportRule", "CSSImportRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CSSKeyframeRule_CSSKeyframeRule() throws Exception {
        test("CSSKeyframeRule", "CSSKeyframeRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CSSKeyframesRule_CSSKeyframesRule() throws Exception {
        test("CSSKeyframesRule", "CSSKeyframesRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CSSMediaRule_CSSMediaRule() throws Exception {
        test("CSSMediaRule", "CSSMediaRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CSSNamespaceRule_CSSNamespaceRule() throws Exception {
        test("CSSNamespaceRule", "CSSNamespaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CSSPageRule_CSSPageRule() throws Exception {
        test("CSSPageRule", "CSSPageRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false/false")
    public void _CSSPrimitiveValue_CSSPrimitiveValue() throws Exception {
        test("CSSPrimitiveValue", "CSSPrimitiveValue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CSSRule_CSSConditionRule() throws Exception {
        test("CSSRule", "CSSConditionRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _CSSRule_CSSCounterStyleRule() throws Exception {
        test("CSSRule", "CSSCounterStyleRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _CSSRule_CSSFontFaceRule() throws Exception {
        test("CSSRule", "CSSFontFaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _CSSRule_CSSGroupingRule() throws Exception {
        test("CSSRule", "CSSGroupingRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _CSSRule_CSSImportRule() throws Exception {
        test("CSSRule", "CSSImportRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _CSSRule_CSSKeyframeRule() throws Exception {
        test("CSSRule", "CSSKeyframeRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _CSSRule_CSSKeyframesRule() throws Exception {
        test("CSSRule", "CSSKeyframesRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CSSRule_CSSMediaRule() throws Exception {
        test("CSSRule", "CSSMediaRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/true")
    public void _CSSRule_CSSNamespaceRule() throws Exception {
        test("CSSRule", "CSSNamespaceRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    @HtmlUnitNYI(CHROME = "true/true",
            EDGE = "true/true",
            FF = "true/true",
            FF_ESR = "true/true")
    public void _CSSRule_CSSPageRule() throws Exception {
        test("CSSRule", "CSSPageRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CSSRule_CSSRule() throws Exception {
        test("CSSRule", "CSSRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true/true",
            FF = "true/false",
            FF_ESR = "true/false")
    @HtmlUnitNYI(FF = "true/true",
            FF_ESR = "true/true")
    public void _CSSRule_CSSStyleRule() throws Exception {
        test("CSSRule", "CSSStyleRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CSSRule_CSSSupportsRule() throws Exception {
        test("CSSRule", "CSSSupportsRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CSSRuleList_CSSRuleList() throws Exception {
        test("CSSRuleList", "CSSRuleList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false/false",
            FF = "true/true",
            FF_ESR = "true/true")
    public void _CSSStyleDeclaration_CSS2Properties() throws Exception {
        test("CSSStyleDeclaration", "CSS2Properties");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CSSStyleDeclaration_CSSStyleDeclaration() throws Exception {
        test("CSSStyleDeclaration", "CSSStyleDeclaration");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CSSStyleRule_CSSStyleRule() throws Exception {
        test("CSSStyleRule", "CSSStyleRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CSSStyleSheet_CSSStyleSheet() throws Exception {
        test("CSSStyleSheet", "CSSStyleSheet");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CSSSupportsRule_CSSSupportsRule() throws Exception {
        test("CSSSupportsRule", "CSSSupportsRule");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false/false")
    public void _CSSValue_CSSPrimitiveValue() throws Exception {
        test("CSSValue", "CSSPrimitiveValue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false/false")
    public void _CSSValue_CSSValue() throws Exception {
        test("CSSValue", "CSSValue");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false/false")
    public void _CSSValue_CSSValueList() throws Exception {
        test("CSSValue", "CSSValueList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false/false")
    public void _CSSValueList_CSSValueList() throws Exception {
        test("CSSValueList", "CSSValueList");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CustomElementRegistry_CustomElementRegistry() throws Exception {
        test("CustomElementRegistry", "CustomElementRegistry");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true/false")
    public void _CustomEvent_CustomEvent() throws Exception {
        test("CustomEvent", "CustomEvent");
    }
}
