
buildscript {

    repositories {
        google()
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.6.1")
        classpath(kotlin("gradle-plugin", dev.mobilehealth.reimaginedlamp.gradle.BuildConfig.kotlinVersion))
    }
}
allprojects {
    group = "dev.mobilehealth.reimaginedlamp"
    version = "1.0"

    repositories {
        jcenter()
        google()
        mavenCentral()
        maven(url = "https://dl.bintray.com/icerockdev/moko")
    }
}
