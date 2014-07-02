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
 * Operations with information dialogs.
 *
 * @author Vitaliy Gulyy
 *
 */

public class InformationDialog extends AbstractTestModule {
    /** @param ide */
    public InformationDialog(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private static final String VIEW_ID = "ideInformationModalView";

    private static final String VIEW_LOCATOR = "//div[@view-id='" + VIEW_ID + "']";

    private static final String OK_BUTTON_ID = "OkButton";

    private static final String MESSAGE_SELECTOR = "div[view-id=" + VIEW_ID + "] div.gwt-Label";

    private static final String MESSAGE_CONTAIN = "//div[@view-id='ideInformationModalView']//tbody//table[contains(.,'%s')]";

    @FindBy(xpath = VIEW_LOCATOR)
    private WebElement view;

    @FindBy(id = OK_BUTTON_ID)
    private WebElement okButton;

    @FindBy(css = MESSAGE_SELECTOR)
    private WebElement message;

    /** Check, is information dialog appeared. */
    public boolean isOpened() {
        return (view != null) && view.isDisplayed() && (okButton != null);
    }

    /**
     * Click Ok button at information dialog.
     *
     * @throws Exception
     */
    public void clickOk() throws Exception {
        okButton.click();
        waitClosed();
    }

    /**
     * Wait for information dialog opened.
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
     * Wait dialog opened with pointed message.
     *
     * @param message
     *         message
     * @throws Exception
     */
    public void waitOpened(final String message) throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return view != null && view.isDisplayed() && message.equals(getMessage().trim());
            }
        });
    }

    /**
     * Wait information dialog closed.
     *
     * @throws Exception
     */
    public void waitClosed() throws Exception {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.invisibilityOfElementLocated(
                        By.xpath(VIEW_LOCATOR)));
    }

    /**
     * Get information message.
     *
     * @return {@link String} message
     */
    public String getMessage() {
        String text = message.getText().trim();
        text = (text.endsWith("\n")) ? text.substring(0, text.length() - 2) : text;
        return text;
    }

    /**
     * wait for message in information dialog
     *
     * @param message - message
     */
    public void waitMessage(String message) {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath(String.format(MESSAGE_CONTAIN, message))));
    }
}
