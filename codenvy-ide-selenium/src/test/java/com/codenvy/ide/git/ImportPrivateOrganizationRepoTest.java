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
package com.codenvy.ide.git;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Git;

import org.junit.AfterClass;
import org.junit.Test;

import java.io.IOException;

/** @author Roman Iuvshin */
public class ImportPrivateOrganizationRepoTest extends GitServices {

    private static final String PROJECT = "qa";

    @AfterClass
    public static void tearDown() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void importPrivateOrganizationRepoTest() throws Exception {
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.EXPLORER.waitOpened();

        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.NEW, MenuCommands.Project.IMPORT_FROM_GITHUB);
        IDE.LOADER.waitClosed();
        IDE.IMPORT_FROM_GITHUB.waitImportFromGithubFormOpened();
        IDE.LOADER.waitClosed();
        IDE.IMPORT_FROM_GITHUB.selectGitHubAccount("codenvy");
        IDE.IMPORT_FROM_GITHUB.waitProjectInImportList(PROJECT);
        IDE.IMPORT_FROM_GITHUB.selectProjectByName(PROJECT);
        IDE.IMPORT_FROM_GITHUB.finishBtnClick();

        IDE.IMPORT_FROM_GITHUB.waitImportFromGithubFormClosed();
        IDE.IMPORT_FROM_GITHUB.waitCloningProgressFormClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.LOADER.waitClosed();

        IDE.OUTPUT.waitOpened();
        // The output panel should display message that repository has been cloned and project has been prepared successfully.
        IDE.OUTPUT.waitForSubTextPresent("git@github.com:codenvy/qa.git was successfully cloned.\n");
        IDE.OUTPUT.waitForSubTextPresent("[INFO] Project preparing successful.");
        new GitServices().waitAppearanceCollaborationFormAndClose();
        // check git menu
        checkMenuStateWhenRepositoryInited();

        // Root folder of project should be marked as "Git Root", all folder and files should be marked as in repository in Project
        // Explorer and Package Explorer.

        // checking icons
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(PROJECT, Git.GitIcons.ROOT);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("codenvy-ide-selenium", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("pom.xml", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithoutGitIcon("Maven Dependencies", Git.GitIcons.IN_REPOSITORY);
        //check icons in project explorer
        IDE.PACKAGE_EXPLORER.closePackageExplorer();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerClosed();
        IDE.EXPLORER.waitOpened();
        IDE.EXPLORER.waitElementWithGitIcon(PROJECT, Git.GitIcons.ROOT);
        IDE.EXPLORER.waitElementWithGitIcon("codenvy-ide-selenium", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("pom.xml", Git.GitIcons.IN_REPOSITORY);
    }

}
