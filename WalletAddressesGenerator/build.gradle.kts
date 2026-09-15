import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.signing)
}

android {
    namespace = "com.cloner.walletaddressesgenerator"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildToolsVersion = "37.0.0"
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use { load(it) }
}
extra["signing.keyId"] = localProperties.getProperty("signing.keyId")
extra["signing.password"] = localProperties.getProperty("signing.password")
extra["signing.secretKeyRingFile"] = localProperties.getProperty("signing.secretKeyRingFile")

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                groupId = "com.smartwalletsfinder"
                artifactId = "WalletAddressesGenerator"
                version = "1.0.0007"
                from(components["release"])
                pom {
                    name.set("WalletAddressesGenerator")
                    description.set("a comprehensive library for generating Wallet addresses based on TonKeeper, MetaMask, TrustWallet and BitCoin standards.")
                    url.set("https://www.smartwalletsfinder.com")

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("smartwallets-dev")
                            name.set("Nasser Khaledi")
                            email.set("support@smartwalletsfinder.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:git://github.com/7cloner/repository.git")
                        developerConnection.set("scm:git:ssh://github.com/7cloner/repository.git")
                        url.set("https://github.com/7cloner/repository")
                    }
                }
            }
        }
        repositories {
            maven {
                name = "LocalArtifacts"
                url = uri(layout.buildDirectory.dir("outputs/artifacts"))
            }
        }
    }
}

signing {
    sign(publishing.publications)
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
    api(libs.tonaddressgenerator)
    api(libs.swfmetamaskaddressesgenerator)
    implementation(libs.wallet.core)
    implementation(libs.bitcoinj.core)
}