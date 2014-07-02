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

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Musienko Maxim
 *
 */
public class ProgressPanelTab extends AbstractTestModule {
    /** @param ide */
    public ProgressPanelTab(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String VIEW_LOCATOR = "//div[@view-id='ideRequestNotificationView']";

        String REMOVE_ALL_FINISHED_OPERATION =
                "//div[@view-id='ideRequestNotificationView']//div[@title='Remove All Finished Operation']";

        String GET_TEXT = "//div[@view-id='ideRequestNotificationView']//table";
    }

    @FindBy(xpath = Locators.VIEW_LOCATOR)
    private WebElement view;

    @FindBy(xpath = Locators.REMOVE_ALL_FINISHED_OPERATION)
    private WebElement removeAllFinishedOperations;

    @FindBy(xpath = Locators.GET_TEXT)
    private WebElement textArea;

    /** wait while Progress Panel will be opened */
    public void waitProgressPanelIsOpened() {
        new WebDriverWait(driver(), 60).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return view != null && view.isDisplayed() && removeAllFinishedOperations.isDisplayed();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /** wait wait progress tab area is empty */
    public void waitProgressTabAreaIsClear() {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.GET_TEXT)));

    }

    /** click for remove list with all operations */
    public void clickRemoveAllOperations() {
        removeAllFinishedOperations.click();
    }

    /** get all text from progress tab */
    public String getAllText() {
        return textArea.getText();
    }

}
