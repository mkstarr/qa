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
package com.codenvy.ide.operation.browse;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.TestConstants;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Map;

/**
 * Created by The eXo Platform SAS.
 *
 * @author <a href="mailto:dmitry.ndp@gmail.com">Dmytro Nochevnov</a>
 *
 */
public class UsingKeyboardTest extends BaseTest {

    private static final String TEST_SUBFOLDER = "subFolder";

    private static final String PROJECT = "project";

    private static final String TEST_FILE = "config.xml";

    private static final String NEW_FILE = "newcreatedfile.xml";

    private static final String TEST_FILE_PATH =
            "src/test/resources/org/exoplatform/ide/operation/file/usingKeyboardTestGoogleGadget.xml";

    private Robot robot;

    @BeforeClass
    public static void setUp() throws Exception {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link link = project.get(Link.REL_CREATE_FOLDER);
            Link linkFile = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils.createFolder(link, TEST_SUBFOLDER);
            VirtualFileSystemUtils
                    .createFileFromLocal(linkFile, TEST_FILE, MimeType.TEXT_XML, TEST_FILE_PATH);
        } catch (Exception e) {
        }
    }

    /**
     * Keyboard works in the TreeGrid only within the Mozilla Firefox browser with SmartGWT 2.2, 2.3
     *
     * @throws Exception
     */
    @Test
    public void testUsingKeyboardInNavigationPanel() throws Exception {
        robot = new Robot();

        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + TEST_SUBFOLDER);
        IDE.EXPLORER.selectItem(PROJECT + "/" + TEST_SUBFOLDER);

        Thread.sleep(2000);
        robot.keyPress(KeyEvent.VK_UP);
        robot.keyRelease(KeyEvent.VK_UP);
        robot.keyPress(KeyEvent.VK_LEFT);
        robot.keyRelease(KeyEvent.VK_LEFT);

        IDE.EXPLORER.waitForItemNotVisible(PROJECT + "/" + TEST_SUBFOLDER);

        IDE.EXPLORER.selectItem(PROJECT);

        Thread.sleep(2000);
        robot.keyPress(KeyEvent.VK_RIGHT);
        robot.keyRelease(KeyEvent.VK_RIGHT);

        IDE.EXPLORER.waitForItem(PROJECT + "/" + TEST_SUBFOLDER);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + TEST_FILE);

        // test keyboard with opened Content Panel
        IDE.EXPLORER.openItem(PROJECT + "/" + TEST_FILE);
        IDE.EDITOR.waitActiveFile();

        IDE.EXPLORER.selectItem(PROJECT + "/" + TEST_SUBFOLDER);

        Thread.sleep(2000);
        robot.keyPress(KeyEvent.VK_UP);
        robot.keyRelease(KeyEvent.VK_UP);

        robot.keyPress(KeyEvent.VK_LEFT);
        robot.keyRelease(KeyEvent.VK_LEFT);

        IDE.EXPLORER.waitForItemNotVisible(PROJECT + "/" + TEST_SUBFOLDER);
        IDE.EXPLORER.waitForItemNotVisible(PROJECT + "/" + TEST_FILE);
        IDE.EDITOR.forcedClosureFile(1);
    }

    /**
     * Keyboard works in the TreeGrid only within the Mozilla Firefox browser with SmartGWT 2.2, 2.3
     *
     * @throws Exception
     */
    @Test
    public void testUsingKeyboardInSearchPanel() throws Exception {
        robot = new Robot();

        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.openItem(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + TEST_SUBFOLDER);
        IDE.EXPLORER.selectItem(PROJECT + "/" + TEST_SUBFOLDER);
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.XML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName(NEW_FILE);
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.closeFile(NEW_FILE);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + TEST_SUBFOLDER + "/" + NEW_FILE);

        IDE.EXPLORER.selectItem(PROJECT + "/" + TEST_SUBFOLDER + "/" + NEW_FILE);
        IDE.SEARCH.performSearch("/" + PROJECT + "/" + TEST_SUBFOLDER, "", MimeType.TEXT_XML);
        IDE.SEARCH_RESULT.waitOpened();
        IDE.SEARCH_RESULT.waitItemPresent(PROJECT + "/" + TEST_SUBFOLDER + "/" + NEW_FILE);

        // move with keys in search three
        IDE.SEARCH_RESULT.selectItem(PROJECT + "/" + TEST_SUBFOLDER + "/" + NEW_FILE);

        Thread.sleep(2000);
        robot.keyPress(KeyEvent.VK_UP);
        robot.keyRelease(KeyEvent.VK_UP);
        robot.keyPress(KeyEvent.VK_UP);
        robot.keyRelease(KeyEvent.VK_UP);
        robot.keyPress(KeyEvent.VK_LEFT);
        robot.keyRelease(KeyEvent.VK_LEFT);

        IDE.SEARCH_RESULT.waitItemNotPresent(PROJECT + "/" + TEST_SUBFOLDER + "/" + NEW_FILE);
        IDE.SEARCH_RESULT.close();
    }

    /**
     * Keyboard works in the TreeGrid only within the Mozilla Firefox browser with SmartGWT 2.2, 2.3
     *
     * @throws Exception
     */
    @Test
    public void testUsingKeyboardInOutlinePanel() throws Exception {
        robot = new Robot();

        IDE.EXPLORER.waitForItem(PROJECT + "/" + TEST_FILE);
        IDE.EXPLORER.openItem(PROJECT + "/" + TEST_FILE);
        IDE.EDITOR.waitActiveFile();
        //TODO Pause for build outline tree
        //after implementation method for check ready state, should be remove
        Thread.sleep(1000);
        // open Outline Panel
        IDE.TOOLBAR.runCommand(ToolbarCommands.View.SHOW_OUTLINE);
        IDE.OUTLINE.waitOpened();

        IDE.EDITOR.selectTab(TEST_FILE);
        IDE.EDITOR.moveCursorDown(2);
        Thread.sleep(TestConstants.SLEEP);

        // check outline tree
        IDE.OUTLINE.waitItemPresent("Module");
        IDE.OUTLINE.waitItemPresent("ModulePrefs");
        IDE.OUTLINE.waitItemPresent("Content");

        // verify keyboard key pressing within the outline
        IDE.OUTLINE.selectItem("Module");
        IDE.STATUSBAR.waitCursorPositionControl();
        IDE.STATUSBAR.waitCursorPositionAt("2 : 1");

        // open "Content" node in the Outline Panel and got to "CDATA" node

        IDE.OUTLINE.selectItem("Content");

        Thread.sleep(2000);
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);

        IDE.OUTLINE.waitItemAtPosition("CDATA", 4);
        IDE.OUTLINE.waitElementIsSelect("CDATA");
        IDE.STATUSBAR.waitCursorPositionAt("6 : 1");

        IDE.EDITOR.forcedClosureFile(1);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        VirtualFileSystemUtils.delete(PROJECT);
    }

}
