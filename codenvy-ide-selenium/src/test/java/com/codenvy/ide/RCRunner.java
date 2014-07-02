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
package com.codenvy.ide;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.util.List;

/**
 * @author <a href="mailto:oksana.vereshchaka@gmail.com">Oksana Vereshchaka</a>
 *
 */
public class RCRunner extends BlockJUnit4ClassRunner {

    /**
     * @param klass
     *         Test class
     * @throws InitializationError
     *         if the test class is malformed.
     */
    public RCRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    /*
     * Override withAfters() so we can append to the statement which will invoke the test
     * method. We don't override methodBlock() because we wont be able to reference
     * the target object.
     */
    @Override
    protected Statement withAfters(FrameworkMethod method, Object target, Statement statement) {
        statement = super.withAfters(method, target, statement);
        return withAfterFailures(method, target, statement);
    }

    protected Statement withAfterFailures(FrameworkMethod method, Object target, Statement statement) {
        List<FrameworkMethod> failures = getTestClass().getAnnotatedMethods(AfterFailure.class);
        return new RunAfterFailures(statement, failures, target);
    }
}
