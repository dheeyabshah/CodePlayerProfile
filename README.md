# IntelliJ Code Player Profile

<!-- Plugin description -->
**IntelliJ Code Player Profile** is a plugin that allows the user to see an individual profile of a Git repository contributor based on their commit history.
<!-- Plugin description end -->

### Table of contents

In this README, we will highlight the following elements of project creation:

- [Getting started](#getting-started)
- [Running the plugin](#running-the-plugin) 
- [Plugin structure](#plugin-structure) 

## Getting started

After opening your project in IntelliJ IDEA, set the proper <kbd>SDK</kbd> to Java in version `17` within the [Project Structure settings].
Then build the project using Gradle 

## Running the plugin

To run the plugin, click the 'Run Plugin' button this will take you to a new window where you can select any project with a git repository by pressing the open button.
To see the details of the user, select the 'Contributor Profiles' button on the left side-bar of the window 
When opening a project for the first time, you can immediately see all the information. 
When reopening a project, you will need to build the project by pressing the build icon and minimise and reopen your window once the build is complete to view the information. 

## Plugin structure

A IntelliJ Code Player Profile repository contains the following content structure:

```
.
├── build/                  Output build directory
├── gradle
│   ├── wrapper/            Gradle Wrapper
│   └── libs.versions.toml  Gradle version catalog
├── run/                    Predefined Run/Debug Configurations
├── src                     Plugin sources
│   ├── main
│       ├── kotlin/         Kotlin production sources
│       └── resources/      Resources - plugin.xml
├── build.gradle.kts        Gradle configuration
├── CHANGELOG.md            Full change history of template 
├── gradle.properties       Gradle configuration properties
├── gradlew                 *nix Gradle Wrapper script
├── gradlew.bat             Windows Gradle Wrapper script
├── LICENSE                 License, MIT by default
├── README.md               README
└── settings.gradle.kts     Gradle project settings
```
