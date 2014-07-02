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
package com.codenvy.ide.preferences;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * IDE-156:HotKeys customization.
 * <p/>
 * Created by The eXo Platform SAS.
 *
 * @author <a href="mailto:musienko.maxim@gmail.com">Musienko Maxim</a>
 *
 */
public class HotkeysCustomizationTest extends BaseTest {
    private final static String PROJECT = HotkeysCustomizationTest.class.getSimpleName();

    private final static String FILE_NAME = HotkeysCustomizationTest.class.getSimpleName();

    @BeforeClass
    public static void setUp() throws Exception {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link linkFile = project.get(Link.REL_CREATE_FILE);
            VirtualFileSystemUtils
                    .createFileFromLocal(linkFile, FILE_NAME, MimeType.TEXT_HTML,
                                         "src/test/resources/org/exoplatform/ide/operation/file/newHtmlFile.html");

//         VirtualFileSystemUtils.put(
//            "src/test/resources/org/exoplatform/ide/operation/restservice/RESTServiceGetURL.groovy",
//            MimeType.GROOVY_SERVICE, WS_URL + PROJECT + "/" + FILE_NAME);
        } catch (IOException e) {

        }
    }

    @AfterClass
    public static void tearDown() {
        try {
            //FIXME VirtualFileSystemUtils.delete(PRODUCTION_SERVICE_PREFIX);
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (IOException e) {
        }
    }

    /**
     * IDE-156:HotKeys customization ----- 1-2 ------------
     *
     * @throws Exception
     */

    @Test
    public void testDefaultHotkeys() throws Exception {

        // step 1 create new project, open default xml file and check hotkey
        // ctrl+N.
        // change xml file, press Ctrl+S and check ask for value dialog
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.TOOLBAR.waitButtonPresentAtLeft(MenuCommands.New.NEW);
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.XML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName("xml_" + FILE_NAME);
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitTabPresent("xml_" + FILE_NAME);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.deleteFileContent();
        IDE.EDITOR.typeTextIntoEditor("change file");
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "s");
        IDE.LOADER.waitClosed();
        IDE.EDITOR.forcedClosureFile(1);
    }

    @Test
    public void testHotkeysInSeveralTabs() throws Exception {

        // step 1 open
        IDE.EXPLORER.waitOpened();
        IDE.EXPLORER.waitForItem(PROJECT);

        openCustomizeHotkeyForm();

        IDE.CUSTOMIZE_HOTKEYS.selectElementOnCommandlistbarByName("New HTML");
        IDE.CUSTOMIZE_HOTKEYS.typeKeys(Keys.CONTROL.toString() + "h");
        IDE.CUSTOMIZE_HOTKEYS.waitBindEnabled();
        IDE.CUSTOMIZE_HOTKEYS.bindButtonClick();
        IDE.LOADER.waitClosed();
        IDE.CUSTOMIZE_HOTKEYS.selectElementOnCommandlistbarByName("About...");
        IDE.CUSTOMIZE_HOTKEYS.typeKeys(Keys.ALT.toString() + "n");
        IDE.CUSTOMIZE_HOTKEYS.waitBindEnabled();
        IDE.CUSTOMIZE_HOTKEYS.bindButtonClick();
        assertTrue(IDE.CUSTOMIZE_HOTKEYS.isOkEnabled());
        IDE.CUSTOMIZE_HOTKEYS.okButtonClick();
        IDE.LOADER.waitClosed();
        IDE.PREFERENCES.clickOnCloseFormBtn();
        IDE.PREFERENCES.waitPreferencesClose();

        // step 2 tabs and check in first tab new hotkeys. Selecting second tab,
        // and checking new hotkey here
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.XML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitTabPresent(1);
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.TEXT_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitTabPresent(2);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.selectTab(1);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.typeTextIntoEditor(Keys.ALT.toString() + "n");
        IDE.ABOUT.waitLogoPresent();
        IDE.ABOUT.waitCheckInfoAboutWindow();
        IDE.ABOUT.closeDialogAboutForm();
        IDE.ABOUT.waitClosedDialogAbout();
        IDE.EDITOR.selectTab(2);
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL + "h");
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName("html_" + FILE_NAME);
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitTabPresent("html_" + FILE_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("</html>");
        IDE.EDITOR.closeFile("Untitled file.txt");
        IDE.EDITOR.closeFile("html_" + FILE_NAME);
    }

    @Test
    public void testHotkeysAfterRefresh() throws Exception {
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.TOOLBAR.waitButtonPresentAtLeft(MenuCommands.New.NEW);
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.XML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName("new_xml_" + FILE_NAME);
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitTabPresent("new_xml_" + FILE_NAME);
        IDE.EDITOR.waitActiveFile();

        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.PYTHON);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName("new_python_file_" + FILE_NAME);
        IDE.FILE.clickCreateButton();
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.EDITOR.selectTab(1);

        IDE.EDITOR.selectTab(2);
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "n");
        IDE.EDITOR.closeFile("new_python_file_" + FILE_NAME);
        IDE.EDITOR.closeFile("new_xml_" + FILE_NAME);
    }

    @Test
    public void testResettingToDefaults() throws Exception {
        IDE.EXPLORER.waitForItem(PROJECT);

        // step 1: create new hotkey for upload file (Alt+U)
        openCustomizeHotkeyForm();

        IDE.CUSTOMIZE_HOTKEYS.selectElementOnCommandlistbarByName("Open File By Path...");
        IDE.CUSTOMIZE_HOTKEYS.typeKeys(Keys.CONTROL.toString() + "m");
        IDE.CUSTOMIZE_HOTKEYS.waitBindEnabled();
        IDE.CUSTOMIZE_HOTKEYS.bindButtonClick();
        assertTrue(IDE.CUSTOMIZE_HOTKEYS.isOkEnabled());
        IDE.CUSTOMIZE_HOTKEYS.okButtonClick();
        IDE.LOADER.waitClosed();
        IDE.PREFERENCES.clickOnCloseFormBtn();
        IDE.PREFERENCES.waitPreferencesClose();

        // step 2: check new hotkey
        IDE.selectMainFrame();
        IDE.EXPLORER.selectItem(PROJECT);
        IDE.TOOLBAR.waitButtonPresentAtLeft(MenuCommands.New.NEW);
        IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.XML_FILE);
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName("new_one_xml_" + FILE_NAME);
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitTabPresent(1);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL + "m");
        IDE.OPEN_FILE_BY_PATH.waitOpened();
        IDE.OPEN_FILE_BY_PATH.clickCancelButton();
        IDE.OPEN_FILE_BY_PATH.waitClosed();

        // step 3: resetting the hotkeys to the default values and checking
        openCustomizeHotkeyForm();
        IDE.CUSTOMIZE_HOTKEYS.clickDefaultsButton();
        IDE.LOADER.waitClosed();
        assertTrue(IDE.CUSTOMIZE_HOTKEYS.isOkEnabled());
        IDE.CUSTOMIZE_HOTKEYS.okButtonClick();
        IDE.LOADER.waitClosed();
        IDE.PREFERENCES.clickOnCloseFormBtn();
        IDE.PREFERENCES.waitPreferencesClose();
        IDE.EDITOR.waitTabPresent(1);
        IDE.EDITOR.selectTab(1);
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL + "m");
        IDE.UPLOAD.waitClosed();
    }

    // close opened files in this test
    private void closeAllTabs() throws Exception {
        for (int i = 0; i < 2; i++) {
            IDE.EDITOR.closeTabIgnoringChanges(1);
        }
    }

    //sequence actions for calling Hotkeys form
    private void openCustomizeHotkeyForm() throws Exception, InterruptedException {
        IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.PREFERNCESS);
        IDE.PREFERENCES.waitPreferencesOpen();
        IDE.PREFERENCES.selectCustomizeMenu(MenuCommands.Preferences.CUSTOMIZE_HOTKEYS);
        IDE.CUSTOMIZE_HOTKEYS.waitOpened();
    }

}