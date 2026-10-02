// Licensed to the Software Freedom Conservancy (SFC) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The SFC licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.htmlunit.selenium;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

class VisibilityTest extends SeleniumTest {

    @Test
    void testShouldAllowTheUserToTellIfAnElementIsDisplayedOrNot() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        assertThat(driver.findElement(By.id("displayed")).isDisplayed()).isTrue();
        assertThat(driver.findElement(By.id("none")).isDisplayed()).isFalse();
        assertThat(driver.findElement(By.id("suppressedParagraph")).isDisplayed()).isFalse();
        assertThat(driver.findElement(By.id("hidden")).isDisplayed()).isFalse();
    }

    @Test
    void testVisibilityShouldTakeIntoAccountParentVisibility() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        WebElement childDiv = driver.findElement(By.id("hiddenchild"));
        WebElement hiddenLink = driver.findElement(By.id("hiddenlink"));

        assertThat(childDiv.isDisplayed()).isFalse();
        assertThat(hiddenLink.isDisplayed()).isFalse();
    }

    @Test
    void testShouldCountElementsAsVisibleIfStylePropertyHasBeenSet() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        WebElement shown = driver.findElement(By.id("visibleSubElement"));

        assertThat(shown.isDisplayed()).isTrue();
    }

    @Test
    public void testShouldModifyTheVisibilityOfAnElementDynamically() throws InterruptedException {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        WebElement element = driver.findElement(By.id("hideMe"));

        assertThat(element.isDisplayed()).isTrue();

        element.click();

        // wait.until(not(visibilityOf(element)));
        sleepRealBrowser(400);

        assertThat(element.isDisplayed()).isFalse();
    }

    @Test
    void testHiddenInputElementsAreNeverVisible() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        WebElement shown = driver.findElement(By.name("hidden"));

        assertThat(shown.isDisplayed()).isFalse();
    }

    @Test
    void testShouldNotBeAbleToClickOnAnElementThatIsNotDisplayed() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");
        WebElement element = driver.findElement(By.id("unclickable"));

        assertThatExceptionOfType(ElementNotInteractableException.class).isThrownBy(element::click);
    }

    @Test
    void testShouldNotBeAbleToTypeToAnElementThatIsNotDisplayed() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");
        WebElement element = driver.findElement(By.id("unclickable"));

        assertThatExceptionOfType(ElementNotInteractableException.class)
                .isThrownBy(() -> element.sendKeys("You don't see me"));
        assertThat(element.getAttribute("value")).isNotEqualTo("You don't see me");
    }

    @Test
    public void testZeroSizedDivIsShownIfDescendantHasSize() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        WebElement element = driver.findElement(By.id("zero"));
        Dimension size = element.getSize();

        assertThat(size.width).isZero();
        assertThat(size.height).isZero();
        assertThat(element.isDisplayed()).isTrue();
    }

    @Test
    void parentNodeVisibleWhenAllChildrenAreAbsolutelyPositionedAndOverflowIsHidden() {
        final WebDriver driver = getWebDriver("/visibility-css.html");

        WebElement element = driver.findElement(By.id("suggest"));
        assertThat(element.isDisplayed()).isTrue();
    }
    //
    //    @Test
    //    public void testElementHiddenByOverflowXIsNotVisible() {
    //        String[] pages = new String[] {
    //                "/overflow/x_hidden_y_hidden.html",
    //                "/overflow/x_hidden_y_scroll.html",
    //                "/overflow/x_hidden_y_auto.html", };
    //        for (String page : pages) {
    //            final WebDriver driver = getWebDriver(page);
    //
    //            WebElement right = driver.findElement(By.id("right"));
    //            assertThat(right.isDisplayed()).as("On page %s", page).isFalse();
    //            WebElement bottomRight = driver.findElement(By.id("bottom-right"));
    //            assertThat(bottomRight.isDisplayed()).as("On page %s", page).isFalse();
    //        }
    //    }
    //
    //    @Test
    //    public void testElementHiddenByOverflowYIsNotVisible() {
    //        String[] pages = new String[] {
    //                "/overflow/x_hidden_y_hidden.html",
    //                "/overflow/x_scroll_y_hidden.html",
    //                "/overflow/x_auto_y_hidden.html", };
    //        for (String page : pages) {
    //            final WebDriver driver = getWebDriver(page);
    //
    //            WebElement bottom = driver.findElement(By.id("bottom"));
    //            assertThat(bottom.isDisplayed()).as("On page %s", page).isFalse();
    //            WebElement bottomRight = driver.findElement(By.id("bottom-right"));
    //            assertThat(bottomRight.isDisplayed()).as("On page %s", page).isFalse();
    //        }
    //    }

    @Test
    void testElementScrollableByOverflowXIsVisible() {
        String[] pages = new String[] {
                "/overflow/x_scroll_y_hidden.html",
                "/overflow/x_scroll_y_scroll.html",
                "/overflow/x_scroll_y_auto.html",
                "/overflow/x_auto_y_hidden.html",
                "/overflow/x_auto_y_scroll.html",
                "/overflow/x_auto_y_auto.html", };
        for (String page : pages) {
            final WebDriver driver = getWebDriver(page);

            WebElement right = driver.findElement(By.id("right"));
            assertThat(right.isDisplayed()).as("On page %s", page).isTrue();
        }
    }

    @Test
    void testElementScrollableByOverflowYIsVisible() {
        String[] pages = new String[] {
                "/overflow/x_hidden_y_scroll.html",
                "/overflow/x_scroll_y_scroll.html",
                "/overflow/x_auto_y_scroll.html",
                "/overflow/x_hidden_y_auto.html",
                "/overflow/x_scroll_y_auto.html",
                "/overflow/x_auto_y_auto.html", };
        for (String page : pages) {
            final WebDriver driver = getWebDriver(page);

            WebElement bottom = driver.findElement(By.id("bottom"));
            assertThat(bottom.isDisplayed()).as("On page %s", page).isTrue();
        }
    }

    @Test
    void testElementScrollableByOverflowXAndYIsVisible() {
        String[] pages = new String[] {
                "/overflow/x_scroll_y_scroll.html",
                "/overflow/x_scroll_y_auto.html",
                "/overflow/x_auto_y_scroll.html",
                "/overflow/x_auto_y_auto.html", };
        for (String page : pages) {
            final WebDriver driver = getWebDriver(page);

            WebElement bottomRight = driver.findElement(By.id("bottom-right"));
            assertThat(bottomRight.isDisplayed()).as("On page %s", page).isTrue();
        }
    }

    //    @Test
    //    void tooSmallAWindowWithOverflowHiddenIsNotAProblem() {
    //        // Browser window cannot be resized on ANDROID (and most mobile platforms
    //        // though others aren't defined in org.openqa.selenium.Platform).
    //        assumeFalse(TestUtilities.getEffectivePlatform(driver).is(ANDROID));
    //        WebDriver.Window window = driver.manage().window();
    //        Dimension originalSize = window.getSize();
    //
    //        try {
    //            // Short in the Y dimension
    //            window.setSize(new Dimension(1024, 500));
    //
    //            final WebDriver driver = getWebDriver("/overflow-body.html");
    //
    //            WebElement element = driver.findElement(By.name("resultsFrame"));
    //            assertThat(element.isDisplayed()).isTrue();
    //        } finally {
    //            window.setSize(originalSize);
    //        }
    //    }

    @Test
    void shouldShowElementNotVisibleWithHiddenAttribute() {
        final WebDriver driver = getWebDriver("/hidden.html");

        WebElement element = driver.findElement(By.id("singleHidden"));
        assertThat(element.isDisplayed()).isFalse();
    }

    @Test
    void testShouldShowElementNotVisibleWhenParentElementHasHiddenAttribute() {
        final WebDriver driver = getWebDriver("/hidden.html");

        WebElement element = driver.findElement(By.id("child"));
        assertThat(element.isDisplayed()).isFalse();
    }

    /**
     * See <a href=
     * "https://github.com/SeleniumHQ/selenium-google-code-issue-archive/issues/1610"></a>
     */
    @Test
    void testShouldBeAbleToClickOnElementsWithOpacityZero() {
        final WebDriver driver = getWebDriver("/click_jacker.html");

        WebElement element = driver.findElement(By.id("clickJacker"));
        // assertThat(element.getCssValue("opacity")).describedAs("Precondition failed: clickJacker should be transparent")
        //         .isEqualTo("0");
        assertThat(element.getCssValue("opacity")).isEqualTo("0");

        element.click();
        assertThat(element.getCssValue("opacity")).isEqualTo("1");
    }

    @Test
    void testShouldBeAbleToSelectOptionsFromAnInvisibleSelect() {
        final WebDriver driver = getWebDriver("/formPage.html");

        WebElement select = driver.findElement(By.id("invisi_select"));

        List<WebElement> options = select.findElements(By.tagName("option"));
        WebElement apples = options.get(0);
        WebElement oranges = options.get(1);

        assertThat(apples.isSelected()).as("Apples").isTrue();
        assertThat(oranges.isSelected()).as("Oranges").isFalse();

        oranges.click();
        assertThat(apples.isSelected()).as("Apples").isFalse();
        assertThat(oranges.isSelected()).as("Oranges").isTrue();
    }

    @Test
    void testCorrectlyDetectMapElementsAreShown() {
        final WebDriver driver = getWebDriver("/map_visibility.html");

        final WebElement area = driver.findElement(By.id("mtgt_unnamed_0"));

        assertThat(area.isDisplayed()).as("The element and the enclosing map").isTrue();
    }
}
