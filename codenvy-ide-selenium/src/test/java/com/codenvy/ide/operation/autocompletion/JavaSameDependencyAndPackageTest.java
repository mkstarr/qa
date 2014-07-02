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
package com.codenvy.ide.operation.autocompletion;

import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertTrue;

public class JavaSameDependencyAndPackageTest extends CodeAssistantBaseTest {
    private static final String PROJECT = JavaSameDependencyAndPackageTest.class.getSimpleName();

    private static final String FILE_NAME = "GreetingController.java";

    @BeforeClass
    public static void setUp() {
        final String filePath =
                "src/test/resources/org/exoplatform/ide/operation/file/autocomplete/SamePackageAndDependency.zip";

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
    public void generateJavaDocTest() throws Exception {
        openTestClass();
        IDE.GOTOLINE.goToLine(7);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("import ju");
        IDE.CODE_ASSISTANT_JAVA.openForm();
        IDE.CODE_ASSISTANT_JAVA.waitFromImportContent("junit.*;");
        IDE.CODE_ASSISTANT_JAVA.insertSelectedItem();
        assertTrue(IDE.JAVAEDITOR.getVisibleTextFromJavaEditor().contains("import junit.*;"));
    }

    private void openTestClass() throws InterruptedException, Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("junit");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("junit");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(FILE_NAME);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(FILE_NAME);

        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
    }

}
