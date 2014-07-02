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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class FormatJavaCodeFromEditMenuTest extends ServicesJavaTextFuction {
    private static final String PROJECT = FormatJavaCodeFromEditMenuTest.class.getSimpleName();

    private static final String TXT_FILE_NAME = "TestTextFile.txt";

    private static final String JAVA_FILE_NAME = "SimpleSum.java";

    @BeforeClass
    public static void setUp() {
        final String filePath = "src/test/resources/org/exoplatform/ide/operation/java/FormatTextTest.zip";
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
    public void formatJavaCodeFromEditMenuTest() throws Exception {

        final String codeAfterEdit =
                "package sumcontroller;\n\npublic class SimpleSum\n{\n   int c = 225;\n\n   int d = 1;\n\n   public " +
                "int sumForEdit(int a, int b)\n   {\n      return a + b;\n   }\n\n   int ss = sumForEdit(c, " +
                "d);\n\n   //For Find/replase test\n   String zero = \"\";\n\n   String ONE = \"\";\n\n   String one " +
                "= \"\";\n\n}\n";

        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        openJavaClassForFormat();

        // formating code.
        IDE.MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.FORMAT);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(codeAfterEdit);
        IDE.TOOLBAR.runCommand(ToolbarCommands.File.SAVE);

        String content =
                VirtualFileSystemUtils.getContent(REST_URL + "contentbypath/" + PROJECT + "/src/main/java/sumcontroller/SimpleSum.java");
        // comparing
        assertEquals(content, codeAfterEdit);
    }
}
