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
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Anna Shumilova
 *
 */
public class DeployNodeType extends AbstractTestModule {
    /** @param ide */
    public DeployNodeType(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private final class Locators {
        static final String VIEW_ID = "ideDeployNodeTypeView";

        static final String VIEW_LOCATOR = "//div[@view-id='" + VIEW_ID + "']";

        static final String CANCEL_BUTTON_ID = "ideDeployNodeTypeViewCancelButton";

        static final String DEPLOY_BUTTON_ID = "ideDeployNodeTypeViewDeployButton";

        static final String FORMAT_FIELD_ID = "ideDeployNodeTypeViewFormatField";

        static final String ALREADY_EXIST_BEHAVIOR_FIELD_ID = "ideDeployNodeTypeViewAlreadyExistBehaviorField";

    }

    @FindBy(xpath = Locators.VIEW_LOCATOR)
    private WebElement view;

    @FindBy(id = Locators.DEPLOY_BUTTON_ID)
    private WebElement deployButton;

    @FindBy(id = Locators.CANCEL_BUTTON_ID)
    private WebElement cancelButton;

    @FindBy(name = Locators.FORMAT_FIELD_ID)
    private WebElement formatField;

    @FindBy(name = Locators.ALREADY_EXIST_BEHAVIOR_FIELD_ID)
    private WebElement alreadyExistsField;

    /**
     * Wait Deploy node type view opened.
     *
     * @throws Exception
     */
    public void waitOpened() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return isOpened();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /**
     * Wait Deploy node type view closed.
     *
     * @throws Exception
     */
    public void waitClosed() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    input.findElement(By.xpath(Locators.VIEW_LOCATOR));
                    return false;
                } catch (NoSuchElementException e) {
                    return true;
                }
            }
        });
    }

    /**
     * Returns the opened state of the view and it's elements.
     *
     * @return {@link Boolean} opened state
     */
    public boolean isOpened() {
        return (view != null && view.isDisplayed() && deployButton != null && deployButton.isDisplayed()
                && cancelButton != null && cancelButton.isDisplayed() && formatField != null &&
                formatField.isDisplayed()
                && alreadyExistsField != null && alreadyExistsField.isDisplayed());
    }

    /** Click deploy button. */
    public void clickDeployButton() {
        deployButton.click();
    }

    /** Click cancel button. */
    public void clickCancelButton() {
        cancelButton.click();
    }

    /**
     * Select the value of the Node type format field by visible text.
     *
     * @param format
     */
    public void selectNodeTypeFormat(String format) {
        new Select(formatField).selectByVisibleText(format);
    }

    /**
     * Select the value of the Already exists behavior field by visible text.
     *
     * @param behavior
     */
    public void selectAlreadyExists(String behavior) {
        new Select(alreadyExistsField).selectByVisibleText(behavior);
    }
}
