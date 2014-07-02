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
import com.codenvy.ide.core.Output;
import com.codenvy.ide.core.appfog.AppFogPaaS;
import com.codenvy.ide.paas.Status;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.mail.MessagingException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * @author Dmytro Nochevnov
 *
 */
public class DeleteApplicationFromPaaSMenuTest extends BaseTest {

    private AppFogPaaS appFog;
    private Output outputPanel;
        
    private final String EMAIL = IDE_SETTINGS.getString("appfog.email");
    private final String PASSWORD = IDE_SETTINGS.getString("appfog.password");
    private final String TOKEN = IDE_SETTINGS.getString("appfog.token");    
    
    private final static String PROJECT_NAME = DeleteApplicationFromPaaSMenuTest.class.getSimpleName().toLowerCase() + "_at_" + Utils.getCurrentTimeForProjectName();
    
    private final static String MYSQL_SERVICE_NAME = DeleteApplicationFromProjectMenuTest.class.getSimpleName().toLowerCase() + "_at_" + Utils.getCurrentTimeForProjectName();
    
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
        appFog = IDE.APP_FOG;
        outputPanel = IDE.OUTPUT; 

        IDE.EXPLORER.waitOpened();

        IDE.GET_STARTED_WIZARD.makeSureWizardIsClosed();
        
        // login
        Status result = new Status();
        appFog.switchAccount(result, EMAIL, PASSWORD);
        result.checkSuccess();
        outputPanel.clearPanel();
        
        // create default application
        IDE.OPEN.openProject(PROJECT_NAME);
        appFog.createDefaultApplicationForCurrentProject(result.init(), PROJECT_NAME);
        result.checkSuccess();
        outputPanel.clearPanel();
        
        // create and bound mysql service to application
        appFog.createMySqlServiceAtPaaSSide(result.init(), MYSQL_SERVICE_NAME, TOKEN);        
        result.checkSuccess();
        appFog.boundServiceAtPaaSSide(result.init(), MYSQL_SERVICE_NAME, PROJECT_NAME, TOKEN);        
        result.checkSuccess();
    }

    @After
    public void tearDown() throws IOException, InterruptedException {
        Status status = new Status();
        
        appFog.deleteApplicationAtPaaSSide(status, PROJECT_NAME, TOKEN);        
        appFog.deleteServiceAtPaaSSide(status, MYSQL_SERVICE_NAME, TOKEN);
        
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
    public void deleteApplicationWithoutBoundServiceTest() throws Exception {
        // GIVEN
        Status status = new Status();
        
        final String OUTPUT_PANEL_MESSAGE = String.format("[INFO] Application %s successfully deleted.", PROJECT_NAME);
        boolean deleteBoundServices = false;

        // WHEN
        appFog.deleteApplicationFromPaaSMenu(status, PROJECT_NAME, deleteBoundServices);
        
        // THEN
        status.checkSuccess();
        checkLastOutputPanelMessage(OUTPUT_PANEL_MESSAGE);

        // THEN
        //// check application is absence in manage applications window
        assertFalse(appFog.checkApplicationPresentsInManageApplicationsWindow(status.init(), PROJECT_NAME));
        status.checkSuccess();
        
        // THEN
        //// check application is absence at AppFog side
        appFog.checkApplicationExistsAtPaaSSide(status.init(), PROJECT_NAME, TOKEN);
        status.checkFail();
        
        // THEN
        //// check service is present at AppFog side
        appFog.checkServiceExistAtPaaSSide(status.init(), MYSQL_SERVICE_NAME, TOKEN);
        status.checkSuccess();
    }
    
    @Test
    public void deleteApplicationWithBoundServiceTest() throws Exception {
        // GIVEN
        Status status = new Status();
        
        final String OUTPUT_PANEL_MESSAGE = String.format("[INFO] Application %s successfully deleted.", PROJECT_NAME);
        boolean deleteBoundServices = true;

        // WHEN
        appFog.deleteApplicationFromPaaSMenu(status, PROJECT_NAME, deleteBoundServices);
        
        // THEN
        status.checkSuccess();
        checkLastOutputPanelMessage(OUTPUT_PANEL_MESSAGE);

        // THEN
        //// check application is absence in manage applications window
        assertFalse(appFog.checkApplicationPresentsInManageApplicationsWindow(status.init(), PROJECT_NAME));
        status.checkSuccess();
        
        // THEN
        //// check application is absence at AppFog side
        appFog.checkApplicationExistsAtPaaSSide(status.init(), PROJECT_NAME, TOKEN);
        status.checkFail();
        
        // THEN
        //// check service is absence at AppFog side
        appFog.checkServiceExistAtPaaSSide(status.init(), MYSQL_SERVICE_NAME, TOKEN);
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
