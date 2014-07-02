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
package com.codenvy.ide.smoke;

import com.codenvy.ide.debug.DebugChangeVariableTest;
import com.codenvy.ide.builder.BuildFailedTest;
import com.codenvy.ide.builder.BuildSuccessedTest;
import com.codenvy.ide.runner.SpringApplicationJRebelRunAndUpdateTst;
import com.codenvy.ide.userInvite.InviteDevelopersFromGithubWithLoginFromOauthTest;
import com.codenvy.ide.userInvite.InviteAllDevelopersFromGoogleContactsWithLoginFromOauthTest;
import com.codenvy.ide.userInvite.ManageAccessAndCheckInvitationMessageTest;
import com.codenvy.ide.operation.autocompletion.AutoCompleteJspTest;
import com.codenvy.ide.operation.autocompletion.AutoCompletionCSSTest;
import com.codenvy.ide.operation.autocompletion.JavaCodeAssistantTest;
import com.codenvy.ide.operation.autocompletion.JavaDocTest;
import com.codenvy.ide.operation.autocompletion.JavaSameDependencyAndPackageTest;
import com.codenvy.ide.operation.autocompletion.JavascriptAutocompleteTest;
import com.codenvy.ide.operation.autocompletion.JspImplicitObjectsTest;
import com.codenvy.ide.operation.autocompletion.JspTagsTest;
import com.codenvy.ide.operation.autocompletion.PhpCodeAssistantTest;
import com.codenvy.ide.operation.autocompletion.RubyAutoCompletionTest;
import com.codenvy.ide.operation.browse.*;
import com.codenvy.ide.operation.contextmenu.EditorContextMenuTest;
import com.codenvy.ide.operation.contextmenu.ProjectExplorerContextMenuTest;
import com.codenvy.ide.operation.edit.outline.JavaQuickOutlineTest;
import com.codenvy.ide.operation.file.*;
import com.codenvy.ide.operation.folder.CreateFolderWithNonLatinSymbolsTest;
import com.codenvy.ide.operation.folder.DeleteFolderTest;
import com.codenvy.ide.operation.folder.RenameFolderTest;
import com.codenvy.ide.operation.java.*;
import com.codenvy.ide.operation.java.refactoing.RefactoringConstructorGenerateUITest;
import com.codenvy.ide.operation.java.refactoing.RefactoringRenameLocalVariableTest;
import com.codenvy.ide.operation.java.refactoing.RefactoringSourceGenerateAllGettersAndSettersTest;
import com.codenvy.ide.operation.java.refactoing.RefactoringSourceGenerateGettersAndSettersUITest;
import com.codenvy.ide.operation.logreader.LogReaderTest;
import com.codenvy.ide.runner.PythonApplicationRunTest;
import com.codenvy.ide.runner.SpringApplicationRunTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Evgen Vidolob
 *
 */
@RunWith(Suite.class)
@SuiteClasses({LogReaderTest.class, RubyAutoCompletionTest.class, CreateFolderWithNonLatinSymbolsTest.class,
               DeleteFolderTest.class, RenameFolderTest.class, ToggleCommentTest.class,
               DeleteCurrentLineWithKeysTest.class, FormatJavaCodeWithShortKeysTest.class, UpdateDependencyTest.class,
               MoveLineUpDownWithShortKeysTest.class, FormatJavaCodeFromEditMenuTest.class,
               RemoveBlockCommentsTest.class,
               AddBlockCommentsTest.class, MoveLineUpDownFromMenuEditTest.class, GoToLineFromHotkeyTest.class,
               UndoRedoFromShortKeyTest.class, SpringApplicationRunTest.class, PythonApplicationRunTest.class,
               SpringApplicationJRebelRunAndUpdateTst.class, JavaDocTest.class, JavaCodeAssistantTest.class,
               JavaSameDependencyAndPackageTest.class, JspTagsTest.class, AutoCompleteJspTest.class,
               JspImplicitObjectsTest.class,
               EditorContextMenuTest.class, ProjectExplorerContextMenuTest.class,
               RefactoringRenameLocalVariableTest.class,
               RefactoringConstructorGenerateUITest.class, RefactoringRenameLocalVariableTest.class,
               RefactoringSourceGenerateGettersAndSettersUITest.class,
               RefactoringSourceGenerateAllGettersAndSettersTest.class,
               BuildFailedTest.class, BuildSuccessedTest.class, OpeningFilesTest.class, ItemOrderingTest.class,
               ShowHideHiddenFilesTest.class, ProjectsListGridTest.class, UsingKeyboardTest.class,
               ManageAccessAndCheckInvitationMessageTest.class,
               InviteAllDevelopersFromGoogleContactsWithLoginFromOauthTest.class,
               InviteDevelopersFromGithubWithLoginFromOauthTest.class, CheckHilightTextTest.class,
               DeletingFilesTest.class,
               ClosingAndSaveAsFileTest.class, RenameClosedFileTest.class, RenameOpenedFileTest.class,
               JavaQuickOutlineTest.class,
               PhpCodeAssistantTest.class, AutoCompletionCSSTest.class, JavascriptAutocompleteTest.class,
               DebugChangeVariableTest.class})
public class SmokeTestSuite {

}
