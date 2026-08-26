plugins {
    id("workitem.java-conventions")
    alias(libs.plugins.spring.boot)
}

dependencies {
    implementation(platform(libs.spring.boot.bom))
    implementation("org.springframework.boot:spring-boot-starter")

    implementation(project(":work-items"))
    implementation(project(":users-api"))
    implementation(project(":users-impl"))
    implementation(project(":outbox"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}