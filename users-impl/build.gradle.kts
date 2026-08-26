plugins {
    id("workitem.java-conventions")
}

dependencies {
    implementation(project(":users-api"))
    implementation(project(":outbox"))
}