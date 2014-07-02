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

import java.util.List;

/** @author Musienko Maxim */
public class ManageApplicationForm extends AbstractTestModule {

    public ManageApplicationForm(IDE ide) {
        super(ide);
    }

    private interface ManageApplicationFormLocators {


        String MANGE_APPLICATION_FORM_ID = "ideManageApplicationsView-window";

        String MANGE_APPLICATION_ROWS = "//table[@id='herokuApplicationsListGrid']/tbody/tr";

        String MANGE_APPLICATION_GET_TEXT_CONTENT = "//div[@view-id='ideManageApplicationsView']";

        String MANGE_APPLICATION_CLOSE_BTN = "ideManageApplicationsViewCloseButton";
    }

    private static final int TIMEOUT = TestConstants.PAGE_LOAD_PERIOD / 100;


    //Manage Application Web elements
    @FindBy(id = ManageApplicationFormLocators.MANGE_APPLICATION_FORM_ID)
    private WebElement manageApplicationForm;

    //Manage Application Web elements
    @FindBy(id = ManageApplicationFormLocators.MANGE_APPLICATION_CLOSE_BTN)
    private WebElement manageApplicationCloseBtn;

    /**
     * get Manage Application Form (use for waitEventOrWarningDialog)
     *
     * @return Manage Application Form as web element
     */
    public WebElement getManageForm() {
        return manageApplicationForm;
    }

    /** wait appearance manage application form */
    public void waitManageApplicationForm() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(manageApplicationForm));
    }

    /** wait appearance manage application form closed */
    public void waitManageApplicationFormClose() {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.invisibilityOfElementLocated(By.id(ManageApplicationFormLocators.MANGE_APPLICATION_FORM_ID)));
    }

    /** @return all raws as List Webelements with name application, delete,	rename,	environment, info, import buttons */
    public List<WebElement> getAllMenegeApplicationsRows() {
        List<WebElement> rows = driver().findElements(By.xpath(ManageApplicationFormLocators.MANGE_APPLICATION_ROWS));
        return rows;
    }

    /** get all text from manage application form */
    public String getAllTextFromManageApplicationForm() {
        return manageApplicationForm.getText();
    }

    /** get all applications and check it. Wait while list of app become >0; */
    public void waitManadgeAppListNotEmpty() {
        new WebDriverWait(driver(), TIMEOUT).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return getAllMenegeApplicationsRows().size() > 0;
            }
        });
    }

    /** click on close button Manage Applications forms */
    public void clickCloseBtn() {
        manageApplicationCloseBtn.click();
    }

}
