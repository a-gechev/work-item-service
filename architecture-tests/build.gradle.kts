plugins {
    id("workitem.java-conventions")
}

dependencies {
    testImplementation(libs.archunit.junit5)

    testImplementation(project(":work-items"))
    testImplementation(project(":users-api"))
    testImplementation(project(":users-impl"))
    testImplementation(project(":outbox"))
    testImplementation(project(":app"))
}
