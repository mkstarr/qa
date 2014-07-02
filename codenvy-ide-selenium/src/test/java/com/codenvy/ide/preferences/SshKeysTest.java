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

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class SshKeysTest extends BaseTest {
    final String HOST = "blabla.com";

    @Test
    public void sshAddTest() throws Exception {
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.EXPLORER.waitOpened();
        IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.PREFERNCESS);
        IDE.PREFERENCES.waitPreferencesOpen();
        IDE.PREFERENCES.selectCustomizeMenu(MenuCommands.Preferences.SSH_KEY);
        IDE.LOADER.waitClosed();
        IDE.SSH.waitSshView();
        IDE.SSH.clickGenerateBtn();
        IDE.SSH.waitSshAskForm();
        IDE.SSH.typeHostToSshAsk(HOST);
        IDE.SSH.cliclOkBtnSshAsk();
        IDE.LOADER.waitClosed();
        IDE.SSH.waitSshAskFormClose();
        IDE.SSH.waitAppearContentInSshListGrig("blabla.com");
        assertThat(IDE.SSH.getAllKeysList().split("\n")).contains("blabla.com", "View", "Delete");
    }

    @Test
    public void sshPreviewKeyTest() throws Exception {
        IDE.SSH.clickViewKeyInGridPosition("blabla.com");
        IDE.LOADER.waitClosed();
        IDE.SSH.waitAppearSshKeyManadger();
        //need for redraw in google chrom
        Thread.sleep(500);
        assertTrue(IDE.SSH.getSshKeyHash().startsWith("ssh-rsa"));
        assertTrue(IDE.SSH.getSshKeyHash().length() > 378);
        IDE.SSH.clickOnCloseSshKeyManager();
        IDE.SSH.waitCloseSshKeyManadger();
    }

    @Test
    public void chekVisibleKeysAfterSwitch() throws Exception {
        IDE.PREFERENCES.selectCustomizeMenu("Customize Toolbar");
        IDE.CUSTOMIZE_TOOLBAR.waitOpened();
        IDE.PREFERENCES.selectCustomizeMenu(MenuCommands.Preferences.SSH_KEY);
        IDE.LOADER.waitClosed();
        IDE.SSH.waitSshView();
        IDE.SSH.waitAppearContentInSshListGrig("blabla.com");
        assertThat(IDE.SSH.getAllKeysList().split("\n")).contains("blabla.com", "View", "Delete");
    }

    @Test
    public void deleteCreatedKey() throws Exception {
        IDE.SSH.clickDeleteKeyInGridPosition("blabla.com");
        IDE.ASK_DIALOG.waitOpened();
        IDE.ASK_DIALOG.clickYes();
        IDE.ASK_DIALOG.waitClosed();
        IDE.SSH.waitDisappearContentInSshListGrig("blabla.com");
    }

    // TODO impossible input path to key file
//    @Test
//    public void updaloadSshKey() throws InterruptedException {
//
//        IDE.SSH.clickOnUploadSshkeyButton();
//        IDE.SSH.waitSshUploadForm();
//        IDE.SSH.typeHostNameInUploadSshForm("blabla2.com");
//        IDE.SSH.typePathToFileInUploadSshForm("/home/roman/qa-ukr-team-stg.key");
//    }
}
