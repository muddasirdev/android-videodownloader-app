# Android Video Downloader

A Java Android application project with separate screens for media associated with Facebook, WhatsApp, YouTube, and ShareChat. It uses Android Views, WebView-based components, and third-party parsing/downloading libraries.

## Included in the source

- App sections for the listed platforms
- Image and video tabs and media browsing UI
- Permission handling via Dexter
- Image loading with Glide
- HTML parsing with Jsoup
- AgentWeb and Downloader libraries
- AndroidNetworking and a YouTube extractor dependency
- View Binding and Data Binding

The dependency list describes the codebase's tools; it does not mean every platform flow is guaranteed to work today. External sites and apps change their markup and delivery behavior, and the project includes an old snapshot YouTube extractor dependency. Verify each flow on current devices before relying on it.

## Technology and Android configuration

- Java, Android Views
- Minimum SDK 21; compile and target SDK 33
- Java 8 source and target compatibility
- AndroidX AppCompat, Material Components, ConstraintLayout, Navigation, and SwipeRefreshLayout

## Project layout

- `app/src/main/java` — activities, fragments, and application code
- `app/src/main/res` — layouts, navigation, and resources
- `app/build.gradle` — SDK settings and dependencies
- `gradle/`, `gradlew`, `gradlew.bat` — Gradle wrapper

## Build and run

Open the repository in Android Studio, sync Gradle, and run the `app` configuration on an API 21+ device or emulator.

From a shell:

```sh
./gradlew assembleDebug
```

On Windows:

```bat
gradlew.bat assembleDebug
```

The project tracks a `local.properties` file. Android SDK paths in that file are machine-specific; set your local SDK path in Android Studio and avoid committing personal machine configuration. No build result is claimed by this document.

## Distribution and content rights

Only download or redistribute content when you have the rights and authorization to do so. Review the terms of service and applicable copyright rules for each platform. The app's name and platform references do not imply affiliation or endorsement by Facebook, WhatsApp, YouTube, or ShareChat.

## License

No license file is included. Contact the repository owner before reuse or distribution.
