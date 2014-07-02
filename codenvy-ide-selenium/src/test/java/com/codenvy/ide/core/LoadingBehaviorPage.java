/*
 *
 * CODENVY CONFIDENTIAL
 * ________________
 *
 * [2012] - [2014] Codenvy, S.A.
 * All Rights Reserved.
 * NOTICE: All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any. The intellectual and technical concepts contained
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
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Musienko Maxim */
public class LoadingBehaviorPage extends AbstractTestModule {

    public LoadingBehaviorPage(IDE ide) {
        super(ide);
    }

    private interface Locators {
        String IDE_PRELOADER_ID = "initializer";
    }

    @FindBy(id = Locators.IDE_PRELOADER_ID)
    WebElement preloadPage;

    /** wait disappearing Load Behavior Page */
    public void waitLoadPageIsClosed() {
        new WebDriverWait(driver(), 160).until(new ExpectedCondition<Boolean>() {
            @Override

            public Boolean apply(WebDriver input) {
                try {
                    driver().findElement(By.id(Locators.IDE_PRELOADER_ID));
                    return false;
                } catch (NoSuchElementException e) {
                    return true;
                }
            }

        });
    }


    public void waitLoadPageIsOpen() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.id(Locators.IDE_PRELOADER_ID)));
    }

}
