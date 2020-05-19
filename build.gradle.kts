buildscript {

    repositories {
        google()
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.6.1")
        classpath(kotlin("gradle-plugin", dev.mobilehealth.reimaginedlamp.gradle.BuildConfig.kotlinVersion))


        classpath(
            "com.squareup.sqldelight:gradle-plugin",
            dev.mobilehealth.reimaginedlamp.gradle.BuildConfig.sqldelightVersion
        )
        classpath(
            "com.squareup.sqldelight:runtime",
            dev.mobilehealth.reimaginedlamp.gradle.BuildConfig.sqldelightVersion
        )

    }
}
allprojects {
    group = "dev.mobilehealth.reimaginedlamp"
    version = "1.0"

    repositories {
        jcenter()
        google()
    }
}
