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
package com.codenvy.ide.miscellaneous;

import com.codenvy.ide.BaseTest;

import org.junit.Test;

/** @author Roman Iuvshin */
public class GetStartedWizardTest extends BaseTest {

    public static final String PHP_PROJECT_NAME = "newPhpPrj";

    public static final String JSP_PROJECT_NAME = "newJspPrj";

    public static final String JAR_PROJECT_NAME = "newJarPrj";

    public static final String JS_PROJECT_NAME = "newJsPrj";

    public static final String PYTHON_PROJECT_NAME = "newPythonPrj";

    public static final String RUBY_PROJECT_NAME = "newRubyPrj";

    public static final String SPRING_PROJECT_NAME = "newSpringPrj";

    public static final String MULTI_MODULE_PROJECT_NAME = "newMultiModulePrj";

    public static final String NODE_JS_PROJECT_NAME = "nodeJsPrj";

    @Test
    public void createPhpProjectFromWizardTest() throws Exception {

        IDE.GET_STARTED_WIZARD.waitGetStartedWizardOpened();
        IDE.LOADER.waitClosed();
        IDE.GET_STARTED_WIZARD.typeProjectName(PHP_PROJECT_NAME);
        IDE.GET_STARTED_WIZARD.clickOnGetStartedButton();
        IDE.LOADER.waitClosed();
        IDE.GET_STARTED_WIZARD.selectPhpProject();
        IDE.GET_STARTED_WIZARD.clickOnNextButton();
        IDE.GET_STARTED_WIZARD.selectNonePaas();
        IDE.GET_STARTED_WIZARD.clickOnFinishButton();
        IDE.EXPLORER.waitForItem(PHP_PROJECT_NAME);
        IDE.EXPLORER.waitForItem(PHP_PROJECT_NAME + "/index.php");
        IDE.EXPLORER.waitForItem(PHP_PROJECT_NAME + "/README");
        // deleting project
        IDE.EXPLORER.selectItem(PHP_PROJECT_NAME);
        IDE.DELETE.deleteSelectedItems();
    }

    @Test
    public void createJspProjectFromWizardTest() throws Exception {
        driver.navigate().refresh();
        IDE.GET_STARTED_WIZARD.waitGetStartedWizardOpened();
        IDE.LOADER.waitClosed();
        IDE.GET_STARTED_WIZARD.typeProjectName(JSP_PROJECT_NAME);
        IDE.GET_STARTED_WIZARD.clickOnGetStartedButton();
        IDE.LOADER.waitClosed();
        IDE.GET_STARTED_WIZARD.selectJspProject();
        IDE.GET_STARTED_WIZARD.clickOnNextButton();
        IDE.GET_STARTED_WIZARD.selectNonePaas();
        IDE.GET_STARTED_WIZARD.clickOnFinishButton();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(JSP_PROJECT_NAME);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        // deleting project
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(JSP_PROJECT_NAME);
        IDE.DELETE.deleteSelectedItems();
    }

    @Test
    public void createJarProjectFromWizardTest() throws Exception {
        driver.navigate().refresh();
        IDE.GET_STARTED_WIZARD.waitGetStartedWizardOpened();
        IDE.LOADER.waitClosed();
        IDE.GET_STARTED_WIZARD.typeProjectName(JAR_PROJECT_NAME);
        IDE.GET_STARTED_WIZARD.clickOnGetStartedButton();
        IDE.LOADER.waitClosed();
        IDE.GET_STARTED_WIZARD.selectJarProject();
        IDE.GET_STARTED_WIZARD.clickOnNextButton();
        IDE.GET_STARTED_WIZARD.selectNonePaas();
        IDE.GET_STARTED_WIZARD.clickOnFinishButton();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(JAR_PROJECT_NAME);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        // deleting project
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(JAR_PROJECT_NAME);
        IDE.DELETE.deleteSelectedItems();
    }

    @Test
    public void createJsProjectFromWizardTest() throws Exception {
        driver.navigate().refresh();
        IDE.GET_STARTED_WIZARD.waitGetStartedWizardOpened();
        IDE.LOADER.waitClosed();
        IDE.GET_STARTED_WIZARD.typeProjectName(JS_PROJECT_NAME);
        IDE.GET_STARTED_WIZARD.clickOnGetStartedButton();
        IDE.LOADER.waitClosed();
        IDE.GET_STARTED_WIZARD.selectJsProject();
        IDE.GET_STARTED_WIZARD.clickOnNextButton();
        IDE.GET_STARTED_WIZARD.selectNonePaas();
        IDE.GET_STARTED_WIZARD.clickOnFinishButton();
        IDE.EXPLORER.waitForItem(JS_PROJECT_NAME);
        IDE.EXPLORER.waitForItem(JS_PROJECT_NAME + "/index.html");
        IDE.EXPLORER.waitForItem(JS_PROJECT_NAME + "/js");
        // deleting project
        IDE.EXPLORER.selectItem(JS_PROJECT_NAME);
        IDE.DELETE.deleteSelectedItems();
    }

