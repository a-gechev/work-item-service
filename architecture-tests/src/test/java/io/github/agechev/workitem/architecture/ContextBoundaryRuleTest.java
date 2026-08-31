package io.github.agechev.workitem.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "io.github.agechev.workitem")
class ContextBoundaryRuleTest {

    @ArchTest
    void nothingOutsideUsersReferencesUsersImpl(JavaClasses classes) {
        ArchRule rule = noClasses()
                .that().resideOutsideOfPackage("..users..")
                .should().dependOnClassesThat().resideInAPackage("..users.impl..")
                .because("a context is reachable only through its published `api` module.\n" +
                        "Nothing outside `users` may reference `users.impl`.\n" +
                            "- see README.md, the six rules, Rule 2 (Context boundary)");
        rule.check(classes);
    }
}
