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
package com.codenvy.ide.factoryUrl;

import com.codenvy.ide.BaseTest;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Musienko Maxim */
public class FactoryAsLoginUserWithOrganizationIDTest extends BaseTest {
    private String expectedImageInTheTabLocator = "//td/img[@src='https://codenvy.com/wp-content/uploads/2014/01/icon-android.png']";

    private String expectedTabcontentLocator =
            "//div[@id='information']//td[@class='tabTitleText' and text()='Greeting title for authenticated users']";


    private String expectedUserContent = "CODENVY PRIVACY POLICY\n" +
                                         "Thank you for visiting Codenvy.com (the “Site”), which is owned and operated by Codenvy, " +
                                         "Inc. (“Codenvy”), " +
                                         "from its location in the United States. We strive to make our Privacy Policy (the “Privacy " +
                                         "Policy”) easy to understand. " +
                                         "While we encourage you to fully review our Privacy Policy, " +
                                         "we provide a summary below that describes in general terms what " +
                                         "we do and what we don’t do. The complete Privacy Policy is set forth below the summary in the " +
                                         "form of an FAQ. We also " +
                                         "encourage you to review the Site Terms of Service (the “Terms of Service”), " +
                                         "the agreement between you and Codenvy that " +
                                         "governs your use of the Site, as well as provides the terms and conditions under which Codenvy " +
                                         "provides, and you may use, " +
                                         "the Codenvy web-based, development platform-as-a-service accessed through the Site (the " +
                                         "“Service”). The Privacy Policy is " +
                                         "incorporated into and made a part of the Terms of Service.\n" +
                                         "Codenvy expressly reserves the right to modify the Privacy Policy at any time in its sole " +
                                         "discretion, " +
                                         "and without prior notice to you, by including such alteration and/or modification in the " +
                                         "Privacy Policy, " +
                                         "along with a notice of the effective date of such modified Privacy Policy.";

    @Test
    public void factoryUrlOrganizationWelcomeAsUnLoginUser() throws Exception {
        driver.get(new FactoryAsUnLoginUserWithOrganizationIDTest().getFactoryUrlWithOrgId());
        IDE.LOADING_BEHAVIOR.waitLoadPageIsClosed();
        IDE.EXPLORER.waitOpened();
        checkMainItems();
        IDE.FACTORY_URL.waitContentIntoInformationPanelWithOrgId(expectedUserContent);
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(expectedImageInTheTabLocator)));
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(expectedTabcontentLocator)));
        IDE.FACTORY_URL.waitCopyToMyWorkspaceButton();
    }

    /**
     * wait main content for just cloned project
     *
     * @throws Exception
     */
    private void checkMainItems() throws Exception {
        IDE.EXPLORER.waitForItemInOpenedProjectByName("js");
        IDE.EXPLORER.waitForItemInOpenedProjectByName("styles");
        IDE.EXPLORER.waitForItemInOpenedProjectByName("index.html");
    }

}
