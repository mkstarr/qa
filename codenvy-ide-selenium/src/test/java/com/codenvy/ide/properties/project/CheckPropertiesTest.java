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
package com.codenvy.ide.properties.project;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Test;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Properties;


/**
 * @author Musienko Maxim
 *
 */
public class CheckPropertiesTest extends PropertiesService {


    @AfterClass
    public static void tearDown() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT);

    }

    private static final String PROJECT                 = "phpChangeProperties";

    private final String        PHP_PROPERTIES_REPO_URI = "git@github.com:exoinvitemain/phpChangeProperties.git";

    @Test
    public void checkPhpPropertiesTest() throws Exception {
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(PHP_PROPERTIES_REPO_URI);
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(PHP_PROPERTIES_REPO_URI + " was successfully cloned.");
        IDE.EXPLORER.waitItemPresent(PROJECT);
        IDE.EXPLORER.waitElementWithGitIcon(PROJECT, Properties.ProjectIcon.PHP_PROJECT_ICON);
        checkPaaSStatesAfterCloneProject();
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.PROJECT_PROPERTIES);
        IDE.PROPERTIES.waitProjectPropertiesOpened();
        IDE.PROPERTIES.waitProjectPropertiesContainsText("PHP");

    }


}
