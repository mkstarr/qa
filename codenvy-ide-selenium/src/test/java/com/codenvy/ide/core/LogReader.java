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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Oksana Vereshchaka */
public class LogReader extends AbstractTestModule {

    /** @param ide */
    public LogReader(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String LOG_READER_VIEW = "//div[@view-id='ideExtensionLogReaderView']";

        String LOG_READER_CONTENT = LOG_READER_VIEW + "//pre";

        String PREV_BUTTON_LOCATOR = LOG_READER_VIEW + "//div[@title='Previous Log']";

        String NEXT_BUTTON_LOCATOR = LOG_READER_VIEW + "//div[@title='Next Log']";

        String ENABLED_ATTR = "enabled";

        String OPENED_OPERATION_FORM =
                "//div[@id='operation']/ancestor::div[contains(@style, 'height: 300px')]";

        String CLOSED_OPERATION_FORM =
                "//div[@id='operation']/ancestor::div[contains(@style, 'height: 0px')]";


        String CLOSE_BTN = "//td[text()='Log']/..//div[@button-name='close-tab']";
    }

    @FindBy(xpath = Locators.LOG_READER_VIEW)
    private WebElement view;

    @FindBy(xpath = Locators.LOG_READER_CONTENT)
    private WebElement content;

    @FindBy(xpath = Locators.PREV_BUTTON_LOCATOR)
    private WebElement prevButton;

    @FindBy(xpath = Locators.NEXT_BUTTON_LOCATOR)
    private WebElement nextButton;

    @FindBy(xpath = Locators.OPENED_OPERATION_FORM)
    private WebElement operationForm;


    @FindBy(xpath = Locators.CLOSE_BTN)
    private WebElement closeBtn;

    public void waitOpened() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {

                return operationForm != null && operationForm.isDisplayed() && view != null && view.isDisplayed()
                       && content != null;
            }
        });
    }

    /** wait while logreader panel closes */
    public void waitClosed() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.CLOSED_OPERATION_FORM)));

    }

    /** click on close img and wait while logreader form clodes */
    public void closeLogreader() {
        closeBtn.click();
        waitClosed();
    }

    public boolean isPreviousButtonEnabled() {
        return isButtonEnabled(prevButton);
    }

    public void waitNextButtonEnabled() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return isButtonEnabled(nextButton);
            }
        });
    }

    public void waitNextButtonDisabled() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return !(isButtonEnabled(nextButton));
            }
        });
    }

    public void clickPrevButton() {
        prevButton.click();
    }

    public void clickNextButton() {
        nextButton.click();
    }

    public String getLogContent() {
        return content.getText();
    }

    private boolean isButtonEnabled(WebElement button) {
        return Boolean.parseBoolean(button.getAttribute(Locators.ENABLED_ATTR));
    }

}
