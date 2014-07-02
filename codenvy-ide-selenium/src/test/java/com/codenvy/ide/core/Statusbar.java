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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by The eXo Platform SAS .
 *
 * @author Vitaliy Gulyy
 */

public class Statusbar extends AbstractTestModule {
    /** @param ide */
    public Statusbar(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String CURSOR_POSITION_LOCATOR = "//div[@control-id='__editor_cursor_position']";

        String NAVIGATION_STATUS_LOCATOR = "//div[@control-id='__navigator_status']";

        String BUILD_FAILED_STATUS_LOCATOR =
                "//div[@control-id='__request-notification-control']//tr[contains(.,'failed')]";

        String BUILD_DISAPPEAR = "//div[@id='exoIDEStatusbar']//div[3]";

    }

    @FindBy(xpath = Locators.CURSOR_POSITION_LOCATOR)
    private WebElement cursorPosition;

    @FindBy(xpath = Locators.NAVIGATION_STATUS_LOCATOR)
    private WebElement navigationStatus;

    @FindBy(xpath = Locators.BUILD_FAILED_STATUS_LOCATOR)
    private WebElement buildFailStatus;

    @FindBy(xpath = Locators.BUILD_DISAPPEAR)
    private WebElement buildDissapear;

    /**
     * Get cursor position.
     *
     * @return {@link String}
     */
    public String getCursorPosition() {
        return cursorPosition.getText();
    }

    /**
     * Click on cursor position control of status bar.
     *
     * @throws Exception
     */
    public void clickOnCursorPositionControl() {
        cursorPosition.click();
    }

    public void waitCursorPositionControl() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver arg0) {
                return (cursorPosition != null && cursorPosition.isDisplayed());
            }
        });
    }

    /** wait while build status panel is disappear */
    public void waitDiasspearBuildStatus() {
        new WebDriverWait(driver(), 120).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver arg0) {
                try {
                    WebElement check = driver().findElement(By.xpath(Locators.BUILD_DISAPPEAR));
                    return !(check.isDisplayed()) && check != null;
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    public void waitTextInstatusBarIsPresent(final String txt) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver arg0) {
                try {
                    return getNavigationStatus().contains(txt);
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    public void waitCursorPositionAt(final String position) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver arg0) {
                return (position.equals(getCursorPosition()));
            }
        });
    }

    /** wait appearance failed build message on status bar */
    public void waitBuildFailStatus() {
        new WebDriverWait(driver(), 120).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver arg0) {
                return buildFailStatus != null && buildFailStatus.isDisplayed();
            }
        });
    }

    /**
     * Get navigation status.
     *
     * @return {@link String} text
     */
    public String getNavigationStatus() {
        return navigationStatus.getText();
    }
}
