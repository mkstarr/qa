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
package com.codenvy.ide.core;

import com.codenvy.ide.ToolbarCommands;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Evgen Vidolob */
public class Properties extends AbstractTestModule {
    /** @param ide */
    public Properties(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String PROPERTY_LOCATOR = "//div[@view-id='ideFilePropertiesView']//td[@propertyname='%1s']";

        String VIEW_LOCATOR = "//div[@view-id='ideFilePropertiesView']";

        String NAME_PROPERTY_LOCATOR = "//div[@view-id='ideFilePropertiesView']//td[@propertyname='Name']";

        String PATH_PROPERTY_LOCATOR = "//div[@view-id='ideFilePropertiesView']//td[@propertyname='Path']";

        String MIME_TYPE_PROPERTY_LOCATOR = "//div[@view-id='ideFilePropertiesView']//td[@propertyname='Mime Type']";

        String CREATED_PROPERTY_LOCATOR = "//div[@view-id='ideFilePropertiesView']//td[@propertyname='Created']";

        String LAST_MODIFIED_PROPERTY_LOCATOR =
                "//div[@view-id='ideFilePropertiesView']//td[@propertyname='Last modified']";

        String CONTENT_LENGHT_PROPERTY_LOCATOR =
                "//div[@view-id='ideFilePropertiesView']//td[@propertyname='Content length']";

        String CLOSE_VIEW_BUTTON_LOCATOR = "//div[@button-name='close-tab' and @tab-title='Properties']";

        String BORDER_PREFIX = "//div[@component=\"Border\" and contains(@style, 'color: rgb(182, 204, 232)')]";

        String HIGHLITER_BORDER = VIEW_LOCATOR + BORDER_PREFIX;

        String END_ANIMATION = "//div[@view-id='ideFilePropertiesView']//ancestor::div[@id='operation']/..";

        String OPERATION_FORM = "//div[@id='operation']/ancestor::div[contains(@style, 'height: 300')]";

        // ----------------------Properties project section

        String PROPERTIES_PROJECT_FORM = "//div[@view-id='ideProjectPropertiesView']";

        String PROPERTIES_PROJECT_ID = "ideProjectPropertiesView-window";

        String PROPERTIES_PROJECT_MAXIMIZE_BTN = "//div[@id='ideProjectPropertiesView-window']//img[@title='Maximize']";

        String PROPERTIES_PROJECT_CLOSE_WIN_BTN = "//div[@id='ideProjectPropertiesView-window']//img[@title='Close']";

        String PROPERTIES_PROJECT_ENABLED_EDIT_BUTTON =
                "//div[@id='ideProjectPropertiesEditButton' and @ button-enabled='true']";

        String PROPERTIES_PROJECT_DISABLED_EDIT_BUTTON =
                "//div[@id='ideProjectPropertiesEditButton' and @ button-enabled='false']";

        String PROPERTIES_PROJECT_DELETE_ENABLED_BUTTON =
                "//div[@id='ideProjectPropertiesDeleteButton' and @ button-enabled='true']";

        String PROPERTIES_PROJECT_DELETE_DISABLED_BUTTON =
                "//div[@id='ideProjectPropertiesDeleteButton' and @ button-enabled='false']";

        String PROPERTIES_PROJECT_ENABLED_OK_BUTTON =
                " //div[@id='ideProjectPropertiesOkButton' and @ button-enabled='true']";

        String PROPERTIES_PROJECT_DISABLED_OK_BUTTON =
                " //div[@id='ideProjectPropertiesOkButton' and @ button-enabled='false']";

        String PROPERTIES_PROJECT_CANCEL_BUTTON = "ideProjectPropertiesCancelButton";

        String ALL_PROPERTIES_TABLE = "ideProjectPropertiesListGrid";

        String ROW_BY_NAME_SELECTOR = "//table[@id='ideProjectPropertiesListGrid']//div[text()='%s']";

        String ROW_BY_NUMBER_SELECTOR = "//table[@id='ideProjectPropertiesListGrid']//tr[@__gwt_row=%d]/td[1]/div";

        String EDIT_PROJECT_PROPERTY_VIEW = "ideEditProjectPropertyView-window";

        String PROJECT_PROPERTY_NAME_INPUT = "//div[text()='Name:']/following-sibling::input";

        String PROJECT_PROPERTY_VALUE_INPUT = "//div[text()='Value:']/following-sibling::input";

        String PROJECT_PROPERTY_TYPE_VALUE = "//div[text()='Value:']/..//input";

        String EDIT_PROJECT_PROPERTY_OK_BTN =
                "//div[@view-id='ideEditProjectPropertyView']//td[text()='Ok']";

        String EDIT_PROJECT_PROPERTY_CANCEL_BTN =
                "//div[@view-id='ideEditProjectPropertyView']//td[text()='Cancel']";

    }


