# SkipTheSpire

A lightweight Slay the Spire mod that skips the startup splash animation and enters the main menu directly.

## Features

- Skip `SplashScreen.update()` on startup to reduce boot waiting time.
- Dispose splash texture safely to avoid leaking one texture object.
- Optional startup safety patch for `spireTogether/skindex`:
  - suppresses `Error loading pixmap` crash path
  - allows game startup to continue when that specific crash happens

## Compatibility

- Slay the Spire: `12-18-2022`
- ModTheSpire: `3.30.3`
- Java target: `1.8` 

## Build

### Prerequisites

- JDK (8+; project compiles with modern JDK and emits Java 8 bytecode)
- Local Slay the Spire install (`desktop-1.0.jar`)
- `ModTheSpire.jar`

### Command

```powershell
cd D:\Desktop\SkipTheSpire
.\gradlew.bat clean build
```

If `STEAM_PATH` is not set:

```powershell
.\gradlew.bat clean build -Psteam.path="E:\SteamLibrary\steamapps"
```

If `ModTheSpire.jar` is in a custom location:

```powershell
.\gradlew.bat clean build -Pmts.jar="D:\path\to\ModTheSpire.jar"
```

You can also print resolved jars:

```shell
.\gradlew.bat printResolvedJars
```

## Output

- `build/libs/SkipTheSpire-1.0.0.jar`

## Install

1. Copy `SkipTheSpire-1.0.0.jar` into your Slay the Spire `mods` directory.
2. Enable `SkipTheSpire` in ModTheSpire.
3. Start the game.

## License

MIT
