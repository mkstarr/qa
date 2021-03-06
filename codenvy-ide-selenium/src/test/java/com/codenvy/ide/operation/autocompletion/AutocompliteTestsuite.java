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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/** @author Musienko Maksim */
@RunWith(Suite.class)
@SuiteClasses({AutoCompleteJspTest.class,
               AutoCompletionCSSTest.class,
               AutoCompletionHTMLTest.class,
               AutoCompletionJavaScriptInContiguousEditors.class,
               AutoCompletionXMLTest.class,
               JavaCodeAssistantTest.class,
               JavaDocTest.class,
               JavaSameDependencyAndPackageTest.class,
               JavascriptAutocompleteTest.class,
               JspImplicitObjectsTest.class,
               JspTagsTest.class,
               PhpCodeAssistantTest.class,
               RubyAutoCompletionTest.class})
public class AutocompliteTestsuite {

}
