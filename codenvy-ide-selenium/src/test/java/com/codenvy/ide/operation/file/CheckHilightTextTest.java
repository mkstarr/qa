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
package com.codenvy.ide.operation.file;

import com.codenvy.ide.BaseTest;
import com.codenvy.ide.MimeType;
import com.codenvy.ide.VirtualFileSystemUtils;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Map;

/** @author <a href="mailto:musienko.maxim@gmail.com">Musienko Maxim</a> */
public class CheckHilightTextTest extends BaseTest {

    private final static String PROJECT = CheckHilightTextTest.class.getSimpleName();

    private final static String HTML_FILE_NAME = "newHtmlFile.html";

    private final static String CSS_FILE_NAME = "newCssFile.css";

    private final static String JS_FILE_NAME = "newJavaScriptFile.js";

    private final static String XML_FILE_NAME = "newXMLFile.xml";

    private final static String TXT_FILE_NAME = "newTxtFile.txt";

    private final static String RUBY_FILE_NAME = "newRubyFile.rb";

    private final static String PHYTHON_FILE_NAME = "newPythonFile.py";

    private final static String PHP_FILE_NAME = "newPHPFile.php";


    private final static String PATH = "src/test/resources/org/exoplatform/ide/operation/file/";

    @BeforeClass
    public static void setUp() {
        try {
            Map<String, Link> project = VirtualFileSystemUtils.createDefaultProject(PROJECT);
            Link linkFile = project.get(Link.REL_CREATE_FILE);

            VirtualFileSystemUtils
                    .createFileFromLocal(linkFile, HTML_FILE_NAME, MimeType.TEXT_HTML, PATH + HTML_FILE_NAME);

            VirtualFileSystemUtils
                    .createFileFromLocal(linkFile, CSS_FILE_NAME, MimeType.TEXT_CSS, PATH + CSS_FILE_NAME);

            VirtualFileSystemUtils
                    .createFileFromLocal(linkFile, JS_FILE_NAME, MimeType.APPLICATION_JAVASCRIPT, PATH + JS_FILE_NAME);

            VirtualFileSystemUtils
                    .createFileFromLocal(linkFile, XML_FILE_NAME, MimeType.TEXT_XML, PATH + XML_FILE_NAME);

            VirtualFileSystemUtils
                    .createFileFromLocal(linkFile, TXT_FILE_NAME, MimeType.TEXT_PLAIN, PATH + TXT_FILE_NAME);

            VirtualFileSystemUtils
                    .createFileFromLocal(linkFile, RUBY_FILE_NAME, MimeType.APPLICATION_RUBY, PATH + RUBY_FILE_NAME);

            VirtualFileSystemUtils
                    .createFileFromLocal(linkFile, PHP_FILE_NAME, MimeType.APPLICATION_PHP, PATH + PHP_FILE_NAME);

            VirtualFileSystemUtils
                    .createFileFromLocal(linkFile, PHYTHON_FILE_NAME, MimeType.TEXT_X_PYTHON, PATH + PHYTHON_FILE_NAME);


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

    @Before
    public void openProject() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.LOADER.waitClosed();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + XML_FILE_NAME);
        IDE.LOADER.waitClosed();
        IDE.EDITOR.waitTabPresent(0);
        IDE.LOADER.waitClosed();
        IDE.WELCOME_PAGE.close();
        IDE.WELCOME_PAGE.waitClosed();
    }

