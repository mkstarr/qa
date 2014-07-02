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

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Anna Shumilova
 *
 */
public class ContextMenu extends AbstractTestModule {
    /** @param ide */
    public ContextMenu(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String ID                    = "eXoIDEContextMenu";

        String LOCK_LAYER_CLASS      = "exo-lockLayer";

        String COMMAND_TITLE_LOCATOR = "//td[contains(@class,'exo-popupMenuTitleField')]//nobr[text()='%s']";

        String COMMAND_LOCATOR       = "//table[@class='exo-popupMenuTable']//tr[contains(., '%s')]";

        String ENABLED_ATTRIBUTE     = "item-enabled";
    }

    @FindBy(id = Locators.ID)
    private WebElement contextMenu;

    @FindBy(className = Locators.LOCK_LAYER_CLASS)
    private WebElement lockLayer;

    /** Wait context menu opened. */
    public void waitOpened() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return driver.findElement(By.id(Locators.ID)) != null
                           && driver.findElement(By.id(Locators.ID)).isDisplayed();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /** Wait context menu closed. */
    public void waitClosed() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return !lockLayer.isDisplayed() && !contextMenu.isDisplayed();
                } catch (NoSuchElementException e) {
                    return true;
                }
            }
        });
    }

    /** Wait context menu closed. */
    public void waitWhileCommandIsEnabled(final String commandName) {
        new WebDriverWait(driver(), 5).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    WebElement command =
                                         driver().findElement(By.xpath(String.format(Locators.COMMAND_LOCATOR, commandName)));
                    return Boolean.parseBoolean(command.getAttribute(Locators.ENABLED_ATTRIBUTE));
                } catch (NoSuchElementException e) {
                    return false;
                }

            }
        });
    }


    /**
     * return amount items in context menu
     * 
     * @return
     */
    public int getAmountItemsInContextMenu() {
        List<WebElement> elements = driver().findElements(By.xpath("//div[@id='eXoIDEContextMenu']//tr[@item-index]"));
        return elements.size();
    }

    /** Wait context menu closed. */
    public void waitWhileCommandIsDisabled(final String commandName) {
        new WebDriverWait(driver(), 5).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    WebElement command =
                                         driver().findElement(By.xpath(String.format(Locators.COMMAND_LOCATOR, commandName)));
                    return command.getAttribute(Locators.ENABLED_ATTRIBUTE).equals("false");
                } catch (NoSuchElementException e) {
                    return false;
                }

            }
        });
    }


    /**
     * Returns enabled state of the context menu command.
     * 
     * @param commandName command name
     * @return {@link Boolean} enabled state of the menu command
     * @throws Exception
     */
    public boolean isCommandEnabled(String commandName) throws Exception {
        try {
            WebElement command = driver().findElement(By.xpath(String.format(Locators.COMMAND_LOCATOR, commandName)));
            return Boolean.parseBoolean(command.getAttribute(Locators.ENABLED_ATTRIBUTE));
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void runCommand(String commandName) {
        WebElement command = driver().findElement(By.xpath(String.format(Locators.COMMAND_TITLE_LOCATOR, commandName)));
        command.click();
    }

    public void closeContextMenu() {
        lockLayer.click();
    }
}
