package io.github.agechev.workitem.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "io.github.agechev.workitem")
class DirectionRuleTest {

    @ArchTest
    void usersNeverDependsOnWorkitems(JavaClasses classes) {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..users..")
                .should().dependOnClassesThat().resideInAPackage("..workitems..")
                .because("the dependency between contexts runs one way only.\n" +
                        "`users` never depends on `workitems`.\n" +
                        "- see README.md, the six rules, Rule 3 (Direction)");
                rule.check(classes);
    }
}
