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
public class TerminateEnvironmentWindow extends AbstractTestModule {

    interface Locators {
        String TERMINATE_ENVIRONMENT_WINDOW_ID = "ideTerminateEnvironmentView-window";
        String TERMINATE_BUTTON_ID             = "ideTerminateEnvironmentViewTerminateButton";
    }

    @FindBy(id = Locators.TERMINATE_ENVIRONMENT_WINDOW_ID)
    WebElement terminateEnvironmentWindow;

    @FindBy(id = Locators.TERMINATE_BUTTON_ID)
    WebElement terminateButton;

    TerminateEnvironmentWindow(IDE ide) {
        super(ide);
    }

    public void terminate() {
        waitCodenvyWindowOpened(terminateEnvironmentWindow, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);

        terminateButton.click();

        try {
            IDE().LOADER.waitClosed();
        } catch (InterruptedException e) {
            throw new TimeoutException(e);
        }

        if (isCodenvyWindowDisplayed(terminateEnvironmentWindow)) {
            waitCodenvyWindowClosed(Locators.TERMINATE_ENVIRONMENT_WINDOW_ID, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);
        }
    }

}