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
public class DeleteApplicationWindow extends AbstractTestModule {

    interface Locators {
        String DELETE_APPLICATION_WINDOW_ID  = "ideDeleteApplicationView-window";
        String DELETE_SERVICE_CHECKBOX_XPATH = "//div[@id='ideDeleteApplicationView-window']/..//input[@type='checkbox']";
        String DELETE_BUTTON_ID              = "ideDeleteApplicationViewRenameButton";
    }

    @FindBy(id = Locators.DELETE_APPLICATION_WINDOW_ID)
    WebElement deleteApplicationWindow;

    @FindBy(xpath = Locators.DELETE_SERVICE_CHECKBOX_XPATH)
    WebElement deleteServiceCheckbox;

    @FindBy(id = Locators.DELETE_BUTTON_ID)
    WebElement deleteButton;

    DeleteApplicationWindow(IDE ide) {
        super(ide);
    }

    public void deleteApplication(boolean deleteBoundServices) {
        waitCodenvyWindowOpened(deleteApplicationWindow, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);

        // invert deleteServiceCheckbox state if (deleteServices flag != deleteServiceCheckbox state) 
        if (deleteBoundServices != deleteServiceCheckbox.isSelected()) {
            deleteServiceCheckbox.click();
        }

        deleteButton.click();

        try {
            IDE().LOADER.waitClosed();
        } catch (InterruptedException e) {
            throw new TimeoutException(e);
        }

        if (isCodenvyWindowDisplayed(deleteApplicationWindow)) {
            waitCodenvyWindowClosed(Locators.DELETE_APPLICATION_WINDOW_ID, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);
        }
    }

}