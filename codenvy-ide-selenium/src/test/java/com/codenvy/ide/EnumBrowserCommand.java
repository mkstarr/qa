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
package com.codenvy.ide;

/**
 * @author Ann Zhuleva
 *         <p/>
 *         selenium.typeKeys() and selenium.keyPressNative() methods properly in the CodeMirror and TextItems of
 *         DynamicForms. Use IE_EXPLORE_PROXY("*iexploreproxy") instead.
 */
public enum EnumBrowserCommand {
    /*FIREFOX("*firefox"),
    MOCK("*mock"),
    FIREFOX_PROXY("*firefoxproxy"),
    PIFIREFOX("*pifirefox"),
    CHROME("*chrome"),
    IE_EXPLORE_PROXY("*iexploreproxy"),/*
    IE_EXPLORE("*iexplore"),  commented because of Selenium doesn't perform selenium.type(),
    selenium.typeKeys() and selenium.keyPressNative() methods properly in the CodeMirror and TextItems of
    DynamicForms. Use IE_EXPLORE_PROXY("*iexploreproxy") instead.
    FIREFOX_3("*firefox3"),
    FIREFOX_2("*firefox2"),
    SAFARI_PROXY("*safariproxy"),
    GOOGLE_CHROME("*googlechrome"),
    SAFARI("*safari"),
    PIEXPLORE("*piiexplore"),
    FIREFOX_CHROME("*firefoxchrome"),
    OPERA("*opera"),
    IEHTA("*iehta"),
    CUSTOM("*custom");*/
    FIREFOX("*chrome"), GOOGLE_CHROME("*googlechrome");
    /** Value. */
    private final String value;

    /**
     * @param v
     *         value
     */
    EnumBrowserCommand(String v) {
        value = v;
    }

    /** @return String */
    @Override
    public String toString() {
        return value;
    }

    /**
     * @param v
     *         value
     * @return EnumBaseObjectTypeIds
     */
    public static EnumBrowserCommand fromValue(String v) {
        for (EnumBrowserCommand c : EnumBrowserCommand.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
}
