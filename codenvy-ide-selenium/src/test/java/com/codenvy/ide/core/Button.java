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

import org.openqa.selenium.WebElement;

/**
 * @author Ann Zhuleva
 *
 */
public class Button extends AbstractTestModule {
    /** @param ide */
    public Button(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private final String ENABLED_ATTRIBUTE = "button-enabled";

    /**
     * Get the enabled state of the button.
     *
     * @param button
     * @return enabled state of the button
     */
    public boolean isButtonEnabled(WebElement button) {
        return Boolean.parseBoolean(button.getAttribute(ENABLED_ATTRIBUTE));
    }
}
