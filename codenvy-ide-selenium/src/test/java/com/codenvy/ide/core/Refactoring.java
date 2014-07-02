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
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/** @author Roman Iuvshin */
public class Refactoring extends AbstractTestModule {
    /** @param ide */
    public Refactoring(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String VIEW_LOCATOR = "//div[@view-id='ideRefactoringRenameView']";

        String RENAME_BUTTON_ID = "ideRefactoringRenameViewRenameButton";

        String CANCEL_BUTTON_ID = "ideRefactoringRenameViewCancelButton";

        String NEW_NAME = "//input[@name='ideRefactoringRenameViewNewNameField']";

        String WAIT_FOR_INIT_WINDOW =
                "//div[@view-id='ideInformationModalView']//div[text()='Wait for initialize Java tooling.']";

        String WAIT_FOR_NEED_TO_SAVE_WINDOW =
                "//div[@view-id='ideInformationModalView']//div[text()='You should save all unsaved files to continue"
                +
                ".']";

        String GENERATE_GETTERS_AND_SETTERS__FORM_ID = "IdeAddGetterSetter-window";

        String SELECT_ALL_BTN = "//div[@view-id='IdeAddGetterSetter']//td[text()='SelectAll']";

        String DESELECT_ALL_BTN = "//div[@view-id='IdeAddGetterSetter']//td[text()='DeselectAll']";

        String SELECT_GETTERS_BTN =
                "//div[@view-id='IdeAddGetterSetter']//td[text()='Select Getters']";

        String SELECT_SETTERS_BTN =
                "//div[@view-id='IdeAddGetterSetter']//td[text()='Select Setters']";

        String GETTERS_AND_SETTERS_OK_BTN = "//div[@view-id='IdeAddGetterSetter']//td[text()='Ok']";

        String FINAL_LABEL =
                "//div[@id='IdeAddGetterSetter-window']//label[text()='final']";

        String SYNCHRONIZED_LABEL =
                "//div[@id='IdeAddGetterSetter-window']//label[text()='synchronized']";

        String PUBLIC_LABEL =
                "//div[@id='IdeAddGetterSetter-window']//label[text()='public']";

        String PROTECTED_LABEL =
                "//div[@id='IdeAddGetterSetter-window']//label[text()='protected']";

        String DEFAULT_LABEL =
                "//div[@id='IdeAddGetterSetter-window']//label[text()='default']";

        String PRIVATE_LABEL =
                "//div[@id='IdeAddGetterSetter-window']//label[text()='private']";

        String ALLOW_SETTERS_FOR_FINAL_FIELDS =
                "//div[@id='IdeAddGetterSetter-window']//div[contains(.,'Allow')]/../input";

        String SELECT_FIELD_BY_NAME =
                "//div[@id='IdeAddGetterSetter-window']//div[@role='treeitem']//div/span[text()='%s']/span/input";

        String EXPAND_GETTERS_AND_SETTERS_SELECTOR_FOR_CLASS_FIELD_BY_NAME =
                "//div[@id='IdeAddGetterSetter-window']//div[@role='treeitem']//div/span[text()='%s']/../../div/img/..";

        String SELECT_GETTER_OR_SETTER_BY_NAME =
                "//div[@id='IdeAddGetterSetter-window']//div[@role='treeitem']//span[text()='%s']/span/input";

        String SELECT_SORT_BY = "//div[@id='IdeAddGetterSetter-window']//select";

        String ARIA_MAIN_ELEMENTS =
                "//div[@id='IdeAddGetterSetter-window']//div[@__gwtcellbasedwidgetimpldispatchingfocus]//div[@aria"
                +
                "-selected]";

        String ARIA_MAIN_ROWS_IS_SELECTED =
                "//div[@id='IdeAddGetterSetter-window']//div[@__gwtcellbasedwidgetimpldispatchingfocus]//div[@aria"
                +
                "-selected ='true']";

        String ARIA_MAIN_INPUTS_IS_SELECTED =
                "//div[@id='IdeAddGetterSetter-window']//div[@__gwtcellbasedwidgetimpldispatchingfocus]//div[@aria"
                +
                "-selected ='true']//input[@type='checkbox' and @checked]";

        String SELECT_HIGLIGHTED_NAME =
                "//div[@id='IdeAddGetterSetter-window']//div[@__gwtcellbasedwidgetimpldispatchingfocus]//div[@aria"
                +
                "-selected ='true']//span[text()='%s']";

        String ALLOW_SETTERS_FOR_FINAL_CHECKBOX =
                "//div[@id='IdeAddGetterSetter-window']//div[@class='gwt-Label' and contains(., "
                +
                "'Allow setters for final fields')]/ preceding-sibling::input";

        String GENERATE_METHOD_COMMENTS_CHECKBOX =
                "//div[@id='IdeAddGetterSetter-window']//div[@class='gwt-Label' and contains(., "
                +
                "'Generate methods comnents')]/ preceding-sibling::input";

        String CONSTRUCTOR_GENERATE_ID = "ideGenerateNewConstructorFields-window";

        String CONSTRUCTOR_SELECT_FIELD_BY_NAME =
                "//div[@id='ideGenerateNewConstructorFields-window']//div[@__gwtcellbasedwidgetimpldispatchingblur"
                +
                "]//span[text()='%s']/span/input";

        String CONSTRUCTOR_SELECT_FIELD_BY_NAME_IS_HIGHLITED =
                "//div[@id='ideGenerateNewConstructorFields-window']//div[@__gwtcellbasedwidgetimpldispatchingblur"
                +
                "]//span[text()='%s']/parent::div";

        String CONSTRUCTOR_SELECT_ALL_FIELDS =
                "//div[@id='ideGenerateNewConstructorFields-window']//div[@__gwtcellbasedwidgetimpldispatchingblur"
                +
                "]//span//input/parent::span/parent::span/parent::div";

        String SELECT_ALL_CONSTRUCTOR_INPUTS =
                "//div[@id='ideGenerateNewConstructorFields-window']//div[@__gwtcellbasedwidgetimpldispatchingblur"
                +
                "]//span//input";

        String CONSTRUCTOR_BUTTON_SELECT_ALL =
                "//div[@id='ideGenerateNewConstructorFields-window']//td[text()='Select "
                +
                "All']";

        String CONSTRUCTOR_BUTTON_DESELECT_ALL = "//div[@id='ideGenerateNewConstructorFields-window']//td[text()='Deselect All']";

        String CONSTRUCTOR_BUTTON_OK =
                "//div[@id='ideGenerateNewConstructorFields-window']//td[text()='Ok']";

        String CONSTRUCTOR_BUTTON_CANCEL =
                "//div[@id='ideGenerateNewConstructorFields-window']//td[text()='Cancel']";

        String CONSTRUCTOR_RADIOBUTTON_PUBLIC_ACCESS =
                "//div[@id='ideGenerateNewConstructorFields-window']//label[text()='public']/preceding-sibling::input";

        String CONSTRUCTOR_RADIOBUTTON_PROTECTED_ACCESS =
                "//div[@id='ideGenerateNewConstructorFields-window']//label[text()"
                +
                "='protected']/preceding-sibling::input";

        String CONSTRUCTOR_RADIOBUTTON_DEFAULT_ACCESS =
                "//div[@id='ideGenerateNewConstructorFields-window']//label[text()='default']/preceding-sibling::input";

        String CONSTRUCTOR_RADIOBUTTON_PRIVATE_ACCESS =
                "//div[@id='ideGenerateNewConstructorFields-window']//label[text()='private']/preceding-sibling::input";

        String CONSTRUCTOR_GENERATE_COMMENTS_CHECKBOX =
                "//div[@id='ideGenerateNewConstructorFields-window']//label[text()='Generate constructor "
                +
                "comments']/preceding-sibling::input";

        String CONSTRUCTOR_OMIT_SUPER_CHECKBOX_ENABLED_STATE =
                "//div[@id='ideGenerateNewConstructorFields-window']//label[contains(., "
                +
                "'Omit call to default constructor super')]/preceding-sibling::input";


        String CONSTRUCTOR_OMIT_SUPER_CHECKBOX_DISABLED_STATE =
                "//div[@id='ideGenerateNewConstructorFields-window']//label[contains(., "
                +
                "'Omit call to default constructor super')]/preceding-sibling::input[@disabled]";


    }

