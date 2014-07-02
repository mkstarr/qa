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

import com.codenvy.ide.MenuCommands;

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
public class GetURL extends AbstractTestModule {
    /** @param ide */
    public GetURL(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private final class Locators {
        public static final String VIEW_ID = "ideGetItemURLForm";

        public static final String VIEW_LOCATOR = "//div[@view-id='" + VIEW_ID + "']";

        public static final String OK_BUTTON_ID = "ideGetItemURLFormOkButton";

        public static final String PRIVATE_URL_FIELD_ID = "ideGetItemURLFormPrivateURLField";

        public static final String PUBLIC_URL_FIELD_ID = "ideGetItemURLFormPublicURLField";
    }

    @FindBy(xpath = Locators.VIEW_LOCATOR)
    WebElement view;

    @FindBy(name = Locators.PRIVATE_URL_FIELD_ID)
    WebElement privateUrlField;

    @FindBy(name = Locators.PUBLIC_URL_FIELD_ID)
    WebElement publicUrlField;

    @FindBy(id = Locators.OK_BUTTON_ID)
    WebElement okButton;

    /**
     * Waits for Get URL view to be opened.
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
     * Waits for Get URL view to be closed.
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
     * Checks view's components.
     *
     * @return {@link Boolean} if <code>true</code> view's elements are present
     */
    public boolean isOpened() {
        return (view != null && view.isDisplayed() && okButton != null && okButton.isDisplayed()
                && privateUrlField != null && privateUrlField.isDisplayed() && publicUrlField != null && publicUrlField
                .isDisplayed());
    }

    /** Click Ok button. */
    public void clickOkButton() {
        okButton.click();
    }

    /**
     * Get the value of the private URL field.
     *
     * @return {@link String} private URL value
     */
    public String getPrivateURLValue() {
        return IDE().INPUT.getValue(privateUrlField);
    }

    /**
     * Get the value of the public URL field.
     *
     * @return {@link String} public URL value
     */
    public String getPublicURLValue() {
        return IDE().INPUT.getValue(publicUrlField);
    }

    public String getPrivateURL() throws Exception {
        IDE().MENU.runCommand(MenuCommands.View.VIEW, MenuCommands.View.GET_URL);
        waitOpened();
        String url = IDE().INPUT.getValue(privateUrlField);
        clickOkButton();
        waitClosed();
        return url;
    }

    public String getPublicURL() throws Exception {
        IDE().MENU.runCommand(MenuCommands.View.VIEW, MenuCommands.View.GET_URL);
        waitOpened();
        String url = IDE().INPUT.getValue(publicUrlField);
        clickOkButton();
        waitClosed();
        return url;
    }

}
