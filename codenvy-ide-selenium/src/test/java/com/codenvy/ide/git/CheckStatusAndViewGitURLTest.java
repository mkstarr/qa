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
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.Utils;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Git;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/** @author Roman Iuvshin */
public class CheckStatusAndViewGitURLTest extends GitServices {

    private static final String PROJECT = CheckStatusAndViewGitURLTest.class.getSimpleName();

    protected static Map<String, Link> project;

    private static final String NOTHING_TO_COMMIT_MESSAGE = "# On branch master\n" +
                                                            "nothing to commit, working directory clean";

    private static final String NEW_FILE_NAME = "newFile.css";

    private static final String UNTRACKED_FILE_MESSAGE = "# On branch master\n" +
                                                         "# Untracked files:\n" +
                                                         "#   (use \"git add ...\" to include in what will be committed)\n" +
                                                         "#\n" +
                                                         "# newFile.css";

    private static final String PROJECT_NAME = "ClonedProject";

    private static final String REMOTE_NAME = "neworigin";

    private final String uriRepo = "git@github.com:exoinvitemain/nodejsTestPrj.git";

    private static final String nodejsTestPrj = "nodejsTestPrj";


    private GitServices gitservice = new GitServices();


    @BeforeClass
    public static void before() {
        try {
            project = VirtualFileSystemUtils.importZipProject(PROJECT, "src/test/resources/org/exoplatform/ide/git/gitSampleProject.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT);
        VirtualFileSystemUtils.delete(nodejsTestPrj);
        VirtualFileSystemUtils.delete(PROJECT_NAME);
    }


    // TODO IDE-2762 after fix should be add check 'othing added to commit but untracked files present (use "git add" to track)' string
    // to UNTRACKED_FILE_MESSAGE variable

    @After
    public void closeOpenedPrj() throws UnsupportedEncodingException, InterruptedException {
        if (new Utils().checkOpenedProjects(driver)) {
            IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
        }
    }

    @Test
    public void checkStatus() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);

        // check git status
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.STATUS);
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(NOTHING_TO_COMMIT_MESSAGE);

        // create new file
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.CSS_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(NEW_FILE_NAME);
        IDE.FILE.clickCreateButton();
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.EDITOR.closeFile(NEW_FILE_NAME);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(NEW_FILE_NAME);

        // check git status again
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.STATUS);
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(UNTRACKED_FILE_MESSAGE);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(NEW_FILE_NAME, Git.GitIcons.UNTRACKED);
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);


        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(uriRepo);
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.OUTPUT.waitForSubTextPresent(nodejsTestPrj + "." + "git was successfully cloned.");

       // gitservice.waitAppearanceCollaborationFormAndClose();
        IDE.EXPLORER.waitForItem(nodejsTestPrj);
        IDE.EXPLORER.openItem(nodejsTestPrj + "/" + "README.md");
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "d");
        IDE.EDITOR.typeTextIntoEditor(Long.toString(System.currentTimeMillis()));

        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.waitNoContentModificationMark("README.md");
        IDE.EDITOR.forcedClosureFile("README.md");

        gitservice.addToIndex();
        gitservice.commmitChanges(new Date().toString());
        gitservice.puchChanges();
        IDE.OUTPUT.clickClearButton();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.STATUS);
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent("nothing to commit, working directory clean");
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
        assertTrue(NOTHING_TO_COMMIT_MESSAGE.equals(IDE.OUTPUT.getAllMessagesFromOutput()));
    }


    @Test
    public void viewGitURL() throws Exception {
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);

        // open git url form and get url for current repo
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.GIT_URL);
        IDE.GIT.waitGitUrlForm();
        String gitUrl = IDE.GIT.getGitUrl();
        IDE.GIT.clickCloseGitUrlForm();
        // close current project
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
        IDE.EXPLORER.waitOpened();
        // clone as new project
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(gitUrl);
        IDE.GIT.typeProjectNameInCloneRepositoryForm(PROJECT_NAME);
        IDE.GIT.typeRemoteNameInCloneRepositoryForm(REMOTE_NAME);
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(gitUrl + " was successfully cloned.");
        IDE.OUTPUT.waitForSubTextPresent("[INFO] Project preparing successful.");
        // wait project opened in package explorer
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT_NAME);
        // check git icons
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(PROJECT_NAME, Git.GitIcons.ROOT);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("src/main/java", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("src", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("pom.xml", Git.GitIcons.IN_REPOSITORY);

        // check git icons in project Explorer and return
        IDE.EXPLORER.selectProjectTab(PROJECT_NAME);
        expandNodesInProjectExplorer(PROJECT_NAME);
        IDE.EXPLORER.waitElementWithGitIcon(PROJECT_NAME, Git.GitIcons.ROOT);
        IDE.EXPLORER.waitElementWithGitIcon("src", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("main", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("java", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("helloworld", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("pom.xml", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.selectPackageExplorerTab();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT_NAME);


        // check remote repositories
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.REMOTES);
        IDE.GIT.waitRemoteRepositoriesForm();
        IDE.GIT.waitRemoteRepository(REMOTE_NAME, gitUrl);
        IDE.GIT.clickCloseRemoteRepositoryForm();
    }
}