    @FindBy(xpath = Locators.VIEW_LOCATOR)
    WebElement view;

    @FindBy(id = Locators.RENAME_BUTTON_ID)
    WebElement renameBtn;

    @FindBy(id = Locators.CANCEL_BUTTON_ID)
    WebElement cancelBtn;

    @FindBy(xpath = Locators.NEW_NAME)
    WebElement newNameInput;

    @FindBy(xpath = Locators.GETTERS_AND_SETTERS_OK_BTN)
    WebElement gettersAndSettersOkBtn;

    @FindBy(xpath = Locators.SELECT_ALL_BTN)
    WebElement selectAll;

    @FindBy(xpath = Locators.DESELECT_ALL_BTN)
    WebElement deSelectAll;

    @FindBy(xpath = Locators.SELECT_GETTERS_BTN)
    WebElement selectGetters;

    @FindBy(xpath = Locators.SELECT_SETTERS_BTN)
    WebElement selectSetters;

    @FindBy(xpath = Locators.PROTECTED_LABEL)
    WebElement protectedLabel;

    @FindBy(xpath = Locators.PRIVATE_LABEL)
    WebElement privateLabel;

    @FindBy(xpath = Locators.FINAL_LABEL)
    WebElement finalLabel;

    @FindBy(xpath = Locators.SYNCHRONIZED_LABEL)
    WebElement synchronizedLabel;

