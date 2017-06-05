package org.xyc.showsome.pecan.drools;

import org.drools.runtime.rule.Activation;
import org.drools.runtime.rule.WorkingMemory;
import org.drools.runtime.rule.impl.DefaultConsequenceExceptionHandler;

/**
 * created by wks on date: 2017/1/20
 */
public class DroolExceptionHandler extends DefaultConsequenceExceptionHandler {

    @Override
    public void handleException(Activation activation, WorkingMemory workingMemory, Exception exception) {
        System.out.println(activation.getRule().getName() + " error." + exception);
    }
}
