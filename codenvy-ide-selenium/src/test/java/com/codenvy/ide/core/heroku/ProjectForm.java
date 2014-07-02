package com.codenvy.ide.core.heroku;

import com.codenvy.ide.IDE;
import com.codenvy.ide.TestConstants;
import com.codenvy.ide.core.AbstractTestModule;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Musienko Maxim */
public class ProjectForm extends AbstractTestModule {
    public ProjectForm(IDE ide) {
        super(ide);
    }

    private static int TIMEOUT = TestConstants.PAGE_LOAD_PERIOD / 100;

    interface ProjectHerokuFormLocators {

        String PROJECT_FORM_ID = "ideHerokuProjectView-window";

        String APPLICATION_NAME_INPUT_CSS = "input[name=ideHerokuProjectViewNameField]";

        String APPLICATION_RENAME_BUTTON_ID = "ideHerokuProjectViewRenameButton";

        String STACK_NAME_FIELD_CSS = "input[name=ideHerokuProjectViewSatckField]";

        String STACK_MIGRATE_BTN_ID = "ideHerokuProjectViewEditStackButton";

        String LOGS_BTN_ID = "ideHerokuProjectViewLogsButton";

        String RAKE_BTN_ID = "ideHerokuProjectViewRakeButton";

        String DELETE_BTN_ID = "ideHerokuProjectViewDeleteButton";

        String CLOSE_BTN_ID = "ideHerokuProjectViewCloseButton";

        String LINK_APP_CSS = "a[name='ideHerokuProjectViewUrlField']";
    }


    //Project property Web elements
    @FindBy(id = ProjectHerokuFormLocators.PROJECT_FORM_ID)
    private WebElement projectForm;

    @FindBy(css = ProjectHerokuFormLocators.APPLICATION_NAME_INPUT_CSS)
    private WebElement projectNameField;

    @FindBy(id = ProjectHerokuFormLocators.APPLICATION_RENAME_BUTTON_ID)
    private WebElement projectRenameBtn;

    @FindBy(css = ProjectHerokuFormLocators.STACK_NAME_FIELD_CSS)
    private WebElement projectStackNameField;

    @FindBy(id = ProjectHerokuFormLocators.STACK_MIGRATE_BTN_ID)
    private WebElement projectMigrateStackBtn;

    @FindBy(id = ProjectHerokuFormLocators.LOGS_BTN_ID)
    private WebElement projectLogsBtn;

    @FindBy(id = ProjectHerokuFormLocators.RAKE_BTN_ID)
    private WebElement projectRakeBtn;

    @FindBy(id = ProjectHerokuFormLocators.DELETE_BTN_ID)
    private WebElement projectDeleteBtn;

    @FindBy(id = ProjectHerokuFormLocators.CLOSE_BTN_ID)
    private WebElement projectCloseBtn;

    @FindBy(css = ProjectHerokuFormLocators.LINK_APP_CSS)
    private WebElement projectLinkUrlApp;

    //Heroku Project Form methods

    /** wait closing of  the IDE Heroku Project form */
    public void waitDisappearHerokuProjectForm() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.invisibilityOfElementLocated(
                By.id(ProjectHerokuFormLocators.PROJECT_FORM_ID)));
    }

    /** wait appearance of  the IDE Heroku Project form */
    public void waitAppearanceHerokuProjectForm() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.visibilityOf(projectForm));
    }


    /** wait while Heroku AppName is not empty */
    public void waitWhileAppNameNotEmpty() {
        new WebDriverWait(driver(), TIMEOUT).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return (projectNameField.getAttribute("value").length()) != 0;
            }
        });
    }


    /** wait while Heroku StackName is not empty */
    public void waitWhileStackNameNotEmpty() {
        new WebDriverWait(driver(), TIMEOUT).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return (projectStackNameField.getAttribute("value").length()) != 0;
            }
        });
    }

    /**
     * get name of the cureent app from IDE Heroku Project form
     *
     * @return name of a application into Application Field the IDE Heroku Project form
     */
    public String projectFormgetAppName() {
        return projectNameField.getText();
    }

    /**
     * return link to App from IDE Heroku Project form
     *
     * @return text representation of the link to curen app the IDE Heroku Project form
     */
    public String projectFormGetUrlApp() {
        return projectLinkUrlApp.getText();
    }

    /**
     * return name of a application stack from the IDE Heroku Project form
     *
     * @return name of a application stack
     */
    public String projectFormGetStackName() {
        return projectStackNameField.getAttribute("value");
    }

    /** click on close button on the IDE Heroku Project form */
    public void clickProjectHerokuCloseBtn() {
        projectCloseBtn.click();
    }


}



