/*
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
import com.codenvy.ide.core.exceptions.PaaSException;
import com.google.common.base.Predicate;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * PageObject of progressor window.
 * @author Dmytro Nochevnov
 *
 */
public class ProgressorWindow extends AbstractTestModule {

    public ProgressorWindow(IDE ide) {
        super(ide);
    }
    
    public interface Messages {
        String CREATING_APPLICATION_MESSAGE_TEMPLATE = "Creating application %s on %s.";
        
        String LAUNCHING_ENVIRONMENT_MESSAGE_TEMPLATE = "Launching Environment %s...";
    }
    
    private interface Locators {
        String WINDOW_ID = "ide.modalJob.view-window";
        String MESSAGE_XPATH = "//*[@id='" + WINDOW_ID + "']/div/table/tbody/tr[2]/td[2]/div/div/div[2]/div/div[2]/div/div[2]/div/div[1]";
    }
    
    @FindBy(id = Locators.WINDOW_ID)
    private WebElement window; 
    
    @FindBy(xpath = Locators.MESSAGE_XPATH)
    private WebElement message;
    
    
    public void waitWindowOpened() {       
        try {
            IDE().LOADER.waitClosed();
        } catch (InterruptedException e) {
            throw new TimeoutException(e);
        }
        
        IDE().WARNING_DIALOG.waitEventOrWarningDialog(new Predicate<WebDriver>() {
            public boolean apply(WebDriver input) {
                return isOpened();
            }
        }, TestConstants.ANIMATION_PERIOD, RuntimeException.class);
    }
    
    public void waitWindowClosed(int timeoutInSec) throws PaaSException {                
        IDE().WARNING_DIALOG.waitEventOrWarningDialog(new Predicate<WebDriver>() {
            public boolean apply(WebDriver input) {
                return ! isOpened();
            }
        }, timeoutInSec, RuntimeException.class);
    }

    public void waitForMessage(final String expectedMessage) {       
        IDE().WARNING_DIALOG.waitEventOrWarningDialog(new Predicate<WebDriver>() {
            public boolean apply(WebDriver input) {
                String message = getMessage();
                return message != null && message.contains(expectedMessage);
            }
        }, TestConstants.ANIMATION_PERIOD, RuntimeException.class);
    }

    public boolean isOpened() {
        try {  
            return driver().findElement(By.id(Locators.WINDOW_ID)) != null;
        } catch(NoSuchElementException e) {
            return false;
        }
    }
    
    public String getMessage() {
        if (isOpened()) {
            return message.getText().trim();                
        }        
        
        return null;
    }
}
