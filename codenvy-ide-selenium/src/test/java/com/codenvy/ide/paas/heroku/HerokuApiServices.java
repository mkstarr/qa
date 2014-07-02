package com.codenvy.ide.paas.heroku;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.Utils;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Musienko Maxim
 *
 */
public class HerokuApiServices {
    static {
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
    }

    private String login;
    private String password;
    private       String herokuAPIKeysListURL = "https://api.heroku.com/account/keys";
    private       String herokuAPIKEYDelURL   = "https://api.heroku.com/user/keys";
    private       String HerokuapiUrl         = "https://api.heroku.com";
    private final int    maxAppNumber         = 5;

    HerokuApiServices(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * get all ssh key list from Heroku PaaS as json
     *
     * @return
     * @throws IOException
     *         if something occurd I/O
     */
    String getHerokuKeys() throws IOException {
        StringBuilder jsonString = new StringBuilder();
        URL url = new URL(herokuAPIKeysListURL);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/vnd.heroku+json; version=3");
        String userpass = login + ":" + password;
        String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
        connection.setRequestProperty("Authorization", basicAuth);

        while (connection.getInputStream().available() != 0) {
            jsonString.append((char)connection.getInputStream().read());
        }
        connection.getInputStream().close();
        connection.disconnect();
        return jsonString.toString();
    }

    /**
     * get all applications user from Heroku side
     *
     * @throws IOException
     *         id any i/o errors occurs
     * @throws SAXException
     *         if http response wit invalid data
     */
    protected List<String> listApplications()
            throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
        HttpURLConnection http = null;
        try {
            URL url = new URL(HerokuapiUrl + "/apps");
            http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setRequestProperty("Accept", "application/xml");
            String userpass = login + ":" + password;
            String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
            http.setRequestProperty("Authorization", basicAuth);

            InputStream input = http.getInputStream();
            Document xmlDoc;
            try {
                xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
            } finally {
                input.close();
            }

            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList appNodes = (NodeList)xPath.evaluate("/apps/app", xmlDoc, XPathConstants.NODESET);
            int appLength = appNodes.getLength();
            List<String> apps = new ArrayList<String>(appLength);
            for (int i = 0; i < appLength; i++) {
                String name = (String)xPath.evaluate("name", appNodes.item(i), XPathConstants.STRING);
                apps.add(name);
            }
            return apps;
        } finally {
            if (http != null) {
                http.disconnect();
            }
        }
    }

    /** remove SSH key for heroku.com host in the IDE */
    public void removeHerokuIdeSshKey() {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(BaseTest.PROTOCOL + "://" + BaseTest.IDE_HOST + "/ide/rest/" + BaseTest.TENANT_NAME +
                              "/ssh-keys/remove?host=heroku.com&callback=__gwt_jsonp__.P12.onSuccess");
            Utils.getConnection(url);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            System.out.println("<<<<<<<<<<<Deleting-ssh-herolu-key<<<<<<<<<<<<<<<<<<<<" + connection.getResponseCode());
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    /**
     * destroy application on Heroku PaaS
     *
     * @param appName
     *         name of the  application
     */
    public void destroyApplication(String appName) {
        HttpURLConnection http = null;
        try {
            URL url = new URL(HerokuapiUrl + "/apps/" + appName);
            http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("DELETE");
            http.setRequestProperty("Accept", "application/xml, */*");
            String userpass = login + ":" + password;
            String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
            http.setRequestProperty("Authorization", basicAuth);
            if (http.getResponseCode() != 200) {
                throw new Exception("Can not delete app from Heroku side");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (http != null) {
                http.disconnect();
            }
        }
    }

    /**
     * check count of the app (on this moment max application number on Heroku 5)
     * we can not create more 5 apps, if we have 5 app - method remove first application.
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     *         if invalid parsing xml document
     * @throws XPathExpressionException
     *         if received response  with invalid xml structure
     * @throws IOException
     *         id any i/o errors occurs
     */
    public void checkOverLoadApp() throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        List<String> applicationList = listApplications();
        if (applicationList.size() == maxAppNumber)
            destroyApplication(applicationList.get(0));
    }

    /**
     * delete all a user ssh keys on heroku side
     *
     * @return if all ok response code equal 200
     * @throws IOException
     */
    int removeAllKeys() throws IOException {
        URL url = new URL(herokuAPIKEYDelURL);
        int responceCode = 0;
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("DELETE");
        connection.setRequestProperty("Accept", "application/json");
        String userpass = login + ":" + password;
        String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
        connection.setRequestProperty("Authorization", basicAuth);
        responceCode = connection.getResponseCode();
        connection.disconnect();
        return responceCode;
    }


    //TODO can be removed. We can use from HerokuApiServices class

    /** reset login for Heroku in the IDE */
    public void resetLoginState() {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(BaseTest.PROTOCOL + "://" + BaseTest.IDE_HOST + "/ide/rest/" + BaseTest.TENANT_NAME + "/heroku/logout");
            Utils.getConnection(url);
            connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.connect();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }


}
