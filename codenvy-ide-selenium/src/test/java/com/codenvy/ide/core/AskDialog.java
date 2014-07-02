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

import com.google.common.base.Predicate;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Evgen Vidolob */
public class AskDialog extends AbstractTestModule {

    /** @param ide */
    public AskDialog(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String VIEW_ID = "ideAskModalView";

        String VIEW_LOCATOR = "//div[@view-id='" + VIEW_ID + "']";

        String ASK_TITLE_SELECTOR = "div[id^=" + VIEW_ID + "] div.Caption>div";

        String QUESTION_SELECTOR = "div[view-id=" + VIEW_ID + "] div.gwt-Label";

        String YES_BUTTON_ID = "YesButton";

        String NO_BUTTON_ID = "NoButton";

        String CONTINUE_BTN = "ContinueButton";

        String CANCEL_BTN = "CancelButton";
    }

    @FindBy(xpath = Locators.VIEW_LOCATOR)
    private WebElement view;

    @FindBy(css = Locators.ASK_TITLE_SELECTOR)
    private WebElement askTitle;

    @FindBy(css = Locators.QUESTION_SELECTOR)
    private WebElement question;

    @FindBy(id = Locators.YES_BUTTON_ID)
    private WebElement yesButton;

    @FindBy(id = Locators.NO_BUTTON_ID)
    private WebElement noButton;


    @FindBy(id = Locators.CONTINUE_BTN)
    private WebElement continueBtn;


    @FindBy(id = Locators.CANCEL_BTN)
    private WebElement cancelBtn;

    /**
     * Wait dialog view opened.
     *
     * @throws Exception
     */
    public void waitOpened() {
        new WebDriverWait(driver(), 240).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.VIEW_LOCATOR)));
    }

    /**
     * Wait dialog view closed.
     *
     * @throws Exception
     */
    public void waitClosed() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.VIEW_LOCATOR)));
    }

    /**
     * Wait dialog view closed.
     *
     * @param timeout
     * @param clazz
     * @throws RuntimeException
     *         clazz with error message from WarningDialog if this dialog is displayed.
     */
    public void waitClosed(int timeout, Class<? extends RuntimeException> clazz) throws RuntimeException {
        IDE().WARNING_DIALOG.waitEventOrWarningDialog(new Predicate<WebDriver>() {
            public boolean apply(WebDriver input) {
                return ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.VIEW_LOCATOR)).apply(driver());
            }
        }, timeout, clazz);
    }

    /**
     * Return state of view
     *
     * @return {@link Boolean}
     */
    public boolean isOpened() {
        try {
            return view != null && view.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * Click No button.
     *
     * @throws Exception
     */
    public void clickNo() {
        noButton.click();
    }

    /** wait Yes button */
    public void waitYesButton() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOf(yesButton));
    }

    /**
     * Click Yes button.
     *
     * @throws Exception
     */
    public void clickYes() {
        waitYesButton();
        yesButton.click();
    }

    /**
     * Click on Cancel button.
     *
     * @throws Exception
     */
    public void clickCancelBtn() {
        cancelBtn.click();
    }


    /**
     * Click continue btn
     *
     * @throws Exception
     */
    public void clickContinueBtn() {
        continueBtn.click();
    }


    /**
     * Get question' text.
     *
     * @return {@link String} question
     */
    public String getQuestion() {
        String text = question.getText().trim();
        text = (text.endsWith("\n")) ? text.substring(0, text.length() - 2) : text;
        return text;
    }

    /**
     * Returns the title(caption) of the ask dialog.
     *
     * @return {@link String} title of the ask dialog
     */
    public String getTitle() {
        return askTitle.getText();
    }

}
