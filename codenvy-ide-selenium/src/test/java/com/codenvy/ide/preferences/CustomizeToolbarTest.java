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

import org.junit.AfterClass;
import org.junit.Test;

import java.util.List;

/**
 * @author <a href="mailto:musienko.maxim@gmail.com">Musienko Maxim</a>
 *
 */
public class CustomizeToolbarTest extends BaseTest {
    @AfterClass
    public static void restoreDefault() {
        driver.manage().deleteAllCookies();
    }

    @Test
    public void CustomizeToolbartest() throws Exception {
        //step 1 (run CUSTOMIZE TOOLBAR form, delete New * [Popup], press CANCEL button. Run form again and  check
        // "New * [Popup]" element presents)
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.EXPLORER.waitOpened();
        openCustomizeHotkeyForm();
        IDE.CUSTOMIZE_TOOLBAR.selectNumElementOnCommandListbar(7);
        IDE.CUSTOMIZE_TOOLBAR.selectElementOnCommandlistbarByName("New *");
        IDE.CUSTOMIZE_TOOLBAR.selectElementOnToolbarByName("New *");
        IDE.LOADER.waitClosed();
        IDE.CUSTOMIZE_TOOLBAR.deleteClick();
        IDE.LOADER.waitClosed();
        IDE.PREFERENCES.clickOnCloseFormBtn();
        IDE.PREFERENCES.waitPreferencesClose();
        IDE.EXPLORER.waitOpened();
        openCustomizeHotkeyForm();

        IDE.CUSTOMIZE_TOOLBAR.waitToolbarElementIsPresent("New * [Popup]");

        IDE.CUSTOMIZE_TOOLBAR.selectElementOnToolbarByName("New *");
        IDE.LOADER.waitClosed();
        IDE.CUSTOMIZE_TOOLBAR.deleteClick();
        IDE.LOADER.waitClosed();
        IDE.CUSTOMIZE_TOOLBAR.okClick();
        IDE.LOADER.waitClosed();
        IDE.PREFERENCES.clickOnCloseFormBtn();
        IDE.PREFERENCES.waitPreferencesClose();
        openCustomizeHotkeyForm();

        IDE.CUSTOMIZE_TOOLBAR.waitToolbarElementIsNotPresent("New * [Popup]");

        //step 3  (restore default settings and check them)
        IDE.CUSTOMIZE_TOOLBAR.defaultClick();
        IDE.LOADER.waitClosed();

//        for (String o :  IDE.CUSTOMIZE_TOOLBAR.getallCommandList() ) {
//            System.out.println("mmmmmmmmmmmmmmmmmmmmmmmmmm: "+o);
//        }

        IDE.CUSTOMIZE_TOOLBAR.isDefaultCommandlbarList();
        IDE.CUSTOMIZE_TOOLBAR.isDefaultToolbarList();
        IDE.CUSTOMIZE_TOOLBAR.okClick();

        //step 4  (move first element down, check element in new position. Reopen Customize Toolbar form,
        // and check element in new position )
        IDE.CUSTOMIZE_TOOLBAR.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.CUSTOMIZE_TOOLBAR.selectElementOnToolbarByName("New *");
        IDE.LOADER.waitClosed();
        IDE.CUSTOMIZE_TOOLBAR.moveDownClick();
        IDE.LOADER.waitClosed();

        IDE.CUSTOMIZE_TOOLBAR.waitToolbarElementPresentInPosition(3, "New * [Popup]");

        IDE.CUSTOMIZE_TOOLBAR.okClick();
        IDE.LOADER.waitClosed();

        IDE.CUSTOMIZE_TOOLBAR.waitToolbarElementPresentInPosition(3, "New * [Popup]");

        //step 4  (Remove element which was moved Check next placement element after reopen form. (should be "Save"
        // instead of "New * [Popup]"))
        IDE.CUSTOMIZE_TOOLBAR.selectElementOnToolbarByName("New *");
        IDE.LOADER.waitClosed();
        IDE.CUSTOMIZE_TOOLBAR.deleteClick();
        IDE.LOADER.waitClosed();
        IDE.CUSTOMIZE_TOOLBAR.okClick();
        IDE.CUSTOMIZE_TOOLBAR.waitClosed();

        IDE.CUSTOMIZE_TOOLBAR.waitOpened();

        IDE.CUSTOMIZE_TOOLBAR.waitToolbarElementPresentInPosition(3, "Save");
        IDE.LOADER.waitClosed();

        //step 5 (Click on "Save" element, add delimiter, check new delimiter on toolbar. Move second delimiter on
        // one position down. And check his position)
        IDE.CUSTOMIZE_TOOLBAR.selectElementOnToolbarByName("Save");
        IDE.LOADER.waitClosed();
        IDE.CUSTOMIZE_TOOLBAR.delimiterClick();
        IDE.LOADER.waitClosed();

        IDE.CUSTOMIZE_TOOLBAR.waitToolbarElementPresentInPosition(4, "Delimiter");

        IDE.PREFERENCES.clickOnCloseFormBtn();
        IDE.PREFERENCES.waitPreferencesClose();

        //step 6 (reopen customize toolbar and set default values. Check default elements on commandlist and toolbar)
        openCustomizeHotkeyForm();
        IDE.CUSTOMIZE_TOOLBAR.defaultClick();
        IDE.LOADER.waitClosed();
        IDE.CUSTOMIZE_TOOLBAR.isDefaultCommandlbarList();
        IDE.CUSTOMIZE_TOOLBAR.isDefaultToolbarList();
    }

    private void openCustomizeHotkeyForm() throws Exception, InterruptedException {
        IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.PREFERNCESS);
        IDE.PREFERENCES.waitPreferencesOpen();
        IDE.PREFERENCES.selectCustomizeMenu(MenuCommands.Preferences.CUSTOMIZE_TOOLBAR);
        IDE.CUSTOMIZE_TOOLBAR.waitOpened();
    }

}