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
package com.codenvy.sitecldide.core;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Musienko Maxim
 *
 */
public class ThankYouPage extends AbstractTestModule {


    public ThankYouPage(com.codenvy.sitecldide.CLDIDE CLDIDE) {
        super(CLDIDE);
    }

    private interface Locators {

        String EMAIL_FIELD = "//input[@placeholder='Enter email for free account']";


    }

    @FindBy(linkText = Locators.EMAIL_FIELD)
    private WebElement headerEnterpriceLink;


    /**
     * get all text from page and wait while specified in param text appears on page
     *
     * @param text
     */
    public void waitExpectedTextOnPage(final String text) {
        (new WebDriverWait(driver(), 10)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                String allTxt = driver().findElement(By.tagName("body")).getText();
                boolean chkText = false;
                if (allTxt.contains(text) || allTxt.contains(text.toUpperCase()))
                    chkText = true;
                return chkText;

            }
        });

    }


    public void waitLinkWithSpecifiedName(final String text) {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOfElementLocated(By.linkText(text)));
    }

}