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
package com.codenvy.ide.git;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


/** @author Musienko Maxim */

@RunWith(Suite.class)
@SuiteClasses({AddFilesToIndexTest.class,
               AddFilesToIndexWithoutNewFileTest.class,
               AddRemoteRepositoryTest.class,
               CheckoutBranchTest.class,
               CheckoutToRemoteBranchTest.class,
               CheckoutToRemoteBranchWhichAlreadyHasLinkedLocalBranchTest.class,
               CheckStatusAndViewGitURLTest.class,
               ClonePrivateOrganizationRepositoryTest.class,
               ClonePublicOrganizationRepositoryTest.class,
               CloneRemotePublicPersonalRepositoryAndInviteCollaboratorTest.class,
               CloneRemotePublicPersonalRepositoryWithDifferentProjectTypesTest.class,
               CommitFilesTest.class,
               CommitFilesWithAllChangesExceptOfNewFilesTest.class,
               CreateLocalBranchTest.class,
               DeleteBranchTest.class,
               EmptyRepositoryCloneTest.class,
               FetchUpdatesAndMergeRemoteBranchIntoLocalTest.class,
               GitPullTest.class,
               HardResetToCommitTest.class,
               ImportPrivateOrganizationRepoTest.class,
               ImportPublicOrganizationRepoTest.class,
               InitializeAndDeleteLocalRepositoryTest.class,
               MergeLocalBranchIntoAnotherTest.class,
               MixedResetToCommitTest.class,
               PushChangeNonUpdatedRepoTest.class,
               PushingChangesTest.class,
               RemoveFilesFromIndexAndWorkingDirectoryTest.class,
               RemoveFilesFromIndexOnlyTest.class,
               RemoveRemoteRepositoryTest.class,
               RenameAndDeleteRemoteBranchTest.class,
               RenameLocalBranchTest.class,
               ResetIndexTest.class,
               ShellGitMainCommandTest.class,
               ShellGitRemoteRepositoriesTest.class,
               ShellGitStatusCommandTest.class,
               SoftResetToCommitTest.class,
               ViewHistoryAndDiffWithPreviousVersionTest.class,
               WorkingWithMergeConflictTest.class,
               WorkingWithPullConflictsTest.class})
public class GITTestSuite {

}


