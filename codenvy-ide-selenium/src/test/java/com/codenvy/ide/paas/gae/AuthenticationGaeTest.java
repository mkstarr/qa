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

package com.codenvy.ide.paas.gae;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;

/**
 * @author Zaryana Dombrovskaya
 */

public class AuthenticationGaeTest extends BaseTest {

    private String outputMessLogout = "[INFO] You are not logged in Google App Engine.";

    @Before
    public void setUp() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.GET_STARTED_WIZARD.makeSureWizardIsClosed();
    }


    @After
    public void tearDown() throws Exception {
        IDE.GOOGLE.openGoogleAccountPage();
        IDE.GOOGLE.deleteGoogleTokens();
    }


    @Test
    public void authentication() throws Exception {
        IDE.GAE.login();
        IDE.GAE.logout(outputMessLogout);
        IDE.MENU.waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.GoogleAppEngine.GOOGLEAPPENGINE, MenuCommands.PaaS.GoogleAppEngine.LOGIN);
        IDE.MENU.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.GoogleAppEngine.GOOGLEAPPENGINE, MenuCommands.PaaS.GoogleAppEngine.LOGIN);
        IDE.MENU.waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.GoogleAppEngine.GOOGLEAPPENGINE, MenuCommands.PaaS.GoogleAppEngine.LOGOUT);
    }

}
