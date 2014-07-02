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
package com.codenvy.ide.userInvite;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;

import org.junit.AfterClass;
import org.junit.Test;

import javax.mail.MessagingException;
import java.util.concurrent.TimeoutException;

/** @author <a href="mailto:foo@bar.org">Foo Bar</a> */
public class ManageAccessAndCheckInvitationMessageTest extends BaseTest {
    final static String INVITATION_MESSAGE = "This is a test message for checking";

    final static String SUBJECT = "You've been invited to use " + BaseTest.TENANT_NAME + " workspace";

    @AfterClass
    public static void tearDown() throws Exception {
        IDE.GOOGLE.openGoogleAccountPage();
        IDE.GOOGLE.deleteGoogleTokens();
        try {
            IDE.MAIL_CHECK.cleanMailBox(SINGLE_USER_FOR_INVITE, USER_PASSWORD);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void revokeUserInviteTest() throws Exception {
        /**
         * Authorization on google
         */
        {
            String currentWin = driver.getWindowHandle();
            IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
            IDE.EXPLORER.waitOpened();
            IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.INVITE_DEVELOPERS);

            IDE.INVITE_FORM.clickOnShowGmailContactsButton();
            IDE.INVITE_FORM.waitForConnectToGoogleForm();
            IDE.INVITE_FORM.clickOnOkButtonOnConnectToGoogleForm();

            switchToNonCurrentWindow(currentWin);

            IDE.GOOGLE.waitOauthPageOpened();
            IDE.GOOGLE.typeLogin(USER_NAME);
            IDE.GOOGLE.typePassword(USER_PASSWORD);
            IDE.GOOGLE.clickSignIn();
            IDE.GOOGLE.waitAllowApplicationButton();
            IDE.GOOGLE.clickOnAllowButton();

            driver.switchTo().window(currentWin);

            IDE.EXPLORER.waitOpened();
        }
        IDE.LOADER.waitClosed();
        IDE.INVITE_FORM.waitInviteDevelopersOpened();
        IDE.INVITE_FORM.typeEmailForInvite("tratata@ss.ss");
        IDE.INVITE_FORM.clickOnAddAMessageButton();
        IDE.INVITE_FORM.waitMessageInputTextarea();
        IDE.INVITE_FORM.typeInvitationMeassge(INVITATION_MESSAGE);
        IDE.INVITE_FORM.inviteClick();
        IDE.INVITE_FORM.waitPopUp();
        IDE.INVITE_FORM.clickOkOnPopUp();

        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.MANAGE_ACCESS);
        IDE.MANAGE_ACCESS.waitManageAccessForm();
        IDE.MANAGE_ACCESS.waitUserInInviteinList("tratata@ss.ss");
        IDE.MANAGE_ACCESS.closeManageAccessForm();
    }

    @Test
    public void checkConfirmedInviteAndInvitationMessageTest() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.INVITE_DEVELOPERS);
        IDE.INVITE_FORM.waitInviteDevelopersOpened();
        IDE.INVITE_FORM.typeEmailForInvite(SINGLE_USER_FOR_INVITE);
        IDE.INVITE_FORM.clickOnAddAMessageButton();
        IDE.INVITE_FORM.waitMessageInputTextarea();
        IDE.INVITE_FORM.typeInvitationMeassge(INVITATION_MESSAGE);
        IDE.INVITE_FORM.inviteClick();
        IDE.INVITE_FORM.waitPopUp();
        IDE.INVITE_FORM.clickOkOnPopUp();

        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.MANAGE_ACCESS);
        IDE.MANAGE_ACCESS.waitManageAccessForm();
        IDE.MANAGE_ACCESS.waitUserInInviteinList(SINGLE_USER_FOR_INVITE);
        IDE.MANAGE_ACCESS.closeManageAccessForm();

// On this moment we have not  pending and accepted statuses for invited statuses, part of the code below comment,
// if the features will be returned, we can uncomment the code

//        IDE.LOGIN.logout();
//
        IDE.MAIL_CHECK.waitAndGetInviteLink(SINGLE_USER_FOR_INVITE, USER_PASSWORD);
//
        IDE.MAIL_CHECK.gotoConfirmInvitePage();
        IDE.EXPLORER.waitOpened();
//        IDE.EXPLORER.waitOpened();
//        // checking for invitation message
//        assertTrue(IDE.MAIL_RECEIVER.getFullMessage(SUBJECT).contains(INVITATION_MESSAGE));
//        IDE.LOGIN.waitTenantAllLoginPage();
//        IDE.LOGIN.tenantLogin(USER_NAME, USER_PASSWORD);
//        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
//        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.MANAGE_ACCESS);
//        IDE.MANAGE_ACCESS.waitManageAccessForm();
//        IDE.MANAGE_ACCESS.waitUserInInviteinList(SINGLE_USER_FOR_INVITE);
//        IDE.MANAGE_ACCESS.closeManageAccessForm();
    }


}
