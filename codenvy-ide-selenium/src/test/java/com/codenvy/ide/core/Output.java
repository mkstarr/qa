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
import com.codenvy.ide.TestConstants;
import com.google.common.base.Function;
import com.google.common.base.Predicate;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.concurrent.TimeUnit;

/** @author Evgen Vidolob */
public class Output extends AbstractTestModule {
    /** @param ide */
    public Output(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    public interface Messages {
        String APPLICATION_SUCCESSFULLY_STARTED_ON_MESSAGE_TEMPLATE = "Application %s successfully started on ";

        String APPLICATION_SUCCESSFULLY_DELETED_MESSAGE_TEMPLATE = "[INFO] Application %s successfully deleted.";

        String APPLICATION_SUCCESSFULLY_DELETED_ON_PAAS_MESSAGE_TEMPLATE = "[INFO] Application %s is successfully deleted on %s.";

        String BUILD_SUCCESS = "Project successfully built.";

        String BUILD_FAILED = "Building of project failed. See details in Build project view.";

        String ERROR_MESSAGE_PREFIX = "[ERROR]";

        // for AppFog PaaS
        String SUCCESS_LOGIN_TO_PAAS_MESSAGE_TEMPLATE = "[INFO] Logged in %s successfully.";

        // for AWS PaaS
        String SUCCESS_LOGIN_TO_PAAS_MESSAGE_2_TEMPLATE = "[INFO] Successfully logged in %s.";

        String TERMINATING_ENVIRONMENT_MESSAGE_TEMPLATE = "[INFO] Terminating Environment %s...";

        String ENVIRONMENT_SUCCESSFULLY_TERMINATED_MESSAGE_TEMPLATE = "[INFO] Environment %s successfully terminated.";
    }

    private interface Locators {
        String VIEW_ID = "ideOutputView";

        String VIEW_LOCATOR = "//div[@view-id='" + VIEW_ID + "']";

        String CLEAR_BUTTON_SELECTOR = "//div[@view-id='" + VIEW_ID + "']//div[@title='Clear output']";

        String OUTPUT_CONTENT_ID = "ideOutputContent";

        String OUTPUT_ROW_BY_INDEX = "//div[@id='" + OUTPUT_CONTENT_ID + "']/div[%d]";

        String OUTPUT_ERROR_BY_INDEX = "//div[@id='" + OUTPUT_CONTENT_ID + "']/div[%d]//b";

        String OUTPUT_TAB = "//div[@class='gwt-TabLayoutPanelTabs']//td[@class='tabTitleText' and text()='Output']";

        String OPERATION_FORM = "//div[@id='operation']/ancestor::div[contains(@style, 'height: 300')]";

        String CLOSE_OUTPUT_TAB = "//div[@tab-title='Output']";

        String SEPARATE_MESSAGE_XPATH_TEMPLATE = "//*[@id='" + OUTPUT_CONTENT_ID + "']/div/table/tbody/tr/td/font[contains(text(), '%s')]";
    }

    @FindBy(xpath = Locators.VIEW_LOCATOR)
    private WebElement view;

    @FindBy(xpath = Locators.CLEAR_BUTTON_SELECTOR)
    private WebElement clearButton;

    @FindBy(id = Locators.OUTPUT_CONTENT_ID)
    private WebElement outputContent;

    @FindBy(xpath = Locators.OUTPUT_TAB)
    private WebElement outputTab;

    @FindBy(xpath = Locators.OPERATION_FORM)
    private WebElement operationForm;

    @FindBy(xpath = Locators.CLOSE_OUTPUT_TAB)
    private WebElement closeOuputForm;

    /**
     * Wait Output view opened.
     *
     * @throws Exception
     */
    public void waitOpened() throws Exception {
        new WebDriverWait(driver(), 60).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return operationForm != null && operationForm.isDisplayed() && view != null && view.isDisplayed();
            }
        });
    }

    /**
     * Wait Output view closed.
     *
     * @throws Exception
     */
    public void waitClosed() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return view == null || !view.isDisplayed();
                } catch (NoSuchElementException e) {
                    return true;
                }
            }
        });
    }

    /**
     * Get Output message by its index.
     *
     * @param index
     *         message's index. <b>Message count starts with 1.</b>
     * @return {@link String} text of the message
     */
    public String getOutputMessage(int index) {
        WebElement message = getMessageByIndex(index);
        return message.getText();
    }

    /** Check is Output form opened. */
    public boolean isOpened() {
        try {
            return operationForm != null && operationForm.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param messageIndex
     *         index of the message to wait
     * @param timeout
     *         seconds
     * @throws Exception
     */
    public void waitForMessageShow(final int messageIndex, int timeout) throws Exception {
        new WebDriverWait(driver(), timeout).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    WebElement message = getMessageByIndex(messageIndex);
                    return message != null && message.isDisplayed();
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    public void waitForMessageShow(final int messageIndex) throws Exception {
        waitForMessageShow(messageIndex, 10);
    }

    /**
     * wait any sub text in output panel
     *
     * @param subText
     * @throws Exception
     */
    public void waitForSubTextPresent(final String subText) {
        new WebDriverWait(driver(), 100).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return getAllMessagesFromOutput().contains(subText);
            }
        });
    }


    /**
     * wait sub text in output panel does not present
     *
     * @param subText
     * @throws Exception
     */
    public void waitForSubTextIsNotPresent(final String subText) throws Exception {
        new WebDriverWait(driver(), 120).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {

                return !getAllMessagesFromOutput().contains(subText);
            }
        });
    }

    /**
     * Click on error message, pointed by its position in output panel. The index starts from 1.
     *
     * @param messageNumber
     *         number of the message
     */
    public void clickOnErrorMessage(int messageNumber) {
        outputContent.findElement(By.xpath(String.format(Locators.OUTPUT_ERROR_BY_INDEX, messageNumber))).click();
    }

    /** click on label tab Output panel */
    public void clickOnOutputTab() {
        outputTab.click();
    }

    /**
     * @param index
     * @return
     */
    private WebElement getMessageByIndex(int index) {
        return outputContent.findElement(By.xpath(String.format(Locators.OUTPUT_ROW_BY_INDEX, index)));
    }

    /**
     * get all text from output panel
     *
     * @return
     */
    public String getAllMessagesFromOutput() {
        return outputContent.getText();
    }

    /** Click clear output button. */
    public void clickClearButton() {
        clearButton.click();
    }

    /** Wait output panel is cleaned. */
    public void waitOutputCleaned() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return outputContent.getText() == null || outputContent.getText().isEmpty();
            }
        });
    }

    /**
     * wait 20 sec. link in output panel with part text in link
     *
     * @param text
     */
    public void waitLinkWithParticalLinkText(final String text) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return driver().findElement(By.partialLinkText(text)).isDisplayed();
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /**
     * click on link in output panel with part text in link
     *
     * @param text
     */
    public void clickOnAppLinkWithParticalText(final String text) {
        driver().findElement(By.partialLinkText(text)).click();
    }

    /**
     * get url from link in output with specified prefix in linkname
     *
     * @param text
     * @return
     */
    public String getUrlTextText(String text) {
        return driver().findElement(By.partialLinkText(text)).getText();
    }

    /** close Output Tab */
    public void closeOutputTab() {
        closeOuputForm.click();
    }

    /**
     * Get last message from Output Panel
     *
     * @return last message or <b>null</b> if there is no any message
     */
    public String getLastMessage() {
        if (!isOpened()) {
            try {
                IDE().MENU.runCommand(MenuCommands.View.VIEW, MenuCommands.View.OUTPUT);
                waitOpened();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String lastMessage = null;
        int i = 0;
        try {
            do {
                getOutputMessage(++i);
            } while (true);
        } catch (NoSuchElementException ex) {
        }

        if (i > 1) {
            lastMessage = getOutputMessage(--i);
        }

        return lastMessage;
    }

    /** Clear opened and closed panel */
    public void clearPanel() {
        boolean wasOpened = isOpened();
        if (!wasOpened) {
            try {
                IDE().MENU.runCommand(MenuCommands.View.VIEW, MenuCommands.View.OUTPUT);
                waitOpened();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // wait until output panel finishes expanding and clear button becomes clickable
        sleepInTest(TestConstants.SHORT_WAIT_IN_SEC);

        clickClearButton();

        // close tab if it was not opened
        if (!wasOpened) {
            closeOutputTab();
        }
    }

    /**
     * Wait until some message appears in output panel.
     *
     * @param message
     * @param timeout
     * @param clazz
     * @throws RuntimeException
     *         clazz with error message from WarningDialog if this dialog is displayed.
     */
    public void waitOnMessage(final String message, int timeout, Class<? extends RuntimeException> clazz) throws RuntimeException {
        IDE().WARNING_DIALOG.waitEventOrWarningDialog(new Predicate<WebDriver>() {
            public boolean apply(WebDriver input) {
                return getAllMessagesFromOutput().contains(message);
            }
        }, timeout, clazz);
    }

    /**
     * Wait until some message appears in output panel.
     *
     * @param message
     * @param timeout
     * @param clazz
     * @throws RuntimeException
     *         clazz if there is error message in output panel, or WarningDialog is displayed
     */
    public void waitOnInfoMessage(final String infoMessage, int timeout, final Class<? extends RuntimeException> clazz)
            throws RuntimeException {
        IDE().WARNING_DIALOG.waitEventOrWarningDialog(new Predicate<WebDriver>() {
            public boolean apply(WebDriver input) {
                String allMessages = getAllMessagesFromOutput();

                if (allMessages.contains(infoMessage)) {
                    return true;
                }

                if (allMessages.contains(Messages.ERROR_MESSAGE_PREFIX)) {
                    // create instance of exception clazz and pass into its constructor the message from WarningDialog
                    RuntimeException exceptionObject = null;
                    try {
                        Constructor<? extends RuntimeException> constructor = clazz.getConstructor(String.class);
                        exceptionObject = constructor.newInstance(new Object[]{getLastErrorMessage()});
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    throw exceptionObject;
                }

                return false;
            }
        }, timeout, clazz);
    }

    public String getLastErrorMessage() {
        String errorMessageXpath = String.format(Locators.SEPARATE_MESSAGE_XPATH_TEMPLATE, Messages.ERROR_MESSAGE_PREFIX);
        List<WebElement> errorMessages = driver().findElements(By.xpath(errorMessageXpath));
        if (errorMessages == null || errorMessages.size() == 0) {
            return null;
        }

        WebElement lastErrorMessage = errorMessages.get(errorMessages.size() - 1);

        return lastErrorMessage.getText();
    }


    /**
     * Wait until some message appears in output panel during timeout = TestConstants.LONG_WAIT_IN_SEC
     *
     * @param message
     * @throws RuntimeException
     *         with error message from WarningDialog if this dialog is displayed.
     */
    public void waitOnMessage(final String message) throws RuntimeException {
        waitOnMessage(message, TestConstants.LONG_WAIT_IN_SEC, RuntimeException.class);
    }


    /**
     * Wait until some message appears in output panel during timeout = TestConstants.LONG_WAIT_IN_SEC
     *
     * @param message
     * @throws RuntimeException
     *         clazz if there is error message in output panel, or WarningDialog is displayed
     */
    public void waitOnInfoMessage(final String message) throws RuntimeException {
        waitOnInfoMessage(message, TestConstants.LONG_WAIT_IN_SEC, RuntimeException.class);
    }


    /**
     * Steps:
     * 1) wait for certain link linkUrl in Output Panel,
     * 2) click on it,
     * 3) wait until new window is opened,
     * 4) switch to it,
     * 5) verify presence of element by xpathOfMessageOnApplicationPageToCheck,
     * <p/>
     * 6) close new window,
     * 7) return to window with Codenvy
     *
     * @param linkUrl
     * @param xpathOfMessageOnApplicationPageToCheck
     *
     * @param timeout
     * @throws RuntimeException
     *         if there is no certain link in output during timeout,
     *         or link is not clickable,
     *         or new window isn't opened,
     *         or there is no element xpathOfMessageOnApplicationPageToCheck in new window
     */
    public void gotoLinkFromOutputPanelAndCheckLinkedPage(String linkUrl, final String xpathOfMessageOnApplicationPageToCheck, int timeout)
            throws RuntimeException {
        String currentWindowHandler = driver().getWindowHandle();

        try {
            clickOnOutputTab();
            waitOnMessage(linkUrl, timeout, RuntimeException.class);
            clickOnAppLinkWithParticalText(linkUrl);

            waitNewWindowOpened();
            switchToNonCurrentWindow(currentWindowHandler);

            // Waiting 'timeout' period for an element to be present on the page, checking
            // for its presence once every 10 seconds.
            Wait<WebDriver> wait = new FluentWait<WebDriver>(driver())
                    .withTimeout(timeout, TimeUnit.SECONDS)
                    .pollingEvery(10, TimeUnit.SECONDS)
                    .ignoring(NoSuchElementException.class);

            wait.until(new Function<WebDriver, WebElement>() {
                public WebElement apply(WebDriver driver) {
                    if (driver.findElement(By.xpath(xpathOfMessageOnApplicationPageToCheck)) == null) {
                        driver.navigate().refresh();
                    }

                    return driver.findElement(By.xpath(xpathOfMessageOnApplicationPageToCheck));
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (!currentWindowHandler.equals(driver().getWindowHandle())) {
                driver().close();
            }

            driver().switchTo().window(currentWindowHandler);
        }
    }
}
