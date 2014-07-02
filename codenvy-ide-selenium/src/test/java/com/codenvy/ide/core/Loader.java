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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Anna Shumilova
 *
 */
public class Loader extends AbstractTestModule {
    /** @param ide */
    public Loader(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private final String LOADER_ID = "GWTLoaderId";

    @FindBy(id = LOADER_ID)
    private WebElement loader;

    /**
     * Waits for Loader to appear.
     *
     * @throws Exception
     */
    public void waitOpened() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    Thread.sleep(1000);
                    return loader != null && loader.isDisplayed();
                } catch (NoSuchElementException e) {
                    return false;
                } catch (InterruptedException e) {
                    return false;
                }
            }
        });
    }

    /**
     * Waits for Loader to hide.
     *
     * @throws Exception
     */
    public void waitClosed() throws InterruptedException {
        Thread.sleep(1000);
        new WebDriverWait(driver(), 160).until(ExpectedConditions.invisibilityOfElementLocated(By.id(LOADER_ID)));
    }
}
