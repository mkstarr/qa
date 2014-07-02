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

import com.codenvy.ide.IDE;
import com.codenvy.ide.TestConstants;
import com.google.common.base.Predicate;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;

/**
 * Created by The eXo Platform SAS .
 *
 * @author Vitaliy Gulyy
 * @author Dmytro Nochevnov
 *
 */

public abstract class AbstractTestModule {
    private com.codenvy.ide.IDE ide;

    public AbstractTestModule(IDE ide) {
        this.ide = ide;
    }

    public WebDriver driver() {
        return ide.driver();
    }

    public IDE IDE() {
        return ide;
    }


    /**
     * Sleep during timeout, in sec"
     * @param timeoutInSec
     */
    public void sleepInTest(int timeoutInSec) {
        try {
            Thread.sleep(timeoutInSec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Common methods to work with browser window
     */    
    
    /** @param currentWindowHandler */
    public void switchToNonCurrentWindow(String currentWindowHandler) {
        for (String handle : driver().getWindowHandles()) {
            if (! currentWindowHandler.equals(handle)) {
                driver().switchTo().window(handle);
                break;
            }
        }
    }
    
    /**
     * Wait while browser will open new window.
     */
    public void waitNewWindowOpened() {
        new WebDriverWait(driver(), TestConstants.ANIMATION_PERIOD).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                Set<String> driverWindows = driver().getWindowHandles();
                return (driverWindows.size() > 1) ? (true) : (false);
            }
        });
    }
    
    
    
    /**
     * Common methods to work with Codenvy window
     */
    
    /**
     * Wait until Codenvy open window. 
     * @param window
     * @param timeout
     * @param clazz
     * @throws RuntimeException clazz with error message from WarningDialog if this dialog is displayed.
     */
    public void waitCodenvyWindowOpened(final WebElement window, int timeout, Class<? extends RuntimeException> clazz) throws RuntimeException {
        try {
            IDE().LOADER.waitClosed();
        } catch (InterruptedException e) {
            throw new TimeoutException(e);
        }

        IDE().WARNING_DIALOG.waitEventOrWarningDialog(new Predicate<WebDriver>() {
            public boolean apply(WebDriver input) {
                return ExpectedConditions.visibilityOf(window).apply(driver()) != null;
            }
        }, timeout, clazz);
    }    
    
    /**
     * Wait until Codenvy closes window. 
     * @param windowId
     * @param timeout
     * @param clazz
     * @throws RuntimeException clazz with error message from WarningDialog if this dialog is displayed.
     */
    public void waitCodenvyWindowClosed(final String windowId, int timeout, Class<? extends RuntimeException> clazz) throws RuntimeException {
        IDE().WARNING_DIALOG.waitEventOrWarningDialog(new Predicate<WebDriver>() {
            public boolean apply(WebDriver input) {
                return ExpectedConditions.invisibilityOfElementLocated(By.id(windowId)).apply(driver()).booleanValue();
            }
        }, timeout, clazz);
    }

    public boolean isCodenvyWindowDisplayed(WebElement window) {
        try {
            return window != null && window.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
