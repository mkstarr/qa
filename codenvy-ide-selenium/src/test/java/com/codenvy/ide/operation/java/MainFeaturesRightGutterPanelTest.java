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
package com.codenvy.ide.operation.java;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.util.Map;

/**
 * @author Musienko Maxim
 *
 */
public class MainFeaturesRightGutterPanelTest extends ServicesJavaTextFuction {

    private static final String PROJECT             = MainFeaturesRightGutterPanelTest.class.getSimpleName();

    private String              AUTOGENERATE_METHOD =
                                                      "/**\n * \n */\nprivate void autoGenMethod()\n{\n   // TODO Auto-generated method stub\n\n}";

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

    @Test
    public void unUsedImportsTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.SHOW_SYNTAX_ERROR_HIGHLIGHTING);
        openJavaClassForUpdateDependencyTest();
        IDE.JAVAEDITOR.waitWarningMarkerPresentInPosition(6);
        IDE.JAVAEDITOR.waitWarningMarkerPresentInPosition(7);
        IDE.JAVAEDITOR.moveCursorToMarker(6);
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("The import java.util.HashMap is never used");

        IDE.GOTOLINE.goToLine(4);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + "d");

        IDE.JAVAEDITOR
                      .waitWhileJavaEditorWillNotContainSpecifiedText("import org.springframework.web.servlet.mvc.Controller;");
        IDE.JAVAEDITOR.waitErrorMarkerPresentInPosition(15);
        IDE.JAVAEDITOR.waitErrorMarkerPresentInPosition(11);

        IDE.JAVAEDITOR.moveCursorToMarker(11);
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("Controller cannot be resolved to a type");

        IDE.JAVAEDITOR.clickOnMarker(11);
        IDE.CODE_ASSISTANT_JAVA.waitForDocPanelOpened();
        IDE.CODE_ASSISTANT_JAVA.insertSelectedItem();
        IDE.JAVAEDITOR.waitAllErrorMarkersIsDisAppear();
        
        IDE.JAVAEDITOR.clickOnMarker(6);
        IDE.STATUSBAR.waitCursorPositionAt("6 : 8");
        IDE.CODE_ASSISTANT_JAVA.waitForDocPanelOpened();
        IDE.CODE_ASSISTANT_JAVA.insertSelectedItem();
        IDE.JAVAEDITOR.waitMarkerIsNotPresentInPosition(7);
        IDE.JAVAEDITOR.waitWarningMarkerPresentInPosition(6);

        IDE.JAVAEDITOR.clickOnMarker(6);
        IDE.CODE_ASSISTANT_JAVA.waitForDocPanelOpened();
        IDE.CODE_ASSISTANT_JAVA.insertSelectedItem();
        IDE.JAVAEDITOR.waitAllMarkersIsDisappear();

    }

    @Test
    public void toDoTest() throws Exception {
        IDE.GOTOLINE.goToLine(11);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("autoGenMethod");
        IDE.CODE_ASSISTANT_JAVA.openForm();
        IDE.CODE_ASSISTANT_JAVA.waitForElementInCodeAssistant("autoGenMethod() : void");
        IDE.CODE_ASSISTANT_JAVA.insertSelectedItem();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(AUTOGENERATE_METHOD);
        IDE.JAVAEDITOR.waitNoteMarkerPresentInPosition(16);
        IDE.JAVAEDITOR.clickOnMarker(16);
        IDE.CODE_ASSISTANT_JAVA.waitForDocPanelOpened();
        IDE.CODE_ASSISTANT_JAVA.insertSelectedItem();
        IDE.JAVAEDITOR.waitNoteMarkerPresentInPosition(16);

    }

}
