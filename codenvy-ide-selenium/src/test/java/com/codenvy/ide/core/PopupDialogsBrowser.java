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

import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PopupDialogsBrowser extends AbstractTestModule {

    /** @param ide */
    public PopupDialogsBrowser(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    /**
     * check state pop up browser window
     * 
     * @return
     */
    public boolean isAlertPresent() {
        try {
            Thread.sleep(1000);
            Alert alert = driver().switchTo().alert();
            System.out.println(alert.getText());
            if (alert.getText().isEmpty()) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /** click on accept button on pop up browser window */
    public void acceptAlert() {
        Alert alert = driver().switchTo().alert();
        alert.accept();
    }

    /** click on dismiss button on pop up browser window */
    public void dismissAlert() {
        driver().switchTo().alert().dismiss();
    }

    /** get text from Alert */
    public String getTextFromAlert() {
        return driver().switchTo().alert().getText();
    }

    /**
     * Wait pop up browser window
     * 
     * @throws Exception
     */
    public void waitOpened() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return isAlertPresent();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }


    /**
     * Wait pop up browser window is closed
     * @throws Exception
     */
    public void waitAlertClose() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    driver().switchTo().alert();
                } catch (NoAlertPresentException e) {
                    return true;
                }
                catch (Exception e) {
                    return false;
                }
                return false;
            }
        });

    }
}
