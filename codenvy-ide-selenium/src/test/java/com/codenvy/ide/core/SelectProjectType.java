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

import com.codenvy.ide.IDE;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Musienko Maxim */
public class SelectProjectType extends AbstractTestModule {
    public SelectProjectType(IDE ide) {
        super(ide);
    }

    public interface ProjectTypes {
        String PHP               = "PHP";
        String WAR               = "War";
        String JAVASCRIPT        = "JavaScript";
        String PYTHON            = "Python";
        String RAILS             = "Rails";
        String SPRING            = "Spring";
        String MAVEN_MULTIMODULE = "Maven Multi-module";
        String DEFAULT           = "default";
        String NODE_JS           = "nodejs";
    }

    private interface Locators {
        String VIEW       = "ideProjectPrepareView-window";
        String OK_BTN     = "//div[@id='ideProjectPrepareView-window']//td[text()='Ok']";
        String CANCEL_BTN = "//div[@id='ideProjectPrepareView-window']//td[text()='Cancel']";
    }

    @FindBy(id = Locators.VIEW)
    private WebElement view;

    @FindBy(xpath = Locators.OK_BTN)
    private WebElement okBtn;

    @FindBy(xpath = Locators.CANCEL_BTN)
    private WebElement cancelBtn;


    public void waitSelectProjectTypeForm() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOf(view));
    }

    public void waitSelectProjectTypeFormDisAppear() {

        new WebDriverWait(driver(), 10).until(ExpectedConditions.invisibilityOfElementLocated(By.id(Locators.VIEW)));
    }

    public void selectProjectTypeForm(String type) {
        WebElement selectDropdown = view.findElement(By.tagName("select"));
        Select select = new Select(selectDropdown);
        select.selectByVisibleText(type);
    }

    public void clickOkBtn() {
        okBtn.click();
    }

    public void clickCancelBtn() {
        cancelBtn.click();
    }

    public void selectPrjAndConfirm(String nameProj) {
        selectProjectTypeForm(nameProj);
        clickOkBtn();
        waitSelectProjectTypeFormDisAppear();
    }

}
