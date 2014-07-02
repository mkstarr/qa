package com.codenvy.ide.core.heroku;

import com.codenvy.ide.IDE;
import com.codenvy.ide.TestConstants;
import com.codenvy.ide.core.AbstractTestModule;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Musienko Maxim */
public class CreateAppForm extends AbstractTestModule {
    public CreateAppForm(IDE ide) {
        super(ide);
    }

    private static int TIMEOUT = TestConstants.PAGE_LOAD_PERIOD / 100;

    private interface CreateAppFormLocators {
        String ID_FORM = "ideCreateApplicationView-window";

        String LOCATION_OF_GITREPOSITORY_FIELD_CSS = "input[name='ideCreateApplicationViewWorkDirField']";

        String ENTER_APPLICATION_NAME_FIELS_CSS = "input[name='ideCreateApplicationViewNameField']";

        String ENTER_REMOTEREPOSITORY_NAME_FIELD_CSS = "input[name='ideCreateApplicationViewRemoteNameField']";

        String IDE_CREATE_APPLICATION_VIEW_CREATE_BUTTON_CSS =
                "div#ideCreateApplicationView-window div#ideCreateApplicationViewCreateButton";

        String IDE_APPLICATION_CANCEL_BUTTON_CSS =
                "div#ideCreateApplicationView-window div#ideCreateApplicationViewCancelButton";
    }

    @FindBy
            (id = CreateAppFormLocators.ID_FORM)
    private WebElement createAppForm;

    @FindBy
            (css = CreateAppFormLocators.LOCATION_OF_GITREPOSITORY_FIELD_CSS)
    private WebElement locationGitRepoField;

    @FindBy
            (css = CreateAppFormLocators.ENTER_APPLICATION_NAME_FIELS_CSS)
    private WebElement appNameField;

    @FindBy
            (css = CreateAppFormLocators.ENTER_REMOTEREPOSITORY_NAME_FIELD_CSS)
    private WebElement enterRemoteRepoField;

    @FindBy
            (css = CreateAppFormLocators.IDE_CREATE_APPLICATION_VIEW_CREATE_BUTTON_CSS)
    private WebElement createAppButton;


    @FindBy
            (css = CreateAppFormLocators.IDE_APPLICATION_CANCEL_BUTTON_CSS)
    private WebElement cancelBtn;

    /** wait appearance of  the IDE create new app project form */
    public void waitAppereanceCreateAppForm() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.visibilityOf(createAppForm));
    }

    /** wait disappearance of  the IDE create new app project form */
    public void waitDisappearCreateAppForm() {
        new WebDriverWait(driver(), TIMEOUT).until(ExpectedConditions.invisibilityOfElementLocated(By.id(CreateAppFormLocators.ID_FORM)));
    }

    /**
     * get value current repository from the IDE Location of Git repository field
     *
     * @return current git repo from 'Location of Git repository:' field
     */
    public String getLocationOfRepo() {
        return locationGitRepoField.getAttribute("value");
    }

    /**
     * type name into the app name field
     *
     * @param name
     *         name Of the app
     */
    public void typeNameOfTheApp(String name) {
        appNameField.clear();
        appNameField.sendKeys(name);
    }


    /**
     * type uof the specified name repository into field
     *
     * @param nameRepo
     *         user name repo
     */
    public void typeRemoteRepoName(String nameRepo) {
        appNameField.clear();
        appNameField.sendKeys(nameRepo);
    }

    /** click on create application button */
    public void clickCreateBtn() {
        createAppButton.click();
    }


    /** click on cancel application button */
    public void clickCancelBtn() {
        cancelBtn.click();
    }


}
