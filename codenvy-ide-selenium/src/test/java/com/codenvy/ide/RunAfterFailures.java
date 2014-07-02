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

import org.junit.internal.runners.model.MultipleFailureException;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.List;


/**
 * @author <a href="mailto:oksana.vereshchaka@gmail.com">Oksana Vereshchaka</a>
 *
 */
public class RunAfterFailures extends Statement {

    private final Statement fNext;

    private final Object fTarget;

    private final List<FrameworkMethod> fAfterFailures;

    public RunAfterFailures(Statement next, List<FrameworkMethod> afterFailures,
                            Object target) {
        fNext = next;
        fAfterFailures = afterFailures;
        fTarget = target;
    }

    @Override
    public void evaluate() throws Throwable {
        List<Throwable> fErrors = new ArrayList<Throwable>();
        fErrors.clear();
        try {
            fNext.evaluate();
        } catch (Throwable e) {
            fErrors.add(e);
            for (FrameworkMethod each : fAfterFailures) {
                try {
                    each.invokeExplosively(fTarget, e);
                } catch (Throwable e2) {
                    fErrors.add(e2);
                }
            }
        }
        if (fErrors.isEmpty()) {
            return;
        }
        if (fErrors.size() == 1) {
            throw fErrors.get(0);
        }
        throw new MultipleFailureException(fErrors);
    }

}
