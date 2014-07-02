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
package com.codenvy.sitecldide;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.IDE;
import com.codenvy.sitecldide.core.ProfilePage;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;

/**
 * @author Musienko Maxim
 *
 */
public class ProfileMainOperationsTst extends BaseCLDIDE {


    @Test
    public void checkMessForNotValidPassword() throws Exception {
        IDE ide = new IDE("", this.driver);
        driver.get(BaseTest.LOGIN_URL);
        ide.LOGIN.waitTenantLoginPage();
        ide.LOGIN.tenantLogin(BaseCLDIDE.MAIL_BOX_LOGIN, BaseCLDIDE.MAIL_BOX_PASSWORD +"2");
        ide.EXPLORER.waitOpened();
        String currentWin = driver.getWindowHandle();
        ide.GET_STARTED_WIZARD.waitAndCloseWizard();
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Profile")));
        driver.findElement(By.linkText("Profile")).click();
        ide.DEBUGER.waitOpenedSomeWin();
        switchToWin(currentWin);
        CLDIDE.PROFILE.waitProfilePage();
        CLDIDE.PROFILE.typeNewPass(BaseCLDIDE.MAIL_BOX_PASSWORD);
        CLDIDE.PROFILE.typeConfirmedPass(BaseCLDIDE.MAIL_BOX_PASSWORD + "***");
        CLDIDE.PROFILE.clickSubmitPass();
        CLDIDE.PROFILE.waitErrorMessWithNotMatchedPass();
    }


    @Test
    public void checkMessForEmptyName() throws Exception {
        CLDIDE.PROFILE.clearProfileFields();
        CLDIDE.PROFILE.selectJobFromDropList(ProfilePage.JobList.SELECT_ROLE);
        CLDIDE.PROFILE.clickUpdateProfileBtn();
        CLDIDE.PROFILE.waitErrorMessWithEmptyFirstNameField();
    }


    @Test
    public void checkMessForEmptyLastName() throws Exception {
        CLDIDE.PROFILE.typeFirstName(MAIL_BOX_LOGIN.substring(0, MAIL_BOX_LOGIN.indexOf('@')));
        CLDIDE.PROFILE.clickUpdateProfileBtn();
        CLDIDE.PROFILE.waitErrorMessWithEmptyLastNameField();
    }


    @Test
    public void checkMessForEmptyPhone() throws Exception {
        CLDIDE.PROFILE.typeLastName(MAIL_BOX_LOGIN.substring(0, MAIL_BOX_LOGIN.indexOf('@')) + "lastMame");
        CLDIDE.PROFILE.clickUpdateProfileBtn();
        CLDIDE.PROFILE.waitErrorMessWithEmptyPhoneNumberField();
    }


    @Test
    public void checkMessForEmptyCompany() throws Exception {
        CLDIDE.PROFILE.typePhone("866456");
        CLDIDE.PROFILE.clickUpdateProfileBtn();
        CLDIDE.PROFILE.waitErrorMessWithEmptyCompanyField();
    }

    @Test
    public void checkMessForEmptyRole() throws Exception {
        CLDIDE.PROFILE.typeCompany("codenvy");
        CLDIDE.PROFILE.clickUpdateProfileBtn();
        CLDIDE.PROFILE.waitErrorMessWithNotSelectedRole();
    }


    @Test
    public void enterAndChangeValidPassword() throws Exception {
        CLDIDE.PROFILE.typeNewPass(BaseCLDIDE.MAIL_BOX_PASSWORD);
        CLDIDE.PROFILE.typeConfirmedPass(BaseCLDIDE.MAIL_BOX_PASSWORD);
        CLDIDE.PROFILE.clickSubmitPass();
        CLDIDE.PROFILE.waitConfirmMessForChangedPass();
    }

    @Test
    public void enterAndChangeValidPrifileData() throws Exception {
        CLDIDE.PROFILE.typeLastName(MAIL_BOX_LOGIN.substring(0, MAIL_BOX_LOGIN.indexOf('@')) + "lastMame");
        CLDIDE.PROFILE.typeLastName(MAIL_BOX_LOGIN.substring(0, MAIL_BOX_LOGIN.indexOf('@')) + "lastMame");
        CLDIDE.PROFILE.typePhone("86645678");
        CLDIDE.PROFILE.typeCompany("codenvy");
        CLDIDE.PROFILE.selectJobFromDropList(ProfilePage.JobList.OTHER);
        CLDIDE.PROFILE.clickUpdateProfileBtn();
        CLDIDE.PROFILE.waitConfirmMessForChangedProfile();
    }



    public void switchToWin(String winHandl) {
        Set<String> driverWindows = driver.getWindowHandles();
        for (String wins : driverWindows) {
            if (!wins.equals(winHandl)) {
                driver.switchTo().window(wins);

            }
        }
    }


}
