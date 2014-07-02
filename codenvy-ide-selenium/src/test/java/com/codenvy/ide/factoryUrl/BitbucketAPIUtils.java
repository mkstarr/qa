/*
 *
 * CODENVY CONFIDENTIAL
 * ________________
 *
 * [2012] - [2014] Codenvy, S.A.
 * All Rights Reserved.
 * NOTICE: All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any. The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.factoryUrl;


import com.codenvy.ide.BaseTest;

import org.everrest.core.impl.provider.json.JsonValue;
import org.exoplatform.ide.commons.ParsingResponseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/** @author Musienko Maxim */
public class BitbucketAPIUtils {
    private String changeSetPrefix  = "/scribe-java/changesets/";
    private String keyStoreUser     = "exoinvitemain";
    private String apiUrlprefix     = "https://bitbucket.org/api/1.0/users/";
    private String repositoryPrefix = "https://bitbucket.org/api/1.0/repositories/";
    private String keysStoreUrl     = apiUrlprefix + keyStoreUser + "/ssh-keys";

    /**
     * @return list available public ssh keys from bitbucket
     * @throws IOException
     * @throws ParsingResponseException
     */
    List<String> getBitBacketPublicSshKeys() throws IOException, ParsingResponseException {
        String pass = keyStoreUser + ":" + BaseTest.readCredential("bitbucket.pass");
        List<String> keyList = new ArrayList<String>();
        BufferedReader br = null;
        StringBuilder responceKeys = new StringBuilder();
        String line = null;
        URL url = new URL(keysStoreUrl);
        HttpsURLConnection hpcon = (HttpsURLConnection)url.openConnection();
        String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(pass.getBytes());
        hpcon.setRequestProperty("Authorization", basicAuth);
        hpcon.setRequestMethod("GET");
        if (hpcon.getResponseCode() != 200) {
            hpcon.disconnect();
            throw new RuntimeException("Can not get key(s) from bitbucket ");
        }
        br = new BufferedReader(new InputStreamReader(hpcon.getInputStream()));

        while ((line = br.readLine()) != null) {
            responceKeys.append(line);
        }

        hpcon.getInputStream().close();
        hpcon.disconnect();
        Iterator<JsonValue> iter = org.exoplatform.ide.commons.JsonHelper.parseJson(responceKeys.toString()).getElements();
        while (iter.hasNext()) {
            keyList.add(iter.next().getElement("pk").getStringValue());
        }
        return keyList;
    }

    /**
     * delete ssh keys if any key is present
     *
     * @throws IOException
     * @throws ParsingResponseException
     */
    void checkAviailableBitBucketKeysAndDelete() throws IOException, ParsingResponseException {
        List<String> instKeys = getBitBacketPublicSshKeys();

        String pass = keyStoreUser + ":" + BaseTest.readCredential("bitbucket.pass");
        if (instKeys.isEmpty()) {
            return;
        } else {
            for (String key : instKeys) {

                URL url = new URL(keysStoreUrl + "/" + key);
                HttpsURLConnection hpcon = (HttpsURLConnection)url.openConnection();
                String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(pass.getBytes());
                hpcon.setRequestProperty("Authorization", basicAuth);
                hpcon.setRequestMethod("DELETE");
                if (hpcon.getResponseCode() != 204) {
                    hpcon.disconnect();
                    throw new RuntimeException("Can not delete ssh key(s) on bitbucket size");
                }

                hpcon.disconnect();
            }
        }

    }

