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

/** @author Artem Zatsarynnyy */
public class ShowKeyboardShortcuts extends AbstractTestModule {
    /** @param ide */
    public ShowKeyboardShortcuts(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {

        String ENABLED_BUTTON_PREFICS = "[button-enabled=true]";

        String SHOW_KEYBOARD_SHORCUTS_VIEW = "ideShowHotKeysView-window";

        String CLOSE_BUTTON_ID = "ideShowHotKeysViewCloseButton";

        String IS_CANCEL_ENABLED_SELECTOR = "div#" + CLOSE_BUTTON_ID + ENABLED_BUTTON_PREFICS;

        String LIST_GRID_FORM = "ideShowHotKeysListGrid";

        String ROW_SELECTOR = "//table[@id='" + LIST_GRID_FORM + "']" + "/tbody//tr/td/div[text()='%s']";

        String CLOSE_TITLE = "img[title=Close]";

    }

    @FindBy(id = Locators.SHOW_KEYBOARD_SHORCUTS_VIEW)
    private WebElement showKeyboardShortcutsForm;

    @FindBy(id = Locators.CLOSE_BUTTON_ID)
    private WebElement closeButton;

    @FindBy(css = Locators.IS_CANCEL_ENABLED_SELECTOR)
    private WebElement isCancelEnabled;

    @FindBy(css = Locators.CLOSE_TITLE)
    private WebElement closeTitle;

    /** Wait appearance Show Keyboard Shortcuts view */
    public void waitOpened() throws InterruptedException {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                return showKeyboardShortcutsForm != null && showKeyboardShortcutsForm.isDisplayed();
            }
        });
    }

    /**
     * Wait disappearance Customize Hotkeys Form
     *
     * @throws InterruptedException
     */
    public void waitClosed() throws InterruptedException {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    input.findElement(By.id(Locators.SHOW_KEYBOARD_SHORCUTS_VIEW));
                    return false;
                } catch (Exception e) {
                    return true;
                }
            }
        });
    }

    /** @return true if close button enabled */
    public boolean isCloseButtonEnabled() {
        return isCancelEnabled != null && isCancelEnabled.isDisplayed();
    }

    /** Click close button. */
    public void closeButtonClick() throws InterruptedException {
        closeButton.click();
        waitClosed();
    }

    /**
     * Wait for shortcut is present in list grid.
     *
     * @param shortcut
     *         keyboard shortcut
     */
    public void waitShortcutIsPresent(String shortcut) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(String.format(Locators.ROW_SELECTOR, shortcut))));
    }

    /**
     * Wait for shortcut is not present in list grid.
     *
     * @param shortcut
     *         keyboard shortcut
     */
    public void waitShortcutIsNotPresent(String shortcut) {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(String.format(Locators.ROW_SELECTOR, shortcut))));
    }

    /** Close form using the method of click on close label in form */
    public void closeClick() {
        closeTitle.click();
    }

}
