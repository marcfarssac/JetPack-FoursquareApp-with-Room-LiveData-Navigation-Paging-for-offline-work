# Foursquare
Simple Android app that connects to the Foursquare API ( https://developer.foursquare.com/ ) and allows the user to search for venues by entering the name of a place.

## Getting Started

The App will use the JetPack collection of Android Software Components' latest versions to use the latest features of the Android SDK and to seek for the longest life of the App, also knowing that this may bring some issues related to the lack of experience and scare availability of documentation. Explanations of the solution will be added in this document to a reasonable level of detail.

### Prerequisites

This App uses the latest software versions (IDE, Gradle, Plugins and JetPack collection of Android Software Componenets) available at the time of development. In order for the code to work the following versions shall be chekced and corresponding updates shall be performed accordingly:

#### Android Studio

The Android Studio (AS) 3.2 Integrated Development Environment (IDE) available since September 24th 2018 is required for the navigations tooling used in this App. The latest Android Studio version can be downloaded [here](https://developer.android.com/studio/)- 

#### Plugins

Due to known issues in the Kotlin pluggin installed with this version it is recommended (see the [release notes](https://androidstudio.googleblog.com/2018/09/android-studio-32-available-in-stable.html)) to uninstall the plugin and install the latest available at version 1.2.71 or above.

To update the Kotlin plugin navigate to the Settings of Android Studio and look for the plugins section. Search the kotlin one and verify its version number. Uninstall the plugin and restard the IDE when needed. Install the proposed version which will be above 1.2.71. Restar AS.

#### Gradle

The project builds with the given configuration. Updating dependencies at the alpha release stage might require to replace deprecated instances' atrributes.

