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
package com.codenvy.ide.operation.datasource;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.git.CreateLocalBranchTest;

/**
 * @author Musienko Maxim
 *
 */
public class DatasourceConfigureTest extends BaseTest {
    private static final String        PROJECT                   = DatasourceConfigureTest.class.getSimpleName();


    private final String               webXml                    = "web.xml";

    private final String               contextFile               = "context.xml";

    private final String               newURL                    = "jdbc:mysql://172.19.11.41:3306/tester2";

    private final String               newUser                   = "user2";

    private final String               newPass                   = "pass2";

    private final String               newDBName                 = "jdbc/newCodenvy";

    private final String               driverClassName           = "com.mysql.jdbc.Driver";

    private final String               contextXmlAfterAddBase    =
                                                                   "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Context>\n    <Resource auth=\"Container\" driverClassName=\"org.hsqldb.jdbcDriver\"\n        maxActive=\"20\" maxIdle=\"20\" maxWait=\"5000\"\n        name=\"jdbc/hsql/lastaccess\" password=\"\"\n        type=\"javax.sql.DataSource\" url=\"jdbc:hsqldb:file:db/lastaccess\" username=\"sa\"/>\n    <Resource auth=\"Container\" defaultAutoCommit=\"true\"\n        driverClassName=\"com.mysql.jdbc.Driver\" initialSize=\"0\"\n        maxActive=\"30\" maxIdle=\"10\" maxOpenPreparedStatements=\"0\"\n        maxWait=\"10000\" minIdle=\"0\" name=\"jdbc/newCodenvy\"\n        password=\"pass2\" poolPreparedStatements=\"false\"\n        type=\"javax.sql.DataSource\"\n        url=\"jdbc:mysql://172.19.11.41:3306/tester2\" username=\"user2\"/>\n</Context>";
    private final String               webXmlAfterAddBase        =
                                                                   "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<web-app>\n    <display-name>Web Application Created With Codenvy IDE</display-name>\n    <resource-ref>\n        <res-ref-name>jdbc/hsql/lastaccess</res-ref-name>\n        <res-type>javax.sql.DataSource</res-type>\n        <res-auth>Container</res-auth>\n    </resource-ref>\n    <resource-ref>\n        <res-ref-name>jdbc/newCodenvy</res-ref-name>\n        <res-type>javax.sql.DataSource</res-type>\n        <res-auth>Container</res-auth>\n    </resource-ref>\n</web-app>";


    private final String               contextXmlAfterDeleteBase =
                                                                   "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Context>\n    <Resource auth=\"Container\" driverClassName=\"org.hsqldb.jdbcDriver\"\n        maxActive=\"20\" maxIdle=\"20\" maxWait=\"5000\"\n        name=\"jdbc/hsql/lastaccess\" password=\"\"\n        type=\"javax.sql.DataSource\" url=\"jdbc:hsqldb:file:db/lastaccess\" username=\"sa\"/>\n</Context>";

    private final String               webXmlAfterDeleteBase     =
                                                                   "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<web-app>\n    <display-name>Web Application Created With Codenvy IDE</display-name>\n    <resource-ref>\n        <res-ref-name>jdbc/hsql/lastaccess</res-ref-name>\n        <res-type>javax.sql.DataSource</res-type>\n        <res-auth>Container</res-auth>\n    </resource-ref>\n</web-app";


    private final String               endContent                =
                                                                   "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Context>\n    <Resource auth=\"Container\" defaultAutoCommit=\"true\"\n        driverClassName=\"com.mysql.jdbc.Driver\" initialSize=\"0\"\n        maxActive=\"30\" maxIdle=\"10\" maxOpenPreparedStatements=\"0\"\n        maxWait=\"10000\" minIdle=\"0\" name=\"jdbc/mysql-datasource\"\n        password=\"tester1\" poolPreparedStatements=\"false\"\n        type=\"javax.sql.DataSource\"\n        url=\"jdbc:mysql://172.19.11.41:3306/tester1\" username=\"tester1\"/>\n    <Resource auth=\"Container\" defaultAutoCommit=\"true\"\n        driverClassName=\"com.mysql.jdbc.Driver\" initialSize=\"0\"\n        maxActive=\"30\" maxIdle=\"10\" maxOpenPreparedStatements=\"0\"";

