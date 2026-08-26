rootProject.name = "work-item-service"

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        mavenCentral()
    }
}

include(
    "work-items",
    "users-api",
    "users-impl",
    "outbox",
    "app",
)