    @FindBy(xpath = Locators.ARIA_MAIN_ELEMENTS)
    WebElement areaElements;

    @FindBy(xpath = Locators.ALLOW_SETTERS_FOR_FINAL_CHECKBOX)
    WebElement allowSetersForFinalFieldCheckBox;

    @FindBy(xpath = Locators.GENERATE_METHOD_COMMENTS_CHECKBOX)
    WebElement generateMethodCommandsCheckBox;

    @FindBy(id = Locators.CONSTRUCTOR_GENERATE_ID)
    WebElement constructorGenerateForm;

    @FindBy(xpath = Locators.CONSTRUCTOR_BUTTON_SELECT_ALL)
    WebElement constructorSelectAllBtn;

    @FindBy(xpath = Locators.CONSTRUCTOR_BUTTON_DESELECT_ALL)
    WebElement constructorDeSelectAllBtn;

    @FindBy(xpath = Locators.CONSTRUCTOR_RADIOBUTTON_PUBLIC_ACCESS)
    WebElement constructorPublicAccessRadioBtn;

    @FindBy(xpath = Locators.CONSTRUCTOR_RADIOBUTTON_PROTECTED_ACCESS)
    WebElement constructorProtectedAccess;

    @FindBy(xpath = Locators.CONSTRUCTOR_RADIOBUTTON_PRIVATE_ACCESS)
    WebElement constructoPrivateAccessr;

    @FindBy(xpath = Locators.CONSTRUCTOR_BUTTON_OK)
    WebElement constructorOkBtn;

    @FindBy(xpath = Locators.CONSTRUCTOR_BUTTON_CANCEL)
    WebElement constructorCancelBtn;

    @FindBy(xpath = Locators.CONSTRUCTOR_RADIOBUTTON_PUBLIC_ACCESS)
    WebElement publicAccessRadioBtn;

    @FindBy(xpath = Locators.CONSTRUCTOR_RADIOBUTTON_DEFAULT_ACCESS)
    WebElement defaultAccessRadioBtn;

    @FindBy(xpath = Locators.CONSTRUCTOR_RADIOBUTTON_PROTECTED_ACCESS)
    WebElement protectedAccessRadioBtn;

    @FindBy(xpath = Locators.CONSTRUCTOR_RADIOBUTTON_PRIVATE_ACCESS)
    WebElement privatedAccessRadioBtn;

