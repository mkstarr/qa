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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Roman Iuvshin
 *
 */
public class File extends AbstractTestModule {

    /** @param ide */
    public File(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String NEW_FILE_WINDOW_ID      = "ideCreateFileForm-window";

        String INPUT_NEW_NAME_FIELD_ID = "ideCreateFileFormNameField";

        String CREATE_BUTTON           = "ideCreateFileFormCreateButton";

        String CANCEL_BUTTON           = "ideCreateFileFormCancelButton";
    }

    @FindBy(id = Locators.NEW_FILE_WINDOW_ID)
    private WebElement newFileWindow;

    @FindBy(name = Locators.INPUT_NEW_NAME_FIELD_ID)
    private WebElement newFileNameInput;

    @FindBy(id = Locators.CREATE_BUTTON)
    private WebElement createBtn;

    @FindBy(id = Locators.CANCEL_BUTTON)
    private WebElement cancelBtn;

    /** wait for create new file window appear */
    public void waitCreateNewFileWindow() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                              .id(Locators


                                                                                              .NEW_FILE_WINDOW_ID)));
    }

    /** wait for create new file window disappear */
    public void waitCreateNewFileWindowIsClosed() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                .id(Locators


                                                                                                .NEW_FILE_WINDOW_ID)));
    }

    /** type new file name */
    public void typeNewFileName(String name) {
        newFileNameInput.clear();
        newFileNameInput.sendKeys(name);
    }

    /** click create button */
    public void clickCreateButton() {
        createBtn.click();
        waitCreateNewFileWindowDisappear();
    }

    /** click cancel button */
    public void clickCancelButton() {
        cancelBtn.click();
        waitCreateNewFileWindowDisappear();
    }

    /** wait for create window disappear */
    private void waitCreateNewFileWindowDisappear() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                .id(Locators.NEW_FILE_WINDOW_ID)));
    }
}
