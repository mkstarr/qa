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
package com.codenvy.ide.operation.java;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FindReplaceJavaTest extends ServicesJavaTextFuction {

    private static final String PROJECT  = FindReplaceJavaTest.class.getSimpleName();

    final String                fileName = "SimpleSum.java";

    @BeforeClass
    public static void setUp() {
        final String filePath = "src/test/resources/org/exoplatform/ide/operation/java/FormatTextTest.zip";

        try {
            Map<String, Link> project = VirtualFileSystemUtils.importZipProject(PROJECT, filePath);
        } catch (Exception e) {
        }
    }

    @After
    public void closeFindReplase() throws Exception {
        IDE.FINDREPLACE.closeView();
        IDE.FINDREPLACE.waitClosed();
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

    @Test
    public void findAndReplaceTest() throws Exception {
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.SHOW_SYNTAX_ERROR_HIGHLIGHTING);
        openJavaClassForFormat();

        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.FIND_REPLACE);
        IDE.FINDREPLACE.waitOpened();
        assertFalse(IDE.FINDREPLACE.isReplaceButtonEnabled());
        assertFalse(IDE.FINDREPLACE.isReplaceFindButtonEnabled());
        assertFalse(IDE.FINDREPLACE.isFindButtonEnabled());
        assertFalse(IDE.FINDREPLACE.isReplaceAllButtonEnabled());
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.Editor.FIND_REPLACE);

        IDE.FINDREPLACE.typeInFindField("int ss = sumForEdit (c, d);");

        assertTrue(IDE.FINDREPLACE.isFindButtonEnabled());
        assertTrue(IDE.FINDREPLACE.isReplaceAllButtonEnabled());
        assertFalse(IDE.FINDREPLACE.isReplaceFindButtonEnabled());
        assertFalse(IDE.FINDREPLACE.isReplaceButtonEnabled());

        IDE.FINDREPLACE.clickFindButton();
        assertTrue(IDE.FINDREPLACE.isFindButtonEnabled());
        assertTrue(IDE.FINDREPLACE.isReplaceAllButtonEnabled());
        assertTrue(IDE.FINDREPLACE.isReplaceFindButtonEnabled());
        assertTrue(IDE.FINDREPLACE.isReplaceButtonEnabled());
        IDE.FINDREPLACE.typeInReplaceField("int newVar = sumForEdit (c, d);");
        IDE.FINDREPLACE.clickReplaceButton();
        IDE.JAVAEDITOR.selectTab(fileName);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("int newVar = sumForEdit (c, d);");
    }

    @Test
    public void findAndReplaceAllWitNoneCaseSensitive() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);

        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.FIND_REPLACE);
        IDE.FINDREPLACE.waitOpened();
        assertFalse(IDE.FINDREPLACE.isReplaceButtonEnabled());
        assertFalse(IDE.FINDREPLACE.isReplaceFindButtonEnabled());
        assertFalse(IDE.FINDREPLACE.isFindButtonEnabled());
        assertFalse(IDE.FINDREPLACE.isReplaceAllButtonEnabled());
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.Editor.FIND_REPLACE);

        IDE.FINDREPLACE.typeInFindField("zero");
        IDE.FINDREPLACE.typeInReplaceField("replace");
        assertTrue(IDE.FINDREPLACE.isReplaceAllButtonEnabled());
        IDE.FINDREPLACE.clickReplaceAllButton();

        IDE.EDITOR.selectTab(fileName);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("String replace=\"\";");
    }

    @Test
    public void findWithNoneCaseSensitive() throws Exception {
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.FIND_REPLACE);
        IDE.FINDREPLACE.waitOpened();
        assertFalse(IDE.FINDREPLACE.isReplaceButtonEnabled());
        assertFalse(IDE.FINDREPLACE.isReplaceFindButtonEnabled());
        assertFalse(IDE.FINDREPLACE.isFindButtonEnabled());
        assertFalse(IDE.FINDREPLACE.isReplaceAllButtonEnabled());
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.Editor.FIND_REPLACE);

        IDE.FINDREPLACE.typeInFindField("ONE");

        assertTrue(IDE.FINDREPLACE.isFindButtonEnabled());
        IDE.FINDREPLACE.clickFindButton();
        assertFalse(IDE.FINDREPLACE.getFindResultText().equals("String not found."));

        IDE.FINDREPLACE.clickFindButton();
        assertFalse(IDE.FINDREPLACE.getFindResultText().equals("String not found."));

    }

    @Test
    public void replaseAllWithCaseSensitive() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.PROGRESS_BAR.waitProgressBarControlClose();

        // TODO IDE-2469 need for reset selection previous test
        IDE.JAVAEDITOR.moveCursorUp(5);

        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.FIND_REPLACE);
        IDE.FINDREPLACE.waitOpened();
        IDE.FINDREPLACE.clickCaseSensitiveField();
        assertFalse(IDE.FINDREPLACE.isReplaceButtonEnabled());
        assertFalse(IDE.FINDREPLACE.isReplaceFindButtonEnabled());
        assertFalse(IDE.FINDREPLACE.isFindButtonEnabled());
        assertFalse(IDE.FINDREPLACE.isReplaceAllButtonEnabled());
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.Editor.FIND_REPLACE);

        IDE.FINDREPLACE.typeInFindField("ONE");
        assertTrue(IDE.FINDREPLACE.isReplaceAllButtonEnabled());
        IDE.FINDREPLACE.typeInReplaceField("REPLACE_ONE");
        IDE.FINDREPLACE.clickReplaceAllButton();
        IDE.EDITOR.selectTab(fileName);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("REPLACE_ONE");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("String one");
    }

    @Test
    public void replaseAllWithNoneCaseSensitive() throws Exception {
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.GOTOLINE.goToLine(13);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + "d");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText("REPLACE_ONE");

        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.END.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("\n");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("String one = null;");

        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.FIND_REPLACE);
        IDE.FINDREPLACE.waitOpened();
        assertFalse(IDE.FINDREPLACE.isReplaceButtonEnabled());
        assertFalse(IDE.FINDREPLACE.isReplaceFindButtonEnabled());
        assertFalse(IDE.FINDREPLACE.isFindButtonEnabled());
        assertFalse(IDE.FINDREPLACE.isReplaceAllButtonEnabled());
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.Editor.FIND_REPLACE);

        IDE.FINDREPLACE.typeInFindField("ONE");
        IDE.FINDREPLACE.clickFindButton();
        assertTrue(IDE.FINDREPLACE.isReplaceFindButtonEnabled());
        IDE.FINDREPLACE.typeInReplaceField("replace");
        IDE.FINDREPLACE.clickReplaceAllButton();
        IDE.EDITOR.selectTab(fileName);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("String replace=\"\";");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("String replace = null;");
        IDE.JAVAEDITOR.waitAnyErrorMarkerIsAppear();
    }

    @Test
    public void findWithShortKey() throws Exception {
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.FIND_REPLACE);
        IDE.FINDREPLACE.waitOpened();
        assertFalse(IDE.FINDREPLACE.isReplaceButtonEnabled());
        assertFalse(IDE.FINDREPLACE.isReplaceFindButtonEnabled());
        assertFalse(IDE.FINDREPLACE.isFindButtonEnabled());
        assertFalse(IDE.FINDREPLACE.isReplaceAllButtonEnabled());
        IDE.TOOLBAR.waitForButtonDisabled(ToolbarCommands.Editor.FIND_REPLACE);

        IDE.FINDREPLACE.typeInFindField("String replace = null;");
        assertTrue(IDE.FINDREPLACE.isFindButtonEnabled());
        assertTrue(IDE.FINDREPLACE.isReplaceAllButtonEnabled());
        assertFalse(IDE.FINDREPLACE.isReplaceFindButtonEnabled());
        assertFalse(IDE.FINDREPLACE.isReplaceButtonEnabled());
        IDE.FINDREPLACE.clickFindButton();
        assertTrue(IDE.FINDREPLACE.isFindButtonEnabled());
        assertTrue(IDE.FINDREPLACE.isReplaceAllButtonEnabled());
        assertTrue(IDE.FINDREPLACE.isReplaceFindButtonEnabled());
        assertTrue(IDE.FINDREPLACE.isReplaceButtonEnabled());
        IDE.FINDREPLACE.typeInReplaceField("String replaceFix = null;");
        IDE.FINDREPLACE.clickReplaceButton();
        IDE.EDITOR.selectTab(fileName);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("String replaceFix = null;");
        IDE.JAVAEDITOR.waitAllMarkersIsDisappear();
    }

}