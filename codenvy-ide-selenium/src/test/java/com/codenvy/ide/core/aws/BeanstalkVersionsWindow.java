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

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** @author Dmytro Nochevnov */
public class BeanstalkVersionsWindow extends AbstractTestModule {

    BeanstalkVersionsWindow(IDE ide) {
        super(ide);
    }

    interface Locators {
        String VERSIONS_TAB_TITLE_XPATH     = "//*[@class='tabTitleText'][contains(text(), 'Versions')]";
        String VERSIONS_GRID_XPATH          = "//*[@id='ideVersionsGrid']";
        String VERSION_LABEL_XPATH_TEMPLATE = VERSIONS_GRID_XPATH + "/tbody[1]/tr/td[1]/div[contains(text(), '%s')]";
        String FIRST_VERSION_NAME_XPATH     = VERSIONS_GRID_XPATH + "/tbody[1]/tr[1]/td[1]/div";
        String DELETE_BUTTON_XPATH          = "./td[6]/div/button[contains(text(), 'Delete')]";
    }

    public enum VersionProperty {
        VERSION_LABEL("./td[1]/div"),
        DESCRIPTION("./td[2]/div"),
        CREATION_DATE("./td[3]/div"),
        UPDATED_DATE("./td[4]/div");

        private String relationLocator;

        private VersionProperty(String relationLocator) {
            this.relationLocator = relationLocator;
        }

        public String getRelationLocator() {
            return relationLocator;
        }

    }

    @FindBy(xpath = Locators.VERSIONS_TAB_TITLE_XPATH)
    WebElement versionTabTitle;

    @FindBy(xpath = Locators.VERSIONS_GRID_XPATH)
    WebElement versionsTable;

    @FindBy(xpath = Locators.FIRST_VERSION_NAME_XPATH)
    WebElement firstVersionNameField;

    public String getFirstVersionLabel() {
        waitCodenvyWindowOpened(versionsTable, TestConstants.MIDDLE_WAIT_IN_SEC, PaaSException.class);

        return firstVersionNameField.getText();
    }

    public Map<String, String> getVersionProperties(String versionLabel) {
        waitCodenvyWindowOpened(versionsTable, TestConstants.MIDDLE_WAIT_IN_SEC, PaaSException.class);

        Map<String, String> versionProperties = new HashMap<String, String>();

        WebElement versionRow = getVersionRow(versionLabel);

        if (versionRow == null) {
            throw new NoSuchElementException(String.format("There is no version with label '%s'.", versionLabel));
        }

        for (VersionProperty property : VersionProperty.values()) {
            System.out.println(String.format("[INFO] get property %s", property.toString()));   // TODO use unified logger
            String value = versionRow.findElement(By.xpath(property.getRelationLocator())).getText();
            versionProperties.put(property.toString(), value);
        }

        return versionProperties;
    }

    @SuppressWarnings("unused")
    public void deleteVersion(final String versionLabel, boolean deleteS3Bucket) {
        waitCodenvyWindowOpened(versionsTable, TestConstants.MIDDLE_WAIT_IN_SEC, PaaSException.class);

        final WebElement versionRow = getVersionRow(versionLabel);

//        System.out.println(versionRow.getText());

        if (versionRow == null) {
            throw new NoSuchElementException(String.format("There is no version with label '%s'.", versionLabel));
        }

        // delete version
        WebElement deleteButton =
                versionRow.findElement(By.xpath(Locators.DELETE_BUTTON_XPATH));   // get delete button from version row
        deleteButton.click();

        IDE().AWS.deleteVersionWindow.deleteVersion(deleteS3Bucket);
    }

    public boolean checkVersionPresent(String versionLabel) {
        waitCodenvyWindowOpened(versionsTable, TestConstants.MIDDLE_WAIT_IN_SEC, PaaSException.class);

        String versionLabelFieldXpath = String.format(Locators.VERSION_LABEL_XPATH_TEMPLATE, versionLabel);

        boolean isPresent = true;
        try {
            versionsTable.findElement(By.xpath(versionLabelFieldXpath));
        } catch (NoSuchElementException nse) {
            isPresent = false;
        }

        return isPresent;
    }

    /**
     * @param versionLabel
     * @return null if there is no version row with name = versionLabel in manage versions window
     */
    public WebElement getVersionRow(String versionLabel) {
        List<WebElement> versions = driver().findElements(By.xpath(Locators.VERSIONS_GRID_XPATH + "/tbody[1]/tr"));

        if (versions == null) {
            return null;
        }

        for (WebElement versionRow : versions) {
            WebElement versionLabelField = versionRow.findElement(By.xpath(VersionProperty.VERSION_LABEL.getRelationLocator()));

            if (versionLabelField.getText().equals(versionLabel)) {
                return versionRow;
            }
        }

        return null;
    }

    public String getVersionProperty(VersionProperty property, final String versionLabel) {
        return getVersionRow(versionLabel)
                .findElement(By.xpath(property.getRelationLocator()))
                .getText();
    }
}
