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
import com.codenvy.sitecldide.MailUtils.MailReciver;

import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import javax.mail.MessagingException;
import java.util.concurrent.TimeoutException;

/**
 * @author Musienko Maxim
 *
 */
public class ForgotPasswordTst extends BaseCLDIDE {

    private String notExistMail = "notExistMAil@gmail.com";

    private String errorMess = "User " + notExistMail + " is not registered in the system.";

    static MailReciver reciver = new MailReciver(MAIL_BOX_LOGIN, MAIL_BOX_PASSWORD, TENANT_NAME);

    @AfterClass
    public static void cleanMailBox() throws TimeoutException, MessagingException {
        reciver.cleanMailBox();
    }

    @Test
    public void tryLoginWithInvalidCredantionals() throws Exception {
        driver.get(BaseTest.LOGIN_URL);
        IDE ide = new IDE("", this.driver);
        ide.LOGIN.waitTenantLoginPage();
        ide.LOGIN.clickForgotPassword();
        CLDIDE.RECOVER_PASSWORD.waitMainElements();
        CLDIDE.RECOVER_PASSWORD.typeToMailField(notExistMail);
        CLDIDE.RECOVER_PASSWORD.clickOnSendMyPassBtn();
        CLDIDE.RECOVER_PASSWORD.waitSignUpLink(errorMess);
    }


    @Test
    public void tryRecoverPasswordAndLoginWithNewPass() throws Exception {
        CLDIDE.RECOVER_PASSWORD.typeToMailField(MAIL_BOX_LOGIN);
        CLDIDE.RECOVER_PASSWORD.clickOnSendMyPassBtn();
        CLDIDE.RECOVER_PASSWORD.waitResetMessage();
        String forgotLnk = reciver.waiAndGetForgotPassLink(10, 120);
        driver.get(forgotLnk);


    }

    @Test
    public void checkMessagesForInValidDate() {
        CLDIDE.RESET_PASSWORD.typeNewPassword(Keys.ENTER.toString());
        CLDIDE.RESET_PASSWORD.clickResetBtn();
        CLDIDE.RESET_PASSWORD.waitErrorMessForEmptyFields();
        CLDIDE.RESET_PASSWORD.typeNewPassword("123");
        CLDIDE.RESET_PASSWORD.confirmNewPassword("456");
        CLDIDE.RESET_PASSWORD.clickResetBtn();
        CLDIDE.RESET_PASSWORD.waitErrorMessForNotIdentPass();
    }

    @Test
    public void setupNewPassAndLogin() throws Exception {
        CLDIDE.RESET_PASSWORD.waitLabelWithUsedEmail(BaseCLDIDE.MAIL_BOX_LOGIN);
        CLDIDE.RESET_PASSWORD.typeNewPassword(BaseCLDIDE.MAIL_BOX_PASSWORD + "2");
        CLDIDE.RESET_PASSWORD.confirmNewPassword(BaseCLDIDE.MAIL_BOX_PASSWORD + "2");
        CLDIDE.RESET_PASSWORD.clickResetBtn();
        IDE ide = new IDE("", this.driver);
        ide.LOGIN.waitTenantLoginPage();
        ide.LOGIN.tenantLogin(BaseCLDIDE.MAIL_BOX_LOGIN, BaseCLDIDE.MAIL_BOX_PASSWORD + "2");
        ide.EXPLORER.waitOpened();
    }





}
