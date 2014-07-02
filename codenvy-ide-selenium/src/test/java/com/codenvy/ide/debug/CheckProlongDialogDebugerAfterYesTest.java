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
package com.codenvy.ide.debug;

import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/** @author Musienko Maxim */

public class CheckProlongDialogDebugerAfterYesTest extends DebuggerServices {
    private static final String PROJECT = CheckProlongDialogDebugerAfterYesTest.class.getSimpleName();

    protected static Map<String, Link> project;

    @BeforeClass
    public static void before() {

        try {
            project =
                    VirtualFileSystemUtils.importZipProject(PROJECT,


                                                            "src/test/resources/org/exoplatform/ide/debug/debugStepIntoStepOver.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @After
    public void tearDown() throws IOException, InterruptedException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void checkAskDialogWithProngDebuggerSesionAndProlong() throws Exception {
        runDebugApp(PROJECT);
        IDE.OUTPUT.clickOnOutputTab();
        IDE.OUTPUT.waitLinkWithParticalLinkText("run");
        String lnk = IDE.OUTPUT.getUrlTextText("run");
        IDE.DEBUGER.waitTabOfDebuger();
        IDE.DEBUGER.waitExparedSessionView(9, 60);
        IDE.DEBUGER.waitExparedSessionYesBtn();
        IDE.DEBUGER.expiredYesBtnClick();
        IDE.DEBUGER.waitExparedSessionViewIsClose();
        IDE.DEBUGER.waitTabOfDebuger();
        IDE.DEBUGER.clickOnDebugerTab();
        IDE.DEBUGER.waitDisconnectBtnIsEnabled(true);
        IDE.DEBUGER.clickDisconnectBtnClick();
        IDE.DEBUGER.waitDebugerIsClosed();
        IDE.OUTPUT.waitForSubTextPresent("stopped.");

    }
}
