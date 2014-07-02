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

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.util.Map;

/** @author Musienko Maxim */
public class CheckCodeCollaborationWithAutocompletionFeaturesTest extends CollaborationService {

    private static final String PROJECT =
            CheckCodeCollaborationWithAutocompletionFeaturesTest.class.getSimpleName();

    private final String FILLER_TEXT = "String fillerText=\"Lorem ipsum dolor sit amet\";";

    private final String INSERT_IN_METHOD =
            "/**\n * \n */\nprivate void autogenMethod()\n{\n   // TODO Auto-generated method stub\nfillerText\n}";

    private final String INSERT_IN_METHOD2 =
            "/**\n * \n */\nprivate void oneMoreMetgod()\n{\n   // TODO Auto-generated method stub\n\n}}";


    private final String AUTOGENERATE_METHOD_TEXT =
            "/**\n * \n */\nprivate void autogenMethod()\n{\n   // TODO Auto-generated method stub\n\n}";

    protected static Map<String, Link> project;

    @BeforeClass
    public static void before() {

        try {
            project =
                    VirtualFileSystemUtils.importZipProject(PROJECT,


                                                            "src/test/resources/org/exoplatform/ide/debug/debugStepIntoStepOver.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
            killSecondBrowser();
        } catch (Exception e) {
        }
    }

    @Test
    public void typeInEditorTest() throws Exception {
        initSecondBrowser();

        // TODO may be add verification of the notifications?
        // opening project from first user

        // TODO see also issue IDE - IDE-2495

        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        // opening project from second user
        IDE2.EXPLORER.waitOpened();
        IDE2.OPEN.openProject(PROJECT);
        IDE2.PACKAGE_EXPLORER.waitPackageExplorerOpened();

        // open class as first user
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("NewClass.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("NewClass.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.ENABLE_COLLABORATION_MODE);
        IDE.ASK_DIALOG.waitOpened();
        IDE.ASK_DIALOG.clickContinueBtn();
        IDE.ASK_DIALOG.waitClosed();

        // open class as second user
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE2.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE2.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE2.MENU.waitForMenuItemPresent(MenuCommands.Project.PROJECT, MenuCommands.Project.DISABLE_COLLABORATION_MODE);
        IDE2.PACKAGE_EXPLORER.waitItemInPackageExplorer("NewClass.java");
        IDE2.PACKAGE_EXPLORER.openItemWithDoubleClick("NewClass.java");
        IDE2.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();

        IDE.GOTOLINE.goToLine(4);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.END.toString());
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("\n");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(FILLER_TEXT);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(FILLER_TEXT);
//TODO WORKAROUND CHECK IT
//        IDE.JAVAEDITOR.waitAllMarkersIsDisappear();

        IDE2.GOTOLINE.goToLine(5);
        IDE2.JAVAEDITOR.typeTextIntoJavaEditor(Keys.END.toString());
        IDE2.JAVAEDITOR.typeTextIntoJavaEditor("\n");
        IDE2.JAVAEDITOR.typeTextIntoJavaEditor("autogenMethod");
        IDE2.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("autogenMethod");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("autogenMethod");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + Keys.SPACE.toString());

        IDE2.CODE_ASSISTANT_JAVA.openForm();
        IDE2.CODE_ASSISTANT_JAVA.waitForElementInCodeAssistant("autogenMethod() : void");
        IDE2.CODE_ASSISTANT_JAVA.insertSelectedItem();


        IDE2.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(AUTOGENERATE_METHOD_TEXT);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(AUTOGENERATE_METHOD_TEXT);

        IDE.GOTOLINE.goToLine(12);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("filler");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + Keys.SPACE.toString());
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(INSERT_IN_METHOD);
        IDE2.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(INSERT_IN_METHOD);

        IDE2.GOTOLINE.goToLine(12);
        IDE2.JAVAEDITOR.typeTextIntoJavaEditor(Keys.END.toString());
        IDE2.JAVAEDITOR.typeTextIntoJavaEditor(".");
        IDE2.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + Keys.SPACE.toString());
        IDE2.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("toUpperCase()");
        IDE2.CODE_ASSISTANT_JAVA.selectImportProposal("toUpperCase()");
        IDE2.CODE_ASSISTANT_JAVA.insertSelectedItem();
        IDE2.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("fillerText." + "toUpperCase()");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("fillerText." + "toUpperCase()");

        IDE2.JAVAEDITOR.typeTextIntoJavaEditor(";");
        IDE2.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("fillerText." + "toUpperCase();");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("fillerText." + "toUpperCase();");

        IDE.GOTOLINE.goToLine(2);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("import java.util.ArrayList;");
        IDE2.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("import java.util.ArrayList;");

        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("import java.util.ArrayList;");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + Keys.SHIFT.toString() + "o");

        IDE2.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText("import java.util.ArrayList;");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillNotContainSpecifiedText("import java.util.ArrayList;");
    }

    @Test
    public void quickAutocomleteTest() throws Exception {
        IDE.GOTOLINE.goToLine(5);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("\n");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("public  double someField = 0.5;");
        IDE2.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("public  double someField = 0.5;");
        IDE.GOTOLINE.goToLine(19);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("\n");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("oneMoreMetgod");
        IDE2.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("oneMoreMetgod");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + Keys.SPACE.toString());
        IDE.CODE_ASSISTANT_JAVA.openForm();
        IDE.CODE_ASSISTANT_JAVA.waitForElementInCodeAssistant("oneMoreMetgod() : void");
        IDE.CODE_ASSISTANT_JAVA.insertSelectedItem();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(INSERT_IN_METHOD2);
        IDE2.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(INSERT_IN_METHOD2);
        IDE.GOTOLINE.goToLine(26);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("some");
        IDE2.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("some");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + Keys.SPACE.toString());
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("// TODO Auto-generated method stub\nsomeField");
        IDE2.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("// TODO Auto-generated method stub\nsomeField");

        IDE.JAVAEDITOR.typeTextIntoJavaEditor("= bigV");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + Keys.SPACE.toString());
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("someField= bigValuenewMTd()");
        IDE2.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("someField= bigValuenewMTd()");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(";");
        IDE.JAVAEDITOR.waitAllErrorMarkersIsDisAppear();
        IDE2.JAVAEDITOR.waitAllErrorMarkersIsDisAppear();
    }
}
