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
import com.codenvy.ide.core.Output;
import com.codenvy.ide.core.exceptions.PaaSException;
import com.codenvy.ide.paas.aws.AwsApplicationInfo;
import com.google.common.base.Predicate;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dmytro Nochevnov
 *
 */
public class BeanstalkEnvironmentsWindow extends AbstractTestModule {
    
    BeanstalkEnvironmentsWindow(IDE ide) {
        super(ide);
    }
    
    interface Locators {
        String ENVIRONMENTS_TAB_TITLE_XPATH = "//*[@class='tabTitleText'][contains(text(), 'Environments')]";
        String ENVIRONMENTS_GRID_XPATH = "//*[@id='ideEnvironmentsGrid']";
        String ENVIRONMENT_NAME_XPATH_TEMPLATE = ENVIRONMENTS_GRID_XPATH + "/tbody[1]/tr/td[1]/div[contains(text(), '%s')]";
        String FIRST_ENVIRONMENT_NAME_XPATH = ENVIRONMENTS_GRID_XPATH + "/tbody[1]/tr[1]/td[1]/div";
        String TERMINATE_BUTTON_ID = "ideManageApplicationViewTerminateEnvironmentButton"; 
    }
    
    public enum EnvironmentProperty {
        ENVIRONMENT_NAME("./td[1]/div"),
        SOLUTION_STACK("./td[2]/div"),        
        RUNNING_VERSION("./td[3]/div"),
        STATUS("./td[4]/div"),
        HEALTH("./td[5]/div"),
        URL("./td[6]/div/a");
        
        private String relationLocator;
        
        private EnvironmentProperty(String relationLocator) {
            this.relationLocator = relationLocator;
        }
        
        public String getRelationLocator() {
            return relationLocator;
        }
        
    }
    
    @FindBy(xpath = Locators.ENVIRONMENTS_TAB_TITLE_XPATH)
    WebElement environmentTabTitle;
    
    @FindBy(xpath = Locators.ENVIRONMENTS_GRID_XPATH)
    WebElement environmentsTable;
    
    @FindBy(id = Locators.TERMINATE_BUTTON_ID)
    WebElement terminateButton;
    
    @FindBy(xpath = Locators.FIRST_ENVIRONMENT_NAME_XPATH)
    WebElement firstEnvironmentNameField;
    
    public String getFirstEnvironmentName() {
        waitCodenvyWindowOpened(environmentsTable, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);
        
        return firstEnvironmentNameField.getText();
    }
    
    public Map<String, String> getEnvironmentProperties(String environmentName) {
        waitCodenvyWindowOpened(environmentsTable, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);
        
        Map<String, String> environmentProperties = new HashMap<String, String>();
        
        WebElement environmentRow = getEnvironmentRow(environmentName);
        
        if (environmentRow == null) {       
            throw new NoSuchElementException(String.format("There is no environment with name '%s'.", environmentName));
        }

        for (EnvironmentProperty property: EnvironmentProperty.values()) {
            System.out.println(String.format("[INFO] get property %s", property.toString()));   // TODO use unified logger
            String value = environmentRow.findElement(By.xpath(property.getRelationLocator())).getText();
            environmentProperties.put(property.toString(), value);
        }        
        
        // rewrite url with url prefix
        String url = environmentRow.findElement(By.xpath(EnvironmentProperty.URL.getRelationLocator())).getAttribute("href");
        String urlPrefix = url.split("-")[0];  // get prefix "http://qandpspring29101env" of url like "http://qandpspring29101env-x45wmzhjmy.elasticbeanstalk.com/"
        environmentProperties.put(EnvironmentProperty.URL.toString(), urlPrefix);
        
        
        return environmentProperties;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void terminateEnvironment(final String environmentName) {
        waitCodenvyWindowOpened(environmentsTable, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);
        
        final WebElement environmentRow = getEnvironmentRow(environmentName);

        if (environmentRow == null) {
            throw new NoSuchElementException(String.format("There is no environment with name '%s'.", environmentName));
        }
        
        // select environment row
        environmentRow.click();        
        new WebDriverWait(driver(), TestConstants.SHORT_WAIT_IN_SEC).until(ExpectedConditions.elementToBeClickable(By.id(Locators.TERMINATE_BUTTON_ID)));        
        terminateButton.click();
        
        IDE().AWS.terminateEnvironmentWindow.terminate();
        
        // wait until environment start terminating in environments table
        waitOnEnvironmentStatus(environmentName, AwsApplicationInfo.Status.Terminating.name());
        IDE().OUTPUT.waitOnInfoMessage(String.format(Output.Messages.TERMINATING_ENVIRONMENT_MESSAGE_TEMPLATE, environmentName),
                                       TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);
        
        // wait until environment terminated in environments table        
        waitOnEnvironmentStatus(environmentName, AwsApplicationInfo.Status.Terminated.name());
    }
    
    public boolean checkEnvironmentPresent(String environmentName) {
        waitCodenvyWindowOpened(environmentsTable, TestConstants.LONG_WAIT_IN_SEC, PaaSException.class);

        String environmentNameFieldXpath = String.format(Locators.ENVIRONMENT_NAME_XPATH_TEMPLATE, environmentName);
        
        boolean isPresent = true;
        try {
            environmentsTable.findElement(By.xpath(environmentNameFieldXpath));
        } catch(NoSuchElementException nse) {
            isPresent = false;
        }
        
        return isPresent;
    }
        
    /** 
     * @param environmentName
     * @return null if there is no environment row with name = environmentName in manage environments window
     */
    public WebElement getEnvironmentRow(String environmentName) {
        List<WebElement> environments = driver().findElements(By.xpath(Locators.ENVIRONMENTS_GRID_XPATH + "/tbody[1]/tr"));
     
        if (environments == null) {
            return null;
        }
        
        for (WebElement environmentRow: environments) {            
            WebElement environmentNameField = environmentRow.findElement(By.xpath(EnvironmentProperty.ENVIRONMENT_NAME.getRelationLocator()));
            
            if (environmentNameField.getText().equals(environmentName)) {
                return environmentRow;
            }
        }
        
        return null;
    }
    
    public String getEnvironmentProperty(EnvironmentProperty property, final String environmentName) {
        return getEnvironmentRow(environmentName)
               .findElement(By.xpath(property.getRelationLocator()))
               .getText();
    }
    
    /**
     * 
     * @param environmentName
     * @param status
     * @throws NoSuchElementException if there is no row with environmentName
     * @throws PaaSException if there is WarningDialog
     */
    @SuppressWarnings("rawtypes")
    private void waitOnEnvironmentStatus(final String environmentName, final String status) throws NoSuchElementException, PaaSException {
        IDE().WARNING_DIALOG.waitEventOrWarningDialog(new Predicate(){
            public boolean apply(Object input) {
                try {
                    return getEnvironmentProperty(EnvironmentProperty.STATUS, environmentName).equals(status);
                } catch(NoSuchElementException e) {
                    throw new NoSuchElementException(String.format("There is no environment with name '%s'.", environmentName));
                }
            }
        }, TestConstants.VERY_LONG_WAIT_IN_SEC, PaaSException.class);
    }
}