    @FindBy(xpath = Locators.CONSTRUCTOR_GENERATE_COMMENTS_CHECKBOX)
    WebElement generateConstructorComments;

    @FindBy(xpath = Locators.CONSTRUCTOR_OMIT_SUPER_CHECKBOX_ENABLED_STATE)
    WebElement omitDefaultCoctructorEnabledChkBox;

    @FindBy(xpath = Locators.CONSTRUCTOR_OMIT_SUPER_CHECKBOX_DISABLED_STATE)
    WebElement omitDefaultCoctructorDisabledChkBox;

    /**
     * wait rename form
     *
     * @throws Exception
     */
    public void waitRenameForm() throws Exception {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                                    .xpath(Locators


                                                                                                                   .VIEW_LOCATOR)));
    }

    /**
     * wait rename form is closed
     *
     * @throws Exception
     */
    public void waitRenameFormIsClosed() throws Exception {
        new WebDriverWait(driver(), 60).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                      .xpath(Locators.VIEW_LOCATOR)));
    }

    /** type text in new name field */
    public void typeNewName(String newName) {
        newNameInput.clear();
        newNameInput.sendKeys(newName);
    }

    /** click rename button */
    public void clickRenameButton() {
        renameBtn.click();
    }

    /**
     * wait pop up with message that need to wait initialize Java tooling before refactoring.
     *
     * @throws Exception
     */
    public void waitPopupWithWaitInitializeJavaToolingMessage() throws Exception {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                                    .xpath(Locators.WAIT_FOR_INIT_WINDOW)));
    }

    /**
     * wait pop up with message that need save file before refactoring.
     *
     * @throws Exception
     */
    public void waitPopupWithMessageThatNeedSaveFileBeforeRefactor() throws Exception {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.WAIT_FOR_NEED_TO_SAVE_WINDOW)));
    }

    /**
     * wait while row in getters and setters area will selected with specified name
     *
     * @throws Exception
     */
    public void waitRowIsSelectWitName(String name) throws Exception {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                Locators.SELECT_HIGLIGHTED_NAME,
                name))));
    }

    /**
     * wait while selected rows in area for Getters and Setters will be highlited
     *
     * @param highlitsNum
     */
    public void waitIsNumberAreasRowsIsSelected(final int highlitsNum) {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return (getInAreaSelectedRows() == highlitsNum);
            }
        });

    }

    /**
     * wait while checkboxes in area for Getters and Setters will be is checked
     *
     * @param highlitsNum
     */
    public void waitIsNumberAreasInputsIsSelected(final int highlitsNum) {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return (getAreaSelectedInputs() == highlitsNum);
            }
        });

    }

    /**
     * wait generate getters and setters form
     *
     * @throws Exception
     */
    public void waitGenerateGettersAndSettersForm() throws Exception {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                                    .id(Locators


                                                                                                                .GENERATE_GETTERS_AND_SETTERS__FORM_ID)));
    }

    /**
     * wait generate getters and setters form si closed
     *
     * @throws Exception
     */
    public void waitGenerateGettersAndSettersFormIsClose() throws Exception {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                      .id(Locators
                                                                                                                  .GENERATE_GETTERS_AND_SETTERS__FORM_ID)));
    }

    /**
     * wait generate constructor form
     *
     * @throws Exception
     */
    public void waitGenerateConstructorForm() throws Exception {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                                    .id(Locators.CONSTRUCTOR_GENERATE_ID)));
    }

    /**
     * wait generate constructor form is closed
     *
     * @throws Exception
     */
    public void waitGenerateConstructorFormIsClosed() throws Exception {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                      .id(Locators
                                                                                                                  .CONSTRUCTOR_GENERATE_ID)));
    }

    /**
     * wait generate constructor form is closed
     *
     * @throws Exception
     */

    public void waitOmitDefaultConstructorChkBoxIsDisabled() throws Exception {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(omitDefaultCoctructorDisabledChkBox));

    }

    /**
     * wait generate constructor form is closed
     *
     * @throws Exception
     */
    public void waitOmitDefaultConstructorChkBoxIsUnCheked() throws Exception {
        new WebDriverWait(driver(), 5)
                .until(ExpectedConditions.elementSelectionStateToBe(omitDefaultCoctructorEnabledChkBox, false));
    }

    /**
     * wait generate constructor form is closed
     *
     * @throws Exception
     */
    public void waitOmitDefaultConstructorChkBoxIsChecked() throws Exception {
        new WebDriverWait(driver(), 5).until(ExpectedConditions.elementSelectionStateToBe(omitDefaultCoctructorEnabledChkBox, true));
    }

    /**
     * wait while field is highlited
     *
     * @throws Exception
     */
    public void waitFieldInConstructorFormIsHighLighted(final String name) throws Exception {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                WebElement elem =
                        driver().findElement(
                                By.xpath(String.format(Locators.CONSTRUCTOR_SELECT_FIELD_BY_NAME_IS_HIGHLITED, name)));
                return elem.getCssValue("background").contains("image/png");
            }
        });
    }

    /**
     * wait while num fields is highlight state
     *
     * @param numIShighlightfield
     * @throws Exception
     */
    public void waitNumConstructorFieldsIsHiglighted(final int numIShighlightfield) throws Exception {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                int counter = 0;

                List<WebElement> highLightElements =
                        driver().findElements(By.xpath(Locators.CONSTRUCTOR_SELECT_ALL_FIELDS));

                for (WebElement elem : highLightElements) {
                    if (elem.getCssValue("background-image").contains("png")) {
                        counter++;
                    }
                }
                return (counter == numIShighlightfield);
            }
        });
    }

    /**
     * wait while num fields is highlight state
     *
     * @param numIShighlightfield
     * @throws Exception
     */
    public void waitNumConstructorFieldsCheckBoxIsCheked(final int numIShighlightfield) throws Exception {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                int counter = 0;

                List<WebElement> highLightElements =
                        driver().findElements(By.xpath(Locators.SELECT_ALL_CONSTRUCTOR_INPUTS));

                for (WebElement elem : highLightElements) {
                    if (elem.isSelected()) {
                        counter++;
                    }
                }
                return (counter == numIShighlightfield);
            }
        });
    }

    /**
     * return number of the highlited elements in Geeter and Setters area
     *
     * @return
     */
    public int getInAreaSelectedRows() {
        List<WebElement> allhigHlightRows = driver().findElements(By.xpath(Locators.ARIA_MAIN_ROWS_IS_SELECTED));
        return allhigHlightRows.size();
    }

    /**
     * return number of the highlited elements in Geeter and Setters area
     *
     * @return
     */
    public int getAreaSelectedInputs() {
        List<WebElement> allSelInput = driver().findElements(By.xpath(Locators.ARIA_MAIN_INPUTS_IS_SELECTED));
        return allSelInput.size();
    }

    /**
     * Select class field by name for generating getters and setters
     *
     * @param name
     * @throws Exception
     */
    public void clickOnClassFieldWithName(String name) throws Exception {
        WebElement element = driver().findElement(By.xpath(String.format(Locators.SELECT_FIELD_BY_NAME, name)));
        element.click();
    }

    /** click ok button on getters and setters form */
    public void clickOkGettersAndSettersForm() {
        gettersAndSettersOkBtn.click();
    }

    /** click on select all button */
    public void clickOnSelectAllButton() {
        selectAll.click();
    }

    /** click on select Getters button */
    public void clickOnSelectGettersButton() {
        selectGetters.click();
    }

    /** click on select Setters button */
    public void clickOnSelectSettersButton() {
        selectSetters.click();
    }

    /** click on protected label */
    public void clickOnProtectedLabel() {
        protectedLabel.click();
    }

    /** click on private label */
    public void clickOnPrivateLabel() {
        privateLabel.click();
    }

    /** click on deselect all btn */
    public void clickOnDeselectButton() {
        deSelectAll.click();
    }

    /**
     * Expand class field for choose getters and setters
     *
     * @param name
     * @throws Exception
     */
    public void clickOnExpandClassFieldWithName(String name) throws Exception {
        WebElement element =
                driver().findElement(
                        By.xpath(String.format(Locators.EXPAND_GETTERS_AND_SETTERS_SELECTOR_FOR_CLASS_FIELD_BY_NAME,
                                               name)));
        element.click();
    }

    /**
     * wait custom getter or setters by name
     *
     * @throws Exception
     */
    public void waitCustomGetterOrSettersByName(String name) throws Exception {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                Locators.SELECT_GETTER_OR_SETTER_BY_NAME,
                name))));
    }

    /**
     * wait while generate method checkbox is uncheked
     *
     * @throws Exception
     */
    public void waitGenerateMethodCommandsUnchecked() throws Exception {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.elementSelectionStateToBe(
                By.xpath(Locators.GENERATE_METHOD_COMMENTS_CHECKBOX),
                false));
    }

    /**
     * wait custom getter or setters by name is disappear from area
     *
     * @throws Exception
     */
    public void waitCustomGetterOrSettersByNameIsDissapear(String name) throws Exception {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(String.format(
                Locators.SELECT_GETTER_OR_SETTER_BY_NAME,
                name))));
    }

    /**
     * Click on Getter or Setter with name
     *
     * @param name
     * @throws Exception
     */
    public void clickOnGetterOrSetterWithName(String name) throws Exception {
        WebElement element =
                driver().findElement(By.xpath(String.format(Locators.SELECT_GETTER_OR_SETTER_BY_NAME, name)));
        element.click();
    }

    /** click on final label */
    public void clickOnFinalLabel() {
        finalLabel.click();
    }

    /** click on chekbox allow setters for final fields */
    public void clickOnCheckBoxAllowSettersFialFields() {
        allowSetersForFinalFieldCheckBox.click();
    }

    /** click on chekbox generate method comments */
    public void clickGenerateMethodCommentsChkBox() {
        generateMethodCommandsCheckBox.click();
    }

    /** click on synchronized label */
    public void clickOnSynchronizedLabel() {
        synchronizedLabel.click();
    }

    /**
     * click on check box with specified name
     *
     * @param nameField
     */
    public void constructorSelectFieldByName(String nameField) {
        driver().findElement(By.xpath(String.format(Locators.CONSTRUCTOR_SELECT_FIELD_BY_NAME, nameField))).click();
    }

    /** click on deselect all btn in constructor generate form */
    public void constructorDeselectAllClickBtn() {
        constructorDeSelectAllBtn.click();
    }

    /** click on select all btn in constructor generate form */
    public void constructorSelectAllClickBtn() {
        constructorSelectAllBtn.click();
    }

    /** click on ok button */
    public void constructorOkClickBtn() {
        constructorOkBtn.click();
    }

    /** click on cancel button */
    public void constructorCancelClickBtn() {
        constructorCancelBtn.click();
    }

    /** click on protected radio button */
    public void constructorProtectedRadioBtnClick() {
        protectedAccessRadioBtn.click();
    }

    /** click on default radio button */
    public void constructorDefaultRadioBtnClick() {
        defaultAccessRadioBtn.click();
    }

    /** click on public radio button */
    public void constructorPublicRadioBtnClick() {
        publicAccessRadioBtn.click();
    }

    /** click on privated radio button */
    public void constructorPrivatedRadioBtnClick() {
        privatedAccessRadioBtn.click();
    }

    /** click on generate comments checkbox */
    public void constructorGenerateCommentsChkBoxClick() {
        generateConstructorComments.click();
    }

    /** click on generate omit default constructor checkbox */
    public void constructorOmitConstructorClick() {
        omitDefaultCoctructorEnabledChkBox.click();
    }


    /**
     * check open or close generate constructor form. Return true if form is opened and false if the form is closed.
     *
     * @return
     */
    public boolean generateConstrictorFormIsPresent() {
        try {
            return constructorGenerateForm.isDisplayed();
        } catch (NotFoundException e) {
            return false;
        }
    }

}
