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
package com.codenvy.ide.operation.java;

import com.codenvy.ide.BaseTest;

public class ServicesJavaTextFuction extends BaseTest {

    public void waitEditorIsReady() throws Exception {
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
    }

    public void waitFormatTestIsReady() throws Exception {
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
    }

    public void waitJavaCommentTestIsReady() throws Exception {
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
    }

    public void waitJavaRemoveCommentTestIsReady() throws Exception {
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
    }

    public void openSpringJavaTetsFile() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("sumcontroller");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("sumcontroller");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("SumController.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("SumController.java");
        waitFormatTestIsReady();
    }

    public void openJavaClassForFormat() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("sumcontroller");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("sumcontroller");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("SimpleSum.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("SimpleSum.java");
        waitEditorIsReady();
    }

    public void openJavaClassForUpdateDependencyTest() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("helloworld");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("helloworld");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("GreetingController.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("GreetingController.java");
        waitEditorIsReady();
    }

    public void openJavaClassInPackageExplorerFormat() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("sumcontroller");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("sumcontroller");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("SimpleSum.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("SimpleSum.java");
        waitEditorIsReady();
    }

    public void openJavaClassForFormatInAlreadyOpenedProgect() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("SimpleSum.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("SimpleSum.java");
        waitEditorIsReady();
    }

    public void openJavaCommenTest() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("commenttest");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("commenttest");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("JavaCommentsTest.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("JavaCommentsTest.java");
        waitJavaCommentTestIsReady();
    }

    public void openJavaRemoveCommenTest() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("commenttest");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("commenttest");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("JavaRemoveCommentsTest.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("JavaRemoveCommentsTest.java");
        waitJavaRemoveCommentTestIsReady();
    }

    public void expandAllNodesForCalcInPackageExplorer() throws Exception {
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("src/main/java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src/main/java");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("sumcontroller");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("sumcontroller");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("SumController.java");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("src");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("main");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("main");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("webapp");
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick("webapp");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("WEB-INF");
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer("calc.jsp");

    }

    public void expandAllNodesForCalcInProjectExplorer(String project) throws Exception {
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.EXPLORER.waitForItem(project);
        IDE.EXPLORER.waitForItem(project + "/" + "src");
        IDE.EXPLORER.waitForItem(project + "/" + "pom.xml");
        IDE.EXPLORER.openItem(project + "/" + "src");
        IDE.EXPLORER.waitForItem(project + "/" + "src" + "/" + "main");
        IDE.EXPLORER.openItem(project + "/" + "src" + "/" + "main");
        IDE.EXPLORER.waitForItem(project + "/" + "src" + "/" + "main" + "/" + "java");
        IDE.EXPLORER.waitForItem(project + "/" + "src" + "/" + "main" + "/" + "webapp");
        IDE.EXPLORER.openItem(project + "/" + "src" + "/" + "main" + "/" + "java");
        IDE.EXPLORER.waitForItem(project + "/" + "src" + "/" + "main" + "/" + "java" + "/" + "sumcontroller");
        IDE.EXPLORER.openItem(project + "/" + "src" + "/" + "main" + "/" + "java" + "/" + "sumcontroller");
        IDE.EXPLORER.waitForItem(project + "/" + "src" + "/" + "main" + "/" + "java" + "/" + "sumcontroller"
                                 + "/" + "SumController.java");
        IDE.EXPLORER.openItem(project + "/" + "src" + "/" + "main" + "/" + "webapp");
        IDE.EXPLORER.waitForItem(project + "/" + "src" + "/" + "main" + "/" + "webapp" + "/" + "WEB-INF");
        IDE.EXPLORER.waitForItem(project + "/" + "src" + "/" + "main" + "/" + "webapp" + "/" + "calc.jsp");
    }

}
