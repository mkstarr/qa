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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by The eXo Platform SAS .
 *
 * @author Vitaliy Gulyy
 *
 */

public class AskForValueDialog extends AbstractTestModule {

    /** @param ide */
    public AskForValueDialog(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    public interface Locator {
        String VIEW_LOCATOR = "//div[@view-id='ideAskForValueView']";

        String OK_BUTTON_ID = "ideAskForValueViewYesButton";

        String NO_BUTTON_ID = "ideAskForValueViewNoButton";

        String CANCEL_BUTTON_ID = "ideAskForValueViewCancelButton";

        String VALUE_FIELD_ID = "ideAskForValueViewValueField";

        String ASK_SSH_FORM_FIELD = "";

        String ASK_FORM_VIEW = "codenvyAskForValueModalView-window";

        String INPUT_VALUE_FIELD = "//div[@id='codenvyAskForValueModalView-window']//input";

        String ASK_FORM_OK_BUTTON_ID = "OkButton";

        String ASK_FORM_CANCEL_BUTTON_ID = "CancelButton";


    }

    @FindBy(id = Locator.ASK_FORM_OK_BUTTON_ID)
    private WebElement okBtn;

    @FindBy(xpath = Locator.VIEW_LOCATOR)
    private WebElement view;

    @FindBy(xpath = Locator.INPUT_VALUE_FIELD)
    private WebElement inputValueField;

    @FindBy(id = Locator.OK_BUTTON_ID)
    private WebElement okButton;

    @FindBy(id = Locator.NO_BUTTON_ID)
    private WebElement noButton;

    @FindBy(id = Locator.CANCEL_BUTTON_ID)
    private WebElement cancelButton;

    @FindBy(name = Locator.VALUE_FIELD_ID)
    private WebElement valueField;

    @FindBy(id = Locator.ASK_FORM_VIEW)
    private WebElement askFormView;

    /** @return {@link Boolean} */
    public boolean isOpened() {
        try {
            return view != null && view.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Waits until AskForValue dialog will be opened.
     *
     * @throws Exception
     */
    public void waitOpened() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return view != null && view.isDisplayed() && valueField != null && valueField.isDisplayed();
            }
        });
    }

    /**
     * Waits until AskForValue dialog closes.
     *
     * @throws Exception
     */
    public void waitClosed() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    input.findElement(By.xpath(Locator.VIEW_LOCATOR));
                    return false;
                } catch (NoSuchElementException e) {
                    return true;
                }
            }
        });
    }

    /**
     * Clicks on "Ok" button.
     *
     * @throws Exception
     */
    public void clickOkButton() throws Exception {
        okButton.click();
    }

    /**
     * Clicks on "No" button.
     *
     * @throws Exception
     */
    public void clickNoButton() throws Exception {
        noButton.click();
    }

    /**
     * Determines whether the "No" button is visible.
     *
     * @return
     */
    public boolean isNoButtonPresent() {
        return noButton != null && noButton.isDisplayed();
    }

    /**
     * Clicks on "Cancel" button.
     *
     * @throws Exception
     */
    public void clickCancelButton() throws Exception {
        cancelButton.click();
    }

    /**
     * Sets a new value of text field.
     *
     * @param value
     * @throws Exception
     */
    public void setValue(String value) throws Exception {
        IDE().INPUT.typeToElement(valueField, value, true);
    }

    /** wait for enabled finish button */
    public void waitAskForValueForm() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.id(Locator.ASK_FORM_VIEW)));
    }

    /** wait for enabled finish button */
    public void waitAskForValueFormDisappear() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.id(Locator.ASK_FORM_VIEW)));
    }

    /**
     * set value in set value form
     *
     * @param value
     *         value
     */
    public void setValueInForm(String value) {
        inputValueField.sendKeys(value);
    }

    /** click ok */
    public void clickOkBtn() {
        okBtn.click();
        waitAskForValueFormDisappear();
    }

}
