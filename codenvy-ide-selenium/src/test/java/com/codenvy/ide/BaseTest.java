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

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

import static org.junit.Assert.assertTrue;

/**
 * Created by The eXo Platform SAS.
 *
 * @author Ann Zhuleva
 * @author Evgen Vidolob
 * @author Oksana Vereshchaka
 * @author Dmytro Nochevnov
 * @author Roman Iuvshin
 */
@RunWith(RCRunner.class)
public abstract class BaseTest {

    private static boolean beforeClass = false;

    public static final ResourceBundle IDE_SETTINGS = ResourceBundle.getBundle("conf/selenium");

    public static final EnumBrowserCommand BROWSER_COMMAND = EnumBrowserCommand.valueOf(System.getProperty("browser",
                                                                                                           "GOOGLE_CHROME"));
    public static String USER_NAME;

    public static final String NOT_ROOT_USER_NAME;

    public static final String USER_PASSWORD = IDE_SETTINGS.getString("ide.test.password");

    public static final String NOT_ROOT_USER_PASSWORD = USER_PASSWORD;

    public static String FIRST_USER_FOR_INVITE;

    public static String SECOND_USER_FOR_INVITE;

    public static String THIRD_USER_FOR_INVITE;

    public static String SINGLE_USER_FOR_INVITE;

    public static String FIRST_GITHUB_USER_FOR_INVITE;

    public static String SECOND_GITHUB_USER_FOR_INVITE;

    public static String THIRD_GITHUB_USER_FOR_INVITE;

    public static String USER_FOR_FACTORY_URL_CHECK;

    public static String THIRD_TENANT_NAME;

    static {
        if (BaseTest.BROWSER_COMMAND.equals(EnumBrowserCommand.GOOGLE_CHROME)) {
            USER_NAME = IDE_SETTINGS.getString("ide.user.root.name");
            NOT_ROOT_USER_NAME = IDE_SETTINGS.getString("ide.user.dev.name");
            FIRST_USER_FOR_INVITE = IDE_SETTINGS.getString("ide.test.invite.user1");
            SECOND_USER_FOR_INVITE = IDE_SETTINGS.getString("ide.test.invite.user2");
            THIRD_USER_FOR_INVITE = IDE_SETTINGS.getString("ide.test.invite.user3");
            SINGLE_USER_FOR_INVITE = IDE_SETTINGS.getString("ide.test.invite.single");
            FIRST_GITHUB_USER_FOR_INVITE = IDE_SETTINGS.getString("ide.test.invite.git.hub.user1");
            SECOND_GITHUB_USER_FOR_INVITE = IDE_SETTINGS.getString("ide.test.invite.git.hub.user2");
            THIRD_GITHUB_USER_FOR_INVITE = IDE_SETTINGS.getString("ide.test.invite.git.hub.user3");
            USER_FOR_FACTORY_URL_CHECK = IDE_SETTINGS.getString("ide.test.factoryurl");
            THIRD_TENANT_NAME = "selenium3";
        } else {
            USER_NAME = IDE_SETTINGS.getString("ide.user.root.name_2");
            NOT_ROOT_USER_NAME = IDE_SETTINGS.getString("ide.user.dev.name_2");
            FIRST_USER_FOR_INVITE = IDE_SETTINGS.getString("ide.test.invite.user1_2");
            SECOND_USER_FOR_INVITE = IDE_SETTINGS.getString("ide.test.invite.user2_2");
            THIRD_USER_FOR_INVITE = IDE_SETTINGS.getString("ide.test.invite.user3_2");
            SINGLE_USER_FOR_INVITE = IDE_SETTINGS.getString("ide.test.invite.single_2");
            FIRST_GITHUB_USER_FOR_INVITE = IDE_SETTINGS.getString("ide.test.invite.git.hub.user1_2");
            SECOND_GITHUB_USER_FOR_INVITE = IDE_SETTINGS.getString("ide.test.invite.git.hub.user2_2");
            THIRD_GITHUB_USER_FOR_INVITE = IDE_SETTINGS.getString("ide.test.invite.git.hub.user3_2");
            USER_FOR_FACTORY_URL_CHECK = IDE_SETTINGS.getString("ide.test.factoryurl_2");
            THIRD_TENANT_NAME = "selenium3_2";
        }
    }

    public static String PROTOCOL = System.getProperty("protocol", "http");

    public static String IDE_HOST = System.getProperty("host", "codenvy-dev.com");

    public static final String TENANT_NAME = System.getProperty("tenantName", BaseTest.USER_NAME.split("@")[0]);

    public static final String ADDITIONAL_TENANT_NAME = BaseTest.NOT_ROOT_USER_NAME.split("@")[0];

    public static final String LOGIN_URL = PROTOCOL + "://" + IDE_HOST + "/site/login";

    public static String WORKSPACE_URL = PROTOCOL + "://" + IDE_HOST + "/ide/" + TENANT_NAME;

    public static String LOGIN_URL_VFS = PROTOCOL + "://" + IDE_HOST;

