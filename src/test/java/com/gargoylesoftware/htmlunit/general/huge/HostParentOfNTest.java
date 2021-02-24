/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.general.huge;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import com.gargoylesoftware.htmlunit.BrowserParameterizedRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests two Host classes, if one prototype is parent of another.
 *
 * This class handles all host names which starts by character 'M' to 'O'.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserParameterizedRunner.class)
public class HostParentOfNTest extends HostParentOf {

    /**
     * Returns the parameterized data.
     *
     * @return the parameterized data
     * @throws Exception
     *             if an error occurs
     */
    @Parameters
    public static Collection<Object[]> data() throws Exception {
        return HostParentOf.data(input -> {
            final char ch = Character.toUpperCase(input.charAt(0));
            return ch >= 'N' && ch <= 'O';
        });
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _NamedNodeMap_NamedNodeMap() throws Exception {
        test("NamedNodeMap", "NamedNodeMap");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Navigator_Navigator() throws Exception {
        test("Navigator", "Navigator");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            EDGE = "true")
    public void _NetworkInformation_NetworkInformation() throws Exception {
        test("NetworkInformation", "NetworkInformation");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_Attr() throws Exception {
        test("Node", "Attr");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_Audio() throws Exception {
        test("Node", "Audio");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_CDATASection() throws Exception {
        test("Node", "CDATASection");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_CharacterData() throws Exception {
        test("Node", "CharacterData");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_Comment() throws Exception {
        test("Node", "Comment");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_Document() throws Exception {
        test("Node", "Document");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_DocumentFragment() throws Exception {
        test("Node", "DocumentFragment");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_DocumentType() throws Exception {
        test("Node", "DocumentType");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_Element() throws Exception {
        test("Node", "Element");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLAnchorElement() throws Exception {
        test("Node", "HTMLAnchorElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Node_HTMLAppletElement() throws Exception {
        test("Node", "HTMLAppletElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLAreaElement() throws Exception {
        test("Node", "HTMLAreaElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLAudioElement() throws Exception {
        test("Node", "HTMLAudioElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLBaseElement() throws Exception {
        test("Node", "HTMLBaseElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Node_HTMLBaseFontElement() throws Exception {
        test("Node", "HTMLBaseFontElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Node_HTMLBGSoundElement() throws Exception {
        test("Node", "HTMLBGSoundElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Node_HTMLBlockElement() throws Exception {
        test("Node", "HTMLBlockElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLBodyElement() throws Exception {
        test("Node", "HTMLBodyElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLBRElement() throws Exception {
        test("Node", "HTMLBRElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLButtonElement() throws Exception {
        test("Node", "HTMLButtonElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLCanvasElement() throws Exception {
        test("Node", "HTMLCanvasElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            EDGE = "true")
    public void _Node_HTMLContentElement() throws Exception {
        test("Node", "HTMLContentElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_HTMLDataElement() throws Exception {
        test("Node", "HTMLDataElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLDataListElement() throws Exception {
        test("Node", "HTMLDataListElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Node_HTMLDDElement() throws Exception {
        test("Node", "HTMLDDElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_HTMLDetailsElement() throws Exception {
        test("Node", "HTMLDetailsElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            EDGE = "true")
    public void _Node_HTMLDialogElement() throws Exception {
        test("Node", "HTMLDialogElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLDirectoryElement() throws Exception {
        test("Node", "HTMLDirectoryElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLDivElement() throws Exception {
        test("Node", "HTMLDivElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLDListElement() throws Exception {
        test("Node", "HTMLDListElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLDocument() throws Exception {
        test("Node", "HTMLDocument");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Node_HTMLDTElement() throws Exception {
        test("Node", "HTMLDTElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLElement() throws Exception {
        test("Node", "HTMLElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLEmbedElement() throws Exception {
        test("Node", "HTMLEmbedElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLFieldSetElement() throws Exception {
        test("Node", "HTMLFieldSetElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLFontElement() throws Exception {
        test("Node", "HTMLFontElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLFormElement() throws Exception {
        test("Node", "HTMLFormElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLFrameElement() throws Exception {
        test("Node", "HTMLFrameElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLFrameSetElement() throws Exception {
        test("Node", "HTMLFrameSetElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLHeadElement() throws Exception {
        test("Node", "HTMLHeadElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLHeadingElement() throws Exception {
        test("Node", "HTMLHeadingElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLHRElement() throws Exception {
        test("Node", "HTMLHRElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLHtmlElement() throws Exception {
        test("Node", "HTMLHtmlElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLIFrameElement() throws Exception {
        test("Node", "HTMLIFrameElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLImageElement() throws Exception {
        test("Node", "HTMLImageElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLInputElement() throws Exception {
        test("Node", "HTMLInputElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Node_HTMLIsIndexElement() throws Exception {
        test("Node", "HTMLIsIndexElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLLabelElement() throws Exception {
        test("Node", "HTMLLabelElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLLegendElement() throws Exception {
        test("Node", "HTMLLegendElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLLIElement() throws Exception {
        test("Node", "HTMLLIElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLLinkElement() throws Exception {
        test("Node", "HTMLLinkElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLMapElement() throws Exception {
        test("Node", "HTMLMapElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLMarqueeElement() throws Exception {
        test("Node", "HTMLMarqueeElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLMediaElement() throws Exception {
        test("Node", "HTMLMediaElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLMenuElement() throws Exception {
        test("Node", "HTMLMenuElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF78 = "true")
    public void _Node_HTMLMenuItemElement() throws Exception {
        test("Node", "HTMLMenuItemElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLMetaElement() throws Exception {
        test("Node", "HTMLMetaElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_HTMLMeterElement() throws Exception {
        test("Node", "HTMLMeterElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLModElement() throws Exception {
        test("Node", "HTMLModElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Node_HTMLNextIdElement() throws Exception {
        test("Node", "HTMLNextIdElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLObjectElement() throws Exception {
        test("Node", "HTMLObjectElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLOListElement() throws Exception {
        test("Node", "HTMLOListElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLOptGroupElement() throws Exception {
        test("Node", "HTMLOptGroupElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLOptionElement() throws Exception {
        test("Node", "HTMLOptionElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_HTMLOutputElement() throws Exception {
        test("Node", "HTMLOutputElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLParagraphElement() throws Exception {
        test("Node", "HTMLParagraphElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLParamElement() throws Exception {
        test("Node", "HTMLParamElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Node_HTMLPhraseElement() throws Exception {
        test("Node", "HTMLPhraseElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_HTMLPictureElement() throws Exception {
        test("Node", "HTMLPictureElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLPreElement() throws Exception {
        test("Node", "HTMLPreElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLProgressElement() throws Exception {
        test("Node", "HTMLProgressElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLQuoteElement() throws Exception {
        test("Node", "HTMLQuoteElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLScriptElement() throws Exception {
        test("Node", "HTMLScriptElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLSelectElement() throws Exception {
        test("Node", "HTMLSelectElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            EDGE = "true")
    public void _Node_HTMLShadowElement() throws Exception {
        test("Node", "HTMLShadowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
           IE = "false")
    public void _Node_HTMLSlotElement() throws Exception {
        test("Node", "HTMLSlotElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLSourceElement() throws Exception {
        test("Node", "HTMLSourceElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLSpanElement() throws Exception {
        test("Node", "HTMLSpanElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLStyleElement() throws Exception {
        test("Node", "HTMLStyleElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLTableCaptionElement() throws Exception {
        test("Node", "HTMLTableCaptionElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLTableCellElement() throws Exception {
        test("Node", "HTMLTableCellElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLTableColElement() throws Exception {
        test("Node", "HTMLTableColElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Node_HTMLTableDataCellElement() throws Exception {
        test("Node", "HTMLTableDataCellElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLTableElement() throws Exception {
        test("Node", "HTMLTableElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _Node_HTMLTableHeaderCellElement() throws Exception {
        test("Node", "HTMLTableHeaderCellElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLTableRowElement() throws Exception {
        test("Node", "HTMLTableRowElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLTableSectionElement() throws Exception {
        test("Node", "HTMLTableSectionElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_HTMLTemplateElement() throws Exception {
        test("Node", "HTMLTemplateElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLTextAreaElement() throws Exception {
        test("Node", "HTMLTextAreaElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_HTMLTimeElement() throws Exception {
        test("Node", "HTMLTimeElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLTitleElement() throws Exception {
        test("Node", "HTMLTitleElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLTrackElement() throws Exception {
        test("Node", "HTMLTrackElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLUListElement() throws Exception {
        test("Node", "HTMLUListElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLUnknownElement() throws Exception {
        test("Node", "HTMLUnknownElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_HTMLVideoElement() throws Exception {
        test("Node", "HTMLVideoElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_Image() throws Exception {
        test("Node", "Image");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_Node() throws Exception {
        test("Node", "Node");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_Option() throws Exception {
        test("Node", "Option");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_ProcessingInstruction() throws Exception {
        test("Node", "ProcessingInstruction");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_ShadowRoot() throws Exception {
        test("Node", "ShadowRoot");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGAElement() throws Exception {
        test("Node", "SVGAElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_SVGAnimateElement() throws Exception {
        test("Node", "SVGAnimateElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_SVGAnimateMotionElement() throws Exception {
        test("Node", "SVGAnimateMotionElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_SVGAnimateTransformElement() throws Exception {
        test("Node", "SVGAnimateTransformElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_SVGAnimationElement() throws Exception {
        test("Node", "SVGAnimationElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGCircleElement() throws Exception {
        test("Node", "SVGCircleElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGClipPathElement() throws Exception {
        test("Node", "SVGClipPathElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGComponentTransferFunctionElement() throws Exception {
        test("Node", "SVGComponentTransferFunctionElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGDefsElement() throws Exception {
        test("Node", "SVGDefsElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGDescElement() throws Exception {
        test("Node", "SVGDescElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGElement() throws Exception {
        test("Node", "SVGElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGEllipseElement() throws Exception {
        test("Node", "SVGEllipseElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFEBlendElement() throws Exception {
        test("Node", "SVGFEBlendElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFEColorMatrixElement() throws Exception {
        test("Node", "SVGFEColorMatrixElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFEComponentTransferElement() throws Exception {
        test("Node", "SVGFEComponentTransferElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFECompositeElement() throws Exception {
        test("Node", "SVGFECompositeElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFEConvolveMatrixElement() throws Exception {
        test("Node", "SVGFEConvolveMatrixElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFEDiffuseLightingElement() throws Exception {
        test("Node", "SVGFEDiffuseLightingElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFEDisplacementMapElement() throws Exception {
        test("Node", "SVGFEDisplacementMapElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFEDistantLightElement() throws Exception {
        test("Node", "SVGFEDistantLightElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_SVGFEDropShadowElement() throws Exception {
        test("Node", "SVGFEDropShadowElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFEFloodElement() throws Exception {
        test("Node", "SVGFEFloodElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFEFuncAElement() throws Exception {
        test("Node", "SVGFEFuncAElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFEFuncBElement() throws Exception {
        test("Node", "SVGFEFuncBElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFEFuncGElement() throws Exception {
        test("Node", "SVGFEFuncGElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFEFuncRElement() throws Exception {
        test("Node", "SVGFEFuncRElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFEGaussianBlurElement() throws Exception {
        test("Node", "SVGFEGaussianBlurElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFEImageElement() throws Exception {
        test("Node", "SVGFEImageElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFEMergeElement() throws Exception {
        test("Node", "SVGFEMergeElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFEMergeNodeElement() throws Exception {
        test("Node", "SVGFEMergeNodeElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFEMorphologyElement() throws Exception {
        test("Node", "SVGFEMorphologyElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFEOffsetElement() throws Exception {
        test("Node", "SVGFEOffsetElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFEPointLightElement() throws Exception {
        test("Node", "SVGFEPointLightElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFESpecularLightingElement() throws Exception {
        test("Node", "SVGFESpecularLightingElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFESpotLightElement() throws Exception {
        test("Node", "SVGFESpotLightElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFETileElement() throws Exception {
        test("Node", "SVGFETileElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFETurbulenceElement() throws Exception {
        test("Node", "SVGFETurbulenceElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGFilterElement() throws Exception {
        test("Node", "SVGFilterElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_SVGForeignObjectElement() throws Exception {
        test("Node", "SVGForeignObjectElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGGElement() throws Exception {
        test("Node", "SVGGElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_SVGGeometryElement() throws Exception {
        test("Node", "SVGGeometryElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGGradientElement() throws Exception {
        test("Node", "SVGGradientElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_SVGGraphicsElement() throws Exception {
        test("Node", "SVGGraphicsElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGImageElement() throws Exception {
        test("Node", "SVGImageElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGLinearGradientElement() throws Exception {
        test("Node", "SVGLinearGradientElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGLineElement() throws Exception {
        test("Node", "SVGLineElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGMarkerElement() throws Exception {
        test("Node", "SVGMarkerElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGMaskElement() throws Exception {
        test("Node", "SVGMaskElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGMetadataElement() throws Exception {
        test("Node", "SVGMetadataElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_SVGMPathElement() throws Exception {
        test("Node", "SVGMPathElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGPathElement() throws Exception {
        test("Node", "SVGPathElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGPatternElement() throws Exception {
        test("Node", "SVGPatternElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGPolygonElement() throws Exception {
        test("Node", "SVGPolygonElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGPolylineElement() throws Exception {
        test("Node", "SVGPolylineElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGRadialGradientElement() throws Exception {
        test("Node", "SVGRadialGradientElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGRectElement() throws Exception {
        test("Node", "SVGRectElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGScriptElement() throws Exception {
        test("Node", "SVGScriptElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Node_SVGSetElement() throws Exception {
        test("Node", "SVGSetElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGStopElement() throws Exception {
        test("Node", "SVGStopElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGStyleElement() throws Exception {
        test("Node", "SVGStyleElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGSVGElement() throws Exception {
        test("Node", "SVGSVGElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGSwitchElement() throws Exception {
        test("Node", "SVGSwitchElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGSymbolElement() throws Exception {
        test("Node", "SVGSymbolElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGTextContentElement() throws Exception {
        test("Node", "SVGTextContentElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGTextElement() throws Exception {
        test("Node", "SVGTextElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGTextPathElement() throws Exception {
        test("Node", "SVGTextPathElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGTextPositioningElement() throws Exception {
        test("Node", "SVGTextPositioningElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGTitleElement() throws Exception {
        test("Node", "SVGTitleElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGTSpanElement() throws Exception {
        test("Node", "SVGTSpanElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGUseElement() throws Exception {
        test("Node", "SVGUseElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_SVGViewElement() throws Exception {
        test("Node", "SVGViewElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_Text() throws Exception {
        test("Node", "Text");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Node_XMLDocument() throws Exception {
        test("Node", "XMLDocument");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("false")
    @NotYetImplemented
    public void _NodeFilter_NodeFilter() throws Exception {
        test("NodeFilter", "NodeFilter");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _NodeIterator_NodeIterator() throws Exception {
        test("NodeIterator", "NodeIterator");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _NodeList_NodeList() throws Exception {
        test("NodeList", "NodeList");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _NodeList_RadioNodeList() throws Exception {
        test("NodeList", "RadioNodeList");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Notification_Notification() throws Exception {
        test("Notification", "Notification");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _OES_element_index_uint_OES_element_index_uint() throws Exception {
        test("OES_element_index_uint", "OES_element_index_uint");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _OES_standard_derivatives_OES_standard_derivatives() throws Exception {
        test("OES_standard_derivatives", "OES_standard_derivatives");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _OES_texture_float_linear_OES_texture_float_linear() throws Exception {
        test("OES_texture_float_linear", "OES_texture_float_linear");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _OES_texture_float_OES_texture_float() throws Exception {
        test("OES_texture_float", "OES_texture_float");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _OfflineAudioCompletionEvent_OfflineAudioCompletionEvent() throws Exception {
        test("OfflineAudioCompletionEvent", "OfflineAudioCompletionEvent");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _OfflineAudioContext_OfflineAudioContext() throws Exception {
        test("OfflineAudioContext", "OfflineAudioContext");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            CHROME = "false",
            EDGE = "false",
            IE = "false")
    public void _OfflineResourceList_OfflineResourceList() throws Exception {
        test("OfflineResourceList", "OfflineResourceList");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Option_HTMLOptionElement() throws Exception {
        // although Option != HTMLOptionElement, they seem to be synonyms!!!
        test("Option", "HTMLOptionElement");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts("true")
    public void _Option_Option() throws Exception {
        test("Option", "Option");
    }

    /**
     * @throws Exception
     *             if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _OscillatorNode_OscillatorNode() throws Exception {
        test("OscillatorNode", "OscillatorNode");
    }

}
