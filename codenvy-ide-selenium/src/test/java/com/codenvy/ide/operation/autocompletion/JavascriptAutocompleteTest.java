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
import com.codenvy.ide.operation.java.FormatJavaCodeWithShortKeysTest;

import org.exoplatform.ide.vfs.shared.Link;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;

import java.util.Map;

import static org.fest.assertions.Assertions.assertThat;

public class JavascriptAutocompleteTest extends BaseTest {
    private static final String PROJECT                 = FormatJavaCodeWithShortKeysTest.class.getSimpleName();

    private static Object[]     stringJavascriptMethods = new String[]{"charAt(index) : String", "charCodeAt(index) : Number",
                                                        "concat(array) : String", "hasOwnProperty(property) : boolean",
                                                        "indexOf(searchString) : Number",
                                                        "isPrototypeOf(object) : boolean", "lastIndexOf(searchString) : Number",
                                                        "length() : Number", "localeCompare(Object) : Number",
                                                        "match(regexp) : Boolean", "propertyIsEnumerable(property) : boolean",
                                                        "prototype : Object",
                                                        "replace(searchValue, replaceValue) : String", "search(regexp) : String",
                                                        "slice(start, end) : String",
                                                        "split(separator, [limit]) : Array", "substring(start, end) : String",
                                                        "toLocaleString() : String",
                                                        "toLocaleUpperCase() : String", "toLowerCase() : String", "toString() : String",
                                                        "toUpperCase() : String", "trim() : String",
                                                        "valueOf() : Object"};

    private static Object[]     stringDateMethods       = new String[]{"getDay() : Number", "getFullYear() : Number",
                                                        "getHours() : Number", "getMinutes() : Number",
                                                        "hasOwnProperty(property) : boolean",
                                                        "isPrototypeOf(object) : boolean",
                                                        "propertyIsEnumerable(property) : boolean",
                                                        "prototype : Object",
                                                        "setDay(dayOfWeek) : Number", "setFullYear(year) : Number",
                                                        "setHours(hour) : Number",
                                                        "setMinutes(minute) : Number", "setTime(millis) : Number",
                                                        "toLocaleString() : String", "toString() : String",
                                                        "valueOf() : Object"};

    @BeforeClass
    public static void setUp() {
        final String filePath = "src/test/resources/org/exoplatform/ide/project/JavaScriptAutoComplete.zip";

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
    public void mainStringMethods() throws Exception {
        IDE.EXPLORER.waitOpened();
        IDE.OPEN.openProject(PROJECT);
        IDE.EXPLORER.waitForItem(PROJECT + "/" + "js");
        IDE.EXPLORER.openItem(PROJECT + "/" + "js");
        IDE.EXPLORER.waitForItem(PROJECT + "/" + "js" + "/" + "script.js");
        IDE.EXPLORER.openItem(PROJECT + "/" + "js" + "/" + "script.js");
        IDE.JAVAEDITOR.waitJavaEditorIsActive();
        IDE.GOTOLINE.goToLine(10);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("a.");
        IDE.JAVAEDITOR.waitJavaDocContainer();
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText(stringJavascriptMethods);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("\n");
    }

    @Test
    public void mainDateMethods() throws Exception {
        IDE.GOTOLINE.goToLine(21);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("date.");
        IDE.JAVAEDITOR.waitJavaDocContainer();
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText(stringDateMethods);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("\n");
    }

    @Test
    public void mainNumberMethods() throws Exception {
        IDE.GOTOLINE.goToLine(31);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("var numb =10000;\n");
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("var numb =10000;");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("numb.");
        IDE.JAVAEDITOR.waitJavaDocContainer();
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("toFixed(digits) : Number");
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("toExponential(digits) : Number");
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("toPrecision(digits) : Number");
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("valueOf() : Object");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("\n");
    }

    @Test
    public void jsonMethods() throws Exception {
        IDE.GOTOLINE.goToLine(36);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("JSON.");
        IDE.JAVAEDITOR.waitJavaDocContainer();
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("parse(str) : Object");
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("stringify(obj) : String");
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("valueOf() : Object");
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("valueOf() : Object");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("\n");
    }


    @Test
    public void userFunctionsTest() throws Exception {
        IDE.GOTOLINE.goToLine(42);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.CONTROL.toString() + Keys.SPACE.toString());
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("objectTest() : Object");
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("stringTest() : Object");
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("arrayTest() : Object");
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("dateTest() : Object");
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("booleanTest() : Object");
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("numberTest() : Object");
        IDE.JAVAEDITOR.waitJavaDocContainerWithSpecifiedText("regExpTest() : Object");
        IDE.JAVAEDITOR.typeTextIntoJavaEditor(Keys.ESCAPE.toString());
        IDE.GOTOLINE.goToLine(45);
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("fu");
        IDE.CODE_ASSISTANT_JAVA.waitCodenvyAutocompletePanel();
        IDE.CODE_ASSISTANT_JAVA.insertSelectedItem();
        IDE.JAVAEDITOR.waitWhileJavaEditorWillContainSpecifiedText("function () {\n  \n}");
        IDE.JAVAEDITOR.waitAnyErrorMarkerIsAppear();
        IDE.JAVAEDITOR.typeTextIntoJavaEditor("a");
        IDE.JAVAEDITOR.waitAllMarkersIsDisappear();
    }
}
