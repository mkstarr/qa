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
package com.codenvy.ide.debug;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Build;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/** @author Musienko Maxim */

public class DebugCheckStopedDebugAfterClosingProjectTest extends DebuggerServices {
    private static final String PROJECT = DebugCheckStopedDebugAfterClosingProjectTest.class.getSimpleName();

    protected static Map<String, Link> project;

    @BeforeClass
    public static void before() {

        try {
            project =
                    VirtualFileSystemUtils.importZipProject(PROJECT,


                                                            "src/test/resources/org/exoplatform/ide/debug/change-variable-proj.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @After
    public void tearDown() throws IOException, InterruptedException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void setBreackPointTest() throws Exception {
        //step 1 run debug app and wait while build finish
        openProjectAndClass(PROJECT);

        //step 2 run debug app and wait while build finish
        IDE.MENU.runCommand(MenuCommands.Run.RUN, MenuCommands.Run.DEBUG_APPLICATION);
        IDE.BUILD.waitOpened();
        String builderMessage = IDE.BUILD.getOutputMessage();
        assertTrue(builderMessage.startsWith(Build.Messages.BUILDING_PROJECT));
        IDE.PROGRESS_BAR.waitProgressBarControlClose();

        //step 3 check opening output panel and wait debug control
        IDE.DEBUGER.waitOpened();
        IDE.DEBUGER.waitTabOfDebuger();
        IDE.DEBUGER.clickOnDebugerTab();
        IDE.DEBUGER.waitTabOfDebuger();
        isDebugerButtonsWithoutBreakPoints();
        IDE.JAVAEDITOR.selectTab("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        //TODO after resolve issue IDE-2179 method should be completed
        IDE.DEBUGER.setBreakPoint(22);
        IDE.DEBUGER.waitToggleBreackPointIsSet(22);
        IDE.DEBUGER.waitBreackPointsTabContainetWithSpecifiedValue("[line :");
        assertTrue(IDE.DEBUGER.getTextFromBreackPointTabContainer().contains(
                "helloworld.GreetingController - [line : 22]"));

        //step 3 close the curent project and check closing
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
        IDE.ASK_DIALOG.waitOpened();
        IDE.ASK_DIALOG.clickYes();
        IDE.ASK_DIALOG.waitClosed();
        IDE.DEBUGER.waitDebugerIsClosed();
        IDE.OUTPUT.waitForSubTextPresent("stopped.");
        assertTrue(IDE.OUTPUT.getAllMessagesFromOutput().endsWith("stopped."));
        IDE.OUTPUT.clickClearButton();
    }

    /** @throws Exception */
    private void openProjectAndClass(String project) throws Exception {
        IDE.OPEN.openProject(project);
        if (IDE.ASK_DIALOG.isOpened()) {
            IDE.ASK_DIALOG.clickYes();
            IDE.ASK_DIALOG.waitClosed();
        }
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("GreetingController.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
    }

}
