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

/** @author Ann Zhuleva */
public class Response {
    private int statusCode;

    private String data;

    public Response(int statusCode, String data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    /** @return the statusCode */
    public int getStatusCode() {
        return statusCode;
    }

    /** @return the data */
    public String getData() {
        return data;
    }

}
