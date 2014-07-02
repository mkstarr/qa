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
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Output;
import com.codenvy.ide.core.aws.AwsPaaS;
import com.codenvy.ide.paas.Status;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.mail.MessagingException;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;

/**
 * @author Dmytro Nochevnov
 *
 */
public class DeleteApplicationTest extends BaseTest {

    private AwsPaaS aws;
    private Output outputPanel;
        
    private static String ACCESS_KEY;
    private static String SECRET_KEY;
        
    private final static String PROJECT_NAME = "qa" + new Date().getTime();
    
    private final String ENVIRONMENT_NAME = PROJECT_NAME + "env";

    @BeforeClass
    public static void readAwsCredentials() {       
        ACCESS_KEY = readCredential("aws.access.key");
        SECRET_KEY = readCredential("aws.secret.key");
    } 
    
    @BeforeClass
    public static void createProject() {
        try {
            VirtualFileSystemUtils
                    .importZipProject(PROJECT_NAME,
                                      "src/test/resources/org/exoplatform/ide/extension/maven/TestSpringProjectWithPOM.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        
        // create default application
        IDE.OPEN.openProject(PROJECT_NAME);
        aws.createDefaultApplicationForCurrentProject(status.init(), PROJECT_NAME, ENVIRONMENT_NAME);
        status.checkSuccess();
        
        aws.waitUntilSimpleStringApplicationStarts(status.init(), PROJECT_NAME, ENVIRONMENT_NAME);
        status.checkSuccess();
    }

    @After
    public void tearDown() throws IOException, InterruptedException {
        Status status = new Status();
        
        aws.logout(status.init());
        status.checkSuccess();
        
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
    }    

    @AfterClass
    public static void removeProject() throws IOException, InterruptedException, TimeoutException, MessagingException {
        if (VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT_NAME).getStatusCode() == 200) {
            VirtualFileSystemUtils.delete(PROJECT_NAME);
        }
    }    
        
    @Test
    public void deleteApplicationTest() throws Exception {
        // GIVEN
        Status status = new Status();
        String DELETE_APPLICATION_FAIL_MESSAGE = String.format("[ERROR] Deleting application %1$s failed.\n"
           + "Unable to delete application %1$s because it has a version that is deployed to a running environment.", PROJECT_NAME);
        
        // WHEN
        aws.deleteCurrentProjectApplication(status, PROJECT_NAME);
        // THEN
        status.checkFail();
        checkLastOutputPanelMessage(DELETE_APPLICATION_FAIL_MESSAGE);
        outputPanel.clearPanel();
        
        // WHEN
        aws.terminateFirstEnvironmentOfCurrentProjectApplication(status.init());
        // THEN
        status.checkSuccess();        
        
        // WHEN
        aws.deleteFirstVersionOfCurrentProject(status.init(), true);
        // THEN
        status.checkSuccess();
        
        // WHEN
        aws.deleteCurrentProjectApplication(status.init(), PROJECT_NAME);
        // THEN
        status.checkSuccess();
        
        // THEN
        //// check application is absence at PaaS side
        aws.checkApplicationExistsAtPaaSSide(status.init(), PROJECT_NAME);
        status.checkFail();
    }

    
    /**
     * Verifications
     */
    
    /**
     * Assert equals expectedMessage and last message in Output Panel
     * @param expectedMessage
     */
    public void checkLastOutputPanelMessage(String expectedMessage) {      
        assertEquals(expectedMessage, outputPanel.getLastMessage());
    }
}
