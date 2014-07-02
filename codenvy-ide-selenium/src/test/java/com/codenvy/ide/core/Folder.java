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
 * @author Oksana Vereshchaka
 *
 */
public class Folder extends AbstractTestModule {

    /** @param ide */
    public Folder(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String VIEW_LOCATOR = "//div[@view-id='ideCreateFolderForm']";

        String INPUT_FIELD_NAME = "ideCreateFolderFormNameField";

        String CREATE_BUTTON_ID = "ideCreateFolderFormCreateButton";

        String CANCEL_BUTTON_ID = "ideCreateFolderFormCancelButton";
    }

    @FindBy(xpath = Locators.VIEW_LOCATOR)
    WebElement view;

    @FindBy(name = Locators.INPUT_FIELD_NAME)
    WebElement nameField;

    @FindBy(id = Locators.CREATE_BUTTON_ID)
    WebElement createButton;

    @FindBy(id = Locators.CANCEL_BUTTON_ID)
    WebElement cancelButton;

    /**
     * Wait Create folder view opened.
     *
     * @throws Exception
     */
    public void waitOpened() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return view != null && view.isDisplayed();
            }
        });
    }

    /**
     * Wait create folder view closed.
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
     * Type folder's name.
     *
     * @param name
     *         folder's name
     * @throws InterruptedException
     */
    public void typeFolderName(String name) throws InterruptedException {
        IDE().INPUT.typeToElement(nameField, name, true);
    }

    /**
     * Click create folder button.
     *
     * @throws Exception
     */
    public void clickCreateButton() throws Exception {
        createButton.click();
    }

    /**
     * Click cancel button.
     *
     * @throws Exception
     */
    public void clickCancelButton() throws Exception {
        cancelButton.click();
    }

    /**
     * Performs operations to create new folder.
     *
     * @param name
     *         folder's name. May be <code>null</code> if default name is used
     * @throws Exception
     */
    public void createFolder(String name) throws Exception {
        IDE().MENU.runCommand(MenuCommands.File.FILE, MenuCommands.New.NEW, MenuCommands.New.FOLDER);
        waitOpened();

        if (name != null) {
            typeFolderName(name);
        }

        clickCreateButton();
        waitClosed();
    }
}
