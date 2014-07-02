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
package com.codenvy.ide.factoryUrl;

import com.codenvy.ide.BaseTest;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Roman Iuvshin */

public class InvalidFactoryURLTest extends BaseTest {

    private WebDriverWait wait;

    @Test
    public void ivalidFactoryURLTest() throws Exception {
        wait = new WebDriverWait(driver, 30);
        IDE.EXPLORER.waitOpened();
        IDE.LOGIN.logout();
        driver.get(PROTOCOL + "://" + IDE_HOST + "/factory?v=invalidArgs");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[contains(.,'INVALID FACTORY URL')]")));
    }
}