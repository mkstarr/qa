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
import com.codenvy.ide.MenuCommands;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.mail.MessagingException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** @author Roman Iuvshin */
public class InviteAllDevelopersFromGoogleContactsWithLoginFromOauthTest extends BaseTest {

    private static String CURRENT_WINDOW = null;

    private WebDriverWait wait;

    /**
     * login using oauth
     *
     * @see com.codenvy.ide.BaseTest#start()
     */
    @Override
    @Before
    public void start() {
        //Choose browser Web driver:
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
        }

        try {
            IDE.LOGIN.waitTenantAllLoginPage();
            IDE.LOGIN.googleOauthBtnClick();
            IDE.GOOGLE.waitOauthPageOpened();
            IDE.GOOGLE.typeLogin(USER_NAME);
            IDE.GOOGLE.typePassword(USER_PASSWORD);
            IDE.GOOGLE.clickSignIn();
            IDE.GOOGLE.waitAllowApplicationButton();
            IDE.GOOGLE.clickOnAllowButton();
            IDE.EXPLORER.waitOpened();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @AfterClass
    public static void tearDown() throws Exception {

        try {
            IDE.MAIL_CHECK.cleanMailBox(FIRST_USER_FOR_INVITE, USER_PASSWORD);
            IDE.MAIL_CHECK.cleanMailBox(SECOND_USER_FOR_INVITE, USER_PASSWORD);
            IDE.MAIL_CHECK.cleanMailBox(THIRD_USER_FOR_INVITE, USER_PASSWORD);
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        try {
            driver.getWindowHandle();
        } catch (NoSuchWindowException e) {
            e.printStackTrace();
            driver.switchTo().window(CURRENT_WINDOW);
        }
        if (!driver.getWindowHandle().equals(CURRENT_WINDOW)) {
            driver.close();
            driver.switchTo().window(CURRENT_WINDOW);
        }
        IDE.GOOGLE.openGoogleAccountPage();
        IDE.GOOGLE.deleteGoogleTokens();


    }

    @Test
    public void inviteAllDevelopersFromGoogleContactsWithLoginFromOauthTest() throws Exception {
        CURRENT_WINDOW = driver.getWindowHandle();
        wait = new WebDriverWait(driver, 60);

        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.INVITE_DEVELOPERS);
        IDE.INVITE_FORM.clickOnShowGmailContactsButton();
        IDE.ASK_DIALOG.waitOpened();
        IDE.ASK_DIALOG.clickYes();
        IDE.ASK_DIALOG.waitClosed();
        // allow operation in google
        IDE.DEBUGER.waitOpenedSomeWin();
        IDE.LOADER.waitClosed();
        switchToNonCurrentWindow(CURRENT_WINDOW);
        IDE.GOOGLE.waitAllowApplicationButton();
        IDE.GOOGLE.clickOnAllowButton();
        driver.switchTo().window(CURRENT_WINDOW);
        IDE.INVITE_FORM.waitInviteDevelopersOpened();
        IDE.INVITE_FORM.waitForUsersFromContactsToInvite(FIRST_USER_FOR_INVITE);
        IDE.INVITE_FORM.waitForUsersFromContactsToInvite(SECOND_USER_FOR_INVITE);
        IDE.INVITE_FORM.waitForUsersFromContactsToInvite(THIRD_USER_FOR_INVITE);

        //step 2 invite all click and check states boxes
        IDE.INVITE_FORM.clickInviteAll();
        assertEquals(IDE.INVITE_FORM.getTextFromIviteButton(), getAmountInvDevelopers(3));
        IDE.INVITE_FORM.waitCheckboxIsCheckedGoogleForm("1");
        IDE.INVITE_FORM.waitCheckboxIsCheckedGoogleForm("2");
        IDE.INVITE_FORM.waitCheckboxIsCheckedGoogleForm("3");

        //step 3 uncheck invite  btn and check states boxes and inv. developers button
        IDE.INVITE_FORM.clickInviteAll();
        IDE.INVITE_FORM.waitChekBoxUnchekedGoogleForm("1");
        IDE.INVITE_FORM.waitChekBoxUnchekedGoogleForm("2");
        IDE.INVITE_FORM.waitChekBoxUnchekedGoogleForm("3");

        assertTrue(IDE.INVITE_FORM.getTextFromIviteButton().contains("Invite developers"));

        //step 4 click on developers 1-3 and check state boxes and inv. developers button
        IDE.INVITE_FORM.clickOnCheckBoxGoogleForm("1");
        IDE.INVITE_FORM.waitCheckboxIsCheckedGoogleForm("1");
        assertEquals(IDE.INVITE_FORM.getTextFromIviteButton(), getAmountInvDevelopers(1));
        IDE.INVITE_FORM.clickOnCheckBoxGoogleForm("2");
        IDE.INVITE_FORM.waitCheckboxIsCheckedGoogleForm("2");
        assertEquals(IDE.INVITE_FORM.getTextFromIviteButton(), getAmountInvDevelopers(2));
        IDE.INVITE_FORM.clickOnCheckBoxGoogleForm("3");
        IDE.INVITE_FORM.waitCheckboxIsCheckedGoogleForm("3");
        assertEquals(IDE.INVITE_FORM.getTextFromIviteButton(), getAmountInvDevelopers(3));

        //step 6 click on developers 1-3 and check unchecked states boxes and inv. developers button
        IDE.INVITE_FORM.clickOnCheckBoxGoogleForm("1");
        IDE.INVITE_FORM.waitChekBoxUnchekedGoogleForm("1");
        assertEquals(IDE.INVITE_FORM.getTextFromIviteButton(), getAmountInvDevelopers(2));
        IDE.INVITE_FORM.clickOnCheckBoxGoogleForm("2");
        IDE.INVITE_FORM.waitChekBoxUnchekedGoogleForm("1");
        assertEquals(IDE.INVITE_FORM.getTextFromIviteButton(), getAmountInvDevelopers(1));
        IDE.INVITE_FORM.clickOnCheckBoxGoogleForm("3");
        IDE.INVITE_FORM.waitChekBoxUnchekedGoogleForm("3");
        assertTrue(IDE.INVITE_FORM.getTextFromIviteButton().contains("Invite developers"));

        ///////////////////////
        IDE.INVITE_FORM.clickInviteAll();

        assertTrue(IDE.INVITE_FORM.getTextFromIviteButton().contains("3 developers"));

        IDE.INVITE_FORM.inviteClick();
        IDE.INVITE_FORM.waitPopUp();
        IDE.INVITE_FORM.clickOkOnPopUp();
        IDE.LOGIN.logout();

        // checking email and proceed to invite url
        IDE.MAIL_CHECK.waitAndGetInviteLink(FIRST_USER_FOR_INVITE, USER_PASSWORD);
        IDE.MAIL_CHECK.gotoConfirmInvitePage();
        IDE.EXPLORER.waitOpened();
        //IDE.LOGIN.logout();
        driver.get(LOGIN_URL);

        // checking email and proceed to invite url
        IDE.MAIL_CHECK.waitAndGetInviteLink(SECOND_USER_FOR_INVITE, USER_PASSWORD);
        IDE.MAIL_CHECK.gotoConfirmInvitePage();
        IDE.EXPLORER.waitOpened();
        //  IDE.LOGIN.logout();
        driver.get(LOGIN_URL);


        // checking email and proceed to invite url
        IDE.MAIL_CHECK.waitAndGetInviteLink(THIRD_USER_FOR_INVITE, USER_PASSWORD);
        IDE.MAIL_CHECK.gotoConfirmInvitePage();
        IDE.EXPLORER.waitOpened();
        //IDE.LOGIN.logout();
    }

    private String getAmountInvDevelopers(int numUser) {
        String str =
                (numUser > 1) ? (String.format("Invite %s developers", numUser)) : (String.format("Invite %s developer",
                                                                                                  numUser));
        return str;
    }

}
