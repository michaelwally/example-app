# Example App

## Architecture
I implemented a MvvM type architecture and utilized a clean architecture approach where the data layer is independent of all other layers, the domain layer only depends on the data layer and the presentation layer depends on the others. I particularly like this approach as it modularizes business logic and decouples it from presentation so it is easily reuseable and testable.

## Libraries
* <b>Hilt</b> - Hilt is a dependency injection library built by Google based on Dagger. I used Hilt over Koin or Dagger 2 because of it's simplicity and I hadn't tried it before.
* <b>OkHttp</b> - OkHttp is a commonly used network client library which is easily configured and supports features such as caching.
* <b>Retrofit</b> - Retrofit is an easy to use, typesafe Http client. It can be used with OkHttp, works with coroutines and has useful features such as adding header interceptors.
* <b>GSON</b> - A common JSON parsing library that is supported with Retrofit adapters.
* <b>View Binding</b> - View binding library generates "Binding" classes per layout file which maintain references to the contained views. I used it as convenience for replacing `findViewById`.
* <b>Coroutines</b> - Coroutines are like lightweight threads. I recently learned coroutines and prefer using them to RxJava as I find them simpler and many of the RxJava mapping features exist in Kotlin.
* <b>Flow</b> - Flow allows returning multiple values asynchronously. I used it because I never have and wanted to learn.
