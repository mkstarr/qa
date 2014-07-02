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
 * @author Ann Zhuleva
 *
 */
public class Delete extends AbstractTestModule {
    /** @param ide */
    public Delete(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String VIEW_LOCATOR = "//div[@view-id='ideDeleteItemsView']";

        String OK_BUTTON_ID = "ideDeleteItemFormOkButton";

        String NO_BUTTON_ID = "ideDeleteItemFormCancelButton";

        String LABEL_LOCATOR = VIEW_LOCATOR + "//div[@class='gwt-Label']";

    }

    @FindBy(xpath = Locators.VIEW_LOCATOR)
    private WebElement view;

    @FindBy(xpath = Locators.LABEL_LOCATOR)
    private WebElement label;

    @FindBy(id = Locators.OK_BUTTON_ID)
    private WebElement okButton;

    @FindBy(id = Locators.NO_BUTTON_ID)
    private WebElement noButton;

    /**
     * Wait delete items view opened.
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
     * Wait delete items view closed.
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

    /** Click ok button. */
    public void clickOkButton() {
        okButton.click();
    }

    /** Click no button. */
    public void clickNoButton() {
        noButton.click();
    }

    /**
     * Returns deletion question text.
     *
     * @return {@link String} text displayed on deletion dialog
     */
    public String getDeletionText() {
        return (label != null) ? label.getText() : null;
    }

    /**
     * Performs deletion of selected items.
     *
     * @throws Exception
     */
    public void deleteSelectedItems() throws Exception {
        IDE().MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.DELETE);
        waitOpened();
        clickOkButton();
        waitClosed();
        IDE().LOADER.waitClosed();
    }
}
