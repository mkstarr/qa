/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
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
package com.codenvy.ide.core;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Roman Iuvshin
 *
 */
public class CreateANewProjectFromScratch extends AbstractTestModule {
    /** @param ide */
    public CreateANewProjectFromScratch(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String CREATE_PROJECT_FROM_SCRATCH_ID = "eXoCreateNewProjectView-window";

        String INTPUT_PROJECT_NAME =
                "//div[@id='eXoCreateNewProjectView-window']//input[@name='eXoCreateNewProjectViewNameField']";

        String CREATE_PROJECT_JAR_ID = "CREATE-PROJECT-Jar";

        String CREATE_PROJECT_JSP_ID = "CREATE-PROJECT-Servlet/JSP";

        String CREATE_PROJECT_SPRING_ID = "CREATE-PROJECT-Spring";

        String CREATE_PROJECT_JAVASCRIPT_ID = "CREATE-PROJECT-JavaScript";

        String CREATE_PROJECT_RUBY_ON_RAILS_ID = "CREATE-PROJECT-Rails";

        String CREATE_PROJECT_PYTHON_ID = "CREATE-PROJECT-Python";

        String CREATE_PROJECT_PHP_ID = "CREATE-PROJECT-PHP";

        String CREATE_PROJECT_MULTI_MODULE_ID = "CREATE-PROJECT-Maven Multi-module";

        String CREATE_PROJECT_NODE_JS_ID = "CREATE-PROJECT-nodejs";

        String CREATE_PROJECT_ANDROID_ID = "CREATE-PROJECT-Android";

        String CREATE_PROJECT_PAAS_AWS_ID = "CREATE-PROJECT-PAAS-AWS";

        String CREATE_PROJECT_PAAS_APPFOG_ID = "CREATE-PROJECT-PAAS-AppFog";

        String CREATE_PROJECT_PAAS_CLOUDFOUNDRY_ID = "CREATE-PROJECT-PAAS-CloudFoundry";

        String CREATE_PROJECT_PAAS_CLOUDBEES_ID = "CREATE-PROJECT-PAAS-CloudBees";

        String CREATE_PROJECT_PAAS_GAE_ID = "CREATE-PROJECT-PAAS-GAE";

        String CREATE_PROJECT_PAAS_HEROKU_ID = "CREATE-PROJECT-PAAS-Heroku";

        String CREATE_PROJECT_PAAS_OPENSHIFT_ID = "CREATE-PROJECT-PAAS-OpenShift";

        String CREATE_PROJECT_PAAS_NONE_ID = "CREATE-PROJECT-PAAS-none";

        String CREATE_PROJECT_PAAS_MANYMO_ID = "//*[@id='CREATE-PROJECT-PAAS-Manymo']//div[@role='button']";

        String NEXT_BUTTON_ID = "eXoCreateNewProjectViewNextButton";

        String FINISH_BUTTON_ID = "eXoCreateNewProjectViewFinishButton";

        String CANCEL_BUTTON_ID = "eXoCreateNewProjectViewCancelButton";

        String BACK_BUTTON_ID = "eXoCreateNewProjectViewBackButton";

        String PROJECT_TEMPLATE_BY_NAME = "//div[@id='eXoCreateNewProjectView-window']//div[text()='%s']";

        String JREBEL_CHECKBOX = "//div[@id='eXoCreateNewProjectView-window']//input[@type='checkbox']";

        String JREBEL_FIRST_NAME_ID = "jrebelprofilefirstname";

        String JREBEL_PHONE_ID = "jrebelprofilephone";

        String JREBEL_LAST_NAME_ID = "jrebelprofilelastname";

        String ENABLED_FINISH_BUTTON = "//div[@id='eXoCreateNewProjectViewFinishButton' and @button-enabled='true']";
        
        String AWS_ENVIRONMENT_NAME_XPATH = "//*[@id='eXoCreateNewProjectView-window']/div/table/tbody/tr[2]/td[2]/div/div/div[2]/div/div[2]/div/div[2]/div/div[2]/div[2]/div/div[2]/div/div[2]/div/div[3]/div[3]/input";
    }

    @FindBy(xpath = Locators.CREATE_PROJECT_PAAS_MANYMO_ID)
    WebElement createProjectPaasManymo;

    @FindBy(id = Locators.CREATE_PROJECT_NODE_JS_ID)
    WebElement createProjectNodeJs;

    @FindBy(id = Locators.CREATE_PROJECT_ANDROID_ID)
    WebElement createProjectAdndroid;

    @FindBy(id = Locators.CREATE_PROJECT_PAAS_AWS_ID)
    WebElement createProjectPaasAWS;

    @FindBy(id = Locators.CREATE_PROJECT_PAAS_APPFOG_ID)
    WebElement createProjectPaasAppFog;

