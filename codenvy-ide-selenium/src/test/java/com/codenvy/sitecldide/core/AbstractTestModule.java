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

import com.codenvy.sitecldide.CLDIDE;

import org.openqa.selenium.WebDriver;

/**
 * @author Musienko Maxim
 *
 */
public class AbstractTestModule
{
    private CLDIDE    CLDIDE;
    public AbstractTestModule(CLDIDE CLDIDE){
            this.CLDIDE =CLDIDE;
    }



    public WebDriver driver() {
        return CLDIDE.driver();
    }

    CLDIDE CLDIDE() {
        return CLDIDE;
    }


}
