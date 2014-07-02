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
package com.codenvy.ide.factoryUrl;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/** @author Musienko Maxim */


@RunWith(Suite.class)
@Suite.SuiteClasses({AndroidAppRunInTemporaryWorkspaceTest.class,
                     AuthenticatedUserInTemporaryWsTest.class,
                     CheckFactoryURLWithDifferentProjectTypesTest.class,
                     CheckMailValidationFromFormTest.class,
                     CheckNullProjectNameTest.class,
                     CloneBitBucketTest.class,
                     CloneToTemporaryWsWithoutSshTest.class,
                     CloneToTemporaryWsWithoutSshWitCoToBranchTest.class,
                     CollaborationFeaturesInAnonimWrkSpaceTest.class,
                     CollaborationFeaturesInWithExistWrkSpaceTest.class,
                     CookiesActivateOnTheBrowserTest.class,
                     CopyPrivateProjectInReadOnlyTest.class,
                     CopyPrivateProjectToMyWorkspaceTest.class,
                     CopyToMyWorkspaceWithChangesTest.class,
                     CopyToMyWorkspaceWithMultipleProjectsTest.class,
                     CopyToMyWorkspaceWithSameProjectNameTest.class,
                     CreateAccountFromTemporaryWorkspaceTest.class,
                     CreateAccountFromTmpyWsWithOauchTest.class,
                     FactoryAsLoginUserWithOrganizationIDTest.class,
                     FactoryAsUnLoginUserWithOrganizationIDTest.class,
                     FactoryUrlAdvansedOptionsTest.class,
                     FactoryURLButtonStateTest.class,
                     FactoryUrlByOptionalWSNameTest.class,
                     FactoryUrlCustomizeUiTest.class,
                     FactoryUrlGerritHubTest.class,
                     FactoryUrlGoogleMBSClientAndroidTest.class,
                     FactoryURLOpenFileTest.class,
                     FactoryURLShareButtonsTest.class,
                     FactoryURLWithAdvancedParametersTest.class,
                     FactoryUrlWithIDCommitAndVCSBranchTest.class,
                     FactoryURLWithNotExistedIdcommitTest.class,
                     FactoryURLWithNotExistedVcsbranchTest.class,
                     FactoryURLWithoutCommitIDTest.class,
                     FactoryURLWithSpecifiedCommitIDTest.class,
                     FactoryURLWithVcsinfoFalseTest.class,
                     FactoryURLWorkingWithCommittedChangestTest.class,
                     FactoryURLWorkingWithUncommittedChangestTest.class,
                     GitInitAfterOpeningFactoryURLFormTest.class,
                     InvalidFactoryURLTest.class,
                     NonAuthenticatedUserInTemporaryWsTest.class,
                     TemporaryWorkspaceAccessibleActionsTest.class,
                     TemporaryWorkspaceBlockedActionsTest.class,
                     TemporaryWsLimitationsTst.class,
                     UIMainActionsTest.class
                    })
public class FactoryURLTestSuite {


}



