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

import com.codenvy.ide.EnumBrowserCommand;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.Test;

import java.io.IOException;

/** @author Musienko Maxim */
public class EmptyRepositoryCloneTest extends GitServices {
    private static final String NAME_PROJECT = "checkoutToRemoteBranch";

    private String URI_EPTY_REPO = "git@github.com:exoinvitemain/checkoutToRemoteBranch.git";

    private final String OUTPUT_MESSAGE =
            "git@github.com:exoinvitemain/checkoutToRemoteBranch.git was successfully cloned.\n[INFO] Project preparing successful.";


    public EmptyRepositoryCloneTest() {
        if (BROWSER_COMMAND.equals(EnumBrowserCommand.FIREFOX))
            URI_EPTY_REPO = "git@github.com:codenvymain/emptyRemoteBranch.git";
    }

    @AfterClass
    public static void deleteProject() throws IOException {
        VirtualFileSystemUtils.delete(NAME_PROJECT);
    }

    @Test
    public void checkCloneemptyRepo() throws Exception {
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.EXPLORER.waitOpened();
        IDE.MENU.runCommand(MenuCommands.Git.GIT, MenuCommands.Git.CLONE_REPOSITORY);
        IDE.GIT.waitGitCloneForm();
        IDE.GIT.typeRemoteGitRepositoryURI(URI_EPTY_REPO);
        IDE.GIT.clickCloneButtonOnCloneRemoteRepositoryForm();
        IDE.OUTPUT.waitForSubTextPresent(NAME_PROJECT + "." + "git was successfully cloned.");
        IDE.SELECT_PROJECT_TYPE.waitSelectProjectTypeForm();
        IDE.SELECT_PROJECT_TYPE.selectPrjAndConfirm("default");
        IDE.EXPLORER.waitItemPresent(NAME_PROJECT);
        IDE.OPEN.openProject(NAME_PROJECT);
    }

}
