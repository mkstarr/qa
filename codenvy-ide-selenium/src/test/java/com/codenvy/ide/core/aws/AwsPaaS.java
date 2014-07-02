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
package com.codenvy.ide.core.aws;

import com.codenvy.ide.IDE;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.TestConstants;
import com.codenvy.ide.Utils;
import com.codenvy.ide.core.AbstractTestModule;
import com.codenvy.ide.core.Build;
import com.codenvy.ide.core.IDEConfiguration;
import com.codenvy.ide.core.Loader;
import com.codenvy.ide.core.Menu;
import com.codenvy.ide.core.Output;
import com.codenvy.ide.core.ProgressorWindow;
import com.codenvy.ide.core.exceptions.PaaSException;
import com.codenvy.ide.paas.PaaSLogout;
import com.codenvy.ide.paas.Status;
import com.codenvy.ide.paas.aws.AwsApplicationInfo;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.PageFactory;

import java.util.Map;

/**
 * AWS PaaS Page Object Controller
 *
 * @author Dmytro Nochevnov
 */
public class AwsPaaS extends AbstractTestModule {

    public AwsPaaS(IDE ide) {
        super(ide);

        // get and init internal page objects
        PageFactory.initElements(driver(), loginWindow = new LoginWindow(ide));
        PageFactory.initElements(driver(), applicationManagerWindow = new ApplicationManagerWindow(ide));
        PageFactory.initElements(driver(), createApplicationWindow = new CreateApplicationWindow(ide));
        PageFactory.initElements(driver(), beanstalkEnvironmentsWindow = new BeanstalkEnvironmentsWindow(ide));
        PageFactory.initElements(driver(), terminateEnvironmentWindow = new TerminateEnvironmentWindow(ide));
        PageFactory.initElements(driver(), s3ManagementConsole = new S3ManagementConsole(ide));
        PageFactory.initElements(driver(), beanstalkVersionsWindow = new BeanstalkVersionsWindow(ide));
        PageFactory.initElements(driver(), deleteVersionWindow = new DeleteVersionWindow(ide));


        // get external page objects
        progressorWindow = IDE().PROGRESSOR_WINDOW;
        outputPanel = IDE().OUTPUT;
        buildPanel = IDE().BUILD;
        topMenu = IDE().MENU;
        loader = IDE().LOADER;
    }

    // internal page objects
    public static LoginWindow                 loginWindow;
    public static ApplicationManagerWindow    applicationManagerWindow;
    public static CreateApplicationWindow     createApplicationWindow;
    public static BeanstalkEnvironmentsWindow beanstalkEnvironmentsWindow;
    public static BeanstalkVersionsWindow     beanstalkVersionsWindow;
    public static TerminateEnvironmentWindow  terminateEnvironmentWindow;
    public static S3ManagementConsole         s3ManagementConsole;
    public static DeleteVersionWindow         deleteVersionWindow;

    // external page objects
    private static ProgressorWindow progressorWindow;
    private static Output           outputPanel;
    private static Build            buildPanel;
    private static Menu             topMenu;
    private static Loader           loader;
    private static IDEConfiguration ideConfiguration;

    static final int    TIMEOUT           = TestConstants.LONG_WAIT_IN_SEC;
    static final String PAAS_NAME         = "Amazon Web Services";
    static final String ELASTIC_BEANSTALK = "Elastic Beanstalk";

    public void switchAccount(Status status, String accessKey, String secretKey) {
        try {
            topMenu.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.ElasticBeanstalk.ELASTIC_BEANSTALK,
                               MenuCommands.PaaS.ElasticBeanstalk.SWITCH_ACCOUNT);

            loginWindow.login(accessKey, secretKey);

            outputPanel.waitOnInfoMessage(String.format(Output.Messages.SUCCESS_LOGIN_TO_PAAS_MESSAGE_2_TEMPLATE, PAAS_NAME), TIMEOUT,
                                          PaaSException.class);
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }

    public void createDefaultApplicationForCurrentProject(Status status, String applicationName, String environmentName) {
        try {
            topMenu.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.ElasticBeanstalk.ELASTIC_BEANSTALK,
                               MenuCommands.PaaS.ElasticBeanstalk.CREATE_APPLICATION);

            createApplicationWindow.create(applicationName, environmentName);

            if (!buildPanel
                    .isTabPresentButNotOpened()) {   // check if there was not build in time of filling create application form,
                    // and so there should be progressor with launch environment message
                progressorWindow.waitWindowOpened();
                progressorWindow
                        .waitForMessage(String.format(ProgressorWindow.Messages.LAUNCHING_ENVIRONMENT_MESSAGE_TEMPLATE, environmentName));
                progressorWindow.waitWindowClosed(TestConstants.VERY_LONG_WAIT_IN_SEC);
            }

            outputPanel
                    .waitOnInfoMessage(String.format(Output.Messages.APPLICATION_SUCCESSFULLY_STARTED_ON_MESSAGE_TEMPLATE, applicationName),
                                       TestConstants.VERY_LONG_WAIT_IN_SEC, PaaSException.class);
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }

    public void waitUntilSimpleStringApplicationStarts(Status status, String applicationName, String environmentName) {
        final String DEFAULT_APPLICATION_URL_PREFIX = "http://" + environmentName;
        final String SIMPLE_STRING_APPLICATION_MAIN_PAGE_MESSAGE_XPATH = "//span[contains(text(), 'Enter your name:')]";

        try {
            outputPanel.gotoLinkFromOutputPanelAndCheckLinkedPage(DEFAULT_APPLICATION_URL_PREFIX,
                                                                  SIMPLE_STRING_APPLICATION_MAIN_PAGE_MESSAGE_XPATH, TIMEOUT);
        } catch (Exception e1) {
            // give AWS additional time to start environment and application
            sleepInTest(TestConstants.LONG_WAIT_IN_SEC);
            try {
                outputPanel.gotoLinkFromOutputPanelAndCheckLinkedPage(DEFAULT_APPLICATION_URL_PREFIX,
                                                                      SIMPLE_STRING_APPLICATION_MAIN_PAGE_MESSAGE_XPATH, TIMEOUT);
            } catch (Exception e2) {
                Utils.processException(status, e2, driver());
                return;
            }
        }
    }

    /**
     * Logout at Codenvy side by sending logout request to Codenvy REST service
     *
     * @param status
     */
    public void logout(Status status) {
        try {
            PaaSLogout.logoutFromAws();
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }

    public void createSimpleSpringProject(Status status, String projectName, String environmentName) {   // TODO
        try {
            topMenu.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.NEW, MenuCommands.Project.CREATE_PROJECT);

            // step 1
            IDE().CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratch();
            IDE().CREATE_PROJECT_FROM_SCRATHC.typeProjectName(projectName);
            IDE().CREATE_PROJECT_FROM_SCRATHC.selectJavaSpringTechnology();
            IDE().CREATE_PROJECT_FROM_SCRATHC.selectAWSPaaS();
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

            IDE().CREATE_PROJECT_FROM_SCRATHC.typeAwsEnvironmentName(environmentName);
            IDE().CREATE_PROJECT_FROM_SCRATHC.waitActiveFinishButton();
            IDE().CREATE_PROJECT_FROM_SCRATHC.clickFinishButton();
            IDE().CREATE_PROJECT_FROM_SCRATHC.waitCreateProjectFromScratchClosed();

            // wait on application creation
            buildPanel.waitBuilderContainText(Build.Messages.BUILD_SUCCESS, TIMEOUT);

            try {
                IDE().LOADER.waitClosed();
            } catch (InterruptedException e) {
                throw new TimeoutException(e);
            }

            outputPanel.waitOnInfoMessage(String.format(Output.Messages.APPLICATION_SUCCESSFULLY_STARTED_ON_MESSAGE_TEMPLATE, projectName),
                                          TestConstants.VERY_LONG_WAIT_IN_SEC, PaaSException.class);
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }

    public void deleteCurrentProjectApplication(Status status, String applicationName) {
        try {
            topMenu.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.ElasticBeanstalk.ELASTIC_BEANSTALK,
                               MenuCommands.PaaS.ElasticBeanstalk.MANAGE_APPLICATION);

            applicationManagerWindow.deleteApplication();

            outputPanel.waitOnInfoMessage(String.format(Output.Messages.APPLICATION_SUCCESSFULLY_DELETED_ON_PAAS_MESSAGE_TEMPLATE,
                                                        applicationName, ELASTIC_BEANSTALK), TIMEOUT, PaaSException.class);
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }

    public void terminateFirstEnvironmentOfCurrentProjectApplication(Status status) {
        try {
            topMenu.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.ElasticBeanstalk.ELASTIC_BEANSTALK,
                               MenuCommands.PaaS.ElasticBeanstalk.MANAGE_APPLICATION);
            String firstEnvironmentName = applicationManagerWindow.getFirstEnvironmentName();

            topMenu.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.ElasticBeanstalk.ELASTIC_BEANSTALK,
                               MenuCommands.PaaS.ElasticBeanstalk.MANAGE_APPLICATION);
            applicationManagerWindow.terminateEnvironment(firstEnvironmentName);

            IDE().OUTPUT.waitOnInfoMessage(
                    String.format(Output.Messages.ENVIRONMENT_SUCCESSFULLY_TERMINATED_MESSAGE_TEMPLATE, firstEnvironmentName),
                    TestConstants.VERY_LONG_WAIT_IN_SEC, PaaSException.class);
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }


