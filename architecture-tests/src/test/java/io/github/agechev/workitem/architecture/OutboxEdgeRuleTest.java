package io.github.agechev.workitem.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "io.github.agechev.workitem")
class OutboxEdgeRuleTest {

    @ArchTest
    void domainAndApplicationDoNotDependOnOutbox(JavaClasses classes) {
        ArchRule rule = noClasses()
                .that().resideInAnyPackage("..application..", "..domain..")
                .should().dependOnClassesThat().resideInAPackage("..outbox..")
                .because("only `adapters/out` may reference the `outbox` module;\n" +
                        "nothing in `domain` or `application` may.\n" +
                        "- see README.md, the six rules, Rule 6 (Infrastructure only at the edge)");

        rule.check(classes);
    }
}
