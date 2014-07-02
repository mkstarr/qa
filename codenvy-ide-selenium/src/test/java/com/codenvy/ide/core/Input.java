/*
 *
 * CODENVY CONFIDENTIAL
 * __________________
 *
 *  [2012] - [2013] Codenvy, S.A.
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.core;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Evgen Vidolob
 *
 */
public class Input extends AbstractTestModule {
    /** @param ide */
    public Input(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String SUGGEST_BOX_ID = "exoSuggestPanel";

        String COMBOBOX_VALUE_LOCATOR = "//div[@id='" + SUGGEST_BOX_ID + "']//td[contains(., '%s')]";

        String ARROW_SELECTOR = "img[image-id=suggest-image]";

        String INPUT_FIELD = "//input[@name='ideUploadFormFilenameField']";
    }

    /**
     * Type text to element, optional clear it.
     *
     * @param element
     *         WebElement MUST point or input or textarea Html element
     * @param text
     *         to type
     * @param isClear
     *         is clear element before typing
     * @throws InterruptedException
     */
    public void typeToElement(WebElement element, String text, boolean isClear) throws InterruptedException {
        if (isClear) {
            element.clear();
        }
        element.sendKeys(text);
    }

    /**
     * Type text to element
     *
     * @param element
     *         WebElement, MUST point or input or textarea Html element
     * @param text
     *         Text to type
     * @throws InterruptedException
     */
    public void typeToElement(WebElement element, String text) throws InterruptedException {
        typeToElement(element, text, false);
    }

    /**
     * Set value of the combobox item by typing it into it and pressing enter.
     *
     * @param element
     *         combobox element
     * @param value
     *         value to set
     * @throws InterruptedException
     */
    public void setComboboxValue(WebElement element, String value) throws InterruptedException {
        typeToElement(element, value + "\n", true);
        waitSuggestBoxHide();
    }

    public void clearComboboxValue(WebElement element) throws InterruptedException {
        element.clear();
    }

    private void waitSuggestBoxHide() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    driver.findElement(By.id(Locators.SUGGEST_BOX_ID));
                    return false;
                } catch (NoSuchElementException e) {
                    return true;
                }
            }
        });
    }

    private void waitSuggestBoxShow() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return driver.findElement(By.id(Locators.SUGGEST_BOX_ID)) != null;
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    public void selectComboboxValue(WebElement combobox, String value) {
        try {
            driver().findElement(By.id(Locators.SUGGEST_BOX_ID));
        } catch (NoSuchElementException e) {
            driver().findElement(By.cssSelector(Locators.ARROW_SELECTOR)).click();
            waitSuggestBoxShow();
        }

        WebElement item = driver().findElement(By.xpath(String.format(Locators.COMBOBOX_VALUE_LOCATOR, value)));
        item.click();
        waitSuggestBoxHide();
    }

    public boolean isComboboxValuePresent(WebElement combobox, String value) {
        WebElement arrow = driver().findElement(By.cssSelector(Locators.ARROW_SELECTOR));
        boolean closeSuggestBox = true;
        try {
            driver().findElement(By.id(Locators.SUGGEST_BOX_ID));
            closeSuggestBox = false;
        } catch (NoSuchElementException e) {
            arrow.click();
            waitSuggestBoxShow();
        }
        try {
            WebElement item = driver().findElement(By.xpath(String.format(Locators.COMBOBOX_VALUE_LOCATOR, value)));
            return item != null;
        } catch (NoSuchElementException e) {
            return false;
        } finally {
            if (closeSuggestBox) {
                arrow.click();
                waitSuggestBoxHide();
            }
        }
    }

    /**
     * Returns display value of select item.
     *
     * @param input
     *         select item
     * @return {@link String} display value of the select item
     */
    public String getDisplayValue(WebElement input) {
        return input.getText();
    }

    /**
     * Get value of the input.
     *
     * @param input
     *         input element (select, combobox)
     * @return {@link String} input's value
     */
    public String getValue(WebElement input) {
        return input.getAttribute("value");
    }
}
