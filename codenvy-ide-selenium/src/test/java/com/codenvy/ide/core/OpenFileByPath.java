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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Anna Shumilova */
public class OpenFileByPath extends AbstractTestModule {
    /** @param ide */
    public OpenFileByPath(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private final class Locators {
        public static final String VIEW_ID = "ideOpenFileByPathWindow";

        public static final String VIEW_LOCATOR = "//div[@view-id='" + VIEW_ID + "']";

        public static final String OPEN_BUTTON_ID = "ideOpenFileByPathFormOpenButton";

        public static final String CANCEL_BUTTON_ID = "ideOpenFileByPathFormCancelButton";

        public static final String FILE_PATH_FIELD_NAME = "ideOpenFileByPathFormFilePathField";

    }

    @FindBy(xpath = Locators.VIEW_LOCATOR)
    WebElement view;

    @FindBy(name = Locators.FILE_PATH_FIELD_NAME)
    WebElement pathField;

    @FindBy(id = Locators.OPEN_BUTTON_ID)
    WebElement openButton;

    @FindBy(id = Locators.CANCEL_BUTTON_ID)
    WebElement cancelButton;

    /**
     * Waits for Open file by path view to be opened.
     *
     * @throws Exception
     */
    public void waitOpened() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return isOpened();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /**
     * Waits for Open file by path view to be closed.
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
     * Checks view's components.
     *
     * @return {@link Boolean} if <code>true</code> view's elements are present
     */
    public boolean isOpened() {
        return (view != null && view.isDisplayed() && openButton != null && openButton.isDisplayed() &&
                pathField != null
                && pathField.isDisplayed() && cancelButton != null && cancelButton.isDisplayed());
    }

    public void clickOpenButton() {
        openButton.click();
    }

    public void clickCancelButton() {
        cancelButton.click();
    }

    public boolean isOpenButtonEnabled() {
        return IDE().BUTTON.isButtonEnabled(openButton);
    }

    public boolean isCancelButtonEnabled() {
        return IDE().BUTTON.isButtonEnabled(cancelButton);
    }

    public String getFilePath() {
        return pathField.getText();
    }

    public void setFilePath(String path) throws InterruptedException {
        IDE().INPUT.typeToElement(pathField, path, true);
    }
}
