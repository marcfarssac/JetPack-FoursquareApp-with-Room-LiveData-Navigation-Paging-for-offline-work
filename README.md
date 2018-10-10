# Foursquare
Simple Android app that connects to the [Foursquare API]( https://developer.foursquare.com/ ) and allows the user to search for venues by entering the name of a place.

## Getting Started

The aim of the App is to show a list of venues obtained from a backend repository. 

## Architecture

It has been moved to the [ARCHITECTURE](ARCHITECTURE.md)to keep theREADME file shorter and with more recent project information.

### Prerequisites

This App uses the latest software versions (IDE, Gradle, Plugins and JetPack collection of Android Software Components) available at the time of development. In order for the code to work the following versions shall be checked and corresponding updates shall be installed as explained in the following lines.

#### Android Studio

The Android Studio (AS) 3.2 Integrated Development Environment (IDE) available since September 24th 2018 is required for the navigation tooling used in this App. The latest Android Studio version can be downloaded [here](https://developer.android.com/studio/). In this version the Navigation editor has to be enabled from the experimental settings (File → Settings → Experimental → Editor → Enable Navigation Editor) and the related (navigation-fragment) dependencies to be implemented in the module App gradle file. 

#### Plugins

##### Kotlin in Android Studio

Due to known issues in the Kotlin plugging installed with this version it is recommended (see the [release notes](https://androidstudio.googleblog.com/2018/09/android-studio-32-available-in-stable.html)) to uninstall the plugin and install the latest available at version 1.2.71 or above.

To update the Kotlin plugin navigate to the Settings of Android Studio and look for the plugins section. Search the kotlin one and verify its version number. Uninstall the plugin and restart the IDE when needed. Install the proposed version which will be above 1.2.71. Restart AS.

#### Gradle

The project builds with the given configuration. Updating dependencies at the alpha release stage might require to replace deprecated instances' attributes.

## Test Driven Development

An initial project structure with last dependencies' versions has been created to move forward. 

## Current status (old one documented in previous README file)

10 October

Updated local data model to store exact queries' results as those can not be found from the query. Similar queries will not produce offline results at this stage.

## Author

* **Marc Farssac** - *Initial work* 4.10.2018
* **Marc Farssac** - *Implementing tests* 10.10.2018

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## Acknowledgements

* [Android developers](https://developer.android.com/docs/) - Documentation for app developers
* [Google Developers](https://developers.google.com/) - Build <b>anything</b> with Google
* [Stackoverflow](https://stackoverflow.com/) - Questions and answers

