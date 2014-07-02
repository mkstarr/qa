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

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** @author Dmytro Nochevnov */
public class ManageApplicationsWindow extends AbstractTestModule {

    ManageApplicationsWindow(IDE ide) {
        super(ide);
    }

    interface Locators {
        String MANAGE_APPLICATIIONS_WINDOW_ID  = "ideAppfogApplicationsView-window";
        String APPLICATIONS_GRID_XPATH         = "//*[@id='applicationsListGrid']";
        String APPLICATION_NAME_XPATH_TEMPLATE = APPLICATIONS_GRID_XPATH + "/tbody[1]/tr/td[1]/div[contains(text(), '%s')]";
        String DELETE_BUTTON_XPATH             = "./td/div/button[contains(text(), 'Delete')]";
        String CLOSE_BUTTON_ID                 = "applicationsCloseButton";
    }

    public enum Property {
        NAME("./td[1]/div"),
        INSTANCES("./td[2]/div"),
        STATUS("./td[3]/div"),
        URLS("./td[4]/div"),
        SERVICES("./td[5]/div");

        private String retionalLocator;

        private Property(String retionalLocator) {
            this.retionalLocator = retionalLocator;
        }

        public String getRetionalLocator() {
            return retionalLocator;
        }

    }

    @FindBy(id = Locators.MANAGE_APPLICATIIONS_WINDOW_ID)
    WebElement manageApplicationsWindow;

    @FindBy(id = Locators.CLOSE_BUTTON_ID)
    WebElement closeButton;

    public Map<Property, String> getApplicationProperties(String applicationName) {
        waitCodenvyWindowOpened(manageApplicationsWindow, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);

        Map<Property, String> applicationProperties = new HashMap<Property, String>();

        WebElement applicationRow = getApplicationRow(applicationName);

        if (applicationRow == null) {
            throw new NoSuchElementException(String.format("There is no application with name '%s'.", applicationName));
        }

        for (Property property : Property.values()) {
            System.out.println(String.format("[INFO] get property %s", property.toString()));   // TODO use unified logger
            String value = applicationRow.findElement(By.xpath(property.getRetionalLocator())).getText();
            applicationProperties.put(property, value);
        }

        closeButton.click();

        if (isCodenvyWindowDisplayed(manageApplicationsWindow)) {
            waitCodenvyWindowClosed(Locators.MANAGE_APPLICATIIONS_WINDOW_ID, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);
        }

        return applicationProperties;
    }

    public void deleteApplication(final String applicationName, boolean deleteBoundServices) {
        waitCodenvyWindowOpened(manageApplicationsWindow, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);

        WebElement applicationRow = getApplicationRow(applicationName);

        if (applicationRow == null) {
            throw new NoSuchElementException(String.format("There is no application with name '%s'.", applicationName));
        }

        WebElement deleteButton =
                applicationRow.findElement(By.xpath(Locators.DELETE_BUTTON_XPATH));   // get delete button from application row
        deleteButton.click();

        IDE().APP_FOG.deleteApplicationWindow.deleteApplication(deleteBoundServices);

        // wait until application disappear from manage applications window
        String applicationNameFieldXpath = String.format(Locators.APPLICATION_NAME_XPATH_TEMPLATE, applicationName);
        new WebDriverWait(driver(), TestConstants.ANIMATION_PERIOD)
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(applicationNameFieldXpath)));

        closeButton.click();

        if (isCodenvyWindowDisplayed(manageApplicationsWindow)) {
            waitCodenvyWindowClosed(Locators.MANAGE_APPLICATIIONS_WINDOW_ID, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);
        }
    }

    public boolean checkApplicationPresents(String applicationName) {
        waitCodenvyWindowOpened(manageApplicationsWindow, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);

        String applicationNameFieldXpath = String.format(Locators.APPLICATION_NAME_XPATH_TEMPLATE, applicationName);

        boolean isPresent = true;
        try {
            manageApplicationsWindow.findElement(By.xpath(applicationNameFieldXpath));
        } catch (NoSuchElementException nse) {
            isPresent = false;
        }

        closeButton.click();

        if (isCodenvyWindowDisplayed(manageApplicationsWindow)) {
            waitCodenvyWindowClosed(Locators.MANAGE_APPLICATIIONS_WINDOW_ID, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);
        }

        return isPresent;
    }

    /**
     * @param applicationName
     * @return null if there is no application row with name = applicationName in manage applications window
     */
    public WebElement getApplicationRow(String applicationName) {
        List<WebElement> applications = manageApplicationsWindow.findElements(By.xpath(Locators.APPLICATIONS_GRID_XPATH + "/tbody[1]/tr"));

        if (applications == null) {
            return null;
        }

        for (WebElement applicationRow : applications) {
            WebElement applicationNameField = applicationRow.findElement(By.xpath(Property.NAME.getRetionalLocator()));

            if (applicationNameField.getText().equals(applicationName)) {
                return applicationRow;
            }
        }

        return null;
    }
}
