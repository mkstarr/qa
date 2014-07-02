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
package com.codenvy.ide.core;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/** @author Anna Shumilova */
public class SearchResult extends AbstractTestModule {
    /** @param ide */
    public SearchResult(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    private final class Locators {
        public static final String VIEW_LOCATOR = "//div[@view-id='ideSearchResultView']";

        public static final String TREE_PREFIX = "search-";

        public static final String SEARCH_RESULT_TREE = "ideSearchResultItemTreeGrid";

        public static final String TREE_GRID_ID = "//div[@id='ideSearchResultItemTreeGrid']/div/div";

        public static final String SEARCH_RESULT_SELECTOR =
                "div#" + SEARCH_RESULT_TREE + " div[id^=" + TREE_PREFIX + "]";
    }

    private final String VIEW_TITLE = "Search";

    @FindBy(xpath = Locators.VIEW_LOCATOR)
    private WebElement view;

    @FindBy(xpath = Locators.TREE_GRID_ID)
    private WebElement treeGrid;

    /** @throws InterruptedException */
    public void waitOpened() throws InterruptedException {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return view != null && view.isDisplayed();
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /**
     * Wait Search results view closed.
     *
     * @throws Exception
     */
    public void waitClosed() throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    input.findElement(By.xpath(Locators.VIEW_LOCATOR));
                    return false;
                } catch (NoSuchElementException e) {
                    return true;
                }
            }
        });
    }

    /**
     * Wait Search results view closed.
     *
     * @throws Exception
     */
    public void waitItemIsSelected(final WebElement elem) throws Exception {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                try {
                    return elem.findElement(By.cssSelector("div.gwt-TreeItem-selected")).isDisplayed();
                } catch (NoSuchElementException e) {
                    return true;
                }
            }
        });
    }

    public void close() throws Exception {

        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@view-id='ideSearchResultView' and @aria-hidden='false']")));
        IDE().PERSPECTIVE.getCloseViewButton(VIEW_TITLE).click();
        IDE().SEARCH_RESULT.waitClosed();
    }

    /**
     * wait item present in search results tree.
     *
     * @param path
     *         item's path
     * @throws Exception
     */
    public void waitItemPresent(String path) throws Exception {

        new WebDriverWait(driver(), 30).until(ExpectedConditions.visibilityOfElementLocated(By.id(getItemId(path))));
    }

    /**
     * wait item not present in search results tree.
     *
     * @param path
     *         item's path
     * @throws Exception
     */
    public void waitItemNotPresent(String path) throws Exception {

        new WebDriverWait(driver(), 30).until(ExpectedConditions.invisibilityOfElementLocated(By.id(getItemId(path))));
    }

    /**
     * Open item (make double click) in Search results tree.
     *
     * @param path
     *         item's path
     * @throws Exception
     */
    public void openItem(String path) throws Exception {
        WebElement item = driver().findElement(By.id(getItemId(path)));
        item.click();
        new Actions(driver()).doubleClick(item).build().perform();
    }

    /**
     * Open item (make double click) in Search results tree.
     *
     * @param path
     *         item's path
     * @throws Exception
     */
    public void expandItem(String path) throws Exception {
        WebElement item = driver().findElement(By.id(getItemId(path)));
        item.click();
        new Actions(driver()).doubleClick(item).build().perform();
    }

    /**
     * Select item in Search results tree.
     *
     * @param path
     *         item's path
     * @throws Exception
     */
    public void selectItem(String path) throws Exception {
        driver().findElement(By.xpath("//div[@id='" + getItemId(path) + "']//div[@class='ide-Tree-label']")).click();
    }

    /**
     * @param nameItem
     * @throws Exception
     */
    public void openItemByName(String nameItem) throws Exception {
        WebElement item =
                driver().findElement(By.xpath("//div[@id='ideSearchResultItemTreeGrid']//div[text()='" + nameItem + "']//..//..//div/img"));
        item.click();
        new Actions(driver()).doubleClick(item).build().perform();
    }

    /**
     * send key to search tree
     *
     * @param keys
     */
    public void typeKeys(String keys) {
        new Actions(driver()).sendKeys(treeGrid, keys).build().perform();
    }

    /**
     * send keyboard keys to item into search tree
     *
     * @param path
     * @param keys
     * @throws Exception
     */
    public void typeKeysToItem(String path, String keys) throws Exception {
        WebElement elem = driver().findElement(By.id(getItemId(path)));
        elem.sendKeys(keys);
    }

    /**
     * Generate item id
     *
     * @param path
     *         item's name
     * @return id of item
     */
    public String getItemId(String path) throws Exception {
        path = (path.startsWith(BaseTest.REST_URL)) ? path.replace(BaseTest.REST_URL, "") : path;
        String itemId = (path.startsWith("/")) ? path : "/" + path;
        itemId = Utils.md5(itemId);
        return Locators.TREE_PREFIX + itemId;
    }

    /**
     * Generate item id
     *
     * @param path
     *         item's name
     * @return id of item
     */
    public WebElement getWebElem(String path) throws Exception {
        path = (path.startsWith(BaseTest.REST_URL)) ? path.replace(BaseTest.REST_URL, "") : path;
        String itemId = (path.startsWith("/")) ? path : "/" + path;
        itemId = Utils.md5(itemId);
        WebElement elem = driver().findElement(By.id(Locators.TREE_PREFIX + itemId));
        return elem;
    }

    /**
     * wait found elements by name in search tree
     * search by id does not use
     *
     * @param item
     * @throws Exception
     */
    public void waitForItemByName(final String item) throws Exception {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@id='ideSearchResultItemTreeGrid']//div[text()='" + item + "']")));
    }


    /**
     * wait while elements by name in search tree
     * does not appear
     *
     * @param item
     * @throws Exception
     */
    public void waitNotItemByName(final String item) throws Exception {
        new WebDriverWait(driver(), 10).until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("//div[@id='ideSearchResultItemTreeGrid']//div[text()='" + item + "']")));
    }

    /**
     * returns amount found items
     *
     * @return
     */
    public int getResultCount() {
        return driver().findElements(By.cssSelector(Locators.SEARCH_RESULT_SELECTOR)).size();
    }
}
