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
import com.codenvy.ide.IDE;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertTrue;

/** @author <a href="mailto:foo@bar.org">Foo Bar</a> */
public class InviteDevelopersFromGithubWithLoginFromOauthTest extends BaseTest {
    private static final String PROJECT = "testRepo";

    private static final String INVITATION_MESSAGE = "This is a test message for checking";

    private static final String SUBJECT = "You've been invited to use " + BaseTest.TENANT_NAME + " workspace";

    @Override
    @Before
    public void start() throws Exception {
    }

    @BeforeClass
    public static void before() throws Exception {
        switch (BROWSER_COMMAND) {
            case GOOGLE_CHROME:
                driver = new ChromeDriver();
                break;
            default:
                driver = new FirefoxDriver();
        }

        IDE = new IDE(LOGIN_URL, driver);
        try {
            driver.manage().window().maximize();
            driver.get(LOGIN_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        IDE.LOGIN.waitTenantAllLoginPage();
        IDE.LOGIN.githubBtnClick();
        IDE.GITHUB.waitAuthorizationPageOpened();
        IDE.GITHUB.typeLogin(USER_NAME);
        IDE.GITHUB.typePass(USER_PASSWORD);
        IDE.GITHUB.clickOnSignInButton();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
            IDE.MAIL_CHECK.cleanMailBox(FIRST_GITHUB_USER_FOR_INVITE, USER_PASSWORD);
            IDE.MAIL_CHECK.cleanMailBox(SECOND_GITHUB_USER_FOR_INVITE, USER_PASSWORD);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
    }

    @Test
    public void inviteAllDevelopersFromGithubWithLoginFromOauthAndCHTest() throws Exception {
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.EXPLORER.waitOpened();

        IDE.WELCOME_PAGE.waitImportFromGithubBtn();
        IDE.WELCOME_PAGE.clickImportFromGithub();
        IDE.LOADER.waitClosed();
        IDE.IMPORT_FROM_GITHUB.waitImportFromGithubFormOpened();
        IDE.LOADER.waitClosed();
        IDE.IMPORT_FROM_GITHUB.selectProjectByName(PROJECT);
        IDE.IMPORT_FROM_GITHUB.finishBtnClick();
        IDE.IMPORT_FROM_GITHUB.waitImportFromGithubFormClosed();
        IDE.IMPORT_FROM_GITHUB.waitCloningProgressFormClosed();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);

        IDE.LOADER.waitClosed();
        IDE.INVITE_FORM.waitInviteDevelopersOpened();
        IDE.LOADER.waitClosed();
        // there is only checking that collaborators is appeared in invite form.
        IDE.INVITE_FORM.waitForUsersFromContactsToInvite(FIRST_GITHUB_USER_FOR_INVITE);
        IDE.INVITE_FORM.waitForUsersFromContactsToInvite(SECOND_GITHUB_USER_FOR_INVITE);
        IDE.INVITE_FORM.waitChekBoxUnchekedGithubForm(FIRST_GITHUB_USER_FOR_INVITE);
        IDE.INVITE_FORM.waitChekBoxUnchekedGithubForm(SECOND_GITHUB_USER_FOR_INVITE);
        assertTrue(IDE.INVITE_FORM.isInviteButtonDisabled());

        IDE.INVITE_FORM.clickOnCheckBoxGithubForm(FIRST_GITHUB_USER_FOR_INVITE);
        IDE.INVITE_FORM.clickOnCheckBoxGithubForm(SECOND_GITHUB_USER_FOR_INVITE);

        IDE.INVITE_FORM.waitCheckboxIsCheckedGithubForm(FIRST_GITHUB_USER_FOR_INVITE);
        IDE.INVITE_FORM.waitCheckboxIsCheckedGithubForm(SECOND_GITHUB_USER_FOR_INVITE);

        assertTrue(IDE.INVITE_FORM.isInviteButtonEnabled());

        IDE.INVITE_FORM.clickOnAddAMessageButton();

        IDE.INVITE_FORM.waitMessageInputTextarea();

        IDE.INVITE_FORM.typeInvitationMeassge(INVITATION_MESSAGE);

        IDE.INVITE_FORM.inviteClick();

        IDE.INVITE_FORM.waitPopUp();
        IDE.INVITE_FORM.clickOkOnPopUp();

        IDE.LOGIN.logout();
        // checking email and proceed to invite url
        IDE.MAIL_CHECK.waitAndGetInviteLink(FIRST_GITHUB_USER_FOR_INVITE, USER_PASSWORD);
        IDE.MAIL_CHECK.gotoConfirmInvitePage();
        IDE.EXPLORER.waitOpened();
        driver.get(LOGIN_URL);
        // IDE.LOGIN.logout();

        // checking email and proceed to invite url
        IDE.MAIL_CHECK.waitAndGetInviteLink(SECOND_GITHUB_USER_FOR_INVITE, USER_PASSWORD);
        IDE.MAIL_CHECK.gotoConfirmInvitePage();
        IDE.EXPLORER.waitOpened();
        driver.get(LOGIN_URL);
        //IDE.LOGIN.logout();
    }

    @Test
    public void checkInvitationMessageFromGithubForm() throws IOException, MessagingException, TimeoutException {
        IDE.MAIL_CHECK.waitAndGetInviteLink(FIRST_GITHUB_USER_FOR_INVITE, USER_PASSWORD);
        assertTrue(IDE.MAIL_RECEIVER.getFullMessage(SUBJECT).contains(INVITATION_MESSAGE));

        IDE.MAIL_CHECK.waitAndGetInviteLink(SECOND_GITHUB_USER_FOR_INVITE, USER_PASSWORD);
        assertTrue(IDE.MAIL_RECEIVER.getFullMessage(SUBJECT).contains(INVITATION_MESSAGE));

    }
}
