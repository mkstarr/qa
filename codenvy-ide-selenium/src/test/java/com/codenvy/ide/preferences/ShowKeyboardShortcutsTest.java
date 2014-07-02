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

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.Keys;

import static org.junit.Assert.assertTrue;

/**
 * IDE-1597: Add "Show Keyboard Shortcuts" feature in Help menu.
 *
 * @author <a href="mailto:azatsarynnyy@exoplatform.org">Artem Zatsarynnyy</a>
 *
 */
public class ShowKeyboardShortcutsTest extends BaseTest {

    @After
    public void restoreDefault() {
        try {
            IDE.CUSTOMIZE_HOTKEYS.resetToDefaults();
        } catch (Exception e) {
        }
    }

    @Test
    public void testView() throws Exception {
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.EXPLORER.waitOpened();
        IDE.MENU.runCommand(MenuCommands.Help.HELP, MenuCommands.Help.SHOW_KEYBOARD_SHORTCUTS);
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitOpened();
        IDE.SHOW_KEYBOARD_SHORTCUTS.isCloseButtonEnabled();

        // check Ctrl+S is present
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitShortcutIsPresent("Ctrl+S");
        // check Ctrl+M is absent
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitShortcutIsNotPresent("Ctrl+M");

        IDE.SHOW_KEYBOARD_SHORTCUTS.closeButtonClick();
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitClosed();

        // change shortcut for Save command
        //      IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.CUSTOMIZE_HOTKEYS);
        openCustomizeHotkeyForm();
        IDE.CUSTOMIZE_HOTKEYS.selectElementOnCommandlistbarByName(ToolbarCommands.File.SAVE);
        IDE.CUSTOMIZE_HOTKEYS.typeKeys(Keys.CONTROL.toString() + "m");
        IDE.CUSTOMIZE_HOTKEYS.bindButtonClick();
        IDE.CUSTOMIZE_HOTKEYS.waitOkEnabled();
        IDE.CUSTOMIZE_HOTKEYS.okButtonClick();
        IDE.LOADER.waitClosed();
        closeCustomizeHotkeyForm();
        Thread.sleep(2000);
        // refresh and check shortcuts
        driver.navigate().refresh();
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.EXPLORER.waitOpened();

        IDE.MENU.runCommand(MenuCommands.Help.HELP, MenuCommands.Help.SHOW_KEYBOARD_SHORTCUTS);
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitOpened();
        Thread.sleep(2000);
        // check new shortcut Ctrl+M is present
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitShortcutIsPresent("Ctrl+M");

        IDE.SHOW_KEYBOARD_SHORTCUTS.closeButtonClick();
        IDE.SHOW_KEYBOARD_SHORTCUTS.waitClosed();
    }

    private void openCustomizeHotkeyForm() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.PREFERNCESS);
        IDE.PREFERENCES.waitPreferencesOpen();
        IDE.PREFERENCES.selectCustomizeMenu(MenuCommands.Preferences.CUSTOMIZE_HOTKEYS);
        IDE.CUSTOMIZE_HOTKEYS.waitOpened();
    }

    private void closeCustomizeHotkeyForm() throws Exception {
        IDE.PREFERENCES.waitPreferencesOpen();
        IDE.PREFERENCES.clickOnCloseFormBtn();
        IDE.PREFERENCES.waitPreferencesClose();
    }
}