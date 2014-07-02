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

import java.util.List;

/**
 * @author Musienko Maxim
 *
 */
public class HeaderMainElements extends AbstractTestModule {

    public HeaderMainElements(com.codenvy.sitecldide.CLDIDE CLDIDE) {
        super(CLDIDE);
    }


    private final String[] headerLinks =
            {"Features", "Pricing", "Enterprise", "ISV"};


    private interface Locators {
        String HEADER_ELEMENTS = "//header//a";

        String LOGO = "a.logo>h1";
    }

    @FindBy(css = Locators.LOGO)
    private WebElement logo;


    /**
     * wait while specific link will be present in footer
     *
     * @param lnk
     */
    public void waitMainLinksInFooter(final String lnk) {

        (new WebDriverWait(driver(), 10)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                List<WebElement> elements = driver().findElements(By.xpath(Locators.HEADER_ELEMENTS));
                String resultStr = "";
                StringBuilder strBuild = new StringBuilder();
                for (WebElement element : elements) {
                    strBuild = strBuild.append(element.getText() + " ");
                }

                return strBuild.toString().contains(lnk);

            }
        });

    }

    /** wait import with specific name */
    public void waitLogo() {


        (new WebDriverWait(driver(), 10)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {

                return logo.getCssValue("background-image").contains("url(http") &&
                       logo.getCssValue("background-image").contains("codenvy") &&
                       logo.getCssValue("background-image").contains("images/logoCodenvy");

            }
        });
    }

    /** wait sign up button on header */

    public void waitSignUpBtn() {

        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.sign-up-button")));
    }

    /** wait login button on header */

    public void waitLoginBtn() {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a.light-button")));

    }


    /** wait while all default links in footer will appear */
    public void waitMainHeaderLinks() {
        for (String footerLink : headerLinks) {
            waitMainLinksInFooter(footerLink);
        }
        waitLogo();
        CLDIDE().MAINPAGE.waitHeaderLoginBtnLink();
    }

    /** click on 'Features' link in header a page */
    public void clickFeaturesLnk() {
        driver().findElement(By.linkText("Features")).click();
    }

    /** click on 'Stories' link in header a page */
    public void clickStoriesLnk() {
        driver().findElement(By.linkText("Stories")).click();
    }


    /** click on 'Pricing' link in header a page */
    public void clickPricingLnk() {
        driver().findElement(By.linkText("Pricing")).click();
    }

    /** click on 'Enterprice' link in header a page */
    public void clickEnterpriseLnk() {
        driver().findElement(By.linkText("Enterprise")).click();
    }


}
