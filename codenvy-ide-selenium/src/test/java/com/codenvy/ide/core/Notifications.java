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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Roman Iuvshin */
public class Notifications extends AbstractTestModule {

    public Notifications(IDE ide) {
        super(ide);
    }

    private interface Locators {

        String NOTIFICATION_POPUP = "//div[@class='popupContent']/..";

        String CLOSE_POPUP_BUTTON = "//div[@class='popupContent']/img";

        String NOTIFY_USERS_FORM = "//div[@view-id='ideCollaborationResourceLocked']";

        String NOTIFY_USERS_BUTTON = "//div[@view-id='ideCollaborationResourceLocked']//td[text()='Notify Users']";

        String CANCEL_BUTTON = "//div[@view-id='ideCollaborationResourceLocked']//td[text()='Cancel']";

    }

    @FindBy(xpath = Locators.NOTIFICATION_POPUP)
    private WebElement notification;

    @FindBy(xpath = Locators.NOTIFY_USERS_BUTTON)
    private WebElement notifyUsersBtn;

    @FindBy(xpath = Locators.CLOSE_POPUP_BUTTON)
    private WebElement closePopUpBtn;

    /** wait for appearing notification popup */
    public void waitNotificationPopup() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                Locators.NOTIFICATION_POPUP)));
    }

    /** wait until notification popup will disappear */
    public void waitUntilNotificationPopupDisappear() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(
                Locators.NOTIFICATION_POPUP)));
    }

    /**
     * Get notification message
     *
     * @return notification message
     */
    public String getNotificationContent() {
        return notification.getText();
    }

    /** click on notify users button */
    public void clickOnNotifyUsersButton() {
        notifyUsersBtn.click();
    }

    /** wait notify users form appear */
    public void waitNotifyUsersFormAppear() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                Locators.NOTIFY_USERS_FORM)));
    }

    /** wait notify users form disappear */
    public void waitNotifyUsersFormDisappear() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(
                Locators.NOTIFY_USERS_FORM)));

    }

    /** click on close notificatiion pop-up button */
    public void clickOnCloseNotificationButton() {
        closePopUpBtn.click();
    }

    /** wait close notification button */
    public void waitCloseNotificationPopupButton() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                Locators.CLOSE_POPUP_BUTTON)));
    }

    public void closeNotification() {
        WebElement element = driver().findElement(By.xpath(Locators.NOTIFICATION_POPUP));
        element.click();
        waitCloseNotificationPopupButton();
        clickOnCloseNotificationButton();
        waitUntilNotificationPopupDisappear();
    }
}