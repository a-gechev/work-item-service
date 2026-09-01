plugins {
    id("workitem.java-conventions")
}

dependencies {
    implementation(platform(libs.spring.boot.bom))
    implementation("org.springframework:spring-context")

    implementation(project(":users-api"))
    implementation(project(":outbox"))
}