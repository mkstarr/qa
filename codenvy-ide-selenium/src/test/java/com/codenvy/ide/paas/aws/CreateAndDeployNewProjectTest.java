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
import com.codenvy.ide.TestConstants;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Build;
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
import static org.junit.Assert.assertTrue;

/**
 * @author Dmytro Nochevnov
 *
 */
public class CreateAndDeployNewProjectTest extends BaseTest {

    private AwsPaaS aws;
    private Output outputPanel;
    private Build buildPanel;
        
    private static String ACCESS_KEY;
    private static String SECRET_KEY;
    
    private final static String PROJECT_NAME = "qa" + new Date().getTime();
    
    @BeforeClass
    public static void readAwsCredentials() {       
        ACCESS_KEY = readCredential("aws.access.key");
        SECRET_KEY = readCredential("aws.secret.key");
    } 
    
    @Before
    public void setup() throws Exception {       
        aws = IDE.AWS;
        outputPanel = IDE.OUTPUT; 
        buildPanel = IDE.BUILD;

        IDE.EXPLORER.waitOpened();

        IDE.GET_STARTED_WIZARD.makeSureWizardIsClosed();
        
        Status status = new Status();
        aws.switchAccount(status, ACCESS_KEY, SECRET_KEY);
        status.checkSuccess();
        outputPanel.clearPanel();
    }
    
    @After
    public void tearDown() throws IOException, InterruptedException {        
        Status status = new Status();
        
        aws.terminateFirstEnvironmentOfCurrentProjectApplication(status.init());
        status.checkSuccess();

        aws.deleteFirstVersionOfCurrentProject(status.init(), true);
        status.checkSuccess();        
        
        aws.deleteCurrentProjectApplication(status.init(), PROJECT_NAME);
        status.checkSuccess();
        
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
    public void createAndDeployDefaultSpringProjectTest() throws Exception {
        // GIVEN
        Status status = new Status();
              
        final String DEFAULT_SOLUTION_STACK = "32bit Amazon Linux 2013.09 running Tomcat 7 Java 7";
        final String ENVIRONMENT_NAME = PROJECT_NAME + "env";
        final String DEFAULT_APPLICATION_URL_PREFIX = "http://" + ENVIRONMENT_NAME;
        final String DEFAULT_RUNNING_VERSION = "initial version";
        
        AwsApplicationInfo expectedInfo = new AwsApplicationInfo();
        expectedInfo.setApplicationProperty(AwsApplicationInfo.Property.NAME, PROJECT_NAME);
        expectedInfo.setApplicationProperty(AwsApplicationInfo.Property.SOLUTION_STACK, DEFAULT_SOLUTION_STACK);        
        expectedInfo.setApplicationProperty(AwsApplicationInfo.Property.ENVIRONMENT_NAME, ENVIRONMENT_NAME);
        expectedInfo.setApplicationProperty(AwsApplicationInfo.Property.RUNNING_VERSION, DEFAULT_RUNNING_VERSION);        
        expectedInfo.setApplicationProperty(AwsApplicationInfo.Property.STATUS, AwsApplicationInfo.Status.Ready.name());
        expectedInfo.setApplicationProperty(AwsApplicationInfo.Property.HEALTH, AwsApplicationInfo.Health.Green.name());
        expectedInfo.setApplicationProperty(AwsApplicationInfo.Property.URL, DEFAULT_APPLICATION_URL_PREFIX);

        
        final String OUTPUT_PANEL_STARTING_MESSAGE = String.format("[INFO] Project successfully built.\n[INFO] Application %1$s is successfully created on Elastic Beanstalk.\n[INFO] Launching Environment %2$s...", PROJECT_NAME, ENVIRONMENT_NAME);
        final String OUTPUT_PANEL_MIDDLE_MESSAGE = String.format("[INFO] Application %1$s successfully started on http://%2$s", PROJECT_NAME, ENVIRONMENT_NAME);
        final String OUTPUT_PANEL_ENDING_MESSAGE = ".elasticbeanstalk.com";  
        
        final String BUILD_PANEL_MESSAGE = String.format("Building project %1$s\nFinished building project %1$s.\nResult: Successful", PROJECT_NAME);
        
        final String MESSAGE_FROM_APPLICATION_MAIN_PAGE_XPATH = "//span[contains(text(), 'Enter your name:')]";
        
        // WHEN
        aws.createSimpleSpringProject(status.init(), PROJECT_NAME, ENVIRONMENT_NAME);        
        
        // THEN
        status.checkSuccess();
        checkOutputContainMessage(OUTPUT_PANEL_STARTING_MESSAGE);        
        checkOutputContainMessage(OUTPUT_PANEL_MIDDLE_MESSAGE);        
        checkOutputContainMessage(OUTPUT_PANEL_ENDING_MESSAGE);        
        checkAllBuilderMessages(BUILD_PANEL_MESSAGE);

        // THEN
        aws.gotoLinkFromOutputPanelAndCheckRunApplication(status.init(), DEFAULT_APPLICATION_URL_PREFIX, MESSAGE_FROM_APPLICATION_MAIN_PAGE_XPATH);
        if (status.isFail()) {
            sleepInTest(TestConstants.LONG_WAIT_IN_SEC);   // give AWS additional time to start environment and application
   
            aws.gotoLinkFromOutputPanelAndCheckRunApplication(status.init(), DEFAULT_APPLICATION_URL_PREFIX, MESSAGE_FROM_APPLICATION_MAIN_PAGE_XPATH);
            status.checkSuccess();
        }
        
        // THEN
        AwsApplicationInfo applicationInfo = aws.getApplicationInfoFromApplicationManagerWindow(status.init());
        status.checkSuccess();
        applicationInfo.checkApplicationProperties(expectedInfo);
        
        // THEN
        aws.checkApplicationExistsAtPaaSSide(status.init(), PROJECT_NAME);
        status.checkSuccess();
    }

    
    /**
     * Verifications 
     */
    
    private void checkOutputContainMessage(String expectedMessage) {
        String outputPanelMessages = outputPanel.getAllMessagesFromOutput();
        String errorMessage = String.format("Output panel '%s' doesn't contain expected message '%s'", outputPanelMessages, expectedMessage);
        assertTrue(errorMessage, outputPanel.getAllMessagesFromOutput().contains(expectedMessage));
    }
    
    private void checkAllBuilderMessages(String expectedMessages) {        
        buildPanel.selectBuilderOutputTab();
        assertEquals(expectedMessages, buildPanel.getOutputMessage());
    }
}