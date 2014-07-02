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

import com.codenvy.ide.operation.browse.ShowHideHiddenFilesTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Evgen Vidolob
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ AddBlockCommentsTest.class,
                CreateJavaClassInDifferentSourceFolderTest.class,
                CreateNewClassTest.class,
                DeleteCurrentLineWithKeysTest.class,
                DeleteLineFromEditMenuTest.class,
                FindReplaceJavaTest.class,
                FormatJavaCodeFromEditMenuTest.class,
                FormatJavaCodeWithShortKeysTest.class,
                GotoLineFromEditMenuTest.class,
                GoToLineFromHotkeyTest.class,
                JavaCodeFoldingTest.class,
                JavaFoldSelectionTest.class,
                MainFeaturesRightGutterPanelTest.class,
                MoveLineUpDownFromMenuEditTest.class,
                MoveLineUpDownWithShortKeysTest.class,
                RemoveBlockCommentsTest.class,
                SelectAllDeleteCopyPasteFromShortKeysTest.class,
                SelectAllFromEditMenuTest.class,
                TextEditFomContextMenuTest.class,
                ToggleCommentTest.class,
                UndoRedoFromEditMenuTest.class,
                UndoRedoFromShortKeyTest.class,
                UpdateDependencyTest.class})
public class JavaTestSuite {
}
