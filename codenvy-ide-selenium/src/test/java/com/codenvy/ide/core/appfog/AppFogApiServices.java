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
package com.codenvy.ide.core.appfog;


import com.codenvy.ide.core.exceptions.PaaSException;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Library of static methods to make direct Http requests to the AppFog PaaS through it's API.
 * AppFog API docs: https://github.com/lucperkins/appfog-api-docs
 * @author Dmytro Nochevnov
 *
 */
public class AppFogApiServices {
    private static final int SUCCESS_CODE = 200;
    private final static String URL = "https://api.appfog.com";
    
    /**
     * Similar to @see org.exoplatform.ide.extension.appfog.server.Appfog::appicationInfo()
     */
    public static String getAppicationInfo(String authToken, String applicationName) throws IOException, PaaSException {
        return getJson(URL + "/apps/" + applicationName, authToken, SUCCESS_CODE);       
    }

    /**
     * Similar to @see org.exoplatform.ide.extension.appfog.server.Appfog::deleteApplication()
     */    
    public static void deleteApplication(String authToken, String applicationName) throws IOException, PaaSException {
        deleteJson(URL + "/apps/" + applicationName, authToken, SUCCESS_CODE);
    }
    
    /**
     * Similar to @see org.exoplatform.ide.extension.appfog.server.Appfog::provisionedServices()
     */
    public static boolean isServiceExist(String authToken, String serviceName) throws PaaSException, IOException {
        // Example of server response: 
        // [{"name":"mysql132013","type":"database","vendor":"mysql","provider":"core"...}]
        return getJson(URL + "/services", authToken, SUCCESS_CODE)
            .contains(String.format("\"name\":\"%s\"", serviceName));
    }

    /**
     * Similar to @see org.exoplatform.ide.extension.appfog.server.Appfog::createService()
     */
    public static void createMySqlService(String authToken, String serviceName) throws IOException, PaaSException {
        String jsonTemplate = "{\"name\":\"%s\",\"type\":\"database\",\"vendor\":\"mysql\",\"provider\":\"core\",\"version\":\"5.1\",\"tier\":\"free\",\"infra\":{\"provider\":\"aws\",\"name\":\"aws\"}}";
        String json = String.format(jsonTemplate, serviceName);
        
        postJson(URL + "/services", authToken, json, SUCCESS_CODE);        
    }

    /**
     * Similar to @see org.exoplatform.ide.extension.appfog.server.Appfog::bindService() 
     */
    public static void boundService(String serviceName, String applicationName, String authToken) throws PaaSException, IOException {
        String applicationInfoJson = getAppicationInfo(authToken, applicationName);
        String json = applicationInfoJson.replace("\"services\":[]", "\"services\":[\"" + serviceName+ "\"]");   // add service into application info json.
        
        putJson(URL + "/apps/" + applicationName, authToken, json, SUCCESS_CODE);
    }

    /**
     * Similar to @see org.exoplatform.ide.extension.appfog.server.Appfog::deleteService()
     */
    public static void deleteService(String authToken, String serviceName) throws PaaSException, IOException { 
        deleteJson(URL + "/services/" + serviceName, authToken, SUCCESS_CODE);
    }    
    
    /**
     * The same as @see org.exoplatform.ide.extension.appfog.server.Appfog::postJson() 
     */
    private static String postJson(String url, String authToken, String body, int success) throws IOException, PaaSException  {
        return doRequest(url, "POST", authToken, body, "application/json", success);
    }

    /**
     * The same as @see org.exoplatform.ide.extension.appfog.server.Appfog::putJson()
     */
    private static String putJson(String url, String authToken, String body, int success) throws IOException, PaaSException {
        return doRequest(url, "PUT", authToken, body, "application/json", success);
    }     
    
    /**
     * The same as @see org.exoplatform.ide.extension.appfog.server.Appfog::deleteJson()
     */      
    private static String deleteJson(String url, String authToken, int success) throws IOException, PaaSException {
        return doRequest(url, "DELETE", authToken, null, null, success);
    }
    
    /**
     * The same as @see org.exoplatform.ide.extension.appfog.server.Appfog::getJson()
     */
    private static String getJson(String url, String authToken, int success) throws IOException, PaaSException {
        return doRequest(url, "GET", authToken, null, null, success);
    }
    
    /**
     * The same as @see org.exoplatform.ide.extension.appfog.server.Appfog::doRequest()
     */
    private static String doRequest(String url, String method, String authToken, String body, String contentType, int success)
        throws IOException {
        HttpURLConnection http = null;
        try {
            http = (HttpURLConnection)new URL(url).openConnection();
            http.setInstanceFollowRedirects(false);
            http.setRequestMethod(method);
            http.setRequestProperty("Authorization", authToken);
            http.setRequestProperty("Accept", "*/*");
            if (!(body == null || body.isEmpty())) {
                http.setRequestProperty("Content-type", contentType);
                http.setDoOutput(true);
                BufferedWriter writer = null;
                try {
                    writer = new BufferedWriter(new OutputStreamWriter(http.getOutputStream()));
                    writer.write(body);
                } finally {
                    if (writer != null) {
                        writer.close();
                    }
                } 
            }
            if (http.getResponseCode() != success) {
                // throw fault(http);
                throw new PaaSException(String.format("Expected: <%s>, but server returned status <%s: %s>", success,
                                                      http.getResponseCode(), http.getResponseMessage()));
            }
        
            InputStream input = http.getInputStream();
            String result;
            try {
                result = readBody(input, http.getContentLength());
            } finally {
                input.close();
            }
            return result;
        } finally {
            if (http != null) {
                http.disconnect();
            }
        }
    
    }

    /**
     * The same as @see org.exoplatform.ide.extension.appfog.server.Appfog::readBody()
     */
    private static String readBody(InputStream input, int contentLength) throws IOException {
        String body = null;
        if (contentLength > 0) {
            byte[] b = new byte[contentLength];
            int point, off = 0;
            while ((point = input.read(b, off, contentLength - off)) > 0) {
                off += point;
            }
            body = new String(b);
        } else if (contentLength < 0) {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int point;
            while ((point = input.read(buf)) != -1) {
                bout.write(buf, 0, point);
            }
            body = bout.toString();
        }
        return body;
    }

}
