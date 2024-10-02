package org.htmlunit.benchmarks;

import java.util.concurrent.TimeUnit;

import org.htmlunit.BrowserVersion;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Timeout;
import org.openjdk.jmh.annotations.Warmup;

@State(Scope.Benchmark)
public class HtmlUnitBenchmark extends SimpleWebTestCase {

    String html = """
            <html id="1253372200" duda_id="1253372200">
            <head id="1755471494" duda_id="1755471494">
            </head>
            <body dmwrapped="true" id="1901957768" duda_id="1901957768" class="dm-home-page" themewaschanged="true">
            <div id="allWrapper" class="allWrapper" duda_id="allWrapper">
              <!-- navigation placeholders -->
              <div id="navWrapper" class="navWrapper" dmle_nee_nav="true" duda_id="navWrapper">
                <div id="hiddenNavPlaceHolder" class="hiddenNavPlaceHolder navPlaceHolder dmDisplay_None" navplaceholder="true" duda_id="hiddenNavPlaceHolder">
                </div>
                <div id="backToHomePlaceHolder" class="backToHomePlaceHolder navPlaceHolder" navplaceholder="true" dmle_nav_view="backToHome" duda_id="backToHomePlaceHolder">
                </div>
                <div id="newNavigationElementPlaceHolder" class="newNavigationElementPlaceHolder navPlaceHolder" navplaceholder="true" dmle_nav_view="showNav" duda_id="newNavigationElementPlaceHolder">
                </div>
                <div id="newNavigationSubNavPlaceHolder" class="newNavigationSubNavPlaceHolder navPlaceHolder" navplaceholder="true" dmle_nav_view="subNav" duda_id="newNavigationSubNavPlaceHolder">
                </div>
              </div>
              <div id="dm_content" class="dmContent" duda_id="dm_content">
                <div dm:templateorder="170" class="dmHomeRespTmpl mainBorder dmRespRowsWrapper dmFullRowRespTmpl" id="1716942098" duda_id="1716942098">
                  <div class="dmRespRow fullBleedChanged fullBleedMode u_1612054359 mobile-columns-reversed" duda_id="1612054359" id="1612054359">
                    <div class="dmRespColsWrapper" duda_id="1553988516" id="1553988516">
                      <div class="u_1670260801 dmRespCol empty-column small-12 large-6 medium-6" duda_id="1670260801" id="1670260801">
                        <div class="u_1152709336 dmNewParagraph" dmle_widget="paragraph" data-element-type="paragraph" id="1152709336" duda_id="1152709336" style="transition: none 0s ease 0s; display: block; line-height: initial; margin-top: 0px !important;" data-uialign="left" no_space_e="true" data-editor-state="closed" data-version="5">
                          <h1 class="m-text-align-center" style="line-height: 1;" localization_key="templates.custom.tsjDOps.4">
                      <span style="font-weight: 700; font-family: &apos;Red Rose&apos;; display: initial;" no_space_b="true" no_space_e="true">
                       Stella Levinson
                      </span>
                          </h1>
                        </div>
                        <div class="u_1075054509 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" id="1075054509" duda_id="1075054509" style="transition: none 0s ease 0s; text-align: left;" data-uialign="center" no_space_e="true" data-version="5">
                          <h4 class="m-text-align-center" localization_key="templates.custom.LgFLa1i.70">
                      <span style="font-weight: 700; font-family: &apos;Public Sans&apos;; display: initial;" no_space_b="true" no_space_e="true">
                       Project manager
                      </span>
                          </h4>
                        </div>
                        <div class="u_1799381409 dmNewParagraph ql-disabled" data-dmtmpl="true" data-element-type="paragraph" id="1799381409" duda_id="1799381409" style="transition: none 0s ease 0s; text-align: left; display: block;" data-uialign="left" data-version="5">
                          <h4 class="m-text-align-center" style="line-height: 1.5;" localization_key="templates.custom.4L4aN7P.5">
                      <span style="display: initial;" no_space_b="true" no_space_e="true">
                       Looking for my next opportunity to make a change. The digital way.
                      </span>
                          </h4>
                        </div>
                        <div class="u_1501309825 dmRespRow" duda_id="1501309825" id="1501309825">
                          <div class="dmRespColsWrapper" duda_id="1241775793" id="1241775793">
                            <div class="u_1919870676 dmRespCol empty-column small-12 large-5 medium-5" duda_id="1919870676" id="1919870676">
                              <a data-display-type="block" class="u_1375947128 align-center dmButtonLink dmWidget dmWwr default dmOnlyButton dmDefaultGradient" file="false" href="/#Resume" dmle_widget="dudaButtonLinkId" data-element-type="dButtonLinkId" id="1375947128" duda_id="1375947128" icon-name="icon-apple" dm_dont_rewrite_url="false">
                        <span class="iconBg" id="1771437997" duda_id="1771437997">
                         <span class="icon hasFontIcon icon-star" id="1629513736" duda_id="1629513736">
                         </span>
                        </span>
                                <span class="text" id="1484031977" duda_id="1484031977">
                         SEE RESUME
                        </span>
                              </a>
                            </div>
                            <div class="u_1793016313 dmRespCol empty-column small-12 large-5 medium-5" duda_id="1793016313" id="1793016313">
                              <a data-display-type="block" class="u_1503672830 align-center dmButtonLink dmWidget dmWwr default dmOnlyButton dmDefaultGradient" file="false" href="/#Contact" dmle_widget="dudaButtonLinkId" data-element-type="dButtonLinkId" id="1503672830" duda_id="1503672830" icon-name="icon-android" dm_dont_rewrite_url="false">
                        <span class="iconBg" id="1525309787" duda_id="1525309787">
                         <span class="icon hasFontIcon icon-star" id="1387430132" duda_id="1387430132">
                         </span>
                        </span>
                                <span class="text" id="1274515524" duda_id="1274515524">
                         CONTACT ME
                        </span>
                              </a>
                            </div>
                            <div class="u_1304981671 dmRespCol empty-column small-12 large-2 medium-2 hide-for-medium" id="1304981671" duda_id="1304981671">
                              <div dmle_widget="spacer" data-element-type="spacer" class="dmSpacer" id="1734893940" duda_id="1734893940">
                              </div>
                            </div>
                          </div>
                        </div>
                      </div>
                      <div class="u_1258021451 dmRespCol empty-column small-12 large-6 medium-6" id="1258021451" duda_id="1258021451">
                        <div class="u_1173781913 imageWidget align-center" editablewidget="true" data-element-type="image" dmle_widget="image" data-widget-type="image" id="1173781913" duda_id="1173781913">
                          <a href="/" id="1183082945" duda_id="1183082945">
                            <img src="https://master-image-res.s3.amazonaws.com/md/dmtmpl/fe43b84e-7fb3-4fa6-b954-e92c77aaa2bb/dms3rep/multi/woman-cv-portrait-pink-background.jpg" alt="" id="1540041305" duda_id="1540041305" class="" data-dm-image-path="https://master-image-res.s3.amazonaws.com/md/dmtmpl/fe43b84e-7fb3-4fa6-b954-e92c77aaa2bb/dms3rep/multi/woman-cv-portrait-pink-background.jpg"/>
                          </a>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="u_About dmRespRow" duda_id="About" style="text-align: center;" id="About" data-anchor="About">
                    <div class="dmRespColsWrapper" duda_id="1173148036" id="1173148036">
                      <div class="u_1620106714 dmRespCol small-12 large-6 medium-6" duda_id="1620106714" id="1620106714">
                        <div class="u_1787242025 imageWidget align-center" editablewidget="true" data-element-type="image" dmle_widget="image" data-widget-type="image" id="1787242025" duda_id="1787242025">
                          <img src="https://master-image-res.s3.amazonaws.com/md/dmtmpl/fe43b84e-7fb3-4fa6-b954-e92c77aaa2bb/dms3rep/multi/Smiling-woman-portrait-pink-shirt.jpg" alt="" id="1412531738" duda_id="1412531738" class="" data-dm-image-path="https://master-image-res.s3.amazonaws.com/md/dmtmpl/fe43b84e-7fb3-4fa6-b954-e92c77aaa2bb/dms3rep/multi/Smiling-woman-portrait-pink-shirt.jpg"/>
                        </div>
                      </div>
                      <div class="u_1822259188 dmRespCol empty-column small-12 large-6 medium-6" id="1822259188" duda_id="1822259188">
                        <div class="u_1291993299 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1291993299" duda_id="1291993299" style="transition: opacity 1s ease-in-out 0s;" data-uialign="right">
                          <h2 localization_key="templates.custom.sJSQKu3.7">
                      <span style="display: unset; font-family: &apos;Red Rose&apos;; font-weight: 700;" no_space_b="true" no_space_e="true">
                       I advise companies and NGOs in initiatives and campaigns
                      </span>
                          </h2>
                        </div>
                        <div class="u_1133695970 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1133695970" duda_id="1133695970" style="transition: opacity 1s ease-in-out 0s;" data-uialign="left">
                          <p style="line-height: 2;" no_space_b="true" no_space_e="true" localization_key="templates.custom.JFYfTDO.8">
                      <span style="display: unset;" no_space_b="true" no_space_e="true">
                       For more than 15 years, I've been working with international clients on a range of projects, gathering experience in diverse areas. I look forward to hearing about your project and plan its success together.&nbsp;
                      </span>
                          </p>
                        </div>
                        <a data-display-type="block" class="u_1468422761 align-center dmButtonLink dmWidget dmWwr default dmOnlyButton dmDefaultGradient" file="false" href="/#Resume" dmle_widget="dudaButtonLinkId" data-element-type="dButtonLinkId" id="1468422761" duda_id="1468422761" icon-name="icon-apple" dm_dont_rewrite_url="false">
                     <span class="iconBg" id="1788160174" duda_id="1788160174">
                      <span class="icon hasFontIcon icon-star" id="1769921985" duda_id="1769921985">
                      </span>
                     </span>
                          <span class="text" id="1247984195" duda_id="1247984195">
                      SEE RESUME
                     </span>
                        </a>
                      </div>
                    </div>
                  </div>
                  <div class="dmRespRow u_pa_5415952" duda_id="pa_5415952" id="pa_5415952">
                    <div class="dmRespColsWrapper" duda_id="1574262555" id="1574262555">
                      <div class="dmRespCol empty-column large-12 medium-12 small-12 u_1165363466" duda_id="1165363466" id="1165363466">
                        <div class="u_1244696584 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1244696584" duda_id="1244696584" style="transition: opacity 1s ease-in-out 0s;" data-uialign="left">
                          <h2 class="text-align-center" localization_key="templates.custom.7Tvb6ih.9">
                      <span style="font-weight: 700; font-family: &apos;Red Rose&apos;; display: unset;" no_space_b="true" no_space_e="true">
                       Recommendations
                      </span>
                          </h2>
                        </div>
                        <div class="u_1122639017 flex-container dmImageSlider dmNoMargin dmNoMark" editablewidget="true" data-widget-type="imageSlider" dmle_volatile_widget="true" dmle_widget="dudaSliderId" data-element-type="dSliderId" id="1122639017" duda_id="1122639017">
                          <div class="flexslider ed-version nav-layout-2" sliderscriptparams="{&apos;stretch&apos;:true,&apos;animation&apos;:true,&apos;randomize&apos;:false,&apos;directionNav&apos;:true,&apos;isAutoPlay&apos;:true,&apos;isFade&apos;:true,&apos;controlNav&apos;:false,&apos;slideshowSpeed&apos;:7000,&apos;animationDuration&apos;:600,&apos;pausePlay&apos;:true,&apos;prevText&apos;:&apos;&apos;,&apos;nextText&apos;:&apos;&apos;}" id="1650991305" duda_id="1650991305">
                            <ul class="slides" id="1480311421" duda_id="1480311421">
                              <li layout="center" position="center" animation="slideInLeft" show-content="true" color-overlay="true" text-background="true" id="1345569005" duda_id="1345569005" class="" show-caption="true">
                                <img dm="true" src="https://irt-cdn.multiscreensite.com/60004bcbaf1841caaaef1d7babde589b/dms3rep/multi/empty_02.png" id="1121811699" duda_id="1121811699"/>
                                <div class="color-overlay" id="1342448321" duda_id="1342448321">
                                </div>
                                <div class="slide-inner" id="1378020215" duda_id="1378020215">
                                  <div class="text-wrapper" id="1971839218" duda_id="1971839218">
                                    <h3 class="slide-title u_1854925658" id="1854925658" duda_id="1854925658">
                                    </h3>
                                    <div class="slide-text richText u_1316600406" id="1316600406" duda_id="1316600406" no_space_e="true" style="">
                                      <p class="rteBlock" no_space_b="true" no_space_e="true">
                                        Working with Stella was an unforgettable experience! She is insightful, engaging and full of energy. She had an incredible impact on the project.&nbsp;
                                      </p>
                                    </div>
                                  </div>
                                  <div class="slide-button dmWidget clearfix slide-button-visible" id="1986515136" duda_id="1986515136">
                          <span class="iconBg" dmle_generated="true" id="1080773607" duda_id="1080773607">
                           <span class="icon hasFontIcon icon-star" dmle_dont_remove="true" dmle_generated="true" id="1160022221" duda_id="1160022221">
                           </span>
                          </span>
                                    <span class="text" id="1288206133" duda_id="1288206133">
                           Dana Rosen | Nickel
                          </span>
                                  </div>
                                </div>
                              </li>
                              <li layout="center" position="center" animation="slideInLeft" show-content="true" color-overlay="true" text-background="true" id="1677996263" duda_id="1677996263" class="" show-caption="true">
                                <img dm="true" src="https://irt-cdn.multiscreensite.com/60004bcbaf1841caaaef1d7babde589b/dms3rep/multi/empty_02.png" id="1700813576" duda_id="1700813576"/>
                                <div class="color-overlay" id="1013856460" duda_id="1013856460">
                                </div>
                                <div class="slide-inner" id="1241126171" duda_id="1241126171">
                                  <div class="text-wrapper" id="1237299636" duda_id="1237299636">
                                    <h3 class="slide-title u_1498022897" id="1498022897" duda_id="1498022897">
                                      Slide title
                                    </h3>
                                    <div class="slide-text richText u_1213140029" id="1213140029" duda_id="1213140029" no_space_e="true">
                                      <p class="rteBlock" no_space_b="true" no_space_e="true">
                                        Working with Stella was an unforgettable experience! She is insightful, engaging and full of energy. She had an incredible impact on the project.&nbsp;
                                      </p>
                                    </div>
                                  </div>
                                  <div class="slide-button dmWidget clearfix slide-button-visible" id="1572198726" duda_id="1572198726">
                          <span class="iconBg" dmle_generated="true" id="1327503749" duda_id="1327503749">
                           <span class="icon hasFontIcon icon-star" dmle_dont_remove="true" dmle_generated="true" id="1597230146" duda_id="1597230146">
                           </span>
                          </span>
                                    <span class="text" id="1548814394" duda_id="1548814394">
                           John Taylor | Tuesday
                          </span>
                                  </div>
                                </div>
                              </li>
                              <li layout="center" position="center" animation="fadeInUp" show-content="true" color-overlay="true" text-background="true" id="1824818719" duda_id="1824818719" class="" show-caption="true">
                                <img dm="true" src="https://irt-cdn.multiscreensite.com/60004bcbaf1841caaaef1d7babde589b/dms3rep/multi/empty_02.png" id="1467786613" duda_id="1467786613"/>
                                <div class="color-overlay" id="1666262621" duda_id="1666262621">
                                </div>
                                <div class="slide-inner" id="1663730747" duda_id="1663730747">
                                  <div class="text-wrapper" id="1546639561" duda_id="1546639561">
                                    <h3 class="slide-title u_1982140297" id="1982140297" duda_id="1982140297">
                                      Slide title
                                    </h3>
                                    <div class="slide-text richText u_1635297109" id="1635297109" duda_id="1635297109" no_space_e="true">
                                      <p class="rteBlock" no_space_b="true" no_space_e="true">
                                        Working with Stella was an unforgettable experience! She is insightful, engaging and full of energy. She had an incredible impact on the project.&nbsp;
                                      </p>
                                    </div>
                                  </div>
                                  <div class="slide-button dmWidget clearfix slide-button-visible" id="1466611441" duda_id="1466611441">
                          <span class="iconBg" dmle_generated="true" id="1058366293" duda_id="1058366293">
                           <span class="icon hasFontIcon icon-star" dmle_dont_remove="true" dmle_generated="true" id="1223439770" duda_id="1223439770">
                           </span>
                          </span>
                                    <span class="text" id="1810713485" duda_id="1810713485">
                           Rina Brick | Eletrics
                          </span>
                                  </div>
                                </div>
                              </li>
                            </ul>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="dmRespRow u_1176755350" duda_id="1176755350" id="1176755350">
                    <div class="dmRespColsWrapper" duda_id="1418698645" id="1418698645">
                      <div class="dmRespCol empty-column small-12 medium-12 large-12" id="1843432655" duda_id="1843432655">
                        <div class="dmNewParagraph u_1295668183" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1295668183" duda_id="1295668183" style="transition: none 0s ease 0s; text-align: left; display: block;" data-uialign="center">
                          <h1 localization_key="templates.custom.un1Dl68.13">
                      <span style="color: rgb(254, 244, 245); font-weight: 600; font-family: &apos;Red Rose&apos;; display: initial;" no_space_b="true" no_space_e="true">
                       Work Experience
                      </span>
                          </h1>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="dmRespRow u_Resume" duda_id="Resume" id="Resume" data-anchor="Resume">
                    <div class="dmRespColsWrapper" duda_id="1890467726" id="1890467726">
                      <div class="dmRespCol empty-column small-12 large-6 medium-6" duda_id="1902404742" id="1902404742">
                        <div class="dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1708923745" duda_id="1708923745" style="transition: opacity 1s ease-in-out 0s;" data-uialign="left">
                          <h5 localization_key="templates.custom.ejxbWuA.14">
                      <span style="color: rgb(254, 244, 245); display: unset;" no_space_b="true" no_space_e="true">
                       2020-2021
                      </span>
                          </h5>
                        </div>
                        <div class="u_1920507446 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1920507446" duda_id="1920507446" style="transition: none 0s ease 0s; text-align: left;" data-uialign="left">
                          <h4 localization_key="templates.custom.cV6mPiT.15">
                      <span style="display: unset; color: rgb(254, 244, 245); font-family: &apos;Public Sans&apos;; font-weight: 600;" no_space_b="true" no_space_e="true">
                       Advisor, South Company
                      </span>
                          </h4>
                        </div>
                      </div>
                      <div class="dmRespCol empty-column large-6 medium-6 small-12" id="1942416174" duda_id="1942416174">
                        <div class="u_1826032935 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1826032935" duda_id="1826032935" style="transition: opacity 1s ease-in-out 0s;" data-uialign="right">
                          <p style="line-height: 1.5;" no_space_b="true" no_space_e="true" localization_key="templates.custom.KX4kpiD.16">
                      <span style="color: rgb(254, 244, 245); display: unset; font-family: &apos;Public Sans&apos;; font-weight: 300;" no_space_b="true" no_space_e="true">
                       In this position, I led a team of more than 40 professional and volunteer staff members, from various company departments.&nbsp;
                      </span>
                            <span single-space="true" class="" style="display: initial; font-weight: 300;" no_space_b="true" no_space_e="true">
                       <span style="display: initial; font-weight: 300;">
                       </span>
                      </span>
                          </p>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="dmRespRow u_1668929750" duda_id="1668929750" id="1668929750">
                    <div class="dmRespColsWrapper" duda_id="1632902107" id="1632902107">
                      <div class="dmRespCol empty-column small-12 large-6 medium-6" duda_id="1771667782" id="1771667782">
                        <div class="dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1986913998" duda_id="1986913998" style="transition: opacity 1s ease-in-out 0s;" data-uialign="left">
                          <h5 localization_key="templates.custom.9nwMRDO.17">
                      <span style="color: rgb(254, 244, 245); display: unset;" no_space_b="true" no_space_e="true">
                       2019-2020
                      </span>
                          </h5>
                        </div>
                        <div class="u_1553606713 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1553606713" duda_id="1553606713" style="transition: none 0s ease 0s; text-align: left;" data-uialign="left">
                          <h4 localization_key="templates.custom.XBnaXiL.18">
                      <span style="display: unset; font-weight: 600; color: rgb(254, 244, 245); font-family: &apos;Public Sans&apos;;" no_space_b="true" no_space_e="true">
                       Consultant, North Company
                      </span>
                          </h4>
                        </div>
                      </div>
                      <div class="dmRespCol empty-column large-6 medium-6 small-12" id="1167057381" duda_id="1167057381">
                        <div class="u_1961011275 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1961011275" duda_id="1961011275" style="transition: opacity 1s ease-in-out 0s;" data-uialign="right">
                          <p style="line-height: 1.5;" no_space_b="true" no_space_e="true" localization_key="templates.custom.KX4kpiD.16">
                      <span style="color: rgb(254, 244, 245); display: unset;" no_space_b="true" no_space_e="true">
                       In this position, I led a team of more than 40 professional and volunteer staff members, from various company departments.&nbsp;
                      </span>
                          </p>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="dmRespRow u_1769486185" duda_id="1769486185" id="1769486185">
                    <div class="dmRespColsWrapper" duda_id="1697336106" id="1697336106">
                      <div class="dmRespCol empty-column small-12 large-6 medium-6" duda_id="1280406245" id="1280406245">
                        <div class="dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1102924901" duda_id="1102924901" style="transition: opacity 1s ease-in-out 0s;" data-uialign="left">
                          <h5 localization_key="templates.custom.glc4ygJ.19">
                      <span style="color: rgb(254, 244, 245); display: unset;" no_space_b="true" no_space_e="true">
                       2018-2019
                      </span>
                          </h5>
                        </div>
                        <div class="u_1530189599 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1530189599" duda_id="1530189599" style="transition: none 0s ease 0s; text-align: left;" data-uialign="left">
                          <h4 localization_key="templates.custom.KaEpVr7.20">
                      <span style="display: unset; color: rgb(254, 244, 245); font-family: &apos;Public Sans&apos;; font-weight: 600;" no_space_b="true" no_space_e="true">
                       Project manager, East Company
                      </span>
                          </h4>
                        </div>
                      </div>
                      <div class="dmRespCol empty-column large-6 medium-6 small-12" id="1441629520" duda_id="1441629520">
                        <div class="u_1617800209 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1617800209" duda_id="1617800209" style="transition: opacity 1s ease-in-out 0s;" data-uialign="right">
                          <p style="line-height: 1.5;" no_space_b="true" no_space_e="true" localization_key="templates.custom.KnITsI6.21">
                      <span class="" style="display: unset; color: rgb(254, 244, 245);" no_space_b="true" no_space_e="true">
                       <span style="display: unset; color: rgb(254, 244, 245);" no_space_b="true">
                        In this position, I led a team of more than 40 professional and volunteer staff members, from various company departments.
                       </span>
                      </span>
                          </p>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="dmRespRow u_1932440846" duda_id="1932440846" id="1932440846">
                    <div class="dmRespColsWrapper" duda_id="1421389444" id="1421389444">
                      <div class="dmRespCol empty-column large-12 medium-12 small-12" duda_id="1059054081" id="1059054081">
                        <div class="dmDividerWrapper clearfix u_1221215378" dmle_widget="dudaDividerId" data-element-type="dDividerId" data-layout="divider-style-1" data-widget-version="2" id="1221215378" duda_id="1221215378">
                          <hr class="dmDivider" style="border-width:2px; border-top-style:solid; color:grey;" id="1853916308" duda_id="1853916308"/>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="dmRespRow u_1869329494" duda_id="1869329494" id="1869329494">
                    <div class="dmRespColsWrapper" duda_id="1463742773" id="1463742773">
                      <div class="dmRespCol empty-column small-12 large-6 medium-6" duda_id="1246092528" id="1246092528">
                        <div class="u_1610237263 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1610237263" duda_id="1610237263" style="transition: none 0s ease 0s; text-align: left; display: block;" data-uialign="center">
                          <h2 localization_key="templates.custom.PiPlUIt.22">
                      <span style="display: initial; color: rgb(254, 244, 245); font-family: &apos;Red Rose&apos;; font-weight: 700;" no_space_b="true" no_space_e="true">
                       Education
                      </span>
                          </h2>
                        </div>
                      </div>
                      <div class="dmRespCol empty-column large-6 medium-6 small-12" id="1520163291" duda_id="1520163291">
                        <div class="u_1867428546 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1867428546" duda_id="1867428546" style="transition: opacity 1s ease-in-out 0s;" data-uialign="right">
                          <h5 localization_key="templates.custom.Kxg4ZHk.23">
                      <span style="color: rgb(254, 244, 245); display: unset;" no_space_b="true" no_space_e="true">
                       2014-2018
                      </span>
                          </h5>
                        </div>
                        <div class="u_1531851164 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1531851164" duda_id="1531851164" style="transition: none 0s ease 0s; text-align: left; display: block;" data-uialign="right">
                          <h4 localization_key="templates.custom.VyCPGeu.24">
                      <span style="display: unset; color: rgb(254, 244, 245); font-family: &apos;Public Sans&apos;; font-weight: 600;" no_space_b="true" no_space_e="true">
                       New York University
                      </span>
                          </h4>
                        </div>
                        <div class="u_1591656626 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1591656626" duda_id="1591656626" style="transition: opacity 1s ease-in-out 0s;" data-uialign="right">
                          <p style="line-height: 1.5;" no_space_b="true" no_space_e="true" localization_key="templates.custom.zHiqTBU.25">
                      <span class="" style="display: unset; color: rgb(254, 244, 245);" no_space_b="true" no_space_e="true">
                       <span style="display: unset; color: rgb(254, 244, 245);" no_space_b="true">
                        MBA in International Studies and Economics
                       </span>
                      </span>
                          </p>
                        </div>
                        <div class="u_1671421179 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1671421179" duda_id="1671421179" style="transition: opacity 1s ease-in-out 0s;" data-uialign="right">
                          <h5 localization_key="templates.custom.lu56BNq.26">
                      <span style="color: rgb(254, 244, 245); display: unset;" no_space_b="true" no_space_e="true">
                       2010-2014
                      </span>
                          </h5>
                        </div>
                        <div class="u_1046408906 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1046408906" duda_id="1046408906" style="transition: none 0s ease 0s; text-align: left; display: block;" data-uialign="right">
                          <h4 localization_key="templates.custom.Yy4JWZ3.27">
                      <span style="display: unset; color: rgb(254, 244, 245); font-family: &apos;Public Sans&apos;; font-weight: 600;" no_space_b="true" no_space_e="true">
                       Texas University
                      </span>
                          </h4>
                        </div>
                        <div class="u_1631383041 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1631383041" duda_id="1631383041" style="transition: opacity 1s ease-in-out 0s;" data-uialign="right">
                          <p style="line-height: 1.5;" no_space_b="true" no_space_e="true" localization_key="templates.custom.W3JxSzf.28">
                      <span style="display: unset; color: rgb(254, 244, 245);" no_space_b="true" no_space_e="true">
                       Bachelor of Arts in International Studies (BAIS)
                      </span>
                          </p>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="dmRespRow u_1561437806" duda_id="1561437806" id="1561437806">
                    <div class="dmRespColsWrapper" duda_id="1419274859" id="1419274859">
                      <div class="dmRespCol empty-column large-12 medium-12 small-12" duda_id="1369745680" id="1369745680">
                        <div class="dmDividerWrapper clearfix u_1314427049" dmle_widget="dudaDividerId" data-element-type="dDividerId" data-layout="divider-style-1" data-widget-version="2" id="1314427049" duda_id="1314427049">
                          <hr class="dmDivider" style="border-width:2px; border-top-style:solid; color:grey;" id="1514774937" duda_id="1514774937"/>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="dmRespRow u_Skills" duda_id="Skills" id="Skills" mode="3" data-anchor="Skills">
                    <div class="dmRespColsWrapper" duda_id="1212843828" id="1212843828">
                      <div class="u_1331384729 dmRespCol empty-column small-12 large-6 medium-6" duda_id="1331384729" id="1331384729">
                        <div class="u_1856174802 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1856174802" duda_id="1856174802" style="transition: none 0s ease 0s; text-align: left; display: block;" data-uialign="center">
                          <h2 localization_key="templates.custom.CM9k146.29">
                      <span style="font-weight: 700; font-family: &apos;Red Rose&apos;; color: rgb(254, 244, 245); display: initial;" no_space_b="true" no_space_e="true">
                       Professional skills
                      </span>
                          </h2>
                        </div>
                      </div>
                      <div class="u_1065448118 dmRespCol empty-column large-3 medium-3 small-6" id="1065448118" duda_id="1065448118">
                        <div class="u_1701633262 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1701633262" duda_id="1701633262" style="transition: none 0s ease 0s; text-align: left;" data-uialign="left">
                          <h4 localization_key="templates.custom.vzl4306.30">
                      <span style="color: rgb(254, 244, 245); font-family: &apos;Public Sans&apos;; display: unset; font-weight: 600;" no_space_b="true" no_space_e="true">
                       Entrepreneurial Mindset
                      </span>
                          </h4>
                        </div>
                        <div class="u_1810754977 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1810754977" duda_id="1810754977" style="transition: none 0s ease 0s; text-align: left;" data-uialign="left">
                          <h4 localization_key="templates.custom.Ls5uj1l.31">
                      <span style="color: rgb(254, 244, 245); font-family: &apos;Public Sans&apos;; display: unset; font-weight: 600;" no_space_b="true" no_space_e="true">
                       Go-to-Market Planning
                      </span>
                          </h4>
                        </div>
                      </div>
                      <div class="u_1324460135 dmRespCol empty-column large-3 medium-3 small-6" id="1324460135" duda_id="1324460135">
                        <div class="u_1067975976 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1067975976" duda_id="1067975976" style="transition: none 0s ease 0s; text-align: left;" data-uialign="left">
                          <h4 localization_key="templates.custom.jX1GQvW.32">
                      <span style="display: unset; color: rgb(254, 244, 245); font-family: &apos;Public Sans&apos;; font-weight: 600;" no_space_b="true" no_space_e="true">
                       Teamwork &amp; Collaboration
                      </span>
                          </h4>
                        </div>
                        <div class="u_1034710117 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1034710117" duda_id="1034710117" style="transition: none 0s ease 0s; text-align: left;" data-uialign="left">
                          <h4 localization_key="templates.custom.YRZnR7U.33">
                      <span style="display: unset; color: rgb(254, 244, 245); font-family: &apos;Public Sans&apos;; font-weight: 600;" no_space_b="true" no_space_e="true">
                       Digital Analytics
                      </span>
                          </h4>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="dmRespRow u_1201107252" duda_id="1201107252" id="1201107252">
                    <div class="dmRespColsWrapper" duda_id="1104606757" id="1104606757">
                      <div class="dmRespCol empty-column large-12 medium-12 small-12" duda_id="1670220500" id="1670220500">
                        <div class="dmDividerWrapper clearfix u_1319024607" dmle_widget="dudaDividerId" data-element-type="dDividerId" data-layout="divider-style-1" data-widget-version="2" id="1319024607" duda_id="1319024607">
                          <hr class="dmDivider" style="border-width:2px; border-top-style:solid; color:grey;" id="1076926646" duda_id="1076926646"/>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="dmRespRow u_1546507235" duda_id="1546507235" id="1546507235" mode="3">
                    <div class="dmRespColsWrapper" duda_id="1295916566" id="1295916566">
                      <div class="u_1822960922 dmRespCol empty-column small-12 large-6 medium-6" duda_id="1822960922" id="1822960922">
                        <div class="u_1795638472 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1795638472" duda_id="1795638472" style="transition: none 0s ease 0s; text-align: left; display: block;" data-uialign="center">
                          <h2>
                      <span style="font-weight: 700; font-family: &apos;Red Rose&apos;; color: rgba(255, 255, 0, 0.5); display: initial;" no_space_b="true" no_space_e="true">
                       Languages
                      </span>
                          </h2>
                        </div>
                      </div>
                      <div class="u_1336781317 dmRespCol empty-column large-3 medium-3 small-6" id="1336781317" duda_id="1336781317">
                        <div class="u_1693693729 dmNewParagraph ql-disabled" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1693693729" duda_id="1693693729" style="transition: none 0s ease 0s; text-align: left; display: block;" data-uialign="left">
                          <h4>
                      <span style="font-weight: 700; display: unset; font-family: &apos;Public Sans&apos;; color: rgba(255, 255, 0, 0.5);" no_space_b="true" no_space_e="true">
                       English
                      </span>
                          </h4>
                        </div>
                        <div class="u_1569758128 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1569758128" duda_id="1569758128" style="transition: none 0s ease 0s; text-align: left; display: block;" data-uialign="left">
                          <h4>
                      <span style="font-weight: 700; display: unset; font-family: &apos;Public Sans&apos;; color: rgba(255, 255, 0, 0.5);" no_space_b="true" no_space_e="true">
                       French
                      </span>
                          </h4>
                        </div>
                      </div>
                      <div class="u_1889391922 dmRespCol empty-column large-3 medium-3 small-6" id="1889391922" duda_id="1889391922">
                        <div class="u_1999146337 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1999146337" duda_id="1999146337" style="transition: none 0s ease 0s; text-align: left; display: block;" data-uialign="left">
                          <h4>
                      <span style="font-weight: 700; font-family: &apos;Public Sans&apos;; color: rgba(255, 255, 0, 0.5); display: unset;" no_space_b="true" no_space_e="true">
                       Dutch
                      </span>
                          </h4>
                        </div>
                        <div class="u_1134298912 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1134298912" duda_id="1134298912" style="transition: none 0s ease 0s; text-align: left;" data-uialign="left">
                          <h4>
                      <span style="font-weight: 700; font-family: &apos;Public Sans&apos;; color: rgba(255, 255, 0, 0.5); display: unset;" no_space_b="true" no_space_e="true">
                       Spanish
                      </span>
                          </h4>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="dmRespRow u_Contact" duda_id="Contact" id="Contact" data-anchor="Contact">
                    <div class="dmRespColsWrapper" duda_id="1282377539" id="1282377539">
                      <div class="dmRespCol empty-column small-12 large-6 medium-6" duda_id="1973642587" id="1973642587">
                        <div class="u_1047860777 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1047860777" duda_id="1047860777" style="transition: none 0s ease 0s; text-align: left; display: block;" data-uialign="left">
                          <h1 localization_key="templates.custom.1794">
                      <span style="display: initial; color: rgb(254, 244, 245); font-family: &apos;Red Rose&apos;; font-weight: 700;" no_space_b="true" no_space_e="true">
                       Get in Touch
                      </span>
                          </h1>
                        </div>
                        <div class="u_1204388963 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1204388963" duda_id="1204388963" style="transition: opacity 1s ease-in-out 0s;" data-uialign="left">
                          <p no_space_b="true" no_space_e="true" localization_key="templates.custom.Xm0Zqsm.39">
                      <span class="" style="color: rgb(254, 244, 245); display: unset;" no_space_b="true" no_space_e="true">
                       <span style="color: rgb(254, 244, 245); display: unset;" no_space_b="true">
                        Want to hear more about how I can help your project succeed. Send me an email or call me and let's talk about your needs.
                       </span>
                      </span>
                          </p>
                        </div>
                        <div class="u_1587648856 dmNewParagraph" data-dmtmpl="true" data-element-type="paragraph" data-version="5" id="1587648856" duda_id="1587648856" style="transition: none 0s ease 0s; text-align: left; display: block;" data-uialign="left">
                          <h4 localization_key="templates.custom.zR5R1sN.40">
                      <span style="display: initial; color: rgb(254, 244, 245); font-family: &apos;Public Sans&apos;; font-weight: 700;" no_space_b="true" no_space_e="true">
                       Call: 123-456-7890
                      </span>
                          </h4>
                          <h4 style="line-height: normal;" localization_key="templates.custom.280">
                      <span style="display: initial; color: rgb(254, 244, 245); font-family: &apos;Public Sans&apos;; font-weight: 700;" no_space_b="true" no_space_e="true">
                       <br/>
                      </span>
                          </h4>
                          <h4 localization_key="templates.custom.skeJRM8.41">
                      <span style="display: initial; color: rgb(254, 244, 245); font-family: &apos;Public Sans&apos;; font-weight: 700;" no_space_b="true" no_space_e="true">
                       Write: info@mysite.com
                      </span>
                          </h4>
                        </div>
                        <span dmle_extension="social_hub" wr="true" networks="" icon="true" surround="true" class="u_1060369786 align-center text-align-center" adwords="" dmle_clean="true" data-editor="eyJ0aXRsZSI6IiIsImRpc3BsYXlUaXRsZSI6dHJ1ZSwiZm9yY2VEaXNwbGF5IjpmYWxzZSwibGF5b3V0IjoiIiwibnVtYmVyT2ZDdXN0b21JY29ucyI6MCwibmV0d29ya3MiOlt7InVybCI6Imh0dHA6Ly9saW5rZWRpbi5jb20vIiwib3JkZXIiOjAsImN1c3RvbSI6ZmFsc2UsImlkIjoibGlua2VkaW4iLCJpY29uX2NsYXNzIjoiIiwiaWNvbl9jdXN0b20iOiIiLCJkYXRhVmFsdWUiOnt9fSx7InVybCI6Imh0dHA6Ly9pbnN0YWdyYW0uY29tLyIsIm9yZGVyIjoxLCJjdXN0b20iOmZhbHNlLCJpZCI6Imluc3RhZ3JhbSIsImljb25fY2xhc3MiOiIiLCJpY29uX2N1c3RvbSI6IiIsImRhdGFWYWx1ZSI6e319LHsidXJsIjoiaHR0cHM6Ly90d2l0dGVyLmNvbS8iLCJvcmRlciI6MiwiY3VzdG9tIjpmYWxzZSwiaWQiOiJ0d2l0dGVyIiwiaWNvbl9jbGFzcyI6IiIsImljb25fY3VzdG9tIjoiIiwiZGF0YVZhbHVlIjp7fX0seyJ1cmwiOiJodHRwczovL2ZhY2Vib29rLmNvbS8iLCJvcmRlciI6MywiY3VzdG9tIjpmYWxzZSwiaWQiOiJmYWNlYm9vayIsImljb25fY2xhc3MiOiIiLCJpY29uX2N1c3RvbSI6IiIsImRhdGFWYWx1ZSI6e319XSwiaWNvbnNfc3R5bGUiOiI1IiwiYWxsX3BhZ2VzIjpmYWxzZX0=" id="1060369786" duda_id="1060369786">
                    </span>
                      </div>
                      <div class="dmRespCol empty-column large-6 medium-6 small-12" id="1760071587" duda_id="1760071587">
                        <div class="u_1900231882 dmform default" preserve_css="true" dmle_widget="dudaContactUsRespId" data-element-type="dContactUsRespId" captcha="true" data-require-captcha="true" data-captcha-position="bottomleft" id="1900231882" duda_id="1900231882" data-layout="layout-3">
                          <h3 class="dmform-title dmwidget-title" id="1436249131" duda_id="1436249131" hide="true">
                          </h3>
                          <div class="dmform-wrapper" preserve_css="true" id="1776352262" duda_id="1776352262">
                            <form method="post" class="dmRespDesignRow" locale="ENGLISH" id="1464574982" duda_id="1464574982">
                              <div class="dmforminput required  small-12 dmRespDesignCol medium-12 large-12" id="1484187177" duda_id="1484187177">
                                <label for="dmform-0" id="1432198341" duda_id="1432198341">
                                  Name:
                                </label>
                                <input type="text" class="" name="dmform-0" id="1636113297" duda_id="1636113297"/>
                                <input type="hidden" name="label-dmform-0" value="Name" id="1633466045" duda_id="1633466045"/>
                              </div>
                              <div class="dmforminput required  small-12 dmRespDesignCol medium-12 large-12" id="1909423398" duda_id="1909423398">
                                <label for="dmform-1" id="1877656125" duda_id="1877656125">
                                  Email:
                                </label>
                                <input type="email" class="" name="dmform-1" id="1324899019" duda_id="1324899019"/>
                                <input type="hidden" name="label-dmform-1" value="Email" id="1292029781" duda_id="1292029781"/>
                              </div>
                              <div class="dmforminput large-12 medium-12 dmRespDesignCol small-12" id="1464399135" duda_id="1464399135">
                                <label for="dmform-3" id="1846408751" duda_id="1846408751">
                                  Message:
                                </label>
                                <textarea name="dmform-3" id="1540237291" duda_id="1540237291">
                        </textarea>
                                <input type="hidden" name="label-dmform-3" value="Message" id="1510145214" duda_id="1510145214"/>
                              </div>
                              <span duda_id="undefined_clear" id="undefined_clear" class="dmWidgetClear" dmle_clear="true">
                       </span>
                              <div class="dmformsubmit dmWidget R" preserve_css="true" id="1750157822" duda_id="1750157822">
                                <input class="" name="submit" type="submit" value="SEND MESSAGE" id="1160607352" duda_id="1160607352"/>
                              </div>
                              <input name="dmformsendto" type="hidden" value="" preserve_css="true" id="1362334408" duda_id="1362334408"/>
                              <input class="dmActionInput" type="hidden" name="action" value="/_dm/s/rt/widgets/dmform.submit.jsp" id="1036150042" duda_id="1036150042"/>
                              <input name="dmformsubject" type="hidden" value="Form Message" preserve_css="true" id="1394386942" duda_id="1394386942"/>
                              <input name="dmformfrom" type="hidden" value="" preserve_css="true" id="1419869606" duda_id="1419869606"/>
                            </form>
                          </div>
                          <div class="dmform-success" style="display:none" preserve_css="true" id="1612999037" duda_id="1612999037">
                            Thank you for contacting us.
                            <br id="1649480364" duda_id="1649480364"/>
                            We will get back to you as soon as possible.
                          </div>
                          <div class="dmform-error" style="display:none" preserve_css="true" id="1331188479" duda_id="1331188479">
                            Oops, there was an error sending your message.
                            <br id="1360040034" duda_id="1360040034"/>
                            Please try again later.
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            </body>
            </html>
            
            
            """;

    @Benchmark
    @Fork(2)
    @Warmup(iterations = 3)
    @Timeout(time = 1000)
    @Measurement(iterations = 4)
    @BenchmarkMode(Mode.All)
    public void JMH() throws Exception {

        setBrowserVersion(BrowserVersion.CHROME);

        HtmlPage pageFromString = loadPage( html);

        assert !pageFromString.querySelector("h1").asNormalizedText().contains("Stella");
    }

}
