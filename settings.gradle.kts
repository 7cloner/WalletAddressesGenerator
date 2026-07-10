import java.util.Properties

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}



dependencyResolutionManagement {
    val properties = Properties()
    val localProps = File(rootDir.absolutePath, "local.properties")
    if (localProps.exists()) {
        properties.load(localProps.inputStream())
    }

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven {
            url = uri("https://maven.pkg.github.com/trustwallet/wallet-core")
            credentials {
                username = properties.getProperty("gitHubUserName") as String
                password = properties.getProperty("gitHubToken") as String
            }
        }
    }
}

rootProject.name = "SWFAddressesGenerator"
include(":app")
include(":WalletAddressesGenerator")
