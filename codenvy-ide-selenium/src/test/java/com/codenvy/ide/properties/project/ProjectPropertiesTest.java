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
package com.codenvy.ide.properties.project;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Musienko Maxim
 *
 */
public class ProjectPropertiesTest extends BaseTest {
    private static final String PROJECT                = ProjectPropertiesTest.class.getSimpleName();

    String[] ALL_PROJECT_PROPERTIES = {"Project Type\nSpring\n",
            "{\"groupID\":\"aopalliance\",\"artifactID\":\"aopalliance\",\"type\":\"jar\",\"version\":\"1.0\"}",
            "{\"groupID\":\"commons-logging\",\"artifactID\":\"commons-logging\",\"type\":\"jar\",\"version\":\"1.1.1\"}",
            "{\"groupID\":\"javax.servlet\",\"artifactID\":\"servlet-api\",\"type\":\"jar\",\"version\":\"2.5\"}",
            "{\"groupID\":\"junit\",\"artifactID\":\"junit\",\"type\":\"jar\",\"version\":\"3.8.1\"}",
            "{\"groupID\":\"org.springframework\",\"artifactID\":\"spring-aop\",\"type\":\"jar\",\"version\":\"3.0.5.RELEASE\"}",
            "{\"groupID\":\"org.springframework\",\"artifactID\":\"spring-asm\",\"type\":\"jar\",\"version\":\"3.0.5.RELEASE\"}",
            "{\"groupID\":\"org.springframework\",\"artifactID\":\"spring-beans\",\"type\":\"jar\",\"version\":\"3.0.5.RELEASE\"}",
            "{\"groupID\":\"org.springframework\",\"artifactID\":\"spring-context\",\"type\":\"jar\",\"version\":\"3.0.5.RELEASE\"}",
            "{\"groupID\":\"org.springframework\",\"artifactID\":\"spring-context-support\",\"type\":\"jar\",\"version\":\"3.0.5.RELEASE\"}",
            "{\"groupID\":\"org.springframework\",\"artifactID\":\"spring-core\",\"type\":\"jar\",\"version\":\"3.0.5.RELEASE\"}",
            "{\"groupID\":\"org.springframework\",\"artifactID\":\"spring-expression\",\"type\":\"jar\",\"version\":\"3.0.5.RELEASE\"}",
            "{\"groupID\":\"org.springframework\",\"artifactID\":\"spring-web\",\"type\":\"jar\",\"version\":\"3.0.5.RELEASE\"}",
            "{\"groupID\":\"org.springframework\",\"artifactID\":\"spring-webmvc\",\"type\":\"jar\",\"version\":\"3.0.5.RELEASE\"}",
            "Mime Type\ntext/vnd.ideproject+directory\n",
            "Jrebel\nfalse\n",
            "Target\nCloudFoundry<br>AWS<br>AppFog<br>Tier3WF\n",
            "Project Description\nSimple Spring application."};

    @BeforeClass
    public static void setUp() {
        final String filePath =
                                "src/test/resources/org/exoplatform/ide/operation/file/autocomplete/JavaTestProject.zip";

        try {
            Map<String, Link> project = VirtualFileSystemUtils.importZipProject(PROJECT, filePath);
        } catch (Exception e) {
        }
    }

    @AfterClass
    public static void TearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT);
        } catch (Exception e) {
            fail("Can't create test folders");
        }

    }

    @Test
    public void springSimpleProjectProperty() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);

        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("GreetingController.java");

        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.PROJECT_PROPERTIES);
        IDE.PROPERTIES.waitProjectPropertiesOpened();

        for (int i=0;i<ALL_PROJECT_PROPERTIES.length;i++) {
            IDE.PROPERTIES.waitProjectPropertiesContainsText(ALL_PROJECT_PROPERTIES[i]);
            System.out.println(ALL_PROJECT_PROPERTIES[i]);
        }

        IDE.PROPERTIES.selectRowInProjectProperties("Project Type");
        IDE.PROPERTIES.clickOnEditBtn();

        IDE.PROPERTIES.waitEditProjectWinView();
        IDE.PROPERTIES.waitTextIntoNameFieldEditForm("vfs:projectType");
        assertTrue(IDE.PROPERTIES.getProjectTypeValue().contains("Spring"));

        IDE.PROPERTIES.clickEditFormCancelBtn();
        IDE.PROPERTIES.waitEditDialogFormIsClosed();

        IDE.PROPERTIES.selectRowInProjectProperties("Mime Type");
        IDE.PROPERTIES.waitEditButtonIsDisabled();
        IDE.PROPERTIES.waitOkButtonIsDisabled();

        IDE.PROPERTIES.selectRowInProjectProperties("Jrebel");
        IDE.PROPERTIES.clickOnEditBtn();

        IDE.PROPERTIES.waitEditProjectWinView();
        IDE.PROPERTIES.waitTextIntoNameFieldEditForm("jrebel");
        IDE.PROPERTIES.waitProjectTypeValueIsAppear("false");

        IDE.PROPERTIES.clickEditFormCancelBtn();
        IDE.PROPERTIES.waitEditDialogFormIsClosed();


        IDE.PROPERTIES.selectRowInProjectProperties("Target");
        IDE.PROPERTIES.clickOnEditBtn();
        IDE.PROPERTIES.typeNewValue("Spring2");
        IDE.PROPERTIES.clickEditFormOkBtn();
        IDE.PROPERTIES.waitEditDialogFormIsClosed();
        IDE.PROPERTIES.waitProjectPropertiesContainsText("Spring2");
    }

}
