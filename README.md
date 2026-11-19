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


## 17. Strict Functionality, Tool Organization & User Experience Requirements

**Critical acceptance rule: U SHORT is a real Android application, not a visual prototype.** Every control presented as available must execute its intended operation. Do not ship fake buttons, simulated progress, fabricated success messages, or unfinished tools labelled as functional.

### A. Complete functionality
- Wire every button, menu item, slider, switch, dropdown, timeline control, import, upload, save, delete, undo, redo and export action to real behavior.
- Connect AI tools to real configured AI services or supported on-device engines.
- Show truthful processing status and progress only when measurable. Provide loading, success, error, retry and cancellation states where supported.
- Clearly disclose any backend, API key, subscription, network, or external authorization requirement and provide its connection workflow.
- Do not claim a feature is complete until it has been implemented and tested.

### B. Professional tool organization
Organize the application into these discoverable categories:
1. **CREATE:** Auto Shorts, Manual Shorts, AI Script Writer, AI Storyboard.
2. **EDIT:** Video Editor, Timeline, Transitions, Effects, Text & Captions, Audio Pro.
3. **COLOR LAB:** Color Wheels, Curves, HSL, LUTs, Color Mixer, Scopes.
4. **AI TOOLS:** Hook Master, Retention Booster, Background Remover, Video Enhancer, Voice Dubbing, Thumbnail Battle.
5. **DISCOVER:** Viral Hunter, Trending Topics, Competitor Intelligence, Content Ideas.
6. **PUBLISH:** Social Accounts, Content Calendar, Auto Scheduler, Smart Repost, Publishing Queue.
7. **ANALYTICS:** Channel Performance, Video Statistics, Audience Insights, A/B Experiments.

### C. Clear and intelligent interface
- Give each tool a clear name and recognizable icon; keep layouts consistent and uncluttered.
- Use expandable panels for advanced settings and show essential controls first.
- Add tooltips for complex features, instant tool search, favorites, simple consistent navigation and a clear back path from every screen.

### D. Professional editing workspace
- Keep the preview at the top and a dedicated timeline below it.
- Label video, audio, text and effects tracks clearly.
- Provide context-sensitive editing controls, timeline zoom/navigation, and always-accessible undo/redo.
- Support project saving and automatic recovery.
- Give Color Lab a dedicated workspace for color controls, scopes, presets and before/after comparison.

### E. Real-time feedback and recovery
Every operation must provide truthful loading/progress information, a success confirmation or clear error, and retry/cancel/recovery options when supported. Never leave a frozen screen or unexplained failure.

### F. Connected end-to-end workflow
Support project continuity across:
**Import Video → Analyze Video → Generate Shorts → Select Version → Edit → Color Grade → Add Audio → Generate Captions → Preview → Export → Schedule → Publish → Analyze Performance.**
Moving between stages must not discard the project or editing progress.

### G. Quality assurance and release gate
Before declaring the app complete, test every button and interactive component, tool, navigation path, video import/export, timeline edit, project save/restore, AI error path, account authorization and officially supported publishing flow. Test multiple Android screen sizes and fix crashes, broken interactions and build errors.

Maintain a functionality checklist using exactly these status meanings:
- **Fully implemented and tested**
- **Implemented but requires external configuration**
- **Not yet implemented**

### Current implementation status (honest baseline)
This is a staged roadmap, not a claim that all features already work:
- **Fully implemented and tested:** No feature is certified by this checklist yet; test evidence must be recorded before applying this status.
- **Implemented but requires external configuration:** None certified yet.
- **Not yet implemented / not yet verified:** The complete feature set above, including production-grade editing, rendering/export, AI services, social publishing and analytics, must be implemented and individually tested before release.

**Absolute release rule:** deliver stable, genuinely usable stages incrementally. Beginners must be able to understand the interface while advanced creators retain access to professional controls. Everything organized, accessible and functional; no fake features or broken buttons.

## Latest development increment — navigation and in-app AI connection

- Added animated fade/slide transitions when switching primary workspaces.
- Added a searchable, categorized tool library covering CREATE, EDIT, COLOR LAB, AI TOOLS, DISCOVER, PUBLISH and ANALYTICS.
- Connected the Create hub to the Auto Shorts and Manual Shorts screens.
- Added an in-app U SHORT AI chat screen with a configurable HTTPS backend endpoint saved in Android app preferences.
- The AI client sends `POST { "message": "..." }` and expects `{ "reply": "..." }`. Provider credentials must remain on the server; do not embed API keys in the APK.
- Added Android INTERNET permission for this opt-in network connection.

### AI backend contract
Configure the full chat endpoint URL in the AI screen. The server must accept JSON POST requests with a `message` string and return JSON with a non-empty `reply` string. It should authenticate requests, apply rate limits, protect secrets, and return useful HTTP error codes. Without a configured reachable endpoint, live AI responses are unavailable and the app reports that instead of fabricating a reply.

### Status of this increment
- **Implemented in source; awaiting CI build verification:** animated workspace navigation, searchable category directory, in-app AI chat client and endpoint persistence.
- **Requires external configuration:** live AI answers (a deployed backend endpoint and provider/model credentials on that backend).
- **Not yet implemented:** full automatic clip detection and rendering, multi-track timeline operations, advanced color pipeline/scopes/LUT processing, production audio tools, cloud projects, OAuth social publishing, scheduler and analytics.

The tool directory is an organized roadmap and does not imply that every listed tool is already operational. Do not mark those tools complete until each implementation passes its own tests.

## Google Gemini integration

The Android U SHORT assistant now supports a dedicated Gemini backend. The API key is kept server-side; do not put it in Android source code, APK resources, or GitHub commits.

### Deploy the backend
1. Create a Gemini API key in [Google AI Studio](https://aistudio.google.com/app/apikey).
2. In [Render](https://render.com/), create a new Blueprint deployment from this GitHub repository. Render reads the root `render.yaml` and deploys the Node service from `server/`.
3. In the Render service environment, set:
   - `GEMINI_API_KEY`: your key from Google AI Studio.
   - `USHORT_APP_TOKEN`: a long, random private token that you create. Do not commit it to GitHub or share it publicly.
   - `GEMINI_MODEL`: optional; defaults to `gemini-3.8-flash`.
4. Wait for deployment and check `https://YOUR-SERVICE.onrender.com/health`. It should return JSON with `ok: true`.
5. In U SHORT → AI, enter `https://YOUR-SERVICE.onrender.com/api/chat` and the same `USHORT_APP_TOKEN`, then save the connection.
6. Send a test message. The app expects a JSON response containing `reply`.

### Backend contract
- `POST /api/chat`
- Header: `Authorization: Bearer YOUR_USHORT_APP_TOKEN`
- JSON body: `{ "message": "Write a 20-second short video script about..." }`
- Successful response: `{ "reply": "...", "model": "..." }`
- `GET /health` checks service readiness.

The backend uses Google's official `@google/genai` SDK and the Gemini Interactions API. The app token is a basic private-access gate for a personal test deployment, not a replacement for per-user authentication, Firebase App Check, or production abuse prevention. Add proper user authentication and quotas before opening the service to the public. Never expose either secret in screenshots, chat messages, app source, or public commits.

### Current integration status
- **Implemented in source:** Android assistant sends authenticated requests to the configured backend; Node server calls Gemini and returns its answer; server-side API key; request size limit, rate limiting, security headers and health endpoint.
- **Requires external configuration:** Gemini API key, deployed backend URL and matching private app token.
- **Not yet verified:** Live end-to-end Gemini response and deployed service; these require deployment and valid secrets. No live Gemini connection is claimed until the user configures and tests it.
