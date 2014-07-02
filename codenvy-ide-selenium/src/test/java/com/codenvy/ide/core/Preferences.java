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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by The eXo Platform SAS .
 *
 * @author Musienko Maksim
 */

public class Preferences extends AbstractTestModule {
    /** @param ide */
    public Preferences(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private interface Locators {
        String PREFERNCESS_FORM_ID = "eXoPreferencesView-window";

        String PREFERNCES_STHREE_FORM_ID = "eXoPreferencesViewPreferencesTree";

        String PREFERNCES_VIEW_PANEL_FORM_ID = "eXoViewPanel";

        String PREFERNCES_TREE_SELECTOR = "//div[@id='eXoPreferencesViewPreferencesTree']//div/span[text()='%s']";

        String PREFERNCES_CLOSE_BUTTON_ID = "eXoPreferencesViewCloseButton";

    }

    @FindBy(id = Locators.PREFERNCESS_FORM_ID)
    private WebElement preferenceForm;

    @FindBy(id = Locators.PREFERNCES_STHREE_FORM_ID)
    private WebElement menuesThree;

    @FindBy(id = Locators.PREFERNCES_VIEW_PANEL_FORM_ID)
    private WebElement viewForm;

    @FindBy(id = Locators.PREFERNCES_CLOSE_BUTTON_ID)
    private WebElement closeBtn;

    /** wait opening preferences form */
    public void waitPreferencesOpen() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return preferenceForm != null && preferenceForm.isDisplayed() && menuesThree != null
                           && menuesThree.isDisplayed() && viewForm != null && viewForm.isDisplayed();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /** wait while for closed */
    public void waitPreferencesClose() {
        (new WebDriverWait(driver(), 30)).until(ExpectedConditions.invisibilityOfElementLocated(By
                                                                                                        .id(Locators


                                                                                                                    .PREFERNCESS_FORM_ID)));
    }

    /**
     * wait tree menu for calling customize form
     *
     * @param nameMenu
     */
    public void waitCustomizeMenuRedraw(final String nameMenu) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    WebElement menu =
                            driver().findElement(By.xpath(String.format(Locators.PREFERNCES_TREE_SELECTOR, nameMenu)));
                    return menu != null && menu.isDisplayed();
                } catch (NoSuchElementException e) {
                    return false;
                }
            }
        });
    }

    /**
     * select menu for customization
     *
     * @param nameMenu
     */
    public void selectCustomizeMenu(String nameMenu) {
        waitCustomizeMenuRedraw(nameMenu);
        driver().findElement(By.xpath(String.format(Locators.PREFERNCES_TREE_SELECTOR, nameMenu))).click();
    }

    /** click on close btn of the preferences form */
    public void clickOnCloseFormBtn() {
        closeBtn.click();
        waitPreferencesClose();
    }

}
