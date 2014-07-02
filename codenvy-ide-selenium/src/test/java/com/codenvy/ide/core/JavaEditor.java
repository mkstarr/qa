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

import com.codenvy.ide.MenuCommands;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/** @author Musienko Maxim */
public class JavaEditor extends AbstractTestModule {
    /** @param ide */
    public JavaEditor(com.codenvy.ide.IDE ide) {
        super(ide);
    }

    public static final String MODIFIED_MARK = "*";

    private interface Locators {

        String FIND_AND_REPLACE_BTN = "//div[@title='Find/Replace...' and @enabled='true']";

        String LINENUMBER_PREFIX = "//div[@component='Border']/div/div/div/div/div[3]";

        String EDITOR_TABSET_LOCATOR = "//div[@id='editor']";

        String EDITOR_TAB_LOCATOR = "//div[@panel-id='editor' and @view-id='editor-%s' ]//div[@tabindex]";

        String EDITOR_VIEW_LOCATOR = "//div[@panel-id='editor' and @view-id='editor-%s']";

        String SELECTED_EDITOR_TAB_LOCATOR = EDITOR_TABSET_LOCATOR
                                             +
                                             "//div[contains(@class, 'gwt-TabLayoutPanelTab-selected') and contains(" +
                                             "., '%s')]";

        String JAVAEDITOR_SET_CURSOR_LOCATOR = EDITOR_VIEW_LOCATOR + "//div[@tabindex='-1']/div";

        String TITLE_SPAN_LOCATOR = "//span[@title='%s']/..";

        String GET_TEXT_LOCATOR = "//div[@panel-id='editor' and @view-id='editor-%s']//div[@tabindex='-1']";

        String GET_TEXT_LOCATOR_FULL_TEXT =
                "//div[@panel-id='editor' and @view-id='editor-%d']//div[@tabindex='-1']/div/div[contains(@style,"
                +
                "'top: %spx')]/span";

        String GET_FULL_TEXT_LOCATOR = "//div[@panel-id='editor' and @view-id='editor-%d']//div[@tabindex='-1']/div";

        String LINE_NUMBER_LOCATOR = EDITOR_VIEW_LOCATOR + LINENUMBER_PREFIX;

        String GET_POSITION_TEXT = GET_TEXT_LOCATOR + "/div/div[%s]";

        String ERROR_LABEL_TEXT = "//div[text()='%s']";

        String JAVA_DOC_CONTAINER = "//div[@__animcontrollerstate]";

        String NUM_ACTIVE_EDITOR =
                "//div[@class='gwt-TabLayoutPanelContent' and @is-active='true' and @panel-id='editor']";

        String NUM_INACTIVE_EDITOR =
                "//div[@class='gwt-TabLayoutPanelContent' and @is-active='false' and @panel-id='editor']";

        String HIGHLITER_BORDER_PREFIX = "//div[@component='Border' and contains(@style, '182, 204, 232')]";

        String ACTIVE_EDITOR_WITH_REDACTOR = NUM_ACTIVE_EDITOR + HIGHLITER_BORDER_PREFIX + "/div/div//textarea";

        String EDITOR_JAVA_CONTAINER_READY_STATUS =
                "//div[@class='gwt-TabLayoutPanelContent' and @is-active='true' and @panel-id='editor']/..";

        String EDITOR_CONTAINER_READY_DESC =
                "//div[@class='gwt-TabLayoutPanelContent' and @is-active='true' and " +
                "@panel-id='editor']/parent::div/preceding-sibling::div[1]";

        String TAB_LOCATOR = "//div[@tab-bar-index='%s']";

        String SOURCE_BUTTON = "//div[text()='Source']";

        String CLOSE_BUTTON_LOCATOR = "//div[@button-name='close-tab']";

        String ACTIVE_EDITOR_TAB_LOCATOR = "//div[@panel-id='editor' and @is-active='true']";

        String MARKERS_LOCATOR = "//div[@data-line-number]";

