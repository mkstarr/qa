/*
 *
 * CODENVY CONFIDENTIAL
 * ________________
 *
 * [2012] - [2014] Codenvy, S.A.
 * All Rights Reserved.
 * NOTICE: All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any. The intellectual and technical concepts contained
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

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Random;

/** @author Musienko Maxim */
public class CheckLoginWithValiDAndInvalidCredentionals extends BaseCLDIDE {

    String invalidPAssword = "invpas" + new Random().nextInt(1024);

    @Test
    public void loginWithWalidCredentionals() throws Exception {
        IDE ide = new IDE("", this.driver);
        driver.get(BaseTest.LOGIN_URL);
        ide.LOGIN.waitTenantLoginPage();
        ide.LOGIN.loginAsTenantRoot();
        ide.GET_STARTED_WIZARD.waitAndCloseWizard();
        ide.LOGIN.logout();
    }

    @Test
    public void loginWithInWalidCredentionals() throws Exception {
        IDE ide = new IDE("", this.driver);
        driver.get(BaseTest.LOGIN_URL);
        ide.LOGIN.waitTenantLoginPage();
        ide.LOGIN.tenantLogin(BaseTest.USER_NAME, invalidPAssword);
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.error-container")));
    }
}