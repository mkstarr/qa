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
package com.codenvy.ide.git;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.io.IOException;

/** @author Musienko Maxim */
public class CheckoutToRemoteBranchTest extends GitServices {

    private static final String PROJECT = "CheckoutToRemoteBranchTest";

    private static final String PROJECT2 = "CheckoutToRemoteBranchTest2";

    private final String AUTOSAVE_FILE = "GreetingController.java";

    private final String NONE_AUTOSAVE_FILE = "index.jsp";

    private final String MASTER_BRANCH = "master";

    private final String ORIGIN_MASTER_BRANCH = "origin/master";

    private final String SECOND_REMOTE_BRANCH = "origin/second_remote_branch";

    private final String SECOND_BRANCH = "second_remote_branch";

    private final String CLONE_URI = "git@github.com:exoinvitemain/CheckoutToRemoteBranchTest.git";

    private final String PUSHED_BRANCH_VALUE = "refs/heads/second_remote_branch";

    private final String GIT_STATUS_MESS_3_STEP = "# On branch " + SECOND_BRANCH + "\nnothing to commit, working directory clean";

    private final String PUSHED_MESSAGE =
            "Successfully pushed to git@github.com:exoinvitemain/CheckoutToRemoteBranchTest.git";

    private final String COMMIT_MESS = "commitchk_remote";

    private String uniqueValue;

    private String uniqueValue2;

