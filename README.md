# Poker_Deck_Builder

Western themed deckbuilder that uses poker as the basis.

## Tech stack
- LibGDX
- Android-first build target

## Current playable prototype
- Tap the screen to draw cards from a shuffled poker starter deck.
- Hand caps at 7 cards and older cards spill into discard.
- When the deck is empty, discard is reshuffled back in.
- Score increases from drawn card values to represent run momentum.

## Build commands
From the repository root:

```bash
./gradlew :core:test
./gradlew -PincludeAndroid=true :android:assembleDebug
```

Use Android Studio to run the `android` module on a device/emulator.
