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

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.core.Git;

/** @author Roman Iuvshin */
public class GitServices extends BaseTest {

    private final String addedToIndexMessage = "[INFO] Successfully added to index.";

    private final String commitMessage = "Committed with revision";

    private final String nothingToCommitMessage = "# On branch master\n" +
                                                  "nothing to commit (working directory clean)";

    private final String repoInitMessage = "[INFO] Repository was successfully initialized.";

    /** Check for menu buttons when git repository is not inited */
    public void checkMenuStateWhenRepositoryNotInited() {
        // checking disabled buttons
        IDE.MENU.waitCommandDisabled(MenuCommands.Git.GIT, MenuCommands.Git.ADD);
        IDE.MENU.waitCommandDisabled(MenuCommands.Git.GIT, MenuCommands.Git.RESET);
        IDE.MENU.waitCommandDisabled(MenuCommands.Git.GIT, MenuCommands.Git.REMOVE);
        IDE.MENU.waitCommandDisabled(MenuCommands.Git.GIT, MenuCommands.Git.COMMIT);
        IDE.MENU.waitCommandDisabled(MenuCommands.Git.GIT, MenuCommands.Git.BRANCHES);
        IDE.MENU.waitCommandDisabled(MenuCommands.Git.GIT, MenuCommands.Git.MERGE);
        IDE.MENU.waitCommandDisabled(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE);
        IDE.MENU.waitCommandDisabled(MenuCommands.Git.GIT, MenuCommands.Git.RESET_INDEX);
        IDE.MENU.waitCommandDisabled(MenuCommands.Git.GIT, MenuCommands.Git.DELETE_REPOSITORY);
        IDE.MENU.waitCommandDisabled(MenuCommands.Git.GIT, MenuCommands.Git.SHOW_HISTORY);
        IDE.MENU.waitCommandDisabled(MenuCommands.Git.GIT, MenuCommands.Git.STATUS);
        IDE.MENU.waitCommandDisabled(MenuCommands.Git.GIT, MenuCommands.Git.GIT_URL);
        // checking enabled buttons
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.INITIALIZE_REPOSITORY);
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
    }

    /** Check for menu buttons when git repository is inited */
    public void checkMenuStateWhenRepositoryInited() {
        // checking disabled buttons
        IDE.MENU.waitCommandDisabled(MenuCommands.Git.GIT, MenuCommands.Git.INITIALIZE_REPOSITORY);
        // checking enabled buttons
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.ADD);
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.RESET);
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.REMOVE);
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.COMMIT);
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.BRANCHES);
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.MERGE);
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE);
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.RESET_INDEX);
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.DELETE_REPOSITORY);
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.SHOW_HISTORY);
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.STATUS);
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.GIT_URL);
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
    }


    /** Check for menu buttons when git repository is inited for temporary Ws */
    public void checkMenuStateWhenRepositoryInitedInTempWs() {
        // checking disabled buttons
        IDE.MENU.waitCommandDisabled(MenuCommands.Git.GIT, MenuCommands.Git.INITIALIZE_REPOSITORY);
        // checking enabled buttons
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.ADD);
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.RESET);
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.REMOVE);
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.COMMIT);
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.BRANCHES);
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.MERGE);
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE);
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.RESET_INDEX);
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.DELETE_REPOSITORY);
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.SHOW_HISTORY);
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.STATUS);
        IDE.MENU.waitCommandDisabled(MenuCommands.Git.GIT, MenuCommands.Git.GIT_URL);
        IDE.MENU.waitCommandEnabled(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
    }


    public void openNodesAndCheckIcons(String project) {
        // checking icons
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(project, Git.GitIcons.ROOT);
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
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("NewClass.java", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.IN_REPOSITORY);
    }

    public void openNodesAndCheckIconsInDefaultSpringProject(String project) {
        // checking icons
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(project, Git.GitIcons.ROOT);
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
    }

    protected void cloneProject(String project, String clonedURI) throws Exception {
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(clonedURI);
        IDE.GIT.typeProjectNameInCloneRepositoryForm(project);
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.GIT.waitGitCloneFormDisappear();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(project);
        waitAppearanceCollaborationFormAndClose();
    }

    /** @param currentWin */
    private void switchToGithubLoginWindow(String currentWin) {
        for (String handle : driver.getWindowHandles()) {
            if (currentWin.equals(handle)) {

            } else {
                driver.switchTo().window(handle);
                break;
            }
        }
    }

    public void  waitAppearanceCollaborationFormAndClose() throws Exception {
        IDE.INVITE_FORM.waitInviteDevelopersOpened();
        IDE.LOADER.waitClosed();
        IDE.INVITE_FORM.clickOnCancelButton();
        IDE.INVITE_FORM.waitInviteDevelopersClosed();
    }

    protected static void createNewFileAndPushItToGithub(String fileName,
                                                         String projectName,
                                                         String AddedToIndexMessage,
                                                         String commitMessage,
                                                         String commitOutputMessage1,
                                                         String commitOutputMessage2,
                                                         String pushMessage)
            throws Exception {
        // ///////////////////////
        // create new file
        // check that file not present
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent(fileName);
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(projectName);
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.CSS_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(fileName);
        IDE.FILE.clickCreateButton();
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.EDITOR.closeFile(fileName);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(fileName);
        // add to index
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(projectName);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.ADD);
        IDE.GIT.waitAddToIndexForm();
        IDE.GIT.clickAddButtonInAddToIndexForm();
        IDE.LOADER.waitClosed();
        // check output
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(AddedToIndexMessage);
        // commit
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.COMMIT);
        IDE.GIT.waitCommitForm();
        IDE.GIT.typeCommitMessage("add new file for test" + commitMessage);
        IDE.GIT.clickCommitButton();
        // check output message
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(commitOutputMessage1);
        IDE.OUTPUT.waitForSubTextPresent(commitOutputMessage2);
        // check git icons in package explorer
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(fileName, Git.GitIcons.IN_REPOSITORY);
        // push
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PUSH);
        IDE.GIT.waitPushView();
        IDE.GIT.clickPushBtn();
        IDE.GIT.waitPushFormIsClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(pushMessage);
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
    }

    /**
     * expand main nodes on project tree for default string project
     *
     * @param project
     * @throws Exception
     */
    protected void expandNodesInProjectExplorer(String project) throws Exception {
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.EXPLORER.waitForItem(project);
        IDE.EXPLORER.waitForItem(project + "/" + "src");
        IDE.EXPLORER.waitForItem(project + "/" + "pom.xml");
        IDE.EXPLORER.openItem(project + "/" + "src");
        IDE.EXPLORER.waitForItem(project + "/" + "src" + "/" + "main");
        IDE.EXPLORER.openItem(project + "/" + "src" + "/" + "main");
        IDE.EXPLORER.waitForItem(project + "/" + "src" + "/" + "main" + "/" + "java");
        IDE.EXPLORER.waitForItem(project + "/" + "src" + "/" + "main" + "/" + "webapp");
        IDE.EXPLORER.openItem(project + "/" + "src" + "/" + "main" + "/" + "java");
        IDE.EXPLORER.waitForItem(project + "/" + "src" + "/" + "main" + "/" + "java" + "/" + "helloworld");
        IDE.EXPLORER.openItem(project + "/" + "src" + "/" + "main" + "/" + "java" + "/" + "helloworld");
        IDE.EXPLORER.waitForItem(project + "/" + "src" + "/" + "main" + "/" + "java" + "/" + "helloworld"
                                 + "/" + "GreetingController.java");
        IDE.EXPLORER.openItem(project + "/" + "src" + "/" + "main" + "/" + "webapp");
        IDE.EXPLORER.waitForItem(project + "/" + "src" + "/" + "main" + "/" + "webapp" + "/" + "WEB-INF");
        IDE.EXPLORER.waitForItem(project + "/" + "src" + "/" + "main" + "/" + "webapp" + "/" + "index.jsp");
        IDE.EXPLORER.openItem(project + "/" + "src" + "/" + "main" + "/" + "webapp" + "/" + "WEB-INF");
        IDE.EXPLORER.waitForItem(project + "/" + "src" + "/" + "main" + "/" + "webapp" + "/" + "WEB-INF" + "/" + "jsp");
        IDE.EXPLORER.openItem(project + "/" + "src" + "/" + "main" + "/" + "webapp" + "/" + "WEB-INF" + "/" + "jsp");
        IDE.EXPLORER.waitForItem(project + "/" + "src" + "/" + "main" + "/" + "webapp" + "/" + "WEB-INF" + "/jsp" + "/hello_view.jsp");
    }

    /** check init git icons in default simple spring project */
    protected void chkInitGitIconsInExpandedSpngDefPrjInExplorer(String currentProject) {
        IDE.EXPLORER.waitElementWithGitIcon(currentProject, Git.GitIcons.ROOT);
        IDE.EXPLORER.waitElementWithGitIcon("src", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("main", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("java", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("helloworld", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("WEB-INF", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("jsp", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("webapp", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("hello_view.jsp", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("spring-servlet.xml", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("web.xml", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("index.jsp", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("pom.xml", Git.GitIcons.IN_REPOSITORY);
    }

    /**
     * run Git->Remote->Push, wait push window, click push button. wait closing the form, wait while progress bar with push message
     * disappear
     *
     * @throws Exception
     *         timeout exceptions for appear/disappear elements,
     *         and webdriver exceptions
     */
    public void puchChanges() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PUSH);
        IDE.GIT.waitPushView();
        IDE.LOADER.waitClosed();
        IDE.GIT.clickPushBtn();
        IDE.GIT.waitPushFormIsClosed();
        IDE.OUTPUT.waitOpened();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
    }

    /**
     * run menu Git -> Add
     * click on add to index button
     * wait output menu with label 'added to index'
     *
     * @throws Exception
     */
    public void addToIndex() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.ADD);
        IDE.GIT.waitAddToIndexForm();
        IDE.GIT.clickAddButtonInAddToIndexForm();
        IDE.LOADER.waitClosed();
        // check output
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(addedToIndexMessage);
    }

    /**
     * run menu Git -> Commit
     * type user commit message, click commit button, wait disappear commit form
     * wait standart commit message in output panel
     *
     * @param commitMess
     *         user commit message
     * @throws Exception
     */
    public void commmitChanges(String commitMess) throws Exception {
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.COMMIT);
        IDE.GIT.waitCommitForm();
        IDE.GIT.typeCommitMessage(commitMess);
        IDE.GIT.clickCommitButton();
        // check output message
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(commitMessage);

    }

    /**
     * run menu Git -> Commit
     * click ok button wait appearance init message
     */
    public void gitInit() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.INITIALIZE_REPOSITORY);
        IDE.GIT.waitInitializeLocalRepositoryForm();
        IDE.GIT.clickOkInitializeLocalRepository();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitOpened();
        IDE.OUTPUT.waitForSubTextPresent(repoInitMessage);
    }

    protected void selectRootAndRefresh() {
        IDE.PACKAGE_EXPLORER.selectRoot();
        try {
            IDE.TOOLBAR.runCommand(ToolbarCommands.File.REFRESH);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


}
