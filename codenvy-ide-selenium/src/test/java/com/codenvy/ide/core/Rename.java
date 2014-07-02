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

import com.codenvy.ide.MenuCommands;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by The eXo Platform SAS .
 *
 * @author Vitaliy Gulyy
 */

public class Rename extends AbstractTestModule {
    /** @param ide */
    public Rename(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {

        String VIEW_LOCATOR = "//div[@view-id='ideRenameItemForm']";

        String RENAME_BUTTON_ID = "ideRenameItemFormRenameButton";

        String CANCEL_BUTTON_ID = "ideRenameItemFormCancelButton";

        String NEW_NAME_ID = "ideRenameItemFormRenameField";

        String MIME_TYPE_ID = "ideRenameItemFormMimeTypeField";

        String WARNING_MESSAGE_CLASS_NAME = "exo-rename-warning-msg";

    }

    @FindBy(xpath = Locators.VIEW_LOCATOR)
    WebElement view;

    @FindBy(name = Locators.NEW_NAME_ID)
    WebElement newNameField;

    @FindBy(name = Locators.MIME_TYPE_ID)
    WebElement mimeTypeField;

    @FindBy(id = Locators.RENAME_BUTTON_ID)
    WebElement renameButton;

    @FindBy(id = Locators.CANCEL_BUTTON_ID)
    WebElement cancelButton;

    @FindBy(className = Locators.WARNING_MESSAGE_CLASS_NAME)
    WebElement warningMessage;

    /**
     * Wait Rename item view opened.
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
     * Wait Rename item view closed.
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
     * Returns value of new name field.
     *
     * @return {@link String} value of new name field
     */
    public String getNewName() {
        return IDE().INPUT.getValue(newNameField);
    }

    /**
     * Sets new item's name.
     *
     * @param newName
     *         name to rename to
     * @throws InterruptedException
     */
    public void setNewName(String newName) throws InterruptedException {
        newNameField.clear();
        IDE().INPUT.typeToElement(newNameField, newName, true);
    }

    /**
     * Returns value of mime type field.
     *
     * @return {@link String} mime type field value
     */
    public String getMimeType() {
        return IDE().INPUT.getValue(mimeTypeField);
    }

    /**
     * Sets the new file's mime type.
     *
     * @param mimeType
     *         mime type to change to
     * @throws InterruptedException
     */
    public void setMimeType(String mimeType) throws InterruptedException {
        IDE().INPUT.setComboboxValue(mimeTypeField, mimeType);
    }

    /**
     * Clicks on Rename button.
     *
     * @throws Exception
     */
    public void clickRenameButton() throws Exception {
        renameButton.click();
    }

    /**
     * Returns the enabled state of the rename button.
     *
     * @return enabled state of the rename button
     */
    public boolean isRenameButtonEnabled() {
        return IDE().BUTTON.isButtonEnabled(renameButton);
    }

    /**
     * Clicks on Cancel button.
     *
     * @throws Exception
     */
    public void clickCancelButton() throws Exception {
        cancelButton.click();
    }

    /**
     * Performs rename of the item (only name changes).
     *
     * @param newName
     *         new name
     * @throws Exception
     */
    public void rename(String newName) throws Exception {
        rename(newName, null);
    }

    /**
     * Perform renaming and/or changing the file's mime type.
     *
     * @param newName
     *         new name of the item (may be <code>null</code> if only mime type is changed)
     * @param mimeType
     *         new file's mime type (may be <code>null</code> if only name is changed)
     * @throws Exception
     */
    public void rename(String newName, String mimeType) throws Exception {
        IDE().MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.RENAME);
        waitOpened();

        if (newName != null) {
            setNewName(newName);
        }

        if (mimeType != null) {
            setMimeType(mimeType);
        }
        clickRenameButton();
        waitClosed();
        IDE().LOADER.waitClosed();
    }

    /** @return {@link String} text of the warning message */
    public String getWarningMessage() {
        return warningMessage.getText();
    }

    /**
     * Returns enabled state of the mime type field.
     *
     * @return enabled state of the mime type field
     */
    public boolean isMimeTypeFieldEnabled() {
        return mimeTypeField.isEnabled();
    }
}
