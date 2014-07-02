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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by The eXo Platform SAS .
 *
 * @author <a href="mailto:mmusienko@exoplatform.com">Musienko Maksim</a>
 *
 */

public class GoToLine extends AbstractTestModule {

    /** @param ide */
    public GoToLine(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String VIEW_LOCATOR = "//div[@view-id='ideGoToLineForm']";

        String LINE_NUBER_FIELD_ID = "ideGoToLineFormLineNumberField";

        String LINE_RANGE_LABEL_ID = "ideGoToLineFormLineRangeLabel";

        String GO_TO_LINE_BUTTON_ID = "ideGoToLineFormGoButton";

        String CANCEL_BUTTON_ID = "ideGoToLineFormCancelButton";
    }

    public static final String RANGE_LABEL = "Enter line number (%d..%d):";

    @FindBy(xpath = Locators.VIEW_LOCATOR)
    private WebElement view;

    @FindBy(name = Locators.LINE_NUBER_FIELD_ID)
    private WebElement lineNumberField;

    @FindBy(id = Locators.LINE_RANGE_LABEL_ID)
    private WebElement lineRangeLabel;

    @FindBy(id = Locators.GO_TO_LINE_BUTTON_ID)
    private WebElement goButton;

    @FindBy(id = Locators.CANCEL_BUTTON_ID)
    private WebElement cancelButton;

    /**
     * Wait Go to line view opened.
     *
     * @throws Exception
     */
    public void waitOpened() throws Exception {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                                    .xpath(Locators


                                                                                                                   .VIEW_LOCATOR)));
    }

    /**
     * Wait Go to line view closed.
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

    /** Check Go To LineForm present */
    public void waitGoToLineViewPresent() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver arg0) {
                return (view != null && view.isDisplayed()) && (goButton != null && goButton.isDisplayed())
                       && (cancelButton != null && cancelButton.isDisplayed())
                       && (lineNumberField != null && lineNumberField.isDisplayed());
            }
        });
    }

    /**
     * Get the value of the line number range.
     *
     * @return {@link String}
     */
    public String getLineNumberRangeLabel() {
        return lineRangeLabel.getText();
    }

    /**
     * Type value of the line number field.
     *
     * @param value
     *         line number value
     * @throws InterruptedException
     */
    public void typeIntoLineNumberField(String value) throws InterruptedException {
        driver().findElement(By.name("ideGoToLineFormLineNumberField")).click();
        IDE().INPUT.typeToElement(lineNumberField, value, true);
    }

    /**
     * Click cancel button.
     *
     * @throws InterruptedException
     */
    public void clickCancelButton() throws InterruptedException {
        cancelButton.click();
    }

    /**
     * Click go to line button.
     *
     * @throws InterruptedException
     */
    public void clickGoButton() throws InterruptedException {
        goButton.click();
    }

    /**
     * Move to pointed line number.
     *
     * @param line
     *         line number to move to
     * @throws Exception
     */
    public void goToLine(int line) throws Exception {
        IDE().MENU.waitCommandEnabled(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.GO_TO_LINE);
        IDE().MENU.runCommand(MenuCommands.Edit.EDIT_MENU, MenuCommands.Edit.GO_TO_LINE);
        waitOpened();
        typeIntoLineNumberField(String.valueOf(line));
        clickGoButton();
        waitClosed();
    }
}
