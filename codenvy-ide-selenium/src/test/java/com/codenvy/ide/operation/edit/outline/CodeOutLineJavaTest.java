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
package com.codenvy.ide.operation.edit.outline;

import com.codenvy.ide.MenuCommands;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.operation.java.ServicesJavaTextFuction;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

/**
 * @author Ann Zhuleva
 * @author <a href="mailto:dmitry.ndp@gmail.com">Dmytro Nochevnov</a>
 *
 */
public class CodeOutLineJavaTest extends ServicesJavaTextFuction {
    private final static String FILE_NAME = "JavaCodeOutline.java";

    private final static String PROJECT = CodeOutLineJavaTest.class.getSimpleName();

    @BeforeClass
    public static void setUp() {
        final String filePath = "src/test/resources/org/exoplatform/ide/operation/java/calc.zip";

        try {
            Map<String, Link> project = VirtualFileSystemUtils.importZipProject(PROJECT, filePath);
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
    public void testCodeOutLineJava() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);

        openSpringJavaTetsFile();

        //TODO Pause for build outline tree
        //after implementation method for check ready state, should be remove
        Thread.sleep(2000);

        // open outline panel
        IDE.TOOLBAR.runCommand(ToolbarCommands.View.SHOW_OUTLINE);

        // wait while outline tree is loaded
        IDE.OUTLINE.waitOutlineTreeVisible();
        IDE.OUTLINE.waitHiglightBorderJavaOutlinePresent();

        // check for presence and visibility of outline tab
        IDE.OUTLINE.waitJavaOutlineViewVisible();

        // expand all outline tree
        IDE.OUTLINE.selectItem("import declarations");
        IDE.OUTLINE.expandSelectItem();
        IDE.OUTLINE.selectItem("SumController");
        IDE.OUTLINE.expandSelectItem();
        checkAllNodes();
        checkMoveToTree();
        checkMoveInEditor();

    }

    //check all expand nodes
    private void checkAllNodes() throws Exception {
        //wait last node in tree
        IDE.OUTLINE.waitItemPresent("handleRequestInternal(HttpServletRequest, HttpServletResponse)");
        IDE.OUTLINE.waitItemPresent("sumcontroller");
        IDE.OUTLINE.waitItemPresent("import declarations");
        IDE.OUTLINE.waitItemPresent("org.springframework.web.servlet.ModelAndView");
        IDE.OUTLINE.waitItemPresent("org.springframework.web.servlet.mvc.AbstractController");
        IDE.OUTLINE.waitItemPresent("javax.servlet.http.HttpServletRequest");
        IDE.OUTLINE.waitItemPresent("javax.servlet.http.HttpServletResponse");
        IDE.OUTLINE.waitItemPresent("SumController");
    }

    // check move in outline tree
    private void checkMoveToTree() throws Exception {
        IDE.OUTLINE.selectItem("org.springframework.web.servlet.ModelAndView");
        IDE.STATUSBAR.waitCursorPositionAt("2 : 1");

        IDE.OUTLINE.selectItem("org.springframework.web.servlet.mvc.AbstractController");
        IDE.STATUSBAR.waitCursorPositionAt("3 : 1");

        IDE.OUTLINE.selectItem("javax.servlet.http.HttpServletRequest");
        IDE.STATUSBAR.waitCursorPositionAt("4 : 1");

        IDE.OUTLINE.selectItem("javax.servlet.http.HttpServletResponse");
        IDE.STATUSBAR.waitCursorPositionAt("5 : 1");

        IDE.OUTLINE.selectItem("SumController");
        IDE.STATUSBAR.waitCursorPositionAt("10 : 1");

        IDE.OUTLINE.selectItem("handleRequestInternal(HttpServletRequest, HttpServletResponse)");
        IDE.STATUSBAR.waitCursorPositionAt("11 : 1");
    }

    //check move in editor and highlight elements in outline
    private void checkMoveInEditor() throws Exception {

        IDE.GOTOLINE.goToLine(2);
        IDE.OUTLINE.waitElementIsSelect("org.springframework.web.servlet.ModelAndView");

        IDE.GOTOLINE.goToLine(5);
        IDE.OUTLINE.waitElementIsSelect("javax.servlet.http.HttpServletResponse");

        IDE.GOTOLINE.goToLine(3);
        IDE.OUTLINE.waitElementIsSelect("org.springframework.web.servlet.mvc.AbstractController");

        IDE.GOTOLINE.goToLine(4);
        IDE.OUTLINE.waitElementIsSelect("javax.servlet.http.HttpServletRequest");

        IDE.GOTOLINE.goToLine(10);
        IDE.OUTLINE.waitElementIsSelect("SumController");

        IDE.GOTOLINE.goToLine(13);
        IDE.OUTLINE.waitElementIsSelect("handleRequestInternal(HttpServletRequest, HttpServletResponse)");
    }

}
