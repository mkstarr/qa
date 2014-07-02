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

import com.codenvy.ide.paas.Status;

import org.exoplatform.ide.commons.ParsingResponseException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.fail;

/**
 * Created by The eXo Platform SAS.
 *
 * @author Vitaly Parfonov
 */
public class Utils {
    static String workspaceAPIPrefix = BaseTest.LOGIN_URL_VFS + "/api/workspace/";

    public static HttpURLConnection getConnection(URL url) throws IOException {
        // login
        login();

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestProperty("Referer", url.toString());
        connection.setAllowUserInteraction(false);
        return connection;
    }

    public static String readFileAsString(String filePath) throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

    /**
     * Encode URL(Add /IDE/ to path) string in md5 hash
     *
     * @param href
     *         to encode
     * @return md5 hash of string
     */
    public static String md5(String href) {
        MessageDigest m;
        try {
            m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(href.getBytes());

            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String hashtext = bigInt.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            fail();
        }
        return "";
    }

    // login for bundles cloud-ide with https (cloudtest, cloudtest2, staging and production servers)
    // workaround: we make 5 attempts to login, until we become authorized on the server

    /** login for test servers, production server and staging */
    private static void login() {

        if (isLogged()) {
            return;
        }
        HttpURLConnection http = null;
        try {
            String lOGIN_URL =
                    BaseTest.LOGIN_URL_VFS + "/api/auth/login";
            //  System.err.println(lOGIN_URL);
            http = (HttpURLConnection)new URL(lOGIN_URL).openConnection();
            http.setRequestMethod("POST");
            http.setAllowUserInteraction(false);
            http.setRequestProperty("Content-Type", "application/json");
            http.setInstanceFollowRedirects(true);
            http.setDoOutput(true);
            OutputStream output = http.getOutputStream();
            //   System.out.println("********json********************:" + "{\"username\":\"" + BaseTest.USER_NAME + "\",\"password\":\"" +
            //                    BaseTest.USER_PASSWORD + "\"}");
            output.write(("{\"username\":\"" + BaseTest.USER_NAME + "\",\"password\":\"" + BaseTest.USER_PASSWORD + "\"}").getBytes());
            output.close();

            Map<String, List<String>> headerFields = http.getHeaderFields();
            Set<String> keySet = headerFields.keySet();

//            for (String key : keySet) {
//                List<String> vals = headerFields.get(key);
//                for (String string : vals) {
//                    System.out.println("               " + key + " :: " + string);
//                }
//            }
            http.getResponseCode();
//            System.err.println("-----------------------------------------------------");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (http != null) {
                http.disconnect();
            }
        }

    }


    /**
     * login as root user for selenium tests
     *
     * @return the token for main selenium user
     */
    public static String getRootLoginToken() {
        StringBuilder jsonStringWithTocken = new StringBuilder();
        String tocken = null;
        JsonHelper json = new JsonHelper();
        HttpURLConnection http = null;
        try {
            String lOGIN_URL =
                    BaseTest.LOGIN_URL_VFS + "/api/auth/login";
            System.err.println(lOGIN_URL);
            http = (HttpURLConnection)new URL(lOGIN_URL).openConnection();
            http.setRequestMethod("POST");
            http.setAllowUserInteraction(false);
            http.setRequestProperty("Content-Type", "application/json");
            http.setInstanceFollowRedirects(true);
            http.setDoOutput(true);
            OutputStream output = http.getOutputStream();
            output.write(("{\"username\":\"" + BaseTest.USER_NAME + "\",\"password\":\"" + BaseTest.USER_PASSWORD + "\"}").getBytes());
            output.close();
            while (http.getInputStream().available() != 0) {
                jsonStringWithTocken.append((char)http.getInputStream().read());
            }
            tocken = json.getValueByKey(jsonStringWithTocken.toString(), "value");
//
//            Map<String, List<String>> responceMap = http.getHeaderFields();
//            Set<String> keySet = responceMap.keySet();
//
//            for (List<String> strings : responceMap.values()) {
//                for (String string : strings) {
//
//                    System.out.println("<<<<<<<<<<<loginouath<<<<<<<<<<" + string);
//                }
//            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParsingResponseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            if (http != null) {
                http.disconnect();
            }
        }
        return tocken;
    }


    /**
     * login as additional user for selenium tests
     *
     * @return the token for additional user
     */
    public static String getAdditionalLoginToken() {
        StringBuilder jsonStringWithTocken = new StringBuilder();
        String tocken = null;
        JsonHelper json = new JsonHelper();
        HttpURLConnection http = null;
        try {
            String lOGIN_URL =
                    BaseTest.LOGIN_URL_VFS + "/api/auth/login";
            System.err.println(lOGIN_URL);
            http = (HttpURLConnection)new URL(lOGIN_URL).openConnection();
            http.setRequestMethod("POST");
            http.setAllowUserInteraction(false);
            http.setRequestProperty("Content-Type", "application/json");
            http.setInstanceFollowRedirects(true);
            http.setDoOutput(true);
            OutputStream output = http.getOutputStream();
            output.write(
                    ("{\"username\":\"" + BaseTest.NOT_ROOT_USER_NAME + "\",\"password\":\"" + BaseTest.USER_PASSWORD + "\"}").getBytes());
            output.close();
            while (http.getInputStream().available() != 0) {
                jsonStringWithTocken.append((char)http.getInputStream().read());
            }
            tocken = json.getValueByKey(jsonStringWithTocken.toString(), "value");
//
//            Map<String, List<String>> responceMap = http.getHeaderFields();
//            Set<String> keySet = responceMap.keySet();
//
//            for (List<String> strings : responceMap.values()) {
//                for (String string : strings) {
//
//                    System.out.println("<<<<<<<<<<<loginouath<<<<<<<<<<" + string);
//                }
//            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParsingResponseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            if (http != null) {
                http.disconnect();
            }
        }
        //  System.out.println("<<<<<<<<<<<<getted tocken<<<<<<<<<<<<<<<<<<<<<<:" + tocken);
        return tocken;
    }


    /**
     * check login user on test servers, production server and staging
     *
     * @return
     */
    private static boolean isLogged() {
        HttpURLConnection http = null;
        BufferedReader reader = null;
        try {
            http = (HttpURLConnection)new URL(BaseTest.WORKSPACE_URL).openConnection();
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
            in.close();
            return sb.toString().contains("Save As...");
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } finally {
            if (http != null) {
                http.disconnect();
            }
        }
        return false;
    }

    /**
     * @param status
     * @param exception
     * @param driver
     */
    public static void processException(Status status, Throwable exception, WebDriver driver) {
        exception.printStackTrace();
        status.setScreenshotName(Utils.captureScreenShotOnFailure(driver));
        status.setFail();
        status.setException(exception);
    }

    // TODO move this method to appropriate util class

    /**
     * Make screenshot.
     *
     * @return screenshot name
     */
    public static String captureScreenShotOnFailure(WebDriver driver) {
        return Utils.captureScreenShotOnFailure(driver, Thread.currentThread().getStackTrace());
    }

    // TODO move this method to appropriate util class

    /**
     * Make screenshot.
     *
     * @return screenshot name
     */
    public static String captureScreenShotOnFailure(WebDriver driver, StackTraceElement[] stackTrace) {
        // Get test method name
        String screenshotName = String.format("screenshot%d.png", new Date().getTime());  // set default name

        for (int i = 0; i < stackTrace.length; i++) {
            StackTraceElement testStackTraceElement = stackTrace[i];
            if (testStackTraceElement.getClassName().endsWith("Test") ||
                testStackTraceElement.getClassName().endsWith("Tst")) {
                String testMethodClass = testStackTraceElement.getClassName();
                String testMethodName = testStackTraceElement.getMethodName();
                int testMethodLineNumber = testStackTraceElement.getLineNumber();

                screenshotName = String.format("%s.%s:%d at %s.png", testMethodClass, testMethodName, testMethodLineNumber,
                                               Utils.getCurrentTimeForLog());
                break;
            }
        }

        try {
            Utils.captureScreenShotOnFailure(driver, screenshotName);
        } catch (IOException ioe) {
            // ignore problems with saving screenshot
        }

        return screenshotName;
    }

    // TODO move this method to appropriate util class
    public static void captureScreenShotOnFailure(WebDriver driver, String screenshotName) throws IOException {
        byte[] sc = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
        File parent = new File("target/screenshots");
        parent.mkdirs();
        File file = new File(parent, screenshotName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new IOException("I/O Error: Can't create screenshot file :" + file.toString());
        }
        FileOutputStream outputStream = new FileOutputStream(file);
        try {
            outputStream.write(sc);
        }
        // Closing opened file
        finally {
            try {
                //need to check for null
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                throw new IOException("I/O Error: Can't write screenshot to file :" + file.toString());
            }
        }
    }

    // TODO move this method to appropriate util class

    /**
     * Get current time string in format mm-dd-yy 5:31:31 PM
     *
     * @return
     */
    public static String getCurrentTimeForLog() {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, Locale.US).format(new Date()).replaceAll("/", "-");
    }

    // TODO move this method to appropriate util class

    /**
     * Get current time string in format "mm-dd-yy_5-31-31_pm"
     *
     * @return
     */
    public static String getCurrentTimeForProjectName() {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, Locale.US).format(new Date()).replaceAll("[/:]", "-")
                         .replaceAll(" ", "_").toLowerCase();
    }

    /**
     * Read properties from local file placed at filePath
     *
     * @param filePath
     * @return Properties
     * @throws IOException
     */
    public static Properties readProperties(String filePath) throws IOException {
        Properties properties = new Properties();

        FileInputStream propertiesFile = new FileInputStream(filePath);
        properties.load(propertiesFile);
        propertiesFile.close();

        return properties;
    }

    /**
     * stop tenant with using REST API
     * used in the IDE v. 2.14.2
     * in older versions does not work
     *
     * @param nameTenant
     */
    @Deprecated
    public static void stopTenantByOldRESRAPI(String nameTenant) {
        String restPrefixForStop = "/cloud-admin/shell/rest/private/cloud-admin/tenant-service/stop?tenant=";
        HttpURLConnection connection = null;
        try {
            URL url = new URL(BaseTest.PROTOCOL + "://" + BaseTest.IDE_HOST + restPrefixForStop + nameTenant);
            connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            String userpass = "cldadmin" + ":" + "tomcat";
            String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
            connection.setRequestProperty("Authorization", basicAuth);
            int responceCode = connection.getResponseCode();
            //  System.out.println("<<<<<<<<<<<<<<<<<<<Stop tenant responce" + nameTenant + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<:" + responceCode);
            connection.disconnect();
            if (responceCode != 200) {
                throw new RuntimeException("Can not delete current tenant");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    /**
     * delete tenant with using REST API
     * used in the IDE v. 2.14.2
     * in older versions does not work
     *
     * @param nameTenant
     */
    @Deprecated
    public static void deleteTenantByOldRESTAPI(String nameTenant) {
        stopTenantByOldRESRAPI(nameTenant);
        String restPrefixForRemove = "/cloud-admin/shell/rest/private/cloud-admin/tenant-service/remove?tenant=";

        HttpURLConnection connection = null;
        try {
            URL url = new URL(BaseTest.PROTOCOL + "://" + BaseTest.IDE_HOST + restPrefixForRemove + nameTenant);
            connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            String userpass = "cldadmin" + ":" + "tomcat";
            String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
            connection.setRequestProperty("Authorization", basicAuth);
            int responceCode = connection.getResponseCode();
            connection.disconnect();
            if (responceCode != 200) {
                throw new RuntimeException("Can not delete current tenant");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }

    }

    /**
     * check all opened Projects in the IDE
     *
     * @param driver
     *         current instanse of the Webdriver
     * @return true if project opened
     * @throws UnsupportedEncodingException
     */

    public boolean checkOpenedProjects(WebDriver driver) throws UnsupportedEncodingException {
        try {
            return
                    driver.manage().getCookieNamed("eXo-IDE-" + URLEncoder.encode(BaseTest.USER_NAME, "UTF-8") + "-opened-project_str")
                          .getValue().length() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }


    }

//-----------------------------------API-----------------------------------

    /**
     * @return admin token for working with some specified actions
     *         from the IDE Workspace API (see also https://wiki.codenvycorp.com/display/PSR/Workspace+API)
     * @throws IOException
     * @throws ParsingResponseException
     */
    public static String getAdmintokenWithWorkspaceApi() throws IOException, ParsingResponseException {
        HttpURLConnection connection = null;
        String token = null;
        try {
            String apiUrl = BaseTest.LOGIN_URL_VFS + "/api/auth/login";
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setAllowUserInteraction(false);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            OutputStream output = connection.getOutputStream();
            output.write(BaseTest.readCredential("workspase.admin").getBytes());

            if (connection.getResponseCode() != 200) {
                throw new RuntimeException(new Exception("Can not get token from the IDE API"));
            }
            output.close();
            token = org.exoplatform.ide.commons.JsonHelper.parseJson(connection.getInputStream()).getElement("value").getStringValue();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        //  System.out.println(token);
        return token;
    }

    /**
     * check existing organization by name with IDE REST API
     *
     * @param tenantName
     * @return
     * @throws IOException
     * @throws ParsingResponseException
     */
    public static boolean checkOrganizationIdByName(String tenantName) throws IOException, ParsingResponseException {
        HttpURLConnection connection = null;
        boolean isExist = false;
        try {
            String apiUrl =
                    BaseTest.LOGIN_URL_VFS + "/api/account/find?name=" + tenantName;
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection)url.openConnection();
            connection.addRequestProperty("Cookie", "session-access-key=" + getAdmintokenWithWorkspaceApi());
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                isExist = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return isExist;
    }

    /**
     * get organization id by name using the IDE Workspace API
     *
     * @param tenantName
     *         tenan name
     * @return getted id
     */

    public static String getOrganisationIdByName(String tenantName) {
        HttpURLConnection connection = null;
        String id = null;
        BufferedReader br = null;
        StringBuilder responceData = new StringBuilder();
        String line = null;

        try {
            String apiUrl =
                    BaseTest.LOGIN_URL_VFS + "/api/account/find?name=" + tenantName;
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection)url.openConnection();
            connection.addRequestProperty("Cookie", "session-access-key=" + getAdmintokenWithWorkspaceApi());
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            if (connection.getResponseCode() != 200) {
                throw new RuntimeException(new Exception("Can not get token from the IDE API"));
            }
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = br.readLine()) != null) {
                responceData.append(line);
            }
            id = org.exoplatform.ide.commons.JsonHelper.parseJson(responceData.toString()).getElement("id").getStringValue();
            connection.getInputStream().close();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return id;
    }

    /**
     * delete orginization with the IDE Workspace API
     *
     * @param tenantName
     * @return
     */
    public static void deleteOrganisationIdByName(String tenantName) throws IOException, ParsingResponseException {
        if (checkOrganizationIdByName(tenantName)) {
            System.out.println("Found account" + tenantName + " with API util");
            HttpURLConnection connection = null;
            String id = null;
            BufferedReader br = null;
            StringBuilder responceData = new StringBuilder();
            String line = null;

            try {
                String apiUrl =
                        BaseTest.LOGIN_URL_VFS + "/api/account/" + getOrganisationIdByName(tenantName);
                URL url = new URL(apiUrl);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("DELETE");
                connection.addRequestProperty("Cookie", "session-access-key=" + getAdmintokenWithWorkspaceApi());
                if (connection.getResponseCode() != 204) {
                    throw new RuntimeException(
                            new Exception("Can not delete organization from the IDE API server responce " + connection.getResponseCode()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }

    /**
     * check existing a user by email
     *
     * @param userEmail
     * @return
     */
    public static boolean checkExixtingUserByEmail(String userEmail) {
        HttpURLConnection connection = null;
        String id = null;
        BufferedReader br = null;
        StringBuilder responceData = new StringBuilder();
        boolean isExist = false;

        try {
            String apiUrl =
                    BaseTest.LOGIN_URL_VFS + "/api/user/find?email=" + userEmail;
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("Cookie", "session-access-key=" + getAdmintokenWithWorkspaceApi());
            if (connection.getResponseCode() == 200) {
                isExist = true;
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return isExist;
    }


    /**
     * get user id by maul using IDE REST API
     *
     * @param userEmail
     *         regusted user
     * @return user id
     */
    public static String getUserIdByMail(String userEmail) {
        HttpURLConnection connection = null;
        String id = null;
        BufferedReader br = null;
        StringBuilder responceData = new StringBuilder();
        String line = null;
        if (checkExixtingUserByEmail(userEmail)) {
            try {
                String apiUrl =
                        BaseTest.LOGIN_URL_VFS + "/api/user/find?email=" + userEmail;
                URL url = new URL(apiUrl);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.addRequestProperty("Cookie", "session-access-key=" + getAdmintokenWithWorkspaceApi());
                if (connection.getResponseCode() != 200) {
                    throw new RuntimeException(
                            new Exception("Can not get the requested user" + connection.getResponseCode()));
                }

                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    responceData.append(line);
                }
                id = org.exoplatform.ide.commons.JsonHelper.parseJson(responceData.toString()).getElement("id").getStringValue();
                connection.getInputStream().close();
                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } else {
            throw new IllegalStateException("Can not find the requested user");
        }
        return id;
    }


    /** selete user from codenvy IDE using e-mail */
    public static void deleteUserByEmail(String userEmail) {
        HttpURLConnection connection = null;
        StringBuilder responceData = new StringBuilder();
        String line = null;
        if (checkExixtingUserByEmail(userEmail)) {
            System.out.println("Founf user related with next email:" + userEmail);
            try {
                String apiUrl =
                        BaseTest.LOGIN_URL_VFS + "/api/user/" + getUserIdByMail(userEmail);
                URL url = new URL(apiUrl);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("DELETE");
                connection.addRequestProperty("Cookie", "session-access-key=" + getAdmintokenWithWorkspaceApi());
                if (connection.getResponseCode() != 204) {
                    throw new RuntimeException(
                            new Exception(
                                    "Can not delete requested user: + " + userEmail + "Server rsponce:" + connection.getResponseCode()));
                }

                connection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }

    /**
     * get workspace id by tenant name
     * (see also https://wiki.codenvycorp.com/display/PSR/Workspace+API)
     *
     * @param tenantName
     *         tenant name
     * @return getted id
     */
    public static String getTenanIdByName(String tenantName) {
        HttpURLConnection connection = null;
        String id = null;
        BufferedReader br = null;
        StringBuilder responceData = new StringBuilder();
        String line = null;

        try {
            String apiUrl =
                    BaseTest.LOGIN_URL_VFS + "/api/workspace?name=" + tenantName;
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection)url.openConnection();
            connection.addRequestProperty("Cookie", "session-access-key=" + getAdmintokenWithWorkspaceApi());
            connection.setRequestMethod("GET");

            connection.setDoInput(true);
            connection.setDoOutput(true);
            if (connection.getResponseCode() != 200) {
                throw new RuntimeException(
                        new Exception("Can not get token from the IDE API server responce: " + connection.getResponseCode()));
            }
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = br.readLine()) != null) {
                responceData.append(line);
            }
            //    System.out.println(responceData.toString());
            id = org.exoplatform.ide.commons.JsonHelper.parseJson(responceData.toString()).getElement("id").getStringValue();
            connection.getInputStream().close();
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return id;
    }


    /**
     * remove the tenant by name using the IDE workspace API
     *
     * @param tenantName
     */
    public static void deleteWorkSpacetByName(String tenantName) throws IOException, ParsingResponseException {
        if (checkExistWorkSpaceByName(tenantName)) {
            System.out.println("Found workspace" + tenantName + "with API util");
            HttpURLConnection connection = null;
            String id = null;
            BufferedReader br = null;
            StringBuilder responceData = new StringBuilder();
            String line = null;
            try {
                String apiUrl =
                        BaseTest.LOGIN_URL_VFS + "/api/workspace/" + getTenanIdByName(tenantName);
                URL url = new URL(apiUrl);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("DELETE");
                connection.addRequestProperty("Cookie", "session-access-key=" + getAdmintokenWithWorkspaceApi());
                if (connection.getResponseCode() != 204) {
                    throw new RuntimeException(
                            new Exception(
                                    "Can not delete account from the IDE API servere responce: " + connection.getResponseCode()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }

    /**
     * change user profile for a current user
     * (change lastName to codenvy and first name to exomain)
     * for using this method user should be authorized in the IDE
     *
     * @param sessionAccessKey
     *         access token for auntificated user
     * @param password
     *         changed password
     * @throws IOException
     * @throws ParsingResponseException
     */
    public static void restoreUserProfileInfo(String sessionAccessKey, String password) throws IOException, ParsingResponseException {
        if (checkExistWorkSpaceByName(BaseTest.TENANT_NAME)) {
            String dataForProfile = "[\n" +
                                    "        {\n" +
                                    "            \"name\": \"lastName\",   \n" +
                                    "            \"value\": \"exomain\",\n" +
                                    "  \n" +
                                    "        },\n" +
                                    "        {\n" +
                                    "            \"name\": \"firstName\",   \n" +
                                    "            \"value\": \"codenvy\",\n" +
                                    "  \n" +
                                    "        }\n" +
                                    "]";

            System.out.println("Found workspace" + BaseTest.TENANT_NAME + "with API util");
            HttpURLConnection connection = null;
            String id = null;
            BufferedReader br = null;
            StringBuilder responceData = new StringBuilder();
            String line = null;
            try {
                String apiUrl =
                        BaseTest.LOGIN_URL_VFS + "/api/profile";
                URL url = new URL(apiUrl);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.addRequestProperty("Cookie", sessionAccessKey);
                connection.setDoOutput(true);
                connection.setDoInput(true);
                OutputStream output = connection.getOutputStream();
                output.write(dataForProfile.getBytes());
                if (connection.getResponseCode() != 200) {
                    throw new RuntimeException(
                            new Exception(
                                    "Can not update first name and last name about user profile with the IDE API, servere responce: " +
                                    connection.getResponseCode()));
                }
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }


    /**
     * change password for a current user
     * for using this method user should be authorized in the IDE
     *
     * @param sessionAccessKey
     *         access token for auntificated user
     * @param password
     *         changed password
     * @throws IOException
     * @throws ParsingResponseException
     */
    public static void restoreUserPassword(String sessionAccessKey, String password) throws IOException, ParsingResponseException {
        if (checkExistWorkSpaceByName(BaseTest.TENANT_NAME)) {
            System.out.println("Found workspace" + BaseTest.TENANT_NAME + "with API util");
            HttpURLConnection connection = null;
            String id = null;
            BufferedReader br = null;
            StringBuilder responceData = new StringBuilder();
            String line = null;
            try {
                String apiUrl =
                        BaseTest.LOGIN_URL_VFS + "/api/user/password";
                URL url = new URL(apiUrl);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("POST");
                connection.addRequestProperty("Content-type", "application/x-www-form-urlencoded");
                connection.addRequestProperty("Cookie", sessionAccessKey);
                connection.setDoOutput(true);
                connection.setDoInput(true);
                OutputStream output = connection.getOutputStream();
                output.write(("password=" + password).getBytes());
                if (connection.getResponseCode() != 204) {
                    throw new RuntimeException(
                            new Exception(
                                    "Can not change password  with the IDE API, servere responce: " + connection.getResponseCode()));
                }
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
    }


    /**
     * in the selenium test we have main tenant with name 'exoinvitemain'
     * this method recreates the org ID using Wokspase API
     *
     * @return id of the just created organization
     */
    public static void restoreMainOrgId() {
        HttpURLConnection connection = null;
        String id = null;
        BufferedReader br = null;
        StringBuilder responceData = new StringBuilder();
        String line = null;
        String jsonDataForCreateOrganization = "{" +
                                               "    \"name\": \"exoinvitemain\"," +
                                               "    \"attributes\": [" +
                                               "        {" +
                                               "            \"name\": \"policy\",   " +
                                               "            \"value\": \"free\"," +
                                               "            \"description\": \"This is exoinvitemain selenium\"" +
                                               "        }" +
                                               "    ]" +
                                               "}";
        try {
            String apiUrl = BaseTest.LOGIN_URL_VFS + "/api/account";
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setAllowUserInteraction(false);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("Cookie", "session-access-key=" + getRootLoginToken());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStream output = connection.getOutputStream();
            output.write(jsonDataForCreateOrganization.getBytes());
            if (connection.getResponseCode() != 201) {
                throw new RuntimeException(
                        new Exception(
                                "Can not create organization from the IDE API, servere responce: " + connection.getResponseCode()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }


    }


    /**
     * in the selenium test we have main tenant with name 'additional.test.user'
     * this method recreates the org ID using Wokspase API
     *
     * @return id of the just created organization
     */
    public static void restoreAdditionalOrgId() {
        HttpURLConnection connection = null;
        String id = null;
        BufferedReader br = null;
        StringBuilder responceData = new StringBuilder();
        String line = null;
        String jsonDataForCreateOrganization = "{" +
                                               "    \"name\": \"additional.test.user\"," +
                                               "    \"attributes\": [" +
                                               "        {" +
                                               "            \"name\": \"policy\",   " +
                                               "            \"value\": \"free\"," +
                                               "            \"description\": \"This is additional selenium\"" +
                                               "        }" +
                                               "    ]" +
                                               "}";
        try {
            String apiUrl = BaseTest.LOGIN_URL_VFS + "/api/account";
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setAllowUserInteraction(false);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("Cookie", "session-access-key=" + getAdditionalLoginToken());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStream output = connection.getOutputStream();

            output.write(jsonDataForCreateOrganization.getBytes());
            System.out.println(",,,,,,,,,,,,,,,,,,,,,,,,," + connection.getResponseCode());
            Map<String, List<String>> responceMap = connection.getHeaderFields();
            Set<String> keySet = responceMap.keySet();

            for (List<String> strings : responceMap.values()) {
                for (String string : strings) {

                    System.out.println(string);
                }
            }
            if (connection.getResponseCode() != 201) {
                throw new RuntimeException(new Exception("Can not create organization from the IDE API"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }


    }


    /**
     * in the selenium test we have main tenant with name 'exoinvitemain'
     * this method recreates the tenant
     */
    public static void restoreMainTestTenant() {
        restoreMainOrgId();
        HttpURLConnection connection = null;
        String mainTenantJsonData = "{" +
                                    "\"name\": \"exoinvitemain\"," +
                                    "\"organizationId\":\"" + getOrganisationIdByName("exoinvitemain") + "\"," +
                                    "\"attributes\": [" +
                                    "{" +
                                    "\"name\": \"maintesttenant\"," +
                                    "\"value\": \"attributeValue\"," +
                                    "\"description\": \"main tenant for selenium test\"" +
                                    "}" +
                                    "]" +
                                    "}";
        String id = null;
        BufferedReader br = null;
        StringBuilder responceData = new StringBuilder();
        String line = null;
        try {

            String apiUrl = BaseTest.LOGIN_URL_VFS + "/api/workspace";
            //  System.out.println(apiUrl);
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setAllowUserInteraction(false);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("Cookie", "session-access-key=" + getRootLoginToken());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStream output = connection.getOutputStream();
            output.write(mainTenantJsonData.getBytes());
            //         System.out.println("****************:" + mainTenantJsonData);
            //       System.out.println("restore main tenant:" + connection.getResponseCode());

            if (connection.getResponseCode() != 201) {
                throw new RuntimeException(new Exception("Can not create main test tenant using the workspace IDE API"));
            }
            System.out.println();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }


    /**
     * in the selenium test we have main tenant with name 'additional.test.user'
     * this method recreates the tenant
     */
    public static void restoreAdditionalTestTenant() {
        restoreAdditionalOrgId();
        HttpURLConnection connection = null;
        String mainTenantJsonData = "{" +
                                    "\"name\": \"additional.test.user\"," +
                                    "\"organizationId\":\"" + getOrganisationIdByName("additional.test.user") + "\"," +
                                    "\"attributes\": [" +
                                    "{" +
                                    "\"name\": \"maintesttenant\"," +
                                    "\"value\": \"attributeValue\"," +
                                    "\"description\": \"main tenant for selenium test\"" +
                                    "}" +
                                    "]" +
                                    "}";
        String id = null;
        BufferedReader br = null;
        StringBuilder responceData = new StringBuilder();
        String line = null;
        try {

            String apiUrl = BaseTest.LOGIN_URL_VFS + "/api/workspace";
            //  System.out.println(apiUrl);
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setAllowUserInteraction(false);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("Cookie", "session-access-key=" + getAdditionalLoginToken());
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStream output = connection.getOutputStream();
            output.write(mainTenantJsonData.getBytes());
            if (connection.getResponseCode() != 201) {
                throw new RuntimeException(new Exception(
                        "Can not create main test tenant using the workspace IDE API server responce " + connection.getResponseCode()));
            }
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }


    /**
     * check existing tenant by nemw using the IDE workspace API
     *
     * @return true if exist
     */
    public static boolean checkExistWorkSpaceByName(String tenantName) throws IOException, ParsingResponseException {

        HttpURLConnection connection = null;
        boolean exist = false;
        BufferedReader br = null;
        StringBuilder responceData = new StringBuilder();
        String line = null;

        try {
            String apiUrl =
                    BaseTest.LOGIN_URL_VFS + "/api/workspace?name=" + tenantName;
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection)url.openConnection();
            connection.addRequestProperty("Cookie", "session-access-key=" + getAdmintokenWithWorkspaceApi());
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                exist = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return exist;
    }


}
