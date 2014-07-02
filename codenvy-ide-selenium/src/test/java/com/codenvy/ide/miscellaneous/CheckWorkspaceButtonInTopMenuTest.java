/*
 *
 * CODENVY CONFIDENTIAL
 * __________________
 *
 *  [2012] - [2014] Codenvy, S.A.
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
package com.codenvy.ide.miscellaneous;

import com.codenvy.ide.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Zaryana Dombrovskaya
 */
public class CheckWorkspaceButtonInTopMenuTest extends BaseTest {

    @Test
    public void checkWorkspaceButtonInTopMenu() throws Exception{
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
        IDE.EXPLORER.waitOpened();
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Workspace")));
        driver.findElement(By.linkText("Workspace")).click();
        IDE.SELECT_WORKSPACE.waitSelectWorkspacesPageOpened();
        IDE.SELECT_WORKSPACE.clickOnWorkspaceName(TENANT_NAME);
        IDE.EXPLORER.waitOpened();
        Assert.assertEquals(driver.getCurrentUrl(), (PROTOCOL + "://" + IDE_HOST + "/ide/" + TENANT_NAME));
    }
}
