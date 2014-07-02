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
import com.codenvy.ide.Utils;
import com.codenvy.ide.core.AbstractTestModule;
import com.codenvy.ide.core.exceptions.PaaSException;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
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
        String APPLICATION_MANAGER_WINDOW_ID = "ideAppfogProjectView-window";
        String DELETE_BUTTON_ID              = "ideAppfogProjectViewDeleteButton";
        String CLOSE_BUTTON_ID               = "ideAppfogProjectViewCloseButton";
    }

    @FindBy(id = Locators.APPLICATION_MANAGER_WINDOW_ID)
    WebElement applicationManagerWindow;

    @FindBy(id = Locators.DELETE_BUTTON_ID)
    WebElement deleteButton;

    @FindBy(id = Locators.CLOSE_BUTTON_ID)
    WebElement closeButton;

    public enum Property {
        NAME("ideAppfogProjectViewNameField", PropertyType.FIELD_WITH_NAME),
        URLS("ideAppfogProjectViewUrlField", PropertyType.TEXT_WITH_NAME),
        MEMORY("ideAppfogProjectViewMemoryField", PropertyType.FIELD_WITH_NAME),
        INSTANCES("ideAppfogProjectViewInstancesField", PropertyType.FIELD_WITH_NAME),
        INFRASTRUCTER("ideAppfogProjectViewInfraField", PropertyType.TEXT_WITH_ID),
        STACK("ideAppfogProjectViewStackField", PropertyType.TEXT_WITH_ID),
        MODEL("ideAppfogProjectViewModelField", PropertyType.TEXT_WITH_ID),
        STATUS("ideAppfogProjectViewStatusField", PropertyType.TEXT_WITH_ID);
//        SERVICES() {
//            public String getValue(WebDriver driver) {
//                // TODO
//                return null;
//            }
//        };

        private String locator;

        private PropertyType propertyType;

        private enum PropertyType {FIELD_WITH_NAME, TEXT_WITH_ID, TEXT_WITH_NAME}

        private Property() {
        }

        private Property(String locator, PropertyType propertyType) {
            this.locator = locator;
            this.propertyType = propertyType;
        }

        /**
         * Default way to findElement(By.name(applicationManagerLocator)).getAttribute("value")
         *
         * @param driver
         * @return value of property
         */
        public String getValue(AbstractTestModule outer) {
            switch (propertyType) {
                case FIELD_WITH_NAME: {
                    return getFieldValueByName(outer);
                }
                case TEXT_WITH_ID: {
                    return getTextById(outer);
                }
                case TEXT_WITH_NAME: {
                    return getTextByName(outer);
                }
            }
            return null;
        }

        public void setValue(AbstractTestModule outer, String value) {
            if (propertyType == PropertyType.FIELD_WITH_NAME) {
                WebElement field = outer.driver().findElement(By.name(locator));
                field.clear();
                field.sendKeys(value);
            }
        }

        private String getFieldValueByName(AbstractTestModule outer) {
            try {
                return outer.driver().findElement(By.name(locator)).getAttribute("value");
            } catch (NoSuchElementException exc) {
                System.out.println(String.format("[TEST FAIL] Field %s with name='%s' is not found", this,
                                                 locator));   // weak fail without interrupting; TODO use unified logger
                Utils.captureScreenShotOnFailure(outer.driver(), exc.getStackTrace());
                exc.printStackTrace();
                return null;
            }
        }

        private String getTextById(AbstractTestModule outer) {
            try {
                return outer.driver().findElement(By.id(locator)).getText();
            } catch (NoSuchElementException exc) {
                System.out.println(String.format("[TEST FAIL] Property %s with id='%s' is not found", this,
                                                 locator));  // weak fail without interrupting; TODO use unified logger
                Utils.captureScreenShotOnFailure(outer.driver(), exc.getStackTrace());
                exc.printStackTrace();
                return null;
            }
        }

        private String getTextByName(AbstractTestModule outer) {
            try {
                return outer.driver().findElement(By.name(locator)).getText();
            } catch (NoSuchElementException exc) {
                System.out.println(String.format("[TEST FAIL] Property %s with name='%s' is not found", this,
                                                 locator));   // weak fail without interrupting; TODO use unified logger
                exc.printStackTrace();
                Utils.captureScreenShotOnFailure(outer.driver(), exc.getStackTrace());
                return null;
            }
        }
    }

    public Map<Property, String> getApplicationProperties() {
        waitCodenvyWindowOpened(applicationManagerWindow, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);

        Map<Property, String> applicationProperties = new HashMap<Property, String>();

        for (Property property : Property.values()) {
            System.out.println(String.format("[INFO] get property %s", property.toString()));   // TODO use unified logger
            applicationProperties.put(property, property.getValue(this));
        }

        closeButton.click();

        if (isCodenvyWindowDisplayed(applicationManagerWindow)) {
            waitCodenvyWindowClosed(Locators.APPLICATION_MANAGER_WINDOW_ID, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);
        }

        return applicationProperties;
    }

    public void deleteApplication(boolean deleteBoundServices) {
        waitCodenvyWindowOpened(applicationManagerWindow, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);

        deleteButton.click();

        try {
            IDE().LOADER.waitClosed();
        } catch (InterruptedException e) {
            throw new TimeoutException(e);
        }

        IDE().APP_FOG.deleteApplicationWindow.deleteApplication(deleteBoundServices);

        if (isCodenvyWindowDisplayed(applicationManagerWindow)) {
            waitCodenvyWindowClosed(Locators.APPLICATION_MANAGER_WINDOW_ID, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);
        }
    }

}
