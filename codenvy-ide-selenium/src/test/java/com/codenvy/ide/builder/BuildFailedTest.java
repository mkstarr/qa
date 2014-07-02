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
package com.codenvy.ide.builder;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Build;
import com.codenvy.ide.core.Output.Messages;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertTrue;

/** @author Artem Zatsarynnyy */
public class BuildFailedTest extends BaseTest {

    private static final String PROJECT = BuildFailedTest.class.getSimpleName();

    public static final String PATH = "src/test/resources/org/exoplatform/ide/extension/maven/TestSpringProjectWithoutPOM.zip";

    protected static Map<String, Link> project;

    @BeforeClass
    public static void before() {
        try {
            project =
                    VirtualFileSystemUtils.importZipProject(PROJECT, PATH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void after() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

    @Test
    public void testBuildFailed() throws Exception {
        IDE.EXPLORER.waitOpened();
        // Open project
        IDE.OPEN.openProject(PROJECT);
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PROGRESS_BAR.waitProgressBarControl();
        IDE.OUTPUT.waitOpened();
        // IDE.PACKAGE_EXPLORER.waitAndClosePackageExplorer();
        // Building of project is started
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.BUILD_PROJECT);
        IDE.BUILD.waitOpened();
        String builderMessage = IDE.BUILD.getOutputMessage();
        assertTrue(builderMessage.startsWith(Build.Messages.BUILDING_PROJECT));

        // Wait until building is finished.
        IDE.STATUSBAR.waitBuildFailStatus();
        // --IDE.BUILD.waitOpened();

        // assertTrue(IDE.BUILD.getOutputMessage().startsWith(BEGINING_BUILD_FAIL_MESS));
        // Close Build project view because Output view is not visible
        IDE.OUTPUT.clickOnOutputTab();
        IDE.OUTPUT.waitOpened();
        // Get error message
        IDE.OUTPUT.waitForMessageShow(2, 15);
        String buildErrorMessage = IDE.OUTPUT.getOutputMessage(2);
        assertTrue(buildErrorMessage.endsWith(Messages.BUILD_FAILED));
    }
}
