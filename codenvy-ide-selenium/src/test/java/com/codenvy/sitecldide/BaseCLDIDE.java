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
package com.codenvy.sitecldide;

import com.codenvy.ide.AfterFailure;
import com.codenvy.ide.BaseTest;
import com.codenvy.ide.EnumBrowserCommand;
import com.codenvy.ide.RCRunner;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ResourceBundle;

import static org.junit.Assert.assertTrue;

/**
 * @author Musienko Maxim
 *
 */
@RunWith(RCRunner.class)
public class BaseCLDIDE {

    public static final ResourceBundle IDE_SETTINGS = ResourceBundle.getBundle("conf/selenium");

    public static CLDIDE CLDIDE;

    protected static WebDriver driver;

    protected static final EnumBrowserCommand BROWSER_COMMAND = EnumBrowserCommand.valueOf(System.getProperty("browser",

                                                                                                              "GOOGLE_CHROME"));
    protected static String MAIL_BOX_LOGIN;

    protected static String TENANT;

    static {
        if (BaseTest.BROWSER_COMMAND.equals(EnumBrowserCommand.GOOGLE_CHROME)) {
            MAIL_BOX_LOGIN = IDE_SETTINGS.getString("cldide.confirm.mailbox");
            TENANT = "selenium4";
        } else {
            MAIL_BOX_LOGIN = IDE_SETTINGS.getString("cldide.confirm.mailbox_2");
            TENANT = "selenium4_2";
        }
    }

    protected static final String MAIL_BOX_PASSWORD = convertLoginToPass(MAIL_BOX_LOGIN);

    public static final String TENANT_NAME = System.getProperty("tenantName", TENANT);

    private static boolean beforeClass = false;

    @Before
    public void start() throws Exception {
        if (beforeClass) {
            return;
        }
        beforeClass = true;
        //Choose browser Web driver:
        switch (BROWSER_COMMAND) {
            case GOOGLE_CHROME:
                driver = new ChromeDriver();
                break;
            default:
                // for starting Firefox with profile
                FirefoxProfile profile = new FirefoxProfile();//ProfilesIni().getProfile("default");
                profile.setPreference("dom.max_script_run_time", 240);
                profile.setPreference("dom.max_chrome_script_run_time", 240);
                driver = new FirefoxDriver(profile);
        }

        CLDIDE = new CLDIDE(driver);
        try {

            driver.manage().window().maximize();
        } catch (Exception e) {
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

    @AfterClass
    public static void killBrowser() throws InterruptedException {
        beforeClass = false;
        if (BaseCLDIDE.BROWSER_COMMAND.equals(EnumBrowserCommand.GOOGLE_CHROME)) {
            driver.quit();
        } else {
            driver.quit();
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

    /**
     * convert login to pass
     *
     * @param login
     * @return
     */
    private static String convertLoginToPass(String login) {
        char[] pass = login.toCharArray();
        for (int i = 0; i < pass.length; i++) {
            char c = (char)(pass[i] + 2);
            pass[i] = c;
        }
        return new String(pass).substring(9);
    }


}
