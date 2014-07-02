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
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by The eXo Platform SAS .
 *
 * @author Vitaliy Gulyy
 */
public class Toolbar extends AbstractTestModule {

    /** @param ide */
    public Toolbar(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String TOOLBAR_ID = "exoIDEToolbar";

        String BUTTON_SELECTOR = "div[title='%s']";

        String POPUP_PANEL_LOCATOR = "//table[@class='exo-popupMenuTable']";

        String ROW_FROM_NEW_POPUP_LOCATOR = POPUP_PANEL_LOCATOR + "//tr[contains(., '%s')]";

        String BUTTON_FROM_NEW_POPUP_LOCATOR = ROW_FROM_NEW_POPUP_LOCATOR + "//nobr";

        String LOCKLAYER_CLASS = "exo-lockLayer";

        String POPUP_SELECTOR = "table.exo-popupMenuTable";

        String SELECTED_BUTTON_SELECTOR = "div#" + TOOLBAR_ID + " div.exoIconButtonPanelSelected[title='%s']";

        String RIGHT_SIDE_BUTTON_SELECTOR = "div#" + TOOLBAR_ID + " div[title='%s']";

        String LEFT_SIDE_BUTTON_SELECTOR = "div#" + TOOLBAR_ID + " div[title='%s']";
    }

    @FindBy(className = Locators.LOCKLAYER_CLASS)
    WebElement lockLayer;

    @FindBy(id = Locators.TOOLBAR_ID)
    WebElement toolbar;

    /**
     * Performs click on toolbar button.
     *
     * @param buttonTitle
     *         button's title
     */
    public void runCommand(String buttonTitle) throws Exception {
        waitForButtonEnabled(buttonTitle);
        WebElement button = toolbar.findElement(By.cssSelector(String.format(Locators.BUTTON_SELECTOR, buttonTitle)));
        button.click();
        IDE().LOADER.waitClosed();
    }

    /**
     * Clicks on New button on toolbar and then clicks on menuName from list
     *
     * @param commandName
     *         command's name from New popup
     */
    public void runCommandFromNewPopupMenu(final String commandName) throws Exception {
        waitButtonFromNewPopupMenuEnabled(commandName);
        WebElement toolBarbutton =
                toolbar.findElement(By.cssSelector(String.format(Locators.BUTTON_SELECTOR, MenuCommands.New.NEW)));
        toolBarbutton.click();
        waitMenuPopUp();
        try {
            WebElement button =
                    driver().findElement(By.xpath(String.format(Locators.BUTTON_FROM_NEW_POPUP_LOCATOR, commandName)));
            button.click();
        } finally {
            try {
                if (lockLayer != null && lockLayer.isDisplayed()) {
                    lockLayer.click();
                }
            } catch (NoSuchElementException e) {
            }
        }
    }

    /** Wait for popup to draw. */
    protected void waitMenuPopUp() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return driver.findElement(By.cssSelector(Locators.POPUP_SELECTOR)) != null;
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /**
     * Returns the enabled state of the Toolbar button.
     *
     * @param name
     *         button's title
     * @return enabled state of the button
     */
    public boolean isButtonEnabled(String name) {
        try {
            WebElement button = driver().findElement(By.cssSelector(String.format(Locators.BUTTON_SELECTOR, name)));
            return Boolean.parseBoolean(button.getAttribute("enabled"));
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Returns the enabled state of button from new popup.
     *
     * @param name
     *         button's name
     * @throws Exception
     */
    public void waitButtonFromNewPopupMenuEnabled(final String name) throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {

                try {
                    WebElement toolBarbutton =
                            toolbar.findElement(
                                    By.cssSelector(String.format(Locators.BUTTON_SELECTOR, MenuCommands.New.NEW)));
                    toolBarbutton.click();
                    WebElement button =
                            driver().findElement(By.xpath(String.format(Locators.ROW_FROM_NEW_POPUP_LOCATOR, name)));
                    return Boolean.parseBoolean(button.getAttribute("item-enabled"));
                } catch (NoSuchElementException e) {
                    return false;
                } catch (Exception e) {
                    return false;
                } finally {
                    if (lockLayer != null) {
                        lockLayer.click();
                    }
                }
            }
        });
    }

    /**
     * Returns the disabled state of button from new popup.
     *
     * @param name
     *         button's name
     * @throws Exception
     */
    public void waitButtonFromNewPopupMenuDisabled(final String name) throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {

                try {
                    WebElement toolBarbutton =
                            toolbar.findElement(
                                    By.cssSelector(String.format(Locators.BUTTON_SELECTOR, MenuCommands.New.NEW)));
                    toolBarbutton.click();
                    WebElement button =
                            driver().findElement(By.xpath(String.format(Locators.ROW_FROM_NEW_POPUP_LOCATOR, name)));
                    return !(Boolean.parseBoolean(button.getAttribute("item-enabled")));
                } catch (NoSuchElementException e) {
                    return false;
                } catch (Exception e) {
                    return false;
                } finally {
                    if (lockLayer != null) {
                        lockLayer.click();
                    }
                }
            }
        });
    }

    /**
     * Wait for button change the enabled state.
     *
     * @param name
     *         button's name
     * @throws Exception
     */
    public void waitForButtonEnabled(final String name) throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                return isButtonEnabled(name);
            }
        });
    }

    /**
     * Wait for button change the disbled state.
     *
     * @param name
     *         button's name
     * @throws Exception
     */
    public void waitForButtonDisabled(final String name) throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                return !(isButtonEnabled(name));
            }
        });
    }

    /**
     * Check is button present on right part of Toolbar.
     *
     * @param name
     *         button name (title in DOM)
     */
    public boolean isButtonPresentAtRight(String name) {
        try {
            return driver().findElement(By.cssSelector(String.format(Locators.RIGHT_SIDE_BUTTON_SELECTOR, name))) !=
                   null;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void waitButtonNotPresentAtRight(final String name) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                return !isButtonPresentAtRight(name);
            }
        });
    }

    public void waitButtonPresentAtRight(final String name) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                return isButtonPresentAtRight(name);
            }
        });
    }

    /**
     * Check is button present on left part of  Toolbar.
     *
     * @param name
     *         button name (title in DOM)
     */
    public boolean isButtonPresentAtLeft(String name) {
        try {
            WebElement button =
                    driver().findElement(By.cssSelector(String.format(Locators.LEFT_SIDE_BUTTON_SELECTOR, name)));
            return button != null && button.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void waitButtonNotPresentAtLeft(final String name) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                return !isButtonPresentAtLeft(name);
            }
        });
    }

    public void waitButtonPresentAtLeft(final String name) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                return isButtonPresentAtLeft(name);
            }
        });
    }

    /**
     * Get the button's selected state.
     *
     * @param name
     *         button's name
     * @return if <code>true</code>, then button is selected
     */
    public boolean isButtonSelected(String name) {
        try {
            WebElement button =
                    driver().findElement(By.cssSelector(String.format(Locators.SELECTED_BUTTON_SELECTOR, name)));
            return button != null && button.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
