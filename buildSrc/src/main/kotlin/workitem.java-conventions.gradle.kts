plugins {
    `java-library`
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

// The type-safe `libs` accessor is not generated inside precompiled script
// plugins (gradle/gradle#15383). This lookup is the portable equivalent.
val libs = the<VersionCatalogsExtension>().named("libs")

dependencies {
    testImplementation(platform(libs.findLibrary("junit-bom").get()))
    testImplementation(libs.findLibrary("junit-jupiter").get())
    testImplementation(libs.findLibrary("assertj-core").get())
    testImplementation(libs.findLibrary("mockito-core").get())
    testRuntimeOnly(libs.findLibrary("junit-platform-launcher").get())
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}
