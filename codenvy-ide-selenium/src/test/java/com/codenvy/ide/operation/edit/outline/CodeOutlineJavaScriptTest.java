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
package com.codenvy.ide.operation.edit.outline;

import com.codenvy.ide.MimeType;
import com.codenvy.ide.TestConstants;
import com.codenvy.ide.ToolbarCommands;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.operation.autocompletion.CodeAssistantBaseTest;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Musienko Maxim
 *
 */

public class CodeOutlineJavaScriptTest extends CodeAssistantBaseTest {

    private final static String FILE_NAME = "TestJavaScriptFile.js";

    @BeforeClass
    public static void setUp() {
        try {
            createProject(CodeOutlineJavaScriptTest.class.getSimpleName());
            VirtualFileSystemUtils.createFileFromLocal(project.get(Link.REL_CREATE_FILE), FILE_NAME,
                                                       MimeType.APPLICATION_JAVASCRIPT,
                                                       "src/test/resources/org/exoplatform/ide/operation/edit/outline/"
                                                           + FILE_NAME);
        } catch (Exception e) {
        }
    }

    @Before
    public void openFile() throws Exception {
        openProject();
        IDE.EXPLORER.waitForItem(projectName + "/" + FILE_NAME);
        IDE.EXPLORER.openItem(projectName + "/" + FILE_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
    }

    // TODO After fix https://jira.codenvycorp.com/browse/IDE-2849
    // we will should be check all nodes in outline, move in outline tree, move into java script editor 
    
    @Test
    public void testCodeOutlineJavaScript() throws Exception {
        IDE.TOOLBAR.runCommand(ToolbarCommands.View.SHOW_OUTLINE);
        IDE.OUTLINE.waitOpened();
        IDE.OUTLINE.waitItemPresent("createXmlHttpRequest()");
        IDE.OUTLINE.waitItemPresent("downloadUrl(url, type, data, callback)");
        IDE.OUTLINE.waitItemPresent("downloadScript(url)");
        IDE.OUTLINE.waitItemPresent("highlightUser(me, div)");
        IDE.OUTLINE.waitItemPresent("nodesWithUris");
        IDE.OUTLINE.waitItemPresent("fixlinks()");
        IDE.OUTLINE.waitItemPresent("makeLinks(baseNode)");
        IDE.OUTLINE.waitItemPresent("getNodesWithUris(node)");
        expandAllOutlineNodes();
    }


    private void expandAllOutlineNodes() throws Exception {
        // expand 2 node and wait sub node
        IDE.OUTLINE.expandItem("downloadUrl(");
        IDE.OUTLINE.waitItemPresent("status");
        IDE.OUTLINE.waitItemPresent("request");
        // expand 3 node and wait sub node
        IDE.OUTLINE.expandItem("downloadScript(");
        IDE.OUTLINE.waitItemPresent("script");
        // expand 4 node and wait sub node
        IDE.OUTLINE.expandItem("highlightUser(");
        IDE.OUTLINE.waitItemPresent("meFinder");
        IDE.OUTLINE.waitItemPresent("highlighting");
        IDE.OUTLINE.waitItemPresent("chatLines");


    }

}