    List<String>                       labels                    = Arrays.asList("Name", "Url *", "Username *", "Password *",
                                                                                 "Driver Class Name *",
                                                                                 "Connection Properties", "Default Auto Commit",
                                                                                 "Default Read Only",
                                                                                 "Default Transaction Isolation", "Default Catalog",
                                                                                 "Initial Size",
                                                                                 "Max Active", "Max Idle", "Min Idle", "Max Wait",
                                                                                 "Validation Query", "Pool Prepared Statements",
                                                                                 "Max Open Prepared Statements");

    protected static Map<String, Link> project;

    @BeforeClass
    public static void before() {

        try {
            project =
                      VirtualFileSystemUtils
                                            .importZipProject(PROJECT,
                                                              "src/test/resources/org/exoplatform/ide/operation/java/DatasourceSampleProject.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT);
    }


    @Test
    public void addNewDataSourceTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        // IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        openNodesToMetaInf();
        IDE.PACKAGE_EXPLORER.selectItemInPackageExplorer(PROJECT);
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.DATASOURCE);
        IDE.DATA_SOURCE_CONFIGURE.waitConfigireViewForm();
        IDE.DATA_SOURCE_CONFIGURE.clickOnAddButton();
        IDE.DATA_SOURCE_CONFIGURE.waitAskForValueNewDBForm();
        IDE.DATA_SOURCE_CONFIGURE.typeNameOfDataSource(newDBName);
        IDE.DATA_SOURCE_CONFIGURE.clickOnOkAskForValBtn();
        IDE.DATA_SOURCE_CONFIGURE.waitAskForValueNewDBFormIsClosed();
        IDE.DATA_SOURCE_CONFIGURE.waitNameIsPresentInDataSourceList(newDBName);
        String labelsName = IDE.DATA_SOURCE_CONFIGURE.getAllTextOfLabels();
        for (String lbl : labels) {
            assertTrue(labelsName.contains(lbl));
        }
        IDE.DATA_SOURCE_CONFIGURE.typeURLField(newURL);
        IDE.DATA_SOURCE_CONFIGURE.typeUserNameField(newUser);
        IDE.DATA_SOURCE_CONFIGURE.typePasswordField(newPass);
        IDE.DATA_SOURCE_CONFIGURE.typeDriverClassField(driverClassName);
        IDE.DATA_SOURCE_CONFIGURE.clickOkBtn();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(contextFile);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitContentIsPresent(contextXmlAfterAddBase);

        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(webXml);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitContentIsPresent(webXmlAfterAddBase);

    }

    @Test
    public void removeAddedDBAndCheckContextFile() throws Exception {
        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.DATASOURCE);
        IDE.DATA_SOURCE_CONFIGURE.waitConfigireViewForm();
        IDE.DATA_SOURCE_CONFIGURE.selectDataBaseByName(newDBName);
        IDE.DATA_SOURCE_CONFIGURE.clickOnDelDBButton();
        IDE.DATA_SOURCE_CONFIGURE.waitDisappearNameDataSourceList(newDBName);
        IDE.DATA_SOURCE_CONFIGURE.clickOkBtn();
        IDE.EDITOR.waitContentIsPresent(webXmlAfterDeleteBase);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.selectTab(contextFile);
        IDE.EDITOR.waitContentIsEquals(contextXmlAfterDeleteBase);

    }


    private void openNodesToMetaInf() {
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("main");

        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("main");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("webapp");

        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("webapp");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("META-INF");

        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("META-INF");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("context.xml");

        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("WEB-INF");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("web.xml");
    }

}
