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
import com.codenvy.ide.operation.java.ServicesJavaTextFuction;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import java.util.Map;

/**
 * @author Musienko Maxim
 *
 */

public class SwitchBetweenEditorsFromNavigationTest extends BaseTest {

    private static final String PROJECT = SwitchBetweenEditorsFromNavigationTest.class.getSimpleName();

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
    public void swtitchBetweenEditors() throws Exception {
        ServicesJavaTextFuction servOperation = new ServicesJavaTextFuction();
        IDE.EXPLORER.waitOpened();
        IDE.EXPLORER.waitForItemInProjectList(PROJECT);
        IDE.EXPLORER.selectProjectByNameInProjectsListGrid(PROJECT);
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        servOperation.openSpringJavaTetsFile();

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("pom.xml");
        IDE.EDITOR.waitTabPresent("pom.xml");
        IDE.EDITOR.selectTab("pom.xml");
        IDE.EDITOR.waitActiveFile();
        IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.NAVIGATION,
                            MenuCommands.Window.PREVIOUS_EDITOR);
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("return mav;");
        IDE.MENU
           .runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.NAVIGATION, MenuCommands.Window.NEXT_EDITOR);
        IDE.EDITOR.waitContentIsPresent("<groupId>org.springframework</groupId>");

        //TODO after changing codemirror on collab-editor will should be overwrite
        //used Actions because after switch on next editor in FF can't exit into frame
        new Actions(driver).sendKeys(Keys.CONTROL.toString() + Keys.SHIFT.toString() + Keys.PAGE_UP.toString()).build()
                           .perform();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("return mav;");

        new Actions(driver).sendKeys(Keys.PAGE_DOWN.toString()).build().perform();

        IDE.EDITOR.waitContentIsPresent("<groupId>org.springframework</groupId>");
        new Actions(driver).sendKeys(Keys.PAGE_DOWN.toString()).build().perform();
    }
}
