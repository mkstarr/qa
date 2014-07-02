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

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Roman Iuvshin */
public class ManageAcces extends AbstractTestModule {
    /** @param ide */
    public ManageAcces(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String MANAGE_ACCESS_WINDOW_ID = "ideManageInvitesView-window";

        String CLOSE_MANAGE_ACCESS_FORM_BUTTON_ID = "ideManageInvitesViewCloseButton";

        String ACCEPTED_INVITE =
                "//div[@id='ideManageInvitesViewUserListElement']//div[text()='%s']/../..//span[text()='Accepted']";

        String PENDING_INVITE =
                "//div[@id='ideManageInvitesViewUserListElement']//div[text()='%s']/../..//span[text()='Pending']";


        String INVITED_USER =
                "//div[@id='ideManageInvitesViewUserListElement']//div[text()='%s']";

    }

    @FindBy(id = Locators.MANAGE_ACCESS_WINDOW_ID)
    WebElement manageAccessWindow;

    @FindBy(id = Locators.CLOSE_MANAGE_ACCESS_FORM_BUTTON_ID)
    WebElement closeBtn;

    /** wait for appearing manage access form */
    public void waitManageAccessForm() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                                    .id(Locators


                                                                                                                .MANAGE_ACCESS_WINDOW_ID)));
    }

    /** wait for appearing accepted invite user */
    public void waitAcceptedInviteUser(String userEmail) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                Locators.ACCEPTED_INVITE, userEmail))));
    }


    /** wait for appearing  user in list */
    public void waitUserInInviteinList(String user) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                Locators.INVITED_USER, user))));
    }

    /** wait for appearing pending invite user */
    public void waitPendingInviteUser(String userEmail) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                Locators.PENDING_INVITE, userEmail))));
    }

    /** close manage access form */
    public void closeManageAccessForm() {
        closeBtn.click();
        waitForManageAccessFormIsClosed();
    }

    /** wait for manage access form is closed */
    private void waitForManageAccessFormIsClosed() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                      .id(Locators
                                                                                                                  .MANAGE_ACCESS_WINDOW_ID)));
    }
}
