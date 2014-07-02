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

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/** @author Musienko Maxim */
public class ShellGitStatusCommandTest extends BaseTest {
    protected static Map<String, Link> project;

    private static final String PROJECT = ShellGitStatusCommandTest.class.getSimpleName();


    private String gitStMess = "# On branch master\n" +
                               "nothing to commit, working directory clean";


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
    }

    @Test
    public void gitInitCommand() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.SHELL.setIDEWindowHandle(driver.getWindowHandle());
        IDE.SHELL.callShellFromIde();
        IDE.SHELL.waitContainsTextInShell("Welcome to Codenvy Shell");
        IDE.SHELL.waitContainsTextInShell(PROJECT + "$");
        IDE.SHELL.typeAndExecuteCommand("git status");
        IDE.SHELL.waitContainsTextInShell(gitStMess);
        IDE.SHELL.switchToIDE();
    }
}
