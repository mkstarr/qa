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
package com.codenvy.ide.miscellaneous;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;

import org.junit.Test;

/**
 * Created by The eXo Platform SAS.
 *
 * @author Oksana Vereshchaka
 */
public class DialogAboutTest extends BaseTest {

    @Test
    public void testDialogAbout() throws Exception {
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.EXPLORER.waitOpened();
        //call heplp menu and submenu About
        IDE.MENU.runCommand(MenuCommands.Help.HELP, MenuCommands.Help.ABOUT);

        //check present logo
        IDE.ABOUT.waitLogoPresent();

        //check all basic elements on About form
        IDE.ABOUT.waitCheckInfoAboutWindow();

        //close dialogAboutMenu
        IDE.ABOUT.closeDialogAboutForm();
    }
}
