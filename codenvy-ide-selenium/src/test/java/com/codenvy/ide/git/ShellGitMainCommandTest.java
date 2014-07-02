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
import org.openqa.selenium.Keys;

import static org.junit.Assert.fail;

/** @author Roman Iuvshin */
public class ShellGitMainCommandTest extends GitServices {

    private static final String PROJECT = ShellGitMainCommandTest.class.getSimpleName();

    private String afterAddFile =
            "# On branch master\n" +
            "# Changes to be committed:\n" +
            "#   (use \"git reset HEAD ...\" to unstage)\n" +
            "#\n" +
            "# modified:   pom.xml\n" +
            "#";

    private String commitOutputMess = "# On branch master\n" +
                                      "nothing to commit, working directory clean";

    private String finalMessage = "# On branch master\n" +
                                  "nothing to commit, working directory clean\n" +
                                  "# On branch master\n" +
                                  "# Changes to be committed:\n" +
                                  "#   (use \"git reset HEAD ...\" to unstage)\n" +
                                  "#\n" +
                                  "# modified:   pom.xml\n" +
                                  "#";

    @AfterClass
    public static void TearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
            fail("Can't delete test folders");
        }

    }

    @Test
    public void shellGitInitCommandTest() throws Exception {
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.WELCOME_PAGE.clickCreateNewProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.typeProjectName(PROJECT);
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectJavaSpringTechnology();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitProjectTemplateForm();
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectProjectTemplate("Simple Spring application.");
//TODO REMOVED DUE TO DISABLING JREBEL
//        IDE.CREATE_PROJECT_FROM_SCRATHC.waitForJRebelCheckbox();
//        IDE.CREATE_PROJECT_FROM_SCRATHC.clickOnJRebelCheckbox();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickFinishButton();
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        checkMenuStateWhenRepositoryNotInited();
        IDE.SHELL.setIDEWindowHandle(driver.getWindowHandle());
        IDE.SHELL.callShellFromIde();
        IDE.SHELL.waitContainsTextInShell("Welcome to Codenvy Shell");
        IDE.SHELL.waitContainsTextAfterCommandInShell(PROJECT + "$");
        IDE.SHELL.typeAndExecuteCommand("git init");
        Thread.sleep(1500);
        IDE.SHELL.typeAndExecuteCommand("git status");
        IDE.SHELL.waitContainsTextInShell(commitOutputMess);
        IDE.SHELL.switchToIDE();

        checkMenuStateWhenRepositoryInited();

        // checking icons in package explorer
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(PROJECT, Git.GitIcons.ROOT);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("src/main/java", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("src", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("pom.xml", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithoutGitIcon("Maven Dependencies", Git.GitIcons.IN_REPOSITORY);

        // check that nested nodes also has git icons
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src");
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("main", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("main");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("main");
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("webapp", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("webapp");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("webapp");
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("WEB-INF", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("index.jsp", Git.GitIcons.IN_REPOSITORY);

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("helloworld", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.IN_REPOSITORY);

        // check icons in project explorer
        IDE.PACKAGE_EXPLORER.closePackageExplorer();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerClosed();
        IDE.EXPLORER.waitOpened();
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.waitElementWithGitIcon(PROJECT, Git.GitIcons.ROOT);
        IDE.EXPLORER.waitElementWithGitIcon("src", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("pom.xml", Git.GitIcons.IN_REPOSITORY);
        IDE.TOOLBAR.runCommand("Package Explorer");
        IDE.PACKAGE_EXPLORER.waitPackageExplorerReOpened();
    }

    @Test
    public void shellGitStatusTest() throws Exception {

        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.REFRESH);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(PROJECT, Git.GitIcons.ROOT);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.STATUS);
        IDE.OUTPUT.waitForSubTextPresent(commitOutputMess);
    }

    @Test
    public void shellGitAddFileCheck() throws Exception {
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("pom.xml");
        IDE.EDITOR.waitActiveFile();
        IDE.GOTOLINE.goToLine(7);
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "d");
        IDE.EDITOR.waitEditorWillNotContainSpecifiedText("<version>1.0-SNAPSHOT</version>");
        IDE.EDITOR.typeTextIntoEditor("<version>2.0-SNAPSHOT</version>" + "\n");
        IDE.EDITOR.waitContentIsPresent("<version>2.0-SNAPSHOT</version>");
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.EDITOR.waitNoContentModificationMark("pom.xml");
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.EDITOR.closeFile("pom.xml");
        IDE.SHELL.switchOnShellWindow();
        IDE.SHELL.typeAndExecuteCommand("git add pom.xml");
        IDE.SHELL.typeAndExecuteCommand("git status");
        IDE.SHELL.waitContainsTextInShell(afterAddFile);
        IDE.SHELL.switchToIDE();
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.STATUS);
        IDE.OUTPUT.waitForSubTextPresent(afterAddFile);
    }

    @Test
    public void shellCommitCheck() throws Exception {
        IDE.SHELL.switchOnShellWindow();
        IDE.SHELL.typeAndExecuteCommand("git commit -m\"commit check\" ");
        IDE.SHELL.waitContainsTextInShell("[master ");
        IDE.SHELL.waitContainsTextInShell("commit check");
        IDE.SHELL.waitContainsTextInShell("@gmail.com:" + PROJECT);
        IDE.SHELL.switchToIDE();
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.STATUS);
        IDE.OUTPUT.waitForSubTextPresent(commitOutputMess);
        IDE.LOADER.waitClosed();
    }

    @Test
    public void shellGitAddCommand() throws Exception {
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("pom.xml");
        IDE.EDITOR.waitActiveFile();
        IDE.GOTOLINE.goToLine(7);
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "d");
        IDE.EDITOR.waitEditorWillNotContainSpecifiedText("<version>1.2-SNAPSHOT</version>");
        IDE.EDITOR.typeTextIntoEditor("<version>1.0-SNAPSHOT</version>" + "\n");
        IDE.EDITOR.waitContentIsPresent("<version>1.0-SNAPSHOT</version>");
        IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.EDITOR.waitNoContentModificationMark("pom.xml");
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.SHELL.switchOnShellWindow();
        IDE.SHELL.typeAndExecuteCommand("clear");
        Thread.sleep(1000);
        IDE.SHELL.typeAndExecuteCommand("git add");
        IDE.SHELL.waitContainsTextInShell(USER_NAME + ":" + PROJECT + "$ " + "git add");
        IDE.SHELL.typeAndExecuteCommand("git status");
        IDE.SHELL.waitContainsTextInShell(afterAddFile);
        IDE.SHELL.switchToIDE();
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.STATUS);
        IDE.OUTPUT.waitForSubTextPresent(finalMessage);
    }


}
