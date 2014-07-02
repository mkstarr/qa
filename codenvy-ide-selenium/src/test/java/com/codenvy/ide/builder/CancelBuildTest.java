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
package com.codenvy.ide.builder;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Build;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertTrue;

/** @author Roman Iuvshin */
public class CancelBuildTest extends BaseTest {

    private static final String PROJECT = CancelBuildTest.class.getSimpleName();

    public static final String PATH = "src/test/resources/org/exoplatform/ide/extension/maven/TestSpringProjectWithPOM.zip";

    protected static Map<String, Link> project;

    @BeforeClass
    public static void before() {
        try {
            project =
                    VirtualFileSystemUtils.importZipProject(PROJECT, PATH);
            //need for full complete import zip folder in DavFs
        } catch (Exception e) {
        }
    }

    @AfterClass
    public static void after() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

    @Test
    public void cancelBuildTest() throws Exception {
        IDE.EXPLORER.waitOpened();

        // Open project
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.LOADER.waitClosed();
        // Building of project is started

        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.BUILD_PROJECT);
        IDE.BUILD.waitOpened();
        String builderMessage = IDE.BUILD.getOutputMessage();
        assertTrue(builderMessage.startsWith(Build.Messages.BUILDING_PROJECT));

        //wait progress bar and cancel build button
        IDE.PROGRESS_BAR.waitProgressBarControl();
        IDE.PROGRESS_BAR.waitCancelButtonAppear();

        // cancel build and check that build was cancelled
        IDE.PROGRESS_BAR.clickOnCancelBuildButton();

        IDE.PROGRESS_BAR.waitCancelButtonDisappear();
        IDE.PROGRESS_BAR.waitBuildCanceledBar();

        IDE.BUILD.waitBuilderContainText("Result: Cancelled");

        // check progress tab
        IDE.PROGRESS_BAR.clickProgressBarControl();
        IDE.PROGRESS_BAR.waitProgressBarView();
        assertTrue(IDE.PROGRESS_BAR.getViewText().contains("Build cancelled"));
    }

}
