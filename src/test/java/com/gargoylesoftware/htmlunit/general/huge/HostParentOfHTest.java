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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF78;

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
 * This class handles all host names which starts by character 'H'.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserParameterizedRunner.class)
public class HostParentOfHTest extends HostParentOf {

    /**
     * Returns the parameterized data.
     * @return the parameterized data
     * @throws Exception if an error occurs
     */
    @Parameters
    public static Collection<Object[]> data() throws Exception {
        return HostParentOf.data(input -> {
            final char ch = Character.toUpperCase(input.charAt(0));
            return ch == 'H';
        });
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HashChangeEvent_HashChangeEvent() throws Exception {
        test("HashChangeEvent", "HashChangeEvent");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _Headers_Headers() throws Exception {
        test("Headers", "Headers");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _History_History() throws Exception {
        test("History", "History");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLAllCollection_HTMLAllCollection() throws Exception {
        test("HTMLAllCollection", "HTMLAllCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLAnchorElement_HTMLAnchorElement() throws Exception {
        test("HTMLAnchorElement", "HTMLAnchorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLAppletElement_HTMLAppletElement() throws Exception {
        test("HTMLAppletElement", "HTMLAppletElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLAreaElement_HTMLAreaElement() throws Exception {
        test("HTMLAreaElement", "HTMLAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLAudioElement_Audio() throws Exception {
        test("HTMLAudioElement", "Audio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLAudioElement_HTMLAudioElement() throws Exception {
        test("HTMLAudioElement", "HTMLAudioElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLBaseElement_HTMLBaseElement() throws Exception {
        test("HTMLBaseElement", "HTMLBaseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLBaseFontElement_HTMLBaseFontElement() throws Exception {
        test("HTMLBaseFontElement", "HTMLBaseFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLBGSoundElement_HTMLBGSoundElement() throws Exception {
        test("HTMLBGSoundElement", "HTMLBGSoundElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLBlockElement_HTMLBlockElement() throws Exception {
        test("HTMLBlockElement", "HTMLBlockElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLBodyElement_HTMLBodyElement() throws Exception {
        test("HTMLBodyElement", "HTMLBodyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLBRElement_HTMLBRElement() throws Exception {
        test("HTMLBRElement", "HTMLBRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLButtonElement_HTMLButtonElement() throws Exception {
        test("HTMLButtonElement", "HTMLButtonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLCanvasElement_HTMLCanvasElement() throws Exception {
        test("HTMLCanvasElement", "HTMLCanvasElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    @NotYetImplemented({CHROME, EDGE, FF, FF78})
    public void _HTMLCollection_HTMLAllCollection() throws Exception {
        test("HTMLCollection", "HTMLAllCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLCollection_HTMLCollection() throws Exception {
        test("HTMLCollection", "HTMLCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLCollection_HTMLFormControlsCollection() throws Exception {
        test("HTMLCollection", "HTMLFormControlsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    @NotYetImplemented({CHROME, EDGE, FF, FF78})
    public void _HTMLCollection_HTMLOptionsCollection() throws Exception {
        test("HTMLCollection", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            EDGE = "true")
    public void _HTMLContentElement_HTMLContentElement() throws Exception {
        test("HTMLContentElement", "HTMLContentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLDataElement_HTMLDataElement() throws Exception {
        test("HTMLDataElement", "HTMLDataElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLDataListElement_HTMLDataListElement() throws Exception {
        test("HTMLDataListElement", "HTMLDataListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLDDElement_HTMLDDElement() throws Exception {
        test("HTMLDDElement", "HTMLDDElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLDetailsElement_HTMLDetailsElement() throws Exception {
        test("HTMLDetailsElement", "HTMLDetailsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            EDGE = "true")
    public void _HTMLDialogElement_HTMLDialogElement() throws Exception {
        test("HTMLDialogElement", "HTMLDialogElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLDirectoryElement_HTMLDirectoryElement() throws Exception {
        test("HTMLDirectoryElement", "HTMLDirectoryElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLDivElement_HTMLDivElement() throws Exception {
        test("HTMLDivElement", "HTMLDivElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLDListElement_HTMLDListElement() throws Exception {
        test("HTMLDListElement", "HTMLDListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLDocument_HTMLDocument() throws Exception {
        test("HTMLDocument", "HTMLDocument");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLDTElement_HTMLDTElement() throws Exception {
        test("HTMLDTElement", "HTMLDTElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_Audio() throws Exception {
        test("HTMLElement", "Audio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLAnchorElement() throws Exception {
        test("HTMLElement", "HTMLAnchorElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLElement_HTMLAppletElement() throws Exception {
        test("HTMLElement", "HTMLAppletElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLAreaElement() throws Exception {
        test("HTMLElement", "HTMLAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLAudioElement() throws Exception {
        test("HTMLElement", "HTMLAudioElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLBaseElement() throws Exception {
        test("HTMLElement", "HTMLBaseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLElement_HTMLBaseFontElement() throws Exception {
        test("HTMLElement", "HTMLBaseFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLElement_HTMLBGSoundElement() throws Exception {
        test("HTMLElement", "HTMLBGSoundElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLElement_HTMLBlockElement() throws Exception {
        test("HTMLElement", "HTMLBlockElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLBodyElement() throws Exception {
        test("HTMLElement", "HTMLBodyElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLBRElement() throws Exception {
        test("HTMLElement", "HTMLBRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLButtonElement() throws Exception {
        test("HTMLElement", "HTMLButtonElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLCanvasElement() throws Exception {
        test("HTMLElement", "HTMLCanvasElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            EDGE = "true")
    public void _HTMLElement_HTMLContentElement() throws Exception {
        test("HTMLElement", "HTMLContentElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLElement_HTMLDataElement() throws Exception {
        test("HTMLElement", "HTMLDataElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLDataListElement() throws Exception {
        test("HTMLElement", "HTMLDataListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLElement_HTMLDDElement() throws Exception {
        test("HTMLElement", "HTMLDDElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLElement_HTMLDetailsElement() throws Exception {
        test("HTMLElement", "HTMLDetailsElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            EDGE = "true")
    public void _HTMLElement_HTMLDialogElement() throws Exception {
        test("HTMLElement", "HTMLDialogElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLDirectoryElement() throws Exception {
        test("HTMLElement", "HTMLDirectoryElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLDivElement() throws Exception {
        test("HTMLElement", "HTMLDivElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLDListElement() throws Exception {
        test("HTMLElement", "HTMLDListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLElement_HTMLDTElement() throws Exception {
        test("HTMLElement", "HTMLDTElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLElement() throws Exception {
        test("HTMLElement", "HTMLElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLEmbedElement() throws Exception {
        test("HTMLElement", "HTMLEmbedElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLFieldSetElement() throws Exception {
        test("HTMLElement", "HTMLFieldSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLFontElement() throws Exception {
        test("HTMLElement", "HTMLFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLFormElement() throws Exception {
        test("HTMLElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLFrameElement() throws Exception {
        test("HTMLElement", "HTMLFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLFrameSetElement() throws Exception {
        test("HTMLElement", "HTMLFrameSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLHeadElement() throws Exception {
        test("HTMLElement", "HTMLHeadElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLHeadingElement() throws Exception {
        test("HTMLElement", "HTMLHeadingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLHRElement() throws Exception {
        test("HTMLElement", "HTMLHRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLHtmlElement() throws Exception {
        test("HTMLElement", "HTMLHtmlElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLIFrameElement() throws Exception {
        test("HTMLElement", "HTMLIFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLImageElement() throws Exception {
        test("HTMLElement", "HTMLImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLInputElement() throws Exception {
        test("HTMLElement", "HTMLInputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLElement_HTMLIsIndexElement() throws Exception {
        test("HTMLElement", "HTMLIsIndexElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLLabelElement() throws Exception {
        test("HTMLElement", "HTMLLabelElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLLegendElement() throws Exception {
        test("HTMLElement", "HTMLLegendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLLIElement() throws Exception {
        test("HTMLElement", "HTMLLIElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLLinkElement() throws Exception {
        test("HTMLElement", "HTMLLinkElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLMapElement() throws Exception {
        test("HTMLElement", "HTMLMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLMarqueeElement() throws Exception {
        test("HTMLElement", "HTMLMarqueeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLMediaElement() throws Exception {
        test("HTMLElement", "HTMLMediaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLMenuElement() throws Exception {
        test("HTMLElement", "HTMLMenuElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF78 = "true")
    public void _HTMLElement_HTMLMenuItemElement() throws Exception {
        test("HTMLElement", "HTMLMenuItemElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLMetaElement() throws Exception {
        test("HTMLElement", "HTMLMetaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLElement_HTMLMeterElement() throws Exception {
        test("HTMLElement", "HTMLMeterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLModElement() throws Exception {
        test("HTMLElement", "HTMLModElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLElement_HTMLNextIdElement() throws Exception {
        test("HTMLElement", "HTMLNextIdElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLObjectElement() throws Exception {
        test("HTMLElement", "HTMLObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLOListElement() throws Exception {
        test("HTMLElement", "HTMLOListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLOptGroupElement() throws Exception {
        test("HTMLElement", "HTMLOptGroupElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLOptionElement() throws Exception {
        test("HTMLElement", "HTMLOptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLElement_HTMLOutputElement() throws Exception {
        test("HTMLElement", "HTMLOutputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLParagraphElement() throws Exception {
        test("HTMLElement", "HTMLParagraphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLParamElement() throws Exception {
        test("HTMLElement", "HTMLParamElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLElement_HTMLPhraseElement() throws Exception {
        test("HTMLElement", "HTMLPhraseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLElement_HTMLPictureElement() throws Exception {
        test("HTMLElement", "HTMLPictureElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLPreElement() throws Exception {
        test("HTMLElement", "HTMLPreElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLProgressElement() throws Exception {
        test("HTMLElement", "HTMLProgressElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLQuoteElement() throws Exception {
        test("HTMLElement", "HTMLQuoteElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLScriptElement() throws Exception {
        test("HTMLElement", "HTMLScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLSelectElement() throws Exception {
        test("HTMLElement", "HTMLSelectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            EDGE = "true")
    public void _HTMLElement_HTMLShadowElement() throws Exception {
        test("HTMLElement", "HTMLShadowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLElement_HTMLSlotElement() throws Exception {
        test("HTMLElement", "HTMLSlotElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLSourceElement() throws Exception {
        test("HTMLElement", "HTMLSourceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLSpanElement() throws Exception {
        test("HTMLElement", "HTMLSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLStyleElement() throws Exception {
        test("HTMLElement", "HTMLStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLTableCaptionElement() throws Exception {
        test("HTMLElement", "HTMLTableCaptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLTableCellElement() throws Exception {
        test("HTMLElement", "HTMLTableCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLTableColElement() throws Exception {
        test("HTMLElement", "HTMLTableColElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLElement_HTMLTableDataCellElement() throws Exception {
        test("HTMLElement", "HTMLTableDataCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLTableElement() throws Exception {
        test("HTMLElement", "HTMLTableElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLElement_HTMLTableHeaderCellElement() throws Exception {
        test("HTMLElement", "HTMLTableHeaderCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLTableRowElement() throws Exception {
        test("HTMLElement", "HTMLTableRowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLTableSectionElement() throws Exception {
        test("HTMLElement", "HTMLTableSectionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLElement_HTMLTemplateElement() throws Exception {
        test("HTMLElement", "HTMLTemplateElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLTextAreaElement() throws Exception {
        test("HTMLElement", "HTMLTextAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLElement_HTMLTimeElement() throws Exception {
        test("HTMLElement", "HTMLTimeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLTitleElement() throws Exception {
        test("HTMLElement", "HTMLTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLTrackElement() throws Exception {
        test("HTMLElement", "HTMLTrackElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLUListElement() throws Exception {
        test("HTMLElement", "HTMLUListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLUnknownElement() throws Exception {
        test("HTMLElement", "HTMLUnknownElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_HTMLVideoElement() throws Exception {
        test("HTMLElement", "HTMLVideoElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_Image() throws Exception {
        test("HTMLElement", "Image");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLElement_Option() throws Exception {
        test("HTMLElement", "Option");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLEmbedElement_HTMLEmbedElement() throws Exception {
        test("HTMLEmbedElement", "HTMLEmbedElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLFieldSetElement_HTMLFieldSetElement() throws Exception {
        test("HTMLFieldSetElement", "HTMLFieldSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLFontElement_HTMLFontElement() throws Exception {
        test("HTMLFontElement", "HTMLFontElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLFormControlsCollection_HTMLFormControlsCollection() throws Exception {
        test("HTMLFormControlsCollection", "HTMLFormControlsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLFormElement_HTMLFormElement() throws Exception {
        test("HTMLFormElement", "HTMLFormElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLFrameElement_HTMLFrameElement() throws Exception {
        test("HTMLFrameElement", "HTMLFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLFrameSetElement_HTMLFrameSetElement() throws Exception {
        test("HTMLFrameSetElement", "HTMLFrameSetElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLHeadElement_HTMLHeadElement() throws Exception {
        test("HTMLHeadElement", "HTMLHeadElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLHeadingElement_HTMLHeadingElement() throws Exception {
        test("HTMLHeadingElement", "HTMLHeadingElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLHRElement_HTMLHRElement() throws Exception {
        test("HTMLHRElement", "HTMLHRElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLHtmlElement_HTMLHtmlElement() throws Exception {
        test("HTMLHtmlElement", "HTMLHtmlElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLIFrameElement_HTMLIFrameElement() throws Exception {
        test("HTMLIFrameElement", "HTMLIFrameElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLImageElement_HTMLImageElement() throws Exception {
        test("HTMLImageElement", "HTMLImageElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLImageElement_Image() throws Exception {
        //although Image != HTMLImageElement, they seem to be synonyms!!!
        test("HTMLImageElement", "Image");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLInputElement_HTMLInputElement() throws Exception {
        test("HTMLInputElement", "HTMLInputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLIsIndexElement_HTMLIsIndexElement() throws Exception {
        test("HTMLIsIndexElement", "HTMLIsIndexElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLLabelElement_HTMLLabelElement() throws Exception {
        test("HTMLLabelElement", "HTMLLabelElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLLegendElement_HTMLLegendElement() throws Exception {
        test("HTMLLegendElement", "HTMLLegendElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLLIElement_HTMLLIElement() throws Exception {
        test("HTMLLIElement", "HTMLLIElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLLinkElement_HTMLLinkElement() throws Exception {
        test("HTMLLinkElement", "HTMLLinkElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLMapElement_HTMLMapElement() throws Exception {
        test("HTMLMapElement", "HTMLMapElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLMarqueeElement_HTMLMarqueeElement() throws Exception {
        test("HTMLMarqueeElement", "HTMLMarqueeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLMediaElement_Audio() throws Exception {
        test("HTMLMediaElement", "Audio");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLMediaElement_HTMLAudioElement() throws Exception {
        test("HTMLMediaElement", "HTMLAudioElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLMediaElement_HTMLMediaElement() throws Exception {
        test("HTMLMediaElement", "HTMLMediaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLMediaElement_HTMLVideoElement() throws Exception {
        test("HTMLMediaElement", "HTMLVideoElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLMenuElement_HTMLMenuElement() throws Exception {
        test("HTMLMenuElement", "HTMLMenuElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            FF78 = "true")
    public void _HTMLMenuItemElement_HTMLMenuItemElement() throws Exception {
        test("HTMLMenuItemElement", "HTMLMenuItemElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLMetaElement_HTMLMetaElement() throws Exception {
        test("HTMLMetaElement", "HTMLMetaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLMeterElement_HTMLMeterElement() throws Exception {
        test("HTMLMeterElement", "HTMLMeterElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLModElement_HTMLModElement() throws Exception {
        test("HTMLModElement", "HTMLModElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLNextIdElement_HTMLNextIdElement() throws Exception {
        test("HTMLNextIdElement", "HTMLNextIdElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLObjectElement_HTMLObjectElement() throws Exception {
        test("HTMLObjectElement", "HTMLObjectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLOListElement_HTMLOListElement() throws Exception {
        test("HTMLOListElement", "HTMLOListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLOptGroupElement_HTMLOptGroupElement() throws Exception {
        test("HTMLOptGroupElement", "HTMLOptGroupElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLOptionElement_HTMLOptionElement() throws Exception {
        test("HTMLOptionElement", "HTMLOptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLOptionElement_Option() throws Exception {
        //although Option != HTMLOptionElement, they seem to be synonyms!!!
        test("HTMLOptionElement", "Option");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLOptionsCollection_HTMLOptionsCollection() throws Exception {
        test("HTMLOptionsCollection", "HTMLOptionsCollection");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLOutputElement_HTMLOutputElement() throws Exception {
        test("HTMLOutputElement", "HTMLOutputElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLParagraphElement_HTMLParagraphElement() throws Exception {
        test("HTMLParagraphElement", "HTMLParagraphElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLParamElement_HTMLParamElement() throws Exception {
        test("HTMLParamElement", "HTMLParamElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLPhraseElement_HTMLPhraseElement() throws Exception {
        test("HTMLPhraseElement", "HTMLPhraseElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLPictureElement_HTMLPictureElement() throws Exception {
        test("HTMLPictureElement", "HTMLPictureElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLPreElement_HTMLPreElement() throws Exception {
        test("HTMLPreElement", "HTMLPreElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLProgressElement_HTMLProgressElement() throws Exception {
        test("HTMLProgressElement", "HTMLProgressElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLQuoteElement_HTMLQuoteElement() throws Exception {
        test("HTMLQuoteElement", "HTMLQuoteElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLScriptElement_HTMLScriptElement() throws Exception {
        test("HTMLScriptElement", "HTMLScriptElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLSelectElement_HTMLSelectElement() throws Exception {
        test("HTMLSelectElement", "HTMLSelectElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            CHROME = "true",
            EDGE = "true")
    public void _HTMLShadowElement_HTMLShadowElement() throws Exception {
        test("HTMLShadowElement", "HTMLShadowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLSlotElement_HTMLSlotElement() throws Exception {
        test("HTMLSlotElement", "HTMLSlotElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLSourceElement_HTMLSourceElement() throws Exception {
        test("HTMLSourceElement", "HTMLSourceElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLSpanElement_HTMLSpanElement() throws Exception {
        test("HTMLSpanElement", "HTMLSpanElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLStyleElement_HTMLStyleElement() throws Exception {
        test("HTMLStyleElement", "HTMLStyleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTableCaptionElement_HTMLTableCaptionElement() throws Exception {
        test("HTMLTableCaptionElement", "HTMLTableCaptionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTableCellElement_HTMLTableCellElement() throws Exception {
        test("HTMLTableCellElement", "HTMLTableCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLTableCellElement_HTMLTableDataCellElement() throws Exception {
        test("HTMLTableCellElement", "HTMLTableDataCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLTableCellElement_HTMLTableHeaderCellElement() throws Exception {
        test("HTMLTableCellElement", "HTMLTableHeaderCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTableColElement_HTMLTableColElement() throws Exception {
        test("HTMLTableColElement", "HTMLTableColElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLTableDataCellElement_HTMLTableDataCellElement() throws Exception {
        test("HTMLTableDataCellElement", "HTMLTableDataCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTableElement_HTMLTableElement() throws Exception {
        test("HTMLTableElement", "HTMLTableElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "true")
    public void _HTMLTableHeaderCellElement_HTMLTableHeaderCellElement() throws Exception {
        test("HTMLTableHeaderCellElement", "HTMLTableHeaderCellElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTableRowElement_HTMLTableRowElement() throws Exception {
        test("HTMLTableRowElement", "HTMLTableRowElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTableSectionElement_HTMLTableSectionElement() throws Exception {
        test("HTMLTableSectionElement", "HTMLTableSectionElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLTemplateElement_HTMLTemplateElement() throws Exception {
        test("HTMLTemplateElement", "HTMLTemplateElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTextAreaElement_HTMLTextAreaElement() throws Exception {
        test("HTMLTextAreaElement", "HTMLTextAreaElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    public void _HTMLTimeElement_HTMLTimeElement() throws Exception {
        test("HTMLTimeElement", "HTMLTimeElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTitleElement_HTMLTitleElement() throws Exception {
        test("HTMLTitleElement", "HTMLTitleElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLTrackElement_HTMLTrackElement() throws Exception {
        test("HTMLTrackElement", "HTMLTrackElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLUListElement_HTMLUListElement() throws Exception {
        test("HTMLUListElement", "HTMLUListElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLUnknownElement_HTMLUnknownElement() throws Exception {
        test("HTMLUnknownElement", "HTMLUnknownElement");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void _HTMLVideoElement_HTMLVideoElement() throws Exception {
        test("HTMLVideoElement", "HTMLVideoElement");
    }

}