    @Test
    public void checkHilight() throws InterruptedException, Exception {
        /*
         * 1. Check highlighting XML
         */
        IDE.EXPLORER.waitForItem(PROJECT + "/" + XML_FILE_NAME);
        IDE.EXPLORER.openItem(PROJECT + "/" + XML_FILE_NAME);
        IDE.EDITOR.waitActiveFile();
        checkHilightXML();
        IDE.EDITOR.closeFile(0);
        IDE.EDITOR.waitTabNotPresent(0);

        /*
         * 2. Check highlighting TXT
         */
        IDE.EXPLORER.openItem(PROJECT + "/" + TXT_FILE_NAME);
        IDE.EDITOR.waitActiveFile();
        checkHiligtTXT();
        IDE.EDITOR.closeFile(0);
        IDE.EDITOR.waitTabNotPresent(0);

        /*
         * 3. Check highlighting JavaScript
         */
        IDE.EXPLORER.openItem(PROJECT + "/" + JS_FILE_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        checkHilightJavaScript();
        IDE.EDITOR.closeFile(0);
        IDE.EDITOR.waitTabNotPresent(0);

        /*
         * 4. Check highlighting in HTML
         */
        IDE.EXPLORER.openItem(PROJECT + "/" + HTML_FILE_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        checkHilightHTML();
        IDE.EDITOR.closeFile(0);
        IDE.EDITOR.waitTabNotPresent(0);

        /*
         * 5. Check highlighting in CSS
         */
        IDE.EXPLORER.openItem(PROJECT + "/" + CSS_FILE_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        chekHilightingInCssFile();
        IDE.EDITOR.closeFile(0);
        IDE.EDITOR.waitTabNotPresent(0);

        /*
         * 6. Check highlighting in Ruby File
         */
        IDE.EXPLORER.openItem(PROJECT + "/" + RUBY_FILE_NAME);
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        checkHilightRuby();
        IDE.EDITOR.closeFile(0);
        IDE.EDITOR.waitTabNotPresent(0);

        /*
         * 7. Check highlighting in Php File
         */
        IDE.EXPLORER.openItem(PROJECT + "/" + PHP_FILE_NAME);
        checkHilightPhp();
        IDE.EDITOR.closeFile(0);
        IDE.EDITOR.waitTabNotPresent(0);


        /*
         * 8. Check highlighting in Php File
         */
        IDE.EXPLORER.openItem(PROJECT + "/" + PHYTHON_FILE_NAME);
        checkHilightPython();
        IDE.EDITOR.closeFile(0);
        IDE.EDITOR.waitTabNotPresent(0);


    }

    /**
     * checking key tags in test - XML file
     *
     * @throws Exception
     */
    public void checkHilightXML() throws Exception {
        // check color highlight in tags "Module" "xml" "userpref" and not
        // highlight "xml-text"
        // for searching elements used xpath, because check color highlight and
        // location text in DOM
        IDE.EDITOR.selectIFrameWithEditor();
        driver.findElement(
                By.xpath("//body[@class=\"editbox\"]/span[1][@class='xml-processing' and text()=\"<?xml \"]"))
              .isDisplayed();
        driver.findElement(By.xpath("//body[@class='editbox']/span[4][@class='xml-tagname' and text()=\"Module\"]"))
              .isDisplayed();
        driver.findElement(By.xpath("//body[@class='editbox']/span[8][@class='xml-tagname' and text()=\"UserPref\"]"))
              .isDisplayed();
        driver
                .findElement(
                        By.xpath(
                                "//body[@class='editbox']/span[10][@class='xml-text' and text()" +
                                "='name=\"last_location\" datatype=\"hidden\"']"))
                .isDisplayed();
        IDE.selectMainFrame();
    }


    /**
     * checking color highlight in TXT file
     *
     * @throws Exception
     */
    public void checkHiligtTXT() throws Exception {
        // check color highlight in "text content" - word
        // for searching elements used xpath, because check color highlight and
        // location text in DOM
        IDE.EDITOR.selectIFrameWithEditor();
        driver.findElement(By.xpath("//body[@class='editbox']/span[@class='xml-text' and text()=\"newTXTFile\"]"))
              .isDisplayed();
        IDE.selectMainFrame();
    }

    /**
     * checking key tags in test - Java Script file
     *
     * @throws Exception
     */
    public void checkHilightJavaScript() throws Exception {
        // chek next elements in example file: var, undo,redo, x+y, 44.4

        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                "//div[@class" + " and " + "@tabindex]/div/div[18]//span[@class='javascript-variable-2' and text()='x']")));

        driver
                .findElement(
                        By.xpath(
                                "//div[@class and @tabindex]/div/div[2]//span[text()='//Here you see some JavaScript code. Mess around " +
                                "with it to get']"))
                .isDisplayed();
        driver.findElement(
                By.xpath("//div[@class and @tabindex]/div/div[9]//span[@class='javascript-keyword' and text()='var']"))
              .isDisplayed();
        driver
                .findElement(
                        By.xpath(
                                "//div[@class and @tabindex]/div/div[9]//span[@class='javascript-variable' and text()='keyBindings']"))
                .isDisplayed();
        driver.findElement(
                By.xpath(
                        "//div[@class and @tabindex]/div/div[12]//span[@class='javascript-string' and text()='\"undo\"']"))
              .isDisplayed();
        driver
                .findElement(
                        By.xpath(
                                "//div[@class and @tabindex]/div/div[15]//span[@class='javascript-string-2' and text()='/foo|bar/i']"))
                .isDisplayed();
        driver.findElement(
                By.xpath("//div[@class and @tabindex]/div/div[16]//span[@class='javascript-def' and text()='x']"))
              .isDisplayed();

        driver.findElement(
                By.xpath("//div[@class and @tabindex]/div/div[17]//span[@class='javascript-number' and text()='44.4']"))
              .isDisplayed();

        driver.findElement(
                By.xpath(
                        "//div[@class and @tabindex]/div/div[18]//span[@class='javascript-variable-2' and text()='x']"))
              .isDisplayed();

    }

