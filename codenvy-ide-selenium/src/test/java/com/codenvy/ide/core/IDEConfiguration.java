/*
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

import com.codenvy.ide.BaseTest;

import org.openqa.selenium.WebDriver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dmytro Nochevnov
 *
 */
public class IDEConfiguration {
    private enum Property {
        VFS_ID("-entry-point_str"),
        PROJECT_ID("-opened-project_str");
        
        String cookieSuffix;
        
        // get prefix in view of "eXo-IDE-qacodenvy7%40gmail.com"
        String COOKIE_PREFIX = "eXo-IDE-" + BaseTest.USER_NAME.replace("@", "%40");
        
        Property(String cookieSuffix) {
            this.cookieSuffix = cookieSuffix;
        }
        
        String getCookieName() {
            return COOKIE_PREFIX + cookieSuffix;
        }
    }

    public static String getWorkspaceId(WebDriver driver) {
        return getPropertyFromCookies(Property.VFS_ID.getCookieName(), driver);
    }

    public static String getOpenedProjectId(WebDriver driver) {
        return getPropertyFromCookies(Property.PROJECT_ID.getCookieName(), driver);
    }


    private static String getPropertyFromCookies(String cookieName, WebDriver driver) {       
        String cookies = driver.manage().getCookies().toString();
        
        Pattern pattern = Pattern.compile(cookieName + "=\\w*");
        Matcher matcher = pattern.matcher(cookies);
        
        if (matcher.find()) {
            String cookieValue = matcher.group().split("=")[1];        
            return cookieValue;
        }
        
        return null;
    }
}
