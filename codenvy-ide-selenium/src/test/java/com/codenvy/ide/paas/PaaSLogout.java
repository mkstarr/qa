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
package com.codenvy.ide.paas;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.Utils;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;

// TODO move to the utils.pass package

/**
 * This class is devoted to logout from PaaS at Codenvy side by sending logout request to Codenvy REST services which are described in extensions org.exoplatform.ide.extension.{paas}.server.rest.{paas}Service
 * @author Dmytro Nochevnov
 *
 */
public class PaaSLogout {
    static {
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
    }
    
    private enum LogoutFromPaaSUrl {
        APP_FOG(BaseTest.PROTOCOL + "://" + BaseTest.IDE_HOST + "/ide/rest/" + BaseTest.TENANT_NAME + "/appfog/logout?server=https%3A%2F%2Fapi.appfog.com"),
        HEROKU(BaseTest.PROTOCOL + "://" + BaseTest.IDE_HOST + "/ide/rest/" + BaseTest.TENANT_NAME + "/heroku/logout"),
        AWS_BEANSTALK(BaseTest.PROTOCOL + "://" + BaseTest.IDE_HOST + "/ide/rest/" + BaseTest.TENANT_NAME + "/aws/beanstalk/logout"),
        AWS_EC2(BaseTest.PROTOCOL + "://" + BaseTest.IDE_HOST + "/ide/rest/" + BaseTest.TENANT_NAME + "/aws/ec2/logout"),        
        AWS_S3(BaseTest.PROTOCOL + "://" + BaseTest.IDE_HOST + "/ide/rest/" + BaseTest.TENANT_NAME + "/aws/s3/logout");        
        
        private String url;
        
        LogoutFromPaaSUrl(String url) {
            this.url = url;            
        }
        
        public String getUrl() {
            return url;
        }
    }
    
    public static void logoutFromAppFog() throws IOException {        
        doRequest(LogoutFromPaaSUrl.APP_FOG.getUrl(), "POST", 204);
    }

    public static void logoutFromHeroku() throws IOException {        
        doRequest(LogoutFromPaaSUrl.HEROKU.getUrl(), "POST", 204);
    }
    
    public static void logoutFromAws() throws IOException {
        doRequest(LogoutFromPaaSUrl.AWS_BEANSTALK.getUrl(), "POST", 204);
        doRequest(LogoutFromPaaSUrl.AWS_EC2.getUrl(), "POST", 204);
        doRequest(LogoutFromPaaSUrl.AWS_S3.getUrl(), "POST", 204);
    }
    
    private static void doRequest(String urlString, String httpMethod, int expectedCode) throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            Utils.getConnection(url);
            connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod(httpMethod);
            connection.connect();
            
            if (connection.getResponseCode() != expectedCode) {
                throw new IOException(String.format("Expected response code: <%s>, but server returned status <%s: %s>",
                                          expectedCode, connection.getResponseCode(), connection.getResponseMessage()));
            }
            connection.disconnect();
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }
}