        String WARNING_ICON_BASE_64 =
                "url(data:image/png;base64,"
                +
                "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABUElEQVR42s2Tu0vDUBSH8"
                +
                "+c4uetgBIcOLiq4iENE6eAk6GAntVgcxMcg3URBRJBqpKUq9dEQpFFU8EGhgxVbFbXkYSNoNIk"
                +
                "/c88gKrRm7IXDxznc850Dl8txNXWMvR6oq81QYy1ElvtuLu8HYaZ7gYfkd7x4eTkd9CfRNng4TyLcx"
                +
                "/VfVEXen8DY5mEbcThmxosDoq3HoW35FOipBlimN9lR4dp3RJbrO40oSVMoLHeidLRQWWZdTcNQAnh"
                +
                "/lcEOo3EYgJWfoWY4l8SqW7g3I/g47yPB23EX3OIYTbdyo2ir44gsrypJrYWgZHUkVwZJxKb+FPy7RSR2Ro3t"
                +


                "/ZMoJIahSQI1Xsw3EbW0gKJXrygYX9qFuCmje2CCprnXYWjyEG3A6ObD1bdISCdoFULInGaRidTj8zaK59wsknMdRBSjVPf1tPfKIl3+G6xeOx/wC"
                +
                "+jXP4zVABExAAAAAElFTkSuQmCC)";

        String ERROR_ICON_BASE_64 =
                "url(data:image/png;base64,"
                +
                "iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABCElEQVR42mNgGFTg/d7E/29WOfx/s8YZTIP4RGv+uC/t/6f9yf//P98Kx5+B/I/704gz5O0G"
                +
                "+/9/Xq7///fFOhT6zXp74gx4v8P+/+/3m/7/+XQMiI+A6d/vNv1/u51IA97tsvz/4xPQ5j9v/v/9/QRM33Dyx4kxDPhxp+//++Pu/39+O/QfBEA0SOHj8ob"
                +
                "/bxYs/383Og1Mg/hYDQCBvw/q//+6mAI24PflVLBCkCYQeDFhOpgG8XEaAAK71lb+P3713f+ty4vBCkE2wzSDaBAfrwGtay6AFQdmdZNnQMviPf"
                +
                "/Xbzv0P6agnTwvbD5w5r93QuX/9TuPYA3E5x0T8BuADEiKxgEDAO7lR+m35uNLAAAAAElFTkSuQmCC)";

        String TODO_NOTE_ICON =
                "url(data:image/gif;base64,"
                +
                "R0lGODlhEAAQANUAAPHz+vj5/PX3/Ozw+YeUrYyWquft+fD0/PP2/PX3+/r7/fj5+152o22ErV9zlmBzlnGCoN/o+OLq"
                +
                "+ICUtIGUtJutzIKRqpenwJinwI6bsK+6zO7z+666zPX3+pKbpvv8/fr7/D+m/6vB1ACK+ABVlgBGe5mfoaGknKmpltDQxLCtkbeyjbaxjbu0itCseNSwe"
                +
                "+HLqsWaYc6pdseicsqldNOuetvEpMGbbMSeb76Ya62BUryWarWQZ+TWxtTBr"
                +
                "////yH5BAEAAD8ALAAAAAAQABAAAAaIwJ9wmBGJMsNk0sLhaDgWpbAXi0EwlwsGoov1kq"
                +
                "/Xg0KZkCmOMLi1aLvbrVeyxl587qHPolVLulh2eCR3Ky5JPioKgiEdHSoySSEpiwYVAyg0SSR5IZsGA6AnM5ElIyUhBgYNDBImOIemIxKztB43fikjEbu8EQU5fgTCw8Q7hzzIyco+Us1KQQA7)";

        String LINE_MARKER_POSITION = "//div[@data-line-number='%s']";

        String GUTTER_COLLAPSE_MARKER = "//div[@id='foldGutterCollapseMarker_line:%d']";

        String GUTTER_EXPAND_MARKER = "//div[@id='foldGutterExpandMarker_line:%d']";

        String EXPAND_MARKER = "//span[@id='expandMarker_line:%d']";

        String INACTIVE_SELECTION = "span.collabeditor-selection-inactive";

        String ACTIVE_SELECTION = "//span[contains(@class, 'collabeditor-selection') and text()='%s']";

