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
package com.codenvy.ide.core.appfog;

import com.codenvy.ide.IDE;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.TestConstants;
import com.codenvy.ide.Utils;
import com.codenvy.ide.core.AbstractTestModule;
import com.codenvy.ide.core.Build;
import com.codenvy.ide.core.Loader;
import com.codenvy.ide.core.Menu;
import com.codenvy.ide.core.Output;
import com.codenvy.ide.core.ProgressorWindow;
import com.codenvy.ide.core.exceptions.PaaSException;
import com.codenvy.ide.paas.PaaSLogout;
import com.codenvy.ide.paas.Status;
import com.codenvy.ide.paas.appfog.AppFogApplicationInfo;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Map;

/**
 * AppFog PaaS Page Object Controller
 * @author Dmytro Nochevnov
 *
 */
public class AppFogPaaS extends AbstractTestModule {

    public AppFogPaaS(IDE ide) {
        super(ide);
        
        // get and init internal page objects
        PageFactory.initElements(driver(), loginWindow = new LoginWindow(ide));       
        PageFactory.initElements(driver(), createApplicationWindow = new CreateApplicationWindow(ide));
        PageFactory.initElements(driver(), applicationManagerWindow = new ApplicationManagerWindow(ide));
        PageFactory.initElements(driver(), deleteApplicationWindow = new DeleteApplicationWindow(ide));        
        PageFactory.initElements(driver(), manageApplicationsWindow = new ManageApplicationsWindow(ide));
        
        // get external page objects
        progressorWindow = IDE().PROGRESSOR_WINDOW;
        outputPanel = IDE().OUTPUT;
        buildPanel = IDE().BUILD;
        topMenu = IDE().MENU;
        loader = IDE().LOADER;
    }

    // internal page objects
    public static LoginWindow loginWindow;    
    public static CreateApplicationWindow createApplicationWindow;
    public static ApplicationManagerWindow applicationManagerWindow;
    public static DeleteApplicationWindow deleteApplicationWindow;
    public static ManageApplicationsWindow manageApplicationsWindow;
    
    // external page objects
    private static ProgressorWindow progressorWindow;
    private static Output outputPanel;
    private static Build buildPanel;
    private static Menu topMenu;
    private static Loader loader;

    static final int TIMEOUT = TestConstants.VERY_LONG_WAIT_IN_SEC;
    static final String PAAS_NAME = "AppFog";



    public void switchAccount(Status status, String email, String password) {
        try {
            topMenu.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.AppFog.APP_FOG, MenuCommands.PaaS.AppFog.SWITCH_ACCOUNT);

            loginWindow.login(email, password);

            outputPanel.waitOnInfoMessage(String.format(Output.Messages.SUCCESS_LOGIN_TO_PAAS_MESSAGE_TEMPLATE, PAAS_NAME), TIMEOUT, PaaSException.class);
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }


    public void deleteCurrentProjectApplicationFromProjectMenu(Status status, String applicationName, boolean deleteBoundServices) {
        try {
            topMenu.runCommand(MenuCommands.Project.PROJECT, MenuCommands.PaaS.PAAS, MenuCommands.PaaS.AppFog.APP_FOG);

            applicationManagerWindow.deleteApplication(deleteBoundServices);

            outputPanel.waitOnInfoMessage(String.format(Output.Messages.APPLICATION_SUCCESSFULLY_DELETED_MESSAGE_TEMPLATE, applicationName), TIMEOUT, PaaSException.class);
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }

    public void deleteApplicationFromPaaSMenu(Status status, String applicationName, boolean deleteBoundServices) {
        try {
            topMenu.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.AppFog.APP_FOG, MenuCommands.PaaS.AppFog.APLICATIONS);

            manageApplicationsWindow.deleteApplication(applicationName, deleteBoundServices);

            outputPanel.waitOnInfoMessage(String.format(Output.Messages.APPLICATION_SUCCESSFULLY_DELETED_MESSAGE_TEMPLATE, applicationName), TIMEOUT, PaaSException.class);
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }

    public void createDefaultApplicationForCurrentProject(Status status, String applicationName) {
        try {
            topMenu.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.AppFog.APP_FOG, MenuCommands.PaaS.AppFog.CREATE_APPLICATION);

            createApplicationWindow.create(applicationName);

            new WebDriverWait(driver(), TestConstants.SHORT_WAIT_IN_SEC);
            if (buildPanel.isOpened()) {
                buildPanel.waitBuilderContainText(Build.Messages.BUILD_SUCCESS, TIMEOUT);
                
                try {
                    IDE().LOADER.waitClosed();
                } catch (InterruptedException e) {
                    throw new TimeoutException(e);
                }
            }
          
            outputPanel.clickOnOutputTab();

            outputPanel.waitOnInfoMessage(String.format(Output.Messages.APPLICATION_SUCCESSFULLY_STARTED_ON_MESSAGE_TEMPLATE, applicationName), TIMEOUT, PaaSException.class);
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }

    public AppFogApplicationInfo getApplicationInfoFromApplicationManagerWindow(Status status) {
        try {
            topMenu.runCommand(MenuCommands.Project.PROJECT, MenuCommands.PaaS.PAAS, MenuCommands.PaaS.AppFog.APP_FOG);

            AppFogApplicationInfo applicationInfo = new AppFogApplicationInfo();

            Map<ApplicationManagerWindow.Property, String> applicationProperties = applicationManagerWindow.getApplicationProperties();
            for (ApplicationManagerWindow.Property property : applicationProperties.keySet()) {
                applicationInfo.setApplicationProperty(property.toString(), applicationProperties.get(property));
            }

            return applicationInfo;
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return null;
        }
    }

    public AppFogApplicationInfo getApplicationInfoFromManageApplicationsWindow(Status status, String applicationName) {
        try {
            topMenu.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.AppFog.APP_FOG, MenuCommands.PaaS.AppFog.APLICATIONS);

            AppFogApplicationInfo applicationInfo = new AppFogApplicationInfo();

            Map<ManageApplicationsWindow.Property, String> applicationProperties =
                                                                                   manageApplicationsWindow.getApplicationProperties(applicationName);
            for (ManageApplicationsWindow.Property property : applicationProperties.keySet()) {
                applicationInfo.setApplicationProperty(property.toString(), applicationProperties.get(property));
            }

            return applicationInfo;
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return null;
        }
    }

