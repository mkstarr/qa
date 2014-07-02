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
import com.codenvy.ide.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertEquals;

/**
 * Search component.
 *
 * @author Anna Shumilova
 */
public class Search extends AbstractTestModule {
    /** @param ide */
    public Search(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private static final String TREE_PREFIX_ID = "search-";

    private interface Locators {
        String VIEW_ID = "ideSearchView";

        String VIEW_LOCATOR = "//div[@view-id='" + VIEW_ID + "']";

        String PATH_FIELD_ID = "ideSearchFormPathField";

        String CONTAINING_TEXT_FIELD_ID = "ideSearchFormContentField";

        String MIME_TYPE_FIELD_ID = "ideSearchFormMimeTypeField";

        String SEARCH_BUTTON_ID = "ideSearchFormSearchButton";

        String CANCEL_BUTTON_ID = "ideSearchFormCancelButton";
    }

    @FindBy(xpath = Locators.VIEW_LOCATOR)
    private WebElement performSearchView;

    @FindBy(name = Locators.PATH_FIELD_ID)
    private WebElement pathField;

    @FindBy(name = Locators.CONTAINING_TEXT_FIELD_ID)
    private WebElement containingTextField;

    @FindBy(name = Locators.MIME_TYPE_FIELD_ID)
    private WebElement mimeTypeField;

    @FindBy(id = Locators.SEARCH_BUTTON_ID)
    private WebElement searchButton;

    @FindBy(id = Locators.CANCEL_BUTTON_ID)
    private WebElement cancelButton;

    /**
     * Wait Perform search view opened.
     *
     * @throws Exception
     */
    public void waitPerformSearchOpened() throws Exception {
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
     * Wait Perform search view closed.
     *
     * @throws Exception
     */
    public void waitPerformSearchClosed() throws Exception {
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
     * Returns opened state of the perform search view.
     *
     * @return {@link Boolean} opened state
     * @throws Exception
     */
    public boolean isPerformSearchOpened() throws Exception {
        try {
            return performSearchView != null && performSearchView.isDisplayed() && pathField != null
                   && pathField.isDisplayed() && containingTextField != null && containingTextField.isDisplayed()
                   && mimeTypeField != null && mimeTypeField.isDisplayed() && searchButton != null
                   && searchButton.isDisplayed() && cancelButton != null && cancelButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get the value(content) of path field.
     *
     * @return String path field's value
     */
    public String getPathValue() {
        return IDE().INPUT.getValue(pathField);
    }

    /**
     * Get the value(content) of containing text field.
     *
     * @return String containing text field's value
     */
    public String getContainingTextValue() {
        return IDE().INPUT.getValue(containingTextField);
    }

    /**
     * Get the value(content) of Mime type field.
     *
     * @return String Mime type field's value
     */
    public String getMimeTypeValue() {
        return IDE().INPUT.getValue(mimeTypeField);
    }

    /**
     * Type text into path field.
     *
     * @param value
     *         value to type
     * @throws InterruptedException
     */
    public void setPathValue(String value) throws InterruptedException {
        IDE().INPUT.typeToElement(pathField, value, true);
    }

    /**
     * Type text into containing text field.
     *
     * @param value
     *         value to type
     * @throws InterruptedException
     */
    public void setContainingTextValue(String value) throws InterruptedException {
        IDE().INPUT.typeToElement(containingTextField, value, true);
    }

    /**
     * Type text in Mime type field.
     *
     * @param value
     *         value to type
     * @throws InterruptedException
     */
    public void setMimeTypeValue(String value) throws InterruptedException {
        IDE().INPUT.typeToElement(mimeTypeField, value + Keys.RETURN.toString());
    }

    /** Click on Search button. */
    public void clickSearchButton() {
        searchButton.click();
    }

    /** Click on Cancel button. */
    public void clickCancelButton() {
        cancelButton.click();
    }

    /**
     * Performs search from clicking the control to showing the results panel.
     *
     * @param checkPath
     *         check path of the search
     * @param text
     *         text to search
     * @param mimeType
     *         Mime type
     * @throws Exception
     */
    public void performSearch(String checkPath, String text, String mimeType) throws Exception {
        IDE().TOOLBAR.runCommand(ToolbarCommands.File.SEARCH);
        waitPerformSearchOpened();
        assertEquals(checkPath, getPathValue());
        setContainingTextValue(text);
        setMimeTypeValue(mimeType);
        clickSearchButton();
        waitPerformSearchClosed();
    }


    /**
     * Performs search from clicking the control to showing the results panel.
     *
     * @param checkPath
     *         check path of the search
     * @param text
     *         text to search
     * @param text
     *         Mime type
     * @throws Exception
     */
    public void performSearch(String checkPath, String text) throws Exception {
        IDE().TOOLBAR.runCommand(ToolbarCommands.File.SEARCH);
        waitPerformSearchOpened();

        assertEquals(checkPath, getPathValue());
        setContainingTextValue(text);
        clickSearchButton();
        waitPerformSearchClosed();
    }


    /**
     * Generate item id in search tree
     *
     * @param href
     *         of item
     * @return id of item
     */
    public String getItemId(String href) throws Exception {
        return TREE_PREFIX_ID + Utils.md5(href);
    }
}
