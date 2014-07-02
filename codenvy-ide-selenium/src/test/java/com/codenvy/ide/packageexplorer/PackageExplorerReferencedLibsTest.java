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
package com.codenvy.ide.packageexplorer;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.util.Map;

/**
 * @author Roman Iuvshin
 *
 */
public class PackageExplorerReferencedLibsTest extends BaseTest {
    private static final String PROJECT = "RefLibsPrj";

    private static final String DEPENDENCY =
            "\t<dependency>\n\t\t<groupId>junit</groupId>\n\t\t<artifactId>junit</artifactId>\n\t\t<version>4" +
            ".10</version>\n\t\t<scope>test</scope>\n\t</dependency>";

    final String filePath = "src/test/resources/org/exoplatform/ide/operation/java/JavaCommentsTest.zip";

    static Map<String, Link> project;

    @BeforeClass
    public static void setUp() {
        final String filePath = "src/test/resources/org/exoplatform/ide/operation/java/JavaCommentsTest.zip";

        try {
            project = VirtualFileSystemUtils.importZipProject(PROJECT, filePath);
        } catch (Exception e) {
        }
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
        }
    }

    @Test
    public void packageCreationTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("Maven Dependencies");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("Maven Dependencies");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("servlet-api-2.5.jar");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("spring-webmvc-3.0.5.RELEASE.jar");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("junit-3.8.1.jar");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorerIsNotPresent("junit-4.10.jar");

        //adding dependency in pom
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("pom.xml");
        IDE.EDITOR.waitActiveFile();
        IDE.GOTOLINE.goToLine(31);
        IDE.EDITOR.typeTextIntoEditor(Keys.ENTER.toString());
        IDE.EDITOR.typeTextIntoEditor(Keys.ARROW_UP.toString());
        IDE.EDITOR.typeTextIntoEditor(DEPENDENCY);
        IDE.EDITOR.typeTextIntoEditor(Keys.CONTROL.toString() + "s");
        IDE.LOADER.waitClosed();

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("junit-4.10.jar");
    }
}
