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
package com.codenvy.ide.operation.autocompletion;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.Assert;

import java.io.IOException;
import java.util.Map;

/** @author Evgen Vidolob */
public abstract class CodeAssistantBaseTest extends BaseTest {

    protected static Map<String, Link> project;

    protected static String projectName;

    public static void createProject(String name, String zipPath) {
        projectName = name;
        try {
            if (zipPath == null) {
                project = VirtualFileSystemUtils.createDefaultProject(name);
            } else {
                project = VirtualFileSystemUtils.importZipProject(name, zipPath);
            }
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }

    }

    /**
     * create empty exo-project in IDE
     *
     * @param name
     * @param zipPath
     */
    public static void createExoProject(String name, String zipPath) {
        projectName = name;
        try {
            if (zipPath == null) {
                project = VirtualFileSystemUtils.createExoProject(name);
            } else {
                project = VirtualFileSystemUtils.importZipProject(name, zipPath);
            }
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }

    }

    public static void createProject(String name) {
        createProject(name, null);
    }

    /**
     * create empty eXo project
     *
     * @param name
     */
    public static void createExoPrj(String name) {
        createExoProject(name, null);
    }

    @AfterClass
    public static void deleteProject() throws IOException {
        if (project != null) {
            VirtualFileSystemUtils.deleteFolder(project.get(Link.REL_DELETE));
        }
    }

    public void openProject() throws Exception {
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(projectName);
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitForItem(projectName);
    }

    public void openJavaProject() throws Exception {
        IDE.LOADER.waitClosed();
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(projectName);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(projectName);
    }

    protected void openFile(String name) throws Exception {
        IDE.EXPLORER.waitForItem(projectName + "/" + name);
        IDE.EXPLORER.openItem(projectName + "/" + name);
        IDE.EDITOR.waitActiveFile();
    }

}
