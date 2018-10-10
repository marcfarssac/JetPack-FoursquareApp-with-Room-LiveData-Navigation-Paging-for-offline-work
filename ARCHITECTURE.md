# Foursquare

Simple Android app that connects to the [Foursquare API]( https://developer.foursquare.com/ ) and allows the user to search for venues by entering the name of a place.

## Architecture

Data will be cached locally on devices running the App using the [Room persistence library](https://developer.android.com/topic/libraries/architecture/room). Thanks to the [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) observable data holder class, local data will be updated whenever changes occur on the backend. This will allow the user to view the list of venues also off-line. 

Since the number of venues might be very big, the App uses the [Page Library](https://developer.android.com/topic/libraries/architecture/paging/) to loads pages of data gracefully, meaning that searches resulting in big amounts of data would be called in chunks of a few items called pages. The library manages the loading of data when scrolling down the list triggering the required actions to be supplied with additional data. Calls will be made in the first place to the local database and then to the remote repository. 

An overflow menu / or a navigation drawer with Geolocation related features will let the user enable current position detection and search based on the user current position. Thanks to the [Navigation component](https://developer.android.com/topic/libraries/architecture/navigation/) a transition to a fragment with a map showing the venues'search results will be displayed. [Navigation actions](https://developer.android.com/topic/libraries/architecture/navigation/navigation-implementing) a res/navigation/mobile_navigation.xml resource and related transition files let the user navigate to the map results page. 

The [Google Maps Platform](https://cloud.google.com/maps-platform/maps/) will supply the map. An billing enabled Google Cloud account is required for this step to work (note that the [google services file](https://support.google.com/firebase/answer/7015592?hl=en) is not included in this repository for obvious billing related constraints)

Making use of the [LocationServices API](https://developers.google.com/android/reference/com/google/android/gms/location/LocationServices) the App will use the last known position of the user while trying to update it periodically when good gps signal detection is available. Different strategies are applied for devices running Android N and below or Android-O, since the last emphasizes battery drainage reduction on background services, using a Broadcast receiver instead when the App is in the Foreground. 

The App uses the [JetPack](https://developer.android.com/jetpack/) collection of Android Software Components latest versions' and features of the Android SDK and [the last stable release](https://developer.android.com/studio/releases/) of the [Android Studio] Integrated Development Environment. 

## JetPack Components

### Navigation

The Navigation Architecture Component simplifies the implementation of navigation in an Android app. From Activities to Activities or Fragments and also from Deeplinks.

#### Notes related to Navigation

The NavHostFragment required for pagination to work uses AndroidX as it can be seen in the XML fragment descriptions. JetPack uses the AndroidX open-source project, a major improvement to the original Android Support Library. Like the Support Library, AndroidX ships separately from the Android OS and provides backwards-compatibility across Android releases. AndroidX fully replaces the Support Library by providing feature parity and new libraries

### Paging

The Paging Library loads data gradually and gracefully within the app's. It works with the local database, a Web API api, the ViewModel and the UI.

![Paging diagram](https://user-images.githubusercontent.com/18221570/46413907-47aaee00-c722-11e8-9924-fb10ce179f84.png)

### Room persistence library

The [Room persistence library](https://developer.android.com/topic/libraries/architecture/room) provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.

#### Debugging the local Room database

A debug implementation has been added to the gradle file with the [Android debug database](https://github.com/amitshekhariitbhu/Android-Debug-Database) dependency. It is implemented while debugging and it allows to access the local room database using the browser at an http://x.x.x.x: 8080 port. Check logs to find the ip address / debug url.

### Live Data

The [Live Data](https://developer.android.com/topic/libraries/architecture/livedata) is an observable data holder class used to update the UI when changes occur to the displayed data.

### ViewModel

The [View Model](https://developer.android.com/topic/libraries/architecture/viewmodel) class is life cycle aware designed to store and manage UI-related data in a lifecycle conscious way. In other words, configuration changes on the application will not affect the View Model data except in those states where the data really needs to be updated.

## [Foursquare Api](https://developer.foursquare.com/)

Sophisticated location platform tools to power the way software interacts with real world. One of the many Foursquare available products will be used for this project, namely the [Places Api](https://developer.foursquare.com/places-api). After registration and setting up a project, the Client ID and Client Secret Keys are provided for the developer to make the calls.

### Places Api

This App uses Foursquare Developers [regular endpoints](https://developer.foursquare.com/docs/api/endpoints) with basic venue data. Out of the many endpoints, the [Search for Venues](https://developer.foursquare.com/docs/api/venues/search) will be used in the first place.

## Data Model

Different models for the local room database and those provided by the API. 

## Google Api

An Account with billing enabled in the [Google Cloud console](https://accounts.google.com/ServiceLogin/signinchooser?flowName=GlifWebSignIn&flowEntry=ServiceLogin) is needed to enable the Google Apis. Google offers [up to €100.000 FREE CREDIT](https://cloud.google.com/developers/startups/) can be requested from Google Cloud to start new projects.

### Google Maps

Used to retrieve a map around the last known user location.

## Author

* **Marc Farssac** - *Initial work* 4.10.2018

## Acknowledgements

* [Android developers](https://developer.android.com/docs/) - Documentation for app developers
* [Google Developers](https://developers.google.com/) - Build <b>anything</b> with Google
* [Stackoverflow](https://stackoverflow.com/) - Questions and answers

