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

import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.params.provider.Arguments;

/**
 * Tests two Host classes, if one prototype is parent of another.
 *
 * This class handles all host names which starts by character 'H'.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HostParentOfHTest extends HostParentOf {

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    public static Collection<Arguments> data() throws Exception {
        return HostParentOf.data(input -> {
            final char ch = Character.toUpperCase(input.charAt(0));
            return ch == 'H';
        });
    }

    @Alerts("true/false")
    void _HashChangeEvent_HashChangeEvent() throws Exception {
        test("HashChangeEvent", "HashChangeEvent");
    }

    @Alerts("true/false")
    void _Headers_Headers() throws Exception {
        test("Headers", "Headers");
    }

    @Alerts("true/false")
    void _History_History() throws Exception {
        test("History", "History");
    }

    @Alerts("true/false")
    void _HTMLAllCollection_HTMLAllCollection() throws Exception {
        test("HTMLAllCollection", "HTMLAllCollection");
    }

    @Alerts("true/false")
    void _HTMLAnchorElement_HTMLAnchorElement() throws Exception {
        test("HTMLAnchorElement", "HTMLAnchorElement");
    }

    @Alerts("false/false")
    void _HTMLAppletElement_HTMLAppletElement() throws Exception {
        test("HTMLAppletElement", "HTMLAppletElement");
    }

    @Alerts("true/false")
    void _HTMLAreaElement_HTMLAreaElement() throws Exception {
        test("HTMLAreaElement", "HTMLAreaElement");
    }

    @Alerts("true/false")
    @HtmlUnitNYI(CHROME = "true/true",
            EDGE = "true/true",
            FF = "true/true",
            FF_ESR = "true/true")
    void _HTMLAudioElement_Audio() throws Exception {
        test("HTMLAudioElement", "Audio");
    }

    @Alerts("true/false")
    void _HTMLAudioElement_HTMLAudioElement() throws Exception {
        test("HTMLAudioElement", "HTMLAudioElement");
    }

    @Alerts("true/false")
    void _HTMLBaseElement_HTMLBaseElement() throws Exception {
        test("HTMLBaseElement", "HTMLBaseElement");
    }

    @Alerts("false/false")
    void _HTMLBaseFontElement_HTMLBaseFontElement() throws Exception {
        test("HTMLBaseFontElement", "HTMLBaseFontElement");
    }

    @Alerts("false/false")
    void _HTMLBGSoundElement_HTMLBGSoundElement() throws Exception {
        test("HTMLBGSoundElement", "HTMLBGSoundElement");
    }

    @Alerts("false/false")
    void _HTMLBlockElement_HTMLBlockElement() throws Exception {
        test("HTMLBlockElement", "HTMLBlockElement");
    }

    @Alerts("true/false")
    void _HTMLBodyElement_HTMLBodyElement() throws Exception {
        test("HTMLBodyElement", "HTMLBodyElement");
    }

    @Alerts("true/false")
    void _HTMLBRElement_HTMLBRElement() throws Exception {
        test("HTMLBRElement", "HTMLBRElement");
    }

    @Alerts("true/false")
    void _HTMLButtonElement_HTMLButtonElement() throws Exception {
        test("HTMLButtonElement", "HTMLButtonElement");
    }

    @Alerts("true/false")
    void _HTMLCanvasElement_HTMLCanvasElement() throws Exception {
        test("HTMLCanvasElement", "HTMLCanvasElement");
    }

    @Alerts("false/false")
    void _HTMLCollection_HTMLAllCollection() throws Exception {
        test("HTMLCollection", "HTMLAllCollection");
    }

    @Alerts("true/false")
    void _HTMLCollection_HTMLCollection() throws Exception {
        test("HTMLCollection", "HTMLCollection");
    }

    @Alerts("true/true")
    void _HTMLCollection_HTMLFormControlsCollection() throws Exception {
        test("HTMLCollection", "HTMLFormControlsCollection");
    }

    @Alerts("true/true")
    @HtmlUnitNYI(CHROME = "false/false",
            EDGE = "false/false",
            FF = "false/false",
            FF_ESR = "false/false")
    void _HTMLCollection_HTMLOptionsCollection() throws Exception {
        test("HTMLCollection", "HTMLOptionsCollection");
    }

    @Alerts("true/false")
    void _HTMLDataElement_HTMLDataElement() throws Exception {
        test("HTMLDataElement", "HTMLDataElement");
    }

    @Alerts("true/false")
    void _HTMLDataListElement_HTMLDataListElement() throws Exception {
        test("HTMLDataListElement", "HTMLDataListElement");
    }

    @Alerts("false/false")
    void _HTMLDDElement_HTMLDDElement() throws Exception {
        test("HTMLDDElement", "HTMLDDElement");
    }

    @Alerts("true/false")
    void _HTMLDetailsElement_HTMLDetailsElement() throws Exception {
        test("HTMLDetailsElement", "HTMLDetailsElement");
    }

    @Alerts("true/false")
    void _HTMLDialogElement_HTMLDialogElement() throws Exception {
        test("HTMLDialogElement", "HTMLDialogElement");
    }

    @Alerts("true/false")
    void _HTMLDirectoryElement_HTMLDirectoryElement() throws Exception {
        test("HTMLDirectoryElement", "HTMLDirectoryElement");
    }

    @Alerts("true/false")
    void _HTMLDivElement_HTMLDivElement() throws Exception {
        test("HTMLDivElement", "HTMLDivElement");
    }

    @Alerts("true/false")
    void _HTMLDListElement_HTMLDListElement() throws Exception {
        test("HTMLDListElement", "HTMLDListElement");
    }

    @Alerts("true/false")
    void _HTMLDocument_HTMLDocument() throws Exception {
        test("HTMLDocument", "HTMLDocument");
    }

    @Alerts("false/false")
    void _HTMLDTElement_HTMLDTElement() throws Exception {
        test("HTMLDTElement", "HTMLDTElement");
    }

    @Alerts("true/false")
    void _HTMLElement_Audio() throws Exception {
        test("HTMLElement", "Audio");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLAnchorElement() throws Exception {
        test("HTMLElement", "HTMLAnchorElement");
    }

    @Alerts("false/false")
    void _HTMLElement_HTMLAppletElement() throws Exception {
        test("HTMLElement", "HTMLAppletElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLAreaElement() throws Exception {
        test("HTMLElement", "HTMLAreaElement");
    }

    @Alerts("true/false")
    void _HTMLElement_HTMLAudioElement() throws Exception {
        test("HTMLElement", "HTMLAudioElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLBaseElement() throws Exception {
        test("HTMLElement", "HTMLBaseElement");
    }

    @Alerts("false/false")
    void _HTMLElement_HTMLBaseFontElement() throws Exception {
        test("HTMLElement", "HTMLBaseFontElement");
    }

    @Alerts("false/false")
    void _HTMLElement_HTMLBGSoundElement() throws Exception {
        test("HTMLElement", "HTMLBGSoundElement");
    }

    @Alerts("false/false")
    void _HTMLElement_HTMLBlockElement() throws Exception {
        test("HTMLElement", "HTMLBlockElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLBodyElement() throws Exception {
        test("HTMLElement", "HTMLBodyElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLBRElement() throws Exception {
        test("HTMLElement", "HTMLBRElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLButtonElement() throws Exception {
        test("HTMLElement", "HTMLButtonElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLCanvasElement() throws Exception {
        test("HTMLElement", "HTMLCanvasElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLDataElement() throws Exception {
        test("HTMLElement", "HTMLDataElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLDataListElement() throws Exception {
        test("HTMLElement", "HTMLDataListElement");
    }

    @Alerts("false/false")
    void _HTMLElement_HTMLDDElement() throws Exception {
        test("HTMLElement", "HTMLDDElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLDetailsElement() throws Exception {
        test("HTMLElement", "HTMLDetailsElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLDialogElement() throws Exception {
        test("HTMLElement", "HTMLDialogElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLDirectoryElement() throws Exception {
        test("HTMLElement", "HTMLDirectoryElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLDivElement() throws Exception {
        test("HTMLElement", "HTMLDivElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLDListElement() throws Exception {
        test("HTMLElement", "HTMLDListElement");
    }

    @Alerts("false/false")
    void _HTMLElement_HTMLDTElement() throws Exception {
        test("HTMLElement", "HTMLDTElement");
    }

    @Alerts("true/false")
    void _HTMLElement_HTMLElement() throws Exception {
        test("HTMLElement", "HTMLElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLEmbedElement() throws Exception {
        test("HTMLElement", "HTMLEmbedElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLFieldSetElement() throws Exception {
        test("HTMLElement", "HTMLFieldSetElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLFontElement() throws Exception {
        test("HTMLElement", "HTMLFontElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLFormElement() throws Exception {
        test("HTMLElement", "HTMLFormElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLFrameElement() throws Exception {
        test("HTMLElement", "HTMLFrameElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLFrameSetElement() throws Exception {
        test("HTMLElement", "HTMLFrameSetElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLHeadElement() throws Exception {
        test("HTMLElement", "HTMLHeadElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLHeadingElement() throws Exception {
        test("HTMLElement", "HTMLHeadingElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLHRElement() throws Exception {
        test("HTMLElement", "HTMLHRElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLHtmlElement() throws Exception {
        test("HTMLElement", "HTMLHtmlElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLIFrameElement() throws Exception {
        test("HTMLElement", "HTMLIFrameElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLImageElement() throws Exception {
        test("HTMLElement", "HTMLImageElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLInputElement() throws Exception {
        test("HTMLElement", "HTMLInputElement");
    }

    @Alerts("false/false")
    void _HTMLElement_HTMLIsIndexElement() throws Exception {
        test("HTMLElement", "HTMLIsIndexElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLLabelElement() throws Exception {
        test("HTMLElement", "HTMLLabelElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLLegendElement() throws Exception {
        test("HTMLElement", "HTMLLegendElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLLIElement() throws Exception {
        test("HTMLElement", "HTMLLIElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLLinkElement() throws Exception {
        test("HTMLElement", "HTMLLinkElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLMapElement() throws Exception {
        test("HTMLElement", "HTMLMapElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLMarqueeElement() throws Exception {
        test("HTMLElement", "HTMLMarqueeElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLMediaElement() throws Exception {
        test("HTMLElement", "HTMLMediaElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLMenuElement() throws Exception {
        test("HTMLElement", "HTMLMenuElement");
    }

    @Alerts("false/false")
    void _HTMLElement_HTMLMenuItemElement() throws Exception {
        test("HTMLElement", "HTMLMenuItemElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLMetaElement() throws Exception {
        test("HTMLElement", "HTMLMetaElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLMeterElement() throws Exception {
        test("HTMLElement", "HTMLMeterElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLModElement() throws Exception {
        test("HTMLElement", "HTMLModElement");
    }

    @Alerts("false/false")
    void _HTMLElement_HTMLNextIdElement() throws Exception {
        test("HTMLElement", "HTMLNextIdElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLObjectElement() throws Exception {
        test("HTMLElement", "HTMLObjectElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLOListElement() throws Exception {
        test("HTMLElement", "HTMLOListElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLOptGroupElement() throws Exception {
        test("HTMLElement", "HTMLOptGroupElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLOptionElement() throws Exception {
        test("HTMLElement", "HTMLOptionElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLOutputElement() throws Exception {
        test("HTMLElement", "HTMLOutputElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLParagraphElement() throws Exception {
        test("HTMLElement", "HTMLParagraphElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLParamElement() throws Exception {
        test("HTMLElement", "HTMLParamElement");
    }

    @Alerts("false/false")
    void _HTMLElement_HTMLPhraseElement() throws Exception {
        test("HTMLElement", "HTMLPhraseElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLPictureElement() throws Exception {
        test("HTMLElement", "HTMLPictureElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLPreElement() throws Exception {
        test("HTMLElement", "HTMLPreElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLProgressElement() throws Exception {
        test("HTMLElement", "HTMLProgressElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLQuoteElement() throws Exception {
        test("HTMLElement", "HTMLQuoteElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLScriptElement() throws Exception {
        test("HTMLElement", "HTMLScriptElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLSelectElement() throws Exception {
        test("HTMLElement", "HTMLSelectElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLSlotElement() throws Exception {
        test("HTMLElement", "HTMLSlotElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLSourceElement() throws Exception {
        test("HTMLElement", "HTMLSourceElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLSpanElement() throws Exception {
        test("HTMLElement", "HTMLSpanElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLStyleElement() throws Exception {
        test("HTMLElement", "HTMLStyleElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLTableCaptionElement() throws Exception {
        test("HTMLElement", "HTMLTableCaptionElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLTableCellElement() throws Exception {
        test("HTMLElement", "HTMLTableCellElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLTableColElement() throws Exception {
        test("HTMLElement", "HTMLTableColElement");
    }

    @Alerts("false/false")
    void _HTMLElement_HTMLTableDataCellElement() throws Exception {
        test("HTMLElement", "HTMLTableDataCellElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLTableElement() throws Exception {
        test("HTMLElement", "HTMLTableElement");
    }

    @Alerts("false/false")
    void _HTMLElement_HTMLTableHeaderCellElement() throws Exception {
        test("HTMLElement", "HTMLTableHeaderCellElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLTableRowElement() throws Exception {
        test("HTMLElement", "HTMLTableRowElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLTableSectionElement() throws Exception {
        test("HTMLElement", "HTMLTableSectionElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLTemplateElement() throws Exception {
        test("HTMLElement", "HTMLTemplateElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLTextAreaElement() throws Exception {
        test("HTMLElement", "HTMLTextAreaElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLTimeElement() throws Exception {
        test("HTMLElement", "HTMLTimeElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLTitleElement() throws Exception {
        test("HTMLElement", "HTMLTitleElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLTrackElement() throws Exception {
        test("HTMLElement", "HTMLTrackElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLUListElement() throws Exception {
        test("HTMLElement", "HTMLUListElement");
    }

    @Alerts("true/true")
    void _HTMLElement_HTMLUnknownElement() throws Exception {
        test("HTMLElement", "HTMLUnknownElement");
    }

    @Alerts("true/false")
    void _HTMLElement_HTMLVideoElement() throws Exception {
        test("HTMLElement", "HTMLVideoElement");
    }

    @Alerts("true/false")
    void _HTMLElement_Image() throws Exception {
        test("HTMLElement", "Image");
    }

    @Alerts("true/false")
    void _HTMLElement_Option() throws Exception {
        test("HTMLElement", "Option");
    }

    @Alerts("true/false")
    void _HTMLEmbedElement_HTMLEmbedElement() throws Exception {
        test("HTMLEmbedElement", "HTMLEmbedElement");
    }

    @Alerts("true/false")
    void _HTMLFieldSetElement_HTMLFieldSetElement() throws Exception {
        test("HTMLFieldSetElement", "HTMLFieldSetElement");
    }

    @Alerts("true/false")
    void _HTMLFontElement_HTMLFontElement() throws Exception {
        test("HTMLFontElement", "HTMLFontElement");
    }

    @Alerts("true/false")
    void _HTMLFormControlsCollection_HTMLFormControlsCollection() throws Exception {
        test("HTMLFormControlsCollection", "HTMLFormControlsCollection");
    }

    @Alerts("true/false")
    void _HTMLFormElement_HTMLFormElement() throws Exception {
        test("HTMLFormElement", "HTMLFormElement");
    }

    @Alerts("true/false")
    void _HTMLFrameElement_HTMLFrameElement() throws Exception {
        test("HTMLFrameElement", "HTMLFrameElement");
    }

    @Alerts("true/false")
    void _HTMLFrameSetElement_HTMLFrameSetElement() throws Exception {
        test("HTMLFrameSetElement", "HTMLFrameSetElement");
    }

    @Alerts("true/false")
    void _HTMLHeadElement_HTMLHeadElement() throws Exception {
        test("HTMLHeadElement", "HTMLHeadElement");
    }

    @Alerts("true/false")
    void _HTMLHeadingElement_HTMLHeadingElement() throws Exception {
        test("HTMLHeadingElement", "HTMLHeadingElement");
    }

    @Alerts("true/false")
    void _HTMLHRElement_HTMLHRElement() throws Exception {
        test("HTMLHRElement", "HTMLHRElement");
    }

    @Alerts("true/false")
    void _HTMLHtmlElement_HTMLHtmlElement() throws Exception {
        test("HTMLHtmlElement", "HTMLHtmlElement");
    }

    @Alerts("true/false")
    void _HTMLIFrameElement_HTMLIFrameElement() throws Exception {
        test("HTMLIFrameElement", "HTMLIFrameElement");
    }

    @Alerts("true/false")
    void _HTMLImageElement_HTMLImageElement() throws Exception {
        test("HTMLImageElement", "HTMLImageElement");
    }

    @Alerts("true/false")
    void _HTMLImageElement_Image() throws Exception {
        //although Image != HTMLImageElement, they seem to be synonyms!!!
        test("HTMLImageElement", "Image");
    }

    @Alerts("true/false")
    void _HTMLInputElement_HTMLInputElement() throws Exception {
        test("HTMLInputElement", "HTMLInputElement");
    }

    @Alerts("false/false")
    void _HTMLIsIndexElement_HTMLIsIndexElement() throws Exception {
        test("HTMLIsIndexElement", "HTMLIsIndexElement");
    }

    @Alerts("true/false")
    void _HTMLLabelElement_HTMLLabelElement() throws Exception {
        test("HTMLLabelElement", "HTMLLabelElement");
    }

    @Alerts("true/false")
    void _HTMLLegendElement_HTMLLegendElement() throws Exception {
        test("HTMLLegendElement", "HTMLLegendElement");
    }

    @Alerts("true/false")
    void _HTMLLIElement_HTMLLIElement() throws Exception {
        test("HTMLLIElement", "HTMLLIElement");
    }

    @Alerts("true/false")
    void _HTMLLinkElement_HTMLLinkElement() throws Exception {
        test("HTMLLinkElement", "HTMLLinkElement");
    }

    @Alerts("true/false")
    void _HTMLMapElement_HTMLMapElement() throws Exception {
        test("HTMLMapElement", "HTMLMapElement");
    }

    @Alerts("true/false")
    void _HTMLMarqueeElement_HTMLMarqueeElement() throws Exception {
        test("HTMLMarqueeElement", "HTMLMarqueeElement");
    }

    @Alerts("true/false")
    void _HTMLMediaElement_Audio() throws Exception {
        test("HTMLMediaElement", "Audio");
    }

    @Alerts("true/true")
    void _HTMLMediaElement_HTMLAudioElement() throws Exception {
        test("HTMLMediaElement", "HTMLAudioElement");
    }

    @Alerts("true/false")
    void _HTMLMediaElement_HTMLMediaElement() throws Exception {
        test("HTMLMediaElement", "HTMLMediaElement");
    }

    @Alerts("true/true")
    void _HTMLMediaElement_HTMLVideoElement() throws Exception {
        test("HTMLMediaElement", "HTMLVideoElement");
    }

    @Alerts("true/false")
    void _HTMLMenuElement_HTMLMenuElement() throws Exception {
        test("HTMLMenuElement", "HTMLMenuElement");
    }

    @Alerts("false/false")
    void _HTMLMenuItemElement_HTMLMenuItemElement() throws Exception {
        test("HTMLMenuItemElement", "HTMLMenuItemElement");
    }

    @Alerts("true/false")
    void _HTMLMetaElement_HTMLMetaElement() throws Exception {
        test("HTMLMetaElement", "HTMLMetaElement");
    }

    @Alerts("true/false")
    void _HTMLMeterElement_HTMLMeterElement() throws Exception {
        test("HTMLMeterElement", "HTMLMeterElement");
    }

    @Alerts("true/false")
    void _HTMLModElement_HTMLModElement() throws Exception {
        test("HTMLModElement", "HTMLModElement");
    }

    @Alerts("false/false")
    void _HTMLNextIdElement_HTMLNextIdElement() throws Exception {
        test("HTMLNextIdElement", "HTMLNextIdElement");
    }

    @Alerts("true/false")
    void _HTMLObjectElement_HTMLObjectElement() throws Exception {
        test("HTMLObjectElement", "HTMLObjectElement");
    }

    @Alerts("true/false")
    void _HTMLOListElement_HTMLOListElement() throws Exception {
        test("HTMLOListElement", "HTMLOListElement");
    }

    @Alerts("true/false")
    void _HTMLOptGroupElement_HTMLOptGroupElement() throws Exception {
        test("HTMLOptGroupElement", "HTMLOptGroupElement");
    }

    @Alerts("true/false")
    void _HTMLOptionElement_HTMLOptionElement() throws Exception {
        test("HTMLOptionElement", "HTMLOptionElement");
    }

    @Alerts("true/false")
    void _HTMLOptionElement_Option() throws Exception {
        //although Option != HTMLOptionElement, they seem to be synonyms!!!
        test("HTMLOptionElement", "Option");
    }

    @Alerts("true/false")
    void _HTMLOptionsCollection_HTMLOptionsCollection() throws Exception {
        test("HTMLOptionsCollection", "HTMLOptionsCollection");
    }

    @Alerts("true/false")
    void _HTMLOutputElement_HTMLOutputElement() throws Exception {
        test("HTMLOutputElement", "HTMLOutputElement");
    }

    @Alerts("true/false")
    void _HTMLParagraphElement_HTMLParagraphElement() throws Exception {
        test("HTMLParagraphElement", "HTMLParagraphElement");
    }

    @Alerts("true/false")
    void _HTMLParamElement_HTMLParamElement() throws Exception {
        test("HTMLParamElement", "HTMLParamElement");
    }

    @Alerts("false/false")
    void _HTMLPhraseElement_HTMLPhraseElement() throws Exception {
        test("HTMLPhraseElement", "HTMLPhraseElement");
    }

    @Alerts("true/false")
    void _HTMLPictureElement_HTMLPictureElement() throws Exception {
        test("HTMLPictureElement", "HTMLPictureElement");
    }

    @Alerts("true/false")
    void _HTMLPreElement_HTMLPreElement() throws Exception {
        test("HTMLPreElement", "HTMLPreElement");
    }

    @Alerts("true/false")
    void _HTMLProgressElement_HTMLProgressElement() throws Exception {
        test("HTMLProgressElement", "HTMLProgressElement");
    }

    @Alerts("true/false")
    void _HTMLQuoteElement_HTMLQuoteElement() throws Exception {
        test("HTMLQuoteElement", "HTMLQuoteElement");
    }

    @Alerts("true/false")
    void _HTMLScriptElement_HTMLScriptElement() throws Exception {
        test("HTMLScriptElement", "HTMLScriptElement");
    }

    @Alerts("true/false")
    void _HTMLSelectElement_HTMLSelectElement() throws Exception {
        test("HTMLSelectElement", "HTMLSelectElement");
    }

    @Alerts("true/false")
    void _HTMLSlotElement_HTMLSlotElement() throws Exception {
        test("HTMLSlotElement", "HTMLSlotElement");
    }

    @Alerts("true/false")
    void _HTMLSourceElement_HTMLSourceElement() throws Exception {
        test("HTMLSourceElement", "HTMLSourceElement");
    }

    @Alerts("true/false")
    void _HTMLSpanElement_HTMLSpanElement() throws Exception {
        test("HTMLSpanElement", "HTMLSpanElement");
    }

    @Alerts("true/false")
    void _HTMLStyleElement_HTMLStyleElement() throws Exception {
        test("HTMLStyleElement", "HTMLStyleElement");
    }

    @Alerts("true/false")
    void _HTMLTableCaptionElement_HTMLTableCaptionElement() throws Exception {
        test("HTMLTableCaptionElement", "HTMLTableCaptionElement");
    }

    @Alerts("true/false")
    void _HTMLTableCellElement_HTMLTableCellElement() throws Exception {
        test("HTMLTableCellElement", "HTMLTableCellElement");
    }

    @Alerts("false/false")
    void _HTMLTableCellElement_HTMLTableDataCellElement() throws Exception {
        test("HTMLTableCellElement", "HTMLTableDataCellElement");
    }

    @Alerts("false/false")
    void _HTMLTableCellElement_HTMLTableHeaderCellElement() throws Exception {
        test("HTMLTableCellElement", "HTMLTableHeaderCellElement");
    }

    @Alerts("true/false")
    void _HTMLTableColElement_HTMLTableColElement() throws Exception {
        test("HTMLTableColElement", "HTMLTableColElement");
    }

    @Alerts("false/false")
    void _HTMLTableDataCellElement_HTMLTableDataCellElement() throws Exception {
        test("HTMLTableDataCellElement", "HTMLTableDataCellElement");
    }

    @Alerts("true/false")
    void _HTMLTableElement_HTMLTableElement() throws Exception {
        test("HTMLTableElement", "HTMLTableElement");
    }

    @Alerts("false/false")
    void _HTMLTableHeaderCellElement_HTMLTableHeaderCellElement() throws Exception {
        test("HTMLTableHeaderCellElement", "HTMLTableHeaderCellElement");
    }

    @Alerts("true/false")
    void _HTMLTableRowElement_HTMLTableRowElement() throws Exception {
        test("HTMLTableRowElement", "HTMLTableRowElement");
    }

    @Alerts("true/false")
    void _HTMLTableSectionElement_HTMLTableSectionElement() throws Exception {
        test("HTMLTableSectionElement", "HTMLTableSectionElement");
    }

    @Alerts("true/false")
    void _HTMLTemplateElement_HTMLTemplateElement() throws Exception {
        test("HTMLTemplateElement", "HTMLTemplateElement");
    }

    @Alerts("true/false")
    void _HTMLTextAreaElement_HTMLTextAreaElement() throws Exception {
        test("HTMLTextAreaElement", "HTMLTextAreaElement");
    }

    @Alerts("true/false")
    void _HTMLTimeElement_HTMLTimeElement() throws Exception {
        test("HTMLTimeElement", "HTMLTimeElement");
    }

    @Alerts("true/false")
    void _HTMLTitleElement_HTMLTitleElement() throws Exception {
        test("HTMLTitleElement", "HTMLTitleElement");
    }

    @Alerts("true/false")
    void _HTMLTrackElement_HTMLTrackElement() throws Exception {
        test("HTMLTrackElement", "HTMLTrackElement");
    }

    @Alerts("true/false")
    void _HTMLUListElement_HTMLUListElement() throws Exception {
        test("HTMLUListElement", "HTMLUListElement");
    }

    @Alerts("true/false")
    void _HTMLUnknownElement_HTMLUnknownElement() throws Exception {
        test("HTMLUnknownElement", "HTMLUnknownElement");
    }

    @Alerts("true/false")
    void _HTMLVideoElement_HTMLVideoElement() throws Exception {
        test("HTMLVideoElement", "HTMLVideoElement");
    }
}
