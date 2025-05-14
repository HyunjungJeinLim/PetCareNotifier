plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.petcarenotifier"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.petcarenotifier"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
    }
}

dependencies {
    // AndroidX and App UI
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.core.ktx)

    // Room DB
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    // Google Sign-In
    implementation(libs.play.services.auth)

    // OAuth and HTTP client
    implementation(libs.google.api.client)
    implementation(libs.google.api.client.android)
    implementation(libs.google.api.client.gson)
    implementation(libs.google.http.client)
    implementation(libs.google.http.client.android)
    implementation(libs.google.oauth.client)
    implementation(libs.google.oauth.client.jetty)

    // Calendar API (with '@jar' to force local resolution)
    implementation("com.google.apis:google-api-services-calendar:v3-rev411-1.25.0@jar") {
        exclude(group = "org.apache.httpcomponents", module = "httpclient")
    }

    // JSON
    implementation(libs.gson)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

