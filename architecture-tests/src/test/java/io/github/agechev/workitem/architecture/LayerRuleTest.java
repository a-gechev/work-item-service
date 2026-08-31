package io.github.agechev.workitem.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "io.github.agechev.workitem")
class LayerRuleTest {

    @ArchTest
    void domainDoesNotDependOnFrameworksApplicationOrAdapters(JavaClasses classes) {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        "org.springframework..",
                        "jakarta.persistence..",
                        "com.fasterxml..",
                        "..application..",
                        "..adapters..")
                .because("The domain layer must depend on no framework, no infrastructure, "
                + "and no outer layer - see README.MD's six rules, Rule 1(Layer)");
        rule.check(classes);
    }
}
