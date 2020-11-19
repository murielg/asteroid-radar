# AsteroidRadar

AsteroidRadar is an app to view a list of asteroids detected by NASA that pass near Earth, you can view all the detected asteroids in a period of time, their data (Size, velocity, distance to Earth) and whether or not they are potentially hazardous. 

# Features

- Single-activity architecture with Navigation component to manage fragment transitions and operations.
- Room implementation with LiveData, TypeConverters for date and Transformations
- Periodic caching and cleaning data in the background using WorkManager
- Reactive UIs with encapsulated LiveData and Data Binding
- Retrofit, Moshi and Coroutines


This kind of app is one of the most usual in the real world, what you will learn by doing this are some of the most fundamental skills you need to know to work as a professional Android developer, as fetching data from the internet, saving data to a database, and display the data in a clear, clear, compelling UI.

### Screenshots

[<img src="/screenshots/asteroid_list.png" width="250"/>](/screenshots/asteroid_list.png)
[<img src="/screenshots/hazardous_detail.png" width="250"/>](/screenshots/hazardous_detail.png)
[<img src="/screenshots/not_hazardous_detail.png" width="250"/>](/screenshots/not_hazardous_detail.png)
[<img src="/screenshots/options_menu.png" width="250"/>](/screenshots/options_menu.png)

### Instructions

- Add the NASA API Key in local.properties `NASA_API_KEY="ADD_KEY_HERE"`

## Built With

- NASA NeoWS (Near Earth Object Web Service) [https://api.nasa.gov/](https://api.nasa.gov/)

