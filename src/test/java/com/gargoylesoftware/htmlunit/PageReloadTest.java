/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit;

import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for page reloading in various situations.
 *
 * @version $Revision$
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class PageReloadTest extends WebDriverTestCase {

    private static final String ANCHOR = "#anchor";
    private static final String ANCHOR2 = "#anchor2";
    private static final String PATHNAME = "/reload.html";
    private static final String PATHNAME2 = "/reload2.html";
    private static final String RELOAD_URL = "http://localhost:" + PORT + PATHNAME;
    private static final String RELOAD2_URL = "http://localhost:" + PORT + PATHNAME2;
    private static final String RELOAD_URL_ANCHOR = RELOAD_URL + ANCHOR;

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = PATHNAME, IE = "/")
    public void link_url_emptyUrl() throws Exception {
        openUrlAndClickById(RELOAD_URL, "linkEmpty", 1, getExpectedAlerts()[0], "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = "",
            IE10 = "#")
    public void link_url_emptyHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "linkEmptyHash", 0, PATHNAME, getExpectedAlerts()[0]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void link_url_hash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "linkHash", 0, PATHNAME, ANCHOR);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void link_url_differentHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "linkDifferentHash", 0, PATHNAME, ANCHOR2);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void link_url_url() throws Exception {
        openUrlAndClickById(RELOAD_URL, "linkUrl", 1, PATHNAME, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void link_url_urlHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "linkUrlHash", 0, PATHNAME, ANCHOR);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void link_url_differentUrl() throws Exception {
        openUrlAndClickById(RELOAD_URL, "linkDifferentUrl", 1, PATHNAME2, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void link_url_differentUrlHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "linkDifferentUrlHash", 1, PATHNAME2, ANCHOR);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void link_url_differentUrlDifferentHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "linkDifferentUrlDifferentHash", 1, PATHNAME2, ANCHOR2);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = PATHNAME, IE = "/")
    public void link_urlHash_emptyUrl() throws Exception {
        openUrlAndClickById("http://localhost:" + PORT + "/reload.html#anchor",
                "linkEmpty", 1, getExpectedAlerts()[0], "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void link_urlHash_hash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "linkHash", 0, PATHNAME, ANCHOR);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void link_urlHash_differentHash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "linkDifferentHash", 0, PATHNAME, ANCHOR2);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void link_urlHash_url() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "linkUrl", 1, PATHNAME, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void link_urlHash_urlHash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "linkUrlHash", 0, PATHNAME, ANCHOR);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void link_urlHash_differentUrl() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "linkDifferentUrl", 1, PATHNAME2, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void link_urlHash_differentUrlHash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "linkDifferentUrlHash", 1, PATHNAME2, ANCHOR);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void link_urlHash_differentUrlDifferentHash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "linkDifferentUrlDifferentHash", 1, PATHNAME2, ANCHOR2);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = PATHNAME, IE = "/")
    public void javascript_url_emptyUrl() throws Exception {
        openUrlAndClickById(RELOAD_URL, "javascriptEmpty", 1, getExpectedAlerts()[0], "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = "",
            IE10 = "#")
    public void javascript_url_emptyHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "javascriptEmptyHash", 0, PATHNAME, getExpectedAlerts()[0]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void javascript_url_hash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "javascriptHash", 0, PATHNAME, ANCHOR);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void javascript_url_differentHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "javascriptDifferentHash", 0, PATHNAME, ANCHOR2);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void javascript_url_url() throws Exception {
        openUrlAndClickById(RELOAD_URL, "javascriptUrl", 1, PATHNAME, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void javascript_url_urlHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "javascriptUrlHash", 0, PATHNAME, ANCHOR);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void javascript_url_differentUrl() throws Exception {
        openUrlAndClickById(RELOAD_URL, "javascriptDifferentUrl", 1, PATHNAME2, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void javascript_url_differentUrlHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "javascriptDifferentUrlHash", 1, PATHNAME2, ANCHOR);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void javascript_url_differentUrlDifferentHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "javascriptDifferentUrlDifferentHash", 1, PATHNAME2, ANCHOR2);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = PATHNAME, IE = "/")
    public void javascript_urlHash_emptyUrl() throws Exception {
        openUrlAndClickById("http://localhost:" + PORT + "/reload.html#anchor",
                "javascriptEmpty", 1, getExpectedAlerts()[0], "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void javascript_urlHash_hash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "javascriptHash", 0, PATHNAME, ANCHOR);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void javascript_urlHash_differentHash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "javascriptDifferentHash", 0, PATHNAME, ANCHOR2);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void javascript_urlHash_url() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "javascriptUrl", 1, PATHNAME, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void javascript_urlHash_urlHash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "javascriptUrlHash", 0, PATHNAME, ANCHOR);
    }

    /**
     * @exception Exception If the test fails
     */
    @Alerts(DEFAULT = "", IE = ANCHOR)
    public void javascript_urlHash_differentUrl() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "javascriptDifferentUrl", 1, PATHNAME2, getExpectedAlerts()[0]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void submitGet_url_emptyUrl() throws Exception {
        openUrlAndClickById(RELOAD_URL, "submitGetEmpty", 1, PATHNAME, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = "0", IE10 = "1")
    public void submitGet_url_emptyHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "submitGetEmptyHash", Integer.parseInt(getExpectedAlerts()[0]), PATHNAME, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", ANCHOR }, IE = {"1", "" })
    public void submitGet_url_hash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "submitGetHash", Integer.parseInt(getExpectedAlerts()[0]),
                PATHNAME, getExpectedAlerts()[1]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", ANCHOR2 }, IE = {"1", "" })
    public void submitGet_url_differentHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "submitGetDifferentHash", Integer.parseInt(getExpectedAlerts()[0]),
                PATHNAME, getExpectedAlerts()[1]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void submitGet_url_url() throws Exception {
        openUrlAndClickById(RELOAD_URL, "submitGetUrl", 1, PATHNAME, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", ANCHOR }, IE = {"1", "" })
    public void submitGet_url_urlHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "submitGetUrlHash", Integer.parseInt(getExpectedAlerts()[0]),
                PATHNAME, getExpectedAlerts()[1]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void submitGet_url_differentUrl() throws Exception {
        openUrlAndClickById(RELOAD_URL, "submitGetDifferentUrl", 1, PATHNAME2, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = ANCHOR, IE = "")
    public void submitGet_url_differentUrlHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "submitGetDifferentUrlHash", 1,
                PATHNAME2, getExpectedAlerts()[0]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = ANCHOR2, IE = "")
    public void submitGet_url_differentUrlDifferentHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "submitGetDifferentUrlDifferentHash", 1, PATHNAME2, getExpectedAlerts()[0]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", ANCHOR }, IE = {"1", "" })
    public void submitGet_urlHash_emptyUrl() throws Exception {
        openUrlAndClickById("http://localhost:" + PORT + "/reload.html#anchor", "submitGetEmpty",
                Integer.parseInt(getExpectedAlerts()[0]),
                PATHNAME, getExpectedAlerts()[1]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", ANCHOR }, IE = {"1", "" })
    public void submitGet_urlHash_hash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "submitGetHash", Integer.parseInt(getExpectedAlerts()[0]),
                PATHNAME, getExpectedAlerts()[1]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", ANCHOR2 }, IE = {"1", "" })
    public void submitGet_urlHash_differentHash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "submitGetDifferentHash", Integer.parseInt(getExpectedAlerts()[0]),
                PATHNAME, getExpectedAlerts()[1]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void submitGet_urlHash_url() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "submitGetUrl", 1, PATHNAME, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", ANCHOR }, IE = {"1", "" })
    public void submitGet_urlHash_urlHash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "submitGetUrlHash", Integer.parseInt(getExpectedAlerts()[0]),
                PATHNAME, getExpectedAlerts()[1]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void submitGet_urlHash_differentUrl() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "submitGetDifferentUrl", 1, PATHNAME2, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = ANCHOR, IE = "")
    public void submitGet_urlHash_differentUrlHash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "submitGetDifferentUrlHash", 1, PATHNAME2, getExpectedAlerts()[0]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = ANCHOR2, IE = "")
    public void submitGet_urlHash_differentUrlDifferentHash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "submitGetDifferentUrlDifferentHash", 1,
                PATHNAME2, getExpectedAlerts()[0]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void submitPost_url_emptyUrl() throws Exception {
        openUrlAndClickById(RELOAD_URL, "submitPostEmpty", 1, PATHNAME, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = "",
            IE10 = "#")
    public void submitPost_url_emptyHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "submitPostEmptyHash", 1, PATHNAME, getExpectedAlerts()[0]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void submitPost_url_hash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "submitPostHash", 1, PATHNAME, ANCHOR);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void submitPost_url_differentHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "submitPostDifferentHash", 1, PATHNAME, ANCHOR2);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void submitPost_url_url() throws Exception {
        openUrlAndClickById(RELOAD_URL, "submitPostUrl", 1, PATHNAME, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void submitPost_url_urlHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "submitPostUrlHash", 1, PATHNAME, ANCHOR);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void submitPost_url_differentUrl() throws Exception {
        openUrlAndClickById(RELOAD_URL, "submitPostDifferentUrl", 1, PATHNAME2, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void submitPost_url_differentUrlHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "submitPostDifferentUrlHash", 1, PATHNAME2, ANCHOR);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void submitPost_url_differentUrlDifferentHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "submitPostDifferentUrlDifferentHash", 1, PATHNAME2, ANCHOR2);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = ANCHOR, IE = "")
    public void submitPost_urlHash_emptyUrl() throws Exception {
        openUrlAndClickById("http://localhost:" + PORT + "/reload.html#anchor",
                "submitPostEmpty", 1, PATHNAME, getExpectedAlerts()[0]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void submitPost_urlHash_hash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "submitPostHash", 1, PATHNAME, ANCHOR);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void submitPost_urlHash_differentHash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "submitPostDifferentHash", 1, PATHNAME, ANCHOR2);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void submitPost_urlHash_url() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "submitPostUrl", 1, PATHNAME, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void submitPost_urlHash_urlHash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "submitPostUrlHash", 1, PATHNAME, ANCHOR);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void submitPost_urlHash_differentUrl() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "submitPostDifferentUrl", 1, PATHNAME2, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void submitPost_urlHash_differentUrlHash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "submitPostDifferentUrlHash", 1, PATHNAME2, ANCHOR);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void submitPost_urlHash_differentUrlDifferentHash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "submitPostDifferentUrlDifferentHash", 1, PATHNAME2, ANCHOR2);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void jsSubmitGet_url_emptyUrl() throws Exception {
        openUrlAndClickById(RELOAD_URL, "jsSubmitGetEmpty", 1, PATHNAME, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            IE10 = "1")
    public void jsSubmitGet_url_emptyHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "jsSubmitGetEmptyHash", Integer.parseInt(getExpectedAlerts()[0]), PATHNAME, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", ANCHOR }, IE = { "1", "" })
    public void jsSubmitGet_url_hash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "jsSubmitGetHash", Integer.parseInt(getExpectedAlerts()[0]),
                    PATHNAME, getExpectedAlerts()[1]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", ANCHOR2 }, IE = { "1", "" })
    public void jsSubmitGet_url_differentHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "jsSubmitGetDifferentHash", Integer.parseInt(getExpectedAlerts()[0]),
                PATHNAME, getExpectedAlerts()[1]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void jsSubmitGet_url_url() throws Exception {
        openUrlAndClickById(RELOAD_URL, "jsSubmitGetUrl", 1, PATHNAME, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", ANCHOR }, IE = { "1", "" })
    public void jsSubmitGet_url_urlHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "jsSubmitGetUrlHash", Integer.parseInt(getExpectedAlerts()[0]),
                PATHNAME, getExpectedAlerts()[1]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void jsSubmitGet_url_differentUrl() throws Exception {
        openUrlAndClickById(RELOAD_URL, "jsSubmitGetDifferentUrl", 1, PATHNAME2, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = ANCHOR, IE = "")
    public void jsSubmitGet_url_differentUrlHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "jsSubmitGetDifferentUrlHash", 1, PATHNAME2, getExpectedAlerts()[0]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = ANCHOR2, IE = "")
    public void jsSubmitGet_url_differentUrlDifferentHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "jsSubmitGetDifferentUrlDifferentHash", 1, PATHNAME2, getExpectedAlerts()[0]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", ANCHOR }, IE = { "1", "" })
    public void jsSubmitGet_urlHash_emptyUrl() throws Exception {
        openUrlAndClickById("http://localhost:" + PORT + "/reload.html#anchor",
               "jsSubmitGetEmpty", Integer.parseInt(getExpectedAlerts()[0]), PATHNAME, getExpectedAlerts()[1]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", ANCHOR }, IE = { "1", "" })
    public void jsSubmitGet_urlHash_hash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "jsSubmitGetHash", Integer.parseInt(getExpectedAlerts()[0]),
                PATHNAME, getExpectedAlerts()[1]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", ANCHOR2 }, IE = { "1", "" })
    public void jsSubmitGet_urlHash_differentHash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "jsSubmitGetDifferentHash",
                Integer.parseInt(getExpectedAlerts()[0]), PATHNAME, getExpectedAlerts()[1]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void jsSubmitGet_urlHash_url() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "jsSubmitGetUrl", 1, PATHNAME, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = { "0", ANCHOR }, IE = { "1", "" })
    public void jsSubmitGet_urlHash_urlHash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "jsSubmitGetUrlHash", Integer.parseInt(getExpectedAlerts()[0]),
                PATHNAME, getExpectedAlerts()[1]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void jsSubmitGet_urlHash_differentUrl() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "jsSubmitGetDifferentUrl", 1, PATHNAME2, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = ANCHOR, IE = "")
    public void jsSubmitGet_urlHash_differentUrlHash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "jsSubmitGetDifferentUrlHash", 1, PATHNAME2, getExpectedAlerts()[0]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = ANCHOR2, IE = "")
    public void jsSubmitGet_urlHash_differentUrlDifferentHash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "jsSubmitGetDifferentUrlDifferentHash",
                1, PATHNAME2, getExpectedAlerts()[0]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void jsSubmitPost_url_emptyUrl() throws Exception {
        openUrlAndClickById(RELOAD_URL, "jsSubmitPostEmpty", 1, PATHNAME, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = "",
            IE10 = "#")
    public void jsSubmitPost_url_EmptyHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "jsSubmitPostEmptyHash", 1, PATHNAME, getExpectedAlerts()[0]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void jsSubmitPost_url_hash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "jsSubmitPostHash", 1, PATHNAME, ANCHOR);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void jsSubmitPost_url_differentHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "jsSubmitPostDifferentHash", 1, PATHNAME, ANCHOR2);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void jsSubmitPost_url_url() throws Exception {
        openUrlAndClickById(RELOAD_URL, "jsSubmitPostUrl", 1, PATHNAME, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void jsSubmitPost_url_urlHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "jsSubmitPostUrlHash", 1, PATHNAME, ANCHOR);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void jsSubmitPost_url_differentUrl() throws Exception {
        openUrlAndClickById(RELOAD_URL, "jsSubmitPostDifferentUrl", 1, PATHNAME2, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void jsSubmitPost_url_differentUrlHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "jsSubmitPostDifferentUrlHash", 1, PATHNAME2, ANCHOR);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void jsSubmitPost_url_differentUrlDifferentHash() throws Exception {
        openUrlAndClickById(RELOAD_URL, "jsSubmitPostDifferentUrlDifferentHash", 1, PATHNAME2, ANCHOR2);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    @Alerts(DEFAULT = ANCHOR, IE = "")
    public void jsSubmitPost_urlHash_emptyUrl() throws Exception {
        openUrlAndClickById("http://localhost:" + PORT + "/reload.html#anchor",
                "jsSubmitPostEmpty", 1, PATHNAME, getExpectedAlerts()[0]);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void jsSubmitPost_urlHash_hash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "jsSubmitPostHash", 1, PATHNAME, ANCHOR);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void jsSubmitPost_urlHash_differentHash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "jsSubmitPostDifferentHash", 1, PATHNAME, ANCHOR2);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void jsSubmitPost_urlHash_url() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "jsSubmitPostUrl", 1, PATHNAME, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void jsSubmitPost_urlHash_urlHash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "jsSubmitPostUrlHash", 1, PATHNAME, ANCHOR);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void jsSubmitPost_urlHash_differentUrl() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "jsSubmitPostDifferentUrl", 1, PATHNAME2, "");
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void jsSubmitPost_urlHash_differentUrlHash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "jsSubmitPostDifferentUrlHash", 1, PATHNAME2, ANCHOR);
    }

    /**
     * @exception Exception If the test fails
     */
    @Test
    public void jsSubmitPost_urlHash_differentUrlDifferentHash() throws Exception {
        openUrlAndClickById(RELOAD_URL_ANCHOR, "jsSubmitPostDifferentUrlDifferentHash", 1, PATHNAME2, ANCHOR2);
    }

    private void openUrlAndClickById(
            final String url,
            final String id,
            final int counterChange,
            final String expectedPathname,
            final String expectedHash) throws Exception {

        final String html = testPage();

        getMockWebConnection().setDefaultResponse(testResponse());
        getMockWebConnection().setResponse(new URL(RELOAD_URL), html);
        getMockWebConnection().setResponse(new URL(RELOAD2_URL), html);

        final WebDriver driver = loadPage2(html, new URL(url));
        Assert.assertEquals(getMockWebConnection().getRequestCount(), 1);

        // click
        driver.findElement(By.id(id)).click();
        Assert.assertEquals(getMockWebConnection().getRequestCount(), counterChange + 1);

        // check location visible to javascript
        driver.findElement(By.id("updateLocationInfo")).click();
        final String hash = driver.findElement(By.id("locationHash")).getText();
        final String pathname = driver.findElement(By.id("locationPathname")).getText();

        Assert.assertEquals(expectedPathname, pathname);
        Assert.assertEquals(expectedHash, hash);
    }

    private String testPage() {
        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n"
                + "<html>\n"
                + "  <head>\n"
                + "    <META HTTP-EQUIV='Pragma' CONTENT='text/html;charset=UTF-8'>\n"
                + "    <META HTTP-EQUIV='Pragma' CONTENT='no-cache'>\n"
                + "    <META HTTP-EQUIV='CACHE-CONTROL' CONTENT='NO-CACHE'>\n"
                + "  </head>\n"

                + "<body>\n"

                + "  <div id='locationPathname'></div>\n"
                + "  <div id='locationHash'></div>\n"
                + "  <input type='button' id='updateLocationInfo' value='updateLocationInfo' "
                + "onclick='document.getElementById(\"locationHash\").innerHTML=location.hash;"
                + "document.getElementById(\"locationPathname\").innerHTML=location.pathname;'>\n"

                + "  <a id='linkEmpty' href=''>linkEmpty</a>\n"
                + "  <a id='linkEmptyHash' href='#'>linkEmptyHash</a>\n"
                + "  <a id='linkHash' href='#anchor'>linkHash</a>\n"
                + "  <a id='linkDifferentHash' href='#anchor2'>linkDifferentHash</a>\n"
                + "  <a id='linkUrl' href='" + RELOAD_URL + "'>linkUrl</a>\n"
                + "  <a id='linkUrlHash' href='" + RELOAD_URL_ANCHOR + "'>linkUrlHash</a>\n"
                + "  <a id='linkDifferentUrl' href='" + "http://localhost:" + PORT + "/reload2.html"
                + "'>linkDifferentUrl</a>\n"
                + "  <a id='linkDifferentUrlHash' href='" + "http://localhost:" + PORT + "/reload2.html"
                + ANCHOR + "'>linkDifferentUrlHash</a>\n"
                + "  <a id='linkDifferentUrlDifferentHash' href='" + "http://localhost:" + PORT
                + "/reload2.html#anchor2" + "'>linkDifferentUrlDifferentHash</a>\n"

                + "  <form action='' method='GET'>\n"
                + "    <input type='button' id='javascriptEmpty' value='javascriptEmpty'"
                + " onclick='location.href=\"\"'>\n"
                + "    <input type='button' id='javascriptEmptyHash' value='javascriptHash'"
                + " onclick='location.href=\"#\"'>\n"
                + "    <input type='button' id='javascriptHash' value='javascriptHash'"
                + " onclick='location.href=\"#anchor\"'>\n"
                + "    <input type='button' id='javascriptDifferentHash' value='javascriptDifferentHash'"
                + " onclick='location.href=\"#anchor2\"'>\n"
                + "    <input type='button' id='javascriptUrl' value='javascriptUrl'"
                + " onclick='location.href=\"" + RELOAD_URL + "\"'>\n"
                + "    <input type='button' id='javascriptUrlHash' value='javascriptUrlHash'"
                + " onclick='location.href=\"" + RELOAD_URL_ANCHOR + "\"'>\n"
                + "    <input type='button' id='javascriptDifferentUrl' value='javascriptDifferentUrl'"
                + " onclick='location.href=\"" + "http://localhost:" + PORT + "/reload2.html" + "\"'>\n"
                + "    <input type='button' id='javascriptDifferentUrlHash' value='javascriptDifferentUrlHash'"
                + " onclick='location.href=\"" + "http://localhost:" + PORT + "/reload2.html" + ANCHOR + "\"'>\n"
                + "    <input type='button' id='javascriptDifferentUrlDifferentHash'"
                + " value='javascriptDifferentUrlDifferentHash'"
                + " onclick='location.href=\"" + "http://localhost:" + PORT + "/reload2.html#anchor2" + "\"'>\n"
                + "  </form>\n"

                + "  <form action='' method='GET'>\n"
                + "    <input type='submit' id='submitGetEmpty' value='submitGetEmpty' >\n"
                + "    <input type='BUTTON' id='jsSubmitGetEmpty' value='jsSubmitGetEmpty'  onclick='submit();'>\n"
                + "  </form>\n"
                + "  <form action='#' method='GET'>\n"
                + "    <input type='submit' id='submitGetEmptyHash' value='submitGetEmptyHash'/>\n"
                + "    <input type='button' id='jsSubmitGetEmptyHash' value='jsSubmitGetEmptyHash'"
                + " onclick='submit();'>\n"
                + "  </form>\n"
                + "  <form action='#anchor' method='GET'>\n"
                + "    <input type='submit' id='submitGetHash' value='submitGetHash'/>\n"
                + "    <input type='button' id='jsSubmitGetHash' value='jsSubmitGetHash' onclick='submit();'>\n"
                + "  </form>\n"
                + "  <form action='#anchor2' method='GET'>\n"
                + "    <input type='submit' id='submitGetDifferentHash' value='submitGetDifferentHash'>\n"
                + "    <input type='button' id='jsSubmitGetDifferentHash' value='jsSubmitGetDifferentHash'"
                + " onclick='submit();'>\n"
                + "  </form>\n"
                + "  <form action='" + RELOAD_URL + "' method='GET'>\n"
                + "    <input type='submit' id='submitGetUrl' value='submitGetUrl'>\n"
                + "    <input type='button' id='jsSubmitGetUrl' value='jsSubmitGetUrl' onclick='submit();'>\n"
                + "  </form>\n"
                + "  <form action='" + RELOAD_URL_ANCHOR + "' method='GET'>\n"
                + "    <input type='submit' id='submitGetUrlHash' value='submitGetUrlHash'>\n"
                + "    <input type='button' id='jsSubmitGetUrlHash' value='jsSubmitGetUrlHash' onclick='submit();'>\n"
                + "  </form>\n"
                + "  <form action='" + "http://localhost:" + PORT + "/reload2.html" + "' method='GET'>\n"
                + "    <input type='submit' id='submitGetDifferentUrl' value='submitGetDifferentUrl'>\n"
                + "    <input type='button' id='jsSubmitGetDifferentUrl' value='jsSubmitGetDifferentUrl'"
                + " onclick='submit();'>\n"
                + "  </form>\n"
                + "  <form action='" + "http://localhost:" + PORT + "/reload2.html" + ANCHOR + "' method='GET'>\n"
                + "    <input type='submit' id='submitGetDifferentUrlHash' value='submitGetDifferentUrlHash'>\n"
                + "    <input type='button' id='jsSubmitGetDifferentUrlHash' value='jsSubmitGetDifferentUrlHash'"
                + " onclick='submit();'>\n"
                + "  </form>\n"
                + "  <form action='" + "http://localhost:" + PORT + "/reload2.html#anchor2" + "' method='GET'>\n"
                + "    <input type='submit' id='submitGetDifferentUrlDifferentHash'"
                + " value='submitGetDifferentUrlDifferentHash'>\n"
                + "    <input type='button' id='jsSubmitGetDifferentUrlDifferentHash'"
                + " value='jsSubmitGetDifferentUrlDifferentHash' onclick='submit();'>\n"
                + "  </form>\n"

                + "  <form action='' method='POST'>\n"
                + "    <input type='submit' id='submitPostEmpty' value='submitPostEmpty'>\n"
                + "    <input type='BUTTON' id='jsSubmitPostEmpty' value='jsSubmitPostEmpty'  onclick='submit();'>\n"
                + "  </form>\n"
                + "  <form action='#' method='POST'>\n"
                + "    <input type='submit' id='submitPostEmptyHash' value='submitPostEmptyHash'>\n"
                + "    <input type='button' id='jsSubmitPostEmptyHash' value='jsSubmitPostEmptyHash'"
                + "onclick='submit();'>\n"
                + "  </form>\n"
                + "  <form action='#anchor' method='POST'>\n"
                + "    <input type='submit' id='submitPostHash' value='submitPostHash'>\n"
                + "    <input type='button' id='jsSubmitPostHash' value='jsSubmitPostHash' onclick='submit();'>\n"
                + "  </form>\n"
                + "  <form action='#anchor2' method='POST'>\n"
                + "    <input type='submit' id='submitPostDifferentHash' value='submitPostDifferentHash'>\n"
                + "    <input type='button' id='jsSubmitPostDifferentHash' value='jsSubmitPostDifferentHash'"
                + " onclick='submit();'>\n"
                + "  </form>\n"
                + "  <form action='" + RELOAD_URL + "' method='POST'>\n"
                + "    <input type='submit' id='submitPostUrl' value='submitPostUrl'>\n"
                + "    <input type='button' id='jsSubmitPostUrl' value='jsSubmitPostUrl' onclick='submit();'>\n"
                + "  </form>\n"
                + "  <form action='" + RELOAD_URL_ANCHOR + "' method='POST'>\n"
                + "    <input type='submit' id='submitPostUrlHash' value='submitPostUrlHash'>\n"
                + "    <input type='button' id='jsSubmitPostUrlHash' value='jsSubmitPostUrlHash' onclick='submit();'>\n"
                + "  </form>\n"
                + "  <form action='" + "http://localhost:" + PORT + "/reload2.html" + "' method='POST'>\n"
                + "    <input type='submit' id='submitPostDifferentUrl' value='submitPostDifferentUrl'>\n"
                + "    <input type='button' id='jsSubmitPostDifferentUrl' value='jsSubmitPostDifferentUrl'"
                + " onclick='submit();'>\n"
                + "  </form>\n"
                + "  <form action='" + "http://localhost:" + PORT + "/reload2.html" + ANCHOR + "' method='POST'>\n"
                + "    <input type='submit' id='submitPostDifferentUrlHash' value='submitPostDifferentUrlHash'>\n"
                + "    <input type='button' id='jsSubmitPostDifferentUrlHash'"
                + " value='jsSubmitPostDifferentUrlHash' onclick='submit();'>\n"
                + "  </form>\n"
                + "  <form action='" + "http://localhost:" + PORT + "/reload2.html#anchor2" + "' method='POST'>\n"
                + "    <input type='submit' id='submitPostDifferentUrlDifferentHash'"
                + " value='submitPostDifferentUrlDifferentHash'>\n"
                + "    <input type='button' id='jsSubmitPostDifferentUrlDifferentHash'"
                + " value='jsSubmitPostDifferentUrlDifferentHash' onclick='submit();'>\n"
                + "  </form>\n"

                + "</body>\n"
                + "</html>";
    }

    private String testResponse() {
        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n"
                + "<html>\n"
                + "  <head>\n"
                + "    <META HTTP-EQUIV='Pragma' CONTENT='no-cache'>\n"
                + "    <META HTTP-EQUIV='CACHE-CONTROL' CONTENT='NO-CACHE'>\n"
                + "  </head>\n"

                + "<body>\n"

                + "  <div id='locationPathname'></div>\n"
                + "  <div id='locationHash'></div>\n"
                + "  <input type='button' id='updateLocationInfo' value='updateLocationInfo' "
                + "onclick='document.getElementById(\"locationHash\").innerHTML=location.hash;"
                + "document.getElementById(\"locationPathname\").innerHTML=location.pathname;'>\n"

                + "</body>\n"
                + "</html>";
    }
}
