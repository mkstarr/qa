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
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.Utils;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Build;
import com.codenvy.ide.core.Output;
import com.codenvy.ide.core.appfog.AppFogPaaS;
import com.codenvy.ide.paas.Status;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import javax.mail.MessagingException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;

/**
 * @author Dmytro Nochevnov
 *
 */
public class CreateAndDeployNewProjectTest extends BaseTest {

    private AppFogPaaS appFog;
    private Output outputPanel;
    private Build buildPanel;
        
    private final String EMAIL = IDE_SETTINGS.getString("appfog.email");
    private final String PASSWORD = IDE_SETTINGS.getString("appfog.password");
    private final String TOKEN = IDE_SETTINGS.getString("appfog.token");
    
    private final static String PROJECT_NAME = CreateAndDeployNewProjectTest.class.getSimpleName().toLowerCase() + "_at_" + Utils.getCurrentTimeForProjectName();
        
    @Before
    public void setup() throws Exception {       
        appFog = IDE.APP_FOG;
        outputPanel = IDE.OUTPUT; 
        buildPanel = IDE.BUILD;

        IDE.EXPLORER.waitOpened();

        IDE.GET_STARTED_WIZARD.makeSureWizardIsClosed();
        
        Status status = new Status();
        appFog.switchAccount(status, EMAIL, PASSWORD);
        status.checkSuccess();
        outputPanel.clearPanel();
    }
    
    @After
    public void tearDown() throws IOException, InterruptedException {        
        Status status = new Status();
        appFog.deleteApplicationAtPaaSSide(status, PROJECT_NAME, TOKEN);
        status.checkSuccess();
        
        appFog.logout(status.init());
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
              
        final String DEFAULT_APPLICATION_DOMAIN_NAME = String.format("%s.aws.af.cm", PROJECT_NAME);
        final String DEFAULT_APPLICATION_URL = String.format("http://%s", DEFAULT_APPLICATION_DOMAIN_NAME);
        final String DEFAULT_SPRING_MEMORY = "768MB";
        final String DEFAULT_INSTANCES_NUMBER = "1";
        final String DEFAULT_INFRASTRUCTURE = "aws";
        final String DEFAULT_STACK = "java";
        final String DEFAULT_MODEL = "spring"; 
        final String DEFAULT_SERVICES = "";
        
        AppFogApplicationInfo expectedInfo = new AppFogApplicationInfo();
        expectedInfo.setApplicationProperty(AppFogApplicationInfo.Property.NAME, PROJECT_NAME);
        expectedInfo.setApplicationProperty(AppFogApplicationInfo.Property.URLS, DEFAULT_APPLICATION_URL);
        expectedInfo.setApplicationProperty(AppFogApplicationInfo.Property.MEMORY, DEFAULT_SPRING_MEMORY);
        expectedInfo.setApplicationProperty(AppFogApplicationInfo.Property.INSTANCES, DEFAULT_INSTANCES_NUMBER);
        expectedInfo.setApplicationProperty(AppFogApplicationInfo.Property.INFRASTRUCTER, DEFAULT_INFRASTRUCTURE);
        expectedInfo.setApplicationProperty(AppFogApplicationInfo.Property.STACK, DEFAULT_STACK);
        expectedInfo.setApplicationProperty(AppFogApplicationInfo.Property.MODEL, DEFAULT_MODEL);
        expectedInfo.setApplicationProperty(AppFogApplicationInfo.Property.STATUS, AppFogApplicationInfo.ApplicationStatus.STARTED.name());
        expectedInfo.setApplicationProperty(AppFogApplicationInfo.Property.SERVICES, DEFAULT_SERVICES);
        
        final String OUTPUT_PANEL_MESSAGE = String.format("[INFO] Project successfully built.\n[INFO] Application %1$s successfully created.\nApplication %1$s successfully started on %2$s", PROJECT_NAME, DEFAULT_APPLICATION_URL);
        
        final String BUILD_PANEL_MESSAGE = String.format("Building project %1$s\nFinished building project %1$s.\nResult: Successful", PROJECT_NAME);
        
        final String MESSAGE_FROM_APPLICATION_MAIN_PAGE_XPATH = "//span[contains(text(), 'Enter your name:')]";
        
        // WHEN
        appFog.createSimpleSpringProject(status.init(), PROJECT_NAME);        
        
        // THEN
        status.checkSuccess();
        checkOutputMessages(OUTPUT_PANEL_MESSAGE);        
        checkAllBuilderMessages(BUILD_PANEL_MESSAGE);
        
        // THEN
        AppFogApplicationInfo applicationInfo = appFog.getApplicationInfoFromApplicationManagerWindow(status.init());
        status.checkSuccess();
        applicationInfo.checkApplicationProperties(expectedInfo);

        // THEN
        expectedInfo.setApplicationProperty(AppFogApplicationInfo.Property.URLS, DEFAULT_APPLICATION_DOMAIN_NAME);   //  there is url without "http://" in manage applications window
        applicationInfo = appFog.getApplicationInfoFromManageApplicationsWindow(status.init(), PROJECT_NAME);
        status.checkSuccess();
        applicationInfo.checkApplicationProperties(expectedInfo);
        
        // THEN
        appFog.checkApplicationExistsAtPaaSSide(status.init(), PROJECT_NAME, TOKEN);
        status.checkSuccess();
        
        // THEN
        appFog.gotoLinkFromOutputPanelAndCheckRunApplication(status.init(), DEFAULT_APPLICATION_URL, MESSAGE_FROM_APPLICATION_MAIN_PAGE_XPATH);
        status.checkSuccess();
        outputPanel.clearPanel();
    }

    
    /**
     * Verifications
     */
    
    private void checkOutputMessages(String expectedMessages) {
        assertEquals(expectedMessages, outputPanel.getAllMessagesFromOutput());
    }
    
    private void checkAllBuilderMessages(String expectedMessages) {        
        buildPanel.selectBuilderOutputTab();
        assertEquals(expectedMessages, buildPanel.getOutputMessage());
    }
}