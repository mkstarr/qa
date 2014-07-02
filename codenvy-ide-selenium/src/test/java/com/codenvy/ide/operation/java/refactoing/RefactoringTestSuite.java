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
package com.codenvy.ide.operation.java.refactoing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Musienko Maxim
 *
 */

@RunWith(Suite.class)
@SuiteClasses({RefactoringAllowSettersForFinalFieldChekBoxTest.class,
               RefactoringClassFromPackageMenuTest.class,
               RefactoringConstructorGenerateAllAccessTest.class,
               RefactoringConstructorGenerateUITest.class,
               RefactoringConstructorOmitCallConstructorSuperTest.class,
               RefactoringConstructorWithoutCommentsTest.class,
               RefactoringRenameCheckingPopupMessagesTest.class,
               RefactoringRenameLocalVariableTest.class,
               RefactoringRenameSharedVariableTest.class,
               RefactoringSourceGenerateAllGettersAndSettersTest.class,
               RefactoringSourceGenerateAllGettersTest.class,
               RefactoringSourceGenerateAllSettersTest.class,
               RefactoringSourceGenerateCustomGettersAndSettersTest.class,
               RefactoringSourceGenerateGettersAndSettersUITest.class,
               RefactoringTestSuite.class,
               RefactoringWithoutGenerateMethodCommentsTest.class,
               RefactService.class})
public class RefactoringTestSuite {

}
