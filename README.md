# U SHORT Android Starter

Stage 1 Android-native starter using Kotlin and Jetpack Compose.

## Included
- Home screen and bottom navigation
- Auto Shorts screen with phone video picker and duration choices
- Manual Shorts workspace starter
- Pro Studio starter with editing-tool layout and interactive color sliders
- Purple / violet / pink / cyan glass-inspired visual system

## Not implemented yet
AI analysis, transcription, scene detection, real timeline editing, video rendering/export, professional color processing, cloud backend, OAuth, scheduled publishing and analytics. These require separate processing engines, backend services and official platform permissions. The current screens identify the next integration points rather than claiming those features are complete.

## Open in Android Studio
1. Install Android Studio, Android SDK 35 and JDK 17.
2. Extract the ZIP and open the `U_SHORT_Android` folder.
3. Wait for Gradle sync, then run the `app` configuration on a device or emulator.

## Build APK
Android Studio → Build → Build Bundle(s) / APK(s) → Build APK(s).
The debug APK is normally at `app/build/outputs/apk/debug/app-debug.apk`.

## Push to GitHub
Create an empty repository named `U-SHORT`, then run in the extracted project folder:

```bash
git init
git add .
git commit -m "Initial U SHORT Android app"
git branch -M main
git remote add origin https://github.com/YOUR-USERNAME/U-SHORT.git
git push -u origin main
```

Replace `YOUR-USERNAME` with your GitHub username.

Never commit API keys, OAuth secrets, signing keys or user tokens.
