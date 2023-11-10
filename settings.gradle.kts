pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        // 원형 그래프를 위해 Kotlin DSL(Domain Specific Language)을 사용하여 Gradle에서 Maven 저장소 추가 (JitPack 추가)
        // uri 함수는 문자열을 URI(Uniform Resource Identifier)로 변환
        // JitPack: GitHub의 프로젝트를 Maven 종속성으로 사용할 수 있게 하는 서비스
        maven { url = uri("https://jitpack.io") }

    }
}

rootProject.name = "Team_Project"
include(":app")
 