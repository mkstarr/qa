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
import com.codenvy.ide.core.Output;
import com.codenvy.ide.core.aws.AwsPaaS;
import com.codenvy.ide.paas.Status;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

/**
 * @author Dmytro Nochevnov
 *
 */
public class DeleteS3ObjectTst extends BaseTest {

    private AwsPaaS aws;
    private Output outputPanel;
        
    private static String ACCESS_KEY;
    private static String SECRET_KEY;
        
    private final static String BUCKET_NAME = "qa" + new Date().getTime() + "bucket";

    @BeforeClass
    public static void readAwsCredentials() {       
        ACCESS_KEY = readCredential("aws.access.key");
        SECRET_KEY = readCredential("aws.secret.key");
    } 
    
    
    @Before
    public void setup() throws Exception {        
        aws = IDE.AWS;
        outputPanel = IDE.OUTPUT; 

        IDE.EXPLORER.waitOpened();

        IDE.GET_STARTED_WIZARD.makeSureWizardIsClosed();
        
        // login
        Status status = new Status();
        aws.switchAccount(status, ACCESS_KEY, SECRET_KEY);
        status.checkSuccess();
        outputPanel.clearPanel();
        
        // create bucket

        // upload object 
    }

    @After
    public void tearDown() throws IOException, InterruptedException {
        // remove bucket at PaaSSide
        
        Status status = new Status();        
        aws.logout(status.init());
        status.checkSuccess();
    }    
        
    @Test
    public void deleteS3ObjectTest() throws Exception {
        // GIVEN
        Status status = new Status();
    }

    
}
