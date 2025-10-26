# smartFarm-Kotlin
Smart Weather and Finance Companion for Farmers

# **Configure `keys.properties`**

Create a file named `keys.properties` in the root directory with the following content:

```properties
# keys.properties
# Place this file in the root directory of your project
# DO NOT commit this file to version control!
# Add keys.properties to your .gitignore file

# Get your free API key from: https://www.weatherapi.com/
# Sign up and get your API key, then paste it below
WEATHER_API_KEY=your_api_key_here

MAP_API_KEY="your_google_maps_api_key_here"

```

### Explanation of Properties

- `WEATHER_API_KEY`: The KEY is used for weather API

### Ensure the File is Ignored in Version Control

To prevent accidental commits of sensitive information, make sure `keys.properties` is included in
the `.gitignore` file:

```gitignore
keys.properties
```

### How the File is Used in `build.gradle.kts`

The properties are loaded in the build script and used to fetch weather data from respective API
configuration:

```kotlin
import java.util.Properties
import java.io.FileInputStream


android {
    // others code

        defaultConfig {
        applicationId = "com.example.smartfarm"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        // Load API key from keys.properties
        val keysPropertiesFile = rootProject.file("keys.properties")
        val keysProperties = Properties()
        if (keysPropertiesFile.exists()) {
            keysProperties.load(FileInputStream(keysPropertiesFile))
        }

        buildConfigField(
            "String",
            "WEATHER_API_KEY",
            "\"${keysProperties.getProperty("WEATHER_API_KEY", "")}\""
        )

         manifestPlaceholders["MAP_API_KEY"] = keysProperties.getProperty("MAP_API_KEY", "")

        buildConfigField(
            "String",
            "MAP_API_KEY",
            "\"${keysProperties.getProperty("MAP_API_KEY", "")}\""
        )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }
    

 buildFeatures {
        compose = true
        buildConfig = true
    }

]


# Smart Android App - Clean Architecture Folder Structure

```
