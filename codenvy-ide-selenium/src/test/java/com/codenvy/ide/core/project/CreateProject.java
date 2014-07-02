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

import com.codenvy.ide.core.AbstractTestModule;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Ann Zhuleva */
public class CreateProject extends AbstractTestModule {
    /** @param ide */
    public CreateProject(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String VIEW_ID = "org.exoplatform.ide.client.project.create.CreateProjectForm";

        String VIEW_LOCATOR = "//div[@view-id='" + VIEW_ID + "']";

        String PROJECT_NAME_ID = "CreateProjectFormProjectName";

        String PROJECT_TYPE_ID = "CreateProjectFormProjectType";

        String CREATE_BUTTON_ID = "CreateProjectFormCreateButton";

        String CANCEL_BUTTON_ID = "CreateProjectFormCancelButton";

    }

    @FindBy(how = How.XPATH, using = Locators.VIEW_LOCATOR)
    private WebElement view;

    @FindBy(name = Locators.PROJECT_NAME_ID)
    private WebElement projectNameField;

    @FindBy(name = Locators.PROJECT_TYPE_ID)
    private WebElement projectTypeField;

    @FindBy(id = Locators.CREATE_BUTTON_ID)
    private WebElement createButton;

    @FindBy(id = Locators.CANCEL_BUTTON_ID)
    private WebElement cancelButton;

    /**
     * Wait view is opened.
     *
     * @throws InterruptedException
     */
    public void waitOpened() throws InterruptedException {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return view != null && view.isDisplayed();
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /**
     * Wait view is closed.
     *
     * @throws InterruptedException
     */
    public void waitClosed() throws InterruptedException {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    input.findElement(By.xpath(Locators.VIEW_LOCATOR));
                    return false;
                } catch (Exception e) {
                    return true;
                }
            }
        });
    }

    /** Click create button. */
    public void clickCreateButton() {
        createButton.click();
    }

    /** Click cancel button. */
    public void clickCancelButton() {
        cancelButton.click();
    }

    /**
     * Set project's name (type to input).
     *
     * @param name
     *         project's name
     * @throws InterruptedException
     */
    public void setProjectName(String name) throws InterruptedException {
        IDE().INPUT.typeToElement(projectNameField, name, true);
    }

    /**
     * Create default project with pointed name.
     *
     * @param name
     *         project's name
     * @throws Exception
     */
    public void createProject(String name) throws Exception {
        //TODO not working yet
        //      IDE().MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.NEW,
        // MenuCommands.Project.CREATE_PROJECT);
        //      waitOpened();
        //      setProjectName(name);
        //      clickCreateButton();
        //      waitClosed();

    }
}
