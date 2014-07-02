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
package com.codenvy.ide;

import com.codenvy.sitecldide.CLDIDE;

import org.exoplatform.ide.commons.ParsingResponseException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/** @author Musienko Maxim */
public class TenantsPrepare extends BaseTest {

    String firstHeaderText = "Thank you for signing up";

    @Override
    @Before
    public void start() throws TimeoutException, MessagingException {
        CLDIDE inst = new CLDIDE(driver);
        IDE.MAIL_CHECK.cleanMailBox(USER_NAME, USER_PASSWORD);
        IDE.MAIL_CHECK.cleanMailBox(NOT_ROOT_USER_NAME, NOT_ROOT_USER_PASSWORD);
        driver.get(BaseTest.PROTOCOL + "://" + BaseTest.IDE_HOST + "/site/create-account");
        inst.MAINPAGE.waitEmailField();
        inst.MAINPAGE.waitWorkSpaceField();
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

        IDE = new com.codenvy.ide.IDE(LOGIN_URL, driver);
        try {

            driver.manage().window().maximize();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void restoreMainTenant() throws IOException, ParsingResponseException, InterruptedException {
        CLDIDE inst = new CLDIDE(driver);
        deleteMainUser();
        inst.MAINPAGE.typeEmail(USER_NAME);
        inst.MAINPAGE.typeDomenName(TENANT_NAME);
        inst.MAINPAGE.clickSignUpBtn();
        inst.THANK_YOU_PAGE.waitExpectedTextOnPage(firstHeaderText);
        IDE.MAIL_CHECK.waitAndGetConfirmLink(USER_NAME, USER_PASSWORD, TENANT_NAME);
        IDE.MAIL_CHECK.gotoConfirmCreatePage();
        IDE.EXPLORER.waitOpened();
        //t.substring(0, t.indexOf(";"))
        String accesKey = driver.manage().getCookieNamed("session-access-key").toString();
        accesKey = accesKey.substring(0, accesKey.indexOf(";"));
        Utils.restoreUserPassword(accesKey, USER_PASSWORD);
        Utils.restoreUserProfileInfo(accesKey, USER_PASSWORD);
    }

    @Test
    public void restoreAdditionalTenant() throws IOException, ParsingResponseException, InterruptedException {
        driver.manage().deleteAllCookies();
        CLDIDE inst = new CLDIDE(driver);
        deleteAdditionalUser();
        inst.MAINPAGE.typeEmail(NOT_ROOT_USER_NAME);
        inst.MAINPAGE.typeDomenName(ADDITIONAL_TENANT_NAME);
        inst.MAINPAGE.clickSignUpBtn();
        inst.THANK_YOU_PAGE.waitExpectedTextOnPage(firstHeaderText);
        IDE.MAIL_CHECK.waitAndGetConfirmLink(NOT_ROOT_USER_NAME, USER_PASSWORD, ADDITIONAL_TENANT_NAME);
        IDE.MAIL_CHECK.gotoConfirmCreatePage();
        IDE.EXPLORER.waitOpened();
        //t.substring(0, t.indexOf(";"))
        String accesKey = driver.manage().getCookieNamed("session-access-key").toString();
        accesKey = accesKey.substring(0, accesKey.indexOf(";"));
        Utils.restoreUserPassword(accesKey, USER_PASSWORD);
    }


    /**
     * delete all 'exoinvite' main user infrastructure from the IDE
     * it mean delete workspace, organization and current user with
     * IDE API
     * This tenant using as main in selenium test-process
     *
     * @throws IOException
     * @throws ParsingResponseException
     */
    private void deleteMainUser() throws IOException, ParsingResponseException {
        Utils.deleteWorkSpacetByName(BaseTest.TENANT_NAME);
        Utils.deleteOrganisationIdByName(BaseTest.TENANT_NAME);
        Utils.deleteUserByEmail(BaseTest.USER_NAME);
    }

    /**
     * delete all additional user infrastructure from the IDE
     * it mean delete workspace, organization and current user with
     * IDE API
     * This tenant using as additional in selenium test-process (for invite and collaboration operations).
     *
     * @throws IOException
     * @throws ParsingResponseException
     */
    private void deleteAdditionalUser() throws IOException, ParsingResponseException {
        Utils.deleteWorkSpacetByName(BaseTest.ADDITIONAL_TENANT_NAME);
        Utils.deleteOrganisationIdByName(BaseTest.ADDITIONAL_TENANT_NAME);
        Utils.deleteUserByEmail(BaseTest.NOT_ROOT_USER_NAME);
    }


}
