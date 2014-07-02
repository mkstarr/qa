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

import com.codenvy.ide.IDE;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 @author Roman Iuvshin
 *
 */
public class ErrorTenantNamePage extends AbstractTestModule {
    public ErrorTenantNamePage(IDE ide) {
        super(ide);
    }

    interface Locators {

        String WS_DOES_NOT_EXIST = "//h1[text()='WORKSPACE DOES NOT EXIST']";

        String WARNING_MESSAGE = "//p[text()=' Please, make sure you entered a correct workspace name.']";
    }

    /** wait for error page */
    public void waitErrorTenantNamePage() {
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.WS_DOES_NOT_EXIST)));
        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.WARNING_MESSAGE)));
    }
}
