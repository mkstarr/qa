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

import com.codenvy.ide.IDE;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Roman Iuvshin */
public class ReadOnlyMode extends AbstractTestModule {

    public ReadOnlyMode(IDE ide) {
        super(ide);
    }

    private interface Locators {

        String READ_ONLY_INDICATOR = "//div[@id='exoIDEToolbar']//div[text()='Read-only']";

        String READ_ONLY_VIEW = "//div[@view-id='ideReadOnlyUserView']";

        String JOIN_BUTTON_ID = "ideReadOnlyUserViewJoinButton";

        String CANCEL_BUTTON_ID = "ideReadOnlyUserViewCancelButton";

        String LABEL_ID = "ideReadOnlyUserViewLabel";

        String DISABLED_INVITE_BUTTON =
                "//a[contains(@style, 'color: grey;cursor: default; text-decoration: none;') and text()='Invite People']";

        String READ_ONLY_VIEW_TAB_TITLE = "//div[@id='ideReadOnlyUserView-window']//div[text()='Join Codenvy!']";

        String READ_ONLY_VIEW_TAB_TITLE_SWITHC = "//div[@id='ideReadOnlyUserView-window']//div[text()='Switch to your workspace']";
    }

    @FindBy(xpath = Locators.READ_ONLY_INDICATOR)
    WebElement readonlyIndicator;

    @FindBy(id = Locators.JOIN_BUTTON_ID)
    WebElement joinBtnId;

    @FindBy(id = Locators.CANCEL_BUTTON_ID)
    WebElement cancelBtnId;

    @FindBy(id = Locators.LABEL_ID)
    WebElement labelId;


    /** wait for read only mode indicator */
    public void waitReadonlyIndicator() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.READ_ONLY_INDICATOR)));
    }

    /** wait for read only mode indicator become invisible */
    public void waitReadonlyIndicatorNotVisible() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.READ_ONLY_INDICATOR)));
    }

    /** wait for read only mode view opened */
    public void waitReadOnlyModeViewOpenedWhenNotAuthenticated() {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions
                               .visibilityOfElementLocated(By.xpath(Locators.READ_ONLY_VIEW)));
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions
                               .visibilityOfElementLocated(By.xpath(Locators.READ_ONLY_VIEW_TAB_TITLE)));
    }

    /** wait for read only mode view opened */
    public void waitReadOnlyModeViewOpenedWhenIsAuthenticated() {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions
                               .visibilityOfElementLocated(By.xpath(Locators.READ_ONLY_VIEW)));
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions
                               .visibilityOfElementLocated(By.xpath(Locators.READ_ONLY_VIEW_TAB_TITLE_SWITHC)));
    }


    /** wait for disabled invite users button */
    public void waitDisabledInviteUsersButton() {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions
                               .visibilityOfElementLocated(By.xpath(Locators.DISABLED_INVITE_BUTTON)));
    }

    /** wait for read only mode view closed */
    public void waitReadOnlyModeViewClosed() {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions
                               .invisibilityOfElementLocated(By.xpath(Locators.READ_ONLY_VIEW)));
    }

    /** click on join button */
    public void clickOnJoinButton() {
        joinBtnId.click();
        waitReadOnlyModeViewClosed();
    }

    /** click on cancel button */
    public void clickOnCancelButton() {
        cancelBtnId.click();
        waitReadOnlyModeViewClosed();
    }

    public void waitTextOnlabel(final String labelMessage) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return labelId.getText().equals(labelMessage);
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /** click on read only mode indicator */
    public void clickOnReadOnlyModeIndicator() {
        readonlyIndicator.click();
    }


}