        // locator check area after code
        String INACTIVE_SELECTION_AREA =
                NUM_INACTIVE_EDITOR
                +
                "//div[@style='position: absolute; top: %spx;']/span[3][@style[contains(., '100')]and@class[contains(., " +
                "'collabeditor-selection-inactive')]]";

        // locator check area after code
        String ACTIVE_SELECTION_AREA =
                "//div[@style='position: absolute; top: %spx;']/span[3][@style[contains(., '100')]and@class[contains(., " +
                "'collabeditor-selection')]]";


        String TAB_READONLY_MODE =
                "//td[@class='tabTitleText']/span[@title='File opened in read only mode. Use SaveAs command.' and contains(., " +
                "'%s')]/img[@id='fileReadonly']";

        String GUTTERS_OF_EDITOR =
                "//div[@panel-id='editor' and @is-active='true']//div[contains(@style, 'float: left;')]";

    }

    private WebElement editor;

    @FindBy(xpath = Locators.JAVA_DOC_CONTAINER)
    private WebElement javaDocContainer;

    @FindBy(xpath = Locators.ACTIVE_EDITOR_WITH_REDACTOR)
    private WebElement numActiveEditorWithRedactor;

    @FindBy(xpath = Locators.EDITOR_JAVA_CONTAINER_READY_STATUS)
    private WebElement javacontainerReadyStatus;

    @FindBy(xpath = Locators.EDITOR_CONTAINER_READY_DESC)
    private WebElement descendReadyStatus;

    @FindBy(xpath = Locators.NUM_ACTIVE_EDITOR)
    private WebElement numActiveEditor;

    @FindBy(xpath = Locators.FIND_AND_REPLACE_BTN)
    private WebElement findAndReplaceBtn;