    public interface ProjectIcon {
        String PHP_PROJECT_ICON =
                "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAWUlEQVR42mNoaDjwnxLMMDgNuH79O"
                +
                "hyTbQADAwPxBqDbiGwAsiHYXMYAUwzDMAVEi6HbBmMTLTZ4DMDmX2wGEAwD5BBHD31s6hiIjXNc6hiITXW41A18ZgIAgl+yoD0ErLQAAAAASUVORK5CYII=";

    }

    @FindBy(xpath = Locators.VIEW_LOCATOR)
    private WebElement view;

    /* Properties */
    @FindBy(xpath = Locators.NAME_PROPERTY_LOCATOR)
    private WebElement nameProperty;

    @FindBy(xpath = Locators.PATH_PROPERTY_LOCATOR)
    private WebElement pathProperty;

    @FindBy(xpath = Locators.MIME_TYPE_PROPERTY_LOCATOR)
    private WebElement mimeTypeProperty;

    @FindBy(xpath = Locators.CREATED_PROPERTY_LOCATOR)
    private WebElement createdProperty;

    @FindBy(xpath = Locators.LAST_MODIFIED_PROPERTY_LOCATOR)
    private WebElement lastModifiedProperty;

    @FindBy(xpath = Locators.CONTENT_LENGHT_PROPERTY_LOCATOR)
    private WebElement contentLenghtProperty;

    @FindBy(xpath = Locators.CLOSE_VIEW_BUTTON_LOCATOR)
    private WebElement closeViewButton;

    @FindBy(xpath = Locators.END_ANIMATION)
    private WebElement endAnimationPanel;

    @FindBy(xpath = Locators.OPERATION_FORM)
    private WebElement operationForm;

    @FindBy(id = Locators.ALL_PROPERTIES_TABLE)
    private WebElement allTextPrjProperties;


    // ----------------------Properties project WebElement section

    @FindBy(id = Locators.PROPERTIES_PROJECT_ID)
    private WebElement projectPropertyview;

    @FindBy(xpath = Locators.PROPERTIES_PROJECT_MAXIMIZE_BTN)
    private WebElement projectPropertyMaximizeBtn;

    @FindBy(xpath = Locators.PROPERTIES_PROJECT_CLOSE_WIN_BTN)
    private WebElement projectPropertyCloseBtn;

    @FindBy(xpath = Locators.PROPERTIES_PROJECT_ENABLED_EDIT_BUTTON)
    private WebElement projectPropertyEditEnableBtn;

    @FindBy(xpath = Locators.PROPERTIES_PROJECT_DISABLED_EDIT_BUTTON)
    private WebElement projectPropertyEditDisableBtn;

    @FindBy(xpath = Locators.PROPERTIES_PROJECT_DELETE_DISABLED_BUTTON)
    private WebElement projectPropertyDeleteDisableBtn;

    @FindBy(xpath = Locators.PROPERTIES_PROJECT_DELETE_ENABLED_BUTTON)
    private WebElement projectPropertyDeleteEnableBtn;


    @FindBy(xpath = Locators.PROPERTIES_PROJECT_ENABLED_OK_BUTTON)
    private WebElement projectPropertyOkEnableBtn;

    @FindBy(xpath = Locators.PROPERTIES_PROJECT_DISABLED_OK_BUTTON)
    private WebElement projectPropertyOkDisableBtn;

    @FindBy(id = Locators.PROPERTIES_PROJECT_CANCEL_BUTTON)
    private WebElement projectPropertyCancelBtn;

    @FindBy(id = Locators.EDIT_PROJECT_PROPERTY_VIEW)
    private WebElement editProjectPropertyView;

    @FindBy(xpath = Locators.EDIT_PROJECT_PROPERTY_OK_BTN)
    private WebElement editProjectPropertyOkBtn;


    @FindBy(xpath = Locators.EDIT_PROJECT_PROPERTY_CANCEL_BTN)
    private WebElement editProjectPropertyCancelBtn;


    @FindBy(xpath = Locators.PROJECT_PROPERTY_NAME_INPUT)
    private WebElement nameFieldPropertiesPrjEdit;

    @FindBy(xpath = Locators.PROJECT_PROPERTY_VALUE_INPUT)
    private WebElement valueFieldPropertiesPrjEdit;

    @FindBy(xpath = Locators.PROJECT_PROPERTY_TYPE_VALUE)
    private WebElement projectTypeValueInput;


    /** click on Proporties editor */
    public void clickOnProperties() {
        view.click();
    }

    /**
     * Get Content Length property value.
     *
     * @return String value
     */
    public String getContentLength() {
        return contentLenghtProperty.getText();
    }

