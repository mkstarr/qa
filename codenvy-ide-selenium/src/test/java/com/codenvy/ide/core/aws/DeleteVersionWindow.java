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
package com.codenvy.ide.core.aws;

import com.codenvy.ide.IDE;
import com.codenvy.ide.TestConstants;
import com.codenvy.ide.core.AbstractTestModule;
import com.codenvy.ide.core.exceptions.PaaSException;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/** @author Dmytro Nochevnov */
public class DeleteVersionWindow extends AbstractTestModule {

    interface Locators {
        String DELETE_VERSION_WINDOW_ID        = "ideDeleteVersionView-window";
        String DELETE_S3_BUNDLE_CHECKBOX_XPATH = "//div[@id='ideDeleteVersionView-window']/..//input[@type='checkbox']";
        String DELETE_BUTTON_ID                = "ideDeleteVersionViewDeleteButton";
    }

    @FindBy(id = Locators.DELETE_VERSION_WINDOW_ID)
    WebElement deleteVersionWindow;

    @FindBy(xpath = Locators.DELETE_S3_BUNDLE_CHECKBOX_XPATH)
    WebElement deleteS3BundleCheckbox;

    @FindBy(id = Locators.DELETE_BUTTON_ID)
    WebElement deleteButton;

    DeleteVersionWindow(IDE ide) {
        super(ide);
    }

    public void deleteVersion(boolean deleteS3Bundle) {
        waitCodenvyWindowOpened(deleteVersionWindow, TestConstants.SHORT_WAIT_IN_SEC, PaaSException.class);

        // invert deleteS3BundleCheckbox state if (deleteS3Bundle flag != deleteS3BundleCheckbox state) 
        if (deleteS3Bundle != deleteS3BundleCheckbox.isSelected()) {
            deleteS3BundleCheckbox.click();
        }

        deleteButton.click();

        try {
            IDE().LOADER.waitClosed();
        } catch (InterruptedException e) {
            throw new TimeoutException(e);
        }

        if (isCodenvyWindowDisplayed(deleteVersionWindow)) {
            waitCodenvyWindowClosed(Locators.DELETE_VERSION_WINDOW_ID, TestConstants.SHORT_WAIT_IN_SEC, PaaSException.class);
        }
    }

}