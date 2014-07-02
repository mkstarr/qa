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
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.preferences.AbstractCustomizeHotkeys.Commands;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * IDE-156:HotKeys customization.
 * <p/>
 * Created by The eXo Platform SAS.
 *
 * @author <a href="mailto:musienko.maxim@gmail.com">Musienko Maxim</a>
 * @author Oksana Vereshchaka
 *
 */
public class HotkeysFormTest extends BaseTest {

    private final static String PROJECT = HotkeysFormTest.class.getSimpleName();

    @AfterClass
    public static void tearDown() {

        try {
            VirtualFileSystemUtils.delete(PROJECT);
            // restore to defaults hotkeys
            IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.PREFERNCESS);
            IDE.PREFERENCES.waitPreferencesOpen();
            IDE.PREFERENCES.selectCustomizeMenu(MenuCommands.Preferences.CUSTOMIZE_HOTKEYS);
            IDE.CUSTOMIZE_HOTKEYS.waitOpened();
            IDE.CUSTOMIZE_HOTKEYS.clickDefaultsButton();
            IDE.LOADER.waitClosed();
            IDE.PREFERENCES.clickOnCloseFormBtn();
            driver.manage().deleteAllCookies();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeClass
    public static void setUp() throws Exception {
        try {
            VirtualFileSystemUtils.createDefaultProject(PROJECT);
        } catch (IOException e) {
        }
    }

    @Test
    public void testFormAndButtons() throws Exception {
        // step 1 create new project, open Customize Hotkey form
        // select CSS file and checks status of buttons
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EDITOR.waitTabPresent(0);
        openCustomizeHotkeyForm();
        IDE.CUSTOMIZE_HOTKEYS.selectElementOnCommandlistbarByName(Commands.NEW_CSS_FILE);
        IDE.CUSTOMIZE_HOTKEYS.isBindDisabled();
        IDE.CUSTOMIZE_HOTKEYS.isUnBindEnabled();
        IDE.CUSTOMIZE_HOTKEYS.isKeyFieldActive(true);

        // step2 deselect row and check elements state

        // reset all selection in hotkey form
        IDE.PREFERENCES.selectCustomizeMenu(MenuCommands.Preferences.CUSTOMIZE_TOOLBAR);
        IDE.CUSTOMIZE_TOOLBAR.waitOpened();
        IDE.PREFERENCES.selectCustomizeMenu(MenuCommands.Preferences.CUSTOMIZE_HOTKEYS);
        IDE.CUSTOMIZE_HOTKEYS.waitOpened();

        IDE.CUSTOMIZE_HOTKEYS.isBindDisabled();
        IDE.CUSTOMIZE_HOTKEYS.isUnBindDisabled();
        IDE.CUSTOMIZE_HOTKEYS.isOkDisabled();

        // step3 select 'Save' raw and check state buttons and hotkey field
        IDE.CUSTOMIZE_HOTKEYS.selectElementOnCommandlistbarByName(ToolbarCommands.File.SAVE);
        IDE.CUSTOMIZE_HOTKEYS.isBindDisabled();
        IDE.CUSTOMIZE_HOTKEYS.isUnBindEnabled();
        IDE.CUSTOMIZE_HOTKEYS.isKeyFieldActive(true);
        assertEquals("Ctrl+S", IDE.CUSTOMIZE_HOTKEYS.getTextTypeKeys());

        // step4 set new hotkey value for selected (ctrl+K) row and check elements state
        IDE.CUSTOMIZE_HOTKEYS.typeKeys(Keys.CONTROL.toString() + "k");
        IDE.CUSTOMIZE_HOTKEYS.isBindEnabled();
        IDE.CUSTOMIZE_HOTKEYS.isUnBindEnabled();
        IDE.CUSTOMIZE_HOTKEYS.isOkDisabled();

        assertEquals("Ctrl+K", IDE.CUSTOMIZE_HOTKEYS.getTextTypeKeys());

        // step5 click on bind button and checked changes elements state
        IDE.CUSTOMIZE_HOTKEYS.bindButtonClick();
        IDE.CUSTOMIZE_HOTKEYS.isBindDisabled();
        IDE.CUSTOMIZE_HOTKEYS.isUnBindEnabled();
        IDE.CUSTOMIZE_HOTKEYS.isOkEnabled();
        IDE.CUSTOMIZE_HOTKEYS.selectElementOnCommandlistbarByName(ToolbarCommands.File.SAVE);
        assertEquals("Ctrl+K", IDE.CUSTOMIZE_HOTKEYS.getTextTypeKeys());
        IDE.PREFERENCES.clickOnCloseFormBtn();
        IDE.PREFERENCES.waitPreferencesClose();

    }

    @Test
    public void testBindingAndUnbindingNewHotkey() throws Exception {
        // step 1 bind for CSS command new value, check state elements, save changes
        openCustomizeHotkeyForm();

        IDE.CUSTOMIZE_HOTKEYS.selectElementOnCommandlistbarByName(Commands.NEW_CSS_FILE);
        IDE.CUSTOMIZE_HOTKEYS.typeKeys(Keys.CONTROL.toString() + "m");
        IDE.CUSTOMIZE_HOTKEYS.isAlredyNotView();
        IDE.CUSTOMIZE_HOTKEYS.isBindEnabled();
        IDE.CUSTOMIZE_HOTKEYS.bindButtonClick();
        IDE.CUSTOMIZE_HOTKEYS.waitOkEnabled();
        IDE.CUSTOMIZE_HOTKEYS.okButtonClick();
        IDE.LOADER.waitClosed();
        IDE.PREFERENCES.clickOnCloseFormBtn();
        IDE.PREFERENCES.waitPreferencesClose();

        // step 2 check
        driver.switchTo().activeElement().sendKeys(Keys.CONTROL.toString() + "m");
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.clickCreateButton();

        // TODO on this moment after click on create button editor cannot open
        //but after fix it we chould return behavior 
        // IDE.EDITOR.waitTabPresent(1);
        // IDE.EDITOR.isTabPresentInEditorTabset("Untitled file.css");
        // IDE.EDITOR.closeFile(1);
        //IDE.EXPLORER.waitForItem(PROJECT);

        IDE.EXPLORER.waitForItem(PROJECT + "/Untitled file.css");
        openCustomizeHotkeyForm();

        IDE.CUSTOMIZE_HOTKEYS.selectElementOnCommandlistbarByName(Commands.NEW_CSS_FILE);
        assertEquals("Ctrl+M", IDE.CUSTOMIZE_HOTKEYS.getTextTypeKeys());
        IDE.CUSTOMIZE_HOTKEYS.unbindButtonClick();
        IDE.CUSTOMIZE_HOTKEYS.waitOkEnabled();
        IDE.CUSTOMIZE_HOTKEYS.okButtonClick();
        IDE.LOADER.waitClosed();
        IDE.PREFERENCES.clickOnCloseFormBtn();
        IDE.PREFERENCES.waitPreferencesClose();
        driver.switchTo().activeElement().sendKeys(Keys.CONTROL.toString() + "m");
        IDE.EDITOR.waitTabNotPresent("Untitled file.css *");
    }

    @Test
    public void testTryToBindForbiddenHotkeys() throws Exception {

        openCustomizeHotkeyForm();
        IDE.CUSTOMIZE_HOTKEYS.selectElementOnCommandlistbarByName(Commands.NEW_TEXT_FILE);
        IDE.CUSTOMIZE_HOTKEYS.typeKeys("y");
        IDE.CUSTOMIZE_HOTKEYS.isFirstKeyMessageView();
        IDE.CUSTOMIZE_HOTKEYS.typeKeys("8");
        IDE.CUSTOMIZE_HOTKEYS.isFirstKeyMessageView();
        IDE.CUSTOMIZE_HOTKEYS.typeKeys("n");
        IDE.CUSTOMIZE_HOTKEYS.isFirstKeyMessageView();

        // step 2 Presses Ctrl and Alt and check states buttons and messages on the form
        IDE.CUSTOMIZE_HOTKEYS.selectElementOnCommandlistbarByName(Commands.NEW_CSS_FILE);
        IDE.CUSTOMIZE_HOTKEYS.typeKeys(Keys.CONTROL.toString());
        IDE.CUSTOMIZE_HOTKEYS.isHoltMessageView();
        assertEquals("Ctrl+", IDE.CUSTOMIZE_HOTKEYS.getTextTypeKeys());
        IDE.CUSTOMIZE_HOTKEYS.isBindDisabled();
        IDE.CUSTOMIZE_HOTKEYS.typeKeys(Keys.ALT.toString());
        assertEquals("Alt+", IDE.CUSTOMIZE_HOTKEYS.getTextTypeKeys());
        IDE.CUSTOMIZE_HOTKEYS.isBindDisabled();
        IDE.CUSTOMIZE_HOTKEYS.isOkDisabled();
        IDE.CUSTOMIZE_HOTKEYS.typeKeys(Keys.SHIFT.toString() + "n");

        // step 3 checking forbidden values, that are reserved by editors
        IDE.CUSTOMIZE_HOTKEYS.typeKeys(Keys.CONTROL.toString() + "c");
        IDE.CUSTOMIZE_HOTKEYS.isHotKeyUsedMessageView();
        IDE.CUSTOMIZE_HOTKEYS.typeKeys(Keys.CONTROL.toString() + "p");
        IDE.CUSTOMIZE_HOTKEYS.isAlredyNotView();
        IDE.CUSTOMIZE_HOTKEYS.isBindEnabled();
        IDE.CUSTOMIZE_HOTKEYS.isOkDisabled();
        IDE.CUSTOMIZE_HOTKEYS.bindButtonClick();
        IDE.CUSTOMIZE_HOTKEYS.waitOkEnabled();
        IDE.CUSTOMIZE_HOTKEYS.isOkEnabled();

        // after fix issue IDE 1420 string 213 should be remove
        IDE.CUSTOMIZE_HOTKEYS.selectElementOnCommandlistbarByName(Commands.NEW_HTML_FILE);
        IDE.CUSTOMIZE_HOTKEYS.selectElementOnCommandlistbarByName(Commands.NEW_CSS_FILE);
        IDE.CUSTOMIZE_HOTKEYS.typeKeys(Keys.CONTROL.toString() + "p");
        IDE.CUSTOMIZE_HOTKEYS.isAlreadyToThisCommandMessView();
        IDE.PREFERENCES.clickOnCloseFormBtn();
        IDE.PREFERENCES.waitPreferencesClose();
    }

    @Test
    public void testUnbindingDefaultHotkey() throws Exception {
        openCustomizeHotkeyForm();

        // step 2: preconditioning: Select XML file, set new key bind and
        // check this work hotkey in IDE
        IDE.CUSTOMIZE_HOTKEYS.selectElementOnCommandlistbarByName(Commands.NEW_CSS_FILE);
        IDE.CUSTOMIZE_HOTKEYS.typeKeys(Keys.CONTROL.toString() + "m");
        IDE.CUSTOMIZE_HOTKEYS.waitBindEnabled();
        IDE.CUSTOMIZE_HOTKEYS.bindButtonClick();
        IDE.CUSTOMIZE_HOTKEYS.waitOkEnabled();

        IDE.CUSTOMIZE_HOTKEYS.okButtonClick();
        IDE.LOADER.waitClosed();
        IDE.PREFERENCES.clickOnCloseFormBtn();
        IDE.PREFERENCES.waitPreferencesClose();
        IDE.EXPLORER.waitForItem(PROJECT);

        driver.switchTo().activeElement().sendKeys(Keys.CONTROL.toString() + "m");
        IDE.FILE.waitCreateNewFileWindow();
        IDE.FILE.typeNewFileName("newCssFile");
        IDE.FILE.clickCreateButton();
        IDE.EDITOR.waitTabPresent("newCssFile");
        IDE.EDITOR.isEditorTabSelected("Untitled file.css");
        IDE.LOADER.waitClosed();
        IDE.EDITOR.closeFile("Untitled file.css");
        IDE.EXPLORER.waitForItem(PROJECT);

        // step 3: unbind new hotkey and check this in IDE
        openCustomizeHotkeyForm();
        IDE.CUSTOMIZE_HOTKEYS.selectElementOnCommandlistbarByName(Commands.NEW_CSS_FILE);
        IDE.CUSTOMIZE_HOTKEYS.waitUnBindEnabled();
        IDE.CUSTOMIZE_HOTKEYS.unbindButtonClick();
        IDE.CUSTOMIZE_HOTKEYS.waitOkEnabled();
        IDE.CUSTOMIZE_HOTKEYS.okButtonClick();
        IDE.LOADER.waitClosed();
        IDE.PREFERENCES.clickOnCloseFormBtn();
        IDE.PREFERENCES.waitPreferencesClose();
        IDE.EXPLORER.waitForItem(PROJECT);
        driver.switchTo().activeElement().sendKeys(Keys.CONTROL.toString() + "m");
        IDE.EDITOR.waitTabNotPresent("Untitled file.css");
    }

    private void openCustomizeHotkeyForm() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.PREFERNCESS);
        IDE.PREFERENCES.waitPreferencesOpen();
        IDE.PREFERENCES.selectCustomizeMenu(MenuCommands.Preferences.CUSTOMIZE_HOTKEYS);
        IDE.CUSTOMIZE_HOTKEYS.waitOpened();
    }

}
