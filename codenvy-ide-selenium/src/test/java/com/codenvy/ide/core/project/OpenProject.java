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
package com.codenvy.ide.core.project;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.core.AbstractTestModule;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Evgen Vidolob
 *
 */
public class OpenProject extends AbstractTestModule {
    /** @param ide */
    public OpenProject(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private static final String VIEW_LOCATOR = "//div[@view-id='ideShowProjectsView']";

    private static final String LIST_ID = "ideProjectsListGrid";

    private static final String PROJECT_LOCATOR = "//table[@id='ideProjectsListGrid']//tbody//td/div[text()='%s']";

    @FindBy(how = How.XPATH, using = VIEW_LOCATOR)
    private WebElement view;

    @FindBy(id = LIST_ID)
    private WebElement tree;

    @FindBy(id = "ideShowProjectsOpenButton")
    private WebElement openButton;

    @FindBy(id = "ideShowProjectsCancelButton")
    private WebElement cancelButton;

    /**
     * Wait view is opened.
     *
     * @throws InterruptedException
     */
    public void waitOpened() throws InterruptedException {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(VIEW_LOCATOR)));
    }

    public void openProject(String name) throws Exception {
        waitProject(name);
        IDE().MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.OPEN_PROJECT);
        waitOpened();
        IDE().LOADER.waitClosed();
        waitProjectInOpenProjectForm(name);
        selectProjectName(name);
        clickOpenButton();
        waitClosed();
        IDE().LOADER.waitClosed();
    }

    public void waitProject(final String name) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    IDE().MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.OPEN_PROJECT);
                    waitOpened();
                    IDE().LOADER.waitClosed();
                    WebElement prj = driver().findElement(By.xpath(String.format(PROJECT_LOCATOR, name)));
                    waitProjectInOpenProjectForm(name);
                    return prj.isDisplayed() && prj != null;
                } catch (Exception e) {
                    return false;
                } finally {
                    cancelButton.click();
                }
            }
        });
    }

    public void waitProjectNotPresent(final String name) throws InterruptedException {
        IDE().MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.OPEN_PROJECT);
        waitOpened();
        IDE().LOADER.waitClosed();
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(String.format(PROJECT_LOCATOR, name))));

    }


    public void openOtherProjectIfCurentProjectNotClosed(String name) throws Exception {
        IDE().MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.OPEN_PROJECT);
        waitOpened();
        IDE().LOADER.waitClosed();
        selectProjectName(name);
        clickOpenButton();
        IDE().ASK_DIALOG.waitOpened();
        IDE().ASK_DIALOG.clickYes();
        waitClosed();
    }

    /**
     *
     */
    public void waitClosed() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return view != null;
                } catch (Exception e) {
                    return true;
                }
            }
        });

    }

    /** click open btn */
    public void clickOpenButton() {
        openButton.click();
    }

    /**
     * select project by name
     *
     * @param name
     */
    public void selectProjectName(String name) {
        tree.findElement(By.xpath("tbody//td/div[text()='" + name + "']")).click();
    }

    /** wait project in open project form */
    public void waitProjectInOpenProjectForm(final String name) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return tree.findElement(By.xpath("tbody//td/div[text()='" + name + "']")).isDisplayed();
            }
        });
    }
}