    public static final String REST_URL = LOGIN_URL_VFS + "/ide/rest/" + TENANT_NAME + "/vfs/v2/";

    protected static WebDriver driver;

    public static IDE IDE;

    @Before
    public void start() throws Exception {

        if (beforeClass) {
            return;
        }
        beforeClass = true;

        //Choose browser Web driver:
        switch (BROWSER_COMMAND) {
            case GOOGLE_CHROME:
//                service =
//                        new ChromeDriverService.Builder()
//                                .usingDriverExecutable(new File("src/test/resources/chromedriver"))
//                                .usingAnyFreePort().build();
//                service.start();
//                System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver");

                // For use with ChromeDriver:
                //driver = new ChromeDriver(service);
                driver = new ChromeDriver();
//                driver = new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());
                break;
            default:
                // for starting Firefox with profile
                FirefoxProfile profile = new FirefoxProfile(); //ProfilesIni().getProfile("default");
                profile.setPreference("dom.max_script_run_time", 240);
                profile.setPreference("dom.max_chrome_script_run_time", 240);
                driver = new FirefoxDriver(profile);
                ;
        }

        IDE = new IDE(LOGIN_URL, driver);
        try {

            driver.manage().window().maximize();
            driver.get(LOGIN_URL);
            waitIdeLoginPage();
            IDE.LOGIN.waitTenantAllLoginPage();
            IDE.LOGIN.tenantLogin(USER_NAME, USER_PASSWORD);
            IDE.LOADING_BEHAVIOR.waitLoadPageIsOpen();
            IDE.LOADING_BEHAVIOR.waitLoadPageIsClosed();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Read file content.
     *
     * @param filePath
     *         to read
     * @return String file content
     */
    protected String getFileContent(String filePath) {
        File file = new File(filePath);
        StringBuilder content = new StringBuilder();

        try {
            BufferedReader input = new BufferedReader(new FileReader(file));
            try {
                String line = null;

                while ((line = input.readLine()) != null) {
                    content.append(line);
                    content.append('\n');
                }
            } finally {
                input.close();
            }
        } catch (IOException e) {
            assertTrue(false);
        }

        return content.toString();
    }

    /**
     * wait load login page
     *
     * @throws Exception
     */
    public void waitIdeLoginPage() throws Exception {
        IDE.LOGIN.waitTenantLoginPage();
    }

    @AfterClass
    public static void killBrowser() throws InterruptedException {

        VirtualFileSystemUtils.checkExistedProjectsAndDelete();
        beforeClass = false;
        if (BaseTest.BROWSER_COMMAND.equals(EnumBrowserCommand.GOOGLE_CHROME)) {
            if (IDE.POPUP.isAlertPresent()) {
                IDE.POPUP.acceptAlert();
            }
            driver.quit();
            if (IDE.POPUP.isAlertPresent()) {
                IDE.POPUP.acceptAlert();
            }
        } else {
            driver.manage().deleteAllCookies();
            System.out.println("Cookies deleted.");
            if (IDE.POPUP.isAlertPresent()) {
                IDE.POPUP.acceptAlert();
            }
            driver.quit();
            if (IDE.POPUP.isAlertPresent()) {
                IDE.POPUP.acceptAlert();
            }
        }
    }

    @AfterFailure
    public void captureScreenShotOnFailure(Throwable failure) throws IOException {
        // Get test method name
        String testMethodName = null;
        for (StackTraceElement stackTrace : failure.getStackTrace()) {
            if (stackTrace.getClassName().equals(this.getClass().getName())) {
                testMethodName = stackTrace.getMethodName();
                break;
            }
        }

        byte[] sc = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES);
        File parent = new File("target/screenshots");
        parent.mkdirs();
        File file = new File(parent, this.getClass().getName() + "." + testMethodName + ".png");
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

    /** @param currentWindow */
    public void switchToNonCurrentWindow(String currentWindow) {
        for (String handle : driver.getWindowHandles()) {
            if (currentWindow.equals(handle)) {
            } else {
                driver.switchTo().window(handle);
                break;
            }
        }
    }

    /**
     * Sleep during timeout, in sec"
     *
     * @param timeoutInSec
     */
    public void sleepInTest(int timeoutInSec) {
        try {
            Thread.sleep(timeoutInSec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read credential from local file with paas credentials. Path to file had been defined in selenium.properties file in property
     * "paas.credentials.file".
     *
     * @param credentialName
     * @return
     */
    public static String readCredential(String credentialName) {
        Properties paasCredentials = null;

        try {
            paasCredentials = Utils.readProperties(IDE_SETTINGS.getString("credentials.file"));
        } catch (Exception e) {
            throw new RuntimeException(String.format("Can't read local file with PaaS credentials '%s'. %s: %s.",
                                                     IDE_SETTINGS.getString("credentials"), e.getClass().getName(),
                                                     e.getMessage()));
        }

        return paasCredentials.getProperty(credentialName);
    }
}