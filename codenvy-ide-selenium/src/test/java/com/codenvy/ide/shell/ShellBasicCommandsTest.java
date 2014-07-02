/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
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
package com.codenvy.ide.shell;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Response;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Roman Iuvshin
 *
 */
public class ShellBasicCommandsTest extends BaseTest {

    private static final String PROJECT = ShellBasicCommandsTest.class.getSimpleName();

    private static final String FILE_CONTENT =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<project xmlns=\"http://maven.apache.org/POM/4.0.0\" " +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache" +
            ".org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n   <modelVersion>4.0.0</modelVersion>\n   " +
            "<groupId>cloud-ide</groupId>\n   <artifactId>spring-demo</artifactId>\n   <packaging>war</packaging>\n  " +
            " <version>1.0</version>\n   <name>SpringDemo</name>\n   <properties>\n      <maven.compiler.source>1" +
            ".6</maven.compiler.source>\n      <maven.compiler.target>1.6</maven.compiler.target>\n   </properties>\n" +
            "   <dependencies>\n      <dependency>\n         <groupId>javax.servlet</groupId>\n         " +
            "<artifactId>servlet-api</artifactId>\n         <version>2.5</version>\n         " +
            "<scope>provided</scope>\n      </dependency>\n      <dependency>\n         <groupId>org" +
            ".springframework</groupId>\n         <artifactId>spring-webmvc</artifactId>\n         <version>3.0.5" +
            ".RELEASE</version>\n      </dependency>\n      <dependency>\n         <groupId>junit</groupId>\n        " +
            " <artifactId>junit</artifactId>\n         <version>3.8.1</version>\n         <scope>test</scope>\n      " +
            "</dependency>\n   </dependencies>\n   <build>\n      <finalName>greeting</finalName>\n   " +
            "</build>\n</project>";

    @BeforeClass
    public static void setUp() throws Exception {
        final String filePath = "src/test/resources/org/exoplatform/ide/operation/java/JavaCommentsTest.zip";

        try {
            Map<String, Link> project = VirtualFileSystemUtils.importZipProject(PROJECT, filePath);
        } catch (Exception e) {
        }
    }

    @Test
    public void basicCommandsTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.SHELL.setIDEWindowHandle(driver.getWindowHandle());
        IDE.SHELL.callShellFromIde();
        // test ls command
        IDE.SHELL.typeAndExecuteCommand("ls");
        IDE.SHELL.waitContainsTextInShell(PROJECT);
        // test cd command
        IDE.SHELL.typeAndExecuteCommand("cd " + PROJECT);
        IDE.SHELL.waitContainsTextInShell(PROJECT + "$");

        IDE.SHELL.typeAndExecuteCommand("ls");
        IDE.SHELL.waitContainsTextInShell("src  pom.xml");
        // test pwd command
        IDE.SHELL.typeAndExecuteCommand("pwd");
        IDE.SHELL.waitContainsTextInShell("/ShellBasicCommandsTest");
        IDE.SHELL.typeAndExecuteCommand("cat pom.xml");
        IDE.SHELL.waitContainsTextInShell(FILE_CONTENT);
        // creation folder
        IDE.SHELL.typeAndExecuteCommand("mkdir");
        IDE.SHELL.waitContainsTextInShell("mkdir: missing folder name");
        IDE.SHELL.typeAndExecuteCommand("mkdir new_folder");
        IDE.SHELL.typeAndExecuteCommand("ls");
        IDE.SHELL.waitContainsTextInShell("new_folder  src  pom.xml");
        // check that folder exists on vfs
        Response response = VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT + "/new_folder");
        assertEquals(200, response.getStatusCode());
        // check rm command
        IDE.SHELL.typeAndExecuteCommand("clear");
        IDE.SHELL.typeAndExecuteCommand("rm new_folder");
        IDE.SHELL.typeAndExecuteCommand("ls");
        IDE.SHELL.waitContainsTextInShell("src  pom.xml");
        // check that folder does not exists on vfs
        Response response2 = VirtualFileSystemUtils.get(REST_URL + "itembypath/" + PROJECT + "/new_folder");
        assertEquals(404, response2.getStatusCode());
    }

    @AfterClass
    public static void TearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
            fail("Can't create test folders");
        }
    }
}
