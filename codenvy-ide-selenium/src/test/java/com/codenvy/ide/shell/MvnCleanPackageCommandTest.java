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
package com.codenvy.ide.shell;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

/**
 * @author Musienko Maxim
 *
 */
public class MvnCleanPackageCommandTest extends BaseTest {

    private static final String PROJECT = MvnCleanPackageCommandTest.class.getSimpleName();

    private final String STATE_SHELL_AFTER_CD_COMMAND = USER_NAME + ":" + PROJECT + "$";

    private final String BUILD_STATUS_PREFIX_1 =
            LOGIN_URL_VFS.replace("com/", "com:") + "/ide/rest";

    private final String BUILD_STATUS_PREFIX_2 =
            //TODO
        /* USER_NAME + */"/maven/status";


    @BeforeClass
    public static void setUp() {
        final String filePath =
                "src/test/resources/org/exoplatform/ide/operation/file/autocomplete/JavaTestProject.zip";

        try {
            Map<String, Link> project = VirtualFileSystemUtils.importZipProject(PROJECT, filePath);
        } catch (Exception e) {
        }
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

    // TODO we should try to implement possibility download of a artifact
    @Test
    public void MvnCleanPackageCommandTest() throws Exception {
        IDE.SHELL.setIDEWindowHandle(driver.getWindowHandle());
        IDE.EXPLORER.waitOpened();
        IDE.SHELL.callShellFromIde();
        IDE.SHELL.typeAndExecuteCommand("ls");
        IDE.SHELL.waitContainsTextInShell(PROJECT);
        IDE.SHELL.typeAndExecuteCommand("cd " + PROJECT);
        IDE.SHELL.typeAndExecuteCommand("");
        IDE.SHELL.waitContainsTextAfterCommandInShell(STATE_SHELL_AFTER_CD_COMMAND);
        IDE.SHELL.typeAndExecuteCommand("mvn clean package");
        IDE.SHELL.waitContainsTextInShell(BUILD_STATUS_PREFIX_1);
        IDE.SHELL.waitContainsTextInShell(BUILD_STATUS_PREFIX_2);
    }
}