    @FindBy(id = Locators.CREATE_PROJECT_PAAS_CLOUDFOUNDRY_ID)
    WebElement createProjectPaasCloudfoundry;

    @FindBy(id = Locators.CREATE_PROJECT_PAAS_CLOUDBEES_ID)
    WebElement createProjectPaasCloudbees;

    @FindBy(id = Locators.CREATE_PROJECT_PAAS_GAE_ID)
    WebElement createProjectPaasGAE;

    @FindBy(id = Locators.CREATE_PROJECT_PAAS_HEROKU_ID)
    WebElement createProjectPaasHeroku;

    @FindBy(id = Locators.CREATE_PROJECT_PAAS_OPENSHIFT_ID)
    WebElement createProjectPaasOpenShift;

    @FindBy(id = Locators.CREATE_PROJECT_PAAS_NONE_ID)
    WebElement createProjectPaasNone;

    @FindBy(id = Locators.CREATE_PROJECT_JAR_ID)
    WebElement createProjectJar;

    @FindBy(id = Locators.CREATE_PROJECT_JSP_ID)
    WebElement createProjectJsp;

    @FindBy(id = Locators.CREATE_PROJECT_SPRING_ID)
    WebElement createProjectSpring;

    @FindBy(id = Locators.CREATE_PROJECT_JAVASCRIPT_ID)
    WebElement createProjectJS;

    @FindBy(id = Locators.CREATE_PROJECT_RUBY_ON_RAILS_ID)
    WebElement createProjectRuby;

    @FindBy(id = Locators.CREATE_PROJECT_PYTHON_ID)
    WebElement createProjectPython;

    @FindBy(id = Locators.CREATE_PROJECT_PHP_ID)
    WebElement createProjectPhp;

    @FindBy(id = Locators.CREATE_PROJECT_MULTI_MODULE_ID)
    WebElement createProjectMultiModule;

    @FindBy(id = Locators.CREATE_PROJECT_FROM_SCRATCH_ID)
    WebElement createFromScratchForm;

    @FindBy(xpath = Locators.INTPUT_PROJECT_NAME)
    WebElement inputProjectName;

    @FindBy(xpath = Locators.JREBEL_CHECKBOX)
    WebElement jrebelCheckbox;

    @FindBy(id = Locators.JREBEL_FIRST_NAME_ID)
    WebElement jrebelFirstName;

    @FindBy(id = Locators.JREBEL_PHONE_ID)
    WebElement jrebelPhone;

    @FindBy(id = Locators.JREBEL_LAST_NAME_ID)
    WebElement jrebelLastName;

    @FindBy(id = Locators.NEXT_BUTTON_ID)
    WebElement nextButtonId;

    @FindBy(id = Locators.BACK_BUTTON_ID)
    WebElement backButtonId;

    @FindBy(id = Locators.CANCEL_BUTTON_ID)
    WebElement cancelButtonId;

    @FindBy(id = Locators.FINISH_BUTTON_ID)
    WebElement finishButtonId;

    @FindBy(xpath = Locators.AWS_ENVIRONMENT_NAME_XPATH)
    WebElement awsEnvironmentName;
    
