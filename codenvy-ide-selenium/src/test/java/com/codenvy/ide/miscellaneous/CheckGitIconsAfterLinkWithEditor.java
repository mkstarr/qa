package com.codenvy.ide.miscellaneous;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.core.Git;
import com.codenvy.ide.git.GitServices;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

/** @author Musienko Maxim */
public class CheckGitIconsAfterLinkWithEditor extends BaseTest {

    protected static Map<String, Link> project;

    private final String collabEditorFile = "GreetingController.java";

    private final String codemirrorEditorFile1 = "pom.xml";

    private final String codemirrorEditorFile2 = "index.jsp";

    static final String PROJECT = "IconsCheck";

    private GitServices girserviceInatance = new GitServices();

    @BeforeClass
    public static void before() {
        try {
            project = VirtualFileSystemUtils.importZipProject(PROJECT, "src/test/resources/org/exoplatform/ide/git/gitSampleProject.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void checkWithCollabEditorFileInPkgExp() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        girserviceInatance.openNodesAndCheckIconsInDefaultSpringProject(PROJECT);
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(collabEditorFile);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.PROGRESS_BAR.waitProgressBarControlClose();
        IDE.PACKAGE_EXPLORER.clickOnLinkWithEditorButton();
        checkStateGitIconsInPackageExplorer();


    }

    @Test
    public void checkWithNoneCollabEditorFileInPkgExp() throws Exception {
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(codemirrorEditorFile1);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitTabPresent(codemirrorEditorFile1);
        checkStateGitIconsInPackageExplorer();
        IDE.PACKAGE_EXPLORER.openItemWithDoubleClick(codemirrorEditorFile2);
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitTabPresent(codemirrorEditorFile2);
        checkStateGitIconsInPackageExplorer();
    }


    @Test
    public void checkWithNoneCollabEditorFileInProjectExp() throws Exception {
        IDE.EXPLORER.selectProjectTab(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EDITOR.selectTab(collabEditorFile);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.EXPLORER.clickOnLinkWithEditorButton();
        checkStateGitIconsInProjectExplorerAfterLinkClass();
        IDE.EDITOR.selectTab(codemirrorEditorFile1);
        IDE.EDITOR.waitActiveFile();
        checkStateGitIconsInProjectExplorerAfterLinkClass();


    }

    public void checkStateGitIconsInPackageExplorer() {
        // checking icons
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon(PROJECT, Git.GitIcons.ROOT);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("src/main/java", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("src", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("pom.xml", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithoutGitIcon("Maven Dependencies", Git.GitIcons.IN_REPOSITORY);

        // check that nested nodes also has git icons
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("main", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("webapp", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("WEB-INF", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("index.jsp", Git.GitIcons.IN_REPOSITORY);

        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("helloworld", Git.GitIcons.IN_REPOSITORY);
        IDE.PACKAGE_EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.IN_REPOSITORY);
    }

    public void checkStateGitIconsInProjectExplorerAfterLinkClass() {
        IDE.EXPLORER.waitElementWithGitIcon(PROJECT, Git.GitIcons.ROOT);
        IDE.EXPLORER.waitElementWithGitIcon("src", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("main", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("java", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("helloworld", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("GreetingController.java", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("webapp", Git.GitIcons.IN_REPOSITORY);
        IDE.EXPLORER.waitElementWithGitIcon("pom.xml", Git.GitIcons.IN_REPOSITORY);

    }


}