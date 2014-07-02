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
import com.codenvy.ide.core.Build;
import com.codenvy.ide.core.exceptions.PaaSException;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/** @author Dmytro Nochevnov */
public class CreateApplicationWindow extends AbstractTestModule {

    interface Locators {
        String CREATE_APPICATION_WINDOW_ID = "ideCreateApplicationView-window";

        // step 1 web elements
        String APPLICATION_NAME_FIELD_NAME = "ideCreateApplicationViewNameField";
        String NEXT_BUTTON_ID              = "ideCreateApplicationViewNextButton";

        // step 2 web elements
        String ENVIRONMENT_NAME_FIELD_NAME = "ideCreateApplicationViewEnvNameField";
        String FINISH_BUTTON_ID            = "ideCreateApplicationViewFinishButton";
    }

    @FindBy(id = Locators.CREATE_APPICATION_WINDOW_ID)
    WebElement createApplicationWindow;

    @FindBy(name = Locators.APPLICATION_NAME_FIELD_NAME)
    WebElement applicationNameField;

    @FindBy(name = Locators.ENVIRONMENT_NAME_FIELD_NAME)
    WebElement environmentNameField;

    @FindBy(id = Locators.NEXT_BUTTON_ID)
    WebElement nextButton;

    @FindBy(id = Locators.FINISH_BUTTON_ID)
    WebElement finishButton;

    CreateApplicationWindow(IDE ide) {
        super(ide);
    }

    public void create(String applicationName, String environmentName) {
        waitCodenvyWindowOpened(createApplicationWindow, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);

        applicationNameField.clear();
        applicationNameField.sendKeys(applicationName);

        nextButton.click();

        if (IDE().BUILD.isOpened()) {
            IDE().BUILD.waitBuilderContainText(Build.Messages.BUILD_SUCCESS, TestConstants.MIDDLE_WAIT_IN_SEC);
        }

        waitCodenvyWindowOpened(environmentNameField, TestConstants.SHORT_WAIT_IN_SEC, RuntimeException.class);

        environmentNameField.clear();
        environmentNameField.sendKeys(environmentName);

        finishButton.click();

        try {
            IDE().LOADER.waitClosed();
        } catch (InterruptedException e) {
            throw new TimeoutException(e);
        }

        waitCodenvyWindowClosed(Locators.CREATE_APPICATION_WINDOW_ID, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);
    }

}