    /**
     * wait while java editor is active
     *
     * @throws Exception
     */
    public void waitJavaEditorIsActive() throws Exception {


        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return javacontainerReadyStatus.getAttribute("style").contains("width: 100%; height: 100%;")
                       && !descendReadyStatus.getAttribute("style").isEmpty() &&
                       numActiveEditorWithRedactor.getTagName().equals("textarea");
            }
        });
        IDE().LOADER.waitClosed();
    }

    /** wait appearance error on panel with line numbers if in java - mistake */
    public void waitErrorLabel(final String errorMess) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    WebElement textJavaErr =
                            driver().findElement(By.xpath(String.format(Locators.ERROR_LABEL_TEXT, errorMess)));
                    return textJavaErr != null;
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /** wait appearance line number panel */
    public void waitLineCloseNumberPanel() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    WebElement line =
                            driver.findElement(By.xpath(
                                    String.format(Locators.LINE_NUMBER_LOCATOR, getNumberTabOfActiveEditor())));
                    return false;
                } catch (Exception e) {
                    return true;
                }
            }
        });
    }

    /** wait line number panel disappear */
    public void waitFileContentModificationMark() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    WebElement line =
                            driver.findElement(By.xpath(
                                    String.format(Locators.LINE_NUMBER_LOCATOR, getNumberTabOfActiveEditor())));
                    return line != null && line.isDisplayed();
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }


    /**
     * wait tab with specified name and read only title and lock icon
     *
     * @param nameTab
     */
    public void waitTadWitReadOnlyMode(String nameTab) {
        new WebDriverWait(driver(), 10)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(Locators.TAB_READONLY_MODE,
                                                                                            nameTab))));
    }


    /** wait appereance javadoc Container */
    public void waitJavaDocContainer() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                return javaDocContainer != null && javaDocContainer.isDisplayed();
            }
        });
    }

    /**
     * wait appearance javadoc Container with specified javadoc text
     *
     * @param obj
     */
    public void waitJavaDocContainerWithSpecifiedText(final Object[] obj) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                waitJavaDocContainer();
                String[] stringArray = Arrays.copyOf(obj, obj.length, String[].class);
                String compare1 = convertObjToStr(stringArray);
                String compare2 = convertObjToStr(getTextFromJavaDocContainer().split("\n"));
                return compare2.contains(compare1);
            }
        });
    }

    /**
     * wait appereance javadoc Container with specified javadoc text
     *
     * @param text
     */
    public void waitJavaDocContainerWithSpecifiedText(final String text) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                return javaDocContainer.getText().contains(text);
            }
        });
    }

    /**
     * auxiliary for cast arraystring to string
     *
     * @param arr
     * @return
     */
    protected String convertObjToStr(String[] arr) {
        StringBuilder builderForObj = new StringBuilder();
        for (String s : arr) {
            builderForObj.append(s);

        }
        return builderForObj.toString();
    }

    /**
     * get text from javadoccontainer
     *
     * @return
     */
    public String getTextFromJavaDocContainer() {
        return javaDocContainer.getText();
    }

    public boolean isEditorTabSelected(String tabTitle) {
        try {
            return editor.findElement(By.xpath(String.format(Locators.SELECTED_EDITOR_TAB_LOCATOR, tabTitle))) != null;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /** set cursor in begin position in java editor */
    public void setCursorToActiveJavaEditor() throws Exception {
        driver().findElement(
                By.xpath(String.format(Locators.JAVAEDITOR_SET_CURSOR_LOCATOR, getNumberTabOfActiveEditor()))).click();
    }

    /** set cursor on active and not active java editor in begin position */
    public void setCursorToJavaEditor() throws Exception {
        driver().findElement(
                By.xpath(String.format(Locators.JAVAEDITOR_SET_CURSOR_LOCATOR, getNumberTabOfActiveEditor()))).click();
    }

    /**
     * @param text
     *         (can be used '\n' as line break)
     */
    public void typeTextIntoJavaEditor(String text) throws Exception {
        try {


            WebElement eleme =
                    driver().findElement(
                            By.xpath(String.format(Locators.EDITOR_VIEW_LOCATOR + "//textarea",
                                                   getNumberTabOfActiveEditor())));
            if (driver() instanceof FirefoxDriver) {
                JavascriptExecutor executor = (JavascriptExecutor)driver();
                executor.executeScript("arguments[0].style.left='0px'", eleme);

            }
            eleme.sendKeys(text);

        } finally {
            IDE().selectMainFrame();
        }
    }

    /**
     * Move cursor in Java editor left to pointed number of symbols.
     *
     * @param symbols
     *         number of symbols to move left
     * @throws Exception
     */
    public void moveCursorLeft(int symbols) throws Exception {
        for (int i = 0; i < symbols; i++) {
            typeTextIntoJavaEditor(Keys.ARROW_LEFT.toString());
        }
    }

    /**
     * Move cursor in Javaeditor right to pointed number of symbols.
     *
     * @param symbols
     *         number of symbols to move right
     * @throws Exception
     */
    public void moveCursorRight(int symbols) throws Exception {
        for (int i = 0; i < symbols; i++) {
            typeTextIntoJavaEditor(Keys.ARROW_RIGHT.toString());
        }
    }

    /**
     * Move cursor in Javaeditor down to pointed number of symbols.
     *
     * @param symbols
     *         number of symbols to move right
     * @throws Exception
     */
    public void moveCursorDown(int symbols) throws Exception {
        for (int i = 0; i < symbols; i++) {
            typeTextIntoJavaEditor(Keys.ARROW_DOWN.toString());
        }
    }

    /**
     * Move cursor in Javaeditor up to pointed number of symbols.
     *
     * @param symbols
     *         number of symbols to move right
     * @throws Exception
     */
    public void moveCursorUp(int symbols) throws Exception {
        for (int i = 0; i < symbols; i++) {
            typeTextIntoJavaEditor(Keys.ARROW_UP.toString());
        }
    }

    /**
     * emulate right click of a mouse in java editor
     *
     * @throws Exception
     */
    public void callContextMenuIntoJavaEditor() throws Exception {
        try {
            WebElement eleme =
                    driver().findElement(
                            By.xpath(String.format(Locators.EDITOR_VIEW_LOCATOR, getNumberTabOfActiveEditor())));
            new Actions(driver()).contextClick(eleme).build().perform();
            IDE().CONTEXT_MENU.waitOpened();
        } finally {
            IDE().selectMainFrame();
        }
    }

    /** @param title */
    public void waitNoContentModificationMark(final String title) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    WebElement tab =
                            editor.findElement(By.xpath(Locators.EDITOR_TABSET_LOCATOR
                                                        + String.format(Locators.TITLE_SPAN_LOCATOR, title)));
                    return !tab.getText().trim().endsWith(MODIFIED_MARK);
                } catch (Exception e) {
                    return true;
                }
            }
        });
    }

    /** wait while all markers disappear */
    public void waitAllMarkersIsDisappear() {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    List<WebElement> elements = driver().findElements(By.xpath(Locators.MARKERS_LOCATOR));
                    return (elements.size() == 0);
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /** wait while the right amount markers is appear */
    public void waitNumMarkersIsAppear(final int numMark) {
        new WebDriverWait(driver(), 20).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    List<WebElement> elements = driver().findElements(By.xpath(Locators.MARKERS_LOCATOR));
                    return (elements.size() == numMark);
                } catch (Exception e) {
                    return true;
                }
            }
        });
    }

    /** wait while appear any error marker */
    public void waitAnyErrorMarkerIsAppear() {
        new WebDriverWait(driver(), 20).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    int countMarkers = 0;
                    List<WebElement> elements = driver().findElements(By.xpath(Locators.MARKERS_LOCATOR));
                    for (WebElement webElement : elements) {
                        if (webElement.getCssValue("background-image").replace("\"", "").equals(Locators.ERROR_ICON_BASE_64)) {
                            countMarkers++;
                        }
                    }

                    return (countMarkers > 0);
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /** wait while appear any error marker */
    public void waitAllErrorMarkersIsDisAppear() {
        new WebDriverWait(driver(), 20).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    int countMarkers = 0;
                    List<WebElement> elements = driver().findElements(By.xpath(Locators.MARKERS_LOCATOR));
                    for (WebElement webElement : elements) {
                        if (webElement.getCssValue("background-image").replace("\"", "").equals(Locators.ERROR_ICON_BASE_64)) {
                            countMarkers++;
                        }
                    }
                    return (countMarkers == 0);
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /**
     * wait while any marker is disappear in specified position
     *
     * @param position
     */
    public void waitMarkerIsNotPresentInPosition(final int position) {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(String.format(
                Locators.LINE_MARKER_POSITION,
                position - 1))));
    }

    /**
     * wait while warning marker is appear in specified position
     *
     * @param position
     */
    public void waitWarningMarkerPresentInPosition(final int position) {

        new WebDriverWait(driver(), 20).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    WebElement elem =
                            driver().findElement(By.xpath(String.format(Locators.LINE_MARKER_POSITION, position - 1)));
                    return elem.isDisplayed() &&
                           elem.getCssValue("background-image").replace("\"", "").equals(Locators.WARNING_ICON_BASE_64);
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /**
     * wait while note marker is appear in specified position
     *
     * @param position
     */
    public void waitNoteMarkerPresentInPosition(final int position) {

        new WebDriverWait(driver(), 20).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    WebElement elem =
                            driver().findElement(By.xpath(String.format(Locators.LINE_MARKER_POSITION, position - 1)));
                    return elem.isDisplayed() && elem.getCssValue("background-image").replace("\"", "").equals(Locators.TODO_NOTE_ICON);
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /**
     * wait while warning marker is appear in specified position
     *
     * @param position
     */
    public void waitErrorMarkerPresentInPosition(final int position) {

        new WebDriverWait(driver(), 20).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    WebElement elem =
                            driver().findElement(By.xpath(String.format(Locators.LINE_MARKER_POSITION, position - 1)));
                    return elem.isDisplayed() &&
                           elem.getCssValue("background-image").replace("\"", "").equals(Locators.ERROR_ICON_BASE_64);
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /**
     * Wait mark of file content modification appear (symbol "*" near title).
     *
     * @param title
     *         file's title
     */
    public void waitFileContentModificationMark(final String title) {
        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    WebElement tab =
                            editor.findElement(By.xpath(Locators.EDITOR_TABSET_LOCATOR
                                                        + String.format(Locators.TITLE_SPAN_LOCATOR, title)));
                    return tab.getText().trim().endsWith(MODIFIED_MARK);
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /**
     * get text from tag with java code in DOM note: start position can be not with 1. Because text with java - code in DOM of the java
     * editor not sequenced follows
     *
     * @param position
     * @return
     */
    public String getTextFromSetPosition(int position) {
        return driver().findElement(
                By.xpath(String.format(Locators.GET_POSITION_TEXT, getNumberTabOfActiveEditor(), position))).getText();
    }

    /** wait while in java editor will appear text */
    public void waitWhileJavaEditorWillContainSpecifiedText(final String text) {
        new WebDriverWait(driver(), 60).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return getVisibleTextFromJavaEditor().contains(text);
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /** wait while in java editor will appear text */
    public void waitWhileJavaEditorIsEmpty() {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return getVisibleTextFromJavaEditor().isEmpty();
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /** wait while into java editor disappears text fragment */
    public void waitWhileJavaEditorWillNotContainSpecifiedText(final String text) {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return !(getVisibleTextFromJavaEditor().contains(text));
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /**
     * TODO should be remove or changed Close tab in editor. Close ask window in case it appear while closing.
     *
     * @param tabIndex
     *         index of tab, starts at 0
     * @throws Exception
     */
    public void closeTabIgnoringChanges(int tabIndex) throws Exception {
        selectTab(tabIndex);
        final String viewId = editor.findElement(By.xpath(Locators.ACTIVE_EDITOR_TAB_LOCATOR)).getAttribute("view-id");
        clickCloseEditorButton(tabIndex);

        /*
         * Closing ask dialogs if them is appears.
         */
        if (IDE().ASK_DIALOG.isOpened()) {
            IDE().ASK_DIALOG.clickNo();
        } else if (IDE().ASK_FOR_VALUE_DIALOG.isOpened()) {
            IDE().ASK_FOR_VALUE_DIALOG.clickNoButton();
        } else {
            fail("Dialog has been not found!");
        }

        new WebDriverWait(driver(), 30).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                try {
                    input.findElement(By.xpath(String.format(Locators.EDITOR_VIEW_LOCATOR, viewId)));
                    return false;
                } catch (NoSuchElementException e) {
                    return true;
                }
            }
        });
    }

    /**
     * get all visible text from collab editor
     *
     * @return
     * @throws Exception
     */
    public String getVisibleTextFromJavaEditor() throws Exception {
        StringBuilder sb = new StringBuilder();

        boolean flag = true;

        int lineValue = -15;

        while (flag) {
            lineValue += 15;
            try {
                WebElement line =
                        driver()
                                .findElement(
                                        By.xpath(String.format(Locators.GET_TEXT_LOCATOR_FULL_TEXT,
                                                               getNumberTabOfActiveEditor(),
                                                               lineValue)));
                if (line.isDisplayed()) {
                    sb.append(line.getText() + "\n");
                }
            } catch (NoSuchElementException e) {
                flag = false;
            }

        }
        return sb.toString();
    }


    /** get all text from java editor, but text unsorted (text order doesn't match with visible text in Editor) */
    public String getAllTextFromJavaEditor(final String content) throws Exception {
        new WebDriverWait(driver(), 20).until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver webDriver) {
                return driver().findElement(By.xpath(String.format(Locators.GET_FULL_TEXT_LOCATOR, getNumberTabOfActiveEditor()))).getText()
                        .contains(content);
            }
        });
        return content;
    }

    /**
     * Getting of number current active tab of the code editor
     *
     * @return
     */
    public int getNumberTabOfActiveEditor() {
        return Integer.parseInt(numActiveEditor.getAttribute("view-id").replace("editor-", ""));
    }

    /**
     * Select tab the code editor with the specified name
     *
     * @param fileName
     * @throws Exception
     */
    public void selectTab(String fileName) throws Exception {
        WebElement tab =
                editor.findElement(By.xpath(String.format(Locators.EDITOR_TABSET_LOCATOR + Locators.TITLE_SPAN_LOCATOR,
                                                          fileName)));
        tab.click();
        waitJavaEditorIsActive();
    }

    /**
     * Click on editor tab to make it active.
     * <p/>
     * Numbering of tabs starts with 0.
     *
     * @param tabIndex
     *         index of tab
     * @throws Exception
     */
    public void selectTab(int tabIndex) throws Exception {
        editor.findElement(
                By.xpath(String.format(Locators.EDITOR_TABSET_LOCATOR + Locators.TAB_LOCATOR + "//span/..", tabIndex)))
              .click();
        waitJavaEditorIsActive();
    }

    /**
     * click on save as button and type in save as field new name of the file
     *
     * @param tabIndex
     * @param name
     * @throws Exception
     */
    public void saveAs(int tabIndex, String name) throws Exception {
        selectTab(tabIndex);
        IDE().MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.SAVE_AS);
        IDE().ASK_FOR_VALUE_DIALOG.waitOpened();
        IDE().ASK_FOR_VALUE_DIALOG.setValue(name);
        IDE().ASK_FOR_VALUE_DIALOG.clickOkButton();
        IDE().ASK_FOR_VALUE_DIALOG.waitClosed();
        IDE().LOADER.waitClosed();
    }

    /**
     * Click on Source button at the bottom of editor.
     *
     * @throws Exception
     */
    public void clickSourceButton() throws Exception {
        editor.findElement(By.xpath(Locators.SOURCE_BUTTON)).click();
    }

    /**
     * Delete pointed number of lines in editor.
     *
     * @param count
     *         number of lines to delete
     * @throws Exception
     */
    public void deleteLinesInEditor(int count) throws Exception {
        for (int i = 0; i < count; i++) {
            typeTextIntoJavaEditor(Keys.CONTROL.toString() + "d");
        }
    }

    /**
     * Click on Close Tab button. Old name of this method is "clickCloseTabButton(int tabIndex)"
     *
     * @param tabIndex
     *         index of tab, starts at 0
     */
    public void clickCloseEditorButton(int tabIndex) throws Exception {
        WebElement closeButton =
                editor.findElement(
                        By.xpath(Locators.EDITOR_TABSET_LOCATOR + String.format(Locators.TAB_LOCATOR, tabIndex)
                                 + Locators.CLOSE_BUTTON_LOCATOR));
        closeButton.click();
    }

    /**
     * @param markerPosition
     *         move cursor to error or warning marker
     */
    public void moveCursorToMarker(int markerPosition) {
        WebElement marker = driver().findElement(By.xpath(String.format(Locators.MARKERS_LOCATOR, markerPosition)));
        new Actions(driver()).moveToElement(marker).build().perform();
    }

    /** click on marker in specified position */
    public void clickOnMarker(int markerPosition) {
        driver().findElement(By.xpath(String.format(Locators.MARKERS_LOCATOR, markerPosition))).click();
    }

    /**
     * wait gutter collapse button in specified line number
     *
     * @param lineNumber
     */
    public void waitGutterCollapseButtonInSpecifiedLineNumber(final int lineNumber) {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                Locators.GUTTER_COLLAPSE_MARKER,
                lineNumber))));
    }

    /**
     * wait gutter collapse button disappear in specified line number
     *
     * @param lineNumber
     */
    public void waitGutterCollapseButtonDisappearInSpecifiedLineNumber(final int lineNumber) {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(String.format(
                Locators.GUTTER_COLLAPSE_MARKER,
                lineNumber))));
    }

    /**
     * click on gutter collapse button in specified line number
     *
     * @param lineNumber
     * @throws Exception
     */
    public void clickOnGutterCollapseButtonInSpecifiedLineNumber(int lineNumber) throws Exception {
        WebElement marker = driver().findElement(By.xpath(String.format(Locators.GUTTER_COLLAPSE_MARKER, lineNumber)));
        marker.click();
    }

    /**
     * wait expand button in specified line number
     *
     * @param lineNumber
     */
    public void waitExpandButtonInSpecifiedLineNumber(final int lineNumber) {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                Locators.EXPAND_MARKER,
                lineNumber))));

    }

    /**
     * wait expand button disappear in specified line number
     *
     * @param lineNumber
     */
    public void waitExpandButtonDisappearInSpecifiedLineNumber(final int lineNumber) {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(String.format(
                Locators.EXPAND_MARKER,
                lineNumber))));

    }

    /**
     * click on expand button in specified line number in editor
     *
     * @param lineNumber
     *         number of the line
     * @throws Exception
     */
    public void clickOnExpandButtonInSpecifiedLineNumber(int lineNumber) throws Exception {
        WebElement marker = driver().findElement(By.xpath(String.format(Locators.EXPAND_MARKER, lineNumber)));
        marker.click();
    }

    /**
     * wait gutter expand button
     *
     * @param lineNumber
     */
    public void waitGutterExpandButtonInSpecifiedLineNumber(final int lineNumber) {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                Locators.GUTTER_EXPAND_MARKER,
                lineNumber))));
    }

    /**
     * wait gutter expand button disappear in specified line number
     *
     * @param lineNumber
     */
    public void waitGutterExpandButtonDisappearInSpecifiedLineNumber(final int lineNumber) {
        new WebDriverWait(driver(), 20).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(String.format(
                Locators.GUTTER_EXPAND_MARKER,
                lineNumber))));
    }

    /**
     * click on gutter expand button in specified line number
     *
     * @param lineNumber
     * @throws Exception
     */
    public void clickOnGutterExpandButtonInSpecifiedLineNumber(int lineNumber) throws Exception {
        WebElement marker = driver().findElement(By.xpath(String.format(Locators.GUTTER_EXPAND_MARKER, lineNumber)));
        marker.click();
    }


    /**
     * wait while word becomes with inactive - selections (grey selection)
     *
     * @param code
     */

    public void whaitInactiveSelectionWord(final String code) {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {

                boolean state = false;
                List<WebElement> inactiveSeletElem = driver().findElements(By.cssSelector(Locators.INACTIVE_SELECTION));
                for (WebElement webElement : inactiveSeletElem) {
                    if (webElement.getText().contains(code)) {
                        state = true;
                    } else {
                        state = false;
                        break;
                    }
                }
                return state;

            }
        });
    }


    /**
     * wait while word becomes with active - selections (blue selection)
     *
     * @param code
     */

    public void whaitActiveSelectionWord(final String code) {
        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver input) {
                return driver().findElement(By.xpath(String.format(Locators.ACTIVE_SELECTION, code))).isDisplayed();
            }
        });
    }


    /**
     * wait while string becomes with active - selections (grey selection) !Text should be visible on editor.
     *
     * @param codeFragment
     */
    public void waitStringActiveSelection(final String codeFragment) {

        for (String pk : codeFragment.split(" ")) {
            whaitActiveSelectionWord(pk);
        }
    }


    /**
     * wait while string becomes with inactive - selections (grey selection) !Text should be visible on editor.
     *
     * @param codeFragment
     */
    public void waitStringIsInactiveSelection(final String codeFragment) {

        for (String pk : codeFragment.split(" ")) {
            whaitInactiveSelectionWord(pk);
        }
    }


    /**
     * wait gutter expand button in specified line number !Text should be visible on page.
     *
     * @param numberOfString
     */
    public void waitWhileAreaAfterCodeIsInactiveSelection(int numberOfString) {

        new WebDriverWait(driver(), 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format(
                Locators.INACTIVE_SELECTION_AREA,
                (numberOfString - 1) * 15))));
    }

    /**
     * wait while all area is selected !Text should be visible on page.
     *
     * @param numberOfString
     */
    public void waitWhileAreaAfterCodeIsActiveSelection(final int numberOfString) {

        new WebDriverWait(driver(), 10).until(new ExpectedCondition<Boolean>() {

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    WebElement highLightArea =
                            driver().findElement(By.xpath(String.format(Locators.ACTIVE_SELECTION_AREA,
                                                                        (numberOfString - 1) * 15)));
                    return highLightArea.getCssValue("background-color").equals("rgba(212, 226, 255, 1)");
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }

    /** check number of gutters */
    public void checkNumberOfGutter() {
        List<WebElement> list = driver().findElements(By.xpath(Locators.GUTTERS_OF_EDITOR));
        assertThat(4, equalTo(list.size()));
    }
}