    public AwsApplicationInfo getApplicationInfoFromApplicationManagerWindow(Status status) {
        try {
            topMenu.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.ElasticBeanstalk.ELASTIC_BEANSTALK,
                               MenuCommands.PaaS.ElasticBeanstalk.MANAGE_APPLICATION);

            AwsApplicationInfo applicationInfo = new AwsApplicationInfo();

            Map<String, String> applicationProperties = applicationManagerWindow.getApplicationProperties();
            for (String property : applicationProperties.keySet()) {
                applicationInfo.setApplicationProperty(property, applicationProperties.get(property));
            }

            return applicationInfo;
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return null;
        }
    }

    public void gotoLinkFromOutputPanelAndCheckRunApplication(Status status, String linkUrl,
                                                              String xpathOfMessageOnApplicationPageToCheck) {
        try {
            outputPanel.gotoLinkFromOutputPanelAndCheckLinkedPage(linkUrl, xpathOfMessageOnApplicationPageToCheck, TIMEOUT);
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }


    public void checkApplicationExistsAtPaaSSide(Status status, String applicationName) {
        try {
            System.out.println("[INFO]" + IDEConfiguration.getWorkspaceId(driver()) + "-" + IDEConfiguration.getOpenedProjectId(driver()));
            CodenvyAwsExtensionApiServices
                    .checkApplicationExists(IDEConfiguration.getWorkspaceId(driver()), IDEConfiguration.getOpenedProjectId(driver()));
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }


    /**
     * Delete elastic beanstalk application
     *
     * @param status
     * @param applicationName
     */
    public void deleteApplicationAtPaaSSide(Status status, String applicationName) {
        try {
            CodenvyAwsExtensionApiServices
                    .deleteApplication(IDEConfiguration.getWorkspaceId(driver()), IDEConfiguration.getOpenedProjectId(driver()));
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }

    /**
     * Delete S3 bucket
     *
     * @param status
     * @param bucketName
     */
    public void deleteS3BucketAtPaaSSide(Status status, String bucketName) {
        try {
            CodenvyAwsExtensionApiServices.deleteS3Bucket(bucketName);
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }

    /**
     * Delete S3 object
     *
     * @param status
     * @param bucketName
     */
    public void deleteS3ObjectAtPaaSSide(Status status, String objectName) {
        try {
            CodenvyAwsExtensionApiServices.deleteS3Object(objectName);
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }

    /**
     * Terminate elastic beanstalk environment
     *
     * @param status
     * @param environmentName
     */
    public void terminateEnvironmentAtPaaSSide(Status status, String environmentName) {
        try {
            CodenvyAwsExtensionApiServices.terminateEnvironment(environmentName);
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }


    /** @param status */
    public void deleteFirstVersionOfCurrentProject(Status status, boolean deleteS3Bundle) {
        try {
            topMenu.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.ElasticBeanstalk.ELASTIC_BEANSTALK,
                               MenuCommands.PaaS.ElasticBeanstalk.MANAGE_APPLICATION);
            String firstVersionLabel = applicationManagerWindow.getFirstVersionLabel();

            deleteVersionOfCurrentProject(status, firstVersionLabel, deleteS3Bundle);
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }

    /** @param status */
    public void deleteVersionOfCurrentProject(Status status, String versionLabel, boolean deleteS3Bundle) {
        try {
            topMenu.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.ElasticBeanstalk.ELASTIC_BEANSTALK,
                               MenuCommands.PaaS.ElasticBeanstalk.MANAGE_APPLICATION);
            applicationManagerWindow.deleteVersion(versionLabel, deleteS3Bundle);

            topMenu.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.ElasticBeanstalk.ELASTIC_BEANSTALK,
                               MenuCommands.PaaS.ElasticBeanstalk.MANAGE_APPLICATION);
            if (applicationManagerWindow.checkVersionPresent(versionLabel)) {
                throw new RuntimeException(String.format("Version '%s' isn't deleted", versionLabel));
            }
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }


    /** @param status */
    public void deleteFirstVersionOfCurrentProjectAtPaaSSide(Status status, String projectName) {
        try {
            topMenu.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.ElasticBeanstalk.ELASTIC_BEANSTALK,
                               MenuCommands.PaaS.ElasticBeanstalk.MANAGE_APPLICATION);
            String firstVersionLabel = applicationManagerWindow.getFirstVersionLabel();

            CodenvyAwsExtensionApiServices.deleteApplicationVersion(firstVersionLabel,
                                                                    IDEConfiguration.getWorkspaceId(driver()),
                                                                    IDEConfiguration.getOpenedProjectId(driver()),
                                                                    projectName);
        } catch (Exception e) {
            Utils.processException(status, e, driver());
            return;
        }
    }
}
