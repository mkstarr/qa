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
package com.codenvy.ide.core.openshift;

import java.util.regex.Pattern;
/**
 * @author Zaryana Dombrovskaya
 */
public class OpenShiftApiServices {
    private static final Pattern GIT_URL_PATTERN =
            Pattern.compile("ssh://(\\w+)@(\\w+)-(\\w+)\\.rhcloud\\.com/~/git/(\\w+)\\.git/");

    private String login;
    private String password;
    private String OpenShiftapiUrl          = "https://openshift.redhat.com/broker/rest/api";
    private String OpenShiftApiDomain       = "https://openshift.redhat.com/broker/rest/domains";
    private String OpenShiftApiApplicationList = "https://openshift.redhat.com/broker/rest/domains/[Domain_ID]/applications";
    private final int    maxAppNumber       = 3;

    OpenShiftApiServices(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
