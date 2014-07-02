/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/** @author Musienko Maxim */
public class OpenResource extends AbstractTestModule {
    /** @param ide */
    public OpenResource(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {

        String OPEN_RESOURCE_VIEW = "ideOpenResourceView-window";

        String SEARCH_FIELD = "div#ideOpenResourceView-window input";

        String MATCHING_AREA = "//table[@__gwtcellbasedwidgetimpldispatchingfocus]";

        String ALL_FOUND_ELEMENTS_AREA = "//div[@id='ideOpenResourceView-window']//tr[@__gwt_row]";

        String ALL_TEXT_IN_AREA = "//div[@id='ideOpenResourceView-window']//tr[@__gwt_row]/parent::tbody";

        String SELECT_ITEM = "//div[@id='ideOpenResourceView-window']//tr[@__gwt_row]/td//div[contains(., '%s')]";

        String OPEN_BUTTON = "ideProjectResourcesOpenButton";

        String CANCEL_BUTTON = "ideProjectResourcesCancelButton";

        String CLOSE_WINDOW = "//div[@id='ideOpenResourceView-window']//img[@title='Close']";

    }

    // The basic webelements for open resource
    @FindBy(id = Locators.OPEN_RESOURCE_VIEW)
    private WebElement openResourceForm;

    @FindBy(xpath = Locators.ALL_TEXT_IN_AREA)
    private WebElement allText;

    @FindBy(css = Locators.SEARCH_FIELD)
    private WebElement searchField;

    @FindBy(id = Locators.OPEN_BUTTON)
    private WebElement openBtn;

    @FindBy(id = Locators.CANCEL_BUTTON)
    private WebElement closeBtn;

    @FindBy(xpath = Locators.CLOSE_WINDOW)
    private WebElement closeWindow;

    /** wait while open resource form opened */
    public void waitOpenResouceFormIsOpen() {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.visibilityOf(openResourceForm));
    }

    /** wait form closed */
    public void waitOpenResouceFormIsClosed() {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                      .xpath(Locators


                                                                                                                     .OPEN_RESOURCE_VIEW)));
    }

    /**
     * wait number all of the found items
     *
     * @param numberOfFound
     */
    public void waitWhileCertainNumberOfElementsIsAppear(final int numberOfFound) {

        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    List<WebElement> foundsElem = driver().findElements(By.xpath(Locators.ALL_FOUND_ELEMENTS_AREA));
                    return (foundsElem.size() == numberOfFound);
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /**
     * get all items into area
     *
     * @return
     */
    public String getAllText() {
        return allText.getText().trim();
    }

    /**
     * type value into search field
     *
     * @param txt
     */
    public void typeToSearchField(String txt) {
        searchField.sendKeys(txt);
    }

    /**
     * wait appearance full match
     *
     * @param item
     */

    public void waitFoundFiles(final String item) {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return getAllText().equals(item);
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /**
     * click on item with specified name
     *
     * @param item
     */
    public void selectItemInMatchList(String item) {
        driver().findElement(By.xpath(String.format(Locators.SELECT_ITEM, item))).click();
    }

    /**
     * click on close button
     *
     * @param item
     */
    public void clickOnCloseBtn() {
        closeBtn.click();
    }

    /**
     * click on close button
     *
     * @param item
     */
    public void clickOnOpenBtn() {
        openBtn.click();
    }

    /**
     * click on close window
     *
     * @param item
     */
    public void clickCloseWindow() {
        closeWindow.click();
    }

}
