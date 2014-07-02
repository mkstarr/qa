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

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;

/**
 * Created by The eXo Platform SAS .
 *
 * @author Vitaliy Gulyy
 */


public class Perspective extends AbstractTestModule {

    /** @param ide */
    public Perspective(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    public interface Panel {
        String NAVIGATION = "navigation";

        String EDITOR = "editor";

        String INFORMATION = "information";

        String OPERATION = "operation";
    }

    private interface Locators {
        String CLOSE_BUTTON_SELECTOR = "div.tabTitleCloseButton[tab-title='%s']";

        String PANEL_MAXIMIZED_ATTRIBUTE = "panel-maximized";

        String PANEL_LOCATOR = "//div[id='%s']";

        String VIEW_LOCATOR = "//div[@view-id='%s']";

        String EXPLORE_TABS_LOCATOR =
                "//div[@class='gwt-TabLayoutPanelTabs']//td[@class='tabTitleText' and text()='%s']";

        String ACTIVE_VIEW_ATTRIBUTE = "is-active";

        String RESORE_BUTTON_ID = "%s-restore";

        String MAXIMIZE_BUTTON_ID = "%s-maximize";
    }

    /**
     * Maximize panel by click on maximize button.
     *
     * @param panelId
     *         panel's id
     * @throws Exception
     */
    public void maximizePanel(String panelId) throws Exception {
        WebElement maximizeButton = driver().findElement(By.id(String.format(Locators.MAXIMIZE_BUTTON_ID, panelId)));
        waitMaximizeButtonAppear(panelId);
        maximizeButton.click();
        waitMaximized(panelId);
    }

    /**
     * Wait panel appearance maximize button.
     *
     * @param panelId
     *         panel's id
     */
    private void waitMaximizeButtonAppear(final String panelId) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                WebElement maxButton = driver().findElement(By.id(String.format(Locators.MAXIMIZE_BUTTON_ID, panelId)));
                return maxButton != null && maxButton.isDisplayed();
            }
        });
    }

    /**
     * Restore panel's size by clicking restore button.
     *
     * @param panelId
     *         panel's id
     * @throws Exception
     */
    public void restorePanel(String panelId) throws Exception {
        WebElement restoreButton = driver().findElement(By.id(String.format(Locators.RESORE_BUTTON_ID, panelId)));

        restoreButton.click();
        waitRestored(panelId);
    }

    /**
     * Wait panel is maximized.
     *
     * @param panelId
     *         panel's id
     */
    public void waitMaximized(final String panelId) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                // need for
                try {
                    Thread.sleep(TestConstants.PAGE_LOAD_PERIOD);
                } catch (InterruptedException e) {
                }
                return isPanelMaximized(panelId);
            }
        });
    }

    /**
     * Wait panel is restored.
     *
     * @param panelId
     *         panel's id
     */
    public void waitRestored(final String panelId) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return !isPanelMaximized(panelId);
            }
        });
    }

    /**
     * Returns whether panel is maximized.
     *
     * @param panelId
     *         panel's id
     * @return {@link Boolean} <code>true</code> if panel is maximized
     */
    public boolean isPanelMaximized(String panelId) {
        WebElement panel = driver().findElement(By.id(panelId));
        String attribute = panel.getAttribute(Locators.PANEL_MAXIMIZED_ATTRIBUTE);
        return panel.isDisplayed() && attribute != null && Boolean.parseBoolean(attribute);
    }

    /**
     * Returns the active state of the view.
     *
     * @param view
     *         view
     * @return boolean view's active state
     */
    public boolean isViewActive(WebElement view) {
        return (view != null) ? Boolean.parseBoolean(view.getAttribute(Locators.ACTIVE_VIEW_ATTRIBUTE)) : false;
    }

    /**
     * Forms view locator by its id.
     *
     * @param viewId
     * @return {@link String} view's locator
     */
    public String getViewLocator(String viewId) {
        return String.format(Locators.VIEW_LOCATOR, viewId);
    }

    /**
     * Get close button of the view.
     *
     * @param viewTitle
     *         view's title
     * @return {@link WebElement} close button
     */
    public WebElement getCloseViewButton(String viewTitle) {
        return driver().findElement(By.cssSelector(String.format(Locators.CLOSE_BUTTON_SELECTOR, viewTitle)));
    }

    /**
     * select tabs between project explorer and other tabs for example: between search and project panel
     *
     * @param tabName
     */
    public void selectTabsOnExplorer(String tabName) {
        WebElement tab = driver().findElement(By.xpath(String.format(Locators.EXPLORE_TABS_LOCATOR, tabName)));
        new WebDriverWait(driver(), 5).until(ExpectedConditions.visibilityOf(tab));
        tab.click();
    }


    /**
     * wait while on tab of the project explorer appearance specified name
     *
     * @param tabName
     */
    public void waitTabsWithSpecifiedNameOnExplorer(String tabName) {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(Locators.EXPLORE_TABS_LOCATOR,
                                                                                            tabName))));
    }


    /**
     * send F11 for full maximize browser window
     *
     * @throws InterruptedException
     */
    public void fullMaximizeBrowser() throws InterruptedException {
        new Actions(driver()).sendKeys(Keys.F11).build().perform();
    }

    /**
     * Wait while will open more 1 window. This method uses for check opening the window with debug demo project.
     * Because
     * demo project opened In a separate window
     *
     * @throws Exception
     */
    public void waitOpenedSomeWin() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                Set<String> driverWindows = driver().getWindowHandles();
                return (driverWindows.size() > 1) ? (true) : (false);
            }
        });
    }

}
