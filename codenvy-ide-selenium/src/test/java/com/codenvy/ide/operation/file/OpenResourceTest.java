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
package com.codenvy.ide.operation.file;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * @author Musienko Maxim
 *
 */
public class OpenResourceTest extends BaseTest {

    private static final String PROJECT = OpenResourceTest.class.getSimpleName();

    private static final String PROJECT2 = OpenResourceTest.class.getSimpleName() + "gadget";

    private String POM_CONTENT =
            "      <dependency>\n         <groupId>junit</groupId>\n         <artifactId>junit</artifactId>\n        " +
            " <version>3.8.1</version>\n         <scope>test</scope>\n      </dependency>";

    private String JAVA_CONTENT =
            "      numbers.add(1);\n      numbers.add(2);\n      numbers.add(3);\n      numbers.add(4);\n      " +
            "numbers.add(5);\n      numbers.add(6);";

    protected static Map<String, Link> project;

    @BeforeClass
    public static void before() {

        try {
            project =
                    VirtualFileSystemUtils.importZipProject(PROJECT,


                                                            "src/test/resources/org/exoplatform/ide/operation/java/JavaCommentsTest.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            project =
                    VirtualFileSystemUtils.importZipProject(PROJECT2,
                                                            "src/test/resources/org/exoplatform/ide/operation/file/Calculator.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @AfterClass
    public static void tearDown() throws IOException, InterruptedException {
        VirtualFileSystemUtils.delete(PROJECT);
        VirtualFileSystemUtils.delete(PROJECT2);
    }

    @Test
    public void openRecourceTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.OPEN_RECOURCE);
        IDE.OPEN_RESOURCE.waitOpenResouceFormIsOpen();
        IDE.OPEN_RESOURCE.waitWhileCertainNumberOfElementsIsAppear(8);
        IDE.OPEN_RESOURCE.typeToSearchField("p");
        IDE.OPEN_RESOURCE.waitFoundFiles("pom.xml");
        IDE.OPEN_RESOURCE.selectItemInMatchList("pom.xml");
        IDE.OPEN_RESOURCE.clickOnOpenBtn();
        IDE.OPEN_RESOURCE.waitOpenResouceFormIsClosed();
        IDE.EDITOR.waitTabPresent("pom.xml");
        IDE.EDITOR.waitActiveFile();
        assertTrue(IDE.EDITOR.getTextFromCodeEditor().contains(POM_CONTENT));

        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.OPEN_RECOURCE);
        IDE.OPEN_RESOURCE.waitOpenResouceFormIsOpen();
        IDE.OPEN_RESOURCE.waitWhileCertainNumberOfElementsIsAppear(8);
        IDE.OPEN_RESOURCE.selectItemInMatchList("JavaCommentsTest.java");
        IDE.OPEN_RESOURCE.clickOnOpenBtn();
        IDE.OPEN_RESOURCE.waitOpenResouceFormIsClosed();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.EDITOR.waitTabPresent("JavaCommentsTest.java");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText(JAVA_CONTENT);
    }
}
