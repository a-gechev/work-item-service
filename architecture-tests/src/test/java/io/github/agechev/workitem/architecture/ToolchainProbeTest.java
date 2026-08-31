package io.github.agechev.workitem.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static org.assertj.core.api.Assertions.assertThat;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

/**
 * Not one of the six rules. This only exists to prove that ArchUnit 1.5.0 runs under JUnit 6.1.3 in
 * this repo, before any rule that is meant to mean something is written against that toolchain.
 */
class ToolchainProbeTest {

    @Test
    void archUnitCanImportAndAssertAgainstThisRepositorysClasses() {
        JavaClasses imported = new ClassFileImporter()
                .importPackages("io.github.agechev.workitem.workitems");

        assertThat(imported).isNotEmpty();

        classes()
                .that().haveSimpleName("WorkItemEvent")
                .should().resideInAPackage("..workitems.domain..")
                .check(imported);
    }
}
