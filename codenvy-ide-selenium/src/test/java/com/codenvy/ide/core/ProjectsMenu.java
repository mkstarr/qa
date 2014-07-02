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
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProjectsMenu extends AbstractTestModule {
    /** @param ide */
    public ProjectsMenu(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {

        String VIEW_LOCATOR = "CreateNewProjectView-window";

        String CANCEL_BUTTON = "ideCreateNewProjectCancelButton";

        String NEXT_BUTTON = "ideCreateNewProjectNextButton";

        String NAME_PROJECT_SELECT =
                "//table[@id='ideCreateFileFromTemplateFormTemplateListGrid']//td/div/span[text()='%s']";

    }


    private class Top {
        public void gg(String cls) {
            String vv;
            vv = cls;

        }


    }


    @FindBy(id = Locators.VIEW_LOCATOR)
    WebElement view;

    @FindBy(id = Locators.CANCEL_BUTTON)
    WebElement cancel;

    @FindBy(id = Locators.NEXT_BUTTON)
    WebElement next;

    /** Wait appearance Projects form */
    public void waitOpened() throws InterruptedException {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                return view != null && view.isDisplayed() && cancel != null && cancel.isDisplayed();
            }
        });
    }

    /** Wait appearance Projects form */
    public void waitForProjectAppear(final String namePrj) throws InterruptedException {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    WebElement prj =
                            driver().findElement(By.xpath(String.format(Locators.NAME_PROJECT_SELECT, namePrj)));
                    return prj.isDisplayed() && prj != null;
                } catch (Exception e) {
                    return false;
                }

            }
        });
    }

    /**
     * select project with specified name
     *
     * @throws InterruptedException
     */
    public void selectPoject(String name) throws InterruptedException {
        WebElement elem = driver().findElement(By.xpath(String.format(Locators.NAME_PROJECT_SELECT, name)));
        elem.click();
    }

    /** click on cancel button */
    public void cancelBtnClick() {
        cancel.click();
    }

    /** click on next button */
    public void nextBtnClick() {
        next.click();
    }

    /**
     * type of the name project
     *
     * @throws InterruptedException
     */
    public void typeNameOfProject(String txt) throws InterruptedException {
        WebElement elem = view.findElement(By.tagName("input"));
        IDE().INPUT.typeToElement(elem, txt, true);
    }

}
