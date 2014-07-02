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
package com.codenvy.ide.collaboration;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.fail;

/** @author Zaryana Dombrovskaya */

public class BrokenJavaEditorAfterEnableCollaborationModeTest extends BaseTest {
    private static final String PROJECT = BrokenJavaEditorAfterEnableCollaborationModeTest.class.getSimpleName();

    protected static Map<String, Link> project;

    @AfterClass
    public static void TearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
            fail("Can't create test folders");
        }
    }

    @Test
    public void checkNumberOfGutterInJavaEditorTest() throws Exception {
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.WELCOME_PAGE.clickCreateNewProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
        IDE.CREATE_PROJECT_FROM_SCRATHC.typeProjectName(PROJECT);
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectJavaLibraryTechnology();
        IDE.CREATE_PROJECT_FROM_SCRATHC.clickNextButton();
        IDE.CREATE_PROJECT_FROM_SCRATHC.waitProjectTemplateForm();
        IDE.CREATE_PROJECT_FROM_SCRATHC.selectProjectTemplate("Simple JAR project.");

        IDE.CREATE_PROJECT_FROM_SCRATHC.clickFinishButton();
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("hello");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("hello");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("SayHello.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("SayHello.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.ENABLE_COLLABORATION_MODE);
        Thread.sleep(2000);
        IDE.JAVAEDITOR.checkNumberOfGutter();
    }
}

