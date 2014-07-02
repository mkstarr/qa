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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Operations with form for selection and changing current workspace.
 *
 * @author Musienko Maxim
 */
public class SelectWorkspace extends AbstractTestModule {

    /** @param ide */
    public SelectWorkspace(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String VIEW = "//h2[contains(.,'Select')]";

        String WORKSPACE_LOCATOR = "//li[text()='%s']";
    }

    @FindBy(xpath = Locators.VIEW)
    private WebElement selectWorkspacePage;

    /** wait for opening select workspace page */
    public void waitSelectWorkspacesPageOpened() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.VIEW)));
    }

    public boolean isSelectWorkspacePageOpened() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            return selectWorkspacePage.isDisplayed() && selectWorkspacePage != null;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            System.out.println("Element not found!");
            return false;
        }
    }

    /** wait for worksapce in select workspace page */
    public void waitWorkspaceInSelectWorkspacePage(String workspaceName) {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                Locators.WORKSPACE_LOCATOR, workspaceName))));
    }

    /**
     * wait tenant container and select workspace on workspace page
     *
     * @param workspaceName
     */
    public void clickOnWorkspaceName(String workspaceName) throws InterruptedException {
        //TODO may be we can find workarounf for tenant animation
        new WebDriverWait(driver(), 5).until(ExpectedConditions.visibilityOfElementLocated(By.id("select-ws")));
        new WebDriverWait(driver(), 5).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return driver().findElement(By.cssSelector("div.select-ws")).getCssValue("left").contains("px");
            }
        });
        WebElement element = driver().findElement(By.xpath(String.format(
                Locators.WORKSPACE_LOCATOR, workspaceName)));
        element.click();
    }

}