    @Test
    public void createPythonProjectFromWizardTest() throws Exception {
        driver.navigate().refresh();
        IDE.GET_STARTED_WIZARD.waitGetStartedWizardOpened();
        IDE.LOADER.waitClosed();
        IDE.GET_STARTED_WIZARD.typeProjectName(PYTHON_PROJECT_NAME);
        IDE.GET_STARTED_WIZARD.clickOnGetStartedButton();
        IDE.LOADER.waitClosed();
        IDE.GET_STARTED_WIZARD.selectPythonProject();
        IDE.GET_STARTED_WIZARD.clickOnNextButton();
        IDE.GET_STARTED_WIZARD.selectNonePaas();
        IDE.GET_STARTED_WIZARD.clickOnFinishButton();
        IDE.EXPLORER.waitForItem(PYTHON_PROJECT_NAME);
        IDE.EXPLORER.waitForItem(PYTHON_PROJECT_NAME + "/requirements.txt");
        IDE.EXPLORER.waitForItem(PYTHON_PROJECT_NAME + "/wsgi.py");
        // deleting project
        IDE.EXPLORER.selectItem(PYTHON_PROJECT_NAME);
        IDE.DELETE.deleteSelectedItems();
    }

    @Test
    public void createRubyProjectFromWizardTest() throws Exception {
        driver.navigate().refresh();
        IDE.GET_STARTED_WIZARD.waitGetStartedWizardOpened();
        IDE.LOADER.waitClosed();
        IDE.GET_STARTED_WIZARD.typeProjectName(RUBY_PROJECT_NAME);
        IDE.GET_STARTED_WIZARD.clickOnGetStartedButton();
        IDE.LOADER.waitClosed();
        IDE.GET_STARTED_WIZARD.selectRubyProject();
        IDE.GET_STARTED_WIZARD.clickOnNextButton();
        IDE.GET_STARTED_WIZARD.selectNonePaas();
        IDE.GET_STARTED_WIZARD.clickOnFinishButton();
        IDE.EXPLORER.waitForItem(RUBY_PROJECT_NAME);
        IDE.EXPLORER.waitForItem(RUBY_PROJECT_NAME + "/Gemfile");
        IDE.EXPLORER.waitForItem(RUBY_PROJECT_NAME + "/config.ru");
        // deleting project
        IDE.EXPLORER.selectItem(RUBY_PROJECT_NAME);
        IDE.DELETE.deleteSelectedItems();
    }

    @Test
    public void createSpringProjectFromWizardTest() throws Exception {
        driver.navigate().refresh();
        IDE.GET_STARTED_WIZARD.waitGetStartedWizardOpened();
        IDE.LOADER.waitClosed();
        IDE.GET_STARTED_WIZARD.typeProjectName(SPRING_PROJECT_NAME);
        IDE.GET_STARTED_WIZARD.clickOnGetStartedButton();
        IDE.LOADER.waitClosed();
        IDE.GET_STARTED_WIZARD.selectSpringProject();
        IDE.GET_STARTED_WIZARD.clickOnNextButton();
        IDE.GET_STARTED_WIZARD.selectNonePaas();
        IDE.GET_STARTED_WIZARD.clickOnFinishButton();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(SPRING_PROJECT_NAME);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        // deleting project
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(SPRING_PROJECT_NAME);
        IDE.DELETE.deleteSelectedItems();
    }

    @Test
    public void createMultiModuleProjectFromWizardTest() throws Exception {
        driver.navigate().refresh();
        IDE.GET_STARTED_WIZARD.waitGetStartedWizardOpened();
        IDE.LOADER.waitClosed();
        IDE.GET_STARTED_WIZARD.typeProjectName(MULTI_MODULE_PROJECT_NAME);
        IDE.GET_STARTED_WIZARD.clickOnGetStartedButton();
        IDE.LOADER.waitClosed();
        IDE.GET_STARTED_WIZARD.selectMultiModuleProject();
        IDE.GET_STARTED_WIZARD.clickOnNextButton();
        IDE.GET_STARTED_WIZARD.selectNonePaas();
        IDE.GET_STARTED_WIZARD.clickOnFinishButton();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(MULTI_MODULE_PROJECT_NAME);
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        // deleting project
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(MULTI_MODULE_PROJECT_NAME);
        IDE.DELETE.deleteSelectedItems();
    }

    @Test
    public void createNodeJsProjectFromWizardTest() throws Exception {
        driver.navigate().refresh();
        IDE.GET_STARTED_WIZARD.waitGetStartedWizardOpened();
        IDE.LOADER.waitClosed();
        IDE.GET_STARTED_WIZARD.typeProjectName(NODE_JS_PROJECT_NAME);
        IDE.GET_STARTED_WIZARD.clickOnGetStartedButton();
        IDE.LOADER.waitClosed();
        IDE.GET_STARTED_WIZARD.selectNodeJsProject();
        IDE.GET_STARTED_WIZARD.clickOnNextButton();
        IDE.GET_STARTED_WIZARD.selectNonePaas();
        IDE.GET_STARTED_WIZARD.clickOnFinishButton();
        IDE.EXPLORER.waitForItem(NODE_JS_PROJECT_NAME);
        IDE.EXPLORER.waitForItem(NODE_JS_PROJECT_NAME + "/app.js");
        // deleting project
        IDE.EXPLORER.selectItem(NODE_JS_PROJECT_NAME);
        IDE.DELETE.deleteSelectedItems();
    }
}
