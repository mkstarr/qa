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

import com.codenvy.ide.TestConstants;
import com.google.common.base.Predicate;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Constructor;

import static org.junit.Assert.fail;

/**
 * This class provides methods for working with Warning dialog.
 * <p/>
 * Created by The eXo Platform SAS .
 *
 * @author Vitaliy Gulyy
 */

public class WarningDialog extends AbstractTestModule {
    /** @param ide */
    public WarningDialog(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    interface Locators {

        String WARNING_DIALOG_ID = "ideWarningModalView-window";

        String WARNING_DIALOG_VIEW_ID = "ideWarningModalView";

        String WARNING_DIALOG_LOCATOR = "//div[@view-id='" + WARNING_DIALOG_VIEW_ID + "']";

        String OK_BUTTON_ID = "OkButton";

        String MESSAGE_SELECTOR = "div[view-id=" + WARNING_DIALOG_VIEW_ID + "] div.gwt-Label";

        String MESSAGE_TITLE = "//div[@id='ideWarningModalView-window']//div[text()='%s']";
    }

    @FindBy(id = Locators.OK_BUTTON_ID)
    private WebElement okButton;

    @FindBy(how = How.CSS, using = Locators.MESSAGE_SELECTOR)
    private WebElement warningMessage;

    @FindBy(id = Locators.WARNING_DIALOG_ID)
    private WebElement warningDialog;

    /**
     * Wait Warning dialog opened.
     *
     * @throws Exception
     */
    public void waitOpened() throws Exception {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.WARNING_DIALOG_LOCATOR)));
    }

    /**
     * Wait Warning dialog closed.
     *
     * @throws Exception
     */
    public void waitClosed() throws Exception {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.WARNING_DIALOG_LOCATOR)));
    }


    /**
     * Wait text in warning dialog
     *
     * @throws Exception
     */
    public void waitTextInDialogPresent(final String text) throws Exception {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return getWarningMessage().contains(text);
            }

        });
    }


    /**
     * return true if dialog present
     * Use isOpened() method instead.
     *
     * @return
     */
    @Deprecated
    public boolean isDisplayed() {
        try {
            driver().findElement(By.id(Locators.WARNING_DIALOG_ID));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean isOpened() {
        return warningDialog != null && warningDialog.isDisplayed();
    }

    /**
     * Click ok button.
     *
     * @throws Exception
     */
    public void clickOk() {

        new WebDriverWait(driver(), 5).until(ExpectedConditions.elementToBeClickable(By.id("OkButton")));
        okButton.click();
    }

    public void clickYes() throws Exception {
        fail();
        Thread.sleep(TestConstants.REDRAW_PERIOD);
    }

    public void clickNo() throws Exception {
        fail();
        Thread.sleep(TestConstants.REDRAW_PERIOD);
    }

    public void clickCancel() throws Exception {
        fail();
        Thread.sleep(TestConstants.REDRAW_PERIOD);
    }

    /**
     * Get warning message.
     *
     * @return {@link String} warning message
     */
    public String getWarningMessage() {
        String text = warningMessage.getText().trim();
        text = (text.endsWith("\n")) ? text.substring(0, text.length() - 2) : text;
        return text;
    }

    /**
     * wait for warning dialog with title
     *
     * @param warningTitle
     */
    public void waitTitleInDialogPresent(String warningTitle) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                Locators.MESSAGE_TITLE,
                warningTitle))));

    }

    public void closeDialog() {
        if (isOpened()) {
            clickOk();
        }
    }

    /**
     * Wait on some event. WarningDialog is interrupting of waiting, and then exception with error message from WarningDialog is being
     * thrown
     *
     * @param event
     * @param timeout
     * @param clazz
     *         class of RuntimeException, but not NoSuchElementException
     */
    public void waitEventOrWarningDialog(final Predicate<WebDriver> event, int timeout, final Class<? extends RuntimeException> clazz) {
        Predicate<WebDriver> waitEventOrWarningDialogPredicate = new Predicate<WebDriver>() {
            public boolean apply(WebDriver input) {
                try {
                    if (driver().findElement(By.xpath(Locators.WARNING_DIALOG_LOCATOR)) != null) {
                        // create instance of exception clazz and pass into its constructor the message from WarningDialog
                        RuntimeException exceptionObject = null;
                        try {
                            Constructor<? extends RuntimeException> constructor = clazz.getConstructor(String.class);
                            exceptionObject = constructor.newInstance(new Object[]{getErrorDialogMessage()});
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        throw exceptionObject;
                    }

                    return event.apply(driver());
                } catch (NoSuchElementException e) {
                    return event.apply(driver());
                }
            }
        };

        new WebDriverWait(driver(), timeout).until(waitEventOrWarningDialogPredicate);
    }

    /** @return error message or null if error dialog wasn't opened */
    public String getErrorDialogMessage() {
        String errorMessage = null;

        if (isOpened()) {
            errorMessage = getWarningMessage();
        }

        return errorMessage;
    }
}