    @AfterClass
    public static void tearDown() throws IOException {

        if (VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT).getStatusCode() == 200 &&
            VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT2).getStatusCode() == 200) {
            VirtualFileSystemUtils.delete(PROJECT);
            VirtualFileSystemUtils.delete(PROJECT2);
        } else if (VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT).getStatusCode() == 200) {
            VirtualFileSystemUtils.delete(PROJECT);
        }

    }


    @Test
    public void checkoutToRemoteBranch() throws Exception {

        uniqueValue = String.valueOf(System.currentTimeMillis()) + "\n";
        uniqueValue2 = String.valueOf(System.currentTimeMillis()) + "\n";
        // preconditions
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.EXPLORER.waitOpened();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(CLONE_URI);
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        // TODO If run on another tenant
        //  IDE.INVITE_FORM.waitInviteDevelopersOpened();
        //       IDE.INVITE_FORM.clickOnCancelButton();
//              IDE.INVITE_FORM.waitInviteDevelopersClosed();

        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);

        // step 1 open branches and check him
        checkoutToSecondRemoteBranch();

        // step 3 git status checking
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.STATUS);
        IDE.OUTPUT.waitForSubTextPresent(GIT_STATUS_MESS_3_STEP);

        // step 4 check content in package and project explorer
        openNodesAndCheckIconsInDefaultSpringProject(PROJECT);
        IDE.EXPLORER.selectProjectTab(PROJECT);
        expandNodesInProjectExplorer(PROJECT);
        chkInitGitIconsInExpandedSpngDefPrjInExplorer(PROJECT);
        IDE.PACKAGE_EXPLORER.clickOnRightScroller();
        IDE.PACKAGE_EXPLORER.selectPackageExplorerTab();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);

        // step 5
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PULL);
        IDE.GIT.waitGitPullForm();
        IDE.GIT.waitPullInRmtBranchFieldValueIsSet(SECOND_BRANCH);
        IDE.GIT.waitPullInLocBranchFieldValueIsSet(SECOND_BRANCH);

        // step 6
        IDE.GIT.clickPullButtonOnGitPullForm();
        IDE.OUTPUT.waitForSubTextPresent("Already up-to-date.");

        // step 7
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(AUTOSAVE_FILE);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.GOTOLINE.goToLine(1);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + "d");

        IDE.JAVAEDITOR.typeTextIntoJavaEditor(uniqueValue);
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.closeFile(AUTOSAVE_FILE);

        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(NONE_AUTOSAVE_FILE);
        IDE.EDITOR.waitTabPresent(NONE_AUTOSAVE_FILE);
        IDE.GOTOLINE.goToLine(1);
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "d");
        IDE.EDITOR.typeTextIntoEditor(uniqueValue2);
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);
        IDE.EDITOR.waitNoContentModificationMark(NONE_AUTOSAVE_FILE);
        addToIndexAndCommitAll("chk_remote", PROJECT);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.REMOTE, MenuCommands.Git.PUSH);
        IDE.GIT.waitPushView();

        IDE.GIT.waitPushFromBranchFieldIsSetVal(PUSHED_BRANCH_VALUE);
        IDE.GIT.waitPushToBranchFieldIsSetVal(PUSHED_BRANCH_VALUE);

        //step 8
        IDE.GIT.clickPushBtn();
        IDE.OUTPUT.waitForSubTextPresent(PUSHED_MESSAGE);

        //step 9
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
        IDE.ASK_DIALOG.waitOpened();
        IDE.ASK_DIALOG.clickYes();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerClosed();

        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(CLONE_URI);
        IDE.GIT.typeProjectNameInCloneRepositoryForm(PROJECT2);
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT2);
        checkoutToSecondRemoteBranch();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.SHOW_HISTORY);
        validateMainDataInHist();
        openNodesAndCheckIconsInDefaultSpringProject(PROJECT2);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(NONE_AUTOSAVE_FILE);
        IDE.EDITOR.waitContentIsPresent(uniqueValue2);
    }

    private void validateMainDataInHist() {
        IDE.GIT.waitCommitInHistoryView(COMMIT_MESS);
        IDE.GIT.waitDiffContainerContainText("package helloworld;");
        IDE.GIT.waitDiffContainerContainText("+" + uniqueValue);
        IDE.GIT.waitDiffContainerContainText("package helloworld;");
        IDE.GIT.waitDiffContainerContainText("+" + uniqueValue2);
        IDE.GIT.waitDiffContainerContainText("--- a/src/main/webapp/index.jsp");
        IDE.GIT.waitDiffContainerContainText("+++ b/src/main/webapp/index.jsp");
    }

    private void checkoutToSecondRemoteBranch() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.BRANCHES);
        IDE.GIT.waitBranhesOpened();
        IDE.GIT.waitBranchIsCheckout(MASTER_BRANCH);
        IDE.GIT.waitFieldWithNewNameBranchForm(ORIGIN_MASTER_BRANCH);
        IDE.GIT.waitFieldWithNewNameBranchForm(SECOND_REMOTE_BRANCH);

        // step 2 checkout to remote branch
        IDE.GIT.selectBranchInList(SECOND_REMOTE_BRANCH);
        IDE.GIT.clickCheckOutBtn();
        // check items
        IDE.GIT.waitBranchIsCheckout(SECOND_BRANCH);
        IDE.GIT.waitFieldWithNewNameBranchForm(SECOND_REMOTE_BRANCH);
        IDE.GIT.waitFieldWithNewNameBranchForm(ORIGIN_MASTER_BRANCH);
        IDE.GIT.waitFieldWithNewNameBranchForm(MASTER_BRANCH);
        IDE.LOADER.waitClosed();
        IDE.GIT.clickCloseBranchBtn();
    }


    private void addToIndexAndCommitAll(String commitMessage, String Project) throws Exception {
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(Project);
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.ADD);
        IDE.GIT.waitAddToIndexForm();
        IDE.GIT.clickAddButtonInAddToIndexForm();
        IDE.LOADER.waitClosed();
        IDE.OUTPUT.waitForSubTextPresent("Successfully added to index.");
        IDE.GIT.waitAddToIndexFormDisappear();
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(Project);

        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.COMMIT);
        IDE.GIT.waitCommitForm();
        IDE.GIT.typeCommitMessage("commit" + commitMessage);
        IDE.GIT.clickCommitButton();
        IDE.GIT.waitCommitFormDisappear();

        IDE.OUTPUT.waitForSubTextPresent("Committed with revision");
    }

}
