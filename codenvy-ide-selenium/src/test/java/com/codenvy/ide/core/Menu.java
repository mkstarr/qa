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
 */

public class Menu extends AbstractTestModule {

    /** @param ide */
    public Menu(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String LOCK_LAYER_CLASS = "exo-lockLayer";

        String TOP_MENU_ITEM_LOCATOR = "//div[@class='exo-menuBar']//td[@class='exo-menuBarItem' and text()='%s']/.";

        String TOP_MENU_ITEM_DISABLED_LOCATOR = "//td[@class='exo-menuBarItemDisabled' and text()='%s']";

        String MENU_ITEM_LOCATOR = "//td[contains(@class,'exo-popupMenuTitleField')]//nobr[text()='%s']/..";

        String POPUP_SELECTOR = "//div[@class='exo-popupMenuMain']/table/tbody/tr[@item-enabled][last()]";

        String MENU_ITEM_ROW_LOCATOR = "//table[@class='exo-popupMenuTable']//tr[contains(., '%s')]";

        String ENABLED_ATTRIBUTE = "item-enabled";
    }

    @FindBy(className = Locators.LOCK_LAYER_CLASS)
    private WebElement lockLayer;

    /**
     * Run command from top menu.
     *
     * @param topMenuName
     *         name of menu
     * @param commandName
     *         command name
     */
    public void runCommand(String topMenuName, String commandName) throws InterruptedException {
        IDE().LOADER.waitClosed();
        waitCommandEnabled(topMenuName, commandName);
        //Call top menu command:
        WebElement topMenuItem =
                driver().findElement(By.xpath(String.format(Locators.TOP_MENU_ITEM_LOCATOR, topMenuName)));
        topMenuItem.click();
        waitMenuPopUp();
        //Call command from menu popup:
        WebElement menuItem = driver().findElement(By.xpath(String.format(Locators.MENU_ITEM_LOCATOR, commandName)));
        menuItem.click();
        waitMenuPopUpClosed();
        IDE().LOADER.waitClosed();
    }

    /**
     * Run command from top menu.
     *
     * @param topMenuName
     *         name of menu
     */
    public void clickOnCommand(String topMenuName) throws Exception {
        //Call top menu command:
        WebElement topMenuItem =
                driver().findElement(By.xpath(String.format(Locators.TOP_MENU_ITEM_LOCATOR, topMenuName)));
        topMenuItem.click();
        waitMenuPopUp();
    }

