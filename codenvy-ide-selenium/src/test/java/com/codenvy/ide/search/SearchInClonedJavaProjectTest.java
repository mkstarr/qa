package com.codenvy.ide.search;

import com.codenvy.ide.VirtualFileSystemUtils;
import com.codenvy.ide.git.GitServices;

import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.io.IOException;

/**
 * @author Musienko Maxim
 */
public class SearchInClonedJavaProjectTest extends GitServices {

    private final String CLONE_URI = "git@github.com:exoinvitemain/testRepo.git";

    private static final String PROJECT = "SearchInClonedJavaProjectTest";

    private String firstFind[] = {"pom.xml", "spring-servlet.xml"};


    @AfterClass
    public static void tearDown() throws IOException {
        VirtualFileSystemUtils.delete(PROJECT);
    }

    @Test
    public void mainSearchOperationsInPckgExplorer() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.GET_STARTED_WIZARD.waitAndCloseWizard();
//        IDE.WELCOME_PAGE.close();
//        IDE.WELCOME_PAGE.waitClosed();
        cloneProject(PROJECT, CLONE_URI);
        //IDE.OPEN.openProject(PROJECT);
        IDE.PACKAGE_EXPLORER.waitPackageExplorerOpened();
        IDE.LOADER.waitClosed();
        IDE.PACKAGE_EXPLORER.waitItemInPackageExplorer(PROJECT);
        //check search by mime type
        expandSplitPanel();
        IDE.SEARCH.performSearch("/" + PROJECT, "", "text/xml");
        waitFirstSearchItems();
        IDE.SEARCH_RESULT.close();
        //check search without matches, search tree should be refreshed
        IDE.SEARCH.performSearch("/" + PROJECT, "", "text/css");
        waitSecondSearchNotFound();
        //check search by inner content
        IDE.SEARCH_RESULT.close();
        IDE.SEARCH.performSearch("/" + PROJECT, "response.sendRedirect");
        IDE.SEARCH_RESULT.waitForItemByName("index.jsp");

        //close search tab and package explorer tab and
        IDE.SEARCH_RESULT.close();
        IDE.PACKAGE_EXPLORER.closePackageExplorer();
        IDE.EXPLORER.waitForItem(PROJECT);
        // /search in folder
        IDE.EXPLORER.selectItem(PROJECT + "/src");
        IDE.SEARCH.performSearch("/" + PROJECT + "/src", "", "text/xml");
        IDE.SEARCH_RESULT.waitItemNotPresent("spring-servlet.xml");
        IDE.SEARCH_RESULT.waitItemNotPresent("web.xml");
        IDE.SEARCH_RESULT.close();
        IDE.EXPLORER.waitForItem(PROJECT);
        IDE.EXPLORER.selectItem(PROJECT + "/src");
        IDE.SEARCH.performSearch("/" + PROJECT + "/src", "java.lang.String answer");
        IDE.SEARCH_RESULT.waitItemNotPresent("hello_view.jsp");
        IDE.SEARCH_RESULT.openItemByName("hello_view.jsp");
        IDE.EDITOR.waitActiveFile();
        IDE.EDITOR.waitContentIsPresent("java.lang.String answer = (java.lang.String)request.getAttribute(\"greeting\");");
    }

    private void waitFirstSearchItems() throws Exception {
        for (String founded : firstFind) {
            IDE.SEARCH_RESULT.waitForItemByName(founded);
        }
    }

    private void waitSecondSearchNotFound() throws Exception {
        for (String founded : firstFind) {
            IDE.SEARCH_RESULT.waitItemNotPresent(founded);
        }
    }

    private void expandSplitPanel() {
        WebElement we = driver.findElement(By.cssSelector("div.gwt-SplitLayoutPanel-HDragger"));
        new Actions(driver).dragAndDropBy(we, 200, 0).build().perform();
    }

}
