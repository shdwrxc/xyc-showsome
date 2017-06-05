package org.xyc.showsome.pecan.drools;

import java.io.InputStreamReader;
import java.io.Reader;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.compiler.PackageBuilder;
import org.junit.Test;

/**
 * created by wks on date: 2017/1/20
 */
public class DroolTest {

    @Test
    public void execRule() throws Exception {
        // 读写DRL文件
        Reader source = new InputStreamReader(DroolTest.class.getResourceAsStream("/rules/test.drl"));

        //添加DRL源到PackageBuilder，可以添加多个
        PackageBuilder builder = new PackageBuilder();
        builder.addPackageFromDrl(source);

        //添加package到RuleBase（部署规则集）
        RuleBase ruleBase = RuleBaseFactory.newRuleBase();
        ruleBase.addPackages(builder.getPackages());

        WorkingMemory workingMemory = ruleBase.newStatefulSession();

        DroolSample sample = new DroolSample();
//        sample.setStr("hello");
        sample.setI(0);
        sample.addList("hi");

        workingMemory.insert(sample);
        workingMemory.fireAllRules();
    }
}
