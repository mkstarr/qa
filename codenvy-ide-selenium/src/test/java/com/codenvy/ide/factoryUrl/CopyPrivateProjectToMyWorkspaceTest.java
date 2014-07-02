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
 * herein are proprietary to Codenvy S.A.typeTextIntoEditor(Keys.CONTROL
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.factoryUrl;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.Utils;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.everrest.core.impl.provider.json.JsonValue;
import org.exoplatform.ide.commons.ParsingResponseException;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** @author Musienko Maxim */

public class CopyPrivateProjectToMyWorkspaceTest extends BaseTest {
    private static String URLToPrivateRepoWs;
    private static final String PROJECT             = "null";
    private              String discoveryTabMessage = "Welcome back!\n" +
                                                      "\n" +
                                                      "\n" +
                                                      "This project has been factory-created in a temporary workspace.\n" +
                                                      "\n" +
                                                      "You have full code, build, run and deploy capabilities in this temporary workspace" +
                                                      ".\n" +
                                                      "\n" +
                                                      "This workspace, and its contents, will be deleted if you are inactive or close the" +
                                                      " browser.\n" +
                                                      "\n" +
                                                      "Your work can be persisted to your named workspace. We will move all projects from" +
                                                      " this temporary workspace into your permanent workspace.\n" +
                                                      "\n" +
                                                      "Terms of Service";

    @AfterClass
    public static void tearDown() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void copyPrivareLoginAsLoginUserTest() throws Exception {
        driver.get(getFactoryUrlFromPrivateRepo());
        IDE.LOADING_BEHAVIOR.waitLoadPageIsClosed();
        URLToPrivateRepoWs = driver.getCurrentUrl();
        String currentWin = driver.getWindowHandle();
        IDE.FACTORY_URL.waitWelcomeIframe();
        if (IDE.GITHUB.checkExistAutorizedAppByName(BaseTest.IDE_HOST)) {
            IDE.ASK_DIALOG.waitOpened();
            IDE.ASK_DIALOG.clickYes();
            waitOpenedGithubWin();
            switchToNonCurrentWindow(currentWin);
            IDE.GITHUB.waitAuthorizationPageOpened();
            IDE.GITHUB.waitAuthorizationPageOpened();
            IDE.GITHUB.typeLogin(USER_NAME);
            IDE.GITHUB.typePass(USER_PASSWORD);
            IDE.GITHUB.clickOnSignInButton();
            IDE.GITHUB.clickOnAuthorizeBtn();
            driver.switchTo().window(currentWin);
        }
        IDE.FACTORY_URL.waitWelcomeIframeAndGotoMainFrame();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("codenvy-ide-selenium");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("codenvy-cloud-ide-selenium");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        IDE.FACTORY_URL.waitContentIntoInformationPanel(discoveryTabMessage);
        IDE.FACTORY_URL.waitCopyToMyWorkspaceButton();
        IDE.FACTORY_URL.clickOnCopyToMyWorkspaceButton();
        if (IDE.SELECT_WORKSPACE.isSelectWorkspacePageOpened() == true) {
            IDE.SELECT_WORKSPACE.waitWorkspaceInSelectWorkspacePage(TENANT_NAME);
            IDE.SELECT_WORKSPACE.clickOnWorkspaceName(TENANT_NAME);
        }
        IDE.LOADING_BEHAVIOR.waitLoadPageIsClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("codenvy-ide-selenium");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("codenvy-cloud-ide-selenium");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
    }

    @Test
    public void checkPrivateTemporaryWsAsLoginUser() throws Exception {
        driver.manage().deleteAllCookies();
        driver.get(LOGIN_URL);
        IDE.LOGIN.loginAsTenantUser();
        IDE.SELECT_WORKSPACE.clickOnWorkspaceName(ADDITIONAL_TENANT_NAME);
        IDE.EXPLORER.waitOpened();
        driver.get(URLToPrivateRepoWs);
        checkReadOnlyStatus();
        IDE.EXPLORER.waitForItemNotVisible("codenvy-ide-selenium");
        IDE.EXPLORER.waitForItemNotVisible("codenvy-cloud-ide-selenium");
    }


    /** wait while hithub autorization window will open */
    public void waitOpenedGithubWin() {
        IDE.DEBUGER.waitOpenedSomeWin();
    }

    /**
     * get factory Url link on private repository with IDE REST
     *
     * @return
     * @throws ParsingResponseException
     */
    String getFactoryUrlFromPrivateRepo() throws ParsingResponseException {
        HttpURLConnection http = null;
        StringBuilder responceData = new StringBuilder();
        String boundary = Long.toHexString(System.currentTimeMillis());
        List<String> keyList = new ArrayList<String>();
        String factoryLink = null;
        List<String> projectLinks = new ArrayList<String>();
        try {
            String lOGIN_URL =
                    BaseTest.LOGIN_URL_VFS + "/api/factory?token=" + Utils.getRootLoginToken();
            String gitUrl = "https://github.com/exoinvitemain/qa.git";
            String jsonData = "{\"v\":\"1.1\",\"vcs\":\"git\",\"vcsurl\":\"" + gitUrl + "\"}";
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

            for (String projectLink : projectLinks) {
                System.out.println("****:" + projectLink);
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

    /** check read only status menu */
    void checkReadOnlyStatus() {
        IDE.READ_ONLY_MODE.waitReadonlyIndicator();
        // checking visible commands
        IDE.MENU.waitCommandVisible(MenuCommands.File.FILE, MenuCommands.File.OPEN_FILE_BY_PATH);
        IDE.MENU.waitCommandVisible(MenuCommands.File.FILE, MenuCommands.File.DOWNLOAD);
        IDE.MENU.waitCommandVisible(MenuCommands.File.FILE, MenuCommands.File.DOWNLOAD_ZIPPED_FOLDER);
        IDE.MENU.waitCommandVisible(MenuCommands.File.FILE, MenuCommands.File.CLOSE);
        IDE.MENU.waitCommandVisible(MenuCommands.File.FILE, MenuCommands.File.SEARCH);
        IDE.MENU.waitCommandVisible(MenuCommands.File.FILE, MenuCommands.File.REFRESH);

        // checking that other commands are invivisible
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.RENAME);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.NEW);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.UPLOAD_FILE);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.UPLOAD_FOLDER);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.OPEN_FILE_BY_URL);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.OPEN_LOCAL_FILE);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.SAVE);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.SAVE_AS);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.SAVE_ALL);
        IDE.MENU.waitCommandInvisible(MenuCommands.File.FILE, MenuCommands.File.DELETE);

    }
}


