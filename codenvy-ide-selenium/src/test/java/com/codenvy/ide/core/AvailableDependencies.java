/*
 *
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
package com.codenvy.ide.core;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Anna Shumilova
 *
 */
public class AvailableDependencies extends AbstractTestModule {
    /** @param ide */
    public AvailableDependencies(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private final class Locators {
        static final String VIEW_ID = "ideJARPackagesView";

        static final String VIEW_LOCATOR = "//div[@view-id='" + VIEW_ID + "']";

        static final String DEPENDENCIES_GRID_ID = "ideAvailableDependenciesJARList";

        static final String ATTRIBUTES_GRID_ID = "ideAvailableDependenciesJARAttributesTable";

        static final String OK_BUTTON_ID = "ideAvailableDependenciesOkButton";

        static final String DEPENDENCY_BY_NAME_LOCATOR = "//table[@id='" + DEPENDENCIES_GRID_ID
                                                         + "']//div/span[contains(text(), '%s')]";

        static final String DEPENDENCY_BY_POSITION_SELECTOR = "table#" + DEPENDENCIES_GRID_ID
                                                              + " tbody:first-of-type>tr:nth-of-type(%s) span";

        static final String DEPENDENCY_SELECTOR = "table#" + DEPENDENCIES_GRID_ID + " tbody:first-of-type>tr";

        static final String ATTRIBUTE_ROW_LOCATOR = "//table[@id='" + ATTRIBUTES_GRID_ID + "']//tr[contains(., '%s')]";
        ;

        static final String ATTRIBUTE_SELECTOR = "table#" + ATTRIBUTES_GRID_ID + " tbody:first-of-type>tr";
    }

    @FindBy(xpath = Locators.VIEW_LOCATOR)
    private WebElement view;

    @FindBy(id = Locators.OK_BUTTON_ID)
    private WebElement okButton;

    @FindBy(id = Locators.DEPENDENCIES_GRID_ID)
    private WebElement dependenciesGrid;

    @FindBy(id = Locators.ATTRIBUTES_GRID_ID)
    private WebElement attributesGrid;

    /**
     * Wait Available dependencies view opened.
     *
     * @throws Exception
     */
    public void waitOpened() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return isOpened();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /**
     * Wait Available dependencies view closed.
     *
     * @throws Exception
     */
    public void waitClosed() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    input.findElement(By.xpath(Locators.VIEW_LOCATOR));
                    return false;
                } catch (NoSuchElementException e) {
                    return true;
                }
            }
        });
    }

    /**
     * Returns the opened state of the view and it's elements.
     *
     * @return {@link Boolean} opened state
     */
    public boolean isOpened() {
        return (view != null && view.isDisplayed() && okButton != null && okButton.isDisplayed()
                && dependenciesGrid != null && dependenciesGrid.isDisplayed() && attributesGrid != null &&
                attributesGrid
                        .isDisplayed());
    }

    /** Click Ok button. */
    public void clickOkButton() {
        okButton.click();
    }

    /**
     * Returns number of the attributes.
     *
     * @return int number of the attributes
     */
    public int getAttributeCount() {
        return driver().findElements(By.cssSelector(Locators.ATTRIBUTE_SELECTOR)).size();
    }

    /**
     * Returns number of the dependencies.
     *
     * @return int number of the dependencies
     */
    public int getDependencyCount() {
        return driver().findElements(By.cssSelector(Locators.DEPENDENCY_SELECTOR)).size();
    }

    /** Wait for dependencies to load. */
    public void waitForDependencies() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return getDependencyCount() > 0;
            }
        });
    }

    /**
     * Select dependency by it's name.
     *
     * @param dependency
     *         dependency
     */
    public void selectDependency(String dependency) {
        try {
            WebElement dependencyElement =
                    driver().findElement(By.xpath(String.format(Locators.DEPENDENCY_BY_NAME_LOCATOR, dependency)));
            dependencyElement.click();
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Select dependency by its position (starts from 1) in the dependency list.
     *
     * @param dependencyPosition
     *         dependency position in list of dependecies
     */
    public void selectDependency(int dependencyPosition) {
        try {
            WebElement dependencyElement =
                    driver().findElement(
                            By.cssSelector(
                                    String.format(Locators.DEPENDENCY_BY_POSITION_SELECTOR, dependencyPosition)));
            dependencyElement.click();
        } catch (NoSuchElementException e) {
        }
    }

    /**
     * Get value of the attribute, by it's name.
     *
     * @param attribute
     *         name of the attribute
     * @return {@link String} attribute's name
     */
    public String getAttributeValue(String attribute) {
        try {
            WebElement row = driver().findElement(By.xpath(String.format(Locators.ATTRIBUTE_ROW_LOCATOR, attribute)));
            return row.findElement(By.xpath("td[2]")).getText();
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
