package net.serenitybdd.integration.utils;

import java.util.List;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

public class RuleChains {
    public static RuleChain from(TestRule... rules) {
        return from(List.of(rules));
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
                : chained(acc.around(ListFunctions.head(customRules)), ListFunctions.tail(customRules));
    }
}
