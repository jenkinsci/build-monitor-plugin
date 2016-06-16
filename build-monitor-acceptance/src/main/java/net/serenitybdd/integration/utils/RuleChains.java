package net.serenitybdd.integration.utils;

import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

import java.util.Arrays;
import java.util.List;

import static net.serenitybdd.integration.utils.ListFunctions.head;
import static net.serenitybdd.integration.utils.ListFunctions.tail;

public class RuleChains {
    private RuleChains(){}
    public static RuleChain from(TestRule... rules) {
        return from(Arrays.asList(rules));
    }

    private static RuleChain from(List<TestRule> testRules) {
        return chained(testRules);
    }


    public static <R extends TestRule> RuleChain chained(List<R> customRules) {
        return chained(RuleChain.emptyRuleChain(), customRules);
    }

    public static <R extends TestRule> RuleChain chained(RuleChain acc, List<R> customRules) {
        return customRules.isEmpty()
                ? acc
                : chained(acc.around(head(customRules)), tail(customRules));
    }
}
