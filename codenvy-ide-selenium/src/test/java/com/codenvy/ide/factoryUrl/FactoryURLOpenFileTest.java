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

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.JsonHelper;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/** @author Musienko Maxim */
public class FactoryURLOpenFileTest extends BaseTest {

    private static final String PROJECT = FactoryURLOpenFileTest.class.getSimpleName();

    private final String rootOpenQueryParam = "&action=openproject&projectattributes.ptype=Spring&openfile=pom.xml";

    private final String classOpenQueryParam =
            "&action=openproject&projectattributes.ptype=Spring&openfile=src/main/java/helloworld/GreetingController.java";

    private final String invalidOpenQueryParam = "&openfile=main/java/helloworld/GreetingController.java";

    private final String pomFragment = "      <dependency>\n" +
                                       "         <groupId>junit</groupId>\n" +
                                       "         <artifactId>junit</artifactId>\n" +
                                       "         <version>3.8.1</version>\n" +
                                       "         <scope>test</scope>\n" +
                                       "      </dependency>";

    private final String classFragment = "      ModelAndView view = new ModelAndView(\"hello_view\");\n" +
                                         "      view.addObject(\"greeting\", result);\n" +
                                         "      return view;";

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
    public void сopyToMyWorkspaceWithChangesTest() throws Exception {
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


        String factory_url_v1 =
                PROTOCOL + "://" + IDE_HOST + "/factory?v=1.1" + "&projectattributes.pname=" + PROJECT + "&vcs=git&vcsurl=" +
                jsonHelper.getValueByKey("vcsurl") + rootOpenQueryParam;
        driver.get(factory_url_v1);
        IDE.FACTORY_URL.waitWelcomeIframe();
        IDE.selectMainFrame();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.EDITOR.waitTabPresent("pom.xml");
        IDE.EDITOR.selectTab("pom.xml");
        IDE.EDITOR.waitContentIsPresent(pomFragment);

        String factory_url_v1_2 =
                PROTOCOL + "://" + IDE_HOST + "/factory?v=1.1&projectattributes.pname=" + PROJECT  + "&vcs=git&vcsurl=" +
                jsonHelper.getValueByKey("vcsurl") + classOpenQueryParam;

        driver.get(factory_url_v1_2);
        IDE.FACTORY_URL.waitWelcomeIframe();
        IDE.selectMainFrame();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.EDITOR.waitTabPresent("GreetingController.java");
        IDE.LOADER.waitClosed();
        IDE.EDITOR.selectTab("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(classFragment);
////TODO we should check output message for invalid path
//        try {
//            driver.get(invalidOpenQueryParam);
//        } catch (WebDriverException e){
//            e.printStackTrace();
//        }
    }
}