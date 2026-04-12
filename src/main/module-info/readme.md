# Module Info Directory

This directory contains the `module-info.java` file for the Java Platform Module System (JPMS).

## Why is this in a separate directory?

The `module-info.java` file is kept separate from `src/main/java/` for the following reasons:

1. **Eclipse IDE Compatibility**: Eclipse has known issues with Java modules, particularly when the project uses older Java versions or has complex module configurations. Keeping `module-info.java` separate prevents Eclipse from attempting to process it during development.

2. **Build-Time Integration**: The module descriptor is added to the compilation during the Maven build process via the `build-helper-maven-plugin`, which adds this directory as a source folder only during the build.

3. **IDE Independence**: This approach allows developers to use Eclipse without module-related compilation errors while still producing proper modular JARs when building with Maven.

## How it works

The Maven build process:
1. Compiles all regular Java sources from `src/main/java/`
2. Adds `src/main/module-info/` as an additional source directory
3. Compiles `module-info.java` 
4. Packages everything into a proper modular JAR

## For Developers

- **Eclipse users**: You don't need to worry about this file - Eclipse won't see it
- **IntelliJ users**: IntelliJ handles modules better and can work with this setup
- **Maven builds**: The module descriptor is automatically included in the final JAR

If you need to modify the module descriptor, edit `src/main/module-info/module-info.java` directly.
