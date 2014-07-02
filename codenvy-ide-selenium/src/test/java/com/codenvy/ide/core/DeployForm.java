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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DeployForm extends AbstractTestModule {

    /** @param ide */
    public DeployForm(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {

        String VIEW_LOCATOR = "DeployNewProjectView-window";

        String VIEW_SAMPLES_LOCATOR = "DeploySamplesView-window";

        String CANCEL_BUTTON_ID = "ideLoadDeploymentCancelButtonId";

        String BACK_BUTTON_ID = "ideWizardDeploymentBackButtonId";

        String FINISH_BTN_ID = "ideWizardDeploymentNextButtonId";

        String SELECT_TAG = "select";

        String LIST = "//option[text()='%s']";

        String GETTEXT_FROM_PASS_FIELD = "option[value=%s]";

        String DEPOY_TO_ITEMS = "//option[@value and text()='%s']";

    }

    @FindBy(id = Locators.VIEW_LOCATOR)
    WebElement view;

    @FindBy(id = Locators.VIEW_SAMPLES_LOCATOR)
    WebElement viewSamples;

    @FindBy(id = Locators.CANCEL_BUTTON_ID)
    WebElement cancelBtn;

    @FindBy(id = Locators.BACK_BUTTON_ID)
    WebElement backBtn;

    @FindBy(id = Locators.FINISH_BTN_ID)
    WebElement finishBtn;

    @FindBy(tagName = Locators.SELECT_TAG)
    WebElement select;

    /** Wait appearance Deploy form */
    public void waitOpened() throws InterruptedException {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                return view != null && view.isDisplayed() && cancelBtn != null && cancelBtn.isDisplayed();
            }
        });
    }

    /** Wait appearance Deploy samples (form Welcome page -> Import a sample project) form */
    public void waitOpenedDeploySamples() throws InterruptedException {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                return viewSamples != null && viewSamples.isDisplayed() && cancelBtn != null && cancelBtn.isDisplayed();
            }
        });
    }

    /** Wait appearance list with specified PaaS name */
    public void waitPaasListOpened(final String paas) throws InterruptedException {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {

                try {
                    WebElement listNamePaas = driver().findElement(By.xpath(String.format(Locators.LIST, paas)));
                    return listNamePaas != null && listNamePaas.isDisplayed();
                } catch (Exception e) {
                    return false;
                }

            }
        });
    }

    /**
     * wait while item is present in 'Deploy to' list
     *
     * @param value
     */
    public void waitItemInDeployList(String value) {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.presenceOfElementLocated(By.xpath(String.format(
                Locators.DEPOY_TO_ITEMS, value))));
    }

    /** click on PaaS option form */
    public void collapsePaasList() {
        select.click();
    }

    /**
     * return true if name is selected
     *
     * @param name
     * @return
     */
    public boolean paasNameIsSelected(String name) {
        WebElement element =
                driver().findElement(By.cssSelector(String.format(Locators.GETTEXT_FROM_PASS_FIELD, name)));
        return element.isSelected();
    }

    /**
     * click on PaaS option form
     *
     * @throws InterruptedException
     */
    public void selectAndClickOnPaasInList(String selectionName) throws InterruptedException {
        //select.click();
        WebElement namePaas = driver().findElement(By.xpath(String.format(Locators.LIST, selectionName)));
        namePaas.click();
        new Actions(driver()).moveToElement(namePaas).build().perform();
        new Actions(driver()).click(namePaas).build().perform();
    }

    /** click on finish button */
    public void clickFinishBtn() {
        finishBtn.click();
    }

}
