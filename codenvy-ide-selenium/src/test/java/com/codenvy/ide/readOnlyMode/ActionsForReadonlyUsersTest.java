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
package com.codenvy.ide.readOnlyMode;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.util.Map;

/**
 @author Roman Iuvshin
 *
 */
public class ActionsForReadonlyUsersTest extends BaseTest {


    private static final String PROJECT = "actionsForAnnonymous";

    protected static Map<String, Link> project;

    @BeforeClass
    public static void before()  throws Exception{
        try {
            project =
                    VirtualFileSystemUtils
                            .importZipProject(PROJECT, "src/test/resources/org/exoplatform/ide/debug/debugStepIntoStepOver.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }


        //Choose browser Web driver:
        switch (BROWSER_COMMAND) {
            case GOOGLE_CHROME:
                driver = new ChromeDriver();
                break;
            default:
                driver = new FirefoxDriver();
        }

        IDE = new com.codenvy.ide.IDE(WORKSPACE_URL, driver);
        try {

            driver.manage().window().maximize();
            driver.get(WORKSPACE_URL);
        } catch (Exception e) {
        }

        IDE.EXPLORER.waitOpened();
        IDE.READ_ONLY_MODE.waitReadonlyIndicator();
        IDE.OPEN.openProject(PROJECT);
    }

    @AfterClass
    public static void tearDown() throws IOException, InterruptedException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Override
    @Before
    public void start(){
    }

    @Test
    public void checkActionsForReadOnlyUsersInFileMenuTest() throws Exception {
        IDE.READ_ONLY_MODE.waitReadonlyIndicator();
        // checking visible commands
        IDE.MENU.waitCommandVisible(MenuCommands.File.FILE, MenuCommands.File.OPEN_FILE_BY_PATH);
        IDE.MENU.waitCommandVisible(MenuCommands.File.FILE, MenuCommands.File.DOWNLOAD);
        IDE.MENU.waitCommandVisible(MenuCommands.File.FILE, MenuCommands.File.DOWNLOAD_ZIPPED_FOLDER);
        IDE.MENU.waitCommandVisible(MenuCommands.File.FILE, MenuCommands.File.CLOSE);
        IDE.MENU.waitCommandVisible(MenuCommands.File.FILE, MenuCommands.File.SEARCH);
        IDE.MENU.waitCommandVisible(MenuCommands.File.FILE, MenuCommands.File.REFRESH);

        // checking that other commands are invivisible
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.RENAME);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.NEW);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.UPLOAD_FILE);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.UPLOAD_FOLDER);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.OPEN_FILE_BY_URL);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.OPEN_LOCAL_FILE);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.SAVE_AS);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.SAVE_ALL);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.DELETE);
    }

    @Test
    public void checkActionsForReadOnlyUsersInProjectMenuTest() throws Exception {
        IDE.READ_ONLY_MODE.waitReadonlyIndicator();
        // checking project menu commands
        // checking visible commands
        IDE.MENU.waitCommandVisible(MenuCommands.Project.PROJECT, MenuCommands.Project.OPEN_PROJECT);
        IDE.MENU.waitCommandVisible(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
        IDE.MENU.waitCommandVisible(MenuCommands.Project.PROJECT, MenuCommands.Project.PROJECT_PROPERTIES);
        IDE.MENU.waitCommandVisible(MenuCommands.Project.PROJECT, MenuCommands.Project.OPEN_RECOURCE);

        // checking that other commands are invivisible
        IDE.MENU.waitCommandInvisible(MenuCommands.Project.PROJECT, MenuCommands.Project.NEW);
        IDE.MENU.waitCommandInvisible(MenuCommands.Project.PROJECT, MenuCommands.Project.BUILD_PROJECT);
        IDE.MENU.waitCommandInvisible(MenuCommands.Project.PROJECT, MenuCommands.Project.BUILD_AND_PUBLISH_PROJECT);

        // checking project properties
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.PROJECT_PROPERTIES);
        IDE.PROPERTIES.waitProjectPropertiesOpened();
        IDE.PROPERTIES.selectRowInProjectPreperties(1);
        IDE.PROPERTIES.waitEditButtonIsDisabled();
        IDE.PROPERTIES.waitDeleteButtonIsDisabled();
        IDE.PROPERTIES.selectRowInProjectPreperties(2);
        IDE.PROPERTIES.waitEditButtonIsDisabled();
        IDE.PROPERTIES.waitDeleteButtonIsDisabled();
        IDE.PROPERTIES.selectRowInProjectPreperties(3);
        IDE.PROPERTIES.waitEditButtonIsDisabled();
        IDE.PROPERTIES.waitDeleteButtonIsDisabled();
        IDE.PROPERTIES.selectRowInProjectPreperties(4);
        IDE.PROPERTIES.waitEditButtonIsDisabled();
        IDE.PROPERTIES.waitDeleteButtonIsDisabled();
        IDE.PROPERTIES.selectRowInProjectPreperties(5);
        IDE.PROPERTIES.waitEditButtonIsDisabled();
        IDE.PROPERTIES.waitDeleteButtonIsDisabled();
        IDE.PROPERTIES.selectRowInProjectPreperties(6);
        IDE.PROPERTIES.waitEditButtonIsDisabled();
        IDE.PROPERTIES.waitDeleteButtonIsDisabled();
        IDE.PROPERTIES.clickCancelButtonOnPropertiesForm();
    }

    @Test
    public void checkActionsForReadOnlyUsersInEditMenuTest() throws Exception {
        IDE.READ_ONLY_MODE.waitReadonlyIndicator();
        // checking edit menu commands
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("pom.xml");
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitTabPresent("File opened in read only mode. Use SaveAs command.");
        // checking visible commands
        IDE.MENU.waitCommandVisible(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.GO_TO_LINE);
        IDE.MENU.waitCommandVisible(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.SELECT_ALL);

        // checking that other commands are invivisible
        IDE.MENU.waitCommandInvisible(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.CUT_MENU);
        IDE.MENU.waitCommandInvisible(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.COPY_MENU);
        IDE.MENU.waitCommandInvisible(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.UNDO_TYPING);
        IDE.MENU.waitCommandInvisible(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.REDO_TYPING);
        IDE.MENU.waitCommandInvisible(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.FORMAT);
        IDE.MENU.waitCommandInvisible(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.ADD_BLOCK_COMMENT);
        IDE.MENU.waitCommandInvisible(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.REMOVE_BLOCK_COMMENT);
        IDE.MENU.waitCommandInvisible(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.TOGGLE_COMMENT);
        IDE.MENU.waitCommandInvisible(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.DELETE_CURRENT_LINE);
        IDE.MENU.waitCommandInvisible(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.DELETE);
        IDE.MENU.waitCommandInvisible(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.MOVE_LINE_UP);
        IDE.MENU.waitCommandInvisible(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.MOVE_LINE_DOWN);
        IDE.MENU.waitCommandInvisible(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.SOURCE);
        IDE.MENU.waitCommandInvisible(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.REFACTOR);
        IDE.MENU.waitCommandInvisible(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.SEND_CODE_POINTER);
    }

    @Test
    public void checkActionsForReadOnlyUsersInRunMenuTest() throws Exception {
        IDE.READ_ONLY_MODE.waitReadonlyIndicator();
        IDE.MENU.waitTopMenuDisabled(MenuCommands.Run.RUN);
    }

    @Test
    public void checkActionsForReadOnlyUsersInGitMenuTest() throws Exception {
        IDE.READ_ONLY_MODE.waitReadonlyIndicator();
        IDE.MENU.waitTopMenuDisabled(MenuCommands.Git.GIT);
    }

    @Test
    public void checkActionsForReadOnlyUsersInPaasMenuTest() throws Exception {
        IDE.READ_ONLY_MODE.waitReadonlyIndicator();
        IDE.MENU.waitTopMenuDisabled(MenuCommands.PaaS.PAAS);
    }

    @Test
    public void checkActionsForReadOnlyUsersInShareMenuTest() throws Exception {
        IDE.READ_ONLY_MODE.waitReadonlyIndicator();
        IDE.MENU.waitTopMenuDisabled(MenuCommands.Share.SHARE);
    }

    @Test
    public void checkActionsForReadOnlyUsersInWindowMenuTest() throws Exception {
        IDE.READ_ONLY_MODE.waitReadonlyIndicator();
        // checking visible commands
        IDE.MENU.waitCommandVisible(MenuCommands.Window.WINDOW, MenuCommands.Window.SHOW_VIEW);
        IDE.MENU.waitCommandVisible(MenuCommands.Window.WINDOW, MenuCommands.Window.NAVIGATION);
        IDE.MENU.waitCommandVisible(MenuCommands.Window.WINDOW, MenuCommands.Window.WELCOME);

        // checking that other commands are invivisible
        IDE.MENU.waitCommandInvisible(MenuCommands.Window.WINDOW, MenuCommands.Window.PREFERNCESS);
    }

    @Test
    public void checkActionsForReadOnlyUsersInHelpMenuTest() throws Exception {
        IDE.READ_ONLY_MODE.waitReadonlyIndicator();
        // checking visible commands
        IDE.MENU.waitCommandVisible(MenuCommands.Help.HELP, MenuCommands.Help.ABOUT);
        IDE.MENU.waitCommandVisible(MenuCommands.Help.HELP, MenuCommands.Help.SHOW_KEYBOARD_SHORTCUTS);
        IDE.MENU.waitCommandVisible(MenuCommands.Help.HELP, MenuCommands.Help.DOCUMENTATION);
        IDE.MENU.waitCommandVisible(MenuCommands.Help.HELP, MenuCommands.Help.CONTACT_SUPPORT);
        IDE.MENU.waitCommandVisible(MenuCommands.Help.HELP, MenuCommands.Help.SUBMIT_FEEDBACK);

        //check Show Keyboard Shortcuts...
        IDE.MENU.runCommand(MenuCommands.Help.HELP, MenuCommands.Help.SHOW_KEYBOARD_SHORTCUTS);
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitOpened();
        // check available shortcuts
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitShortcutIsPresent("Ctrl+W");
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitShortcutIsPresent("Ctrl+Shift+R");
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitShortcutIsPresent("Ctrl+F");
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitShortcutIsPresent("Ctrl+O");
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitShortcutIsPresent("Ctrl+Q");
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitShortcutIsPresent("Ctrl+Shift+PageDown");
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitShortcutIsPresent("Ctrl+Shift+PageUp");
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitShortcutIsPresent("F1");
        // check unavailable shortcuts
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitShortcutIsNotPresent("Ctrl+S");
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitShortcutIsNotPresent("Ctrl+Shift+F");
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitShortcutIsNotPresent("Ctrl+Shift+O");
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitShortcutIsNotPresent("Ctrl+Shift+/");
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitShortcutIsNotPresent("Ctrl+Shift+\\");
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitShortcutIsNotPresent("Ctrl+D");
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitShortcutIsNotPresent("Ctrl+Shift+C");
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitShortcutIsNotPresent("Alt+Up");
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitShortcutIsNotPresent("Alt+Down");
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitShortcutIsNotPresent("Ctrl+1");
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitShortcutIsNotPresent("Alt+Shift+R");
        IDE.SHOW_KEYBOARD_SHORTCUTS.closeButtonClick();
    }

    @Test
    public void checkActionsForReadOnlyUsersInViewMenuTest() throws Exception {
        // checking view menu commands
        IDE.MENU.waitCommandVisible(MenuCommands.View.VIEW, MenuCommands.View.SHOW_PROPERTIES);
        IDE.MENU.waitCommandVisible(MenuCommands.View.VIEW, MenuCommands.View.OUTLINE);
        IDE.MENU.waitCommandVisible(MenuCommands.View.VIEW, MenuCommands.View.GO_TO_FOLDER);
        IDE.MENU.waitCommandVisible(MenuCommands.View.VIEW, MenuCommands.View.PROGRESS);
        IDE.MENU.waitCommandVisible(MenuCommands.View.VIEW, MenuCommands.View.OUTPUT);
    }
}
