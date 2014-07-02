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
package com.codenvy.ide.core.appfog;

import com.codenvy.ide.IDE;
import com.codenvy.ide.TestConstants;
import com.codenvy.ide.core.AbstractTestModule;
import com.codenvy.ide.core.exceptions.PaaSException;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/** @author Dmytro Nochevnov */
public class CreateApplicationWindow extends AbstractTestModule {

    interface Locators {
        String CREATE_APPICATION_WINDOW_ID = "ideAppfogCreateAppView-window";
        String APPLICATION_NAME_FIELD      = "ideAppfogAppViewNameField";
        String CREATE_BUTTON_ID            = "ideAppfogAppViewCreateButton";
    }

    @FindBy(id = Locators.CREATE_APPICATION_WINDOW_ID)
    WebElement createApplicationWindow;

    @FindBy(name = Locators.APPLICATION_NAME_FIELD)
    WebElement applicationNameField;

    @FindBy(id = Locators.CREATE_BUTTON_ID)
    WebElement createButton;

    CreateApplicationWindow(IDE ide) {
        super(ide);
    }

    public void create(String applicationName) {
        waitCodenvyWindowOpened(createApplicationWindow, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);

        applicationNameField.clear();
        applicationNameField.sendKeys(applicationName);

        createButton.click();

        try {
            IDE().LOADER.waitClosed();
        } catch (InterruptedException e) {
            throw new TimeoutException(e);
        }

        if (isCodenvyWindowDisplayed(createApplicationWindow)) {
            waitCodenvyWindowClosed(Locators.CREATE_APPICATION_WINDOW_ID, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);
        }
    }

}