    /**
     * @param commitMess
     *         get hash (raw node) associate with
     * @throws IOException
     * @throws ParsingResponseException
     */
    String getRawNode(String commitMess) throws IOException, ParsingResponseException {
        String pass = keyStoreUser + ":" + BaseTest.readCredential("bitbucket.pass");
        BufferedReader br = null;
        StringBuilder responceKeys = new StringBuilder();
        String line = null;
        String raw_node = null;
        URL url = new URL(repositoryPrefix + keyStoreUser + changeSetPrefix);
        HttpsURLConnection hpcon = (HttpsURLConnection)url.openConnection();
        String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(pass.getBytes());
        hpcon.setRequestProperty("Authorization", basicAuth);
        hpcon.setRequestMethod("GET");
        if (hpcon.getResponseCode() != 200) {
            hpcon.disconnect();
            throw new RuntimeException("Can not get bitbucket ");
        }
        br = new BufferedReader(new InputStreamReader(hpcon.getInputStream()));

        while ((line = br.readLine()) != null) {
            responceKeys.append(line);
        }
        hpcon.getInputStream().close();
        hpcon.disconnect();
        JsonValue changeSets = org.exoplatform.ide.commons.JsonHelper.parseJson(responceKeys.toString()).getElement("changesets");
        Iterator<JsonValue> iter = changeSets.getElements();

        while (iter.hasNext()) {
            JsonValue val = iter.next();
            if (val.getElement("message").getStringValue().trim().equals(commitMess)) {
                raw_node = val.getElement("raw_node").getStringValue();
            }


        }
        return raw_node;
    }

    /**
     * @param commitMess
     *         expected message after push
     * @return if message present after push operation in bitbucket repository
     * @throws IOException
     * @throws ParsingResponseException
     */
    boolean checkMessageIsPresentOnBitBucket(String commitMess) throws IOException, ParsingResponseException {
        boolean result = false;
        String pass = keyStoreUser + ":" + BaseTest.readCredential("bitbucket.pass");
        List<String> shangesList = new ArrayList<String>();
        BufferedReader br = null;
        StringBuilder responceKeys = new StringBuilder();
        String line = null;
        URL url = new URL(repositoryPrefix + keyStoreUser + changeSetPrefix);
        HttpsURLConnection hpcon = (HttpsURLConnection)url.openConnection();
        String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(pass.getBytes());
        hpcon.setRequestProperty("Authorization", basicAuth);
        hpcon.setRequestMethod("GET");
        if (hpcon.getResponseCode() != 200) {
            hpcon.disconnect();
            throw new RuntimeException("Can not get bitbucket ");
        }
        br = new BufferedReader(new InputStreamReader(hpcon.getInputStream()));

        while ((line = br.readLine()) != null) {
            responceKeys.append(line);
        }
        hpcon.getInputStream().close();
        hpcon.disconnect();
        JsonValue changeSets = org.exoplatform.ide.commons.JsonHelper.parseJson(responceKeys.toString()).getElement("changesets");
        Iterator<JsonValue> iter = changeSets.getElements();

        while (iter.hasNext()) {
            shangesList.add(iter.next().getElement("message").getStringValue());
        }
        for (String s : shangesList) {
            if (s.contains(commitMess)) {
                result = true;
                break;
            }

        }
        return result;
    }

    /**
     * return all diff dates as json from test project
     *
     * @param mess
     * @return
     * @throws IOException
     * @throws ParsingResponseException
     */
    String getAllDiffContentByMess(String mess) throws IOException, ParsingResponseException {
        String pass = keyStoreUser + ":" + BaseTest.readCredential("bitbucket.pass");
        List<String> shangesList = new ArrayList<String>();
        BufferedReader br = null;
        StringBuilder responceKeys = new StringBuilder();
        String line = null;
        String restUrl = "https://bitbucket.org/api/1.0/repositories/exoinvitemain/scribe-java/changesets/";
        String raw_node = getRawNode(mess);
        URL url = new URL(restUrl + raw_node + "/diff");
        HttpsURLConnection hpcon = (HttpsURLConnection)url.openConnection();
        String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(pass.getBytes());
        hpcon.setRequestProperty("Authorization", basicAuth);
        hpcon.setRequestMethod("GET");
        if (hpcon.getResponseCode() != 200) {
            hpcon.disconnect();
            throw new RuntimeException("Can not get bitbucket ");
        }
        br = new BufferedReader(new InputStreamReader(hpcon.getInputStream()));

        while ((line = br.readLine()) != null) {
            responceKeys.append(line);
        }
        return responceKeys.toString();
    }


}
