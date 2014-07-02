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
package com.codenvy.ide.addAdditionalUser;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;

import org.junit.AfterClass;
import org.junit.Test;

import javax.mail.MessagingException;
import java.util.concurrent.TimeoutException;

import static org.fest.assertions.Assertions.assertThat;
///** @author Musienko Maxim */

/** @author Roman Iuvshin */
public class AddSshKeyForGitHubAndAddAdditionalUserTest extends BaseTest {
    @AfterClass
    public static void tearDown() {
        try {
            IDE.MAIL_CHECK.cleanMailBox(NOT_ROOT_USER_NAME, NOT_ROOT_USER_PASSWORD);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void generateAndUploadKeyToGithubTest() throws Exception {
        // check and clean up ssh key and token on github
        IDE.GITHUB.apiCheckAndDeleteTokens();
        IDE.GITHUB.apiCheckAndDeleteSshKeys();
        IDE.GITHUB.apiRemoveGithubSShOnIDE();
        driver.get(WORKSPACE_URL);

        // add new ssh key and token
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        String currentWin = driver.getWindowHandle();
        IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.PREFERNCESS);
        IDE.PREFERENCES.waitPreferencesOpen();
        IDE.PREFERENCES.selectCustomizeMenu(MenuCommands.Preferences.SSH_KEY);
        IDE.LOADER.waitClosed();
        IDE.SSH.waitSshView();
        IDE.LOADER.waitClosed();
        if (IDE.SSH.isKeyPresent("github.com") == true) {
            IDE.SSH.clickDeleteKeyInGridPosition("github.com");
            IDE.ASK_DIALOG.waitOpened();
            IDE.ASK_DIALOG.clickYes();
            IDE.ASK_DIALOG.waitClosed();
            IDE.SSH.waitDisappearContentInSshListGrig("github.com");

            IDE.SSH.clickOnGenerateAndUploadButton();
            IDE.ASK_DIALOG.waitOpened();
            IDE.ASK_DIALOG.clickYes();
//            IDE.SSH.waitSshConfirmUploadForm();
//            IDE.SSH.clickOnUploadKeyOkButton();
//            IDE.IMPORT_FROM_GITHUB.waitLoginFormToGithub();
//            IDE.IMPORT_FROM_GITHUB.clickOkOnLoginForm();
            // switch to auth window
            switchToGithubLoginWindow(currentWin);
            IDE.GITHUB.waitAuthorizationPageOpened();
            IDE.GITHUB.typeLogin(USER_NAME);
            IDE.GITHUB.typePass(USER_PASSWORD);
            IDE.GITHUB.clickOnSignInButton();
            IDE.GITHUB.waitAuthorizeBtn();
            IDE.GITHUB.clickOnAuthorizeBtn();
        } else {
            IDE.SSH.clickOnGenerateAndUploadButton();
            IDE.ASK_DIALOG.waitOpened();
            IDE.ASK_DIALOG.clickYes();
//            IDE.SSH.waitSshConfirmUploadForm();
//            IDE.SSH.clickOnUploadKeyOkButton();
//            IDE.IMPORT_FROM_GITHUB.waitLoginFormToGithub();
//            IDE.IMPORT_FROM_GITHUB.clickOkOnLoginForm();
            // switch to auth window
            switchToGithubLoginWindow(currentWin);
            IDE.GITHUB.waitAuthorizationPageOpened();
            IDE.GITHUB.typeLogin(USER_NAME);
            IDE.GITHUB.typePass(USER_PASSWORD);
            IDE.GITHUB.clickOnSignInButton();
            IDE.GITHUB.waitAuthorizeBtn();
            IDE.GITHUB.clickOnAuthorizeBtn();
        }
        // switch to ide window
        driver.switchTo().window(currentWin);

        IDE.SSH.waitSshView();
        IDE.LOADER.waitClosed();
        IDE.SSH.clickOnGenerateAndUploadButton();
        IDE.LOADER.waitClosed();
        IDE.SSH.waitAppearContentInSshListGrig("github.com");
        assertThat(IDE.SSH.getAllKeysList().split("\n")).contains("github.com", "View", "Delete");
        IDE.PREFERENCES.clickOnCloseFormBtn();

        // TODO REMOVE IT AFTER FIX IDE-2773
        if (IDE.SSH.isUploadKeyFormVisible() == true) {
            IDE.SSH.clickOnUploadKeyCancelButton();
        }
    }

    @Test
    public void addAdditionalUserForTesting() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.INVITE_DEVELOPERS);
        //  IDE.ASK_DIALOG.waitOpened();
        //  IDE.ASK_DIALOG.clickNo();
        IDE.INVITE_FORM.waitInviteDevelopersOpened();
        IDE.INVITE_FORM.typeEmailForInvite(NOT_ROOT_USER_NAME);
        IDE.INVITE_FORM.inviteClick();
        IDE.INVITE_FORM.waitPopUp();
        IDE.INVITE_FORM.clickOkOnPopUp();

        IDE.LOGIN.logout();

        IDE.MAIL_CHECK.waitAndGetInviteLink(NOT_ROOT_USER_NAME, NOT_ROOT_USER_PASSWORD);
        IDE.MAIL_CHECK.gotoConfirmInvitePage();

        if (IDE.SELECT_WORKSPACE.isSelectWorkspacePageOpened() == true) {
            IDE.SELECT_WORKSPACE.waitWorkspaceInSelectWorkspacePage(TENANT_NAME);
            IDE.SELECT_WORKSPACE.clickOnWorkspaceName(TENANT_NAME);
        }

        IDE.EXPLORER.waitOpened();
    }


    /** @param currentWin */
    private void switchToGithubLoginWindow(String currentWin) {
        for (String handle : driver.getWindowHandles()) {
            if (currentWin.equals(handle)) {

            } else {
                driver.switchTo().window(handle);
                break;
            }
        }
    }

}
