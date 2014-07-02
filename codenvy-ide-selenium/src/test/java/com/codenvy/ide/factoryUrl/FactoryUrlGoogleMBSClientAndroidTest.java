/*
 *
 * CODENVY CONFIDENTIAL
 * __________________
 *
 *  [2012] - [2014] Codenvy, S.A.
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
package com.codenvy.ide.factoryUrl;

import com.codenvy.ide.BaseTest;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;

/** @author Zaryana Dombrovskaya */
public class FactoryUrlGoogleMBSClientAndroidTest extends BaseTest {

    private static final String FACTORY_PARAMS =
            "&pname=Sample-Android&wname=codenvy-factories&vcs=git&vcsurl=http%3A%2F%2Fcodenvy" +
            ".com%2Fgit%2F04%2F0f%2F7f%2Fworkspacegcpv6cdxy1q34n1i%2FSample-Android&action=openproject&ptype=google-mbs-client-android";

    private static final String CONTENT_1 =
            "The Android client sample project for Google Mobile Backend Starter has been imported and updated with your Project ID and " +
            "Project Number.\n" +
            "Run the sample and edit the project to:\n" +
            "Add Google Cloud Messaging\n" +
            "Add Authenticationg\n" +
            "\n" +
            "This project has been factory-created into a temporary workspace on Codenvy.\n" +
            "\n" +
            "Codenvy is your complete cloud environment to:\n" +
            "\n" +
            "Code, Commit\n" +
            "Build, Package, Test\n" +
            "Run, Debug\n" +
            "PaaS Deploy\n" +
            "Collaborate, Share\n" +
            "\n" +
            "Home Page | Docs | Support | Feature Vote\n" +
            "\n" +
            "\n" +
            "A temporary workspace will be deleted if you are inactive or close the browser.\n" +
            "\n" +
            "To save your work, Login or Create an Account.\n" +
            "\n" +
            "Terms of Service";

    @Before
    public void start() throws IOException {
    }


    @Test
    public void factoryURLWithVcsinfoFalseTest() throws Exception {

        String factory_url_v1 =
                PROTOCOL + "://" + IDE_HOST + "/factory?v=1.0" + FACTORY_PARAMS;

        switch (BROWSER_COMMAND) {
            case GOOGLE_CHROME:
                driver = new ChromeDriver();
                break;
            default:
                driver = new ChromeDriver();
        }

        IDE = new com.codenvy.ide.IDE(factory_url_v1, driver);
        try {
            driver.manage().window().maximize();
            driver.get(factory_url_v1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        IDE.LOADER.waitClosed();
        IDE.FACTORY_URL.waitWelcomeIframe();
        IDE.selectMainFrame();
        IDE.FACTORY_URL.waitContentIntoInformationPanel(CONTENT_1);
    }
}