    /** wait for opening Create project from scratch form */
    public void waitCreateProjectFromScratch() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    IDE().LOADER.waitClosed();
                    return createFromScratchForm.isDisplayed() && createFromScratchForm != null
                           && inputProjectName.isDisplayed() && inputProjectName != null &&
                           createProjectJar.isDisplayed()
                           && createProjectJsp.isDisplayed() && createProjectSpring.isDisplayed()
                           && createProjectMultiModule.isDisplayed() && nextButtonId.isDisplayed()
                           && cancelButtonId.isDisplayed() && finishButtonId.isDisplayed();
                } catch (NoSuchElementException e) {
                    return false;
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /** wait for closing Create project from scratch form */
    public void waitCreateProjectFromScratchClosed() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    IDE().LOADER.waitClosed();
                    return !createFromScratchForm.isDisplayed() && createFromScratchForm != null
                           && inputProjectName.isDisplayed() && inputProjectName != null &&
                           createProjectJar.isDisplayed()
                           && createProjectJsp.isDisplayed() && createProjectSpring.isDisplayed()
                           && createProjectMultiModule.isDisplayed() && nextButtonId.isDisplayed()
                           && cancelButtonId.isDisplayed() && finishButtonId.isDisplayed();
                } catch (NoSuchElementException e) {
                    return true;
                } catch (Exception e) {
                    return true;
                }
            }
        });
    }


    /** type in project name */
    public void typeProjectName(String value) throws InterruptedException {
        inputProjectName.clear();
        inputProjectName.sendKeys(value);
    }

    /** select Java Library (JAR) technology */
    public void selectJavaLibraryTechnology() {
        createProjectJar.click();
    }

    /** select Java Web Application (WAR) technology */
    public void selectJavaWebApplicationTechnology() {
        createProjectJsp.click();
    }

    /** select Java Spring technology */
    public void selectJavaSpringTechnology() {
        createProjectSpring.click();
    }

    /** select JavaScript technology */
    public void selectJavaScriptTechnology() {
        createProjectJS.click();
    }

    /** select Ruby on Rails technology */
    public void selectRubyTechnology() {
        createProjectRuby.click();
    }

    /** select Python technology */
    public void selectPythonTechnology() {
        createProjectPython.click();
    }

    /** select PHP technology */
    public void selectPHPTechnology() {
        createProjectPhp.click();
    }

    /** select Maven Multi-module technology */
    public void selectMavenMultiModuleTechnology() {
        createProjectMultiModule.click();
    }

    /** select node.js technology */
    public void selectNodeJsTechnology() {
        createProjectNodeJs.click();
    }

    /** select Android technology */
    public void selectAndroidTechnology() {
        createProjectAdndroid.click();
    }

    /** select AWS Elastic Beanstalk PaaS */
    public void selectAWSPaaS() {
        createProjectPaasAWS.click();
    }

    /** select Manymo PaaS */
    public void selectManymoPaaS() {
        createProjectPaasManymo.click();
    }

    /** select AppFog PaaS */
    public void selectAppFogPaaS() {
        createProjectPaasAppFog.click();
    }

    /** select Cloud Foundry PaaS */
    public void selectCloudFoundryPaaS() {
        createProjectPaasCloudfoundry.click();
    }

    /** select CloudBees PaaS */
    public void selectCloudBeesPaaS() {
        createProjectPaasCloudbees.click();
    }

    /** select Google App Engine PaaS */
    public void selectGAEPaaS() {
        createProjectPaasGAE.click();
    }

    /** select Heroku PaaS */
    public void selectHerokuPaaS() {
        createProjectPaasHeroku.click();
    }

    /** select OpenShift PaaS */
    public void selectOpenShiftPaaS() {
        createProjectPaasOpenShift.click();
    }

    /** select None PaaS */
    public void selectNonePaaS() {
        createProjectPaasNone.click();
    }

    /** click next button */
    public void clickNextButton() {
        nextButtonId.click();
    }

    /** click back button */
    public void clickBackButton() {
        backButtonId.click();
    }

    /** click cancel button */
    public void clickCancelButton() {
        cancelButtonId.click();
    }

    /** click finish button */
    public void clickFinishButton() {
        finishButtonId.click();
    }

    /** select project template */
    public void selectProjectTemplate(String value) {
        driver().findElement(By.xpath(String.format(Locators.PROJECT_TEMPLATE_BY_NAME, value))).click();
    }

    /** wait for project template form */

    public void waitProjectTemplateForm() {

        (new WebDriverWait(driver(), 30)).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver d) {
                try {
                    WebElement item =
                            driver().findElement(
                                    By.xpath(String.format(Locators.PROJECT_TEMPLATE_BY_NAME,
                                                           "Choose project template")));
                    return item != null && item.isDisplayed();
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /** click on JRebel check box */
    public void clickOnJRebelCheckbox() {
        jrebelCheckbox.click();
    }

    /** Type first name in JRebel field */
    public void typeJRebelFirstName(String value) {
        jrebelFirstName.sendKeys(value);
    }

    /** Type phone name in JRebel field */
    public void typeJRebelPhoneName(String value) {
        jrebelPhone.sendKeys(value);
    }

    /** Type last name name in JRebel field */
    public void typeJRebelLastName(String value) {
        jrebelLastName.sendKeys(value);
    }

    /** Type last name name in AWS environment name field */
    public void typeAwsEnvironmentName(String value) {
        awsEnvironmentName.sendKeys(value);
    }
    
    /** wait for JRebel checkbox */

    public void waitForJRebelCheckbox() {

        (new WebDriverWait(driver(), 30)).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver d) {
                try {
                    return jrebelCheckbox.isDisplayed() && jrebelCheckbox != null && jrebelCheckbox.isSelected();
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    public void waitJRebelCheckBoxIsNotPresent() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.JREBEL_CHECKBOX)));
    }

    /**
     * check that JRebel input fields is present.
     *
     * @return
     */
    public boolean isJRebelInputsVisible() {
        return jrebelFirstName.isDisplayed() && jrebelPhone.isDisplayed() && jrebelLastName.isDisplayed();
    }

    /** wait for enabled finish button */
    public void waitActiveFinishButton() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.ENABLED_FINISH_BUTTON)));
    }
}
