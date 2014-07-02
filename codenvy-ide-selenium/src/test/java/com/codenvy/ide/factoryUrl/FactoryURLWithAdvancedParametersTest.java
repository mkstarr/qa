/*
 *
 * CODENVY CONFIDENTIAL
 * ________________
 *
 * [2012] - [2013] Codenvy, S.A.
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

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/** @author Roman Iuvshin */
public class FactoryURLWithAdvancedParametersTest extends BaseTest {

    private String encodedParameters =
            "%5B%7B%22files%22%3A%5B%22%7Bpom.xm*%2Ctest.*%7D%22%2C%22test_file.txt%22%5D" +
            "%2C%22entries%22%3A%5B%7B%22find%22%3A%22GROUP_ID%22%2C%22replace%22%3A%22Co" +
            "denvy%22%7D%2C%7B%22find%22%3A%22ARTIFACT_ID%22%2C%22replace%22%3A%22com.cod" +
            "envy.factory.sample%22%7D%2C%7B%22find%22%3A%22PROJECT_VERSION%22%2C%22repla" +
            "ce%22%3A%220.1-SNAPSHOT%22%7D%2C%7B%22find%22%3A%22THIS%20IS%20TEST%22%2C%22" +
            "replace%22%3A%22REPLACED%22%2C%22replacemode%22%3A%22text_multipass%22%7D%5D" +
            "%7D%2C%7B%22files%22%3A%5B%22src%2Fmain%2Fjava%2Fhelloworld%2FGreetingContro" +
            "ller.java%22%5D%2C%22entries%22%3A%5B%7B%22find%22%3A%22REPLACE_ME%22%2C%22r" +
            "eplace%22%3A%22REPLACED_JAVA%22%7D%2C%7B%22find%22%3A%2212345%22%2C%22replac" +
            "e%22%3A%229876%22%2C%22replacemode%22%3A%22text_multipass%22%7D%5D%7D%2C%7B%" +
            "22files%22%3A%5B%22README*%5B1-2%5D.md%22%5D%2C%22entries%22%3A%5B%7B%22find" +
            "%22%3A%22factoryUrlRepo%22%2C%22replace%22%3A%22this_is_replaced%22%2C%22rep" +
            "lacemode%22%3A%22text_multipass%22%7D%5D%7D%5D";

    private String vcsUrl = "https://github.com/exoinvitemain/parametrizedFactories.git";


    /**
     * login using oauth
     *
     * @see com.codenvy.ide.BaseTest#start()
     */
    @Override
    @Before
    public void start() {
        //Choose browser Web driver:
        switch (BROWSER_COMMAND) {
            case GOOGLE_CHROME:
                driver = new ChromeDriver();
                break;
            default:
                driver = new FirefoxDriver();
        }

        IDE = new com.codenvy.ide.IDE(WORKSPACE_URL, driver);
        try {

            driver.manage().window().maximize();
            driver.get(LOGIN_URL_VFS);
        } catch (Exception e) {
        }
    }

    @Test
    public void factoryURLWithAdvancedParametersTest() throws Exception {
        driver.get(LOGIN_URL_VFS +
                   "/factory?v=1.1&projectattributes.pname=ParametrizedFactories&vcs=git&projectattributes.ptype=Spring&vcsinfo=true" +
                   "&vcsurl=" +
                   vcsUrl + "&variables=" +
                   encodedParameters);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("ParametrizedFactories");

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("pom.xml");
        // check that values was injected
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("pom.xml");
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitContentIsPresent("<groupId>Codenvy</groupId>");
        IDE.EDITOR.waitContentIsPresent("<artifactId>com.codenvy.factory.sample</artifactId>");
        IDE.EDITOR.waitContentIsPresent("<version>0.1-SNAPSHOT</version>");
        IDE.EDITOR.closeFile("pom.xml");

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("test_file.txt");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("test_file.txt");
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitContentIsPresent("REPLACED");
        IDE.EDITOR.closeFile("test_file.txt");

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("GreetingController.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("GreetingController.java");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("REPLACED_JAVA");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("9876");
        IDE.EDITOR.closeFile("GreetingController.java");

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("README_1.md");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("README_1.md");
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitContentIsPresent("this_is_replaced");
        IDE.EDITOR.closeFile("README_1.md");

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("README_2.md");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("README_2.md");
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitContentIsPresent("this_is_replaced");
        IDE.EDITOR.closeFile("README_2.md");

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("README_3.md");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("README_3.md");
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitEditorWillNotContainSpecifiedText("this_is_replaced");
        IDE.EDITOR.closeFile("README_3.md");
    }
}
