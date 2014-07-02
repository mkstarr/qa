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
package com.codenvy.ide.collaboration;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

/** @author Musienko Maxim</a> */
public class CollaborationCheckPropertyTest extends CollaborationService {
    private static final String PROJECT = CollaborationCheckPropertyTest.class.getSimpleName();

    private String onMode = "Codenvy Collaboration Mode\n" +
                            "true";

    private String offMode = "Codenvy Collaboration Mode\n" +
                             "false";


    protected static Map<String, Link> project;

    CollaborationActivityInProjectExplorerTest instance = new CollaborationActivityInProjectExplorerTest();


    private String FIRST_USER = USER_NAME.split("@")[0];

    private String SECOND_USER = NOT_ROOT_USER_NAME.split("@")[0];


    @BeforeClass
    public static void before() {

        try {
            project =
                    VirtualFileSystemUtils.importZipProject(PROJECT,
                                                            "src/test/resources/org/exoplatform/ide/operation/java/spring-with-big-sample" +
                                                            ".zip");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
            killSecondBrowser();
        } catch (Exception e) {
        }
    }

    @Test
    public void checkEnabledCollaborationProperties() throws Exception {
        //login as first user and enable collaboration mode
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.ENABLE_COLLABORATION_MODE);

        //run browser for invited user
        initSecondBrowser();
        // check collaboration properties as first user
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.PROJECT_PROPERTIES);
        IDE.PROPERTIES.waitProjectPropertiesContainsText(onMode);
        IDE.PROPERTIES.clickCancelButtonOnPropertiesForm();
        //check properties project with collab mode as invited user
        IDE2.EXPLORER.waitInDomPresent();
        IDE2.OPEN.openProject(PROJECT);
        IDE2.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE2.MENU.waitForMenuItemPresent(MenuCommands.Project.PROJECT, MenuCommands.Project.DISABLE_COLLABORATION_MODE);
        IDE2.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.PROJECT_PROPERTIES);
        IDE2.PROPERTIES.waitProjectPropertiesContainsText(onMode);
        IDE2.PROPERTIES.clickCancelButtonOnPropertiesForm();

    }

    /** @throws Exception */
    @Test
    public void checkDisabledCollaborationProperties() throws Exception {
        //second user disabble collab. properties , refresh browser and check it
        IDE2.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.DISABLE_COLLABORATION_MODE);
        driver2.navigate().refresh();
        IDE2.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE2.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.PROJECT_PROPERTIES);
        IDE2.PROPERTIES.waitProjectPropertiesContainsText(offMode);
        //first user logout, login again and check disabled collab. properties by inv. user
        IDE.LOGIN.logout();
        IDE.LOGIN.loginAsTenantRoot();
        IDE.EXPLORER.waitInDomPresent();
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.PROJECT_PROPERTIES);
        IDE.PROPERTIES.waitProjectPropertiesContainsText(offMode);
    }

}