    /**
     * Run command from sub menu.
     *
     * @param menuName
     * @param commandName
     * @param subCommandName
     * @throws Exception
     */
    public void runCommand(String menuName, String commandName, String subCommandName) {
        waitSubCommandEnabled(menuName, commandName, subCommandName);
        //Call top menu command:
        WebElement topMenuItem =
                driver().findElement(By.xpath(String.format(Locators.TOP_MENU_ITEM_LOCATOR, menuName)));
        topMenuItem.click();
        waitMenuPopUp();

        //Call command from menu popup:
        WebElement menuItem = driver().findElement(By.xpath(String.format(Locators.MENU_ITEM_LOCATOR, commandName)));
        menuItem.click();

        //Call sub menu command
        waitForMenuItemPresent(subCommandName);


        new WebDriverWait(driver(), 5)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(Locators.MENU_ITEM_LOCATOR, subCommandName))));

        WebElement subMenuItem =
                driver().findElement(By.xpath(String.format(Locators.MENU_ITEM_LOCATOR, subCommandName)));

        subMenuItem.click();
        waitMenuPopUpClosed();
    }

    /**
     * Wait visibility state of the menu command.
     *
     * @param topMenuName
     *         mane of menu
     * @param commandName
     *         command name
     * @throws Exception
     */

    public void waitCommandVisible(final String topMenuName, final String commandName) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                WebElement topMenuItem =
                        driver().findElement(By.xpath(String.format(Locators.TOP_MENU_ITEM_LOCATOR, topMenuName)));
                topMenuItem.click();
                waitMenuPopUp();

                try {
                    return driver().findElement(By.xpath(String.format(Locators.MENU_ITEM_LOCATOR, commandName))) !=
                           null;
                } catch (NoSuchElementException e) {
                    return false;
                } finally {
                    topMenuItem.click();
                }
            }
        });
    }

    /**
     * Wait visibility state of the menu command.
     *
     * @param topMenuName
     *         mane of menu
     * @param commandName
     *         command name
     * @throws Exception
     */

    public void waitCommandInvisible(final String topMenuName, final String commandName) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                WebElement topMenuItem =
                        driver().findElement(By.xpath(String.format(Locators.TOP_MENU_ITEM_LOCATOR, topMenuName)));
                topMenuItem.click();
                waitMenuPopUp();

                try {
                    return driver().findElement(By.xpath(String.format(Locators.MENU_ITEM_LOCATOR, commandName))) !=
                           null;
                } catch (NoSuchElementException e) {
                    return true;
                } finally {
                    topMenuItem.click();
                }
            }
        });
    }

    /** Wait for menu popup to draw. */
    protected void waitMenuPopUp() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOfElementLocated(By
                                                                                                    .xpath(Locators


                                                                                                                   .POPUP_SELECTOR)));
    }

    /** Wait for menu popup to close. */
    protected void waitMenuPopUpClosed() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                      .xpath(Locators.POPUP_SELECTOR)));
    }

    /**
     * Wait for enabled state of the menu command.
     *
     * @param topMenuName
     *         top menu command name
     * @param commandName
     *         command name
     */
    public void waitCommandEnabled(final String topMenuName, final String commandName) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                WebElement topMenuItem =
                        driver().findElement(By.xpath(String.format(Locators.TOP_MENU_ITEM_LOCATOR, topMenuName)));
                topMenuItem.click();
                waitMenuPopUp();
                try {
                    WebElement command =
                            driver().findElement(By.xpath(String.format(Locators.MENU_ITEM_ROW_LOCATOR, commandName)));
                    return Boolean.parseBoolean(command.getAttribute(Locators.ENABLED_ATTRIBUTE));
                } catch (NoSuchElementException e) {
                    return false;
                } finally {
                    topMenuItem.click();
                }
            }
        });
    }

    /**
     * Wait for disabled state of the menu command.
     *
     * @param topMenuName
     *         top menu command name
     * @param commandName
     *         command name
     */
    public void waitCommandDisabled(final String topMenuName, final String commandName) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                WebElement topMenuItem =
                        driver().findElement(By.xpath(String.format(Locators.TOP_MENU_ITEM_LOCATOR, topMenuName)));
                topMenuItem.click();
                waitMenuPopUp();
                try {
                    WebElement command =
                            driver().findElement(By.xpath(String.format(Locators.MENU_ITEM_ROW_LOCATOR, commandName)));
                    return !(Boolean.parseBoolean(command.getAttribute(Locators.ENABLED_ATTRIBUTE)));
                } catch (NoSuchElementException e) {
                    return false;
                } finally {
                    topMenuItem.click();
                }
            }
        });
    }

    /**
     * Wait for disabled state of the menu sub command.
     *
     * @param menuName
     *         top menu command name
     * @param commandName
     *         command name
     */
    public void waitSubCommandDisabled(final String menuName, final String commandName, final String subCommandName) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                WebElement topMenuItem =
                        driver().findElement(By.xpath(String.format(Locators.TOP_MENU_ITEM_LOCATOR, menuName)));
                topMenuItem.click();
                waitMenuPopUp();
                //Call command from menu popup:
                WebElement menuItem =
                        driver().findElement(By.xpath(String.format(Locators.MENU_ITEM_LOCATOR, commandName)));
                menuItem.click();
                try {
                    WebElement command =
                            driver().findElement(
                                    By.xpath(String.format(Locators.MENU_ITEM_ROW_LOCATOR, subCommandName)));
                    return !(Boolean.parseBoolean(command.getAttribute(Locators.ENABLED_ATTRIBUTE)));
                } catch (NoSuchElementException e) {
                    return false;
                } finally {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    topMenuItem.click();
                }
            }
        });
    }

    /**
     * Wait for disabled state of the menu sub command.
     *
     * @param menuName
     *         top menu command name
     * @param commandName
     *         command name
     */
    public void waitSubCommandEnabled(final String menuName, final String commandName, final String subCommandName) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                WebElement topMenuItem =
                        driver().findElement(By.xpath(String.format(Locators.TOP_MENU_ITEM_LOCATOR, menuName)));
                topMenuItem.click();
                waitMenuPopUp();
                //Call command from menu popup:
                WebElement menuItem =
                        driver().findElement(By.xpath(String.format(Locators.MENU_ITEM_LOCATOR, commandName)));
                menuItem.click();
                try {
                    WebElement command =
                            driver().findElement(
                                    By.xpath(String.format(Locators.MENU_ITEM_ROW_LOCATOR, subCommandName)));
                    return (Boolean.parseBoolean(command.getAttribute(Locators.ENABLED_ATTRIBUTE)));
                } catch (NoSuchElementException e) {
                    return false;
                } finally {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    topMenuItem.click();
                }
            }
        });
    }

    /** check disabled state top menu */
    public void waitTopMenuDisabled(String topMenuName) throws Exception {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(String.format(Locators.TOP_MENU_ITEM_DISABLED_LOCATOR, topMenuName))));
    }

    /** check enabled state top menu */
    public void isTopMenuEnabled(String topMenuName) throws Exception {

        try {
            WebElement topMenuItem =
                    driver().findElement(By.xpath(String.format(Locators.TOP_MENU_ITEM_LOCATOR, topMenuName)));
        } catch (NoSuchElementException e) {

        }

    }

    /**
     * Wait for menu item to appear.
     *
     * @param itemName
     *         name of item to wait
     */
    protected void waitForMenuItemPresent(final String itemName) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return driver().findElement(By.xpath(String.format(Locators.MENU_ITEM_LOCATOR, itemName))) != null;
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /**
     * Wait for command from menu.
     *
     * @param menuName
     *         name of the top menu
     * @param itemName
     *         name of the menu item to wait
     * @throws Exception
     */
    public void waitForMenuItemPresent(String menuName, String itemName) throws Exception {
        //Call top menu command:
        WebElement topMenuItem =
                driver().findElement(By.xpath(String.format(Locators.TOP_MENU_ITEM_LOCATOR, menuName)));
        topMenuItem.click();
        waitMenuPopUp();
        try {
            waitForMenuItemPresent(itemName);
        } finally {
            topMenuItem.click();
            waitMenuPopUpClosed();
        }
    }

    /** click for onlock leayer */
    public void clickOnLockLayer() {
        lockLayer.click();
    }

    /**
     * Click on item  in 'New' menu
     *
     * @param menuName
     */
    public void clickOnNewMenuItem(String menuName) {
        WebElement menuItem = driver().findElement(By.xpath(String.format(Locators.MENU_ITEM_LOCATOR, menuName)));
        menuItem.click();
        waitMenuPopUpClosed();
    }

}