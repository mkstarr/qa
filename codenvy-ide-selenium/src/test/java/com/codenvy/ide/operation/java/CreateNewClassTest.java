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

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;


/**
 @author Roman Iuvshin
 *
 */
public class CreateNewClassTest extends BaseTest {
    private static final String PROJECT            = CreateNewClassTest.class.getSimpleName();
    private static final String CLASS_CONTENT      = "package sumcontroller;\n" +
                                                     "\n" +
                                                     "public class NewClassFromForm\n" +
                                                     "{\n" +
                                                     "}\n";
    private static final String INTERFACE_CONTENT  = "package sumcontroller;\n" +
                                                     "\n" +
                                                     "public interface NewInterface\n" +
                                                     "{\n" +
                                                     "}\n";
    private static final String ENUM_CONTENT       = "package sumcontroller;\n" +
                                                     "\n" +
                                                     "public enum NewEnum\n" +
                                                     "{\n" +
                                                     "}\n";
    private static final String ANNOTATION_CONTENT = "package test;\n" +
                                                     "\n" +
                                                     "public @interface NewAnnotation\n" +
                                                     "{\n" +
                                                     "}\n";

    @BeforeClass
    public static void setUp() {
        final String filePath = "src/test/resources/org/exoplatform/ide/operation/java/calc.zip";

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
    public void createNewJavaClassTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("sumcontroller");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("sumcontroller");

        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.JAVA_CLASS);

        IDE.CREATE_NEW_CLASS.waitCreateFormIsPresent();
        IDE.CREATE_NEW_CLASS.waitItemIsPresentInPackageList("sumcontroller");
        IDE.CREATE_NEW_CLASS.waitItemIsPresentInKindList("Class");
        IDE.CREATE_NEW_CLASS.waitCreateButtonIsDisabled();
        IDE.CREATE_NEW_CLASS.typeClassName("NewClassFromForm");
        IDE.CREATE_NEW_CLASS.waitCreateButtonIsEnabled();
        IDE.CREATE_NEW_CLASS.clickCreateBtn();
        IDE.CREATE_NEW_CLASS.waitFormIsClosed();
        IDE.LOADER.waitClosed();
        // check file content
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(CLASS_CONTENT);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("NewClassFromForm.java");
    }

    @Test
    public void createNewJavaInterfaceTest() throws Exception {
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.JAVA_CLASS);
        IDE.CREATE_NEW_CLASS.waitCreateFormIsPresent();
        IDE.CREATE_NEW_CLASS.waitItemIsPresentInPackageList("sumcontroller");
        IDE.CREATE_NEW_CLASS.selectClassKind("Interface");
        IDE.CREATE_NEW_CLASS.waitCreateButtonIsDisabled();
        IDE.CREATE_NEW_CLASS.typeClassName("NewInterface");
        IDE.CREATE_NEW_CLASS.waitCreateButtonIsEnabled();
        IDE.CREATE_NEW_CLASS.clickCreateBtn();
        IDE.CREATE_NEW_CLASS.waitFormIsClosed();
        IDE.LOADER.waitClosed();
        // check file content
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(INTERFACE_CONTENT);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("NewInterface.java");
    }

    @Test
    public void createNewJavaEnumTest() throws Exception {
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.JAVA_CLASS);
        IDE.CREATE_NEW_CLASS.waitCreateFormIsPresent();
        IDE.CREATE_NEW_CLASS.waitItemIsPresentInPackageList("sumcontroller");
        IDE.CREATE_NEW_CLASS.selectClassKind("Enum");
        IDE.CREATE_NEW_CLASS.waitCreateButtonIsDisabled();
        IDE.CREATE_NEW_CLASS.typeClassName("NewEnum");
        IDE.CREATE_NEW_CLASS.waitCreateButtonIsEnabled();
        IDE.CREATE_NEW_CLASS.clickCreateBtn();
        IDE.CREATE_NEW_CLASS.waitFormIsClosed();
        IDE.LOADER.waitClosed();
        // check file content
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(ENUM_CONTENT);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("NewEnum.java");
    }

    @Test
    public void createNewJavaAnnotationTest() throws Exception {
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.JAVA_CLASS);
        IDE.CREATE_NEW_CLASS.waitCreateFormIsPresent();
        IDE.CREATE_NEW_CLASS.waitItemIsPresentInPackageList("sumcontroller");
        IDE.CREATE_NEW_CLASS.selectClassKind("Annotation");
        IDE.CREATE_NEW_CLASS.waitCreateButtonIsDisabled();
        IDE.CREATE_NEW_CLASS.typeClassName("NewAnnotation");
        IDE.CREATE_NEW_CLASS.waitCreateButtonIsEnabled();
        IDE.CREATE_NEW_CLASS.selectPackage("test");
        IDE.CREATE_NEW_CLASS.clickCreateBtn();
        IDE.CREATE_NEW_CLASS.waitFormIsClosed();
        IDE.LOADER.waitClosed();
        // check file content
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(ANNOTATION_CONTENT);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("NewAnnotation.java");
    }
}