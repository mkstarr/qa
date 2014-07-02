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

package com.codenvy.ide.core.gae;

import com.codenvy.ide.IDE;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.core.AbstractTestModule;

import org.openqa.selenium.support.PageFactory;

import java.util.ResourceBundle;

/** @author Zaryana Dombrovskaya */
public class GaePaaS extends AbstractTestModule {

    private String currentWindow;

    public static final ResourceBundle IDE_SETTINGS = ResourceBundle.getBundle("conf/selenium");
    public static       String         LOGIN        = IDE_SETTINGS.getString("gae.email");
    public static       String         PASSWORD     = IDE_SETTINGS.getString("gae.password");
    public static       String         LOGIN_PHP    = IDE_SETTINGS.getString("gae.php.email");
    public static       String         PASSWORD_PHP = IDE_SETTINGS.getString("gae.php.password");

    public static ApplicationAppEngineWindow         applicationAppEngineWindow;
    public static CreateApplicationWindow            createApplicationWindow;
    public static ApplicationSettingsAppEngineWindow applicationSettingsAppEngineWindow;

    public GaePaaS(IDE ide) {
        super(ide);
        PageFactory.initElements(driver(), applicationAppEngineWindow = new ApplicationAppEngineWindow(this.IDE()));
        PageFactory.initElements(driver(), createApplicationWindow = new CreateApplicationWindow(this.IDE()));
        PageFactory.initElements(driver(), applicationSettingsAppEngineWindow = new ApplicationSettingsAppEngineWindow(this.IDE()));
    }

    /** login */
    public void login() {
        currentWindow = driver().getWindowHandle();

        IDE().MENU.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.GoogleAppEngine.GOOGLEAPPENGINE,
                              MenuCommands.PaaS.GoogleAppEngine.LOGIN);
        switchToNonCurrentWindow(currentWindow);
        IDE().GOOGLE.waitOauthPageOpened();
        IDE().GOOGLE.typeLogin(LOGIN);
        IDE().GOOGLE.typePassword(PASSWORD);
        IDE().GOOGLE.clickSignIn();
//        IDE().GOOGLE.waitAllowApplicationButton();
//        IDE().GOOGLE.clickOnAllowButton();
        driver().switchTo().window(currentWindow);
    }

    /** login */
    public void loginForPhp() {
        currentWindow = driver().getWindowHandle();

        IDE().MENU.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.GoogleAppEngine.GOOGLEAPPENGINE,
                              MenuCommands.PaaS.GoogleAppEngine.LOGIN);
        switchToNonCurrentWindow(currentWindow);
        IDE().GOOGLE.waitOauthPageOpened();
        IDE().GOOGLE.typeLogin(LOGIN_PHP);
        IDE().GOOGLE.typePassword(PASSWORD_PHP);
        IDE().GOOGLE.clickSignIn();
//        IDE().GOOGLE.waitAllowApplicationButton();
//        IDE().GOOGLE.clickOnAllowButton();
        driver().switchTo().window(currentWindow);
    }

    /**
     * logout
     *
     * @param outputMessLogout
     */
    public void logout(String outputMessLogout) {
        IDE().MENU.waitSubCommandEnabled(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.GoogleAppEngine.GOOGLEAPPENGINE,
                                         MenuCommands.PaaS.GoogleAppEngine.LOGOUT);
        IDE().MENU.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.GoogleAppEngine.GOOGLEAPPENGINE,
                              MenuCommands.PaaS.GoogleAppEngine.LOGOUT);
        IDE().OUTPUT.waitOnMessage(outputMessLogout);

    }

    /**
     * create WAR project with AppEngine template
     *
     * @param projectName
     * @throws Exception
     */
    public void createWarProject(String projectName) throws Exception {
        createApplicationWindow.createWarProject(projectName);
    }

    /**
     * create PHP project with AppEngine template
     *
     * @param projectName
     * @throws Exception
     */
    public void createPhpProject(String projectName) throws Exception {
        createApplicationWindow.createPhpProject(projectName);
    }


    /**
     * create application
     *
     * @param applicationId
     * @param applicationTitle
     * @throws Exception
     */
    public void createApplication(String applicationId, String applicationTitle) throws Exception {
        createApplicationWindow.createApplication(applicationId, applicationTitle);
    }

    /**
     * edit application and save change
     *
     * @param projectName
     * @throws Exception
     */
    public void editApplication(String projectName) throws Exception {
        IDE().PACKAGE_EXPLORER.waitItemInPackageExplorer(projectName);
        IDE().PACKAGE_EXPLORER.waitItemInPackageExplorer("src");
        IDE().PACKAGE_EXPLORER.openItemWithDoubleClick("src");
        IDE().PACKAGE_EXPLORER.waitItemInPackageExplorer("main");
        IDE().PACKAGE_EXPLORER.openItemWithDoubleClick("main");
        IDE().PACKAGE_EXPLORER.waitItemInPackageExplorer("webapp");
        IDE().PACKAGE_EXPLORER.openItemWithDoubleClick("webapp");
        IDE().PACKAGE_EXPLORER.waitItemInPackageExplorer("display.jsp");
        IDE().PACKAGE_EXPLORER.openItemWithDoubleClick("display.jsp");
        IDE().EDITOR.waitActiveFile();
        IDE().PROGRESS_BAR.waitProgressBarControlClose();
        IDE().GOTOLINE.goToLine(69);
        IDE().EDITOR.typeTextIntoEditor("Test");
        IDE().MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE().LOADER.waitClosed();
        IDE().EDITOR.closeFile("display.jsp");
        IDE().PACKAGE_EXPLORER.openItemWithDoubleClick("display.jsp");
        IDE().EDITOR.waitActiveFile();
        IDE().EDITOR.waitContentIsPresent("Test");
    }

    /**
     * Update application from PaaS -> Google App Engine -> Update Application
     *
     * @throws Exception
     */
    public void updateApplication() throws Exception {
        IDE().MENU.runCommand(MenuCommands.PaaS.PAAS, MenuCommands.PaaS.GoogleAppEngine.GOOGLEAPPENGINE,
                              MenuCommands.PaaS.GoogleAppEngine.UPDATE_APPLICATION);
        IDE().LOADER.waitClosed();
        IDE().PROGRESS_BAR.waitProgressBarControlClose();
    }

    /**
     * Check text in build panel
     *
     * @param buildPanelMessage
     */
    public void checkBuildPanel(String buildPanelMessage) {
        IDE().BUILD.selectBuilderOutputTab();
        IDE().BUILD.waitBuilderContainText(buildPanelMessage);
    }

    /**
     * Check message in output panel
     *
     * @param outputMessage
     */
    public void checkOutputPanel(String outputMessage) {
        IDE().OUTPUT.waitOnMessage(outputMessage);
    }

    /**
     * delete Application in AppEngine
     *
     * @param projectName
     */
    public void deleteApplication(String projectName) {
        applicationSettingsAppEngineWindow.deleteGaeApplication(projectName);
    }

}


