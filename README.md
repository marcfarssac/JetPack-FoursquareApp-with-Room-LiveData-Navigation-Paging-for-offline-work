# Foursquare
Simple Android app that connects to the [Foursquare API]( https://developer.foursquare.com/ ) and allows the user to search for venues by entering the name of a place.

## Getting Started

The aim of the App is to show a list of venues obtained from a backend repository. 

Data will be cached locally on devices running the App using the [Room persistance library](https://developer.android.com/topic/libraries/architecture/room). Thanks to the [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) observable data holder class, local data will be updated whenever changes occur on the backend. This will allow the user to view the list of venues also off-line. 

Since the number of venues might be very big, the App uses the [Page Library](https://developer.android.com/topic/libraries/architecture/paging/) to loads pages of data gracefully, meaning that searches resulting in big amounts of data would be called in chunks of a few items called pages. The library manages the loading of data when scrolling down the list triggering the required actions to be supplied with additional data. Calls will be made in the first place to the local database and then to the remote repository. 

An overflow menu / or a navigation drawer with Geolocation related features will let the user enable current position detection and search based on the user current position. Thanks to the [Navigation component](https://developer.android.com/topic/libraries/architecture/navigation/) a transition to a fragment with a map showing the venues'search results will be displayed. [Navigation actions](https://developer.android.com/topic/libraries/architecture/navigation/navigation-implementing) a res/navigation/mobile_navigation.xml resource and related transition files let the user navigate to the map results page. 

The [Google Maps Platform](https://cloud.google.com/maps-platform/maps/) will supply the map. An billing enabled Google Cloud account is required for this step to work (note that the [google services file](https://support.google.com/firebase/answer/7015592?hl=en) is not included in this repository for obvious billing related constraints)

Making use of the [LocatoinServices API](https://developers.google.com/android/reference/com/google/android/gms/location/LocationServices) the App will use the last known position of the user while trying to update it periodically when good gps signal detection is available. Different strategies are applied for devices running Android N and below or Android-O, since the last emphasizes battery draingage reduction on background services, using a Broadcast reciever instead when the App is in the Foreground. 

The App uses the [JetPack](https://developer.android.com/jetpack/) collection of Android Software Components latest versions' and features of the Android SDK and [the last stable release](https://developer.android.com/studio/releases/) of the [Android Studio] Integrated Development Environment. 

### Prerequisites

This App uses the latest software versions (IDE, Gradle, Plugins and JetPack collection of Android Software Componenets) available at the time of development. In order for the code to work the following versions shall be chekced and corresponding updates shall be installed as explained in the following lines.

#### Android Studio

The Android Studio (AS) 3.2 Integrated Development Environment (IDE) available since September 24th 2018 is required for the navigations tooling used in this App. The latest Android Studio version can be downloaded [here](https://developer.android.com/studio/). In this version the Navigation editor has to be enabled from the experimental settings (File → Settings → Experimental → Editor → Enable Navigation Editor) and the related (navigation-fragment) dependencies to be implemented in the mobule App gradle file. 

#### Plugins

##### Kotlin in Android Studio

Due to known issues in the Kotlin pluggin installed with this version it is recommended (see the [release notes](https://androidstudio.googleblog.com/2018/09/android-studio-32-available-in-stable.html)) to uninstall the plugin and install the latest available at version 1.2.71 or above.

To update the Kotlin plugin navigate to the Settings of Android Studio and look for the plugins section. Search the kotlin one and verify its version number. Uninstall the plugin and restard the IDE when needed. Install the proposed version which will be above 1.2.71. Restar AS.

##### Kotlin Safeargs

Automatic generation of getters and setters to directly access variables null safe.

#### Gradle

The project builds with the given configuration. Updating dependencies at the alpha release stage might require to replace deprecated instances' atrributes.

## JetPack Components

### Navigation

The Navigation Architecture Component simplifies the implementation of navigation in an Android app. From Activities to Activities or Fragments and also from Deeplinks.

#### Notes related to Navigation

The NavHostFragment required for pagination to work uses AndroidX as it can be seen in the XML fragment descriptions. JetPack uses the AndroidX open-source project, a major improvement to the original Android Support Library. Like the Support Library, AndroidX ships separately from the Android OS and provides backwards-compatibility across Android releases. AndroidX fully replaces the Support Library by providing feature parity and new libraries

### Paging

The Paging Library loads data gradually and gracefully within the app's. It works with the local database, a Web API service, the ViewModel and the UI.

![Paging diagram](https://user-images.githubusercontent.com/18221570/46413907-47aaee00-c722-11e8-9924-fb10ce179f84.png)

### Room persistance library

The [Room persistence library](https://developer.android.com/topic/libraries/architecture/room) provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.

#### Debuging the local Room database

A debug implementation has been added to the gradle file with the [Android debug database](https://github.com/amitshekhariitbhu/Android-Debug-Database) dependency. It is implemented while debuging and it allows to access the local room database using the browser at an http://x.x.x.x: 8080 port. Check logs to find the ip address / debug url.

### Live Data

The [Live Data](https://developer.android.com/topic/libraries/architecture/livedata) is an observable data holder class used to update the UI when changes occur to the displayed data.

### ViewModel

The [View Model](https://developer.android.com/topic/libraries/architecture/viewmodel) class is life cycle aware designed to store and manage UI-related data in a lifecycle conscious way. In other words, configuration changes on the application will not affect the View Model data except in those states where the data really needs to be updated.

## Google Api

An Account with billing enabled in the [Google Cloud console](https://accounts.google.com/ServiceLogin/signinchooser?flowName=GlifWebSignIn&flowEntry=ServiceLogin) is needed to enable the Google Apis. Google offers [up to €100.000 FREE CREDIT](https://cloud.google.com/developers/startups/) can be requested from Google Cloud to start new projects.

### Google Maps

Used to retrieve a map around the last known user location.

## Test Driven Development

Using the App wiring diagram, development starts with the [Android Testing Support library](https://developer.android.com/training/testing/) (ATSL) which has a JUnit 4-compatible test runner (AndroidJUnitRunner) and functional UI testing through Espresso and UI Automator. Different Build variants will be defined in the Gradle file for testing on a mock target or on a production device with [Espresso](https://developer.android.com/topic/libraries/testing-support-library/#Espresso) and the [Espresso tests recorder](https://developer.android.com/studio/test/espresso-test-recorder). Needed source sets under <b>/src</b> will also be created for each of the flavours. Variant filters are configured in the Gradle file.

The gradle file contains the <b>androidTestCompile</b> dependencies for the Support Library and the UI which will only be compiled during testing.

## Current status

- Initial commit with architecture summary

## License





