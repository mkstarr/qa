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
package com.codenvy.ide.operation.browse;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author <a href="mailto:azatsarynnyy@exoplatform.org">Artem Zatsarynnyy</a>
 *
 */
public class ProjectsListGridTest extends BaseTest {
    private static final String PROJECT1 = ProjectsListGridTest.class.getSimpleName() + "1";

    private static final String PROJECT2 = ProjectsListGridTest.class.getSimpleName() + "2";

    @BeforeClass
    public static void setUp() {
        try {
            VirtualFileSystemUtils.createDefaultProject(PROJECT1);
            VirtualFileSystemUtils.createDefaultProject(PROJECT2);
        } catch (Exception e) {
            fail("Can't create test project");
        }
    }

    @AfterClass
    public static void tearDown() {
        try {
            VirtualFileSystemUtils.delete(PROJECT2);
        } catch (Exception e) {
            fail("Can't delete test folders");
        }
    }

    @Test
    public void projectsListTest() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitForItemInProjectList(PROJECT1);
        IDE.EXPLORER.waitForItemInProjectList(PROJECT2);
        IDE.EXPLORER.waitProjectsListGridVisible();
        int countOfProjects = IDE.EXPLORER.getProjectsCountInProjectsListGrid();

        // make sure that two test projects are present in the projects list grid
        IDE.EXPLORER.selectProjectByNameInProjectsListGrid(PROJECT1);
        IDE.EXPLORER.selectProjectByNameInProjectsListGrid(PROJECT2);

        // open project1
        IDE.OPEN.openProject(PROJECT1);
        IDE.EXPLORER.waitForItem(PROJECT1);
        IDE.LOADER.waitClosed();

        IDE.EXPLORER.waitProjectsListGridNotVisible();

        IDE.MENU.runCommand(MenuCommands.Project.PROJECT, MenuCommands.Project.CLOSE_PROJECT);
        IDE.EXPLORER.waitForItemInProjectList(PROJECT1);
        IDE.EXPLORER.waitProjectsListGridVisible();

        // delete project1
        VirtualFileSystemUtils.delete(PROJECT1);
        driver.navigate().refresh();
        IDE.EXPLORER.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitForItemInProjectList(PROJECT2);
        IDE.EXPLORER.waitProjectsListGridVisible();
        assertEquals(countOfProjects - 1, IDE.EXPLORER.getProjectsCountInProjectsListGrid());
    }

}
