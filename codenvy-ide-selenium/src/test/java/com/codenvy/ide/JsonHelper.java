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

import org.exoplatform.ide.commons.ParsingResponseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/** @author Roman Iuvshin */

public class JsonHelper extends BaseTest {

    private static String json;

    private static final String FACTORY_BY_ID = PROTOCOL + "://" + IDE_HOST + "/api/factory/";

    public JsonHelper() {
    }

    public JsonHelper(String factoryUrl) {
        BufferedReader reader;
        HttpURLConnection http = null;
        try {
            http = (HttpURLConnection)new URL(FACTORY_BY_ID + factoryUrl.split("=")[1].toString()).openConnection();
            http.setRequestMethod("GET");
            InputStream in = http.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            if (reader != null) {
                reader.close();
            }
            json = sb.toString();
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } finally {
            if (http != null) {
                http.disconnect();
            }
        }
    }

    /**
     * return json key string value
     *
     * @param key
     * @return json string value
     * @throws ParsingResponseException
     */
    public String getValueByKey(String key) throws ParsingResponseException {
        return org.exoplatform.ide.commons.JsonHelper.parseJson(json).getElement(key).getStringValue();
    }

    /**
     * return string value from json by key
     *
     * @param key
     * @param json
     * @return json string value
     * @throws ParsingResponseException
     */
    public String getValueByKey(String json, String key) throws ParsingResponseException {
        return org.exoplatform.ide.commons.JsonHelper.parseJson(json).getElement(key).getStringValue();
    }

}
