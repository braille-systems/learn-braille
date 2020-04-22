# Contributing to Learn Braille

## Build

### Requirements
`android-studio >= 3.3`

Installation: see [developer.android.com/studio](https:://developer.android.com/studio)
For building and running the app, your either need to download a virtual Android device image (this can be done from within the Android Studio) or connect a physical device (with developer options enabled and USB debugging turned on) via USB cable.

### Setting up environement

Clone the repository and open 'android/' subfolder as an Android Studio project. Gradle build will start automatically and all libraries will be downloaded (an Internet connection is required). Then, if you have already set up an emulator or connected a physical device, you will be able to launch it (click green triangle screen button in the toolbar above the code editor).

## Coding style

- Kotlin [coding conventions](https://kotlinlang.org/docs/reference/coding-conventions.html) + Java [google style guides](https://google.github.io/styleguide/javaguide.html) and [oracle](https://www.oracle.com/technetwork/java/codeconvtoc-136057.html).
- Also look around each time you do something new to see, how such thing was formatted and impelemented before.
- Set up Android Studio kotlin code style `editor -> code style -> kotlin -> set from -> predefined -> Kotlin style guide`.
- Apply autoformatting to edited files each time before commit.

## Commits

- Follow [this guide](https://chris.beams.io/posts/git-commit/) when you write commit message.
- If commit is assigned to issue, add #<issue-number> at the beginning.
- If commit is assigned to trello card, add <sprint-number>.<card-number> annotation and the beginning of commit message.
  
## Git workflow

- Follow this paper: [en](https://nvie.com/posts/a-successful-git-branching-model/), [ru](https://habr.com/ru/post/106912/)

