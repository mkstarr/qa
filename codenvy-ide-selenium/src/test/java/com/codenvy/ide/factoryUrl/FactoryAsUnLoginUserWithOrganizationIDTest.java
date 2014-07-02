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
import com.codenvy.ide.Utils;

import org.everrest.core.impl.provider.json.JsonValue;
import org.exoplatform.ide.commons.ParsingResponseException;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** @author Musienko Maxim */
public class FactoryAsUnLoginUserWithOrganizationIDTest extends BaseTest {

    private String orgId;

    @Override
    @Before
    public void start() {
        switch (BROWSER_COMMAND) {
            case GOOGLE_CHROME:
                driver = new ChromeDriver();
                break;
            default:
                // for starting Firefox with profile
                FirefoxProfile profile = new FirefoxProfile(); //ProfilesIni().getProfile("default");
                profile.setPreference("dom.max_script_run_time", 240);
                profile.setPreference("dom.max_chrome_script_run_time", 240);
                driver = new FirefoxDriver(profile);
        }

        IDE = new com.codenvy.ide.IDE(null, driver);
        try {

            driver.manage().window().maximize();
        } catch (Exception e) {
        }
    }

    public FactoryAsUnLoginUserWithOrganizationIDTest() {
        orgId = Utils.getOrganisationIdByName(TENANT_NAME);
    }


    private String expectedTabcontentLocator =
            "//div[@id='information']//td[@class='tabTitleText' and text()='Greeting title for nonauthenticated users']";

    private String expectedImageInTheTabLocator = "//td/img[@src='https://codenvy.com/wp-content/uploads/2013/08/factory-bootstrap.png']";


    private String expectedUserContent = "ABOUT CODENVY\n" +
                                         "Codenvy is the “Develop Instantly” company. Our continuous development cloud improves velocity " +
                                         "and control by making developer environments instant, extensible, " +
                                         "and controllable. Based on an extensible plug-in platform, " +
                                         "our SaaS developer environments include Codenvy Developer, Codenvy Enterprise, Codenvy ISV, " +
                                         "and Codenvy Affiliate. By integrating with traditional IDEs, ALM, frameworks, libraries, " +
                                         "databases, code repositories, CI, and PAAS/IAAS, Codenvy works with your processes and requires" +
                                         " no changes to get started. This simplicity of development is why Google, Atlassian, " +
                                         "Intuit and Accenture use Codenvy today.\n" +
                                         "We have 40 employees with offices in San Francisco, Luxembourg, " +
                                         "and the Ukraine. Codenvy has raised $9M financing from Toba Capital, Auriga Ventures, " +
                                         "and angels.";


    @Test
    public void factoryUrlOrganizationWelcomeAsUnLoginUser() throws Exception {
        driver.get(getFactoryUrlWithOrgId());
        IDE.LOADING_BEHAVIOR.waitLoadPageIsClosed();
        IDE.EXPLORER.waitOpened();
        checkMainItems();
        //check icon,  info tab title and expected user content for user with org id
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(expectedImageInTheTabLocator)));
        new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(expectedTabcontentLocator)));
        IDE.FACTORY_URL.waitContentIntoInformationPanelWithOrgId(expectedUserContent);

    }

    /**
     * wait main content for just cloned project
     *
     * @throws Exception
     */
    private void checkMainItems() throws Exception {
        IDE.EXPLORER.waitForItemInOpenedProjectByName("js");
        IDE.EXPLORER.waitForItemInOpenedProjectByName("styles");
        IDE.EXPLORER.waitForItemInOpenedProjectByName("index.html");
    }

    /**
     * get factory Url link on with a specified content (iconurl,  contenturl)
     * for user with org id
     *
     * @return
     * @throws org.exoplatform.ide.commons.ParsingResponseException
     *
     */
    String getFactoryUrlWithOrgId() throws ParsingResponseException {
        HttpURLConnection http = null;
        StringBuilder responceData = new StringBuilder();
        String boundary = Long.toHexString(System.currentTimeMillis());
        List<String> keyList = new ArrayList<String>();
        String factoryLink = null;
        List<String> projectLinks = new ArrayList<String>();
        try {
            String lOGIN_URL =
                    BaseTest.LOGIN_URL_VFS + "/api/factory?token=" + Utils.getRootLoginToken();
            String gitUrl = "https://github.com/exoinvitemain/jsTestProject.git";

            String welcomeAsLoginUserParams = "\"welcome\":{" +
                                              "\"authenticated\":{" +
                                              "\"title\":\"Greeting title for authenticated users\"," +
                                              "\"iconurl\":\"https://codenvy.com/wp-content/uploads/2014/01/icon-android.png\"," +
                                              "\"contenturl\":\"https://codenvy.com/privacy\"" +
                                              "},";
            String welcomeAsNotLoginUserParams = "\"nonauthenticated\":{" +
                                                 "\"title\":\"Greeting title for nonauthenticated users\"," +
                                                 "\"iconurl\":\"https://codenvy.com/wp-content/uploads/2013/08/factory-bootstrap.png\"," +
                                                 "\"contenturl\":\"https://codenvy.com/about\"" +
                                                 "}";
            String welcomeParams = welcomeAsLoginUserParams + welcomeAsNotLoginUserParams;

            String jsonData =
                    "{\"v\":\"1.1\",\"vcs\":\"git\",\"vcsurl\":\"" + gitUrl + "\",\"orgid\":" + orgId + "," + welcomeParams +
                    "}}";

            String multipartRequest = "------" + boundary + "\r\n" +
                                      "Content-Disposition: form-data; name=\"factoryUrl\"\r\n" +
                                      "\r\n" +
                                      jsonData +
                                      "\r\n" +
                                      "------" + boundary + "--";
            http = (HttpURLConnection)new URL(lOGIN_URL).openConnection();
            http.setRequestMethod("POST");
            http.setAllowUserInteraction(false);
            http.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + "----" + boundary);
            http.setDoOutput(true);
            OutputStream output = http.getOutputStream();
            output.write((multipartRequest).getBytes());
            output.close();

            while (http.getInputStream().available() != 0) {
                responceData.append((char)http.getInputStream().read());
            }

            JsonValue jsonArrayLinks = org.exoplatform.ide.commons.JsonHelper.parseJson(responceData.toString()).getElement("links");
            Iterator<JsonValue> iter = jsonArrayLinks.getElements();
            while (iter.hasNext()) {
                JsonValue link = iter.next();
                if ("create-project".equals(link.getElement("rel").getStringValue())) {
                    factoryLink = link.getElement("href").getStringValue();
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (http != null) {
                http.disconnect();
            }
        }
        return factoryLink;
    }


}