    /**
     * Get Content Type property value
     *
     * @return String value
     */
    public String getContentType() {
        return mimeTypeProperty.getText();
    }

    /**
     * Get Display Name property value
     *
     * @return String value
     */
    public String getDisplayName() {
        return nameProperty.getText();
    }

    /**
     * Get the value of path property.
     *
     * @return {@link String} path value
     */
    public String getPath() {
        return pathProperty.getText();
    }

    /**
     * Get the value of last modified property.
     *
     * @return {@link String} last modified value
     */
    public String getLastModified() {
        return lastModifiedProperty.getText();
    }

    /**
     * Get the value of created property.
     *
     * @return {@link String} created value
     */
    public String getCreated() {
        return createdProperty.getText();
    }

    /** Close Properties View. */
    public void closeProperties() throws Exception {
        waitEndAnimation();
        closeViewButton.click();
        waitClosed();
    }

    /**
     * Open properties view.
     *
     * @throws Exception
     */
    public void openProperties() throws Exception {
        IDE().TOOLBAR.waitForButtonEnabled(ToolbarCommands.View.SHOW_PROPERTIES);
        IDE().TOOLBAR.runCommand(ToolbarCommands.View.SHOW_PROPERTIES);
        waitOpened();
    }

    /**
     * Wait for properties view to be opened.
     *
     * @throws Exception
     */
    public void waitOpened() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    waitEndAnimation();
                } catch (Exception e) {
                }
                return (operationForm != null && operationForm.isDisplayed() && view != null && view.isDisplayed()
                        && contentLenghtProperty != null && contentLenghtProperty.isDisplayed() && nameProperty != null
                        && nameProperty.isDisplayed() && closeViewButton != null && closeViewButton.isDisplayed());
            }
        });
    }


    /**
     * Wait tex in file properties control
     *
     * @throws Exception
     */
    public void waitMimeTypePropertyContainsText(final String text) throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                return getContentType().contains(text);
            }
        });
    }

    /**
     * Wait while properties panel set height on 300 px
     *
     * @throws Exception
     */
    public void waitEndAnimation() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return endAnimationPanel.getAttribute("style").contains("height: 300px");
            }
        });

    }

    /**
     * Wait for properties view closed.
     *
     * @throws Exception
     */
    public void waitClosed() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    input.findElement(By.xpath(Locators.VIEW_LOCATOR));
                    return false;
                } catch (NoSuchElementException e) {
                    return true;
                }
            }
        });
    }

    /**
     * Returns active state of properties view.
     *
     * @return {@link Boolean} active state of properties view
     */
    public boolean isActive() {
        return IDE().PERSPECTIVE.isViewActive(view);
    }

    /**
     * Returns true if highlight border present
     *
     * @return
     */
    public boolean isHiglightBorderPresent() {
        WebElement border = driver().findElement(By.xpath(Locators.HIGHLITER_BORDER));
        try {
            return border != null && border.isDisplayed();
        } catch (Exception e) {
            return false;
        }

    }

    // ----------------------Properties project methods section

    /**
     * Wait for properties view to be opened.
     *
     * @throws Exception
     */
    public void waitProjectPropertiesOpened() throws Exception {
        new WebDriverWait(driver(), 20).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                return projectPropertyview.isDisplayed() && projectPropertyCancelBtn.isDisplayed() &&
                       projectPropertyCloseBtn.isDisplayed()
                       && projectPropertyMaximizeBtn.isDisplayed() && projectPropertyOkDisableBtn.isDisplayed()
                       && projectPropertyEditDisableBtn.isDisplayed();
            }
        });
    }

    /**
     * Wait for properties form closed.
     *
     * @throws Exception
     */
    public void waitProjectPropertiesClosed() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.PROPERTIES_PROJECT_FORM)));

    }

    /**
     * Wait while properties will be same as specified text
     *
     * @throws Exception
     */
    public void waitProjectPropertiesEqualsText(final String txt) throws Exception {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                return getAllTextFromProjectProperties().equals(txt);
            }
        });
    }


    /**
     * Wait while properties contains specified text
     *
     * @throws Exception
     */
    public void waitProjectPropertiesContainsText(final String txt) throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                return getAllTextFromProjectProperties().contains(txt);
            }
        });
    }


    /**
     * Wait while properties contains specified text
     *
     * @throws Exception
     */
    public void waitTextIntoNameFieldEditForm(final String txt) throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                return getTextFromNameInputPrjProperties().equals(txt);
            }
        });
    }


    /**
     * Wait while properties contains specified text
     *
     * @throws Exception
     */
    public void waitTextIntoValueFieldEditForm(final String txt) throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                return getTextFromNameValuePrjProperties().contains(txt);
            }
        });
    }


    /**
     * Type new project description value
     *
     * @throws Exception
     */
    public void typeNewValue(final String txt) throws Exception {
        valueFieldPropertiesPrjEdit.clear();
        valueFieldPropertiesPrjEdit.sendKeys(txt);
    }


    /**
     * Type new project type value
     *
     * @throws Exception
     */
    public void typeNewProjectTypeValue(final String txt) throws Exception {
        projectTypeValueInput.clear();
        projectTypeValueInput.sendKeys(txt);
    }


    /**
     * Wait appearance edit project properties form
     *
     * @throws Exception
     */
    public void waitEditDialogFormIsOpen() throws Exception {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.visibilityOf(editProjectPropertyView));

    }

    /**
     * Wait edit project properties form is closed
     *
     * @throws Exception
     */
    public void waitEditDialogFormIsClosed() throws Exception {
        new WebDriverWait(driver(), 20)
                .until(ExpectedConditions
                               .invisibilityOfElementLocated(By.xpath(Locators.EDIT_PROJECT_PROPERTY_VIEW)));

    }


    /**
     * Wait while editor button on project properties form is enabled
     *
     * @throws Exception
     */
    public void waitEditProjectWinView() throws Exception {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.id(Locators.EDIT_PROJECT_PROPERTY_VIEW)));
    }


    /** wait while ok button is disabled */
    public void waitOkButtonIsDisabled() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(projectPropertyOkDisableBtn));
    }


    /**
     * Wait while editor button on project properties form is enabled
     *
     * @throws Exception
     */
    public void waitEditButtonIsEnabled() throws Exception {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(projectPropertyEditEnableBtn));
    }


    /**
     * Wait while edit button on project properties form is disabled
     *
     * @throws Exception
     */
    public void waitEditButtonIsDisabled() throws Exception {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(projectPropertyEditDisableBtn));
    }

    /**
     * Wait while delete button on project properties form is disabled
     *
     * @throws Exception
     */
    public void waitDeleteButtonIsDisabled() throws Exception {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(projectPropertyDeleteDisableBtn));
    }


    /**
     * click on edit button on Project properties form
     *
     * @throws Exception
     */
    public void clickOnEditBtn() throws Exception {
        projectPropertyEditEnableBtn.click();
    }


    /**
     * click on ok button on edit form
     *
     * @throws Exception
     */
    public void clickEditFormOkBtn() throws Exception {
        editProjectPropertyOkBtn.click();
    }


    /**
     * click on cancel button on edit form
     *
     * @throws Exception
     */
    public void clickEditFormCancelBtn() throws Exception {
        editProjectPropertyCancelBtn.click();
    }


    /** get all text from project properties window */
    public String getAllTextFromProjectProperties() {
        return allTextPrjProperties.getText();

    }

    /** get text by row number start with 0 */
    public String getTextFromRowProjectProperties(int rowNumber) {
        return driver().findElement(By.xpath(String.format(Locators.ROW_BY_NUMBER_SELECTOR, rowNumber))).getText();

    }

    /** select row by number start with 0 */
    public void selectRowInProjectPreperties(int rowNumber) {
        driver().findElement(By.xpath(String.format(Locators.ROW_BY_NUMBER_SELECTOR, rowNumber))).click();

    }

    /** select row by name */
    public void selectRowInProjectProperties(String rowName) {
        driver().findElement(By.xpath(String.format(Locators.ROW_BY_NAME_SELECTOR, rowName))).click();

    }

    /**
     * get text from name field of the edit project properties
     *
     * @return
     */
    public String getTextFromNameInputPrjProperties() {
        return nameFieldPropertiesPrjEdit.getAttribute("value");
    }


    /**
     * get text from value field of the edit project properties
     *
     * @return
     */
    public String getTextFromNameValuePrjProperties() {
        return valueFieldPropertiesPrjEdit.getAttribute("value");
    }

    /** click cancel button on project properties form */
    public void clickCancelButtonOnPropertiesForm() {
        projectPropertyCancelBtn.click();
        waitProjectPropertiesClosed();
    }

    /** click ok button on project properties form */
    public void clickOkButtonOnPropertiesForm() {
        projectPropertyOkEnableBtn.click();
    }

    /**
     * get text from 'value' field of the edit property form
     *
     * @return
     */
    public String getProjectTypeValue() {
        WebElement element = driver().findElement(By.xpath(Locators.PROJECT_PROPERTY_TYPE_VALUE));
        return element.getAttribute("value");
    }


    /**
     * wait while text appear in the value field of the edit value form
     *
     * @param nameVal
     */
    public void waitProjectTypeValueIsAppear(final String nameVal) {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                return driver().findElement(By.xpath(Locators.PROJECT_PROPERTY_VALUE_INPUT)).getAttribute("value").contains(nameVal);
            }
        });
    }


}
