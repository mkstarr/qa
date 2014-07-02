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
 * @author <a href="mailto:foo@bar.org">Foo Bar</a>
 *
 */
public class UpdateDependencyTest extends ServicesJavaTextFuction {
    private static final String PROJECT = UpdateDependencyTest.class.getSimpleName();

    private String DEPENDENCY =
            "    <dependency>\n      <groupId>net.twonky</groupId>\n      " +
            "<artifactId>twonky-string-utils</artifactId>\n      <version>1.0</version>\n    </dependency>";

    @BeforeClass
    public static void setUp() {
        final String filePath = "src/test/resources/org/exoplatform/ide/operation/java/UpdateDependency.zip";
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
    public void updateDependencyTets() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.SHOW_SYNTAX_ERROR_HIGHLIGHTING);
        openJavaClassForUpdateDependencyTest();
        IDE.JAVAEDITOR.waitErrorMarkerPresentInPosition(2);
        IDE.JAVAEDITOR.waitErrorMarkerPresentInPosition(18);
        IDE.JAVAEDITOR.waitErrorMarkerPresentInPosition(20);

        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("pom.xml");
        IDE.EDITOR.waitActiveFile();
        IDE.GOTOLINE.goToLine(13);
        IDE.EDITOR.typeTextIntoEditor(Keys.END.toString());
        IDE.EDITOR.typeTextIntoEditor("\n");
        IDE.EDITOR.typeTextIntoEditor(DEPENDENCY);
        Thread.sleep(1000);
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL + "s");
        IDE.EDITOR.waitNoContentModificationMark("pom.xml");
        IDE.LOADER.waitClosed();
        IDE.EDITOR.closeFile("pom.xml");
        IDE.EDITOR.waitTabNotPresent("pom.xml");
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.UPDATE_DEPENDENCY);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.JAVAEDITOR.waitAllMarkersIsDisappear();
    }
}
