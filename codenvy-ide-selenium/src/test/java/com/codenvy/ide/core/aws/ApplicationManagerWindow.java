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
import com.codenvy.ide.Utils;
import com.codenvy.ide.core.AbstractTestModule;
import com.codenvy.ide.core.exceptions.PaaSException;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.HashMap;
import java.util.Map;

/** @author Dmytro Nochevnov */
public class ApplicationManagerWindow extends AbstractTestModule {

    ApplicationManagerWindow(IDE ide) {
        super(ide);
    }

    interface Locators {
        String APPLICATION_MANAGER_WINDOW_ID = "ideManageApplicationView-window";
        String DELETE_BUTTON_ID              = "ideMainTabPainDeleteApplicationButton";
        String CLOSE_BUTTON_XPATH            = "//*[@id='ideManageApplicationViewCloseButton']/table";
    }

    @FindBy(id = Locators.APPLICATION_MANAGER_WINDOW_ID)
    WebElement applicationManagerWindow;

    @FindBy(id = Locators.DELETE_BUTTON_ID)
    WebElement deleteButton;

    @FindBy(xpath = Locators.CLOSE_BUTTON_XPATH)
    WebElement closeButton;

    public enum GeneralProperty {
        NAME(By.name("ideMainTabPainNameField")) {
            public String getValue(WebDriver driver) {
                try {
                    return driver.findElement(locator).getAttribute("value");
                } catch (NoSuchElementException exc) {
                    Utils.captureScreenShotOnFailure(driver, exc.getStackTrace());
                    exc.printStackTrace();
                    return null;
                }
            }
        };

        By locator;

        private GeneralProperty(By locator) {
            this.locator = locator;
        }

        /** @return value of property */
        public String getValue(WebDriver driver) {
            return null;
        }
    }

    public Map<String, String> getApplicationProperties() {
        waitCodenvyWindowOpened(applicationManagerWindow, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);

        Map<String, String> applicationProperties = new HashMap<String, String>();

        for (GeneralProperty property : GeneralProperty.values()) {
            System.out.println(String.format("[INFO] get property %s", property.toString()));   // TODO use unified logger
            applicationProperties.put(property.toString(), property.getValue(driver()));
        }

        IDE().AWS.beanstalkEnvironmentsWindow.environmentTabTitle.click();
        String firstEnvironmentName = IDE().AWS.beanstalkEnvironmentsWindow.getFirstEnvironmentName();
        applicationProperties.putAll(IDE().AWS.beanstalkEnvironmentsWindow.getEnvironmentProperties(firstEnvironmentName));

        closeButton.click();

        if (isCodenvyWindowDisplayed(applicationManagerWindow)) {
            waitCodenvyWindowClosed(Locators.APPLICATION_MANAGER_WINDOW_ID, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);
        }

        return applicationProperties;
    }

    public void deleteApplication() {
        waitCodenvyWindowOpened(applicationManagerWindow, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);

        deleteButton.click();

        try {
            IDE().LOADER.waitClosed();
        } catch (InterruptedException e) {
            throw new TimeoutException(e);
        }

        IDE().ASK_DIALOG.waitOpened();

        IDE().ASK_DIALOG.clickYes();

        IDE().ASK_DIALOG.waitClosed(TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);

        try {
            IDE().LOADER.waitClosed();
        } catch (InterruptedException e) {
            throw new TimeoutException(e);
        }

        if (isCodenvyWindowDisplayed(applicationManagerWindow)) {
            closeButton.click();
            waitCodenvyWindowClosed(Locators.APPLICATION_MANAGER_WINDOW_ID, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);
        }
    }

    public String getFirstEnvironmentName() {
        waitCodenvyWindowOpened(applicationManagerWindow, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);

        IDE().AWS.beanstalkEnvironmentsWindow.environmentTabTitle.click();

        String firstEnvironmentName = IDE().AWS.beanstalkEnvironmentsWindow.getFirstEnvironmentName();

        closeButton.click();

        if (isCodenvyWindowDisplayed(applicationManagerWindow)) {
            waitCodenvyWindowClosed(Locators.APPLICATION_MANAGER_WINDOW_ID, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);
        }

        return firstEnvironmentName;
    }

    public void terminateEnvironment(String environmentName) {
        waitCodenvyWindowOpened(applicationManagerWindow, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);

        IDE().AWS.beanstalkEnvironmentsWindow.environmentTabTitle.click();
        IDE().AWS.beanstalkEnvironmentsWindow.terminateEnvironment(environmentName);

        closeButton.click();

        if (isCodenvyWindowDisplayed(applicationManagerWindow)) {
            waitCodenvyWindowClosed(Locators.APPLICATION_MANAGER_WINDOW_ID, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);
        }
    }

    public void deleteVersion(String versionLabel, boolean deleteS3Bundle) {
        waitCodenvyWindowOpened(applicationManagerWindow, TestConstants.MIDDLE_WAIT_IN_SEC, PaaSException.class);

        IDE().AWS.beanstalkVersionsWindow.versionTabTitle.click();
        IDE().AWS.beanstalkVersionsWindow.deleteVersion(versionLabel, deleteS3Bundle);

        closeButton.click();

        if (isCodenvyWindowDisplayed(applicationManagerWindow)) {
            waitCodenvyWindowClosed(Locators.APPLICATION_MANAGER_WINDOW_ID, TestConstants.MIDDLE_WAIT_IN_SEC, PaaSException.class);
        }
    }

    public String getFirstVersionLabel() {
        waitCodenvyWindowOpened(applicationManagerWindow, TestConstants.MIDDLE_WAIT_IN_SEC, PaaSException.class);

        IDE().AWS.beanstalkVersionsWindow.versionTabTitle.click();

        String firstVersionLabel = IDE().AWS.beanstalkVersionsWindow.getFirstVersionLabel();

        closeButton.click();

        if (isCodenvyWindowDisplayed(applicationManagerWindow)) {
            waitCodenvyWindowClosed(Locators.APPLICATION_MANAGER_WINDOW_ID, TestConstants.MIDDLE_WAIT_IN_SEC, PaaSException.class);
        }

        return firstVersionLabel;
    }

    public boolean checkVersionPresent(String versionLabel) {
        waitCodenvyWindowOpened(applicationManagerWindow, TestConstants.MIDDLE_WAIT_IN_SEC, PaaSException.class);

        IDE().AWS.beanstalkVersionsWindow.versionTabTitle.click();

        boolean isVersionPresent = IDE().AWS.beanstalkVersionsWindow.checkVersionPresent(versionLabel);

        closeButton.click();

        if (isCodenvyWindowDisplayed(applicationManagerWindow)) {
            waitCodenvyWindowClosed(Locators.APPLICATION_MANAGER_WINDOW_ID, TestConstants.MIDDLE_WAIT_IN_SEC, PaaSException.class);
        }

        return isVersionPresent;
    }

}
