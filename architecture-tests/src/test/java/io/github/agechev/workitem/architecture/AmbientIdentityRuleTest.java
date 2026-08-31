package io.github.agechev.workitem.architecture;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "io.github.agechev.workitem")
class AmbientIdentityRuleTest {

    @ArchTest
    void domainAndApplicationDoNotReadASecurityContext(JavaClasses classes) {
        DescribedPredicate<JavaClass> readsASecurityContext = JavaClass.Predicates.resideInAnyPackage("org.springframework.security..", "javax.security.auth..")
                .or(JavaClass.Predicates.belongToAnyOf(java.security.Principal.class));

        ArchRule rule = noClasses()
                .that().resideInAnyPackage("..domain..", "..application..")
                .should().dependOnClassesThat(readsASecurityContext)
                .because("the acting `UserId` is an explicit parameter.\n" +
                                "Nothing in `domain` or `application` reads a security context.\n" +
                        "- see README.md, the six rules, Rule 4 (No ambient identity)");
        rule.check(classes);
    }

}
