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
package com.codenvy.ide.core.aws;


import com.codenvy.ide.BaseTest;
import com.codenvy.ide.core.exceptions.PaaSException;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Library of static methods to make direct requests to the AWS PaaS through Rest API of Codenvy AWS extension.
 *
 * @author Dmytro Nochevnov
 */
public class CodenvyAwsExtensionApiServices {
    private final static String BEANSTALK_URL =
            BaseTest.PROTOCOL + "://" + BaseTest.IDE_HOST + "/ide/rest/" + BaseTest.TENANT_NAME + "/aws/beanstalk/";
    private final static String EC2_URL       =
            BaseTest.PROTOCOL + "://" + BaseTest.IDE_HOST + "/ide/rest/" + BaseTest.TENANT_NAME + "/aws/ec2/";
    private final static String S3_URL        =
            BaseTest.PROTOCOL + "://" + BaseTest.IDE_HOST + "/ide/rest/" + BaseTest.TENANT_NAME + "/aws/s3/";

    private final static String DELETE_APPLICATION_VERSION_REQUEST_TEMPLATE =
            "{\"deleteS3Bundle\":true,\"applicationName\":\"%s\",\"versionLabel\":\"%s\"}";

    public static void checkApplicationExists(String vfsId, String projectId) throws IOException, PaaSException {
        String url = String.format(BEANSTALK_URL + "apps/info?vfsid=%s&projectid=%s", vfsId, projectId);
        doRequest(url, "GET", null, null, 200);
    }

    public static void terminateEnvironment(String environmentName) throws IOException, PaaSException {
        String url = String.format(BEANSTALK_URL + "environments/stop/%s", environmentName);
        doRequest(url, "GET", null, null, 200);
    }

    public static void deleteApplication(String vfsId, String projectId) throws IOException, PaaSException {
        String url = String.format(BEANSTALK_URL + "apps/delete/?vfsid=%s&projectid=%s", vfsId, projectId);
        doRequest(url, "POST", null, null, 200);
    }

    public static void deleteApplicationVersion(String versionLabel, String vfsId, String projectId, String applicationName)
            throws PaaSException, IOException {
        String url = String.format(BEANSTALK_URL + "apps/versions/delete/?vfsid=%s&projectid=%s", vfsId, projectId);
        String json = String.format(DELETE_APPLICATION_VERSION_REQUEST_TEMPLATE, applicationName, versionLabel);

//        System.out.println("[INFO] deleteApplicationVersion url=" + url + "| json=" + json);

        doRequest(url, "POST", json, "application/json", 204);
    }

    public static void deleteS3Bucket(String bucketName) throws IOException, PaaSException {
        String url = String.format(S3_URL + "buckets/delete/%s", bucketName);
        doRequest(url, "POST", null, null, 200);
    }

    public static void deleteS3Object(String objectName) throws IOException, PaaSException {
        String url = String.format(S3_URL + "objects/delete/%s", objectName);
        doRequest(url, "POST", null, null, 200);
    }


    /** The same as @see org.exoplatform.ide.extension.appfog.server.Appfog::doRequest() */
    private static String doRequest(String url, String method, String body, String contentType, int success)
            throws IOException, PaaSException {
        HttpURLConnection http = null;
        try {
            http = (HttpURLConnection)new URL(url).openConnection();
            http.setInstanceFollowRedirects(false);
            http.setRequestMethod(method);
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
                throw new PaaSException(String.format("Expected: <%s>, but server returned status <%s: %s>.", success,
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

    /** The same as @see org.exoplatform.ide.extension.appfog.server.Appfog::readBody() */
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
