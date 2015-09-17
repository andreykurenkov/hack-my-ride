# TransitTimes (hackmyride2 submission)

Beacon-based instant info about when the bus or train get to your stop, via an app, smartwatch, or website. 
The GTFS and GTFS real time  feed datasets from VTA were used in implementing this.

The core of the idea is to provide riders on public transit systems with information about upcoming bus or train arrivals as soon as they get to the station. This is done with an Eddystone-compatible Bluetooth Beacon, which can transmit both a UID and a URL. The UID can be picked up by an Android app (the apk can be [gotten here](https://drive.google.com/file/d/0B6Vx0T-KbUreczNFVktpdmtsQXc/view?usp=sharing), which will notify the user with information about the stop. Information about stops is gotten by importing the GTFS feed published by VTA into a PostGIS enabled database on a Django server, which enables the app to get the GTFS information via REST calls. In this way, the app is easily extensible to transit systems all over the world. 

Beyond supplying the notification with stop times when a Beacon is seen, the app has much additional functionality to leverage the data imported from GTFS. The app provides a general Browse interface - an Agency (among those imported into the Django server, which should support all systems with GTFS feeds) can be selected  using a Drawer interface and its routes and stops can be browsed freely and easily. Secondly, there is a Nearby tab which uses the Location of the phone to show a list of nearby transit stops, and to allow to find out info about them. The third tab is the Beacon tab - if the beacon notification is clicked, it brings the user to a more detailed view of the stop and the stop times. The last major functionality of the app is to interface with a Pebble app, so that whenever a new beacon is seen all the info shown on the phone is also shown on a Pebble app - the phone does not even need to be looked at. 

Notably, the app queries Django for both the stores scheduled time and realtime predictions - the Django server itsels gets the predictions from the the [511 tripupdates REST endpoints](http://511.org/vta/). If predictions are not available for that stop time, only the scheduled time is shown, and otherwise both are shown. The REST api-doc is an excellent way to explore the realtime API and other things the App leverages. 

The other major components of TransitTimes is the Django frontend. Because not all people will have the app installed, but can see the Eddystone URL [without](http://beekn.net/2015/07/google-eddystone-jumps-browser-ios/) a specific app to detect it (this functionality will come to Android Chrome soon, but for now is done with the Physical Web app). So, the beacon can be configured to have a URL that points to the same stop page as the UID identifies. The web page could also inform the user of the app, which is beneficial to install due to notifications that tell the time immediately without going to a website, the Nearby future, Pebble support, and more. Finally, the REST API is already quite extensive and could power other GTFS-powered apps, such as for instance ones intended to visualize schedules or compare fares. 

Note:
(Django) Needed installation beyond virtualenv:
https://docs.djangoproject.com/en/1.8/ref/contrib/gis/install/geolibs/
https://docs.djangoproject.com/en/1.8/ref/contrib/gis/install/postgis/
https://github.com/google/protobuf
