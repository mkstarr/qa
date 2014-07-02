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
package com.codenvy.ide.paas.aws;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.core.aws.AwsPaaS;
import com.codenvy.ide.paas.Status;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Dmytro Nochevnov
 *
 */
public class SwitchAccountTest extends BaseTest {

    private AwsPaaS aws;
    
    private static String ACCESS_KEY;
    private static String SECRET_KEY;
    
    @BeforeClass
    public static void readAwsCredentials() {       
        ACCESS_KEY = readCredential("aws.access.key");
        SECRET_KEY = readCredential("aws.secret.key");
    } 
    
    @Before
    public void setup() throws InterruptedException {        
        aws = IDE.AWS;      

        IDE.EXPLORER.waitOpened();

        IDE.GET_STARTED_WIZARD.makeSureWizardIsClosed();   // TODO should be re-writed 
    }
    
    @After
    public void tearDown() {
        Status status = new Status();
        aws.logout(status);
        status.checkSuccess();        
    }
    
    @Test
    public void switchAccountTest() throws Exception {
        // GIVEN
        Status status = new Status();
        
        // WHEN
        aws.switchAccount(status, ACCESS_KEY, SECRET_KEY);
        
        // THEN
        status.checkSuccess();
    }
    
    @Test
    public void switchToAccountWithWrongCredentialsTest() {
        // GIVEN
        Status status = new Status();
        
        final String WRONG_ACCESS_KEY = "wrong access key";
        final String WRONG_SECRET_KEY = "wrong secrete key";
        final String ERROR_MESSAGE = "Invalid access or/and secret key value. Try again.";

        // WHEN
        aws.switchAccount(status, WRONG_ACCESS_KEY, WRONG_SECRET_KEY);

        // THEN
        status.checkFail();
        status.checkPaaSErrorMessage(ERROR_MESSAGE);
    }
}
