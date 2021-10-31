# Contributing to Learn Braille

## Build

To work with the project you need `android-studio >= 3.3`, you can get it [here](https:://developer.android.com/studio).

For building and running the app, your either need to download a virtual Android device image (this can be done from within the Android Studio) or connect a physical device (with developer options enabled and USB debugging turned on) via USB cable.

## Git workflow

- Write short and informative commit messages. Mention the issue you're working on (begins with `#`), e. g. `implement something(#0)`. [How to Write a Git Commit Message](https://chris.beams.io/posts/git-commit/).
- Follow [a successful Git branching model](https://nvie.com/posts/a-successful-git-branching-model/).

## Coding style

- Kotlin [coding conventions](https://kotlinlang.org/docs/reference/coding-conventions.html) + Java [google style guides](https://google.github.io/styleguide/javaguide.html) and [oracle](https://www.oracle.com/technetwork/java/codeconvtoc-136057.html).
- Also look around each time you do something new to see, how such a thing was formatted and implemented before.
- Set up Android Studio proper Kotlin code style `editor -> code style -> kotlin -> set from -> predefined -> Kotlin style guide`.
- Apply autoformatting to edited files each time before commit.

## Adding content

There is a handy DSL that allows writing content in a typesafe way.

- All app content should be placed into `com.github.braillesystems.learnbraille.res` package.
- Use `DslTest.kt` file as DSL tutorial.

Information correctness should be checked in compile-time or during app initialization runtime as much as possible. If some additional info is needed, do not hardcode it. Just request the new DSL feature via GitHub Issues.

Adding rules, prevent lambda of capturing context that will be invalid next time the fragment entered, so use `Fragment.getString` outside of lambdas.

#### Adding course

1. Create lessons by `lessons` delegate.
2. Create a course in `CourseBuilder` and add lessons to it.

Always use `com.github.braillesystems.learnbraille.res.content` value to get materials, they are indexed here in a proper way.

#### Adding deck

1. Add a new deck tag to `DeckTags`.
2. Map tag to deck's predicate in `DecksBuilder`.
3. Map deck's tag to user-visible string in `deckTagToName`.

#### Adding materials

1. Create materials by one of delegates: `markers` or `symbols`.
2. Add created materials to the `contens` (`materials` delegate).
3. Add to `inputSymbolPrintRules` and `showSymbolPrintRules`, or to `inputMarkerPrintRules` and `showMarkerPrintRules`.

Symbols that are not from a particular alphabet and do not exist on the classical American keyboard should be treated as special and be added via `enum class`.

New materials can be marked as known by default in `knownMaterials` (`known` delegate).

## Database

Database scheme is described [here](https://github.com/braille-systems/learn-braille/blob/master/database.md).
