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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Roman Iuvshin */
public class Navigator extends AbstractTestModule {
    /** @param ide */
    public Navigator(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    public interface Locators {
        String VIEW_ID = "//div[@view-id='ideWorkspaceView']";
    }

    /**
     * wait for Navigator will opened
     *
     * @throws InterruptedException
     */
    public void waitNavigatorOpened() throws InterruptedException {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Locators.VIEW_ID)));
    }

    /**
     * wait for Navigator will closed
     *
     * @throws InterruptedException
     */
    public void waitNavigatorClosed() throws InterruptedException {
        new WebDriverWait(driver(), 30)
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Locators.VIEW_ID)));
    }
}
