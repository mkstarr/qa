/*
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
package com.codenvy.ide.paas.appfog;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.core.appfog.AppFogPaaS;
import com.codenvy.ide.paas.Status;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Dmytro Nochevnov
 *
 */
public class SwitchAccountTest extends BaseTest {

    private AppFogPaaS appFog;
        
    private final String EMAIL = IDE_SETTINGS.getString("appfog.email");
    private final String PASSWORD = IDE_SETTINGS.getString("appfog.password");
    
    @Before
    public void setup() throws InterruptedException {        
        appFog = IDE.APP_FOG;       

        IDE.EXPLORER.waitOpened();

        IDE.GET_STARTED_WIZARD.makeSureWizardIsClosed();   // TODO should be re-writed 
    }
    
    @After
    public void tearDown() {
        Status status = new Status();
        appFog.logout(status);
        status.checkSuccess();        
    }
    
    @Test
    public void switchAccountTest() throws Exception {
        // GIVEN
        Status status = new Status();
        
        // WHEN
        appFog.switchAccount(status, EMAIL, PASSWORD);
        
        // THEN
        status.checkSuccess();
    }
    
    @Test
    public void switchToAccountWithWrongCredentialsTest() {
        // GIVEN
        Status status = new Status();
        
        final String WRONG_EMAIL = "unexisted";
        final String WRONG_PASSWORD = "wrong";
        final String ERROR_MESSAGE = "Invalid username or password. Try again.";

        // WHEN
        appFog.switchAccount(status, WRONG_EMAIL, WRONG_PASSWORD);

        // THEN
        status.checkFail();
        status.checkPaaSErrorMessage(ERROR_MESSAGE);
    }
}
