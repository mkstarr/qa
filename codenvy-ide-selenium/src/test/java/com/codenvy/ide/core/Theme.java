/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
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

import com.codenvy.ide.IDE;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Roman Iuvshin */
public class Theme extends AbstractTestModule {
    /** @param ide */
    public Theme(IDE ide) {
        super(ide);
    }

    private interface Locators {
        String THEME_SELECTOR = "//table[@id='ideThemesListGrid']//div[contains(.,'%s')]";

        String APPLY_BUTTON_ID = "ideApplyThemeButton";

        String APPPLY_BUTTON_STATE = "//div[@id='ideApplyThemeButton' and @button-enabled='%s']";

        String CURRENT_THEME_ID = "//div[@id='debug-current-theme-name' and text()='%s']";
    }

    @FindBy(id = Locators.APPLY_BUTTON_ID)
    private WebElement applyButton;

    /**
     * wait theme with specific name
     *
     * @param themeName
     */
    public void waitTheme(String themeName) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                                    .xpath(String


                                                                                                                   .format(


                                                                                                                           Locators


                                                                                                                                   .THEME_SELECTOR,
                                                                                                                           themeName))));
    }

    /**
     * select theme with specific name by clicking on it
     *
     * @param themeName
     */
    public void selectTheme(String themeName) {
        driver().findElement(By.xpath(String.format(Locators.THEME_SELECTOR, themeName))).click();
    }

    /** click on apply button */
    public void clickOnApplyButton() {
        applyButton.click();
    }

    /**
     * Wait apply button state enabled or disabled. Use true or false states.
     *
     * @param state
     */
    public void waitApplyButtonState(String state) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                                    .xpath(String.format(
                                                                                                            Locators.APPPLY_BUTTON_STATE,
                                                                                                            state))));
    }

    /**
     * Check that specified theme was changed.
     *
     * @param themeName
     *         Default or Darkula
     */
    public void waitCurrentThemeIDState(final String themeName) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                WebElement element =
                        driver().findElement(By.xpath(String.format(Locators.CURRENT_THEME_ID, themeName)));
                return element != null;
            }
        });
    }
}
