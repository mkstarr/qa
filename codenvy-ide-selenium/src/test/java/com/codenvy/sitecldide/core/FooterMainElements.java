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
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * @author Musienko Maxim
 *
 */
public class FooterMainElements extends AbstractTestModule {


    public FooterMainElements(com.codenvy.sitecldide.CLDIDE CLDIDE) {
        super(CLDIDE);
    }

    private final String[] footerLinks =
            {"Features", "Pricing", "Enterprise", "ISV", "Documentation", "About", "Jobs", "Contact", "Blog", "Facebook", "Twitter",
             "Google+"};


    private interface Locators {
        String FOOTER_ELEMENTS = "//footer//a";
    }

    /**
     * wait while specific link will be present in footer
     *
     * @param lnk
     */
    public void waitMainLinksInFooter(final String lnk) {

        (new WebDriverWait(driver(), 10)).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                List<WebElement> elements = driver().findElements(By.xpath(Locators.FOOTER_ELEMENTS));
                String resultStr = "";
                StringBuilder strBuild = new StringBuilder();
                for (WebElement element : elements) {
                    strBuild = strBuild.append(element.getText() + " ");
                }

                return strBuild.toString().contains(lnk);

            }
        });

    }

    /** wait while all default links in footer will appear */
    public void waitMainFooterLinks() {
        for (String footerLink : footerLinks) {
            waitMainLinksInFooter(footerLink);
        }
    }


}
