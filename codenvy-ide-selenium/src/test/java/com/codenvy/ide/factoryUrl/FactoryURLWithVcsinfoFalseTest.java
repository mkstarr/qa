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
package com.codenvy.ide.factoryUrl;

import com.codenvy.ide.JsonHelper;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.git.GitServices;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/** @author Roman Iuvshin */

public class FactoryURLWithVcsinfoFalseTest extends GitServices {

    private static final String PROJECT = "vcsinfoFalse";

    private static final String FACTORY_PARAMS = "&action=openproject&ptype=Spring&vcsinfo=false";

    private static Map<String, Link> project;

    private JsonHelper jsonHelper;

    @BeforeClass
    public static void before() {
        try {
            project =
                    VirtualFileSystemUtils
                            .importZipProject(PROJECT,
                                              "src/test/resources/org/exoplatform/ide/extension/maven/TestSpringProjectWithPOM.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() throws IOException, InterruptedException, TimeoutException, MessagingException {
        if (VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT).getStatusCode() == 200) {
            VirtualFileSystemUtils.delete(PROJECT);
        }
    }

    @Test
    public void factoryURLWithVcsinfoFalseTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.MENU.runCommand(MenuCommands.Share.SHARE, MenuCommands.Share.FACTORY_URL);
        IDE.FACTORY_URL.waitFactoryURLFormOpened();
        IDE.FACTORY_URL.clickOnNextFactoryButton();
        IDE.FACTORY_URL.waitForFinishCreationPage();
        String factoryURL = IDE.FACTORY_URL.getDirectSharingURL();
        IDE.FACTORY_URL.clickOnFinishButtonInFactoryURLForm();
        IDE.LOGIN.logout();
        jsonHelper = new JsonHelper(factoryURL);
        // building v1 factory url
        String factory_url_v1 =
                PROTOCOL + "://" + IDE_HOST + "/factory?v=1.0&pname=" + PROJECT + "&wname=" + TENANT_NAME + "&vcs=git&vcsurl=" +
                jsonHelper.getValueByKey("vcsurl") + FACTORY_PARAMS;


        driver.get(factory_url_v1);
        IDE.LOADER.waitClosed();
        IDE.FACTORY_URL.waitWelcomeIframe();
        IDE.selectMainFrame();
        // do some changes
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        checkMenuStateWhenRepositoryNotInited();
    }
}
