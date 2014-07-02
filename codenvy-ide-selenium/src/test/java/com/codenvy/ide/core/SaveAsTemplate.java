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

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Class for operations with templates: file and projects from templates.
 *
 * @author Oksana Vereshchaka
 */
public class SaveAsTemplate extends AbstractTestModule {
    /** @param ide */
    public SaveAsTemplate(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String VIEW_ID = "ideSaveAsTemplateForm";

        String VIEW_LOCATOR = "//div[@view-id='" + VIEW_ID + "']";

        String CANCEL_BUTTON_ID = "ideSaveAsTemplateFormCancelButton";

        String SAVE_BUTTON_ID = "ideSaveAsTemplateFormSaveButton";

        String TYPE_FIELD_ID = "ideSaveAsTemplateFormTypeField";

        String NAME_FIELD_ID = "ideSaveAsTemplateFormNameField";

        String DESCRIPTION_FIELD_ID = "ideSaveAsTemplateFormDescriptionField";

    }

    @FindBy(xpath = Locators.VIEW_LOCATOR)
    private WebElement view;

    @FindBy(id = Locators.SAVE_BUTTON_ID)
    private WebElement saveButton;

    @FindBy(id = Locators.CANCEL_BUTTON_ID)
    private WebElement cancelButton;

    @FindBy(name = Locators.NAME_FIELD_ID)
    private WebElement nameField;

    @FindBy(name = Locators.DESCRIPTION_FIELD_ID)
    private WebElement descriptionField;

    @FindBy(name = Locators.TYPE_FIELD_ID)
    private WebElement typeField;

    /**
     * Wait Save as template view opened.
     *
     * @throws Exception
     */
    public void waitOpened() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    WebElement view = input.findElement(By.xpath(Locators.VIEW_LOCATOR));
                    return (view != null && view.isDisplayed());
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /**
     * Wait Save as template view closed.
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
     * Returns opened state of the dialog.
     *
     * @return {@link Boolean} opened
     */
    public void isOpened() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return view != null && view.isDisplayed() && saveButton != null && saveButton.isDisplayed()
                           && cancelButton != null && cancelButton.isDisplayed() && nameField != null &&
                           nameField.isDisplayed()
                           && typeField != null && typeField.isDisplayed() && descriptionField != null
                           && descriptionField.isDisplayed();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /** Wait the enabled state of the save button. */
    public void waitSaveButtonEnabled() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return IDE().BUTTON.isButtonEnabled(saveButton);
            }
        });
    }

    /** Wait the disabled state of the save button. */
    public void waitSaveButtonDisabled() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return !(IDE().BUTTON.isButtonEnabled(saveButton));
            }
        });
    }

    /**
     * Set name of the template.
     *
     * @param name
     *         template's name
     * @throws InterruptedException
     */
    public void setName(String name) throws InterruptedException {
        IDE().INPUT.typeToElement(nameField, name, true);
    }

    /**
     * Set type of the template.
     *
     * @param type
     *         - the type
     * @throws InterruptedException
     */
    public void setType(String type) throws InterruptedException {
        IDE().INPUT.typeToElement(typeField, type, true);
    }

    /**
     * Set description of the template.
     *
     * @param description
     *         - the description
     * @throws InterruptedException
     */
    public void setDescription(String description) throws InterruptedException {
        IDE().INPUT.typeToElement(descriptionField, description, true);
    }

    /**
     * Click save button.
     *
     * @throws Exception
     */
    public void clickSaveButton() throws Exception {
        saveButton.click();
    }

    /**
     * Click cancel button.
     *
     * @throws Exception
     */
    public void clickCancelButton() throws Exception {
        cancelButton.click();
    }

}