    /**
     * check keys elements in the JavaScript file
     *
     * @throws Exception
     */
    public void checkHilightHTML() throws Exception {
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        // chek next elements in example file: open and closed tags html,
        // function foo (bar, bar), 1px, #448888
        driver.findElement(By.xpath("//div[@class and @tabindex]//span[@class='html-tag' and text()='<html']"));
        driver.findElement(By.xpath("//div[@class and @tabindex]//span[@class='html-tag' and text()='</html']"));
        driver.findElement(By.xpath("(//div[@class and @tabindex]//span[@class='html-tag' and text()='>'])[last()]"));
        driver.findElement(By
                                   .xpath("//div[@class and @tabindex]//span[@class='javascript-keyword' and text()='function']"));
        driver.findElement(
                By.xpath("//div[@class and @tabindex]//span[@class='javascript-variable' and text()='foo']"));
        driver.findElement(By
                                   .xpath("(//div[@class and @tabindex]//span[@class='javascript-nil' and text()='('])[last()]"));
        driver.findElement(By.xpath("//div[@class and @tabindex]//span[@class='javascript-def' and text()='bar']"));
        driver.findElement(By.xpath("//div[@class and @tabindex]//span[@class='css-number' and text()='1px']"));
        driver.findElement(By.xpath("//div[@class and @tabindex]//span[@class='css-atom' and text()='#448888']"));
    }

    /**
     * check keys elements in the GROOVY file
     *
     * @throws Exception
     */
    public void checkHilightGroovy() throws Exception {
        IDE.EDITOR.selectIFrameWithEditor();
        // chek next elements in example file: import, public class, @Path,
        // ("hello world")
        driver.findElement(By
                                   .xpath("//body[@class='editbox']/span[1][@class='groovyComment' and text()=\"//simple groovy " +
                                          "script\"]"));
        driver.findElement(By.xpath("//body[@class='editbox']/span[10][@class='javaKeyword' and text()=\"import \"]"));
        driver.findElement(By.xpath("//body[@class='editbox']/span[32][@class='javaModifier' and text()=\"public \"]"));
        driver.findElement(By.xpath("//body[@class='editbox']/span[33][@class='javaType' and text()=\"class \"]"));

        driver.findElement(
                By.xpath("//body[@class='editbox']/span[37][@class='javaAnnotation' and text()=\"@Path \"]"));
        driver.findElement(By.xpath("//body[@class='editbox']/span[40][@class='groovyString' and text()=\"h\"]"));
        driver.findElement(By.xpath("//body[@class='editbox']/span[41][@class='groovyString' and text()=\"e\"]"));
        IDE.selectMainFrame();
    }

    /**
     * check keys elements in the GOOGLE GADGET file
     *
     * @throws Exception
     */
    public void checkHiligtGoogleGadget() throws Exception {
        IDE.EDITOR.selectIFrameWithEditor();
        // chek next elements in example file: <?xml, <Module>,</Module>
        driver.findElement(By.xpath("//body[@class='editbox']/span[1][@class='xml-processing' and text()=\"<?xml \"]"));
        driver.findElement(By.xpath("//body[@class='editbox']/span[3][@class='xml-punctuation' and text()=\"<\"]"));
        driver.findElement(By.xpath("//body[@class='editbox']/span[4][@class='xml-tagname' and text()=\"Module\"]"));
        driver.findElement(By.xpath("//body[@class='editbox']/span[5][@class='xml-punctuation' and text()=\">\"]"));
        driver.findElement(By.xpath("//body[@class='editbox']/span[16][@class='xml-attname' and text()=\"type\"]"));
        driver.findElement(By.xpath("//body[@class='editbox']/span[17][@class='xml-punctuation' and text()=\"=\"]"));
        driver.findElement(By.xpath("//body[@class='editbox']/span[18][@class='xml-attribute' and text()='\"html\"']"));
        driver.findElement(By.xpath("//body[@class='editbox']/span[44][@class='css-selector']"));
        driver.findElement(By.xpath("//body[@class='editbox']/span[69][@class='css-unit' and text()='3em']"));
        driver.findElement(By.xpath("//body[@class='editbox']/span[117][@class='xml-attribute' and text()='\"block\"']"));
        IDE.selectMainFrame();
    }

