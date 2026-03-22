# Skycast

A modern Android weather application built with Jetpack Compose and Kotlin, providing real-time weather data, multi-day forecasts, air quality monitoring, and weather-related news -- all in a clean Material 3 interface.

## Screenshots

<p align="center">
  <img src="art/Intro%20Screen.jpg" width="200" alt="Intro Screen"/>
  <img src="art/Weather%20Home%20Page.jpg" width="200" alt="Weather Home Page"/>
  <img src="art/Daily%20Weather.jpg" width="200" alt="Daily Weather"/>
  <img src="art/Forecast%20Weather.jpg" width="200" alt="Forecast Weather"/>
</p>
<p align="center">
  <img src="art/Weekly%20Weather.jpg" width="200" alt="Weekly Weather"/>
  <img src="art/Current%20AQI.jpg" width="200" alt="Current AQI"/>
  <img src="art/AQI%20Index.jpg" width="200" alt="AQI Index"/>
  <img src="art/Daily%20News.jpg" width="200" alt="Daily News"/>
</p>

## Features

- **Current Weather** -- Temperature, feels-like, wind, humidity, UV index, pressure, and precipitation for any location.
- **10-Day Forecast** -- Daily and hourly breakdowns with data from multiple providers for reliability.
- **Air Quality Index** -- Hourly and daily AQI readings with pollutant breakdowns (PM2.5, PM10, O3, NO2, SO2, CO).
- **Weather News** -- Curated weather-related articles from NewsAPI.
- **Location Management** -- GPS-based detection, city search with autocomplete, and saved favorite locations.
- **Unit Switching** -- Toggle between metric and imperial units with persistent preferences.
- **Adaptive Theming** -- Automatic light/dark theme based on local day/night conditions, plus Material You dynamic color support on Android 12+.

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM with unidirectional data flow (State-Action-Event) |
| Dependency Injection | Dagger Hilt |
| Networking | Retrofit + OkHttp |
| Local Storage | Room, SharedPreferences |
| Async | Kotlin Coroutines + Flow |
| Image Loading | Coil |
| Animations | Lottie |
| Navigation | Navigation Compose |

## Architecture

The project follows clean architecture principles with three distinct layers:

```
com.app.skycast/
|-- core/            # Base ViewModel, navigation routes, transitions
|-- data/            # API services, Room DB, DTOs, interceptors
|-- domain/          # Repository interfaces, domain models, mappers, managers
|-- presentation/    # Compose screens, UI components, theming
```

**Key patterns:**

- `BaseViewModel<State, Event, Action>` enforces unidirectional data flow across all screens.
- A custom `ResultCallAdapter` wraps every Retrofit call in `Result<T>` for type-safe error handling without try-catch.
- Domain models are fully decoupled from DTOs via dedicated mapper classes.
- Multiple weather APIs (WeatherAPI, Visual Crossing, Open-Meteo) provide redundancy and richer data.

## APIs

| Provider | Purpose |
|---|---|
| [WeatherAPI](https://www.weatherapi.com/) | Current weather and forecasts |
| [Visual Crossing](https://www.visualcrossing.com/) | Alternative forecast data |
| [Open-Meteo](https://open-meteo.com/) | Air quality metrics |
| [NewsAPI](https://newsapi.org/) | Weather-related news |
| [The Companies API](https://www.thecompaniesapi.com/) | City search and autocomplete |

## Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- JDK 17+
- Android SDK 35

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/Dracula-101/Skycast.git
   cd Skycast
   ```

2. Create a `local.properties` file in the project root (if not already present) and add your API keys:
   ```properties
   WEATHER_API_KEY=your_weatherapi_key
   WEATHER_API_BASE_URL=https://api.weatherapi.com
   VISUAL_CROSSING_API_KEY=your_visual_crossing_key
   VISUAL_CROSSING_BASE_URL=https://weather.visualcrossing.com
   NEWS_API_KEY=your_newsapi_key
   NEWS_API_BASE_URL=https://newsapi.org/
   CITY_API_BASE_URL=https://api.thecompaniesapi.com
   AQI_API_BASE_URL=https://air-quality-api.open-meteo.com
   ```

3. Build and run:
   ```bash
   ./gradlew assembleDebug
   ```
   Or open the project in Android Studio and run on a device/emulator (API 24+).

## Build Variants

| Variant | Description |
|---|---|
| `debug` | Development build with logging enabled |
| `release` | ProGuard-minified build with signing (requires `key.properties`) |

## Requirements

- Minimum SDK: 24 (Android 7.0)
- Target SDK: 34 (Android 14)

## License

This project is for educational and personal use.
