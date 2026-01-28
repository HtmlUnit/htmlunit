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
package org.htmlunit.libraries.gwt;

import java.util.List;

import org.eclipse.jetty.server.Server;
import org.htmlunit.util.JettyServerUtils;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Tests for version 2.5.0 of <a href="https://www.gwtproject.org/">GWT Project</a>.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class GWTTest2x5x0 extends GWTTest {

    @Override
    public String getDirectory() {
        return "2.5.0";
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void hello() throws Exception {
        final String url = URL_FIRST + "Hello/Hello.html";

        final WebDriver driver = loadGWTPage(url, "//button");

        final WebElement button = driver.findElement(By.xpath("//button"));
        assertEquals("Click me", button.getText());

        button.click();
        assertEquals("Hello, AJAX", driver.switchTo().alert().getText());
        driver.switchTo().alert().dismiss();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void mail() throws Exception {
        final String url = URL_FIRST + "Mail/Mail.html";

        final WebDriver driver = loadGWTPage(url, "//div[@class='MGJ']");

        final WebElement cell = driver.findElement(By.xpath("//div[@class='MGJ']"));
        assertTrue(cell.getText(), cell.getText().startsWith("Welcome back, foo@example.com"));

        final List<WebElement> cells = driver.findElements(By.xpath("//tr[@class='MKI']/td"));
        assertEquals(3, cells.size());
        assertEquals("markboland05", cells.get(0).getText());
        assertEquals("mark@example.com", cells.get(1).getText());
        assertEquals("URGENT -[Mon, 24 Apr 2006 02:17:27 +0000]", cells.get(2).getText());

        verifyStartMailBody(driver,
                "Dear Friend,",
                "",
                "I am Mr. Mark Boland the Bank Manager of ABN AMRO BANK 101 Moorgate, London, EC2M 6SB.");

        // click on email from Hollie Voss
        final WebElement toClick = driver.findElement(By.xpath("//td[text() = 'Hollie Voss']"));
        assertEquals("Hollie Voss", toClick.getText());

        toClick.click();
        verifyStartMailBody(driver,
                ">> Componentes e decodificadores; confira aqui;",
                "http://br.geocities.com/listajohn/index.htm",
                "THE GOVERNING AWARD");
    }

    private static void verifyStartMailBody(final WebDriver driver, final String... details) {
        final WebElement mail = driver.findElement(By.xpath("//div[@class='MGI']"));
        final String text = mail.getText();
        final String[] lines = text.split("\\R");

        for (int i = 0; i < details.length; i++) {
            assertEquals(details[i], lines[i]);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void json() throws Exception {
        final String url = URL_FIRST + "JSON/JSON.html";

        final WebDriver driver = loadGWTPage(url, "//button");

        final WebElement button = driver.findElement(By.xpath("//button"));
        assertEquals("Search", button.getText());

        button.click();

        textToBePresentInElementLocated(driver,
                By.xpath("//div[@class='JSON-JSONResponseObject']/div/div/table//td[2]/div/span"),
                "ResultSet");
    }

    /**
     * @throws Exception if an error occurs
     */
    // this is a blinker with HtmlUnit at the moment
    // @Test
    public void showcase() throws Exception {
        final String url = URL_FIRST + "Showcase/Showcase.html";

        final WebDriver driver = loadGWTPage(url, "id('gwt-debug-cwCheckBox-Monday-label')");

        WebElement elem = driver.findElement(By.id("gwt-debug-cwCheckBox-Monday-label"));
        assertEquals("Monday", elem.getText());

        elem = driver.findElement(By.id("gwt-debug-cwCheckBox-Tuesday-label"));
        assertEquals("Tuesday", elem.getText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dynaTable() throws Exception {
        stopWebServers();

        final Server server = JettyServerUtils.startWebAppServer(PORT,
                            "src/test/resources/libraries/GWT/" + getDirectory() + "/DynaTable",
                            new String[] {"src/test/resources/libraries/GWT/" + getDirectory() + "/gwt-servlet.jar"});
        try {
            final String url = URL_FIRST + "DynaTable.html";
            final WebDriver driver = loadGWTPage(url,
                    "/html/body/table/tbody/tr/td[1]/table/tbody/tr[2]/td/table/tbody/tr[2]/td[2]");

            final List<WebElement> cells = driver.findElements(By.xpath("//table[@class='table']//tr[2]/td"));
            assertEquals(3, cells.size());

            textToBePresentInElement(driver, cells.get(0), "Inman Mendez");
            assertEquals("Majoring in Phrenology", cells.get(1).getText());
            assertEquals("Mon 9:45-10:35, Tues 2:15-3:05, Fri 8:45-9:35, Fri 9:45-10:35", cells.get(2).getText());
            stopWebServers();
        }
        finally {
            JettyServerUtils.stopServer(server);
        }
    }
}