    public void checkApplicationExistsAtPaaSSide(Status status, String applicationName, String token) {
        try {
            AppFogApiServices.getAppicationInfo(token, applicationName);
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }

    public void deleteApplicationAtPaaSSide(Status status, String applicationName, String token) {
        try {
            AppFogApiServices.deleteApplication(token, applicationName);
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }
    
    /**
     * Logout at Codenvy side by sending logout request to Codenvy REST service
     * 
     * @param status
     */
    public void logout(Status status) {
        try {
            PaaSLogout.logoutFromAppFog();
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }

    public void checkServiceExistAtPaaSSide(Status status, String serviceName, String token) {
        try {
            status.setSuccess(AppFogApiServices.isServiceExist(token, serviceName));
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }

    public void createMySqlServiceAtPaaSSide(Status status, String serviceName, String token) {
        try {
            AppFogApiServices.createMySqlService(token, serviceName);
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }

    public void boundServiceAtPaaSSide(Status status, String serviceName, String applicationName, String token) {
        try {
            AppFogApiServices.boundService(serviceName, applicationName, token);
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }

    public void deleteServiceAtPaaSSide(Status status, String serviceName, String token) {
        try {
            AppFogApiServices.deleteService(token, serviceName);
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }

    public void gotoLinkFromOutputPanelAndCheckRunApplication(Status status, String linkUrl, String xpathOfMessageOnApplicationPageToCheck) {
        try {
            outputPanel.gotoLinkFromOutputPanelAndCheckLinkedPage(linkUrl, xpathOfMessageOnApplicationPageToCheck, TIMEOUT);
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }

    public boolean checkApplicationPresentsInManageApplicationsWindow(Status status, String applicationName) {
        try {
            topMenu.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.AppFog.APP_FOG, MenuCommands.PaaS.AppFog.APLICATIONS);

            return manageApplicationsWindow.checkApplicationPresents(applicationName);
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return false;
        }
    }

    public void createSimpleSpringProject(Status status, String projectName) {
        try {
            topMenu.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.NEW, MenuCommands.Project.CREATE_PROJECT);

            // step 1
            IDE().CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
            IDE().CREATE_PROJECT_FROM_SCRATHC.typeProjectName(projectName);
            IDE().CREATE_PROJECT_FROM_SCRATHC.selectJavaSpringTechnology();
            IDE().CREATE_PROJECT_FROM_SCRATHC.selectAppFogPaaS();
            IDE().CREATE_PROJECT_FROM_SCRATHC.clickNextButton();

            // step 2
            IDE().CREATE_PROJECT_FROM_SCRATHC.waitProjectTemplateForm();
            IDE().CREATE_PROJECT_FROM_SCRATHC.selectProjectTemplate("Simple Spring application.");
            IDE().CREATE_PROJECT_FROM_SCRATHC.clickNextButton();

            // step 3
            try {
                loader.waitClosed();
            } catch (InterruptedException e) {
                throw new TimeoutException(e);
            }
            IDE().CREATE_PROJECT_FROM_SCRATHC.waitActiveFinishButton();
            IDE().CREATE_PROJECT_FROM_SCRATHC.clickFinishButton();
            IDE().CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratchClosed();

            // wait on application creation                
            buildPanel.waitBuilderContainText(Build.Messages.BUILD_SUCCESS, TIMEOUT);

            progressorWindow.waitWindowOpened();
            progressorWindow.waitForMessage(String.format(ProgressorWindow.Messages.CREATING_APPLICATION_MESSAGE_TEMPLATE, projectName, PAAS_NAME));
            progressorWindow.waitWindowClosed(TIMEOUT);

            outputPanel.waitOnInfoMessage(String.format(Output.Messages.APPLICATION_SUCCESSFULLY_STARTED_ON_MESSAGE_TEMPLATE, projectName), TIMEOUT, PaaSException.class);
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }
}
