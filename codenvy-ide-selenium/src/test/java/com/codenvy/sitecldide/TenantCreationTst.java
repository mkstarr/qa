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
import com.codenvy.ide.Utils;
import com.codenvy.sitecldide.MailUtils.MailReciver;

import org.exoplatform.ide.commons.ParsingResponseException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

/** @author Musienko Maxim */
public class TenantCreationTst extends BaseCLDIDE {

   static String ws[]            =
            {"ndp", "testide423", "testide411", "testide100", "testide83", "testide72", "testide413", "testide128", "testide415",
             "testide412", "testide392", "testide414", "testide416", "testide410", "testide357", "cloudidetest2", "testide391", "testide61",
             "testide53"};
    String domaiNamePrefix = "cldide";

    String firstHeaderText = "Thank you for signing up";

    String secondHeaderText = "We've just sent you an email. Click the confirmation link sent by email to finalize your registration.";


    String[] allLinks =
            {"Features", "Pricing", "Enterprise", "ISV", "Documentation", "About", "Jobs", "Contact", "Blog", "Facebook", "Twitter",
             "Google+", "Sign up", "Login"};


    String goToCreatedTenant;


    static MailReciver reciver = new MailReciver(MAIL_BOX_LOGIN, MAIL_BOX_PASSWORD, TENANT_NAME);


    @AfterClass
    public static void cleanMailBox() throws TimeoutException, MessagingException {
        reciver.cleanMailBox();
    }

    @BeforeClass
    public static void removeTenant() throws IOException, ParsingResponseException {
        Utils.deleteWorkSpacetByName("selenium4");
        Utils.deleteOrganisationIdByName("selenium4");
        Utils.deleteUserByEmail(MAIL_BOX_LOGIN);
   }

    @Test
    public void tenantCreationTest() throws Exception {
        driver.get(BaseTest.PROTOCOL + "://" + BaseTest.IDE_HOST + "/site/create-account");
        CLDIDE.MAINPAGE.waitEmailField();
        IDE ide = new IDE("", this.driver);
        CLDIDE.MAINPAGE.typeEmail(MAIL_BOX_LOGIN);
        CLDIDE.MAINPAGE.waitWorkSpaceField();
        CLDIDE.MAINPAGE.typeDomenName(TENANT_NAME);
        CLDIDE.MAINPAGE.clickSignUpBtn();
        CLDIDE.THANK_YOU_PAGE.waitExpectedTextOnPage(firstHeaderText);
        CLDIDE.THANK_YOU_PAGE.waitExpectedTextOnPage(secondHeaderText);
        //checkAllLinks();
        goToCreatedTenant = reciver.waiAndGetConfirmLink(20, 120);
        driver.get(goToCreatedTenant);
        //IDE ide = new IDE("", this.driver);
        ide.EXPLORER.waitOpened();
        ide.GET_STARTED_WIZARD.waitAndCloseWizard();
        ide.LOGIN.logout();
    }

    /** wait appearance all links on the page */
    private void checkAllLinks() {
        for (String allLink : allLinks) {
            CLDIDE.THANK_YOU_PAGE.waitLinkWithSpecifiedName(allLink);
        }
    }
}
