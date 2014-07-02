/*
 *
 * CODENVY CONFIDENTIAL
 * ________________
 *
 * [2012] - [2013] Codenvy, S.A.
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
import com.codenvy.ide.TestConstants;
import com.codenvy.sitecldide.MailUtils.MailReciver;

import org.junit.Test;

import javax.mail.MessagingException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/** @author Musienko Maxim */
public class TryCreateAlreadyExistTst extends BaseCLDIDE {


    MailReciver mailRecive    = new MailReciver(IDE_SETTINGS.getString("gae.email"), IDE_SETTINGS.getString("gae.password"));
    String[]    invalidSample = {"help", "support", "api", "docs"};
    String      PLUS_MAIL     = "plus+mail@mail.com";
    String      SLASH_MAIL    = "slash/mailh@mail.com";

    @Test
    public void checkExistEmail() throws InterruptedException, TimeoutException, MessagingException {
        mailRecive.cleanMailBox();
        driver.get(BaseTest.PROTOCOL + "://" + BaseTest.IDE_HOST + "/site/create-account");
        IDE ide = new IDE("", this.driver);
        CLDIDE.MAINPAGE.typeEmail(MAIL_BOX_LOGIN);
        CLDIDE.MAINPAGE.waitWorkSpaceField();
        CLDIDE.MAINPAGE.typeDomenName(TENANT_NAME + new Random().nextInt(10) + 5);
        CLDIDE.MAINPAGE.clickSignUpBtn();
        CLDIDE.MAINPAGE.waitAlreadyExistErrorMess();
    }


    @Test
    public void checkExistTenantName() throws InterruptedException {
        IDE ide = new IDE("", this.driver);
        CLDIDE.MAINPAGE.typeEmail("cloud.ide.test2@gmail.com");
        CLDIDE.MAINPAGE.waitWorkSpaceField();
        CLDIDE.MAINPAGE.typeDomenName(BaseTest.USER_NAME.split("@")[0]);
        CLDIDE.MAINPAGE.clickSignUpBtn();
        CLDIDE.MAINPAGE.waitInvalidTenantNameErrorMess();
    }


    @Test
    public void checkInvalidTenantNames() throws InterruptedException {
        for (String sample : invalidSample) {
            CLDIDE.MAINPAGE.typeEmail("cloud.ide.test2@gmail.com");
            CLDIDE.MAINPAGE.waitWorkSpaceField();
            CLDIDE.MAINPAGE.typeDomenName(sample);
            CLDIDE.MAINPAGE.clickSignUpBtn();
            CLDIDE.MAINPAGE.waitInvalidTenantNameErrorMess();
        }


    }

    @Test
    public void checkMailBoxIsEmpty() throws InterruptedException, TimeoutException, MessagingException {
        assertFalse(mailRecive.getSubjects().contains("Codenvy"));
        //pause between checks. Mailbox should not contain any mails from codenvy
        Thread.sleep(TestConstants.TIMEOUT);
        assertFalse(mailRecive.getSubjects().contains("Codenvy"));
    }


    @Test
    public void checkValidEmailName() throws Exception {
        driver.get(BaseTest.PROTOCOL + "://" + BaseTest.IDE_HOST + "/site/create-account");
        IDE ide = new IDE("", this.driver);
        CLDIDE.MAINPAGE.typeEmail(PLUS_MAIL);
        CLDIDE.MAINPAGE.clickSignUpBtn();
        String errorMessage = CLDIDE.MAINPAGE.getNotAllowedMess();
        assertEquals("Emails with '+' and '/' are not allowed", errorMessage);
        CLDIDE.MAINPAGE.typeEmail(SLASH_MAIL);
        CLDIDE.MAINPAGE.clickSignUpBtn();
        String errorMessage1 = CLDIDE.MAINPAGE.getNotAllowedMess();
        assertEquals("Emails with '+' and '/' are not allowed", errorMessage1);
    }

}