    /**
     * check keys elements in the CSS file
     *
     * @throws Exception
     */
    public void chekHilightingInCssFile() throws Exception {
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        // chek next elements in example file: /*Some example CSS*,"6em, #000,
        // bold, !important
        driver.findElement(By
                                   .xpath("//div[@class and @tabindex]//span[@class='css-comment' and text()='/*Some example CSS*/']"));
        driver.findElement(By.xpath("//div[@class and @tabindex]//span[@class='css-meta' and text()='@import']"));

        driver.findElement(By.xpath("//div[@class and @tabindex]//span[@class='css-number' and text()='3em']"));
        driver.findElement(By.xpath("//div[@class and @tabindex]//span[@class='css-atom' and text()='#000']"));
        driver.findElement(
                By.xpath("//div[@class and @tabindex]//span[@class='css-string-2' and text()='#navigation']"));
        driver.findElement(By.xpath("//div[@class and @tabindex]//span[@class='css-number' and text()='bold']"));
        driver.findElement(By.xpath("//div[@class and @tabindex]//span[@class='css-keyword' and text()='!important']"));
    }


    /**
     * checking key tags in test - Ruby file
     *
     * @throws Exception
     */
    public void checkHilightRuby() throws Exception {
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        driver.findElement(
                By.xpath("//span[@class='ruby-comment'  and text()='# Ruby Sample program']"))
              .isDisplayed();

        driver.findElement(
                By.xpath("//span[@class='ruby-keyword'  and text()='class']"))
              .isDisplayed();
        driver.findElement(
                By.xpath("//span[@class='ruby-def'  and text()='getCostAndMpg']"))
              .isDisplayed();

        driver.findElement(
                By.xpath("//span[@class='ruby-number'  and text()='30000']"))
              .isDisplayed();

        driver.findElement(
                By.xpath("//span[@class='ruby-string' and text()='\"Hello, world!\"']"))
              .isDisplayed();

        driver.findElement(
                By.xpath("//span[@class='ruby-nil' and text()='(']"))
              .isDisplayed();

        driver.findElement(
                By.xpath("//span[@class='ruby-tag' and text()='HelloClass']"))
              .isDisplayed();
    }

    /**
     * checking key tags in test - Pph file
     *
     * @throws Exception
     */
    public void checkHilightPhp() throws Exception {
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        driver.findElement(
                By.xpath("//div[@class and @tabindex]//span[@class='php-meta' and text()='<?php']"))
              .isDisplayed();

        driver.findElement(
                By.xpath("//div[@class and @tabindex]//span[@class='php-variable-2' and text()='$subject']"))
              .isDisplayed();

        driver.findElement(
                By.xpath(
                        "//div[@class and @tabindex]//span[@class='php-comment' and text()='//if \"email\" is not filled out, " +
                        "display the form']"))
              .isDisplayed();

        driver.findElement(
                By.xpath("//div[@class and @tabindex]//span[@class='php-string' and text()=\"'message'\"]"))
              .isDisplayed();


        driver.findElement(
                By.xpath("//div[@class and @tabindex]//span[@class='html-tag' and text()='<html']"))
              .isDisplayed();

    }


    /**
     * checking key tags in test - Pph file
     *
     * @throws Exception
     */
    public void checkHilightPython() throws Exception {
        IDE.JAVAEDITOR.waitJavaEditorIsActive();

        driver.findElement(
                By.xpath("//div[@class and @tabindex]//span[@class='python-keyword' and text()='from']"))
              .isDisplayed();

        driver.findElement(
                By.xpath("//div[@class and @tabindex]//span[@class='python-string' and contains(., 'Python 3.2.2')]"))
              .isDisplayed();

        driver.findElement(
                By.xpath("//div[@class and @tabindex]//span[@class='python-number' and contains(., '10000')]"))
              .isDisplayed();

        driver.findElement(
                By.xpath("//div[@class and @tabindex]//span[@class='python-builtin' and text()='range']"))
              .isDisplayed();

    }


}
