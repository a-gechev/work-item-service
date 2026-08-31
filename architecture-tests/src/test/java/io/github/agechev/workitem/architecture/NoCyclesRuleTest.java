package io.github.agechev.workitem.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;

@AnalyzeClasses(packages = {
        "io.github.agechev.workitem.workitems",
        "io.github.agechev.workitem.users",
        "io.github.agechev.workitem.outbox"
})
class NoCyclesRuleTest {

    @ArchTest
    void theContextsAreFreeOfDependencyCycles(JavaClasses classes) {
        ArchRule rule = SlicesRuleDefinition.slices()
                .matching("io.github.agechev.workitem.(*)..")
                .should().beFreeOfCycles()
                .because("the contexts — `workitems`, `users`, `outbox` — are free of dependency cycles.\n" +
                        "- see README.md, the six rules, Rule 5 (No cycles)");
        rule.check(classes);
    }
}
